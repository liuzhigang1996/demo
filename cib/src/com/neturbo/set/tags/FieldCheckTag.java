package com.neturbo.set.tags;

import java.util.*;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

public class FieldCheckTag extends BodyTagSupport {
    private String file;
    private String form;
    private String name;
    private static String contextRoot;
    static {
        contextRoot = Config.getProperty("AppContextRoot");
        //缺省为当前应用
        if (contextRoot == null) {
            contextRoot = "/cib";
        }
        if (contextRoot.endsWith("/")) {
            contextRoot = contextRoot.substring(0, contextRoot.length() - 1);
        }

    }

    public FieldCheckTag() {
    }

    public int doStartTag() {
        try {
            //获得输出
            JspWriter out = pageContext.getOut();
            //从session获得语言种类
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

            if (file != null) {
                try {
                    //从父类ListTag中获取当前行数据
                    LoadRBTag parent = (LoadRBTag) findAncestorWithClass(this,
                            LoadRBTag.class);
                    RBFactory rbFactory = null;
                    if (parent != null) {
                        rbFactory = parent.getRbFactory();
                    }
                    FieldChkXMLFactory.generate(locale.toString(),file, rbFactory);//mod by linrui for mul-language20171115
                    out.println(
                            "<SCRIPT language=JavaScript src='" + contextRoot +
                            "/javascript/temp/" +
//                          file +
                            file  + "_" + locale.toString()+//mod by linrui 20180519
                            ".js'></SCRIPT>");
                } catch (Exception e) {
                    Log.error(
                            "Could not generate javascript file, please check xml file");
                }
            }
        } catch (Exception e) {
            Log.warn("Custom Tag Process error (FieldCheck)", e);
        }
        return (EVAL_BODY_TAG);
    }

    public int doAfterBody() throws JspException {
        try {
            BodyContent body = getBodyContent();
            JspWriter out = body.getEnclosingWriter();
            out.print(body.getString());
            body.clearBody();
        } catch (Exception e) {
            Log.warn("Custom Tag Process doAfterBody error (FieldCheck)", e);
        }
        return SKIP_BODY;
    }

    public String getFile() {
        return file;
    }

    public String getForm() {
        return form;
    }

    public String getName() {
        return name;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public void setName(String name) {
        this.name = name;
    }
}
