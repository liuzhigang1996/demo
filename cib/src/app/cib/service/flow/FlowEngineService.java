package app.cib.service.flow;

import java.util.Date;
import java.util.List;
import java.util.Map;

import app.cib.bo.flow.FlowProcess;
import app.cib.bo.flow.FlowWork;
import app.cib.core.CibAction;

import com.neturbo.set.core.NTBUser;
import com.neturbo.set.exception.NTBException;
import app.cib.bo.flow.FlowWorkHis;
import app.cib.bo.sys.CorpUser;

/**
 * @author panwen
 */
/**
 * @author panwen
 *
 */
public interface FlowEngineService {

	public static final String CORPID_BANK = "bank001";

	public static final String ACTION_REQUEST = "R";

	public static final String ACTION_APPROVE = "A";

	public static final String ACTION_EXECUTE = "E";

	public static final String ACTION_REJECT = "B";

	public static final String TXN_CATEGORY_NONFINANCE = "0";

	public static final String TXN_CATEGORY_FINANCE = "1";

	public static final String RULE_TYPE_SINGLE = "0";

	public static final String RULE_TYPE_MULTI = "1";

	public static final String APPROVE_TYPE_UNASSIGNED = "0";

	public static final String APPROVE_TYPE_ASSIGNED = "1";

	public static final String PROCESS_STATUS_NEW = "0";

	public static final String PROCESS_STATUS_EXECUTE_PENDING = "6";

	public static final String PROCESS_STATUS_REJECT = "7";

	public static final String PROCESS_STATUS_CANCEL = "8";

	public static final String PROCESS_STATUS_FINISH = "9";
	
	public static final String PROCESS_STATUS_EXPIRED = "3";

	public static final String WORK_STATUS_NEW = "0";

	public static final String WORK_STATUS_CHECKOUT = "1";

	public static final String WORK_STATUS_CANCEL = "8";

	public static final String WORK_STATUS_FINISH = "9";
	
	public static final String WORK_STATUS_EXPIRED = "3";

	public static final String ID_TYPE_WORK = "flowWork";

	public static final String ID_TYPE_PROCESS = "FlowProcess";

	public static final String RULE_FLAG_FROM = "0";

	public static final String RULE_FLAG_TO = "1";

	/**
	 * @param txnType
	 * @param txnCategory
	 * @param txnBean
	 * @param currency
	 * @param amount
	 * @param toCurrency
	 * @param toAmount
	 * @param amountMopEq
	 * @param transNo
	 * @param desc
	 * @param user
	 * @param assignedUser
	 * @param allowExecutor
	 * @param ruleFlag
	 * @return
	 * @throws NTBException
	 */
	String startProcess(String txnType, String txnCategory, Class txnBean,
			String currency, double amount, String toCurrency, double toAmount,
			double amountMopEq, String transNo, String desc, NTBUser user,
			String assignedUser, String allowExecutor, String ruleFlag)
			throws NTBException;

	/**
	 * @param processId
	 * @param user
	 * @throws NTBException
	 */
	void cancelProcess(String processId, NTBUser user) throws NTBException;

	/**
	 * @param processId
	 * @param user
	 * @param bean
	 * @throws NTBException
	 */
	void cancelProcess(String processId, NTBUser user, CibAction bean)
			throws NTBException;

	/**
	 * @param workId
	 * @param user
	 * @return
	 * @throws NTBException
	 */
	FlowWork viewWork(String workId, NTBUser user) throws NTBException;

        /**
         * @param workId
         * @param user
         * @return
         * @throws NTBException
         */
        FlowWorkHis viewWorkHis(String workId, NTBUser user) throws NTBException;

	/**
	 * @param user
	 * @return
	 * @throws NTBException
	 */
	List listWork(NTBUser user) throws NTBException;

	/**
	 * @param user
	 * @return
	 * @throws NTBException
	 */
	List listWorkChecked(NTBUser user) throws NTBException;

	/**
	 * @param transNo
	 * @param user
	 * @return
	 * @throws NTBException
	 */
	List listWorkByTrans(String txnType, String transNo, NTBUser user)
			throws NTBException;

	
	/**
	 * @param procID
	 * @param user
	 * @return
	 * @throws NTBException
	 */
	List getProgress(String procID, NTBUser user) throws NTBException;

	/**
	 * @param procID
	 * @param user
	 * @return
	 * @throws NTBException
	 */
	Map getProgressByBatch(String procIDs[], NTBUser user) throws NTBException;

	/**
	 * @param procId
	 * @param user
	 * @return
	 * @throws NTBException
	 */
	List listWorkByProcDealed(String procId, NTBUser user) throws NTBException;

	/**
	 * @param user
	 * @return
	 * @throws NTBException
	 */
	List listWorkByUserDealing(NTBUser user, String userId,
    		String dateFrom, String dateTo) throws NTBException;
	
	
	List listWorkByUserExpired(NTBUser user, String userId,
			String dateFrom, String dateTo) throws NTBException;

	/**
	 * @param loginUser
	 * @param queryUser
	 * @return
	 * @throws NTBException
	 */
	/*
	List listWorkByCorpDealing(NTBUser loginUser,NTBUser queryUser) throws NTBException;
	*/

	/**
	 * @param user
	 * @param userId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws NTBException
	 */
	List listWorkByCorpDealing(NTBUser loginUser,NTBUser queryUser,Date fromDate,Date toDate) throws NTBException;

	/**
	 * @param user
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws NTBException
	 */
	List listWorkByUserAll(NTBUser user, Date fromDate, Date toDate)
			throws NTBException;

	/**
	 * @param user
	 * @param userId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws NTBException
	 */
	List listWorksByCorpAll(NTBUser loginUser,NTBUser queryUser,Date fromDate,Date toDate) throws NTBException;

	/**
	 * @param workId
	 * @param user
	 * @return
	 * @throws NTBException
	 */
	boolean checkoutWork(String workId, NTBUser user) throws NTBException;

	/**
	 * @param workId
	 * @param userId
	 * @return
	 * @throws NTBException
	 */
	boolean undoCheckoutWork(String workId, String userId) throws NTBException;

	/**
	 * @param userId
	 * @throws NTBException
	 */
	void undoAllCheckoutWork(String userId) throws NTBException;

	/**
	 * @param workId
	 * @param action
	 * @param memo
	 * @param user
	 * @return
	 * @throws NTBException
	 */
	boolean doWork(String workId, String action, String memo, NTBUser user,
			CibAction bean) throws NTBException;

	/**
	 * @param workIds
	 * @param action
	 * @param memo
	 * @param user
	 * @return
	 * @throws NTBException
	 */
	boolean[] doMultiWork(String[] workIds, String action, String memo,
			NTBUser user, CibAction bean) throws NTBException;
	
	/**
	 * 
	 * @param workIds
	 * @param action
	 * @param memo
	 * @param user
	 * @param bean
	 * @return
	 * @throws NTBException
	 * @author su_jj
	 * @since 20110307
	 */
	Object[] execMultiWork(String[] workIds, String action, String memo,
			NTBUser user, CibAction bean) throws NTBException;

	/**
	 * @param workId
	 * @param assignedUser
	 * @param user
	 * @return
	 * @throws NTBException
	 */
	boolean assignWork(String workId, String assignedUser, NTBUser user)
			throws NTBException;

	/**
	 * @param rule
	 * @return
	 * @throws NTBException
	 */
	List extractRuleStr(String rule) throws NTBException;
	
	
	
	List loadProcess(String procId) throws NTBException;
	
	
	
	/**
	 * @param status
	 * @return
	 * @throws NTBException
	 */
	int getApprovedCount(String status) throws NTBException;

	/**
	 * @param items
	 * @return
	 * @throws NTBException
	 */
	String combineRuleStr(List items) throws NTBException;

	/**
	 * @param txnType
	 * @param transNo
	 * @return
	 * @throws NTBException
	 */
	FlowProcess viewFlowProcess(String txnType, String transNo)
			throws NTBException;

	/**
	 * @param procId
	 * @return
	 * @throws NTBException
	 */
	FlowProcess viewFlowProcess(String procId) throws NTBException;

	boolean changeAssignedUser(String procId, String assignedUser,
			CibAction bean) throws NTBException;

	/**
	 * @param txnType
	 * @param transNo
	 * @return
	 * @throws NTBException
	 */
	boolean checkApproveComplete(String txnType, String transNo)
			throws NTBException;

	/**
	 * @param procId
	 * @return
	 * @throws NTBException
	 */
	String getProcessStatus(String procId) throws NTBException;
	
	/**
	 * add by hjs
	 * @param corpId
	 * @param userId
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 * @throws NTBException
	 */
	public List listProcByCorpDealing(String corpId, String userId, String dateFrom, String dateTo)
		throws NTBException;

    public List listWorkByUserDealed(NTBUser user, String userId, String procStatus,
    		String dateFrom, String dateTo) throws NTBException;
    
    public boolean checkOutstandingStatus(CorpUser user, String corpType) throws NTBException;
    //add by linrui 20190916 for CR589
    public List listWorkByUserAll(NTBUser user, Date fromDate, Date toDate, String sortOrder) throws NTBException;
    public List listWorkChecked(NTBUser user, String sortOrder) throws NTBException;
    
    public List listWorkByUserDealing(NTBUser user, String userId, String dateFrom, String dateTo, String sortOrder) throws NTBException;
    public List listWorkByUserDealed(NTBUser user, String userId, String procStatus, String dateFrom, String dateTo, String sortOrder) throws NTBException;
    public List listWorkByUserExpired(NTBUser user, String userId, String dateFrom, String dateTo, String sortOrder) throws NTBException;
    
}
