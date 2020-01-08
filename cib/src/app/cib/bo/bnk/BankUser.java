/*
 * Created Mon Jul 10 18:49:57 CST 2006 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.bnk;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;

import app.cib.service.flow.*;
import app.cib.util.Constants;

import com.neturbo.set.core.*;
import com.neturbo.set.utils.Utils;

import app.cib.core.CibUserCache;

/**
 * A class that represents a row in the 'BANK_USER' table.
 * This class may be customized as it is never re-generated
 * after being created.
 */
public class BankUser extends AbstractBankUser implements NTBUser, Serializable, HttpSessionBindingListener {

    public static final String ROLE_BANK_OPERATOR = "7";
    public static final String ROLE_BANK_SUPERVISOR = "8";
    public static final int ONLINE_STATUS_ON = 1;
    public static final int ONLINE_STATUS_OFF = 0;

    int retryTimes = 0;
    String sessionId = null;
    HttpSession session = null;
    List functionList = null;
    List accountList = null;
    Locale language = Locale.US;
    String langCode = "E";
    int onlineStatus = ONLINE_STATUS_OFF;

    /**
     * Simple constructor of BankUser instances.
     */
    public BankUser() {
    }

    /**
     * Constructor of BankUser instances given a simple primary key.
     * @param userId
     */
    public BankUser(java.lang.String userId) {
        super(userId);
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public void setLanguage(Locale language) {
        this.language = language;
        // add by li_zd at 20180419
        String langCode = "E";
        if (Constants.US.equals(language.toString())) {
        	langCode = "E";
        } else if (Constants.CN.equals(language.toString())) {
        	langCode = "S";
        } else if (Constants.PT.equals(language.toString())) {
        	langCode = "P";
        } else if (Constants.HK.equals(language.toString()) || Constants.TW.equals(language.toString())) {
        	langCode = "C";
        }
        this.setLangCode(langCode);
    }

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

    public void setFunctionList(List functionList) {
        this.functionList = functionList;
    }

    public void setAccountList(List accountList) {
        this.accountList = accountList;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public HttpSession getSession() {
        return session;
    }

    public String getSessionId() {
        return sessionId;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public Locale getLanguage() {
        return language;
    }

	public String getLangCode() {
		return langCode;
	}

    public List getFunctionList() {
        return functionList;
    }

    public List getAccountList() {
        return accountList;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    /**
      * valueBound method comment.
      */
     public void valueBound(javax.servlet.http.HttpSessionBindingEvent arg1) {
         this.setOnlineStatus(ONLINE_STATUS_ON);
         Log.info("Bank User valueBound: user=" + getUserId());
     }

     /**
      * valueUnbound method comment.
      */
     public void valueUnbound(javax.servlet.http.HttpSessionBindingEvent arg1) {
         try {
             this.setOnlineStatus(ONLINE_STATUS_OFF);

             //undo approve works checkouted by me
             Log.info("Bank user valueUnbound: user=" + getUserId());
             FlowEngineService flowEngineService = (FlowEngineService) Config
                                 .getAppContext().getBean("FlowEngineService");
             flowEngineService.undoAllCheckoutWork(this.getUserId());

             //Remove user from cache
             Map userCache = CibUserCache.getBankUserCache();
             BankUser cacheUser = (BankUser)userCache.get(getUserId());
             //by wen 20110311
             if(Utils.null2Empty(cacheUser.getSessionId()).equals(this.getSessionId())){
             	userCache.remove(getUserId());
             }else{
             	Log.info("Force logout because of concurrent login!");
             	Log.info("UserCache will not be remove for this case!");
             }

         } catch (Exception e) {
             Log.error("Error process corporation user value unbound", e);
         }
     }

}
