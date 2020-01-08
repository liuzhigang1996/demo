package app.cib.dao.sys;

import java.util.List;

import org.springframework.dao.DataAccessResourceFailureException;

import net.sf.hibernate.HibernateException;
import app.cib.bo.sys.ApproveRule;
import app.cib.util.Constants;

import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;

public class ApproveRuleDao extends GenericHibernateDao {
	private static final String GET_APPROVE_RULE = "from ApproveRule as approveRule where "
			+ "(approveRule.corpId = ? or approveRule.corpId is null) "
			+ "and (approveRule.txnType = ? or approveRule.txnType='ALL') and (approveRule.currency = ? or approveRule.currency is null) "
			+ " and ( approveRule.ruleType = '"
			+ Constants.RULE_TYPE_SINGLE
			+ "' or ( (approveRule.fromAmount is null or approveRule.fromAmount <= ?) and "
			+ "(approveRule.toAmount is null or approveRule.toAmount > ?) )) "
			+ "and approveRule.status ='" + Constants.STATUS_NORMAL + "' ";

	public ApproveRule getApproveRule(String txnType, String corpId,
			String currency, double amount) throws NTBException {
		Object[] objArray = new Object[] { corpId, txnType, currency,
				new Double(amount), new Double(amount) };
		List list = getHibernateTemplate().find(GET_APPROVE_RULE, objArray);

		ApproveRule aRule = null;
		if (null != list && list.size() > 0) {
			aRule = (ApproveRule) list.get(0);

			if (list.size() > 1) {
				Log.error("Error getting approve rule, result count is"
						+ list.size() + "(1 is Normal)");
			}
		}
		return aRule;
	}

	public List getApproveRule(ApproveRule approveRuleObj)
			throws DataAccessResourceFailureException, HibernateException,
			IllegalStateException {
		String sql = " from ApproveRule as rule where rule.corpId = ? and rule.txnType = ? and rule.currency = ? and rule.status = '0' order by rule.fromAmount ";
		Object[] objArray = new Object[] { approveRuleObj.getCorpId(),
				approveRuleObj.getTxnType(), approveRuleObj.getCurrency() };
		List list = getHibernateTemplate().find(sql, objArray);

		return list;
	}
	
	// Jet added 2008-01-30
	public List getAll(ApproveRule approveRuleObj)
			throws DataAccessResourceFailureException, HibernateException,
			IllegalStateException {
		String sql = " from ApproveRule as rule where rule.corpId = ? and rule.status = '0' ";
		Object[] objArray = new Object[] { approveRuleObj.getCorpId() };
		List list = getHibernateTemplate().find(sql, objArray);

		return list;
	}

	public List getAll(String corpId)
			throws DataAccessResourceFailureException, HibernateException,
			IllegalStateException {
		String sql = " from ApproveRule as rule where rule.corpId = ? and rule.status = '0' ";
		Object[] objArray = new Object[] { corpId };
		List list = getHibernateTemplate().find(sql, objArray);

		return list;
	}

	
	public List getAllPending(String corpId)
			throws DataAccessResourceFailureException, HibernateException,
			IllegalStateException {
		String sql = " from ApproveRule as rule where rule.corpId = ? and rule.txnType ='ALL' and rule.status = '1' ";
		Object[] objArray = new Object[] { corpId };
		List list = getHibernateTemplate().find(sql, objArray);

		return list;
	}


	public List getApproveRuleByPref(String prefId)
			throws DataAccessResourceFailureException, HibernateException,
			IllegalStateException {
		String sql = " from ApproveRule as rule where rule.prefId = ? and rule.status <> ?";
		Object[] objArray = new Object[] { prefId, Constants.STATUS_UPDATE_BEFORE};
		List list = getHibernateTemplate().find(sql, objArray);

		return list;
	}

        public List getAllApproveRuleByPref(String prefId)
                        throws DataAccessResourceFailureException, HibernateException,
                        IllegalStateException {
                String sql = " from ApproveRule as rule where rule.prefId = ?";
                Object[] objArray = new Object[] { prefId};
                List list = getHibernateTemplate().find(sql, objArray);

                return list;
        }
}
