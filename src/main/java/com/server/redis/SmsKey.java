package com.server.redis;

public class SmsKey extends BasePrefix {

	public SmsKey(String prefix) {
		super(prefix);
	}


	public SmsKey(String prefix,int expireSeconds) {
		super(prefix,expireSeconds);
	}


	public static SmsKey getCodeByMobile=new SmsKey("mobile",300);
	
	public static SmsKey ip=new SmsKey("ipCount",300);
	public static SmsKey mobile=new SmsKey("sendCount",300);

}
