/**
 * @author hjs
 * 2006-11-14
 */
package app.cib.action.rpt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bat.Payroll;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.service.bat.PayrollService;
import app.cib.service.sys.CorpAccountService;

/**
 * @author hjs
 * 2006-11-14
 */
public class PayrollReportAction extends CibAction {

	public void listReportLoad() throws NTBException {
		this.setResultData(new HashMap());
	}
	
	public void listReport() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		PayrollService payrollService = (PayrollService) appContext.getBean("PayrollService");
		
		CorpUser corpUser = (CorpUser) this.getUser();

		String date_range = Utils.null2EmptyWithTrim(this.getParameter("date_range"));
		String dateFrom = Utils.null2EmptyWithTrim(this.getParameter("dateFrom"));
		String dateTo = Utils.null2EmptyWithTrim(this.getParameter("dateTo"));

		List historyList = payrollService.listHistory(corpUser.getCorpId(), "", dateFrom, dateTo);
		historyList = this.convertPojoList2MapList(historyList);
		
		Map resultData = new HashMap();
		resultData.put("date_range", date_range);
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("historyList", historyList);
		setResultData(resultData);
		
	}
	
	public void viewReportDetail() throws NTBException {

		ApplicationContext appContext = Config.getAppContext();
		PayrollService payrollService = (PayrollService) appContext.getBean("PayrollService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		
		String payrollId = Utils.null2EmptyWithTrim(getParameter("payrollId"));
		Payroll payroll = payrollService.viewAvaliablePayroll(payrollId);
		List payrollRecList = payrollService.listUploadErrorDetailRec(payroll.getPayrollId());
		
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

}
