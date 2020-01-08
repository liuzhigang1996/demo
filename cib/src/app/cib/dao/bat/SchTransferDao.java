/**
 * @author hjs
 * 2006-11-28
 */
package app.cib.dao.bat;

import java.util.ArrayList;
import java.util.List;

import app.cib.util.Constants;

import com.neturbo.set.database.GenericHibernateDao;

/**
 * @author hjs 2006-11-28
 */
public class SchTransferDao extends GenericHibernateDao {

	public List listSchTransfer(String beneficiaryType, String corpId) {
		// hjs
		String hql = "from ScheduleTransfer as sch where sch.beneficiaryType = ? and sch.corporaitonId = ? and sch.status != ? ";
		List valueList = new ArrayList();
		valueList.add(beneficiaryType);
		valueList.add(corpId);
		valueList.add(Constants.STATUS_REMOVED);
		return this.list(hql, valueList.toArray());

	}

}
