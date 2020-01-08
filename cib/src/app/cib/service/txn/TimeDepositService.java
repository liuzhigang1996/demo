/**
 * 
 */
package app.cib.service.txn;

import java.math.BigDecimal;
import java.util.*;

import com.neturbo.set.exception.NTBException;

import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TimeDeposit;

/**
 * @author hjs
 *
 */
public interface TimeDepositService {
	
	public Map queryNewTDInfo(CorpUser corpUser, String debitAccount, String amout, String currency, String period, String accountCcy, String proNo) throws NTBException;
	
	public Map queryWithdrawTDInfo(CorpUser corpUser, String tdAccount, 
			Date valueDate, Date maturityDate,
			String ccyccyCBS, BigDecimal exchangeRate, String productNo) throws NTBException ;
	/*
	 * toHost 332I-HST-0351202
	 * add by gan 20180122
	 */
	public Map queryWithdrawInfoByHost(CorpUser corpUser, String ccy, String principal, 
			Date valueDate, Date maturityDate, String period, String productNo) throws NTBException ;
		
	
	/*
	 * new a time deposit
	 */
	public void addTiemDeposit(TimeDeposit pojoTimeDeposit) throws NTBException ;
	
	/*
	 * view a detail time deposit infomation
	 */
	public TimeDeposit viewTimeDeposit(String transID) throws NTBException ;
	
	/*
	 * withdraw a time deposit
	 */
	public void withdrawTiemDeposit(TimeDeposit pojoTimeDeposit) throws NTBException ;
	
	/*
	 * list a time deposit list
	 */
	public List listTimeDeposit(String corpID, String userID) throws NTBException ;
	
	public String approveNewTDAccout(TimeDeposit timeDeposit, CorpUser corpUser, BigDecimal equivalentMOP) throws NTBException;
	
	public void approveWithdrawTDAccout(TimeDeposit timeDeposit, CorpUser corpUser) throws NTBException;
	
	public void rejectNewTDAccout(String transId, CorpUser corpUser) throws NTBException;
	
	public void rejectWithdrawTDAccout(String transId, CorpUser corpUser) throws NTBException;
	
	public boolean isAuthorize(TimeDeposit timeDeposit) throws NTBException;
	
	public List listNewTdAccCcy() throws NTBException;
	
	public List lsitHistory(String corpId, String userId, String dateFrom, String dateTo) throws NTBException;

	public String tdType2TxnType(String tdType) throws NTBException;
	//public String getPeriodDescription(String period) throws NTBException;
	public String getPeriodDescription(String period,String language) throws NTBException;
//	public String tinor2pe(String period) throws NTBException;//add by linrui 20181102
	public List loadAccount(String corpId) throws NTBException;//add by linrui for temp get ccy
	public String getInCd(String tdAccount) throws NTBException;//add by linrui 20190525
	public Map viewDepositDetial(String accountNo)throws NTBException;//add by linrui 20190526

	public void removeTransfer(String tableName, String transNo) throws NTBException;

	public List getInterestList(String language) throws NTBException;
	
	public List getPeriodList(String language) throws NTBException;

	public List getCurrencyList(String language)  throws NTBException;

}
