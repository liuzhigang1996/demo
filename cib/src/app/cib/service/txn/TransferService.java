package app.cib.service.txn;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TransferBank;
import app.cib.bo.txn.TransferMacau;
import app.cib.bo.txn.TransferOversea;

import com.neturbo.set.exception.NTBException;

/**
 * @author mxl 2006-07-16
 */
public interface TransferService {
	/* Add template */
	public TransferBank templateInBANK1to1(TransferBank pojoTransfer)
	     throws NTBException;
	public TransferMacau templateInMacau(TransferMacau pojoTransfer)
	     throws NTBException;
	public TransferOversea templateInAccOverseas(TransferOversea pojoTransfer)
	     throws NTBException;
	
	
	/* Transfer Account to Account in BANK */
	public TransferBank transferAccInBANK1to1(TransferBank pojoTransfer)
			throws NTBException;

	/* Transfer In Macau */
	public TransferMacau transferAccMacau(TransferMacau pojoTransfer)
			throws NTBException;

	/* Transfer In Banks Overseas */
	public TransferOversea transferAccOverseas(TransferOversea pojoTransfer)
			throws NTBException;

	/* List TransferInBANK History */
	public List transferHistoryInBANK(String dateFrom, String dateTo,
			String corpID, String userID, String fromAccount)
			throws NTBException;

	/* List Transfer In Macau */
	public List transferHistoryInMacau(Map queryParam) throws NTBException;

	/* List Transfer In Overseas */
	public List transferHistoryOverseas(Map queryParam) throws NTBException;

	/* View Transfer In Bank */
	public TransferBank viewInBANK(String transID) throws NTBException;

	/* List TransferIn Macau History */
	public List transferHistoryInMacau(String dateFrom, String dateTo,
			String corpID, String userID, String fromAccount)
			throws NTBException;

	/* List TransferIn Oversea History */
	public List transferHistoryInOversea(String dateFrom, String dateTo,
			String corpID, String userID, String fromAccount)
			throws NTBException;

	/* List Transfer History */
	public List transferHistory(String business_type, Date dateFrom,
			Date dateTo, String corpID, String userID, String fromAccount)
			throws NTBException;

	/* View Transfer In Macau */
	public TransferMacau viewInMacau(String transID) throws NTBException;

	/* View Transfer In Oversea */
	public TransferOversea viewInOversea(String transID) throws NTBException;

	public List loadOversea(String coutry, String city, String action)
			throws NTBException;

	public HashMap loadOversea(String coutry) throws NTBException;

	public List loadOversea() throws NTBException;
	public List loadAccount(String corpId) throws NTBException;//add by linrui for mul-ccy
	public List loadAccountCheckBook(String corpId) throws NTBException;//add by linrui for mul-ccy checkbook
	
	public String loadCgd(String bank) throws  NTBException;
	public String loadCgdMacau(String bank) throws  NTBException;

	// edit by mxl 0912
	public Map toHostInBANK(TransferBank transferBank, CorpUser user ,String txnType) throws NTBException;

	public Map toHostInMacau(TransferMacau transferMacau, CorpUser user ,String txnType) throws NTBException;

	public Map toHostOverseas(TransferOversea transferOversea, CorpUser user, String txnType) throws NTBException;
	
	//public Map toHostChargeEnquiry(String transID, CorpUser user, BigDecimal tranAmount, String countryCode, String bankID, String CGD, String fromCurrency, String toCurrency, String bankCharges) throws NTBException; 
	
	// edit by mxl 0827
	public Map toHostChargeEnquiryNew(String transID, CorpUser user, BigDecimal tranAmount, String countryCode,  String CGD, String fromCurrency, String toCurrency, String chargeBy ,String chargeCurrency ,String ibanFlag) throws NTBException; 
	
	
	/* add by mxl 0819 */
	
	//public Map toHostAccountInBANK(String transID, CorpUser user) throws NTBException;
	
	public Map toHostAccountInBANK(String transID, CorpUser user, String toAccount) throws NTBException;
	//add by linrui for mul-ccy
	public Map toHostAccountInBANK(String transID, CorpUser user, String toAccount, String toCcy) throws NTBException;
	
	
	
	/* add 0808 mxl */
    public void approveBank(TransferBank transferBank) throws NTBException;
	
	public void rejectBank(TransferBank transferBank) throws NTBException;
	
	/* add 0810 mxl */
	public void approveMacau(TransferMacau transferMacau) throws NTBException;
	public void rejectMacau(TransferMacau transferMacau) throws NTBException;
	public void approveOversea(TransferOversea transferOversea) throws NTBException;
	public void rejectOversea(TransferOversea transferOversea) throws NTBException;
    
	
	//add bymxl 0817 insert a row after receiveing the fromHost 
	
	public void loadUploadBANK(TransferBank transferBank, Map Map) throws NTBException;
	
	public void loadUploadMacau(TransferMacau transferMacau, Map fromHost) throws NTBException;
	
	public void loadUploadOversea(TransferOversea transferOversea, Map fromHost) throws NTBException;
	
	// add bymxl 0818 insert a row after receiveing the fromHost exectuing enquriry chares
	
	public void uploadEnquiryBANK(TransferBank transferBank,Map fromHost) throws NTBException;
	
	public void uploadEnquiryMacau(TransferMacau transferMacau,Map fromHost) throws NTBException;
	
	public void uploadEnquiryOversea(TransferOversea transferOversea,Map fromHost) throws NTBException;
	
	// add by mxl 0818 save as a template
	public TransferBank editBANK(TransferBank transferBank,String userID) throws NTBException;
	public TransferMacau editMacau(TransferMacau transferMacau,String userID) throws NTBException;
	public TransferOversea  editOversea(TransferOversea transferOversea,String userID) throws NTBException;
	// add by mxl 0921 update 
	public TransferBank updateBANK(TransferBank transferBank) throws NTBException;
	public TransferMacau updateMacau(TransferMacau transferMacau) throws NTBException;
	public TransferOversea  updateOversea(TransferOversea transferOversea) throws NTBException;
	/* add by mxl 1031List Batch Transfer History */
	public List fileRequestHistory(String batchType, Date dateFrom,
			Date dateTo, String corpID, String userID, String fromAccount)
			throws NTBException;
	//add by Peng Haisen 2009-10-10 for CR103
	public List fileRequestHistoryForRequestHis(String batchType, Date dateFrom,
			Date dateTo, String corpID, String groupId, String fromAccount)
			throws NTBException;
	public List fileRequestHistoryMtS(String batchType, Date dateFrom,
			Date dateTo, String corpID, String userID, String toAccount)
			throws NTBException;
    //by mxl 1102
	public List listTransferBankStmByBatchid(String batchId) throws NTBException;
	public List listTransferBankMtsByBatchid(String batchId) throws NTBException;
	public List listTransferMacauStmByBatchid(String batchId) throws NTBException;
	public List listTransferOverseaStmByBatchid(String batchId) throws NTBException;
	//By mxl 0119
	public String loadSpbankLabel(String countryCode) throws  NTBException;
//	By mxl 0120
	public String loadAlphal(String transCode) throws  NTBException;
	
	//added by xyf 20090721, check the transfer country to find optional or mandatory flag
	public String getAddrMandatoryFlagOversea(String countryCode, String lang) throws  NTBException;
	
	//Add by heyongjijang 20100806
	public int checkNeedPurpose(String corpId, String accountNo, String transferAmount, String debitAmount, String fromCurrency, String toCurrency, String toCountryCode) throws NTBException;
	//public String getPurposeDescription(String purposeCode) throws NTBException;
	//Add by heyongjiang end
	
	/**
	 * by wency 20130118
	 * get list for checking duplicated input
	 */
	public List listTransferEntity4CheckDuplicatedInput(String transferType)throws NTBException;
	
	//add by wcc 20190402
	public String getBenName(String corpId) throws  NTBException;
	public Map viewCurrentDetail(String accountNo) throws NTBException;
	//add by linrui 20190619
	public Map toHostInMacauTrial(TransferMacau transferMacau, CorpUser user) throws NTBException ;
	public Map toHostOverseasTrial(TransferOversea transferOversea, CorpUser user) throws NTBException ;
	public Map toHostInBankTrial(TransferBank transferBank, CorpUser user) throws NTBException ;
	//end
	
	//add by lzg 20190708
	public void checkConvertibility(String fromCurrency,String toCurrency) throws NTBException ;
	public List loadAccountForRemittance(String corpId) throws NTBException;
	//add by lzg end
	//add by linrui 20190729 for mul-language
	public List loadOversea(String coutry, String city, String action,String lang)throws NTBException;
	public List getCountryList(Locale lang) throws NTBException;
	public void removeTransfer(String tableName,String transId);
	
}