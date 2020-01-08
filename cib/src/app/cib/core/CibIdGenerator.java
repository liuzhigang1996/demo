package app.cib.core;

import com.neturbo.set.database.IDGenerator;
import org.springframework.context.ApplicationContext;
import com.neturbo.set.core.*;
import app.cib.bo.sys.TellerSequence;
import app.cib.bo.sys.IdGenerator;
import com.neturbo.set.utils.*;
import com.neturbo.set.database.GenericHibernateDao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

import com.neturbo.set.database.GenericJdbcDao;
import com.sun.org.apache.bcel.internal.generic.NEW;

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
public class CibIdGenerator {

    private static GenericJdbcDao dao;
    private static IdGeneratorJdbcDao dao1;
    private static int tellerNumber = 0;
    private static String[] tellerList;
    private static boolean initialized = false;

    private static Object lockFlag = new Object();
    private static boolean fileFlag = true;

    //ȡ���
    private static String SELECT_ALL_SEQ =
            "SELECT SEQ_NO,TELLER,CURRENT_SEQUENCE,EFFECTIVE_DATE FROM TELLER_SEQUENCE";

    private static String SELECT_SEQ =
            "SELECT SEQ_NO,TELLER,CURRENT_SEQUENCE,EFFECTIVE_DATE FROM TELLER_SEQUENCE WHERE TELLER = ? AND EFFECTIVE_DATE=? ORDER BY SEQ_NO DESC";

    //������ݿ�����±��
    private static String UPDATE_SEQ =
            "UPDATE TELLER_SEQUENCE SET TELLER=?,CURRENT_SEQUENCE=?,EFFECTIVE_DATE=? WHERE SEQ_NO=? ";

    private static String ADD_SEQ =
            "INSERT INTO TELLER_SEQUENCE(SEQ_NO,TELLER,CURRENT_SEQUENCE,EFFECTIVE_DATE) VALUES(?,?,?,?)";

    private static String SELECT_TELLER_COUNTER =
            "select TELLER_COUNTER.nextval from dual";//mod

    private static String SELECT_OPERATION_ID =
        "select OPERATION_ID.nextval from dual";//mod

    private static String SELECT_BATCH_RECORD_ID =
        "select BATCH_RECORD_ID.nextval from dual";//mod
    //add by linrui for file name only one
    private static String SELECT_FILE_NAME_SEQ =
    	"select replace(lpad(substr(FILE_NAME_SEQ.nextval,-2,2),2),' ','0') from dual";
    //add by linrui for GET TD REFERENCE
    private static String SELECT_TD_REF =
    	"select replace(lpad(TIME_DEPOSIT_SEQ.nextval,10),' ','0') from dual";

    static {
        if (!initialized) {
            populateCache();
        }
    }

    public static void populateCache() {
        try {
            ApplicationContext appContext = Config.getAppContext();
            dao = (GenericJdbcDao) appContext.getBean(
                    "genericJdbcDao");
            dao1 = (IdGeneratorJdbcDao) appContext.getBean("idGeneratorJdbcDao");

            String tellerListStr = Config.getProperty("TransTellerList");
            tellerList = Utils.splitStr(tellerListStr, ",");
            Log.info("Teller ID ready for transaction: " + tellerListStr);
            tellerNumber = tellerList.length;
            initialized = true;

        } catch (Exception e) {
            Log.error("Error populating cache for Teller Sequence", e);
        }
    }

    public static String getRefNoForTransaction() {

        try {
            synchronized(lockFlag){
            	/*//mod by linrui for oracle 20171206
            	BigDecimal idobjBig =(BigDecimal)dao.querySingleValue(
                        SELECT_TELLER_COUNTER, new Object[] {});
            	Long idOjb = idobjBig.longValue();
//            	Long idOjb = (Long)dao.querySingleValue(
//                        SELECT_TELLER_COUNTER, new Object[] {});
                long currentIndex = idOjb.longValue();
                int currentOrder = (int) (currentIndex % tellerNumber);
                String currentTeller = tellerList[currentOrder];

                String currentDate = CibTransClient.getCurrentDate();
                List seqMapList = dao.query(SELECT_SEQ,
                                            new Object[] {currentTeller, currentDate});

                Map currentSeqMap = new HashMap();
                int iCurrentSeq =0;
                if(seqMapList.size() > 0){
                    currentSeqMap = (Map) seqMapList.get(0);
                    //mod by linrui for oracle 20171206
                    BigDecimal intercurren =(BigDecimal)currentSeqMap.get(
                          "CURRENT_SEQUENCE");   
                    Integer integerCurrentSeq = intercurren.intValue();
                    iCurrentSeq = integerCurrentSeq.intValue();
                }
                iCurrentSeq ++;
                String idStr = String.valueOf(currentIndex);
                idStr = "SQ" + Utils.prefixZero(idStr, 16);
                currentSeqMap.put("SEQ_NO", idStr);
                currentSeqMap.put("CURRENT_SEQUENCE", new Integer(iCurrentSeq));
                currentSeqMap.put("EFFECTIVE_DATE", currentDate);
                currentSeqMap.put("TELLER", currentTeller);

                
               dao1.addTellerSequence((String) currentSeqMap.get("SEQ_NO"),
                                         (String) currentSeqMap.get("TELLER"),
                                         (Integer) currentSeqMap.get(
                       "CURRENT_SEQUENCE"),
                                         (String) currentSeqMap.get(
                       "EFFECTIVE_DATE"));

                

                String currentSeq = Utils.prefixZero(String.valueOf(iCurrentSeq),
                        4);
                String refNo = "TH" + currentTeller + currentSeq + currentDate;
                return refNo;*/
            	//mod by linrui for useless teller seq logic
            	BigDecimal idobjBig =(BigDecimal)dao.querySingleValue(
            			SELECT_BATCH_RECORD_ID, new Object[] {});
            	Long idOjb = idobjBig.longValue();
                long currentIndex = idOjb.longValue();
                int currentOrder = (int) (currentIndex % tellerNumber);
                String currentTeller = tellerList[currentOrder];

                String currentDate = CibTransClient.getCurrentDate();
            	String currentTime = Utils.prefixZero(CibTransClient.getCurrentTime(),4);
                String refNo = "TH" + currentTeller + currentTime + currentDate;
                return refNo;
                //end
            }
        } catch (Exception e) {
            Log.error("Error generation reference no for transaction", e);
        }
        return null;

    }

    public static String getIdForOperation(String txnType) {
        String[] ids = getIdsForOperation(txnType, 1);
        return String.valueOf(ids[0]);

    }

    public static String[] getIdsForOperation(String txnType,
            int number) {

        String[] idStrs = new String[number];
        try {
            for (int i = 0; i < number; i++) {
            	//mod by linrui
            	BigDecimal idobjBig =(BigDecimal)dao.querySingleValue(
            			SELECT_OPERATION_ID, new Object[] {});
            	Long idOjb = idobjBig.longValue();
//                Long idOjb = (Long) dao.querySingleValue(
//                        SELECT_OPERATION_ID, new Object[] {});
                String idStr = String.valueOf(idOjb.longValue());
                idStr = "DM" + Utils.prefixZero(idStr, 16);
                idStrs[i] = String.valueOf(idStr);
            }
        } catch (Exception e) {
            Log.error("Error generation opearion id", e);
        }
        return idStrs;
    }

    public static String getIdForBatchRecord(String txnType) {
        String[] ids = getIdForBatchRecord(txnType, 1);
        return String.valueOf(ids[0]);

    }

    public static String[] getIdForBatchRecord(String txnType,
            int number) {

        String[] idStrs = new String[number];
        try {
            for (int i = 0; i < number; i++) {
            	//mod by linrui
            	BigDecimal idobjBig =(BigDecimal)dao.querySingleValue(
            			SELECT_BATCH_RECORD_ID, new Object[] {});
            	Long idOjb = idobjBig.longValue();
//                Long idOjb = (Long) dao.querySingleValue(
//                        SELECT_BATCH_RECORD_ID, new Object[] {});
                String idStr = String.valueOf(idOjb.longValue());
                idStr = "BT" + Utils.prefixZero(idStr, 16);
                idStrs[i] = String.valueOf(idStr);
            }
        } catch (Exception e) {
            Log.error("Error generation batch record id", e);
        }
        return idStrs;
    }
    //add by linrui for get only filename to host for overbank single to many 20180122
    public synchronized static String getFileNameSequence(){   	
    	try {
    	 String idobjBig = (String)dao.querySingleValue(
    			SELECT_FILE_NAME_SEQ, new Object[] {});
//    	 Long idOjb = idobjBig.longValue();
    	 return "MA"+idobjBig;   	
    	}catch (Exception e) {
    		Log.error("Error generation batch record id", e);
		}
    	return null;
    }
    //add by linrui for get time deposit ref 20180122
    public synchronized static String getTDRef(){   	
    	try {
    		SimpleDateFormat sFormat = new SimpleDateFormat("yyMMdd");
    		String idobjBig = (String)dao.querySingleValue(
    				SELECT_TD_REF, new Object[] {});
//    	 Long idOjb = idobjBig.longValue();
    		return sFormat.format(new Date())+idobjBig;   	
    	}catch (Exception e) {
    		Log.error("Error generation batch record id", e);
    	}
    	return null;
    }

}
