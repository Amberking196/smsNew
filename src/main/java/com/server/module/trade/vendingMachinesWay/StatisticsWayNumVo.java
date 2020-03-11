package com.server.module.trade.vendingMachinesWay;
import com.server.common.utils.excel.annotation.ExcelField;

import lombok.Data;

@Data
public class StatisticsWayNumVo {
	@ExcelField(title = "售货机编码",align=2)
    private String vmCode;
	@ExcelField(title = "地址",align=2)
    private String address;
    @ExcelField(title = "货道",align=2)
    private Integer way;
    @ExcelField(title = "状态",align=2)
    private Integer state;
    @ExcelField(title = "商品名",align=2)
    private String itemName;
    @ExcelField(title = "存货量",align=2)
    private Integer num;
    @ExcelField(title = "容量",align=2)
    private Integer fullNum;
    
	private Long id; 
   
}
