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
import app.cib.service.srv.EFormsService;
import app.cib.util.StatementToken;

/**
 * @author hjs
 * 2007-4-24
 */
public class EFormsAction extends CibAction {
	
	public void listDocLoad() throws NTBException {
		this.clearUsrSessionDataValue();
		this.setResultData(new HashMap(1));
		listDoc();
	}
	
	public void listDoc() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		EFormsService formsService = (EFormsService) appContext.getBean("EFormsService");
		
		CorpUser corpUser = (CorpUser) this.getUser();
		String typeOfForm = this.getParameter("typeOfForm");
		
		List docList = formsService.list(corpUser.getCorpId(), typeOfForm);
		
		Map row = null;
		for(int i=0; i<docList.size(); i++){
			row = (Map) docList.get(i);
			row.put("FILE_LINK", formsService.getHyperLink(corpUser, Utils.null2EmptyWithTrim(row.get("PDF_FILE_NAME"))));
			row.put("KEY", "O-" + StatementToken.getToken(Utils.null2EmptyWithTrim(row.get("PDF_FILE_NAME"))));
		}

		Map resultData = new HashMap();
		resultData.put("typeOfForm", typeOfForm);
		resultData.put("docList", docList);
		resultData.put("listSize", String.valueOf(docList.size()));
		setResultData(resultData);
		
	}

}
