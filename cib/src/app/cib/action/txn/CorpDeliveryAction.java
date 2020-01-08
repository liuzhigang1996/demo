package app.cib.action.txn;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.exception.NTBWarningException;

import app.cib.bo.bnk.Corporation;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TransferBank;
import app.cib.core.*;
import app.cib.dao.enq.ExchangeRatesDao;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.sys.ApproveRuleService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.CutOffTimeService;
import app.cib.service.sys.MailService;
import app.cib.service.sys.TransAmountService;
import app.cib.service.txn.CorpTransferService;
import app.cib.service.txn.TransferLimitService;
import app.cib.service.txn.TransferService;
import app.cib.util.Constants;
import app.cib.util.TransAmountFormate;
import app.cib.util.UploadReporter;

/**
 * @author mxl 08/02
 *
 */

public class CorpDeliveryAction extends CibAction implements Approvable {
	public void addLoad() throws NTBException {
		// ���ÿյ� ResultData �����ʾ���
		CorpUser userObj = (CorpUser) this.getUser();
		Corporation corpObj = userObj.getCorporation();
		String fromCorporation = corpObj.getCorpName();

		Map resultData = new HashMap();
		this.setUsrSessionDataValue("fromCorporation", fromCorporation);
		resultData.put("fromCorporation", fromCorporation);
		//add by long_zg 2014-10-13 for BOB_security_update_20140812
		resultData.put("corpId", userObj.getCorpId()) ;
		this.setResultData(resultData);

	}

	public void add() throws NTBException {
		//initial service
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		CorpTransferService corpTransferService = (CorpTransferService) appContext
				.getBean("CorpTransferService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		TransAmountService transAmountService = (TransAmountService) appContext
				.getBean("TransAmountService");


		CorpUser userObj = (CorpUser) this.getUser();
		TransferBank transferBank = new TransferBank(CibIdGenerator
				.getRefNoForTransaction());
		this.convertMap2Pojo(this.getParameters(), transferBank);
		transferBank.setUserId(userObj.getUserId());
		transferBank.setCorpId(userObj.getCorpId());
		
		//*********
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");
		Double transAmt  = TransAmountFormate.formateAmount(this.getParameter("transferAmount"),lang);
        Double debitAmt = TransAmountFormate.formateAmount(this.getParameter("debitAmount"), lang);
        transferBank.setTransferAmount(transAmt);
        transferBank.setDebitAmount(debitAmt);

//		transferBank.setExecuteTime(new Date());
//		transferBank.setRequestTime(new Date());
		// add by mxl 0826
		transferBank.setFromCorporation(userObj.getCorpId());

		// from account
		CorpAccount corpAccount = corpAccountService
				.viewCorpAccount(transferBank.getFromAccount());
		transferBank.setFromCurrency(corpAccount.getCurrency());
		transferBank.setFromAcctype(corpAccount.getAccountType());

		// to account
		corpAccount = corpAccountService.viewCorpAccount(transferBank
				.getToAccount());
		transferBank.setToCurrency(corpAccount.getCurrency());
		transferBank.setToAcctype(corpAccount.getAccountType());

		CorpUser corpUser = (CorpUser) getUser();

		if (transferBank.getTransferAmount().doubleValue() == 0) {
			
			transferBank.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_FROM);
			transferBank.setFlag("1");
			BigDecimal transferAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), new BigDecimal(transferBank
					.getDebitAmount().toString()), null, 2);

			transferBank.setTransferAmount(new Double(transferAmount.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinTransAmt(transferBank.getFromCurrency(), transferBank.getDebitAmount().doubleValue());

			// add by mxl 0906 get the exchange rate
			Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), 7);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

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
				exchangeRate = buyRate.divide(sellRate, 7,
						BigDecimal.ROUND_HALF_UP);
			}
			
			transferBank.setExchangeRate(new Double(exchangeRate.doubleValue()));
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
			
			// add by mxl 0906 get the exchange rate
			Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferBank.getToCurrency(), transferBank
					.getFromCurrency(), 7);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

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
				exchangeRate = buyRate.divide(sellRate, 7,
						BigDecimal.ROUND_HALF_UP);
			}
			
			transferBank.setExchangeRate(new Double(exchangeRate.doubleValue()));
		}
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

        // check user limit
		if (!corpUser.checkUserLimit(Constants.TXN_TYPE_TRANSFER_CORP,equivalentMOP)) {
            //write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", transferBank.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_CORP);
			reportMap.put("LOCAL_TRANS_CODE", transferBank.getTransId());
			reportMap.put("FROM_CURRENCY", transferBank.getFromCurrency());
			reportMap.put("TO_CURRENCY", transferBank.getToCurrency());
			reportMap.put("FROM_AMOUNT", transferBank.getDebitAmount());
			reportMap.put("TO_AMOUNT", transferBank.getTransferAmount());
			reportMap.put("EXCEEDING_TYPE", "2");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
			throw new NTBWarningException("err.txn.ExceedUserLimit");
		}
		 // add by mxl0110  corpId
		 String corpId = corpTransferService.getCorpIdByAccount(transferBank.getFromAccount());
		 
		//modify by long_zg 2015-04-24 for prd BOB transfer exceed limit begin
		/*if (!transferLimitService.checkLimitQuota(transferBank
				.getFromAccount(), corpId, Constants.TXN_TYPE_TRANSFER_BANK,
				transferBank.getDebitAmount().doubleValue(),
				equivalentMOP.doubleValue())) {*/
		 
		 if (!transferLimitService.checkCurAmtLimitQuota(transferBank
					.getFromAccount(), corpId, Constants.TXN_TYPE_TRANSFER_CORP,
					transferBank.getDebitAmount().doubleValue(),
					equivalentMOP.doubleValue())) {
		//modify by long_zg 2015-04-24 for prd BOB transfer exceed limit end 
			 
			//write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", transferBank.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			//modify by long_zg 2015-04-24 for prd BOB transfer exceed limit begin
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_CORP);
			//modify by long_zg 2015-04-24 for prd BOB transfer exceed limit end
			reportMap.put("LOCAL_TRANS_CODE", transferBank.getTransId());
			reportMap.put("FROM_CURRENCY", transferBank.getFromCurrency());
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

		Map resultData = new HashMap();
		this.setResultData(resultData);
		// edit by mxl 0821
		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_DELIVERY);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);

		this.convertPojo2Map(transferBank, resultData);

		ApproveRuleService approveRuleService = (ApproveRuleService) Config
				.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(), resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}

		// ���û����д��session���Ա�confirm��д����ݿ�
		this.setUsrSessionDataValue("transferBank", transferBank);
	}

	public void addConfirm() throws NTBException {
		//initial service
		ApplicationContext appContext = Config.getAppContext();
		CorpTransferService corpTransferService = (CorpTransferService) appContext
				.getBean("CorpTransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");

		CorpUser corpUser = (CorpUser) getUser();
		TransferBank transferBank = (TransferBank) this
				.getUsrSessionDataValue("transferBank");
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));
		
         //�ж��û�����Ļ��Һͽ�� add by mxl 0922
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
         //edit by mxl 0109 
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), currency, "MOP",
				new BigDecimal(transferAmount.toString()),
				null, 2);

		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));

        // check value date cut-off time add by mxl 1123
		checkCutoffTimeAndSetMsg(transferBank);
		// add by chen_y for CR225 20170412
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		if (!transferLimitService.checkLimitQuota(transferBank
				.getFromAccount(), corpUser.getCorpId(), Constants.TXN_TYPE_TRANSFER_CORP,
				transferBank.getDebitAmount().doubleValue(),
				equivalentMOP.doubleValue())) {
			//modify by long_zg 2015-04-24 for prd BOB transfer exceed limit end 
			 
			//write limit report
	    	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", transferBank.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			//modify by long_zg 2015-04-24 for prd BOB transfer exceed limit begin
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_CORP);
			//modify by long_zg 2015-04-24 for prd BOB transfer exceed limit end
			reportMap.put("LOCAL_TRANS_CODE", transferBank.getTransId());
			reportMap.put("FROM_CURRENCY", transferBank.getFromCurrency());
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
		// �½�һ����Ȩ����
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_TRANSFER_DELIVERY,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				CorpDeliveryAction.class, transferBank.getFromCurrency(),
				transferBank.getDebitAmount().doubleValue(),  transferBank.getToCurrency(),transferBank.getTransferAmount().doubleValue(),equivalentMOP
				.doubleValue(), transferBank.getTransId(),transferBank
						.getRemark(), getUser(), assignedUser, corpUser
						.getCorporation().getAllowExecutor(),inputAmountFlag);

		try {
			// ���һ����д����ݿ�
			transferBank.setExecuteTime(new Date());
			transferBank.setRequestTime(new Date());
			transferBank.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			transferBank.setStatus(Constants.STATUS_PENDING_APPROVAL);
			transferBank.setOperation(Constants.OPERATION_NEW);
			transferBank.setRecordType(TransferBank.TRANSFER_TYPE_CORPDELIVERY);
			corpTransferService.transferDelivery(transferBank);

			// for display
			Map resultData = new HashMap();

			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");

			resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_DELIVERY);
			resultData.put("currency",currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);

			this.convertPojo2Map(transferBank, resultData);

			ApproveRuleService approveRuleService = (ApproveRuleService) Config
					.getAppContext().getBean("ApproveRuleService");
			if (!approveRuleService.checkApproveRule(corpUser.getCorpId(), resultData)) {
				throw new NTBException("err.flow.ApproveRuleNotDefined");
			}

			setResultData(resultData);

			// �����ʼ� add by mxl 0928
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
			
			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_TRANSFER_DELIVERY, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_TRANSFER_DELIVERY, userName ,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */
			
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			throw new NTBException("err.txn.TranscationFaily");
		}
	}

	public void addCancel() throws NTBException {
		TransferBank transferBank = (TransferBank) this.getUsrSessionDataValue("transferBank");
		
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
		
//		String toAccount2 = null;
//		toAccount2 = transferBank.getToAccount();
//    	resultData.put("toAccount2", toAccount2);
		setResultData(resultData);
		
		this.setUsrSessionDataValue("transferBank", transferBank);
	}

	
	public String viewDetail(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpTransferService corpTransferService = (CorpTransferService) appContext
				.getBean("CorpTransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		TransferBank transferBank = corpTransferService.viewInBANK(id);
		CorpUser corpUser = (CorpUser) bean.getUser();

        // add by mxl �������»��� 0921
		Map exchangeMap = exRatesService.getExchangeRate(corpUser
				.getCorpId(), transferBank.getFromCurrency(), transferBank
				.getToCurrency(), 7);
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
			exchangeRate = buyRate.divide(sellRate, 7,
					BigDecimal.ROUND_HALF_UP);
		}
		
		// for display
		Map resultData = bean.getResultData();
		convertPojo2Map(transferBank, resultData);

		String inputAmountFlag = transferBank.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferBank.getFromCurrency();
			transferAmount = transferBank.getDebitAmount();
			
			//jet modified for showing new transaction amount
			Double new_From_Amount = transferBank.getDebitAmount();			
			BigDecimal new_To_Amount_temp = exRatesService.getEquivalent(
					corpUser.getCorpId(), currency,
					transferBank.getToCurrency(), new BigDecimal(transferAmount
							.toString()), null, 2);
			Double new_To_Amount = new Double(new_To_Amount_temp.toString());
			resultData.put("newFromAmount", new_From_Amount);
			resultData.put("newToAmount", new_To_Amount);

		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferBank.getToCurrency();
			transferAmount = transferBank.getTransferAmount();
			
			//jet modified for showing new transaction amount
			Double new_To_Amount = transferBank.getTransferAmount();			
			BigDecimal new_From_Amount_temp = exRatesService.getEquivalent(
					corpUser.getCorpId(), transferBank.getFromCurrency(),
					currency, null, new BigDecimal(transferAmount.toString()),
					2);		
			Double new_From_Amount = new Double(new_From_Amount_temp.toString());
			resultData.put("newFromAmount", new_From_Amount);
			resultData.put("newToAmount", new_To_Amount);
		}
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), currency, "MOP",
				new BigDecimal(transferAmount.toString()),
				null, 2);

		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_DELIVERY);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);
		resultData.put("newExchangeRate", exchangeRate);
		bean.setResultData(resultData);
		return "/WEB-INF/pages/txn/corp_transfer/corp_delivery_approval_view.jsp";

	}

	public boolean approve(String txnType, String id, CibAction bean)
			throws NTBException {

		ApplicationContext appContext = Config.getAppContext();
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpTransferService corpTransferService = (CorpTransferService) appContext
				.getBean("CorpTransferService");
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		CorpUser corpUser = (CorpUser) bean.getUser();
		if (txnType != null) {
			TransferBank transferBank = corpTransferService.viewInBANK(id);
            //add by mxl �������»��� 0921
			Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), 7);
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
				exchangeRate = buyRate.divide(sellRate, 7,
						BigDecimal.ROUND_HALF_UP);
			}
			
			transferBank.setExchangeRate(new Double(exchangeRate.doubleValue()));
//			transferService.updateBANK(transferBank);

			//  add by mxl 0922
			String inputAmountFlag = transferBank.getInputAmountFlag();
			String currency = null;
			Double transferAmount = new Double(0);
			if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferBank.getFromCurrency();
				transferAmount = transferBank.getDebitAmount();
				
				//jet modified for updating new transaction amount
				BigDecimal new_To_Amount_temp = exRatesService.getEquivalent(
						corpUser.getCorpId(), currency, transferBank
								.getToCurrency(), new BigDecimal(transferAmount
								.toString()), null, 2);
				transferBank.setTransferAmount(new Double(new_To_Amount_temp.toString()));

			} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferBank.getToCurrency();
				transferAmount = transferBank.getTransferAmount();
				
				//jet modified for updating new transaction amount
				BigDecimal new_From_Amount_temp = exRatesService.getEquivalent(
						corpUser.getCorpId(), transferBank.getFromCurrency(),
						currency, null, new BigDecimal(transferAmount
								.toString()), 2);
				transferBank.setDebitAmount(new Double(new_From_Amount_temp.toString()));

			}
			BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
					.getCorpId(), currency, "MOP",
					new BigDecimal(transferAmount.toString()),
					null, 2);
			 String corpId = corpTransferService.getCorpIdByAccount(transferBank.getFromAccount());
			//modify by long_zg 2015-04-24 for prd BOB transfer exceed limit begin
			/*if (!transferLimitService.checkLimitQuota(transferBank
					.getFromAccount(), corpId, Constants.TXN_TYPE_TRANSFER_BANK,
					transferBank.getDebitAmount().doubleValue(),
					equivalentMOP.doubleValue())) {*/
			 if (!transferLimitService.checkLimitQuota(transferBank
						.getFromAccount(), corpId, Constants.TXN_TYPE_TRANSFER_CORP,
						transferBank.getDebitAmount().doubleValue(),
						equivalentMOP.doubleValue())) {
			//modify by long_zg 2015-04-24 for prd BOB transfer exceed limit end
				//write limit report
	        	Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("USER_ID", transferBank.getUserId());
				reportMap.put("CORP_ID", corpUser.getCorpId());
				//modify by long_zg 2015-04-24 for prd BOB transfer exceed limit begin
				reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_CORP);
				//modify by long_zg 2015-04-24 for prd BOB transfer exceed limit end
				reportMap.put("LOCAL_TRANS_CODE", transferBank.getTransId());
				reportMap.put("FROM_CURRENCY", transferBank.getFromCurrency());
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

			Map fromHost = transferService.toHostInBANK(
					transferBank, corpUser, txnType);

			//add by long_zg 2015-01-28 for CR205 Add IP
			String ipAddress = bean.getRequestIP() ;
			fromHost.put("IP_ADDRESS", ipAddress) ;
			
			transferService.loadUploadBANK(transferBank, fromHost);
			return true;
		} else {
			return false;
		}
	}

	public boolean reject(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpTransferService corpTransferService = (CorpTransferService) appContext.getBean("CorpTransferService");
		if (txnType != null) {
			TransferBank transferBank = corpTransferService.viewInBANK(id);
			corpTransferService.rejectBank(transferBank);
			return true;
		} else {
			return false;
		}

	}

	public boolean cancel(String txnType, String id, CibAction bean)
			throws NTBException {
		return reject(txnType, id, bean);
	}

	private void checkCutoffTimeAndSetMsg(TransferBank transferBank) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext.getBean("CutOffTimeService");

		String fromAcctypeCode = null;
		String toAcctypeCode = null;
		if (transferBank.getFromAcctype().equals(CorpAccount.ACCOUNT_TYPE_CURRENT) || transferBank.getFromAcctype().equals(CorpAccount.ACCOUNT_TYPE_OVER_DRAFT))
		{
			 fromAcctypeCode = "C";
		} else if (transferBank.getFromAcctype().equals(CorpAccount.ACCOUNT_TYPE_SAVING)) {
			 fromAcctypeCode = "S";
		}
		if (transferBank.getToAcctype().equals(CorpAccount.ACCOUNT_TYPE_CURRENT) || transferBank.getToAcctype().equals(CorpAccount.ACCOUNT_TYPE_OVER_DRAFT))
		{
			 toAcctypeCode = "C";
		} else if (transferBank.getToAcctype().equals(CorpAccount.ACCOUNT_TYPE_SAVING)) {
			 toAcctypeCode = "S";
		}
		String txnCode = "51" + fromAcctypeCode + toAcctypeCode;
		setMessage(cutOffTimeService.check(txnCode,transferBank.getFromCurrency(),transferBank.getToCurrency()));
    }
}
