package com.server.module.itemBasic;
import com.server.common.persistence.Entity;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: state_info 
 * author name: yjr 
 * create time: 2018-03-30 11:10:15
 */
@Data
@Entity(tableName = "state_info", id = "state", idGenerate = "asign")
public class StateInfoBean {
/*	@JsonIgnore
	public String tableName = "state_info";
	@JsonIgnore
	public String selectSql = "select * from state_info where 1=1 ";
	@JsonIgnore
	public String selectSql1 = "select keyName,state,name,id from state_info where 1=1 ";*/
	
	private String keyName;
	private Long state;
	private String name;
	private Long id;
}
