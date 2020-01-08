/**
 * @author hjs
 * 2006-10-3
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
 * 2006-10-3
 */
public class RcCurrencyComboBox implements NTBResources {

    private static List objList = null;
    private static boolean refreshed = false;
    private NTBUser user;//add by linrui 20180517
    public RcCurrencyComboBox() {
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
            String ccyName = (String) obj.get("CCY_LONG_NAME");
            String langCode = (String) obj.get("LOCAL_CODE");//add by linrui 20180517
//            if(ccyCode.equals(key)){
            if(ccyCode.equals(key)&& langCode.equals(lang)){//mod by linrui 20180517
                return ccyCode+" - "+ccyName;
            }
        }
        return null;
	}

	public void populate() {
		ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
//        String sql = " select t1.CCY_CODE, t2.CCY_LONG_NAME FROM HS_CURRENCY t1 left join HS_CURRENCY_NAME t2 on t1.CCY_CODE = t2.CCY_CODE ORDER BY t1.SEQNO_IN_ZONE";
        String sql = " SELECT c.CCY_CODE, cn.CCY_LONG_NAME, cn.LOCAL_CODE FROM HS_CURRENCY c,HS_CURRENCY_NAME cn,HS_EXCHANGE_RATE er WHERE c.CCY_CODE=cn.CCY_CODE AND c.CCY_CODE=er.CCY_CODE AND er.TT_BUY_RATE_MOP > 0 ";//mod by linrui 20180517
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
