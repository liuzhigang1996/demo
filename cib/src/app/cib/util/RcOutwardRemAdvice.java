/**
 * @author hjs
 * 2006-11-7
 */
package app.cib.util;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.utils.NTBResources;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 2006-11-7
 */
public class RcOutwardRemAdvice implements NTBResources {
	private static List objList = null;
	private static boolean refreshed = false;
	private NTBUser user = null;
	
//	private final static String linkTemplate = "<a href=\"/cib/DownloadStatement?accNo=%1&key=%2\">%3</a>";

	public RcOutwardRemAdvice() {
		if (!refreshed) {
			populate();
		}

	}

	public List getKeys() {
		return null;
	}

	public String getProperty(String key) {
		if (!key.equals("")) {
			Log.info("Outward Key not null objList size is  "+ objList.size() +",search begin time is : " + new Date());//add by linrui 20190602
			String[] keys = key.split(",");
			Locale locale = ((user==null || user.getLanguage()==null)) ? Config.getDefaultLocale() : user.getLanguage();
			RBFactory rbFactory = RBFactory.getInstance("app.cib.resource.enq.statement_Status", locale.toString());

			Map obj = null;
			String accNo = "";
			String postingDate = "";
			String effectiveDate = "";
			String tellerNo = "";
			String seqNo = "";
			String transNo = "";
			String fileName = "";
			String tmpLink = "";
			for (int i = 0; i < objList.size(); i++) {
				obj = (Map) objList.get(i);
				accNo = obj.get("ACCOUNT_NO").toString().trim();
				postingDate = obj.get("POSTING_DATE").toString().trim();
				effectiveDate = obj.get("EFFECTIVE_DATE").toString().trim();
				tellerNo = obj.get("TELLER_NO").toString().trim();
				seqNo = obj.get("SEQ_NO").toString().trim();
				transNo = obj.get("TRANS_NO").toString().trim();

				if (accNo.equals(keys[0].trim()) && postingDate.equals(keys[1].trim())
						&& effectiveDate.equals(keys[2].trim()) && tellerNo.equals(keys[3].trim())
						&& seqNo.equals(keys[4].trim()) && transNo.equals(keys[5].trim())) {
					
					fileName = Utils.null2EmptyWithTrim(obj.get("PDF_FILENAME")) ;
					/*tmpLink = linkTemplate.replaceAll("%1", accNo) ;
					tmpLink =  tmpLink.replaceAll("%2", "O-" + StatementToken.getToken(fileName)) ;
					tmpLink =  tmpLink.replaceAll("%3", rbFactory.getString("view")) ;*/
					tmpLink = rbFactory.getString("view");
					Log.info("Outward Key not nul ,search end time is : " + new Date());//add by linrui 20190602
					return tmpLink;
				}

			}
			return rbFactory.getString("notApp");
			
		} else {
			return "";
		}
	}

	public void populate() {
		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
		String sql = "select * from HS_OUTWARD_REMITTANCE_ADVICE order by POSTING_DATE DESC, EFFECTIVE_DATE DESC, TRANS_NO DESC";
		try {
			objList = dao.query(sql, new Object[] {});
		} catch (Exception e) {
			Log.error("Error loading group resource", e);
		}
		refreshed = true;
	}

	public void setArgs(Object argObj) {
		if (argObj instanceof NTBUser) {
			user = (NTBUser) argObj;
		}

	}

}
