package app.cib.dao.sys;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;

import org.springframework.dao.DataAccessResourceFailureException;

import app.cib.bo.sys.CorpUser;
import app.cib.util.Constants;

import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class CorpUserDao extends GenericHibernateDao {
	public CorpUserDao() {
	}

	public List listByCorp(String corpId)
			throws DataAccessResourceFailureException, HibernateException,
			IllegalStateException {
		String hql = "from CorpUser as corpUser where corpUser.status != ? and  corpUser.id.corpId = ? order by corpUser.roleId,corpUser.id.userId";
		
		return this.list(hql, new Object[] { Constants.STATUS_REMOVED, corpId });

	}

	public CorpUser loadByUserIdAndCoprId(String userId, String corpId)
			throws NTBException {
		CorpUser userObj = null;
		try{
		String hql = "from CorpUser as corpUser where corpUser.id.userId = ? and  corpUser.id.corpId = ? ";
		List list = this.list(hql, new Object[] { userId, corpId });
		if(list != null && list.size()>0){
			userObj = (CorpUser)list.get(0);
		}
		}catch(Exception e){
			Log.error("loadByUserIdAndCoprId error", e);
			throw new NTBException("err.sys.GeneralError");
		}
		return userObj;
	}

	public List listNormalByCorp(String corpId)
			throws DataAccessResourceFailureException, HibernateException,
			IllegalStateException {
		String hql = "from CorpUser as corpUser where corpUser.status = ? and  corpUser.id.corpId = ?";
		
		
		return this.list(hql, new Object[] { Constants.STATUS_NORMAL, corpId });
		
	}

	public List listByRoleId(String corpId, String roleId, String status)
			throws NTBException {
		String hql = "from CorpUser as corpUser where corpUser.id.corpId = ? and corpUser.roleId = ?";
		List dataList = new ArrayList();
		dataList.add(corpId);
		dataList.add(roleId);

		if (!Utils.null2EmptyWithTrim(status).equals("")) {
			hql += " and corpUser.status = ?";
			dataList.add(status);
		}
		
		
		
		return list(hql, dataList.toArray());
	}

	public List listByCorpLevel(String corpId, String level)
			throws DataAccessResourceFailureException, HibernateException,
			IllegalStateException {

		Object[] objArray = new Object[] { corpId, level,
				Constants.STATUS_NORMAL, Constants.ROLE_APPROVER };
		List list = getHibernateTemplate().find(LIST_BY_CORP_LEVEL, objArray);

		return list ;
		
	}

	private static final String LIST_BY_CORP_LEVEL = "from CorpUser as corpUser "
			+ "where corpUser.id.corpId= ? and corpUser.authLevel is not null "
			+ "and corpUser.authLevel <= ? "// "and corpUser.authLevel <> '' and corpUser.authLevel <= ? "
			+ "and corpUser.status=? and corpUser.roleId = ? "
			+ "order by corpUser.authLevel,corpUser.id.userId";

}
