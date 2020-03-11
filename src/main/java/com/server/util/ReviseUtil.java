package com.server.util;


public class ReviseUtil {

	private final static String RESTART_COMMAND="r:1&1$";
	
	public static Integer checkCodeSum(String command){
		if(StringUtil.isNotBlank(command)){
			int sum = 0;
			char[] ss = command.toCharArray();
			for (char c : ss) {
				if(Character.isDigit(c)){
					Integer unitChar = Integer.valueOf(String.valueOf(c));
					sum+=unitChar;
				}
			}
			return sum;
		}
		return null;
	}
	
	
	public static String restartCommand(){
		return RESTART_COMMAND;
	}
	
//	public static void main(String[] args) {
//		Integer checkCodeSum = checkCodeSum("w:3,1,23,4700&$");
//		System.out.println(checkCodeSum);
//	}
}
