package com.server.module.zfb_trade.bean;

import java.util.Date;
import java.util.List;

public class TcpRequest {

	

	/**
	 * 1-RFID,2-桶装水
	 */
	private Integer machienType;
	/**
	 * 出厂编号
	 * */
	private String factoryNumber;
	/**
	 * 门的具体状态
	 * */
	private List<Integer> doorList;

	/**
	 * 0：机器空闲 1：正在开门 2：门已打开 3：门已关上;4:异常
	 */
	private Integer status;

	/**
	 * 0-正常，1-使用备用电源（报警）
	 */
	private Integer powerSupplyStatus;
	/**
	 * 桶装水的机器重量
	 * */
	private List<Double> weightList;
	/**
	 * 校验位，所有数字求和=校验位
	 * */
	private Integer checkBit;
	/**
	 * 心跳时间
	 * */
	private Date heartbeatTime;
	
	/**
	 * 内容
	 * */
	private String content;

	public Integer getMachienType() {
		return machienType;
	}

	public void setMachienType(Integer machienType) {
		this.machienType = machienType;
	}

	public String getFactoryNumber() {
		return factoryNumber;
	}

	public void setFactoryNumber(String factoryNumber) {
		this.factoryNumber = factoryNumber;
	}

	public List<Integer> getDoorList() {
		return doorList;
	}

	public void setDoorList(List<Integer> doorList) {
		this.doorList = doorList;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPowerSupplyStatus() {
		return powerSupplyStatus;
	}

	public void setPowerSupplyStatus(Integer powerSupplyStatus) {
		this.powerSupplyStatus = powerSupplyStatus;
	}

	public List<Double> getWeightList() {
		return weightList;
	}

	public void setWeightList(List<Double> weightList) {
		this.weightList = weightList;
	}

	public Integer getCheckBit() {
		return checkBit;
	}

	public void setCheckBit(Integer checkBit) {
		this.checkBit = checkBit;
	}

	public Date getHeartbeatTime() {
		return heartbeatTime;
	}

	public void setHeartbeatTime(Date heartbeatTime) {
		this.heartbeatTime = heartbeatTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
