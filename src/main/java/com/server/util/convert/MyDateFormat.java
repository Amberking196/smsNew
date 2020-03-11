package com.server.util.convert;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class MyDateFormat extends DateFormat {
	
	private DateFormat dateFormat;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
	private static SimpleDateFormat sdfDetail = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
	static{
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		sdfDetail.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
	}

	public MyDateFormat(DateFormat dateFormat){
		this.dateFormat = dateFormat;
	}
	
	@Override
	public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
		// TODO Auto-generated method stub
		return dateFormat.format(date, toAppendTo, fieldPosition);
	}

	@Override
	public Date parse(String source, ParsePosition pos) {
		// TODO Auto-generated method stub
		Date date = null;
		if(source.matches("\\d{4}-\\d{1,2}-\\d{1,2}")){
			try{
				date=sdf.parse(source, pos);
			}catch(Exception e){
				date=dateFormat.parse(source, pos);
			}
		}else if(source.matches("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")){
			try{
				date=sdfDetail.parse(source, pos);
			}catch(Exception e){
				date=dateFormat.parse(source, pos);
			}
		}
		return date;
	}
	
	@Override
	public Date parse(String source) throws ParseException{
		Date date = null;
		if(source.matches("\\d{4}-\\d{1,2}-\\d{1,2}")){
			try{
				date=sdf.parse(source);
			}catch(Exception e){
				date=dateFormat.parse(source);
			}
		}else if(source.matches("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")){
			try{
				date=sdfDetail.parse(source);
			}catch(Exception e){
				date=dateFormat.parse(source);
			}
		}
		return date;
	}
	
	@Override
	public Object clone() {
		Object format = dateFormat.clone();
		return new MyDateFormat((DateFormat) format);
	}

}
