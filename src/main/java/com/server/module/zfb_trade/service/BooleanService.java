package com.server.module.zfb_trade.service;


import javax.servlet.http.HttpServletRequest;

import com.server.module.zfb_trade.module.logininfo.LoginInfoBean;

public interface BooleanService {




	/**
	 * @Description: 判断运维人员是否绑定用户
	 */
	public LoginInfoBean isBindLoginInfo(HttpServletRequest request);
}
