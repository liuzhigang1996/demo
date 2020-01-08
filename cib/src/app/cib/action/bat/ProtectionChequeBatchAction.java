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
import app.cib.service.bat.ProtectionChequeBatchService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.srv.RequestService;
import app.cib.service.sys.CutOffTimeService;
import app.cib.service.sys.MailService;
import app.cib.util.Constants;

public class ProtectionChequeBatchAction extends CibAction implements
		Approvable {
	private static final String saveFilePath = Config.getProperty("BatchFileUploadDir") + "/";
	public void uploadFileLoad() throws NTBException {
		Map resultData = new HashMap(1);
		this.setResultData(resultData);
		ApplicationContext appContext = Config.getAppContext();
		ProtectionChequeBatchService protectionChequeBatchService = (ProtectionChequeBatchService) appContext
				.getBean("ProtectionChequeBatchService");
		CorpUser corpUser = (CorpUser) this.getUser();
		// clear unavailable db data
		protectionChequeBatchService.clearUnavailableDataByCorpId(corpUser
				.getCorpId());
	}

	public void uploadFile() throws NTBException {
		// ��ʼ��Service
		ApplicationContext appContext = Config.getAppContext();
		ProtectionChequeBatchService protectionChequeBatchService = (ProtectionChequeBatchService) appContext
				.getBean("ProtectionChequeBatchService");
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
		FileRequestFileBean fileRequestFileBean = protectionChequeBatchService
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
		Map headInfo = new HashMap();

		// if error
		/*
		 * if (errList.size() != 0) { errFlag = "Y"; } else if (allCount !=
		 * Integer.parseInt(fileRequest.get("TOTAL_NUMBER").toString())) {
		 * errFlag = "Y"; }
		 */

		if (errList.size() != 0) {
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

		this.setResultData(resultData);
		this.setUsrSessionDataValue("fileRequest", fileRequest);
		this.setUsrSessionDataValue("headInfo", headInfo);
		this.setUsrSessionDataValue("allCount", new Integer(allCount));
		this.setUsrSessionDataValue("recList", normalList);
		} catch (Exception e) {
			protectionChequeBatchService.cancelUpload(fileRequest.get("BATCH_ID").toString().trim(), null, null, new File(saveFilePath + fileRequest.get("FILE_NAME").toString().trim()));
			throw new NTBException (e.getMessage());
		}

	}

	public void uploadFileConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		ProtectionChequeBatchService protectionChequeBatchService = (ProtectionChequeBatchService) appContext
				.getBean("ProtectionChequeBatchService");
		MailService mailService = (MailService) appContext.getBean("MailService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext.getBean("CutOffTimeService");
		CorpUser corpUser = (CorpUser) this.getUser();

		List recList = (List) this.getUsrSessionDataValue("recList");
		Map fileRequest = (Map) this.getUsrSessionDataValue("fileRequest");
		Map headInfo = (Map) this.getUsrSessionDataValue("headInfo");

		String batchId = (String) fileRequest.get("BATCH_ID");
		FileRequest pojoFileRequest = requestService.viewFileRequest(batchId);
		pojoFileRequest.setTotalNumber(new Integer(headInfo.get("allCount")
				.toString()));
		requestService.updateFileRequest(pojoFileRequest);

		// ������Ȩ
		FlowEngineService flowEngineService = (FlowEngineService) Config
				.getAppContext().getBean("FlowEngineService");
		 String mailUser = Utils.null2EmptyWithTrim(this.getParameter("mailUser"));
		 //check value date cut-off time add by mxl 0130
			setMessage(cutOffTimeService.check("XC08", "",""));
		// �½�һ����Ȩ����
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_CHEQUE_PROTECTION,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				ProtectionChequeBatchAction.class, null, 0, null, 0, 0,
				pojoFileRequest.getBatchId(), pojoFileRequest.getRemark(),
				getUser(), null, corpUser.getCorporation().getAllowExecutor(),
				"1");
		try {
			// ���һ����д����ݿ�
			protectionChequeBatchService.updateStatus(pojoFileRequest
					.getBatchId());
            // send mial to approver
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
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_CHEQUE_PROTECTION, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_CHEQUE_PROTECTION, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */
			

			Map resultData = new HashMap();
			resultData.putAll(fileRequest);
			resultData.putAll(headInfo);
			
			resultData.put("recList", recList);
			this.setResultData(resultData);
			
		} catch (Exception e) {
			// clear unavailable db data
			protectionChequeBatchService
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
		ProtectionChequeBatchService protectionChequeBatchService = (ProtectionChequeBatchService) appContext
				.getBean("ProtectionChequeBatchService");

		Map fileRequest = (Map) this.getUsrSessionDataValue("fileRequest");

		protectionChequeBatchService.cancelUpload(fileRequest.get("BATCH_ID")
				.toString().trim(), null, null, new File(saveFilePath
				+ fileRequest.get("FILE_NAME").toString().trim()));
	}
		
	public boolean approve(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		ProtectionChequeBatchService protectionChequeBatchService = (ProtectionChequeBatchService) appContext
				.getBean("ProtectionChequeBatchService");
		CorpUser corpUser = (CorpUser) bean.getUser();
		if (txnType != null) {
			FileRequest pojoFileRequest = requestService.viewFileRequest(id);
			protectionChequeBatchService.approveChequeProtection(
					pojoFileRequest, corpUser);
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
		ProtectionChequeBatchService protectionChequeBatchService = (ProtectionChequeBatchService) appContext
				.getBean("ProtectionChequeBatchService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
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
		viewPageUrl = "/WEB-INF/pages/bat/batch_request/upload_file_approval_viewProtectionCheque.jsp";
		
		//add by hjs : list detail
		List recList = requestService.listProtectionChequeByBatchid(id);
		recList = this.convertPojoList2MapList(recList);
		
		bean.convertPojo2Map(pojoFileRequest, resultData);
		// ������ݵ�assignuser_tag
		resultData.put("recList", recList);
		bean.setResultData(resultData);
		return viewPageUrl;
	}

}
