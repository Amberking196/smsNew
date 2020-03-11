
package com.server.module.zfb_trade.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.server.util.StringUtil;

/**
 * @ClassName: DateUtil 
 * @Description: 
 * @author 火神Ace 
 * @date 2017年11月27日 下午3:23:52
 */
public class DateUtil {		
    /** 年月日时分秒(无下划线) yyyyMMddHHmmss */
    public static final String dtLong = "yyyyMMddHHmmss";
	
    /** 完整时间 yyyy-MM-dd HH:mm:ss */
    public static final String simple = "yyyy-MM-dd HH:mm:ss";
    
    /** 年月日(无下划线) yyyyMMdd */
    public static final String dtShort = "yyyyMMdd";
	
        
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String HH_MM_SS = "HH:mm:ss";

    public static final String HH_MM = "HH:mm";

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    public static final String YYMMDDHHMMSS = "yyMMddHHmmss";

    public static final String MMDDHHMMSS = "MMddHHmmss";

    public static final String YYYY = "yyyy";
    
    /**
     * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
     * @return
     *      以yyyyMMddHHmmss为格式的当前系统时间
     */
	public  static String getOrderNum(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(dtLong);
		return df.format(date);
	}
	
	/**
	 * 获取系统当前日期(精确到毫秒)，格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public  static String getDateFormatter(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(simple);
		return df.format(date);
	}
	
	/**
	 * 获取系统当期年月日(精确到天)，格式：yyyyMMdd
	 * @return
	 */
	public static String getDate(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(dtShort);
		return df.format(date);
	}
	
	/**
	 * 产生随机的三位数
	 * @return
	 */
	public static String getThree(){
		Random rad=new Random();
		return rad.nextInt(1000)+"";
	}
	
	/**
	 * 把对象转换成字符串
	 * @param obj
	 * @return String 转换成字符串,若对象为null,则返回空字符串.
	 */
	public static String toString(Object obj) {
		if(obj == null)
			return "";
		
		return obj.toString();
	}
	
	/**
	 * 把对象转换为int数值.
	 * 
	 * @param obj
	 *            包含数字的对象.
	 * @return int 转换后的数值,对不能转换的对象返回0。
	 */
	public static int toInt(Object obj) {
		int a = 0;
		try {
			if (obj != null)
				a = Integer.parseInt(obj.toString());
		} catch (Exception e) {

		}
		return a;
	}
	
	/**
	 * 获取当前时间 yyyyMMddHHmmss
	 * @return String
	 */ 
	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}
	
	/**
	 * 获取当前时间 yyyyMMddHHmmss
	 * @return String
	 */ 
	public static String getCurrTime1() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = outFormat.format(now);
		return s;
	}
	
	/**
	 * 获取当前日期 yyyyMMdd
	 * @param date
	 * @return String
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String strDate = formatter.format(date);
		return strDate;
	}
	
	
	/**
	 * 获取编码字符集
	 * @param request
	 * @param response
	 * @return String
	 */
	public static String getCharacterEncoding(HttpServletRequest request,
			HttpServletResponse response) {
		
		if(null == request || null == response) {
			return "gbk";
		}
		
		String enc = request.getCharacterEncoding();
		if(null == enc || "".equals(enc)) {
			enc = response.getCharacterEncoding();
		}
		
		if(null == enc || "".equals(enc)) {
			enc = "gbk";
		}
		
		return enc;
	}
	
	/**
	 * 获取unix时间，从1970-01-01 00:00:00开始的秒数
	 * @param date
	 * @return long
	 */
	public static long getUnixTime(Date date) {
		if( null == date ) {
			return 0;
		}
		
		return date.getTime()/1000;
	}
		
	/**
	 * 时间转换成字符串
	 * @param date 时间
	 * @param formatType 格式化类型
	 * @return String
	 */
	public static String date2String(Date date, String formatType) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatType);
		return sdf.format(date);
	}
	
	
	/**
	 * 把字节数组转化为十六进制字符串
	 * @param bArray
	 * @return
	 */
	public static  String bytesToHexString(byte[] bArray) { 
	    StringBuffer sb = new StringBuffer(bArray.length); 
	    String str; 
	    for (int i = 0; i < bArray.length; i++) { 
	    	str = Integer.toHexString(0xFF & bArray[i]); 
	    	
		     if (str.length() < 2) {
		      sb.append(0); 
		     }
		     sb.append(str.toUpperCase()); 
	    } 
	    return sb.toString(); 
	} 
	
	   /**
     * Return Date
     *
     * @param date    : Date String
     * @param pattern : Date Format
     * @return : Date
     */
    public static Date format(String date, String pattern) {
        if (pattern == null || pattern.equals("") || pattern.equals("null")||StringUtil.isBlank(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (date == null || date.equals("") || date.equals("null")) {
            return new Date();
        }
        Date d = null;
        try {
        	java.text.SimpleDateFormat format= new java.text.SimpleDateFormat(pattern);
        	format.setLenient(false);
            d =format.parse(date);
        } catch (ParseException pe) {
        }
        return d;
    }
	
    /**
     * Return Date
     *
     * @param date : Date String
     * @return : Date
     */
    public static Date format(String date) {
        if (StringUtil.isNotEmpty(date)){
            if (date.length() == 5){
                return format(date, HH_MM);
            }else if(date.length() == 8){
                return format(date, HH_MM_SS);
            }else if (date.length() == 10){
                return format(date, YYYY_MM_DD);
            }else if (date.length() == 16){
                return format(date, YYYY_MM_DD_HH_MM);
            }else if (date.length() == 19){
                return format(date, YYYY_MM_DD_HH_MM_SS);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.SECONDS);
    }
    
    public static long getDateDiffInDay(Date date1, Date date2) {
        return getDateDiff(date1, date2, TimeUnit.DAYS);
    }
    
    /**
     * @Description: 返回毫秒
     */
    public static long getDateDiff(Date date1, Date date2) {
    	return (date2.getTime() - date1.getTime()) / 1000;
    }
    
    /**
     * Return Date
     *
     * @param date : Date String
     * @return : Date
     */
    public static Date dateAddMinute(Date date, int minute) {
    	  Date afterDate = new Date(date.getTime() + minute*60000);
    	  return afterDate;
    }  
    
    /**
	 * 获取指定日期的时间串
	 * 
	 * @param time
	 * @return
	 */
	public final static String getDateStr(Date time) {
		return getDateStr(time, "MMdd");
	}
	
	/**
	 * 获取指定日期的时间串
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public final static String getDateStr(Date time, String format) {
		if (time == null) {
			return null;
		}
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
		return sdf.format(time).toString();
	}
	
	/**
	 * 获取当前时间
	 * 
	 * @return Timestamp
	 */
	public final static Timestamp curTime() {
		return new Timestamp(System.currentTimeMillis());
	}
}
