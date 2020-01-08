package app.cib.action.sys;

/**
 * @author nabai
 *
 */
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import app.cib.bo.bnk.BankUser;
import app.cib.bo.bnk.Corporation;
import app.cib.bo.sys.AbstractCorpUser;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.sys.CorpUserHis;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.cription.AESUtil;
import app.cib.dao.sys.CorpUserDao;
import app.cib.service.bnk.ConfigCheckingService;
import app.cib.service.bnk.CorporationService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.sys.CorpUserService;
import app.cib.util.Constants;
import app.cib.util.otp.SMSOTPObject;
import app.cib.util.otp.SMSOTPUtil;
import app.cib.util.otp.SMSReturnObject;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBWarningException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Encryption;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.core.Log;
import org.springframework.context.ApplicationContext;

public class CorpUserAction extends CibAction implements Approvable {
	
	
    public Map showLimits(String roleId, String corpType, Map resultData) {
    	if(Constants.CORP_TYPE_LARGE.equals(corpType) || Constants.CORP_TYPE_MIDDLE_NO_ADMIN.equals(corpType)){
            if (roleId.equals(Constants.ROLE_OPERATOR)) {
                resultData.put("showOperator", "Y");
                resultData.put("showApprover", "N");
            } else if (roleId.equals(Constants.ROLE_APPROVER)) {
                resultData.put("showOperator", "N");
                resultData.put("showApprover", "Y");
            } else {
                resultData.put("showOperator", "N");
                resultData.put("showApprover", "N");
            }
    	} else if(Constants.CORP_TYPE_MIDDLE.equals(corpType)){
    		if (roleId.equals(Constants.ROLE_OPERATOR)) {
                resultData.put("showOperator", "Y");
                resultData.put("showApprover", "N");
            } else if (roleId.equals(Constants.ROLE_APPROVER)) {
                resultData.put("showOperator", "Y");
                resultData.put("showApprover", "Y");
            } else {
                resultData.put("showOperator", "N");
                resultData.put("showApprover", "N");
            }
    	} else if(Constants.CORP_TYPE_SMALL.equals(corpType)){
            if (roleId.equals(Constants.ROLE_OPERATOR)) {
                resultData.put("showOperator", "Y");
                resultData.put("showApprover", "N");
            } else if (roleId.equals(Constants.ROLE_APPROVER)) {
                resultData.put("showOperator", "Y");
                resultData.put("showApprover", "Y");
            } else {
                resultData.put("showOperator", "N");
                resultData.put("showApprover", "N");
            }
    	} else {
            resultData.put("showOperator", "N");
            resultData.put("showApprover", "N");
    	}
        return resultData;
    }

    public void addLoad() throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();
    	CorporationService corporationService = (CorporationService) appContext.getBean("CorporationService");
    	
        Map resultData = new HashMap();
        setResultData(new HashMap(1));
        String corpName = Utils.null2Empty(getParameter("corpName"));
        String corpId = Utils.null2Empty(getParameter("corpId"));
        Corporation corp = corporationService.view(corpId);
        resultData.put("corpName", corpName);
        resultData.put("corpId", corpId);
        resultData.put("corpType", corp.getCorpType());
        resultData.put("authenticationMode", corp.getAuthenticationMode());
        resultData.put("allowFinancialController", corp.getAllowFinancialController());
        //add by linrui 20190816
        resultData.put("allowExcutor", corp.getAllowExecutor());
		/*  Add by long_zg 2019-05-16 for otp login begin */
        resultData.put("otpLogin", corp.getOtpLogin());
        /*  Add by long_zg 2019-05-16 for otp login begin */
        this.setResultData(resultData);
    }

    public void addCancel() throws NTBException {
    }

    public void add() throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();
        CorpUserService corpUserService = (CorpUserService) appContext.getBean("corpUserService");
        Corporation corporation = (Corporation)this.getUsrSessionDataValue("corporation");
        Map para = this.getParameters();
        
        String issueDateString = this.getParameter("idIssueDate");
        Date idIssueDate = null;
        if (issueDateString != null && !issueDateString.equals("")) {
            idIssueDate = DateTime.getDateFromStr(issueDateString);
        }
        para.put("idIssueDate", idIssueDate);
        String certInfo = this.getParameter("certInfo1");
        if(certInfo !=null){
        	certInfo = certInfo.trim();
        	para.put("certInfo1", certInfo);
        }

        CorpUserHis userObj = new CorpUserHis();
        this.convertMap2Pojo(this.getParameters(), userObj);
        userObj.setUserId(userObj.getUserId().toUpperCase());
        //modified by lzg for GAPMC-EB-001-0040
        //CorpUser corpUser = corpUserService.load(userObj.getUserId());
        CorpUser corpUser = corpUserService.loadWithCorpId(userObj.getUserId(),userObj.getCorpId());
        //add by lzg end
        if (corpUser != null) {
            throw new NTBException("err.bnk.corpUserExist");
        }
        if (!corpUserService.checkIdDuplicate(userObj)) {
        	throw new NTBException("err.dupilicate.user");
        }
        //modified by lzg for GAPMC-EB-001-0040
        /*if (corpUserService.loadPendingHis(userObj.getUserId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }*/
        if (corpUserService.loadPendingHisWithCorpId(userObj.getUserId(),userObj.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        //modified by lzg end
        //add by wen_chy 20090925
        String merchantType=this.getMerchantType();
        userObj.setMerchantType(merchantType);

        Map resultData = new HashMap();
        showLimits(userObj.getRoleId(), corporation.getCorpType(), resultData);
        //add by wen_chy 20090927
        this.showMerchantType(merchantType, resultData);
        
        this.setResultData(resultData);
        this.convertPojo2Map(userObj, resultData);
        resultData.put("corpId", corporation.getCorpId());
        resultData.put("corpName", corporation.getCorpName());
        resultData.put("idIssueDate", userObj.getIdIssueDate());
        resultData.put("corpType", corporation.getCorpType());
        resultData.put("authenticationMode", corporation.getAuthenticationMode());
        resultData.put("allowFinancialController", corporation.getAllowFinancialController());

        this.setUsrSessionDataValue("userObj", userObj);
    }
    
    public void addConfirm() throws NTBException {
        CorpUserHis userHisObj = (CorpUserHis)this
                                 .getUsrSessionDataValue("userObj");
        CorpUserService corpUserService = (CorpUserService) Config
                                          .getAppContext().getBean(
                                                  "corpUserService");
        //modified by lzg for GAPMC-EB-001-0040
        //CorpUser corpUser = corpUserService.load(userHisObj.getUserId());
        CorpUser corpUser = corpUserService.loadWithCorpId(userHisObj.getUserId(),userHisObj.getCorpId());
        //modified by lzg end
        if (corpUser != null) {
            throw new NTBException("err.bnk.corpUserExist");
        }
        //modified by lzg for GAPMC-EB-001-0040
        /*if (corpUserService.loadPendingHis(userHisObj.getUserId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }*/
        if (corpUserService.loadPendingHisWithCorpId(userHisObj.getUserId(),userHisObj.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        //modified by lzg end
        String seqNo = CibIdGenerator.getIdForOperation("CORP_USER_HIS");
        Corporation corporation = (Corporation)this
                                  .getUsrSessionDataValue("corporation");

        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");

        String processId = flowEngineService.startProcess(Constants.
                TXN_SUBTYPE_CORP_USER_ADD, "0",
                CorpUserAction.class, null, 0, null, 0, 0, seqNo, null,
                getUser(), null, null, null);

        try {
            userHisObj.setSeqNo(seqNo);
            userHisObj.setUserPassword(Constants.OPERATION_RESET_PASSWORD);
            userHisObj.setOperation(Constants.OPERATION_NEW);
            userHisObj.setStatus(Constants.STATUS_PENDING_APPROVAL);
            userHisObj.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
            userHisObj.setCorpId(corporation.getCorpId());
            userHisObj.setVersion(new Integer(1));
            userHisObj.setLastUpdateTime(new Date());
            userHisObj.setRequester(this.getUser().getUserId());
            corpUserService.addCorpUserHis(userHisObj);

            corpUser = new CorpUser();
            BeanUtils.copyProperties(corpUser, userHisObj);

            corpUserService.add(corpUser);

        } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error adding corp user", e);
            throw new NTBException("err.bnk.AddCorpUserFailure");
        }
        Map resultData = this.getResultData();
        resultData.put("corpId", corporation.getCorpId());
        resultData.put("corpName", corporation.getCorpName());
        resultData.put("seqNo", seqNo);

    }

    public void updateLoad() throws NTBException {
        CorpUserService corpUserService = (CorpUserService) Config
                                          .getAppContext().getBean(
                                                  "corpUserService");

        String userID = getParameter("userId");
        //modified by lzg for GAPMC-EB-001-0040
        String corpId = getParameter("corpId");
        /*if (corpUserService.loadPendingHis(userID) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }*/
        if (corpUserService.loadPendingHisWithCorpId(userID,corpId) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        //CorpUser corpUser = corpUserService.load(userID);
        CorpUser corpUser = corpUserService.loadWithCorpId(userID, corpId);
      //modified by lzg end
        Map resultData = new HashMap();
        this.convertPojo2Map(corpUser, resultData);
        
        //add by wen_chy 20090927
        this.showMerchantType(corpUser.getMerchantType(), resultData);
        
        resultData.put("corpId", corpUser.getCorporation().getCorpId());
        resultData.put("corpName", corpUser.getCorporation().getCorpName());
        resultData.put("corpType", corpUser.getCorporation().getCorpType());
        resultData.put("authenticationMode", corpUser.getCorporation().getAuthenticationMode());
        resultData.put("allowFinancialController", corpUser.getCorporation().getAllowFinancialController());
      //add by linrui 20190816
        resultData.put("allowExcutor", corpUser.getCorporation().getAllowExecutor());
        setResultData(resultData);
        this.setUsrSessionDataValue("corpUser", corpUser);

    }

    public void update() throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();
    	CorpUserService corpUserService = (CorpUserService) appContext.getBean("corpUserService");
        CorpUser corpUser = (CorpUser)this.getUsrSessionDataValue("corpUser");
        FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");
        //modified by lzg for GAPMC-EB-001-0040
        /*if (corpUserService.loadPendingHis(corpUser.getUserId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }*/
        if (corpUserService.loadPendingHisWithCorpId(corpUser.getUserId(),corpUser.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        //modified by lzg end
        Map para = this.getParameters();
        
        if(!para.get("roleId").toString().equals(corpUser.getRoleId())){
            // check is there any outstanding item related to the user
            if(!flowEngineService.checkOutstandingStatus(corpUser, corpUser.getCorporation().getCorpType())){
            	throw new NTBException("err.sys.OutstandingError");
            }
        }

        String idIssueDate = this.getParameter("idIssueDate");
        Date tempIssueDate = null;
        if (idIssueDate != null && !idIssueDate.equals("")) {
            tempIssueDate = DateTime.getDateFromStr(idIssueDate);
        }
        para.put("idIssueDate", tempIssueDate);
        String certInfo = this.getParameter("certInfo1");
        if(certInfo !=null){
        	certInfo = certInfo.trim();
        	para.put("certInfo1", certInfo);
        }     
        
        //###add by wen_chy 20100409
        String roleId = this.getParameter("roleId");
        if(!Constants.ROLE_APPROVER.equals(roleId)){
        		corpUser.setAuthLevel(null);
        		corpUser.setCertInfo1(null);
        }
        //###end
        
        this.convertMap2Pojo(para, corpUser);
        
        //add by wen_chy 20090925
        String merchantType=this.getMerchantType();
        corpUser.setMerchantType(merchantType);


        Map resultData = new HashMap();
        showLimits(corpUser.getRoleId(), corpUser.getCorporation().getCorpType(), resultData);
        
        //add by wen_chy 20090927
        this.showMerchantType(merchantType, resultData);
        
        this.setResultData(resultData);
        this.convertPojo2Map(corpUser, resultData);

        resultData.put("corpId", corpUser.getCorporation().getCorpId());
        resultData.put("corpName", corpUser.getCorporation().getCorpName());
        resultData.put("idIssueDate", corpUser.getIdIssueDate());
        resultData.put("allowFinancialController", corpUser.getCorporation().getAllowFinancialController());
        resultData.put("corpType", corpUser.getCorporation().getCorpType());
        resultData.put("authenticationMode", corpUser.getCorporation().getAuthenticationMode());
        this.setUsrSessionDataValue("corpUser", corpUser);
    }

    public void updateCancel() throws NTBException {
    }
    
    public void updateConfirm() throws NTBException {
        CorpUserService corpUserService = (CorpUserService) Config
                                          .getAppContext().getBean(
                                                  "corpUserService");
        CorpUser corpUser = (CorpUser)this.getUsrSessionDataValue("corpUser");
        //modified by lzg for GAPMC-EB-001-0040
        /*if (corpUserService.loadPendingHis(corpUser.getUserId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }*/
        if (corpUserService.loadPendingHisWithCorpId(corpUser.getUserId(),corpUser.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        //modified by lzg end
        CorpUserHis corpUserHis = new CorpUserHis();
        try {
            BeanUtils.copyProperties(corpUserHis, corpUser);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }
        //hjs
        String[] seqNo = CibIdGenerator.getIdsForOperation("CORP_USER_HIS", 2);

        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");

        String processId = flowEngineService.startProcess(Constants.
                TXN_SUBTYPE_CORP_USER_EDIT,
                "0", CorpUserAction.class, null, 0, null, 0, 0,
                seqNo[1], //hjs
                null, getUser(), null, null, null);

        try {        	
        	Date updateTime = new Date();
            //hjs before
        	//modified by lzg for GAPMC-EB-001-0040
            /*CorpUser corpUserBefore = corpUserService.loadBySeqNo(corpUser.
                    getUserId());*/
        	CorpUser corpUserBefore = corpUserService.loadWithCorpId(corpUser.
                    getUserId(),corpUser.getCorpId());
            //modified by lzg end
            CorpUserHis corpUserHisBefore = new CorpUserHis();
            try {
                BeanUtils.copyProperties(corpUserHisBefore, corpUserBefore);
            } catch (Exception e) {
                Log.error("Error copy properties", e);
                throw new NTBException("err.sys.GeneralError");
            }
            corpUserHisBefore.setSeqNo(seqNo[0]);
            corpUserHisBefore.setOperation(Constants.OPERATION_UPDATE_BEFORE);
            corpUserHisBefore.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
            corpUserHisBefore.setAfterModifyId(seqNo[1]);
            corpUserHisBefore.setLastUpdateTime(updateTime);
            corpUserService.addCorpUserHis(corpUserHisBefore);

            //after
            corpUserHis.setSeqNo(seqNo[1]);
            corpUserHis.setOperation(Constants.OPERATION_UPDATE);
            corpUserHis.setStatus(Constants.STATUS_PENDING_APPROVAL);
            corpUserHis.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
            corpUserHis.setVersion(new Integer(corpUserHis.getVersion().intValue() + 1));
            corpUserHis.setLastUpdateTime(updateTime);
            corpUserHis.setRequester(this.getUser().getUserId());
            //hjs
            String newPrefId = CibIdGenerator.getIdForOperation("CORP_PREFERENCE");
            corpUserHis.setPrefId(newPrefId);
            corpUserService.addCorpUserHis(corpUserHis);

        } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error Editing corp user", e);
            throw new NTBException("err.bnk.EditCorpUserFailure");
        }
        Map resultData = this.getResultData();
        resultData.put("seqNo", seqNo);
    }

    public void setPasswordLoad() throws NTBException {
    	
		//add by xyf 20081218, get PIN/Security code policy check
    	Map resultData = new HashMap();
		resultData.putAll(this.getCheckRuleMap());
		boolean hasMobile = true;
		if(this.getUser() instanceof CorpUser){
			CorpUser corpUser = (CorpUser) this.getUser();
			
			String corpType = corpUser.getCorporation().getCorpType();
			String checkFlag = corpUser.getCorporation().getAuthenticationMode();
			resultData.put("corpType", corpType);
			resultData.put("checkFlag", checkFlag);
			resultData.put("operationType", "send");
	    	resultData.put("showMobileNo", corpUser.getMobile());
		}
		// add by long_zg 2015-06-24 for CR210 New policy to BOB/BOL password
		// begin
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("userId", corpUser.getUserId());
		resultData.put("idNo", corpUser.getIdNo());
		//add by lzg 20190524
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");
		setMessage(RBFactory.getInstance("app.cib.resource.sys.corp_user",lang.toString()).getString("Change_Password_Tip"));
		//add by lzg end
        setResultData(resultData);
    }

    public void setPassword() throws NTBException {

    	//add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB begin
		String jCryption = this.getParameter("jCryption") ;
		String jCryptionKey = (String)this.getRequest().getSession().getAttribute("jCryptionKey") ;
		//this.newSession();
		String loginParameters = AESUtil.decryptJC(jCryption, jCryptionKey) ;
		
		Map paraMap = AESUtil.getParamFromUrl(loginParameters) ;
//    	Map paraMap = this.getParameters();
		//add by linrui 20190528
		String oldPassword = (String)paraMap.get("oldPassword");
        String newPassword = (String)paraMap.get("newPassword");
		Map resultData = this.getResultData();
		resultData.put("newPassword", newPassword);
		resultData.put("oldPassword", oldPassword);
		resultData.put("confirmPassword", newPassword);
		String checkFlag = (String) paraMap.get("checkFlag");
		if("C".equals(checkFlag)){
			String otp = (String) paraMap.get("otp");
			String smsFlowNo =(String) paraMap.get("smsFlowNo");
			SMSOTPObject otpObject = SMSOTPUtil.getOtpObject(smsFlowNo) ;
			SMSReturnObject returnObject = new SMSReturnObject();
			try{
            	SMSOTPUtil.check(returnObject, smsFlowNo, otp, "N", "E") ;
            }catch (NTBException e) {
            	Log.info("OTP Error");
            	returnObject.setErrorFlag("Y") ;
				returnObject.setReturnErr(e.getErrorCode()) ;
			}
            if(!returnObject.getErrorFlag().equals("N")){
				Log.info("One time password error") ;
				resultData.put("smsFlowNo", smsFlowNo);
				setResultData(resultData);
				throw new NTBException(returnObject.getReturnErr());
    		}
		}else if("S".equals(checkFlag)){
			resultData.put("secErrorFlag", "Y");
			this.setResultData(resultData);
			String securityCode = (String) paraMap.get("showSecurityCode");
			checkSecurityCode(securityCode);
		}
		this.setResultData(resultData);
		
      //add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB end
        CorpUserService corpUserService = (CorpUserService) Config.getAppContext().getBean("corpUserService");
        
        // BankUser userObj = (BankUser)this.getUser();
        CorpUser userObj = (CorpUser)this.getUser();
        String userId = userObj.getUserId();
        String encryptedPass = Encryption.digest(userId + oldPassword, "MD5");
        String savedPass = userObj.getUserPassword();

    /* if(!Constants.CORP_TYPE_SMALL.equals(userObj.getCorporation().getCorpType())){
        	String securityCode = (String)paraMap.get("securityCode");
        	checkInputSecurityCode(newPassword);
        }
       */ 
        if (!savedPass.equals(encryptedPass)) {
            throw new NTBException("err.sys.PasswordError.oldPassword");
        }
        
        
        //add by lzg 20191008
       String[] dictionary  = Constants.passwordLimitDictionary.split(",");
       for (int i = 0; i < dictionary.length; i++) {
    	   if(newPassword!= null && newPassword.contains(dictionary[i])){
    		   throw new NTBException("err.sys.passwordRuleError");
    	   }
       }
        //add by lzg end
        
        String encryptedNewPass = Encryption.digest(userId + newPassword, "MD5");
        //add by linrui 20190708
        if (encryptedNewPass.equals(savedPass)) {
            throw new NTBException("err.sys.PasswordError.duplicate");
        }
        //end
        userObj.setUserPassword(encryptedNewPass);
        corpUserService.update(userObj);
        corpUserService.loadUploadPin(userObj);

        /* Add by long_zg 2019-06-02 UAT6-465 COB begin */
        CorpUser user = (CorpUser) this.getUser() ;
//		String lang = SMSOTPUtil.getLang(null==user.getLanguage()?Config.getDefaultLocale():user.getLanguage());
		String lang = null==user.getLanguage().toString()?Config.getDefaultLocale().toString():user.getLanguage().toString();
		String sessionId = this.getSession().getId() ;
		try {
			SMSOTPUtil.sendNotificationSMS(lang, user.getMobile(), sessionId, Constants.SMS_TRAN_TYPE_CHANGE_LOGIN_PASSWORD,
					"", (new Date()).toString(), "", user.getCorpId(), "",
					this.getRequest().getRemoteAddr(), user.getUserId(), "") ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.error("send logged in sms msg error", e) ;
		}
		/* Add by long_zg 2019-06-02 UAT6-465 COB begin */
    }
    
    public void setSecurityCodeLoad() throws NTBException {
    	
		//add by xyf 20081218, get PIN/Security code policy check
    	Map resultData = new HashMap();
		resultData.putAll(this.getCheckRuleMap());
		String corpType = "" ;
		if(this.getUser() instanceof CorpUser){
          	CorpUser Corpuser = (CorpUser)this.getUser() ;
          	corpType =  Corpuser.getCorporation().getCorpType();
      	}
//    	CorpUser Corpuser = (CorpUser)this.getUser() ;
//    	String corpType =  Corpuser.getCorporation().getCorpType();
        resultData.put("corpType", corpType);
        setResultData(resultData);
    }
    
    public void setSecurityCode() throws NTBException {
    	
    	//add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB begin
		String jCryption = this.getParameter("jCryption") ;
		String jCryptionKey = (String)this.getRequest().getSession().getAttribute("jCryptionKey") ;
		//this.newSession();
		String loginParameters = AESUtil.decryptJC(jCryption, jCryptionKey) ;
		
		Map paraMap = AESUtil.getParamFromUrl(loginParameters) ;
//		Map paraMap = this.getParameters();
		
		String oldSecurityCode = (String)paraMap.get("oldSecurityCode");
        String newSecurityCode = (String)paraMap.get("newSecurityCode");
    	ApplicationContext appContext = Config.getAppContext();
        CorpUserService corpUserService = (CorpUserService) appContext.getBean("corpUserService");
        
        
        // BankUser userObj = (BankUser)this.getUser();
        CorpUser userObj = (CorpUser)this.getUser();
        String userId = userObj.getUserId();
        
        if(oldSecurityCode != null){
            String encryptedCode = Encryption.digest(userId + oldSecurityCode, "MD5");
            String savedCode = userObj.getSecurityCode();

            if (!savedCode.equals(encryptedCode)) {
                throw new NTBException("err.sys.SecurityCodeError");
            }
        }
        
     // add by long_zg 2015-06-24 for CR210 New policy to BOB/BOL
        checkSecurityCode(newSecurityCode) ;
        
        String encryptedNewCode = Encryption.digest(userId + newSecurityCode, "MD5");
        userObj.setSecurityCode(encryptedNewCode);
        corpUserService.update(userObj);
        
        CorpUser user = (CorpUser) this.getUser() ;
//		String lang = SMSOTPUtil.getLang(null==user.getLanguage()?Config.getDefaultLocale():user.getLanguage());
		String lang = null==user.getLanguage().toString()?Config.getDefaultLocale().toString():user.getLanguage().toString();
		String sessionId = this.getSession().getId() ;
		try {
			SMSOTPUtil.sendNotificationSMS(lang, user.getMobile(), sessionId, Constants.SMS_TRAN_TYPE_CHANGE_TRANSACTION_PASSWORD,
					"", (new Date()).toString(), "", user.getCorpId(), "",
					this.getRequest().getRemoteAddr(), user.getUserId(), "") ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.error("send logged in sms msg error", e) ;
		}
    }

    public void resetPassword() throws NTBException {
        String userID = getParameter("userId");
        CorpUserService corpUserService = (CorpUserService) Config
                                          .getAppContext().getBean(
                                                  "corpUserService");

        // modified by lzg for GAPMC-EB-001-0040
        String corpId = getParameter("corpId");
        /*if (corpUserService.loadPendingHis(userID) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }*/
        if (corpUserService.loadPendingHisWithCorpId(userID,corpId) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        //CorpUser corpUser = corpUserService.load(userID);
        CorpUser corpUser = corpUserService.loadWithCorpId(userID, corpId);
        //modified by lzg end
        Map resultData = new HashMap();
        showLimits(corpUser.getRoleId(), corpUser.getCorporation().getCorpType(), resultData);
        //add by wen_chy 20090927
        this.showMerchantType(corpUser.getMerchantType(), resultData);
        
        this.convertPojo2Map(corpUser, resultData);
        resultData.put("corpId", corpUser.getCorporation().getCorpId());
        resultData.put("corpName", corpUser.getCorporation().getCorpName());
        resultData.put("corpType", corpUser.getCorporation().getCorpType());
        resultData.put("authenticationMode", corpUser.getCorporation().getAuthenticationMode());
        resultData.put("allowFinancialController", corpUser.getCorporation().getAllowFinancialController());
        setResultData(resultData);
        this.setUsrSessionDataValue("corpUser", corpUser);
    }

    public void resetPasswordCancel() throws NTBException {
    }
    
    public void resetPasswordConfirm() throws NTBException {
        CorpUserService corpUserService = (CorpUserService) Config
                                          .getAppContext().getBean(
                                                  "corpUserService");
        CorpUser corpUser = (CorpUser) getUsrSessionDataValue("corpUser");
        
        //modified by lzg for GAPMC-EB-001-0040
        /*if (corpUserService.loadPendingHis(corpUser.getUserId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }*/
        if (corpUserService.loadPendingHisWithCorpId(corpUser.getUserId(),corpUser.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        //modified by lzg end
        CorpUserHis userHisObj = new CorpUserHis();
        try {
            BeanUtils.copyProperties(userHisObj, corpUser);
        } catch (Exception e) {
            throw new NTBException("err.bnk.copyPropertiesFault");
        }
        String seqNo = CibIdGenerator.getIdForOperation("CORP_USER_HIS");

        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");

        String processId = flowEngineService.startProcess(Constants.TXN_SUBTYPE_CORP_USER_RESET_PWD,
                "0", CorpUserAction.class, null, 0, null, 0, 0,
                seqNo,
                null, getUser(), null, null, null);

        try {
            userHisObj.setSeqNo(seqNo);
            userHisObj.setUserPassword(Constants.OPERATION_RESET_PASSWORD);
            userHisObj.setOperation(Constants.OPERATION_RESET_PASSWORD);
            userHisObj.setLoginTimes(new Integer(0));
            // Jet modified 2008-11-11
            userHisObj.setLoginFailTimes(new Integer(0));
            
            userHisObj.setStatus(Constants.STATUS_PENDING_APPROVAL);
            userHisObj.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
            userHisObj.setLastUpdateTime(new Date());
            userHisObj.setRequester(this.getUser().getUserId());
            corpUserService.addCorpUserHis(userHisObj);

        } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error resetting corp user's password", e);
            throw new NTBException("err.bnk.ResetCorpUserFailure");
        }
    }

    public void resetSecurityCode() throws NTBException {
        String userId = getParameter("userId");
        CorpUserService corpUserService = (CorpUserService) Config
                                          .getAppContext().getBean(
                                                  "corpUserService");

        // modified by lzg for GAPMC-EB-001-0040
        String corpId = getParameter("corpId");
        /*if (corpUserService.loadPendingHis(userId) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }*/
        if (corpUserService.loadPendingHisWithCorpId(userId,corpId) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        //CorpUser corpUser = corpUserService.load(userId);
        CorpUser corpUser = corpUserService.loadWithCorpId(userId, corpId);
        //modified by lzg end
        Map resultData = new HashMap();
        showLimits(corpUser.getRoleId(), corpUser.getCorporation().getCorpType(), resultData);
        //add by wen_chy 20090927
        this.showMerchantType(corpUser.getMerchantType(), resultData);
        
        this.convertPojo2Map(corpUser, resultData);
        resultData.put("corpId", corpUser.getCorporation().getCorpId());
        resultData.put("corpName", corpUser.getCorporation().getCorpName());
        resultData.put("corpType", corpUser.getCorporation().getCorpType());
        resultData.put("authenticationMode", corpUser.getCorporation().getAuthenticationMode());
        resultData.put("allowFinancialController", corpUser.getCorporation().getAllowFinancialController());
        setResultData(resultData);
        this.setUsrSessionDataValue("corpUser", corpUser);
    }

    public void resetSecurityCodeCancel() throws NTBException {
    }
    
    public void resetSecurityCodeConfirm() throws NTBException {
        CorpUserService corpUserService = (CorpUserService) Config
                                          .getAppContext().getBean(
                                                  "corpUserService");
        CorpUser corpUser = (CorpUser) getUsrSessionDataValue("corpUser");
        //modified by lzg for GAPMC-EB-001-0040
        /*if (corpUserService.loadPendingHis(corpUser.getUserId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }*/
        if (corpUserService.loadPendingHisWithCorpId(corpUser.getUserId(),corpUser.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        //modified by lzg end
        CorpUserHis userHisObj = new CorpUserHis();
        try {
            BeanUtils.copyProperties(userHisObj, corpUser);
        } catch (Exception e) {
            throw new NTBException("err.bnk.copyPropertiesFault");
        }
        String seqNo = CibIdGenerator.getIdForOperation("CORP_USER_HIS");

        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");

        String processId = flowEngineService.startProcess(Constants.
                TXN_SUBTYPE_CORP_USER_RESET_CODE,
                "0", CorpUserAction.class, null, 0, null, 0, 0,
                seqNo,
                null, getUser(), null, null, null);

        try {
            userHisObj.setSeqNo(seqNo);
            userHisObj.setUserPassword(Constants.OPERATION_RESET_PASSWORD);
            userHisObj.setSecurityCode("R");
            userHisObj.setOperation(Constants.OPERATION_RESET_PASSWORD);
            userHisObj.setLoginTimes(new Integer(0));
            // Jet modified 2008-11-11
            userHisObj.setLoginFailTimes(new Integer(0));            
            
            userHisObj.setStatus(Constants.STATUS_PENDING_APPROVAL);
            userHisObj.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
            userHisObj.setLastUpdateTime(new Date());
            userHisObj.setRequester(this.getUser().getUserId());
            corpUserService.addCorpUserHis(userHisObj);

        } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error resetting corp user's password", e);
            throw new NTBException("err.bnk.ResetCorpUserFailure");
        }
    }

    public void delete() throws NTBException {

        String userID = getParameter("userId");

        ApplicationContext appContext = Config.getAppContext();
        CorpUserService corpUserService = (CorpUserService) appContext.getBean("corpUserService");
        FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");
        //modified by lzg for GAPMC-EB-001-0040
        /*if (corpUserService.loadPendingHis(userID) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }*/
        String corpId = getParameter("corpId");
        if (corpUserService.loadPendingHisWithCorpId(userID,corpId) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        //CorpUser corpUser = corpUserService.load(userID);
        CorpUser corpUser = corpUserService.loadWithCorpId(userID,corpId);
      //modified by lzg end
        // check is there any outstanding item related to the user
        if(!flowEngineService.checkOutstandingStatus(corpUser, corpUser.getCorporation().getCorpType())){
        	this.setWarningMessageData(RBFactory.getInstance("app.cib.resource.common.errmsg").getString("err.sys.OutstandingError"));
        	//throw new NTBException("err.sys.OutstandingError");/*mod by linrui for yezhijiang do not related 20190819*/
        }

        Map resultData = new HashMap();
        showLimits(corpUser.getRoleId(), corpUser.getCorporation().getCorpType(), resultData);
        //add by wen_chy 20090927
        this.showMerchantType(corpUser.getMerchantType(), resultData);
        
        this.convertPojo2Map(corpUser, resultData);
        resultData.put("corpId", corpUser.getCorporation().getCorpId());
        resultData.put("corpName", corpUser.getCorporation().getCorpName());
        resultData.put("corpType", corpUser.getCorporation().getCorpType());
        resultData.put("authenticationMode", corpUser.getCorporation().getAuthenticationMode());
        resultData.put("allowFinancialController", corpUser.getCorporation().getAllowFinancialController());
        setResultData(resultData);
        this.setUsrSessionDataValue("corpUser", corpUser);
    }

    public void deleteCancel() throws NTBException {
    }
    
    public void deleteConfirm() throws NTBException {
        CorpUserService corpUserService = (CorpUserService) Config
                                          .getAppContext().getBean(
                                                  "corpUserService");
        CorpUser corpUser = (CorpUser)this.getUsrSessionDataValue("corpUser");
        
        //modified by lzg for GAPMC-EB-001-0040
        /*if (corpUserService.loadPendingHis(corpUser.getUserId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }*/
        if (corpUserService.loadPendingHisWithCorpId(corpUser.getUserId(),corpUser.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        //modified by lzg end
        CorpUserHis userHisObj = new CorpUserHis();
        try {
            BeanUtils.copyProperties(userHisObj, corpUser);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }
        String seqNo = CibIdGenerator.getIdForOperation("CORP_USER_HIS");

        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");

        String processId = flowEngineService.startProcess(Constants.
                TXN_SUBTYPE_CORP_USER_DELETE,
                "0", CorpUserAction.class, null, 0, null, 0, 0,
                seqNo,
                null, getUser(), null, null, null);

        try {
            userHisObj.setSeqNo(seqNo);
            userHisObj.setOperation(Constants.OPERATION_REMOVE);
            userHisObj.setStatus(Constants.STATUS_PENDING_APPROVAL);
            userHisObj.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
            userHisObj.setLastUpdateTime(new Date());
            userHisObj.setRequester(this.getUser().getUserId());
            corpUserService.addCorpUserHis(userHisObj);

        } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error deleting corp user", e);
            throw new NTBException("err.bnk.DeleteCorpUserFailure");
        }
    }

    public void block() throws NTBException {
        String userID = getParameter("userId");
        //add by lzg for GAPMC-EB-001-0040
        String corpId = getParameter("corpId");
        //add by lzg end
        ApplicationContext appContext = Config.getAppContext();
        CorpUserService corpUserService = (CorpUserService) appContext.getBean("corpUserService");
        FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");
        //modified by lzg for GAPMC-EB-001-0040
        /*if (corpUserService.loadPendingHis(userID) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }*/
        if (corpUserService.loadPendingHisWithCorpId(userID,corpId) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        Map resultData = new HashMap();
        //CorpUser corpUser = corpUserService.load(userID);
        CorpUser corpUser = corpUserService.loadWithCorpId(userID,corpId);
        //modified by lzg end
        // check is there any outstanding item related to the user
        if(!flowEngineService.checkOutstandingStatus(corpUser, corpUser.getCorporation().getCorpType())){
        	throw new NTBException("err.sys.OutstandingError");
        }
        
        showLimits(corpUser.getRoleId(), corpUser.getCorporation().getCorpType(), resultData);
        //add by wen_chy 20090927
        this.showMerchantType(corpUser.getMerchantType(), resultData);
        
        this.convertPojo2Map(corpUser, resultData);
        resultData.put("corpId", corpUser.getCorporation().getCorpId());
        resultData.put("corpName", corpUser.getCorporation().getCorpName());
        resultData.put("corpType", corpUser.getCorporation().getCorpType());
        resultData.put("authenticationMode", corpUser.getCorporation().getAuthenticationMode());
        resultData.put("allowFinancialController", corpUser.getCorporation().getAllowFinancialController());
        setResultData(resultData);
        this.setUsrSessionDataValue("corpUser", corpUser);

    }

    public void blockCancel() throws NTBException {
    }
    
    public void blockConfirm() throws NTBException {
        CorpUserService corpUserService = (CorpUserService) Config
                                          .getAppContext().getBean(
                                                  "corpUserService");
        CorpUser corpUser = (CorpUser)this.getUsrSessionDataValue("corpUser");
        //modified by lzg for GAPMC-EB-001-0040
        /*if (corpUserService.loadPendingHis(corpUser.getUserId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }*/
        if (corpUserService.loadPendingHisWithCorpId(corpUser.getUserId(),corpUser.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        //modified by lzg end
        CorpUserHis userHisObj = new CorpUserHis();
        try {
            BeanUtils.copyProperties(userHisObj, corpUser);
        } catch (Exception e) {
            throw new NTBException("err.bnk.copyPropertiesFault");
        }
        String seqNo = CibIdGenerator.getIdForOperation("CORP_USER_HIS");

        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");

        String processId = flowEngineService.startProcess(Constants.
                TXN_SUBTYPE_CORP_USER_BLOCK, "0",
                CorpUserAction.class, null, 0, null, 0, 0, seqNo, null,
                getUser(), null, null, null);

        try {
            userHisObj.setSeqNo(seqNo);
            userHisObj.setOperation(Constants.OPERATION_BLOCK);
            userHisObj.setBlockReason(Constants.BLOCK_REASON_BY_ADMIN);
            userHisObj.setStatus(Constants.STATUS_PENDING_APPROVAL);
            userHisObj.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
            userHisObj.setLastUpdateTime(new Date());
            userHisObj.setRequester(this.getUser().getUserId());
            corpUserService.addCorpUserHis(userHisObj);

        } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error blocking corp user", e);
            throw new NTBException("err.bnk.BlockCorpUserFailure");
        }

    }

    public void unblock() throws NTBException {
        String userID = getParameter("userId");
        CorpUserService corpUserService = (CorpUserService) Config
                                          .getAppContext().getBean(
                                                  "corpUserService");
        //modified by lzg for GAPMC-EB-001-0040	
        /*if (corpUserService.loadPendingHis(userID) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }*/
        String corpId = getParameter("corpId");
        if (corpUserService.loadPendingHisWithCorpId(userID,corpId) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        //CorpUser corpUser = corpUserService.load(userID);
        CorpUser corpUser = corpUserService.loadWithCorpId(userID,corpId);
      //modified by lzg end
        Map resultData = new HashMap();
        showLimits(corpUser.getRoleId(), corpUser.getCorporation().getCorpType(), resultData);
        //add by wen_chy 20090927
        this.showMerchantType(corpUser.getMerchantType(), resultData);
        
        this.convertPojo2Map(corpUser, resultData);
        resultData.put("corpId", corpUser.getCorporation().getCorpId());
        resultData.put("corpName", corpUser.getCorporation().getCorpName());
        resultData.put("corpType", corpUser.getCorporation().getCorpType());
        resultData.put("authenticationMode", corpUser.getCorporation().getAuthenticationMode());
        resultData.put("allowFinancialController", corpUser.getCorporation().getAllowFinancialController());
        setResultData(resultData);
        this.setUsrSessionDataValue("corpUser", corpUser);
    }

    public void unblockCancel() throws NTBException {
    }
    
    public void unblockConfirm() throws NTBException {
        CorpUserService corpUserService = (CorpUserService) Config
                                          .getAppContext().getBean(
                                                  "corpUserService");
        CorpUser corpUser = (CorpUser)this.getUsrSessionDataValue("corpUser");
        //modified by lzg for GAPMC-EB-001-0040
        /*if (corpUserService.loadPendingHis(corpUser.getUserId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }*/
        if (corpUserService.loadPendingHisWithCorpId(corpUser.getUserId(),corpUser.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        //modified by lzg end
        CorpUserHis userHisObj = new CorpUserHis();
        try {
            BeanUtils.copyProperties(userHisObj, corpUser);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }
        String seqNo = CibIdGenerator.getIdForOperation("CORP_USER_HIS");

        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");

        String processId = flowEngineService.startProcess(Constants.
                TXN_SUBTYPE_CORP_USER_UNBLOCK, "0",
                CorpUserAction.class, null, 0, null, 0, 0, seqNo, null,
                getUser(), null, null, null);

        try {
            userHisObj.setSeqNo(seqNo);
            userHisObj.setOperation(Constants.OPERATION_UNBLOCK);
            userHisObj.setStatus(Constants.STATUS_PENDING_APPROVAL);
            userHisObj.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
            userHisObj.setLastUpdateTime(new Date());
            userHisObj.setRequester(this.getUser().getUserId());
            corpUserService.addCorpUserHis(userHisObj);

        } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error Unblocking corp user", e);
            throw new NTBException("err.bnk.UnblockCorpUserFailure");
        }
    }

    public void view() throws NTBException {
        String userId = this.getParameter("userId");
        String corpId = this.getParameter("corpId");
        CorpUserService corpUserService = (CorpUserService) Config
                                          .getAppContext().getBean(
                                                  "corpUserService");
        //modified by lzg for GAPMC-EB-001-0040
        //CorpUser user = corpUserService.load(userId);
        CorpUser user = corpUserService.loadWithCorpId(userId,corpId);
        //modified by lzg end
        Map resultData = new HashMap();
        showLimits(user.getRoleId(), user.getCorporation().getCorpType(), resultData);
        //add by wen_chy 20090927
        this.showMerchantType(user.getMerchantType(), resultData);
        
        this.convertPojo2Map(user, resultData);
        resultData.put("idIssueDate", user.getIdIssueDate());
        resultData.put("corpId", user.getCorporation().getCorpId());
        resultData.put("corpName", user.getCorporation().getCorpName());
        resultData.put("corpType", user.getCorporation().getCorpType());
        resultData.put("authenticationMode", user.getCorporation().getAuthenticationMode());
        resultData.put("allowFinancialController", user.getCorporation().getAllowFinancialController());
        setResultData(resultData);
    }

    public void listLoad() throws NTBException {
        setResultData(new HashMap(1));
    }

    public void list() throws NTBException {
    	
    	String corpId = null;
        corpId = Utils.null2EmptyWithTrim(this.getParameter("corpId"));
    	
        ApplicationContext appContext = Config.getAppContext();
        CorporationService corpService = (CorporationService) appContext.getBean("CorporationService");
    	ConfigCheckingService configCheckingService = (ConfigCheckingService) appContext.getBean("ConfigCheckingService");
    	
    	Corporation corp = corpService.view(corpId);
    	List userInfoList = configCheckingService.checkUsersInfo(corp);
    	

        CorporationService corporationService = (CorporationService) Config
                                                .getAppContext().getBean(
                "CorporationService");
        Corporation corp1 = corporationService.view(corpId);
        if (corp1 == null) {
            throw new NTBException("err.bnk.CorpNotExist");
        }
        this.setUsrSessionDataValue("corporation", corp1);

        CorpUserService corpUserService = (CorpUserService) Config
                                          .getAppContext().getBean(
                                                  "corpUserService");

        // CorpUser user = (CorpUser)this.getUser();



        if ((corpId != null) && (!corpId.equals(""))) {
            List userList = corpUserService.listUserByCorp(corpId);
            userList = this.convertPojoList2MapList(userList);
            Map resultData = new HashMap();
            resultData.put("userList", userList);
            resultData.put("corpId", corpId);
            resultData.put("corpName", corp1.getCorpName());
            resultData.put("userInfoList", userInfoList);
            setResultData(resultData);
        }
    }

    public boolean approve(String txnType, String id, CibAction bean) throws
            NTBException {

        ApplicationContext appContext = Config.getAppContext();
        CorpUserService corpUserService = (CorpUserService) appContext.getBean(
                "corpUserService");
//        MailService mailService = (MailService) appContext.getBean(
//                "MailService");
        CorpUserHis corpUserHis = corpUserService.loadHisBySeqNo(id);
        
        String operatorId = ""; //Add by heyj 20190521
        if (txnType.equals(Constants.TXN_SUBTYPE_CORP_USER_ADD)) {
        	
        	//------update by xyf 20081222, set status to BLOCKED-------//
            //corpUserHis.setStatus(Constants.STATUS_NORMAL);
        	corpUserHis.setStatus(Constants.STATUS_BLOCKED);
        	//------update by xyf 20081222 end-------------------------//
        	
            corpUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            corpUserHis.setLastUpdateTime(new Date());
            corpUserService.updateCorpUserHis(corpUserHis);
            // update corpUser
            CorpUser corpUserObj = new CorpUser();
            try {
                BeanUtils.copyProperties(corpUserObj, corpUserHis);
                // TODO remove it after
                /*
                 corpUserObj.setUserPassword(Encryption.digest(corpUserObj.
                        getUserId() + "111111", "MD5"));
                 */
            } catch (Exception e) {
                Log.error("Error copy properties", e);
                throw new NTBException("err.sys.GeneralError");
            }
            
            //Update by heyj 20190520
            corpUserService.update(corpUserObj);
            operatorId = Utils.null2EmptyWithTrim(corpUserHis.getRequester());
            corpUserService.updateAddPrintInfo(corpUserObj, operatorId);
        } else {
            if (txnType.equals(Constants.TXN_SUBTYPE_CORP_USER_EDIT)) {
                corpUserHis.setStatus(Constants.STATUS_NORMAL);
                corpUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
                //hjs before
                List beforeList = corpUserService.listUserHisByAfterId(id);
                if(beforeList.size()>0){
                	CorpUserHis corpUserHisBefore = (CorpUserHis) beforeList.get(0);
                	corpUserHisBefore.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
                    corpUserHisBefore.setLastUpdateTime(new Date());
                    corpUserService.updateCorpUserHis(corpUserHisBefore);
                }
            } else if (txnType.equals(Constants.TXN_SUBTYPE_CORP_USER_BLOCK)) {
                corpUserHis.setStatus(Constants.STATUS_BLOCKED);
                corpUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            } else if (txnType.equals(Constants.TXN_SUBTYPE_CORP_USER_UNBLOCK)) {
                corpUserHis.setStatus(Constants.STATUS_NORMAL);
                corpUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
                corpUserHis.setLoginFailTimes(new Integer(0));
            } else if (txnType.equals(Constants.TXN_SUBTYPE_CORP_USER_DELETE)) {
                corpUserHis.setStatus(Constants.STATUS_REMOVED);
                corpUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            } else if (txnType.equals(Constants.TXN_SUBTYPE_CORP_USER_RESET_PWD)) {
            	//update by xyf 20090424
            	//corpUserHis.setStatus(Constants.STATUS_NORMAL);
            	corpUserHis.setStatus(Constants.STATUS_BLOCKED);
                corpUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
                operatorId = Utils.null2EmptyWithTrim(corpUserHis.getRequester());
            } else if (txnType.equals(Constants.TXN_SUBTYPE_CORP_USER_RESET_CODE)) {
            	//update by xyf 20090424
                //corpUserHis.setStatus(Constants.STATUS_NORMAL);
            	corpUserHis.setStatus(Constants.STATUS_BLOCKED);
                corpUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            }
            corpUserHis.setLastUpdateTime(new Date());
            corpUserService.updateCorpUserHis(corpUserHis);
            // update corpUser
            //modified by lzg for GAPMC-EB-001-0040
            //CorpUser corpUser = corpUserService.load(corpUserHis.getUserId());
            CorpUser corpUser = corpUserService.loadWithCorpId(corpUserHis.getUserId(),corpUserHis.getCorpId());
            
            //modified by lzg end
            try {
            	// Jet added - reserve active information 2008-11-10
            	String orgPassword = corpUser.getUserPassword();
            	String orgSecurityCode = corpUser.getSecurityCode();
            	Integer orgLoginTimes = corpUser.getLoginTimes();
            	String orgLoginStatus = corpUser.getLoginStatus();
            	String orgCurrLoginIp = corpUser.getCurrLoginIp();
            	Date orgCurrLoginTime = corpUser.getCurrLoginTime();
            	String orgPervLoginStatus = corpUser.getPervLoginStatus();
            	String orgPrevLoginIp = corpUser.getPrevLoginIp();
            	Date orgPrevLoginTime = corpUser.getPrevLoginTime();
            	String orgStatus = corpUser.getStatus();
            	String orgBlockReason = corpUser.getBlockReason();
            	
                BeanUtils.copyProperties(corpUser, corpUserHis);
                // keeping UserPassword and LoginTimes except "RESET PASSWORD" and "RESET SECURITY CODE" operation
            	if(!(txnType.equals(Constants.TXN_SUBTYPE_CORP_USER_RESET_PWD) || txnType.equals(Constants.TXN_SUBTYPE_CORP_USER_RESET_CODE))){  
            		corpUser.setUserPassword(orgPassword);
            		corpUser.setLoginTimes(orgLoginTimes);
            	}
            	
            	// keeping SecurityCode except "RESET SECURITY CODE" operation
            	if(!txnType.equals(Constants.TXN_SUBTYPE_CORP_USER_RESET_CODE)){ 
            		corpUser.setSecurityCode(orgSecurityCode);
            	}
            	
                if (txnType.equals(Constants.TXN_SUBTYPE_CORP_USER_UNBLOCK)) {
                	corpUser.setLoginFailTimes(new Integer(0));
                }
                
            	corpUser.setLoginStatus(orgLoginStatus);
            	corpUser.setCurrLoginIp(orgCurrLoginIp);
            	corpUser.setCurrLoginTime(orgCurrLoginTime);
            	corpUser.setPervLoginStatus(orgPervLoginStatus);
            	corpUser.setPrevLoginIp(orgPrevLoginIp);
            	corpUser.setPrevLoginTime(orgPrevLoginTime);
            	
            	if ((Constants.STATUS_BLOCKED).equals(orgStatus) && (Constants.BLOCK_REASON_BY_RETRY).equals(orgBlockReason)){
            		if (txnType.equals(Constants.TXN_SUBTYPE_CORP_USER_EDIT) 
            				|| txnType.equals(Constants.TXN_SUBTYPE_CORP_USER_RESET_PWD) 
            					|| txnType.equals(Constants.TXN_SUBTYPE_CORP_USER_RESET_CODE)) {
                		corpUser.setStatus(orgStatus);
            		}
            	}
            } catch (Exception e) {
                Log.error("Error copy properties", e);
                throw new NTBException("err.sys.GeneralError");
            }
            
            corpUserService.update(corpUser);
            
             //Update by heyj 20190520
            if(txnType.equals(Constants.TXN_SUBTYPE_CORP_USER_RESET_PWD)){
    		    corpUserService.addPrintInfo(corpUser, operatorId);
            }//end
            
            /* hjs
               //write approver releated transation suspend reprot
               if (txnType.equals(Constants.TXN_SUBTYPE_CORP_USER_DELETE)) {
                SchTransferService schTransferService = (SchTransferService) appContext.getBean("SchTransferService");
                SchTxnBatchService schTxnBatchService = (SchTxnBatchService) appContext.getBean("SchTxnBatchService");
                schTransferService.deleteByUserId(corpUserObj.getUserId());
                schTxnBatchService.cancelByUserId(corpUserObj.getUserId());
               }
             */
        }
        // Jet marked 2008-1-16
//        Map mailData = new HashMap();
//        this.convertPojo2Map(corpUserHis, mailData);
//        mailService.toCorpUser_Operated(corpUserHis.getOperation(),
//                                        corpUserHis.getUserId(), mailData);

        return true;
    }

    public boolean reject(String txnType, String id, CibAction bean) throws
            NTBException {
        CorpUserService corpUserService = (CorpUserService) Config
                                          .getAppContext().getBean(
                                                  "corpUserService");
        CorpUserHis corpUserHis = corpUserService.loadHisBySeqNo(id);
        corpUserHis.setStatus(Constants.STATUS_REMOVED);
        corpUserHis.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
        corpUserHis.setLastUpdateTime(new Date());
        corpUserService.updateCorpUserHis(corpUserHis);
        //hjs before
//        List beforeList = corpUserService.listUserHisByAfterId(id);
//        if(beforeList.size()>0){
//            CorpUserHis corpUserHisBefore = (CorpUserHis) beforeList.get(0);
//            corpUserHisBefore.setLastUpdateTime(new Date());
//            corpUserHisBefore.setStatus(Constants.STATUS_REMOVED);
//            corpUserHisBefore.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
//            corpUserService.updateCorpUserHis(corpUserHisBefore);
//        }
        //after
        String userid = corpUserHis.getUserId();
        String corpId = corpUserHis.getCorpId();
        if (txnType.equals(Constants.TXN_SUBTYPE_CORP_USER_ADD)) {
        	//modified by lzg for GAPMC-EB-001-0040
            //CorpUser corpUserObj = corpUserService.loadBySeqNo(userid);
            CorpUser corpUserObj = corpUserService.loadWithCorpId(userid, corpId);
            //modified by lzg end
            corpUserObj.setStatus(Constants.STATUS_REMOVED);
            corpUserService.update(corpUserObj);
        }
        return true;
    }

    public String viewDetail(String txnType, String id, CibAction bean) throws
            NTBException {
    	ApplicationContext appContext = Config.getAppContext();
        CorpUserService corpUserService = (CorpUserService) appContext.getBean("corpUserService");
        CorporationService corpService = (CorporationService) appContext.getBean("CorporationService");
        
        CorpUserHis corpUserHis = corpUserService.loadHisBySeqNo(id);
        Corporation corp = corpService.view(corpUserHis.getCorpId());
        Map resultData = bean.getResultData();
        showLimits(corpUserHis.getRoleId(), corp.getCorpType(), resultData);
        //add by wen_chy 20090925
        this.showMerchantType(corpUserHis.getMerchantType(), resultData);
        resultData.put("allowFinancialController", corp.getAllowFinancialController());
        //add by lzg for GAPMC-EB-001-0063
        resultData.put("corpName", corp.getCorpName());
        //add by lzg end
        this.convertPojo2Map(corpUserHis, resultData);
        return "/WEB-INF/pages/sys/corp_user/user_view_detail.jsp";
    }


    public boolean cancel(String txnType, String id, CibAction bean) throws
            NTBException {
        return reject(txnType, id, bean);
    }
    
    public String getMerchantType(){
    	String merchantType="$";
        String cage = this.getParameter("cage");
        String fb = this.getParameter("fb");
        String ticketing = this.getParameter("ticketing");
        String hotel = this.getParameter("hotel");
        String retail = this.getParameter("retail");
        String general = this.getParameter("general");
        if("Y".equals(general)){
        	merchantType=merchantType+", ";
        }
        if("Y".equals(cage)){
        	merchantType=merchantType+",C";
        }
        if("Y".equals(fb)){
        	merchantType=merchantType+",F";
        }
        if("Y".equals(ticketing)){
        	merchantType=merchantType+",T";
        }
        if("Y".equals(hotel)){
        	merchantType=merchantType+",H";
        }
        if("Y".equals(retail)){
        	merchantType=merchantType+",R";
        }
        return merchantType;
    }
    
    public Map showMerchantType(String merchantType,Map resultData) {
    	
    	if(merchantType==null||"".equals(merchantType))
    		merchantType = "$";
    	
    	if(merchantType.indexOf(" ")>-1)
			resultData.put("general", "Y");
    	else
			resultData.put("general", "N");
    	
    	if(merchantType.indexOf("C")>-1)
			resultData.put("cage", "Y");
		else
			resultData.put("cage", "N");
    	
    	if(merchantType.indexOf("F")>-1)
			resultData.put("fb", "Y");
		else
			resultData.put("fb", "N");
    	
    	if(merchantType.indexOf("T")>-1)
			resultData.put("ticketing", "Y");
    	else
			resultData.put("ticketing", "N");
    	
    	if(merchantType.indexOf("H")>-1)
			resultData.put("hotel", "Y");
    	else
			resultData.put("hotel", "N");
    	
    	if(merchantType.indexOf("R")>-1)
			resultData.put("retail", "Y");
    	else
			resultData.put("retail", "N");
    	
    	return resultData;
    }
    
    
    private void checkSecurityCode(String secCode) throws NTBException {
    	if(this.getUser() instanceof BankUser){
    		return;
    	}
    	if(secCode == null){
    		throw new NTBException("err.sys.getSecurityCodeError");
    	}
    	
    	CorpUser corpUser = (CorpUser) this.getUser();
        String encryptedCode = Encryption.digest(corpUser.getUserId() + secCode, "MD5");
        String savedCode = corpUser.getSecurityCode();
        
        if (Constants.ROLE_APPROVER.equals(((AbstractCorpUser) corpUser).getRoleId()) &&
                ((CorpUser)corpUser).getCorporation().getAuthenticationMode().equals(Constants.AUTHENTICATION_SECURITY_CODE)) {
            
            if (savedCode == null) {
                // 闁跨喐鏋婚幏鐑芥晸鏉炶儻鎻幏鐑芥晸閺傘倖瀚规刊鎺楁晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨噦鎷�
                throw new NTBException("err.sys.SecurityCodeIsNull");
            }
            if ("R".equals(savedCode)) {
                // 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗濮樷�茬串閹风兘鏁撻弬銈嗗闁跨噦鎷�
                throw new NTBException("err.sys.SecurityCodeResetError");
            }        
            if (!savedCode.equals(encryptedCode)) {
                // 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏锟�
                throw new NTBException("err.sys.SecurityCodeError");
            }
        }
    }
    
    
    private void checkInputSecurityCode(String secCode) throws NTBException {
    	if(this.getUser() instanceof BankUser){
    		return;
    	}
    	if(secCode == null){
    		throw new NTBException("err.sys.getSecurityCodeError");
    	}
    	
    	CorpUser corpUser = (CorpUser) this.getUser();
        String encryptedCode = Encryption.digest(corpUser.getUserId() + secCode, "MD5");
        String savedCode = corpUser.getSecurityCode();
        
        if (Constants.ROLE_APPROVER.equals(((AbstractCorpUser) corpUser).getRoleId()) &&
                ((CorpUser)corpUser).getCorporation().getAuthenticationMode().equals(Constants.AUTHENTICATION_SECURITY_CODE)) {
            
            if (savedCode == null) {
                throw new NTBException("err.sys.SecurityCodeIsNull");
            }
            if ("R".equals(savedCode)) {
                throw new NTBException("err.sys.SecurityCodeResetError");
            }        
            if (!savedCode.equals(encryptedCode)) {
                throw new NTBException("err.sys.SecurityCodeError");
            }
        }
    }
    
    /*public static void main(String[] args){
    	String merchantType = "$";
    	Map result = new HashMap();
    	CorpUserAction ab = new CorpUserAction();
    	result = ab.showMerchantType(merchantType,result);
    }*/
    
    public void inputCodeLoad()throws NTBException{
    	Map resultData = this.getResultData();
    	String cropType = "";
    	String authenticationMode = "";//add by linrui 20190512
    	if(this.getUser() instanceof CorpUser){
    		CorpUser user = (CorpUser) this.getUser();
    		cropType = user.getCorporation().getCorpType();
    		authenticationMode = user.getCorporation().getAuthenticationMode();//add by linrui 20190512
    	}
    	resultData.put("corpType", cropType);
    	resultData.put("authenticationMode", authenticationMode);//add by linrui 20190512
    	resultData.put("funcName", "app.cib.action.sys.CorpUser");
    	setResultData(resultData);
    }
}
