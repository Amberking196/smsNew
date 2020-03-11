package com.server.module.trade.web.service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.server.module.trade.customer.TblCustomerDao;
import com.server.module.trade.support.SmsSendRequest;
import com.server.module.trade.util.SmsSendUtil;
import com.server.module.zfb_trade.module.vmbase.VmbaseInfoDto;
import com.server.redis.RedisService;
import com.server.redis.SmsKey;
import com.server.util.StringUtil;

@Service
public class SmsServiceImpl implements SmsService {


	public static Logger log = LogManager.getLogger( SmsServiceImpl.class);    
	@Autowired
	private RedisService redisService;
	@Autowired
	private TblCustomerDao tblCustomerDao;
	
	@Override
	public String sendMsg(Long customerId, String msg){
		String response = null;
		try{
			String phone = tblCustomerDao.getPhoneByCustomerId(customerId);
			if (StringUtil.isNotBlank(phone)) {
				response = sendMsg(phone, msg);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	@Override
	public String sendMsgInterval(Long customerId, VmbaseInfoDto baseInfo, String msg, Integer seconds){
		String response = null;
		try{
			String phone = tblCustomerDao.getPhoneByCustomerId(customerId);
			if (StringUtil.isNotBlank(phone) && redisService.setIfNotExists("sendMsgInterval"+phone, "1", seconds)) {
				String userMsg="系统检测到机器编号为"+baseInfo.getVmCode()+"的售货机，用户购买后没有将门关好，请及时让运维工作人员处理，以免造成误扣款或其他损失。谢谢！";

				response = sendMsg(phone, msg);
				sendMsg(baseInfo.getPrincipalPhone(), userMsg);

			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	@Override
	public String sendMsg(String phone, String msg) {
		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
		// String msg = "【253云通讯】你好,你的验证码是"+smsCode;
		// 手机号码
		// String report= "true";
		SmsSendRequest smsSingleRequest = new SmsSendRequest(msg, phone);
		String requestJson = JSON.toJSONString(smsSingleRequest);
		log.info("before request string is: " + requestJson);
		String response = SmsSendUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
		if (response == null) {
			response = SmsSendUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
		}
		log.info("response after request result is :" + response);
		return response;
	}
	@Override
	public String genSmsCode() {
		return (int)((Math.random()*9+1)*1000)+"";
	}
	@Override
	public  boolean checkCode(String phone, String code){
		String redisCode=redisService.get(SmsKey.getCodeByMobile, phone, String.class);
		if(code.equals(redisCode)) {
			return true;
		}
		return false;
	}

	@Override
	public String sendMsgInterval(String phone, String msg, Integer seconds) {
		String response = null;
		try{
			if (StringUtil.isNotBlank(phone) && redisService.setIfNotExists("sendMsgInterval"+phone, "1", seconds)) {
				response = sendMsg(phone, msg);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}

}
