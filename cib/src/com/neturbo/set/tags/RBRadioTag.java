package com.neturbo.set.tags;

import javax.servlet.jsp.*;
import javax.servlet.http.*;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.*;
import java.util.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

import javax.servlet.jsp.tagext.TagSupport;

public class RBRadioTag
    extends TagSupport {
  private String name;
  private String inrb;
  public RBRadioTag() {
  }

  public int doStartTag() {
    try {
      //������
      JspWriter out = pageContext.getOut();
      //��session�����������
      HttpSession session = pageContext.getSession();

      //�Ӹ���RBListTag�л�ȡ��ǰ������
      RBListTag parent = (RBListTag) findAncestorWithClass(this, RBListTag.class);
      //���ָ�����б���ѭ���ϲ�
      if (inrb != null) {
              while (parent != null
                      && !parent.getFile().equals(inrb)) {
                      RBListTag parent_tmp =
                              (RBListTag) TagSupport.findAncestorWithClass(
                                      parent,
                                      RBListTag.class);
                      if (parent_tmp != null) {
                              parent = parent_tmp;
                      } else {
                              Log.warn(
                                      "No list("
                                              + this.getInrb()
                                              + ") exist when processing list Tag");
                              parent = null;
                              break;
                      }
              }
      }
      String[] rowData = parent.getRow();

      String value = rowData[0];
      String text = rowData[1];

      boolean selectedFlag = false;
      Object selectedObject = null;
      //���������ָ����
      if (name != null) {

        //��session������ύ�Ĳ�������
        HashMap parameters = (HashMap) session.getAttribute(
            "Parameters$Of$Neturbo");
        HashMap resultData = (HashMap) session.getAttribute(
            "ResultData$Of$Neturbo");

        if (parameters != null) {
          selectedObject = parameters.get(name);
        }

        if (selectedObject == null) {
          //��session��ý������
          if (resultData != null) {
            selectedObject = resultData.get(name);
          }
        }

        if (selectedObject != null) {
          //���ָ����������
          if (selectedObject.getClass().isArray()) {
            Object[] keyvalueObjs = (Object[]) selectedObject;
            for (int i = 0; i < keyvalueObjs.length; i++) {
              String keyValue = keyvalueObjs[i].toString().trim();
              if (value.equals(keyValue)) {
                //д��JSP
                selectedFlag = true;
                break;
              }
            }
          }
          else { //���ָ�����ǵ�ֵ
            String keyValue = selectedObject.toString().trim();
            if (value.equals(keyValue)) {
              //д��JSP
              selectedFlag = true;
            }
          }
        }
      }
      if (selectedFlag) {
        out.print("<input type='radio' name='" + name + "' value='"+value+"' checked>" + text);
      }
      else {
        out.print("<input type='radio' name='" + name + "' value='"+value+"'>" + text);
      }
    }
    catch (Exception e) {
      Log.warn("Custom Tag Process error (RBOption)", e);
    }
    return (SKIP_BODY);

  }


  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getInrb() {
    return inrb;
  }
  public void setInrb(String inrb) {
    this.inrb = inrb;
  }
}
