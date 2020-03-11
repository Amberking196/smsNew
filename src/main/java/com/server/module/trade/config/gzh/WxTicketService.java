package com.server.module.trade.config.gzh;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.redis.RedisService;
import com.server.util.HttpUtil;
import com.server.util.JsonUtils;
import com.server.util.RandomUtils;
import com.server.util.StringUtil;

@Component
public class WxTicketService {

	private Logger log = LogManager.getLogger(WxTicketService.class);
	@Autowired
	private WXGzhConfigFactory gzhConfigFactory;
	@Autowired
	private RedisService redisService;
	
	protected Lock accessTokenLock = new ReentrantLock();
	protected Lock jsapiTicketLock = new ReentrantLock();
	protected Lock cardApiTicketLock = new ReentrantLock();
	public String redisAccessToken = "wx_accessToken_base";
	public String redisJsapiTicket = "wx_jsapiTicket_base";
	public String redisSdkTicket = "wx_sdkTicket_base";
	/**
	 * 获取ticket，该方法使用了lock来处理并发问题，若有性能问题以后需改写
	 * @author hebiting
	 * @date 2018年6月22日上午9:58:41
	 * @return
	 */
	public String getTicket(MyWXGzhConfig myWXGzhConfig) {
		Lock lock = jsapiTicketLock;
		try {
			lock.lock();
			String access_token = "";
			String ticket = "";
			String apiticket = redisService.getString(redisJsapiTicket);
			log.info("[apiticket] =" + apiticket);
			if (StringUtil.isBlank(apiticket)) {
				Lock secondLock = accessTokenLock;
				try{
					secondLock.lock();
					String act = redisService.getString(redisAccessToken);
					log.info("[act] ="+act);
					if (StringUtil.isBlank(act)) {
						StringBuffer url = new StringBuffer("https://api.weixin.qq.com/cgi-bin/token?");
						url.append("grant_type=client_credential&appid=");
						url.append(myWXGzhConfig.getAppId());
						url.append("&secret=");
						url.append(myWXGzhConfig.getSecret());
						String jsonStrToken = null;
						log.debug("[jsonStrToken] = " + jsonStrToken);
						String jsonResult = HttpUtil.get(url.toString(),5);
						if(StringUtil.isBlank(jsonResult)){
							log.info("获取access_token失败，请紧急处理！");
							return null;
						}
						Map<String,Object> resultStrToken = JsonUtil.toObject(jsonResult, new TypeReference<Map<String,Object>>(){});
						access_token = resultStrToken.get("access_token") != null
								? resultStrToken.get("access_token").toString() : null;
						Object accessExpires = resultStrToken.get("expires_in");
						if (access_token == null || accessExpires == null) {
							log.info("获取access_token为null 或者 过期时间为null,jsonResult="+jsonResult);
							return null;
						} else {
							redisService.set(redisAccessToken,access_token, Integer.valueOf(accessExpires.toString())-200);
						}
					} else {
						access_token = (String) act;
					}
				}finally{
					secondLock.unlock();
				}
				
				StringBuffer urlTicket = new StringBuffer("https://api.weixin.qq.com/cgi-bin/ticket/getticket?");
				urlTicket.append("access_token=");
				urlTicket.append(access_token);
				urlTicket.append("&type=jsapi");
				String jsonResult = HttpUtil.get(urlTicket.toString(),5);
				if(StringUtil.isBlank(jsonResult)){
					log.info("获取ticket失败，请紧急处理！");
				}
				Map<String,Object> result = JsonUtil.toObject(jsonResult, new TypeReference<Map<String,Object>>(){});
				ticket = result.get("ticket")!=null?result.get("ticket").toString():null;
				Object ticketExpires = result.get("expires_in");
				if(ticket!=null && ticketExpires!=null){
					log.info("[ticket] =" + ticket);
					redisService.set(redisJsapiTicket,ticket, Integer.valueOf(ticketExpires.toString())-200);
				}else{
					log.info("获取的ticket 或 过期时间为null,jsonResult="+jsonResult);
				}
			} else {
				ticket = (String) apiticket;
			}
			return ticket;
		} finally {
			lock.unlock();
		}
	}
	/**
	 * 获取签名
	 * @author hebiting
	 * @date 2018年6月25日下午1:59:16
	 * @param ticket
	 * @param timestamp
	 * @param noncestr
	 * @param url
	 * @return
	 */
	public String getSignature(String ticket,long timestamp,String noncestr,String url){
		StringBuffer str = new StringBuffer("jsapi_ticket=");
		str.append(ticket);
		str.append("&noncestr=");
		str.append(noncestr);
		str.append("&timestamp=");
		str.append(timestamp);
		str.append("&url=");
		str.append(url);
		String signature = DigestUtils.sha1Hex(str.toString());
		return signature;
	}
	/**
	 * 获取jsapi的签名结果
	 * @author hebiting
	 * @date 2018年6月25日下午1:59:25
	 * @param url
	 * @return
	 */
	public Map<String,String> getJspidSignature(String url,Integer companyId){
		MyWXGzhConfig myWXGzhConfig = gzhConfigFactory.getMyWXGzhConfig(companyId);
		long timestamp = System.currentTimeMillis()/1000;
		String nonceStr = RandomUtils.getRandomStr();
		String ticket = getTicket(myWXGzhConfig);
		String signature = getSignature(ticket,timestamp,nonceStr,url);
		Map<String,String> result = new HashMap<String,String>();
		result.put("appId", myWXGzhConfig.getAppId());
		result.put("timestamp", timestamp+"");
		result.put("nonceStr", nonceStr);
		result.put("url", url);
		result.put("signature", signature);
		return result;
	}
	
	//获取AccessToken
	public String getAccessToken(MyWXGzhConfig myWXGzhConfig) {
		Lock lock = accessTokenLock;
		try {
			lock.lock();
			String access_token = "";
			String act = redisService.getString(redisAccessToken);
			if (StringUtil.isBlank(act)) {
				StringBuffer url = new StringBuffer("https://api.weixin.qq.com/cgi-bin/token?");
				url.append("grant_type=client_credential&appid=");
				url.append(myWXGzhConfig.getAppId());
				url.append("&secret=");
				url.append(myWXGzhConfig.getSecret());
				String jsonResult = HttpUtil.get(url.toString(),5);
				if(StringUtil.isBlank(jsonResult)){
					log.info("获取access_token失败，请紧急处理！");
					return null;
				}
				Map<String,Object> resultStrToken = JsonUtil.toObject(jsonResult, new TypeReference<Map<String,Object>>(){});
				access_token = resultStrToken.get("access_token") != null
						? resultStrToken.get("access_token").toString() : null;
				Object accessExpires = resultStrToken.get("expires_in");
				if (access_token == null || accessExpires == null) {
					log.info("获取access_token为null 或者 过期时间为null="+jsonResult);
					return null;
				} else {
					redisService.set(redisAccessToken,access_token, Integer.valueOf(accessExpires.toString())-200);
				}
			} else {
				access_token = (String) act;
			}
			return access_token;
		}finally {
			lock.unlock();
		}
		
	}
	
	//获取App的AccessToken
	public String getAppAccessToken(MyWXGzhConfig myWXGzhConfig) {
			Lock lock = accessTokenLock;
			try {
				lock.lock();
				String access_token = "";
				String act = redisService.getString(redisAccessToken);
				if (StringUtil.isBlank(act)) {
					StringBuffer url = new StringBuffer("https://api.weixin.qq.com/sns/oauth2/access_token?");
					url.append("appid=");
					url.append(myWXGzhConfig.getAppAppId());
					url.append("&secret=");
					url.append(myWXGzhConfig.getAppSecret());
					url.append("&code=code");
					
					url.append("&grant_type=authorization_code");
					String jsonResult = HttpUtil.get(url.toString(),5);
					if(StringUtil.isBlank(jsonResult)){
						log.info("获取access_token失败，请紧急处理！");
						return null;
					}
					Map<String,Object> resultStrToken = JsonUtil.toObject(jsonResult, new TypeReference<Map<String,Object>>(){});
					access_token = resultStrToken.get("access_token") != null
							? resultStrToken.get("access_token").toString() : null;
					Object accessExpires = resultStrToken.get("expires_in");
					String refreshToken	= (String) resultStrToken.get("refresh_token");
					
					if (access_token == null || accessExpires == null) {
						log.info("获取access_token为null 或者 过期时间为null="+jsonResult);
						return null;
					} else {
						redisService.set(redisSdkTicket,access_token, Integer.valueOf(accessExpires.toString())-200);
					}
				} else {
					access_token = (String) act;
				}
				return access_token;
			}finally {
				lock.unlock();
			}
			
		}
	
	//app刷新token 30天后返回尚未清楚
	public void refreshToken(MyWXGzhConfig myWXGzhConfig) {
		StringBuffer url = new StringBuffer("https://api.weixin.qq.com/sns/oauth2/refresh_token?");
		url.append("appid=?");
		url.append(myWXGzhConfig.getAppAppId());
		url.append("&grant_type=refresh_token");
		url.append("&refresh_token=REFRESH_TOKEN");
		
		
		//https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
	}

}
 