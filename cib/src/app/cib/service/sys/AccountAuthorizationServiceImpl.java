package app.cib.service.sys;

import java.util.*;

import app.cib.bo.sys.*;
import app.cib.dao.sys.*;
import app.cib.util.*;

import com.neturbo.set.core.*;
import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.*;
import app.cib.core.CibIdGenerator;


public class AccountAuthorizationServiceImpl implements AccountAuthorizationService {
	private AccountAuthorizationDao accountAuthorizationDao;
	private GenericHibernateDao genericHibernateDao;

	public AccountAuthorizationDao getAccountAuthorizationDao() {
		return accountAuthorizationDao;
	}

	public void setAccountAuthorizationDao(AccountAuthorizationDao accountAuthorizationDao) {
		this.accountAuthorizationDao = accountAuthorizationDao;
	}

	public GenericHibernateDao getGenericHibernateDao() {
		return genericHibernateDao;
	}

	public void setGenericHibernateDao(GenericHibernateDao genericHibernateDao) {
		this.genericHibernateDao = genericHibernateDao;
	}

    public List listAll(String corpId) throws NTBException{
    	List resList = null;
		try {
			resList = accountAuthorizationDao.listAll(corpId);
		} catch (Exception e) {
			Log.error("Error list all account authorization", e);
			throw new NTBException("err.sys.DBError");
		}    	
		return resList;
    }

	public List listByAccount(String corpId, String account) throws NTBException {
    	List resList = null;
		try {
			resList = accountAuthorizationDao.listByAccount(corpId,account);
		} catch (Exception e) {
			Log.error("Error list account authorization by account", e);
			throw new NTBException("err.sys.DBError");
		}    	
		return resList;
	}

	public List listByUser(String corpId, String authUser) throws NTBException {
    	List resList = null;
		try {
			resList = accountAuthorizationDao.listByUser(corpId,authUser);
		} catch (Exception e) {
			Log.error("Error list account authorization by user", e);
			throw new NTBException("err.sys.DBError");
		}    	
		return resList;
	}

	public List listByAccountUser(String corpId, String account, String authUser) throws NTBException {
    	List resList = null;
		try {
			resList = accountAuthorizationDao.listByAccountUser(corpId, account, authUser);
		} catch (Exception e) {
			Log.error("Error list account authorization by account and user", e);
			throw new NTBException("err.sys.DBError");
		}    	
		return resList;
	}
	
	public List listByAccountUserHistory(String corpId, String account, String authUser) throws NTBException {
    	List resList = null;
		try {
			resList = accountAuthorizationDao.listByAccountUserHistory(corpId, account, authUser);
		} catch (Exception e) {
			Log.error("Error list account authorization history by account and user", e);
			throw new NTBException("err.sys.DBError");
		}    	
		return resList;
	}
	
    public void addAccountAuthorizationHis(Map filedValueMap) throws NTBException{
		try {
			accountAuthorizationDao.addAccountAuthorizationHis(filedValueMap);
		} catch (Exception e) {
			Log.error("Error insert account authorization history by account and user", e);
			throw new NTBException("err.sys.DBError");
		}    	
    	
    }

    public List loadHisBySeqNo(String id) throws NTBException{
    	List resList = null;
		try {
			resList = accountAuthorizationDao.loadHisBySeqNo(id);
		} catch (Exception e) {
			Log.error("Error list account authorization history by account and user", e);
			throw new NTBException("err.sys.DBError");
		}    	
		return resList;
    }

    public void updateAccountAuthorizationHis(Map columnMap, Map conditionMap) throws NTBException{
		try {
			accountAuthorizationDao.updateAccountAuthorizationHis(columnMap, conditionMap);
		} catch (Exception e) {
			Log.error("Error update account authorization history by account and user", e);
			throw new NTBException("err.sys.DBError");
		}    	
    	
    }
	
    public void addAccountAuthorization(Map filedValueMap) throws NTBException{
		try {
			accountAuthorizationDao.addAccountAuthorization(filedValueMap);
		} catch (Exception e) {
			Log.error("Error insert account authorization history by account and user", e);
			throw new NTBException("err.sys.DBError");
		}    	
    	
    }

    public void updateAccountAuthorization(Map columnMap, Map conditionMap) throws NTBException{
		try {
			accountAuthorizationDao.updateAccountAuthorization(columnMap, conditionMap);
		} catch (Exception e) {
			Log.error("Error update account authorization by account and user", e);
			throw new NTBException("err.sys.DBError");
		}    	
    	
    }
	
    public void deleteAccountAuthorization(String recordId) throws NTBException{
		try {
			accountAuthorizationDao.deleteAccountAuthorization(recordId);
		} catch (Exception e) {
			Log.error("Error delete account authorization by account and user", e);
			throw new NTBException("err.sys.DBError");
		}    	
    	
    }
}
