package com.server.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.server.util.HttpUtil;
import com.server.util.stateEnum.CommandVersionEnum;
@Component
public class ReplenishClient {
	
	@Value("${mms.address}")
	private String mmsAddress;
	
	private final static String ONE_CLOSED_VER1 = "/closed/ver1";
	private final static String ONE_CLOSED_VER2 = "/closed/ver2";
	private final static String ALL_CLOSED_VER1 = "/closed/allVer1";
	private final static String ALL_CLOSED_VER2 = "/closed/allVer2";

	public void toMMSOneClosed(String param,Integer version){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("param", param);
		String jsonString = JSON.toJSONString(params);
		if(CommandVersionEnum.VER1.getState().equals(version)){
			HttpUtil.post(mmsAddress+ONE_CLOSED_VER1,jsonString);
		}else if (CommandVersionEnum.VER2.getState().equals(version)){
			HttpUtil.post(mmsAddress+ONE_CLOSED_VER2,jsonString);
		}
	}
	
	public void toMMSAllClosed(String param,Integer version){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("param", param);
		String jsonString = JSON.toJSONString(params);
		if(CommandVersionEnum.VER1.getState().equals(version)){
			HttpUtil.post(mmsAddress+ALL_CLOSED_VER1,jsonString);
		}else if (CommandVersionEnum.VER2.getState().equals(version)){
			HttpUtil.post(mmsAddress+ALL_CLOSED_VER2,jsonString);
		}
	}
	
	public static void main(String[] args) {
		String param = "aa";
		Map<String,Object> aa = new HashMap<String,Object>();
		aa.put("param", param);
		String jsonString = JSON.toJSONString(aa);
		System.out.println(jsonString);
	}
}
