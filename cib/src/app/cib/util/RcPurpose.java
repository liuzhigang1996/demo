package app.cib.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.utils.NTBResources;

/**
 * 
 * @author heyongjiang
 * @since 20100804
 */
public class RcPurpose implements NTBResources{
	 private static List objList = null;
	 private static boolean refreshed = false;
	    public RcPurpose() {
	        if(!refreshed){
	            populate();
	        }
	    }

	public List getKeys() {
		List keys = new ArrayList();
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String code = (String) obj.get("CODE");
            keys.add(code);
        }
        return keys;
	}

	public String getProperty(String key) {
		for(int i=0; i < objList.size(); i++){
            Map obj = (Map)objList.get(i);
            String code = (String) obj.get("CODE");
            String description = (String) obj.get("DESCRIPTION");
            if(code.equals(key)){
                return description;
            }
        }
        return null;
	}

	public void populate() {
		ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
        String sql = "SELECT p.CODE, P.DESCRIPTION FROM HS_PURPOSE_DESCRIPTION p WHERE LANGUAGE_CODE = 'E' ORDER BY CODE";
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
