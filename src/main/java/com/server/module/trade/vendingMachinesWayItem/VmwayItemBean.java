package com.server.module.trade.vendingMachinesWayItem;

import java.util.Date;

import com.server.common.persistence.NotField;

public class VmwayItemBean {

	private Long id;
	private Long machineWayId;
	private String vmCode;
	private Integer wayNumber;
	private Long basicItemId;
	private String itemName;
	private String pic;
	private Integer weight;
	private Integer orderNumber;
	private Double price;
	private Integer num;
	private Integer fullNum;
	private Double promotionPrice;
	private Date updateTime;
	private Date createTime;
	private String img;
	@NotField
	public String mainProgramVersion;
	
	public String getMainProgramVersion() {
		return mainProgramVersion;
	}
	public void setMainProgramVersion(String mainProgramVersion) {
		this.mainProgramVersion = mainProgramVersion;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMachineWayId() {
		return machineWayId;
	}
	public void setMachineWayId(Long machineWayId) {
		this.machineWayId = machineWayId;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public Integer getWayNumber() {
		return wayNumber;
	}
	public void setWayNumber(Integer wayNumber) {
		this.wayNumber = wayNumber;
	}
	public Long getBasicItemId() {
		return basicItemId;
	}
	public void setBasicItemId(Long basicItemId) {
		this.basicItemId = basicItemId;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public Integer getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getFullNum() {
		return fullNum;
	}
	public void setFullNum(Integer fullNum) {
		this.fullNum = fullNum;
	}
	public Double getPromotionPrice() {
		return promotionPrice;
	}
	public void setPromotionPrice(Double promotionPrice) {
		this.promotionPrice = promotionPrice;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
