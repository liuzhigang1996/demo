/**
 * @author hjs
 * 2006-12-7
 */
package com.jsax.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import app.cib.util.ErrConstants;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBError;
import com.neturbo.set.exception.NTBErrorArray;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.utils.DBRCFactory;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 
 * ���������ȡ���ײ�һ���������ܻ�������Ŀ��ͬ�������ı���쳣�����ַ�����һ�㣬�������ƴ���Ҳ���ӵ�����
 * 2006-12-7
 */
public abstract class JsaxAction extends AbstractJsaxAction {
	
    private static String msgResource = Config.getProperty("MessageResources");
	
	String processException (HttpServletRequest request, Exception e) {
		
		try {
			//for CR204 add by chen_y����һ����ձ�־�����û�о�Ĭ����գ�����N�����
			String clearFlag = "";
			try{
				clearFlag = (String) this.getResultData().get("clearFlag");
			}catch (Exception e1) {
				clearFlag = null;
			}
			if(clearFlag!=null && !clearFlag.equals("N")){
				//���ResponseList
				clearResponseList();
			}
			
			
            Locale locale = (Locale) request.getSession().getAttribute("Locale$Of$Neturbo");
            if(locale == null){
                if (this.getUser() != null) {
                    locale = this.getUser().getLanguage();
                }
            }
            if(locale == null){
                locale = Config.getDefaultLocale();
            }
			
			if (e instanceof NTBException) {
				//NTBException
				
	            
				NTBException ntbErr = (NTBException) e;
				
	            //������������б�
	            NTBErrorArray errArray = ntbErr.getErrorArray();
	            boolean showErrArrayFlag = false;

	            if (errArray != null) {
	            	List errMsgList = new ArrayList();
	                for (int i = 0; i < errArray.size(); i++) {
	                    NTBError fieldError = (NTBError) errArray.getError(i);
	                    String fieldName = fieldError.getField();
	                    String errCode = fieldError.getErrorCode();
	                    String errLabel = fieldError.getLabel();

	                    String errStr = "";
	                    if (errCode != null) {

	                        //��������뷭��ɴ�����Ϣ
	                    	/*
	                        RBFactory rbErrMsg = RBFactory.getInstance(msgResource, locale.toString());
	                        if (errStr == null) {
	                            DBRCFactory dbHostMsg = DBRCFactory.getInstance("hostErrMsg");
	                            if (dbHostMsg != null) {
	                                errStr = dbHostMsg.getString(errCode.toString());
	                            }
	                        }
	                         * 
	                         */

                        	if (ntbErr instanceof NTBHostException) {
                                DBRCFactory dbHostMsg = DBRCFactory.getInstance("hostErrMsg");
                                if (dbHostMsg != null) {
                                    errStr = dbHostMsg.getString(errCode);
                                    if(!errCode.equals(errStr)){
                                        errStr = errCode + " - " + errStr;
                                    }
                                }
                        	} else {
                                RBFactory rbErrMsg = RBFactory.getInstance(msgResource, locale.toString());
                                if (rbErrMsg != null) {
                                	errStr = rbErrMsg.getString(errCode);
                                }
                                Object[] parameters = ntbErr.getParameters();
                                if (parameters != null) {
                                    for (int j = 0; j < parameters.length; j++) {
                                    	errStr = Utils.replaceStr(errStr,
                                                "[%" + String.valueOf(j + 1) +
                                                "]",
                                                Utils.null2Empty(parameters[j]));
                                    }
                                }
                        	}

	                        if (errStr == null) {
	                            errStr = errCode.toString();
	                        }
	                        
	                        ErrMsgHandler handler = new ErrMsgHandler(errStr, Utils.null2Empty(fieldName),
	                        		Utils.null2Empty(errLabel), "-1", "0");                    
	                        SubElement errMsgElement = handler.getSubErrMsgElement();
	                        errMsgList.add(errMsgElement);
	                        
                            showErrArrayFlag = true;
	                        
	                    }
	                }
	                this.addSubResponseList(TARGET_TYPE_OBJECT, ERR_PROCESSOR, errMsgList);
	            }

                //���������Ϊ��������������ݽ�����
                String errCode = ntbErr.getErrorCode();
                String errMsg = "";
                if (errCode != null && !showErrArrayFlag) {

                    RBFactory rbErrMsg = RBFactory.getInstance(msgResource, locale.toString());
                    errMsg = rbErrMsg.getString(errCode);
                    Object[] parameters = ntbErr.getParameters();
                    if (parameters != null) {
                        for (int i = 0; i < parameters.length; i++) {
                            errMsg = Utils.replaceStr(errMsg,
                                    "[%" + String.valueOf(i + 1) + "]",
                                    Utils.null2Empty(parameters[i]));
                        }
                    }
                    
                    ErrMsgHandler handler = new ErrMsgHandler(errMsg, "", "", "-1", "0");                    
                    SubElement errMsgElement = handler.getSubErrMsgElement();
                    
                    this.addSubResponseList(TARGET_TYPE_OBJECT, ERR_PROCESSOR, errMsgElement);
                } else if (!showErrArrayFlag) {
                    this.addSubResponseList(TARGET_TYPE_OBJECT, ERR_PROCESSOR, "");
                }
	            return this.backToPage();
	            
			} else {
				
                //����Exception
                //���������Ϊ��������������ݽ�����
                String errCode = ErrConstants.GENERAL_ERROR;
                String errMsg = "";
                if (errCode != null) {

                    RBFactory rbErrMsg = RBFactory.getInstance(msgResource, locale.toString());
                    errMsg = rbErrMsg.getString(errCode);
                    
                    ErrMsgHandler handler = new ErrMsgHandler(errMsg, "", "", "-1", "0");                    
                    SubElement errMsgElement = handler.getSubErrMsgElement();
                    
                    this.addSubResponseList(TARGET_TYPE_OBJECT, ERR_PROCESSOR, errMsgElement);
                }
	            return this.backToPage();
			}
			
		} catch (Exception unknownEx) {
			
			Log.error("Jsax-Service[" + this.getServiceName() + "] processException() Exception", unknownEx);
			return "<ajax-response><response type=\"" + TARGET_TYPE_OBJECT + "\" id=\""+ ERR_PROCESSOR +"\">" + unknownEx.getMessage() + "</response></ajax-response>";
		}
	}

}
