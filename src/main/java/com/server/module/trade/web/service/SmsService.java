package com.server.module.trade.web.service;

import com.server.module.zfb_trade.module.vmbase.VmbaseInfoDto;

public interface SmsService {
	public String sendMsg(String phone,String msg);
	
	public String genSmsCode();
	
	public  boolean checkCode(String phone,String code);
	
	public String sendMsg(Long customerId,String msg);
	
	public String sendMsgInterval(Long customerId,VmbaseInfoDto baseInfo,String msg,Integer seconds);
	
	public String sendMsgInterval(String phone,String msg,Integer seconds);
}
