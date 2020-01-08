package app.cib.action.txn;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Encryption;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bnk.BankUser;
import app.cib.bo.flow.FlowProcess;
import app.cib.bo.srv.TxnPrompt;
import app.cib.bo.sys.AbstractCorpUser;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TransferBank;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
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
import app.cib.service.txn.TransferTemplateService;
import app.cib.util.Constants;
import app.cib.util.RcCurrency;
import app.cib.util.TransAmountFormate;
import app.cib.util.UploadReporter;
import app.cib.util.otp.SMSOTPObject;
import app.cib.util.otp.SMSOTPUtil;
import app.cib.util.otp.SMSReturnObject;
import app.cib.core.CibIdGenerator;
import app.cib.dao.enq.ExchangeRatesDao;
import app.cib.dao.srv.TransferPromptDao;

import com.neturbo.set.exception.NTBWarningException;

public class TransferTemplateInBankAction extends CibAction implements Approvable{

	private TransferBank transferBank;

	public void listTemplate() throws NTBException {
		setResultData(new HashMap(1));

		String corpID = null;
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");

		CorpUser corpUser = (CorpUser) getUser();
		corpID = corpUser.getCorpId();
		
		
		/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		String ownerAccFlag = Utils.null2Empty(this.getParameter("ownerAccFlag")) ;
		/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */

		/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		/*List templateList = transferTemplateService.listTemplateBANK(corpID);*/
		List templateList = null ;
		if(Constants.NO.equalsIgnoreCase(ownerAccFlag)){
			templateList = transferTemplateService.listTemplateBANK(corpID,Constants.NO);
		} else {
			templateList = transferTemplateService.listTemplateBANK(corpID,Constants.YES);
		}
		
		/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		String recordFlag = null;
		if ((templateList.size()== 0) || (templateList == null)){
			recordFlag = "Y";
		} else {
			recordFlag = "N";
		}
		for (int i = 0; i < templateList.size(); i++) {
			transferBank = (TransferBank) templateList.get(i);

		}
		templateList = this.convertPojoList2MapList(templateList);

		Map resultData = new HashMap();

		resultData.put("templateList", templateList);
		resultData.put("recordFlag", recordFlag);
		//resultData.put("recordFlag", "Y");;
		setResultData(resultData);
	}

	public void addTemplateLoad() throws NTBException {
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
//		setResultData(new HashMap(1));
	}
	public void addTemplateLoad3rd() throws NTBException {
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
//		setResultData(new HashMap(1));
	}

	public void addTemplate() throws NTBException {
		// initial service
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");		
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		TransAmountService transAmountService = (TransAmountService) appContext.getBean("TransAmountService");

		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = (CorpUser) this.getUser();
		corpUser.setLanguage(lang);//add by linrui for mul-language
		TransferBank transferBank = new TransferBank(CibIdGenerator
				.getRefNoForTransaction());
		this.convertMap2Pojo(this.getParameters(), transferBank);
		transferBank.setUserId(corpUser.getUserId());
		transferBank.setCorpId(corpUser.getCorpId());

		CorpAccount corpAccount = corpAccountService
				.viewCorpAccount(transferBank.getFromAccount());
//		transferBank.setFromCurrency(corpAccount.getCurrency());
		transferBank.setFromAcctype(corpAccount.getAccountType());
		//add by linrui for mul-language 
        Double transAmt  = TransAmountFormate.formateAmount(this.getParameter("transferAmount"),lang);
        Double debitAmt = TransAmountFormate.formateAmount(this.getParameter("debitAmount"), lang);
        transferBank.setTransferAmount(transAmt);
        transferBank.setDebitAmount(debitAmt);
        //end
		//to account and to currency... 
		String toAccount = this.getParameter("toAccount");
		String toAccount2 = this.getParameter("toAccount2");
		String toName = "";
		
		/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		String ownerAccFlag = Utils.null2Empty(this.getParameter("ownerAccFlag")) ;
		/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
		
		/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		/*if (toAccount.equals("0")) {*/
		if (Constants.NO.equalsIgnoreCase(ownerAccFlag)) {
		/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			//add by wcc 20190402
			
			/* Modify by long_zg 2015-05-25 UAT6-292 COB锛氱涓夎�杞夎超寰炴牳蹇冩嬁鎴跺悕閷锛屽皫鑷翠氦鏄撳牨CM1013 begin */
			/*toName = this.getParameter("toName");	*/
			Map curMap = transferService.viewCurrentDetail(toAccount) ;
			toName = (String ) curMap.get("SHORT_NAME") ;
			transferBank.setToName(toName);
			/* Modify by long_zg 2015-05-25 UAT6-292 COB锛氱涓夎�杞夎超寰炴牳蹇冩嬁鎴跺悕閷锛屽皫鑷翠氦鏄撳牨CM1013 begin */
			
			/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			/*transferBank.setToAccount(toAccount2);
			transferBank.setOwnerAccFlag("N");*/
			//transferBank.setOwnerAccFlag(Constants.NO);
			/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			
			/* get the currency of the toAccount and accType from the fromHost */
			Map fromHost = transferService.toHostAccountInBANK(transferBank
					.getTransId(), corpUser, transferBank.getToAccount());
			String currencyCode = Utils.null2EmptyWithTrim(this.getParameter("toCurrency"));
			String toAccType = (String) fromHost.get("PRODUCT_TYPE");
			if (CorpAccount.APPLICATION_CODE_CURRENT.equals(toAccType)) {
				transferBank.setToAcctype(CorpAccount.ACCOUNT_TYPE_CURRENT);
			} else if (CorpAccount.APPLICATION_CODE_SAVING.equals(toAccType)) {
				transferBank.setToAcctype(CorpAccount.ACCOUNT_TYPE_SAVING);
			} else if (CorpAccount.APPLICATION_CODE_OVERDRAFT.equals(toAccType)) {
				transferBank.setToAcctype(CorpAccount.ACCOUNT_TYPE_OVER_DRAFT);
			}

			RcCurrency rcCurrency = new RcCurrency();
			transferBank.setToCurrency(rcCurrency.getCcyByCbsNumber(currencyCode));

		} else {
			//add by wcc 20190402
			String corpId = corpUser.getCorpId();
			toName = transferService.getBenName(corpId);
			transferBank.setToName(toName);
			//end
			transferBank.setToAccount(toAccount);
			corpAccount = corpAccountService.viewCorpAccount(transferBank
					.getToAccount());
//			transferBank.setToCurrency(corpAccount.getCurrency());
			transferBank.setToAcctype(corpAccount.getAccountType());
			/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			/*transferBank.setOwnerAccFlag("Y");*/
			transferBank.setOwnerAccFlag(Constants.YES);
			/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		}
		
		//add by lzg 20190708
		transferService.checkConvertibility(transferBank.getFromCurrency(), transferBank.getToCurrency());
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
			
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferBank.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferBank.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/

		} else if (transferBank.getDebitAmount().doubleValue() == 0) {
			transferBank.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_TO);
			transferBank.setFlag("2");
			BigDecimal toAmount = new BigDecimal(transferBank
					.getTransferAmount().toString());
			BigDecimal fromAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), null, toAmount, 2);

			transferBank.setDebitAmount(new Double(fromAmount.toString()));
			
			transAmountService.checkMinTransAmt(transferBank.getToCurrency(), transferBank.getTransferAmount().doubleValue());
			
			
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
			
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferBank.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferBank.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
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
		
		
//		transferBank.setRequestTime(new Date());
//		transferBank.setExecuteTime(new Date());
		transferBank.setRecordType(TransferBank.TRANSFER_TYPE_TEMPLATE);
		transferBank.setOperation(Constants.OPERATION_NEW);
		
		if (transferBank.getFromAccount().equals(transferBank.getToAccount())&&transferBank.getFromCurrency().equals(transferBank.getToCurrency())) {
			throw new NTBException("err.txn.TransferAccError");
		}

		transferBank.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
		transferBank.setStatus(Constants.STATUS_NORMAL);
		transferBank.setRecordType(TransferBank.TRANSFER_TYPE_TEMPLATE);
		transferBank.setOperation(Constants.OPERATION_NEW);

		Map resultData = new HashMap();
		this.setResultData(resultData);
		this.convertPojo2Map(transferBank, resultData);

		this.setUsrSessionDataValue("transferBank", transferBank);
	}

	public void addTemplateConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");

		TransferBank transferBank = (TransferBank) this
				.getUsrSessionDataValue("transferBank");

		transferBank.setRequestTime(new Date());
		transferBank.setExecuteTime(new Date());
		
		transferTemplateService.addTemplateBANK(transferBank);

		// for display 
		Map resultData = new HashMap();
		setResultData(resultData);
		this.convertPojo2Map(transferBank, resultData);
	}

	public void addCancel() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferBank transferBank = (TransferBank) this.getUsrSessionDataValue("transferBank");
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
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
		
		/*if("N".equals(transferBank.getOwnerAccFlag())){
			resultData.put("toAccount", "0");
		}*/
		
		String toAccount2 = null;
		toAccount2 = transferBank.getToAccount();
    	resultData.put("toAccount2", toAccount2);
    	CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
    	resultData.put("selectFromAcct", transferBank.getFromAccount() + " - "  + transferBank.getFromCurrency());//add by linrui for mul-ccy
		resultData.put("selectToAcct", transferBank.getToAccount() + " - "  + transferBank.getToCurrency());//add by linrui for mul-ccy
		setResultData(resultData);
		resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		this.setUsrSessionDataValue("transferBank", transferBank);
	}
	
	public void transferTemplateLoad() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		setResultData(new HashMap(1));
		String transID = getParameter("transId");
		/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
        /*String toAccount2 = null;*/
        /* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
		TransferBank transferBank = transferTemplateService.viewTemplate(transID);
		
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
		/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		/*toAccount2 = transferBank.getToAccount();
		resultData.put("toAccount2", toAccount2);*/
		/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("selectFromAcct", transferBank.getFromAccount() + " - "  + transferBank.getFromCurrency());//add by linrui for mul-ccy
		resultData.put("selectToAcct", transferBank.getToAccount() + " - "  + transferBank.getToCurrency());//add by linrui for mul-ccy
		
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
		this.setUsrSessionDataValue("transferBank", transferBank);
	}
	public void transferTemplateLoad3rd() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		setResultData(new HashMap(1));
		String transID = getParameter("transId");
		/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		/*String toAccount2 = null;*/
		/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
		TransferBank transferBank = transferTemplateService.viewTemplate(transID);
		
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
		/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		/*toAccount2 = transferBank.getToAccount();
		resultData.put("toAccount2", toAccount2);*/
		/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("selectFromAcct", transferBank.getFromAccount() + " - "  + transferBank.getFromCurrency());//add by linrui for mul-ccy
		resultData.put("selectToAcct", transferBank.getToAccount() + " - "  + transferBank.getToCurrency());//add by linrui for mul-ccy
		
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
		this.setUsrSessionDataValue("transferBank", transferBank);
	}

	public void transferTemplate() throws NTBException {
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

		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = (CorpUser) this.getUser();
		corpUser.setLanguage(lang);//add by linrui for mul-language
		TransferBank transferBank = (TransferBank) this.getUsrSessionDataValue("transferBank");
		Map map = this.getParameters() ;
		this.convertMap2Pojo(this.getParameters(), transferBank);
		transferBank.setUserId(corpUser.getUserId());
		transferBank.setCorpId(corpUser.getCorpId());
		transferBank.setTransId(CibIdGenerator.getRefNoForTransaction());	
//		transferBank.setRequestTime(new Date());
//		transferBank.setExecuteTime(new Date());
		
		/* get currency accounding to account 0807 */
		CorpAccount corpAccount = corpAccountService.viewCorpAccount(transferBank.getFromAccount());
//		transferBank.setFromCurrency(corpAccount.getCurrency());
		transferBank.setFromAcctype(corpAccount.getAccountType());

		/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		/*String toAccount = this.getParameter("toAccount");
		String toAccount2 = this.getParameter("toAccount2");

		if (toAccount.equals("0")) {
			transferBank.setToAccount(toAccount2);
			transferBank.setOwnerAccFlag("N");*/
		String ownerAccFlag = this.getParameter("ownerAccFlag") ;
		
		String toName = "" ;
		if (Constants.NO.equalsIgnoreCase(ownerAccFlag)) {
		/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			/*
			 * get the currency of the toAccount and accType from the fromHost
			 * executing the accountEnquriy 0819 add by mxl
			 */
			
			/* Modify by long_zg 2015-05-25 UAT6-292 COB锛氱涓夎�杞夎超寰炴牳蹇冩嬁鎴跺悕閷锛屽皫鑷翠氦鏄撳牨CM1013 begin */
			/*toName = this.getParameter("toName");	*/
			Map curMap = transferService.viewCurrentDetail(transferBank.getToAccount()) ;
			toName = (String ) curMap.get("SHORT_NAME") ;
			transferBank.setToName(toName);
			/* Modify by long_zg 2015-05-25 UAT6-292 COB锛氱涓夎�杞夎超寰炴牳蹇冩嬁鎴跺悕閷锛屽皫鑷翠氦鏄撳牨CM1013 begin */
			
			Map fromHost = transferService.toHostAccountInBANK(transferBank
					.getTransId(), corpUser, transferBank.getToAccount());
			String currencyCode = (String) fromHost.get("CURRENCY_CODE");
			String toAccType = (String) fromHost.get("APPLICATION_CODE");
			if (CorpAccount.APPLICATION_CODE_CURRENT.equals(toAccType)) {
				transferBank.setToAcctype(CorpAccount.ACCOUNT_TYPE_CURRENT);
			} else if (CorpAccount.APPLICATION_CODE_SAVING.equals(toAccType)) {
				transferBank.setToAcctype(CorpAccount.ACCOUNT_TYPE_SAVING);
			} else if (CorpAccount.APPLICATION_CODE_OVERDRAFT.equals(toAccType)) {
				transferBank.setToAcctype(CorpAccount.ACCOUNT_TYPE_OVER_DRAFT);
			}
			RcCurrency rcCurrency = new RcCurrency();
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			currencyCode = this.getParameter("toCurrency") ;
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			transferBank.setToCurrency(rcCurrency.getCcyByCbsNumber(currencyCode));
			transferBank.setOwnerAccFlag(Constants.YES);
		} else {
			/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			
			String corpId = corpUser.getCorpId();
			toName = transferService.getBenName(corpId);
			transferBank.setToName(toName);
			
			/*transferBank.setToAccount(toAccount);
			transferBank.setOwnerAccFlag("N");*/
			transferBank.setOwnerAccFlag(Constants.YES);
			/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			corpAccount = corpAccountService.viewCorpAccount(transferBank
					.getToAccount());
//			transferBank.setToCurrency(corpAccount.getCurrency());
			transferBank.setToAcctype(corpAccount.getAccountType());
			
		}
		
		//add by lzg 20190708
		transferService.checkConvertibility(transferBank.getFromCurrency(), transferBank.getToCurrency());
		//add by lzg end
		
		if (transferBank.getTransferAmount().doubleValue() == 0) {
			transferBank.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_FROM);
			BigDecimal transferAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), new BigDecimal(transferBank
					.getDebitAmount().toString()), null, 2);

			transferBank
					.setTransferAmount(new Double(transferAmount.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinTransAmt(transferBank.getFromCurrency(), transferBank.getDebitAmount().doubleValue());
			
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
			
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferBank.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferBank.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
		} else if (transferBank.getDebitAmount().doubleValue() == 0) {
			transferBank.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_TO);
			BigDecimal toAmount = new BigDecimal(transferBank
					.getTransferAmount().toString());
			BigDecimal fromAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), null, toAmount, 2);

			transferBank.setDebitAmount(new Double(fromAmount.toString()));
			
			transAmountService.checkMinTransAmt(transferBank.getToCurrency(), transferBank.getTransferAmount().doubleValue());
			
			
			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferBank.getToCurrency(), transferBank
					.getFromCurrency(), 8);
			String rateType = (String) exchangeMap
					.get(ExchangeRatesDao.RATE_TYPE);
			BigDecimal exchangeRate = new BigDecimal("0");
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
			}
			transferBank.setExchangeRate(new Double(exchangeRate.doubleValue()));
			
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferBank.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferBank.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
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
		
		//add by lzg 20190619
		checkCutoffTimeAndSetMsg(transferBank,corpUser.getLangCode());
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
				reportMap.put("USER_ID",  corpUser.getUserId());
				reportMap.put("CORP_ID", corpUser.getCorpId());
				reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT);
				reportMap.put("LOCAL_TRANS_CODE", transferBank.getTransId());
				reportMap.put("FROM_CURRENCY", transferBank.getFromCurrency());
				reportMap.put("FROM_ACCOUNT", transferBank.getFromAccount());
				reportMap.put("TO_CURRENCY", transferBank.getToCurrency());
				reportMap.put("FROM_AMOUNT", transferBank.getDebitAmount());
				reportMap.put("TO_AMOUNT", transferBank.getTransferAmount());
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
				Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("USER_ID",  corpUser.getUserId());
				reportMap.put("CORP_ID", corpUser.getCorpId());
				reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_BANK);
				reportMap.put("LOCAL_TRANS_CODE", transferBank.getTransId());
				reportMap.put("FROM_CURRENCY", transferBank.getFromCurrency());
				reportMap.put("FROM_ACCOUNT", transferBank.getFromAccount());
				reportMap.put("TO_CURRENCY", transferBank.getToCurrency());
				reportMap.put("FROM_AMOUNT", transferBank.getDebitAmount());
				reportMap.put("TO_AMOUNT", transferBank.getTransferAmount());
				reportMap.put("EXCEEDING_TYPE", "0");
				reportMap.put("LIMIT_TYPE", "");
				reportMap.put("USER_LIMIT ", corpUser.getUserLimit(Constants.TXN_TYPE_TRANSFER_BANK));
				reportMap.put("DAILY_LIMIT ", new Double(0));
				reportMap.put("TOTAL_AMOUNT ", new Double(0));
				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
				throw new NTBWarningException("err.txn.ExceedUserLimit");
			}
		}
		String txnTypeBank = null;
		if(transferBank.getOwnerAccFlag().equals("Y")) {
			txnTypeBank = Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT;
		} else if(transferBank.getOwnerAccFlag().equals("N")) {
			txnTypeBank = Constants.TXN_TYPE_TRANSFER_BANK;
		}
		// check daily limit
		if (!transferLimitService.checkCurAmtLimitQuota(transferBank
				.getFromAccount(), corpUser.getCorpId(), txnTypeBank,
				transferBank.getDebitAmount().doubleValue(),
				equivalentMOP.doubleValue())) {
			 //write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", corpUser.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", txnTypeBank);
			reportMap.put("LOCAL_TRANS_CODE", transferBank.getTransId());
			reportMap.put("FROM_CURRENCY", transferBank.getFromCurrency());
			reportMap.put("FROM_ACCOUNT", transferBank.getFromAccount());
			reportMap.put("TO_CURRENCY", transferBank.getToCurrency());
			reportMap.put("FROM_AMOUNT", transferBank.getDebitAmount());
			reportMap.put("TO_AMOUNT", transferBank.getTransferAmount());
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
		
		// edit by mxl 0819
		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);
		/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		/*if (transferBank.getOwnerAccFlag().equals("N")) {
        	toAccount2 = transferBank.getToAccount();
        }
        resultData.put("toAccount2", toAccount2);*/
        /* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
        
		this.convertPojo2Map(transferBank, resultData);

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

	public void transferTemplateConfirm() throws NTBException {

		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		MailService mailService = (MailService) appContext.getBean("MailService");
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
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), currency, "MOP",
				new BigDecimal(transferAmount.toString()),
				null, 2);
		
		// add by chen_y for CR225 20170412
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		
		String txnTypeBank = null;
		if(transferBank.getOwnerAccFlag().equals("Y")) {
			txnTypeBank = Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT;
		} else if(transferBank.getOwnerAccFlag().equals("N")) {
			txnTypeBank = Constants.TXN_TYPE_TRANSFER_BANK;
		}
		
		if (!transferLimitService.checkLimitQuota(transferBank
				.getFromAccount(), corpUser.getCorpId(), txnTypeBank,
				transferBank.getDebitAmount().doubleValue(),
				equivalentMOP.doubleValue())) {
			 //write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", corpUser.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", txnTypeBank);
			reportMap.put("LOCAL_TRANS_CODE", transferBank.getTransId());
			reportMap.put("FROM_CURRENCY", transferBank.getFromCurrency());
			reportMap.put("FROM_ACCOUNT", transferBank.getFromAccount());
			reportMap.put("TO_CURRENCY", transferBank.getToCurrency());
			reportMap.put("FROM_AMOUNT", transferBank.getDebitAmount());
			reportMap.put("TO_AMOUNT", transferBank.getTransferAmount());
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

		String processId = flowEngineService.startProcess(
				/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
				/*Constants.TXN_SUBTYPE_TRANSFER_BANK,*/
				txnTypeBank ,
				/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
				FlowEngineService.TXN_CATEGORY_FINANCE,
				TransferTemplateInBankAction.class, transferBank.getFromCurrency(),
				transferBank.getDebitAmount().doubleValue(), transferBank.getToCurrency(), transferBank.getTransferAmount().doubleValue(),equivalentMOP
				.doubleValue(), transferBank.getTransId(),transferBank
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
//			transferBank.setRecordType(transferBank.TRANSFER_TYPE_GENERAL);
			
//			transferService.editBANK(transferBank, corpUser.getUserId());

			// edit by mxl 0819
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");

			/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			/*resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);*/
			resultData.put("txnType", txnTypeBank);
			/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);

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
			/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_TRANSFER_BANK, userName,corpUser.getCorpId(), dataMap);*/
			//mailService.toApprover_Seleted(txnTypeBank, userName,corpUser.getCorpId(), dataMap);
			/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			throw new NTBException("err.txn.TranscationFaily");
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
	
	
    public void payCancel() throws NTBException {
    	this.addCancel();
    }
	
	public void editTemplateLoad() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext.getBean("TransferTemplateService");
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		String transID = getParameter("transId");
		String toAccount2 = null;
		TransferBank transferBank = transferTemplateService.viewTemplate(transID);
		
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
		/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		/*toAccount2 = transferBank.getToAccount();
    	resultData.put("toAccount2", toAccount2);
		if("N".equals(transferBank.getOwnerAccFlag())){
			resultData.put("toAccount", "0");
		}*/
		/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
		
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("selectFromAcct", transferBank.getFromAccount() + " - "  + transferBank.getFromCurrency());//add by linrui for mul-ccy
		resultData.put("selectToAcct", transferBank.getToAccount() + " - "  + transferBank.getToCurrency());//add by linrui for mul-ccy

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
		this.setUsrSessionDataValue("transferBank", transferBank);
	}
	public void editTemplateLoad3rd() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext.getBean("TransferTemplateService");
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		String transID = getParameter("transId");
		String toAccount2 = null;
		TransferBank transferBank = transferTemplateService.viewTemplate(transID);
		
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
		/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		/*toAccount2 = transferBank.getToAccount();
    	resultData.put("toAccount2", toAccount2);
		if("N".equals(transferBank.getOwnerAccFlag())){
			resultData.put("toAccount", "0");
		}*/
		/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
		
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("selectFromAcct", transferBank.getFromAccount() + " - "  + transferBank.getFromCurrency());//add by linrui for mul-ccy
		resultData.put("selectToAcct", transferBank.getToAccount() + " - "  + transferBank.getToCurrency());//add by linrui for mul-ccy
		
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
		this.setUsrSessionDataValue("transferBank", transferBank);
	}
		
	public void editTemplate() throws NTBException {
		TransferBank transferBank = (TransferBank) this.getUsrSessionDataValue("transferBank");
		this.convertMap2Pojo(this.getParameters(), transferBank);
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");		
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		TransAmountService transAmountService = (TransAmountService) appContext.getBean("TransAmountService");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = (CorpUser) this.getUser();
		corpUser.setLanguage(lang);//add by linrui for mul-language
		//from account and from currency		
		CorpAccount corpAccount = corpAccountService.viewCorpAccount(transferBank.getFromAccount());
//		transferBank.setFromCurrency(corpAccount.getCurrency());
		transferBank.setFromAcctype(corpAccount.getAccountType());
		//add by linrui for mul-language 
        Double transAmt  = TransAmountFormate.formateAmount(this.getParameter("transferAmount"),lang);
        Double debitAmt = TransAmountFormate.formateAmount(this.getParameter("debitAmount"), lang);
        transferBank.setTransferAmount(transAmt);
        transferBank.setDebitAmount(debitAmt);
        //end
		//to account and to 
        /* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		/*String toAccount = this.getParameter("toAccount");
		String toAccount2 = this.getParameter("toAccount2");
		String toName = "";
		if (toAccount.equals("0")) {
		toName = this.getParameter("toName");
		transferBank.setToName(toName);
		//end
		transferBank.setToAccount(toAccount2);
		transferBank.setOwnerAccFlag("N");
		*/
        String toName = "";
        
        String ownerAccFlag = this.getParameter("ownerAccFlag") ;
        
        if (Constants.NO.equalsIgnoreCase(ownerAccFlag)) {
			//transferBank.setOwnerAccFlag(Constants.NO);
		/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			
			/* Modify by long_zg 2015-05-25 UAT6-292 COB锛氱涓夎�杞夎超寰炴牳蹇冩嬁鎴跺悕閷锛屽皫鑷翠氦鏄撳牨CM1013 begin */
			/*toName = this.getParameter("toName");	*/
			Map curMap = transferService.viewCurrentDetail(transferBank.getToAccount()) ;
			toName = (String ) curMap.get("SHORT_NAME") ;
			transferBank.setToName(toName);
			/* Modify by long_zg 2015-05-25 UAT6-292 COB锛氱涓夎�杞夎超寰炴牳蹇冩嬁鎴跺悕閷锛屽皫鑷翠氦鏄撳牨CM1013 begin */
			
			/* get the currency of the toAccount and accType from the fromHost */
			Map fromHost = transferService.toHostAccountInBANK(transferBank
					.getTransId(), corpUser, transferBank.getToAccount());
			String currencyCode = Utils.null2EmptyWithTrim(this.getParameter("toCurrency"));
			String toAccType = (String) fromHost.get("APPLICATION_CODE");
			if (CorpAccount.APPLICATION_CODE_CURRENT.equals(toAccType)) {
				transferBank.setToAcctype(CorpAccount.ACCOUNT_TYPE_CURRENT);
			} else if (CorpAccount.APPLICATION_CODE_SAVING.equals(toAccType)) {
				transferBank.setToAcctype(CorpAccount.ACCOUNT_TYPE_SAVING);
			} else if (CorpAccount.APPLICATION_CODE_OVERDRAFT.equals(toAccType)) {
				transferBank.setToAcctype(CorpAccount.ACCOUNT_TYPE_OVER_DRAFT);
			}

			RcCurrency rcCurrency = new RcCurrency();
			transferBank.setToCurrency(rcCurrency.getCcyByCbsNumber(currencyCode));

		} else {
			//add by wcc 20190402
			String corpId = corpUser.getCorpId();
			toName = transferService.getBenName(corpId);
			transferBank.setToName(toName);
			//end
			/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template beign */
			/*transferBank.setToAccount(toAccount);
			 * transferBank.setOwnerAccFlag("Y");*/
			transferBank.setOwnerAccFlag(Constants.YES);
			/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			corpAccount = corpAccountService.viewCorpAccount(transferBank
					.getToAccount());
//			transferBank.setToCurrency(corpAccount.getCurrency());
			transferBank.setToAcctype(corpAccount.getAccountType());
			
		}
        
      //add by lzg 20190708
		transferService.checkConvertibility(transferBank.getFromCurrency(), transferBank.getToCurrency());
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
			
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferBank.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferBank.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/

		} else if (transferBank.getDebitAmount().doubleValue() == 0) {
			transferBank.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_TO);
			transferBank.setFlag("2");
			BigDecimal toAmount = new BigDecimal(transferBank
					.getTransferAmount().toString());
			BigDecimal fromAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), null, toAmount, 2);

			transferBank.setDebitAmount(new Double(fromAmount.toString()));
			
			transAmountService.checkMinTransAmt(transferBank.getToCurrency(), transferBank.getTransferAmount().doubleValue());

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
			
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferBank.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferBank.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
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
		
		
		transferBank.setRequestTime(new Date());
		transferBank.setExecuteTime(new Date());
		transferBank.setRecordType(TransferBank.TRANSFER_TYPE_TEMPLATE);
		transferBank.setOperation(Constants.OPERATION_UPDATE);
		// transferBank.setAuthStatus();

		if (transferBank.getFromAccount().equals(transferBank.getToAccount())&&transferBank.getFromCurrency().equals(transferBank.getToCurrency())) {
			throw new NTBException("err.txn.TransferAccError");
		}
		Map resultData = new HashMap();
		this.setResultData(resultData);
		this.convertPojo2Map(transferBank, resultData);

		this.setUsrSessionDataValue("transferBank", transferBank);
	}

	public void editTemplateConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");

		TransferBank transferBank = (TransferBank) this
				.getUsrSessionDataValue("transferBank");
		// commit
		transferTemplateService.editTemplateBANK(transferBank);
		Map resultData = new HashMap();
		setResultData(resultData);
		this.convertPojo2Map(transferBank, resultData);
	}

    public void editCancel() throws NTBException {
    	this.addCancel();
    }
		
	public void deleteTemplate() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");

		setResultData(new HashMap(1));
		String transID = getParameter("transId");

		TransferBank transferBank = transferTemplateService.viewTemplate(transID);

		Map resultData = new HashMap();
		
		// added by Jet for display
		if ((Constants.INPUT_AMOUNT_FLAG_FROM).equals(transferBank
				.getInputAmountFlag())) {
			transferBank.setTransferAmount(null);
		} else if ((Constants.INPUT_AMOUNT_FLAG_TO).equals(transferBank
				.getInputAmountFlag())) {
			transferBank.setDebitAmount(null);
		}
		
		this.convertPojo2Map(transferBank, resultData);
		
		/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		/*if("N".equals(transferBank.getOwnerAccFlag())){
			resultData.put("toAccount", "0");
		}
		
		String toAccount2 = null;
		toAccount2 = transferBank.getToAccount();
    	resultData.put("toAccount2", toAccount2);*/
    	/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
    	
		setResultData(resultData);
		this.setUsrSessionDataValue("transferBank", transferBank);
	}

	public void deleteTemplateConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");

		TransferBank transferBank = (TransferBank) this
				.getUsrSessionDataValue("transferBank");
		transferBank.setOperation(Constants.OPERATION_REMOVE);
		transferTemplateService.deleteTemplateBANK(transferBank.getTransId());
		Map resultData = new HashMap();
		setResultData(resultData);
		this.convertPojo2Map(transferBank, resultData);
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

			if (null == buyRate){
				buyRate = new BigDecimal("1");
			}
			if (null == sellRate){
				sellRate = new BigDecimal("1");
			}
			
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
			
			if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
				exchangeRate = new BigDecimal("0");
			} else {
				exchangeRate = buyRate.divide(sellRate, 8,
						BigDecimal.ROUND_DOWN);
			}
			
			transferBank.setExchangeRate(new Double(exchangeRate.doubleValue()));*/

			String inputAmountFlag = transferBank.getInputAmountFlag();
			String currency = null;
			Double transferAmount = new Double(0);
			if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferBank.getFromCurrency();
				transferAmount = transferBank.getDebitAmount();
				
				/*BigDecimal new_To_Amount_temp = exRatesService.getEquivalentFromHostRate(
						corpUser.getCorpId(), currency, transferBank
								.getToCurrency(), new BigDecimal(transferAmount
								.toString()), null, 2,exchangeMap);
				transferBank.setTransferAmount(new Double(new_To_Amount_temp.toString()));*/

			} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferBank.getToCurrency();
				transferAmount = transferBank.getTransferAmount();
				
				/*BigDecimal new_From_Amount_temp = exRatesService.getEquivalentFromHostRate(
						corpUser.getCorpId(), transferBank.getFromCurrency(),
						currency, null, new BigDecimal(transferAmount
								.toString()), 2,exchangeMap);
				transferBank.setDebitAmount(new Double(new_From_Amount_temp.toString()));*/

			}
			//add by lzg 20190619
			checkCutoffTimeAndSetMsg(transferBank,corpUser.getLangCode());
			//add by lzg end
			
			//add by lzg 20190902
			Map Host = transferService.toHostInBankTrial(transferBank,corpUser);
			transferBank.setFromAccount(((String)Host.get("DR_AC")).trim());
			transferBank.setToAccount(((String)Host.get("CR_AC")).trim());
			transferBank.setToName(((String)Host.get("CR_NM")).trim());
			transferBank.setDebitAmount(((BigDecimal)Host.get("TX_DR_AMT")).doubleValue());
			transferBank.setTransferAmount(((BigDecimal)Host.get("TX_CR_AMT")).doubleValue());
			transferBank.setExchangeRate(((BigDecimal)Host.get("EX_RAT")).doubleValue());
			//transferBank.setShowExchangeRate(((BigDecimal)Host.get("EX_RAT")).doubleValue());
			//add by lzg end
			
			BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
					.getCorpId(), currency, "MOP",
					new BigDecimal(transferAmount.toString()),
					null, 2);
			String txnTypeBank = null;
			if(transferBank.getOwnerAccFlag().equals("Y")) {
				txnTypeBank = Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT;
			} else if(transferBank.getOwnerAccFlag().equals("N")) {
				txnTypeBank = Constants.TXN_TYPE_TRANSFER_BANK;
			}
			
			//add by lzg 20190604
			//transferBank.setExchangeRate(new Double(showExchangeRate.doubleValue()));
			//add by lzg end
			
			if (!transferLimitService.checkLimitQuota(transferBank
					.getFromAccount(), corpUser.getCorpId(), txnType,
					transferBank.getDebitAmount().doubleValue(),
					equivalentMOP.doubleValue())) {
				 //write limit report
	        	Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("USER_ID", corpUser.getUserId());
				reportMap.put("CORP_ID", corpUser.getCorpId());
				reportMap.put("TRANS_TYPE", txnTypeBank);
				reportMap.put("LOCAL_TRANS_CODE", transferBank.getTransId());
				reportMap.put("FROM_CURRENCY", transferBank.getFromCurrency());
				reportMap.put("TO_CURRENCY", transferBank.getToCurrency());
				reportMap.put("FROM_AMOUNT", transferBank.getDebitAmount());
				reportMap.put("FROM_ACCOUNT", transferBank.getFromAccount());
				reportMap.put("TO_AMOUNT", transferBank.getTransferAmount());
				reportMap.put("EXCEEDING_TYPE", "1");
				reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
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
			//add by lzg 20190531
			Long JRNNO = (Long)fromHost.get("JRNNO");
			String serialNumber = null;
			if(JRNNO != null){
				serialNumber = JRNNO.toString();
			}
			if(serialNumber != null){
				serialNumber = Utils.prefixZero(serialNumber, 9);
			}
			transferBank.setSerialNumber(serialNumber);
			transferService.updateBANK(transferBank);
			Map resultData = bean.getResultData();
			this.convertPojo2Map(transferBank, resultData);
			setResultData(resultData);
			//add by lzg end
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
		resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);

		//add by lzg 20190604
		/*if(transferBank.getExchangeRate() < 1){
			transferBank.setExchangeRate(transferBank.getShowExchangeRate());
		}*/
		//add by lzg 20190612
		convertPojo2Map(transferBank, resultData);
		//add by lzg end
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
	
	public boolean cancel(String txnType, String id, CibAction bean)
			throws NTBException {
		return reject(txnType, id, bean);
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	private void checkCutoffTimeAndSetMsg(TransferBank transferBank,String language) throws NTBException {
		//modified by lzg 20190619
		/*ApplicationContext appContext = Config.getAppContext();
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext.getBean("CutOffTimeService");
		String fromAcctypeCode = null;
		String toAcctypeCode = null;
		if (CorpAccount.ACCOUNT_TYPE_CURRENT.equals(transferBank.getFromAcctype()) || CorpAccount.ACCOUNT_TYPE_OVER_DRAFT.equals(transferBank.getFromAcctype()))
		{
			 fromAcctypeCode = "C";
		} else if (CorpAccount.ACCOUNT_TYPE_SAVING.equals(transferBank.getFromAcctype())) {
			 fromAcctypeCode = "S";
		}
		if (CorpAccount.ACCOUNT_TYPE_CURRENT.equals(transferBank.getToAcctype()) || CorpAccount.ACCOUNT_TYPE_OVER_DRAFT.equals(transferBank.getToAcctype()))
		{
			 toAcctypeCode = "C";
		} else if (CorpAccount.ACCOUNT_TYPE_SAVING.equals(transferBank.getToAcctype())) {
			 toAcctypeCode = "S";
		}
		String txnCode = "51" + fromAcctypeCode + toAcctypeCode;
		setMessage(cutOffTimeService.check(txnCode,transferBank.getFromCurrency(),transferBank.getToCurrency()));*/
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
			clientReference=(String)map.get("REMARK");
			
			String clientRef = Utils.null2EmptyWithTrim(transferBank.getRemark());
			if(!"".equals(clientRef)){
				if(transferBank.getFromAccount().equals(fromAccount)&&
						transferBank.getToAccount().equals(toAccount)&&
						transferBank.getTransferAmount().doubleValue()==amount.doubleValue()&&
						transferBank.getRemark().equals(clientReference)){
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
