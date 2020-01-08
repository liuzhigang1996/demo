package app.cib.dao.flow;

import java.util.List;
import app.cib.bo.flow.FlowProcess;

import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;

public class FlowProcessDao extends GenericHibernateDao {

	// Modified by Na Bai 2013-12-31
	// 为优化流程查询新增 loadByProcIds，用in语句将多个查询合并成一个
	// 该方法可能暂时不需要使用，调用该方法的getProgress被废弃
	public List loadByProcIds(String[] procIds) throws NTBException {
		List processes = null;
		StringBuffer procIdsStr = new StringBuffer("");
		for (int i = 0; i < procIds.length; i++) {
			if (i > 0) {
				procIdsStr.append(",");
			}
			procIdsStr.append("'");
			procIdsStr.append(procIds[i]);
			procIdsStr.append("'");
		}

		String LOAD_BY_PROCIDS = "from FlowProcess as flowProcess where "
				+ "flowProcess.procId in (" + procIdsStr.toString()
				+ ") order by flowProcess.procCreateTime desc ";
		try {
			processes = getHibernateTemplate().find(LOAD_BY_PROCIDS,
					new Object[] {});
		} catch (Exception e) {
			Log.error("Error finding procs by txn", e);
			throw new NTBException("err.sys.DBError");
		}

		return processes;
	}

	public FlowProcess loadByTrans(String txnType, String transNo)
			throws NTBException {
		List processes = null;
		FlowProcess flowProcess = null;
		try {
			processes = getHibernateTemplate().find(LOAD_BY_TRANS,
					new Object[] { txnType, transNo });
		} catch (Exception e) {
			Log.error("Error finding procs by txn", e);
			throw new NTBException("err.sys.DBError");
		}

		if (null != processes && processes.size() > 0) {
			if (processes.size() != 1) {
				Log.error("Error processing "
						+ "app.cib.dao.flow.FlowProcessDao."
						+ "loadByTrans(String,String) cause result count is "
						+ processes.size() + ", TXN_TYPE=" + txnType
						+ ", TRANS_NO=" + transNo);
			}
			flowProcess = (FlowProcess) processes.get(0);

		}

		return flowProcess;
	}

	private static final String LOAD_BY_TRANS = "from FlowProcess as flowProcess where "
			+ "flowProcess.txnType =? and flowProcess.transNo = ? order by flowProcess.procCreateTime desc ";

	private static final String LOAD_BY_PROCIDS = "from FlowProcess as flowProcess where "
			+ "flowProcess.procId order by flowProcess.procCreateTime desc ";

}
