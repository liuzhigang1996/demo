/**
 * @author hjs
 * 2006-11-29
 */
package app.cib.service.rpt;

import java.util.List;

import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2006-11-29
 */
public interface ApproverSuspendReportService {
	
	public List listReport(String corpId) throws NTBException;

}
