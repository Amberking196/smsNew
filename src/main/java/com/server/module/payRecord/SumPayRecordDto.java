package com.server.module.payRecord;

import java.math.BigDecimal;

public class SumPayRecordDto {

	//总记录数
	private Long total;
	//销售总价
	private BigDecimal sumPrice;
	//进货总价
	private BigDecimal sumCostPrice;
	//总销售商品量
	private Long sumNum;
	
	public Long getSumNum() {
		return sumNum;
	}
	public void setSumNum(Long sumNum) {
		this.sumNum = sumNum;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public BigDecimal getSumPrice() {
		return sumPrice;
	}
	public void setSumPrice(BigDecimal sumPrice) {
		this.sumPrice = sumPrice;
	}
	public BigDecimal getSumCostPrice() {
		return sumCostPrice;
	}
	public void setSumCostPrice(BigDecimal sumCostPrice) {
		this.sumCostPrice = sumCostPrice;
	}
	
}
