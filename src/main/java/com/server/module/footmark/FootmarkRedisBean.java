package com.server.module.footmark;

import com.server.util.stateEnum.ModeEnum;

public class FootmarkRedisBean {

	private Integer modeId;
	private Long footmarkId;
	public Integer getModeId() {
		return modeId;
	}
	public void setModeId(Integer modeId) {
		this.modeId = modeId;
	}
	
	
	public Long getFootmarkId() {
		return footmarkId;
	}
	public void setFootmarkId(Long footmarkId) {
		this.footmarkId = footmarkId;
	}
	public FootmarkRedisBean(){}
	public FootmarkRedisBean(ModeEnum mode,Long footmarkId){
		this.modeId = mode.getState();
		this.footmarkId = footmarkId;
	}
}
