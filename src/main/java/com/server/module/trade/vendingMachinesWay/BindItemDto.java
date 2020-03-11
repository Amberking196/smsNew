package com.server.module.trade.vendingMachinesWay;
import java.util.Date;
import lombok.Data;
@Data
public class BindItemDto {
	private Long basicItemId;
	private Float price;
	private Float costPrice;
	private Date endTime;
	private String vmCode;
	private Integer wayNumber;
	private Integer fullNum;
	private Integer num;
	
	private Long id;//如果不为空，认为是编制，否则是新增
	private Integer hot;
}
