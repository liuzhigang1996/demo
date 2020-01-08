package app.cib.util;
import java.util.*;

import org.springframework.context.ApplicationContext;

import app.cib.bo.sys.CorpUser;

import com.neturbo.set.core.*;
import com.neturbo.set.database.*;
import com.neturbo.set.utils.*;
public class RcCurrencyCBS implements NTBResources {
    private static List objList = null;
    private static boolean refreshed = false;
    private NTBUser user;
    public RcCurrencyCBS() {
        if(!refreshed){
            populate();
        }
    }
	public List getKeys() {
		List keys = new ArrayList();
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String ccyCode = (String) obj.get("CCY_CODE");
            if (!keys.contains(ccyCode)) {
            	keys.add(ccyCode);
            }
        }
        return keys;
	}
	public String getProperty(String key) {
		String lang = user.getLangCode();
		for(int i=0; i < objList.size(); i++){
            Map obj = (Map)objList.get(i);
            String ccyCode = (String) obj.get("CCY_CODE");
            String ccyCbsCode = (String) obj.get("CCY_CODE_CBS");
            String ccyName = (String) obj.get("CCY_NAME");
            String langCode = (String) obj.get("LOCAL_CODE");
            if((ccyCbsCode.equals(key) && langCode.equals(lang))||(ccyCode.equals(key) && langCode.equals(lang))){
                return ccyName;
            }
        }
        return key;
	}
	public String getProperty(CorpUser user,String key){
		setArgs(user);
		return getProperty(key);
	}
	public void populate() {
		ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
        String sql = "select c.LOCAL_CODE , c.CCY_CODE,c.CCY_NAME,cn.CCY_CODE_CBS from "+
                       "HS_CURRENCY_NAME c,HS_CURRENCY cn where  c.CCY_CODE=cn.CCY_CODE";
        try {
            objList = dao.query(sql, new Object[] {});
            refreshed = true;
        } catch (Exception e) {
            Log.error("Error loading group resource", e);
            refreshed = false;
        }
		
	}
	public void setArgs(Object argObj) {
		if (argObj instanceof NTBUser) {
			user = (NTBUser) argObj;
		}
	}
}
