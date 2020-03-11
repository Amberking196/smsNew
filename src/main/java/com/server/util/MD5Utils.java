package com.server.util;
import java.security.MessageDigest;

import org.springframework.stereotype.Component;  

@Component
public class MD5Utils {
	public  String getMD5(String inStr) {  
        MessageDigest md5 = null;  
        try {  
            md5 = MessageDigest.getInstance("MD5");  
        } catch (Exception e) {  
              
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = inStr.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
   
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
   
        byte[] md5Bytes = md5.digest(byteArray);  
   
        StringBuffer hexValue = new StringBuffer();  
   
        for (int i = 0; i < md5Bytes.length; i++) {  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
   
        return hexValue.toString();  
    }  
	

	   /**
	    * 
	    * Function  : 加密指定字符串
	    * @author   : 
	    * @param s  : 被加密参数
	    * @return   : 加密后的结果
	    */
	    public  String getMd5(String s)
	    {
	        char hexDigits[] = {
	            '5', '0', '5', '6', '2', '9', '6', '2', '5', 'q',
	            'b', 'l', 'e', 's', 's', 'y'
	        };
	        try{
	        char str[];
	        byte strTemp[] = s.getBytes();
	        MessageDigest mdTemp = MessageDigest.getInstance("MD5");
	        mdTemp.update(strTemp);
	        byte md[] = mdTemp.digest();
	        int j = md.length;
	        str = new char[j * 2];
	        int k = 0;
	        for (int i = 0; i < j; i++)
	        {
	            byte byte0 = md[i];
	            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	            str[k++] = hexDigits[byte0 & 0xf];
	        }

	        return new String(str);
	        }catch(Exception e){
	        return null;
	        }
	    }
	    
}
