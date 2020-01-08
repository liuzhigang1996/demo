/*
 * 在页面上显示提示信息。
 */
package com.neturbo.set.tags;

import java.util.*;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

/**
 * @author panwen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LabelTag extends TagSupport {

    private static boolean showDefaultValue = true;
    static {
        String showDefaultStr = Utils.null2Empty(Config.getProperty(
                "ShowDefaultValueInLabel")).toUpperCase();
        showDefaultValue = !showDefaultStr.equals("NO");
    }


    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException {
        // TODO Auto-generated method stub

        try {

            JspWriter out = pageContext.getOut();

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

            RBFactory rbFactory = null;
            if (rb == null) {
                //从父类ListTag中获取当前行数据
                LoadRBTag parent = (LoadRBTag) findAncestorWithClass(this,
                        LoadRBTag.class);
                if (parent != null) {
                    rbFactory = parent.getRbFactory();
                }
            } else {
                rbFactory =
                        RBFactory.getInstance(
                                rb,
                                locale.toString());
            }
            String value = null;
            if (rbFactory != null) {
                if (!showDefaultValue) {
                    defaultvalue = "null";
                }
                value = rbFactory.getString(name, defaultvalue);
            }

            out.print(value);
        } catch (Exception e) {
            Log.warn("Custom Tag Process error (label) ", e);
        }

        return TagSupport.SKIP_BODY;
    }

    private String name = null;
    private String rb;
    private String defaultvalue;

    /**
     * @return
     */
    public String getName() {

        return name;
    }

    public String getRb() {
        return rb;
    }

    public String getDefaultvalue() {

        return defaultvalue;
    }

    /**
     * @param string
     */
    public void setName(String name) {

        this.name = name;
    }

    public void setRb(String rb) {
        this.rb = rb;
    }

    public void setDefaultvalue(String defaultvalue) {

        this.defaultvalue = defaultvalue;
    }

    private void jbInit() throws Exception {
    }

}
