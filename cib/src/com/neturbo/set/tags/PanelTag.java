package com.neturbo.set.tags;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

public class PanelTag
    extends BodyTagSupport {

  private String name;
  private String text;
  private String openOnLoad;
  private String from;
  public PanelTag() {
  }

  public int doStartTag() throws JspException {
    try {

      HttpSession session = pageContext.getSession();
      HashMap parameters = (HashMap) session.getAttribute(
          "Parameters$Of$Neturbo");
      HashMap resultData = (HashMap) session.getAttribute(
          "ResultData$Of$Neturbo");

      Object valueObj = null;
      if (from == null) {
        //从session获得已提交的参数数据
        if (parameters != null) {
          valueObj =
              parameters.get(name) == null
              ? null
              : parameters.get(name);
        }
        //从session获得结果数据
        if (valueObj == null) {
          if (resultData != null) {
            valueObj =
                resultData.get(name) == null
                ? null
                : resultData.get(name);
          }
        }
      }
      else {
        if (from.toUpperCase().equals("PARAMETERS")) {
          valueObj = (String) parameters.get(name);
        }
        if (from.toUpperCase().equals("RESULTDATA")) {
          if (resultData != null) {
            valueObj = (String) resultData.get(name);
          }
        }
      }

      String pnTitleClass;
      String pnShown;
      if (openOnLoad!=null&&openOnLoad.toUpperCase().equals("NO") &&
          openOnLoad.toUpperCase().equals("FALSE")) {
        pnTitleClass = "td_panel1_off";
        pnShown = "style='display: none'";
      }
      else {
        pnTitleClass = "td_panel1_on";
        pnShown = "";
      }
      //获得输出
      JspWriter out = pageContext.getOut();
      out.print("<table width='100%'> \n <tr> <td id='PANEL_HEAD_" + name +
              "' class='" + pnTitleClass +
                "'> \n ");
      out.print("<img src='/images/spacer.gif' width='12' height='12'  align='absmiddle' onclick=\"togglePanel('" +
                name + "');\"> \n &nbsp; ");
      out.print(text);
      out.print(" \n </td><tr><td class='td_panelbody1'><div name='PANEL_" + name + "' id='PANEL_" +
                name + "' " + pnShown + ">");

      return EVAL_BODY_TAG;
    }

    catch (Exception e) {
      Log.warn("Custom Tag Process doStartTag error (if)", e);
    }
    return SKIP_BODY;
  }

  public int doAfterBody() throws JspException {
    try {
      BodyContent body = getBodyContent();
      JspWriter out = body.getEnclosingWriter();
      out.print(body.getString());
      body.clearBody();
      out.print("</div></td></tr></table>");
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

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getOpenOnLoad() {
    return openOnLoad;
  }

  public void setOpenOnLoad(String openOnLoad) {
    this.openOnLoad = openOnLoad;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

}
