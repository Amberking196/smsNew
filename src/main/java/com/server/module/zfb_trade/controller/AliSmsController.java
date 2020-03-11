package com.server.module.zfb_trade.controller;


import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.server.module.trade.util.SmsSendUtil;
import com.server.module.zfb_trade.bean.SmsSendRequest;
import com.server.module.zfb_trade.bean.SmsSendResponse;
import com.server.module.zfb_trade.service.AliSmsService;
import com.server.module.zfb_trade.util.ResultUtil;
import com.server.module.zfb_trade.util.enumparam.ResultEnum;
import com.server.redis.RedisService;
import com.server.redis.SmsKey;
import com.server.util.ReturnDataUtil;

@Controller
@RequestMapping(value = "/alisms")
public class AliSmsController {
	public static Logger log = LogManager.getLogger(AliSmsController.class); 
	
	@Autowired
	private RedisService redisService;
	@Autowired
	private AliSmsService smsService;

	public static final String charset = "utf-8";

	String preSmsCode = "sms_code_";

	@PostMapping("/send")
	@ResponseBody
	public ReturnDataUtil sendCode(@RequestBody Map<String,String> param) {
		log.info("<AliSmsController--sendCode--start>");
		String phone = param.get("phone");
		String smsCode = (int) ((Math.random() * 9 + 1) * 1000) + "";
		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
		log.info(" 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取");
		String msg = "【优水到家】你好,你的验证码是" + smsCode;
		// 手机号码
		SmsSendRequest smsSingleRequest = new SmsSendRequest(msg, phone);

		String requestJson = JSON.toJSONString(smsSingleRequest);

		log.info("before request string is: " + requestJson);

		String response = SmsSendUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
		if (response == null) {
			response = SmsSendUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
		}
		log.info("response after request result is :" + response);

		if (response != null) {
			redisService.set(SmsKey.getCodeByMobile, phone, smsCode);
			SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
			log.info("response  toString is :" + smsSingleResponse);
			log.info("<AliSmsController--sendCode--end>");
			return ResultUtil.success();
		} else {
			log.info("<AliSmsController--sendCode--end>");
			return ResultUtil.error(ResultEnum.UNKOWN_ERROR,null);
		}

	}
	
	@PostMapping("/sendMessage")
	@ResponseBody
	public ReturnDataUtil sendMessage(@RequestParam String phone,@RequestParam String msg){
		return smsService.sendMessage(phone, msg);
	}

}
