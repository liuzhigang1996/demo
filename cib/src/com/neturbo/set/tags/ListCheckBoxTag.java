package com.neturbo.set.tags;

import javax.servlet.jsp.*;
import javax.servlet.http.*;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.*;
import java.util.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

import javax.servlet.jsp.tagext.TagSupport;

public class ListCheckBoxTag extends TagSupport {

    private String value;
    private String text;
    private String inlevel;
    private String name;
    public ListCheckBoxTag() {
    }

    public int doStartTag() {
        try {
            //������
            JspWriter out = pageContext.getOut();
            //��session�����������
            HttpSession session = pageContext.getSession();

            Object valueObj = null;
            Object textObj = null;
            //��ø��б�
            ListTag parent =
                    (ListTag) TagSupport.findAncestorWithClass(this, ListTag.class);

            //���ָ�����б���ѭ���ϲ�
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
                //�Ӹ���ListTag�л�ȡ��ǰ������
                Object rowObj = parent.getRow();
                if (rowObj == null) {
                    return (SKIP_BODY);
                }
                //�ӵ�ǰ��������ȡ��ָ��ֵ
                if (value != null) {
                    valueObj = ((HashMap) rowObj).get(value);
                } else {
                    valueObj = rowObj;
                }
                //�ӵ�ǰ��������ȡ��ָ��ֵ
                if (text != null) {
                    textObj = ((HashMap) rowObj).get(text);
                } else {
                    textObj = rowObj;
                }
            } else {
                Log.warn("No list exist when processing listseleted Tag");
            }

            String valueStr = Utils.null2EmptyWithTrim(valueObj);
            String textStr = Utils.null2Empty(textObj);

            boolean selectedFlag = false;
            Object selectedObject = null;
            //���������ָ����
            if (name != null) {

                //��session������ύ�Ĳ�������
                HashMap parameters =
                        (HashMap) session.getAttribute(
                                "ParameterValues$Of$Neturbo");
                HashMap resultData =
                        (HashMap) session.getAttribute("ResultData$Of$Neturbo");

                if (parameters != null) {
                    selectedObject = parameters.get(name);
                }

                if (selectedObject == null) {
                    //��session��ý������
                    if (resultData != null) {
                        selectedObject = resultData.get(name);
                    }
                }

                if (selectedObject != null) {
                    //���ָ����������
                    if (selectedObject.getClass().isArray()) {
                        Object[] keyvalueObjs = (Object[]) selectedObject;
                        for (int i = 0; i < keyvalueObjs.length; i++) {
                            String keyValue = keyvalueObjs[i].toString().trim();
                            if (valueStr.equals(keyValue)) {
                                //д��JSP
                                selectedFlag = true;
                                break;
                            }
                        }
                    } else { //���ָ�����ǵ�ֵ
                        String keyValue = selectedObject.toString().trim();
                        if (valueStr.equals(keyValue)) {
                            //д��JSP
                            selectedFlag = true;
                        }
                    }
                }
            }

            if (selectedFlag) {
                out.print("<input type='checkbox' name='" + name + "' value='" +
                          valueStr + "' checked>" + textStr);
            } else {
                out.print("<input type='checkbox' name='" + name + "' value='" +
                          valueStr + "'>" + textStr);
            }

        } catch (Exception e) {
            Log.warn("Custom Tag Process error (ListCheckBox)", e);
        }
        return (SKIP_BODY);

    }

    public String getInlevel() {
        return inlevel;
    }

    public void setInlevel(String inlevel) {
        this.inlevel = inlevel;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
