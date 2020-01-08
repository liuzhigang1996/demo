package app.cib.service.txn;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import app.cib.bo.enq.LceBean;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TransferBank;
import app.cib.bo.txn.TransferMacau;
import app.cib.bo.txn.TransferOversea;
import app.cib.core.CibIdGenerator;
import app.cib.core.CibTransClient;
import app.cib.dao.txn.TransferDao;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.sys.CorpAccountService;
import app.cib.util.Constants;
import app.cib.util.RcCurrency;
import app.cib.util.UploadReporter;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.exception.NTBWarningException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.Utils;

/**
 * @author mxl 2006-07-16
 */

public class TransferServiceImpl implements TransferService {
	CibTransClient cibTransClient;

	//private static String SELECT_COUNTRY = "Select Country_Code,Country_Name from HS_COUNTRY_CODE where Country_Code<>'MO' Order by Seq_No,Country_Code";
	private static String SELECT_COUNTRY = "Select Country_Code,Country_Name from HS_COUNTRY_CODE where Country_Code NOT IN ('MO','99') and LOCAL_CODE = ? Order by Seq_No,Country_Code";

	private static String SELECT_BANK_LABEL = "Select COUNTRY_CODE, BANK_CODE_LABEL from HS_COUNTRY_SPECIFIC_BANK_CODE where COUNTRY_CODE=? ";

	private static String SELECT_ACCOUNT_LABEL = "Select COUNTRY_CODE, ACC_LABEL_SEQ, ACC_LABEL from HS_COUNTRY_SPECIFIC_ACCOUNT where COUNTRY_CODE=?  Order by ACC_LABEL_SEQ";

	private static String SELECT_CITY = "select CITY_CODE, CITY_NAME from HS_CITY_CODE where COUNTRY_CODE=? AND LOCAL_CODE = ?";

	private static String SELECT_BANK = "select concat(BANK_CODE,concat(',',SWIFT_CODE)) as BANK_CODE, BANK_NAME from HS_OVERSEAS_BANK where COUNTRY_CODE=? and local_code = ? order by BANK_NAME"; 
//	private static String SELECT_BANK = "select concat(BANK_CODE,concat(',',SWIFT_CODE)) as BANK_CODE, BANK_NAME from HS_OVERSEAS_BANK where CITY_CODE = ? ";//mod by linrui 20180312 
	// CITY_CODE
	// =
	// ?";

	String SELECT_CGD = "select CGD_FLAG from HS_OVERSEAS_BANK where BANK_CODE=?";
	String SELECT_CGDMACAU = "select CGD_FLAG from HS_LOCAL_BANK_CODE where BANK_CODE=? ";
	// mxl add 0914
	private static String SELECT_ACC_LABEL_SQL = "Select ACC_LABEL_SEQ from HS_COUNTRY_SPECIFIC_ACCOUNT where COUNTRY_CODE=?  and ACC_LABEL=?";

	// mxl add 0807

	private static String SELECT_CGDLOCAL = "select CGD_FLAG,BANK_NAME,SWIFT_CODE from HS_LOCAL_BANK_CODE where BANK_CODE=? ";

	private static String SELECT_CGDOVERSEA = "select CGD_FLAG,BANK_NAME,SWIFT_CODE from HS_OVERSEAS_BANK where BANK_CODE=?";

	private static String SELECT_CITYNAME = " select CITY_NAME from HS_CITY_CODE WHERE CITY_CODE=?";

	private static String SELECT_SPBANK_LABAEL = "Select BANK_CODE_LABEL  from HS_COUNTRY_SPECIFIC_BANK_CODE where COUNTRY_CODE=?";

	private static String SELECT_ALPHA = "Select ALPHA2 from HS_TRANSACTION_CODE where TRANS_CODE=?";
	
	//private static String SELECT_ACCT = " SELECT DISTINCT ACCOUNT_NO FROM CORP_ACCOUNT WHERE CORP_ID=? AND ACCOUNT_TYPE NOT IN ('LN','TD')";
	private static String SELECT_ACCT = " SELECT DISTINCT ACCOUNT_NO FROM CORP_ACCOUNT WHERE CORP_ID=? AND ACCOUNT_TYPE NOT IN ('LN','TD') AND CURRENCY not in (select a.ccy_code from available_currencies a where available_flag = 'N')";
	private static String SELECT_ACCT_remittance = " SELECT DISTINCT ACCOUNT_NO FROM CORP_ACCOUNT WHERE CORP_ID=? AND ACCOUNT_TYPE NOT IN ('LN','TD') AND CURRENCY not in (select a.ccy_code from available_currencies a where available_flag = 'N') AND CURRENCY not in('CNY')";
	//private static String SELECT_ACCT_CHE_BOOK = " SELECT DISTINCT ACCOUNT_NO FROM CORP_ACCOUNT WHERE CORP_ID=? AND ACCOUNT_TYPE NOT IN ('LN','TD','SA')";
	private static String SELECT_ACCT_CHE_BOOK = " SELECT DISTINCT ACCOUNT_NO FROM CORP_ACCOUNT WHERE CORP_ID=? AND ACCOUNT_TYPE NOT IN ('LN','TD','SA') AND CURRENCY in ('MOP','HKD')";
	/*private static String SELECT_ACCT = " SELECT ACCOUNT_NO, CURRENCY, ACCOUNT_NAME, ACCOUNT_DESC, ACCOUNT_TYPE, CORP_ID, STATUS, (ACCOUNT_NO || ' - ' || CURRENCY) AS ACCOUNT_INFO FROM CORP_ACCOUNT " +
	"  WHERE CORP_ID=? AND ACCOUNT_TYPE NOT IN ('LN','TD')";*/

	//add by wcc 20190402
	private static String SELECT_TO_NAME = "SELECT CUSTOMER_SHORT_NAME FROM HS_CORPORATE_INFO WHERE CORP_ID=?";

	private TransferDao transferDao;
	private GenericJdbcDao genericJdbcDao;

	// mxl add 0817
	// private static String SELECT_CODELOCAL = "select BANK_CODE from
	// HS_LOCAL_BANK_CODE where BANK_NAME=? ";
	public static void main(String[] args) {

	}

	public TransferDao getTransferDao() {
		return transferDao;
	}

	public void setTransferDao(TransferDao transferDao) {
		this.transferDao = transferDao;
	}

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	/* List Transfer History In BANK */
	public List transferHistoryInBANK(Map queryParam) throws NTBException {
		List historyList;
		historyList = transferDao.list(TransferBank.class, queryParam);
		return historyList;
	}

	/* List Transfer History In Macau */
	public List transferHistoryInMacau(Map queryParam) throws NTBException {
		List historyList;
		historyList = transferDao.list(TransferMacau.class, queryParam);
		return historyList;
	}

	/* List Transfer History In verseas */
	public List transferHistoryOverseas(Map queryParam) throws NTBException {
		List historyList;
		historyList = transferDao.list(TransferOversea.class, queryParam);
		return historyList;
	}

	/* Transfer In BANK */
	public TransferBank transferAccInBANK1to1(TransferBank pojoTransfer)
			throws NTBException {
		if (pojoTransfer != null) {
			pojoTransfer.setRecordType(TransferBank.TRANSFER_TYPE_GENERAL);
			transferDao.add(pojoTransfer);
		} else {
			throw new NTBException("err.txn.pojoTransferIsNull");
		}
		return pojoTransfer;
	}

	/* Transfer In Macau */
	public TransferMacau transferAccMacau(TransferMacau pojoTransfer)
			throws NTBException {

		if (pojoTransfer != null) {
			pojoTransfer.setRecordType(TransferMacau.TRANSFER_TYPE_GENERAL);
			transferDao.add(pojoTransfer);
		} else {
			throw new NTBException("err.txn.pojoTransferIsNull");
		}
		return pojoTransfer;
	}

	/* Transfer In Ovrersea */
	public TransferOversea transferAccOverseas(TransferOversea pojoTransfer)
			throws NTBException {
		if (pojoTransfer != null) {
			pojoTransfer.setRecordType(TransferOversea.TRANSFER_TYPE_GENERAL);
			transferDao.add(pojoTransfer);
		} else {
			throw new NTBException("err.txn.pojoTransferIsNull");
		}
		return pojoTransfer;
	}

	public List transferHistoryInBANK(String dateFrom, String dateTo,
			String corpID, String userID, String fromAccount)
			throws NTBException {
		List historyList = null;
		historyList = transferDao.listHistoryBank(dateFrom, dateTo, corpID,
				userID, fromAccount);
		return historyList;
	}

	public TransferBank viewInBANK(String transID) throws NTBException {
		TransferBank transferBank = null;
		if ((transID != null) && (!transID.equals(""))) {

			transferBank = (TransferBank) transferDao.load(TransferBank.class,
					transID);

		} else {
			throw new NTBException("err.txn.TransIDIsNullOrEmpty");
		}

		return transferBank;
	}

	public List transferHistoryInMacau(String dateFrom, String dateTo,
			String corpID, String userID, String fromAccount)
			throws NTBException {
		List historyList = null;
		historyList = transferDao.listHistoryMacau(dateFrom, dateTo, corpID,
				userID, fromAccount);
		return historyList;
	}

	public List transferHistoryInOversea(String dateFrom, String dateTo,
			String corpID, String userID, String fromAccount)
			throws NTBException {
		List historyList = null;
		historyList = transferDao.listHistoryOversea(dateFrom, dateTo, corpID,
				userID, fromAccount);
		return historyList;
	}

	public TransferMacau viewInMacau(String transID) throws NTBException {
		TransferMacau transferMacau = null;
		if ((transID != null) && (!transID.equals(""))) {

			transferMacau = (TransferMacau) transferDao.load(
					TransferMacau.class, transID);

		} else {
			throw new NTBException("err.txn.TransIDIsNullOrEmpty");
		}

		return transferMacau;
	}

	public TransferOversea viewInOversea(String transID) throws NTBException {
		TransferOversea transferOversea = null;
		if ((transID != null) && (!transID.equals(""))) {

			transferOversea = (TransferOversea) transferDao.load(
					TransferOversea.class, transID);

		} else {
			throw new NTBException("err.txn.TransIDIsNullOrEmpty");
		}

		return transferOversea;
	}

	public List transferHistory(String business_type, Date dateFrom,
			Date dateTo, String corpID, String userID, String fromAccount)
			throws NTBException {
		List historyList = null;
		historyList = transferDao.listHistory(business_type, dateFrom, dateTo,
				corpID, userID, fromAccount);
		return historyList;
	}

	public String loadCgd(String bank) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext
				.getBean("genericJdbcDao");
		HashMap cdtMap = new HashMap();
		String CGD = null;
		try {
			List idList = null;
			idList = dao.query(SELECT_CGDOVERSEA, new Object[] { bank });
			cdtMap = (HashMap) idList.get(0);
			CGD = (String) cdtMap.get("CGD_FLAG");
			return CGD;

		} catch (Exception e) {
			throw new NTBException("err.txn.GetCGDException");
		}
	}

	public List loadOversea(String country, String city, String action)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext
				.getBean("genericJdbcDao");
		try {
			List idList = null;
			if (action.equals("country")) {
				idList = dao.query(SELECT_CITY, new Object[] { country });
			} else if (action.equals("city")) {
				idList = dao.query(SELECT_BANK, new Object[] { country//city  //mod by linrui 20180312
						});
			}
			return idList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List loadOversea() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext
				.getBean("genericJdbcDao");
		try {
			List idList = dao.query(SELECT_COUNTRY, new Object[] {});
			return idList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//add by linrui for mul-ccy
	public List loadAccount(String corpId) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext
		.getBean("genericJdbcDao");
		try {
			List idList = dao.query(SELECT_ACCT, new Object[] {corpId});
			return idList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//add by linrui for mul-ccy
	
	//add by lzg 20190708
	public List loadAccountForRemittance(String corpId) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext
		.getBean("genericJdbcDao");
		try {
			List idList = dao.query(SELECT_ACCT_remittance, new Object[] {corpId});
			return idList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//add by lzg end
	
	public List loadAccountCheckBook(String corpId) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext
		.getBean("genericJdbcDao");
		try {
			List idList = dao.query(SELECT_ACCT_CHE_BOOK, new Object[] {corpId});
			return idList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap loadOversea(String coutry) throws NTBException {
		HashMap result = new HashMap();
		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext
				.getBean("genericJdbcDao");
		try {
			List bankLabelList = dao.query(SELECT_BANK_LABEL,
					new Object[] { coutry });
			result.put("BANK_LABEL", bankLabelList);

			List idList = dao.query(SELECT_ACCOUNT_LABEL,
					new Object[] { coutry });
			result.put("ACCOUNT_LABEL", idList);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// edit by mxl 0826
	public Map toHostChargeEnquiryNew(String transID, CorpUser user,
			BigDecimal tranAmount, String countryCode, String CGD,
			String fromCurrency, String toCurrency, String chargeBy,
			String chargeCurrency, String ibanFlag) throws NTBException {

		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		CibTransClient testClient = new CibTransClient("CIB", "ZJ53");
		toHost.put("TRANSACTION_AMOUNT", tranAmount);
		toHost.put("COUNTRY_CODE", countryCode);
		toHost.put("CGD", CGD);
		toHost.put("FROM_CCY", fromCurrency);
		toHost.put("TO_CCY", toCurrency);
		toHost.put("BENE_BNK_CHRG_OPT", chargeBy);
		toHost.put("CHG_CCY", chargeCurrency);
		toHost.put("IBAN", ibanFlag);
		
		//modify by chen_y 2017-06-01 for CR192 batch bob
		RcCurrency rcCurrency = new RcCurrency();
		toHost.put("CHECK_FULLDAY_CURRENCY", rcCurrency.getCcyByCbsNumber(toCurrency)) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_FROM", rcCurrency.getCcyByCbsNumber(fromCurrency)) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_TO", rcCurrency.getCcyByCbsNumber(toCurrency)) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_FLAG", "O") ;

		testClient.setAlpha8(user, CibTransClient.TXNNATURE_TRANSFER,
				CibTransClient.ACCTYPE_3RD_PARTY, transID);

		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		return fromHost;
	}

	public Map toHostInBANK(TransferBank transferBank, CorpUser user,
			String txnType) throws NTBException {

		Map toHost = new HashMap();
		Map fromHost = new HashMap();

		RcCurrency rcCurrency = new RcCurrency();

		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpTransferService corpTransferService = (CorpTransferService) appContext
				.getBean("CorpTransferService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");

		CibTransClient testClient = new CibTransClient("CIB", "51CC");

		// TransferBank transferBank = transferService.viewInBANK(transID);

		// edit by mxl 0120
		String fromAcctypeCode = null;
		String toAcctypeCode = null;
		/* Modify by long_zg 2019-05-28 UAT6-113 COB 缁楊兛绗侀懓鍛扮秮鐠╊剙銇戦弫锟絙egin */
		/*if (transferBank.getFromAcctype().equals(
				CorpAccount.ACCOUNT_TYPE_CURRENT)
				|| transferBank.getFromAcctype().equals(
						CorpAccount.ACCOUNT_TYPE_OVER_DRAFT)) {
			fromAcctypeCode = "C";
		} else if (transferBank.getFromAcctype().equals(
				CorpAccount.ACCOUNT_TYPE_SAVING)) {
			fromAcctypeCode = "S";
		}
		if (transferBank.getToAcctype().equals(CorpAccount.ACCOUNT_TYPE_CURRENT)
				|| transferBank.getToAcctype().equals(
						CorpAccount.ACCOUNT_TYPE_OVER_DRAFT)) {
			toAcctypeCode = "C";
		} else if (transferBank.getToAcctype().equals(
				CorpAccount.ACCOUNT_TYPE_SAVING)) {
			toAcctypeCode = "S";
		}*/
		if (CorpAccount.ACCOUNT_TYPE_CURRENT.equals(transferBank.getFromAcctype())
				|| CorpAccount.ACCOUNT_TYPE_OVER_DRAFT.equals(transferBank.getFromAcctype())) {
			fromAcctypeCode = "C";
		} else if (CorpAccount.ACCOUNT_TYPE_SAVING.equals(transferBank.getFromAcctype())) {
			fromAcctypeCode = "S";
		}
		if (CorpAccount.ACCOUNT_TYPE_CURRENT.equals(transferBank.getToAcctype())
				|| CorpAccount.ACCOUNT_TYPE_OVER_DRAFT.equals(transferBank.getToAcctype())) {
			toAcctypeCode = "C";
		} else if (CorpAccount.ACCOUNT_TYPE_SAVING.equals(transferBank.getToAcctype())) {
			toAcctypeCode = "S";
		}
		
		if(null==toAcctypeCode || "".equals(toAcctypeCode)){
			if(transferBank.getToAccount().startsWith("6")){
				toAcctypeCode = "C";
			} else {
				toAcctypeCode = "S";
			}
		}
		
		/* Modify by long_zg 2019-05-28 UAT6-113 COB 缁楊兛绗侀懓鍛扮秮鐠╊剙銇戦弫锟絜nd */
//		String txnCode = "51" + fromAcctypeCode + toAcctypeCode;
		String txnCode = "51CC";
		testClient = new CibTransClient("CIB", txnCode);

		// add by mxl 0120闁跨喐鏋婚幏鐑芥晸缁叉Γpha1,alph2
		String alphaDesc = transferService.loadAlphal(txnCode);
		String alpha1 = getAlphiField(alphaDesc, transferBank.getToAccount());
		String alpha2 = getAlphiField(alphaDesc, transferBank.getFromAccount());
		/* add by mxl 0826 get the currency number according the currency code */
		String fromCurrencyCode = rcCurrency.getCbsNumberByCcy(transferBank
				.getFromCurrency());
		String toCurrencyCode = rcCurrency.getCbsNumberByCcy(transferBank
				.getToCurrency());

		/* add by mxl 0814 */
		transferBank.setStatus(Constants.STATUS_NORMAL);
		transferBank.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
		transferBank.setExecuteTime(new Date());

		// update the transaction table
		transferDao.update(transferBank);

		String inputAmountFlag = transferBank.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferBank.getFromCurrency();
			transferAmount = transferBank.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferBank.getToCurrency();
			transferAmount = transferBank.getTransferAmount();
		}

		BigDecimal equivalentMOP = exRatesService.getEquivalent(user
				.getCorpId(), currency, "MOP", new BigDecimal(transferAmount
				.toString()), null, 2);
		
		String corpIdLimit = null;
		if (txnType.equals(Constants.TXN_SUBTYPE_TRANSFER_DELIVERY)
				|| txnType
						.equals(Constants.TXN_SUBTYPE_TRANSFER_BETWEEN_SUBSIDIARY)
				|| txnType.equals(Constants.TXN_SUBTYPE_TRANSFER_REPETRIATE)) {
			corpIdLimit = corpTransferService.getCorpIdByAccount(transferBank
					.getFromAccount());
		} else {
			corpIdLimit = user.getCorpId();
		}
		if (txnType.equals(Constants.TXN_SUBTYPE_TRANSFER_DELIVERY)) {
			//modify by long_zg 2015-04-24 for prd BOB transfer exceed limit begin
			/*transferLimitService.addUsedLimitQuota(
					transferBank.getFromAccount(), corpIdLimit,
					Constants.TXN_SUBTYPE_TRANSFER_BANK, transferBank
							.getDebitAmount().doubleValue(), equivalentMOP
							.doubleValue());*/
			transferLimitService.addUsedLimitQuota(
					transferBank.getFromAccount(), corpIdLimit,
					Constants.TXN_TYPE_TRANSFER_CORP, transferBank
							.getDebitAmount().doubleValue(), equivalentMOP
							.doubleValue());
			//modify by long_zg 2015-04-24 for prd BOB transfer exceed limit end
		} else if (txnType
				.equals(Constants.TXN_SUBTYPE_TRANSFER_BETWEEN_SUBSIDIARY)
				|| txnType.equals(Constants.TXN_SUBTYPE_TRANSFER_REPETRIATE)) {
			transferLimitService.addUsedLimitQuota(
					transferBank.getFromAccount(), corpIdLimit,
					Constants.TXN_TYPE_TRANSFER_CORP, transferBank
							.getDebitAmount().doubleValue(), equivalentMOP
							.doubleValue());
		} else {
			transferLimitService.addUsedLimitQuota(
					transferBank.getFromAccount(), corpIdLimit, txnType,
					transferBank.getDebitAmount().doubleValue(), equivalentMOP
							.doubleValue());
		}
		BigDecimal passbookBalance;
		BigDecimal exchangeIn;
		BigDecimal exchangeOut;

		passbookBalance = testClient.getPassbookBalance(transferBank
				.getFromCurrency(), transferBank.getToCurrency(),
				new BigDecimal(transferBank.getTransferAmount().toString()));

		exchangeIn = testClient.getExchangeIn(transferBank.getFromCurrency(),
				transferBank.getToCurrency(), new BigDecimal(transferBank
						.getTransferAmount().toString()));

		exchangeOut = testClient.getExchangeOut(transferBank.getFromCurrency(),
				transferBank.getToCurrency(), new BigDecimal(transferBank
						.getDebitAmount().toString()));

		// add by mxl 0830 get the from exchange rate and to exchange rate
		LceBean lceInfo = exRatesService.getLceInfo(transferBank
				.getFromCurrency(), transferBank.getToCurrency(),
				new BigDecimal(transferBank.getDebitAmount().toString()),
				new BigDecimal(transferBank.getTransferAmount().toString()));
		BigDecimal fromAmtLCE = lceInfo.getFromAmtLCE();
		BigDecimal fromExchangeRate = lceInfo.getFromRateLCE();
		BigDecimal toAmtLCE = lceInfo.getToAmtLCE();
		BigDecimal toExchangeRate = lceInfo.getToRateLCE();
		toHost.put("EFFECTIVE_DATE", CibTransClient.getCurrentDate());
		toHost.put("ACCOUNT_NO", transferBank.getFromAccount());
		toHost.put("TRANSFER_TO_ACC", transferBank.getToAccount());
		toHost.put("FROM_CCY", transferBank.getFromCurrency());
		toHost.put("TO_CCY", transferBank.getToCurrency());
		toHost.put("PASSBOOK_BALANCE", passbookBalance);

		
//		toHost.put("TRANSACTION_AMOUNT", transferBank.getDebitAmount());
		toHost.put("TRANSACTION_AMOUNT", transferAmount);
		toHost.put("PRINCIPAL_AMOUNT", transferBank.getTransferAmount());
		toHost.put("INTEREST_AMOUNT", toAmtLCE);
//		toHost.put("ESCROW_AMOUNT", toExchangeRate.multiply(new BigDecimal(
//				"100000")));
		toHost.put("ESCROW_AMOUNT", toExchangeRate.multiply(new BigDecimal(
		"0")));
		toHost.put("LCE_OF_TRANS_AMOUNT", fromAmtLCE);
		//toHost.put("FOREIGN_CCY_CODE", fromCurrencyCode);
//		toHost.put("FOREIGN_CCY_CODE", transferBank.getFromCurrency());
		toHost.put("FOREIGN_CCY_CODE", currency);
		toHost.put("FOREIGN_CCY_RATE", fromExchangeRate);
		toHost.put("CASHIER_NO", toCurrencyCode);
		toHost.put("ALPHA_1", alpha1);
		toHost.put("ALPHA_2", alpha2);
		toHost.put("EXCHANGE_IN_DR", exchangeIn);
		toHost.put("EXCHANGE_OUT_CR", exchangeOut);
		toHost.put("CLIENT_REF", transferBank.getRemark());
		
		//modify by long_zg 2014-12-15 for CR192 batch bob
		toHost.put("CHECK_FULLDAY_CURRENCY", transferBank.getFromCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_FROM", transferBank.getFromCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_TO", transferBank.getToCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_FLAG", "B") ;
		
		//update by gan 20180129
		toHost.put("REQ_REF", CibIdGenerator.getTDRef());
		toHost.put("TO_NAME", transferBank.getToName());
		toHost.put("REMARK", transferBank.getRemarkHost());//add by linrui 20190911

		testClient.setAlpha8_for51xx(user, CibTransClient.TXNNATURE_TRANSFER,
				CibTransClient.ACCTYPE_3RD_PARTY, transferBank.getTransId(),
				transferBank.getFlag());

		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		return fromHost;
	}

	public Map toHostInMacau(TransferMacau transferMacau, CorpUser user,
			String txnType) throws NTBException {

		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		Map cdtMap = new HashMap();
		Map conditionMap = new HashMap();
		ApplicationContext appContext = Config.getAppContext();
		// TransferMacau transferMacau = null;
		// TransferService transferService = (TransferService)
		// appContext.getBean("TransferService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");

		// transferMacau = transferService.viewInMacau(transID);

		RcCurrency rcCurrency = new RcCurrency();
		/* add by mxl 0826 */
		/* get the charge currency accounding to account 0807 */
		/*CorpAccount corpAccount = corpAccountService
				.viewCorpAccount(transferMacau.getChargeAccount());
		String chargeCurrency = corpAccount.getCurrency();*/

		transferMacau.setStatus(Constants.STATUS_NORMAL);
		transferMacau.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
		transferMacau.setExecuteTime(new Date());

		transferDao.update(transferMacau);

		// 闁跨喎褰ㄧ拋瑙勫闁跨喓鐓导娆愬闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归獮鏇㈡晸閺傘倖瀚瑰☉鎻掔番闁跨喐鏋婚幏锟絘dd by mxl 0922
		String inputAmountFlag = transferMacau.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferMacau.getFromCurrency();
			transferAmount = transferMacau.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferMacau.getToCurrency();
			transferAmount = transferMacau.getTransferAmount();
		}

		// edit by mxl 0109 闁岸鏁撻弬銈嗗闁跨喓鐓导娆愬闁跨喐鏋婚幏鐑姐�夐柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏閿嬪幈闁跨喐鏋婚幏椋庣崐闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喖銈虹拋瑙勫闁跨喕绶濋幉瀣
		BigDecimal equivalentMOP = exRatesService.getEquivalent(user
				.getCorpId(), currency, "MOP", new BigDecimal(transferAmount
				.toString()), null, 2);

		transferLimitService.addUsedLimitQuota(transferMacau.getFromAccount(),
				user.getCorpId(), txnType, transferMacau.getDebitAmount()
						.doubleValue(), equivalentMOP.doubleValue());
		CibTransClient testClient = new CibTransClient("CIB", "ZJ55");

		String CGD = null;
		String executeDate = null;
		String date = null;
		String exchangeRate = null;
		String BANK_NAME = null;
		String swiftCode = null;

		conditionMap.put("BANK_CODE", transferMacau.getBeneficiaryBank());

		// get bankcode from local_bank_code according to the bank_name
		GenericJdbcDao dao = (GenericJdbcDao) appContext
				.getBean("genericJdbcDao");

		try {
			List idList = null;
			idList = dao.query(SELECT_CGDLOCAL, new Object[] { transferMacau
					.getBeneficiaryBank() });
			cdtMap = (Map) idList.get(0);
			CGD = (String) cdtMap.get("CGD_FLAG");
			BANK_NAME = (String) cdtMap.get("BANK_NAME");
			swiftCode = (String) cdtMap.get("SWIFT_CODE");

		} catch (Exception e) {
			throw new NTBException("err.txn.GetCGDException");
		}

		/* add by mxl 0826 get the currency number according the currency code */
//		String fromCurrencyCode = rcCurrency.getCbsNumberByCcy(transferMacau
//				.getFromCurrency());
//		String toCurrencyCode = rcCurrency.getCbsNumberByCcy(transferMacau
//				.getToCurrency());
//		String chareCurrencyCode = rcCurrency.getCbsNumberByCcy(chargeCurrency);

		toHost.put("CORP_ID", this.getCifNoByCorpId(user.getCorpId())); //update by gan 20180201 "8000010994");//
		toHost.put("FROM_CCY", transferMacau.getFromCurrency());	//update by gan 20180201
		toHost.put("TO_CCY", transferMacau.getToCurrency());	//update by gan 20180201
		toHost.put("FROM_AMT", transferMacau.getDebitAmount());
		toHost.put("TO_AMT", transferMacau.getTransferAmount());
		toHost.put("DEBIT_ACC_NO", transferMacau.getFromAccount());
		toHost.put("CHARGE_ACC_NO", transferMacau.getChargeAccount());
		toHost.put("COMMISSION_AMT", transferMacau.getCommissionAmount());
		toHost.put("TAX_AMT4COMM", transferMacau.getTaxAmount());
		//add by lzg 20190601
		//fromHost = toHostChargeEnquiry(transferMacau.getTransferAmount(),transferMacau.getFromCurrency(),transferMacau.getToCurrency());
		//add by lzg end
		//modified by lzg 20190601
		/*toHost.put("HANDLING_CHRG_AMT", transferOversea.getChargeAmount());
		toHost.put("HANDLING_FEE_AMT", transferOversea.getHandlingAmount());*/
		toHost.put("HANDLING_CHRG_AMT", transferMacau.getChargeAmount());
		toHost.put("HANDLING_FEE_AMT", transferMacau.getHandlingAmount());
		/*toHost.put("HANDLING_CHRG_AMT", fromHost.get("HANDLING_CHRG_AMT"));
		toHost.put("HANDLING_FEE_AMT", fromHost.get("HANDLING_FEE_AMT"));*/
		//add by lzg 0601
		toHost.put("SWIFT_CHRG_AMT", transferMacau.getSwiftAmount());
		toHost.put("OUR_CHG_AMT", transferMacau.getChargeOur());
		toHost.put("COUNTRY_CODE", "MO");
		toHost.put("CITY_CODE", " ");
//		toHost.put("BANK_ID", transferMacau.getBeneficiaryBank());
		toHost.put("BANK_ID", Utils.prefixSpace(swiftCode, 11)); //update by gan 20180202
		toHost.put("BENE_NAME_LINE_1", transferMacau.getBeneficiaryName1());
		toHost.put("BENE_NAME_LINE_2", transferMacau.getBeneficiaryName2());
		toHost.put("BENE_NAME_LINE_3", " ");
		toHost.put("BENE_NAME_LINE_4", " ");
		toHost.put("BENE_ACCOUNT", transferMacau.getToAccount());
		toHost.put("BENE_BIC_SWIFT", swiftCode);
		toHost.put("BENE_BNK_LINE_1", BANK_NAME);
		toHost.put("BENE_BNK_LINE_2", " ");
		toHost.put("BENE_BNK_LINE_3", " ");
		toHost.put("BENE_BNK_LINE_4", " ");
		toHost.put("REQUEST_TYPE", transferMacau.getRequestType());
		
		// add by mxl 0827
		if (transferMacau.getRequestType().equals("1")) {
			date = "0";
		}

		else if (transferMacau.getRequestType().equals("2")) {
			executeDate = transferMacau.getTransferDate().toString();
			date = DateTime.formatDate(executeDate, "yyyyMMdd");
		}
		toHost.put("EXECUTION_DATE", date);
		toHost.put("CONTACT_NUMBER", " ");
		toHost.put("CGD", CGD);
		if (transferMacau.getExchangeRate().doubleValue() == 1.0) {
			exchangeRate = "0";
		} else {
			exchangeRate = transferMacau.getExchangeRate().toString();
		}
		toHost.put("EX_RATE_AMT", exchangeRate);
		toHost.put("BENE_BNK_CHRG_OPT", transferMacau.getChargeBy());
		toHost.put("MSG_LINE_1", transferMacau.getMesssage());
		toHost.put("MSG_LINE_2", transferMacau.getMesssage2());
		toHost.put("MSG_LINE_3", " ");
		toHost.put("MSG_LINE_4", " ");
		toHost.put("COUNTRY_BANK_CODE", "MO");
		toHost.put("COUNTRY_BENE_ACC_TYPE", "0");
		toHost.put("CLIENT_REF", transferMacau.getRemark());
		toHost.put("CHG_CCY", transferMacau.getFromCurrency());
		toHost.put("CORR_BNK_LINE_1", " ");
		toHost.put("CORR_BNK_LINE_2", " ");
		toHost.put("CORR_BNK_LINE_3", " ");
		toHost.put("CORR_BNK_LINE_4", " ");
		toHost.put("CORR_BNK_ACCOUNT", " ");

		/* Add by heyongjiang 20100804 */
//		toHost.put("PURPOSE_CODE", "99");mod by linrui 20180323
		//toHost.put("PURPOSE_CODE", "C.4.5");
		//MODIFIED BY LZG 20190603
		String purposeCode = transferMacau.getPurposeCode();
		/*if(purposeCode != null){
			if(purposeCode.contains("C.4.5")){
				purposeCode = "C.4.5";
			}
		}*/
		toHost.put("PURPOSE_CODE", purposeCode);
		//MODIFIED BY LZG END
		toHost.put("OTHER_PURPOSE", transferMacau.getOtherPurpose());
		toHost.put("PROOF_OF_PURPOSE", transferMacau.getProofOfPurpose());
		Log.info("to Host In Macau the purpose is:" + transferMacau.getOtherPurpose());
		/* Add by heyongjiang 20200804 end */

		toHost.put("IBAN", "N");
		
		//modify by long_zg 2014-12-15 for CR192 batch bob
		toHost.put("CHECK_FULLDAY_CURRENCY", transferMacau.getToCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_FROM", transferMacau.getFromCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_TO", transferMacau.getToCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_FLAG", "O") ;

		//update by gan 20180202 TXSERNO
		String txSerNo = Utils.prefixZero(CibIdGenerator.getTDRef(),24);
		toHost.put("TXSERNO", txSerNo);
		//add by linrui 20180326
		String charge = transferMacau.getChargeBy().toString();
		if("O".equalsIgnoreCase(charge))
			charge = "1";
		else 
			charge = "S".equalsIgnoreCase(charge)?"2":"3";
		toHost.put("BANKXGE", charge);
		//toHost.put("CHANGE_FLG", Constants.INPUT_AMOUNT_FLAG_TO.equalsIgnoreCase(transferMacau.getInputAmountFlag().toString())?Constants.INPUT_AMOUNT_FLAG_TO:Constants.INPUT_AMOUNT_FLAG_FROM);
		String changeFlag = "";
		if(!transferMacau.getToCurrency().equals(transferMacau.getFromCurrency())){
			changeFlag = transferMacau.getInputAmountFlag();
		}
		toHost.put("CHANGE_FLG", changeFlag);
//		toHost.put("CHANGE_FLG", Constants.INPUT_AMOUNT_FLAG_TO.equalsIgnoreCase(transferMacau.getInputAmountFlag().toString())?0:"");
		toHost.put("CHANGE_AMT", Constants.INPUT_AMOUNT_FLAG_TO.equalsIgnoreCase(transferMacau.getInputAmountFlag().toString())?
				                                      transferMacau.getTransferAmount():
				                                      transferMacau.getDebitAmount());
		
		testClient.setAlpha8(user, CibTransClient.TXNNATURE_TRANSFER,
				CibTransClient.ACCTYPE_3RD_PARTY, transferMacau.getTransId());

		fromHost = testClient.doTransaction(toHost);
		// mod by linrui 20180306
		/*if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}*/
		if ("".equals(Utils.null2EmptyWithTrim(fromHost.get("TRANSFER_REFERENCE")))) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		return fromHost;

	}

	public Map toHostOverseas(TransferOversea transferOversea, CorpUser user,
			String txnType) throws NTBException {

		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		Map cdtMap = new HashMap();
		RcCurrency rcCurrency = new RcCurrency();

		CibTransClient testClient = new CibTransClient("CIB", "ZJ55");
		// TransferOversea transferOversea = null;
		String CGD = "N";

		String swiftCode = null;
		String executeDate = null;
		String date = null;
		String exchangeRate = null;
		String Iban = null;

		// initial service
		ApplicationContext appContext = Config.getAppContext();
		// TransferOversea transferOversea = null;
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		// TransferOversea transferOversea = transferService
		// .viewInOversea(transID);
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		/* add by mxl 0826 */
		/* get the charge currency accounding to account 0807 */
		/*CorpAccount corpAccount = corpAccountService
				.viewCorpAccount(transferOversea.getChargeAccount());
		String chargeCurrency = corpAccount.getCurrency();*/

		/* add by mxl 0826 get the currency number according the currency code */
		String fromCurrencyCode = rcCurrency.getCbsNumberByCcy(transferOversea
				.getFromCurrency());
		String toCurrencyCode = rcCurrency.getCbsNumberByCcy(transferOversea
				.getToCurrency());
		/*String chargeCurrencyCode = rcCurrency
				.getCbsNumberByCcy(chargeCurrency);*/

		transferOversea.setStatus(Constants.STATUS_NORMAL);
		transferOversea.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
		transferOversea.setExecuteTime(new Date());

		// update tansaction table
		transferDao.update(transferOversea);

		String inputAmountFlag = transferOversea.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferOversea.getFromCurrency();
			transferAmount = transferOversea.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferOversea.getToCurrency();
			transferAmount = transferOversea.getTransferAmount();
		}

		BigDecimal equivalentMOP = exRatesService.getEquivalent(user
				.getCorpId(), currency, "MOP", new BigDecimal(transferAmount
				.toString()), null, 2);

		transferLimitService.addUsedLimitQuota(
				transferOversea.getFromAccount(), user.getCorpId(), txnType,
				transferOversea.getDebitAmount().doubleValue(), equivalentMOP
						.doubleValue());

		GenericJdbcDao dao = (GenericJdbcDao) appContext
				.getBean("genericJdbcDao");
		String Bank_Name = transferOversea.getBeneficiaryBank1();
		// get the CGD_FLAG from the table HS_OVERSEAS_BANK
		if (transferOversea.getBankFlag().equals("N")) {
			try {
				List idList = null;
				idList = dao.query(SELECT_CGDOVERSEA,
						new Object[] { transferOversea.getBeneficiaryBank1() });
				cdtMap = (Map) idList.get(0);
				if (null != idList && idList.size() > 0) {
					CGD = (String) cdtMap.get("CGD_FLAG");
					Bank_Name = (String) cdtMap.get("BANK_NAME");
					swiftCode = (String) cdtMap.get("SWIFT_CODE");
				} else {
					Bank_Name = transferOversea.getBeneficiaryBank1();
				}

			} catch (Exception e) {
				throw new NTBException("err.txn.GetCGDException");
			}
		}
		// get the country name from the table HS_COUNTRY_CODE according to the
		// Country code
		// get the city name form the table HS_CITY_CODE according to the city
		// code
		String City_Name = transferOversea.getBeneficiaryCity();
		if (transferOversea.getCityFlag().equals("N")) {
			try {
				List idList1 = null;
				idList1 = dao.query(SELECT_CITYNAME,
						new Object[] { transferOversea.getBeneficiaryCity() });
				if (null != idList1 && idList1.size() > 0) {
					cdtMap = (Map) idList1.get(0);
					City_Name = (String) cdtMap.get("CITY_NAME");
				} else {
					City_Name = transferOversea.getBeneficiaryCity();
				}

			} catch (Exception e) {
				throw new NTBException("err.txn.GetCityNameException");
			}
		}
		// add by mxl 0914 get the Country specific Bank Code from the table
		// HS_COUNTRY_SPECIFIC_BANK_CODE_LABEL

		String ACC_LABEL_SEQ = null;
		try {
			List idList1 = null;
			idList1 = dao.query(SELECT_ACC_LABEL_SQL, new Object[] {
					transferOversea.getBeneficiaryCountry(),
					transferOversea.getAccType() });
			if (null != idList1 && idList1.size() > 0) {
				Map cdtMap1 = (Map) idList1.get(0);
				ACC_LABEL_SEQ = (String) cdtMap1.get("ACC_LABEL_SEQ");
			} else {
				ACC_LABEL_SEQ = "00";
			}

		} catch (Exception e) {
			throw new NTBException("err.txn.GetAccLabelException");
		}

		// add by mxl 0915
		if (Utils.null2EmptyWithTrim(transferOversea.getAccType()).equals(
				"IBAN")) {
			Iban = "Y";
		} else {
			Iban = "N";
		}

		toHost.put("CORP_ID",  this.getCifNoByCorpId(user.getCorpId())); //update by gan 20180205
		toHost.put("FROM_CCY", transferOversea.getFromCurrency()); //update by gan
		toHost.put("TO_CCY", transferOversea.getToCurrency()); // update by gan
		toHost.put("FROM_AMT", transferOversea.getDebitAmount());
		toHost.put("TO_AMT", transferOversea.getTransferAmount());
		toHost.put("DEBIT_ACC_NO", transferOversea.getFromAccount());
		toHost.put("CHARGE_ACC_NO", transferOversea.getChargeAccount());
		toHost.put("COMMISSION_AMT", transferOversea.getCommissionAmount());
		toHost.put("TAX_AMT4COMM", transferOversea.getTaxAmount());
		//add by lzg 20190601
		//fromHost = toHostChargeEnquiry(transferOversea.getTransferAmount(),transferOversea.getFromCurrency(),transferOversea.getToCurrency());
		//add by lzg end
		//modified by lzg 20190601
		/*toHost.put("HANDLING_CHRG_AMT", transferOversea.getChargeAmount());
		toHost.put("HANDLING_FEE_AMT", transferOversea.getHandlingAmount());*/
		toHost.put("HANDLING_CHRG_AMT", transferOversea.getChargeAmount());
		toHost.put("HANDLING_FEE_AMT", transferOversea.getHandlingAmount());
		/*toHost.put("HANDLING_CHRG_AMT", fromHost.get("HANDLING_CHRG_AMT"));
		toHost.put("HANDLING_FEE_AMT", fromHost.get("HANDLING_FEE_AMT"));*/
		//add by lzg 0601
		toHost.put("SWIFT_CHRG_AMT", transferOversea.getSwiftAmount());
		toHost.put("OUR_CHG_AMT", transferOversea.getChargeOur());
		if(transferOversea.getBeneficiaryCountry().equals("OT")){
			toHost.put("COUNTRY_CODE", "");
		}else{
			toHost.put("COUNTRY_CODE", transferOversea.getBeneficiaryCountry());
		}
		if (transferOversea.getCityFlag().equals("Y")) {
			toHost.put("CITY_CODE", " ");

		} else if (transferOversea.getCityFlag().equals("N")) {
			if("OTH".equals(transferOversea.getBeneficiaryCity())){
				toHost.put("CITY_CODE", "");
			}else{
				toHost.put("CITY_CODE", transferOversea.getBeneficiaryCity());
			}
		}
		//update by gan 20180226
		/*if (transferOversea.getBankFlag().equals("Y")) {
			toHost.put("BANK_ID", " ");
		} else if (transferOversea.getBankFlag().equals("N")) {
			toHost.put("BANK_ID", transferOversea.getBeneficiaryBank1());
		}*/
		//toHost.put("BANK_ID", Utils.prefixSpace(transferOversea.getSwiftAddress(), 11)); 
		toHost.put("BANK_ID", transferOversea.getSwiftAddress()); //mod by linrui 20190523

		toHost.put("BENE_NAME_LINE_1", transferOversea.getBeneficiaryName1());
		toHost.put("BENE_NAME_LINE_2", transferOversea.getBeneficiaryName2());
		// updated by xyf 20090721 for CR107--------
		/*
		 * toHost.put("BENE_NAME_LINE_3", " "); toHost.put("BENE_NAME_LINE_4", "
		 * ");
		 */
		if((transferOversea.getBeneficiaryName3() != null && !transferOversea.getBeneficiaryName3().equals("")) || (transferOversea.getBeneficiaryName4() != null && !transferOversea.getBeneficiaryName4().equals(""))){
			toHost.put("BENE_NAME_LINE_3", "ADD." + transferOversea.getBeneficiaryName3());
		}else{
			toHost.put("BENE_NAME_LINE_3", transferOversea.getBeneficiaryName3());
		}
		toHost.put("BENE_NAME_LINE_4", transferOversea.getBeneficiaryName4());
		// ---------------end of xyf----------------
		toHost.put("BENE_ACCOUNT", transferOversea.getToAccount());
		toHost.put("BENE_BIC_SWIFT", transferOversea.getSwiftAddress());
		toHost.put("BENE_BNK_LINE_1", Bank_Name);
		toHost.put("BENE_BNK_LINE_2", transferOversea.getBeneficiaryBank2());
		toHost.put("BENE_BNK_LINE_3", transferOversea.getBeneficiaryBank3());
		toHost.put("BENE_BNK_LINE_4", " ");
		toHost.put("REQUEST_TYPE", transferOversea.getRequestType());
		toHost.put("BENE_BNK_CITY", City_Name);
		// add by mxl 0827
		if (transferOversea.getRequestType().equals("1")) {
			date = "0";
		}

		else if (transferOversea.getRequestType().equals("2")) {
			executeDate = transferOversea.getTransferDate().toString();
			date = DateTime.formatDate(executeDate, "yyyyMMdd");
		}

		toHost.put("EXECUTION_DATE", date);
		toHost.put("CONTACT_NUMBER", " ");
		toHost.put("CGD", CGD);
		if (transferOversea.getExchangeRate().doubleValue() == 1.0) {
			exchangeRate = "0";
		} else {
			exchangeRate = transferOversea.getExchangeRate().toString();

		}
		toHost.put("EX_RATE_AMT", exchangeRate);
		toHost.put("BENE_BNK_CHRG_OPT", transferOversea.getChareBy());
		toHost.put("MSG_LINE_1", transferOversea.getMesssage());
		toHost.put("MSG_LINE_2", transferOversea.getMesssage2());
		toHost.put("MSG_LINE_3", transferOversea.getMesssage3());
		toHost.put("MSG_LINE_4", " ");
		toHost.put("COUNTRY_BANK_CODE", transferOversea.getSpbankCode());
		toHost.put("COUNTRY_BENE_ACC_TYPE", ACC_LABEL_SEQ);
		toHost.put("CLIENT_REF", transferOversea.getRemark());
		toHost.put("CHG_CCY", transferOversea.getFromCurrency());
		/*toHost.put("CORR_BNK_LINE_1", transferOversea
				.getCorrespondentBankLine1());
		toHost.put("CORR_BNK_LINE_2", transferOversea
				.getCorrespondentBankLine2());
		toHost.put("CORR_BNK_LINE_3", transferOversea
				.getCorrespondentBankLine3());
		toHost.put("CORR_BNK_LINE_4", transferOversea
				.getCorrespondentBankLine4());
		toHost.put("CORR_BNK_ACCOUNT", transferOversea
				.getCorrespondentBankAccount());*/

		/* Add by heyongjiang 20100804 */
//		toHost.put("PURPOSE_CODE", "99");mod by linrui 20180323
		//modified by lzg 20190602
		//toHost.put("PURPOSE_CODE", "C.4.5");
		String purposeCode = transferOversea.getPurposeCode();
		/*if(purposeCode != null){
			if(purposeCode.contains("C.4.5")){
				purposeCode = "C.4.5";
			}
		}*/
		toHost.put("PURPOSE_CODE", purposeCode);
		//modified by lzg end
		toHost.put("OTHER_PURPOSE", transferOversea.getOtherPurpose());
		toHost.put("PROOF_OF_PURPOSE", transferOversea.getProofOfPurpose());
		Log.info("to Host In Oversea the purpose is:" + transferOversea.getOtherPurpose());
		/* Add by heyongjiang 20200804 end */

		toHost.put("IBAN", Iban);
		
		//modify by long_zg 2014-12-15 for CR192 batch bob
		toHost.put("CHECK_FULLDAY_CURRENCY", transferOversea.getToCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_FROM", transferOversea.getFromCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_TO", transferOversea.getToCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_FLAG", "O") ;

		//update by gan 20180202 TXSERNO
		String txSerNo = Utils.prefixZero(CibIdGenerator.getTDRef(),24);
		toHost.put("TXSERNO", txSerNo);
		//add by linrui 20180326
		String charge = transferOversea.getChareBy().toString();
		if("O".equalsIgnoreCase(charge))
			charge = "1";
		else 
			charge = "S".equalsIgnoreCase(charge)?"2":"3";
		toHost.put("BANKXGE", charge);
		String changeFlag = "";
		if(!transferOversea.getToCurrency().equals(transferOversea.getFromCurrency())){
			changeFlag = transferOversea.getInputAmountFlag();
		}
		toHost.put("CHANGE_FLG", changeFlag);
//		toHost.put("CHANGE_FLG", Constants.INPUT_AMOUNT_FLAG_TO.equalsIgnoreCase(transferOversea.getInputAmountFlag().toString())?Constants.INPUT_AMOUNT_FLAG_TO:Constants.INPUT_AMOUNT_FLAG_FROM);
		toHost.put("CHANGE_AMT", Constants.INPUT_AMOUNT_FLAG_TO.equalsIgnoreCase(transferOversea.getInputAmountFlag().toString())?
				                                      transferOversea.getTransferAmount():
				                                      transferOversea.getDebitAmount());
		//toHost.put("INTER_SWIFT_CODE", transferOversea.getCorrespondentBankLine1());
		toHost.put("INTER_SWIFT_CODE", transferOversea.getCorrespondentBankCode());
		
		testClient.setAlpha8(user, CibTransClient.TXNNATURE_TRANSFER,
				CibTransClient.ACCTYPE_3RD_PARTY, transferOversea.getTransId());

		fromHost = testClient.doTransaction(toHost);
		// mod by linrui 20180306
		/*if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}*/
		if ("".equals(Utils.null2EmptyWithTrim(fromHost.get("TRANSFER_REFERENCE")))) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		return fromHost;
	}
	
	//add by lzg 20190601
	private Map toHostChargeEnquiry(Double transferAmount,String fromCurrency,String toCurrency) throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		CibTransClient testClient = new CibTransClient("CIB", "ZJ53");
		toHost.put("TRANSACTION_AMOUNT", transferAmount);
		toHost.put("FROM_CCY", fromCurrency);
		toHost.put("TO_CCY", toCurrency);
		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		return fromHost;
	}
	//add by lzg end
	// add by mxl 0818

	public Map toHostAccountInBANK(String transID, CorpUser user,
			String toAccount) throws NTBException {

		Map toHost = new HashMap();
		Map fromHost = new HashMap();
//		CibTransClient testClient = new CibTransClient("CIB", "ZJ32");//mod by linrui 20180227
		CibTransClient testClient = new CibTransClient("CIB", "0295");

		toHost.put("ACCOUNT_NO", toAccount);
		testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_OWNER_ACCOUNT, transID);

		fromHost = testClient.doTransaction(toHost);

		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}

		return fromHost;
	}
	//add by linrui for mul-ccy 20181224
	public Map toHostAccountInBANK(String transID, CorpUser user,
			String toAccount, String toCcy) throws NTBException {
		
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
//		CibTransClient testClient = new CibTransClient("CIB", "ZJ32");//mod by linrui 20180227
		CibTransClient testClient = new CibTransClient("CIB", "0295");
		
		toHost.put("ACCOUNT_NO", toAccount);
		toHost.put("CCY", toCcy);
		testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_OWNER_ACCOUNT, transID);
		
		fromHost = testClient.doTransaction(toHost);
		
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		
		return fromHost;
	}

	/* add by mxl 0820 */

	/* add mxl 0810 */
	public void approveBank(TransferBank transferBank) throws NTBException {
		if (transferBank != null) {
			transferBank.setStatus(Constants.STATUS_NORMAL);
			transferBank.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			transferBank.setExecuteTime(new Date());
			transferDao.update(transferBank);
		}

	}

	public void rejectBank(TransferBank transferBank) throws NTBException {
		if (transferBank != null) {
			transferBank.setStatus(Constants.STATUS_REMOVED);
			transferBank.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
			transferBank.setExecuteTime(new Date());
			transferDao.update(transferBank);
		}

	}

	public void approveMacau(TransferMacau transferMacau) throws NTBException {
		if (transferMacau != null) {
			transferMacau.setStatus(Constants.STATUS_NORMAL);
			transferMacau.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			transferMacau.setExecuteTime(new Date());
			transferDao.update(transferMacau);
		}

	}

	public void rejectMacau(TransferMacau transferMacau) throws NTBException {
		if (transferMacau != null) {
			transferMacau.setStatus(Constants.STATUS_REMOVED);
			transferMacau.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
			transferMacau.setExecuteTime(new Date());
			transferDao.update(transferMacau);
		}

	}

	public void approveOversea(TransferOversea transferOversea)
			throws NTBException {
		if (transferOversea != null) {
			transferOversea.setStatus(Constants.STATUS_NORMAL);
			transferOversea.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			transferOversea.setExecuteTime(new Date());
			transferDao.update(transferOversea);
		}

	}

	public void rejectOversea(TransferOversea transferOversea)
			throws NTBException {
		if (transferOversea != null) {
			transferOversea.setStatus(Constants.STATUS_REMOVED);
			transferOversea.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
			transferOversea.setExecuteTime(new Date());
			transferDao.update(transferOversea);
		}

	}

	// edit by mxl 0819
	public void loadUploadBANK(TransferBank transferBank, Map fromHost)
			throws NTBException {
		Map uploadMap = new HashMap();

		uploadMap.put("TRANS_DATE", CibTransClient.getCurrentDate());
		uploadMap.put("TRANS_TIME ", CibTransClient.getCurrentTime());
		uploadMap.put("TRANS_CODE", "51CC");
		uploadMap.put("CORRECTION_FLAG", " ");
		uploadMap.put("TRANS_REF_NO", fromHost.get("TELLER_ID").toString()
				+ fromHost.get("UNIQUE_SEQUENCE_NO").toString());
		uploadMap.put("TRANS_AMOUNT", transferBank.getDebitAmount());
		uploadMap.put("TRANS_PN_SIGN", "+");
		uploadMap.put("TRANS_AMT_CCY", transferBank.getFromCurrency());
		uploadMap.put("FROM_ACCOUNT", transferBank.getFromAccount());
		uploadMap.put("TO_ACCOUNT", transferBank.getToAccount());
		uploadMap.put("CREDIT_AMOUNT", transferBank.getTransferAmount());
		uploadMap.put("CREDIT_PN_SIGN", "+");
		uploadMap.put("CREDIT_AMT_CCY", transferBank.getToCurrency());
		uploadMap.put("EXCHANGE_RATE", transferBank.getExchangeRate());
		uploadMap.put("PN_SIGN", "+");
		uploadMap.put("USER_ID", transferBank.getUserId());
		//uploadMap.put("CIF", transferBank.getCorpId());
		uploadMap.put("CIF", transferBank.getCorpId().replace("C", ""));
		uploadMap.put("STATUS", fromHost.get("RESPONSE_CODE"));
		uploadMap.put("REJECT_CODE", fromHost.get("REJECT_CODE"));
		//add by long_zg 2015-01-28 for CR205 Add IP
//		uploadMap.put("IP_ADDRESS", fromHost.get("IP_ADDRESS"));
		UploadReporter.write("RP_ACCTTFR", uploadMap);

	}

	public void loadUploadMacau(TransferMacau transferMacau, Map fromHost)
			throws NTBException {
		Map cdtMap = new HashMap();
		String BANK_NAME = null;
		try {
			List idList = null;
			idList = genericJdbcDao.query(SELECT_CGDLOCAL,
					new Object[] { transferMacau.getBeneficiaryBank() });
			cdtMap = (Map) idList.get(0);

			BANK_NAME = (String) cdtMap.get("BANK_NAME");
		} catch (Exception e) {
			throw new NTBException("err.txn.GetBankNameException");
		}

		Map uploadMap = new HashMap();
		// edit by mxl 0930
		uploadMap.put("TRANS_DATE", CibTransClient.getCurrentDate());
		uploadMap.put("TRANS_TIME ", CibTransClient.getCurrentTime());
		uploadMap.put("TRANS_CODE", "ZJ55");
		uploadMap.put("CORRECTION_FLAG", " ");

		uploadMap.put("TRANS_REF_NO", fromHost.get("TELLER_ID").toString()
				+ fromHost.get("UNIQUE_SEQUENCE_NO").toString());

		uploadMap.put("TRANS_FROM_CCY", transferMacau.getFromCurrency());
		uploadMap.put("TRANS_TO_CCY", transferMacau.getToCurrency());
		uploadMap.put("TRANS_FROM_AMT", transferMacau.getDebitAmount());
		uploadMap.put("TRANS_FROM_SIGN", "+");
		uploadMap.put("TRANS_TO_AMT", transferMacau.getTransferAmount());
		uploadMap.put("TRANS_TO_SIGN", "+");
		uploadMap.put("DEBIT_ACCOUNT", transferMacau.getFromAccount());
		uploadMap.put("CHARGE_ACCOUNT", transferMacau.getChargeAccount());
		uploadMap.put("COMMISSION_AMOUNT", transferMacau.getCommissionAmount());
		uploadMap.put("COMM_PN_SIGN", "+");
		uploadMap.put("TAX_AMOUNT", transferMacau.getTaxAmount());
		uploadMap.put("TAX_PN_SIGN", "+");
		uploadMap.put("HANDLING_CHG_AMT", transferMacau.getHandlingAmount());
		uploadMap.put("HANDLING_PN_SIGN", "+");
		uploadMap.put("SWIFT_CHARGE", transferMacau.getSwiftAmount());
		uploadMap.put("PN_SIGN", "+");
		uploadMap.put("COUNTRY_CODE", "MO");
		uploadMap.put("CITY_CODE", "");
		uploadMap.put("BANK_CODE", transferMacau.getBeneficiaryBank());
		uploadMap.put("REQUEST_TYPE", transferMacau.getRequestType());
		uploadMap.put("EXECUTION_DATE", DateTime.formatDate(transferMacau
				.getExecuteTime(), "yyyyMMdd"));
		uploadMap.put("BENE_NAME_1", transferMacau.getBeneficiaryName1());
		uploadMap.put("BENE_NAME_2", transferMacau.getBeneficiaryName2());
		uploadMap.put("BENE_NAME_3", " ");
		uploadMap.put("BENE_NAME_4", " ");
		uploadMap.put("BENE_BANK_1", BANK_NAME.trim());
		uploadMap.put("BENE_BANK_2", " ");
		uploadMap.put("BENE_BANK_3", " ");
		uploadMap.put("BENE_BANK_4", " ");
		uploadMap.put("BENE_CITY", " ");
		uploadMap.put("BIC_SWIFT", " ");
		uploadMap.put("CONTACT_PHONE_NO", " ");
		uploadMap.put("USER_ID", transferMacau.getUserId());
		uploadMap.put("CORPORATION_ID", transferMacau.getCorpId());
		uploadMap.put("REMIT_TRANS_REF", transferMacau.getTransId()
				.substring(2));
		uploadMap.put("STATUS", fromHost.get("RESPONSE_CODE"));
		uploadMap.put("REJECT_CODE", fromHost.get("REJECT_CODE"));
		
		//add by long_zg 2015-01-28 for CR205 Add IP
		uploadMap.put("IP_ADDRESS", fromHost.get("IP_ADDRESS"));

		UploadReporter.write("RP_BENTFRNREG", uploadMap);

	}

	public void loadUploadOversea(TransferOversea transferOversea, Map fromHost)
			throws NTBException {
		Map uploadMap = new HashMap();
		Map cdtMap = new HashMap();
		String Bank_Name = null;
		String City_Name = null;
		if (transferOversea.getBankFlag().equals("N")) {
			try {
				List idList = null;
				idList = genericJdbcDao.query(SELECT_CGDOVERSEA,
						new Object[] { transferOversea.getBeneficiaryBank1() });
				cdtMap = (Map) idList.get(0);
				if (null != idList && idList.size() > 0) {

					Bank_Name = (String) cdtMap.get("BANK_NAME");

				} else {
					Bank_Name = transferOversea.getBeneficiaryBank1();
				}

			} catch (Exception e) {
				throw new NTBException("err.txn.GetBankNameException");
			}
		} else {
			Bank_Name = transferOversea.getBeneficiaryBank1();
		}
		if (transferOversea.getCityFlag().equals("N")) {
			try {
				List idList1 = null;
				idList1 = genericJdbcDao.query(SELECT_CITYNAME,
						new Object[] { transferOversea.getBeneficiaryCity() });
				if (null != idList1 && idList1.size() > 0) {
					cdtMap = (Map) idList1.get(0);
					City_Name = (String) cdtMap.get("CITY_NAME");
				} else {
					City_Name = transferOversea.getBeneficiaryCity();
				}

			} catch (Exception e) {
				// City_Name = transferOversea.getBeneficiaryCity();
				throw new NTBException("err.txn.GetCityNameException");
			}
		} else {
			City_Name = transferOversea.getBeneficiaryCity();
		}
		// edit by mxl 0930
		uploadMap.put("TRANS_DATE", CibTransClient.getCurrentDate());
		uploadMap.put("TRANS_TIME ", CibTransClient.getCurrentTime());
		uploadMap.put("TRANS_CODE", "ZJ55");
		uploadMap.put("CORRECTION_FLAG", "");
		uploadMap.put("TRANS_REF_NO", fromHost.get("TELLER_ID").toString()
				+ fromHost.get("UNIQUE_SEQUENCE_NO").toString());
		uploadMap.put("TRANS_FROM_CCY", transferOversea.getFromCurrency());
		uploadMap.put("TRANS_TO_CCY", transferOversea.getToCurrency());
		uploadMap.put("TRANS_FROM_AMT", transferOversea.getDebitAmount());
		uploadMap.put("TRANS_FROM_SIGN", "+");
		uploadMap.put("TRANS_TO_AMT", transferOversea.getTransferAmount());
		uploadMap.put("TRANS_TO_SIGN", "+");
		uploadMap.put("DEBIT_ACCOUNT", transferOversea.getFromAccount());
		uploadMap.put("CHARGE_ACCOUNT", transferOversea.getChargeAccount());
		uploadMap.put("COMMISSION_AMOUNT", transferOversea
				.getCommissionAmount());
		uploadMap.put("COMM_PN_SIGN", "+");
		uploadMap.put("TAX_AMOUNT", transferOversea.getTaxAmount());
		uploadMap.put("TAX_PN_SIGN", "+");
		uploadMap.put("HANDLING_CHG_AMT", transferOversea.getHandlingAmount());
		uploadMap.put("HANDLING_PN_SIGN", "+");
		uploadMap.put("SWIFT_CHARGE", transferOversea.getSwiftAmount());
		uploadMap.put("PN_SIGN", "+");
		uploadMap.put("COUNTRY_CODE", transferOversea.getBeneficiaryCountry());
		if (transferOversea.getCityFlag().equals("Y")) {
			uploadMap.put("CITY_CODE", " ");

		} else if (transferOversea.getCityFlag().equals("N")) {
			uploadMap.put("CITY_CODE", transferOversea.getBeneficiaryCity());

		}
		if (transferOversea.getBankFlag().equals("Y")) {
			uploadMap.put("BANK_CODE", " ");
		} else if (transferOversea.getBankFlag().equals("N")) {
			uploadMap.put("BANK_CODE", transferOversea.getBeneficiaryBank1());
		}
		uploadMap.put("REQUEST_TYPE", transferOversea.getRequestType());
		uploadMap.put("EXECUTION_DATE", DateTime.formatDate(transferOversea
				.getExecuteTime(), "yyyyMMdd"));
		uploadMap.put("BENE_NAME_1", transferOversea.getBeneficiaryName1());
		uploadMap.put("BENE_NAME_2", transferOversea.getBeneficiaryName2());

		// xyf 20090721 Modified, for CR107-------
		/*
		 * uploadMap.put("BENE_NAME_3", ""); uploadMap.put("BENE_NAME_4", "");
		 */
		uploadMap.put("BENE_NAME_3", transferOversea.getBeneficiaryName3());
		uploadMap.put("BENE_NAME_4", transferOversea.getBeneficiaryName4());
		// -----------end of xyf-------------------

		// Jet Modified, bank name may be more than 35 bytes
		if (Bank_Name.length() > 35) {
			Bank_Name = Bank_Name.trim().substring(0, 35);
		}
		uploadMap.put("BENE_BANK_1", Bank_Name.trim());
		uploadMap.put("BENE_BANK_2", transferOversea.getBeneficiaryBank2());
		uploadMap.put("BENE_BANK_3", transferOversea.getBeneficiaryBank3());
		uploadMap.put("BENE_BANK_4", transferOversea.getBeneficiaryBank4());
		uploadMap.put("BENE_CITY", City_Name.trim());
		uploadMap.put("BIC_SWIFT", transferOversea.getSwiftAddress());
		uploadMap.put("CONTACT_PHONE_NO", "");
		uploadMap.put("USER_ID", transferOversea.getUserId());
		uploadMap.put("CORPORATION_ID", transferOversea.getCorpId());
		uploadMap.put("REMIT_TRANS_REF", transferOversea.getTransId()
				.substring(2));
		uploadMap.put("STATUS", fromHost.get("RESPONSE_CODE"));
		uploadMap.put("REJECT_CODE", fromHost.get("REJECT_CODE"));
		
		//add by long_zg 2015-01-28 for CR205 Add IP
		uploadMap.put("IP_ADDRESS", fromHost.get("IP_ADDRESS"));
		
		UploadReporter.write("RP_BENTFRNREG", uploadMap);

	}

	public void uploadEnquiryBANK(TransferBank transferBank, Map fromHost)
			throws NTBException {
		Map uploadMap = new HashMap();
		uploadMap.put("TRANS_DATE", CibTransClient.getCurrentDate());
		uploadMap.put("TRANS_TIME ", CibTransClient.getCurrentTime());
		uploadMap.put("TRANS_CODE", "ZJ53");
		uploadMap.put("TRANS_REF_NO", fromHost.get("TELLER_ID").toString()
				+ fromHost.get("UNIQUE_SEQUENCE_NO").toString());
		uploadMap.put("REMITTING_AMOUNT", transferBank.getTransferAmount());
		uploadMap.put("PN_SIGN", "+");
		uploadMap.put("COUNTRY_CODE", "MO");
		uploadMap.put("BANK_CODE", "");
		uploadMap.put("CGD_FLAG", "N");
		uploadMap.put("TRANS_FROM_CCY", transferBank.getFromCurrency());
		uploadMap.put("TRANS_TO_CCY", transferBank.getToCurrency());
		uploadMap.put("USER_ID", transferBank.getUserId());
		uploadMap.put("CORPORATION_ID", transferBank.getCorpId());
		uploadMap.put("STATUS", fromHost.get("RESPONSE_CODE"));
		uploadMap.put("REJECT_CODE", fromHost.get("REJECT_CODE"));
		UploadReporter.write("RP_RMTCHRENQ", uploadMap);

	}

	public void uploadEnquiryMacau(TransferMacau transferMacau, Map fromHost)
			throws NTBException {
		Map uploadMap = new HashMap();
		uploadMap.put("TRANS_DATE", CibTransClient.getCurrentDate());
		uploadMap.put("TRANS_TIME ", CibTransClient.getCurrentTime());
		uploadMap.put("TRANS_CODE", "ZJ53");
		uploadMap.put("TRANS_REF_NO", fromHost.get("TELLER_ID").toString()
				+ fromHost.get("UNIQUE_SEQUENCE_NO").toString());
		uploadMap.put("REMITTING_AMOUNT", transferMacau.getTransferAmount());
		uploadMap.put("PN_SIGN", "+");
		uploadMap.put("COUNTRY_CODE", "MO");
		uploadMap.put("BANK_CODE", transferMacau.getBeneficiaryBank());
		uploadMap.put("CGD_FLAG", "N");
		uploadMap.put("TRANS_FROM_CCY", transferMacau.getFromCurrency());
		uploadMap.put("TRANS_TO_CCY", transferMacau.getToCurrency());
		uploadMap.put("USER_ID", transferMacau.getUserId());
		uploadMap.put("CORPORATION_ID", transferMacau.getCorpId());
		uploadMap.put("STATUS", fromHost.get("RESPONSE_CODE"));
		uploadMap.put("REJECT_CODE", fromHost.get("REJECT_CODE"));

		UploadReporter.write("RP_RMTCHRENQ", uploadMap);

	}

	public void uploadEnquiryOversea(TransferOversea transferOversea,
			Map fromHost) throws NTBException {
		Map uploadMap = new HashMap();
		uploadMap.put("TRANS_DATE", CibTransClient.getCurrentDate());
		uploadMap.put("TRANS_TIME ", CibTransClient.getCurrentTime());
		uploadMap.put("TRANS_CODE", "ZJ53");
		uploadMap.put("TRANS_REF_NO", fromHost.get("TELLER_ID").toString()
				+ fromHost.get("UNIQUE_SEQUENCE_NO").toString());
		uploadMap.put("REMITTING_AMOUNT", transferOversea.getTransferAmount());
		uploadMap.put("PN_SIGN", "+");
		uploadMap.put("COUNTRY_CODE", transferOversea.getBeneficiaryCountry());
		uploadMap.put("BANK_CODE", transferOversea.getBeneficiaryBank1());
		uploadMap.put("CGD_FLAG", "N");
		uploadMap.put("TRANS_FROM_CCY", transferOversea.getFromCurrency());
		uploadMap.put("TRANS_TO_CCY", transferOversea.getFromCurrency());
		uploadMap.put("USER_ID", transferOversea.getUserId());
		uploadMap.put("CORPORATION_ID", transferOversea.getCorpId());
		uploadMap.put("STATUS", fromHost.get("RESPONSE_CODE"));
		uploadMap.put("REJECT_CODE", fromHost.get("REJECT_CODE"));

		UploadReporter.write("RP_RMTCHRENQ", uploadMap);

	}

	public TransferBank editBANK(TransferBank transferBank, String userID)
			throws NTBException {
		if (transferBank != null) {
			if ((transferBank.getUserId().equals(userID))) {
				transferDao.update(transferBank);
			} else {
				throw new NTBException("err.txn.UserIdIsInvalid");
			}
		}
		return transferBank;
	}

	public TransferMacau editMacau(TransferMacau transferMacau, String userID)
			throws NTBException {
		if (transferMacau != null) {
			if ((transferMacau.getUserId().equals(userID))) {
				transferDao.update(transferMacau);
			} else {
				throw new NTBException("err.txn.UserIdIsInvalid");
			}
		}
		return transferMacau;
	}

	public TransferOversea editOversea(TransferOversea transferOversea,
			String userID) throws NTBException {
		if (transferOversea != null) {

			if ((transferOversea.getUserId().equals(userID))) {
				transferDao.update(transferOversea);
			} else {
				throw new NTBException("err.txn.UserIdIsInvalid");
			}
		}
		return transferOversea;
	}

	public TransferOversea templateInAccOverseas(TransferOversea pojoTransfer)
			throws NTBException {
		if (pojoTransfer != null) {
			pojoTransfer.setRecordType(TransferOversea.TRANSFER_TYPE_TEMPLATE);
			transferDao.add(pojoTransfer);
		} else {
			throw new NTBException("err.txn.pojoTransferIsNull");
		}
		return pojoTransfer;
	}

	public TransferBank templateInBANK1to1(TransferBank pojoTransfer)
			throws NTBException {
		if (pojoTransfer != null) {
			pojoTransfer.setRecordType(TransferBank.TRANSFER_TYPE_TEMPLATE);
			transferDao.add(pojoTransfer);
		} else {
			throw new NTBException("err.txn.pojoTransferIsNull");
		}
		return pojoTransfer;
	}

	public TransferMacau templateInMacau(TransferMacau pojoTransfer)
			throws NTBException {
		if (pojoTransfer != null) {
			pojoTransfer.setRecordType(TransferMacau.TRANSFER_TYPE_TEMPLATE);
			transferDao.add(pojoTransfer);
		} else {
			throw new NTBException("err.txn.pojoTransferIsNull");
		}
		return pojoTransfer;
	}

	public TransferBank updateBANK(TransferBank transferBank) throws NTBException {
		if (transferBank != null) {
			transferDao.update(transferBank);
		}
		return transferBank;
	}

	public TransferMacau updateMacau(TransferMacau transferMacau)
			throws NTBException {
		if (transferMacau != null) {
			transferDao.update(transferMacau);
		}
		return transferMacau;
	}

	public TransferOversea updateOversea(TransferOversea transferOversea)
			throws NTBException {
		if (transferOversea != null) {
			transferDao.update(transferOversea);
		}
		return transferOversea;
	}

	public String loadCgdMacau(String bank) throws NTBException {
		HashMap cdtMap = new HashMap();
		String CGD = null;
		try {
			List idList = null;
			idList = genericJdbcDao.query(SELECT_CGDMACAU,
					new Object[] { bank });
			cdtMap = (HashMap) idList.get(0);
			CGD = (String) cdtMap.get("CGD_FLAG");
			return CGD;

		} catch (Exception e) {
			throw new NTBException("err.txn.GetCGDException");
		}

	}

	public List fileRequestHistory(String batchType, Date dateFrom,
			Date dateTo, String corpID, String userID, String fromAccount)
			throws NTBException {
		List historyList = null;
		historyList = transferDao.listFileRequest(batchType, dateFrom, dateTo,
				corpID, userID, fromAccount);
		return historyList;
	}

	// add by Peng Haisen 2009-10-10 for CR103
	public List fileRequestHistoryForRequestHis(String batchType,
			Date dateFrom, Date dateTo, String corpID, String groupId,
			String fromAccount) throws NTBException {

		// String sql =
		// "Select tb.* from FILE_REQUEST as tb,CORP_USER as t2 where
		// tb.BATCH_TYPE= ? and tb.CORP_ID=? and tb.USER_ID=t2.USER_ID and
		// t2.GROUP_ID=? and tb.FROM_ACCOUNT = ? order by tb.REQUEST_TIME DESC";

		try {
			List historyList = null;

			List valueList = new ArrayList();
			//mod by linrui for oracle
			//String sql = "Select tb.* from FILE_REQUEST as tb,CORP_USER as t2 where tb.USER_ID=t2.USER_ID and tb.BATCH_TYPE= ? ";			
			String sql = "Select tb.* from FILE_REQUEST tb,CORP_USER t2 where tb.USER_ID=t2.USER_ID and tb.BATCH_TYPE= ? ";
			valueList.add(batchType);

			if ((dateFrom != null) && (!dateFrom.equals(""))) {
				sql += "and tb.REQUEST_TIME >= ? ";
				valueList.add(dateFrom);
			}

			if ((dateTo != null) && (!dateTo.equals(""))) {
				sql += "and tb.REQUEST_TIME <= ? ";
				valueList.add(dateTo);
			}

			if ((corpID != null) && (!corpID.equals(""))) {
				sql += "and  tb.CORP_ID = ? ";
				valueList.add(corpID);
			}
			if ((groupId != null) && (!groupId.equals(""))) {
				sql += "and  t2.GROUP_ID = ? ";
				valueList.add(groupId);
			}
			if ((fromAccount != null) && (!fromAccount.equals(""))
					&& (!fromAccount.equals("0"))) {
				sql += "and  tb.FROM_ACCOUNT = ? ";
				valueList.add(fromAccount);
			}
			historyList = genericJdbcDao.query(sql, valueList.toArray());
			return historyList;
		} catch (Exception e) {
			Log
					.error(
							"TransferServiceImpl.fileRequestHistoryForRequestHis ERROR :",
							e);
			throw new NTBException("err.sys.DBError");
		}

	}

	public List fileRequestHistoryMtS(String batchType, Date dateFrom,
			Date dateTo, String corpID, String userID, String toAccount)
			throws NTBException {
		List historyList = null;
		historyList = transferDao.listFileRequestMtS(batchType, dateFrom,
				dateTo, corpID, userID, toAccount);
		return historyList;
	}

	public List listTransferBankMtsByBatchid(String batchId) throws NTBException {
		List valueList = new ArrayList();
		if ((batchId != null) && (!batchId.equals(""))) {
			valueList = transferDao.listTransferBankStm(batchId);
		} else {
			throw new NTBException("err.txn.BatchIdIsNullOrEmpty");
		}
		return valueList;
	}

	public List listTransferBankStmByBatchid(String batchId) throws NTBException {
		List valueList = new ArrayList();
		if ((batchId != null) && (!batchId.equals(""))) {
			valueList = transferDao.listTransferBankStm(batchId);
		} else {
			throw new NTBException("err.txn.BatchIdIsNullOrEmpty");
		}
		return valueList;
	}

	public List listTransferMacauStmByBatchid(String batchId)
			throws NTBException {
		List valueList = new ArrayList();
		if ((batchId != null) && (!batchId.equals(""))) {
			valueList = transferDao.listTransferMacauStm(batchId);
		} else {
			throw new NTBException("err.txn.BatchIdIsNullOrEmpty");
		}
		return valueList;
	}

	public List listTransferOverseaStmByBatchid(String batchId)
			throws NTBException {
		List valueList = new ArrayList();
		if ((batchId != null) && (!batchId.equals(""))) {
			valueList = transferDao.listTransferOverseaStm(batchId);
		} else {
			throw new NTBException("err.txn.BatchIdIsNullOrEmpty");
		}
		return valueList;
	}

	public String loadSpbankLabel(String countryCode) throws NTBException {
		HashMap map = new HashMap();
		String spbankLabel = "#";
		try {
			List idList = null;
			idList = genericJdbcDao.query(SELECT_SPBANK_LABAEL,
					new Object[] { countryCode });
			if (null != idList && idList.size() > 0) {
				map = (HashMap) idList.get(0);
				spbankLabel = (String) map.get("BANK_CODE_LABEL");
				return spbankLabel;
			} else {
				// 闁跨喐鏋婚幏鐑芥晸鐟欐帟顕滈幏鐑芥晸閺傘倖瀚规稉娲晸閺傘倖瀚�
				return spbankLabel;
			}

		} catch (Exception e) {
			throw new NTBException("err.txn.GetSpbankLabelException");
		}

	}

	// add by mxl 闁跨喐鏋婚幏鐑芥晸缁叉count 闁跨喐鏋婚幏锟絟s_transfacton_code闁跨喐鏋婚幏閿嬆佸蹇涙晸閺傘倖瀚归柨鐔虹lpha1,alpha2闁跨喕顢滅拋瑙勫
	private String getAlphiField(String desc, String account)
			throws NTBException {
		desc = Utils.null2EmptyWithTrim(desc);
		account = Utils.null2EmptyWithTrim(account);
		int index = 0;
		String alpha = null;
		String[] strs = desc.split(" ");
		// 闁跨喐褰粵瑙勫闁跨喕顢滈崙銈嗗0闁跨喐鏋婚幏鐑芥晸閼哄倻顣幏铚傜秴闁跨喐鏋婚幏锟�
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].substring(0, 1).equals("0")) {
				index = i;
			}
		}
		// account = Utils.prefixZero(account, strs[index].length());
		// 闁跨喐鏋婚幏鐑芥晸缁茬垔dex闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐕傛嫹
		if (index == 0) {
			alpha = strs[1];
			for (int i = 2; i < strs.length; i++) {
				alpha = alpha + " " + strs[i];
			}

			alpha = account + " " + alpha;
		} else if (index == strs.length - 1) {
			alpha = strs[0];
			for (int i = 1; i < strs.length - 1; i++) {
				alpha = alpha + " " + strs[i];
			}

			alpha = alpha + " " + account;
		} else {
			alpha = strs[0];
			for (int i = 1; i < index - 1; i++) {
				alpha = alpha + " " + strs[i];
			}
			alpha = alpha + " " + account;
			for (int i = index + 1; i < strs.length; i++) {
				alpha = alpha + " " + strs[i];
			}
		}

		return alpha;
	}

	public String loadAlphal(String transCode) throws NTBException {
		HashMap map = new HashMap();
		String alpha = null;
		try {
			List idList = null;
			idList = genericJdbcDao.query(SELECT_ALPHA,
					new Object[] { transCode });
			if (null != idList && idList.size() > 0) {
				map = (HashMap) idList.get(0);
				alpha = (String) map.get("ALPHA2");
				return alpha;
			}
		} catch (Exception e) {
			throw new NTBException("err.txn.GetAlphaException");
		}
		return null;
	}

	/**
	 * added by xyf 20090721 check the transfer country to find optional or
	 * mandatory flag
	 */
	public String getAddrMandatoryFlagOversea(String countryCode, String lang)
			throws NTBException {
		List idList = null;
		HashMap map = new HashMap();
		String addrMandatoryFlag = null;
		String sql = "select ADDR_MANDATORY_FLAG from HS_COUNTRY_CODE where COUNTRY_CODE=? and LOCAL_CODE=? ";

		try {
			idList = genericJdbcDao.query(sql,
					new Object[] { countryCode, lang });
			if (null != idList && idList.size() > 0) {
				map = (HashMap) idList.get(0);
				addrMandatoryFlag = (String) map.get("ADDR_MANDATORY_FLAG");
				return addrMandatoryFlag;
			}
		} catch (Exception e) {
			throw new NTBException("err.txn.getAddrMandatoryFlagOversea");
		}
		return null;
	}

	/**
	 * Add by heyongjiang 20100806 Need to provide purpose, return 1; Need to
	 * provide proof document, return 2闁跨喐鏋婚幏锟紼lse return 0.
	 * 
	 * update by liuwei 20100921 闁跨喐鏋婚幏绋皉ovide Proof闁跨喐鏋婚幏锟絘mount with the selected
	 * country 闁跨喐鏋婚幏绋ception Amount闁跨喐鏋婚幏锟絝rom the exception list use the higher amount
	 * as the one to compare with the customer transfer amount
	 * 
	 * @param cifNo
	 *            Customer no.
	 * @param accountNo
	 *            Account no.
	 * @param transferAmount
	 *            Transfer amount.
	 * @param debitAmount
	 *            Debit amount
	 * @param fromCurrency
	 *            From currency
	 * @param toCurrency
	 *            To cuerrency
	 * @param toCountryCode
	 *            To country code
	 * @return int
	 * @throws NTBException
	 */
	public int checkNeedPurpose(String corpId, String accountNo,
			String transferAmount, String debitAmount, String fromCurrency,
			String toCurrency, String toCountryCode) throws NTBException {
		// TODO Auto-generated method stub
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		String sql = "select PURPOSE_AMOUNT, PURPOSE_PROOF_AMOUNT from HS_COUNTRY_CODE where COUNTRY_CODE = ? ";
		String sqlTemp = "select Exception_Amount from HS_PURPOSE_PROOF_EXCEPTION where CIF = ?";

		List purposeAmountList = null;
		List exceptionAmountList = null;
		Map map = new HashMap();
		Map emap = new HashMap();
		Double purposeAmount = null;
		Double proofAmount = null;
		Double exceptionAmount = null;

		try {
			purposeAmountList = genericJdbcDao.query(sql,
					new Object[] { toCountryCode });
			exceptionAmountList = genericJdbcDao.query(sqlTemp,
					new Object[] { this.getCifNoByCorpId(corpId) });
			if (null != exceptionAmountList && exceptionAmountList.size() > 0) {
				emap = (Map) exceptionAmountList.get(0);
				String eamt = Utils.null2Empty(emap.get("EXCEPTION_AMOUNT"));
				if (eamt != null && !"".equals(eamt)) {
					exceptionAmount = new Double(eamt);
				}
			}
			if (null != purposeAmountList && purposeAmountList.size() > 0) {
				map = (Map) purposeAmountList.get(0);
				String pamt = Utils.null2Empty(map.get("PURPOSE_AMOUNT"));
				String famt = Utils.null2Empty(map.get("PURPOSE_PROOF_AMOUNT"));
				if (pamt != null && !"".equals(pamt)) {
					purposeAmount = new Double(pamt);
				}
				if (famt != null && !"".equals(famt)) {
					proofAmount = new Double(famt);
				}
				if(purposeAmount.doubleValue() == 0.0 && proofAmount.doubleValue() == 0){
					return 2;
				}
			}
		} catch (Exception e) {
			Log.error(e);
			throw new NTBException("err.txn.GetPurposeAmountException");
		}

		if (purposeAmount != null && proofAmount != null) {
			if (transferAmount != null && !"".equals(transferAmount)
					&& Double.valueOf(transferAmount).doubleValue() > 0) {
				BigDecimal equivalentMOP = exRatesService
						.getEquivalentForCheckPurpose(corpId, toCurrency,
								"MOP", new BigDecimal(transferAmount), null, 2);
				Double equAmt = new Double(equivalentMOP.toString());
				if (equAmt.doubleValue() >= purposeAmount.doubleValue()
						&& equAmt.doubleValue() < proofAmount.doubleValue()) {
					return 1;
				} else if (equAmt.doubleValue() >= proofAmount.doubleValue()) {
					if (exceptionAmount != null && !"".equals(exceptionAmount)
							&& exceptionAmount.doubleValue() > 0) {
						if ((equAmt.doubleValue() >= proofAmount.doubleValue() && proofAmount
								.doubleValue() > exceptionAmount.doubleValue())
								|| (equAmt.doubleValue() >= exceptionAmount
										.doubleValue() && exceptionAmount
										.doubleValue() > proofAmount
										.doubleValue())
								|| (proofAmount.doubleValue() == exceptionAmount
										.doubleValue())) {
							return 2;
						} else {
							return 1;
						}
					} else {
						return 2;
					}
				} else {
					return 0;
				}
			} else {
				BigDecimal equivalentMOP = exRatesService
						.getEquivalentForCheckPurpose(corpId, fromCurrency,
								"MOP", new BigDecimal(debitAmount), null, 2);
				Double equAmt = new Double(equivalentMOP.toString());
				if (equAmt.doubleValue() >= purposeAmount.doubleValue()
						&& equAmt.doubleValue() < proofAmount.doubleValue()) {
					return 1;
				} else if (equAmt.doubleValue() >= proofAmount.doubleValue()) {
					if (exceptionAmount != null && !"".equals(exceptionAmount)
							&& exceptionAmount.doubleValue() > 0) {
						if ((equAmt.doubleValue() >= proofAmount.doubleValue() && proofAmount
								.doubleValue() > exceptionAmount.doubleValue())
								|| (equAmt.doubleValue() >= exceptionAmount
										.doubleValue() && exceptionAmount
										.doubleValue() > proofAmount
										.doubleValue())
								|| (proofAmount.doubleValue() == exceptionAmount
										.doubleValue())) {
							return 2;
						} else {
							return 1;
						}
					} else {
						return 2;
					}
				} else {
					return 0;
				}
			}
		}
		return 0;
	}

	/**
	 * Get purpose description by purpose code.
	 * 
	 * @param purposeCode
	 *            Purpose code
	 * @return String Purpose description
	 * @throws NTBEXception
	 */
	/*
	public String getPurposeDescription(String purposeCode) throws NTBException {
		// TODO Auto-generated method stub
		String sql = "select DESCRIPTION from HS_PURPOSE_DESCRIPTION where CODE = ? and LANGUAGE_CODE = 'E'";
		String purposeDescription = null;
		String purposeCodeStr = null;
		try {
			List purposeList = genericJdbcDao.query(sql,
					new Object[] { purposeCode });
			if (purposeList != null && purposeList.size() > 0) {
				Map purpose = (Map) purposeList.get(0);
				purposeDescription = Utils.null2Empty(purpose
						.get("DESCRIPTION"));
			}
		} catch (Exception e) {
			Log.error(e);
			throw new NTBException("err.txn.GetPurposeDescriptionException");
		}
		return purposeDescription;
	}// Add by heyongjiang end
	*/
	/**
	 * get cifNo by corpId
	 * 
	 * @param corpId
	 * @return
	 * @throws NTBException
	 */
	/*public String getCifNoByCorpId(String corpId) throws NTBException {
		String sql = "select CIF_NO from HS_CORPORATE_INFO where CORP_ID = ?";
		String cifNo = null;
		try {
			List cifNoList = genericJdbcDao.query(sql, new Object[] { corpId });
			if (cifNoList != null && cifNoList.size() > 0) {
				Map cifNoMap = (Map) cifNoList.get(0);
				cifNo = Utils.null2Empty(cifNoMap.get("CIF_NO"));
			}
		} catch (Exception e) {
			Log.error(e);
			throw new NTBException("err.txn.GetCIFNoException");
		}
		return cifNo;
	}*/
	public String getCifNoByCorpId(String corpId) throws NTBException {
		if(!("".equals(corpId) || null == corpId)){
			return corpId.substring(1);
		}
		return "";
	}
	
	/**
	 * 20130118 by wency
	 * get list for checking duplicated input
	 */
	public List listTransferEntity4CheckDuplicatedInput(String transferType)throws NTBException {
		
		String sql = "select t.* from TRANSFER_BNU t,FLOW_PROCESS f where (to_char(t.REQUEST_TIME,'yyyyMMdd')=? and f.TRANS_NO=t.TRANS_ID) " 
			+ " or (f.TRANS_NO=t.TRANS_ID and to_char(f.PROC_FINISH_TIME,'yyyyMMdd')=? and f.PROC_STATUS='"+FlowEngineService.PROCESS_STATUS_FINISH+"')";
		
		if("M".equals(transferType)){
			sql = "select t.* from TRANSFER_MACAU t,FLOW_PROCESS f where (to_char(t.REQUEST_TIME,'yyyyMMdd')=? and f.TRANS_NO=t.TRANS_ID) " 
			+ " or (f.TRANS_NO=t.TRANS_ID and to_char(f.PROC_FINISH_TIME,'yyyyMMdd')=? and f.PROC_STATUS='"+FlowEngineService.PROCESS_STATUS_FINISH+"')";
		}else if("O".equals(transferType)){
			sql = "select t.* from TRANSFER_OVERSEA t,FLOW_PROCESS f where (to_char(t.REQUEST_TIME,'yyyyMMdd')=? and f.TRANS_NO=t.TRANS_ID) " 
			+ " or (f.TRANS_NO=t.TRANS_ID and to_char(f.PROC_FINISH_TIME,'yyyyMMdd')=? and f.PROC_STATUS='"+FlowEngineService.PROCESS_STATUS_FINISH+"')";
		}
		
		String today = DateTime.formatDate(new Date(), "yyyyMMdd");
		List list = null;
		try {
			list = genericJdbcDao.query(sql, new Object[] { today ,today });
			if (list == null) {
				list = new ArrayList();
			}
		} catch (Exception e) {
			Log.error(e);
			//throw new NTBException("err.txn.GetCIFNoException");
		}
		return list;
	}
	//add by wcc 20190402
	//modified by lzg 20190510
	public String getBenName(String corpId)throws NTBException {
		Map map = new HashMap();
		String benName = null;
			List list = null;
			map = getCusInfo(corpId);
			benName = ((String) map.get("CUSTOMER_NAME")).trim();
			return benName;

	}
	//modified by lzg end
	
	//add by lzg 20190510
	private Map getCusInfo(String cifNo) throws NTBException {
		  Map toHost = new HashMap();
		  Map fromHost = new HashMap();
		  CibTransClient testClient = new CibTransClient("CIB", "ZB01");
		  toHost.put("CIF_NO",cifNo.substring(1));
		  fromHost = testClient.doTransaction(toHost);
		  if (!testClient.isSucceed()) {
		   throw new NTBHostException(testClient.getErrorArray());
		  }
		  return fromHost;
		 }
	//add by lzg end
	//add by linrui 20190524
	public Map viewCurrentDetail(String accountNo) throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		CibTransClient testClient = new CibTransClient("CIB", "0195");

		toHost.put("ACCOUNT_NO", accountNo);
		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		return fromHost;
	}
	//end
	//add by linrui for bank trial 20190823 CR0001-600
	public Map toHostInBankTrial(TransferBank transferBank, CorpUser user) throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();

		RcCurrency rcCurrency = new RcCurrency();

		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpTransferService corpTransferService = (CorpTransferService) appContext
				.getBean("CorpTransferService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");

		CibTransClient testClient = new CibTransClient("CIB", "51CD");
		String fromAcctypeCode = null;
		String toAcctypeCode = null;

		String fromCurrencyCode = rcCurrency.getCbsNumberByCcy(transferBank
				.getFromCurrency());
		String toCurrencyCode = rcCurrency.getCbsNumberByCcy(transferBank
				.getToCurrency());


		String inputAmountFlag = transferBank.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferBank.getFromCurrency();
			transferAmount = transferBank.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferBank.getToCurrency();
			transferAmount = transferBank.getTransferAmount();
		}
		BigDecimal equivalentMOP = exRatesService.getEquivalent(user
				.getCorpId(), currency, "MOP", new BigDecimal(transferAmount
				.toString()), null, 2);
		String corpIdLimit = null;
		
		BigDecimal passbookBalance;
		BigDecimal exchangeIn;
		BigDecimal exchangeOut;
		passbookBalance = testClient.getPassbookBalance(transferBank
				.getFromCurrency(), transferBank.getToCurrency(),
				new BigDecimal(transferBank.getTransferAmount().toString()));

		exchangeIn = testClient.getExchangeIn(transferBank.getFromCurrency(),
				transferBank.getToCurrency(), new BigDecimal(transferBank
						.getTransferAmount().toString()));

		exchangeOut = testClient.getExchangeOut(transferBank.getFromCurrency(),
				transferBank.getToCurrency(), new BigDecimal(transferBank
						.getDebitAmount().toString()));

		LceBean lceInfo = exRatesService.getLceInfo(transferBank
				.getFromCurrency(), transferBank.getToCurrency(),
				new BigDecimal(transferBank.getDebitAmount().toString()),
				new BigDecimal(transferBank.getTransferAmount().toString()));
		BigDecimal fromAmtLCE = lceInfo.getFromAmtLCE();
		BigDecimal fromExchangeRate = lceInfo.getFromRateLCE();
		BigDecimal toAmtLCE = lceInfo.getToAmtLCE();
		BigDecimal toExchangeRate = lceInfo.getToRateLCE();
		toHost.put("EFFECTIVE_DATE", CibTransClient.getCurrentDate());
		toHost.put("ACCOUNT_NO", transferBank.getFromAccount());
		toHost.put("TRANSFER_TO_ACC", transferBank.getToAccount());
		toHost.put("FROM_CCY", transferBank.getFromCurrency());
		toHost.put("TO_CCY", transferBank.getToCurrency());
		toHost.put("PASSBOOK_BALANCE", passbookBalance);
		toHost.put("TRANSACTION_AMOUNT", transferAmount);
		toHost.put("PRINCIPAL_AMOUNT", transferBank.getTransferAmount());
		toHost.put("INTEREST_AMOUNT", toAmtLCE);
		toHost.put("ESCROW_AMOUNT", toExchangeRate.multiply(new BigDecimal(
		"0")));
		toHost.put("LCE_OF_TRANS_AMOUNT", fromAmtLCE);
		toHost.put("FOREIGN_CCY_CODE", currency);
		toHost.put("FOREIGN_CCY_RATE", fromExchangeRate);
		toHost.put("CASHIER_NO", toCurrencyCode);
		toHost.put("EXCHANGE_IN_DR", exchangeIn);
		toHost.put("EXCHANGE_OUT_CR", exchangeOut);
		toHost.put("CLIENT_REF", transferBank.getRemark());
		
		toHost.put("CHECK_FULLDAY_CURRENCY", transferBank.getFromCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_FROM", transferBank.getFromCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_TO", transferBank.getToCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_FLAG", "B") ;
		
		toHost.put("REQ_REF", CibIdGenerator.getTDRef());
		toHost.put("TO_NAME", transferBank.getToName());

		testClient.setAlpha8_for51xx(user, CibTransClient.TXNNATURE_TRANSFER,
				CibTransClient.ACCTYPE_3RD_PARTY, transferBank.getTransId(),
				transferBank.getFlag());

		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		return fromHost;
	}
	//add by linrui 20190619 for EB_001_0097
	public Map toHostInMacauTrial(TransferMacau transferMacau, CorpUser user) throws NTBException {

		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		Map cdtMap = new HashMap();
		Map conditionMap = new HashMap();
		ApplicationContext appContext = Config.getAppContext();

		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");

		RcCurrency rcCurrency = new RcCurrency();

		String inputAmountFlag = transferMacau.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferMacau.getFromCurrency();
			transferAmount = transferMacau.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferMacau.getToCurrency();
			transferAmount = transferMacau.getTransferAmount();
		}

		CibTransClient testClient = new CibTransClient("CIB", "ZJ52");

		String CGD = null;
		String executeDate = null;
		String date = null;
		String exchangeRate = null;
		String BANK_NAME = null;
		String swiftCode = null;

		conditionMap.put("BANK_CODE", transferMacau.getBeneficiaryBank());
		GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");

		try {
			List idList = null;
			idList = dao.query(SELECT_CGDLOCAL, new Object[] { transferMacau
					.getBeneficiaryBank() });
			cdtMap = (Map) idList.get(0);
			CGD = (String) cdtMap.get("CGD_FLAG");
			BANK_NAME = (String) cdtMap.get("BANK_NAME");
			swiftCode = (String) cdtMap.get("SWIFT_CODE");

		} catch (Exception e) {
			throw new NTBException("err.txn.GetCGDException");
		}
		toHost.put("CORP_ID", this.getCifNoByCorpId(user.getCorpId())); //update by gan 20180201 "8000010994");//
		toHost.put("FROM_CCY", transferMacau.getFromCurrency());	//update by gan 20180201
		toHost.put("TO_CCY", transferMacau.getToCurrency());	//update by gan 20180201
		toHost.put("FROM_AMT", transferMacau.getDebitAmount());
		toHost.put("TO_AMT", transferMacau.getTransferAmount());
		toHost.put("DEBIT_ACC_NO", transferMacau.getFromAccount());
		toHost.put("CHARGE_ACC_NO", transferMacau.getChargeAccount());
		toHost.put("COMMISSION_AMT", transferMacau.getCommissionAmount());
		toHost.put("TAX_AMT4COMM", transferMacau.getTaxAmount());
		fromHost = toHostChargeEnquiry(transferMacau.getTransferAmount(),transferMacau.getFromCurrency(),transferMacau.getToCurrency());
		toHost.put("HANDLING_CHRG_AMT", fromHost.get("HANDLING_CHRG_AMT"));
		toHost.put("HANDLING_FEE_AMT", fromHost.get("HANDLING_FEE_AMT"));
		toHost.put("SWIFT_CHRG_AMT", transferMacau.getSwiftAmount());
		toHost.put("OUR_CHG_AMT", transferMacau.getChargeOur());
		toHost.put("COUNTRY_CODE", "MO");
		toHost.put("CITY_CODE", " ");
		toHost.put("BANK_ID", Utils.prefixSpace(swiftCode, 11)); //update by gan 20180202
		toHost.put("BENE_NAME_LINE_1", transferMacau.getBeneficiaryName1());
		toHost.put("BENE_NAME_LINE_2", transferMacau.getBeneficiaryName2());
		toHost.put("BENE_NAME_LINE_3", " ");
		toHost.put("BENE_NAME_LINE_4", " ");
		toHost.put("BENE_ACCOUNT", transferMacau.getToAccount());
		toHost.put("BENE_BIC_SWIFT", swiftCode);
		toHost.put("BENE_BNK_LINE_1", BANK_NAME);
		toHost.put("BENE_BNK_LINE_2", " ");
		toHost.put("BENE_BNK_LINE_3", " ");
		toHost.put("BENE_BNK_LINE_4", " ");
		toHost.put("REQUEST_TYPE", transferMacau.getRequestType());
		/*if (transferMacau.getRequestType().equals("1")) {
			date = "0";
		}

		else if (transferMacau.getRequestType().equals("2")) {
			executeDate = transferMacau.getTransferDate().toString();
			date = DateTime.formatDate(executeDate, "yyyyMMdd");
		}*/
		date = "0";
		toHost.put("EXECUTION_DATE", date);
		toHost.put("CONTACT_NUMBER", " ");
		toHost.put("CGD", CGD);
		/*if (transferMacau.getExchangeRate().doubleValue() == 1.0) {
			exchangeRate = "0";
		} else {
			exchangeRate = transferMacau.getExchangeRate().toString();
		}
		toHost.put("EX_RATE_AMT", exchangeRate);*/
		toHost.put("BENE_BNK_CHRG_OPT", transferMacau.getChargeBy());
		toHost.put("MSG_LINE_1", transferMacau.getMesssage());
		toHost.put("MSG_LINE_2", transferMacau.getMesssage2());
		toHost.put("MSG_LINE_3", transferMacau.getMesssage3());
		toHost.put("MSG_LINE_4", " ");
		toHost.put("COUNTRY_BANK_CODE", "MO");
		toHost.put("COUNTRY_BENE_ACC_TYPE", "0");
		toHost.put("CLIENT_REF", transferMacau.getRemark());
		toHost.put("CHG_CCY", transferMacau.getFromCurrency());
		toHost.put("CORR_BNK_LINE_1", " ");
		toHost.put("CORR_BNK_LINE_2", " ");
		toHost.put("CORR_BNK_LINE_3", " ");
		toHost.put("CORR_BNK_LINE_4", " ");
		toHost.put("CORR_BNK_ACCOUNT", " ");
		String purposeCode = transferMacau.getPurposeCode();
		if(purposeCode != null){
			if(purposeCode.contains("C.4.5")){
				purposeCode = "C.4.5";
			}
		}
		toHost.put("PURPOSE_CODE", purposeCode);
		toHost.put("OTHER_PURPOSE", transferMacau.getOtherPurpose());
		toHost.put("PROOF_OF_PURPOSE", transferMacau.getProofOfPurpose());
		Log.info("to Host In Macau the purpose is:" + transferMacau.getOtherPurpose());
		toHost.put("IBAN", "N");
		toHost.put("CHECK_FULLDAY_CURRENCY", transferMacau.getToCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_FROM", transferMacau.getFromCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_TO", transferMacau.getToCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_FLAG", "O") ;

		String txSerNo = Utils.prefixZero(CibIdGenerator.getTDRef(),24);
		toHost.put("TXSERNO", txSerNo);
		String charge = transferMacau.getChargeBy().toString();
		if("O".equalsIgnoreCase(charge))
			charge = "1";
		else 
			charge = "S".equalsIgnoreCase(charge)?"2":"3";
		toHost.put("BANKXGE", charge);
		String changeFlag = "";
		if(!transferMacau.getToCurrency().equals(transferMacau.getFromCurrency())){
			changeFlag = transferMacau.getInputAmountFlag();
		}
		toHost.put("CHANGE_FLG", changeFlag);
		toHost.put("CHANGE_AMT", Constants.INPUT_AMOUNT_FLAG_TO.equalsIgnoreCase(transferMacau.getInputAmountFlag().toString())?
				                                      transferMacau.getTransferAmount():
				                                      transferMacau.getDebitAmount());
		
		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		return fromHost;

	}
	public Map toHostOverseasTrial(TransferOversea transferOversea, CorpUser user) throws NTBException {

		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		Map cdtMap = new HashMap();
		RcCurrency rcCurrency = new RcCurrency();

		CibTransClient testClient = new CibTransClient("CIB", "ZJ52");
		String CGD = "N";

		String swiftCode = null;
		String executeDate = null;
		String date = null;
		String exchangeRate = null;
		String Iban = null;
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		

		String fromCurrencyCode = rcCurrency.getCbsNumberByCcy(transferOversea.getFromCurrency());
		String toCurrencyCode = rcCurrency.getCbsNumberByCcy(transferOversea.getToCurrency());
		
		String inputAmountFlag = transferOversea.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferOversea.getFromCurrency();
			transferAmount = transferOversea.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferOversea.getToCurrency();
			transferAmount = transferOversea.getTransferAmount();
		}


		GenericJdbcDao dao = (GenericJdbcDao) appContext
				.getBean("genericJdbcDao");
		String Bank_Name = transferOversea.getBeneficiaryBank1();
		if (transferOversea.getBankFlag().equals("N")) {
			try {
				List idList = null;
				idList = dao.query(SELECT_CGDOVERSEA,
						new Object[] { transferOversea.getBeneficiaryBank1() });
				cdtMap = (Map) idList.get(0);
				if (null != idList && idList.size() > 0) {
					CGD = (String) cdtMap.get("CGD_FLAG");
					Bank_Name = (String) cdtMap.get("BANK_NAME");
					swiftCode = (String) cdtMap.get("SWIFT_CODE");
				} else {
					Bank_Name = transferOversea.getBeneficiaryBank1();
				}

			} catch (Exception e) {
				throw new NTBException("err.txn.GetCGDException");
			}
		}
		
		String City_Name = transferOversea.getBeneficiaryCity();
		if (transferOversea.getCityFlag().equals("N")) {
			try {
				List idList1 = null;
				idList1 = dao.query(SELECT_CITYNAME,
						new Object[] { transferOversea.getBeneficiaryCity() });
				if (null != idList1 && idList1.size() > 0) {
					cdtMap = (Map) idList1.get(0);
					City_Name = (String) cdtMap.get("CITY_NAME");
				} else {
					City_Name = transferOversea.getBeneficiaryCity();
				}

			} catch (Exception e) {
				throw new NTBException("err.txn.GetCityNameException");
			}
		}

		String ACC_LABEL_SEQ = null;
		try {
			List idList1 = null;
			idList1 = dao.query(SELECT_ACC_LABEL_SQL, new Object[] {
					transferOversea.getBeneficiaryCountry(),
					transferOversea.getAccType() });
			if (null != idList1 && idList1.size() > 0) {
				Map cdtMap1 = (Map) idList1.get(0);
				ACC_LABEL_SEQ = (String) cdtMap1.get("ACC_LABEL_SEQ");
			} else {
				ACC_LABEL_SEQ = "00";
			}

		} catch (Exception e) {
			throw new NTBException("err.txn.GetAccLabelException");
		}

		// add by mxl 0915
		if (Utils.null2EmptyWithTrim(transferOversea.getAccType()).equals(
				"IBAN")) {
			Iban = "Y";
		} else {
			Iban = "N";
		}

		toHost.put("CORP_ID",  this.getCifNoByCorpId(user.getCorpId())); //update by gan 20180205
		toHost.put("FROM_CCY", transferOversea.getFromCurrency()); //update by gan
		toHost.put("TO_CCY", transferOversea.getToCurrency()); // update by gan
		toHost.put("FROM_AMT", transferOversea.getDebitAmount());
		toHost.put("TO_AMT", transferOversea.getTransferAmount());
		toHost.put("DEBIT_ACC_NO", transferOversea.getFromAccount());
		toHost.put("CHARGE_ACC_NO", transferOversea.getChargeAccount());
		toHost.put("COMMISSION_AMT", transferOversea.getCommissionAmount());
		toHost.put("TAX_AMT4COMM", transferOversea.getTaxAmount());
		
		fromHost = toHostChargeEnquiry(transferOversea.getTransferAmount(),transferOversea.getFromCurrency(),transferOversea.getToCurrency());
		
		toHost.put("HANDLING_CHRG_AMT", fromHost.get("HANDLING_CHRG_AMT"));
		toHost.put("HANDLING_FEE_AMT", fromHost.get("HANDLING_FEE_AMT"));
		//add by lzg 0601
		toHost.put("SWIFT_CHRG_AMT", transferOversea.getSwiftAmount());
		toHost.put("OUR_CHG_AMT", transferOversea.getChargeOur());
		toHost.put("COUNTRY_CODE", transferOversea.getBeneficiaryCountry());
		if (transferOversea.getCityFlag().equals("Y")) {
			toHost.put("CITY_CODE", " ");

		} else if (transferOversea.getCityFlag().equals("N")) {
			if(transferOversea.getBeneficiaryCity().equals("OTH")){
				toHost.put("CITY_CODE", "");
			}
			toHost.put("CITY_CODE", transferOversea.getBeneficiaryCity());

		}
		
		toHost.put("BANK_ID", transferOversea.getSwiftAddress()); //mod by linrui 20190523

		toHost.put("BENE_NAME_LINE_1", transferOversea.getBeneficiaryName1());
		toHost.put("BENE_NAME_LINE_2", transferOversea.getBeneficiaryName2());
		
		toHost.put("BENE_NAME_LINE_3", "");//transferOversea.getBeneficiaryName3()
		toHost.put("BENE_NAME_LINE_4", transferOversea.getBeneficiaryName4());
		// ---------------end of xyf----------------
		toHost.put("BENE_ACCOUNT", transferOversea.getToAccount());
		toHost.put("BENE_BIC_SWIFT", transferOversea.getSwiftAddress());
		toHost.put("BENE_BNK_LINE_1", Bank_Name);
		toHost.put("BENE_BNK_LINE_2", transferOversea.getBeneficiaryBank2());
		toHost.put("BENE_BNK_LINE_3", transferOversea.getBeneficiaryBank3());
		toHost.put("BENE_BNK_LINE_4", " ");
		toHost.put("REQUEST_TYPE", transferOversea.getRequestType());
		toHost.put("BENE_BNK_CITY", City_Name);
		/*if (transferOversea.getRequestType().equals("1")) {
			date = "0";
		}

		else if (transferOversea.getRequestType().equals("2")) {
			executeDate = transferOversea.getTransferDate().toString();
			date = DateTime.formatDate(executeDate, "yyyyMMdd");
		}*/
		date = "0";
		toHost.put("EXECUTION_DATE", date);
		toHost.put("CONTACT_NUMBER", " ");
		toHost.put("CGD", CGD);
		/*if (transferOversea.getExchangeRate().doubleValue() == 1.0) {
			exchangeRate = "0";
		} else {
			exchangeRate = transferOversea.getExchangeRate().toString();

		}*/
		//toHost.put("EX_RATE_AMT", exchangeRate);
		toHost.put("BENE_BNK_CHRG_OPT", transferOversea.getChareBy());
		toHost.put("MSG_LINE_1", transferOversea.getMesssage());
		toHost.put("MSG_LINE_2", transferOversea.getMesssage2());
		toHost.put("MSG_LINE_3", " ");
		toHost.put("MSG_LINE_4", " ");
		toHost.put("COUNTRY_BANK_CODE", transferOversea.getSpbankCode());
		toHost.put("COUNTRY_BENE_ACC_TYPE", ACC_LABEL_SEQ);
		toHost.put("CLIENT_REF", transferOversea.getRemark());
		toHost.put("CHG_CCY", transferOversea.getFromCurrency());
		String purposeCode = transferOversea.getPurposeCode();
		if(purposeCode != null){
			if(purposeCode.contains("C.4.5")){
				purposeCode = "C.4.5";
			}
		}
		toHost.put("PURPOSE_CODE", purposeCode);
		toHost.put("OTHER_PURPOSE", transferOversea.getOtherPurpose());
		toHost.put("PROOF_OF_PURPOSE", transferOversea.getProofOfPurpose());
		Log.info("to Host In Oversea the purpose is:" + transferOversea.getOtherPurpose());

		toHost.put("IBAN", Iban);
		
		toHost.put("CHECK_FULLDAY_CURRENCY", transferOversea.getToCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_FROM", transferOversea.getFromCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_TO", transferOversea.getToCurrency()) ;
		toHost.put("CHECK_FULLDAY_CURRENCY_FLAG", "O") ;

		String txSerNo = Utils.prefixZero(CibIdGenerator.getTDRef(),24);
		toHost.put("TXSERNO", txSerNo);
		String charge = transferOversea.getChareBy().toString();
		if("O".equalsIgnoreCase(charge))
			charge = "1";
		else 
			charge = "S".equalsIgnoreCase(charge)?"2":"3";
		toHost.put("BANKXGE", charge);
		String changeFlag = "";
		if(!transferOversea.getToCurrency().equals(transferOversea.getFromCurrency())){
			changeFlag = transferOversea.getInputAmountFlag();
		}
		toHost.put("CHANGE_FLG", changeFlag);
		toHost.put("CHANGE_AMT", Constants.INPUT_AMOUNT_FLAG_TO.equalsIgnoreCase(transferOversea.getInputAmountFlag().toString())?
				                                      transferOversea.getTransferAmount():
				                                      transferOversea.getDebitAmount());

		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		
		return fromHost;
	}
	//end

	public void checkConvertibility(String fromCurrency, String toCurrency)
			throws NTBException {
		if((fromCurrency.equals("CNY") && !toCurrency.equals("CNY")) || (!fromCurrency.equals("CNY") && toCurrency.equals("CNY"))){
			throw new NTBException("err.bnk.NotAllowedCNY");
		}
		
	}
	//add by linrui for mul-language 20190729
	public List loadOversea(String country, String city, String action,
			String lang) throws NTBException {

		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext
				.getBean("genericJdbcDao");
		try {
			List idList = null;
			if (action.equals("country")) {
				idList = dao.query(SELECT_CITY, new Object[] { country,lang});
			} else if (action.equals("city")) {
				idList = dao.query(SELECT_BANK, new Object[] { country,lang });// mod by linrui 20190729
				System.out.println(idList);
			}
			return idList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List getCountryList(Locale lang) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext
				.getBean("genericJdbcDao");
		try {
			List idList = dao.query(SELECT_COUNTRY, new Object[] {Format.transferLang(lang.toString())});
			return idList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void removeTransfer(String tableName,String transId){
		genericJdbcDao.removeTransfer(tableName,transId);
	}
	
	
}
