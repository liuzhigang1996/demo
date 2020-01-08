/**
 * @author hjs
 * 2006-10-10
 */
package app.cib.action.bat;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.context.ApplicationContext;

import com.jsax.service.util.FtpUtil;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bat.Payroll;
import app.cib.bo.bat.PayrollFileBean;
import app.cib.bo.sys.CorpUser;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.service.bat.PayrollService;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.CutOffTimeService;
import app.cib.service.sys.MailService;
import app.cib.service.txn.TransferLimitService;
import app.cib.util.Constants;
import app.cib.util.ErrConstants;
import app.cib.util.UploadReporter;
import com.neturbo.set.exception.NTBWarningException;

/**
 * @author hjs
 * 2006-10-10
 */
public class PayrollAction extends CibAction implements Approvable {
	private static final String saveFilePath = Config.getProperty("BatchFileUploadDir") + "/";

	public void uploadFileLoad() throws NTBException {
		//add by linrui 20180313
		//setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg",Constants.US).getString("warnning.transfer.DifferenceCcy"));
		Map resultData = new HashMap(1);
		this.setResultData(resultData);

		this.clearUsrSessionDataValue();

		ApplicationContext appContext = Config.getAppContext();
		PayrollService payrollService = (PayrollService) appContext.getBean("PayrollService");

		CorpUser corpUser = (CorpUser) this.getUser();
		//clear unavailable db data
		payrollService.clearUnavailableDataByCorpId(corpUser.getCorpId());
	}

	public void uploadFile() throws NTBException {

		ApplicationContext appContext = Config.getAppContext();
		PayrollService payrollService = (PayrollService) appContext.getBean("PayrollService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");

		String errFlag = "N";

		CorpUser corpUser = (CorpUser) this.getUser();

		//锟叫断斤拷锟斤拷锟角凤拷锟斤拷执锟叫癸拷媒锟斤拷锟�
		//payrollService.isDoable(corpUser.getCorpId());

		//String fileName = this.getUploadFileName();
		InputStream inStream;
		try {
			inStream = this.getUploadFileInputStream();
		} catch (FileNotFoundException e) {
			Log.error("", e);
			throw new NTBException("err.bat.UploadFileNotFound");
		} catch (IOException e) {
			Log.error("", e);
			throw new NTBException("err.bat.UploadFailed");
		}

		PayrollFileBean payrollFileInfo = payrollService.parseFile(corpUser, inStream);

		Map payroll = null;
		try {
			payroll = payrollFileInfo.getPayrollHeader();
			List normalList = payrollFileInfo.getNormalList();
			List errList = payrollFileInfo.getErrList();
			BigDecimal normalTotalAmt = payrollFileInfo.getNormalTotalAmt();
			BigDecimal errTotalAmt = payrollFileInfo.getErrTotalAmt();
			int allCount = payrollFileInfo.getAllCount();

			// if error
			if (errList.size() != 0) {
				errFlag = "Y";
			} else if (normalTotalAmt.add(errTotalAmt).compareTo(new BigDecimal(payroll.get("TOTAL_AMOUNT").toString())) != 0) {
				errFlag = "Y";
			} else if (allCount != Integer.parseInt(payroll.get("TOTAL_NUMBER").toString())) {
				errFlag = "Y";
			}

			String currency = corpAccountService.getCurrency(payroll.get("ORIGINATOR_AC_NO").toString(),true);

			// 锟斤拷织assignuser_tag锟斤拷锟�
			Map assignuser = new HashMap();
			BigDecimal amountMopEq = exRatesService.getEquivalent(corpUser.getCorpId(),
					currency, "MOP",
					new BigDecimal(payroll.get("TOTAL_AMOUNT").toString()),
					null, 2);
			assignuser.put("txnTypeField", "txnType");
			assignuser.put("currencyField", "currency");
			assignuser.put("amountField", "TOTAL_AMOUNT");
			assignuser.put("amountMopEqField", "amountMopEq");
			assignuser.put("txnType", Constants.TXN_SUBTYPE_PAYROLL);
			assignuser.put("amountMopEq", amountMopEq);

			// Check User Limit
			if (!corpUser.checkUserLimit(Constants.TXN_TYPE_PAYROLL, amountMopEq)) {

				// write limit report
	        	Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("USER_ID", payroll.get("USER_ID"));
				reportMap.put("CORP_ID", payroll.get("CORP_ID"));
				reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
				reportMap.put("LOCAL_TRANS_CODE", payroll.get("PAYROLL_ID"));
				reportMap.put("FROM_ACCOUNT", payroll.get("ORIGINATOR_AC_NO"));
				reportMap.put("FROM_CURRENCY", currency);
				reportMap.put("TO_CURRENCY", currency);
				reportMap.put("FROM_AMOUNT", payroll.get("TOTAL_AMOUNT"));
				reportMap.put("TO_AMOUNT", payroll.get("TOTAL_AMOUNT"));
				reportMap.put("EXCEEDING_TYPE", "2");
				reportMap.put("LIMIT_TYPE", "");
				reportMap.put("USER_LIMIT ", corpUser.getUserLimit(Constants.TXN_TYPE_PAYROLL));
				reportMap.put("DAILY_LIMIT ", new Double(0));
				reportMap.put("TOTAL_AMOUNT ", new Double(0));
				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

				throw new NTBWarningException("err.txn.ExceedUserLimit");
			}
			
			// check Limit
			if (!transferLimitService.checkCurAmtLimitQuota(payroll.get("ORIGINATOR_AC_NO").toString(),
					corpUser.getCorpId(),
					Constants.TXN_TYPE_PAYROLL,
					Double.parseDouble(payroll.get("TOTAL_AMOUNT").toString()),
					amountMopEq.doubleValue())) {

				// write limit report
	        	Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("USER_ID", payroll.get("USER_ID"));
				reportMap.put("CORP_ID", payroll.get("CORP_ID"));
				reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
				reportMap.put("LOCAL_TRANS_CODE", payroll.get("PAYROLL_ID"));
				reportMap.put("FROM_ACCOUNT", payroll.get("ORIGINATOR_AC_NO"));
				reportMap.put("FROM_CURRENCY", currency);
				reportMap.put("TO_CURRENCY", currency);
				reportMap.put("FROM_AMOUNT", payroll.get("TOTAL_AMOUNT"));
				reportMap.put("TO_AMOUNT", payroll.get("TOTAL_AMOUNT"));
				reportMap.put("EXCEEDING_TYPE", "1");
				reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
				reportMap.put("USER_LIMIT ", "0");
				reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
				reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

				throw new NTBWarningException("err.txn.ExceedDailyLimit");
			}

			/*
			headInfo.put("errList", errList);
			headInfo.put("rightRecordNo", new Integer(normalList.size()));
			headInfo.put("errRecordNo", new Integer(errList.size()));
			headInfo.put("errTotalAmt", new Double(errTotalAmt));
			*/
			Map headInfo = new HashMap();
			headInfo.put("currency", currency);
			headInfo.put("allCount", new Integer(allCount));
			headInfo.put("totalAmount", normalTotalAmt.add(errTotalAmt));

			Map resultData = new HashMap();
			resultData.putAll(payroll);
			resultData.putAll(headInfo);
			// 锟斤拷锟斤拷锟斤拷莸锟絘ssignuser_tag
			resultData.putAll(assignuser);
			resultData.put("errFlag", errFlag);
			resultData.put("errList", errList);
			resultData.put("rightRecordNo", new Integer(normalList.size()));
			resultData.put("errRecordNo", new Integer(errList.size()));
			this.setResultData(resultData);

			this.setUsrSessionDataValue("assignuser", assignuser);
			this.setUsrSessionDataValue("headInfo", headInfo);
			this.setUsrSessionDataValue("payroll", payroll);

		} catch (Exception e) {
			payrollService.cancelUpload(payroll.get("PAYROLL_ID").toString().trim(),
					null, null, new File(saveFilePath + payroll.get("FILE_NAME").toString().trim()));
			throw new NTBException (e.getMessage());
		}

	}

	public void uploadFileCancel() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		PayrollService payrollService = (PayrollService) appContext.getBean("PayrollService");
		
		Map resultData = this.getResultData();
		String payrollId = resultData.get("PAYROLL_ID").toString();
		
		payrollService.cancelUpload(payrollId, null, null, 
				new File(saveFilePath + resultData.get("FILE_NAME").toString().trim()));

		this.setResultData(new HashMap(1));
		this.clearUsrSessionDataValue();
	}

	public void uploadFileCfm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		PayrollService payrollService = (PayrollService) appContext.getBean("PayrollService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");
		MailService mailService = (MailService) appContext.getBean("MailService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext.getBean("CutOffTimeService");

		CorpUser corpUser = (CorpUser) this.getUser();
				
		Map assignuser = (Map) this.getUsrSessionDataValue("assignuser");
		Map headInfo = (Map) this.getUsrSessionDataValue("headInfo");
		Map payroll = (Map) this.getUsrSessionDataValue("payroll");

		String payrollId = payroll.get("PAYROLL_ID").toString();

		// check value date cut-off time
		setMessage(cutOffTimeService.check("PYRL", "",
				corpAccountService.getCurrency(payroll.get("ORIGINATOR_AC_NO").toString(),true)));
		
		//add by chen_y for chen_y 20170412
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		//ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
		
		//String currency = corpAccountService.getCurrency(payroll.get("ORIGINATOR_AC_NO").toString());
		String currency = (String) headInfo.get("currency");
		BigDecimal amountMopEq = (BigDecimal) assignuser.get("amountMopEq");
		
		// check Limit
		if (!transferLimitService.checkLimitQuota(payroll.get("ORIGINATOR_AC_NO").toString(),
				corpUser.getCorpId(),
				Constants.TXN_TYPE_PAYROLL,
				Double.parseDouble(payroll.get("TOTAL_AMOUNT").toString()),
				amountMopEq.doubleValue())) {

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", payroll.get("USER_ID"));
			reportMap.put("CORP_ID", payroll.get("CORP_ID"));
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", payroll.get("PAYROLL_ID"));
			reportMap.put("FROM_ACCOUNT", payroll.get("ORIGINATOR_AC_NO"));
			reportMap.put("FROM_CURRENCY", currency);
			reportMap.put("TO_CURRENCY", currency);
			reportMap.put("FROM_AMOUNT", payroll.get("TOTAL_AMOUNT"));
			reportMap.put("TO_AMOUNT", payroll.get("TOTAL_AMOUNT"));
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			//throw new NTBWarningException("err.txn.ExceedDailyLimit");
			setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg").getString("warnning.txn.ExceedDailyLimit"));
			
		}
		//add by chen_y for chen_y 20170412 end

		// 锟斤拷织assignuser_tag锟斤拷锟�
		String txnType = (String) assignuser.get("txnType");
		
		String assignedUser = Utils.null2EmptyWithTrim(this.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this.getParameter("mailUser"));
		// 锟铰斤拷一锟斤拷锟斤拷权锟斤拷锟斤拷
		String processId = flowEngineService.startProcess(txnType,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				PayrollAction.class,
				headInfo.get("currency").toString(),
				Double.parseDouble(payroll.get("TOTAL_AMOUNT").toString()),
				headInfo.get("currency").toString(),
				Double.parseDouble(payroll.get("TOTAL_AMOUNT").toString()),
				amountMopEq.doubleValue(),
				payroll.get("PAYROLL_ID").toString(),
				"", getUser(), assignedUser,
				corpUser.getCorporation().getAllowExecutor(), FlowEngineService.RULE_FLAG_FROM);

		try {
			// 锟斤拷锟揭伙拷锟斤拷锟叫达拷锟斤拷锟捷匡拷
			payrollService.updateStatus(payrollId);

			Map resultData = new HashMap();
			resultData.putAll(headInfo);
			resultData.putAll(payroll);
			// 锟斤拷锟斤拷锟斤拷莸锟絘ssignuser_tag
			resultData.putAll(assignuser);
			this.setResultData(resultData);

			//send mial to approver
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("corpName", corpUser.getCorporation().getCorpName());
			dataMap.put("requestTime", payroll.get("REQUEST_TIME"));
			dataMap.put("payrollId", payroll.get("PAYROLL_ID"));
			dataMap.put("currency", corpAccountService.getCurrency(payroll.get("ORIGINATOR_AC_NO").toString()));
			dataMap.put("amount", payroll.get("TOTAL_AMOUNT"));
			dataMap.put("startValueDate", payroll.get("START_VALUE_DATE"));
			dataMap.put("totalNumber", payroll.get("TOTAL_NUMBER"));
			dataMap.put("fromAccount", payroll.get("ORIGINATOR_AC_NO"));
			dataMap.put("remark", payroll.get("REMARK"));
			
			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_PAYROLL, mailUser.split(";"), dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_PAYROLL, mailUser.split(";"),corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */
		
			
		} catch (Exception e) {
			//cancel upload
			payrollService.cancelUpload(payroll.get("PAYROLL_ID").toString().trim(),
					null, null, new File(saveFilePath + payroll.get("FILE_NAME").toString().trim()));

			flowEngineService.cancelProcess(processId, getUser());
			Log.error("err.txn.TranscationFaily", e);
			if (e instanceof NTBException) {
				throw new NTBException(e.getMessage());
			} else {
				throw new NTBException("err.txn.TranscationFaily");
			}
		}
	}

	public void usePreviousFileLoad() throws NTBException {

		this.clearUsrSessionDataValue();

		ApplicationContext appContext = Config.getAppContext();
		PayrollService payrollService = (PayrollService) appContext.getBean("PayrollService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");

		CorpUser corpUser = (CorpUser) this.getUser();
		String currency = "";

		Payroll payroll = payrollService.getLatestPayroll(corpUser.getCorpId());
		List payrollRecList = payrollService.listAvaliablePayrollRec(payroll.getPayrollId());
		if (payrollRecList.size() == 0) {
			this.setMessage("No any previous records");
		}
		if (payroll.getOriginatorAcNo() != null) {
			corpAccountService.getCurrency(payroll.getOriginatorAcNo(),true);
		}

		Map resultData = new HashMap();
		this.convertPojo2Map(payroll, resultData);
		resultData.put("payrollRecList", payrollRecList);
		resultData.put("currency", currency);
		resultData.put("listSize", String.valueOf(payrollRecList.size()));
		this.setResultData(resultData);

		this.setUsrSessionDataValue("currency", currency);
		this.setUsrSessionDataValue("payroll", payroll);
		this.setUsrSessionDataValue("payrollRecList", payrollRecList);

	}

	public void usePreviousFile() throws NTBException {

		ApplicationContext appContext = Config.getAppContext();
		PayrollService payrollService = (PayrollService) appContext.getBean("PayrollService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");

		CorpUser corpUser = (CorpUser) this.getUser();

		String currency = (String) this.getUsrSessionDataValue("currency");
		Payroll payroll = (Payroll) this.getUsrSessionDataValue("payroll");
		List payrollRecList =  (List) this.getUsrSessionDataValue("payrollRecList");

		String payDate = DateTime.formatDate(this.getParameter("payDate"), Config.getProperty("DefaultDatePattern"), "yyMMdd");
		String remark = Utils.null2EmptyWithTrim(this.getParameter("remark"));

		//锟叫断斤拷锟斤拷锟角凤拷锟斤拷执锟叫癸拷媒锟斤拷锟�
		payrollService.isDoable(corpUser.getCorpId(), payroll.getOriginatorAcNo(), payroll.getGroupId(), payDate);

		if (Integer.parseInt(DateTime.formatDate(payDate, "yyMMdd", "yyyyMMdd")) - Integer.parseInt(DateTime.formatDate(new Date(), "yyyyMMdd")) < 2) {
			throw new NTBException("err.bat.PaydateError");
		}

		payroll.setPayrollId(CibIdGenerator.getRefNoForTransaction());
		payroll.setUserId(corpUser.getUserId());
		payroll.setStartValueDate(payDate);
		payroll.setEndValueDate(payDate);
		payroll.setBatchDate(DateTime.formatDate(new Date(), "yyMMdd"));
		payroll.setRemark(remark);
		payroll.setRequestTime(new Date());
		payroll.setExecuteTime(null);
		payroll.setStatus(Constants.STATUS_PENDING_APPROVAL);
		payroll.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
		payroll.setOperation(Constants.OPERATION_NEW);

		Map payrollRec = null;
		for (int i=0; i<payrollRecList.size(); i++){
			payrollRec = (Map) payrollRecList.get(i);
			payrollRec.put("TRANS_ID", CibIdGenerator.getRefNoForTransaction());
			payrollRec.put("PAYROLL_ID", payroll.getPayrollId());
			payrollRec.put("TO_ACCOUNT", Utils.removePrefixZero(payrollRec.get("TO_ACCOUNT").toString()));
			payrollRec.put("EXECUTE_TIME", new Date());
			payrollRec.put("STATUS", Constants.STATUS_PENDING_APPROVAL);
		}

		OutputStream newFileOutStream = payrollService.getOutputStreamByPreFile(payroll);


		// 锟斤拷织assignuser_tag锟斤拷锟�
		Map assignuser = new HashMap();
		BigDecimal amountMopEq = exRatesService.getEquivalent(corpUser.getCorpId(),
				currency, "MOP",
				new BigDecimal(payroll.getTotalAmount().toString()),
				null, 2);
		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "currency");
		assignuser.put("amountField", "totalAmount");
		assignuser.put("amountMopEqField", "amountMopEq");
		assignuser.put("txnType", Constants.TXN_SUBTYPE_PAYROLL);
		assignuser.put("amountMopEq", amountMopEq);

		// Check User Limit
		if (!corpUser.checkUserLimit(Constants.TXN_TYPE_PAYROLL, amountMopEq)) {
			//clear unavailable db data
			payrollService.clearUnavailableDataByPayrollId(payroll.getPayrollId());

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", payroll.getUserId());
			reportMap.put("CORP_ID", payroll.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", payroll.getPayrollId());
			reportMap.put("FROM_ACCOUNT", payroll.getOriginatorAcNo());
			reportMap.put("FROM_CURRENCY", currency);
			reportMap.put("TO_CURRENCY", currency);
			reportMap.put("FROM_AMOUNT", payroll.getTotalAmount());
			reportMap.put("TO_AMOUNT", payroll.getTotalAmount());
			reportMap.put("EXCEEDING_TYPE", "2");
			reportMap.put("LIMIT_TYPE", "");
			reportMap.put("USER_LIMIT ", corpUser.getUserLimit(Constants.TXN_TYPE_PAYROLL));
			reportMap.put("DAILY_LIMIT ", new Double(0));
			reportMap.put("TOTAL_AMOUNT ", new Double(0));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg").getString("err.txn.ExceedDailyLimit"));
			
		}
		
		// check Limit
		if (!transferLimitService.checkCurAmtLimitQuota(payroll.getOriginatorAcNo(),
				corpUser.getCorpId(),
				Constants.TXN_TYPE_PAYROLL,
				Double.parseDouble(payroll.getTotalAmount().toString()),
				amountMopEq.doubleValue())) {
			//clear unavailable db data
			payrollService.clearUnavailableDataByPayrollId(payroll.getPayrollId());

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", payroll.getUserId());
			reportMap.put("CORP_ID", payroll.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", payroll.getPayrollId());
			reportMap.put("FROM_ACCOUNT", payroll.getOriginatorAcNo());
			reportMap.put("FROM_CURRENCY", currency);
			reportMap.put("TO_CURRENCY", currency);
			reportMap.put("FROM_AMOUNT", payroll.getTotalAmount());
			reportMap.put("TO_AMOUNT", payroll.getTotalAmount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			throw new NTBWarningException("err.txn.ExceedDailyLimit");
		}


		Map resultData = new HashMap();
		this.convertPojo2Map(payroll, resultData);
		resultData.put("payrollRecList", payrollRecList);
		resultData.put("currency", currency);
		// 锟斤拷锟斤拷锟斤拷莸锟絘ssignuser_tag
		resultData.putAll(assignuser);
		this.setResultData(resultData);

		this.setUsrSessionDataValue("assignuser", assignuser);
		this.setUsrSessionDataValue("currency", currency);
		this.setUsrSessionDataValue("payroll", payroll);
		this.setUsrSessionDataValue("payrollRecList", payrollRecList);
		this.setUsrSessionDataValue("newFileOutStream", newFileOutStream);

	}

	public void usePreviousFileCfm() throws NTBException {

		ApplicationContext appContext = Config.getAppContext();
		PayrollService payrollService = (PayrollService) appContext.getBean("PayrollService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");
		MailService mailService = (MailService) appContext.getBean("MailService");

		CorpUser corpUser = (CorpUser) this.getUser();

		Map assignuser = (Map) this.getUsrSessionDataValue("assignuser");
		String currency = (String) this.getUsrSessionDataValue("currency");
		Payroll payroll = (Payroll) this.getUsrSessionDataValue("payroll");
		List payrollRecList =  (List) this.getUsrSessionDataValue("payrollRecList");
		OutputStream newFileOutStream = (OutputStream) this.getUsrSessionDataValue("newFileOutStream");

		// 锟斤拷织assignuser_tag锟斤拷锟�
		String txnType = (String) assignuser.get("txnType");
		BigDecimal amountMopEq = (BigDecimal) assignuser.get("amountMopEq");
		String assignedUser = Utils.null2EmptyWithTrim(this.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this.getParameter("mailUser"));
		
		// add by chen_y for CR225 20170412
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		
		//check Limit
		if (!transferLimitService.checkLimitQuota(payroll.getOriginatorAcNo(),
				corpUser.getCorpId(),
				Constants.TXN_TYPE_PAYROLL,
				Double.parseDouble(payroll.getTotalAmount().toString()),
				amountMopEq.doubleValue())) {
			//clear unavailable db data
			payrollService.clearUnavailableDataByPayrollId(payroll.getPayrollId());

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", payroll.getUserId());
			reportMap.put("CORP_ID", payroll.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", payroll.getPayrollId());
			reportMap.put("FROM_ACCOUNT", payroll.getOriginatorAcNo());
			reportMap.put("FROM_CURRENCY", currency);
			reportMap.put("TO_CURRENCY", currency);
			reportMap.put("FROM_AMOUNT", payroll.getTotalAmount());
			reportMap.put("TO_AMOUNT", payroll.getTotalAmount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			throw new NTBWarningException("err.txn.ExceedDailyLimit");
		}
		// add by chen_y for CR225 20170412 end
		
		// 锟铰斤拷一锟斤拷锟斤拷权锟斤拷锟斤拷
		String processId = flowEngineService.startProcess(txnType,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				PayrollAction.class,
				currency,
				payroll.getTotalAmount().doubleValue(),
				currency,
				payroll.getTotalAmount().doubleValue(),
				amountMopEq.doubleValue(),
				payroll.getPayrollId(),
				"", getUser(), assignedUser,
				((CorpUser)getUser()).getCorporation().getAllowExecutor(), FlowEngineService.RULE_FLAG_FROM);

		try {
			// 锟斤拷锟揭伙拷锟斤拷锟叫达拷锟斤拷锟捷匡拷
			payrollService.addPayrollByPreFile(payroll, payrollRecList, newFileOutStream);

			Map resultData = new HashMap();
			this.convertPojo2Map(payroll, resultData);
			resultData.put("payrollRecList", payrollRecList);
			resultData.put("currency", currency);
			// 锟斤拷锟斤拷锟斤拷莸锟絘ssignuser_tag
			resultData.putAll(assignuser);
			this.setResultData(resultData);

			//send mial to approver
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", payroll.getRequestTime());
			dataMap.put("payrollId", payroll.getPayrollId());
			dataMap.put("currency", currency);
			dataMap.put("startValueDate", payroll.getStartValueDate());
			dataMap.put("totalNumber", payroll.getTotalNumber());
			dataMap.put("corpName", corpUser.getCorporation().getCorpName());
			dataMap.put("fromAccount", payroll.getOriginatorAcNo());
			dataMap.put("remark", payroll.getRemark());

			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_PAYROLL, mailUser.split(";"), dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_PAYROLL, mailUser.split(";"),corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */
			
		} catch (Exception e) {
			//clear unavailable db data
			payrollService.cancelUpload(payroll.getPayrollId().trim(),
					null, null, new File(saveFilePath + payroll.getFileName().trim()));

			flowEngineService.cancelProcess(processId, getUser());
			Log.error("err.txn.TranscationFaily", e);
			if (e instanceof NTBException) {
				throw new NTBException(e.getMessage());
			} else {
				throw new NTBException("err.txn.TranscationFaily");
			}
		}

	}

	public void listHistoryLoad() throws NTBException {
		this.setResultData(new HashMap());
	}

	public void listHistory() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		PayrollService payrollService = (PayrollService) appContext.getBean("PayrollService");
		
		//add by wen_chy 20091126
		FlowEngineService flowEngineService = (FlowEngineService) appContext
		.getBean("FlowEngineService");
		Map payrollData = null;
		List toList = new ArrayList();
		String changeFlag = null;//

		CorpUser corpUser = (CorpUser) this.getUser();
		
		//add by wen_chy 20091126
		String approverFlag = corpUser.getCorporation().getAllowApproverSelection();//

		String date_range = Utils.null2EmptyWithTrim(this.getParameter("date_range"));
		String dateFrom = Utils.null2EmptyWithTrim(this.getParameter("dateFrom"));
		String dateTo = Utils.null2EmptyWithTrim(this.getParameter("dateTo"));

		List historyList = payrollService.listHistory(corpUser.getCorpId(), corpUser.getUserId(), dateFrom, dateTo);
		
		//add by wen_chy 20091126
		for(int i=0;i<historyList.size();i++){
			Payroll payroll = (Payroll) historyList.get(i);
			if ("Y".equals(approverFlag)) {
				if ("1".equals(payroll.getStatus())) {
					if (flowEngineService.checkApproveComplete(
							Constants.TXN_SUBTYPE_PAYROLL, payroll.getPayrollId())) {
						changeFlag = "N";
					} else {
						changeFlag = "Y";
					}
				}
			}
			payrollData = new HashMap();
			convertPojo2Map(payroll, payrollData);
			payrollData.put("changeFlag", changeFlag);
			toList.add(payrollData);
		}//
		
		//historyList = this.convertPojoList2MapList(historyList);

		Map resultData = new HashMap();
		resultData.put("date_range", date_range);
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("historyList", toList);
		resultData.put("txnType", Constants.TXN_SUBTYPE_PAYROLL);
		resultData.put("approverFlag", approverFlag);
		setResultData(resultData);

	}

	public void viewHistoryDetail() throws NTBException {

		ApplicationContext appContext = Config.getAppContext();
		PayrollService payrollService = (PayrollService) appContext.getBean("PayrollService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");

		String payrollId = Utils.null2EmptyWithTrim(getParameter("payrollId"));
		Payroll payroll = payrollService.viewAvaliablePayroll(payrollId);
		//add by linrui 20180307 for check mdb detail
		if(""==Utils.null2EmptyWithTrim(payroll.getHaddownHis())){
		  	payrollService.downloadHisToRec(payroll);
		}
		//end
		List payrollRecList = payrollService.listUploadErrorDetailRec(payroll.getPayrollId());
		payrollRecList= this.convertPojoList2MapList(payrollRecList);

		String currency = corpAccountService.getCurrency(payroll.getOriginatorAcNo(),true);

		String date_range = Utils.null2EmptyWithTrim(this.getParameter("date_range"));
		String dateFrom = Utils.null2EmptyWithTrim(this.getParameter("dateFrom"));
		String dateTo = Utils.null2EmptyWithTrim(this.getParameter("dateTo"));

		Map calResult = payrollService.calculate(payroll);

		Map resultData = new HashMap();
		this.convertPojo2Map(payroll, resultData);
		resultData.put("date_range", date_range);
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("payrollRecList", payrollRecList);
		resultData.put("currency", currency);
		resultData.putAll(calResult);
		this.setResultData(resultData);
	}


	public boolean approve(String txnType, String id, CibAction bean) throws NTBException {

		ApplicationContext appContext = Config.getAppContext();
		PayrollService payrollService = (PayrollService) appContext.getBean("PayrollService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");

		CorpUser corpUser = (CorpUser) bean.getUser();

		Date startDate = null;
		if (txnType != null) {
			Payroll payroll = payrollService.viewAvaliablePayroll(id);
			
			try {
				//锟角凤拷锟斤拷锟节革拷式
	            startDate = (new SimpleDateFormat("yyMMdd")).parse(payroll.getStartValueDate().trim());
			} catch (Exception e) {
				Log.error("START_VALUE_DATE format error");
				throw new NTBException("err.bat.StartValueDateFormatError");
			}
			
            //锟角凤拷锟斤拷诘锟斤拷锟�锟斤拷锟斤拷2锟斤拷working day锟斤拷锟斤拷锟斤拷锟�
            int workingDays = payrollService.checkStartValueDate(payroll.getCorpId(), payroll.getOriginatorAcNo(), payroll.getGroupId(), startDate);
			if (workingDays == 1) {
				Log.error("START_VALUE_DATE must be later than the day after 1 working day");
				throw new NTBException("err.bat.StartValueDateError1");
				
			} else if (workingDays == 2) {
				Log.error("START_VALUE_DATE must be later than or equal to the day after 2 working days");
				throw new NTBException("err.bat.StartValueDateError2");
				
			} else if (workingDays == -1){
				Log.error("START_VALUE_DATE format error");
				throw new NTBException(ErrConstants.GENERAL_ERROR);
				
			} else if (workingDays == -2){
				Log.error("START_VALUE_DATE should not be a holiday");
				throw new NTBException("err.bat.StartValueDateShouldNotBeHoliday");
			}

			//取锟斤拷Debit锟绞号伙拷锟斤拷锟斤拷锟酵和斤拷锟�
			String currency = corpAccountService.getCurrency(payroll.getOriginatorAcNo(),true);
			Map headInfo = new HashMap();
			headInfo.put("currency", currency);

			BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser.getCorpId(),
					currency, "MOP",
					new BigDecimal(payroll.getTotalAmount().toString()), null, 2);
			// check Limit
			if (!transferLimitService.checkLimitQuota(payroll.getOriginatorAcNo(),
					corpUser.getCorpId(),
					Constants.TXN_TYPE_PAYROLL,
					payroll.getTotalAmount().doubleValue(),
					equivalentMOP.doubleValue())) {

				// write limit report
	        	Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("USER_ID", payroll.getUserId());
				reportMap.put("CORP_ID", payroll.getCorpId());
				reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
				reportMap.put("LOCAL_TRANS_CODE", payroll.getPayrollId());
				reportMap.put("FROM_ACCOUNT", payroll.getOriginatorAcNo());
				reportMap.put("FROM_CURRENCY", currency);
				reportMap.put("TO_CURRENCY", currency);
				reportMap.put("FROM_AMOUNT", payroll.getTotalAmount());
				reportMap.put("TO_AMOUNT", "");
				reportMap.put("EXCEEDING_TYPE", "1");
				reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
				reportMap.put("USER_LIMIT ", "0");
				reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
				reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

				throw new NTBWarningException("err.txn.ExceedDailyLimit");
			}
			payrollService.approvePayroll(payroll, corpUser, equivalentMOP);
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
		PayrollService payrollService = (PayrollService) appContext.getBean("PayrollService");

		if (txnType != null) {
			Payroll payroll = payrollService.viewAvaliablePayroll(id);
			payrollService.rejectPayroll(payroll);
			return true;
		} else {
			return false;
		}
	}


	public String viewDetail(String txnType, String id, CibAction bean) throws NTBException {

		ApplicationContext appContext = Config.getAppContext();
		PayrollService payrollService = (PayrollService) appContext.getBean("PayrollService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");

		String viewPageUrl = "";
		Map resultData = bean.getResultData();
		CorpUser corpUser = (CorpUser) bean.getUser();

		Payroll payroll = payrollService.viewAvaliablePayroll(id);

		String currency = corpAccountService.getCurrency(payroll.getOriginatorAcNo(),true);
		viewPageUrl = "/WEB-INF/pages/bat/payroll/upload_file_approval_view.jsp";

		// 锟斤拷织assignuser_tag锟斤拷锟�
		Map assignuser = new HashMap();
		BigDecimal amountMopEq = exRatesService.getEquivalent(corpUser.getCorpId(),
				currency, "MOP",
				new BigDecimal(payroll.getTotalNumber().toString()),
				null, 2);
		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "currency");
		assignuser.put("amountField", "totalAmount");
		assignuser.put("amountMopEqField", "amountMopEq");
		assignuser.put("txnType", txnType);
		assignuser.put("amountMopEq", amountMopEq);

		bean.convertPojo2Map(payroll, resultData);
		// 锟斤拷锟斤拷锟斤拷莸锟絘ssignuser_tag
		resultData.put("currency", currency);
		resultData.putAll(assignuser);

		bean.setResultData(resultData);

		return viewPageUrl;
	}

}
