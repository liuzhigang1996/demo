package com.neturbo.set.tags;

import javax.servlet.jsp.*;
import javax.servlet.http.*;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.*;
import java.util.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

import javax.servlet.jsp.tagext.TagSupport;

public class RBSelectedTag extends TagSupport {
    private String inrb;
    private String equals;
    private String equalsvalue;
    private String output = "selected";
    public RBSelectedTag() {
    }

    public int doStartTag() {
        try {
            //������
            JspWriter out = pageContext.getOut();
            //��session�����������
            HttpSession session = pageContext.getSession();

            //�Ӹ���RBListTag�л�ȡ��ǰ������
            RBListTag parent = (RBListTag) findAncestorWithClass(this,
                    RBListTag.class);
            //���ָ�����б���ѭ���ϲ�
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

            String value = rowData[0];

            //��resultData��ȡ��ָ��ֵ
            String equalsfield = getEquals();
            Object compareValueObj = null;
            //���������ָ����
            if (equalsfield != null) {

                //��session������ύ�Ĳ�������
                HashMap parameters = (HashMap) session.getAttribute(
                        "ParameterValues$Of$Neturbo");
                HashMap resultData = (HashMap) session.getAttribute(
                        "ResultData$Of$Neturbo");

                if (parameters != null) {
                    compareValueObj = parameters.get(equalsfield);
                }

                if (compareValueObj == null) {
                    //��session��ý������
                    if (resultData != null) {
                        compareValueObj = resultData.get(equalsfield);
                    }
                }

                if (compareValueObj != null) {
                    //���ָ����������
                    if (compareValueObj.getClass().isArray()) {
                        Object[] compareValueObjs = (Object[]) compareValueObj;
                        for (int i = 0; i < compareValueObjs.length; i++) {
                            String compareValue = compareValueObjs[i].toString().
                                                  trim();
                            if (value.equals(compareValue)) {
                                //д��JSP
                                out.print(getOutput());
                                break;
                            }
                        }
                    } else { //���ָ�����ǵ�ֵ
                        String compareValue = compareValueObj.toString().trim();
                        if (value.equals(compareValue)) {
                            //д��JSP
                            out.print(getOutput());
                        }
                    }
                }
            }

            if (compareValueObj == null && equalsvalue != null) { //���������ָ��ֵ
                if (value.equals(equalsvalue)) {
                    //д��JSP
                    out.print(getOutput());
                }
            }
        } catch (Exception e) {
            Log.warn("Custom Tag Process error (RBSelected)", e);
        }
        return (SKIP_BODY);

    }

    public String getInrb() {
        return inrb;
    }

    public void setInrb(String inrb) {
        this.inrb = inrb;
    }

    public String getEquals() {
        return equals;
    }

    public void setEquals(String equals) {
        this.equals = equals;
    }

    public String getEqualsvalue() {
        return equalsvalue;
    }

    public void setEqualsvalue(String equalsvalue) {
        this.equalsvalue = equalsvalue;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
