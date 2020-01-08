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
      //获得输出
      JspWriter out = pageContext.getOut();
      //从session获得语言种类
      HttpSession session = pageContext.getSession();

      //从父类RBListTag中获取当前行数据
      RBListTag parent = (RBListTag) findAncestorWithClass(this, RBListTag.class);
      //如果指定父列表则循环上查
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
      //如果条件是指定域
      if (name != null) {

        //从session获得已提交的参数数据
        HashMap parameters = (HashMap) session.getAttribute(
            "Parameters$Of$Neturbo");
        HashMap resultData = (HashMap) session.getAttribute(
            "ResultData$Of$Neturbo");

        if (parameters != null) {
          selectedObject = parameters.get(name);
        }

        if (selectedObject == null) {
          //从session获得结果数据
          if (resultData != null) {
            selectedObject = resultData.get(name);
          }
        }

        if (selectedObject != null) {
          //如果指定域是数组
          if (selectedObject.getClass().isArray()) {
            Object[] keyvalueObjs = (Object[]) selectedObject;
            for (int i = 0; i < keyvalueObjs.length; i++) {
              String keyValue = keyvalueObjs[i].toString().trim();
              if (value.equals(keyValue)) {
                //写入JSP
                selectedFlag = true;
                break;
              }
            }
          }
          else { //如果指定域是单值
            String keyValue = selectedObject.toString().trim();
            if (value.equals(keyValue)) {
              //写入JSP
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
