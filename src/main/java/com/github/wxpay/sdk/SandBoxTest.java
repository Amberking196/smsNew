package com.github.wxpay.sdk;

import java.util.HashMap;
import java.util.Map;

public class SandBoxTest {


	public static void main(String[] args) {

		try {
			testRefundBySandBox();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//沙箱退款

	}
	
	private static void testRefundBySandBox() throws Exception{
		Map<String, String> reqData = new HashMap<>();
		reqData.put("out_trade_no", "000");
		reqData.put("out_refund_no", "000");
		reqData.put("total_fee", "1");
		reqData.put("refund_fee", "1");
		
		WXPay pay = new WXPay(new WXPayConfigImpl(WXPayConfigImpl.getsignkey()), false, true);//关键在此构造
		System.out.println("开始退款");
		Map<String, String> map = pay.refund(reqData);
		System.out.println(map);
	}
}
