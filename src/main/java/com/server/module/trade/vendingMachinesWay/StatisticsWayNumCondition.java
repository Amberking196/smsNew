package com.server.module.trade.vendingMachinesWay;
import com.server.module.commonBean.PageAssist;

import lombok.Data;

@Data
public class StatisticsWayNumCondition extends PageAssist{
	Integer companyId;
	String idIns;
	Integer underNum;
	Integer state;
	String vmCode;
}
