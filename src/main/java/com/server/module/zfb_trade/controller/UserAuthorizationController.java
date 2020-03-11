package com.server.module.zfb_trade.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.server.company.MachinesService;
import com.server.module.payRecord.AliPayRecordService;
import com.server.module.payRecord.PayRecordBean;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.auth.util.JwtTokenUtil;
import com.server.module.trade.memberOrder.MemberOrderBean;
import com.server.module.trade.memberOrder.MemberOrderService;
import com.server.module.trade.order.OrderService;
import com.server.module.trade.vendingMachinesWay.VendingMachinesWayService;
import com.server.module.trade.vendingMachinesWayItem.MoreGoodsWayDto;
import com.server.module.trade.vendingMachinesWayItem.VmwayItemService;
import com.server.module.zfb_trade.convert.AlipayUser2CustomerConverter;
import com.server.module.zfb_trade.convert.Msg2CreditWithheldConverter;
import com.server.module.zfb_trade.factory.AlipayAPIClientFactory;
import com.server.module.zfb_trade.factory.AlipayConfigFactory;
import com.server.module.zfb_trade.factory.AlipayEnvConfigFactory;
import com.server.module.zfb_trade.module.creditwithheld.AliCreditWithheldService;
import com.server.module.zfb_trade.module.creditwithheld.CreditWithheldBean;
import com.server.module.zfb_trade.module.customer.AliCustomerService;
import com.server.module.zfb_trade.module.customer.CustomerBean;
import com.server.module.zfb_trade.module.logininfo.AliLoginInfoService;
import com.server.module.zfb_trade.module.logininfo.LoginInfoBean;
import com.server.module.zfb_trade.module.vmbase.AliVendingMachinesService;
import com.server.module.zfb_trade.module.vmbase.VmbaseInfoDto;
import com.server.module.zfb_trade.module.vmitem.AliVmitemService;
import com.server.module.zfb_trade.paramconfig.AlipayConfig;
import com.server.module.zfb_trade.paramconfig.AlipayServiceEnvConfig;
import com.server.module.zfb_trade.paramconfig.H5URLConfig;
import com.server.module.zfb_trade.paramconfig.UrlConfig;
import com.server.module.zfb_trade.service.AlipayService;
import com.server.module.zfb_trade.util.AlipaySubmit;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.module.zfb_trade.util.RequestUtil;
import com.server.module.zfb_trade.util.ResultUtil;
import com.server.module.zfb_trade.util.enumparam.CustomerEnum;
import com.server.module.zfb_trade.util.enumparam.ResultEnum;
import com.server.redis.RedisService;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.stateEnum.CommandVersionEnum;
import com.server.util.stateEnum.CompanyEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/aliUser")
public class UserAuthorizationController {

	public static Logger log = LogManager.getLogger(UserAuthorizationController.class); 
	@Autowired
	private AlipayEnvConfigFactory alipayEnvFactory;
	@Autowired
	private AlipayAPIClientFactory alipayClientFactory;
	@Autowired
	@Qualifier("aliCustomerService")
	private AliCustomerService customerService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private AlipayService alipayService;
	@Autowired
	private H5URLConfig h5URLConfig;
	@Value("${wechat.pay.replenishHost}")
	private String replenishHost;

	@Autowired
	@Qualifier("aliCreditWithheldService")
	private AliCreditWithheldService creditWithheldService;
	@Autowired
	@Qualifier("aliVmitemService")
	private AliVmitemService vmitemService;
	@Autowired
	private AliPayRecordService aliPayRecordService;
	@Autowired
	private AlipaySubmit alipaySubmit;
	@Autowired
	private AliLoginInfoService loginInfoService;
	@Autowired
	private MachinesService machinesService;
	@Autowired
	private AlipayConfigFactory alipayConfigFactory;
	@Autowired
	private OrderService orderServiceImpl;
	@Autowired
	private VendingMachinesWayService vendingMachinesWayServiceImpl;
	@Autowired
	private VmwayItemService vmwayItemService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private MemberOrderService memberOrderService;

    @Autowired
    @Qualifier("aliVendingMachinesService")
    private AliVendingMachinesService vendingMachinesService;
	/**
	 * 获取授权地址
	 * @author hebiting
	 * @date 2018年9月3日上午10:06:15
	 * @return
	 */
	@GetMapping("/getAuthorizationUrl")
	@ResponseBody
	public ReturnDataUtil getAuthorizationUrl(String vmCode){
		log.info("<UserAuthorizationController--getAuthorizationUrl--start>");
		if(StringUtils.isBlank(vmCode) || ("undefined").equals(vmCode)){
			return ResultUtil.error(ResultEnum.ILLEGAL_PARAMS);
		}
		Integer companyId = machinesService.getCompanyIdByVmCode(vmCode);
		AlipayServiceEnvConfig alipayServiceEnv = alipayEnvFactory.getAlipayServiceEnvConfig(companyId);
		String oauthURL = String.format(UrlConfig.OAUTH2URL, alipayServiceEnv.getApp_id(),vmCode);
		log.info("<UserAuthorizationController--getAuthorizationUrl--end>");
		return ResultUtil.success(oauthURL);
	}
	
	/**
	 * 获取授权地址
	 * @author hebiting
	 * @date 2018年9月3日上午10:06:15
	 * @return
	 */
	@GetMapping("/getAiInfoUrl")
	@ResponseBody
	public ReturnDataUtil getAiInfoUrl(String vmCode){
		log.info("<UserAuthorizationController--getAiInfoUrl--start>");
		if(StringUtils.isBlank(vmCode) || ("undefined").equals(vmCode)){
			return ResultUtil.error(ResultEnum.ILLEGAL_PARAMS);
		}
		Integer companyId = machinesService.getCompanyIdByVmCode(vmCode);
		AlipayServiceEnvConfig alipayServiceEnv = alipayEnvFactory.getAlipayServiceEnvConfig(companyId);
		String oauthURL = String.format(UrlConfig.OAUTH2GETALIINFOURL, alipayServiceEnv.getApp_id());
		log.info("<UserAuthorizationController--getAiInfoUrl--end>");
		return ResultUtil.success(oauthURL);
	}


	/**
	 * 支付完成后重新生成token跳转请求
	 * @author hebiting
	 * @date 2018年5月22日下午8:19:50
	 * @param vmCode
	 * @param payCode
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/forward")
	public String forward(@RequestParam String vmCode, @RequestParam("out_trade_no") String payCode,
						  HttpServletRequest request, HttpServletResponse response) {
		log.info("<UserAuthorizationController--forward--start>");
		log.info("回调成功");
		String token = "";
		PayRecordBean record = aliPayRecordService.findPayRecordByPayCode(payCode);
		if (record.getCustomerId() != null) {
			log.info("重新生成cookie");
			CustomerBean cus = customerService.queryById(record.getCustomerId());
			UserVo userVo = new UserVo();
			userVo.setId(cus.getId());
			userVo.setOpenId(cus.getAlipayUserId());
			userVo.setPayType(UserVo.Pay_Type_ZhiFuBao);// 支付宝
			userVo.setType(UserVo.USER_CUSTOMER);// 消费开门
			String randomKey = jwtTokenUtil.getRandomKey();
			token = jwtTokenUtil.generateToken(JsonUtil.toJson(userVo), randomKey);
		}
		log.info("<UserAuthorizationController--forward--end>");
		VmbaseInfoDto baseInfo = vendingMachinesService.getBaseInfo(vmCode);
		Integer freeMachine=baseInfo.getFreeMachine();
		if(freeMachine==0){
			return "redirect:".concat(h5URLConfig.weightH5Page.concat("?vmCode=").concat(vmCode).
					concat("&token=").concat(token));
		}else{
			return "redirect:".concat(h5URLConfig.weightH5PageForFree.concat("?vmCode=").concat(vmCode).
					concat("&token=").concat(token));
		}
		//return "redirect:".concat(h5URLConfig.weightH5Page.concat("?vmCode=").concat(vmCode).concat("&token=").concat(token));
	}


	@GetMapping("/balanceForward")
	public String balanceForward(@RequestParam String vmCode, @RequestParam("out_trade_no") String payCode,
						  HttpServletRequest request, HttpServletResponse response) {
		log.info("<UserAuthorizationController--balanceForward--start>");
		String token = "";
		MemberOrderBean memberOrder = memberOrderService.getMemberOrder(payCode);

		if (memberOrder.getCustomerId() != null) {
			log.info("重新生成cookie");
			CustomerBean cus = customerService.queryById(memberOrder.getCustomerId());
			UserVo userVo = new UserVo();
			userVo.setId(cus.getId());
			userVo.setOpenId(cus.getAlipayUserId());
			userVo.setPayType(UserVo.Pay_Type_ZhiFuBao);// 支付宝
			userVo.setType(UserVo.USER_CUSTOMER);// 消费开门
			String randomKey = jwtTokenUtil.getRandomKey();
			token = jwtTokenUtil.generateToken(JsonUtil.toJson(userVo), randomKey);
		}
		log.info("<UserAuthorizationController--balanceForward--end>");
//		VmbaseInfoDto baseInfo = vendingMachinesService.getBaseInfo(vmCode);
//		Integer freeMachine=baseInfo.getFreeMachine();
//		if(freeMachine==0){
//			return "redirect:".concat(h5URLConfig.weightH5Page.concat("?vmCode=").concat(vmCode).
//					concat("&token=").concat(token));
//		}else{
			return "redirect:".concat(h5URLConfig.weightH5PageForFree.concat("?vmCode=").concat(vmCode).concat("&wayNumber=").concat(memberOrder.getWayNumber()).
					concat("&openDoor=1&token=").concat(token));
//		}
		//return "redirect:".concat(h5URLConfig.weightH5Page.concat("?vmCode=").concat(vmCode).concat("&token=").concat(token));
	}
	/**
	 * 生活号授权，然后获取客户信息，生成token
	 * @author hebiting
	 * @date 2018年5月22日下午8:19:18
	 * @param request
	 * @param response
	 * @return
	 * @throws AlipayApiException
	 */
	@GetMapping("/getUserInfo")
	public String getUserInfo(HttpServletRequest request, HttpServletResponse response)
			throws AlipayApiException {
		log.info("<UserAuthorizationController--getUserInfo--start>");
		/** 1. 解析请求参数 */
		Map<String, String> params = RequestUtil.getRequestParams(request);
		/** 2. 获得authCode */
		String authCode = params.get("auth_code");
		String token = "";
		/** 3. 利用authCode获得authToken */
		AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
		oauthTokenRequest.setCode(authCode);
		AlipayServiceEnvConfig alipayServiceEnvConfig = alipayEnvFactory.getAlipayServiceEnvConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		oauthTokenRequest.setGrantType(alipayServiceEnvConfig.grant_type);
		AlipayClient alipayClient = alipayClientFactory.getAlipayClient(null);
		AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(oauthTokenRequest);
		// 成功获得authToken
		if (null != oauthTokenResponse && oauthTokenResponse.isSuccess()) {
			/** 4. 利用authToken获取用户信息 */
			AlipayUserInfoShareRequest userinfoShareRequest = new AlipayUserInfoShareRequest();
			AlipayUserInfoShareResponse userinfoShareResponse = alipayClient.execute(userinfoShareRequest,
					oauthTokenResponse.getAccessToken());
			// 成功获得用户信息
			if (null != userinfoShareResponse && userinfoShareResponse.isSuccess()) {
				// 获取用户信息
				String alipayUserId = userinfoShareResponse.getUserId();
				/*** 把支付宝的用户信息保存到数据库中 */
				CustomerBean customer = AlipayUser2CustomerConverter.setCutomerData(userinfoShareResponse);
				log.info("CustomerBean    " + JsonUtil.toJson(customer));
				if (StringUtil.isNotBlank(alipayUserId)) {
					CustomerBean temCustomer = new CustomerBean();
					temCustomer.setAlipayUserId(alipayUserId);
					CustomerBean queryCustomer = customerService.queryByAliId(temCustomer);
					if (queryCustomer != null) {
						if (!alipayUserId.equals(queryCustomer.getAlipayUserId())) {
							customer.setId(queryCustomer.getId());
							customerService.updateCustomer(customer);
						}
					}
				}
				CustomerBean cus = customerService.queryByAliId(customer);
				String randomKey = jwtTokenUtil.getRandomKey();
				UserVo userVo = new UserVo();
				userVo.setId(cus!=null?cus.getId():null);
				userVo.setPayType(UserVo.Pay_Type_ZhiFuBao);// 支付宝
				userVo.setType(UserVo.USER_CUSTOMER);// 消费开门
				token = jwtTokenUtil.generateToken(JsonUtil.toJson(userVo), randomKey);
			}
		}
		String returnUrl = params.get("state");
		log.info("<UserAuthorizationController--getUserInfo--end>");
		return "redirect:".concat(returnUrl).concat("?title=1").concat("&token=").concat(token);
	}

	/**
	 * 给运维人员登录时授权拿到aliUserId
	 * @author hebiting
	 * @date 2018年5月24日上午9:14:23
	 * @param request
	 * @param response
	 * @return
	 * @throws AlipayApiException
	 */
	@GetMapping("/getAliInfo")
	public String getAliInfo(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException{
		log.info("<UserAuthorizationController--getAliInfo--start>");
		/** 1. 解析请求参数 */
		Map<String, String> params = RequestUtil.getRequestParams(request);
		/** 2. 获得authCode */
		String authCode = params.get("auth_code");
		String state = params.get("state");
		/** 3. 利用authCode获得authToken */
		String[] urls = state.split("-");
		String token = null;
		String alipayUserId ="";
		AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
		oauthTokenRequest.setCode(authCode);
		AlipayServiceEnvConfig alipayServiceEnvConfig = alipayEnvFactory.getAlipayServiceEnvConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		oauthTokenRequest.setGrantType(alipayServiceEnvConfig.grant_type);
		AlipayClient alipayClient = alipayClientFactory.getAlipayClient(null);
		AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(oauthTokenRequest);
		// 成功获得authToken
		if (null != oauthTokenResponse && oauthTokenResponse.isSuccess()) {
			/** 4. 利用authToken获取用户信息 */
			AlipayUserInfoShareRequest userinfoShareRequest = new AlipayUserInfoShareRequest();
			AlipayUserInfoShareResponse userinfoShareResponse = alipayClient.execute(userinfoShareRequest,
					oauthTokenResponse.getAccessToken());
			// 成功获得用户信息
			if (null != userinfoShareResponse && userinfoShareResponse.isSuccess()) {
				// 获取用户信息
				alipayUserId = userinfoShareResponse.getUserId();
				LoginInfoBean findLoginInfo = loginInfoService.findLoginInfo(alipayUserId);
				if(findLoginInfo != null){
					final String randomKey = jwtTokenUtil.getRandomKey();
					UserVo userVo = new UserVo();
					userVo.setId(findLoginInfo.getId());
					userVo.setOpenId(alipayUserId);
					userVo.setPayType(UserVo.Pay_Type_ZhiFuBao);
					userVo.setType(UserVo.USER_ADMIN);
					token = jwtTokenUtil.generateToken(JSON.toJSONString(userVo), randomKey);
				}
			}
		}
		String address = h5URLConfig.address;
		if(("mms").equals(redisService.getString("mms"))){
			address = replenishHost;
		}
		if(token != null){
			log.info("运维重定向地址redirect:".concat(address).concat(urls[1]).concat("&openId=").concat(alipayUserId).concat("&token=").concat(token).concat("&payType=2"));
			return "redirect:".concat(address).concat(urls[1]).concat("&openId=").concat(alipayUserId).concat("&token=").concat(token)
					.concat("&payType=2");
		}
		log.info("运维重定向地址redirect:".concat(address).concat(urls[0]).concat("&openId=").concat(alipayUserId).concat("&payType=2"));
		return "redirect:".concat(address).concat(urls[0]).concat("&openId=").concat(alipayUserId)
				.concat("&payType=2");
	}
	
	/**
	 * 生活号授权，然后生成运维人员token
	 * @author hebiting
	 * @date 2018年5月22日下午8:18:49
	 * @param request
	 * @param response
	 * @return
	 * @throws AlipayApiException
	 */
	@GetMapping("/getLoginInfo")
	public String getLoginInfo(HttpServletRequest request, HttpServletResponse response)
			throws AlipayApiException {
		log.info("<UserAuthorizationController--getLoginInfo--start>");
		/** 1. 解析请求参数 */
		Map<String, String> params = RequestUtil.getRequestParams(request);
		/** 2. 获得authCode */
		String authCode = params.get("auth_code");
		String returnUrl = params.get("state");
		String token = "";
		/** 3. 利用authCode获得authToken */
		AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
		oauthTokenRequest.setCode(authCode);
		AlipayServiceEnvConfig alipayServiceEnv = alipayEnvFactory.getAlipayServiceEnvConfig(CompanyEnum.GUANGZHOU_YOUSHUI.getCompanyId());
		oauthTokenRequest.setGrantType(alipayServiceEnv.grant_type);
		AlipayClient alipayClient = alipayClientFactory.getAlipayClient(null);
		AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(oauthTokenRequest);
		// 成功获得authToken
		if (null != oauthTokenResponse && oauthTokenResponse.isSuccess()) {
			/** 4. 利用authToken获取用户信息 */
			AlipayUserInfoShareRequest userinfoShareRequest = new AlipayUserInfoShareRequest();
			AlipayUserInfoShareResponse userinfoShareResponse = alipayClient.execute(userinfoShareRequest,
					oauthTokenResponse.getAccessToken());
			// 成功获得用户信息
			if (null != userinfoShareResponse && userinfoShareResponse.isSuccess()) {
				// 获取用户信息
				String alipayUserId = userinfoShareResponse.getUserId();
				LoginInfoBean findLoginInfo = loginInfoService.findLoginInfo(alipayUserId);
				if(findLoginInfo==null){
					String randomKey = jwtTokenUtil.getRandomKey();
					UserVo userVo = new UserVo();
					userVo.setId(findLoginInfo!=null?findLoginInfo.getId():null);
					userVo.setOpenId(alipayUserId);
					userVo.setPayType(UserVo.Pay_Type_ZhiFuBao);// 支付宝
					userVo.setType(UserVo.USER_ADMIN);// 消费开门
					token = jwtTokenUtil.generateToken(JsonUtil.toJson(userVo), randomKey);
					log.info("<UserAuthorizationController--getLoginInfo--end>");
					return "redirect:".concat(returnUrl).concat("?token=").concat(token);
				}else{
					returnUrl = h5URLConfig.skipUrl;
					return "redirect:".concat(returnUrl).concat("?openId=").concat(alipayUserId).
							concat("&payType=2");
				}
			}
		}
		//定向
		return "redirect:".concat(returnUrl).concat("?token=").concat(token);
	}
	
	/**
	 * 扫码进去直接授权
	 * @author hebiting
	 * @date 2018年5月22日下午8:21:10
	 * @param vmCode
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/{vmCode}")
	public String vmCode(@PathVariable String vmCode, HttpServletRequest request, HttpServletResponse response) {
		log.info("<UserAuthorizationController--vmCode--start>");
		try {
			Map<String, String> requestMap = RequestUtil.getRequestParams(request);
			String authCode = requestMap.get("auth_code");
			AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
			oauthTokenRequest.setCode(authCode);
			Integer companyId = machinesService.getCompanyIdByVmCode(vmCode);
			AlipayServiceEnvConfig alipayServiceEnv = alipayEnvFactory.getAlipayServiceEnvConfig(companyId);
			oauthTokenRequest.setGrantType(alipayServiceEnv.grant_type);
			AlipayClient alipayClient = alipayClientFactory.getAlipayClient(companyId);
			String token = "";
			AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(oauthTokenRequest);
			if (oauthTokenResponse != null && oauthTokenResponse.isSuccess()) {
				AlipayUserInfoShareRequest shareRequest = new AlipayUserInfoShareRequest();
				AlipayUserInfoShareResponse shareResponse = alipayClient.execute(shareRequest,
						oauthTokenResponse.getAccessToken());
				if (shareResponse != null && shareResponse.isSuccess()) {
					/** 5. 判断是否进行了实名验证 */
					if (!booleanIsCertified(shareResponse)) {
						return "redirect:".concat(h5URLConfig.realNamePage); // 跳到提醒用户先进行实名认证
					}
					String alipayUserId = shareResponse.getUserId();
					CustomerBean customer = AlipayUser2CustomerConverter.setCutomerData(shareResponse);
					Long customerId = null;
					if (StringUtil.isNotBlank(alipayUserId)) {
						CustomerBean queryCustomer = customerService.queryByAliId(customer);
						if(queryCustomer == null){
							customer.setVmCode(vmCode);
							customerId = customerService.saveCustomer(customer);
						}else{
							customerId = queryCustomer.getId();
						}
					}
					String randomKey = jwtTokenUtil.getRandomKey();
					UserVo userVo = new UserVo();
					userVo.setId(customerId);
					userVo.setOpenId(alipayUserId);
					userVo.setPayType(UserVo.Pay_Type_ZhiFuBao);
					userVo.setType(UserVo.USER_CUSTOMER);
					token = jwtTokenUtil.generateToken(JsonUtil.toJson(userVo), randomKey);
					String agreementNo = alipayService.querySign(alipayUserId,vmCode);
					log.info("【用户签约码】agreementNo:" + agreementNo);
					// 判断用户是否签署代扣协议
					if (StringUtil.isBlank(agreementNo)) {
						return "redirect:".concat(h5URLConfig.signUrl.concat("?vmCode=").concat(vmCode));
					}
				}
			}
            VmbaseInfoDto baseInfo = vendingMachinesService.getBaseInfo(vmCode);
            Integer freeMachine=baseInfo.getFreeMachine();
            if(freeMachine==0){
                return "redirect:".concat(h5URLConfig.weightH5Page.concat("?vmCode=").concat(vmCode).
                        concat("&token=").concat(token));
            }else{
                return "redirect:".concat(h5URLConfig.weightH5PageForFree.concat("?vmCode=").concat(vmCode).
                        concat("&token=").concat(token));
            }

		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return "redirect:http://p.alipay.com/P/AYf6YBeM";
	}

	@GetMapping("/vision/{vmCode}")
	public String visionVmCode(@PathVariable String vmCode, HttpServletRequest request, HttpServletResponse response) {
		log.info("<UserAuthorizationController--visionVmCode--start>");
		try {
			Map<String, String> requestMap = RequestUtil.getRequestParams(request);
			String authCode = requestMap.get("auth_code");
			AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
			oauthTokenRequest.setCode(authCode);
			Integer companyId = machinesService.getCompanyIdByVmCode(vmCode);
			AlipayServiceEnvConfig alipayServiceEnv = alipayEnvFactory.getAlipayServiceEnvConfig(companyId);
			oauthTokenRequest.setGrantType(alipayServiceEnv.grant_type);
			AlipayClient alipayClient = alipayClientFactory.getAlipayClient(companyId);
			String token = "";
			AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(oauthTokenRequest);
			if (oauthTokenResponse != null && oauthTokenResponse.isSuccess()) {
				AlipayUserInfoShareRequest shareRequest = new AlipayUserInfoShareRequest();
				AlipayUserInfoShareResponse shareResponse = alipayClient.execute(shareRequest,
						oauthTokenResponse.getAccessToken());
				if (shareResponse != null && shareResponse.isSuccess()) {
					/** 5. 判断是否进行了实名验证 */
					if (!booleanIsCertified(shareResponse)) {
						return "redirect:".concat(h5URLConfig.realNamePage); // 跳到提醒用户先进行实名认证
					}
					String alipayUserId = shareResponse.getUserId();
					CustomerBean customer = AlipayUser2CustomerConverter.setCutomerData(shareResponse);
					Long customerId = null;
					if (StringUtil.isNotBlank(alipayUserId)) {
						CustomerBean queryCustomer = customerService.queryByAliId(customer);
						if(queryCustomer == null){
							customer.setVmCode(vmCode);
							customerId = customerService.saveCustomer(customer);
						}else{
							customerId = queryCustomer.getId();
						}
					}
					String randomKey = jwtTokenUtil.getRandomKey();
					UserVo userVo = new UserVo();
					userVo.setId(customerId);
					userVo.setOpenId(alipayUserId);
					userVo.setPayType(UserVo.Pay_Type_ZhiFuBao);
					userVo.setType(UserVo.USER_CUSTOMER);
					token = jwtTokenUtil.generateToken(JsonUtil.toJson(userVo), randomKey);
					String agreementNo = alipayService.querySign(alipayUserId,vmCode);
					log.info("【用户签约码】agreementNo:" + agreementNo);
					// 判断用户是否签署代扣协议
					if (StringUtil.isBlank(agreementNo)) {
						return "redirect:".concat("http://sms.youshuidaojia.com:8301/aliCreditPayment".concat("?vmCode=").concat(vmCode));
					}
				}
			}
			return "redirect:".concat("http://sms.youshuidaojia.com:8301/aliMain".concat("?vmCode=").concat(vmCode).
					concat("&token=").concat(token));
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return "redirect:http://p.alipay.com/P/AYf6YBeM";
	}
	
	/**
	 * 判断是否实名验证
	 * 
	 * @author hebiting
	 * @date 2018年4月24日下午3:51:26
	 * @param shareResponse
	 * @return
	 */
	public boolean booleanIsCertified(AlipayUserInfoShareResponse shareResponse) {
		log.info("<UserAuthorizationController--booleanIsCertified--start>");
		String isCertified = shareResponse.getIsCertified();
		boolean result = false;
		if (isCertified != null && isCertified.equals(CustomerEnum.CERTIFIED.getMessage())) {
			result = true;
		}
		log.info("<UserAuthorizationController--booleanIsCertified--end>");
		return result;
	}

	/**
	 * 查询售卖机商品信息
	 * 
	 * @author hebiting
	 * @date 2018年5月5日下午4:10:52
	 * @param vmCode
	 * @return
	 */
	@GetMapping("/queryItem")
	@ResponseBody
	public ReturnDataUtil queryItem(@RequestParam String vmCode) {
		log.info("<UserAuthorizationController--queryItem--start>");
		Assert.hasText(vmCode, "智能设备编号不能为空");
		Integer version = orderServiceImpl.getVersionByVmCode(vmCode);
		List<MoreGoodsWayDto> wayInfo = null;
		if(CommandVersionEnum.VER2.getState().equals(version)){
			wayInfo = vmwayItemService.getWayInfo(vmCode,1);
		}else{
			wayInfo = vendingMachinesWayServiceImpl.getWayInfo(vmCode,1);
		}
		log.info("<UserAuthorizationController--queryItem--end>");
		return ResultUtil.success(wayInfo);
	}

	

	/**
	 * 获取协议信息
	 * @author hebiting
	 * @date 2018年5月15日下午9:12:25
	 * @param vmCode
	 * @param request
	 * @return
	 */
	@GetMapping("/getCreditWithheld")
	@ResponseBody
	public ReturnDataUtil getCreditWithheld(@RequestParam String vmCode, HttpServletRequest request) {
		log.info("<UserAuthorizationController--getCreditWithheld--start>");
		log.info("forwardAlipayapi进来了");
		// 产品码
		String product_code = "GENERAL_WITHHOLDING_P"; // 必填，个人页面代扣GENERAL_WITHHOLDING_P
		String returnUrl = h5URLConfig.signReturnUrl.concat("?vmCode=").concat(vmCode);
		Integer companyId = machinesService.getCompanyIdByVmCode(vmCode);
		AlipayConfig alipayConfig = alipayConfigFactory.getAlipayConfig(companyId);
		AlipayServiceEnvConfig alipayServiceEnv = alipayEnvFactory.getAlipayServiceEnvConfig(companyId);
		Map<String, String> param = new HashMap<String, String>();
		param.put("service", "alipay.dut.customer.agreement.page.sign");
		param.put("partner", alipayServiceEnv.partner);
		param.put("_input_charset", AlipayServiceEnvConfig.CHARSET_UTF8);
		param.put("product_code", product_code);
		param.put("return_url", returnUrl);
		String htmltext = alipaySubmit.buildRequest(param, "get", "确认",alipayConfig);
		log.info("<UserAuthorizationController--getCreditWithheld--end>");
		return ResultUtil.success(htmltext, false);
	}

	/**
	 * 保存协议内容
	 * 
	 * @author hebiting
	 * @date 2018年4月24日下午3:26:27
	 * @return
	 */
	@GetMapping("/save/CreditWithheld")
	public String saveCreditWithheld(HttpServletRequest request) {
		log.info("<UserAuthorizationController--saveCreditWithheld--start>");
		String vmCode = request.getParameter("vmCode");
		String method = request.getParameter("method");
		log.info("CreditWithheld"+method);
		Map<String, String[]> param = request.getParameterMap();
		CreditWithheldBean creditWithheld = Msg2CreditWithheldConverter.setCreditWithheld(param);
		creditWithheld.setCreateTime(new Date());
		Integer companyId = machinesService.getCompanyIdByVmCode(vmCode);
		creditWithheld.setCompanyId(companyId);
		creditWithheldService.save(creditWithheld);
		AlipayServiceEnvConfig alipayServiceEnv = alipayEnvFactory.getAlipayServiceEnvConfig(companyId);
		String url = String.format(h5URLConfig.buyEntry, alipayServiceEnv.getApp_id());
		if(StringUtils.isNotBlank(method)) {
			url = String.format("https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=%s&scope=auth_user&redirect_uri=http://yms.youshuidaojia.com/aliUser/vision/", alipayServiceEnv.getApp_id());
		}
		log.info("<UserAuthorizationController--saveCreditWithheld--end>");
		return "redirect:".concat(url).concat(vmCode);
	}
	
}
