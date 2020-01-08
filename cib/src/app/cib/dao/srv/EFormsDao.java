/**
 * @author hjs
 * 2007-4-24
 */
package app.cib.dao.srv;

import java.util.ArrayList;
import java.util.List;

import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 2007-4-24
 */
public class EFormsDao extends GenericJdbcDao {

	public List listRecords(String corpId, String typeOfForm) throws NTBException {


		String sql = "select * from HS_E_FORMS where CORP_ID = ? ";
		List valueList = new ArrayList();

		valueList.add(corpId);
		
		if ((!Utils.null2EmptyWithTrim(typeOfForm).equals("")) && (!typeOfForm.equals("0"))) {
			sql += "and TYPE_OF_FORM = ? ";
			valueList.add(typeOfForm);
		}

		sql += "order by TYPE_OF_FORM";


		try {
			return this.query(sql, valueList.toArray());
		} catch (Exception e) {
			Log.error("DB Error", e);
			throw new NTBException("err.sys.DBError");
		}
	}

}
