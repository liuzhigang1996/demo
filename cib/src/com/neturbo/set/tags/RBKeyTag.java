package com.neturbo.set.tags;

import javax.servlet.jsp.*;
import javax.servlet.http.*;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.*;
import java.util.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

import javax.servlet.jsp.tagext.TagSupport;

public class RBKeyTag extends TagSupport {
    private String inrblist;
    private String inrb;
    public RBKeyTag() {
    }

    public int doStartTag() {
        try {
            //获得输出
            JspWriter out = pageContext.getOut();

            //从父类ListTag中获取当前行数据
            RBListTag parent = (RBListTag) findAncestorWithClass(this,
                    RBListTag.class);
            //如果指定父列表则循环上查
            if (inrb != null) {
                while (parent != null
                       && !parent.getFile().equals(inrb)) {
                    RBListTag parent_tmp =
                            (RBListTag) TagSupport.findAncestorWithClass(
                                    parent,
                                    RBListTag.class);
                    if (parent_tmp != null) {
                        parent = parent_tmp;
                    } else {
                        Log.warn(
                                "No list("
                                + this.getInrb()
                                + ") exist when processing list Tag");
                        parent = null;
                        break;
                    }
                }
            }
            String[] rowData = parent.getRow();

            //写入JSP
            out.print(rowData[0]);
        } catch (Exception e) {
            Log.warn("Custom Tag Process error (RBKeyTag)", e);
        }
        return (SKIP_BODY);

    }

    public String getInrblist() {
        return inrblist;
    }

    public void setInrblist(String inrblist) {
        this.inrblist = inrblist;
    }

    public String getInrb() {
        return inrb;
    }

    public void setInrb(String inrb) {
        this.inrb = inrb;
    }
}
