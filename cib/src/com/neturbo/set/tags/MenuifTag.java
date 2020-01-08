package com.neturbo.set.tags;

import javax.servlet.jsp.*;
import javax.servlet.http.*;
import javax.servlet.jsp.tagext.*;
import java.io.*;
import java.util.*;
import com.neturbo.set.xml.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

import javax.servlet.jsp.tagext.TagSupport;

public class MenuifTag extends BodyTagSupport {
    private boolean ifTrue = false;
    private String inlevel;
    private String property;
    private String mid;
    private String condition;
    private String value;
    private String field;
    public MenuifTag() {
    }

    public int doStartTag() {
        try {
            //获得输出
            JspWriter out = pageContext.getOut();
            //从session获得语言种类
            HttpSession session = pageContext.getSession();

            //查询父列表
            MenuTag parent = (MenuTag) TagSupport.findAncestorWithClass(this,
                    MenuTag.class);

            if (inlevel != null) {
                while (parent != null &&
                       !parent.getLevel().equals(this.getInlevel())) {
                    MenuTag parent_tmp = (MenuTag) TagSupport.
                                         findAncestorWithClass(parent,
                            MenuTag.class);
                    if (parent_tmp != null) {
                        parent = parent_tmp;
                    } else {
                        Log.warn(
                                "No Menugroup(" + this.getInlevel() +
                                ") exist when processing menuif Tag");
                        parent = null;
                        break;

                    }
                }
            }

            if (parent != null) {
                String keyValue = null;
                if (property.equals("@INDEX")) {
                    keyValue = String.valueOf(parent.getIndex() + 1);
                }else if(property.equals("@COUNT")){
                    keyValue = String.valueOf(parent.getCount() + 1);
                }else{
                    keyValue = parent.getElement().getAttribute(property);
                }
                String compareValue = null;

                HashMap parameters = (HashMap) session.getAttribute(
                        "Parameters$Of$Neturbo");
                HashMap resultData = (HashMap) session.getAttribute(
                        "ResultData$Of$Neturbo");

                //从session获得已提交的参数数据
                if (parameters != null) {
                    compareValue = (String) parameters.get(field);
                }
                //从session获得结果数据
                if (compareValue == null) {
                    if (resultData != null) {
                        compareValue = (String) resultData.get(field);
                    }
                }

                if (value != null) {
                    compareValue = value;
                }

                //从resultData中取出指定的列表
                if (keyValue != null && compareValue != null && condition != null) {
                    if (condition.toUpperCase().equals("EQUALS") &&
                        keyValue.equals(compareValue)) {
                        ifTrue = true;
                        return EVAL_BODY_TAG;
                    }
                    if (condition.toUpperCase().equals("NOTEQUALS") &&
                        !keyValue.equals(compareValue)) {
                        ifTrue = true;
                        return EVAL_BODY_TAG;
                    }
                }
            } else {
                Log.warn("No menu exist when processing menuif Tag");
            }
        } catch (Exception e) {
            Log.warn("Custom Tag Process error (menuif)", e);
        }
        return (SKIP_BODY);

    }

    public int doAfterBody() throws JspException {
        try {
            if (ifTrue) {
                BodyContent body = getBodyContent();
                JspWriter out = body.getEnclosingWriter();
                out.print(body.getString());
                body.clearBody();
            }
        } catch (Exception e) {
            Log.warn("Custom Tag Process doAfterBody error (if)", e);
        }
        return SKIP_BODY;
    }

    public String getInlevel() {
        return inlevel;
    }

    public void setInlevel(String inlevel) {
        this.inlevel = inlevel;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

}
