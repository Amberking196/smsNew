package com.server.util.stateEnum;

public enum RefundApplicationEnum {

	PEDDING_APPROVAL(1,"待审核"),
	REFUND_SUCCESS(2,"退款成功"),
	APPROVAL_FAIL(3,"审核不通过")
	;
	private Integer state;
	private String name;
	
	RefundApplicationEnum(Integer state,String name){
		this.state = state;
		this.name = name;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static String getName(Integer state){
		RefundApplicationEnum[] values = RefundApplicationEnum.values();
		for (RefundApplicationEnum app : values) {
			if(app.getState().equals(state)){
				return app.getName();
			}
		}
		return null;
	}
	
}
