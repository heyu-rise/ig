package org.heyu.ig.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * 常用工具函数类. 用静态函数的方法定义一些常用的函数，如字符串转数字、字符串转日期等。 *
 * 
 */
public class DateTimeUtil {

	private static ThreadLocal<HashMap<String, SimpleDateFormat>> dateFormatMap = ThreadLocal.withInitial(HashMap::new);

	/**
	 * 日期转字符串，输出"yyyy-MM-dd HH:mm:ss"的表示形式
	 *
	 * @param aDate
	 *            需要转换的日期对象
	 * @return 字符串格式时间
	 */
	public static String date2dateTimeStr(Date aDate) {
		if (aDate == null) {
			return null;
		}
		SimpleDateFormat theFormat = getDateFormat("yyyy-MM-dd HH:mm:ss");
		return theFormat.format(aDate);
	}

	/**
	 * 日期转字符串
	 *
	 * @param aDate
	 *            需要转换的日期对象
	 * @param pattern
	 *            yyyy-MM-dd HH:mm:ss yyyy/MM/dd HH:mm:ss yyyy-MM-dd-HH-mm-ss
	 * @return 字符串格式时间
	 */
	public static String date2dateTimeStr(Date aDate, String pattern) {
		if (aDate == null) {
			return null;
		}
		SimpleDateFormat theFormat = getDateFormat(pattern);
		return theFormat.format(aDate);
	}

	public static Date Str2dateTime(String aDate, String pattern) {

		SimpleDateFormat theFormat = getDateFormat(pattern);
		Date date = null;
		try {
			date = theFormat.parse(aDate);
		} catch (ParseException e) {
			return null;
		}
		return date;
	}

	/**
	 * 日期转字符串，输出"yyyy-MM-dd"的表示形式
	 *
	 * @param dateValue
	 *            需要转换的日期对象
	 * @return
	 */
	public static String date2dateStr(Date dateValue) {
		if (dateValue == null) {
			return null;
		}
		SimpleDateFormat theFormat = getDateFormat("yyyy-MM-dd");
		return theFormat.format(dateValue);
	}

	/**
	 * 日期转字符串，输出"yyyy-MM-dd 00:00:00"的表示形式
	 *
	 * @param aDate
	 *            需要转换的日期对象
	 * @return String 字符串格式时间
	 */
	public static String date2dateStr_ex(Date aDate) {
		if (aDate == null) {
			return null;
		}
		SimpleDateFormat theFormat = getDateFormat("yyyy-MM-dd 00:00:00");
		return theFormat.format(aDate);
	}

	/**
	 * 字符串xxxx-xx-xx转换成Date
	 *
	 * @param str
	 *            xxxx-xx-xx格式的时间字符串
	 * @return 日期对象
	 */
	public static Date dateStr2date(String str) {
		if (str == null) {
			return null;
		}
		SimpleDateFormat theFormat = getDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = theFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 字符串xxxx-xx-xx xx:xx:xx转换成Date
	 *
	 * @param str
	 *            xxxx-xx-xx xx:xx:xx格式的时间字符串
	 * @return 日期对象
	 */
	public static Date dateTimeStr2date(String str) {
		if (str == null) {
			return null;
		}
		SimpleDateFormat theFormat = getDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = theFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 截取日期(把时分秒去掉)
	 *
	 * @param date
	 *            日期对象
	 * @return 不包含时分秒的日期对象
	 */
	public static Date truncateDateTime(Date date) {
		if (date == null) {
			return null;
		}
		String dateStr = date2dateStr(date);
		return dateStr2date(dateStr);
	}

	/**
	 * 获取某月第一天起始时刻
	 *
	 * @param date
	 *            一月中的某一天
	 * @return 该月的第一天的零点日期
	 */
	public static Date getFirstTimeOfMonth(Date date) {
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(date);
		cDay1.set(Calendar.DAY_OF_MONTH, 1);
		Date lastDate = cDay1.getTime();
		String lastTimeStr = date2dateStr(lastDate) + " 00:00:00";
		return dateTimeStr2date(lastTimeStr);
	}

	/**
	 * 获取某月最后一天最后时刻
	 *
	 * @param date
	 *            一月中的某一天
	 * @return 该月的最后一天的23:59:59
	 */
	public static Date getLastTimeOfMonth(Date date) {
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(date);
		final int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
		cDay1.set(Calendar.DAY_OF_MONTH, lastDay);
		Date lastDate = cDay1.getTime();
		String lastTimeStr = date2dateStr(lastDate) + " 23:59:59";
		return dateTimeStr2date(lastTimeStr);
	}

	/**
	 * 获取当天开始时间
	 *
	 * @param date
	 *            当天时间
	 * @return 当天开始时间
	 */
	public static Date getTheDayFirstTime(Date date) {
		try {
			if (date == null) {
				return null;
			}
			String lastTimeStr = date2dateStr(date) + " 00:00:00";
			return dateTimeStr2date(lastTimeStr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当天的最后时刻
	 *
	 * @param date
	 *            当天时间
	 * @return 最后时刻时间对象(23:59:59)
	 */
	public static Date getTheDayLastTime(Date date) {
		try {
			if (date == null) {
				return null;
			}
			String lastTimeStr = date2dateStr(date) + " 23:59:59";
			return dateTimeStr2date(lastTimeStr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static SimpleDateFormat getDateFormat(String pattern) {
		HashMap<String, SimpleDateFormat> map = dateFormatMap.get();
		SimpleDateFormat theFormat = map.get(pattern);
		if (theFormat == null) {
			theFormat = new SimpleDateFormat(pattern);
			map.put(pattern, theFormat);
		}
		return theFormat;
	}


}
