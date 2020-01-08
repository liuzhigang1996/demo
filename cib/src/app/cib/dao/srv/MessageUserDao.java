package app.cib.dao.srv;

import java.util.Date;
import java.util.List;

import net.sf.hibernate.type.DateType;
import net.sf.hibernate.type.StringType;
import net.sf.hibernate.type.Type;
import app.cib.util.Constants;

import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;

public class MessageUserDao extends GenericHibernateDao {
	public List findByUser(String userId) throws NTBException {
		Date curDate = new Date();
		try {
			StringType stringType = new StringType();
			DateType dateType = new DateType();
			return getHibernateTemplate().find(
					FIND_BY_USER,
					new Object[] { userId, Constants.STATUS_REMOVED,
							Constants.STATUS_REMOVED, curDate, curDate }
					//,
					//new Type[] { stringType, stringType, stringType, dateType,
					//		dateType }
					);

		} catch (Exception e) {
			throw new NTBException("err.sys.DBError");
		}
	}

	private static final String FIND_BY_USER = "from MessageUser as messageUser "
			+ "where messageUser.userId = ? and messageUser.muStatus <> ? and messageUser.message.status <> ? "
			+ "and messageUser.message.fromDate <= ? and messageUser.message.toDate >= ? ";

}
