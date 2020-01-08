/**
 * @author hjs
 * 2006-8-3
 */
package app.cib.dao.bnk;

import java.util.List;

import org.springframework.dao.DataAccessResourceFailureException;

import net.sf.hibernate.HibernateException;
import app.cib.bo.bnk.SetCurrency;
import app.cib.bo.bnk.SetCurrencyHis;
import app.cib.util.Constants;

import com.neturbo.set.database.GenericHibernateDao;

/**
 * @author hjs 2006-8-3
 */
public class SetCurrencyDao extends GenericHibernateDao {
	private static final String FIND_CCY = "from SetCurrency";
	private static final String FIND_CCY_HIS_PENDDING = "from SetCurrencyHis setCurrencyHis where setCurrencyHis.status='"+ Constants.STATUS_PENDING_APPROVAL+"'";
	private static final String FIND_CCY_HIS_NORMAL = "from SetCurrencyHis setCurrencyHis where setCurrencyHis.status='"+ Constants.STATUS_NORMAL+"'";

	public List listManageCcy() throws DataAccessResourceFailureException, HibernateException, IllegalStateException {
		List aList = getHibernateTemplate().find(FIND_CCY,new Object[] {});
		return aList;
	}
	
	public List listManageCcyPending() throws DataAccessResourceFailureException, HibernateException, IllegalStateException {
		List aList = getHibernateTemplate().find(FIND_CCY_HIS_PENDDING,new Object[] {});
		return aList;
	}
	
	public List listManageCcyNormal() throws DataAccessResourceFailureException, HibernateException, IllegalStateException {
		List aList = getHibernateTemplate().find(FIND_CCY_HIS_NORMAL,new Object[] {});
		return aList;
	}
	/*public SetCurrency getCurrency(String ccyCode) throws DataAccessResourceFailureException, HibernateException, IllegalStateException {
		List aList = getHibernateTemplate().find(FIND_CCY_HIS_NORMAL,new Object[] {});
		return aList;
	}
	public SetCurrencyHis getCurrencyHis(String ccyCode) throws DataAccessResourceFailureException, HibernateException, IllegalStateException {
		List aList = getHibernateTemplate().find(FIND_CCY_HIS_NORMAL,new Object[] {});
		return aList;
	}*/

}
