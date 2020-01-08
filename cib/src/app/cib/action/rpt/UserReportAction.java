/**
 * @author hjs
 * 2006-11-9
 */
package app.cib.action.rpt;

import java.util.*;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.service.rpt.UserReportService;

/**
 * @author hjs
 * 2006-11-9
 */
public class UserReportAction extends CibAction {
	
	public void listActivityLogReportLoad() throws NTBException {
		this.setResultData(new HashMap(1));
	}

	public void listActivityLogReport() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		UserReportService userReportService = (UserReportService) appContext.getBean("UserReportService");
		
		CorpUser corpUser = (CorpUser) this.getUser();

		String userId = Utils.null2EmptyWithTrim(this.getParameter("userId"));
		String date_range = Utils.null2EmptyWithTrim(this.getParameter("date_range"));
		String dateFrom = Utils.null2EmptyWithTrim(this.getParameter("dateFrom"));
		String dateTo = Utils.null2EmptyWithTrim(this.getParameter("dateTo"));

		List reportList = userReportService.listActivityLogReport(corpUser.getCorpId(), userId, dateFrom, dateTo);

		Map resultData = new HashMap();
		resultData.put("date_range", date_range);
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("userId", userId);
		resultData.put("reportList", reportList);
		setResultData(resultData);
		
	}
}
