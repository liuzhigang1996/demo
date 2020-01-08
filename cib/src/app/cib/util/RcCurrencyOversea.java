package app.cib.util;

import java.util.*;

import org.springframework.context.ApplicationContext;
import com.neturbo.set.core.*;
import com.neturbo.set.database.*;
import com.neturbo.set.utils.*;

import app.cib.bo.sys.*;

public class RcCurrencyOversea implements NTBResources {
    private static List objList = null;
    private static boolean refreshed = false;
    private NTBUser user;
    public RcCurrencyOversea() {
        if(!refreshed){
            populate();
        }
    }
	public List getKeys() {
		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
		String availableSql = "SELECT CCY_CODE FROM available_currencies where available_flag = 'Y'";
		List availableCcy = new ArrayList();
		try {
			availableCcy = dao.query(availableSql, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		List keys = new ArrayList();
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String ccyCode = (String) obj.get("CCY_CODE");
            boolean flag = false;
            for (int j = 0; j < availableCcy.size(); j++) {
            	String ccy = (String) ((HashMap)availableCcy.get(j)).get("CCY_CODE");
            	if(ccy.equals(ccyCode)){
            		flag = true;
            		break;
            	}
    		}
            if(flag){
	            if (!keys.contains(ccyCode)) {
	                keys.add(ccyCode);
	            }
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
            String langCode = (String) obj.get("LOCAL_CODE");
            if(ccyCode.equals(key) && langCode.equals(lang)){
                return ccyCode+" - "+ccyName;
            }
        }
        return null;
	}
	
	public void populate() {
		ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");

        //Jet modified 2007-12-4
//        String sql = " SELECT HS_CURRENCY.CCY_CODE, HS_CURRENCY_NAME.CCY_LONG_NAME FROM HS_CURRENCY,HS_CURRENCY_NAME WHERE HS_CURRENCY.CCY_CODE=HS_CURRENCY_NAME.CCY_CODE AND HS_CURRENCY.MIN_AMT_OTHER_BANKS <> 0 ORDER BY SORTING_SEQ_OVERSEA";
        //String sql = " SELECT c.CCY_CODE, cn.CCY_LONG_NAME FROM HS_CURRENCY c,HS_CURRENCY_NAME cn,HS_EXCHANGE_RATE er WHERE c.CCY_CODE=cn.CCY_CODE AND c.CCY_CODE=er.CCY_CODE AND er.TT_BUY_RATE_MOP > 0  AND qexr_exr_typ = 'BOK' ORDER BY SORTING_SEQ_OVERSEA";//mod by linrui add AND qexr_exr_typ = 'AMC' 
        //String sql = " SELECT c.CCY_CODE, cn.CCY_LONG_NAME FROM HS_CURRENCY c,HS_CURRENCY_NAME cn,HS_EXCHANGE_RATE er WHERE c.CCY_CODE=cn.CCY_CODE AND c.CCY_CODE=er.CCY_CODE AND er.TT_BUY_RATE_MOP > 0  ORDER BY SORTING_SEQ_OVERSEA";//mod by linrui add AND qexr_exr_typ = 'AMC'
          //modified by lzg 20190521
          //String sql = " SELECT c.CCY_CODE, cn.CCY_LONG_NAME FROM HS_CURRENCY c,HS_CURRENCY_NAME cn,HS_EXCHANGE_RATE er WHERE c.CCY_CODE=cn.CCY_CODE AND c.CCY_CODE=er.CCY_CODE AND er.TT_BUY_RATE_MOP > 0 and c.CCY_CODE in ('HKD','USD','EUR')  ORDER BY SORTING_SEQ_OVERSEA";
          String sql = " SELECT c.CCY_CODE, cn.CCY_LONG_NAME ,cn.LOCAL_CODE FROM HS_CURRENCY c,HS_CURRENCY_NAME cn,HS_EXCHANGE_RATE er WHERE c.CCY_CODE=cn.CCY_CODE AND c.CCY_CODE=er.CCY_CODE AND er.TT_BUY_RATE_MOP > 0 and c.CCY_CODE in ('HKD','EUR','USD')  ORDER BY SORTING_SEQ_OVERSEA";
          //modified by lzg end

        try {
            objList = dao.query(sql, new Object[] {});
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