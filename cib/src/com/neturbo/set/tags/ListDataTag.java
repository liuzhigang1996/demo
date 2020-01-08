package com.neturbo.set.tags;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

public class ListDataTag extends TagSupport {

    private static final long serialVersionUID = 6147985832518385173L;

    private String name;

    private String inlevel;

    private String rb;

    private String rbs;

    private String format;

    private String defaultvalue;

    private String maxlen;

    private String pattern;

    private String escapechar;

    private String db;

    public ListDataTag() {
    }

    public int doStartTag() {
        try {
            // ������
            JspWriter out = pageContext.getOut();
            // ��session�����������
            HttpSession session = pageContext.getSession();
            Locale locale = (Locale) session.getAttribute("Locale$Of$Neturbo");
            NTBUser user = ((NTBUser) session.getAttribute(
                    "UserObject$Of$Neturbo"));
            if (locale == null) {
                if (user != null) {
                    locale = user.getLanguage();
                }
            }
            if (locale == null) {
                locale = Config.getDefaultLocale();
            }

            Object valueObj = null;
            // ��ø��б�
            ListTag parent = (ListTag) TagSupport.findAncestorWithClass(this,
                    ListTag.class);

            // ���ָ�����б���ѭ���ϲ�
            if (inlevel != null) {
                while (parent != null
                       && !parent.getLevel().equals(this.getInlevel())) {
                    ListTag parent_tmp = (ListTag) TagSupport
                                         .findAncestorWithClass(parent, ListTag.class);
                    if (parent_tmp != null) {
                        parent = parent_tmp;
                    } else {
                        Log.warn("No list(" + this.getInlevel()
                                 + ") exist when processing list Tag");
                        parent = null;
                        break;
                    }
                }
            }

            if (parent != null) {
                // �Ӹ���ListTag�л�ȡ��ǰ������
                Object rowObj = parent.getRow();
                if (rowObj == null) {
                    return (SKIP_BODY);
                }
                // �ӵ�ǰ��������ȡ��ָ��ֵ
                if (name != null) {
                    if (name.toUpperCase().equals("@INDEX")) {
                        valueObj = String.valueOf(parent.getIndex());
                    } else {
                        valueObj = ((HashMap) rowObj).get(name);
                    }
                } else {
                    valueObj = rowObj;
                }
            } else {
                Log.warn("No list exist when processing listdata Tag");
            }

            // �������ֵΪnull�������ȱʡֵ
            if (valueObj == null) {
                valueObj = defaultvalue;
            }

            if (valueObj != null) {
                String value = "";
                if (valueObj instanceof Date) {
                    value = DateTime.Millis2DateTime(((Date) valueObj)
                            .getTime());
                } else {
                    value = valueObj.toString().trim();
                }


                // ���resourceBundle���ڣ������ִ�
                if (rb != null && !rb.trim().equals("")) {
                    RBFactory rbList = RBFactory.getInstance(rb, locale
                            .toString());
                    value = rbList.getString(value);
                }
                // hjs: ��չrb, �������, ���Ÿ���
                if (rbs != null && !rbs.trim().equals("")) {
                    RBFactory rbList = RBFactory.getInstance(rbs, locale
                            .toString());
                    String[] s = value.split(",");
                    value = "";
                    for(int i=0; i<s.length; i++) {
                    	value += rbList.getString(s[i]) + "<br>";
                    }
                    if (!value.equals("")) {
                        value = value.substring(0, value.length()-4);
                    }
                }

                // ���resourceBundle���ڣ������ִ�
                if (db != null && !db.trim().equals("")) {
                    Log.debug("List Data : name=" + name +", db=" + db);
                    DBRCFactory rbList = DBRCFactory.getInstance(db);
                    if (rbList != null) {
                        rbList.setArgs(user);
                        value = rbList.getString(value);
                    }
                }

                // ���format���ڣ����ʽ���ִ�
                if (format != null && !format.trim().equals("")) {
                    value = Format.formatData(value, format, pattern,locale.toString());//mod by linrui for mul-language
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

                // д��JSP
                out.print(value);
            }
        } catch (Exception e) {
            Log.warn("Custom Tag Process error (ListData)", e);
        }
        return (SKIP_BODY);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInlevel() {
        return inlevel;
    }

    public void setInlevel(String inlevel) {
        this.inlevel = inlevel;
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

	public String getRbs() {
		return rbs;
	}

	public void setRbs(String rbs) {
		this.rbs = rbs;
	}

}
