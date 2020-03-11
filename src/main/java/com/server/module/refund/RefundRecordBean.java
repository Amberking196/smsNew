package com.server.module.refund;

import java.util.Date;

public class RefundRecordBean {

	public final static Integer SUCCESS = 1;
	public final static Integer FAIL = 2;

	private Long id;
	private Integer state;// 退款状态(1:成功;2:失败)
	private Integer type;// 订单产生类型(1:机器订单退款;2:商城普通订单退款;3:团购订单退款)
	private Integer refundPlatform;
	private String outRefundNo;
	private String platformNo;
	private String payCode;
	private String ptCode;
	private Double price;
	private String itemName;
	private Double refundPrice;
	private String reason;
	private Long createUser;
	private Date createTime;
	private Integer companyId;
	// 退款类型 0.自动退款 1.拍错了 2.取货不方便
	private Integer refundGenre;
	// 退款金额类型 0 全额退款 1部分退款
	private Integer refundMoneyType;
	//退款数量
	private Integer refundNum;
	
	private Date payTime;
	
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPlatformNo() {
		return platformNo;
	}

	public void setPlatformNo(String platformNo) {
		this.platformNo = platformNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getRefundPlatform() {
		return refundPlatform;
	}

	public void setRefundPlatform(Integer refundPlatform) {
		this.refundPlatform = refundPlatform;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getPtCode() {
		return ptCode;
	}

	public void setPtCode(String ptCode) {
		this.ptCode = ptCode;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(Double refundPrice) {
		this.refundPrice = refundPrice;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public Integer getRefundGenre() {
		return refundGenre;
	}

	public void setRefundGenre(Integer refundGenre) {
		this.refundGenre = refundGenre;
	}

	public Integer getRefundMoneyType() {
		return refundMoneyType;
	}

	public void setRefundMoneyType(Integer refundMoneyType) {
		this.refundMoneyType = refundMoneyType;
	}
	public Integer getRefundNum() {
		return refundNum;
	}

	public void setRefundNum(Integer refundNum) {
		this.refundNum = refundNum;
	}

	public static RefundRecordBean createRefundRecordBean(RefundDto refundDto, Long userId, String outRefundNo,
			String platformNo, Integer state, Integer platform) {
		RefundRecordBean refundRecord = new RefundRecordBean();
		refundRecord.setCreateUser(userId);
		refundRecord.setItemName(refundDto.getItemName());
		refundRecord.setOutRefundNo(outRefundNo);
		refundRecord.setPayCode(refundDto.getPayCode());
		refundRecord.setPlatformNo(platformNo);
		refundRecord.setPrice(refundDto.getPrice().doubleValue());
		refundRecord.setPtCode(refundDto.getPtCode());
		refundRecord.setReason(refundDto.getReason());
		refundRecord.setRefundPlatform(platform);
		refundRecord.setRefundPrice(refundDto.getRefundPrice().doubleValue());
		refundRecord.setState(state);
		refundRecord.setCompanyId(refundDto.getCompanyId());
		refundRecord.setType(refundDto.getOrderType());
		refundRecord.setRefundGenre(refundDto.getRefundGenre());
		refundRecord.setRefundMoneyType(refundDto.getRefundMoneyType() == null ? 0 : refundDto.getRefundMoneyType());
		refundRecord.setRefundNum(refundDto.getRefundNum());
		return refundRecord;
	}

}
