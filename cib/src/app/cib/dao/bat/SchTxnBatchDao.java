/**
 * @author hjs
 * 2006-10-30
 */
package app.cib.dao.bat;

import java.util.ArrayList;
import java.util.List;

import com.neturbo.set.core.Config;
import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 2006-10-30
 */
public class SchTxnBatchDao extends GenericHibernateDao {
	
	private static final String defalutPattern = Config.getProperty("DefaultDatePattern");
	
	public void cancelByUserId(String userId) throws NTBException {
		
	}

	public List listRecords(String beneficiaryType, String corpId, String userId, 
			String dateFrom, String dateTo) throws NTBException {
		List valueList = new ArrayList();
		String hql = "from ScheduleTransferBatch as tb where tb.corporaitonId= ? ";
		valueList.add(corpId.trim());

		if (!Utils.null2EmptyWithTrim(beneficiaryType).equals("")) {
			hql = hql + "and  tb.beneficiaryType = ? ";
			valueList.add(beneficiaryType.trim());
		}

		if (!Utils.null2EmptyWithTrim(userId).equals("")) {
			hql = hql + "and  tb.userId = ? ";
			valueList.add(userId.trim());
		}
		if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
			hql = hql + "and tb.scheduleDate >= ? ";
			dateFrom = DateTime.formatDate(dateFrom.trim(), defalutPattern, "yyyyMMdd");
			valueList.add(dateFrom);
		}
		if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
			hql = hql + "and tb.scheduleDate <= ? ";
			dateTo = DateTime.formatDate(dateTo.trim(), defalutPattern, "yyyyMMdd");
			valueList.add(dateTo);
		}
		hql += "order by tb.scheduleDate DESC";
		return this.list(hql, valueList.toArray());
	}

}
