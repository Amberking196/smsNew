package com.server.util.page;

public class Page {
	private int currentpage;//当前页  
    private int pagsize;//每页的数据量  
    private int totalpage;//总页数  
    private int totalcount;//总数据量 
	public int getCurrentpage() {
		return currentpage;
	}
	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}
	public int getPagsize() {
		return pagsize;
	}
	public void setPagsize(int pagsize) {
		this.pagsize = pagsize;
	}
	public int getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}
	public int getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}
}
