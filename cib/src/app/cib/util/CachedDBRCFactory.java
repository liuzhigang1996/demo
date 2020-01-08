package app.cib.util;

import java.util.*;
import com.neturbo.set.utils.*;
import com.neturbo.set.core.*;
import app.cib.service.sys.CorpUserService;
import app.cib.bo.sys.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CachedDBRCFactory extends DBRCFactory {

    private static Map tableCacheMap = new HashMap();

    public CachedDBRCFactory() {
    }

    private CachedDBRCFactory(String key, String table, String keyField,
                              String valueField, String condiction, boolean isMul) {
    	// update by li_zd at 20180419 for CR230, add the param is Multilingual
        super(key, table, keyField, valueField, condiction, isMul);
    }

    private CachedDBRCFactory(String key, NTBResources rcObj) {
        super(key, rcObj);
    }


    public static synchronized void addPendingCache(String rcName) {
        CacheController.addNewUpdate(CacheController.CACHE_TYPE_DBRC, rcName);
    }

    public static synchronized void addPendingCacheByTableName(String tableName) {
        String rcNames = (String) tableCacheMap.get(tableName);
        if (rcNames != null) {
            String[] rcNameArray = Utils.splitStr(rcNames, ",");
            for (int i = 0; i < rcNameArray.length; i++) {
                addPendingCache(rcNameArray[i]);
            }
        }
    }

    public static synchronized void addPendingCacheAll() {
        String[] instanceNames = CachedDBRCFactory.getInstanceNames();
        for (int i = 0; i < instanceNames.length; i++) {
            addPendingCache(instanceNames[i]);
        }
        Log.info("*** populate cache for all DB resource successfully");
    }


      public void init() {

        new CachedDBRCFactory("groupByCorp",
                              new RcGroup(RcGroup.SELECT_GROUP_BY_CORP));
        new CachedDBRCFactory("groupByRole",
                              new RcGroup(RcGroup.SELECT_GROUP_BY_ROLE));

        new CachedDBRCFactory("accountByUser",
                              new RcAccount(CorpAccount.ACCOUNT_TYPE_ALL));
        new CachedDBRCFactory("accountBySubCorp",
                              new RcAccount(CorpAccount.ACCOUNT_TYPE_SUB_CORP));
        new CachedDBRCFactory("caoaAccountByUser",
                              new RcAccount(CorpAccount.ACCOUNT_TYPE_CAOA));                            
        new CachedDBRCFactory("caoasaAccountByUser",
                              new RcAccount(CorpAccount.ACCOUNT_TYPE_CAOASA));                              
        new CachedDBRCFactory("caAccountByUser",
                              new RcAccount(CorpAccount.ACCOUNT_TYPE_CURRENT));
        new CachedDBRCFactory("tdAccountByUser",
                              new RcAccount(CorpAccount.
                                            ACCOUNT_TYPE_TIME_DEPOSIT));
        new CachedDBRCFactory("oaAccountByUser",
                              new RcAccount(CorpAccount.ACCOUNT_TYPE_OVER_DRAFT));
        new CachedDBRCFactory("laAccountByUser",
                new RcAccount(CorpAccount.ACCOUNT_TYPE_LOAN));
        new CachedDBRCFactory("saAccountByUser",
                new RcAccount(CorpAccount.ACCOUNT_TYPE_SAVING));
        
        new CachedDBRCFactory("ccAccountByUser",
        		new RcAccount(CorpAccount.ACCOUNT_TYPE_CREDIT));
        
        new CachedDBRCFactory("accountName",
                              new RcAccount(CorpAccount.ACCOUNT_NAME));
        new CachedDBRCFactory("accountType",
                              new RcAccount(CorpAccount.ACCOUNT_TYPE));
        new CachedDBRCFactory("accountDesc",
                              new RcAccount(CorpAccount.ACCOUNT_DESCRIPTION));
        //add by linrui for cr230 download file description 20190130
        new CachedDBRCFactory("accountDescDownLoad",
        		new RcAccount(CorpAccount.ACCOUNT_DESCRIPTION_DOWNLOAD));
        //add by linrui for cr230 acciybr kusr desc 20190412
        new CachedDBRCFactory("accountListDesc",
        		new RcAccount(CorpAccount.ACCOUNT_LIST_DESCRIPTION));

        new CachedDBRCFactory("corpInTree", new RcCorporation(RcCorporation.SHOW_CORP_WITHOUT_ROOT));
        new CachedDBRCFactory("corpInTreeWithRoot", new RcCorporation(RcCorporation.SHOW_CORP_WITH_ROOT));
        new CachedDBRCFactory("corpName", new RcCorporation(RcCorporation.SHOW_CORP_NAME));

        new CachedDBRCFactory("accessControl", new RcAccessIp());

        //----------------------------------- 锟斤拷锟斤拷 RC 锟斤拷锟斤拷锟斤拷锟截憋拷 -------------------------------------
        // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷乇锟�HS_***** 锟斤拷RC锟斤拷锟斤拷同时锟斤拷锟斤拷 populateCacheByTableName() 锟斤拷锟斤拷
        new CachedDBRCFactory("txnDesc", new RcTransactionDescription());
        tableCacheMap.put("HS_TRANSACTION_DESCRIPTION", "txnDesc");

        //add by long_zg 2014-01-08 for CR192 bob batch
       /* new CachedDBRCFactory("txnFilter", new RcTransactionFilter());
        tableCacheMap.put("HS_TRANSACTION_FILTER_CODE", "txnFilter");
        tableCacheMap.put("HS_TRANSACTION_FILTER_VALUE", "txnFilter");*/
        new CachedDBRCFactory("txnFilter", new RcTransactionFilter(Config.getProperty("app.tx.filter.account")));
        tableCacheMap.put("HS_TRANSACTION_FILTER_CODE", "txnFilter");
        tableCacheMap.put("HS_TRANSACTION_FILTER_VALUE", "txnFilter");
        new CachedDBRCFactory("txnFilterCC", new RcTransactionFilter(Config.getProperty("app.tx.filter.creditcard")));

        new CachedDBRCFactory("currencyCBS", new RcCurrency());
        new CachedDBRCFactory("currency", "HS_CURRENCY_NAME", "CCY_CODE",
                              "CCY_LONG_NAME", "", true);
        new CachedDBRCFactory("chequeStyle", new RcChequeStyle());
        new CachedDBRCFactory("currencyMacau", new RcCurrencyMacau());
        new CachedDBRCFactory("currencyOversea", new RcCurrencyOversea());
        new CachedDBRCFactory("currencyComboBox", new RcCurrencyComboBox());
        tableCacheMap.put("HS_CURRENCY_NAME",
                          "currencyCBS,currency,currencyMacau,currencyOversea,currencyComboBox");

//        new CachedDBRCFactory("localBank", "HS_LOCAL_BANK_CODE", "BANK_CODE","BANK_NAME", "");
        new CachedDBRCFactory("localBank", new RcLocalBank());//add by linrui 20190530
        tableCacheMap.put("HS_LOCAL_BANK_CODE", "localBank");
        new CachedDBRCFactory("stopReason", "HS_STOP_CHEQUE_REASON", "REASON_CODE",
                "SHORT_DESC", "", true);

        tableCacheMap.put("HS_STOP_CHEQUE_REASON", "stopReason");
        new CachedDBRCFactory("branch", "HS_BRANCH_LIST", "BRANCH_CODE",
                              "BRANCH_DESCRIPTION", "ORDER BY BRANCH_CODE", true);
        tableCacheMap.put("HS_BRANCH_LIST", "branch");
        new CachedDBRCFactory("hostErrMsg", "HS_HOST_ERROR_MESSAGE", "ERR_CODE",
                              "ERR_MSG_DESCRIPTION", "", true);
        tableCacheMap.put("HS_HOST_ERROR_MESSAGE", "hostErrMsg");
        new CachedDBRCFactory("country", "HS_COUNTRY_CODE", "COUNTRY_CODE",
                              "COUNTRY_NAME", "", true);
        tableCacheMap.put("HS_COUNTRY_CODE", "country");
        new CachedDBRCFactory("city", "HS_CITY_CODE", "CITY_CODE", "CITY_NAME",
                              "", true);
        tableCacheMap.put("HS_CITY_CODE", "city");
        new CachedDBRCFactory("overseaBank", "HS_OVERSEAS_BANK", "BANK_CODE",
                              "BANK_NAME", "", true);
        tableCacheMap.put("HS_OVERSEAS_BANK", "overseaBank");

        new CachedDBRCFactory("merchant", "HS_MERCHANT_NAME", "MER_CODE",
                              "MER_SHORT_NAME",
                              "AND (CATEGORY_CODE='010' OR CATEGORY_CODE='090' OR CATEGORY_CODE='980')", true);
        //add by wcc 20180823
        new CachedDBRCFactory("merchant01", "HS_MERCHANT_NAME", "MER_CODE",
                              "MER_SHORT_NAME",
                             // "AND (CATEGORY_CODE='010' OR CATEGORY_CODE='090' OR CATEGORY_CODE='980') AND (MER_CODE!='FSS')", true);
                             //mod by linrui 20190317 
                              "AND (CATEGORY_CODE='010' OR CATEGORY_CODE='090' OR CATEGORY_CODE='980') AND (MER_CODE NOT IN ('FSS','CRD2','CRD3'))", true);
        //end
        //add by linrui 20190402
        new CachedDBRCFactory("merchant02", "HS_MERCHANT_NAME", "MER_CODE",
                              "MER_SHORT_NAME",
                              "AND (CATEGORY_CODE='010' OR CATEGORY_CODE='090' OR CATEGORY_CODE='980') AND (MER_CODE NOT IN ('CRD2','CRD3'))", true);
        //end
        new CachedDBRCFactory("merchantAll", "HS_MERCHANT_NAME", "MER_CODE",
                              "MER_SHORT_NAME", "", true);
        new CachedDBRCFactory("tax", "HS_MERCHANT_NAME", "MER_CODE",
                              "MER_SHORT_NAME", "AND CATEGORY_CODE='970'", true);
        tableCacheMap.put("HS_MERCHANT_NAME", "merchant,merchantAll,tax");

        new CachedDBRCFactory("merCodeByCategory", "HS_MERCHANT_BILL_PAYMENT",
                              "CATEGORY_CODE", "MER_CODE", "", false);
        tableCacheMap.put("HS_MERCHANT_BILL_PAYMENT", "merCodeByCategory");

        new CachedDBRCFactory("category", "HS_MERCHANT_CATEGORY",
                              "CATEGORY_CODE", "CATEGORY_NAME",
                              "AND (CATEGORY_CODE='010' OR CATEGORY_CODE='090')", true);
        tableCacheMap.put("HS_MERCHANT_CATEGORY", "category");

        new CachedDBRCFactory("period", "HS_PERIOD_CODE", "PERIOD_CODE",
                              "PERIOD_DESCRIPTION", "ORDER BY PERIOD_NO", true);
        tableCacheMap.put("HS_PERIOD_CODE", "period");

        new CachedDBRCFactory("branch", "HS_BRANCH_LIST", "BRANCH_CODE",
                              "BRANCH_DESCRIPTION", "", false);
        tableCacheMap.put("HS_BRANCH_LIST", "branch");

        new CachedDBRCFactory("statementForList", new RcStatementStatus(RcStatementStatus.STATEMENT_TYPE_FOR_LIST));
        new CachedDBRCFactory("statementForDetail",new RcStatementStatus(RcStatementStatus.STATEMENT_TYPE_FOR_DETAIL));
        tableCacheMap.put("HS_E_STATEMENT_METADATA", "statementForList");

        new CachedDBRCFactory("pdfCategory", "HS_PDF_CATEGORY",
                              "CODE", "DESCRIPTION", "", false);
        tableCacheMap.put("HS_PDF_CATEGORY", "pdfCategory");

        new CachedDBRCFactory("inwardRemInfo", new RcInwardRemInfo());
        tableCacheMap.put("HS_INWARD_REMITTANCE_INFO", "inwardRemInfo");

        new CachedDBRCFactory("outwardRemAdvice",new RcOutwardRemAdvice());
        tableCacheMap.put("HS_OUTWARD_REMITTANCE_ADVICE", "outwardRemAdvice");

        new CachedDBRCFactory("newTdCcy",new RcNewTdCcy());
        tableCacheMap.put("HS_TD_ACCOUNT_PRODUCT_NO", "newTdCcy");
        tableCacheMap.put("HS_CURRENCY_NAME", "newTdCcy");

        new CachedDBRCFactory("adviceOperation",new RcEAdviceOperation());
        tableCacheMap.put("HS_E_ADVICE", "adviceOperation");

        new CachedDBRCFactory("reportsOperation",new RcEReportsOperation());
        tableCacheMap.put("HS_E_REPORTS", "reportsOperation");

        new CachedDBRCFactory("reportsName",new RcEReportsName());
        tableCacheMap.put("HS_E_REPORTS", "reportsName");

        new CachedDBRCFactory("formsType",new RcEFormsType());
        tableCacheMap.put("HS_E_FORMS", "formsType");
        
        /* Add by heyongjiang */
        new CachedDBRCFactory("purpose", new RcPurpose());
        tableCacheMap.put("HS_PURPOSE_DESCRIPTION", "purpose");

        // Jet added for 
        new CachedDBRCFactory("transactionNature", "HS_TRANSACTION_NATURE", "TRANSACTION_NATURE_CODE",
                "TRANSACTION_NATURE_DESCRIPTION", "",false);

        tableCacheMap.put("HS_TRANSACTION_NATURE", "transactionNature");
        
        
        
        
        
        //add by long_zg 2014-01-07 for CR192 BOB Batch
        new CachedDBRCFactory("autopay", new RcAutopay());
        //add by linrui 20180504 for cr230 ccy name
        new CachedDBRCFactory("rcCurrencyCBS",new RcCurrencyCBS());
        
        //锟斤拷锟斤拷锟铰硷拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷cache锟斤拷锟铰碉拷锟斤拷锟斤拷 锟斤拷CacheController
        setEventHandler(new CacheController());
    }

    public static void main(String[] args) {
        CorpUserService userService = (CorpUserService) Config.getAppContext().
                                      getBean("corpUserService");
        DBRCFactory rc = DBRCFactory.getInstance("group");
        try {
            NTBUser user = (NTBUser) userService.load("1101");
            rc = DBRCFactory.getInstance("groupByCorp");
            rc.setArgs(user);
            System.out.println(rc.getString("10001"));
            rc = DBRCFactory.getInstance("accountByUser");
            rc.setArgs(user);
            System.out.println(rc.getString("9000010011"));
            System.out.println(rc.getString("9000010012"));
            rc = DBRCFactory.getInstance("currentAccountByUser");
            rc.setArgs(user);
            System.out.println(rc.getString("9000010011"));
            System.out.println(rc.getString("9000010012"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
