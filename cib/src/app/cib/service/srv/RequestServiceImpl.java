package app.cib.service.srv;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.exception.NTBWarningException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bat.FileRequest;
import app.cib.bo.srv.ReqBankDraft;
import app.cib.bo.srv.ReqCashierOrder;
import app.cib.bo.srv.ReqChequeProtection;
import app.cib.bo.srv.ReqStopCheque;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibIdGenerator;
import app.cib.core.CibTransClient;
import app.cib.dao.srv.RequestDao;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.txn.TransferLimitService;
import app.cib.service.txn.TransferService;
import app.cib.service.utl.UtilService;
import app.cib.util.Constants;
import app.cib.util.RcCurrency;


public class RequestServiceImpl implements RequestService{
	private static String SELECT_TRANS_ID_BANK = "Select TRANS_ID from REQ_BANK_DRAFT where BATCH_ID=?";
	private static String SELECT_TRANS_ID_CASH = "Select TRANS_ID from REQ_CASHIER_ORDER where BATCH_ID=?";
	/*private static String SELECT_TRANS_ID_STOP = "Select TRANS_ID from REQ_STOP_CHEQUE where BATCH_ID=?";dudu*/
	private static String SELECT_TRANS_ID_STOP = "Select TRANS_ID from REQ_STOP_CHEQUE where TRANS_ID=?";
	private static String SELECT_TRANS_ID_PROTECTION = "Select TRANS_ID from REQ_CHEQUE_PROTECTION where BATCH_ID=?";
	private static String SELECT_CORP_ACCOUNT = "Select ACCOUNT_NO from CORP_ACCOUNT Where CORP_ID=?";
	private static String SELECT_ACCOUNT = "Select ACCOUNT_NUMBER from HS_CHEQUE_PROTECTION Where CORPORATION_ID=?";
	private static String SELECT_TOTAL_NUMBER = "Select TOTAL_NUMBER from FILE_REQUEST Where CORP_ID=? and ORDER_DATE=? and BATCH_REFERENCE= ? and BATCH_TYPE= ?";
	private static String SELECT_ACCOUNT_DESCRIPTION = "Select ACCOUNT_DESC from CORP_ACCOUNT Where ACCOUNT_NO=?";
	private RequestDao requestDao;
	private GenericJdbcDao genericJdbcDao;
	public RequestDao getTransferDao() {
        return requestDao;
    }

    public void setRequestDao(RequestDao requestDao) {
        this.requestDao = requestDao;
    }
    public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}
	public FileRequest addFileRequest(FileRequest fileRequest) throws NTBException {
		 if (fileRequest != null) {
			 requestDao.add(fileRequest);
	        } else {
	            throw new NTBException("err.txn.fileRequestIsNull");
	        }
	        return fileRequest;
	}

	public ReqBankDraft addReqBankDraft(ReqBankDraft pojoReqBankDraft) throws NTBException {
		if (pojoReqBankDraft != null) {
			 requestDao.add(pojoReqBankDraft);
	        } else {
	            throw new NTBException("err.txn.pojoReqBankDraft");
	        }
	        return pojoReqBankDraft;
	}

	public ReqCashierOrder addCashierOrder(ReqCashierOrder pojoReqCashierOrder) throws NTBException {
		if (pojoReqCashierOrder != null) {
			 requestDao.add(pojoReqCashierOrder);
	        } else {
	            throw new NTBException("err.txn.pojoReqBankDraft");
	        }
	        return pojoReqCashierOrder;
	}

	public ReqChequeProtection addChequeProtection(ReqChequeProtection pojoChequeProtection) throws NTBException {
		if (pojoChequeProtection != null) {
			 requestDao.add(pojoChequeProtection);
	        } else {
	            throw new NTBException("err.txn.pojoReqBankDraft");
	        }
	        return pojoChequeProtection;
	}

	public ReqStopCheque addStopCheque(ReqStopCheque pojoReqStopCheque) throws NTBException {
		if (pojoReqStopCheque != null) {
			 requestDao.add(pojoReqStopCheque);
	        } else {
	            throw new NTBException("err.txn.pojoReqBankDraft");
	        }
	        return pojoReqStopCheque;
	}

	public Map toHostCashierOrder(String batchID, CorpUser user) throws NTBException {
        //��ʼ��Service
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext.getBean("RequestService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
		TransferLimitService transferLimitService = (TransferLimitService)appContext.getBean("TransferLimitService");
		// add by lw 20100904
		TransferService transferService = (TransferService) appContext
		.getBean("TransferService");
		// add by lw end
	    Map toHost = new HashMap();
	    Map fromHost = new HashMap();
	    RcCurrency rcCurrency = new RcCurrency();
	    CibTransClient testClient = new CibTransClient("CIB", "ZC06");
	    FileRequest fileRequest = requestService.viewFileRequest(batchID);
	    fileRequest.setStatus(Constants.STATUS_NORMAL);
	    fileRequest.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
	    fileRequest.setExecuteTime(new Date());
        requestService.updateFileRequest(fileRequest);
        
        BigDecimal equivalentMOP = exRatesService.getEquivalent(user.getCorpId(),
        		fileRequest.getToCurrency(), "MOP",
                                   new BigDecimal(fileRequest.getToAmount().
                                                  toString()), null, 2);
        transferLimitService.addUsedLimitQuota(fileRequest.getFromAccount(),
                                               user.getCorpId(), Constants.TXN_TYPE_CASHIER_ORDER,
                                               fileRequest.getFromAmount().
                                               doubleValue(),
                                               equivalentMOP.doubleValue());
	    
	    //����toHost
	    toHost.put("DEBIT_ACC_NO", fileRequest.getFromAccount().trim());
	    toHost.put("TOTAL_DEBIT_AMT", fileRequest.getToAmount());
	    toHost.put("DEBIT_CCY", rcCurrency.getCbsNumberByCcy( fileRequest.getToCurrency()));
	    toHost.put("NO_OF_CASHIER_ORDER", fileRequest.getTotalNumber());
	    //�ж��շ��ʻ�
	    String chargeAccount = null;
	    if(fileRequest.getChargeFlag().equals("1")) {
	    	chargeAccount = "0";
	    } else if(fileRequest.getChargeFlag().equals("2")) {
	    	chargeAccount = fileRequest.getChargeAccount();
	    }
	    toHost.put("CHRG_ACC", chargeAccount);
        // ����list
	    List accListCashier = new ArrayList();
	    List toList = new ArrayList();
	    //accListCashier = requestService.accListBank(batchID);
	    accListCashier = requestService.accListCasg(batchID);
	    if (null != accListCashier && accListCashier.size() > 0) {
	     for (int i=0; i<accListCashier.size();i++) {
	    	 Map transIdMap = new HashMap();
	    	 Map accountMap = new HashMap();
	    	 String transId = null;
	    	 transIdMap = (Map) accListCashier.get(i);
	    	 transId = (String) transIdMap.get("TRANS_ID");
	    	 ReqCashierOrder  reqCashierOrder = requestService.viewCashierOrder(transId);
	    	 // add by mxl 1106
	    	 reqCashierOrder.setStatus(Constants.STATUS_NORMAL);
	    	 reqCashierOrder.setExecuteTime(new Date());
	    	 requestService.updateCashierOrder(reqCashierOrder);
	    	 accountMap.put("NAME_ON_CASHIER_ORDER", reqCashierOrder.getBeneficiaryName());
	    	 accountMap.put("NAME_ON_CASHIER_ORDER1", reqCashierOrder.getBeneficiaryName2());
	    	 accountMap.put("NAME_ON_CASHIER_ORDER2", reqCashierOrder.getBeneficiaryName3());
	    	 accountMap.put("CASHIER_ORDER_AMT", reqCashierOrder.getCashierAmount());
	    	 accountMap.put("BNK_DRAFT_CHRG_AMT", reqCashierOrder.getChargeAmount());
	    	 // add by lw 20100905
	 		 accountMap.put("PURPOSE_CODE", "99");
	 		 accountMap.put("OTHER_PURPOSE", reqCashierOrder.getOtherPurpose());
	 		 accountMap.put("PROOF_OF_PURPOSE", reqCashierOrder.getProofOfPurpose());
	 		Log.info("to Host CashierOrder the purpose is :" + reqCashierOrder.getOtherPurpose());
	    	// add by lw end
	    	 toList.add(accountMap);
	     }
        } 
	    toHost.put("CHRG_ACC_LIST", toList);
	    toHost.put("NO_OF_CASHIER_ORDER",String.valueOf(toList.size()));
	    testClient.setAlpha8(user, CibTransClient.TXNNATURE_REQUEST,
                CibTransClient.ACCTYPE_3RD_PARTY, batchID);
	    fromHost = testClient.doTransaction(toHost);
        // ����ײ��ɹ��򱨳�������� 
        if (!testClient.isSucceed()) {
        	throw new NTBHostException(testClient.getErrorArray());
        }
        return fromHost;
	}

	public Map toHostChequeProtection(String batchID, CorpUser user) throws NTBException {
		//��ʼ��Service
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext.getBean("RequestService");
	    Map toHost = new HashMap();
	    Map fromHost = new HashMap();
	    CibTransClient testClient = new CibTransClient("CIB", "ZC08");
	    RcCurrency rcCurrency = new RcCurrency();
    
	    FileRequest fileRequest = requestService.viewFileRequest(batchID);
		fileRequest.setStatus(Constants.STATUS_NORMAL);
	    fileRequest.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
	    fileRequest.setExecuteTime(new Date());
        requestService.updateFileRequest(fileRequest);
        
        ReqChequeProtection reqChequeProtection = null;
        String transId = null;
        // ����list
	    List accListStop =  new ArrayList();
	    accListStop = requestService.transIdProtectionCheque(batchID);
	    if (null != accListStop && accListStop.size() > 0) {
	    
	    	 Map transIdMap = new HashMap();
	    	 transIdMap = (Map) accListStop.get(0);
	    	 transId = (String) transIdMap.get("TRANS_ID");
	    	 
	    	 reqChequeProtection = requestService.viewChequeProtection(transId);
	    	 reqChequeProtection.setStatus(Constants.STATUS_NORMAL);
	 	     reqChequeProtection.setExecuteTime(new Date());
	 	     requestService.updateChequeProtection(reqChequeProtection);
        } 
	    /* add by mxl 1121 get the currency number according the currency code */
        String chequeCurrencyCode = rcCurrency.getCbsNumberByCcy(reqChequeProtection.getCurrency());   
        //����toHost
	    toHost.put("CHEQUE_AMOUNT", reqChequeProtection.getAmount());
	    toHost.put("CHEQUE_NO", reqChequeProtection.getChequeNumber());
	    toHost.put("CHEQUE_ACCOUNT", reqChequeProtection.getAccount());
	    toHost.put("CHEQUE_STYLE", reqChequeProtection.getChequeStyle());
	    toHost.put("CHEQUE_CCY", chequeCurrencyCode); 
	    toHost.put("BENEFICIARY_NAME", reqChequeProtection.getBeneficiaryName());
	    toHost.put("ISSUE_DATE", DateTime.formatDate(reqChequeProtection.getIssueDate().toString(), "yyyyMMdd"));
	    testClient.setAlpha8(user, CibTransClient.TXNNATURE_REQUEST,
                CibTransClient.ACCTYPE_3RD_PARTY, transId);
	   
	    fromHost = testClient.doTransaction(toHost);
        // ����ײ��ɹ��򱨳�������� 
        if (!testClient.isSucceed()) {
        	throw new NTBHostException(testClient.getErrorArray());
        }
        return fromHost;
	}

	public Map toHostBankDraft(String batchID, CorpUser user) throws NTBException {
        // ��ʼ��Service
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext.getBean("RequestService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
		TransferLimitService transferLimitService = (TransferLimitService)appContext.getBean("TransferLimitService");
		// add by lw 20100904
		TransferService transferService = (TransferService) appContext
		.getBean("TransferService");
		// add by lw end

	    Map toHost = new HashMap();
	    Map fromHost = new HashMap();
	    RcCurrency rcCurrency = new RcCurrency();
	    CibTransClient testClient = new CibTransClient("CIB", "ZC05");
	    FileRequest fileRequest = requestService.viewFileRequest(batchID);
	   
	    fileRequest.setStatus(Constants.STATUS_NORMAL);
	    fileRequest.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
	    fileRequest.setExecuteTime(new Date());
        requestService.updateFileRequest(fileRequest);
       
        BigDecimal equivalentMOP = exRatesService.getEquivalent(user.getCorpId(),
        		fileRequest.getToCurrency(), "MOP",
                                   new BigDecimal(fileRequest.getToAmount().
                                                  toString()), null, 2);
        transferLimitService.addUsedLimitQuota(fileRequest.getFromAccount(),
                                               user.getCorpId(), Constants.TXN_TYPE_BANK_DRAFT,                                        fileRequest.getFromAmount().
                                               doubleValue(),
                                               equivalentMOP.doubleValue());
	    //����toHost
	    toHost.put("DEBIT_ACC_NO", fileRequest.getFromAccount().trim());
	    toHost.put("TOTAL_DEBIT_AMT", fileRequest.getToAmount());
	    toHost.put("DEBIT_CCY",  rcCurrency.getCbsNumberByCcy( fileRequest.getToCurrency()));
	    toHost.put("NO_OF_BNK_DRAFT", fileRequest.getTotalNumber());
	    //�ж��շ��ʻ�
	    String chargeAccount = null;
	    if(fileRequest.getChargeFlag().equals("1")) {
	    	chargeAccount = "0";
	    } else if(fileRequest.getChargeFlag().equals("2")) {
	    	chargeAccount = fileRequest.getChargeAccount();
	    }
	    toHost.put("CHRG_ACC", chargeAccount);
	    // ����list
	    List accListBank =  new ArrayList();
	    List toList =  new ArrayList();
	    accListBank = requestService.accListBank(batchID);
	    if (null != accListBank && accListBank.size() > 0) {
	     for (int i=0; i<accListBank.size();i++) {
	    	 Map transIdMap = new HashMap();
	    	 Map accountMap = new HashMap();
	    	 String transId = null;
	    	 transIdMap = (Map) accListBank.get(i);
	    	 transId = (String) transIdMap.get("TRANS_ID");
	    	 ReqBankDraft reqBankDraft = requestService.viewBankDraft(transId);
	    	 reqBankDraft.setStatus(Constants.STATUS_NORMAL);
	    	 reqBankDraft.setExecuteTime(new Date());
	    	 requestService.updateReqBankDraft(reqBankDraft);
	    	 accountMap.put("NAME_ON_BNK_DRAFT", reqBankDraft.getBeneficiaryName());
	    	 accountMap.put("NAME_ON_BNK_DRAFT1", reqBankDraft.getBeneficiaryName2());
//	    	 accountMap.put("NAME_ON_BNK_DRAFT2", reqBankDraft.getBeneficiaryName3());
	    	 accountMap.put("NAME_ON_BNK_DRAFT2", " ");
	    	 accountMap.put("BNK_DRAFT_AMT", reqBankDraft.getDraftAmount());
	    	 accountMap.put("BNK_DRAFT_CHRG_AMT", reqBankDraft.getChargeAmount());
	    	 // add by lw 20100905
	 		 accountMap.put("PURPOSE_CODE", "99");
	 		 accountMap.put("OTHER_PURPOSE", reqBankDraft.getOtherPurpose());
	 		 accountMap.put("PROOF_OF_PURPOSE", reqBankDraft.getProofOfPurpose());
	 		 Log.info("to Host BankDraft the purpose is :" + reqBankDraft.getOtherPurpose());
	    	 // add by lw end
	    	 toList.add(accountMap);
	     }
        } 
	    
	    toHost.put("CHRG_ACC_LIST", toList);
	    toHost.put("NO_OF_BNK_DRAFT",String.valueOf(toList.size()));
	    testClient.setAlpha8(user, CibTransClient.TXNNATURE_REQUEST,
                CibTransClient.ACCTYPE_3RD_PARTY, batchID);
	    fromHost = testClient.doTransaction(toHost);
        // ����ײ��ɹ��򱨳�������� 
        if (!testClient.isSucceed()) {
        	throw new NTBHostException(testClient.getErrorArray());
        }
        return fromHost;
 
	}

	public Map toHostStopCheque(String tranId, CorpUser user) throws NTBException {
         //��ʼ��Service
		ApplicationContext appContext = Config.getAppContext();
//		RequestService requestService = (RequestService) appContext.getBean("RequestService");
		UtilService utilService = (UtilService) appContext.getBean("UtilService");
	    Map toHost = new HashMap();
	    Map fromHost = new HashMap();
	    CibTransClient testClient = new CibTransClient("CIB", "ZC07");
	    /*FileRequest fileRequest = requestService.viewFileRequest(batchID);
		fileRequest.setStatus(Constants.STATUS_NORMAL);
	    fileRequest.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
	    fileRequest.setExecuteTime(new Date());
        requestService.updateFileRequest(fileRequest);*/
        ReqStopCheque reqStopCheque = null;
        String transId = null;
        // list
	    List accListStop =  new ArrayList();
	    accListStop = transIdStopCheque(tranId);
	    Log.info("==STOP CHECK LIST IS===" + accListStop);
	    if (null != accListStop && accListStop.size() > 0) {
	    
	    	 Map transIdMap = new HashMap();
	    	 
	    	 transIdMap = (Map) accListStop.get(0);
	    	 transId = (String) transIdMap.get("TRANS_ID");
	    	 reqStopCheque = viewStopCheque(transId);
	    	 Log.info("==STOP REQ OBJECT trans id ===" + reqStopCheque.getTransId()
	    			 +"==and batch id " + reqStopCheque.getBatchId());
	    	 reqStopCheque.setStatus(Constants.STATUS_NORMAL);
	    	 reqStopCheque.setExecuteTime(new Date());
//	    	 updateStopCheque(reqStopCheque);
	    	 updateStopChequeAtfterToHost(reqStopCheque);
	    	 Log.info("===UPDATE SUCUSSES AND STATUS IS ===" + reqStopCheque.getStatus());
        } 
	    //to host
	    toHost.put("CHEQUE_AMOUNT", reqStopCheque.getAmount());
	    toHost.put("CHEQUE_NO", reqStopCheque.getChequeNumber());
	    toHost.put("CHEQUE_ACCOUNT", reqStopCheque.getCurrentAccount());
	    toHost.put("BENEFICIARY_NAME", reqStopCheque.getBeneficiaryName());
	    String issDate = Utils.null2EmptyWithTrim(reqStopCheque.getIssueDate());
	    if("".equals(issDate)){
	    	issDate="00000000";
	    }
	    toHost.put("ISSUE_DATE", issDate);
	    //Jet modified for optional expiry date
	    String expiryDate = Utils.null2EmptyWithTrim(reqStopCheque.getExpiryDate());
	    if ("".equals(expiryDate)){
	    	expiryDate="00000000";	    	
	    }
	    toHost.put("EXPIRY_DATE", expiryDate);
	    toHost.put("REASON", reqStopCheque.getStopReason());
	    toHost.put("CURRENCY", reqStopCheque.getCurrentAccountCcy());
	    toHost.put("REQ_REF", CibIdGenerator.getTDRef());
	    //ADD BY LINRUI 20190514 for type to stop cheque
	    Double amt = reqStopCheque.getAmount();
	    if(amt == 0.00){
	    	toHost.put("TYPE", "C");//ONLY CHECK CHEQUE NUM
	    }else{
	    	toHost.put("TYPE", "B");//CHECK CHEQUE NUM AND AMOUNT
	    }
	    
	    fromHost = testClient.doTransaction(toHost);
        if (!testClient.isSucceed()) {
        	throw new NTBHostException(testClient.getErrorArray());
        }
        return fromHost;
	    
	}
	//add by linrui for stopCheque 20190806
	public void rejectStopCheque(String id) throws NTBException {
		String sql_reject_stopCheuque_record = 
			"update REQ_STOP_CHEQUE set status = ? where TRANS_ID = ? and status is not null";
		try {
			this.genericJdbcDao.update(sql_reject_stopCheuque_record,
					new Object[]{Constants.STATUS_REMOVED, id});
		} catch (Exception e) {
			throw new NTBException("err.bat.RejectReqStopChequeError");
		}
	}

	public ReqBankDraft viewBankDraft(String transID) throws NTBException {
		    ReqBankDraft reqBankDraft = null;
	        if ((transID != null) && (!transID.equals(""))) {
	        	reqBankDraft = (ReqBankDraft) requestDao.load(
	        			ReqBankDraft.class, transID);
	        } else {
	            throw new NTBException("err.txn.TransIDIsNullOrEmpty");
	        }
	        return reqBankDraft;
	}

	public ReqCashierOrder viewCashierOrder(String transID) throws NTBException {
		ReqCashierOrder reqCashierOrder = null;
        if ((transID != null) && (!transID.equals(""))) {
        	reqCashierOrder = (ReqCashierOrder) requestDao.load(
        			ReqCashierOrder.class, transID);
        } else {
            throw new NTBException("err.txn.TransIDIsNullOrEmpty");
        }
        return reqCashierOrder;
	}

	public ReqChequeProtection viewChequeProtection(String transID) throws NTBException {
		ReqChequeProtection reqChequeProtection = null;
        if ((transID != null) && (!transID.equals(""))) {
        	reqChequeProtection = (ReqChequeProtection) requestDao.load(
        			ReqChequeProtection.class, transID);
        } else {
            throw new NTBException("err.txn.TransIDIsNullOrEmpty");
        }
        return reqChequeProtection;
	}

	public FileRequest viewFileRequest(String batchID) throws NTBException {
		FileRequest fileRequest = null;
        if ((batchID != null) && (!batchID.equals(""))) {
        	fileRequest = (FileRequest) requestDao.load(
        			FileRequest.class, batchID);
        } else {
            throw new NTBException("err.txn.TransIDIsNullOrEmpty");
        }
        return fileRequest;
	}

	public ReqStopCheque viewStopCheque(String transID) throws NTBException {
		ReqStopCheque reqStopCheque = null;
        if ((transID != null) && (!transID.equals(""))) {
        	reqStopCheque = (ReqStopCheque) requestDao.load(
        			ReqStopCheque.class, transID);
        } else {
            throw new NTBException("err.txn.TransIDIsNullOrEmpty");
        }
        return reqStopCheque;
	}

	public ReqCashierOrder updateCashierOrder(ReqCashierOrder pojoReqCashierOrder) throws NTBException {
		if (pojoReqCashierOrder != null) {
			 requestDao.update(pojoReqCashierOrder);
   }
   return pojoReqCashierOrder;
	}

	public ReqChequeProtection updateChequeProtection(ReqChequeProtection pojoChequeProtection) throws NTBException {
		if (pojoChequeProtection != null) {
			 requestDao.update(pojoChequeProtection);
    }
    return pojoChequeProtection;
	}

	public ReqBankDraft updateReqBankDraft(ReqBankDraft pojoReqBankDraft) throws NTBException {
		 if (pojoReqBankDraft != null) {
			 requestDao.update(pojoReqBankDraft);
     }
     return pojoReqBankDraft;
	}

	public ReqStopCheque updateStopCheque(ReqStopCheque pojoReqStopCheque) throws NTBException {
		 if (pojoReqStopCheque != null) {
			 requestDao.update(pojoReqStopCheque);
     }
     return pojoReqStopCheque;
	}

	public FileRequest updateFileRequest(FileRequest fileRequest) throws NTBException {
		 if (fileRequest != null) {
			 requestDao.update(fileRequest);
     }
     return fileRequest;
	}

	public List accListBank(String batchId) throws NTBException {
        // ��ʼ��
		// get trans_id from req_bank_draft according to the batch_id
        List accListBank = null;
        try {
          accListBank = genericJdbcDao.query(SELECT_TRANS_ID_BANK, new Object[] {batchId});
          return accListBank;
        } catch (Exception e) {
        	throw new NTBException("err.bat.GetTransIdException");
        }
		
	}

	public List accListCasg(String batchId) throws NTBException {
        // ��ʼ��
		List accListCashier = null;
        try {
        	accListCashier = genericJdbcDao.query(SELECT_TRANS_ID_CASH, new Object[] {batchId});
        	return accListCashier;
        } catch (Exception e) {
        	throw new NTBException("err.bat.GetTransIdException");
        }
		
	}

	public List listBankDraftByBatchid(String batchId) throws NTBException {
		List valueList = new ArrayList();
		if ((batchId != null) && (!batchId.equals(""))) {
			valueList =  requestDao.listReqBankDraft(batchId);
			} else {
            throw new NTBException("err.txn.BatchIdIsNullOrEmpty");
        }
        return valueList;
	}

	public List listCashierOrderByBatchid(String batchId) throws NTBException {
		List valueList = new ArrayList();
		if ((batchId != null) && (!batchId.equals(""))) {
			valueList =  requestDao.listReqCashierOrder(batchId);
			} else {
            throw new NTBException("err.txn.BatchIdIsNullOrEmpty");
        }
        return valueList;
	}

	public List listProtectionChequeByBatchid(String batchId) throws NTBException {
		List valueList = new ArrayList();
		if ((batchId != null) && (!batchId.equals(""))) {
			valueList =  requestDao.listReqChequeProtection(batchId);
			} else {
            throw new NTBException("err.txn.BatchIdIsNullOrEmpty");
        }
        return valueList;
	}

	public List listStopChequeByBatchid(String batchId) throws NTBException {
		List valueList = new ArrayList();
		if ((batchId != null) && (!batchId.equals(""))) {
			valueList =  requestDao.listReqStopCheque(batchId);
			} else {
            throw new NTBException("err.txn.BatchIdIsNullOrEmpty");
        }
        return valueList;
	}

	public List toHostBankDraftCharges(String chargeCurrency, String debitCurrency, String totalNumber,List chargeList,CorpUser user, String batchId) throws NTBException {
        // ��ʼ��Service  add by mxl 1106
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext.getBean("RequestService");
	    Map toHost = new HashMap();
	    Map fromHost = new HashMap();
	    CibTransClient testClient = new CibTransClient("CIB", "ZC12");
	    RcCurrency rcCurrency = new RcCurrency();
	    //����toHost
	    toHost.put("DEBIT_CCY", rcCurrency.getCbsNumberByCcy(debitCurrency));
	    toHost.put("CHRG_CCY", rcCurrency.getCbsNumberByCcy(chargeCurrency));
	    toHost.put("NO_OF_BNK_DRAFT", totalNumber);
	    toHost.put("CHRG_AMT_LIST", chargeList);
	    testClient.setAlpha8(user, CibTransClient.TXNNATURE_REQUEST,
                CibTransClient.ACCTYPE_3RD_PARTY, batchId);
	    fromHost = testClient.doTransaction(toHost);
        // ����ײ��ɹ��򱨳�������� 
        if (!testClient.isSucceed()) {
        	throw new NTBHostException(testClient.getErrorArray());
        }
        List retList = (List) fromHost.get("CHARGE_LIST");
        return retList;
	}

	public List toHostCashierOrderCharges(String chargeCurrency, String debitCurrency, String totalNumber,List chargeList,CorpUser user, String batchId) throws NTBException {
		 // ��ʼ��Service  add by mxl 1106
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext.getBean("RequestService");
	    Map toHost = new HashMap();
	    Map fromHost = new HashMap();
	    CibTransClient testClient = new CibTransClient("CIB", "ZC13");
	    RcCurrency rcCurrency = new RcCurrency();
	    //����toHost
	    toHost.put("DEBIT_CCY", rcCurrency.getCbsNumberByCcy(debitCurrency));
	    toHost.put("CHRG_ACC", rcCurrency.getCbsNumberByCcy(chargeCurrency));
	    toHost.put("NO_OF_CASHIER_ORDER", totalNumber);
	    toHost.put("CHRG_AMT_LIST", chargeList);
	    testClient.setAlpha8(user, CibTransClient.TXNNATURE_REQUEST,
                CibTransClient.ACCTYPE_3RD_PARTY, batchId);
	    fromHost = testClient.doTransaction(toHost);
	    // ����ײ��ɹ��򱨳�������� 
        if (!testClient.isSucceed()) {
        	throw new NTBHostException(testClient.getErrorArray());
        }
        List retList = (List) fromHost.get("CHARGE_LIST");
        return retList;
	}

	public List accListChequeProtection(String corpId) throws NTBException {
	
        List accList = null;
        try {
        	accList = genericJdbcDao.query(SELECT_ACCOUNT, new Object[] {corpId});
        	return accList;
        } catch (Exception e) {
        	throw new NTBException("err.srv.GetAccountException");
        }
		
	}

	public List transIdProtectionCheque(String batchId) throws NTBException {
		 // ��ʼ��
		List accListProtection = null;
        try {
        	accListProtection = genericJdbcDao.query(SELECT_TRANS_ID_PROTECTION, new Object[] {batchId});
        	return accListProtection;
        } catch (Exception e) {
        	throw new NTBException("err.srv.GetTransIdException");
        }
		
	}

	public List transIdStopCheque(String batchId) throws NTBException {
		 // ��ʼ��
		List accListStop = null;
        try {
        	accListStop = genericJdbcDao.query(SELECT_TRANS_ID_STOP, new Object[] {batchId});
        	return accListStop;
        } catch (Exception e) {
        	throw new NTBException("err.srv.GetTransIdException");
        }
		
	}

	public List accListCorpAccount(String corpId) throws NTBException {
		List accList = null;
        try {
        	accList = genericJdbcDao.query(SELECT_CORP_ACCOUNT, new Object[] {corpId});
        	return accList;
        } catch (Exception e) {
        	throw new NTBException("err.srv.GetAccountException");
        }
		
	}

	public List listFileRequestBythreekeys(String corpId, String orderDate, String batchReference, String batchType) throws NTBException {
		List accList = null;
        try {
        	accList = genericJdbcDao.query(SELECT_TOTAL_NUMBER, new Object[] {corpId,orderDate,batchReference,batchType});
        } catch (Exception e) {
        	throw new NTBException("err.srv.GetFileRequestException");
        }
		return accList;
	}

	public String getAccountDescription(String account) throws NTBException {
		HashMap accountDescMap = new HashMap();
        String accountDesc = null;
        try {
			List idList = null;
			idList = genericJdbcDao.query(SELECT_ACCOUNT_DESCRIPTION,
					new Object[] { account });
			accountDescMap = (HashMap) idList.get(0);
			accountDesc = (String) accountDescMap.get("ACCOUNT_DESC");
			return accountDesc;

		} catch (Exception e) {
			throw new NTBException("err.srv.GetAccountDescError");
		}
		
	}

	public void checkAmount(String amount) throws NTBException {
		String[] amountArray = amount.split(",");
		int dayPerMonth = 0;
		for (int i=0; i<amountArray.length; i++) {
			try {
				dayPerMonth = Integer.parseInt(amountArray[i]);
			} catch (Exception e) {
				throw new NTBException("err.bat.AmountFieldError");
			}
			if (dayPerMonth<0 || dayPerMonth>9) {
				throw new NTBException("err.bat.AmountFieldError");
			}
		}
	}
    // add by mxl 0124
	public List listReqStopCheque(Date dateFrom, Date dateTo, String corpID, String userID, String chequeNumber) throws NTBException {
		 List historyList = null;
         historyList = requestDao.listReqStopChequeHistory( dateFrom,  dateTo,  corpID,  userID,  chequeNumber);
         return historyList;
	}
	//add by Peng Haisen 2009-10-10 for CR103
	public List listReqStopChequeForRequestHis(Date dateFrom, Date dateTo, String corpID, String groupId, String chequeNumber) throws NTBException {
		 try {
				List historyList = null;

				List valueList = new ArrayList();
//				String sql = "Select tb.* from REQ_STOP_CHEQUE  tb,CORP_USER  t2 where tb.USER_ID=t2.USER_ID and tb.CORP_ID= ? ";
				//mod by linrui 20190612
				String sql = "Select to_char(tb.request_time,'yyyy-mm-dd hh24:mi:ss') as REQUEST_TIME," +
						"tb.current_account as CURRENT_ACCOUNT,tb.beneficiary_name as BENEFICIARY_NAME,tb.issue_date as ISSUE_DATE," +
						"tb.expiry_date as EXPIRY_DATE,tb.status as STATUS,tb.reason_flag as REASON_FLAG," +
						"tb.stop_reason as STOP_REASON from REQ_STOP_CHEQUE  tb,CORP_USER  t2 where tb.USER_ID=t2.USER_ID and tb.CORP_ID=t2.CORP_ID and tb.CORP_ID= ? ";
				valueList.add(corpID);

				if ((dateFrom != null) && (!dateFrom.equals(""))) {
					sql +="and tb.REQUEST_TIME >= ? ";
					valueList.add(dateFrom);
				}

				if ((dateTo != null) && (!dateTo.equals(""))) {
					sql +="and tb.REQUEST_TIME <= ? ";
					valueList.add(dateTo);
				}

				if ((groupId != null) && (!groupId.equals(""))) {
					sql += "and  t2.GROUP_ID = ? ";
					valueList.add(groupId);
				}
				if ((chequeNumber != null) && (!chequeNumber.equals(""))) {
					sql +="and  tb.CHEQUE_NUMBER = ? ";
					valueList.add(chequeNumber);
				}
				//add by linrui 20190919
				sql +=" order by REQUEST_TIME DESC ";
				Log.info("----SELECT STOP HISTORY,AND SQL:"+sql);
				Log.info("----SELECT STOP HISTORY,AND valueList.toArray():"+ valueList);
				historyList = genericJdbcDao.query(sql, valueList.toArray());
				Log.info("----SELECT STOP HISTORY,AND historyList:"+historyList);
				return historyList;
			} catch (Exception e) {
				Log.error("RequestServiceImpl.listReqStopChequeForRequestHis ERROR :", e);
				throw new NTBException("err.sys.DBError");
			}
	}
	public List listReqChequeForRequestHis(Date dateFrom, Date dateTo, String corpID, String groupId) throws NTBException {
		try {
			List historyList = null;
			List valueList = new ArrayList();
			String sql = "select to_char(cbr.request_time,'yyyy-mm-dd hh24:mi:ss') as REQUEST_TIME,cbr.ACCOUNT_NO as ACCOUNT_NO,cbr.PACKAGE_NAME as PACKAGE_NAME,cbr.PACKAGE_ADRESS as PACKAGE_ADRESS,cbr.NO_OF_BOOK as NO_OF_BOOK,cbr.STATUS as STATUS,cbr.PAY_CURRENCY as PAY_CURRENCY,cbr.PICKUP_TYPE as PICKUP_TYPE from CHEQUE_BOOK_REQUEST cbr,CORP_USER  t2 where cbr.USER_ID=t2.USER_ID and cbr.CORP_ID=t2.CORP_ID and cbr.CORP_ID= ?";
			valueList.add(corpID);
			
			if ((dateFrom != null) && (!dateFrom.equals(""))) {
				sql +="and cbr.REQUEST_TIME >= ? ";
				valueList.add(dateFrom);
			}
			
			if ((dateTo != null) && (!dateTo.equals(""))) {
				sql +="and cbr.REQUEST_TIME <= ? ";
				valueList.add(dateTo);
			}
			
			if ((groupId != null) && (!groupId.equals(""))) {
				sql += "and  t2.GROUP_ID = ? ";
				valueList.add(groupId);
			}
			//add by linrui 20190919
			sql+= " order by REQUEST_TIME desc";
			Log.info("----SELECT CHEQUE_BOOK_REQUEST HISTORY,AND SQL:"+sql);
			Log.info("----SELECT CHEQUE_BOOK_REQUEST HISTORY,AND valueList.toArray():"+ valueList);
			historyList = genericJdbcDao.query(sql, valueList.toArray());
			Log.info("----SELECT CHEQUE_BOOK_REQUEST HISTORY,AND historyList:"+historyList);
			return historyList;
		} catch (Exception e) {
			Log.error("RequestServiceImpl.listReqStopChequeForRequestHis ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	
	public List listReqChequeProtection(Date dateFrom, Date dateTo, String corpID, String userID, String chequeNumber) throws NTBException {
		List historyList = null;
        historyList = requestDao.listReqChequeProtectionHistory(dateFrom,  dateTo,  corpID,  userID,  chequeNumber);
        return historyList;
	}


	//add by Peng Haisen 2009-10-10 for CR103
	public List listReqChequeProtectionForRequestHis(Date dateFrom, Date dateTo, String corpID, String groupId, String chequeNumber) throws NTBException {
		 try {
				List historyList = null;

				List valueList = new ArrayList();
				String sql = "Select tb.* from REQ_CHEQUE_PROTECTION as tb,CORP_USER as t2 where tb.USER_ID=t2.USER_ID and tb.CORP_ID= ? ";
				valueList.add(corpID);

				if ((dateFrom != null) && (!dateFrom.equals(""))) {
					sql +="and tb.REQUEST_TIME >= ? ";
					valueList.add(dateFrom);
				}

				if ((dateTo != null) && (!dateTo.equals(""))) {
					sql +="and tb.REQUEST_TIME <= ? ";
					valueList.add(dateTo);
				}

				if ((groupId != null) && (!groupId.equals(""))) {
					sql += "and  t2.GROUP_ID = ? ";
					valueList.add(groupId);
				}
				if ((chequeNumber != null) && (!chequeNumber.equals(""))) {
					sql +="and  tb.CHEQUE_NUMBER = ? ";
					valueList.add(chequeNumber);
				}
				historyList = genericJdbcDao.query(sql, valueList.toArray());
				return historyList;
			} catch (Exception e) {
				Log.error("RequestServiceImpl.listReqChequeProtectionForRequestHis ERROR :", e);
				throw new NTBException("err.sys.DBError");
			}
	}

	public void removeStopCheque(String tableName, String transNo) {
		genericJdbcDao.removeTransfer(tableName,transNo);
		
	}
	public void updateStopChequeAtfterToHost(ReqStopCheque pojoReqStopCheque) throws NTBException {
        String sqlUdt = "UPDATE REQ_STOP_CHEQUE SET STATUS = ? ,EXECUTE_TIME= ? WHERE TRANS_ID = ?";
        try {
			genericJdbcDao.update(sqlUdt,  new Object[] {Constants.STATUS_NORMAL, new Date(), pojoReqStopCheque.getTransId()});
		} catch (Exception e) {
			Log.error("RequestServiceImpl.updateStopChequeAtfterToHost ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	

}
