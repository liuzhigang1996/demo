package app.cib.util;

import java.util.*;

import org.springframework.context.ApplicationContext;
import com.neturbo.set.core.*;
import com.neturbo.set.database.*;
import com.neturbo.set.utils.*;

public class RcLocalBank implements NTBResources {
    private static List objList = null;
    private static boolean refreshed = false;
    private NTBUser user;
    public RcLocalBank() {
        if(!refreshed){
            populate();
        }
    }
	public List getKeys() {
		List keys = new ArrayList();
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String bankCode = (String) obj.get("BANK_CODE");
            if (!keys.contains(bankCode)) {
            	keys.add(bankCode);
            }
        }
        return keys;
	}
	public String getProperty(String key) {
		String lang = user.getLangCode();
		for(int i=0; i < objList.size(); i++){
            Map obj = (Map)objList.get(i);
            String bankCode = (String) obj.get("BANK_CODE");
            String bankName = (String) obj.get("BANK_NAME");
            String langCode = (String) obj.get("LOCAL_CODE");
            if(bankCode.equals(key)&& langCode.equals(lang)){
                return bankName;
            }
        }
        return null;
	}
	public void populate() {
		ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
          String sql = "select * from ( select c.LOCAL_CODE, c.CITY_CODE, c.BANK_CODE, c.BANK_NAME, " +
          "row_number() over(partition by c.city_code,c.bank_code,c.local_code order by c.swift_code) as seq " +
          " from hs_local_bank_code c )t where t.seq = 1 ";
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