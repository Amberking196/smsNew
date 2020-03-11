package com.server.module.zfb_trade.module.logininfo;


public interface AliLoginInfoDao {




	/**
	 * 是否为该机器所在线路负责人
	 * @author hebiting
	 * @date 2018年4月27日下午3:42:32
	 * @param loginInfo
	 * @param vmCode
	 * @return
	 */
	boolean isDuty(Long dutyId,String vmCode);
	

	/**
	 * 根据alipayUserId查询工作人员信息
	 * @author hebiting
	 * @date 2018年4月28日下午4:45:55
	 * @param alipayUserId
	 * @return
	 */
	LoginInfoBean findLoginInfo(String alipayUserId);
	/**
	 * 更新工作人员信息aliUserId,updateDate
	 * @author hebiting
	 * @date 2018年4月28日下午5:26:19
	 * @param loginInfo
	 * @return
	 */
	boolean update(LoginInfoBean loginInfo);
	/**
	 * 登录验证
	 * @author hebiting
	 * @date 2018年4月28日下午5:34:45
	 * @param loginCode
	 * @param password
	 * @return
	 */
	LoginInfoBean login(String loginCode,String password);
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
