package com.server.module.trade.web.order.preferential;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayUtil;
import com.server.module.commonBean.ItemBasicBean;
import com.server.module.coupon.CouponService;
import com.server.module.coupon.MachinesLAC;
import com.server.module.itemBasic.ItemBasicDao;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.config.pay.MyWXPayConfig;
import com.server.module.trade.config.pay.WxPayConfigFactory;
import com.server.module.trade.customer.TblCustomerBean;
import com.server.module.trade.customer.TblCustomerDao;
import com.server.module.trade.order.bean.PayRecordBean;
import com.server.module.trade.order.dao.OrderDao;
import com.server.module.trade.order.domain.MachineInfo;
import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;
import com.server.module.trade.web.service.SmsService;
import com.server.module.zfb_trade.module.customer.AliCustomerDao;
import com.server.module.zfb_trade.module.customer.CustomerBean;
import com.server.module.zfb_trade.service.AlipayService;
import com.server.redis.RedisService;
import com.server.util.StringUtil;
@Component
public class PayHandler extends PreferentialHandler{
	
	private final static Logger log = LogManager.getLogger(PayHandler.class);
	
	@Autowired
	private AlipayService alipayService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private WxPayConfigFactory wxPayConfigFactory;
	@Autowired
	private AliCustomerDao aliCustomerDao;
	@Autowired
	private CouponService couponService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private TblCustomerDao tblCustomerDao;
	@Autowired
	private ItemBasicDao itemBasicDao;
	@Value("${wechat.pay.notifyUrl}") // 跟支付的回调接口一样
	private String depositNotifyUrl;

	public PayHandler(){
		setType(HandlerTypeEnum.PAY);
	}

	public PayHandler(Handler carryHandler){
		setHandler(carryHandler);
		setType(HandlerTypeEnum.PAY);
	}
	
	@Override
	public void handler(MachineInfo machineInfo, UserVo userVo, PayRecordBean payRecord, MachinesLAC mlac,String items,List<HandlerTypeEnum> typeList) {
		if(!hasSimilar(typeList)){
			typeList.add(getType());
			CustomerBean customer = aliCustomerDao.queryById(userVo.getId());
			if (payRecord.getPrice().doubleValue() <=0) {
				couponService.sendInviteCoupon(customer);//被邀请人首次购买送邀请人券 
			}
			else if (payRecord.getPrice().doubleValue() > 0) {
				if (userVo.getPayType() == UserVo.Pay_Type_ZhiFuBao) {// 支付宝 调用扣款程序
					String agreementNo = alipayService.querySign(customer.getAlipayUserId(), machineInfo.getVmCode());
					String ptCode = alipayService.cutPayment(payRecord.getPrice(), payRecord.getPayCode(),
							payRecord.getItemName(), agreementNo);

					if(StringUtil.isNotBlank(ptCode)) {
						//用户积分更新
						updateUserIntegral(userVo.getId(),machineInfo.getItemChangeList());
						couponService.sendInviteCoupon(customer);//被邀请人首次购买送邀请人券 
						redisService.set("orderUserId" + userVo.getId(), "SUCCESS", 300);
					} else {
						payFailSendMsg(customer.getPhone(),userVo.getId());
					}
				} else if (userVo.getPayType() == UserVo.Pay_Type_WeiXin) {
					MyWXPayConfig wxPayConfig = (MyWXPayConfig) wxPayConfigFactory.getWXPayConfig(mlac.getCompanyId());
					Integer usingCompanyConfig = wxPayConfig.getUsingCompanyConfig();
					String contractId = orderDao.getContractId(usingCompanyConfig, userVo.getOpenId());
					this.deposit(payRecord.getPayCode(), payRecord.getPrice().multiply(new BigDecimal(100)), contractId, payRecord.getItemName(),
							mlac.getCompanyId(), userVo);
				}

			}
		}
		if(getHandler() != null){
			getHandler().handler(machineInfo, userVo, payRecord,mlac,items, typeList);
		}
	}
	
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
	
	public void payFailSendMsg(Long customerId){
		try{
			redisService.set("orderUserId" + customerId, "FAIL", 300);
			String phone = tblCustomerDao.getPhoneByCustomerId(customerId);
			if (StringUtil.isNotBlank(phone)) {
				smsService.sendMsg(phone, "订单支付失败，请扫码在我的订单中进行支付!");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void payFailSendMsg(String phone,Long customerId){
		try{
			redisService.set("orderUserId" + customerId, "FAIL", 300);
			if (StringUtil.isNotBlank(phone)) {
				smsService.sendMsg(phone, "订单支付失败，请扫码在我的订单中进行支付!");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
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
		log.info("<PayHandler>----<updateUserIntegral>-----start"+custoerId);
		for(ItemChangeDto prib:list) {
			if(prib.getChangeNum()<0) {
				ItemBasicBean itemBasicBean=itemBasicDao.getItemBasic(prib.getBasicItemId());
				if(!("克").equals(itemBasicBean.getUnitName())) {
					//查询是否为未过期的会员
					TblCustomerBean findBen = tblCustomerDao.findBen(custoerId);
					if (findBen != null) {// 会员积分双倍
						couponService.updateIntegral(custoerId, Math.abs(prib.getChangeNum())  * 2l);
					}else {
						couponService.updateIntegral(custoerId, Math.abs(prib.getChangeNum())* 1l);
					}
				}	
			}
		}
		log.info("<PayHandler>----<updateUserIntegral>-----end");
	}
	

}
