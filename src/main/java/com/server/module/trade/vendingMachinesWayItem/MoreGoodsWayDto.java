package com.server.module.trade.vendingMachinesWayItem;

import java.util.List;

public class MoreGoodsWayDto {

	private Integer wayNumber;
	private Integer wayWeight;
	private Integer version;
	private List<VmwayItemBean> wayItemList;
	
	
	public Integer getWayNumber() {
		return wayNumber;
	}
	public void setWayNumber(Integer wayNumber) {
		this.wayNumber = wayNumber;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getWayWeight() {
		return wayWeight;
	}
	public void setWayWeight(Integer wayWeight) {
		this.wayWeight = wayWeight;
	}
	public List<VmwayItemBean> getWayItemList() {
		return wayItemList;
	}
	public void setWayItemList(List<VmwayItemBean> wayItemList) {
		this.wayItemList = wayItemList;
	}
	
	
}
