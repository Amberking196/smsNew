package com.server.module.payRecord;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomerMachineHistoryBean {
	//货道号
	private Integer doorNO;
	//开门前重量
	private double  preWeight;
	//开门后重量
	private double posWeight;
	//够买数量
	private Integer num;
	//开门前的数量
	private Integer preNum;
	//关门后的数量
	private Integer posNum;
	
	public Integer getPreNum() {
		return preNum;
	}
	public void setPreNum(Integer preNum) {
		this.preNum = preNum;
	}
	public Integer getPosNum() {
		return posNum;
	}
	public void setPosNum(Integer posNum) {
		this.posNum = posNum;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	//开门时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private Date openedTime;
	//关门时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08")
    private Date closedTime;
	public Integer getDoorNO() {
		return doorNO;
	}
	public void setDoorNO(Integer doorNO) {
		this.doorNO = doorNO;
	}
	public double getPreWeight() {
		return preWeight;
	}
	public void setPreWeight(double preWeight) {
		this.preWeight = preWeight;
	}
	public double getPosWeight() {
		return posWeight;
	}
	public void setPosWeight(double posWeight) {
		this.posWeight = posWeight;
	}
	
	public Date getOpenedTime() {
		return openedTime;
	}
	public void setOpenedTime(Date openedTime) {
		this.openedTime = openedTime;
	}
	public Date getClosedTime() {
		return closedTime;
	}
	public void setClosedTime(Date closedTime) {
		this.closedTime = closedTime;
	}  
}
