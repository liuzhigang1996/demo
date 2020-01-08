package com.neturbo.set.utils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.text.SimpleDateFormat;

/**
 * ��������ʱ�䴦����࣬ģ��java.util.Calendar�࣬���Ƕ��·ݵĴ�����<br>
 * ����1�ģ���1����һ�£�����java.util.Calendar�ǻ���0�ġ�<br>
 */
public class NTBCalendar implements Cloneable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7106897257798770082L;

	/**
	 * �ڽ����������󣬴���ÿһ��ʵ����ʱ�䡣
	 */

	private Calendar cal = null;

	/**
	 * ����������������ǰʱ���ʵ����
	 */
	public NTBCalendar() {
		super();
		cal = Calendar.getInstance();
	}

	/**
	 * ������������һ�������ݲ�����ָ��ʱ���ʵ����
	 * 
	 * @param year
	 *            int �ꡣ
	 * @param month
	 *            int �� ����1����1����һ�¡�
	 * @param date
	 *            int �գ�����1����1����һ�š�
	 */
	public NTBCalendar(int year, int month, int date) {
		this(year, month, date, 0, 0, 0);
	}

	/**
	 * ������������һ�������ݲ�����ָ��ʱ���ʵ����
	 * 
	 * @param year
	 *            int �ꡣ
	 * @param month
	 *            int �� ����1����1����һ�¡�
	 * @param date
	 *            int �գ�����1����1����һ�š�
	 * @param hour
	 *            int ʱ��
	 * @param minute
	 *            int �֡�
	 */
	public NTBCalendar(int year, int month, int date, int hour, int minute) {
		this(year, month, date, hour, minute, 0);
	}

	/**
	 * ������������һ�������ݲ�����ָ��ʱ���ʵ����
	 * 
	 * @param year
	 *            int �ꡣ
	 * @param month
	 *            int �� ����1����1����һ�¡�
	 * @param date
	 *            int �գ�����1����1����һ�š�
	 * @param hour
	 *            int ʱ��
	 * @param minute
	 *            int �֡�
	 * @param second
	 *            int �롣
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
	 * ������������һ�������ݲ�����ָ��ʱ���ʵ����
	 * 
	 * @param oneDay
	 *            Calendar ����ʵ���������ʱ��
	 */
	public NTBCalendar(Calendar oneDay) {
		super();
		cal = oneDay;
	}

	/**
	 * ������������һ�������ݲ�����ָ��ʱ���ʵ����
	 * 
	 * @param oneday
	 *            java.util.Date
	 */
	public NTBCalendar(Date oneDay) {
		this();
		cal.setTime(oneDay);
	}

	/**
	 * ��ָ��ʱ��������ָ������λ��Ӱ��߼���ʱ����
	 * 
	 * @param field
	 *            int ָ����ʱ������java.util.Calendar��ͬ��
	 * @param amount
	 *            int ���ӵĵ�λ����
	 */
	public void add(int field, int amount) {
		cal.add(field, amount);
	}

	/**
	 * �жϵ�ǰʵ���Ƿ��ڲ���������ʱ����档
	 * 
	 * @return boolean
	 * @param when
	 *            java.lang.Object ָ����ʱ�䣬������CommonCalendar��ʵ��������һ�ɷ���false��
	 */
	public boolean after(Object when) {
		if (when instanceof NTBCalendar) {
			return cal.after(((NTBCalendar) when).cal);
		} else {
			return false;
		}
	}

	/**
	 * �жϵ�ǰʵ���Ƿ��ڸ�����ʱ��ǰ�档
	 * 
	 * @return boolean
	 * @param when
	 *            java.lang.Object ָ����ʱ�䣬������CommonCalendar��ʵ��������һ�ɷ���false��
	 */
	public boolean before(Object when) {
		if (when instanceof NTBCalendar) {
			return cal.before(((NTBCalendar) when).cal);
		} else {
			return false;
		}
	}

	/**
	 * ����Object��clone()����������һ����ǰʵ���Ŀ�¡��
	 */
	protected Object clone() {
		return new NTBCalendar((Calendar) cal.clone());
	}

	/**
	 * �жϵ�ǰʵ���Ƿ�͸���ʵ��������ͬ��ʱ�䡣
	 * 
	 * @return boolean
	 * @param obj
	 *            java.lang.Object ������ʱ�䣬������CommonCalendar��ʵ��������һ�ɷ���false��
	 */
	public boolean equals(Object obj) {
		if (obj instanceof NTBCalendar) {
			return cal.equals(((NTBCalendar) obj).cal);
		} else {
			return false;
		}
	}

	/**
	 * ȡ��ǰʵ��ĳ��ʱ�����ֵ��
	 * 
	 * @return int ��ǰʵ��ָ��ʱ�����ֵ��
	 * @param field
	 *            int
	 *            ʱ������java.util.Calendar��ͬ,ע����ֵ�ǻ���1����1����һ�£�����java.util.Calendar��ͬ��
	 */
	public int get(int field) {
		if (field == Calendar.MONTH) {
			return (cal.get(field) + 1);
		} else {
			return cal.get(field);
		}
	}

	/**
	 * ���ص�ǰʵ��������ʱ������ڲ��ֵ��ַ������統ǰʵ�������ʱ��Ϊ2000��6��30������3ʱ30��45�룬<br>
	 * ����ø÷������ص��ַ����� 2000-06-30��<br>
	 * 
	 * @return java.lang.String
	 */
	public String getDateString() {
		return getTimeFormatted("yyyy-MM-dd");
	}

	/**
	 * ���ص�ǰʵ��������ʱ�䡣
	 * 
	 * @return java.util.Date
	 */
	public Date getTime() {
		return cal.getTime();
	}

	/**
	 * ����һ����ʽ���������ַ�����
	 * 
	 * @return java.lang.String ��ǰʵ����������ڵ��ַ�������������Ĳ���Ϊnull���ǺϷ���ģʽ�򷵻�null��
	 * @param pattern
	 *            java.lang.String ��ʽ��ģʽ��y�����꣬M������,d�����գ�H��h����ʱ��m����֣�s�����롣<br>
	 *            ��yyyy-MM-dd HH:mm:ss����������2000-06-30 15:30:45���ַ�����<br>
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
	 * ���ص�ǰʵ��������ʱ���ʱ�䲿�ֵ��ַ������統ǰʵ�������ʱ��Ϊ2000��6��30������3ʱ30��45�룬<br>
	 * ����ø÷������ص��ַ����� 15:30:45��<br>
	 * 
	 * @return java.lang.String
	 */
	public String getTimeString() {
		return getTimeFormatted("HH:mm:ss");
	}

	/**
	 * ����Object��hashCode()���������ش���ǰʵ���Ĺ�ϣֵ��
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
	 * ��һ����������ʱ����ַ���������һ��CommonCalendar����Ŀǰֻ֧�����ָ�ʽ��<br>
	 * 2000-06-30 15:30:45��2000-06-30��20060630 15:30:45, 20060630, 2000/06/30
	 * 15:30:45��2000/06/30��<br>
	 * 
	 * @return com.csii.workflow.util.CommonCalendar
	 *         ����������CommonCalendar������������򷵻�null��
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
	 * ��һ����������ʱ����ַ�����������ģʽ������һ��CommonCalendar����
	 * 
	 * @return com.csii.workflow.util.CommonCalendar
	 *         ����������CommonCalendar������������򷵻�null��
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
	 * ��ָ��ʱ�������ָ������λ����Ӱ��߼���ʱ����
	 * 
	 * @param field
	 *            int ָ����ʱ������java.util.Calendar��ͬ��
	 * @param amount
	 *            int �����ĵ�λ�����������Ϲ������������¹�����
	 */
	public void roll(int field, int amount) {
		cal.roll(field, amount);
	}

	/**
	 * ��ָ����ʱ�������һ����λ����Ӱ��߼���ʱ����
	 * 
	 * @param field
	 *            int Ҫ������ʱ������java.util.Calendar��ͬ��
	 * @param up
	 *            boolean ��������true���Ϲ�����false���¹�����
	 */
	public void roll(int field, boolean up) {
		cal.roll(field, up);
	}

	/**
	 * ����ĳ��ʱ�����ֵ��
	 * 
	 * @param field
	 *            int ʱ������java.util.Calendar��ͬ��
	 * @param value
	 *            int Ҫ���õ�ֵ,ע����ֵ�ǻ���1����1����һ�£�����java.util.Calendar��ͬ��
	 */
	public void set(int field, int value) {
		if (field == Calendar.MONTH) {
			cal.set(field, value - 1);
		} else {
			cal.set(field, value);
		}
	}

	/**
	 * �����µ������ա�
	 * 
	 * @param year
	 *            int �ꡣ
	 * @param month
	 *            int �£�����1����1����һ�¡�
	 * @param date
	 *            int �ա�
	 */
	public void set(int year, int month, int date) {
		cal.set(year, month - 1, date);
	}

	/**
	 * �����µ�������ʱ���롣
	 * 
	 * @param year
	 *            int �ꡣ
	 * @param month
	 *            int �£�����1����1����һ�¡�
	 * @param date
	 *            int �ա�
	 * @param hour
	 *            int ʱ��
	 * @param minute
	 *            int �֡�
	 * @param second
	 *            int �롣
	 */
	public void set(int year, int month, int date, int hour, int minute,
			int second) {
		cal.set(year, month - 1, date, hour, minute, second);
	}

	/**
	 * ����ǰʵ���������ʱ����ɲ����������ʱ�䡣
	 * 
	 * @param oneDay
	 *            java.util.Date Ҫ���õ�ʱ�䡣
	 */
	public void setTime(Date oneDay) {
		cal.setTime(oneDay);
	}

	/**
	 * ����ʱ�䣬��ʽHH:mm:ss
	 */
	public void setDayTime(String aTimeString) {
		StringTokenizer st = new StringTokenizer(aTimeString, ":");
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(st.nextToken()));
		cal.set(Calendar.MINUTE, Integer.parseInt(st.nextToken()));
		cal.set(Calendar.SECOND, Integer.parseInt(st.nextToken()));

	}

	/**
	 * �������ڣ���ʽYYYY-MM-DD
	 */
	public void setDate(String aDateString) {
		StringTokenizer st = new StringTokenizer(aDateString, "-");
		cal.set(Calendar.YEAR, Integer.parseInt(st.nextToken()));
		cal.set(Calendar.MONTH, Integer.parseInt(st.nextToken()) - 1);
		cal.set(Calendar.DATE, Integer.parseInt(st.nextToken()));
	}

	/**
	 * ����ǰʵ���������ʱ����ɲ����������ʱ�䡣
	 * 
	 * @param oneDay
	 *            java.util.Date Ҫ���õ�ʱ�䡣
	 */
	public void setTimeInMillis(long millis) {
		cal.setTime(new Date(millis));
	}

	/**
	 * ����Object��toString()���������ش���ǰʵ��������ʱ����ַ�������ʽyyyy-MM-dd HH:mm:ss��
	 */
	public String toString() {
		SimpleDateFormat df = (SimpleDateFormat) SimpleDateFormat
				.getDateTimeInstance();
		df.applyPattern("yyyy-MM-dd HH:mm:ss");
		return df.format(cal.getTime());
	}
}
