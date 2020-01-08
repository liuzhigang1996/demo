package com.neturbo.set.tags;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

public class IfTag
    extends BodyTagSupport {

  private String name;
  private String field;
  private boolean ifTrue = false;
  private String value;
  private String condition;
  private String type;
  public IfTag() {
  }

  public int doStartTag() throws JspException {
    try {
      ifTrue = false;
      String keyValue = null;
      String compareValue = null;

      HttpSession session = pageContext.getSession();

      HashMap parameters = (HashMap) session.getAttribute(
          "Parameters$Of$Neturbo");
      HashMap resultData = (HashMap) session.getAttribute(
          "ResultData$Of$Neturbo");

      //从session获得已提交的参数数据
      if (parameters != null) {
        keyValue = (String) parameters.get(name);
        compareValue = (String) parameters.get(field);
      }
      //从session获得结果数据
      if (keyValue == null) {
        if (resultData != null) {
          keyValue = (String) resultData.get(name);
        }
      }
      if (compareValue == null) {
        if (resultData != null) {
          compareValue = (String) resultData.get(field);
        }
      }

      if (type != null && type.equals("GLOBAL")) {
        if (name.equals("@ACTION")) {
            //toDo
        }
      }

      if (value != null) {
        compareValue = value;
      }

      //从resultData中取出指定的列表
      if (keyValue != null && compareValue != null && condition != null) {
        String orCompareValue = "||" + compareValue + "||";
        String orKeyValue = "||" + keyValue + "||";
        if (condition.toUpperCase().equals("EQUALS")) {
          if (orCompareValue.indexOf(orKeyValue) >= 0) {
            ifTrue = true;
            return EVAL_BODY_TAG;
          }
        }
        if (condition.toUpperCase().equals("NOTEQUALS")) {
          if (! (orCompareValue.indexOf(orKeyValue) >= 0)) {
            ifTrue = true;
            return EVAL_BODY_TAG;
          }
        }
      }
    }

    catch (Exception e) {
      Log.warn("Custom Tag Process doStartTag error (if)", e);
    }
    return SKIP_BODY;
  }

  public int doAfterBody() throws JspException {
    try {
      if (ifTrue) {
        BodyContent body = getBodyContent();
        JspWriter out = body.getEnclosingWriter();
        out.print(body.getString());
        body.clearBody();
      }
    }
    catch (Exception e) {
      Log.warn("Custom Tag Process doAfterBody error (if)", e);
    }
    return SKIP_BODY;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}
