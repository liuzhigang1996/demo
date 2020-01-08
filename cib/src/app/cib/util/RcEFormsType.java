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
public class RcEFormsType implements NTBResources {
    private static List objList = null;
    private static boolean refreshed = false;

    public RcEFormsType() {
        if(!refreshed){
            populate();
        }
    }
    
	public List getKeys() {
        List keys = new ArrayList();
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String typeKey = (String) obj.get("TYPE_KEY");
            keys.add(typeKey);
        }
        return keys;
	}

	public String getProperty(String key) {
        for(int i=0; i < objList.size(); i++){
            Map obj = (Map)objList.get(i);
            String typeKey = (String) obj.get("TYPE_KEY");
            String typeValue = (String) obj.get("TYPE_OF_FORM");
            if(typeKey.equals(key)){
                return typeValue;
            }
        }
        return null;
	}

	public void populate() {
        ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean(
                "genericJdbcDao");
        String sql = " SELECT distinct TYPE_OF_FORM as TYPE_KEY, TYPE_OF_FORM FROM HS_E_FORMS";
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
