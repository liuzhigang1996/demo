package app.cib.service.sys;

import java.util.*;

import app.cib.bo.sys.*;
import app.cib.dao.sys.*;
import com.neturbo.set.exception.*;
import com.neturbo.set.core.*;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.ApplicationContext;

import app.cib.service.srv.TxnSubtypeService;
import app.cib.util.Constants;
import app.cib.util.ErrConstants;
import com.neturbo.set.database.GenericJdbcDao;
import app.cib.core.CibIdGenerator;

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
public class ApproveRuleServiceImpl implements ApproveRuleService {
    private ApproveRuleDao approveRuleDao;

    private GenericJdbcDao genericJdbcDao;

    public ApproveRuleServiceImpl() {
    }

    public ApproveRule getApproveRule(String txnType, String corpId,
                                      String currency, double amount,
                                      double amountMopEq) throws NTBException {

        ApproveRule aRule = null;

        aRule = approveRuleDao
                .getApproveRule(txnType, corpId, currency, amount);

        if (null == aRule || null == aRule.getRuleId()) {
            aRule = approveRuleDao.getApproveRule(txnType, corpId, "MOP",
                                                  amountMopEq);
        }

        if (null == aRule || null == aRule.getRuleId()) {
            throw new NTBException("err.flow.ApproveRuleNotDefined");
        }

        return aRule;
    }

    public ApproveRuleDao getApproveRuleDao() {
        return approveRuleDao;
    }

    public GenericJdbcDao getGenericJdbcDao() {
        return genericJdbcDao;
    }

    public void setApproveRuleDao(ApproveRuleDao approveruleDao) {
        this.approveRuleDao = approveruleDao;
    }

    public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
        this.genericJdbcDao = genericJdbcDao;
    }

    // add by jet for authorizatin preference
    public List list(ApproveRule approveRuleObj) throws NTBException {
        List list = null;
        try {
            list = approveRuleDao.getApproveRule(approveRuleObj);
        } catch (Exception e) {
            Log.error("Error listing approve rules", e);
            throw new NTBException("err.sys.ListApproveRuleError");

        }
        return list;
    }

    // Jet add for all txn type 2008-1-23
    public List listAllTxnType(ApproveRule approveRuleObj) throws NTBException {
        List list = null;
        List listTemp = null;
        
        String sql = "SELECT DISTINCT PREF_ID FROM APPROVE_RULE WHERE CORP_ID=? and TXN_TYPE = 'ALL' and (STATUS='0' or STATUS='1')";
//        String sqlList = "SELECT * FROM APPROVE_RULE WHERE CORP_ID=? and PREF_ID =?";

        try {
            listTemp = genericJdbcDao.query(sql, new Object[] {approveRuleObj.getCorpId()});
            if(listTemp.size() >0 ){
            	String prefid = (String) ((Map)listTemp.get(0)).get("PREF_ID");
            	// get all the records for all txn type
            	list = listByPref(prefid);                
            }
        } catch (Exception e) {
            throw new NTBException("err.sys.ListApproveRuleError");
        }
        return list;
    }

    // Jet add for all txn type 2008-1-29
    public List listAll(ApproveRule approveRuleObj) throws NTBException {
        List list = null;
        try {
            list = approveRuleDao.getAll(approveRuleObj);
        } catch (Exception e) {
            Log.error("Error listing approve rules", e);
            throw new NTBException("err.sys.ListApproveRuleError");
        }
        return list;
    }
    
    public List listAll(String corpId) throws NTBException {
        List list = null;
        try {
            list = approveRuleDao.getAll(corpId);
        } catch (Exception e) {
            Log.error("Error listing approve rules", e);
            throw new NTBException("err.sys.ListApproveRuleError");
        }
        return list;
    }

    public List listAllPending(String corpId) throws NTBException {
        List list = null;
        try {
            list = approveRuleDao.getAllPending(corpId);
        } catch (Exception e) {
            Log.error("Error listing approve rules", e);
            throw new NTBException("err.sys.ListApproveRuleError");
        }
        return list;
    }

    public String getCorpId(String prefId) throws NTBException {
        String corpId = null;
    	String sql = "SELECT DISTINCT CORP_ID FROM APPROVE_RULE WHERE PREF_ID=? ";
        try {
            List list = genericJdbcDao.query(sql, new Object[] {prefId});
            if (list.size() > 0){
                Map row = (Map) list.get(0);
                corpId = (String) row.get("CORP_ID");
            }
        } catch (Exception e) {
            Log.error("Error reading approve rule status", e);
            throw new NTBException("err.sys.ListApproveRuleError");
        }
        return corpId;
    }
    
    public List listByPref(String perfId) throws NTBException {
        List list = null;
        try {
            list = approveRuleDao.getApproveRuleByPref(perfId);
        } catch (Exception e) {
            Log.error("Error listing approve rules by Preference ID", e);
            throw new NTBException("err.sys.ListApproveRuleError");

        }
        return list;
    }

    public List listAllByPref(String perfId) throws NTBException {
        List list = null;
        try {
            list = approveRuleDao.getAllApproveRuleByPref(perfId);
        } catch (Exception e) {
            Log.error("Error listing approve rules by Preference ID", e);
            throw new NTBException("err.sys.ListApproveRuleError");

        }
        return list;
    }

    public Map mapRuleStatus(String corpId) throws NTBException {
        String sql = "SELECT DISTINCT CURRENCY, TXN_TYPE, STATUS FROM APPROVE_RULE WHERE CORP_ID=? and (STATUS='0' or STATUS='1')";
        Map map = new HashMap();
        boolean flagAllPending = false;
        boolean flagAllNormal = false;
        try {
            List list = genericJdbcDao.query(sql, new Object[] {corpId});
            // Jet added for all txn type 2008-2-1
            // ��all ת all
            for (int i = 0; i < list.size(); i++) {
                Map row = (Map) list.get(i);
                String txnType = (String) row.get("TXN_TYPE");
                String status = (String) row.get("STATUS");

                if (status.equals(Constants.STATUS_PENDING_APPROVAL) && txnType.equals("ALL")){
                	flagAllPending = true;
                	break;
                }
                if (status.equals(Constants.STATUS_NORMAL) && txnType.equals("ALL")){
                	flagAllNormal = true;
                	break;
                }
                
            }            
            
            for (int i = 0; i < list.size(); i++) {
                Map row = (Map) list.get(i);
                String currency = (String) row.get("CURRENCY");
                String txnType = (String) row.get("TXN_TYPE");
                String status = (String) row.get("STATUS");
                // Jet added for all txn type 2008-2-1
                if (flagAllPending && !txnType.equals("ALL") && status.equals(Constants.STATUS_PENDING_APPROVAL)){
                	continue;
                } else if(flagAllNormal && !txnType.equals("ALL") && status.equals(Constants.STATUS_NORMAL)){
                	continue;                	
                } else {
                    map.put(txnType + currency, status);                	                	
                }
            }
        } catch (Exception e) {
            Log.error("Error reading approve rule status", e);
            throw new NTBException("err.sys.ListApproveRuleError");
        }
        return map;
    }

    // add by jet for authorizatin preference
    public void setRules(CorpPreference corpPref, String corpId, List oldRuleList, List ruleList) throws
            NTBException {

        // д��CorpPreference pojo
        ApplicationContext appContext = Config.getAppContext();
        CorpPreferenceService corpPrefService = (CorpPreferenceService)
                                                appContext
                                                .getBean(
                "CorpPreferenceService");
        corpPref.setCorpId(corpId);
        corpPref.setStatus(Constants.STATUS_PENDING_APPROVAL);
        corpPref.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
        corpPref.setPrefType(CorpPreference.PREF_TYPE_AUTHORIZATION);
        corpPrefService.newPref(corpPref);

        // �� Before ��¼д�� ApproveRuleDao pojo list
        try {
            for (int i = 0; i < oldRuleList.size(); i++) {
                ApproveRule ruleObj = (ApproveRule) oldRuleList.get(i);
                ruleObj.setRuleId(CibIdGenerator.getIdForOperation("APPROVE_RULE_RULE"));
                ruleObj.setPrefId(corpPref.getPrefId());
                ruleObj.setOperation(Constants.OPERATION_UPDATE_BEFORE);
                ruleObj.setStatus(Constants.STATUS_UPDATE_BEFORE);
                ruleObj.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
                ruleObj.setRequester(corpPref.getRequester());
                ruleObj.setLastUpdateTime(corpPref.getLastUpdateTime());
                approveRuleDao.add(ruleObj);
            }
        } catch (Exception e) {
            Log.error("Error setting approve rules", e);
            throw new NTBException("err.sys.SetApproveRuleError");

        }

        // д��ApproveRuleDao pojo list
        String operation = Constants.OPERATION_NEW;
        if(oldRuleList.size() > 0){
            operation = Constants.OPERATION_UPDATE;
        }
        try {
            for (int i = 0; i < ruleList.size(); i++) {
                ApproveRule ruleObj = (ApproveRule) ruleList.get(i);
                ruleObj.setRuleId(CibIdGenerator.getIdForOperation("APPROVE_RULE_RULE"));
                ruleObj.setPrefId(corpPref.getPrefId());
                ruleObj.setOperation(operation);
                ruleObj.setStatus(Constants.STATUS_PENDING_APPROVAL);
                ruleObj.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
                ruleObj.setRequester(corpPref.getRequester());
                ruleObj.setLastUpdateTime(corpPref.getLastUpdateTime());
                approveRuleDao.add(ruleObj);
            }
        } catch (Exception e) {
            Log.error("Error setting approve rules", e);
            throw new NTBException("err.sys.SetApproveRuleError");

        }
    }

    public void updateForApprove(String prefId) throws NTBException {
        try {
            ApplicationContext appContext = Config.getAppContext();
            CorpPreferenceService corpPrefService = (CorpPreferenceService)appContext.getBean("CorpPreferenceService");
            CorpPreference corpPref = corpPrefService.findCorpPrefByID(prefId);
            CorpPreference oldCorpPref = null;
            String oldPrefId = corpPref.getRelativeId();
            if (oldPrefId != null) {
                oldCorpPref = corpPrefService.findCorpPrefByID(oldPrefId);
                oldCorpPref.setStatus(Constants.STATUS_REMOVED);
                corpPrefService.updatePref(oldCorpPref);

                List ruleList = listByPref(oldPrefId);
                for (int i = 0; i < ruleList.size(); i++) {
                    ApproveRule ruleOjb = (ApproveRule) ruleList.get(i);
                    ruleOjb.setStatus(Constants.STATUS_REMOVED);
                    approveRuleDao.update(ruleOjb);
                }
            } else {
            	// ��allתall
            	String corpId = getCorpId(prefId);
            	List rule_temp = listAllPending(corpId);
            	if (rule_temp.size() > 0){
                    List ruleList = listAll(corpId);
                    for (int i = 0; i < ruleList.size(); i++) {
                        ApproveRule ruleOjb = (ApproveRule) ruleList.get(i);
                        ruleOjb.setStatus(Constants.STATUS_REMOVED);
                        approveRuleDao.update(ruleOjb);
                        
                        oldCorpPref = corpPrefService.findCorpPrefByID(ruleOjb.getPrefId());
                        if (oldCorpPref != null){
                        	oldCorpPref.setStatus(Constants.STATUS_REMOVED);
                            corpPrefService.updatePref(oldCorpPref);                        	
                        }
                    }            		            		
            	}
            }
            
            // �޸�
            corpPref.setStatus(Constants.STATUS_NORMAL);
            corpPref.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            corpPref.setLastUpdateTime(new Date());
            corpPrefService.updatePref(corpPref);

            List ruleList = listAllByPref(prefId);
            for (int i = 0; i < ruleList.size(); i++) {
                ApproveRule ruleOjb = (ApproveRule) ruleList.get(i);
                if(ruleOjb.getOperation().equals(Constants.OPERATION_UPDATE_BEFORE)){
                    ruleOjb.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
                    ruleOjb.setLastUpdateTime(new Date());
                }else{
                    ruleOjb.setStatus(Constants.STATUS_NORMAL);
                    ruleOjb.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
                    ruleOjb.setLastUpdateTime(new Date());
                }
                approveRuleDao.update(ruleOjb);
            }
        } catch (Exception e) {
            Log.error("Error updating to table of corporation", e);
            throw new NTBException(ErrConstants.GENERAL_ERROR);
        }
    }

    public void updateForReject(String prefId) throws NTBException {
        try {
            ApplicationContext appContext = Config.getAppContext();
            CorpPreferenceService corpPrefService = (CorpPreferenceService)
                    appContext
                    .getBean("CorpPreferenceService");
            CorpPreference corpPref = corpPrefService.findCorpPrefByID(prefId);

            //�޸�
            corpPref.setStatus(Constants.STATUS_REMOVED);
            corpPref.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
            corpPrefService.updatePref(corpPref);

            List ruleList = listAllByPref(prefId);
            for (int i = 0; i < ruleList.size(); i++) {
                ApproveRule ruleOjb = (ApproveRule) ruleList.get(i);
                ruleOjb.setStatus(Constants.STATUS_REMOVED);
                ruleOjb.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
                approveRuleDao.update(ruleOjb);
            }
        } catch (Exception e) {
            Log.error("Error updating to table of corporation", e);
            throw new NTBException(ErrConstants.GENERAL_ERROR);
        }
    }

    public boolean checkApproveRule(String corpId, Map data) throws
            NTBException {
        boolean flag = false;

        String currencyField = (String) data.get("currencyField");
        String currency = (String) data.get(currencyField);

        String amountField = (String) data.get("amountField");
        Object amount = data.get(amountField);

        String amountMopEqField = (String) data.get("amountMopEqField");
        Object amountMopEq = data.get(amountMopEqField);

        String txnTypeField = (String) data.get("txnTypeField");
        String txnType = (String) data.get(txnTypeField);

        if (null != currencyField && null != currency && null != amountField
            && null != amount && null != amountMopEqField
            && null != amountMopEq && null != txnTypeField
            && null != txnType) {

            TxnSubtypeService txnSubtypeService = (TxnSubtypeService) Config
                                                  .getAppContext().getBean(
                    "TxnSubtypeService");

            TxnSubtype txnSubtype = txnSubtypeService.load(txnType);
            if (null != txnSubtype && null != txnSubtype.getTxnType()) {
                txnType = txnSubtype.getTxnType();
            }

            ApproveRule approveRule = getApproveRule(txnType, corpId, currency,
                    Double.parseDouble(amount.toString()), Double
                    .parseDouble(amountMopEq.toString()));

            if (null != approveRule) {
                flag = true;
            }
        }

        return flag;
    }
    
    public void checkUsability(String corpId, Object[] ccyArray) throws NTBException{
    	String hql = "from ApproveRule as t where t.corpId=? and t.status=? and currency in(";
    	for(int i=0; i<ccyArray.length; i++){
    		hql += "'" + ccyArray[i] + "',";
    	}
    	if(hql.lastIndexOf(",")==hql.length()-1){
    		hql = hql.substring(0, hql.length()-1);
    	}
    	hql += ")";
    	List list = this.approveRuleDao.list(hql, new Object[]{corpId, Constants.STATUS_PENDING_APPROVAL});
    	if(list!=null && list.size()>0){
			throw new NTBException("err.sys.RulePending");
    	}
    }
    
    public void removeApproveRule(String corpId, Object[] ccyArray) throws NTBException {
    	String hql = "from ApproveRule as t where t.corpId=? and t.status=? and currency in(";
    	for(int i=0; i<ccyArray.length; i++){
    		hql += "'" + ccyArray[i] + "',";
    	}
    	if(hql.lastIndexOf(",")==hql.length()-1){
    		hql = hql.substring(0, hql.length()-1);
    	}
    	hql += ")";
    	List list = this.approveRuleDao.list(hql, new Object[]{corpId, Constants.STATUS_NORMAL});
    	if(list!=null){
        	ApproveRule approveRule = null;
        	for(int i=0; i<list.size(); i++){
        		approveRule = (ApproveRule) list.get(i);
    			ApproveRule newApproveRule = new ApproveRule();
    			try {
					BeanUtils.copyProperties(newApproveRule, approveRule);
				} catch (Exception e) {
					Log.error("copy properties error", e);
					throw new NTBException("err.sys.GeneralError");
				}
				String ruleId = CibIdGenerator.getIdForOperation("");
				
				approveRule.setStatus(Constants.STATUS_REMOVED);
				approveRule.setAfterModifyId(ruleId);
				
				newApproveRule.setRuleId(ruleId);
				newApproveRule.setOperation(Constants.OPERATION_REMOVE);
				newApproveRule.setStatus(Constants.STATUS_REMOVED);
				newApproveRule.setLastUpdateTime(new Date());
				
				this.approveRuleDao.update(approveRule);
				this.approveRuleDao.update(newApproveRule);
        	}
    	}
    }

    /*
     * add by hjs 20071024
     * ���½�һ��Corporationʱ�����Corporation TypeΪ2��3�����ѡ��Ļ������ͣ�
     * Ĭ�����Rule TypeδSingle������ΪA��Approve Rule
     */ 
    public void initApproveRule(String requester, String corpId, Object[] ccyArray) throws NTBException {
    	List corpPrefList = new ArrayList();
    	List approveRuleList = new ArrayList();
    	Map map = null;
    	for(int i=0; i<ccyArray.length; i++){
    		map = getInitApproveRule(requester, corpId, ccyArray[i].toString());
    		corpPrefList.addAll((Collection) map.get("corpPrefList"));
    		approveRuleList.addAll((Collection) map.get("approveRuleList"));
    	}
    	CorpPreference corpPref = null;
    	for(int i=0; i<corpPrefList.size(); i++){
    		corpPref = (CorpPreference) corpPrefList.get(i);
    		approveRuleDao.add(corpPref);
    	}
    	ApproveRule approveRule = null;
    	for(int i=0; i<approveRuleList.size(); i++){
    		approveRule = (ApproveRule) approveRuleList.get(i);
    		approveRuleDao.add(approveRule);
    	}
    }

    // add by hjs 20071024
    private Map getInitApproveRule(String requester, String corpId, String ccy) throws NTBException {
    	Map retMap = new HashMap();
    	
    	String[] txnTypeArray = new String[]{
    			Constants.TXN_TYPE_TRANSFER_BANK,
    			Constants.TXN_TYPE_TRANSFER_MACAU,
    			Constants.TXN_TYPE_TRANSFER_OVERSEAS,
    			Constants.TXN_TYPE_TRANSFER_CORP,
    			Constants.TXN_TYPE_PAY_BILLS,
    			Constants.TXN_TYPE_TIME_DEPOSIT,
    			Constants.TXN_TYPE_PAYROLL,
    			Constants.TXN_TYPE_SCHEDULE_TXN,
    			Constants.TXN_TYPE_BANK_DRAFT,
    			Constants.TXN_TYPE_CASHIER_ORDER,
    	};
    	
    	List corpPrefList = new ArrayList();
    	List approveRuleList = new ArrayList();
    	for(int i=0; i<txnTypeArray.length; i++){
        	//CorpPreference
        	CorpPreference corpPreference = new CorpPreference(CibIdGenerator.getIdForOperation(""));
        	corpPreference.setCorpId(corpId);
        	corpPreference.setPrefType(CorpPreference.PREF_TYPE_AUTHORIZATION);
        	corpPreference.setStatus(Constants.STATUS_NORMAL);
        	corpPreference.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        	corpPreference.setVersion(new Integer(1));
        	corpPreference.setLastUpdateTime(new Date());
        	corpPreference.setRequester(requester);
        	corpPrefList.add(corpPreference);
        	
        	//ApproveRule
        	ApproveRule approveRule = new ApproveRule(CibIdGenerator.getIdForOperation(""));
        	approveRule.setCorpId(corpId);
        	approveRule.setPrefId(corpPreference.getPrefId());
        	approveRule.setTxnType(txnTypeArray[i]);
        	approveRule.setCurrency(ccy);
        	approveRule.setRuleType(Constants.RULE_TYPE_SINGLE);
        	approveRule.setSingleLevel("A");
        	approveRule.setOperation(Constants.OPERATION_NEW);
        	approveRule.setStatus(Constants.STATUS_NORMAL);
        	approveRule.setLastUpdateTime(new Date());
        	approveRule.setRequester(requester);
        	approveRule.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        	approveRuleList.add(approveRule);
    	}
    	retMap.put("corpPrefList", corpPrefList);
    	retMap.put("approveRuleList", approveRuleList);
    	
    	return retMap;
    }

}
