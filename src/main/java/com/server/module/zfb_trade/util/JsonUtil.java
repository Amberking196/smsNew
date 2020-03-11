package com.server.module.zfb_trade.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	private static Log log = LogFactory.getLog(JsonUtil.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	static{
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	public static String toJson(Object object){
		Objects.requireNonNull(object, "object is null.");
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T toObject(String json, TypeReference<T> valueTypeRef){
		Objects.requireNonNull(json, "json is null.");
		Objects.requireNonNull(valueTypeRef, "valueTypeRef type is null.");
		try {
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			return mapper.readValue(json, valueTypeRef);
		} catch (IOException e) {
			log.error("【异常】e={}",e);
			return null;
		}
	}
	
//	public static void main(String[] args) {
//		String string = "{\"tcpRequest\":{\"machienType\":2,\"factoryNumber\":\"a00\",\"doorList\":[0,0,0,0],\"status\":0,\"powerSupplyStatus\":0,\"weightList\":[1000.0,1000.0,1000.0,1000.0],\"checkBit\":6,\"heartbeatTime\":\"2017-12-02 18:02:05\"},\"tcpResponse\":null,\"tcpCurrentTask\":null,\"vmCode\":\"1988200001\"}";
//		TcpCookies cookies = JsonUtil.toObject(string,new TypeReference<TcpCookies>(){});
//		System.out.println(cookies);
//	}
}
