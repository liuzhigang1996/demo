package com.neturbo.set.tags;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.neturbo.set.core.*;
import com.neturbo.set.xml.*;

public class MenuTag extends BodyTagSupport {
    private String mid;
    private ArrayList childs;
    private MenuTag parent;
    int index;
    int count;
    int size;
    private String inlevel;
    private Object child;
    private String level;
    private String type;
    private List userFunctionList;
    private com.neturbo.set.xml.XMLElement element;
    private com.neturbo.set.core.NTBUser user;

    public MenuTag() {
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Object getChild() {
        if (index < size) {
            child = (XMLElement) childs.get(index);
            return child;
        } else {
            return null;
        }
    }

    private void setChild(Object child) {
        this.child = child;
    }

    public int doStartTag() throws JspException {
        try {

            //从session获得语言种类
            HttpSession session = pageContext.getSession();
            user = ((NTBUser) session.getAttribute("UserObject$Of$Neturbo"));

            Object valueObj = null;
            XMLElement root = Config.getMenuXML();

            if (mid != null) {
                valueObj = root.findNodeByAtrribute("menu", "mid", mid);
            } else {
                //获得父列表
                parent = (MenuTag) TagSupport.findAncestorWithClass(this,
                        MenuTag.class);
                if (inlevel != null) {
                    while (parent != null &&
                           !parent.getLevel().equals(this.getInlevel())) {
                        MenuTag parent_tmp = (MenuTag) TagSupport.
                                             findAncestorWithClass(
                                parent,
                                MenuTag.class);
                        if (parent_tmp != null) {
                            parent = parent_tmp;
                        } else {
                            Log.warn(
                                    "No Menu(" + this.getInlevel() +
                                    ") exist when processing Menu Tag");
                            parent = null;
                            break;
                        }
                    }
                }

                if (parent != null) {
                    //从父列表中获得当前行数据
                    valueObj = parent.getChild();
                } else {
                    valueObj = root;
                }
            }

            if (valueObj != null) {
                element = (XMLElement) valueObj;
                if (type == null || type.equals(element.getAttribute("type"))) {
                    //初始化列表
                    childs = (ArrayList) element.getChildren();
                    userFunctionList = user.getFunctionList();
                    size = childs.size();
                    //取第一行
                    index = 0;
                    count = 0;
                }
                return EVAL_BODY_TAG;
            }
        }

        catch (Exception e) {
            Log.warn("Custom Tag Process doStartTag error (menu)", e);
        }
        return SKIP_BODY;
    }

    public int doAfterBody() throws JspException {
        try {
            boolean hasRight = false;
            //获得用户
            String menuType = element.getAttribute("type");
            if (menuType != null && menuType.equals("delimiter")) {
                hasRight = true;
            }
            if (element.findNodeByAtrribute("menu", "permission", "") != null) {
                hasRight = true;
            }
            if (userFunctionList != null) {
                for (int i = 0; i < userFunctionList.size(); i++) {
                    NTBPermission permission = (NTBPermission)
                                                userFunctionList.get(i);
                    String permId = permission.getPermissionResource();
                    if (element.findNodeByAtrribute("menu", "permission",
                            permId) != null) {
                        hasRight = true;
                        break;
                    }
                }
            }
            String className= element.getAttribute("class");
            if(className!= null){
                try{
                    Class clazz = Class.forName(className);
                    MenuObject object = (MenuObject) clazz.
                            newInstance();
                    object.setUser(user);
                    object.setXmlElement(element);
                    if (!object.isShowMenu()) {
                        hasRight = false;
                    }
                }catch(Exception e){
                    Log.error("Error creating menu class instance", e);
                    hasRight = false;
                }
            }
            BodyContent body = getBodyContent();
            if (hasRight &&
                (type == null || type.equals(element.getAttribute("type")))) {
                JspWriter out = body.getEnclosingWriter();
                out.print(body.getString());
                if (parent != null) {
                    int count1 = parent.getCount();
                    parent.setCount(++count1);
                }
            }
            body.clearBody();
        } catch (IOException ioe) {
            Log.warn(
                    "Custom Tag Process doAfterBody error (menu)", ioe);
        }
        if (parent != null) {
            int index1 = parent.getIndex();
            int size1 = parent.getSize();
            index1++;
            if (index1 < size1) {
                parent.setIndex(index1);
                element = (XMLElement) parent.getChild();
                if (type == null || type.equals(element.getAttribute("type"))) {
                    childs = (ArrayList) element.getChildren();
                    size = childs.size();
                    //取第一行
                    index = 0;
                    count = 0;
                }
                return EVAL_BODY_TAG;
            } else {
                index1 = 0;
                parent.setIndex(index1);
                element = (XMLElement) parent.getChild();
                if (type == null || type.equals(element.getAttribute("type"))) {
                    childs = (ArrayList) element.getChildren();
                    size = childs.size();
                    //取第一行
                    index = 0;
                    count = 0;
                }
            }
        }
        return SKIP_BODY;
    }

    public String getInlevel() {
        return inlevel;
    }

    public void setInlevel(String inlevel) {
        this.inlevel = inlevel;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public com.neturbo.set.xml.XMLElement getElement() {
        return element;
    }

    public void setElement(com.neturbo.set.xml.XMLElement element) {
        this.element = element;
    }

    public int getIndex() {
        return index;
    }

    public int getSize() {
        return size;
    }

    public int getCount() {
        return count;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
