//package com.server.module.trade.config;
//
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import com.server.module.trade.auth.filter.AuthFilter;
//import com.server.module.trade.auth.security.DataSecurityAction;
//import com.server.module.trade.auth.security.impl.Base64SecurityAction;
//import com.server.module.trade.config.properties.RestProperties;
//
//import me.chanjar.weixin.common.exception.WxErrorException;
//import me.chanjar.weixin.common.session.WxSessionManager;
//import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
//import me.chanjar.weixin.mp.api.WxMpMessageHandler;
//import me.chanjar.weixin.mp.api.WxMpMessageRouter;
//import me.chanjar.weixin.mp.api.WxMpService;
//import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
//import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
//import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
//import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
//
///**
// * web配置
// *
// * @author yjr
// * @date 2017-08-23 15:48
// */
//@Configuration
//public class WebConfig {
//	@Value("${wechat.appId}")
//	private String appId;
//	@Value("${wechat.secret}")
//	private String secret;
//	@Value("${wechat.token}")
//	private String token;
//
//    @Bean
//    @ConditionalOnProperty(prefix = RestProperties.REST_PREFIX, name = "auth-open", havingValue = "true", matchIfMissing = true)
//    public AuthFilter jwtAuthenticationTokenFilter() {
//        return new AuthFilter();
//    }
//
//    @Bean
//    public DataSecurityAction dataSecurityAction() {
//        return new Base64SecurityAction();
//    }
//    
//    
//	@Bean
//	public WxMpService initWxMpService(){
//		WxMpInMemoryConfigStorage config=new WxMpInMemoryConfigStorage();
//	    config.setAppId(appId); // 设置微信公众号的appid
//	    config.setSecret(secret); // 设置微信公众号的app corpSecret
//	    config.setToken(token); // 设置微信公众号的token
//	   // config.setAesKey("..."); // 设置微信公众号的EncodingAESKey
//	    WxMpService wxMpService = new WxMpServiceImpl();
//	    wxMpService.setWxMpConfigStorage(config);
//	    return wxMpService;
//	}
//	@Bean
//    public WxMpMessageRouter initWxMpMessageRouter(){
//    	
//    	  WxMpMessageHandler handler = new WxMpMessageHandler() {
//
//			@Override
//			public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
//					WxSessionManager arg3) throws WxErrorException {
//				// TODO Auto-generated method stub
//				 WxMpXmlOutTextMessage m
// 	            = WxMpXmlOutMessage.TEXT().content("测试加密消息").fromUser(wxMessage.getToUser())
// 	            .toUser(wxMessage.getFromUser()).build();
// 	        return m;
//			}
//    	  };
//    	  
//    	  WxMpMessageRouter wxMpMessageRouter = new WxMpMessageRouter(initWxMpService());
//    	    wxMpMessageRouter
//    	        .rule()
//    	        .async(false)
//    	        .content("哈哈") // 拦截内容为“哈哈”的消息
//    	        .handler(handler)
//    	        .end();
//
//			return wxMpMessageRouter;
//
//    }
//}
