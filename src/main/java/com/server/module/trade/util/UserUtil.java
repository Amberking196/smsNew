package com.server.module.trade.util;

import com.server.module.trade.auth.UserVo;

//import plm.common.exceptions.UnloginException;

public class UserUtil {
	private final static ThreadLocal<UserVo> tlUser = new ThreadLocal<UserVo>();
	public static final String KEY_USER = "user";

	public static void setUser(UserVo vo) {
		tlUser.set(vo);
		//tlUser.

		// 把用户信息放到log4j
		//MDC.put(KEY_USER, userid);
	}

	/**
	 * 如果没有登录，返回null
	 * 
	 * @return
	 */
	/*public static String getUserIfLogin() {
		return tlUser.get();
	}*/

	/**
	 * 如果没有登录会抛出异常
	 * 
	 * @return
	 */
	public static UserVo getUser() {
		UserVo vo = tlUser.get();
		if(vo==null){
			
		}

		/*if (user == null) {
			throw new UnloginException();
		}*/

		return vo;
	}

	/*public static void setLocale(String locale) {
		setLocale(new Locale(locale));
	}

	*/

	public static void clearAllUserInfo() {
		tlUser.remove();
		//.remove();

	//	MDC.remove(KEY_USER);
	}
}
