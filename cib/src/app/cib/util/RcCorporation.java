package app.cib.util;

import java.util.*;
import org.springframework.context.ApplicationContext;
import com.neturbo.set.core.*;
import com.neturbo.set.database.*;
import com.neturbo.set.utils.*;

import app.cib.bo.sys.CorpUser;

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
public class RcCorporation implements NTBResources {
    private static List objList = null;
    private static Map objMap = null;
    private int showType = SHOW_CORP_WITHOUT_ROOT;
    private String corpId = null;

    public static int SHOW_CORP_WITH_ROOT = 0;
    public static int SHOW_CORP_WITHOUT_ROOT = 1;
    public static int SHOW_CORP_NAME = 5;


    private static boolean refreshed = false;
    public RcCorporation(int showType) {
        this.showType = showType;
        /*if(!refreshed){
            populate();
        }*/
        //mod by linrui 20190708
        populate();
    }

    public List getKeys() {
        List keys = new ArrayList();
        if(showType == SHOW_CORP_WITH_ROOT){
            Map corpOjb = (Map) objMap.get(corpId);
            corpOjb.put("LEVEL", new Integer(0));
            keys.add(corpId);
        }
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String corpId1 = (String) obj.get("CORP_ID");
            String parentId = (String) obj.get("PARENT_CORP");
            //String corpName = (String)obj.get("CORP_NAME");
            if (corpId.equals(parentId)) {
                keys.add(corpId1);
                obj.put("LEVEL", new Integer(1));
                getChildren(keys, corpId1, 2);
            }
        }
        return keys;
    }

    private List getChildren(List keys, String parentId, int level) {
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String corpId1 = (String) obj.get("CORP_ID");
            String parentId1 = (String) obj.get("PARENT_CORP");
            //String corpName = (String)obj.get("CORP_NAME");
            if (parentId.equals(parentId1)) {
                keys.add(corpId1);
                obj.put("LEVEL", new Integer(level));
                getChildren(keys, corpId1, level + 1);
            }
        }
        return keys;
    }

    public String getProperty(String key) {
        Map obj = (Map) objMap.get(key);
        if (obj != null) {
            String corpId1 = (String) obj.get("CORP_ID");
            //String parentId1 = (String)obj.get("CORP_PARENT");
            String corpName1 = (String) obj.get("CORP_NAME");

            String showName = corpName1 + "(" + corpId1 + ")";
            if(showType != SHOW_CORP_NAME){
                for (int j = 0; j < ((Integer) obj.get("LEVEL")).intValue(); j++) {
                    showName = "--" + showName;
                }
            }
            return showName;
        }
        return null;
    }

    public boolean isParentCorp(String key){
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            //String corpId1 = (String) obj.get("CORP_ID");
            String parentId = (String) obj.get("PARENT_CORP");
            if (key.equals(parentId)) {
                return true;
            }
        }
        return false;
    }

    public Map getObject(String key) {
        Map obj = (Map) objMap.get(key);
        return obj;
    }

    public void populate() {
        ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean(
                "genericJdbcDao");
        String sql = " SELECT CORP_ID, PARENT_CORP, CORP_NAME FROM CORPORATION WHERE STATUS='0'";
        try {
            objList = dao.query(sql, new Object[] {});
            objMap = new HashMap();
            for (int i = 0; i < objList.size(); i++) {
                Map obj = (Map) objList.get(i);
                String corpId1 = (String) obj.get("CORP_ID");
                objMap.put(corpId1, obj);
            }
            refreshed = true;
        } catch (Exception e) {
            Log.error("Error loading group resource", e);
            refreshed = false;
        }
    }

    public void setArgs(Object obj) {
        if (obj instanceof CorpUser) {
            CorpUser user = (CorpUser) obj;
            corpId = user.getCorpId();
        }
    }

}
