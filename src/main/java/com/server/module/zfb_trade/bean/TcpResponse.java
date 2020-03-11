package com.server.module.zfb_trade.bean;

import java.util.ArrayList;
import java.util.List;

public class TcpResponse {

	
	private String vmCode;
	/**
	 * 门的具体状态
	 * */
	private List<Integer> doorList;
	/**
	 * 音量,99为不下发音量
	 */
	private Integer volume = 99;
	
	/**
	 * @param doorCount
	 */
	public TcpResponse(int doorCount) {
		//默认音量，99代表不下发
		this.doorList = new ArrayList<>();
		for(int i=0;i<doorCount;i++){
			doorList.add(0);
		}
	}
	public TcpResponse(){
		
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public List<Integer> getDoorList() {
		return doorList;
	}
	public void setDoorList(List<Integer> doorList) {
		this.doorList = doorList;
	}
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	
	
}
