package com.server.util.stateEnum;

public enum PayTypeEnum {

	WEIXIN(1,"微信"),
	ZHIFUBAO(2,"支付宝"),
	YS_BALANCE(3,"优水余额"),
	APP(4,"App"),
	UNION(5,"统一标识");
	
	private int index;
	private String payType;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	PayTypeEnum(int index,String payType){
		this.index = index;
		this.payType = payType;
	}
	
	public static String getPayTypeInfo(int index){
		for (PayTypeEnum payType : PayTypeEnum.values()) {
			if(payType.getIndex() == index){
				return payType.getPayType();
			}
		}
		return null;
	}
}
