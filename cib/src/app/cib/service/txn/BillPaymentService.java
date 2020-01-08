/**
 * 
 */
package app.cib.service.txn;

import java.math.BigDecimal;
import java.util.*;

import app.cib.bo.bat.FileRequest;
import app.cib.bo.enq.MerchantBillBean;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.BillPayment;

import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 *
 */
public interface BillPaymentService {
	/*
	 * process a payment operat
	 */
	public void processPayment(BillPayment pojoPayment)throws NTBException;
	
	public List listBillNo(String corpId) throws NTBException;
	
	/* 
	 * list a payment history list
	 */
	public List listHistory (String corpId, String userId, String dateFrom, String dateTo,
			String merchant, String fromAcc) throws NTBException;
	
	/*
	 * load a payment
	 */
	public BillPayment viewPayment(String transID, String payType) throws NTBException;
	public BillPayment viewPayment(String transId) throws NTBException;
	
	public String getPayType(String transId) throws NTBException;
	
	public boolean isAuthorizatin (String transId) throws NTBException;
	
	public String txnType2PayType(String approveTxnType) throws NTBException;
	
	public String payType2TxnType(String payType) throws NTBException;
	
	public void approvePayment(BillPayment billPayment, CorpUser corpUser, BigDecimal equivalentMOP) throws NTBException;
	
	public void rejectPayment(BillPayment billPayment) throws NTBException;
	
	public Map getGenPaymentInfo(CorpUser corpUser, String category, String merchant, String billNo) throws NTBException;
	
	public Map getTaxPaymentInfo(CorpUser corpUser, String category, String merchant, String billNo1, String billNo2) throws NTBException;
	
	public Map getCardPaymentInfo(CorpUser corpUser, String cardNo) throws NTBException;
	
	public Map getCEMBillInfo(CorpUser corpUser, String billNo) throws NTBException;
	
	public Map getSAAMBillInfo(CorpUser corpUser, String billNo) throws NTBException;
	
	public Map getCTMBillInfo(CorpUser corpUser, String billNo) throws NTBException;
	
	public String checkOtherMerchant(String category, String merchant) throws NTBException;
	
	public MerchantBillBean getMerchantBill(String category, String merchant) throws NTBException;
	
	public void processBatchPayment(FileRequest fileRequest, List detailList) throws NTBException;
	
	public void approveBatchPayment(String batchId) throws NTBException;
	
	public void rejectBatchPayment(String batchId) throws NTBException;
	
	public List listRecordByBatchId(String batchId) throws NTBException;
	
	public List listBatchPaymentHistory(String corpId, String userId,
			String fromAcc,	String dateFrom, String dateTo) throws NTBException;
}
