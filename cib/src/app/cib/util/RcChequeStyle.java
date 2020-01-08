package app.cib.util;

import java.util.*;

import org.springframework.context.ApplicationContext;

import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;

import com.neturbo.set.core.*;
import com.neturbo.set.database.*;
import com.neturbo.set.utils.*;

public class RcChequeStyle implements NTBResources {
    private static List objList = null;
    private static boolean refreshed = false;
    private Map permMap = null;
    private String corpId = null;
    public RcChequeStyle() {
        if(!refreshed){
            populate();
        }
    }
	public List getKeys() {
		List keys = new ArrayList();
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String corpId1 = (String) obj.get("CORP_ID");
            corpId1 = corpId1.trim();
            String styleCode = (String) obj.get("STYLE_CODE");
            styleCode = styleCode.trim();
            if (corpId1.equals(corpId)) {
            keys.add(styleCode);
            }
            
        }
        return keys;
	}
	public String getProperty(String key) {
		for(int i=0; i < objList.size(); i++){
            Map obj = (Map)objList.get(i);
            String corpId1 = (String) obj.get("CORP_ID");
            corpId1 = corpId1.trim();
            String styleCode = (String) obj.get("STYLE_CODE");
            styleCode = styleCode.trim();
           if(styleCode.equals(key)){
                return styleCode;
            }
        }
        return null;
	}
	public void populate() {
		ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
       
        String sql = "Select STYLE_CODE ,CORP_ID from HS_CHEQUE_BOOK_STYLE";
        try {
            objList = dao.query(sql, new Object[] {});
            refreshed = true;
        } catch (Exception e) {
            Log.error("Error loading group resource", e);
            refreshed = false;
        }
		
	}
	 public Map getObject(String key) {
	        for (int i = 0; i < objList.size(); i++) {
	            Map obj = (Map) objList.get(i);
	            String corpId1 = (String) obj.get("CORP_ID");
	            corpId1 = corpId1.trim();
	            if (corpId1.equals(corpId)) {
	                return obj;
	            }
	        }
	        return null;
	    }

	public void setArgs(Object obj) {
        permMap = new HashMap();
        corpId = "";
        if (obj instanceof CorpUser) {
            CorpUser user = (CorpUser) obj;
            corpId = user.getCorpId();
            
            List permList = user.getAccountList();
            for (int i = 0; i < permList.size(); i++) {
                NTBPermission perm = (NTBPermission) permList.get(i);
                permMap.put(perm.getPermissionResource(), perm);
            }
        }

    }
}