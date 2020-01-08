/**
 * @author hjs
 * 2006-11-29
 */
package app.cib.service.rpt;

import java.util.List;

import app.cib.dao.rpt.ApproverSuspendReportDao;

import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2006-11-29
 */
public class ApproverSuspendReportServiceImpl implements
		ApproverSuspendReportService {
	
	private ApproverSuspendReportDao reportDao;

	public ApproverSuspendReportDao getReportDao() {
		return reportDao;
	}

	public void setReportDao(ApproverSuspendReportDao reportDao) {
		this.reportDao = reportDao;
	}

	public List listReport(String corpId) throws NTBException {
		return reportDao.listReport(corpId);
	}

}
