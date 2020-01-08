package app.cib.util;

import java.util.*;

import org.springframework.context.ApplicationContext;
import com.neturbo.set.core.*;
import com.neturbo.set.database.*;
import com.neturbo.set.utils.*;

public class RcAutopay implements NTBResources {
    private static List objList = null;
    private static boolean refreshed = false;
    public RcAutopay() {
        if(!refreshed){
            populate();
        }
    }
	public List getKeys() {
		List keys = new ArrayList();
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String apsCode = (String) obj.get("APS_CODE");
           
            keys.add(apsCode);
        }
        return keys;
	}
	public String getProperty(String key) {
		for(int i=0; i < objList.size(); i++){
            Map obj = (Map)objList.get(i);
            String apsCode = (String) obj.get("APS_CODE");
            String merchantName = (String) obj.get("MERCHANT_NAME");
            if(apsCode.equals(key)){
                return merchantName;
            }
        }
        return null;
	}
	public void populate() {
		ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
        //Jet modified 2007-12-4
        String sql = "SELECT M.APS_CODE, M.PAY_BY_ACCT,M.PAY_BY_CREDIT_CARD, N.MERCHANT_NAME " +
        		"FROM AUTOPAY_MERCHANT_INFO M,AUTOPAY_MERCHANT_NAME N WHERE M.APS_CODE = N.APS_CODE and N.LANG='E' order by N.MERCHANT_NAME";
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