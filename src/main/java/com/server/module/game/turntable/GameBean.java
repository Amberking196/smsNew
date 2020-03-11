package com.server.module.game.turntable;

import java.util.Date;

public class GameBean {
	
	//自增标识
	private Integer id;
	//游戏名称
	private String name;
	//游戏类型
	private Integer type;
	//次数
	private Integer times;
	//开始时间
	private Date startTime;
	//结束时间
	private Date endTime;
	//中奖后的可领取时间
	private Integer canReceive;
	//创建者
	private Long createUser;
	//创建时间
	private Date createTime;
	//修改者
	private Long updateUser;
	//修改时间
	private Date updateTime;
	//删除标识 0：未删除 1：已删除
	private Integer deleteFlag;
	//目标
	private Integer target;
	//公司id
	private Integer companyId;
	//区域id
	private Integer areaId;
	//机器编号
	private String vmCode;
	//是否购买后可参与(0:不需要 1：购买后参与)
	private Integer needGo;
	//奖品数量 6/8
	private Integer prizeNum;
	//游戏所需积分
	private Integer integral;
	
	public Integer getTarget() {
		return target;
	}
	public void setTarget(Integer target) {
		this.target = target;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public Integer getNeedGo() {
		return needGo;
	}
	public void setNeedGo(Integer needGo) {
		this.needGo = needGo;
	}
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getCanReceive() {
		return canReceive;
	}
	public void setCanReceive(Integer canReceive) {
		this.canReceive = canReceive;
	}
	public Long getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getPrizeNum() {
		return prizeNum;
	}
	public void setPrizeNum(Integer prizeNum) {
		this.prizeNum = prizeNum;
	}
	public Integer getIntegral() {
		return integral;
	}
	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	

}
