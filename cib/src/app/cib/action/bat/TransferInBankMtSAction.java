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

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.exception.NTBWarningException;

import app.cib.bo.bat.FileRequest;
import app.cib.bo.bat.FileRequestFileBean;
import app.cib.bo.bat.TransferBankStm;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.dao.enq.ExchangeRatesDao;
import app.cib.service.bat.TransferBankMtSService;
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

public class TransferInBankMtSAction extends CibAction implements Approvable{
	private static final String saveFilePath = Config.getProperty("BatchFileUploadDir") + "/";
    public void uploadFileLoad() throws NTBException {
    	Map resultData = new HashMap(1);
		this.setResultData(resultData);
		this.clearUsrSessionDataValue();
		ApplicationContext appContext = Config.getAppContext();
		TransferBankMtSService transferBankMtSService = (TransferBankMtSService) appContext.getBean("TransferBankMtSService");
		CorpUser corpUser = (CorpUser) this.getUser();
		//clear unavailable db data
		transferBankMtSService.clearUnavailableDataByCorpId(corpUser.getCorpId());
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
		TransferBankMtSService transferBankMtSService = (TransferBankMtSService) appContext
				.getBean("TransferBankMtSService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		TransAmountService transAmountService = (TransAmountService) appContext
				.getBean("TransAmountService");


		CorpUser corpUser = (CorpUser) this.getUser();
		String errFlag = "N";
		// 锟斤拷锟斤拷页锟斤拷锟斤拷锟�
		String toAccount = null;
		String toCurrency = null;
		
		//Add by heyongjiang 2010-04-21
		String toAccountName = null;
		String toAccountName2 = null;

		InputStream inStream;
		try {
			inStream = this.getUploadFileInputStream();
		} catch (FileNotFoundException e) {
			throw new NTBException("err.bat.UploadFileNotFound");
		} catch (IOException e) {
			throw new NTBException("err.bat.UploadFaily");
		}

		FileRequestFileBean fileRequestFileBean = transferBankMtSService
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
			
			//Jet add, check minimum transaction amount
			transAmountService.checkMinTransAmt(fileRequest.get("TO_CURRENCY").toString(), Double.parseDouble(fileRequest.get("TO_AMOUNT").toString()));
			
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
				setError(new NTBException("err.bat.CountNotEqual"));
				errFlag = "Y";
			}

			headInfo.put("allCount", new Integer(allCount));
			headInfo.put("totalAmount",
					new Double(normalTotalAmt + errTotalAmt));
			
			toAccount = fileRequest.get("TO_ACCOUNT").toString().trim();
			
			//Add by heyongjiang 2010-04-21
			//update by gan 20180111
//			toAccountName = fileRequest.get("TO_ACCOUNT_NAME").toString().trim();
//			toAccountName2 = fileRequest.get("TO_ACCOUNT_NAME2").toString().trim();
			
			/*if("".equals(toAccountName2.trim())){
				toAccountName = fileRequest.get("TO_ACCOUNT_NAME").toString().trim();
			}else{
				toAccountName = (fileRequest.get("TO_ACCOUNT_NAME").toString() + toAccountName2).trim();
			}*/
			
			CorpAccount corpAccount = corpAccountService
					.viewCorpAccount(toAccount);
			toCurrency = corpAccount.getCurrency();

			// 锟斤拷锟斤拷toAmount
			BigDecimal creditAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), fileRequest.get("TO_CURRENCY").toString(),
					toCurrency, new BigDecimal(fileRequest.get("TO_AMOUNT")
							.toString()), null, 2);
			
			// Jet add, show the exchange rate
			Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), fileRequest.get("TO_CURRENCY").toString(),
					toCurrency, 7);
			
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

			String errorFlag = "N";
			if (errList.size() == 0) {
				errorFlag = "Y";
				String batchId = fileRequest.get("BATCH_ID").toString();
				FileRequest pojoFileRequest = requestService
						.viewFileRequest(batchId);

				// 为SelectUser Tag 

				BigDecimal equivalentMOP = exRatesService.getEquivalent(
						corpUser.getCorpId(), fileRequest.get("TO_CURRENCY")
								.toString(), "MOP", new BigDecimal(fileRequest
								.get("TO_AMOUNT").toString()), null, 2);
				/*
				 * exRatesService.getEquivalent(corpUser
				 * .getCorpId(),fileRequest.get("FROM_CURRENCY").toString(),
				 * toCurrency, new
				 * BigDecimal(fileRequest.get("FROM_AMOUNT").toString()), null,
				 * 2);
				 */
				if (!corpUser.checkUserLimit(Constants.TXN_TYPE_TRANSFER_BANK,
						equivalentMOP)) {
					// write limit report
					Map reportMap = new HashMap();
					reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
							"yyyyMMdd"));
					reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
							"HHmmss"));
					reportMap.put("USER_ID", corpUser.getUserId());
					reportMap.put("CORP_ID", corpUser.getCorpId());
					reportMap
							.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_BANK);
					reportMap.put("LOCAL_TRANS_CODE", fileRequest
							.get("BATCH_ID"));
					reportMap.put("FROM_CURRENCY", fileRequest.get("TO_CURRENCY"));
//					 reportMap.put("FROM_ACCOUNT", fileRequest.get("FROM_ACCOUNT"));
					reportMap.put("TO_CURRENCY", toCurrency);
					reportMap.put("FROM_AMOUNT", fileRequest.get("TO_AMOUNT"));
					reportMap.put("TO_AMOUNT", creditAmount);
					reportMap.put("EXCEEDING_TYPE", "2");
					reportMap.put("LIMIT_TYPE", "");
					reportMap.put("USER_LIMIT ", corpUser
							.getUserLimit(Constants.TXN_TYPE_TRANSFER_BANK));
					reportMap.put("DAILY_LIMIT ", new Double(0));
					reportMap.put("TOTAL_AMOUNT ", new Double(0));
					UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
					throw new NTBWarningException("err.txn.ExceedUserLimit");
				}

				// add by mxl 1109
				List transIdList = new ArrayList();
				transIdList = transferBankMtSService
						.transIdListTransferBankStm(batchId);
				if (null != transIdList && transIdList.size() > 0) {
					for (int i = 0; i < transIdList.size(); i++) {
						Map transIdMap = new HashMap();
						String transId = null;
						transIdMap = (Map) transIdList.get(i);
						transId = (String) transIdMap.get("TRANS_ID");
						TransferBankStm transferBankStm = transferBankMtSService
								.viewInBANKStm(transId);
						
						CorpAccount corpAccount2 = corpAccountService
								.viewCorpAccount(transferBankStm
										.getFromAccount());
						String fromCurrency = corpAccount2.getCurrency();
						transferBankStm.setFromCurrency(fromCurrency);
						
						
						BigDecimal fromAmount = exRatesService.getEquivalent(
								corpUser.getCorpId(), transferBankStm
										.getFromCurrency(), fileRequest.get(
										"TO_CURRENCY").toString(), null,
								new BigDecimal(transferBankStm.getDebitAmount()
										.toString()), 2);
												
						// 锟斤拷锟斤拷fromAmount锟侥等讹拷锟较憋拷
						
						  BigDecimal fromEquivalentMOP = exRatesService
						 
								.getEquivalent(corpUser.getCorpId(),
										transferBankStm.getFromCurrency(),
										"MOP", new BigDecimal(fromAmount
												.toString()), null, 2);
						
						// add by mxl  daylimit add by mxl 1008
						if (!transferLimitService.checkCurAmtLimitQuota(
								transferBankStm.getFromAccount(), corpUser
										.getCorpId(),
								Constants.TXN_TYPE_TRANSFER_BANK, fromAmount
										.doubleValue(), fromEquivalentMOP
										.doubleValue())) {
							// write limit report
							Map reportMap = new HashMap();
							reportMap.put("TRANS_DATE", DateTime.formatDate(
									new Date(), "yyyyMMdd"));
							reportMap.put("TRANS_TIME", DateTime.formatDate(
									new Date(), "HHmmss"));
							reportMap.put("USER_ID", corpUser.getUserId());
							reportMap.put("CORP_ID", corpUser.getCorpId());
							reportMap.put("TRANS_TYPE",
									Constants.TXN_TYPE_TRANSFER_BANK);
							reportMap.put("LOCAL_TRANS_CODE", transferBankStm
									.getTransId());
							reportMap.put("FROM_ACCOUNT", transferBankStm
									.getFromAccount());
							reportMap.put("FROM_CURRENCY", transferBankStm.getFromCurrency());									
							reportMap.put("TO_CURRENCY", toCurrency);
							reportMap.put("FROM_AMOUNT", fromAmount);
							reportMap.put("TO_AMOUNT", transferBankStm
									.getDebitAmount());
							reportMap.put("EXCEEDING_TYPE", "1");
							reportMap.put("LIMIT_TYPE", transferLimitService
									.getLimitType());
							reportMap.put("USER_LIMIT ", "0");
							reportMap.put("DAILY_LIMIT ", new Double(
									transferLimitService.getDailyLimit()));
							reportMap.put("TOTAL_AMOUNT ", new Double(
									transferLimitService.getTotalLimit()));
							UploadReporter
									.writeBuffer("RP_EXCLIMIT", reportMap);
							throw new NTBWarningException(
									"err.txn.ExceedDailyLimit");
						}
					}
					// add by li_zd at 20170612
					if (!transferLimitService.checkLimitQuotaByCorpId(corpUser.getCorpId(), 
							Constants.TXN_TYPE_TRANSFER_BANK, new BigDecimal(fileRequest.get("TO_AMOUNT").toString()).doubleValue(), equivalentMOP.doubleValue())) {
						// write limit report
//						Map reportMap = new HashMap();
//						reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
//						reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
//						reportMap.put("USER_ID", corpUser.getUserId());
//						reportMap.put("CORP_ID", corpUser.getCorpId());
//						reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_BANK);
//						reportMap.put("LOCAL_TRANS_CODE", transferBankStm.getTransId());
//						reportMap.put("FROM_ACCOUNT", transferBankStm.getFromAccount());
//						reportMap.put("FROM_CURRENCY", transferBankStm.getFromCurrency());
//						reportMap.put("TO_CURRENCY", toCurrency);
//						reportMap.put("FROM_AMOUNT", fromAmount);
//						reportMap.put("TO_AMOUNT", transferBankStm.getDebitAmount());
//						reportMap.put("EXCEEDING_TYPE", "1");
//						reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
//						reportMap.put("USER_LIMIT ", "0");
//						reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
//						reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
//						UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
						throw new NTBWarningException("err.txn.ExceedDailyLimit");		
					}// end
				}
				resultData.put("txnTypeField", "txnType");
				resultData.put("currencyField", "currency");
				resultData.put("amountField", "amount");
				resultData.put("amountMopEqField", "amountMopEq");

				resultData.put("txnType",
						Constants.TXN_SUBTYPE_TRANSFER_BANK_NTO1);
				resultData.put("currency", fileRequest.get("TO_CURRENCY")
						.toString());
				resultData.put("amount", new BigDecimal(fileRequest.get(
						"TO_AMOUNT").toString()));
				resultData.put("amountMopEq", equivalentMOP);
				this.setResultData(resultData);
				// add by mxl 1102 update FileQequest锟斤拷
				pojoFileRequest.setFromCurrency(toCurrency);
				pojoFileRequest
						.setFromAmount(new Double(creditAmount.toString()));
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
			
			resultData.put("toCurrency", toCurrency);
			resultData.put("toAccount", toAccount);
			resultData.put("toAmount", creditAmount);
			
			//Add by heyongjiang 2010-04-21
			resultData.put("TO_ACCOUNT_NAME", toAccountName);
			resultData.put("TO_ACCOUNT_NAME2", toAccountName2);
			
			this.setResultData(resultData);
			this.setUsrSessionDataValue("errorFlag", errorFlag);
			this.setUsrSessionDataValue("toCurrency", toCurrency);
			this.setUsrSessionDataValue("fileRequest", fileRequest);
			this.setUsrSessionDataValue("headInfo", headInfo);
			this.setUsrSessionDataValue("recList", normalList);
		} catch (Exception e) {
			transferBankMtSService.cancelUpload(fileRequest.get("BATCH_ID")
					.toString().trim(), null, null, new File(saveFilePath
					+ fileRequest.get("FILE_NAME").toString().trim()));
			throw new NTBException(e.getMessage());
		}
	}

    public void uploadFileConfirm() throws NTBException {
    	// 锟斤拷始锟斤拷Service
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
		RequestService requestService = (RequestService) appContext.getBean("RequestService");
		TransferBankMtSService transferBankMtSService = (TransferBankMtSService) appContext.getBean("TransferBankMtSService");
		MailService mailService = (MailService) appContext.getBean("MailService");
        FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");
        CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext.getBean("CutOffTimeService");
        
		CorpUser corpUser = (CorpUser) this.getUser();
		List recList = (List) this.getUsrSessionDataValue("recList");
	    Map fileRequest =(Map) this.getUsrSessionDataValue("fileRequest");
	    Map headInfo = (Map) this.getUsrSessionDataValue("headInfo");
	    String errorFlag = (String) this.getUsrSessionDataValue("errorFlag");
	    

		String assignedUser = Utils.null2EmptyWithTrim(this.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this.getParameter("mailUser"));
		
		String toCurrency = (String) this
				.getUsrSessionDataValue("toCurrency");

		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(),fileRequest.get("TO_CURRENCY").toString(), "MOP",
				new BigDecimal(fileRequest.get("TO_AMOUNT").toString()),
				null, 2);

		BigDecimal creditAmount = exRatesService.getEquivalent(corpUser
				.getCorpId(), fileRequest.get("TO_CURRENCY").toString(),
				toCurrency, new BigDecimal(fileRequest.get("TO_AMOUNT")
						.toString()), null, 2);
		
		 //	check value date cut-off time add by mxl 0130
		setMessage(cutOffTimeService.check("51XX", "",fileRequest.get("TO_CURRENCY").toString()));
		// add by chen_y for CR225  20170412
		if(errorFlag!=null && "Y".equals(errorFlag)){
			TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
			// add by mxl 1109
			CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
	
			List transIdList = new ArrayList();
			String batchId = fileRequest.get("BATCH_ID").toString();
			transIdList = transferBankMtSService.transIdListTransferBankStm(batchId);
			if (null != transIdList && transIdList.size() > 0) {
				for (int i = 0; i < transIdList.size(); i++) {
					Map transIdMap = new HashMap();
					String transId = null;
					transIdMap = (Map) transIdList.get(i);
					transId = (String) transIdMap.get("TRANS_ID");
					TransferBankStm transferBankStm = transferBankMtSService
							.viewInBANKStm(transId);
					CorpAccount corpAccount2 = corpAccountService
							.viewCorpAccount(transferBankStm
									.getFromAccount());
					String fromCurrency = corpAccount2.getCurrency();
					transferBankStm.setFromCurrency(fromCurrency);
	
					//  fromCurrency fromAmount add by mxl 1117
					BigDecimal fromAmount = exRatesService.getEquivalent(
							corpUser.getCorpId(), transferBankStm
									.getFromCurrency(), fileRequest.get(
									"TO_CURRENCY").toString(), null,
							new BigDecimal(transferBankStm.getDebitAmount()
									.toString()), 2);
											
					BigDecimal fromEquivalentMOP = exRatesService
							.getEquivalent(corpUser.getCorpId(),
									transferBankStm.getFromCurrency(),
									"MOP", new BigDecimal(fromAmount
											.toString()), null, 2);
			
					// add by mxl  daylimit add by mxl 1008
					if (!transferLimitService.checkLimitQuota(
							transferBankStm.getFromAccount(), corpUser
									.getCorpId(),
							Constants.TXN_TYPE_TRANSFER_BANK, fromAmount
									.doubleValue(), fromEquivalentMOP
									.doubleValue())) {
						// write limit report
						Map reportMap = new HashMap();
						reportMap.put("TRANS_DATE", DateTime.formatDate(
								new Date(), "yyyyMMdd"));
						reportMap.put("TRANS_TIME", DateTime.formatDate(
								new Date(), "HHmmss"));
						reportMap.put("USER_ID", corpUser.getUserId());
						reportMap.put("CORP_ID", corpUser.getCorpId());
						reportMap.put("TRANS_TYPE",
								Constants.TXN_TYPE_TRANSFER_BANK);
						reportMap.put("LOCAL_TRANS_CODE", transferBankStm
								.getTransId());
						reportMap.put("FROM_ACCOUNT", transferBankStm
								.getFromAccount());
						reportMap.put("FROM_CURRENCY", transferBankStm.getFromCurrency());									
						reportMap.put("TO_CURRENCY", toCurrency);
						reportMap.put("FROM_AMOUNT", fromAmount);
						reportMap.put("TO_AMOUNT", transferBankStm
								.getDebitAmount());
						reportMap.put("EXCEEDING_TYPE", "1");
						reportMap.put("LIMIT_TYPE", transferLimitService
								.getLimitType());
						reportMap.put("USER_LIMIT ", "0");
						reportMap.put("DAILY_LIMIT ", new Double(
								transferLimitService.getDailyLimit()));
						reportMap.put("TOTAL_AMOUNT ", new Double(
								transferLimitService.getTotalLimit()));
						UploadReporter
								.writeBuffer("RP_EXCLIMIT", reportMap);
						setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg").getString("warnning.txn.ExceedDailyLimit"));		
					}
				}
			}

			// add by li_zd at 20170608
			if (!transferLimitService.checkLimitQuotaByCorpId(corpUser.getCorpId(), 
					Constants.TXN_TYPE_TRANSFER_BANK, new BigDecimal(fileRequest.get("TO_AMOUNT").toString()).doubleValue(), equivalentMOP.doubleValue())) {
				// write limit report
//				Map reportMap = new HashMap();
//				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
//				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
//				reportMap.put("USER_ID", corpUser.getUserId());
//				reportMap.put("CORP_ID", corpUser.getCorpId());
//				reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_BANK);
//				reportMap.put("LOCAL_TRANS_CODE", transferBankStm.getTransId());
//				reportMap.put("FROM_ACCOUNT", transferBankStm.getFromAccount());
//				reportMap.put("FROM_CURRENCY", transferBankStm.getFromCurrency());
//				reportMap.put("TO_CURRENCY", toCurrency);
//				reportMap.put("FROM_AMOUNT", fromAmount);
//				reportMap.put("TO_AMOUNT", transferBankStm.getDebitAmount());
//				reportMap.put("EXCEEDING_TYPE", "1");
//				reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
//				reportMap.put("USER_LIMIT ", "0");
//				reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
//				reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
//				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
				setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg").getString("warnning.txn.ExceedDailyLimit"));		
			}// end
		}
		// add by chen_y for CR225  20170412 end
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_TRANSFER_BANK_NTO1,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				TransferInBankMtSAction.class, fileRequest.get("TO_CURRENCY")
						.toString(), (new BigDecimal(fileRequest.get(
						"TO_AMOUNT").toString())).doubleValue(), toCurrency,
				creditAmount.doubleValue(), equivalentMOP.doubleValue(),
				fileRequest.get("BATCH_ID").toString(), fileRequest.get(
						"REMARK").toString(), getUser(), assignedUser, corpUser
						.getCorporation().getAllowExecutor(),
				FlowEngineService.RULE_FLAG_TO);

	    FileRequest pojoFileRequest = requestService.viewFileRequest(fileRequest.get("BATCH_ID").toString());

	    try {
	    	transferBankMtSService.updateStatus(fileRequest.get("BATCH_ID").toString());
	    	Map resultData = new HashMap();

		    resultData.putAll(fileRequest);
		 	resultData.putAll(headInfo);
		 	convertPojo2Map(pojoFileRequest, resultData);
		 	resultData.put("recList", recList);
		 	this.setResultData(resultData);
            
		 	//Add by heyongjiang 2010-04-21
		 	String toAccountName = "";
		 	String toAccountName2 = "";
		 	//update by gan 20180111
//		 	toAccountName = fileRequest.get("TO_ACCOUNT_NAME").toString().trim();
//			toAccountName2 = fileRequest.get("TO_ACCOUNT_NAME2").toString().trim();
		 	
		/*	if("".equals(toAccountName2.trim())){
				toAccountName = fileRequest.get("TO_ACCOUNT_NAME").toString().trim();
			}else{
				toAccountName = (fileRequest.get("TO_ACCOUNT_NAME").toString() + toAccountName2).trim();
			}*/
		 	resultData.put("TO_ACCOUNT_NAME", toAccountName);
			resultData.put("TO_ACCOUNT_NAME2", toAccountName2);
			//add by heyongjiang end
			
		 	resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");

			resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK_NTO1);
			resultData.put("currency", fileRequest.get("TO_CURRENCY").toString());
			resultData.put("amount", new BigDecimal(fileRequest.get("TO_AMOUNT").toString()));
			resultData.put("amountMopEq", equivalentMOP);
			
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime",pojoFileRequest.getRequestTime());
			dataMap.put("transId",pojoFileRequest.getBatchId());
			dataMap.put("toCurrency",pojoFileRequest.getToCurrency());
			dataMap.put("amount",pojoFileRequest.getToAmount());
			dataMap.put("corpName",corpUser.getCorporation().getCorpName());
			dataMap.put("toAccount",pojoFileRequest.getToAccount());
			dataMap.put("remark",pojoFileRequest.getRemark());
			
			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_TRANSFER_BANK_NTO1, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_TRANSFER_BANK_NTO1, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */
			
		} catch (Exception e) {
			//clear unavailable db data
			transferBankMtSService.clearUnavailableDataByPaybatchId(fileRequest.get("BATCH_ID").toString());
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
		TransferBankMtSService transferBankMtSService = (TransferBankMtSService) appContext
				.getBean("TransferBankMtSService");

		Map fileRequest = (Map) this.getUsrSessionDataValue("fileRequest");

		transferBankMtSService.cancelUpload(fileRequest.get("BATCH_ID")
				.toString().trim(), null, null, new File(saveFilePath
				+ fileRequest.get("FILE_NAME").toString().trim()));
	}

	public String viewDetail(String txnType, String id, CibAction bean) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		RequestService requestService = (RequestService) appContext.getBean("RequestService");

		String viewPageUrl = "";
		Map resultData = bean.getResultData();
		CorpUser corpUser = (CorpUser) bean.getUser();
		FileRequest pojoFileRequest = requestService.viewFileRequest(id);
		viewPageUrl = "/WEB-INF/pages/bat/batch_transfer/upload_file_approval_viewBankMtS.jsp";
        //add by mxl 1208
		List recList = transferService.listTransferBankMtsByBatchid(id);
		recList = this.convertPojoList2MapList(recList);
		
		// 锟斤拷锟斤拷toAmount
		BigDecimal newToAmount = exRatesService.getEquivalent(corpUser
				.getCorpId(), pojoFileRequest.getToCurrency(),
				pojoFileRequest.getFromCurrency(), new BigDecimal(pojoFileRequest.getToAmount()
						.toString()), null, 2);
		
		// Jet add, show the exchange rate
		Map exchangeMap = exRatesService.getExchangeRate(corpUser
				.getCorpId(), pojoFileRequest.getToCurrency(),
				pojoFileRequest.getFromCurrency(), 7);
		
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
				.getCorpId(),pojoFileRequest.getToCurrency(), "MOP",
				new BigDecimal(pojoFileRequest.getToAmount().toString()),
				null, 2);

		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "currency");
		assignuser.put("amountField", "totalAmount");
		assignuser.put("amountMopEqField", "amountMopEq");
		assignuser.put("txnType", txnType);
		assignuser.put("amountMopEq", equivalentMOP);

		bean.convertPojo2Map(pojoFileRequest, resultData);
		// 锟斤拷锟斤拷锟斤拷莸锟絘ssignuser_tag
		//resultData.put("currency", pojoFileRequest.getFromCurrency());
		resultData.putAll(assignuser);
		resultData.put("recList", recList);
		resultData.put("newExchangeRate", ex_Rate);
		resultData.put("newToAmount", new Double(newToAmount.doubleValue()));				
		bean.setResultData(resultData);
		return viewPageUrl;
	}

	public boolean approve(String txnType, String id, CibAction bean) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		TransferBankMtSService transferBankMtSService = (TransferBankMtSService) appContext
				.getBean("TransferBankMtSService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");

		CorpUser corpUser = (CorpUser) bean.getUser();
		if (txnType != null) {
			FileRequest pojoFileRequest = requestService.viewFileRequest(id);
			
			String toAccount = pojoFileRequest.getToAccount().trim();
			
			CorpAccount corpAccount = corpAccountService
					.viewCorpAccount(toAccount);
			String toCurrency = corpAccount.getCurrency();

			BigDecimal creditAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), pojoFileRequest.getToCurrency(), toCurrency,
					new BigDecimal(pojoFileRequest.getToAmount().toString()),
					null, 2);

			// get fromEquivalentMOP, add by li_zd at 20170609
			BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
					.getCorpId(), pojoFileRequest.getToCurrency(), "MOP",
					new BigDecimal(pojoFileRequest.getToAmount().toString()),
					null, 2);
			// end
			pojoFileRequest.setFromAmount(new Double(creditAmount.toString()));
			requestService.updateFileRequest(pojoFileRequest);

			Map headInfo = new HashMap();
			headInfo.put("currency", pojoFileRequest.getCurrency());
			/*
			BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser.getCorpId(),
					pojoFileRequest.getFromCurrency(), "MOP",
					new BigDecimal(pojoFileRequest.getFromAmount().toString()), null, 2);
					*/
            //  batchId TransferBankStM transfer transId 
			List transIdList = new ArrayList();
			transIdList = transferBankMtSService.transIdListTransferBankStm(id);
		    if (null != transIdList && transIdList.size() > 0) {
		     for (int i = 0; i < transIdList.size(); i++) {
					Map transIdMap = new HashMap();
					String transId = null;
					transIdMap = (Map) transIdList.get(i);
					transId = (String) transIdMap.get("TRANS_ID");
					TransferBankStm transferBankStm = transferBankMtSService
							.viewInBANKStm(transId);
					// fromCurrency
					CorpAccount corpAccount2 = corpAccountService
							.viewCorpAccount(transferBankStm.getFromAccount());
					String fromCurrency = corpAccount2.getCurrency();
					transferBankStm.setFromCurrency(fromCurrency);

					//  fromCurrency fromAmount add by mxl 1117
					BigDecimal fromAmount = exRatesService.getEquivalent(
							corpUser.getCorpId(), transferBankStm
									.getFromCurrency(), pojoFileRequest
									.getToCurrency(), null,
							new BigDecimal(transferBankStm.getDebitAmount()
									.toString()), 2);

					BigDecimal fromEquivalentMOP = exRatesService
							.getEquivalent(corpUser.getCorpId(), transferBankStm
									.getFromCurrency(), "MOP",
									new BigDecimal(transferBankStm
											.getDebitAmount().toString()),
									null, 2);

					// add by mxl daylimit fromAccout 
					if (!transferLimitService.checkLimitQuota(transferBankStm
							.getFromAccount(), corpUser.getCorpId(),
							Constants.TXN_TYPE_TRANSFER_BANK, transferBankStm
									.getDebitAmount().doubleValue(),
							fromEquivalentMOP.doubleValue())) {
						// write limit report
						Map reportMap = new HashMap();
						reportMap.put("TRANS_DATE", DateTime.formatDate(
								new Date(), "yyyyMMdd"));
						reportMap.put("TRANS_TIME", DateTime.formatDate(
								new Date(), "HHmmss"));
						reportMap.put("USER_ID", corpUser.getUserId());
						reportMap.put("CORP_ID", corpUser.getCorpId());
						reportMap.put("TRANS_TYPE",
								Constants.TXN_TYPE_TRANSFER_BANK);
						reportMap.put("LOCAL_TRANS_CODE", transferBankStm
								.getTransId());
						reportMap.put("FROM_ACCOUNT", transferBankStm
								.getFromAccount());
						reportMap.put("FROM_CURRENCY", transferBankStm
								.getFromCurrency());
						reportMap.put("TO_CURRENCY", toCurrency);
						reportMap.put("FROM_AMOUNT", fromAmount);
						reportMap.put("TO_AMOUNT", creditAmount);
						reportMap.put("EXCEEDING_TYPE", "1");
						reportMap.put("LIMIT_TYPE", transferLimitService
								.getLimitType());
						reportMap.put("USER_LIMIT ", "0");
						reportMap.put("DAILY_LIMIT ", new Double(
								transferLimitService.getDailyLimit()));
						reportMap.put("TOTAL_AMOUNT ", new Double(
								transferLimitService.getTotalLimit()));
						UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
						throw new NTBWarningException(
								"err.txn.ExceedDailyLimit");
					}
				}
				// add by li_zd at 20170608
				if (!transferLimitService.checkLimitQuotaByCorpId(corpUser.getCorpId(), 
						Constants.TXN_TYPE_TRANSFER_BANK, new BigDecimal(pojoFileRequest.getToAmount().toString()).doubleValue(), equivalentMOP.doubleValue())) {
					// write limit report
//					Map reportMap = new HashMap();
//					reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
//					reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
//					reportMap.put("USER_ID", corpUser.getUserId());
//					reportMap.put("CORP_ID", corpUser.getCorpId());
//					reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_BANK);
//					reportMap.put("LOCAL_TRANS_CODE", transferBankStm.getTransId());
//					reportMap.put("FROM_ACCOUNT", transferBankStm.getFromAccount());
//					reportMap.put("FROM_CURRENCY", transferBankStm.getFromCurrency());
//					reportMap.put("TO_CURRENCY", toCurrency);
//					reportMap.put("FROM_AMOUNT", fromAmount);
//					reportMap.put("TO_AMOUNT", transferBankStm.getDebitAmount());
//					reportMap.put("EXCEEDING_TYPE", "1");
//					reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
//					reportMap.put("USER_LIMIT ", "0");
//					reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
//					reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
//					UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
					throw new NTBWarningException("err.txn.ExceedDailyLimit");		
				}// end
			}

			transferBankMtSService.approveTransferBankMtS(pojoFileRequest, corpUser);
			//add by linrui to host after upload file
			transferBankMtSService.toHostTransferSTM(pojoFileRequest,
					corpUser, txnType);
			//end
            //send email to last approver or executor, add by wen_cy 20110511
			MailService mailService = (MailService) appContext.getBean("MailService");
			Map dataMap = new HashMap();
			// get summary info
			this.convertPojo2Map(pojoFileRequest, dataMap);
			dataMap.put("lastUpdateTime", new Date());
            // get bene detail info
			TransferService transferService = (TransferService) appContext.getBean("TransferService");
			List beneList_temp = transferService.listTransferBankStmByBatchid(pojoFileRequest.getBatchId());
			List beneList = new ArrayList();
			for (int i = 0; i < beneList_temp.size(); i++){
				Map recordMap = new HashMap();
				this.convertPojo2Map(beneList_temp.get(i),recordMap);
				beneList.add(recordMap);
			}
			dataMap.put("beneList", beneList);
			
			/* Modify by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
			/*mailService.toLastApprover_Executor(Constants.TXN_SUBTYPE_TRANSFER_BANK_NTO1, corpUser.getUserId(), dataMap);*/
			mailService.toLastApprover_Executor(Constants.TXN_SUBTYPE_TRANSFER_BANK_NTO1, corpUser.getUserId(),corpUser.getCorpId(), dataMap);
			/* Modify by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
			return true;
		} else {
			return false;
		}
	}

	public boolean cancel(String txnType, String id, CibAction bean) throws NTBException {
		return this.reject(txnType, id, bean);
	}

	public boolean reject(String txnType, String id, CibAction bean) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferBankMtSService transferBankMtSService = (TransferBankMtSService) appContext.getBean("TransferBankMtSService");
		RequestService requestService = (RequestService) appContext.getBean("RequestService");
		if (txnType != null) {
			FileRequest pojoFileRequest = requestService.viewFileRequest(id);
			transferBankMtSService.rejectFileRequest(pojoFileRequest);
			return true;
		} else {
			return false;
		}
	}



}
