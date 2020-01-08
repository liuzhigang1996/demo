package com.neturbo.set.utils;

import java.util.*;
import java.io.*;
import java.text.*;
import com.neturbo.set.core.*;
import com.neturbo.set.exception.NTBException;
import com.oroinc.text.regex.MalformedPatternException;
import com.oroinc.text.regex.Pattern;
import com.oroinc.text.regex.PatternCompiler;
import com.oroinc.text.regex.PatternMatcher;
import com.oroinc.text.regex.Perl5Compiler;
import com.oroinc.text.regex.Perl5Matcher;

public class Utils {

    private static final int BUFFER_SIZE = 32 * 1024;

    private static final float RATE = 1.5f;
    
    //20091116
    private static final String DEFALT_ENCODING = "GBK";

    private static final char hexChar[] = {
                                          '0', '1', '2', '3', '4', '5', '6',
                                          '7', '8', '9',
                                          'a', 'b', 'c', 'd', 'e', 'f'
    };

    private static final char decimalChar[] = {
                                              '0', '1', '2', '3', '4', '5', '6',
                                              '7', '8', '9'
    };

    private Utils() {
    }

    public static String appendSpace(String s, int i) {
        if (s == null) {
            s = "";
        }
        int j = i - s.getBytes().length;
        if (j <= 0) {
            return s.substring(0, i);
        }
        StringBuffer stringbuffer = new StringBuffer(s);
        for (int k = 0; k < j; k++) {
            stringbuffer.append(' ');

        }
        return stringbuffer.toString();
    }

    public static final String prefixZero(String s, int i) {
        StringBuffer stringbuffer = new StringBuffer(i);
        for (int j = 0; j < i - s.length(); j++) {
            stringbuffer.append('0');

        }
        stringbuffer.append(s);
        return stringbuffer.toString();
    }

    public static final String prefixSpace(String s, int i) {
        StringBuffer stringbuffer = new StringBuffer(i);
        for (int j = 0; j < i - s.length(); j++) {
            stringbuffer.append(' ');

        }
        stringbuffer.append(s);
        return stringbuffer.toString();
    }

    public static final String bytes2HexStr(byte abyte0[]) {
        StringBuffer stringbuffer = new StringBuffer(abyte0.length * 2);
        for (int i = 0; i < abyte0.length; i++) {
            stringbuffer.append(hexChar[abyte0[i] >>> 4 & 0xf]);
            stringbuffer.append(hexChar[abyte0[i] & 0xf]);
        }

        return stringbuffer.toString();
    }

    public static final void copyIntoByteArray(byte abyte0[], byte abyte1[],
                                               int i, int j) {
        for (int k = 0; k < j; k++) {
            abyte1[k + i] = abyte0[k];

        }
    }

    public static final byte[] hexStr2Bytes(String s) {
        if (s.length() % 2 != 0) {
            s = "0" + s;
        }
        byte abyte0[] = new byte[s.length() / 2];
        for (int i = 0; i < abyte0.length; i++) {
            if (2 * i + 2 < s.length()) {
                abyte0[i] = (byte) Integer.parseInt(s.substring(2 * i,
                        2 * i + 2), 16);
            } else {
                abyte0[i] = (byte) Integer.parseInt(s.substring(2 * i), 16);
            }
        }
        return abyte0;
    }

    public static final byte[] binStr2Bytes(String s) {
        byte abyte0[] = new byte[(s.length() + 7) / 8];
        for (int i = 0; i < abyte0.length; i++) {
            if (8 * i + 8 < s.length()) {
                abyte0[i] = (byte) Integer.parseInt(s.substring(8 * i,
                        8 * i + 8), 2);
            } else {
                abyte0[i] = (byte) Integer.parseInt(s.substring(8 * i), 2);
            }
        }
        return abyte0;
    }

    public static byte[] convHexOrBin(String value) {
        if (value == null) {
            return new byte[] {};
        }
        if (value.length() < 4) {
            try {
				return value.getBytes(DEFALT_ENCODING);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        byte[] resultBytes = null;
		try {
			resultBytes = value.getBytes(DEFALT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        String symbol = value.substring(0, 2);
        value = value.substring(2);
        if (symbol.toUpperCase().equals("0X")) {
            resultBytes = hexStr2Bytes(value);
        }
        if (symbol.toUpperCase().equals("0B")) {
            resultBytes = binStr2Bytes(value);
        }
        return resultBytes;
    }

    public static final int charInStr(char c, String s) {
        int i = 0;
        for (int j = 0; j < s.length(); j++) {
            if (s.charAt(j) == c) {
                i++;
            }
        }
        return i;
    }

    public static final String null2Empty(Object s) {
        return s != null ? s.toString() : "";
    }

    public static final String null2EmptyWithTrim(Object s) {
        if (s == null) {
            return "";
        } else {
            return s.toString().trim();
        }
    }

    public static final int nullEmpty2Zero(Object s) {
        String str = s != null ? s.toString() : "";
        int i;
        try {
            i = Integer.parseInt(str);
        } catch (Exception e) {
            i = 0;
        }
        return i;
    }
    //add by linrui 20180328
    public static final Double nullEmptyToZero(Object s) {
        String str = s != null ? s.toString() : "";
        Double i;
        try {
            i = Double.parseDouble(str);
        } catch (Exception e) {
            i = 0.00;
        }
        return i;
    }

    public static String convertEncoding(String strvalue, String fromEncoding,
                                         String toEncoding) {
        try {
            if (strvalue == null) {
                return null;
            } else {
                strvalue = new String(strvalue.getBytes(fromEncoding),
                                      toEncoding);
                return strvalue;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 锟斤拷ISO8859-1锟街凤拷转锟斤拷GBK锟街凤拷
     * @param String     ISO8859-1锟街凤拷
     * @return String    GBK锟街凤拷
     */
    public static String convertISO2GBK(String str) {
        if (str == null) {
            return null;
        }
        try {
            return new String(str.getBytes("ISO8859_1"), "GBK");
        } catch (Exception e) {
            return str;
        }
    }

    /**
     * 锟斤拷GB2312锟街凤拷转锟斤拷ISO8859-1锟街凤拷
     * @param String     GB2312锟街凤拷
     * @return String    ISO8859-1锟街凤拷
     */
    public static String convertGBK2IS0(String str) {
        if (str == null) {
            return null;
        }
        try {
            return new String(str.getBytes("GB2312"), "ISO8859_1");
        } catch (Exception e) {
            return str;
        }
    }

    public static String[] splitStr(String strSource, String spliter) {

        StrTokenizer st = new StrTokenizer(strSource, spliter, true, false);
        String[] retstrs = new String[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens()) {
            retstrs[i++] = (String) st.nextToken();
        }
        return retstrs;
    }

// 锟芥换锟街凤拷锟斤拷
    // String strSource - 源锟街凤拷
    // String strFrom   - 要锟芥换锟斤拷锟接达拷
    // String strTo     - 锟芥换为锟斤拷锟街凤拷
    public static String replaceStr(String strSource, String strFrom,
                                    String strTo) {
        // 锟斤拷锟揭拷婊伙拷锟斤拷哟锟轿拷眨锟斤拷锟街憋拷臃锟斤拷锟皆达拷锟�
        if (strSource == null || strSource.equals("")) {
            return strSource;
        }
        String strDest = "";
        // 要锟芥换锟斤拷锟接达拷锟斤拷锟斤拷
        int intFromLen = strFrom.length();
        int intPos;
        // 循锟斤拷锟芥换锟街凤拷
        while ((intPos = strSource.indexOf(strFrom)) != -1) {
            // 锟斤拷取匹锟斤拷锟街凤拷锟斤拷锟斤拷锟接达拷
            strDest = strDest + strSource.substring(0, intPos);
            // 锟斤拷锟斤拷锟芥换锟斤拷锟斤拷哟锟�
            strDest = strDest + strTo;
            // 锟睫革拷源锟斤拷为匹锟斤拷锟接达拷锟斤拷锟斤拷哟锟�
            strSource = strSource.substring(intPos + intFromLen);
        }
        // 锟斤拷锟斤拷没锟斤拷匹锟斤拷锟斤拷哟锟�
        strDest = strDest + strSource;
        // 锟斤拷锟斤拷
        return strDest;
    }
    public static String replaceStrForCaculater(String strSource, String strFrom,
    		String strTo) {
    	// 锟斤拷锟揭拷婊伙拷锟斤拷哟锟轿拷眨锟斤拷锟街憋拷臃锟斤拷锟皆达拷锟�
    	if (strSource == null || strSource.equals("")) {
    		return "0";
    	}
    	String strDest = "";
    	// 要锟芥换锟斤拷锟接达拷锟斤拷锟斤拷
    	int intFromLen = strFrom.length();
    	int intPos;
    	// 循锟斤拷锟芥换锟街凤拷
    	while ((intPos = strSource.indexOf(strFrom)) != -1) {
    		// 锟斤拷取匹锟斤拷锟街凤拷锟斤拷锟斤拷锟接达拷
    		strDest = strDest + strSource.substring(0, intPos);
    		// 锟斤拷锟斤拷锟芥换锟斤拷锟斤拷哟锟�
    		strDest = strDest + strTo;
    		// 锟睫革拷源锟斤拷为匹锟斤拷锟接达拷锟斤拷锟斤拷哟锟�
    		strSource = strSource.substring(intPos + intFromLen);
    	}
    	// 锟斤拷锟斤拷没锟斤拷匹锟斤拷锟斤拷哟锟�
    	strDest = strDest + strSource;
    	// 锟斤拷锟斤拷
    	return strDest;
    }

    //私锟叫猴拷锟斤拷锟斤拷锟斤拷锟叫讹拷锟斤拷锟斤拷锟斤拷锟角凤拷锟絢ey
    public static boolean arrayContainKey(String[] keyArray, String key) {
        key = key.toUpperCase();
        for (int i = 0; i < keyArray.length; i++) {
            if (key.equals(keyArray[i].toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    //私锟叫猴拷锟斤拷锟斤拷锟斤拷锟叫讹拷锟斤拷锟斤拷锟斤拷锟角凤拷锟絢ey
    public static String[] arrayList2StrArray(ArrayList list) {
        String[] retArray = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            retArray[i] = list.get(i).toString();
        }
        return retArray;
    }

    /**
     * 锟斤拷式锟斤拷String为double锟酵ｏ拷锟斤拷锟斤拷址锟轿拷眨锟斤拷锟斤拷锟�0.
     * @param String s 双锟斤拷锟斤拷锟斤拷锟斤拷
     * @return double 锟斤拷锟斤拷double锟斤拷锟斤拷锟斤拷值
     */
    public static double parseDouble(String s) {
        double d = 0;
        if (s != null) {
            try {
                d = Double.parseDouble(s);
            } catch (NumberFormatException e) {
                d = 0;
                e.printStackTrace();
            }
        }
        return d;
    }

    /**
     * 锟斤拷式锟斤拷String为float锟酵ｏ拷锟斤拷锟斤拷址锟轿拷眨锟斤拷锟斤拷锟�0.
     * @param String s 双锟斤拷锟斤拷锟斤拷锟斤拷
     * @return float 锟斤拷锟斤拷float锟斤拷锟斤拷锟斤拷值
     */
    public static float parseFloat(String s) {
        float f = 0;
        if (s != null) {
            try {
                f = Float.parseFloat(s);
            } catch (NumberFormatException e) {
                f = 0;
                e.printStackTrace();
            }
        }
        return f;
    }

    /**
     * 锟斤拷式锟斤拷String为int锟酵ｏ拷锟斤拷锟斤拷址锟轿拷眨锟斤拷锟斤拷锟�0.
     * @param String s 双锟斤拷锟斤拷锟斤拷锟斤拷
     * @return int 锟斤拷锟斤拷int锟斤拷锟斤拷锟斤拷值
     */
    public static int parseInt(String s) {
        int i = 0;
        if (s != null) {
            try {
                i = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                i = 0;
                e.printStackTrace();
            }
        }
        return i;
    }

    /**
     * 锟斤拷式锟斤拷锟斤拷锟斤拷String锟斤拷锟斤拷锟斤拷锟叫的讹拷锟斤拷去锟斤拷锟斤拷锟斤拷址锟轿拷眨锟斤拷锟斤拷锟�0.
     * @param String s 双锟斤拷锟斤拷锟斤拷锟斤拷
     * @return String 锟斤拷锟斤拷锟铰的革拷式锟街凤拷
     */
    public static String StringConvertor(String s) {
        StringBuffer bf = new StringBuffer();
        if (s != null) {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ',') {
                    continue;
                } else {
                    bf.append(s.charAt(i));
                }
            }
        }
        return bf.toString();
    }

    /**
     * 锟斤拷锟斤拷锟斤拷锟街凤拷喜锟轿拷锟街革拷锟斤拷指锟斤拷锟斤拷址锟�,锟街凤拷锟斤拷式为"01,02,03"
     * @param String inputStr 	锟斤拷锟斤拷锟斤拷址锟�
     * @param Sting separateStr 锟街凤拷之锟斤拷姆指锟斤拷锟�
     * @param boolean bl    	锟角凤拷锟节分革拷锟斤拷址锟絵锟竭硷拷锟较碉拷锟斤拷锟�
     * @return String 			锟斤拷锟截达拷指锟斤拷锟斤拷址锟�
     */
    public static String combineStr(
            String[] inputStrArr,
            String separateStr,
            boolean bl) {

        //if the input array is null or the input array's length is "0" then return null
        if ((inputStrArr == null) || (inputStrArr.length == 0)) {
            return null;
        }
        //else,structure a string with separater for output
        String outString = "";
        if (bl) {
            for (int i = 0; i < inputStrArr.length; i++) {
                outString += "'" + inputStrArr[i] + "'" + separateStr;
            }
        } else {
            for (int i = 0; i < inputStrArr.length; i++) {
                outString += inputStrArr[i] + separateStr;
            }
        }
        outString = outString.substring(0, outString.length() - 1);
        return outString;
    }

    /**
     * 锟芥换锟街凤拷锟斤拷delim锟斤拷锟斤拷址锟�
     * @param String msg 	锟斤拷锟斤拷锟斤拷址锟�
     * @param Hashtable     锟芥换锟斤拷锟街凤拷锟斤拷
     * @param delim            锟街革拷锟�
     * @return String 			锟斤拷锟斤拷锟街凤拷
     */
    public static String getReplaceStr(
            String msg,
            Hashtable paraMap,
            String delim) {
        StringTokenizer token = new StringTokenizer(msg, delim);
        int i = 0;
        StringBuffer returnStr = new StringBuffer();

        while (token.hasMoreTokens()) {
            String divmsg = token.nextToken();
            i++;
            if (i / 2 * 2 == i) {
                String value = (String) paraMap.get(divmsg);
                returnStr.append(value);
            } else {
                returnStr.append(divmsg);
            }
        }
        return returnStr.toString();
    }

    /**
     * 锟斤拷锟斤拷)
     * @return
     */
    private static int delWithK(String msg, boolean count_sub) {
        int count_b = 0;
        int count_e = 0;
        //取(锟斤拷前锟斤拷锟斤拷锟�
        if (msg.indexOf(")") == -1) {
            return 0;
        }
        //取)前锟斤拷锟街凤拷
        String t_msg = msg.substring(0, msg.indexOf(")"));
        //锟斤拷锟斤拷锟斤拷锟�
        int t = 0;
        t = t_msg.indexOf("(", t);
        while (t != -1) {
            count_b++;
            t = t_msg.indexOf("(", t + 1);

        }

        //锟斤拷锟斤拷(锟侥革拷锟斤拷
        int tmp_index = msg.indexOf("(", msg.indexOf(")") + 1);

        if (tmp_index == -1) {

            tmp_index = msg.length();
        }

        String e_msg = msg.substring(0, tmp_index);

        t = 0;
        t = e_msg.indexOf(")", t);
        while (t != -1) {
            count_e++;
            t = e_msg.indexOf(")", t + 1);

        }

        if (count_b <= count_e && !count_sub) {
            return count_e;
        }
        //锟斤拷锟斤拷锟斤拷锟铰的革拷锟斤拷
        String sub_str = msg.substring(tmp_index);
        count_e = count_e + delWithK(sub_str, true);

        return count_e;
    }

    /**
     * 去锟斤拷(锟斤拷
     * @param msg
     * @return
     */
    private static String divK(
            String msg,
            Hashtable paraMap,
            String delim,
            boolean ifdiv) {
        int t_div = delWithK(msg, false);
        int k_index = msg.indexOf("(");
        if (k_index == -1) {
            return msg;
        }
        int div = 0;
        for (int i = 0; i < t_div; i++) {
            div = msg.indexOf(")", div + 1);

        }
        String f_s = msg.substring(0, k_index);
        String div_s = msg.substring(k_index + 1, div);
        //logger.debug("fs=[" + f_s + "]");
        //logger.debug("divs=[" + div_s + "]");
        div_s = getReplaceStr(div_s, paraMap, delim, ifdiv); //取锟斤拷锟斤拷锟斤拷
        String e_s = msg.substring(div + 1);
        e_s = divK(e_s, paraMap, delim, ifdiv); //锟叫讹拷锟角凤拷锟斤拷(
        if (div_s == null || div_s.length() == 0) {
            f_s = f_s.trim();
            e_s = e_s.trim();
            msg = f_s + e_s;

            if (f_s.trim().endsWith("and") || f_s.trim().endsWith("AND")) {
                msg = f_s.substring(0, f_s.length() - 3) + e_s;
            }
            if (f_s.trim().endsWith("or") || f_s.trim().endsWith("OR")) {
                msg = f_s.substring(0, f_s.length() - 2) + e_s;
            }

        } else {
            div_s = div_s.trim();
            if (div_s.startsWith("or") || div_s.startsWith("OR")) {
                div_s = "1=0 " + div_s;
            }
            if (div_s.startsWith("and") || div_s.startsWith("AND")) {
                div_s = "1=1 " + div_s;
            }
            if (div_s.endsWith("or") || div_s.endsWith("OR")) {
                div_s = div_s + " 1=0";
            }
            if (div_s.endsWith("and") || div_s.endsWith("AND")) {
                div_s = div_s + " 1=1";
            }
            msg = f_s + "( " + div_s + " )" + e_s;
        }
        return msg;
    }

    /**
     * 锟芥换锟街凤拷锟斤拷delim锟斤拷锟斤拷址锟�
     * @param String msg 	锟斤拷锟斤拷锟斤拷址锟�
     * @param Hashtable     锟芥换锟斤拷锟街凤拷锟斤拷
     * @param delim            锟街革拷锟�
     * @param boolean ifdiv   锟角凤拷取锟斤拷为锟斤拷锟街讹拷
     * @return String 			锟斤拷锟斤拷锟街凤拷
     */
    public static String getReplaceStr(
            String msg,
            Hashtable paraMap,
            String delim,
            boolean ifdiv) {
        if (msg.indexOf(delim) == -1) {
            return msg;
        }
        //锟斤拷锟斤拷(锟脚ｏ拷(锟斤拷锟斤拷锟斤拷只锟斤拷锟斤拷一锟斤拷

        int k_index = msg.indexOf("(");
        if (k_index != -1) {
            msg = divK(msg, paraMap, delim, ifdiv);

        }

        StringTokenizer token = new StringTokenizer(msg, delim);
        int i = 0;
        if (msg.startsWith(delim)) {
            i++;
        }
        StringBuffer returnStr = new StringBuffer();
        String msg_name = "";
        while (token.hasMoreTokens()) {
            String divmsg = token.nextToken();
            //logger.debug("i=" + i + "divmsg=" + divmsg);
            i++;
            if (i / 2 * 2 == i) {
                //logger.debug("divmsg=" + divmsg);
                String value = (String) paraMap.get(divmsg);
                if (value != null && ifdiv && value.equals("")) {
                    //锟斤拷锟斤拷锟揭★拷锟轿拷锟斤拷侄锟�
                    continue;
                } else {
                    returnStr.append(msg_name);
                    returnStr.append(value);
                }
            } else {
                msg_name = divmsg;
            }

        }
        if (i / 2 * 2 != i && !msg_name.equals("")) {
            returnStr.append(msg_name);
        }
        return returnStr.toString();
    }

    /**
     * 锟斤拷锟斤拷页锟斤拷锟斤拷锟绞�,锟斤拷\n,转为<br>,锟斤拷锟饺筹拷锟斤拷50锟斤拷锟街凤拷,锟斤拷锟揭伙拷锟�<br>
     * @param str
     * @return
     */
    //	public static String getHtmlStr(String str){
    //	  //去锟斤拷锟街凤拷锟叫碉拷html锟斤拷签
    //	  /*
     //	   String[] htmstr ={"<br>","<td>","</td>","<tr>","</tr>","</","\\>"};
     //		for(int i=0;i<htmstr.length;i++){
     //		  str=str.replaceAll(htmstr[i],"\n");
     //	   }*/
    //	   str=str.replaceAll("<","&lt;");
    //	   str=str.replaceAll(">","&gt;");
    //	   //str=str.replaceAll("'","&apos;");
    //	   str=str.replaceAll("\"","&quot;");
    //	   str=str.replaceAll(" ","&nbsp;");
    //	   String ret=str;
    //	   if((str.indexOf("\n")) ==-1 && str.length()>50){
    //		   ret="";
    //		   //锟斤拷锟斤拷50锟斤拷染图锟�<br>
    //		   while(str.length()>50){
    //			  String tmpStr="";
    //			  tmpStr = str.substring(0,50);
    //			  tmpStr = tmpStr+"<br>";
    //			  str = str.substring(50);
    //			  ret = ret + tmpStr;
    //		   }
    //		   ret = ret +str;
    //	   }
    //	   else{
    //		ret = str.replaceAll("\n","<br>");
    //	   }
    //	   return ret;
    //	}
    /**
     *  锟矫碉拷utf锟斤拷式锟斤拷锟街凤拷,锟斤拷应锟斤拷锟斤拷锟斤拷锟斤拷锟侥硷拷时,锟斤拷锟侥硷拷锟斤拷拇锟斤拷锟�
     */
    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = null;
                    //b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) {
                        k += 256;
                    }
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 锟斤拷锟斤拷锟斤拷锟絰取全锟斤拷锟街癸拷
     * @param res
     * @param dFile
     * @param dName
     */
    public static byte[] getAllBytesFromStream(InputStream is) throws
            IOException {
        byte[] bytes = null;
        byte[] tmpBytes = null;
        int totalRead = 0;
        int readBytes = 0;

        bytes = new byte[BUFFER_SIZE];

        while ((readBytes =
                is.read(bytes, totalRead, bytes.length - totalRead))
                != -1) {

            totalRead += readBytes;

            if (totalRead == bytes.length) {
                tmpBytes = new byte[Math.round(bytes.length * RATE)];
                System.arraycopy(bytes, 0, tmpBytes, 0, totalRead);
                bytes = tmpBytes;
            }
        }

        if (totalRead < bytes.length) {
            tmpBytes = new byte[totalRead];
            System.arraycopy(bytes, 0, tmpBytes, 0, totalRead);
            bytes = tmpBytes;
        }

        return bytes;
    }

    /**
     * hjs: 去锟斤拷锟街凤拷前锟斤拷锟叫碉拷"0"
     * @param s
     * @return
     * @throws NTBException
     */
    public static String removePrefixZero(String s) throws NTBException {
        s = s.trim();
        int i = 0;
        for (; i < s.length()-1; i++) {
            if (s.charAt(i) != 48) {
                break;
            }
        }
        String s1 = s.substring(i);
        return s1;
    }
    
  //add by linrui for change period to tinor
	public static int peToTinor(String period){
		RBFactory rb = RBFactory.getInstance("app.cib.resource.common.period");
		if( null != rb.getString(period)){
			return Integer.parseInt(rb.getString(period));
		}else{
			return Integer.parseInt(period);
		}
	}
	
  //add by gzy for change period
	public static String tinor2pe(String period){
		RBFactory rb = RBFactory.getInstance("app.cib.resource.common.period_2");
		if( null != rb.getString(period)){
			return rb.getString(period).toString();
		}else{
			return period;
		}
		
	}
	//add by linrui 20180329
	public static String dropZeroAfter(String str){
		return str.indexOf(".")>0?str.replaceAll("0+?$", "").replaceAll("[.]$", ""):str;
	}
	//add by linrui 20190510 for rate
	public static String addZeroForNum(String str, int peddingLen) {
		   int strLen = 0;
		       while (strLen < peddingLen) {
                StringBuffer sb = new StringBuffer();
		           sb.append(str).append("0");
		           str = sb.toString();
		           strLen ++;
		       }
		   return str;
		}
	public static String dropZeroAfterNotPoint(String str){
		return str.indexOf(".")>0?str.replaceAll("0+?$", ""):str;
	}
	//end
	//add by linrui for mul-languege 20190729
	//add by linrui 20180522
    public static String subTotalString(String language){
    	RBFactory rbList = RBFactory.getInstance("app.cib.resource.enq.account_enquiry", language);
		return rbList.getString("Sub_Total");
    }
  //add by linrui 20190124
    public static String getStringFromProperties(String propertiesPath, String parameterName, String language){
    	RBFactory rbList = RBFactory.getInstance(propertiesPath, language);
		return rbList.getString(parameterName)==null?"":rbList.getString(parameterName);
    }
    public static boolean matchPattern(String textPattern, String inputString) {
		try {
			if (inputString == null || "".equals(inputString)) {
				return true;
			}
			if ((inputString.contains("鈥�") || inputString.contains("鈥�"))) {
				inputString = inputString.replaceAll("鈥�", "");
				inputString = inputString.replaceAll("鈥�", "");
			}
			PatternMatcher matcher = new Perl5Matcher();
			PatternCompiler compiler = new Perl5Compiler();
			Pattern pattern = null;

			pattern = compiler.compile(textPattern,
					Perl5Compiler.CASE_INSENSITIVE_MASK);
			if (matcher.matches(inputString, pattern)) {
				return true;
			}
		} catch (MalformedPatternException e) {
			Log.info("MobileUtils matchPattern error",e);
		}
		return false;
	}


    public static void main(String[] args) throws NTBException {
        System.out.println(removePrefixZero("040"));
    }

}
