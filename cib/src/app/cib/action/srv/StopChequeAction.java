package app.cib.action.srv;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.neturbo.set.utils.KeyNameUtils;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bat.FileRequest;
import app.cib.bo.bnk.BankUser;
import app.cib.bo.flow.FlowProcess;
import app.cib.bo.srv.ReqStopCheque;
import app.cib.bo.srv.TxnPrompt;
import app.cib.bo.sys.AbstractCorpUser;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.dao.srv.TransferPromptDao;
import app.cib.service.bat.StopChequeBatchService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.srv.RequestService;
import app.cib.service.srv.TransferPromptService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.CutOffTimeService;
import app.cib.service.sys.MailService;
import app.cib.service.txn.TransferService;
import app.cib.util.CacheController;
import app.cib.util.Constants;
import app.cib.util.TransAmountFormate;
import app.cib.util.otp.SMSOTPObject;
import app.cib.util.otp.SMSOTPUtil;
import app.cib.util.otp.SMSReturnObject;

public class StopChequeAction extends CibAction implements Approvable {
	public void listLoad() throws NTBException {
		// ���ÿյ� ResultData �����ʾ���
		setResultData(new HashMap(1));
	}

	//modify by Peng Haisen 2009-10-10 for CR103
	public void listHistory() throws NTBException {
		// ���ÿյ� ResultData �����ʾ���
		setResultData(new HashMap(1));
		CorpUser user = (CorpUser) this.getUser();
//		String userId = user.getUserId();
		String corpId = user.getCorpId();
		String groupId= user.getGroupId();
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext
		.getBean("RequestService");

		Date dateFrom = null;
		Date dateTo = null;
		//String fromAccount = null;
		//String toAccount = null;
		String chequeNumber = null;
//		String changeFlag = null;

		//fromAccount = Utils.null2EmptyWithTrim(getParameter("fromAccount"));
		//toAccount = Utils.null2EmptyWithTrim(getParameter("toAccount"));
		chequeNumber = Utils.null2EmptyWithTrim(getParameter("chequeNumber"));

		// add by mxl 0824
		String date_range = Utils
				.null2EmptyWithTrim(getParameter("date_range"));
		if (date_range.equals("range")) {

			if (!Utils.null2EmptyWithTrim(getParameter("dateFrom"))
					.equals("")) {

				dateFrom = DateTime.getDateFromStr(Utils
						.null2EmptyWithTrim(getParameter("dateFrom")), true);
				
			}
			if (!Utils.null2EmptyWithTrim(getParameter("dateTo")).equals("")) {

				dateTo = DateTime.getDateFromStr(Utils
						.null2EmptyWithTrim(getParameter("dateTo")), false);
				
			}
		}

		// Bank Draft Request�Ĳ�ѯ
		List stopCheuqeList = requestService.listReqStopChequeForRequestHis(dateFrom, dateTo, corpId, groupId, chequeNumber);
		/*
	     List toList = new ArrayList();
		for (int i = 0; i < transferList.size(); i++) {
			FileRequest fileRequest = (FileRequest) transferList.get(i);

			String requestType = null;
			if (fileRequest.getFileName().equals("#")) {
				requestType = "ONLINE REQUEST";
			} else {
				requestType = "FILE REQUEST";
			}
			Map fileRequestData = new HashMap();
			this.convertPojo2Map(fileRequest, fileRequestData);
			fileRequestData.put("changeFlag", changeFlag);
			fileRequestData.put("requestType", requestType);
			toList.add(fileRequestData);

		} */
//		stopCheuqeList = this.convertPojoList2MapList(stopCheuqeList);
		stopCheuqeList=KeyNameUtils.listDash2CaseDiff(stopCheuqeList);
		Log.info("----SELECT STOP HISTORY,AND KeyNameUtils.listDash2CaseDiff:"+stopCheuqeList);

		Map resultData = new HashMap();

		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("toList", stopCheuqeList);
		resultData.put("chequeNumber", chequeNumber);
		resultData.put("date_range", date_range);
		setResultData(resultData);
	}

	public void viewDetail() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		setResultData(new HashMap(1));
		String batchID = getParameter("batchId");
		FileRequest pojoFileRequest = requestService.viewFileRequest(batchID);
		Map resultData = new HashMap();
		resultData.put("fromAccount", pojoFileRequest.getFromAccount());
		resultData.put("currency", pojoFileRequest.getCurrency());
		resultData.put("fromAmount", pojoFileRequest.getFromAmount());

		// ���batchId�ڱ�ReqStopCheque��ѯ��¼
		List requestList = requestService.listStopChequeByBatchid(batchID);
		List toList = new ArrayList();
		double totalAmount = 0;
		for (int i = 0; i < requestList.size(); i++) {
			ReqStopCheque reqStopCheque = (ReqStopCheque) requestList.get(i);
			totalAmount = totalAmount
					+ (new Double(reqStopCheque.getAmount().toString()))
							.doubleValue();
			Map stopChequeData = new HashMap();

			this.convertPojo2Map(reqStopCheque, stopChequeData);
			// bankDraftData.put("changeFlag", changeFlag);
			stopChequeData.put("sequenceNo", String.valueOf(i + 1));
			toList.add(stopChequeData);
		}

		resultData.put("toList", toList);
		resultData.put("totalNumber", String.valueOf(requestList.size()));
		resultData.put("totalAmount", String.valueOf(totalAmount));
		setResultData(resultData);
	}

	public void stopLoad() throws NTBException {
		// ���ÿյ� ResultData �����ʾ���
		Map resultData = new HashMap() ;
//		setResultData(new HashMap(1));
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccountCheckBook(corpUser.getCorpId()));
		
		//add by lzg 20191022
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		TxnPrompt txnPrompt = new TxnPrompt();
		try{
			txnPrompt = transferPromptService.loadByTxnType("7",TransferPromptDao.STATUS_NORMAL);
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
	}

	public void stop() throws NTBException {

		// ��ʼ�� POJO
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		CorpUser corpUser = (CorpUser) this.getUser();
		ReqStopCheque reqStopCheque = new ReqStopCheque(CibIdGenerator
				.getRefNoForTransaction());
		/*FileRequest fileRequest = new FileRequest(CibIdGenerator
				.getRefNoForTransaction());dudu*/
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		this.convertMap2Pojo(this.getParameters(), reqStopCheque);

		// �ж�ԭ��
		String reason = Utils.null2EmptyWithTrim(getParameter("reason"));
		if (reason.equals("1")) {
			reqStopCheque.setStopReason(Utils
					.null2EmptyWithTrim(getParameter("stopReason")));
			reqStopCheque.setReasonFlag("Y");
		} else if (reason.equals("2")) {
			reqStopCheque.setStopReason(Utils
					.null2EmptyWithTrim(getParameter("stopOtherReason")));
			reqStopCheque.setReasonFlag("N");
		}

		/*Date issueDate = DateTime.getDateFromStr(reqStopCheque.getIssueDate());
		// modify by hjs 20070619 Expiry Date is optional
		if(!"".equals(reqStopCheque.getExpiryDate())){
			Date expiryDate = DateTime.getDateFromStr(
					reqStopCheque.getExpiryDate(), false);
			if (!expiryDate.after(issueDate)) {
				throw new NTBException("err.srv.expiryDateMustLater");
			}
		}
		Date today = new Date();
		if (issueDate.after(today)) {
			throw new NTBException("err.srv.issueDateMustLater");
		}*/
		Date issueDate = null;
		if(!"".equals(reqStopCheque.getIssueDate())){
			issueDate = DateTime.getDateFromStr(reqStopCheque.getIssueDate());
			Date today = new Date();
			if (issueDate.after(today)) {
				throw new NTBException("err.srv.issueDateMustLater");
			}
			reqStopCheque.setIssueDate(DateTime.formatDate(reqStopCheque
					.getIssueDate(), Config.getProperty("DefaultDatePattern"),
			"yyyyMMdd"));
		}
		if(!"".equals(reqStopCheque.getExpiryDate())&&!"".equals(reqStopCheque.getIssueDate())){
			Date expiryDate = DateTime.getDateFromStr(
					reqStopCheque.getExpiryDate(), false);
			if (!expiryDate.after(issueDate)) {
				throw new NTBException("err.srv.expiryDateMustLater");
			}
		}
		if(!"".equals(reqStopCheque.getExpiryDate())){
			reqStopCheque.setExpiryDate(DateTime.formatDate(reqStopCheque
					.getExpiryDate(), Config.getProperty("DefaultDatePattern"),
					"yyyyMMdd"));
		}

		/*fileRequest.setFromAccount(reqStopCheque.getCurrentAccount());
		fileRequest.setFromAmount(reqStopCheque.getAmount());dudu*/

		/*fileRequest.setFromCurrency(reqStopCheque.getCurrentAccountCcy());dudu*/
		String accountDescription = requestService
				.getAccountDescription(reqStopCheque.getCurrentAccount().trim());
		reqStopCheque.setUserId(corpUser.getUserId());
		reqStopCheque.setCorpId(corpUser.getCorpId());
		
		//*********
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");
		Double amount  = TransAmountFormate.formateAmount(this.getParameter("amount"),lang);
		reqStopCheque.setAmount(amount);
		
		Map resultData = new HashMap();
		
		//add by lzg 20190820
		String corpType = corpUser.getCorporation().getCorpType();
		String checkFlag = corpUser.getCorporation().getAuthenticationMode();
		resultData.put("corpType", corpType);
		resultData.put("checkFlag", checkFlag);
		resultData.put("operationType", "send");
		resultData.put("showMobileNo", corpUser.getMobile());
		resultData.put("txnType", Constants.TXN_SUBTYPE_STOP_CHEQUE);
		
		//add by lzg end
		
		this.convertPojo2Map(reqStopCheque, resultData);
		resultData.put("reason", reason);
		resultData.put("accountDescription", accountDescription);
		//add by linrui 20190618
		Map assignuser = new HashMap();
		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "currentAccountCcy");
		assignuser.put("payCurrency", reqStopCheque.getCurrentAccountCcy());
		assignuser.put("amountField", "amount");
		assignuser.put("amount", 0.0);
		assignuser.put("amountMopEqField", "amountMopEq");
		assignuser.put("txnType", Constants.TXN_SUBTYPE_STOP_CHEQUE);
		assignuser.put("amountMopEq", new BigDecimal(0));
		resultData.putAll(assignuser);
		resultData.put("amount1", amount);
		//end
		this.setResultData(resultData);
		
		this.setUsrSessionDataValue("reqStopCheque", reqStopCheque);
		/*this.setUsrSessionDataValue("fileRequest", fileRequest);dudu*/
	}

	public void stopConfirm() throws NTBException {
		// ��ʼ��Service
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");
		/*FileRequest fileRequest = (FileRequest) this
				.getUsrSessionDataValue("fileRequest");dudu*/
		ReqStopCheque reqStopCheque = (ReqStopCheque) this
				.getUsrSessionDataValue("reqStopCheque");
		CorpUser corpUser = (CorpUser) this.getUser();
		FlowEngineService flowEngineService = (FlowEngineService) Config
				.getAppContext().getBean("FlowEngineService");
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));
		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
         //check value date cut-off time add by mxl 1123
		/*checkCutoffTimeAndSetMsg(fileRequest);dudu*/
	
		/*String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_STOP_CHEQUE,
				FlowEngineService.TXN_CATEGORY_FINANCE, StopChequeAction.class,
				fileRequest.getFromCurrency(), fileRequest.getFromAmount().doubleValue(), null, 0, 0,
				fileRequest.getBatchId(), fileRequest.getRemark(), getUser(),
				null, corpUser.getCorporation().getAllowExecutor(), Constants.INPUT_AMOUNT_FLAG_FROM);dudu*/
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_STOP_CHEQUE,
				FlowEngineService.TXN_CATEGORY_FINANCE, StopChequeAction.class,
				reqStopCheque.getCurrentAccountCcy(), reqStopCheque.getAmount().doubleValue(), null, 0, 0,
				reqStopCheque.getTransId(), reqStopCheque.getRemark(), getUser(),
				assignedUser, corpUser.getCorporation().getAllowExecutor(), Constants.INPUT_AMOUNT_FLAG_FROM);
		
		
		Map resultData = new HashMap();
		String corpType = getParameter("corpType");
		resultData.put("corpType", corpType);
		try {

			/*fileRequest.setTotalNumber(new Integer(1));
			fileRequest.setRequestTime(new Date());
			fileRequest.setExecuteTime(new Date());
			fileRequest.setFileName("#");
			fileRequest.setBatchResult("--");
			fileRequest.setUserId(corpUser.getUserId());
			fileRequest.setCorpId(corpUser.getCorpId());
			fileRequest.setBatchType(FileRequest.STOP_CHEQUE_BATCH_TYPE);
			fileRequest.setStatus(Constants.STATUS_PENDING_APPROVAL);
			requestService.addFileRequest(fileRequest);dudu*/
			reqStopCheque.setRequestTime(new Date());
			reqStopCheque.setExecuteTime(new Date());
			reqStopCheque.setDetailResult("--");
			/*reqStopCheque.setBatchId(fileRequest.getBatchId());dudu*/
			reqStopCheque.setStatus(Constants.STATUS_PENDING_APPROVAL);

			// send mial to approver
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			/*dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", fileRequest.getRequestTime());
			dataMap.put("batchId", fileRequest.getBatchId());
			dataMap.put("fromCurrency", fileRequest.getFromCurrency());
			dataMap.put("fromAmount", fileRequest.getFromAmount());
			dataMap.put("fromAccount", fileRequest.getFromAccount());
			dataMap.put("corpName", corpUser.getCorporation().getCorpName());
			dataMap.put("remark", fileRequest.getRemark());dudu*/
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", reqStopCheque.getRequestTime());
			dataMap.put("transId", reqStopCheque.getTransId());
			dataMap.put("fromCurrency", reqStopCheque.getCurrentAccountCcy());
			dataMap.put("fromAmount", reqStopCheque.getAmount());
			dataMap.put("fromAccount", reqStopCheque.getCurrentAccount());
			dataMap.put("corpName", corpUser.getCorporation().getCorpName());
			dataMap.put("remark", reqStopCheque.getRemark());
			
			/* Add by long_zg 2019-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_STOP_CHEQUE, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_STOP_CHEQUE, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2019-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */
			
			
			this.convertPojo2Map(reqStopCheque, resultData);
			this.getResultData().putAll(resultData);
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
			requestService.addStopCheque(reqStopCheque);
			try{
				approve(currentFlowProcess.getTxnType(), currentFlowProcess.getTransNo(), this);
			}catch (NTBHostException e) {
				requestService.removeStopCheque("req_stop_cheque", currentFlowProcess.getTransNo());
				throw new NTBHostException(e.getErrorArray());
			}catch (NTBException e) {
				requestService.removeStopCheque("req_stop_cheque", currentFlowProcess.getTransNo());
				throw new NTBException(e.getErrorCode());
			}
		}
		if(!"3".equals(corpType)){
			try{
				requestService.addStopCheque(reqStopCheque);
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
	
	public boolean approve(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		CorpUser corpUser = (CorpUser) bean.getUser();
		if (txnType != null) {
			/*FileRequest pojoFileRequest = requestService.viewFileRequest(id);
			requestService.toHostStopCheque(pojoFileRequest.getBatchId(),
					corpUser);*/
			requestService.toHostStopCheque(id,
					corpUser);
			ReqStopCheque reqStop = requestService.viewStopCheque(id);
			Map resultData = bean.getResultData();
			this.convertPojo2Map(reqStop, resultData);
			setResultData(resultData);
	
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
		StopChequeBatchService stopChequeBatchService = (StopChequeBatchService) appContext
				.getBean("StopChequeBatchService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		if (txnType != null) {
			/*FileRequest pojoFileRequest = requestService.viewFileRequest(id);
			stopChequeBatchService.rejectFileRequest(pojoFileRequest);*/
			requestService.rejectStopCheque(id);
			return true;
		} else {
			return false;
		}
	}

	public String viewDetail(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");

		String viewPageUrl = "";
		Map resultData = bean.getResultData();
		/*FileRequest pojoFileRequest = requestService.viewFileRequest(id);dudu*/
		// 
		/*List requestList = requestService.listStopChequeByBatchid(pojoFileRequest.getBatchId());*/
		viewPageUrl = "/WEB-INF/pages/srv/request/stopCheque_approval_view.jsp";
		/*ReqStopCheque reqStopCheque = (ReqStopCheque) requestList.get(0);dudu*/
		ReqStopCheque reqStopCheque  = requestService.viewStopCheque(id);
		//add wen_yh 1-19
		String accountDescription = requestService.getAccountDescription(reqStopCheque.getCurrentAccount().trim());
		bean.convertPojo2Map(reqStopCheque, resultData);
		resultData.put("accountDescription",accountDescription);
		bean.setResultData(resultData);
		return viewPageUrl;
	}

	private void checkCutoffTimeAndSetMsg(FileRequest pojoFileRequest)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext
				.getBean("CutOffTimeService");

		// check value date cut-off time
		setMessage(cutOffTimeService.check("ZC07", pojoFileRequest
				.getFromCurrency(), ""));
	}
	
    public void addCancel() throws NTBException {
    	Map resultData = this.getResultData();
    	
    	String issueDate = (String) resultData.get("issueDate");
    	if(!"".equals(issueDate)){
    		issueDate = DateTime.formatDate(issueDate, "yyyyMMdd", Config.getProperty("DefaultDatePattern"));
    		resultData.put("issueDate", issueDate);
    	}
    	
    	String expiryDate =  (String) resultData.get("expiryDate");
    	if(!"".equals(expiryDate)){
    		expiryDate = DateTime.formatDate(expiryDate, "yyyyMMdd", Config.getProperty("DefaultDatePattern"));
        	resultData.put("expiryDate", expiryDate);
    	}
    	
    	String reasonFlag = (String) resultData.get("reasonFlag");
    	String stopReason = (String) resultData.get("stopReason");
    	if("N".equals(reasonFlag)){
        	resultData.put("stopOtherReason", stopReason);
    	}
    	ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccountCheckBook(corpUser.getCorpId()));
		setResultData(resultData);
		}
}
