package com.server.module.zfb_trade.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.lly835.bestpay.utils.JsonUtil;
import com.server.jwt.TokenAuthenticationService;
import com.server.module.payRecord.AliPayRecordService;
import com.server.module.refund.RefundDto;
import com.server.module.refund.RefundForm;
import com.server.module.refund.RefundOrderInfo;
import com.server.module.refund.RefundRecordBean;
import com.server.module.refund.RefundService;
import com.server.module.zfb_trade.bean.MsgDto;
import com.server.module.zfb_trade.factory.AlipayAPIClientFactory;
import com.server.module.zfb_trade.util.ResultUtil;
import com.server.module.zfb_trade.util.enumparam.ResultEnum;
import com.server.redis.RedisService;
import com.server.redis.SmsKey;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.UUIDUtil;
import com.server.util.stateEnum.PayTypeEnum;

@RestController
@RequestMapping("/aliRefund")
public class AliRefundController {
	
	private final static Logger log = LogManager.getLogger(AliRefundController.class);
	
	@Autowired
	private AlipayAPIClientFactory alipayAPIClientFactory;
	@Autowired
	@Qualifier("aliPayRecordService")
	private AliPayRecordService payRecordService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private RefundService refundService;
	@Autowired
	private TokenAuthenticationService tokenService;
	
	@RequestMapping("/do")
	public ReturnDataUtil refund(@RequestBody RefundDto refundDto){
		boolean checkToken = tokenService.checkToken(refundDto.getAdminToken());
		if(checkToken){
			RefundOrderInfo orderInfo = refundService.findOrder(refundDto.getPayCode(), refundDto.getOrderType());
//			MsgDto orderInfo = payRecordService.findMsgDto(refundDto.getPayCode());
			if (orderInfo == null) {
				refundDto.setOrderType(6);
				orderInfo = refundService.findOrder(refundDto.getPayCode(), refundDto.getOrderType());
			}
			if(orderInfo.getPrice().compareTo(refundDto.getRefundPrice()) < 0
					|| orderInfo.getCompanyId() == null || StringUtil.isBlank(orderInfo.getPtCode())){
				return ResultUtil.error(ResultEnum.REFUND_ORDER_ERROR, null);
			}
			refundDto.setPtCode(orderInfo.getPtCode());
			refundDto.setCompanyId(orderInfo.getCompanyId());
			refundDto.setPrice(orderInfo.getPrice());
			String param = tokenService.getIdByToken(refundDto.getAdminToken());
			String[] params = param.split(",");
			Long userId = Long.valueOf(params[0]);
			String phone = refundService.getPrincipalInfoById(userId);
			if(StringUtil.isBlank(phone)){
				return ResultUtil.error(ResultEnum.REFUND_NOT_AUTHORIZED, null);
			}
			refundDto.setItemName(orderInfo.getItemName());
			refundDto.setPhone(phone);
			String phoneCode = redisService.get(SmsKey.getCodeByMobile, refundDto.getPhone());
			if(StringUtil.isNotBlank(phoneCode) && phoneCode.equals(refundDto.getPhoneCode())){
				AlipayClient alipayClient = alipayAPIClientFactory.getAlipayClient(refundDto.getCompanyId());
				AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
				RefundForm refundForm = new RefundForm();
				refundForm.setTrade_no(refundDto.getPtCode());
				refundForm.setOut_trade_no(refundDto.getPayCode());
				refundForm.setRefund_amount(refundDto.getRefundPrice().toString());
				refundForm.setRefund_reason(refundDto.getReason());
				refundForm.setTerminal_id(refundDto.getVmCode());
				//标识退款id
				String uuid = UUIDUtil.getUUID();
				refundForm.setOut_request_no(uuid);
				//操作人：phone
				refundForm.setOperator_id(refundDto.getPhone());
				if(refundDto.getItemList()!=null && refundDto.getItemList().size()>0){
					refundForm.setGoods_detail(refundDto.getItemList());
				}
				String refundJson = JsonUtil.toJson(refundForm);
				request.setBizContent(refundJson);
				AlipayTradeRefundResponse response = null;
				try {
					response = alipayClient.execute(request);
					log.info("AlipayTradeRefundResponse--"+JsonUtil.toJson(response));
				} catch (AlipayApiException e) {
					e.printStackTrace();
				}
				if (response!=null && response.isSuccess()) {
					RefundRecordBean refundBean = RefundRecordBean.
							createRefundRecordBean(refundDto,userId,uuid,null,RefundRecordBean.SUCCESS,PayTypeEnum.ZHIFUBAO.getIndex());
					refundBean.setPayTime(orderInfo.getPayTime());
					refundService.insertRefundRecord(refundBean);
					List<String> refundSuccessList = new ArrayList<String>();
					refundSuccessList.add(refundDto.getPayCode());
					
					if(refundDto.getOrderType() == 1 && orderInfo.getPrice().compareTo(refundDto.getRefundPrice()) == 0) {
						refundDto.setOrderType(5);//完全退款
					}
					if(refundDto.getOrderType() == 6 && orderInfo.getPrice().compareTo(refundDto.getRefundPrice()) == 0) {
						refundDto.setOrderType(7);//视觉完全退款
					}
					refundService.updateOrderRefundState(refundSuccessList, refundDto.getOrderType());
					return ResultUtil.success(ResultEnum.REFUND_SUCCESS, null);
				} else {
					RefundRecordBean refundBean = RefundRecordBean.
							createRefundRecordBean(refundDto,userId,uuid,null,RefundRecordBean.FAIL,PayTypeEnum.ZHIFUBAO.getIndex());
					refundService.insertRefundRecord(refundBean);
					return ResultUtil.error(ResultEnum.REFUND_ERROR, null);
				}
			}
			return ResultUtil.error(ResultEnum.REFUND_NOT_AUTHORIZED, null);
		}
		return ResultUtil.error(ResultEnum.NOT_LOGIN,null);
	}
	
}
