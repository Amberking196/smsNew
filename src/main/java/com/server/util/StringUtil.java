package com.server.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;
 
import java.util.regex.Matcher; 

import static java.util.regex.Pattern.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
 

@Component
public class StringUtil {
	public static Log log = LogFactory.getLog(StringUtil.class);

	public static Integer value = 0;
	public static String lowerFirstCase(String str) {  
	    return str.substring(0, 1).toLowerCase() + str.substring(1);  
	}  
 
	public  String getIntValue() {
		String sValue = "";
		synchronized (value) {
			if (value == 2000) {
				value = 0;
			} else {
				value++;
			}
			sValue = String.valueOf(System.currentTimeMillis());
			sValue =  setOrder(sValue, 3) +sValue +"-"+ value;
			 
		}
		return sValue;
	}

	public String[] getStringArray(String str, String flag) {
		String[] sArray = str.split(flag);
		return sArray;
	}

  
	public static String getISO8859String(String str) {
		try {
			return new String(str.getBytes(), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
		}
		return "";
	}

	public static String getGBK(String str) {
		try {
			return new String(str.getBytes("ISO-8859-1"), "GBK");
		} catch (Exception e) {
		}
		return "";
	}

	public static String getLastInsertTime(Long id) {
		return "iid=" + id + "&time=" + DateUtil.getCurrentTime();
	}
   
	public String getBeanName(String string) {
		String[] aString = string.split("\\?");
		if (aString.length > 0) {
			return aString[0];
		}
		return null;
	}

	public static String removeNull(String str) {
		// System.err.println("StringUtil.removeNull : " + str);
		if(str!=null){
			str = str.replaceAll("null:\\^~", "");
			str = str.replaceAll(":\\^~null", "");
		}
		return str;
	}
	
 
	  

	public static String createUrl(Map<Object, Object> uMap) { 
		StringBuffer buf = new StringBuffer(); 
		Set key = uMap.keySet();
		boolean flag = true;
		for (Object object : key) {
			if (((String) object).equals("url"))
				continue;
			if (flag) {
				buf.append(object).append("=").append(uMap.get(object));
				flag = false;
			} else {
				buf.append("&").append(object).append("=").append(
						uMap.get(object));
			}
		}
		return buf.toString();
	}

	// tag by string
	public String substringBefore(String string, String str) {
		int len = string.indexOf(str);
		if (len != -1) {
			return string.substring(0, len);
		}
		return null;
	}

 public List<String> splitStringList(String str){
		List<String> list = new ArrayList<String>();
		int indexBegin = str.indexOf("[");
		int indexEnd = str.indexOf("]");
		if(indexBegin!=-1 && indexEnd!=-1){
			str = str.substring(indexBegin+1,indexEnd);
			String aString[] = str.split(",");
			for (String string : aString) {
				string = string.trim();
				list.add(string.substring(1,string.length()-1));
			}
		}
		return list;
	}
    //	ת���ַ��List<Integer>
	//"[\"11452386\", \"11452381\", \"11452382\", \"11452384\", \"11452383\", \"11452385\"]";
	public List<Integer> splitIntegerList(String str){
		List<Integer> list = new ArrayList<Integer>();
		int indexBegin = str.indexOf("[");
		int indexEnd = str.indexOf("]");
		if(indexBegin!=-1 && indexEnd!=-1){
			str = str.substring(indexBegin+1,indexEnd);
			String aString[] = str.split(",");
			for (String string : aString) {
				string = string.trim();
				string = string.substring(1,string.length()-1);
				try{
				Integer value = Integer.valueOf(string);
				list.add(value);
				}catch (Exception e) {
					System.err.println("splitIntegerList �� string = " + string +" �������֣�");
				}
			}
		}
		return list;
	}
	
	public String substringAfter(String string, String str) {
		int len = string.lastIndexOf(str);
		if (len != -1) {
			return string.substring(len + 1);
		}
		return null;
	}

	public boolean startWith(String string, String str) {
		return string.startsWith(str);
	}

	public boolean endWith(String string, String str) {
		return string.endsWith(str);
	}

	public static int getIntByFloatString(String lValue) {
		int index = lValue.indexOf(".");
		if (index != -1) {
			lValue = lValue.substring(0, index);
		}
		return new Integer(lValue);
	}

	
	// ����
	public static final String[] ask = {"~","!","@","#" ,"$","%","^","&","*","(",")",")","_","+","|","`","-","=","{","}",":",";","'",",",".","/","<",">","?" 
    ,"a","b","c","d","e","f","g","h","i","g","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z" 
    ,"A","B","C","D","E","F","G","H","I","G","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z" };
	
	public static final String key = "||-_-||Fack|You|Zhu|Ge|Liang||-_-||*5$2a#3@#WER23sfTYH43sfFsw3rdc&UfrtS#EawdB^Rh8y@#$wesfs3wt4HRt78FThe4r4e#$efw3setw34$#sdFw3rDVAaq243S";
	public static final int keyCount = key.length();
	public static final byte[] keychar = key.getBytes();
	
	public static final URLCodec code = new URLCodec();
	public static final Base64 base64 = new Base64();
	 
	
	public String getDecodeString(String msg) {
		try {
			if (msg == null) {
				return null;
			} 
			byte[] bb = base64.decode(code.decode(msg.getBytes()));
			byte[] bytes = new byte[bb.length];
			
			int i = 0;
			int len;
			byte s ;
			int mk ;
			int bbLen = bb.length;
			byte b;
			for(int j=0;i<bbLen;j++){
				b= bb[j];
				len = i % keyCount;
				i++;
				s = keychar[len];
				mk = b ^ s; 
				bytes[j] = (byte)mk;
			} 
			msg = new String(code.decode(bytes));
		} catch (DecoderException e1) { 
			return null;
		}
		return msg;
	}

	
	
	public String getDecodeString(String msg,String key) {
		try {
			if (msg == null) {
				return "";
			} 
			byte[] bb = base64.decode(code.decode(msg.getBytes()));
			byte[] bytes = new byte[bb.length];
			int keyCount = key.length();	 
			byte[] byteKey = key.getBytes(); 
			int i = 0;
			byte keychar = 'c';
			int mk = 0;
			byte b;
			int bbLen = bb.length;
			for(int j=0;i<bbLen;j++){
				int len = i % keyCount;
				i++;
				keychar =byteKey[len];
				b = bb[j];
				mk = b ^ keychar; 
				bytes[j] = (byte)mk;
			}
			msg = new String(code.decode(bytes)); 
		} catch (DecoderException e1) {
			return null;
		}
		return msg;
	}

	
 
	public String getEncodeString(String msg) {
		try{
			// url code
			msg = URLEncoder.encode(msg);
			// ����
			int keyCount = key.length();
			int msgCount = msg.length();
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < msgCount; i++) {
				String charnum = String.valueOf(msg.charAt(i));
				int len = i % keyCount;
				String keychar = String.valueOf(key.charAt(len));
				int m = charnum.getBytes()[0];
				int k = keychar.getBytes()[0];
				int mk = m ^ k;
				buf.append((char) mk);
			}
			msg = buf.toString();
			// base64 code
			msg = new String(base64.encode(msg.getBytes()));
			// url code
			msg = URLEncoder.encode(msg);
			}catch (Exception e) { 
			}
			return msg;
	}
	 
	
	public String getDecodeStringNoURLEncoder(String msg) {
	//	try {
			if (msg == null) {
				return "";
			}
			//msg = new String(code.decodeUrl(msg.getBytes()));
			// base64 code
			msg = new String(base64.decode(msg.getBytes()));
			// ����
			int keyCount = key.length();
			int msgCount = msg.length();
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < msgCount; i++) {
				String charnum = String.valueOf(msg.charAt(i));
				int len = i % keyCount;
				String keychar = String.valueOf(key.charAt(len));
				int m = charnum.getBytes()[0];
				int k = keychar.getBytes()[0];
				int mk = m ^ k;
				buf.append((char) mk);
			}
			msg = buf.toString();
			// url code
			//byte[] b = code.decodeUrl(msg.getBytes());
			//msg = new String(b);
		//} catch (DecoderException e1) {
		//	LogUtil.log(log, e1, "���ܴ���" + msg);
		//	return null;
		//}
		return msg;
	}
	 
	public static final String sp = "%#%";
	public String getMsg(String msg){
		msg = getDecodeString(msg);
		msg = getOrder(msg,5);
		
		String[] str = msg.split(sp);
		msg = str[0];
		
		String newKey = str[1];
		newKey = getDecodeString(newKey);
		newKey = getOrder(newKey,4);
		newKey = newKey.substring(0,newKey.length()-10);
		
		msg = getDecodeString(msg,newKey);
		msg = getOrder(msg, 7);
		
		msg = msg.substring(7);
		msg = msg.substring(0,msg.length()-8);
		
		byte[] b  = null;
		try {
			b = code.decodeUrl(msg.getBytes());
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		msg = new String(b);
		
		return msg;
	}
	 
	public String setOrder(String msg,int value){
		int len = msg.length();
		List[] aList = new ArrayList[value];
		for(int i=0;i<value;i++){
			aList[i] = new ArrayList<Character>();
		}
		for (int i = 0; i < len; i++) {
			int index = (i+1)%value; 
			aList[index].add(msg.charAt(i));
		}
		char[] c = new char[len];
		int j = 0;
		for(int i=0;i<value;i++){
			List<Character> list = aList[i];
			for (Character character : list) {
				c[j++] = character;
			}
		}
		String newMsg = new String(c);
		return newMsg;
	}

	//�ҿ�����˳��
	public String getOrder(String msg,int value){
		int len = msg.length();
		int[][] aIndex = new int[len][2];
		int ii = 0;
		char[] c = new char[len];
		int[] aInt = new int[value];
		for (int i = 0; i < len; i++) {
			int index = (i+1)%value; 
			aInt[index]++;
		}
		String[] str = new String[value];
		int begin = 0;
		for (int i = 0; i < aInt.length; i++) {
			int end = begin+aInt[i];
			str[i] = msg.substring(begin,end);
			begin = end;
		}
		for (int i = 0; i < len; i++) {
			int index = (i+1)%value; 
			if(i!=0 && i%value==0){
				ii++;
			}
			aIndex[i][0] = index;
			aIndex[i][1] = ii;
		}
		for (int i=0;i<len;i++) {
			int[] is =  aIndex[i];
			int arrayIndex = is[0];
			int index = is[1];
			c[i] = str[arrayIndex].charAt(index);
		}
		return new String(c);
	}
	 

	/**
	 * 检查字符串是否为<code>null</code>或空字符串<code>""</code>。
	 * 
	 * <pre>
	 * StringUtil.isEmpty(null)      = true
	 * StringUtil.isEmpty("")        = true
	 * StringUtil.isEmpty(" ")       = false
	 * StringUtil.isEmpty("bob")     = false
	 * StringUtil.isEmpty("  bob  ") = false
	 * </pre>
	 *
	 * @param str
	 *            要检查的字符串
	 *
	 * @return 如果为空, 则返回<code>true</code>
	 */
	public static boolean isEmpty(String str) {
		return ((str == null) || (str.length() == 0));
	}

	/**
	 * 检查字符串是否不是<code>null</code>和空字符串<code>""</code>。
	 * 
	 * <pre>
	 * StringUtil.isEmpty(null)      = false
	 * StringUtil.isEmpty("")        = false
	 * StringUtil.isEmpty(" ")       = true
	 * StringUtil.isEmpty("bob")     = true
	 * StringUtil.isEmpty("  bob  ") = true
	 * </pre>
	 *
	 * @param str
	 *            要检查的字符串
	 *
	 * @return 如果不为空, 则返回<code>true</code>
	 */
	public static boolean isNotEmpty(String str) {
		return ((str != null) && (str.length() > 0));
	}

	/**
	 * 检查字符串是否是空白：<code>null</code>、空字符串<code>""</code>或只有空白字符。
	 * 
	 * <pre>
	 * StringUtil.isBlank(null)      = true
	 * StringUtil.isBlank("")        = true
	 * StringUtil.isBlank(" ")       = true
	 * StringUtil.isBlank("bob")     = false
	 * StringUtil.isBlank("  bob  ") = false
	 * </pre>
	 *
	 * @param str
	 *            要检查的字符串
	 *
	 * @return 如果为空白, 则返回<code>true</code>
	 */
	public static boolean isBlank(String str) {
		int length;

		if ((str == null) || ((length = str.length()) == 0)) {
			return true;
		}

		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查字符串是否不是空白：<code>null</code>、空字符串<code>""</code>或只有空白字符。
	 * 
	 * <pre>
	 * StringUtil.isBlank(null)      = false
	 * StringUtil.isBlank("")        = false
	 * StringUtil.isBlank(" ")       = false
	 * StringUtil.isBlank("bob")     = true
	 * StringUtil.isBlank("  bob  ") = true
	 * </pre>
	 *
	 * @param str
	 *            要检查的字符串
	 *
	 * @return 如果为空白, 则返回<code>true</code>
	 */
	public static boolean isNotBlank(String str) {
		int length;

		if ((str == null) || ((length = str.length()) == 0)) {
			return false;
		}

		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 比较两个字符串（大小写敏感）。
	 * 
	 * <pre>
	 * StringUtil.equals(null, null)   = true
	 * StringUtil.equals(null, "abc")  = false
	 * StringUtil.equals("abc", null)  = false
	 * StringUtil.equals("abc", "abc") = true
	 * StringUtil.equals("abc", "ABC") = false
	 * </pre>
	 *
	 * @param str1
	 *            要比较的字符串1
	 * @param str2
	 *            要比较的字符串2
	 *
	 * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
	 */
	public static boolean equals(String str1, String str2) {
		if (str1 == null) {
			return str2 == null;
		}

		return str1.equals(str2);
	}

	/**
	 * 比较两个字符串（大小写不敏感）。
	 * 
	 * <pre>
	 * StringUtil.equalsIgnoreCase(null, null)   = true
	 * StringUtil.equalsIgnoreCase(null, "abc")  = false
	 * StringUtil.equalsIgnoreCase("abc", null)  = false
	 * StringUtil.equalsIgnoreCase("abc", "abc") = true
	 * StringUtil.equalsIgnoreCase("abc", "ABC") = true
	 * </pre>
	 *
	 * @param str1
	 *            要比较的字符串1
	 * @param str2
	 *            要比较的字符串2
	 *
	 * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
	 */
	public static boolean equalsIgnoreCase(String str1, String str2) {
		if (str1 == null) {
			return str2 == null;
		}

		return str1.equalsIgnoreCase(str2);
	}

	/***
	 * 获取6位随机数
	 * 
	 * @return
	 */
	public static String getRandnum() {
		String result = "";
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			result += random.nextInt(10);
		}
		return result;
	}

	/**
	 * 获取4位验证码
	 * 
	 * @return
	 */
	public static String getVerificationCode() {
		Random random = new Random();
		char[] strs = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		String str = "";
		for (int i = 0; i < 4; i++) {
			str += strs[random.nextInt(strs.length)];
		}
		return str;
	}

	/**
	 * 验证字符串是否为整数 true为是
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		try {
			Integer.valueOf(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 验证手机号码是否合法
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p;
		p = compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 验证邮箱是否合法
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		return matches(
				"^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",
				email);
	}

	public static void main(String args[]) {
		StringUtil sUtil = new StringUtil();
		String msg = "nami^_^admin^_^nanamimi123D`end`";
		String msg1 = "Eh1ANghJGRlEVg4dPQIcEn9dECNiUBkiCA8PChURRG4fTzgPAxRXD0cWdhM%3d";
		               
//		
	    System.out.println(sUtil.getEncodeString(msg));
	    System.out.println(sUtil.getDecodeString(msg1));
	}

}
