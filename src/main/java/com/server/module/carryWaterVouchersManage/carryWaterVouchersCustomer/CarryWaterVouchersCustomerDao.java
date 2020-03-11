package com.server.module.carryWaterVouchersManage.carryWaterVouchersCustomer;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-11-03 16:14:12
 */
public interface CarryWaterVouchersCustomerDao {

	
	/**
	 * 更新用户提水券
	 * @author why
	 * @date 2018年11月5日 上午11:50:31 
	 * @param entity
	 * @return
	 */
	public boolean update(CarryWaterVouchersCustomerBean entity);


	/**
	 * 提水券绑定用户
	 * @author why
	 * @date 2018年11月4日 上午10:03:13 
	 * @param entity
	 * @return
	 */
	public CarryWaterVouchersCustomerBean insert(CarryWaterVouchersCustomerBean entity);
	
	/**
	 * 获取用户绑定提水券信息
	 * @author why
	 * @date 2018年11月5日 上午11:45:28 
	 * @param customerId
	 * @param carryId
	 * @return
	 */
	public CarryWaterVouchersCustomerBean  getCarryWaterVouchersCustomerBean(Long customerId,Long carryId);
	
	/**
	 * 使用用户提水券后，更新
	 * @author hebiting
	 * @date 2018年11月7日上午11:35:25
	 * @param id
	 * @param useNum
	 * @return
	 */
	public boolean reduceCarry(Long id ,Integer useNum);

	/**我的提水券
	 * @param:carryWaterVouchersCustomerForm
	 * @return:
	 * @auther: why
	 * @date: 2018/12/1 10:45
	 */
	public List<CarryWaterVouchersCustomerDto> myCarryWaterVouchers(CarryWaterVouchersCustomerForm carryWaterVouchersCustomerForm);

	/**
	 * 用户退款后删除用户提水券
	 * @author why
	 * @date 2019年3月22日 上午10:28:07 
	 * @param ordeId
	 * @return
	 */
	public boolean delete(Long orderId);
}
