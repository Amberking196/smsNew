package com.server.module.payRecord;

import java.math.BigDecimal;
import java.util.Date;

public class PayRecordBean {

	//自增id
		private Long id;
		//客户标识
		private Long customerId;
		
		private Long itemExtractId;
		//商品标识
		private Long itemId;
		//基础商品标识
		private Long basicItemId;
		//售货机标识
		private String vendingMachinesCode;
		//支付订单标识
		private String payCode;
		//支付类型
		private Integer payType;
		//价格（元）
		private BigDecimal price;
		//状态
		private Integer state;
		//数量
		private Integer num;
		//平台返回的订单号
		private String ptCode;
		//创建时间
		private Date createTime;
		//订单支付时间
		private Date payTime;
		//订单退款时间
		private Date refundTime;
		//订单完成时间
		private Date finishTime;
		//进货价
		private BigDecimal costPrice;
		//退款原因
		private String refundName;
		//订单描述
		private String remark;
		//商品名称
		private String itemName;
		//商品类型标识
		private Integer itemTypeId;
		//货道
		private Integer wayNumber;
		//已出货数
		private Integer pickupNum;
		
		
		public Long getItemExtractId() {
			return itemExtractId;
		}
		public void setItemExtractId(Long itemExtractId) {
			this.itemExtractId = itemExtractId;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Long getCustomerId() {
			return customerId;
		}
		public void setCustomerId(Long customerId) {
			this.customerId = customerId;
		}
		public Long getItemId() {
			return itemId;
		}
		public void setItemId(Long itemId) {
			this.itemId = itemId;
		}
		public Long getBasicItemId() {
			return basicItemId;
		}
		public void setBasicItemId(Long basicItemId) {
			this.basicItemId = basicItemId;
		}
		public String getVendingMachinesCode() {
			return vendingMachinesCode;
		}
		public void setVendingMachinesCode(String vendingMachinesCode) {
			this.vendingMachinesCode = vendingMachinesCode;
		}
		public String getPayCode() {
			return payCode;
		}
		public void setPayCode(String payCode) {
			this.payCode = payCode;
		}
		public Integer getPayType() {
			return payType;
		}
		public void setPayType(Integer payType) {
			this.payType = payType;
		}
		public BigDecimal getPrice() {
			return price;
		}
		public void setPrice(BigDecimal price) {
			this.price = price;
		}
		public Integer getState() {
			return state;
		}
		public void setState(Integer state) {
			this.state = state;
		}
		public Integer getNum() {
			return num;
		}
		public void setNum(Integer num) {
			this.num = num;
		}
		public String getPtCode() {
			return ptCode;
		}
		public void setPtCode(String ptCode) {
			this.ptCode = ptCode;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public Date getPayTime() {
			return payTime;
		}
		public void setPayTime(Date payTime) {
			this.payTime = payTime;
		}
		public Date getRefundTime() {
			return refundTime;
		}
		public void setRefundTime(Date refundTime) {
			this.refundTime = refundTime;
		}
		public Date getFinishTime() {
			return finishTime;
		}
		public void setFinishTime(Date finishTime) {
			this.finishTime = finishTime;
		}
		public BigDecimal getCostPrice() {
			return costPrice;
		}
		public void setCostPrice(BigDecimal costPrice) {
			this.costPrice = costPrice;
		}
		public String getRefundName() {
			return refundName;
		}
		public void setRefundName(String refundName) {
			this.refundName = refundName;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getItemName() {
			return itemName;
		}
		public void setItemName(String itemName) {
			this.itemName = itemName;
		}
		public Integer getItemTypeId() {
			return itemTypeId;
		}
		public void setItemTypeId(Integer itemTypeId) {
			this.itemTypeId = itemTypeId;
		}
		public Integer getWayNumber() {
			return wayNumber;
		}
		public void setWayNumber(Integer wayNumber) {
			this.wayNumber = wayNumber;
		}
		public Integer getPickupNum() {
			return pickupNum;
		}
		public void setPickupNum(Integer pickupNum) {
			this.pickupNum = pickupNum;
		}
		
}
