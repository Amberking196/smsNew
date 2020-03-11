package com.server.module.zfb_trade.paramconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@ConfigurationProperties(prefix="alipay")
@Configuration
public class AlipayConfig implements Cloneable{
	// 字符编码格式 目前支持 gbk 或 utf-8
		public static final String INPUT_CHARSET = "utf-8";
		// 签名方式 不需修改
		public static final String SIGN_TYPE = "MD5";
		// 签约账号id
		public String partner;
		// 商户的私钥
		public String key;
		// 收款账号id，可以跟partner相同
		public String seller_id; 
		// 日志路径
		public String log_path;
		 /**
	     * 支付宝消息验证地址
	     */
	    public String https_verify_url;
	    /**
	     * 支付宝提供给商户的服务接入网关URL(新)
	     */
	    public String alipay_gateway_new;
	    
	    /** 支付宝支付调地址 */
		public String alipay_notify_url;
		/** 手机网站支付异步回调地址 */
		public String wap_notify_url;
		/** 手机网站支付同步回调地址 */
		public String wap_return_url;
		public String balance_wap_return_url;
	    public String balance_wap_notify_url;
		public String template_id;

	public String getBalance_wap_return_url() {
		return balance_wap_return_url;
	}

	public void setBalance_wap_return_url(String balance_wap_return_url) {
		this.balance_wap_return_url = balance_wap_return_url;
	}

	public String getBalance_wap_notify_url() {
		return balance_wap_notify_url;
	}

	public void setBalance_wap_notify_url(String balance_wap_notify_url) {
		this.balance_wap_notify_url = balance_wap_notify_url;
	}

	public String getTemplate_id() {
			return template_id;
		}

		public void setTemplate_id(String template_id) {
			this.template_id = template_id;
		}

		public String getWap_return_url() {
			return wap_return_url;
		}

		public void setWap_return_url(String wap_return_url) {
			this.wap_return_url = wap_return_url;
		}

		public String getWap_notify_url() {
			return wap_notify_url;
		}

		public void setWap_notify_url(String wap_notify_url) {
			this.wap_notify_url = wap_notify_url;
		}

		public String getPartner() {
			return partner;
		}

		public void setPartner(String partner) {
			this.partner = partner;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getSeller_id() {
			return seller_id;
		}

		public void setSeller_id(String seller_id) {
			this.seller_id = seller_id;
		}

		public String getLog_path() {
			return log_path;
		}

		public void setLog_path(String log_path) {
			this.log_path = log_path;
		}

		public String getHttps_verify_url() {
			return https_verify_url;
		}

		public void setHttps_verify_url(String https_verify_url) {
			this.https_verify_url = https_verify_url;
		}

		public String getAlipay_gateway_new() {
			return alipay_gateway_new;
		}

		public void setAlipay_gateway_new(String alipay_gateway_new) {
			this.alipay_gateway_new = alipay_gateway_new;
		}

		public String getAlipay_notify_url() {
			return alipay_notify_url;
		}

		public void setAlipay_notify_url(String alipay_notify_url) {
			this.alipay_notify_url = alipay_notify_url;
		}

		@Override
		public Object clone() throws CloneNotSupportedException {
			return super.clone();
		}
		
}
