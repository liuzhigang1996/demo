/**
 * @author hjs
 * 2006-12-18
 */
package app.cib.dao.bat;

import java.util.ArrayList;
import java.util.List;

import app.cib.bo.bat.ScheduleTransferHis;

import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2006-12-18
 */
public class SchTxnHisDao extends GenericHibernateDao {
	
	public List listRecords(String beneficiaryType, String corpId, String userId) throws NTBException {
		String hql = "from ScheduleTransferHis as schHis where schHis.corporaitonId = ? ";

		List valueList = new ArrayList();
		valueList.add(corpId.trim());
		if(!beneficiaryType.trim().equals("")) {
			hql += " and schHis.beneficiaryType = ? ";
			valueList.add(beneficiaryType.trim());
		}
		if(!userId.trim().equals("")) {
			hql += " and schHis.userId = ? ";
			valueList.add(userId.trim());
		}
		
		hql += " order by schHis.seqNo desc";
		
		return this.list(hql, valueList.toArray());
		
	}
	
	public ScheduleTransferHis getHisByScheduleId(String scheduleId) throws NTBException {
		String hql = "from ScheduleTransferHis as his where his.scheduleId = ? order by his.seqNo desc";
		List list = this.list(hql, new Object[]{scheduleId});
		if (list.size()>0) {
			return (ScheduleTransferHis) list.get(0);
		}
		return null;
	}

}
