package com.server.module.commonBean;

import java.util.List;

public class AdminDepartment {
	//部门ID
	private int id;
	//名称	
	private String name;
	//上级ID	
	private int parentId;
	
	private List<AdminRole> adminRoleList;

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

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public List<AdminRole> getAdminRoleList() {
		return adminRoleList;
	}

	public void setAdminRoleList(List<AdminRole> adminRoleList) {
		this.adminRoleList = adminRoleList;
	}

}
