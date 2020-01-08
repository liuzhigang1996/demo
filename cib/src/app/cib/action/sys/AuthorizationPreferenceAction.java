package app.cib.action.sys;

/**
 * @author Jet
 *
 * Authorization rules settings
 */
import app.cib.core.CibAction;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;
import java.util.*;

import app.cib.core.*;
import app.cib.bo.bnk.Corporation;
import app.cib.bo.sys.*;
import app.cib.service.bnk.CorporationService;
import app.cib.service.sys.ApproveRuleService;
import app.cib.util.Constants;
import app.cib.service.sys.CorpPreferenceService;
import org.springframework.context.ApplicationContext;
import app.cib.service.flow.FlowEngineService;
import app.cib.util.ErrConstants;

public class AuthorizationPreferenceAction extends CibAction implements
		Approvable {

	public void query() throws NTBException {

		String corpId = this.getParameter("corpId");

		ApplicationContext appContext = Config.getAppContext();
		CorporationService corpService = (CorporationService) appContext
				.getBean("CorporationService");

		ApproveRuleService approveRuleService = (ApproveRuleService) appContext
				.getBean("ApproveRuleService");
		// 閫氳繃corp id璇籇B
		Corporation corpObj = corpService.view(corpId);

		Map resultData = new HashMap();
		this.setResultData(resultData);
		this.convertPojo2Map(corpObj, resultData);

		Map statusMap = approveRuleService.mapRuleStatus(corpObj.getCorpId());

		/*
		 * TRANSFER_BANK TRANSFER_MACAU TRANSFER_OVERSEAS TRANSFER_CORP PAY_BILLS
		 * PAYROLL TIME_DEPOSIT SCHEDULE_TXN
		 */

		// Jet added for all txn type 2008-1-22
		String keyAll = "ALL" + "MOP";
		String statusAll = (String) statusMap.get(keyAll);
		if (statusAll == null) {
			statusAll = "N";
		}
		resultData.put("allTxnTypeStatus", statusAll);

		
		/* Modify by long_zg 2019-05-30 uat-6 begin */
		List curList = getStatusByTxntype(corpObj, statusMap, Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT);
		resultData.put("List_TRANSFER_OWN_ACCOUNT", curList);
		
		curList = getStatusByTxntype(corpObj, statusMap, Constants.TXN_TYPE_TRANSFER_BANK);
		resultData.put("List_TRANSFER_BANK", curList);
		/* Modify by long_zg 2019-05-30 uat-6 end */
		
		curList = getStatusByTxntype(corpObj, statusMap, "TRANSFER_MACAU");
		resultData.put("List_TRANSFER_MACAU", curList);
		curList = getStatusByTxntype(corpObj, statusMap, "TRANSFER_OVERSEAS");
		resultData.put("List_TRANSFER_OVERSEAS", curList);
		curList = getStatusByTxntype(corpObj, statusMap, "TRANSFER_CORP");
		resultData.put("List_TRANSFER_CORP", curList);
		curList = getStatusByTxntype(corpObj, statusMap, "PAY_BILLS");
		resultData.put("List_PAY_BILLS", curList);
		curList = getStatusByTxntype(corpObj, statusMap, "PAYROLL");
		resultData.put("List_PAYROLL", curList);
		curList = getStatusByTxntype(corpObj, statusMap, "TIME_DEPOSIT");
		resultData.put("List_TIME_DEPOSIT", curList);
		curList = getStatusByTxntype(corpObj, statusMap, "SCHEDULE_TXN");
		resultData.put("List_SCHEDULE_TXN", curList);
		curList = getStatusByTxntype(corpObj, statusMap, "BANK_DRAFT");
		resultData.put("List_BANK_DRAFT", curList);
		curList = getStatusByTxntype(corpObj, statusMap, "CASHIER_ORDER");
		resultData.put("List_CASHIER_ORDER", curList);

		// 灏嗕紒涓氫俊鎭啓鍏ession锛屼互渚垮湪setLoad椤甸潰鏄剧ず淇℃伅
		this.setUsrSessionDataValue("corpObj", corpObj);

	}

	public static List getStatusByTxntype(Corporation corpObj, Map statusMap,
			String txnType) {
		ArrayList curList = new ArrayList();
		HashMap cur1 = new HashMap();
		HashMap cur2 = new HashMap();
		HashMap cur3 = new HashMap();
		HashMap cur4 = new HashMap();
		cur1.put("cur", "MOP");
		cur2.put("cur", "HKD");
		cur3.put("cur", "USD");
		cur4.put("cur", "EUR");

		// Jet added for all txn type 2008-1-22
//		String keyAll = "ALL" + "MOP";
//		String statusAll = (String) statusMap.get(keyAll);
//		boolean flagAll = false;
//		if (statusAll != null && statusAll.equals(Constants.STATUS_NORMAL)) {
//			flagAll = true;
//		}

		// 鍔犲伐jsp鏄剧ず鏁版嵁
		int counter = 0;
		// if (!flagAll){
		if ("Y".equals(corpObj.getAuthCurMop())) {
			curList.add(cur1);
			String key = txnType + "MOP";
			String status = (String) statusMap.get(key);
//			if (status == null || flagAll) {
//				status = "N";
//			}
			if (status == null) {
				status = "N";
			}
			cur1.put("status", status);
		}
		// }
		if ("Y".equals(corpObj.getAuthCurHkd())) {
			curList.add(cur2);
			String key = txnType + "HKD";
			String status = (String) statusMap.get(key);
			if (status == null) {
				status = "N";
			}
			cur2.put("status", status);
		}
		if ("Y".equals(corpObj.getAuthCurUsd())) {
			curList.add(cur3);
			String key = txnType + "USD";
			String status = (String) statusMap.get(key);
			if (status == null) {
				status = "N";
			}
			cur3.put("status", status);
		}
		if ("Y".equals(corpObj.getAuthCurEur())) {
			curList.add(cur4);
			String key = txnType + "EUR";
			String status = (String) statusMap.get(key);
			if (status == null) {
				status = "N";
			}
			cur4.put("status", status);
		}
		counter++;

		return curList;
	}

	public void setLoad() throws NTBException {

		ApproveRule approveRule = new ApproveRule();
		this.convertMap2Pojo(this.getParameters(), approveRule);

		ApplicationContext appContext = Config.getAppContext();
		ApproveRuleService approveRuleService = (ApproveRuleService) appContext
				.getBean("ApproveRuleService");

		Map statusMap = approveRuleService.mapRuleStatus(approveRule
				.getCorpId());
		
		// Jet added for all txn type 2008-1-23		
		// status checking
		String keyAll = "ALL" + "MOP";
		String statusAll = (String) statusMap.get(keyAll);

		String key = approveRule.getTxnType() + approveRule.getCurrency();
		String prefStatus = (String) statusMap.get(key);

		boolean flagAllNoSetting = false;
		boolean flagOtherNoSetting = false;
		boolean flagAllNormal = false;
		boolean flagOtherNormal = false;
		boolean flagAllPending = false;
		
		if (statusAll == null){
			flagAllNoSetting = true;
		}
		
		if ((statusAll == null && statusMap.size() == 0) || (statusAll != null && statusMap.size() == 1)){
			flagOtherNoSetting = true;
		}
				
		if (statusAll != null && statusAll.equals(Constants.STATUS_NORMAL)) {
			flagAllNormal = true;
		}
		
		if (((statusAll == null && statusMap.size() == 0) || (statusAll != null && statusMap.size() > 1)) && statusMap.containsValue(Constants.STATUS_NORMAL)){
			flagOtherNormal = true;
		}

		if (statusAll != null && statusAll.equals(Constants.STATUS_PENDING_APPROVAL)) {
			flagAllPending = true;
		}
				
		// select all
		boolean flagSelectAll = false;
		if ("ALL".equals(approveRule.getTxnType())){
			flagSelectAll = true;
		} 
		
		/* 
		 * 鍧囨病鏈塻etting: 涓嶇敤澶勭悊
		 * all no setting: 閫塧ll -> 鍙鏈変竴涓猵ending,reject
		 *                 閫夐潪all > 鐪嬭嚜宸辨槸鍚﹀凡鏈�
		 * 闈瀉ll no setting: 閫塧ll -> 鐪嬭嚜宸�
		 *                   閫夐潪all -> 鍙鏈変竴涓猵ending,reject 
		 * all normal : 閫�all -> 鍙鏈変竴涓猵ending,鍒檙eject
		 *              閫�闈瀉ll -> 鐪嬭嚜宸辨槸鍚﹀凡鏈�
		 * 闈瀉ll normal: 閫�all -> 鍙鏈変竴涓猵ending,鍒檙eject
		 *              閫夐潪all -> 鐪嬭嚜宸辨槸鍚﹀凡鏈夋垨all pending
		 */

		if (flagAllNoSetting && flagSelectAll){
			if (statusMap.containsValue(Constants.STATUS_PENDING_APPROVAL)) {
				throw new NTBException("err.bnk.OperationPending");
			}			
		} else if (flagAllNoSetting && !flagSelectAll){
			if (Constants.STATUS_PENDING_APPROVAL.equals(prefStatus)) {
				throw new NTBException("err.bnk.OperationPending");
			}			
		}
		
		if (flagOtherNoSetting && flagSelectAll){
			if (Constants.STATUS_PENDING_APPROVAL.equals(prefStatus)) {
				throw new NTBException("err.bnk.OperationPending");
			}									
		} else if (flagOtherNoSetting && !flagSelectAll){
			if (statusMap.containsValue(Constants.STATUS_PENDING_APPROVAL)) {
				throw new NTBException("err.bnk.OperationPending");
			}						
		}
		
		if (flagAllNormal && flagSelectAll){
			if (statusMap.containsValue(Constants.STATUS_PENDING_APPROVAL)) {
				throw new NTBException("err.bnk.OperationPending");
			}									
		} else if(flagAllNormal && !flagSelectAll){
			if (Constants.STATUS_PENDING_APPROVAL.equals(prefStatus)) {
				throw new NTBException("err.bnk.OperationPending");
			}									
		}
		
		if (flagOtherNormal && flagSelectAll){
			if (statusMap.containsValue(Constants.STATUS_PENDING_APPROVAL)) {
				throw new NTBException("err.bnk.OperationPending");
			}												
		} else if(flagOtherNormal && !flagSelectAll){
			if (Constants.STATUS_PENDING_APPROVAL.equals(prefStatus) || flagAllPending) {
				throw new NTBException("err.bnk.OperationPending");
			}												
		}
		
//		// 闈瀉ll 杞�all, 鍙鍘熸潵鏈変竴涓猵ending status,鍒欎笉鍙浆
//		if ("ALL".equals(approveRule.getTxnType()) && !flagAll) {
//			if (statusMap.containsValue(Constants.STATUS_PENDING_APPROVAL)) {
//				throw new NTBException("err.bnk.OperationPending");
//			}
//		}
//		// all 杞潪 all, all涓簆ending status锛屼笉鍙浆
//		else if (!"ALL".equals(approveRule.getTxnType()) && flagAll) {
//			if ((Constants.STATUS_PENDING_APPROVAL).equals(statusAll)) {
//				throw new NTBException("err.bnk.OperationPending");
//			}
//			// all 杞�all, 闈瀉ll杞潪all
//		} else {
//			if (Constants.STATUS_PENDING_APPROVAL.equals(prefStatus)) {
//				throw new NTBException("err.bnk.OperationPending");
//			}
//		}
								
		// 鍒楀嚭鐘舵�涓烘甯哥殑浜ゆ槗
		approveRule.setStatus(Constants.STATUS_NORMAL);
		List ruleList = approveRuleService.list(approveRule);

		List oldRuleList = new ArrayList();
		// all杞琣ll,all杞潪all
		if (flagAllNormal) {
			oldRuleList = approveRuleService.listAllTxnType(approveRule);
		}
		// 闈瀉ll杞潪all
		else if (!"ALL".equals(approveRule.getTxnType()) && !flagAllNormal) {
			oldRuleList.addAll(ruleList);
		}
		// 闈瀉ll杞琣ll
		else {
			oldRuleList = approveRuleService.listAll(approveRule);
		}

		// 杞崲鍚庡啓鍏�
		ruleList = this.convertPojoList2MapList(ruleList);

		// 鑾峰緱褰撳墠 PrefId
		// all杞潪all, all杞琣ll, 闈瀉ll杞潪all锛�鍘熸潵鏈変竴涓�
		// 闈瀉ll杞琣ll: 鍘熸潵鏈夊涓猵refid,涓嶇煡閬撳啓鍝釜, 鍥犳涓嶅啓
		String prefId = null;
//		List oldRuleList_temp = new ArrayList();
//		oldRuleList_temp = this.convertPojoList2MapList(oldRuleList);
//		
//		if (oldRuleList_temp.size() > 0){
			// all杞琣ll,all杞潪all,闈瀉ll杞潪all
			if (flagAllNormal || (!"ALL".equals(approveRule.getTxnType()) && !flagAllNormal)) {
//				prefId = (String)((HashMap) oldRuleList_temp.get(0)).get("prefId");
				for (int i = 0; i < ruleList.size(); i++) {
					HashMap temp_map = new HashMap();
					temp_map = (HashMap) ruleList.get(i);
					temp_map.put("seq", new Integer(i));
					prefId = (String) temp_map.get("prefId");
				}
			}
//		}
		
//		for (int i = 0; i < ruleList.size(); i++) {
//			HashMap temp_map = new HashMap();
//			temp_map = (HashMap) ruleList.get(i);
//			temp_map.put("seq", new Integer(i));
//			prefId = (String) temp_map.get("prefId");
//		}
		
		// all杞潪all, 闈瀉ll杞琣ll
		if ((!"ALL".equals(approveRule.getTxnType()) && flagAllNormal) || ("ALL".equals(approveRule.getTxnType()) && !flagAllNormal)) {
			ruleList.clear();
		}
		
		Map resultData = new HashMap();
		resultData.put("ruleList", ruleList);
		this.setResultData(resultData);

		/* 缁勭粐椤甸潰淇℃伅 */
		Map InfoMap = new HashMap();
		// 浠巗ession涓彇鍥炰紒涓氫俊鎭�
		Corporation corpObj = (Corporation) this
				.getUsrSessionDataValue("corpObj");
		InfoMap.put("corpName", corpObj.getCorpName());
		InfoMap.put("corpId", corpObj.getCorpId());
		InfoMap.put("prefId", prefId);

		// 鍥炴樉涓婇〉鎵��淇℃伅
		InfoMap.put("txnType", approveRule.getTxnType());
		InfoMap.put("currency", approveRule.getCurrency());
		InfoMap.put("prefStatus", prefStatus);
		// ruleType and singleLevel
		if (ruleList.size() > 0) {
			String ruleType = Utils.null2Empty(((Map) ruleList.get(0))
					.get("ruleType"));
			InfoMap.put("ruleType", ruleType);
			if (Constants.RULE_TYPE_SINGLE.equals(ruleType)) {
				InfoMap.put("singleLevel", ((Map) ruleList.get(0))
						.get("singleLevel"));
				ruleList = new ArrayList();
				InfoMap.put("ruleList", ruleList);
			}
		}

		resultData.putAll(InfoMap);

		// save the data in session
		this.setUsrSessionDataValue("ruleList", ruleList);
		this.setUsrSessionDataValue("oldRuleList", oldRuleList);
		this.setUsrSessionDataValue("infoMap", InfoMap);
	}

	
	
	public void setLoadForSupervisor() throws NTBException {

		ApproveRule approveRule = new ApproveRule();
		this.convertMap2Pojo(this.getParameters(), approveRule);

		ApplicationContext appContext = Config.getAppContext();
		ApproveRuleService approveRuleService = (ApproveRuleService) appContext
				.getBean("ApproveRuleService");

		Map statusMap = approveRuleService.mapRuleStatus(approveRule
				.getCorpId());
		
		// Jet added for all txn type 2008-1-23		
		// status checking
		String keyAll = "ALL" + "MOP";
		String statusAll = (String) statusMap.get(keyAll);

		String key = approveRule.getTxnType() + approveRule.getCurrency();
		String prefStatus = (String) statusMap.get(key);

//		boolean flagAllNoSetting = false;
//		boolean flagOtherNoSetting = false;
		boolean flagAllNormal = false;
//		boolean flagOtherNormal = false;
//		boolean flagAllPending = false;
//		
//		if (statusAll == null){
//			flagAllNoSetting = true;
//		}
//		
//		if ((statusAll == null && statusMap.size() == 0) || (statusAll != null && statusMap.size() == 1)){
//			flagOtherNoSetting = true;
//		}
//				
		if (statusAll != null && statusAll.equals(Constants.STATUS_NORMAL)) {
			flagAllNormal = true;
		}
//		
//		if (((statusAll == null && statusMap.size() == 0) || (statusAll != null && statusMap.size() > 1)) && statusMap.containsValue(Constants.STATUS_NORMAL)){
//			flagOtherNormal = true;
//		}
//
//		if (statusAll != null && statusAll.equals(Constants.STATUS_PENDING_APPROVAL)) {
//			flagAllPending = true;
//		}
//				
//		// select all
//		boolean flagSelectAll = false;
//		if ("ALL".equals(approveRule.getTxnType())){
//			flagSelectAll = true;
//		} 
		
		/* 
		 * 鍧囨病鏈塻etting: 涓嶇敤澶勭悊
		 * all no setting: 閫塧ll -> 鍙鏈変竴涓猵ending,reject
		 *                 閫夐潪all > 鐪嬭嚜宸辨槸鍚﹀凡鏈�
		 * 闈瀉ll no setting: 閫塧ll -> 鐪嬭嚜宸�
		 *                   閫夐潪all -> 鍙鏈変竴涓猵ending,reject 
		 * all normal : 閫�all -> 鍙鏈変竴涓猵ending,鍒檙eject
		 *              閫�闈瀉ll -> 鐪嬭嚜宸辨槸鍚﹀凡鏈�
		 * 闈瀉ll normal: 閫�all -> 鍙鏈変竴涓猵ending,鍒檙eject
		 *              閫夐潪all -> 鐪嬭嚜宸辨槸鍚﹀凡鏈夋垨all pending
		 */

//		if (flagAllNoSetting && flagSelectAll){
//			if (statusMap.containsValue(Constants.STATUS_PENDING_APPROVAL)) {
//				throw new NTBException("err.bnk.OperationPending");
//			}			
//		} else if (flagAllNoSetting && !flagSelectAll){
//			if (Constants.STATUS_PENDING_APPROVAL.equals(prefStatus)) {
//				throw new NTBException("err.bnk.OperationPending");
//			}			
//		}
//		
//		if (flagOtherNoSetting && flagSelectAll){
//			if (Constants.STATUS_PENDING_APPROVAL.equals(prefStatus)) {
//				throw new NTBException("err.bnk.OperationPending");
//			}									
//		} else if (flagOtherNoSetting && !flagSelectAll){
//			if (statusMap.containsValue(Constants.STATUS_PENDING_APPROVAL)) {
//				throw new NTBException("err.bnk.OperationPending");
//			}						
//		}
//		
//		if (flagAllNormal && flagSelectAll){
//			if (statusMap.containsValue(Constants.STATUS_PENDING_APPROVAL)) {
//				throw new NTBException("err.bnk.OperationPending");
//			}									
//		} else if(flagAllNormal && !flagSelectAll){
//			if (Constants.STATUS_PENDING_APPROVAL.equals(prefStatus)) {
//				throw new NTBException("err.bnk.OperationPending");
//			}									
//		}
//		
//		if (flagOtherNormal && flagSelectAll){
//			if (statusMap.containsValue(Constants.STATUS_PENDING_APPROVAL)) {
//				throw new NTBException("err.bnk.OperationPending");
//			}												
//		} else if(flagOtherNormal && !flagSelectAll){
//			if (Constants.STATUS_PENDING_APPROVAL.equals(prefStatus) || flagAllPending) {
//				throw new NTBException("err.bnk.OperationPending");
//			}												
//		}
										
		// 鍒楀嚭鐘舵�涓烘甯哥殑浜ゆ槗
		approveRule.setStatus(Constants.STATUS_NORMAL);
		List ruleList = approveRuleService.list(approveRule);

//		List oldRuleList = new ArrayList();
//		// all杞琣ll,all杞潪all
//		if (flagAllNormal) {
//			oldRuleList = approveRuleService.listAllTxnType(approveRule);
//		}
//		// 闈瀉ll杞潪all
//		else if (!"ALL".equals(approveRule.getTxnType()) && !flagAllNormal) {
//			oldRuleList.addAll(ruleList);
//		}
//		// 闈瀉ll杞琣ll
//		else {
//			oldRuleList = approveRuleService.listAll(approveRule);
//		}

		// 杞崲鍚庡啓鍏�
		ruleList = this.convertPojoList2MapList(ruleList);

		// 鑾峰緱褰撳墠 PrefId
		// all杞潪all, all杞琣ll, 闈瀉ll杞潪all锛�鍘熸潵鏈変竴涓�
		// 闈瀉ll杞琣ll: 鍘熸潵鏈夊涓猵refid,涓嶇煡閬撳啓鍝釜, 鍥犳涓嶅啓
		String prefId = null;
//		List oldRuleList_temp = new ArrayList();
//		oldRuleList_temp = this.convertPojoList2MapList(oldRuleList);
//		
//		if (oldRuleList_temp.size() > 0){
			// all杞琣ll,all杞潪all,闈瀉ll杞潪all
			if (flagAllNormal || (!"ALL".equals(approveRule.getTxnType()) && !flagAllNormal)) {
//				prefId = (String)((HashMap) oldRuleList_temp.get(0)).get("prefId");
				for (int i = 0; i < ruleList.size(); i++) {
					HashMap temp_map = new HashMap();
					temp_map = (HashMap) ruleList.get(i);
					temp_map.put("seq", new Integer(i));
					prefId = (String) temp_map.get("prefId");
				}
			}
//		}
		
//		for (int i = 0; i < ruleList.size(); i++) {
//			HashMap temp_map = new HashMap();
//			temp_map = (HashMap) ruleList.get(i);
//			temp_map.put("seq", new Integer(i));
//			prefId = (String) temp_map.get("prefId");
//		}
		
		// all杞潪all, 闈瀉ll杞琣ll
		if ((!"ALL".equals(approveRule.getTxnType()) && flagAllNormal) || ("ALL".equals(approveRule.getTxnType()) && !flagAllNormal)) {
			ruleList.clear();
		}
		
		Map resultData = new HashMap();
		resultData.put("ruleList", ruleList);
		this.setResultData(resultData);

		/* 缁勭粐椤甸潰淇℃伅 */
		Map InfoMap = new HashMap();
		// 浠巗ession涓彇鍥炰紒涓氫俊鎭�
		Corporation corpObj = (Corporation) this
				.getUsrSessionDataValue("corpObj");
		InfoMap.put("corpName", corpObj.getCorpName());
		InfoMap.put("corpId", corpObj.getCorpId());
		InfoMap.put("prefId", prefId);

		// 鍥炴樉涓婇〉鎵��淇℃伅
		InfoMap.put("txnType", approveRule.getTxnType());
		InfoMap.put("currency", approveRule.getCurrency());
		InfoMap.put("prefStatus", prefStatus);
		// ruleType and singleLevel
		if (ruleList.size() > 0) {
			String ruleType = Utils.null2Empty(((Map) ruleList.get(0))
					.get("ruleType"));
			InfoMap.put("ruleType", ruleType);
			if (Constants.RULE_TYPE_SINGLE.equals(ruleType)) {
				InfoMap.put("singleLevel", ((Map) ruleList.get(0))
						.get("singleLevel"));
				ruleList = new ArrayList();
				InfoMap.put("ruleList", ruleList);
			}
		}

		resultData.putAll(InfoMap);

		// save the data in session
		this.setUsrSessionDataValue("ruleList", ruleList);
//		this.setUsrSessionDataValue("oldRuleList", oldRuleList);
		this.setUsrSessionDataValue("infoMap", InfoMap);
	}
	
	public void addList() throws NTBException {
		Map tempMap = new HashMap();
		tempMap = (HashMap) this.getUsrSessionDataValue("infoMap");

		List ruleList_old = (List) this.getUsrSessionDataValue("ruleList");
		List ruleList = new ArrayList();

		// refresh the ruleList (because user may update the data)
		if (ruleList_old.size() > 0) {
			for (int i = 0; i < ruleList_old.size(); i++) {
				HashMap row = new HashMap();

				row.put("fromAmount", (this.getParameter("fromAmount"
						+ String.valueOf(i))).replaceAll(",", ""));
				row.put("toAmount", (this.getParameter("toAmount"
						+ String.valueOf(i))).replaceAll(",", ""));
				row.put("levelA", this.getParameter("levelA"
						+ String.valueOf(i)));
				row.put("levelB", this.getParameter("levelB"
						+ String.valueOf(i)));
				row.put("levelC", this.getParameter("levelC"
						+ String.valueOf(i)));
				row.put("levelD", this.getParameter("levelD"
						+ String.valueOf(i)));
				row.put("levelE", this.getParameter("levelE"
						+ String.valueOf(i)));
				row.put("seq", new Integer(i));

				ruleList.add(row);
			}
		}

		// add a new record
		if (ruleList.size() == 0) {
			HashMap first_row = new HashMap();
			first_row.put("seq", new Integer(0));
			first_row.put("fromAmount", new Double(0));
			first_row.put("toAmount", null);
			first_row.put("levelA", new Integer(0));
			first_row.put("levelB", new Integer(0));
			first_row.put("levelC", new Integer(0));
			first_row.put("levelD", new Integer(0));
			first_row.put("levelE", new Integer(0));
			// and the rule type ??????

			ruleList.add(first_row);
		} else if (ruleList.size() > 0) {
			HashMap new_row = new HashMap();
			new_row.put("seq", new Integer(ruleList.size()));
			new_row.put("fromAmount", null);
			new_row.put("toAmount", null);
			new_row.put("levelA", new Integer(0));
			new_row.put("levelB", new Integer(0));
			new_row.put("levelC", new Integer(0));
			new_row.put("levelD", new Integer(0));
			new_row.put("levelE", new Integer(0));
			// and the rule type ??????

			ruleList.add(new_row);
		}

		/* 缁勭粐椤甸潰淇℃伅 */
		tempMap.put("ruleType", Constants.RULE_TYPE_MULTI);

		Map resultData = new HashMap();
		resultData.putAll(tempMap);
		resultData.put("ruleList", ruleList);
		this.setResultData(resultData);

		// reset session data
		this.setUsrSessionDataValue("ruleList", ruleList);
		this.setUsrSessionDataValue("infoMap", tempMap);
	}

	public void deleteList() throws NTBException {

		List ruleList_old = (List) this.getUsrSessionDataValue("ruleList");
		List ruleList_temp = new ArrayList();
		List ruleList = new ArrayList();

		// refresh the ruleList (because user may update the data)
		if (ruleList_old.size() > 0) {
			for (int i = 0; i < ruleList_old.size(); i++) {
				HashMap row = new HashMap();

				if (this.getParameter("fromAmount" + String.valueOf(i)) != null) {
					row.put("fromAmount", (this.getParameter("fromAmount"
							+ String.valueOf(i))).replaceAll(",", ""));
				} else {
					row.put("fromAmount", this.getParameter("fromAmount"
							+ String.valueOf(i)));
				}
				if (this.getParameter("toAmount" + String.valueOf(i)) != null) {
					row.put("toAmount", (this.getParameter("toAmount"
							+ String.valueOf(i))).replaceAll(",", ""));
				} else {
					row.put("toAmount", this.getParameter("toAmount"
							+ String.valueOf(i)));
				}
				row.put("levelA", this.getParameter("levelA"
						+ String.valueOf(i)));
				row.put("levelB", this.getParameter("levelB"
						+ String.valueOf(i)));
				row.put("levelC", this.getParameter("levelC"
						+ String.valueOf(i)));
				row.put("levelD", this.getParameter("levelD"
						+ String.valueOf(i)));
				row.put("levelE", this.getParameter("levelE"
						+ String.valueOf(i)));
				row.put("seq", new Integer(i));

				ruleList_temp.add(row);
			}
		}

		// delete a record
		for (int j = 0; j < ruleList_temp.size(); j++) {
			HashMap temp = new HashMap();
			temp = (HashMap) ruleList_temp.get(j);

			if (temp.get("seq").toString().equals(
					this.getParameter("seqno").toString())) {
				ruleList_temp.remove(j);
				break;
			}
		}

		// reset the field "seq"
		if (ruleList_temp.size() > 0) {
			for (int k = 0; k < ruleList_temp.size(); k++) {
				HashMap temp_row = new HashMap();
				temp_row = (HashMap) ruleList_temp.get(k);

				temp_row.put("seq", new Integer(k));

				ruleList.add(temp_row);
			}
		}

		/* 缁勭粐椤甸潰淇℃伅 */
		Map tempMap = new HashMap();
		tempMap = (HashMap) this.getUsrSessionDataValue("infoMap");
		tempMap.put("ruleType", Constants.RULE_TYPE_MULTI);

		Map resultData = new HashMap();
		resultData.putAll(tempMap);
		resultData.put("ruleList", ruleList);
		this.setResultData(resultData);

		// reset session data
		this.setUsrSessionDataValue("ruleList", ruleList);
		this.setUsrSessionDataValue("infoMap", tempMap);
	}

	public void set() throws NTBException {

		List ruleList_old = (List) this.getUsrSessionDataValue("ruleList");
		Map infoMap = (Map) this.getUsrSessionDataValue("infoMap");
		String corpId = (String) infoMap.get("corpId");
		String txnType = (String) infoMap.get("txnType");
		String currency = (String) infoMap.get("currency");
		String singleLevel = (String) this.getParameter("singleLevel");

		List ruleList = new ArrayList();
		List ruleAllList = null;

		String ruleType = (String) this.getParameter("ruleType");
		if (Constants.RULE_TYPE_MULTI.equals(ruleType)
				&& ruleList_old.size() == 0) {
			throw new NTBException("err.bnk.NoRuleForMultiClass");
		}

		// refresh and prepare the ruleList
		NTBException err = null;

		// Jet added for all txn type 2008-1-22
		if ("ALL".equals(txnType)) {
			ArrayList allTxnTypeList = new ArrayList();
			allTxnTypeList.add("ALL");
			
			/**
			 * Modify by long_zg 2019-05-16 add by long_zg 2019-05-16 for transfer 3rd
			 * 淇敼鎴愪粠Constants鍙栧�
			 * 鍒犻櫎涓嶅啀浣跨敤鐨勬ā鍧桺AY_BILLS,PAYROLL,BANK_DRAFT,CASHIER_ORDER??????
			 * begin
			 */
			/*
			allTxnTypeList.add("TRANSFER_BANK");//mod by linrui 20181211
			allTxnTypeList.add("TRANSFER_MACAU");
			allTxnTypeList.add("TRANSFER_OVERSEAS");
			allTxnTypeList.add("TRANSFER_CORP");
			allTxnTypeList.add("PAY_BILLS");
			allTxnTypeList.add("PAYROLL");
			allTxnTypeList.add("TIME_DEPOSIT");
			allTxnTypeList.add("SCHEDULE_TXN");
			allTxnTypeList.add("BANK_DRAFT");
			allTxnTypeList.add("CASHIER_ORDER");
			*/
			allTxnTypeList.add(Constants.TXN_TYPE_TRANSFER_BANK);
			//add by lzg 20190626
			allTxnTypeList.add(Constants.TXN_TYPE_TRANSFER_BANK_3RD);
			//add by lzg end
			allTxnTypeList.add(Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT);
			allTxnTypeList.add(Constants.TXN_TYPE_TRANSFER_MACAU);
			allTxnTypeList.add(Constants.TXN_TYPE_TRANSFER_OVERSEAS);
			allTxnTypeList.add(Constants.TXN_TYPE_TRANSFER_CORP);
			allTxnTypeList.add(Constants.TXN_TYPE_PAY_BILLS);
			allTxnTypeList.add(Constants.TXN_TYPE_PAYROLL);
			allTxnTypeList.add(Constants.TXN_TYPE_TIME_DEPOSIT);
			allTxnTypeList.add(Constants.TXN_TYPE_SCHEDULE_TXN);
			allTxnTypeList.add(Constants.TXN_TYPE_BANK_DRAFT);
			allTxnTypeList.add(Constants.TXN_TYPE_CASHIER_ORDER);
			/**
			 * Modify by long_zg 2019-05-16 add by long_zg 2019-05-16 for transfer 3rd
			 * 淇敼鎴愪粠Constants鍙栧�
			 * 鍒犻櫎涓嶅啀浣跨敤鐨勬ā鍧桺AY_BILLS,PAYROLL,BANK_DRAFT,CASHIER_ORDER??????
			 * end
			 */

			// add records for every txn type
			for (int k = 0; k < allTxnTypeList.size(); k++) {
				// 鍘熸潵娌℃湁绾綍锛岀洿鎺ュ姞涓�ingle class璁剧疆
				if (ruleList_old.size() == 0) {
					Map row = new HashMap();
					row.put("corpId", corpId);
					row.put("txnType", allTxnTypeList.get(k));
					row.put("currency", currency);
					row.put("ruleType", ruleType);
					row.put("singleLevel", singleLevel);
					row.put("operation", Constants.OPERATION_NEW);
					row.put("status", Constants.STATUS_PENDING_APPROVAL);

					ruleList.add(row);
				}
				// 鍘熸潵宸叉湁绾綍锛屼慨鏀硅缃�
				else if (ruleList_old.size() > 0) {
					String prevToAmount = "0.0";
					for (int i = 0; i < ruleList_old.size(); i++) {
						HashMap row = new HashMap();
						row.put("corpId", corpId);
						row.put("txnType", allTxnTypeList.get(k));
						row.put("currency", currency);
						row.put("ruleType", ruleType);

						row.put("levelF", "");
						row.put("levelG", "");
						row.put("operation", Constants.OPERATION_NEW);
						row.put("status", Constants.STATUS_PENDING_APPROVAL);

						// for multi class authorization
						if (((String) this.getParameter("ruleType"))
								.equals(Constants.RULE_TYPE_MULTI)) {
							String fromAmount = (String) this
									.getParameter("fromAmount"
											+ String.valueOf(i));
							String toAmount = (String) this
									.getParameter("toAmount"
											+ String.valueOf(i));

							if (i > 0
									&& Double.parseDouble(fromAmount) != Double
											.parseDouble(prevToAmount)) {
								err = new NTBException(
										"err.bnk.FormAmountMustEqPrevToAmount");
							}

							if (i == 0 && Double.parseDouble(fromAmount) != 0) {
								err = new NTBException(
										"err.bnk.FirstFromAmountMustZero");
							}

							if (i == ruleList_old.size() - 1
									&& !toAmount.trim().equals("")) {
								err = new NTBException(
										"err.bnk.LastToAmountMustEmpty");
							}

							if (i < ruleList_old.size() - 1
									&& (toAmount.trim().equals("") || toAmount
											.trim().equals(""))) {
								err = new NTBException(
										"err.bnk.AmountMustInput");
							}

							if (i < ruleList_old.size() - 1
									&& (Double.parseDouble(fromAmount) >= Double
											.parseDouble(toAmount))) {
								err = new NTBException(
										"err.bnk.ToAmountMustLarger");
							}

							String levelA = (String) this.getParameter("levelA"
									+ String.valueOf(i));
							String levelB = (String) this.getParameter("levelB"
									+ String.valueOf(i));
							String levelC = (String) this.getParameter("levelC"
									+ String.valueOf(i));
							String levelD = (String) this.getParameter("levelD"
									+ String.valueOf(i));
							String levelE = (String) this.getParameter("levelE"
									+ String.valueOf(i));

							row.put("fromAmount", fromAmount);
							if ("".equals(toAmount)) {
								row.put("toAmount", null);
							} else {
								row.put("toAmount", toAmount);
							}
							row.put("levelA", levelA);
							row.put("levelB", levelB);
							row.put("levelC", levelC);
							row.put("levelD", levelD);
							row.put("levelE", levelE);
							row.put("seq", new Integer(i));

							ruleList.add(row);
							prevToAmount = toAmount;
						}
						// for single class authorization
						else if (((String) this.getParameter("ruleType"))
								.equals(Constants.RULE_TYPE_SINGLE)) {
							singleLevel = (String) getParameter("singleLevel");
							row.put("singleLevel", singleLevel);
							ruleList.add(row);
							break;
						}
					}
				}
				if ("ALL".equals(allTxnTypeList.get(k))) {
					ruleAllList = new ArrayList(ruleList);
				}
			}
		} else {
			// 鍘熸潵娌℃湁绾綍锛岀洿鎺ュ姞涓�ingle class璁剧疆
			if (ruleList_old.size() == 0) {
				Map row = new HashMap();
				row.put("corpId", corpId);
				row.put("txnType", txnType);
				row.put("currency", currency);
				row.put("ruleType", ruleType);
				row.put("singleLevel", singleLevel);
				row.put("operation", Constants.OPERATION_NEW);
				row.put("status", Constants.STATUS_PENDING_APPROVAL);

				ruleList.add(row);
			}
			// 鍘熸潵宸叉湁绾綍锛屼慨鏀硅缃�
			else if (ruleList_old.size() > 0) {
				String prevToAmount = "0.0";
				for (int i = 0; i < ruleList_old.size(); i++) {
					HashMap row = new HashMap();
					row.put("corpId", corpId);
					row.put("txnType", txnType);
					row.put("currency", currency);
					row.put("ruleType", ruleType);

					row.put("levelF", "");
					row.put("levelG", "");
					row.put("operation", Constants.OPERATION_NEW);
					row.put("status", Constants.STATUS_PENDING_APPROVAL);

					// for multi class authorization
					if (((String) this.getParameter("ruleType"))
							.equals(Constants.RULE_TYPE_MULTI)) {
						String fromAmount = (String) this
								.getParameter("fromAmount" + String.valueOf(i));
						String toAmount = (String) this.getParameter("toAmount"
								+ String.valueOf(i));

						if (i > 0
								&& Double.parseDouble(fromAmount) != Double
										.parseDouble(prevToAmount)) {
							err = new NTBException(
									"err.bnk.FormAmountMustEqPrevToAmount");
						}

						if (i == 0 && Double.parseDouble(fromAmount) != 0) {
							err = new NTBException(
									"err.bnk.FirstFromAmountMustZero");
						}

						if (i == ruleList_old.size() - 1
								&& !toAmount.trim().equals("")) {
							err = new NTBException(
									"err.bnk.LastToAmountMustEmpty");
						}

						if (i < ruleList_old.size() - 1
								&& (toAmount.trim().equals("") || toAmount
										.trim().equals(""))) {
							err = new NTBException("err.bnk.AmountMustInput");
						}

						if (i < ruleList_old.size() - 1
								&& (Double.parseDouble(fromAmount) >= Double
										.parseDouble(toAmount))) {
							err = new NTBException("err.bnk.ToAmountMustLarger");
						}

						String levelA = (String) this.getParameter("levelA"
								+ String.valueOf(i));
						String levelB = (String) this.getParameter("levelB"
								+ String.valueOf(i));
						String levelC = (String) this.getParameter("levelC"
								+ String.valueOf(i));
						String levelD = (String) this.getParameter("levelD"
								+ String.valueOf(i));
						String levelE = (String) this.getParameter("levelE"
								+ String.valueOf(i));

						row.put("fromAmount", fromAmount);
						row.put("toAmount", toAmount);
						row.put("levelA", levelA);
						row.put("levelB", levelB);
						row.put("levelC", levelC);
						row.put("levelD", levelD);
						row.put("levelE", levelE);
						row.put("seq", new Integer(i));

						ruleList.add(row);
						prevToAmount = toAmount;
					}
					// for single class authorization
					else if (((String) this.getParameter("ruleType"))
							.equals(Constants.RULE_TYPE_SINGLE)) {
						singleLevel = (String) getParameter("singleLevel");
						row.put("singleLevel", singleLevel);
						ruleList.add(row);
						break;
					}
				}
			}
		}// end else all txn type

		/* 缁勭粐椤甸潰淇℃伅 */
		Map tempMap = new HashMap();
		tempMap = (HashMap) this.getUsrSessionDataValue("infoMap");
		tempMap.put("ruleType", ruleType);
		tempMap.put("singleLevel", singleLevel);

		Map resultData = new HashMap();
		resultData.putAll(tempMap);
		if ("ALL".equals(txnType)) {
			resultData.put("ruleList", ruleAllList);
		} else {
			resultData.put("ruleList", ruleList);
		}
		this.setResultData(resultData);


		// 濡傛灉鍑虹幇 field check 閿欒
		if (err != null) {
			throw err;
		}
		
		// reset session data
		this.setUsrSessionDataValue("ruleList", ruleList);
		this.setUsrSessionDataValue("infoMap", tempMap);
	}

	public void setReturn() throws NTBException {
		Map infoMap = (Map) this.getUsrSessionDataValue("infoMap");
		String ruleType = (String) infoMap.get("ruleType");
		if (ruleType.equals("0")) {
			this.getResultData().remove("ruleList");
			this.setUsrSessionDataValue("ruleList", new ArrayList());
		}
	}

	public void setCfm() throws NTBException {

		// 浠巗ession 鑾峰緱鏁版嵁
		Map corpInfoMap = (Map) this.getUsrSessionDataValue("infoMap");
		String corpId = (String) corpInfoMap.get("corpId");
		String oldPrefId = (String) corpInfoMap.get("prefId");
		List ruleList = (List) this.getUsrSessionDataValue("ruleList");

		// 鍒濆鍖朣ervice
		ApplicationContext appContext = Config.getAppContext();
		ApproveRuleService approveRuleService = (ApproveRuleService) appContext
				.getBean("ApproveRuleService");
		CorpPreferenceService corpPrefService = (CorpPreferenceService) appContext
				.getBean("CorpPreferenceService");

		// 妫�煡涓婁竴娆′氦鏄撴槸鍚﹀凡鎺堟潈鎴愬姛
		// 鑾峰緱鏈�柊Version 鐨�CorpPreference
		CorpPreference corpPref = corpPrefService.getLatestPref(oldPrefId);
		// 濡傛灉杩樺湪鎺堟潈澶勭悊杩囩▼涓紝鍒欒璁剧疆涓嶅厑璁镐慨鏀癸紝鎶ラ敊
		if (corpPref != null
				&& corpPref.getStatus().equals(
						Constants.STATUS_PENDING_APPROVAL)) {
			throw new NTBException("err.bnk.OperationPending");
		}

		// 缁勭粐CorpPreference pojo
		String prefId = CibIdGenerator.getIdForOperation("CORP_PREFERENCE");

		// 鍚姩宸ヤ綔娴�
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_PREF_AUTHORIZATION, "0", this.getClass(),
				null, 0, null, 0, 0, prefId, null, getUser(), null, null, null);

		try {
			// 鍐欐暟鎹簱
			corpPref = corpPrefService.findCorpPrefByID(oldPrefId);
			if (corpPref == null) {
				corpPref = new CorpPreference(prefId);
				corpPref.setVersion(new Integer(1));
			} else {
				corpPref.setPrefId(prefId);
				corpPref.setRelativeId(oldPrefId);
				corpPref.setVersion(new Integer(corpPref.getVersion()
						.intValue() + 1));
			}
			corpPref.setLastUpdateTime(new Date());
			corpPref.setRequester(this.getUser().getUserId());
			List ruleObjList = new ArrayList();
			// transfer to POJO Map
			for (int i = 0; i < ruleList.size(); i++) {
				Map approveRule = new HashMap();
				ApproveRule approveRuleObj = new ApproveRule();

				approveRule = (HashMap) ruleList.get(i);
				this.convertMap2Pojo(approveRule, approveRuleObj);

				// Jet modified for all txn type 2008-1-23
				// if (i == ruleList.size() - 1) {
				// approveRuleObj.setToAmount(null);
				// }
				if (Constants.RULE_TYPE_MULTI.equals(approveRuleObj.getRuleType())
						&& (approveRuleObj.getToAmount().compareTo(new Double(0)) == 0)) {
					approveRuleObj.setToAmount(null);
				}

				ruleObjList.add(approveRuleObj);
			}

			// insert data
			List oldRuleList = (List) this
					.getUsrSessionDataValue("oldRuleList");
			approveRuleService.setRules(corpPref, corpId, oldRuleList,
					ruleObjList);
		} catch (Exception e) {
			// 濡傛灉鍑虹幇閿欒锛屼腑姝㈠伐浣滄祦
			flowEngineService.cancelProcess(processId, getUser());
			throw new NTBException(ErrConstants.GENERAL_ERROR);
		}
	}

	public boolean approve(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		ApproveRuleService approveRuleService = (ApproveRuleService) appContext
				.getBean("ApproveRuleService");
		
		approveRuleService.updateForApprove(id);

		return true;
	}

	public boolean reject(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		ApproveRuleService approveRuleService = (ApproveRuleService) appContext
				.getBean("ApproveRuleService");
		approveRuleService.updateForReject(id);

		return true;
	}

	public String viewDetail(String txnType, String id, CibAction bean)
			throws NTBException {

		ApplicationContext appContext = Config.getAppContext();
		ApproveRuleService approveRuleService = (ApproveRuleService) appContext
				.getBean("ApproveRuleService");

		// 鍒楀嚭鐘舵�涓烘甯哥殑浜ゆ槗
		List ruleList = approveRuleService.listByPref(id);
		ruleList = this.convertPojoList2MapList(ruleList);

		// Jet added for txn type 2008-01-22
		boolean flagAll = false;
		List ruleListAll = new ArrayList();
		if (ruleList.size() > 0) {
			for (int j = 0; j < ruleList.size(); j++) {
				String txnTypeAll = (String) ((Map) ruleList.get(j))
						.get("txnType");
				if ("ALL".equals(txnTypeAll)) {
					ruleListAll.add(ruleList.get(j));
					flagAll = true;
				}
			}
		}

		// 鑾峰緱褰撳墠 PrefId
		String corpId = null;
		String txnTypeInPref = null;
		String currency = null;
		if (ruleList.size() > 0) {
			for (int i = 0; i < ruleList.size(); i++) {
				HashMap temp_map = new HashMap();
				temp_map = (HashMap) ruleList.get(i);
				temp_map.put("seq", new Integer(i));
				corpId = (String) temp_map.get("corpId");
				txnTypeInPref = (String) temp_map.get("txnType");
				currency = (String) temp_map.get("currency");
			}
		}

		// Jet added for txn type 2008-01-22
		if (flagAll) {
			for (int i = 0; i < ruleListAll.size(); i++) {
				HashMap temp_map2 = new HashMap();
				temp_map2 = (HashMap) ruleListAll.get(i);
				temp_map2.put("seq", new Integer(i));
				corpId = (String) temp_map2.get("corpId");
				txnTypeInPref = (String) temp_map2.get("txnType");
				currency = (String) temp_map2.get("currency");
			}
		}

		Map resultData = bean.getResultData();
		// Jet modified for all txn type 2008-01-23
		if (flagAll) {
			resultData.put("ruleList", ruleListAll);
		} else {
			resultData.put("ruleList", ruleList);
		}

		/* 缁勭粐椤甸潰淇℃伅 */
		CorporationService corpService = (CorporationService) Config
				.getAppContext().getBean("CorporationService");

	// 閫氳繃corp id璇籇B
		Corporation corpObj = corpService.view(corpId);
		resultData.put("corpName", corpObj.getCorpName());
		resultData.put("corpId", corpObj.getCorpId());
		resultData.put("txnType1", txnTypeInPref);
		resultData.put("currency", currency);

		// ruleType and singleLevel
		if (ruleList.size() > 0) {
			resultData.put("ruleType", ((HashMap) ruleList.get(0))
					.get("ruleType"));
			resultData.put("singleLevel", ((HashMap) ruleList.get(0))
					.get("singleLevel"));
		}

		return "/WEB-INF/pages/sys/authorization_preference/authorization_set_view.jsp";
	}

	public boolean cancel(String txnType, String id, CibAction bean)
			throws NTBException {
		return reject(txnType, id, bean);
	}
	
}
