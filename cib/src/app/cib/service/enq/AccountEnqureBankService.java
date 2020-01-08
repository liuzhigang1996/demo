package app.cib.service.enq;

import java.util.List;
import java.util.Map;


import app.cib.bo.sys.CorpUser;

import com.neturbo.set.exception.NTBException;

public interface AccountEnqureBankService {
	public List listSummaryByAccType(CorpUser user, String corpId, String accType, String appCode) throws NTBException;
	//modify by wcc 20180208 public List listCurrentAccount(CorpUser user, String corpId) throws NTBException;
	public List listCurrentAccount(CorpUser user, String corpId,String appCode,String prePage) throws NTBException;
	//modify by wcc public List listTimeDeposit(CorpUser user, String corpId) throws NTBException;
	public List listTimeDeposit(CorpUser user, String corpId,String appCode,String prePage) throws NTBException;
	public List listOverdraftAccount(CorpUser user, String corpId) throws NTBException;
	//midify by wcc public List listLoanAccount(CorpUser user, String corpId) throws NTBException;
	public List listLoanAccount(CorpUser user, String corpId,String appCode,String prePage) throws NTBException;
	public Map viewCurrentDetail(CorpUser user, String accountNo) throws NTBException;
	public Map viewCurrentDetail(CorpUser user, String accountNo,String ccy) throws NTBException;
	public Map viewDepositDetial(CorpUser user, String accountNo) throws NTBException;
	public Map viewOverdraftDetial(CorpUser user, String accountNo) throws NTBException;
	public Map viewOverdraftDetial(CorpUser user, String accountNo,String fromCCY) throws NTBException;
	public Map viewLoanDetail(CorpUser user, String accountNo) throws NTBException;
	/* modify by wcc public List listTransHistory(CorpUser user, String accountNo) throws NTBException;*/
	public List listTransHistory(CorpUser user, String accountNo,long dateFrom,long dateTo,String prePage,String ccy) throws NTBException; 
	//add by linrui 20190918
	public List listTransHistory(CorpUser user, String accountNo,long dateFrom,long dateTo,String prePage,String ccy,String sortOrder) throws NTBException; 
	public List listTransHistoryFromDB(Map condition) throws NTBException;
	public Map viewInwardInfo(String key) throws NTBException;
	public List listCreditCard(CorpUser user, String corpId) throws NTBException;
	/*
	 * listFromDB ����
	 * listFromHost ����
	 * 
	 * add by hjs 2006-09-30
	 */
	public List filter(List listFromDB, List listFromHost) throws NTBException;
	public String formatPeriod(String periodFromHost) throws NTBException;
	
	
	
	// add by mxl120
	//modify wcc wcc public List listSavingAccount(CorpUser user, String corpId) throws NTBException;
	public List listSavingAccount(CorpUser user, String corpId,String appCode,String prePage) throws NTBException;
	public Map viewSavingDetail(CorpUser user, String accountNo) throws NTBException;
	public Map viewSavingDetail(CorpUser user, String accountNo,String fromCCY) throws NTBException;
	
	
	public List listCreditTransHistory(CorpUser user, String accountNo) throws NTBException ;
	public Map viewCreditDetail(CorpUser user, String accountNo) throws NTBException;
	public List filterCredit(List listFromDB, List listFromHost) throws NTBException;
	//add by lzg 20190701
	public List getAvailableCurrencies() throws NTBException;
	//add by lzg end
}

