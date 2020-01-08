package com.neturbo.set.utils;

import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.Timestamp;

import app.cib.core.CibTransClient;

import com.neturbo.set.core.*;
import com.neturbo.set.exception.NTBException;

/**
 * �˴���������������
 * �������ڣ�(2003-8-25 2:38:32)
 * @author��Administrator
 */
public class DateTime {
    /**
     * DateTime ������ע�⡣
     */
    private static String defalutPattern;
    static {
        defalutPattern = Config.getProperty("DefaultDatePattern");
        if (defalutPattern == null) {
            defalutPattern = "yyyy-MM-dd";
        }
    }

    public DateTime() {
        super();
    }

    /**
     * getCurrentDataTime �� yyyy-MM-dd HH:mm:ss ��ʽ��õ�ǰʱ�䡣
     */
    public static final String getCurrentDataTime() {
        java.util.Date date = new java.util.Date();
        try {
            SimpleDateFormat simpledateformat = new SimpleDateFormat();
            simpledateformat.applyPattern("yyyy-MM-dd HH:mm:ss");
            return simpledateformat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * getDate �� yyyymmdd �ĸ�ʽ��õ�ǰʱ�䡣
     */
    public static String getDate(int day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);

        int iYear = cal.get(Calendar.YEAR);
        int iMonth = cal.get(Calendar.MONTH) + 1;
        int iDay = cal.get(Calendar.DAY_OF_MONTH);

        return ""
                + iYear
                + (iMonth < 10 ? "0" + iMonth : "" + iMonth)
                + (iDay < 10 ? "0" + iDay : "" + iDay);

    }

    /**
     * getDate �� yyyy-mm-dd �ĸ�ʽ��õ�ǰʱ�䣬���У�Ϊ�ɶ���ķָ���
     */
    public static String getDate(int day, String delimiter) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);

        int iYear = cal.get(Calendar.YEAR);
        int iMonth = cal.get(Calendar.MONTH) + 1;
        int iDay = cal.get(Calendar.DAY_OF_MONTH);

        return ""
                + iYear
                + delimiter
                + (iMonth < 10 ? "0" + iMonth : "" + iMonth)
                + delimiter
                + (iDay < 10 ? "0" + iDay : "" + iDay);

    }

    public static final String Millis2DateTime(long millis) {
        java.util.Date date = new java.util.Date(millis);
        try {
            SimpleDateFormat simpledateformat = new SimpleDateFormat();
            simpledateformat.applyPattern("yyyy-MM-dd HH:mm:ss");
            return simpledateformat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * ��������Date�����֮��ķ�����
     *
     * @param Date date1   ��Ϊ�������Date����
     * @param Date date2   ��Ϊ�����Date����
     * @return int         ����Date�����֮��ķ�����
     */
    public static int getMinutesTween(Date date1, Date date2) {
        long mill1 = date1.getTime();
        long mill2 = date2.getTime();
        return (int) ((mill1 - mill2) / (1000 * 60));
    }

    /**
     * ��������Date�����֮���Сʱ��
     *
     * @param Date date1   ��Ϊ�������Date����
     * @param Date date2   ��Ϊ�����Date����
     * @return int         ����Date�����֮���Сʱ��
     */
    public static int getHoursTween(Date date1, Date date2) {
        long mill1 = date1.getTime();
        long mill2 = date2.getTime();
        return (int) ((mill1 - mill2) / (1000 * 60 * 60));
    }

    /**
     * ��������string(��ʽ�ǣ�yyyy-MM-dd HH:mm:ss)��֮���Сʱ��
     *
     * @param String date1   ��Ϊ�������String����
     * @param String date2   ��Ϊ�����String����
     * @return int         ����Date�����֮���Сʱ��
     */
    public static int getHoursTween(String date1, String date2) {
        Calendar cal1 = getCalendarFromStr(formatDate(date1,
                "yyyy-MM-dd HH:mm:ss"));
        Calendar cal2 = getCalendarFromStr(formatDate(date2,
                "yyyy-MM-dd HH:mm:ss"));
        Date d1 = cal1.getTime();
        Date d2 = cal2.getTime();
        return getHoursTween(d1, d2);
    }

    /**
     * ��������Date�����֮�������
     *
     * @param Date date1   ��Ϊ�������Date����
     * @param Date date2   ��Ϊ�����Date����
     * @return int         ����Date�����֮�������
     */
    public static int getDaysTween(Date date1, Date date2) {
        //	return toJulian(date1) - toJulian(date2);
        long mill1 = date1.getTime();
        long mill2 = date2.getTime();
        return (int) ((mill1 - mill2) / (1000 * 60 * 60 * 24));
    }

    /**
     * ��������String(��ʽ�ǣ�yyyy-MM-dd HH:mm:ss)�����֮�������
     *
     * @param String date1   ��Ϊ�������String����
     * @param String date2   ��Ϊ�����String����
     * @return int         ����String�����֮�������
     */
    public static int getDaysTween(String date1, String date2) {
        Calendar cal1 = getCalendarFromStr(formatDate(date1,
                "yyyy-MM-dd HH:mm:ss"));
        Calendar cal2 = getCalendarFromStr(formatDate(date2,
                "yyyy-MM-dd HH:mm:ss"));
        Date d1 = cal1.getTime();
        Date d2 = cal2.getTime();
        return getDaysTween(d1, d2);
    }

    /**
     * ��Date�������ת��Ϊ�ض��ĸ�ʽ, ���ʽΪnull,
     * ��ʹ��ȱʡ��ʽyyyy-MM-dd.
     *
     * @param Date day ����
     * @param String toPattern Ҫת���ɵ����ڸ�ʽ
     * @return String  ���������ַ�
     */
    public static String formatDate(Date day, String toPattern) {
        String date = null;
        if (day != null) {
            try {
                SimpleDateFormat formatter = null;
                if (toPattern != null) {
                    formatter = new SimpleDateFormat(toPattern);
                } else {
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                }
                date = formatter.format(day);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return date;
        } else {
            return null;
        }
    }

    /**
     * ��ԭ�е����ڸ�ʽ���ַ�ת��Ϊ�ض��ĸ�ʽ, ��(ԭ�к�ת��)��ʽΪnull,
     * ��ʹ��ȱʡ��ʽyyyy-MM-dd.
     *
     * @param String value ���ڸ�ʽ���ַ�
     * @param String fromPattern ԭ�е����ڸ�ʽ
     * @param String toPattern ת���ɵ����ڸ�ʽ
     * @return String  ���������ַ�
     */
    public static String formatDate(
            String value,
            String fromPattern,
            String toPattern) {
        String date = null;
        if (toPattern == null) {
            toPattern = defalutPattern;
        }
        if (toPattern == null) {
            toPattern = "yyyy-MM-dd";
        }
        if (value != null) {
            try {
                SimpleDateFormat formatter = null;
                if (fromPattern != null) {
                    formatter = new SimpleDateFormat(fromPattern);
                } else {
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                }
                Date day = formatter.parse(value);
                formatter.applyPattern(toPattern);
                date = formatter.format(day);
            } catch (Exception e) {
                e.printStackTrace();
                return value;
            }
            return date;
        } else {
            return null;
        }
    }

    /**
     * ��ԭ�е����ڸ�ʽ���ַ�ת��Ϊ�ض��ĸ�ʽ, ԭ�и�ʽΪyyyy-MM-dd.
     *
     * @param String value ���ڸ�ʽ���ַ�
     * @param String toPattern ת���ɵ����ڸ�ʽ
     * @return String  ���������ַ�
     */
    public static String formatDate(String value, String toPattern) {
        return formatDate(value, null, toPattern);
    }

    /**
     * ����������ַ����Calendar����.
     *
     * @param  String str  ������ַ�,��ʽ=yyyy-MM-dd HH:mm:ss.
     * @return Calendar    ���ش�������ַ��Calendar����.
     */
    public static Calendar getCalendarFromStr(String str) {
        Calendar cal = Calendar.getInstance();
        try {
            DateFormat myformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = myformat.parse(str);
            cal.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal;
    }

    /**
     * ����������ַ����Calendar����.
     *
     * @param  String str  ������ַ�
     * @param  String str  ������ַ����ڸ�ʽ, ȱʡΪyyyy-MM-dd
     * @return Calendar    ���ش�������ַ��Calendar����
     */
    public static Calendar getCalendarFromStr(String str, String pattern) {
        Calendar cal = Calendar.getInstance();
        if (pattern == null) {
            pattern = "yyyy-MM-dd";
        }
        try {
            DateFormat myformat = new SimpleDateFormat(pattern);
            Date date = myformat.parse(str);
            cal.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cal;
    }

    /**
     * ����������ַ����Date����.
     *
     * @param  String str  ������ַ�
     * @param  String str  ������ַ����ڸ�ʽ, ȱʡΪyyyy-MM-dd
     * @return Date    ���ش�������ַ��Date����
     */
    public static Date getDateFromStr(String str, String pattern) {
        if (pattern == null) {
            pattern = defalutPattern;
        }
        if (pattern == null) {
            pattern = "yyyy-MM-dd";
        }
        try {
            DateFormat myformat = new SimpleDateFormat(pattern);
            Date date = myformat.parse(str);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ����������ַ����Date����.
     *
     * @param  String str  ������ַ�
     * @return Date    ���ش�������ַ��Date����
     */
    public static Date getDateFromStr(String str) {
        try {
            DateFormat myformat = new SimpleDateFormat(defalutPattern);
            Date date = myformat.parse(str);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ����������ַ����Date����.
     *
     * @param  String str  ������ַ�
     * @return Date    ���ش�������ַ��Date����
     */
    public static Date getDateFromStr(String str, boolean dateStart) {
        try {
        	String timeStr = " 23:59:59";
        	if(dateStart){
        		timeStr = " 00:00:00";
        	}
        	str += timeStr;
        	String datePattern = defalutPattern + " HH:mm:ss";
            DateFormat myformat = new SimpleDateFormat(datePattern);
            Date date = myformat.parse(str);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
       
    }

    /**
     * ���ص�ǰʱ��,��ʽHH:mm:ss
     *
     * @return String ��ǰʱ��
     */
    public static String getCurrentTime() {
        String time = null;
        try {
            DateFormat myformat = new SimpleDateFormat("HH:mm:ss");
            time = myformat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * ���ص�ǰ��������,��ʽyyyy-MM-dd
     *
     * @return String ��������
     */
    public static String getCurrentDate() {
        String date = null;
        try {
            String pattern = defalutPattern;
            if (pattern == null) {
                pattern = "yyyy-MM-dd";
            }
            DateFormat myformat = new SimpleDateFormat(pattern);
            date = myformat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * ���ص�ǰ��������,��ʽyyyy-MM-dd
     *
     * @return String ��������
     */
    public static Timestamp getTimestampByStr(String date, boolean DAY_START) {
        String formatDate = formatDate(date, "yyyy-MM-dd");
        int year = Integer.parseInt(formatDate.substring(0, 4));
        int month = Integer.parseInt(formatDate.substring(5, 7));
        int day = Integer.parseInt(formatDate.substring(8));

        if (DAY_START) {
            return new Timestamp(year - 1900, month - 1, day, 0, 0, 0, 0);
        } else {
            return new Timestamp(year - 1900, month - 1, day, 23, 59, 59, 0);
        }
    }

    /**
     * @author hjs
     * ͨ��yyyyMMdd�ĸ�ʽ�Ƚ���������
     * @param d1������1
     * @param d2������2
     * @return 0����ȣ�����0��d1>d2��С��0��d1<d2
     */
    public static int compareDate(Date d1, Date d2) {
        return (new Integer(formatDate(d1, "yyyyMMdd")))
        	.compareTo(new Integer(formatDate(d2, "yyyyMMdd")));
    }
    
    public static int compareDate(Date d1, Date d2, String pattern) {
    	if(pattern==null || pattern=="") pattern="yyyyMMddHHmmss";
        return (new Long(formatDate(d1, pattern)))
        	.compareTo(new Long(formatDate(d2, pattern)));
    }
    //add by linrui 20190611
    public static Date strToDate(String DateSrt,String patten) {  //string to date
		Date date =null;
		SimpleDateFormat sdf=new SimpleDateFormat(patten);
		try{
			date=sdf.parse(DateSrt);
			}
		catch (Exception e){
			e.printStackTrace();
            return null;	
				}
		return  date; 
		
	}
    //get host time 20190717
    public static Date getHostTime() throws NTBException {  //string to date
    	//mod by linrui 20190716 get host time
		CibTransClient testClient = new CibTransClient("CIB", "ZJ05");
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
        toHost.put("EFFECTIVE_DATE", DateTime.getCurrentDate());
        fromHost = testClient.doTransaction(toHost);
//		Date date = DateTime.strToDate(fromHost1.get("EFFECTIVE_DATE").toString(),"yyyyMMdddd");
    	return DateTime.strToDate(fromHost.get("EFFECTIVE_DATE").toString(),"yyyyMMdddd");
    }

}
