/**
 * @author hjs
 * 2006-8-3
 */
package app.cib.dao.bnk;

import java.util.List;

import org.springframework.dao.DataAccessResourceFailureException;

import net.sf.hibernate.HibernateException;
import app.cib.bo.bnk.TxnLimit;

import com.neturbo.set.database.GenericHibernateDao;

/**
 * @author hjs 2006-8-3
 */
public class TxnLimitDao extends GenericHibernateDao {
	private static final String FIND_BY_CORP = "from TxnLimit  txnLimit where txnLimit.status='0' and txnLimit.operation<>'B' "
			+ "and txnLimit.corpId = ? and txnLimit.account =? "
			+ "and (exists (from CorpPreference corpPreference "
			+ "where corpPreference.status='0' and corpPreference.authStatus='0' "
			+ "and corpPreference.prefId = txnLimit.prefId))"
			+ "order by txnLimit.limitId";

	public TxnLimit findByCorp(String corpId, String account)
			throws DataAccessResourceFailureException, HibernateException,
			IllegalStateException {
		TxnLimit txnLimit = null;

		List aList = getHibernateTemplate().find(FIND_BY_CORP,
				new Object[] { corpId, account });

		if (null != aList && aList.size() > 0) {
			txnLimit = (TxnLimit) aList.get(0);
		}

		return txnLimit;
	}

}
