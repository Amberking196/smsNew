package com.server.module.commonBean;

public class PageAssist {
	private int currentPage = 1;
	private int pageSize = 10;
	private int total;
	private int isShowAll=0;
	

	public int getIsShowAll() {
		return isShowAll;
	}
	public void setIsShowAll(int isShowAll) {
		this.isShowAll = isShowAll;
	}
	public PageAssist(int currentPage){
		this.currentPage = currentPage;
	}
	public PageAssist(int currentPage,int pageSize){
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}
	public PageAssist(){}
	
	
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
}
