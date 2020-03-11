package com.server.module.trade.order.domain;

import lombok.Data;

@Data
public class Way {
	private Integer wayNumber;// 货道编号  1   2   3   4 
	private Integer num;//数据库里的数量
	private Integer calculateNum;//计算出来的数量
	private Integer fullNumber;
	private Integer weight;//心跳获得该货道重量
	private String state;//该货道状态
	private int standard;//单元质量 克
	private Long id;
	private Long itemId;
	private Long basicItemId;

}
