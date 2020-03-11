package com.server.common.persistence;

import java.util.List;

import com.google.common.collect.Lists;

public class Row {
	
	private String tableName;
	private String primaryKey;
	private String idGenerate;//id生成方式  auto   assign
	private List<Column> listColumn=Lists.newArrayList();
	private String insertSql="";
	private String updateSql="";
	private String delSql="";
	private String getSql="";
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public void parseInsertSql(){
		StringBuilder sb=new StringBuilder();
		sb.append("insert into "+this.tableName+" (");
		if( idGenerate.equals("auto")){
			for (int i=0;i<listColumn.size();i++){
			     if (listColumn.get(i).getField().equals(primaryKey)){
			    	 listColumn.remove(i);
			    	 
			     }
			}
		}
		for (int i=0;i<listColumn.size();i++){
			Column column=listColumn.get(i);
			sb.append(column.getField());
			if(i!=listColumn.size()-1){
				sb.append(",");
			}
		}
		sb.append(") values (");
		for (int i=0;i<listColumn.size();i++){
			sb.append("?");
			if(i!=listColumn.size()-1){
				sb.append(",");
			}
		}
		sb.append(")");
		insertSql=sb.toString();
	}
	private void paserGetSql(){
		StringBuilder sb=new StringBuilder();
		sb.append("select *  from ").append(this.tableName).append(" where ").append(this.primaryKey+"=?");
		getSql=sb.toString();
 
	}
	public String getGetSql() {
		paserGetSql();
		return getSql;
	}
	public void setGetSql(String getSql) {
		this.getSql = getSql;
	}
	public void paserDelSql(){
		StringBuilder sb=new StringBuilder();
		sb.append("delete from ").append(this.tableName).append(" where ").append(this.primaryKey+"=?");
		delSql=sb.toString();
 
	}
	public void paserUpdateSql(){
		StringBuilder sb=new StringBuilder();
		sb.append("update ").append(tableName).append(" set ");
		putPrimaryToLast();
		for (int i=0;i<listColumn.size()-1;i++){
			Column column=listColumn.get(i);
			if (!column.getField().equals(primaryKey)){
				sb.append(column.getField());
				sb.append(" =? ");
				if(i!=listColumn.size()-2){
					sb.append(",");
				}
			}		
		}
		sb.append(" where ").append(primaryKey).append(" = ?");
		updateSql=sb.toString();

	}
	/**
	 * 把主键放到后面
	 */
	private void putPrimaryToLast() {
		//把主键放到后面
		for (int i=0;i<listColumn.size();i++){
			Column temp=null;
			if (listColumn.get(i).getField().equals(primaryKey)){
				temp=listColumn.get(i);
				listColumn.set(i, listColumn.get(listColumn.size()-1));
			    listColumn.set(listColumn.size()-1, temp);
			    break;
			}
		}
	}
	public List<Column> getListColumn() {
		return listColumn;
	}
	public void setListColumn(List<Column> listColumn) {
		this.listColumn = listColumn;
	}
	
	public void addColumn(Column column){
		this.listColumn.add(column);
	}
	public String getInsertSql() {
		parseInsertSql();
		return insertSql;
	}
	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}
	
	public String getUpdateSql() {
		paserUpdateSql();
		return updateSql;
	}
	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}
	public String getDelSql() {
		paserDelSql();
		return delSql;
	}
	public void setDelSql(String delSql) {
		this.delSql = delSql;
	}
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getIdGenerate() {
		return idGenerate;
	}
	public void setIdGenerate(String idGenerate) {
		this.idGenerate = idGenerate;
	}
	public static void main(String[] args) {
		Row row=new Row();
		
		Column c=new Column();
		c.setField("id");
		c.setValue("v1");
		
		Column c1=new Column();
		c1.setField("field1");
		c1.setValue("v11");
		Column c3=new Column();
		c3.setField("field3");
		c3.setValue("v13");
		
		row.addColumn(c);
		row.addColumn(c1);
		row.addColumn(c3);
		row.setTableName("filed");
		row.setTableName("info");
		//row.parseInsertSql();
		row.setPrimaryKey("id");
		
		System.out.println(row.getDelSql());
		
		
	}
   
}
