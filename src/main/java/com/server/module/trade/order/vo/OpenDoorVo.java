package com.server.module.trade.order.vo;

public class OpenDoorVo {
	
	private String message;
	private String toClient;
	private String fromClient;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getToClient() {
		return toClient;
	}
	public void setToClient(String toClient) {
		this.toClient = toClient;
	}
	public String getFromClient() {
		return fromClient;
	}
	public void setFromClient(String fromClient) {
		this.fromClient = fromClient;
	}

}
