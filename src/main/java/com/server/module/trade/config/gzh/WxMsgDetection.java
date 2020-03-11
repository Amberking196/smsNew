package com.server.module.trade.config.gzh;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.redis.RedisService;
@Component
public class WxMsgDetection {
	@Autowired
	private WxTicketService wxTicketService;
	@Autowired
	private RedisService redisService;

	public boolean detectionAccessToken(Map<String,Object> msg){
		boolean result = true;
		if(msg.containsKey("errcode")){
			String errcode = msg.get("errcode").toString();
			if(("40001").equals(errcode)){
				result = false;
				redisService.del(wxTicketService.redisAccessToken);
			}else if(("0").equals(errcode)){
				result = true;
			}else{
				result = false;
			}
		}
		return result;
	}
}
