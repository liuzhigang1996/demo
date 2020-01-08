/**
 * @author js
 * 2007-8-6
 */
package app.cib.service.bnk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import app.cib.bo.bnk.Corporation;
import app.cib.bo.bnk.TxnLimit;
import app.cib.bo.sys.CorpUser;
import app.cib.dao.bnk.TxnLimitDao;
import app.cib.service.sys.CorpUserService;
import app.cib.util.Constants;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBAction;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

/**
 * @author js 2007-8-6
 */
public class ConfigCheckingServiceImpl implements ConfigCheckingService {
	//�����Ȩ����
	private static int highestLevel = 7;
	
	private int[] currentRule = null;
	private String currentRuleDesc = "";

	private TxnLimitDao txnLimitDao;
	private GenericJdbcDao genericJdbcDao;

	public TxnLimitDao getTxnLimitDao() {
		return txnLimitDao;
	}

	public void setTxnLimitDao(TxnLimitDao txnLimitDao) {
		this.txnLimitDao = txnLimitDao;
	}

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	public List checkUsersInfo(Corporation corp) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpUserService corpUserService = (CorpUserService) appContext.getBean("corpUserService");

		int reqOprs = 0, reqApprs = 0, reqExecs = 0, reqAdms = 0, reqCtrs = 0;
		int currOprs = 0, currApprs = 0, currExecs = 0, currAdms = 0, currCtrs = 0;

		// get current setting
		List normalUserList = corpUserService.listNormalByCorp(corp.getCorpId());
		CorpUser corpUser = null;
		for (int i = 0; i < normalUserList.size(); i++) {
			corpUser = (CorpUser) normalUserList.get(i);
			if (Constants.ROLE_OPERATOR.equals(corpUser.getRoleId())) {
				currOprs++;
			} else if (Constants.ROLE_APPROVER.equals(corpUser.getRoleId())) {
				currApprs++;
				if("Y".equals(corp.getAllowFinancialController()) &&
						"1".equals(corpUser.getFinancialControllerFlag())){
					currCtrs ++;
				}
			} else if (Constants.ROLE_EXECUTOR.equals(corpUser.getRoleId())) {
				currExecs++;
			} else if (Constants.ROLE_ADMINISTRATOR.equals(corpUser.getRoleId())) {
				currAdms++;
			}
		}

		// get requried setting and check result
		String result = Result_Iffy;
		if (Constants.CORP_TYPE_LARGE.equals(corp.getCorpType())) {
			reqOprs = 1;
			reqApprs = 2;
			if(Constants.YES.equals(corp.getAllowExecutor())){
				reqExecs = 1;
			} else {
				reqExecs = 0;
			}
			reqAdms = 2;
			if (currOprs >= reqOprs && currApprs >= reqApprs
					&& currExecs >= reqExecs && currAdms >= reqAdms) {
				result = Result_Pass;
			} else {
				result = Result_Iffy;
			}
		} else if (Constants.CORP_TYPE_MIDDLE.equals(corp.getCorpType())) {
			reqOprs = 0;
			reqApprs = 1;
			reqExecs = 0;
			reqAdms = 1;
			if (currOprs == reqOprs && currApprs >= reqApprs
					&& currExecs == reqExecs && currAdms >= reqAdms) {
				result = Result_Pass;
			} else {
				result = Result_Iffy;
			}
		} else if (Constants.CORP_TYPE_SMALL.equals(corp.getCorpType())) {
			reqOprs = 0;
			reqApprs = 1;
			reqExecs = 0;
			reqAdms = 0;
			if (currOprs == reqOprs && currApprs >= reqApprs
					&& currExecs == reqExecs && currAdms == reqAdms) {
				result = Result_Pass;
			} else {
				result = Result_Iffy;
			}
		} else if (Constants.CORP_TYPE_MIDDLE_NO_ADMIN.equals(corp.getCorpType())) {
			reqOprs = 1;
			reqApprs = 1;
			reqExecs = 0;
			reqAdms = 0;
			if (currOprs >= reqOprs && currApprs >= reqApprs
					&& currExecs == reqExecs && currAdms == reqAdms) {
				result = Result_Pass;
			} else {
				result = Result_Iffy;
			}
		}
		//check financial controller
		if("Y".equals(corp.getAllowFinancialController())){
			reqCtrs = 1;
			if (currCtrs >= reqCtrs) {
				//result = Result_Pass;
			} else {
				result = Result_Iffy;
			}
		}

		List userInfoList = new ArrayList();
		Map usreInfo = new HashMap();
		usreInfo.put("corpType", corp.getCorpType());
		usreInfo.put("allowExecutor", corp.getAllowExecutor());
		usreInfo.put("reqOprs", new Integer(reqOprs));
		usreInfo.put("reqApprs", new Integer(reqApprs));
		usreInfo.put("reqExecs", new Integer(reqExecs));
		usreInfo.put("reqAdms", new Integer(reqAdms));
		usreInfo.put("reqCtrs", new Integer(reqCtrs));
		usreInfo.put("currOprs", new Integer(currOprs));
		usreInfo.put("currApprs", new Integer(currApprs));
		usreInfo.put("currExecs", new Integer(currExecs));
		usreInfo.put("currAdms", new Integer(currAdms));
		usreInfo.put("currCtrs", new Integer(currCtrs));
		usreInfo.put("result", result);
		userInfoList.add(usreInfo);

		return userInfoList;
	}

	public List checkAccLimitsInfo(String corpId, NTBAction action) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TxnLimitService txnLimitService = (TxnLimitService) appContext
				.getBean("TxnLimitService");

		TxnLimit txnLimit = null;
		try {
			txnLimit = txnLimitDao.findByCorp(corpId, TxnLimit.ACCOUNT_ALL);
		} catch (Exception e) {
			Log.error("chec account limits info error", e);
			throw new NTBException("err.sys.DBError");
		}
		List corpAccList = null;
		if (txnLimit == null) {
//			corpAccList = txnLimitService.listAccount(corpId);
			corpAccList = txnLimitService.listAccountClass(corpId);//add by linrui 20190327
			if (corpAccList.isEmpty()) {
				Locale locale = (action.getUser().getLanguage() == null) ? Config.getDefaultLocale() : action.getUser().getLanguage();
				RBFactory rbFactory = RBFactory.getInstance("app.cib.resource.bnk.config_checking", locale.toString());
				
				Map noRecord = new HashMap();
				noRecord.put("accountNo", rbFactory.getString("no_setting"));
				noRecord.put("accountLimitStatus", rbFactory.getString("no_setting"));
				noRecord.put("result", Result_Iffy);
				corpAccList.add(noRecord);
				return corpAccList;
			}
			corpAccList = action.convertPojoList2MapList(corpAccList);

			// ��ʾ�����˺ŵ�Limit״̬
			for (int i = 0; i < corpAccList.size(); i++) {
				Map corpAccMap = ((Map) corpAccList.get(i));
				String accountLimitStatus = txnLimitService.checkLimitsStatus(
						corpId, corpAccMap.get("accountNo").toString());
				corpAccMap.put("accountLimitStatus", accountLimitStatus);
				if (Constants.STATUS_NORMAL.equals(accountLimitStatus)) {
					corpAccMap.put("result", Result_Pass);
				} else {
					corpAccMap.put("result", Result_Iffy);
				}
			}
		} else {
			corpAccList = new ArrayList();

			Locale locale = (action.getUser().getLanguage() == null) ? Config
					.getDefaultLocale() : action.getUser().getLanguage();
			RBFactory rbFactory = RBFactory.getInstance(
					"app.cib.resource.bnk.corp_account", locale.toString());

			Map corpAccMap = new HashMap();
			String allAccountLimitStatus = txnLimitService.checkLimitsStatus(
					corpId, TxnLimit.ACCOUNT_ALL);
			corpAccMap.put("accountNo", rbFactory.getString("all_accounts"));
			corpAccMap.put("accountLimitStatus", allAccountLimitStatus);
			corpAccMap.put("result", Result_Pass);
			corpAccList.add(corpAccMap);
		}
		
		return corpAccList;
	}

	public List checkNoSettingRules(Corporation corp) throws NTBException {
		String sql = "select concat('%1 - ', t2.TXN_TYPE_DESC) as REQUIRED_SETTING, t1.CURRENCY " +
				"from " + 
				"(select distinct CURRENCY, TXN_TYPE from APPROVE_RULE where CORP_ID=? and CURRENCY=? and STATUS=? order by TXN_TYPE) t1 " +//as t1mod by linrui 20180209
				"right join " +
				"(select distinct TXN_TYPE, TXN_TYPE_DESC, TXN_TYPE_INDEX from TXN_SUBTYPE where TXN_TYPE not in('STOP_CHEQUE','CHEQUE_PROTECTION','CHEQUE_BOOK_REQUEST')) t2 " +//as t2mod by linrui 20180209
				"on t1.TXN_TYPE = t2.TXN_TYPE " +
				"where t1.CURRENCY is null " +
				"order by t2.TXN_TYPE_INDEX";

		try {
			List retList = new ArrayList();
			if ("Y".equals(corp.getAuthCurMop())) {
				List mopList = genericJdbcDao.query(Utils.replaceStr(sql, "%1", "MOP"),
						new Object[] {corp.getCorpId(), "MOP", Constants.STATUS_NORMAL});
				retList.addAll(mopList);
			}
			if ("Y".equals(corp.getAuthCurHkd())) {
				List hkdList = genericJdbcDao.query(Utils.replaceStr(sql, "%1", "HKD"), 
						new Object[] {corp.getCorpId(), "HKD", Constants.STATUS_NORMAL});
				retList.addAll(hkdList);
			}
			if ("Y".equals(corp.getAuthCurUsd())) {
				List usdList = genericJdbcDao.query(Utils.replaceStr(sql, "%1", "USD"),
						new Object[] {corp.getCorpId(), "USD", Constants.STATUS_NORMAL});
				retList.addAll(usdList);
			}
			if ("Y".equals(corp.getAuthCurEur())) {
				List eurList = genericJdbcDao.query(Utils.replaceStr(sql, "%1", "EUR"), 
						new Object[] {corp.getCorpId(), "EUR", Constants.STATUS_NORMAL});
				retList.addAll(eurList);
			}
			return retList;
		} catch (Exception e) {
			Log.error("check no setting rule error", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	
	private void setCurrentRule(String corpId) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpUserService corpUserService = (CorpUserService) appContext.getBean("corpUserService");
		
		List approverList = corpUserService.listByRoleId(corpId, Constants.ROLE_APPROVER, Constants.STATUS_NORMAL);
		int[] currRule = new int[highestLevel];
		int level = 0;
		CorpUser approver = null;
		for(int i=0; i<approverList.size(); i++){
			approver = (CorpUser) approverList.get(i);
			level = approver.getAuthLevel().charAt(0)-65;
			currRule[level] += 1;
		}
		this.currentRule = currRule;
		
		String str = "";
		for(int j=0; j<currRule.length; j++){
			if(currRule[j]>0){
				str += String.valueOf((char)(65+j)) + currRule[j] + "-";
			}
		}
		if(str.lastIndexOf("-") != -1 && str.lastIndexOf("-") == str.length()-1){
			str = str.substring(0, str.length()-1);
		}
		this.currentRuleDesc = str;
		
	}

	public List checkNormalRules(Corporation corp, NTBAction action) throws NTBException {
		setCurrentRule(corp.getCorpId());
		
		String sql = "select t1.*,t2.TXN_TYPE_DESC " +
				"from " +
				"(" +
				"select CURRENCY, TXN_TYPE, RULE_TYPE, SINGLE_LEVEL, FROM_AMOUNT, TO_AMOUNT, " +
				"LEVEL_A, LEVEL_B, LEVEL_C, LEVEL_D, LEVEL_E, LEVEL_F, LEVEL_G " +
				"from APPROVE_RULE where CORP_ID=? and CURRENCY=? and STATUS=? " +
				"order by TXN_TYPE" +
				") t1, " +//mod by linrui 20180209
				"(" +
				"select distinct TXN_TYPE, TXN_TYPE_DESC,txn_type_index from TXN_SUBTYPE" +
				") t2 " +//mod by linrui 20180209
				"where t1.TXN_TYPE = t2.TXN_TYPE " +
				"order by t2.TXN_TYPE_INDEX";

		try {
			List retList = new ArrayList();
			if ("Y".equals(corp.getAuthCurMop())) {
				List mopList = genericJdbcDao.query(sql, 
						new Object[] {corp.getCorpId(), "MOP", Constants.STATUS_NORMAL});
				parseRuleList(mopList);
				retList.addAll(mopList);
			}
			if ("Y".equals(corp.getAuthCurHkd())) {
				List hkdList = genericJdbcDao.query(sql, 
						new Object[] {corp.getCorpId(), "HKD", Constants.STATUS_NORMAL});
				parseRuleList(hkdList);
				retList.addAll(hkdList);
			}
			if ("Y".equals(corp.getAuthCurUsd())) {
				List usdList = genericJdbcDao.query(sql, 
						new Object[] {corp.getCorpId(), "USD", Constants.STATUS_NORMAL});
				parseRuleList(usdList);
				retList.addAll(usdList);
			}
			if ("Y".equals(corp.getAuthCurEur())) {
				List eurList = genericJdbcDao.query(sql,
						new Object[] {corp.getCorpId(), "EUR", Constants.STATUS_NORMAL});
				parseRuleList(eurList);
				retList.addAll(eurList);
			}
			if(retList.isEmpty()){
				Locale locale = (action.getUser().getLanguage() == null) ? Config.getDefaultLocale() : action.getUser().getLanguage();
				RBFactory rbFactory = RBFactory.getInstance("app.cib.resource.bnk.config_checking", locale.toString());
				
				Map noRecord = new HashMap();
				noRecord.put("requiredSetting", rbFactory.getString("no_setting"));
				noRecord.put("currentSetting", currentRuleDesc.equals("")?rbFactory.getString("no_setting"):currentRuleDesc);
				noRecord.put("result", Result_Iffy);
				retList.add(noRecord);
			}
			return retList;
		} catch (Exception e) {
			Log.error("check normal rule error", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	
	private void parseRuleList(List ruleList){
		Map row = null;
		String ruleType = "";
		for(int i=0; i<ruleList.size(); i++){
			row = (Map) ruleList.get(i);
			ruleType = (String) row.get("RULE_TYPE");
			if(Constants.RULE_TYPE_SINGLE.equals(ruleType)){
				parseSingleLevelRule(row);
			} else {
				parseMultiLevelRule(row);
			}
			row.put("currentSetting", currentRuleDesc);
		}
	}
	
	private void parseSingleLevelRule(Map rule){
		String reqSetting = rule.get("CURRENCY") +
				" - " + rule.get("TXN_TYPE_DESC") +
				" - " + "Single - " + rule.get("SINGLE_LEVEL");
		rule.put("requiredSetting", reqSetting);

		// A->0, B->1, C->2 ...
		int index = ((int)rule.get("SINGLE_LEVEL").toString().charAt(0)) - 65;
		rule.put("result", checkSingleLevelRule(index));
	}
	
	private String checkSingleLevelRule(int index) {
		if(currentRule[index]>0){
			return Result_Pass;
		} else {
			if(index != 0){
				return checkSingleLevelRule(index-1);
			} else {
				return Result_Iffy;
			}
		}
	}
	
	private void parseMultiLevelRule(Map rule){
		int[] requriedRule = new int[highestLevel];
		String reqSetting = "";
		
		reqSetting = rule.get("CURRENCY")+" - "+rule.get("TXN_TYPE_DESC") + " - ";
		if(rule.get("TO_AMOUNT")==null){
			reqSetting += Format.formatData(rule.get("FROM_AMOUNT").toString(), "AMOUNT", null) +
			" Up" + " - ";
		} else {
			reqSetting += Format.formatData(rule.get("FROM_AMOUNT").toString(), "AMOUNT", null) + 
			" to " + Format.formatData(rule.get("TO_AMOUNT").toString(), "AMOUNT", null) + " - ";
		}
		
		for(int i=0; i<requriedRule.length; i++){
			String level = String.valueOf((char)(65+i));
			//if((new Integer(0)).compareTo((Integer)rule.get("LEVEL_"+level)) != 0){
			if((new Integer(0)).compareTo(Integer.parseInt(rule.get("LEVEL_"+level).toString())) != 0){//mod by linrui 20190617
				reqSetting += level + rule.get("LEVEL_"+level)+"-";
				requriedRule[i] = Integer.parseInt(rule.get("LEVEL_"+level).toString());
			}
		}
		if(reqSetting.lastIndexOf("-") != -1 
				&& reqSetting.lastIndexOf("-") == reqSetting.length()-1){
			reqSetting = reqSetting.substring(0, reqSetting.length()-1);
		}
		rule.put("requiredSetting", reqSetting);
		
		rule.put("result", checkMultiLevelRule(requriedRule));
	}
	
	private String checkMultiLevelRule(int[] requriedRule) {
		int[] currRule = new int[highestLevel];
		System.arraycopy(currentRule, 0, currRule, 0, highestLevel);
		
		int[] result = new int[highestLevel];
		int tmp = 0;
		for(int i=currRule.length-1; i>-1; i--){
			tmp = currRule[i] - requriedRule[i];
			if(tmp>0){
				result[i] = tmp;
			} else {
				if(i!=0){ //δ�ﵽ��A����������ǰ���
					currRule[i-1] = currRule[i-1] + tmp;
					result[i] = 0;
				} else {
					result[i] = tmp;
				}
			}
		}
		if(result[0]<0){
			return Result_Iffy;
		}
		return Result_Pass;
		
	}
	
	public static void main(String[] arg){
		ConfigCheckingServiceImpl clazz = new ConfigCheckingServiceImpl();
		clazz.currentRule = new int[]{3,2,0,0,0,0,0};
		System.out.println(clazz.checkMultiLevelRule(new int[]{2,2,1,0,0,0,0}));
	}

}
