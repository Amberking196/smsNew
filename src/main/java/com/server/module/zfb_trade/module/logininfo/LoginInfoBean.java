package com.server.module.zfb_trade.module.logininfo;

import java.util.Date;

public class LoginInfoBean {

	private Long id;
	private String loginCode;   //用户账号
	private String password;    //密码
	private Integer status;         //状态
	private String name;        //名字
	private Date hireDate;      //雇用时间 (不需要传送)
	private String codeNo;      //不用传		
	private String role;        //角色
	private Integer companyId;      //所属公司
	private String phone;     //手机号码
	private Integer loginModel;  //登录方式
	private String mail;     //邮箱
	private Long founder;    //创建人
	private Date updateDate;  //更新时间
	private String extraMenu;   //可以显示的菜单 menuId
	private String rightsCompany;  //公司权限
	private String rightsMachines;  //机器权限
	private String rightsLine;    //线路权限
	private String rightsButton;  //按纽
	private String validataCode;
	private Date outDate;
	private String banVmCode;
	private String openId; //varchar(32) DEFAULT NULL,
	private String aliUserId; //varchar(32) DEFAULT NULL,
	private String isPrincipal; //是否是管理员(0代表不是,1代表是)
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
	public String getIsPrincipal() {
		return isPrincipal;
	}
	public void setIsPrincipal(String isPrincipal) {
		this.isPrincipal = isPrincipal;
	}
	
}
