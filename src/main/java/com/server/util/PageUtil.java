package com.server.util;

import java.util.ArrayList;
import java.util.List;

public class PageUtil {

	
	public int pageNow = 1; // 初始化为1,默认从第一页开始显示
	public int pageSize = 10; // 初始化每页显示10条记录
	public int totalPage = 0;
	public int RecordCount = 0; // 记录数
	public List<PageBean> pageNumList;

	public int pageNum=1;
	
	public PageUtil(){}

	public PageUtil( int pageSize) {
		this.pageSize = pageSize;
		this.pageNow = 1; // 初始化为1,默认从第一页开始显示
		this.totalPage = 0;
		this.RecordCount = 0; // 记录数
		this.pageNumList = new ArrayList<PageBean>();;
	}
	
	public void setpageNumList(int totalPage){
		System.out.println("**********"+totalPage);
		for(int i=1;i<=totalPage;i++){
			PageBean pageBean =new PageBean();
			pageBean.setPageNum(i);
			this.pageNumList.add(pageBean);
		}
	}
	
	public void setTotalPage() {
		this.totalPage = this.RecordCount / this.pageSize;
		if (this.totalPage * this.pageSize >= this.RecordCount){
			this.totalPage = this.RecordCount / this.pageSize;
		}else{
			this.totalPage = this.RecordCount / this.pageSize + 1;
		}
	}

	public void setRecordCount(int recordCount) {
		RecordCount = recordCount;
	}

	/**
	 * 页面下一页.
	 */
	public List<Object> nextPage(List<Object> objList) throws Exception {
		pageNow++;
		// 当前页加1
		return savePageData(objList);
	}
	/**
	 * 页面上一页.
	 */
	public List<Object> prePage(List<Object> objList) throws Exception {
		pageNow--;
		// 当前页减1
		return savePageData(objList);
	}
	
	public List<Object> numbPage(List<Object> objList) throws Exception {
		pageNow=pageNum;
		// 得到当前页
		return savePageData(objList);
	}
	/**
	 * 翻页时保存数据到Model.
	 */
	private List<Object> savePageData(List<Object> objList) throws Exception {
		List<Object> strList = new ArrayList<Object>();
		int loopNumber = 0;
		int startLoop = 0;
		if (pageNow * pageSize < RecordCount) {
			loopNumber = pageNow * pageSize;
			if (pageNow > 1) {
				startLoop = (pageNow - 1) * pageSize;
			} else {
				startLoop = 0;
			}
		} else {
			startLoop = (pageNow - 1) * pageSize;
			loopNumber = RecordCount;
		}
		// 更新bean中的记录
		for (int i = startLoop; i < loopNumber; i++) {
			Object objectBean = (Object)objList.get(i);
			strList.add(objectBean);
		}
		return strList;
	}
	
	public List<PageBean> getPageNumList() {
		return pageNumList;
	}

	public void setPageNumList(List<PageBean> pageNumList) {
		this.pageNumList = pageNumList;
	}
}
