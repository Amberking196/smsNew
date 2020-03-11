package com.server.module.trade.customer.wxcustomer;

public interface WxCustomerService {
	
	/**
	 * 微信用户信息更新
	 * @author hebiting
	 * @date 2018年7月10日上午10:33:18
	 * @param wxCustomer
	 * @return
	 */
	public boolean update(WxCustomerBean wxCustomer);
	
	/**
	 * 微信用户信息更新 unionid
	 * @author hjc
	 * @date 2019年6月24日上午10:33:18
	 * @param wxCustomer
	 * @return
	 */
	public boolean updateUnionid(WxCustomerBean wxCustomer);
	
	/**
	 * 查询用户信息是否完整是否需要进行微信授权获取用户详细信息
	 * @author hebiting
	 * @date 2018年8月13日下午4:54:10
	 * @return
	 */
	public boolean needUpdate(String openId);

	/**
	 * 微信用户详细信息新增
	 * @author hjc
	 * @date 2018年9月18日上午10:33:00
	 * @param wxCustomer
	 * @return
	 */
	public WxCustomerBean insert(WxCustomerBean wxCustomer);
	
	/**
	 * 微信用户详细信息查询
	 * @author hjc
	 * @date 2018年12月4日上午10:33:00
	 * @param openId companyId
	 * @return
	 */
	public WxCustomerBean selectByOpenId(String openId, Integer companyId) ;

}
