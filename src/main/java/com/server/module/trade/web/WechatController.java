package com.server.module.trade.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.server.jwt.TokenAuthenticationService;
import com.server.util.stateEnum.PayTypeEnum;
import com.server.util.stateEnum.RegisterFlowEnum;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.common.collect.Maps;
import com.server.company.MachinesService;
import com.server.module.activeDegree.RegisterFlowBean;
import com.server.module.activeDegree.UserActiveDegreeService;
import com.server.module.coupon.CouponBean;
import com.server.module.coupon.CouponCustomerBean;
import com.server.module.coupon.CouponForm;
import com.server.module.coupon.CouponService;
import com.server.module.coupon.MachinesLAC;
import com.server.module.footmark.FootmarkBean;
import com.server.module.footmark.FootmarkService;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.auth.controller.dto.AuthResponse;
import com.server.module.trade.auth.util.JwtTokenUtil;
import com.server.module.trade.config.gzh.GzhParam;
import com.server.module.trade.config.gzh.MyWXGzhConfig;
import com.server.module.trade.config.gzh.WXAddressConstant;
import com.server.module.trade.config.gzh.WXGzhConfigFactory;
import com.server.module.trade.config.gzh.WechatUser;
import com.server.module.trade.config.gzh.WxTicketService;
import com.server.module.trade.config.pay.MerchantInfo;
import com.server.module.trade.config.pay.MyWXPayConfig;
import com.server.module.trade.config.pay.WxPayConfigFactory;
import com.server.module.trade.constant.ReturnConstant;
import com.server.module.trade.customer.TblCustomerBean;
import com.server.module.trade.customer.TblCustomerDao;
import com.server.module.trade.customer.wxcustomer.WxCustomerBean;
import com.server.module.trade.customer.wxcustomer.WxCustomerService;
import com.server.module.trade.exception.MyException;
import com.server.module.trade.order.OrderService;
import com.server.module.trade.util.ResultBean;
import com.server.module.trade.web.service.SmsService;
import com.server.module.trade.web.service.UserService;
import com.server.module.zfb_trade.module.company.AliCompanyService;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.redis.RedisService;
import com.server.util.HttpUtil;
import com.server.util.JsonUtils;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CompanyEnum;
import com.server.util.stateEnum.CouponEnum;
import com.server.util.stateEnum.ModeEnum;

@Controller
@RequestMapping(value = "/wechat")
public class WechatController {
	
	private final static Logger log = LogManager.getLogger(WechatController.class);

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private WXGzhConfigFactory gzhConfigFactory;
	
	@Autowired
	private UserService userServiceImpl;

	@Autowired
	private RedisService redisService;

	@Autowired
	private SmsService smsService;

	@Autowired
	private OrderService orderService;
	@Value("${wechat.recallUrl}")
	private String url;

	@Value("${wechat.pay.frontHost}")
	private String frontHost;

	@Value("${wechat.pay.entrustNotifyUrl}")
	private String entrustNotifyUrl;
	
	@Value("${wechat.appHost}")
	private String appHost;

	@Autowired
	private MachinesService machinesService;
	@Autowired
	private WXGzhConfigFactory wxGzhConfigFactory;
	@Autowired
	private WxPayConfigFactory wxPayConfigFactory;
	@Autowired
	private CouponService couponService;
	@Autowired
	private WxTicketService wxTicketService;
	@Autowired
	private WxCustomerService wxCustomerService;
	@Autowired
	private AliCompanyService companyService;
	@Autowired
	private FootmarkService footmarkService;
	@Autowired
	private UserService userService;
	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;
	@Autowired
	private TblCustomerDao tblCustomerDao;
	@Autowired
	private UserActiveDegreeService userActiveDegreeService;


	/**
	 * 获取微信授权地址，返回前端
	 * 
	 * @author yjr
	 * @return
	 */
	@RequestMapping("/oauth2")
	@ResponseBody
	public ResultBean<String> oauth2Url(String vmCode) {
		Integer companyId = machinesService.getCompanyIdByVmCode(vmCode);
		MyWXGzhConfig myWXGzhConfig = wxGzhConfigFactory.getMyWXGzhConfig(companyId);
		String uri = String.format(WXAddressConstant.OAUTH2_CODE, myWXGzhConfig.getAppId(), url, companyId);
		return new ResultBean<String>(uri);
	}

	/**
	 * 直接登录
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping("/getAccessToken")
	// @ResponseBody
	public void getAccessToken(String code, String state, HttpServletResponse response) {
		//state===/main?vmCode=1996000007-/main?vmCode=1996000007-76
		//state===/main?vmCode=1996000007-/main?vmCode=1996000007-76-vision
		
		//state===/register?vmCode=1988000080-/main?vmCode=1988000080-vision-76
		//state===/register?vmCode=1988000080-vision-/main?vmCode=1988000080-76
		log.info("state===" + state);
		String[] urls = state.split("-");
		MyWXGzhConfig myWXGzhConfig = null;
		if(state.contains("vision")) {
			myWXGzhConfig = wxGzhConfigFactory.getMyWXGzhConfig(Integer.valueOf(urls[3]));
		}else {
			myWXGzhConfig = wxGzhConfigFactory.getMyWXGzhConfig(Integer.valueOf(urls[2]));
		}
		
		String format = String.format(WXAddressConstant.OAUTH2_ACCESS_TOKEN, myWXGzhConfig.getAppId(),
				myWXGzhConfig.getSecret(), code);
		String jsonResult = HttpUtil.get(format);
		GzhParam object = JsonUtil.toObject(jsonResult, new TypeReference<GzhParam>() {
		});
		String openId = object.getOpenid();
		Integer type = 1;
		// 检查是否可以登录
		TblCustomerBean customer = userServiceImpl.getCustomer(type, openId);

		if (customer != null && customer.getPhone()!=null) {// 登录
			try {
				if(StringUtil.isBlank(customer.getUnionId())) {
					WxCustomerBean wxCustomerBean = new WxCustomerBean();
					wxCustomerBean.setUnionid(object.getUnionid());
					wxCustomerBean.setOpenId(object.getOpenid());
					wxCustomerService.updateUnionid(wxCustomerBean);
				}
			} catch (Exception e) {
				log.info("unionId有问题"+e);
			}
			
			final String randomKey = jwtTokenUtil.getRandomKey();
			UserVo userVo = new UserVo();
			userVo.setId(customer.getId());
			userVo.setOpenId(openId);
			userVo.setType(1);
			userVo.setPayType(1L);
			String vmCode = urls[0].substring(urls[0].lastIndexOf("=")+1, urls[0].length());
			FootmarkBean footmark = new FootmarkBean(userVo,ModeEnum.LOGIN_MODE,vmCode);
			Long insertFootmark = footmarkService.insertFootmark(footmark);
			redisService.set("EnterId"+userVo.getOpenId(), insertFootmark.toString(), 60*60);
			final String token = jwtTokenUtil.generateToken(JSON.toJSONString(userVo), randomKey);
			// return new ResultBean<AuthResponse>(new AuthResponse(token,
			// randomKey));
			Cookie cookie = new Cookie("token", token);
			cookie.setDomain("youshuidaojia.com");
			cookie.setPath("/");
			response.addCookie(cookie);
			Cookie cookie1 = new Cookie("randomKey", randomKey);
			// cookie1.setDomain("localhost");
			cookie1.setPath("/");
			response.addCookie(cookie1);
			try {
				log.info(frontHost + urls[1] + "&token=" + token + "&randomKey=" + randomKey + "&openId=" + openId);
				if(state.contains("vision")) {
					response.sendRedirect("http://sms.youshuidaojia.com:8301" + urls[2] + "&token=" + token + "&randomKey=" + randomKey + "&openId=" + openId);
				}else {
					response.sendRedirect(frontHost + urls[1] + "&token=" + token + "&randomKey=" + randomKey + "&openId=" + openId);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {// 去注册
			try {
				Cookie cookie = new Cookie("openId", openId);
				cookie.setDomain("youshuidaojia.com");
				cookie.setPath("/");
				response.addCookie(cookie);
				// String smsToken=UUID.randomUUID()
				log.info(frontHost + urls[0] + "&openId=" + openId);
				if(state.contains("vision")) {
					response.sendRedirect("http://sms.youshuidaojia.com:8301"+ urls[0] + "&openId=" + openId);
				}else {
					response.sendRedirect(frontHost + urls[0] + "&openId=" + openId);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	
	@RequestMapping("/getCustomerToken")
	public void getCustomerToken(String code, String state,HttpServletResponse response) {
		log.info("state===" + state);
		MyWXGzhConfig myWXGzhConfig = wxGzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String format = String.format(WXAddressConstant.OAUTH2_ACCESS_TOKEN,
				myWXGzhConfig.getAppId(),myWXGzhConfig.getSecret(),code);
		String jsonResult = HttpUtil.get(format);
		GzhParam object = JsonUtil.toObject(jsonResult, new TypeReference<GzhParam>(){});
		String openId = object.getOpenid();
		String token = "";
		TblCustomerBean customer = userServiceImpl.getCustomer(1, openId);
		if(customer!=null){
			final String randomKey = jwtTokenUtil.getRandomKey();
			UserVo userVo = new UserVo();
			userVo.setId(customer.getId());
			userVo.setOpenId(openId);
			userVo.setPayType(UserVo.Pay_Type_WeiXin);
			userVo.setType(UserVo.USER_CUSTOMER);
			token = jwtTokenUtil.generateToken(JSON.toJSONString(userVo), randomKey);
		}
		try {
			if(state.contains("?")){
				response.sendRedirect(frontHost + state + "&token=" + token+"&payType=1");
			}else{
				response.sendRedirect(frontHost + state + "?token=" + token+"&payType=1");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 注册用户
	 * 
	 * @author yjr
	 * @param openId
	 * @param phone
	 * @param smsCode
	 * @param vmCode
	 * @return
	 * @throws MyException
	 */
	@RequestMapping("/register")
	@ResponseBody
	public ResultBean<?> register(String openId, String phone, String smsCode, String vmCode ,String inviteId) throws MyException {
		boolean b = smsService.checkCode(phone, smsCode);
		//log.info("register===b=" + b);
		if (!b) {
			ResultBean<?> result = new ResultBean<Boolean>();
			result.setCode(ResultBean.UNKNOWN_EXCEPTION);
			result.setMsg("验证码不对或者已过期");
			return result;
		}
		if (StringUtil.isBlank(openId)) {
			ResultBean<?> result = new ResultBean<Boolean>();
			result.setCode(ResultBean.UNKNOWN_EXCEPTION);
			result.setMsg("参数传递错误");
			return result;
		}
		Integer type = 1;
		log.info("register===openId=" + openId + " phone==" + phone + " smsCode=" + smsCode);
		
		MyWXGzhConfig myWXGzhConfig = gzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String accessToken=wxTicketService.getAccessToken(myWXGzhConfig);
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+openId; 
		String jsonResult = HttpUtil.get(url);
		WechatUser wechatUser = JsonUtil.toObject(jsonResult, new TypeReference<WechatUser>(){});
		//log.info("jsonResult"+jsonResult+"用户者信息"+wechatUser);
		Integer follow=1;
		if(wechatUser.getSubscribe()!=null) {
			follow=wechatUser.getSubscribe();
		}else {
			log.info("异常access_token默认用户已关注"+openId);
		}
		
		Map<String, Object> register = userServiceImpl.register(type, openId, phone, smsCode, vmCode,inviteId,wechatUser);
		Long customerId = (Long)register.get("customerId");
		Boolean isInsert = (Boolean)register.get("isInsert");
		Integer registerCoupon = 0;
		if (customerId > 0) {// 注册成功 做登录
			if(isInsert){
				RegisterFlowBean registerBean = new RegisterFlowBean(customerId,vmCode,(int)UserVo.Pay_Type_WeiXin);
				userActiveDegreeService.save(registerBean);
				MachinesLAC machinesLAC = couponService.getMachinesLAC(vmCode);
				CouponForm couponForm = new CouponForm(machinesLAC, CouponEnum.REGISTER_COUPON.getState(), null);
				List<CouponBean> presentCoupon = couponService.getPresentCoupon(couponForm);
				if (presentCoupon != null && presentCoupon.size() > 0) {
					List<CouponBean> collect = presentCoupon.stream().filter(coupon -> !couponService.isReceive(customerId, coupon.getId())).collect(Collectors.toList());
					List<Integer> sonCompanyList = companyService.findAllSonCompanyId(CompanyEnum.WUHAN_YOUSHUI.getCompanyId());
					List<Integer> yaShengHuoList = companyService.findAllSonCompanyId(CompanyEnum.YASHENGHUO.getCompanyId());
					if(collect.size()>0 && !sonCompanyList.contains(machinesLAC.getCompanyId())){
						if(yaShengHuoList.contains(machinesLAC.getCompanyId())){
							registerCoupon = 2;
						}else{
							registerCoupon = 1;
						}
					}
					collect.forEach(coupon -> {
								CouponCustomerBean couCusBean = new CouponCustomerBean();
								couCusBean.setQuantity(coupon.getSendMax());
								couCusBean.setCouponId(coupon.getId());
								couCusBean.setCustomerId(customerId);
								couCusBean.setStartTime(coupon.getLogicStartTime());
								couCusBean.setEndTime(coupon.getLogicEndTime());
								couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());// 状态：0：新建，1：领取，2：已使用
								couponService.insertCouponCustomer(couCusBean);
							});
				}

				if(follow==1) {
					userActiveDegreeService.update(customerId, RegisterFlowEnum.ATTENTION);
					CouponForm couponForm1 = new CouponForm();
					couponForm1.setCompanyId(CompanyEnum.YOUSHUI.getCompanyId());
					couponForm1.setWay(CouponEnum.FOLLOW_COUPON.getState());
					couponForm1.setLimitRange(true);
					List<CouponBean> presentCoupon1 = couponService.getPresentCoupon(couponForm1);
					if(presentCoupon1 != null && presentCoupon1.size()>0){
						presentCoupon1.stream().
						filter(coupon1 -> !couponService.isReceive(customerId, coupon1.getId())).
						forEach(coupon1 -> {
							CouponCustomerBean couCusBean1 = new CouponCustomerBean();
							couCusBean1.setQuantity(coupon1.getSendMax());
							couCusBean1.setCouponId(coupon1.getId());
							couCusBean1.setCustomerId(customerId);
							couCusBean1.setStartTime(coupon1.getLogicStartTime());
							couCusBean1.setEndTime(coupon1.getLogicEndTime());
							couCusBean1.setState(CouponEnum.RECEIVE_COUPON.getState());//状态：0：新建，1：领取，2：已使用
							couponService.insertCouponCustomer(couCusBean1);
						});
						userActiveDegreeService.update(customerId, RegisterFlowEnum.RECEIVE_PERCENT);
		            	TblCustomerBean customer = userService.getCustomer(PayTypeEnum.WEIXIN.getIndex(),openId);
		            	customer.setIsSend(1);
		            	customer.setFollow(1);
		            	tblCustomerDao.update(customer);
					}
				}
				
			}
			final String randomKey = jwtTokenUtil.getRandomKey();
			UserVo userVo = new UserVo();
			userVo.setId(customerId);
			userVo.setOpenId(openId);
			userVo.setType(1);
			userVo.setPayType(1l);
			final String token = jwtTokenUtil.generateToken(JSON.toJSONString(userVo), randomKey);
			return new ResultBean<AuthResponse>(new AuthResponse(token, randomKey,registerCoupon));
		} else {
			ResultBean<AuthResponse> result = new ResultBean<AuthResponse>();
			result.setCode(ResultBean.UNKNOWN_EXCEPTION);
			return result;
		}
	}

	

	
	@RequestMapping("/loginCheckSend")
	@ResponseBody
	public ResultBean<?> loginCheckSend(String openId,HttpServletRequest request, HttpServletResponse response) {
		Integer type=1;
		ResultBean<?> result = new ResultBean<Boolean>();
		//3 弹框显示二维码
		TblCustomerBean customer = userServiceImpl.getCustomer(type, openId);
		if(customer!=null && customer.getPhone()!=null) {
			if(customer.getIsSend()==0 && customer.getFollow()==0) {
				result.setCode(ResultBean.FAIL);
				return result;
			}
		}
		return result;
	}
	
	@RequestMapping("/checkSend")
	@ResponseBody
	public ResultBean<?> checkSend(String openId,HttpServletRequest request, HttpServletResponse response) {
		Integer type=1;
		ResultBean<?> result = new ResultBean<Boolean>();
		//3 弹框显示二维码 
		//0 已领券  1自动送券
		//-1 用户未登录
		TblCustomerBean customer = userServiceImpl.getCustomer(type, openId);
		if(customer!=null && customer.getPhone()!=null) {
			if(customer.getIsSend()==0 && customer.getFollow()==0) {
				result.setCode(ResultBean.FAIL);
				return result;
			}
			else if(customer.getIsSend()==1) {
				result.setCode(ResultBean.SUCCESS);//已送关注券
				result.setMsg("您已领取100元购水大礼包");
				return result;
			}else if(customer.getFollow()==1){
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
						couCusBean.setQuantity  (coupon.getSendMax());      
						couCusBean.setCouponId(coupon.getId());
						couCusBean.setCustomerId(customer.getId());
						couCusBean.setStartTime(coupon.getLogicStartTime());
						couCusBean.setEndTime(coupon.getLogicEndTime());
						couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());//状态：0：新建，1：领取，2：已使用
						couponService.insertCouponCustomer(couCusBean);
					});	
					userActiveDegreeService.update(customer.getId(), RegisterFlowEnum.RECEIVE_PERCENT);
	    			customer.setIsSend(1);
				} 
				tblCustomerDao.update(customer);
				result.setCode(1);
				result.setMsg("已帮您自动领取100元购水大礼包");
				return result;
			}
		}else {
			result.setCode(ResultBean.FAIL);//允许去往商城注册
			result.setMsg("请先点击商品购买进行登录操作");
		}
		return result;
		
	}
	
	
	
	
	@RequestMapping("/index")
	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("-------index-------");
		return;
	}

	/**
	 * 用户签订委托代扣
	 * 
	 * @author yjr
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/entrustweb")
	@ResponseBody
	public String entrustweb(String vmCode, HttpServletRequest request, HttpServletResponse response) {
		String url = "https://api.mch.weixin.qq.com/papay/entrustweb";
		Map<String, String> map = Maps.newHashMap();
		Integer companyId = machinesService.getCompanyIdByVmCode(vmCode);
		WXPayConfig wxPayConfig = wxPayConfigFactory.getWXPayConfig(companyId);
		map.put("appid", wxPayConfig.getAppID());
		map.put("mch_id", wxPayConfig.getMchID());
		map.put("plan_id", ((MyWXPayConfig) wxPayConfig).getPlanId());
		String requestSerial = genRequestSerial();
		// 协议号跟序列号一样
		map.put("contract_code", requestSerial);// 商户侧的签约协议号，由商户生成
		map.put("request_serial", requestSerial);// 商户请求签约时的序列号
		map.put("contract_display_account", "优水代扣");
		map.put("notify_url", entrustNotifyUrl);
		map.put("return_web", "1");
		map.put("version", "1.0");
		map.put("timestamp", System.currentTimeMillis() / 1000 + "");
		try {
			String urlsign = WXPayUtil.generateSignature(map, wxPayConfig.getKey());
			Set<String> keySet = map.keySet();
			String[] keyArray = keySet.toArray(new String[keySet.size()]);
			Arrays.sort(keyArray);
			StringBuilder sb = new StringBuilder();
			for (String k : keyArray) {
				if (k.equals(WXPayConstants.FIELD_SIGN)) {
					continue;
				}
				if (map.get(k).trim().length() > 0) // 参数值为空，则不参与签名
					sb.append(k).append("=").append(map.get(k).trim()).append("&");
			}
			sb.append("key=").append(wxPayConfig.getKey());
			String urls = sb.toString();
			int c = urls.indexOf("&key=");
			urls = urls.substring(0, c);
			urls = urls + "&sign=" + urlsign;
			url = url + "?" + urls;
			int i = url.indexOf("notify_url=");
			int e = url.indexOf("&", i);
			url = url.substring(0, i) + "notify_url=" + URLEncoder.encode(entrustNotifyUrl) + url.substring(e);
			log.info(url);
			return url;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private synchronized String genRequestSerial() {
		SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmss");
		return sf.format(new Date());
	}

	/**
	 * 委托代扣回调
	 * 
	 * @author yjr
	 * @param xml
	 * @return
	 */
	@RequestMapping("/entrustNotify")
	public void entrustNotify(@RequestBody String xml,HttpServletResponse response) {
		String returnResult = null;
		try {
			Map<String, String> map = WXPayUtil.xmlToMap(xml);
			String result_code = map.get("result_code");
			String mchId = map.get("mch_id");
			if (StringUtil.isBlank(mchId)) {
				returnResult = ReturnConstant.SUCCESS;
				writeMsgToWx(returnResult,response);
				return;
			}
			WXPayConfig wxPayConfig = wxPayConfigFactory.getWXPayConfig(mchId);
			boolean b = WXPayUtil.isSignatureValid(map, wxPayConfig.getKey());
			if (b) {
				if (("SUCCESS").equals(result_code)) {
					Integer companyId = ((MyWXPayConfig) wxPayConfig).getUsingCompanyConfig();
					map.put("companyId", companyId.toString());
					orderService.insertEntrustInfo(map);
					userActiveDegreeService.update(map.get("openid"), RegisterFlowEnum.NO_PASSWORD_PAY);
				}
				returnResult = ReturnConstant.SUCCESS;
			} else {
				returnResult = ReturnConstant.FAIL;
			}
			writeMsgToWx(returnResult,response);
			return;

		} catch (Exception e) {
			e.printStackTrace();

		}
		try {
			returnResult = ReturnConstant.FAIL;
			writeMsgToWx(returnResult,response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * 用户解约委托代扣
	 * 
	 * @author yjr
	 * @param xml
	 * @return
	 */
	@RequestMapping("/jieyue")
	public void jieyue(@RequestBody String xml,HttpServletResponse response) {
		log.info("=====notify=entrust=解约==" + xml);
		String returnResult = null;
		try {
			Map<String,String> map = WXPayUtil.xmlToMap(xml);
//			String result_code = (String) map.get("result_code");
			String mchId = (String) map.get("mch_id");
			if (StringUtil.isBlank(mchId)) {
				returnResult = ReturnConstant.SUCCESS;
				writeMsgToWx(returnResult,response);
				return;
			}
			WXPayConfig wxPayConfig = wxPayConfigFactory.getWXPayConfig(mchId);
			boolean b = WXPayUtil.isSignatureValid(map, wxPayConfig.getKey());
			if (b) {
				Integer companyId = ((MyWXPayConfig) wxPayConfig).getUsingCompanyConfig();
				map.put("companyId", companyId.toString());
				orderService.jieyue(map);
				userActiveDegreeService.update((String)map.get("openid"), RegisterFlowEnum.CANCEL_NO_PASSWORD);
				returnResult = ReturnConstant.SUCCESS;
			} else {
				returnResult = ReturnConstant.FAIL;
			}
			writeMsgToWx(returnResult,response);
			return;

		} catch (Exception e) {
			e.printStackTrace();

		}
		try {
			returnResult = ReturnConstant.FAIL;
			writeMsgToWx(returnResult,response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	private void writeMsgToWx(String msg,HttpServletResponse response){
		try {
			response.reset();
			PrintWriter printWriter = response.getWriter();
			printWriter.write(msg);
			printWriter.flush();
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检测机器是否关门
	 * 
	 * @author yjr
	 * @param vmCode
	 * @return
	 */
	@RequestMapping("/isClosed")
	@ResponseBody
	public ResultBean<Boolean> isMachinesClosed(String vmCode) {
//		UserVo userVo = UserUtil.getUser();
		String factoryNumber = orderService.getFactoryNumberByVmCode(vmCode);
		boolean b = redisService.exists("OHM-"+factoryNumber);
		// HBT  待解决：增加订单结果返回
//		if(b){
//			String status = redisService.getString("orderUserId"+userVo.getId());
//			if("SUCCESS".equals(status)){
//				System.out.println("订单支付成功");
//			} else if("FAIL".equals(status)){
//				System.out.println("订单支付失败");
//			} else {
//				System.out.println("订单处理中");
//			}
//		}
		ResultBean<Boolean> result = new ResultBean<Boolean>();
		if (b) {
			result.setData(new Boolean(false));
		} else {
			result.setData(new Boolean(true));
		}
		return result;
	}
	
	/**
	 * 分享时获取的参数信息
	 * @author hebiting
	 * @date 2018年8月13日下午4:52:10
	 * @return
	 */
	@PostMapping("/shareInfo")
	@ResponseBody
	public ResultBean<Map<String,String>> shareInfo(String url){
		//log.info("分享接口--start");
		ResultBean<Map<String,String>> result = new ResultBean<Map<String,String>>();
		Map<String, String> jspidSignature = wxTicketService.getJspidSignature(url, CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		result.setData(jspidSignature);
		return result;
	}
	
	/**
	 * 获取用户详细信息
	 * @author hebiting
	 * @date 2018年8月13日下午4:52:26
	 * @param code
	 * @param state
	 * @param response
	 * @return
	 */
	@RequestMapping("/getUserInfo")
	@ResponseBody
	public void getUserInfo(String code, String state, HttpServletResponse response){
		log.info("获取用户详细信息state===" + state);
		String[] urls = state.split("-");
		MyWXGzhConfig myWXGzhConfig = wxGzhConfigFactory.getMyWXGzhConfig(Integer.valueOf(urls[2]));
		String format = String.format(WXAddressConstant.OAUTH2_ACCESS_TOKEN, myWXGzhConfig.getAppId(),
				myWXGzhConfig.getSecret(), code);
		String jsonResult = HttpUtil.get(format);
		log.info("授权返回信息:" + jsonResult);
		GzhParam gzhParam = JsonUtil.toObject(jsonResult, new TypeReference<GzhParam>() {
		});
		String userInfoUrl = String.format(WXAddressConstant.OAUTH2_USER_INFO, gzhParam.getAccess_token(),gzhParam.getOpenid());
		String jsonUserInfo = HttpUtil.get(userInfoUrl);
		log.info("getUserInfo Json"+jsonUserInfo);
		WxCustomerBean wxCus = JsonUtil.toObject(jsonUserInfo, new TypeReference<WxCustomerBean>(){});
		wxCus.setOpenId(gzhParam.getOpenid());
		boolean update = wxCustomerService.update(wxCus);
		if(update){
			TblCustomerBean customer = userServiceImpl.getCustomer(PayTypeEnum.WEIXIN.getIndex(),gzhParam.getOpenid());
			String userCompanyId = customer.getId().toString()+",0"+","+gzhParam.getOpenid();
			//生成后台系统的token
			String token = tokenAuthenticationService.generateToken(userCompanyId);
			try {
				log.info("getUserInfo重定向地址："+appHost + urls[0] );
				response.sendRedirect(appHost + urls[0] + "&token=" + token);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@PostMapping("/updatePayConfig")
	@ResponseBody
	public boolean updatePayConfig(@RequestBody MerchantInfo merchantInfo) {
//		boolean result = false;
//		if(merchantInfo==null){
//			result = false;
//		}else{
//			wxPayConfigFactory.updateConfig(merchantInfo);
//			result = true;
//		}
//		return result;
		return true;
	}

	
	//查询支付分开通-调转小程序申请-
	//https://api.mch.weixin.qq.com/payscore/user-service-state?service_id=500001&appid=wxd678efh567hg6787&openid=oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
	@GetMapping("/checkPayScore")
	public ReturnDataUtil checkPayScore(String openid) {
		ReturnDataUtil returnDataUtil= new ReturnDataUtil();
		String url="https://api.mch.weixin.qq.com/payscore/user-service-state?service_id=500001&appid=wxd678efh567hg6787&openid=oUpF8uMuAJO_M2pxb1Q9zNjWeS6o";
		String jsonResult = HttpUtil.get(url);
		//GzhParam object = JsonUtil.toObject(jsonResult, new TypeReference<GzhParam>(){});
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtil.isNotBlank(jsonResult)) {
			map = JsonUtils.toObject(jsonResult, new TypeReference<Map<String, Object>>() {
			});
		}
		String result=(String) map.get("use_service_state");
		if(result.equals("AVAILABLE")) {
			returnDataUtil.setStatus(1);
		}else if(result.equals("UNAVAILABLE")) {
			returnDataUtil.setStatus(0);
		}
/*		service_id string(32) true 调用该接口提交的服务ID 500001
		appid string(32) true 调用接口提交的公众账号ID wxd678efh567hg6787
		openid string(128) true 用户在商户appid下的唯一标识。 oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
		use_service_state*/
		
		return returnDataUtil;
	}
	
	
		
}
