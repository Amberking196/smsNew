package com.server.configQRcode;

import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenPublicQrcodeCreateRequest;
import com.alipay.api.response.AlipayOpenPublicQrcodeCreateResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * author name: why
 * create time: 2018-04-11 11:02:28
 */
@Service
@Slf4j
public class AlipayQRCode{
	
	/***
	 *  调用支付宝生成二维码
	 * @param vmCode
	 * @return
	 */
	public static String createQRCode(String vmCode) {
		
		log.info("---------------------------");
		log.info(AlipayConstant.ALIPAY_PUBLIC_KEY);
		log.info(AlipayConstant.APP_PRIVATE_KEY);
		log.info(AlipayConstant.APP_ID);
		log.info(AlipayConstant.GOTO_URL);
		log.info("---------------------------");
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
				AlipayConstant.APP_ID, AlipayConstant.APP_PRIVATE_KEY, "json", "GBK", AlipayConstant.ALIPAY_PUBLIC_KEY,
				"RSA2");
		AlipayOpenPublicQrcodeCreateRequest request = new AlipayOpenPublicQrcodeCreateRequest();
		request.setBizContent("{" + "\"code_info\":{" + "\"scene\":{" + "\"scene_id\":\"lianye\"" + " },"
				+ "\"goto_url\":\"" + AlipayConstant.GOTO_URL.concat(vmCode) + "\"}," + "\"code_type\":\"PERM\","
				+ "\"expire_second\": \"\" ," + "\"show_logo\":\"Y\"" + " }");
		try {
			AlipayOpenPublicQrcodeCreateResponse response = alipayClient.execute(request);
			if (response.isSuccess()) {
				log.info("【调用支付宝生成二维码成功】vmCode:{}", vmCode);
				return response.getCodeImg();
			}
		} catch (AlipayApiException e) {
			log.error("【错误信息】error:[{}]", e.getMessage(), e);
		}
		log.info("【调用支付宝生成二维码失败】vmCode:{}", vmCode);
		return "";
	}

}