/**
 * @author hjs
 * 2006-8-3
 */
package app.cib.action.bnk;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBWarningException;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bnk.Corporation;
import app.cib.bo.bnk.TxnLimit;
import app.cib.bo.sys.CorpPreference;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.service.bnk.CorpSubsidiaryService;
import app.cib.service.bnk.CorporationService;
import app.cib.service.bnk.TxnLimitService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.sys.CorpPreferenceService;
import app.cib.service.txn.TransferLimitService;
import app.cib.util.*;

/**
 * @author hjs
 * 2006-8-3
 */
public class CorpAccountLimitsAction extends CibAction implements Approvable {
	public void listCorpAccountLoad() throws NTBException {
        //���ÿյ� ResultData �����ʾ���
        setResultData(new HashMap(1));
	}

	public void listCorpAccount() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        TxnLimitService txnLimitService = (TxnLimitService)appContext.getBean("TxnLimitService");
        CorporationService corpService = (CorporationService) appContext.getBean("CorporationService");
        
		String corpId = Utils.null2EmptyWithTrim(this.getParameter("corpId"));
        //get corp name
        Corporation corpObj = corpService.view(corpId);
        String corpName = corpObj.getCorpName();
		Map corpInfoMap = new HashMap();
		corpInfoMap.put("corpId", corpId);
		corpInfoMap.put("corpName", corpName);

//        List corpAccList = txnLimitService.listAccount(corpId);
        List corpAccList = txnLimitService.listAccountClass(corpId);
        if(corpAccList.size()==0){
        	Map resultData = new HashMap();
        	resultData.put("corpId", corpId);
        	resultData.put("corpName", corpName);
        	this.setResultData(resultData);
        	throw new NTBException("err.bnk.ThisCorporationHasNoAnyAccounts");
        }
        corpAccList = this.convertPojoList2MapList(corpAccList);

        //��ʾ�����˺ŵ�Limit״̬
        for(int i=0; i<corpAccList.size(); i++){
        	Map corpAccMap = ((Map)corpAccList.get(i));
        	String accountLimitStatus = txnLimitService.checkLimitsStatus(corpId, corpAccMap.get("accountNo").toString());
        	corpAccMap.put("accountLimitStatus", accountLimitStatus);
        }
        String allAccountLimitStatus = txnLimitService.checkLimitsStatus(corpId, TxnLimit.ACCOUNT_ALL);

        Map resultData = new HashMap();
        resultData.putAll(corpInfoMap);
        resultData.put("corpAccList", corpAccList);
        resultData.put("allAccountLimitStatus", allAccountLimitStatus);
        resultData.put("accountAll", TxnLimit.ACCOUNT_ALL);
        setResultData(resultData);

        this.setUsrSessionDataValue("corpInfoMap", corpInfoMap);
	}

	public void viewAccountLimits() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        TxnLimitService txnLimitService = (TxnLimitService)appContext.getBean("TxnLimitService");

        Map corpInfoMap = (Map) this.getUsrSessionDataValue("corpInfoMap");

        String account = Utils.null2EmptyWithTrim(this.getParameter("account"));
        corpInfoMap.put("account", account);

        //ͨ��allAccounts���ccy
        String allAccounts = Utils.null2EmptyWithTrim(this.getParameter("allAccounts"));
        String currency = txnLimitService.getCurrency(account, allAccounts);
        corpInfoMap.put("currency", currency);

        Map resultData = new HashMap();
        TxnLimit txnLimit = txnLimitService.viewLimitsDetial(corpInfoMap.get("corpId").toString(), account, Constants.STATUS_NORMAL);
        if (txnLimit == null) {
        	throw new NTBException("err.bnk.NoSuchAccountLimitsInfo");
        } else {
			String prefId = Utils.null2EmptyWithTrim(txnLimit.getPrefId());
			String txnType = Utils.null2EmptyWithTrim(txnLimit.getTxnType());
			if(!txnLimitService.checkPrefIdInCorpPref(prefId)){ //״̬Ϊ����ɵ���CORP_PREFERENCE����Ϊδ��Ȩ
				throw new NTBException("err.bnk.LastTransNotCompleted");
			}else {
				//��ʾ�����õ�limits��¼
				this.convertPojo2Map(txnLimit, resultData);
				resultData.putAll(txnLimitService.getOutstandingLimit(txnLimit));
				//״̬�����Ƿ�Ա��˺�������һ���ܵ�����
				if (txnType.equals(TxnLimit.TXN_TYPE_ALL)) {
		            corpInfoMap.put("allLimits", txnLimit.getLimit1());
				}
			}
        }
        resultData.putAll(corpInfoMap);
        setResultData(resultData);

        this.setUsrSessionDataValue("corpInfoMap", corpInfoMap);
	}

	public void setAllAccLinitsLoad() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        TxnLimitService txnLimitService = (TxnLimitService)appContext.getBean("TxnLimitService");
        TransferLimitService transferLimitService = (TransferLimitService) appContext
		.getBean("TransferLimitService");
        
        Map corpInfoMap = (Map) this.getUsrSessionDataValue("corpInfoMap");
		String account = TxnLimit.ACCOUNT_ALL;
		String currency = "MOP";
        corpInfoMap.put("account", account);
        corpInfoMap.put("currency", currency);

        String allAccounts = Utils.null2EmptyWithTrim(this.getParameter("allAccounts"));
        corpInfoMap.put("allAccounts", allAccounts);
      //add by lr for CR 225 2017-3-1 begin
        String dailyLimitString = null;
        Double dailyLimit = 0.0;
        String corpId = (String)corpInfoMap.get("corpId");
        dailyLimit = transferLimitService.getCorpDailyLimit(corpId);
        if(null!=dailyLimit)
        {  
        	//dailyLimitString = dailyLimit.toString();
        	corpInfoMap.put("dailyLimit", dailyLimit);
        }
        //end
		Map resultData = new HashMap();

		//�����Ȩ��������resultData
		txnLimitService.setResultDataForAllAcc(corpInfoMap.get("corpId").toString(), corpInfoMap, resultData);

		resultData.putAll(corpInfoMap);
		setResultData(resultData);

        this.setUsrSessionDataValue("corpInfoMap", corpInfoMap);
	}

	public void setAccountLimitsLoad() throws NTBException{
        ApplicationContext appContext = Config.getAppContext();
        TxnLimitService txnLimitService = (TxnLimitService)appContext.getBean("TxnLimitService"); 
        TransferLimitService transferLimitService = (TransferLimitService) appContext
		.getBean("TransferLimitService");
        Map corpInfoMap = (Map) this.getUsrSessionDataValue("corpInfoMap");    
        String account = Utils.null2EmptyWithTrim(this.getParameter("account"));
        corpInfoMap.put("account", account);
        //add by lr for CR 225 2017-3-1 begin
        String dailyLimitString = null;
        Double dailyLimit = 0.0;
        String corpId = (String)corpInfoMap.get("corpId");
        dailyLimit = transferLimitService.getCorpDailyLimit(corpId);
        if(null!=dailyLimit)
        {  
        	dailyLimitString = dailyLimit.toString();
        	corpInfoMap.put("dailyLimit", dailyLimitString);
        }
        //end
        //�Ƿ�Ϊ����ҵ�����˺���һ���ܵ�LIMITS
        String allAccounts = Utils.null2EmptyWithTrim(this.getParameter("allAccounts"));
        corpInfoMap.put("allAccounts", allAccounts);
        //ͨ��allAccounts���ccy
        String currency = txnLimitService.getCurrency(account, allAccounts);
        corpInfoMap.put("currency", currency);

		Map resultData = new HashMap();

//		�����Ȩ��������resultData
		txnLimitService.setResultDataForCommonAcc(corpInfoMap.get("corpId").toString(), account, corpInfoMap, resultData);

		resultData.putAll(corpInfoMap);
		setResultData(resultData);

        this.setUsrSessionDataValue("corpInfoMap", corpInfoMap);
	}

	public void setAccountLimits() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        TxnLimitService txnLimitService = (TxnLimitService)appContext.getBean("TxnLimitService");
        TransferLimitService transferLimitService = (TransferLimitService) appContext
		.getBean("TransferLimitService");
        Map corpInfoMap = (Map) this.getUsrSessionDataValue("corpInfoMap");
		String allTransType = Utils.null2EmptyWithTrim(this.getParameter("allTransType"));
        //add by lr for cr225 20170303
		Double dailyLimit = 0.0;
        String corpId = (String)corpInfoMap.get("corpId");
        dailyLimit = transferLimitService.getCorpDailyLimit(corpId);
		if (allTransType.equals("checked")){
		Double limit = Double.parseDouble(Utils.nullEmptyToZero(this.getParameter("allLimits")).toString());
		    if(dailyLimit - limit< 0.0){
		    Log.info("dailyLimit is[" + dailyLimit + "]");
			throw new NTBWarningException("err.txn.ExceedDailyLimit");
			}
		}
		else {Double limit1 = Double.parseDouble(Utils.nullEmptyToZero(this.getParameter("limit1")).toString());
		Log.info(" Limit1 is["+limit1+"]and dailyLimit - limit1<0.0?"+((dailyLimit - limit1)<=0.0?1:0));
		Double limit2 = Double.parseDouble(Utils.nullEmptyToZero(this.getParameter("limit2")).toString());
		Log.info(" limit2 is["+limit2+"]and dailyLimit - limit2:"+(dailyLimit - limit2));
		Double limit3 = Double.parseDouble(Utils.nullEmptyToZero(this.getParameter("limit3")).toString());
		Log.info(" limit3 is["+limit3+"]and dailyLimit - limit3:"+(dailyLimit - limit3));
		Double limit4 = Double.parseDouble(Utils.nullEmptyToZero(this.getParameter("limit4")).toString());
		Log.info(" limit4 is["+limit4+"]and dailyLimit - limit4:"+(dailyLimit - limit4));
		Double limit5 = Double.parseDouble(Utils.nullEmptyToZero(this.getParameter("limit5")).toString());
		Log.info(" limit5 is["+limit5+"]and dailyLimit - limit5:"+(dailyLimit - limit5));
		Double limit6 = Double.parseDouble(Utils.nullEmptyToZero(this.getParameter("limit6")).toString());
		Log.info(" limit6 is["+limit6+"]and dailyLimit - limit6:"+(dailyLimit - limit6));
		Double limit7 = Double.parseDouble(Utils.nullEmptyToZero(this.getParameter("limit7")).toString());
		Log.info(" limit7 is["+limit7+"]and dailyLimit - limit7:"+(dailyLimit - limit7));
		Double limit8 = Double.parseDouble(Utils.nullEmptyToZero(this.getParameter("limit8")).toString());
		Log.info(" limit8 is["+limit8+"]and dailyLimit - limit8:"+(dailyLimit - limit8));
		Double limit9 = Double.parseDouble(Utils.nullEmptyToZero(this.getParameter("limit9")).toString());
		Log.info(" limit9 is["+limit9+"]and dailyLimit - limit9:"+(dailyLimit - limit9));
		Double limit10 = Double.parseDouble(Utils.nullEmptyToZero(this.getParameter("limit10")).toString());
		Log.info(" limit10 is["+limit10+"]and dailyLimit - limit10:"+(dailyLimit - limit10));
		    if(dailyLimit - limit1< 0.0||dailyLimit - limit2< 0.0||dailyLimit - limit3< 0.0||dailyLimit - limit4< 0.0||dailyLimit - limit5< 0.0
		    		||dailyLimit - limit6< 0.0||dailyLimit - limit7< 0.0||dailyLimit - limit8< 0.0||dailyLimit - limit9< 0.0
		    		||dailyLimit - limit10< 0.0){
                 throw new NTBWarningException("err.txn.ExceedDailyLimit");
		    	}
		    }
		//end
		//��֯TxnLimit pojo
		TxnLimit txnLimit = null;
        //�ж���new����update
		txnLimit = txnLimitService.viewLimitsDetial(corpInfoMap.get("corpId").toString(),
				corpInfoMap.get("account").toString(), Constants.STATUS_NORMAL);
		if(txnLimit == null) { //new
			txnLimit = new TxnLimit();
			txnLimit.setAccount(corpInfoMap.get("account").toString().trim());
			txnLimit.setOperation(Constants.OPERATION_NEW);
			
		} else { //update
			txnLimit = txnLimitService.viewLimitsDetial(corpInfoMap.get("corpId").toString(),
					corpInfoMap.get("account").toString(), Constants.STATUS_NORMAL);
			txnLimit.setAccount(corpInfoMap.get("account").toString().trim());
			txnLimit.setTxnType(null);
			txnLimit.setOperation(Constants.OPERATION_UPDATE);
		}
		/*
		List recList = txnLimitService.listLimitsByStatus(corpInfoMap.get("corpId").toString(),
				Constants.STATUS_NORMAL);
		TxnLimit lastTxnLimit = null;
		if(recList.size() == 1) {
			lastTxnLimit = (TxnLimit) recList.get(0);
		}
		if (lastTxnLimit == null) { //new
			txnLimit = new TxnLimit();
			txnLimit.setAccount(corpInfoMap.get("account").toString().trim());
			txnLimit.setOperation(Constants.OPERATION_NEW);
		} else {
			if(lastTxnLimit.getAccount().equals(corpInfoMap.get("account"))) { //update
				txnLimit = lastTxnLimit;
				txnLimit.setAccount(corpInfoMap.get("account").toString().trim());
				txnLimit.setTxnType(null);
				txnLimit.setOperation(Constants.OPERATION_UPDATE);

			} else { //new
				txnLimit = new TxnLimit();
				txnLimit.setAccount(corpInfoMap.get("account").toString().trim());
				txnLimit.setOperation(Constants.OPERATION_NEW);
			}
		}
		*/
		txnLimit.setStatus(Constants.STATUS_PENDING_APPROVAL);
                txnLimit.setRequester(this.getUser().getUserId());
		txnLimit.setCorpId(corpInfoMap.get("corpId").toString());
		txnLimit.setCurrency(corpInfoMap.get("currency").toString());

        //�Ƿ�Ϊ���˺���һ���ܵ�LIMITS
		//corpInfoMap.put("allTransType", allTransType);
		if (allTransType.equals("checked")) {
			String allLimits = Utils.null2EmptyWithTrim(this.getParameter("allLimits")).replaceAll(",", "");
			corpInfoMap.put("allLimits", allLimits);
			txnLimit.setTxnType(TxnLimit.TXN_TYPE_ALL);
			txnLimit.setLimit1(new Double(allLimits));
			txnLimit.setLimit2(null);
			txnLimit.setLimit3(null);
			txnLimit.setLimit4(null);
			txnLimit.setLimit5(null);
			txnLimit.setLimit6(null);
			txnLimit.setLimit7(null);
			txnLimit.setLimit8(null);
			txnLimit.setLimit9(null);
			txnLimit.setLimit10(null);
		} else {
			corpInfoMap.put("allLimits", "");
			this.convertMap2Pojo(this.getParameters(), txnLimit);
		}

        Map resultData = new HashMap();
        this.convertPojo2Map(txnLimit, resultData);
        resultData.putAll(corpInfoMap);
        this.setResultData(resultData);

		this.setUsrSessionDataValue("corpInfoMap", corpInfoMap);
		this.setUsrSessionDataValue("txnLimit", txnLimit);
	}

	public void setAccountLimitsCfm() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        TxnLimitService txnLimitService = (TxnLimitService)appContext.getBean("TxnLimitService");
		FlowEngineService flowEngineService = (FlowEngineService)appContext.getBean("FlowEngineService");

        Map corpInfoMap = (Map) this.getUsrSessionDataValue("corpInfoMap");
		TxnLimit txnLimit = (TxnLimit) this.getUsrSessionDataValue("txnLimit");

		String newPrefId = CibIdGenerator.getIdForOperation("CORP_PREFERENCE");
		//�½�һ����Ȩ����
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_PREF_LIMIT,
				FlowEngineService.TXN_CATEGORY_NONFINANCE,
				CorpAccountLimitsAction.class,
				null, 0,
				null, 0,
				0,
				newPrefId,
				null,
				getUser(), null, null, null);

		try {
			//hjs: add before if record exitstent
			List beforeList = null;
			if(txnLimit.getOperation().equals(Constants.OPERATION_NEW)) {
				beforeList = txnLimitService.listLimitsByStatus(corpInfoMap.get("corpId").toString(),
						Constants.STATUS_NORMAL);
				//all -> by account �� by account + by account
				if(!txnLimit.getAccount().equals(TxnLimit.ACCOUNT_ALL)) {
					TxnLimit limit = null;
					if(beforeList.size() > 0){
						limit = (TxnLimit) beforeList.get(0);
					}
					if(limit!=null && TxnLimit.ACCOUNT_ALL.equals(limit.getAccount())) {
						//all -> by account
						beforeList.clear();
						beforeList.add(limit);
					} else { //by account + by account
						beforeList.clear();
					}
				}
				//by account -> all
			} else if(txnLimit.getOperation().equals(Constants.OPERATION_UPDATE)){
				//all -> all �� by account -> by account
				beforeList = new ArrayList();
				beforeList.add(txnLimitService.viewLimitsDetial(txnLimit.getCorpId(),
						txnLimit.getAccount(), Constants.STATUS_NORMAL));
			}
			String[] limitId = CibIdGenerator.getIdsForOperation("TXN_LIMIT", beforeList.size()+1);
			for(int i=0; i<beforeList.size(); i++) {
				TxnLimit txnLimitBefore = (TxnLimit) beforeList.get(i);
				txnLimitBefore.setLimitId(limitId[i]);
				//20070306
                txnLimitBefore.setOperation(Constants.OPERATION_UPDATE_BEFORE);
                txnLimitBefore.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
				txnLimitBefore.setAfterModifyId(limitId[limitId.length-1]);
				txnLimitBefore.setPrefId(newPrefId);
				txnLimitService.add(txnLimitBefore);
			}
			txnLimit.setLimitId(limitId[limitId.length-1]);
			txnLimitService.newLimits(txnLimit, newPrefId);

	        Map resultData = this.getResultData();
	        resultData.put(txnLimit, resultData);
	        resultData.putAll(corpInfoMap);
	        resultData.put("transId", newPrefId);
	        this.setResultData(resultData);
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("err.txn.TranscationFaily", e);
			if (e instanceof NTBException) {
				throw new NTBException(e.getMessage());
			} else {
				throw new NTBException("err.txn.TranscationFaily");
			}
		}

	}

	public void setAccountLimitsCancel() throws NTBException {
	}

	public boolean approve(String txnType, String id, CibAction bean) throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        TxnLimitService txnLimitService = (TxnLimitService)appContext.getBean("TxnLimitService");

        if(txnType.equals(Constants.TXN_SUBTYPE_PREF_LIMIT)){
        	//update before
        	TxnLimit txnLimit = txnLimitService.viewLimitsDetialByPrefId(id);
        	List beforeList = txnLimitService.listLimitsByAfterId(txnLimit.getLimitId());
        	for(int i=0; i<beforeList.size(); i++) {
        		TxnLimit txnLimitBefore = (TxnLimit) beforeList.get(i);
        		txnLimitBefore.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            	txnLimitBefore.setLastUpdateTime(new Date());
            	txnLimitService.update(txnLimitBefore);
        	}

        	//update after
        	txnLimitService.approve(txnType, id);
        	return true;
        } else {
        	return false;
        }
	}

	public boolean reject(String txnType, String id, CibAction bean) throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        TxnLimitService txnLimitService = (TxnLimitService)appContext.getBean("TxnLimitService");

        if(txnType.equals(Constants.TXN_SUBTYPE_PREF_LIMIT)){
        	//update before
        	TxnLimit txnLimit = txnLimitService.viewLimitsDetialByPrefId(id);
        	List beforeList = txnLimitService.listLimitsByAfterId(txnLimit.getLimitId());
        	for(int i=0; i<beforeList.size(); i++) {
        		TxnLimit txnLimitBefore = (TxnLimit) beforeList.get(i);
            	txnLimitBefore.setStatus(Constants.STATUS_REMOVED);
        		txnLimitBefore.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
            	txnLimitBefore.setLastUpdateTime(new Date());
            	txnLimitService.update(txnLimitBefore);
        	}

        	//update after
        	txnLimitService.reject(txnType, id);
        	return true;
        } else {
        	return false;
        }
	}

	public String viewDetail(String txnType, String id, CibAction bean) throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        TxnLimitService txnLimitService = (TxnLimitService)appContext.getBean("TxnLimitService");
        CorpPreferenceService corpPrefService = (CorpPreferenceService)appContext.getBean("CorpPreferenceService");
        CorporationService corpService = (CorporationService)appContext.getBean("CorporationService");

		String viewPageUrl = "";
		Map resultData = bean.getResultData();
        if(txnType.equals(Constants.TXN_SUBTYPE_PREF_LIMIT)){
        	CorpPreference corpPref = corpPrefService.findCorpPrefByID(id);
        	if (corpPref == null) {
        		throw new NTBException("err.bnk.NoSuchPreferenceId");
        	} else {
        		TxnLimit txnLimit = txnLimitService.viewLimitsDetialByPrefId(corpPref.getPrefId());
        		String corpId = txnLimit.getCorpId();
        		Corporation corppration = corpService.view(corpId);
                String corpName = corppration.getCorpName();

                //view detial web page
                viewPageUrl = "/WEB-INF/pages/bank/corp_account/account_limits_set_view.jsp";

                this.convertPojo2Map(txnLimit, resultData);
                resultData.put("corpId", corpId);
                resultData.put("corpName", corpName);
				//״̬�����Ƿ�Ա��˺�������һ���ܵ�����
				if (Utils.null2EmptyWithTrim(txnLimit.getTxnType()).equals(TxnLimit.TXN_TYPE_ALL)) {
					resultData.put("allLimits", txnLimit.getLimit1());
				} else {
					resultData.put("allLimits", null);
				}
                bean.setResultData(resultData);
        	}
        }
		return viewPageUrl;
	}

        public boolean cancel(String txnType, String id, CibAction bean) throws
                NTBException {
            return reject(txnType, id, bean);
    }
}
