package com.server.module.commonBean;

import java.util.Date;

public class LoginInfoBean {

	//标识id
	private Long id;
	//登录账号
	private String loginCode;
	//密码
	private String password;
	//状态 1=可用 2=禁用	
	private Integer status;
	//名字
	private String name;
	//雇用时间
	private Date hireDate;
	//
	private String codeNo;
	//角色 可以拥有多个角色
	private String role;
	//公司
	private Integer companyId;
	//手机号码
	private String phone;
	//登录类型
	private Integer loginModel;
	//邮箱	
	private String mail;
	//创建人
	private Long founder;
	//更新时间
	private Date updateDate;
	//额外显示的菜单
	private String extraMenu;
	//公司权限
	private String rightsCompany;
	//机器权限
	private String rightsMachines;
	//线路权限
	private String rightsLine;
	//按纽权限
	private String rightsButton;
	//找回密码的密钥
	private String validataCode;
	//找回密码的过期时间
	private Date outDate;
	//禁用的售货机编号
	private String banVmCode;
	//
	private String openId;
	//
	private String aliUserId;
	//
	private Character isPrincipal;
	//
	private Integer departMent;
	//
	private Integer post;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLoginCode() {
		return loginCode;
	}
	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getHireDate() {
		return hireDate;
	}
	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}
	public String getCodeNo() {
		return codeNo;
	}
	public void setCodeNo(String codeNo) {
		this.codeNo = codeNo;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getLoginModel() {
		return loginModel;
	}
	public void setLoginModel(Integer loginModel) {
		this.loginModel = loginModel;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public Long getFounder() {
		return founder;
	}
	public void setFounder(Long founder) {
		this.founder = founder;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getExtraMenu() {
		return extraMenu;
	}
	public void setExtraMenu(String extraMenu) {
		this.extraMenu = extraMenu;
	}
	public String getRightsCompany() {
		return rightsCompany;
	}
	public void setRightsCompany(String rightsCompany) {
		this.rightsCompany = rightsCompany;
	}
	public String getRightsMachines() {
		return rightsMachines;
	}
	public void setRightsMachines(String rightsMachines) {
		this.rightsMachines = rightsMachines;
	}
	public String getRightsLine() {
		return rightsLine;
	}
	public void setRightsLine(String rightsLine) {
		this.rightsLine = rightsLine;
	}
	public String getRightsButton() {
		return rightsButton;
	}
	public void setRightsButton(String rightsButton) {
		this.rightsButton = rightsButton;
	}
	public String getValidataCode() {
		return validataCode;
	}
	public void setValidataCode(String validataCode) {
		this.validataCode = validataCode;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public String getBanVmCode() {
		return banVmCode;
	}
	public void setBanVmCode(String banVmCode) {
		this.banVmCode = banVmCode;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getAliUserId() {
		return aliUserId;
	}
	public void setAliUserId(String aliUserId) {
		this.aliUserId = aliUserId;
	}
	public Character getIsPrincipal() {
		return isPrincipal;
	}
	public void setIsPrincipal(Character isPrincipal) {
		this.isPrincipal = isPrincipal;
	}
	public Integer getDepartMent() {
		return departMent;
	}
	public void setDepartMent(Integer departMent) {
		this.departMent = departMent;
	}
	public Integer getPost() {
		return post;
	}
	public void setPost(Integer post) {
		this.post = post;
	}
	
	
}
