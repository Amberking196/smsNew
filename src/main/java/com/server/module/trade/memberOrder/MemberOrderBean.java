package com.server.module.trade.memberOrder;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
/**
 * table name:  member_order
 * author name: why
 * create time: 2018-09-26 14:36:43
 */
@Data
@Entity(tableName="member_order",id="id",idGenerate="auto")
public class MemberOrderBean{

	//主键id
	private Long id;
	//会员id
	private Long customerId;

	//购买金额
	private BigDecimal price;
	//状态
	private Long state;
	//平台订单号
	private String ptCode;
	//支付订单号
	private String payCode;
	//创建时间
	private Date createTime;
	//订单支付时间
	private Date payTime;
	//更新时间
	private Date updateTime;
	//好友用户id
	private  Long friendCustomerId;
	//好友手机号
	private String friendPhone;

	private Integer type;// 0 正常充值 1捐献

	private String vmCode;//机器编码

	private Integer payType;//支付方式

	private String wayNumber;//货道
	//开始时间
	@NotField
	private String startTime;
	//结束时间
	@NotField
	private String endTime;
	//有效期 1: 一个月 2：一年
	@NotField
	private Integer validity;


}

