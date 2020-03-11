package com.server.module.trade.order.support;

import com.server.module.trade.support.CodeEnum;

public enum PayRecordStateEnum implements CodeEnum{
	PAY_SUCCESS(10001,"已支付"),
	NOT_PAY(10002,"未支付"),
//	PAY_NOTIFY(10003,"通知出货"),
//	PAY_LOSE(10004,"支付失败"),
//	PAY_CLOSE(10006,"关闭订单"),
//	PAY_REFUND_SUCCESS(10007,"退款已完成"),
//	PAY_REFUND_FAIL(10008,"退款失败"),
	
	PAY_TYPE_WX(1,"微信"),
	PAY_TYPE_ALIPAY(2,"支付宝"),
	
	;
	private Integer code;
	private String msg;
	private PayRecordStateEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}

}
