package com.server.module.trade.order;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayUtil;
import com.server.client.MachinesClient;
import com.server.client.ReplenishClient;
import com.server.company.MachinesDao;
import com.server.module.activeDegree.UserActiveDegreeDao;
import com.server.module.activity.ActivityBean;
import com.server.module.activity.ActivityDao;
import com.server.module.carryWaterVouchersManage.carryRecord.CarryRecordBean;
import com.server.module.carryWaterVouchersManage.carryRecord.CarryRecordDao;
import com.server.module.carryWaterVouchersManage.carryWaterVouchers.CarryDto;
import com.server.module.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersDao;
import com.server.module.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerDao;
import com.server.module.commonBean.ItemBasicBean;
import com.server.module.commonBean.ItemInfoDto;
import com.server.module.coupon.*;
import com.server.module.footmark.FootmarkBean;
import com.server.module.footmark.FootmarkRedisBean;
import com.server.module.footmark.FootmarkService;
import com.server.module.itemBasic.ItemBasicService;
import com.server.module.member.MemberDao;
import com.server.module.member.MemberUseLog;
import com.server.module.payRecord.BlankOrderCreator;
import com.server.module.store.*;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.config.pay.MyWXPayConfig;
import com.server.module.trade.config.pay.WxPayConfigFactory;
import com.server.module.trade.customer.TblCustomerBean;
import com.server.module.trade.customer.TblCustomerDao;
import com.server.module.trade.order.bean.PayRecordBean;
import com.server.module.trade.order.calculate.activity.ActivityExecute;
import com.server.module.trade.order.calculate.carry.CarryExecute;
import com.server.module.trade.order.calculate.coupon.CouponExecute;
import com.server.module.trade.order.dao.OrderDao;
import com.server.module.trade.order.dao.PayRecordDao;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.order.domain.Way;
import com.server.module.trade.order.vo.OpenDoorVo;
import com.server.module.trade.payRecordItem.PayRecordItemBean;
import com.server.module.trade.payRecordItem.PayRecordItemDao;
import com.server.module.trade.util.ResultBean;
import com.server.module.trade.util.UserUtil;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;
import com.server.module.trade.vendingMachinesWayItem.VmwayItemBean;
import com.server.module.trade.vendingMachinesWayItem.VmwayItemDao;
import com.server.module.trade.vendingMachinesWayItem.VmwayItemService;
import com.server.module.trade.web.service.SmsService;
import com.server.module.zfb_trade.bean.DoorOperateHistoryBean;
import com.server.module.zfb_trade.convert.Msg2DoorOperateConverter;
import com.server.module.zfb_trade.module.customer.AliCustomerService;
import com.server.module.zfb_trade.module.customer.CustomerBean;
import com.server.module.zfb_trade.module.dooroperation.AliDoorOperationDao;
import com.server.module.zfb_trade.module.replenish.ReplenishDao;
import com.server.module.zfb_trade.module.vmbase.AliVendingMachinesService;
import com.server.module.zfb_trade.module.vmbase.VmbaseInfoDto;
import com.server.module.zfb_trade.module.vminfo.AliVminfoService;
import com.server.module.zfb_trade.module.vminfo.VminfoDto;
import com.server.module.zfb_trade.module.vmway.AliVmwayDao;
import com.server.module.zfb_trade.service.AlipayService;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.module.zfb_trade.util.enumparam.DoorOperateEventEnum;
import com.server.redis.MachinesKey;
import com.server.redis.NormalRedisKey;
import com.server.redis.RedisService;
import com.server.util.*;
import com.server.util.stateEnum.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
	public static Logger log = LogManager.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private AlipayService alipayService;
	@Autowired
	private AliCustomerService aliCustomerService;
	@Autowired
	private PayRecordDao payRecordDaoImpl;
	@Autowired
	private RedisService redisService;
	@Autowired
	private WxPayConfigFactory wxPayConfigFactory;
	@Value("${wechat.pay.notifyUrl}") // 跟支付的回调接口一样
	private String depositNotifyUrl;

	@Value("${heartBeatHost}")
	private String heartBeatHost;
	@Autowired
	private TblCustomerDao tblCustomerDao;
	@Autowired
	private AliVminfoService vminfoService;
	@Autowired
	private AliDoorOperationDao doorOperationDao;
	@Autowired
	private StoreOrderDao storeOrderDaoImpl;
	@Autowired
	private CouponDao couponDaoImpl;
	@Autowired
	private ActivityDao activityDao;
	@Autowired
	private ItemBasicService itemBasicService;
	@Autowired
	private VmwayItemDao vmwayItemDao;
	@Autowired
	private PayRecordItemDao payRecordItemDao;
	@Autowired
	private SmsService smsService;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private ActivityExecute activityExecute;
	@Autowired
	private CouponExecute couponExecute;
	@Autowired
	private ReplenishDao replenishDao;
	@Autowired
	private CarryWaterVouchersDao carryWaterDao;
	@Autowired
	private CarryRecordDao carryRecordDao;
	@Autowired
	private CarryExecute carryExecute;
	@Autowired
	private CarryWaterVouchersCustomerDao carryCustomerDao;
	@Autowired
	private FootmarkService footmarkService;
	@Autowired
	private ReplenishClient replenishClient;
	@Autowired
	private UserActiveDegreeDao userActiveDegreeDao;
	@Autowired
	private BlankOrderCreator blankOrderCreator;
	@Autowired
	private AliVmwayDao vmwayDao;
	@Autowired
	private MachinesDao machinesDao;
	@Autowired
	private VmwayItemService vmwayItemService;
	
	@Autowired
	private MachinesClient machinesClient;
	@Autowired
	@Qualifier("aliVendingMachinesService")
	private AliVendingMachinesService vendingMachinesService;
	/**
	 * 下单服务
	 */
	// @SuppressWarnings("unused")
	@Override
	public ResultBean<?> closeDoor(MachineInfo machineInfo, String params) {
		//log.info("closeDoor--" + params);
		ResultBean<?> result = new ResultBean<>();
		/**
		 * MachineInfo 在购水过程中，把生成订单的必要的机器信息封装到该对象，然后运算，得出购水数量金额等
		 */

		// 根据出厂编码获取编号
		String vmCode = redisService.get(MachinesKey.getVmCodeByFactoryNum, machineInfo.getFactoryNumber(),
				String.class);

		if (vmCode == null) {
			vmCode = orderDao.getVmCodeByFactoryNumber(machineInfo.getFactoryNumber());
			//log.info("去数据库获取vmCode");
			redisService.set(MachinesKey.getVmCodeByFactoryNum, machineInfo.getFactoryNumber(), vmCode);
		}
		//log.info("机器关门 vmCode=" + vmCode);
		machineInfo.setVmCode(vmCode);

		UserVo userVo = redisService.get(MachinesKey.currentMachinesUser, machineInfo.getVmCode(), UserVo.class);
		if (userVo == null) {// 找不到当前用户
			log.info("vmCode=" + vmCode + " 找不到当前用户");
			result.setCode(-1);
			result.setMsg("找不到当前用户");
			clearRedisState(vmCode, machineInfo.getFactoryNumber());
			return result;
		}

		if(userVo.getType() == UserVo.USER_ADMIN && ("mms").equals(redisService.getString("mms"))){
			replenishClient.toMMSOneClosed(params, CommandVersionEnum.VER1.getState());
			return result;
		}
		
		String wayStr = redisService.getString("CurrWay-" + vmCode);//
		//log.info("当前门号====" + wayStr);
		int currWay = Integer.parseInt(wayStr);
		FootmarkRedisBean footmark = redisService.get("redis_mode_id"+vmCode, FootmarkRedisBean.class);
		
		if(ModeEnum.REVISE_MODE.getState().equals(footmark!=null?footmark.getModeId():null) || ModeEnum.ONCE_REVISE_MODE.getState().equals(footmark!=null?footmark.getModeId():null)){
			
		}else {
			redisService.del("redis_mode_id"+vmCode);
		}
		// 在redis取得机器上一刻的心跳数据
		String heartbeat = redisService.getString("PHM-" + machineInfo.getFactoryNumber());
		// 更新心跳,必须要先取出再更新心跳,以免心跳更新不及时造成算错误
		redisService.setString("HM-" + machineInfo.getFactoryNumber(), params);
	
		//log.info("开门前心跳=" + heartbeat);
		// 记录开门心跳日志
		orderDao.saveHeartBeatLog(heartbeat, machineInfo.getFactoryNumber(), 'k', userVo.getId(),
				userVo.getType(),footmark!=null?footmark.getFootmarkId():null);
		// 记录关门心跳日志
		orderDao.saveHeartBeatLog(params, machineInfo.getFactoryNumber(), 'g', userVo.getId(),
				userVo.getType(),footmark!=null?footmark.getFootmarkId():null);
		// t:2;n:111380310014;d:1,1,1,1;s:0;p:0;w:3700,3500,2500,3650&45
		// 上一刻机器数据
		MachineInfo preMachineInfo = new MachineInfo(heartbeat);
		// 计算购买哪个货道的水
		machineInfo.calculateWhatWay(preMachineInfo, currWay);
		// 获取货道基础信息如：状态、商品id、当前数量、容量
		Map<String, Object> wayMap = orderDao.getWay(vmCode, machineInfo.getCurrWay());
		// Integer state=(Integer) wayMap.get("state");
		// Integer fullNum=(Integer) wayMap.get("fullNum");
		Long wayId = (Long) wayMap.get("id");
		Long itemId = (Long) wayMap.get("itemId");
		int preNum = (Integer) wayMap.get("num");
		Map<String, Object> itemMap = orderDao.getItemInfo(itemId);
		machineInfo.setPrice(new BigDecimal(itemMap.get("price").toString()));
		machineInfo.setUnitWeight(Integer.valueOf((String) itemMap.get("standard")));
		// 计算数量价格
		machineInfo.calculateHowUnitAndPrice();

		// 获取当前开关门记录id,并更新关门记录
		Long operateHistoryId = null;
		if (StringUtil.isNotBlank(redisService.getString("OperateHistoryId-" + vmCode))) {
			operateHistoryId = Long.valueOf(redisService.getString("OperateHistoryId-" + vmCode));
			DoorOperateHistoryBean doorOperate = new DoorOperateHistoryBean();
			doorOperate.setId(operateHistoryId);
			doorOperate.setClosedTime(new Date());
			doorOperate.setStatus(DoorOperateEventEnum.CLOSED.getCode());
			doorOperate.setRemark("关门成功");
			Integer posWeight = null;
			if (machineInfo.getList().size() >= currWay && (currWay - 1) >= 0) {
				posWeight = machineInfo.getList().get(currWay - 1).getWeight();
			}
			doorOperate.setPosWeight(posWeight);
			doorOperate.setNormalFlag(CalculateUtil.isNormal(machineInfo.getUnitWeight(), machineInfo.getCurrWeight()));
			boolean update = doorOperationDao.update(doorOperate);
			//log.info(vmCode + ": 更新开关门记录状态--" + update);
		} else {
			log.info(vmCode + ":接受的开门记录id为null 或者 空");
		}

		//log.info("计算结果---");
		//log.info("重量差=" + machineInfo.getCurrWeight());
		//log.info("计算出来的当前门号===" + machineInfo.getCurrWay());
		if (currWay != machineInfo.getCurrWay()) {
			log.info("计算出来的门号跟当前点击门号不对应======");
			clearRedisState(vmCode, machineInfo.getFactoryNumber());
			return null;
		}
		Long basicItemId = ((Integer) itemMap.get("id")) * 1l;

		List<ItemChangeDto> itemList = new ArrayList<ItemChangeDto>();
		ItemChangeDto itemChange = new ItemChangeDto();
		if((machineInfo.getCurrWeight() <= 0)){
			itemChange.setRealNum(-machineInfo.getUnit());
		}else{
			itemChange.setRealNum(machineInfo.getUnit());
		}
		//获取用户存水
		if(userVo.getType() == UserVo.USER_CUSTOMER && machineInfo.getCurrWeight() < 0 && machineInfo.getUnit() > 0){
			CustomerStockBean customerStockBean = storeOrderDaoImpl.getStockByBasicItem(basicItemId, userVo.getId());
			Integer stockWater = 0;
			if (customerStockBean != null) {// 有存水用户
				stockWater = customerStockBean.getStock() - customerStockBean.getPickNum();// 剩余存水量
				if (stockWater > 0) {
					Integer pickNum = 0;
					if (stockWater >= machineInfo.getUnit()) {// 剩余存水量>=取水量
						customerStockBean.setPickNum(customerStockBean.getPickNum() + machineInfo.getUnit());
						storeOrderDaoImpl.update(customerStockBean);
						pickNum = machineInfo.getUnit();
					} else {// 剩余存水量<取水量
						customerStockBean.setPickNum(customerStockBean.getStock());
						storeOrderDaoImpl.update(customerStockBean);
						machineInfo.setUnit(machineInfo.getUnit() - stockWater);
						machineInfo
								.setTotalPrice(machineInfo.getPrice().multiply(new BigDecimal(machineInfo.getUnit())));
						pickNum = stockWater;
					}
					CustomerPickRecord pickRecord = new CustomerPickRecord();
					pickRecord.setBasicItemId(basicItemId);
					pickRecord.setCustomerId(userVo.getId());
					pickRecord.setVmCode(vmCode);
					pickRecord.setWayNum(currWay);
					pickRecord.setPickNum(pickNum);
					storeOrderDaoImpl.insertPickRecord(pickRecord);
				}
			}
		}
		itemChange.setBasicItemId(basicItemId);
		if ((machineInfo.getCurrWeight() <= 0)) {
			itemChange.setChangeNum(-machineInfo.getUnit());
		} else {
			itemChange.setChangeNum(machineInfo.getUnit());
		}
		itemChange.setVmCode(vmCode);
		itemChange.setItemName((String) itemMap.get("name"));
		itemChange.setPic((String) itemMap.get("pic"));
		itemChange.setPrice(Double.valueOf(itemMap.get("price").toString()));
		itemChange.setItemTypeId(Long.valueOf(itemMap.get("typeId").toString()));
		itemChange.setMachineWayId(wayId);
		itemChange.setWayNum(currWay);
		itemChange.setNum(preNum);
		BigDecimal realPrice = new BigDecimal(itemChange.getPrice().toString()).multiply(new BigDecimal(machineInfo.getUnit()));
		itemChange.setRealTotalPrice(realPrice.doubleValue());
		itemList.add(itemChange);
		machineInfo.setItemChangeList(itemList);
		redisService.set("replenish_" + vmCode, JsonUtil.toJson(itemList), 60 * 10);
		clearRedisState(vmCode, machineInfo.getFactoryNumber());
		if (userVo.getType() == UserVo.USER_CUSTOMER) {
			if (machineInfo.getCurrWeight() > 0 && machineInfo.getUnit() > 0) {// 有异常
																				// 水比原来多了
																				// 此处应该通知系统管理员
				log.info("数量比原来多了。。。。。。。。。。。");
				log.info(machineInfo.toString());
				result.setCode(-99);
				result.setMsg("购买流程，水数量比原来多了");
				return result;
			}
			if (machineInfo.getUnit() == 0) {// 表示水量没变化
//				PayRecordBean createPayRecord = blankOrderCreator.createPayRecord(userVo, vmCode, machineInfo.getCurrWay());
//				blankOrderCreator.createPayRecordItem(machineInfo.getItemChangeList(), createPayRecord.getId());
				log.info("水量没变化，不生成订单。。。。。。。。。。。");
				log.info(machineInfo.toString());
				result.setCode(-99);
				result.setMsg("水量没变化，不生成订单");
				return result;
			}
			
			payRecord(machineInfo,userVo,operateHistoryId,basicItemId.toString(),footmark,"正常订单");
			//设置购买测试
			setRedisPurchaseTest(vmCode, currWay);
		} else {// 补货人员
			// 调整货道商品数量 记录日志
			int num = 0;
			Integer unit = machineInfo.getUnit();
			if (machineInfo.getCurrWeight() <= 0) {// 加了水
				unit = -unit;
			}
			num = preNum + unit;
			orderDao.updateStockAndLog(wayId, vmCode, machineInfo.getCurrWay(), num, preNum, userVo.getId(), 2,
					basicItemId, itemId,footmark!=null?footmark.getFootmarkId():null);// 2是运维补水
		}

		return result;
	}
	@Override
	public OrderBean getMessageByPayCode(String payCode) {
		OrderBean orderBean = orderDao.getMessageByPayCode(payCode);
		return orderBean;
	}
	private void clearRedisState(String vmCode, String factoryNumber) {
		// 删除开门标识
		long l = redisService.del(MachinesKey.currentMachinesOpenState, vmCode);
		//log.info(vmCode + ": del the open state = " + l);
		redisService.set("ConsOperateHistoryUserId-"+vmCode, redisService.get(MachinesKey.currentMachinesUser,vmCode),60);
		redisService.set("ConsOperateHistoryId-"+vmCode, redisService.getString("OperateHistoryId-" + vmCode) ,60);
		l = redisService.del(MachinesKey.currentMachinesUser, vmCode);
		//log.info(vmCode + ": del the curr user = " + l);
		l = redisService.del("CurrWay-" + vmCode);
		//log.info(vmCode + ": del curr way =" + l);
		l = redisService.del("OperateHistoryId-" + vmCode);
		//log.info(vmCode + ": del open door OperateHistodyId  =" + l);
		l = redisService.del("OHM-" + factoryNumber);
		//log.info(vmCode + ": del open door state ohm = " + l);
//		l = redisService.del("GOODS_" + factoryNumber);
//		log.info(vmCode + ": del GOODS change info = " + l);
		l = redisService.del("PHM-" + factoryNumber);
		//log.info(vmCode + ": del open door heart phm = " + l);
//		l = redisService.del("DOHM-" + factoryNumber);
//		log.info(vmCode + ": del DOHM = " + l);
	}

	/**
	 * 重设货道水量
	 * 
	 * @param
	 *            机器心跳
	 * @return
	 */
	@Override
	@SuppressWarnings("unused")
	public ResultBean<String> resetWaysNum(String vmCode) {

		ResultBean<String> result = new ResultBean<String>();
		String factoryNumber = orderDao.getFactoryNumberByVmCode(vmCode);
		String heartbeat = getHeartbeatStr(factoryNumber);
		if (heartbeat == null) {
			result.setCode(-1);
			result.setMsg("找不到该机器的心跳");
			return result;
		}
		/**
		 * MachineInfo 机器信息封装到该对象，然后运算，得出购水数量金额等
		 */
		MachineInfo machineInfo = new MachineInfo(heartbeat);
		machineInfo.setVmCode(vmCode);
		UserVo userVo = UserUtil.getUser();// redisService.get(MachinesKey.currentMachinesUser,
											// machineInfo.getVmCode(),
											// UserVo.class);
		if (userVo == null) {// 找不到当前用户
			// 记录开门心跳日志
			orderDao.saveHeartBeatLog(heartbeat, factoryNumber, 'r', null,null,null);
			log.info("heartbeat=" + heartbeat);
			log.info("vmCode=" + vmCode + " 找不到当前用户【运维】");
			result.setCode(-1);
			result.setMsg("找不到当前用户");
			return result;
		}
		String enterIdString = redisService.getString("EnterId"+userVo.getOpenId());
		Long enterId = enterIdString==null?null:Long.valueOf(enterIdString);
		FootmarkBean footmark = new FootmarkBean(userVo, ModeEnum.REVISE_ALL_MODE, vmCode,enterId);
		Long id = footmarkService.insertFootmark(footmark);
		// 记录开门心跳日志
		orderDao.saveHeartBeatLog(heartbeat, factoryNumber, 'r', userVo.getId(),userVo.getType(),id);
		log.info("heartbeat=" + heartbeat);
		Long payType = userVo.getPayType();// 来源类型 1 微信 2 支付宝

		// 计算购买哪个货道的水
		// 获取货道基础信息如：状态、商品id、当前数量、容量
		List<Map<String, Object>> listWayMap = orderDao.getWays(vmCode);
		for (Map<String, Object> wayMap : listWayMap) {
			Long wayId = (Long) wayMap.get("id");
			Long itemId = (Long) wayMap.get("itemId");
			int preNum = (Integer) wayMap.get("num");
			int wayNumber = (Integer) wayMap.get("wayNumber");
			Map<String, Object> itemMap = orderDao.getItemInfo(itemId);
			Long basicItemId = ((Integer) itemMap.get("id")) * 1l;

			List<Way> listWay = machineInfo.getList();
			for (Way way : listWay) {
				if (way.getWayNumber() == wayNumber) {
					way.setNum(preNum);
					way.setId(wayId);
					way.setStandard(Integer.valueOf((String) itemMap.get("standard")));
					// 计算出来的数量
					int calculateNum = machineInfo.calculateHowUnit(way.getWeight(), way.getStandard());
					way.setCalculateNum(calculateNum);
					way.setBasicItemId(basicItemId);
					way.setItemId(itemId);

				}
			}

		}
		log.info("计算结果--reset-");
		log.info(machineInfo.toString());
		// 修改数据库
		List<Way> listway = machineInfo.getList();

		for (Way way : listway) {
			orderDao.updateStockAndLog(way.getId(), vmCode, way.getWayNumber(), way.getCalculateNum(), way.getNum(),
					userVo.getId(), 3, way.getBasicItemId(), way.getItemId(),id);// 3
																				// 重设货道水量
		}
		result.setData("done");
		return result;
	}

	/**
	 * 自动扣款
	 * 
	 * @param payCode
	 * @param price
	 * @param contractId
	 * @param itemName
	 */
	// @SuppressWarnings("unchecked")
	private String deposit(String payCode, BigDecimal price, String contractId, String itemName, Integer companyId,
			UserVo userVo) {
		String requestWithoutCert = null;
		Map<String, String> map = new HashMap<String,String>();
		WXPayConfig wxPayConfig = wxPayConfigFactory.getWXPayConfig(companyId);
		WXPay wxPay = wxPayConfigFactory.getWXPay(companyId);
		map.put("appid", wxPayConfig.getAppID());
		map.put("mch_id", wxPayConfig.getMchID());
		map.put("nonce_str", UUID.randomUUID().toString().replaceAll("-", ""));
		String cutName = itemName;
		if (itemName.length() > 32) {
			cutName = itemName.substring(0, 32);
		}
		map.put("body", cutName);
		map.put("detail", itemName);
		map.put("attach", userVo.getId().toString());
		// map.put("detail", );
		map.put("out_trade_no", payCode);
		map.put("total_fee", price.intValue() + "");
		// spbill_create_ip
		String ip = getLocalIp();
		map.put("spbill_create_ip", ip);
		// map.put("goods_tag", );
		map.put("notify_url", depositNotifyUrl);
		map.put("trade_type", "PAP");
		map.put("contract_id", contractId);
		try {
			String sign = WXPayUtil.generateSignature(map, wxPayConfig.getKey());
			map.put("sign", sign);
		} catch (Exception e) {
			//发短信提醒
			payFailSendMsg(userVo.getId() != null ? Long.valueOf(userVo.getId()) : null);
			log.info("微信扣款签名失败");
			log.info(e.getMessage());
			e.printStackTrace();
		}
		try {
			requestWithoutCert = wxPay.requestWithoutCert("/pay/pappayapply", map, 5000,
					10000);
			log.info("requestWithoutCert"+requestWithoutCert);
		} catch (Exception e) {
			log.info("微信扣款异常");
			log.info(e.getMessage());
			e.printStackTrace();
		}
		return requestWithoutCert;

	}

	private String getLocalIp() {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String ip = addr.getHostAddress().toString(); // 获取本机ip
		return ip;
	}


	private String getHeartbeatStr(String factoryNumber) {
		String content = redisService.getString("HM-" + factoryNumber);
		return content;
	}
	
	public CanOpenResult canOpenHMRedis(String factoryNum,UserVo userVo,String vmCode){
		String heartBeat = redisService.getString("HM-"+factoryNum);
		if(heartBeat == null){
			return new CanOpenResult(false,OpenStateEnum.NOT_HEART.getState());
		}
		Boolean exists = redisService.exists("OHM-" + factoryNum);
		if(exists){
			UserVo lastUser = redisService.get(MachinesKey.currentMachinesUser,vmCode,UserVo.class);
			if(lastUser != null && !lastUser.equals(userVo)){
				return new CanOpenResult(false,OpenStateEnum.REOPEN_FAIL.getState());
			}
		}
		if(heartBeat.contains("s:3") || heartBeat.contains("s:0") || heartBeat.contains("s:6")){
			redisService.del("OHM-" + factoryNum);
			return new CanOpenResult(true,null);
		} else if (heartBeat.contains("s:4")){
			return new CanOpenResult(false,OpenStateEnum.MACHINES_BREAK_DOWN.getState());
		} else {
			return new CanOpenResult(false,OpenStateEnum.MACHINES_BUSY.getState());
		}
	}

	
	/**
	 * 发送命令请求开门
	 */
	@Override
	public String sendCommandToOpenDoor1(Map<String, Object> result , String vmCode, int way) {
		String returnValue = null;
		UserVo userVo = UserUtil.getUser();
		String factoryNumber = result.get("factoryNumber") != null ? result.get("factoryNumber").toString() : null;
		String totalWayNum = result.get("totalWayNum") != null ? result.get("totalWayNum").toString() : null;
		Integer machineVersion = (Integer) result.get("machineVersion");
		int wayNum = 4;
		if (StringUtil.isBlank(factoryNumber)) {
			log.info(vmCode + "：该机器出厂编号为空，无法开门,开门用户信息：" + JsonUtil.toJson(userVo));
			return returnValue;
		}
		String redisVmCode = redisService.get(MachinesKey.getVmCodeByFactoryNum, factoryNumber);
		if (StringUtil.isBlank(redisVmCode) || !redisVmCode.equals(vmCode)) {
			redisService.set(MachinesKey.getVmCodeByFactoryNum, factoryNumber, vmCode);
		}
		CanOpenResult canOpenHMRedis = canOpenHMRedis(factoryNumber,userVo,vmCode);
		if (!canOpenHMRedis.getCanOpen()) {
			return canOpenHMRedis.getState();
		}
		if (StringUtil.isNotBlank(totalWayNum)) {
			try {
				wayNum = Integer.parseInt(totalWayNum.toString());
			} catch (Exception e) {
				log.info(vmCode + "：该机器货道总数是非数字，无法生成开门命令");
				return returnValue;
			}
		} else {
			log.info(vmCode + ":该机器总货道数为空或者null，无法生成开门命令");
			return returnValue;
		}
		factoryNumber = factoryNumber.trim();
		String command = genCommandStr(factoryNumber, way, wayNum);
		log.info("command===" + command);
		String openHeart = getHeartbeatStr(factoryNumber);
		MachineInfo info = new MachineInfo(openHeart);
		Way way2 = info.getList().get(way-1);
		if(way2 == null || way2.getWeight()==0){
			return "-1";
		}
		DoorOperateHistoryBean doorOperateHistory = Msg2DoorOperateConverter.setDoorOperateHistoryData(info, way,
				vmCode, userVo);
		// 保存开门记录
		DoorOperateHistoryBean save = doorOperationDao.save(doorOperateHistory);
		OpenDoorVo openDoorVo = new OpenDoorVo();
		URI uri = null;
		try {
			uri = new URIBuilder().setScheme("http").setHost(heartBeatHost).setPath("/openDoor").build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		openDoorVo.setFromClient(userVo.getId().toString());
		openDoorVo.setMessage(command);
		openDoorVo.setToClient(factoryNumber);
		returnValue = HttpUtil.post(uri,JsonUtil.toJson(openDoorVo));
		if (OpenStateEnum.OPEN_SUCCESS.getState().equals(returnValue)) {
			if (CommandVersionEnum.VER2.getState().equals(machineVersion)) {
				returnValue = OpenStateEnum.NEW_OPEN_SUCCESS.getState();
			}
			//准备开门的标志位
			redisService.set("openflag-"+factoryNumber, "1",3);
			redisService.set("closeflag-"+factoryNumber,"1",300);
			if(userVo.getType() == UserVo.USER_CUSTOMER){
				PayRecordBean createPayRecord = blankOrderCreator.createPayRecord(userVo, vmCode, way,save.getId());
				List<ItemInfoDto> oneWayItmeInfo = null;
				if(CommandVersionEnum.VER1.getState().equals(machineVersion)){
					oneWayItmeInfo = vmwayDao.getOneWayItemInfo(vmCode, way);
				}else{
					oneWayItmeInfo = vmwayItemDao.getOneWayItmeInfo(vmCode,way);
				}
				blankOrderCreator.createPayRecordItemByItemInfo(oneWayItmeInfo, createPayRecord.getId());
				redisService.set("CurrOrderId"+vmCode,createPayRecord.getId().toString(),1200);
			}
			// 如果门已经打开
			save.setStatus(DoorOperateEventEnum.OPENED.getCode());
			doorOperationDao.update(save);
			// 设置当前机器的用户
			redisService.set(MachinesKey.currentMachinesUser, vmCode, userVo);
			// 设置当前机器货道
			redisService.set("CurrWay-" + vmCode, way + "",3600);
			// 设置门的在redis里的状态
			redisService.set(MachinesKey.currentMachinesOpenState, vmCode, "the machine is opened");
			// 设置开门时的开门历史记录表的id
			redisService.set("OperateHistoryId-" + vmCode, save.getId().toString(),1200);

			redisService.set("OHM-" + factoryNumber, "open", 10);
		} else {
			save.setOpenFailedTime(new Date());
			save.setStatus(DoorOperateEventEnum.FAILED.getCode());
			doorOperationDao.update(save);
		}
		String enterIdString = redisService.getString("EnterId"+userVo.getOpenId());
		Long enterId = enterIdString==null?null:Long.valueOf(enterIdString);
		Long id = footmarkService.insertFootmarkAndRedis(userVo, ModeEnum.CHANGE_STOCK_MODE, vmCode,enterId);
		orderDao.saveHeartBeatLog(command, factoryNumber, 'o', userVo == null ? null : userVo.getId(), userVo == null ? null : userVo.getType(),id);
		return returnValue;
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

	@Override
	public ReturnDataUtil myOrderList(int curPage, Long customerId) {

		return payRecordDaoImpl.listPage(curPage, customerId);
	}

	@Override
	public Map<String, Object> getOrder(Long orderId) {
		return orderDao.getOrder(orderId);
	}

	/**
	 * 微信支付成功回调 修改订单状态
	 */
	@Override
	public void paySuccessOrder(String outTradeNo, String transactionId) {

		String sql = "update pay_record set ptCode='" + transactionId
				+ "'  ,payTime=current_timestamp()  ,finishTime=current_timestamp()  ,payType=1 ,state="
				+ PayStateEnum.PAY_SUCCESS.getState() + "  where payCode='" + outTradeNo + "'";
		log.info(sql);
		int result=orderDao.upate(sql);
		if(result<=0) {
			sql = "update pay_record_vision set ptCode='" + transactionId
					+ "'  ,payTime=current_timestamp()  ,finishTime=current_timestamp()  ,payType=1 ,state="
					+ PayStateEnum.PAY_SUCCESS.getState() + "  where payCode='" + outTradeNo + "'";
			orderDao.upate(sql);
		}
	}

	/**
	 * 记录签约信息 微信
	 */
	@Override
	public void insertEntrustInfo(Map map) {
		//log.info("签约记录。。。。");
		StringBuffer sql = new StringBuffer(
				"insert into wechat_entrust(changeType,contractCode,contractId,mchId,openid,planId,requestSerial,createTime,companyId) values (");
		sql.append("'" + (String) map.get("change_type") + "',");
		sql.append("'" + (String) map.get("contract_code") + "',");
		sql.append("'" + (String) map.get("contract_id") + "',");
		sql.append("'" + (String) map.get("mch_id") + "',");
		sql.append("'" + (String) map.get("openid") + "',");
		sql.append("'" + (String) map.get("plan_id") + "',");
		sql.append("'" + (String) map.get("request_serial") + "',");
		sql.append("CURRENT_TIMESTAMP(),");
		sql.append("'" + map.get("companyId") + "')");
		log.info(sql.toString());
		orderDao.insert(sql.toString());
	}

	@Override
	public boolean jieyue(Map map) {

		String openid = (String) map.get("openid");
		String mchId = (String) map.get("mch_id");

		String sql = "delete from wechat_entrust where mchId = '" + mchId + "' AND openid='" + openid
				+ "' and changeType='ADD'";
		orderDao.delete(sql);
		insertEntrustInfo(map);

		return true;
	}

	@Override
	public boolean isEntrust(Integer companyId, String openid) {

		String sql = "select id from wechat_entrust where companyId = '" + companyId + "' and openid='" + openid
				+ "' and changeType='ADD'";
		log.info(sql);
		long count = orderDao.selectCountBySql(sql, null);
		if (count > 0)
			return true;
		return false;
	}

	// 是否有未支付订单
	@Override
	public boolean isHaveNotPayOrder(Long id) {

		return orderDao.isHaveNotPayOrder(id);
	}


	@Override
	public void paySuccessStroeOrder(Integer distributionModel,String outTradeNo, String transactionId,Integer type) {
		if(type==0) {
			if (distributionModel==1) {
				String sql = "update store_order set ptCode='" + transactionId
						+ "'  ,payTime=current_timestamp() ,payType=1 ,state=" + PayStateEnum.PAY_SUCCESS.getState()
						+ "  where payCode='" + outTradeNo + "'";
				log.info(sql);
				orderDao.upate(sql);
			}else {
				String sql = "update store_order set ptCode='" + transactionId
						+ "'  ,payTime=current_timestamp() ,payType=1 ,state=" + PayStateEnum.Delivering.getState()
						+ "  where payCode='" + outTradeNo + "'";
				log.info(sql);
				orderDao.upate(sql);
			}
		}else{
			String sql = "update store_order set ptCode='" + transactionId
					+ "'  ,payTime=current_timestamp() ,payType=1 ,state=" + PayStateEnum.PAY_SUCCESS.getState()
					+ "  ,spellgroupState="+type+"  where payCode='" + outTradeNo + "'";
			log.info(sql);
			orderDao.upate(sql);
		}
	}

	@Override
	public List<String> findItemName(String product) {
		String sql = "select itemName from shopping_car where id in(" + product + ")";
		log.info(sql);
		List<String> qury = orderDao.qury(sql);
		return qury;
	}

	

	@Override
	public String getFactoryNumberByVmCode(String vmCode) {
		return orderDao.getFactoryNumberByVmCode(vmCode);
	}

	
	

	@Override
	public Integer getVersionByFactoryNum(String factoryNum) {
		Integer version = orderDao.getVersionByFactoryNum(factoryNum);
		return version;
	}

	public void payRecord(MachineInfo machineInfo, UserVo userVo, Long operateHistoryId, String items,
			FootmarkRedisBean footmark,String remark) {
		boolean existsOpenBeforeOrder = false;
		boolean lessThanTenNumber = true;
		Long pid = null;//补扣订单的父id
		Date date = new Date();
		long payType = userVo.getPayType();
		String vmCode = machineInfo.getVmCode();
//		String orderIdString = redisService.getString("CurrOrderId"+vmCode);
//		redisService.del("CurrOrderId"+vmCode);
		PayRecordBean payRecord = null;
		if(operateHistoryId != null){
			payRecord = payRecordDaoImpl.getByOperateId(operateHistoryId);
			if(payRecord!=null){
				pid = payRecord.getId();
				if(remark.equals("补扣订单") && payRecord.getNum()==1) {
					log.info("补扣订单数量为1"+payRecord.getId());
					existsOpenBeforeOrder = false;
				}else if(!PayStateEnum.BLANK_ORDER.getState().equals(Integer.valueOf(payRecord.getState().intValue()))){
					return ;
				}else {
					existsOpenBeforeOrder = true;
				}
			}
		}
		if(!existsOpenBeforeOrder){
			payRecord = new PayRecordBean();
			payRecord.setPid(pid);
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
		payRecord.setOperateHistoryId(operateHistoryId);// 关联开关门记录id
		payRecord.setState(PayStateEnum.NOT_PAY.getState() * 1l);// 订单创建
		// payRecord.setPayType(payType);
		payRecord.setSrcType(userVo.getPayType().intValue());
		payRecord.setPrice(machineInfo.getTotalPrice());
		VminfoDto queryByVmCode = vminfoService.queryByVmCode(vmCode);
		// 获取该机器所属路线，区域，公司
		MachinesLAC mlac = couponDaoImpl.getMachinesLAC(vmCode);
		boolean canPartakeActivity = true;
		Map<CarryDto, Integer> usedCarryMap = new HashMap<CarryDto, Integer>();
		Map<CouponBean, Integer> usedCouponMap = new HashMap<CouponBean, Integer>();
		Map<CarryDto, List<Long>> carryMap = carryWaterDao.getCarryWaterVoucherByCustomer(userVo.getId(), mlac, items);
		if(carryMap != null && carryMap.size() > 0){
			canPartakeActivity = false;
			BigDecimal finalPrice = carryExecute.chooceAndCalculate(carryMap, usedCarryMap, machineInfo);
			if(finalPrice.compareTo(payRecord.getPrice())<0){
				payRecord.setPrice(finalPrice);
			}
			for(Map.Entry<CarryDto, Integer> carryEntry : usedCarryMap.entrySet()){
				carryCustomerDao.reduceCarry(carryEntry.getKey().getCarryCustomerId(), carryEntry.getValue());
			}
		}
		//是否可以参与活动或者优惠券
		if(canPartakeActivity){
			// 是否可以使用优惠券
			boolean canUseCoupon = true;
			Map<ActivityBean, List<Long>> activityMap = activityDao.getMoreGoodsActivity(mlac, machineInfo.getTotalPrice(),
					items);
			Set<ActivityBean> keySet = activityMap.keySet();
			if (keySet != null && keySet.size() > 0) {
				Map<String, Object> result = activityExecute.chooceAndCalculate(activityMap, machineInfo);
				if ((boolean) result.get("canDo")) {
					BigDecimal finalPrice = (BigDecimal) result.get("finalPrice");
					if(payRecord.getPrice().compareTo(finalPrice) <= 0){
						canUseCoupon = true;
					}else{
						canUseCoupon = false;
						payRecord.setPrice(finalPrice);
					}
				}
			}
			if (canUseCoupon) {
				MoreGoodsCouponDto moreGoodsCoupon = couponDaoImpl.getMoreGoodsCoupon(userVo.getId(), mlac,
						machineInfo.getTotalPrice().doubleValue(), items);
				Set<CouponBean> canUseCouponSet = moreGoodsCoupon.getCouponMap().keySet();
				if (canUseCouponSet != null && canUseCouponSet.size() > 0) {
					BigDecimal finalPrice = couponExecute.chooceAndCalculate(moreGoodsCoupon, machineInfo, usedCouponMap);
					if(finalPrice.compareTo(payRecord.getPrice())<0){
						payRecord.setPrice(finalPrice);
					}
					// 将使用的优惠券减少
					for (Map.Entry<CouponBean, Integer> entry : usedCouponMap.entrySet()) {
						couponDaoImpl.reduceCoupon(entry.getKey().getCouponCustomerId(), entry.getValue());
					}
				}
			}
		}
		
		BigDecimal limit = new BigDecimal("0.01");
		payRecord.setPrice(payRecord.getPrice().setScale(2, BigDecimal.ROUND_DOWN));
		boolean useMemberMoney = false;
		BigDecimal useMoney = null;
		OrderResultBean orderResult = new OrderResultBean();
		lessThanTenNumber=checkItTenNum(machineInfo.getItemChangeList());
		if(payRecord.getPrice().compareTo(limit)>=0 && machinesDao.isSonCompany(mlac.getCompanyId(), CompanyEnum.YOUSHUI.getCompanyId()) && lessThanTenNumber){
			BigDecimal memberMoney = memberDao.getMemberMoney(userVo.getId());
			if(memberMoney!=null && memberMoney.compareTo(payRecord.getPrice())>=0 && memberDao.updateMemberMoney(userVo.getId(), payRecord.getPrice())){
				useMoney = payRecord.getPrice();
				payRecord.setPrice(BigDecimal.ZERO);
				payRecord.setMemberPay(useMoney);
				//优水余额支付
				payRecord.setPayType(PayTypeEnum.YS_BALANCE.getIndex());
				useMemberMoney = true;
			}
		}
		if (payRecord.getPrice().compareTo(limit) < 0) {
			payRecord.setState(Long.valueOf(PayStateEnum.PAY_SUCCESS.getState()));
			payRecord.setPayTime(date);
			payRecord.setFinishTime(date);
			payRecord.setPrice(BigDecimal.ZERO);
			payRecord.setRemark("订单金额0元");
			//用户积分更新
			//updateUserIntegral(userVo.getId(),machineInfo.getItemChangeList());
		}
		payRecord.setNum(machineInfo.getRealUnit() * 1l);
		List<Long> couponIds = new ArrayList<Long>();
		for (CouponBean coupon : usedCouponMap.keySet()) {
			couponIds.add(coupon.getId());
		}
		String join = StringUtils.join(couponIds,",");
		payRecord.setCouponId(join);
		StringBuffer itemName = new StringBuffer();
		for (ItemChangeDto itemChangeDto : machineInfo.getItemChangeList()) {
			if (itemChangeDto.getRealNum() < 0) {
				int changeNum = Math.abs(itemChangeDto.getRealNum());
				itemName.append(itemChangeDto.getItemName()).append("*").append(changeNum).append(",");
			}
		}
		String realName = itemName.substring(0, itemName.length() - 1);
		payRecord.setItemName(realName);
		// 生成订单 扣减库存
		payRecordDaoImpl.newCreatePayRecordAndReduceStock(payRecord, machineInfo, userVo,footmark!=null?footmark.getFootmarkId():null,existsOpenBeforeOrder);
		userActiveDegreeDao.update(userVo.getId(),machineInfo.getUnit());
	
		if(remark.equals("正常订单")) {
			redisService.del(NormalRedisKey.openflag,machineInfo.getFactoryNumber());
			redisService.set(NormalRedisKey.closeflag,machineInfo.getFactoryNumber(),"1");
		}
		
		if(!existsOpenBeforeOrder){
			for (ItemChangeDto itemChangeDto : machineInfo.getItemChangeList()) {
				if (itemChangeDto.getChangeNum() < 0) {
					int changeNum = Math.abs(itemChangeDto.getChangeNum());
					PayRecordItemBean recordItem = new PayRecordItemBean();
					recordItem.setBasicItemId(itemChangeDto.getBasicItemId());
					recordItem.setItemName(itemChangeDto.getItemName());
					recordItem.setNum(changeNum);
					recordItem.setPayRecordId(payRecord.getId());
					recordItem.setPrice(new BigDecimal(itemChangeDto.getPrice().toString())
							.multiply(new BigDecimal(changeNum)).doubleValue());
					if (useMemberMoney) {
						recordItem.setRealTotalPrice(0d);
					} else {
						recordItem.setRealTotalPrice(itemChangeDto.getRealTotalPrice());
					}
					recordItem.setFinalNum(recordItem.getNum());
					recordItem.setFinalTotalPrice(recordItem.getRealTotalPrice());
					payRecordItemDao.insert(recordItem);
				}
			}
		}else{
			List<PayRecordItemBean> payRecordItemList = payRecordItemDao.getListByPayRecordId(payRecord.getId());
			for (PayRecordItemBean payRecordItemBean : payRecordItemList) {
				for(ItemChangeDto itemChangeDto : machineInfo.getItemChangeList()){
					if(itemChangeDto.getBasicItemId().equals(payRecordItemBean.getBasicItemId())
							&& itemChangeDto.getChangeNum() < 0){
						int changeNum = Math.abs(itemChangeDto.getChangeNum());
						payRecordItemBean.setNum(changeNum);
						payRecordItemBean.setPrice(new BigDecimal(itemChangeDto.getPrice().toString())
								.multiply(new BigDecimal(changeNum)).doubleValue());
						if (useMemberMoney) {
							payRecordItemBean.setRealTotalPrice(0d);
						} else {
							payRecordItemBean.setRealTotalPrice(itemChangeDto.getRealTotalPrice());
						}
						payRecordItemBean.setFinalNum(payRecordItemBean.getNum());
						payRecordItemBean.setFinalTotalPrice(payRecordItemBean.getRealTotalPrice());
						payRecordItemDao.updatePayRecordItem(payRecordItemBean);
					}
				}
			}
		}
		
		if(useMemberMoney){
			orderResult.setPayType("1");//1 余额支付  其他 null
			orderResult.setMemberMoney(useMoney);
			MemberUseLog memberLog = new MemberUseLog();
			memberLog.setCustomerId(userVo.getId());
			memberLog.setOrderId(payRecord.getId());
			memberLog.setUseMoney(useMoney);
			memberDao.insertMemberUseLog(memberLog);
		}
		if (usedCarryMap != null && usedCarryMap.size() > 0) {
			Integer sumCarryRecordNum=0;
			for (Map.Entry<CarryDto, Integer> entry : usedCarryMap.entrySet()) {
				CarryRecordBean carryRecord = new CarryRecordBean();
				carryRecord.setCarryId(entry.getKey().getCarryId());
				carryRecord.setCustomerId(userVo.getId());
				carryRecord.setNum(entry.getValue());
				carryRecord.setOrderId(payRecord.getId());
				sumCarryRecordNum=sumCarryRecordNum+entry.getValue();
				//插入提水券使用记录
				carryRecordDao.insertCarryRecord(carryRecord);
				//修改拼团订单状态
				if(entry.getKey().getOrderId()!=null&&entry.getKey().getOrderId()>0) {
					paySpellgroupStroeOrder(entry.getKey().getOrderId()+"",0);
				}
				
			}
			orderResult.setCarryWaterCouponName(sumCarryRecordNum+"张");
			orderResult.setType("2");
			orderResult.setOrderId(payRecord.getId().toString());
		}
		if (usedCouponMap != null && usedCouponMap.size() > 0) {
			for (Map.Entry<CouponBean, Integer> entry : usedCouponMap.entrySet()) {
				CouponLog couponLog = new CouponLog();
				couponLog.setCouponId(entry.getKey().getId());
				couponLog.setOrderId(payRecord.getId());
				couponLog.setCouponCustomerId(userVo.getId());
				couponLog.setMoney(machineInfo.getTotalPrice().doubleValue());
				couponLog.setDeductionMoney(entry.getKey().getDeductionMoney());
				couponLog.setType(1);// 机器优惠券1商城优惠券2
				// 插入优惠券使用记录
				storeOrderDaoImpl.insertCouponLog(couponLog);
			}
			orderResult.setCouponName(usedCouponMap.size()+"张");
			orderResult.setType("1");
			orderResult.setCouponId(payRecord.getCouponId());
			userActiveDegreeDao.update(userVo.getId(), RegisterFlowEnum.USED_PERCENT);
		}
		// 转盘 设置为购物后可参与 时，会用次标志位
		redisService.set("orderUserId" + userVo.getId(), "CREATE", 300);
		//记录支付完成页参数
		BigDecimal sumDeductionMoney=machineInfo.getTotalPrice().subtract(payRecord.getPrice());
		if(orderResult.getPayType()!=null){
			 sumDeductionMoney=machineInfo.getTotalPrice().subtract(orderResult.getMemberMoney());
		}
		orderResult.setPrice(payRecord.getPrice().toString());
		orderResult.setSumDeductionMoney(sumDeductionMoney.toString());
		//log.info("原金额"+machineInfo.getTotalPrice()+"终金额"+payRecord.getPrice()+"优惠金额"+sumDeductionMoney.toString());
		if(lessThanTenNumber==false) {
			orderResult.setPayState("0");
		}else {
			orderResult.setPayState("1");
		}
		redisService.set("orderReuslt_"+vmCode, orderResult,300);

		CustomerBean customer = aliCustomerService.queryById(userVo.getId());
		if (payRecord.getPrice().doubleValue() <=0) {
			sendInviteCoupon(customer);//被邀请人首次购买送邀请人券 
		}
		//不为商品类型克的数量大于10  金额不变  手动支付  
		else if (payRecord.getPrice().doubleValue() > 0 && lessThanTenNumber) {
			if (payType == UserVo.Pay_Type_ZhiFuBao) {// 支付宝 调用扣款程序
				String agreementNo = alipayService.querySign(customer.getAlipayUserId(), vmCode);
				String ptCode = alipayService.cutPayment(payRecord.getPrice(), payRecord.getPayCode(),
						payRecord.getItemName(), agreementNo);

				if(StringUtil.isNotBlank(ptCode)) {
					//用户积分更新
					//updateUserIntegral(userVo.getId(),machineInfo.getItemChangeList());
					//短信商户通知
					if (payRecord.getItemName().contains("乔府五常大米") || payRecord.getItemName().contains("长粒香米") || payRecord.getItemName().contains("稻花香米")){
					//if(payRecord.getItemName().contains("L")){
						LocalTime time = LocalTime.now();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						String buyTime=time.format(formatter);
						String msg=buyTime+" "+"用户在"+payRecord.getVendingMachinesCode()+"售货机上用支付宝购买了"+payRecord.getItemName();
						smsService.sendMsg("15986666885",msg);
					}
					sendInviteCoupon(customer);//被邀请人首次购买送邀请人券 
					redisService.set("orderUserId" + userVo.getId(), "SUCCESS", 300);
				} else {
					payFailSendMsg(userVo.getId());
				}
			} else if (payType == UserVo.Pay_Type_WeiXin) {
				Integer query = queryByVmCode.getCompanyId();
				MyWXPayConfig wxPayConfig = (MyWXPayConfig) wxPayConfigFactory.getWXPayConfig(query);
				Integer usingCompanyConfig = wxPayConfig.getUsingCompanyConfig();
				String contractId = orderDao.getContractId(usingCompanyConfig, userVo.getOpenId());
/*				if(vmCode.equals("1999990000001")) {
					https://api.mch.weixin.qq.com/v3/payscore/smartretail-orders/{out_order_no}/complete
					FinishOrderBean finishOrderBean = new FinishOrderBean();
					finishOrderBean.setAppid(appid);
					finishOrderBean.setService_id(service_id);
					finishOrderBean.setFinish_type(finish_type);
					finishOrderBean.setFinish_ticket(finish_ticket);
				}else {*/
					this.deposit(payRecord.getPayCode(), payRecord.getPrice().multiply(new BigDecimal(100)), contractId, realName,
							queryByVmCode.getCompanyId(), userVo);
//				}

			}
			
		}
		
		if(remark.equals("补扣订单")) {
			//补扣时再发一次校准
			log.info(machineInfo.getVmCode()+"补扣校准规格");
			List<VmwayItemBean> list=vmwayItemDao.findItemWeight(machineInfo.getFactoryNumber(),machineInfo.getCurrWay());
			if(list!=null && list.size()>0 ) { 
				redisService.set("ServiceOrderFlag_"+machineInfo.getFactoryNumber(),"1",3);
				String backValue = machinesClient.sendCommandTimes(userVo, machineInfo.getFactoryNumber(), CalculateUtil.reviseCommand(machineInfo, list),2);
				if(TransferStateEnum.SUCCESS.getState().equals(backValue)){
					redisService.del("ServiceOrderFlag_"+machineInfo.getFactoryNumber());
				}else {
					log.info("重新发送补扣校准规格失败"+machineInfo.getVmCode());
				}
			}
		}
	}

	@Override
	public void payRecordForFree(MachineInfo machineInfo, UserVo userVo, Long operateHistoryId, String items,
						  FootmarkRedisBean footmark,String remark) {
		boolean existsOpenBeforeOrder = false;
		Long pid = null;//补扣订单的父id
		Date date = new Date();
		long payType = userVo.getPayType();
		String vmCode = machineInfo.getVmCode();
//		String orderIdString = redisService.getString("CurrOrderId"+vmCode);
//		redisService.del("CurrOrderId"+vmCode);
		PayRecordBean payRecord = null;
		if(operateHistoryId != null){
			payRecord = payRecordDaoImpl.getByOperateId(operateHistoryId);
			if(payRecord!=null){
				pid = payRecord.getId();
				if(remark.equals("补扣订单") && payRecord.getNum()==1) {
					log.info("补扣订单数量为1"+payRecord.getId());
					existsOpenBeforeOrder = false;
				}else if(!PayStateEnum.BLANK_ORDER.getState().equals(Integer.valueOf(payRecord.getState().intValue()))){
					return ;
				}else {
					existsOpenBeforeOrder = true;
				}
			}
		}
		if(!existsOpenBeforeOrder){
			payRecord = new PayRecordBean();
			payRecord.setPid(pid);
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
		// 关联开关门记录id
		payRecord.setOperateHistoryId(operateHistoryId);
		payRecord.setState(PayStateEnum.FREE_ORDER.getState() * 1L);
		payRecord.setSrcType(userVo.getPayType().intValue());
		payRecord.setPrice(machineInfo.getTotalPrice());
		// 获取该机器所属路线，区域，公司

		//是否可以参与活动或者优惠券
		BigDecimal limit = new BigDecimal("0");
		payRecord.setPrice(limit);
		payRecord.setState(Long.valueOf(PayStateEnum.FREE_ORDER.getState()));
		payRecord.setPayTime(date);
		payRecord.setFinishTime(date);
		payRecord.setPrice(BigDecimal.ZERO);
		payRecord.setRemark("订单金额0元");
		payRecord.setNum(machineInfo.getRealUnit() * 1L);

		StringBuffer itemName = new StringBuffer();
		for (ItemChangeDto itemChangeDto : machineInfo.getItemChangeList()) {
			if (itemChangeDto.getRealNum() < 0) {
				int changeNum = Math.abs(itemChangeDto.getRealNum());
				itemName.append(itemChangeDto.getItemName()).append("*").append(changeNum).append(",");
			}
		}
		String realName = itemName.substring(0, itemName.length() - 1);
		payRecord.setItemName(realName);
		// 生成订单 扣减库存
		payRecordDaoImpl.newCreatePayRecordAndReduceStock(payRecord, machineInfo, userVo,footmark!=null?footmark.getFootmarkId():null,existsOpenBeforeOrder);
		userActiveDegreeDao.update(userVo.getId(),machineInfo.getUnit());

		if(remark.equals("正常订单")) {
			redisService.del(NormalRedisKey.openflag,machineInfo.getFactoryNumber());
			redisService.set(NormalRedisKey.closeflag,machineInfo.getFactoryNumber(),"1");
		}

		if(!existsOpenBeforeOrder){
			for (ItemChangeDto itemChangeDto : machineInfo.getItemChangeList()) {
				if (itemChangeDto.getChangeNum() < 0) {
					int changeNum = Math.abs(itemChangeDto.getChangeNum());
					PayRecordItemBean recordItem = new PayRecordItemBean();
					recordItem.setBasicItemId(itemChangeDto.getBasicItemId());
					recordItem.setItemName(itemChangeDto.getItemName());
					recordItem.setNum(changeNum);
					recordItem.setPayRecordId(payRecord.getId());
					recordItem.setPrice(new BigDecimal(itemChangeDto.getPrice().toString())
							.multiply(new BigDecimal(changeNum)).doubleValue());
					recordItem.setRealTotalPrice(0d);
					recordItem.setFinalNum(recordItem.getNum());
					recordItem.setFinalTotalPrice(recordItem.getRealTotalPrice());
					payRecordItemDao.insert(recordItem);
				}
			}
		}else{
			List<PayRecordItemBean> payRecordItemList = payRecordItemDao.getListByPayRecordId(payRecord.getId());
			for (PayRecordItemBean payRecordItemBean : payRecordItemList) {
				for(ItemChangeDto itemChangeDto : machineInfo.getItemChangeList()){
					if(itemChangeDto.getBasicItemId().equals(payRecordItemBean.getBasicItemId())
							&& itemChangeDto.getChangeNum() < 0){
						int changeNum = Math.abs(itemChangeDto.getChangeNum());
						payRecordItemBean.setNum(changeNum);
						payRecordItemBean.setPrice(new BigDecimal(itemChangeDto.getPrice().toString())
								.multiply(new BigDecimal(changeNum)).doubleValue());
							payRecordItemBean.setRealTotalPrice(0d);
						payRecordItemBean.setFinalNum(payRecordItemBean.getNum());
						payRecordItemBean.setFinalTotalPrice(payRecordItemBean.getRealTotalPrice());
						payRecordItemDao.updatePayRecordItem(payRecordItemBean);
					}
				}
			}
		}

		userActiveDegreeDao.update(userVo.getId(), RegisterFlowEnum.USED_PERCENT);


		if(remark.equals("补扣订单")) {
			//补扣时再发一次校准
			log.info(machineInfo.getVmCode()+"补扣校准规格");
			List<VmwayItemBean> list=vmwayItemDao.findItemWeight(machineInfo.getFactoryNumber(),machineInfo.getCurrWay());
			if(list!=null && list.size()>0 ) {
				redisService.set("ServiceOrderFlag_"+machineInfo.getFactoryNumber(),"1",3);
				String backValue = machinesClient.sendCommandTimes(userVo, machineInfo.getFactoryNumber(), CalculateUtil.reviseCommand(machineInfo, list),2);
				if(TransferStateEnum.SUCCESS.getState().equals(backValue)){
					redisService.del("ServiceOrderFlag_"+machineInfo.getFactoryNumber());
				}else {
					log.info("重新发送补扣校准规格失败"+machineInfo.getVmCode());
				}
			}
		}
	}
	/**
	 * 支付失败发送信息提醒
	 * @author hebiting
	 * @date 2018年10月19日上午9:20:22
	 */
	@Override
	public void payFailSendMsg(Long customerId){
		try{
			redisService.set("orderUserId" + customerId, "FAIL", 300);
//			TblCustomerBean tblCustomerBean = tblCustomerDao.get(customerId);
			String phone = tblCustomerDao.getPhoneByCustomerId(customerId);
			if (StringUtil.isNotBlank(phone)) {
				smsService.sendMsg(phone, "订单自动支付失败，请重新扫码或在优水到家公众号我的订单中进行手动支付!");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public Integer getVersionByVmCode(String vmCode) {
		return orderDao.getVersionByVmCode(vmCode);
	}

	@Override
	public ResultBean<?> moreGoodsOrder(String param) {
		log.info("关门信息：" + param);
		ResultBean<?> result = new ResultBean<>();
		// String goodsChangeInfo = redisService.getString("GOODS_"+factoryNum);
		// factNum:*****;goods:3;1:-1,2:-1;end
		if (StringUtil.isBlank(param)) {
			log.info("同层多商品机器关门redis心跳为空--" + param);
			result.setCode(-1);
			result.setMsg("同层多商品机器关门,参数传递为空");
			return result;
		}
		MachineInfo machineInfo = new MachineInfo();
		machineInfo.moreGoodsAllPaser(param);
		String factoryNum = machineInfo.getFactoryNumber();
		String vmCode = redisService.get(MachinesKey.getVmCodeByFactoryNum, factoryNum, String.class);
		if (vmCode == null) {
			vmCode = orderDao.getVmCodeByFactoryNumber(factoryNum);
			log.info("去数据库获取vmCode");
			redisService.set(MachinesKey.getVmCodeByFactoryNum, factoryNum, vmCode);
		}
		machineInfo.setVmCode(vmCode);
		VmbaseInfoDto baseInfo = vendingMachinesService.getBaseInfo(vmCode);

		UserVo userVo = redisService.get(MachinesKey.currentMachinesUser, machineInfo.getVmCode(), UserVo.class);
		if (userVo == null) {// 找不到当前用户
			log.info("vmCode=" + vmCode + " 找不到当前用户,此时param:"+param);
			result.setCode(-1);
			result.setMsg("找不到当前用户");
			clearRedisState(vmCode, machineInfo.getFactoryNumber());
			return result;
		}
		//转发到mms
		if(userVo.getType() == UserVo.USER_ADMIN && ("mms").equals(redisService.getString("mms"))){
			replenishClient.toMMSOneClosed(param, CommandVersionEnum.VER2.getState());
			return result;
		}
		
		String wayStr = redisService.getString("CurrWay-" + vmCode);//
		Integer currWay = Integer.valueOf(wayStr);
		machineInfo.setCurrWay(currWay);
		int total = 0;
		int realTotal = 0;
		Integer currWeight = 0;
		BigDecimal totalPrice = new BigDecimal(0);
		List<Long> itemList = new ArrayList<Long>();
		Iterator<ItemChangeDto> iterator = machineInfo.getItemChangeList().iterator();
		Long operateHistoryId = null;
		if (StringUtil.isNotBlank(redisService.getString("OperateHistoryId-" + vmCode))) {
			operateHistoryId = Long.valueOf(redisService.getString("OperateHistoryId-" + vmCode));
			DoorOperateHistoryBean doorOperate = new DoorOperateHistoryBean();
			doorOperate.setId(operateHistoryId);
			doorOperate.setClosedTime(new Date());
			doorOperate.setStatus(DoorOperateEventEnum.CLOSED.getCode());
			doorOperate.setRemark("关门成功");
			doorOperate.setNormalFlag(10);// 多商品货道状态为10
			boolean update = doorOperationDao.update(doorOperate);
			log.info(vmCode + ": 更新开关门记录状态--" + update);
		} else {
			log.info(vmCode + ":接受的开门记录id为null 或者 空");
		}
		while(iterator.hasNext()){
			ItemChangeDto itemChange = iterator.next();
			itemChange.setVmCode(vmCode);
			if(vmwayItemDao.getChangeInfo(itemChange)==null){
				iterator.remove();
				log.error(vmCode+"机器货道商品信息不正常"+param);
				continue;
			}
			if (itemChange.getChangeNum() < 0) {
				int num = Math.abs(itemChange.getChangeNum());
				realTotal += num;
				CustomerStockBean stock = null;
				if (userVo.getType() == UserVo.USER_CUSTOMER) {
					stock = storeOrderDaoImpl.getStockByBasicItem(itemChange.getBasicItemId(), userVo.getId());
					if (stock != null) {
						int stockNum = stock.getStock() - stock.getPickNum();
						int pickNum = 0;
						if (stockNum >= num) {
							itemChange.setChangeNum(0);
							stock.setPickNum(stock.getPickNum() + num);
							pickNum = num;
						} else {
							itemChange.setChangeNum(stockNum - num);
							stock.setPickNum(stock.getStock());
							pickNum = stockNum;
						}
						storeOrderDaoImpl.update(stock);
						CustomerPickRecord pickRecord = new CustomerPickRecord();
						pickRecord.setBasicItemId(stock.getBasicItemId());
						pickRecord.setCustomerId(userVo.getId());
						pickRecord.setVmCode(vmCode);
						pickRecord.setWayNum(machineInfo.getCurrWay());
						pickRecord.setPickNum(pickNum);
						storeOrderDaoImpl.insertPickRecord(pickRecord);
					}
				}
				if (itemChange.getChangeNum() < 0) {
					num = Math.abs(itemChange.getChangeNum());
					itemList.add(itemChange.getBasicItemId());
					currWeight += num * itemChange.getWeight();
					total += num;
					BigDecimal realTotalPrice = new BigDecimal(itemChange.getPrice().toString())
							.multiply(new BigDecimal(num)).setScale(2, BigDecimal.ROUND_DOWN);
					itemChange.setRealTotalPrice(realTotalPrice.doubleValue());
					totalPrice = totalPrice.add(realTotalPrice);
				}
			}
		}
		String items = StringUtils.join(itemList, ",");
		machineInfo.setCurrWeight(currWeight);
		machineInfo.setUnit(total);
		machineInfo.setRealUnit(realTotal);
		machineInfo.setTotalPrice(totalPrice);
		FootmarkRedisBean footmark = redisService.get("redis_mode_id"+vmCode, FootmarkRedisBean.class);

		
		if(ModeEnum.REVISE_MODE.getState().equals(footmark!=null?footmark.getModeId():null) || ModeEnum.ONCE_REVISE_MODE.getState().equals(footmark!=null?footmark.getModeId():null)){
			
		}else {
			redisService.del("redis_mode_id"+vmCode);
		}
		orderDao.saveHeartBeatLog(param, machineInfo.getFactoryNumber(), 'm', userVo.getId(),userVo.getType(),footmark!=null?footmark.getFootmarkId():null);
		// 获取当前开关门记录id,并更新关门记录

		redisService.set("replenish_" + vmCode, JsonUtil.toJson(machineInfo.getItemChangeList()), 60 * 10);
		// 清除redis标志位
		clearRedisState(vmCode, machineInfo.getFactoryNumber());
		if (userVo.getType() == UserVo.USER_CUSTOMER) {
			if (machineInfo.getUnit() == 0) {
//				PayRecordBean createPayRecord = blankOrderCreator.createPayRecord(userVo, vmCode, machineInfo.getCurrWay());
//				blankOrderCreator.createPayRecordItem(machineInfo.getItemChangeList(), createPayRecord.getId());
				log.info("水量没变化，不生成订单。。。。。。。。。。。");
				result.setCode(-99);
				result.setMsg("水量没变化，不生成订单");
				
				redisService.del(NormalRedisKey.openflag,machineInfo.getFactoryNumber());
				redisService.set(NormalRedisKey.closeflag,machineInfo.getFactoryNumber(),"1");
				
				return result;
			}
			if(baseInfo.getFreeMachine()==1){
				log.info("免费送商品"+machineInfo.getVmCode()+userVo.getId());
				payRecordForFree(machineInfo, userVo, operateHistoryId, items,footmark,"正常订单");
			}else{
				payRecord(machineInfo, userVo, operateHistoryId, items,footmark,"正常订单");
			}
			//设置购买测试
			setRedisPurchaseTest(vmCode, currWay);
		} else {
			// 调整货道商品数量 记录日志
			if (machineInfo.getItemChangeList().size() > 0) {
				orderDao.updateVmwayItemAndLog(machineInfo.getItemChangeList(), userVo,footmark!=null?footmark.getFootmarkId():null,0);
			}
		}
		return result;
	}

	
	/**
	 * 更新用户积分
	 * @author why
	 * @date 2018年10月11日 上午9:11:11 
	 * @param orderId
	 * @param custoerId
	 * @param num
	 */
	public void  updateUserIntegral(Long custoerId,List<ItemChangeDto> list) {
		log.info("<OrderServiceImpl>----<updateUserIntegral>-----start"+custoerId);
		for(ItemChangeDto prib:list) {
			if(prib.getChangeNum()<0) {
				ItemBasicBean itemBasicBean=itemBasicService.getItemBasic(prib.getBasicItemId());
				if(!("克").equals(itemBasicBean.getUnitName())) {
					//查询是否为未过期的会员
					TblCustomerBean findBen = tblCustomerDao.findBen(custoerId);
					if (findBen != null) {// 会员积分双倍
						log.info("=====是会员=====");
						couponDaoImpl.updateIntegral(custoerId, Math.abs(prib.getChangeNum())  * 2l);
					}else {
						log.info("=====非会员=====");
					couponDaoImpl.updateIntegral(custoerId, Math.abs(prib.getChangeNum())* 1l);
					}
				}	
			}
		}
		log.info("<OrderServiceImpl>----<updateUserIntegral>-----end");
	}
	
	public void sendInviteCoupon(CustomerBean customer) {
		if(customer.getInviterId()>0 && tblCustomerDao.isFirstBuy(customer.getId()).equals(1) && tblCustomerDao.isStoreFirstBuy(customer.getId()).equals(0)){
			//成功邀请获得10积分
			couponDaoImpl.updateIntegral(customer.getInviterId(), 11l);			
			CouponForm couponForm = new CouponForm();
			couponForm.setWay(CouponEnum.INVITE_COUPON.getState());
			couponForm.setLimitRange(false);
			List<CouponBean> presentCoupon = couponDaoImpl.getPresentCoupon(couponForm);
			if(presentCoupon != null && presentCoupon.size()>0) {
				for(CouponBean coupon:presentCoupon) {
					CouponCustomerBean couCusBean = new CouponCustomerBean();
					couCusBean.setQuantity(coupon.getSendMax());
					couCusBean.setCouponId(coupon.getId());
					couCusBean.setCustomerId(customer.getInviterId());
					couCusBean.setStartTime(coupon.getLogicStartTime());
					couCusBean.setEndTime(coupon.getLogicEndTime());
					couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());//状态：0：新建，1：领取，2：已使用
					couponDaoImpl.insertCouponCustomer(couCusBean);
				
				}
			}
		}
	}
	
	public boolean  checkItTenNum(List<ItemChangeDto> list) {
		boolean flag=true;
		for(ItemChangeDto prib:list) {
			if(Math.abs(prib.getChangeNum())>10) {
				ItemBasicBean itemBasicBean=itemBasicService.getItemBasic(prib.getBasicItemId());
				if(!("克").equals(itemBasicBean.getUnitName())) {
					flag=false;
				}	
			}
		}
		return flag;
	}

	/**
	 * 根据product查shoppingGoogsName
	 */
	@Override
	public String findShoppingGoogsName(String product) {
		String sql = " select name from shopping_goods where id in(" + product + ") ";
		log.info("查询商城商品名称的SQL 语句："+sql);
		String name = orderDao.queryShoppingGoodsName(sql);
		return name;
	}


	@Override
	public Map<String, Object> getBaseInfoByVmCode(String vmCode) {
		return orderDao.getBaseInfoByVmCode(vmCode);
	}
	
	@Override
	public void paySpellgroupStroeOrder(String outTradeNo,Integer type) {
		log.info("<OrderServiceImpl>---<paySpellgroupStroeOrder>----start");
		if(type==0) { //已完成状态
			String sql = "update store_order set spellgroupState=4  where id='" + outTradeNo + "'" ;
			log.info(sql);
			orderDao.upate(sql);
		}else {
			String sql = " update store_order set spellgroupState="+type+"  where payCode='" + outTradeNo + "'" ;
			log.info(sql);
			orderDao.upate(sql);
		}
		
		
	}

	@Override
	public Integer getDistributionModelByPayCode(String payCode) {
		log.info("<OrderServiceImpl>----<getDistributionModelByPayCode>----start>");
		Integer companyId = orderDao.getDistributionModelByPayCode(payCode);
		log.info("<OrderServiceImpl>----<getDistributionModelByPayCode>----end>");
		return companyId;
	}

	@Override
	public boolean delivering(Long orderId) {
		log.info("<OrderServiceImpl>-------<delivering>-------start");
		boolean bean = orderDao.editDelivery(orderId);
		log.info("<OrderServiceImpl>-------<delivering>-------end");
		return bean;
	}

	@Override
	public void updatePayState(String ptCode, String payCode, Integer state, Integer type) {
		if (type == 0) {
			String sql = "update store_order set ptCode='" + ptCode
					+ "'  ,payTime=current_timestamp() ,payType=1 ,state=" + state
					+ "  where payCode='" + payCode + "'";
			log.info(sql);
			orderDao.upate(sql);
		} else {
			String sql = "update store_order set ptCode='" + ptCode
					+ "'  ,payTime=current_timestamp() ,payType=1 ,state=" + state
					+ "  ,spellgroupState=" + type + "  where payCode='" + payCode + "'";
			log.info(sql);
			orderDao.upate(sql);
		}
	}

	public void setRedisPurchaseTest(String vmCode,Integer wayNumber){
		if(redisService.exists("startTest"+vmCode)){
			String key = "purchaseTest"+vmCode;
			String testValue = redisService.getString(key);
			if(StringUtils.isNotBlank(testValue)){
				String value = testValue+","+wayNumber;
				redisService.set(key, value, 3600);
			}else{
				redisService.set(key, wayNumber.toString(), 3600);
			}
		}
		
	}
	public static Integer checkCodeSum(String command){
		if(StringUtils.isNotBlank(command)){
			int sum = 0;
			char[] ss = command.toCharArray();
			for (char c : ss) {
				if(Character.isDigit(c)){
					Integer unitChar = Integer.valueOf(String.valueOf(c));
					sum+=unitChar;
				}
			}
			return sum;
		}
		return 0;
	}
	
	
	public void completeOrderV3(String outTradeNo) {
		//https://api.mch.weixin.qq.com/v3/payscore/smartretail-orders/{out_order_no}/complete


	}
}
