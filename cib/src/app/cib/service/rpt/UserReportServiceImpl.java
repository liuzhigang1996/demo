/**
 * @author hjs
 * 2006-11-9
 */
package app.cib.service.rpt;

import java.util.List;

import app.cib.dao.rpt.UserReportDao;

import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2006-11-9
 */
public class UserReportServiceImpl implements UserReportService {
	
	private UserReportDao userReportDao;

	public UserReportDao getUserReportDao() {
		return userReportDao;
	}

	public void setUserReportDao(UserReportDao userReportDao) {
		this.userReportDao = userReportDao;
	}

	public List listActivityLogReport(String corpId, String userId,
			String dateFrom, String dateTo)
			throws NTBException {
		
		List reportList = userReportDao.listActivityLogReport(corpId, userId, dateFrom, dateTo);
		
		return reportList;
	}

}
