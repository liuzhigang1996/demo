package app.cib.dao.flow;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import app.cib.service.flow.FlowEngineService;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

public class FlowProcessHisDao extends GenericHibernateDao {

	private static final String defalutPattern = Config.getProperty("DefaultDatePattern");

	public List loadByProcIds(String[] procIds) throws NTBException {
		List processes = null;
		StringBuffer procIdsStr = new StringBuffer("");
		for (int i = 0; i < procIds.length; i++) {
			if (i > 0) {
				procIdsStr.append(",");
			}
			procIdsStr.append("'");
			procIdsStr.append(procIds);
			procIdsStr.append("'");
		}

		String LOAD_BY_PROCIDS = "from flowProcessHis as flowProcessHis where "
				+ "flowProcessHis.procId in (" + procIdsStr.toString()
				+ ") order by flowProcessHis.procId ";
		try {
			processes = getHibernateTemplate().find(LOAD_BY_PROCIDS,
					new Object[] {});
		} catch (Exception e) {
			Log.error("Error finding procs by txn", e);
			throw new NTBException("err.sys.DBError");
		}

		return processes;
	}
	
	/**
	 * add by hjs
	 * @param corpId
	 * @param userId
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 * @throws NTBException
	 */
	public List listProcessHis(String corpId, String userId, String dateFrom, String dateTo) throws NTBException {

		String hql = "from FlowProcess as t where t.corpId = ? and t.procStatus not in (?,?,?) ";
		List valueList = new ArrayList();

		valueList.add(corpId);
		valueList.add(FlowEngineService.PROCESS_STATUS_REJECT);
		valueList.add(FlowEngineService.PROCESS_STATUS_CANCEL);
		valueList.add(FlowEngineService.PROCESS_STATUS_FINISH);

		if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
			dateFrom = DateTime.formatDate(dateFrom, defalutPattern, "yyyy-MM-dd");
			Timestamp timeFrom = DateTime.getTimestampByStr(dateFrom, true);
			hql += "and t.procCreateTime >= ? ";
			valueList.add(timeFrom);
		}
		if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
			dateTo = DateTime.formatDate(dateTo, defalutPattern, "yyyy-MM-dd");
			Timestamp timeTo = DateTime.getTimestampByStr(dateTo, false);
			hql += "and t.procCreateTime <= ? ";
			valueList.add(timeTo);
		}
		if (!Utils.null2EmptyWithTrim(userId).equals("")) {
			hql += "and t.procCreator = ? ";
			valueList.add(userId);
		}

		return this.list(hql, valueList.toArray());

	}

}
