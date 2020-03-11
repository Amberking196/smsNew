package com.server.module.trade.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.server.module.activeDegree.UserActiveDegreeService;
import com.server.module.coupon.CouponBean;
import com.server.module.coupon.CouponCustomerBean;
import com.server.module.coupon.CouponForm;
import com.server.module.coupon.CouponService;
import com.server.module.event.EventBean;
import com.server.module.event.EventConfig;
import com.server.module.event.EventDao;
import com.server.module.trade.config.gzh.MyWXGzhConfig;
import com.server.module.trade.config.gzh.WXGzhConfigFactory;
import com.server.module.trade.config.gzh.WxTicketService;
import com.server.module.trade.customer.TblCustomerBean;
import com.server.module.trade.customer.TblCustomerDao;
import com.server.module.trade.customer.TblCustomerService;
import com.server.module.trade.customer.wxcustomer.WxCustomerBean;
import com.server.module.trade.customer.wxcustomer.WxCustomerService;
import com.server.module.trade.util.ResultBean;
import com.server.module.trade.web.service.UserService;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.redis.RedisService;
import com.server.util.EmojiUtil;
import com.server.util.HttpUtil;
import com.server.util.JsonUtils;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CompanyEnum;
import com.server.util.stateEnum.CouponEnum;
import com.server.util.stateEnum.PayTypeEnum;
import com.server.util.stateEnum.RegisterFlowEnum;
import com.server.util.wechatMessage.InputMessage;
import com.server.util.wechatMessage.MsgType;
import com.server.util.wechatMessage.OutputMessage;
import com.server.util.wechatMessage.SHA1;
import com.server.util.wechatMessage.SerializeXmlUtil;
import com.thoughtworks.xstream.XStream;


@Controller
@RequestMapping("/weixin")
public class WechatEventController {
	
	private final static Logger log = LogManager.getLogger(WechatEventController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private TblCustomerService tblCustomerService;
	@Autowired
	private CouponService couponService;
	@Autowired
	private WxCustomerService wxCustomerService;
	@Autowired
	private WXGzhConfigFactory gzhConfigFactory;
	@Autowired 
	private WxTicketService wxTicketService;
	@Autowired
	private TblCustomerDao customerDao;
	@Autowired
	private UserActiveDegreeService userActiveDegreeService;
	@Autowired
	private EventConfig eventConfig;
	@Autowired
	private EventDao eventDao;
	
	@Autowired
	private RedisService redisService;
	
	@Value("${wechat.customerLogin}")
	private String customerLogin;
	
	private String Token = "youshui";
	private String AccessToken="";
	//创菜单 删菜单 获取菜单
	//https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN
	private TransactionTemplate transactionTemplate;
    @RequestMapping(value = "/chat", method = { RequestMethod.GET, RequestMethod.POST })  
    @ResponseBody  
    public void liaotian(HttpServletRequest request, HttpServletResponse response) {  
        //log.info("进入chat");  
        boolean isGet = request.getMethod().toLowerCase().equals("get");  
        if (isGet) {  
            String signature = request.getParameter("signature");  
            String timestamp = request.getParameter("timestamp");  
            String nonce = request.getParameter("nonce");  
            String echostr = request.getParameter("echostr");  
            log.info(signature);  
            log.info(timestamp);  
            log.info(nonce);  
            log.info(echostr);  
            access(request, response);  
        } else {  
            // 进入POST聊天处理  
            //log.info("enter post");  
            try {  
                // 接收消息并返回消息  
                acceptMessage(request, response);  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    /**  
     * 验证URL真实性  
     *   
     * @author morning  
     * @date 2015年2月17日 上午10:53:07  
     * @param request  
     * @param response  
     * @return String  
     */  
    private String access(HttpServletRequest request, HttpServletResponse response) {  
        // 验证URL真实性  
        log.info("进入验证access");  
        String signature = request.getParameter("signature");// 微信加密签名  
        String timestamp = request.getParameter("timestamp");// 时间戳  
        String nonce = request.getParameter("nonce");// 随机数  
        String echostr = request.getParameter("echostr");// 随机字符串  
        List<String> params = new ArrayList<String>();  
        params.add(Token);  
        params.add(timestamp);  
        params.add(nonce);  
        // 1. 将token、timestamp、nonce三个参数进行字典序排序  
        Collections.sort(params, new Comparator<String>() {  
            @Override  
            public int compare(String o1, String o2) {  
                return o1.compareTo(o2);  
            }  
        });  
        // 2. 将三个参数字符串拼接成一个字符串进行sha1加密  
        String temp = SHA1.encode(params.get(0) + params.get(1) + params.get(2));  
        if (temp.equals(signature)) {  
            try {  
                response.getWriter().write(echostr);  
                log.info("成功返回 echostr：" + echostr);  
                return echostr;  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        log.info("失败认证");  
        return null;  
    }  
  
    private void acceptMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        // 处理接收消息  
        ServletInputStream in = request.getInputStream();  
        // 将POST流转换为XStream对象  
        XStream xs = SerializeXmlUtil.createXstream();  
        xs.processAnnotations(InputMessage.class);  
        xs.processAnnotations(OutputMessage.class);  
        // 将指定节点下的xml节点数据映射为对象  
        xs.alias("xml", InputMessage.class);  
        // 将流转换为字符串  
        StringBuilder xmlMsg = new StringBuilder();  
        byte[] b = new byte[4096];  
        for (int n; (n = in.read(b)) != -1;) {  
            xmlMsg.append(new String(b, 0, n, "UTF-8"));  
        }  
        // 将xml内容转换为InputMessage对象  
        InputMessage inputMsg = (InputMessage) xs.fromXML(xmlMsg.toString());  
  
        String servername = inputMsg.getToUserName();// 服务端  
        String custermname = inputMsg.getFromUserName();// 客户端  
        long createTime = inputMsg.getCreateTime();// 接收时间  
        Long returnTime = Calendar.getInstance().getTimeInMillis() / 1000;// 返回时间  

        String msgType = inputMsg.getMsgType();        // 取得消息类型  
        String event = inputMsg.getEvent();// 事件
        String eventKey = inputMsg.getEventKey();//事件id
        
//        log.info("开发者微信号：" + inputMsg.getToUserName());
////        log.info("发送方帐号：" + inputMsg.getFromUserName());
////        log.info("消息创建时间：" + inputMsg.getCreateTime() + new Date(createTime * 1000l));
////        log.info("消息内容：" + inputMsg.getContent());
////        log.info("MsgId：" + inputMsg.getMsgId());
////        log.info("MsgType：" + inputMsg.getMsgType());
////        log.info("Event：" + inputMsg.getEvent());
////        log.info("EventKey：" + inputMsg.getEventKey());

        StringBuffer str = new StringBuffer();  
        str.append("<xml>");  
        str.append("<ToUserName><![CDATA[" + custermname + "]]></ToUserName>");  
        str.append("<FromUserName><![CDATA[" + servername + "]]></FromUserName>");  
        str.append("<CreateTime>" + returnTime + "</CreateTime>");  
        
        List<EventBean> eventList=eventConfig.getEventList();
        					   
        String eventConfigVersion = redisService.getString("eventConfigVersion");
        if(eventConfigVersion!=null && !eventConfigVersion.equals(eventConfig.getVersion())) {
        	eventList=eventConfig.updateEventList(eventConfigVersion);
        }
        
        boolean autoReply=true;//true 
        boolean isPic=false;//false 文字回复  true 图文回复
        boolean returnSuccess=false;//false 回复  true 不回复
       // MsgType	消息类型，event
       // Event	事件类型，subscribe(订阅)、unsubscribe(取消订阅) 、SCAN(扫描带参二维码)  EventKey:Invite26
       //返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENEPROFILE LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_OTHERS 其他
        if(msgType.equals(MsgType.Event.toString())) {
        	//关注公众号后自动回复
            if(event.equals("subscribe") || event.equals("SCAN")) {
            	for(EventBean eb:eventList) {
            		if(eb.getType().equals("follow")) {
            			//log.info(eb.getContent());
            			inputMsg.setContent(eb.getContent());
            		}
            	}
//            	inputMsg.setContent("感谢您关注“优水到家”\r\n" + 
//            			"\r\n" + 
//            			"感恩回馈，进入\"水商城-优水商城“购水长期优惠<a href=\"https://mp.weixin.qq.com/s/vbO3IwlG76DstUR5b0lC3w\">了解详情</a>\r\n" + 
//            			"\r\n" + 
//            			"请您点击领取您的<a href=\"http://t.cn/EyJOrOR\">100元优惠礼包</a>\r\n" + 
//            			"\r\n" + 
//            			"向好友成功推荐优水后立送10积分参与抽奖<a href=\"http://t.cn/EAMqOJd\">点击生成</a>专属推荐页面\r\n" + 
//            			"\r\n" + 
//            			"详细购买流程请看<a href=\"http://t.cn/EAMqECr\">图文教程</a>\r\n");
            	
        		TblCustomerBean customer = userService.getCustomer(PayTypeEnum.WEIXIN.getIndex(),inputMsg.getFromUserName());
        		//微信已注册用户直接关注送券 并标志为1
        		if(customer!=null) {//用户已存在 考虑送优惠券
        			WxCustomerBean wxCustomerBean=wxCustomerService.selectByOpenId(customer.getOpenId(), null);
        			Boolean update=false;
        			Boolean updateFollowUserNum=false;
        			if(customer.getFollow()==0) {
        				update=true;updateFollowUserNum=true;
        			}
        			if(customer.getIsSend()==0) {
	        			CouponForm couponForm = new CouponForm();
	        			couponForm.setCompanyId(CompanyEnum.YOUSHUI.getCompanyId());
	        			couponForm.setWay(CouponEnum.FOLLOW_COUPON.getState());
	        			couponForm.setLimitRange(true);
	        			List<CouponBean> presentCoupon = couponService.getPresentCoupon(couponForm);
	        			if(presentCoupon != null && presentCoupon.size()>0){
	    					presentCoupon.stream().
	    					filter(coupon -> !couponService.isReceive(customer.getId(), coupon.getId())).
	    					forEach(coupon -> {
	    						CouponCustomerBean couCusBean = new CouponCustomerBean();
	    						couCusBean.setQuantity(coupon.getSendMax());
	    						couCusBean.setCouponId(coupon.getId());
	    						couCusBean.setCustomerId(customer.getId());
	    						couCusBean.setStartTime(coupon.getLogicStartTime());
	    						couCusBean.setEndTime(coupon.getLogicEndTime());
	    						couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());//状态：0：新建，1：领取，2：已使用
	    						couponService.insertCouponCustomer(couCusBean);
	    					});
	    					userActiveDegreeService.update(customer.getId(), RegisterFlowEnum.RECEIVE_PERCENT);
		        			customer.setIsSend(1);
		        			update=true;
	    				}
        			}
        			if(wxCustomerBean!=null && StringUtils.isBlank(wxCustomerBean.getNickname())) {
        				MyWXGzhConfig myWXGzhConfig2 = gzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
    					String accessToken=wxTicketService.getAccessToken(myWXGzhConfig2);
        				String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+customer.getOpenId(); 
        				String jsonResult2 = HttpUtil.get(url);
        				Map<String,Object> result = JsonUtil.toObject(jsonResult2, new TypeReference<Map<String,Object>>(){});
        				log.info("用户者信息"+result);
        				String nickname=(String) result.get("nickname");
        				nickname=EmojiUtil.getString(nickname);
	
        				String headimgurl=(String) result.get("headimgurl");
        				Integer sex=(Integer) result.get("sex");
        				if(nickname!=null) {
		        			wxCustomerBean.setNickname(nickname);
		        			wxCustomerBean.setHeadimgurl(headimgurl);
		        			wxCustomerService.update(wxCustomerBean);
		        			if(StringUtils.isBlank(customer.getNickname())) {
	            				customer.setNickname(nickname);
	            				customer.setHeadImgUrl(headimgurl);
	            				customer.setSexId(sex);
			        			update=true;
		        			}
        				}

        			}
        			if(update) {
        				if(updateFollowUserNum) {
            				userActiveDegreeService.update(customer.getId(), RegisterFlowEnum.ATTENTION);
        				}
                		customer.setFollow(1);
        				customerDao.update(customer);
        			}
        		}
        		
    			
        		//EventKey	事件KEY值，qrscene_为前缀，后面为二维码的参数值
/*            	if(inputMsg.getEventKey()!=null) {
            		log.info("---"+inputMsg.getEventKey());
            		String subkey=inputMsg.getEventKey().split("qrscene_")[1];
            		if(subkey.equals("80")) {
                		//绑定80
            			log.info("scene_id:"+subkey);
            		}
            		
            	}*/
            	
            	
            }else if(event.equals("unsubscribe")){//用户取消关注公众号
            	TblCustomerBean customer = userService.getCustomer(PayTypeEnum.WEIXIN.getIndex(),inputMsg.getFromUserName());
        		if(customer!=null) {
        			userActiveDegreeService.update(customer.getId(), RegisterFlowEnum.CANCEL_ATTENTION);
                	customer.setFollow(0);
    				customerDao.update(customer);
        		}

            }
            
            else if(event.equals("CLICK")){//用户点击菜单按钮
            	for(EventBean eb:eventList) {
            		if(eb.getType().equals("clickMenu")) {
            			if(eventKey.equals(eb.getCode())) {
                			if(eb.getIsPic().equals("1")) {
                				//log.info("菜单内容"+eb.getContent());
                				isPic=true;
                				str.append(eb.getContent());
                			}else {
                    			inputMsg.setContent(eb.getContent());
                			}
                			break;
            			}
            		}
            	}
//            	if(eventKey.equals("20001")) {//留言有好礼
//            		inputMsg.setContent("您好，留言活动已经结束，请有参与活动留言的用户\r\n" +
//            				"回复 领奖+手机号码，领取礼品\r\n" + 
//            				"留言被点赞数最多的前两名用户，将会获得怡宝饮水机一台\r\n" +
//            				"小优会在今日内联系您们");
//            	}else if(eventKey.equals("20002")) {//最新活动
//    				isPic=true;
//     		        str.append("<MsgType><![CDATA[news]]></MsgType>");  
//        			//str.append("<ArticleCount>1</ArticleCount><Articles><item><Title>< ![CDATA[“优水”新一轮强力降水福利来啦！] ]></Title> <Description>< ![CDATA[优水给你发的福利已到账，请查收] ]></Description><PicUrl>< ![CDATA[http://mmbiz.qpic.cn/mmbiz_jpg/yIolvpYFIaBp8UUW9iaF8HDicmscx17ibYPUMTK1IlGbeoQD8UCv27ksCCAosAcHDz4FtUibeXGvx5tdDZZNK4TCCg/0?wx_fmt=jpeg] ]></PicUrl><Url>< ![CDATA[http://mp.weixin.qq.com/s?__biz=MzU0NzQ4MTY0Mg==&mid=100000151&idx=1&sn=14c50dc46ee9b8205000f5e95a5f9027&chksm=7b4cf0d24c3b79c42f1ac1e3b4abda3b22815e8a0649297c64ccba552fa154a60b2478919e4d#rd] ]></Url></item></Articles></xml>"); 
//        			str.append(" <ArticleCount>1</ArticleCount>");
//        			str.append(" <Articles>");
//        			    
//        			str.append(" <item>");
//        			str.append(" <Title><![CDATA双旦暖冬周特惠，商城购水买一送一]]></Title> ");
//        			str.append(" <Description><![CDATA[双旦暖冬周特惠，商城购水买一送一]]></Description>");
//        			str.append(" <PicUrl><![CDATA[http://mmbiz.qpic.cn/mmbiz_jpg/yIolvpYFIaChMpuRrQ7w7xP5Bz3oDkkibls70zjjUBrzmARTNFHyHehKkZCgW6sbFbVzaXyDlibDbWHwVVBnbB9A/0?wx_fmt=jpeg]]></PicUrl>");
//        			str.append(" <Url><![CDATA[http://mp.weixin.qq.com/s?__biz=MzU0NzQ4MTY0Mg==&mid=100000708&idx=2&sn=8a4327daf5c0362b36ed757ab4a009f9&chksm=7b4cf2814c3b7b970ba88e82bf9758c7f49e9cd3aa58d597fac64f5c785ed17127b635f7d683#rd]]></Url> ");
//        			str.append(" </item>");
//          			    
//        			str.append(" </Articles>");
//        			str.append(" </xml> ");
//            	}else if (eventKey.equals("20003")) {//亚运城好礼
//            		isPic=true;
//            		str.append(" <MsgType><![CDATA[image]]></MsgType> ");
//            		str.append(" <Image><MediaId><![CDATA[RJHbnurKP3_p-azW3I_XDIt6vqSk47D2HZjxLpT5oNc]]></MediaId></Image>");
//            		str.append(" </xml>");
//            	
//            	}else if (eventKey.equals("30001")) {//联系我们
//            		inputMsg.setContent("热线电话：020-020-89850917  小优客服：xygj_1\r\n" + 
//            				"办公地址：广州市科学城开源大道11号企业加速器B10栋4层");
//            				
//            	}else if(eventKey.equals("20004")) {//上一次最新活动 
//    				isPic=true;
//    		        str.append("<MsgType><![CDATA[news]]></MsgType>");  
//    			    //str.append("<ArticleCount>1</ArticleCount><Articles><item><Title>< ![CDATA[“优水”新一轮强力降水福利来啦！] ]></Title> <Description>< ![CDATA[优水给你发的福利已到账，请查收] ]></Description><PicUrl>< ![CDATA[http://mmbiz.qpic.cn/mmbiz_jpg/yIolvpYFIaBp8UUW9iaF8HDicmscx17ibYPUMTK1IlGbeoQD8UCv27ksCCAosAcHDz4FtUibeXGvx5tdDZZNK4TCCg/0?wx_fmt=jpeg] ]></PicUrl><Url>< ![CDATA[http://mp.weixin.qq.com/s?__biz=MzU0NzQ4MTY0Mg==&mid=100000151&idx=1&sn=14c50dc46ee9b8205000f5e95a5f9027&chksm=7b4cf0d24c3b79c42f1ac1e3b4abda3b22815e8a0649297c64ccba552fa154a60b2478919e4d#rd] ]></Url></item></Articles></xml>"); 
//    			    str.append(" <ArticleCount>1</ArticleCount>");
//    			    str.append(" <Articles>");
//    			    
//    			    str.append(" <item>");
//    			    str.append(" <Title><![CDATA[每月能省几十块！优水感恩回馈，商城购水长期优惠]]></Title> ");
//    			    str.append(" <Description><![CDATA[商城先购券，水盒自助拿，好水不贵，每桶最高可省3块钱]]></Description>");
//    			    str.append(" <PicUrl><![CDATA[http://mmbiz.qpic.cn/mmbiz_png/yIolvpYFIaC7funQsmoXlXBUKpfwsoplyxRyth174KwJB5xnTgdh08bos5yM6IRtu1WhoYficKNHnIzFZBvypTw/0?wx_fmt=png]]></PicUrl>");
//    			    str.append(" <Url><![CDATA[http://mp.weixin.qq.com/s?__biz=MzU0NzQ4MTY0Mg==&mid=100000605&idx=1&sn=7f4ebc80f774aeaf516831097d666fc3&chksm=7b4cf2184c3b7b0e5801569d8fbf8fb773919f516186d3a93f39675ecf6c71d1e11e508dae5d#rd]]></Url> ");
//    			    str.append(" </item>");
//  
//    			    str.append(" </Articles>");
//    			    str.append(" </xml> ");
//           	}
            } 		
        }else if(msgType.equals(MsgType.Text.toString())) {
        	boolean exist=redisService.setIfNotExists("reply-"+custermname,"Y",3600);
        	if(!exist) {//同一用户一小时内只自动回复一次
        		returnSuccess=true;
        	}
        	for(EventBean eb:eventList) {
        		if(eb.getType().equals("autoReply")) {
        			if(autoReply && eb.getCode().equals("自动回复")) {
            			if(eb.getIsPic().equals("1")) {
            				isPic=true;
            				str.append(eb.getContent());
            			}else {
                			inputMsg.setContent(eb.getContent());
            			}
        			}
          			if(inputMsg.getContent().contains(eb.getCode())) {//命中关键词
            			if(eb.getIsPic().equals("1")) {
            				isPic=true;
            				str.append(eb.getContent());
            			}else {
                			inputMsg.setContent(eb.getContent());
            			}
            			returnSuccess=false;
            			autoReply=false;
            			break; 
        			}
    				TblCustomerBean customer = userService.getCustomer(PayTypeEnum.WEIXIN.getIndex(), inputMsg.getFromUserName());

        			if(inputMsg.getContent().equals("绑定珠海")) {
        				if(customer==null) {
        					inputMsg.setContent("请先点击商城注册");
        				}else if(tblCustomerService.updateCusVmCode(customer.getId(),"1988000202")) {
        					inputMsg.setContent("绑定成功");
        				}else {
        					inputMsg.setContent("不可重复绑定机器");
        				}
            			returnSuccess=false;
            			autoReply=false;
            			break; 
        			}
           			if(inputMsg.getContent().equals("绑定保利")) {
        				if(customer==null) {
        					inputMsg.setContent("请先点击商城注册");
        				}else if(tblCustomerService.updateCusVmCode(customer.getId(),"1988000232")) {
        					inputMsg.setContent("绑定成功");
        				}else {
        					inputMsg.setContent("不可重复绑定机器");
        				}
            			returnSuccess=false;
            			autoReply=false;
            			break; 
        			}
           			if(inputMsg.getContent().equals("绑定武汉")) {
        				if(customer==null) {
        					inputMsg.setContent("请先点击商城注册");
        				}else if(tblCustomerService.updateCusVmCode(customer.getId(),"1988001590")) {
        					inputMsg.setContent("绑定成功");
        				}else {
        					inputMsg.setContent("不可重复绑定机器");
        				}
            			returnSuccess=false;
            			autoReply=false;
            			break; 
        			}
        		}
        	}
        	
//			if(inputMsg.getContent().equals("80")) {
//				TblCustomerBean customer = userService.getCustomer(PayTypeEnum.WEIXIN.getIndex(), inputMsg.getFromUserName());
//	            log.info("customer" + customer.getId()); 
//				if(tblCustomerService.updateCusVmCode(customer.getId(),"1988000080")) {
//					 inputMsg.setContent("绑定成功");
//				}else {
//					 inputMsg.setContent("不可重复绑定机器");
//				}
//			}else if(inputMsg.getContent().equals("邀新")) {
//				isPic=true;
//			    ArticleCount	是	图文消息个数，限制为8条以内
//			    Articles	是	多条图文消息信息，默认第一个item为大图,注意，如果图文数超过8，则将会无响应
//			    Title	是	图文消息标题
//			    Description	是	图文消息描述
//			    PicUrl	是	图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
//			    Url	是	点击图文消息跳转链接
//		        str.append("<MsgType><![CDATA[news]]></MsgType>");  
//			    str.append(" <ArticleCount>1</ArticleCount>");
//			    str.append(" <Articles>");
//			    str.append(" <item>");
//			    str.append(" <Title><![CDATA[“优水”新一轮强力降水福利来啦！]]></Title> ");
//			    str.append(" <Description><![CDATA[优水给你发的福利已到账，请查收]]></Description>");
//			    str.append(" <PicUrl><![CDATA[http://mmbiz.qpic.cn/mmbiz_jpg/yIolvpYFIaBp8UUW9iaF8HDicmscx17ibYPUMTK1IlGbeoQD8UCv27ksCCAosAcHDz4FtUibeXGvx5tdDZZNK4TCCg/0?wx_fmt=jpeg]]></PicUrl>");
//			    str.append(" <Url><![CDATA[http://mp.weixin.qq.com/s?__biz=MzU0NzQ4MTY0Mg==&mid=100000151&idx=1&sn=14c50dc46ee9b8205000f5e95a5f9027&chksm=7b4cf0d24c3b79c42f1ac1e3b4abda3b22815e8a0649297c64ccba552fa154a60b2478919e4d#rd]]></Url> ");
//			    str.append(" </item>");
//			    str.append(" </Articles>");
//			    str.append(" </xml> ");		
//			}else if(inputMsg.getContent().equals("如何购买")){
//				inputMsg.setContent("1.用打开微信/支付宝扫一扫优水盒子机身二维码；\r\n" + 
//						"2.按流程注册优水账号及授权微信支付功能；\r\n" + 
//						"3.手机开门自助提水，完成提水后关门系统自动结算扣款；");
//			}else if(inputMsg.getContent().contains("再看一遍")){
//            	inputMsg.setContent("感谢您关注“优水到家”\r\n"+
//            			"请您点击领取您的<a href=\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxa41aef1ebf72a4b2&redirect_uri=http://yms.youshuidaojia.com/admin/getShopToken1&response_type=code&scope=snsapi_userinfo&state=/cMain/coupon?vm=1\">100元购水优惠券</a>\r\n" + 
//            			"欢迎到最近的优水智提水盒买水\r\n" + 
//            			"详细购买流程请看 <a href=\"https://mp.weixin.qq.com/s?__biz=MzU0NzQ4MTY0Mg==&mid=100000098&idx=1&sn=aede46ac6a690c74888259b0bc795653&scene=19#wechat_redirect\">图文教程</a>" + 
//            			"");
//			}else {
//				inputMsg.setContent("小优已经收到了您的消息，会在工作时间内为你及时处理。\r\n" + 
//						"方便的话，可以留下您的微信号，我们会主动联系您。\r\n" + 
//						"如有更多疑问可以拨打热线电话：400-885-8203\r\n" + 
//						"OVER OVER!");
//			}
        }else if(msgType.equals(MsgType.Image.toString())) {
        	for(EventBean eb:eventList) {
        		if(eb.getType().equals("autoReply")) {
        			if(eb.getCode().equals("自动回复"))  {
            			if(eb.getIsPic().equals("1")) {
            				isPic=true;
            				str.append(eb.getContent());
            			}else {
                			inputMsg.setContent(eb.getContent());
            			}
            			break; 
        			}
        		}
        	}
        }
        
        if(!isPic) {
        	//log.info(inputMsg.getContent());
            str.append("<MsgType><![CDATA[text]]></MsgType>");  
        	str.append("<Content><![CDATA[" + inputMsg.getContent() + "]]></Content>"); 
        	str.append("</xml>");  
        }
        if(returnSuccess) {//不回复任何内容
        	str.replace(0, str.length(), "success");
        }
        response.getWriter().write(str.toString());
    }  
        // 获取并返回多图片消息  
//        if (msgType.equals(MsgType.Image.toString())) {  
//            log.info("获取多媒体信息");  
//            log.info("多媒体文件id：" + inputMsg.getMediaId());  
//            log.info("图片链接：" + inputMsg.getPicUrl());  
//            log.info("消息id，64位整型：" + inputMsg.getMsgId());  
//            OutputMessage outputMsg = new OutputMessage();  
//            outputMsg.setFromUserName(servername);  
//            outputMsg.setToUserName(custermname);  
//            outputMsg.setCreateTime(returnTime);  
//            outputMsg.setMsgType(msgType);  
//            ImageMessage images = new ImageMessage();  
//            images.setMediaId(inputMsg.getMediaId());  
//            outputMsg.setImage(images);  
//            log.info("xml转换：/n" + xs.toXML(outputMsg));  
//            response.getWriter().write(xs.toXML(outputMsg));  
//  
//        }  
    
    /** 
     * 创建永久二维码(字符串)
     * @param sceneStr 场景值 
     * @param accessToken 
     * @return 
     */ 
//    public void  createForeverStrQr() {
//    	String sceneStr="80";
//    	String accessToken="14_uQtM9MrfJQIyi_3s-h301ypuuueHO0QWG9DokJz4FyWm6Kwkf1HrwTHTtlIi6SRaW1zbTculD7ytEtAgII1GfrHlP8blPPQ0ziQPwwOnOy3ZnxJ4axP-MP-rfI3nMGCVWMaCPlpvDcTvBwLwNFAfACABTO";
//        RestTemplate rest = new RestTemplate();
//        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accessToken ;
//        // 参数：{"action_name": "QR_LIMIT_STR_SCENE", "action_info": {"scene": {"scene_str": sceneStr}}}
//        Map<String,Object> param = new HashMap<>();
//        param.put("action_name", "QR_LIMIT_STR_SCENE");
//        Map<String,Object> action = new HashMap<>();
//        Map<String,Object> scene = new HashMap<>();
//        scene.put("scene_str", sceneStr);
//        action.put("scene", scene);
//        param.put("action_info", action);
//        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
//        HttpEntity requestEntity = new HttpEntity(param, headers);
//        Map result = null;
//        try {
//            ResponseEntity<Map> entity = rest.exchange(url, HttpMethod.POST, requestEntity,Map.class, new Object[0]);
//            log.info("调用生成微信永久二维码URL接口返回结果:" + entity.getBody());
//            result = (Map) entity.getBody();
//            System.out.println(result);
//        } catch (Exception e) {
//            log.error("调用生成微信永久二维码URL接口异常",e);
//        }
//        String ticket=(String) result.get("ticket");
//        String url2 = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket ;
//        ResponseEntity<Map> entity = rest.getForEntity(url2, Map.class);
//        result = (Map) entity.getBody();
//        System.out.println(result);
//        //HTTP GET请求（请使用https协议）
//        //https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET
//        return result;
//    }


    public void  getSubUserList() {
		MyWXGzhConfig myWXGzhConfig = gzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String accessToken=wxTicketService.getAccessToken(myWXGzhConfig);
		String content="优水欢迎你";
        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token="+accessToken; 
		String jsonResult = HttpUtil.get(url);
		Map<String,Object> result = JsonUtil.toObject(jsonResult, new TypeReference<Map<String,Object>>(){});
		log.info("用户信息"+result);
		Map data=(Map) result.get("data");
		List<String> openIdList=(List<String>) data.get("openid");
		List<TblCustomerBean> oldList=tblCustomerService.getCustomerByCompanyId(null);

		log.info("已关注用户:"+openIdList.size());
		log.info("优水微信总用户数量:"+oldList.size());
		for(TblCustomerBean tcb:oldList) {
			if(tcb.getPhone()!=null) {
				if(tcb.getOpenId()==null) {//支付宝用户
			        //SmsClient.sendMessage("18819488238", content);
				}
				else if (openIdList.contains(tcb.getOpenId())) {
					//log.info("已经关注公众号的微信用户更新状态");
        			tcb.setFollow(1);
    				customerDao.update(tcb);
				}
			}
		}
//		List<String> list=Lists.newArrayList();
//		for(TblCustomerBean tcb:oldList) {
//			if(tcb.getOpenId()!=null) {
//				list.add(tcb.getOpenId());
//			}
//		}
//		int i=0;
//		for(String tcb:openIdList) {
//				if (list.contains(tcb)) {
//				}else {
//					i++;
//					log.info("关注公众号未注册的微信用户"+tcb);//目前772名
//				}
//			
//		}
//    	log.info("---"+i);
    }

    
//    public void followUserPerDay() {
//		MyWXGzhConfig myWXGzhConfig = gzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
//		String accessToken=wxTicketService.getAccessToken(myWXGzhConfig);
//		Map<String, Object> map = new HashMap<String, Object>();
//        String url = "https://api.weixin.qq.com/datacube/getusersummary?access_token="+accessToken; 
//        UserSummary u=new UserSummary();
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE,-1);
//        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 
//        calendar.get(Calendar.DATE), 0, 0, 0);
//        Date startDate = calendar.getTime();
//        u.setBegin_date(startDate);
//        u.setEnd_date(startDate);
//		String jsonResult = HttpUtil.post(url,JsonUtils.toJson(u));
//		if (StringUtil.isNotBlank(jsonResult)) {
//			map = JsonUtils.toObject(jsonResult, new TypeReference<Map<String, Object>>() {
//			});
//		}
//		List<FollowUserSummary> followList=(List<FollowUserSummary>) map.get("list");
//		Integer sumNewUser=0;
//		Integer sumCancelUser=0;
//		for(FollowUserSummary f:followList) {
//			sumNewUser=sumNewUser+f.getNew_user();
//			sumCancelUser=sumCancelUser+f.getCancel_user();
//		}
//		
//		
//    }
    
    //获取accessToken
	@GetMapping(value = "/nowAccessToken", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResultBean getAccessToken() {
		MyWXGzhConfig myWXGzhConfig = gzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String accessToken=wxTicketService.getAccessToken(myWXGzhConfig);
		ResultBean<String> result = new ResultBean<>();
		result.setData(accessToken);
		return result;
	}
	
	//更新事件内容
	@GetMapping(value = "/updateEvent", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResultBean updateEvent(EventBean eventBean) {
		ResultBean<String> result = new ResultBean<>();
		boolean updateFlag=eventDao.updateEvent(eventBean);
		if(updateFlag) {
	        String eventConfigVersion = redisService.getString("eventConfigVersion");
	        String newEventConfigVersion = String.valueOf(Integer.valueOf(eventConfigVersion)+1);
	        redisService.setString("eventConfigVersion",newEventConfigVersion);
			result.setData("成功");
		}
		return result;
	}
	
	//删除事件
	@GetMapping(value = "/delEvent", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResultBean delEvent(EventBean eventBean) {
		ResultBean<String> result = new ResultBean<>();
		boolean insertFlag=eventDao.delEvent(eventBean);
		if(insertFlag) {
	        String eventConfigVersion = redisService.getString("eventConfigVersion");
	        String newEventConfigVersion = String.valueOf(Integer.valueOf(eventConfigVersion)+1);
	        redisService.setString("eventConfigVersion",newEventConfigVersion);
			result.setData("成功");
		}
		return result;
	}
	
	//公众号菜单更新
	@PostMapping(value = "/updateMenu", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResultBean updateMenu(@RequestBody String param) {
		ResultBean<String> result = new ResultBean<>();
		MyWXGzhConfig myWXGzhConfig = gzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		Map<String, Object> map = new HashMap<String, Object>();
		String accessToken=wxTicketService.getAccessToken(myWXGzhConfig);
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken; 
		String jsonResult = HttpUtil.post(url,param);
		if (StringUtil.isNotBlank(jsonResult)) {
			map = JsonUtils.toObject(jsonResult, new TypeReference<Map<String, Object>>() {
			});
		}
		transactionTemplate.execute(new TransactionCallback<Object>() {

			@Override
			public Object doInTransaction(TransactionStatus status) {
				// TODO Auto-generated method stub
				return null;
			}
			
		});
		result.setData(JSON.toJSONString(map));
		return result;

	}
}  
	

