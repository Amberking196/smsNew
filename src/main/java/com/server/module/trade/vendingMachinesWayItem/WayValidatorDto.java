package com.server.module.trade.vendingMachinesWayItem;

import java.math.BigDecimal;

public class WayValidatorDto {

	private Integer num;
	private Integer state;
	private BigDecimal minUnitPrice;
	private Integer minStandard;
	
	
	public Integer getMinStandard() {
		return minStandard;
	}
	public void setMinStandard(Integer minStandard) {
		this.minStandard = minStandard;
	}
	public BigDecimal getMinUnitPrice() {
		return minUnitPrice;
	}
	public void setMinUnitPrice(BigDecimal minUnitPrice) {
		this.minUnitPrice = minUnitPrice;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	
}
