package com.server.module.zfb_trade.paramconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties(prefix="alipayServiceName")
public class AlipayServiceNameConfig {

	public String alipay_check_service;
	public String alipay_public_message_notify;
	public String getAlipay_check_service() {
		return alipay_check_service;
	}
	public void setAlipay_check_service(String alipay_check_service) {
		this.alipay_check_service = alipay_check_service;
	}
	public String getAlipay_public_message_notify() {
		return alipay_public_message_notify;
	}
	public void setAlipay_public_message_notify(String alipay_public_message_notify) {
		this.alipay_public_message_notify = alipay_public_message_notify;
	}
	
}
