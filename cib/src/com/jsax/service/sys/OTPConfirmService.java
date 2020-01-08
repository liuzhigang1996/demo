/**
 * @author hjs
 * 2007-4-26
 */
package com.jsax.service.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;

import app.cib.bo.bnk.Corporation;
import app.cib.bo.sys.AbstractCorpUser;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.service.sys.CorpUserService;
import app.cib.service.txn.BillPaymentService;
import app.cib.util.Constants;
import app.cib.util.otp.SMSOTPUtil;
import app.cib.util.otp.SMSReturnObject;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.jsax.core.NotLogined;
import com.jsax.core.SubElement;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBLoginException;
import com.neturbo.set.utils.Encryption;
import com.neturbo.set.utils.RBFactory;

/**
 * @author Nabai
 * 2011-11-29
 * CR149 cert type
 */
public class OTPConfirmService extends JsaxAction implements JsaxService,NotLogined {
	
	public void doTransaction() throws Exception {
		
			String operatorFlag = this.getParameter("operatorFlag") ;
			
			if("Y".equals(operatorFlag)){
				String otp = getParameter("otp");
				String smsFlowNo = getParameter("smsFlowNo");
				SMSReturnObject returnObject = new SMSReturnObject() ;
				try{
                	SMSOTPUtil.check(returnObject, smsFlowNo, otp, "N", "E") ;
                	this.addSubResponseList(TARGET_TYPE_FIELD, "checkOTPFlag", "N");
                	this.addSubResponseList(TARGET_TYPE_FIELD, "smsFlowNo", smsFlowNo);
                	return;
                }catch (NTBException e) {
                	Log.info("OTP Error");
                	returnObject.setErrorFlag("Y") ;
    				returnObject.setReturnErr(e.getErrorCode()) ;
    				if("err.sms.ExceedsResendOTPCount".equals(e.getErrorCode())){
    					this.addSubResponseList(TARGET_TYPE_FIELD, "OTPWrongMaxFlag", "Y");
    				}
				}catch (Exception e) {
					String OTPWrongMaxFlag = this.getParameter("OTPWrongMaxFlag");
					if("Y".equals(OTPWrongMaxFlag)){
						this.addSubResponseList(TARGET_TYPE_FIELD, "refreshFlag", "Y");
					}
					Log.info("OTP Wrong times max");
					throw e;
				}
                if(!returnObject.getErrorFlag().equals("N")){
    				Log.info("One time password error") ;
    				this.addSubResponseList(TARGET_TYPE_FIELD, "smsFlowNo", smsFlowNo);
    				this.addSubResponseList(TARGET_TYPE_FIELD, "checkOTPFlag", "Y");
    				String loginFlag = getParameter("loginFlag");
    				if("Y".equals(loginFlag)){
    					this.addSubResponseList(TARGET_TYPE_FIELD, "VerificationCodeFlag", "Y");
    					this.addSubResponseList(TARGET_TYPE_FIELD, "otpLogin", "Y");
    				}
    				throw new NTBException(returnObject.getReturnErr());
        		}
			}
			
	    	String cropType = "";
	    	String authenticationMode = "";
	    	String operationType = this.getParameter("operationType");
	    	String smsFlowNo = this.getParameter("smsFlowNo") ;
	    	String funcName = "";
	    	CorpUser user  = (CorpUser) this.getUser();
    		cropType = user.getCorporation().getCorpType();
    		authenticationMode = user.getCorporation().getAuthenticationMode();
    		if (null != authenticationMode && "C".equals(authenticationMode)){
	    		String txnType = this.getParameter("txnType");
	        	String approveType=this.getParameter("approveType");
	        	if (approveType==null || approveType.equals("M") ){
	        		funcName = "Multi-Approval";
	        	}else {
	        		funcName = SMSOTPUtil.getFuncName(txnType);
	        	}
    		}
	    	String corpId = user.getCorpId();
	    	if("send".equals(operationType)){
	    		smsFlowNo = SMSOTPUtil.sendOtpSMS(user.getLanguage().toString()/*"E"*/, user.getMobile(), "", funcName, "", "", "", corpId.substring(0,corpId.length()), "", "", user.getUserId(), "");
	    	}else if("reSend".equals(operationType)){
	    		smsFlowNo = SMSOTPUtil.resendSMS(smsFlowNo) ;
	    	}
	    	this.addSubResponseList(TARGET_TYPE_FIELD, "smsFlowNo", smsFlowNo);
	    	
	}

	public String processNotLogined(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
