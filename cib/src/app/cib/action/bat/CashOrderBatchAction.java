package app.cib.action.bat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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

import app.cib.bo.bat.FileRequest;
import app.cib.bo.bat.FileRequestFileBean;
import app.cib.bo.srv.ReqBankDraft;
import app.cib.bo.srv.ReqCashierOrder;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.service.bat.CashOrderBatchService;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.srv.RequestService;
import app.cib.service.sys.ApproveRuleService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.CutOffTimeService;
import app.cib.service.sys.MailService;
import app.cib.service.txn.TransferLimitService;
import app.cib.service.txn.TransferService;
import app.cib.util.Constants;
import app.cib.util.UploadReporter;
import com.neturbo.set.exception.NTBWarningException;

public class CashOrderBatchAction extends CibAction implements Approvable {
	private static final String saveFilePath = Config.getProperty("BatchFileUploadDir") + "/";
	public void uploadFileLoad() throws NTBException {
		Map resultData = new HashMap(1);
		this.setResultData(resultData);
		ApplicationContext appContext = Config.getAppContext();
		CashOrderBatchService cashOrderBatchService = (CashOrderBatchService) appContext
				.getBean("CashOrderBatchService");
		CorpUser corpUser = (CorpUser) this.getUser();
		// clear unavailable db data
		cashOrderBatchService
				.clearUnavailableDataByCorpId(corpUser.getCorpId());
	}

	public void uploadFile() throws NTBException {
		// 锟斤拷始锟斤拷Service
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		CashOrderBatchService cashOrderBatchService = (CashOrderBatchService) appContext
				.getBean("CashOrderBatchService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		// add by lw 20101015
		TransferService transferService = (TransferService) appContext
		.getBean("TransferService");
		// add by lw end
		CorpUser corpUser = (CorpUser) this.getUser();
		String errFlag = "N";
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
		FileRequestFileBean fileRequestFileBean = cashOrderBatchService
				.parseFile(corpUser, inStream);
		Map fileRequest = null;

		// add by mxl 1220
		try {
		List normalList = fileRequestFileBean.getNormalList();
		List errList = fileRequestFileBean.getErrList();
		double normalTotalAmt = fileRequestFileBean.getNormalTotalAmt();
		double errTotalAmt = fileRequestFileBean.getErrTotalAmt();
		int allCount = fileRequestFileBean.getAllCount();
		fileRequest = fileRequestFileBean.getFileRequestHeader();
		Map headInfo = new HashMap();
		Map resultData = new HashMap();
		// if error
		if (errList.size() != 0) {
			errFlag = "Y";
		} else if (normalTotalAmt + errTotalAmt != Double
				.parseDouble(fileRequest.get("TO_AMOUNT").toString())) {
			errFlag = "Y";
            //edit by mxl 1219
			setError(new NTBException("err.bat.AmountNotEqual"));
		} else if (allCount != Integer.parseInt(fileRequest.get("TOTAL_NUMBER")
				.toString())) {
			errFlag = "Y";
			setError(new NTBException("err.bat.CountNotEqual"));
		}
		headInfo.put("allCount", new Integer(allCount));
		headInfo.put("totalAmount", new Double(normalTotalAmt + errTotalAmt));

		String fromAccount = fileRequest.get("FROM_ACCOUNT").toString().trim();
        //锟斤拷锟�From Currency 锟侥憋拷锟街猴拷锟斤拷锟斤拷
		CorpAccount corpAccount = corpAccountService.viewCorpAccount(fromAccount);
		String fromCurrency = corpAccount.getCurrency();
        //锟斤拷锟斤拷fromAmount edit by mxl 1218
		BigDecimal debitAmount = exRatesService.getEquivalent(corpUser
				.getCorpId(), fromCurrency, fileRequest.get("TO_CURRENCY").toString(), null, new BigDecimal(fileRequest.get("TO_AMOUNT").toString()), 2);
		// 锟斤拷锟斤拷洗锟斤拷募锟斤拷锟矫伙拷写锟斤拷锟斤拷录锟斤拷锟斤拷锟斤拷要锟斤拷锟斤拷权准锟斤拷锟斤拷锟�锟斤拷锟斤拷锟斤拷锟斤拷 1201
		if (errList.size() == 0) {

			String batchId = (String) fileRequest.get("BATCH_ID");
			FileRequest pojoFileRequest = requestService
					.viewFileRequest(batchId);
			//add 1227
			pojoFileRequest.setFromCurrency(fromCurrency);
			pojoFileRequest.setFromAmount(new Double(debitAmount.toString()));
			pojoFileRequest.setRequestTime(new Date());

			// 锟斤拷锟斤拷榷锟斤拷媳锟�
			BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
					.getCorpId(), pojoFileRequest.getFromCurrency(), "MOP",
					new BigDecimal(pojoFileRequest.getFromAmount().toString()),
					null, 2);
			// 锟叫断斤拷锟斤拷锟睫讹拷

			if (!corpUser.checkUserLimit(Constants.TXN_TYPE_CASHIER_ORDER,
					equivalentMOP)) {
				// write limit report
				Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
						"yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
						"HHmmss"));
				reportMap.put("USER_ID", pojoFileRequest.getUserId());
				reportMap.put("CORP_ID", corpUser.getCorpId());
				reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_CASHIER_ORDER);
				reportMap.put("LOCAL_TRANS_CODE", pojoFileRequest.getBatchId());
				reportMap.put("FROM_CURRENCY", pojoFileRequest
						.getFromCurrency());
				reportMap.put("FROM_ACCOUNT", pojoFileRequest.getFromAccount());
				reportMap.put("TO_CURRENCY", pojoFileRequest.getToCurrency());
				reportMap.put("FROM_AMOUNT", pojoFileRequest.getFromAmount());
				// reportMap.put("TO_AMOUNT", fileRequest.getToAccount());
				reportMap.put("EXCEEDING_TYPE", "2");
				reportMap.put("LIMIT_TYPE", "");
				reportMap.put("USER_LIMIT ", corpUser
						.getUserLimit(Constants.TXN_TYPE_CASHIER_ORDER));
				reportMap.put("DAILY_LIMIT ", new Double(0));
				reportMap.put("TOTAL_AMOUNT ", new Double(0));
				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
				throw new NTBWarningException("err.txn.ExceedUserLimit");
			}
			
			// add by mxl 锟斤拷锟絛aylimit
			if (!transferLimitService.checkCurAmtLimitQuota(pojoFileRequest
					.getFromAccount(), corpUser.getCorpId(),
					Constants.TXN_TYPE_CASHIER_ORDER, pojoFileRequest
							.getFromAmount().doubleValue(), equivalentMOP
							.doubleValue())) {
				// write limit report
				Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
						"yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
						"HHmmss"));
				reportMap.put("USER_ID", pojoFileRequest.getUserId());
				reportMap.put("CORP_ID", corpUser.getCorpId());
				reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_CASHIER_ORDER);
				reportMap.put("LOCAL_TRANS_CODE", pojoFileRequest.getBatchId());
				reportMap.put("FROM_CURRENCY", pojoFileRequest
						.getFromCurrency());
				reportMap.put("FROM_ACCOUNT", pojoFileRequest.getFromAccount());
				reportMap.put("TO_CURRENCY", pojoFileRequest.getToCurrency());
				reportMap.put("FROM_AMOUNT", pojoFileRequest.getFromAmount());
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
			// add by lw 20101015
			// check Need purpose of proof
			//modified by lzg 20190602
			/*if(null != normalList && normalList.size() > 0){
				for (int i = 0; i < normalList.size(); i++) {
					Map tempMap = (Map) normalList.get(i);
					String cashierAmount = String.valueOf(tempMap.get("CASHIER_AMOUNT"));					
					int checkPurpose = transferService.checkNeedPurpose(pojoFileRequest.getCorpId(),
							pojoFileRequest.getFromAccount(), cashierAmount, null, pojoFileRequest.getFromCurrency(),
							pojoFileRequest.getToCurrency(), "MO");
					if(checkPurpose == 2){
						resultData.put("needProof", "Y");
					}
				}
			}*/
			//modified by lzg end
			// add by lw end
			// 为SelectUser Tag准锟斤拷锟斤拷锟�
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");

			resultData.put("txnType", Constants.TXN_SUBTYPE_CASHIER_ORDER);
			resultData.put("currency", pojoFileRequest.getToCurrency());
			resultData.put("amount", pojoFileRequest.getToAmount());
			resultData.put("amountMopEq", equivalentMOP);
			pojoFileRequest.setRequestTime(new Date());
			ApproveRuleService approveRuleService = (ApproveRuleService) Config
					.getAppContext().getBean("ApproveRuleService");
			if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
					resultData)) {
				throw new NTBException("err.flow.ApproveRuleNotDefined");
			}
            // add by mxl 1227 update FileQequest锟斤拷
			requestService.updateFileRequest(pojoFileRequest);
			this.setUsrSessionDataValue("pojoFileRequest", pojoFileRequest);
		}
		resultData.putAll(fileRequest);
		resultData.putAll(headInfo);
		resultData.put("errFlag", errFlag);
		resultData.put("rightRecordNo", String.valueOf(normalList.size()));
		resultData.put("errRecordNo", String.valueOf(errList.size()));
		if (errList.size() == 0) {
			resultData.put("recList", normalList);
		} else {
			resultData.put("recList", errList);
		}
		resultData.put("fromCurrency", fromCurrency);
		resultData.put("fromAccount", fromAccount);
		resultData.put("fromAmount", debitAmount);
		resultData.put("errTotalAmt", new Double(errTotalAmt));
		this.setResultData(resultData);
		this.setUsrSessionDataValue("fromCurrency", fromCurrency);
		this.setUsrSessionDataValue("fileRequest", fileRequest);
		this.setUsrSessionDataValue("headInfo", headInfo);
		this.setUsrSessionDataValue("recList", normalList);
		} catch (Exception e) {
			cashOrderBatchService.cancelUpload(fileRequest.get("BATCH_ID").toString().trim(), null, null, new File(saveFilePath + fileRequest.get("FILE_NAME").toString().trim()));
			throw new NTBException (e.getMessage());
		}

	}

	public void uploadFileConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CashOrderBatchService cashOrderBatchService = (CashOrderBatchService) appContext
				.getBean("CashOrderBatchService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext.getBean("CutOffTimeService");
		MailService mailService = (MailService) appContext.getBean("MailService");
		CorpUser corpUser = (CorpUser) this.getUser();

		List recList = (List) this.getUsrSessionDataValue("recList");
		Map fileRequest = (Map) this.getUsrSessionDataValue("fileRequest");
		Map headInfo = (Map) this.getUsrSessionDataValue("headInfo");
		FileRequest pojoFileRequest = (FileRequest) this
				.getUsrSessionDataValue("pojoFileRequest");
		requestService.updateFileRequest(pojoFileRequest);
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), pojoFileRequest.getFromCurrency(), "MOP",
				new BigDecimal(pojoFileRequest.getFromAmount().toString()),
				null, 2);
		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));
        //check value date cut-off time add by mxl 0130
		setMessage(cutOffTimeService.check("XC06", pojoFileRequest.getFromCurrency(),pojoFileRequest.getToCurrency()));
		//add by chen_y for CR225 20170412
		// add by mxl 锟斤拷锟絛aylimit
		
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		
		if (!transferLimitService.checkLimitQuota(pojoFileRequest
				.getFromAccount(), corpUser.getCorpId(),
				Constants.TXN_TYPE_CASHIER_ORDER, pojoFileRequest
						.getFromAmount().doubleValue(), equivalentMOP
						.doubleValue())) {
			// write limit report
			Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
					"yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
					"HHmmss"));
			reportMap.put("USER_ID", pojoFileRequest.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_CASHIER_ORDER);
			reportMap.put("LOCAL_TRANS_CODE", pojoFileRequest.getBatchId());
			reportMap.put("FROM_CURRENCY", pojoFileRequest
					.getFromCurrency());
			reportMap.put("FROM_ACCOUNT", pojoFileRequest.getFromAccount());
			reportMap.put("TO_CURRENCY", pojoFileRequest.getToCurrency());
			reportMap.put("FROM_AMOUNT", pojoFileRequest.getFromAmount());
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
			//throw new NTBWarningException("err.txn.ExceedDailyLimit");
			setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg").getString("warnning.txn.ExceedDailyLimit"));
		}
		//add by chen_y for CR225 20170412 end
		
		// 锟铰斤拷一锟斤拷锟斤拷权锟斤拷锟斤拷 add by mxl 1109
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_CASHIER_ORDER,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				CashOrderBatchAction.class, pojoFileRequest.getFromCurrency(),
				pojoFileRequest.getFromAmount().doubleValue(), pojoFileRequest
						.getToCurrency(), pojoFileRequest.getToAmount()
						.doubleValue(), equivalentMOP.doubleValue(),
				pojoFileRequest.getBatchId(), pojoFileRequest.getRemark(),
				getUser(), assignedUser, corpUser.getCorporation()
						.getAllowExecutor(), FlowEngineService.RULE_FLAG_TO);
		try {
			// 锟斤拷锟揭伙拷锟斤拷锟叫达拷锟斤拷锟捷匡拷
			cashOrderBatchService.updateStatus(pojoFileRequest.getBatchId());
			Map resultData = new HashMap();

			resultData.putAll(fileRequest);
			resultData.putAll(headInfo);
			convertPojo2Map(pojoFileRequest, resultData);
			resultData.put("recList", recList);
			this.setResultData(resultData);
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");

			resultData.put("txnType", Constants.TXN_SUBTYPE_CASHIER_ORDER);
			resultData.put("currency", pojoFileRequest.getToCurrency());
			resultData.put("amount", pojoFileRequest.getToAmount());
			resultData.put("amountMopEq", equivalentMOP);


			//setResultData(resultData);
             //send mial to approver
			// 锟斤拷锟斤拷锟绞硷拷 add by mxl 1225
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime",pojoFileRequest.getRequestTime());
			dataMap.put("batchId",pojoFileRequest.getBatchId());
			dataMap.put("fromCurrency",pojoFileRequest.getFromCurrency());
			dataMap.put("fromAmount",pojoFileRequest.getFromAmount());
			dataMap.put("fromAccount",pojoFileRequest.getFromAccount());
			dataMap.put("corpName",corpUser.getCorporation().getCorpName());
			dataMap.put("remark",pojoFileRequest.getRemark());
			
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_CASHIER_ORDER, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_CASHIER_ORDER, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
		
		} catch (Exception e) {
			// clear unavailable db data
			cashOrderBatchService.clearUnavailableDataByPaybatchId(fileRequest
					.get("BATCH_ID").toString());
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
		CashOrderBatchService cashOrderBatchService = (CashOrderBatchService) appContext
				.getBean("CashOrderBatchService");

		Map fileRequest = (Map) this.getUsrSessionDataValue("fileRequest");

		cashOrderBatchService.cancelUpload(fileRequest.get("BATCH_ID")
				.toString().trim(), null, null, new File(saveFilePath
				+ fileRequest.get("FILE_NAME").toString().trim()));
	}
	
	public boolean approve(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		CashOrderBatchService cashOrderBatchService = (CashOrderBatchService) appContext
				.getBean("CashOrderBatchService");
		CorpUser corpUser = (CorpUser) bean.getUser();
		if (txnType != null) {
			FileRequest pojoFileRequest = requestService.viewFileRequest(id);
			BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
					.getCorpId(), pojoFileRequest.getFromCurrency(), "MOP",
					new BigDecimal(pojoFileRequest.getFromAmount().toString()),
					null, 2);
			if (!transferLimitService.checkLimitQuota(pojoFileRequest
					.getFromAccount(), corpUser.getCorpId(),
					Constants.TXN_TYPE_CASHIER_ORDER, pojoFileRequest
							.getFromAmount().doubleValue(), equivalentMOP
							.doubleValue())) {
				// write limit report
				Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
						"yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
						"HHmmss"));
				reportMap.put("USER_ID", pojoFileRequest.getUserId());
				reportMap.put("CORP_ID", corpUser.getCorpId());
				reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_CASHIER_ORDER);
				reportMap.put("LOCAL_TRANS_CODE", pojoFileRequest.getBatchId());
				reportMap.put("FROM_CURRENCY", pojoFileRequest
						.getFromCurrency());
				reportMap.put("TO_CURRENCY", pojoFileRequest.getToCurrency());
				reportMap.put("FROM_AMOUNT", pojoFileRequest.getFromAmount());
				reportMap.put("TO_AMOUNT", pojoFileRequest.getToAmount());
				reportMap.put("EXCEEDING_TYPE", "1");
				reportMap
						.put("LIMIT_TYPE", transferLimitService.getLimitType());
				reportMap.put("FROM_ACCOUNT", pojoFileRequest.getFromAccount());
				reportMap.put("USER_LIMIT ", "0");
				reportMap.put("DAILY_LIMIT ", new Double(transferLimitService
						.getDailyLimit()));
				reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService
						.getTotalLimit()));
				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
				throw new NTBWarningException("err.txn.ExceedDailyLimit");
			}
			cashOrderBatchService.approveCashOrder(pojoFileRequest, corpUser,
					equivalentMOP);
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
		CashOrderBatchService cashOrderBatchService = (CashOrderBatchService) appContext
				.getBean("CashOrderBatchService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		if (txnType != null) {
			FileRequest pojoFileRequest = requestService.viewFileRequest(id);
			cashOrderBatchService.rejectFileRequest(pojoFileRequest);
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
		// add by lw 20101016
		TransferService transferService = (TransferService) appContext
		.getBean("TransferService");
		// add by lw end
		String viewPageUrl = "";
		Map resultData = bean.getResultData();
		CorpUser corpUser = (CorpUser) bean.getUser();
		FileRequest pojoFileRequest = requestService.viewFileRequest(id);
		viewPageUrl = "/WEB-INF/pages/bat/batch_request/upload_file_approval_viewCashierOrder.jsp";

		//add by hjs : list detail
		//modified by lzg 20190602
		List recList = requestService.listCashierOrderByBatchid(id);
		/*if (null != recList && recList.size() > 0){
			for (int i = 0; i < recList.size(); i++) {
				ReqCashierOrder reqCashierOrder = (ReqCashierOrder) recList.get(i);
				String cashierAmount = reqCashierOrder.getCashierAmount().toString();					
				int checkPurpose = transferService.checkNeedPurpose(pojoFileRequest.getCorpId(),
						pojoFileRequest.getFromAccount(), cashierAmount, null, pojoFileRequest.getFromCurrency(),
						pojoFileRequest.getToCurrency(), "MO");
				if(checkPurpose == 2){
					resultData.put("needProof", "Y");
				}
			}
		}*/
		//modified by lzg end
		recList = this.convertPojoList2MapList(recList);

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
		resultData.put("currency", pojoFileRequest.getCurrency());
		resultData.putAll(assignuser);
		resultData.put("recList", recList);
		bean.setResultData(resultData);
		return viewPageUrl;
	}

}
