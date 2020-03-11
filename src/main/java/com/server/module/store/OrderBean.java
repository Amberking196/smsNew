package com.server.module.store;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(tableName = "store_order", id = "id", idGenerate = "auto")
public class OrderBean {
   //订单id
	private long id;
   //平台订单号
	private String ptCode;
   //订单类型  1：商城订单；2：售货机订单
	private Integer type;
   //状态
	private Integer state;
   //商品
	private String product;
   //原价格
	private BigDecimal price;
   //现在价格
	private  BigDecimal nowprice;
   //收货地址
	private String location;
   //订单创建时间
	private Date createTime;
   //订单支付时间
	private Date payTime;
   //发货时间
	private Date deliverTime;
   //确认收货时间
	private Date affirmTime;
   //退款时间
	private Date refundTime;
   //售货机编号
	private String vendingMachinesCode;
   //用户id
	private String openid;
   //配送方式
	private Integer distributionModel;
   //优惠券
	private Integer coupon;
   //优惠券扣减金额
	private BigDecimal couponPrice;
   //支付类型
	private Integer payType;
   //ip
	private String ip;
   //微信优惠券Fee
	private Integer wxCouponFee;
   //微信代金券使用数量
	private Integer wxCouponCount;
   //微信充值代金券
	private Integer wxCouponType;
   //微信代金券id
	private String wxCouponId;
   //单个代金券支付金额
	private Integer wxCouponOneFee;
   //支付订单号
	private String payCode;
	//顾客id
	private Long customerId;
	//用户拼团表主键id
	private Long customerGroupId;
	//商品数量
	private Integer num;
	@NotField
	private Long startCustomerId;//团长id
	@NotField
	private String participationCustomerId;//参与者ids
	@NotField
	private Integer minimumGroupSize;//最小成团人数
	@NotField
	private Integer timeLimit;//成团时间限制
	
	@NotField
	private String name;//商品名称
	@NotField
	private Integer typeId;//商品类型
	@NotField
	private Long vouchersId;//单张提水券
	@NotField
	private String vouchersIds;//多张提水券
	//支付状态
	@NotField
	private String stateName;

}
