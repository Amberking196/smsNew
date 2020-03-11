package com.server.module.carryWaterVouchersManage.carryWaterVouchers;

import java.util.List;
import java.util.Map;

import com.server.module.coupon.MachinesLAC;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-11-03 09:02:25
 */
public interface CarryWaterVouchersDao {
	
	/**
	 * 获取提水券信息
	 * @author why
	 * @date 2018年11月5日 下午2:43:14 
	 * @param id
	 * @return
	 */
	public CarryWaterVouchersBean get(Long id);

	/**
	 * 提水券列表查询
	 * @author why
	 * @date 2018年11月3日 上午9:20:35 
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
