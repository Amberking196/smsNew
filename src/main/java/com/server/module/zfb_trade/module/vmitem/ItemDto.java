package com.server.module.zfb_trade.module.vmitem;

import java.math.BigDecimal;

public class ItemDto {

	/**
	 * 商品名
	 */
	private String itemName;
	/**
	 * 价格
	 */
	private BigDecimal price;
	/**
	 * 图片路径
	 */
	private String imageUrl;
	/**
	 * 门号
	 */
	private Integer doorNO;
	//剩余商品数
	private Integer num;
	
	private Integer fullNum;
	//货道状态（30001：货道可用，30003：货道不可用）
	private Integer state;
	
	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getFullNum() {
		return fullNum;
	}
	public void setFullNum(Integer fullNum) {
		this.fullNum = fullNum;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Integer getDoorNO() {
		return doorNO;
	}
	public void setDoorNO(Integer doorNO) {
		this.doorNO = doorNO;
	}
	
	
}
