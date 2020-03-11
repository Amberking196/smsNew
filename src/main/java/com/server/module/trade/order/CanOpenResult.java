package com.server.module.trade.order;

public class CanOpenResult {

	private boolean canOpen;
	private String state;
	public boolean getCanOpen() {
		return canOpen;
	}
	public void setCanOpen(boolean canOpen) {
		this.canOpen = canOpen;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public CanOpenResult(){}
	
	public CanOpenResult(boolean canOpen,String state){
		this.canOpen = canOpen;
		this.state = state;
	}
}
