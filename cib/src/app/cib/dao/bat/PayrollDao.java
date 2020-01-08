/**
 * @author hjs
 * 2006-10-11
 */
package app.cib.dao.bat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import app.cib.util.Constants;

import com.neturbo.set.core.Config;
import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs 2006-10-11
 */
public class PayrollDao extends GenericHibernateDao {

	private static final String defalutPattern = Config.getProperty("DefaultDatePattern");

	public List listHistory(String corpId, String userId, String dateFrom, String dateTo) throws NTBException {

		String hql = "from Payroll as pr where pr.corpId = ? ";
		List valueList = new ArrayList();

		valueList.add(corpId);

		if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
			dateFrom = DateTime.formatDate(dateFrom, defalutPattern, "yyyy-MM-dd");
			Timestamp timeFrom = DateTime.getTimestampByStr(dateFrom, true);
			hql += "and pr.requestTime >= ? ";
			valueList.add(timeFrom);
		}
		if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
			dateTo = DateTime.formatDate(dateTo, defalutPattern, "yyyy-MM-dd");
			Timestamp timeTo = DateTime.getTimestampByStr(dateTo, false);
			hql += "and pr.requestTime <= ? ";
			valueList.add(timeTo);
		}
		if (!Utils.null2EmptyWithTrim(userId).equals("")) {
			hql += "and pr.userId = ? ";
			valueList.add(userId);
		}

		hql += "order by pr.requestTime";

		return this.list(hql, valueList.toArray());
	}
	
	public List calculate(String payrollId) throws NTBException {
//		String hql = "select sum(p.creditAmount), count(*) from PayrollRec  as p where p.payrollId = ? and p.detailResult = ?";
		String hql = "select sum(rp.creditAmount), count(*) from PayrollRec rp where rp.payrollId = ? and rp.detailResult = ? ";//mod by linrui 20180323
//		return this.list(hql, new Object[] {payrollId, "0"});
		return this.list(hql, new Object[] {payrollId, "0"});//mod by linrui 20180323
	}

}
