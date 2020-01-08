/**
 *
 */
package app.cib.service.txn;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.context.ApplicationContext;

import app.cib.bo.enq.LceBean;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TimeDeposit;
import app.cib.core.CibIdGenerator;
import app.cib.core.CibTransClient;
import app.cib.dao.enq.ExchangeRatesDao;
import app.cib.dao.sys.CorpAccountDao;
import app.cib.dao.sys.CorpPermissionDao;
import app.cib.dao.txn.TimeDepositDao;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.sys.CorpAccountService;
import app.cib.util.Constants;
import app.cib.util.RcCurrency;
import app.cib.util.UploadReporter;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.utils.*;

/**
 * @author hjs 2006-07-18
 */
public class TimeDepositServiceImpl implements TimeDepositService {
	/**
	 * @param args
	 */
	private TimeDepositDao timeDepositDao;

	private CorpAccountDao corpAccountDao;
	
	private CorpPermissionDao corpPermissionDao;

	private GenericJdbcDao genericJdbcDao;
	//private static String SELECT_ACCT = " SELECT DISTINCT ACCOUNT_NO FROM CORP_ACCOUNT WHERE CORP_ID=? AND ACCOUNT_TYPE NOT IN ('LN','TD')";
	private static String SELECT_ACCT = " SELECT DISTINCT ACCOUNT_NO FROM CORP_ACCOUNT WHERE CORP_ID=? AND ACCOUNT_TYPE NOT IN ('LN','TD')  AND CURRENCY not in (select a.ccy_code from available_currencies a where available_flag = 'N')";
	/*private static String SELECT_ACCT = " SELECT ACCOUNT_NO, CURRENCY, ACCOUNT_NAME, ACCOUNT_DESC, ACCOUNT_TYPE, CORP_ID, STATUS, (ACCOUNT_NO || ' - ' || CURRENCY) AS ACCOUNT_INFO FROM CORP_ACCOUNT " +
	"  WHERE CORP_ID=? AND ACCOUNT_TYPE NOT IN ('LN','TD')";*/

	public TimeDepositDao getTimeDepositDao() {
		return timeDepositDao;
	}

	public void setTimeDepositDao(TimeDepositDao timeDepositDao) {
		this.timeDepositDao = timeDepositDao;
	}

	public CorpAccountDao getCorpAccountDao() {
		return corpAccountDao;
	}

	public void setCorpAccountDao(CorpAccountDao corpAccountDao) {
		this.corpAccountDao = corpAccountDao;
	}

	public CorpPermissionDao getCorpPermissionDao() {
		return corpPermissionDao;
	}

	public void setCorpPermissionDao(CorpPermissionDao corpPermissionDao) {
		this.corpPermissionDao = corpPermissionDao;
	}

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	public static void main(String[] args) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see app.cib.service.txn.TDAccountService#addTiemDeposit(app.cib.bo.txn.TimeDeposit)
	 */
	public void addTiemDeposit(TimeDeposit pojoTimeDeposit) throws NTBException {
		if (pojoTimeDeposit != null) {
			timeDepositDao.add(pojoTimeDeposit);
		} else {
			throw new NTBException("err.txn.TimeDepositPojoIsNull");
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see app.cib.service.txn.TDAccountService#viewTiemDeposit(java.lang.String)
	 */
	public TimeDeposit viewTimeDeposit(String transID) throws NTBException {
		TimeDeposit timeDeposit = null;
		if ((transID != null) && (!transID.equals(""))) {
			timeDeposit = (TimeDeposit) timeDepositDao.load(TimeDeposit.class,
					transID);
		} else {
			throw new NTBException("err.txn.TransIDIsNullOrEmpty");
		}
		return timeDeposit;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see app.cib.service.txn.TDAccountService#withdrawTiemDeposit(java.lang.String)
	 */
	public void withdrawTiemDeposit(TimeDeposit pojoTimeDeposit) throws NTBException {
		if (pojoTimeDeposit != null) {
			timeDepositDao.add(pojoTimeDeposit);
		}else {
			throw new NTBException("err.txn.TimeDepositPojoIsNull");
		}
	}

	public List listTimeDeposit(String corpID, String userID) throws NTBException {
		return null;
	}

	public Map queryNewTDInfo(CorpUser corpUser, String debitAccount, String amout, String currency, String period, String accountCcy, String productNo) throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        CorpAccountService corpAccService = (CorpAccountService)appContext.getBean("CorpAccountService");

		/*String accType = Utils.null2EmptyWithTrim(corpAccService.getAccountType(debitAccount,accountCcy));
		if (accType.equals(CorpAccount.ACCOUNT_TYPE_CURRENT)
				|| accType.equals(CorpAccount.ACCOUNT_TYPE_OVER_DRAFT)) {
			accType = "6";
		} else if (accType.equals(CorpAccount.ACCOUNT_TYPE_SAVING)) {
			accType = "1";
		}*/
//		String productNo = this.getProductNoFronDB(currency, tinor2pe(period));
//		String productNo = this.getProductNoFronDB(currency, period);

		//send to host
    	Map toHost = new HashMap();
        Map fromHost = new HashMap();
        CibTransClient testClient = new CibTransClient("CIB", "ZJ16");
        String effectiveDate = CibTransClient.getCurrentDate();

        toHost.put("PRODUCT_NO", productNo);
//        toHost.put("ACCOUNT_TYPE1", accType);
        toHost.put("ACCOUNT_TYPE2", "0");
        toHost.put("ACCOUNT_TYPE3", "0");
        toHost.put("TA_ACC_NO_1", debitAccount);
        toHost.put("TA_ACC_NO_2", "0000000000");
        toHost.put("TA_ACC_NO_3", "0000000000");
        toHost.put("AMOUNT", amout);
        toHost.put("EFFECTIVE_DATE",effectiveDate);
        //add by linrui for test HST
//        toHost.put("PERIOD", peToTinor(period));
        toHost.put("PERIOD", period);
        toHost.put("CCY", currency);
        //end
        String refNo = CibIdGenerator.getRefNoForTransaction();
        testClient.setAlpha8(corpUser, CibTransClient.APPCODE_TIME_DEPOSIT, CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);

        //receive from host
        fromHost = testClient.doTransaction(toHost);
        if(!testClient.isSucceed()){
        	throw new NTBHostException(testClient.getErrorArray());
        }
		return fromHost;
	}

	public Map queryWithdrawTDInfo(CorpUser corpUser, String tdAccount, 
			Date valueDate, Date maturityDate,
			String ccyCBS, BigDecimal exchangeRate, String productNo) throws NTBException {

			//send to host
	    	Map toHost = new HashMap();
	        Map fromHost = new HashMap();
	        CibTransClient testClient = null;
	        
	        //閿熷彨璁规嫹331N
	        productNo = Utils.null2EmptyWithTrim(productNo);
	        /*if (productNo.equals("001") || productNo.equals("002") 
	        		|| productNo.equals("300") || productNo.equals("302")) {
	        	
		        if (new Date().getTime() - valueDate.getTime() > 0) {
		        	testClient = new CibTransClient("CIB", "331I");
		        } else {
		        	testClient = new CibTransClient("CIB", "332I");
		        }
		        
	        } else {
	        	
		        if (new Date().getTime() - maturityDate.getTime() >= 0) {
		        	testClient = new CibTransClient("CIB", "331I");
		        } else {
		        	testClient = new CibTransClient("CIB", "332I");
		        }
	        }*/
	        testClient = new CibTransClient("CIB", "332I");//add by linrui 20190326
	        String effectiveDate = CibTransClient.getCurrentDate();

	        toHost.put("EFFECTIVE_DATE", effectiveDate);
	        toHost.put("ACCOUNT_NO", tdAccount);
	        toHost.put("CURRENCY_CODE", ccyCBS);
	        toHost.put("EXCHANGE_RATE", exchangeRate);

	        String refNo = CibIdGenerator.getRefNoForTransaction();
	        testClient.setAlpha8(corpUser, CibTransClient.APPCODE_TIME_DEPOSIT, CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);

	        //receive from host
	        fromHost = testClient.doTransaction(toHost);
	        if(!testClient.isSucceed()){
	        	throw new NTBHostException(testClient.getErrorArray());
	        }
			return fromHost;
	}

	/*add by gan 20180122*/
	public Map queryWithdrawInfoByHost(CorpUser corpUser, String ccy,
			String principal, Date valueDate, Date maturityDate, String period, String productNo)
			throws NTBException {
		//send to host
    	Map toHost = new HashMap();
        Map fromHost = new HashMap();
        CibTransClient testClient = null;
        
        testClient = new CibTransClient("CIB", "331I");
	       
        String effectiveDate = CibTransClient.getCurrentDate();

        /*if( null != valueDate)
            toHost.put("EFFECTIVE_DATE", valueDate);
        if( null != maturityDate)
            toHost.put("MT_DATE", maturityDate);*/
        toHost.put("PRODUCT_NO", productNo);
        toHost.put("PERIOD", period);
        toHost.put("CURRENCY_CODE", ccy);
        toHost.put("PRINCIPAL", principal);

        String refNo = CibIdGenerator.getRefNoForTransaction();
        testClient.setAlpha8(corpUser, CibTransClient.APPCODE_TIME_DEPOSIT, CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);

        //receive from host
        fromHost = testClient.doTransaction(toHost);
        if(!testClient.isSucceed()){
        	throw new NTBHostException(testClient.getErrorArray());
        }
		return fromHost;
	}

	
	public String approveNewTDAccout(TimeDeposit timeDeposit, CorpUser corpUser, BigDecimal equivalentMOP) throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        CorpAccountService corpAccService = (CorpAccountService)appContext.getBean("CorpAccountService");
        ExchangeRatesService exRatesService = (ExchangeRatesService)appContext.getBean("ExchangeRatesService");
        TransferLimitService transferLimitService = (TransferLimitService)appContext.getBean("TransferLimitService");

		if (timeDeposit == null) {
			throw new NTBException("err.txn.NoSuchTransId");
		} else {
			String tdType = Utils.null2EmptyWithTrim(timeDeposit.getTdType());
			if (tdType.equals(TimeDeposit.TD_TYPE_NEW)) {
				timeDeposit.setStatus(Constants.STATUS_NORMAL);
				timeDeposit.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
				timeDeposit.setExecuteTime(new Date());
				timeDepositDao.update(timeDeposit);

				RcCurrency rcCcy = new RcCurrency();

//				String fromCcy = corpAccService.getCurrency(timeDeposit.getCurrentAccount(),true);
				String fromCcy = timeDeposit.getCurrentAccountCcy();//add by linrui for mul-ccy
				
				// exchange rate
				Map exRateMap = exRatesService.getExchangeRate(timeDeposit.getCorpId(), fromCcy, timeDeposit.getCurrency(), 7);
				String rateType = exRateMap.get(ExchangeRatesDao.RATE_TYPE).toString();
				BigDecimal exRate = null;
				BigDecimal fromAmt = null;
				if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
					throw new NTBException("err.txn.NoSuchExchangeRate");
					
				} else if (rateType.equals(ExchangeRatesDao.RATE_TYPE_BUY_RATE)) {
					exRate = new BigDecimal(exRateMap.get(ExchangeRatesDao.BUY_RATE).toString());
					fromAmt = new BigDecimal(timeDeposit.getPrincipal().toString()).divide(exRate, 2, BigDecimal.ROUND_HALF_UP);	
				
				} else if (rateType.equals(ExchangeRatesDao.RATE_TYPE_SELL_RATE)) {
					exRate = new BigDecimal(exRateMap.get(ExchangeRatesDao.SELL_RATE).toString());	
					fromAmt = new BigDecimal(timeDeposit.getPrincipal().toString()).multiply(exRate);				
				
				} else if (rateType.equals(ExchangeRatesDao.RATE_TYPE_ALL_RATE)) {
					exRate = new BigDecimal(exRateMap.get(ExchangeRatesDao.SELL_RATE).toString())
					.divide(new BigDecimal(exRateMap.get(ExchangeRatesDao.BUY_RATE).toString()), 7, BigDecimal.ROUND_HALF_UP);
					fromAmt = new BigDecimal(timeDeposit.getPrincipal().toString()).multiply(exRate);
					
				}
				fromAmt = fromAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
				/*
				//from exchange rate
				BigDecimal fromExRate = null;
				Map fromExRateMap = exRatesService.getExchangeRate(timeDeposit.getCorpId(), fromCcy, "MOP", 7);
				String fromRateType = fromExRateMap.get(ExchangeRatesDao.RATE_TYPE).toString();
				if (fromRateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
					throw new NTBException("err.txn.NoSuchExchangeRate");
				} else {
					fromExRate = new BigDecimal(fromExRateMap.get(ExchangeRatesDao.BUY_RATE).toString());
				}
				
				//to exchange rate
				BigDecimal toExRate = null;
				Map toExRateMap = exRatesService.getExchangeRate(timeDeposit.getCorpId(), "MOP", timeDeposit.getCurrency(), 7);
				String toRateType = toExRateMap.get(ExchangeRatesDao.RATE_TYPE).toString();
				if (toRateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
					throw new NTBException("err.txn.NoSuchExchangeRate");
				} else if (toRateType.equals(ExchangeRatesDao.RATE_TYPE_SELL_RATE)) {
					toExRate = new BigDecimal(toExRateMap.get(ExchangeRatesDao.SELL_RATE).toString());	
				}
				*/
				// get LCE info
				LceBean lceInfo = exRatesService.getLceInfo(fromCcy, timeDeposit.getCurrency(), fromAmt, new BigDecimal(timeDeposit.getPrincipal().toString()));
				BigDecimal fromAmtLCE = lceInfo.getFromAmtLCE();
				BigDecimal fromRateLCE = lceInfo.getFromRateLCE();
				BigDecimal toAmtLCE = lceInfo.getToAmtLCE();
				BigDecimal toRateLCE = lceInfo.getToRateLCE();
				
				// add Limit
				transferLimitService.addUsedLimitQuota(timeDeposit.getCurrentAccount(), corpUser.getCorpId(), 
						Constants.TXN_TYPE_TIME_DEPOSIT,
						fromAmt.doubleValue(), equivalentMOP.doubleValue());

				//send to host
		    	Map toHost = new HashMap();
		        Map fromHost = new HashMap();
		        CibTransClient testClient = new CibTransClient("CIB", "ZJ37");
		        String effectiveDate = CibTransClient.getCurrentDate();

		        toHost.put("TRANS_CODE_1", "51CG");
		        toHost.put("EFFECTIVE_DATE1", effectiveDate);
		        toHost.put("ACC_NO", timeDeposit.getCurrentAccount());
		        toHost.put("TRANSFER_TO_ACC", "1902010");
		        toHost.put("PASSBOOK_BALANCE", testClient.getPassbookBalance(fromCcy, timeDeposit.getCurrency(), new BigDecimal(timeDeposit.getPrincipal().toString())));
		        toHost.put("TRANS_AMT", fromAmt);
		        toHost.put("PRINCIPAL_AMT1", timeDeposit.getPrincipal());
		        toHost.put("INTEREST_AMT", toAmtLCE);
		        toHost.put("ESCROW_AMT", toRateLCE.multiply(new BigDecimal("100000")));
		        toHost.put("LCE_OF_TRANS_AMT", fromAmtLCE);
		        toHost.put("FOREIGN_CCY_CODE", fromCcy);
		        toHost.put("FOREIGN_CCY_RATE", fromRateLCE);
		        toHost.put("COST_CENTRE", "00000441");
		        toHost.put("CASHIER_NO", rcCcy.getCbsNumberByCcy(timeDeposit.getCurrency()));
//		        toHost.put("PERIOD", peToTinor(timeDeposit.getTerm()));//add by linrui 20180112
		        toHost.put("PERIOD", timeDeposit.getTerm());//mod by linrui 20190323
		        toHost.put("REQ_REF", CibIdGenerator.getTDRef());//add by linrui 20180112
		        toHost.put("ALPHA_1", "EBK NEW TIME DEPOSIT");
		        toHost.put("ALPHA_2", "EBK NEW TIME DEPOSIT");
		        toHost.put("EXCHANGE_IN_DR", testClient.getExchangeIn(fromCcy, timeDeposit.getCurrency(), new BigDecimal(timeDeposit.getPrincipal().toString())));
		        toHost.put("EXCHANGE_OUT_CR", testClient.getExchangeOut(fromCcy, timeDeposit.getCurrency(), fromAmt));
		        testClient.setAlpha8(corpUser, CibTransClient.APPCODE_TIME_DEPOSIT, CibTransClient.ACCTYPE_OWNER_ACCOUNT, timeDeposit.getTransId(), "1");

		        toHost.put("TRANS_CODE_2", "ZJ18");
		        toHost.put("OPEN_DATE", effectiveDate);
		        //occurs 6 times * 10A each
		        //update by linrui get CIF_NO
		        toHost.put("CIF_NO_1", (timeDeposit.getCifNo1().trim()!=null&&!"".equals(timeDeposit.getCifNo1().trim()))?
		        		timeDeposit.getCifNo1().substring(0, 10):getCifByAcct(timeDeposit.getCurrentAccount()));//update by linrui 20180112
		        toHost.put("RELATIONSHIP1", "SOW");
		        toHost.put("CIF_NO_2", "");
		        toHost.put("RELATIONSHIP2", "");
		        toHost.put("CIF_NO_3", "");
		        toHost.put("RELATIONSHIP3", "");
		        toHost.put("PRINCIPAL_AMT2", timeDeposit.getPrincipal());
		        toHost.put("MATURITY_DATE", DateTime.formatDate(timeDeposit.getMaturityDate(), "yyyyMMdd"));
		        toHost.put("EFFECTIVE_DATE2", effectiveDate);
//		        toHost.put("PRODUCT_TYPE", getProductNoFronDB(fromCcy,timeDeposit.getTerm()));//"TD/R    " timeDeposit.getProductionNo());閺嗗倹妞傛穱顔芥暭
		        toHost.put("PRODUCT_TYPE", timeDeposit.getProductionNo());//"TD/R    " timeDeposit.getProductionNo());閺嗗倹妞傛穱顔芥暭
		        toHost.put("OFFICER_CODE", "409");
		        toHost.put("BRANCH_CODE", "409");
		        toHost.put("SHORT_NAME", timeDeposit.getShortName().substring(0,18));
		        toHost.put("VARIANCE_RATE", "0");
		        toHost.put("ACCRUAL_DATE", DateTime.formatDate(toHost.get("OPEN_DATE").toString(), "yyyyMMdd", "ddMMyy"));
		        toHost.put("FUND_AVA_DATE", DateTime.formatDate(toHost.get("OPEN_DATE").toString(), "yyyyMMdd", "ddMMyy"));
		        toHost.put("PERSONAL_FLAG", "N");
		        toHost.put("TITLE_MODIFIER", "");
		        toHost.put("TIME_INTS_RATE", timeDeposit.getDefaultRate());
		        toHost.put("INST_CD", timeDeposit.getInstCd());//add by linrui for add Maturity_Instruction 20190424
		        testClient.setAlpha8(corpUser, CibTransClient.APPCODE_TIME_DEPOSIT, CibTransClient.ACCTYPE_OWNER_ACCOUNT, timeDeposit.getTransId(), "2");
		        //receive from host
		        fromHost = testClient.doTransaction(toHost);

		        //write rp
				Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("TRANS_CODE", toHost.get("TRANSACTION_CODE"));
				reportMap.put("CORRECTION_FLAG", " ");
				reportMap.put("TRANS_REF_NO", Utils.null2EmptyWithTrim(toHost.get("TELLER_NO")) + Utils.null2EmptyWithTrim(toHost.get("UNIQUE_SEQUENCE")));
				reportMap.put("DEBIT_ACCOUNT", timeDeposit.getCurrentAccount());
				reportMap.put("DEBIT_AMOUNT", fromAmt);
				reportMap.put("DEBIT_PN_SIGN ", "+");
				reportMap.put("DEBIT_ACC_CCY ", fromCcy);
				reportMap.put("TD_ACC_NO ", fromHost.get("ACCOUNT_NO"));
				reportMap.put("TD_CURRENCY ", timeDeposit.getCurrency());
				reportMap.put("TD_AMOUNT ", timeDeposit.getPrincipal());
				reportMap.put("TD_PN_SIGN ", "+");
				reportMap.put("EXCHANGE_RATE ", exRate);
				reportMap.put("PN_SIGN ", "+");
				reportMap.put("PERIOD ", timeDeposit.getTerm());
				reportMap.put("PRODUCT_NO ", timeDeposit.getProductionNo());
				reportMap.put("INTEREST_RATE ", timeDeposit.getDefaultRate());
				reportMap.put("INTEREST_PN_SIGN ", "+");
				reportMap.put("START_DATE ", DateTime.formatDate(timeDeposit.getValueDate(), "yyyyMMdd"));
				reportMap.put("MATURITY_DATE ", DateTime.formatDate(timeDeposit.getMaturityDate(), "yyyyMMdd"));
				reportMap.put("USER_ID ", timeDeposit.getUserId());
				reportMap.put("CIF ",  (timeDeposit.getCifNo1().substring(0, 10)!=null||!"".equals(timeDeposit.getCifNo1().substring(0, 10)))?
		        		timeDeposit.getCifNo1().substring(0, 10):getCifByAcct(timeDeposit.getCurrentAccount()));//update by linrui 20180112
				reportMap.put("STATUS ", fromHost.get("RESPONSE_CODE"));
//				reportMap.put("REJECT_CODE ", fromHost.get("REJECT_CODE"));	no reject_code    20180113
				UploadReporter.write("RP_TMPLACE", reportMap); 

		        if(!testClient.isSucceed()){
		        	throw new NTBHostException(testClient.getErrorArray());
		        }
		        
		        String tdAccount = fromHost.get("ACCOUNT_NO").toString();
		        timeDeposit.setTdAccount(tdAccount);
		        this.timeDepositDao.update(timeDeposit);
		        //add to corp_account table
		        CorpAccount corpAccount = new CorpAccount(tdAccount);
		        corpAccount.setAccountType(CorpAccount.ACCOUNT_TYPE_TIME_DEPOSIT);
		        corpAccount.setCurrency(timeDeposit.getCurrency());
		        corpAccount.setPrefId(getAvailablePrefId(corpUser.getCorpId()));
		        corpAccount.setCorpId(corpUser.getCorpId());
		        //corpAccount.setAccountName("New Time Deposit");
		        corpAccount.setOperation(Constants.OPERATION_NEW);
		        corpAccount.setStatus(Constants.STATUS_NORMAL);
		        corpAccount.setCifNo(getCifByAcct(timeDeposit.getCurrentAccount()));//add by linrui 20180409
		        corpAccountDao.add(corpAccount);//mod by linrui for oracle
//		        Map corp_Acct_Map = new HashMap();
//		        corp_Acct_Map.put("ACCOUNT_NO", tdAccount);
//		        corp_Acct_Map.put("ACCOUNT_TYPE", CorpAccount.ACCOUNT_TYPE_TIME_DEPOSIT);
//		        corp_Acct_Map.put("CURRENCY", timeDeposit.getCurrency());
//		        corp_Acct_Map.put("PREF_ID", getAvailablePrefId(corpUser.getCorpId()));
//		        corp_Acct_Map.put("CORP_ID", corpUser.getCorpId());
//		        corp_Acct_Map.put("OPERATION", Constants.OPERATION_NEW);
//		        corp_Acct_Map.put("STATUS", Constants.STATUS_NORMAL);
//		        try {
//					genericJdbcDao.query("insert into ", valueObjArray);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				//end
		        
		        return fromHost.get("ACCOUNT_NO").toString();
			} else {
				throw new NTBException("err.txn.ApproveNewTDAccoutException");
			}
		}
	}

	public void approveWithdrawTDAccout(TimeDeposit timeDeposit, CorpUser corpUser) throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        ExchangeRatesService exRatesService = (ExchangeRatesService)appContext.getBean("ExchangeRatesService");
        CorpAccountService corpAccService = (CorpAccountService)appContext.getBean("CorpAccountService");

		if (timeDeposit == null) {
			throw new NTBException("err.txn.NoSuchTransId");
		} else {
			String tdAccount = timeDeposit.getTdAccount();//add by linrui 20190326
			String tdType = Utils.null2EmptyWithTrim(timeDeposit.getTdType());
			if (tdType.equals(TimeDeposit.TD_TYPE_WITHDRAW)) {
				timeDeposit.setStatus(Constants.STATUS_NORMAL);
				timeDeposit.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
				timeDeposit.setExecuteTime(new Date());
				timeDepositDao.update(timeDeposit);

		        RcCurrency rcCcy = new RcCurrency();

		        double interest = timeDeposit.getInterest().doubleValue();
		        //update by gan 20180123
		        //double penalty = timeDeposit.getPenalty().doubleValue();
		        /*double penalty = 0.0;
		        double netInterestAmt = (interest - penalty > 0) ? interest - penalty : 0;*/
		        BigDecimal fromAmt = new BigDecimal(String.valueOf(timeDeposit.getPrincipal().doubleValue() /*+ netInterestAmt*/));//modify by wen_chy 20100322
		        
//				String toCcy = corpAccService.getCurrency(timeDeposit.getCurrentAccount(),true);
				String toCcy = timeDeposit.getCurrentAccountCcy();//mod by linrui for mul-ccy
				
				// exchange rate
				Map exRateMap = exRatesService.getExchangeRate(timeDeposit.getCorpId(), timeDeposit.getCurrency(), toCcy, 7);
				String rateType = exRateMap.get(ExchangeRatesDao.RATE_TYPE).toString();
				BigDecimal exRate = null;
				BigDecimal toAmt = null;
				if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
					throw new NTBException("err.txn.NoSuchExchangeRate");
					
				} else if (rateType.equals(ExchangeRatesDao.RATE_TYPE_BUY_RATE)) {
					exRate = new BigDecimal(exRateMap.get(ExchangeRatesDao.BUY_RATE).toString());
					toAmt = fromAmt.multiply(exRate);
					
				} else if (rateType.equals(ExchangeRatesDao.RATE_TYPE_SELL_RATE)) {
					exRate = new BigDecimal(exRateMap.get(ExchangeRatesDao.SELL_RATE).toString());
					toAmt = fromAmt.divide(exRate, 2, BigDecimal.ROUND_HALF_UP);
					
				} else if (rateType.equals(ExchangeRatesDao.RATE_TYPE_ALL_RATE)) {
					exRate = new BigDecimal(exRateMap.get(ExchangeRatesDao.BUY_RATE).toString())
					.divide(new BigDecimal(exRateMap.get(ExchangeRatesDao.SELL_RATE).toString()), 7, BigDecimal.ROUND_HALF_UP);
					toAmt = fromAmt.multiply(exRate);
					
				}
				toAmt = toAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
				/*
				//from exchange rate
				BigDecimal fromExRate = null;
				Map fromExRateMap = exRatesService.getExchangeRate(timeDeposit.getCorpId(), timeDeposit.getCurrency(), "MOP", 7);
				String fromRateType = fromExRateMap.get(ExchangeRatesDao.RATE_TYPE).toString();
				if (fromRateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
					throw new NTBException("err.txn.NoSuchExchangeRate");
				} else {
					fromExRate = new BigDecimal(fromExRateMap.get(ExchangeRatesDao.BUY_RATE).toString());
				}
				
				//to exchange rate
				BigDecimal toExRate = null;
				Map toExRateMap = exRatesService.getExchangeRate(timeDeposit.getCorpId(), "MOP", toCcy, 7);
				String toRateType = toExRateMap.get(ExchangeRatesDao.RATE_TYPE).toString();
				if (toRateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
					throw new NTBException("err.txn.NoSuchExchangeRate");
				} else if (toRateType.equals(ExchangeRatesDao.RATE_TYPE_SELL_RATE)) {
					toExRate = new BigDecimal(toExRateMap.get(ExchangeRatesDao.SELL_RATE).toString());	
				}
				*/
				// get LCE info
				LceBean lceInfo = exRatesService.getLceInfo(timeDeposit.getCurrency(), toCcy, fromAmt, toAmt);
				BigDecimal fromAmtLCE = lceInfo.getFromAmtLCE();
				BigDecimal fromRateLCE = lceInfo.getFromRateLCE();
				BigDecimal toAmtLCE = lceInfo.getToAmtLCE();
				BigDecimal toRateLCE = lceInfo.getToRateLCE();

		        //String accType = corpAccService.getAccountType(timeDeposit.getCurrentAccount(),timeDeposit.getCurrency());
				/*if (accType.equals(CorpAccount.ACCOUNT_TYPE_SAVING)) {
					accType = "S";
				} else {
					accType = "C";
				}*/

				//send to host
		    	Map toHost = new HashMap();
		    	Map fromHost = new HashMap();
		        CibTransClient testClient = new CibTransClient("CIB", "ZJ38");
		        String effectiveDate = CibTransClient.getCurrentDate();
		        
		        //閿熷彨璁规嫹331N
		        String productNo = Utils.null2EmptyWithTrim(timeDeposit.getProductionNo());
		        if (productNo.equals("001") || productNo.equals("002") 
		        		|| productNo.equals("300") || productNo.equals("302")) {
		        	
			        if (new Date().getTime() - timeDeposit.getValueDate().getTime() > 0) {
			        	toHost.put("TRANS_CODE_1", "331N");
			        } else {
			        	toHost.put("TRANS_CODE_1", "332N");
			        }
			        
		        } else {

			        if (new Date().getTime() - timeDeposit.getMaturityDate().getTime() >= 0) {
				        toHost.put("TRANS_CODE_1", "331N");
			        } else {
				        toHost.put("TRANS_CODE_1", "332N");
			        }
		        }
		        toHost.put("EFFECTIVE_DATE1", effectiveDate);
		        toHost.put("ACC_NO1", tdAccount);
		        toHost.put("TRANS_AMT1", fromAmt);
		        toHost.put("PENALTY_FROM_PRINCIPAL", new Double(0/*penalty*/));
		        toHost.put("AMT_FROM_PRINCIPAL", "0");
		        toHost.put("LCY_4_TRANS_AMT1", fromAmtLCE);
//		        toHost.put("CURR1", rcCcy.getCbsNumberByCcy(timeDeposit.getCurrency()));
		        toHost.put("EXCHANGE_RATE1", fromRateLCE);
		        testClient.setAlpha8(corpUser, CibTransClient.APPCODE_TIME_DEPOSIT, CibTransClient.ACCTYPE_OWNER_ACCOUNT, timeDeposit.getTransId(), "1");

		        toHost.put("TRANS_CODE_2", "0783");
		        toHost.put("EFFECTIVE_DATE2", effectiveDate);
		        toHost.put("ACC_NO2", "1902010");
		        toHost.put("TRANS_AMT2", fromAmt);
		        toHost.put("LCY_4_TRANS_AMT2", fromAmtLCE);
//		        toHost.put("CURR2", timeDeposit.getCurrency());
		        toHost.put("EXCHANGE_RATE2", fromRateLCE);
		        toHost.put("SERIAL_NO", "0");
		        toHost.put("REFERENCE_NO", "000441");
		        toHost.put("ALPHA_1_1", "EBK TIME RDM " + tdAccount);
		        testClient.setAlpha8(corpUser, CibTransClient.APPCODE_TIME_DEPOSIT, CibTransClient.ACCTYPE_OWNER_ACCOUNT, timeDeposit.getTransId(), "2");

		        //toHost.put("TRANS_CODE_3", "51G" + accType);
		        toHost.put("EFFECTIVE_DATE3", effectiveDate);
		        toHost.put("ACC_NO3", "1902010");
		        toHost.put("TRANSFER_2_ACC", timeDeposit.getCurrentAccount());
		        toHost.put("PASSBOOK_BALANCE", testClient.getPassbookBalance(timeDeposit.getCurrency(), toCcy, toAmt));
		        toHost.put("TRANS_AMT3", fromAmt);
		        toHost.put("PRINCIPAL_AMT", toAmt);
		        toHost.put("INTEREST_AMT", toAmtLCE);
		        toHost.put("ESCROW_AMT", toRateLCE.multiply(new BigDecimal("100000")));
		        toHost.put("LCE_OF_TRANS_AMT", fromAmtLCE);
//		        toHost.put("FOREIGN_CCY_CODE", timeDeposit.getCurrency());
		        toHost.put("FOREIGN_CCY_RATE", fromRateLCE);
		        toHost.put("CASHIER_NO", rcCcy.getCbsNumberByCcy(toCcy));
		        toHost.put("COST_CENTRE", "000441");
		        toHost.put("ALPHA_1_2", "EBK TIME RDM " + tdAccount);
		        toHost.put("ALPHA_2", "EBK TIME RDM " + tdAccount);
		        toHost.put("EXCHANGE_IN_DR", testClient.getExchangeIn(timeDeposit.getCurrency(), toCcy, toAmt));
		        toHost.put("EXCHANGE_OUT_CR", testClient.getExchangeOut(timeDeposit.getCurrency(), toCcy, fromAmt));
		        testClient.setAlpha8(corpUser, CibTransClient.APPCODE_TIME_DEPOSIT, CibTransClient.ACCTYPE_OWNER_ACCOUNT, timeDeposit.getTransId(), "3");

				//update by gan 20180129
				toHost.put("REQ_REF", CibIdGenerator.getTDRef());
		        //receive from host
				
		        fromHost = testClient.doTransaction(toHost);

		        //write rp
				Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
				reportMap.put("TRANS_CODE", toHost.get("TRANSACTION_CODE"));
				reportMap.put("CORRECTION_FLAG", " ");
				reportMap.put("TRANS_REF_NO", Utils.null2EmptyWithTrim(toHost.get("TELLER_NO")) + Utils.null2EmptyWithTrim(toHost.get("UNIQUE_SEQUENCE")));
				reportMap.put("TD_ACC_NO ", timeDeposit.getTdAccount());
				reportMap.put("TIME_CCY ", timeDeposit.getCurrency());
				reportMap.put("PRINCIPAL ", timeDeposit.getPrincipal());
				reportMap.put("PRINCIPAL_PN_SIGN ", "+");
				reportMap.put("MATURITY_DATE ", DateTime.formatDate(timeDeposit.getMaturityDate(), "yyyyMMdd"));
				reportMap.put("INTEREST_AMT ", timeDeposit.getInterest());
				reportMap.put("INTEREST_PN_SIGN ", "+");
				reportMap.put("PENALTY_AMT ", toAmt);
				reportMap.put("PENALTY_PN_SIGN ", "+");
				reportMap.put("CREDIT_ACCOUNT ", timeDeposit.getCurrentAccount());
				reportMap.put("CREDIT_AMOUNT ", toAmt);
				reportMap.put("CREDIT_AMT_CCY ", toCcy);
				reportMap.put("EXCHANGE_RATE ", exRate);
				reportMap.put("PN_SIGN ", "+");
				reportMap.put("USER_ID ", timeDeposit.getUserId());
				reportMap.put("CORPORATION_ID ", timeDeposit.getCorpId());
				reportMap.put("STATUS ", fromHost.get("RESPONSE_CODE"));
				reportMap.put("REJECT_CODE ", fromHost.get("REJECT_CODE"));
				UploadReporter.write("RP_TMREDEEM", reportMap); 
				
		        //閿熸枻鎷烽敓鏂ゆ嫹鎾為敓鏂ゆ嫹鏅掗敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓锟�
		        if(!testClient.isSucceed()){
		        	throw new NTBHostException(testClient.getErrorArray());
		        }

		        /*
		        //set corp account
		        CorpAccount corpAccount = (CorpAccount) this.corpAccountDao.load(CorpAccount.class, timeDeposit.getTdAccount());
		        if (corpAccount == null) {
		        	throw new NTBException("err.txn.NoSuchAccount");
		        }
		        corpAccount.setStatus(Constants.STATUS_THOROUGH_REMOVED);
		        this.corpAccountDao.update(corpAccount);*/
				//update by linrui 20190326
		        corpPermissionDao.delAccForDefaultGrp(corpUser.getCorpId(), tdAccount);
				if(isTimedepositAccountExist(tdAccount)){
					updateAcctStatus(tdAccount,Constants.STATUS_THOROUGH_REMOVED);
				}

			} else {
				throw new NTBException("err.txn.ApproveWithdrawTDAccoutException");
			}
		}
	}

	public void rejectNewTDAccout(String transId, CorpUser corpUser) throws NTBException {
		TimeDeposit timeDeposit = this.viewTimeDeposit(transId);
		if (timeDeposit == null) {
			throw new NTBException("err.txn.NoSuchTransId");
		} else {
			String tdType = Utils.null2EmptyWithTrim(timeDeposit.getTdType());
			if (tdType.equals(TimeDeposit.TD_TYPE_NEW)) {
				timeDeposit.setStatus(Constants.STATUS_REMOVED);
				timeDeposit.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
				timeDepositDao.update(timeDeposit);

			} else {
				throw new NTBException("err.txn.TdTypeException");
			}
		}

	}

	public void rejectWithdrawTDAccout(String transId, CorpUser corpUser) throws NTBException {
		TimeDeposit timeDeposit = this.viewTimeDeposit(transId);
		if (timeDeposit == null) {
			throw new NTBException("err.txn.NoSuchTransId");
		} else {
			String tdType = Utils.null2EmptyWithTrim(timeDeposit.getTdType());
			if (tdType.equals(TimeDeposit.TD_TYPE_WITHDRAW)) {
				timeDeposit.setStatus(Constants.STATUS_REMOVED);
				timeDeposit.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
				timeDepositDao.update(timeDeposit);

			} else {
				throw new NTBException("err.txn.TdTypeException");
			}
		}

	}

	private String getProductNoFronDB(String currency, String period) throws NTBException{
		/*String sql = "select PRODUCT_NO from HS_TD_ACCOUNT_PRODUCT_NO where ccy = ? and period = ?";
		try {
			Map rowMap = this.genericJdbcDao.querySingleRow(sql, new Object[] {currency, period});
			return rowMap.get("PRODUCT_NO").toString();
		} catch (Exception e) {
			throw new NTBException("err.txn.GetProductNoFronDBException");
		}*/
		String sql = "select PRODUCT_NO from HS_TD_ACCOUNT_PRODUCT_NO where ccy = ? and period = ?";
		try {
			List list = this.genericJdbcDao.query(sql, new Object[]{currency,period});
			if(list.size()>1){
				Map result = (Map)list.get(1);
				return result.get("PRODUCT_NO").toString();
			}
			return "F5Y";
		} catch (Exception e) {
			throw new NTBException("err.txn.GetNewTdAccCcyException");
		}
	}

	public boolean isAuthorize(TimeDeposit timeDeposit) throws NTBException {
		Map conditionMap = new HashMap();
		conditionMap.put("corpId", timeDeposit.getCorpId());
		conditionMap.put("tdAccount", timeDeposit.getTdAccount());
		conditionMap.put("status", Constants.STATUS_PENDING_APPROVAL);

		List list = this.timeDepositDao.list(TimeDeposit.class, conditionMap);

		return list.size()>0 ? false : true;
	}
	
	public List listNewTdAccCcy() throws NTBException{
		String sql = "select distinct ccy, concat(concat(ccy, ' - '), ccy_long_name) as ccy_long_name from hs_td_account_product_no t1, hs_currency_name t2 where t1.ccy=t2.ccy_code and t1.resisdent_flag = ?";
		try {
			List list = this.genericJdbcDao.query(sql, new Object[]{"R"});
			return list;
		} catch (Exception e) {
			throw new NTBException("err.txn.GetNewTdAccCcyException");
		}
		
	}	
	private String getAvailablePrefId(String corpId) {
		String hql = "from CorpAccount as ca where ca.corpId = ? and ca.status = ?";
		List list = this.corpAccountDao.list(hql, new Object[] {corpId, Constants.STATUS_NORMAL});
		return list.size() > 0 ? ((CorpAccount)list.get(0)).getPrefId() : null;
	}
	
	public List lsitHistory(String corpId, String userId, String dateFrom, String dateTo) throws NTBException {
		return this.timeDepositDao.listHistory(corpId, userId, dateFrom, dateTo);
	}

	public String tdType2TxnType(String tdType) throws NTBException {
		if (tdType.equals(TimeDeposit.TD_TYPE_NEW)) {
			return Constants.TXN_SUBTYPE_NEW_TIME_DEPOSIT;
		} else if (tdType.equals(TimeDeposit.TD_TYPE_WITHDRAW)){
			return Constants.TXN_SUBTYPE_WITHDRAWAL_DEPOSIT;
		} else {
			return null;
		}
	}
//	//add by linrui for change period to tinor
//	public int peToTinor(String period){
//		RBFactory rb = RBFactory.getInstance("app.cib.resource.common.period");
//		if( null != rb.getString(period)){
//			return Integer.parseInt(rb.getString(period));
//		}else{
//			return Integer.parseInt(period);
//		}
//	}
//	//end
//	
//	//add by gzy for change period
//	public String tinor2pe(String period){
//		RBFactory rb = RBFactory.getInstance("app.cib.resource.common.period_2");
//		if( null != rb.getString(period)){
//			return rb.getString(period.trim()).toString();
//		}else{
//			return period;
//		}
//		
//	}
	
	//search cif_no from acct linrui 20180122
	public String getCifByAcct(String acct) throws NTBException {
     String sql = "select * from Corp_Account ca where ca.ACCOUNT_NO = ?";
     try{
     Map map = genericJdbcDao.querySingleRow(sql, new Object[] { acct });
     return  map.get("CIF_NO").toString();
     } catch (Exception e) {
			throw new NTBException("err.bat.RejectTransferBankSTMError");
		}
    }
	//add by linrui 20190323
	public String getPeriodDescription(String period,String lang) throws NTBException {
		String sql = "SELECT PERIOD_DESCRIPTION FROM hs_period_code where period_code = ? and LOCAL_CODE = ?";
		try{
			Map map = genericJdbcDao.querySingleRow(sql, new Object[] { period, lang});
			return  map.get("PERIOD_DESCRIPTION").toString();
		} catch (Exception e) {
			throw new NTBException("err.bat.RejectTransferBankSTMError");
		}
	}
	//add by linrui 20190326
	public boolean isTimedepositAccountExist(String accountNo) throws NTBException{
		String sql = "select account_no from Corp_Account where account_no = ?";
		try {
			List list = this.genericJdbcDao.query(sql, new Object[]{accountNo});
			if(list !=null){
				return list.size()>0 ;
			}else{
				return false;
			}
			
		} catch (Exception e) {
			throw new NTBException("err.txn.GetNewTdAccCcyException");
		}
		
	}
	//add by linrui 20190326
	 public void updateAcctStatus(String accountNo, String status) throws NTBException {
	        try{
	        	genericJdbcDao.update(
	                    "update Corp_Account set STATUS = ? where account_no = ? ",
	                    new Object[] {status,accountNo});
	        }catch(Exception e){
	            Log.error("Error update group's permission", e);
	            throw new NTBException("err.sys.DBError");
	        }
	    }
	 //add by linrui template get ccy 
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
	 //end
	//add by linrui 20190525
		public String getInCd(String tdAccount) throws NTBException {
			ApplicationContext appContext = Config.getAppContext();
			GenericJdbcDao dao = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
			String sql = "select INST_CD from time_deposit where td_type = ? and TD_ACCOUNT = ? ";
			try{
				List idList = dao.query(sql, new Object[] {TimeDeposit.TD_TYPE_NEW,tdAccount});
				if(idList.size()>0){
					for(int i = 0 ; i < idList.size() ; i++){
						if(!"".equals(Utils.null2EmptyWithTrim(((Map)idList.get(i)).get("INST_CD")))){
							return ((Map)idList.get(i)).get("INST_CD").toString();
						}
					}
				}else{
					return "";
				}
				/*Map map = genericJdbcDao.querySingleRow(sql, new Object[] { TimeDeposit.TD_TYPE_NEW,tdAccount });
				return  map.get("INST_CD").toString();*/
			} catch (Exception e) {
				throw new NTBException("err.bat.RejectTransferBankSTMError");
			}
			return "";
		}
		//end
		//add by linrui 20190525
	public Map viewDepositDetial(String accountNo)throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		CibTransClient testClient = new CibTransClient("CIB", "0395");
		toHost.put("ACCOUNT_NO", accountNo);
		toHost.put("IS_RPD", "Y");

		String refNo = CibIdGenerator.getRefNoForTransaction();
		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
//		UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);

		return fromHost;
	}

	public void removeTransfer(String tableName, String transNo) {
		genericJdbcDao.removeTransfer(tableName,transNo);
		
	}

	@Override
	public List getInterestList(String language) throws NTBException {
		List periodList = null;
		List interestList = new ArrayList();
		try {
			periodList = getPeriodList(language);
			String sql = "select * from (select hc.ccy_name,INTEREST_RATE,PERIOD_DESCRIPTION,ir.UPDATE_TIME from INTEREST_RATE ir,hs_period_code pc,hs_currency_name hc where ir.period_code = pc.period_code and ir.ccy = hc.ccy_code and pc.local_code = ? and hc.local_code = ?) PIVOT(SUM(INTEREST_RATE) FOR PERIOD_DESCRIPTION IN('";
			for(int i = 0;i < periodList.size();i++){
				Map retMap = (Map) periodList.get(i);
				String periodDescription = (String) retMap.get("PERIOD_DESCRIPTION");
				if(i != periodList.size() -1){
					sql = sql + periodDescription + "','";
				}else{
					sql = sql +periodDescription + "') )";
				}
			}
			return genericJdbcDao.query(sql, new String[]{language,language});
		} catch (Exception e) {
			e.printStackTrace();
			throw new NTBException("err.sys.DBError");
		}
	}

	@Override
	public List getPeriodList(String language) throws NTBException {
		List periodList = null;
		String sql = "SELECT PERIOD_DESCRIPTION FROM hs_period_code WHERE LOCAL_CODE = ? AND to_number(substr(PERIOD_CODE,0,1)) <=1";
		try {
			periodList = genericJdbcDao.query(sql, new String[]{language});
		} catch (Exception e) {
			e.printStackTrace();
			throw new NTBException("err.sys.DBError");
		}
		return periodList;
	}

	@Override
	public List getCurrencyList(String language) throws NTBException {
		String sql = "select ccy_name from hs_currency_name where local_code = ?";
		try {
			List retList = genericJdbcDao.query(sql, new String[]{language});
			List resultList = new ArrayList();
			for(int i = 0; i < retList.size();i++){
				Map retMap = (Map) retList.get(i);
				resultList.add(retMap.get("CCY_NAME"));
			}
			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new NTBException("err.sys.DBError");
		}
	}

	
}
