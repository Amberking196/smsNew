package com.server.module.trade.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.mysql.jdbc.log.Log;
import com.server.client.MachinesClient;
import com.server.module.commonBean.ItemInfoDto;
import com.server.module.footmark.FootmarkBean;
import com.server.module.footmark.FootmarkService;
import com.server.module.machinesBadOpenLog.MachinesBadOpenLogBean;
import com.server.module.machinesBadOpenLog.MachinesBadOpenLogService;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.order.OrderService;
import com.server.module.trade.order.bean.PayRecordBean;
import com.server.module.trade.order.dao.OrderDao;
import com.server.module.trade.order.dao.PayRecordDaoImpl;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.order.domain.Way;
import com.server.module.trade.order.vo.MessageVo;
import com.server.module.trade.payRecordItem.PayRecordItemBean;
import com.server.module.trade.payRecordItem.PayRecordItemDaoImpl;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;
import com.server.module.trade.vendingMachinesWayItem.VmwayItemBean;
import com.server.module.trade.vendingMachinesWayItem.VmwayItemDao;
import com.server.module.trade.vendingMachinesWayItem.VmwayItemService;
import com.server.module.trade.web.balance.BalanceUtil;
import com.server.module.trade.web.service.SmsService;
import com.server.module.zfb_trade.bean.DoorOperateHistoryBean;
import com.server.module.zfb_trade.module.customer.AliCustomerDao;
import com.server.module.zfb_trade.module.dooroperation.AliDoorOperationService;
import com.server.module.zfb_trade.module.replenish.ReplenishService;
import com.server.module.zfb_trade.module.vmbase.AliVendingMachinesService;
import com.server.module.zfb_trade.module.vmbase.VmbaseInfoDto;
import com.server.module.zfb_trade.module.vmway.AliVmwayService;
import com.server.module.zfb_trade.util.ResultUtil;
import com.server.redis.MachinesKey;
import com.server.redis.NormalRedisKey;
import com.server.redis.RedisService;
import com.server.util.CalculateUtil;
import com.server.util.JsonUtils;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.client.SmsClient;
import com.server.util.stateEnum.CommandVersionEnum;
import com.server.util.stateEnum.ModeEnum;
import com.server.util.stateEnum.PayStateEnum;
import com.server.util.stateEnum.TransferStateEnum;

@RestController
@RequestMapping("/haert")
public class HeartbeatController {
	
	private final static Logger log = LogManager.getLogger(HeartbeatController.class);
	
	@Autowired
	private RedisService redisService;
	@Autowired
	private AliDoorOperationService doorOperationService;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private VmwayItemService vmwayItemService;
	@Autowired
	@Qualifier("aliVmwayService")
	private AliVmwayService vmwayService;
	@Autowired
	private OrderService orderService;
	@Autowired
	@Qualifier("aliVendingMachinesService")
	private AliVendingMachinesService vendingMachinesService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private MachinesBadOpenLogService machinesBadOpenLogService;
	@Autowired
	private PayRecordDaoImpl payRecordDao;
	@Autowired
	private PayRecordItemDaoImpl payRecordItemDao;
	@Autowired
	private MachinesClient machinesClient;
	@Autowired
	private VmwayItemDao vmwayItemDao;
	@Autowired
	private AliCustomerDao aliCustomerDao;
	@Autowired
	private ReplenishService replenishService;
	@Autowired
	private FootmarkService footmarkService;
	
	/**
	 * 获取机器心跳
	 * @author hebiting
	 * @date 2018年12月25日上午11:53:12
	 * @param factoryNumber
	 * @return
	 */
	@PostMapping("/beat")
	public ReturnDataUtil getHeartbeatInfo(String factoryNumber){
		String heartbeat = redisService.getString("HM-"+factoryNumber);
		return ResultUtil.success(heartbeat);
	}
	
	/**
	 * 每次版本2关门后调用完善关门重量信息 当closeFlag未删除时会反复调用此接口(运维与顾客)
	 * @author hebiting
	 * @date 2018年12月25日下午3:46:56
	 * @param param
	 * @return
	 */
	@RequestMapping("/consummateOpenHistory")
	public String consummateOpenHistory(@RequestBody String param){
		log.info("consummateOpenHistory param="+param);
		boolean revise=true;
		if(StringUtils.isBlank(param)){
			return TransferStateEnum.ERROR.getState();
		}
		MachineInfo machinesInfo = new MachineInfo(param);

		if(param.contains("w:0,0,0,0")) {
			log.error("非正常重量consummateOpenHistory param="+param);
			List<Way> wayList=machinesInfo.getList();
			for(Way w:wayList) {
				if(w.getState().equals("1")) {
					log.error("开门状态中并不处理空重量");
					return TransferStateEnum.ERROR.getState(); 
				}
			}
			return TransferStateEnum.ERROR.getState(); 
		}

		String vmCode = redisService.get(MachinesKey.getVmCodeByFactoryNum, machinesInfo.getFactoryNumber(),
				String.class);
		if (vmCode == null) {
			vmCode = orderDao.getVmCodeByFactoryNumber(machinesInfo.getFactoryNumber());
			log.info("去数据库获取vmCode");
			redisService.set(MachinesKey.getVmCodeByFactoryNum, machinesInfo.getFactoryNumber(), vmCode);
		}
		VmbaseInfoDto baseInfo = vendingMachinesService.getBaseInfo(vmCode);

		boolean isNotReturn = false;
		Integer version = orderDao.getVersionByVmCode(vmCode);
		String userValue = redisService.get(NormalRedisKey.ConsOperateHistoryUserId, vmCode);
		String operationValue = redisService.get(NormalRedisKey.ConsOperateHistoryId, vmCode);
		machinesInfo.setVmCode(vmCode);
		if(StringUtils.isBlank(userValue) || StringUtils.isBlank(operationValue)){
			isNotReturn = true;
			userValue = redisService.get(MachinesKey.currentMachinesUser, vmCode);
			operationValue = redisService.get(NormalRedisKey.OperateHistoryId, vmCode);
			if(StringUtils.isBlank(userValue) || StringUtils.isBlank(operationValue))
				return TransferStateEnum.ERROR.getState();
		}
		UserVo userVo = JsonUtils.toObject(userValue,new TypeReference<UserVo>(){});
		Long customerId = userVo.getId();
		Long operationId = Long.valueOf(operationValue);
		PayRecordBean payRecord = payRecordDao.getByOperateId(operationId);
		List<PayRecordItemBean> payRecordList =null;
		boolean comsummateInfo = doorOperationService.comsummateInfo(operationId, customerId, machinesInfo);
		
		List<VmwayItemBean> list=vmwayItemDao.findItemWeight(machinesInfo.getFactoryNumber(),machinesInfo.getCurrWay());
		if(list!=null && list.size()>0 ) {
			log.info(vmCode+"新版发送规格");// s:3;1;1:12,4700;2:10,5235&46$ 
			//老版校准 拿/补一桶 不用发校准
			//货道 用户id 货道号  补货 adjustNum 1
			if(userVo.getType() != UserVo.USER_CUSTOMER ) {
				FootmarkBean fb=footmarkService.findFinalReplenishRecord(vmCode, doorOperationService.get(operationId, customerId),null, customerId);
				if(fb.getMode().equals(ModeEnum.REVISE_MODE.getState())) {
					revise=false;
					log.info("新版换货1桶暂时不支持校准");
				}else {
//					redisService.set("ServiceOrderFlag_"+machinesInfo.getFactoryNumber(),"1",3);
//					String backValue = machinesClient.sendCommandTimes(userVo, machinesInfo.getFactoryNumber(), CalculateUtil.reviseCommand(machinesInfo, list),2);
//					if(TransferStateEnum.SUCCESS.getState().equals(backValue)){
//						redisService.del("ServiceOrderFlag_"+machinesInfo.getFactoryNumber());
//					}else {
//						log.info("重新发送校准规格失败"+machinesInfo.getVmCode());
//					}
				}
			}else {
//				redisService.set("ServiceOrderFlag_"+machinesInfo.getFactoryNumber(),"1",3);
//				String backValue = machinesClient.sendCommandTimes(userVo, machinesInfo.getFactoryNumber(), CalculateUtil.reviseCommand(machinesInfo, list),2);
//				if(TransferStateEnum.SUCCESS.getState().equals(backValue)){
//					redisService.del("ServiceOrderFlag_"+machinesInfo.getFactoryNumber());
//				}else {
//					log.info("重新发送校准规格失败"+machinesInfo.getVmCode());
//				}
			}

			if( (payRecord==null || (payRecord.getNum()==0 )) && userVo.getType() == UserVo.USER_CUSTOMER) {
				log.info(vmCode+"新版补扣计算");
				if(payRecord == null) {
					payRecordList =Lists.newArrayList();
					log.info(userVo.getId()+"没有首订单");
				}else {
					log.info("第一单数量为"+payRecord.getNum());
					payRecordList = payRecordItemDao.getListByPayRecordId(payRecord.getId());
				}
				DoorOperateHistoryBean history = doorOperationService.getById(operationId);
				Integer currWeight = history.getPosWeight() - history.getPreWeight();
				List<ItemInfoDto> itemList = null;
				if(CommandVersionEnum.VER1.getState().equals(version)){
					itemList = vmwayService.getOneWayItemInfo(vmCode, history.getDoorNO());
				}else {
					itemList = vmwayItemService.getOneWayItmeInfo(vmCode, history.getDoorNO());
				}
				BalanceUtil balance = new BalanceUtil();
				List<BigDecimal> bList = balance.calculate(itemList, currWeight,payRecordList,machinesInfo);
				int[] approximateArray = balance.getApproximateArray();
				BigDecimal totalPrice =bList.get(1);
				if(userVo.getType() == UserVo.USER_CUSTOMER){
					//处理用户购买的补扣
					int totalNum = 0;
					if(totalPrice.compareTo(BigDecimal.ZERO)>0 && currWeight < 0){
						List<Long> itemIdList = new ArrayList<Long>();
						for (int i = 0 ; i < approximateArray.length ; i++) {
							ItemChangeDto changeDto = new ItemChangeDto();
							changeDto.setBasicItemId(itemList.get(i).getBasicItemId());
							changeDto.setPrice(itemList.get(i).getUnitPrice().doubleValue());
							changeDto.setChangeNum(-approximateArray[i]);
							changeDto.setItemName(itemList.get(i).getItemName());
							changeDto.setOrderNum(itemList.get(i).getOrderNum());
							changeDto.setVmCode(vmCode);
							changeDto.setWayNum(history.getDoorNO());
							changeDto.setNum(itemList.get(i).getNum());
							changeDto.setRealNum(-approximateArray[i]);
							changeDto.setWeight(itemList.get(i).getUnitWeight());
							
							BigDecimal realTotalPrice = itemList.get(i).getUnitPrice().multiply(new BigDecimal(approximateArray[i]));
							changeDto.setRealTotalPrice(realTotalPrice.doubleValue());
							machinesInfo.getItemChangeList().add(changeDto);
							totalNum += approximateArray[i];
							if(changeDto.getChangeNum()<0){
								itemIdList.add(changeDto.getBasicItemId());
							}
						}
						machinesInfo.setVmCode(vmCode);
						machinesInfo.setVersion(version);
						machinesInfo.setCurrWay(history.getDoorNO());
						machinesInfo.setTotalPrice(totalPrice);
						machinesInfo.setRealUnit(totalNum);
						machinesInfo.setUnit(totalNum);
						
						revise=false;
						if(baseInfo.getFreeMachine()==1){
							orderService.payRecordForFree(machinesInfo, userVo, operationId, StringUtils.join(itemIdList,","), null,"补扣订单");
						}else{
							orderService.payRecord(machinesInfo, userVo, operationId, StringUtils.join(itemIdList,","), null,"补扣订单");
						}
					}
				}
				
			}
		}else if( (payRecord==null || payRecord.getNum()==1 || payRecord.getNum()==0) && userVo.getType() == UserVo.USER_CUSTOMER){
			revise=false;
			//用户类型判断(避免mms中关门未删除closeFlag-factoryNumber)
			if(payRecord == null) {
				payRecordList =Lists.newArrayList();
				log.info(userVo.getId()+"没有首订单");
			}else {
				log.info("第一单数量为"+payRecord.getNum());
				payRecordList = payRecordItemDao.getListByPayRecordId(payRecord.getId());
			}
			log.info("更新重量操作"+comsummateInfo);
			DoorOperateHistoryBean history = doorOperationService.getById(operationId);
			Integer currWeight = history.getPosWeight() - history.getPreWeight();
			List<ItemInfoDto> itemList = null;
			if(CommandVersionEnum.VER1.getState().equals(version)){
				itemList = vmwayService.getOneWayItemInfo(vmCode, history.getDoorNO());
			}else {
				itemList = vmwayItemService.getOneWayItmeInfo(vmCode, history.getDoorNO());
			}
			BalanceUtil balance = new BalanceUtil();
			List<BigDecimal> bList = balance.calculate(itemList, currWeight,payRecordList,machinesInfo);
			int[] approximateArray = balance.getApproximateArray();
			BigDecimal totalPrice =bList.get(1);
			if(bList.get(0).compareTo(new BigDecimal(0))==0) {
				log.info("重启"+machinesInfo.getFactoryNumber());
				redisService.set("ServiceOrderFlag_"+machinesInfo.getFactoryNumber(),"1",3);
				String restartMachines = machinesClient.restartMachines(machinesInfo.getFactoryNumber());
				if(("0").equals(restartMachines)){	
					redisService.del("ServiceOrderFlag_"+machinesInfo.getFactoryNumber());
				}else {
					log.info("重启失败");
				}
			}else {
				log.info("一样数量不操作"+machinesInfo.getFactoryNumber());
				if(comsummateInfo){
					redisService.del(NormalRedisKey.GOODS, machinesInfo.getFactoryNumber());
					redisService.del(NormalRedisKey.ConsOperateHistoryId, vmCode);
					redisService.del(NormalRedisKey.ConsOperateHistoryUserId , vmCode);
					redisService.del(NormalRedisKey.closeflag , machinesInfo.getFactoryNumber());

					if(isNotReturn){
						redisService.del(NormalRedisKey.OHM, machinesInfo.getFactoryNumber());
						redisService.del(MachinesKey.currentMachinesUser, vmCode);
						redisService.del(NormalRedisKey.OperateHistoryId,vmCode);
					}
					return TransferStateEnum.SUCCESS.getState();
				}
				return TransferStateEnum.SUCCESS.getState();
			}
			
			if(userVo.getType() == UserVo.USER_CUSTOMER){
				//处理用户购买的补扣
				int totalNum = 0;
				if(totalPrice.compareTo(BigDecimal.ZERO)>0 && currWeight < 0){
					List<Long> itemIdList = new ArrayList<Long>();
					for (int i = 0 ; i < approximateArray.length ; i++) {
						ItemChangeDto changeDto = new ItemChangeDto();
						changeDto.setBasicItemId(itemList.get(i).getBasicItemId());
						changeDto.setPrice(itemList.get(i).getUnitPrice().doubleValue());
						changeDto.setChangeNum(-approximateArray[i]);
						changeDto.setItemName(itemList.get(i).getItemName());
						changeDto.setOrderNum(itemList.get(i).getOrderNum());
						changeDto.setVmCode(vmCode);
						changeDto.setWayNum(history.getDoorNO());
						changeDto.setNum(itemList.get(i).getNum());
						changeDto.setRealNum(-approximateArray[i]);
						changeDto.setWeight(itemList.get(i).getUnitWeight());

						BigDecimal realTotalPrice = itemList.get(i).getUnitPrice().multiply(new BigDecimal(approximateArray[i]));
						changeDto.setRealTotalPrice(realTotalPrice.doubleValue());
						machinesInfo.getItemChangeList().add(changeDto);
						totalNum += approximateArray[i];
						if(changeDto.getChangeNum()<0){
							itemIdList.add(changeDto.getBasicItemId());
						}
					}
					machinesInfo.setVmCode(vmCode);
					machinesInfo.setVersion(version);
					machinesInfo.setCurrWay(history.getDoorNO());
					machinesInfo.setTotalPrice(totalPrice);
					machinesInfo.setRealUnit(totalNum);
					machinesInfo.setUnit(totalNum);
					if(baseInfo.getFreeMachine()==1){
						orderService.payRecordForFree(machinesInfo, userVo, operationId, StringUtils.join(itemIdList,","), null,"补扣订单");
					}else {
						orderService.payRecord(machinesInfo, userVo, operationId, StringUtils.join(itemIdList, ","), null, "补扣订单");
					}
				}
			}else{
				//处理补水人员的补充
				//暂不处理
			}
		}
		if(revise) {
			redisService.set("ServiceOrderFlag_"+machinesInfo.getFactoryNumber(),"1",3);
			String backValue = machinesClient.sendCommandTimes(userVo, machinesInfo.getFactoryNumber(), CalculateUtil.reviseCommand(machinesInfo, list),2);
			if(TransferStateEnum.SUCCESS.getState().equals(backValue)){
				redisService.del("ServiceOrderFlag_"+machinesInfo.getFactoryNumber());
			}else {
				log.info("重新发送校准规格失败"+machinesInfo.getVmCode());
			}
		}
		
		if(comsummateInfo){
			redisService.del(NormalRedisKey.GOODS, machinesInfo.getFactoryNumber());
			redisService.del(NormalRedisKey.ConsOperateHistoryId, vmCode);
			redisService.del(NormalRedisKey.ConsOperateHistoryUserId , vmCode);
			redisService.del(NormalRedisKey.closeflag , machinesInfo.getFactoryNumber());
	
			if(isNotReturn){
				redisService.del(NormalRedisKey.OHM, machinesInfo.getFactoryNumber());
				redisService.del(MachinesKey.currentMachinesUser, vmCode);
				redisService.del(NormalRedisKey.OperateHistoryId,vmCode);
			}
			return TransferStateEnum.SUCCESS.getState();
		}
		
		return TransferStateEnum.ERROR.getState();
	}
	
	//非正常开门
	@PostMapping("/improper")
	public ReturnDataUtil improperClosed(@RequestBody String param){
		//t:2;n:860027010242;d:0,1,0,0;s:0;p:1;w:112800,78690,109935,86400;c:+06&131\n
		log.info("improper param="+param);
		if(param.contains("s:0")){
			if(param.contains("w:0,0,0,0")) {
				log.error("非正常开门improper param="+param);
				return ResultUtil.error();
			}
			MachineInfo machinesInfo = new MachineInfo(param);
			String factoryNum = machinesInfo.getFactoryNumber();
			String vmCode = redisService.get(MachinesKey.getVmCodeByFactoryNum, factoryNum, String.class);
			if (vmCode == null) {
				vmCode = orderDao.getVmCodeByFactoryNumber(factoryNum);
				log.info("去数据库获取vmCode");
				redisService.set(MachinesKey.getVmCodeByFactoryNum, factoryNum, vmCode);
			}
			int subWeight = 0;
			VmbaseInfoDto baseInfo = vendingMachinesService.getBaseInfo(vmCode);
			int[] doorArray = new int[baseInfo.getTotalWayNum()];
			Arrays.fill(doorArray, 0);
			String doorString = StringUtils.join(doorArray,",");
			if(param.contains(doorString)){
				return ResultUtil.error();
			}
			UserVo userVo = redisService.get(MachinesKey.improperMachinesUser,vmCode,UserVo.class);
			if(userVo == null || userVo.getType() == UserVo.USER_ADMIN){
				MachinesBadOpenLogBean badOpenLogBean = new MachinesBadOpenLogBean(vmCode,null,new Date());
				machinesBadOpenLogService.insert(badOpenLogBean);
				redisService.set(NormalRedisKey.workMachines,baseInfo.getFactoryNumber(), "1");
				return ResultUtil.error();
			}
			String msg = "系统检测到您上次购买结束后未关紧售货机窗口，如关闭前有人提水则会他人拿水您付款。请及时关闭售货机窗口！谢谢合作！";
			if(!redisService.exists(NormalRedisKey.doorInduction,vmCode) && !redisService.exists(NormalRedisKey.doorInduction,factoryNum) && baseInfo.getFreeMachine()==0){
				smsService.sendMsgInterval(userVo.getId() ,baseInfo, msg , 60*3);
			}
			if(!redisService.exists(NormalRedisKey.badOpenLog,vmCode)){
				MachinesBadOpenLogBean badOpenLog = new MachinesBadOpenLogBean(vmCode,userVo.getId(),new Date());
				machinesBadOpenLogService.insert(badOpenLog);
				redisService.set(NormalRedisKey.badOpenLog, vmCode, "1");
			}
			String way = redisService.get(NormalRedisKey.improperWay,vmCode);
			Integer wayValue = Integer.valueOf(way);
			String preWeight = redisService.get(NormalRedisKey.improperWeight,vmCode);
			//当前重量
			Integer cuWeight = machinesInfo.getList().get(wayValue -1).getWeight();
			if(StringUtils.isBlank(preWeight)){
				DoorOperateHistoryBean history = doorOperationService.getByVmCode(vmCode);
				if(history != null && wayValue == history.getDoorNO() && userVo.getId().equals(history.getReferenceId())){
					if(history.getPosWeight() != null){
						subWeight = cuWeight - history.getPosWeight();
					}else{
						subWeight = cuWeight - history.getPreWeight();
					}
				}else{
					return ResultUtil.error();
				}
			}else{
				subWeight = cuWeight - Integer.valueOf(preWeight);
			}

			List<ItemInfoDto> itemList = null;
			if(CommandVersionEnum.VER1.getState().equals(baseInfo.getMachinesVersion())){
				itemList = vmwayService.getOneWayItemInfo(vmCode,wayValue);
			}else {
				itemList = vmwayItemService.getOneWayItmeInfo(vmCode,wayValue);
			}
			BalanceUtil balance = new BalanceUtil();
			BigDecimal totalPrice = balance.calculate(itemList, subWeight);
			int[] approximateArray = balance.getApproximateArray();
			if(userVo.getType() == UserVo.USER_CUSTOMER){
				//处理用户购买的补扣
				int totalNum = 0;
				if(totalPrice.compareTo(BigDecimal.ZERO)>0 && subWeight < 0){
					//设置非正常情况下之前重量
					redisService.set(NormalRedisKey.improperWeight,vmCode, cuWeight.toString());
					List<Long> itemIdList = new ArrayList<Long>();
					for (int i = 0 ; i < approximateArray.length ; i++) {
						ItemChangeDto changeDto = new ItemChangeDto();
						changeDto.setBasicItemId(itemList.get(i).getBasicItemId());
						changeDto.setPrice(itemList.get(i).getUnitPrice().doubleValue());
						changeDto.setChangeNum(-approximateArray[i]);
						changeDto.setItemName(itemList.get(i).getItemName());
						changeDto.setOrderNum(itemList.get(i).getOrderNum());
						changeDto.setVmCode(vmCode);
						changeDto.setWayNum(wayValue);
						changeDto.setNum(itemList.get(i).getNum());
						changeDto.setRealNum(-approximateArray[i]);
						BigDecimal realTotalPrice = itemList.get(i).getUnitPrice().multiply(new BigDecimal(approximateArray[i]));
						changeDto.setRealTotalPrice(realTotalPrice.doubleValue());
						machinesInfo.getItemChangeList().add(changeDto);
						totalNum += approximateArray[i];
						if(changeDto.getChangeNum()<0){
							itemIdList.add(changeDto.getBasicItemId());
						}
					}
					machinesInfo.setVmCode(vmCode);
					machinesInfo.setVersion(baseInfo.getMachinesVersion());
					machinesInfo.setCurrWay(wayValue);
					machinesInfo.setTotalPrice(totalPrice);
					machinesInfo.setRealUnit(totalNum);
					machinesInfo.setUnit(totalNum);
					DoorOperateHistoryBean doorOperateHistoryBean = new DoorOperateHistoryBean();
					doorOperateHistoryBean.setPreWeight(cuWeight-subWeight);
					doorOperateHistoryBean.setPosWeight(cuWeight);
					doorOperateHistoryBean.setStatus(2);
					doorOperateHistoryBean.setVmCode(vmCode);
					doorOperateHistoryBean.setOpenType(1);
					doorOperateHistoryBean.setDoorNO(wayValue);
					doorOperateHistoryBean.setRemark("非正常开门关门");
					doorOperateHistoryBean.setReferenceId(userVo.getId());
					doorOperateHistoryBean.setCreateTime(new Date());
					DoorOperateHistoryBean history = doorOperationService.save(doorOperateHistoryBean);
					if(baseInfo.getFreeMachine()==1){
						orderService.payRecordForFree(machinesInfo, userVo, history.getId(), StringUtils.join(itemIdList,","), null,"非正常开门订单");
					}else{
						orderService.payRecord(machinesInfo, userVo, history.getId(), StringUtils.join(itemIdList,","), null,"非正常开门订单");
					}

					return ResultUtil.success();
				}
			}
		}
		return ResultUtil.error();
	}
	
	
	@PostMapping("/sendMessage")
	public ReturnDataUtil sendMessage(@RequestBody String factoryNumber){
		//最后一笔无结算的售货机订单(员工除外)
		MessageVo messageVo=payRecordDao.getFinalOrder(factoryNumber);

		if(messageVo != null) {
			if(StringUtil.isNotBlank(messageVo.getPhone())) {
				if(!aliCustomerDao.queryIsLoginUserByPhone(messageVo.getPhone())) {
					if(!redisService.exists("MMS_"+factoryNumber)) {
						log.info("发送未关门短信"+factoryNumber+"---"+messageVo.getPhone());

						String vmCode = redisService.get(MachinesKey.getVmCodeByFactoryNum, factoryNumber, String.class);
						VmbaseInfoDto baseInfo = vendingMachinesService.getBaseInfo(vmCode);
						if(baseInfo.getFreeMachine()==0){
							String msg="尊敬的客户，我们检测到您在优水售卖机上开门已经超过1分钟，如已经拿取商品，请及时关门，避免他人拿取商品！";
							String userMsg="系统检测到机器编号为"+messageVo.getVmCode()+"的售货机，用户购买后没有将门关好，请及时让运维工作人员处理，以免造成损失。谢谢！";
							smsService.sendMsg(messageVo.getPhone(), msg);
							smsService.sendMsg(messageVo.getPrincipalPhone(), userMsg);
						}else{
							log.info("factoryNumber{}超过1分钟未关门",factoryNumber);
						}

						return ResultUtil.success();
					}
					//if(!aliCustomerDao.queryIfReplenishRecent(messageVo.getVmCode())) {

					//}
					
				}
			}

		}
		return ResultUtil.error();
	}
	
}
