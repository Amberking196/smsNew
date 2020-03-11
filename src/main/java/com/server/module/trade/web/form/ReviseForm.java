package com.server.module.trade.web.form;

public class ReviseForm {

	//机器编码
	private String vmCode;
	//货道号
	private Integer wayNum;
	//数量
	private String num;
	//次数
	private Integer times;
	//出厂编号
	private String factoryNumber;
	//校验码
	private String checkCode;
	//单品重量
	private String unitWeight;
	
	private String orderNumber;
	
	
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getUnitWeight() {
		return unitWeight;
	}
	public void setUnitWeight(String unitWeight) {
		this.unitWeight = unitWeight;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public Integer getWayNum() {
		return wayNum;
	}
	public void setWayNum(Integer wayNum) {
		this.wayNum = wayNum;
	}
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	public String getFactoryNumber() {
		return factoryNumber;
	}
	public void setFactoryNumber(String factoryNumber) {
		this.factoryNumber = factoryNumber;
	}
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	
	
}
