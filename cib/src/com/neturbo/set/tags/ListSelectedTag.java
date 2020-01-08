package com.neturbo.set.tags;

import javax.servlet.jsp.*;
import javax.servlet.http.*;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.*;
import java.util.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

import javax.servlet.jsp.tagext.TagSupport;

public class ListSelectedTag
    extends TagSupport {
  private String equalsvalue;
  private String key;
  private String inlevel;
  private String output = "checked";
  private String equals;
  public ListSelectedTag() {
  }

  public int doStartTag() {
    try {
      //������
      JspWriter out = pageContext.getOut();
      //��session�����������
      HttpSession session = pageContext.getSession();

      Object valueObj = null;
      //��ø��б�
      ListTag parent =
          (ListTag) TagSupport.findAncestorWithClass(this, ListTag.class);

      //���ָ�����б���ѭ���ϲ�
      if (inlevel != null) {
        while (parent != null
               && !parent.getLevel().equals(this.getInlevel())) {
          ListTag parent_tmp =
              (ListTag) TagSupport.findAncestorWithClass(
              parent,
              ListTag.class);
          if (parent_tmp != null) {
            parent = parent_tmp;
          }
          else {
            Log.warn(
                "No list("
                + this.getInlevel()
                + ") exist when processing list Tag");
            parent = null;
            break;
          }
        }
      }

      if (parent != null) {
        //�Ӹ���ListTag�л�ȡ��ǰ������
        Object rowObj = parent.getRow();
        if (rowObj == null) {
            return (SKIP_BODY);
        }
        //�ӵ�ǰ��������ȡ��ָ��ֵ
        if (key != null) {
          valueObj = ( (HashMap) rowObj).get(key);
        }
        else {
          valueObj = rowObj;
        }
      }
      else {
        Log.warn("No list exist when processing listseleted Tag");
      }

      String value = Utils.null2EmptyWithTrim(valueObj);

      //��resultData��ȡ��ָ��ֵ
      String equalsfield = getEquals();
      Object compareValueObj = null;
      //���������ָ����
      if (equalsfield != null) {

        //��session������ύ�Ĳ�������
        HashMap parameters =
            (HashMap) session.getAttribute(
            "ParameterValues$Of$Neturbo");
        HashMap resultData =
            (HashMap) session.getAttribute("ResultData$Of$Neturbo");

        if (parameters != null) {
          compareValueObj = parameters.get(equalsfield);
        }

        if (compareValueObj == null) {
          //��session��ý������
          if (resultData != null) {
            compareValueObj = resultData.get(equalsfield);
          }
        }

        if (compareValueObj != null) {
          //���ָ����������
          if (compareValueObj.getClass().isArray()) {
            Object[] compareValueObjs = (Object[]) compareValueObj;
            for (int i = 0; i < compareValueObjs.length; i++) {
              String compareValue = compareValueObjs[i].toString().trim();
              if (value.equals(compareValue)) {
                //д��JSP
                out.print(getOutput());
                break;
              }
            }
          }
          else { //���ָ�����ǵ�ֵ
            String compareValue = compareValueObj.toString().trim();
            if (value.equals(compareValue)) {
              //д��JSP
              out.print(getOutput());
            }
          }
        }
      }

      if (compareValueObj == null && equalsvalue != null) { //���������ָ��ֵ
        if (value.equals(equalsvalue)) {
          //д��JSP
          out.print(getOutput());
        }
      }

    }
    catch (Exception e) {
      Log.warn("Custom Tag Process error (ListSelected)", e);
    }
    return (SKIP_BODY);

  }

  public String getInlevel() {
    return inlevel;
  }

  public void setInlevel(String inlevel) {
    this.inlevel = inlevel;
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
