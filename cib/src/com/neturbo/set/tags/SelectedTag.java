package com.neturbo.set.tags;

import javax.servlet.jsp.*;
import javax.servlet.http.*;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.*;
import java.util.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

import javax.servlet.jsp.tagext.TagSupport;

public class SelectedTag
    extends TagSupport {
  private String equalsvalue;
  private String key;
  private String inlist;
  private String output = "checked";
  private String equals;
  public SelectedTag() {
  }

  public int doStartTag() {
    try {
      //������
      JspWriter out = pageContext.getOut();
      //��session�����������
      HttpSession session = pageContext.getSession();

      String keyValue = null;
      String compareValue = null;

      HashMap parameters = (HashMap) session.getAttribute(
          "Parameters$Of$Neturbo");
      HashMap resultData = (HashMap) session.getAttribute(
          "ResultData$Of$Neturbo");

      //��session������ύ�Ĳ�������
      if (parameters != null) {
        keyValue = (String) parameters.get(key);
        compareValue = (String) parameters.get(equals);
      }
      //��session��ý������
      if (keyValue == null) {
        if (resultData != null) {
          keyValue = (String) resultData.get(key);
        }
      }
      if (compareValue == null) {
        if (resultData != null) {
          compareValue = (String) resultData.get(equals);
        }
      }

      //��resultData��ȡ��ָ�����б�
      if (keyValue != null) {
        if (compareValue != null) {
          if (keyValue.equals(compareValue)) {
            //д��JSP
            out.print(getOutput());
          }
        }
        else {
          if (keyValue.equals(equalsvalue)) {
            //д��JSP
            out.print(getOutput());
          }
        }
      }

    }

    catch (Exception e) {
      Log.warn("Custom Tag Process error (ListSelected)", e);
    }
    return (SKIP_BODY);

  }

  public String getInlist() {
    return inlist;
  }

  public void setInlist(String inlist) {
    this.inlist = inlist;
  }

  public String getOutput() {
    return output;
  }

  public void setOutput(String output) {
    this.output = output;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getEqualsvalue() {
    return equalsvalue;
  }

  public void setEqualsvalue(String equalsvalue) {
    this.equalsvalue = equalsvalue;
  }

  public String getEquals() {
    return equals;
  }

  public void setEquals(String equals) {
    this.equals = equals;
  }
}
