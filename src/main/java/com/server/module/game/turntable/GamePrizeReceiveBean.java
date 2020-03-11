package com.server.module.game.turntable;

import java.util.Date;

public class GamePrizeReceiveBean {

	//自增标识
	private Long id;
	//奖品id
	private Long gamePrizeId;
	//顾客id
	private Long customerId;
	//中奖时间
	private Date createTime;
	//截止领取时间
	private Date endTime;
	//领取时间
	private Date receiveTime;
	//用户地址id
	private Long addressId;
	
	
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getGamePrizeId() {
		return gamePrizeId;
	}
	public void setGamePrizeId(Long gamePrizeId) {
		this.gamePrizeId = gamePrizeId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	
	
}
