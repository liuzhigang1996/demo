package com.neturbo.set.transaction;

import java.text.*;
import java.util.*;
import com.neturbo.set.xml.*;
import com.neturbo.set.utils.*;
import com.neturbo.set.exception.*;

public class Packet_Field {

  /**
   * Formatting a primitive field into a byte array.
   * Creation date: (2002-1-21 16:01:23)
   * @param picture char 'l', 'r', '9', '0', 'b'
   * @param buf byte[] the part of array that will contain the result
   * @param value java.lang.Object The value to be formatted
   * @param start_pos int The offset in the buffer buf
   * @param length int The length of the result byte (sub-)array
   * @param signed boolean Reserved for future use
   * @param hiddenDecimal boolean Whether it uses hidden decimal
   * @param decimal int The number of decimal position (10 to the power of)
   * @exception com.csii.ebank.util.exception.CsiiException
   */
  public static int formatFixlenField(
      byte[] buf,
      char picture,
      String value,
      int start_pos,
      int length,
      boolean signed,
      boolean hiddenDecimal,
      int decimal) throws NTBException {
    String value2 = null;
    try {
      if (picture == '9' || picture == '0') {
        if (hiddenDecimal) {
          value2 = TransUtils.rmDecimalPoint(value, decimal);
        }
        else {
          value2 = TransUtils.trimDecimalPoint(value, decimal);
        }
        byte[] bs = new byte[length];
        if (picture == '9') {
          bs = (Utils.prefixSpace(value2, length)).getBytes();
        }
        else {
          bs = (Utils.prefixZero(value2, length)).getBytes();
        }
        Utils.copyIntoByteArray(bs, buf, start_pos, length);
      }
      if (picture == 'l' || picture == 'r') {
        String s = (value.toString()).trim();
        if (picture == 'r') {
          s = Utils.appendSpace(s, length);
        }
        else { //picture == 'l'
          s = Utils.appendSpace(s, length);
        }
        System.arraycopy(s.getBytes("GBK"), 0, buf, start_pos, length);
      }
      if (picture == 'b') {
        byte[] bs = Utils.hexStr2Bytes(value);
        Utils.copyIntoByteArray(bs, buf, start_pos, length);
      }
    }
    catch (Exception e) {
    }
    return (length);
  }

  /**
   * Formatting a primitive field into a byte array.
   * Creation date: (2002-1-21 16:01:23)
   * @param picture char 'l', 'r', '9', '0', 'b'
   * @param buf byte[] the part of array that will contain the result
   * @param value java.lang.Object The value to be formatted
   * @param start_pos int The offset in the buffer buf
   * @param delimiter
   * @param signed boolean Reserved for future use
   * @param hiddenDecimal boolean Whether it uses hidden decimal
   * @param decimal int The number of decimal position (10 to the power of)
   * @exception com.csii.ebank.util.exception.CsiiException
   */
  public static int formatDelimiterField(
      byte[] buf,
      char picture,
      String value,
      int start_pos,
      String delimiter,
      boolean signed,
      boolean hiddenDecimal,
      int decimal) throws NTBException {
    String value2 = null;
    int length = 0;
    try {
      if (picture == '9' || picture == '0') {
        if (hiddenDecimal) {
          value2 = TransUtils.rmDecimalPoint(value, decimal);
        }
        else {
          value2 = TransUtils.trimDecimalPoint(value, decimal);
        }
        byte[] bs = (value2 + delimiter).getBytes();
        length = bs.length;
        Utils.copyIntoByteArray(bs, buf, start_pos, length);
      }
      else {
        String s = (value.toString()).trim() + delimiter;
        length = s.length();
        System.arraycopy(s.getBytes("GBK"), 0, buf, start_pos, s.length());
      }
    }
    catch (Exception e) {
    }
    return (length);
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
    int parse_length = TransUtils.nextDelimiter(rawBytes, start_pos,
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
