package com.server.module.trade.web;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.server.module.trade.support.SmsSendRequest;
import com.server.module.trade.support.SmsSendResponse;
import com.server.module.trade.util.HttpClientUtil;
import com.server.module.trade.util.ResultBean;
import com.server.module.trade.web.service.SmsService;
import com.server.redis.RedisService;
import com.server.redis.SmsKey;
@Controller
@RequestMapping(value = "/sms")
public class SmsController {

	public static Logger log = LogManager.getLogger( SmsController.class);    	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private SmsService smsService;
	public static final String charset = "utf-8";
	
	@PostMapping("/send")
	@ResponseBody
	public ResultBean<?> sendCode(@RequestBody Map<String,String> param,HttpServletRequest request){
		       ResultBean<?> result=new ResultBean();
		       String phone = param.get("phone");
		       log.info("phone对应客户端地址"+request.getRemoteAddr());
		       //不做限制
		       // HBT  待解决：对访问请求不作限制
//		       filterSend(phone, request, result);
//		       if(result.getCode()<0){
//		    	   return result;
//		       }
		       String smsCode=smsService.genSmsCode();
		      //请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
			    String msg = "【优水到家】您好,你的验证码是"+smsCode+",请在5分钟内使用！";
				//手机号码
				String response = smsService.sendMsg(phone, msg);
				if(response!=null){
					redisService.set(SmsKey.getCodeByMobile, phone, smsCode);
					SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
					log.info("response  toString is :" + smsSingleResponse);
					return result;
				}else{
					result.setCode(ResultBean.FAIL);
					return result;
				}
	}


	private void filterSend(String phone, HttpServletRequest request, ResultBean<?> result) {
		//防刷  start
		  String ip=request.getRemoteAddr();
		   Integer ipCount=redisService.get(SmsKey.ip, ip,Integer.class);
		   if(ipCount!=null){
			   if(ipCount>5){
				   result.setCode(-99);
				   result.setMsg("超出请求次数");
			   }
			   redisService.incr(SmsKey.ip, ip);
			   
		   }else{
			   redisService.set(SmsKey.ip, ip,Integer.valueOf(1));

		   }
		   Integer phoneCount=redisService.get(SmsKey.mobile, phone,Integer.class);
		   if(phoneCount!=null){
			   if(phoneCount>5){
				   result.setCode(-99);
				   result.setMsg("超出请求次数");
			   }
			   redisService.incr(SmsKey.mobile, phone);
		   }else{
			   redisService.set(SmsKey.mobile, phone,Integer.valueOf(1));
		   }
		   
		   //防刷  end
	}
	
	
	@RequestMapping("/send1")
	@ResponseBody
	public ResultBean<?> sendCode1(String phone,String certificate){
		       ResultBean<?> result=new ResultBean();
		       String smsCode=(int)((Math.random()*9+1)*1000)+"";
		      //请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
				String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
			    String msg = "【优水到家】您好,你的验证码是"+smsCode+",请在5分钟内使用！";
				//手机号码
			//	String report= "true";
				SmsSendRequest smsSingleRequest = new SmsSendRequest(msg, phone);
				String requestJson = JSON.toJSONString(smsSingleRequest);
				String response=null;
				log.info("before request string is: " + requestJson);
				try {
					response=HttpClientUtil.post(smsSingleRequestServerUrl,SmsSendRequest.toMap(smsSingleRequest) );
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				log.info("response after request result is :" + response);
				if(response!=null){
					/*redisService.set(SmsKey.getCodeByMobile, phone, smsCode);
					SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
					log.info("response  toString is :" + smsSingleResponse);
					return result;*/
				}else{
					result.setCode(ResultBean.FAIL);
					return result;
				}
				return result;
	}
	@RequestMapping("/test")
	@ResponseBody
	public Object sendtest(HttpServletRequest request){
		
		 ResultBean<?> result=new ResultBean();
	       String ip=request.getRemoteAddr();
	       Integer ipCount=redisService.get(SmsKey.ip, ip,Integer.class);
	       if(ipCount!=null){
	    	   if(ipCount>5){
	    		   result.setCode(-99);
	    		   result.setMsg("超出请求次数");
	    		   return result;
	    	   }
	    	   redisService.incr(SmsKey.ip, ip);
	    	   
	       }else{
	    	   redisService.set(SmsKey.ip, ip,Integer.valueOf(1));

	       }
	       try{
	    	   int i=1/0;
	       }catch(Exception e){
	    	   e.printStackTrace();
	    	   log.error(e.getMessage());
	       }
	      
		//stringRedisTemplate.opsForValue().set("smsCode", "9785");
		//stringRedisTemplate.opsForValue().set("smsCode", "9527");
		//stringRedisTemplate.expire("smsCode", 300, TimeUnit.SECONDS);
		//System.out.println("jkdfheskdhflkdsjfkdsf899999999999999999");
		//return stringRedisTemplate.opsForValue().get("smsCode");
	      // System.out.println(request.getRemoteAddr());
	     //  System.out.println(request.get);

	  /*  redisService.setString("nihao", "hello");
	    System.out.println(redisService.getString("nihao"));
	    
	    redisService.del("nihao");
	    System.out.println("-----");
	    System.out.println(redisService.getString("nihao"));
	    
	    redisService.set(MachinesKey.currentMachinesOpenState, "vmcode", "kkkk");
	    
	    
      System.out.println(redisService.get(MachinesKey.currentMachinesOpenState, "vmcode"));
	    
	    redisService.del(MachinesKey.currentMachinesOpenState, "vmcode");
	    System.out.println("---ddd--");
	    System.out.println(redisService.get(MachinesKey.currentMachinesOpenState, "vmcode"));
	    */

		return null;

	}
	
	@RequestMapping("/getCode")
	@ResponseBody
	public Object getCode(){
		//stringRedisTemplate.opsForValue().set("smsCode", "9785");
		//stringRedisTemplate.opsForValue().set("smsCode", "9785", 10);
		redisService.set(SmsKey.getCodeByMobile, "13570339593", "8888");
		System.out.println("jkdfheskdhflkdfdfdfd======dsjfkdsf899999999999999999");
		System.out.println(redisService.get(SmsKey.getCodeByMobile, "13570339593", String.class));
		//return stringRedisTemplate.opsForValue().get("smsCode");
		return null;

	}

}
