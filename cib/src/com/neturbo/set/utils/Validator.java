package com.neturbo.set.utils;

import com.neturbo.set.core.*;
import com.neturbo.set.exception.*;
import java.util.StringTokenizer;

public class Validator {

  public Validator() {
  }

  //����ʽ
  public static final String checkFormat(String checkStr, String format){
    if (format.toUpperCase().equals("DATE")) {
      if (!checkDate(checkStr)) {
        return ("431");
      }
      return null;
    }
    if (format.toUpperCase().equals("AMOUNT")) {
      if (!checkAmount(checkStr)) {
        return ("432");
      }
      return null;
    }
    if (format.toUpperCase().equals("NUMBER")) {
      if (!checkNumber(checkStr)) {
        return ("433");
      }
      return null;
    }
    if (format.toUpperCase().equals("EMAIL")) {
      if (!checkEmail(checkStr)) {
        return ("434");
      }
      return null;
    }
    if (format.toUpperCase().equals("TIME")) {
      if (!checkTime(checkStr)) {
        return ("435");
      }
      return null;
    }
    if (format.toUpperCase().equals("PHONENO")) {
      if (!checkPhoneNo(checkStr)) {
        return ("436");
      }
      return null;
    }
    if (format.toUpperCase().equals("DOUBLE")) {
      if (!checkDouble(checkStr)) {
        return ("437");
      }
      return null;
    }
    //�����ʽ�ִ�����ȷ
    Log.warn( "Format(" + format + ") not defined when format checking");
    return null;
  }

  public static final boolean checkAmount(String s) {

    //���Ϊ��
    if (s == null || s.equals("")) {
      return false;
    }

    //ȥ������
    byte abyte0[] = s.getBytes();
    int i = abyte0.length;
    byte abyte1[] = new byte[i];
    int j = 0;
    for (int k = 0; k < i; k++) {
      if (abyte0[k] != 44) {
        abyte1[j++] = abyte0[k];

      }
    }
    String s1 = new String(abyte1, 0, j);
    if (s1 == null || s1.equals("")) {
      return false;
    }

    //���С����󳬹���λ
    int l = s1.indexOf(".");
    if (l == -1) {
      String s2 = s1.substring(l + 1);
      if (s2.length() > 2) {
        return false;
      }
    }

    //����Խ���Ϊ��ֵ����
    try {
      Double.parseDouble(s1);
    }
    catch (Exception _ex) {
      return false;
    }

    //������ȷ
    return true;
  }

  public static final boolean checkDate(String s) {

    int ai[] = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    int ai1[] = {
        31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    //���Ϊ��
    if (s == null || s.equals("")) {
      return false;
    }

    //������Ȳ���ȷ
    int l = s.length();
    if (l != 8 && l != 10) {
      return false;
    }

    //����ִ�
    String s1;
    String s2;
    String s3;
    if (l == 8) {
      s1 = s.substring(0, 4);
      s2 = s.substring(4, 6);
      s3 = s.substring(6);
    }
    else {
      s1 = s.substring(0, 4);
      s2 = s.substring(5, 7);
      s3 = s.substring(8);
      if (!s.substring(4, 5).equals("-") || !s.substring(7, 8).equals("-")) {
        return false;
      }
    }

    //����������ֲ���ȷ
    int i;
    int j;
    int k;
    try {
      i = Integer.parseInt(s1);
      j = Integer.parseInt(s2);
      k = Integer.parseInt(s3);
    }
    catch (Exception _ex) {
      return false;
    }

    //������ڲ�������Χ��
    if (i < 0 || j < 1 || j > 12 || k < 1) {
      return false;
    }
    if (isLeapYear(i)) {
      if (k > ai1[j - 1]) {
        return false;
      }
    }
    else {
      if (k > ai[j - 1]) {
        return false;
      }
    }

    //������ȷ
    return true;
  }

  public static final boolean checkDouble(String s) {

    //���Ϊ��
    if (s == null || s.equals("")) {
      return false;
    }

    //����Խ���Ϊ��ֵ����
    try {
      Double.parseDouble(s);
    }
    catch (Exception _ex) {
      return false;
    }

    //������ȷ
    return true;
  }

  public static final boolean checkEmail(String s) {
    //���Ϊ��
    if (s == null || s.equals("")) {
      return false;
    }

    //���Email��������
    try {
      if (s.indexOf("@") == -1) {
        return false;
      }
    }
    catch (Exception _ex) {
      return false;
    }

    //������ȷ
    return true;
  }

  public static final boolean checkNumber(String s) {

    //���Ϊ��
    if (s == null || s.equals("")) {
      return false;
    }

    //����������ִ���
    byte abyte0[] = s.getBytes();
    for (int i = 0; i < abyte0.length; i++) {
      if (abyte0[i] < 48 || abyte0[i] > 57) {
        return false;
      }
    }

    //������ȷ
    return true;
  }

  public static final boolean checkPhoneNo(String s) {

    //���Ϊ��
    if (s == null || s.equals("")) {
      return false;
    }

    //��������绰�������
    try {
      String s1;
      for (StringTokenizer stringtokenizer = new StringTokenizer(s, ", ;-");
           stringtokenizer.hasMoreTokens();
           Long.parseLong(s1)) {
        s1 = stringtokenizer.nextToken();

      }
    }
    catch (Exception _ex) {
      return false;
    }

    //������ȷ
    return true;
  }

  public static final boolean checkTime(String s) {

    //���Ϊ��
    if (s == null || s.equals("")) {
      return false;
    }

    int i = s.length();
    if (i != 6 && i != 8) {
      return false;
    }

    if (i == 6) {
      String s1 = s.substring(0, 2);
      String s2 = s.substring(2, 4);
      String s3 = s.substring(4);
      if (s1.compareTo("00") < 0 || s1.compareTo("23") > 0) {
        return false;
      }
      if (s2.compareTo("00") < 0 || s2.compareTo("59") > 0) {
        return false;
      }
      if (s3.compareTo("00") < 0 || s3.compareTo("59") > 0) {
        return false;
      }
    }
    else {
      String as[] = {
          "23", "59", "59"};
      StringTokenizer stringtokenizer = new StringTokenizer(s, "-:");
      for (int j = 0; stringtokenizer.hasMoreTokens(); j++) {
        String s4 = stringtokenizer.nextToken();
        if (s4.length() != 2) {
          return false;
        }
        if (s4.compareTo("00") < 0 || s4.compareTo(as[j]) > 0) {
          return false;
        }
      }

    }

    return true;
  }

  public static final boolean isAsciiStr(String s) {
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) > '\177') {
        return false;
      }
    }
    return true;
  }

  public static boolean isChineseStr(String s) {
    for (int index = 0; index < s.length(); index++) {
      if (s.charAt(index) < 256) {
        return false;
      }
    }
    return true;
  }

  public static final boolean isLeapYear(int i) {
    if (i % 400 == 0) {
      return true;
    }
    if (i % 100 == 0) {
      return false;
    }
    return i % 4 == 0;
  }

  public static boolean dataCheck(String srcBuffer, String picture) {

    StringBuffer srcbuffer = new StringBuffer(srcBuffer);
    int i;
    char tmpchar;

    for (i = 0; i < srcbuffer.length(); i++) {
      tmpchar = srcbuffer.charAt(i);
      if (tmpchar >= '0' && tmpchar <= '9') {
        tmpchar = '9';
      }
      else {
        tmpchar = Character.toLowerCase(tmpchar);
        if ( (tmpchar >= 97 && tmpchar <= 122)) {
          tmpchar = 'x';

        }
      }
      if (picture.indexOf(tmpchar) == -1) {
        return false;
      }
    }
    return true;
  }

}
