package com.server.configQRcode;

import java.util.Properties;

import com.server.dbpool.PropManager;

/**
 * 
 * author name: why
 * create time: 2018-04-11 11:01:22
 */
public class AlipayConstant {

	private static Properties config = null;
	static {
		PropManager prop = new PropManager("application-account.properties");
		config = prop.getProp();
	}
	
	/** 合作身份者ID,以2088开头由16位纯数字组成的字符串 */
	public static String PARTNER = config.getProperty("alipay.partner");
	/** 商户的私钥key */
	public static String APP_PRIVATE_KEY = config.getProperty("app.private.key");
	/** 应用ID */
	public static String APP_ID = config.getProperty("app.id");
	/** 支付宝公钥 */
	public static String ALIPAY_PUBLIC_KEY = config.getProperty("alipay.public.key");
	/** 签名方式 不需修改 */
	public static String GOTO_URL = config.getProperty("goto.url");
}
