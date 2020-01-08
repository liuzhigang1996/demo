package app.cib.test.txn;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.*;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

import app.cib.action.txn.TransferInBankAction;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TransferBank;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.sys.ApproveRuleService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.CorpUserService;
import app.cib.service.sys.CutOffTimeService;
import app.cib.service.sys.MailService;
import app.cib.service.txn.TransferLimitService;
import app.cib.service.txn.TransferService;
import app.cib.util.Constants;
import app.cib.util.UploadReporter;
import app.cib.core.CibIdGenerator;
import app.cib.dao.enq.ExchangeRatesDao;
import app.cib.util.RcCurrency;
import com.neturbo.set.exception.NTBWarningException;

/**
 * @author mxl 07/20
 *
 */
public class TransferInBankTestAction extends CibAction implements Approvable {
    protected void processNotLogin() throws NTBException {
    }

	public void addLoad() throws NTBException {
		Log.info("[TransferInBankTest]Start test");
		ApplicationContext appContext = Config.getAppContext();
		CorpUserService userService = (CorpUserService)appContext.getBean("corpUserService");

		//modify the user id below before test
		String userId = "corpopr1";
		Log.info("load a user: " + userId);
		CorpUser user = userService.load(userId);
		this.setUser(user);

		// ���ÿյ� ResultData �����ʾ���
		setResultData(new HashMap(1));
	}

	public void add() throws NTBException {
		// ��ʼ��Service
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext
		.getBean("TransferLimitService");
        
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		// ��ʼ�� POJO
		CorpUser corpUser = (CorpUser) this.getUser();
		corpUser.setLanguage(lang);//add by linrui for mul-language
                String transId = CibIdGenerator.getRefNoForTransaction();
		TransferBank transferBank = new TransferBank(transId);
		this.convertMap2Pojo(this.getParameters(), transferBank);
		transferBank.setUserId(corpUser.getUserId());
		transferBank.setCorpId(corpUser.getCorpId());

		// ��� From Currency �ı��ֺ�����
		CorpAccount corpAccount = corpAccountService
				.viewCorpAccount(transferBank.getFromAccount());
		transferBank.setFromCurrency(corpAccount.getCurrency());
		transferBank.setFromAcctype(corpAccount.getAccountType());

		// ��� To Currency �ı��ֺ�����
		String toAccount = this.getParameter("toAccount");
		//other account
		String toAccount2 = this.getParameter("toAccount2");
		if (toAccount.equals("0")) {
             //���ж�own account�ı�־ add mxl 0918
			transferBank.setOwnerAccFlag("N");
			transferBank.setToAccount(toAccount2);
			/*
			 * get the currency of the toAccount and accType from the fromHost
			 * executing the accountEnquriy 0819 add by mxl
			 */
			Map fromHost = transferService.toHostAccountInBANK(transferBank
					.getTransId(), corpUser, transferBank.getToAccount());
			String currencyCode = (String) fromHost.get("CURRENCY_CODE");
			String toAccType = (String) fromHost.get("APPLICATION_CODE");
			if (toAccType.equals("20")) {
				transferBank.setToAcctype("1");
			} else if (toAccType.equals("26")) {
				transferBank.setToAcctype("5");
			} else if (toAccType.equals("21")) {
				transferBank.setToAcctype("3");
			}
			RcCurrency rcCurrency = new RcCurrency();
			transferBank.setToCurrency(rcCurrency
					.getCcyByCbsNumber(currencyCode));

		} else {
			//���ж�own account�ı�־ add mxl 0918
			transferBank.setOwnerAccFlag("Y");
			transferBank.setToAccount(toAccount);
			corpAccount = corpAccountService.viewCorpAccount(transferBank
					.getToAccount());
			transferBank.setToCurrency(corpAccount.getCurrency());
			transferBank.setToAcctype(corpAccount.getAccountType());

		}

		// ���� Transfer Amount �� Debit Amount
		if (transferBank.getTransferAmount().doubleValue() == 0) {
			// ��������ת�ʽ��ı�־ 0923
			transferBank.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_FROM);
			BigDecimal transferAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), new BigDecimal(transferBank
					.getDebitAmount().toString()), null, 2);
			transferBank.setFlag("1");

			transferBank.setTransferAmount(new Double(transferAmount.toString()));
			// add by mxl 0907 get the exchange rate
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

//			if (rateType.equals("0")) {
//				exchangeRate = new BigDecimal("0");
//			} else if (rateType.equals("1")) {
//				exchangeRate = buyRate;
//			} else if (rateType.equals("2")) {
//				exchangeRate = sellRate;
//			} else if (rateType.equals("3")) {
//				exchangeRate = buyRate.divide(sellRate, 7,
//						BigDecimal.ROUND_HALF_UP);
//			}
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
			// add by mxl 0907 get the exchange rate
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

//			if (rateType.equals("0")) {
//				exchangeRate = new BigDecimal("0");
//			} else if (rateType.equals("1")) {
//				exchangeRate = buyRate;
//			} else if (rateType.equals("2")) {
//				exchangeRate = sellRate;
//			} else if (rateType.equals("3")) {
//				exchangeRate = buyRate.divide(sellRate, 7,
//						BigDecimal.ROUND_HALF_UP);
//			}
			transferBank.setExchangeRate(new Double(exchangeRate.doubleValue()));

		}
		// ��ʾ���
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferBank.getToCurrency(), "MOP",
				new BigDecimal(transferBank.getTransferAmount().toString()),
				null, 2);
		// ΪSelectUser Tag׼�����
		Map resultData = new HashMap();
		this.convertPojo2Map(transferBank, resultData);
		this.setResultData(resultData);

		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

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
		resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);

		ApproveRuleService approveRuleService = (ApproveRuleService) Config
				.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(), resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}

		// ���From Account = To Account, ���
		if (transferBank.getFromAccount().equals(transferBank.getToAccount())) {
			throw new NTBException("err.txn.TransferAccError");
		}

		// ��� Transfer Amount �Ƿ񳬹� User Limit
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
				reportMap.put("EXCEEDING_TYPE", "0");
				reportMap.put("LIMIT_TYPE", "");
				reportMap.put("USER_LIMIT ", corpUser.getUserLimit(Constants.TXN_TYPE_TRANSFER_BANK));
				reportMap.put("DAILY_LIMIT ", new Double(0));
				reportMap.put("TOTAL_AMOUNT ", new Double(0));
				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
				throw new NTBWarningException("err.txn.ExceedUserLimit");
			}

		}

		 // add by mxl 0928 �жϽ�������
		String txnTypeBank = null;
		if(transferBank.getOwnerAccFlag().equals("Y")) {
			txnTypeBank = Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT;
		} else if(transferBank.getOwnerAccFlag().equals("N")) {
			txnTypeBank = Constants.TXN_TYPE_TRANSFER_BANK;
		}
		// ���daylimit 0929
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
			throw new NTBWarningException("err.txn.ExceedDailyLimit");
		}


		// ���û����д��session���Ա�confirm��д����ݿ�
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
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = (CorpUser) getUser();
        corpUser.setLanguage(lang);//add by linrui for mul-language
		TransferBank transferBank = (TransferBank) this
				.getUsrSessionDataValue("transferBank");
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferBank.getToCurrency(), "MOP",
				new BigDecimal(transferBank.getTransferAmount().toString()),
				null, 2);
		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));

		// �½�һ����Ȩ����
		String inputAmountFlag = transferBank.getInputAmountFlag();
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_TRANSFER_BANK,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				TransferInBankAction.class,
				transferBank.getFromCurrency(),
				transferBank.getDebitAmount().doubleValue(),
				transferBank.getToCurrency(),
				transferBank.getTransferAmount().doubleValue(),
				equivalentMOP.doubleValue(),
				transferBank.getTransId(),
				transferBank.getRemark(),
				getUser(), assignedUser,
				corpUser.getCorporation().getAllowExecutor(),
				inputAmountFlag);

		try {
			// ���һ����д����ݿ�
			transferBank.setExecuteTime(new Date());
			transferBank.setRequestTime(new Date());
			transferBank.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			transferBank.setStatus(Constants.STATUS_PENDING_APPROVAL);
			transferBank.setOperation(Constants.OPERATION_NEW);
                        Log.debug("TRANSFER IN BANK ID=" + transferBank.getTransId());

			transferService.transferAccInBANK1to1(transferBank);

			Map resultData = new HashMap();
			// edit by mxl 0819
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");
            //�ж��û�����Ļ��Һͽ�� add by mxl 0922
			String currency = null;
			Double transferAmount = new Double(0);
			if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferBank.getFromCurrency();
				transferAmount = transferBank.getDebitAmount();
			} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferBank.getToCurrency();
				transferAmount = transferBank.getTransferAmount();
			}
			//resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);
			resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);
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
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_TRANSFER_BANK, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_TRANSFER_BANK, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */
		
            // check value date cut-off time add by mxl 1123
			checkCutoffTimeAndSetMsg(transferBank);

			String txnTypeBank = null;

			if(transferBank.getOwnerAccFlag().equals("Y")) {
				txnTypeBank = Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT;
			} else if(transferBank.getOwnerAccFlag().equals("N")) {
				txnTypeBank = Constants.TXN_TYPE_TRANSFER_BANK;
			}
			transferService.toHostInBANK(
					transferBank, corpUser, txnTypeBank);


		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("Error process confirmation of transfering in BANK", e);
			if (e instanceof NTBException) {
				throw (NTBException)e;
			} else {
				throw new NTBException("err.txn.TranscationFaily");
			}
		}
		Log.info("[TransferInBankTest]End test");

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
			if (rateType.equals("0")) {
				exchangeRate = new BigDecimal("0");
			} else if (rateType.equals("1")) {
				exchangeRate = buyRate;
			} else if (rateType.equals("2")) {
				exchangeRate = sellRate;
			} else if (rateType.equals("3")) {
				exchangeRate = buyRate.divide(sellRate, 7,
						BigDecimal.ROUND_HALF_UP);
			}
			transferBank.setExchangeRate(new Double(exchangeRate.doubleValue()));
			// add by mxl 0928 �жϽ�������
			String txnTypeBank = null;
			if(transferBank.getOwnerAccFlag().equals("Y")) {
				txnTypeBank = Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT;
			} else if(transferBank.getOwnerAccFlag().equals("N")) {
				txnTypeBank = Constants.TXN_TYPE_TRANSFER_BANK;
			}

			// add mxl 0912
			BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferBank.getToCurrency(), "MOP",
					new BigDecimal(transferBank.getTransferAmount().toString()),
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

			//�����޶���
			transferService.loadUploadBANK(transferBank, fromHost);

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
		if (rateType.equals("0")) {
			exchangeRate = new BigDecimal("0");
		} else if (rateType.equals("1")) {
			exchangeRate = buyRate;
		} else if (rateType.equals("2")) {
			exchangeRate = sellRate;
		} else if (rateType.equals("3")) {
			exchangeRate = buyRate.divide(sellRate, 7,
					BigDecimal.ROUND_HALF_UP);
		}
		//transferBank.setExchangeRate(new Double(exchangeRate.doubleValue()));
		//transferService.updateBANK(transferBank);

		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferBank.getToCurrency(), "MOP",
				new BigDecimal(transferBank.getTransferAmount().toString()),
				null, 2);

		Map resultData = bean.getResultData();
		convertPojo2Map(transferBank, resultData);

		// edit by mxl 0819
		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");
        // �ж��û�����Ļ��Һͽ�� add by mxl 0922
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
		resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);
        // add by mxl 0930 ��ʾ���»���
		resultData.put("newExchangeRate", exchangeRate);
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
		// add  by mxl 1005 �ж�������
		if (transferBank.getInputAmountFlag().equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			transferBank.setTransferAmount(null);
		} else if (transferBank.getInputAmountFlag().equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			transferBank.setDebitAmount(null);
		}
		transferService.templateInBANK1to1(transferBank);

	}

        public boolean cancel(String txnType, String id, CibAction bean) throws
                NTBException {
            return reject(txnType, id, bean);
        }

        private void checkCutoffTimeAndSetMsg(TransferBank transferBank) throws NTBException {
    		ApplicationContext appContext = Config.getAppContext();
    		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext.getBean("CutOffTimeService");
    		// check value date cut-off time
               if (transferBank.getFromAcctype().equals("1")
                    && transferBank.getToAcctype().equals("1")) {
                   setMessage(cutOffTimeService.check("51CC",transferBank.getFromCurrency(),transferBank.getToCurrency()));
                } else if (transferBank.getFromAcctype().equals("1")
                           && transferBank.getToAcctype().equals("5")) {
                   setMessage(cutOffTimeService.check("51CS",transferBank.getFromCurrency(),transferBank.getToCurrency()));
                } else if (transferBank.getFromAcctype().equals("5")
                           && transferBank.getToAcctype().equals("5")) {
                   setMessage(cutOffTimeService.check("51SS",transferBank.getFromCurrency(),transferBank.getToCurrency()));
                }  else if (transferBank.getFromAcctype().equals("5")
                           && transferBank.getToAcctype().equals("1")) {
                	setMessage(cutOffTimeService.check("51SC",transferBank.getFromCurrency(),transferBank.getToCurrency()));
                }

        }

}
