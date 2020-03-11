package com.server.module.zfb_trade.aspect;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.server.module.zfb_trade.util.JsonUtil;


@Aspect
@Component
public class NoAuthorizeAspect {

	private static Logger log = LogManager.getLogger(NoAuthorizeAspect.class);
	@Pointcut("execution(* com.server.module.zfb_trade.controller.Gate*.*(..))"
			+ "|| execution(* com.server.module.zfb_trade.controller.Login*.*(..))"
			+ "|| execution(* com.server.module.zfb_trade.controller.User*.*(..))"
			+ "|| execution(* com.server.module.zfb_trade.controller.AliSms*.*(..))"
			+ "|| execution(* com.server.module.zfb_trade.controller.AlipayNotify*.*(..))")
	public void verify(){}
	
	@Before("verify()")
	public void doVerify(){
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		//log.info("-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-");
		log.info("请求地址="+request.getRequestURL());
		log.info("请求参数="+JsonUtil.toJson(request.getParameterMap()));
	}
}
