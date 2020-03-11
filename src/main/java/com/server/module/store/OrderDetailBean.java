package com.server.module.store;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
@Data
public class OrderDetailBean {

	private Long id;
	private Long orderId;
	private Integer itemId;
	private String itemName;
	private BigDecimal price;
	private Integer num;
	private Date createTime;
	private Long customerId; 
	private Integer pickNum;
	private Integer distributionModel;
	private String pic;
}
