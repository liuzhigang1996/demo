/**
 * @author hjs
 * 2006-8-6
 */
package app.cib.action.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bnk.TxnLimit;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.util.Constants;
import app.cib.service.bnk.TxnLimitService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.ApproveRuleService;
import app.cib.service.sys.CorpUserService;
import app.cib.service.bnk.CorporationService;
import app.cib.bo.bnk.Corporation;
import app.cib.bo.sys.ApproveRule;
import app.cib.bo.sys.UserGroup;
import app.cib.service.sys.GroupService;

/**
 * @author hjs
 * 2006-8-6
 */
public class CorpInfoAction extends CibAction {

    public void viewAccInfoList() throws NTBException {
        //锟斤拷锟矫空碉拷 ResultData 锟斤拷锟斤拷锟绞撅拷锟斤拷
        setUsrSessionData(new HashMap(1));
        setResultData(new HashMap(1));

        ApplicationContext appContext = Config.getAppContext();
        CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");

        CorpUser corpUser = (CorpUser)this.getUser();

//        List onlineAccList = corpAccountService.listLocalCorpAccount(corpUser.getCorpId(), Constants.STATUS_NORMAL);
        List onlineAccList = corpAccountService.listLocalCorpAccountMap(corpUser.getCorpId(), Constants.STATUS_NORMAL);
        //List offlineAccList = corpAccountService.listLocalCorpAccount(corpUser.getCorpId(), Constants.STATUS_REMOVED);

//        onlineAccList = this.convertPojoList2MapList(onlineAccList);
        //offlineAccList = this.convertPojoList2MapList(offlineAccList);

        Map resultData = new HashMap();
        resultData.put("onlineAccList", onlineAccList);
        //resultData.put("offlineAccList", offlineAccList);
        setResultData(resultData);
    }

    public void viewAccLimitsList() throws NTBException {
        //锟斤拷锟矫空碉拷 ResultData 锟斤拷锟斤拷锟绞撅拷锟斤拷
        setUsrSessionData(new HashMap(1));
        setResultData(new HashMap(1));

        ApplicationContext appContext = Config.getAppContext();
        TxnLimitService txnLimitService = (TxnLimitService) appContext.getBean(
                "TxnLimitService");

        CorpUser corpUser = (CorpUser)this.getUser();

//        List corpAccList = txnLimitService.listAccount(corpUser.getCorpId());
        List corpAccList = txnLimitService.listAccountClass(corpUser.getCorpId());//add by linrui 20190327
        if (corpAccList.size() == 0) {
            throw new NTBException("err.bnk.ThisCorporationHasNoAnyAccounts");
        }
        corpAccList = this.convertPojoList2MapList(corpAccList);

        //锟斤拷示锟斤拷锟斤拷锟剿号碉拷Limit状态
        for (int i = 0; i < corpAccList.size(); i++) {
            Map corpAccMap = ((Map) corpAccList.get(i));
            String accountLimitStatus = txnLimitService.checkLimitsStatus(
                    corpUser.getCorpId(), corpAccMap.get("accountNo").toString());
            corpAccMap.put("accountLimitStatus", accountLimitStatus);
        }
        String allAccountLimitStatus = txnLimitService.checkLimitsStatus(
                corpUser.getCorpId(), TxnLimit.ACCOUNT_ALL);

        Map resultData = new HashMap();
        resultData.put("corpAccList", corpAccList);
        resultData.put("allAccountLimitStatus", allAccountLimitStatus);
        resultData.put("accountAll", TxnLimit.ACCOUNT_ALL);
        setResultData(resultData);

    }

    public void viewLimitsDetial() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        TxnLimitService txnLimitService = (TxnLimitService) appContext.getBean(
                "TxnLimitService");

        CorpUser corpUser = (CorpUser)this.getUser();

        String account = Utils.null2EmptyWithTrim(this.getParameter("account"));
        //通锟斤拷allAccounts锟斤拷锟絚cy
        String allAccounts = Utils.null2EmptyWithTrim(this.getParameter(
                "allAccounts"));
        String currency = txnLimitService.getCurrency(account, allAccounts);

        Map resultData = new HashMap();
        TxnLimit txnLimit = txnLimitService.viewLimitsDetial(corpUser.getCorpId(),
                account, Constants.STATUS_NORMAL);
        if (txnLimit == null) {
            throw new NTBException("err.sys.NoSuchAccountLimitsInfo");
        } else {
            String prefId = Utils.null2EmptyWithTrim(txnLimit.getPrefId());
            String txnType = Utils.null2EmptyWithTrim(txnLimit.getTxnType());
            if (!txnLimitService.checkPrefIdInCorpPref(prefId)) { //状态为锟斤拷锟斤拷傻锟斤拷锟紺ORP_PREFERENCE锟斤拷锟斤拷为未锟斤拷权
                throw new NTBException("err.sys.LastTransNotCompleted");
            } else {
                //锟斤拷示锟斤拷锟斤拷锟矫碉拷limits锟斤拷录
                this.convertPojo2Map(txnLimit, resultData);
                //状态锟斤拷锟斤拷锟角凤拷员锟斤拷撕锟斤拷锟斤拷锟斤拷锟揭伙拷锟斤拷艿锟斤拷锟斤拷锟�
                if (txnType.equals(TxnLimit.TXN_TYPE_ALL)) {
                    resultData.put("allLimits", txnLimit.getLimit1());
                }
            }
        }

        this.convertPojo2Map(txnLimit, resultData);
        resultData.put("currency", currency);
        resultData.put("corpType", corpUser.getCorporation().getCorpType());
        setResultData(resultData);
    }

    public void viewAuthPrefList() throws NTBException {
        //锟斤拷锟矫空碉拷 ResultData 锟斤拷锟斤拷锟绞撅拷锟斤拷
        setUsrSessionData(new HashMap(1));
        setResultData(new HashMap(1));

        CorpUser corpUser = (CorpUser)this.getUser();
        String corpId = corpUser.getCorpId();

        ApplicationContext appContext = Config.getAppContext();
        CorporationService corpService = (CorporationService) appContext.getBean("CorporationService");

        ApproveRuleService approveRuleService = (ApproveRuleService) appContext.getBean("ApproveRuleService");
        // 通锟斤拷corp id锟斤拷DB
        Corporation corpObj = corpService.view(corpId);

        Map resultData = new HashMap();
        this.setResultData(resultData);
        this.convertPojo2Map(corpObj, resultData);

        Map statusMap = approveRuleService.mapRuleStatus(corpObj.getCorpId());
        
		// Jet added for all txn type 2008-1-22
		String keyAll = "ALL" + "MOP";
		String statusAll = (String) statusMap.get(keyAll);
		if (statusAll == null) {
			statusAll = "N";
		}
		resultData.put("allTxnTypeStatus", statusAll);
        
        List curList = AuthorizationPreferenceAction.getStatusByTxntype(corpObj,
                statusMap, "TRANSFER_BANK");
        resultData.put("List_TRANSFER_BANK", curList);
        
        /*  Add by long_zg 2019-05-30 UAT6-457 COB：company setting菜單項問題 begin  */
        curList = AuthorizationPreferenceAction.getStatusByTxntype(corpObj,
                statusMap, "TRANSFER_BANK_3RD");
        resultData.put("List_TRANSFER_BANK_3RD", curList);
        /*  Add by long_zg 2019-05-30 UAT6-457 COB：company setting菜單項問題 begin  */
        
        curList = AuthorizationPreferenceAction.getStatusByTxntype(corpObj,
                statusMap, "TRANSFER_MACAU");
        resultData.put("List_TRANSFER_MACAU", curList);
        curList = AuthorizationPreferenceAction.getStatusByTxntype(corpObj,
                statusMap, "TRANSFER_OVERSEAS");
        resultData.put("List_TRANSFER_OVERSEAS", curList);
        curList = AuthorizationPreferenceAction.getStatusByTxntype(corpObj,
                statusMap, "TRANSFER_CORP");
        resultData.put("List_TRANSFER_CORP", curList);
        curList = AuthorizationPreferenceAction.getStatusByTxntype(corpObj,
                statusMap, "PAY_BILLS");
        resultData.put("List_PAY_BILLS", curList);
        curList = AuthorizationPreferenceAction.getStatusByTxntype(corpObj,
                statusMap, "PAYROLL");
        resultData.put("List_PAYROLL", curList);
        curList = AuthorizationPreferenceAction.getStatusByTxntype(corpObj,
                statusMap, "TIME_DEPOSIT");
        resultData.put("List_TIME_DEPOSIT", curList);
        curList = AuthorizationPreferenceAction.getStatusByTxntype(corpObj,
                statusMap, "SCHEDULE_TXN");
        resultData.put("List_SCHEDULE_TXN", curList);
        curList = AuthorizationPreferenceAction.getStatusByTxntype(corpObj,
                statusMap, "BANK_DRAFT");
        resultData.put("List_BANK_DRAFT", curList);
        curList = AuthorizationPreferenceAction.getStatusByTxntype(corpObj,
                statusMap, "CASHIER_ORDER");
        resultData.put("List_CASHIER_ORDER", curList);

        // 锟斤拷锟斤拷业锟斤拷息写锟斤拷session锟斤拷锟皆憋拷锟斤拷setLoad页锟斤拷锟斤拷示锟斤拷息
        this.setUsrSessionDataValue("corpObj", corpObj);
    }

    public void viewAuthPrefDetail() throws NTBException {
        ApproveRule approveRule = new ApproveRule();
        this.convertMap2Pojo(this.getParameters(), approveRule);
        CorpUser corpUser = (CorpUser)this.getUser();
        String corpId = corpUser.getCorpId();
        approveRule.setCorpId(corpId);

        ApplicationContext appContext = Config.getAppContext();
        ApproveRuleService approveRuleService = (ApproveRuleService) appContext.getBean("ApproveRuleService");

        Map statusMap = approveRuleService.mapRuleStatus(corpId);
        
		// Jet added for all txn type 2008-1-23		
		// status checking
		String keyAll = "ALL" + "MOP";
		String statusAll = (String) statusMap.get(keyAll);

//		String key = approveRule.getTxnType() + approveRule.getCurrency();
//		String prefStatus = (String) statusMap.get(key);

		boolean flagAllNormal = false;

		if (statusAll != null && statusAll.equals(Constants.STATUS_NORMAL)) {
			flagAllNormal = true;
		}

//        String prefStatus = (String) statusMap.get(key);
//        if (Constants.STATUS_PENDING_APPROVAL.equals(prefStatus)) {
//            throw new NTBException("err.bnk.OperationPending");
//        }

        //锟叫筹拷状态为锟斤拷慕锟斤拷锟�
        approveRule.setStatus(Constants.STATUS_NORMAL);
        List ruleList = approveRuleService.list(approveRule);
        ruleList = this.convertPojoList2MapList(ruleList);

        //锟斤拷玫锟角�PrefId
        String prefId = null;
        // set record seq no for add/delete/update maintain
		if (flagAllNormal || (!"ALL".equals(approveRule.getTxnType()) && !flagAllNormal)) {
			for (int i = 0; i < ruleList.size(); i++) {
				HashMap temp_map = new HashMap();
				temp_map = (HashMap) ruleList.get(i);
				temp_map.put("seq", new Integer(i));
				prefId = (String) temp_map.get("prefId");
			}
		}

		// all转锟斤拷all, 锟斤拷all转all
		if ((!"ALL".equals(approveRule.getTxnType()) && flagAllNormal) || ("ALL".equals(approveRule.getTxnType()) && !flagAllNormal)) {
			ruleList.clear();
		}
				
        Map resultData = new HashMap();
        resultData.put("ruleList", ruleList);
        this.setResultData(resultData);

        /* 锟斤拷织页锟斤拷锟斤拷息 */
        Map InfoMap = new HashMap();
        // 锟斤拷session锟斤拷取锟斤拷锟斤拷业锟斤拷息
        Corporation corpObj = (Corporation)this.getUsrSessionDataValue(
                "corpObj");
        InfoMap.put("corpName", corpObj.getCorpName());
        InfoMap.put("corpId", corpObj.getCorpId());
        InfoMap.put("prefId", prefId);

        // 锟斤拷锟斤拷锟斤拷页锟斤拷选锟斤拷息
        InfoMap.put("txnType", approveRule.getTxnType());
        InfoMap.put("currency", approveRule.getCurrency());
        // ruleType and singleLevel
        if (ruleList.size() > 0) {
            InfoMap
                    .put("ruleType", ((HashMap) ruleList.get(0))
                         .get("ruleType"));
            InfoMap.put("singleLevel", ((HashMap) ruleList.get(0))
                        .get("singleLevel"));
        }
 
        resultData.putAll(InfoMap);

    }

    //add by mxl 0909
    public void viewCorpUserList() throws NTBException {

        String corpId = null;
        //corpId = Utils.null2EmptyWithTrim(this.getParameter("corpId"));
        CorpUser userObj = (CorpUser)this.getUser();
        Corporation corpObj = userObj.getCorporation();
        corpId = corpObj.getCorpId();

        CorporationService corporationService = (CorporationService) Config
                                                .getAppContext().getBean(
                "CorporationService");
        // 锟斤拷锟�corpId 锟斤拷应锟斤拷 Corporation Object
        Corporation corp1 = corporationService.view(corpId);
        // 锟斤拷锟�Corporation 锟斤拷锟斤拷锟斤拷锟津报达拷
        if (corp1 == null) {
            throw new NTBException("err.bnk.CorpNotExist");
        }
        // 锟斤拷Corporation锟斤拷锟斤拷锟斤拷锟矫伙拷Session
        this.setUsrSessionDataValue("corporation", corp1);

        CorpUserService corpUserService = (CorpUserService) Config
                                          .getAppContext().getBean(
                                                  "corpUserService");
        System.out.println("enter the 1");
        // CorpUser user = (CorpUser)this.getUser();

        System.out.println("corpId=" + corpId);

        if ((corpId != null) && (!corpId.equals(""))) {
            List userList = corpUserService.listUserByCorp(corpId);
            userList = this.convertPojoList2MapList(userList);
            Map resultData = new HashMap();
            resultData.put("userList", userList);
            resultData.put("corpId", corpId);
            resultData.put("corpName", corp1.getCorpName());
            setResultData(resultData);
        }
    }

    public void viewCorpUserDetial() throws NTBException {
        String userId = this.getParameter("userId");
        
        //modified by lzg for GAPMC-EB-001-0040
        String corpId = this.getParameter("corpId");
        CorpUserService corpUserService = (CorpUserService) Config
                                          .getAppContext().getBean(
                                                  "corpUserService");
        //CorpUser user = corpUserService.load(userId);
        CorpUser user = corpUserService.loadWithCorpId(userId,corpId);
      //modified by lzg end
        Map resultData = new HashMap();
        showLimits(user.getRoleId(), user.getCorporation().getCorpType(), resultData);
        this.convertPojo2Map(user, resultData);
        resultData.put("allowFinancialController", user.getCorporation().getAllowFinancialController());
        setResultData(resultData);
    }

    public Map showLimits(String roleId, String corpType, Map resultData) {
    	if(Constants.CORP_TYPE_LARGE.equals(corpType) || Constants.CORP_TYPE_MIDDLE_NO_ADMIN.equals(corpType)){
            if (roleId.equals(Constants.ROLE_OPERATOR)) {
                resultData.put("showOperator", "Y");
                resultData.put("showApprover", "N");
            } else if (roleId.equals(Constants.ROLE_APPROVER)) {
                resultData.put("showOperator", "N");
                resultData.put("showApprover", "Y");
            } else {
                resultData.put("showOperator", "N");
                resultData.put("showApprover", "N");
            }
    	} else if(Constants.CORP_TYPE_MIDDLE.equals(corpType)){
    		if (roleId.equals(Constants.ROLE_OPERATOR)) {
                resultData.put("showOperator", "Y");
                resultData.put("showApprover", "N");
            } else if (roleId.equals(Constants.ROLE_APPROVER)) {
                resultData.put("showOperator", "Y");
                resultData.put("showApprover", "Y");
            } else {
                resultData.put("showOperator", "N");
                resultData.put("showApprover", "N");
            }
    	} else if(Constants.CORP_TYPE_SMALL.equals(corpType)){
            if (roleId.equals(Constants.ROLE_OPERATOR)) {
                resultData.put("showOperator", "Y");
                resultData.put("showApprover", "N");
            } else if (roleId.equals(Constants.ROLE_APPROVER)) {
                resultData.put("showOperator", "Y");
                resultData.put("showApprover", "Y");
            } else {
                resultData.put("showOperator", "N");
                resultData.put("showApprover", "N");
            }
    	} else {
            resultData.put("showOperator", "N");
            resultData.put("showApprover", "N");
    	}
        return resultData;
    }


    public void viewGroupList() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        GroupService groupService = (GroupService) appContext.getBean("GroupService");
        CorporationService corpService = (CorporationService) appContext.getBean("CorporationService");

        String corpId = Utils.null2EmptyWithTrim(this.getParameter("corpId"));
        List userGroupList = groupService.listUserGroupByCorp(corpId);
        Corporation corpObj = corpService.view(corpId);

        userGroupList = this.convertPojoList2MapList(userGroupList);
        Map resultData = new HashMap();
        this.convertPojo2Map(corpObj, resultData);
        resultData.put("userGroupList", userGroupList);
        setResultData(resultData);

    }

    public void viewGroupDetail() throws NTBException {
        String groupId = this.getParameter("groupId");
        GroupService groupService = (GroupService) Config.getAppContext()
                                    .getBean("GroupService");

        UserGroup userGroup = groupService.loadUserGroup(groupId);
        Map resultData = new HashMap();
        this.convertPojo2Map(userGroup, resultData);

        List accountPermissionList = groupService.getAccountPermission(groupId,
                userGroup.getVersion());
        List functonPermissionList = groupService.getFunctionPermission(
                groupId, userGroup.getVersion());
        resultData.put("item_accountList", this
                       .convertPojoList2MapList(accountPermissionList));
        resultData.put("item_functionList", this
                       .convertPojoList2MapList(functonPermissionList));

        List corpUserList = groupService.listCorpUser(groupId);
        resultData.put("corpUserList", this
                       .convertPojoList2MapList(corpUserList));
        resultData.put("corpType", Utils.null2EmptyWithTrim(this.getParameter("corpType")));
        setResultData(resultData);
    }

}
