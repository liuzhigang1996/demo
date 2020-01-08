/**
 * @author hjs
 * 2006-11-9
 */
package app.cib.service.rpt;

import java.util.List;

import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2006-11-9
 */
public interface UserReportService {
	
	public List listActivityLogReport(String corpId, String userId, String dateFrom, String dateTo) throws NTBException;

}
