package com.neturbo.set.core;

import java.util.*;
import java.rmi.Remote.*;
import javax.servlet.http.*;
import javax.xml.rpc.server.*;
import javax.xml.rpc.handler.soap.*;
import javax.xml.soap.*;
import javax.xml.rpc.handler.MessageContext;
import java.security.Principal;

import com.neturbo.set.xml.*;
import com.neturbo.set.exception.*;
import com.neturbo.set.utils.*;
import com.neturbo.base.upload.*;
import com.neturbo.base.action.*;

public class ActionFormA extends ActionForm {

    private Map parameters;
    private Map parameterValues;
    private Map attributes;
    private HttpServletRequest httpServletRequest;
    private FormFile uploadFile;
    private NTBUser user;

    public FormFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(FormFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getParameter(String key) {
        return (String) parameters.get(key);
    }

    public Map getParameters() {
        return parameters;
    }
    
    public void setParameters(Map para) {
        parameters = para;
    }
    
    public String getAttribute(String key) {
        return (String) attributes.get(key);
    }

    public Map getAttributes() {
        return attributes;
    }

    public ActionErrors validate(
            ActionMapping actionMapping,
            HttpServletRequest httpServletRequest) {

        String key;
        Object valueObj;

        try {

            //锟斤拷锟皆拷锟斤拷拇锟斤拷锟�
            HttpSession session = httpServletRequest.getSession();
            try {
                session.removeAttribute("ErrorData$Of$Neturbo");
            } catch (Exception e) {
            }
            //锟斤拷锟絉equest锟叫碉拷锟斤拷锟斤拷
            HashMap attributes = new HashMap();
            for (Enumeration enumeration =
                    httpServletRequest.getAttributeNames();
                    enumeration.hasMoreElements();
                    attributes.put(key, valueObj)) {
                key = (String) enumeration.nextElement();
                valueObj = httpServletRequest.getAttribute(key);
            }
            //锟斤拷锟街わ拷锟斤拷锟斤拷荩锟酵拷锟斤拷锟斤拷锟斤拷锟斤拷锟叫憋拷锟斤拷
            key = "javax.net.ssl.peer_certificates";
            valueObj = httpServletRequest.getAttribute(key);
            attributes.put(key, valueObj);

            //锟斤拷锟絉equest锟叫的诧拷锟斤拷Form锟斤拷锟斤拷锟斤拷锟斤拷锟経RL锟叫达拷牟锟斤拷锟�
            parameters = new HashMap();
            parameterValues = new HashMap();
            for (Enumeration enumeration =
                    httpServletRequest.getParameterNames();
                    enumeration.hasMoreElements();
                    ) {
                key = (String) enumeration.nextElement();
                String valueObj1 = httpServletRequest.getParameter(key);
                String[] valueObj2 = httpServletRequest.getParameterValues(key);

                valueObj = valueObj1;

                parameters.put(key, valueObj);

                if (valueObj2 != null) {
                    valueObj = valueObj2;
                    parameterValues.put(key, valueObj);
                }
            }

            //锟斤拷锟斤拷锟斤拷牟锟斤拷锟斤拷锟斤拷锟絉esultData锟叫ｏ拷锟皆憋拷InputTag锟斤拷锟斤拷
            session.setAttribute("Parameters$Of$Neturbo", parameters);
            session.setAttribute("ParameterValues$Of$Neturbo", parameterValues);

            //锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
            doValidate(actionMapping);
            //doValidateToken(actionMapping,httpServletRequest);//mod by linrui 20190620

        } catch (NTBException e) {
            //锟斤拷锟斤拷锟斤拷锟斤拷锟叫达拷锟絊ession锟叫ｏ拷锟斤拷InputError锟斤拷Tag锟斤拷锟斤拷示
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("ErrorData$Of$Neturbo", e);

            //写一锟斤拷锟秸达拷锟斤拷
            ActionErrors errors = new ActionErrors();
            errors.add("INPUTERROR", new ActionError(e.getErrorCode()));
            return errors;
        } catch (Exception e) {
            //锟斤拷录锟斤拷锟斤拷
            Log.error(
                    "System error when validating Servlet Form("
                    + actionMapping.getName()
                    + ")",
                    e);
            //锟斤拷锟斤拷锟斤拷锟斤拷锟叫达拷锟絊ession锟叫ｏ拷锟斤拷锟斤拷锟斤拷锟酵筹拷啤锟较低筹拷诓锟斤拷锟斤拷锟�
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("ErrorData$Of$Neturbo", new NTBException("err.sys.GeneralError"));

            //写一锟斤拷锟秸达拷锟斤拷
            ActionErrors errors = new ActionErrors();
            errors.add(
                    "INPUTERROR",
                    new ActionError("err.sys.GeneralError"));
            return errors;
        }
        return null;

    }

    public HashMap validate1(
            ActionMapping actionMapping,
            ServletEndpointContext serviceContext,
            HashMap paras) {
        HashMap retData = new HashMap();
        try {

            //锟斤拷锟斤拷说锟斤拷锟斤拷锟斤拷锟�
            MessageContext messageContext = serviceContext.getMessageContext();
            Principal userPrincipal = serviceContext.getUserPrincipal();
            parameters = paras;

            //锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
            doValidate(actionMapping);
        } catch (NTBException e) {
            retData.put("ErrorData$Of$Neturbo", e.getErrorCode());
        } catch (Exception e) {
            //锟斤拷录锟斤拷锟斤拷
            Log.error(
                    "System error when validating WebService Form("
                    + actionMapping.getName()
                    + ")",
                    e);
            //锟斤拷锟斤拷锟斤拷锟斤拷锟叫达拷锟絊ession锟叫ｏ拷锟斤拷锟斤拷锟斤拷锟酵筹拷啤锟较低筹拷诓锟斤拷锟斤拷锟�
            retData.put(
                    "ErrorData$Of$Neturbo",
                    "9999");
        }
        return retData;

    }

    /**
     *锟斤拷锟絊OAP锟斤拷锟斤拷姆锟斤拷锟�
     */
    private boolean getSOAPRequest(MessageContext ctx) {
        try {
            SOAPMessageContext mc = (SOAPMessageContext) ctx;
            SOAPMessage msg = mc.getMessage();
            SOAPPart sp = msg.getSOAPPart();
            SOAPEnvelope se = sp.getEnvelope();
            SOAPHeader header = se.getHeader();
            // Now we can process the header
        } catch (Exception ex) {
            Log.error("Process SOAPRequest Error");
        }
        return true;
    }

    public void doValidate(ActionMapping actionMapping) throws NTBException {
        String submitToken = (String) parameters.get("SubmitAttack");
        if (submitToken != null) {
            if (!SubmitToken.checkToken(submitToken)) {
                throw new NTBException("450");
            }
        }
    	
        /*
           String[] fieldNames = actionMapping.findFields();
           NTBErrorArray errorArray = new NTBErrorArray();

           for (int i = 0; i < fieldNames.length; i++) {
            String fieldName = fieldNames[i];
            ActionField field = actionMapping.findField(fieldName);
                String fieldLabel = field.getLabel();

            //锟斤拷锟斤拷锟斤拷锟斤拷锟绞憋拷锟斤拷锟轿拷盏锟酵拷锟轿狽ULL
            String fieldValue = Utils.null2Empty(parameters.get(fieldName));

            //锟斤拷锟絩equired锟斤拷锟角凤拷锟斤拷耄�
            if (field.getRequired() == true) {
             if (fieldValue.equals("")) {
              errorArray.addError("401", fieldName, fieldLabel);
              continue;
             }
            }
            //锟斤拷锟絝ixlen锟斤拷锟教讹拷锟斤拷锟饺ｏ拷
            int fixlen = field.getFixlen();
            if (fixlen >= 0) {
             if (fieldValue.length() != fixlen) {
              errorArray.addError("402", fieldName, fieldLabel);
              continue;
             }
            }
            //锟斤拷锟絤axlen锟斤拷锟斤拷蟪ざ龋锟�
            int maxlen = field.getMaxlen();
            if (maxlen >= 0) {
             if (fieldValue.length() > maxlen) {
              errorArray.addError("403", fieldName, fieldLabel);
              continue;
             }
            }
            //锟斤拷锟絤inlen锟斤拷锟斤拷小锟斤拷锟饺ｏ拷
            int minlen = field.getMinlen();
            if (minlen >= 0) {
             if (fieldValue.length() < minlen) {
              errorArray.addError("404", fieldName, fieldLabel);
              continue;
             }
            }
            String format = field.getFormat();
            if (format != null) {
             String checkRetCode = Validator.checkFormat(fieldValue, format);
             if (checkRetCode != null) {
              errorArray.addError(
               Validator.checkFormat(fieldValue, format),
               fieldName, fieldLabel);
              continue;
             }
            }
           }
           if (errorArray.size() > 0) {
            throw new NTBException(errorArray);
           }
         */

    }
    public void doValidateToken(ActionMapping actionMapping,HttpServletRequest httpServletRequest) throws NTBException {

		String submitToken = (String) parameters.get("SubmitAttack");
		if (submitToken != null) {
			HttpSession session = httpServletRequest.getSession();
			if (session != null) {
				try {
					String token = session.getAttribute(
							"ResultData$Of$TokenSubmit").toString();
					if (!SubmitToken.checkToken(token)) {
						throw new NTBException("err.sys.NotAccessable.token");
					}
				} catch (Exception e) {
					throw new NTBException("err.sys.NotAccessable.token");
				}
			}
		}
		//end 
    }

    public void reset(
            ActionMapping actionMapping,
            HttpServletRequest httpServletRequest) {
    }

    public NTBUser getUser() {
        return user;
    }

    public void setUser(NTBUser user) {
        this.user = user;
    }

    public String[] getParameterValues(String key) {
        return (String[]) parameterValues.get(key);
    }

    public Map getParameterValuesMap() {
        return parameterValues;
    }
}
