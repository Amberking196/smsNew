package com.server.module.commonBean;

import java.util.Date;

import com.server.common.persistence.NotField;

public class ItemBasicBean {

	//商品id
	private Integer id;
	//类型id
	private Integer typeId;
	//
	private Long loginInfoId;
	//图片地址
	private String pic;
	//商品名称
	private String name;
	//商品拼写
	private String spell;
	//条形码
	private String barCode;
	//状态
	private Integer state;
	//采购方式
	private String purchaseWay;
	//品牌
	private String brand;
	//是否更新
	private Integer isUpdate;
	//
	private String showUrl;
	//单位
	private Integer unit;
	//规格
	private String pack;
	//商品简称
	private String simpleName;
	//规格
	private String standard;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;
	
	//类型
	@NotField
	private  String type;
	//商品单位名称
	@NotField
	private String unitName;
	//商品状态名称
	@NotField
	private String stateName;
	
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getSimpleName() {
		return simpleName;
	}
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public Long getLoginInfoId() {
		return loginInfoId;
	}
	public void setLoginInfoId(Long loginInfoId) {
		this.loginInfoId = loginInfoId;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpell() {
		return spell;
	}
	public void setSpell(String spell) {
		this.spell = spell;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getPurchaseWay() {
		return purchaseWay;
	}
	public void setPurchaseWay(String purchaseWay) {
		this.purchaseWay = purchaseWay;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public Integer getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(Integer isUpdate) {
		this.isUpdate = isUpdate;
	}
	public String getShowUrl() {
		return showUrl;
	}
	public void setShowUrl(String showUrl) {
		this.showUrl = showUrl;
	}
	public Integer getUnit() {
		return unit;
	}
	public void setUnit(Integer unit) {
		this.unit = unit;
	}
	public String getPack() {
		return pack;
	}
	public void setPack(String pack) {
		this.pack = pack;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
