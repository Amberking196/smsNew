package com.server.module.zfb_trade.module.customer;

import java.util.List;

public interface AliCustomerService {

	/**
	 * 通过AlipayUserId查询 
	 * @author hebiting
	 * @date 2018年5月27日下午8:39:08
	 * @param customer
	 * @return
	 */
	public CustomerBean queryByAliId(CustomerBean customer);
	
	/**
	 * 保存顾客信息
	 * @author hebiting
	 * @date 2018年5月27日下午8:34:31
	 * @param customer
	 * @return
	 */
	public Long saveCustomer(CustomerBean customer);
	
	/**
	 * 更新顾客信息
	 * @author hebiting
	 * @date 2018年5月27日下午8:34:42
	 * @param customer
	 * @return
	 */
	public boolean updateCustomer(CustomerBean customer);
	/**
	 * 根据id查询顾客信息
	 * @author hebiting
	 * @date 2018年5月27日下午8:34:52
	 * @param id
	 * @return
	 */
	public CustomerBean queryById(Long id);
	/**
	 * 根据顾客id的集合查询相关的顾客信息
	 * @author hebiting
	 * @date 2018年5月27日下午8:35:10
	 * @param customerIdList
	 * @return
	 */
	List<CustomerBean> queryCustomer(List<Long> customerIdList);
	
	/**
	 * 根据phone查询顾客信息
	 * @author hebiting
	 * @date 2018年5月27日下午8:34:52
	 * @param id
	 * @return
	 */
	public CustomerBean queryByPhone(String phone);
	
	/**
	 * 根据id删除当前用户
	 * @author hebiting
	 * @date 2018年7月4日下午3:16:36
	 * @param id
	 * @return
	 */
	public boolean delCustomer(Long id);
	
	/**
	 * 根据phone查询顾客是否自己人
	 * @author hjc
	 * @date 2019年5月31日下午8:34:52
	 * @param id
	 * @return
	 */
	public boolean queryIsLoginUserByPhone(String phone);
}
