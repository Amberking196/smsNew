package com.server.module.carryWaterVouchersManage.carryWaterVouchers;

import java.util.List;

import com.server.module.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersForm;
import com.server.util.ReturnDataUtil;
import java.util.Map;


import com.server.module.coupon.MachinesLAC;

/**
 * author name: why create time: 2018-11-03 09:02:25
 */
public interface CarryWaterVouchersService {

	
	/***
	 * 获取提水券信息
	 * @author why
	 * @date 2018年11月5日 下午2:45:11 
	 * @param id
	 * @return
	 */
	public CarryWaterVouchersBean get(Long id);

	/**
	 * 提水券列表
	 * 
	 * @author hjc
	 * @date 2018年11月6日 上午9:37:11
	 * @param carryWaterVouchersForm
	 * @return
	 */
	public ReturnDataUtil listPage(CarryWaterVouchersForm carryWaterVouchersForm);

	/**
	 * 获取用户可用提水券
	 * @author hebiting
	 * @date 2018年11月6日下午6:21:02
	 * @param customerId
	 * @param mlac
	 * @param productIds
	 * @return
	 */
	public Map<CarryDto,List<Long>> getCarryWaterVoucherByCustomer(Long customerId,MachinesLAC mlac, String productIds);
	

}
