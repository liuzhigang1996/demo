/**
 * @author hjs
 * 2007-4-27
 */
package app.cib.dao.sys;

import java.util.*;

import app.cib.util.Constants;

import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2007-4-27
 */
public class AccountAuthorizationDao extends GenericJdbcDao {
	public List listAll(String corpId) throws NTBException {
		String sql = "select * from ACCOUNT_AUTHORIZATION where CORP_ID = ? and STATUS = ? order by ACCOUNT_NO";
		
		try {
			return this.query(sql, new Object[]{corpId, Constants.STATUS_NORMAL});
		} catch (Exception e) {
			Log.error("query account authroization list error", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	public List listByAccount(String corpId, String account) throws NTBException {
		String sql = "select * from ACCOUNT_AUTHORIZATION where CORP_ID = ? and ACCOUNT_NO = ? and STATUS = ? order by ACCOUNT_NO";
		
		try {
			return this.query(sql, new Object[]{corpId, account, Constants.STATUS_NORMAL});
		} catch (Exception e) {
			Log.error("query account authroization list error", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	public List listByUser(String corpId, String authUser) throws NTBException {
		String sql = "select * from ACCOUNT_AUTHORIZATION where CORP_ID = ? and STATUS = ? and AUTH_USER like '%" + authUser + "%' order by AUTH_USER";
		
		try {
			return this.query(sql, new Object[]{corpId, Constants.STATUS_NORMAL});
		} catch (Exception e) {
			Log.error("query account authroization list error", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	public List listByAccountUser(String corpId, String account, String authUser) throws NTBException {
		String sql = "select * from ACCOUNT_AUTHORIZATION where CORP_ID = ? and ACCOUNT_NO = ? and AUTH_USER = ?";
		
		try {
			return this.query(sql, new Object[]{corpId, account, authUser});
		} catch (Exception e) {
			Log.error("query account authroization list error", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	public List listByAccountUserHistory(String corpId, String account, String authUser) throws NTBException {
		String sql = "select * from ACCOUNT_AUTHORIZATION where CORP_ID = ? and ACCOUNT_NO = ? and AUTH_USER = ? and STATUS = ?";
		
		try {
			return this.query(sql, new Object[]{corpId, account, authUser, Constants.STATUS_PENDING_APPROVAL});
		} catch (Exception e) {
			Log.error("query account authroization history list error", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	
	public void addAccountAuthorizationHis(Map filedValueMap) throws NTBException {
		try{
			this.add("ACCOUNT_AUTHORIZATION_HIS", filedValueMap);		
		} catch (Exception e) {
			Log.error("insert account authroization history error", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	public List loadHisBySeqNo(String id) throws NTBException {
		String sql = "select * from ACCOUNT_AUTHORIZATION_HIS where SEQ_NO = ?";
		
		try {
			return this.query(sql, new Object[]{id});
		} catch (Exception e) {
			Log.error("query account authroization history list error", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	public void updateAccountAuthorizationHis(Map columnMap, Map conditionMap) throws NTBException {
		try{
			this.update("ACCOUNT_AUTHORIZATION_HIS", columnMap, conditionMap);		
		} catch (Exception e) {
			Log.error("update account authroization history error", e);
			throw new NTBException("err.sys.DBError");
		}
	}

	public void addAccountAuthorization(Map filedValueMap) throws NTBException {
		try{
			this.add("ACCOUNT_AUTHORIZATION", filedValueMap);		
		} catch (Exception e) {
			Log.error("insert account authroization history error", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	
	public void updateAccountAuthorization(Map columnMap, Map conditionMap) throws NTBException {
		try{
			this.update("ACCOUNT_AUTHORIZATION", columnMap, conditionMap);		
		} catch (Exception e) {
			Log.error("update account authroization error", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	
	public void deleteAccountAuthorization(String recordId) throws NTBException {
		String sql = "delete from ACCOUNT_AUTHORIZATION where RECORD_ID = ?";
		
		try {
			this.update(sql, new Object[]{recordId});
		} catch (Exception e) {
			Log.error("delete account authroization error", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	
}
