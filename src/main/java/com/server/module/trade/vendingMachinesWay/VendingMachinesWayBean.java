package com.server.module.trade.vendingMachinesWay;
import java.util.Date;

import com.server.common.persistence.Entity;

import lombok.Data;
/**
 * table name:  vending_machines_way
 * author name: yjr
 * create time: 2018-04-12 14:04:38
 */ 
@Data
@Entity(tableName="vending_machines_way",id="id",idGenerate="auto")
public class VendingMachinesWayBean{


//@JsonIgnore	public String tableName="vending_machines_way";
//@JsonIgnore	public String selectSql="select * from vending_machines_way where 1=1 ";
//@JsonIgnore	public String selectSql1="select id,vendingMachinesCode,wayNumber,itemId,state,num,fullNum,updateTime,createTime from vending_machines_way where 1=1 ";
	private Long id;
	private String vendingMachinesCode;
	private Integer wayNumber;
	private Long itemId;
	private Integer state;
	private Integer num;
	private Integer fullNum;
	private Date updateTime;
	private Date createTime;

}

