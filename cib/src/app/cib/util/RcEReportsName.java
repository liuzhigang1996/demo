/**
 * @author hjs
 * 2007-5-9
 */
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
 * @author hjs
 * 2007-5-9
 */
public class RcEReportsName implements NTBResources {
    private static List objList = null;
    private static boolean refreshed = false;

    public RcEReportsName() {
        if(!refreshed){
            populate();
        }
    }
    
	public List getKeys() {
        List keys = new ArrayList();
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String reportKey = (String) obj.get("REPORT_KEY");
            keys.add(reportKey);
        }
        return keys;
	}

	public String getProperty(String key) {
        for(int i=0; i < objList.size(); i++){
            Map obj = (Map)objList.get(i);
            String reportKey = (String) obj.get("REPORT_KEY");
            String reportValue = (String) obj.get("REPORT_NAME");
            if(reportKey.equals(key)){
                return reportValue;
            }
        }
        return null;
	}

	public void populate() {
        ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean(
                "genericJdbcDao");
        String sql = " SELECT distinct REPORT_NAME as REPORT_KEY, REPORT_NAME FROM HS_E_REPORTS";
        try {
            objList = dao.query(sql, new Object[] {});
            refreshed = true;
        } catch (Exception e) {
            Log.error("Error loading group resource", e);
            refreshed = false;
        }
	}

	public void setArgs(Object argObj) {
	}

}
