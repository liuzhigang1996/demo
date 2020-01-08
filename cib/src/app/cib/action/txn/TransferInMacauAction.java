package app.cib.action.txn;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Encryption;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.RBFactory;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bnk.BankUser;
import app.cib.bo.flow.FlowProcess;
import app.cib.bo.srv.TxnPrompt;
import app.cib.bo.sys.AbstractCorpUser;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TransferMacau;
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

/**
 * @author mxl 07/20
 * 
 */
public class TransferInMacauAction extends CibAction implements Approvable {
	private String defalutPattern = Config.getProperty("DefaultDatePattern");

	public void addLoad() throws NTBException {
		//add by linrui 20180313
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

	public void add() throws NTBException {
		// initial service
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
		TransferMacau transferMacau = new TransferMacau(CibIdGenerator
				.getRefNoForTransaction());	
		String SELECT_CGDLOCAL = "select CGD_FLAG,BANK_NAME from HS_LOCAL_BANK_CODE where BANK_CODE=? ";
		RcCurrency rcCurrency = new RcCurrency();
		String requestType = this.getParameter("requestType");
		String transferDateString = this.getParameter("transferDateString");
		String CGD = "N";

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

		this.convertMap2Pojo(this.getParameters(), transferMacau);

		transferMacau.setUserId(corpUser.getUserId());
		transferMacau.setCorpId(corpUser.getCorpId());
		// transferMacau.setExecuteTime(new Date());
		// transferMacau.setRequestTime(new Date());

		// chargeAccount,chargeBy (charge account is always same as from account
//		transferMacau.setChargeAccount(transferMacau.getFromAccount());
		//add by linrui for mul-language 20171114
		Double transAmt  = TransAmountFormate.formateAmount(this.getParameter("transferAmount"),lang);
        Double debitAmt = TransAmountFormate.formateAmount(this.getParameter("debitAmount"), lang);
        transferMacau.setTransferAmount(transAmt);
        transferMacau.setDebitAmount(debitAmt);
        //end
		// Jet modified 2007-12-17
		// transferMacau.setChargeBy("O");

		CorpAccount corpAccount = corpAccountService
				.viewCorpAccount(transferMacau.getFromAccount());
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
		
		/* Add by heyongjiang 20100806 */
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
		} else if (checkPurpose == 2) {
			transferMacau.setProofOfPurpose("Y");
			if(transferMacau.getOtherPurpose() == null 
					|| "".equals(transferMacau.getOtherPurpose())){
				throw new NTBWarningException("err.txn.NullPurpose");
			}
		} else if (checkPurpose == 3) {
			transferMacau.setProofOfPurpose("E");
			if(transferMacau.getOtherPurpose() == null 
					|| "".equals(transferMacau.getOtherPurpose())){
				throw new NTBWarningException("err.txn.NullPurpose");
			}
		} else {
			transferMacau.setProofOfPurpose("N");
			transferMacau.setPurposeCode("");
			transferMacau.setOtherPurpose("");
		}*/
		//modified by lzg end
		/* Add by heyongjiang end */
		
		//add by lzg 20190530 Determine whether the chargeAccount has the fromCurrency
		String chargeAccount = transferMacau.getChargeAccount();
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		CibTransClient testClient = new CibTransClient("CIB", "0195");

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
		if(!flag){
			throw new NTBWarningException("err.txn.chargeAccountError");
		}
		//add by lzg end
		

		// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撶粣鏍村灲閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹凤拷edit by mxl 0115
		checkCutoffTimeAndSetMsg(transferMacau,corpUser.getLangCode());
		
		/*fromHost = transferService.toHostChargeEnquiryNew(transferMacau
				.getTransId(), corpUser, new BigDecimal(transferMacau
				.getTransferAmount().toString()), "MO", CGD, transferMacau.getFromCurrency(),	//update by gan 20180131
				transferMacau.getToCurrency(), transferMacau.getChargeBy(), chargeCurrency,		//cbsCode change to ccy
				"N");*/
		
		
		//transferService.uploadEnquiryMacau(transferMacau, fromHost);
		
		/*double chargeToAmount = (new Double(fromHost.get("HANDLING_CHRG_AMT").toString())).doubleValue();
		double chargeFromAmount = (new Double(fromHost.get("HANDLING_FEE_AMT").toString())).doubleValue();

		transferMacau.setHandlingAmount(new Double(chargeFromAmount));
		transferMacau.setChargeAmount(new Double(chargeToAmount));*/

		if (transferMacau.getTransferAmount().doubleValue() == 0) {
			transferMacau.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_FROM);
			BigDecimal transferAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(), new BigDecimal(transferMacau
							.getDebitAmount().toString()), null, 2);

			transferMacau.setTransferAmount(new Double(transferAmount
					.toString()));

			// Jet add, check minimum trans amount
			transAmountService.checkMinAmtOtherBanks(transferMacau
					.getFromCurrency(), transferMacau.getDebitAmount()
					.doubleValue());

			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(), 8);
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
			transferMacau
					.setExchangeRate(new Double(exchangeRate.doubleValue()));
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
					transferMacau.getToCurrency(), null, toAmount, 2);

			transferMacau.setDebitAmount(new Double(fromAmount.toString()));

			transAmountService.checkMinAmtOtherBanks(transferMacau
					.getToCurrency(), transferMacau.getTransferAmount()
					.doubleValue());

			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(),

			transferMacau.getFromCurrency(), transferMacau.getToCurrency(), 8);
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
			transferMacau
					.setExchangeRate(new Double(exchangeRate.doubleValue()));
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
				.getCorpId(), currency, "MOP", new BigDecimal(transferAmount
				.toString()), null, 2);

		if (!corpUser.checkUserLimit(Constants.TXN_TYPE_TRANSFER_MACAU,
				equivalentMOP)) {
			Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
					"yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
					"HHmmss"));
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
			reportMap.put("USER_LIMIT ", corpUser
					.getUserLimit(Constants.TXN_TYPE_TRANSFER_MACAU));
			reportMap.put("DAILY_LIMIT ", new Double(0));
			reportMap.put("TOTAL_AMOUNT ", new Double(0));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
			throw new NTBWarningException("err.txn.ExceedUserLimit");
		}
		if (!transferLimitService.checkCurAmtLimitQuota(transferMacau
				.getFromAccount(), corpUser.getCorpId(),
				Constants.TXN_TYPE_TRANSFER_MACAU, transferMacau
						.getDebitAmount().doubleValue(), equivalentMOP
						.doubleValue())) {
			Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
					"yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
					"HHmmss"));
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
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService
					.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService
					.getTotalLimit()));
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

		String purpose = transferMacau.getOtherPurpose();
		if(purpose != null && !"".equals(purpose)){
			resultData.put("showPurpose", "true");
			resultData.put("purpose", purpose);
		}
		
		ApproveRuleService approveRuleService = (ApproveRuleService) Config
				.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
				resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}
		//add by linrui 20190619 for EB_001_0097
		transferService.toHostInMacauTrial(transferMacau, corpUser);
		//end

		//by wency 20130118
//		this.checkDuplicatedInput("M", transferMacau);
		
		this.setResultData(resultData);

		// 闁跨喐鏋婚幏鐑芥晸閻偂绱幏鐑芥晸閺傘倖瀚归柨鐔峰建鏉堢偓瀚归柨鐔烘爱ession闁跨喐鏋婚幏鐑芥晸閻ㄥ棙鍞婚幏绌媜nfirm闁跨喐鏋婚幏宄板晸闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归懣姗�鏁撻敓锟�
		this.setUsrSessionDataValue("transferMacau", transferMacau);
	}

	public void addConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		// 闁跨喐鏋婚幏鐑芥晸閹活厺绱幏鐑芥晸閺傘倖瀚归柨鐔峰建鏉堢偓瀚归柨鐔告灮閹风兘鏁撻幑宄板皡閹凤拷
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");
		TransferMacau transferMacau = (TransferMacau) this
				.getUsrSessionDataValue("transferMacau");
		CorpUser corpUser = (CorpUser) this.getUser();
		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));

		// 闁跨喎褰ㄧ拋瑙勫闁跨喓鐓导娆愬闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归獮鏇㈡晸閺傘倖瀚瑰☉鎻掔番闁跨喐鏋婚幏锟絘dd by mxl 0922
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
		// check value date cut-off time add by mxl 0130
		//checkCutoffTimeAndSetMsg(transferMacau);
			
		// 闁跨喖鎽弬銈嗗娑擄拷鏁撻弬銈嗗闁跨喐鏋婚幏閿嬫綀闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), currency, "MOP", new BigDecimal(transferAmount
				.toString()), null, 2);
		
		// add by chen_y for CR225 20170412
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		if (!transferLimitService.checkLimitQuota(transferMacau
				.getFromAccount(), corpUser.getCorpId(),
				Constants.TXN_TYPE_TRANSFER_MACAU, transferMacau
						.getDebitAmount().doubleValue(), equivalentMOP
						.doubleValue())) {
			// write limit report
			Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
					"yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
					"HHmmss"));
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
				Constants.TXN_SUBTYPE_TRANSFER_MACAU,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				TransferInMacauAction.class, transferMacau.getFromCurrency(),
				transferMacau.getDebitAmount().doubleValue(), transferMacau
						.getToCurrency(), transferMacau.getTransferAmount()
						.doubleValue(), equivalentMOP.doubleValue(),
				transferMacau.getTransId(), transferMacau.getRemark(),
				corpUser, assignedUser, corpUser.getCorporation()
						.getAllowExecutor(), inputAmountFlag);
		
		Map resultData = new HashMap();
		String corpType = getParameter("corpType");
		resultData.put("corpType", corpType);
		try {
			transferMacau.setExecuteTime(new Date());
			transferMacau.setRequestTime(new Date());
			transferMacau.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			transferMacau.setStatus(Constants.STATUS_PENDING_APPROVAL);
			transferMacau.setOperation(Constants.OPERATION_NEW);


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

			ApproveRuleService approveRuleService = (ApproveRuleService) Config
					.getAppContext().getBean("ApproveRuleService");
			if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
					resultData)) {
				throw new NTBException("err.flow.ApproveRuleNotDefined");
			}

			/* Add by heyongjiang 20100808 */
			String purpose = transferMacau.getOtherPurpose();
			if(purpose != null && !"".equals(purpose)){
				resultData.put("showPurpose", "true");
				resultData.put("purpose", purpose);
			}
			/* Add by heyongjiang end */

			setResultData(resultData);
			resultData.put("transferDate", transferMacau.getTransferDate());

			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔虹哺绾攱瀚� add by mxl 0928
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", transferMacau.getRequestTime());
			dataMap.put("transId", transferMacau.getTransId());
			dataMap.put("toAmount", transferMacau.getTransferAmount());
			dataMap.put("corpName", corpUser.getCorporation().getCorpName());
			dataMap.put("toAccount", transferMacau.getToAccount());
			dataMap.put("beneficiaryAccName", transferMacau
					.getBeneficiaryName1());
			dataMap.put("beneficiaryBank", transferMacau.getBeneficiaryBank());
			dataMap.put("transId", transferMacau.getTransId());
			dataMap.put("remark", transferMacau.getRemark());

			/* Add by long_zg 2015-05-22 UAT6-242 缁楊兛绗侀懓鍛扮秮鐠╃惤perator閹存劕濮涢棆浣哄繁鐏忔唶ave as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_TRANSFER_MACAU, userName, dataMap);*/
			//mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_TRANSFER_MACAU, userName ,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 缁楊兛绗侀懓鍛扮秮鐠╃惤perator閹存劕濮涢棆浣哄繁鐏忔唶ave as template end */
			
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("Error process confirmation of transfering in MACAU", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
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
	
	//add by lzg 20190820
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
	//add by lzg end

	public void addCancel() throws NTBException {
		TransferMacau transferMacau = (TransferMacau) this.getUsrSessionDataValue("transferMacau");
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");

		// 闁跨喐鏋婚幏鐑芥晸閻偆鈹栫喊澶嬪 ResultData 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔虹哺閹惧懏瀚归柨鐔告灮閹凤拷
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
		String transferDateString = DateTime.formatDate(transferDate,
				defalutPattern);
		resultData.put("transferDateString", transferDateString);

		/* Add by heyongkjiang 20100810 */
		String purpose = transferMacau.getOtherPurpose();
		if(purpose != null && !"".equals(purpose)){
			resultData.put("showPurpose", "true");
			resultData.put("purpose", purpose);
		}
		/* Add by heyongjiang end */
		CorpUser corpUser = (CorpUser) this.getUser();
		//modified by lzg 20190708
		//resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("transferuser", transferService.loadAccountForRemittance(corpUser.getCorpId()));
		//modified by lzg end
		resultData.put("selectFromAcct", transferMacau.getFromAccount() + " - "  + transferMacau.getFromCurrency());//add by linrui for mul-ccy
		setResultData(resultData);

		// 闁跨喐鏋婚幏鐑芥晸閻偂绱幏鐑芥晸閺傘倖瀚归柨鐔峰建鏉堢偓瀚归柨鐔烘爱ession闁跨喐鏋婚幏鐑芥晸閻ㄥ棙鍞婚幏绌媜nfirm闁跨喐鏋婚幏宄板晸闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归懣姗�鏁撻敓锟�
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

			if (null == buyRate) {
				buyRate = new BigDecimal("1");
			}
			if (null == sellRate) {
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
					.setExchangeRate(new Double(exchangeRate.doubleValue()));
*/
			// transferService.updateMacau(transferMacau);
			
			
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
			
			
			String inputAmountFlag = transferMacau.getInputAmountFlag();
			String currency = null;
			Double transferAmount = new Double(0);
			if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferMacau.getFromCurrency();
				transferAmount = transferMacau.getDebitAmount();

				/*BigDecimal new_To_Amount_temp = exRatesService.getEquivalentFromHostRate(
						corpUser.getCorpId(), currency, transferMacau
								.getToCurrency(), new BigDecimal(transferAmount
								.toString()), null, 2,exchangeMap);
				transferMacau.setTransferAmount(new Double(new_To_Amount_temp
						.toString()));*/

			} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferMacau.getToCurrency();
				transferAmount = transferMacau.getTransferAmount();

				// jet modified for updating new transaction amount
				/*BigDecimal new_From_Amount_temp = exRatesService.getEquivalentFromHostRate(
						corpUser.getCorpId(), transferMacau.getFromCurrency(),
						currency, null, new BigDecimal(transferAmount
								.toString()), 2,exchangeMap);
				transferMacau.setDebitAmount(new Double(new_From_Amount_temp
						.toString()));*/

			}
			
			String languaage = corpUser.getLangCode();
			//add by lzg 20190619
			checkCutoffTimeAndSetMsg(transferMacau,languaage);
			//add by lzg end
			
			
			
			BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
					.getCorpId(), currency, "MOP", new BigDecimal(
					transferAmount.toString()), null, 2);
			
			String requestType = transferMacau.getRequestType();
			if("2".equals(requestType)){
                //add by lw 20100927
				// get transfer date
				Date transferDate = transferMacau.getTransferDate();
//				transferDate=DateTime.getDateFromStr(DateTime.formatDate(transferDate, "yyyy-MM-dd HH:mm:ss"));
				// get current date	
				Date currentDate = DateTime.getDateFromStr(DateTime.getCurrentDate());
				Log.info("TransferInMacauAction.approve() transferDate="+transferDate+" currentDate="+currentDate);
				// transfer date expired
				if(currentDate.after(transferDate)){
					Log.info("TransferInMacauAction.approve() REJECT: currentDate.after(transferDate)");
					// reject
					transferService.rejectMacau(transferMacau);
					// show error message
					throw new NTBWarningException("err.txn.TransferDateExpired");
				}						
				// add by lw end
			}
			
			//add by lzg 20190604
			//transferMacau.setExchangeRate(new Double(showExchangeRate.doubleValue()));
			//add by lzg end
			
			// check daily limit
			if (!transferLimitService.checkLimitQuota(transferMacau
					.getFromAccount(), corpUser.getCorpId(), txnType,
					transferMacau.getDebitAmount().doubleValue(), equivalentMOP
							.doubleValue())) {
				// write limit report
				Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
						"yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
						"HHmmss"));
				reportMap.put("USER_ID", transferMacau.getUserId());
				reportMap.put("CORP_ID", corpUser.getCorpId());
				reportMap.put("TRANS_TYPE", txnType);
				reportMap.put("LOCAL_TRANS_CODE", transferMacau.getTransId());
				reportMap.put("FROM_ACCOUNT", transferMacau.getFromAccount());
				reportMap.put("FROM_CURRENCY", transferMacau.getFromCurrency());
				reportMap.put("TO_CURRENCY", transferMacau.getToCurrency());
				reportMap.put("FROM_AMOUNT", transferMacau.getDebitAmount());
				reportMap.put("TO_AMOUNT", transferMacau.getTransferAmount());
				reportMap.put("EXCEEDING_TYPE", "1");
				reportMap
						.put("LIMIT_TYPE", transferLimitService.getLimitType());
				reportMap.put("USER_LIMIT ", "0");
				reportMap.put("DAILY_LIMIT ", new Double(transferLimitService
						.getDailyLimit()));
				reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService
						.getTotalLimit()));
				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
				throw new NTBWarningException("err.txn.ExceedDailyLimit");
			}
			Map fromHost = transferService.toHostInMacau(transferMacau,
					corpUser, txnType);

			// add by mxl 0930 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔峰建閸戙倖瀚归柨鐔告灮閹风ǖRANSFER_REFERENCE
			Map resultData = bean.getResultData();
			resultData.put("TRANSFER_REFERENCE", fromHost
					.get("TRANSFER_REFERENCE"));

			//add by long_zg 2015-01-28 for CR205 Add IP
			String ipAddress = bean.getRequestIP() ;
			fromHost.put("IP_ADDRESS", ipAddress) ;
			
			//transferService.loadUploadMacau(transferMacau, fromHost);
			//add by lzg 20190613
			transferMacau.setSerialNumber((String)fromHost.get("TRANSFER_REFERENCE"));
			//add by lzg end
			//add by lzg 20190531
			transferService.updateMacau(transferMacau);
			this.convertPojo2Map(transferMacau, resultData);
			bean.setResultData(resultData);
			//add by lzg end
			// send email to last approver or executor, add by hjs 20071029
			MailService mailService = (MailService) appContext
					.getBean("MailService");
			Map dataMap = new HashMap();
			this.convertPojo2Map(transferMacau, dataMap);
			if ("".equals(dataMap.get("remark"))) {
				dataMap.put("remark", "--");
			}
			dataMap.put("transferDate", DateTime.formatDate(transferMacau
					.getTransferDate(), defalutPattern));
			dataMap.put("lastUpdateTime", new Date());
			
			/* Modify by long_zg 2015-05-22 UAT6-242 缁楊兛绗侀懓鍛扮秮鐠╃惤perator閹存劕濮涢棆浣哄繁鐏忔唶ave as template begin */
			/*mailService.toLastApprover_Executor(Constants.TXN_SUBTYPE_TRANSFER_MACAU, corpUser.getUserId(),dataMap);*/
			mailService.toLastApprover_Executor(Constants.TXN_SUBTYPE_TRANSFER_MACAU, corpUser.getUserId(),corpUser.getCorpId(), dataMap);
			/* Modify by long_zg 2015-05-22 UAT6-242 缁楊兛绗侀懓鍛扮秮鐠╃惤perator閹存劕濮涢棆浣哄繁鐏忔唶ave as template begin */
			
			
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
		CorpUser corpUser = (CorpUser) bean.getUser();
		
		
		/*Map exchangeMap = exRatesService.getExchangeRateFromHost(corpUser.getCorpId(),
				transferMacau.getFromCurrency(), transferMacau.getToCurrency(),
				8);
		String rateType = (String) exchangeMap.get("rateType");
		BigDecimal exchangeRate = new BigDecimal("0");
		BigDecimal showExchangeRate = new BigDecimal("0");
		BigDecimal buyRate = (BigDecimal) exchangeMap
		.get(ExchangeRatesDao.BUY_RATE);
		BigDecimal sellRate = (BigDecimal) exchangeMap
		.get(ExchangeRatesDao.SELL_RATE);

		if (null == buyRate) {
			buyRate = new BigDecimal("1");
		}
		if (null == sellRate) {
			sellRate = new BigDecimal("1");
		}
		
		if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
			exchangeRate = new BigDecimal("0");
		} else {
			exchangeRate = buyRate
					.divide(sellRate, 8, BigDecimal.ROUND_DOWN);
		}*/

		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");
		
		
		//add by lzg 20190902
		Double FromAmount = 0.0;
		Double ToAmount = 0.0;
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
		//transferMacau.setExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		transferMacau.setShowExchangeRate(((BigDecimal)Host.get("CUS_RATE")).doubleValue());
		
		resultData.put("newFromAmount", FromAmount);
		resultData.put("newToAmount",ToAmount);
		resultData.put("newExchangeRate", transferMacau.getShowExchangeRate());
		
		//add by lzg end
		
		String inputAmountFlag = transferMacau.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferMacau.getFromCurrency();
			transferAmount = FromAmount;

			/*Double new_From_Amount = transferMacau.getDebitAmount();
			BigDecimal new_To_Amount_temp = exRatesService.getEquivalentFromHostRate(
					corpUser.getCorpId(), currency, transferMacau
							.getToCurrency(), new BigDecimal(transferAmount
							.toString()), null, 2,exchangeMap);
			Double new_To_Amount = new Double(new_To_Amount_temp.toString());
			resultData.put("newFromAmount", new_From_Amount);
			resultData.put("newToAmount", new_To_Amount);*/
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferMacau.getToCurrency();
			transferAmount = ToAmount;

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
				.getCorpId(), currency, "MOP", new BigDecimal(transferAmount
				.toString()), null, 2);

		resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);
		//add by lzg 20190604
		/*if(transferMacau.getExchangeRate() < 1){
			transferMacau.setExchangeRate(transferMacau.getShowExchangeRate());
		}*/
		//add by lzg 20190612
		bean.convertPojo2Map(transferMacau, resultData);
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
		//add by lzg end

		/* update by lw 20110101 */
		//modified by lzg 20190602
		/*String purpose = transferMacau.getOtherPurpose();
		if(purpose != null && !"".equals(purpose)){
			resultData.put("showPurpose", "true");
			resultData.put("purpose", purpose);
		}
		if(transferMacau.getProofOfPurpose().equals("Y")){
			resultData.put("needProof", "Y");
		}*/
		//modified by lzg end
		/* update by lw end */

		bean.setResultData(resultData);
		return "/WEB-INF/pages/txn/transfer_account/transfer_InMacau_approval_view.jsp";

	}

	public void saveTemplate() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		String transId = Utils.null2EmptyWithTrim(this.getParameter("transId"));

		TransferMacau transferMacau = transferService.viewInMacau(transId);
		// add by mxl 0913
		transferMacau.setTransId(CibIdGenerator.getRefNoForTransaction());
		transferMacau.setRecordType(TransferMacau.TRANSFER_TYPE_TEMPLATE);

		transferService.templateInMacau(transferMacau);
	}

	private void checkCutoffTimeAndSetMsg(TransferMacau transferMacau,String language)
			throws NTBException {
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
