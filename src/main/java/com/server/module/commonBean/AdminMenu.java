package com.server.module.commonBean;

public class AdminMenu {
	//自增标识 
	private int id;   
	//父节点ID
	private int pid;
	//地址
	private String url;
	//名称
	private String name;
	//是否显示；0=否，1=是
	private int isShow;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	} 

}
