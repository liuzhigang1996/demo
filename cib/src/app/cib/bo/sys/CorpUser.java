/*
 * Created Mon Jul 10 18:45:07 CST 2006 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.sys;

import java.io.*;
import java.math.*;
import java.security.cert.*;
import java.util.*;

import javax.servlet.http.*;

import app.cib.bo.bnk.*;
import app.cib.service.flow.*;
import app.cib.util.*;
import com.neturbo.set.core.*;
import com.neturbo.set.exception.*;
import com.neturbo.set.utils.Utils;

import app.cib.core.CibUserCache;

/**
 * A class that represents a row in the 'CORP_USER' table.
 * This class may be customized as it is never re-generated
 * after being created.
 */
public class CorpUser extends AbstractCorpUser implements NTBUser, Serializable,
        HttpSessionBindingListener {

    public static final String ROLE_CORP_OPERATOR = "1";
    public static final String ROLE_CORP_APPROVER = "2";
    public static final String ROLE_CORP_EXECUTER = "3";
    public static final String ROLE_CORP_ADMINISRATOR = "4";

    public static final String LOGIN_STATUS_SUCCESSFUL = "0";
    public static final String LOGIN_STATUS_FAILED = "1";
    public static final int ONLINE_STATUS_ON = 1;
    public static final int ONLINE_STATUS_OFF = 0;

    public static final String VIEW_ALL_TRANS_OFF = "0";
    public static final String VIEW_ALL_TRANS_ON = "1";

    int retryTimes = 0;
    String sessionId = null;
    List functionList = null;
    List accountList = null;
    transient Corporation corporation = null;
    Locale language = Locale.US;
    String langCode = "E";
    int onlineStatus = ONLINE_STATUS_OFF;
    int corpAdministatorCount = 0;
    int corpApproverCount = 0;
     X509Certificate userCert = null;

    /**
     * Simple constructor of CorpUser instances.
     */
    public CorpUser() {
    }

    /**
     * Constructor of CorpUser instances given a simple primary key.
     * @param userId
     */
    public CorpUser(java.lang.String userId) {
        super(userId);
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setFunctionList(List functionList) {
        this.functionList = functionList;
    }

    public void setAccountList(List accountList) {
        this.accountList = accountList;
    }

    public void setCorporation(Corporation corporation) {
        this.corporation = corporation;
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

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public void setUserCert(X509Certificate userCert) {
        this.userCert = userCert;
        Log.info("TEST---setUserCert,userCert="+userCert);
    }

    public List getFunctionList() {
        return functionList;
    }

    public List getAccountList() {
        return accountList;
    }

    public Corporation getCorporation() {
        return corporation;
    }

    public Locale getLanguage() {
        return language;
    }

	public String getLangCode() {
		return langCode;
	}

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public X509Certificate getUserCert() {
        Log.info("TEST---getUserCert,userCert="+userCert);
        return userCert;
    }

    public boolean checkUserLimit(String txnType, BigDecimal amountMop) throws
            NTBException {
        BigDecimal limit = getUserLimit(txnType);
        if (amountMop.compareTo(limit) <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public BigDecimal getUserLimit(String txnType) throws
            NTBException {
        Double retVal = null;
        if (txnType.equals(Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT)) {
            retVal = this.getLimit1();
        } else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_BANK)) {
            retVal = this.getLimit2();
        } else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_MACAU)) {
            retVal = this.getLimit3();
        } else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_OVERSEAS)) {
            retVal = this.getLimit4();
        } else if (txnType.equals(Constants.TXN_TYPE_TRANSFER_CORP)) {
            retVal = this.getLimit5();
        } else if (txnType.equals(Constants.TXN_TYPE_PAY_BILLS)) {
            retVal = this.getLimit6();
        } else if (txnType.equals(Constants.TXN_TYPE_TIME_DEPOSIT)) {
            retVal = this.getLimit7();
        } else if (txnType.equals(Constants.TXN_TYPE_PAYROLL)) {
            retVal = this.getLimit8();
        } else if (txnType.equals(Constants.TXN_TYPE_BANK_DRAFT)) {
            retVal = this.getLimit9();
        } else if (txnType.equals(Constants.TXN_TYPE_CASHIER_ORDER)) {
            retVal = this.getLimit10();
        }
        if (retVal != null) {
            return new BigDecimal(String.valueOf(retVal.doubleValue()));//modify by wen_chy 20100322
        } else {
            throw new NTBException("err.txn.UserLimitNotDefined");
        }
    }

    /**
     * valueBound method comment.
     */
    public void valueBound(javax.servlet.http.HttpSessionBindingEvent arg1) {
        this.setOnlineStatus(ONLINE_STATUS_ON);
        Log.info("Corporation User valueBound: user=" + getUserId());
    }

    /**
     * valueUnbound method comment.
     */
    public void valueUnbound(javax.servlet.http.HttpSessionBindingEvent arg1) {
        try {
            this.setOnlineStatus(ONLINE_STATUS_OFF);

            //undo approve works checkouted by me
            Log.info("Corporaton user valueUnbound: user=" + getUserId());
            FlowEngineService flowEngineService = (FlowEngineService) Config
                                                  .getAppContext().getBean(
                    "FlowEngineService");
            flowEngineService.undoAllCheckoutWork(this.getUserId());

            //Remove user from cache
            Map userCache = CibUserCache.getCorpUserCache();
            String userKey =  getCorpId() + "_" + getUserId();//add by linrui 20190521
    		CorpUser cacheUser = (CorpUser) userCache.get(userKey);//add by linrui 20190521
//            CorpUser cacheUser = (CorpUser)userCache.get(getUserId());
            //by wen 20110311 check user concurrent login
            if(Utils.null2Empty(cacheUser.getSessionId()).equals(this.getSessionId())){
//            	userCache.remove(getUserId());
            	userCache.remove(userKey);
            }else{
            	Log.info("Force logout because of concurrent login!");
            	Log.info("UserCache will not be remove for this case!");
            }
            

        } catch (Exception e) {
            Log.error("Error process corporation user value unbound", e);
        }
    }

	public int getCorpAdministatorCount() {
		return corpAdministatorCount;
	}

	public void setCorpAdministatorCount(int corpAdministatorCount) {
		this.corpAdministatorCount = corpAdministatorCount;
	}

	public int getCorpApproverCount() {
		return corpApproverCount;
	}

	public void setCorpApproverCount(int corpApproverCount) {
		this.corpApproverCount = corpApproverCount;
	}

}
