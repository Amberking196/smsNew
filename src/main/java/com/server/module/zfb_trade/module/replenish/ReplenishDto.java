package com.server.module.zfb_trade.module.replenish;

public class ReplenishDto {

	//补货数量
	private Integer replenishNum;
	//商品名称
	private String itemName;
	//商品基础id
	private Integer basicItemId;
	//图片地址
	private String pic;
	
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Integer getReplenishNum() {
		return replenishNum;
	}
	public void setReplenishNum(Integer replenishNum) {
		this.replenishNum = replenishNum;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Integer getBasicItemId() {
		return basicItemId;
	}
	public void setBasicItemId(Integer basicItemId) {
		this.basicItemId = basicItemId;
	}
	
	
}
