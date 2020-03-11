package com.server.module.zfb_trade.bean;


public class TcpCookies {

	/**
	 * 最后一次请求
	 * */
	private TcpRequest tcpRequest;
	/**
	 * 最后响应
	 * */
	private TcpResponse tcpResponse;

	private TcpCurrentTask tcpCurrentTask;
	/**
	 * 开门前的心跳，为了在盘点数据时比对
	 * */
	private TcpRequest preOpenDoorRequest;
	/**
	 * 机器编码
	 * */
	private String vmCode;
	public TcpRequest getTcpRequest() {
		return tcpRequest;
	}
	public void setTcpRequest(TcpRequest tcpRequest) {
		this.tcpRequest = tcpRequest;
	}
	public TcpResponse getTcpResponse() {
		return tcpResponse;
	}
	public void setTcpResponse(TcpResponse tcpResponse) {
		this.tcpResponse = tcpResponse;
	}
	public TcpCurrentTask getTcpCurrentTask() {
		return tcpCurrentTask;
	}
	public void setTcpCurrentTask(TcpCurrentTask tcpCurrentTask) {
		this.tcpCurrentTask = tcpCurrentTask;
	}
	public TcpRequest getPreOpenDoorRequest() {
		return preOpenDoorRequest;
	}
	public void setPreOpenDoorRequest(TcpRequest preOpenDoorRequest) {
		this.preOpenDoorRequest = preOpenDoorRequest;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	
	
}
