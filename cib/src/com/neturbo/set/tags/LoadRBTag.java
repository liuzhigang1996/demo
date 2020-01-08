package com.neturbo.set.tags;

import java.util.*;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

public class LoadRBTag
    extends BodyTagSupport {
  private RBFactory rbFactory;
  private String file;
  private String alias;

  public LoadRBTag() {
  }

  public int doStartTag() throws JspException {
    try {

      //从session获得语言种类
      HttpSession session = pageContext.getSession();
      Locale locale = (Locale) session.getAttribute("Locale$Of$Neturbo");
      NTBUser user = ((NTBUser) session.getAttribute(
              "UserObject$Of$Neturbo"));
      if(locale == null){
          if (user != null) {
              locale = user.getLanguage();
          }
      }
      if(locale == null){
          locale = Config.getDefaultLocale();
      }

      if (file != null && !file.trim().equals("")) {
        rbFactory =
            RBFactory.getInstance(file, locale.toString());
      }
      return EVAL_BODY_TAG;
  }
    catch (Exception e) {
      Log.warn("Custom Tag Process doStartTag error (loadRB)", e);
    }
    return SKIP_BODY;
  }

  public int doAfterBody() throws JspException {
      try{
          BodyContent body = getBodyContent();
          JspWriter out = body.getEnclosingWriter();
          out.print(body.getString());
          body.clearBody();
      }catch(Exception e){
          Log.warn("Custom Tag Process doAfterBody error (loadRB)", e);
      }
        return SKIP_BODY;

  }

  public String getFile() {
    return file;
  }

    public String getAlias() {
        return alias;
    }

    public RBFactory getRbFactory() {
        return rbFactory;
    }

    public void setFile(String file) {
    this.file = file;
  }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setRbFactory(RBFactory rbFactory) {
        this.rbFactory = rbFactory;
    }

}
