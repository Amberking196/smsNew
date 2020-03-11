package com.server.module.trade.customer.complain;
import com.server.module.commonBean.PageAssist;

import lombok.Data;
/**
 * table name:  customer_message
 * author name: why
 * create time: 2018-08-17 08:48:16
 */ 

@Data
public class TblCustomerComplainForm extends PageAssist{

	//故障申报回复状态  0 未回复 1 已回复
	private Integer state;
}

