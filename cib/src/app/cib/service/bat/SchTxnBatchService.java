/**
 * @author hjs
 * 2006-10-30
 */
package app.cib.service.bat;

import java.util.List;

import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2006-10-30
 */
public interface SchTxnBatchService {
	public List listNextBatchDayReport(String corpId, String nextBatchDay) throws NTBException;
	public void cancel(String batchId) throws NTBException ;
	public void cancelByCorpId(String corpId) throws NTBException;
	public void cancelByUserId(String userId) throws NTBException;
	public List listSchBatchHistory(String beneficiaryType, String corpId, String userId, 
			String dateFrom, String dateTo) throws NTBException;
}
