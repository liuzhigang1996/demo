package com.neturbo.set.xml;

import java.util.*;

import com.neturbo.set.utils.*;

/**
 * 此处插入类型描述。
 * 创建日期：(2003-8-31 13:06:28)
 * @author：Administrator
 */
public class XMLElement {
  public String Name;
  public String Text;
  public Map Attributes;
  public List Children;
  public XMLElement Parent;

  /**
   * XMLElement 构造子注解。
   */
  public XMLElement() {
  }

  /**
   * XMLElement 构造子注解。
   */
  public XMLElement(String name) {
    this.Name = name;
    Attributes = new HashMap();
    Children = new ArrayList();
    Text = "";
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-8-31 13:12:14)
   * @param attribName java.lang.String
   * @param attribValue java.lang.String
   */
  public void addAttribute(String attribName, String attribValue) {
    Attributes.put(attribName, attribValue);
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-8-31 20:11:57)
   * @param name java.lang.String
   * @param node com.neturbo.set.xml.XMLElement
   */
  public boolean addChild(XMLElement node) {
    return Children.add(node);
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-9-1 1:25:51)
   * @return com.neturbo.set.xml.XMLElement
   * @param name java.lang.String
   * @param attribute java.lang.String
   * @param value java.lang.String
   */
  public XMLElement findNodeByName(String name) {
    if (getName().equals(name)) {
      return this;
    }
    for (int i = 0; i < Children.size(); i++) {
      XMLElement child = (XMLElement) Children.get(i);
      XMLElement res = child.findNodeByName(name);
      if (res != null) {
        return res;
      }
    }
    return null;
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-9-1 1:25:51)
   * @return com.neturbo.set.xml.XMLElement
   * @param name java.lang.String
   * @param attribute java.lang.String
   * @param value java.lang.String
   */
  public XMLElement findNodeByAtrribute(String name, String attribute,
                                        String value) {
    String attrValue = getAttribute(attribute);
    if (getName().equals(name) && attrValue != null && attrValue.equals(value)) {
      return this;
    }
    for (int i = 0; i < Children.size(); i++) {
      XMLElement child = (XMLElement) Children.get(i);
      XMLElement res = child.findNodeByAtrribute(name, attribute, value);
      if (res != null) {
        return res;
      }
    }
    return null;
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-9-1 1:25:51)
   * @return com.neturbo.set.xml.XMLElement
   * @param name java.lang.String
   * @param attribute java.lang.String
   * @param value java.lang.String
   */
  public XMLElement findNodeByInAtrribute(String name, String attribute,
                                          String value) {
    String attrValue = getAttribute(attribute);
    if (getName().equals(name) && attrValue != null) {
      if (attrValue.indexOf(value)>=0) {
        return this;
      }
    }

    for (int i = 0; i < Children.size(); i++) {
      XMLElement child = (XMLElement) Children.get(i);
      XMLElement res = child.findNodeByInAtrribute(name, attribute, value);
      if (res != null) {
        return res;
      }
    }
    return null;
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-9-1 1:47:46)
   * @return com.neturbo.set.xml.XMLElement
   * @param name java.lang.String
   * @param value java.lang.String
   */
  public XMLElement findNodeByText(String name, String value) {
    String textValue = getText();
    if (getName().equals(name) && textValue != null && textValue.equals(value)) {
      return this;
    }
    for (int i = 0; i < Children.size(); i++) {
      XMLElement child = (XMLElement) Children.get(i);
      XMLElement res = child.findNodeByText(name, value);
      if (res != null) {
        return res;
      }
    }
    return null;
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-9-1 1:47:46)
   * @return com.neturbo.set.xml.XMLElement
   * @param name java.lang.String
   * @param value java.lang.String
   */
  public XMLElement findNodeByChildText(String name, String childName,
                                        String value) {
    for (int i = 0; i < Children.size(); i++) {
      XMLElement child = (XMLElement) Children.get(i);
      String textValue = child.getText();
      if (this.getName().equals(name) && child.getName().equals(childName) &&
          textValue != null && textValue.equals(value)) {
        return this;
      }
      XMLElement res = child.findNodeByChildText(name, childName, value);
      if (res != null) {
        return res;
      }
    }
    return null;
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-9-1 1:31:38)
   * @return java.lang.String
   * @param attribute java.lang.String
   */
  public String getAttribute(String attribute) {
    return (String) Attributes.get(attribute);
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-8-31 13:09:52)
   * @return java.util.HashMap
   */
  public Map getAttributes() {
    return Attributes;
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-9-1 2:41:17)
   * @return com.neturbo.set.xml.XMLElement
   * @param index int
   */
  XMLElement getChildByIndex(int index) {
    Object res = Children.get(index);
    return res == null ? null : (XMLElement) res;
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-9-1 2:38:35)
   * @return com.neturbo.set.xml.XMLElement
   * @param Name java.lang.String
   */
  public XMLElement getChildByName(String name) {
    for (int i = 0; i < Children.size(); i++) {
      XMLElement child = (XMLElement) Children.get(i);
      if (child.getName().equals(name)) {
        return child;
      }
    }
    return null;
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-8-31 20:07:48)
   * @return int
   */
  public List getChildren() {
    return Children;
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-8-31 20:09:03)
   * @return java.lang.String
   */
  public java.lang.String getName() {
    return Name;
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-8-31 21:05:17)
   * @return com.neturbo.set.xml.XMLElement
   */
  public XMLElement getParent() {
    return Parent;
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-8-31 13:08:17)
   * @return java.lang.String
   */
  public java.lang.String getText() {
    return Text;
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-9-1 2:23:39)
   * @param node com.neturbo.set.xml.XMLElement
   */
  public boolean removeChild(XMLElement node) {
    return Children.remove(node);
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-8-31 13:16:12)
   * @param atrribName java.lang.String
   * @param atrribValue java.lang.String
   */
  public void setAttribute(String attribName, String attribValue) {
    Attributes.put(attribName, attribValue);
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-8-31 20:09:03)
   * @param newName java.lang.String
   */
  public void setName(java.lang.String newName) {
    Name = newName;
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-8-31 21:05:17)
   * @param newParent com.neturbo.set.xml.XMLElement
   */
  public void setParent(XMLElement newParent) {
    Parent = newParent;
  }

  /**
   * 此处插入方法描述。
   * 创建日期：(2003-8-31 13:08:17)
   * @param newText java.lang.String
   */
  public void setText(String newText) {
    Text = newText;
  }
}
