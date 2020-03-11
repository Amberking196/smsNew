package com.server.module.trade.order.bean;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  pay_record
 * author name: yjr
 * create time: 2018-04-27 09:29:08
 */ 
@Data
@Entity(tableName="pay_record",id="id",idGenerate="auto")
public class PayRecordBean{

	private Long id;
	private Long customerId;
	private Long itemExtractId;
	private Long basicItemId;
	private Long itemId;
	private String payCode;
	private String vendingMachinesCode;
	private Integer payType;
	private BigDecimal price;
	private Long state;
	private Long num;
	private Long pickupNum;
	private String ptCode;
	private Date createTime;
	private Date payTime;
	private Date refundTime;
	private Date finishTime;
	private BigDecimal costPrice;
	private String refundName;
	private String remark;
	private String itemName;
	private Long itemTypeId;
	private Long wayNumber;
	private Long operateHistoryId;
	private String couponId;
	private Integer srcType;//订单来源    1 微信  2 支付宝    新加
	private BigDecimal memberPay;//余额支付金额大小
	@NotField
	private String stateLabel;
	@NotField
	public boolean existsOpenBeforeOrder;
	public Long pid;
}

