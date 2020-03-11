package com.server.module.trade.web.order.preferential;

import java.util.List;

public abstract class PreferentialHandler implements Handler{

	private Handler nextHandler;
	private HandlerTypeEnum type;
	public PreferentialHandler(){}
	
	public PreferentialHandler(HandlerTypeEnum type){
		this.type = type;
	}
	
	public PreferentialHandler(HandlerTypeEnum type,Handler nextHandler){
		this.type = type;
		this.nextHandler = nextHandler;
	}
	

	public HandlerTypeEnum getType() {
		return type;
	}

	public void setType(HandlerTypeEnum type) {
		this.type = type;
	}

	public Handler getHandler() {
		return nextHandler;
	}

	public void setHandler(Handler nextHandler) {
		this.nextHandler = nextHandler;
	}

	/**
	 * 判断是否有同类处理器(ELSE不算同类)，如果有则返回true,反之为false
	 * @author hebiting
	 * @date 2019年2月13日下午3:09:34
	 * @param typeList
	 * @return
	 */
	public boolean hasSimilar(List<HandlerTypeEnum> typeList){
		HandlerTypeEnum someType = getType();
		if(someType!=HandlerTypeEnum.ELSE){
			if(typeList.contains(someType)){
				return true;
			}
		}
		return false;
	}
	
	
}
