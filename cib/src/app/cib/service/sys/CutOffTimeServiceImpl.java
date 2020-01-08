/**
 * @author hjs
 * 2006-11-20
 */
package app.cib.service.sys;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import app.cib.service.utl.UtilService;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.xml.XMLElement;

/**
 * @author hjs
 * 2006-11-20
 */
public class CutOffTimeServiceImpl implements CutOffTimeService {
	
	
	private static final String CHECK_SQL = "select * from HS_CUT_OFF_TIME where TRANS_CODE = ? and CCY_CODE = ?";
	
	private static final String CHECK_SQL_for_aofa = "select * from HS_CUT_OFF_TIME where TRANS_CODE = ? and CCY_CODE = ? and transferType = ?";
	
	private GenericJdbcDao dao;
	
	final String dayNames[] = { "0", "1", "2", "3", "4", "5","6" };

	public GenericJdbcDao getDao() {
		return dao;
	}

	public void setDao(GenericJdbcDao dao) {
		this.dao = dao;
	}
	
	public String check(String transCode, String fromCcy, String toCcy) throws NTBException {
		transCode = transCode.trim();
		fromCcy = fromCcy.trim();
		toCcy = toCcy.trim();
		
		String ccy = "";
		try {
			Map row = null;
			if (transCode.substring(0, 2).equals("51")) {
				//add by chen_y for CR192 20161213
				String bankFullDay = Config.getProperty("bankFullDayCurrency") ;
				if(null !=fromCcy && !"".equals(fromCcy) 
						&& null!=bankFullDay && !"".equals(bankFullDay)
						&& toCcy!=null && !"".equals(toCcy)){
		    		
		    		if( 0<=bankFullDay.indexOf(fromCcy) 
		    				&& 0<=bankFullDay.indexOf(toCcy) ){

		    			return null;
		    		}
		    	}
				//add by chen_y for CR192 20161213
				if (fromCcy.equals(toCcy)) {
					ccy = "";
				} else {
					ccy = "EX";
				}
			} else {
				ccy = toCcy;
			}
			row = (Map) dao.querySingleRow(CHECK_SQL, new Object[]{transCode, ccy});
			if (row == null) {
				row = (Map) dao.querySingleRow(CHECK_SQL, new Object[]{transCode, ""});
			}
			if (row == null) {
				return null;
			}
			if (Utils.null2EmptyWithTrim(row.get("VALUE_DATE_WARNING_MSG")).equals("") || Utils.null2EmptyWithTrim(row.get("VALUE_DATE_ALERT_MSG")).equals("")) {
				Log.warn("Transaction[" + transCode + "] had no any warning message or alert message");
				return null;
			}
				
			String cutoffTime = row.get("VALUE_DATE_CUT_OFF").toString().trim();
			String currTime = DateTime.formatDate(new Date(), "HHmm");
			if (Integer.parseInt(currTime) > Integer.parseInt(cutoffTime)) {
				return row.get("VALUE_DATE_ALERT_MSG").toString().trim();
			} else if (checkTime(cutoffTime, currTime)) {
				return null;
			} else {
				return row.get("VALUE_DATE_WARNING_MSG").toString().trim();
			}
		} catch (Exception e) {
			Log.error("Check value date cut-off time error", e);
			throw new NTBException("err.sys.CheckValueDateCutOffError");
		}
	}
	
	public String check(String transCode, String fromCcy, String toCcy,String transferType,String lang) throws NTBException {
		
		ApplicationContext appContext = Config.getAppContext();
		
		UtilService utilService = (UtilService)appContext
		.getBean("UtilService");
		
		transCode = transCode.trim();
		fromCcy = fromCcy.trim();
		toCcy = toCcy.trim();
		String ccy = "";
		//add by chen_y for CR192 20161213
		/*String bankFullDay = Config.getProperty("bankFullDayCurrency") ;
		if(null !=fromCcy && !"".equals(fromCcy) 
				&& null!=bankFullDay && !"".equals(bankFullDay)
				&& toCcy!=null && !"".equals(toCcy)){
    		
    		if( 0<=bankFullDay.indexOf(fromCcy) 
    				&& 0<=bankFullDay.indexOf(toCcy) ){

    			return null;
    		}
    	}*/
		//add by chen_y for CR192 20161213
		
		//add by lzg 20190521
		String nextDay = "";
		//add by lzg end
		
		try {
			Map row = null;
			if (transCode.substring(0, 2).equals("51")) {
				//modified by lzg 20190619
				/*if (fromCcy.equals(toCcy)) {
					ccy = "";
				} else {
					ccy = "EX";
				}*/
				if(("HKD".equals(toCcy) || "MOP".equals(toCcy)) && ("HKD".equals(fromCcy) || "MOP".equals(fromCcy))){
					return null;
				}else if(fromCcy.equals(toCcy)){
					return null;
				}else{
					ccy = "Y";
				}
				//modified by lzg end
			} else {
				ccy = toCcy;
			}
			
			//add by lzg 20190521
//			Date date = new Date();
			Date date = DateTime.getHostTime();
			//end
			if(utilService.isHoliday(date)){
				nextDay = getNextDay(utilService);
				row = (Map) dao.querySingleRow(CHECK_SQL_for_aofa,new Object[]{transCode, ccy,transferType});
				if("C".equals(lang)){
					return row.get("VALUE_DATE_ALERT_MSG").toString().trim();
				}else if("E".equals(lang)){
					return row.get("VALUE_DATE_WARNING_MSG").toString().trim();
				}
			}
			//add by lzg end
			
			row = (Map) dao.querySingleRow(CHECK_SQL_for_aofa, new Object[]{transCode, ccy,transferType});
			if (row == null) {
				row = (Map) dao.querySingleRow(CHECK_SQL_for_aofa, new Object[]{transCode, "",transferType});
			}
			if (row == null) {
				return null;
			}
			if (Utils.null2EmptyWithTrim(row.get("VALUE_DATE_WARNING_MSG")).equals("") || Utils.null2EmptyWithTrim(row.get("VALUE_DATE_ALERT_MSG")).equals("")) {
				Log.warn("Transaction[" + transCode + "] had no any warning message or alert message");
				return null;
			}
				
			String cutoffTime = row.get("VALUE_DATE_CUT_OFF").toString().trim();
			String currTime = DateTime.formatDate(new Date(), "HHmm");
			nextDay = getNextDay(utilService);
			
			/*if (Integer.parseInt(currTime) > Integer.parseInt(cutoffTime)) {
				return row.get("VALUE_DATE_ALERT_MSG").toString().trim() + nextDay;
			} else if (checkTime(cutoffTime, currTime)) {
				return null;
			} else {
				return row.get("VALUE_DATE_WARNING_MSG").toString().trim();
			}*/
			if (Integer.parseInt(currTime) > Integer.parseInt(cutoffTime)) {
				if("C".equals(lang)){
					return row.get("VALUE_DATE_ALERT_MSG").toString().trim();
				}else if("E".equals(lang)){
					return row.get("VALUE_DATE_WARNING_MSG").toString().trim();
				}
			}else if(Integer.parseInt(currTime) < 900){
				if("C".equals(lang)){
					return row.get("VALUE_DATE_ALERT_MSG").toString().trim();
				}else if("E".equals(lang)){
					return row.get("VALUE_DATE_WARNING_MSG").toString().trim();
				}
			}else{
				return null;
			}
			return null;
		} catch (Exception e) {
			Log.error("Check value date cut-off time error", e);
			throw new NTBException("err.sys.CheckValueDateCutOffError");
		}
	}
	
	private String getNextDay(UtilService utilService) throws NTBException {
		SimpleDateFormat sf = new SimpleDateFormat("MM. dd yyyy");
		Calendar c = Calendar.getInstance();
		while(true){
			c.add(Calendar.DAY_OF_MONTH, 1);
			if(!utilService.isHoliday(c.getTime())){
				break;
			}
		}
		String nextDay = sf.format(c.getTime());
		return nextDay;
	}

	private boolean checkTime(String cutoffTime, String currTime) throws NTBException  {
		return (strTime2Minute(cutoffTime) - strTime2Minute(currTime)) >= 30 ; 
	}
	
	private long strTime2Minute(String strTime) throws NTBException {
		long hour = Integer.parseInt(strTime.substring(0,2));
		long minute = Integer.parseInt(strTime.substring(2,4));
		
		return (hour * 60) + minute;
	}
	
	/**
	 * 
	 * @param transCode
	 * @return true:δ����30����
	 * @throws NTBException
	 */
	
	//modify by chen_y 2016-10-18 for CR192 batch bob
	//public String checkService(String transCode) throws NTBException {
	public String checkService(String transCode,boolean isFX) throws NTBException {
		transCode = transCode.trim();
		String ccy="";
		try {
			if (transCode.substring(0, 2).equals("51")) {
				if (!isFX) {
					ccy = "";
				} else {
					ccy = "EX";
				}
			}else{
				ccy = "";
			}
			Map row = (Map) dao.querySingleRow(CHECK_SQL, new Object[]{transCode, ccy});
			if (row == null) {
				return null;
			}
			String beginTime = row.get("SERVICE_BEGIN").toString().trim();
			String cutoffTime = row.get("SERVICE_CUT_OFF").toString().trim();
			String currTime = DateTime.formatDate(new Date(), "HHmm");
			if (Integer.parseInt(currTime)<Integer.parseInt(beginTime) || Integer.parseInt(currTime)>Integer.parseInt(cutoffTime)) {
				return row.get("SERVICE_ALERT_MSG").toString().trim();
			} else {
				return null;
			}
		} catch (Exception e) {
			Log.error("Check service cut-off time error", e);
			throw new NTBException("err.sys.CheckServiceCutOffError");
		}
	}
	
	//modify by long_zg 2014-12-15 for CR192 batch bob
	/*public void checkTransAvailable(String transCode) throws NTBException {*/
	public void checkTransAvailable(String transCode,boolean fullDay, boolean isFX) throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();
    	UtilService utilService = (UtilService)appContext.getBean("UtilService");
    	
    	XMLElement configFile = Config.getConfigXML("HostNotAvailableInHolidayXML");

        // add by hjs
    	if (configFile == null) {
    		throw new NTBException("err.host.MissingHostNotAvailableInHolidayXML");
    	}

    	// modify by chen_y 2017-05-31 for CR192 batch bob
    	if(!fullDay){
	    	if (configFile.findNodeByAtrribute("trans", "code", transCode) != null) {
	            if (utilService.isHoliday(new Date())) {
	                throw new NTBException("err.host.HostTxnNotAvailableToday");
	            }
	    	}
    	}
    	//modify by long_zg 2014-12-15 for CR192 batch bob
        /*String msg = checkService(transCode);*/
    	String msg =null ;
    	/*if(!fullDay){
    		msg = checkService(transCode,isFX);
    	}*/
    	
    	if (msg != null) {
    		throw new NTBException(msg);
    	}
		
	}
	
}
