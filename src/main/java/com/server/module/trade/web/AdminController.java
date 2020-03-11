package com.server.module.trade.web;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import com.server.company.MachinesService;
import com.server.module.trade.util.HttpClientUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.server.jwt.TokenAuthenticationService;
import com.server.module.activeDegree.UserActiveDegreeService;
import com.server.module.coupon.CouponBean;
import com.server.module.coupon.CouponCustomerBean;
import com.server.module.coupon.CouponForm;
import com.server.module.coupon.CouponService;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.auth.util.JwtTokenUtil;
import com.server.module.trade.config.gzh.GzhParam;
import com.server.module.trade.config.gzh.MyWXGzhConfig;
import com.server.module.trade.config.gzh.WXAddressConstant;
import com.server.module.trade.config.gzh.WXGzhConfigFactory;
import com.server.module.trade.config.gzh.WechatUser;
import com.server.module.trade.config.gzh.WxTicketService;
import com.server.module.trade.customer.TblCustomerBean;
import com.server.module.trade.customer.TblCustomerDao;
import com.server.module.trade.customer.wxcustomer.WxCustomerBean;
import com.server.module.trade.customer.wxcustomer.WxCustomerService;
import com.server.module.trade.util.ResultBean;
import com.server.module.trade.util.UserUtil;
import com.server.module.trade.web.service.AdminService;
import com.server.module.trade.web.service.SmsService;
import com.server.module.trade.web.service.UserService;
import com.server.module.zfb_trade.module.logininfo.AliLoginInfoService;
import com.server.module.zfb_trade.module.logininfo.LoginInfoBean;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.redis.RedisService;
import com.server.util.EmojiUtil;
import com.server.util.HttpUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CompanyEnum;
import com.server.util.stateEnum.CouponEnum;
import com.server.util.stateEnum.PayTypeEnum;
import com.server.util.stateEnum.RegisterFlowEnum;


@Controller
@RequestMapping(value = "/admin")
public class AdminController {


	private Logger log = LogManager.getLogger(AdminController.class);

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private AdminService adminService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;
	@Autowired
	private UserService userService;
	@Autowired
	private WXGzhConfigFactory wxGzhConfigFactory;
	@Value("${wechat.adminAuthUrl}")
	private String url;
	@Value("${wechat.pay.frontHost}")
	private String frontHost;
	@Value("${wechat.pay.replenishHost}")
	private String replenishHost;
	@Value("${wechat.skipUrl}")
	private String skipUrl;
	@Value("${wechat.appHost}")
	private String appHost;
	@Value("${wechat.appSkipUrl}")
	private String appSkipUrl;
	@Value("${wechat.customerLogin}")
	private String customerLogin;
	@Autowired
	private CouponService couponService;
	@Autowired
	private WXGzhConfigFactory gzhConfigFactory;
	@Autowired
	private WxTicketService wxTicketService;
	@Autowired
	private AliLoginInfoService loginInfoService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private TblCustomerDao tblCustomerDao;
	@Autowired
	private UserActiveDegreeService userActiveDegreeService;
	@Autowired
	private WxCustomerService wxCustomerService;
	@Autowired
	private MachinesService machinesService;


	/**
	 * 用户登录及绑定支付宝或微信
	 *
	 * @param phone
	 * @param smsCode
	 * @param payType
	 * @param openId
	 * @return
	 * @author yjr
	 */
	@RequestMapping("/login")
	@ResponseBody
	public ResultBean<String> login(@RequestParam String phone,
									@RequestParam String smsCode,
									@RequestParam long payType,
									@RequestParam(required = false) String openId) {
		log.info("payType---" + payType);
		ResultBean<String> result = new ResultBean<>();
		boolean b = smsService.checkCode(phone, smsCode);
		if (!b) {
			result.setCode(-99);
			result.setMsg("验证码错误");
			return result;
		}
		List<Map<String, Object>> list = adminService.listByPhone(phone);
		Map<String, Object> map = Maps.newHashMap();
		if (list.size() > 0) {
			map = list.get(0);
		} else {
			result.setCode(-99);
			result.setMsg("没有该用户");
			return result;
		}

		Integer status = ((Integer) map.get("status"));
		if (status != 1) {
			result.setCode(-99);
			result.setMsg("该用户已被禁用");
			return result;
		}
		Long userId = (Long) map.get("id");
		log.info("map" + JsonUtil.toJson(map));
		if (StringUtil.isBlank((String) map.get("aliUserId")) && payType == UserVo.Pay_Type_ZhiFuBao && StringUtil.isNotBlank(openId)) {
			log.info("绑定支付宝");
			if (adminService.update(openId, null, userId)) {
				result.setMsg("已绑定支付宝");
			}
		}

		if (StringUtil.isBlank((String) map.get("openId")) && payType == UserVo.Pay_Type_WeiXin && StringUtil.isNotBlank(openId)) {
			log.info("绑定微信");
			if (adminService.update(null, openId, userId)) {
				result.setMsg("已绑定微信");
			}
		}
		final String randomKey = jwtTokenUtil.getRandomKey();
		UserVo userVo = new UserVo();
		userVo.setId(userId);
		userVo.setOpenId(openId);
		userVo.setPayType(payType);
		userVo.setType(UserVo.USER_ADMIN);
		final String token = jwtTokenUtil.generateToken(JSON.toJSONString(userVo), randomKey);
		result.setData(token);
		return result;
	}


	/**
	 * 后台管理系统用户登录及绑定支付宝或微信
	 *
	 * @param phone
	 * @param smsCode
	 * @param payType
	 * @param openId
	 * @return
	 * @author yjr
	 */
	@RequestMapping("/appLogin")
	@ResponseBody
	public ResultBean<String> appLogin(@RequestParam String phone,
									   @RequestParam String smsCode,
									   @RequestParam long payType,
									   @RequestParam(required = false) String openId) {
		log.info("payType---" + payType);
		ResultBean<String> result = new ResultBean<>();
		boolean b = smsService.checkCode(phone, smsCode);
		if (!b) {
			result.setCode(-99);
			result.setMsg("验证码错误");
			return result;
		}
		List<Map<String, Object>> list = adminService.listByPhone(phone);
		Map<String, Object> map = Maps.newHashMap();
		if (list.size() > 0) {
			map = list.get(0);
		} else {
			result.setCode(-99);
			result.setMsg("没有该用户");
			return result;
		}

		Integer status = ((Integer) map.get("status"));
		if (status != 1) {
			result.setCode(-99);
			result.setMsg("该用户已被禁用");
			return result;
		}
		Long userId = (Long) map.get("id");
		log.info("map" + JsonUtil.toJson(map));
		if (map.get("aliUserId") == null && payType == UserVo.Pay_Type_ZhiFuBao && StringUtil.isNotBlank(openId)) {
			log.info("绑定支付宝");
			if (adminService.update(openId, null, userId)) {
				result.setMsg("已绑定支付宝");
			}
		}

		if (map.get("openId") == null && payType == UserVo.Pay_Type_WeiXin && StringUtil.isNotBlank(openId)) {
			log.info("绑定微信");
			if (adminService.update(null, openId, userId)) {
				result.setMsg("已绑定微信");
			}
		}
		String userCompanyId = map.get("id").toString() + "," + map.get("companyId") + "," + openId;
		//生成后台系统的token
		String token = tokenAuthenticationService.generateToken(userCompanyId);
		result.setData(token);
		return result;
	}


	/**
	 * 获取微信授权地址
	 *
	 * @return
	 * @author yjr
	 */
	@RequestMapping("/oauth2")
	@ResponseBody
	public ResultBean<String> oauth2Url() {
		MyWXGzhConfig myWXGzhConfig = wxGzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String format = String.format(WXAddressConstant.OAUTH2_CODE, myWXGzhConfig.getAppId(), url, null);
//		String url1 = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
		return new ResultBean<String>(format);
	}

	/**
	 * 授权回调，返回openId给前端
	 *
	 * @param code
	 * @param state
	 * @param response
	 * @author yjr
	 */
	@RequestMapping("/getAccessToken")
	public void getAccessToken(String code, String state, HttpServletResponse response) {
		MyWXGzhConfig myWXGzhConfig = wxGzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String format = String.format(WXAddressConstant.OAUTH2_ACCESS_TOKEN,
				myWXGzhConfig.getAppId(), myWXGzhConfig.getSecret(), code);
		String[] urls = state.split("-");
		String jsonResult = HttpUtil.get(format);
		GzhParam object = JsonUtil.toObject(jsonResult, new TypeReference<GzhParam>() {
		});
		String openId = object.getOpenid();
		LoginInfoBean findLoginInfo = loginInfoService.findLoginInfo(openId);
		String token = null;
		if (findLoginInfo != null) {
			final String randomKey = jwtTokenUtil.getRandomKey();
			UserVo userVo = new UserVo();
			userVo.setId(findLoginInfo.getId());
			userVo.setOpenId(openId);
			userVo.setPayType(UserVo.Pay_Type_WeiXin);
			userVo.setType(UserVo.USER_ADMIN);
			token = jwtTokenUtil.generateToken(JSON.toJSONString(userVo), randomKey);
		}
		try {
			if (("mms").equals(redisService.getString("mms"))) {
				if (token != null) {
					response.sendRedirect(replenishHost + urls[1] + "&token=" + token + "&openId=" + openId + "&payType=1");
				} else {
					response.sendRedirect(replenishHost + urls[0] + "&openId=" + openId + "&payType=1");
				}
			} else {
				if (token != null) {
					response.sendRedirect(frontHost + urls[1] + "&token=" + token + "&openId=" + openId + "&payType=1");
				} else {
					response.sendRedirect(frontHost + urls[0] + "&openId=" + openId + "&payType=1");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@RequestMapping("/getCustomerToken")
	public void getCustomerToken(String code, String state, HttpServletResponse response) {
		log.info("state===" + state);
		MyWXGzhConfig myWXGzhConfig = wxGzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String format = String.format(WXAddressConstant.OAUTH2_ACCESS_TOKEN,
				myWXGzhConfig.getAppId(), myWXGzhConfig.getSecret(), code);
		String jsonResult = HttpUtil.get(format);
		GzhParam object = JsonUtil.toObject(jsonResult, new TypeReference<GzhParam>() {
		});
		String openId = object.getOpenid();
		String token = "";
		List<Map<String, Object>> listByOpenId = adminService.listByOpenId(openId);
		if (listByOpenId != null && listByOpenId.size() > 0) {
			Map<String, Object> map = listByOpenId.get(0);
			Long userId = Long.valueOf(map.get("id").toString());
			final String randomKey = jwtTokenUtil.getRandomKey();
			UserVo userVo = new UserVo();
			userVo.setId(userId);
			userVo.setOpenId(openId);
			userVo.setPayType(UserVo.Pay_Type_WeiXin);
			userVo.setType(UserVo.USER_CUSTOMER);
			token = jwtTokenUtil.generateToken(JSON.toJSONString(userVo), randomKey);
		}
		try {
			response.sendRedirect(frontHost + state + "?token=" + token + "&payType=1");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 后台工作人员授权，若未绑定微信，则跳去登录绑定流程，若绑定则直接进入系统
	 *
	 * @param code
	 * @param state
	 * @param response
	 * @author hebiting
	 * @date 2018年6月9日上午9:40:59
	 */
	@RequestMapping("/getOperatorToken")
	public void getOperatorToken(String code, String state, HttpServletResponse response) {
		log.info("state===" + state);
		MyWXGzhConfig myWXGzhConfig = wxGzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String format = String.format(WXAddressConstant.OAUTH2_ACCESS_TOKEN,
				myWXGzhConfig.getAppId(), myWXGzhConfig.getSecret(), code);
		String jsonResult = HttpUtil.get(format);
		GzhParam object = JsonUtil.toObject(jsonResult, new TypeReference<GzhParam>() {
		});
		String openId = object.getOpenid();
		List<Map<String, Object>> listByOpenId = adminService.listByOpenId(openId);
		if (listByOpenId != null && listByOpenId.size() > 0) {
			Map<String, Object> map = listByOpenId.get(0);
			final String randomKey = jwtTokenUtil.getRandomKey();
			UserVo userVo = new UserVo();
			userVo.setId(Long.valueOf(map.get("id").toString()));
			userVo.setOpenId(openId);
			userVo.setType(UserVo.USER_ADMIN);
			userVo.setPayType(UserVo.Pay_Type_WeiXin);
			final String token = jwtTokenUtil.generateToken(JSON.toJSONString(userVo), randomKey);
			try {
				log.info("getOperatorToken重定向地址：" + frontHost + state + "&token=" + token);
				response.sendRedirect(frontHost + state + "&token=" + token);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			log.info("getOperatorToken重定向登录绑定微信");
			try {
				response.sendRedirect(skipUrl + "?payType=1&openId=" + openId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 公众号的后台管理系统授权页面
	 *
	 * @param code
	 * @param state
	 * @param response
	 * @author hebiting
	 * @date 2018年6月9日上午9:41:54
	 */
	@RequestMapping("/getOperatorTokenApp")
	public void getOperatorTokenApp(String code, String state, HttpServletResponse response) {
		log.info("state===" + state);
		MyWXGzhConfig myWXGzhConfig = wxGzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String format = String.format(WXAddressConstant.OAUTH2_ACCESS_TOKEN,
				myWXGzhConfig.getAppId(), myWXGzhConfig.getSecret(), code);
		String jsonResult = HttpUtil.get(format);
		GzhParam object = JsonUtil.toObject(jsonResult, new TypeReference<GzhParam>() {
		});
		String openId = object.getOpenid();
		List<Map<String, Object>> listByOpenId = adminService.listByOpenId(openId);
		if (listByOpenId != null && listByOpenId.size() > 0) {
			Map<String, Object> map = listByOpenId.get(0);
			final String randomKey = jwtTokenUtil.getRandomKey();
			UserVo userVo = new UserVo();
			userVo.setId(Long.valueOf(map.get("id").toString()));
			userVo.setOpenId(openId);
			userVo.setType(UserVo.USER_ADMIN);
			userVo.setPayType(UserVo.Pay_Type_WeiXin);
			final String token = jwtTokenUtil.generateToken(JSON.toJSONString(userVo), randomKey);
			try {
				log.info("getOperatorTokenApp重定向地址：" + appHost + state + "&token=" + token);
				response.sendRedirect(appHost + state + "&token=" + token);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			log.info("getOperatorTokenApp重定向登录绑定微信");
			try {
				response.sendRedirect(appSkipUrl + "?payType=1&openId=" + openId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 公众号的后台管理系统授权页面现在换到admin项目中去，所以token生成admin项目使用的token
	 *
	 * @param code
	 * @param state
	 * @param response
	 * @author hebiting
	 * @date 2018年6月9日上午9:41:54
	 */
	@RequestMapping("/getOperatorTokenAppAdmin")
	public void getOperatorTokenAppAdmin(String code, String state, HttpServletResponse response) {
		log.info("state===" + state);
		MyWXGzhConfig myWXGzhConfig = wxGzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String format = String.format(WXAddressConstant.OAUTH2_ACCESS_TOKEN,
				myWXGzhConfig.getAppId(), myWXGzhConfig.getSecret(), code);
		String jsonResult = HttpUtil.get(format);
		GzhParam object = JsonUtil.toObject(jsonResult, new TypeReference<GzhParam>() {
		});
		String openId = object.getOpenid();
		List<Map<String, Object>> listByOpenId = adminService.listByOpenId(openId);
		if (listByOpenId != null && listByOpenId.size() > 0) {
			Map<String, Object> map = listByOpenId.get(0);
			String userCompanyId = map.get("id").toString() + "," + map.get("companyId") + "," + openId;
			//生成后台系统的token
			String token = tokenAuthenticationService.generateToken(userCompanyId);
			try {
				log.info("getOperatorTokenAppAdmin重定向地址：" + appHost + state + "&token=" + token);
				response.sendRedirect(appHost + state + "&token=" + token);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			log.info("数据库中查询不是后台人员，跳转到官网中去！");
			try {
				response.sendRedirect("http://www.youshuidaojia.com:6661/gw");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping("/getShopToken")
	@ResponseBody
	public Map<String, String> getShopToken(String code, String state, HttpServletResponse response) {
		log.info("state===" + state);
		String type = null;
		if (state.indexOf('-') == -1) {
			type = "1";
		} else {
			type = state.substring(state.indexOf('-') + 1, state.length());
			state = state.substring(0, state.indexOf('-'));
		}
		Map<String, String> m = new HashMap<String, String>();
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		MyWXGzhConfig myWXGzhConfig = wxGzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String format = String.format(WXAddressConstant.OAUTH2_ACCESS_TOKEN,
				myWXGzhConfig.getAppId(), myWXGzhConfig.getSecret(), code);
		String jsonResult = HttpUtil.get(format);
		GzhParam object = JsonUtil.toObject(jsonResult, new TypeReference<GzhParam>() {
		});
		String openId = object.getOpenid();
		TblCustomerBean customer = userService.getCustomer(PayTypeEnum.WEIXIN.getIndex(), openId);
		if (customer == null) {
			log.info("新用户注册");
			m.put("url", "http://webapp.youshuidaojia.com/cLogin?openId=" + openId + "&type=" + type);
		} else if (customer != null && customer.getPhone() == null) {
			m.put("url", "http://webapp.youshuidaojia.com/cLogin?openId=" + openId + "&type=" + type);
			log.info("用户存在无手机号");
		}
		//生成后台系统的token
		if (customer != null && customer.getPhone() != null) {
			String userCompanyId = customer.getId() + ",0" + "," + openId;
			String token = tokenAuthenticationService.generateToken(userCompanyId);
			m.put("token", token);
			try {
				response.sendRedirect(appHost + state + "&token=" + token);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			returnDataUtil.setStatus(2);
		}
		returnDataUtil.setReturnObject(m);
		return m;
	}

	@RequestMapping("/getShopToken2")
	public void getShopToken2(String code, String state, HttpServletResponse response) {
		log.info("state===" + state);
		String type = null;
		if (state.indexOf('-') == -1) {
			type = "1";
		} else {
			type = state.substring(state.indexOf('-') + 1, state.length());
			state = state.substring(0, state.indexOf('-'));
		}
		log.info("state===" + state);
		MyWXGzhConfig myWXGzhConfig = wxGzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String format = String.format(WXAddressConstant.OAUTH2_ACCESS_TOKEN,
				myWXGzhConfig.getAppId(), myWXGzhConfig.getSecret(), code);
		String jsonResult = HttpUtil.get(format);
		GzhParam object = JsonUtil.toObject(jsonResult, new TypeReference<GzhParam>() {
		});
		String openId = object.getOpenid();
		TblCustomerBean customer = userService.getCustomer(PayTypeEnum.WEIXIN.getIndex(), openId);
		if (customer != null) {
			updateCustomerUnionId(customer, object);
			String userCompanyId = customer.getId().toString() + ",0" + "," + openId;
			//生成后台系统的token
			String token = tokenAuthenticationService.generateToken(userCompanyId);
			try {
				log.info("getOperatorTokenAppAdmin重定向地址：" + appHost + state);
				response.sendRedirect(appHost + state + "&token=" + token);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			log.info("商城用户未注册，则进行注册");
			try {
				response.sendRedirect(customerLogin + "?openId=" + openId + "&type=" + type);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 商城系统登录入口(我的优惠券)
	 *
	 * @param code
	 * @param state
	 * @param response
	 * @author hebiting
	 * @date 2018年6月9日上午9:41:54
	 */
	@RequestMapping("/getShopToken1")
	public void getShopToken1(String code, String state, HttpServletResponse response) {
		//state=/cMain/coupon?vm=1-2
		String type = null;
		if (state.indexOf('-') == -1) {
			type = "1";
		} else {
			type = state.substring(state.indexOf('-') + 1, state.length());
			state = state.substring(0, state.indexOf('-'));
		}
		MyWXGzhConfig myWXGzhConfig = wxGzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String format = String.format(WXAddressConstant.OAUTH2_ACCESS_TOKEN,
				myWXGzhConfig.getAppId(), myWXGzhConfig.getSecret(), code);
		String jsonResult = HttpUtil.get(format);
		GzhParam object = JsonUtil.toObject(jsonResult, new TypeReference<GzhParam>() {
		});
		String openId = object.getOpenid();
		TblCustomerBean customer = userService.getCustomer(PayTypeEnum.WEIXIN.getIndex(), openId);
		if (customer == null) {
			log.info("新用户注册");
			try {
				response.sendRedirect(customerLogin + "?openId=" + openId + "&type=" + type);
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (customer != null && customer.getPhone() == null) {
			log.info("用户存在无手机号");
			try {
				response.sendRedirect(customerLogin + "?openId=" + openId + "&type=" + type);
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (customer != null && customer.getPhone() != null) {
			//已注册微信用户    关注返券        	
			if (customer.getFollow() == 1 && customer.getIsSend() == 0) {
				CouponForm couponForm = new CouponForm();
				couponForm.setCompanyId(CompanyEnum.YOUSHUI.getCompanyId());
				couponForm.setWay(CouponEnum.FOLLOW_COUPON.getState());
				couponForm.setLimitRange(true);
				Long customerId = customer.getId();
				List<CouponBean> presentCoupon = couponService.getPresentCoupon(couponForm);
				if (presentCoupon != null && presentCoupon.size() > 0) {
					presentCoupon.stream().
							filter(coupon -> !couponService.isReceive(customerId, coupon.getId())).
							forEach(coupon -> {
								CouponCustomerBean couCusBean = new CouponCustomerBean();
								couCusBean.setQuantity(coupon.getSendMax());
								couCusBean.setCouponId(coupon.getId());
								couCusBean.setCustomerId(customerId);
								couCusBean.setStartTime(coupon.getLogicStartTime());
								couCusBean.setEndTime(coupon.getLogicEndTime());
								couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());//状态：0：新建，1：领取，2：已使用
								couponService.insertCouponCustomer(couCusBean);
							});
					customer.setIsSend(1);
					tblCustomerDao.update(customer);
				}
			}
		}
		//生成后台系统的token
		if (customer != null && customer.getPhone() != null) {
			String userCompanyId = customer.getId() + ",0" + "," + openId;
			String token = tokenAuthenticationService.generateToken(userCompanyId);
			try {
				response.sendRedirect(appHost + state + "&token=" + token);
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 商城用户绑定手机号
	 *
	 * @param param
	 * @return
	 * @author hebiting
	 */
	@PostMapping("/shopRegister")
	@ResponseBody
	public ResultBean<String> shopRegister(@RequestBody Map<String, String> param) {
		String phone = param.get("phone");
		String smsCode = param.get("smsCode");
		String openId = param.get("openId");
		String cityId = param.get("cityId");
		String vmCode = param.get("vmCode");
		ResultBean<String> result = new ResultBean<>();
		Integer c = machinesService.getCompanyIdByVmCode(vmCode);
		if (c != null) {
			//广州
			if (Integer.parseInt(cityId) == 1) {
				vmCode = param.get("vmCode");
			}
			//珠海
			if (Integer.parseInt(cityId) == 2) {
				vmCode = "1999000111";
				//System.out.println("===================================================="+vmCode);
			}
			//四川
			if (Integer.parseInt(cityId) == 3) {
				vmCode = "1988001672";
			}
	        if (Integer.parseInt(cityId)==4){
	            if(StringUtils.isBlank(vmCode)){
	                vmCode="1988000080";
	            }
	        }
		} else {
			result.setCode(-99);
			result.setMsg("机器编码错误！！！");
			return result;
		}
		boolean b = smsService.checkCode(phone, smsCode);
		if (!b) {
			result.setCode(-99);
			result.setMsg("验证码错误");
			return result;
		}
		if (StringUtil.isBlank(openId)) {
			result.setCode(-99);
			result.setMsg("参数传递错误");
			return result;
		}
		MyWXGzhConfig myWXGzhConfig = gzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String accessToken = wxTicketService.getAccessToken(myWXGzhConfig);
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openId;
		String jsonResult = HttpUtil.get(url);
		WechatUser wechatUser = JsonUtil.toObject(jsonResult, new TypeReference<WechatUser>() {
		});
		log.info("jsonResult" + jsonResult + "用户者信息" + wechatUser);

		Integer follow = 1;
		if (wechatUser.getSubscribe() != null) {
			follow = wechatUser.getSubscribe();
			wechatUser.setNickname(EmojiUtil.getString(wechatUser.getNickname()));
		} else {
			log.info("异常access_token默认用户已关注" + openId);
		}


		Map<String, Object> register = userService.shopRegister((int) UserVo.Pay_Type_WeiXin, openId, phone, smsCode, vmCode, wechatUser);
		Long id = (Long) register.get("customerId");
		Boolean isUpdate = (Boolean) register.get("isUpdate");
		if (id <= 0 || isUpdate == false) {
			result.setCode(-99);
			result.setMsg("注册失败");
			log.info("注册失败");
			return result;
		} else {//新用户填写完手机号后赠送优惠券
			if (follow == 1) {
				userActiveDegreeService.update(id, RegisterFlowEnum.ATTENTION);
				CouponForm couponForm = new CouponForm();
				couponForm.setCompanyId(CompanyEnum.YOUSHUI.getCompanyId());
				couponForm.setWay(CouponEnum.REGISTER_COUPON.getState());
				couponForm.setLimitRange(true);
				List<CouponBean> presentCoupon = couponService.getPresentCoupon(couponForm);
				if (presentCoupon != null && presentCoupon.size() > 0) {
					presentCoupon.stream().
							filter(coupon -> !couponService.isReceive(id, coupon.getId())).
							forEach(coupon -> {
								CouponCustomerBean couCusBean = new CouponCustomerBean();
								couCusBean.setQuantity(coupon.getSendMax());
								couCusBean.setCouponId(coupon.getId());
								couCusBean.setCustomerId(id);
								couCusBean.setStartTime(coupon.getLogicStartTime());
								couCusBean.setEndTime(coupon.getLogicEndTime());
								couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());//状态：0：新建，1：领取，2：已使用
								couponService.insertCouponCustomer(couCusBean);
							});
					userActiveDegreeService.update(id, RegisterFlowEnum.RECEIVE_PERCENT);
					TblCustomerBean customer = userService.getCustomer(PayTypeEnum.WEIXIN.getIndex(), openId);
					customer.setIsSend(1);
					tblCustomerDao.update(customer);
				}
			} else {

			}
		}
		String userCompanyId = id + ",0" + "," + openId;
		//生成后台系统的token
		String token = tokenAuthenticationService.generateToken(userCompanyId);
		result.setData(token);
		return result;
	}

	@PostMapping("/appRegister")
	@ResponseBody
	public ResultBean<String> appRegister(@RequestBody Map<String, String> param) {
		String phone = param.get("phone");
		String smsCode = param.get("smsCode");
		String openId = param.get("openId");
		ResultBean<String> result = new ResultBean<>();
		boolean b = smsService.checkCode(phone, smsCode);
		if (!b) {
			result.setCode(-99);
			result.setMsg("验证码错误");
			return result;
		}
		if (StringUtil.isBlank(openId)) {
			result.setCode(-99);
			result.setMsg("参数传递错误");
			return result;
		}
		MyWXGzhConfig myWXGzhConfig = gzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String accessToken = wxTicketService.getAppAccessToken(myWXGzhConfig);
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId;
		String jsonResult = HttpUtil.get(url);
		WechatUser wechatUser = JsonUtil.toObject(jsonResult, new TypeReference<WechatUser>() {
		});
		log.info("jsonResult" + jsonResult + "用户者信息" + wechatUser);
		wechatUser.setNickname(EmojiUtil.getString(wechatUser.getNickname()));

		Map<String, Object> register = userService.appRegister((int) UserVo.Pay_Type_WeiXin, openId, phone, smsCode, null, wechatUser);
		Long id = (Long) register.get("customerId");
		Boolean isUpdate = (Boolean) register.get("isUpdate");
		if (id <= 0 || isUpdate == false) {
			result.setCode(-99);
			result.setMsg("注册失败");
			log.info("注册失败");
			return result;
		} else {//新用户填写完手机号后赠送优惠券
			userActiveDegreeService.update(id, RegisterFlowEnum.ATTENTION);
			CouponForm couponForm = new CouponForm();
			couponForm.setCompanyId(CompanyEnum.YOUSHUI.getCompanyId());
			couponForm.setWay(CouponEnum.REGISTER_COUPON.getState());
			couponForm.setLimitRange(true);
			List<CouponBean> presentCoupon = couponService.getPresentCoupon(couponForm);
			if (presentCoupon != null && presentCoupon.size() > 0) {
				presentCoupon.stream().
						filter(coupon -> !couponService.isReceive(id, coupon.getId())).
						forEach(coupon -> {
							CouponCustomerBean couCusBean = new CouponCustomerBean();
							couCusBean.setQuantity(coupon.getSendMax());
							couCusBean.setCouponId(coupon.getId());
							couCusBean.setCustomerId(id);
							couCusBean.setStartTime(coupon.getLogicStartTime());
							couCusBean.setEndTime(coupon.getLogicEndTime());
							couCusBean.setState(CouponEnum.RECEIVE_COUPON.getState());//状态：0：新建，1：领取，2：已使用
							couponService.insertCouponCustomer(couCusBean);
						});
				userActiveDegreeService.update(id, RegisterFlowEnum.RECEIVE_PERCENT);
				TblCustomerBean customer = userService.getCustomer(PayTypeEnum.WEIXIN.getIndex(), openId);
				customer.setIsSend(1);
				tblCustomerDao.update(customer);
			}

		}
		String userCompanyId = id + ",0" + "," + openId;
		//生成后台系统的token
		String token = tokenAuthenticationService.generateToken(userCompanyId);
		result.setData(token);
		return result;
	}

	@RequestMapping("/getAppToken")
	@ResponseBody
	public ReturnDataUtil getAppToken(String code, String state, HttpServletResponse response) {
		log.info("appState===" + state);
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		//code换 accessToken 和 openid
		MyWXGzhConfig myWXGzhConfig = wxGzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String format = String.format(WXAddressConstant.OAUTH2_ACCESS_TOKEN,
				myWXGzhConfig.getAppAppId(), myWXGzhConfig.getAppSecret(), code);
		log.info("authFormat" + format);
		String jsonResult = HttpUtil.get(format);
		GzhParam object = JsonUtil.toObject(jsonResult, new TypeReference<GzhParam>() {
		});
		String openId = object.getOpenid();
		String unionId = object.getUnionid();

		TblCustomerBean customer = userService.getCustomer(PayTypeEnum.UNION.getIndex(), unionId);

		if (customer != null) {
			final String randomKey = jwtTokenUtil.getRandomKey();
			UserVo userVo = new UserVo();
			userVo.setId(customer.getId());
			userVo.setOpenId(customer.getOpenId());
			userVo.setType(UserVo.USER_ADMIN);
			userVo.setPayType(UserVo.Pay_Type_WeiXin);
			final String token = jwtTokenUtil.generateToken(JSON.toJSONString(userVo), randomKey);
			returnDataUtil.setReturnObject(token);
			returnDataUtil.setStatus(1);
			return returnDataUtil;
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setReturnObject(openId);
			log.info("app用户未注册，则进行注册");
			return returnDataUtil;
		}
	}


	@RequestMapping("/getShopToken3")
	public void getShopToken3(String code, String state, HttpServletResponse response) {
		log.info("state===" + state);
		MyWXGzhConfig myWXGzhConfig = wxGzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String format = String.format(WXAddressConstant.OAUTH2_ACCESS_TOKEN,
				myWXGzhConfig.getAppId(), myWXGzhConfig.getSecret(), code);
		String jsonResult = HttpUtil.get(format);
		GzhParam object = JsonUtil.toObject(jsonResult, new TypeReference<GzhParam>() {
		});
		String openId = object.getOpenid();
		TblCustomerBean customer = userService.getCustomer(PayTypeEnum.WEIXIN.getIndex(), openId);
		if (customer != null) {
			final String randomKey = jwtTokenUtil.getRandomKey();
			UserVo userVo = new UserVo();
			userVo.setId(customer.getId());
			userVo.setOpenId(openId);
			userVo.setType(UserVo.USER_ADMIN);
			userVo.setPayType(UserVo.Pay_Type_WeiXin);
			final String token = jwtTokenUtil.generateToken(JSON.toJSONString(userVo), randomKey);
			try {
				log.info("getOperatorTokenAppAdmin重定向地址：" + appHost + state);
				response.sendRedirect(frontHost + state + "&token=" + token);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			log.info("商城用户未注册，则进行注册");
			try {
				response.sendRedirect(customerLogin + "?openId=" + openId + "&type=2");
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 生成临时二维码
	 *
	 * @return url
	 * @author hjc
	 */
	@PostMapping("/createStrQr")
	@ResponseBody
	public ResultBean<String> createStrQr(@RequestBody(required = false) String id) {
		ResultBean<String> resultBean = new ResultBean<String>();
		MyWXGzhConfig myWXGzhConfig = gzhConfigFactory.getMyWXGzhConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		String accessToken = wxTicketService.getAccessToken(myWXGzhConfig);
		String sceneStr = null;
		if (id != null) {
			sceneStr = "Invite" + id;
			log.info("商城邀请人" + sceneStr);
		} else if (UserUtil.getUser() != null) {
			sceneStr = "Invite" + UserUtil.getUser().getId();
			log.info("机器邀请人" + sceneStr);
		}
		RestTemplate rest = new RestTemplate();
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;
		// 参数：{"action_name": "QR_LIMIT_STR_SCENE", "action_info": {"scene": {"scene_str": sceneStr}}}
		//{"expire_seconds": 604800, "action_name": "QR_STR_SCENE", "action_info": {"scene": {"scene_str": "test"}}}
		Map<String, Object> param = new HashMap<>();
		param.put("action_name", "QR_STR_SCENE");
		param.put("expire_seconds", 2592000);
		Map<String, Object> action = new HashMap<>();
		Map<String, Object> scene = new HashMap<>();
		scene.put("scene_str", sceneStr);
		action.put("scene", scene);
		param.put("action_info", action);
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		HttpEntity requestEntity = new HttpEntity(param, headers);
		Map result = null;
		try {
			ResponseEntity<Map> entity = rest.exchange(url, HttpMethod.POST, requestEntity, Map.class, new Object[0]);
			log.info("调用生成微信临时二维码URL接口返回结果:" + entity.getBody());
			result = (Map) entity.getBody();
		} catch (Exception e) {
			log.error("调用生成微信临时二维码URL接口异常", e);
		}
		String ticket = (String) result.get("ticket");
		String url2 = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;
		//ResponseEntity<Map> entity = rest.getForEntity(url2, Map.class);
		//result = (Map) entity.getBody();
		log.info("返回带有邀请场景值的二维码图片url" + url2);
		//HTTP GET请求（请使用https协议）
		//https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET
		//return result;
		resultBean.setData(url2);
		return resultBean;
	}

	@PostMapping("/test")
	@ResponseBody
	public String test(String openId, Integer id) {
		String userCompanyId = id + ",76" + "," + openId;
		String token = tokenAuthenticationService.generateToken(userCompanyId);
		return token;
	}

	public void updateCustomerUnionId(TblCustomerBean customer, GzhParam object) {
		if (StringUtil.isNotBlank(object.getUnionid()) && StringUtil.isBlank(customer.getUnionId())) {
			WxCustomerBean wxCustomer = new WxCustomerBean();
			wxCustomer.setUnionid(object.getUnionid());
			wxCustomer.setOpenId(customer.getOpenId());
			wxCustomerService.updateUnionid(wxCustomer);
		}
	}

	@PostMapping("/test6")
	@ResponseBody
	public String login(@RequestBody String code) {
		String appid = "";
		String secret = "";
		String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
//		String jsonResult = HttpUtil.get(url);
//		openid	string	用户唯一标识
//		session_key	string	会话密钥
//		unionid	string	用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UnionID 机制说明。
//		errcode	number	错误码
//		errmsg	string	错误信息
		return code;

		//GET https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code

	}
//	@GetMapping("/test2")
//	@ResponseBody
//	public String test2(Long id,String openId){
//		final String randomKey = jwtTokenUtil.getRandomKey();
//		UserVo userVo = new UserVo();
//		userVo.setId(id);
//		userVo.setOpenId(openId);
//		userVo.setType(UserVo.USER_ADMIN);
//		userVo.setPayType(UserVo.Pay_Type_WeiXin);
//		final String token = jwtTokenUtil.generateToken(JSON.toJSONString(userVo), randomKey);
//		return token;
//	}


	@RequestMapping("/huafaTokenLogin")
	public String huafaToken(@RequestParam String code, HttpServletRequest request,HttpServletResponse response) {
		// 换取access_token
		//String access_token_url = "https://devapp.huafatech.com/app/oauth2/authorize?client_id=85ce53814bbb4681a95a093e9b3ee7e5&client_secret=29d0dd5f35cf4dcd9321d70875658484&grant_type=authorization_code&redirect_uri=http://free-tcp.svipss.top:15234/admin/huafaTokenLogin?code=b34ae678a313d9289be90f655181630e&state=IOS";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("client_id", "85ce53814bbb4681a95a093e9b3ee7e5");
		map.put("client_secret", "29d0dd5f35cf4dcd9321d70875658484");
		map.put("grant_type", "authorization_code");
		map.put("redirect_uri", "http://free-tcp.svipss.top:15238/admin/huafaTokenLogin");
		map.put("code", code);
		String access_json = HttpClientUtil.doPost("http://devapp.huafatech.com/app/oauth2/accessToken", map);
		System.out.println(access_json + "=================++++==================================");

		Map<String, String> map_access_json = new HashMap<String, String>();
		Map access_map = JSON.parseObject(access_json, map_access_json.getClass());
		// 获得第三方用户数据
		String access_token = (String) access_map.get("access_token");
		String huafaAppOpenId = (String) access_map.get("openid");

		String show_url = "http://devapp.huafatech.com/app/oauth2/userInfo?access_token=" + access_token;
		String user_json = HttpClientUtil.get(show_url);
		Map<String, String> map_user_json = new HashMap<String, String>();
		Map user_map = JSON.parseObject(user_json, map_user_json.getClass());
		System.out.println(user_map);

		TblCustomerBean huafaUser = new TblCustomerBean();
		huafaUser = tblCustomerDao.getCustomerByPhone((String) user_map.get("phone"));

		if (huafaUser !=null && huafaUser.getHuafaAppOpenId() !=null){
			String userCompanyId = huafaUser.getId().toString()+",0"+","+huafaUser.getPhone();
			String token = tokenAuthenticationService.generateToken(userCompanyId);
			
			log.info("token=" + token);
			sendCoupon(huafaUser.getId());
			return "redirect:http://webapp.youshuidaojia.com:8081/cMain/firstPage?vm=1&token=" + token;
		}else if (huafaUser != null) {
			// 修改数据
			huafaUser.setHuafaAppOpenId(huafaAppOpenId);
			huafaUser.setLastUpdateId("优生活用户");
			tblCustomerDao.update(huafaUser);
			// 根据华发用户信息生成token
			String userCompanyId = huafaUser.getId().toString()+",0"+","+huafaUser.getPhone();
			String token = tokenAuthenticationService.generateToken(userCompanyId);
			
			log.info("token=" + token);
			sendCoupon(huafaUser.getId());
			return "redirect:http://webapp.youshuidaojia.com:8081/cMain/firstPage?vm=1&token=" + token;
		} else {
			// 存入数据
			TblCustomerBean newUser = new TblCustomerBean();
			newUser.setHuafaAppOpenId(huafaAppOpenId);
			newUser.setPhone((String) user_map.get("phone"));
			newUser.setNickname("优生活用户" + (String) user_map.get("phone"));
			newUser.setType(6);
			newUser.setCreateTime(new Date());
			newUser.setUpdateTime(new Date());
			newUser = userService.insert(newUser);
			// 根据华发用户信息生成token
			String userCompanyId = newUser.getId().toString()+",0"+","+newUser.getPhone();
			String token = tokenAuthenticationService.generateToken(userCompanyId);
			
			log.info("token=" + token);
			// 将生成的token和登录用户信息保存在缓存中一分
			//userService.addUserCache(token,umsMember);
			//response.sendRedirect("");
			sendCoupon(newUser.getId());
			return "redirect:http://webapp.youshuidaojia.com:8081/cMain/firstPage?vm=1&token=" + token;
		}
	}
	
	public void sendCoupon(Long customerId) {
		CouponForm couponForm = new CouponForm();
		couponForm.setWay(CouponEnum.SHOPREGISTER_COUPON.getState());
		couponForm.setLimitRange(false);
		couponForm.setUseWhere(CouponEnum.USE_MACHINES.getState());
		List<CouponBean> presentCoupon = couponService.getPresentCoupon(couponForm);
		if (presentCoupon != null && presentCoupon.size() > 0) {
			presentCoupon.stream().filter(coupon -> !couponService.isReceive(customerId, coupon.getId()))
					.forEach(coupon -> {
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
	}
}
