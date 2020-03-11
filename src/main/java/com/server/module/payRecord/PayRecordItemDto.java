package com.server.module.payRecord;

import java.math.BigDecimal;

public class PayRecordItemDto {

	private Long basicItemId;
	private String itemName;
	private String pic;
	private Integer num;
	private BigDecimal price;
	private BigDecimal realTotalPrice;
	
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
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
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getRealTotalPrice() {
		return realTotalPrice;
	}
	public void setRealTotalPrice(BigDecimal realTotalPrice) {
		this.realTotalPrice = realTotalPrice;
	}
	
	
}
