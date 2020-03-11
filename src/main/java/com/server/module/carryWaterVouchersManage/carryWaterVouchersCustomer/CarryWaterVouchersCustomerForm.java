package com.server.module.carryWaterVouchersManage.carryWaterVouchersCustomer;

import com.server.module.commonBean.PageAssist;
import lombok.Data;

/**
 * table name: carry_water_vouchers_customer author name: why create time:
 * 2018-11-03 16:14:12
 */
@Data
public class CarryWaterVouchersCustomerForm extends PageAssist {

	//提水券id
	private Long carryId;
	//用户手机号
	private String phone;
	//状态 1 未使用  2 已使用   3 已过期
	private Integer state;
}
