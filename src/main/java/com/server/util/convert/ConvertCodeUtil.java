package com.server.util.convert;

public class ConvertCodeUtil {
	/**
	 * 转为六位String类型
	 * */
	public static String codeConvertString(Long code){
		if(code<10){
			return "00000"+code;
		}else if (code<100){
			return "0000"+code;
		}else if (code<1000){
			return "000"+code;
		}else if (code<10000){
			return "00"+code;
		}else if (code<100000){
			return "0"+code;
		}else{
			return code.toString();
		}
	}
}