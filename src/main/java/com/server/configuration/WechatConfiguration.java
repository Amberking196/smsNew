package com.server.configuration;


import org.springframework.context.annotation.Configuration;

/*import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;*/
@Configuration
public class WechatConfiguration {
	/*@Bean
	public WxMpService initWxMpService(){
		WxMpInMemoryConfigStorage config=new WxMpInMemoryConfigStorage();
	    config.setAppId("wx1f797b706d58f98d"); // 设置微信公众号的appid
	    config.setSecret("bf077d0c5bc9346db585dd2c7660474f"); // 设置微信公众号的app corpSecret
	    config.setToken("youshui"); // 设置微信公众号的token
	   // config.setAesKey("..."); // 设置微信公众号的EncodingAESKey
	    WxMpService wxMpService = new WxMpServiceImpl();
	    wxMpService.setWxMpConfigStorage(config);
	    return wxMpService;
	}
	@Bean
    public WxMpMessageRouter initWxMpMessageRouter(){
    	
    	  WxMpMessageHandler handler = new WxMpMessageHandler() {

			@Override
			public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
					WxSessionManager arg3) throws WxErrorException {
				// TODO Auto-generated method stub
				 WxMpXmlOutTextMessage m
 	            = WxMpXmlOutMessage.TEXT().content("测试加密消息").fromUser(wxMessage.getToUser())
 	            .toUser(wxMessage.getFromUser()).build();
 	        return m;
			}
    	  };
    	  
    	  WxMpMessageRouter wxMpMessageRouter = new WxMpMessageRouter(initWxMpService());
    	    wxMpMessageRouter
    	        .rule()
    	        .async(false)
    	        .content("哈哈") // 拦截内容为“哈哈”的消息
    	        .handler(handler)
    	        .end();

			return wxMpMessageRouter;

    }*/
}
