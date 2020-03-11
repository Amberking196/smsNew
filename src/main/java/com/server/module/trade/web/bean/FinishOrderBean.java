package com.server.module.trade.web.bean;


import java.util.List;

import lombok.Data;
@Data
public class FinishOrderBean {
	
	private String appid;
	private String service_id;//服务ID
	private String finish_ticket;
	//用于完结订单时传入（每次获取到的字段内容可能变化，但之前获取的字段始终有效，可一直使用）,请确保数据完整性，无需对字段再做处理。
	//（注意：该字段可通过查询订单或者用户确认使用服务回调里获取finish_ticket，finish_ticket是支付分用来校验的参数，商户不可自定义）
	private Integer finish_type;
	private String cancel_reason;
	private Integer total_amount;

/*	大于等于0的数字，单位为分  100
	需满足：总金额=付费项目金额之和-商户优惠项目金额之和<=服务风险金额
	未使用服务，取消订单时，该字段必须填0*/
	
	private boolean profit_sharing;//是否指定服务商分账，true-需要分账，false-不需要分账
	
	private List<Fee> fees;

}

