package com.server.module.trade.vendingMachinesWayItem;

public class ItemChangeDto {
	
	//机器编码
	private String vmCode;
	//货道号
	private Integer wayNum;
	//同一货道的序号
	private Integer orderNum;
	//商品改变数量，拿取为负数，添加为正数
	private Integer changeNum;
	//基础商品id
	private Long basicItemId;
	//商品名称
	private String itemName;
	//图片地址
	private String pic;
	//单价
	private Double price;
	//货道重量
	private Integer weight;
	//货道商品数量
	private Integer num;
	//货道id
	private Long machineWayId;
	//促销价
	private Double promotionPrice;
	//该商品实际支付金额
	private Double realTotalPrice;
	//商品类型Id
	private Long itemTypeId;
	//进货价（已废弃）
	private Double costPrice;
	//实际数量(正负数)
	private Integer realNum;
	
	public Integer getRealNum() {
		return realNum;
	}
	public void setRealNum(Integer realNum) {
		this.realNum = realNum;
	}
	public Double getRealTotalPrice() {
		return realTotalPrice;
	}
	public void setRealTotalPrice(Double realTotalPrice) {
		this.realTotalPrice = realTotalPrice;
	}
	public Long getItemTypeId() {
		return itemTypeId;
	}
	public void setItemTypeId(Long itemTypeId) {
		this.itemTypeId = itemTypeId;
	}
	public Double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Long getMachineWayId() {
		return machineWayId;
	}
	public void setMachineWayId(Long machineWayId) {
		this.machineWayId = machineWayId;
	}
	public Double getPromotionPrice() {
		return promotionPrice;
	}
	public void setPromotionPrice(Double promotionPrice) {
		this.promotionPrice = promotionPrice;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
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
	public Integer getWayNum() {
		return wayNum;
	}
	public void setWayNum(Integer wayNum) {
		this.wayNum = wayNum;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public Integer getChangeNum() {
		return changeNum;
	}
	public void setChangeNum(Integer changeNum) {
		this.changeNum = changeNum;
	}
}
