/**
 *
 */
package app.cib.service.sys;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import app.cib.bo.bat.ScheduleTransferHis;
import app.cib.bo.bnk.BankUserHis;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpAccountHis;
import app.cib.bo.sys.CorpPermission;
import app.cib.bo.sys.CorpPreference;
import app.cib.bo.sys.CorpUser;
import app.cib.dao.sys.CorpAccountDao;
import app.cib.dao.sys.CorpAccountHisDao;
import app.cib.dao.sys.CorpPreferenceDao;
import app.cib.util.Constants;
import app.cib.util.RcCurrency;
import app.cib.core.CibIdGenerator;
import app.cib.core.CibTransClient;

import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBPermission;
import app.cib.dao.sys.CorpPermissionDao;

/**
 * @author hjs 2006-07-25
 */
public class CorpAccountServiceImpl implements CorpAccountService {

	private CorpAccountDao corpAccountDao;

	private CorpAccountHisDao corpAccountHisDao;

	private CorpPreferenceDao corpPreferenceDao;

	private CorpPermissionDao corpPermissionDao;

	private GenericJdbcDao genericJdbcDao;

	public CorpAccountDao getCorpAccountDao() {
		return corpAccountDao;
	}

	public void setCorpAccountDao(CorpAccountDao corpAccountDao) {
		this.corpAccountDao = corpAccountDao;
	}

	public CorpAccountHisDao getCorpAccountHisDao() {
		return corpAccountHisDao;
	}

	public void setCorpAccountHisDao(CorpAccountHisDao corpAccountHisDao) {
		this.corpAccountHisDao = corpAccountHisDao;
	}

	public CorpPreferenceDao getCorpPreferenceDao() {
		return corpPreferenceDao;
	}

	public void setCorpPreferenceDao(CorpPreferenceDao corpPreferenceDao) {
		this.corpPreferenceDao = corpPreferenceDao;
	}

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public CorpPermissionDao getCorpPermissionDao() {
		return corpPermissionDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	public void setCorpPermissionDao(CorpPermissionDao corpPermissionDao) {
		this.corpPermissionDao = corpPermissionDao;
	}

	/*public CorpAccount viewCorpAccount(String accountNo) throws NTBException {
		return (CorpAccount) corpAccountDao.load(CorpAccount.class, accountNo);
	}*/
	//mod by linrui for mul-ccy temp
	public CorpAccount viewCorpAccount(String accountNo) throws NTBException {
		accountNo = Utils.null2EmptyWithTrim(accountNo);
		Map conditionMap = new HashMap();
		conditionMap.put("accountNo", accountNo);
		List corpList = this.corpAccountDao.list(CorpAccount.class,
				conditionMap);

		if (corpList.size() > 0) {
			return (CorpAccount) corpList.get(0);
		} else {
			throw new NTBException("err.sys.NoSuchAccountNoInLocalDB");
		}
	}
	//mod by linrui for mul-ccy temp2
	public CorpAccount viewCorpAccount(String accountNo, String ccy) throws NTBException {
		accountNo = Utils.null2EmptyWithTrim(accountNo);
		Map conditionMap = new HashMap();
		conditionMap.put("accountNo", accountNo);
		conditionMap.put("currency", ccy);
		List corpList = this.corpAccountDao.list(CorpAccount.class,
				conditionMap);
		
		if (corpList.size() > 0) {
			return (CorpAccount) corpList.get(0);
		} else {
			throw new NTBException("err.sys.NoSuchAccountNoInLocalDB");
		}
	}

	public String getAccountType(String account) throws NTBException {
		account = Utils.null2EmptyWithTrim(account);
		CorpAccount corpAccount =
			(CorpAccount) this.corpAccountDao.load(CorpAccount.class, account);
		if (corpAccount != null) {
			return corpAccount.getAccountType();
		} else {
			throw new NTBException("err.sys.NoSuchAccountNoInLocalDB");
		}
	}
	//add by linrui for mul-ccy 20181221
	public String getAccountType(String account, String ccy) throws NTBException {
		account = Utils.null2EmptyWithTrim(account);
		ccy = Utils.null2EmptyWithTrim(ccy);
		
		Map conditionMap = new HashMap();
        conditionMap.put("accountNo", account);
        conditionMap.put("currency", ccy);
        List corpList = this.corpAccountDao.list(CorpAccount.class, conditionMap);
		
        if(corpList.size()>0){
            CorpAccount corpAccount = (CorpAccount) corpList.get(0);
            return corpAccount.getAccountType();
		} else {
			throw new NTBException("err.sys.NoSuchAccountNoInLocalDB");
		}
	}

	public String getCurrency(String account) throws NTBException {
		account = Utils.null2EmptyWithTrim(account);
		CorpAccount corpAccount =
			(CorpAccount) this.corpAccountDao.load(CorpAccount.class, account);
		if (corpAccount != null) {
			return corpAccount.getCurrency();
		} else {
			throw new NTBException("err.sys.NoSuchAccountNoInLocalDB");
		}
	}
	//add by linrui for mul-ccy
	public String getCurrency(String account , boolean isMulCcy) throws NTBException {
		account = Utils.null2EmptyWithTrim(account);
		Map conditionMap = new HashMap();
        conditionMap.put("accountNo", account);
        List corpList = this.corpAccountDao.list(CorpAccount.class, conditionMap);
        if(corpList.size()>0){
            CorpAccount corpAccount = (CorpAccount) corpList.get(0);
            return corpAccount.getCurrency();
		} else {
			throw new NTBException("err.sys.NoSuchAccountNoInLocalDB");
		}
	}

	public String getAccountName(String account) throws NTBException {
		account = Utils.null2EmptyWithTrim(account);
		CorpAccount corpAccount =
			(CorpAccount) this.corpAccountDao.load(CorpAccount.class, account);
		if (corpAccount != null) {
			return corpAccount.getAccountName();
		} else {
			throw new NTBException("err.sys.NoSuchAccountNoInLocalDB");
		}
	}
	//ADD BY LINRUI FOR MUL-CCY
	public String getAccountName(String account, String ccy) throws NTBException {
		account = Utils.null2EmptyWithTrim(account);
		ccy = Utils.null2EmptyWithTrim(ccy);
		Map conditionMap = new HashMap();
        conditionMap.put("accountNo", account);
        conditionMap.put("currency", ccy);
        List corpList = this.corpAccountDao.list(CorpAccount.class, conditionMap);
		
		if(corpList.size()>0){
        CorpAccount corpAccount = (CorpAccount) corpList.get(0);
		return corpAccount.getAccountName();
		} else {
//			throw new NTBException("err.sys.NoSuchAccountNoInLocalDB");
			return "";
		}
	}

	public Map getHostAccListMap(String corpId, CorpUser corpUser)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpPreferenceService corpPrefService =
			(CorpPreferenceService) appContext.getBean("CorpPreferenceService");

		List accList = new ArrayList();
		String refNo = "";

		Map hostAccMap = new HashMap();
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		CibTransClient testClient = new CibTransClient("CIB", "ZC11");
		toHost.put("CORPORATE_ID", corpId);

		String productNo = "";
		// 20=C/A
		toHost.put("APPLICATION_CODE", "20");
		refNo = CibIdGenerator.getRefNoForTransaction();
		testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_3RD_PARTY, refNo);
		fromHost = testClient.doTransaction(toHost);
		// 锟斤拷锟斤拷撞锟斤拷晒锟斤拷虮ǔ锟斤拷锟斤拷锟斤拷锟斤拷
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		List caList = (List) fromHost.get("ACCOUNT_LIST");
		for (int i = 0; i < caList.size(); i++) {
			((Map) caList.get(i)).put("APPLICATION_CODE", "20");
			productNo = ((Map) caList.get(i)).get("PRODUCT_NO").toString();
			((Map) caList.get(i)).put("PRODUCT_DESCRIPTION", this.getPrDescription("20", productNo));
		}
		accList.addAll(caList);

		// TODO 锟斤拷锟斤拷锟斤拷锟轿ｏ拷锟斤拷式锟斤拷锟斤拷锟斤拷锟斤拷锟绞憋拷锟斤拷
		// 21=O/A
		toHost.put("APPLICATION_CODE", "21");
		refNo = CibIdGenerator.getRefNoForTransaction();
		testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_3RD_PARTY, refNo);
		fromHost = testClient.doTransaction(toHost);
		// 锟斤拷锟斤拷撞锟斤拷晒锟斤拷虮ǔ锟斤拷锟斤拷锟斤拷锟斤拷
		if (!testClient.isSucceed()) {
//			throw new NTBHostException(testClient.getErrorArray());
			throw new NTBHostException(fromHost.get("REJECT_CODE").toString());
		}
		List oaList = (List) fromHost.get("ACCOUNT_LIST");
		for (int i = 0; i < oaList.size(); i++) {
			((Map) oaList.get(i)).put("APPLICATION_CODE", "21");
			productNo = ((Map) oaList.get(i)).get("PRODUCT_NO").toString();
			((Map) oaList.get(i)).put("PRODUCT_DESCRIPTION", this.getPrDescription("21", productNo));
		}
		accList.addAll(oaList);

		// 30=Time
		toHost.put("APPLICATION_CODE", "30");
		refNo = CibIdGenerator.getRefNoForTransaction();
		testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_3RD_PARTY, refNo);
		fromHost = testClient.doTransaction(toHost);
		// 锟斤拷锟斤拷撞锟斤拷晒锟斤拷虮ǔ锟斤拷锟斤拷锟斤拷锟斤拷
		if (!testClient.isSucceed()) {
//			throw new NTBHostException(testClient.getErrorArray());
			throw new NTBHostException(fromHost.get("REJECT_CODE").toString());
		}
		List timeList = (List) fromHost.get("ACCOUNT_LIST");
		for (int i = 0; i < timeList.size(); i++) {
			((Map) timeList.get(i)).put("APPLICATION_CODE", "30");
			productNo = ((Map) timeList.get(i)).get("PRODUCT_NO").toString();
			((Map) timeList.get(i)).put("PRODUCT_DESCRIPTION", this.getPrDescription("30", productNo));
		}
		accList.addAll(timeList);

		// 50=Loan
		toHost.put("APPLICATION_CODE", "50");
		refNo = CibIdGenerator.getRefNoForTransaction();
		testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_3RD_PARTY, refNo);
		fromHost = testClient.doTransaction(toHost);
		// 锟斤拷锟斤拷撞锟斤拷晒锟斤拷虮ǔ锟斤拷锟斤拷锟斤拷锟斤拷
		if (!testClient.isSucceed()) {
//			throw new NTBHostException(testClient.getErrorArray());
			throw new NTBHostException(fromHost.get("REJECT_CODE").toString());
		}
		List loanList = (List) fromHost.get("ACCOUNT_LIST");
		for (int i = 0; i < loanList.size(); i++) {
			((Map) loanList.get(i)).put("APPLICATION_CODE", "50");
			productNo = ((Map) loanList.get(i)).get("PRODUCT_NO").toString();
			((Map) loanList.get(i)).put("PRODUCT_DESCRIPTION", this.getPrDescription("50", productNo));
		}
		accList.addAll(loanList);

		CorpPreference corpPref = corpPrefService.findCorpPrefByCorpId(corpId,
				CorpPreference.PREF_TYPE_ACCOUNT,
				Constants.STATUS_PENDING_APPROVAL);
		if (corpPref == null) {
			// validate Account
			validateAccount(corpId, accList);
		}

		Map accInfo = null;
		List onlineAccList = new ArrayList();
		List offlineAccList = new ArrayList();

		//
		if (accList != null) {
			for (int i = 0; i < accList.size(); i++) {
				accInfo = (Map) accList.get(i);
				if (accInfo.get("ONLINE_FLAG").toString().equals("Y")) {
					onlineAccList.add(accInfo);
				} else if (accInfo.get("ONLINE_FLAG").toString().equals("N")) {
					offlineAccList.add(accInfo);
				}
			}
		} else {
			onlineAccList = new ArrayList();
			offlineAccList = new ArrayList();
		}
		hostAccMap.put(ACC_LIST_TYPE_ONLINE, onlineAccList);
		hostAccMap.put(ACC_LIST_TYPE_OFFLINE, offlineAccList);

		return hostAccMap;
	}

	public Map getLocalAccListMap(String corpID) throws NTBException {
		Map localAccMap = new HashMap();
		List accList = this.corpAccountDao.list(
				"from CorpAccount as ca where ca.corpId = ? and ca.status <> ?",
				new Object[] { corpID, Constants.STATUS_THOROUGH_REMOVED });
		CorpAccount corpAccout = null;
		List onlineAccList = new ArrayList();
		List offlineAccList = new ArrayList();

		//
		if (accList != null) {
			String opereation = "";
			for (int i = 0; i < accList.size(); i++) {
				corpAccout = (CorpAccount) accList.get(i);
				opereation = corpAccout.getOperation();
				if ((opereation.equals(Constants.OPERATION_NEW))
						|| (opereation.equals(Constants.OPERATION_KEEPONLINE))) {
					onlineAccList.add(corpAccout);
				} else if ((opereation.equals(Constants.OPERATION_REMOVE))
						|| (opereation.equals(Constants.OPERATION_KEEPOFFLINE))) {
					offlineAccList.add(corpAccout);
				}
			}
		} else {
			onlineAccList = new ArrayList();
			offlineAccList = new ArrayList();
		}
		localAccMap.put(ACC_LIST_TYPE_ONLINE, onlineAccList);
		localAccMap.put(ACC_LIST_TYPE_OFFLINE, offlineAccList);

		return localAccMap;
	}

	public Map getNewAccListMap(List currOnlineAccList,
			List currOfflineAccList, String[] varAccs, String oppType)
			throws NTBException {
		Map newAccListMap = new HashMap();
		Map objMap = null;

		if (oppType.equals(OPERATION_TYPE_ADD_ACC)) {
			List newOnlineAccList = new ArrayList();

			// 锟斤拷offlineAccList删锟斤拷要online锟斤拷锟剿猴拷
			for (int i = 0; i < varAccs.length; i++) {
				for (int j = 0; j < currOfflineAccList.size(); j++) {
					objMap = (Map) currOfflineAccList.get(j);
					if (objMap.get("ACCOUNT_NO").toString().equals(varAccs[i])) {
						currOfflineAccList.remove(objMap);
						newOnlineAccList.add(objMap);
					}
				}
			}
			// 锟斤拷锟铰碉拷online锟剿号硷拷锟诫到onlineAccList
			currOnlineAccList.addAll(newOnlineAccList);

		} else if (oppType.equals(OPERATION_TYPE_REMOVE_ACC)) {
			List newOfflineAccList = new ArrayList();

			// 锟斤拷onlineAccList删锟斤拷要offline锟斤拷锟剿猴拷
			for (int i = 0; i < varAccs.length; i++) {
				for (int j = 0; j < currOnlineAccList.size(); j++) {
					objMap = (Map) currOnlineAccList.get(j);
					if (objMap.get("ACCOUNT_NO").toString().equals(varAccs[i])) {
						currOnlineAccList.remove(objMap);
						newOfflineAccList.add(objMap);
					}
				}
			}
			// 锟斤拷锟铰碉拷offline锟剿号硷拷锟诫到offlineAccList
			currOfflineAccList.addAll(newOfflineAccList);
		}

		newAccListMap.put(ACC_LIST_TYPE_ONLINE, currOnlineAccList);
		newAccListMap.put(ACC_LIST_TYPE_OFFLINE, currOfflineAccList);

		return newAccListMap;
	}

	public List listLocalCorpAccount(String corpID, String status) throws NTBException {
		List accList = null;
		Map conditionMap = new HashMap();
		conditionMap.put("corpId", corpID);
		if ((status != null) && (!status.equals(""))) {
			conditionMap.put("status", status);
		}
		accList = corpAccountDao.list(CorpAccount.class, conditionMap);
		return accList;
	}
	//add by linrui 
	public List listLocalCorpAccountMap(String corpID, String status) throws NTBException {
		List accList = null;
		String sql = "";
		try {
			if ((status != null) && (!status.equals(""))) {
				sql = "select ca.account_No as ACCOUNTNO, ca.account_Type as ACCOUNTTYPE, ca.currency as CURRENCY  from Corp_Account ca where ca.corp_Id = ? and ca.status = ? ";
				List localAccList = this.genericJdbcDao.query(sql,
						new Object[] { corpID, status });
				return localAccList;
			} else {
				sql = "select ca.account_No as ACCOUNTNO, ca.account_Type as ACCOUNTTYPE, ca.currency as CURRENCY from Corp_Account ca where ca.corp_Id = ? ";
				List localAccList = this.genericJdbcDao.query(sql,
						new Object[] { corpID });
				return localAccList;
			}
		} catch (Exception e) {
			throw new NTBException("err.txn.GetCGDException");
		}
	}
	//add by linrui for return more CorpAccount
	public List listLocalCorpAccountClass(String corpID, String status) throws NTBException {
		List accList = null;
		String sql = "";
		List corpAcctList = new ArrayList();
		try {
			if((status != null) && (!status.equals(""))){
				sql = "select ca.account_No ACCOUNTNO, ca.account_Type ACCOUNTTYPE, ca.currency CURRENCY, ca.Pref_id PREFID, ca.operation OPERATION, ca.account_name ACCOUNTNAME from Corp_Account ca where ca.corp_Id = ? and ca.status = ?";
			    List localAccList = this.genericJdbcDao.query(sql, new Object[] { corpID, status });
			    if(localAccList.size()>0){
				   for (int i = 0; i < localAccList.size(); i++) {
					   Map acctMap = (Map)localAccList.get(i);
					   CorpAccount corpAccount = new CorpAccount();
					   corpAccount.setPrefId(Utils.null2EmptyWithTrim(acctMap.get("PREFID")));
					   corpAccount.setAccountType((Utils.null2EmptyWithTrim(acctMap.get("ACCOUNTTYPE"))));
					   corpAccount.setAccountNo(acctMap.get("ACCOUNTNO").toString());
					   corpAccount.setOperation(acctMap.get("OPERATION").toString());
					   corpAccount.setAccountName((Utils.null2EmptyWithTrim(acctMap.get("ACCOUNTNAME"))));
					   if(acctMap.get("CURRENCY") !=null)
					   corpAccount.setCurrency(acctMap.get("CURRENCY").toString());
					   corpAcctList.add(corpAccount);
				   }
			    }	
			    return corpAcctList;
			}else{
				sql = "select ca.account_No ACCOUNTNO, ca.account_Type ACCOUNTTYPE, ca.currency CURRENCY, ca.Pref_id PREFID, ca.operation OPERATION from Corp_Account ca where ca.corp_Id = ?  ";
				List localAccList = this.genericJdbcDao.query(sql, new Object[] { corpID });
				if(localAccList.size()>0){
					for (int i = 0; i < localAccList.size(); i++) {
						Map acctMap = (Map)localAccList.get(i);
						CorpAccount corpAccount = new CorpAccount();
						corpAccount.setPrefId(Utils.null2EmptyWithTrim(acctMap.get("PREFID")));
						corpAccount.setAccountType((Utils.null2EmptyWithTrim(acctMap.get("ACCOUNTTYPE"))));
						corpAccount.setAccountNo(acctMap.get("ACCOUNTNO").toString());
						corpAccount.setOperation(acctMap.get("OPERATION").toString());
						corpAccount.setAccountName((Utils.null2EmptyWithTrim(acctMap.get("ACCOUNTNAME"))));
						if(acctMap.get("CURRENCY") !=null)
						corpAccount.setCurrency(acctMap.get("CURRENCY").toString());
						corpAcctList.add(corpAccount);
					}
				}	
				return corpAcctList;
			}
		} catch (Exception e) {
			throw new NTBException("err.txn.GetCGDException");
		}
	}

	public void updateLocalAccount(String userId, String corpId, List onlineAccList,
			List offlineAccList, String oldPrefId, String newPrefId)
			throws NTBException {

		CorpAccount corpAccount = null;
		Map accMap = null;

//		List localOnlineAccList = listLocalCorpAccount(corpId,Constants.STATUS_NORMAL);
		List localOnlineAccList = listLocalCorpAccountClass(corpId,Constants.STATUS_NORMAL);//add by linrui 20190327
		corpAccount = null;
		accMap = null;
		// 锟斤拷锟斤拷锟斤拷锟斤拷online锟剿猴拷状态
		for (int i = 0; i < localOnlineAccList.size(); i++) {
			corpAccount = (CorpAccount) localOnlineAccList.get(i);
			corpAccount.setPrefId(newPrefId);
			corpAccount.setOperation(Constants.OPERATION_REMOVE);
			corpAccount.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
                        corpAccount.setRequester(userId);
			loop1:
				for (int j = 0; j < onlineAccList.size(); j++) {
					accMap = (Map) onlineAccList.get(j);
					if (corpAccount.getAccountNo().equals(accMap.get("ACCOUNT_NO").toString())) {
						corpAccount.setOperation(Constants.OPERATION_KEEPONLINE);
						corpAccount.setAccountDesc(accMap.get("PRODUCT_DESCRIPTION").toString().trim());
						break loop1;
					}
				}
			corpAccountDao.update(corpAccount);
			// add account history
			this.addAccountHis(corpAccount);
		}

		// 锟斤拷锟铰憋拷锟斤拷offline锟剿猴拷
//		List loaclOfflineAccList = listLocalCorpAccount(corpId,Constants.STATUS_REMOVED);
		List loaclOfflineAccList = listLocalCorpAccountClass(corpId,Constants.STATUS_REMOVED);
		corpAccount = null;
		accMap = null;
		// 锟斤拷锟斤拷锟斤拷锟斤拷offline锟剿猴拷状态
		for (int i = 0; i < loaclOfflineAccList.size(); i++) {
			corpAccount = (CorpAccount) loaclOfflineAccList.get(i);
			corpAccount.setPrefId(newPrefId);
			corpAccount.setOperation(Constants.OPERATION_NEW);
			corpAccount.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
                        corpAccount.setRequester(userId);
			loop2: for (int j = 0; j < offlineAccList.size(); j++) {
				accMap = (Map) offlineAccList.get(j);
				if (corpAccount.getAccountNo().equals(accMap.get("ACCOUNT_NO").toString())) {
					corpAccount.setOperation(Constants.OPERATION_KEEPOFFLINE);
					corpAccount.setAccountDesc(accMap.get("PRODUCT_DESCRIPTION").toString().trim());
					break loop2;
				}
			}
			corpAccountDao.update(corpAccount);
			// add account history
			this.addAccountHis(corpAccount);
		}

		// 锟斤拷锟斤拷CORP_PREFERENCE锟斤拷
		// 锟斤拷一锟斤拷锟铰斤拷锟剿号ｏ拷preference锟斤拷锟斤拷未锟叫革拷锟斤拷业锟斤拷锟�
		CorpPreference oldCorpPref = null;
		int oldVersion = 0;
		if (Utils.null2EmptyWithTrim(oldPrefId).equals("")) {
			// oldCorpPref = new CorpPreference();
		} else {
			oldCorpPref = (CorpPreference) corpPreferenceDao.load(CorpPreference.class, oldPrefId);
			oldVersion = oldCorpPref.getVersion().intValue();
		}
		CorpPreference newCorpPref = new CorpPreference(newPrefId);
		newCorpPref.setPrefType(CorpPreference.PREF_TYPE_ACCOUNT);
		newCorpPref.setCorpId(corpId);
		newCorpPref.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
		newCorpPref.setStatus(Constants.STATUS_PENDING_APPROVAL);
		newCorpPref.setVersion(new Integer(oldVersion + 1));
		newCorpPref.setRelativeId(oldPrefId);
		corpPreferenceDao.add(newCorpPref);
	}

	public void updateLocalAccount(CorpAccount pojoCorpAccount)	throws NTBException {
		corpAccountDao.update(pojoCorpAccount);
	}

	public List listPrivilegedAccount(CorpUser user, String accType) throws NTBException {
		Map conditionMap = new HashMap();
		conditionMap.put("corpId", user.getCorpId());
		conditionMap.put("accountType", accType);
		conditionMap.put("status", Constants.STATUS_NORMAL);
		List accList = corpAccountDao.list(CorpAccount.class, conditionMap);
		List permList = user.getAccountList();
		List retList = new ArrayList();
		for (int i = 0; i < accList.size(); i++) {
			CorpAccount acc = (CorpAccount) accList.get(i);
			for (int j = 0; j < permList.size(); j++) {
				NTBPermission perm = (NTBPermission) permList.get(j);
				if (acc.getAccountNo().equals(perm.getPermissionResource())) {
					retList.add(acc);
					break;
				}
			}
		}
		return retList;
	}
	
	public List listAllPrivilegedAccount(CorpUser user, String accType) throws NTBException {
		Map conditionMap = new HashMap();
		conditionMap.put("corpId", user.getCorpId());
		if (accType==null || accType.equals("")){
			conditionMap.put("accountType", accType);
		}
		conditionMap.put("status", Constants.STATUS_NORMAL);
		List accList = corpAccountDao.list(CorpAccount.class, conditionMap);
		List permList = user.getAccountList();
		List retList = new ArrayList();
		for (int i = 0; i < accList.size(); i++) {
			CorpAccount acc = (CorpAccount) accList.get(i);
			for (int j = 0; j < permList.size(); j++) {
				NTBPermission perm = (NTBPermission) permList.get(j);
				//Log.info("CorpAccount accountNo="+acc.getAccountNo()+"\t NTBPermission accountNo="+perm.getPermissionResource());
				if (acc.getAccountNo().equals(perm.getPermissionResource())) {
					retList.add(acc);
					break;
				}
			}
		}
		return retList;
	}

	public List listCorpAccountByAccType(String corpId, String accType)	throws NTBException {
		Map conditionMap = new HashMap();
		conditionMap.put("corpId", corpId);
		conditionMap.put("accountType", accType);
		conditionMap.put("status", Constants.STATUS_NORMAL);
		List accList = corpAccountDao.list(CorpAccount.class, conditionMap);
		return accList;
	}

	public void approve(String txnType, String prefId, CorpUser corpUser) throws NTBException {
		Map toHost = new HashMap();
		CibTransClient testClient = new CibTransClient("CIB", "ZC10");
		Map fromHost = new HashMap();
		CorpPreference corpPref =
			(CorpPreference) corpPreferenceDao.load(CorpPreference.class, prefId);
		CorpAccount corpAccount = null;
		if (corpPref == null) {
			throw new NTBException("err.bnk.NoSuchPreferenceId");
		} else {
			// 锟斤拷锟斤拷CORP_PREFERENCE锟斤拷
			String oldPrefId = Utils.null2EmptyWithTrim(corpPref
					.getRelativeId());
			String corpId = corpPref.getCorpId();
			// 锟斤拷锟铰旧碉拷pref锟斤拷录
			if (!oldPrefId.equals("")) {
				CorpPreference oldPorpPref = (CorpPreference) corpPreferenceDao.load(CorpPreference.class, oldPrefId);
				oldPorpPref.setStatus(Constants.STATUS_REMOVED);
				corpPreferenceDao.update(oldPorpPref);
			}
			// 锟斤拷锟斤拷锟铰碉拷pref锟斤拷录
			corpPref.setStatus(Constants.STATUS_NORMAL);
			corpPref.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			corpPreferenceDao.update(corpPref);

			// 锟斤拷锟斤拷CORP_ACCOUNT锟斤拷
//			List accList = this.listLocalCorpAccount(corpId, null);
			List accList = this.listLocalCorpAccountClass(corpId, null);
			if (accList != null) {
				String opereation = "";
				String accountNo = "";
				for (int i = 0; i < accList.size(); i++) {
					corpAccount = (CorpAccount) accList.get(i);
					opereation = corpAccount.getOperation();
					accountNo = corpAccount.getAccountNo();
					if ((opereation.equals(Constants.OPERATION_NEW))
							|| (opereation.equals(Constants.OPERATION_KEEPONLINE))) {
						corpAccount.setStatus(Constants.STATUS_NORMAL);
						corpAccount.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
					} else if ((opereation.equals(Constants.OPERATION_REMOVE))
							|| (opereation.equals(Constants.OPERATION_KEEPOFFLINE))) {
						corpAccount.setStatus(Constants.STATUS_REMOVED);
						corpAccount.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
					} else {
						continue;
					}
					corpAccount.setLastUpdateTime(new Date());
					corpAccountDao.update(corpAccount);
					// update account history
					this.updateAccountHis(corpAccount);

					// up to host(ZC10)
					toHost.clear();
					toHost.put("ACCOUNT_NO", accountNo);
					if (opereation.equals(Constants.OPERATION_NEW)) {
						// 锟斤拷锟斤拷权锟睫憋拷
						corpPermissionDao.addAccForDefaultGrp(corpId, accountNo);
						toHost.put("SWITCH", "1");
						testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_ENQUIRY,
								CibTransClient.ACCTYPE_3RD_PARTY, prefId);
						fromHost = testClient.doTransaction(toHost);
						if (!testClient.isSucceed()) {
							throw new NTBHostException(testClient.getErrorArray());
						}
						Log.info("Update to Host(Add to online), Account No is : " + toHost.get("ACCOUNT_NO"));
					} else if (opereation.equals(Constants.OPERATION_REMOVE)) {
						// 锟斤拷锟斤拷权锟睫憋拷
						corpPermissionDao.delAccForDefaultGrp(corpId, accountNo);
						//
						toHost.put("SWITCH", "2");
						testClient.setAlpha8(corpUser,	CibTransClient.TXNNATURE_ENQUIRY,
								CibTransClient.ACCTYPE_3RD_PARTY, prefId);
						fromHost = testClient.doTransaction(toHost);
						// 锟斤拷锟斤拷撞锟斤拷晒锟斤拷虮ǔ锟斤拷锟斤拷锟斤拷锟斤拷
						if (!testClient.isSucceed()) {
							throw new NTBHostException(testClient.getErrorArray());
						}
						Log.info("Update to Host(Remove from online), Account No is : "	+ toHost.get("ACCOUNT_NO"));
					}
				}
			} else {
				throw new NTBException("err.bnk.NoSuchCorporationId");
			}
		}

	}

	public void reject(String txnType, String prefId, CorpUser corpUser) throws NTBException {
		CorpPreference corpPref =
			(CorpPreference) corpPreferenceDao.load(CorpPreference.class, prefId);
		if (corpPref == null) {
			throw new NTBException("err.bnk.NoSuchPreferenceId");
		} else {
			// 锟斤拷锟斤拷CORP_PREFERENCE锟斤拷
			corpPref.setStatus(Constants.STATUS_REMOVED);
			corpPref.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
			corpPreferenceDao.update(corpPref);
		}

	}

	private void validateAccount(String corpId, List hostAccList) throws NTBException {
		String hql = "from CorpAccount as ca where ca.corpId = ? and (ca.status = ? or ca.status = ?)";
		List localAccList = this.corpAccountDao.list(hql, new Object[] {
				corpId, Constants.STATUS_NORMAL, Constants.STATUS_REMOVED });

		Map hostAcc = null;
		String hostAccNo = "";
		CorpAccount localAcc = null;
		String localAccNo = "";

		// check new account
		String ccyCbsNo = "";
		String hostOnlineFlag = "";
		RcCurrency rcCcy = new RcCurrency();
		loop1:
			for (int i = 0; i < hostAccList.size(); i++) {
				hostAcc = (Map) hostAccList.get(i);
				hostAccNo = hostAcc.get("ACCOUNT_NO").toString().trim();
				hostOnlineFlag = Utils.null2EmptyWithTrim(hostAcc.get("ONLINE_FLAG").toString());
				StatusBean bean = this.getStatusBeanByHostOnlineFlag(hostOnlineFlag);
				for (int j = 0; j < localAccList.size(); j++) {
					localAcc = (CorpAccount) localAccList.get(j);
					localAccNo = localAcc.getAccountNo().trim();
					if (hostAccNo.equals(localAccNo)) { // 锟斤拷锟斤拷锟剿猴拷锟节憋拷锟斤拷锟剿猴拷锟叫达拷锟斤拷
						//锟斤拷锟阶刺拷欠锟揭伙拷锟�
						if (!bean.getStatus().equals(localAcc.getStatus())) {
							// 锟斤拷取Account Type
							String appCode = hostAcc.get("APPLICATION_CODE").toString();
							String accType = appCode2AccType(appCode);
							// 锟斤拷锟铰憋拷锟斤拷锟剿猴拷
							CorpAccount updateAccount = new CorpAccount(hostAccNo);
							updateAccount.setAccountType(accType);
							ccyCbsNo = Utils.prefixZero(hostAcc.get("CURRENCY_CODE").toString(), 3);
							updateAccount.setCurrency(rcCcy.getCcyByCbsNumber(ccyCbsNo));
							updateAccount.setPrefId(localAcc.getPrefId());
							updateAccount.setCorpId(corpId);
							updateAccount.setAccountName(localAcc.getAccountName());
							updateAccount.setAccountDesc(hostAcc.get("PRODUCT_DESCRIPTION").toString().trim());
							updateAccount.setOperation(bean.getOperation());
							updateAccount.setStatus(bean.getStatus());
							this.corpAccountDao.update(updateAccount);
						}
						// 锟斤拷锟斤拷锟揭伙拷锟紿ost Account No
						continue loop1;
					}
				}
				// 锟斤拷取Account Type
				String appCode = hostAcc.get("APPLICATION_CODE").toString();
				String accType = appCode2AccType(appCode);
				// 锟斤拷锟斤拷锟斤拷锟斤拷锟剿猴拷
				CorpAccount newAccount = new CorpAccount(hostAccNo);
				newAccount.setAccountType(accType);
				ccyCbsNo = Utils.prefixZero(hostAcc.get("CURRENCY_CODE").toString(), 3);
				newAccount.setCurrency(rcCcy.getCcyByCbsNumber(ccyCbsNo));
				newAccount.setPrefId((localAcc == null) ? null : localAcc.getPrefId());
				newAccount.setCorpId(corpId);
				newAccount.setAccountName("");
				newAccount.setAccountDesc(hostAcc.get("PRODUCT_DESCRIPTION").toString().trim());
				newAccount.setOperation(bean.getOperation());
				newAccount.setStatus(bean.getStatus());
				newAccount.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
				CorpAccount tmpAccount = (CorpAccount) corpAccountDao.load(CorpAccount.class, hostAccNo);
				if (tmpAccount == null) {
					this.corpAccountDao.add(newAccount);
				} else {
					this.corpAccountDao.update(newAccount);
				}
			}

		// check remove account
		loop2:
			for (int i = 0; i < localAccList.size(); i++) {
				localAcc = (CorpAccount) localAccList.get(i);
				localAccNo = localAcc.getAccountNo().trim();
				for (int j = 0; j < hostAccList.size(); j++) {
					hostAcc = (Map) hostAccList.get(j);
					hostAccNo = hostAcc.get("ACCOUNT_NO").toString().trim();
					if (localAccNo.equals(hostAccNo)) { // 锟斤拷锟斤拷锟剿猴拷锟斤拷锟斤拷锟斤拷锟剿猴拷锟叫达拷锟斤拷
						// 锟斤拷锟斤拷锟揭伙拷锟絃ocal Account No
						continue loop2;
					}
				}
				// 删锟斤拷锟斤拷撕锟�
				localAcc.setOperation(Constants.OPERATION_THOROUGH_REMOVE);
				localAcc.setStatus(Constants.STATUS_THOROUGH_REMOVED);
				localAcc.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
				this.corpAccountDao.update(localAcc);
			}

	}

	private String appCode2AccType(String appCode) throws NTBException {
		String accType = "";
		if (appCode.equals(CibTransClient.APPCODE_CURRENT_ACCOUNT)) {
			accType = CorpAccount.ACCOUNT_TYPE_CURRENT;
		} else if (appCode.equals(CibTransClient.APPCODE_OVERDRAFT_ACCOUNT)) {
			accType = CorpAccount.ACCOUNT_TYPE_OVER_DRAFT;
		} else if (appCode.equals(CibTransClient.APPCODE_TIME_DEPOSIT)) {
			accType = CorpAccount.ACCOUNT_TYPE_TIME_DEPOSIT;
		} else if (appCode.equals(CibTransClient.APPCODE_LOAN_ACCOUNT)) {
			accType = CorpAccount.ACCOUNT_TYPE_LOAN;
		} else if (appCode.equals(CibTransClient.APPCODE_CREDIT_VISA) 
				|| appCode.equals(CibTransClient.APPCODE_CREDIT_MASTER) 
				|| appCode.equals(CibTransClient.APPCODE_CREDIT_AE)
				|| appCode.equals(CibTransClient.APPCODE_CREDIT_UT)) {
			accType = CorpAccount.ACCOUNT_TYPE_CREDIT;
		}
		return accType;
	}

	private String getPrDescription(String appCode, String productNo)
			throws NTBException {
		String sql = "select PRODUCT_DESCRIPTION from HS_PRODUCT_TYPE where APP_CODE = ? and PRODUCT_TYPE = ?";
		try {
			Map rowMap = this.genericJdbcDao.querySingleRow(sql, new Object[] {appCode, Utils.prefixZero(productNo, 3) });
			return rowMap == null ? "" : rowMap.get("PRODUCT_DESCRIPTION").toString();
		} catch (Exception e) {
			throw new NTBException("err.bnk.GetPrDescriptionException");
		}
	}

	private StatusBean getStatusBeanByHostOnlineFlag(String hostOnlineFlag) throws NTBException {
		StatusBean bean = new StatusBean();
		if (hostOnlineFlag.equals("Y")) {
			bean.setStatus(Constants.STATUS_NORMAL);
			bean.setOperation(Constants.OPERATION_NEW);
		} else if (hostOnlineFlag.equals("N")) {
			bean.setStatus(Constants.STATUS_REMOVED);
			bean.setOperation(Constants.OPERATION_REMOVE);
		}
		return bean;
	}

	private class StatusBean {
		private String operation;
		private String status;

		public String getOperation() {
			return operation;
		}

		public void setOperation(String operation) {
			this.operation = operation;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}

	private void addAccountHis(CorpAccount corpAccount) throws NTBException{
		if (corpAccount.getOperation().equals(Constants.OPERATION_KEEPOFFLINE )
				|| corpAccount.getOperation().equals(Constants.OPERATION_KEEPONLINE)) {
			//
		} else {
			CorpAccountHis accountHis = new CorpAccountHis(CibIdGenerator.getIdForOperation("CORP_ACCOUNT_HIS"));
			try {
				org.apache.commons.beanutils.BeanUtils.copyProperties(accountHis, corpAccount);
			} catch (IllegalAccessException e) {
				Log.error("copyProperties IllegalAccessException", e);
				throw new NTBException("err.bnk.AddAccountHisException");
			} catch (InvocationTargetException e) {
				Log.error("copyProperties IllegalAccessException", e);
				throw new NTBException("err.bnk.AddAccountHisException");
			}
			this.corpAccountHisDao.add(accountHis);
		}
	}

	private void updateAccountHis(CorpAccount corpAccount) throws NTBException {
		if (corpAccount.getOperation().equals(Constants.OPERATION_KEEPOFFLINE )
				|| corpAccount.getOperation().equals(Constants.OPERATION_KEEPONLINE)) {
			//
		} else {
			Map conditionMap = new HashMap();
			conditionMap.put("accountNo", corpAccount.getAccountNo());
			conditionMap.put("prefId", corpAccount.getPrefId());
			List list = this.corpAccountHisDao.list(CorpAccountHis.class, conditionMap);
			if (list.size() > 0) {
				CorpAccountHis accountHis = (CorpAccountHis) list.get(0);
				try {
					org.apache.commons.beanutils.BeanUtils.copyProperties(accountHis, corpAccount);
				} catch (IllegalAccessException e) {
					Log.error("copyProperties IllegalAccessException", e);
					throw new NTBException("err.bnk.AddAccountHisException");
				} catch (InvocationTargetException e) {
					Log.error("copyProperties IllegalAccessException", e);
					throw new NTBException("err.bnk.AddAccountHisException");
				}
				accountHis.setLastUpdateTime(new Date());
				this.corpAccountHisDao.update(accountHis);
			}
		}
	}
	
	public boolean isSavingAccount(String accountNo) throws NTBException {
		accountNo = Utils.null2EmptyWithTrim(accountNo);		
		return getAccountType(accountNo.trim()).trim().equals(CorpAccount.ACCOUNT_TYPE_SAVING);
		
	}
	//add by linrui for mul-ccy
	public boolean isSavingAccount(String accountNo,String ccy) throws NTBException {
		accountNo = Utils.null2EmptyWithTrim(accountNo);		
		ccy = Utils.null2EmptyWithTrim(ccy);		
		return getAccountType(accountNo.trim(),ccy).trim().equals(CorpAccount.ACCOUNT_TYPE_SAVING);
		
	}
	
	public boolean checkAccountByUser(CorpUser corpUser, String checkingAccount) throws NTBException {
		List accList = corpUser.getAccountList();
		CorpPermission obj = null;
		for(int i=0; i<accList.size();i++) {
			obj = (CorpPermission) accList.get(i);
			if(obj.getPermissionResource().trim().equals(checkingAccount)) {
				return true;
			}
		}
		return false;
	}
	//add by linrui 20190402
	 public void updateAcctName(String accountNo, String accountName , String currency) throws NTBException {
	        try{
	        	genericJdbcDao.update(
	                    "update Corp_Account set ACCOUNT_NAME = ? where account_no = ? and currency = ? ",
	                    new Object[] {accountName,accountNo,currency});
	        }catch(Exception e){
	            Log.error("Error update group's permission", e);
	            throw new NTBException("err.sys.DBError");
	        }
	    }

}
