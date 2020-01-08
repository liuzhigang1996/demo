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

/**
 * @author hjs
 * 2006-11-7
 */
public class RcInwardRemInfo implements NTBResources {
	private static List objList = null;
	private static boolean refreshed = false;
	private NTBUser user = null;
	
	private final static String linkTemplate = "<a href=\"#\" onClick=\"popUpWindow('/cib/accEnquiry.do?ActionMethod=viewInwardInfo&key=%1', 800, 500);\">%2</a>";

	public RcInwardRemInfo() {
		if (!refreshed) {
			populate();
		}
	}
	
	public List getKeys() {
		return null;
	}

	public String getProperty(String key) {
		if (!key.equals("")) {
			Log.info("Inward Key not null objList size is  "+ objList.size() +",search begin time is : " + new Date());//add by linrui 20190602
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
					
					tmpLink = linkTemplate.replaceAll("%1", key) ;
					tmpLink =  tmpLink.replaceAll("%2", rbFactory.getString("view")) ;
					Log.info("Inward Key not nul ,search end time is : " + new Date());//add by linrui 20190602
					return tmpLink;
				}

			}
			Log.info("Inward nothing,end time is: " + new Date());//add by linrui 20190602
			return rbFactory.getString("notApp");
			
		} else {
			return "";
		}
	}
	
	public void populate() {
		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
		String sql = "select * from HS_INWARD_REMITTANCE_INFO Order by POSTING_DATE DESC, EFFECTIVE_DATE DESC, TRANS_NO DESC";
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
