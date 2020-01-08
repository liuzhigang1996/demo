package com.jsax.service.util;

import app.cib.util.otp.SMSOTPObject;
import app.cib.util.otp.SMSOTPUtil;
import app.cib.util.otp.SMSReturnObject;
import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

public class SMSOtpService extends JsaxAction implements JsaxService{

	@Override
	public void doTransaction() throws Exception {
		this.setTargetType(TARGET_TYPE_ELEMENT);
		
		this.checkSMS();
		
//		this.addSubResponseListByDefaultType("exceedResendFlag", exceedResendFlag);
//		this.addSubResponseListByDefaultType("smsFlowNo", smsFlowNo);
		this.addSubResponseListByDefaultType("errorFlag", "N") ;
	}
	 public void checkSMS() throws NTBException{
			String otp = Utils.null2Empty(this.getParameter("otp"));
			String smsFlowNo = Utils.null2Empty(this.getParameter("smsFlowNo"));
			String exceedResend = Utils.null2Empty(this.getParameter("exceedResend"));
			
			String lang = SMSOTPUtil.getLang(this.getUser().getLanguage());
			
			boolean isDoubleSubmit = false ;
			SMSOTPObject otpObject = SMSOTPUtil.getOtpObject(smsFlowNo) ;
			if(null==otpObject || "".equals(otpObject)){
				isDoubleSubmit = true ;
			}
			
			SMSReturnObject returnObject = new SMSReturnObject() ;
			
			if(!isDoubleSubmit){
				SMSOTPUtil.check(returnObject, smsFlowNo, otp, exceedResend, lang) ;
			}
			
			//update by liang_ly for CR204 2015-4-9
			if(!returnObject.isReturnFlag() && !isDoubleSubmit){
				Log.info("Check SMS: Correct, go on...");
				if(!returnObject.getErrorFlag().equals("N")){
					this.getResultData().put("clearFlag", "N");
					this.addSubResponseListByDefaultType("errorFlag", returnObject.getErrorFlag()) ;
					throw new NTBException(returnObject.getReturnErr());
				}
			}else{
				Log.info("Check SMS: Error, return to previous page...");
				if(!returnObject.getErrorFlag().equals("N")){
					this.getResultData().put("clearFlag", "N");
					this.addSubResponseListByDefaultType("errorFlag", returnObject.getErrorFlag()) ;
					throw new NTBException(returnObject.getReturnErr());
				}else{
					throw new NTBException("err.sms.OTPERROR");
				}
			}
	 }
}

