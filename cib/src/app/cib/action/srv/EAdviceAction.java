/**
 * @author hjs
 * 2007-4-24
 */
package app.cib.action.srv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.service.srv.EAdviceService;
import app.cib.service.sys.CorpAccountService;
import app.cib.util.StatementToken;

/**
 * @author hjs
 * 2007-4-24
 */
public class EAdviceAction extends CibAction {
	
	public void listDocLoad() throws NTBException {
		this.clearUsrSessionDataValue();
		this.setResultData(new HashMap(1));
		this.getParameters().put("range", "1");
		listDoc();
	}
	
	public void listDoc() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		EAdviceService adviceService = (EAdviceService) appContext.getBean("EAdviceService");
		
		CorpAccountService accService = (CorpAccountService) appContext.getBean("CorpAccountService");
		CorpUser corpUser = (CorpUser) this.getUser();
		String rangeType = this.getParameter("rangeType");
		String range = this.getParameter("range");
		String dateFrom = this.getParameter("dateFrom");
		String dateTo = this.getParameter("dateTo");
		String accountType = this.getParameter("accountType");
		String accountNo = this.getParameter("accountNo");
		String operation = this.getParameter("operation");
		String refType = this.getParameter("refType");
		String reference = this.getParameter("reference");
		
		List docList = adviceService.list(corpUser.getCorpId(), range, dateFrom, dateTo,
				accountType, accountNo, operation, refType, reference);
		
		List priviledgeList = accService.listAllPrivilegedAccount((CorpUser)this.getUser(),
				accountType);
		
		//mod by chen_y for CR192 20161117
		//刷选出有权限的账户的结单
		Map row = null;
		List eDocList = new ArrayList();
		for(int i=0; i<docList.size();i++){
			row = (Map) docList.get(i);
			//boolean isExsit = false;
			for(int j=0; j<priviledgeList.size(); j++){
				
				CorpAccount corpAccount = (CorpAccount) priviledgeList.get(j);
				
				if(corpAccount.getAccountNo().equals((String)row.get("ACCOUNT_NO"))){
					eDocList.add(row);
					break;
				}
			}
		}
		
		//Map row = null;
		for(int i=0; i<eDocList.size(); i++){
			row = (Map) eDocList.get(i);
			row.put("FILE_LINK", adviceService.getHyperLink(corpUser, Utils.null2EmptyWithTrim(row.get("PDF_FILE_NAME"))));
			row.put("KEY", "O-" + StatementToken.getToken(Utils.null2EmptyWithTrim(row.get("PDF_FILE_NAME"))));
		}

		Map resultData = new HashMap();
		resultData.put("rangeType", rangeType);
		resultData.put("range", range);
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("accountType", accountType);
		resultData.put("accountNo", accountNo);
		resultData.put("operation", operation);
		resultData.put("refType", refType);
		resultData.put("reference", reference);
		resultData.put("docList", eDocList);
		resultData.put("listSize", String.valueOf(eDocList.size()));
		setResultData(resultData);
		
	}

}
