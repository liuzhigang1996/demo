package app.cib.util;

import java.util.*;

import org.springframework.context.ApplicationContext;
import com.neturbo.set.core.*;
import com.neturbo.set.database.*;
import com.neturbo.set.utils.*;

public class RcCurrencyAutopay implements NTBResources {
    private static List objList = null;
    private static boolean refreshed = false;
    public RcCurrencyAutopay() {
        if(!refreshed){
            populate();
        }
    }
	public List getKeys() {
		List keys = new ArrayList();
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String ccyCode = (String) obj.get("CCY_CODE");
           
            keys.add(ccyCode);
        }
        return keys;
	}
	public String getProperty(String key) {
		for(int i=0; i < objList.size(); i++){
            Map obj = (Map)objList.get(i);
            String ccyCode = (String) obj.get("CCY_CODE");
            String ccyName = (String) obj.get("CCY_LONG_NAME");
            if(ccyCode.equals(key)){
                return ccyCode+" - "+ccyName;
            }
        }
        return null;
	}
	public void populate() {
		ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
        //Jet modified 2007-12-4
//        String sql = " SELECT HS_CURRENCY.CCY_CODE, HS_CURRENCY_NAME.CCY_LONG_NAME FROM HS_CURRENCY,HS_CURRENCY_NAME WHERE HS_CURRENCY.CCY_CODE=HS_CURRENCY_NAME.CCY_CODE AND HS_CURRENCY.MIN_AMT_OTHER_BANKS <> 0 ORDER BY SORTING_SEQ_MACAU";
        String sql = " SELECT c.CCY_CODE, cn.CCY_LONG_NAME FROM HS_CURRENCY c,HS_CURRENCY_NAME cn,HS_EXCHANGE_RATE er WHERE (c.CCY_CODE=cn.CCY_CODE AND c.CCY_CODE=er.CCY_CODE AND er.TT_BUY_RATE_MOP > 0) OR (c.CCY_CODE=cn.CCY_CODE AND c.CCY_CODE=er.CCY_CODE AND c.CCY_CODE='MOP') ORDER BY SORTING_SEQ_MACAU";
        try {
            objList = dao.query(sql, new Object[] {});
            refreshed = true;
        } catch (Exception e) {
            Log.error("Error loading group resource", e);
            refreshed = false;
        }
		
	}
	public void setArgs(Object argObj) {
		// TODO Auto-generated method stub
		
	}
}