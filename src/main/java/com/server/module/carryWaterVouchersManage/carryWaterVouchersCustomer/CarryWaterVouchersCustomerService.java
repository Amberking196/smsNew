package com.server.module.carryWaterVouchersManage.carryWaterVouchersCustomer;

import java.util.List;

/**
 * author name: why create time: 2018-11-03 16:14:12
 */
public interface CarryWaterVouchersCustomerService {


	/**
	 * 更新用户提水券
	 * @author why
	 * @date 2018年11月5日 下午2:55:11 
	 * @param entity
	 * @return
	 */
	public boolean update(CarryWaterVouchersCustomerBean entity);

	

	
	/**
	 * 提水券绑定用户
	 * @author why
	 * @date 2018年11月5日 下午5:42:44 
	 * @param carryId
	 * @param customerId
	 * @param num
	 * @return
	 */
	public CarryWaterVouchersCustomerBean add(Long carryId,Long customerId,Integer num,Long orderId);

	/**我的提水券
	 * @param:carryWaterVouchersCustomerForm
	 * @return:
	 * @auther: why
	 * @date: 2018/12/1 10:48
	 */
	public List<CarryWaterVouchersCustomerDto> myCarryWaterVouchers(CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm);
	
	/**
	 * 用户退款后删除用户提水券
	 * @author why
	 * @date 2019年3月22日 上午10:46:50 
	 * @param orderId
	 * @return
	 */
	public boolean delete(Long orderId);

}
