/**
 * @author hjs
 * 2007-4-23
 */
package app.cib.service.srv;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.ApplicationContext;

import app.cib.dao.srv.EStatementDao;
import app.cib.util.StatementToken;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 2007-4-23
 */
public class EStatementServiceImpl implements EStatementService{

	private final static String linkTemplate = "<a name = \"downloadHref\" class = 'estatement_a_href'  style = \"cursor: pointer;padding-right: 20px;\"  href=\"/cib/DownloadStatement?key=%1\">%2</a>";
	private EStatementDao estatementDao;

	public EStatementDao getEstatementDao() {
		return estatementDao;
	}

	public void setEstatementDao(EStatementDao estatementDao) {
		this.estatementDao = estatementDao;
	}

	public List list(String corpId, String range, String dateFrom, String dateTo, String accountType, String accountNo) throws NTBException {
		return estatementDao.listRecords(corpId, range, dateFrom, dateTo, accountType, accountNo);
	}

	public String getHyperLink(NTBUser user, String fileName) throws NTBException {
		
		Locale locale = (user.getLanguage()==null) ? Config.getDefaultLocale() : user.getLanguage();
		RBFactory rbFactory = RBFactory.getInstance("app.cib.resource.enq.statement_Status", locale.toString());
		
		if (!fileName.equals("")) {
//			String tmpLink = linkTemplate.replaceAll("%1", "") ;
			String tmpLink =  linkTemplate.replaceAll("%1", "S-" + StatementToken.getToken(fileName)) ;
			tmpLink =  tmpLink.replaceAll("%2", rbFactory.getString("download")) ;
			return tmpLink;
		} else {
			return rbFactory.getString("notApp");
		}
	}
	//add by linrui 20190907 for security
	public boolean isBelongCorpId(String corpId, String fileName) {
		String sql = "SELECT DISTINCT CI_NO FROM E_STATEMENT WHERE PDF_FILE_NAME = ? ";
		Object[] valueObjArray = { fileName };
		String corpIdInForm = null;
		try {
			corpIdInForm = Utils.null2EmptyWithTrim(estatementDao.querySingleValue(sql, valueObjArray));
			if (corpIdInForm.equals(corpId)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			Log.error("SELECT E-STATEMENT ERROR", e);
			return false;
		}
	}
	
	public List listEstatement(String functionName, String stmtType, String corpId, String range, String dateFrom, String dateTo, String accountNo) throws NTBException {
		Log.info("====functionName : " + functionName);
		Log.info("====stmtType : " + stmtType);
		Log.info("====corpId : " + corpId);
		Log.info("====range : " + range);
		Log.info("====dateFrom : " + dateFrom);
		Log.info("====dateFrom : " + dateTo);
		Log.info("====accountNo : " + accountNo);
		return estatementDao.listHisRecords(functionName, stmtType, corpId, range, dateFrom, dateTo, accountNo);
	}

	public void changeMarkRead(String interalId, String reportDate) throws NTBException {
		try {
			estatementDao.changeMarkRead(interalId,reportDate);
		} catch (Exception e) {
			throw new NTBException("err.sys.DBError");
		}
		
	}
	
	public void readCheckAll(String stmtType, String actionMethod,String ciNo) throws NTBException {
		try {
			estatementDao.readCheckAll(stmtType,actionMethod,ciNo);
		} catch (Exception e) {
			throw new NTBException("err.sys.DBError");
		}
	}

	public boolean getReadFlag(String reportCode,String CiNo) throws NTBException {
		try {
			return estatementDao.getReadFlag(reportCode,CiNo);
		} catch (Exception e) {
			throw new NTBException("err.sys.DBError");
		}
	}
	public boolean getReadFlagByActionMethod(String ActionMethod,String CiNo) throws NTBException {
		try {
			return estatementDao.getReadFlagByActionMethod(ActionMethod,CiNo);
		} catch (Exception e) {
			throw new NTBException("err.sys.DBError");
		}
	}

	@Override
	public void changeMarkReadByPdfName(String estatementName) throws NTBException {
		try {
			estatementDao.changeMarkReadByPdfName(estatementName);
		} catch (Exception e) {
			throw new NTBException("err.sys.DBError");
		}
		
	}

	
}
