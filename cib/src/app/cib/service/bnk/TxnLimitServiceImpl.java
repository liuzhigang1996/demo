/**
 * @author hjs
 * 2006-8-3
 */
package app.cib.service.bnk;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.ApplicationContext;

import app.cib.bo.bnk.TxnLimit;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpPreference;
import app.cib.bo.txn.TxnLimitUsage;
import app.cib.dao.bnk.TxnLimitDao;
import app.cib.dao.sys.CorpAccountDao;
import app.cib.dao.sys.CorpPreferenceDao;
import app.cib.dao.txn.TxnLimitUsageDao;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.CorpPreferenceService;
import app.cib.util.Constants;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 2006-8-3
 */
public class TxnLimitServiceImpl implements TxnLimitService {
	
	private TxnLimitDao txnLimitDao;
	
	private CorpAccountDao corpAccountDao;
	
	private CorpPreferenceDao corpPreferenceDao;
	
	private TxnLimitUsageDao txnLimitUsageDao;

	public TxnLimitDao getTxnLimitDao() {
		return txnLimitDao;
	}

	public void setTxnLimitDao(TxnLimitDao txnLimitDao) {
		this.txnLimitDao = txnLimitDao;
	}

	public CorpAccountDao getCorpAccountDao() {
		return corpAccountDao;
	}

	public void setCorpAccountDao(CorpAccountDao corpAccountDao) {
		this.corpAccountDao = corpAccountDao;
	}

	public CorpPreferenceDao getCorpPreferenceDao() {
		return corpPreferenceDao;
	}

	public void setCorpPreferenceDao(CorpPreferenceDao corpPreferenceDao) {
		this.corpPreferenceDao = corpPreferenceDao;
	}

	public TxnLimitUsageDao getTxnLimitUsageDao() {
		return txnLimitUsageDao;
	}

	public void setTxnLimitUsageDao(TxnLimitUsageDao txnLimitUsageDao) {
		this.txnLimitUsageDao = txnLimitUsageDao;
	}

	public String checkLimitsStatus(String corpId, String account) throws NTBException {
		if (viewLimitsDetial(corpId, account, Constants.STATUS_PENDING_APPROVAL) != null) {
			return "1";			
		} else if (viewLimitsDetial(corpId, account, Constants.STATUS_NORMAL) != null) {
			return "0";
		}else {
			return "N";
		}
	}

	public TxnLimit viewLimitsDetialByPK(String limitId) throws NTBException {
		TxnLimit txnLimit = (TxnLimit) txnLimitDao.load(TxnLimit.class, limitId);
		return txnLimit;
	}

	public TxnLimit viewLimitsDetialByPrefId(String prefId) throws NTBException {
		if (!Utils.null2EmptyWithTrim(prefId).equals("")){
			String hql = "from TxnLimit as t where t.prefId=? order by t.limitId desc";
			TxnLimit txnLimit = null;
			List list = txnLimitDao.list(hql, new Object[]{prefId});
			if (list.size()>0) {
				txnLimit = (TxnLimit) list.get(0);
				return txnLimit;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see app.cib.service.bnk.TxnLimitService#viewLimitsDetial(java.lang.String)
	 */
	public TxnLimit viewLimitsDetial(String corpId, String accNo, String status) throws NTBException {
		TxnLimit txnLimit = null;
		List objList = new ArrayList();
		String hql = "from TxnLimit as t where t.operation<>? ";
		objList.add(Constants.OPERATION_UPDATE_BEFORE);
		if (!Utils.null2EmptyWithTrim(corpId).equals("")){
			hql += " and t.corpId = ? ";
			objList.add(corpId);
		}
		if (!Utils.null2EmptyWithTrim(accNo).equals("")){
			hql += " and t.account = ? ";
			objList.add(accNo);
		}
		if (!Utils.null2EmptyWithTrim(status).equals("")){
			hql += " and t.status = ? ";
			objList.add(status);
		}
		hql += " order by t.limitId desc";
		List list = txnLimitDao.list(hql, objList.toArray());
		if (list.size()>0) {
			txnLimit = (TxnLimit) list.get(0);
		}
		return txnLimit;
	}

	public void newLimits(TxnLimit pojoTxnLimit, String newPrefId) throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        CorpPreferenceService corpPrefService = (CorpPreferenceService)appContext.getBean("CorpPreferenceService");

		CorpPreference corpPref = null;
		CorpPreference newCorpPref = null;
		if(pojoTxnLimit!=null){			
			//��֯CorpPreference pojo
			//ԭ��û��Pref��¼
			if (pojoTxnLimit.getOperation().equals(Constants.OPERATION_NEW)){
				newCorpPref = new CorpPreference(newPrefId);
				newCorpPref.setVersion(new Integer(1));
			} else if (pojoTxnLimit.getOperation().equals(Constants.OPERATION_UPDATE)){
				//���ԭ�е�pref��¼����һ��pref��¼
				String oldPrefId = pojoTxnLimit.getPrefId();
				corpPref = corpPrefService.findCorpPrefByID(oldPrefId);
				if(corpPref == null){
					throw new NTBException("err.bnk.NoSuchPrefIdAs:" + oldPrefId);
				}
				if(!corpPref.getAuthStatus().equals(Constants.AUTH_STATUS_COMPLETED)){
					throw new NTBException("err.bnk.LastTransNotCompleted");
				}
				newCorpPref = new CorpPreference();
				try {
					BeanUtils.copyProperties(newCorpPref, corpPref);
				} catch (IllegalAccessException e) {
					throw new NTBException("err.bnk.CopyPropertiesException");
				} catch (InvocationTargetException e) {
					throw new NTBException("err.bnk.CopyPropertiesException");
				}
				newCorpPref.setPrefId(newPrefId);
				newCorpPref.setVersion(new Integer(corpPref.getVersion().intValue()+1));
				newCorpPref.setRelativeId(corpPref.getPrefId());
			}
			newCorpPref.setPrefType(CorpPreference.PREF_TYPE_LIMIT);
			newCorpPref.setCorpId(pojoTxnLimit.getCorpId());
			newCorpPref.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			newCorpPref.setStatus(Constants.STATUS_PENDING_APPROVAL);
			//Ϊ�µ�txnLimit����prefId
			pojoTxnLimit.setPrefId(newCorpPref.getPrefId());
			
			txnLimitDao.add(pojoTxnLimit);
			corpPrefService.newPref(newCorpPref);
		}
	}

	public String getCurrency(String account, String allAccounts) throws NTBException { 
        ApplicationContext appContext = Config.getAppContext();
        CorpAccountService corpAccountService = (CorpAccountService)appContext.getBean("CorpAccountService");
        
        if(allAccounts.equals("checked")){
            return "MOP";
        } else {
            CorpAccount corpAcc = corpAccountService.viewCorpAccount(account);
            return corpAcc.getCurrency();
        }
	}

	public List listAccLimitsInfo(String corpId, String status) throws NTBException {
		String hql = "";
		List objList = new ArrayList();
		hql = "from TxnLimit as t where t.status <> ? and t.operation<>? ";
		objList.add(Constants.STATUS_REMOVED);
		objList.add(Constants.OPERATION_UPDATE_BEFORE);
		if (!Utils.null2EmptyWithTrim(corpId).equals("")){
			hql = hql + " and t.corpId = ?";
			objList.add(corpId);
		}
		if (!Utils.null2EmptyWithTrim(status).equals("")){
			hql = hql + " and t.status = ?";
			objList.add(status);
		}
		List list = txnLimitDao.list(hql, objList.toArray());
		return list;
	}

	public boolean checkPrefIdInCorpPref(String prefId) throws NTBException {
		CorpPreference corpPref = (CorpPreference) corpPreferenceDao.load(CorpPreference.class, prefId);
		if (corpPref == null) {
			return false;
		} else {
			String status = Utils.null2EmptyWithTrim(corpPref.getStatus());
			String authStatus = Utils.null2EmptyWithTrim(corpPref.getAuthStatus());
			if ((status.equals(Constants.STATUS_NORMAL)) && (authStatus.equals(Constants.AUTH_STATUS_COMPLETED))){
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * ALL_ACCOUNT�˺ż�����:
	 * 
	 * ���ÿһ����REMOVED��¼
	 * 	|- ÿһ����¼���������������
	 * 		|- 1. ACC_ACCOUNT�˺�
	 * 		|	|- ״̬ΪNORMAL
	 * 		|	|	|- CORP_PREFERENCE�����޴˼�¼ --> �׳�NTBException
	 * 		|	|	|- CORP_PREFERENCE����״̬Ϊ��NORMAL --> �׳�NTBException
	 * 		|	|	|- CORP_PREFERENCE����״̬ΪNORMAL --> convertPojo2Map(txnLimit, resultData);
	 * 		|	|		|- txnType =?= TxnLimit.TXN_TYPE_ALL
	 * 		|	|			|- true	 --> corpInfoMap.put("allLimits", txnLimit.getLimit1());
	 * 		|	|			|- false --> Undo Anything
	 * 		|	|- ״̬ΪPENDING_APPROVAL --> �׳�NTBException
	 * 		|- 2. ��ͨ�˺�
	 * 			|- ״̬ΪPENDING_APPROVAL --> �׳�NTBException
	 *			|- ״̬ΪNORMAL
	 * 			|	|- CORP_PREFERENCE�����޴˼�¼ --> �׳�NTBException
	 *			|	|- CORP_PREFERENCE����״̬Ϊ��NORMAL --> �׳�NTBException
	 *			|	|- CORP_PREFERENCE����״̬ΪNORMAL --> Undo Anything
	 * 			|- ״̬Ϊ���� --> �׳�NTBException
	 */
	public void setResultDataForAllAcc(String corpId, Map corpInfoMap, Map resultData) throws NTBException {
		TxnLimit txnLimit = null;
		List list = this.listAccLimitsInfo(corpId, null);
		//��鱾��ҵ����״̬Ϊ��removed��limits��¼
		for(int i=0; i<list.size(); i++){
			txnLimit = (TxnLimit) list.get(i);
			String tmpAcc = Utils.null2EmptyWithTrim(txnLimit.getAccount());
			String status = Utils.null2EmptyWithTrim(txnLimit.getStatus());
			String prefId = Utils.null2EmptyWithTrim(txnLimit.getPrefId());
			String txnType = Utils.null2EmptyWithTrim(txnLimit.getTxnType());
			//ALL_ACCOUNT�˺�
			if (tmpAcc.equals(TxnLimit.ACCOUNT_ALL)) {
				if (status.equals(Constants.STATUS_NORMAL)){
					if(!this.checkPrefIdInCorpPref(prefId)){ //״̬Ϊ����ɵ���CORP_PREFERENCE����Ϊδ��Ȩ
						throw new NTBException("err.bnk.LastTransNotCompleted");
					}else {
						//��ʾ�����õ�limits��¼
						convertPojo2Map(txnLimit, resultData);
						//״̬�����Ƿ�Ա��˺�������һ���ܵ�����
						if (txnType.equals(TxnLimit.TXN_TYPE_ALL)) {
				            corpInfoMap.put("allLimits", txnLimit.getLimit1());					
						}
					}
				} else if (status.equals(Constants.STATUS_PENDING_APPROVAL)){ //��Ȩ�����׳�NTBException
					throw new NTBException("err.bnk.LastTransNotCompleted");
				} else {
					throw new NTBException("err.bnk.UnKnowStatus");
				}
			} else { //��ͨ�˺�
				if (status.equals(Constants.STATUS_PENDING_APPROVAL)){ //��Ȩ�����׳�NTBException
					throw new NTBException("err.bnk.LastTransNotCompleted");
				} else if (status.equals(Constants.STATUS_NORMAL)) {
					//���CORP_PREFERENCE���е���Ȩ���
					if(!this.checkPrefIdInCorpPref(prefId)){
						//��Ȩ�У��׳�NTBException
						throw new NTBException("err.bnk.LastTransNotCompleted");
					}
				} else {
					throw new NTBException("err.bnk.UnKnowStatus");
				}
			}
		}
	}

	/**
	 * ��ͨ�˺ż�����:
	 * 
	 * ���ÿһ����REMOVED��¼
	 * 	|- ÿһ����¼���������������
	 * 		|- 1. ACC_ACCOUNT�˺�
	 * 		|	|- ״̬ΪPENDING_APPROVAL --> �׳�NTBException
	 *		|	|- ״̬ΪNORMAL
	 *		|	|	|	|- CORP_PREFERENCE�����޴˼�¼ --> �׳�NTBException
	 *		|	|	|	|- CORP_PREFERENCE����״̬Ϊ��NORMAL --> �׳�NTBException
	 *		|	|	|	|- CORP_PREFERENCE����״̬ΪNORMAL --> Undo Anything
	 *		|	|- ״̬Ϊ���� --> �׳�NTBException
	 * 		|- 2. ��ͨ�˺�
	 * 			|- �Ǳ��˺� --> Undo Anything
	 * 			|- ���˺�
	 * 				|- ״̬ΪNORMAL
	 * 				|	|- CORP_PREFERENCE�����޴˼�¼ --> �׳�NTBException
	 *				|	|- CORP_PREFERENCE����״̬Ϊ��NORMAL --> �׳�NTBException
	 *				|	|- CORP_PREFERENCE����״̬ΪNORMAL --> convertPojo2Map(txnLimit, resultData);
	 *				|		|- txnType =?= TxnLimit.TXN_TYPE_ALL
	 *				|			|- true	 --> corpInfoMap.put("allLimits", txnLimit.getLimit1());
	 *				|			|- false --> Undo Anything
	 *				|- ״̬ΪPENDING_APPROVAL --> �׳�NTBException
	 * 				|- ״̬Ϊ���� --> �׳�NTBException
	 */	
	public void setResultDataForCommonAcc(String corpId, String account, Map corpInfoMap, Map resultData) throws NTBException {	
		TxnLimit txnLimit = null;
		List list = this.listAccLimitsInfo(corpId, null);
		//��鱾��ҵ����״̬Ϊ��removed��limits��¼
		for (int i=0; i<list.size(); i++) {
			txnLimit = (TxnLimit) list.get(i);
			String tmpAcc = Utils.null2EmptyWithTrim(txnLimit.getAccount());
			String status = Utils.null2EmptyWithTrim(txnLimit.getStatus());
			String prefId = Utils.null2EmptyWithTrim(txnLimit.getPrefId());
			String txnType = Utils.null2EmptyWithTrim(txnLimit.getTxnType());
			//ALL_ACCOUNT�˺����еȴ���Ȩ�ļ�¼���׳�NTBException
			if (tmpAcc.equals(TxnLimit.ACCOUNT_ALL)) {
				if (status.equals(Constants.STATUS_PENDING_APPROVAL)){
					throw new NTBException("err.bnk.LastTransNotCompleted");
				} else if (status.equals(Constants.STATUS_NORMAL)) {
					//���CORP_PREFERENCE���е���Ȩ���
					if(!this.checkPrefIdInCorpPref(prefId)){
						//��Ȩ�У��׳�NTBException
						throw new NTBException("err.bnk.LastTransNotCompleted");
					} else {
					}
				} else {
					throw new NTBException("err.bnk.UnKnowStatus");
				}
			} else {
				//��鱾�˺�״̬
				if (txnLimit.getAccount().equals(account)) {
					//״̬��
					if (status.equals(Constants.STATUS_NORMAL)) {
						//���CORP_PREFERENCE���е���Ȩ���
						if(!this.checkPrefIdInCorpPref(prefId)){
							//��Ȩ�У��׳�NTBException
							throw new NTBException("err.bnk.LastTransNotCompleted");
						} else {
							//��ʾ�����õ�limits��¼
							this.convertPojo2Map(txnLimit, resultData);
							//״̬�����Ƿ�Ա��˺�������һ���ܵ�����
							if (txnType.equals(TxnLimit.TXN_TYPE_ALL)) {
					            corpInfoMap.put("allLimits", txnLimit.getLimit1());					
							}
						}
					} else if (status.equals(Constants.STATUS_PENDING_APPROVAL)) {
						//��Ȩ�У��׳�NTBException
						throw new NTBException("err.bnk.LastTransNotCompleted");
					} else {
						throw new NTBException("err.bnk.UnKnowStatus");
					}
				}
			}
		}
	}

    public void convertPojo2Map(Object pojo, Map valueMap) throws NTBException {
        try {
            valueMap.putAll(BeanUtils.describe(pojo));
        } catch (Exception e) {
            Log.warn("Error reading values from POJO", e);
        }
    }

	public void approve(String txnType, String prefId) throws NTBException {
		
        CorpPreference corpPref = (CorpPreference) corpPreferenceDao.load(CorpPreference.class, prefId);
        //CorpAccount corpAccout = null;
    	if (corpPref == null) {
    		throw new NTBException("err.bnk.NoSuchPreferenceId");
    	} else {
    		//����CORP_PREFERENCE��
    		String oldPrefId = Utils.null2EmptyWithTrim(corpPref.getRelativeId());
    		String corpId = corpPref.getCorpId();
    		//���ɵ�pref��¼��ΪREMOVED״̬
    		if (!oldPrefId.equals("")) {
    	        CorpPreference oldPorpPref = (CorpPreference) corpPreferenceDao.load(CorpPreference.class, oldPrefId);
    	        oldPorpPref.setStatus(Constants.STATUS_REMOVED);
    	        oldPorpPref.setLastUpdateTime(new Date());
    			corpPreferenceDao.update(oldPorpPref);
    			//����ص�TXN_LIMIT��¼��ΪREMOVED״̬
    			TxnLimit oldTxnLimit = viewLimitsDetialByPrefId(oldPrefId);
    			oldTxnLimit.setStatus(Constants.STATUS_REMOVED);
    			oldTxnLimit.setOperation(Constants.OPERATION_REMOVE);
    			this.txnLimitDao.update(oldTxnLimit);
    		}
    		//�����µ�pref��¼
			corpPref.setStatus(Constants.STATUS_NORMAL);
			corpPref.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			corpPref.setLastUpdateTime(new Date());
			corpPreferenceDao.update(corpPref);
			
    		//����TXN_LIMIT��
			TxnLimit txnLimit = viewLimitsDetialByPrefId(prefId);
			String account = Utils.null2EmptyWithTrim(txnLimit.getAccount());
			
			if (account.equals(TxnLimit.ACCOUNT_ALL)) {
				//ALL_ACCOUNT�˺�
				//����ҵ�����˺Ŷ���ΪREMOVED״̬������ALL_ACCOUNT�˺�)
				List list = this.listAccLimitsInfo(corpId, Constants.STATUS_NORMAL);
				TxnLimit txnLimitTmp = null;
				for (int i=0; i<list.size(); i++) {
					txnLimitTmp = (TxnLimit) list.get(i);
					if (!Utils.null2EmptyWithTrim(txnLimitTmp.getAccount()).equals(TxnLimit.ACCOUNT_ALL)) {
						txnLimitTmp.setStatus(Constants.STATUS_REMOVED);
						txnLimitTmp.setOperation(Constants.OPERATION_REMOVE);
						this.txnLimitDao.update(txnLimitTmp);
						//����ص�CORP_PREFEENCE��¼��ΪREMOVED״̬
		    	        CorpPreference corpPrefTmp = (CorpPreference) corpPreferenceDao.load(CorpPreference.class, txnLimitTmp.getPrefId());
		    	        corpPrefTmp.setStatus(Constants.STATUS_REMOVED);
		    	        corpPrefTmp.setLastUpdateTime(new Date());
		    			corpPreferenceDao.update(corpPrefTmp);
					}
				}
			} else {
				//��ͨ�˺�
				//������ҵ��ALL_ACCOUNT�˺���ΪREMOVED״̬
				String hql = "from TxnLimit as t where t.corpId=? and t.account=? and t.status=? and t.operation<>?";
				Map conditionMap = new HashMap();
				conditionMap.put("corpId", corpId);
				conditionMap.put("status", Constants.STATUS_NORMAL);
				conditionMap.put("account", TxnLimit.ACCOUNT_ALL);
				List list = this.txnLimitDao.list(hql, 
						new Object[]{corpId, TxnLimit.ACCOUNT_ALL, Constants.STATUS_NORMAL, Constants.OPERATION_UPDATE_BEFORE});
				TxnLimit txnLimitTmp = null;
				if (list.size()>0) {
					txnLimitTmp = (TxnLimit) list.get(0);
					txnLimitTmp.setStatus(Constants.STATUS_REMOVED);
					txnLimitTmp.setOperation(Constants.OPERATION_REMOVE);
					this.txnLimitDao.update(txnLimitTmp);
					//����ص�CORP_PREFEENCE��¼��ΪREMOVED״̬
	    	        CorpPreference corpPrefTmp = (CorpPreference) corpPreferenceDao.load(CorpPreference.class, txnLimitTmp.getPrefId());
	    	        corpPrefTmp.setStatus(Constants.STATUS_REMOVED);
	    	        corpPrefTmp.setLastUpdateTime(new Date());
	    			corpPreferenceDao.update(corpPrefTmp);
				}				
			}
			//������Ȩ�˺���Ϊ����Ȩ
			txnLimit.setStatus(Constants.STATUS_NORMAL);
			txnLimit.setLastUpdateTime(new Date());
			this.txnLimitDao.update(txnLimit);    		
    	}
		
	}

	public void reject(String txnType, String prefId) throws NTBException {
        CorpPreference corpPref = (CorpPreference) corpPreferenceDao.load(CorpPreference.class, prefId);
    	if (corpPref == null) {
    		throw new NTBException("err.bnk.NoSuchPreferenceId");
    	} else {
    		//����CORP_PREFERENCE��
			corpPref.setStatus(Constants.STATUS_REMOVED);
			corpPref.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
			corpPref.setLastUpdateTime(new Date());
			corpPreferenceDao.update(corpPref);
			//����TXN_LIMIT��
			TxnLimit txnLimit = this.viewLimitsDetialByPrefId(prefId);
			txnLimit.setStatus(Constants.STATUS_REMOVED);
			txnLimit.setOperation(Constants.OPERATION_REMOVE);
			txnLimit.setLastUpdateTime(new Date());
			this.txnLimitDao.update(txnLimit);
    	}		
	}

	public String getLastLimitId(String corpId, String account) throws NTBException {
		String hql = "from TxnLimit tx where tx.corpId = ? and tx.account = ? order by tx.limitId desc";
		List list = this.txnLimitDao.list(hql, new Object[] {corpId, account});
		TxnLimit txnLimit = null;
		if (list.size()>0) {
			txnLimit = viewLimitsDetial(corpId, account, Constants.STATUS_NORMAL);
			if (txnLimit == null) {
				txnLimit = (TxnLimit) list.get(0);
				return txnLimit.getLimitId();
			} else {
				return txnLimit.getLimitId();
			}
		} else {
			return null;
		}
	}
	
	public List listAccount(String corpId) throws NTBException {
		String hql = "from CorpAccount as ca where ca.corpId = ? and ca.status = ? and (ca.accountType = ? or ca.accountType = ? or ca.accountType = ?)";
		List list = this.corpAccountDao.list(
				hql, new Object[] {
						corpId,
						Constants.STATUS_NORMAL,
						CorpAccount.ACCOUNT_TYPE_CURRENT,
						CorpAccount.ACCOUNT_TYPE_OVER_DRAFT,
						CorpAccount.ACCOUNT_TYPE_SAVING } );
		
		return list;
	}
	//add by linrui 20190327
	public List listAccountClass(String corpId) throws NTBException {
		String hql = "from CorpAccount as ca where ca.corpId = ? and ca.status = ? and (ca.accountType = ? or ca.accountType = ? or ca.accountType = ?)";
		List accountList = new ArrayList();
		List accountNoList = new ArrayList();
		List list = this.corpAccountDao.list(
				hql, new Object[] {
						corpId,
						Constants.STATUS_NORMAL,
						CorpAccount.ACCOUNT_TYPE_CURRENT,
						CorpAccount.ACCOUNT_TYPE_OVER_DRAFT,
						CorpAccount.ACCOUNT_TYPE_SAVING } );
		if(list.size()>0){
			for(int i =0; i< list.size(); i++){
				CorpAccount ca = (CorpAccount)list.get(i);
				if(!accountNoList.contains(ca.getAccountNo())){
					accountList.add(ca);
					accountNoList.add(ca.getAccountNo());
				}
			}
		}
		return accountList;
	}
	
	public void add(TxnLimit txnLimit) throws NTBException {
		this.txnLimitDao.add(txnLimit);
	}
	
	public void update(TxnLimit txnLimit) throws NTBException {
		this.txnLimitDao.update(txnLimit);
	}

	public List listLimitsByAfterId(String afterModifyId) throws NTBException {
		if (!Utils.null2EmptyWithTrim(afterModifyId).equals("")){
			Map conditionMap = new HashMap();
			conditionMap.put("afterModifyId", afterModifyId);
			return this.txnLimitDao.list(TxnLimit.class, conditionMap);
		} else {
			Log.warn("afterModifyId is null or empty");
			return null;
		}
	}
	
	public List listLimitsByStatus(String corpId, String status) throws NTBException {
		String hql ="from TxnLimit as t where t.corpId = ? and t.status=? and t.operation<>?" ;
		return this.txnLimitDao.list(hql, new Object[]{corpId, status, Constants.OPERATION_UPDATE_BEFORE});
	}
	
	public Map getOutstandingLimit(TxnLimit txnLimit) throws NTBException {
		Map outstandingLimits = new HashMap();
		String curDate = DateTime.getCurrentDate();
		TxnLimitUsage txnLimitUsage = txnLimitUsageDao.findByCorpAndTxn(txnLimit.getCorpId(), txnLimit.getAccount(), txnLimit.getTxnType());
		if(txnLimitUsage!=null && curDate.equals(txnLimitUsage.getUsageDate())){
			if(!Utils.null2EmptyWithTrim(txnLimit.getTxnType()).equals("")){
				outstandingLimits.put("oLimit", subtract(txnLimit.getLimit1(), txnLimitUsage.getLimit()));
			} else {
				outstandingLimits.put("oLimit1", subtract(txnLimit.getLimit1(), txnLimitUsage.getLimit1()));
				outstandingLimits.put("oLimit2", subtract(txnLimit.getLimit2(), txnLimitUsage.getLimit2()));
				outstandingLimits.put("oLimit3", subtract(txnLimit.getLimit3(), txnLimitUsage.getLimit3()));
				outstandingLimits.put("oLimit4", subtract(txnLimit.getLimit4(), txnLimitUsage.getLimit4()));
				outstandingLimits.put("oLimit5", subtract(txnLimit.getLimit5(), txnLimitUsage.getLimit5()));
				outstandingLimits.put("oLimit6", subtract(txnLimit.getLimit6(), txnLimitUsage.getLimit6()));
				outstandingLimits.put("oLimit7", subtract(txnLimit.getLimit7(), txnLimitUsage.getLimit7()));
				outstandingLimits.put("oLimit8", subtract(txnLimit.getLimit8(), txnLimitUsage.getLimit8()));
				outstandingLimits.put("oLimit9", subtract(txnLimit.getLimit9(), txnLimitUsage.getLimit9()));
				outstandingLimits.put("oLimit10", subtract(txnLimit.getLimit10(), txnLimitUsage.getLimit10()));
			}
		} else {
			if(!Utils.null2EmptyWithTrim(txnLimit.getTxnType()).equals("")){
				outstandingLimits.put("oLimit", txnLimit.getLimit1());
			} else {
				outstandingLimits.put("oLimit1", txnLimit.getLimit1());
				outstandingLimits.put("oLimit2", txnLimit.getLimit2());
				outstandingLimits.put("oLimit3", txnLimit.getLimit3());
				outstandingLimits.put("oLimit4", txnLimit.getLimit4());
				outstandingLimits.put("oLimit5", txnLimit.getLimit5());
				outstandingLimits.put("oLimit6", txnLimit.getLimit6());
				outstandingLimits.put("oLimit7", txnLimit.getLimit7());
				outstandingLimits.put("oLimit8", txnLimit.getLimit8());
				outstandingLimits.put("oLimit9", txnLimit.getLimit9());
				outstandingLimits.put("oLimit10", txnLimit.getLimit10());
			}
		}
		return outstandingLimits;
	}
	
	
	private Double subtract(Double d1, Double d2) {
		return new Double(d1.doubleValue() - d2.doubleValue());
	}
}
