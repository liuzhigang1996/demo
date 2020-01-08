package app.cib.util;

import java.util.Map;
import java.util.ArrayList;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.core.Log;
import org.springframework.context.ApplicationContext;
import java.util.List;
import com.neturbo.set.core.Config;
import com.neturbo.set.utils.*;

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
public class RcAccessIp  implements NTBResources {
    private static List objList = null;
    private static boolean refreshed = false;

    public RcAccessIp() {
        if(!refreshed){
            populate();
        }
    }

    public List getKeys() {
        List keys = new ArrayList();
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String checkIpAccess = (String) obj.get("IP_ADDRESS");
            keys.add(checkIpAccess);
        }
        return keys;
    }


    public String getProperty(String ipAccess) {
        for(int i=0; i < objList.size(); i++){
            Map obj = (Map)objList.get(i);
            String checkCorpId = (String) obj.get("CORP_ID");
            String checkIpAccess = (String) obj.get("IP_ADDRESS");
            if(checkIpAccess.equals(ipAccess)){
                return checkCorpId;
            }
        }
        return null;
    }

    public boolean checkAccess(String corpId, String ipAccess) {
        boolean accessable = true;
        Log.debug("*** request ip=" + ipAccess);
        for(int i=0; i < objList.size(); i++){
            Map obj = (Map)objList.get(i);
            String checkCorpId = Utils.null2EmptyWithTrim(obj.get("CORP_ID"));
            String checkIpAccess = Utils.null2EmptyWithTrim(obj.get("IP_ADDRESS"));
            Log.debug("*** check ip=" + checkIpAccess);
            if(checkCorpId.equals(corpId)){
                accessable = false;
                if(checkIpAccess.equals(ipAccess)){
                    accessable = true;;
                    break;
                }
            }
        }
        return accessable;
    }

    public void populate() {
        ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean(
                "genericJdbcDao");
        String sql = " SELECT CORP_ID, IP_ADDRESS FROM HS_CORP_ACCESS_CONTROL";
        try {
            List tmpObjList = dao.query(sql, new Object[] {});
            objList = new ArrayList();
            for(int i=0; i < tmpObjList.size(); i++){
                Map obj = (Map) tmpObjList.get(i);
                String ipAccess = Utils.null2EmptyWithTrim(obj.get(
                        "IP_ADDRESS"));
                String[] numArray = Utils.splitStr(ipAccess,".");
                String newIp = Utils.removePrefixZero(numArray[0]);
                for(int j=1; j< numArray.length; j++){
                    newIp += "." + Utils.removePrefixZero(numArray[j]);
                }
                obj.put("IP_ADDRESS", newIp);
                objList.add(obj);
            }
            refreshed = true;
        } catch (Exception e) {
            Log.error("Error loading group resource", e);
            refreshed = false;
        }
    }

    public void setArgs(Object obj) {
    }

    public static void main(String[] args){
        RcAccessIp obj = new RcAccessIp();
        obj.populate();
    }
}
