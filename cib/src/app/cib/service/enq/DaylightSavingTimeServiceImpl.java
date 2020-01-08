/**
 * @author hjs
 * 2007-4-27
 */
package app.cib.service.enq;

import java.util.*;

import org.springframework.context.ApplicationContext;

import app.cib.bo.enq.DaylightSavingTime;
import app.cib.dao.enq.DaylightSavingTimeDao;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 2007-4-27
 */
public class DaylightSavingTimeServiceImpl implements DaylightSavingTimeService {
	
	private DaylightSavingTimeDao daylightSavingTimeDao;

	public DaylightSavingTimeDao getDaylightSavingTimeDao() {
		return daylightSavingTimeDao;
	}

	public void setDaylightSavingTimeDao(DaylightSavingTimeDao daylightSavingTimeDao) {
		this.daylightSavingTimeDao = daylightSavingTimeDao;
	}

	public DaylightSavingTime checkDST(String cityName) throws NTBException {
		DaylightSavingTime bean = new DaylightSavingTime();
		List list = daylightSavingTimeDao.listByCity(cityName);
		if(list.size()>0){
			Map row = null;
			for(int i=0; i<list.size(); i++){
				row = (Map) list.get(i);
				if(checkRow(row)){
					return toBean(row);
				}
			}
		}
		return bean;
	}
	
	private boolean checkRow(Map row) throws NTBException {
		String dstTimeZone = Utils.null2EmptyWithTrim(row.get("DST_TIME_ZONE"));
		String dstBeginTime = Utils.null2EmptyWithTrim(row.get("DST_BEGIN_TIME"));
		String dstEndTime = Utils.null2EmptyWithTrim(row.get("DST_END_TIME"));
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -8);
		if(dstTimeZone.charAt(0)=='+'){
			cal.add(Calendar.HOUR, Integer.parseInt(dstTimeZone.substring(1)));
		} else {
			cal.add(Calendar.HOUR, Integer.parseInt(dstTimeZone));
		}
		Date cityTime = cal.getTime();
		Date beginTime = DateTime.getDateFromStr(dstBeginTime, "yyyyMMddHHmmss");
		Date endTime = DateTime.getDateFromStr(dstEndTime, "yyyyMMddHHmmss");
		if(DateTime.compareDate(cityTime, beginTime, "yyyyMMddHHmmss")>=0 
				&& DateTime.compareDate(endTime, cityTime, "yyyyMMddHHmmss")>0){
			return true;
		} else {
			return false;
		}
	}
	
	private DaylightSavingTime toBean(Map row) throws NTBException{
		DaylightSavingTime bean = new DaylightSavingTime();
		bean.setCityName(row.get("CITY_NAME").toString().trim());
		bean.setDstBeginTime(row.get("DST_BEGIN_TIME").toString().trim());
		bean.setDstEndTime(row.get("DST_END_TIME").toString().trim());
		bean.setDstTimeZone(row.get("DST_TIME_ZONE").toString().trim());
		bean.setDstTimeZoneName(row.get("DST_TIME_ZONE_NAME").toString().trim());
		bean.setTimeZoneName(row.get("TIME_ZONE_NAME").toString().trim());
		bean.setFlag(true); 
		return bean;
		
	}
	
	public static void main(String[] args) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		DaylightSavingTimeService service = (DaylightSavingTimeService) appContext.getBean("DaylightSavingTimeService");
		DaylightSavingTime bean = service.checkDST("Melbourne");
		System.err.println(bean);
	}

}
