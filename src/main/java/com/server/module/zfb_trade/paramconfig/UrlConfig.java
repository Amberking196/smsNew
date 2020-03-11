package com.server.module.zfb_trade.paramconfig;

public class UrlConfig {

	//支付宝授权地址 (跳转到接口http://yms.youshuidaojia.com/aliUser/vmCode)
	public static final String OAUTH2URL = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=%s&scope=auth_user&redirect_uri=http://yms.youshuidaojia.com/aliUser/%s";
	//支付宝授权地址 (跳转到接口http://yms.youshuidaojia.com/aliUser/getAliInfo)
	public static final String OAUTH2GETALIINFOURL = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=%s&scope=auth_user&redirect_uri=http://yms.youshuidaojia.com/aliUser/getAliInfo";

	public static final String VISIONOAUTH2URL = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=%s&scope=auth_user&redirect_uri=http://yms.youshuidaojia.com/aliUser/vision/%s";

}
