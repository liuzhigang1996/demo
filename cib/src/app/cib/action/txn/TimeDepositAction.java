/**
 *
 */
package app.cib.action.txn;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSON;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Encryption;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Sorting;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bnk.BankUser;
import app.cib.bo.bnk.Corporation;
import app.cib.bo.flow.FlowProcess;
import app.cib.bo.srv.TxnPrompt;
import app.cib.bo.sys.AbstractCorpUser;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TimeDeposit;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.core.CibTransClient;
import app.cib.dao.enq.ExchangeRatesDao;
import app.cib.dao.srv.TransferPromptDao;
import app.cib.service.bnk.CorporationService;
import app.cib.service.enq.AccountEnqureService;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.srv.TransferPromptService;
import app.cib.service.sys.ApproveRuleService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.CutOffTimeService;
import app.cib.service.sys.MailService;
import app.cib.service.txn.TimeDepositService;
import app.cib.service.txn.TransferLimitService;
import app.cib.service.txn.TransferService;
import app.cib.util.CachedDBRCFactory;
import app.cib.util.Constants;
import app.cib.util.RcCurrency;
import app.cib.util.TransAmountFormate;
import app.cib.util.UploadReporter;
import app.cib.util.otp.SMSOTPObject;
import app.cib.util.otp.SMSOTPUtil;
import app.cib.util.otp.SMSReturnObject;

import com.neturbo.set.exception.NTBWarningException;

/**
 * @author hjs
 *
 */
public class TimeDepositAction extends CibAction implements Approvable {

	public void addTimeDepositLoad() throws NTBException {
		HashMap resultData = new HashMap(1);
		ApplicationContext appContext = Config.getAppContext();
		/*TransferService transferService = (TransferService) appContext.getBean("TransferService");
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));*/
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");
		CorpUser corpUser = (CorpUser) this.getUser();
		
		//add by lzg 20191022
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		TxnPrompt txnPrompt = new TxnPrompt();
		try{
			txnPrompt = transferPromptService.loadByTxnType("5",TransferPromptDao.STATUS_NORMAL);
			if(txnPrompt == null){
				throw new NTBException("DBError");
			}
		}catch (Exception e) {
			throw new NTBException("DBError");
		}
		String descriptionE = txnPrompt.getDescription("E");
		String descriptionC = txnPrompt.getDescription("C");
		descriptionE =transferPromptService.format(descriptionE);
		descriptionC = transferPromptService.format(descriptionC);
		resultData.put("descriptionE", descriptionE);
		resultData.put("descriptionC", descriptionC);
		resultData.put("transferuser", timeDepositService.loadAccount(corpUser.getCorpId()));
		
		
		setResultData(resultData);
//		this.setResultData(new HashMap(1));
		//add by linrui 20190703
		//setMessage(RBFactory.getInstance("app.cib.resource.txn.time_deposit").getString("WITHDRAW_WARNING"));
	}

	private void filter(List showInterestList) {
		for(int i =0; i < showInterestList.size(); i++){
			Map retMap = (Map) showInterestList.get(i);
			for(Object key : retMap.keySet()){
				if(retMap.get(key) == null){
					retMap.put(key, "--.-%");
				}else if(retMap.get(key) instanceof BigDecimal){
					BigDecimal value = (BigDecimal) retMap.get(key);
					retMap.put(key, value.multiply(new BigDecimal("100")) + "%");
				}
			}
		}
		
	}

	public void addTimeDeposit() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = (CorpUser) this.getUser();
		corpUser.setLanguage(lang);//add by linrui for mul-language

		String principal = Utils.null2EmptyWithTrim(this.getParameter("principal"));
		String currentAccount = Utils.null2EmptyWithTrim(this.getParameter("currentAccount"));
		String currency = Utils.null2EmptyWithTrim(this.getParameter("currency"));
		String period = Utils.null2EmptyWithTrim(this.getParameter("term"));
		//add by linrui 20190325
		String productNo = "";
		String periodAndProdNo[] = period.split("&");
		period = periodAndProdNo[0];
		productNo = periodAndProdNo[1];
		//end
		//add by lzg 20190514
		String selectTermValue = period + "&" + productNo;
		String accountCcy = Utils.null2EmptyWithTrim(this.getParameter("currentAccountCcy"));//add by linrui for mul-ccy

		Map fromHost = timeDepositService.queryNewTDInfo(corpUser, currentAccount,
				principal.toString(), currency, period, accountCcy, productNo);
		Date maturityDate = DateTime.getDateFromStr(fromHost.get("MATURITY_DATE").toString(), "yyyyMMdd");
		Date valueDate = DateTime.getDateFromStr(fromHost.get("EFFECTIVE_DATE").toString(), "yyyyMMdd");

		TimeDeposit timeDeposit = new TimeDeposit(String.valueOf(CibIdGenerator.getRefNoForTransaction()));
		this.convertMap2Pojo(this.getParameters(), timeDeposit);
		timeDeposit.setUserId(corpUser.getUserId());
		timeDeposit.setCorpId(corpUser.getCorpId());
		timeDeposit.setTdType(TimeDeposit.TD_TYPE_NEW);
		timeDeposit.setStatus(Constants.STATUS_PENDING_APPROVAL);
		timeDeposit.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
		timeDeposit.setRequestTime(new Date());
		timeDeposit.setValueDate(valueDate);
		timeDeposit.setMaturityDate(maturityDate);
		timeDeposit.setProductionNo(productNo);
		timeDeposit.setTerm(period);
//		timeDeposit.setCurrentAccountCcy(accountCcy);//add by linrui for mul-ccy
		
//		timeDeposit.setProductionNo(Utils.prefixZero(fromHost.get("PRODUCTION_NO").toString(), 3));
		timeDeposit.setCifNo1(fromHost.get("CIF_1").toString());
		timeDeposit.setShortName(fromHost.get("SHORT_NAME1").toString());
		timeDeposit.setDefaultRate(new Double(fromHost.get("DEFAULT_RATE").toString()));

		//*********
		Double principalEnq  = TransAmountFormate.formateAmount(this.getParameter("principal"),lang);
		timeDeposit.setPrincipal(principalEnq);
		
//		String currentAccountCcy = corpAccountService.getCurrency(timeDeposit.getCurrentAccount(),true);
		//mod by linrui 20190319 mul-ccy
		String currentAccountCcy = accountCcy;

		// 闁岸鏁撻弬銈嗗妞ょ敻鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚规禍鈺呮晸閺傘倖瀚归柨鐔告灮閹烽攱鎸勫▽銈夋晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗閹枫儵鏁撶憴鎺戝簻閹峰嘲鐫呴柨鐔兼▉椤忓孩瀚归柨鐔告灮閹烽攱鍘遍柨鐔告灮閹凤拷
		String currentAccPrincipal = exRatesService.getEquivalent(
				corpUser.getCorpId(),
				currentAccountCcy,
				timeDeposit.getCurrency(),
				null,
				new BigDecimal(timeDeposit.getPrincipal().toString()),
				2).toString();
		
		//String currentAccPrincipal = new BigDecimal(timeDeposit.getPrincipal().toString()).setScale(2).toString();

		fromHost.put("currentAccountCcy", currentAccountCcy);
		fromHost.put("currentAccPrincipal", currentAccPrincipal);

		// 闁跨喐鏋婚幏椋庣矏assignuser_tag闁跨喐鏋婚幏鐑芥晸閿燂拷
		Map assignuser = new HashMap();
		BigDecimal amountMopEq = exRatesService.getEquivalent(corpUser.getCorpId(),
				timeDeposit.getCurrency(), "MOP",
				new BigDecimal(timeDeposit.getPrincipal().toString()),
				null, 2);

		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "currency");
		assignuser.put("amountField", "principal");
		assignuser.put("amountMopEqField", "amountMopEq");
		assignuser.put("txnType", Constants.TXN_SUBTYPE_NEW_TIME_DEPOSIT);
		assignuser.put("amountMopEq", amountMopEq);

		BigDecimal currentAccPrincipalMopEq = exRatesService.getEquivalent(corpUser.getCorpId(),
				currentAccountCcy, "MOP",
				new BigDecimal(currentAccPrincipal),
				null, 2);
		assignuser.put("currentAccountCcy", currentAccountCcy);
		assignuser.put("currentAccPrincipal", currentAccPrincipal);
		assignuser.put("currentAccPrincipalMopEq", currentAccPrincipalMopEq);
		//add by lzg 20191018
		Double defaultRate = new Double(fromHost.get("DEFAULT_RATE").toString());
		Double expectedInterest = getExpectedInterest(defaultRate,new Double(currentAccPrincipal),currentAccountCcy,period);
		assignuser.put("expectedInterest", expectedInterest);
		//add by lzg end
		// Check User Limit
		if (!corpUser.checkUserLimit(Constants.TXN_TYPE_TIME_DEPOSIT, currentAccPrincipalMopEq)) {

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", timeDeposit.getUserId());
			reportMap.put("CORP_ID", timeDeposit.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", timeDeposit.getTransId());
			reportMap.put("FROM_ACCOUNT", timeDeposit.getCurrentAccount());
			reportMap.put("FROM_CURRENCY", currentAccountCcy);
			reportMap.put("TO_CURRENCY", timeDeposit.getCurrency());
			reportMap.put("FROM_AMOUNT", currentAccPrincipal);
			reportMap.put("TO_AMOUNT", timeDeposit.getPrincipal());
			reportMap.put("EXCEEDING_TYPE", "2");
			reportMap.put("LIMIT_TYPE", "");
			reportMap.put("USER_LIMIT ", corpUser.getUserLimit(Constants.TXN_TYPE_PAY_BILLS));
			reportMap.put("DAILY_LIMIT ", new Double(0));
			reportMap.put("TOTAL_AMOUNT ", new Double(0));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			throw new NTBWarningException("err.txn.ExceedUserLimit");
		}
		// check Limit
		if (!transferLimitService.checkCurAmtLimitQuota(timeDeposit.getCurrentAccount(),
				corpUser.getCorpId(),
				Constants.TXN_TYPE_TIME_DEPOSIT,
				new Double(currentAccPrincipal).doubleValue(),
				currentAccPrincipalMopEq.doubleValue())) {

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", timeDeposit.getUserId());
			reportMap.put("CORP_ID", timeDeposit.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", timeDeposit.getTransId());
			reportMap.put("FROM_ACCOUNT", timeDeposit.getCurrentAccount());
			reportMap.put("FROM_CURRENCY", currentAccountCcy);
			reportMap.put("TO_CURRENCY", timeDeposit.getCurrency());
			reportMap.put("FROM_AMOUNT", currentAccPrincipal);
			reportMap.put("TO_AMOUNT", timeDeposit.getPrincipal());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			throw new NTBWarningException("err.txn.ExceedDailyLimit");
		}

		Map resultData = new HashMap();
		
		//add by lzg 20190820
		String corpType = corpUser.getCorporation().getCorpType();
		String checkFlag = corpUser.getCorporation().getAuthenticationMode();
		resultData.put("corpType", corpType);
		resultData.put("checkFlag", checkFlag);
		resultData.put("operationType", "send");
		resultData.put("showMobileNo", corpUser.getMobile());
		resultData.put("txnType", Constants.TXN_TYPE_TIME_DEPOSIT);
		resultData.put("expectedInterest", expectedInterest);
		
		//add by lzg end
		
		resultData.putAll(fromHost);
		resultData.putAll(assignuser);
		this.convertPojo2Map(timeDeposit, resultData);

		ApproveRuleService approveRuleService = (ApproveRuleService) Config
				.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(), resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}
		resultData.put("selectTermValue",selectTermValue);
		this.setResultData(resultData);
		this.setUsrSessionDataValue("assignuser", assignuser);
        //add by linrui 20180329
		fromHost.put("DEFAULT_RATE", Utils.dropZeroAfter(fromHost.get("DEFAULT_RATE").toString()));
		//end
		this.setUsrSessionDataValue("fromHost", fromHost);
		this.setUsrSessionDataValue("timeDeposit", timeDeposit);
	}

	private Double getExpectedInterest(Double defaultRate, Double principal,String ccy, String period) {
		RBFactory rbList = RBFactory.getInstance(
				"app.cib.resource.txn.Interest_Computation");
		int baseDays = Integer.parseInt(rbList.getString(rbList.getString(ccy)));
		Double interestRate = defaultRate/100;
		if(period.startsWith("0") || period.startsWith("2")){
			int days = 0;
			String  childPeriod = period.substring(1);
			if(childPeriod.startsWith("0")){
				days = Integer.parseInt(period.substring(2));
			}else{
				days = Integer.parseInt(period.substring(1));
			}
			return ((principal * interestRate)/baseDays)*days;
		}else if(period.startsWith("1") || period.startsWith("4")){
			int months = 0;
			String  childPeriod = period.substring(1);
			if(childPeriod.startsWith("0")){
				months = Integer.parseInt(period.substring(2));
			}else{
				months = Integer.parseInt(period.substring(1));
			}
			return ((principal * interestRate)/12)*months;
		}
		return null;
	}

	public void addTimeDepositCfm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");
		MailService mailService = (MailService) appContext.getBean("MailService");
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext.getBean("CutOffTimeService");

		CorpUser corpUser = (CorpUser) this.getUser();

		Map fromHost = (Map) this.getUsrSessionDataValue("fromHost");
		TimeDeposit timeDeposit = (TimeDeposit) this.getUsrSessionDataValue("timeDeposit");

		// 闁跨喐鏋婚幏椋庣矏assignuser_tag闁跨喐鏋婚幏鐑芥晸閿燂拷
		Map assignuser = (Map) this.getUsrSessionDataValue("assignuser");
		String txnType = (String) assignuser.get("txnType");
		BigDecimal amountMopEq = (BigDecimal) assignuser.get("amountMopEq");

		// check value date cut-off time
		setMessage(cutOffTimeService.check("ZJ37", "", timeDeposit.getCurrency()));
		
		// add by chen_y for CR225 20170412
		String currentAccountCcy = (String)assignuser.get("currentAccountCcy");
		String currentAccPrincipal = (String) assignuser.get("currentAccPrincipal");
		BigDecimal currentAccPrincipalMopEq = (BigDecimal) assignuser.get("currentAccPrincipalMopEq");
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		if (!transferLimitService.checkLimitQuota(timeDeposit.getCurrentAccount(),
				corpUser.getCorpId(),
				Constants.TXN_TYPE_TIME_DEPOSIT,
				new Double(currentAccPrincipal).doubleValue(),
				currentAccPrincipalMopEq.doubleValue())) {

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", timeDeposit.getUserId());
			reportMap.put("CORP_ID", timeDeposit.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", timeDeposit.getTransId());
			reportMap.put("FROM_ACCOUNT", timeDeposit.getCurrentAccount());
			reportMap.put("FROM_CURRENCY", currentAccountCcy);
			reportMap.put("TO_CURRENCY", timeDeposit.getCurrency());
			reportMap.put("FROM_AMOUNT", currentAccPrincipal);
			reportMap.put("TO_AMOUNT", timeDeposit.getPrincipal());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			//throw new NTBWarningException("err.txn.ExceedDailyLimit");
			setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg").getString("warnning.txn.ExceedDailyLimit"));
		}
		// add by chen_y for CR225 20170412 end

		String assignedUser = Utils.null2EmptyWithTrim(this.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this.getParameter("mailUser"));
		String processId = flowEngineService.startProcess(txnType,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				TimeDepositAction.class,
				fromHost.get("currentAccountCcy").toString(),
				new Double(fromHost.get("currentAccPrincipal").toString()).doubleValue(),
				timeDeposit.getCurrency(),
				timeDeposit.getPrincipal().doubleValue(),
				amountMopEq.doubleValue(),
				timeDeposit.getTransId(),
				null, getUser(), assignedUser,
				((CorpUser)getUser()).getCorporation().getAllowExecutor(),
				FlowEngineService.RULE_FLAG_TO);
		
		
		Map resultData = new HashMap();
		String corpType = getParameter("corpType");
		resultData.put("corpType", corpType);
		try {

			resultData.putAll(fromHost);
			resultData.putAll(assignuser);
			this.convertPojo2Map(timeDeposit, resultData);
			//add by lzg 20191018
			Double defaultRate = new Double(fromHost.get("DEFAULT_RATE").toString());
			Double expectedInterest = getExpectedInterest(defaultRate,new Double(currentAccPrincipal),currentAccountCcy,timeDeposit.getTerm());
			assignuser.put("expectedInterest", expectedInterest);
			resultData.put("expectedInterest", expectedInterest);
			//add by lzg end
			this.setResultData(resultData);

			//send mial to approver
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", timeDeposit.getRequestTime());
			dataMap.put("transId", timeDeposit.getTransId());

			/* Add by long_zg 2015-05-22 UAT6-242 缁楊兛绗侀懓鍛扮秮鐠╃惤perator閹存劕濮涢棆浣哄繁鐏忔唶ave as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_NEW_TIME_DEPOSIT, mailUser.split(";"), dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_NEW_TIME_DEPOSIT, mailUser.split(";") ,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 缁楊兛绗侀懓鍛扮秮鐠╃惤perator閹存劕濮涢棆浣哄繁鐏忔唶ave as template end */
			
			
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("err.txn.TranscationFaily", e);
			if (e instanceof NTBException) {
				throw (NTBException)e;
			} else {
				throw new NTBException("err.txn.TranscationFaily");
			}
		}
		
		//add by lzg 20190820
		if("3".equals(corpType)){
			List processList = flowEngineService.loadProcess(processId);
			FlowProcess currentFlowProcess = (FlowProcess) processList.get(0);
			flowEngineService.cancelProcess(processId, getUser());
			String checkFlag = getParameter("checkFlag");
			if("C".equals(checkFlag)){
				String otp = getParameter("otp");
				String smsFlowNo = getParameter("smsFlowNo");
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
				String securityCode = getParameter("showSecurityCode");
				checkSecurityCode(securityCode);
			}
			timeDepositService.addTiemDeposit(timeDeposit);
			try{
				approve(currentFlowProcess.getTxnType(), currentFlowProcess.getTransNo(), this);
			}catch (NTBHostException e) {
				timeDepositService.removeTransfer("time_deposit", currentFlowProcess.getTransNo());
				throw new NTBHostException(e.getErrorArray());
			}catch (NTBException e) {
				timeDepositService.removeTransfer("time_deposit", currentFlowProcess.getTransNo());
				throw new NTBException(e.getErrorCode());
			}
		}
		if(!"3".equals(corpType)){
			try{
			timeDepositService.addTiemDeposit(timeDeposit);
			}catch (Exception e) {
				flowEngineService.cancelProcess(processId, getUser());
				throw new NTBException("err.txn.TranscationFaily");
			}
		}
		//add by lzg end
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
	
	public void addTimeDepositCancel() throws NTBException {
		Map resultData = this.getResultData();
		ApplicationContext appContext = Config.getAppContext();
		/*TransferService transferService = (TransferService) appContext.getBean("TransferService");
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));*/
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", timeDepositService.loadAccount(corpUser.getCorpId()));
		
		setResultData(resultData);
	}

	public void listTimeDepositWithdrawal() throws NTBException {
		// 闁跨喐鏋婚幏鐑芥晸閻偆鈹栫喊澶嬪 ResultData 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔虹哺閹惧懏瀚归柨鐔告灮閹凤拷
		/*setResultData(new HashMap(1));
		this.clearUsrSessionDataValue();

		ApplicationContext appContext = Config.getAppContext();
		AccountEnqureService accountEnqureService = (AccountEnqureService) appContext.getBean("AccountEnqureService");
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171106
		CorpUser corpUser = (CorpUser) this.getUser();
		corpUser.setLanguage(lang);//add by linrui for mul-language
		Map tdDetial = null;
		Map tdListMap = null;
		String accountNo = "";
		String period = "";

//		List timeDepositList = accountEnqureService.listTimeDeposit(corpUser, null);
		List timeDepositList = accountEnqureService.listTimeDeposit(corpUser, corpUser.getCorpId(),CibTransClient.APPCODE_TIME_DEPOSIT,null);//mod by linrui 20180226
		timeDepositList.remove(timeDepositList.size()-1);//add by linrui 20180227
		//add by lzg 20190701
		List timeDepositListTemp = new ArrayList();
		List availableCurrenciesList = accountEnqureService.getAvailableCurrencies();
		for(int i = 0;i < timeDepositList.size(); i++){
			Map retMap = (HashMap)timeDepositList.get(i);
			for(int j = 0; j < availableCurrenciesList.size(); j++){
				Map availableCcyMap = (HashMap)availableCurrenciesList.get(j);
				if(!availableCcyMap.get("CCY_CODE").equals(retMap.get("CURRENCY_CODE"))){
					continue;
				}
				timeDepositListTemp.add(timeDepositList.get(i));
				break;
			}
		}
		timeDepositList = timeDepositListTemp;
		//add by lzg end
		List openList = new ArrayList();
		RcCurrency rc = new RcCurrency();
		CorpUser user = (CorpUser) getUser();//add by linrui for mul-language
		user.setLanguage(lang);//end
		for (int i = 0; i < timeDepositList.size(); i++) {
			tdListMap = (Map) timeDepositList.get(i);
			//modify by linrui 20180227 闁稿繐鐗嗛獮鎾诲箳婢跺鐟撻梻鍫涘灮濞堟垿宕氶妶鍡樼劷
//			if (tdListMap.get("ACCOUNT_STATUS").toString().trim().equals("1")) {
				tdListMap.put("MATURITY_DATE", tdListMap.get("MATURITY_DATE").toString().trim());
				accountNo = tdListMap.get("ACCOUNT_NO").toString();
				tdDetial = accountEnqureService.viewDepositDetial(user, accountNo);//mod by linrui for mul-language
				period = tdDetial.get("INT_FREQUENCY").toString();// + tdDetial.get("REN_PERIOD").toString();
//				tdListMap.put("period", accountEnqureService.formatPeriod(period));
//				tdListMap.put("period", timeDepositService.getPeriodDescription(period));
				tdListMap.put("currency", rc.getCcyByCbsNumber(tdListMap.get("CURRENCY_CODE").toString().trim()));
				openList.add(tdListMap);
//			}
		}
		String sortType = Utils.null2EmptyWithTrim(this.getParameter("sortType"));
		if(sortType.equals("M")) {
			Sorting.sortMapList(openList,
					new String[]{"MATURITY_DATE", "ACCOUNT_NO"}, Sorting.SORT_TYPE_ASC);
		} else {
			Sorting.sortMapList(openList,
					new String[]{"currency", "ACCOUNT_NO"}, Sorting.SORT_TYPE_ASC);
		}

		Map resultData = new HashMap();
		resultData.put("timeDepositList", openList);
		resultData.put("sortType", sortType);
		this.setResultData(resultData);*/
		
		
		/*mod by linrui 20190815*/
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171106
		CorpUser user = (CorpUser) this.getUser();
        user.setLanguage(lang);//add by linrui for mul-language 20171106
		AccountEnqureService accountEnqureService = (AccountEnqureService) Config
				.getAppContext().getBean("AccountEnqureService");
		TimeDepositService timeDepositService = (TimeDepositService) Config
		        .getAppContext().getBean("TimeDepositService");
		//add by wcc 20180203
		//keyAll閻庢稒蓱閺備線骞嶉敓鑺ョ畳闂佹鍠栭敓浠嬪触閸絾宕抽柡澶堝劤濞堟垹锟藉Δ鍐惧剨濞戞搫鎷�
		String keyAll = Utils.null2EmptyWithTrim(this.getParameter("keyAll"));
		//閻犙冾儑缁増绋夋繝浣侯伇濡炪倗鏁稿▓鎴︽煥椤旂》鎷�
		String prePage = Utils.null2EmptyWithTrim(this.getParameter("prePage"));
		//閻犙冾儑缁増绋夌�ｂ晝顏卞銈囨暩濞堟垿鏌ㄩ纭锋嫹
		String nextPage = Utils.null2EmptyWithTrim(this.getParameter("nextPage"));
		//闁告帇鍊栭弻鍥倷閻熸澘姣婂ù婊冩缁楀倹绋夐敓濮愶拷閺夆晜蓱濡插憡绋夌�ｂ晝顏卞銈忔嫹
		String flagPage = Utils.null2EmptyWithTrim(this.getParameter("flagPage"));
		// monify by wcc 20180115 濠⒀呭仜婵偤宕楅崼锝囩闁告艾楠搁崵顓㈡儍閸曨剛鍨絪tack
		Stack<String> stackPage = new Stack<String>();
		//keyPage閻犲洢鍎查弳鐔虹磼閸曨厽鏆忛柡澶堝劚閻°劑寮ㄧ欢濯媑ekey缂傚牆顭烽妴澶愭儍閸曨垱鏆涢柛濠忔嫹
		String[] keyPage = new String[]{};
		//闁告帇鍊栭弻鍥及椤栨碍鍎婇柡鍕靛灣椤戝洦绋夐敓濮愶拷
		if("".equals(keyAll)){
			stackPage.push(prePage);
		}else{
			 if(keyAll.substring(keyAll.length()-1,keyAll.length()).equals(",")){
			    	keyAll = keyAll+" ";
			    }
			    keyPage = keyAll.split(",");
			   
			  for(int i=0;i<keyPage.length;i++){
				  stackPage.push(keyPage[i]);
			  }
		}
		//flagPage闁哄鍎遍崹浠嬪棘椤撶偛绔鹃柡宥呯墳缁辨繈宕欓悜妯煎灲	闁汇劌瀚弻鐔煎触閿燂拷
		if("N".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//闁绘劗鎳撻崵顔界▔鐎ｂ晝顏卞銈呯仛濡炲倿鏁嶇仦鐣屾闁哄秴鐗呴懙鎴炵▔鐎ｂ晝顏卞銈囨暩濞堟垿鏌ㄩ纭锋嫹閻犙冾儏閿熺晫绱掑▎搴ｇ憪濞戞搫鎷烽妴澶愭晬瀹�锟戒化闁告垼顔婄粭鍌涚▔閿熷锟介柡鍐啇缁辨繄浜搁崱妯煎灲濞戞搩鍘虹粭鍌涚▔閿熷锟介柣銊ュ閺侇參宕愰懝鎵偞闁稿﹪妫跨粭鍌涚▔閿熷锟�
			prePage = nextPage;
		}else if("".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//闁绘劗鎳撻崵顔界▔鐎ｂ晝顏卞銈呯仛濡炲倿鏁嶇仦鐣屾闁哄秴鐗呴懙鎴炵▔鐎ｂ晝顏卞銈囨暩濞堟垿鏌ㄩ纭锋嫹閻犙冾儏閿熺晫绱掑▎搴ｇ憪濞戞搫鎷烽妴澶愭晬瀹�锟戒化闁告垼顔婄粭鍌涚▔閿熷锟介柡鍐啇缁辨繄浜搁崱妯煎灲濞戞搩鍘虹粭鍌涚▔閿熷锟介柣銊ュ閺侇參宕愰懝鎵偞闁稿﹪妫跨粭鍌涚▔閿熷锟�
			prePage = nextPage;
		}else if("P".equals(flagPage)){
			//闁绘劗鎳撻崵顔界▔婵犱胶顏卞銈呯仛濡炲倿鏁嶇仦钘夊弗閻忓繐妫楅妵鏃�绋夐妶鍕殝闂佹鍠栭敓锟�
			stackPage.pop();
			stackPage.pop();
			//闁瑰嘲鐏濋崺宀�绱欐繝姘ワ拷闁汇劌瀚伴弫顓㈠磹閿燂拷
			prePage = stackPage.pop();
			stackPage.push(prePage);
		}
		//modify by wcc 20180209
		/*List timeDepositList = accountEnqureService.listTimeDeposit(user, user
				.getCorpId());*/
		List timeDepositList = accountEnqureService.listTimeDeposit(user, user.getCorpId(),CibTransClient.APPCODE_TIME_DEPOSIT,prePage);
		//modify by wcc 20180115 闁瑰嘲鐏濋崵锟絅EXT_KEY"闁汇劌瀚伴弫顓㈠磹閿燂拷
		nextPage = timeDepositList.get(timeDepositList.size()-1).toString();
		
		//disableN,disableP闁活枌鍔嶅鐢稿礆閵堝棙鐒藉☉鎾筹梗缁斿瓨銇勭喊澶岀濞戞挸顑勭粩瀛樸亜閸偄鐦婚梺绛嬪枟濡叉悂宕ラ敂鑺モ枖缂侊拷浜介敓锟�
		String disableN = "";
		if(!"".equals(nextPage)&&!"".equals(nextPage.trim())){
			 disableN = "button";
		}else{
			 disableN = "hidden";
		}
		String disableP = "";
		if(!"".equals(prePage)){
			disableP = "button";
		}else{
			disableP = "hidden";
		}
		//modify by wcc 20180115 remove闁瑰搫顦诲﹢鐚tCurrentPostingStmt闁哄倽顬冪涵鑸电▔椤撶儐鏉婚柛鏃傚Х濞堟唲erPage閻庣數顢婇挅锟�
		timeDepositList.remove(timeDepositList.size()-1);
		//閻忓繐妫楃欢閬嶅礆閹殿喗鐣遍柡鍌涘濞堟垿鏌ㄩ纭锋嫹闁规亽鍎冲鍐储鐎ｎ亜寮抽柡宥忔嫹
		
		//add by lzg 20190701
		List timeDepositListTemp = new ArrayList();
		List availableCurrenciesList = accountEnqureService.getAvailableCurrencies();
		for(int i = 0;i < timeDepositList.size(); i++){
			Map retMap = (HashMap)timeDepositList.get(i);
			for(int j = 0; j < availableCurrenciesList.size(); j++){
				Map availableCcyMap = (HashMap)availableCurrenciesList.get(j);
				if(!availableCcyMap.get("CCY_CODE").equals(retMap.get("CURRENCY_CODE"))){
					continue;
				}
				timeDepositListTemp.add(timeDepositList.get(i));
				break;
			}
		}
		timeDepositList = timeDepositListTemp;
		//add by lzg end
		
		stackPage.push(nextPage);
		//闁烩懇濞峵ringBuffer闁哄鍎茬敮鎾绩閸洘鏆涢柛濠呭亹濞堟垹锟藉Δ鍐惧剨濞戞捁顕滅槐婵囩閵夆晪鎷烽柛娆忓槻閸ㄥ酣姊鹃弮鎾剁闁告ê顭峰▍搴ㄥ嫉閿熻姤鍊靛☉鎿勬嫹闁叉粍寰勫顐ょ▏闁汇劌瀚伴敓浠嬪矗閺嬵偓鎷�
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<stackPage.size();i++){
			sb.append(stackPage.get(i)).append(",");
		}
		sb.replace(sb.length()-1, sb.length(),"");
		keyAll = sb.toString();
		
		// 鐠ㄣ劑鏁撻懘姘枦閹风兘鏁撶徊锟�
		// modify by hjs 2006-09-01
		Map timeDeposit = null;
		Map tdDetial = null;
		String period = "";
		List openList = new ArrayList();
		RcCurrency rc = new RcCurrency();
		for (int i = 0; i < timeDepositList.size(); i++) {

			// hjs: set period
			timeDeposit = (Map) timeDepositList.get(i);
			//modify by wcc 20180209 闁稿繐鐗嗛獮鎾诲箳婢跺鐟撻梻鍫涘灮濞堟垿宕氶妶鍡樼劷
			//if (timeDeposit.get("ACCOUNT_STATUS").toString().trim().equals("1")) {

				timeDeposit.put("MATURITY_DATE", timeDeposit.get(
						"MATURITY_DATE").toString().trim());
				tdDetial = accountEnqureService.viewDepositDetial(
						(CorpUser) getUser(), timeDeposit.get("ACCOUNT_NO")
								.toString());
				period = Utils.prefixZero(tdDetial.get("INT_FREQUENCY").toString(),3);
//				period = tdDetial.get("INT_FREQUENCY").toString() + tdDetial.get("REN_PERIOD").toString();
				timeDeposit.put("PERIOD", timeDepositService.getPeriodDescription(period,Format.transferLang(lang.toString())));
				timeDeposit.put("currency", rc.getCcyByCbsNumber(timeDeposit.get("CURRENCY_CODE").toString().trim()));

				openList.add(timeDeposit);
			//}
		}
		String sortType = Utils.null2EmptyWithTrim(this.getParameter("sortType"));
		if(sortType.equals("M")) {
			Sorting.sortMapList(openList,
					new String[]{"MATURITY_DATE", "ACCOUNT_NO"}, Sorting.SORT_TYPE_ASC);
		} else {
			Sorting.sortMapList(openList,
					new String[]{"currency", "ACCOUNT_NO"}, Sorting.SORT_TYPE_ASC);
		}

		Map resultData = new HashMap();
		resultData.put("timeDepositList", openList);
		resultData.put("sortType", sortType);
		//modify by wcc 20180203
		resultData.put("nextPage",nextPage);
		resultData.put("prePage",prePage);
		resultData.put("disableN",disableN);
		resultData.put("disableP",disableP);
		resultData.put("keyAll",keyAll);
		setResultData(resultData);
	}

	public void withdrawTimeDepositLoad() throws NTBException {
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171106
		ApplicationContext appContext = Config.getAppContext();
		AccountEnqureService accountEnqureService = (AccountEnqureService) appContext.getBean("AccountEnqureService");
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");

		CorpUser corpUser = (CorpUser) getUser();
		corpUser.setLanguage(lang);//add by linrui for mul-language
		RcCurrency rcCcy = new RcCurrency();

		String accountNo = Utils.null2EmptyWithTrim(this.getParameter("accountNo"));

		// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹峰嘲褰囬柨鐔稿焻绾板瀚筪eposit detail闁跨喐鏋婚幏椋庣矎闁跨喐鏋婚幏閿嬩紖
//		Map tdDetial = accountEnqureService.viewDepositDetial(corpUser, accountNo);
		Map tdDetial = timeDepositService.viewDepositDetial(accountNo);//mod by linrui 20190526
		String productNo = Utils.prefixZero(tdDetial.get("PRD_TYPE").toString(), 3);
		//String currency = rcCcy.getCcyByCbsNumber(Utils.prefixZero(tdDetial.get("CUR").toString(), 3));
		String currency = Utils.prefixSpace(tdDetial.get("CUR").toString(),3);
//		String period = tdDetial.get("INT_FREQUENCY").toString() + tdDetial.get("REN_PERIOD").toString();
		String period = Utils.prefixZero(tdDetial.get("INT_FREQUENCY").toString(),3);
		Date valueDate = DateTime.getDateFromStr(tdDetial.get("OPEN_DATE").toString(), "yyyyMMdd"); // update by gan REN_DATE 
		Date maturityDate = DateTime.getDateFromStr(tdDetial.get("NEXT_MAT_DATE").toString(), "yyyyMMdd");
		String principal = tdDetial.get("ISSUE_AMOUNT").toString();
		
		// exchange rate
		Map exRateMap = exRatesService.getExchangeRate(corpUser.getCorpId(), currency, "MOP", 7);
		String rateType = exRateMap.get(ExchangeRatesDao.RATE_TYPE).toString();
		BigDecimal exRate = null;
		if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
			throw new NTBException("err.txn.NoSuchExchangeRate");
		} else {
			exRate = new BigDecimal(exRateMap.get(ExchangeRatesDao.BUY_RATE).toString());
		}
		//update by gan 20180122
		/*Map fromHost = timeDepositService.queryWithdrawInfoByHost(corpUser, currency, principal,
				valueDate, maturityDate, period, productNo);*/
		
		/*double netInterestAmt = new Double(fromHost.get("INTEREST").toString()).doubleValue();
		double netCreditAmount = new Double(principal).doubleValue() + netInterestAmt;*/
		double netInterest = new Double(tdDetial.get("INT_RATE").toString()).doubleValue();
		double penalty = new Double(tdDetial.get("INT_PEN").toString()).doubleValue();
		double netCreditAmount = new Double(tdDetial.get("CUR_BAL").toString()).doubleValue();
		
		
		Map resultData = new HashMap();
		resultData.put("accountNo", accountNo);
		resultData.put("currency", currency);
		//resultData.put("period", timeDepositService.getPeriodDescription(period,Format.transferLang(lang.toString())));
		resultData.put("period", period);
		resultData.put("productNo", productNo);
		resultData.put("valueDate", valueDate);
		resultData.put("maturityDate", maturityDate);
		resultData.put("PRINCIPAL", principal);
		resultData.put("interest", netInterest/*netInterestAmt*/);
		resultData.put("netCreditAmount", netCreditAmount);
		resultData.put("penalty", penalty);//add by linrui 20190526
		//add by linrui 20180313
		resultData.put("transferuser", timeDepositService.loadAccount(corpUser.getCorpId()));//ccy temp
		//end
		this.setResultData(resultData);
		this.setUsrSessionDataValue("fromHost", resultData);
		
	}

	public void withdrawTimeDeposit() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");

		CorpUser corpUser = (CorpUser) this.getUser();
		Map fromHost = (Map) this.getUsrSessionDataValue("fromHost");

		TimeDeposit timeDeposit = new TimeDeposit(String.valueOf(CibIdGenerator.getRefNoForTransaction()));
		timeDeposit.setUserId(corpUser.getUserId());
		timeDeposit.setCorpId(corpUser.getCorpId());
		timeDeposit.setStatus(Constants.STATUS_PENDING_APPROVAL);
		timeDeposit.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
		timeDeposit.setRequestTime(new Date());
		timeDeposit.setTdType(TimeDeposit.TD_TYPE_WITHDRAW);

		timeDeposit.setCurrentAccount(this.getParameter("currentAccount"));
		timeDeposit.setCurrentAccountCcy(this.getParameter("currentAccountCcy"));//add by linrui for mul-ccy
		timeDeposit.setTdAccount(fromHost.get("accountNo").toString());
		timeDeposit.setCurrency(fromHost.get("currency").toString());
		timeDeposit.setPrincipal(new Double(fromHost.get("PRINCIPAL").toString()));
		timeDeposit.setTerm(fromHost.get("period").toString());
		timeDeposit.setProductionNo(fromHost.get("productNo").toString());
		timeDeposit.setValueDate((Date) fromHost.get("valueDate"));
		timeDeposit.setMaturityDate((Date) fromHost.get("maturityDate"));
		timeDeposit.setInterest(new Double(fromHost.get("interest").toString()));
		//timeDeposit.setPenalty(new Double(fromHost.get("INTEREST_WITHHELD").toString())); update gzy
        timeDeposit.setInstCd(timeDepositService.getInCd(fromHost.get("accountNo").toString()));//add by linrui 20190525
        timeDeposit.setPenalty(new Double(fromHost.get("penalty").toString()));//add by linrui 20190526
        timeDeposit.setNetCreditAmount(new Double(fromHost.get("netCreditAmount").toString()));//add by linrui 20190526
		//add by linrui for cannot withdraw before expire
        //add by lzg 20190717
        /*Date date = DateTime.getHostTime();
        if(date.before(timeDeposit.getMaturityDate())){
        	throw new NTBException("err.txn.withdraw_pro");
        }*/
        //end
        if (!timeDepositService.isAuthorize(timeDeposit)) {
			throw new NTBException("err.txn.LastTransNotCompleted");
		}

		// 闁跨喐鏋婚幏椋庣矏assignuser_tag闁跨喐鏋婚幏鐑芥晸閿燂拷
		Map assignuser = new HashMap();
		BigDecimal amountMopEq = exRatesService.getEquivalent(corpUser.getCorpId(),
				timeDeposit.getCurrency(), "MOP",
				new BigDecimal(fromHost.get("netCreditAmount").toString()),
				null, 2);
		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "currency");
		assignuser.put("amountField", "netCreditAmount");
		assignuser.put("amountMopEqField", "amountMopEq");
		assignuser.put("txnType", Constants.TXN_SUBTYPE_WITHDRAWAL_DEPOSIT);
		assignuser.put("amountMopEq", amountMopEq);

		Map resultData = new HashMap();
		this.convertPojo2Map(timeDeposit, resultData);
		resultData.putAll(fromHost);
		// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风柉骞愰柨鐔虹ssignuser_tag
		resultData.putAll(assignuser);

		ApproveRuleService approveRuleService = (ApproveRuleService) Config
				.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(), resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}
		
		//add by lzg 20190610
		double netInterestAmt = timeDeposit.getNetCreditAmount();
		double netCreditAmt = timeDeposit.getPrincipal().doubleValue() + netInterestAmt - timeDeposit.getPenalty();
		resultData.put("netCreditAmount", netInterestAmt);
		resultData.put("netCreditAmt", String.valueOf(netCreditAmt));
		//add by lzg 20190820
		String corpType = corpUser.getCorporation().getCorpType();
		String checkFlag = corpUser.getCorporation().getAuthenticationMode();
		resultData.put("corpType", corpType);
		resultData.put("checkFlag", checkFlag);
		resultData.put("operationType", "send");
		resultData.put("showMobileNo", corpUser.getMobile());
		resultData.put("txnType", Constants.TXN_SUBTYPE_WITHDRAWAL_DEPOSIT);
		
		//add by lzg end
		//add by lzg end
		this.setResultData(resultData);

		this.setUsrSessionDataValue("timeDeposit", timeDeposit);
		this.setUsrSessionDataValue("assignuser", assignuser);
		this.setUsrSessionDataValue("fromHost", fromHost);

	}

	public void withdrawTimeDepositCfm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");
		CorpAccountService corpAccService = (CorpAccountService) appContext.getBean("CorpAccountService");
		MailService mailService = (MailService) appContext.getBean("MailService");
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext.getBean("CutOffTimeService");

		CorpUser corpUser = (CorpUser) this.getUser();

		Map fromHost = (Map) this.getUsrSessionDataValue("fromHost");

		// 闁跨喐鏋婚幏椋庣矏assignuser_tag闁跨喐鏋婚幏鐑芥晸閿燂拷
		Map assignuser = (Map) this.getUsrSessionDataValue("assignuser");
		String txnType = (String) assignuser.get("txnType");
		BigDecimal amountMopEq = (BigDecimal) assignuser.get("amountMopEq");

		TimeDeposit timeDeposit = (TimeDeposit) this.getUsrSessionDataValue("timeDeposit");

		// check value date cut-off time
//		setMessage(cutOffTimeService.check("ZJ37", "", corpAccService.getCurrency(timeDeposit.getCurrentAccount(),true)));
		setMessage(cutOffTimeService.check("ZJ37", "", timeDeposit.getCurrentAccountCcy()));//add by linrui formul-ccy

		String assignedUser = Utils.null2EmptyWithTrim(this.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this.getParameter("mailUser"));
//		String toCcy = corpAccService.getCurrency(timeDeposit.getCurrentAccount(),true);
		String toCcy = timeDeposit.getCurrentAccountCcy();//add by mul-ccy 20190326
		// 闁跨喖鎽弬銈嗗娑擄拷鏁撻弬銈嗗闁跨喐鏋婚幏閿嬫綀闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�
		String processId = flowEngineService.startProcess(txnType,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				TimeDepositAction.class,
				timeDeposit.getCurrency(),
				new Double(fromHost.get("netCreditAmount").toString()).doubleValue() +/*add by linrui 20190530*/ timeDeposit.getPrincipal(),
				toCcy,
				new Double(fromHost.get("netCreditAmount").toString()).doubleValue(),
				amountMopEq.doubleValue(),
				timeDeposit.getTransId(),
				null, getUser(), assignedUser,
				((CorpUser)getUser()).getCorporation().getAllowExecutor(),
				FlowEngineService.RULE_FLAG_FROM);
		
		Map resultData = new HashMap();
		String corpType = getParameter("corpType");
		resultData.put("corpType", corpType);
		try {
			timeDepositService.withdrawTiemDeposit(timeDeposit);

			this.convertPojo2Map(timeDeposit, resultData);
			
			//add by lzg 20190610
			double netInterestAmt = timeDeposit.getNetCreditAmount();
			double netCreditAmt = timeDeposit.getPrincipal().doubleValue() + netInterestAmt - timeDeposit.getPenalty();
			resultData.put("netCreditAmount", netInterestAmt);
			resultData.put("netCreditAmt", String.valueOf(netCreditAmt));
			//add by lzg end
			resultData.putAll(fromHost);
			resultData.putAll(assignuser);
			this.setResultData(resultData);

			//send mial to approver
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", timeDeposit.getRequestTime());
			dataMap.put("transId", timeDeposit.getTransId());

			/* Add by long_zg 2015-05-22 UAT6-242 缁楊兛绗侀懓鍛扮秮鐠╃惤perator閹存劕濮涢棆浣哄繁鐏忔唶ave as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_WITHDRAWAL_DEPOSIT, mailUser.split(";"), dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_WITHDRAWAL_DEPOSIT, mailUser.split(";") ,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 缁楊兛绗侀懓鍛扮秮鐠╃惤perator閹存劕濮涢棆浣哄繁鐏忔唶ave as template end */
			
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("err.txn.TranscationFaily", e);
			if (e instanceof NTBException) {
				throw (NTBException)e;
			} else {
				throw new NTBException("err.txn.TranscationFaily");
			}
		}
		
		//add by lzg 20190820
		if("3".equals(corpType)){
			List processList = flowEngineService.loadProcess(processId);
			FlowProcess currentFlowProcess = (FlowProcess) processList.get(0);
			flowEngineService.cancelProcess(processId, getUser());
			String checkFlag = getParameter("checkFlag");
			if("C".equals(checkFlag)){
				String otp = getParameter("otp");
				String smsFlowNo = getParameter("smsFlowNo");
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
				String securityCode = getParameter("showSecurityCode");
				checkSecurityCode(securityCode);
			}
			try{
				approve(currentFlowProcess.getTxnType(), currentFlowProcess.getTransNo(), this);
			}catch (NTBHostException e) {
				throw new NTBHostException(e.getErrorArray());
			}catch (NTBException e) {
				throw new NTBException(e.getErrorCode());
			}
		}
		//add by lzg end
		
	}

	public void withdrawTimeDepositCancel() throws NTBException {
	}

	public void viewTimeDeposit() throws NTBException {
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171106
		CorpUser user = (CorpUser) getUser();
		user.setLanguage(lang);//add by linrui for mul-language
		// 闁跨喐鏋婚幏鐑芥晸閻偆鈹栫喊澶嬪 ResultData 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔虹哺閹惧懏瀚归柨鐔告灮閹凤拷
		setResultData(new HashMap(1));
		this.clearUsrSessionDataValue();

		ApplicationContext appContext = Config.getAppContext();
		AccountEnqureService accountEnqureService = (AccountEnqureService) appContext.getBean("AccountEnqureService");
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");

		String accountNo = this.getParameter("tdAccountNo");
		// time deposit quiry
		Map hostTDDetial = accountEnqureService.viewDepositDetial(user, accountNo);//mod by linrui 20171106
		//update by gan 20180125
		String principal = hostTDDetial.get("ISSUE_AMOUNT").toString();
		String interest = hostTDDetial.get("NEXT_INT_AMT").toString();
//		String period = Utils.tinor2pe(hostTDDetial.get("INT_FREQUENCY").toString()); 
		String period = timeDepositService.getPeriodDescription(Utils.prefixZero(hostTDDetial.get("INT_FREQUENCY").toString(),3),Format.transferLang(lang.toString())); 
		double netCreditAmt = Double.parseDouble(hostTDDetial.get("CUR_BAL").toString()); //Double.parseDouble(principal) + Double.parseDouble(interest);
        //add by linrui 20180402
		double principalAmt = Double.parseDouble(principal);
		//hostTDDetial.put("period", accountEnqureService.formatPeriod(period));
		hostTDDetial.put("period", period);
//		hostTDDetial.put("netCreditAmt", String.valueOf(netCreditAmt));
		hostTDDetial.put("netCreditAmt", String.valueOf(netCreditAmt+principalAmt));//mod by linrui 20180402

		Map resultData = new HashMap();
		resultData.putAll(hostTDDetial);
		
		
		//add by linrui 20180320
		String interserRate = resultData.get("INT_RATE").toString();
		resultData.put("INT_RATE", Utils.dropZeroAfter(interserRate));
		//end
		
		//add by lzg 20191018
		Double defaultRate = new Double(interserRate);
		Double expectedInterest = getExpectedInterest(defaultRate,principalAmt,resultData.get("CUR").toString(),resultData.get("INT_FREQUENCY").toString());
		resultData.put("expectedInterest", expectedInterest);
		//add by lzg end
		
		//modified by lzg 20190814
		//resultData.put("instCd", timeDepositService.getInCd(accountNo));
		resultData.put("instCd", hostTDDetial.get("INST_CD"));
		//end
		resultData.put("interestAccured",netCreditAmt);//add by linrui 20190605
		
		setResultData(resultData);
	}
	
	public void viewTimeDepositforWithdrawl() throws NTBException {
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171106
		CorpUser user = (CorpUser) getUser();
		user.setLanguage(lang);//add by linrui for mul-language
		// 闁跨喐鏋婚幏鐑芥晸閻偆鈹栫喊澶嬪 ResultData 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔虹哺閹惧懏瀚归柨鐔告灮閹凤拷
		setResultData(new HashMap(1));
		this.clearUsrSessionDataValue();

		ApplicationContext appContext = Config.getAppContext();
		AccountEnqureService accountEnqureService = (AccountEnqureService) appContext.getBean("AccountEnqureService");
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");

		String accountNo = this.getParameter("tdAccountNo");
		// time deposit quiry
		Map hostTDDetial = accountEnqureService.viewDepositDetial(user, accountNo);//mod by linrui 20171106
		//update by gan 20180125
		String principal = hostTDDetial.get("ISSUE_AMOUNT").toString();
		String interest = hostTDDetial.get("NEXT_INT_AMT").toString();
//		String period = Utils.tinor2pe(hostTDDetial.get("INT_FREQUENCY").toString()); 
		String period = timeDepositService.getPeriodDescription(Utils.prefixZero(hostTDDetial.get("INT_FREQUENCY").toString(),3),Format.transferLang(lang.toString())); 
		double netCreditAmt = Double.parseDouble(hostTDDetial.get("CUR_BAL").toString()); //Double.parseDouble(principal) + Double.parseDouble(interest);
        //add by linrui 20180402
		double principalAmt = Double.parseDouble(principal);
		//hostTDDetial.put("period", accountEnqureService.formatPeriod(period));
		hostTDDetial.put("period", period);
//		hostTDDetial.put("netCreditAmt", String.valueOf(netCreditAmt));
		hostTDDetial.put("netCreditAmt", String.valueOf(netCreditAmt+principalAmt));//mod by linrui 20180402

		Map resultData = new HashMap();
		resultData.putAll(hostTDDetial);
		//add by linrui 20180320
		String interserRate = resultData.get("INT_RATE").toString();
		resultData.put("INT_RATE", Utils.dropZeroAfter(interserRate));
		//end
		//modified by lzg 20190814
		//resultData.put("instCd", timeDepositService.getInCd(accountNo));
		resultData.put("instCd", hostTDDetial.get("INST_CD"));
		//end
		resultData.put("interestAccured",netCreditAmt);//add by linrui 20190605
		
		setResultData(resultData);
	}

	public boolean approve(String txnType, String id, CibAction bean) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		CorpUser corpUser = (CorpUser) bean.getUser();
		TimeDeposit timeDeposit = timeDepositService.viewTimeDeposit(id);

		txnType = Utils.null2EmptyWithTrim(txnType);
		if (txnType.equals(Constants.TXN_SUBTYPE_NEW_TIME_DEPOSIT)) {

			// 闁岸鏁撻弬銈嗗闁跨喓绮搁悮瀛樺閸欐牠鏁撻惌顐＄串閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�(fromAccountCcy)
//			String currentAccountCcy = corpAccountService.getCurrency(timeDeposit.getCurrentAccount(),true);
			String currentAccountCcy = timeDeposit.getCurrentAccountCcy();

			// 闁岸鏁撻弬銈嗗妞ょ敻鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚规禍鈺呮晸閺傘倖瀚归柨鐔告灮閹烽攱鎸勫▽銈夋晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗閹枫儵鏁撶憴鎺戝簻閹峰嘲鐫呴柨鐔兼▉椤忓孩瀚归柨鐔告灮閹烽攱鍘遍柨鐔告灮閹凤拷
			String currentAccPrincipal = exRatesService.getEquivalent(
					corpUser.getCorpId(),
					currentAccountCcy,
					timeDeposit.getCurrency(),
					null,
					new BigDecimal(timeDeposit.getPrincipal().toString()),
					2).toString();

			BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser.getCorpId(),
					currentAccountCcy, "MOP",
					new BigDecimal(currentAccPrincipal),
					null, 2);
			// check Limit
			if (!transferLimitService.checkLimitQuota(timeDeposit.getCurrentAccount(),
					corpUser.getCorpId(),
					Constants.TXN_TYPE_TIME_DEPOSIT,
					new Double(currentAccPrincipal).doubleValue(),
					equivalentMOP.doubleValue())) {

				// write limit report
	        	Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("USER_ID", timeDeposit.getUserId());
				reportMap.put("CORP_ID", timeDeposit.getCorpId());
				reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
				reportMap.put("LOCAL_TRANS_CODE", timeDeposit.getTransId());
				reportMap.put("FROM_ACCOUNT", timeDeposit.getCurrentAccount());
				reportMap.put("FROM_CURRENCY", currentAccountCcy);
				reportMap.put("TO_CURRENCY", timeDeposit.getCurrency());
				reportMap.put("FROM_AMOUNT", currentAccPrincipal);
				reportMap.put("TO_AMOUNT", timeDeposit.getPrincipal());
				reportMap.put("EXCEEDING_TYPE", "1");
				reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
				reportMap.put("USER_LIMIT ", "0");
				reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
				reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

				throw new NTBWarningException("err.txn.ExceedDailyLimit");
			}
			//CorpUser user = (CorpUser)bean.getUser();
			// approve
			String newTdAccount = timeDepositService.approveNewTDAccout(timeDeposit, (CorpUser)bean.getUser(), equivalentMOP);//mod by linrui for mul-language
			// refresh account info in cache
			CachedDBRCFactory.addPendingCache("accountByUser");

			Map resultData = bean.getResultData();
			resultData.put("newTdAccount", newTdAccount);
			this.convertPojo2Map(timeDeposit, resultData);
			bean.setResultData(resultData);

			return true;
		} else if (txnType.equals(Constants.TXN_SUBTYPE_WITHDRAWAL_DEPOSIT)) {
			// approve
			timeDepositService.approveWithdrawTDAccout(timeDeposit, (CorpUser) bean.getUser());
			Map resultData = bean.getResultData();
			this.convertPojo2Map(timeDeposit, resultData);
			bean.setResultData(resultData);
			// refresh account info in cache
			CachedDBRCFactory.addPendingCache("accountByUser");
			return true;
		} else {
			return false;
		}
	}

	public boolean reject(String txnType, String id, CibAction bean) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");

		txnType = Utils.null2EmptyWithTrim(txnType);
		if (txnType.equals(Constants.TXN_SUBTYPE_NEW_TIME_DEPOSIT)) {
			timeDepositService.rejectNewTDAccout(id, (CorpUser) bean.getUser());
			return true;
		} else if (txnType.equals(Constants.TXN_SUBTYPE_WITHDRAWAL_DEPOSIT)) {
			timeDepositService.rejectWithdrawTDAccout(id, (CorpUser) bean.getUser());
			return true;
		} else {
			return false;
		}
	}

	public String viewDetail(String txnType, String id, CibAction bean)	throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");

		String viewPageUrl = "";
		Map resultData = bean.getResultData();
		TimeDeposit timeDeposit = timeDepositService.viewTimeDeposit(id);

		CorpUser corpUser = (CorpUser) bean.getUser();

		// 闁跨喐鏋婚幏椋庣矏assignuser_tag闁跨喐鏋婚幏鐑芥晸閿燂拷
		Map assignuser = new HashMap();
		assignuser.put("txnTypeField", "txnType");
		assignuser.put("amountMopEqField", "amountMopEq");

		txnType = Utils.null2EmptyWithTrim(txnType);
		if (txnType.equals(Constants.TXN_SUBTYPE_NEW_TIME_DEPOSIT)) {

			// 闁岸鏁撻弬銈嗗闁跨喓绮搁悮瀛樺閸欐牠鏁撻惌顐＄串閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�(fromAccountCcy)
			/*String currentAccount = timeDeposit.getCurrentAccount();
			CorpAccount corpAccount = corpAccountService
					.viewCorpAccount(currentAccount);
			String currentAccountCcy = corpAccount.getCurrency();*/
			String currentAccountCcy = timeDeposit.getCurrentAccountCcy();
			// 闁岸鏁撻弬銈嗗妞ょ敻鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚规禍鈺呮晸閺傘倖瀚归柨鐔告灮閹烽攱鎸勫▽銈夋晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗閹枫儵鏁撶憴鎺戝簻閹峰嘲鐫呴柨鐔兼▉椤忓孩瀚归柨鐔告灮閹烽攱鍘遍柨鐔告灮閹凤拷
			String currentAccPrincipal = exRatesService.getEquivalent(
					corpUser.getCorpId(), currentAccountCcy,
					timeDeposit.getCurrency(), null,
					new BigDecimal(timeDeposit.getPrincipal().toString()), 2)
					.toString();

			// 闁跨喐鏋婚幏椋庣矏assignuser_tag闁跨喐鏋婚幏鐑芥晸閿燂拷
			BigDecimal amountMopEq = exRatesService.getEquivalent(corpUser.getCorpId(),
					timeDeposit.getCurrency(), "MOP",
					new BigDecimal(timeDeposit.getPrincipal().toString()), null, 2);
			assignuser.put("currencyField", "currency");
			assignuser.put("amountField", "principal");
			assignuser.put("txnType", txnType);
			assignuser.put("amountMopEq", amountMopEq);

			resultData.put("currentAccountCcy", currentAccountCcy);
			resultData.put("currentAccPrincipal", currentAccPrincipal);
			//add by lzg 20191018
			Double defaultRate = new Double(timeDeposit.getDefaultRate());
			Double expectedInterest = getExpectedInterest(defaultRate,new Double(currentAccPrincipal),currentAccountCcy,timeDeposit.getTerm());
			
			assignuser.put("expectedInterest", expectedInterest);
			resultData.put("expectedInterest", expectedInterest);
			//add by lzg end
			// resultData.putAll(fromHost);

			viewPageUrl = "/WEB-INF/pages/txn/time_deposit/time_deposit_add_view.jsp";

		} else if (txnType.equals(Constants.TXN_SUBTYPE_WITHDRAWAL_DEPOSIT)) {

			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚筃et Interest Amount
			double interest = timeDeposit.getInterest().doubleValue();
			//update by gan 20180123
			//double interestWithHeld = timeDeposit.getPenalty().doubleValue();
			double interestWithHeld = 0.0;
			//modified by lzg 20190610
			/*double netInterestAmt = (interest - interestWithHeld > 0) ? interest - interestWithHeld : 0;
			double netCreditAmt = timeDeposit.getPrincipal().doubleValue()
					+ netInterestAmt;*/
			double netInterestAmt = timeDeposit.getNetCreditAmount();//(interest - interestWithHeld > 0) ? interest - interestWithHeld : 0;
			double netCreditAmt = timeDeposit.getPrincipal().doubleValue() + netInterestAmt - timeDeposit.getPenalty();
			//modified by lzg end
			BigDecimal amountMopEq = exRatesService.getEquivalent(corpUser.getCorpId(),
					timeDeposit.getCurrency(), "MOP",
					new BigDecimal(String.valueOf(netCreditAmt)),
					null, 2);
			assignuser.put("currencyField", "currency");
			assignuser.put("amountField", "netCreditAmt");
			assignuser.put("txnType", txnType);
			assignuser.put("amountMopEq", amountMopEq);
			
			resultData.put("netInterestAmt", new Double(netInterestAmt));
			resultData.put("netCreditAmt", new Double(netCreditAmt));

			viewPageUrl = "/WEB-INF/pages/txn/time_deposit/time_deposit_withdraw_view.jsp";
		}
		// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风柉骞愰柨鐔虹ssignuser_tag
		this.convertPojo2Map(timeDeposit, resultData);
		// resultData.put("requestTime", timeDeposit.getRequestTime());
		resultData.putAll(assignuser);
		bean.setResultData(resultData);

		return viewPageUrl;
	}
	public void listLoad() throws NTBException {
		// 闁跨喐鏋婚幏鐑芥晸閻偆鈹栫喊澶嬪 ResultData 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔虹哺閹惧懏瀚归柨鐔告灮閹凤拷
		String date_range = "all" ;
		Map newMap = new HashMap() ;
		newMap.put("date_range", date_range) ;
		setResultData(newMap);
	}
	public void listHistory() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");
		CorporationService corpService = (CorporationService) appContext.getBean("CorporationService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");

		CorpUser corpUser = (CorpUser) this.getUser();
		String date_range = Utils.null2EmptyWithTrim(this.getParameter("date_range"));
		String dateFrom = Utils.null2EmptyWithTrim(this.getParameter("dateFrom"));
		String dateTo = Utils.null2EmptyWithTrim(this.getParameter("dateTo"));

		List historyList = timeDepositService.lsitHistory(corpUser.getCorpId(), corpUser.getUserId(), dateFrom, dateTo);
		historyList = this.convertPojoList2MapList(historyList);

		Map tmpMap = null;
		Corporation corp = corpService.view(corpUser.getCorpId());
		double tmpInterest = 0;
		for (int i=0; i<historyList.size(); i++) {
			tmpMap = (Map) historyList.get(i);
			// count net amount
			if (tmpMap.get("tdType").toString().equals(TimeDeposit.TD_TYPE_WITHDRAW)) {
			/*	tmpInterest = new Double(tmpMap.get("interest").toString()).doubleValue()
							- new Double(tmpMap.get("penalty").toString()).doubleValue();
				tmpInterest = tmpInterest >= 0 ? tmpInterest : 0;
				tmpMap.put("principal",
						String.valueOf(
								new Double(tmpMap.get("principal").toString()).doubleValue() + tmpInterest
						)
				);*/
			}
			// set change flag
			if ("Y".equals(corp.getAllowApproverSelection())) {
				if (Constants.STATUS_PENDING_APPROVAL.equals(tmpMap.get("status").toString())) {
					if (flowEngineService.checkApproveComplete(
							timeDepositService.tdType2TxnType(tmpMap.get("tdType").toString()),
							tmpMap.get("transId").toString())) {
						tmpMap.put("changeFlag", "N");
					} else {
						tmpMap.put("changeFlag", "Y");
						// set txnType for changing approver
						tmpMap.put("txnType", timeDepositService.tdType2TxnType(tmpMap.get("tdType").toString()));
					}
				}
			}
		}

		Map resultData = new HashMap();
		resultData.put("date_range", date_range);
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("historyList", historyList);
		this.setResultData(resultData);
	}

	public void viewNewHistory() throws NTBException {
		// 闁跨喐鏋婚幏鐑芥晸閻偆鈹栫喊澶嬪 ResultData 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔虹哺閹惧懏瀚归柨鐔告灮閹凤拷
		setResultData(new HashMap(1));
		this.clearUsrSessionDataValue();

		ApplicationContext appContext = Config.getAppContext();
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");

		CorpUser corpUser = (CorpUser) this.getUser();

		String transId = Utils.null2EmptyWithTrim(this.getParameter("transId"));

		TimeDeposit timeDeposit = timeDepositService.viewTimeDeposit(transId);

		// 闁岸鏁撻弬銈嗗闁跨喓绮搁悮瀛樺閸欐牠鏁撻惌顐＄串閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�(fromCcy)
//		String currentAccountCcy = corpAccountService.getCurrency(timeDeposit.getCurrentAccount(),true);
		String currentAccountCcy = timeDeposit.getCurrentAccountCcy();//add by linrui for mul-ccy

		// 闁岸鏁撻弬銈嗗妞ょ敻鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚规禍鈺呮晸閺傘倖瀚归柨鐔告灮閹烽攱鎸勫▽銈夋晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗閹枫儵鏁撶憴鎺戝簻閹峰嘲鐫呴柨鐔兼▉椤忓孩瀚归柨鐔告灮閹烽攱鍘遍柨鐔告灮閹凤拷
		String currentAccPrincipal = exRatesService.getEquivalent(
				corpUser.getCorpId(),
				currentAccountCcy,
				timeDeposit.getCurrency(),
				null,
				new BigDecimal(timeDeposit.getPrincipal().toString()),
				2).toString();

		Map resultData = new HashMap();
		resultData.put("currentAccountCcy", currentAccountCcy);
		resultData.put("currentAccPrincipal", currentAccPrincipal);
		//add by lzg 20191018
		Double defaultRate = new Double(timeDeposit.getDefaultRate());
		Double expectedInterest = getExpectedInterest(defaultRate,new Double(currentAccPrincipal),currentAccountCcy,timeDeposit.getTerm());
		resultData.put("expectedInterest", expectedInterest);
		//add by lzg end
		this.convertPojo2Map(timeDeposit, resultData);
		this.setResultData(resultData);
	}

	public void viewWithdrawalHistory() throws NTBException {
		// 闁跨喐鏋婚幏鐑芥晸閻偆鈹栫喊澶嬪 ResultData 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔虹哺閹惧懏瀚归柨鐔告灮閹凤拷
		setResultData(new HashMap(1));
		this.clearUsrSessionDataValue();

		ApplicationContext appContext = Config.getAppContext();
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");

		String transId = Utils.null2EmptyWithTrim(this.getParameter("transId"));

		TimeDeposit timeDeposit = timeDepositService.viewTimeDeposit(transId);
		double interest = timeDeposit.getInterest().doubleValue();
//		double penalty = timeDeposit.getPenalty().doubleValue();
		double principal = timeDeposit.getPrincipal().doubleValue();
//		double netInterestAmt = (interest - penalty > 0) ? interest - penalty : 0;
		double netCreditAmount = principal ;//+ netInterestAmt;
		//add by linrui 20190525 begin
		double interestWithHeld = 0.0;
		double netInterestAmt = timeDeposit.getNetCreditAmount();//(interest - interestWithHeld > 0) ? interest - interestWithHeld : 0;
		double netCreditAmt = timeDeposit.getPrincipal().doubleValue() + netInterestAmt - timeDeposit.getPenalty();
		//end

		Map resultData = new HashMap();
//		resultData.put("netInterestAmt", String.valueOf(netInterestAmt));
		resultData.put("netCreditAmount", netInterestAmt);
		resultData.put("netCreditAmt", String.valueOf(netCreditAmt));//add by linrui 20190525
		this.convertPojo2Map(timeDeposit, resultData);
		this.setResultData(resultData);
	}

	public boolean cancel(String txnType, String id, CibAction bean) throws NTBException {
		return reject(txnType, id, bean);
    }
}
