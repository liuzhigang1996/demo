/**
 * @author hjs
 * 2006-11-29
 */
package app.cib.dao.rpt;

import java.util.List;

import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2006-11-29
 */
public class ApproverSuspendReportDao extends GenericHibernateDao {
	
	public List listReport(String corpId) throws NTBException {
		String hql = "from ApproverSuspendReport as rpt where rpt.corpId = ? order by rpt.userId, rpt.transType, rpt.operationType";
		List list = this.list(hql, new Object[]{corpId});
		
		return list;
		
	}

}
