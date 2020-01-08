package app.cib.action.bat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import app.cib.bo.bat.FileRequest;
import app.cib.bo.bat.FileRequestFileBean;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.dao.enq.ExchangeRatesDao;
import app.cib.service.bat.TransferMacauStMService;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.srv.RequestService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.CutOffTimeService;
import app.cib.service.sys.MailService;
import app.cib.service.sys.TransAmountService;
import app.cib.service.txn.TransferLimitService;
import app.cib.service.txn.TransferService;
import app.cib.util.Constants;
import app.cib.util.UploadReporter;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBWarningException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;


public class TransferInMacauStMAction extends CibAction implements Approvable {
	private static final String saveFilePath = Config
			.getProperty("BatchFileUploadDir")
			+ "/";

	public void uploadFileLoad() throws NTBException {
		Map resultData = new HashMap(1);
		this.setResultData(resultData);
		this.clearUsrSessionDataValue();
		ApplicationContext appContext = Config.getAppContext();
		TransferMacauStMService transferMacauStMService = (TransferMacauStMService) appContext
				.getBean("TransferMacauStMService");
		CorpUser corpUser = (CorpUser) this.getUser();
		// clear unavailable db data
		transferMacauStMService.clearUnavailableDataByCorpId(corpUser
				.getCorpId());
		//add by linrui 20180313
		//setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg",Constants.US).getString("warnning.transfer.DifferenceCcy"));
	}

	public void uploadFile() throws NTBException {
		// 锟斤拷始锟斤拷Service
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		TransferMacauStMService transferMacauStMService = (TransferMacauStMService) appContext
				.getBean("TransferMacauStMService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		TransAmountService transAmountService = (TransAmountService) appContext
				.getBean("TransAmountService");
		
		CorpUser corpUser = (CorpUser) this.getUser();
//		String fileName = this.getUploadFileName();
		String errFlag = "N";
		// 锟斤拷锟斤拷页锟斤拷锟斤拷锟�
		String fromAccount = null;
		String fromCurrency = null;
		// fromAccount = Utils.null2EmptyWithTrim(getParameter("fromAccount"));

		InputStream inStream;
		try {
			inStream = this.getUploadFileInputStream();
		} catch (FileNotFoundException e) {
			Log.error("", e);
			throw new NTBException("err.bat.UploadFileNotFound");
		} catch (IOException e) {
			Log.error("", e);
			throw new NTBException("err.bat.UploadFaily");
		}
		FileRequestFileBean fileRequestFileBean = transferMacauStMService
				.parseFile(corpUser, inStream);
		Map fileRequest = null;

		// add by mxl 1220
		try {
			List normalList = fileRequestFileBean.getNormalList();
			List errList = fileRequestFileBean.getErrList();
			double normalTotalAmt = fileRequestFileBean.getNormalTotalAmt();
			double errTotalAmt = fileRequestFileBean.getErrTotleAmt();
			int allCount = fileRequestFileBean.getAllCount();
			fileRequest = fileRequestFileBean.getFileRequestHeader();
			Map resultData = new HashMap();
			Map headInfo = new HashMap();
			headInfo.put("allCount", new Integer(allCount));
			headInfo.put("totalAmount",
					new Double(normalTotalAmt + errTotalAmt));
			
			//Jet add, check minimum transaction amount
			transAmountService.checkMinAmtOtherBanks(fileRequest.get("TO_CURRENCY").toString(), Double.parseDouble(fileRequest.get("TO_AMOUNT").toString()));
			
			// if error
			if (errList.size() != 0) {
				errFlag = "Y";

			} else if (normalTotalAmt + errTotalAmt != Double
					.parseDouble(fileRequest.get("TO_AMOUNT").toString())) {
				errFlag = "Y";
				// edit by mxl 1219
				setError(new NTBException("err.bat.AmountNotEqual"));
			} else if (allCount != Integer.parseInt(fileRequest.get(
					"TOTAL_NUMBER").toString())) {
				errFlag = "Y";
				setError(new NTBException("err.bat.CountNotEqual"));
			}
			fromAccount = fileRequest.get("FROM_ACCOUNT").toString();
			// 锟斤拷锟�From Currency 锟侥憋拷锟街猴拷锟斤拷锟斤拷
			CorpAccount corpAccount = corpAccountService
					.viewCorpAccount(fromAccount);
			fromCurrency = corpAccount.getCurrency();
			BigDecimal debitAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), fromCurrency, fileRequest.get("TO_CURRENCY")
					.toString(), null, new BigDecimal(fileRequest.get(
					"TO_AMOUNT").toString()), 2);
			
			// Jet add, show the exchange rate
			Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), fromCurrency, fileRequest.get("TO_CURRENCY")
					.toString(), 7);
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
			Double ex_Rate = new Double(exchangeRate.doubleValue());
			headInfo.put("exchangeRate", ex_Rate);
			
			// 锟斤拷锟斤拷洗锟斤拷募锟斤拷锟矫伙拷写锟斤拷锟斤拷录锟斤拷锟斤拷锟斤拷要锟斤拷锟斤拷权锟斤拷锟�锟斤拷锟斤拷锟斤拷锟斤拷 1201
			String errorFlag = "N";
			if (errList.size() == 0) {
				errorFlag = "Y";
				String batchId = fileRequest.get("BATCH_ID").toString();
				FileRequest pojoFileRequest = requestService
						.viewFileRequest(batchId);
				// add by mxl 1213
				pojoFileRequest.setFromAccount(pojoFileRequest.getFromAccount()
						.trim());
				// 为SelectUser Tag准锟斤拷锟斤拷锟�
				BigDecimal equivalentMOP = exRatesService.getEquivalent(
						corpUser.getCorpId(), fileRequest.get("TO_CURRENCY")
								.toString(), "MOP", new BigDecimal(fileRequest
								.get("TO_AMOUNT").toString()), null, 2);

				// 锟叫断斤拷锟斤拷锟睫讹拷
				if (!corpUser.checkUserLimit(Constants.TXN_TYPE_TRANSFER_MACAU,
						equivalentMOP)) {
					// write limit report
					Map reportMap = new HashMap();
					reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
							"yyyyMMdd"));
					reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
							"HHmmss"));
					reportMap.put("USER_ID", corpUser.getUserId());
					reportMap.put("CORP_ID", corpUser.getCorpId());
					reportMap.put("TRANS_TYPE",
							Constants.TXN_TYPE_TRANSFER_MACAU);
					reportMap.put("LOCAL_TRANS_CODE", fileRequest
							.get("BATCH_ID"));
					reportMap.put("FROM_CURRENCY", fromCurrency);
					reportMap.put("FROM_ACCOUNT", fileRequest
							.get("FROM_ACCOUNT"));
					reportMap
							.put("TO_CURRENCY", fileRequest.get("TO_CURRENCY"));
					reportMap.put("FROM_AMOUNT", debitAmount);
					reportMap.put("TO_AMOUNT", fileRequest.get("TO_AMOUNT"));
					reportMap.put("EXCEEDING_TYPE", "2");
					reportMap.put("LIMIT_TYPE", "");
					reportMap.put("USER_LIMIT ", corpUser
							.getUserLimit(Constants.TXN_TYPE_TRANSFER_MACAU));
					reportMap.put("DAILY_LIMIT ", new Double(0));
					reportMap.put("TOTAL_AMOUNT ", new Double(0));
					UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
					throw new NTBWarningException("err.txn.ExceedUserLimit");
				}
	
				
				// add by mxl 锟斤拷锟絛aylimit
				if (!transferLimitService.checkCurAmtLimitQuota(fileRequest.get(
						"FROM_ACCOUNT").toString(), corpUser.getCorpId(),
						Constants.TXN_TYPE_TRANSFER_MACAU, debitAmount
								.doubleValue(), equivalentMOP.doubleValue())) {
					// write limit report
					Map reportMap = new HashMap();
					reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
							"yyyyMMdd"));
					reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
							"HHmmss"));
					reportMap.put("USER_ID", corpUser.getUserId());
					reportMap.put("CORP_ID", corpUser.getCorpId());
					reportMap.put("TRANS_TYPE",
							Constants.TXN_TYPE_TRANSFER_MACAU);
					reportMap.put("LOCAL_TRANS_CODE", fileRequest
							.get("BATCH_ID"));
					reportMap.put("FROM_ACCOUNT", fileRequest
							.get("FROM_ACCOUNT"));
					reportMap.put("FROM_CURRENCY", fromCurrency);
					reportMap
							.put("TO_CURRENCY", fileRequest.get("TO_CURRENCY"));
					reportMap.put("FROM_AMOUNT", debitAmount);
					reportMap.put("TO_AMOUNT", fileRequest.get("TO_AMOUNT"));
					reportMap.put("EXCEEDING_TYPE", "1");
					reportMap.put("LIMIT_TYPE", transferLimitService
							.getLimitType());
					reportMap.put("USER_LIMIT ", "0");
					reportMap.put("DAILY_LIMIT ", new Double(
							transferLimitService.getDailyLimit()));
					reportMap.put("TOTAL_AMOUNT ", new Double(
							transferLimitService.getTotalLimit()));
					UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
					throw new NTBWarningException("err.txn.ExceedDailyLimit");
				}
				
				resultData.put("txnTypeField", "txnType");
				resultData.put("currencyField", "currency");
				resultData.put("amountField", "amount");
				resultData.put("amountMopEqField", "amountMopEq");

				resultData.put("txnType",
						Constants.TXN_SUBTYPE_TRANSFER_MACAU_1TON);
				resultData.put("currency", fileRequest.get("TO_CURRENCY")
						.toString());
				resultData.put("amount", new BigDecimal(fileRequest.get(
						"TO_AMOUNT").toString()));
				resultData.put("amountMopEq", equivalentMOP);

				// add by mxl 1102 update FileQequest锟斤拷
				pojoFileRequest.setFromCurrency(fromCurrency);
				pojoFileRequest
						.setFromAmount(new Double(debitAmount.toString()));
				pojoFileRequest.setRequestTime(new Date());
				requestService.updateFileRequest(pojoFileRequest);

			}
			resultData.putAll(fileRequest);
			resultData.putAll(headInfo);
			resultData.put("errFlag", errFlag);
			resultData.put("errList", errList);
			resultData.put("errRecordNo", String.valueOf(errList.size()));
			resultData.put("rightRecordNo", String.valueOf(normalList.size()));
			// add by mxl 1208
			if (errList.size() == 0) {
				resultData.put("recList", normalList);
			} else {
				resultData.put("recList", errList);
			}
			resultData.put("fromCurrency", fromCurrency);
			resultData.put("fromAccount", fromAccount);
			resultData.put("fromAmount", debitAmount);
						
			this.setResultData(resultData);
			this.setUsrSessionDataValue("errorFlag", errorFlag);
			this.setUsrSessionDataValue("fromCurrency", fromCurrency);
			this.setUsrSessionDataValue("fileRequest", fileRequest);
			this.setUsrSessionDataValue("headInfo", headInfo);
			this.setUsrSessionDataValue("recList", normalList);
			
		} catch (Exception e) {
			transferMacauStMService.cancelUpload(fileRequest.get("BATCH_ID")
					.toString().trim(), null, null, new File(saveFilePath
					+ fileRequest.get("FILE_NAME").toString().trim()));
			throw new NTBException(e.getMessage());
		}
	}

	public void uploadFileConfirm() throws NTBException {
		// 锟斤拷始锟斤拷Service
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		TransferMacauStMService transferMacauStMService = (TransferMacauStMService) appContext
				.getBean("TransferMacauStMService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext
				.getBean("CutOffTimeService");
		
		CorpUser corpUser = (CorpUser) this.getUser();
		List recList = (List) this.getUsrSessionDataValue("recList");
		Map fileRequest = (Map) this.getUsrSessionDataValue("fileRequest");
		Map headInfo = (Map) this.getUsrSessionDataValue("headInfo");
		String errorFlag = (String) this.getUsrSessionDataValue("errorFlag");
		String fromCurrency = (String) this
				.getUsrSessionDataValue("fromCurrency");
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), fileRequest.get("TO_CURRENCY").toString(), "MOP",
				new BigDecimal(fileRequest.get("TO_AMOUNT").toString()), null,
				2);
		BigDecimal debitAmount = exRatesService.getEquivalent(corpUser
				.getCorpId(), fromCurrency, fileRequest.get("TO_CURRENCY")
				.toString(), null, new BigDecimal(fileRequest.get("TO_AMOUNT")
				.toString()), 2);

		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));
		// check value date cut-off time add by mxl 0130
		setMessage(cutOffTimeService.check("XJ55", fromCurrency, fileRequest
				.get("TO_CURRENCY").toString()));
		
		//add by chen_y for CR225 20170413
		if(errorFlag!=null && "Y".equals(errorFlag)){
			
			TransferLimitService transferLimitService = (TransferLimitService) appContext
			.getBean("TransferLimitService");
			// add by mxl 锟斤拷锟絛aylimit
			if (!transferLimitService.checkLimitQuota(fileRequest.get(
					"FROM_ACCOUNT").toString(), corpUser.getCorpId(),
					Constants.TXN_TYPE_TRANSFER_MACAU, debitAmount
							.doubleValue(), equivalentMOP.doubleValue())) {
				// write limit report
				Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
						"yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
						"HHmmss"));
				reportMap.put("USER_ID", corpUser.getUserId());
				reportMap.put("CORP_ID", corpUser.getCorpId());
				reportMap.put("TRANS_TYPE",
						Constants.TXN_TYPE_TRANSFER_MACAU);
				reportMap.put("LOCAL_TRANS_CODE", fileRequest
						.get("BATCH_ID"));
				reportMap.put("FROM_ACCOUNT", fileRequest
						.get("FROM_ACCOUNT"));
				reportMap.put("FROM_CURRENCY", fromCurrency);
				reportMap
						.put("TO_CURRENCY", fileRequest.get("TO_CURRENCY"));
				reportMap.put("FROM_AMOUNT", debitAmount);
				reportMap.put("TO_AMOUNT", fileRequest.get("TO_AMOUNT"));
				reportMap.put("EXCEEDING_TYPE", "1");
				reportMap.put("LIMIT_TYPE", transferLimitService
						.getLimitType());
				reportMap.put("USER_LIMIT ", "0");
				reportMap.put("DAILY_LIMIT ", new Double(
						transferLimitService.getDailyLimit()));
				reportMap.put("TOTAL_AMOUNT ", new Double(
						transferLimitService.getTotalLimit()));
				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
				//throw new NTBWarningException("err.txn.ExceedDailyLimit");
				setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg").getString("warnning.txn.ExceedDailyLimit"));
			}
		}
		
		//add by chen_y for CR225 20170413 end
 		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_TRANSFER_MACAU_1TON,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				TransferInMacauStMAction.class, fromCurrency, debitAmount
						.doubleValue(), fileRequest.get("TO_CURRENCY")
						.toString(), (new BigDecimal(fileRequest.get(
						"TO_AMOUNT").toString())).doubleValue(), equivalentMOP
						.doubleValue(), fileRequest.get("BATCH_ID").toString(),
				fileRequest.get("REMARK").toString(), getUser(), assignedUser,
				corpUser.getCorporation().getAllowExecutor(),
				FlowEngineService.RULE_FLAG_TO);
		FileRequest pojoFileRequest = requestService
				.viewFileRequest(fileRequest.get("BATCH_ID").toString());
		// transferBankStMService.approvePayroll(pojoFileRequest, corpUser);
		try {
			// 锟斤拷锟揭伙拷锟斤拷锟叫达拷锟斤拷锟捷匡拷
			transferMacauStMService.updateStatus(fileRequest.get("BATCH_ID")
					.toString());
			Map resultData = new HashMap();

			resultData.putAll(fileRequest);
			resultData.putAll(headInfo);
			resultData.put("recList", recList);
			convertPojo2Map(pojoFileRequest, resultData);
			this.setResultData(resultData);
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");

			resultData
					.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_MACAU_1TON);
			resultData.put("currency", fileRequest.get("TO_CURRENCY")
					.toString());
			resultData.put("amount", new BigDecimal(fileRequest
					.get("TO_AMOUNT").toString()));
			resultData.put("amountMopEq", equivalentMOP);

			// send mial to approver
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", pojoFileRequest.getRequestTime());
			dataMap.put("fromAmount", pojoFileRequest.getFromAmount());
			dataMap.put("corpName", corpUser.getCorporation().getCorpName());
			dataMap.put("fromAccount", pojoFileRequest.getFromAccount());
			dataMap.put("fromCurrency", pojoFileRequest.getFromCurrency());
			
			// dataMap.put("beneficiaryAccName",pojoFileRequest.getBeneficiaryName1());
			// dataMap.put("beneficiaryBank",transferMacau.getBeneficiaryBank());
			dataMap.put("transId", pojoFileRequest.getBatchId());
			dataMap.put("remark", pojoFileRequest.getRemark());
			
			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_TRANSFER_MACAU_1TON, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_TRANSFER_MACAU_1TON, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */
			
		} catch (Exception e) {
			// clear unavailable db data
			transferMacauStMService
					.clearUnavailableDataByPaybatchId(fileRequest.get(
							"BATCH_ID").toString());
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("err.txn.TranscationFaily", e);
			if (e instanceof NTBException) {
				throw new NTBException(e.getMessage());
			} else {
				throw new NTBException("err.txn.TranscationFaily");
			}
		}
	}

	public void uploadFileCancel() throws NTBException {
		// initial service
		ApplicationContext appContext = Config.getAppContext();
		TransferMacauStMService transferMacauStMService = (TransferMacauStMService) appContext
				.getBean("TransferMacauStMService");

		Map fileRequest = (Map) this.getUsrSessionDataValue("fileRequest");

		transferMacauStMService.cancelUpload(fileRequest.get("BATCH_ID")
				.toString().trim(), null, null, new File(saveFilePath
				+ fileRequest.get("FILE_NAME").toString().trim()));
	}

	public boolean approve(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		TransferMacauStMService transferMacauStMService = (TransferMacauStMService) appContext
				.getBean("TransferMacauStMService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		CorpUser corpUser = (CorpUser) bean.getUser();

		if (txnType != null) {
			FileRequest pojoFileRequest = requestService.viewFileRequest(id);
			// 锟斤拷锟�From Currency 锟侥憋拷锟斤拷
			CorpAccount corpAccount = corpAccountService
					.viewCorpAccount(pojoFileRequest.getFromAccount());
			String fromCurrency = corpAccount.getCurrency();
			// 锟斤拷锟斤拷fromAmount
			BigDecimal debitAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), fromCurrency,
					pojoFileRequest.getToCurrency(), null, new BigDecimal(
							pojoFileRequest.getToAmount().toString()), 2);
			pojoFileRequest.setFromAmount(new Double(debitAmount.toString()));
			requestService.updateFileRequest(pojoFileRequest);
			Map headInfo = new HashMap();
			headInfo.put("currency", pojoFileRequest.getCurrency());
			// 锟斤拷锟斤拷榷锟斤拷媳锟�
			BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
					.getCorpId(), pojoFileRequest.getToCurrency(), "MOP",
					new BigDecimal(pojoFileRequest.getToAmount().toString()),
					null, 2);
			// 锟斤拷榻伙拷锟斤拷薅锟�
			if (!transferLimitService.checkLimitQuota(pojoFileRequest
					.getFromAccount(), corpUser.getCorpId(),
					Constants.TXN_TYPE_TRANSFER_MACAU, debitAmount
							.doubleValue(), equivalentMOP.doubleValue())) {
				// write limit report
				Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
						"yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
						"HHmmss"));
				reportMap.put("USER_ID", corpUser.getUserId());
				reportMap.put("CORP_ID", corpUser.getCorpId());
				reportMap.put("TRANS_TYPE", txnType);
				reportMap.put("LOCAL_TRANS_CODE", pojoFileRequest.getBatchId());
				reportMap.put("FROM_ACCOUNT", pojoFileRequest.getFromAccount());
				reportMap.put("FROM_CURRENCY", fromCurrency);
				reportMap.put("TO_CURRENCY", pojoFileRequest.getToCurrency());
				reportMap.put("FROM_AMOUNT", debitAmount);
				reportMap.put("TO_AMOUNT", pojoFileRequest.getToAmount());
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
			transferMacauStMService.approveTransferMacauStM(pojoFileRequest,
					corpUser, equivalentMOP);
			//add by linrui to host after upload file
			transferMacauStMService.toHostTransferSTM(pojoFileRequest,
					corpUser, txnType);
			//end			
			//send email to last approver or executor, add by hjs 20071029
			MailService mailService = (MailService) appContext.getBean("MailService");
			Map dataMap = new HashMap();
			// get summary info
			this.convertPojo2Map(pojoFileRequest, dataMap);
			dataMap.put("lastUpdateTime", new Date());
			
			// get bene detail info
			TransferService transferService = (TransferService) appContext.getBean("TransferService");
			List beneList_temp = transferService.listTransferMacauStmByBatchid(pojoFileRequest.getBatchId());
			List beneList = new ArrayList();
			for (int i = 0; i < beneList_temp.size(); i++){
				Map recordMap = new HashMap();
				this.convertPojo2Map(beneList_temp.get(i),recordMap);
				beneList.add(recordMap);
			}
			dataMap.put("beneList", beneList);
				
			/* Modify by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
			/*mailService.toLastApprover_Executor(Constants.TXN_SUBTYPE_TRANSFER_MACAU_1TON, corpUser.getUserId(), dataMap);*/
			mailService.toLastApprover_Executor(Constants.TXN_SUBTYPE_TRANSFER_MACAU_1TON, corpUser.getUserId(),corpUser.getCorpId(), dataMap);
			/* Modify by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
			return true;
		} else {
			return false;
		}
	}

	public boolean cancel(String txnType, String id, CibAction bean)
			throws NTBException {
		return this.reject(txnType, id, bean);
	}

	public boolean reject(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferMacauStMService transferMacauStMService = (TransferMacauStMService) appContext
				.getBean("TransferMacauStMService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		if (txnType != null) {
			FileRequest pojoFileRequest = requestService.viewFileRequest(id);
			// payrollService.rejectPayroll(payroll);
			transferMacauStMService.rejectFileRequest(pojoFileRequest);
			return true;
		} else {
			return false;
		}
	}

	public String viewDetail(String txnType, String id, CibAction bean)
			throws NTBException {

		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		
		String viewPageUrl = "";
		Map resultData = bean.getResultData();
		CorpUser corpUser = (CorpUser) bean.getUser();
		FileRequest pojoFileRequest = requestService.viewFileRequest(id);
		viewPageUrl = "/WEB-INF/pages/bat/batch_transfer/upload_file_approval_viewMacauStM.jsp";
		// add by mxl 1208
		List recList = transferService.listTransferMacauStmByBatchid(id);
		recList = this.convertPojoList2MapList(recList);

		// Jet add, show new from amount
		BigDecimal newFromAmount = exRatesService.getEquivalent(corpUser
				.getCorpId(), pojoFileRequest.getFromCurrency(),
				pojoFileRequest.getToCurrency(), null, new BigDecimal(
						pojoFileRequest.getToAmount().toString()), 2);
		
		// Jet add, show new exchange rate
		Map exchangeMap = exRatesService.getExchangeRate(corpUser.getCorpId(),
				pojoFileRequest.getFromCurrency(), pojoFileRequest.getToCurrency(), 7);
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
		Double ex_Rate = new Double(exchangeRate.doubleValue());
		
		// 锟斤拷织assignuser_tag锟斤拷锟�
		Map assignuser = new HashMap();
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), pojoFileRequest.getCurrency(), "MOP",
				new BigDecimal(pojoFileRequest.getToAmount().toString()), null,
				2);

		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "currency");
		assignuser.put("amountField", "totalAmount");
		assignuser.put("amountMopEqField", "amountMopEq");
		assignuser.put("txnType", txnType);
		assignuser.put("amountMopEq", equivalentMOP);

		bean.convertPojo2Map(pojoFileRequest, resultData);
		// 锟斤拷锟斤拷锟斤拷莸锟絘ssignuser_tag
		// resultData.put("currency", pojoFileRequest.getCurrency());
		resultData.putAll(assignuser);
		resultData.put("recList", recList);
		resultData.put("newExchangeRate", ex_Rate);
		resultData.put("newFromAmount", new Double(newFromAmount.doubleValue()));
				
		bean.setResultData(resultData);
		return viewPageUrl;
	}

}
