/**
 * @author hjs
 * 2006-10-14
 */
package app.cib.dao.bat;

import java.util.List;

import app.cib.util.Constants;

import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2006-10-14
 */
public class PayrollRecDao extends GenericHibernateDao {
	
	public List listUploadErrorDetailRec(String payrollId) throws NTBException {
		String hql = "from PayrollRec as pr where pr.payrollId = ? and pr.status = ?";
		hql += " and (pr.detailResult = ? or pr.detailResult = ? or pr.detailResult = ? or pr.detailResult = ? or pr.detailResult = ?)";//mod by linrui 20180307
//		hql += " and (pr.detailResult = ? or pr.detailResult = ? or pr.detailResult = ?)";
		
		return list(hql, new Object[]{payrollId, Constants.STATUS_NORMAL, "1", "2", "3", "E", "O"});//mod by linrui 20180307
//		return list(hql, new Object[]{payrollId, Constants.STATUS_NORMAL, "1", "2", "3", });
	}

}
