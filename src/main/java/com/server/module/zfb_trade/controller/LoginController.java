package com.server.module.zfb_trade.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.server.jwt.TokenAuthenticationService;
import com.server.module.trade.auth.UserVo;
import com.server.module.trade.auth.util.JwtTokenUtil;
import com.server.module.zfb_trade.module.customer.AliCustomerService;
import com.server.module.zfb_trade.module.customer.CustomerBean;
import com.server.module.zfb_trade.module.logininfo.AliLoginInfoService;
import com.server.module.zfb_trade.module.logininfo.LoginInfoBean;
import com.server.module.zfb_trade.paramconfig.H5URLConfig;
import com.server.module.zfb_trade.util.CookieUtil;
import com.server.module.zfb_trade.util.JsonUtil;
import com.server.module.zfb_trade.util.ResultUtil;
import com.server.module.zfb_trade.util.enumparam.ResultEnum;
import com.server.redis.RedisService;
import com.server.redis.SmsKey;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/aliLogin")
public class LoginController {
	
	public static Logger log = LogManager.getLogger(LoginController.class); 

	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;
	@Autowired
	@Qualifier("aliLoginInfoService")
	private AliLoginInfoService loginInfoService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private AliCustomerService customerService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private H5URLConfig h5URLConfig;
	
	/**
	 * 登录
	 * @author hebiting
	 * @date 2018年4月29日上午11:38:08
	 * @param loginCode
	 * @param password
	 * @param vmCode
	 * @param request
	 * @return
	 */
	@PostMapping("/doLogin")
	public ReturnDataUtil doLogin(@RequestParam String loginCode,@RequestParam String password,
			@RequestParam String vmCode,HttpServletRequest request,HttpServletResponse response){
		log.info("<LoginController--doLogin--start>");
		Assert.hasLength(loginCode,"账号不能为空");
		Assert.hasLength(password,"密码不能为空");
		Cookie cookie = CookieUtil.getCookieByName(request, CookieUtil.CookieParam.TOKEN);
		String token = cookie.getValue();
		Claims claimFromToken = jwtTokenUtil.getClaimFromToken(token);
		String subject = claimFromToken.getSubject();
		UserVo userVo = JsonUtil.toObject(subject, new TypeReference<UserVo>(){});
		userVo.setType(UserVo.USER_ADMIN);
		String randomKey = jwtTokenUtil.getRandomKey();
		String newToken = jwtTokenUtil.generateToken(JsonUtil.toJson(userVo), randomKey);
		CookieUtil.addCookie(request, response, CookieUtil.CookieParam.TOKEN, newToken, CookieUtil.CookieParam.EXPIRE, false);
		CustomerBean customer = customerService.queryById(userVo.getId());
		ReturnDataUtil doLogin = loginInfoService.doLogin(loginCode, password, customer.getAlipayUserId(), vmCode);
		log.info("<LoginController--doLogin--end>");
		return doLogin;
	}
	
	/**
	 * 已封印，暂时不使用
	 * @author hebiting
	 * @date 2018年4月29日上午11:38:30
	 * @param loginCode
	 * @param password
	 * @param request
	 * @return
	 */
	@PostMapping("/queryLogin")
	public ReturnDataUtil queryLogin(@RequestParam String loginCode,@RequestParam String password,
			HttpServletRequest request){
		log.info("<LoginController--queryLogin--start>");
		Assert.hasLength(loginCode,"账号不能为空");
		Assert.hasLength(password,"密码不能为空");
		Cookie cookie = CookieUtil.getCookieByName(request, CookieUtil.CookieParam.TOKEN);
		String token = cookie.getValue();
		String alipayUserId = tokenAuthenticationService.getIdByToken(token);
		ReturnDataUtil queryLogin = loginInfoService.queryLogin(loginCode, password, alipayUserId);
		log.info("<LoginController--queryLogin--end>");
		return queryLogin;
	}
	
	/**
	 * 运维身份验证
	 * 
	 * @author hebiting
	 * @date 2018年4月27日下午2:51:54
	 * @param openDoor
	 * @param request
	 * @return
	 */
	@PostMapping("/verifyOperate")
	public ReturnDataUtil verifyOperate(@RequestBody Map<String, String> param,HttpServletRequest request) {
		log.info("<LoginController--verifyOperate--start>");
		String phone = param.get("phone");
		String code = param.get("code");
		String phoneCode = redisService.get(SmsKey.getCodeByMobile, phone);
		String vmCode = param.get("vmCode");
		if (StringUtil.isNotBlank(code) && code.equals(phoneCode)) {
			LoginInfoBean bindLoginInfo = loginInfoService.queryByPhone(phone);
			if (bindLoginInfo != null && loginInfoService.identityAuth(bindLoginInfo, vmCode)) {
				return ResultUtil.success(h5URLConfig.replenishH5Page.concat("?vmCode=").concat(vmCode)
						.concat("&loginCode=").concat(bindLoginInfo.getLoginCode()));
			} else {
				return ResultUtil.error(ResultEnum.NOT_AUTHORIZED, null);
			}
		} else {
			return ResultUtil.error(ResultEnum.VERIFYCODE_UNCORRECT, null);
		}
	}
	
}
