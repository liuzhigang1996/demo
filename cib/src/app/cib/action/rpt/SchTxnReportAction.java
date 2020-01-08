/**
 * @author hjs
 * 2006-10-27
 */
package app.cib.action.rpt;

import java.util.*;

import org.springframework.context.ApplicationContext;

import com.neturbo.base.action.ActionForward;
import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bat.ScheduleTransfer;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.service.bat.SchTxnBatchService;
import app.cib.service.txn.TransferService;
import app.cib.service.utl.UtilService;
import app.cib.util.TransferConstants;

/**
 * @author hjs
 * 2006-10-27
 */
public class SchTxnReportAction extends CibAction {

	public void listReport() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		SchTxnBatchService schTxnBatchService = (SchTxnBatchService) appContext.getBean("SchTxnBatchService");
		UtilService utilService = (UtilService) appContext.getBean("UtilService");

		CorpUser corpUser = (CorpUser) this.getUser();

		Date nextBatchDay = utilService.getNextSchDate();

		List reportList = schTxnBatchService.listNextBatchDayReport(corpUser.getCorpId(),
				DateTime.formatDate(nextBatchDay, "yyyyMMdd"));
		reportList = this.convertPojoList2MapList(reportList);

		Map resultData = new HashMap();
		resultData.put("nextBatchDay", nextBatchDay);
                if(reportList == null){
                    reportList = new ArrayList();
                }
		resultData.put("reportList", reportList);
		setResultData(resultData);

	}

	public void viewDetail() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");

		String beneficiaryType = Utils.null2EmptyWithTrim(this.getParameter("beneficiaryType"));

		String transId = Utils.null2EmptyWithTrim(this.getParameter("transId"));
		String frequnceType = Utils.null2EmptyWithTrim(this.getParameter("frequnceType"));
		String frequnceDays = Utils.null2EmptyWithTrim(this.getParameter("frequnceDays"));
		String scheduleName = Utils.null2EmptyWithTrim(this.getParameter("scheduleName"));
		String scheduleDate = Utils.null2EmptyWithTrim(this.getParameter("scheduleDate"));

		Object pojo = null;
		String input = "/WEB-INF/pages/rpt/scheduled_transfer/sch_report_list.jsp";
		String path = null;
		if (beneficiaryType.equals(TransferConstants.TRANSFER_TYPE_BANK)) {
			pojo = transferService.viewInBANK(transId);
			path = "/WEB-INF/pages/bat/schedule_transfer/schTransferView_InBANK.jsp";

		} else if (beneficiaryType.equals(TransferConstants.TRANSFER_TYPE_MACAU)) {
			pojo = transferService.viewInMacau(transId);
			path = "/WEB-INF/pages/bat/schedule_transfer/schTransferView_InMacau.jsp";

		} else if (beneficiaryType.equals(TransferConstants.TRANSFER_TYPE_OVERSEA)) {
			pojo = transferService.viewInOversea(transId);
			path = "/WEB-INF/pages/bat/schedule_transfer/schTransferView_InOversea.jsp";

		} else {
			throw new NTBException();
		}

		Map resultData = new HashMap();
		this.convertPojo2Map(pojo, resultData);
		resultData.put("frequenceType", frequnceType);
		resultData.put("frequenceDays", frequnceDays);
		resultData.put("scheduleName", scheduleName);
		resultData.put("scheduleDate", scheduleDate);
		resultData.put("customReturn", ScheduleTransfer.VIEW_FROM_SCHEDULE_TXN_REPORT);
		setResultData(resultData);

		ActionForward af = new ActionForward();
		af.setInput(input);
		af.setPath(path);
		this.setForward(af);

	}

	public void cancel() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		SchTxnBatchService schTxnBatchService = (SchTxnBatchService) appContext.getBean("SchTxnBatchService");

		String batchId = Utils.null2EmptyWithTrim(this.getParameter("batchId"));

		schTxnBatchService.cancel(batchId);
		listReport();

	}

}
