package com.lmk.ct.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类 默认使用 "yyyy-MM-dd HH:mm:ss" 格式化日期
 * @author LaoMake
 * @since 1.0
 *
 */
public final class DateUtils {
	/**
	 * 英文简写（默认）如：2010-12-01
	 */
	public static String FORMAT_SHORT = "yyyy-MM-dd";
	
	/**
	 * 英文全称  如：2010-12-01 23:15:06
	 */
	public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.SSS";
	
	/**
	 * 中文简写  如：2010年12月01日
	 */
	public static String FORMAT_SHORT_CN = "yyyy年MM月dd";
	
	/**
	 * 中文全称  如：2010年12月01日  23时15分06秒
	 */
	public static String FORMAT_LONG_CN = "yyyy年MM月dd日  HH时mm分ss秒";
	
	/**
	 * 精确到毫秒的完整中文时间
	 */
	public static String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";

	
	/**
	 * 获得默认的 date pattern
	 * @return
	 */
	public static String getDatePattern() {
		return FORMAT_SHORT;
	}

	/**
	 * 根据预设格式返回当前日期
	 * @return
	 */
	public static String getToday() {
		return format(new Date(), getDatePattern());
	}
	
	/**
	 * 根据预设格式返回当前日期
	 * @return
	 */
	public static String getNow() {
		return format(new Date());
	}
	
	/**
	 * 根据用户格式返回当前日期
	 * @param format
	 * @return
	 */
	public static String getNow(String format) {
		return format(new Date(), format);
	}

	/**
	 * 使用预设格式格式化日期
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return format(date, getDatePattern());
	}

	/**
	 * 使用用户格式格式化日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		String returnValue = "";
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			returnValue = df.format(date);
		}
		return (returnValue);
	}

	/**
	 * 使用预设格式提取字符串日期
	 * @param strDate
	 * @return
	 */
	public static Date parse(String strDate) {
		return parse(strDate, getDatePattern());
	}

	/**
	 * 使用用户格式提取字符串日期
	 * @param strDate
	 * @param pattern
	 * @return
	 */
	public static Date parse(String strDate, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		try {
			return df.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 在日期上增加数个整月
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date addMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		return cal.getTime();
	}
	
	/**
	 * 在日期上增加天数
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date addDay(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, n);
		return cal.getTime();
	}
	
	/**
	 * 在日期上增加小时
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date addHour(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, n);
		return cal.getTime();
	}
	
	/**
	 * 在日期上增加分钟
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date addMinute(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, n);
		return cal.getTime();
	}

	/**
	 * 获取时间戳
	 * @return
	 */
	public static String getTimeString() {
		SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL);
		Calendar calendar = Calendar.getInstance();
		return df.format(calendar.getTime());
	}

	/**
	 * 获取日期年份
	 * @param date
	 * @return
	 */
	public static String getYear(Date date) {
		return format(date).substring(0, 4);
	}
	
	/**
	 * 按默认格式的字符串距离今天的天数
	 * @param date
	 * @return
	 */
	public static int countDays (String date) {
		long t = Calendar.getInstance().getTime().getTime();
		Calendar c = Calendar.getInstance();
		c.setTime(parse(date));
		long t1 = c.getTime().getTime();
		return (int)(t/1000 - t1/1000)/3600/24;
	}
	
	/**
	 * 按用户格式字符串距离今天的天数
	 * @param date
	 * @param format
	 * @return
	 */
	public static int countDays (String date, String format) {
		long t = Calendar.getInstance().getTimeInMillis();
		Calendar c = Calendar.getInstance();
		c.setTime(parse(date, format));
		long t1 = c.getTimeInMillis();
		return Math.abs((int)(t/1000 - t1/1000)/3600/24);
	}
	
	/**
	 * 获取距离现在的友好时间
	 * @param date
	 * @return
	 */
	public static String friendlyDate(Date date) {
		String result = null;
		Date now = new Date();
		
		long times = now.getTime() - date.getTime();
		long count = 0;
		
		if(times < 0) {
			result = "数据异常！";
		}else {
			// 判断是否为同一天
			if(format(date).equals(format(now))) {
				if(times < 1000 * 60){// 小于1分钟
					result = "刚刚";
				} else if(times < 1000 * 60 * 60){// 小于1小时
					count = times / (1000 * 60);
					result = count + "分钟前";
				} else { // 大于1小时
					count = times / (1000 * 60 * 60 );
					result = count + "小时前";
				}
			}else {
				count = times / (1000 * 60 * 60 * 24);
				if(count == 0)
					count++;
				result = count + "天前";
			}
		}
		return result;
	}
}
