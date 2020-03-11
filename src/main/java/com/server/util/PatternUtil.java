package com.server.util;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


/**
 * 正则表达式工具
 * @author 黄曾海
 *
 */
public class PatternUtil {

	
	private static Pattern mobilePattern = Pattern.compile("^1(3|5|7|8|9)[0-9]{9}$");
	/**
	 * 替换用户名称（名称是手机号码的，规则：13487884141   134****4141）
	 * @param userName  用户名称
	 * @return
	 */
	public static String replaceUserName(String userName){
		
		if(!StringUtils.isBlank(userName)){
			
			if(mobilePattern.matcher(userName).matches()){
				char [] cs = new char[11];
				
				for (int i = 0; i < cs.length; i++) {
					
					if(i>2&&i<7){
						cs[i] = '*';
					}else{
						cs[i] = userName.charAt(i);
					}
					
				}
				
				return new String(cs);
				
			}
			
		}
		
		return userName;
		
	}
	
}
