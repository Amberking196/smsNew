package com.server.module.store;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import com.server.util.StringUtil;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: tbl_customer_spellGroup author name: why create time: 2018-10-17
 * 11:06:09
 */
@Data
@Entity(tableName = "tbl_customer_spellGroup", id = "id", idGenerate = "auto")
public class TblCustomerSpellGroupBean {

	//主键id
	private Long id;
	//商品id
	private Long goodsId;
	//发起拼团人id
	private Long startCustomerId;
	//参与拼图人id
	private String participationCustomerId;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;
	//
	private Date endTime;

	//状态
	private Integer state;
	//拼团活动id（shopping_goods_spellgroup表id）
	private Long spellGroupId;
	
	//用户手机号
	@NotField
	private String phone;
	//参团人数
	@NotField
	private Integer participationNUm;
	//商品成团总人数
	@NotField
	private Integer totalStaff;
	

}
