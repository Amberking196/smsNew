package com.server.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
@Component
public class JsonUtil { 
	
	    public  static String fillResultString( final Integer status,  final String message,  final Object result){
	        JSONObject jsonObject=null;
			try {
				jsonObject = new JSONObject(){{
				    put("status", status);
				    put("message", message);
				    put("result", result);
				}}; 
		        return jsonObject.toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	        return jsonObject.toString();
	}
}
