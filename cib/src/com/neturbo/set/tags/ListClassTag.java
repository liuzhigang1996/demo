package com.neturbo.set.tags;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

public class ListClassTag extends TagSupport {

    private String class2;
    private String class1;
    private String inlevel;
    private static int continueIndex = 0;
    public ListClassTag() {
    }

    public int doStartTag() throws JspException {
        try {

            // 获得输出
            JspWriter out = pageContext.getOut();
            //获得父列表
            ListTag parent =
                    (ListTag) TagSupport.findAncestorWithClass(this, ListTag.class);

            //如果指定父列表则循环上查
            boolean continueFlag = false;
            if (inlevel != null) {
                if (!inlevel.equals("@CONTINUE")) {
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
                } else {
                    continueFlag = true;
                }
            }

            int rowIndex = 0;
            if (continueFlag) {
                continueIndex++;
                rowIndex = continueIndex;
            } else if (parent != null) {
                rowIndex = parent.getIndex();
                continueIndex = rowIndex;
            }
            //从父类ListTag中获取当前行数
            if (rowIndex % 2 == 0) {
                out.print(class1);
            } else {
                out.print(class2);
            }

        } catch (Exception e) {
            Log.warn("Custom Tag Process doStartTag error (if)", e);
        }
        return SKIP_BODY;
    }

    public String getClass2() {

        return class2;
    }

    public void setClass2(String class2) {

        this.class2 = class2;
    }

    public String getClass1() {

        return class1;
    }

    public void setClass1(String class1) {

        this.class1 = class1;
    }

    public String getInlevel() {
        return inlevel;
    }

    public void setInlevel(String inlevel) {
        this.inlevel = inlevel;
    }

}
