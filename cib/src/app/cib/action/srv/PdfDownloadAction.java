/**
 * @author hjs
 * 2006-11-8
 */
package app.cib.action.srv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.service.srv.PdfDownloadService;

/**
 * @author hjs
 * 2006-11-8
 */
public class PdfDownloadAction extends CibAction {
	
	public void listFileLoad() throws NTBException {
		// 设置空的 ResultData 清空显示数据
		setResultData(new HashMap(1));
	}
	
	public void listFile() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		PdfDownloadService pdfDownloadService = (PdfDownloadService) appContext.getBean("PdfDownloadService");

		CorpUser corpUser = (CorpUser) this.getUser();
		
		String date_range = Utils.null2EmptyWithTrim(this.getParameter("date_range"));
		String dateFrom = Utils.null2EmptyWithTrim(this.getParameter("dateFrom"));
		String dateTo = Utils.null2EmptyWithTrim(this.getParameter("dateTo"));
		String category = Utils.null2EmptyWithTrim(this.getParameter("category"));

		List fileList = pdfDownloadService.listPdfFile(corpUser.getCorpId(), category, dateFrom, dateTo);
		//fileList = this.convertPojoList2MapList(fileList);

		Map row = null;
		for (int i=0; i<fileList.size(); i++) {
			row = (Map) fileList.get(i);
			row.put("pdfFileLink", pdfDownloadService.getHyperLink(corpUser, row.get("CATEGORY_CODE").toString(), row.get("PDF_FILENAME").toString()));
		}

		Map resultData = new HashMap();
		resultData.put("date_range", date_range);
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("category", category);
		resultData.put("fileList", fileList);
		setResultData(resultData);
		
	}

}
