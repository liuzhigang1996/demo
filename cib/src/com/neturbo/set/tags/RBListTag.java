package com.neturbo.set.tags;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

public class RBListTag extends BodyTagSupport {
    private ArrayList rows;
    int index;
    int size;
    private String file;
    private String[] row;
    private String string;
    private String db;
    private String arg;

    public RBListTag() {
    }

    public int doStartTag() throws JspException {
        try {

            //从session获得语言种类
            HttpSession session = pageContext.getSession();
            Locale locale = (Locale) session.getAttribute("Locale$Of$Neturbo");
            NTBUser user = ((NTBUser) session.getAttribute("UserObject$Of$Neturbo"));
            if (locale == null) {
                if (user != null) {
                    locale = user.getLanguage();
                }
            }
            if (locale == null) {
                locale = Config.getDefaultLocale();
            }
            if (user != null) {
            	user.setLanguage(locale);
            }
            if (file != null && !file.trim().equals("")) {

                RBFactory rbList =
                        RBFactory.getInstance(file, locale.toString());
                //初始化列表
                rows = rbList.getStringArray();
            } else if (db != null && !db.trim().equals("")) {
                Object argObj = user;
                if (arg != null) {
                    String[] args = Utils.splitStr(arg, ";");
                    Object[] argArray = new Object[args.length + 1];
                    argArray[0] = user;
                    for (int i = 0; i < args.length; i++) {
                        if (args[i].startsWith("@LISTDATA.")) {
                            Object argItem = null;
                            String argName = args[i].substring(10);
                            // 获得父列表
                            ListTag parent = (ListTag) TagSupport.
                                             findAncestorWithClass(this,
                                    ListTag.class);

                            if (parent != null) {
                                // 从父类ListTag中获取当前行数据
                                Object rowObj = parent.getRow();
                                if (rowObj == null) {
                                    return (SKIP_BODY);
                                }
                                // 从当前行数据中取出指定值
                                if (argName != null) {
                                    if (argName.toUpperCase().equals("@INDEX")) {
                                        argItem = String.valueOf(parent.getIndex());
                                    } else {
                                        argItem = ((HashMap) rowObj).get(argName);
                                    }
                                } else {
                                    argItem = rowObj;
                                }
                            } else {
                                Log.warn(
                                        "No list exist when processing rblist Tag of arg=" +
                                        arg);
                            }
                            argArray[i+1] = argItem;
                        } else if (args[i].startsWith("@DATA.")) {
                            Object argItem = null;
                            HashMap parameters = (HashMap) session
                                                 .getAttribute(
                                    "Parameters$Of$Neturbo");
                            HashMap resultData = (HashMap) session
                                                 .getAttribute(
                                    "ResultData$Of$Neturbo");

                            String argName = args[i].substring(6);
                            // 从session获得已提交的参数数据
                            if (parameters != null) {
                                argItem = parameters.get(argName);
                            }
                            // 从session获得结果数据
                            if (argItem == null) {
                                if (resultData != null) {
                                    argItem = resultData.get(argName);
                                }
                            }
                            argArray[i+1] = argItem;
                        } else {
                            argArray[i+1] = args[i];
                        }
                    }
                    argObj = argArray;
                }
                DBRCFactory rbList =
                        DBRCFactory.getInstance(db);
                rbList.setArgs(argObj);
                //初始化列表
                rows = rbList.getStringArray();
            } else if (string != null && !string.trim().equals("")) {
                rows = new ArrayList();
                StrTokenizer st = new StrTokenizer(string, "||");
                while (st.hasMoreTokens()) {
                    String[] row = Utils.splitStr((String) st.nextToken(), "=");
                    rows.add(row);
                }
            }

            size = rows.size();
            //如果列表长度为零，则跳过
            if (size > 0) {
                //取第一行
                index = 0;
                row = (String[]) rows.get(index);
                return EVAL_BODY_TAG;
            }
        } catch (Exception e) {
            Log.warn("Custom Tag Process doStartTag error (RBList)", e);
        }
        return SKIP_BODY;
    }

    public int doAfterBody() throws JspException {
        if (index < size) {
            try {
                BodyContent body = getBodyContent();
                JspWriter out = body.getEnclosingWriter();
                out.print(body.getString());
                body.clearBody();
                index++;
                if (index < size) {
                    row = (String[]) rows.get(index);
                }
            } catch (IOException ioe) {
                Log.warn("Custom Tag Process doAfterBody error (RBListTag): ",
                         ioe);
            }
            return EVAL_BODY_TAG;
        } else {
            return SKIP_BODY;
        }

    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String[] getRow() {
        return row;
    }

    public void setRow(String[] row) {
        this.row = row;
    }

    public String getString() {
        return string;
    }

    public String getDb() {
        return db;
    }

    public String getArg() {
        return arg;
    }

    public void setString(String string) {
        this.string = string;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }
}
