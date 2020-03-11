package com.server.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PayCodeUtil {
	
	static SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmss");

	public static String genPayCode(Integer type, String vmCode) {
		String d = sf.format(new Date());
		String create = null;
		if(vmCode.length() == 11){
			create = vmCode.substring(1, 11);  
		}else{
			create = vmCode;
		}
		String code = type + create + d;
		return code;
	}
}
