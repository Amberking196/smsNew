package com.server.command;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.client.MachinesClient;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.dao.OrderDao;
import com.server.module.trade.order.vo.OpenDoorVo;
import com.server.module.trade.vendingMachinesWay.VendingMachinesWayService;
import com.server.module.trade.vendingMachinesWayItem.VmwayItemService;
import com.server.redis.RedisService;
import com.server.util.stateEnum.CommandVersionEnum;


@Component
public class StandardCommandCreator {

	private final static Logger log = LogManager.getLogger(StandardCommandCreator.class);
	
	@Autowired
	private VmwayItemService vmwayItemService;
	@Autowired
	private VendingMachinesWayService vmwayService;
	@Autowired
	private MachinesClient machinesClient;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private RedisService redisService;
	
	private String productInfo = "productInfo";
	
	public String sendStandard(String factnum,UserVo userVo,Long footmarkId){
		Integer version = orderDao.getVersionByFactoryNum(factnum);
		return sendStandard( factnum, version, userVo, footmarkId);
	}
	
	public String sendStandard(String factnum,Integer version,UserVo userVo,Long footmarkId){
		Boolean exists = redisService.exists("OHM-" + factnum);
		if(exists){
			return "1";
		}
		String command = null;
		String result = "0";
		if(CommandVersionEnum.VER1.getState().equals(version)){
			command = vmwayService.findItemStandard(factnum);
		}else if(CommandVersionEnum.VER2.getState().equals(version)){
			command = vmwayItemService.getMultilayerCommand(factnum);
		}
		if(StringUtils.isNotBlank(command)){
			OpenDoorVo openDoor = new OpenDoorVo();
			openDoor.setFromClient(productInfo);
			openDoor.setMessage(command);
			openDoor.setToClient(factnum);
			result = machinesClient.sendCommand(openDoor);
			log.info(factnum+"发送规格参数结果:"+result);
			if(userVo != null){
				orderDao.saveHeartBeatLog(command, factnum, 't', userVo.getId(), userVo.getType(), footmarkId);
			}
		}
		return result;
	}
	
}
