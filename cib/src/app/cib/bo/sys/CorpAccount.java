/*
 * Created Fri Jul 21 17:33:02 CST 2006 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.sys;

import java.io.Serializable;

/**
 * A class that represents a row in the 'CORP_ACCOUNT' table.
 * This class may be customized as it is never re-generated
 * after being created.
 */
public class CorpAccount extends AbstractCorpAccount implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 571770797806088953L;

	public static final String ACCOUNT_TYPE_ALL = "0";
    public static final String ACCOUNT_TYPE_CURRENT = "CA";//MOD BY LINRUI 20180208 1
    public static final String ACCOUNT_TYPE_TIME_DEPOSIT = "TD";//MOD BY LINRUI 20180208 2
    public static final String ACCOUNT_TYPE_OVER_DRAFT = "3";
    public static final String ACCOUNT_TYPE_LOAN = "LN";//MOD BY LINRUI 20180208  4
    public static final String ACCOUNT_TYPE_SAVING = "SA";//MOD BY LINRUI 20180208 5

    public static final String ACCOUNT_TYPE_CAOASA = "C";
    public static final String ACCOUNT_TYPE_CAOA = "R";
    public static final String ACCOUNT_TYPE_ALL_CORP = "A";
    public static final String ACCOUNT_TYPE_SUB_CORP = "S";
    public static final String ACCOUNT_NAME = "N";
    public static final String ACCOUNT_TYPE = "T";
    public static final String ACCOUNT_DESCRIPTION = "D";

    // add by Jet 2007-04-13
    public static final String APPLICATION_CODE_CURRENT = "CA";
    public static final String APPLICATION_CODE_SAVING = "SA";
/*    public static final String APPLICATION_CODE_CURRENT = "20";
    public static final String APPLICATION_CODE_SAVING = "26";*/ 
    public static final String APPLICATION_CODE_OVERDRAFT = "21";
   
    //add by long_zg 2014-12-16 for CR192 bob batch
    public static final String ACCOUNT_TYPE_CREDIT = "6";
    public static final String ACCOUNT_TYPE_CREDIT_VISA = "6";
    public static final String ACCOUNT_TYPE_CREDIT_AE = "7";
    public static final String ACCOUNT_TYPE_CREDIT_MASTER = "8";
//    public static final String APPLICATION_CODE_CREDIT_VISA = "02";
//    public static final String APPLICATION_CODE_CREDIT_MASTER = "03";
//    public static final String APPLICATION_CODE_CREDIT_AE = "04";
    public static final String ACCOUNT_DESCRIPTION_DOWNLOAD = "DD";//add by linrui for download file
    public static final String ACCOUNT_LIST_DESCRIPTION = "LD";//add by linrui for download file
    
        
    /**
     * Simple constructor of CorpAccount instances.
     */
    public CorpAccount() {
    }

    /**
     * Constructor of CorpAccount instances given a simple primary key.
     * @param accountNo
     */
    public CorpAccount(java.lang.String accountNo) {
        super(accountNo);
    }

    /* Add customized code below */

}
