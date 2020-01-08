/**
 * @author hjs
 * 2007-4-24
 */
package app.cib.dao.srv;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 2007-4-24
 */
public class EAdviceDao extends GenericJdbcDao {

	private static final String defalutPattern = Config.getProperty("DefaultDatePattern");
	
	public List listRecords(String corpId, String range,
			String dateFrom, String dateTo, String accountType,	String accountNo,
			String operation, String refType, String reference) throws NTBException {


		String sql = "select * from HS_E_ADVICE where CORP_ID = ? ";
		List valueList = new ArrayList();

		valueList.add(corpId);

		if (!Utils.null2EmptyWithTrim(range).equals("")) {
			Calendar cal = Calendar.getInstance();
			if(Utils.null2EmptyWithTrim(range).equals("1")){
				cal.add(Calendar.DATE, -1);
				dateFrom = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateTo = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
			} else if(Utils.null2EmptyWithTrim(range).equals("2")){
				cal.add(Calendar.DATE, -7);
				cal.set(Calendar.DAY_OF_WEEK, 1);
				dateFrom = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				cal.set(Calendar.DAY_OF_WEEK, 7);
				dateTo = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
			} else if(Utils.null2EmptyWithTrim(range).equals("3")){
				cal.set(Calendar.DAY_OF_WEEK, 1);
				dateFrom = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				cal.set(Calendar.DAY_OF_WEEK, 7);
				dateTo = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
			} else if(Utils.null2EmptyWithTrim(range).equals("4")){
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.add(Calendar.DATE, -1);
				dateTo = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				cal.set(Calendar.DAY_OF_MONTH, 1);
				dateFrom = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
			} else {
				cal.add(Calendar.MONTH, 1);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.add(Calendar.DATE, -1);
				dateTo = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				cal.set(Calendar.DAY_OF_MONTH, 1);
				dateFrom = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
			}
			sql += "and STATEMENT_DATE >= ? and STATEMENT_DATE <= ? ";
			valueList.add(dateFrom);
			valueList.add(dateTo);
		} else {
			if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
				dateFrom = DateTime.formatDate(dateFrom, defalutPattern, "yyyyMMdd");
				sql += "and STATEMENT_DATE >= ? ";
				valueList.add(dateFrom);
			}
			if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
				dateTo = DateTime.formatDate(dateTo, defalutPattern, "yyyyMMdd");
				sql += "and STATEMENT_DATE <= ? ";
				valueList.add(dateTo);
			}
			
		}
		if ((!Utils.null2EmptyWithTrim(accountType).equals("")) && (!accountType.equals("0"))) {
			sql += "and ACCOUNT_TYPE = ? ";
			valueList.add(accountType);
		}
		if ((!Utils.null2EmptyWithTrim(accountNo).equals("")) && (!accountNo.equals("0"))) {
			sql += "and ACCOUNT_NO = ? ";
			valueList.add(accountNo);
		}
		if ((!Utils.null2EmptyWithTrim(operation).equals("")) && (!operation.equals("0"))) {
			sql += "and TYPE_OF_OPERATION = ? ";
			valueList.add(operation);
		}
		if (!Utils.null2EmptyWithTrim(refType).equals("") && (!refType.equals("0"))) {
			if(refType.equals("1")){
				sql += "and BANK_REFERENCE like '%"+reference+"%' ";
			} else {
				sql += "and CLIENT_REFERENCE like '%"+reference+"%' ";
			}
		}

		sql += "order by STATEMENT_DATE desc, ACCOUNT_TYPE, ACCOUNT_NO, TYPE_OF_OPERATION, CLIENT_REFERENCE";


		try {
			return this.query(sql, valueList.toArray());
		} catch (Exception e) {
			Log.error("DB Error", e);
			throw new NTBException("err.sys.DBError");
		}
	}

}
