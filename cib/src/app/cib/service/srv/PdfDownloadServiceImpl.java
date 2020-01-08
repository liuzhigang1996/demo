/**
 * @author hjs
 * 2006-11-8
 */
package app.cib.service.srv;

import java.util.List;
import java.util.Locale;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.RBFactory;

import app.cib.dao.srv.PdfDownloadDao;
import app.cib.util.StatementToken;

/**
 * @author hjs
 * 2006-11-8
 */
public class PdfDownloadServiceImpl implements PdfDownloadService {

	private final static String linkTemplate = "<a href=\"/cib/DownloadStatement?accNo=%1&key=%2\">%3</a>";
	
	private PdfDownloadDao pdfDownloadDao;

	public PdfDownloadDao getPdfDownloadDao() {
		return pdfDownloadDao;
	}

	public void setPdfDownloadDao(PdfDownloadDao pdfDownloadDao) {
		this.pdfDownloadDao = pdfDownloadDao;
	}

	public List listPdfFile(String corpId, String category,
			String dateFrom, String dateTo) throws NTBException{
		
		List fileList = pdfDownloadDao.listPdfFile(corpId, category, dateFrom, dateTo);
		return fileList;
	}

	public String getHyperLink(NTBUser user, String category, String fileName) throws NTBException {
		
		Locale locale = (user.getLanguage()==null) ? Config.getDefaultLocale() : user.getLanguage();
		RBFactory rbFactory = RBFactory.getInstance("app.cib.resource.enq.statement_Status", locale.toString());
		
		if (!fileName.equals("")) {
			String tmpLink = linkTemplate.replaceAll("%1", "") ;
			tmpLink =  tmpLink.replaceAll("%2", "P-" + StatementToken.getToken(fileName)) ;
			tmpLink =  tmpLink.replaceAll("%3", fileName/*rbFactory.getString("view")*/) ;
			return tmpLink;
		} else {
			return rbFactory.getString("notApp");
		}
	}

}
