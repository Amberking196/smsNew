package com.server.util;

import java.util.Random;
import java.util.UUID;

/**
 * @author lixiaoying 2017年9月13日 下午4:12:42
 */
public class UUIDUtil {
	
	private static final Random random = new Random();
	
	public static String[] getUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] retArray = new String[number];
		for (int i = 0; i < number; i++) {
			retArray[i] = getUUID();
		}
		return retArray;
	}

	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		// 去掉“-”符号
		return uuid.replaceAll("-", "");
	}
	
	public static String createPayCode30(String payCode){
		int length = payCode.length();
		String result = null;
		if(length < 30){
			int deleg = 30 - length;
			StringBuffer a = new StringBuffer();
			for(int i=0 ;i <deleg ;i++){
				a.append(random.nextInt(10));
			}
			result = payCode + a.toString();
		}else {
			result = payCode;
		}
		return result;
	}
	public static void main(String[] args) {
		System.out.println("119880000832018102311232551212".length());
		String createPayCode30 = createPayCode30("119880000832018102311232551212");
		System.out.println(createPayCode30);
		System.out.println(createPayCode30.length());
//		System.out.println(10^2);
//		System.out.println(getUUID());
	}
}
