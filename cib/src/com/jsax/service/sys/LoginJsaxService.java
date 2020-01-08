/**
 * @author hjs
 * 2007-4-26
 */
package com.jsax.service.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.ApplicationContext;

import app.cib.bo.bnk.Corporation;
import app.cib.bo.sys.AbstractCorpUser;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.sys.CorpUserHis;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.service.sys.CorpUserService;
import app.cib.service.txn.BillPaymentService;
import app.cib.util.Constants;
import app.cib.util.otp.SMSOTPUtil;

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
import com.neturbo.set.utils.Utils;

/**
 * @author Nabai
 * 2011-11-29
 * CR149 cert type
 */
public class LoginJsaxService extends JsaxAction implements JsaxService,NotLogined {
	
	public void doTransaction() throws Exception {
		this.setTargetType(TARGET_TYPE_FIELD);
		String cifNo = this.getParameter("cifNo") ;
		String loginId = this.getParameter("loginId").toUpperCase() ;
		String submitFlag = this.getParameter("submitFlag");
		String PageLanguage = this.getParameter("PageLanguage");
		if(!"en_US".equals(PageLanguage)&&!"zh_HK".equals(PageLanguage)){
			PageLanguage = "en_US";
		}
		//add by lzg 20190805
		String password = this.getParameter("password");
		String encryptedPass = Encryption.digest(loginId + password, "MD5");
		//add by lzg end
		
		String operate = this.getParameter("operate") ;
		
		String smsFlowNo = this.getParameter("smsFlowNo") ;
		
		ApplicationContext appContext = Config.getAppContext();
		CorpUserService corpUserService = (CorpUserService) appContext
				.getBean("corpUserService");
		

//		CorpUser user = corpUserService.load(loginId);
		CorpUser user;
		try{
			user = corpUserService.loadWithCorpId(loginId,"C" + cifNo);
		}catch (Exception e) {
			user = null;
		}
		if(null==user || user.getStatus().equals(Constants.STATUS_REMOVED)){
			this.addSubResponseList(TARGET_TYPE_FIELD, "otpLogin", "");
			this.addSubResponseList(TARGET_TYPE_FIELD, "submitFlag", Constants.NO);
			this.addSubResponseList(TARGET_TYPE_FIELD, "VerificationCodeFlag", Constants.YES);
			throw new NTBException("err.sys.PasswordError");
		}else{
			this.addSubResponseList(TARGET_TYPE_FIELD, "submitFlag", Constants.YES);
			this.addSubResponseList(TARGET_TYPE_FIELD, "VerificationCodeFlag", Constants.NO);
		}
		
		if (user.getStatus().equals(Constants.STATUS_FROZEN)) {
			this.addSubResponseList(TARGET_TYPE_FIELD, "otpLogin", "");
			this.addSubResponseList(TARGET_TYPE_FIELD, "submitFlag", Constants.NO);
			this.addSubResponseList(TARGET_TYPE_FIELD, "VerificationCodeFlag", Constants.YES);
			//send sms
			try {
				SMSOTPUtil.sendNotificationSMS(PageLanguage, user.getMobile(), "", Constants.SMS_TRAN_TYPE_FROZEN_LOGIN_PASSWORD,
						"", (new Date()).toString(), "", user.getCorpId(), "",
						"", user.getUserId(), "") ;
			} catch (Exception e) {
				Log.error("send logged in sms msg error", e) ;
			}
			
			throw new NTBLoginException("err.wrong.password5");
			
		}
		
		if (user.getStatus().equals(Constants.STATUS_BLOCKED)) {
			if(user.getLoginTimes() == 0){
				this.addSubResponseList(TARGET_TYPE_FIELD, "otpLogin", "");
				this.addSubResponseList(TARGET_TYPE_FIELD, "submitFlag", Constants.NO);
				this.addSubResponseList(TARGET_TYPE_FIELD, "VerificationCodeFlag", Constants.YES);
				throw new NTBLoginException("err.sys.UserBlocked");
			}else{
				this.addSubResponseList(TARGET_TYPE_FIELD, "otpLogin", "");
				this.addSubResponseList(TARGET_TYPE_FIELD, "submitFlag", Constants.NO);
				this.addSubResponseList(TARGET_TYPE_FIELD, "VerificationCodeFlag", Constants.YES);
				//send sms
				try {
					SMSOTPUtil.sendNotificationSMS(PageLanguage, user.getMobile(), "", Constants.SMS_TRAN_TYPE_LOCKED_LOGIN_PASSWORD,
							"", (new Date()).toString(), "", user.getCorpId(), "",
							"", user.getUserId(), "") ;
				} catch (Exception e) {
					Log.error("send logged in sms msg error", e) ;
				}
				throw new NTBLoginException("err.wrong.password10");
			}
		}
		
		//add by lzg 20190805
		if(!user.getUserPassword().equals(encryptedPass)){
			this.addSubResponseList(TARGET_TYPE_FIELD, "otpLogin", "");
			this.addSubResponseList(TARGET_TYPE_FIELD, "submitFlag", Constants.NO);
			this.addSubResponseList(TARGET_TYPE_FIELD, "VerificationCodeFlag", Constants.YES);
			int loginFailTimes = Utils.nullEmpty2Zero(user.getLoginFailTimes());
			
			int maxFrozenFailTimes = Constants.AllowMaxTimesForFrozen;
			int maxBlockFailTimes = Constants.AllowMaxTimesForBlock;
			loginFailTimes = loginFailTimes+1;
			user.setLoginFailTimes(new Integer(loginFailTimes));
			corpUserService.update(user);
			
			if(loginFailTimes < maxFrozenFailTimes){
				if(loginFailTimes == Constants.LoginFailTimesWarnning1){
					throw new NTBException("err.wrong.password3");
				}
				if(loginFailTimes == Constants.LoginFailTimesWarnning2){
					throw new NTBException("err.wrong.password4");
				}
				//warnning end
				throw new NTBException("err.sys.PasswordError");
			}
			
			if(loginFailTimes == maxFrozenFailTimes){
				user.setStatus(Constants.STATUS_FROZEN);
				corpUserService.update(user);
				//send sms
				try {
					SMSOTPUtil.sendNotificationSMS(PageLanguage, user.getMobile(), "", Constants.SMS_TRAN_TYPE_FROZEN_LOGIN_PASSWORD,
							"", (new Date()).toString(), "", user.getCorpId(), "",
							"", user.getUserId(), "") ;
				} catch (Exception e) {
					Log.error("send logged in sms msg error", e) ;
				}
				throw new NTBLoginException("err.wrong.password5");
			}
			
			if(loginFailTimes >maxFrozenFailTimes && loginFailTimes < maxBlockFailTimes){
				if(loginFailTimes == 6){
					throw new NTBException("err.wrong.password6");
				}
				if(loginFailTimes == 7){
					throw new NTBException("err.wrong.password7");
				}
				if(loginFailTimes == 8){
					throw new NTBException("err.wrong.password8");
				}
				if(loginFailTimes == 9){
					throw new NTBException("err.wrong.password9");
				}
				//warnning end
				throw new NTBException("err.sys.PasswordError");
			}
			
			if(loginFailTimes >= maxBlockFailTimes){
				user.setStatus(Constants.STATUS_BLOCKED);
				user.setOperation(Constants.OPERATION_BLOCK);
				user.setRequester(user.getUserId());
				user.setBlockReason(Constants.BLOCK_REASON_BY_RETRY);
				String seqNo = CibIdGenerator.getIdForOperation("CORP_USER_HIS");
				CorpUserHis userHis = new CorpUserHis(seqNo);
				try {
					BeanUtils.copyProperties(userHis, user);
				} catch (Exception e) {
					Log.error("Error copy properties", e);
					throw new NTBException("err.sys.GeneralError");
				}
				userHis.setLastUpdateTime(new Date());
				corpUserService.addCorpUserHis(userHis);
				corpUserService.update(user);
				//send sms
				try {
					SMSOTPUtil.sendNotificationSMS(PageLanguage, user.getMobile(), "", Constants.SMS_TRAN_TYPE_LOCKED_LOGIN_PASSWORD,
							"", (new Date()).toString(), "", user.getCorpId(), "",
							"", user.getUserId(), "") ;
				} catch (Exception e) {
					Log.error("send logged in sms msg error", e) ;
				}
				throw new NTBLoginException("err.wrong.password10");
			}
			
		}else{
			this.addSubResponseList(TARGET_TYPE_FIELD, "submitFlag", Constants.YES);
			this.addSubResponseList(TARGET_TYPE_FIELD, "VerificationCodeFlag", Constants.NO);
		}
		
		if("N".equals(submitFlag)){
			this.addSubResponseList(TARGET_TYPE_FIELD, "VerificationCodeFlag", Constants.YES);
		}
		//add by lzg end
		
		Corporation corporation = user.getCorporation() ;
		
		String authMode = corporation.getAuthenticationMode() ;
		String otpLogin = corporation.getOtpLogin() ;
		
		this.addSubResponseList(TARGET_TYPE_FIELD, "showMobile",user.getMobile());
		
		if(Constants.AUTHENTICATION_CERTIFICATION.equalsIgnoreCase(authMode) && Constants.YES.equalsIgnoreCase(otpLogin)){
			
			if("check".equalsIgnoreCase(operate)){
				this.addSubResponseList(TARGET_TYPE_FIELD, "otpLogin", Constants.YES);
				return ;
			} 
			
			String mobile = user.getMobile() ;
			if("send".equalsIgnoreCase(operate)){
				/*smsFlowNo = SMSOTPUtil.sendOtpSMS("E", mobile, "", "", "", "", "", cifNo, "", "", loginId, "") ;*/
				smsFlowNo = SMSOTPUtil.sendOtpSMS(PageLanguage/*"E"*/, mobile, "", "app.login", "", "", "", cifNo, "", "", loginId, "") ;
			} else if("resend".equalsIgnoreCase(operate)){
				smsFlowNo = SMSOTPUtil.resendSMS(smsFlowNo) ;
			}
			this.addSubResponseList(TARGET_TYPE_FIELD, "smsFlowNo", smsFlowNo);
		} else {
			this.addSubResponseList(TARGET_TYPE_FIELD, "otpLogin", Constants.NO);
		}
		
	}

	public String processNotLogined(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
