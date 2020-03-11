package com.server.module.trade.vendingMachiesItem;
import java.math.BigDecimal;
import java.util.Date;
import com.server.common.persistence.Entity;
import lombok.Data;
/**
 * table name:  vending_machines_item
 * author name: yjr
 * create time: 2018-04-13 08:57:26
 */ 
@Data
@Entity(tableName="vending_machines_item",id="id",idGenerate="auto")
public class VendingMachinesItemBean{


//@JsonIgnore	public String tableName="vending_machines_item";
//@JsonIgnore	public String selectSql="select * from vending_machines_item where 1=1 ";
//@JsonIgnore	public String selectSql1="select id,companyId,basicItemId,price,endTime,costPrice,hot,updateTime,createTime from vending_machines_item where 1=1 ";
	private Long id;
	private Long companyId;
	private Long basicItemId;
	private BigDecimal price;
	private Date endTime;
	private BigDecimal costPrice;
	private Integer hot;
	private Date updateTime;
	private Date createTime;

}

