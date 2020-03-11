package com.server.module.trade.web.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.server.module.footmark.FootmarkBean;
import com.server.module.footmark.FootmarkDao;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.dao.OrderDao;
import com.server.module.trade.order.vo.OpenDoorVo;
import com.server.module.trade.util.UserUtil;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.util.HttpUtil;
import com.server.util.ReviseUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.ModeEnum;

@Service
public class MachinesControlService {
	
	private static final Logger log = LogManager.getLogger(MachinesControlService.class);

	@Autowired
	private OrderDao orderDao;
	@Value("${heartBeatHost}")
	private String heartBeatHost;
	@Autowired
	private FootmarkDao footmarkDao;
	
	
	
	
	
	public String restart(String vmCode){
		log.info(vmCode+"--<MachinesControlService--restart--start>");
		String returnValue= null;
		UserVo userVo = UserUtil.getUser();
		//获取重启机器命令
		String restartCommand = ReviseUtil.restartCommand();
		
		String factoryNumber = orderDao.getFactoryNumberByVmCode(vmCode);
		if(StringUtil.isBlank(factoryNumber)){
			log.info(vmCode+"：该机器出厂编号为空，无法向机器发送命令操作,开门用户信息："+JsonUtil.toJson(userVo));
			return returnValue;
		}
		factoryNumber = factoryNumber!=null?factoryNumber.trim():null;
		OpenDoorVo openDoorVo = new OpenDoorVo();
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http").setHost(heartBeatHost).setPath("/openDoor").build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		openDoorVo.setFromClient((userVo!=null && userVo.getId()!=null)?userVo.getId().toString():null);
		openDoorVo.setMessage(restartCommand);
		openDoorVo.setToClient(factoryNumber);
		returnValue = HttpUtil.post(uri, JsonUtil.toJson(openDoorVo));
		FootmarkBean footmark = new FootmarkBean(userVo, ModeEnum.RESTART_MODE, vmCode);
		Long id = footmarkDao.insertFootmark(footmark);
		orderDao.saveHeartBeatLog(restartCommand, factoryNumber, 's',userVo==null?null:userVo.getId(),userVo==null?null:userVo.getType(),id);
		return returnValue;
	}
}
