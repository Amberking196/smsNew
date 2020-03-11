package com.server.module.trade.web.bean;

import lombok.Data;

@Data
public class CreateOrderResultBean {

	private String appid;//调用接口提交的公众账号ID

	private String mchid;//调用接口提交的商户号

	private String out_order_no;//调用接口提交的商户服务订单号

	private String service_id;//服务id

	private String order_id;//微信支付服务订单号一一对应商户服务订单号

	private String miniprogram_businesstype;//

	private String miniprogram_appid;

	private String miniprogram_path;

	private String miniprogram_username;

	private String Package;

/*	{ 
		   "appid": "wxd678efh567hg6787", 
		   "mchid": "1230000109", 
		   "out_order_no": "1234323JKHDFE1243252", 
		   "service_id": "500001", 
		   "order_id": "13145646546516", 
		   "miniprogram_appid": "wxid15165161145", 
		   "miniprogram_path": "/wxvalue", 
		   "miniprogram_username": "username", 
		   "package": "package" 
		}*/
}
