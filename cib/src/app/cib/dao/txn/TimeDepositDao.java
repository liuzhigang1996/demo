/**
 * 
 */
package app.cib.dao.txn;

import java.sql.Timestamp;
import java.util.*;

import org.springframework.dao.DataAccessResourceFailureException;

import net.sf.hibernate.HibernateException;

import com.neturbo.set.core.Config;
import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 *
 */
public class TimeDepositDao extends GenericHibernateDao{
	
	private static final String defalutPattern = Config.getProperty("DefaultDatePattern");

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
	}
	
	public List listTimeDeposit(String corpID, String userID) throws DataAccessResourceFailureException, HibernateException, IllegalStateException{
		return null;
	}
	
	public List listHistory(String corpId, String userId, String dateFrom, String dateTo) throws NTBException {
		String hql = "from TimeDeposit as td where td.corpId = ? ";
		List valueList = new ArrayList();
		
		valueList.add(corpId);
		
		dateFrom = Utils.null2EmptyWithTrim(dateFrom);
		dateTo = Utils.null2EmptyWithTrim(dateTo);
		
		if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
			dateFrom = DateTime.formatDate(dateFrom, defalutPattern, "yyyy-MM-dd");
			Timestamp timeFrom = DateTime.getTimestampByStr(dateFrom, true);
			//dateFrom += " 00:00:00";
			hql += "and td.requestTime >= ? ";
			valueList.add(timeFrom);
		}
		if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
			dateTo = DateTime.formatDate(dateTo, defalutPattern, "yyyy-MM-dd");
			Timestamp timeTo = DateTime.getTimestampByStr(dateTo, false);
			//dateTo += " 23:59:59";
			hql += "and td.requestTime <= ? ";
			valueList.add(timeTo);
		}
		if (!Utils.null2EmptyWithTrim(userId).equals("")) {
			hql += "and td.userId = ? order by request_time desc";
			valueList.add(userId);
		}
		
		List list = this.list(hql, valueList.toArray());
		
		return list;
	}

}
