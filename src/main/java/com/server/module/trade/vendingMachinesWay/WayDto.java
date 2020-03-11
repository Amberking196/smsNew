package com.server.module.trade.vendingMachinesWay;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
@Data
public class WayDto {
	private Long wayId;
	private Integer wayNumber;
	private Long itemId;
	private Integer state;
	private Integer num;
	private Integer fullNum;
	private BigDecimal price;
	private BigDecimal costPrice;
	private Integer hot;
	private String itemName;
	private String pic;
	private int weight;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endTime;
	
}
