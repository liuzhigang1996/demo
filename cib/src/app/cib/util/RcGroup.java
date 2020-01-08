package app.cib.util;

import java.util.*;
import org.springframework.context.ApplicationContext;
import com.neturbo.set.core.*;
import com.neturbo.set.database.*;
import com.neturbo.set.utils.*;

import app.cib.bo.bnk.BankUser;
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
public class RcGroup implements NTBResources {
    private static List objList = null;
    private String corpId = null;
    private String roleId = null;
    private String lang = null;//mod by linrui 20180519
    private static boolean refreshed = false;
    private int selectType;

    public static final int SELECT_GROUP_BY_CORP = 1;
    public static final int SELECT_GROUP_BY_ROLE = 2;

    public RcGroup(int type) {
        if (!refreshed) {
            populate();
        }
        selectType = type;

    }

    public List getKeys() {
        List keys = new ArrayList();
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String corpId1 = (String) obj.get("CORP_ID");
            String groupId1 = (String) obj.get("GROUP_ID");
            String roleId1 = (String) obj.get("ROLE_ID");
            //String groupName1 = (String) obj.get("GROUP_NAME");
            if (selectType == SELECT_GROUP_BY_CORP){
                if(corpId1.equals("0") || corpId1.equals(corpId)) {
                    keys.add(groupId1);
                }
            }
            if (selectType == SELECT_GROUP_BY_ROLE) {
                if(corpId1.equals("0") || corpId1.equals(corpId)) {
                    if (roleId1.equals("0") || roleId1.equals(roleId)) {
                        keys.add(groupId1);
                    }
                }
            }
        }

        return keys;
    }


    public String getProperty(String key) {
    	RBFactory rb = RBFactory.getInstance("app.cib.resource.common.default_group",lang);//add by linrui 20180519
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            //String corpId1 = (String) obj.get("CORP_ID");
            String groupId1 = (String) obj.get("GROUP_ID");
            String groupName1 = (String) obj.get("GROUP_NAME");
            //add by linrui 20180519
            if ("0".equals(key)) {           	
            	return rb.getString("default");
            }
            //end
            if (groupId1.equals(key)) {
                return groupName1;
            }
        }
        return null;
    }

    public void populate() {
        ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean(
                "genericJdbcDao");
        String sql = " SELECT GROUP_ID, CORP_ID, ROLE_ID, GROUP_NAME FROM USER_GROUP WHERE STATUS = '0'";
        try {
            objList = dao.query(sql, new Object[] {});
            refreshed = true;
        } catch (Exception e) {
            Log.error("Error loading group resource", e);
            refreshed = false;
        }
    }

    public void setArgs(Object obj) {
    	// Jet modified to handle null obj
    	if(obj == null){
            corpId = "";
            lang = "en_US";//add by linrui 20180529
    	} else if (obj instanceof CorpUser) {
            CorpUser user = (CorpUser) obj;
            corpId = user.getCorpId();
            lang = user.getLanguage().toString();//add by linrui 20180519
        } else if (obj.getClass().isArray()) {
            Object[] argArray = (Object[]) obj;
            CorpUser user = (CorpUser) argArray[0];
            corpId = user.getCorpId();
            roleId = (String) argArray[1];
            lang = user.getLanguage().toString();//add by linrui 20180519
        } else if(obj instanceof BankUser){//add by linrui 20180529
        	BankUser user = (BankUser) obj;
        	corpId = "";
            lang = user.getLanguage().toString();
            //end
        }else {
            corpId = "";
            lang = "en_US";//add by linrui 20180529
        }
    }

}
