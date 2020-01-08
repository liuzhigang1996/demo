/**
 *
 */
package app.cib.action.txn;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import app.cib.bo.bnk.Corporation;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.BillPayment;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.service.bnk.CorporationService;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.sys.ApproveRuleService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.CutOffTimeService;
import app.cib.service.sys.MailService;
import app.cib.service.txn.BillPaymentService;
import app.cib.service.txn.TransferLimitService;
import app.cib.util.Constants;
import app.cib.util.TransAmountFormate;
import app.cib.util.UploadReporter;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Amount;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.exception.NTBWarningException;

/**
 * @author hjs 2006-07-22
 */
public class BillPaymentAction extends CibAction implements Approvable {

	public void generalPaymentLoad() throws NTBException {

		setResultData(new HashMap(1));

		Map resultData = new HashMap();
		setResultData(resultData);
	}

	public void generalPaymentCheck() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = (CorpUser) this.getUser();
        corpUser.setLanguage(lang);//add by linrui for mul-language
		BillPayment billPayment = new BillPayment(String.valueOf(CibIdGenerator.getRefNoForTransaction()));
		this.convertMap2Pojo(this.getParameters(), billPayment);
		// set Bill No.
		String otherBillNo = this.getParameter("otherBillNo");
		if (otherBillNo != null) {
			billPayment.setBillNo1(Utils.null2EmptyWithTrim(otherBillNo));
		}
		Double transferAmount = null;
		String currency = "";

		// Get Infomation from Host
		Map fromHost = null;
		String inputAmountFlag = "";
		if (billPayment.getCategory().equals("010")&&sepcialMerchant(billPayment.getMerchant())) {
			if (billPayment.getMerchant().equals("CEM")) {
				fromHost = billPaymentService.getCEMBillInfo(corpUser,billPayment.getBillNo1());
			} else if (billPayment.getMerchant().equals("SAAM")) {
				fromHost = billPaymentService.getSAAMBillInfo(corpUser,billPayment.getBillNo1());
			} else if (billPayment.getMerchant().equals("CTM")) {
				fromHost = billPaymentService.getCTMBillInfo(corpUser,billPayment.getBillNo1());
			}
			transferAmount = Double.valueOf(fromHost.get("AMOUNT_DUE").toString());
			currency = billPaymentService.getMerchantBill(
					billPayment.getCategory(), billPayment.getMerchant()).getSuspendAccCcy();

			fromHost.put("EXPIRY_DATE", fromHost.get("BILLING_DATE"));
			inputAmountFlag = "0";
		} else {
			String acc = billPaymentService.checkOtherMerchant(billPayment.getCategory(), billPayment.getMerchant());
			if(acc == null){
				fromHost = billPaymentService.getGenPaymentInfo(
						corpUser,
						billPayment.getCategory(),
						billPayment.getMerchant(),
						billPayment.getBillNo1());
				transferAmount = Double.valueOf(fromHost.get("PAYMENT_AMOUNT").toString());
				currency = fromHost.get("BILL_CURRENCY").toString();
				inputAmountFlag = "0";
			} else {
				fromHost = new HashMap();
				currency = acc;
				inputAmountFlag = "1";
			}
		}

		billPayment.setUserId(corpUser.getUserId());
		billPayment.setCorpId(corpUser.getCorpId());
		billPayment.setPayType(BillPayment.PAYMENT_TYPE_GENERAL);
		billPayment.setCurrency(currency);
		billPayment.setTransferAmount(transferAmount);
		billPayment.setOperation(Constants.OPERATION_NEW);
		billPayment.setStatus(Constants.STATUS_PENDING_APPROVAL);
		billPayment.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
		billPayment.setRequestTime(new Date());
		billPayment.setExecuteTime(null);

		Map resultData = new HashMap();
		this.convertPojo2Map(billPayment, resultData);
		resultData.putAll(fromHost);
		resultData.put("inputAmountFlag", inputAmountFlag);
		setResultData(resultData);

		this.setUsrSessionDataValue("billPayment", billPayment);
		this.setUsrSessionDataValue("EXPIRY_DATE", fromHost.get("EXPIRY_DATE"));
		this.setUsrSessionDataValue("REMARKS", fromHost.get("REMARKS"));
	}

	public void generalPayment() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");

		CorpUser corpUser = (CorpUser) getUser();

		BillPayment billPayment = (BillPayment) this.getUsrSessionDataValue("billPayment");
		this.convertMap2Pojo(this.getParameters(), billPayment);
		//check transferAmount
		if(this.getParameter("transferAmount")!=null){
			if(!Amount.check(this.getParameter("transferAmount"), 13, 2)){
				this.getResultData().put("amountErr", "Y");
				throw new NTBException("err.txn.AmountFormatError");
			}
			billPayment.setTransferAmount(new Double(Utils.replaceStr(this.getParameter("transferAmount"), ",", "")));
		}


		String fromAccountName = corpAccountService.getAccountName(billPayment.getFromAccount(),billPayment.getCurrency());

		String debitCurrency = corpAccountService.getCurrency(billPayment.getFromAccount(),true);
		BigDecimal debitAmount = exRatesService.getEquivalent(corpUser.getCorpId(),
				debitCurrency, billPayment.getCurrency(),
				null, new BigDecimal(billPayment.getTransferAmount().toString()), 2);

		Map debitInfo = new HashMap();
		debitInfo.put("fromAccountName", fromAccountName);
		debitInfo.put("debitCurrency", debitCurrency);
		debitInfo.put("debitAmount", debitAmount);


		Map assignuser = new HashMap();
		BigDecimal amountMopEq = exRatesService.getEquivalent(corpUser.getCorpId(),
				billPayment.getCurrency(), "MOP",
				new BigDecimal(billPayment.getTransferAmount().toString()),
				null, 2);
		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "currency");
		assignuser.put("amountField", "transferAmount");
		assignuser.put("amountMopEqField", "amountMopEq");
		assignuser.put("txnType", Constants.TXN_SUBTYPE_GENERAL_PAYMENT);
		assignuser.put("amountMopEq", amountMopEq);

		// Check User Limit
		BigDecimal debitAmountMopEq = exRatesService.getEquivalent(corpUser.getCorpId(),
				debitCurrency, "MOP",
				debitAmount, null, 2);
		if (!corpUser.checkUserLimit(Constants.TXN_TYPE_PAY_BILLS, debitAmountMopEq)) {
			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", billPayment.getUserId());
			reportMap.put("CORP_ID", billPayment.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", billPayment.getTransId());
			reportMap.put("FROM_ACCOUNT", billPayment.getFromAccount());
			reportMap.put("FROM_CURRENCY", debitCurrency);
			reportMap.put("TO_CURRENCY", billPayment.getCurrency());
			reportMap.put("FROM_AMOUNT", debitAmount);
			reportMap.put("TO_AMOUNT", billPayment.getTransferAmount());
			reportMap.put("EXCEEDING_TYPE", "2");
			reportMap.put("LIMIT_TYPE", "");
			reportMap.put("USER_LIMIT ", corpUser.getUserLimit(Constants.TXN_TYPE_PAY_BILLS));
			reportMap.put("DAILY_LIMIT ", new Double(0));
			reportMap.put("TOTAL_AMOUNT ", new Double(0));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			throw new NTBWarningException("err.txn.ExceedUserLimit");
		}
		
		// Check Limit
		if (!transferLimitService.checkCurAmtLimitQuota(billPayment.getFromAccount(),
				corpUser.getCorpId(),
				Constants.TXN_TYPE_PAY_BILLS,
				debitAmount.doubleValue(),
				amountMopEq.doubleValue())) {

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", billPayment.getUserId());
			reportMap.put("CORP_ID", billPayment.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", billPayment.getTransId());
			reportMap.put("FROM_ACCOUNT", billPayment.getFromAccount());
			reportMap.put("FROM_CURRENCY", debitCurrency);
			reportMap.put("TO_CURRENCY", billPayment.getCurrency());
			reportMap.put("FROM_AMOUNT", debitAmount);
			reportMap.put("TO_AMOUNT", billPayment.getTransferAmount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			throw new NTBWarningException("err.txn.ExceedDailyLimit");
		}

		Map resultData = new HashMap();
		this.convertPojo2Map(billPayment, resultData);
		resultData.putAll(debitInfo);

		resultData.putAll(assignuser);

		ApproveRuleService approveRuleService = (ApproveRuleService) Config.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(), resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}

		setResultData(resultData);

		this.setUsrSessionDataValue("debitInfo", debitInfo);
		this.setUsrSessionDataValue("assignuser", assignuser);
		this.setUsrSessionDataValue("billPayment", billPayment);

	}
	
	public void generalPaymentCancel() throws NTBException {
		Map resultData = this.getResultData();
		resultData.put("inputAmountFlag", getMerEnqFlag(
				resultData.get("payType").toString(),
				resultData.get("category").toString(),
				resultData.get("merchant").toString()));
		resultData.put("EXPIRY_DATE", this.getUsrSessionDataValue("EXPIRY_DATE"));
		resultData.put("REMARKS", this.getUsrSessionDataValue("REMARKS"));
	}

	public void generalPaymentCfm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");
		MailService mailService = (MailService) appContext.getBean("MailService");

		CorpUser corpUser = (CorpUser) this.getUser();

		Map debitInfo = (Map) this.getUsrSessionDataValue("debitInfo");


		Map assignuser = (Map) this.getUsrSessionDataValue("assignuser");
		String txnType = (String) assignuser.get("txnType");
		BigDecimal amountMopEq = (BigDecimal) assignuser.get("amountMopEq");

		BillPayment billPayment = (BillPayment) this.getUsrSessionDataValue("billPayment");

		// check value date cut-off time
		checkCutoffTimeAndSetMsg(billPayment);
		
		// add chen_y for CR225 20170412
		BigDecimal debitAmount = (BigDecimal) debitInfo.get("debitAmount");
		String  debitCurrency = (String) debitInfo.get("debitCurrency");
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		if (!transferLimitService.checkLimitQuota(billPayment.getFromAccount(),
				corpUser.getCorpId(),
				Constants.TXN_TYPE_PAY_BILLS,
				debitAmount.doubleValue(),
				amountMopEq.doubleValue())) {

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", billPayment.getUserId());
			reportMap.put("CORP_ID", billPayment.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", billPayment.getTransId());
			reportMap.put("FROM_ACCOUNT", billPayment.getFromAccount());
			reportMap.put("FROM_CURRENCY", debitCurrency);
			reportMap.put("TO_CURRENCY", billPayment.getCurrency());
			reportMap.put("FROM_AMOUNT", debitAmount);
			reportMap.put("TO_AMOUNT", billPayment.getTransferAmount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			//throw new NTBWarningException("err.txn.ExceedDailyLimit");
			setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg").getString("warnning.txn.ExceedDailyLimit"));
		}
		// add chen_y for CR225 20170412 end

		String assignedUser = Utils.null2EmptyWithTrim(this.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this.getParameter("mailUser"));

		String processId = flowEngineService.startProcess(txnType,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				BillPaymentAction.class,
				debitInfo.get("debitCurrency").toString(),
				new Double(debitInfo.get("debitAmount").toString()).doubleValue(),
				billPayment.getCurrency(),
				billPayment.getTransferAmount().doubleValue(),
				amountMopEq.doubleValue(),
				billPayment.getTransId(),
				billPayment.getRemark(), getUser(), assignedUser,
				((CorpUser)getUser()).getCorporation().getAllowExecutor(), FlowEngineService.RULE_FLAG_TO);

		try {

			billPaymentService.processPayment(billPayment);
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("err.txn.TranscationFaily", e);
			if (e instanceof NTBException) {
				throw (NTBException)e;
			} else {
				throw new NTBException("err.txn.TranscationFaily");
			}
		}

		Map resultData = new HashMap();
		this.convertPojo2Map(billPayment, resultData);
		resultData.putAll(debitInfo);

		resultData.putAll(assignuser);
		setResultData(resultData);

		//send mial to approver
		Map dataMap = new HashMap();
		dataMap.put("userID", corpUser.getUserId());
		dataMap.put("userName", corpUser.getUserName());
		dataMap.put("merchant", billPayment.getMerchant());
		dataMap.put("requestTime", billPayment.getRequestTime());
		dataMap.put("transId", billPayment.getTransId());

		/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
		/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_GENERAL_PAYMENT, mailUser.split(";"), dataMap);*/
		mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_GENERAL_PAYMENT, mailUser.split(";"),corpUser.getCorpId(), dataMap);
		/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */
		
	}
	
	private String getMerEnqFlag(String payType, String category, String merchant) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");

        String retFlag = "";
		if(payType.equals(BillPayment.PAYMENT_TYPE_GENERAL)){
			//Get Infomation from Host
			String acc = "";
			if (category.equals("010")) {
				//return
				retFlag="0";
			} else {
				acc = billPaymentService.checkOtherMerchant(category, merchant);
				if(acc == null){
					//return
					retFlag="0";
				} else {
					//return
					retFlag="1";
				}
			}
		} else if(payType.equals(BillPayment.PAYMENT_TYPE_CARD)){
			//return
			retFlag="1";
		}
		return retFlag;
		
	}

	public void cardPaymentLoad() throws NTBException {

		setResultData(new HashMap(1));
	}

	public void cardPaymentCheck() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = (CorpUser) this.getUser();
        corpUser.setLanguage(lang);//add by linrui for mul-language
		BillPayment billPayment = new BillPayment(String.valueOf(CibIdGenerator.getRefNoForTransaction()));
		this.convertMap2Pojo(this.getParameters(), billPayment);

		// get infomation from host
		Map fromHost = billPaymentService.getCardPaymentInfo(corpUser, billPayment.getBillNo1());
		String currency = fromHost.get("CARD_CURRENCY").toString();

		billPayment.setUserId(corpUser.getUserId());
		billPayment.setCorpId(corpUser.getCorpId());
		billPayment.setCurrency(currency);
		billPayment.setPayType(BillPayment.PAYMENT_TYPE_CARD);
		billPayment.setOperation(Constants.OPERATION_NEW);
		billPayment.setStatus(Constants.STATUS_PENDING_APPROVAL);
		billPayment.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
		billPayment.setRequestTime(new Date());
		billPayment.setExecuteTime(null);

		Map resultData = new HashMap();
		setResultData(resultData);
		this.convertPojo2Map(billPayment, resultData);

		this.setUsrSessionDataValue("billPayment", billPayment);

	}

	public void cardPayment() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");

		CorpUser corpUser = (CorpUser) getUser();

		BillPayment billPayment = (BillPayment) this.getUsrSessionDataValue("billPayment");
		this.convertMap2Pojo(this.getParameters(), billPayment);

		//*********
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");
		Double transAmt  = TransAmountFormate.formateAmount(this.getParameter("transferAmount"),lang);
		billPayment.setTransferAmount(transAmt);

		String debitCurrency = corpAccountService.getCurrency(billPayment.getFromAccount(),true);
		BigDecimal debitAmount = exRatesService.getEquivalent(corpUser.getCorpId(),
				debitCurrency, billPayment.getCurrency(),
				null, new BigDecimal(billPayment.getTransferAmount().toString()), 2);
		Map debitInfo = new HashMap();
		debitInfo.put("debitCurrency", debitCurrency);
		debitInfo.put("debitAmount", debitAmount);


		Map assignuser = new HashMap();
		BigDecimal amountMopEq = exRatesService.getEquivalent(corpUser.getCorpId(),
				billPayment.getCurrency(), "MOP",
				new BigDecimal(billPayment.getTransferAmount().toString()), null, 2);
		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "currency");
		assignuser.put("amountField", "transferAmount");
		assignuser.put("amountMopEqField", "amountMopEq");
		assignuser.put("txnType", Constants.TXN_SUBTYPE_CARD_PAYMENT);
		assignuser.put("amountMopEq", amountMopEq);

		// Check User Limit
		if (!corpUser.checkUserLimit(Constants.TXN_TYPE_PAY_BILLS, amountMopEq)) {

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", billPayment.getUserId());
			reportMap.put("CORP_ID", billPayment.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", billPayment.getTransId());
			reportMap.put("FROM_ACCOUNT", billPayment.getFromAccount());
			reportMap.put("FROM_CURRENCY", debitCurrency);
			reportMap.put("TO_CURRENCY", billPayment.getCurrency());
			reportMap.put("FROM_AMOUNT", debitAmount);
			reportMap.put("TO_AMOUNT", billPayment.getTransferAmount());
			reportMap.put("EXCEEDING_TYPE", "2");
			reportMap.put("LIMIT_TYPE", "");
			reportMap.put("USER_LIMIT ", corpUser.getUserLimit(Constants.TXN_TYPE_PAY_BILLS));
			reportMap.put("DAILY_LIMIT ", new Double(0));
			reportMap.put("TOTAL_AMOUNT ", new Double(0));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			throw new NTBWarningException("err.txn.ExceedUserLimit");
		}
		// check Limit
		if (!transferLimitService.checkCurAmtLimitQuota(billPayment.getFromAccount(),
				corpUser.getCorpId(),
				Constants.TXN_TYPE_PAY_BILLS,
				debitAmount.doubleValue(),
				amountMopEq.doubleValue())) {

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", billPayment.getUserId());
			reportMap.put("CORP_ID", billPayment.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", billPayment.getTransId());
			reportMap.put("FROM_ACCOUNT", billPayment.getFromAccount());
			reportMap.put("FROM_CURRENCY", debitCurrency);
			reportMap.put("TO_CURRENCY", billPayment.getCurrency());
			reportMap.put("FROM_AMOUNT", debitAmount);
			reportMap.put("TO_AMOUNT", billPayment.getTransferAmount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			throw new NTBWarningException("err.txn.ExceedDailyLimit");
		}

		Map resultData = new HashMap();
		this.convertPojo2Map(billPayment, resultData);
		resultData.putAll(debitInfo);

		resultData.putAll(assignuser);

		ApproveRuleService approveRuleService = (ApproveRuleService) Config.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(), resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}
		setResultData(resultData);

		this.setUsrSessionDataValue("debitInfo", debitInfo);
		this.setUsrSessionDataValue("assignuser", assignuser);
		this.setUsrSessionDataValue("billPayment", billPayment);
	}
	
	public void cardPaymentCancel() throws NTBException {
	}

	public void cardPaymentCfm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");
		MailService mailService = (MailService) appContext.getBean("MailService");

		CorpUser corpUser = (CorpUser) this.getUser();

		Map debitInfo = (Map) this.getUsrSessionDataValue("debitInfo");


		Map assignuser = (Map) this.getUsrSessionDataValue("assignuser");
		String txnType = (String) assignuser.get("txnType");
		BigDecimal amountMopEq = (BigDecimal) assignuser.get("amountMopEq");

		BillPayment billPayment = (BillPayment) this.getUsrSessionDataValue("billPayment");

		// check value date cut-off time
		checkCutoffTimeAndSetMsg(billPayment);

		// add by chen_y for CR225 20170412
		BigDecimal debitAmount = (BigDecimal) debitInfo.get("debitAmount");
		String  debitCurrency = (String) debitInfo.get("debitCurrency");
		
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		if (!transferLimitService.checkLimitQuota(billPayment.getFromAccount(),
				corpUser.getCorpId(),
				Constants.TXN_TYPE_PAY_BILLS,
				debitAmount.doubleValue(),
				amountMopEq.doubleValue())) {

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", billPayment.getUserId());
			reportMap.put("CORP_ID", billPayment.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", billPayment.getTransId());
			reportMap.put("FROM_ACCOUNT", billPayment.getFromAccount());
			reportMap.put("FROM_CURRENCY", debitCurrency);
			reportMap.put("TO_CURRENCY", billPayment.getCurrency());
			reportMap.put("FROM_AMOUNT", debitAmount);
			reportMap.put("TO_AMOUNT", billPayment.getTransferAmount());
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
				BillPaymentAction.class,
				debitInfo.get("debitCurrency").toString(),
				new Double(debitInfo.get("debitAmount").toString()).doubleValue(),
				billPayment.getCurrency(),
				billPayment.getTransferAmount().doubleValue(),
				amountMopEq.doubleValue(),
				billPayment.getTransId(),
				billPayment.getRemark(), getUser(), assignedUser,
				((CorpUser)getUser()).getCorporation().getAllowExecutor(), FlowEngineService.RULE_FLAG_TO);
		
		String fromAccountName = "";
		try {
			fromAccountName = corpAccountService.getAccountName(billPayment.getFromAccount(), billPayment.getCurrency());

			billPaymentService.processPayment(billPayment);

		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("err.txn.TranscationFaily", e);
			if (e instanceof NTBException) {
				throw (NTBException)e;
			} else {
				throw new NTBException("err.txn.TranscationFaily");
			}
		}

		Map resultData = new HashMap();
		resultData.put("fromAccountName", fromAccountName);
		resultData.putAll(debitInfo);
		resultData.putAll(assignuser);
		setResultData(resultData);
		this.convertPojo2Map(billPayment, resultData);

		//send mial to approver
		Map dataMap = new HashMap();
		dataMap.put("userID", corpUser.getUserId());
		dataMap.put("userName", corpUser.getUserName());
		dataMap.put("merchant", billPayment.getMerchant());
		dataMap.put("billNo1", billPayment.getBillNo1());
		dataMap.put("requestTime", billPayment.getRequestTime());
		dataMap.put("transId", billPayment.getTransId());

		/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
		/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_CARD_PAYMENT, mailUser.split(";"), dataMap);*/
		mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_CARD_PAYMENT, mailUser.split(";"),corpUser.getCorpId(), dataMap);
		/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */
		
	}

	public void taxPaymentLoad() throws NTBException {
		setResultData(new HashMap(1));

		Map resultData = new HashMap();
		setResultData(resultData);
	}

	public void taxPaymentCheck() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = (CorpUser) this.getUser();
        corpUser.setLanguage(lang);//add by linrui for mul-language
		BillPayment billPayment = new BillPayment(String.valueOf(CibIdGenerator.getRefNoForTransaction()));
		this.convertMap2Pojo(this.getParameters(), billPayment);
		//
		billPayment.setMerchant(billPayment.getBillType());

		Map fromHost = billPaymentService.getTaxPaymentInfo(corpUser,
				billPayment.getCategory(), billPayment.getMerchant(),
				billPayment.getBillNo1(), billPayment.getBillNo2());

		String transferAmount = fromHost.get("PAYMENT_AMOUNT").toString();
		String currency = fromHost.get("BILL_CURRENCY").toString();

		billPayment.setUserId(corpUser.getUserId());
		billPayment.setCorpId(corpUser.getCorpId());
		billPayment.setCurrency(currency);
		billPayment.setPayType(BillPayment.PAYMENT_TYPE_TAX);
		billPayment.setOperation(Constants.OPERATION_NEW);
		billPayment.setStatus(Constants.STATUS_PENDING_APPROVAL);
		billPayment.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
		billPayment.setTransferAmount(new Double(transferAmount));
		billPayment.setRequestTime(new Date());
		billPayment.setExecuteTime(null);

		Map resultData = new HashMap();
		resultData.putAll(fromHost);
		this.convertPojo2Map(billPayment, resultData);
		setResultData(resultData);

		this.setUsrSessionDataValue("billPayment", billPayment);
		this.setUsrSessionDataValue("fromHost", fromHost);
	}

	public void taxPayment() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");

		CorpUser corpUser = (CorpUser) getUser();

		Map fromHost = (Map) this.getUsrSessionDataValue("fromHost");

		BillPayment billPayment = (BillPayment) this.getUsrSessionDataValue("billPayment");
		this.convertMap2Pojo(this.getParameters(), billPayment);

		String fromAccountName = corpAccountService.getAccountName(billPayment.getFromAccount(),billPayment.getCurrency());
		String debitCurrency = corpAccountService.getCurrency(billPayment.getFromAccount(),true);
		BigDecimal debitAmount = exRatesService.getEquivalent(corpUser.getCorpId(),
				debitCurrency, billPayment.getCurrency(),
				null, new BigDecimal(billPayment.getTransferAmount().toString()), 2);

		Map debitInfo = new HashMap();
		debitInfo.put("fromAccountName", fromAccountName);
		debitInfo.put("debitCurrency", debitCurrency);
		debitInfo.put("debitAmount", debitAmount);

		Map assignuser = new HashMap();
		BigDecimal amountMopEq = exRatesService.getEquivalent(corpUser.getCorpId(),
				billPayment.getCurrency(), "MOP",
				new BigDecimal(billPayment.getTransferAmount().toString()), null, 2);
		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "currency");
		assignuser.put("amountField", "transferAmount");
		assignuser.put("amountMopEqField", "amountMopEq");
		assignuser.put("txnType", Constants.TXN_SUBTYPE_TAX_PAYMENT);
		assignuser.put("amountMopEq", amountMopEq);

		// Check User Limit
		if (!corpUser.checkUserLimit(Constants.TXN_TYPE_PAY_BILLS, amountMopEq)) {

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", billPayment.getUserId());
			reportMap.put("CORP_ID", billPayment.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", billPayment.getTransId());
			reportMap.put("FROM_ACCOUNT", billPayment.getFromAccount());
			reportMap.put("FROM_CURRENCY", debitCurrency);
			reportMap.put("TO_CURRENCY", billPayment.getCurrency());
			reportMap.put("FROM_AMOUNT", debitAmount);
			reportMap.put("TO_AMOUNT", billPayment.getTransferAmount());
			reportMap.put("EXCEEDING_TYPE", "2");
			reportMap.put("LIMIT_TYPE", "");
			reportMap.put("USER_LIMIT ", corpUser.getUserLimit(Constants.TXN_TYPE_PAY_BILLS));
			reportMap.put("DAILY_LIMIT ", new Double(0));
			reportMap.put("TOTAL_AMOUNT ", new Double(0));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			throw new NTBWarningException("err.txn.ExceedUserLimit");
		}
		// check Limit
		if (!transferLimitService.checkCurAmtLimitQuota(billPayment.getFromAccount(),
				corpUser.getCorpId(),
				Constants.TXN_TYPE_PAY_BILLS,
				debitAmount.doubleValue(),
				amountMopEq.doubleValue())) {

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", billPayment.getUserId());
			reportMap.put("CORP_ID", billPayment.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", billPayment.getTransId());
			reportMap.put("FROM_ACCOUNT", billPayment.getFromAccount());
			reportMap.put("FROM_CURRENCY", debitCurrency);
			reportMap.put("TO_CURRENCY", billPayment.getCurrency());
			reportMap.put("FROM_AMOUNT", debitAmount);
			reportMap.put("TO_AMOUNT", billPayment.getTransferAmount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			throw new NTBWarningException("err.txn.ExceedDailyLimit");
		}

		Map resultData = new HashMap();
		this.convertPojo2Map(billPayment, resultData);
		resultData.putAll(fromHost);
		resultData.putAll(debitInfo);
		resultData.putAll(assignuser);

		ApproveRuleService approveRuleService = (ApproveRuleService) Config.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(), resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}

		setResultData(resultData);

		this.setUsrSessionDataValue("fromHost", fromHost);
		this.setUsrSessionDataValue("debitInfo", debitInfo);
		this.setUsrSessionDataValue("assignuser", assignuser);
	}
	
	public void taxPaymentCancel() throws NTBException {
	}

	public void taxPaymentCfm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");
		MailService mailService = (MailService) appContext.getBean("MailService");

		CorpUser corpUser = (CorpUser) this.getUser();

		Map fromHost = (Map) this.getUsrSessionDataValue("fromHost");
		Map debitInfo = (Map) this.getUsrSessionDataValue("debitInfo");

		Map assignuser = (Map) this.getUsrSessionDataValue("assignuser");
		String txnType = (String) assignuser.get("txnType");
		BigDecimal amountMopEq = (BigDecimal) assignuser.get("amountMopEq");

		BillPayment billPayment = (BillPayment) this.getUsrSessionDataValue("billPayment");

		// check value date cut-off time
		checkCutoffTimeAndSetMsg(billPayment);
		
		// add chen_y for CR225 20170412
		BigDecimal debitAmount = (BigDecimal) debitInfo.get("debitAmount");
		String  debitCurrency = (String) debitInfo.get("debitCurrency");
		// check Limit
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		if (!transferLimitService.checkLimitQuota(billPayment.getFromAccount(),
				corpUser.getCorpId(),
				Constants.TXN_TYPE_PAY_BILLS,
				debitAmount.doubleValue(),
				amountMopEq.doubleValue())) {

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", billPayment.getUserId());
			reportMap.put("CORP_ID", billPayment.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", billPayment.getTransId());
			reportMap.put("FROM_ACCOUNT", billPayment.getFromAccount());
			reportMap.put("FROM_CURRENCY", debitCurrency);
			reportMap.put("TO_CURRENCY", billPayment.getCurrency());
			reportMap.put("FROM_AMOUNT", debitAmount);
			reportMap.put("TO_AMOUNT", billPayment.getTransferAmount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			//throw new NTBWarningException("err.txn.ExceedDailyLimit");
			setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg").getString("warnning.txn.ExceedDailyLimit"));
		}
		// add chen_y for CR225 20170412 end

		String assignedUser = Utils.null2EmptyWithTrim(this.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this.getParameter("mailUser"));
		String processId = flowEngineService.startProcess(txnType,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				BillPaymentAction.class,
				debitInfo.get("debitCurrency").toString(),
				new Double(debitInfo.get("debitAmount").toString()).doubleValue(),
				billPayment.getCurrency(),
				billPayment.getTransferAmount().doubleValue(),
				amountMopEq.doubleValue(),
				billPayment.getTransId(),
				billPayment.getRemark(), getUser(), assignedUser,
				((CorpUser)getUser()).getCorporation().getAllowExecutor(),
				FlowEngineService.RULE_FLAG_TO);
		try {
			billPaymentService.processPayment(billPayment);
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("err.txn.TranscationFaily", e);
			if (e instanceof NTBException) {
				throw (NTBException)e;
			} else {
				throw new NTBException("err.txn.TranscationFaily");
			}
		}

		Map resultData = new HashMap();
		this.convertPojo2Map(billPayment, resultData);
		resultData.putAll(fromHost);
		resultData.putAll(debitInfo);
		resultData.putAll(assignuser);
		setResultData(resultData);

		//send mial to approver
		Map dataMap = new HashMap();
		dataMap.put("userID", corpUser.getUserId());
		dataMap.put("userName", corpUser.getUserName());
		dataMap.put("merchant", billPayment.getMerchant());
		dataMap.put("billNo1", billPayment.getBillNo1());
		dataMap.put("billNo2", billPayment.getBillNo2());
		dataMap.put("requestTime", billPayment.getRequestTime());
		dataMap.put("transId", billPayment.getTransId());
		
		/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
		/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_TAX_PAYMENT, mailUser.split(";"), dataMap);*/
		mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_TAX_PAYMENT, mailUser.split(";"),corpUser.getCorpId(), dataMap);
		/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */
		
	}

	public void listLoad() throws NTBException {
		setResultData(new HashMap(1));
	}
	
	public void listHistory() throws NTBException {
		setResultData(new HashMap(1));

		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
		CorporationService corpService = (CorporationService) appContext.getBean("CorporationService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");

		CorpUser corpUser = (CorpUser) this.getUser();

		String date_range = Utils.null2EmptyWithTrim(this.getParameter("date_range"));
		String dateFrom = Utils.null2EmptyWithTrim(this.getParameter("dateFrom"));
		String dateTo = Utils.null2EmptyWithTrim(this.getParameter("dateTo"));
		String merchant = Utils.null2EmptyWithTrim(this.getParameter("merchant"));
		String fromAccount = Utils.null2EmptyWithTrim(this.getParameter("fromAccount"));

		List historyList = billPaymentService.listHistory(
				corpUser.getCorpId(), corpUser.getUserId(), dateFrom, dateTo, merchant, fromAccount);
		historyList = this.convertPojoList2MapList(historyList);

		Map tmpMap = null;
		Corporation corp = corpService.view(corpUser.getCorpId());
		for (int i=0; i<historyList.size(); i++) {
			tmpMap = (Map) historyList.get(i);
			// set change flag
			if ("Y".equals(corp.getAllowApproverSelection())) {
				if (Constants.STATUS_PENDING_APPROVAL.equals(tmpMap.get("status").toString())) {
					if (flowEngineService.checkApproveComplete(
							billPaymentService.payType2TxnType(tmpMap.get("payType").toString()),
							tmpMap.get("transId").toString())) {
						tmpMap.put("changeFlag", "N");
					} else {
						tmpMap.put("changeFlag", "Y");
						// set txnType for changing approver
						tmpMap.put("txnType", billPaymentService.payType2TxnType(tmpMap.get("payType").toString()));
					}
				}
			}
		}


		Map resultData = new HashMap();
		resultData.put("date_range", date_range);
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("merchant", merchant);
		resultData.put("historyList", historyList);
		resultData.put("fromAccount", fromAccount);
		setResultData(resultData);
	}

	public boolean approve(String txnType, String id, CibAction bean) throws NTBException {

		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		CorpUser corpUser = (CorpUser) bean.getUser();
		if (txnType != null) {
			String payType = billPaymentService.txnType2PayType(txnType);
			BillPayment billPayment = billPaymentService.viewPayment(id, payType);

			String debitCurrency = corpAccountService.getCurrency(billPayment.getFromAccount(),true);
			BigDecimal debitAmount = exRatesService.getEquivalent(corpUser.getCorpId(),
					debitCurrency, billPayment.getCurrency(),
					null, new BigDecimal(billPayment.getTransferAmount().toString()), 2);
			Map debitInfo = new HashMap();
			debitInfo.put("debitCurrency", debitCurrency);
			debitInfo.put("debitAmount", debitAmount);

			BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser.getCorpId(),
					debitCurrency, "MOP",
					debitAmount, null, 2);
			// check Limit
			if (!transferLimitService.checkLimitQuota(billPayment.getFromAccount(),
					corpUser.getCorpId(),
					Constants.TXN_TYPE_PAY_BILLS,
					debitAmount.doubleValue(),
					equivalentMOP.doubleValue())) {

				// write limit report
	        	Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("USER_ID", billPayment.getUserId());
				reportMap.put("CORP_ID", billPayment.getCorpId());
				reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
				reportMap.put("LOCAL_TRANS_CODE", billPayment.getTransId());
				reportMap.put("FROM_ACCOUNT", billPayment.getFromAccount());
				reportMap.put("FROM_CURRENCY", debitCurrency);
				reportMap.put("TO_CURRENCY", billPayment.getCurrency());
				reportMap.put("FROM_AMOUNT", debitAmount);
				reportMap.put("TO_AMOUNT", billPayment.getTransferAmount());
				reportMap.put("EXCEEDING_TYPE", "1");
				reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
				reportMap.put("USER_LIMIT ", "0");
				reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
				reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

				throw new NTBWarningException("err.txn.ExceedDailyLimit");
			}
			CorpUser user = (CorpUser)bean.getUser();//add by linrui mul-language
			billPaymentService.approvePayment(billPayment, user, equivalentMOP);//mod by linrui for mul-language
			return true;
		} else {
			return false;
		}
	}

	public boolean reject(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");

		if (txnType != null) {
			String payType = billPaymentService.txnType2PayType(txnType);
			BillPayment billPayment = billPaymentService.viewPayment(id, payType);
			billPaymentService.rejectPayment(billPayment);
			return true;
		} else {
			return false;
		}
	}

	public String viewDetail(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");

		String viewPageUrl = "";
		Map resultData = bean.getResultData();

		if (txnType != null) {
			CorpUser corpUser = (CorpUser) bean.getUser();
			String payType = billPaymentService.txnType2PayType(txnType);

			BillPayment billPayment = billPaymentService.viewPayment(id,
					payType);
			if (billPayment != null) {
				bean.convertPojo2Map(billPayment, resultData);

				String fromAccountName = corpAccountService.getAccountName(billPayment.getFromAccount(),billPayment.getCurrency());
				String debitCurrency = corpAccountService.getCurrency(billPayment.getFromAccount(),true);
				BigDecimal debitAmount = exRatesService.getEquivalent(corpUser.getCorpId(),
						debitCurrency, billPayment.getCurrency(),
						null, new BigDecimal(billPayment.getTransferAmount().toString()), 2);

				if (payType.equals(BillPayment.PAYMENT_TYPE_GENERAL)) {
					viewPageUrl = "/WEB-INF/pages/txn/bill_payment/general_payment_approval_view.jsp";
				} else if (payType.equals(BillPayment.PAYMENT_TYPE_CARD)) {
					viewPageUrl = "/WEB-INF/pages/txn/bill_payment/card_payment_approval_view.jsp";
				} else if (payType.equals(BillPayment.PAYMENT_TYPE_TAX)) {
					viewPageUrl = "/WEB-INF/pages/txn/bill_payment/tax_payment_approval_view.jsp";
				}

				Map assignuser = new HashMap();
				BigDecimal amountMopEq = exRatesService.getEquivalent(corpUser.getCorpId(),
						billPayment.getCurrency(), "MOP",
						new BigDecimal(billPayment.getTransferAmount().toString()),
						null, 2);
				assignuser.put("txnTypeField", "txnType");
				assignuser.put("currencyField", "currency");
				assignuser.put("amountField", "transferAmount");
				assignuser.put("amountMopEqField", "amountMopEq");
				assignuser.put("txnType", txnType);
				assignuser.put("amountMopEq", amountMopEq);

				resultData.put("fromAccountName", fromAccountName);
				resultData.put("debitCurrency", debitCurrency);
				resultData.put("debitAmount", debitAmount);
				resultData.putAll(assignuser);

				bean.setResultData(resultData);
			}
		}
		return viewPageUrl;
	}

    public boolean cancel(String txnType, String id, CibAction bean) throws NTBException {
    	return reject(txnType, id, bean);
    }

    private void checkCutoffTimeAndSetMsg(BillPayment billPayment) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext.getBean("CutOffTimeService");

		// check value date cut-off time
        if (corpAccountService.isSavingAccount(billPayment.getFromAccount(),billPayment.getCurrency())){
			setMessage(cutOffTimeService.check("59SC", "", billPayment.getCurrency()));
        } else {
			setMessage(cutOffTimeService.check("59CC", "", billPayment.getCurrency()));
        }
    }
    
    /**
     * if true return,that means it need special processing.
     * @param merchant
     * @return
     * @throws NTBException
     */
    public boolean sepcialMerchant(String merchant) throws NTBException {
    	
    	if (merchant.equals("CEM")||merchant.equals("SAAM")||merchant.equals("CTM")) {
			return true;
		} 
    	return false;
    	
    }
    
    /**
     * add by long_zg 2014-04-17 for IE.11 in BOB application problem 10 begin
     */
    public void frequentPayment() throws NTBException {
    	this.generalPayment() ;
	}
    
    public void frequentPaymentCfm() throws NTBException {
    	this.generalPaymentCfm() ;
	}
    
    public void frequentCardPayment() throws NTBException {
		this.cardPayment() ;
	}
    
    public void frequentCardPaymentCfm() throws NTBException {
		this.cardPaymentCfm() ;
	}
    
    public void frequentPaymentCancel() throws NTBException {
		this.generalPaymentCancel() ;
	}
    
    public void frequentCardPaymentCancel() throws NTBException {
    	this.cardPaymentCancel() ;
    }
    /**
     * add by long_zg 2014-04-17 for IE.11 in BOB application problem 10 end
     */
}
