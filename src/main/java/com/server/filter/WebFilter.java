package com.server.filter;
 

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WebFilter  implements Filter {


	public static Log log = LogFactory.getLog(WebFilter.class);

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

            ModifyHttpServletRequestWrapper  httpServletRequestWrapper = new ModifyHttpServletRequestWrapper ((HttpServletRequest) request);
    	    httpServletRequestWrapper.putHeader("Accept-Charset", "utf-8");  
    	    httpServletRequestWrapper.putHeader("Content-type", "application/json; charset=utf-8");// 设置编码    
    		chain.doFilter(httpServletRequestWrapper, response); 
         
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
