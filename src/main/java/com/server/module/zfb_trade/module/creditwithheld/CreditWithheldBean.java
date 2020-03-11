package com.server.module.zfb_trade.module.creditwithheld;

import java.util.Date;

public class CreditWithheldBean {

	private Integer companyId;
	private Long id; //bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增标识',
	private String agreementNo; //varchar(20) DEFAULT NULL COMMENT '协议号:支付宝系统中用以唯一标识用户签约记录的编号。',
	private String productCode; //varchar(30) DEFAULT NULL COMMENT '签约产品码:协议产品码，商户和支付宝签约时确定，不同业务场景对应不同的签约产品码',
	private String scene; //varchar(20) DEFAULT NULL COMMENT '签约场景:当前签约请求的协议产品场景。',
	private Date validTime; //datetime DEFAULT NULL COMMENT '协议生效时间:用户代扣协议的实际生效时间，格式为yyyy-MM-dd HH:mm:ss。',
	private Date invalidTime; //datetime DEFAULT NULL COMMENT '协议失效时间:用户代扣协议的失效时间，格式为yyyy-MM-dd HH:mm:ss。',
	private String alipayUserId; //varchar(20) DEFAULT NULL COMMENT '支付宝用户号:用户签约的支付宝账号对应的支付宝唯一用户号。\r\n            以2088开头的16位纯数字组成。',
	private String externalSignNo; //varchar(20) DEFAULT NULL COMMENT '商户签约号',
	private String isSuccess; //varchar(1) DEFAULT NULL COMMENT '表示该次处理是否成功。 T代表成功  F代表失败',
	private String signType; //varchar(3) DEFAULT NULL COMMENT '签名方式:DSA、RSA、MD5三个值可选，必须大写',
	private String sign; //varchar(32) DEFAULT NULL COMMENT '签名',
	private String status; //varchar(8) DEFAULT NULL COMMENT '协议状态: TEMP：暂存，协议未生效过； NORMAL：正常；STO\r\n            P：暂停。',
	private Date signTime; //datetime DEFAULT NULL COMMENT '签约时间: 支付宝代扣协议的实际签约时间，格式为yyyy-MM-dd HH:mm:ss。',
	private Date signModifyTime; //datetime DEFAULT NULL COMMENT '签约修改时间:最近一次协议修改时间，格式为yyyy-MM-dd HH:mm:ss。\r\n            如果协议未修改过，本参数值等于签约时间。',
	private Date createTime; //timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
	private String createId; //varchar(20) DEFAULT '‘’' COMMENT '创建人ID',
	private Date updateTime; //timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
	private String lastUpdateId; //varchar(20) DEFAULT NULL COMMENT '最后更新人',
	private Integer deleteFlag; //smallint(1) DEFAULT '0' COMMENT '删除标识: 0 正常，1 已删除',
	
	
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAgreementNo() {
		return agreementNo;
	}
	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getScene() {
		return scene;
	}
	public void setScene(String scene) {
		this.scene = scene;
	}
	public Date getValidTime() {
		return validTime;
	}
	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}
	public Date getInvalidTime() {
		return invalidTime;
	}
	public void setInvalidTime(Date invalidTime) {
		this.invalidTime = invalidTime;
	}
	public String getAlipayUserId() {
		return alipayUserId;
	}
	public void setAlipayUserId(String alipayUserId) {
		this.alipayUserId = alipayUserId;
	}
	public String getExternalSignNo() {
		return externalSignNo;
	}
	public void setExternalSignNo(String externalSignNo) {
		this.externalSignNo = externalSignNo;
	}
	public String getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getSignTime() {
		return signTime;
	}
	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}
	public Date getSignModifyTime() {
		return signModifyTime;
	}
	public void setSignModifyTime(Date signModifyTime) {
		this.signModifyTime = signModifyTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getLastUpdateId() {
		return lastUpdateId;
	}
	public void setLastUpdateId(String lastUpdateId) {
		this.lastUpdateId = lastUpdateId;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	
	
}
