/**
 * @author linrui
 * 2019-7-2
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
import app.cib.bo.bnk.CorporationHis;
import app.cib.bo.bnk.SetCurrencyHis;
import app.cib.bo.bnk.TxnLimit;
import app.cib.bo.sys.CorpPreference;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.service.bnk.CorpSubsidiaryService;
import app.cib.service.bnk.CorporationService;
import app.cib.service.bnk.SetCurrencyService;
import app.cib.service.bnk.TxnLimitService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.sys.CorpPreferenceService;
import app.cib.service.txn.TransferLimitService;
import app.cib.util.*;

/**
 * @author linrui
 * 2019-7-2
 */
public class SetCurrencyAction extends CibAction implements Approvable {
	public void setCurrencyLoad() throws NTBException{
		ApplicationContext appContext = Config.getAppContext();
		SetCurrencyService setCurrencyService = (SetCurrencyService)appContext.getBean("SetCurrencyService");
		List currencyList = setCurrencyService.listManageCcy();
		Map resDta = new HashMap();
		resDta.put("currencyList", currencyList);
		this.setUsrSessionData(resDta);
		
		currencyList = this.convertPojoList2MapList(currencyList);
		//put "flag_" + ccyCode to page
		/*Map flagMap = null;
		for(int i =0 ; i < currencyList.size() ; i++){
			flagMap = (Map)currencyList.get(i);
			flagMap.put("flag_" + flagMap.get("ccyCode"), flagMap.get("availableFlag").toString());
		}*/
		
		//end
		Map resultData = new HashMap();
		resultData.put("ccyShowList", currencyList);
		setResultData(resultData);

	}

	public void setCurrency() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        SetCurrencyService setCurrencyService = (SetCurrencyService)appContext.getBean("SetCurrencyService");
        //check if pocess has not been approve
        if(setCurrencyService.listManageCcyPending().size()>0){
        	throw new NTBException("err.bnk.LastTransNotCompleted");
        }
        //start to set currency
        List currencyList = (List) getUsrSessionDataValue("currencyList");
        String availableFlag = null;
        List newCcyList = new ArrayList();
        for(int i=0; i<currencyList.size(); i++){
        	SetCurrencyHis setCurrencyHis = (SetCurrencyHis)currencyList.get(i);
        	availableFlag = Utils.null2EmptyWithTrim(this.getParameter(setCurrencyHis.getCcyCode() + "_" + setCurrencyHis.getAvailableFlag()));
        	setCurrencyHis.setAvailableFlag(availableFlag);
        	newCcyList.add(setCurrencyHis);
        }
//		this.setUsrSessionDataValue("corpInfoMap", corpInfoMap);
        Map resData = new HashMap();
        resData.put("newCcyList", newCcyList);
		this.setUsrSessionData(resData);
		
		newCcyList = this.convertPojoList2MapList(newCcyList);
		Map resultData = new HashMap();
		resultData.put("ccyShowList", newCcyList);
		setResultData(resultData);
	}

	public void setCurrencyConfirm() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
		FlowEngineService flowEngineService = (FlowEngineService)appContext.getBean("FlowEngineService");
		SetCurrencyService setCurrencyService = (SetCurrencyService)appContext.getBean("SetCurrencyService");

		String prefId = CibIdGenerator.getIdForOperation("CORP_PREFERENCE");
		//start pocess mission
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_CONTROL_CCY,
				FlowEngineService.TXN_CATEGORY_NONFINANCE,
				SetCurrencyAction.class,
				null, 0,
				null, 0,
				0,
				prefId,
				null,
				getUser(), null, null, null);

		try {
			//put status in his
			List newCcyList = (List) getUsrSessionDataValue("newCcyList");
			for(int i =0 ; i < newCcyList.size() ; i++){
				SetCurrencyHis setCurrencyHis = (SetCurrencyHis)newCcyList.get(i);
				setCurrencyHis.setStatus(Constants.STATUS_PENDING_APPROVAL);
				setCurrencyService.updateHis(setCurrencyHis);
			}

	        Map resultData = this.getResultData();
	        newCcyList = this.convertPojoList2MapList(newCcyList);
	        resultData.put("ccyShowList", newCcyList);
			setResultData(resultData);
	        
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

	public void setCurrencyCancel() throws NTBException {
		List currencyList = (List) getUsrSessionDataValue("newCcyList");
		Map resDta = new HashMap();
		resDta.put("currencyList", currencyList);
		this.setUsrSessionData(resDta);
		
		currencyList = this.convertPojoList2MapList(currencyList);
		Map resultData = new HashMap();
		resultData.put("ccyShowList", currencyList);
		setResultData(resultData);
	}

	public boolean approve(String txnType, String id, CibAction bean) throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        SetCurrencyService setCurrencyService = (SetCurrencyService)appContext.getBean("SetCurrencyService");
        setCurrencyService.approve();
        return true;
	}

	public boolean reject(String txnType, String id, CibAction bean) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
        SetCurrencyService setCurrencyService = (SetCurrencyService)appContext.getBean("SetCurrencyService");
        setCurrencyService.reject();
        return true;
	}

	public String viewDetail(String txnType, String id, CibAction bean) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		SetCurrencyService setCurrencyService = (SetCurrencyService) Config
				.getAppContext().getBean("SetCurrencyService");
		List setCurrencyHisList = setCurrencyService.listManageCcyPending();
		if (0 == setCurrencyHisList.size()) {
			setCurrencyHisList = setCurrencyService.listManageCcy();
		}
		setCurrencyHisList = this.convertPojoList2MapList(setCurrencyHisList);
		Map resultData = bean.getResultData();
		resultData.put("ccyShowList", setCurrencyHisList);
		bean.setResultData(resultData);

		return "/WEB-INF/pages/sys/corp_info/setCurrencyViewDetail.jsp";
		
	}

        public boolean cancel(String txnType, String id, CibAction bean) throws
                NTBException {
            return reject(txnType, id, bean);
    }
}
