package app.cib.dao.bnk;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import app.cib.util.Constants;

import com.neturbo.set.core.Config;
import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

public class BankUserHisDao extends GenericHibernateDao{
	
	private static final String defalutPattern = Config.getProperty("DefaultDatePattern");

	public List listRecords(String requester, String dateFrom, String dateTo) throws NTBException {
		List valueList = new ArrayList();
		String hql = "from BankUserHis as t where t.operation<>? ";
		valueList.add(Constants.OPERATION_UPDATE_BEFORE);

		if (!Utils.null2EmptyWithTrim(requester).equals("")) {
			hql = hql + "and t.requester = ? ";
			valueList.add(requester.trim());
		}
		if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
			dateFrom = DateTime.formatDate(dateFrom, defalutPattern, "yyyy-MM-dd");
			Timestamp timeFrom = DateTime.getTimestampByStr(dateFrom, true);
			hql = hql + "and t.lastUpdateTime >= ? ";
			valueList.add(timeFrom);
		}
		if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
			dateTo = DateTime.formatDate(dateTo, defalutPattern, "yyyy-MM-dd");
			Timestamp timeTo = DateTime.getTimestampByStr(dateTo, false);
			hql = hql + "and t.lastUpdateTime <= ? ";
			valueList.add(timeTo);
		}
		hql += "order by t.requester, t.lastUpdateTime DESC";
		return this.list(hql, valueList.toArray());
	}

}
