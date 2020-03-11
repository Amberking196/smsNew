package com.server.module.zfb_trade.service.impl;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.server.module.trade.util.SmsSendUtil;
import com.server.module.zfb_trade.bean.SmsSendRequest;
import com.server.module.zfb_trade.bean.SmsSendResponse;
import com.server.module.zfb_trade.service.AliSmsService;
import com.server.module.zfb_trade.util.ResultUtil;
import com.server.module.zfb_trade.util.enumparam.ResultEnum;
import com.server.util.ReturnDataUtil;
@Service("aliSmsService")
public class AliSmsServiceImpl implements AliSmsService{

	public static Logger log = LogManager.getLogger(AliSmsServiceImpl.class); 
	public ReturnDataUtil sendMessage(String phone,String msg) {
		log.info("<AliSmsServiceImpl--sendMessage--start>");
		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
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
			SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
			log.info("response  toString is :" + smsSingleResponse);
			log.info("<AliSmsServiceImpl--sendMessage--end>");
			return ResultUtil.success();
		} else {
			log.info("<AliSmsServiceImpl--sendMessage--end>");
			return ResultUtil.error(ResultEnum.UNKOWN_ERROR,null);
		}

	}
}
