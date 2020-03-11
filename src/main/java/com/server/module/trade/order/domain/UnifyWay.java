package com.server.module.trade.order.domain;

import java.util.List;

import com.server.module.trade.vendingMachinesWayItem.ItemChangeDto;

public class UnifyWay {

	private Integer wayNumber;// 货道编号
	private Integer wayWeight;
	private String state;
	private List<ItemChangeDto> itemList;

	public Integer getWayNumber() {
		return wayNumber;
	}

	public void setWayNumber(Integer wayNumber) {
		this.wayNumber = wayNumber;
	}

	public Integer getWayWeight() {
		return wayWeight;
	}

	public void setWayWeight(Integer wayWeight) {
		this.wayWeight = wayWeight;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<ItemChangeDto> getItemList() {
		return itemList;
	}

	public void setItemList(List<ItemChangeDto> itemList) {
		this.itemList = itemList;
	}
	
}
