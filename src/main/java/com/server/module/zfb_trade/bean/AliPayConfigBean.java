package com.server.module.zfb_trade.bean;

public class AliPayConfigBean {

	//公司id
	private Integer companyId;
	//商户密钥
	private String key;
	//消息模板id
	private String template_id;
	//生活号id
	private String appId;
	//应用私钥
	private String app_private_key;
	//应用公钥
	private String app_public_key;
	//支付宝公钥
	private String alipay_public_key;
	//商户id
	private String partner;
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
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
	
	
}
