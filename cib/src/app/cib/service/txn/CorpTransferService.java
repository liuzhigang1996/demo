package app.cib.service.txn;

import java.util.Date;
import java.util.List;

import app.cib.bo.txn.TransferBank;

import com.neturbo.set.exception.NTBException;

/**
 * @author mxl
 * 2006-08-01
 */
public interface CorpTransferService {
	/*  Delivery transfer Account to Account in Corp */
	public TransferBank transferDelivery(TransferBank pojoTransfer) throws NTBException;
	
	/*  Subsidiary transfer Account to Account in Corp */
	public TransferBank transferSubsidiary(TransferBank pojoTransfer) throws NTBException;
	
	/*  Repatriate transfer Account to Account in Corp */
	public TransferBank transferRepatriate(TransferBank pojoTransfer) throws NTBException;
	
	/* List Transfer  History */
	public List transferHistory(Date dateFrom, Date dateTo, String fromCcorpID, String userID, String fromAccount, String corpId) throws NTBException;
	
	/* View Transfer In Bank */
	public TransferBank viewInBANK(String transID) throws NTBException;
	
	/* add 0810 mxl */
    public void approveBank(TransferBank transferBank) throws NTBException;
	
	public void rejectBank(TransferBank transferBank) throws NTBException;
	
    //add by mxl 0110
	public String getCorpIdByAccount(String account) throws NTBException;

}
