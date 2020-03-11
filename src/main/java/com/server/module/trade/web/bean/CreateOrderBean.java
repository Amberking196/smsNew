package com.server.module.trade.web.bean;

import lombok.Data;

@Data
public class CreateOrderBean {

	private String appid;
	private String out_order_no;//商户系统内部服务订单号
	private String service_id;//服务ID
	private String service_start_time;//服务开启时间支持三种格式:yyyyMMddHHmmss和yyyyMMdd和“OnAccept”
	private String service_start_location;//开始使用服务的地点.不超过20个字符
	private String risk_amount;
/*	订单风险金额，指该笔订单的预估金额
	大于0的数字，单位为分
	订单风险金额≤每个服务ID的服务风险金额
	当【优惠金额】字段为空时，订单风险金额需满足：
	订单风险金额≥付费项目金额之和
	微信支付分会评估用户是否可以使用此额度的智慧零售权益。若评估通过，可进入下一步流程；若评估不通过，将创建订单失败。*/
	private String service_introduction;//介绍本订单所提供的服务
	//private String attach;
/*	商户自定义说明,可存放本订单所需信息.
	需要先urlencode后传入.
	总长度不大于200字符,超出报错处理.*/
	
	private String attach;//附加信息
}
