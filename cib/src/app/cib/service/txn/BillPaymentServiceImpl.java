/**
 * 2006-07-14
 */
package app.cib.service.txn;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bat.FileRequest;
import app.cib.bo.enq.LceBean;
import app.cib.bo.enq.MerchantBillBean;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.BillPayment;
import app.cib.core.CibIdGenerator;
import app.cib.core.CibTransClient;
import app.cib.dao.enq.ExchangeRatesDao;
import app.cib.dao.srv.RequestDao;
import app.cib.dao.txn.BillPaymentDao;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.utl.UtilService;
import app.cib.util.Constants;
import app.cib.util.RcCurrency;
import app.cib.util.UploadReporter;

/**
 * @author hjs
 * 2006-07-14
 */
public class BillPaymentServiceImpl implements BillPaymentService{

	/**
	 * @param args
	 */
    private static final String batFileDir = Config.getProperty("BatchFileUploadDir") + "/";
	private BillPaymentDao billPaymentDao;
	private GenericJdbcDao genericJdbcDao;
	private RequestDao requestDao;
	private String uploadFileName;


	public BillPaymentDao getBillPaymentDao() {
		return billPaymentDao;
	}

	public void setBillPaymentDao(BillPaymentDao billPaymentDao) {
		this.billPaymentDao = billPaymentDao;
	}

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	public RequestDao getRequestDao() {
		return requestDao;
	}

	public void setRequestDao(RequestDao requestDao) {
		this.requestDao = requestDao;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	/*
	 * process a payment operat
	 */
	public void processPayment(BillPayment pojoPayment) throws NTBException {
		pojoPayment.setOperation(Constants.OPERATION_NEW);
		pojoPayment.setStatus(Constants.STATUS_PENDING_APPROVAL);
		pojoPayment.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
		billPaymentDao.add(pojoPayment);
	}


	public List listBillNo(String corpId) throws NTBException {
		List billNoList = this.billPaymentDao.listBillNo(corpId);
		return billNoList;
	}

	/*
	 * list a payment history list
	 */
	public List listHistory(String corpId, String userId, String dateFrom, String dateTo, 
			String merchant, String fromAcc) throws NTBException {
		List historyList = billPaymentDao.listHistory(corpId, userId, dateFrom, dateTo, merchant, fromAcc);
		//historyList = billPaymentDao.list(BillPayment.class, queryParam);
		return historyList;
	}

	public BillPayment viewPayment(String transID, String payType) throws NTBException {
		Map conditionMap = new HashMap();
		conditionMap.put("transId", transID);
		conditionMap.put("payType", payType);

		List paymentList = billPaymentDao.list(BillPayment.class, conditionMap);
		if (paymentList.size()>0){
			BillPayment billPayment =  (BillPayment) paymentList.get(0);
			return billPayment;
		} else {
			return null;
		}
	}

	public BillPayment viewPayment(String transId) throws NTBException {
		return (BillPayment)billPaymentDao.load(BillPayment.class, transId);
	}

	public String getPayType(String transId) throws NTBException {
		BillPayment billPayment = (BillPayment) billPaymentDao.load(BillPayment.class, transId);
		if (billPayment != null) {
			return billPayment.getPayType();
		} else {
			return null;
		}

	}

	public boolean isAuthorizatin(String transId) throws NTBException {
		BillPayment billPayment = (BillPayment) billPaymentDao.load(BillPayment.class, transId);
		if (billPayment != null) {
			if ((billPayment.getStatus().equals(Constants.STATUS_NORMAL)) && (billPayment.getAuthStatus().equals(Constants.AUTH_STATUS_COMPLETED))) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	public String txnType2PayType(String txnType) throws NTBException {
		if (txnType.equals(Constants.TXN_SUBTYPE_GENERAL_PAYMENT)) {
			return BillPayment.PAYMENT_TYPE_GENERAL;
		} else if (txnType.equals(Constants.TXN_SUBTYPE_CARD_PAYMENT)){
			return BillPayment.PAYMENT_TYPE_CARD;
		} else if (txnType.equals(Constants.TXN_SUBTYPE_TAX_PAYMENT)){
			return BillPayment.PAYMENT_TYPE_TAX;
		} else {
			return null;
		}
	}

	public String payType2TxnType(String payType) throws NTBException {
		if (payType.equals(BillPayment.PAYMENT_TYPE_GENERAL)) {
			return Constants.TXN_SUBTYPE_GENERAL_PAYMENT;
		} else if (payType.equals(BillPayment.PAYMENT_TYPE_CARD)){
			return Constants.TXN_SUBTYPE_CARD_PAYMENT;
		} else if (payType.equals(BillPayment.PAYMENT_TYPE_TAX)){
			return Constants.TXN_SUBTYPE_TAX_PAYMENT;
		} else {
			return null;
		}
	}

	public void approvePayment(BillPayment billPayment, CorpUser corpUser, BigDecimal equivalentMOP) throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        CorpAccountService corpAccService = (CorpAccountService)appContext.getBean("CorpAccountService");
        ExchangeRatesService exRatesService = (ExchangeRatesService)appContext.getBean("ExchangeRatesService");
        TransferLimitService transferLimitService = (TransferLimitService)appContext.getBean("TransferLimitService");

		if (billPayment != null) {
	    	Map toHost = new HashMap();
	    	Map fromHost = new HashMap();
	        CibTransClient testClient = null;
	        // modify for checking saving account
	        if (corpAccService.isSavingAccount(billPayment.getFromAccount(),billPayment.getCurrency())){
	        	testClient = new CibTransClient("CIB", "59SC");
	        } else {
	        	testClient = new CibTransClient("CIB", "59CC");
	        }
	        String effectiveDate = CibTransClient.getCurrentDate();
			//UPDATE������ݿ���Ϣ
			billPayment.setStatus(Constants.STATUS_NORMAL);
			billPayment.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			billPayment.setExecuteTime(new Date());
			billPaymentDao.update(billPayment);

			RcCurrency rcCcy = new RcCurrency();

			MerchantBillBean merBillBean = this.getMerchantBill(billPayment.getCategory(), billPayment.getMerchant());
			// get alpha
			String paymentDescription = merBillBean.getPaymentDescription().toUpperCase();
			String alpha = getAlphiField(paymentDescription, billPayment.getBillNo1(), billPayment.getBillNo2());
			/*
			alpha = paymentDescription.substring(0, paymentDescription.indexOf("X")) + billPayment.getBillNo1();
			if (billPayment.getCategory().trim().equals("970")) {
				alpha += " " + billPayment.getBillNo2();
			}
			*/
			
			String fromCcy = corpAccService.getCurrency(billPayment.getFromAccount(),true);
			
			// exchange rate
			Map exRateMap = exRatesService.getExchangeRate(billPayment.getCorpId(), fromCcy, billPayment.getCurrency(), 7);
			String rateType = exRateMap.get(ExchangeRatesDao.RATE_TYPE).toString();
			BigDecimal exRate = null;
			BigDecimal fromAmt = null;
			if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
				throw new NTBException("err.txn.NoSuchExchangeRate");
			} else if (rateType.equals(ExchangeRatesDao.RATE_TYPE_BUY_RATE)) {
				exRate = new BigDecimal(exRateMap.get(ExchangeRatesDao.BUY_RATE).toString());
				fromAmt = new BigDecimal(billPayment.getTransferAmount().toString()).divide(exRate, 2, BigDecimal.ROUND_HALF_UP);	
			} else if (rateType.equals(ExchangeRatesDao.RATE_TYPE_SELL_RATE)) {
				exRate = new BigDecimal(exRateMap.get(ExchangeRatesDao.SELL_RATE).toString());	
				fromAmt = new BigDecimal(billPayment.getTransferAmount().toString()).multiply(exRate);				
			} else if (rateType.equals(ExchangeRatesDao.RATE_TYPE_ALL_RATE)) {
				exRate = new BigDecimal(exRateMap.get(ExchangeRatesDao.SELL_RATE).toString())
				.divide(new BigDecimal(exRateMap.get(ExchangeRatesDao.BUY_RATE).toString()), 7, BigDecimal.ROUND_HALF_UP);
				fromAmt = new BigDecimal(billPayment.getTransferAmount().toString()).multiply(exRate);
			}
			fromAmt = fromAmt.setScale(2, BigDecimal.ROUND_HALF_UP);

			LceBean lceInfo = exRatesService.getLceInfo(fromCcy, billPayment.getCurrency(), fromAmt, new BigDecimal(billPayment.getTransferAmount().toString()));
			BigDecimal fromAmtLCE = lceInfo.getFromAmtLCE();
			BigDecimal fromRateLCE = lceInfo.getFromRateLCE();
			BigDecimal toAmtLCE = lceInfo.getToAmtLCE();
			BigDecimal toRateLCE = lceInfo.getToRateLCE();
			
			// add Limit
			transferLimitService.addUsedLimitQuota(billPayment.getFromAccount(), corpUser.getCorpId(), 
					Constants.TXN_TYPE_PAY_BILLS,
					fromAmt.doubleValue(), equivalentMOP.doubleValue());

			//up to host
	        toHost.put("EFFECTIVE_DATE",effectiveDate);
	        toHost.put("ACCOUNT_NO", billPayment.getFromAccount());
	        toHost.put("TRANSFER_TO_ACC", merBillBean.getSuspendAccNo());
	        toHost.put("PASSBOOK_BALANCE", testClient.getPassbookBalance(fromCcy, billPayment.getCurrency(), new BigDecimal(billPayment.getTransferAmount().toString())));
	        toHost.put("TRANSACTION_AMOUNT", fromAmt);
	        toHost.put("PRINCIPAL_AMOUNT", billPayment.getTransferAmount());
	        toHost.put("INTEREST_AMOUNT", toAmtLCE);
	        toHost.put("ESCROW_AMOUNT", toRateLCE.multiply(new BigDecimal("100000")));
	        toHost.put("LCE_OF_TRANS_AMOUNT", fromAmtLCE);
	        toHost.put("FOREIGN_CCY_CODE", rcCcy.getCbsNumberByCcy( fromCcy));
	        toHost.put("FOREIGN_CCY_RATE", fromRateLCE);
	        toHost.put("CASHIER_NO", rcCcy.getCbsNumberByCcy(billPayment.getCurrency()));
	        toHost.put("ALPHA_1", alpha);
	        toHost.put("ALPHA_2", alpha);
	        toHost.put("EXCHANGE_IN_DR", testClient.getExchangeIn(fromCcy, billPayment.getCurrency(), new BigDecimal(billPayment.getTransferAmount().toString())));
	        toHost.put("EXCHANGE_OUT_CR", testClient.getExchangeOut(fromCcy, billPayment.getCurrency(), fromAmt));
	        toHost.put("CLIENT_REF", billPayment.getRemark());
	        testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_BILL_PAYMENT, CibTransClient.ACCTYPE_3RD_PARTY, billPayment.getTransId());
	        //send to host
			fromHost = testClient.doTransaction(toHost);

	        //write rp
			Map reportMap = new HashMap();
	        if (billPayment.getPayType().equals(BillPayment.PAYMENT_TYPE_GENERAL)) {
	        	
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("TRANS_CODE", toHost.get("TRANSACTION_CODE"));
				reportMap.put("CORRECTION_FLAG", " ");
				reportMap.put("TRANS_REF_NO", Utils.null2EmptyWithTrim(toHost.get("TELLER_NO")) + Utils.null2EmptyWithTrim(toHost.get("UNIQUE_SEQUENCE")));
				reportMap.put("MERCHANT_CODE", billPayment.getMerchant());
				reportMap.put("BILL_NUMBER", billPayment.getBillNo1());
				reportMap.put("MERCHANT_AC_NO", merBillBean.getSuspendAccNo());
				reportMap.put("BILL_AMOUNT", billPayment.getTransferAmount());
				reportMap.put("BILL_PN_SIGN", "+");
				reportMap.put("BILL_AMT_CCY", billPayment.getCurrency());
				reportMap.put("DEBIT_ACCOUNT", billPayment.getFromAccount());
				reportMap.put("DEBIT_AMOUNT", fromAmt);
				reportMap.put("DEBIT_PN_SIGN ", "+");
				reportMap.put("DEBIT_AMT_CCY ", fromCcy);
				reportMap.put("EXCHANGE_RATE ", exRate);
				reportMap.put("PN_SIGN ", "+");
				reportMap.put("USER_ID ", billPayment.getUserId());
				reportMap.put("CORPORATION_ID ", billPayment.getCorpId());
				reportMap.put("STATUS ", fromHost.get("RESPONSE_CODE"));
				reportMap.put("REJECT_CODE ", fromHost.get("REJECT_CODE"));	    
				UploadReporter.write("RP_BILLPMT", reportMap);    	
				
	        } else if (billPayment.getPayType().equals(BillPayment.PAYMENT_TYPE_CARD)) {
	        	
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("TRANS_CODE", toHost.get("TRANSACTION_CODE"));
				reportMap.put("CORRECTION_FLAG", " ");
				reportMap.put("TRANS_REF_NO", Utils.null2EmptyWithTrim(toHost.get("TELLER_NO")) + Utils.null2EmptyWithTrim(toHost.get("UNIQUE_SEQUENCE")));
				reportMap.put("PAYMENT_4_CARD", billPayment.getBillNo1());
				reportMap.put("DEBIT_ACCOUNT", billPayment.getFromAccount());
				reportMap.put("DEBIT_AMOUNT", fromAmt);
				reportMap.put("DEBIT_PN_SIGN ", "+");
				reportMap.put("DEBIT_AMT_CCY ", fromCcy);
				reportMap.put("TO_ACCOUNT ", merBillBean.getSuspendAccNo());
				reportMap.put("PAYMENT_AMOUNT ", billPayment.getTransferAmount());
				reportMap.put("PAYMENT_PN_SIGN ", "+");
				reportMap.put("PAYMENT_AMT_CCY ", billPayment.getCurrency());
				reportMap.put("EXCHANGE_RATE ", exRate);
				reportMap.put("PN_SIGN ", "+");
				reportMap.put("USER_ID ", billPayment.getUserId());
				reportMap.put("CORPORATION_ID ", billPayment.getCorpId());
				reportMap.put("STATUS ", fromHost.get("RESPONSE_CODE"));
				reportMap.put("REJECT_CODE ", fromHost.get("REJECT_CODE"));	    
				UploadReporter.write("RP_CARDPMT", reportMap); 
	        	
	        } else if (billPayment.getPayType().equals(BillPayment.PAYMENT_TYPE_TAX)) {
	        	
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("TRANS_CODE", toHost.get("TRANSACTION_CODE"));
				reportMap.put("CORRECTION_FLAG", " ");
				reportMap.put("TRANS_REF_NO", Utils.null2EmptyWithTrim(toHost.get("TELLER_NO")) + Utils.null2EmptyWithTrim(toHost.get("UNIQUE_SEQUENCE")));
				reportMap.put("MERCHANT_CODE", billPayment.getMerchant());
				reportMap.put("TAX_PAYMENT_NO1", billPayment.getBillNo1());
				reportMap.put("TAX_PAYMENT_NO2", billPayment.getBillNo2());
				reportMap.put("BILL_AMOUNT", billPayment.getTransferAmount());
				reportMap.put("BILL_PN_SIGN", "+");
				reportMap.put("BILL_AMT_CCY", billPayment.getCurrency());
				reportMap.put("DEBIT_ACCOUNT", billPayment.getFromAccount());
				reportMap.put("DEBIT_AMOUNT", fromAmt);
				reportMap.put("DEBIT_PN_SIGN ", "+");
				reportMap.put("DEBIT_AMT_CCY ", fromCcy);
				reportMap.put("EXCHANGE_RATE ", exRate);
				reportMap.put("PN_SIGN ", "+");
				reportMap.put("USER_ID ", billPayment.getUserId());
				reportMap.put("CORPORATION_ID ", billPayment.getCorpId());
				reportMap.put("STATUS ", fromHost.get("RESPONSE_CODE"));
				reportMap.put("REJECT_CODE ", fromHost.get("REJECT_CODE"));	    
				UploadReporter.write("RP_BILLTAX", reportMap);
	        	
	        }
	        //����ײ��ɹ��򱨳��������
	        if(!testClient.isSucceed()){
	        	throw new NTBHostException(testClient.getErrorArray());
	        }
		}
	}

	public void rejectPayment(BillPayment billPayment) throws NTBException {
		if (billPayment != null) {
			billPayment.setStatus(Constants.STATUS_REMOVED);
			billPayment.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
			billPayment.setExecuteTime(new Date());
			billPaymentDao.update(billPayment);
		}
	}

	public Map getGenPaymentInfo(CorpUser corpUser, String category, String merchant, String billNo) throws NTBException {
    	Map toHost = new HashMap();
    	Map fromHost= null;
        CibTransClient testClient = new CibTransClient("CIB", "ZJ35");

        toHost.put("CIF_NO", category.trim() + merchant.trim());
        toHost.put("BILL_NO", billNo);
        String refNo = CibIdGenerator.getRefNoForTransaction();
        testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_BILL_PAYMENT, CibTransClient.ACCTYPE_3RD_PARTY, refNo);
        fromHost = testClient.doTransaction(toHost);

        //write rp
		Map reportMap = new HashMap();
		reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
		reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
		reportMap.put("TRANS_CODE", toHost.get("TRANSACTION_CODE"));
		reportMap.put("TRANS_REF_NO", Utils.null2EmptyWithTrim(toHost.get("TELLER_NO")) + Utils.null2EmptyWithTrim(toHost.get("UNIQUE_SEQUENCE")));
		reportMap.put("MERCHANT_CODE", merchant);
		reportMap.put("BILL_NUMBER", billNo);
		reportMap.put("BILL_AMOUNT", fromHost.get("PAYMENT_AMOUNT"));
		reportMap.put("POSITIVE_NEGATIVE_SIGN", "+");
		reportMap.put("BILL_DUE_DATE", fromHost.get("EXPIRY_DATE"));
		reportMap.put("USER_ID", corpUser.getUserId());
		reportMap.put("CORPORATION_ID", corpUser.getCorpId());
		reportMap.put("STATUS ", fromHost.get("RESPONSE_CODE"));
		reportMap.put("REJECT_CODE ", fromHost.get("REJECT_CODE"));
		UploadReporter.write("RP_BILLENQ", reportMap);

        //����ײ��ɹ��򱨳��������
        if(!testClient.isSucceed()){
        	throw new NTBHostException(testClient.getErrorArray());
        }
        return fromHost;
	}

	public Map getTaxPaymentInfo(CorpUser corpUser, String category, String merchant, String billNo1, String billNo2) throws NTBException {
    	Map toHost = new HashMap();
    	Map fromHost= null;
        CibTransClient testClient = new CibTransClient("CIB", "ZJ39");

        toHost.put("CIF_NO", category.trim() + merchant.trim());
        toHost.put("BILL_NO_1", billNo1);
        toHost.put("BILL_NO_2", billNo2);
        String refNo = CibIdGenerator.getRefNoForTransaction();
        testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_BILL_PAYMENT, CibTransClient.ACCTYPE_3RD_PARTY, refNo);
        fromHost = testClient.doTransaction(toHost);

        //write rp
		Map reportMap = new HashMap();
		reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
		reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
		reportMap.put("TRANS_CODE", toHost.get("TRANSACTION_CODE"));
		reportMap.put("TRANS_REF_NO", Utils.null2EmptyWithTrim(toHost.get("TELLER_NO")) + Utils.null2EmptyWithTrim(toHost.get("UNIQUE_SEQUENCE")));
		reportMap.put("MERCHANT_CODE", merchant);
		reportMap.put("TAX_PAYMENT_1", billNo1);
		reportMap.put("TAX_PAYMENT_2", billNo2);
		reportMap.put("BILL_AMOUNT", fromHost.get("PAYMENT_AMOUNT"));
		reportMap.put("BILL_PN_SIGN", "+");
		reportMap.put("BILL_DUE_DATE", fromHost.get("EXPIRY_DATE"));
		reportMap.put("USER_ID", corpUser.getUserId());
		reportMap.put("CORPORATION_ID", corpUser.getCorpId());
		reportMap.put("STATUS ", fromHost.get("RESPONSE_CODE"));
		reportMap.put("REJECT_CODE ", fromHost.get("REJECT_CODE"));
		UploadReporter.write("RP_TAXENQ", reportMap);

        //����ײ��ɹ��򱨳��������
        if(!testClient.isSucceed()){
        	throw new NTBHostException(testClient.getErrorArray());
        }
        return fromHost;
	}

	public Map getCardPaymentInfo(CorpUser corpUser, String cardNo) throws NTBException {
    	Map toHost = new HashMap();
    	Map fromHost= null;
        CibTransClient testClient = new CibTransClient("CIB", "ZJ33");

        toHost.put("CARD_NO", cardNo);
        String refNo = CibIdGenerator.getRefNoForTransaction();
        testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_BILL_PAYMENT, CibTransClient.ACCTYPE_3RD_PARTY, refNo);
        fromHost = testClient.doTransaction(toHost);

        /*
        //write rp
		Map reportMap = new HashMap();
		reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
		reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
		reportMap.put("TRANS_CODE", toHost.get("TRANSACTION_CODE"));
		reportMap.put("TRANS_REF_NO", Utils.null2EmptyWithTrim(fromHost.get("TELLER_NO")) + Utils.null2EmptyWithTrim(fromHost.get("UNIQUE_SEQUENCE")));
		reportMap.put("MERCHANT_CODE", "CARD");
		reportMap.put("BILL_NUMBER", cardNo);
		reportMap.put("BILL_AMOUNT", fromHost.get("PAYMENT_DUE"));
		reportMap.put("POSITIVE_NEGATIVE_SIGN", "+");
		reportMap.put("BILL_DUE_DATE", fromHost.get("CARD_EXPIRY_DATE").toString());
		reportMap.put("USER_ID", corpUser.getUserId());
		reportMap.put("CORPORATION_ID", corpUser.getCorpId());
		reportMap.put("STATUS ", fromHost.get("RESPONSE_CODE"));
		reportMap.put("REJECT_CODE ", fromHost.get("REJECT_CODE"));
		UploadReporter.write("RP_BILLENQ", reportMap);
		*/

        //����ײ��ɹ��򱨳��������
        if(!testClient.isSucceed()){
        	throw new NTBHostException(testClient.getErrorArray());
        }
        return fromHost;
	}

	public Map getCEMBillInfo(CorpUser corpUser, String billNo) throws NTBException {
		billNo = Utils.null2EmptyWithTrim(billNo);
		if (!billNo.equals("")) {
	    	Map toHost = new HashMap();
	    	Map fromHost= null;
	        CibTransClient testClient = new CibTransClient("CIB", "ZJ03");

	        toHost.put("CEM_BILL_NO", billNo);
	        String refNo = CibIdGenerator.getRefNoForTransaction();
	        testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_BILL_PAYMENT, CibTransClient.ACCTYPE_3RD_PARTY, refNo);
	        fromHost = testClient.doTransaction(toHost);

	        //write rp
			Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("TRANS_CODE", toHost.get("TRANSACTION_CODE"));
			reportMap.put("TRANS_REF_NO", Utils.null2EmptyWithTrim(toHost.get("TELLER_NO")) + Utils.null2EmptyWithTrim(toHost.get("UNIQUE_SEQUENCE")));
			reportMap.put("MERCHANT_CODE", "CEM");
			reportMap.put("BILL_NUMBER", billNo);
			reportMap.put("BILL_AMOUNT", fromHost.get("AMOUNT_DUE"));
			reportMap.put("POSITIVE_NEGATIVE_SIGN", "+");
			reportMap.put("BILL_DUE_DATE", fromHost.get("BILLING_DATE")!=null ? "20" + fromHost.get("BILLING_DATE") : null);
			reportMap.put("USER_ID", corpUser.getUserId());
			reportMap.put("CORPORATION_ID", corpUser.getCorpId());
			reportMap.put("STATUS ", fromHost.get("RESPONSE_CODE"));
			reportMap.put("REJECT_CODE ", fromHost.get("REJECT_CODE"));
			UploadReporter.write("RP_BILLENQ", reportMap);

	        //����ײ��ɹ��򱨳��������
	        if(!testClient.isSucceed()){
	        	throw new NTBHostException(testClient.getErrorArray());
	        }
	        return fromHost;
		} else {
			Log.error("CEM Bill No. is Null or Empty");
			return null;
		}
	}

	public Map getSAAMBillInfo(CorpUser corpUser, String billNo) throws NTBException {
		billNo = Utils.null2EmptyWithTrim(billNo);
		if (!billNo.equals("")) {
	    	Map toHost = new HashMap();
	        Map fromHost = null;
	        CibTransClient testClient = new CibTransClient("CIB", "ZJ06");

	        toHost.put("SAAM_BILL_NO", billNo);
	        String refNo = CibIdGenerator.getRefNoForTransaction();
	        
	        testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_BILL_PAYMENT, CibTransClient.ACCTYPE_3RD_PARTY, refNo);
	        fromHost = testClient.doTransaction(toHost);

	        //write rp
			Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("TRANS_CODE", toHost.get("TRANSACTION_CODE"));
			reportMap.put("TRANS_REF_NO", Utils.null2EmptyWithTrim(toHost.get("TELLER_NO")) + Utils.null2EmptyWithTrim(toHost.get("UNIQUE_SEQUENCE")));
			reportMap.put("MERCHANT_CODE", "SAAM");
			reportMap.put("BILL_NUMBER", billNo);
			reportMap.put("BILL_AMOUNT", fromHost.get("AMOUNT_DUE"));
			reportMap.put("POSITIVE_NEGATIVE_SIGN", "+");
			reportMap.put("BILL_DUE_DATE", fromHost.get("BILLING_DATE")!=null ? "20" + fromHost.get("BILLING_DATE") : null);
			reportMap.put("USER_ID", corpUser.getUserId());
			reportMap.put("CORPORATION_ID", corpUser.getCorpId());
			reportMap.put("STATUS ", fromHost.get("RESPONSE_CODE"));
			reportMap.put("REJECT_CODE ", fromHost.get("REJECT_CODE"));
			UploadReporter.write("RP_BILLENQ", reportMap);

	        //����ײ��ɹ��򱨳��������
	        if(!testClient.isSucceed()){
	        	throw new NTBHostException(testClient.getErrorArray());
	        }
	        return fromHost;
		} else {
			Log.error("SAAM Bill No. is Null or Empty");
			return null;
		}
	}

	public Map getCTMBillInfo(CorpUser corpUser, String billNo) throws NTBException {
		billNo = Utils.null2EmptyWithTrim(billNo);
		if (!billNo.equals("")) {
	    	Map toHost = new HashMap();
	    	Map fromHost = null;
	        CibTransClient testClient = new CibTransClient("CIB", "ZJ08");

	        toHost.put("CTM_BILL_NO", billNo);
	        String refNo = CibIdGenerator.getRefNoForTransaction();
	        testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_BILL_PAYMENT, CibTransClient.ACCTYPE_3RD_PARTY, refNo);
	        fromHost = testClient.doTransaction(toHost);

	        //write rp
			Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("TRANS_CODE", toHost.get("TRANSACTION_CODE"));
			reportMap.put("TRANS_REF_NO", Utils.null2EmptyWithTrim(toHost.get("TELLER_NO")) + Utils.null2EmptyWithTrim(toHost.get("UNIQUE_SEQUENCE")));
			reportMap.put("MERCHANT_CODE", "CTM");
			reportMap.put("BILL_NUMBER", billNo);
			reportMap.put("BILL_AMOUNT", fromHost.get("AMOUNT_DUE"));
			reportMap.put("POSITIVE_NEGATIVE_SIGN", "+");
			reportMap.put("BILL_DUE_DATE", fromHost.get("BILLING_DATE")!=null ? "20" + fromHost.get("BILLING_DATE") : null);
			reportMap.put("USER_ID", corpUser.getUserId());
			reportMap.put("CORPORATION_ID", corpUser.getCorpId());
			reportMap.put("STATUS ", fromHost.get("RESPONSE_CODE"));
			reportMap.put("REJECT_CODE ", fromHost.get("REJECT_CODE"));
			UploadReporter.write("RP_BILLENQ", reportMap);

	        //����ײ��ɹ��򱨳��������
	        if(!testClient.isSucceed()){
	        	throw new NTBHostException(testClient.getErrorArray());
	        }
	        return fromHost;
		} else {
			Log.error("CTM Bill No. is Null or Empty");
			return null;
		}
	}

	public MerchantBillBean getMerchantBill(String category, String merchant) throws NTBException {
		MerchantBillBean merBillBean = new MerchantBillBean();

		String sql = "select MER_SUSPEND_AC_APP_CCY, MER_SUSPEND_AC_APP_NO, PAYMENT_DESCRIPTION, MER_ENQ_FLAG from HS_MERCHANT_BILL_PAYMENT where CATEGORY_CODE = ? and MER_CODE = ?";

		try {
			Map rowMap = this.genericJdbcDao.querySingleRow(sql, new Object[] {category, merchant});
			merBillBean.setSuspendAccCcy(rowMap.get("MER_SUSPEND_AC_APP_CCY").toString());
			merBillBean.setSuspendAccNo(rowMap.get("MER_SUSPEND_AC_APP_NO").toString());
			merBillBean.setPaymentDescription(rowMap.get("PAYMENT_DESCRIPTION").toString());
			merBillBean.setMerEnqFlag(rowMap.get("MER_ENQ_FLAG").toString());
			return merBillBean;
		} catch (Exception e) {
			Log.error("DB error", e);
            throw new NTBException("err.txn.GetMerchantBillException");
		}
	}
	
	private String getAlphiField (String billDesc, String billNo1, String billNo2) throws NTBException {
		billDesc = Utils.null2EmptyWithTrim(billDesc);
		billNo1 = Utils.null2EmptyWithTrim(billNo1);
		billNo2 = Utils.null2EmptyWithTrim(billNo2);

		String x1 = "";
		String x2 = "";
		
		String[] strs = billDesc.split(" ");
		x1 = " " + Utils.prefixZero(billNo1, strs[2].length());
		
		if (!billNo2.equals("") && (strs.length > 3)) {
			x2 = " " + Utils.prefixZero(billNo2, strs[3].length());
		}
		
		return strs[0] + " " + strs[1] + x1 + x2;
	}
	
	public String checkOtherMerchant(String category, String merchant) throws NTBException {
		MerchantBillBean bill = this.getMerchantBill(category, merchant);
		if("N".equals(bill.getMerEnqFlag())){
			return bill.getSuspendAccCcy();
		}
		return null;
		
	}
	
	public List listRecordByBatchId(String batchId) throws NTBException {
		return this.billPaymentDao.listRecordByBatchId(batchId);
	}
	
	public void processBatchPayment(FileRequest fileRequest, List detailList) throws NTBException {
		this.requestDao.add(fileRequest);
		for(int i=0; i<detailList.size(); i++){
			BillPayment billPayment = (BillPayment) detailList.get(i);
			billPayment.setTransId(CibIdGenerator.getRefNoForTransaction());
			billPayment.setBatchId(fileRequest.getBatchId());
			this.billPaymentDao.add(billPayment);
		}
	}
	
	public void rejectBatchPayment(String batchId) throws NTBException {
		FileRequest fileRequest = (FileRequest) this.requestDao.load(FileRequest.class, batchId);
		List detailList = listRecordByBatchId(batchId);
		
		fileRequest.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
		fileRequest.setStatus(Constants.STATUS_REMOVED);
		this.requestDao.update(fileRequest);
		for(int i=0; i<detailList.size(); i++){
			BillPayment billPayment = (BillPayment) detailList.get(i);
			billPayment.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
			billPayment.setStatus(Constants.STATUS_REMOVED);
			billPayment.setExecuteTime(new Date());
			this.billPaymentDao.update(billPayment);
		}
	}
	
	public void approveBatchPayment(String batchId) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
        TransferLimitService transferLimitService = (TransferLimitService)appContext.getBean("TransferLimitService");
        UtilService utilService = (UtilService)appContext.getBean("UtilService");
        
		FileRequest fileRequest = (FileRequest) this.requestDao.load(FileRequest.class, batchId);
		List detailList = listRecordByBatchId(batchId);
		
		// get file name
		String fileName = getFileName();
		File uploadFile = new File(batFileDir + fileName);
		
		// update db
		fileRequest.setFileName(fileName);
		fileRequest.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
		fileRequest.setStatus(Constants.STATUS_NORMAL);
		fileRequest.setExecuteTime(new Date());
		fileRequest.setBatchResult("P");
		this.requestDao.update(fileRequest);
		for(int i=0; i<detailList.size(); i++){
			BillPayment billPayment = (BillPayment) detailList.get(i);
			billPayment.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			billPayment.setStatus(Constants.STATUS_NORMAL);
			billPayment.setExecuteTime(new Date());
			billPayment.setDetailResult("P");
			this.billPaymentDao.update(billPayment);
		}

		// add Limit
		Double equivalentMOP = fileRequest.getToAmount();
		transferLimitService.addUsedLimitQuota(fileRequest.getFromAccount(), fileRequest.getCorpId(), 
				Constants.TXN_TYPE_PAY_BILLS,
				fileRequest.getFromAmount().doubleValue(), equivalentMOP.doubleValue());
		
		// generate upload file
		genUploadFile(fileRequest, detailList, uploadFile);

		// upload file to host
		try{
			utilService.uploadFileToHost("", uploadFile);
		} catch (NTBException e) {
			uploadFile.delete();
			throw e;
		}
	}
	
	private void genUploadFile(FileRequest fileRequest, List detailList, File uploadFile) throws NTBException {
	    BufferedWriter bufWriter = null;
	    try {
			bufWriter = new BufferedWriter(new FileWriter(uploadFile));

			//File Header
			String recId, orderDate, debitAcc, transferCcy, totalDebitAmt, recCount, corpId, batchRef, filler;
			recId = "H";
			orderDate = DateTime.formatDate(new Date(), "yyyyMMdd");
			debitAcc = Utils.prefixZero(Utils.null2EmptyWithTrim(fileRequest.getFromAccount()), 10);
			transferCcy = fileRequest.getFromCurrency();
			//ȥС���
			//totalDebitAmt = Utils.replaceStr(fileRequest.getFromAmount().toString(), ".", "");
			totalDebitAmt = Utils.prefixZero("000", 15);
			recCount = Utils.prefixZero(String.valueOf(detailList.size()), 5);
			corpId = Utils.appendSpace(Utils.null2EmptyWithTrim(fileRequest.getCorpId()), 10);
			batchRef = Utils.appendSpace(Utils.null2EmptyWithTrim(fileRequest.getBatchId()), 20);
			filler = Utils.appendSpace("", 68);
			bufWriter.write(recId + orderDate + debitAcc
					+ transferCcy + totalDebitAmt + recCount + corpId + batchRef + filler);
			bufWriter.newLine();
			
			//File Detail
			BillPayment billPayment = null;
			for (int i=0; i<detailList.size(); i++) {
				billPayment = (BillPayment) detailList.get(i);
				bufWriter.write(detail2Str(billPayment));
				bufWriter.newLine();
			}
			
			//File Trailer
			String recId2, endOfFile, filler2;
			recId2 = "T";
			endOfFile = "Y";
			filler2 = Utils.appendSpace("", 138);
			bufWriter.write(recId2 + endOfFile + filler2);
			bufWriter.newLine();
			
			// finish, close stream!
			bufWriter.close();
			
		} catch (Exception e) {
			Log.error("generate upload file error", e);
			if(bufWriter!=null){
				try {
					bufWriter.close();
				} catch (IOException e1) {
				}
			}
			if(uploadFile.exists()){
				uploadFile.delete();
			}
			throw new NTBException("err.sys.GeneralError");
		}
	}
	
	private String detail2Str(BillPayment billPayment) throws NTBException {
		String recId, merCode, alpha1, alpha2, billAmount, clientRef, detailRef, filler;

		//get alpha
		MerchantBillBean merBillBean = this.getMerchantBill(billPayment.getCategory(), billPayment.getMerchant());
		String paymentDescription = merBillBean.getPaymentDescription().toUpperCase();
		String alpha = getAlphiField(paymentDescription, billPayment.getBillNo1(), billPayment.getBillNo2());
		
		recId = "D";
		merCode = Utils.appendSpace(Utils.null2EmptyWithTrim(billPayment.getMerchant()), 4);
		alpha1 = Utils.appendSpace(alpha, 40);
		alpha2 = Utils.appendSpace(alpha, 40);
		//format 
		billAmount = Format.formatDecimal((new BigDecimal(billPayment.getTransferAmount().toString())).toString(), 2);
		//ȥС���
		billAmount = Utils.replaceStr(billAmount, ".", "");
		billAmount = Utils.replaceStr(billAmount, ",", "");
		billAmount = Utils.prefixZero(billAmount, 13);
		clientRef = Utils.appendSpace(Utils.null2EmptyWithTrim(billPayment.getRemark()), 40);
		detailRef = Utils.appendSpace(Utils.null2EmptyWithTrim(billPayment.getTransId()), 20);
		filler = Utils.appendSpace("", 22);
		
		return recId + merCode + alpha1 + alpha2 + billAmount + clientRef + detailRef + filler;
	}
	
	private static String _created = "";
	private static int _index = 0;
	private String getFileName() throws NTBException {
		// jet 2009-02-10 ��ֹmulti-approve�����ظ��ļ���
		String created = DateTime.formatDate(new Date(), "ddHHmmss");
		String index = "0";
		if(_created.equals(created)){
			_index++;
		} else {
			_created = created;
			_index = 0;
		}
		index = String.valueOf(_index);
		if(_index > 9){
			throw new NTBException("err.bat.duplicatedFile");
		}
		
		return "BOBUBBP.O" + created + index;
	}
	
	public List listBatchPaymentHistory(String corpId, String userId,
			String fromAcc,	String dateFrom, String dateTo) throws NTBException {
		return this.requestDao.listRecords(corpId, userId,
				FileRequest.BATCH_PAYMENT, fromAcc, dateFrom, dateTo);
	}
	
	public static void main(String[] args) {
		BillPaymentServiceImpl test = new BillPaymentServiceImpl();
		try {
			System.err.println(test.getAlphiField("EBK 1PAY XXXXXXXXXXXXXXXX     ", " 1234567890123456  ", "ghadhfasd"));
			System.err.println(test.getAlphiField("EBK TAX XXXXXXXXXXXXX XXXXXXXX", " 1234567890123 ", " 87654321 "));
		} catch (NTBException e) {
			e.printStackTrace();
		}
	}
}
