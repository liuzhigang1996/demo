package app.cib.dao.srv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.cib.util.Constants;

import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

/**
 * @author linrui
 * 
 */
public class TransferPromptDao extends GenericHibernateDao{
	public static String STATUS_NORMAL = "0";
	//public static String STATUS_ADD = "1";
	//public static String STATUS_UPD = "2";
	public static String STATUS_PEDNING_APPAOVEL = "1";
	public static String STATUS_DELETE = "9";

	/*//add by linrui 20190923 for estatement
		public String listContByTxnType(String txnType, String lang) throws NTBException {
			String sql = "SELECT CONTENT FROM HS_TXN_MESSAGE WHERE TXN_TYPE = ? AND LOCAL_CODE = ? AND STATUS= '0' ";
			try {
				List contentList = this.query(sql, new Object[] { txnType, lang });
				if(contentList.size()>0){
//					Map contentMap = (Map)contentList.get(0);
//					String content = contentMap.get("CONTENT").toString();
					String content = ((Map)contentList.get(0)).get("CONTENT").toString();
					return content;
				}else{
					return "";
				}
				
			} catch (Exception e) {
				Log.error("DB Error", e);
				throw new NTBException("err.sys.DBError");
			}
		}
		//conditionMap : LOCAL_CODE,TXN_TYPE,CONTENT,STATUS
		public void addContentByTypeAndId(Map conditionMap) throws Exception {
			String sql = "INSERT INTO HS_TXN_MESSAGE(LOCAL_CODE,TXN_TYPE,CONTENT,STATUS) " + 
                         "values( ? , ? , ? , ? , ? , ? )";
			List valueList = new ArrayList();
			valueList.add(conditionMap.get("lang").toString());
			valueList.add(conditionMap.get("txnType").toString());
			valueList.add(conditionMap.get("content").toString());
			valueList.add(conditionMap.get("status").toString());
			this.update(sql, valueList.toArray());
		}
		//update content only
		public void updateContentByTypeAndId(String contentId, String txnType, String content) throws Exception {
			String sql = "update E_STATEMENT set MARK_READ = 'Y' where INTERNAL_ID = ? and REPORT_DATE = ?";
			String[] conditonParameter = new String[]{interalId,reportDate};
			this.update(sql, conditonParameter);
		}
		//update title only
		public void updateTitleByType(String txnType, String title) throws Exception {
			String sql = "update E_STATEMENT set MARK_READ = 'Y' where INTERNAL_ID = ? and REPORT_DATE = ?";
			String[] conditonParameter = new String[]{interalId,reportDate};
			this.update(sql, conditonParameter);
		}
		public void deleteContentByTypeAndId(String contentId, String txnType, String content) throws Exception {
			String sql = "update E_STATEMENT set MARK_READ = 'Y' where INTERNAL_ID = ? and REPORT_DATE = ?";
			String[] conditonParameter = new String[]{interalId,reportDate};
			this.update(sql, conditonParameter);
			
		}
		//approve
*/
}
