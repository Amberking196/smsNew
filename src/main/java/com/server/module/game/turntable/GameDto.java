package com.server.module.game.turntable;

public class GameDto {

	//剩余次数
	private Integer times;
	//指标停留
	private Integer indexOf;
	//用户抽奖记录 主键id
	private Long id;
	
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	public Integer getIndexOf() {
		return indexOf;
	}
	public void setIndexOf(Integer indexOf) {
		this.indexOf = indexOf;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
}
