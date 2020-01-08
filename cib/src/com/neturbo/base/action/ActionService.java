package com.neturbo.base.action;

import java.util.*;
import java.rmi.Remote.*;
import javax.servlet.http.*;
import javax.xml.rpc.server.*;
import javax.xml.rpc.handler.soap.*;
import javax.xml.soap.*;
import javax.xml.rpc.handler.MessageContext;
import java.security.Principal;

import com.neturbo.set.core.*;
import com.neturbo.set.xml.*;
import com.neturbo.set.exception.*;
import java.io.*;

public class ActionService
    implements ServiceLifecycle {

  //服务端点上下文
  private ServletEndpointContext serviceContext;

  //Action的集
  protected static HashMap actions = null;

  //Form的集
  protected static ActionFormBeans formBeans = null;

  //Forward的集
  protected static ActionForwards forwards = null;

  //Mapping的集
  protected static ActionMappings mappings = null;

  /**
   *ServiceLifecycle方法：初始化服务端点，或者要使用的资源。
   */
  public void init(java.lang.Object context) {
    serviceContext = (ServletEndpointContext) context;
  }

  /**
   *ServiceLifecycle方法：销毁服务端点实例。
   */
  public void destroy() {
    this.serviceContext = null;
  }

  /**
   *初始化方法
   */
  public ActionService() {
  }

  /**
   *WebService公开调用的方法
   */
  public String process(String actionName, String paraStr) {

    //返回的XML
    String retXML = "";

    try {
      // 查找适合的Mapping
      if (actionName.charAt(0) != '/') {
        actionName = "/" + actionName;
      }
      ActionMapping mapping = processMapping(actionName);
      if (mapping == null) {
        Log.info(
                  " No Action available for request URI " + actionName);
        throw new NTBException("err.sys.GeneralError");
      }

      //处理数据
      HashMap parameters = processParameters(paraStr);

      // 初始化Form并且校验
      ActionForm formInstance = processActionForm(mapping);
      if (!processValidate(mapping, formInstance, parameters)) {
        return retXML;
      }

      // 处理直接Forward的情况
      if (processForward(mapping) != null) {
        return retXML;
      }

      // 处理直接Include的情况
      if (processInclude(mapping) != null) {
        return retXML;
      }

      // 初始化Action
      Action actionInstance = processActionCreate(mapping);
      if (actionInstance == null) {
        Log.info(
                  " Action create error for name " + mapping.getName());
        return retXML;
      }

      // 处理Action的Perform1
      HashMap retData =
          processActionPerform(actionInstance, mapping, formInstance);

      return processRetData(retData);
    }
    catch (Exception e) {
      return "";
    }
  }

  //查找适合的Mapping
  protected ActionMapping processMapping(String path) {

    ActionMapping mapping = mappings.findMapping(path);
    if (mapping == null) {
      mapping = mappings.getDefaultAction(null);
    }
    return (mapping);
  }

  //初始化Form
  protected ActionForm processActionForm(ActionMapping mapping) {

    // Is there a form bean associated with this mapping?
    String name = mapping.getForm();

    // Determine the form bean class that we expect to use
    String className = null;
    ActionFormBean formBean = formBeans.findFormBean(name);
    if (formBean == null) {
      formBean = formBeans.getDefaultForm();
      name = formBean.getName();
    }
    if (formBean != null) {
      className = formBean.getBean();
    }
    else {
      return (null);
    }

    ActionForm instance = null;
    try {
      Class clazz = Class.forName(className);
      instance = (ActionForm) clazz.newInstance();
    }
    catch (Throwable t) {
      Log.fatal(
                "Error creating ActionForm instance of class '" +
                className + "'");
    }
    return (instance);
  }

  protected boolean processValidate(ActionMapping mapping,
                                    ActionForm formInstance, HashMap parameters) {

    if (formInstance == null) {
      return (true);
    }

    // Has validation been turned off on this mapping?
    if (!mapping.getValidate()) {
      return (true);
    }

    // Call the validate() method of our ActionForm bean
    HashMap retData = formInstance.validate1(mapping, serviceContext,
                                             parameters);
    if ( (retData == null) || retData.isEmpty()) {
      return (true);
    }

    // Has an input form been specified for this mapping?
    String uri = mapping.getInput();
    if (uri == null) {
      Log.warn( "No input form, but validation returned errors");
    }

    return (false);

  }

  protected String processForward(ActionMapping mapping) {

    String forward = mapping.getForward();
    if (forward == null) {
      return (forward);
    }
    return ("");

  }

  protected String processInclude(ActionMapping mapping) {

    String include = mapping.getInclude();
    if (include == null) {
      return (include);
    }
    return ("");

  }

  protected Action processActionCreate(ActionMapping mapping) {

    // Acquire the Action instance we will be using
    String actionClass = mapping.getBean();
    Action actionInstance = (Action) actions.get(actionClass);
    if (actionInstance == null) {
      synchronized (actions) {
        actionInstance = (Action) actions.get(actionClass);
        if (actionInstance != null) {
          return (actionInstance);
        }
        try {
          Class clazz = Class.forName(actionClass);
          actionInstance = (Action) clazz.newInstance();
          actions.put(actionClass, actionInstance);
        }
        catch (Throwable t) {
          Log.fatal(
                    "Error creating Action instance for action '" +
                    mapping.getName() + "', class name '" +
                    actionClass + "'");
          return (null);
        }
      }
    }
    return (actionInstance);

  }

  protected HashMap processActionPerform(Action action,
                                         ActionMapping mapping,
                                         ActionForm formInstance) throws
      Exception {

    HashMap retData =
        action.perform1(mapping, formInstance, serviceContext);
    return (retData);

  }

  protected HashMap processParameters(String paraXML) {
    try {
      StringBufferInputStream paraIS = new StringBufferInputStream(paraXML);

      XMLParser doc = XMLFactory.getParser();
      doc.setInput(paraIS);
      doc.unMarshal();
      XMLElement root = doc.getRootElement();
      XMLElement node = root.findNodeByAtrribute("map", "name",
                                                 "Parameters$Of$Neturbo");
      HashMap paraMap = (HashMap) parseParameters(node);
      return paraMap;
    }
    catch (Exception e) {

    }
    return null;
  }

  protected String processRetData(HashMap retData) {
    try {
      ByteArrayOutputStream retOS = new ByteArrayOutputStream();
      XMLWriter doc = XMLFactory.getWriter();
      doc.setOutput(retOS);
      XMLElement root = formatRetData("ReturnData$Of$Neturbo", retData);
      doc.setRootElement(root);
      doc.Marshal();
      String retStr = new String(retOS.toByteArray());
      return retStr;
    }
    catch (Exception e) {

    }
    return null;
  }

  private Object parseParameters(XMLElement node) {
    if (node == null) {
      return null;
    }
    String name = node.getName();
    if (name.equals("field")) {
      return node.getText();
    }
    List children = node.getChildren();
    if (name.equals("array")) {
      ArrayList paraArray = new ArrayList();
      for (int i = 0; i < children.size(); i++) {
        XMLElement childNode = (XMLElement) children.get(i);
        paraArray.add(parseParameters(childNode));
      }
      return paraArray;
    }
    HashMap paraMap = new HashMap();
    for (int i = 0; i < children.size(); i++) {
      XMLElement childNode = (XMLElement) children.get(i);
      paraMap.put(childNode.getAttribute("name"), parseParameters(childNode));
    }
    return paraMap;
  }

  private XMLElement formatRetData(String name, Object nodeObj) {
    if (nodeObj == null) {
      XMLElement xmlNode = new XMLElement("null");
      xmlNode.setAttribute("name", name);
      return xmlNode;
    }
    Class nodeClass = nodeObj.getClass();
    if (nodeClass.getName().equals("java.lang.String")) {
      XMLElement xmlNode = new XMLElement("field");
      xmlNode.setAttribute("name", name);
      xmlNode.setText( (String) nodeObj);
      return xmlNode;
    }
    if (nodeClass.getName().equals("java.util.ArrayList")) {
      ArrayList paraArray = (ArrayList) nodeObj;
      XMLElement xmlNode = new XMLElement("array");
      xmlNode.setAttribute("name", name);
      for (int i = 0; i < paraArray.size(); i++) {
        Object childNodeObj = paraArray.get(i);
        xmlNode.addChild(formatRetData("", childNodeObj));
      }
      return xmlNode;
    }
    if (nodeClass.getName().equals("java.util.HashMap")) {
      HashMap paraMap = (HashMap) nodeObj;
      XMLElement xmlNode = new XMLElement("map");
      xmlNode.setAttribute("name", name);
      Object[] keys = paraMap.keySet().toArray();
      for (int i = 0; i < keys.length; i++) {
        String key = (String) keys[i];
        Object childNodeObj = paraMap.get(key);
        xmlNode.addChild(formatRetData(key, childNodeObj));
      }
      return xmlNode;
    }

    XMLElement xmlNode = new XMLElement("other");
    xmlNode.setAttribute("name", name);
    return xmlNode;
  }

  public static void setActions(HashMap newActions) {
    actions = newActions;
  }

  public static void setFormBeans(ActionFormBeans newFormBeans) {
    formBeans = newFormBeans;
  }

  public static void setForwards(ActionForwards newForwards) {
    forwards = newForwards;
  }

  public static void setMappings(ActionMappings newMappings) {
    mappings = newMappings;
  }
}
