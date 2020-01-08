package app.cib.action.txn;

import java.math.BigDecimal;
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
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TransferBank;
import app.cib.bo.txn.TransferMacau;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.core.CibTransClient;
import app.cib.dao.enq.ExchangeRatesDao;
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
import com.neturbo.set.exception.NTBWarningException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Encryption;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

public class TransferTemplateInMacauAction extends CibAction implements Approvable{

	private TransferMacau transferMacau;
    private String defalutPattern = Config.getProperty("DefaultDatePattern");

	public void listTemplate() throws NTBException  {

		setResultData(new HashMap(1));
		// CorpUser corpUser = new CorpUser();

		String corpID = null;

		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");

		CorpUser corpUser = (CorpUser) getUser();
		corpID = corpUser.getCorpId();
		List templateList = transferTemplateService.listTemplateMacau(corpID);
        // 闂備浇娉曢崰搴ゃ亹閵娾晛妫樻い鎾跺仜濞堚晠鏌熸搴″幋闁轰焦鎹囧顒勫Χ閸℃浼揕ist闂備浇娉曢崰鏇⑩�栭幋锕�绀勯柕鍫濇椤忚泛鈽夐幘瀛樸仢闁轰焦鎹囧顒勫Χ閸℃浼�
		String recordFlag = null;
		if ((templateList.size()== 0) || (templateList == null)){
			recordFlag = "Y";
		} else {
			recordFlag = "N";
		}
		for (int i = 0; i < templateList.size(); i++) {
			transferMacau = (TransferMacau) templateList.get(i);


		}
		templateList = this.convertPojoList2MapList(templateList);

		Map resultData = new HashMap();
		resultData.put("recordFlag", recordFlag);
		resultData.put("templateList", templateList);
		setResultData(resultData);
	}

	public void addTemplateLoad() throws NTBException {
//		//add by linrui 20180313
//		setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg",Constants.US).getString("warnning.transfer.DifferenceCcy"));
		HashMap resultData = new HashMap(1);
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		CorpUser corpUser = (CorpUser) this.getUser();
		//modified by lzg 20190708
		//resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("transferuser", transferService.loadAccountForRemittance(corpUser.getCorpId()));
		//modified by lzg end
		
		//add by lzg 20191022
				TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
				TxnPrompt txnPrompt = new TxnPrompt();
				try{
					txnPrompt = transferPromptService.loadByTxnType("3",TransferPromptDao.STATUS_NORMAL);
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
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		TransAmountService transAmountService = (TransAmountService) appContext
				.getBean("TransAmountService");
		TransferService transferService = (TransferService)appContext
		.getBean("TransferService");

//		TransferService transferService = (TransferService) appContext
//				.getBean("TransferService");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		TransferMacau transferMacau = new TransferMacau(CibIdGenerator.getRefNoForTransaction());
		this.convertMap2Pojo(this.getParameters(), transferMacau);
		
		//add by lzg 20190531
		boolean flag = checkChargeAccount(transferMacau);
		if(!flag){
			throw new NTBWarningException("err.txn.chargeAccountError");
		}
		//add by lzg end
		
		
		//add by linrui for mul-language 
        Double transAmt  = TransAmountFormate.formateAmount(this.getParameter("transferAmount"),lang);
        Double debitAmt = TransAmountFormate.formateAmount(this.getParameter("debitAmount"), lang);
        transferMacau.setTransferAmount(transAmt);
        transferMacau.setDebitAmount(debitAmt);
        //end
		CorpUser corpUser = (CorpUser) this.getUser();
		corpUser.setLanguage(lang);//add by linrui for mul-language
//		String SELECT_CGDLOCAL = "select CGD_FLAG,BANK_NAME from HS_LOCAL_BANK_CODE where BANK_CODE=? ";
//		RcCurrency rcCurrency = new RcCurrency();
//		String requestType = this.getParameter("requestType");
//		String transferDateString = this.getParameter("transferDateString");
//		String CGD = "N";
		
//		if (requestType.equals("1")) {
//			getParameters().put("transferDate",
//					DateTime.getDateFromStr(DateTime.getCurrentDate()));
//		} else if (requestType.equals("2")) {
//			Date transferDate = DateTime.getDateFromStr(transferDateString, false);
//			Date today = new Date();
//			if (!transferDate.after(today)) {
//				throw new NTBException("err.txn.transferDateMustLater");
//			}
//			int daynum=DateTime.getDaysTween(transferDate,today);
//			if(daynum>=14){
//				throw new NTBException("err.txn.transferLaterDateError");
//			}
//			getParameters().put("transferDate",
//					DateTime.getDateFromStr(transferDateString));
//		}

		transferMacau.setUserId(corpUser.getUserId());
		transferMacau.setCorpId(corpUser.getCorpId());
//		transferMacau.setExecuteTime(new Date());
//		transferMacau.setRequestTime(new Date());
		
		// chargeAccount,chargeBy (charge account is always same as from account in transfer in Macau)
		//modified by lzg 20190603
		//transferMacau.setChargeAccount(transferMacau.getFromAccount());
		//modified by lzg end
		// Jet modified 2007-12-18
//		transferMacau.setChargeBy("O");

		/* add 0814 get currency according to account 
		CorpAccount corpAccount = corpAccountService
				.viewCorpAccount(transferMacau.getFromAccount());
		transferMacau.setFromCurrency(corpAccount.getCurrency());

		/* add by mxl 0827 get the currency number according the currency code */
//		CorpAccount corpChargeAccount = corpAccountService.viewCorpAccount(transferMacau.getChargeAccount());
//		String chargeCurrency = corpChargeAccount.getCurrency();
//		transferMacau.setChargeCurrency(chargeCurrency);
//		String chargeCurrency = this.getParameter("chargeCurrency");
//		transferMacau.setChargeCurrency(chargeCurrency);
		
//		String fromCurrencyCode = rcCurrency.getCbsNumberByCcy(transferMacau
//				.getFromCurrency());
//		String toCurrencyCode = rcCurrency.getCbsNumberByCcy(transferMacau
//				.getToCurrency());
//		String chareCurrencyCode = rcCurrency.getCbsNumberByCcy(chargeCurrency);
		
		// get CGD from local_bank_code according to the bank_name
//		GenericJdbcDao dao = (GenericJdbcDao) appContext
//				.getBean("genericJdbcDao");
//		try {
//			List idList = null;
//			idList = dao.query(SELECT_CGDLOCAL, new Object[] { transferMacau
//					.getBeneficiaryBank() });
//			Map cdtMap = (Map) idList.get(0);
//			CGD = (String) cdtMap.get("CGD_FLAG");
//		} catch (Exception e) {
//			throw new NTBException("err.txn.GetCGDException");
//		}
		
//		Map fromHost = transferService.toHostChargeEnquiryNew(transferMacau
//				.getTransId(), corpUser, new BigDecimal(transferMacau
//				.getTransferAmount().toString()), "MO", CGD, fromCurrencyCode,
//				toCurrencyCode, transferMacau.getChargeBy(), chareCurrencyCode,
//				"N");
//		transferService.uploadEnquiryMacau(transferMacau, fromHost);

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
//
//		transferMacau.setHandlingAmount(new Double(fromHost.get(
//				"HANDLING_CHRG_AMT").toString()));
//		transferMacau.setCommissionAmount(new Double(fromHost.get("COMM_AMT")
//				.toString()));
//		transferMacau.setTaxAmount(new Double(fromHost.get("TAX_AMOUNT")
//				.toString()));
//		transferMacau.setSwiftAmount(new Double(fromHost.get("SWIFT_CHRG_AMT")
//				.toString()));
//		transferMacau.setChargeOur(new Double(fromHost.get("OUR_CHG_AMT")
//				.toString()));
//		transferMacau.setChargeAmount(new Double(chargeAmount));

		if (transferMacau.getTransferAmount().doubleValue() == 0) {
			transferMacau.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_FROM);
			BigDecimal transferAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(), new BigDecimal(transferMacau
							.getDebitAmount().toString()), null, 2);

			transferMacau.setTransferAmount(new Double(transferAmount
					.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinAmtOtherBanks(transferMacau.getFromCurrency(), transferMacau.getDebitAmount().doubleValue());
			
			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(), 8);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

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
			transferMacau.setExchangeRate(new Double(exchangeRate.doubleValue()));
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferMacau.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferMacau.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
		} else if (transferMacau.getDebitAmount() == null
				|| transferMacau.getDebitAmount().doubleValue() == 0) {

			transferMacau.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_TO);
			BigDecimal toAmount = new BigDecimal(transferMacau
					.getTransferAmount().toString());
			BigDecimal fromAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(),
					null, toAmount, 2);

			transferMacau.setDebitAmount(new Double(fromAmount.toString()));
			
			transAmountService.checkMinAmtOtherBanks(transferMacau.getToCurrency(), transferMacau.getTransferAmount().doubleValue());
			
			
			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(),

			transferMacau.getFromCurrency(), transferMacau.getToCurrency(), 8);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

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
			transferMacau.setExchangeRate(new Double(exchangeRate.doubleValue()));
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferMacau.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferMacau.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
		}
		
		
		//add by lzg 20190902
		Map Host = transferService.toHostInMacauTrial(transferMacau, corpUser);
		if(!transferMacau.getFromCurrency().equals(transferMacau.getToCurrency())){
			transferMacau.setDebitAmount(((BigDecimal)Host.get("FEE_AMT")).doubleValue());
			transferMacau.setTransferAmount(((BigDecimal)Host.get("CHANGE_AMT")).doubleValue());
			transferMacau.setHandlingAmount(((BigDecimal)Host.get("FEE_AMT")).doubleValue());
		}
		transferMacau.setChargeAmount(((BigDecimal)Host.get("CHG_AMT")).doubleValue());
		transferMacau.setExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		transferMacau.setShowExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		//add by lzg end
		
		transferMacau.setOperation(Constants.OPERATION_NEW);
		transferMacau.setRecordType(TransferBank.TRANSFER_TYPE_TEMPLATE);

		Map resultData = new HashMap();
		this.setResultData(resultData);
		this.convertPojo2Map(transferMacau, resultData);
		
		this.setUsrSessionDataValue("transferMacau", transferMacau);
	}

	private boolean checkChargeAccount(TransferMacau transferMacau) throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		CibTransClient testClient = new CibTransClient("CIB", "0195");
		String chargeAccount = transferMacau.getChargeAccount();
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
					if(ccy.equals(transferMacau.getFromCurrency())){
						flag = true;
					}
					
				}
			}
		}
		return flag;
	}

	public void addTemplateConfirm() throws NTBException {
		// initial service
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");

		TransferMacau transferMacau = (TransferMacau) this.getUsrSessionDataValue("transferMacau");
		transferMacau.setExecuteTime(new Date());
		transferMacau.setRequestTime(new Date());
		transferTemplateService.addTemplateMacau(transferMacau);

		//for display
		Map resultData = new HashMap();
		setResultData(resultData);
		this.convertPojo2Map(transferMacau, resultData);
	}

    public void addCancel() throws NTBException {
		TransferMacau transferMacau = (TransferMacau) this.getUsrSessionDataValue("transferMacau");
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		

		// 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌ｉ褍浜為柍瑙勭墱閸犲﹥寰勭�ｎ亶浼� ResultData 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷閻庢艾缍婇弻銊╂偄閾忕懓鎽甸梺瑙勫劤閹冲繒锟借ぐ鎺撶叆闁绘柨鎲￠悘顕�鏌熼崙銈嗗
		setResultData(new HashMap(1));

		// added by Jet for display
		if ((Constants.INPUT_AMOUNT_FLAG_FROM).equals(transferMacau
				.getInputAmountFlag())) {
			transferMacau.setTransferAmount(null);
		} else if ((Constants.INPUT_AMOUNT_FLAG_TO).equals(transferMacau
				.getInputAmountFlag())) {
			transferMacau.setDebitAmount(null);
		}
				
		Map resultData = new HashMap();
		this.convertPojo2Map(transferMacau, resultData);
		CorpUser corpUser = (CorpUser) this.getUser();
		//modified by lzg 20190708
		//resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("transferuser", transferService.loadAccountForRemittance(corpUser.getCorpId()));
		//modified by lzg end
		resultData.put("selectFromAcct", transferMacau.getFromAccount() + " - "  + transferMacau.getFromCurrency());//add by linrui for mul-ccy
		setResultData(resultData);
		
		// 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌ｉ褍浜楃紒槌栧櫍楠炲繘鎮滈懞銉︽闂佸搫鍊堕崐鏍拷瑜版帗鐓ラ柣鏂垮槻瀵ゆ椽寮堕崼銏犱壕閻庢艾缍婇弻銊╂偄閻戞鍩峞ssion闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌ｉ妸銉︻棯闁哥偛顭烽獮蹇曠矚婵笜firm闂備浇娉曢崰鎰板几婵犳艾绠�瑰嫭婢橀弲鎼佹⒑鐠恒劌鏋戦柡瀣煼楠炲繘鎮滈懞銉︽闂佸搫鍊堕崐鏍拷瑜版帗鍤曟慨妤嬫嫹閺呮悂鏌ㄩ悤鍌涘
		this.setUsrSessionDataValue("transferMacau", transferMacau);
    }
	
	public void editTemplateLoad() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext.getBean("TransferTemplateService");
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		
		String transID = getParameter("transId");

		TransferMacau transferMacau = transferTemplateService
				.viewTemplateMacau(transID);

		// added by Jet for display
		if ((Constants.INPUT_AMOUNT_FLAG_FROM).equals(transferMacau
				.getInputAmountFlag())) {
			transferMacau.setTransferAmount(null);
		} else if ((Constants.INPUT_AMOUNT_FLAG_TO).equals(transferMacau
				.getInputAmountFlag())) {
			transferMacau.setDebitAmount(null);
		}
				
		Map resultData = new HashMap();
		this.convertPojo2Map(transferMacau, resultData);
		CorpUser corpUser = (CorpUser) this.getUser();
		//modified by lzg 20190708
		//resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("transferuser", transferService.loadAccountForRemittance(corpUser.getCorpId()));
		//modified by lzg end
		resultData.put("selectFromAcct", transferMacau.getFromAccount() + " - "  + transferMacau.getFromCurrency());//add by linrui for mul-ccy
		setResultData(resultData);
		
		//add by lzg 20191022
				TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
				TxnPrompt txnPrompt = new TxnPrompt();
				try{
					txnPrompt = transferPromptService.loadByTxnType("3",TransferPromptDao.STATUS_NORMAL);
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
		
		//add by linrui 20180313
		//setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg",Constants.US).getString("warnning.transfer.DifferenceCcy"));
		this.setUsrSessionDataValue("transferMacau", transferMacau);
	}

	public void editTemplate() throws NTBException {
		TransferMacau transferMacau = (TransferMacau) this.getUsrSessionDataValue("transferMacau");
		this.convertMap2Pojo(this.getParameters(), transferMacau);
		
		//add by lzg 20190531
		boolean flag = checkChargeAccount(transferMacau);
		if(!flag){
			throw new NTBWarningException("err.txn.chargeAccountError");
		}
		//add by lzg end
		
		// initial service
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		TransAmountService transAmountService = (TransAmountService) appContext
				.getBean("TransAmountService");
		TransferService transferService = (TransferService)appContext
		.getBean("TransferService");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
//		TransferService transferService = (TransferService) appContext
//				.getBean("TransferService");
		//add by linrui for mul-language 
        Double transAmt  = TransAmountFormate.formateAmount(this.getParameter("transferAmount"),lang);
        Double debitAmt = TransAmountFormate.formateAmount(this.getParameter("debitAmount"), lang);
        transferMacau.setTransferAmount(transAmt);
        transferMacau.setDebitAmount(debitAmt);
        //end
		CorpUser corpUser = (CorpUser) this.getUser();
		corpUser.setLanguage(lang);//add by linrui for mul-language
//		String SELECT_CGDLOCAL = "select CGD_FLAG,BANK_NAME from HS_LOCAL_BANK_CODE where BANK_CODE=? ";
//		RcCurrency rcCurrency = new RcCurrency();
//		String requestType = this.getParameter("requestType");
//		String transferDateString = this.getParameter("transferDateString");
//		String CGD = "N";

		// edit by nabai 闂備浇娉曢崰搴ゃ亹閵娧勫閻熸瑥瀚鍫曞级閻戝棗澧撮柡浣规崌瀵剟濡堕崱妤婁紦闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷閻庢熬鎷�
//		if (requestType.equals("1")) {
//			getParameters().put("transferDate",
//					DateTime.getDateFromStr(DateTime.getCurrentDate()));
//		} else if (requestType.equals("2")) {
//			Date transferDate = DateTime.getDateFromStr(transferDateString, false);
//			Date today = new Date();
//			if (!transferDate.after(today)) {
//				throw new NTBException("err.txn.transferDateMustLater");
//			}
//			int daynum=DateTime.getDaysTween(transferDate,today);
//			if(daynum>=14){
//				throw new NTBException("err.txn.transferLaterDateError");
//			}
//			getParameters().put("transferDate",
//					DateTime.getDateFromStr(transferDateString));
//		}

		transferMacau.setUserId(corpUser.getUserId());
		transferMacau.setCorpId(corpUser.getCorpId());
		transferMacau.setExecuteTime(new Date());
		transferMacau.setRequestTime(new Date());

		// chargeAccount,chargeBy (charge account is always same as from account in transfer in Macau)
//		transferMacau.setChargeAccount(transferMacau.getFromAccount());
		// Jet modified 2007-12-18
//		transferMacau.setChargeBy("O");

		/* add 0814 get currency according to account 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷閻庢艾婀辨禒锔芥償閳╁啫鏆梻浣芥硶閸犳劙寮告繝姘闁绘垼濮ら弲鍝ョ磽娴ｅ搫浠滅憸鏉挎健閹啴宕熼鍡曡闂佸綊顥熼崗姗�寮幘璇叉闁靛牆妫楅鍫曟⒑鐠恒劌鏋戦柡瀣煼楠炲繘鎮滈懞銉︽闂佽法鍣﹂幏锟�
		CorpAccount corpAccount = corpAccountService
				.viewCorpAccount(transferMacau.getFromAccount());
		transferMacau.setFromCurrency(corpAccount.getCurrency());

		/* add by mxl 0827 get the currency number according the currency code */
//		CorpAccount corpChargeAccount = corpAccountService
//				.viewCorpAccount(transferMacau.getChargeAccount());
//		String chargeCurrency = corpChargeAccount.getCurrency();
//		transferMacau.setChargeCurrency(chargeCurrency);
//		String chargeCurrency = this.getParameter("chargeCurrency");
//		transferMacau.setChargeCurrency(chargeCurrency);
		
//		String fromCurrencyCode = rcCurrency.getCbsNumberByCcy(transferMacau
//				.getFromCurrency());
//		String toCurrencyCode = rcCurrency.getCbsNumberByCcy(transferMacau
//				.getToCurrency());
//		String chareCurrencyCode = rcCurrency.getCbsNumberByCcy(chargeCurrency);
		
		// get CGD from local_bank_code according to the bank_name
//		GenericJdbcDao dao = (GenericJdbcDao) appContext
//				.getBean("genericJdbcDao");
//		try {
//			List idList = null;
//			idList = dao.query(SELECT_CGDLOCAL, new Object[] { transferMacau
//					.getBeneficiaryBank() });
//			Map cdtMap = (Map) idList.get(0);
//			CGD = (String) cdtMap.get("CGD_FLAG");
//		} catch (Exception e) {
//			throw new NTBException("err.txn.GetCGDException");
//		}

        // 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷閻庢艾缍婇弻銊╂偄閸涘﹦浼勯梺褰掝棢閺屽顢氬Δ鍛叆闁绘柨鎲￠悘顕�鏌熸搴″幋闁轰焦鎹囧顒勫Χ閸℃浼撻梻浣芥硶閸犳劙寮告繝姘闁绘垼濮ら弲鎼佹煛閸屾ê锟介悗姘秺閺屻劑鎮ら崒娑橆伓edit by mxl 0115
//		Map fromHost = transferService.toHostChargeEnquiryNew(transferMacau
//				.getTransId(), corpUser, new BigDecimal(transferMacau
//				.getTransferAmount().toString()), "MO", CGD, fromCurrencyCode,
//				toCurrencyCode, transferMacau.getChargeBy(), chareCurrencyCode,
//				"N");
//		transferService.uploadEnquiryMacau(transferMacau, fromHost);
//
//		// 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呯鈹戦钘夊婵﹫绠撻弻銊╂偄閸涘﹦浼勯梺褰掝棢閸忔﹢寮幘璇叉闁靛牆妫楅鍫曟⒑鐠恒劌鏋戦柡瀣煼楠炲繘鎮滈懞銉︽闂佽法鍠撶粋濯渕puting the charge amount 0912
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
//
//		transferMacau.setHandlingAmount(new Double(fromHost.get(
//				"HANDLING_CHRG_AMT").toString()));
//		transferMacau.setCommissionAmount(new Double(fromHost.get("COMM_AMT")
//				.toString()));
//		transferMacau.setTaxAmount(new Double(fromHost.get("TAX_AMOUNT")
//				.toString()));
//		transferMacau.setSwiftAmount(new Double(fromHost.get("SWIFT_CHRG_AMT")
//				.toString()));
//		transferMacau.setChargeOur(new Double(fromHost.get("OUR_CHG_AMT")
//				.toString()));
//		transferMacau.setChargeAmount(new Double(chargeAmount));

		if (transferMacau.getTransferAmount().doubleValue() == 0) {
			transferMacau.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_FROM);
			BigDecimal transferAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(), new BigDecimal(transferMacau
							.getDebitAmount().toString()), null, 2);

			transferMacau.setTransferAmount(new Double(transferAmount
					.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinAmtOtherBanks(transferMacau.getFromCurrency(), transferMacau.getDebitAmount().doubleValue());

			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(), 8);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

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
			transferMacau.setExchangeRate(new Double(exchangeRate.doubleValue()));
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferMacau.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferMacau.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
		} else if (transferMacau.getDebitAmount() == null
				|| transferMacau.getDebitAmount().doubleValue() == 0) {

			transferMacau.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_TO);
			BigDecimal toAmount = new BigDecimal(transferMacau
					.getTransferAmount().toString());
			BigDecimal fromAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(),
					null, toAmount, 2);

			transferMacau.setDebitAmount(new Double(fromAmount.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinAmtOtherBanks(transferMacau.getToCurrency(), transferMacau.getTransferAmount().doubleValue());
			
			
			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(),

			transferMacau.getFromCurrency(), transferMacau.getToCurrency(), 8);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

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
			transferMacau.setExchangeRate(new Double(exchangeRate.doubleValue()));
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferMacau.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferMacau.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
		}
		
		//add by lzg 20190902
		Map Host = transferService.toHostInMacauTrial(transferMacau, corpUser);
		if(!transferMacau.getFromCurrency().equals(transferMacau.getToCurrency())){
			transferMacau.setDebitAmount(((BigDecimal)Host.get("FEE_AMT")).doubleValue());
			transferMacau.setTransferAmount(((BigDecimal)Host.get("CHANGE_AMT")).doubleValue());
			transferMacau.setHandlingAmount(((BigDecimal)Host.get("FEE_AMT")).doubleValue());
		}
		transferMacau.setChargeAmount(((BigDecimal)Host.get("CHG_AMT")).doubleValue());
		transferMacau.setExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		transferMacau.setShowExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		//add by lzg end
		
		transferMacau.setRecordType(TransferBank.TRANSFER_TYPE_TEMPLATE);
		transferMacau.setOperation(Constants.OPERATION_UPDATE);
		
		Map resultData = new HashMap();
		this.setResultData(resultData);
		this.convertPojo2Map(transferMacau, resultData);
		
		this.setUsrSessionDataValue("transferMacau", transferMacau);
	}

	public void editTemplateConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");

		TransferMacau transferMacau = (TransferMacau) this
				.getUsrSessionDataValue("transferMacau");
		
		//commit
		transferTemplateService.editTemplateMacau(transferMacau);
		Map resultData = new HashMap();
		setResultData(resultData);
		this.convertPojo2Map(transferMacau, resultData);
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

		TransferMacau transferMacau = transferTemplateService
				.viewTemplateMacau(transID);
		transferMacau.setOperation(Constants.OPERATION_REMOVE);

		Map resultData = new HashMap();
		this.convertPojo2Map(transferMacau, resultData);
		setResultData(resultData);
		// 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌ｉ褍浜楃紒槌栧櫍楠炲繘鎮滈懞銉︽闂佸搫鍊堕崐鏍拷瑜版帗鐓ラ柣鏂垮槻瀵ゆ椽寮堕崼銏犱壕閻庢艾缍婇弻銊╂偄閻戞鍩峞ssion闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌ｉ妸銉︻棯闁哥偛顭烽獮蹇曠矚婵笜firm闂備浇娉曢崰鎰板几婵犳艾绠�瑰嫭婢橀弲鎼佹⒑鐠恒劌鏋戦柡瀣煼楠炲繘鎮滈懞銉︽闂佸搫鍊堕崐鏍拷瑜版帗鍤曟慨妤嬫嫹閺呮悂鏌ㄩ悤鍌涘
		this.setUsrSessionDataValue("transferMacau", transferMacau);

	}

	public void deleteTemplateConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");

		TransferMacau transferMacau = (TransferMacau) this
				.getUsrSessionDataValue("transferMacau");
		transferTemplateService.deleteTemplateMacau(transferMacau.getTransId());
		Map resultData = new HashMap();
		setResultData(resultData);
		this.convertPojo2Map(transferMacau, resultData);
	}

	public void transferTemplateLoad() throws NTBException {
		// initial service
		ApplicationContext appContext = Config.getAppContext();
		TransferTemplateService transferTemplateService = (TransferTemplateService) appContext
				.getBean("TransferTemplateService");

		// 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌ｉ褍浜為柍瑙勭墱閸犲﹥寰勭�ｎ亶浼� ResultData 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷閻庢艾缍婇弻銊╂偄閾忕懓鎽甸梺瑙勫劤閹冲繒锟借ぐ鎺撶叆闁绘柨鎲￠悘顕�鏌熼崙銈嗗
		setResultData(new HashMap(1));
		String transID = getParameter("transId");
		TransferMacau transferMacau = transferTemplateService.viewTemplateMacau(transID);

		// added by Jet for display
		if ((Constants.INPUT_AMOUNT_FLAG_FROM).equals(transferMacau
				.getInputAmountFlag())) {
			transferMacau.setTransferAmount(null);
		} else if ((Constants.INPUT_AMOUNT_FLAG_TO).equals(transferMacau
				.getInputAmountFlag())) {
			transferMacau.setDebitAmount(null);
		}
		
		// 闂備浇娉曢崰鎰板几婵犳艾绠瀣昂娴狅拷
		Map resultData = new HashMap();
		this.convertPojo2Map(transferMacau, resultData);
		
		/* Add by heyongjiang 20100827 begin  */
		//modified by lzg 20190602
		CorpUser corpUser = (CorpUser) this.getUser();
		TransferService transferService = (TransferService) appContext
		.getBean("TransferService");
		/*int checkPurpose = transferService.checkNeedPurpose(corpUser
				.getCorpId(), transferMacau.getFromAccount(), Utils
				.null2Empty(transferMacau.getTransferAmount()), Utils
				.null2Empty(transferMacau.getDebitAmount()), transferMacau
				.getFromCurrency(), transferMacau.getToCurrency(), "MO");
		if (checkPurpose == 1) {
			resultData.put("showPurpose", "true");
		} else if (checkPurpose == 2) {
			resultData.put("showPurpose", "true");
		} else if (checkPurpose == 3) {
			resultData.put("showPurpose", "true");
		} else {
			resultData.put("showPurpose", "false");
		}*/
		//modified by lzg end
		/* Add by heyongjiang end */
		//modified by lzg 20190708
		//resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("transferuser", transferService.loadAccountForRemittance(corpUser.getCorpId()));
		//modified by lzg end
		resultData.put("selectFromAcct", transferMacau.getFromAccount() + " - "  + transferMacau.getFromCurrency());//add by linrui for mul-ccy
		
		//add by lzg 20191022
				TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
				TxnPrompt txnPrompt = new TxnPrompt();
				try{
					txnPrompt = transferPromptService.loadByTxnType("3",TransferPromptDao.STATUS_NORMAL);
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
		
		// 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌ｉ褍浜楃紒槌栧櫍楠炲繘鎮滈懞銉︽闂佸搫鍊堕崐鏍拷瑜版帗鐓ラ柣鏂垮槻瀵ゆ椽寮堕崼銏犱壕閻庢艾缍婇弻銊╂偄閻戞鍩峞ssion闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌ｉ妸銉︻棯闁哥偛顭烽獮蹇曠矚婵笜firm闂備浇娉曢崰鎰板几婵犳艾绠�瑰嫭婢橀弲鎼佹⒑鐠恒劌鏋戦柡瀣煼楠炲繘鎮滈懞銉︽闂佸搫鍊堕崐鏍拷瑜版帗鍤曟慨妤嬫嫹閺呮悂鏌ㄩ悤鍌涘
		this.setUsrSessionDataValue("transferMacau", transferMacau);
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


		CorpUser corpUser = (CorpUser) this.getUser();
		TransferMacau transferMacau = (TransferMacau) this.getUsrSessionDataValue("transferMacau");
		
		String SELECT_CGDLOCAL = "select CGD_FLAG,BANK_NAME from HS_LOCAL_BANK_CODE where BANK_CODE=? ";
		RcCurrency rcCurrency = new RcCurrency();
		String requestType = this.getParameter("requestType");
		String transferDateString = this.getParameter("transferDateString");
		String CGD = "N";

		if (requestType.equals("1")) {
			getParameters().put("transferDate",
					DateTime.getDateFromStr(DateTime.getCurrentDate()));
		} else if (requestType.equals("2")) {
			Date transferDate = DateTime.getDateFromStr(transferDateString, false);
			Date today = new Date();
			if (!transferDate.after(today)) {
				throw new NTBException("err.txn.transferDateMustLater");
			}
			int daynum=DateTime.getDaysTween(transferDate,today);
			if(daynum>=14){
				throw new NTBException("err.txn.transferLaterDateError");
			}
			getParameters().put("transferDate",
					DateTime.getDateFromStr(transferDateString));
		}
		
		this.convertMap2Pojo(this.getParameters(), transferMacau);

		transferMacau.setUserId(corpUser.getUserId());
		transferMacau.setCorpId(corpUser.getCorpId());
		transferMacau.setTransId(CibIdGenerator.getRefNoForTransaction());	
//		transferMacau.setExecuteTime(new Date());
//		transferMacau.setRequestTime(new Date());
		
		//modified by lzg 20190603
		//transferMacau.setChargeAccount(transferMacau.getFromAccount());
		//modified by lzg end
		// Jet modified 2007-12-18
//		transferMacau.setChargeBy("O");

		/* add 0814 get currency accounding to account */
//		CorpAccount corpAccount = corpAccountService.viewCorpAccount(transferMacau.getFromAccount());
//		transferMacau.setFromCurrency(corpAccount.getCurrency());

		/* add by mxl 0827 get the currency number according the currency code */
//		CorpAccount corpChargeAccount = corpAccountService.viewCorpAccount(transferMacau.getChargeAccount());
//		String chargeCurrency = corpChargeAccount.getCurrency();
//		transferMacau.setChargeCurrency(chargeCurrency);
		String chargeCurrency = this.getParameter("chargeCurrency");
		transferMacau.setChargeCurrency(chargeCurrency);


		String fromCurrencyCode = rcCurrency.getCbsNumberByCcy(transferMacau
				.getFromCurrency());
		String toCurrencyCode = rcCurrency.getCbsNumberByCcy(transferMacau
				.getToCurrency());
		String chareCurrencyCode = rcCurrency.getCbsNumberByCcy(chargeCurrency);

		// get CGD from local_bank_code according to the bank_name
		GenericJdbcDao dao = (GenericJdbcDao) appContext
				.getBean("genericJdbcDao");
		try {
			List idList = null;
			idList = dao.query(SELECT_CGDLOCAL, new Object[] { transferMacau
					.getBeneficiaryBank() });
			Map cdtMap = (Map) idList.get(0);
			CGD = (String) cdtMap.get("CGD_FLAG");
		} catch (Exception e) {
			throw new NTBException("err.txn.GetCGDException");
		}
		

		/* Add by heyongjiang 20100827 */
		//modified by lzg 20190602
		/*int checkPurpose = transferService.checkNeedPurpose(corpUser
				.getCorpId(), transferMacau.getFromAccount(), Utils
				.null2Empty(transferMacau.getTransferAmount()), Utils
				.null2Empty(transferMacau.getDebitAmount()), transferMacau
				.getFromCurrency(), transferMacau.getToCurrency(), "MO");
		if (checkPurpose == 1) {
			transferMacau.setProofOfPurpose("N");
			if(transferMacau.getOtherPurpose() == null 
					|| "".equals(transferMacau.getOtherPurpose())){
				throw new NTBWarningException("err.txn.NullPurpose");
			}
			
			if (transferMacau.getPurposeCode() == null
					|| "".equals(transferMacau.getPurposeCode())) {
				throw new NTBWarningException("err.txn.NullPurpose");
			} else {
				if ("99".equals(transferMacau.getPurposeCode())) {
					if (transferMacau.getOtherPurpose() == null
							|| "".equals(transferMacau.getOtherPurpose())) {
						throw new NTBWarningException("err.txn.NullPurpose");
					}
					transferMacau.setPurposeCode("");
				} else {
					transferMacau.setOtherPurpose("");
				}
			}
			
		} else if (checkPurpose == 2) {
			transferMacau.setProofOfPurpose("Y");
			if(transferMacau.getOtherPurpose() == null 
					|| "".equals(transferMacau.getOtherPurpose())){
				throw new NTBWarningException("err.txn.NullPurpose");
			}
			
			if (transferMacau.getPurposeCode() == null
					|| "".equals(transferMacau.getPurposeCode())) {
				throw new NTBWarningException("err.txn.NullPurpose");
			} else {
				if ("99".equals(transferMacau.getPurposeCode())) {
					if (transferMacau.getOtherPurpose() == null
							|| "".equals(transferMacau.getOtherPurpose())) {
						throw new NTBWarningException("err.txn.NullPurpose");
					}
					transferMacau.setPurposeCode("");
				} else {
					transferMacau.setOtherPurpose("");
				}
			}
			
			// throw new NTBWarningException("err.txn.NeedProofDocument");
		} else if (checkPurpose == 3) {
			transferMacau.setProofOfPurpose("E");
			if(transferMacau.getOtherPurpose() == null 
					|| "".equals(transferMacau.getOtherPurpose())){
				throw new NTBWarningException("err.txn.NullPurpose");
			}
			
			if (transferMacau.getPurposeCode() == null
					|| "".equals(transferMacau.getPurposeCode())) {
				throw new NTBWarningException("err.txn.NullPurpose");
			} else {
				if ("99".equals(transferMacau.getPurposeCode())) {
					if (transferMacau.getOtherPurpose() == null
							|| "".equals(transferMacau.getOtherPurpose())) {
						throw new NTBWarningException("err.txn.NullPurpose");
					}
					transferMacau.setPurposeCode("");
				} else {
					transferMacau.setOtherPurpose("");
				}
			}
			
		} else {
			transferMacau.setProofOfPurpose("N");
			transferMacau.setPurposeCode("");
			transferMacau.setOtherPurpose("");
		}*/
		//modified by lzg end
		/* Add by heyongjiang end */

		//add by lzg 20190531
		boolean flag = checkChargeAccount(transferMacau);
		if(!flag){
			throw new NTBWarningException("err.txn.chargeAccountError");
		}
		//add by lzg end
		
		
		//add by lzg 20190619
		checkCutoffTimeAndSetMsg(transferMacau,corpUser.getLangCode());
		//add by lzg end
		
		
		/*Map fromHost = transferService.toHostChargeEnquiryNew(transferMacau
				.getTransId(), corpUser, new BigDecimal(transferMacau
				.getTransferAmount().toString()), "MO", CGD, fromCurrencyCode,
				toCurrencyCode, transferMacau.getChargeBy(), chareCurrencyCode,
				"N");
		transferService.uploadEnquiryMacau(transferMacau, fromHost);

		double chargeAmount = (new Double(fromHost.get("HANDLING_CHRG_AMT")
				.toString())).doubleValue()
				+ (new Double(fromHost.get("COMM_AMT").toString()))
						.doubleValue()
				+ (new Double(fromHost.get("TAX_AMOUNT").toString()))
						.doubleValue()
				+ (new Double(fromHost.get("SWIFT_CHRG_AMT").toString()))
						.doubleValue()
				+ (new Double(fromHost.get("OUR_CHG_AMT").toString()))
						.doubleValue();

		transferMacau.setHandlingAmount(new Double(fromHost.get(
				"HANDLING_CHRG_AMT").toString()));
		transferMacau.setCommissionAmount(new Double(fromHost.get("COMM_AMT")
				.toString()));
		transferMacau.setTaxAmount(new Double(fromHost.get("TAX_AMOUNT")
				.toString()));
		transferMacau.setSwiftAmount(new Double(fromHost.get("SWIFT_CHRG_AMT")
				.toString()));
		transferMacau.setChargeOur(new Double(fromHost.get("OUR_CHG_AMT")
				.toString()));
		transferMacau.setChargeAmount(new Double(chargeAmount));*/

		if (transferMacau.getTransferAmount().doubleValue() == 0) {
			transferMacau.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_FROM);
			BigDecimal transferAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(), new BigDecimal(transferMacau
							.getDebitAmount().toString()), null, 2);

			transferMacau.setTransferAmount(new Double(transferAmount
					.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinAmtOtherBanks(transferMacau.getFromCurrency(), transferMacau.getDebitAmount().doubleValue());
			
			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(), 8);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

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
			transferMacau.setExchangeRate(new Double(exchangeRate.doubleValue()));
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferMacau.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferMacau.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
		} else if (transferMacau.getDebitAmount() == null
				|| transferMacau.getDebitAmount().doubleValue() == 0) {

			transferMacau.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_TO);
			BigDecimal toAmount = new BigDecimal(transferMacau
					.getTransferAmount().toString());
			BigDecimal fromAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(),
					null, toAmount, 2);

			transferMacau.setDebitAmount(new Double(fromAmount.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinAmtOtherBanks(transferMacau.getToCurrency(), transferMacau.getTransferAmount().doubleValue());
			
			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(),

			transferMacau.getFromCurrency(), transferMacau.getToCurrency(), 8);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

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
			transferMacau.setExchangeRate(new Double(exchangeRate.doubleValue()));
			BigDecimal showExchangeRate = new BigDecimal("0");
			if(exchangeRate.compareTo(new BigDecimal("1")) == -1){
				showExchangeRate = sellRate.divide(buyRate, 8,
						BigDecimal.ROUND_DOWN);
				transferMacau.setShowExchangeRate(new Double(showExchangeRate.doubleValue()));
			}else{
				transferMacau.setShowExchangeRate(new Double(exchangeRate.doubleValue()));
			}*/
		}
		
		
		//add by lzg 20190902
		Map Host = transferService.toHostInMacauTrial(transferMacau, corpUser);
		if(!transferMacau.getFromCurrency().equals(transferMacau.getToCurrency())){
			transferMacau.setDebitAmount(((BigDecimal)Host.get("FEE_AMT")).doubleValue());
			transferMacau.setTransferAmount(((BigDecimal)Host.get("CHANGE_AMT")).doubleValue());
			transferMacau.setHandlingAmount(((BigDecimal)Host.get("FEE_AMT")).doubleValue());
		}
		transferMacau.setChargeAmount(((BigDecimal)Host.get("CHG_AMT")).doubleValue());
		transferMacau.setExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		transferMacau.setShowExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		//add by lzg end
		
		String inputAmountFlag = transferMacau.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferMacau.getFromCurrency();
			transferAmount = transferMacau.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferMacau.getToCurrency();
			transferAmount = transferMacau.getTransferAmount();
		}
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(),currency, "MOP",
				new BigDecimal(transferAmount.toString()),
				null, 2);

		if (!corpUser.checkUserLimit(Constants.TXN_TYPE_TRANSFER_MACAU,
				equivalentMOP)) {
			 //write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", transferMacau.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_MACAU);
			reportMap.put("LOCAL_TRANS_CODE", transferMacau.getTransId());
			reportMap.put("FROM_CURRENCY", transferMacau.getFromCurrency());
			reportMap.put("FROM_ACCOUNT", transferMacau.getFromAccount());
			reportMap.put("TO_CURRENCY", transferMacau.getToCurrency());
			reportMap.put("FROM_AMOUNT", transferMacau.getDebitAmount());
			reportMap.put("TO_AMOUNT", transferMacau.getTransferAmount());
			reportMap.put("EXCEEDING_TYPE", "2");
			reportMap.put("LIMIT_TYPE", "");
			reportMap.put("USER_LIMIT ", corpUser.getUserLimit(Constants.TXN_TYPE_TRANSFER_MACAU));
			reportMap.put("DAILY_LIMIT ", new Double(0));
			reportMap.put("TOTAL_AMOUNT ", new Double(0));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
			throw new NTBWarningException("err.txn.ExceedUserLimit");
		}
		if (!transferLimitService.checkCurAmtLimitQuota(transferMacau
				.getFromAccount(), corpUser.getCorpId(), Constants.TXN_TYPE_TRANSFER_MACAU,
				transferMacau.getDebitAmount().doubleValue(),
				equivalentMOP.doubleValue())) {
			 //write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", transferMacau.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_MACAU);
			reportMap.put("LOCAL_TRANS_CODE", transferMacau.getTransId());
			reportMap.put("FROM_CURRENCY", transferMacau.getFromCurrency());
			reportMap.put("FROM_ACCOUNT", transferMacau.getFromAccount());
			reportMap.put("TO_CURRENCY", transferMacau.getToCurrency());
			reportMap.put("FROM_AMOUNT", transferMacau.getDebitAmount());
			reportMap.put("TO_AMOUNT", transferMacau.getTransferAmount());
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
		resultData.put("txnType", Constants.TXN_TYPE_TRANSFER_MACAU);
		
		//add by lzg end
		
		// add by mxl 0821
		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_MACAU);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);

		this.convertPojo2Map(transferMacau, resultData);
		resultData.put("transferDate", transferMacau.getTransferDate());
        
		/* Add by heyongjiang 20100827 */
		String purpose = transferMacau.getOtherPurpose();
		if(purpose != null && !"".equals(purpose)){
			resultData.put("purpose", purpose);
			resultData.put("showPurpose", "true");
		}
		/*
		if (transferMacau.getOtherPurpose() != null
				&& !"".equals(transferMacau.getOtherPurpose())) {
			resultData.put("purpose", transferMacau.getOtherPurpose());
			resultData.put("showPurpose", "true");
		} else {
			if (transferMacau.getPurposeCode() != null
					&& !"".equals(transferMacau.getPurposeCode())) {
				String purposeDescription = transferService
						.getPurposeDescription(transferMacau.getPurposeCode());
				resultData.put("purpose", purposeDescription);
				resultData.put("showPurpose", "true");
			}
		}
		*/
		/* Add by heyongjiang end */
		
		ApproveRuleService approveRuleService = (ApproveRuleService) Config
				.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(), resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}
		
		//by wency 20130118
//		this.checkDuplicatedInput("M", transferMacau);

		this.setResultData(resultData);

		this.setUsrSessionDataValue("transferMacau", transferMacau);
	}

	public void transferTemplateConfirm() throws NTBException {
		// initial service
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		MailService mailService = (MailService) appContext.getBean("MailService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");

		CorpUser corpUser = (CorpUser) this.getUser();		
		TransferMacau transferMacau = (TransferMacau) this.getUsrSessionDataValue("transferMacau");
		String assignedUser = Utils.null2EmptyWithTrim(this.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this.getParameter("mailUser"));
		
		 //闂備浇娉曢崰搴ゃ亹閵娧勫閻熸瑥瀚鍫曟⒑鐠恒劌鏋旈柣鎿冨亝鐎电厧鈻庨幇顒変紦闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷閻庢艾缍婇悰顕�寮撮姀鈩冩闂佽妞块崢濂稿箯娴兼潙妫橀柕鍫濇椤忓爼姊虹捄銊ユ珢闁归顭d by mxl 0922
		String inputAmountFlag = transferMacau.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferMacau.getFromCurrency();
			transferAmount = transferMacau.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferMacau.getToCurrency();
			transferAmount = transferMacau.getTransferAmount();
		}
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), currency, "MOP",
				new BigDecimal(transferAmount.toString()),
				null, 2);
		
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		if (!transferLimitService.checkLimitQuota(transferMacau
				.getFromAccount(), corpUser.getCorpId(), Constants.TXN_TYPE_TRANSFER_MACAU,
				transferMacau.getDebitAmount().doubleValue(),
				equivalentMOP.doubleValue())) {
			 //write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", transferMacau.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_MACAU);
			reportMap.put("LOCAL_TRANS_CODE", transferMacau.getTransId());
			reportMap.put("FROM_CURRENCY", transferMacau.getFromCurrency());
			reportMap.put("FROM_ACCOUNT", transferMacau.getFromAccount());
			reportMap.put("TO_CURRENCY", transferMacau.getToCurrency());
			reportMap.put("FROM_AMOUNT", transferMacau.getDebitAmount());
			reportMap.put("TO_AMOUNT", transferMacau.getTransferAmount());
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
				Constants.TXN_SUBTYPE_TRANSFER_MACAU,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				TransferTemplateInMacauAction.class, transferMacau.getFromCurrency(),
				transferMacau.getDebitAmount().doubleValue(), transferMacau.getToCurrency(),transferMacau.getTransferAmount().doubleValue(),equivalentMOP
						.doubleValue(), transferMacau.getTransId(),
				transferMacau.getRemark(), corpUser, assignedUser, corpUser
						.getCorporation().getAllowExecutor(),inputAmountFlag);
		
		Map resultData = new HashMap();
		String corpType = getParameter("corpType");
		resultData.put("corpType", corpType);
		try {
			transferMacau.setExecuteTime(new Date());
			transferMacau.setRequestTime(new Date());
			transferMacau.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			transferMacau.setStatus(Constants.STATUS_PENDING_APPROVAL);
			transferMacau.setOperation(Constants.OPERATION_NEW);
//			transferMacau.setRecordType(transferMacau.TRANSFER_TYPE_GENERAL);
//			transferService.editMacau(transferMacau, corpUser.getUserId());


			// add by mxl 0821
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");

			resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_MACAU);
			resultData.put("currency", currency);
			resultData.put("amount",transferAmount);
			resultData.put("amountMopEq", equivalentMOP);

			this.convertPojo2Map(transferMacau, resultData);
			resultData.put("transferDate", transferMacau.getTransferDate());
			ApproveRuleService approveRuleService = (ApproveRuleService) Config
					.getAppContext().getBean("ApproveRuleService");
			if (!approveRuleService.checkApproveRule(corpUser.getCorpId(), resultData)) {
				throw new NTBException("err.flow.ApproveRuleNotDefined");
			}
			
			/* Add by heyongjiang 20100827 */
			String purpose = transferMacau.getOtherPurpose();
			if(purpose != null && !"".equals(purpose)){
				resultData.put("purpose", purpose);
				resultData.put("showPurpose", "true");
			}
			/*
			if (transferMacau.getOtherPurpose() != null
					&& !"".equals(transferMacau.getOtherPurpose())) {
				resultData.put("purpose", transferMacau.getOtherPurpose());
				resultData.put("showPurpose", "true");
			} else {
				if (transferMacau.getPurposeCode() != null
						&& !"".equals(transferMacau.getPurposeCode())) {
					String purposeDescription = transferService
							.getPurposeDescription(transferMacau
									.getPurposeCode());
					resultData.put("purpose", purposeDescription);
					resultData.put("showPurpose", "true");
				}
			}
			if(transferMacau.getProofOfPurpose().equals("Y")){
				resultData.put("needProof", "Y");
			}
			*/
			/* Add by heyongjiang end */

			setResultData(resultData);
            String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime",transferMacau.getRequestTime());
			dataMap.put("transId",transferMacau.getTransId());
			dataMap.put("toAmount",transferMacau.getTransferAmount());
			dataMap.put("corpName",corpUser.getCorporation().getCorpName());
			dataMap.put("toAccount",transferMacau.getToAccount());
			dataMap.put("beneficiaryAccName",transferMacau.getBeneficiaryName1());
			dataMap.put("beneficiaryBank",transferMacau.getBeneficiaryBank());
			dataMap.put("transId",transferMacau.getTransId());
			dataMap.put("remark",transferMacau.getRemark());
			
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_TRANSFER_MACAU, userName, dataMap);*/
			//mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_TRANSFER_MACAU, userName ,corpUser.getCorpId(), dataMap);
			

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
			transferService.transferAccMacau(transferMacau);
			try{
				approve(currentFlowProcess.getTxnType(), currentFlowProcess.getTransNo(), this);
			}catch (NTBHostException e) {
				transferService.removeTransfer("transfer_macau", currentFlowProcess.getTransNo());
				throw new NTBHostException(e.getErrorArray());
			}catch (NTBException e) {
				transferService.removeTransfer("transfer_macau", currentFlowProcess.getTransNo());
				throw new NTBException(e.getErrorCode());
			}
		}
		if(!"3".equals(corpType)){
			try{
				transferService.transferAccMacau(transferMacau);
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
		TransferMacau transferMacau = (TransferMacau) this.getUsrSessionDataValue("transferMacau");
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		// 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌ｉ褍浜為柍瑙勭墱閸犲﹥寰勭�ｎ亶浼� ResultData 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌￠崒妯猴拷閻庢艾缍婇弻銊╂偄閾忕懓鎽甸梺瑙勫劤閹冲繒锟借ぐ鎺撶叆闁绘柨鎲￠悘顕�鏌熼崙銈嗗
		setResultData(new HashMap(1));

		// added by Jet for display
		if ((Constants.INPUT_AMOUNT_FLAG_FROM).equals(transferMacau
				.getInputAmountFlag())) {
			transferMacau.setTransferAmount(null);
		} else if ((Constants.INPUT_AMOUNT_FLAG_TO).equals(transferMacau
				.getInputAmountFlag())) {
			transferMacau.setDebitAmount(null);
		}
		
		Map resultData = new HashMap();
		this.convertPojo2Map(transferMacau, resultData);

		Date transferDate = (Date) transferMacau.getTransferDate();
		String transferDateString = DateTime.formatDate(transferDate, defalutPattern);
		resultData.put("transferDateString", transferDateString);
		
		/* Add by heyongkjiang 20100827 */
		String purpose = transferMacau.getOtherPurpose();
		if(purpose != null && !"".equals(purpose)){
			resultData.put("showPurpose", "true");
		}else{
			resultData.put("showPurpose", "false");
		}
		/*
		if (transferMacau.getPurposeCode() != null
				&& !"".equals(transferMacau.getPurposeCode())) {
			resultData.put("showPurpose", "true");
		} else {
			if (transferMacau.getOtherPurpose() != null
					&& !"".equals(transferMacau.getOtherPurpose())) {
				resultData.put("showPurpose", "true");
				resultData.put("isOtherPurpose", "true");
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
		resultData.put("selectFromAcct", transferMacau.getFromAccount() + " - "  + transferMacau.getFromCurrency());//add by linrui for mul-ccy
		setResultData(resultData);
		
		// 闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌ｉ褍浜楃紒槌栧櫍楠炲繘鎮滈懞銉︽闂佸搫鍊堕崐鏍拷瑜版帗鐓ラ柣鏂垮槻瀵ゆ椽寮堕崼銏犱壕閻庢艾缍婇弻銊╂偄閻戞鍩峞ssion闂備浇娉曢崰鎰板几婵犳艾绠柣鎴ｅГ閺呮悂鏌ｉ妸銉︻棯闁哥偛顭烽獮蹇曠矚婵笜firm闂備浇娉曢崰鎰板几婵犳艾绠�瑰嫭婢橀弲鎼佹⒑鐠恒劌鏋戦柡瀣煼楠炲繘鎮滈懞銉︽闂佸搫鍊堕崐鏍拷瑜版帗鍤曟慨妤嬫嫹閺呮悂鏌ㄩ悤鍌涘
		this.setUsrSessionDataValue("transferMacau", transferMacau);
    }
	
	public boolean approve(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpUser corpUser = (CorpUser) bean.getUser();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		if (txnType != null) {
			TransferMacau transferMacau = transferService.viewInMacau(id);
			/*Map exchangeMap = exRatesService.getExchangeRateFromHost(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(), 8);
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
			
			transferMacau
					.setExchangeRate(new Double(exchangeRate.doubleValue()));*/
			// add mxl 0912
			String inputAmountFlag = transferMacau.getInputAmountFlag();
			String currency = null;
			Double transferAmount = new Double(0);
			if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferMacau.getFromCurrency();
				transferAmount = transferMacau.getDebitAmount();
				
				//jet modified for updating new transaction amount
				/*BigDecimal new_To_Amount_temp = exRatesService.getEquivalentFromHostRate(
						corpUser.getCorpId(), currency, transferMacau
								.getToCurrency(), new BigDecimal(transferAmount
								.toString()), null, 2,exchangeMap);
				transferMacau.setTransferAmount(new Double(new_To_Amount_temp.toString()));*/

			} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferMacau.getToCurrency();
				transferAmount = transferMacau.getTransferAmount();
				
				//jet modified for updating new transaction amount
				/*BigDecimal new_From_Amount_temp = exRatesService.getEquivalentFromHostRate(
						corpUser.getCorpId(), transferMacau.getFromCurrency(),
						currency, null, new BigDecimal(transferAmount
								.toString()), 2,exchangeMap);
				transferMacau.setDebitAmount(new Double(new_From_Amount_temp.toString()));*/

			}
			//add by lzg 20190619
			checkCutoffTimeAndSetMsg(transferMacau,corpUser.getLangCode());
			//add by lzg end
			
			//add by lzg 20190902
			Map Host = transferService.toHostInMacauTrial(transferMacau, corpUser);
			if(!transferMacau.getFromCurrency().equals(transferMacau.getToCurrency())){
				transferMacau.setDebitAmount(((BigDecimal)Host.get("FEE_AMT")).doubleValue());
				transferMacau.setTransferAmount(((BigDecimal)Host.get("CHANGE_AMT")).doubleValue());
				transferMacau.setHandlingAmount(((BigDecimal)Host.get("FEE_AMT")).doubleValue());
			}
			transferMacau.setChargeAmount(((BigDecimal)Host.get("CHG_AMT")).doubleValue());
			transferMacau.setExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
			//transferMacau.setShowExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
			//add by lzg end
			
			
			BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
					.getCorpId(), currency, "MOP",
					new BigDecimal(transferAmount.toString()),
					null, 2);
			
			/**
			 * add by long_zg 2014-2-16 for tranfer date doesn't check in transfer template
			 * begin
			 */
			String requestType = transferMacau.getRequestType();
			if("2".equals(requestType)){
                //add by lw 20100927
				// get transfer date
				Date transferDate = transferMacau.getTransferDate();
//				transferDate=DateTime.getDateFromStr(DateTime.formatDate(transferDate, "yyyy-MM-dd HH:mm:ss"));
				// get current date	
				Date currentDate = DateTime.getDateFromStr(DateTime.getCurrentDate());
				Log.info("TransferTemplateInMacauAction.approve() transferDate="+transferDate+" currentDate="+currentDate);
				// transfer date expired
				if(currentDate.after(transferDate)){
					Log.info("TransferTemplateInMacauAction.approve() REJECT: currentDate.after(transferDate)");
					// reject
					transferService.rejectMacau(transferMacau);
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
			//transferMacau.setExchangeRate(new Double(showExchangeRate.doubleValue()));
			//add by lzg end
			
			
			if (!transferLimitService.checkLimitQuota(transferMacau
					.getFromAccount(), corpUser.getCorpId(), txnType,
					transferMacau.getDebitAmount().doubleValue(),
					equivalentMOP.doubleValue())) {
				 //write limit report
	        	Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("USER_ID",corpUser.getUserId());
				reportMap.put("CORP_ID", corpUser.getCorpId());
				reportMap.put("TRANS_TYPE", txnType);
				reportMap.put("LOCAL_TRANS_CODE", transferMacau.getTransId());
				reportMap.put("FROM_ACCOUNT", transferMacau.getFromAccount());
				reportMap.put("FROM_CURRENCY", transferMacau.getFromCurrency());
				reportMap.put("TO_CURRENCY", transferMacau.getToCurrency());
				reportMap.put("FROM_AMOUNT", transferMacau.getDebitAmount());
				reportMap.put("TO_AMOUNT", transferMacau.getTransferAmount());
				reportMap.put("EXCEEDING_TYPE", "1");
				reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
				reportMap.put("USER_LIMIT ", "0");
				reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
				reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
				throw new NTBWarningException("err.txn.ExceedDailyLimit");
			}
			Map fromHost = transferService.toHostInMacau(transferMacau,
					corpUser, txnType);
			Map resultData = bean.getResultData();
			resultData.put("TRANSFER_REFERENCE", fromHost.get("TRANSFER_REFERENCE"));
			
			//add by long_zg 2015-01-28 for CR205 Add IP
			String ipAddress = bean.getRequestIP() ;
			fromHost.put("IP_ADDRESS", ipAddress) ;
			transferMacau.setSerialNumber((String)fromHost.get("TRANSFER_REFERENCE"));
			//transferService.loadUploadMacau(transferMacau, fromHost);
			//add by lzg 20190531
			transferService.updateMacau(transferMacau);
			this.convertPojo2Map(transferMacau, resultData);
			bean.setResultData(resultData);
			//add by lzg end
			//send email to last approver or executor, add by hjs 20071029
			MailService mailService = (MailService) appContext.getBean("MailService");
			Map dataMap = new HashMap();
			this.convertPojo2Map(transferMacau, dataMap);
			if("".equals(dataMap.get("remark"))){
				dataMap.put("remark", "--");
			}
			dataMap.put("transferDate", DateTime.formatDate(transferMacau.getTransferDate(), defalutPattern));
			dataMap.put("lastUpdateTime", new Date());

			/*mailService.toLastApprover_Executor(Constants.TXN_SUBTYPE_TRANSFER_MACAU, corpUser.getUserId(),dataMap);*/
			mailService.toLastApprover_Executor(Constants.TXN_SUBTYPE_TRANSFER_MACAU, corpUser.getUserId(),corpUser.getCorpId(), dataMap);
			
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
			TransferMacau transferMacau = transferService.viewInMacau(id);
			transferService.rejectMacau(transferMacau);

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
		TransferMacau transferMacau = transferService.viewInMacau(id);
		Map resultData = bean.getResultData();
		//modified by lzg 20190612
		//bean.convertPojo2Map(transferMacau, resultData);
		//modified by lzg end
		CorpUser corpUser = (CorpUser) bean.getUser();
		/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
				.getCorpId(), transferMacau.getFromCurrency(),
				transferMacau.getToCurrency(), 8);
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
		
		if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
			exchangeRate = new BigDecimal("0");
		} else {
			exchangeRate = buyRate.divide(sellRate, 8,
					BigDecimal.ROUND_DOWN);
		}*/
		
		
		//add by lzg 20190902
		double FromAmount = 0;
		double ToAmount = 0;
		Map Host = transferService.toHostInMacauTrial(transferMacau, corpUser);
		if(!transferMacau.getFromCurrency().equals(transferMacau.getToCurrency())){
			FromAmount = ((BigDecimal)Host.get("FEE_AMT")).doubleValue();
			ToAmount = ((BigDecimal)Host.get("CHANGE_AMT")).doubleValue();
		}else{
			FromAmount = transferMacau.getDebitAmount();
			ToAmount = transferMacau.getTransferAmount();
		}
		transferMacau.setHandlingAmount(FromAmount);
		transferMacau.setChargeAmount(((BigDecimal)Host.get("CHG_AMT")).doubleValue());
		transferMacau.setExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		transferMacau.setShowExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		
		resultData.put("newFromAmount", FromAmount);
		resultData.put("newToAmount", ToAmount);
		resultData.put("newExchangeRate", transferMacau.getShowExchangeRate());
		//add by lzg end
		
		
		String inputAmountFlag = transferMacau.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferMacau.getFromCurrency();
			transferAmount = FromAmount;
			
			//jet modified for showing new transaction amount
			/*Double new_From_Amount = transferMacau.getDebitAmount();			
			BigDecimal new_To_Amount_temp = exRatesService.getEquivalentFromHostRate(
					corpUser.getCorpId(), currency,
					transferMacau.getToCurrency(), new BigDecimal(transferAmount
							.toString()), null, 2,exchangeMap);
			Double new_To_Amount = new Double(new_To_Amount_temp.toString());
			resultData.put("newFromAmount", new_From_Amount);
			resultData.put("newToAmount", new_To_Amount);*/

		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferMacau.getToCurrency();
			transferAmount = ToAmount;
			
			//jet modified for showing new transaction amount
			/*Double new_To_Amount = transferMacau.getTransferAmount();			
			BigDecimal new_From_Amount_temp = exRatesService.getEquivalentFromHostRate(
					corpUser.getCorpId(), transferMacau.getFromCurrency(),
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

		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);
		//add by lzg 20190604
		if(transferMacau.getExchangeRate() < 1){
			//transferMacau.setExchangeRate(1/transferMacau.getExchangeRate());
			transferMacau.setExchangeRate(transferMacau.getShowExchangeRate());
		}
		//add by lzg 20190612
		bean.convertPojo2Map(transferMacau, resultData);
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
		/*String purpose = transferMacau.getOtherPurpose();
		if(purpose != null && !"".equals(purpose)){
			resultData.put("purpose", purpose);
			resultData.put("showPurpose", "true");
		}*/
		/*
		if (transferMacau.getOtherPurpose() != null
				&& !"".equals(transferMacau.getOtherPurpose())) {
			resultData.put("purpose", transferMacau.getOtherPurpose());
			resultData.put("showPurpose", "true");
		} else {
			if (transferMacau.getPurposeCode() != null
					&& !"".equals(transferMacau.getPurposeCode())) {
				String purposeDescription = transferService
						.getPurposeDescription(transferMacau.getPurposeCode());
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

		return "/WEB-INF/pages/txn/transfer_account/transfer_InMacau_approval_view.jsp";
	}
	
	private void checkCutoffTimeAndSetMsg(TransferMacau transferMacau,String language) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext
				.getBean("CutOffTimeService");

		// check value date cut-off time
		String transferType = "M";
		String message = cutOffTimeService.check("ZJ55", transferMacau
				.getFromCurrency(), transferMacau.getToCurrency(),transferType,language);
		if(message != null){
			throw new NTBWarningException(message);
		}
		/*setMessage(cutOffTimeService.check("ZJ55", transferMacau
				.getFromCurrency(), transferMacau.getToCurrency(),transferType));*/
    }
	
    private void checkDuplicatedInput(String transferType,TransferMacau transferMacau) throws NTBException{
		
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
			
			String clientRef = Utils.null2EmptyWithTrim(transferMacau.getRemark());
			String benef1 = Utils.null2EmptyWithTrim(transferMacau.getBeneficiaryName1());
			String benef2 = Utils.null2EmptyWithTrim(transferMacau.getBeneficiaryName2());
			
			//version2
			if(!"".equals(clientRef)&&(!"".equals(benef1)||!"".equals(benef2))){
				if(transferMacau.getFromAccount().equals(fromAccount)&&
						transferMacau.getToAccount().equals(toAccount)&&
						transferMacau.getTransferAmount().doubleValue()==amount.doubleValue()&&
						clientRef.equals(clientReference)&&
						benef1.equals(beneficiaryName1)&&benef2.equals(beneficiaryName2)){
					fla = true;
					break;
				}
			}
			
			/* version1
			 * if(!"".equals(clientRef)&&(!"".equals(benef1)||!"".equals(benef2))){
				if(transferMacau.getFromAccount().equals(fromAccount)&&
						transferMacau.getToAccount().equals(toAccount)&&
						transferMacau.getTransferAmount().doubleValue()==amount.doubleValue()&&
						clientRef.equals(clientReference)&&
						benef1.equals(beneficiaryName1)&&benef2.equals(beneficiaryName2)){
					fla = true;
					break;
				}
			}else if(!"".equals(clientRef)&&("".equals(benef1)&&"".equals(benef2))){
				if(transferMacau.getFromAccount().equals(fromAccount)&&
						transferMacau.getToAccount().equals(toAccount)&&
						transferMacau.getTransferAmount().doubleValue()==amount.doubleValue()&&
						clientRef.equals(clientReference)){
					fla = true;
					break;
				}
			}else if("".equals(clientRef)&&(!"".equals(benef1)||!"".equals(benef2))){
				if(transferMacau.getFromAccount().equals(fromAccount)&&
						transferMacau.getToAccount().equals(toAccount)&&
						transferMacau.getTransferAmount().doubleValue()==amount.doubleValue()&&
						benef1.equals(beneficiaryName1)&&benef2.equals(beneficiaryName2)){
					fla = true;
					break;
				}
			}else if("".equals(clientRef)&&("".equals(benef1)&&"".equals(benef2))){
				if(transferMacau.getFromAccount().equals(fromAccount)&&
						transferMacau.getToAccount().equals(toAccount)&&
						transferMacau.getTransferAmount().doubleValue()==amount.doubleValue()){
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
