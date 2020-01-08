package com.neturbo.set.utils;

import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.cib.util.Constants;

import com.neturbo.set.core.*;

/**
 * 姝ゅ鎻掑叆绫诲瀷鎻忚堪銆�
 * 鍒涘缓鏃ユ湡锛�2003-8-25 2:29:20)
 * @author锛欰dministrator
 */
public class Format {

    private static String defalutDatePattern;
    static {
        defalutDatePattern = Config.getProperty("DefaultDatePattern");
        if (defalutDatePattern == null) {
            defalutDatePattern = "yyyy-MM-dd";
        }
    }


    public static final String formatData(String s, String s1,String pattern , String lang) {//add by linrui for mul-language 20171116
        if (s != null) {
            s = s.trim();
        }
        if (s1.toUpperCase().equals("AMOUNT")) {
            return formatAmount(s, pattern , lang);
        }
        if (s1.toUpperCase().equals("PERCENT")) {
            return formatPercent(s, pattern, lang);// update by li_zd at 20171204
        }
        if (s1.toUpperCase().equals("AMOUNTSIGN")) {
            return formatAmountSign(s, pattern);
        }
        if (s1.toUpperCase().equals("AMOUNTCC")) {
            return formatAmountCc(s, pattern);
        }
        if (s1.toUpperCase().equals("DATE")) {
            return formatDate(s, pattern);
        }
        if (s1.toUpperCase().equals("TIME")) {
            return formatTime(s, pattern);
        }
        if (s1.toUpperCase().equals("DATETIME")) {
            return formatDateTime(s, "yyyy-MM-dd HH:mm:ss", pattern);
        }
        if (s1.toUpperCase().equals("DATETIME1")) {
            return formatDateTime(s, "yyyy-MM-dd HH:mm:ss", defalutDatePattern + " HH:mm");
        }
        if (s1.toUpperCase().equals("RATE")) {
            return formatRate(s, pattern,lang);
        }
        //add by linrui 20190523
        if (s1.toUpperCase().equals("NAME")) {
            return formatName(s);
        }
        //end
        
        //add by lzg 20190807
        if (s1.toUpperCase().equals("IDNO")) {
            if(s.length() > 4){
            	return "****" + s.substring(s.length() - 4);
            }else{
            	return s;
            }
        }
        //add by lzg end
        return s;
    }
    public static final String formatData(String s, String s1,String pattern) {
        if (s != null) {
            s = s.trim();
        }
        if (s1.toUpperCase().equals("AMOUNT")) {
            return formatAmount(s, pattern );
        }
        if (s1.toUpperCase().equals("PERCENT")) {
            return formatPercent(s, pattern);
        }
        if (s1.toUpperCase().equals("AMOUNTSIGN")) {
            return formatAmountSign(s, pattern);
        }// add by Li_zd 20160928
        if (s1.toUpperCase().equals("AMOUNTCC")) {
            return formatAmountCc(s, pattern);
        }
        if (s1.toUpperCase().equals("DATE")) {
            return formatDate(s, pattern);
        }
        if (s1.toUpperCase().equals("TIME")) {
            return formatTime(s, pattern);
        }
        if (s1.toUpperCase().equals("DATETIME")) {
            return formatDateTime(s, "yyyy-MM-dd HH:mm:ss", pattern);
        }
        if (s1.toUpperCase().equals("DATETIME1")) {
            return formatDateTime(s, "yyyy-MM-dd HH:mm:ss", defalutDatePattern + " HH:mm");
        }
        if (s1.toUpperCase().equals("RATE")) {
            return formatRate(s, pattern);
        }
        return s;
    }

    public static final String formatAmount(String s, String pattern , String lang) {
        try {
            double d = Double.valueOf(s).doubleValue();
            if (pattern == null) {
                pattern = "#,##0.00";
            }
            return fomate4Lang(formatCurrency(d, pattern),lang);
        } catch (Exception _ex) {
            return s;
        }
    }
    public static final String formatAmount(String s, String pattern ) {
        try {
            double d = Double.valueOf(s).doubleValue();
            if (pattern == null) {
                pattern = "#,##0.00";
            }
            return formatCurrency(d, pattern);
        } catch (Exception _ex) {
            return s;
        }
    }

    public static final String formatPercent(String s, String pattern, String lang) {
        try {
            double d = Double.valueOf(s).doubleValue();
//            d = d * 100;
            if (pattern == null) {
                pattern = "#,##0.00000000";
            }
            //add by linrui 20190510
            String formateValue = Utils.dropZeroAfterNotPoint(formatCurrency(d, pattern).trim());
	           int fix = formateValue.length() - formateValue.indexOf(".");
	            if(fix<=2){
	            	formateValue = Utils.addZeroForNum(formateValue,3-fix);
	            }
	        return  formateValue + "%";
//            return formatCurrency(d, pattern) + "%";
        } catch (Exception _ex) {
            return "0.00%";
        }
    }
 // add by linrui at 20190729
    public static final String formatPercent(String s, String pattern) {
        try {
            double d = Double.valueOf(s).doubleValue();
            //d = d * 100;
            if (pattern == null) {
                pattern = "#,##0.00000000";
            }
            return formatCurrency(d, pattern) + "%";
        } catch (Exception _ex) {
            return s;
        }
    }

    public static final String formatAmountSign(String s, String pattern) {
        try {
            double d = Double.valueOf(s).doubleValue();
            String sign = "+";
            if (d < 0) {
                sign = "-";
            }
            if (pattern == null) {
                pattern = "#,##0.00";
            }
            return formatCurrency(Math.abs(d), pattern) + sign;
        } catch (Exception _ex) {
            return s;
        }
    }

    // add by Li_zd 20160928
    public static final String formatAmountCc(String s, String pattern) {
        try {
            double d = Double.valueOf(s).doubleValue();
            String sign = "";
            if (d < 0) {
                sign = "CR";
            }
            if (pattern == null) {
                pattern = "#,##0.00";
            }
            return formatCurrency(Math.abs(d), pattern) + sign;
        } catch (Exception _ex) {
            return s;
        }
    }
    
    public static final String formatAmount(String s) {
        return formatAmount(s, null,null);
    }

    /**
     * 鏍煎紡鍖栨暟瀛楀瓧绗︿覆涓虹壒瀹氭牸寮忕殑瀛楃涓�
     * @param String s 鍘熸湁鐨勬暟瀛楀瓧绗︿覆
     * @param int dot 鍚庤窡灏忔暟鐐逛綅鏁�
     * @return String 杩斿洖鐗瑰畾鏍煎紡鐨勫瓧绗︿覆
     */
    public static String formatDecimal(String s, int dot) {
        double d;
        if (s == null) {
            return "";
        }
        try {
            d = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return s;
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(dot);
        nf.setMaximumFractionDigits(dot);
        return nf.format(d);
    }

    /**
     * 鏍煎紡鍖杁ouble绫诲瀷涓虹壒瀹氭牸寮忕殑瀛楃涓�
     * @param double d 鍙岀簿搴︽暟瀛�
     * @param int dot 鍚庤窡灏忔暟鐐逛綅鏁�
     * @return String 杩斿洖鐗瑰畾鏍煎紡鐨勫瓧绗︿覆
     */
    public static String formatDecimal(double d, int dot) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(dot);
        nf.setMaximumFractionDigits(dot);
        return nf.format(d);
    }

    public static final String formatCurrency(double d, String s) {
        try {
            DecimalFormat decimalformat =
                    (DecimalFormat) NumberFormat.getInstance(Locale.US);
            decimalformat.applyPattern(s);
            return decimalformat.format(d);
        } catch (Exception exception) {
            Log.error(
                    "Exception in formatCurrency(currency="
                    + d
                    + ", format="
                    + s
                    + ")",
                    exception);
        }
        return " ";
    }

    public static final String formatDate(String s, String pattern) {

        if (pattern == null) {
            pattern = defalutDatePattern;
        }
        int len = s.length();
        try {
            if (len > 10) {
                return formatDateTime1(s, "yyyy-MM-dd HH:mm:ss", pattern);
            } else if (len == 8) {
                //涓撲负涓绘満璁剧疆
                if ("00000000".equals(s)) {
                    return "";
                }
                return formatDateTime1(s, "yyyyMMdd", pattern);
            } else if (len == 6) {
                return formatDateTime1(s, "yyMMdd", pattern);
            } else {
                return s;
            }
        } catch (Exception e1) {
            return s;
        }
    }

    public static final String formatDate(String s) {
        return formatDate(s, null);
    }

    public static final String formatTime(String s, String pattern) {
        if (pattern == null) {
            pattern = "HH:mm:ss";
        }
        try {
        	if(s.length() > 6){
                return formatDateTime1(s, "yyyy-MM-dd HH:mm:ss", "HH:mm:ss");
        	} else {
                return formatDateTime1(s, "HHmmss", "HH:mm:ss");
        	}
        } catch (Exception e1) {
            return s;
        }
    }

    public static final String formatTime(String s) {
        return formatTime(s, null);
    }

    public static final String formatDateTime(String s, String fromFormat,
                                              String pattern) {
        if (pattern == null) {
            pattern = defalutDatePattern + " HH:mm:ss";
        }
        try {
            return formatDateTime1(s, fromFormat, pattern);
        } catch (Exception e1) {
            return s;
        }
    }

    private static final String formatDateTime1(
            String s,
            String fromFormat,
            String toFormat) throws Exception {

        if (s == null || s.equals("")) {
            return " ";
        }

        SimpleDateFormat simpledateformat =
                new SimpleDateFormat(fromFormat);
        java.util.Date date =
                simpledateformat.parse(s);
        simpledateformat.applyPattern(toFormat);
        return simpledateformat.format(date);
    }

    public static final String formatRate(String s, String pattern) {
        try {
            double d = Double.valueOf(s).doubleValue();
            if (pattern == null) {
                pattern = "#,##0.0000000";
            }
            return formatCurrency(d, pattern);
        } catch (Exception _ex) {
            return s;
        }
    }
    public static final String formatRate(String s, String pattern,String lang) {//add by linrui for mul-language 20171116
        try {
            double d = Double.valueOf(s).doubleValue();
            if (pattern == null) {
                pattern = "#,##0.00000000";
            }
//            return fomate4Lang(formatCurrency(d, pattern),lang);
            return Utils.dropZeroAfter(fomate4Lang(formatCurrency(d, pattern),lang));//mod by linrui 20180515
        } catch (Exception _ex) {
            return s;
        }
    }
  //add by linrui for GAPMC-EB-001-0036 20190523
    public static final String formatName(String s) {
    	int length = 0;
    	try {
    		if(isContainChinese(s)){
    			return "*" + s.substring(1, s.length());
    		}else{
    			if(s.indexOf(" ")>=0){
    				return s.substring(0,s.indexOf(" ")) +  "*"  ;
    			}else{
    				return s;
    			}
    		}
    	} catch (Exception _ex) {
    		return s;
    	}
    }
    /*public static int getWordCount(String s)  
    {  
        int length = 0;  
        for(int i = 0; i < s.length(); i++)  
        {  
            int ascii = Character.codePointAt(s, i);  
            if(ascii >= 0 && ascii <=255)  
                length++;  
            else  
                length += 2;  

        }  
        return length;  

    }*/
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
    //end
    public static void main(String[] args){
        System.out.println( isContainChinese("sad jklashf "));
    }
    /**
	 * @param amount
	 * @param language
	 * @return
	 */
	public static String fomate4Lang(String amount, String lang) {

		//Log.info("amount=" + amount + " | " + " lang=" + lang + " | before");
		if ("pt_PT".equals(lang)) {

			String[] array = amount.split("\\.");
			String temStr = "";
			if(array.length == 1){
				temStr = array[0].replace(",", ".");
			}
			if (array.length == 2) {
				temStr = array[0].replace(",", ".");
				temStr += "," + array[1];
			}
			amount = temStr;
		}
		//Log.info("amount=" + amount + " | " + " lang=" + lang + " | after");
		return amount;
	}
	public static String transferLang(String lang){
	       if(lang.trim().equalsIgnoreCase("")||lang.trim()==null){
	    	   return Constants.US_HOST;
	       }
	       else if(lang.trim().equalsIgnoreCase(Constants.HK)||lang.trim().equalsIgnoreCase(Constants.TW)){
	 		   return Constants.HK_TW_HOST;
	 	   }
	 	   else if (lang.trim().equalsIgnoreCase(Constants.CN)){
	 		   return Constants.CN_HOST;
	 	   }
	 	   else if(lang.trim().equalsIgnoreCase(Constants.PT)){
	 		   return Constants.PT_HOST;
	 	   }else 
	 		   return Constants.US_HOST; 	   
	    }
}
