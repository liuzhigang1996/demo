package app.cib.action.txn;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import app.cib.bo.bnk.BankUser;
import app.cib.bo.flow.FlowProcess;
import app.cib.bo.srv.TxnPrompt;
import app.cib.bo.sys.AbstractCorpUser;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TransferBank;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.dao.srv.TransferPromptDao;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.srv.TransferPromptService;
import app.cib.service.sys.ApproveRuleService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.CutOffTimeService;
import app.cib.service.sys.MailService;
import app.cib.service.sys.TransAmountService;
import app.cib.service.txn.TransferLimitService;
import app.cib.service.txn.TransferService;
import app.cib.util.Constants;
import app.cib.util.RcCurrency;
import app.cib.util.TransAmountFormate;
import app.cib.util.UploadReporter;
import app.cib.util.otp.SMSOTPObject;
import app.cib.util.otp.SMSOTPUtil;
import app.cib.util.otp.SMSReturnObject;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.exception.NTBWarningException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Encryption;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

/**
 * @author mxl 07/20
 *
 */
public class TransferInBankAction extends CibAction implements Approvable {
	private String defalutPattern = Config.getProperty("DefaultDatePattern");
	public void addLoad() throws NTBException {
		HashMap resultData = new HashMap(1);
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		
		//add by lzg 20191022
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		TxnPrompt txnPrompt = new TxnPrompt();
		try{
			txnPrompt = transferPromptService.loadByTxnType("1",TransferPromptDao.STATUS_NORMAL);
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
		//add by lzg end
		setResultData(resultData);

	}
	public void addLoad3rd() throws NTBException {
		HashMap resultData = new HashMap(1);
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		
		//add by lzg 20191022
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		TxnPrompt txnPrompt = new TxnPrompt();
		try{
			txnPrompt = transferPromptService.loadByTxnType("2",TransferPromptDao.STATUS_NORMAL);
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
		//add by lzg end
		setResultData(resultData);
		
	}

	public void add() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		TransAmountService transAmountService = (TransAmountService) appContext
				.getBean("TransAmountService");
		
		//modified by lzg 20190902 for trial
		
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = (CorpUser) this.getUser();
		corpUser.setLanguage(lang);//add by linrui for mul-language
		TransferBank transferBank = new TransferBank(CibIdGenerator.getRefNoForTransaction());
		this.convertMap2Pojo(this.getParameters(), transferBank);
		transferBank.setUserId(corpUser.getUserId());
		transferBank.setCorpId(corpUser.getCorpId());
		//add by linrui for mul-language 
        Double transAmt  = TransAmountFormate.formateAmount(this.getParameter("transferAmount"),lang);
        Double debitAmt = TransAmountFormate.formateAmount(this.getParameter("debitAmount"), lang);
        transferBank.setTransferAmount(transAmt);
        transferBank.setDebitAmount(debitAmt);
        //end
		CorpAccount corpAccount = corpAccountService
				.viewCorpAccount(transferBank.getFromAccount());
//		transferBank.setFromCurrency(corpAccount.getCurrency());
		transferBank.setFromAcctype(corpAccount.getAccountType());
		
		
		String ownerAccFlag = this.getParameter("ownerAccFlag") ;
		String toName = "";
		if (Constants.NO.equalsIgnoreCase(ownerAccFlag)) {
			
			/*toName = this.getParameter("toName");	*/
			Map curMap = transferService.viewCurrentDetail(transferBank.getToAccount()) ;
			toName = (String ) curMap.get("SHORT_NAME") ;
			transferBank.setToName(toName);
			
			/* get the currency of the toAccount and accType from the fromHost */
			Map fromHost = transferService.toHostAccountInBANK(transferBank
					.getTransId(), corpUser, transferBank.getToAccount());
			
			String toCurrency = Utils.null2EmptyWithTrim(this.getParameter("toCurrency"));
			String toAccType = Utils.null2EmptyWithTrim(fromHost.get("PRODUCT_TYPE"));
			/*if (toAccType.equals(CorpAccount.APPLICATION_CODE_CURRENT)) {
				transferBank.setToAcctype(CorpAccount.ACCOUNT_TYPE_CURRENT);
			} else if (toAccType.equals(CorpAccount.APPLICATION_CODE_SAVING)) {
				transferBank.setToAcctype(CorpAccount.ACCOUNT_TYPE_SAVING);
			} else if (toAccType.equals(CorpAccount.APPLICATION_CODE_OVERDRAFT)) {
				transferBank.setToAcctype(CorpAccount.ACCOUNT_TYPE_OVER_DRAFT);
			}*/
			if(toAccType.length()>=2){	//add by zzh 20180224 for to host accountType
				toAccType=toAccType.substring(0, 2);
			}
			transferBank.setToAcctype(toAccType);
			RcCurrency rcCurrency = new RcCurrency();
			transferBank.setToCurrency(toCurrency);

		} else {
			//add by wcc 20190402
			String corpId = corpUser.getCorpId();
			toName = transferService.getBenName(corpId);
			transferBank.setToName(toName);
			//end
			/* Modify by long_zg 2015-05-22 UAT6-242 缂佹鍏涚粭渚�鎳撻崨鎵М閻犫晝鎯erator闁瑰瓨鍔曟慨娑㈡娴ｅ搫绻侀悘蹇斿敹ave as template begin */
			/*transferBank.setToAccount(toAccount);*/
			/* Modify by long_zg 2015-05-22 UAT6-242 缂佹鍏涚粭渚�鎳撻崨鎵М閻犫晝鎯erator闁瑰瓨鍔曟慨娑㈡娴ｅ搫绻侀悘蹇斿敹ave as template end */
			corpAccount = corpAccountService.viewCorpAccount(transferBank
					.getToAccount());
//			transferBank.setToCurrency(corpAccount.getCurrency());
			transferBank.setToAcctype(corpAccount.getAccountType());
			
			/* Modify by long_zg 2015-05-22 UAT6-242 缂佹鍏涚粭渚�鎳撻崨鎵М閻犫晝鎯erator闁瑰瓨鍔曟慨娑㈡娴ｅ搫绻侀悘蹇斿敹ave as template begin */
			/*transferBank.setOwnerAccFlag("Y");*/
			transferBank.setOwnerAccFlag(Constants.YES);
			/* Modify by long_zg 2015-05-22 UAT6-242 缂佹鍏涚粭渚�鎳撻崨鎵М閻犫晝鎯erator闁瑰瓨鍔曟慨娑㈡娴ｅ搫绻侀悘蹇斿敹ave as template begin */
		}
		
		//add by lzg 20190708
		transferService.checkConvertibility(transferBank.getFromCurrency(),transferBank.getToCurrency());
		//add by lzg end
		
		if (transferBank.getTransferAmount().doubleValue() == 0) {
			transferBank.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_FROM);
			transferBank.setFlag("1");
			BigDecimal transferAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), new BigDecimal(transferBank
					.getDebitAmount().toString()), null, 2);

			transferBank
					.setTransferAmount(new Double(transferAmount.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinTransAmt(transferBank.getFromCurrency(), transferBank.getDebitAmount().doubleValue());
			
			// add by mxl 0907 get the exchange rate
			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), 8);
			String rateType = (String) exchangeMap
					.get(ExchangeRatesDao.RATE_TYPE);
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap
					.get(ExchangeRatesDao.BUY_RATE);
			BigDecimal sellRate = (BigDecimal) exchangeMap
					.get(ExchangeRatesDao.SELL_RATE);

			// Jet modified. show only one exchange rate at the page, it should be always buy rate/sell rate 
			if (null == buyRate){
				buyRate = new BigDecimal("1");
			}
			if (null == sellRate){
				sellRate = new BigDecimal("1");
			}			
			if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
				exchangeRate = new BigDecimal("0");
			} else {
				exchangeRate = buyRate.divide(sellRate, 8,
						BigDecimal.ROUND_DOWN);
			}
			transferBank.setExchangeRate(new Double(exchangeRate.doubleValue()));
			*/
			//add by lzg 20190618
			/*BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferBank.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferBank.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
			//add by lzg end
			
		} else if (transferBank.getDebitAmount().doubleValue() == 0) {
			transferBank.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_TO);
			transferBank.setFlag("2");
			BigDecimal toAmount = new BigDecimal(transferBank
					.getTransferAmount().toString());
			BigDecimal fromAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), null, toAmount, 2);

			transferBank.setDebitAmount(new Double(fromAmount.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinTransAmt(transferBank.getToCurrency(), transferBank.getTransferAmount().doubleValue());
			
			
			// add by mxl 0907 get the exchange rate
			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), 8);
			String rateType = (String) exchangeMap
					.get(ExchangeRatesDao.RATE_TYPE);
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap
					.get(ExchangeRatesDao.BUY_RATE);
			BigDecimal sellRate = (BigDecimal) exchangeMap
					.get(ExchangeRatesDao.SELL_RATE);

			// Jet modified. show only one exchange rate at the page, it should be always buy rate/sell rate 
			if (null == buyRate){
				buyRate = new BigDecimal("1");
			}
			if (null == sellRate){
				sellRate = new BigDecimal("1");
			}			
			if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
				exchangeRate = new BigDecimal("0");
			} else {
				exchangeRate = buyRate.divide(sellRate, 8,
						BigDecimal.ROUND_DOWN);
			}
			transferBank.setExchangeRate(new Double(exchangeRate.doubleValue()));
			//add by lzg 20190618
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferBank.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferBank.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
			//add by lzg end
		}
		
		
		//add by lzg 20190902
		Map Host = transferService.toHostInBankTrial(transferBank,corpUser);
		transferBank.setFromAccount(((String)Host.get("DR_AC")).trim());
		transferBank.setToAccount(((String)Host.get("CR_AC")).trim());
		transferBank.setToName(((String)Host.get("CR_NM")).trim());
		transferBank.setDebitAmount(((BigDecimal)Host.get("TX_DR_AMT")).doubleValue());
		transferBank.setTransferAmount(((BigDecimal)Host.get("TX_CR_AMT")).doubleValue());
		transferBank.setExchangeRate(((BigDecimal)Host.get("EX_RAT")).doubleValue());
		transferBank.setShowExchangeRate(((BigDecimal)Host.get("EX_RAT")).doubleValue());
		//add by lzg end
		
		
		
		String inputAmountFlag = transferBank.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferBank.getFromCurrency();
			transferAmount = transferBank.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferBank.getToCurrency();
			transferAmount = transferBank.getTransferAmount();
		}
		
		String language = corpUser.getLangCode();
		//add by lzg 20190619
		checkCutoffTimeAndSetMsg(transferBank,language);
		//add by lzg end
		
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), currency, "MOP",
				new BigDecimal(transferAmount.toString()),
				null, 2);
		
		if(transferBank.getOwnerAccFlag().equals("Y"))
		{
			if (!corpUser.checkUserLimit(Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT,
					equivalentMOP)) {
				 //write limit report
	        	Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("USER_ID", transferBank.getUserId());
				reportMap.put("CORP_ID", corpUser.getCorpId());
				reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT);
				reportMap.put("LOCAL_TRANS_CODE", transferBank.getTransId());
				reportMap.put("FROM_CURRENCY", transferBank.getFromCurrency());
				reportMap.put("TO_CURRENCY", transferBank.getToCurrency());
				reportMap.put("FROM_AMOUNT", transferBank.getDebitAmount());
				reportMap.put("TO_AMOUNT", transferBank.getTransferAmount());
				reportMap.put("FROM_ACCOUNT", transferBank.getFromAccount());
				reportMap.put("EXCEEDING_TYPE", "2");
				reportMap.put("LIMIT_TYPE", "");
				reportMap.put("USER_LIMIT ", corpUser.getUserLimit(Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT));
				reportMap.put("DAILY_LIMIT ", new Double(0));
				reportMap.put("TOTAL_AMOUNT ", new Double(0));
				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
				throw new NTBWarningException("err.txn.ExceedUserLimit");
			}
		} else if (transferBank.getOwnerAccFlag().equals("N")) {
			if (!corpUser.checkUserLimit(Constants.TXN_TYPE_TRANSFER_BANK,
					equivalentMOP)) {
				 //write limit report
	        	Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("USER_ID", transferBank.getUserId());
				reportMap.put("CORP_ID", corpUser.getCorpId());
				reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_BANK);
				reportMap.put("LOCAL_TRANS_CODE", transferBank.getTransId());
				reportMap.put("FROM_CURRENCY", transferBank.getFromCurrency());
				reportMap.put("TO_CURRENCY", transferBank.getToCurrency());
				reportMap.put("FROM_AMOUNT", transferBank.getDebitAmount());
				reportMap.put("FROM_ACCOUNT", transferBank.getFromAccount());
				reportMap.put("TO_AMOUNT", transferBank.getTransferAmount());
				reportMap.put("EXCEEDING_TYPE", "2");
				reportMap.put("LIMIT_TYPE", "");
				reportMap.put("USER_LIMIT ", corpUser.getUserLimit(Constants.TXN_TYPE_TRANSFER_BANK));
				reportMap.put("DAILY_LIMIT ", new Double(0));
				reportMap.put("TOTAL_AMOUNT ", new Double(0));
				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
				throw new NTBWarningException("err.txn.ExceedUserLimit");
			}
		}
		
		String txnTypeBank = null;
		if(Constants.YES.equals(transferBank.getOwnerAccFlag())) {
			txnTypeBank = Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT;
		} else if(Constants.NO.equals(transferBank.getOwnerAccFlag())) {
			txnTypeBank = Constants.TXN_TYPE_TRANSFER_BANK;
		}
		
		if (!transferLimitService.checkCurAmtLimitQuota(transferBank
				.getFromAccount(), corpUser.getCorpId(), txnTypeBank,
				transferBank.getDebitAmount().doubleValue(),
				equivalentMOP.doubleValue())) {
            //write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", transferBank.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", txnTypeBank);
			reportMap.put("LOCAL_TRANS_CODE", transferBank.getTransId());
			reportMap.put("FROM_CURRENCY", transferBank.getFromCurrency());
			reportMap.put("TO_CURRENCY", transferBank.getToCurrency());
			reportMap.put("FROM_AMOUNT", transferBank.getDebitAmount());
			reportMap.put("TO_AMOUNT", transferBank.getTransferAmount());
			reportMap.put("FROM_ACCOUNT", transferBank.getFromAccount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
			throw new NTBWarningException("err.txn.ExceedDailyLimit");
		}
		if (transferBank.getFromAccount().equals(transferBank.getToAccount())&&transferBank.getFromCurrency().equals(transferBank.getToCurrency())) {
			throw new NTBException("err.txn.TransferAccError");
		}

		Map resultData = new HashMap();
		
		//add by lzg 20190820
		String corpType = corpUser.getCorporation().getCorpType();
		String checkFlag = corpUser.getCorporation().getAuthenticationMode();
		resultData.put("corpType", corpType);
		resultData.put("checkFlag", checkFlag);
		resultData.put("operationType", "send");
    	resultData.put("showMobileNo", corpUser.getMobile());
    	resultData.put("txnType", txnTypeBank);
    	
		//add by lzg end
		
		this.convertPojo2Map(transferBank, resultData);
		
		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		/* Modify by long_zg 2015-05-22 UAT6-242 缂佹鍏涚粭渚�鎳撻崨鎵М閻犫晝鎯erator闁瑰瓨鍔曟慨娑㈡娴ｅ搫绻侀悘蹇斿敹ave as template begin */
		/*resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);*/
		resultData.put("txnType", txnTypeBank);
		/* Modify by long_zg 2015-05-22 UAT6-242 缂佹鍏涚粭渚�鎳撻崨鎵М閻犫晝鎯erator闁瑰瓨鍔曟慨娑㈡娴ｅ搫绻侀悘蹇斿敹ave as template end */
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);

		/* Modify by long_zg 2015-05-22 UAT6-242 缂佹鍏涚粭渚�鎳撻崨鎵М閻犫晝鎯erator闁瑰瓨鍔曟慨娑㈡娴ｅ搫绻侀悘蹇斿敹ave as template begin */
		/*if (transferBank.getOwnerAccFlag().equals("N")) {
        	toAccount2 = transferBank.getToAccount();
        }
        resultData.put("toAccount2", toAccount2);*/
        /* Modify by long_zg 2015-05-22 UAT6-242 缂佹鍏涚粭渚�鎳撻崨鎵М閻犫晝鎯erator闁瑰瓨鍔曟慨娑㈡娴ｅ搫绻侀悘蹇斿敹ave as template begin */
        resultData.put("toName", toName);//add by linrui 20181105

		ApproveRuleService approveRuleService = (ApproveRuleService) Config
				.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(), resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}
		
		//by wency 20130118
		//this.checkDuplicatedInput("B", transferBank);

		this.setResultData(resultData);
		
		this.setUsrSessionDataValue("transferBank", transferBank);
	}

	public void addConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
		.getBean("FlowEngineService");
		CorpUser corpUser = (CorpUser) getUser();

		TransferBank transferBank = (TransferBank) this
				.getUsrSessionDataValue("transferBank");

		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));

		String inputAmountFlag = transferBank.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferBank.getFromCurrency();
			transferAmount = transferBank.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferBank.getToCurrency();
			transferAmount = transferBank.getTransferAmount();
		}
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		String txnTypeBank = null;
		
		if(transferBank.getOwnerAccFlag().equals("Y")) {
			txnTypeBank = Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT;
		} else if(transferBank.getOwnerAccFlag().equals("N")) {
			txnTypeBank = Constants.TXN_TYPE_TRANSFER_BANK;
		}
		
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), currency, "MOP",
				new BigDecimal(transferAmount.toString()),
				null, 2);
		
		
		if (!transferLimitService.checkLimitQuota(transferBank
				.getFromAccount(), corpUser.getCorpId(), txnTypeBank,
				transferBank.getDebitAmount().doubleValue(),
				equivalentMOP.doubleValue())) {
            //write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", transferBank.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", txnTypeBank);
			reportMap.put("LOCAL_TRANS_CODE", transferBank.getTransId());
			reportMap.put("FROM_CURRENCY", transferBank.getFromCurrency());
			reportMap.put("TO_CURRENCY", transferBank.getToCurrency());
			reportMap.put("FROM_AMOUNT", transferBank.getDebitAmount());
			reportMap.put("TO_AMOUNT", transferBank.getTransferAmount());
			reportMap.put("FROM_ACCOUNT", transferBank.getFromAccount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
			//throw new NTBWarningException("err.txn.ExceedDailyLimit");
			setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg").getString("warnning.txn.ExceedDailyLimit")+"");
			Log.info(getMessage());
		}

		/*BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), currency, "MOP",
				new BigDecimal(transferAmount.toString()),
				null, 2);*/
		// add by chen_y for CR225 20170412 end
		
		String processId = flowEngineService.startProcess(
				/* Modify by long_zg 2019-05-22 UAT6-245 COB闁挎稒鑹惧ú鎾剁矙椤旇法绉悹鈺婂墰濞堟垿宕ュ鍤侯偊妫侀敓绛嬫附闁哄洤鐡ㄩ弫锟絙egin */
				/*Constants.TXN_SUBTYPE_TRANSFER_BANK,*/
				txnTypeBank,
				/* Modify by long_zg 2019-05-22 UAT6-245 COB闁挎稒鑹惧ú鎾剁矙椤旇法绉悹鈺婂墰濞堟垿宕ュ鍤侯偊妫侀敓绛嬫附闁哄洤鐡ㄩ弫锟絜nd */
				
				FlowEngineService.TXN_CATEGORY_FINANCE,
				TransferInBankAction.class, transferBank.getFromCurrency(),
				transferBank.getDebitAmount().doubleValue(), transferBank.getToCurrency(),transferBank.getTransferAmount().doubleValue(),equivalentMOP
				.doubleValue(),transferBank.getTransId(),transferBank
						.getRemark(), getUser(), assignedUser, corpUser
						.getCorporation().getAllowExecutor(),inputAmountFlag);
		Map resultData = new HashMap();
		String corpType = getParameter("corpType");
		resultData.put("corpType", corpType);
		try {
			transferBank.setExecuteTime(new Date());
			transferBank.setRequestTime(new Date());
			transferBank.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			transferBank.setStatus(Constants.STATUS_PENDING_APPROVAL);
			transferBank.setOperation(Constants.OPERATION_NEW);
			// edit by mxl 0819
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");

			/* Modify by long_zg 2019-05-22 UAT6-245 COB闁挎稒鑹惧ú鎾剁矙椤旇法绉悹鈺婂墰濞堟垿宕ュ鍤侯偊妫侀敓绛嬫附闁哄洤鐡ㄩ弫锟絙egin */
			/*Constants.TXN_SUBTYPE_TRANSFER_BANK,*/
			/*resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);*/
			resultData.put("txnType", txnTypeBank);
			/* Modify by long_zg 2019-05-22 UAT6-245 COB闁挎稒鑹惧ú鎾剁矙椤旇法绉悹鈺婂墰濞堟垿宕ュ鍤侯偊妫侀敓绛嬫附闁哄洤鐡ㄩ弫锟絜nd */
			
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);
			resultData.put("toName", transferBank.getToName());//add by linrui 20181105
			
			this.convertPojo2Map(transferBank, resultData);
			
			ApproveRuleService approveRuleService = (ApproveRuleService) Config
					.getAppContext().getBean("ApproveRuleService");
			if (!approveRuleService.checkApproveRule(corpUser.getCorpId(), resultData)) {
				throw new NTBException("err.flow.ApproveRuleNotDefined");
			}
			
			setResultData(resultData);
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime",transferBank.getRequestTime());
			dataMap.put("transId",transferBank.getTransId());
			dataMap.put("currency",transferBank.getToCurrency());
			dataMap.put("amount",transferBank.getTransferAmount());
			dataMap.put("corpName",corpUser.getCorporation().getCorpName());
			dataMap.put("toAccount",transferBank.getToAccount());
			dataMap.put("transId",transferBank.getTransId());
			dataMap.put("remark",transferBank.getRemark());
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
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
			transferService.transferAccInBANK1to1(transferBank);
			try{
				approve(currentFlowProcess.getTxnType(), currentFlowProcess.getTransNo(), this);
			}catch (NTBHostException e) {
				transferService.removeTransfer("transfer_bnu", currentFlowProcess.getTransNo());
				throw new NTBHostException(e.getErrorArray());
			}catch (NTBException e) {
				transferService.removeTransfer("transfer_bnu", currentFlowProcess.getTransNo());
				throw new NTBException(e.getErrorCode());
			}
		}
		if(!"3".equals(corpType)){
			try{
				transferService.transferAccInBANK1to1(transferBank);
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

	public void addCancel() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		TransferBank transferBank = (TransferBank) this.getUsrSessionDataValue("transferBank");
		
		CorpUser corpUser = (CorpUser) this.getUser();
		
		setResultData(new HashMap(1));
		
		// added by Jet for display
		if ((Constants.INPUT_AMOUNT_FLAG_FROM).equals(transferBank
				.getInputAmountFlag())) {
			transferBank.setTransferAmount(null);
		} else if ((Constants.INPUT_AMOUNT_FLAG_TO).equals(transferBank
				.getInputAmountFlag())) {
			transferBank.setDebitAmount(null);
		}

		Map resultData = new HashMap();
		this.convertPojo2Map(transferBank, resultData);
		
		/* Modify by long_zg 2015-05-22 UAT6-242 缂佹鍏涚粭渚�鎳撻崨鎵М閻犫晝鎯erator闁瑰瓨鍔曟慨娑㈡娴ｅ搫绻侀悘蹇斿敹ave as template begin */
		/*if("N".equals(transferBank.getOwnerAccFlag())){
			resultData.put("toAccount", "0");
		}
		String toAccount2 = null;
		toAccount2 = transferBank.getToAccount();
    	resultData.put("toAccount2", toAccount2);*/
    	/* Modify by long_zg 2015-05-22 UAT6-242 缂佹鍏涚粭渚�鎳撻崨鎵М閻犫晝鎯erator闁瑰瓨鍔曟慨娑㈡娴ｅ搫绻侀悘蹇斿敹ave as template end */
    	
		resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
    	resultData.put("selectFromAcct", transferBank.getFromAccount() + " - "  + transferBank.getFromCurrency());//add by linrui for mul-ccy
		resultData.put("selectToAcct", transferBank.getToAccount() + " - "  + transferBank.getToCurrency());//add by linrui for mul-ccy
		setResultData(resultData);
		
		this.setUsrSessionDataValue("transferBank", transferBank);
	}

	public boolean approve(String txnType, String id, CibAction bean)
			throws NTBException {

		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpUser corpUser = (CorpUser) bean.getUser();
		if (txnType != null) {
			TransferBank transferBank = transferService.viewInBANK(id);
			/*Map exchangeMap = exRatesService.getExchangeRateFromHost(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), 8);
			String rateType = (String) exchangeMap
					.get(ExchangeRatesDao.RATE_TYPE);
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal showExchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap
					.get(ExchangeRatesDao.BUY_RATE);
			BigDecimal sellRate = (BigDecimal) exchangeMap
					.get(ExchangeRatesDao.SELL_RATE);

			// Jet modified. show only one exchange rate at the page, it should be always buy rate/sell rate 
			if (null == buyRate){
				buyRate = new BigDecimal("1");
			}
			if (null == sellRate){
				sellRate = new BigDecimal("1");
			}
			
			//add by lzg 20190604
			if(buyRate.compareTo(new BigDecimal("1")) == 1 &&(buyRate.compareTo(new BigDecimal("1")) == 0 || sellRate.compareTo(new BigDecimal("1")) == 0)){
				showExchangeRate = buyRate;
			}else if(sellRate.compareTo(new BigDecimal("1")) == 1 &&(buyRate.compareTo(new BigDecimal("1")) == 0 || sellRate.compareTo(new BigDecimal("1")) == 0)){
				showExchangeRate = sellRate;
			}else{
				showExchangeRate = buyRate.divide(sellRate, 8,
						BigDecimal.ROUND_DOWN);
				if(showExchangeRate.compareTo(new BigDecimal("1")) == -1){
					showExchangeRate = sellRate.divide(buyRate,8,BigDecimal.ROUND_DOWN);
				}
			}
			//add by lzg end
			
			if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
				exchangeRate = new BigDecimal("0");
			} else {
				exchangeRate = buyRate.divide(sellRate, 8,
						BigDecimal.ROUND_DOWN);
			}*/

			//transferBank.setExchangeRate(new Double(exchangeRate.doubleValue()));
			
			
			//add by lzg 20190902
			Map Host = transferService.toHostInBankTrial(transferBank,corpUser);
			transferBank.setFromAccount(((String)Host.get("DR_AC")).trim());
			transferBank.setToAccount(((String)Host.get("CR_AC")).trim());
			transferBank.setToName(((String)Host.get("CR_NM")).trim());
			transferBank.setDebitAmount(((BigDecimal)Host.get("TX_DR_AMT")).doubleValue());
			transferBank.setTransferAmount(((BigDecimal)Host.get("TX_CR_AMT")).doubleValue());
			transferBank.setExchangeRate(((BigDecimal)Host.get("EX_RAT")).doubleValue());
			//transferBank.setShowExchangeRate(((BigDecimal)Host.get("EX_RAT")).doubleValue());
			
			String currency = (String)Host.get("CCY");
			Double transferAmount = ((BigDecimal)Host.get("AMT")).doubleValue();
			
			
			String txnTypeBank = null;
			if(Constants.YES.equals(transferBank.getOwnerAccFlag())) {
				txnTypeBank = Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT;
			} else if(Constants.NO.equals(transferBank.getOwnerAccFlag())) {
				txnTypeBank = Constants.TXN_TYPE_TRANSFER_BANK;
			}
			
			/*String inputAmountFlag = transferBank.getInputAmountFlag();
			String currency = null;
			Double transferAmount = new Double(0);*/
			
			//add by lzg 20190708
			transferService.checkConvertibility(transferBank.getFromCurrency(),transferBank.getToCurrency());
			//add by lzg end
			
			/*if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferBank.getFromCurrency();
				transferAmount = transferBank.getDebitAmount();
								
				//jet modified for updating new transaction amount
				BigDecimal new_To_Amount_temp = exRatesService.getEquivalentFromHostRate(
						corpUser.getCorpId(), currency, transferBank
								.getToCurrency(), new BigDecimal(transferAmount
								.toString()), null, 2,exchangeMap);
				transferBank.setTransferAmount(new Double(new_To_Amount_temp.toString()));

			} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferBank.getToCurrency();
				transferAmount = transferBank.getTransferAmount();
								
				//jet modified for updating new transaction amount
				BigDecimal new_From_Amount_temp = exRatesService.getEquivalentFromHostRate(
						corpUser.getCorpId(), transferBank.getFromCurrency(),
						currency, null, new BigDecimal(transferAmount
								.toString()), 2,exchangeMap);
				transferBank.setDebitAmount(new Double(new_From_Amount_temp.toString()));
			}*/
			
			
			String language = corpUser.getLangCode();
			
			//add by lzg 20190619
			checkCutoffTimeAndSetMsg(transferBank,language);
			//add by lzg end
			
			BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
					.getCorpId(), currency, "MOP",
					new BigDecimal(transferAmount.toString()),
					null, 2);
			//add by lzg 20190604
			//transferBank.setExchangeRate(new Double(showExchangeRate.doubleValue()));
			//add by lzg end
			if (!transferLimitService.checkLimitQuota(transferBank
					.getFromAccount(), corpUser.getCorpId(), txnTypeBank,
					transferBank.getDebitAmount().doubleValue(),
					equivalentMOP.doubleValue())) {
                //write limit report
	        	Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("USER_ID", transferBank.getUserId());
				reportMap.put("CORP_ID", corpUser.getCorpId());
				reportMap.put("TRANS_TYPE", txnTypeBank);
				reportMap.put("LOCAL_TRANS_CODE", transferBank.getTransId());
				reportMap.put("FROM_CURRENCY", transferBank.getFromCurrency());
				reportMap.put("TO_CURRENCY", transferBank.getToCurrency());
				reportMap.put("FROM_AMOUNT", transferBank.getDebitAmount());
				reportMap.put("TO_AMOUNT", transferBank.getTransferAmount());
				reportMap.put("EXCEEDING_TYPE", "1");
				reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
				reportMap.put("FROM_ACCOUNT", transferBank.getFromAccount());
				reportMap.put("USER_LIMIT ", "0");
				reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
				reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
				throw new NTBWarningException("err.txn.ExceedDailyLimit");
			}
			
			Map fromHost = transferService.toHostInBANK(
					transferBank, corpUser, txnTypeBank);
			
			//add by long_zg 2015-01-28 for CR205 Add IP
			String ipAddress = bean.getRequestIP() ;
			fromHost.put("IP_ADDRESS", ipAddress) ;
			
			transferService.loadUploadBANK(transferBank, fromHost);
			
			//add by lzg 20190809
			Long JRNNO = (Long)fromHost.get("JRNNO");
			String serialNumber = null;
			if(JRNNO != null){
				serialNumber = JRNNO.toString();
			}
			if(serialNumber != null){
				serialNumber = Utils.prefixZero(serialNumber, 9);
			}
			transferBank.setSerialNumber(serialNumber);
			//add by lzg  end
			//add by lzg 20190531
			transferService.updateBANK(transferBank);
			//add by lzg end
            //send email to last approver or executor, add by wen_cy 20110511
			MailService mailService = (MailService) appContext
					.getBean("MailService");
			Map dataMap = new HashMap();
			this.convertPojo2Map(transferBank, dataMap);
			Map resultData = bean.getResultData();
			this.convertPojo2Map(transferBank, resultData);
			setResultData(resultData);
			if ("".equals(dataMap.get("remark"))) {
				dataMap.put("remark", "--");
			}
			dataMap.put("lastUpdateTime", new Date());
			
			/* Modify by long_zg 2015-05-22 UAT6-242 缂佹鍏涚粭渚�鎳撻崨鎵М閻犫晝鎯erator闁瑰瓨鍔曟慨娑㈡娴ｅ搫绻侀悘蹇斿敹ave as template begin */
			/*mailService.toLastApprover_Executor(Constants.TXN_SUBTYPE_TRANSFER_BANK, corpUser.getUserId(),dataMap);*/
			mailService.toLastApprover_Executor(txnTypeBank, corpUser.getUserId(),corpUser.getCorpId(), dataMap);
			/* Modify by long_zg 2015-05-22 UAT6-242 缂佹鍏涚粭渚�鎳撻崨鎵М閻犫晝鎯erator闁瑰瓨鍔曟慨娑㈡娴ｅ搫绻侀悘蹇斿敹ave as template begin */
			
			return true;
		} else {
			return false;
		}
	}

	public String viewDetail(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		TransferBank transferBank = transferService.viewInBANK(id);
		CorpUser corpUser = (CorpUser) bean.getUser();
		//modified by lzg 20190507
		/*Map exchangeMap = exRatesService.getExchangeRateFromHost(corpUser
				.getCorpId(), transferBank.getFromCurrency(), transferBank
				.getToCurrency(), 8);
		String rateType = (String) exchangeMap
				.get(ExchangeRatesDao.RATE_TYPE);
		BigDecimal exchangeRate = new BigDecimal("0");
		BigDecimal showExchangeRate = new BigDecimal("0");
		BigDecimal buyRate = (BigDecimal) exchangeMap
		.get(ExchangeRatesDao.BUY_RATE);
		BigDecimal sellRate = (BigDecimal) exchangeMap
		.get(ExchangeRatesDao.SELL_RATE);
		if (null == buyRate){
			buyRate = new BigDecimal("1");
		}
		if (null == sellRate){
			sellRate = new BigDecimal("1");
		}
		
		if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
			exchangeRate = new BigDecimal("0");
		} else {
			exchangeRate = buyRate.divide(sellRate, 8,
					BigDecimal.ROUND_DOWN);
		}*/

		Map resultData = bean.getResultData();
		//modified by lzg 20190612
		//convertPojo2Map(transferBank, resultData);
		//modified by lzg 20190612
		// edit by mxl 0819
		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");
		
		
		//add by lzg 20190902
		Map Host = transferService.toHostInBankTrial(transferBank,corpUser);
		transferBank.setFromAccount(((String)Host.get("DR_AC")).trim());
		transferBank.setToAccount(((String)Host.get("CR_AC")).trim());
		transferBank.setToName(((String)Host.get("CR_NM")).trim());
		//transferBank.setExchangeRate(((BigDecimal)Host.get("EX_RAT")).doubleValue());
		transferBank.setShowExchangeRate(((BigDecimal)Host.get("EX_RAT")).doubleValue());
		
		resultData.put("newFromAmount", ((BigDecimal)Host.get("TX_DR_AMT")).doubleValue());
		resultData.put("newToAmount", ((BigDecimal)Host.get("TX_CR_AMT")).doubleValue());
		resultData.put("newExchangeRate", transferBank.getShowExchangeRate());
		//add by lzg end
		
		
		String inputAmountFlag = transferBank.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferBank.getFromCurrency();
			transferAmount = ((BigDecimal)Host.get("TX_DR_AMT")).doubleValue();

			//jet modified for showing new transaction amount
			/*Double new_From_Amount = transferBank.getDebitAmount();			
			BigDecimal new_To_Amount_temp = exRatesService.getEquivalentFromHostRate(
					corpUser.getCorpId(), currency,
					transferBank.getToCurrency(), new BigDecimal(transferAmount
							.toString()), null, 2,exchangeMap);
			Double new_To_Amount = new Double(new_To_Amount_temp.toString());
			resultData.put("newFromAmount", new_From_Amount);
			resultData.put("newToAmount", new_To_Amount);*/
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferBank.getToCurrency();
			transferAmount = ((BigDecimal)Host.get("TX_CR_AMT")).doubleValue();

			//jet modified for showing new transaction amount
			/*Double new_To_Amount = transferBank.getTransferAmount();			
			BigDecimal new_From_Amount_temp = exRatesService.getEquivalentFromHostRate(
					corpUser.getCorpId(), transferBank.getFromCurrency(),
					currency, null, new BigDecimal(transferAmount.toString()),
					2,exchangeMap);
			Double new_From_Amount = new Double(new_From_Amount_temp.toString());
			resultData.put("newFromAmount", new_From_Amount);
			resultData.put("newToAmount", new_To_Amount);*/
		}
		
		
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), currency, "MOP",
				new BigDecimal(transferAmount.toString()),
				null, 2);

		/*resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);*/
		String txtType = Constants.NO.equals(transferBank.getOwnerAccFlag())?Constants.TXN_SUBTYPE_TRANSFER_BANK:Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT ;
		resultData.put("txnType", txtType );
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);
		//add by lzg 20190604
		/*if(transferBank.getExchangeRate() < 1){
			transferBank.setExchangeRate(transferBank.getShowExchangeRate());
		}*/
		//add by lzg 20190612
		convertPojo2Map(transferBank, resultData);
		//add by lzg 20190612
		/*if(buyRate.compareTo(new BigDecimal("1")) == 1 &&(buyRate.compareTo(new BigDecimal("1")) == 0 || sellRate.compareTo(new BigDecimal("1")) == 0)){
			showExchangeRate = buyRate;
		}else if(sellRate.compareTo(new BigDecimal("1")) == 1 &&(buyRate.compareTo(new BigDecimal("1")) == 0 || sellRate.compareTo(new BigDecimal("1")) == 0)){
			showExchangeRate = sellRate;
		}else{
			showExchangeRate = buyRate.divide(sellRate, 8,
					BigDecimal.ROUND_DOWN);
			if(showExchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate,8,BigDecimal.ROUND_DOWN);
			}
		}*/
		//resultData.put("newExchangeRate", exchangeRate);
		//resultData.put("newExchangeRate", showExchangeRate);
		//add by lzg end
		
		bean.setResultData(resultData);
		return "/WEB-INF/pages/txn/transfer_account/transfer_InBank_approval_view.jsp";
	}

	public boolean reject(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		if (txnType != null) {
			TransferBank transferBank = transferService.viewInBANK(id);
			transferService.rejectBank(transferBank);
			return true;
		} else {
			return false;
		}
	}

	public void saveTemplate() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		String transId = Utils.null2EmptyWithTrim(this.getParameter("transId"));
		TransferBank transferBank = transferService.viewInBANK(transId);
		// add by mxl 0913
		transferBank.setTransId(CibIdGenerator.getRefNoForTransaction());
		transferBank.setRecordType(TransferBank.TRANSFER_TYPE_TEMPLATE);

		transferService.templateInBANK1to1(transferBank);
	}

	public boolean cancel(String txnType, String id, CibAction bean)
			throws NTBException {
		return reject(txnType, id, bean);
	}

	private void checkCutoffTimeAndSetMsg(TransferBank transferBank,String language)
			throws NTBException {
		//modified by lzg 20190619
		/*ApplicationContext appContext = Config.getAppContext();
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext
				.getBean("CutOffTimeService");
		String fromAcctypeCode = null;
		String toAcctypeCode = null;
		if (transferBank.getFromAcctype().equals(
				CorpAccount.ACCOUNT_TYPE_CURRENT)
				|| transferBank.getFromAcctype().equals(
						CorpAccount.ACCOUNT_TYPE_OVER_DRAFT)) {
			fromAcctypeCode = "C";
		} else if (transferBank.getFromAcctype().equals(
				CorpAccount.ACCOUNT_TYPE_SAVING)) {
			fromAcctypeCode = "S";
		}
		if (transferBank.getToAcctype().equals(CorpAccount.ACCOUNT_TYPE_CURRENT)
				|| transferBank.getToAcctype().equals(
						CorpAccount.ACCOUNT_TYPE_OVER_DRAFT)) {
			toAcctypeCode = "C";
		} else if (transferBank.getToAcctype().equals(
				CorpAccount.ACCOUNT_TYPE_SAVING)) {
			toAcctypeCode = "S";
		}
		String txnCode = "51" + fromAcctypeCode + toAcctypeCode;
		setMessage(cutOffTimeService.check(txnCode, transferBank
				.getFromCurrency(), transferBank.getToCurrency()));*/
		ApplicationContext appContext = Config.getAppContext();
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext
				.getBean("CutOffTimeService");
		
		
		// check value date cut-off time
		String transferType = "W";
		String message = cutOffTimeService.check("51CC", transferBank
				.getFromCurrency(), transferBank.getToCurrency(),transferType,language);
		if(message != null){
			throw new NTBWarningException(message);
		}
		//modified by lzg end
	}
	
	private void checkDuplicatedInput(String transferType,TransferBank transferBank) throws NTBException{
		
		TransferService transferService = (TransferService) Config.getAppContext().getBean("TransferService");
		List transactionList = transferService.listTransferEntity4CheckDuplicatedInput(transferType);
		
		boolean fla = false;
		Iterator it = transactionList.iterator();
		Map map = null;
		String fromAccount="";
		String toAccount="";
		BigDecimal amount=null;
		String clientReference="";
		
		while(it.hasNext()){
			map = (HashMap)it.next();
			
			//all these fields below will be compared with database
			fromAccount = (String)map.get("FROM_ACCOUNT");
			toAccount = (String)map.get("TO_ACCOUNT");
			amount = (BigDecimal)map.get("TRANSFER_AMOUNT");
			clientReference=Utils.null2EmptyWithTrim((String)map.get("REMARK"));
			
			String clientRef = Utils.null2EmptyWithTrim(transferBank.getRemark());
			if(!"".equals(clientRef)){
				if(transferBank.getFromAccount().equals(fromAccount)&&
						transferBank.getToAccount().equals(toAccount)&&
						transferBank.getTransferAmount().doubleValue()==amount.doubleValue()&&
						clientRef.equals(clientReference)){
					fla = true;
					break;
				}
			}else{
				if(transferBank.getFromAccount().equals(fromAccount)&&
						transferBank.getToAccount().equals(toAccount)&&
						transferBank.getTransferAmount().doubleValue()==amount.doubleValue()){
					fla = true;
					break;
				}
			}
			
		}
		
		if(fla){
			throw new NTBException("error.txn.duplicatedinput");
		}
		
	}
	
}
