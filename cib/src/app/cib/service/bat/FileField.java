package app.cib.service.bat;

import java.text.*;
import java.util.*;
import com.neturbo.set.core.*;

public class FileField {


    private static int nextDelimiter(byte[] buf, int startPosit, byte delimiter) {
      int len = 0;
      for (int i = startPosit; i < buf.length; i++, len++) {
        if (buf[i] == delimiter) {
          break;
        }
      }
      return len;
    }

  /**
   * Parses a number of bytes into a Java object
   * Creation date: (2001-7-16 13:05:25)
   * @return java.lang.Object
   * @param rawBytes byte[]
   * @param picture char
   * @param start_pos int
   * @param length int
   * @param signed boolean
   * @param hiddenDecimal boolean Whether it uses hidden decimal
   * @param decimal int The number of decimal position (10 to the power of)
   * @exception com.csii.ebank.util.exception.CsiiException The exception description.
   */
  public static int parseFixlenField(
      byte[] rawBytes,
      HashMap parseData,
      String name,
      char picture,
      int start_pos,
      int length,
      boolean signed,
      boolean hiddenDecimal,
      int decimal) throws Exception {

    Object retObj = null;

    switch (picture) {
      case '9':
        long result = 0L;
        String result_s = null;
        try {
          if (hiddenDecimal) {
            result = Long.parseLong(new String(rawBytes, start_pos, length));
            if (decimal == 0) {
              if (result <= Integer.MAX_VALUE && result >= Integer.MIN_VALUE) {
                retObj = new Integer( (int) result);
              }
              else {
                retObj = new Long(result);
              }
            }
            else {
              retObj = new Double( ( (double) result) / Math.pow(10, decimal));
            }
          }
          else { // not using hidden decimal
            result_s = new String(rawBytes, start_pos, length);
            if (decimal == 0) {
              result = Long.parseLong(result_s);
              if (result <= Integer.MAX_VALUE && result >= Integer.MIN_VALUE) {
                retObj = new Integer( (int) result);
              }
              else {
                retObj = new Long(result);
              }
            }
            else {
              retObj = new Double(Double.parseDouble(result_s));
            }
          }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        break;

      default: {
        String s1 = new String(rawBytes, start_pos, length);
        retObj = s1;
      }
    }
    parseData.put(name, retObj);
    return length;
  }

  /**
   * Parses a number of bytes into a Java object
   * Creation date: (2001-7-16 13:05:25)
   * @return java.lang.Object
   * @param rawBytes byte[]
   * @param picture char
   * @param start_pos int
   * @param length int
   * @param signed boolean
   * @param hiddenDecimal boolean Whether it uses hidden decimal
   * @param decimal int The number of decimal position (10 to the power of)
   * @exception com.csii.ebank.util.exception.CsiiException The exception description.
   */
  public static int parseDelimiterField(
      byte[] rawBytes,
      HashMap parseData,
      String name,
      char picture,
      int start_pos,
      String delimiter,
      boolean signed,
      boolean hiddenDecimal,
      int decimal) throws Exception {

    Object retObj = null;
    byte[] byteDelimiter = delimiter.getBytes();
    int parse_length = nextDelimiter(rawBytes, start_pos,
                                                byteDelimiter[0]);
    switch (picture) {
      case '9':
        long result = 0L;
        String result_s = null;
        try {
          if (hiddenDecimal) {
            result = Long.parseLong(new String(rawBytes, start_pos,
                                               parse_length));
            if (decimal == 0) {
              if (result <= Integer.MAX_VALUE && result >= Integer.MIN_VALUE) {
                retObj = new Integer( (int) result);
              }
              else {
                retObj = new Long(result);
              }
            }
            else {
              retObj = new Double( ( (double) result) / Math.pow(10, decimal));
            }
          }
          else { // not using hidden decimal
            result_s = new String(rawBytes, start_pos, parse_length);
            if (decimal == 0) {
              result = Long.parseLong(result_s);
              if (result <= Integer.MAX_VALUE && result >= Integer.MIN_VALUE) {
                retObj = new Integer( (int) result);
              }
              else {
                retObj = new Long(result);
              }
            }
            else {
              retObj = new Double(Double.parseDouble(result_s));
            }
          }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        break;
      default: {
        String s1 = new String(rawBytes, start_pos, parse_length);
        retObj = s1;
      }
    }
    parseData.put(name, retObj);
    return parse_length+1;
  }
}
