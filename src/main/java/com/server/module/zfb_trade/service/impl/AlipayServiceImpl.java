package com.server.module.zfb_trade.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.server.company.MachinesDao;
import com.server.module.payRecord.AliPayRecordService;
import com.server.module.zfb_trade.bean.MsgDto;
import com.server.module.zfb_trade.factory.AlipayAPIClientFactory;
import com.server.module.zfb_trade.factory.AlipayConfigFactory;
import com.server.module.zfb_trade.paramconfig.AlipayConfig;
import com.server.module.zfb_trade.paramconfig.H5URLConfig;
import com.server.module.zfb_trade.service.AlipayService;
import com.server.module.zfb_trade.util.AlipaySubmit;
import com.server.module.zfb_trade.util.enumparam.AlipayEnum;
import com.server.util.StringUtil;
import com.server.util.stateEnum.PayStateEnum;
@Service
public class AlipayServiceImpl implements AlipayService{

	public static Logger log = LogManager.getLogger(AlipayServiceImpl.class); 
	@Autowired
	private AlipayConfigFactory alipayConfigFactory;
	@Autowired
	private AlipaySubmit alipaySubmit;
	@Autowired
	private AliPayRecordService payRecordService;
	@Autowired
	private AlipayAPIClientFactory alipayAPIClientFactory;
	@Autowired
	private H5URLConfig h5URLConfig;
	@Autowired
	private MachinesDao machinesDao;
	
	@Override
	public String querySign(String alipayUserId,String vmCode) {
		log.info("<AlipayServiceImpl--querySign--start>");
		String agreementNo = null;
		Integer companyId = machinesDao.getCompanyIdByVmCode(vmCode);
		AlipayConfig alipayConfig = alipayConfigFactory.getAlipayConfig(companyId);
		Map<String,String> param = new HashMap<String,String>();
		param.put("service", "alipay.dut.customer.agreement.query");
		param.put("partner", alipayConfig.partner);
		param.put("_input_charset", AlipayConfig.INPUT_CHARSET);
		param.put("product_code", "GENERAL_WITHHOLDING_P");
		param.put("alipay_user_id", alipayUserId);
		try {
			String result = alipaySubmit.buildRequest("", "", param,alipayConfig);
			log.info("alipaySubmit.buildRequest结果："+result);
			Document doc = DocumentHelper.parseText(result);
			if(AlipayEnum.SUCCESS.getMessage().equals(alipaySubmit.selectNodeText("//alipay/is_success",doc))){
				log.info("支付宝接受请求成功");
				agreementNo = alipaySubmit.selectNodeText("//alipay/response/userAgreementInfo/agreement_no", doc);
				if(StringUtil.isNotBlank(agreementNo)){
					log.info("签约查询成功");
					log.info("支付宝签约号 agreement_no：" + agreementNo);
					log.info("商户外部签约号 external_sign_no："
							+ alipaySubmit.selectNodeText("//alipay/response/userAgreementInfo/external_sign_no", doc));
					log.info("场景码 scene："
							+ alipaySubmit.selectNodeText("//alipay/response/userAgreementInfo/scene", doc));
					log.info("协议状态 status："
							+ alipaySubmit.selectNodeText("//alipay/response/userAgreementInfo/status", doc));
					log.info("签约时间 sign_time："
							+ alipaySubmit.selectNodeText("//alipay/response/userAgreementInfo/sign_time", doc));
					log.info("生效时间 valid_time："
							+ alipaySubmit.selectNodeText("//alipay/response/userAgreementInfo/valid_time", doc));
					log.info("失效时间 invalid_time："
							+ alipaySubmit.selectNodeText("//alipay/response/userAgreementInfo/invalid_time", doc));
				}else{
					log.info("签约查询失败，未查询到签约信息。可能原因为：");
					log.info("1、外部签约号错误。");
					log.info("2、用户在商家系统或者在支付宝端已解约。");
					log.info("3、协议已过期。");
					log.info("4、签约时传的scene、external_sign_no等参数跟查询时不一致。");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("<AlipayServiceImpl--querySign--end>");
		return agreementNo;
	}

	@Override
	public String cutPayment(BigDecimal price,String payCode,String itemName,String agreementNo) {
		log.info("<AlipayServiceImpl--cutPayment--start>");
		String ptCode = "";
		MsgDto findMsgDto = payRecordService.findMsgDto(payCode);
		Integer companyId = machinesDao.getCompanyIdByPayCode(payCode);
		AlipayConfig alipayConfig = alipayConfigFactory.getAlipayConfig(companyId);
		Map<String, String> param = new HashMap<String, String>();
		param.put("service", "alipay.acquire.createandpay");
		param.put("partner", alipayConfig.partner);
		param.put("_input_charset", AlipayConfig.INPUT_CHARSET);
		param.put("out_trade_no", payCode);
		param.put("subject", itemName);
		param.put("product_code", "GENERAL_WITHHOLDING");
		param.put("total_fee",price.setScale(2, BigDecimal.ROUND_DOWN).toString());
		param.put("extend_params","{\"TERMINAL_ID\":\"" + findMsgDto.getVmCode() + "\"}");
		// 支付宝签约号:信用代扣签约(协议号:支付宝系统中用以唯一标识用户签约记录的编号。)
		// 传入支付宝签约号
		param.put("agreement_info", "{\"agreement_no\":\"" + agreementNo + "\"}");
		param.put("notify_url", alipayConfig.alipay_notify_url);
		try {
			String result = alipaySubmit.buildRequest("", "", param,alipayConfig);
			log.info("result:" + result);
			Document doc = DocumentHelper.parseText(result);
			log.info("document:" + doc.toString());
			log.info("结果：" + alipaySubmit.selectNodeText("//alipay/is_success", doc));
			if (AlipayEnum.SUCCESS.getMessage().equals(alipaySubmit.selectNodeText("//alipay/is_success", doc))) {
				if ("ORDER_SUCCESS_PAY_SUCCESS"
						.equals(alipaySubmit.selectNodeText("//alipay/response/alipay/result_code", doc))) {
					log.info("【扣款成功】");
					ptCode = alipaySubmit.selectNodeText("//alipay/response/alipay/trade_no", doc); // 支付宝订单号
					payRecordService.updatePayState(ptCode, payCode, PayStateEnum.PAY_SUCCESS.getState());
					log.info("商户订单号 out_trade_no:"
							+ alipaySubmit.selectNodeText("//alipay/response/alipay/out_trade_no", doc));
					log.info("扣款金额:WIDtotal_fee:"
							+ alipaySubmit.selectNodeText("//alipay/response/alipay/total_fee", doc));
				} else if ("UNKNOWN".equals(alipaySubmit.selectNodeText("//alipay/response/alipay/result_code", doc))) {
					log.info("【扣款结果未知，请通过订单查询接口查询扣款结果】");
				} else {
					payRecordService.updatePayState(ptCode, payCode, PayStateEnum.NOT_PAY.getState());
					log.info("【扣款失败】" + alipaySubmit.selectNodeText("//alipay/response/alipay/result_code", doc));
					log.info("商户订单号 out_trade_no:"
							+ alipaySubmit.selectNodeText("//alipay/response/alipay/out_trade_no", doc));
					log.info("详细错误码 detail_error_code:"
							+ alipaySubmit.selectNodeText("//alipay/response/alipay/detail_error_code", doc));
					log.info("详细错误描述 detail_error_des:"
							+ alipaySubmit.selectNodeText("//alipay/response/alipay/detail_error_des", doc));
				}
			} else {
				log.info("支付宝接收请求失败");
				log.info("错误信息:" + alipaySubmit.selectNodeText("//alipay/error", doc) + "<br/>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("<AlipayServiceImpl--cutPayment--end>");
		return ptCode;
	}



	@Override
	public void wapPayment(BigDecimal price, String payCode, String itemName,
			String vmCode,HttpServletResponse response) throws IOException {
		log.info("<AlipayServiceImpl--wapPayment--start>");
		Integer companyId = machinesDao.getCompanyIdByVmCode(vmCode);
		AlipayClient alipayClient = alipayAPIClientFactory.getAlipayClient(companyId);
		AlipayConfig alipayConfig = alipayConfigFactory.getAlipayConfig(companyId);
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request

		alipayRequest.setReturnUrl(h5URLConfig.weightH5Page.concat("?vmCode=").concat(vmCode));
		alipayRequest.setNotifyUrl(alipayConfig.alipay_notify_url);// 在公共参数中设置回跳和通知地址
		alipayRequest.setBizContent("{" 
				+ " \"out_trade_no\":\""+payCode+"\"," 
				+ " \"total_amount\":\""+price+"\","
				+ " \"subject\":\""+itemName+"\"," 
				+ " \"product_code\":\"QUICK_WAP_PAY\"" 
				+ " }");// 填充业务参数
		String form = "";
		try {
			form = alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		log.info("form:"+form);
		log.info("<AlipayServiceImpl--wapPayment--end>");
		response.setContentType("text/html;charset=" + "utf-8");
		response.getWriter().write(form);// 直接将完整的表单html输出到页面
		response.getWriter().flush();
		response.getWriter().close();
	}
}
