/**
 * @author hjs
 * 2006-8-3
 */
package app.cib.service.bnk;

import java.util.List;
import java.util.Map;

import com.neturbo.set.exception.NTBException;

import app.cib.bo.bnk.TxnLimit;

/**
 * @author hjs
 * 2006-8-3
 */
public interface TxnLimitService {
	
	public String checkLimitsStatus(String corpId, String account) throws NTBException;
	
	public TxnLimit viewLimitsDetial (String corpId, String accNo, String status) throws NTBException;
	
	public TxnLimit viewLimitsDetialByPK (String limitId) throws NTBException;
	
	public TxnLimit viewLimitsDetialByPrefId (String prefId) throws NTBException;
	
	public void newLimits(TxnLimit pojoTxnLimit, String newPrefId) throws NTBException;
	
	public String getCurrency(String account, String allAccounts) throws NTBException;
	
	/*
	 * //��ȡ��һ�ν��׵�����
	 */
	public String getLastLimitId(String corpId, String account) throws NTBException;
	
	/*
	 * lsit all transaction limits record by corp ID that status is not REMOVED 
	 */
	public List listAccLimitsInfo(String corpId, String status) throws NTBException;
	
	public boolean checkPrefIdInCorpPref(String prefId) throws NTBException;

	/**
	 * ALL_ACCOUNT�˺ż�����:<br>
	 * <br>
	 * ���ÿһ����REMOVED��¼<br>
	 * 	|- ÿһ����¼���������������<br>
	 * 		|- 1. ACC_ACCOUNT�˺�<br>
	 * 		|	|- ״̬ΪNORMAL<br>
	 * 		|	|	|- CORP_PREFERENCE�����޴˼�¼ --> �׳�NTBException<br>
	 * 		|	|	|- CORP_PREFERENCE����״̬Ϊ��NORMAL --> �׳�NTBException<br>
	 * 		|	|	|- CORP_PREFERENCE����״̬ΪNORMAL --> convertPojo2Map(txnLimit, resultData);<br>
	 * 		|	|		|- txnType =?= TxnLimit.TXN_TYPE_ALL<br>
	 * 		|	|			|- true	 --> corpInfoMap.put("allLimits", txnLimit.getLimit1());<br>
	 * 		|	|			|- false --> Undo Anything<br>
	 * 		|	|- ״̬ΪPENDING_APPROVAL --> �׳�NTBException<br>
	 * 		|- 2. ��ͨ�˺�<br>
	 * 			|- ״̬ΪPENDING_APPROVAL --> �׳�NTBException<br>
	 *			|- ״̬ΪNORMAL<br>
	 * 			|	|- CORP_PREFERENCE�����޴˼�¼ --> �׳�NTBException<br>
	 *			|	|- CORP_PREFERENCE����״̬Ϊ��NORMAL --> �׳�NTBException<br>
	 *			|	|- CORP_PREFERENCE����״̬ΪNORMAL --> Undo Anything<br>
	 * 			|- ״̬Ϊ���� --> �׳�NTBException<br>
	 */
	public void setResultDataForAllAcc(String corpId, Map corpInfoMap, Map resultData) throws NTBException;

	/**
	 * ��ͨ�˺ż�����:<br>
	 * <br>
	 * ���ÿһ����REMOVED��¼<br>
	 * 	|- ÿһ����¼���������������<br>
	 * 		|- 1. ACC_ACCOUNT�˺�<br>
	 * 		|	|- ״̬ΪPENDING_APPROVAL --> �׳�NTBException<br>
	 *		|	|- ״̬ΪNORMAL<br>
	 *		|	|	|	|- CORP_PREFERENCE�����޴˼�¼ --> �׳�NTBException<br>
	 *		|	|	|	|- CORP_PREFERENCE����״̬Ϊ��NORMAL --> �׳�NTBException<br>
	 *		|	|	|	|- CORP_PREFERENCE����״̬ΪNORMAL --> Undo Anything<br>
	 *		|	|- ״̬Ϊ���� --> �׳�NTBException<br>
	 * 		|- 2. ��ͨ�˺�<br>
	 * 			|- �Ǳ��˺� --> Undo Anything<br>
	 * 			|- ���˺�<br>
	 * 				|- ״̬ΪNORMAL<br>
	 * 				|	|- CORP_PREFERENCE�����޴˼�¼ --> �׳�NTBException<br>
	 *				|	|- CORP_PREFERENCE����״̬Ϊ��NORMAL --> �׳�NTBException<br>
	 *				|	|- CORP_PREFERENCE����״̬ΪNORMAL --> convertPojo2Map(txnLimit, resultData);<br>
	 *				|		|- txnType =?= TxnLimit.TXN_TYPE_ALL<br>
	 *				|			|- true	 --> corpInfoMap.put("allLimits", txnLimit.getLimit1());<br>
	 *				|			|- false --> Undo Anything<br>
	 *				|- ״̬ΪPENDING_APPROVAL --> �׳�NTBException<br>
	 * 				|- ״̬Ϊ���� --> �׳�NTBException<br>
	 */	
	public void setResultDataForCommonAcc(String corpId, String account, Map corpInfoMap, Map resultData) throws NTBException;
	
	public void approve(String txnType, String prefId) throws NTBException;
	
	public void reject(String txnType, String prefId) throws NTBException;
	
	public List listAccount(String corpId) throws NTBException;
	public List listAccountClass(String corpId) throws NTBException;//add by linrui 20190327
	
	public void add(TxnLimit txnLimit) throws NTBException;
	
	public void update(TxnLimit txnLimit) throws NTBException;
	
	public List listLimitsByAfterId(String afterId) throws NTBException;
	
	public List listLimitsByStatus(String corpId, String status) throws NTBException;
	
	public Map getOutstandingLimit(TxnLimit txnLimit) throws NTBException;
}
