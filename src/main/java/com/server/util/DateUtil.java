package com.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.springframework.stereotype.Component;
@Component
public class DateUtil {
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat sdfdetail = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String getAgeByYear(int age){
		GregorianCalendar ca = new GregorianCalendar();
		ca.setTime(new Date());
		ca.add(GregorianCalendar.YEAR, -age);
		Date date = ca.getTime();
		return sdf.format(date);
	}
	
	/**
	 * **********************************************
	 * �� �� �� ��
	 * @param age
	 * @return
	 ***********************************************
	 */
	public static String getDateTime(long time){
		return sdf.format(new Date(time*1000));
	}
	
	
	/**
	 * **********************************************
	 * �� �� ʱ �� ��  �� �� ��
	 * @param date
	 * @return
	 ***********************************************
	 */
	public static int getYearByAge(Date date){
		Date nowDate = new Date();
		long time = nowDate.getTime() - date.getTime();
		return (int)(time/31536000000l);
	}
	
 
	/**
	 * **********************************************
	 * obtains current time
	 * @param args
	 * **********************************************
	 */
	public static long getCurrentDatetime(){
		long time = System.currentTimeMillis()/1000;
		return time;
	}
	
	public static long getLastDatetime(int day){
		long time = getCurrentDatetime();
		time = time+(day*60*60*24);
		return time;
	}
	
	public static String getCurrentTime(){
		return sdfdetail.format(new Date());
	}
	
	
	public static String formatYYYYMMDD(Date date){
		if(date!=null){
			return sdf.format(date);
		}else{
			return null;
		}
	}
	
	public static String formatYYYYMMDDHHMMSS(Date date){
		if(date!=null){
			return sdfdetail.format(date);
		}else{
			return null;
		}
		
	}
	
	public static void main(String[] args) {
		System.out.println(getCurrentDatetime());
	}
	
	public static Date formatToDate(String date , SimpleDateFormat format){
		try {
			return format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}



