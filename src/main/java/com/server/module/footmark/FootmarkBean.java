package com.server.module.footmark;

import java.util.Date;

import com.server.module.trade.auth.UserVo;
import com.server.util.stateEnum.ModeEnum;

public class FootmarkBean {

	private Long id;
	//
	private Long userId;
	//用户类型 1：顾客，2：运维
	private Integer userType;
	//机器编号
	private String vmCode;
	//模式：1：登录，2：取/补货，3：校准，4：校准所有,5:重启模式,6:调整音量,7:其他
	private Integer mode;
	//登录Id
	private Long enterId;
	//创建时间
	private Date createTime;
	//货道
	private String wayNum; 
	
	public String getWayNum() {
		return wayNum;
	}
	public void setWayNum(String wayNum) {
		this.wayNum = wayNum;
	}
	public Long getEnterId() {
		return enterId;
	}
	public void setEnterId(Long enterId) {
		this.enterId = enterId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public FootmarkBean(){}
	public FootmarkBean(UserVo user,ModeEnum mode,String vmCode){
		this.vmCode = vmCode;
		this.userId = user.getId();
		this.mode = mode.getState();
		this.userType = user.getType();
	}
	public FootmarkBean(UserVo user,ModeEnum mode,String vmCode,Long enterId){
		this.vmCode = vmCode;
		this.userId = user.getId();
		this.mode = mode.getState();
		this.userType = user.getType();
		this.enterId = enterId;
	}
	public FootmarkBean(UserVo user,ModeEnum mode,String vmCode,String wayNum,Long enterId){
		this.vmCode = vmCode;
		this.userId = user.getId();
		this.mode = mode.getState();
		this.wayNum = wayNum;
		this.userType = user.getType();
		this.enterId = enterId;
	}
}
