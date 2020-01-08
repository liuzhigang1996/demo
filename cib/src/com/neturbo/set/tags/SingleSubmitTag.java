package com.neturbo.set.tags;

import javax.servlet.jsp.*;
import javax.servlet.http.*;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

public class SingleSubmitTag
    extends TagSupport {

  public SingleSubmitTag() {
  }

  public int doStartTag() {
    try {
      
      JspWriter out = pageContext.getOut();
      //String tokenStr = SubmitToken.getToken();
      String hiddenContent = "<input type='hidden' name='SubmitAttack' value='"+ SubmitToken.getToken() +"'>";
      out.print(hiddenContent);
      /*add by linrui 20190620*/
      /*HttpSession session = pageContext.getSession();
      if (session != null) {
          session.setAttribute("ResultData$Of$TokenSubmit", SubmitToken.getToken());
      }*/
      //end
      
    }
    catch (Exception e) {
      Log.warn( "Custom Tag Process error (singlesubmit)", e);
    }
    return (SKIP_BODY);
  }
}
