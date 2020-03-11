package com.server.util.stateEnum;
/**
 * 机器故障类型
 * @author hebiting
 *
 */
public enum DistroyTypeEnum {

	MACHINES_OFF_LINE(1,"机器掉线"),
	MACHINES_OPEN_DOOR_ADNORMAL(2,"机器开门异常"),
	MACHINES_WEIGH_ERROR(3,"称重故障"),
	MACHINES_LOCK_BREAKDOWN(4,"%s号门锁异常")
	;
	private Integer status;
	private String info;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	DistroyTypeEnum(Integer status,String info){
		this.status = status;
		this.info = info;
	}
}
