package com.neturbo.set.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;
import com.neturbo.set.exception.*;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyContent;

public class MessagesTag extends BodyTagSupport {
    private String cols;
    private String width;
    private String align;
    private String form;
    private static String msgResource = Config.getProperty("MessageResources");
    public MessagesTag() {
    }

    public int doStartTag() {
        try {
            JspWriter out = pageContext.getOut();
            HttpSession session = pageContext.getSession();
            //add by linrui 20180514
            ServletRequest request = pageContext.getRequest();
            Locale locale = (Locale) session.getAttribute("Locale$Of$Neturbo");
            NTBUser user = ((NTBUser) session.getAttribute(
                    "UserObject$Of$Neturbo"));
            if (locale == null) {
                if (user != null) {
                    locale = user.getLanguage();
                }
            }
            if (locale == null) {
                locale = Config.getDefaultLocale();
            }

            if (cols == null || cols.trim().equals("")) {
                cols = "2";
            }
            if (width == null || width.trim().equals("")) {
                width = "100%";
            }
            if (align == null || align.trim().equals("")) {
                align = "center";
            }

            out.println(
                    "<table width='" + width +
                    "' border='0' cellspacing='0' cellpadding='0' align='" +
                    align + "'>");
            out.println("<tr>");
            out.println(
                    "<td><div id='errMsg' name='errMsg' class='errorbox' style='display: none;'></div></td>");
            out.println("</tr>");
            out.println("</table>");

            out.println(
                    "<table width='" + width +
                    "' border='0' cellspacing='0' cellpadding='0' align='" +
                    align + "'>");
            out.println("<tr>");
            out.println(
                    "<td><div id='warnMsg' name='warnMsg' class='messagebox' style='display: none;'></div></td>");
            out.println("</tr>");
            out.println("</table>");

            out.println("<SCRIPT language=JavaScript>chkValidationErrorCol=" +
                        cols +
                        ";</SCRIPT>");

            out.println(
                    "<table width='" + width +
                    "' border='0' cellspacing='0' cellpadding='0' align='" +
                    align + "'>");
            out.println("<tr>");
            out.println(
                    "<td><div id='infoMsg' name='infoMsg' class='messagebox' style='display: none;'></div></td>");
            out.println("</tr>");
            out.println("</table>");

            out.println("<SCRIPT language=JavaScript>chkValidationErrorCol=" +
                        cols +
                        ";</SCRIPT>");

            out.println("<SCRIPT language=JavaScript>");
            out.println("errors = new Array();");
            out.println("errorNameList = new Array();");
            out.println("errorLabelList = new Array();");
            out.println("errorLayerList = new Array();");
            out.println("errorArrayIndex = new Array();");
            out.println("errorIndex = 0;");

            out.println("warns = new Array();");
            out.println("warnNameList = new Array();");
            out.println("warnLabelList = new Array();");
            out.println("warnLayerList = new Array();");
            out.println("warnArrayIndex = new Array();");
            out.println("warnIndex = 0;");

            out.println("infos = new Array();");
            out.println("infoNameList = new Array();");
            out.println("infoLabelList = new Array();");
            out.println("infoLayerList = new Array();");
            out.println("infoArrayIndex = new Array();");
            out.println("infoIndex = 0;");

            boolean errorFlag = false;
            boolean warnFlag = false;
            boolean informationFlag = false;
            boolean warnMessageFlag = false;

            NTBException errData =
                    (NTBException) session.getAttribute("ErrorData$Of$Neturbo");
            //add by linrui 20180514
            if(null==errData)        
             errData = (NTBException) request.getAttribute("ErrorData$Of$Neturbo");
            //end
            if (errData != null) {
                NTBErrorArray errArray = errData.getErrorArray();
                boolean showErrArrayFlag = false;

                if (errArray != null) {
                    for (int i = 0; i < errArray.size(); i++) {
                        NTBError fieldError = (NTBError) errArray.getError(i);
                        String fieldName = fieldError.getField();
                        String errCode = fieldError.getErrorCode();
                        String errLabel = fieldError.getLabel();

                        String errStr = "";
                        if (errCode != null) {
                            LoadRBTag parent = (LoadRBTag)
                                               findAncestorWithClass(this,
                                    LoadRBTag.class);
                            RBFactory rbCurrentPage = null;
                            if (parent != null) {
                                rbCurrentPage = parent.getRbFactory();
                                errStr = rbCurrentPage.getString(errCode.
                                        toString(), null);
                            }

                            //RBFactory rbErrMsg = RBFactory.getInstance(msgResource, locale.toString());
                            if (errStr == null) {
                            	if (errData instanceof NTBHostException) {
                                    DBRCFactory dbHostMsg = DBRCFactory.getInstance("hostErrMsg");
                                    if (dbHostMsg != null) {
                                        dbHostMsg.setArgs(user);//add by linrui 20180505
                                        errStr = dbHostMsg.getString(errCode);
                                        if(!errCode.equals(errStr)){
                                            errStr = errCode + " - " + errStr;
                                        }
                                    }
                            	} else {
                            		// add by hjs 2006-12-06
                                    RBFactory rbErrMsg = RBFactory.getInstance(msgResource, locale.toString());
                                    if (rbErrMsg != null) {
                                    	errStr = rbErrMsg.getString(errCode);
                                    }
                                    Object[] parameters = errData.getParameters();
                                    if (parameters != null) {
                                        for (int j = 0; j < parameters.length; j++) {
                                        	errStr = Utils.replaceStr(errStr,
                                                    "[%" + String.valueOf(j + 1) +
                                                    "]",
                                                    Utils.null2Empty(parameters[j]));
                                        }
                                    }

                            	}
                            }

                            if (errStr == null) {
                                errStr = errCode.toString();
                            }
                            out.println("errors[errorIndex] = \"" + errStr +
                                        "\";");
                            out.println("errorNameList[errorIndex] = \"" +
                                        Utils.null2Empty(fieldName) +
                                        "\";");
                            out.println("errorLabelList[errorIndex] = \"" +
                                        Utils.null2Empty(errLabel) +
                                        "\";");
                            out.println("errorLayerList[errorIndex] = '-1';");
                            out.println("errorArrayIndex[errorIndex] = 0;");
                            out.println("errorIndex++;");

                            //errArray.removeError(errCode, fieldName);
                            showErrArrayFlag = true;
                            errorFlag = true;
                        }
                    }
                }

                String errCode = errData.getErrorCode();
                String errMsg = "";
                if (errCode != null && !showErrArrayFlag) {
                    String errLabel = "";
                    LoadRBTag parent = (LoadRBTag) findAncestorWithClass(this,
                            LoadRBTag.class);
                    RBFactory rbCurrentPage = null;
                    if (parent != null) {
                        rbCurrentPage = parent.getRbFactory();
                        errMsg = rbCurrentPage.getString(errCode, null);
                    }

                    RBFactory rbErrMsg =
                            RBFactory.getInstance(msgResource, locale.toString());
                    if (errMsg == null || errMsg.equals(errCode)) {
                        errMsg = rbErrMsg.getString(errCode);
                    }
                    Object[] parameters = errData.getParameters();
                    if (parameters != null) {
                        for (int i = 0; i < parameters.length; i++) {
                            errMsg = Utils.replaceStr(errMsg,
                                    "[%" + String.valueOf(i + 1) +
                                    "]",
                                    Utils.null2Empty(parameters[i]));
                        }
                    }

                    out.println("errors[errorIndex] = \"" + errMsg + "\";");
                    out.println("errorNameList[errorIndex] = '';");
                    out.println("errorLabelList[errorIndex] = '';");
                    out.println("errorLayerList[errorIndex] = '-1';");
                    out.println("errorArrayIndex[errorIndex] = 0;");
                    out.println("errorIndex++;");
                    errorFlag = true;

                    session.removeAttribute("ErrorData$Of$Neturbo");
                }
            }

            /*NTBException warningData =
                    (NTBException) session.getAttribute("WarningData$Of$Neturbo");*/
            NTBException warningData =
            	(NTBException) request.getAttribute("WarningData$Of$Neturbo");//mod by linrui 20180518
            if(warningData ==null)
            	warningData = (NTBException) session.getAttribute("WarningData$Of$Neturbo");
            if (warningData != null) {

                String warningCode = warningData.getErrorCode();
                RBFactory rbErrMsg = RBFactory.getInstance(msgResource, locale.toString());
                String warnStr = rbErrMsg.getString(warningCode);
                if (warnStr == null) {
                    warnStr = warningCode;
                }

                out.println("warns[warnIndex] = \"" + warnStr + "\";");
                out.println("warnNameList[warnIndex] = '';");
                out.println("warnLabelList[warnIndex] = '';");
                out.println("warnLayerList[warnIndex] = '-1';");
                out.println("warnArrayIndex[warnIndex] = 0;");
                out.println("warnIndex++;");

                session.removeAttribute("WarningData$Of$Neturbo");
                warnFlag = true;
            }

            /*List MessageData =
                    (List) session.getAttribute("MessageData$Of$Neturbo");
                    */
            List MessageData = (List) request.getAttribute("MessageData$Of$Neturbo");
            if(null == MessageData)
            MessageData = (List) session.getAttribute("MessageData$Of$Neturbo");//add by linrui 20190823
            
            if(MessageData!=null){
            	int j=0;
	            for(int i=0; i<MessageData.size(); i++){
	            	String messageCode = (String) MessageData.get(i);
		            if (messageCode != null) {
		            	/*
		            	 * modify by hjs 2006-11-20
		
		                RBFactory rbErrMsg = RBFactory.getInstance("message", locale.toString());
		                String messageStr = rbErrMsg.getString(messageCode);
		                if (messageStr == null) {
		                    messageStr = messageCode;
		                }
		            	*/
		
		                out.println("infos[infoIndex] = \"" + messageCode + "\";");
		                out.println("infoNameList[infoIndex] = '';");
		                out.println("infoLabelList[infoIndex] = '';");
		                out.println("infoLayerList[infoIndex] = '-1';");
		                out.println("infoArrayIndex[infoIndex] = 0;");
		                out.println("infoIndex++;"); 
		                //out.println("alert(infoIndex+' infos='+infos);");
		                j++;
		            }
	            }
	            session.removeAttribute("MessageData$Of$Neturbo");
	            if(j>0){
	            	informationFlag = true;
	            }
            }
            
            List WarningMessageData = (List) request.getAttribute("WarningMessageData$Of$Neturbo");
            if(null == WarningMessageData)
            	WarningMessageData = (List) session.getAttribute("WarningMessageData$Of$Neturbo");
            
            if (WarningMessageData != null) {


            	 out.println("warns[warnIndex] = \"" + WarningMessageData + "\";");
                 out.println("warnNameList[warnIndex] = '';");
                 out.println("warnLabelList[warnIndex] = '';");
                 out.println("warnLayerList[warnIndex] = '-1';");
                 out.println("warnArrayIndex[warnIndex] = 0;");
                 out.println("warnIndex++;");

                session.removeAttribute("WarningMessageData$Of$Neturbo");
                warnMessageFlag = true;
            }
            
            if (errorFlag) {
            	out.println("chkDisplayErrorMessageWithMarkerByLanguage('" + form +"','"+locale.toString() + "');");
                out.println("window.location.hash = 'errMsg';");
            }
            if (warnFlag) {
            	out.println("chkDisplayWarnMessageWithMarkerByLanguage('" + form +"','"+locale.toString() + "');");
                out.println("window.location.hash = 'warnMsg';");
            }
            if (informationFlag) {
            	out.println("chkDisplayInfoMessageWithMarkerByLanguage('" + form +"','"+locale.toString() + "');");
                out.println("window.location.hash = 'infoMsg';");
            }
            
            if (warnMessageFlag) {
            	out.println("chkDisplayWarnNoCatchMessageWithMarkerByLanguage('" + form +"','"+locale.toString() + "');");
                out.println("window.location.hash = 'warnMsg';");
            }
            
            
            out.println("</SCRIPT>");

        } catch (Exception e) {
            Log.warn("Custom Tag Process error (Messages)", e);
        }
        return EVAL_BODY_TAG;
    }

    public int doAfterBody() throws JspException {
        try {
            BodyContent body = getBodyContent();
            JspWriter out = body.getEnclosingWriter();
            out.print(body.getString());
            body.clearBody();
        } catch (Exception e) {
            Log.warn("Custom Tag Process doAfterBody error (Messages)", e);
        }
        return SKIP_BODY;
    }

    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

}
