package com.server.module.zfb_trade.bean;


public class TcpCurrentTask {

	/** 开关历史表标识  */
	private DoorOperateHistoryBean doorOperateHistory;
	/** 音量 */
	private Integer volume;
	/** 支付类型 微信-1，支付宝-2  */
	private Integer payType;
	/** 签约号 */
	private String agreementNo;
	public DoorOperateHistoryBean getDoorOperateHistory() {
		return doorOperateHistory;
	}
	public void setDoorOperateHistory(DoorOperateHistoryBean doorOperateHistory) {
		this.doorOperateHistory = doorOperateHistory;
	}
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public String getAgreementNo() {
		return agreementNo;
	}
	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}
	
}
