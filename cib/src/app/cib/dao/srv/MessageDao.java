package app.cib.dao.srv;

import java.util.List;
import app.cib.util.Constants;
import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;

/**
 * @author panwen
 * 
 */
public class MessageDao extends GenericHibernateDao {

	public List findByBank(int fetchSize) throws NTBException {
		try {
			return getHibernateTemplate().find(FIND_BY_BANK);
		} catch (Exception e) {
			throw new NTBException("err.sys.DBError");
		}
	}

	private static final String FIND_BY_BANK = "from Message where status <> '"
			+ Constants.STATUS_REMOVED + "' order by Message_Id desc";

}
