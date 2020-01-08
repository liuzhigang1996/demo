package com.neturbo.set.tags;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

public class DataTag extends TagSupport {

    private static final long serialVersionUID = -6317765443166483662L;

    private String name;

    private String rb;

    private String format;

    private String defaultvalue;

    private String from;

    private String type;

    private String maxlen;

    private String pattern;

    private String escapechar;

    private String db;

    public DataTag() {
    }

    public int doStartTag() {
        try {

            HttpSession session = pageContext.getSession();
            name = TagUtils.runActionMethod(name, session);

            // 获得输出
            JspWriter out = pageContext.getOut();
            // 从session获得语言种类
            Locale locale = (Locale) session.getAttribute("Locale$Of$Neturbo");
            NTBUser user = ((NTBUser) session.getAttribute(
                    "UserObject$Of$Neturbo"));
            if(locale == null){
                if (user != null) {
                    locale = user.getLanguage();
                }
            }
            if(locale == null){
                locale = Config.getDefaultLocale();
            }

            Object valueObj = null;

            if (type != null && type.equals("GLOBAL")) {
                if (name.equals("@ACTION")) {
                        //toDo
                }
            } else {
                HashMap parameters = (HashMap) session
                                     .getAttribute("Parameters$Of$Neturbo");
                HashMap resultData = (HashMap) session
                                     .getAttribute("ResultData$Of$Neturbo");

                if (from == null) {
                    // 从session获得已提交的参数数据
                    if (parameters != null) {
                        valueObj = parameters.get(name);
                    }
                    // 从session获得结果数据
                    if (valueObj == null) {
                        if (resultData != null) {
                            valueObj = resultData.get(name);
                        }
                    }
                } else {
                    if (from.toUpperCase().equals("PARAMETERS")) {
                        valueObj = parameters.get(name);
                    }
                    if (from.toUpperCase().equals("RESULTDATA")) {
                        if (resultData != null) {
                            valueObj = resultData.get(name);
                        }
                    }
                }
            }

            // 如果数据值为null，则等于缺省值
            if (valueObj == null) {
                valueObj = defaultvalue;
            }

            if (valueObj != null) {
                String value = "";
                if (valueObj instanceof Date) {
                    value = DateTime.Millis2DateTime(((Date) valueObj)
                            .getTime());
                } else if (valueObj.getClass().isArray()) {
                    Object[] valueObjArray = (Object[]) valueObj;
                    if (valueObjArray.length > 0) {
                        value = valueObjArray[0].toString().trim();
                    }
                } else {
                    value = valueObj.toString().trim();
                }

                // 如果resourceBundle存在，则翻译字串
                if (rb != null && !rb.trim().equals("")) {
                    RBFactory rbList = RBFactory.getInstance(rb, locale
                            .toString());
                    value = rbList.getString(value);
                }

                // 如果resourceBundle存在，则翻译字串
                if (db != null && !db.trim().equals("")) {
                    DBRCFactory rbList = DBRCFactory.getInstance(db);
                    rbList.setArgs(user);
                    value = rbList.getString(value);
                }

                // 如果format存在，则格式化字串
                if (format != null && !format.trim().equals("")) {
                    value = Format.formatData(value, format, pattern ,locale.toString());
                }

                int iLen = Utils.nullEmpty2Zero(maxlen);
                if (iLen > 0 && iLen < value.length()) {
                    value = value.substring(0, iLen) + "...";
                }

                if (escapechar != null
                    && ( escapechar.toUpperCase().equals("YES")
                    || escapechar.toUpperCase().equals("TRUE"))) {
                    value = EscapeChar.forHTMLTag(value);
                }

                // 写入JSP
                out.print(value);
            }
        } catch (Exception e) {
            Log.warn("Custom Tag Process error (Data)", e);
        }
        return (SKIP_BODY);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRb() {
        return rb;
    }

    public void setRb(String rb) {
        this.rb = rb;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDefaultvalue() {
        return defaultvalue;
    }

    public void setDefaultvalue(String defaultvalue) {
        this.defaultvalue = defaultvalue;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaxlen() {
        return maxlen;
    }

    public void setMaxlen(String maxlen) {
        this.maxlen = maxlen;
    }

    public String getPattern() {
        return pattern;
    }

    public String getEscapechar() {
        return escapechar;
    }

    public String getDb() {
        return db;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setEscapechar(String escapechar) {
        this.escapechar = escapechar;
    }

    public void setDb(String db) {
        this.db = db;
    }
}
