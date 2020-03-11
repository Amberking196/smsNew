package com.server.util;

import java.io.Serializable;

import org.springframework.stereotype.Component;
 

@Component
public class ReturnDataUtil implements Serializable {
	
	private int status=1;        //状态值,1成功，0失败
	private String message;  //返回中文提示
	private Object returnObject; //返回值对象  json格式
    private boolean willGo = false; //是否跳转
    private String reJson;// json格式
	private int currentPage;   //当前页
	private int totalPage;      //总页数
	
	private Long total;
	private int pageSize=10;
	
	
	private static final long serialVersionUID = 1L;

	public static final int NO_LOGIN = -1;

	public static final int SUCCESS = 1;

	public static final int CHECK_FAIL = 0;

	public static final int NO_PERMISSION = 2;

	public static final int UNKNOWN_EXCEPTION = -99;
	public ReturnDataUtil(){
		setStatus(1);//默认成功
	}
	
	public ReturnDataUtil(Object data) {
		super();
		this.returnObject = data;
	}

	public ReturnDataUtil(Throwable e) {
		super();
		this.message = e.toString();
		this.status = UNKNOWN_EXCEPTION;
	}
	
	public String getReJson() {
		return reJson;
	}
	public void setReJson(String reJson) {
		this.reJson = reJson;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalPage() {
		if (total!=null){
			int i=(int) (total/pageSize);
			int j=(int) (total%pageSize);
			if(j!=0){
				i=i+1;
			}
			return i;
		}
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getReturnObject() {
		return returnObject;
	}
	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isWillGo() {
		return willGo;
	}

	public void setWillGo(boolean willGo) {
		this.willGo = willGo;
	}
	
	
}
