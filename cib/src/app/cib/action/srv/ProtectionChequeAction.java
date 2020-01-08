package app.cib.action.srv;

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
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.KeyNameUtils;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bat.FileRequest;
import app.cib.bo.srv.ReqChequeProtection;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.service.bat.ProtectionChequeBatchService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.srv.RequestService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.CutOffTimeService;
import app.cib.service.sys.MailService;
import app.cib.util.Constants;
import app.cib.util.TransAmountFormate;

public class ProtectionChequeAction extends CibAction implements Approvable{
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
//		String approverFlag = user.getCorporation().getAllowApproverSelection();
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext
		.getBean("RequestService");

		Date dateFrom = null;
		Date dateTo = null;
		String chequeNumber = null;
//		String changeFlag = null;
		
		//fromAccount = Utils.null2EmptyWithTrim(getParameter("fromAccount"));
		//toAccount = Utils.null2EmptyWithTrim(getParameter("toAccount"));
		chequeNumber = Utils.null2EmptyWithTrim(getParameter("chequeNumber"));
		
		
		// add by mxl 0824
		String date_range = Utils
				.null2EmptyWithTrim(getParameter("date_range"));
		if (date_range.equals("range")) {
			if (!Utils.null2EmptyWithTrim(getParameter("dateFrom")).equals("")) {
				dateFrom = DateTime.getDateFromStr( Utils.null2EmptyWithTrim(getParameter("dateFrom")), true);
			}
			if (!Utils.null2EmptyWithTrim(getParameter("dateTo")).equals("")) {
				dateTo = DateTime.getDateFromStr( Utils.null2EmptyWithTrim(getParameter("dateTo")), false);
			}
		}
		
		 // ProtectionCheque Request�Ĳ�ѯ
		List chequeProtectionList = requestService.listReqChequeProtectionForRequestHis(dateFrom, dateTo, corpId, groupId, chequeNumber);
		
		List toList = new ArrayList();
//		toList = this.convertPojoList2MapList(chequeProtectionList);
		toList=KeyNameUtils.listDash2CaseDiff(chequeProtectionList);
		/*
		for (int i = 0; i < transferList.size(); i++) {
			FileRequest fileRequest = (FileRequest) transferList
					.get(i);
			String requestType = null;
			if ( fileRequest.getFileName().equals("#") ) {
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
		Map resultData = new HashMap();

		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("toList", toList);
		resultData.put("chequeNumber", chequeNumber);
		setResultData(resultData);
	}
	
	public void viewDetail() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext.getBean("RequestService");
		setResultData(new HashMap(1));
		String batchID = getParameter("batchId");
		FileRequest pojoFileRequest = requestService.viewFileRequest(batchID);
		
		Map resultData = new HashMap();
		resultData.put("fromAccount", pojoFileRequest.getFromAccount());
		resultData.put("currency", pojoFileRequest.getCurrency());
		resultData.put("fromAmount", pojoFileRequest.getFromAmount());
		
		//���batchId�ڱ�ReqProtectionCheque��ѯ��¼
		List requestList = requestService.listProtectionChequeByBatchid(batchID);
		List toList = new ArrayList();
		double totalAmount = 0;
		for (int i = 0; i < requestList.size(); i++) {
			ReqChequeProtection reqChequeProtection = (ReqChequeProtection)requestList.get(i);
			totalAmount = totalAmount + (new Double(reqChequeProtection.getAmount().toString())).doubleValue();
			Map protectionChequeData = new HashMap();
			this.convertPojo2Map(reqChequeProtection, protectionChequeData);
			//bankDraftData.put("changeFlag", changeFlag);
			//String sequenceNo =( new Double(i)).toString();
			protectionChequeData.put("sequenceNo", String.valueOf(i+1));
			toList.add(protectionChequeData);
		}
		resultData.put("toList", toList);
		resultData.put("totalNumber",  String.valueOf(requestList.size()));
		resultData.put("totalAmount", String.valueOf(totalAmount));
		setResultData(resultData);
	}
	
	public void protectionLoad() throws NTBException {
         //���ÿյ� ResultData �����ʾ���
		setResultData(new HashMap(1));
        
	}
	public void protection() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		CorpUser corpUser = (CorpUser) this.getUser();
		ReqChequeProtection reqChequeProtection = new ReqChequeProtection(
				CibIdGenerator.getRefNoForTransaction());
		FileRequest fileRequest = new FileRequest(CibIdGenerator
				.getRefNoForTransaction());

		String issueDate = Utils.null2EmptyWithTrim(getParameter("issueDate"));
		getParameters().put("issueDate", DateTime.getDateFromStr(issueDate));
		this.convertMap2Pojo(this.getParameters(), reqChequeProtection);

		String account = Utils.null2EmptyWithTrim(getParameter("account"));
		String chequeNumber = Utils.null2EmptyWithTrim(getParameter("chequeNumber"));
		reqChequeProtection.setAccount(account);
		reqChequeProtection.setChequeNumber(chequeNumber);
		reqChequeProtection.setUserId(corpUser.getUserId());
		reqChequeProtection.setCorpId(corpUser.getCorpId());
		fileRequest.setFromAccount(reqChequeProtection.getAccount());
		
		//*********
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");
		Double amount  = TransAmountFormate.formateAmount(this.getParameter("amount"),lang);
		reqChequeProtection.setAmount(amount);

		// add by mxl 0106 ��ȡ���ʺŵ�������Ϣ 
		String accountDescription = requestService.getAccountDescription(reqChequeProtection.getAccount().trim());
		fileRequest.setFromAmount(reqChequeProtection.getAmount());
		
        //�ж�IssueDate�����todayҪǰ
		Date today = new Date();
		if (!today.after(reqChequeProtection.getIssueDate())) {
			throw new NTBException("err.srv.issueDateMustLater");
		}
		
        //����FromCurrency
		CorpAccount corpFromAccount = corpAccountService.viewCorpAccount(fileRequest.getFromAccount());
		fileRequest.setFromCurrency(corpFromAccount.getCurrency());
		// Jet added 2008-12-29
		reqChequeProtection.setCurrency(corpFromAccount.getCurrency());
						
		//add by mxl �жϸ��ʻ��Ƿ���Ȩ�����뱣������ 1108
		boolean availableAccount = false;
		List accountList = new ArrayList();
		accountList = requestService.accListChequeProtection(corpUser.getCorpId());
		if (null != accountList && accountList.size() > 0) {
			Map accountMap = null;
			String accountNumber = "";
			for (int i = 0; i < accountList.size(); i++) {
				accountMap = (Map) accountList.get(i);
				accountNumber = (String) accountMap.get("ACCOUNT_NUMBER");
				if (reqChequeProtection.getAccount().equals(accountNumber)) {
					availableAccount = true;
					break;
				}
			}
		} 
		if (availableAccount == false){
			 throw new NTBException("err.srv.accountIsNotPrivilage");			
		}
		
		Map resultData = new HashMap();
		this.convertPojo2Map(reqChequeProtection, resultData);
		resultData.put("accountDescription",accountDescription);
		this.setResultData(resultData);
		resultData.put("issueDate", reqChequeProtection.getIssueDate());

		// ���û����д��session���Ա�confirm��д����ݿ�
		this.setUsrSessionDataValue("reqChequeProtection", reqChequeProtection);
		this.setUsrSessionDataValue("fileRequest", fileRequest);
		this.setUsrSessionDataValue("accountDescription", accountDescription);		
	}
	
	public void protectionConfirm() throws NTBException {
		 // ��ʼ��Service
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");
		FileRequest fileRequest = (FileRequest) this
				.getUsrSessionDataValue("fileRequest");
		ReqChequeProtection reqChequeProtection = (ReqChequeProtection) this
				.getUsrSessionDataValue("reqChequeProtection");
		CorpUser corpUser = (CorpUser) this.getUser();
		FlowEngineService flowEngineService = (FlowEngineService) Config
				.getAppContext().getBean("FlowEngineService");
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));
		
        //check value date cut-off time add by mxl 1123
		checkCutoffTimeAndSetMsg(fileRequest);
		//�½�һ����Ȩ����
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_CHEQUE_PROTECTION,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				ProtectionChequeAction.class, fileRequest.getFromCurrency(),
				fileRequest.getFromAmount().doubleValue(), null,0,0,
				fileRequest.getBatchId(),fileRequest
						.getRemark(), getUser(), null, corpUser
						.getCorporation().getAllowExecutor(),Constants.INPUT_AMOUNT_FLAG_FROM);
		try {			
			fileRequest.setTotalNumber(new Integer(1));
			fileRequest.setRequestTime(new Date());
			fileRequest.setExecuteTime(new Date());
			fileRequest.setUserId(corpUser.getUserId());
			fileRequest.setCorpId(corpUser.getCorpId());
			fileRequest.setFileName("#");
			fileRequest.setBatchResult("--");
			fileRequest.setBatchType(FileRequest.CHEQUE_PROTECTION_BATCH_TYPE);
			fileRequest.setStatus(Constants.STATUS_PENDING_APPROVAL);
			requestService.addFileRequest(fileRequest);
			reqChequeProtection.setRequestTime(new Date());
			reqChequeProtection.setExecuteTime(new Date());
			Log.info("issueDate  ="+reqChequeProtection.getIssueDate());
			//reqChequeProtection.setIssueDate(null);
			Log.info("RequestTime  ="+reqChequeProtection.getRequestTime());
			Log.info("ExecuteTime  ="+reqChequeProtection.getExecuteTime());
			reqChequeProtection.setBatchId(fileRequest.getBatchId());
			reqChequeProtection.setStatus(Constants.STATUS_PENDING_APPROVAL);
			reqChequeProtection.setDetailResult("--");
			requestService.addChequeProtection(reqChequeProtection);
            //send mial to approver
			// �����ʼ� add by mxl 1225
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime",fileRequest.getRequestTime());
			dataMap.put("batchId",fileRequest.getBatchId());
			dataMap.put("fromCurrency",fileRequest.getFromCurrency());
			dataMap.put("fromAmount",fileRequest.getFromAmount());
			dataMap.put("fromAccount",fileRequest.getFromAccount());
			dataMap.put("corpName",corpUser.getCorporation().getCorpName());
			dataMap.put("remark",fileRequest.getRemark());
			
			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_CHEQUE_PROTECTION, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_CHEQUE_PROTECTION, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */
			
			//Jet added 2008-12-29
			Map resultData = this.getResultData();
			this.convertPojo2Map(reqChequeProtection, resultData);
			resultData.put("issueDate", reqChequeProtection.getIssueDate());
			this.setResultData(resultData);

		}  catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
           	throw new NTBException("err.txn.TranscationFaily");
		}
	}
	
	public boolean approve(String txnType, String id, CibAction bean) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext.getBean("RequestService");
		CorpUser corpUser = (CorpUser) bean.getUser();
		if (txnType != null) {
			FileRequest pojoFileRequest = requestService.viewFileRequest(id);
			requestService.toHostChequeProtection(pojoFileRequest.getBatchId(), corpUser);
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
		ProtectionChequeBatchService protectionChequeBatchService = (ProtectionChequeBatchService) appContext.getBean("ProtectionChequeBatchService");
		RequestService requestService = (RequestService) appContext.getBean("RequestService");
		if (txnType != null) {
			FileRequest pojoFileRequest = requestService.viewFileRequest(id);
			protectionChequeBatchService.rejectFileRequest(pojoFileRequest);
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
		FileRequest pojoFileRequest = requestService.viewFileRequest(id);
		// ���batchId�ڱ�ReqProtectionCheque��ѯ��¼
		List requestList = requestService
				.listProtectionChequeByBatchid(pojoFileRequest.getBatchId());
		ReqChequeProtection reqChequeProtection = (ReqChequeProtection) requestList
				.get(0);
		
		// add by mxl 0106 ��ȡ���ʺŵ�������Ϣ
		String accountDescription = requestService
				.getAccountDescription(reqChequeProtection.getAccount().trim());
		viewPageUrl = "/WEB-INF/pages/srv/request/protectionCheque_approval_view.jsp";
		bean.convertPojo2Map(reqChequeProtection, resultData);
		resultData.put("accountDescription", accountDescription);
		bean.setResultData(resultData);
		return viewPageUrl;
	}
	
	private void checkCutoffTimeAndSetMsg(FileRequest pojoFileRequest)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext
				.getBean("CutOffTimeService");
		// check value date cut-off time
		setMessage(cutOffTimeService.check("ZC08", pojoFileRequest
				.getFromCurrency(), ""));
	}

    public void addCancel() throws NTBException {

    }
	
}
