package com.server.module.trade.config.gzh;

public class WXAddressConstant {
	
	public static final String OAUTH2_CODE="https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect";

	public static final String OAUTH2_ACCESS_TOKEN="https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
	
	public static final String OAUTH2_REFRESH_TOKEN="https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";
	
	public static final String OAUTH2_USER_INFO="https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
}
