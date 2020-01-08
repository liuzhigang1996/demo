/**
 * @author hjs
 * 2006-11-8
 */
package app.cib.service.srv;

import java.util.List;

import com.neturbo.set.core.NTBUser;
import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2006-11-8
 */
public interface PdfDownloadService {
	public List listPdfFile(String corpId, String category, String dateFrom, String dateTo) throws NTBException ;
	public String getHyperLink(NTBUser user, String category, String fileName) throws NTBException ;
}
