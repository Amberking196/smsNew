package com.server.module.commonBean;

import java.math.BigDecimal;

public class ItemInfoDto {

	private Long basicItemId;
	private String itemName;
	private BigDecimal unitPrice;
	private String pic;
	private Integer unitWeight;
	private Integer num;//机器当前改商品库存
	private Integer orderNum;
	
	
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getUnitWeight() {
		return unitWeight;
	}
	public void setUnitWeight(Integer unitWeight) {
		this.unitWeight = unitWeight;
	}
	public Long getBasicItemId() {
		return basicItemId;
	}
	public void setBasicItemId(Long basicItemId) {
		this.basicItemId = basicItemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	
}
