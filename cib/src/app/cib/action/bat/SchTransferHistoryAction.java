package app.cib.action.bat;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.base.action.ActionForward;
import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TransferMacau;
import app.cib.bo.txn.TransferOversea;
import app.cib.core.CibAction;
import app.cib.service.bat.SchTransferService;
import app.cib.service.bat.SchTxnBatchService;
import app.cib.service.txn.TransferService;
import app.cib.util.TransferConstants;

public class SchTransferHistoryAction extends CibAction {

	/**
	 * mxl
	 * @throws NTBException
	 */
	public void listLoad() throws NTBException {
		// ���ÿյ� ResultData �����ʾ���
		setResultData(new HashMap(1));
	}

	/**
	 * add by mxl, modify by hjs
	 * @throws NTBException
	 */
	public void listHistory() throws NTBException {
		
		setResultData(new HashMap(1));
		CorpUser user = (CorpUser) this.getUser();
		String userId = user.getUserId();
		String corpId = user.getCorpId();
		ApplicationContext appContext = Config.getAppContext();
		SchTxnBatchService schTxnBatchService = (SchTxnBatchService) appContext.getBean("SchTxnBatchService");
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		String dateFrom = "";
		String dateTo = "";
		
		String beneficiaryType = Utils.null2EmptyWithTrim(getParameter("beneficiaryType"));
		String date_range = Utils.null2EmptyWithTrim(getParameter("date_range"));
		if (date_range.equals("range")) {
			if (!Utils.null2EmptyWithTrim(getParameter("dateFrom")).equals("")) {
				dateFrom = Utils.null2EmptyWithTrim(getParameter("dateFrom"));
			}
			if (!Utils.null2EmptyWithTrim(getParameter("dateTo")).equals("")) {
				dateTo = Utils.null2EmptyWithTrim(getParameter("dateTo"));
			}
		}

		List recList = schTxnBatchService.listSchBatchHistory(
				beneficiaryType, corpId, userId, dateFrom, dateTo);
		recList = this.convertPojoList2MapList(recList);
		
		Map row = null;
		Object transfer = null;
		Field field = null;
		for(int i=0; i<recList.size(); i++) {
			row = (Map) recList.get(i);
			if (Utils.null2EmptyWithTrim(row.get("beneficiaryType")).equals(TransferConstants.TRANSFER_TYPE_BANK)) {
				transfer = transferService.viewInBANK(row.get("transId").toString().trim());
			} else if (Utils.null2EmptyWithTrim(row.get("beneficiaryType")).equals(TransferConstants.TRANSFER_TYPE_MACAU)) {
				transfer = transferService.viewInMacau(row.get("transId").toString().trim());
			} else if (Utils.null2EmptyWithTrim(row.get("beneficiaryType")).equals(TransferConstants.TRANSFER_TYPE_OVERSEA)) {
				transfer = transferService.viewInOversea(row.get("transId").toString().trim());
			} else if (Utils.null2EmptyWithTrim(row.get("beneficiaryType")).equals(TransferConstants.TRANSFER_TYPE_MDB)) {
				transfer = transferService.viewInBANK(row.get("transId").toString().trim());
			}
			try {
				field = transfer.getClass().getSuperclass().getDeclaredField("hostresponseCode");
				field.setAccessible(true);
				row.put("hostresponseCode", field.get(transfer));
				field = transfer.getClass().getSuperclass().getDeclaredField("hosterrorCode");
				field.setAccessible(true);
				row.put("hosterrorCode", field.get(transfer));
			} catch (Exception e) {
				throw new NTBException("err.bat.GetDetailResultError");
			}
			
		}

		Map resultData = new HashMap();
		resultData.put("beneficiaryType", beneficiaryType);
		resultData.put("date_range", date_range);
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("recList", recList);
		setResultData(resultData);
	}
	
	/**
	 * hjs 
	 * @throws NTBException
	 */
	public void listOperationHistoryLoad() throws NTBException {
		this.setResultData(new HashMap(1));
	}

	/**
	 * hjs 
	 * @throws NTBException
	 */
	public void listOperationHistory() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		SchTransferService schTransferService = (SchTransferService) appContext.getBean("SchTransferService");
		
		CorpUser corpUser = (CorpUser) this.getUser();
		String beneficiaryType = Utils.null2EmptyWithTrim(this.getParameter("beneficiaryType"));
		String userId = Utils.null2EmptyWithTrim(this.getParameter("userId"));
		
		List hisList = schTransferService.listOperationHistory(beneficiaryType, corpUser.getCorpId(), userId);
		hisList = this.convertPojoList2MapList(hisList);
		
		Map resultData = new HashMap();
		resultData.put("beneficiaryType", beneficiaryType);
		resultData.put("userId", userId);
		resultData.put("hisList", hisList);
		setResultData(resultData);
	}

	/**
	 * hjs 
	 * @throws NTBException
	 */
	public void viewDetail() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		
		String beneficiaryType = Utils.null2EmptyWithTrim(this.getParameter("beneficiaryType"));
		String beneficiaryTypeInRec = Utils.null2EmptyWithTrim(this.getParameter("beneficiaryTypeInRec"));
		String userId = Utils.null2EmptyWithTrim(this.getParameter("userId"));
		String date_range = Utils.null2EmptyWithTrim(getParameter("date_range"));
		String dateFrom = "";
		String dateTo = "";
		if (date_range.equals("range")) {
			if (!Utils.null2EmptyWithTrim(getParameter("dateFrom")).equals("")) {
				dateFrom = Utils.null2EmptyWithTrim(getParameter("dateFrom"));
			}
			if (!Utils.null2EmptyWithTrim(getParameter("dateTo")).equals("")) {
				dateTo = Utils.null2EmptyWithTrim(getParameter("dateTo"));
			}
		}
		String customReturn = Utils.null2EmptyWithTrim(this.getParameter("customReturn"));
		
		String transId = Utils.null2EmptyWithTrim(this.getParameter("transId"));
		String frequnceType = Utils.null2EmptyWithTrim(this.getParameter("frequnceType"));
		String frequnceDays = Utils.null2EmptyWithTrim(this.getParameter("frequnceDays"));
		String scheduleName = Utils.null2EmptyWithTrim(this.getParameter("scheduleName"));
		String scheduleDate = Utils.null2EmptyWithTrim(this.getParameter("scheduleDate"));
		String status = Utils.null2EmptyWithTrim(this.getParameter("status"));
		
		Object transfer = null;
		// add by lw 2011-01-18
		String purpose = null;
		// add by lw end
		String input = "/WEB-INF/pages/rpt/scheduled_transfer/sch_report_list.jsp";
		String path = null;
		if (beneficiaryTypeInRec.equals(TransferConstants.TRANSFER_TYPE_BANK)) {
			transfer = transferService.viewInBANK(transId);
			path = "/WEB-INF/pages/bat/schedule_transfer/schTransferView_InBANK.jsp";
			
		} else if (beneficiaryTypeInRec.equals(TransferConstants.TRANSFER_TYPE_MACAU)) {
			transfer = transferService.viewInMacau(transId);
			path = "/WEB-INF/pages/bat/schedule_transfer/schTransferView_InMacau.jsp";
			// add by lw 2011-01-18
			TransferMacau transferMacau = transferService.viewInMacau(transId);
			purpose = transferMacau.getOtherPurpose();
			// add by lw end
			
		} else if (beneficiaryTypeInRec.equals(TransferConstants.TRANSFER_TYPE_OVERSEA)) {
			transfer = transferService.viewInOversea(transId);
			path = "/WEB-INF/pages/bat/schedule_transfer/schTransferView_InOversea.jsp";
			// add by lw 2011-01-18
			TransferOversea transferOversea = transferService
			.viewInOversea(transId);
			purpose = transferOversea.getOtherPurpose();
			// add by lw end
			
		} 
		
		/* Add by long_zg 2019-05-29 UAT6-246 COB：新增的第三者轉賬未完全改好  begin */
		else if (beneficiaryTypeInRec.equals(TransferConstants.TRANSFER_TYPE_MDB)) {
			transfer = transferService.viewInBANK(transId);
			path = "/WEB-INF/pages/bat/schedule_transfer/schTransferView_InBANK3rd.jsp";
			
		} 
		/* Add by long_zg 2019-05-29 UAT6-246 COB：新增的第三者轉賬未完全改好  end */
		
		
		else {
			throw new NTBException();
		}

		Map resultData = new HashMap();
		this.convertPojo2Map(transfer, resultData);
		
		// add by lw 2011-01-18
		if(purpose != null && !"".equals(purpose)){
			resultData.put("showPurpose", "true");
			resultData.put("purpose", purpose);
		}
		// add by lw end
		
		resultData.put("frequenceType", frequnceType);
		resultData.put("frequenceDays", frequnceDays);
		resultData.put("scheduleName", scheduleName);
		resultData.put("scheduleDate", scheduleDate);
		resultData.put("customReturn", customReturn);
		resultData.put("beneficiaryType", beneficiaryType);
		resultData.put("userId", userId);
		resultData.put("date_range", date_range);
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		if(!status.equals("")){
			resultData.put("status", status);
		}
		setResultData(resultData);
		
		ActionForward af = new ActionForward();
		af.setInput(input);
		af.setPath(path);
		this.setForward(af);
		
	}

}
