/**
 * @author hjs
 * 2007-4-27
 */
package app.cib.dao.enq;

import java.util.*;

import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2007-4-27
 */
public class DaylightSavingTimeDao extends GenericJdbcDao {
	
	public List listByCity(String cityName) throws NTBException {
		String sql = "select * from HS_DAYLIGHT_SAVING_TIME where CITY_NAME = ? ";
		
		try {
			return this.query(sql, new Object[]{cityName});
		} catch (Exception e) {
			Log.error("query daylight saving time info error", e);
			throw new NTBException("err.sys.DBError");
		}
	}

}
