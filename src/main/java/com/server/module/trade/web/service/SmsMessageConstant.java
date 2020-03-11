package com.server.module.trade.web.service;

public class SmsMessageConstant {

	/**
	 * 门锁损坏短信提示语
	 */
	public static final String BREAKDOWNVMCODE = "售货机:%s中%s号货道出现门锁损坏的状况，请尽快过去维修！";
	/**
	 * 未关门短信提示语
	 */
	public static final String NOTCLOSEDDOOR = "系统检测到您上次购买结束后售货机窗口未关紧，如关闭前有人提水，则会产生异常交易，他人拿水您付款。请及时关闭售货机窗口！谢谢合作！";
	
	
	
	/**
	 * 获取门锁损坏提示语
	 * @author hebiting
	 * @date 2019年2月18日下午2:26:23
	 * @param vmCode
	 * @param wayNumber
	 * @return
	 */
	public static String getBreakdownVmCode(String vmCode,Integer wayNumber){
		return String.format(BREAKDOWNVMCODE, vmCode,wayNumber);
	}
	
	public static void main(String[] args) {
		System.out.println(getBreakdownVmCode("12", 1));
	}
}
