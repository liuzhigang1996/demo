/**
 * @author hjs
 * 2006-7-28
 */
package app.cib.action.bnk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBAction;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.*;

import app.cib.bo.bnk.BankUser;
import app.cib.bo.bnk.Corporation;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpPreference;
import app.cib.bo.sys.CorpUser;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.service.bnk.CorporationService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.CorpPreferenceService;
import app.cib.util.*;

/**
 * @author hjs
 * 2006-7-28
 */
public class CorpAccountAction extends CibAction implements Approvable, PageActionHandler{

	public void listCorpAccountLoad () throws NTBException {
        //锟斤拷锟矫空碉拷 ResultData 锟斤拷锟斤拷锟绞撅拷锟斤拷
		setUsrSessionData(new HashMap(1));
        setResultData(new HashMap(1));
	}
	
	/*
	public void listCorpAccount () throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        CorpAccountService corpAccountService = (CorpAccountService)appContext.getBean("CorpAccountService");
        CorporationService corpService = (CorporationService) appContext.getBean("CorporationService");

        Map hostAccPojoMap = null;
        List onlineAccList = new ArrayList();
        List offlineAccList = new ArrayList();
        //get corp id
        String corpId = this.getParameter("corpId");
        //get corp name
        Corporation corpObj = corpService.view(corpId);
        String corpName = corpObj.getCorpName();
        Map corpInfo = new HashMap();
        corpInfo.put("corpId", corpId);
        corpInfo.put("corpName", corpName);

        if ((corpId == null) || (corpId.equals(""))){
        	throw new NTBException("err.bnk.CorpIdISNullOrEmpty");
        }

        //锟斤拷锟斤拷锟斤拷取锟斤拷锟铰碉拷锟斤拷业锟剿猴拷锟斤拷息
        CorpUser corpUser = new CorpUser();
        corpUser.setUserId(((BankUser)getUser()).getUserId());
        corpUser.setCorpId(corpId);
        hostAccPojoMap = corpAccountService.getHostAccListMap(corpId, corpUser);
    	onlineAccList = (List) hostAccPojoMap.get(CorpAccountService.ACC_LIST_TYPE_ONLINE);
    	offlineAccList = (List) hostAccPojoMap.get(CorpAccountService.ACC_LIST_TYPE_OFFLINE);
        CachedDBRCFactory.addPendingCache("accountName");

        Map resultData = new HashMap();
        resultData.putAll(corpInfo);
        resultData.put("onlineAccList", onlineAccList);
        resultData.put("offlineAccList", offlineAccList);
        setResultData(resultData);

        this.clearUsrSessionDataValue();
        this.setUsrSessionDataValue("corpInfo", corpInfo);
        this.setUsrSessionDataValue("onlineAccList", onlineAccList);
        this.setUsrSessionDataValue("offlineAccList", offlineAccList);
	}
	*/
	public void listCorpAccount () throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        CorpAccountService corpAccountService = 
        	(CorpAccountService) appContext.getBean("CorpAccountService");
        CorporationService corpService = 
        	(CorporationService) appContext.getBean("CorporationService");
        
        //get corp id
        String corpId = this.getParameter("corpId");
        //get corp name
        Corporation corpObj = corpService.view(corpId);
        String corpName = corpObj.getCorpName();

//        List onlineAccList = corpAccountService.listLocalCorpAccount(corpId, Constants.STATUS_NORMAL);
        List onlineAccList = corpAccountService.listLocalCorpAccountMap(corpId, Constants.STATUS_NORMAL);

//        onlineAccList = this.convertPojoList2MapList(onlineAccList);

        Map resultData = new HashMap();
        resultData.put("corpId", corpId);
        resultData.put("corpName", corpName);
        resultData.put("onlineAccList", onlineAccList);
        setResultData(resultData);
	}
	
	public void addAccount() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        CorpAccountService corpAccountService = (CorpAccountService)appContext.getBean("CorpAccountService");

		List onlineAccList = (List) this.getUsrSessionDataValue("onlineAccList");
		List offlineAccList = (List) this.getUsrSessionDataValue("offlineAccList");

		Map corpInfo = (Map) this.getUsrSessionDataValue("corpInfo");

		String[] offlineAcc = this.getParameterValues("offlineAcc");

		//锟斤拷offlineAccList删锟斤拷要online锟斤拷锟剿猴拷
		Map newAccListMap = corpAccountService.getNewAccListMap(onlineAccList, offlineAccList, offlineAcc, CorpAccountService.OPERATION_TYPE_ADD_ACC);
		onlineAccList = (List) newAccListMap.get(CorpAccountService.ACC_LIST_TYPE_ONLINE);
		offlineAccList = (List) newAccListMap.get(CorpAccountService.ACC_LIST_TYPE_OFFLINE);

        Map resultData = new HashMap();
        resultData.putAll(corpInfo);
        resultData.put("onlineAccList", onlineAccList);
        resultData.put("offlineAccList", offlineAccList);
        setResultData(resultData);

        this.clearUsrSessionDataValue();
        this.setUsrSessionDataValue("corpInfo", corpInfo);
        this.setUsrSessionDataValue("onlineAccList", onlineAccList);
        this.setUsrSessionDataValue("offlineAccList", offlineAccList);
	}

	public void removeAccount() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        CorpAccountService corpAccountService = (CorpAccountService)appContext.getBean("CorpAccountService");

		List onlineAccList = (List) this.getUsrSessionDataValue("onlineAccList");
		List offlineAccList = (List) this.getUsrSessionDataValue("offlineAccList");

		Map corpInfo = (Map) this.getUsrSessionDataValue("corpInfo");

		String[] onlineAcc = this.getParameterValues("onlineAcc");

		//锟斤拷onlineAccList删锟斤拷要offline锟斤拷锟剿猴拷
		Map newAccListMap = corpAccountService.getNewAccListMap(onlineAccList, offlineAccList, onlineAcc, CorpAccountService.OPERATION_TYPE_REMOVE_ACC);
		onlineAccList = (List) newAccListMap.get(CorpAccountService.ACC_LIST_TYPE_ONLINE);
		offlineAccList = (List) newAccListMap.get(CorpAccountService.ACC_LIST_TYPE_OFFLINE);

        Map resultData = new HashMap();
        resultData.putAll(corpInfo);
        resultData.put("onlineAccList", onlineAccList);
        resultData.put("offlineAccList", offlineAccList);
        setResultData(resultData);

        this.clearUsrSessionDataValue();
        this.setUsrSessionDataValue("corpInfo", corpInfo);
        this.setUsrSessionDataValue("onlineAccList", onlineAccList);
        this.setUsrSessionDataValue("offlineAccList", offlineAccList);

	}

	public void accountCfm() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        CorpAccountService corpAccService = (CorpAccountService)appContext.getBean("CorpAccountService");
        CorpPreferenceService corpPrefService = (CorpPreferenceService)appContext.getBean("CorpPreferenceService");
		FlowEngineService flowEngineService = (FlowEngineService)appContext.getBean("FlowEngineService");

        //锟斤拷锟斤拷页锟斤拷锟斤拷示锟斤拷Account List
		List onlineAccList = (List) this.getUsrSessionDataValue("onlineAccList");
		List offlineAccList = (List) this.getUsrSessionDataValue("offlineAccList");

		Map corpInfo = (Map) this.getUsrSessionDataValue("corpInfo");
		String corpId = corpInfo.get("corpId").toString();
		String corpName = corpInfo.get("corpName").toString();

		String oldPrefId = "";
		String newPrefId = CibIdGenerator.getIdForOperation("CORP_PREFERENCE");

		//锟铰斤拷一锟斤拷锟斤拷权锟斤拷锟斤拷
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_PREF_ACCOUNT,
				FlowEngineService.TXN_CATEGORY_NONFINANCE,
				CorpAccountAction.class,
				null, 0,
				null, 0,
				0,
				newPrefId,
				null,
				getUser(), null, null, null);

		try {
			
//			List localOnlineAccList = corpAccService.listLocalCorpAccount(corpId, Constants.STATUS_NORMAL);
			List localOnlineAccList = corpAccService.listLocalCorpAccountClass(corpId, Constants.STATUS_NORMAL);
//	        List localOfflineAccList = corpAccService.listLocalCorpAccount(corpId, Constants.STATUS_REMOVED);
	        List localOfflineAccList = corpAccService.listLocalCorpAccountClass(corpId, Constants.STATUS_REMOVED);
	        CorpAccount tmpCorpAccount = null;
	        if (localOnlineAccList.size()>0){
	        	tmpCorpAccount = (CorpAccount) localOnlineAccList.get(0);
	        	oldPrefId = tmpCorpAccount.getPrefId();
	        } else if (localOfflineAccList.size()>0){
	        	tmpCorpAccount = (CorpAccount) localOfflineAccList.get(0);
	        	oldPrefId = tmpCorpAccount.getPrefId();
	        }

	        //锟斤拷锟斤拷锟揭伙拷谓锟斤拷锟斤拷欠锟斤拷锟斤拷锟饺拷晒锟�
	        if (!Utils.null2EmptyWithTrim(oldPrefId).equals("")) {
		        if (!corpPrefService.isAuthorization(oldPrefId)){
		        	throw new NTBException("err.bnk.LastTransNotCompleted");
		        }
	        }

	        //锟斤拷锟斤拷锟斤拷锟斤拷锟剿猴拷状态
	        corpAccService.updateLocalAccount(this.getUser().getUserId(), corpId, onlineAccList, offlineAccList, oldPrefId, newPrefId);

	        Map resultData = new HashMap();
	        resultData.put("corpId", corpId);
	        resultData.put("corpName", corpName);
	        resultData.put("transId", newPrefId);
	        resultData.put("onlineAccList", onlineAccList);
	        resultData.put("offlineAccList", offlineAccList);
	        setResultData(resultData);
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

	public void nameAccountLoad() throws NTBException {
        //锟斤拷锟矫空碉拷 ResultData 锟斤拷锟斤拷锟绞撅拷锟斤拷
        setResultData(new HashMap(1));

        ApplicationContext appContext = Config.getAppContext();
        CorpAccountService corpAccountService = (CorpAccountService)appContext.getBean("CorpAccountService");

		CorpUser corpUser = (CorpUser) this.getUser();

//		List accList = corpAccountService.listLocalCorpAccount(corpUser.getCorpId(), Constants.STATUS_NORMAL);
		//mod by linrui begin 20190402
		List accList = corpAccountService.listLocalCorpAccountClass(corpUser.getCorpId(), Constants.STATUS_NORMAL);
		/*List accList = new ArrayList();
		for(int i=0 ; i<accListDb.size() ; i++){
			CorpAccount corpAcc = new CorpAccount();
			convertMap2Pojo((Map) accListDb.get(i), corpAcc);
			accList.add(corpAcc);
		}*/
		//end
		accList = this.convertPojoList2MapList(accList);

		Map resultData = new HashMap();
		resultData.put("accList", accList);
		setResultData(resultData);

		this.setUsrSessionDataValue("accList", accList);

	}

	public void nameAccount() throws NTBException {
        String[] listAccounts = this.getParameterValues("listAccounts");
		List accList = (List) this.getUsrSessionDataValue("accList");
		List newAccList = new ArrayList();
		String newAccName = "";

		for(int i=0; i<accList.size(); i++){
			CorpAccount corpAcc = new CorpAccount();
			this.convertMap2Pojo((Map) accList.get(i), corpAcc);
			
			for (int j = 0; j < listAccounts.length; j ++){
				//modified by lzg 20190708
				/*if (corpAcc.getAccountNo().equals(listAccounts[j])){
					newAccName = Utils.null2EmptyWithTrim(this.getParameter("accountName_" + corpAcc.getAccountNo()));
					corpAcc.setAccountName(newAccName);									
					corpAcc.setOperation(Constants.OPERATION_UPDATE);
				}*/
				String account = listAccounts[j].split("_")[0];
				String ccy = listAccounts[j].split("_")[1];
				if (corpAcc.getAccountNo().equals(account) && corpAcc.getCurrency().equals(ccy)){
					newAccName = Utils.null2EmptyWithTrim(this.getParameter("accountName_" + corpAcc.getAccountNo() + "_" + corpAcc.getCurrency()));
					corpAcc.setAccountName(newAccName);									
					corpAcc.setOperation(Constants.OPERATION_UPDATE);
				}
				//modified by lzg end
			}
			newAccList.add(corpAcc);
		}
		newAccList = this.convertPojoList2MapList(newAccList);

		Map resultData = new HashMap();
		resultData.put("newAccList", newAccList);
		setResultData(resultData);

		this.setUsrSessionDataValue("newAccList", newAccList);
	}

	public void nameAccountCfm() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        CorpAccountService corpAccService = (CorpAccountService)appContext.getBean("CorpAccountService");

		List newAccList = (List) this.getUsrSessionDataValue("newAccList");

		for(int i=0; i<newAccList.size(); i++){
			CorpAccount corpAccount = new CorpAccount();
			this.convertMap2Pojo((Map) newAccList.get(i), corpAccount);
//			corpAccService.updateLocalAccount(corpAccount);
			corpAccService.updateAcctName(corpAccount.getAccountNo(),corpAccount.getAccountName(),corpAccount.getCurrency());
		}

//		CachedDBRCFactory.addPendingCache("accountName");

		Map resultData = new HashMap();
		resultData.put("transId", ((Map) newAccList.get(0)).get("prefId"));
		resultData.put("newAccList", newAccList);
		setResultData(resultData);
	}

	public void nameAccountCancel() throws NTBException {
		List list = (List) this.getResultData().get("newAccList");
		this.getResultData().put("accList", list);
	}

	public boolean approve(String txnType, String id, CibAction bean) throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        CorpAccountService corpAccService = (CorpAccountService)appContext.getBean("CorpAccountService");
        CorpPreferenceService corpPrefService = (CorpPreferenceService)appContext.getBean("CorpPreferenceService");
        if (txnType.equals(Constants.TXN_SUBTYPE_PREF_ACCOUNT)) {
        	CorpPreference corpPref = corpPrefService.findCorpPrefByID(id);
        	String corpId = corpPref.getCorpId();

        	CorpUser corpUser = new CorpUser();
        	corpUser.setUserId(((BankUser)bean.getUser()).getUserId());
        	corpUser.setCorpId(corpId);
        	corpAccService.approve(txnType, id, corpUser);
        	return true;
        } else {
    		return false;
        }

	}

	public boolean reject(String txnType, String id, CibAction bean) throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        CorpAccountService corpAccService = (CorpAccountService)appContext.getBean("CorpAccountService");

        if (txnType.equals(Constants.TXN_SUBTYPE_PREF_ACCOUNT)) {
        	CorpUser corpUser = new CorpUser();
        	corpUser.setUserId(((BankUser)bean.getUser()).getUserId());
        	corpAccService.reject(txnType, id,  corpUser);
        	return true;
        } else {
    		return false;
        }
	}

	public String viewDetail(String txnType, String id, CibAction bean) throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        CorpAccountService corpAccService = (CorpAccountService)appContext.getBean("CorpAccountService");
        CorpPreferenceService corpPrefService = (CorpPreferenceService)appContext.getBean("CorpPreferenceService");
        CorporationService corpService = (CorporationService) appContext.getBean("CorporationService");

		String viewPageUrl = "";
		Map resultData = bean.getResultData();

        if (txnType.equals(Constants.TXN_SUBTYPE_PREF_ACCOUNT)) {
        	CorpPreference corpPref = corpPrefService.findCorpPrefByID(id);
        	if (corpPref == null) {
        		throw new NTBException("err.bnk.NoSuchPreferenceId");
        	} else {
        		String corpId = corpPref.getCorpId();
        		Map localAccMap = corpAccService.getLocalAccListMap(corpId);
                
            	List onlineAccList = this.convertPojoList2MapList((List) localAccMap.get(CorpAccountService.ACC_LIST_TYPE_ONLINE));
            	List offlineAccList = this.convertPojoList2MapList((List) localAccMap.get(CorpAccountService.ACC_LIST_TYPE_OFFLINE));

                Corporation corpObj = corpService.view(corpId);
                String corpName = corpObj.getCorpName();

                //view detail web page
                viewPageUrl = "/WEB-INF/pages/bank/corp_account/account_list_view.jsp";

                resultData.put("corpId", corpId);
                resultData.put("corpName", corpName);
                resultData.put("onlineAccList", onlineAccList);
                resultData.put("offlineAccList", offlineAccList);
                bean.setResultData(resultData);
        	}
        }

		return viewPageUrl;
	}
	
	public boolean cancel(String txnType, String id, CibAction bean) throws
                NTBException {
		return reject(txnType, id, bean);
    }
	
	// Jet added 2008-07-31
    public void processPageAction(NTBAction action) throws NTBException {
		List accList = (List) action.getUsrSessionDataValue("accList");
        String[] listAccounts = action.getParameterValues("listAccounts");

    	List newAccList = new ArrayList();
		String newAccName = "";

		for(int i=0; i<accList.size(); i++){			
			CorpAccount corpAcc = new CorpAccount();
			action.convertMap2Pojo((Map) accList.get(i), corpAcc);
			for (int j = 0; j < listAccounts.length; j ++){
				if (corpAcc.getAccountNo().equals(listAccounts[j])){
					newAccName = Utils.null2EmptyWithTrim(action.getParameter("accountName_" + corpAcc.getAccountNo()));
					corpAcc.setAccountName(newAccName);									
				}
			}
			newAccList.add(corpAcc);
		}
		newAccList = this.convertPojoList2MapList(newAccList);

		action.getResultData().put("accList", newAccList);
		action.setUsrSessionDataValue("accList", newAccList);
    }
    //add by linrui 20190402
    /*public void convertMap2Pojo(Map corpMap, CorpAccount ca){
    	if(Utils.null2EmptyWithTrim(corpMap.get("ACCOUNTNO").toString())!=""){
    		ca.setAccountNo(corpMap.get("ACCOUNTNO").toString());
    	}
        if(Utils.null2EmptyWithTrim(corpMap.get("CURRENCY").toString())!=""){
        	ca.setCurrency((corpMap.get("CURRENCY").toString()));
    	}
        if(Utils.null2EmptyWithTrim(corpMap.get("ACCOUNTTYPE").toString())!=""){
        	ca.setAccountType(((corpMap.get("ACCOUNTTYPE").toString())));
        }
    }*/
}
