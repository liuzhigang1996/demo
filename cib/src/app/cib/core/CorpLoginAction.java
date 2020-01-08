package app.cib.core;

/**
 * @author zhushaode
 *
 * 锟斤拷锟狡碉拷锟�
 */
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import app.cib.bo.bnk.Corporation;
import app.cib.bo.enq.DaylightSavingTime;
import app.cib.bo.sys.CorpUser;
import app.cib.cription.AESUtil;
import app.cib.service.enq.DaylightSavingTimeService;
import app.cib.service.sys.CorpUserService;
import app.cib.util.Constants;
import app.cib.util.otp.SMSOTPUtil;
import app.cib.util.otp.SMSReturnObject;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Encryption;
import com.neturbo.set.utils.EscapeChar;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.Utils;

public class CorpLoginAction extends CibAction {
	
	
	public CorpLoginAction() {
	}

	protected void processNotLogin() throws NTBException {
	}

	public void load() throws NTBException {
		String localStr = Utils.null2EmptyWithTrim(this.getParameter("PageLanguage"));
		//add by linrui for mul-language
		if (!(localStr.equals(Constants.US) || localStr.equals(Constants.CN) 
				|| localStr.equals(Constants.TW)|| localStr.equals(Constants.HK)|| localStr.equals(Constants.PT))) {
			localStr = Constants.US;
		}//end
		this.setPageLanguage(localStr);
		Map resultData = new HashMap();
		resultData.put("PageLanguage", localStr);
		//add by xyf 20081218, get PIN/Security code policy check
		resultData.putAll(this.getCheckRuleMap());
		
		/*  Add by long_zg 2019-05-16 for otp login begin */
		resultData.put("disableTime", Config.getProperty("app.sms.otp.disable.time"));
		/*  Add by long_zg 2019-05-16 for otp login end */
		
		resultData.put("pin_min",Config.getProperty("pin.length.min"));
		resultData.put("pin_max",Config.getProperty("pin.length.max"));
		
		
		setResultData(resultData);
	}

	public void forceLogin() throws NTBException {
		
		//modify by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB begin
//		this.newSession();
//		String loginId = getParameter("loginId");
//		String password = getParameter("password");
//		String localStr = this.getParameter("PageLanguage");
		
		String jCryption = this.getParameter("jCryption") ;
		String jCryptionKey = (String)this.getRequest().getSession().getAttribute("jCryptionKey") ;
		this.newSession();
		String loginParameters = AESUtil.decryptJC(jCryption, jCryptionKey) ;
		
		Map paraMap = AESUtil.getParamFromUrl(loginParameters) ;
		
		//add by lzg for GAPMC-EB-001-0040
		String cifNo = (String)paraMap.get("cifNo");
		String corpId = "C" + cifNo;
		//add by lzg end
		String loginId = (String)paraMap.get("loginId");
		//add by lzg 20190705
		loginId = loginId.toUpperCase();
		//add by lzg end
		String password = (String)paraMap.get("password");
		String localStr = (String)paraMap.get("PageLanguage");
		
		/* Add by long_zg 2019-05-16 for otp login begin */
		String otp = (String)paraMap.get("otp");
		String smsFlowNo = (String)paraMap.get("smsFlowNo");
		/* Add by long_zg 2019-05-16 for otp login end */
		
		//modify by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB end
		//add by linrui for mul-language 20171109
		if (!(localStr.equals(Constants.US) || localStr.equals(Constants.CN) 
				|| localStr.equals(Constants.TW)|| localStr.equals(Constants.HK)|| localStr.equals(Constants.PT))) {
			localStr = Constants.US;
		}//end
		this.setPageLanguage(localStr);//add by linrui for mul-language
		
		CorpUserService corpUserService = (CorpUserService) Config
				.getAppContext().getBean("corpUserService");

		//modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB begin -->
		/*CorpUser user = corpUserService.authenticate(loginId, password, this);*/
		//modified by lzg for GAPMC-EB-001-0040 20190515
		/* Modify by long_zg 2019-05-16 for otp login begin */
		/*CorpUser user = corpUserService.authenticate(loginId,corpId, password, this,this.getParameters());//paraMap);//mod by linrui for linux*/
		CorpUser user = corpUserService.authenticate(loginId,corpId, password, smsFlowNo, otp, this,this.getParameters(),true);//paraMap);//mod by linrui for linux
		/* Modify by long_zg 2019-05-16 for otp login end */
		//modified by lzg end
		//modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB end -->
		
		if (user == null) {
			Map resultData = new HashMap();
			
			//add by xyf 20081218, get PIN/Security code policy check
			resultData.putAll(this.getCheckRuleMap());
			
			resultData.put("loginId", loginId);
			resultData.put("PageLanguage", localStr);
			setResultData(resultData);

			this.setError(new NTBException("err.sys.UserHasLogined"));
			setForward("forceLoginLoad");
			Log.info("+--------- LINK TO FORCE LOGIN ------+");
			Log.info("LOGINID  = " + loginId);
			Log.info("+------------------------------------+");
			return;
		}
		//add by linrui 20190305
		user.setLanguage(new Locale(localStr.substring(0, 2), localStr.substring(3, 5)));

		Map resultData = new HashMap();
		resultData.put("userId", user.getUserId());
		resultData.put("userName", user.getUserName());
		resultData.put("title", user.getTitle());
		resultData.put("corpName", user.getCorporation().getCorpName());
		resultData.put("roleId", user.getRoleId());
		resultData.put("PageLanguage", localStr);
		setResultData(resultData);
		
		//add by xyf 20081218, get PIN/Security code policy check
		resultData.putAll(this.getCheckRuleMap());
		
		Log.info("+--------- USER LOGIN ---------------+");
		Log.info("LOGINID  = " + user.getUserId());
		Log.info("SESSIONID  = " + user.getSessionId());
		Log.info("+------------------------------------+");
	}

	public void login() throws NTBException {


		
		//add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB begin
		String jCryption = this.getParameter("jCryption") ;
		String jCryptionKey = (String)this.getRequest().getSession().getAttribute("jCryptionKey") ;
		this.newSession();
		String loginParameters = AESUtil.decryptJC(jCryption, jCryptionKey) ;
		
		Map paraMap = AESUtil.getParamFromUrl(loginParameters) ;

		this.newSession();
//		String loginId = getParameter("loginId");
//		String password = getParameter("password");
//		String localStr = this.getParameter("PageLanguage");
//		String certModuleListString = getParameter("CertModuleListString");//20130129
		//add by lzg for GAPMC-EB-001-0040
		String cifNo = (String)paraMap.get("cifNo");
		String corpId = "C" + cifNo;
		//add by lzg end
		String loginId = (String)paraMap.get("loginId");
		//add by lzg 20190704
		loginId = loginId.toUpperCase();
		//add by lzg end
		String password = (String)paraMap.get("password");
		String localStr = (String)paraMap.get("PageLanguage");
		String certModuleListString = (String)paraMap.get("CertModuleListString");
		//add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB end
		
		/* Add by long_zg 2019-05-16 for otp login begin */
		String otp = (String)paraMap.get("otp");
		String smsFlowNo = (String)paraMap.get("smsFlowNo");
		/* Add by long_zg 2019-05-16 for otp login end */
		
		//add by linrui for mul-language
		if (!(localStr.equals(Constants.US) || localStr.equals(Constants.CN) 
				|| localStr.equals(Constants.TW)|| localStr.equals(Constants.HK)|| localStr.equals(Constants.PT))) {
			localStr = Constants.US;
		}//end
		this.setPageLanguage(localStr);//add by linrui for mul-language
		CorpUserService corpUserService = (CorpUserService) Config
				.getAppContext().getBean("corpUserService");

		//modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB begin -->
		/*CorpUser user = corpUserService.authenticate(loginId, password, this);*/
		//modified by lzg for GAPMC-EB-001-0040 20190515
		/* Modify by long_zg 2019-05-16 for otp login begin */
		/*CorpUser user = corpUserService.authenticate(loginId,corpId, password, this,this.getParameters());//paraMap);//mod by linrui for linux*/
		CorpUser user = corpUserService.authenticate(loginId,corpId, password, smsFlowNo, otp, this,this.getParameters(),true);//paraMap);//mod by linrui for linux
		/* Modify by long_zg 2019-05-16 for otp login end */
		//modified by lzg end
		//modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB begin -->
		try{
		if (user == null) {
			Map resultData = new HashMap();
			resultData.put("loginId", loginId);
			resultData.put("PageLanguage", localStr);
			resultData.put("certModuleListString", certModuleListString);//20130129
			setResultData(resultData);

			this.setError(new NTBException("err.sys.UserHasLogined"));
			setForward("forceLoginLoad");
			Log.info("+--------- LINK TO FORCE LOGIN ------+");
			Log.info("LOGINID  = " + loginId);
			Log.info("+------------------------------------+");
			return;
		}
		user.setLanguage(new Locale(localStr.substring(0, 2), localStr.substring(3, 5)));//add by linrui 20190730

		Map resultData = new HashMap();
		resultData.put("userId", user.getUserId());
		//add by lzg 20190528
		resultData.put("cifNo", user.getCorpId().substring(1));
		resultData.put("oldPassword",password);
		//add by lzg end
		resultData.put("userName", user.getUserName());
		resultData.put("roleId", user.getRoleId());

		Corporation corp = user.getCorporation();
		// Jet modified to change time zone format like "+08" to "+8"
		String time_zone = null;
		if (corp.getTimeZone() != null) {
			time_zone = corp.getTimeZone().substring(0, 1)
					+ Utils.removePrefixZero(corp.getTimeZone().substring(1,
							corp.getTimeZone().length()));
		}
		
		resultData.put("corpName", corp.getCorpName());
		resultData.put("foreignCity", corp.getForeignCity());
		resultData.put("timeZone", time_zone);
		resultData.put("timeMacau", String.valueOf(new Date().getTime()));
		resultData.put("PageLanguage", localStr);
		resultData.put("certModuleListString", certModuleListString);//20130129

		String cityName = corp.getForeignCity();
		
		if (cityName != null) {
			DaylightSavingTimeService daylightSavingTimeService = (DaylightSavingTimeService) Config
					.getAppContext().getBean("DaylightSavingTimeService");
			DaylightSavingTime daylightSavingTime = daylightSavingTimeService
					.checkDST(cityName);
			if (daylightSavingTime != null) {
				if (daylightSavingTime.isFlag()) {
					// Jet modified to change time zone format, like "+08" to "+8"
					String DST_time_zone = null;
					if (daylightSavingTime.getDstTimeZone() != null) {
						DST_time_zone = daylightSavingTime.getDstTimeZone().substring(0, 1)
								+ Utils.removePrefixZero(daylightSavingTime
										.getDstTimeZone().substring(1,daylightSavingTime.getDstTimeZone().length()));
					}

					resultData.put("timeZone", DST_time_zone);
					resultData.put("timeZoneName", daylightSavingTime
							.getDstTimeZoneName());
					resultData.put("isDst", "YES");
				} else {
					resultData.put("timeZoneName", daylightSavingTime
							.getTimeZoneName());
					resultData.put("isDst", "NO");
				}
			}
		}

		setResultData(resultData);
		//20130129 set certModuleList
		this.getSession().setAttribute("certModuleListString", certModuleListString);

		if (Utils.nullEmpty2Zero(user.getLoginTimes()) == 0) {
			
			//Update by heyj 20190524, add column corpId
			//boolean isActive = corpUserService.isInitPinActive(loginId, new Date());
			boolean isActive = corpUserService.isInitPinActive(loginId, corpId, new Date());
			
			if(!isActive){
				resultData.put("loginId", loginId);
				this.setUser(null);
				this.setError(new NTBException("err.sys.UserFirstLoginInactive"));
				setForward("load");
				Log.info("+------------ LINK TO LOGIN ---------+");
				Log.info("LOGINID  = " + loginId);
				Log.info("+------------------------------------+");
				return;
			}
			//add by xyf 20081218, get PIN/Security code policy check
			resultData.putAll(this.getCheckRuleMap());
			
			//show init security code page
			//add by hjs 20071015
			
			//modified by lzg 20190530
			/*if (user.getCorporation().getAuthenticationMode().equals(Constants.AUTHENTICATION_SECURITY_CODE)
					&& user.getRoleId().equals(Constants.ROLE_APPROVER)
					//CR204 mode 1 don't show chen_y 20160422
					&& !user.getCorporation().getCorpType().equals(Constants.CORP_TYPE_SMALL)) { 
				resultData.put("showChangeScode", "Y");
			}*/
			if (user.getCorporation().getAuthenticationMode().equals(Constants.AUTHENTICATION_SECURITY_CODE)
					&& user.getRoleId().equals(Constants.ROLE_APPROVER)){
					//CR204 mode 1 don't show chen_y 20160422 
				resultData.put("showChangeScode", "Y");
			}
			//modified by lzg end
			
			// add by long_zg 2015-06-24 for CR210 New policy to BOB/BOL password
			resultData.put("userId", user.getUserId()) ;
			resultData.put("idNo", user.getIdNo()) ;
			
			this.setUser(null);
			this.setForward("firstLogin");
			Log.info("+--------- USER FIRST LOGIN ---------------+");
			Log.info("LOGINID  = " + user.getUserId());
			Log.info("SESSIONID  = " + user.getSessionId());
			Log.info("+------------------------------------+");
			return;
		}

//		String lang = SMSOTPUtil.getLang(null==user.getLanguage()?Config.getDefaultLocale():user.getLanguage());
		String lang = null==user.getLanguage().toString()?Config.getDefaultLocale().toString():user.getLanguage().toString();
		String sessionId = this.getSession().getId() ;
		try {
			SMSOTPUtil.sendNotificationSMS(lang, user.getMobile(), sessionId, Constants.SMS_TRAN_TYPE_LOGGED_IN,
					"", (new Date()).toString(), "", user.getCorpId(), "",
					this.getRequest().getRemoteAddr(), user.getUserId(), "") ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.error("send logged in sms msg error", e) ;
		}
		
		Log.info("+--------- USER LOGIN ---------------+");
		Log.info("LOGINID  = " + user.getUserId());
		Log.info("SESSIONID  = " + user.getSessionId());
		Log.info("+------------------------------------+");
	} catch (NTBException e) {
		Map resultData = new HashMap();
		resultData.put("loginId", loginId);
		resultData.put("PageLanguage", localStr);
		resultData.put("certModuleListString", certModuleListString);//20130129
		setResultData(resultData);
		throw e;
	}
	}

	public void logout() throws NTBException {
		CorpUser user = (CorpUser) getUser();

		Log.info("+--------- USER LOGOUT ---------------+");
		if (user != null) {
			Log.info("LOGINID  = " + user.getUserId());
			CorpUserService corpUserService = (CorpUserService) Config
					.getAppContext().getBean("corpUserService");

			corpUserService.logout(user, this);
		} else {
			Log.info("USER SESSION TIME OUT");
		}
		Log.info("+------------------------------------+");

		try {
			this.newSession();
			String localStr = this.getParameter("PageLanguage");
			if (localStr != null && !localStr.equals("")) {
				this.setPageLanguage(localStr);
			}
			Map resultData = new HashMap();
			resultData.put("PageLanguage", localStr);
			resultData.put("pin_min",Config.getProperty("pin.length.min"));
			resultData.put("pin_max",Config.getProperty("pin.length.max"));
			setResultData(resultData);
			
    		//add by xyf 20081218, get PIN/Security code policy check
    		resultData.putAll(this.getCheckRuleMap());
    		
		} catch (Exception ex) {
			Log.warn("Session Invalidated");
		}

		String exitFlag = this.getParameter("exitActionFlag");
		if ("exitByCloseWindow".equals(exitFlag)) {
			setForward("close");
		}

	}

	public void changePin() throws NTBException {
        
		//add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB begin
		String jCryption = this.getParameter("jCryption") ;
		String jCryptionKey = (String)this.getRequest().getSession().getAttribute("jCryptionKey") ;
		//this.newSession();
		String loginParameters = AESUtil.decryptJC(jCryption, jCryptionKey) ;
		
		Map paraMap = AESUtil.getParamFromUrl(loginParameters) ;
//		
//		Map paraMap = this.getParameters() ;//mod by linrui for linux
		
		String userId = (String)paraMap.get("userId");
		String oldPassword = (String)paraMap.get("oldPassword");
		String newPassword = (String)paraMap.get("newPassword");
		String newSecurityCode = (String)paraMap.get("newSecurityCode");
		//add by lzg 20190603
		String OldSecurityCode = (String)paraMap.get("OldSecurityCode");
		//add by lzg end
		Log.info(userId);
		Log.info(oldPassword);
		Log.info(newPassword);
		Log.info(newSecurityCode);
		Log.info(OldSecurityCode);
		//add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB begin
		ApplicationContext appContext = Config.getAppContext();
		CorpUserService corpUserService = (CorpUserService) appContext
				.getBean("corpUserService");
		
		//modified by lzg for GAPMC-EB-001-0040
		String corpId = "";
		try{
			corpId = ((CorpUser)this.getUser()).getCorpId();
		}catch (Exception e) {
			corpId = "C" + paraMap.get("cifNo");
		}
		//CorpUser userObj = corpUserService.load(userId);
		CorpUser userObj = corpUserService.loadWithCorpId(userId, corpId);
		//modified by lzg end
		Map resultData = new HashMap();
		//add by lzg 20190524
		resultData.put("userId", userId);
		resultData.put("loginId", userId);
		resultData.put("password", newPassword);
		resultData.put("cifNo", paraMap.get("cifNo"));
		resultData.put("oldPassword", oldPassword);
		resultData.put("OldSecurityCode", OldSecurityCode);
		//add by lzg end
		String localStr = getPageLanguage();
		resultData.put("PageLanguage", localStr);
		
		// Jet added for security code
		if (userObj.getCorporation().getAuthenticationMode().equals(Constants.AUTHENTICATION_SECURITY_CODE)
				&& userObj.getRoleId().equals(Constants.ROLE_APPROVER)) {
			resultData.put("showChangeScode", "Y");
		}
		
		//add by xyf 20081218, get PIN/Security code policy check
		resultData.putAll(this.getCheckRuleMap());
		
		this.setResultData(resultData);

		
		String encryptedPass = Encryption.digest(userId + oldPassword, "MD5");
		String savedPass = userObj.getUserPassword();

		
		
		//add by lzg 20190603
		if(OldSecurityCode != null){
			String encryptedSecurityCode = Encryption.digest(userId + OldSecurityCode, "MD5");
			String savedSecurityCode = userObj.getSecurityCode();
			if (!encryptedSecurityCode.equals(savedSecurityCode)) {
				throw new NTBException("err.sys.SecurityCodeError");
			}
			String encryptedNewSecCode = Encryption.digest(userId + newSecurityCode, "MD5");
			userObj.setSecurityCode(encryptedNewSecCode);
		}
		//add by lzg end
		
		  //add by lzg 20191008
	       String[] dictionary  = Constants.passwordLimitDictionary.split(",");
	       for (int i = 0; i < dictionary.length; i++) {
	    	   if(newPassword!= null && newPassword.contains(dictionary[i])){
	    		   throw new NTBException("err.sys.passwordRuleError");
	    	   }
	       }
	        //add by lzg end
		String encryptedNewPass = Encryption.digest(userId + newPassword, "MD5");
		userObj.setUserPassword(encryptedNewPass);
		userObj.setLoginTimes(new Integer(1));
		corpUserService.update(userObj);

		corpUserService.loadUploadPin(userObj);

	}
	
public void changePinLogin() throws NTBException {
        
	   String jCryption = this.getParameter("jCryption") ;
	   String jCryptionKey = (String)this.getRequest().getSession().getAttribute("jCryptionKey") ;
	   //this.newSession();
	   String loginParameters = AESUtil.decryptJC(jCryption, jCryptionKey) ;
	
	    Map paraMap = AESUtil.getParamFromUrl(loginParameters) ;
//		Map paraMap = this.getParameters() ;
		this.newSession();
		String cifNo = Utils.null2EmptyWithTrim(paraMap.get("cifNo"));
		String corpId = "C" + cifNo;
		//add by lzg end
		String loginId = Utils.null2EmptyWithTrim(paraMap.get("loginId"));
		//add by lzg 20190704
		loginId = loginId.toUpperCase();
		//add by lzg end
		String password = Utils.null2EmptyWithTrim(paraMap.get("password"));
		String localStr = Utils.null2EmptyWithTrim(paraMap.get("PageLanguage"));
		String certModuleListString = Utils.null2EmptyWithTrim(paraMap.get("CertModuleListString"));
		
		String otp = Utils.null2EmptyWithTrim(paraMap.get("otp"));
		String smsFlowNo = Utils.null2EmptyWithTrim(paraMap.get("smsFlowNo"));
		
		if (!(localStr.equals(Constants.US) || localStr.equals(Constants.CN) 
				|| localStr.equals(Constants.TW)|| localStr.equals(Constants.HK)|| localStr.equals(Constants.PT))) {
			localStr = Constants.US;
		}//end
		this.setPageLanguage(localStr);//add by linrui for mul-language
		CorpUserService corpUserService = (CorpUserService) Config.getAppContext().getBean("corpUserService");
		CorpUser user = corpUserService.authenticate(loginId,corpId, password, smsFlowNo, otp, this,this.getParameters(),false);//paraMap);//mod by linrui for linux

		try{
		if (user == null) {
			Map resultData = new HashMap();
			resultData.put("loginId", loginId);
			resultData.put("PageLanguage", localStr);
			resultData.put("certModuleListString", certModuleListString);//20130129
			setResultData(resultData);

			this.setError(new NTBException("err.sys.UserHasLogined"));
			setForward("forceLoginLoad");
			Log.info("+--------- LINK TO FORCE LOGIN ------+");
			Log.info("LOGINID  = " + loginId);
			Log.info("+------------------------------------+");
			return;
		}
		user.setLanguage(new Locale(localStr.substring(0, 2), localStr.substring(3, 5)));//add by linrui 20190730

		Map resultData = new HashMap();
		resultData.put("userId", user.getUserId());
		//add by lzg 20190528
		resultData.put("cifNo", user.getCorpId().substring(1));
		resultData.put("oldPassword",password);
		//add by lzg end
		resultData.put("userName", user.getUserName());
		resultData.put("roleId", user.getRoleId());

		Corporation corp = user.getCorporation();
		// Jet modified to change time zone format like "+08" to "+8"
		String time_zone = null;
		if (corp.getTimeZone() != null) {
			time_zone = corp.getTimeZone().substring(0, 1)
					+ Utils.removePrefixZero(corp.getTimeZone().substring(1,
							corp.getTimeZone().length()));
		}
		
		resultData.put("corpName", corp.getCorpName());
		resultData.put("foreignCity", corp.getForeignCity());
		resultData.put("timeZone", time_zone);
		resultData.put("timeMacau", String.valueOf(new Date().getTime()));
		resultData.put("PageLanguage", localStr);
		resultData.put("certModuleListString", certModuleListString);//20130129

		String cityName = corp.getForeignCity();
		
		if (cityName != null) {
			DaylightSavingTimeService daylightSavingTimeService = (DaylightSavingTimeService) Config
					.getAppContext().getBean("DaylightSavingTimeService");
			DaylightSavingTime daylightSavingTime = daylightSavingTimeService
					.checkDST(cityName);
			if (daylightSavingTime != null) {
				if (daylightSavingTime.isFlag()) {
					// Jet modified to change time zone format, like "+08" to "+8"
					String DST_time_zone = null;
					if (daylightSavingTime.getDstTimeZone() != null) {
						DST_time_zone = daylightSavingTime.getDstTimeZone().substring(0, 1)
								+ Utils.removePrefixZero(daylightSavingTime
										.getDstTimeZone().substring(1,daylightSavingTime.getDstTimeZone().length()));
					}

					resultData.put("timeZone", DST_time_zone);
					resultData.put("timeZoneName", daylightSavingTime
							.getDstTimeZoneName());
					resultData.put("isDst", "YES");
				} else {
					resultData.put("timeZoneName", daylightSavingTime
							.getTimeZoneName());
					resultData.put("isDst", "NO");
				}
			}
		}

		setResultData(resultData);
		//20130129 set certModuleList
		this.getSession().setAttribute("certModuleListString", certModuleListString);
//		String lang = SMSOTPUtil.getLang(null==user.getLanguage()?Config.getDefaultLocale():user.getLanguage());
		String lang = null==user.getLanguage().toString()?Config.getDefaultLocale().toString():user.getLanguage().toString();
		String sessionId = this.getSession().getId() ;
		try {
			SMSOTPUtil.sendNotificationSMS(lang, user.getMobile(), sessionId, Constants.SMS_TRAN_TYPE_LOGGED_IN,
					"", (new Date()).toString(), "", user.getCorpId(), "",
					this.getRequest().getRemoteAddr(), user.getUserId(), "") ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.error("send logged in sms msg error", e) ;
		}
		
		Log.info("+--------- USER LOGIN ---------------+");
		Log.info("LOGINID  = " + user.getUserId());
		Log.info("SESSIONID  = " + user.getSessionId());
		Log.info("+------------------------------------+");
	} catch (NTBException e) {
		Map resultData = new HashMap();
		resultData.put("loginId", loginId);
		resultData.put("PageLanguage", localStr);
		resultData.put("certModuleListString", certModuleListString);//20130129
		setResultData(resultData);
		throw e;
	}

	}
	/*
	public Map getParam(String paramter) {
		Map paraMap = new HashMap() ;
		String[] param = paramter.split("&") ;
		for(int i=0;i<param.length;i++){
			String[] item = param[i].split("=") ;
			paraMap.put(item[0], 1==item.length?"": EscapeChar.fromURL(item[1])) ;
		}
		return paraMap ;
	}*/
}
