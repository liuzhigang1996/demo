/**
 * @author hjs
 * 2006-11-10
 */
package app.cib.action.rpt;

import java.util.*;

import org.springframework.context.ApplicationContext;

import com.neturbo.base.action.ActionForward;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

import app.cib.bo.rpt.ApproverSuspendReport;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.rpt.ApproverSuspendReportService;
import app.cib.service.sys.CorpUserService;
import app.cib.service.txn.TransferService;
import app.cib.util.TransferConstants;

/**
 * @author hjs
 * 2006-11-10
 */
public class ApprovalReportAction extends CibAction {

	private static final String defalutPattern = Config.getProperty("DefaultDatePattern");

	public void listApprovalReportLoad() throws NTBException {
		this.setResultData(new HashMap(1));
	}

	public void listApprovalReport() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		FlowEngineService flowService = (FlowEngineService)appContext.getBean("FlowEngineService");
		CorpUserService corpUserService = (CorpUserService)appContext.getBean("corpUserService");

		CorpUser corpUsre = (CorpUser) this.getUser();

		String userId = Utils.null2EmptyWithTrim(this.getParameter("userId"));
		String date_range = Utils.null2EmptyWithTrim(this.getParameter("date_range"));
		String dateFrom = Utils.null2EmptyWithTrim(this.getParameter("dateFrom"));
		String dateTo = Utils.null2EmptyWithTrim(this.getParameter("dateTo"));
		
		//add by lzg for GAPMC-EB-001-0040
		String corpId = corpUsre.getCorpId();
		
		NTBUser user = null;
		Date tmpDateFrom = null;
		Date tmpDateTo = null;
		//modified by lzg for GAPMC-EB-001-0040
		boolean userFlag=true;
		if (userId.equals("")) {
			user = null;
		}else if(corpId !=null && corpId.equals("")){
			user = null;
		} else {
			//user = corpUserService.load(userId);
			user = corpUserService.loadWithCorpId(userId, corpId);
			//add by wen_chy 20100128
			if(user==null){
				userFlag=false;
			}
			//end
		}
		//modified by lzg end
		Calendar cal = Calendar.getInstance();
		if (date_range.equals("all")){
			cal.add(Calendar.YEAR, -10);
			tmpDateFrom = cal.getTime();
			cal.setTime(new Date());
			cal.set(Calendar.HOUR, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			tmpDateTo = cal.getTime();
		} else {
			if (dateFrom.equals("")) {
				cal.add(Calendar.YEAR, -10);
				tmpDateFrom = cal.getTime();
			} else {
				tmpDateFrom = DateTime.getDateFromStr(dateFrom, defalutPattern);
			}
			if (dateTo.equals("")) {
				cal.setTime(new Date());
				cal.set(Calendar.HOUR, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				tmpDateTo = cal.getTime();
			} else {
				tmpDateTo = DateTime.getDateFromStr(dateTo, defalutPattern);
				cal.setTime(tmpDateTo);
				cal.add(Calendar.DATE, 1);
				cal.add(Calendar.SECOND, -1);
				tmpDateTo = cal.getTime();
			}
		}
		
		List reportList = null;
        if(userFlag)
    		reportList = flowService.listWorksByCorpAll(corpUsre, user, tmpDateFrom, tmpDateTo);

		Map resultData = new HashMap();
		resultData.put("date_range", date_range);
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("userId", userId);
                if(reportList == null){
                    reportList = new ArrayList();
                }
		resultData.put("reportList", reportList);
		setResultData(resultData);
	}

	public void listPendingStatusReportLoad() throws NTBException {
		this.setResultData(new HashMap(1));
	}

	public void listPendingStatusReport() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		FlowEngineService flowService = (FlowEngineService)appContext.getBean("FlowEngineService");
		//CorpUserService corpUserService = (CorpUserService)appContext.getBean("corpUserService");

		CorpUser corpUsre = (CorpUser) this.getUser();

		String userId = Utils.null2EmptyWithTrim(this.getParameter("userId"));
		String date_range = Utils.null2EmptyWithTrim(this.getParameter("date_range"));
		String dateFrom = Utils.null2EmptyWithTrim(this.getParameter("dateFrom"));
		String dateTo = Utils.null2EmptyWithTrim(this.getParameter("dateTo"));

		List reportList = flowService.listProcByCorpDealing(corpUsre.getCorpId(), userId, dateFrom, dateTo);
		reportList = this.convertPojoList2MapList(reportList);

		Map resultData = new HashMap();
		resultData.put("date_range", date_range);
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("userId", userId);
                if(reportList == null){
                    reportList = new ArrayList();
                }
		resultData.put("reportList", reportList);
		setResultData(resultData);

		/*
		NTBUser user = null;
		Date tmpDateFrom = null;
		Date tmpDateTo = null;
		if (userId.equals("")) {
			user = null;
		} else {
			user = corpUserService.load(userId);
		}
		Calendar cal = Calendar.getInstance();
		if (date_range.equals("all")){
			cal.add(Calendar.YEAR, -10);
			tmpDateFrom = cal.getTime();
			tmpDateTo = new Date();
		} else {
			if (dateFrom.equals("")) {
				cal.add(Calendar.YEAR, -10);
				tmpDateFrom = cal.getTime();
			} else {
				tmpDateFrom = DateTime.getDateFromStr(dateFrom, defalutPattern);
			}
			if (dateTo.equals("")) {
				tmpDateTo = new Date();
			} else {
				tmpDateTo = DateTime.getDateFromStr(dateTo, defalutPattern);
			}
		}

		List reportList = flowService.listWorkByCorpDealing(corpUsre, user, tmpDateFrom, tmpDateTo);
		*/

	}

	public void listSuspendReport() throws NTBException {

		ApplicationContext appContext = Config.getAppContext();
		ApproverSuspendReportService reportService =
			(ApproverSuspendReportService)appContext.getBean("ApproverSuspendReportService");

		CorpUser corpUser = (CorpUser) this.getUser();

		List reportList = reportService.listReport(corpUser.getCorpId());
		reportList = this.convertPojoList2MapList(reportList);

		Map resultData = new HashMap();
                if(reportList == null){
                    reportList = new ArrayList();
                }
		resultData.put("reportList", reportList);
		this.setResultData(resultData);

	}

	public void viewSuspendReportDetail() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");

		String transId = Utils.null2EmptyWithTrim(this.getParameter("transId"));
		String transType = Utils.null2EmptyWithTrim(this.getParameter("transType"));
		String subType = Utils.null2EmptyWithTrim(this.getParameter("subType"));

		Map resultData = new HashMap();

		Object pojo = null;
		String input = "/WEB-INF/pages/rpt/approval_report/approver_suspend_report_list.jsp";
		String path = null;
		if (transType.equals(ApproverSuspendReport.TRANS_TYPE_SCHEDULE)
				|| transType.equals(ApproverSuspendReport.TRANS_TYPE_SCHEDULE_JOB) ) {

			String frequnceType = Utils.null2EmptyWithTrim(this.getParameter("frequnceType"));
			String frequnceDays = Utils.null2EmptyWithTrim(this.getParameter("frequnceDays"));
			String scheduleName = Utils.null2EmptyWithTrim(this.getParameter("scheduleName"));
			resultData.put("frequenceType", frequnceType);
			resultData.put("frequenceDays", frequnceDays);
			resultData.put("scheduleName", scheduleName);

			if (subType.equals(TransferConstants.TRANSFER_TYPE_BANK)) {
				pojo = transferService.viewInBANK(transId);
				path = "/WEB-INF/pages/bat/schedule_transfer/schTransferView_InBANK.jsp";

			} else if (subType.equals(TransferConstants.TRANSFER_TYPE_MACAU)) {
				pojo = transferService.viewInMacau(transId);
				path = "/WEB-INF/pages/bat/schedule_transfer/schTransferView_InMacau.jsp";

			} else if (subType.equals(TransferConstants.TRANSFER_TYPE_OVERSEA)) {
				pojo = transferService.viewInOversea(transId);
				path = "/WEB-INF/pages/bat/schedule_transfer/schTransferView_InOversea.jsp";

			}
			resultData.put("customReturn", "2");

		} else if (transType.equals(ApproverSuspendReport.TRANS_TYPE_SCHEDULE_PAYROLL)) {

		}

		this.convertPojo2Map(pojo, resultData);
		//resultData.put("fromReport", "Y");
		setResultData(resultData);

		ActionForward af = new ActionForward();
		af.setInput(input);
		af.setPath(path);
		this.setForward(af);

	}
}
