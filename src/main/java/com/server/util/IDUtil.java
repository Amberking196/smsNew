package com.server.util;

public class IDUtil {

	/**
	 * 生成商城订单payCode
	 * @author hebiting
	 * @date 2018年7月26日下午4:18:25
	 * @return
	 */
	public static String getPayCode(){
		StringBuffer random = new StringBuffer();
		random.append("3");
		random.append((int)((Math.random()*9+1)*100000));
		random.append(System.currentTimeMillis());
		random.append((int)((Math.random()*9+1)*10000));
		String payCode = random.toString();
		return payCode;
	}
}
