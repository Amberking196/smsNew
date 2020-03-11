package com.server.module.carryWaterVouchersManage.carryWaterVouchersCustomer;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: carry_water_vouchers_customer author name: why create time:
 * 2018-11-03 16:14:12
 */
@Data
@Entity(tableName = "carry_water_vouchers_customer", id = "id", idGenerate = "auto")
public class CarryWaterVouchersCustomerBean {

	//主键id
	private Long id;
	//提水券id
	private Long carryId;
	//用户id
	private Long customerId;
	//用户拥有数量
	private Long quantity;
	//用户使用数量
	private String useQuantity;
	//开始时间
	private Date startTime;
	//结束时间
	private Date endTime;
	//创建时间
	private Date createTime;
	//创建用户
	private Long createUser;
	//更新时间
	private Date updateTime;
	//更新用户
	private Long updateUser;
	//是否删除 0 未删除  1 已删除
	private Integer deleteFlag;
	//订单id
	private Long orderId;
	
	//用户电话
	@NotField
	private String phone;
	//用户注册时间
	@NotField
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date userCreateTime;
	

}
