package app.cib.action.sys;

/**
 * @author nabai
 *
 * 用戶管理－新增、修改、删除、查询、鎖定、解鎖、重置密碼
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import app.cib.bo.bnk.Corporation;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.sys.CorpUserHis;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.service.bnk.CorporationService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.sys.AccountAuthorizationService;
import app.cib.service.sys.CorpUserService;
import app.cib.util.Constants;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Encryption;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.core.Log;
import app.cib.service.sys.MailService;
import org.springframework.context.ApplicationContext;

public class AccountAuthorizationAction extends CibAction implements Approvable {

    public static final String SELECT_ALL = "0";
    public static final String SELECT_BY_ACCOUNT = "1";
    public static final String SELECT_BY_USER = "2";
	
    public void listLoad() throws NTBException {
        // 设置空的 ResultData 清空显示数据
        setResultData(new HashMap(1));
    }

    public void listAccountAuthorization() throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();

        String selectBy = null;
        String corpId = null;
        String account = null;
        String authUser = null;
        
        corpId = Utils.null2EmptyWithTrim(this.getParameter("corpId"));
        selectBy = Utils.null2EmptyWithTrim(this.getParameter("Select_By"));
        if(selectBy == null || selectBy == ""){
        	selectBy = SELECT_ALL;
        }
        
        CorporationService corporationService = (CorporationService) Config
				.getAppContext().getBean("CorporationService");
        
		// 获得 corpId 对应的 Corporation Object
		Corporation corp = corporationService.view(corpId);
        AccountAuthorizationService accountAuthorizationService = (AccountAuthorizationService) appContext
				.getBean("AccountAuthorizationService");
                
        // 获得corpID 对应的 account authorization list
		List acct_auth_list = new ArrayList();
        if(SELECT_ALL.equals(selectBy)){
            acct_auth_list = accountAuthorizationService.listAll(corpId);        	
        } else if(SELECT_BY_ACCOUNT.equals(selectBy)){
            account = Utils.null2EmptyWithTrim(this.getParameter("account"));
            acct_auth_list = accountAuthorizationService.listByAccount(corpId, account);        	        	
        } else if(SELECT_BY_USER.equals(selectBy)){
        	authUser = Utils.null2EmptyWithTrim(this.getParameter("authUser"));
            acct_auth_list = accountAuthorizationService.listByUser(corpId, authUser);        	        	
        }

        // for display
		Map resultData = new HashMap();
		resultData.put("acct_auth_list", acct_auth_list);
		resultData.put("corpId", corpId);
		resultData.put("Select_By", selectBy);
		resultData.put("account", account);
		resultData.put("authUser", authUser);
		resultData.put("corpName", corp.getCorpName());
		this.setResultData(resultData);
    }

    public void listAccountAuthorizationCorp() throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();

        String selectBy = null;
        String corpId = null;
        String account = null;
        String authUser = null;
        
		CorpUser corpUser = (CorpUser) this.getUser();
        corpId = corpUser.getCorpId();
        selectBy = Utils.null2EmptyWithTrim(this.getParameter("Select_By"));
        if(selectBy == null || selectBy == ""){
        	selectBy = SELECT_ALL;
        }
        
        CorporationService corporationService = (CorporationService) Config
				.getAppContext().getBean("CorporationService");
        
		// 获得 corpId 对应的 Corporation Object
		Corporation corp = corporationService.view(corpId);
        AccountAuthorizationService accountAuthorizationService = (AccountAuthorizationService) appContext
				.getBean("AccountAuthorizationService");
                
        // 获得corpID 对应的 account authorization list
		List acct_auth_list = new ArrayList();
        if(SELECT_ALL.equals(selectBy)){
            acct_auth_list = accountAuthorizationService.listAll(corpId);        	
        } else if(SELECT_BY_ACCOUNT.equals(selectBy)){
            account = Utils.null2EmptyWithTrim(this.getParameter("account"));
            acct_auth_list = accountAuthorizationService.listByAccount(corpId, account);        	        	
        } else if(SELECT_BY_USER.equals(selectBy)){
        	authUser = Utils.null2EmptyWithTrim(this.getParameter("authUser"));
            acct_auth_list = accountAuthorizationService.listByUser(corpId, authUser);        	        	
        }

        // for display
		Map resultData = new HashMap();
		resultData.put("acct_auth_list", acct_auth_list);
		resultData.put("corpId", corpId);
		resultData.put("Select_By", selectBy);
		resultData.put("account", account);
		resultData.put("authUser", authUser);
		resultData.put("corpName", corp.getCorpName());
		this.setResultData(resultData);
    }
    
    
    public void addLoad() throws NTBException {    	
        // 设置空的 ResultData 清空显示数据
        Map resultData = new HashMap();
        setResultData(new HashMap(1));

        String corpId = Utils.null2Empty(getParameter("corpId"));
        String corpName = Utils.null2Empty(getParameter("corpName"));

        // for display
        resultData.put("corpId", corpId);
        resultData.put("corpName", corpName);
        this.setResultData(resultData);
    }

    public void add() throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();
        AccountAuthorizationService accountAuthorizationService = (AccountAuthorizationService) appContext
				.getBean("AccountAuthorizationService");
        
        String corpId = Utils.null2EmptyWithTrim(this.getParameter("corpId"));
        String corpName = Utils.null2EmptyWithTrim(this.getParameter("corpName"));
        String account = Utils.null2EmptyWithTrim(this.getParameter("account"));
        String authUser = Utils.null2EmptyWithTrim(this.getParameter("authUser"));

        // checking
        List acct_auth_list = new ArrayList();        
        acct_auth_list = accountAuthorizationService.listByAccountUser(corpId, account, authUser);        	        
        if (acct_auth_list.size() > 0) {
            throw new NTBException("err.sys.acctAuthExist");
        }

        List acct_auth_history_list = new ArrayList();        
        acct_auth_history_list = accountAuthorizationService.listByAccountUserHistory(corpId, account, authUser);        	        
        if (acct_auth_history_list.size() > 0) {
            throw new NTBException("err.sys.operationPending");
        }
        
        // for display
		Map resultData = new HashMap();
		resultData.put("corpId", corpId);
		resultData.put("corpName", corpName);
		resultData.put("account", account);
		resultData.put("authUser", authUser);
		this.setResultData(resultData);

        // 将用户数据写入session，以便confirm后写入数据库
        this.setUsrSessionDataValue("corpId", corpId);
        this.setUsrSessionDataValue("corpName", corpName);
        this.setUsrSessionDataValue("account", account);
        this.setUsrSessionDataValue("authUser", authUser);
    }

    public void addConfirm() throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();
        AccountAuthorizationService accountAuthorizationService = (AccountAuthorizationService) appContext
				.getBean("AccountAuthorizationService");
    	
    	String corpId = (String)this.getUsrSessionDataValue("corpId");
    	String corpName = (String)this.getUsrSessionDataValue("corpName");
    	String account = (String)this.getUsrSessionDataValue("account");
    	String authUser = (String)this.getUsrSessionDataValue("authUser");
    	
        // checking
        List acct_auth_list = new ArrayList();        
        acct_auth_list = accountAuthorizationService.listByAccountUser(corpId, account, authUser);        	        
        if (acct_auth_list.size() > 0) {
            throw new NTBException("err.sys.acctAuthExist");
        }

        List acct_auth_history_list = new ArrayList();        
        acct_auth_history_list = accountAuthorizationService.listByAccountUserHistory(corpId, account, authUser);        	        
        if (acct_auth_history_list.size() > 0) {
            throw new NTBException("err.sys.operationPending");
        }
    	
        // for authorization flow
        String seqNo = CibIdGenerator.getIdForOperation("ACCOUNT_AUTHORIZATION_HISTORY");
        FlowEngineService flowEngineService = (FlowEngineService) Config
				.getAppContext().getBean("FlowEngineService");

		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_ACCTOUNT_AUTHORIZATION_ADD, "0", AccountAuthorizationAction.class,
				null, 0, null, 0, 0, seqNo, null, getUser(), null, null, null);
		
		try{
			// insert data
            String recordNo = CibIdGenerator.getIdForOperation("ACCOUNT_AUTHORIZATION");
			Map fieldMap = new HashMap();
			fieldMap.put("SEQ_NO", seqNo);
			fieldMap.put("RECORD_ID", recordNo);
			fieldMap.put("CORP_ID", corpId);
			fieldMap.put("ACCOUNT_NO", account);
			fieldMap.put("AUTH_USER", authUser);
			fieldMap.put("OPERATION", Constants.OPERATION_NEW);
			fieldMap.put("STATUS", Constants.STATUS_PENDING_APPROVAL);
			fieldMap.put("AUTH_STATUS", Constants.AUTH_STATUS_SUBMITED);
			
			accountAuthorizationService.addAccountAuthorizationHis(fieldMap);
			
        } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error adding account authorization", e);
            throw new NTBException("err.sys.AddAccountAuthorizationFailure");
        }
        
        // for display
		Map resultData = new HashMap();
		resultData.put("corpId", corpId);
		resultData.put("corpName", corpName);
		resultData.put("account", account);
		resultData.put("authUser", authUser);
        resultData.put("seqNo", seqNo);
		this.setResultData(resultData);
    }
    
    public void addCancel() throws NTBException {

    }
    
	public String viewDetail(String txnType, String id, CibAction bean) throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();
        AccountAuthorizationService accountAuthorizationService = (AccountAuthorizationService) appContext
				.getBean("AccountAuthorizationService");

        List acct_auth_list = new ArrayList();        
        acct_auth_list = accountAuthorizationService.loadHisBySeqNo(id);        
        
		Map resultMap = new HashMap();
        if(acct_auth_list.size()>0){
        	resultMap = (Map)acct_auth_list.get(0); 
        }

        // get corp name
        CorporationService corpService = (CorporationService) appContext.getBean("CorporationService");
        Corporation corp = corpService.view((String)resultMap.get("CORP_ID"));

        Map resultData = bean.getResultData();
		resultData.put("corpId", resultMap.get("CORP_ID"));
		resultData.put("corpName", corp.getCorpName());
		resultData.put("account", resultMap.get("ACCOUNT_NO"));
		resultData.put("authUser", resultMap.get("AUTH_USER"));
        
        return "/WEB-INF/pages/bank/acct_auth/account_auth_approval_view.jsp";
	}

	public boolean approve(String txnType, String id, CibAction bean) throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();
        AccountAuthorizationService accountAuthorizationService = (AccountAuthorizationService) appContext
				.getBean("AccountAuthorizationService");

        //get history record
        List acct_auth_list = new ArrayList();        
        acct_auth_list = accountAuthorizationService.loadHisBySeqNo(id);        
        
		Map resultMap = new HashMap();
        if(acct_auth_list.size()>0){
        	resultMap = (Map)acct_auth_list.get(0); 
        }
        
        if(txnType.equals(Constants.TXN_SUBTYPE_ACCTOUNT_AUTHORIZATION_ADD)){
            resultMap.put("STATUS", Constants.STATUS_NORMAL);
            resultMap.put("AUTH_STATUS", Constants.AUTH_STATUS_COMPLETED);
        	
        	// update account_authorization_history
            Map conditionMap = new HashMap();
            conditionMap.put("SEQ_NO", id);
            accountAuthorizationService.updateAccountAuthorizationHis(resultMap, conditionMap);
            
            // insert account_authorization
            resultMap.remove("SEQ_NO");
			accountAuthorizationService.addAccountAuthorization(resultMap);
        } else if(txnType.equals(Constants.TXN_SUBTYPE_ACCTOUNT_AUTHORIZATION_EDIT)){
            resultMap.put("STATUS", Constants.STATUS_NORMAL);
            resultMap.put("AUTH_STATUS", Constants.AUTH_STATUS_COMPLETED);
        	
        	// update account_authorization_history
            Map conditionHisMap = new HashMap();
            Map conditionMap = new HashMap();
            conditionHisMap.put("SEQ_NO", id);
            accountAuthorizationService.updateAccountAuthorizationHis(resultMap, conditionHisMap);
            
            // update account_authorization
            resultMap.remove("SEQ_NO");
            conditionMap.put("RECORD_ID", resultMap.get("RECORD_ID"));            
			accountAuthorizationService.updateAccountAuthorization(resultMap, conditionMap);        	
        } else if(txnType.equals(Constants.TXN_SUBTYPE_ACCTOUNT_AUTHORIZATION_DELETE)){
            resultMap.put("STATUS", Constants.STATUS_REMOVED);
            resultMap.put("AUTH_STATUS", Constants.AUTH_STATUS_COMPLETED);

        	// update account_authorization_history
            Map conditionHisMap = new HashMap();
            conditionHisMap.put("SEQ_NO", id);
            accountAuthorizationService.updateAccountAuthorizationHis(resultMap, conditionHisMap);

            // delete account_authorization
			accountAuthorizationService.deleteAccountAuthorization((String)resultMap.get("RECORD_ID"));        	
        }

		return true;
	}

    public void editLoad() throws NTBException {    	
        // 设置空的 ResultData 清空显示数据
        Map resultData = new HashMap();
        setResultData(new HashMap(1));

        String corpId = Utils.null2Empty(getParameter("corpId"));
        String corpName = Utils.null2Empty(getParameter("corpName"));
        String selectAccount = Utils.null2Empty(getParameter("selectAccount"));
        String selectUser = Utils.null2Empty(getParameter("selectUser"));
        String selectRecordId = Utils.null2Empty(getParameter("selectRecordId"));
        
        // for display
        resultData.put("corpId", corpId);
        resultData.put("corpName", corpName);
        resultData.put("account", selectAccount);
        resultData.put("authUser", selectUser);
        resultData.put("selectRecordId", selectRecordId);
        
        this.setResultData(resultData);
    }

    public void edit() throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();
        AccountAuthorizationService accountAuthorizationService = (AccountAuthorizationService) appContext
				.getBean("AccountAuthorizationService");
        
        String corpId = Utils.null2EmptyWithTrim(this.getParameter("corpId"));
        String corpName = Utils.null2EmptyWithTrim(this.getParameter("corpName"));
        String account = Utils.null2EmptyWithTrim(this.getParameter("account"));
        String authUser = Utils.null2EmptyWithTrim(this.getParameter("authUser"));
        String selectRecordId = Utils.null2EmptyWithTrim(this.getParameter("selectRecordId"));

        // checking
        List acct_auth_list = new ArrayList();        
        acct_auth_list = accountAuthorizationService.listByAccountUser(corpId, account, authUser);        	        
        if (acct_auth_list.size() > 0) {
            throw new NTBException("err.sys.acctAuthExist");
        }

        List acct_auth_history_list = new ArrayList();        
        acct_auth_history_list = accountAuthorizationService.listByAccountUserHistory(corpId, account, authUser);        	        
        if (acct_auth_history_list.size() > 0) {
            throw new NTBException("err.sys.operationPending");
        }
        
        // for display
		Map resultData = new HashMap();
		resultData.put("corpId", corpId);
		resultData.put("corpName", corpName);
		resultData.put("account", account);
		resultData.put("authUser", authUser);
		this.setResultData(resultData);

        // 将用户数据写入session，以便confirm后写入数据库
        this.setUsrSessionDataValue("corpId", corpId);
        this.setUsrSessionDataValue("corpName", corpName);
        this.setUsrSessionDataValue("account", account);
        this.setUsrSessionDataValue("authUser", authUser);
        this.setUsrSessionDataValue("selectRecordId", selectRecordId);
    }
    
    public void editConfirm() throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();
        AccountAuthorizationService accountAuthorizationService = (AccountAuthorizationService) appContext
				.getBean("AccountAuthorizationService");
    	
    	String corpId = (String)this.getUsrSessionDataValue("corpId");
    	String corpName = (String)this.getUsrSessionDataValue("corpName");
    	String account = (String)this.getUsrSessionDataValue("account");
    	String authUser = (String)this.getUsrSessionDataValue("authUser");
    	String selectRecordId = (String)this.getUsrSessionDataValue("selectRecordId");
    	
        // checking
        List acct_auth_list = new ArrayList();        
        acct_auth_list = accountAuthorizationService.listByAccountUser(corpId, account, authUser);  
        if (acct_auth_list.size() > 0) {
            throw new NTBException("err.sys.acctAuthExist");
        }
        
        List acct_auth_history_list = new ArrayList();        
        acct_auth_history_list = accountAuthorizationService.listByAccountUserHistory(corpId, account, authUser);        	        
        if (acct_auth_history_list.size() > 0) {
            throw new NTBException("err.sys.operationPending");
        }
    	
        // for authorization flow
        String seqNo = CibIdGenerator.getIdForOperation("ACCOUNT_AUTHORIZATION_HISTORY");
        FlowEngineService flowEngineService = (FlowEngineService) Config
				.getAppContext().getBean("FlowEngineService");

		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_ACCTOUNT_AUTHORIZATION_EDIT, "0", AccountAuthorizationAction.class,
				null, 0, null, 0, 0, seqNo, null, getUser(), null, null, null);
		
		try{
			// insert data
			Map fieldMap = new HashMap();
			fieldMap.put("SEQ_NO", seqNo);
			fieldMap.put("RECORD_ID", selectRecordId);
			fieldMap.put("CORP_ID", corpId);
			fieldMap.put("ACCOUNT_NO", account);
			fieldMap.put("AUTH_USER", authUser);
			fieldMap.put("OPERATION", Constants.OPERATION_UPDATE);
			fieldMap.put("STATUS", Constants.STATUS_PENDING_APPROVAL);
			fieldMap.put("AUTH_STATUS", Constants.AUTH_STATUS_SUBMITED);
			
			accountAuthorizationService.addAccountAuthorizationHis(fieldMap);
			
        } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error adding account authorization", e);
            throw new NTBException("err.sys.AddAccountAuthorizationFailure");
        }
        
        // for display
		Map resultData = new HashMap();
		resultData.put("corpId", corpId);
		resultData.put("corpName", corpName);
		resultData.put("account", account);
		resultData.put("authUser", authUser);
        resultData.put("seqNo", seqNo);
		this.setResultData(resultData);
    }

    public void editCancel() throws NTBException {

    }

    public void delete() throws NTBException {    	
        // 设置空的 ResultData 清空显示数据
        Map resultData = new HashMap();
        setResultData(new HashMap(1));

        String corpId = Utils.null2Empty(getParameter("corpId"));
        String corpName = Utils.null2Empty(getParameter("corpName"));
        String selectAccount = Utils.null2Empty(getParameter("selectAccount"));
        String selectUser = Utils.null2Empty(getParameter("selectUser"));
        String selectRecordId = Utils.null2Empty(getParameter("selectRecordId"));
        
        // for display
        resultData.put("corpId", corpId);
        resultData.put("corpName", corpName);
        resultData.put("account", selectAccount);
        resultData.put("authUser", selectUser);
        resultData.put("selectRecordId", selectRecordId);        
        this.setResultData(resultData);
        
        // 将用户数据写入session，以便confirm后写入数据库
        this.setUsrSessionDataValue("corpId", corpId);
        this.setUsrSessionDataValue("corpName", corpName);
        this.setUsrSessionDataValue("account", selectAccount);
        this.setUsrSessionDataValue("authUser", selectUser);
        this.setUsrSessionDataValue("selectRecordId", selectRecordId);
    }

    public void deleteConfirm() throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();
        AccountAuthorizationService accountAuthorizationService = (AccountAuthorizationService) appContext
				.getBean("AccountAuthorizationService");
        
    	String corpId = (String)this.getUsrSessionDataValue("corpId");
    	String corpName = (String)this.getUsrSessionDataValue("corpName");
    	String account = (String)this.getUsrSessionDataValue("account");
    	String authUser = (String)this.getUsrSessionDataValue("authUser");
    	String selectRecordId = (String)this.getUsrSessionDataValue("selectRecordId");
    	
        // for authorization flow
        String seqNo = CibIdGenerator.getIdForOperation("ACCOUNT_AUTHORIZATION_HISTORY");
        FlowEngineService flowEngineService = (FlowEngineService) Config
				.getAppContext().getBean("FlowEngineService");

		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_ACCTOUNT_AUTHORIZATION_DELETE, "0", AccountAuthorizationAction.class,
				null, 0, null, 0, 0, seqNo, null, getUser(), null, null, null);
    	
		try{
			// insert data
			Map fieldMap = new HashMap();
			fieldMap.put("SEQ_NO", seqNo);
			fieldMap.put("RECORD_ID", selectRecordId);
			fieldMap.put("CORP_ID", corpId);
			fieldMap.put("ACCOUNT_NO", account);
			fieldMap.put("AUTH_USER", authUser);
			fieldMap.put("OPERATION", Constants.OPERATION_REMOVE);
			fieldMap.put("STATUS", Constants.STATUS_PENDING_APPROVAL);
			fieldMap.put("AUTH_STATUS", Constants.AUTH_STATUS_SUBMITED);
			
			accountAuthorizationService.addAccountAuthorizationHis(fieldMap);
			
        } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error adding account authorization", e);
            throw new NTBException("err.sys.AddAccountAuthorizationFailure");
        }

        // for display
		Map resultData = new HashMap();
		resultData.put("corpId", corpId);
		resultData.put("corpName", corpName);
		resultData.put("account", account);
		resultData.put("authUser", authUser);
        resultData.put("seqNo", seqNo);
		this.setResultData(resultData);        
    }
    	
	public boolean reject(String txnType, String id, CibAction bean) throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();
        AccountAuthorizationService accountAuthorizationService = (AccountAuthorizationService) appContext
				.getBean("AccountAuthorizationService");

        //get history record
        List acct_auth_list = new ArrayList();        
        acct_auth_list = accountAuthorizationService.loadHisBySeqNo(id);        
        
		Map resultMap = new HashMap();
        if(acct_auth_list.size()>0){
        	resultMap = (Map)acct_auth_list.get(0); 
        }
        
        resultMap.put("STATUS", Constants.STATUS_REMOVED);
        resultMap.put("AUTH_STATUS", Constants.AUTH_STATUS_REJECTED);

    	// update account_authorization_history
        Map conditionMap = new HashMap();
        conditionMap.put("SEQ_NO", id);
        accountAuthorizationService.updateAccountAuthorizationHis(resultMap, conditionMap);
                
        return true;
	}
	
    public boolean cancel(String txnType, String id, CibAction bean)
			throws NTBException {
		return reject(txnType, id, bean);
	}
}
