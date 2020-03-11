package com.server.module.trade.web.open;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.server.client.MachinesClient;
import com.server.module.activeDegree.UserActiveDegreeService;
import com.server.module.commonBean.ItemInfoDto;
import com.server.module.distroyMachines.DistroyMachinesLogService;
import com.server.module.footmark.FootmarkService;
import com.server.module.payRecord.BlankOrderCreator;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.bean.PayRecordBean;
import com.server.module.trade.order.dao.OrderDao;
import com.server.module.trade.order.dao.PayRecordDao;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.order.domain.Way;
import com.server.module.trade.order.vo.OpenDoorVo;
import com.server.module.trade.vendingMachinesWayItem.VmwayItemDao;
import com.server.module.trade.web.bean.CreateOrderBean;
import com.server.module.trade.web.bean.CreateOrderResultBean;
import com.server.module.zfb_trade.bean.DoorOperateHistoryBean;
import com.server.module.zfb_trade.convert.Msg2DoorOperateConverter;
import com.server.module.zfb_trade.module.dooroperation.AliDoorOperationDao;
import com.server.module.zfb_trade.module.vmbase.AliVendingMachinesDao;
import com.server.module.zfb_trade.module.vmbase.VmbaseInfoDto;
import com.server.module.zfb_trade.module.vmway.AliVmwayDao;
import com.server.module.zfb_trade.util.ResultUtil;
import com.server.module.zfb_trade.util.enumparam.DoorOperateEventEnum;
import com.server.module.zfb_trade.util.enumparam.ResultEnum;
import com.server.redis.MachinesKey;
import com.server.redis.NormalRedisKey;
import com.server.redis.RedisService;
import com.server.util.HttpUtil;
import com.server.util.PayCodeUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.stateEnum.CommandVersionEnum;
import com.server.util.stateEnum.DistroyTypeEnum;
import com.server.util.stateEnum.ModeEnum;
import com.server.util.stateEnum.RegisterFlowEnum;
import com.server.util.stateEnum.TransferStateEnum;

@Service
public class CusOpenServiceImpl implements CusOpenService{
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private MachinesClient machinesClient;
	@Autowired
	private RedisService redisService;
	@Autowired
	private AliDoorOperationDao doorOperationDao;
	@Autowired
	private AliVmwayDao vmwayDao;
	@Autowired
	private BlankOrderCreator blankOrderCreator;
	@Autowired
	private VmwayItemDao vmwayItemDao;
	@Autowired
	private FootmarkService footmarkService;
	@Autowired
	private UserActiveDegreeService userActiveDegreeService;
	@Autowired
	private AliVendingMachinesDao vendingMachinesDao;
	@Autowired
	private PayRecordDao payRecordDaoImpl;
	@Autowired
	private DistroyMachinesLogService distroyMachinesLogService;
	
	private final static Logger log = LogManager.getLogger(CusOpenServiceImpl.class);

	/*	
	 * 空白订单生成
	*/
	@Override
	public ReturnDataUtil isOpened(String vmCode) {

		boolean isOpened = false;String goods=null;
		String wayNumber = null; 
		VmbaseInfoDto baseInfo = vendingMachinesDao.getBaseInfo(vmCode);
		String heartbeat = getHeartbeatStr(baseInfo.getFactoryNumber());
		if(CommandVersionEnum.VER1.getState().equals(baseInfo.getMachinesVersion())){
			if(heartbeat!=null){
				if(heartbeat.contains("s:2")){
					isOpened = true;
				}else if(heartbeat.contains("s:4")){
					//记录门锁损坏
					recordLockBreakdown(baseInfo);
				}
			}
		}else if (CommandVersionEnum.VER2.getState().equals(baseInfo.getMachinesVersion())){
			 goods = redisService.get(NormalRedisKey.GOODS,baseInfo.getFactoryNumber());
			if(StringUtils.isNotBlank(goods)){
				wayNumber = redisService.get(NormalRedisKey.CurrWay, vmCode);
				isOpened = goods.contains("goods:"+wayNumber);
			}
			if(heartbeat!=null && heartbeat.contains("s:4")){
				//记录门锁损坏
				recordLockBreakdown(baseInfo);
			}
		}else{
			return ResultUtil.error(ResultEnum.MACHINES_VERSION_ERROR);
		}
		if(isOpened){
			if(vmCode.equals("19990000001")) {
				return ResultUtil.success();
			}
			else if(redisService.setIfNotExists("blank"+vmCode, "1", 3)){
				String historyId = redisService.get(NormalRedisKey.OperateHistoryId,vmCode);
				UserVo userVo = redisService.get(MachinesKey.currentMachinesUser,vmCode,UserVo.class);
				if(StringUtils.isBlank(wayNumber)){
					wayNumber = redisService.get(NormalRedisKey.CurrWay, vmCode);
				}
				if(StringUtils.isNotBlank(historyId) && StringUtils.isNotBlank(wayNumber) && userVo != null){
					Long operateId = Long.valueOf(historyId);
					PayRecordBean byOperateId = payRecordDaoImpl.getByOperateId(operateId);
					if(byOperateId == null){
						createBlankOrder(baseInfo, userVo, vmCode, Integer.valueOf(wayNumber), operateId);
					}
				}
			}
			redisService.del(NormalRedisKey.improperWeight,vmCode);
			return ResultUtil.success();
		}else{
			//log.error("无法生成空白订单时心跳:"+vmCode+"---"+heartbeat+"---"+goods);
		}
		return ResultUtil.error();
	}

	@Override
	public ReturnDataUtil  openDoor(VmbaseInfoDto baseInfo,String vmCode, Integer wayNumber,UserVo userVo,Integer minStandard) {
		//防止重启请求
		boolean setIfNotExists = redisService.setIfNotExists("currmachine"+vmCode, "1",10);
		try{
			if(!setIfNotExists){
				return ResultUtil.error(ResultEnum.MECHINE_NOT_FREE);
			}
			//判断当前机器状态是否可以开门
			ResultEnum canOpenHMRedis = canOpenHMRedis(baseInfo,userVo,wayNumber);
			if(canOpenHMRedis != ResultEnum.SUCCESS){
				return ResultUtil.error(canOpenHMRedis);
			}
			//获取当前机器心跳
			String openHeart = getHeartbeatStr(baseInfo.getFactoryNumber());
			//生成开门命令
			String command = genCommandStr(baseInfo.getFactoryNumber(), wayNumber, baseInfo.getTotalWayNum());
			MachineInfo info = new MachineInfo(openHeart);
			Way way2 = info.getList().get(wayNumber-1);
			if(way2 == null || way2.getWeight()<(minStandard/2)){
				//如果当前货道重量不及当前商品规格一半，则提示用户该商品暂停销售
				return ResultUtil.error(ResultEnum.DOOR_NOT_AVAILABLE);
			}
			
/*			if(vmCode.equals("19990000001")) {
				//https://api.mch.weixin.qq.com/v3/payscore/user-service-state get请求
				//service_id appid openid
				//use_service_state UNAVAILABLE AVAILABLE
				URIBuilder url1 = null;
				try {
					url1 = new URIBuilder("https://api.mch.weixin.qq.com/v3/payscore/user-service-state");
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				url1.addParameter("service_id","");
				url1.addParameter("appid", "5");
				url1.addParameter("openid","3");
				String jsonResult1 = HttpUtil.get(url1);
				Map<String, Object> map=JSONObject.parseObject(jsonResult1);
				String state=(String) map.get("use_service_state");
				log.info(userVo.getId()+"检测是否开通支付分"+state);
				if(state.equals("AVAILABLE")) {//创建订单
					log.info(userVo.getId()+"已开通支付分");
					//创建订单https://api.mch.weixin.qq.com/v3/payscore/smartretail-orders
					String genPayCode = PayCodeUtil.genPayCode(1, vmCode);
					CreateOrderBean order = new CreateOrderBean();
					order.setAppid("appid");
					order.setOut_order_no(genPayCode);
					order.setService_start_time("OnAccept");
					order.setService_start_location("优水售货机设备附近");
					order.setRisk_amount("200");
					order.setService_id("service_id");
					order.setService_introduction("优水免密支付");
					order.setAttach(userVo.getId().toString());
					log.info(JSON.toJSONString(order));
					URIBuilder uri = null;
					try {
						uri = new URIBuilder("https://api.mch.weixin.qq.com/v3/payscore/smartretail-orders");
					} catch (URISyntaxException e) {
						log.error("URISyntaxException",e);
						e.printStackTrace();
					}
					String returnValue = HttpUtil.post(uri.toString(),JSON.toJSONString(order));
					CreateOrderResultBean result=JSONObject.parseObject(returnValue,CreateOrderResultBean.class);
					
				
					createBlankOrderV3(baseInfo, userVo, vmCode, Integer.valueOf(wayNumber), genPayCode);
					//等待回调确认订单
					return ResultUtil.success(ResultEnum.WECHAT_ERROR,returnValue);

				}else {
					return ResultUtil.error(ResultEnum.WECHAT_ERROR);
				}
			}*/
			
			//转换为开门历史记录bean
			DoorOperateHistoryBean doorOperateHistory = Msg2DoorOperateConverter.setDoorOperateHistoryData(info, wayNumber,
					vmCode, userVo);
			// 保存开门记录
			DoorOperateHistoryBean save = doorOperationDao.save(doorOperateHistory);
			ResultEnum result = null;
			//发送开门命令
			redisService.set("ServiceOrderFlag_"+baseInfo.getFactoryNumber(),"1",3);
			String returnValue = machinesClient.sendCommandTimes(userVo, baseInfo.getFactoryNumber(), command,2);
			if(TransferStateEnum.SUCCESS.getState().equals(returnValue)){
				redisService.del("ServiceOrderFlag_"+baseInfo.getFactoryNumber());
				result = ResultEnum.SUCCESS;
				if (CommandVersionEnum.VER2.getState().equals(baseInfo.getMachinesVersion())) {
					result = ResultEnum.NEW_OPEN_SUCCESS;
				}
				//更新用户首次开门时间
				userActiveDegreeService.update(userVo.getId(),RegisterFlowEnum.OPEN_MACHINES);
				//设置redis标志位及更新开关门标志位
				setRedisFlagAndUpdateHistory(baseInfo, vmCode, wayNumber, save, userVo);
			}else{
				//开门失败
				result = ResultEnum.ERROR;
				save.setOpenFailedTime(new Date());
				save.setStatus(DoorOperateEventEnum.FAILED.getCode());
				save.setRemark("开门失败");
				doorOperationDao.update(save);
			}
			//记录足迹
			String enterIdString = redisService.getString("EnterId"+userVo.getOpenId());
			Long enterId = enterIdString==null?null:Long.valueOf(enterIdString);
			Long id = footmarkService.insertFootmarkAndRedis(userVo, ModeEnum.CHANGE_STOCK_MODE, vmCode,enterId);
			//保存开门命令
			orderDao.saveHeartBeatLog(command, baseInfo.getFactoryNumber(), 'o', userVo.getId(), userVo.getType(),id);
			return ResultUtil.result(result);
		}finally{
			redisService.del("currmachine"+vmCode);
		}
	}
	
	
	
	
	
	

	@Override
	public ReturnDataUtil openDoorV3(VmbaseInfoDto baseInfo,String vmCode, Integer wayNumber,UserVo userVo) {
		//获取当前机器心跳
		String openHeart =redisService.getString("HM-" + baseInfo.getFactoryNumber()); 
		//生成开门命令
		String command = genCommandStr(baseInfo.getFactoryNumber(), wayNumber, baseInfo.getTotalWayNum());
		MachineInfo info = new MachineInfo(openHeart);
		
		//转换为开门历史记录bean
		DoorOperateHistoryBean doorOperateHistory = Msg2DoorOperateConverter.setDoorOperateHistoryData(info, wayNumber,
				vmCode, userVo);
		// 保存开门记录
		DoorOperateHistoryBean save = doorOperationDao.save(doorOperateHistory);
		ResultEnum result = null;
		//发送开门命令
		redisService.set("ServiceOrderFlag_"+baseInfo.getFactoryNumber(),"1",3);
		String returnValue = machinesClient.sendCommandTimes(userVo, baseInfo.getFactoryNumber(), command,2);
		if(TransferStateEnum.SUCCESS.getState().equals(returnValue)){
			redisService.del("ServiceOrderFlag_"+baseInfo.getFactoryNumber());
			result = ResultEnum.SUCCESS;
			if (CommandVersionEnum.VER2.getState().equals(baseInfo.getMachinesVersion())) {
				result = ResultEnum.NEW_OPEN_SUCCESS;
			}
			//更新用户首次开门时间
			userActiveDegreeService.update(userVo.getId(),RegisterFlowEnum.OPEN_MACHINES);
			//设置redis标志位及更新开关门标志位
			setRedisFlagAndUpdateHistory(baseInfo, vmCode, wayNumber, save, userVo);
		}else{
			//开门失败
			result = ResultEnum.ERROR;
			save.setOpenFailedTime(new Date());
			save.setStatus(DoorOperateEventEnum.FAILED.getCode());
			save.setRemark("开门失败");
			doorOperationDao.update(save);
		}
		//记录足迹
		String enterIdString = redisService.getString("EnterId"+userVo.getOpenId());
		Long enterId = enterIdString==null?null:Long.valueOf(enterIdString);
		Long id = footmarkService.insertFootmarkAndRedis(userVo, ModeEnum.CHANGE_STOCK_MODE, vmCode,enterId);
		//保存开门命令
		orderDao.saveHeartBeatLog(command, baseInfo.getFactoryNumber(), 'o', userVo.getId(), userVo.getType(),id);		
		return ResultUtil.result(result);
	}
	/**
	 * 根据redis中的心跳，查看是否可以开门
	 * @author hebiting
	 * @date 2019年1月18日上午10:49:51
	 * @param factoryNum
	 * @param userVo
	 * @param vmCode
	 * @return
	 */
	public ResultEnum canOpenHMRedis(VmbaseInfoDto baseInfo,UserVo userVo,Integer wayNumber){
		String heartBeat = redisService.getString("HM-"+baseInfo.getFactoryNumber());
		if(heartBeat == null){
			return ResultEnum.ERROR;
		}
		Boolean exists = redisService.exists(NormalRedisKey.OHM, baseInfo.getFactoryNumber());
		if(exists){
			UserVo lastUser = redisService.get(MachinesKey.currentMachinesUser,baseInfo.getVmCode(),UserVo.class);
			if(lastUser != null && !lastUser.equals(userVo)){
				return ResultEnum.MECHINE_NOT_FREE;
			}else{
				String[] door = new String[baseInfo.getTotalWayNum()];
				Arrays.fill(door, "0");
				String join = StringUtils.join(door,",");
				if(!heartBeat.contains(join)){
					String wayString = redisService.get(NormalRedisKey.CurrWay,baseInfo.getVmCode());
					if(StringUtils.isNotBlank(wayString)){
						if(wayString.equals(wayNumber.toString())){
							return ResultEnum.MACHINES_IS_OPENED;
						}else{
							return ResultEnum.MACHINES_PRE_DOOR_NOT_CLOSED;
						}
					}
				}
			}
		}else {
			redisService.del(NormalRedisKey.OHM, baseInfo.getFactoryNumber());
		}
		return ResultEnum.SUCCESS;
	}
	/**
	 * 设置redis标志位并更新开关门记录信息
	 * @author hebiting
	 * @date 2019年1月18日上午10:50:20
	 * @param baseInfo
	 * @param vmCode
	 * @param wayNumber
	 * @param save
	 * @param userVo
	 */
	private void setRedisFlagAndUpdateHistory(VmbaseInfoDto baseInfo,String vmCode,Integer wayNumber,
			DoorOperateHistoryBean save,UserVo userVo){
		redisService.del(NormalRedisKey.orderReuslt,vmCode);
		redisService.set(NormalRedisKey.openflag,baseInfo.getFactoryNumber(), "1");
		//redisService.set(NormalRedisKey.closeflag,baseInfo.getFactoryNumber(),"1");
		// 如果门已经打开
		save.setStatus(DoorOperateEventEnum.OPENED.getCode());
		save.setOpenedTime(new Date());
		doorOperationDao.update(save);
		// 设置当前机器的用户
		redisService.set(MachinesKey.currentMachinesUser, vmCode, userVo);
		// 设置当前机器货道
		redisService.set(NormalRedisKey.CurrWay,vmCode, wayNumber + "");
		// 设置门的在redis里的状态
		redisService.set(MachinesKey.currentMachinesOpenState, vmCode, "the machine is opened");
		// 设置开门时的开门历史记录表的id
		redisService.set(NormalRedisKey.OperateHistoryId,vmCode, save.getId().toString());
		//设置开门标志位
		redisService.set(NormalRedisKey.OHM,baseInfo.getFactoryNumber(), "open");
		//设置用于非正常情况下，获取到之前用户信息
		redisService.set(MachinesKey.improperMachinesUser, vmCode, userVo);
		// 设置非正常情况下记录当前机器货道
		redisService.set(NormalRedisKey.improperWay,vmCode, wayNumber.toString());
	}
	/**
	 * 创建空白订单
	 * @author hebiting
	 * @date 2019年1月30日下午2:19:01
	 * @param baseInfo
	 * @param userVo
	 * @param vmCode
	 * @param wayNumber
	 * @param historyId
	 */
	public void createBlankOrder(VmbaseInfoDto baseInfo,UserVo userVo,String vmCode,Integer wayNumber,
			Long historyId){
		if(userVo.getType() == UserVo.USER_CUSTOMER){
			PayRecordBean createPayRecord = blankOrderCreator.createPayRecord(userVo, vmCode, wayNumber,historyId);
			List<ItemInfoDto> oneWayItmeInfo = null;
			if(CommandVersionEnum.VER1.getState().equals(baseInfo.getMachinesVersion())){
				oneWayItmeInfo = vmwayDao.getOneWayItemInfo(vmCode, wayNumber);
			}else{
				oneWayItmeInfo = vmwayItemDao.getOneWayItmeInfo(vmCode,wayNumber);
			}
			blankOrderCreator.createPayRecordItemByItemInfo(oneWayItmeInfo, createPayRecord.getId());
			redisService.set("CurrOrderId"+vmCode,createPayRecord.getId().toString(),1200);
		}
	}
	
	public void createBlankOrderV3(VmbaseInfoDto baseInfo,UserVo userVo,String vmCode,Integer wayNumber,
			String genCode){
		if(userVo.getType() == UserVo.USER_CUSTOMER){
			PayRecordBean createPayRecord = blankOrderCreator.createPayRecordV3(userVo, vmCode, wayNumber,genCode);
			List<ItemInfoDto> oneWayItmeInfo = null;
			if(CommandVersionEnum.VER1.getState().equals(baseInfo.getMachinesVersion())){
				oneWayItmeInfo = vmwayDao.getOneWayItemInfo(vmCode, wayNumber);
			}else{
				oneWayItmeInfo = vmwayItemDao.getOneWayItmeInfo(vmCode,wayNumber);
			}
			blankOrderCreator.createPayRecordItemByItemInfo(oneWayItmeInfo, createPayRecord.getId());
			redisService.set("CurrOrderId"+vmCode,createPayRecord.getId().toString(),1200);
		}
	}
	
	/**
	 * 获取心跳
	 * @author hebiting
	 * @date 2019年1月18日上午10:50:35
	 * @param factoryNumber
	 * @return
	 */
	private String getHeartbeatStr(String factoryNumber) {
		String content = redisService.getString("HM-" + factoryNumber);
		return content;
	}
	
	// 获取开门的校验码
	public static String genCommandStr(String factoryNumber, int way, int totalWayNum) {
		// //n:111380310016;d:0,1,0,0;v:99&44$
		StringBuilder sb = new StringBuilder();
		sb.append("n:").append(factoryNumber);
		sb.append(";d:");
		// String doorstr="0,0,0,0";
		for (int i = 1; i <= totalWayNum; i++) {
			if (i == way) {
				sb.append("1");
			} else {
				sb.append("0");
			}
			if (i != totalWayNum) {
				sb.append(",");
			}
		}
		int len = sumString(factoryNumber) + 1 + 18;
		sb.append(";v:99&" + len + "$");
		return sb.toString();
	}

	public static int sumString(String s) {
		int len = s.length();
		int sum = 0;
		for (int i = 0; i < len; i++) {
			StringBuffer sb = new StringBuffer();
			char c = s.charAt(i);
			sb.append(c);
			sum += Integer.parseInt(sb.toString());
		}
		return sum;
	}
	/**
	 * 记录门锁损坏
	 * @author hebiting
	 * @date 2019年2月18日下午3:43:03
	 * @param baseInfo
	 */
	public void recordLockBreakdown(VmbaseInfoDto baseInfo){
		DistroyTypeEnum type = DistroyTypeEnum.MACHINES_LOCK_BREAKDOWN;
		String wayNumber = redisService.get(NormalRedisKey.CurrWay, baseInfo.getVmCode());
		String info = String.format(type.getInfo(), wayNumber);
		distroyMachinesLogService.insert(baseInfo.getVmCode(), baseInfo.getFactoryNumber(),info,type.getStatus().toString());
	}

}
