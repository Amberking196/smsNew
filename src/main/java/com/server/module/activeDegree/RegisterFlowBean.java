package com.server.module.activeDegree;

import java.util.Date;
/**
 * 严正说明，改了字段名称需要在RegisterFlowEnum中修改相应字段名称
 */
public class RegisterFlowBean {

	
	private Long id;
	private Long customerId;
	private String vmCode;
	private Integer wxOrAli;
	private Integer register;
	private Date registerTime;
	private Integer noPassWordPay;
	private Date noPwPayTime;
	private Integer cancelNopwPay;
	private Date cancelNopwPayTime;
	private Integer attention;
	private Date attentionTime;
	private Integer cancelAttention;
	private Date cancelAttentionTime;
	private Integer receivePercent;
	private Date receiveTime;
	private Integer openMachines;
	private Date openTime;
	private Integer createOrder;
	private Date orderTime;
	private Integer usedPercent;
	private Date usedPercentTime;
	private Integer buyNum;
	
	public RegisterFlowBean(){}
	public RegisterFlowBean(Long customerId,String vmCode,Integer wxOrAli){
		this.customerId = customerId;
		this.vmCode = vmCode;
		this.wxOrAli = wxOrAli;
		this.register = 1;
	}
	
	public Integer getWxOrAli() {
		return wxOrAli;
	}
	public void setWxOrAli(Integer wxOrAli) {
		this.wxOrAli = wxOrAli;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public Integer getRegister() {
		return register;
	}
	public void setRegister(Integer register) {
		this.register = register;
	}
	public Date getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	public Integer getNoPassWordPay() {
		return noPassWordPay;
	}
	public void setNoPassWordPay(Integer noPassWordPay) {
		this.noPassWordPay = noPassWordPay;
	}
	public Date getNoPwPayTime() {
		return noPwPayTime;
	}
	public void setNoPwPayTime(Date noPwPayTime) {
		this.noPwPayTime = noPwPayTime;
	}
	public Integer getCancelNopwPay() {
		return cancelNopwPay;
	}
	public void setCancelNopwPay(Integer cancelNopwPay) {
		this.cancelNopwPay = cancelNopwPay;
	}
	public Date getCancelNopwPayTime() {
		return cancelNopwPayTime;
	}
	public void setCancelNopwPayTime(Date cancelNopwPayTime) {
		this.cancelNopwPayTime = cancelNopwPayTime;
	}
	public Integer getAttention() {
		return attention;
	}
	public void setAttention(Integer attention) {
		this.attention = attention;
	}
	public Date getAttentionTime() {
		return attentionTime;
	}
	public void setAttentionTime(Date attentionTime) {
		this.attentionTime = attentionTime;
	}
	public Integer getCancelAttention() {
		return cancelAttention;
	}
	public void setCancelAttention(Integer cancelAttention) {
		this.cancelAttention = cancelAttention;
	}
	public Date getCancelAttentionTime() {
		return cancelAttentionTime;
	}
	public void setCancelAttentionTime(Date cancelAttentionTime) {
		this.cancelAttentionTime = cancelAttentionTime;
	}
	public Integer getReceivePercent() {
		return receivePercent;
	}
	public void setReceivePercent(Integer receivePercent) {
		this.receivePercent = receivePercent;
	}
	public Date getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	public Integer getOpenMachines() {
		return openMachines;
	}
	public void setOpenMachines(Integer openMachines) {
		this.openMachines = openMachines;
	}
	public Date getOpenTime() {
		return openTime;
	}
	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}
	public Integer getCreateOrder() {
		return createOrder;
	}
	public void setCreateOrder(Integer createOrder) {
		this.createOrder = createOrder;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public Integer getUsedPercent() {
		return usedPercent;
	}
	public void setUsedPercent(Integer usedPercent) {
		this.usedPercent = usedPercent;
	}
	public Date getUsedPercentTime() {
		return usedPercentTime;
	}
	public void setUsedPercentTime(Date usedPercentTime) {
		this.usedPercentTime = usedPercentTime;
	}
	public Integer getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(Integer buyNum) {
		this.buyNum = buyNum;
	}
	
	
}
