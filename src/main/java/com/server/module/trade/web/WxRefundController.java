package com.server.module.trade.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.wxpay.sdk.WXPay;
import com.server.jwt.TokenAuthenticationService;
import com.server.module.carryWaterVouchersManage.carryWaterVouchersCustomer.CarryWaterVouchersCustomerService;
import com.server.module.member.MemberService;
import com.server.module.payRecord.AliPayRecordService;
import com.server.module.refund.RefundDto;
import com.server.module.refund.RefundOrderInfo;
import com.server.module.refund.RefundRecordBean;
import com.server.module.refund.RefundService;
import com.server.module.trade.config.pay.WxPayConfigFactory;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.module.zfb_trade.util.ResultUtil;
import com.server.module.zfb_trade.util.enumparam.ResultEnum;
import com.server.redis.RedisService;
import com.server.redis.SmsKey;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.UUIDUtil;
import com.server.util.stateEnum.PayTypeEnum;
import com.server.util.stateEnum.RefundEnum;
import com.server.util.stateEnum.RefundTypeEnum;

@RestController
@RequestMapping("/wxRefund")
public class WxRefundController {
	private final static Logger log = LogManager.getLogger(WxRefundController.class);
	@Autowired
	private WxPayConfigFactory wxPayConfigFactory;
	@Autowired
	private RedisService redisService;
	@Autowired
	private RefundService refundService;
	@Autowired
	private TokenAuthenticationService tokenService;
	@Autowired
	@Qualifier("aliPayRecordService")
	private AliPayRecordService payRecordService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private CarryWaterVouchersCustomerService carryWaterVouchersCustomerServiceImpl;

	@PostMapping("/groupBuy")
	public ReturnDataUtil wxGroupBuyRefund(Integer groupId) {
		List<RefundDto> groupList = refundService.findGroupBuyOrder(groupId);
		if (groupList == null || groupList.size() == 0) {
			return ResultUtil.error(ResultEnum.REFUND_ORDER_NOT_EXISTS, null);
		}
		List<String> refundSuccessList = new ArrayList<String>();
		boolean priceSuccess = true;
		for (RefundDto refundDto : groupList) {
			refundDto.setRefundGenre(0);
			RefundRecordBean refundBean = wxrefund(refundDto, null);
			refundService.insertRefundRecord(refundBean);
			if (refundBean.getState() == 2) {
				priceSuccess = false;
			} else if (refundBean.getState() == 1) {
				refundSuccessList.add(refundDto.getPayCode());
			}
		}
		refundService.updateOrderRefundState(refundSuccessList, 3);
		if (priceSuccess) {
			refundService.updateGroupBuyState(groupId, RefundEnum.GROUP_BUY_FAIL.getState());
			return ResultUtil.success(ResultEnum.REFUND_SUCCESS, null);
		}
		return ResultUtil.error(ResultEnum.REFUND_ERROR, null);
	}

	@RequestMapping("/do")
	public ReturnDataUtil wechatRefund(@RequestBody RefundDto refundDto) {
		boolean checkToken = tokenService.checkToken(refundDto.getAdminToken());
		if (!checkToken) {
			return ResultUtil.error(ResultEnum.NOT_LOGIN, null);
		}
		RefundOrderInfo orderInfo = refundService.findOrder(refundDto.getPayCode(), refundDto.getOrderType());
		if (orderInfo == null) {
			refundDto.setOrderType(6);
			orderInfo = refundService.findOrder(refundDto.getPayCode(), refundDto.getOrderType());
		}
		// MsgDto orderInfo = payRecordService.findMsgDto(refundDto.getPayCode());
		if (orderInfo == null) {
			return ResultUtil.error(ResultEnum.REFUND_ORDER_NOT_EXISTS, null);
		}
		if (orderInfo.getPrice().compareTo(refundDto.getRefundPrice()) < 0 || orderInfo.getCompanyId() == null
				|| StringUtil.isBlank(orderInfo.getPtCode())) {
			return ResultUtil.error(ResultEnum.REFUND_ORDER_ERROR, null);
		}
		if (refundDto.getCompanyId() == null) {
			refundDto.setCompanyId(orderInfo.getCompanyId());
		}
		refundDto.setItemName(orderInfo.getItemName());
		refundDto.setPrice(orderInfo.getPrice());

		String param = tokenService.getIdByToken(refundDto.getAdminToken());
		String[] params = param.split(",");
		Long userId = Long.valueOf(params[0]);
		String phone = refundService.getPrincipalInfoById(userId);
		if (StringUtil.isBlank(phone)) {
			return ResultUtil.error(ResultEnum.REFUND_NOT_AUTHORIZED, null);
		}
		refundDto.setPhone(phone);
		String phoneCode = redisService.get(SmsKey.getCodeByMobile, refundDto.getPhone());
		log.info("phoneCode==="+phone);
		if (StringUtil.isNotBlank(phoneCode) && phoneCode.equals(refundDto.getPhoneCode())) {
			// 退款开始
			RefundRecordBean result = wxrefund(refundDto, userId);
			result.setPayTime(orderInfo.getPayTime());
			refundService.insertRefundRecord(result);
			if (result.getState() == 1) {
				List<String> refundSuccessList = new ArrayList<String>();
				refundSuccessList.add(refundDto.getPayCode());
				if(refundDto.getOrderType() == 1 && orderInfo.getPrice().compareTo(refundDto.getRefundPrice()) == 0) {
					refundDto.setOrderType(5);//完全退款
				}
				if(refundDto.getOrderType() == 6 && orderInfo.getPrice().compareTo(refundDto.getRefundPrice()) == 0) {
					refundDto.setOrderType(7);//视觉完全退款
				}
				refundService.updateOrderRefundState(refundSuccessList, refundDto.getOrderType());
				if (refundDto.getOrderType() == 4) {// 修改用户余额
					updateCustomerMoney(orderInfo, refundDto.getRefundPrice());
				}
				if (refundDto.getOrderType() == 2 || refundDto.getOrderType() == 3) {// 退款后 删除用户提水券
					carryWaterVouchersCustomerServiceImpl.delete(orderInfo.getOrderId());
				}
				return ResultUtil.success(ResultEnum.REFUND_SUCCESS, null);
			} else {
				return ResultUtil.error(ResultEnum.REFUND_ERROR, null);
			}
		}
		return ResultUtil.error(ResultEnum.REFUND_NOT_AUTHORIZED, null);
	}

	private RefundRecordBean wxrefund(RefundDto refundDto, Long userId) {
		WXPay wxPay = wxPayConfigFactory.getWXPay(refundDto.getCompanyId());
		Map<String, String> reqData = new HashMap<String, String>();
		if (RefundTypeEnum.REFUND_WX_SUCCESS_PAY_MULTIPLE.getState().equals(refundDto.getRefundType())) {
			reqData.put("transaction_id", refundDto.getPtCode());
		} else if (RefundTypeEnum.REFUND_WX_SUCCESS_PAY_FAIL.getState().equals(refundDto.getRefundType())) {
			reqData.put("out_trade_no", refundDto.getPayCode());
		} else {
			reqData.put("transaction_id", refundDto.getPtCode());
		}
		// 退款id
		String uuid = UUIDUtil.getUUID();
		reqData.put("out_refund_no", uuid);
		BigDecimal price = refundDto.getPrice().multiply(new BigDecimal("100"));
		BigDecimal refundPrice = refundDto.getRefundPrice().multiply(new BigDecimal("100"));
		reqData.put("total_fee", price.intValue() + "");
		reqData.put("refund_fee", refundPrice.intValue() + "");
		reqData.put("refund_desc", refundDto.getReason());
		Map<String, String> refund = null;
		try {
			refund = wxPay.refund(reqData);
			log.debug("refund=" + JsonUtil.toJson(refund));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (refund != null && ("SUCCESS").equals(refund.get("return_code"))
				&& ("SUCCESS").equals(refund.get("result_code"))) {
			return RefundRecordBean.createRefundRecordBean(refundDto, userId, uuid, refund.get("refund_id"),
					RefundRecordBean.SUCCESS, PayTypeEnum.WEIXIN.getIndex());
		} else {
			return RefundRecordBean.createRefundRecordBean(refundDto, userId, uuid, null, RefundRecordBean.FAIL,
					PayTypeEnum.WEIXIN.getIndex());
		}
	}

	@RequestMapping("/customerRefund")
	public ReturnDataUtil customerRefund(@RequestBody RefundDto refundDto) {
		log.info("<WxRefundController>----<customerRefund>----start");
		boolean checkToken = tokenService.checkToken(refundDto.getAdminToken());
		if (!checkToken) {
			log.info("<WxRefundController>----<customerRefund>----end");
			return ResultUtil.error(ResultEnum.NOT_LOGIN, null);
		}
		RefundOrderInfo orderInfo = refundService.findOrder(refundDto.getPayCode(), refundDto.getOrderType());
		if (orderInfo == null) {
			log.info("<WxRefundController>----<customerRefund>----end");
			return ResultUtil.error(ResultEnum.REFUND_ORDER_NOT_EXISTS, null);
		}
		if (orderInfo.getPrice().compareTo(refundDto.getRefundPrice()) < 0 || orderInfo.getCompanyId() == null
				|| StringUtil.isBlank(orderInfo.getPtCode())) {
			log.info("<WxRefundController>----<customerRefund>----end");
			return ResultUtil.error(ResultEnum.REFUND_ORDER_ERROR, null);
		}
		if (refundDto.getCompanyId() == null) {
			refundDto.setCompanyId(orderInfo.getCompanyId());
		}
		refundDto.setItemName(orderInfo.getItemName());
		refundDto.setPrice(orderInfo.getPrice());
		String param = tokenService.getIdByToken(refundDto.getAdminToken());
		String[] params = param.split(",");
		Long userId = Long.valueOf(params[0]);
		// 退款开始
		RefundRecordBean result = wxrefund(refundDto, userId);
		refundService.insertRefundRecord(result);
		if (result.getState() == 1) {
			List<String> refundSuccessList = new ArrayList<String>();
			refundSuccessList.add(refundDto.getPayCode());
			// 退款成功修改 订单状态
			refundService.updateOrderRefundState(refundSuccessList, refundDto.getOrderType());
			// 删除用户提水券
			refundService.deleteCarry(orderInfo.getOrderId());
			log.info("<WxRefundController>----<customerRefund>----end");
			return ResultUtil.success(ResultEnum.REFUND_SUCCESS, null);
		} else {
			log.info("<WxRefundController>----<customerRefund>----end");
			return ResultUtil.error(ResultEnum.REFUND_ERROR, null);
		}
	}

	public void updateCustomerMoney(RefundOrderInfo orderInfo, BigDecimal refundPrice) {
		// 得到本次充值金额 和退款金额 计算最终余额--退款金额
		BigDecimal price = orderInfo.getPrice().subtract(refundPrice);
		BigDecimal calculate = calculate(orderInfo.getPrice()).subtract(calculate(price));
		BigDecimal sumPrice = refundPrice.add(calculate);
		if (orderInfo.getFriendCustomerId() != null && orderInfo.getFriendCustomerId() > 0) {
			memberService.updateMemberMoney(orderInfo.getFriendCustomerId(), sumPrice);
		} else {
			memberService.updateMemberMoney(orderInfo.getCustomerId(), sumPrice);
		}
	}

	/**
	 * 计算赠送金额
	 * 
	 * @author why
	 * @date 2019年3月18日 下午5:45:45
	 * @param price
	 * @return
	 */
	public BigDecimal calculate(BigDecimal price) {
		BigDecimal multiply = new BigDecimal(0);
		if (price.compareTo(BigDecimal.valueOf(50)) >= 0 && price.compareTo(BigDecimal.valueOf(100)) < 0) {
			multiply = price.multiply(BigDecimal.valueOf(0.04));
			// 金额单次 100<=price<200
		} else if (price.compareTo(BigDecimal.valueOf(100)) >= 0 && price.compareTo(BigDecimal.valueOf(200)) < 0) {
			multiply = price.multiply(BigDecimal.valueOf(0.06));
			// 金额单次 200<=price<500
		} else if (price.compareTo(BigDecimal.valueOf(200)) >= 0 && price.compareTo(BigDecimal.valueOf(500)) < 0) {
			multiply = price.multiply(BigDecimal.valueOf(0.08));
			// 金额单次 price>=500
		} else if (price.compareTo(BigDecimal.valueOf(500)) >= 0) {
			multiply = price.multiply(BigDecimal.valueOf(0.1));
		}
		return multiply;
	}
}
