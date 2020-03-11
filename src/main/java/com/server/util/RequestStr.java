package com.server.util;

import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Component;

@Component
public class RequestStr {
     public String getpra(ServletRequest request){
    	 String arg=request.getParameter("arg");
    	 if(arg==null){ 
    			Map<String, String[]>  re=request.getParameterMap();
    		 arg=re.get("arg")[0]; 
    	 }
    	 return arg;
     }
}
