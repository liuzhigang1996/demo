package app.cib.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sql.TIMESTAMP;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.utils.DBRCEventHandler;
import com.neturbo.set.utils.DBRCFactory;
import com.neturbo.set.utils.DateTime;

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
public class CacheController implements DBRCEventHandler {
    private static Date myUpdateTime = new Date();
    private static Map cacheMap = new HashMap();
    private static GenericJdbcDao genericJdbcDao = null;
    static {
        ApplicationContext appContext = Config.getAppContext();
        genericJdbcDao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
    }

    private static String sql_LastUpdateTime =
            //"SELECT LAST_UPDATE_TIME FROM CACHE_CONTROL WHERE CONTROL_TYPE=?";
    	    //mod by linrui 20190611
              "SELECT to_char(LAST_UPDATE_TIME,'yyyy-mm-dd hh24:mi:ss') as LAST_UPDATE_TIME FROM CACHE_CONTROL WHERE CONTROL_TYPE=?";
    private static String sql_setLastUpdateTime =
            "UPDATE CACHE_CONTROL SET LAST_UPDATE_TIME=? WHERE CONTROL_TYPE=?";
    private static String sql_newLastUpdateTime =
            "INSERT INTO CACHE_CONTROL(LAST_UPDATE_TIME,CONTROL_TYPE) VALUES(?,?)";

    private static String sql_ALLUpdateItem =
            //"SELECT CONTROL_TYPE,CACHE_NAME,LAST_UPDATE_TIME FROM CACHE_CONTROL";
    	    //mod by linrui 20190611
    	    "SELECT CONTROL_TYPE,CACHE_NAME,to_char(LAST_UPDATE_TIME,'yyyy-mm-dd hh24:mi:ss') as LAST_UPDATE_TIME FROM CACHE_CONTROL";
    private static String sql_CheckItemExist =
            "SELECT CACHE_NAME FROM CACHE_CONTROL WHERE CONTROL_TYPE=? AND CACHE_NAME=?";
    private static String sql_AddItem = "INSERT INTO CACHE_CONTROL(LAST_UPDATE_TIME,CONTROL_TYPE,CACHE_NAME) VALUES(?,?,?)";
    private static String sql_UpdateItem = "UPDATE CACHE_CONTROL SET LAST_UPDATE_TIME=? WHERE CONTROL_TYPE=? AND CACHE_NAME=?";
    private static String sql_CheckItemExistNull =//add by linrui 20190531
    	"SELECT CACHE_NAME FROM CACHE_CONTROL WHERE CONTROL_TYPE=? AND CACHE_NAME is null";
    public static final String CACHE_TYPE_LAST_UPDATE_TIME = "0";
    public static final String CACHE_TYPE_ALL = "1";
    public static final String CACHE_TYPE_TABLE = "2";
    public static final String CACHE_TYPE_DBRC = "3";

    public CacheController() {
    }

    //閿熸枻鎷峰附閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓閾伴潻鎷烽敓鏂ゆ嫹鏃堕敓鏂ゆ嫹
    private synchronized static Date getLastUpdateTime() {
        try {
            Object retVal = genericJdbcDao.querySingleValue(sql_LastUpdateTime,
                    new Object[] {CACHE_TYPE_LAST_UPDATE_TIME});           
 //           return (TIMESTAMP) retVal;
            //mod by linrui
            return getDateBySqlTimestamp(retVal);            
        } catch (Exception e) {
            Log.error("Error getting last update time for cache", e);
        }
        return null;
    }
    //add by linrui for only 
    private synchronized static Object getLastUpdateTimeForNewLastUp() {
    	try {
    		Object retVal = genericJdbcDao.querySingleValue(sql_LastUpdateTime,
    				new Object[] {CACHE_TYPE_LAST_UPDATE_TIME});           
    		//           return (TIMESTAMP) retVal;
    		//mod by linrui
//    		return getDateBySqlTimestamp(retVal);            
    		return retVal;            
    	} catch (Exception e) {
    		Log.error("Error getting last update time for cache", e);
    	}
    	return null;
    }

    //閿熸枻鎷烽敓鏂ゆ嫹纰岄敓锟絧ending update item
    public synchronized static void addNewUpdate(String controlType, String cacheName) {
        try {
/*            Object retVal = genericJdbcDao.querySingleValue(sql_CheckItemExist,
                    new Object[] {controlType, cacheName});*/
        	//add by linrui 20190520
        	Object retVal = null;
        	if("".equals(cacheName.trim())){
        		retVal = genericJdbcDao.querySingleValue(sql_CheckItemExistNull, new Object[] {controlType});
        	}else{
        	//end
                retVal = genericJdbcDao.querySingleValue(sql_CheckItemExist, new Object[] {controlType, cacheName});
        	}
            if (retVal != null) {
                genericJdbcDao.update(sql_UpdateItem, new Object[] {new Date(),
                                      controlType, cacheName});
            } else {
                genericJdbcDao.update(sql_AddItem, new Object[] {new Date(),
                                      controlType, cacheName});
            }
            newLastUpdateTime();
        } catch (Exception e) {
            Log.error("Error adding new update cache :" + cacheName, e);
        }
    }

    //閿熸枻鎷烽敓鐭唻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹璧傞敓鏂ゆ嫹閿熺粸鎲嬫嫹閿燂拷
    private synchronized static void newLastUpdateTime() {
        try {
            Object retVal = getLastUpdateTimeForNewLastUp();
            if (retVal != null) {
                genericJdbcDao.update(sql_setLastUpdateTime, new Object[] {new Date(), CACHE_TYPE_LAST_UPDATE_TIME});
            } else {
                genericJdbcDao.update(sql_newLastUpdateTime, new Object[] {new Date(), CACHE_TYPE_LAST_UPDATE_TIME});
            }
        } catch (Exception e) {
            Log.error("Error adding new last update time :", e);
        }
    }

    //閿熸枻鎷烽敓锟絚ontrolType 閿熸枻鎷�cacheName 閿熸枻鎷烽敓鏂ゆ嫹鏌愰敓鏂ゆ嫹 pending cache item
    private synchronized static void doNewUpdate(String controlType, String cacheName) {
        if (CACHE_TYPE_DBRC.equals(controlType)) {
            populateCache(cacheName);
        }
        //閿熸枻鎷烽敓鏂ゆ嫹鍏ㄩ敓鏂ゆ嫹
        if (CACHE_TYPE_ALL.equals(controlType)) {
            populateAllCache();
        }
        if (CACHE_TYPE_TABLE.equals(controlType)) {
            populateAllCache();
        }
    }

    //閿熸枻鎷烽敓閾扮鎷烽敓鏂ゆ嫹 pending cache item
    private static synchronized void populateCache(String rcName) {
        Date updateTime = new Date();
        DBRCFactory.populateInstance(rcName);
        //閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷锋簮閿熸枻鎷烽敓鏂ゆ嫹閿熼摪闈╂嫹閿熸枻鎷锋椂閿熸枻鎷峰啓閿熸枻鎷穋acheMap
        String key = CACHE_TYPE_DBRC + "&" + rcName;
        cacheMap.put(key, updateTime);
        Log.info("--- populate cache for DB resource [" + rcName +
                 "] successfully");
    }

    //閿熸枻鎷烽敓鏂ゆ嫹鍏ㄩ敓鏂ゆ嫹 pending cache item
    private static synchronized void populateAllCache() {
        String[] instanceNames = CachedDBRCFactory.getInstanceNames();
        for (int i = 0; i < instanceNames.length; i++) {
            DBRCFactory.populateInstance(instanceNames[i]);
        }
        Log.info("--- populate cache for all DB resource successfully");
    }

    //閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷疯祩閿熸枻鎷烽敓锟�
    public static synchronized void checkNewUpdate() {
    	Log.info("---- checkNewUpdate ------- ");
        Date totalLastUpdateTime = getLastUpdateTime();
        Log.info("---- checkNewUpdate getLastUpdateTime status =0 , and date is : " + totalLastUpdateTime);
        if (totalLastUpdateTime != null &&
            myUpdateTime.before(totalLastUpdateTime)) {
            try {
                List updateList = genericJdbcDao.query(sql_ALLUpdateItem,
                        new Object[] {});
                for (int i = 0; i < updateList.size(); i++) {
                    Map updateItem = (Map) updateList.get(i);
                    String controlType = (String) updateItem.get("CONTROL_TYPE");
                    if(!CacheController.CACHE_TYPE_LAST_UPDATE_TIME.equals(controlType)){
                        String cacheName = (String) updateItem.get("CACHE_NAME");
                        //mod by linrui for oracle
//                        Date lastUpdateTime = (Date) updateItem.get(
//                                "LAST_UPDATE_TIME");
                        Date lastUpdateTime = getDateBySqlTimestamp(updateItem.get(
                              "LAST_UPDATE_TIME"));
                        //end
                        String key = controlType + "&" + cacheName;
                        Date myItemUpdateTime = (Date) cacheMap.get(key);
                        if (myItemUpdateTime == null ||
                            myItemUpdateTime.before(lastUpdateTime)) {
                            doNewUpdate(controlType, cacheName);
                        }
                    }
                }
                myUpdateTime = totalLastUpdateTime;
            } catch (Exception e) {
                Log.error("Error checking last update for cache", e);
            }
        }
    }

    //瀹為敓鏂ゆ嫹DBRC閿熼摪纭锋嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓渚ョ櫢鎷烽敓鏂ゆ嫹
    //閿熸枻鎷�getInstance閿熸枻鎷锋椂閿熸枻鎷烽敓鏂ゆ嫹閿熻鍑ゆ嫹閿熷彨闈╂嫹閿熸枻鎷�
    public void onGetInstance(String key) {
        checkNewUpdate();
    }
    //oracle.util.timestamp to java.util.date
    /*public static Date getDateBySqlTimestamp(Object obj) {  //Object obj, String formatStr
        try {  
        	Log.info("--- getDateBySqlTimestamp-----------obj: " + obj);
            TIMESTAMP t = (TIMESTAMP)obj;  
            Log.info("--- getDateBySqlTimestamp----------TIMESTAMP " + obj);
//            if (formatStr == null || formatStr.equals("")) {  
//                formatStr = "yyyy-MM-dd hh:mm:ss";  
//            }  
            Timestamp tt;  
            tt = t.timestampValue();
            Log.info("--- getDateBySqlTimestamp------timestampValue " + obj);
            Date date = new Date(tt.getTime());  
            //SimpleDateFormat sd = new SimpleDateFormat(formatStr);  
            //return sd.format(date); 
            Log.info("--- getDateBySqlTimestamp--return date:" + date);
            return date;              
        } catch (Exception e) { 
        	Log.info("--- getDateBySqlTimestamp--Exception------" + e.getMessage());
        	Log.info("--- getDateBySqlTimestamp--return null--------" + obj);
        	return null;   
        }  
    }  */
    //add by linrui for Timestamp can't not change to Timestamp
    public static Date getDateBySqlTimestamp(Object obj) {  //Object obj, String formatStr
    	try {  
    		String dateStr = obj.toString();
    		Log.info("--- getDateBySqlTimestamp-----------Str: " + dateStr);
    		String formatStr = "yyyy-MM-dd hh:mm:ss";
    		Log.info("--- getDateBySqlTimestamp----------formatStr " + formatStr);
    		return DateTime.strToDate(dateStr, formatStr); 
    	} catch (Exception e) { 
    		Log.info("--- getDateBySqlTimestamp--Exception------" + e.getMessage());
    		Log.info("--- getDateBySqlTimestamp--return null--------" + obj);
    		return null;   
    	}  
    }  
    public static String getDateBySqlTimestamp(Object obj,String formatStr) {  //Object obj, String formatStr
        try {  
            TIMESTAMP t = (TIMESTAMP)obj;  
            if (formatStr == null || formatStr.equals("")) {  
                formatStr = "dd/MM/yyyy";  
            }  
            Timestamp tt;  
            tt = t.timestampValue();  
            Date date = new Date(tt.getTime());  
            SimpleDateFormat sd = new SimpleDateFormat(formatStr);  
            return sd.format(date);  
//            return date;              
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    } 
}
