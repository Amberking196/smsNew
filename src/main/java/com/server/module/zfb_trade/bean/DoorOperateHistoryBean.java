package com.server.module.zfb_trade.bean;

import java.util.Date;

public class DoorOperateHistoryBean {

	private Long id;

	private Long referenceId;
	
	private Long basicItemId;

	private Integer status;
	private Integer doorNO;

	private String vmCode;
	private Integer openType;

	private Integer preWeight;
	private Integer posWeight;
	
	private Date createTime;
	private Date openedTime;
	private Date closedTime;
	private Date openFailedTime;
	private String remark;
	
	private Integer normalFlag;
	
	
	public Integer getNormalFlag() {
		return normalFlag;
	}
	public void setNormalFlag(Integer normalFlag) {
		this.normalFlag = normalFlag;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}
	public Long getBasicItemId() {
		return basicItemId;
	}
	public void setBasicItemId(Long basicItemId) {
		this.basicItemId = basicItemId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public Integer getOpenType() {
		return openType;
	}
	public void setOpenType(Integer openType) {
		this.openType = openType;
	}
	public Integer getPreWeight() {
		return preWeight;
	}
	public void setPreWeight(Integer preWeight) {
		this.preWeight = preWeight;
	}
	public Integer getPosWeight() {
		return posWeight;
	}
	public void setPosWeight(Integer posWeight) {
		this.posWeight = posWeight;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	public Date getOpenFailedTime() {
		return openFailedTime;
	}
	public void setOpenFailedTime(Date openFailedTime) {
		this.openFailedTime = openFailedTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
