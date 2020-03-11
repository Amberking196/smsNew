package com.server.module.trade.customer.wxcustomer;

public interface WxCustomerDao {

	/**
	 * 微信用户详细信息新增
	 * @author hebiting
	 * @date 2018年7月10日上午10:33:00
	 * @param wxCustomer
	 * @return
	 */
	public WxCustomerBean insert(WxCustomerBean wxCustomer);
	/**
	 * 微信用户信息更新
	 * @author hebiting
	 * @date 2018年7月10日上午10:33:18
	 * @param wxCustomer
	 * @return
	 */
	public boolean update(WxCustomerBean wxCustomer);
	/**
	 * 微信用户信息更新
	 * @author hjc
	 * @date 2019年6月24日上午10:33:18
	 * @param wxCustomer
	 * @return
	 */
	public boolean updateUnionid(WxCustomerBean wxCustomer);
	/**
	 * 微信用户删除
	 * @author hebiting
	 * @date 2018年7月10日上午10:33:29
	 * @param id
	 * @return
	 */
	public boolean delete(Long id);
	
	/**
	 * 根据openId以及公司id查询微信用户详细信息
	 * @author hebiting
	 * @date 2018年7月9日下午6:34:34
	 * @param openId
	 * @param companyId
	 * @return
	 */
	public WxCustomerBean selectByOpenId(String openId,Integer companyId);
	
	/**
	 * 根据customerId及公司id查询用户详细信信息
	 * @author hebiting
	 * @date 2018年7月9日下午6:35:03
	 * @param customerId
	 * @param companyId
	 * @return
	 */
	public WxCustomerBean selectByCustomerId(Long customerId,Integer companyId);
	
	/**
	 * 查询用户信息是否完整是否需要进行微信授权获取用户详细信息
	 * @author hebiting
	 * @date 2018年8月13日下午4:54:10
	 * @return
	 */
	public boolean needUpdate(String openId);
	
}
