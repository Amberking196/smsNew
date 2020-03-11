package com.server.module.trade.web.order.closed;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.server.client.ReplenishClient;
import com.server.module.commonBean.ItemInfoDto;
import com.server.module.coupon.CouponDao;
import com.server.module.coupon.MachinesLAC;
import com.server.module.footmark.FootmarkRedisBean;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.customer.TblCustomerDao;
import com.server.module.trade.order.bean.PayRecordBean;
import com.server.module.trade.order.dao.OrderDao;
import com.server.module.trade.order.dao.PayRecordDao;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;
import com.server.module.trade.vendingMachinesWayItem.VmwayItemDao;
import com.server.module.trade.web.order.preferential.HandlerManager;
import com.server.module.trade.web.service.SmsService;
import com.server.module.zfb_trade.bean.DoorOperateHistoryBean;
import com.server.module.zfb_trade.module.dooroperation.AliDoorOperationDao;
import com.server.module.zfb_trade.module.vmway.AliVmwayDao;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.module.zfb_trade.util.ResultUtil;
import com.server.module.zfb_trade.util.enumparam.DoorOperateEventEnum;
import com.server.module.zfb_trade.util.enumparam.ResultEnum;
import com.server.redis.MachinesKey;
import com.server.redis.RedisService;
import com.server.util.CalculateUtil;
import com.server.util.PayCodeUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CommandVersionEnum;
import com.server.util.stateEnum.ModeEnum;
import com.server.util.stateEnum.PayStateEnum;

@Service
public class CusClosedServiceImpl implements CusClosedService{
	
	private final static Logger log = LogManager.getLogger(CusClosedServiceImpl.class);
	@Autowired
	private SmsService smsService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private TblCustomerDao tblCustomerDao;
	@Autowired
	private ReplenishClient replenishClient;
	@Autowired
	private HandlerManager handlerManager;
	@Autowired
	private AliDoorOperationDao doorOperationDao;
	@Autowired
	private VmwayItemDao vmwayItemDao;
	@Autowired
	private PayRecordDao payRecordDaoImpl;
	@Autowired
	private CouponDao couponDaoImpl;
	@Autowired
	@Qualifier("aliVmwayDao")
	private AliVmwayDao vmwayDao;

	@Override
	public ReturnDataUtil notClosedSendMsg(String factoryNum) {
		String vmCode = orderDao.getVmCodeByFactoryNumber(factoryNum);
		UserVo userVo = redisService.get(MachinesKey.currentMachinesUser, vmCode,UserVo.class);
		if(userVo != null){
			String phone = tblCustomerDao.getPhoneByCustomerId(userVo.getId());
			if(StringUtils.isNotBlank(phone)){
				String msg = "您刚才购买商品所在机器门未关紧，请前往关闭，不然会导致他人购物，结算在您。多谢您的配合！";
				String sendMsg = smsService.sendMsg(phone, msg);
				if(StringUtils.isNotBlank(sendMsg)){
					return ResultUtil.success();
				}
			}
		}
		return ResultUtil.error();
	}

	@Override
	public ReturnDataUtil closedVer1(String param) {
		//MachineInfo 在购水过程中，把生成订单的必要的机器信息封装到该对象，然后运算，得出购水数量金额等
		MachineInfo machineInfo = new MachineInfo(param);
		// 根据出厂编码获取编号
		String vmCode = redisService.get(MachinesKey.getVmCodeByFactoryNum, machineInfo.getFactoryNumber(),
				String.class);
		if (vmCode == null) {
			vmCode = orderDao.getVmCodeByFactoryNumber(machineInfo.getFactoryNumber());
			redisService.set(MachinesKey.getVmCodeByFactoryNum, machineInfo.getFactoryNumber(), vmCode);
		}
		machineInfo.setVmCode(vmCode);
		UserVo userVo = redisService.get(MachinesKey.currentMachinesUser, machineInfo.getVmCode(), UserVo.class);
		if (userVo == null) {// 找不到当前用户
			log.error("vmCode=" + vmCode + " 找不到当前用户"+param);
			clearRedisState(vmCode, machineInfo.getFactoryNumber());
			return ResultUtil.error(ResultEnum.CUSTOMER_NOT_EXIST);
		}
		if(userVo.getType() == UserVo.USER_ADMIN){
			replenishClient.toMMSOneClosed(param, CommandVersionEnum.VER1.getState());
			return ResultUtil.success();
		}
		//获取当前货道
		String wayStr = redisService.getString("CurrWay-" + vmCode);//
		int currWay = Integer.parseInt(wayStr);
		//获取足迹信息
		FootmarkRedisBean footmark = redisService.get("redis_mode_id"+vmCode, FootmarkRedisBean.class);

		if(ModeEnum.REVISE_MODE.getState().equals(footmark!=null?footmark.getModeId():null) || ModeEnum.ONCE_REVISE_MODE.getState().equals(footmark!=null?footmark.getModeId():null)){
			
		}else {
			redisService.del("redis_mode_id"+vmCode);
		}
		
		// 在redis取得机器上一刻的心跳数据
		String heartbeat = redisService.getString("PHM-" + machineInfo.getFactoryNumber());
		// 更新心跳,必须要先取出再更新心跳,以免心跳更新不及时造成算错误
		redisService.setString("HM-" + machineInfo.getFactoryNumber(), param);
		log.info("开门前心跳=" + heartbeat);
		// 记录开门心跳日志
		orderDao.saveHeartBeatLog(heartbeat, machineInfo.getFactoryNumber(), 'k', userVo.getId(),
				userVo.getType(),footmark!=null?footmark.getFootmarkId():null);
		// 记录关门心跳日志
		orderDao.saveHeartBeatLog(param, machineInfo.getFactoryNumber(), 'g', userVo.getId(),
				userVo.getType(),footmark!=null?footmark.getFootmarkId():null);
		//解析开门前机器货道信息
		MachineInfo preMachineInfo = new MachineInfo(heartbeat);
		// 计算货道商品变化
		machineInfo.calculateWhatWay(preMachineInfo, currWay);
		//获取货道商品信息
		List<ItemInfoDto> wayItemInfo = vmwayDao.getOneWayItemInfo(vmCode, currWay);
		if(wayItemInfo == null || wayItemInfo.size() == 0){
			//货道商品不存在
			return ResultUtil.error();
		}
		ItemInfoDto itemInfo = wayItemInfo.get(0);
		machineInfo.setPrice(itemInfo.getUnitPrice());
		machineInfo.setUnitWeight(itemInfo.getUnitWeight());
		// 计算数量价格
		machineInfo.calculateHowUnitAndPrice();
		// 获取当前开关门记录id,并更新关门记录
		Integer posWeight = machineInfo.getList().get(currWay - 1).getWeight();
		Integer flag = CalculateUtil.isNormal(machineInfo.getUnitWeight(), machineInfo.getCurrWeight());
		Long operateHistoryId = updateOperateHistory(vmCode,posWeight,flag);
		//清除redis缓存信息
		clearRedisState(vmCode, machineInfo.getFactoryNumber());
		if (machineInfo.getCurrWeight() > 0 && machineInfo.getUnit() > 0) {// 有异常
			log.error(vmCode+"顾客购水后，重量较之前增加，有异常");
			return ResultUtil.error();
		}
		if (machineInfo.getUnit() == 0) {// 表示水量没变化
			return ResultUtil.success();
		}
		//将版本1商品信息转换为版本2商品信息,方便后面逻辑统一
		changeToVer2Info(machineInfo,itemInfo,currWay);
		//把商品变换信息保存到redis中
		redisService.set("replenish_" + vmCode, JsonUtil.toJson(machineInfo.getItemChangeList()), 60 * 10);
		//结算并记录
		payRecord(machineInfo,userVo,operateHistoryId,itemInfo.getBasicItemId().toString(),itemInfo.getItemName(),footmark,"正常订单");
		return ResultUtil.success();
	}
	
	private void changeToVer2Info(MachineInfo machineInfo,ItemInfoDto itemInfo,int currWay){
		List<ItemChangeDto> itemList = new ArrayList<ItemChangeDto>();
		ItemChangeDto itemChange = new ItemChangeDto();
		itemChange.setRealNum(-machineInfo.getUnit());
		itemChange.setBasicItemId(itemInfo.getBasicItemId());
		itemChange.setChangeNum(-machineInfo.getUnit());
		itemChange.setVmCode(machineInfo.getVmCode());
		itemChange.setItemName(itemInfo.getItemName());
		itemChange.setPic(itemInfo.getPic());
		itemChange.setPrice(itemInfo.getUnitPrice().doubleValue());
		itemChange.setItemTypeId(11L);
		itemChange.setWayNum(currWay);
		itemChange.setNum(itemInfo.getNum());
		itemChange.setRealTotalPrice(machineInfo.getTotalPrice().doubleValue());
		itemList.add(itemChange);
		machineInfo.setItemChangeList(itemList);
	}

	@Override
	public ReturnDataUtil closedVer2(String param) {
		if (StringUtil.isBlank(param)) {
			return ResultUtil.error(ResultEnum.ILLEGAL_PARAMS);
		}
		MachineInfo machineInfo = new MachineInfo();
		machineInfo.moreGoodsAllPaser(param);
		String factoryNum = machineInfo.getFactoryNumber();
		String vmCode = redisService.get(MachinesKey.getVmCodeByFactoryNum, factoryNum, String.class);
		if (vmCode == null) {
			vmCode = orderDao.getVmCodeByFactoryNumber(factoryNum);
			redisService.set(MachinesKey.getVmCodeByFactoryNum, factoryNum, vmCode);
		}
		machineInfo.setVmCode(vmCode);
		UserVo userVo = redisService.get(MachinesKey.currentMachinesUser, machineInfo.getVmCode(), UserVo.class);
		if (userVo == null) {// 找不到当前用户
			log.error("vmCode=" + vmCode + " 找不到当前用户"+param);
			clearRedisState(vmCode, machineInfo.getFactoryNumber());
			return ResultUtil.error(ResultEnum.CUSTOMER_NOT_EXIST);
		}
		//转发到mms补货系统
		if(userVo.getType() == UserVo.USER_ADMIN){
			replenishClient.toMMSOneClosed(param, CommandVersionEnum.VER2.getState());
			return ResultUtil.success();
		}
		//获取当前被开货道号
		String wayStr = redisService.getString("CurrWay-" + vmCode);
		machineInfo.setCurrWay(Integer.valueOf(wayStr));
		//获取商品信息并set到machineInfo
		Map<String, String> someItemInfo = setMoreItemInfo(machineInfo);
		String itemIds = someItemInfo.get("itemIds");
		String payRecordItemName = someItemInfo.get("payRecordItemName");
		//获取足迹信息
		FootmarkRedisBean footmark = redisService.get("redis_mode_id"+vmCode, FootmarkRedisBean.class);
		//校准中无须删除
		if(ModeEnum.REVISE_MODE.getState().equals(footmark!=null?footmark.getModeId():null) || ModeEnum.ONCE_REVISE_MODE.getState().equals(footmark!=null?footmark.getModeId():null)){
			
		}else {
			redisService.del("redis_mode_id"+vmCode);
		}
		//记录关门时，机器返回信息
		orderDao.saveHeartBeatLog(param, machineInfo.getFactoryNumber(), 'm', userVo.getId(),userVo.getType(),footmark!=null?footmark.getFootmarkId():null);
		// 获取当前开关门记录id,并更新关门记录
		Long operateHistoryId = updateOperateHistory(vmCode,null,10);
		//设置顾客拿取商品信息
		redisService.set("replenish_" + vmCode, JsonUtil.toJson(machineInfo.getItemChangeList()), 60 * 10);
		// 清除redis标志位
		clearRedisState(vmCode, machineInfo.getFactoryNumber());
		if (machineInfo.getUnit() == 0) {
			//未取货，直接返回成功
			return ResultUtil.success();
		}
		//结算并记录
		payRecord(machineInfo, userVo, operateHistoryId, itemIds,payRecordItemName,footmark,"正常订单");
		return ResultUtil.success();
	}

	@Override
	public ReturnDataUtil allClosedVer2(String param) {
		replenishClient.toMMSAllClosed(param, CommandVersionEnum.VER2.getState());
		return ResultUtil.success();
	}
	
	
	private void payRecord(MachineInfo machineInfo, UserVo userVo, Long operateHistoryId, String items,
			String payRecordItemName,FootmarkRedisBean footmark,String remark){
		boolean existsOpenBeforeOrder = false;
		Date date = new Date();
		long payType = userVo.getPayType();
		String vmCode = machineInfo.getVmCode();
		PayRecordBean payRecord = null;
		if(operateHistoryId != null){
			payRecord = payRecordDaoImpl.getByOperateId(operateHistoryId);
			if(payRecord!=null){
				if(!PayStateEnum.BLANK_ORDER.getState().equals(Integer.valueOf(payRecord.getState().intValue()))){
					return ;
				}
				existsOpenBeforeOrder = true;
			}
		}
		if(!existsOpenBeforeOrder){
			payRecord = new PayRecordBean();
			payRecord.setCreateTime(date);
			String payCode = PayCodeUtil.genPayCode(1, vmCode);
			payRecord.setPayCode(payCode);
			payRecord.setCustomerId(userVo.getId());
			payRecord.setVendingMachinesCode(vmCode);
			payRecord.setPayType(Integer.valueOf(payType+""));
			payRecord.setWayNumber(machineInfo.getCurrWay() * 1l);
		}
		payRecord.setMemberPay(BigDecimal.ZERO);
		payRecord.setRemark(remark);
		payRecord.setItemName(payRecordItemName);
		payRecord.setNum(machineInfo.getRealUnit() * 1l);
		payRecord.existsOpenBeforeOrder = existsOpenBeforeOrder;
		payRecord.setOperateHistoryId(operateHistoryId);// 关联开关门记录id
		payRecord.setState(PayStateEnum.NOT_PAY.getState() * 1l);// 订单创建
		// payRecord.setPayType(payType);
		payRecord.setSrcType(userVo.getPayType().intValue());
		payRecord.setPrice(machineInfo.getTotalPrice());
		// 获取该机器所属路线，区域，公司
		MachinesLAC mlac = couponDaoImpl.getMachinesLAC(vmCode);
		//处理 --存水，提水券，活动，优惠券，优水余额，微信或支付宝支付
		handlerManager.handler(machineInfo, userVo, payRecord, mlac, items);
	}
	
	private Long updateOperateHistory(String vmCode,Integer posWeight,Integer normalFlag){
		if (StringUtil.isNotBlank(redisService.getString("OperateHistoryId-" + vmCode))) {
			Long operateHistoryId = Long.valueOf(redisService.getString("OperateHistoryId-" + vmCode));
			DoorOperateHistoryBean doorOperate = new DoorOperateHistoryBean();
			doorOperate.setId(operateHistoryId);
			doorOperate.setClosedTime(new Date());
			doorOperate.setStatus(DoorOperateEventEnum.CLOSED.getCode());
			doorOperate.setRemark("关门成功");
			doorOperate.setPosWeight(posWeight);
			doorOperate.setNormalFlag(normalFlag);// 多商品货道状态为10
			doorOperationDao.update(doorOperate);
			return operateHistoryId;
		} else {
			log.info(vmCode + ":接受的开门记录id为null 或者 空");
		}
		return null;
	}
	
	private Map<String,String> setMoreItemInfo(MachineInfo machineInfo){
		int total = 0;
		Integer currWeight = 0;
		BigDecimal totalPrice = BigDecimal.ZERO;
		List<Long> itemList = new ArrayList<Long>();
		List<String> itemName = new ArrayList<String>();
		Iterator<ItemChangeDto> iterator = machineInfo.getItemChangeList().iterator();
		while(iterator.hasNext()){
			ItemChangeDto itemChange = iterator.next();
			itemChange.setVmCode(machineInfo.getVmCode());
			if(vmwayItemDao.getChangeInfo(itemChange)==null){
				iterator.remove();
				log.error(machineInfo.getVmCode()+"机器货道商品信息不正常"+machineInfo.getParams());
				continue;
			}
			if (itemChange.getChangeNum() < 0) {
				int num = Math.abs(itemChange.getChangeNum());
				num = Math.abs(itemChange.getChangeNum());
				itemList.add(itemChange.getBasicItemId());
				currWeight += num * itemChange.getWeight();
				total += num;
				BigDecimal realTotalPrice = new BigDecimal(itemChange.getPrice().toString())
						.multiply(new BigDecimal(num)).setScale(2, BigDecimal.ROUND_DOWN);
				itemChange.setRealTotalPrice(realTotalPrice.doubleValue());
				totalPrice = totalPrice.add(realTotalPrice);
				itemName.add(itemChange.getItemName()+"*"+num);
			}
		}
		String payRecordItemName = StringUtils.join(itemName,",");
		String itemIds = StringUtils.join(itemList, ",");
		machineInfo.setCurrWeight(currWeight);
		machineInfo.setUnit(total);
		machineInfo.setRealUnit(total);
		machineInfo.setTotalPrice(totalPrice);
		Map<String,String> result = new HashMap<String,String>();
		result.put("payRecordItemName",payRecordItemName);
		result.put("itemIds",itemIds);
		return result;
	}
	
	private void clearRedisState(String vmCode, String factoryNumber) {
		// 删除开门标识
		long l = redisService.del(MachinesKey.currentMachinesOpenState, vmCode);
		log.info(vmCode + ": del the open state = " + l);
		redisService.set("ConsOperateHistoryUserId-"+vmCode, redisService.get(MachinesKey.currentMachinesUser,vmCode),60);
		redisService.set("ConsOperateHistoryId-"+vmCode, redisService.getString("OperateHistoryId-" + vmCode) ,60);
		l = redisService.del(MachinesKey.currentMachinesUser, vmCode);
		log.info(vmCode + ": del the curr user = " + l);
		l = redisService.del("CurrWay-" + vmCode);
		log.info(vmCode + ": del curr way =" + l);
		l = redisService.del("OperateHistoryId-" + vmCode);
		log.info(vmCode + ": del open door OperateHistodyId  =" + l);
		l = redisService.del("OHM-" + factoryNumber);
		log.info(vmCode + ": del open door state ohm = " + l);
//		l = redisService.del("GOODS_" + factoryNumber);
//		log.info(vmCode + ": del GOODS change info = " + l);
		l = redisService.del("PHM-" + factoryNumber);
		log.info(vmCode + ": del open door heart phm = " + l);
	}

}
