package com.server.module.carryWaterVouchersManage.carryWaterVouchersCustomer;

import java.util.Date;

import lombok.Data;

@Data
public class CarryWaterVouchersCustomerDto {

	private Integer id;
	// 提水券id
	private Long carryId;
	// 用户id
	private Long customerId;
	// 用户拥有剩余数量
	private Long surplusQuantity;
	//用户拥有数量
	private Long quantity;
	//使用数量
	private Long useQuantity;
	// 开始时间
	private Date startTime;
	// 结束时间
	private Date endTime;
	// 提水券名称
	private String carryName;
	// 说明
	private String remark;
	
	private Integer isShow;

}
