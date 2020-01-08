package com.neturbo.set.transaction;

import java.text.*;
import java.util.*;
import java.math.*;
import com.neturbo.set.xml.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;
import com.neturbo.set.exception.*;

public class TransUtils {
  public TransUtils() {
  }

  public static int nextDelimiter(byte[] buf, int startPosit, byte delimiter) {
    int len = 0;
    for (int i = startPosit; i < buf.length; i++, len++) {
      if (buf[i] == delimiter) {
        break;
      }
    }
    return len;
  }

  public static final String rmDecimalPoint(double d, int i)
     throws Exception
 {
     long l = Math.round(d * Math.pow(10, i));
     return "" + l;
 }

  public static final String rmDecimalPoint(String s, int i)
      throws Exception
  {
      StringTokenizer stringtokenizer = new StringTokenizer(s, ".");
      String s1 = stringtokenizer.nextToken();
      String s2 = stringtokenizer.hasMoreTokens() ? stringtokenizer.nextToken() : "";
      int j = s2.length();
      StringBuffer stringbuffer = new StringBuffer(s1);
      int k = j - i;
      if(k == 0)
          stringbuffer.append(s2);
      else
      if(k > 0)
      {
          stringbuffer.append(s2.substring(0, i));
      } else
      {
          k = -k;
          stringbuffer.append(s2);
          for(int l = 0; l < k; l++)
              stringbuffer.append('0');

      }
      return stringbuffer.toString();
  }

  /**
   * Formats the input double according to the decimal points required
   * Creation date: (2001-8-25 13:45:52)
   * @return java.lang.String
   * @param d double
   * @param decimal int number of decimal point required in the result (>= 0)
   * @exception java.lang.Exception The exception description.
   */
  public static String trimDecimalPoint(double d, int decimal) throws java.lang.
      Exception {
    StringBuffer decimals = new StringBuffer();
    if (decimal < 0) {
      throw new Exception("Negative decimal in trimDecimalPoint()");
    }
    for (int i = 0; i < decimal; i++) {
      decimals.append('0');
    }
    String format = null;
    if (decimal > 0) {
      format = "0." + decimals.toString();
    }
    else {
      format = "0";
    }
    NumberFormat nf = NumberFormat.getInstance(Locale.US);
    DecimalFormat df = (DecimalFormat) nf;
    df.applyPattern(format);
    return df.format(d);
  }

  /**
   * Formats the input string with number of decimal points required
   * Assumption: the input string is well-formed, i.e., in the format like: 9*[.9*], a
   * non-empty sequence of digits optionally followed by a decimal point and a
   * non-empty sequence of digits.
   * Truncation occurs when the string contains more decimal digits than hiddenDecimal;
   * Appending zeros occurs when the string contains fewer decimal digits than hiddenDecimal.
   * Creation date: (2001-10-25 19:35:07)
   * @return java.lang.String
   * @param s java.lang.String a string representation of numerical value with a decimal point
   * @param decimalRequired int number of decimal points required
   * @exception java.lang.Exception
   */
  public static String trimDecimalPoint(String s, int decimalRequired) throws
      Exception {
    StringTokenizer st = new StringTokenizer(s, ".");
    String beforePoint = st.nextToken();
    String afterPoint = (st.hasMoreTokens() ? st.nextToken() : "");
    int decimals = afterPoint.length();
    StringBuffer sb = new StringBuffer(beforePoint + '.');
    int diff = decimals - decimalRequired;
    if (diff == 0) {
      sb.append(afterPoint);
    }
    else if (diff > 0) {
      sb.append(afterPoint.substring(0, decimalRequired));
    }
    else {
      diff = -diff;
      sb.append(afterPoint);
      for (int i = 0; i < diff; i++) {
        sb.append('0');
      }
    }
    return sb.toString();
  }

}