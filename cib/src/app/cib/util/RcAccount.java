package app.cib.util;

import java.util.*;
import org.springframework.context.ApplicationContext;
import com.neturbo.set.core.*;
import com.neturbo.set.database.*;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.*;

import app.cib.bo.sys.*;

/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 *
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class RcAccount implements NTBResources {
    private static List objList = null;
    private String corpId = null;
    private String accountType = null;
    private Map permMap = null;
    private NTBUser user;

    private static boolean refreshed = false;
   /* private static RBFactory rbAccType = RBFactory.getInstance(
            "app.cib.resource.common.account_type_desc");*/
    //add and modify by linrui 20180716
    private String language = null;

    public RcAccount(String accountType) {
        this.accountType = accountType;
        if (!refreshed) {
            populate();
        }
    }

    public List getKeys() {
        List keys = new ArrayList();
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String corpId1 = (String) obj.get("CORP_ID");
            String accNo = (String) obj.get("ACCOUNT_NO");
            String accType = (String) obj.get("ACCOUNT_TYPE");
            String Status = (String) obj.get("STATUS");
            if (!Status.equals(Constants.STATUS_NORMAL)) {
                continue;
            }

            if (accountType.equals(CorpAccount.ACCOUNT_TYPE_ALL)) {

            } else if (accountType.equals(CorpAccount.ACCOUNT_TYPE_CAOASA)|| accountType.equals(CorpAccount.ACCOUNT_TYPE_SUB_CORP)) {
                if (!accType.equals(CorpAccount.ACCOUNT_TYPE_CURRENT)
                    && !accType.equals(CorpAccount.ACCOUNT_TYPE_OVER_DRAFT)
                    && !accType.equals(CorpAccount.ACCOUNT_TYPE_SAVING)) {
                    continue;
                }
            }  else if (accountType.equals(CorpAccount.ACCOUNT_TYPE_CAOA)){
            	if (!accType.equals(CorpAccount.ACCOUNT_TYPE_CURRENT)){//mod by linrui 20180312
//              if (!accType.equals(CorpAccount.ACCOUNT_TYPE_CURRENT)
//                	&& !accType.equals(CorpAccount.ACCOUNT_TYPE_OVER_DRAFT)) {
                    continue;
                }
            }else {
                if (!accType.equals(accountType)) {
                    continue;
                }
            }
            if (corpId1.equals(corpId)
                &&
                (accountType.equals(CorpAccount.ACCOUNT_TYPE_SUB_CORP) ||
                 permMap
                 .containsKey(accNo))) {
                keys.add(accNo);
            }
        }
        return keys;
    }

    public String getProperty(String key) {
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String corpId1 = (String) obj.get("CORP_ID");
            String accNo = (String) obj.get("ACCOUNT_NO");
            String ccy = (String) obj.get("CURRENCY");
            String accName = Utils.null2EmptyWithTrim(obj.get("ACCOUNT_NAME")); //modify by hjs 2006-10-03
            String accType = (String) obj.get("ACCOUNT_TYPE");
            String accDesc = Utils.null2EmptyWithTrim(obj.get("ACCOUNT_DESC")); //add by hjs 2006-10-03
            if (accNo.equals(key)) {
                if (accountType.equals(CorpAccount.ACCOUNT_TYPE)) {
                    return accType;
                } else if (accountType.equals(CorpAccount.ACCOUNT_NAME)) {
                    return accName;
                } else if (accountType.equals(CorpAccount.ACCOUNT_DESCRIPTION)) {
                	
                	//add by linrui 20190729
                	RBFactory rbAccType = RBFactory.getInstance(
                    "app.cib.resource.common.account_type_desc" , user.getLanguage().toString());
                	accName = getaccName(accNo,ccy);
                    return /*ccy + " " +*/ rbAccType.getString(accType) + "<br>" +
                            accName;
                }else if (accountType.equals(CorpAccount.ACCOUNT_LIST_DESCRIPTION)) {
                	//add by linrui 20180716
                	RBFactory rbAccType = RBFactory.getInstance(
                    "app.cib.resource.common.account_type_desc" , user.getLanguage().toString());
                	DBRCFactory ccyDescInstance = DBRCFactory.getInstance("rcCurrencyCBS");
                	ccy = ccyDescInstance.getString(ccy);
                	//end
                    return ccy + " " + rbAccType.getString(accType);
                }else if (accountType.equals(CorpAccount.ACCOUNT_DESCRIPTION_DOWNLOAD)) {
                	RBFactory rbAccType = RBFactory.getInstance("app.cib.resource.common.account_type_desc" , 
                			user.getLanguage().toString());
                	DBRCFactory ccyDescInstance = DBRCFactory.getInstance("rcCurrencyCBS");
                	ccy = ccyDescInstance.getString(ccy);
                    return ccy + " " + rbAccType.getString(accType) + " " + accName;
                }else if (accountType.equals(CorpAccount.APPLICATION_CODE_CURRENT)||accountType.equals(CorpAccount.APPLICATION_CODE_SAVING)) {
                	
                }else {
                    //return ccy + " - " + accNo + " - " + accName;
                	// add by hjs 2006-10-03
                	RBFactory rbAccType = RBFactory.getInstance(
                            "app.cib.resource.common.account_type_desc" , user.getLanguage().toString());
                        	DBRCFactory ccyDescInstance = DBRCFactory.getInstance("rcCurrencyCBS");
                        	ccy = ccyDescInstance.getString(ccy);
                	return accNo + " " + ccy ;//mod by lnirui for mul-ccy
                			/*+ (accDesc.equals("") ? "" : " - " + accDesc)
                			+ (accName.equals("") ? "" : " - " + accName);*/
                }
            }
        }
        return key;
    }


    private String getaccName(String accNo,String ccy) {
    	ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext
                             .getBean("genericJdbcDao");
		String sql = "SELECT ACCOUNT_NAME FROM CORP_ACCOUNT where ACCOUNT_NO = ? and CURRENCY = ?";
		try {
			List retList = dao.query(sql, new Object[]{accNo,ccy});
			Map retMap = (HashMap)retList.get(0);
			String accName = (String)retMap.get("ACCOUNT_NAME");
			if(accName == null){
				return "";
			}
			return accName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public Map getObject(String key) {
        for (int i = 0; i < objList.size(); i++) {
            Map obj = (Map) objList.get(i);
            String corpId1 = (String) obj.get("CORP_ID");
            String accNo = (String) obj.get("ACCOUNT_NO");
            if (corpId1.equals(corpId) && accNo.equals(key)) {
                return obj;
            }
        }
        return null;
    }

    public void populate() {
        ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext
                             .getBean("genericJdbcDao");
        String sql = " SELECT ACCOUNT_NO, CURRENCY, ACCOUNT_NAME, ACCOUNT_DESC, ACCOUNT_TYPE, CORP_ID, STATUS FROM CORP_ACCOUNT";
        Object[] objs = new Object[] {};
        try {
            objList = dao.query(sql, objs);
            refreshed = true;
        } catch (Exception e) {
            Log.error("Error loading account resource", e);
            refreshed = false;
        }
    }

    public void setArgs(Object obj) {
        permMap = new HashMap();
        corpId = "";
        if (obj instanceof CorpUser) {
            CorpUser user = (CorpUser) obj;
            corpId = user.getCorpId();
            language = user.getLanguage().toString();//add by linrui 20180716
            List permList = user.getAccountList();
            for (int i = 0; i < permList.size(); i++) {
                NTBPermission perm = (NTBPermission) permList.get(i);
                permMap.put(perm.getPermissionResource(), perm);
            }
        }
        if (obj instanceof NTBUser) {
			user = (NTBUser) obj;
		}

    }

}
