package com.server.module.zfb_trade.bean;

public class OpenDoorBean {

	/**
	 * 开门类型
	 */
	private Integer openType;
	/**
	 * 开门号
	 */
	private Integer doorNO;
	/**
	 * 售货机编号
	 */
	private String vmCode;
	/**
	 * 音量
	 */
	private Integer volume;
	/**
	 * 检验位
	 */
	private Integer checkBit;
	public Integer getOpenType() {
		return openType;
	}
	public void setOpenType(Integer openType) {
		this.openType = openType;
	}
	public Integer getDoorNO() {
		return doorNO;
	}
	public void setDoorNO(Integer doorNO) {
		this.doorNO = doorNO;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	public Integer getCheckBit() {
		return checkBit;
	}
	public void setCheckBit(Integer checkBit) {
		this.checkBit = checkBit;
	}
	
	
}
