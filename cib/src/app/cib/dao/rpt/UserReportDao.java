/**
 * @author hjs
 * 2006-11-9
 */
package app.cib.dao.rpt;

import java.util.ArrayList;
import java.util.List;

import com.neturbo.set.core.Config;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 2006-11-9
 */
public class UserReportDao extends GenericJdbcDao {

	private static final String defalutPattern = Config.getProperty("DefaultDatePattern");

	public List listActivityLogReport(String corpId, String userId,
			String dateFrom, String dateTo)
			throws NTBException {

		//String sql = "select t1.*, t2.LOGOUT_DATE, t2.LOGOUT_TIME, t3.USER_NAME, t4.CORP_NAME from RP_LOGIN t1, RP_LOGOUT t2, CORP_USER t3, CORPORATION t4 where t1.SEQ_NO = t2.SEQ_NO and t1.USER_ID = t3.USER_ID and t1.CORPORATION_ID = t4.CORP_ID and t1.CORPORATION_ID = ? ";
		String sql = "select t5.*, t3.USER_NAME, t4.CORP_NAME from CORP_USER t3, CORPORATION t4, (select t1.*,t2.LOGOUT_DATE,t2.logout_time from RP_LOGIN t1 left join RP_LOGOUT t2  on t1.SEQ_NO = t2.SEQ_NO) as t5 ";
		sql += "where t5.USER_ID = t3.USER_ID and t5.CORPORATION_ID = t4.CORP_ID and t5.CORPORATION_ID = ? ";
		
		List valueList = new ArrayList();

		valueList.add(corpId);

		if ((!Utils.null2EmptyWithTrim(userId).equals(""))) {
			sql += "and t5.USER_ID = ? ";
			valueList.add(userId);
		}
		if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
			dateFrom = DateTime.formatDate(dateFrom, defalutPattern, "yyyyMMdd");
			//Timestamp timeFrom = DateTime.getTimestampByStr(dateFrom, true);
			sql += "and t5.LOGIN_DATE >= ? ";
			valueList.add(dateFrom);
		}
		if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
			dateTo = DateTime.formatDate(dateTo, defalutPattern, "yyyyMMdd");
			//Timestamp timeTo = DateTime.getTimestampByStr(dateTo, false);
			sql += "and t5.LOGIN_DATE <= ? ";
			valueList.add(dateTo);
		}

		sql += "order by t5.LOGIN_DATE desc, t5.LOGOUT_DATE desc";

		try {
			return this.query(sql, valueList.toArray());
		} catch (Exception e) {
			throw new NTBException();
		}
	}
}
