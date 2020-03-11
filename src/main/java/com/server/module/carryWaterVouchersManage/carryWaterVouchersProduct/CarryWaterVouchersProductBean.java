package com.server.module.carryWaterVouchersManage.carryWaterVouchersProduct;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: carry_water_vouchers_product author name: why create time:
 * 2018-11-03 16:25:36
 */
@Data
@Entity(tableName = "carry_water_vouchers_product", id = "id", idGenerate = "auto")
public class CarryWaterVouchersProductBean {

	// 主键id
	private Long id;
	// 提水券id
	private Long carryId;
	// 商品基础信息id
	private Long itemId;
	// 创建时间
	private Date createTime;
	// 创建用户
	private Long createUser;
	// 更新时间
	private Date updateTime;
	// 更新用户
	private Long updateUser;
	// 是否删除 0 未删除 1已删除
	private Integer deleteFlag;
	
	//商品名称
	@NotField
	private String itemName;
	//是否绑定 
	@NotField
	private String bindLabel;

}
