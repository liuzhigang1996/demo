package app.cib.util;

import java.util.List;
import org.springframework.context.ApplicationContext;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.Config;
import com.neturbo.set.database.GenericJdbcDao;
import java.util.Map;
import java.util.ArrayList;
import com.neturbo.set.utils.NTBResources;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class RcCurrency implements NTBResources {
    private static List objList = null;
    private static boolean refreshed = false;
    public RcCurrency() {
        if(!refreshed){
            populate();
        }
    }

    public List getKeys() {
        List keys = new ArrayList();
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String ccyCodeCBS = (String) obj.get("CCY_CODE_CBS");
            //String ccyCode = (String) obj.get("CCY_CODE");
            keys.add(ccyCodeCBS);
        }
        return keys;
    }


    public String getProperty(String key) {
        for(int i=0; i < objList.size(); i++){
            Map obj = (Map)objList.get(i);
            String ccyCodeCBS = (String) obj.get("CCY_CODE_CBS");
            String ccyCode = (String) obj.get("CCY_CODE");
            if(ccyCodeCBS.equals(key)){
                return ccyCode;
            }
        }
        return null;
    }

    public String getCcyByCbsNumber(String key) {
        for(int i=0; i < objList.size(); i++){
            Map obj = (Map)objList.get(i);
            String ccyCodeCBS = (String) obj.get("CCY_CODE_CBS");
            String ccyCode = (String) obj.get("CCY_CODE");
            if(ccyCodeCBS.equals(key)){
                return ccyCode;
            }
        }
        return null;
    }

    public String getCbsNumberByCcy(String key) {
        for(int i=0; i < objList.size(); i++){
            Map obj = (Map)objList.get(i);
            String ccyCodeCBS = (String) obj.get("CCY_CODE_CBS");
            String ccyCode = (String) obj.get("CCY_CODE");
            if(ccyCode.equals(key)){
                return ccyCodeCBS;
            }
        }
        return null;
    }

    public void populate() {
        ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean(
                "genericJdbcDao");
        String sql = " SELECT CCY_CODE, CCY_CODE_CBS FROM HS_CURRENCY";
        try {
            objList = dao.query(sql, new Object[] {});
            refreshed = true;
        } catch (Exception e) {
            Log.error("Error loading group resource", e);
            refreshed = false;
        }
    }

    public void setArgs(Object obj) {
    }
}
