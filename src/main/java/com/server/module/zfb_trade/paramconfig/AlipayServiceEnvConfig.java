package com.server.module.zfb_trade.paramconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@ConfigurationProperties(prefix="alipayServiceEnv")
@Configuration
public class AlipayServiceEnvConfig implements Cloneable{
	/** 签名编码-视支付宝服务窗要求 */
	public static final String SIGN_CHARSET = "GBK";

	/** 字符编码-传递给支付宝的数据编码 */
	public static final String CHARSET = "GBK";

	/** 签名类型-视支付宝服务窗要求 */
	public static final String SIGN_TYPE = "RSA2";

	/** JSON 格式 */
	public static final String FORMAT_JSON = "json";

	/** UTF-8字符集 **/
	public static final String CHARSET_UTF8 = "UTF-8";

	public String alipay_public_key;
	public String partner;
	public String app_id;
	public String app_private_key;
	public String app_public_key;
	public String alipay_gateway;
	public String grant_type;
	public String app_grant_type;
	public String auth_callback_address;
	public String ali_unsign_notify_address;
	public String goto_url;
	public String getAlipay_public_key() {
		return alipay_public_key;
	}
	public void setAlipay_public_key(String alipay_public_key) {
		this.alipay_public_key = alipay_public_key;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getApp_private_key() {
		return app_private_key;
	}
	public void setApp_private_key(String app_private_key) {
		this.app_private_key = app_private_key;
	}
	public String getApp_public_key() {
		return app_public_key;
	}
	public void setApp_public_key(String app_public_key) {
		this.app_public_key = app_public_key;
	}
	public String getAlipay_gateway() {
		return alipay_gateway;
	}
	public void setAlipay_gateway(String alipay_gateway) {
		this.alipay_gateway = alipay_gateway;
	}
	public String getGrant_type() {
		return grant_type;
	}
	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}
	public String getApp_grant_type() {
		return app_grant_type;
	}
	public void setApp_grant_type(String app_grant_type) {
		this.app_grant_type = app_grant_type;
	}
	public String getAuth_callback_address() {
		return auth_callback_address;
	}
	public void setAuth_callback_address(String auth_callback_address) {
		this.auth_callback_address = auth_callback_address;
	}
	public String getAli_unsign_notify_address() {
		return ali_unsign_notify_address;
	}
	public void setAli_unsign_notify_address(String ali_unsign_notify_address) {
		this.ali_unsign_notify_address = ali_unsign_notify_address;
	}
	public String getGoto_url() {
		return goto_url;
	}
	public void setGoto_url(String goto_url) {
		this.goto_url = goto_url;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
