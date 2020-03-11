package com.server.module.trade.customer.address;

import java.util.List;

public interface AddressService {

	/**
	 * 增加用户地址
	 * @author hebiting
	 * @date 2018年7月6日上午8:49:46
	 * @param address
	 * @return
	 */
	AddressBean insert(AddressBean address);
	/**
	 * 修改用户地址信息
	 * @author hebiting
	 * @date 2018年7月6日上午8:49:57
	 * @param address
	 * @return
	 */
	boolean update(AddressBean address);
	/**
	 * 查询用户地址信息
	 * @author hebiting
	 * @date 2018年7月6日上午8:50:06
	 * @param customerId
	 * @return
	 */
	List<AddressBean> select(Long customerId);
	
	/**
	 * 根据id查询用户地址信息
	 * @author hebiting
	 * @date 2018年7月6日上午8:50:06
	 * @param customerId
	 * @return
	 */
	AddressBean selectById(Long customerId,Integer id);
	/**
	 * 删除用户地址信息
	 * @author hebiting
	 * @date 2018年7月6日上午8:50:20
	 * @param addressId
	 * @return
	 */
	boolean delete(Long addressId);
	
	/**
	 * 重置用户的默认地址
	 * @author hebiting
	 * @date 2018年7月6日上午10:29:21
	 * @param customerId
	 * @return
	 */
	boolean updateDefaultFlag(Long customerId);
}
