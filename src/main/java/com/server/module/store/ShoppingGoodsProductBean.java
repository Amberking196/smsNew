package com.server.module.store;

import com.server.common.persistence.Entity;
import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: shopping_goods_product author name: why create time: 2018-09-14
 * 09:53:47
 */
@Data
public class ShoppingGoodsProductBean {

	//主键id
	private Long id;
	//商城商品id
	private Long goodsId;
	//商品基础id
	private Long itemId;
	//商品名称
	private String itemName;
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
	//绑定商品数量
	private Long quantity;

}
