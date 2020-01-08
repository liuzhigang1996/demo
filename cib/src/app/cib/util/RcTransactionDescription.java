package app.cib.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.utils.NTBResources;

public class RcTransactionDescription implements NTBResources{
	private static List objList = null;
    private static Map objMap = null;
    private static boolean refreshed = false;
    private NTBUser user;
    public RcTransactionDescription() {
        if (!refreshed) {
            populate();
        }
    }

	public List getKeys() {
		List keys = new ArrayList();
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String mapString = (String) obj.get("MAP_STRING");
            keys.add(mapString);
        }
        return keys;
	}

	public String getProperty(String key) {
		 for (int i = 0; i < objList.size(); i++) {
	            Map obj = (Map) objList.get(i);
	            String mapString = (String) obj.get("MAP_STRING");
	            String desc = (String) obj.get("TRANS_DESCRIPTION");
	            if (mapString.equals(key)) {
	                return desc;
	            }
	        }
	        return null;
	}

    public Map getObject(String key) {
        Map obj = (Map) objMap.get(key);
        return obj;
    }
    //add by linrui for cr230
    public String getProperties(String key ,String lang) {
//    	String lang = user.getLangCode();
		for(int i=0; i < objList.size(); i++){
            Map obj = (Map)objList.get(i);
            String mapString = (String) obj.get("MAP_STRING");
            String desc = (String) obj.get("TRANS_DESCRIPTION");
            String langCode = (String) obj.get("LOCAL_CODE");
            if(mapString.equals(key) && langCode.equals(lang)){
                return desc;
            }
        }
        return "";
    }
    //end

	public void populate() {
	       ApplicationContext appContext = Config.getAppContext();
	        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean(
	                "genericJdbcDao");
	        String sql = " SELECT LOCAL_CODE, MAP_STRING, TRANS_DESCRIPTION FROM HS_TRANSACTION_DESCRIPTION";
	        try {
	            objList = dao.query(sql, new Object[] {});
	            objMap = new HashMap();
	            for(int i=0; i< objList.size(); i++){
	                Map obj = (Map)objList.get(i);
	                String key = (String)obj.get("MAP_STRING");
	                objMap.put(key, obj);
	            }
	            refreshed = true;
	        } catch (Exception e) {
	            Log.error("Error loading group resource", e);
                    refreshed = false;
	        }

	}

	public void setArgs(Object argObj) {
		//add by linrui for cr230 begin
		if (argObj instanceof NTBUser) {
			user = (NTBUser) argObj;
		}
		//end

	}

}
