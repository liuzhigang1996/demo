/*
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package app.cib.util;

/**
 * @author panwen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Constants {
    public static final String OPERATION_NEW = "1";

    public static final String OPERATION_UPDATE = "2";

    public static final String OPERATION_UPDATE_BEFORE = "B";

    public static final String OPERATION_BLOCK = "3";

    public static final String OPERATION_UNBLOCK = "4";

    public static final String OPERATION_ASSIGN_GROUP = "5";

    public static final String OPERATION_THOROUGH_REMOVE = "8";

    public static final String OPERATION_REMOVE = "9";

    public static final String OPERATION_KEEPONLINE = "A";

    public static final String OPERATION_KEEPOFFLINE = "B";

    public static final String OPERATION_RESET_PASSWORD = "R";

    public static final String STATUS_NORMAL = "0";

    public static final String STATUS_UPDATE_BEFORE = "B";

    public static final String STATUS_PENDING_APPROVAL = "1";

    public static final String STATUS_BLOCKED = "2";
    
    public static final String STATUS_FROZEN = "4";

    public static final String STATUS_REMOVED = "9";
    
    public static final String STATUS_OVERDUE = "3";

    public static final String STATUS_THOROUGH_REMOVED = "8";

    public static final String STATUS_CANCELED = "7";

    public static final String AUTH_STATUS_COMPLETED = "0";

    public static final String AUTH_STATUS_SUBMITED = "1";

    public static final String AUTH_STATUS_REJECTED = "7";

    public static final String AUTH_STATUS_EXECUTE_ERROR = "8";

    public static final String AUTH_STATUS_OTHER_ERROR = "9";

    public static final String YES = "Y";

    public static final String NO = "N";

    public static final String BLOCK_REASON_BY_ADMIN = "2";
    public static final String BLOCK_REASON_BY_RETRY = "1";

    // 閻€劋绨幒鍫熸綀閻ㄥ嫪姘﹂弰鎾崇摍缁鐎�
    // 鏉烆剙绗庨敍鍫熷房閺夊啩姘﹂弰鎾崇摍缁紮绱�
    public static final String TXN_SUBTYPE_TRANSFER_BANK = "TRANSFER_BANK";
    
    //add by long_zg 2019-05-16 for transfer 3rd
    public static final String TXN_SUBTYPE_TRANSFER_BANK_3RD = "TRANSFER_BANK_3RD";
    
    public static final String TXN_SUBTYPE_TRANSFER_BANK_1TON = "TRANSFER_BANK_1TON";
    public static final String TXN_SUBTYPE_TRANSFER_BANK_NTO1 = "TRANSFER_BANK_NTO1";

    public static final String TXN_SUBTYPE_TRANSFER_MACAU = "TRANSFER_MACAU";
    public static final String TXN_SUBTYPE_TRANSFER_MACAU_1TON = "TRANSFER_MACAU_N";

    public static final String TXN_SUBTYPE_TRANSFER_OVERSEAS =
        "TRANSFER_OVERSEAS";
    public static final String TXN_SUBTYPE_TRANSFER_OVERSEAS_1TON =
        "TRANSFER_OVERSEAS_N";

    public static final String TXN_SUBTYPE_TRANSFER_REPETRIATE =
            "REPETRIATE_FROM_SUB";

    public static final String TXN_SUBTYPE_TRANSFER_DELIVERY =
            "DELIVERY_TO_SUB";

    public static final String TXN_SUBTYPE_TRANSFER_BETWEEN_SUBSIDIARY =
            "TRANFER_BETWEEN_SUB";

    // 缂傜鍨傞敍鍫熷房閺夊啩姘﹂弰鎾崇摍缁紮绱�
    public static final String TXN_SUBTYPE_GENERAL_PAYMENT = "GENERAL_PAYMENT";

    public static final String TXN_SUBTYPE_TAX_PAYMENT = "TAX_PAYMENT";

    public static final String TXN_SUBTYPE_CARD_PAYMENT = "CARD_PAYMENT";

    public static final String TXN_SUBTYPE_BATCH_PAYMENT = "BATCH_PAYMENT";

    // 鐎规碍婀＄�涙ɑ顑欓敍鍫熷房閺夊啩姘﹂弰鎾崇摍缁紮绱�
    public static final String TXN_SUBTYPE_NEW_TIME_DEPOSIT =
            "NEW_TIME_DEPOSIT";

    public static final String TXN_SUBTYPE_WITHDRAWAL_DEPOSIT =
            "WITHDRAW_DEPOSIT";

    // 閸忔湹绮禍銈嗘閿涘牊宸块弶鍐ф唉閺勬挸鐡欑猾浼欑礆
    public static final String TXN_SUBTYPE_PAYROLL = "PAYROLL";

    public static final String TXN_SUBTYPE_SCHEDULE_TXN_NEW = "NEW_SCHEDULE_TXN";
    public static final String TXN_SUBTYPE_SCHEDULE_TXN_EDIT = "EDIT_SCHEDULE_TXN";
    public static final String TXN_SUBTYPE_SCHEDULE_TXN_BLOCK = "BLOCK_SCHEDULE_TXN";
    public static final String TXN_SUBTYPE_SCHEDULE_TXN_UNBLOCK = "UNBLOCK_SCHEDULE_TXN";
    public static final String TXN_SUBTYPE_SCHEDULE_TXN_REMOVE = "REMOVE_SCHEDULE_TXN";

    public static final String TXN_SUBTYPE_BANK_DRAFT = "BANK_DRAFT";
    public static final String TXN_SUBTYPE_CASHIER_ORDER = "CASHIER_ORDER";
    public static final String TXN_SUBTYPE_STOP_CHEQUE = "STOP_CHEQUE";
    public static final String TXN_SUBTYPE_CHEQUE_PROTECTION = "CHEQUE_PROTECTION";

    // 閻€劍鍩涚紒鍕吀閻炲棴绱欓幒鍫熸綀娴溿倖妲楃�涙劗琚敍锟�
    public static final String TXN_SUBTYPE_GROUP_ADD = "USER_GROUP_ADD";

    public static final String TXN_SUBTYPE_GROUP_EDIT = "USER_GROUP_EDIT";

    public static final String TXN_SUBTYPE_GROUP_DELETE = "USER_GROUP_DELETE";

    public static final String TXN_SUBTYPE_GROUP_ASSIGN = "USER_GROUP_ASSIGN";

    // 閸忣剙寰冪粻锛勬倞閿涘牊宸块弶鍐ф唉閺勬挸鐡欑猾浼欑礆
    public static final String TXN_SUBTYPE_CORP_ADD = "CORP_ADD";

    public static final String TXN_SUBTYPE_CORP_EDIT = "CORP_EDIT";

    public static final String TXN_SUBTYPE_CORP_BLOCK = "CORP_BLOCK";

    public static final String TXN_SUBTYPE_CORP_UNBLOCK = "CORP_UNBLOCK";

    public static final String TXN_SUBTYPE_CORP_DELETE = "CORP_DELETE";

    // 閸忣剙寰冮崣鍌涙殶闁板秶鐤嗛敍鍫熷房閺夊啩姘﹂弰鎾崇摍缁紮绱�
    public static final String TXN_SUBTYPE_PREF_ACCOUNT = "PREF_ACCOUNT";

    public static final String TXN_SUBTYPE_PREF_LIMIT = "PREF_LIMIT";

    public static final String TXN_SUBTYPE_PREF_AUTHORIZATION =
            "PREF_AUTHORIZATION";

    public static final String TXN_SUBTYPE_PREF_SUBSIDIARY = "PREF_SUBSIDIARY";

    // 閸忣剙寰冮悽銊﹀煕缁狅紕鎮婇敍鍫熷房閺夊啩姘﹂弰鎾崇摍缁紮绱�
    public static final String TXN_SUBTYPE_CORP_USER_ADD = "CORP_USER_ADD";

    public static final String TXN_SUBTYPE_CORP_USER_BLOCK = "CORP_USER_BLOCK";

    public static final String TXN_SUBTYPE_CORP_USER_UNBLOCK =
            "CORP_USER_UNBLOCK";

    public static final String TXN_SUBTYPE_CORP_USER_EDIT = "CORP_USER_EDIT";

    public static final String TXN_SUBTYPE_CORP_USER_RESET_PWD =
            "CORP_USER_RESETPWD";

    public static final String TXN_SUBTYPE_CORP_USER_RESET_CODE =
            "CORP_USER_RESET_CODE";

    public static final String TXN_SUBTYPE_CORP_USER_DELETE =
            "CORP_USER_DELETE";

    // 闁炬儼顢戦悽銊﹀煕缁狅紕鎮婇敍鍫熷房閺夊啩姘﹂弰鎾崇摍缁紮绱�
    public static final String TXN_SUBTYPE_BANK_USER_ADD = "BANK_USER_ADD";

    public static final String TXN_SUBTYPE_BANK_USER_BLOCK = "BANK_USER_BLOCK";

    public static final String TXN_SUBTYPE_BANK_USER_UNBLOCK =
            "BANK_USER_UNBLOCK";

    public static final String TXN_SUBTYPE_BANK_USER_EDIT = "BANK_USER_EDIT";

    public static final String TXN_SUBTYPE_BANK_USER_RESETPWD =
            "BANK_USER_RESETPWD";

    public static final String TXN_SUBTYPE_BANK_USER_DELETE =
            "BANK_USER_DELETE";
    //add by linrui 20190701 for control currency
    public static final String TXN_SUBTYPE_CONTROL_CCY = "CONTROL_CCY";

    // ACCOUNT AUTHROIZATION
    public static final String TXN_SUBTYPE_ACCTOUNT_AUTHORIZATION_ADD = "ACCT_AUTH_ADD";    
    public static final String TXN_SUBTYPE_ACCTOUNT_AUTHORIZATION_EDIT = "ACCT_AUTH_EDIT";    
    public static final String TXN_SUBTYPE_ACCTOUNT_AUTHORIZATION_DELETE = "ACCT_AUTH_DELETE";    
    
    // 濞戝牊浼呯粻锛勬倞閿涘牊宸块弶鍐ф唉閺勬挸鐡欑猾浼欑礆
    public static final String TXN_SUBTYPE_MESSAGE_UPDATE = "DESCRIPTION_UPDATE";

    public static final String TXN_SUBTYPE_MESSAGE_DELETE = "DESCRIPTION_DELETE";

    //
    public static final String TXN_TYPE_TRANSFER_OWN_ACCOUNT = "TRANSFER_OWN_ACCOUNT";

    public static final String TXN_TYPE_TRANSFER_BANK = "TRANSFER_BANK";
    
    public static final String TXN_TYPE_TRANSFER_BANK_3RD = "TRANSFER_BANK_3RD";

    public static final String TXN_TYPE_TRANSFER_MACAU = "TRANSFER_MACAU";

    public static final String TXN_TYPE_TRANSFER_OVERSEAS = "TRANSFER_OVERSEAS";

    public static final String TXN_TYPE_TRANSFER_CORP = "TRANSFER_CORP";

    public static final String TXN_TYPE_PAY_BILLS = "PAY_BILLS";

    public static final String TXN_TYPE_TIME_DEPOSIT = "TIME_DEPOSIT";

    public static final String TXN_TYPE_PAYROLL = "PAYROLL";

    public static final String TXN_TYPE_SCHEDULE_TXN = "SCHEDULE_TXN";

    public static final String TXN_TYPE_BANK_DRAFT = "BANK_DRAFT";

    public static final String TXN_TYPE_CASHIER_ORDER = "CASHIER_ORDER";
  
    // add by li_zd
    public static final String TXN_TYPE_TRANSFER_BANK_SCHEDULE = "TRANSFER_BANK_SCHEDULE";
    public static final String TXN_TYPE_TRANSFER_MACAU_SCHEDULE = "TRANSFER_MACAU_SCHEDULE";
    public static final String TXN_TYPE_TRANSFER_OVERSEAS_SCHEDULE = "TRANSFER_OVERSEAS_SCHEDULE";
    public static final String TXN_TYPE_AUTO_PAY_AUTHORIZATION = "AUTO_PAY_AUTHORIZATION";
    

    // viewer access list
    public static final String TXN_SUBTYPE_VIEWER_ACCESS_LIST = "VIEWER_ACCESS_LIST";

    // 閻€劍鍩涚憴鎺曞
    public static final String ROLE_OPERATOR = "1";

    public static final String ROLE_APPROVER = "2";

    public static final String ROLE_EXECUTOR = "3";

    public static final String ROLE_ADMINISTRATOR = "4";

    public static final String ROLE_OPERATOR_APPROVER = "A";
    
    public static final String ROLE_OPERATOR_APPROVER_ADMIN = "B";
    
    public static final String ROLE_APPROVER_ADMIN = "C";

    public static final String ROLE_BANK_VIEWER = "6";
    
    public static final String ROLE_BANK_OPERATOR = "7";

    public static final String ROLE_BANK_SUPERVISOR = "8";

    //娴间椒绗熺猾璇茬��
    public static final String CORP_TYPE_LARGE="1";
    
    public static final String CORP_TYPE_MIDDLE="2";
    
    public static final String CORP_TYPE_SMALL="3";
    
    public static final String CORP_TYPE_MIDDLE_NO_ADMIN="4";

    //閹哄牊娼堢憴鍕灟缁鐎�
    public static final String RULE_TYPE_SINGLE="0";
    
    public static final String RULE_TYPE_MULTI="1";

    //閸掋倖鏌囨潏鎾冲弳閻ㄥ嫭妲� from amount 鏉╂ɑ妲� to amount
    public static final String INPUT_AMOUNT_FLAG_FROM = "0";
    
    public static final String INPUT_AMOUNT_FLAG_TO = "1";

    //Acknowledgement 缁鐎�
    public static final String ACK_STATUS_PENDING_APPROVAL = "PENDING_APPROVAL";
    
    public static final String ACK_STATUS_ACCOMPLISHED = "ACCOMPLISHED";
    
    // 鐠併倛鐦夌猾璇茬��
    public static final String AUTHENTICATION_CERTIFICATION = "C";
    public static final String AUTHENTICATION_SECURITY_CODE = "S";
    
    
//    add by long_zg 2015-01-08 for CR192 bob batch
    public static final String TXN_SUBTYPE_AUTOPAYMENT = "AUTOPAY";
    
    public static final String TXN_SUBTYPE_AUTOPAYMENT_EDIT = "AUTOPAY_EDIT";
    public static final String TXN_SUBTYPE_AUTOPAYMENT_ADD = "AUTOPAY_ADD";
    public static final String TXN_SUBTYPE_AUTOPAYMENT_DELETE = "AUTOPAY_DELETE";
    
    public static final String TXN_SUBTYPE_CHEQUE_BOOK_REQUEST = "CHEQUE_BOOK_REQUEST";
    
    public static final String AUTOPAYMENT_PAYMENT_FULL = "F" ;
    public static final String AUTOPAYMENT_PAYMENT_MIN = "M" ;
    public static final String AUTOPAYMENT_PAYMENT_INPUT = "P" ;
    public static final String AUTOPAYMENT_NO_LIMIT = "9.999999999E9";
    public static final String AUTOPAYMENT_MODE_ADD = "A" ;
    public static final String AUTOPAYMENT_MODE_EDIT = "E" ;
    public static final String AUTOPAYMENT_MODE_DELETE = "D" ;
    
    
//    <!-- modify by long_zg 2014-04-03 for CR204 Apply OTP to BOB begin-->
    public static final String SHOW_MOBILE_Y = "Y" ;
    //add by linrui for mul-language 20171109
    public static  final String  US = "en_US";
    public static final String  US_HOST = "E";//english
    public static final String  CN = "zh_CN";
    public static final String  CN_HOST = "S";//SIMPLIFIED CHINESE
    public static final String  PT = "pt_PT";
    public static final String  PT_HOST = "P";//Portuguese
    public static final String  HK = "zh_HK";
    public static final String  TW = "zh_TW";
    //public static final String  HK_TW_HOST = "T";//TRADITIONAL CHINESE
    public static final String  HK_TW_HOST = "C";//TRADITIONAL CHINESE
    
    
    
    /* Add by long_zg 2019-06-02 UAT6-465 COB閿涙氨鐓穱鈥叉唉閺勬捇顢ｉ崹瀣繁婢讹拷 begin */
    
    public static final String SMS_TRAN_TYPE_SCHEDULED_TRANSFER = "SCHEDULED_TRANSFER_TRANSFER" ;  //scheduled transfer
    public static final String SMS_TRAN_TYPE_SCHEDULED_CANCELLED = "SCHEDULED_TRANSFER_CANCELLED" ; //scheduled cancelled
    public static final String SMS_TRAN_TYPE_SCHEDULED_REMITTANCE = "SCHEDULED_REMITTANCE" ;  //scheduled remittance
//    public static final String SMS_TRAN_TYPE_SCHEDULED_CANCELLED = "SCHEDULED_CANCELLED" ; //scheduled cancelled
    public static final String SMS_TRAN_TYPE_OPENED_TIME_DEPOSIT = "OPENED_TIME_DEPOSIT" ; //opened time deposit
    public static final String SMS_TRAN_TYPE_TIME_DEPOSIT_WITHDRAWAL = "TIME_DEPOSIT_WITHDRAWAL" ; //time deposit withdrawal
    public static final String SMS_TRAN_TYPE_APPLIED_CHEQUE = "APPLIED_CHEQUE" ; //applied cheque
    public static final String SMS_TRAN_TYPE_CANCELLED_CHEQUE = "CANCELLED_CHEQUE" ; //cancelled cheque
    
    //PASSWORD
    public static final String SMS_TRAN_TYPE_CHANGE_LOGIN_PASSWORD = "CHANGE_LOGIN_PASSWORD" ;    		//changed login password
    public static final String SMS_TRAN_TYPE_CHANGE_TRANSACTION_PASSWORD = "CHANGE_TRANSACTION_PASSWORD" ;		//changed transaction password
    public static final String SMS_TRAN_TYPE_SUSPENDED_LOGIN_PASSWORD = "SUSPENDED_LOGIN_PASSWORD" ;			//suspended login password
    public static final String SMS_TRAN_TYPE_LOCKED_LOGIN_PASSWORD = "LOCKED_LOGIN_PASSWORD" ;				//locked login password
    public static final String SMS_TRAN_TYPE_FROZEN_LOGIN_PASSWORD = "FROZEN_LOGIN_PASSWORD" ;				//frozen login password
    public static final String SMS_TRAN_TYPE_LOCKED_TRANSACTION_PASSWORD = "LOCKED_TRANSACTION_PASSWORD" ;			//locked transaction password
    
    public static final String SMS_TRAN_TYPE_LOGGED_IN = "LOGGED_IN" ;			//logged in
    
    public static final String SMS_TEMPLATE_ID_TYPE_OTP = "OTP" ;
    public static final String SMS_TEMPLATE_ID_TYPE_SCH = "SCH" ;
    public static final String SMS_TEMPLATE_ID_TYPE_SET_PASS = "SET_PASS" ;
    public static final String SMS_TEMPLATE_ID_TYPE_LOC_PASS = "LOC_PASS" ;
    public static final String SMS_TEMPLATE_ID_TYPE_LOGIN = "LOGIN" ;
    public static final String SMS_TEMPLATE_ID_TYPE_REMITTANCE = "REMITTANCE" ;
    
    public static final String SMS_TEMPLATE_CHANNEL_WEB = "CHANNEL_WEB" ;
    public static final String SMS_TEMPLATE_CHANNEL_WEB_LOGIN = "CHANNEL_WEB_LOGIN" ;
    /* Add by long_zg 2019-06-02 UAT6-465 COB end */
    //add by linrui 20190917
    public static final int LoginFailTimesWarnning1 = 3 ;
    public static final int LoginFailTimesWarnning2 = 4 ;
    public static final int AllowMaxTimesForFrozen = 5 ;
    public static final int AllowMaxTimesForBlock = 10 ;
    /*add by linrui 20190923*/
    public final static String ALL_TYPE_STATEMENT = " AND REPORT_CODE = 'CI-S-001' ";
    public final static String ALL_TYPE_TIMEDEPOSIT = "AND REPORT_CODE in ('TD-D-004A','TD-D-003A') ";
    public final static String ALL_TYPE_REMITTANCE = "AND REPORT_CODE IN ('PY-D-035','PY-D-036') ";
    public final static String ALL_TYPE_LOAN = "AND REPORT_CODE IN('LN-D-012A','LN-D-013A','LN-D-014A','LN-D-016A') ";
    public final static String ALL_TYPE_DRAWDOWN = "AND REPORT_CODE = 'LN-D-015A' ";
	public final static String ALL_TYPE_OVERDUE_REPAYMENT = "AND REPORT_CODE = 'LN-D-017A' ";
	public final static String ALL_TYPE_INACTIVE_ACCOUNT = "AND REPORT_CODE in ('DD-S-030','DD-D-001A') ";
	
	public final static String ESTATEMENT_REPORT_CODE_CoNote= "CI-S-001";
	public final static String ESTATEMENT_REPORT_CODE_PeNote = "TD-D-004A,TD-D-003A";
	public final static String ESTATEMENT_REPORT_CODE_AsNote = "DD-S-030,DD-D-001A";
	public final static String ESTATEMENT_REPORT_CODE_ReNote = "PY-D-035,PY-D-036";
	public final static String ESTATEMENT_REPORT_CODE_LoNote = "LN-D-012A,LN-D-013A,LN-D-014A,LN-D-016A";
	public final static String ESTATEMENT_REPORT_CODE_CrNote = "LN-D-015A";
	public final static String ESTATEMENT_REPORT_CODE_OvNote = "LN-D-017A";
	
	public final static String passwordLimitDictionary = "qqqq,wwww,eeee,rrrr,tttt,yyyy,uuuu,iiii,oooo,pppp,aaaa,ssss,dddd,ffff,gggg,hhhh,jjjj,kkkk,llll,zzzz,xxxx,cccc,vvvv,bbbb,nnnn,mmmm," +
			"QQQQ,WWWW,EEEE,RRRR,TTTT,YYYY,UUUU,IIII,OOOO,PPPP,AAAA,SSSS,DDDD,FFFF,GGGG,HHHH,JJJJ,KKKK,LLLL,ZZZZ,XXXX,CCCC,VVVV,BBBB,NNNN,MMMM," + 
			"abcd,bcde,cdef,defg,efgh,fghi,ghij,hijk,ijkl,jklm,klmn,lmno,mnop,nopq,opqr,pqrs,qrst,rstu,stuv,tuvw,uvwx,vwxy,wxyz," +
			"zyxw,yxwv,xwvu,wvut,vuts,utsr,tsrq,srqp,rqpo,qpon,ponm,onml,nmlk,mlkj,lkji,kjih,jihg,ihgf,hgfe,gfed,fedc,edcb,dcba," +
			"ABCD,BCDE,CDEF,DEFG,EFGH,FGHI,GHIJ,HIJK,IJKL,JKLM,KLMN,LMNO,MNOP,NOPQ,OPQR,PQRS,QRST,RSTU,STUV,TUVW,UVWX,VWXY,WXYZ,"+
			"ZYXW,YXWV,XWVU,WVUT,VUTS,UTSR,TSRQ,SRQP,RQPO,QPON,PONM,ONML,NMLK,MLKJ,LKJI,KJIH,JIHG,IHGF,HGFE,GFED,FEDC,EDCB,DCBA,"+
			"1111,2222,3333,4444,5555,6666,7777,8888,9999,0000," + 
			"0123,1234,2345,3456,4567,5678,6789," + 
			"9876,8765,7654,6543,5432,4321,3210,";
    
}
