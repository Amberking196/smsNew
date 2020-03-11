package com.server.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public final class DateUtils {

	/**
	 * 秒的毫秒数
	 */
	public final static long SECOND_MILLIS = 1000;

	/**
	 * 分钟的毫秒数
	 */
	public final static long MIN_MILLIS = 60 * SECOND_MILLIS;

	/**
	 * 小时的毫秒数
	 */
	public final static long HOUR_MILLIS = 60 * MIN_MILLIS;

	/**
	 * 天的毫秒数
	 */
	public final static long DAY_MILLIS = 24 * HOUR_MILLIS;
	public static final String DATE_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String DATE_PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String DATE_PATTERN_YYYYMMDDHHMM = "yyyyMMddHHmm";
	public static final String DATE_PATTERN_HHMMSS = "HH:mm:ss";
	public static final String DATE_PATTERN_YYYYMMDD = "yyyyMMdd";
	public static final String DATE_PATTERN_YYYY_MM_DDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PATTERN_YYYY_MM_DDHHMM = "yyyy-MM-dd HH:mm";
	public static final String DATE_PATTERN_YYYY_MM = "yyyy-MM";

	public static HashMap sFestival = new HashMap();// 阳历固定节日
	public static HashMap wFestival = new HashMap();// 阳历星期节日
	public static HashMap lFestival = new HashMap();// 阴历节日

	public static void main(String[] args) {
		System.out.println(DateUtils.addMonth(3));
		
	}
	
	/**
	 * 计算俩时间差的分钟数
	 * @param curTime
	 * @param beforeTime
	 */
	public static Long getTimeSubtract(String curTime, String beforeTime) {
		DateFormat df = new SimpleDateFormat(DATE_PATTERN_YYYYMMDDHHMMSS);
		try {
			Date d1 = df.parse(curTime);
			Date d2 = df.parse(beforeTime);
			long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒数
			return diff;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 计算两时间的秒差
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Long getTimeSecond(Date startDate,Date endDate) {
		long time = endDate.getTime()-startDate.getTime();
		return time;
	}
	
	public static void initializtion_Data(Calendar calendar) {// 初始化函数
		// //////////////////////////////////////////////////
		/*
		 * 节日和纪念日 格式：起始年(yyyy)+月(mm)+日(dd) 0000表示起始年不明
		 */
		// String []sFestival_={
		sFestival.put("0101", "元旦");
		sFestival.put("0214", "情人节");
		sFestival.put("0308", "妇女节");
		sFestival.put("0312", "植树节");
		sFestival.put("0401", "愚人节");
		sFestival.put("0501", "劳动节");
		sFestival.put("0504", "青年节");
		sFestival.put("0601", "儿童节");
		sFestival.put("0701", "建党节");
		sFestival.put("0801", "建军节");
		sFestival.put("0910", "教师节");
		sFestival.put("1001", "国庆节");
		sFestival.put("1031", "万圣节");
		sFestival.put("1112", "孙中山诞辰");
		sFestival.put("1225", "圣诞节");
		sFestival.put("1226", "毛泽东诞辰");
		// };
		// 某月第几个星期几
		// 起始年(4位)+月(2位)+第几个(1位)+星期几(1位)
		// String []wFestival={
		wFestival.put("0520", "母亲节");
		wFestival.put("0630", "父亲节");
		wFestival.put("1144", "感恩节");
		// };
		// 农历 99表示月最后一天
		// String []lFestival={
		lFestival.put("0101", "春 节");
		lFestival.put("0102", "大年初二");
		lFestival.put("0103", "大年初三");
		lFestival.put("0115", "元宵节");
		lFestival.put("0505", "端午节");
		lFestival.put("0707", "七 夕");
		lFestival.put("0815", "中秋节");
		lFestival.put("0909", "重阳节");
		lFestival.put("1208", "腊八节");
		lFestival.put("1299", "除 夕");
		// };
	}

	public static String getLunarWithFestival(Calendar calendar) {// 获取农历及节日
		// ///////////////////////////////////////////////////////////
		initializtion_Data(calendar);
		int month = calendar.get(Calendar.MONTH);
		int weekindexDay;
		int weekindexMonth;

		weekindexMonth = calendar.get(Calendar.WEEK_OF_MONTH) - 1;
		weekindexDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		// /////////////////////////////////////////////
		String today_, month_;
		today_ = day < 10 ? "0" + day : "" + day;
		month_ = month < 10 ? "0" + (month + 1) : "" + (month + 1);
		Lunar lunar = new Lunar(calendar);
		String lunar_ = lunar.toString();
		// /////////////////////////////////////////
		if (null != sFestival.get(month_ + today_))
			lunar_ += sFestival.get(month_ + today_);
		// /////
		String wFestival_ = month_ + (weekindexMonth) + (weekindexDay);

		if (null != wFestival.get(wFestival_)) {
			lunar_ += wFestival.get(wFestival_);
			System.out.println(wFestival_);
		}

		if (null != lFestival.get(lunar.numeric_md()))
			lunar_ += lFestival.get(lunar.numeric_md());

		// 计算除夕
		Calendar temp_calendar = Calendar.getInstance();
		temp_calendar.set(calendar.get(Calendar.YEAR), month, day + 1);

		// temp_calendar.add(Calendar.DAY_OF_MONTH,1);
		Lunar temp_lunar = new Lunar(temp_calendar);
		String temp_str = temp_lunar.numeric_md();
		if (temp_str.equals("0101"))
			lunar_ += lFestival.get("1299");
		// /计算除夕结束
		// ////////////////////////////////////////
		// if (day < 10)day_str = today_;

		return lunar_ + " " + lunar.getSolarTerms();

	}

	/**
	 * 获取指定日期的星期数,周一至周日,1至7
	 * 
	 * @param time
	 * @return
	 */
	public final static int getTodayWeek(Date time) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(time);
		return cl.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 获取指定日期的星期数,周一至周日,1至7
	 * 
	 * @param time
	 * @return
	 */
	public final static String getTodayWeek(Date time, boolean isCh) {
		int week = getTodayWeek(time);
		if (isCh) {
			switch (week) {
			case 1:
				return "星期一";
			case 2:
				return "星期二";
			case 3:
				return "星期三";
			case 4:
				return "星期四";
			case 5:
				return "星期五";
			case 6:
				return "星期六";
			case 7:
				return "星期日";
			}
		}
		return "" + week;
	}

	/**
	 * 相差天数
	 * 
	 * @param time
	 * @return
	 */
	public final static int getDateDifCount(Date d1, Date d2) {
		Long temp = (d1.getTime() - d2.getTime()) / 86400000;
		return Math.abs(temp.intValue());
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
	 * @return
	 */
	public final static String getYearDateStr(Date time) {
		return getDateStr(time, "yyyy-MM-dd");
	}

	public static Timestamp addMonth(int month) {
		Date dNow = new Date();
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(dNow);
		calendar.add(calendar.MONTH, month);
		dBefore = calendar.getTime();
		return new Timestamp(dBefore.getTime());
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
	 * 转换日期时间串为时间对象
	 * 
	 * @param timeStr
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public final static Date strToDate(String timeStr, String format) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
		try {
			return sdf.parse(timeStr);
		} catch (Exception e) {
			return new Date();
		}
	}

	/**
	 * 获取当前时间前N天的整点毫秒数
	 */
	public final static Long sameDay(Long currentTime, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(currentTime);
		cal.add(Calendar.DAY_OF_YEAR, day * -1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTimeInMillis();
	}

	/**
	 * 获取当年的第一天
	 * 
	 * @param year
	 * @return
	 */
	public static Date getCurrYearFirst() {
		Calendar currCal = Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return getYearFirst(currentYear);
	}

	/**
	 * 获取当年的最后一天
	 * 
	 * @param year
	 * @return
	 */
	public static Date getCurrYearLast() {
		Calendar currCal = Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return getYearLast(currentYear);
	}

	/**
	 * 获取某年第一天日期
	 * 
	 * @param year
	 *            年份
	 * @return Date
	 */
	public static Date getYearFirst(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	/**
	 * 获取某年最后一天日期
	 * 
	 * @param year
	 *            年份
	 * @return Date
	 */
	public static Date getYearLast(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();

		return currYearLast;
	}

	/**
	 * 获得当前N个月的第一天
	 * 
	 */
	public static Date changeDateTime(Date theDate, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(theDate);
		cal.add(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}
}

/*
 * 以下是阴历对象； 是从网络中得来的；
 */
class Lunar {
	private int year;
	private int month;
	private int day;
	private boolean leap;
	private String solarTerms = "";
	final static String chineseNumber[] = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二" };
	final static String Big_Or_Small[] = { "大", "小", "大", "小", "大", "小", "大", "大", "小", "大", "小", "大" };
	private String[] LunarHolDayName = { "小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨", "立夏", "小满", "芒种", "夏至", "小暑",
			"大暑", "立秋", "处暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至" };

	final static long[] STermInfo = new long[] { 0, 21208, 42467, 63836, 85337, 107014, 128867, 150921, 173149, 195551,
			218072, 240693, 263343, 285989, 308563, 331033, 353350, 375494, 397447, 419210, 440795, 462224, 483532,
			504758 };

	static SimpleDateFormat chineseDateFormat = new SimpleDateFormat(" yyyy年MM月dd日 ");

	// 农历月份大小压缩表，两个字节表示一年。两个字节共十六个二进制位数，
	// 前四个位数表示闰月月份，后十二个位数表示十二个农历月份的大小。
	final static long[] lunarInfo = new long[] { 0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0,
			0x09ad0, 0x055d2, 0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
			0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566, 0x0d4a0,
			0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0,
			0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573,
			0x052d0, 0x0a9a8, 0x0e950, 0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950,
			0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
			0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970,
			0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960, 0x0d954, 0x0d4a0, 0x0da50,
			0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0,
			0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260,
			0x0ea65, 0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
			0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };

	// ====== 传回农历 y年的总天数
	final private static int yearDays(int y) {
		int i, sum = 348;
		for (i = 0x8000; i > 0x8; i >>= 1) {
			if ((lunarInfo[y - 1900] & i) != 0)
				sum += 1;
		}
		return (sum + leapDays(y));
	}

	// ====== 传回农历 y年闰月的天数
	final private static int leapDays(int y) {
		if (leapMonth(y) != 0) {
			if ((lunarInfo[y - 1900] & 0x10000) != 0)
				return 30;
			else
				return 29;
		} else
			return 0;
	}

	// ====== 传回农历 y年闰哪个月 1-12 , 没闰传回 0
	final private static int leapMonth(int y) {
		return (int) (lunarInfo[y - 1900] & 0xf);
	}

	// ====== 传回农历 y年m月的总天数
	final private static int monthDays(int y, int m) {
		if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0)
			return 29;
		else
			return 30;
	}

	// ====== 传回农历 y年的生肖
	final public String animalsYear() {
		final String[] Animals = new String[] { "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪" };
		return Animals[(year - 4) % 12];
	}

	// ====== 传入 月日的offset 传回干支, 0=甲子
	final private static String cyclicalm(int num) {
		final String[] Gan = new String[] { "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸" };
		final String[] Zhi = new String[] { "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥" };
		return (Gan[num % 10] + Zhi[num % 12]);
	}

	// ====== 传入 offset 传回干支, 0=甲子
	final public String cyclical() {
		int num = year - 1900 + 36;
		return (cyclicalm(num));
	}

	// ===== 某年的第n个节气为几日(从0小寒起算)
	final public int sTerm(int y, int n) {
		Calendar c = Calendar.getInstance();
		c.set(1900, 0, 6, 2, 5, 0);
		long temp = c.getTime().getTime();
		c.setTime(new Date((long) ((31556925974.7 * (y - 1900) + STermInfo[n] * 60000L) + temp)));
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/** */
	/**
	 * 传出y年m月d日对应的农历. yearCyl3:农历年与1864的相差数 ? monCyl4:从1900年1月31日以来,闰月数
	 * dayCyl5:与1900年1月31日相差的天数,再加40 ?
	 * 
	 * @param cal
	 * @return
	 */
	@SuppressWarnings("unused")
	public Lunar(Calendar cal) {
		// cal.add(cal.get(Calendar.DAY_OF_MONTH),1);
		// @SuppressWarnings( " unused " )

		int yearCyl, monCyl, dayCyl;
		int leapMonth = 0;
		Date baseDate = null;
		try {
			baseDate = chineseDateFormat.parse(" 1900年1月31日 ");
		} catch (ParseException e) {
			e.printStackTrace(); // To change body of catch statement use
									// Options | File Templates.
		}

		// 求出和1900年1月31日相差的天数
		int offset = (int) ((cal.getTime().getTime() - baseDate.getTime()) / 86400000L);
		dayCyl = offset + 40;
		monCyl = 14;

		// 用offset减去每农历年的天数
		// 计算当天是农历第几天
		// i最终结果是农历的年份
		// offset是当年的第几天
		int iYear, daysOfYear = 0;
		for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {
			daysOfYear = yearDays(iYear);
			offset -= daysOfYear;
			monCyl += 12;
		}
		if (offset < 0) {
			offset += daysOfYear;
			iYear--;
			monCyl -= 12;
		}
		// 农历年份
		year = iYear;

		yearCyl = iYear - 1864;
		leapMonth = leapMonth(iYear); // 闰哪个月,1-12
		leap = false;

		// 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
		int iMonth, daysOfMonth = 0;
		for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
			// 闰月
			if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
				--iMonth;
				leap = true;
				daysOfMonth = leapDays(year);
			} else
				daysOfMonth = monthDays(year, iMonth);

			offset -= daysOfMonth;
			// 解除闰月
			if (leap && iMonth == (leapMonth + 1))
				leap = false;
			if (!leap)
				monCyl++;
		}
		// offset为0时，并且刚才计算的月份是闰月，要校正
		if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
			if (leap) {
				leap = false;
			} else {
				leap = true;
				--iMonth;
				--monCyl;
			}
		}
		// offset小于0时，也要校正
		if (offset < 0) {
			offset += daysOfMonth;
			--iMonth;
			--monCyl;
		}
		month = iMonth;
		day = offset + 1;

		// ******************计算节气**********//
		if (day == sTerm(year, (month - 1) * 2))
			solarTerms = LunarHolDayName[(month - 1) * 2];
		else if (month == sTerm(year, (month - 1) * 2 + 1))
			solarTerms = LunarHolDayName[(month - 1) * 2 + 1];
		else
			solarTerms = "";

	}

	public static String getChinaDayString(int day) {
		String chineseTen[] = { "初", "十", "廿", "卅" };
		int n = day % 10 == 0 ? 9 : day % 10 - 1;
		if (day > 30)
			return "";
		if (day == 10)
			return "初十";
		else
			return chineseTen[day / 10] + chineseNumber[n];
	}

	public String toString() {
		return /* cyclical() + "年" + */(leap ? "闰" : "") + chineseNumber[month - 1] + "月" + getChinaDayString(day);
	}

	public String numeric_md() {// 返回阿拉伯数字的阴历日期
		String temp_day;
		String temp_mon;
		temp_mon = month < 10 ? "0" + month : "" + month;
		temp_day = day < 10 ? "0" + day : "" + day;

		return temp_mon + temp_day;
	}

	public String get_month() {// 返回阴历的月份
		return chineseNumber[month - 1];
	}

	public String get_date() {// 返回阴历的天
		return getChinaDayString(day);
	}

	public String get_Big_Or_Small() {// 返回的月份的大或小
		return Big_Or_Small[month - 1];
	}

	public String getSolarTerms() {// 返回的节气
		return solarTerms;
	}

	/**
	 * 修改日期
	 * 
	 * @param theDate
	 *            待修改的日期
	 * @param addMonth
	 *            加减月数
	 * @param addDays
	 *            加减的天数
	 * @param hour
	 *            设置的小时
	 * @param minute
	 *            设置的分
	 * @param second
	 *            设置的秒
	 * @return 修改后的日期
	 */
	public static Date changeDateTime(Date theDate, int addMonth, int addDays, int hour, int minute, int second) {
		if (theDate == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(theDate);
		cal.add(Calendar.MONTH, addMonth);
		cal.add(Calendar.DAY_OF_MONTH, addDays);
		if (hour >= 0 && hour <= 24) {
			cal.set(Calendar.HOUR_OF_DAY, hour);
		}
		if (minute >= 0 && minute <= 60) {
			cal.set(Calendar.MINUTE, minute);
		}
		if (second >= 0 && second <= 60) {
			cal.set(Calendar.SECOND, second);
		}
		return cal.getTime();
	}
	
}
