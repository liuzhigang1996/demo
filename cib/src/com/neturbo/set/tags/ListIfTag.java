package com.neturbo.set.tags;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

public class ListIfTag extends BodyTagSupport {

    private String name;
    private String field;
    private boolean ifTrue = false;
    private String value;
    private String condition;
    private String inlevel;
    public ListIfTag() {
    }

    public int doStartTag() throws JspException {
        try {
            ifTrue = false;
            String keyValue = null;
            String compareValue = null;

            Object keyValueObj = null;
            Object compValueObj = null;
            //获得父列表
            ListTag parent =
                    (ListTag) TagSupport.findAncestorWithClass(this, ListTag.class);

            //如果指定父列表则循环上查
            if (inlevel != null) {
                while (parent != null
                       && !parent.getLevel().equals(this.getInlevel())) {
                    ListTag parent_tmp =
                            (ListTag) TagSupport.findAncestorWithClass(
                                    parent,
                                    ListTag.class);
                    if (parent_tmp != null) {
                        parent = parent_tmp;
                    } else {
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
                //从父类ListTag中获取当前行数据
                Object rowObj = parent.getRow();
                if (rowObj == null) {
                    return (SKIP_BODY);
                }
                //从当前行数据中取出指定值
                if (name != null) {
                    if (name.toUpperCase().equals("@INDEX")) {
                        keyValueObj = String.valueOf(parent.getIndex());
                    } else {
                        keyValueObj = ((HashMap) rowObj).get(name);
                    }
                } else {
                    keyValueObj = rowObj;
                }
                //获取比较值
                if (field != null) {
                    compValueObj = ((HashMap) rowObj).get(field);
                } else {
                    compValueObj = rowObj;
                }
            } else {
                Log.warn("No list exist when processing listseleted Tag");
            }

            keyValue = Utils.null2Empty(keyValueObj);
            compareValue = Utils.null2Empty(compValueObj);

            if (value != null) {
                compareValue = value;
            }

            //从resultData中取出指定的列表
            if (compareValue != null &&
                compareValue.toUpperCase().equals("[NULL]") && condition != null) {
                if (condition.toUpperCase().equals("EQUALS")) {
                    if (keyValue == null) {
                        ifTrue = true;
                        return EVAL_BODY_TAG;
                    }
                }
                if (condition.toUpperCase().equals("NOTEQUALS")) {
                    if (keyValue != null) {
                        ifTrue = true;
                        return EVAL_BODY_TAG;
                    }
                }
            }
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
                    if (!(orCompareValue.indexOf(orKeyValue) >= 0)) {
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
        } catch (Exception e) {
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

    public String getInlevel() {
        return inlevel;
    }

    public void setInlevel(String inlevel) {
        this.inlevel = inlevel;
    }

}
