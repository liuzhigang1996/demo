package app.cib.service.srv;

import java.util.Date;
import java.util.List;
import java.util.Map;

import app.cib.bo.bat.FileRequest;
import app.cib.bo.srv.ReqBankDraft;
import app.cib.bo.srv.ReqCashierOrder;
import app.cib.bo.srv.ReqChequeProtection;
import app.cib.bo.srv.ReqStopCheque;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TransferMacau;

import com.neturbo.set.exception.NTBException;

public interface RequestService {
	public FileRequest addFileRequest(FileRequest fileRequest) throws NTBException;
	public ReqBankDraft addReqBankDraft(ReqBankDraft pojoReqBankDraft) throws NTBException;
	public ReqCashierOrder addCashierOrder(ReqCashierOrder pojoReqCashierOrder) throws NTBException;
	public ReqStopCheque addStopCheque(ReqStopCheque pojoReqStopCheque) throws NTBException;
	public ReqChequeProtection addChequeProtection(ReqChequeProtection pojoChequeProtection) throws NTBException;
	public ReqBankDraft viewBankDraft(String transID) throws NTBException;
	public ReqCashierOrder viewCashierOrder(String transID) throws NTBException;
	public ReqStopCheque viewStopCheque(String transID) throws NTBException;
	public ReqChequeProtection viewChequeProtection(String transID) throws NTBException;
	public FileRequest viewFileRequest(String batchID) throws NTBException;
	public ReqBankDraft updateReqBankDraft(ReqBankDraft pojoReqBankDraft) throws NTBException;
	public ReqCashierOrder updateCashierOrder(ReqCashierOrder pojoReqCashierOrder) throws NTBException;
	public ReqStopCheque updateStopCheque(ReqStopCheque pojoReqStopCheque) throws NTBException;
	public ReqChequeProtection updateChequeProtection(ReqChequeProtection pojoChequeProtection) throws NTBException;
	public FileRequest updateFileRequest(FileRequest fileRequest) throws NTBException;
    //edit by mxl 1016
	public Map toHostBankDraft(String batchID, CorpUser user) throws NTBException;
	public Map toHostCashierOrder(String batchID, CorpUser user) throws NTBException;
	public Map toHostStopCheque(String batchID, CorpUser user) throws NTBException;
	public void rejectStopCheque(String batchID) throws NTBException;//add by linrui 20190806
	public Map toHostChequeProtection(String batchID, CorpUser user) throws NTBException;
	
	//add by mxl 1103
	public List toHostBankDraftCharges(String chargeCurrency, String debitCurrency, String totalNumber,List chargeList,CorpUser user, String batchID) throws NTBException;
	public List toHostCashierOrderCharges(String chargeCurrency, String debitCurrency, String totalNumber,List chargeList,CorpUser user, String batchID) throws NTBException;
	
	//
	public List accListBank(String batchId) throws NTBException;
	public List accListCasg(String batchId) throws NTBException;
	public List transIdStopCheque(String batchId) throws NTBException;
	public List transIdProtectionCheque(String batchId) throws NTBException;
	// by mxl 1101
	public List listBankDraftByBatchid(String batchId) throws NTBException;
	public List listCashierOrderByBatchid(String batchId) throws NTBException;
	public List listStopChequeByBatchid(String batchId) throws NTBException;
	public List listProtectionChequeByBatchid(String batchId) throws NTBException;
	// add by mxl 1108
	public List accListChequeProtection(String corpId) throws NTBException;
	// add by mxl 1115
	public List accListCorpAccount(String corpId) throws NTBException;
	
	//add by mxl 1225
	public List listFileRequestBythreekeys(String corpId, String orderDate,String batchReference, String batchType) throws NTBException;
	//add by mxl 0106
	public String getAccountDescription(String account) throws NTBException;
	//add by mxl 0117
	public void checkAmount(String amount) throws NTBException;
	public List listReqStopCheque( Date dateFrom,Date dateTo, String corpID, String userID,String chequeNumber) throws NTBException;

	//add by Peng Haisen 2009-10-10 for CR103
	public List listReqStopChequeForRequestHis( Date dateFrom,Date dateTo, String corpID, String groupId,String chequeNumber) throws NTBException;
	//add by lzg 20190722
	public List listReqChequeForRequestHis( Date dateFrom,Date dateTo, String corpID, String groupId) throws NTBException;

	// add by mxl 0124
	public List listReqChequeProtection( Date dateFrom,Date dateTo, String corpID, String userID,String chequeNumber) throws NTBException;
	
	//add by Peng Haisen 2009-10-10 for CR103
	public List listReqChequeProtectionForRequestHis( Date dateFrom,Date dateTo, String corpID, String groupId,String chequeNumber) throws NTBException;
	public void removeStopCheque(String tableName, String transNo);
	

}
