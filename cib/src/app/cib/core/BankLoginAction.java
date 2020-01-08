package app.cib.core;

/**
 * @author zhushaode
 *
 */

import org.springframework.context.ApplicationContext;

import java.util.*;

import com.neturbo.set.exception.*;
import com.neturbo.set.utils.Encryption;
import com.neturbo.set.utils.EscapeChar;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.core.*;

import app.cib.service.bnk.*;
import app.cib.util.Constants;
import app.cib.bo.bnk.*;
import app.cib.cription.AESUtil;

public class BankLoginAction extends CibAction {
	public BankLoginAction() {
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

		// add by xyf 20081218, get PIN/Security code policy check
		resultData.putAll(this.getCheckRuleMap());

		setResultData(resultData);
	}

	public void login() throws NTBException {

		//add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB begin
		/*
		this.newSession();
		String loginId = getParameter("loginId");
		String password = getParameter("password");
		String localStr = this.getParameter("PageLanguage");
		*/
		String jCryption = this.getParameter("jCryption") ;
		String jCryptionKey = (String)this.getRequest().getSession().getAttribute("jCryptionKey") ;
		this.newSession(); 
		String loginParameters = AESUtil.decryptJC(jCryption, jCryptionKey) ;
		
		Map paraMap = AESUtil.getParamFromUrl(loginParameters) ;
//		Map paraMap = this.getParameters();//mod by linrui for linux
		String loginId = (String)paraMap.get("loginId");
		String password = (String)paraMap.get("password");
		String localStr = (String)paraMap.get("PageLanguage");
		String certModuleListString = (String)paraMap.get("CertModuleListString");
		//add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB end
		//modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB begin
		String capsLockOnFlag = (String) paraMap.get("capsLockOnFlag") ;
		//add by linrui for mul-language 20171109
		if (!(localStr.equals(Constants.US) || localStr.equals(Constants.CN) 
				|| localStr.equals(Constants.TW)|| localStr.equals(Constants.HK)|| localStr.equals(Constants.PT))) {
			localStr = Constants.US;
		}//end
		this.setPageLanguage(localStr);//add by linrui for mul-language
		BankUserService bankUserService = (BankUserService) Config.getAppContext().getBean("bankUserService");

		//modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB begin
		/*BankUser user = bankUserService.authenticate(loginId, password, this);*/
		BankUser user = bankUserService.authenticate(loginId, password, this, capsLockOnFlag);
		//modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB end
		if (user == null) {
			Map resultData = new HashMap();
			resultData.put("loginId", loginId);
			resultData.put("PageLanguage", localStr);
			setResultData(resultData);

			// add by xyf 20081218, get PIN/Security code policy check
			resultData.putAll(this.getCheckRuleMap());

			this.setError(new NTBException("err.sys.UserHasLogined"));
			setForward("forceLoginLoad");
			Log.info("+--------- LINK TO FORCE LOGIN ------+");
			Log.info("LOGINID  = " + loginId);
			Log.info("+------------------------------------+");
			return;
		}

		Map resultData = new HashMap();
		resultData.put("userId", user.getUserId());
		resultData.put("userName", user.getUserName());
		resultData.put("roleId", user.getRoleId());
		resultData.put("PageLanguage", localStr);
		resultData.put("TimeStamp", (new Date()).getTime());
		setResultData(resultData);

		// add by xyf 20081219, get PIN/Security code policy check
		resultData.putAll(this.getCheckRuleMap());

		if (Utils.nullEmpty2Zero(user.getLoginTimes()) == 0) {
			this.setUser(null);
			this.setForward("firstLogin");

			Log.info("+--------- USER FIRST LOGIN ---------------+");
			Log.info("LOGINID  = " + user.getUserId());
			Log.info("SESSIONID  = " + user.getSessionId());
			Log.info("+------------------------------------+");
			return;
		}

		Log.info("+--------- USER LOGIN ---------------+");
		Log.info("LOGINID  = " + user.getUserId());
		Log.info("SESSIONID  = " + user.getSessionId());
		Log.info("+------------------------------------+");
	}

	public void forceLogin() throws NTBException {

		//add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB begin
		/*
		this.newSession();
		String loginId = getParameter("loginId");
		String password = getParameter("password");
		String localStr = this.getParameter("PageLanguage");
		*/
		String jCryption = this.getParameter("jCryption") ;
		String jCryptionKey = (String)this.getRequest().getSession().getAttribute("jCryptionKey") ;
		this.newSession();
		String loginParameters = AESUtil.decryptJC(jCryption, jCryptionKey) ;
		
		Map paraMap = AESUtil.getParamFromUrl(loginParameters) ;
//		Map paraMap = this.getParameters();
		
		String loginId = (String)paraMap.get("loginId");
		String password = (String)paraMap.get("password");
		String localStr = (String)paraMap.get("PageLanguage");
		//add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB end
		//modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB begin
		String capsLockOnFlag = (String) paraMap.get("capsLockOnFlag") ;
		//add by linrui for mul-language 20171109
		if (!(localStr.equals(Constants.US) || localStr.equals(Constants.CN) 
				|| localStr.equals(Constants.TW)|| localStr.equals(Constants.HK)|| localStr.equals(Constants.PT))) {
			localStr = Constants.US;
		}//end
		this.setPageLanguage(localStr);//add by linrui for mul-language
		BankUserService bankUserService = (BankUserService) Config.getAppContext().getBean("bankUserService");

		//modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB begin
		/*BankUser user = bankUserService.authenticate(loginId, password, this);*/
		BankUser user = bankUserService.authenticate(loginId, password, this, capsLockOnFlag);
		//modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB end
		
		if (user == null) {
			Map resultData = new HashMap();
			resultData.put("loginId", loginId);
			resultData.put("PageLanguage", localStr);
			setResultData(resultData);

			// add by xyf 20081218, get PIN/Security code policy check
			resultData.putAll(this.getCheckRuleMap());

			this.setError(new NTBException("err.sys.UserHasLogined"));
			setForward("forceLoginLoad");
			Log.info("+--------- LINK TO FORCE LOGIN ------+");
			Log.info("LOGINID  = " + loginId);
			Log.info("+------------------------------------+");
			return;
		}

		Map resultData = new HashMap();
		resultData.put("userId", user.getUserId());
		resultData.put("userName", user.getUserName());
		resultData.put("roleId", user.getRoleId());
		resultData.put("PageLanguage", localStr);
		setResultData(resultData);

		// add by xyf 20081218, get PIN/Security code policy check
		resultData.putAll(this.getCheckRuleMap());

		Log.info("+--------- USER LOGIN ---------------+");
		Log.info("LOGINID  = " + user.getUserId());
		Log.info("SESSIONID  = " + user.getSessionId());
		Log.info("+------------------------------------+");
	}

	public void logout() throws NTBException {
		BankUser user = (BankUser) getUser();

		Log.info("+--------- USER LOGOUT ---------------+");
		if (user != null) {
			Log.info("LOGINID  = " + getUser().getUserId());
			BankUserService bankUserService = (BankUserService) Config.getAppContext().getBean("bankUserService");

			bankUserService.logout(user, this);
		} else {
			Log.info("USER SESSION TIME OUT");
		}
		Log.info("+------------------------------------+");

		try {
			this.newSession();
			String localStr = this.getParameter("PageLanguage");
			this.setPageLanguage(localStr);
			Map resultData = new HashMap();
			resultData.put("PageLanguage", localStr);
			setResultData(resultData);

			// add by xyf 20081218, get PIN/Security code policy check
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
		// add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB end
		String jCryption = this.getParameter("jCryption") ;
		String jCryptionKey = (String)this.getRequest().getSession().getAttribute("jCryptionKey") ;
		//this.newSession();
		String loginParameters = AESUtil.decryptJC(jCryption, jCryptionKey) ;
		
		Map paraMap = AESUtil.getParamFromUrl(loginParameters) ;
//		Map paraMap = this.getParameters();//mod by linrui for linux
		String userId = (String)paraMap.get("userId");
		String oldPassword = (String)paraMap.get("oldPassword");
		String newPassword = (String)paraMap.get("newPassword");
		// add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB end
		ApplicationContext appContext = Config.getAppContext();
		BankUserService bankUserService = (BankUserService) appContext.getBean("bankUserService");

		//
		Map resultData = new HashMap();
		String localStr = getPageLanguage();
		resultData.put("PageLanguage", localStr);
		this.setResultData(resultData);

		BankUser userObj = bankUserService.load(userId);
		String encryptedPass = Encryption.digest(userId + oldPassword, "MD5");
		String savedPass = userObj.getUserPassword();

		if (!savedPass.equals(encryptedPass)) {
			throw new NTBException("err.sys.bank.PasswordError");
		}
		String encryptedNewPass = Encryption.digest(userId + newPassword, "MD5");
		userObj.setUserPassword(encryptedNewPass);
		userObj.setLoginTimes(new Integer(1));
		bankUserService.update(userObj);
		bankUserService.loadUploadPin(userObj);

		this.getLocale(this.getRequest());

	}
	
	/**
	 * <!-- add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB -->
	 * @param paramter
	 * @return
	 *//*
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
