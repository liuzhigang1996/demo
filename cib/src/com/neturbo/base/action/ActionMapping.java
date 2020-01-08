/*
 * $Header: /home/cvs/jakarta-struts/src/share/org/apache/struts/action/ActionMapping.java,v 1.17.2.2 2001/11/04 07:43:50 martinc Exp $
 * $Revision: 1.17.2.2 $
 * $Date: 2001/11/04 07:43:50 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Struts", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package com.neturbo.base.action;

import java.io.*;

/**
 * An <strong>ActionMapping</strong> represents the information that the
 * controller servlet, <code>ActionServlet</code>, knows about the mapping
 * of a particular request to an instance of a particular action class.
 * The mapping is passed to the <code>perform()</code> method of the action
 * class itself, enabling access to this information directly.
 * <p>
 * An <code>ActionMapping</code> has the following minimal set of properties.
 * Additional properties can be added by a subclass, simply by
 * providing appropriate public "getter" and "setter" methods.
 * <ul>
 * <li><em>actionClass</em> - Fully qualified Java class name of the
 *     <code>Action</code> implementation class used by this mapping.  This
 *     property is required.  <em>DEPRECATED - use <code>type</code>
 *     instead</em>.
 * <li><strong>attribute</strong> - Name of the request-scope or
 *     session-scope attribute under which our form bean is accessed, if it
 *     is other than the bean's specified name.  Replaces the old
 *     <code>formAttribute</code> property.
 * <li><strong>forward</strong> - Context-relative path of the resource that
 *     should serve this request (via a call to
 *     <code>RequestDispatcher.forward()</code>) instead of instantiating the
 *     Action class specified by the <code>type</code> property.
 *     Exactly one of the <code>forward</code>, <code>include</code>, or
 *     <code>type</code> properties must be specified.
 * <li><strong>forwards</strong> - The set of ActionForwards locally
 *     associated with this mapping.
 * <li><strong>include</strong> - Context-relative path of the resource that
 *     should serve this request (via a call to
 *     <code>RequestDispatcher.include()</code>) instead of instantiating the
 *     Action class specified by the <code>type</code> property.
 *     Exactly one of the <code>forward</code>, <code>include</code>, or
 *     <code>type</code> properties must be specified.</li>
 * <li><strong>input</strong> - Context-relative path of the input form
 *     to which control should be returned if a validation error is
 *     encountered.  Replaces the old <code>inputForm</code> property.
 * <li><em>inputForm</em> - Context-relative path of the input form
 *     to which control should be returned if a validation error is
 *     encountered.  <em>DEPRECATED - use <code>input</code> instead</em>.
 * <li><strong>mappings</strong> - The <code>ActionMappings</code>
 *     collection of which we are a part.
 * <li><strong>name</strong> - Name of the form bean, if any, associated
 *     with this action.
 * <li><strong>parameter</strong> - General purpose configuration parameter
 *     that can be used to pass extra information to the <code>Action</code>
 *     selected by this <code>ActionMapping</code>.
 * <li><strong>path</strong> - Request URI path used to select this mapping.
 *     If extension mapping is used for the controller servlet, the extension
 *     will be stripped before comparisons against this value are made.
 * <li><strong>prefix</strong> - Prefix used to match request parameter
 *     names to form bean property names, if any.  Replaces the old
 *     <code>formPrefix</code> property.
 * <li><strong>scope</strong> - Identifier of the scope ("request" or
 *     "session" within which the form bean, if any, associated with this
 *     action will be created.  Replaces the old <code>formScope</code>
 *     attribute.
 * <li><strong>suffix</strong> - Suffix used to match request parameter
 *     names when populating the properties of our <code>ActionForm</code>
 *     bean (if any).  Replaces the old <code>formSuffix</code> property.
 * <li><strong>type</strong> - Fully qualified Java class name of the
 *     <code>Action</code> implementation class used by this mapping.
 *     Replaces the old <code>actionClass</code> property.  Exactly one of
 *     the <code>forward</code>, <code>include</code>, or <code>type</code>
 *     properties must be specified.
 * <li><strong>unknown</strong> - Set to <code>true</code> if this action
 *     should be configured as the default for this application, to handle
 *     all requests not handled by another action.  Only one action can be
 *     defined as a default within a single application.
 * <li><strong>validate</strong> - Set to <code>true</code> if the
 *     <code>validate()</code> method of the form bean (if any) associated
 *     with this mapping should be called.
 * </ul>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.17.2.2 $ $Date: 2001/11/04 07:43:50 $
 */

public class ActionMapping
    implements Serializable {

  // ----------------------------------------------------- Instance Variables

  /**
   * The name of the request-scope or session-scope attribute under which
   * our form bean, if any, will be created.
   */

  /**
   * The context relative path of the servlet or JSP resource (to be called
   * via <code>RequestDispatcher.forward()</code>) that will process this
   * request, rather than instantiating and calling the Action class that is
   * specified by the <code>type</code> attribute.
   */
  protected String forward = null;

  /**
   * The set of ActionForward objects associated with this mapping.
   */
  protected ActionForwards forwards = new ActionForwards();

  /**
   * The context relative path of the servlet or JSP resource (to be called
   * via <code>RequestDispatcher.include()</code>) that will process this
   * request, rather than instantiating and calling the Action class that is
   * specified by the <code>type</code> attribute.
   */
  protected String include = null;

  /**
   * The context-relative path of the input form to which control should
   * be returned if a validation error is encountered.
   */
  protected String input = null;

  /**
   * The initialized <code>Action</code> instance for this mapping.
   */
  protected Action instance = null;

  /**
   * The <code>ActionMappings</code> collection of which we are a part.
   */
  protected ActionMappings mappings = null;

  /**
   * The fully qualified class name of the <code>MultipartRequestHandler</code>
       * implementation class used to process multipart request data for this mapping
   */
  protected String multipartClass;

  /**
   * The name of the form bean, if any, associated with this action.
   */
  protected String form = null;

  /**
   * General purpose configuration parameter for this mapping.
   */
  protected String parameter = null;

  /**
   * The context-relative path of the submitted request, starting with a
   * "/" character, and without the filename extension (if any), that is
   * mapped to this action.
   */
  protected String name = null;

  /**
   * The parameter name prefix used to select parameters for this action.
   */
  protected String prefix = "";

  /**
   * The identifier of the scope ("request" or "session") under which the
   * form bean associated with this mapping, if any, should be created.
   */
  //缺省值置request，减少 FormBean 的存留时间。
  protected String scope = "request";

  /**
   * The parameter name suffix used to select parameters for this action.
   */
  protected String suffix = "";

  /**
   * The fully qualified Java class name of the <code>Action</code>
   * implementation class to be used to process requests for this mapping.
   */
  protected String bean = null;

  /**
   * Added by nabai 2004-01-12
   */
  /**
   * Should this action be the default for this application?
   */
  protected boolean defaultAction = false;

  /**
   * Should the validate() method of our form bean be called?
   */
  protected boolean validate = true;

    private String permission;
  protected boolean syslog = false;

  //add by panwen 20050312,for indicate when to clearTxnSession
  protected String clearTxnSession;

  private String txnSession;

  // ------------------------------------------------------------- Properties

  /**
   * Return the attribute name for our form bean.
   */

  /**
   * Set the attribute name for our form bean.
   *
   * @param attribute The new attribute name
   */

  /**
   * Return the forward path for this mapping.
   */
  public String getForward() {

    return (this.forward);

  }

  /**
   * Set the forward path for this mapping.
   *
   * @param forward The forward path for this mapping
   */
  public void setForward(String forward) {

    this.forward = forward;

  }

  /**
   * Return the include path for this mapping.
   */
  public String getInclude() {

    return (this.include);

  }

  /**
   * Set the include path for this mapping.
   *
   * @param include The include path for this mapping
   */
  public void setInclude(String include) {

    this.include = include;

  }

  /**
   * Return the input form path for this mapping.
   */
  public String getInput() {

    return (this.input);

  }

  /**
   * Set the input form path for this mapping.
   *
   * @param input The new input form path
   */
  public void setInput(String input) {

    this.input = input;

  }

  /**
   * Return the <code>ActionMappings</code> collection of which
   * we are a part.
   */
  public ActionMappings getMappings() {

    return (this.mappings);

  }

  /**
   * Get the name of the class used to handle multipart request data
   *
       * @return A fully qualified java class name representing the implementation of
   *         MultipartRequestHandler to use.
   */
  public String getMultipartClass() {
    return multipartClass;
  }

  /**
   * Set the <code>ActionMappings</code> collection of which
   * we are a part.
   *
   * @param mappings The new ActionMappings collection
   */
  public void setMappings(ActionMappings mappings) {

    this.mappings = mappings;

  }

  /**
   * Set the name of the class used to handle multipart request data
   *
   * @param multipartClass The fully qualified class name representing the
   *        MultipartRequestHandler class to use.  If <code>null</code>,
   *        the global class specified in "web.xml" will be used.
   */
  public void setMultipartClass(String multipartClass) {
    this.multipartClass = multipartClass;
  }

  /**
   * Return the name of the form bean for this mapping.
   */
  public String getForm() {
    return form;

  }

  /**
   * Set the name of the form bean for this mapping.
   *
   * @param name The new name
   */
  public void setForm(String form) {
    this.form = form;

  }

  /**
   * Return the general purpose configuation parameter for this mapping.
   */
  public String getParameter() {

    return (this.parameter);

  }

  /**
   * Set the general purpose configuration parameter for this mapping.
   *
   * @param parameter The new configuration parameter
   */
  public void setParameter(String parameter) {

    this.parameter = parameter;

  }

  /**
   * Return the request URI path used to select this mapping.
   */
  public String getName() {
    return name;

  }

  /**
   * Set the request URI path used to select this mapping.
   *
   * @param path The new request URI path
   */
  public void setName(String name) {
    this.name = "/" + name;

  }

  /**
   * Return the parameter name prefix for this mapping.
   */
  public String getPrefix() {

    return (this.prefix);

  }

  /**
   * Set the parameter name prefix for this mapping.
   *
   * @param prefix The new parameter name prefix
   */

  /**
   * Return the attribute scope for this mapping.
   */
  public String getScope() {

    return (this.scope);

  }

  /**
   * Set the attribute scope for this mapping.
   *
   * @param scope The new attribute scope
   */

  /**
   * Return the parameter name suffix for this mapping.
   */
  public String getSuffix() {

    return (this.suffix);

  }

  /**
   * Set the parameter name suffix for this mapping.
   *
   * @param suffix The new parameter name suffix
   */

  /**
   * Return the fully qualified Action class name.
   */
  public String getBean() {
    return bean;

  }

  /**
   * Set the fully qualified Action class name.
   *
   * @param type The new class name
   */
  public void setBean(String bean) {
    this.bean = bean;

  }

  /**
   * Return the fully qualified Action class name.
   */

  /**
   * Set the fully qualified Action class name.
   *
   * @param type The new class name
   */

  /**
   * Return the unknown flag for this mapping.
   */
  public boolean getDefaultAction() {

    return (this.defaultAction);

  }

  /**
   * Set the unknown flag for this mapping.
   *
   * @param unknown The new unknown flag
   */
  public void setDefaultAction(boolean defaultAction) {
    this.defaultAction = defaultAction;

  }

  /**
   * Return the validate flag for this mapping.
   */
  public boolean getValidate() {

    return (this.validate);

  }

  // --------------------------------------------------------- Public Methods

  /**
   * Add a new <code>ActionForward</code> associated with this mapping.
   *
   * @param forward The ActionForward to be added
   */
  public void addForward(ActionForward forward) {

    forwards.addForward(forward);

  }

  /**
   * Create and return an initialized instance of our form class.  If
   * instantiation fails for any reason, <code>null</code> is returned.
   *
   * @deprecated Creation of ActionForm instances is now the responsibility
   *  of the controller servlet
   */
  public ActionForm createFormInstance() {

    // Look up the Java class name to be instantiated
    ActionFormBean formBean =
        getMappings().getServlet().findFormBean(getForm());
    if (formBean == null) {
      return (null);
    }

    // Instantiate and return an instance of this class
    try {
      Class clazz = Class.forName(formBean.getBean());
      return ( (ActionForm) clazz.newInstance());
    }
    catch (Throwable t) {
      return (null);
    }

  }

  /**
   * Return the <code>ActionForward</code> with the specified name,
   * if any; otherwise return <code>null</code>.  If there is no locally
   * defined forwarding for the specified name, but a global forwards
   * collection has been associated with this mapping, the global
   * collection will also be searched before returning.
   *
   * @param name Name of the forward entry to be returned
   */
  public ActionForward findForward(String name) {

    // First, check our locally defined forwards
    ActionForward forward = forwards.findForward(name);
    if (forward != null) {
      return (forward);
    }

    // Second, check the globally defined forwards
    ActionMappings mappings = getMappings();
    if (mappings == null) {
      return (null);
    }
    ActionServlet servlet = mappings.getServlet();
    if (servlet == null) {
      return (null);
    }
    else {
      return (servlet.findForward(name));
    }

  }

  /**
   * Return the logical names of all locally defined forwards for this
   * mapping.  If there are no such forwards, a zero-length array
   * is returned.
   */
  public String[] findForwards() {

    return (forwards.findForwards());

  }



  /**
   * Remove a <code>ActionForward</code> associated with this mapping.
   *
   * @param forward The ActionForward to be removed
   */
  public void removeForward(ActionForward forward) {

    forwards.removeForward(forward);

  }

  /**
   * Return a String version of this mapping.
   */
  public String toString() {

    StringBuffer sb = new StringBuffer("ActionMapping[path=");
    sb.append(name);
    if (include != null) {
      sb.append(", include=");
      sb.append(include);
    }
    if (bean != null) {
      sb.append(", bean=");
      sb.append(bean);
    }
    sb.append("]");
    return (sb.toString());

  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getPermission() {

        return permission;
    }

  public void setPermission(String permission) {

        this.permission = permission;
    }

  // 判断是否记录系统日志
  public boolean getSyslog() {
    return syslog;
  }

  public void setSyslog(boolean syslog) {
    this.syslog = syslog;
  }

  //add by panwen 20050312
  public String getClearTxnSession() {
    return clearTxnSession;
  }

  //add by panwen 20050312
  public void setClearTxnSession(String string) {
    clearTxnSession = string;
  }

  public String getTxnSession() {
    return txnSession;
  }

  public void setTxnSession(String txnSession) {
    this.txnSession = txnSession;
  }

}
