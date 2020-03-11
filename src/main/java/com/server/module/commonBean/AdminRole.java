package com.server.module.commonBean;

import java.util.List;

public class AdminRole {
	//角色ID
	private int id;
	//角色名称
	private String name;  
	 //角色菜单
	private List<AdminMenu> adminMenuList;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<AdminMenu> getAdminMenuList() {
		return adminMenuList;
	}
	public void setAdminMenuList(List<AdminMenu> adminMenuList) {
		this.adminMenuList = adminMenuList;
	}
	
	
	
}
