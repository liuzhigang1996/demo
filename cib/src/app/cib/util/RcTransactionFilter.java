package app.cib.util;

import java.util.*;
import com.neturbo.set.database.GenericJdbcDao;
import app.cib.bo.sys.CorpUser;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;

import org.springframework.context.ApplicationContext;
import com.neturbo.set.core.Config;
import com.neturbo.set.utils.NTBResources;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class RcTransactionFilter implements NTBResources{
    private static List objList = null;
    private static Map objMap = null;
    private NTBUser user;
    
  //add by long_zg 2014-01-08 for CR192 bob batch
    private String appCode = null ;
    
    private static boolean refreshed = false;
    
  //add by long_zg 2014-01-08 for CR192 bob batch
   /* public RcTransactionFilter() {*/
    public RcTransactionFilter(String appCode) {
    	//add by long_zg 2014-01-08 for CR192 bob batch
    	this.appCode = appCode ;
        if (!refreshed) {
            populate();
        }
    }

    public List getKeys() {
        List keys = new ArrayList();
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
          //add by long_zg 2014-01-08 for CR192 bob batch
            String objAppCode = (String)obj.get("APP_CODE") ;
            if(!objAppCode.equals(appCode)){
            	continue ;
            }
            
            String key = (String) obj.get("KEY");
            if (!keys.contains(key)) {
                keys.add(key);
            }
        }
        return keys;
    }

    public String getProperty(String key) {
		String lang = user.getLangCode();
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String filterKey = (String) obj.get("KEY");
            String filterName = (String) obj.get("FILTER_NAME");
            String langCode = (String) obj.get("LOCAL_CODE");
            if (filterKey.equals(key) && langCode.equals(lang)){
                return filterName;
            }
        }
        return null;
    }

    public Map getObject(String key) {
        Map obj = (Map) objMap.get(key);
        return obj;
    }

    public void populate() {
        ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean(
                "genericJdbcDao");
        String sql = " SELECT t1.APP_CODE,t1.FILTER_CODE,t1.LOCAL_CODE,FILTER_NAME,FILTER_TYPE,FILTER_VALUE" +
                     " FROM HS_TRANSACTION_FILTER_CODE t1 LEFT OUTER JOIN HS_TRANSACTION_FILTER_VALUE t2" +
                     " ON t1.APP_CODE=t2.APP_CODE and t1.FILTER_CODE=t2.FILTER_CODE";
        try {
            objList = dao.query(sql, new Object[] {});
            objMap = new HashMap();
            for(int i=0; i< objList.size(); i++){
                Map obj = (Map)objList.get(i);
                String key = (String)obj.get("APP_CODE") + (String)obj.get("FILTER_CODE");
                obj.put("KEY", key);
                if (!objMap.containsKey(key)) {
                    objMap.put(key, obj);
                }
            }
            refreshed = true;
        } catch (Exception e) {
            Log.error("Error loading group resource", e);
            refreshed = false;
        }
    }

    public void setArgs(Object obj) {
		if (obj instanceof NTBUser) {
			user = (NTBUser) obj;
		}
    }
}
