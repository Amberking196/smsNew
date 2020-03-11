package com.server.module.zfb_trade.module.vmway;

import java.util.Date;

public class VendingMachinesWayBean {

	private Long id;
	
	private String vendingMachinesCode;
	
	private Integer wayNumber;
	
	private Long itemId;

	private Integer state;

	private Integer num;

	private Integer fullNum;

	private Date createTime;

	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVendingMachinesCode() {
		return vendingMachinesCode;
	}

	public void setVendingMachinesCode(String vendingMachinesCode) {
		this.vendingMachinesCode = vendingMachinesCode;
	}

	public Integer getWayNumber() {
		return wayNumber;
	}

	public void setWayNumber(Integer wayNumber) {
		this.wayNumber = wayNumber;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
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

	public Integer getFullNum() {
		return fullNum;
	}

	public void setFullNum(Integer fullNum) {
		this.fullNum = fullNum;
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
