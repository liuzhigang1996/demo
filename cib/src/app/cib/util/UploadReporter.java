package app.cib.util;

import java.util.*;

import org.springframework.context.ApplicationContext;

import app.cib.core.CibTransClient;

import com.neturbo.set.core.*;
import com.neturbo.set.database.*;
import com.neturbo.set.utils.Utils;

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
public class UploadReporter {

    private static List bufferList = new ArrayList();

    public UploadReporter() {
    }


    public static boolean write(String tableName, Map data) {
        ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean(
                "genericJdbcDao");
        try {
            dao.add(tableName, data);
        } catch (Exception e) {
            Log.error("Error writing tabel of upload report: " + tableName, e);
            return false;
        }
        return true;
    }

    public static void writeBuffer(String tableName, Map data) {
        if (!Utils.null2EmptyWithTrim(tableName).equals("")) {
            if (data != null) {
                bufferList.add(new Object[] {tableName, data});
            } else {
                Log.error("UploadReporter add buffer list error, data is null");
            }
        } else {
            Log.error(
                    "UploadReporter add buffer list error, tableName is null or empty");
        }
    }

    public static synchronized void flushBuffer() {
        try {
            List tmpList = new ArrayList(bufferList);
            Object[] objArray = null;
            for (int i = 0; i < tmpList.size(); i++) {
                objArray = (Object[]) tmpList.get(i);
                write(objArray[0].toString(), (Map) objArray[1]);
                bufferList.remove(objArray);
            }
        } catch (Exception e) {
            Log.error("UploadReporter flush buffer list error", e);
        }
    }

    public static void writeAccEnquiryRpt(Map toHost, Map fromHost,
                                          NTBUser userObj) {

        Map uploadMap = new HashMap();
        uploadMap.put("TRANS_DATE", CibTransClient.getCurrentDate());
        uploadMap.put("TRANS_TIME ", CibTransClient.getCurrentTime());
        uploadMap.put("TRANS_CODE ", toHost.get("TRANSACTION_CODE"));
        uploadMap.put("TRANS_REF_NO ",
                      fromHost.get("TELLER_ID").toString() +
                      fromHost.get("UNIQUE_SEQUENCE_NO").toString());
        uploadMap.put("APPLICATION_CODE ", toHost.get("APPLICATION_CODE"));
        uploadMap.put("ACC_NO ", toHost.get("ACCOUNT_NO"));
        uploadMap.put("USER_ID ", userObj.getUserId());
        uploadMap.put("CORPRATION_ID", toHost.get("CORPORATION_ID"));
        uploadMap.put("STATUS ", fromHost.get("RESPONSE_CODE"));
        uploadMap.put("REJECT_CODE ", fromHost.get("REJECT_CODE"));
        UploadReporter.write("RP_ACCTENQ", uploadMap);

    }
}
