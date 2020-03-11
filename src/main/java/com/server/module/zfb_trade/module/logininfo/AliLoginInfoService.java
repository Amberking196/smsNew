package com.server.module.zfb_trade.module.logininfo;

import com.server.util.ReturnDataUtil;

public interface AliLoginInfoService {

	/**
	 * 登录校验
	 * @author hebiting
	 * @date 2018年4月27日下午3:31:20
	 * @param loginCode
	 * @param password
	 * @param aliUserId
	 * @param vmCode
	 * @return
	 */
	ReturnDataUtil doLogin(String loginCode,String password, String aliUserId,String vmCode);
	
	/**
	 * @Description: 身份认证 
	 */
	boolean identityAuth(LoginInfoBean loginInfo,String vmCode);
	
	/**
	 * @Title: queryLogin 
	 * @Description: 查询售水机列表登录校验
	 */
	ReturnDataUtil queryLogin(String loginCode, String password, String alipayUserId);
	/**
	 * 根据alipayUserId获取工作人员信息
	 * @author hebiting
	 * @date 2018年4月28日下午4:43:52
	 * @return
	 */
	LoginInfoBean findLoginInfo(String alipayUserId);
	
	/**
	 * 根据手机号码查询登陆人信息
	 * @author hebiting
	 * @date 2018年5月12日下午3:18:12
	 * @param phone
	 * @return
	 */
	LoginInfoBean queryByPhone(String phone);
	
	
	/**
	 * 根据id查询登陆人信息
	 * @author hebiting
	 * @date 2018年5月12日下午3:18:12
	 * @param phone
	 * @return
	 */
	LoginInfoBean queryById(Long id);
}
