package app.cib.action.bat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bat.FileRequest;
import app.cib.bo.bat.FileRequestFileBean;
import app.cib.bo.sys.CorpUser;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.service.bat.StopChequeBatchService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.srv.RequestService;
import app.cib.service.sys.CutOffTimeService;
import app.cib.service.sys.MailService;
import app.cib.util.Constants;

public class StopChequeBatchAction extends CibAction implements Approvable {
	private static final String saveFilePath = Config.getProperty("BatchFileUploadDir") + "/";
	public void uploadFileLoad() throws NTBException {
		Map resultData = new HashMap(1);
		this.setResultData(resultData);
		ApplicationContext appContext = Config.getAppContext();
		StopChequeBatchService stopChequeBatchService = (StopChequeBatchService) appContext
				.getBean("StopChequeBatchService");
		CorpUser corpUser = (CorpUser) this.getUser();
		// clear unavailable db data
		stopChequeBatchService.clearUnavailableDataByCorpId(corpUser
				.getCorpId());
	}

	public void uploadFile() throws NTBException {
		// ��ʼ��Service
		ApplicationContext appContext = Config.getAppContext();
		StopChequeBatchService stopChequeBatchService = (StopChequeBatchService) appContext
				.getBean("StopChequeBatchService");
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
		FileRequestFileBean fileRequestFileBean = stopChequeBatchService
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

		// if error
		if (errList.size() != 0) {
			errFlag = "Y";
		} else if (allCount != Integer.parseInt(fileRequest.get("TOTAL_NUMBER")
				.toString())) {
			setError(new NTBException("err.bat.CountNotEqual"));
			errFlag = "Y";
		}

		headInfo.put("allCount", new Integer(allCount));
		headInfo.put("totalAmount", new Double(normalTotalAmt + errTotalAmt));

		Map resultData = new HashMap();
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
		resultData.put("errTotalAmt", new Double(errTotalAmt));
		resultData.put("errList", errList);

		this.setResultData(resultData);
		this.setUsrSessionDataValue("fileRequest", fileRequest);
		this.setUsrSessionDataValue("headInfo", headInfo);
		this.setUsrSessionDataValue("recList", normalList);
		} catch (Exception e) {
			stopChequeBatchService.cancelUpload(fileRequest.get("BATCH_ID").toString().trim(), null, null, new File(saveFilePath + fileRequest.get("FILE_NAME").toString().trim()));
			throw new NTBException (e.getMessage());
		}

	}

	public void uploadFileConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		StopChequeBatchService stopChequeBatchService = (StopChequeBatchService) appContext
				.getBean("StopChequeBatchService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		MailService mailService = (MailService) appContext
		.getBean("MailService");
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext.getBean("CutOffTimeService");
		CorpUser corpUser = (CorpUser) this.getUser();
		String mailUser = Utils.null2EmptyWithTrim(this.getParameter("mailUser"));
		List recList = (List) this.getUsrSessionDataValue("recList");
		Map fileRequest = (Map) this.getUsrSessionDataValue("fileRequest");
		Map headInfo = (Map) this.getUsrSessionDataValue("headInfo");
		String batchId = (String) fileRequest.get("BATCH_ID");
		FileRequest pojoFileRequest = requestService.viewFileRequest(batchId);
		FlowEngineService flowEngineService = (FlowEngineService) Config
				.getAppContext().getBean("FlowEngineService");
        //check value date cut-off time add by mxl 0130
		setMessage(cutOffTimeService.check("XC07", "",""));
		// �½�һ����Ȩ����
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_STOP_CHEQUE,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				StopChequeBatchAction.class, null, 0, null, 0, 0,
				pojoFileRequest.getBatchId(), pojoFileRequest.getRemark(),
				getUser(), null, corpUser.getCorporation().getAllowExecutor(),
				"1");
		try {
			// ���һ����д����ݿ�
			stopChequeBatchService.updateStatus(pojoFileRequest.getBatchId());
             //send mial to approver
			// �����ʼ� add by mxl 1225
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
			
			
			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_STOP_CHEQUE, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_STOP_CHEQUE, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */
			
			
			Map resultData = new HashMap();
			resultData.putAll(fileRequest);
			resultData.putAll(headInfo);
			
			resultData.put("recList", recList);
			this.setResultData(resultData);
			
		} catch (Exception e) {
			// clear unavailable db data
			stopChequeBatchService.clearUnavailableDataByPaybatchId(fileRequest
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
		StopChequeBatchService stopChequeBatchService = (StopChequeBatchService) appContext
				.getBean("StopChequeBatchService");

		Map fileRequest = (Map) this.getUsrSessionDataValue("fileRequest");

		stopChequeBatchService.cancelUpload(fileRequest.get("BATCH_ID")
				.toString().trim(), null, null, new File(saveFilePath
				+ fileRequest.get("FILE_NAME").toString().trim()));
	}		
		
	public boolean approve(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		StopChequeBatchService stopChequeBatchService = (StopChequeBatchService) appContext
				.getBean("StopChequeBatchService");
		CorpUser corpUser = (CorpUser) bean.getUser();
		if (txnType != null) {
			FileRequest pojoFileRequest = requestService.viewFileRequest(id);
			stopChequeBatchService.approveStopCheque(pojoFileRequest, corpUser);
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
			FileRequest pojoFileRequest = requestService.viewFileRequest(id);
			stopChequeBatchService.rejectFileRequest(pojoFileRequest);
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
		viewPageUrl = "/WEB-INF/pages/bat/batch_request/upload_file_approval_viewStopCheque.jsp";
		
		//add by hjs : list detail
		List recList = requestService.listStopChequeByBatchid(id);
		recList = this.convertPojoList2MapList(recList);
		
		bean.convertPojo2Map(pojoFileRequest, resultData);
		// ������ݵ�assignuser_tag
		resultData.put("recList", recList);
		bean.setResultData(resultData);
		return viewPageUrl;
	}

}
