package app.cib.action.txn;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
import app.cib.bo.txn.TransferMacau;
import app.cib.bo.txn.TransferOversea;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibTransClient;
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

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Encryption;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

import app.cib.core.CibIdGenerator;
import app.cib.dao.enq.ExchangeRatesDao;
import app.cib.dao.srv.TransferPromptDao;

import com.neturbo.set.exception.NTBWarningException;

public class TransferTemplateInOverseaAction extends CibAction implements Approvable{        	       
	private TransferOversea transferOversea;
    private String defalutPattern = Config.getProperty("DefaultDatePattern");

	public void listTemplate() throws NTBException {

		setResultData(new HashMap(1));

		String corpID = null;
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");

		CorpUser corpUser = (CorpUser) getUser();
		corpID = corpUser.getCorpId();
		List templateList = transferTemplateService.listTemplateOversea(corpID);
		String recordFlag = null;
		if ((templateList.size()== 0) || (templateList == null)){
			recordFlag = "Y";
		} else {
			recordFlag = "N";
		}
		for (int i = 0; i < templateList.size(); i++) {
			transferOversea = (TransferOversea) templateList.get(i);

		}
		templateList = this.convertPojoList2MapList(templateList);

		Map resultData = new HashMap();

		resultData.put("templateList", templateList);
		resultData.put("recordFlag", recordFlag);
		setResultData(resultData);
	}

	public void addTemplateLoad() throws NTBException {
		// 闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柣顓у亞閳规牜鍠婃径瀣伓 ResultData 闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柡鍌樺�栫�氬綊鏌ㄩ悢铏瑰摵闁规儳鎳忕�氬綊鏌ㄩ悢鍛婄伄闁瑰嚖鎷�
		HashMap resultData = new HashMap(1);

		// get country list
		ApplicationContext appContext = Config.getAppContext();
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//linrui 20190729 mul-language
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		resultData.put("country", transferService.getCountryList(lang));
		//resultData.put("country", transferService.loadOversea());
		CorpUser corpUser = (CorpUser) this.getUser();
		//modified by lzg 20190708
		//resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("transferuser", transferService.loadAccountForRemittance(corpUser.getCorpId()));
		//modified by lzg end
		//add by linrui 20180313
		//setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg",Constants.US).getString("warnning.transfer.DifferenceCcy"));
		
		//add by lzg 20191022
				TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
				TxnPrompt txnPrompt = new TxnPrompt();
				try{
					txnPrompt = transferPromptService.loadByTxnType("4",TransferPromptDao.STATUS_NORMAL);
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
		
		this.setResultData(resultData);
	}

	public void addTemplate() throws NTBException {
		String accType = this.getParameter("accType");
		String toAccount = this.getParameter(accType);
		this.getParameters().put("showToAccount", toAccount);

		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		TransAmountService transAmountService = (TransAmountService) appContext
				.getBean("TransAmountService");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107

		CorpUser corpUser = (CorpUser) this.getUser();
		corpUser.setLanguage(lang);//end
//		RcCurrency rcCurrency = new RcCurrency();
//		String Iban = null;
//		String SELECT_CGDOVERSEA = "select CGD_FLAG,BANK_NAME from HS_OVERSEAS_BANK where BANK_CODE=?";
//		String SELECT_IBAN = "Select IBAN_LENGTH,IBAN_PREFIX from HS_COUNTRY_SPECIFIC_ACCOUNT_LABEL where COUNTRY_CODE=?";

//		Map fromHost = new HashMap();
//		Map cdtMap = new HashMap();
//		String CGD = "N";

		//----------Modified by xyf 20090721 for CR107 begin------------
		//check Beneficiary Address to find optional or mandatory
		String countryCode = Utils.null2Empty(this.getParameter("beneficiaryCountry"));
		String beneficiaryName3 = Utils.null2Empty(this.getParameter("beneficiaryName3"));
		String beneficiaryName4 = Utils.null2Empty(this.getParameter("beneficiaryName4"));
		
		if (("".equals(beneficiaryName3))&&("".equals(beneficiaryName4))) {
			String checkFlag = transferService.getAddrMandatoryFlagOversea(countryCode, "E");
			if("Y".equalsIgnoreCase(checkFlag)){
				throw new NTBException("err.txn.beneficiaryAddrMustInput");
			}
		}
		//--------------------------end of xyf---------------------------
		
		TransferOversea transferOversea = new TransferOversea(CibIdGenerator.getRefNoForTransaction());
		this.convertMap2Pojo(this.getParameters(), transferOversea);
		
		
		//add by lzg 20190531
		boolean flag = checkChargeAccount(transferOversea);
		if(!flag){
			throw new NTBWarningException("err.txn.chargeAccountError");
		}
		//add by lzg end
		
		
		//add by linrui for mul-language 
        Double transAmt  = TransAmountFormate.formateAmount(this.getParameter("transferAmount"),lang);
        Double debitAmt = TransAmountFormate.formateAmount(this.getParameter("debitAmount"), lang);
        transferOversea.setTransferAmount(transAmt);
        transferOversea.setDebitAmount(debitAmt);
        //end
//		String requestType = this.getParameter("requestType");
//		String transferDateString = this.getParameter("transferDateString");

//		if (requestType.equals("1")) {
//			getParameters().put("transferDate",
//					DateTime.getDateFromStr(DateTime.getCurrentDate()));
//		} else if (requestType.equals("2")) {
//			Date transferDate = DateTime.getDateFromStr(transferDateString,
//					false);
//			Date today = new Date();
//			if (!transferDate.after(today)) {
//				throw new NTBException("err.txn.transferDateMustLater");
//			}
//			int daynum = DateTime.getDaysTween(transferDate, today);
//			if (daynum >= 14) {
//				throw new NTBException("err.txn.transferLaterDateError");
//			}
//			getParameters().put("transferDate",
//					DateTime.getDateFromStr(transferDateString));
//		}
		
		transferOversea.setToAccount(toAccount);
		
		// other bank
		String[] name = getParameter("beneficiaryBank1").split(",");
		String beneficaiaryBank = name[0];
		transferOversea.setBeneficiaryBank1(beneficaiaryBank);
		if (beneficaiaryBank.equals("other")) {
			transferOversea.setBankFlag("Y");
			transferOversea.setBeneficiaryBank1(this.getParameter("otherBank"));
		} else {
			transferOversea.setBankFlag("N");
		}
		// other city
		transferOversea.setBeneficiaryCity(this.getParameter("beneficiaryCity"));
		transferOversea.setCityFlag("N");
		/*String beneficaiaryCity = this.getParameter("beneficiaryCity");
		if (beneficaiaryCity.equals("other")) {
			transferOversea.setCityFlag("Y");
			transferOversea.setBeneficiaryCity(this.getParameter("otherCity"));
		} else {
			transferOversea.setCityFlag("N");
		}*/
		
		String beneficiaryCountry = this.getParameter("beneficiaryCountry");
		if("other".equals(beneficiaryCountry)){
			transferOversea.setBeneficiaryCountry(this.getParameter("otherCountry"));
		}
		
		if ("".equals(transferOversea.getToAccount().trim())) {
			throw new NTBException("err.txn.AccountNumberIsNotNull");
		}

		GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
		if ("IBAN".equals(Utils.null2EmptyWithTrim(transferOversea.getAccType()))) {
			checkIBANAccount(transferOversea);
//			String length = null;
//			String prefix = null;
//
//			try {
//				List idList1 = null;
//				idList1 = dao.query(SELECT_IBAN, new Object[] { transferOversea
//						.getBeneficiaryCountry() });
//				if (null != idList1 && idList1.size() > 0) {
//					Map cdtMap1 = (Map) idList1.get(0);
//					length = (String) cdtMap1.get("IBAN_LENGTH");
//					prefix = (String) cdtMap1.get("IBAN_PREFIX");
//				}
//
//			} catch (Exception e) {
//				throw new NTBException("err.txn.GetIBANException");
//			}
//
//			// if the length is 00, no need to check the length.
//			if (!length.equals("00")) {
//				String fromLength = (new Integer(transferOversea.getToAccount()
//						.length())).toString();
//				if (!fromLength.equals(length)) {
//					throw new NTBException("err.txn.IbanAccountLengthError");
//				}
//			}
//			
//			// if the prefix is all space, no need to check the prefix input.
//			if (!"".equals(prefix.trim())) {
//				String toAccount1 = transferOversea.getToAccount().substring(0,
//						2).toUpperCase()
//						+ transferOversea.getToAccount().substring(2);
//				transferOversea.setToAccount(toAccount1);
//				if (!(prefix.compareTo(toAccount1.substring(0, 4)) == 0)) {
//					throw new NTBException("err.txn.IbanAccountPrefixError");
//				}
//			}
		}

		transferOversea.setUserId(corpUser.getUserId());
		transferOversea.setCorpId(corpUser.getCorpId());
//		transferOversea.setRequestTime(new Date());
//		transferOversea.setExecuteTime(new Date());
		transferOversea.setRecordType(TransferOversea.TRANSFER_TYPE_TEMPLATE);
		transferOversea.setOperation(Constants.OPERATION_NEW);

		// add mxl 0814
		/* get currency according to account */
//		CorpAccount corpAccount = corpAccountService
//				.viewCorpAccount(transferOversea.getFromAccount());
//		transferOversea.setFromCurrency(corpAccount.getCurrency());

		/* add by mxl 0827 get the currency number according the currency code */
//		CorpAccount corpChargeAccount = corpAccountService
//				.viewCorpAccount(transferOversea.getChargeAccount());
//		String chargeCurrency = corpChargeAccount.getCurrency();
//		transferOversea.setChargeCurrency(chargeCurrency);
//		String fromCurrencyCode = rcCurrency.getCbsNumberByCcy(transferOversea
//				.getFromCurrency());
//		String toCurrencyCode = rcCurrency.getCbsNumberByCcy(transferOversea
//				.getToCurrency());
//		String chargeCurrencyCode = rcCurrency
//				.getCbsNumberByCcy(chargeCurrency);


		if (transferOversea.getTransferAmount().doubleValue() == 0) {
			transferOversea
					.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_FROM);
			BigDecimal transferAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferOversea.getFromCurrency(),
					transferOversea.getToCurrency(), new BigDecimal(
							transferOversea.getDebitAmount().toString()), null,
					2);

			transferOversea.setTransferAmount(new Double(transferAmount
					.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinAmtOtherBanks(transferOversea.getFromCurrency(), transferOversea.getDebitAmount().doubleValue());
			
			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferOversea.getFromCurrency(),
					transferOversea.getToCurrency(), 8);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

			if (null == buyRate) {
				buyRate = new BigDecimal("1");
			}
			if (null == sellRate) {
				sellRate = new BigDecimal("1");
			}
			if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
				exchangeRate = new BigDecimal("0");
			} else {
				exchangeRate = buyRate.divide(sellRate, 8,
						BigDecimal.ROUND_DOWN);
			}
			transferOversea.setExchangeRate(new Double(exchangeRate
					.doubleValue()));
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferOversea.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferOversea.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
		} else if (transferOversea.getDebitAmount().doubleValue() == 0) {
			transferOversea.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_TO);
			BigDecimal toAmount = new BigDecimal(transferOversea
					.getTransferAmount().toString());
			BigDecimal fromAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferOversea.getFromCurrency(),
					transferOversea.getToCurrency(), null, toAmount, 2);

			transferOversea.setDebitAmount(new Double(fromAmount.toString()));
			
			transAmountService.checkMinAmtOtherBanks(transferOversea.getToCurrency(), transferOversea.getTransferAmount().doubleValue());
			
			
			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferOversea.getFromCurrency(),
					transferOversea.getToCurrency(), 8);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

			if (null == buyRate) {
				buyRate = new BigDecimal("1");
			}
			if (null == sellRate) {
				sellRate = new BigDecimal("1");
			}
			if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
				exchangeRate = new BigDecimal("0");
			} else {
				exchangeRate = buyRate.divide(sellRate, 8,
						BigDecimal.ROUND_DOWN);
			}
			transferOversea.setExchangeRate(new Double(exchangeRate
					.doubleValue()));
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferOversea.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferOversea.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
		}
		
		//add by lzg 20190902
		Map Host = transferService.toHostOverseasTrial(transferOversea, corpUser);
		if(!transferOversea.getFromCurrency().equals(transferOversea.getToCurrency())){
			transferOversea.setDebitAmount(((BigDecimal)Host.get("FEE_AMT")).doubleValue());
			transferOversea.setTransferAmount(((BigDecimal)Host.get("CHANGE_AMT")).doubleValue());
			transferOversea.setHandlingAmount(((BigDecimal)Host.get("FEE_AMT")).doubleValue());
		}
		transferOversea.setChargeAmount(((BigDecimal)Host.get("CHG_AMT")).doubleValue());
		transferOversea.setExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		transferOversea.setShowExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		//add by lzg end
		
		
		
		transferOversea.setSpbankLabel("#");
//		if (transferOversea.getBankFlag().equals("N")) {
//			try {
//				List idList = null;
//				idList = dao.query(SELECT_CGDOVERSEA,
//						new Object[] { transferOversea.getBeneficiaryBank1() });
//				cdtMap = (Map) idList.get(0);
//				CGD = (String) cdtMap.get("CGD_FLAG");
//
//			} catch (Exception e) {
//				throw new NTBException("err.txn.GetCGDException");
//			}
//		}
		
		transferOversea.setSpbankLabel(transferService.loadSpbankLabel(
				transferOversea.getBeneficiaryCountry()).trim());


//		fromHost = transferService.toHostChargeEnquiryNew(transferOversea
//				.getTransId(), corpUser, new BigDecimal(transferOversea
//				.getTransferAmount().toString()), transferOversea
//				.getBeneficiaryCountry(), CGD, fromCurrencyCode,
//				toCurrencyCode, transferOversea.getChareBy(),
//				chargeCurrencyCode, Iban);
//		transferService.uploadEnquiryOversea(transferOversea, fromHost);
//
//		double chargeAmount = (new Double(fromHost.get("HANDLING_CHRG_AMT")
//				.toString())).doubleValue()
//				+ (new Double(fromHost.get("COMM_AMT").toString()))
//						.doubleValue()
//				+ (new Double(fromHost.get("TAX_AMOUNT").toString()))
//						.doubleValue()
//				+ (new Double(fromHost.get("SWIFT_CHRG_AMT").toString()))
//						.doubleValue()
//				+ (new Double(fromHost.get("OUR_CHG_AMT").toString()))
//						.doubleValue();
//		// add by mxl 0913
//		transferOversea.setHandlingAmount(new Double(fromHost.get(
//				"HANDLING_CHRG_AMT").toString()));
//		transferOversea.setCommissionAmount(new Double(fromHost.get("COMM_AMT")
//				.toString()));
//		transferOversea.setTaxAmount(new Double(fromHost.get("TAX_AMOUNT")
//				.toString()));
//		transferOversea.setSwiftAmount(new Double(fromHost
//				.get("SWIFT_CHRG_AMT").toString()));
//		transferOversea.setChargeOur(new Double(fromHost.get("OUR_CHG_AMT")
//				.toString()));
//
//		transferOversea.setChargeAmount(new Double(chargeAmount));
		

		Map resultData = new HashMap();
		resultData.put("otherCityFlag", transferOversea.getCityFlag());
		resultData.put("otherBankFlag", transferOversea.getBankFlag());
		this.setResultData(resultData);
		this.convertPojo2Map(transferOversea, resultData);
		

		this.setUsrSessionDataValue("transferOversea", transferOversea);
	}
	
	//add by lzg 20190530 Determine whether the chargeAccount has the fromCurrency
	private boolean checkChargeAccount(TransferOversea transferOversea) throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		CibTransClient testClient = new CibTransClient("CIB", "0195");
		String chargeAccount = transferOversea.getChargeAccount();
		toHost.put("ACCOUNT_NO", chargeAccount);
		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		ArrayList ccyList = new ArrayList();
		boolean flag = false;
		ArrayList ccyBalList = (ArrayList)fromHost.get("CCY_BAL_LIST");
		for(int i = 0;i < ccyBalList.size();i++){
			String ccy = (String)((HashMap)ccyBalList.get(i)).get("CCY_CODE_OF_AC");
			if(ccy != null){
				if(!"".equals(ccy.trim())){
					if(ccy.equals(transferOversea.getFromCurrency())){
						flag = true;
					}
					
				}
			}
		}
		return flag;
	}

	public void addTemplateConfirm() throws NTBException {

		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");

		TransferOversea transferOversea = (TransferOversea) this
				.getUsrSessionDataValue("transferOversea");

		transferOversea.setRequestTime(new Date());
		transferOversea.setExecuteTime(new Date());

		transferTemplateService.addTemplateOversea(transferOversea);

		// for display
		Map resultData = new HashMap();
		resultData.put("otherCityFlag", transferOversea.getCityFlag());
		resultData.put("otherBankFlag", transferOversea.getBankFlag());
		setResultData(resultData);
		this.convertPojo2Map(transferOversea, resultData);
	}

    public void addCancel() throws NTBException {
		TransferOversea transferOversea = (TransferOversea) this.getUsrSessionDataValue("transferOversea");

		// initial service
		ApplicationContext appContext = Config.getAppContext();
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//linrui 20190729 mul-language

		// 闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柣顓у亞閳规牜鍠婃径瀣伓 ResultData 闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柡鍌樺�栫�氬綊鏌ㄩ悢铏瑰摵闁规儳鎳忕�氬綊鏌ㄩ悢鍛婄伄闁瑰嚖鎷�
		setResultData(new HashMap(1));

		// added by Jet for display
		if ((Constants.INPUT_AMOUNT_FLAG_FROM).equals(transferOversea
				.getInputAmountFlag())) {
			transferOversea.setTransferAmount(null);
		} else if ((Constants.INPUT_AMOUNT_FLAG_TO).equals(transferOversea
				.getInputAmountFlag())) {
			transferOversea.setDebitAmount(null);
		}
		
		Map resultData = new HashMap();
		this.convertPojo2Map(transferOversea, resultData);

		// get country list
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		resultData.put("country", transferService.getCountryList(lang));
		//resultData.put("country", transferService.loadOversea());
		resultData.put("otherCityFlag", transferOversea.getCityFlag());
		resultData.put("otherBankFlag", transferOversea.getBankFlag());

		if ("Y".equals(transferOversea.getBankFlag())) {
			resultData.put("beneficiaryBank1", "other");
			resultData.put("otherBank", transferOversea.getBeneficiaryBank1());
		} else {
			resultData.put("beneficiaryBank1", transferOversea.getBeneficiaryBank1()
					+ "," + transferOversea.getSwiftAddress());
		}
		
		if("Y".equals(transferOversea.getCityFlag())){
			resultData.put("beneficiaryCity","other");
			resultData.put("otherCity", transferOversea.getBeneficiaryCity());
		}
		
		resultData.put("toAccount", transferOversea.getToAccount());
		resultData.put("showToAccount", transferOversea.getToAccount());
		CorpUser corpUser = (CorpUser) this.getUser();
		//modified by lzg 20190708
		//resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("transferuser", transferService.loadAccountForRemittance(corpUser.getCorpId()));
		//modified by lzg end
		resultData.put("selectFromAcct", transferOversea.getFromAccount() + " - "  + transferOversea.getFromCurrency());//add by linrui for mul-ccy
		resultData.put("selectChargeAcct", transferOversea.getChargeAccount() + " - "  + transferOversea.getChargeCurrency());//add by linrui for mul-ccy
		setResultData(resultData);
		
		// 闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柣顓у亗缁鳖噣骞忛悜鑺ユ櫢闁哄倶鍊栫�氬綊鏌ㄩ悢宄板缓閺夊牏鍋撶�氬綊鏌ㄩ悢鐑樼埍ession闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柣銊ユ閸炲骞忕粚濯渘firm闂佽法鍠愰弸濠氬箯瀹勬澘鏅搁梺璺ㄥ枑閺嬪骞忛悜鑺ユ櫢闁哄倶鍊栫�氬綊鎳ｅ锟芥櫢闁跨噦鎷�

		this.setUsrSessionDataValue("transferOversea", transferOversea);
    }
		
	public void editTemplateLoad() throws NTBException {
		// initial service
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//linrui 20190729 mul-language

		// 闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柣顓у亞閳规牜鍠婃径瀣伓 ResultData 闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柡鍌樺�栫�氬綊鏌ㄩ悢铏瑰摵闁规儳鎳忕�氬綊鏌ㄩ悢鍛婄伄闁瑰嚖鎷�
		setResultData(new HashMap(1));
		String transID = getParameter("transId");

		TransferOversea transferOversea = transferTemplateService
				.viewTemplateOversea(transID);

		// added by Jet for display
		if ((Constants.INPUT_AMOUNT_FLAG_FROM).equals(transferOversea
				.getInputAmountFlag())) {
			transferOversea.setTransferAmount(null);
		} else if ((Constants.INPUT_AMOUNT_FLAG_TO).equals(transferOversea
				.getInputAmountFlag())) {
			transferOversea.setDebitAmount(null);
		}
		
		Map resultData = new HashMap();
		this.convertPojo2Map(transferOversea, resultData);

		// get country list
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		resultData.put("country", transferService.getCountryList(lang));
		//resultData.put("country", transferService.loadOversea());
		resultData.put("otherCityFlag", transferOversea.getCityFlag());
		resultData.put("otherBankFlag", transferOversea.getBankFlag());

		if ("Y".equals(transferOversea.getBankFlag())) {
			resultData.put("beneficiaryBank1", "other");
			resultData.put("otherBank", transferOversea.getBeneficiaryBank1());
		} else {
			resultData.put("beneficiaryBank1", transferOversea.getBeneficiaryBank1()
					+ "," + transferOversea.getSwiftAddress());
		}
		
		if("Y".equals(transferOversea.getCityFlag())){
			resultData.put("beneficiaryCity","other");
			resultData.put("otherCity", transferOversea.getBeneficiaryCity());
		}
		
		resultData.put("toAccount", transferOversea.getToAccount());
		resultData.put("showToAccount", transferOversea.getToAccount());
		CorpUser corpUser = (CorpUser) this.getUser();
		//modified by lzg 20190708
		//resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("transferuser", transferService.loadAccountForRemittance(corpUser.getCorpId()));
		//modified by lzg end
		resultData.put("selectFromAcct", transferOversea.getFromAccount() + " - "  + transferOversea.getFromCurrency());//add by linrui for mul-ccy
		resultData.put("selectChargeAcct", transferOversea.getChargeAccount() + " - "  + transferOversea.getChargeCurrency());//add by linrui for mul-ccy
		
		//add by lzg 20191022
				TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
				TxnPrompt txnPrompt = new TxnPrompt();
				try{
					txnPrompt = transferPromptService.loadByTxnType("4",TransferPromptDao.STATUS_NORMAL);
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
		
		// 闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柣顓у亗缁鳖噣骞忛悜鑺ユ櫢闁哄倶鍊栫�氬綊鏌ㄩ悢宄板缓閺夊牏鍋撶�氬綊鏌ㄩ悢鐑樼埍ession闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柣銊ユ閸炲骞忕粚濯渘firm闂佽法鍠愰弸濠氬箯瀹勬澘鏅搁梺璺ㄥ枑閺嬪骞忛悜鑺ユ櫢闁哄倶鍊栫�氬綊鎳ｅ锟芥櫢闁跨噦鎷�
		//add by linrui 20180313
//		setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg",Constants.US).getString("warnning.transfer.DifferenceCcy"));
		this.setUsrSessionDataValue("transferOversea", transferOversea);
	}

	public void editTemplate() throws NTBException {
		String accType = this.getParameter("accType");
		String toAccount = this.getParameter(accType);
		this.getParameters().put("showToAccount", toAccount);
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		TransferOversea transferOversea = (TransferOversea) this.getUsrSessionDataValue("transferOversea");
		this.convertMap2Pojo(this.getParameters(), transferOversea);
		
		//add by lzg 20190531
		boolean flag = checkChargeAccount(transferOversea);
		if(!flag){
			throw new NTBWarningException("err.txn.chargeAccountError");
		}
		//add by lzg end
		
		
		//add by linrui for mul-language 
        Double transAmt  = TransAmountFormate.formateAmount(this.getParameter("transferAmount"),lang);
        Double debitAmt = TransAmountFormate.formateAmount(this.getParameter("debitAmount"), lang);
        transferOversea.setTransferAmount(transAmt);
        transferOversea.setDebitAmount(debitAmt);
        //end
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		TransAmountService transAmountService = (TransAmountService) appContext
				.getBean("TransAmountService");

		
		CorpUser corpUser = (CorpUser) this.getUser();
		corpUser.setLanguage(lang);//add by linrui for mul-language
//		RcCurrency rcCurrency = new RcCurrency();
//		String Iban = null;
//		String SELECT_CGDOVERSEA = "select CGD_FLAG,BANK_NAME from HS_OVERSEAS_BANK where BANK_CODE=?";
//		String SELECT_IBAN = "Select IBAN_LENGTH,IBAN_PREFIX from HS_COUNTRY_SPECIFIC_ACCOUNT_LABEL where COUNTRY_CODE=?";

//		Map fromHost = new HashMap();
//		Map cdtMap = new HashMap();
//		String CGD = "N";

//		String requestType = this.getParameter("requestType");
//		String transferDateString = this.getParameter("transferDateString");
		
//		if (requestType.equals("1")) {
//			getParameters().put("transferDate",
//					DateTime.getDateFromStr(DateTime.getCurrentDate()));
//		} else if (requestType.equals("2")) {
//			Date transferDate = DateTime.getDateFromStr(transferDateString,
//					false);
//			Date today = new Date();
//			if (!transferDate.after(today)) {
//				throw new NTBException("err.txn.transferDateMustLater");
//			}
//			int daynum = DateTime.getDaysTween(transferDate, today);
//			if (daynum >= 14) {
//				throw new NTBException("err.txn.transferLaterDateError");
//			}
//			getParameters().put("transferDate",
//					DateTime.getDateFromStr(transferDateString));
//		}

		transferOversea.setToAccount(toAccount);
		
		//----------Modified by xyf 20090721 for CR107 begin------------
		//check Beneficiary Address to find optional or mandatory
		String countryCode = Utils.null2Empty(this.getParameter("beneficiaryCountry"));
		String beneficiaryName3 = Utils.null2Empty(this.getParameter("beneficiaryName3"));
		String beneficiaryName4 = Utils.null2Empty(this.getParameter("beneficiaryName4"));
		
		if (("".equals(beneficiaryName3))&&("".equals(beneficiaryName4))) {
			String checkFlag = transferService.getAddrMandatoryFlagOversea(countryCode, "E");
			if("Y".equalsIgnoreCase(checkFlag)){
				throw new NTBException("err.txn.beneficiaryAddrMustInput");
			}
		}
		//--------------------------end of xyf---------------------------
		
		// other bank
		String[] name = getParameter("beneficiaryBank1").split(",");
		String beneficaiaryBank = name[0];
		transferOversea.setBeneficiaryBank1(beneficaiaryBank);
		if (beneficaiaryBank.equals("other")) {
			transferOversea.setBankFlag("Y");
			transferOversea.setBeneficiaryBank1(this.getParameter("otherBank"));
		} else {
			transferOversea.setBankFlag("N");
		}
		// other city
		transferOversea.setBeneficiaryCity(this.getParameter("beneficiaryCity"));
		transferOversea.setCityFlag("N");
		/*String beneficaiaryCity = this.getParameter("beneficiaryCity");
		if (beneficaiaryCity.equals("other")) {
			transferOversea.setCityFlag("Y");
			transferOversea.setBeneficiaryCity(this.getParameter("otherCity"));
		} else {
			transferOversea.setCityFlag("N");
		}*/
		
		String beneficiaryCountry = this.getParameter("beneficiaryCountry");
		if("other".equals(beneficiaryCountry)){
			transferOversea.setBeneficiaryCountry(this.getParameter("otherCountry"));
		}
		
		if ("".equals(transferOversea.getToAccount().trim())) {
			throw new NTBException("err.txn.AccountNumberIsNotNull");
		}

		GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
		if ("IBAN".equals(Utils.null2EmptyWithTrim(transferOversea.getAccType()))) {
			checkIBANAccount(transferOversea);

//			String length = null;
//			String prefix = null;
//
//			try {
//				List idList1 = null;
//				idList1 = dao.query(SELECT_IBAN, new Object[] { transferOversea
//						.getBeneficiaryCountry() });
//				if (null != idList1 && idList1.size() > 0) {
//					Map cdtMap1 = (Map) idList1.get(0);
//					length = (String) cdtMap1.get("IBAN_LENGTH");
//					prefix = (String) cdtMap1.get("IBAN_PREFIX");
//				}
//
//			} catch (Exception e) {
//				throw new NTBException("err.txn.GetIBANException");
//			}
//
//			// if the length is 00, no need to check the length.
//			if (!length.equals("00")) {
//				String fromLength = (new Integer(transferOversea.getToAccount()
//						.length())).toString();
//				if (!fromLength.equals(length)) {
//					throw new NTBException("err.txn.IbanAccountLengthError");
//				}
//			}
//			
//			// if the prefix is all space, no need to check the prefix input.
//			if (!"".equals(prefix.trim())) {
//				String toAccount1 = transferOversea.getToAccount().substring(0,
//						2).toUpperCase()
//						+ transferOversea.getToAccount().substring(2);
//				transferOversea.setToAccount(toAccount1);
//				if (!(prefix.compareTo(toAccount1.substring(0, 4)) == 0)) {
//					throw new NTBException("err.txn.IbanAccountPrefixError");
//				}
//			}
		}

		transferOversea.setUserId(corpUser.getUserId());
		transferOversea.setCorpId(corpUser.getCorpId());
		transferOversea.setRequestTime(new Date());
		transferOversea.setExecuteTime(new Date());
		transferOversea.setRecordType(TransferOversea.TRANSFER_TYPE_TEMPLATE);
		transferOversea.setOperation(Constants.OPERATION_UPDATE);
		
		// add mxl 0814
		/* get currency according to account */
//		CorpAccount corpAccount = corpAccountService
//				.viewCorpAccount(transferOversea.getFromAccount());
//		transferOversea.setFromCurrency(corpAccount.getCurrency());

		/* add by mxl 0827 get the currency number according the currency code */
//		CorpAccount corpChargeAccount = corpAccountService
//				.viewCorpAccount(transferOversea.getChargeAccount());
//		String chargeCurrency = corpChargeAccount.getCurrency();
//		transferOversea.setChargeCurrency(chargeCurrency);
//		String fromCurrencyCode = rcCurrency.getCbsNumberByCcy(transferOversea
//				.getFromCurrency());
//		String toCurrencyCode = rcCurrency.getCbsNumberByCcy(transferOversea
//				.getToCurrency());
//		String chargeCurrencyCode = rcCurrency
//				.getCbsNumberByCcy(chargeCurrency);


		if (transferOversea.getTransferAmount().doubleValue() == 0) {
			transferOversea
					.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_FROM);
			BigDecimal transferAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferOversea.getFromCurrency(),
					transferOversea.getToCurrency(), new BigDecimal(
							transferOversea.getDebitAmount().toString()), null,
					2);

			transferOversea.setTransferAmount(new Double(transferAmount
					.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinAmtOtherBanks(transferOversea.getFromCurrency(), transferOversea.getDebitAmount().doubleValue());
			
			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferOversea.getFromCurrency(),
					transferOversea.getToCurrency(), 8);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

			if (null == buyRate) {
				buyRate = new BigDecimal("1");
			}
			if (null == sellRate) {
				sellRate = new BigDecimal("1");
			}
			if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
				exchangeRate = new BigDecimal("0");
			} else {
				exchangeRate = buyRate.divide(sellRate, 8,
						BigDecimal.ROUND_DOWN);
			}
			transferOversea.setExchangeRate(new Double(exchangeRate
					.doubleValue()));
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferOversea.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferOversea.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
		} else if (transferOversea.getDebitAmount().doubleValue() == 0) {
			transferOversea.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_TO);
			BigDecimal toAmount = new BigDecimal(transferOversea
					.getTransferAmount().toString());
			BigDecimal fromAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferOversea.getFromCurrency(),
					transferOversea.getToCurrency(), null, toAmount, 2);

			transferOversea.setDebitAmount(new Double(fromAmount.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinAmtOtherBanks(transferOversea.getToCurrency(), transferOversea.getTransferAmount().doubleValue());
			
			
			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferOversea.getFromCurrency(),
					transferOversea.getToCurrency(), 8);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

			if (null == buyRate) {
				buyRate = new BigDecimal("1");
			}
			if (null == sellRate) {
				sellRate = new BigDecimal("1");
			}
			if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
				exchangeRate = new BigDecimal("0");
			} else {
				exchangeRate = buyRate.divide(sellRate, 8,
						BigDecimal.ROUND_DOWN);
			}
			transferOversea.setExchangeRate(new Double(exchangeRate
					.doubleValue()));
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferOversea.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferOversea.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
		}
		
		
		//add by lzg 20190902
		Map Host = transferService.toHostOverseasTrial(transferOversea, corpUser);
		if(!transferOversea.getFromCurrency().equals(transferOversea.getToCurrency())){
			transferOversea.setDebitAmount(((BigDecimal)Host.get("FEE_AMT")).doubleValue());
			transferOversea.setTransferAmount(((BigDecimal)Host.get("CHANGE_AMT")).doubleValue());
			transferOversea.setHandlingAmount(((BigDecimal)Host.get("FEE_AMT")).doubleValue());
		}
		transferOversea.setChargeAmount(((BigDecimal)Host.get("CHG_AMT")).doubleValue());
		transferOversea.setExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		transferOversea.setShowExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		//add by lzg end
		
		// add by mxl 0828 get the CGD from local_bank_code according to the
		transferOversea.setSpbankLabel("#");
//		if (transferOversea.getBankFlag().equals("N")) {
//			try {
//				List idList = null;
//				idList = dao.query(SELECT_CGDOVERSEA,
//						new Object[] { transferOversea.getBeneficiaryBank1() });
//				cdtMap = (Map) idList.get(0);
//				CGD = (String) cdtMap.get("CGD_FLAG");
//
//			} catch (Exception e) {
//				throw new NTBException("err.txn.GetCGDException");
//			}
//		}
		
		transferOversea.setSpbankLabel(transferService.loadSpbankLabel(
				transferOversea.getBeneficiaryCountry()).trim());


//		fromHost = transferService.toHostChargeEnquiryNew(transferOversea
//				.getTransId(), corpUser, new BigDecimal(transferOversea
//				.getTransferAmount().toString()), transferOversea
//				.getBeneficiaryCountry(), CGD, fromCurrencyCode,
//				toCurrencyCode, transferOversea.getChareBy(),
//				chargeCurrencyCode, Iban);
//		transferService.uploadEnquiryOversea(transferOversea, fromHost);

//		double chargeAmount = (new Double(fromHost.get("HANDLING_CHRG_AMT")
//				.toString())).doubleValue()
//				+ (new Double(fromHost.get("COMM_AMT").toString()))
//						.doubleValue()
//				+ (new Double(fromHost.get("TAX_AMOUNT").toString()))
//						.doubleValue()
//				+ (new Double(fromHost.get("SWIFT_CHRG_AMT").toString()))
//						.doubleValue()
//				+ (new Double(fromHost.get("OUR_CHG_AMT").toString()))
//						.doubleValue();
//		// add by mxl 0913
//		transferOversea.setHandlingAmount(new Double(fromHost.get(
//				"HANDLING_CHRG_AMT").toString()));
//		transferOversea.setCommissionAmount(new Double(fromHost.get("COMM_AMT")
//				.toString()));
//		transferOversea.setTaxAmount(new Double(fromHost.get("TAX_AMOUNT")
//				.toString()));
//		transferOversea.setSwiftAmount(new Double(fromHost
//				.get("SWIFT_CHRG_AMT").toString()));
//		transferOversea.setChargeOur(new Double(fromHost.get("OUR_CHG_AMT")
//				.toString()));
//
//		transferOversea.setChargeAmount(new Double(chargeAmount));
		

		Map resultData = new HashMap();
		resultData.put("otherCityFlag", transferOversea.getCityFlag());
		resultData.put("otherBankFlag", transferOversea.getBankFlag());
		this.setResultData(resultData);
		this.convertPojo2Map(transferOversea, resultData);
		

		this.setUsrSessionDataValue("transferOversea", transferOversea);
	}

	public void editTemplateConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");

		TransferOversea transferOversea = (TransferOversea) this
				.getUsrSessionDataValue("transferOversea");

		transferTemplateService.editTemplateOversea(transferOversea);
		
		// for display
		Map resultData = new HashMap();
		resultData.put("otherCityFlag", transferOversea.getCityFlag());
		resultData.put("otherBankFlag", transferOversea.getBankFlag());
		setResultData(resultData);
		this.convertPojo2Map(transferOversea, resultData);
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

		TransferOversea transferOversea = transferTemplateService
				.viewTemplateOversea(transID);
		transferOversea.setOperation(Constants.OPERATION_REMOVE);
		// transferOversea.setStatus(Constants.STATUS_REMOVED);
		// transferOversea.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);

		Map resultData = new HashMap();
		this.convertPojo2Map(transferOversea, resultData);
		setResultData(resultData);
		resultData.put("otherCityFlag", transferOversea.getCityFlag());
		resultData.put("otherBankFlag", transferOversea.getBankFlag());
		// 闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柣顓у亗缁鳖噣骞忛悜鑺ユ櫢闁哄倶鍊栫�氬綊鏌ㄩ悢宄板缓閺夊牏鍋撶�氬綊鏌ㄩ悢鐑樼埍ession闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柣銊ユ閸炲骞忕粚濯渘firm闂佽法鍠愰弸濠氬箯瀹勬澘鏅搁梺璺ㄥ枑閺嬪骞忛悜鑺ユ櫢闁哄倶鍊栫�氬綊鎳ｅ锟芥櫢闁跨噦鎷�

		this.setUsrSessionDataValue("transferOversea", transferOversea);

	}

	public void deleteTemplateConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");

		TransferOversea transferOversea = (TransferOversea) this
				.getUsrSessionDataValue("transferOversea");

		transferTemplateService.deleteTemplateOversea(transferOversea
				.getTransId());

		Map resultData = new HashMap();
		setResultData(resultData);
		resultData.put("otherCityFlag", transferOversea.getCityFlag());
		resultData.put("otherBankFlag", transferOversea.getBankFlag());
		this.convertPojo2Map(transferOversea, resultData);

	}

	public void transferTemplateLoad() throws NTBException {
		// initial service
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//linrui 20190729 mul-language

		// 闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柣顓у亞閳规牜鍠婃径瀣伓 ResultData 闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柡鍌樺�栫�氬綊鏌ㄩ悢铏瑰摵闁规儳鎳忕�氬綊鏌ㄩ悢鍛婄伄闁瑰嚖鎷�
		setResultData(new HashMap(1));
		String transID = getParameter("transId");

		TransferOversea transferOversea = transferTemplateService
				.viewTemplateOversea(transID);

		// added by Jet for display
		if ((Constants.INPUT_AMOUNT_FLAG_FROM).equals(transferOversea
				.getInputAmountFlag())) {
			transferOversea.setTransferAmount(null);
		} else if ((Constants.INPUT_AMOUNT_FLAG_TO).equals(transferOversea
				.getInputAmountFlag())) {
			transferOversea.setDebitAmount(null);
		}
		
		Map resultData = new HashMap();
		this.convertPojo2Map(transferOversea, resultData);

		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		resultData.put("country", transferService.getCountryList(lang));
		//resultData.put("country", transferService.loadOversea());
		resultData.put("otherCityFlag", transferOversea.getCityFlag());
		resultData.put("otherBankFlag", transferOversea.getBankFlag());

		if ("Y".equals(transferOversea.getBankFlag())) {
			resultData.put("beneficiaryBank1", "other");
			resultData.put("otherBank", transferOversea.getBeneficiaryBank1());
		} else {
			resultData.put("beneficiaryBank1", transferOversea.getBeneficiaryBank1()
					+ "," + transferOversea.getSwiftAddress());
		}
		
		if("Y".equals(transferOversea.getCityFlag())){
			resultData.put("beneficiaryCity","other");
			resultData.put("otherCity", transferOversea.getBeneficiaryCity());
		}
		
		resultData.put("toAccount", transferOversea.getToAccount());
		resultData.put("showToAccount", transferOversea.getToAccount());
		
		/* Add by heyongjiang 20100827 begin  */
		CorpUser corpUser = (CorpUser) this.getUser();
		//modified by lzg 20190602
		/*int checkPurpose = transferService.checkNeedPurpose(corpUser
				.getCorpId(), transferOversea.getFromAccount(), Utils
				.null2Empty(transferOversea.getTransferAmount()), Utils
				.null2Empty(transferOversea.getDebitAmount()), transferOversea.getFromCurrency(), transferOversea.getToCurrency(),
				transferOversea.getBeneficiaryCountry());
		if (checkPurpose == 1) {
			resultData.put("showPurpose", "true");
		} else if (checkPurpose == 2) {
			resultData.put("showPurpose", "true");
		} else if (checkPurpose == 3) {
			resultData.put("showPurpose", "true");
		} else {
			resultData.put("showPurpose", "false");
		}*/
		//modified by lzg 
		/* Add by heyongjiang end */
		//modified by lzg 20190708
		//resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("transferuser", transferService.loadAccountForRemittance(corpUser.getCorpId()));
		//modified by lzg end
		resultData.put("selectChargeAcct", transferOversea.getChargeAccount() + " - "  + transferOversea.getChargeCurrency());//add by linrui for mul-ccy
		resultData.put("selectFromAcct", transferOversea.getFromAccount() + " - "  + transferOversea.getFromCurrency());//add by linrui for mul-ccy
		//add by lzg 20191022
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		TxnPrompt txnPrompt = new TxnPrompt();
		try{
			txnPrompt = transferPromptService.loadByTxnType("4",TransferPromptDao.STATUS_NORMAL);
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

		// 闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柣顓у亗缁鳖噣骞忛悜鑺ユ櫢闁哄倶鍊栫�氬綊鏌ㄩ悢宄板缓閺夊牏鍋撶�氬綊鏌ㄩ悢鐑樼埍ession闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柣銊ユ閸炲骞忕粚濯渘firm闂佽法鍠愰弸濠氬箯瀹勬澘鏅搁梺璺ㄥ枑閺嬪骞忛悜鑺ユ櫢闁哄倶鍊栫�氬綊鎳ｅ锟芥櫢闁跨噦鎷�

		this.setUsrSessionDataValue("transferOversea", transferOversea);
	}

	public void transferTemplate() throws NTBException {
		String accType = this.getParameter("accType");
		String toAccount = this.getParameter(accType);
		this.getParameters().put("showToAccount", toAccount);

		TransferOversea transferOversea = (TransferOversea) this.getUsrSessionDataValue("transferOversea");

		//initial service
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		TransAmountService transAmountService = (TransAmountService) appContext
				.getBean("TransAmountService");

		//----------Modified by xyf 20090721 for CR107 begin------------
		//check Beneficiary Address to find optional or mandatory
		String countryCode = Utils.null2Empty(this.getParameter("beneficiaryCountry"));
		String beneficiaryName3 = Utils.null2Empty(this.getParameter("beneficiaryName3"));
		String beneficiaryName4 = Utils.null2Empty(this.getParameter("beneficiaryName4"));
		
		if (("".equals(beneficiaryName3))&&("".equals(beneficiaryName4))) {
			String checkFlag = transferService.getAddrMandatoryFlagOversea(countryCode, "E");
			if("Y".equalsIgnoreCase(checkFlag)){
				throw new NTBException("err.txn.beneficiaryAddrMustInput");
			}
		}
		//--------------------------end of xyf---------------------------
		
		CorpUser corpUser = (CorpUser) this.getUser();
		RcCurrency rcCurrency = new RcCurrency();
		String Iban = null;
		String SELECT_CGDOVERSEA = "select CGD_FLAG,BANK_NAME from HS_OVERSEAS_BANK where BANK_CODE=?";
//		String SELECT_IBAN = "Select IBAN_LENGTH,IBAN_PREFIX from HS_COUNTRY_SPECIFIC_ACCOUNT_LABEL where COUNTRY_CODE=?";

		Map fromHost = new HashMap();
		Map cdtMap = new HashMap();
		String CGD = "N";
		
		String requestType = this.getParameter("requestType");
		String transferDateString = this.getParameter("transferDateString");

		if (requestType.equals("1")) {
			getParameters().put("transferDate",
					DateTime.getDateFromStr(DateTime.getCurrentDate()));
		} else if (requestType.equals("2")) {
			Date transferDate = DateTime.getDateFromStr(transferDateString,
					false);
			Date today = new Date();
			if (!transferDate.after(today)) {
				throw new NTBException("err.txn.transferDateMustLater");
			}
			// wen_yh add 1.6
			int daynum = DateTime.getDaysTween(transferDate, today);
			if (daynum >= 14) {
				throw new NTBException("err.txn.transferLaterDateError");
			}
			getParameters().put("transferDate",
					DateTime.getDateFromStr(transferDateString));
		}
		this.convertMap2Pojo(this.getParameters(), transferOversea);
		
		transferOversea.setToAccount(toAccount);
		
		// other bank
		String[] name = getParameter("beneficiaryBank1").split(",");
		String beneficaiaryBank = name[0];
		transferOversea.setBeneficiaryBank1(beneficaiaryBank);
		if (beneficaiaryBank.equals("other")) {
			transferOversea.setBankFlag("Y");
			transferOversea.setBeneficiaryBank1(this.getParameter("otherBank"));
		} else {
			transferOversea.setBankFlag("N");
		}
		// other city
		transferOversea.setBeneficiaryCity(this.getParameter("beneficiaryCity"));
		transferOversea.setCityFlag("N");
	/*	String beneficaiaryCity = this.getParameter("beneficiaryCity");
		if (beneficaiaryCity.equals("other")) {
			transferOversea.setCityFlag("Y");
			transferOversea.setBeneficiaryCity(this.getParameter("otherCity"));
		} else {
			transferOversea.setCityFlag("N");
		}*/
		
		String beneficiaryCountry = this.getParameter("beneficiaryCountry");
		if("other".equals(beneficiaryCountry)){
			transferOversea.setBeneficiaryCountry(this.getParameter("otherCountry"));
		}
		
		if ("".equals(transferOversea.getToAccount().trim())) {
			throw new NTBException("err.txn.AccountNumberIsNotNull");
		}

		GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
		if ("IBAN".equals(Utils.null2EmptyWithTrim(transferOversea.getAccType()))) {
			checkIBANAccount(transferOversea);

//			String length = null;
//			String prefix = null;
//
//			try {
//				List idList1 = null;
//				idList1 = dao.query(SELECT_IBAN, new Object[] { transferOversea
//						.getBeneficiaryCountry() });
//				if (null != idList1 && idList1.size() > 0) {
//					Map cdtMap1 = (Map) idList1.get(0);
//					length = (String) cdtMap1.get("IBAN_LENGTH");
//					prefix = (String) cdtMap1.get("IBAN_PREFIX");
//				}
//
//			} catch (Exception e) {
//				throw new NTBException("err.txn.GetIBANException");
//			}
//
//			// if the length is 00, no need to check the length.
//			if (!length.equals("00")) {
//				String fromLength = (new Integer(transferOversea.getToAccount()
//						.length())).toString();
//				if (!fromLength.equals(length)) {
//					throw new NTBException("err.txn.IbanAccountLengthError");
//				}
//			}
//			
//			// if the prefix is all space, no need to check the prefix input.
//			if (!"".equals(prefix.trim())) {
//				String toAccount1 = transferOversea.getToAccount().substring(0,
//						2).toUpperCase()
//						+ transferOversea.getToAccount().substring(2);
//				transferOversea.setToAccount(toAccount1);
//				if (!(prefix.compareTo(toAccount1.substring(0, 4)) == 0)) {
//					throw new NTBException("err.txn.IbanAccountPrefixError");
//				}
//			}
		}

		transferOversea.setUserId(corpUser.getUserId());
		transferOversea.setCorpId(corpUser.getCorpId());
		transferOversea.setTransId(CibIdGenerator.getRefNoForTransaction());	

		// add mxl 0814
		/* get currency according to account */
//		CorpAccount corpAccount = corpAccountService
//				.viewCorpAccount(transferOversea.getFromAccount());
//		transferOversea.setFromCurrency(corpAccount.getCurrency());

		/* add by mxl 0827 get the currency number according the currency code */
//		CorpAccount corpChargeAccount = corpAccountService
//				.viewCorpAccount(transferOversea.getChargeAccount());
//		String chargeCurrency = corpChargeAccount.getCurrency();
//		transferOversea.setChargeCurrency(chargeCurrency);
		String chargeCurrency = this.getParameter("chargeCurrency");
		String fromCurrencyCode = rcCurrency.getCbsNumberByCcy(transferOversea
				.getFromCurrency());
		String toCurrencyCode = rcCurrency.getCbsNumberByCcy(transferOversea
				.getToCurrency());
		String chargeCurrencyCode = rcCurrency.getCbsNumberByCcy(chargeCurrency);
        
		/* Add by heyongjiang 20100827 */
		//modified by lzg 20190602
		/*int checkPurpose = transferService.checkNeedPurpose(corpUser
				.getCorpId(), transferOversea.getFromAccount(), Utils
				.null2Empty(transferOversea.getTransferAmount()), Utils
				.null2Empty(transferOversea.getDebitAmount()), transferOversea.getFromCurrency(), transferOversea.getToCurrency(),
				transferOversea.getBeneficiaryCountry());
		if (checkPurpose == 1) {
			transferOversea.setProofOfPurpose("N");
			if(transferOversea.getOtherPurpose() == null 
					|| "".equals(transferOversea.getOtherPurpose())){
				throw new NTBWarningException("err.txn.NullPurpose");
			}
			
			if (transferOversea.getPurposeCode() == null
					|| "".equals(transferOversea.getPurposeCode())) {
				throw new NTBWarningException("err.txn.NullPurpose");
			} else {
				if ("99".equals(transferOversea.getPurposeCode())) {
					if (transferOversea.getOtherPurpose() == null
							|| "".equals(transferOversea.getOtherPurpose())) {
						throw new NTBWarningException("err.txn.NullPurpose");
					}
					transferOversea.setPurposeCode("");
				} else {
					transferOversea.setOtherPurpose("");
				}
			}
			
		} else if (checkPurpose == 2) {
			transferOversea.setProofOfPurpose("Y");
			if(transferOversea.getOtherPurpose() == null 
					|| "".equals(transferOversea.getOtherPurpose())){
				throw new NTBWarningException("err.txn.NullPurpose");
			}
			
			if (transferOversea.getPurposeCode() == null
					|| "".equals(transferOversea.getPurposeCode())) {
				throw new NTBWarningException("err.txn.NullPurpose");
			} else {
				if ("99".equals(transferOversea.getPurposeCode())) {
					if (transferOversea.getOtherPurpose() == null
							|| "".equals(transferOversea.getOtherPurpose())) {
						throw new NTBWarningException("err.txn.NullPurpose");
					}
					transferOversea.setPurposeCode("");
				} else {
					transferOversea.setOtherPurpose("");
				}
			}
			
			// throw new NTBWarningException("err.txn.NeedProofDocument");
		} else if (checkPurpose == 3) {
			transferOversea.setProofOfPurpose("E");
			if(transferOversea.getOtherPurpose() == null 
					|| "".equals(transferOversea.getOtherPurpose())){
				throw new NTBWarningException("err.txn.NullPurpose");
			}
			
			if (transferOversea.getPurposeCode() == null
					|| "".equals(transferOversea.getPurposeCode())) {
				throw new NTBWarningException("err.txn.NullPurpose");
			} else {
				if ("99".equals(transferOversea.getPurposeCode())) {
					if (transferOversea.getOtherPurpose() == null
							|| "".equals(transferOversea.getOtherPurpose())) {
						throw new NTBWarningException("err.txn.NullPurpose");
					}
					transferOversea.setPurposeCode("");
				} else {
					transferOversea.setOtherPurpose("");
				}
			}
			
		} else {
			transferOversea.setProofOfPurpose("N");
			transferOversea.setPurposeCode("");
			transferOversea.setOtherPurpose("");
		}*/
		//modified by lzg end
		/* Add by heyongjiang end */
		
		//add by lzg 20190531
		boolean flag = checkChargeAccount(transferOversea);
		if(!flag){
			throw new NTBWarningException("err.txn.chargeAccountError");
		}
		//add by lzg end
		
		

		if (transferOversea.getTransferAmount().doubleValue() == 0) {
			transferOversea
					.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_FROM);
			BigDecimal transferAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferOversea.getFromCurrency(),
					transferOversea.getToCurrency(), new BigDecimal(
							transferOversea.getDebitAmount().toString()), null,
					2);

			transferOversea.setTransferAmount(new Double(transferAmount
					.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinAmtOtherBanks(transferOversea.getFromCurrency(), transferOversea.getDebitAmount().doubleValue());
			
			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferOversea.getFromCurrency(),
					transferOversea.getToCurrency(), 8);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

			if (null == buyRate) {
				buyRate = new BigDecimal("1");
			}
			if (null == sellRate) {
				sellRate = new BigDecimal("1");
			}
			if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
				exchangeRate = new BigDecimal("0");
			} else {
				exchangeRate = buyRate.divide(sellRate, 8,
						BigDecimal.ROUND_DOWN);
			}
			transferOversea.setExchangeRate(new Double(exchangeRate
					.doubleValue()));
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferOversea.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferOversea.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
		} else if (transferOversea.getDebitAmount().doubleValue() == 0) {
			transferOversea.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_TO);
			BigDecimal toAmount = new BigDecimal(transferOversea
					.getTransferAmount().toString());
			BigDecimal fromAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferOversea.getFromCurrency(),
					transferOversea.getToCurrency(), null, toAmount, 2);

			transferOversea.setDebitAmount(new Double(fromAmount.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinAmtOtherBanks(transferOversea.getToCurrency(), transferOversea.getTransferAmount().doubleValue());

			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferOversea.getFromCurrency(),
					transferOversea.getToCurrency(), 8);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

			if (null == buyRate) {
				buyRate = new BigDecimal("1");
			}
			if (null == sellRate) {
				sellRate = new BigDecimal("1");
			}
			if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
				exchangeRate = new BigDecimal("0");
			} else {
				exchangeRate = buyRate.divide(sellRate, 8,
						BigDecimal.ROUND_DOWN);
			}
			transferOversea.setExchangeRate(new Double(exchangeRate
					.doubleValue()));
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferOversea.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferOversea.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
		}
		
		
		//add by lzg 20190902
		Map Host = transferService.toHostOverseasTrial(transferOversea, corpUser);
		if(!transferOversea.getFromCurrency().equals(transferOversea.getToCurrency())){
			transferOversea.setDebitAmount(((BigDecimal)Host.get("FEE_AMT")).doubleValue());
			transferOversea.setTransferAmount(((BigDecimal)Host.get("CHANGE_AMT")).doubleValue());
			transferOversea.setHandlingAmount(((BigDecimal)Host.get("FEE_AMT")).doubleValue());
		}
		transferOversea.setChargeAmount(((BigDecimal)Host.get("CHG_AMT")).doubleValue());
		transferOversea.setExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		transferOversea.setShowExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		//add by lzg end
		
		
		String inputAmountFlag = transferOversea.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferOversea.getFromCurrency();
			transferAmount = transferOversea.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferOversea.getToCurrency();
			transferAmount = transferOversea.getTransferAmount();
		}
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), currency, "MOP", new BigDecimal(transferAmount
				.toString()), null, 2);
		if (!corpUser.checkUserLimit(Constants.TXN_TYPE_TRANSFER_OVERSEAS,
				equivalentMOP)) {
			// write limit report
			Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
					"yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
					"HHmmss"));
			reportMap.put("USER_ID", transferOversea.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_OVERSEAS);
			reportMap.put("LOCAL_TRANS_CODE", transferOversea.getTransId());
			reportMap.put("FROM_CURRENCY", transferOversea.getFromCurrency());
			reportMap.put("TO_CURRENCY", transferOversea.getToCurrency());
			reportMap.put("FROM_ACCOUNT", transferOversea.getFromAccount());
			reportMap.put("FROM_AMOUNT", transferOversea.getDebitAmount());
			reportMap.put("TO_AMOUNT", transferOversea.getTransferAmount());
			reportMap.put("EXCEEDING_TYPE", "2");
			reportMap.put("LIMIT_TYPE", "");
			reportMap.put("USER_LIMIT ", corpUser
					.getUserLimit(Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT));
			reportMap.put("DAILY_LIMIT ", new Double(0));
			reportMap.put("TOTAL_AMOUNT ", new Double(0));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
			throw new NTBWarningException("err.txn.ExceedUserLimit");
		}
		if (!transferLimitService.checkCurAmtLimitQuota(transferOversea
				.getFromAccount(), corpUser.getCorpId(),
				Constants.TXN_TYPE_TRANSFER_OVERSEAS, transferOversea
						.getDebitAmount().doubleValue(), equivalentMOP
						.doubleValue())) {
			// write limit report
			Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
					"yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
					"HHmmss"));
			reportMap.put("USER_ID", transferOversea.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_OVERSEAS);
			reportMap.put("LOCAL_TRANS_CODE", transferOversea.getTransId());
			reportMap.put("FROM_CURRENCY", transferOversea.getFromCurrency());
			reportMap.put("FROM_ACCOUNT", transferOversea.getFromAccount());
			reportMap.put("TO_CURRENCY", transferOversea.getToCurrency());
			reportMap.put("FROM_AMOUNT", transferOversea.getDebitAmount());
			reportMap.put("TO_AMOUNT", transferOversea.getTransferAmount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService
					.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService
					.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
			throw new NTBWarningException("err.txn.ExceedDailyLimit");
		}
		
		// add by mxl 0828 get the CGD from local_bank_code according to the
		transferOversea.setSpbankLabel("#");
		if (transferOversea.getBankFlag().equals("N")) {
			try {
				List idList = null;
				idList = dao.query(SELECT_CGDOVERSEA,
						new Object[] { transferOversea.getBeneficiaryBank1() });
				cdtMap = (Map) idList.get(0);
				CGD = (String) cdtMap.get("CGD_FLAG");

			} catch (Exception e) {
				throw new NTBException("err.txn.GetCGDException");
			}
		}
		
		transferOversea.setSpbankLabel(transferService.loadSpbankLabel(
				transferOversea.getBeneficiaryCountry()).trim());

		if ("IBAN".equals(Utils
				.null2EmptyWithTrim(transferOversea.getAccType()))) {
			Iban = "Y";
		} else {
			Iban = "N";
		}

		//add by lzg 20190619
		checkCutoffTimeAndSetMsg(transferOversea,corpUser.getLangCode());
		//add by lzg end
		
		
		/*fromHost = transferService.toHostChargeEnquiryNew(transferOversea
				.getTransId(), corpUser, new BigDecimal(transferOversea
				.getTransferAmount().toString()), transferOversea
				.getBeneficiaryCountry(), CGD, fromCurrencyCode,
				toCurrencyCode, transferOversea.getChareBy(),
				chargeCurrencyCode, Iban);*/
		//transferService.uploadEnquiryOversea(transferOversea, fromHost);

		/*double chargeAmount = (new Double(fromHost.get("HANDLING_CHRG_AMT")
				.toString())).doubleValue()
				+ (new Double(fromHost.get("COMM_AMT").toString()))
						.doubleValue()
				+ (new Double(fromHost.get("TAX_AMOUNT").toString()))
						.doubleValue()
				+ (new Double(fromHost.get("SWIFT_CHRG_AMT").toString()))
						.doubleValue()
				+ (new Double(fromHost.get("OUR_CHG_AMT").toString()))
						.doubleValue();*/
		// add by mxl 0913
		/*transferOversea.setHandlingAmount(new Double(fromHost.get(
				"HANDLING_CHRG_AMT").toString()));
		transferOversea.setCommissionAmount(new Double(fromHost.get("COMM_AMT")
				.toString()));
		transferOversea.setTaxAmount(new Double(fromHost.get("TAX_AMOUNT")
				.toString()));
		transferOversea.setSwiftAmount(new Double(fromHost
				.get("SWIFT_CHRG_AMT").toString()));
		transferOversea.setChargeOur(new Double(fromHost.get("OUR_CHG_AMT")
				.toString()));

		transferOversea.setChargeAmount(new Double(chargeAmount));*/
		

		Map resultData = new HashMap();
		
		//add by lzg 20190820
		String corpType = corpUser.getCorporation().getCorpType();
		String checkFlag = corpUser.getCorporation().getAuthenticationMode();
		resultData.put("corpType", corpType);
		resultData.put("checkFlag", checkFlag);
		resultData.put("operationType", "send");
		resultData.put("showMobileNo", corpUser.getMobile());
		resultData.put("txnType", Constants.TXN_TYPE_TRANSFER_OVERSEAS);
		
		//add by lzg end
		
		// add by mxl 0821
		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_OVERSEAS);
		resultData.put("currency", currency);
		resultData.put("amount",transferAmount);
		resultData.put("amountMopEq", equivalentMOP);

		resultData.put("otherCityFlag", transferOversea.getCityFlag());
		resultData.put("otherBankFlag", transferOversea.getBankFlag());
		resultData.put("requestType", requestType);
		this.convertPojo2Map(transferOversea, resultData);
		resultData.put("transferDate", transferOversea.getTransferDate());
        
		/* Add by heyongjiang 20100827 */
		String purpose = transferOversea.getOtherPurpose();
		if(purpose != null && !"".equals(purpose)){
			resultData.put("purpose", purpose);
			resultData.put("showPurpose", "true");
		}
		/*
		if (transferOversea.getOtherPurpose() != null
				&& !"".equals(transferOversea.getOtherPurpose())) {
			resultData.put("purpose", transferOversea.getOtherPurpose());
			resultData.put("showPurpose", "true");
		} else {
			if (transferOversea.getPurposeCode() != null
					&& !"".equals(transferOversea.getPurposeCode())) {
				String purposeDescription = transferService
						.getPurposeDescription(transferOversea.getPurposeCode());
				resultData.put("purpose", purposeDescription);
				resultData.put("showPurpose", "true");
			}
		}
		*/
		/* Add by heyongjiang end */

		ApproveRuleService approveRuleService = (ApproveRuleService) Config
				.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
				resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}
		
		//by wency 20130118
//		this.checkDuplicatedInput("O", transferOversea);

		this.setResultData(resultData);


		this.setUsrSessionDataValue("transferOversea", transferOversea);
	}

	public void transferTemplateConfirm() throws NTBException {
		//initial service
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		TransferOversea transferOversea = (TransferOversea) this
				.getUsrSessionDataValue("transferOversea");
		MailService mailService = (MailService) appContext
		.getBean("MailService");

		CorpUser corpUser = (CorpUser) getUser();
		String inputAmountFlag = transferOversea.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferOversea.getFromCurrency();
			transferAmount = transferOversea.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferOversea.getToCurrency();
			transferAmount = transferOversea.getTransferAmount();
		}
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), currency, "MOP",
				new BigDecimal(transferAmount.toString()),
				null, 2);
		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));

		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		if (!transferLimitService.checkLimitQuota(transferOversea
				.getFromAccount(), corpUser.getCorpId(),
				Constants.TXN_TYPE_TRANSFER_OVERSEAS, transferOversea
						.getDebitAmount().doubleValue(), equivalentMOP
						.doubleValue())) {
			// mod by lr 20170515
			Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
					"yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
					"HHmmss"));
			reportMap.put("USER_ID", transferOversea.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_OVERSEAS);
			reportMap.put("LOCAL_TRANS_CODE", transferOversea.getTransId());
			reportMap.put("FROM_CURRENCY", transferOversea.getFromCurrency());
			reportMap.put("FROM_ACCOUNT", transferOversea.getFromAccount());
			reportMap.put("TO_CURRENCY", transferOversea.getToCurrency());
			reportMap.put("FROM_AMOUNT", transferOversea.getDebitAmount());
			reportMap.put("TO_AMOUNT", transferOversea.getTransferAmount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService
					.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService
					.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
			//throw new NTBWarningException("err.txn.ExceedDailyLimit");
			setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg").getString("warnning.txn.ExceedDailyLimit"));
		}
		// add by chen_y for CR225 20170412 end


		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_TRANSFER_OVERSEAS,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				TransferInOverseaAction.class, transferOversea
						.getFromCurrency(), transferOversea.getDebitAmount()
						.doubleValue(), transferOversea.getToCurrency(),
				transferOversea.getTransferAmount().doubleValue(),
				equivalentMOP.doubleValue(), transferOversea.getTransId(),
				transferOversea.getRemark(), getUser(), assignedUser, corpUser
						.getCorporation().getAllowExecutor(), inputAmountFlag);
		
		Map resultData = new HashMap();
		String corpType = getParameter("corpType");
		resultData.put("corpType", corpType);
		try {
			transferOversea.setExecuteTime(new Date());
			transferOversea.setRequestTime(new Date());
			transferOversea.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			transferOversea.setStatus(Constants.STATUS_PENDING_APPROVAL);
			transferOversea.setOperation(Constants.OPERATION_NEW);

//			transferOversea.setRecordType(TransferOversea.TRANSFER_TYPE_GENERAL);
//			transferService.editOversea(transferOversea, corpUser.getUserId());


			// add by mxl 0821
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");

			resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_OVERSEAS);
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);
			
			resultData.put("otherCityFlag", transferOversea.getCityFlag());
			resultData.put("otherBankFlag", transferOversea.getBankFlag());

			this.convertPojo2Map(transferOversea, resultData);
			resultData.put("transferDate", transferOversea.getTransferDate());

			ApproveRuleService approveRuleService = (ApproveRuleService) Config
					.getAppContext().getBean("ApproveRuleService");
			if (!approveRuleService.checkApproveRule(corpUser.getCorpId(), resultData)) {
				throw new NTBException("err.flow.ApproveRuleNotDefined");
			}
            
			/* Add by heyongjiang 20100827 */
			String purpose = transferOversea.getOtherPurpose();
			if(purpose != null && !"".equals(purpose)){
				resultData.put("purpose", purpose);
				resultData.put("showPurpose", "true");
			}
			/*
			if (transferOversea.getOtherPurpose() != null
					&& !"".equals(transferOversea.getOtherPurpose())) {
				resultData.put("purpose", transferOversea.getOtherPurpose());
				resultData.put("showPurpose", "true");
			} else {
				if (transferOversea.getPurposeCode() != null
						&& !"".equals(transferOversea.getPurposeCode())) {
					String purposeDescription = transferService
							.getPurposeDescription(transferOversea
									.getPurposeCode());
					resultData.put("purpose", purposeDescription);
					resultData.put("showPurpose", "true");
				}
			}
			if(transferOversea.getProofOfPurpose().equals("Y")){
				resultData.put("needProof", "Y");
			}
			*/
			/* Add by heyongjiang end */
			
			setResultData(resultData);
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime",transferOversea.getRequestTime());
			dataMap.put("transId",transferOversea.getTransId());
			dataMap.put("toAmount",transferOversea.getTransferAmount());
			dataMap.put("corpName",corpUser.getCorporation().getCorpName());
			dataMap.put("toAccount",transferOversea.getToAccount());
			dataMap.put("beneficiaryAccName",transferOversea.getBeneficiaryName1());
			dataMap.put("beneficiaryBank",transferOversea.getBeneficiaryBank1());
			dataMap.put("transId",transferOversea.getTransId());
			dataMap.put("remark",transferOversea.getRemark());
			

		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			if (e instanceof NTBException) {
				throw (NTBException) e;
			} else {
				throw new NTBException("err.txn.TranscationFaily");
			}
		}
		
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
			transferService.transferAccOverseas(transferOversea);
			try{
				approve(currentFlowProcess.getTxnType(), currentFlowProcess.getTransNo(), this);
			}catch (NTBHostException e) {
				transferService.removeTransfer("transfer_oversea", currentFlowProcess.getTransNo());
				throw new NTBHostException(e.getErrorArray());
			}catch (NTBException e) {
				transferService.removeTransfer("transfer_oversea", currentFlowProcess.getTransNo());
				throw new NTBException(e.getErrorCode());
			}
		}
		if(!"3".equals(corpType)){
			try{
				transferService.transferAccOverseas(transferOversea);
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
		TransferOversea transferOversea = (TransferOversea) this.getUsrSessionDataValue("transferOversea");

		// initial service
		ApplicationContext appContext = Config.getAppContext();
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//linrui 20190729 mul-language

		// 闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柣顓у亞閳规牜鍠婃径瀣伓 ResultData 闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柡鍌樺�栫�氬綊鏌ㄩ悢铏瑰摵闁规儳鎳忕�氬綊鏌ㄩ悢鍛婄伄闁瑰嚖鎷�
		setResultData(new HashMap(1));

		// added by Jet for display
		if ((Constants.INPUT_AMOUNT_FLAG_FROM).equals(transferOversea
				.getInputAmountFlag())) {
			transferOversea.setTransferAmount(null);
		} else if ((Constants.INPUT_AMOUNT_FLAG_TO).equals(transferOversea
				.getInputAmountFlag())) {
			transferOversea.setDebitAmount(null);
		}
		
		Map resultData = new HashMap();
		this.convertPojo2Map(transferOversea, resultData);

		// get country list
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		resultData.put("country", transferService.getCountryList(lang));
		//resultData.put("country", transferService.loadOversea());
		resultData.put("otherCityFlag", transferOversea.getCityFlag());
		resultData.put("otherBankFlag", transferOversea.getBankFlag());

		if ("Y".equals(transferOversea.getBankFlag())) {
			resultData.put("beneficiaryBank1", "other");
			resultData.put("otherBank", transferOversea.getBeneficiaryBank1());
		} else {
			resultData.put("beneficiaryBank1", transferOversea.getBeneficiaryBank1()
					+ "," + transferOversea.getSwiftAddress());
		}
		
		if("Y".equals(transferOversea.getCityFlag())){
			resultData.put("beneficiaryCity","other");
			resultData.put("otherCity", transferOversea.getBeneficiaryCity());
		}
		
		resultData.put("toAccount", transferOversea.getToAccount());
		resultData.put("showToAccount", transferOversea.getToAccount());
		
		Date transferDate = (Date) transferOversea.getTransferDate();
		String transferDateString = DateTime.formatDate(transferDate, defalutPattern);
		resultData.put("transferDateString", transferDateString);
		

		/* Add by heyongkjiang 20100827 */
		String purpose = transferOversea.getOtherPurpose();
		if(purpose != null && !"".equals(purpose)){
			resultData.put("showPurpose", "true");
		}
		/*
		if (transferOversea.getPurposeCode() != null
				&& !"".equals(transferOversea.getPurposeCode())) {
			resultData.put("showPurpose", "true");
		} else {
			if (transferOversea.getOtherPurpose() != null
					&& !"".equals(transferOversea.getOtherPurpose())) {
				resultData.put("isOtherPurpose", "true");
				resultData.put("showPurpose", "true");
			} else {
				resultData.put("isOtherPurpose", "false");
				resultData.put("showPurpose", "false");
			}
		}
		*/
		/* Add by heyongjiang end */
		CorpUser corpUser = (CorpUser) this.getUser();
		//modified by lzg 20190708
		//resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("transferuser", transferService.loadAccountForRemittance(corpUser.getCorpId()));
		//modified by lzg end
		resultData.put("selectFromAcct", transferOversea.getFromAccount() + " - "  + transferOversea.getFromCurrency());//add by linrui for mul-ccy
		resultData.put("selectChargeAcct", transferOversea.getChargeAccount() + " - "  + transferOversea.getChargeCurrency());//add by linrui for mul-ccy
		
		setResultData(resultData);
		
		// 闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柣顓у亗缁鳖噣骞忛悜鑺ユ櫢闁哄倶鍊栫�氬綊鏌ㄩ悢宄板缓閺夊牏鍋撶�氬綊鏌ㄩ悢鐑樼埍ession闂佽法鍠愰弸濠氬箯閻戣姤鏅搁柣銊ユ閸炲骞忕粚濯渘firm闂佽法鍠愰弸濠氬箯瀹勬澘鏅搁梺璺ㄥ枑閺嬪骞忛悜鑺ユ櫢闁哄倶鍊栫�氬綊鎳ｅ锟芥櫢闁跨噦鎷�

		this.setUsrSessionDataValue("transferOversea", transferOversea);
    }
		
	public boolean approve(String txnType, String id, CibAction bean)
			throws NTBException {
		//initial service
		ApplicationContext appContext = Config.getAppContext();
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		CorpUser corpUser = (CorpUser) bean.getUser();
		if (txnType != null) {
			TransferOversea transferOversea = transferService.viewInOversea(id);
			/*Map exchangeMap = exRatesService.getExchangeRateFromHost(corpUser
					.getCorpId(), transferOversea.getFromCurrency(),
					transferOversea.getToCurrency(), 8);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal showExchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");
			
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
			
			transferOversea.setExchangeRate(new Double(exchangeRate
					.doubleValue()));*/
			
			//add by lzg 20190902
			Map Host = transferService.toHostOverseasTrial(transferOversea, corpUser);
			if(!transferOversea.getFromCurrency().equals(transferOversea.getToCurrency())){
				transferOversea.setDebitAmount(((BigDecimal)Host.get("FEE_AMT")).doubleValue());
				transferOversea.setTransferAmount(((BigDecimal)Host.get("CHANGE_AMT")).doubleValue());
				transferOversea.setHandlingAmount(((BigDecimal)Host.get("FEE_AMT")).doubleValue());
			}
			transferOversea.setChargeAmount(((BigDecimal)Host.get("CHG_AMT")).doubleValue());
			transferOversea.setExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
			//transferOversea.setShowExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
			//add by lzg end
			
			
			String inputAmountFlag = transferOversea.getInputAmountFlag();
			String currency = null;
			Double transferAmount = new Double(0);
			if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferOversea.getFromCurrency();
				transferAmount = transferOversea.getDebitAmount();
				
				//jet modified for updating new transaction amount
				/*BigDecimal new_To_Amount_temp = exRatesService.getEquivalentFromHostRate(
						corpUser.getCorpId(), currency, transferOversea
								.getToCurrency(), new BigDecimal(transferAmount
								.toString()), null, 2,exchangeMap);
				transferOversea.setTransferAmount(new Double(new_To_Amount_temp.toString()));*/

			} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferOversea.getToCurrency();
				transferAmount = transferOversea.getTransferAmount();
				
				//jet modified for updating new transaction amount
				/*BigDecimal new_From_Amount_temp = exRatesService.getEquivalentFromHostRate(
						corpUser.getCorpId(), transferOversea.getFromCurrency(),
						currency, null, new BigDecimal(transferAmount
								.toString()), 2,exchangeMap);
				transferOversea.setDebitAmount(new Double(new_From_Amount_temp.toString()));*/

			}
			//add by lzg 20190619
			checkCutoffTimeAndSetMsg(transferOversea,corpUser.getLangCode());
			//add by lzg end
			BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
					.getCorpId(), currency, "MOP",
					new BigDecimal(transferAmount.toString()), null, 2);
			
			/**
			 * add by long_zg 2014-2-16 for tranfer date doesn't check in transfer template
			 * begin
			 */
			String requestType = transferOversea.getRequestType();
			if("2".equals(requestType)){
                //add by lw 20100927
				// get transfer date
				Date transferDate = transferOversea.getTransferDate();		
//				transferDate=DateTime.getDateFromStr(DateTime.formatDate(transferDate, "yyyy-MM-dd HH:mm:ss"));	
				// get current date	
				Date currentDate = DateTime.getDateFromStr(DateTime.getCurrentDate());
				Log.info("TransferTemplateInOverseaAction.approve() transferDate="+transferDate+" currentDate="+currentDate);
				// transfer date expired
				if(currentDate.after(transferDate)){
					Log.info("TransferTemplateInOverseaAction.approve() REJECT: currentDate.after(transferDate)");
					// reject
					transferService.rejectOversea(transferOversea);
					// show error message
					throw new NTBWarningException("err.txn.TransferDateExpired");
				}						
				// add by lw end
			
			}	
			/**
			 * add by long_zg 2014-2-16 for tranfer date doesn't check in transfer template
			 * end
			 */
			
			//add by lzg 20190604
			//transferOversea.setExchangeRate(new Double(showExchangeRate.doubleValue()));
			//add by lzg end
			
			
			//check daily limit
			if (!transferLimitService.checkLimitQuota(transferOversea
					.getFromAccount(), corpUser.getCorpId(), txnType,
					transferOversea.getDebitAmount().doubleValue(),
					equivalentMOP.doubleValue())) {
				 //write limit report
	        	Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("USER_ID", corpUser.getUserId());
				reportMap.put("CORP_ID", corpUser.getCorpId());
				reportMap.put("TRANS_TYPE", txnType);
				reportMap.put("LOCAL_TRANS_CODE", transferOversea.getTransId());
				reportMap.put("FROM_CURRENCY", transferOversea.getFromCurrency());
				reportMap.put("FROM_ACCOUNT", transferOversea.getFromAccount());
				reportMap.put("TO_CURRENCY", transferOversea.getToCurrency());
				reportMap.put("FROM_AMOUNT", transferOversea.getDebitAmount());
				reportMap.put("TO_AMOUNT", transferOversea.getTransferAmount());
				reportMap.put("EXCEEDING_TYPE", "1");
				reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
				reportMap.put("USER_LIMIT ", "0");
				reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
				reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
				throw new NTBWarningException("err.txn.ExceedDailyLimit");
			}

			Map fromHost = transferService.toHostOverseas(transferOversea,
					corpUser, txnType);
			Map resultData = bean.getResultData();
			resultData.put("TRANSFER_REFERENCE", fromHost.get("TRANSFER_REFERENCE"));
			bean.setResultData(resultData);
			
			//add by long_zg 2015-01-28 for CR205 Add IP
			String ipAddress = bean.getRequestIP() ;
			fromHost.put("IP_ADDRESS", ipAddress) ;
			
			//transferService.loadUploadOversea(transferOversea, fromHost);
			transferOversea.setSerialNumber((String)fromHost.get("TRANSFER_REFERENCE"));
			//add by lzg 20190531
			transferService.updateOversea(transferOversea);
			//add by lzg end
			//send email to last approver or executor, add by hjs 20071029
			MailService mailService = (MailService) appContext.getBean("MailService");
			Map dataMap = new HashMap();
			this.convertPojo2Map(transferOversea, dataMap);
			this.convertPojo2Map(transferOversea, resultData);
			setResultData(resultData);
			if("".equals(dataMap.get("remark"))){
				dataMap.put("remark", "--");
			}
			dataMap.put("transferDate", DateTime.formatDate(transferOversea.getTransferDate(), defalutPattern));
			dataMap.put("lastUpdateTime", new Date());
			
			/*mailService.toLastApprover_Executor(Constants.TXN_SUBTYPE_TRANSFER_OVERSEAS, corpUser.getUserId(),dataMap);*/
			mailService.toLastApprover_Executor(Constants.TXN_SUBTYPE_TRANSFER_OVERSEAS, corpUser.getUserId(),corpUser.getCorpId(), dataMap);
			
			
			//add by lzg 20190830
			Locale locale = (Locale) bean.getSession().getAttribute("Locale$Of$Neturbo");
			String lang = "";
			if(locale != null){
				lang = locale.toString();
			}
			String sessionId = bean.getSession().getId() ;
			try {
				SMSOTPUtil.sendNotificationSMS(lang, corpUser.getMobile(), sessionId, Constants.SMS_TEMPLATE_ID_TYPE_REMITTANCE,
						"", (new Date()).toString(), "", corpUser.getCorpId(), "",
						bean.getRequest().getRemoteAddr(), corpUser.getUserId(), "",(String)fromHost.get("TRANSFER_REFERENCE")) ;
			} catch (Exception e) {
				Log.error("send logged in sms msg error", e) ;
			}
			//add by lzg end
			
			return true;
		} else {
			return false;
		}
	}

	public boolean reject(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		if (txnType != null) {
			TransferOversea transferOversea = transferService.viewInOversea(id);
			transferService.rejectOversea(transferOversea);
			return true;
		} else {
			return false;
		}
	}

	public boolean cancel(String txnType, String id, CibAction bean)
			throws NTBException {
		return reject(txnType, id, bean);
	}
	
	public String viewDetail(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");

		TransferOversea transferOversea = transferService.viewInOversea(id);
		Map resultData = bean.getResultData();
		//modified by lzg 20190612
		//convertPojo2Map(transferOversea, resultData);
		//modified by lzg end
		CorpUser corpUser = (CorpUser) bean.getUser();
		/*Map exchangeMap = exRatesService.getExchangeRateFromHost(corpUser
				.getCorpId(), transferOversea.getFromCurrency(),
				transferOversea.getToCurrency(), 8);
		String rateType = (String) exchangeMap.get("rateType");
		BigDecimal exchangeRate = new BigDecimal("0");
		BigDecimal showExchangeRate = new BigDecimal("0");
		BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
		BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");
		
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
				showExchangeRate = new BigDecimal("1").divide(showExchangeRate,8,BigDecimal.ROUND_DOWN);
			}
		}
		
		if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
			exchangeRate = new BigDecimal("0");
		} else {
			exchangeRate = buyRate.divide(sellRate, 8,
					BigDecimal.ROUND_DOWN);
		}*/
		
		// edit by mxl 0912
		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");
		
		//add by lzg 20190902
		double FromAmount = 0;
		double ToAmount = 0;
		Map Host = transferService.toHostOverseasTrial(transferOversea, corpUser);
		if(!transferOversea.getFromCurrency().equals(transferOversea.getToCurrency())){
			FromAmount = ((BigDecimal)Host.get("FEE_AMT")).doubleValue();
			ToAmount = ((BigDecimal)Host.get("CHANGE_AMT")).doubleValue();
		}else{
			FromAmount = transferOversea.getDebitAmount();
			ToAmount = transferOversea.getTransferAmount();
		}
		transferOversea.setHandlingAmount(FromAmount);
		transferOversea.setChargeAmount(((BigDecimal)Host.get("CHG_AMT")).doubleValue());
		transferOversea.setExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		transferOversea.setShowExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		
		resultData.put("newFromAmount", FromAmount);
		resultData.put("newToAmount", ToAmount);
		resultData.put("newExchangeRate", transferOversea.getShowExchangeRate());
		//add by lzg end
		
		
		String inputAmountFlag = transferOversea.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferOversea.getFromCurrency();
			transferAmount = FromAmount;
			
			/*Double new_From_Amount = transferOversea.getDebitAmount();			
			BigDecimal new_To_Amount_temp = exRatesService.getEquivalentFromHostRate(
					corpUser.getCorpId(), currency,
					transferOversea.getToCurrency(), new BigDecimal(transferAmount
							.toString()), null, 2,exchangeMap);
			Double new_To_Amount = new Double(new_To_Amount_temp.toString());
			resultData.put("newFromAmount", new_From_Amount);
			resultData.put("newToAmount", new_To_Amount);*/

		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferOversea.getToCurrency();
			transferAmount = ToAmount;
			
			/*Double new_To_Amount = transferOversea.getTransferAmount();			
			BigDecimal new_From_Amount_temp = exRatesService.getEquivalentFromHostRate(
					corpUser.getCorpId(), transferOversea.getFromCurrency(),
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
		if(transferOversea.getExchangeRate() < 1){
			transferOversea.setExchangeRate(1/transferOversea.getExchangeRate());
		}
		//add by lzg 20190612
		convertPojo2Map(transferOversea, resultData);
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
		
		/* Add by heyongjiang 20100827 */
		/*String purpose = transferOversea.getOtherPurpose();
		if(purpose != null && !"".equals(purpose)){
			resultData.put("purpose", purpose);
			resultData.put("showPurpose", "true");
		}*/
		/*
		if (transferOversea.getOtherPurpose() != null
				&& !"".equals(transferOversea.getOtherPurpose())) {
			resultData.put("purpose", transferOversea.getOtherPurpose());
			resultData.put("showPurpose", "true");
		} else {
			if (transferOversea.getPurposeCode() != null
					&& !"".equals(transferOversea.getPurposeCode())) {
				String purposeDescription = transferService
						.getPurposeDescription(transferOversea.getPurposeCode());
				resultData.put("purpose", purposeDescription);
				resultData.put("showPurpose", "true");
			}
			else{
				resultData.put("purpose", "");
				resultData.put("showPurpose", "false");
			}
		}
		*/
		/* Add by heyongjiang end */
		
		bean.setResultData(resultData);
		return "/WEB-INF/pages/txn/transfer_account/transfer_InOversea_approval_view.jsp";

	}
	
	private void checkCutoffTimeAndSetMsg(TransferOversea transferOversea,String language) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext
				.getBean("CutOffTimeService");

		// check value date cut-off time
		String transferType = "O";
		String message = cutOffTimeService.check("ZJ55", transferOversea
				.getFromCurrency(), transferOversea.getToCurrency(),transferType,language);
		if(message != null){
			throw new NTBWarningException(message);
		}
		
		/*setMessage(cutOffTimeService.check("ZJ55", transferOversea
				.getFromCurrency(), transferOversea.getToCurrency(),transferType));*/

    }
	
	// Jet added 2008-1-16
	/*
	 *  the precedure is:
		1. Validate the length of IBAN. Every country has a fixed IBAN length.
		2. The first 2 characters must be a country code which is equal to the country of the beneficiary bank.
		3. Checksum validation: 
		    a. Check that the total IBAN length is correct as per the country. If not, the IBAN is invalid.
	        b. Move the four initial characters to the end of the string.
	        c. Replace the letters in the string with digits, expanding the string as necessary, such that A=10, B=11 ... Z=35.
	        d. Convert the string to an integer and mod-97 the entire number.
	        
	        For example, PT50 0002 0123 1234 5678 9015 4  :
	        Step 1: Check the IBAN length.
	            The IBAN length is 25. Match with country Portugal (PT).            
	        Step 2: Move the four initial characters to the end of the string.
	            PT50 0002 0123 1234 5678 9015 4  ---->  0002 0123 1234 5678 9015 4 PT50
	        Step 3: Replace the letters with digits.
	            0002 0123 1234 5678 9015 4 PT50  -----> 0002 0123 1234 5678 9015 4 25 29 50
	        Step 4: Convert the string to an integer and mod-97 
	            000201231234567890154252950 mod 97 = 1     The remainder is 1. Valid IBAN number.
	*/
		private void checkIBANAccount(TransferOversea transferOversea)throws NTBException {
			ApplicationContext appContext = Config.getAppContext();
			GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");

			String SELECT_IBAN = "Select IBAN_LENGTH,IBAN_PREFIX from HS_COUNTRY_SPECIFIC_ACCOUNT where COUNTRY_CODE=?";
			String length = null;
//			String prefix = null;

			// Get the checking standard
			try {
				List idList1 = null;
				idList1 = dao.query(SELECT_IBAN, new Object[] { transferOversea.getBeneficiaryCountry() });
				if (null != idList1 && idList1.size() > 0) {
					Map cdtMap1 = (Map) idList1.get(0);
					length = (String) cdtMap1.get("IBAN_LENGTH");
//					prefix = (String) cdtMap1.get("IBAN_PREFIX");
				}

			} catch (Exception e) {
				throw new NTBException("err.txn.GetIBANException");
			}

			// 1. Validate the length of IBAN. Every country has a fixed IBAN length.
			if (!length.equals("00")) {
				String accountLength = (new Integer(transferOversea.getToAccount().length())).toString();
				if (!accountLength.equals(length)) {
					throw new NTBException("err.txn.IbanAccountLengthError");
				}
			}
			
			// 2. check the first 2 characters must be a country code which is equal to the country of the beneficiary bank.
//			if (!"".equals(prefix.trim()) && prefix.length() >2 ) {
//				prefix = prefix.substring(0,2);			
				String accountPrefix = transferOversea.getToAccount().substring(0,2).toUpperCase();			
				String toAccount1 = accountPrefix + transferOversea.getToAccount().substring(2);
				transferOversea.setToAccount(toAccount1);

				if (!(accountPrefix.compareTo(transferOversea.getBeneficiaryCountry()) == 0)) {
					throw new NTBException("err.txn.IbanAccountPrefixError");
				}
//			}

			// 3. Checksum validation
			if (!length.equals("00") && new Integer(length).intValue() > 4){
//				String after4Bytes = transferOversea.getToAccount().substring(4);			
//				String newIBANString = after4Bytes
//						+ prefixConversion(transferOversea.getToAccount().substring(0, 1).toUpperCase())
//						+ prefixConversion(transferOversea.getToAccount().substring(1, 2).toUpperCase())
//						+ transferOversea.getToAccount().substring(2, 4); 

				String tempIBANString = transferOversea.getToAccount().substring(4) + transferOversea.getToAccount().substring(0, 4);
				String newIBANString = "";
				for(int i = 0; i < tempIBANString.length(); i ++){
					newIBANString = newIBANString + prefixConversion(tempIBANString.substring(i, i+1).toUpperCase());
				}
				
				BigInteger newIBAN = new BigInteger(newIBANString);
				if(newIBAN.remainder(new BigInteger("97")).intValue() != 1 ){
					throw new NTBException("err.txn.IbanAccountChecksumError");
				}			
			}
		}

		private String prefixConversion(String letter)throws NTBException {
			String digitString = null;
			if ("A".equals(letter)){
				digitString = "10";
			} else if ("B".equals(letter)) {
				digitString = "11";			
			} else if ("C".equals(letter)) {
				digitString = "12";			
			} else if ("D".equals(letter)) {
				digitString = "13";			
			} else if ("E".equals(letter)) {
				digitString = "14";			
			} else if ("F".equals(letter)) {
				digitString = "15";			
			} else if ("G".equals(letter)) {
				digitString = "16";			
			} else if ("H".equals(letter)) {
				digitString = "17";			
			} else if ("I".equals(letter)) {
				digitString = "18";			
			} else if ("J".equals(letter)) {
				digitString = "19";			
			} else if ("K".equals(letter)) {
				digitString = "20";			
			} else if ("L".equals(letter)) {
				digitString = "21";			
			} else if ("M".equals(letter)) {
				digitString = "22";			
			} else if ("N".equals(letter)) {
				digitString = "23";			
			} else if ("O".equals(letter)) {
				digitString = "24";			
			} else if ("P".equals(letter)) {
				digitString = "25";			
			} else if ("Q".equals(letter)) {
				digitString = "26";			
			} else if ("R".equals(letter)) {
				digitString = "27";			
			} else if ("S".equals(letter)) {
				digitString = "28";			
			} else if ("T".equals(letter)) {
				digitString = "29";			
			} else if ("U".equals(letter)) {
				digitString = "30";			
			} else if ("V".equals(letter)) {
				digitString = "31";			
			} else if ("W".equals(letter)) {
				digitString = "32";			
			} else if ("X".equals(letter)) {
				digitString = "33";			
			} else if ("Y".equals(letter)) {
				digitString = "34";			
			} else if ("Z".equals(letter)) {
				digitString = "35";			
			} else {
				digitString = letter;
			}
			return digitString;
		}	
		
		private void checkDuplicatedInput(String transferType,TransferOversea transferOversea) throws NTBException{
			
			TransferService transferService = (TransferService) Config.getAppContext().getBean("TransferService");
			List transactionList = transferService.listTransferEntity4CheckDuplicatedInput(transferType);
			
			boolean fla = false;
			Iterator it = transactionList.iterator();
			Map map = null;
			String fromAccount="";
			String toAccount="";
			BigDecimal amount=null;
			String beneficiaryName1="";
			String beneficiaryName2="";
			String clientReference="";
			
			while(it.hasNext()){
				map = (HashMap)it.next();
				
				//all fields below will be compare to database
				fromAccount = (String)map.get("FROM_ACCOUNT");
				toAccount = (String)map.get("TO_ACCOUNT");
				amount = (BigDecimal)map.get("TRANSFER_AMOUNT");
				beneficiaryName1 = Utils.null2EmptyWithTrim((String)map.get("BENEFICIARY_NAME1"));
				beneficiaryName2 = Utils.null2EmptyWithTrim((String)map.get("BENEFICIARY_NAME2"));
				clientReference= Utils.null2EmptyWithTrim((String)map.get("REMARK"));
				
				String clientRef = Utils.null2EmptyWithTrim(transferOversea.getRemark());
				String benef1 = Utils.null2EmptyWithTrim(transferOversea.getBeneficiaryName1());
				String benef2 = Utils.null2EmptyWithTrim(transferOversea.getBeneficiaryName2());
				
				//version2
				if(!"".equals(clientRef)&&(!"".equals(benef1)||!"".equals(benef2))){
					if(transferOversea.getFromAccount().equals(fromAccount)&&
							transferOversea.getToAccount().equals(toAccount)&&
							transferOversea.getTransferAmount().doubleValue()==amount.doubleValue()&&
							clientRef.equals(clientReference)&&
							benef1.equals(beneficiaryName1)&&benef2.equals(beneficiaryName2)){
						fla = true;
						break;
					}
				}
				
				/*if(!"".equals(clientRef)&&(!"".equals(benef1)||!"".equals(benef2))){
					if(transferOversea.getFromAccount().equals(fromAccount)&&
							transferOversea.getToAccount().equals(toAccount)&&
							transferOversea.getTransferAmount().doubleValue()==amount.doubleValue()&&
							clientRef.equals(clientReference)&&
							benef1.equals(beneficiaryName1)&&benef2.equals(beneficiaryName2)){
						fla = true;
						break;
					}
				}else if(!"".equals(clientRef)&&("".equals(benef1)&&"".equals(benef2))){
					if(transferOversea.getFromAccount().equals(fromAccount)&&
							transferOversea.getToAccount().equals(toAccount)&&
							transferOversea.getTransferAmount().doubleValue()==amount.doubleValue()&&
							clientRef.equals(clientReference)){
						fla = true;
						break;
					}
				}else if("".equals(clientRef)&&(!"".equals(benef1)||!"".equals(benef2))){
					if(transferOversea.getFromAccount().equals(fromAccount)&&
							transferOversea.getToAccount().equals(toAccount)&&
							transferOversea.getTransferAmount().doubleValue()==amount.doubleValue()&&
							benef1.equals(beneficiaryName1)&&benef2.equals(beneficiaryName2)){
						fla = true;
						break;
					}
				}else if("".equals(clientRef)&&("".equals(benef1)&&"".equals(benef2))){
					if(transferOversea.getFromAccount().equals(fromAccount)&&
							transferOversea.getToAccount().equals(toAccount)&&
							transferOversea.getTransferAmount().doubleValue()==amount.doubleValue()){
						fla = true;
						break;
					}
				}*/
				
			}
			
			if(fla){
				throw new NTBException("error.txn.duplicatedinput");
			}
			
		}
	
}
