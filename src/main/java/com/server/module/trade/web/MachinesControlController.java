package com.server.module.trade.web;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.server.command.StandardCommandCreator;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.OrderService;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.util.ResultBean;
import com.server.module.trade.util.UserUtil;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;
import com.server.module.trade.vendingMachinesWayItem.VmwayItemService;
import com.server.module.trade.web.service.MachinesControlService;
import com.server.module.zfb_trade.module.replenish.ReplenishService;
import com.server.module.zfb_trade.module.vminfo.AliVminfoService;
import com.server.module.zfb_trade.module.vminfo.VminfoDto;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.redis.MachinesKey;
import com.server.redis.RedisService;
import com.server.util.StringUtil;

/**
 * 机器控制
 * 
 * @author hebiting
 *
 */
@RestController
@RequestMapping("/machineControl")
public class MachinesControlController {
	
	private final static Logger log = LogManager.getLogger(MachinesControlController.class);

	@Autowired
	private MachinesControlService machinesControlService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private VmwayItemService vmwayItemService;
	@Autowired
	private ReplenishService replenishService;
	@Autowired
	private StandardCommandCreator standardCreator;
	@Autowired
	private AliVminfoService vminfoService;
	
//	@GetMapping("/send")
//	public void send(){
//		UserVo userVo = UserUtil.getUser();
//		// 检查是否登录
//		if (userVo == null) {
//			return ;
//		}
//		if (userVo.getType() != UserVo.USER_ADMIN) {
//			return ;
//		}
//		List<VminfoDto> queryVMList = vminfoService.queryVMList(null, "1,100,105,111,145,149,150,153,76,83,103,104,109,110,112,113,120,121,123,130,141,142,143,147,152,84,114,116,117,118,119,131,144,146,148,151", null, 20001);
//		System.out.println("机器总数："+queryVMList.size());
//		for(VminfoDto vm:queryVMList){
//			try{
//				String vmCode = vm.getVmCode();
//				Map<String, Object> base = orderService.getBaseInfoByVmCode(vmCode);
//				String result = standardCreator.sendStandard((String)base.get("factoryNumber"), (Integer)base.get("machineVersion"), userVo, null);
//				System.out.println("machines="+vmCode+",result="+result);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//	}


	@PostMapping("/restart")
	public ResultBean<?> restart(String vmCode) {
		ResultBean<?> result = new ResultBean<>();
		UserVo userVo = UserUtil.getUser();
		// 检查是否登录
		if (userVo == null) {
			result.setCode(ResultBean.NO_LOGIN);
			result.setMsg("未登录");
			return result;
		}
		if (userVo.getType() != UserVo.USER_ADMIN) {
			result.setCode(ResultBean.NO_LOGIN);
			result.setMsg("非运维人员,不能进行操作");
			return result;
		}
		//发送重启机器命令
		String openFlag = machinesControlService.restart(vmCode);
		//返回状态0：成功，1：失败
		if (String.valueOf("0").equals(openFlag)) {
			String factoryNum = orderService.getFactoryNumberByVmCode(vmCode);
			redisService.del("OHM-"+factoryNum);
			redisService.del(MachinesKey.currentMachinesUser,vmCode);
			result.setMsg("重启成功");
			return result;
		} else {
			result.setCode(ResultBean.FAIL);
			result.setMsg("重启失败");
			return result;
		}
	}
	
	@PostMapping("/adjustReplenish")
	public ResultBean<?> adjustReplenish(String vmCode,Integer wayNum,Long basicItemId,Integer adjustNum){
		ResultBean<?> result = new ResultBean<>();
		UserVo userVo = UserUtil.getUser();
		if (userVo == null) {
			result.setCode(ResultBean.NO_LOGIN);
			result.setMsg("未登录");
			return result;
		}
		if (userVo.getType() != UserVo.USER_ADMIN) {
			result.setCode(ResultBean.NO_LOGIN);
			result.setMsg("非运维人员,不能进行操作");
			return result;
		}
		Long logId = replenishService.getCurrentReplenish(vmCode, wayNum, basicItemId, userVo.getId());
		boolean adjustResult = false;
		if(logId != null && logId>0){
			adjustResult = replenishService.adjustReplenishNum(adjustNum,logId);
		}
		if(adjustResult){
			result.setMsg("修正补货成功");
			return result;
		}else{
			result.setCode(ResultBean.FAIL);
			result.setMsg("修正补货值失败");
		}
		return result;
	}
	
	/**
	 * 获取补货信息
	 * @author hebiting
	 * @date 2018年9月20日下午3:38:31
	 * @param vmCode
	 * @return
	 */
	@GetMapping("/getReplenishInfo")
	public ResultBean<List<ItemChangeDto>> getReplenishInfo(String vmCode){
		ResultBean<List<ItemChangeDto>> result = new ResultBean<>();
		String json = redisService.getString("replenish_"+vmCode);
		List<ItemChangeDto> data = null;
		if(StringUtil.isNotBlank(json)){
			data = JsonUtil.toObject(json, new TypeReference<List<ItemChangeDto>>(){});
			redisService.del("replenish_"+vmCode);
		}
		result.setData(data);
		return result;
	}
	
	/**
	 * 获取货道商品增加或减少时的信息
	 * @author hebiting
	 * @date 2018年9月4日下午5:14:51
	 * @param vmCode
	 * @return
	 */
	@GetMapping("/getChangeInfo")
	public ResultBean<Map<String,Object>> getChangeInfo(String vmCode){
		UserVo user = UserUtil.getUser();
		ResultBean<Map<String,Object>> result = new ResultBean<Map<String,Object>>();
		List<ItemChangeDto> itemList = null;
		String facNum = orderService.getFactoryNumberByVmCode(vmCode);
		String key = "GOODS_"+facNum;
		//goods:1;1:+0;goods:2;1:+0;goods:3;1:-0,2:-0;goods:4;1:-0;all_closed
		//goods:2;1:-1,2: -2,3:+1,4:-5;end  
		//goods:2;1:-1,2: -2,3:+1,4:-5
		String command = redisService.getString(key);
		BigDecimal totalPrice = new BigDecimal(0);
		log.info("货道数量变化:"+vmCode+"-getChangeInfo--"+command);
		if(StringUtil.isNotBlank(command)){
			MachineInfo parseInfo = MachineInfo.parseParamGetMachineInfo(command);
			Iterator<ItemChangeDto> iterator = parseInfo.getItemChangeList().iterator();
			while(iterator.hasNext()){
				ItemChangeDto changeInfo = iterator.next();
				changeInfo.setVmCode(vmCode);
				if(vmwayItemService.getChangeInfo(changeInfo) == null){
					iterator.remove();
				}else{
					if(changeInfo.getChangeNum()<0){
						totalPrice = totalPrice.add(new BigDecimal(Math.abs(changeInfo.getChangeNum())).multiply(new BigDecimal(changeInfo.getPrice().toString())));
					}
				}
			}
			itemList = parseInfo.getItemChangeList();
		}
		Map<String,Object> data = new HashMap<String,Object>();
		if(UserVo.USER_CUSTOMER == user.getType()){
			data.put("totalPrice", totalPrice);
		}
		data.put("itemList", itemList);
		result.setData(data);
		return result;
	}
	
}
