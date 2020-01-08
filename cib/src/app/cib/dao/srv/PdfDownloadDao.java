/**
 * @author hjs
 * 2006-11-8
 */
package app.cib.dao.srv;

import java.util.ArrayList;
import java.util.List;

import com.neturbo.set.core.Config;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 2006-11-8
 */
public class PdfDownloadDao extends GenericJdbcDao {

	private static final String defalutPattern = Config.getProperty("DefaultDatePattern");
	
	public List listPdfFile(String corpId, String category, String dateFrom, String dateTo) throws NTBException {

		String sql = "select * from HS_PDF_INDEX where CORP_ID = ? ";
		List valueList = new ArrayList();

		valueList.add(corpId);

		if ((!Utils.null2EmptyWithTrim(category).equals("")) && (!category.equals("0"))) {
			sql += "and CATEGORY_CODE = ? ";
			valueList.add(category);
		}
		if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
			dateFrom = DateTime.formatDate(dateFrom, defalutPattern, "yyyyMMdd");
			//Timestamp timeFrom = DateTime.getTimestampByStr(dateFrom, true);
			sql += "and GENERATION_DATE >= ? ";
			valueList.add(dateFrom);
		}
		if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
			dateTo = DateTime.formatDate(dateTo, defalutPattern, "yyyyMMdd");
			//Timestamp timeTo = DateTime.getTimestampByStr(dateTo, false);
			sql += "and GENERATION_DATE <= ? ";
			valueList.add(dateTo);
		}

		sql += "order by GENERATION_DATE desc";

		try {
			return this.query(sql, valueList.toArray());
		} catch (Exception e) {
			throw new NTBException();
		}
		
	}
	
}
