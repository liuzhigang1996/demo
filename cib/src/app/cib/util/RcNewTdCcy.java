/**
 * @author hjs
 * 2007-1-10
 */
package app.cib.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.utils.NTBResources;

/**
 * @author hjs
 * 2007-1-10
 */
public class RcNewTdCcy implements NTBResources {
    private static List objList = null;
    private static boolean refreshed = false;
    private NTBUser user;
    
    public RcNewTdCcy() {
        if(!refreshed){
            populate();
        }
    }

	public List getKeys() {
        List keys = new ArrayList();
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String ccy = (String) obj.get("CCY");
            if (!keys.contains(ccy)) {
            	keys.add(ccy);
            }
        }
        return keys;
	}

	public String getProperty(String key) {
		String lang = user.getLangCode();
        for(int i=0; i < objList.size(); i++){
            Map obj = (Map)objList.get(i);
            String ccy = (String) obj.get("CCY");
            String ccyLongName = (String) obj.get("CCY_LONG_NAME");
            String langCode = (String) obj.get("LOCAL_CODE");
            if(ccy.equals(key) && langCode.equals(lang)){
                return ccyLongName;
            }
        }
        return null;
	}

	public void populate() {
        ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
        //modified by lzg 20190701
		//String sql = "select distinct CCY, concat(concat(CCY, ' - '), CCY_LONG_NAME) as CCY_LONG_NAME from HS_TD_ACCOUNT_PRODUCT_NO t1, HS_CURRENCY_NAME t2 where t1.CCY=t2.CCY_CODE and t1.RESISDENT_FLAG = ?";
        //String sql = "select distinct CCY, concat(concat(CCY, ' - '), CCY_LONG_NAME) as CCY_LONG_NAME from HS_TD_ACCOUNT_PRODUCT_NO t1, HS_CURRENCY_NAME t2 where t1.CCY=t2.CCY_CODE and t2.CCY_CODE in (SELECT CCY_CODE FROM available_currencies where available_flag = 'Y') and  t1.RESISDENT_FLAG = ?";
        String sql = "select distinct CCY, concat(concat(CCY, ' - '), CCY_LONG_NAME) as CCY_LONG_NAME ,t2.LOCAL_CODE from HS_TD_ACCOUNT_PRODUCT_NO t1, HS_CURRENCY_NAME t2 where t1.CCY=t2.CCY_CODE and t2.CCY_CODE in ('MOP','HKD') and  t1.RESISDENT_FLAG = ?";
        //modified by lzg end
        try {
            objList = dao.query(sql, new Object[]{"R"});
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
