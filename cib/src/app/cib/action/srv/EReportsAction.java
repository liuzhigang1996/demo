/**
 * @author hjs
 * 2007-4-24
 */
package app.cib.action.srv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.service.srv.EReportsService;
import app.cib.util.StatementToken;

/**
 * @author hjs
 * 2007-4-24
 */
public class EReportsAction extends CibAction {
	
	public void listDocLoad() throws NTBException {
		this.clearUsrSessionDataValue();
		this.setResultData(new HashMap(1));
		listDoc();
	}
	
	public void listDoc() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		EReportsService reportsService = (EReportsService) appContext.getBean("EReportsService");
		
		CorpUser corpUser = (CorpUser) this.getUser();
		String rangeType = this.getParameter("rangeType");
		String range = this.getParameter("range");
		String dateFrom = this.getParameter("dateFrom");
		String dateTo = this.getParameter("dateTo");
		String operation = this.getParameter("operation");
		String reports = this.getParameter("reports");
		
		List docList = reportsService.list(corpUser.getCorpId(), range, dateFrom, dateTo,
				operation, reports);
		
		Map row = null;
		for(int i=0; i<docList.size(); i++){
			row = (Map) docList.get(i);
			row.put("FILE_LINK", reportsService.getHyperLink(corpUser, Utils.null2EmptyWithTrim(row.get("PDF_FILE_NAME"))));
			row.put("KEY", "O-" + StatementToken.getToken(Utils.null2EmptyWithTrim(row.get("PDF_FILE_NAME"))));
		}

		Map resultData = new HashMap();
		resultData.put("rangeType", rangeType);
		resultData.put("range", range);
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("operation", operation);
		resultData.put("reports", reports);
		resultData.put("docList", docList);
		resultData.put("listSize", String.valueOf(docList.size()));
		setResultData(resultData);
		
	}

}
