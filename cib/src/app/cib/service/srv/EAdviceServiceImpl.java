/**
 * @author hjs
 * 2007-4-24
 */
package app.cib.service.srv;

import java.util.List;
import java.util.Locale;

import app.cib.dao.srv.EAdviceDao;
import app.cib.util.StatementToken;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.RBFactory;

/**
 * @author hjs
 * 2007-4-24
 */
public class EAdviceServiceImpl implements EAdviceService {

	private final static String linkTemplate = "<a href=\"/cib/DownloadStatement?accNo=%1&key=%2\">%3</a>";
	
	private EAdviceDao eadviceDao;

	public EAdviceDao getEadviceDao() {
		return eadviceDao;
	}

	public void setEadviceDao(EAdviceDao eadviceDao) {
		this.eadviceDao = eadviceDao;
	}

	public String getHyperLink(NTBUser user, String fileName)
			throws NTBException {
		
		Locale locale = (user.getLanguage()==null) ? Config.getDefaultLocale() : user.getLanguage();
		RBFactory rbFactory = RBFactory.getInstance("app.cib.resource.enq.statement_Status", locale.toString());
		
		if (!fileName.equals("")) {
			String tmpLink = linkTemplate.replaceAll("%1", "") ;
			tmpLink =  tmpLink.replaceAll("%2", "O-" + StatementToken.getToken(fileName)) ;
			tmpLink =  tmpLink.replaceAll("%3", rbFactory.getString("view")) ;
			return tmpLink;
		} else {
			return rbFactory.getString("notApp");
		}
	}

	public List list(String corpId, String range, String dateFrom,
			String dateTo, String accountType, String accountNo,
			String operation, String refType, String reference) throws NTBException {
		return eadviceDao.listRecords(corpId, range, dateFrom, dateTo,
				accountType, accountNo, operation, refType, reference);
	}

}
