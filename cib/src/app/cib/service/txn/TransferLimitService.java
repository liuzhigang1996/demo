package app.cib.service.txn;

import java.util.Map;

import app.cib.bo.bnk.TxnLimit;

import com.neturbo.set.exception.NTBException;

/**
 * @author panwen
 * 
 */
public interface TransferLimitService {
	/**
	 * @param account
	 * @param corpId
	 * @param txnType
	 * @param amountEq
	 * @param amountMopEq
	 * @return
	 * @throws NTBException
	 */
	boolean checkLimitQuota(String account, String corpId, String txnType,
			double amountEq, double amountMopEq) throws NTBException;

	boolean checkLimitQuotaByCorpId(String corpId, String txnType,
			double amountEq, double amountMopEq) throws NTBException;

	boolean checkLimitQuotaByCorpId1(String corpId, String txnType,
			double amountEq, double amountMopEq) throws NTBException;
	
	boolean checkCurAmtLimitQuota(String account, String corpId, String txnType,
			double amountEq, double amountMopEq) throws NTBException;

	/**
	 * @param account
	 * @param corpId
	 * @param txnType
	 * @param amountEq
	 * @param amountMopEq
	 * @return
	 * @throws NTBException
	 */
	boolean addUsedLimitQuota(String account, String corpId, String txnType,
			double amountEq, double amountMopEq) throws NTBException;	
	
	/*
	 * add by hjs
	 */
	public double getDailyLimit() ;

	public double getTotalLimit() ;

	public String getLimitType() ;
	
	public Double getCorpDailyLimit(String corpId)throws NTBException;
    
	public Double getCorpDailyAcctTotal(String corpId , String date) throws NTBException;
}
