package com.neturbo.set.utils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.text.SimpleDateFormat;

/**
 * 进行日期时间处理的类，模仿java.util.Calendar类，但是对月份的处理是<br>
 * 基于1的，即1代表一月，不像java.util.Calendar是基于0的。<br>
 */
public class NTBCalendar implements Cloneable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7106897257798770082L;

	/**
	 * 内建的日历对象，代表每一个实例的时间。
	 */

	private Calendar cal = null;

	/**
	 * 构建器，创建代表当前时间的实例。
	 */
	public NTBCalendar() {
		super();
		cal = Calendar.getInstance();
	}

	/**
	 * 构建器，创建一个代表传递参数所指定时间的实例。
	 * 
	 * @param year
	 *            int 年。
	 * @param month
	 *            int 月 基于1，即1代表一月。
	 * @param date
	 *            int 日，基于1，即1代表一号。
	 */
	public NTBCalendar(int year, int month, int date) {
		this(year, month, date, 0, 0, 0);
	}

	/**
	 * 构建器，创建一个代表传递参数所指定时间的实例。
	 * 
	 * @param year
	 *            int 年。
	 * @param month
	 *            int 月 基于1，即1代表一月。
	 * @param date
	 *            int 日，基于1，即1代表一号。
	 * @param hour
	 *            int 时。
	 * @param minute
	 *            int 分。
	 */
	public NTBCalendar(int year, int month, int date, int hour, int minute) {
		this(year, month, date, hour, minute, 0);
	}

	/**
	 * 构建器，创建一个代表传递参数所指定时间的实例。
	 * 
	 * @param year
	 *            int 年。
	 * @param month
	 *            int 月 基于1，即1代表一月。
	 * @param date
	 *            int 日，基于1，即1代表一号。
	 * @param hour
	 *            int 时。
	 * @param minute
	 *            int 分。
	 * @param second
	 *            int 秒。
	 */
	public NTBCalendar(int year, int month, int date, int hour, int minute,
			int second) {
		this();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
	}

	/**
	 * 构建器，创建一个代表传递参数所指定时间的实例。
	 * 
	 * @param oneDay
	 *            Calendar 创建实例所代表的时间
	 */
	public NTBCalendar(Calendar oneDay) {
		super();
		cal = oneDay;
	}

	/**
	 * 构建器，创建一个代表传递参数所指定时间的实例。
	 * 
	 * @param oneday
	 *            java.util.Date
	 */
	public NTBCalendar(Date oneDay) {
		this();
		cal.setTime(oneDay);
	}

	/**
	 * 将指定时间域增加指定个单位，影响高级的时间域。
	 * 
	 * @param field
	 *            int 指定的时间域，与java.util.Calendar相同。
	 * @param amount
	 *            int 增加的单位数。
	 */
	public void add(int field, int amount) {
		cal.add(field, amount);
	}

	/**
	 * 判断当前实例是否在参数给定的时间后面。
	 * 
	 * @return boolean
	 * @param when
	 *            java.lang.Object 指定的时间，必须是CommonCalendar的实例，否则一律返回false。
	 */
	public boolean after(Object when) {
		if (when instanceof NTBCalendar) {
			return cal.after(((NTBCalendar) when).cal);
		} else {
			return false;
		}
	}

	/**
	 * 判断当前实例是否在给定的时间前面。
	 * 
	 * @return boolean
	 * @param when
	 *            java.lang.Object 指定的时间，必须是CommonCalendar的实例，否则一律返回false。
	 */
	public boolean before(Object when) {
		if (when instanceof NTBCalendar) {
			return cal.before(((NTBCalendar) when).cal);
		} else {
			return false;
		}
	}

	/**
	 * 覆盖Object的clone()方法，返回一个当前实例的克隆。
	 */
	protected Object clone() {
		return new NTBCalendar((Calendar) cal.clone());
	}

	/**
	 * 判断当前实例是否和给定实例代表相同的时间。
	 * 
	 * @return boolean
	 * @param obj
	 *            java.lang.Object 给定的时间，必须是CommonCalendar的实例，否则一律返回false。
	 */
	public boolean equals(Object obj) {
		if (obj instanceof NTBCalendar) {
			return cal.equals(((NTBCalendar) obj).cal);
		} else {
			return false;
		}
	}

	/**
	 * 取当前实例某个时间域的值。
	 * 
	 * @return int 当前实例指定时间域的值。
	 * @param field
	 *            int
	 *            时间域，与java.util.Calendar相同,注意月值是基于1，即1代表一月，这与java.util.Calendar不同。
	 */
	public int get(int field) {
		if (field == Calendar.MONTH) {
			return (cal.get(field) + 1);
		} else {
			return cal.get(field);
		}
	}

	/**
	 * 返回当前实例所代表时间的日期部分的字符串，如当前实例代表的时间为2000年6月30日下午3时30分45秒，<br>
	 * 则调用该方法返回的字符串是 2000-06-30。<br>
	 * 
	 * @return java.lang.String
	 */
	public String getDateString() {
		return getTimeFormatted("yyyy-MM-dd");
	}

	/**
	 * 返回当前实例所代表时间。
	 * 
	 * @return java.util.Date
	 */
	public Date getTime() {
		return cal.getTime();
	}

	/**
	 * 返回一个格式化的日期字符串。
	 * 
	 * @return java.lang.String 当前实例代表的日期的字符串，如果传给的参数为null或不是合法的模式则返回null。
	 * @param pattern
	 *            java.lang.String 格式化模式，y代表年，M代表月,d代表日，H和h代表时，m代表分，s代表秒。<br>
	 *            如yyyy-MM-dd HH:mm:ss将返回形如2000-06-30 15:30:45的字符串。<br>
	 */
	public String getTimeFormatted(String pattern) {
		String rtStr = null;
		try {
			SimpleDateFormat df = (SimpleDateFormat) SimpleDateFormat
					.getDateTimeInstance();
			df.applyPattern(pattern);
			rtStr = df.format(cal.getTime());
		} catch (Exception e) {
		}
		return rtStr;
	}

	/**
	 * 返回当前实例所代表时间的时间部分的字符串，如当前实例代表的时间为2000年6月30日下午3时30分45秒，<br>
	 * 则调用该方法返回的字符串是 15:30:45。<br>
	 * 
	 * @return java.lang.String
	 */
	public String getTimeString() {
		return getTimeFormatted("HH:mm:ss");
	}

	/**
	 * 覆盖Object的hashCode()方法，返回代表当前实例的哈希值。
	 */
	public int hashCode() {
		return cal.hashCode();
	}

	public static void main(String[] args) {
		NTBCalendar calInstance = new NTBCalendar();
		calInstance.setDayTime("12:15:26");
		System.out.println(calInstance);
	}

	/**
	 * 将一个代表日期时间的字符串解析成一个CommonCalendar对象，目前只支持六种格式：<br>
	 * 2000-06-30 15:30:45，2000-06-30，20060630 15:30:45, 20060630, 2000/06/30
	 * 15:30:45和2000/06/30。<br>
	 * 
	 * @return com.csii.workflow.util.CommonCalendar
	 *         解析出来的CommonCalendar对象，如果出错则返回null。
	 * @param date
	 *            java.lang.String
	 */
	public static NTBCalendar parse(String date) {
		if (date.indexOf('-') > 0) {
			if (date.indexOf(':') > 0) {
				return parse(date, "yyyy-MM-dd HH:mm:ss");
			} else {
				return parse(date, "yyyy-MM-dd");
			}
		} else if (date.indexOf('/') > 0) {
			if (date.indexOf(':') > 0) {
				return parse(date, "yyyy/MM/dd HH:mm:ss");
			} else {
				return parse(date, "yyyy/MM/dd");
			}
		} else {
			if (date.indexOf(':') > 0) {
				return parse(date, "yyyyMMdd HH:mm:ss");
			} else {
				return parse(date, "yyyyMMdd");
			}
		}
	}

	/**
	 * 将一个代表日期时间的字符串按给定的模式解析成一个CommonCalendar对象。
	 * 
	 * @return com.csii.workflow.util.CommonCalendar
	 *         解析出来的CommonCalendar对象，如果出错则返回null。
	 * @param date
	 *            java.lang.String
	 * @param pattern
	 *            java.lang.String
	 */
	public static NTBCalendar parse(String date, String pattern) {
		NTBCalendar rtCal = null;
		try {
			SimpleDateFormat df = (SimpleDateFormat) SimpleDateFormat
					.getDateTimeInstance();
			df.applyPattern(pattern);
			Calendar cal = Calendar.getInstance();
			cal.setTime(df.parse(date));
			rtCal = new NTBCalendar(cal);
		} catch (Exception e) {
		}
		return rtCal;
	}

	/**
	 * 将指定时间域滚动指定个单位，不影响高级的时间域。
	 * 
	 * @param field
	 *            int 指定的时间域，与java.util.Calendar相同。
	 * @param amount
	 *            int 滚动的单位数，正数向上滚动，负数向下滚动。
	 */
	public void roll(int field, int amount) {
		cal.roll(field, amount);
	}

	/**
	 * 将指定的时间域滚动一个单位，不影响高级的时间域。
	 * 
	 * @param field
	 *            int 要滚动的时间域，与java.util.Calendar相同。
	 * @param up
	 *            boolean 滚动方向，true向上滚动，false向下滚动。
	 */
	public void roll(int field, boolean up) {
		cal.roll(field, up);
	}

	/**
	 * 设置某个时间域的值。
	 * 
	 * @param field
	 *            int 时间域，与java.util.Calendar相同。
	 * @param value
	 *            int 要设置的值,注意月值是基于1，即1代表一月，这与java.util.Calendar不同。
	 */
	public void set(int field, int value) {
		if (field == Calendar.MONTH) {
			cal.set(field, value - 1);
		} else {
			cal.set(field, value);
		}
	}

	/**
	 * 设置新的年月日。
	 * 
	 * @param year
	 *            int 年。
	 * @param month
	 *            int 月，基于1，即1代表一月。
	 * @param date
	 *            int 日。
	 */
	public void set(int year, int month, int date) {
		cal.set(year, month - 1, date);
	}

	/**
	 * 设置新的年月日时分秒。
	 * 
	 * @param year
	 *            int 年。
	 * @param month
	 *            int 月，基于1，即1代表一月。
	 * @param date
	 *            int 日。
	 * @param hour
	 *            int 时。
	 * @param minute
	 *            int 分。
	 * @param second
	 *            int 秒。
	 */
	public void set(int year, int month, int date, int hour, int minute,
			int second) {
		cal.set(year, month - 1, date, hour, minute, second);
	}

	/**
	 * 将当前实例所代表的时间设成参数所代表的时间。
	 * 
	 * @param oneDay
	 *            java.util.Date 要设置的时间。
	 */
	public void setTime(Date oneDay) {
		cal.setTime(oneDay);
	}

	/**
	 * 设置时间，格式HH:mm:ss
	 */
	public void setDayTime(String aTimeString) {
		StringTokenizer st = new StringTokenizer(aTimeString, ":");
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(st.nextToken()));
		cal.set(Calendar.MINUTE, Integer.parseInt(st.nextToken()));
		cal.set(Calendar.SECOND, Integer.parseInt(st.nextToken()));

	}

	/**
	 * 设置日期，格式YYYY-MM-DD
	 */
	public void setDate(String aDateString) {
		StringTokenizer st = new StringTokenizer(aDateString, "-");
		cal.set(Calendar.YEAR, Integer.parseInt(st.nextToken()));
		cal.set(Calendar.MONTH, Integer.parseInt(st.nextToken()) - 1);
		cal.set(Calendar.DATE, Integer.parseInt(st.nextToken()));
	}

	/**
	 * 将当前实例所代表的时间设成参数所代表的时间。
	 * 
	 * @param oneDay
	 *            java.util.Date 要设置的时间。
	 */
	public void setTimeInMillis(long millis) {
		cal.setTime(new Date(millis));
	}

	/**
	 * 覆盖Object的toString()方法，返回代表当前实例所代表时间的字符串，格式yyyy-MM-dd HH:mm:ss。
	 */
	public String toString() {
		SimpleDateFormat df = (SimpleDateFormat) SimpleDateFormat
				.getDateTimeInstance();
		df.applyPattern("yyyy-MM-dd HH:mm:ss");
		return df.format(cal.getTime());
	}
}
