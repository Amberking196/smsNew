package com.server.util.stateEnum;

public enum RegisterFlowEnum {

	REGISTE(1,"注册","register","registerTime"),
	NO_PASSWORD_PAY(2,"签约免密代扣","noPassWordPay","noPwPayTime"),
	CANCEL_NO_PASSWORD(3,"取消免密代扣","cancelNopwPay","cancelNopwPayTime"),
	ATTENTION(4,"关注公众号","attention","attentionTime"),
	CANCEL_ATTENTION(5,"取消关注","cancelAttention","cancelAttentionTime"),
	RECEIVE_PERCENT(6,"领取优惠券","receivePercent","receiveTime"),
	OPEN_MACHINES(7,"开门","openMachines","openTime"),
	CREATE_ORDER(8,"创建订单","createOrder","orderTime"),
	USED_PERCENT(9,"使用优惠券","usedPercent","usedPercentTime")
	;
	private Integer state;
	private String name;
	private String columnName;
	private String timeName;
	
	RegisterFlowEnum(Integer state,String name,String columnName,String timeName){
		this.state = state;
		this.name = name;
		this.columnName = columnName;
		this.timeName = timeName;
	}

	
	public String getTimeName() {
		return timeName;
	}


	public Integer getState() {
		return state;
	}

	public String getName() {
		return name;
	}

	public String getColumnName() {
		return columnName;
	}
	
}
