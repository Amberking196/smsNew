package com.server.module.zfb_trade.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CookieUtil {

	private static Log log = LogFactory.getLog(CookieUtil.class);

	/** 定义Cookie名称的静态内部类 */
	public static class CookieParam {
		/** 定义存放在Cookie中用户登录票据的名称 */
		public static final String TOKEN = "alipay_token";
		// 过期时间
		public static final int EXPIRE = 30 * 60;
	}

	/**
	 * 根据cookieName获取请求中的cookie
	 * 
	 * @author hebiting
	 * @date 2018年4月24日上午10:04:03
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request, String cookieName) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals(cookieName)) {
					return cookie;
				}
			}
		}
		return null;
	}

	public static void addCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue, int maxAge, boolean encoded) {
		if (cookieValue == null) {
			cookieValue = "";
		}
		if (encoded) {
			try {
				cookieValue = new String(cookieValue.getBytes(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Cookie cookie = getCookieByName(request, cookieName);
		if (cookie == null) {
			cookie = new Cookie(cookieName, cookieValue);
		}
		cookie.setValue(cookieValue);
		cookie.setMaxAge(maxAge);
		cookie.setPath("/");
		cookie.setDomain("youshuidaojia.com");
		log.info("cookie:" + JsonUtil.toJson(cookie));
		response.addCookie(cookie);

	}

	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
		addCookie(request, response, cookieName, null, 0, false);
	}
}
