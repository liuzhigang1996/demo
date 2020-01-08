package app.cib.core;

import java.math.BigDecimal;
import java.text.*;
import java.util.*;

import org.springframework.context.ApplicationContext;

import app.cib.bo.sys.*;
import app.cib.service.sys.CutOffTimeService;
import app.cib.service.utl.UtilService;
import app.cib.util.Constants;

import com.neturbo.set.core.*;
import com.neturbo.set.exception.*;
import com.neturbo.set.transaction.*;
import com.neturbo.set.utils.*;
import com.neturbo.set.xml.XMLElement;

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
public class CibTransClient extends TransClient {

    /*
         Transaction Nature
         1=Enquiry Transaction
         2=Normal Fund Transfer
         3=Bill Payment Execution (Fund Transfer)
         4=Time Open/Withdraw
         6=Corporate Fund Allocation
         7=Request
     */
    public static final String TXNNATURE_ENQUIRY = "1";
    public static final String TXNNATURE_TRANSFER = "2";
    public static final String TXNNATURE_BILL_PAYMENT = "3";
    public static final String TXNNATURE_TIME_DEPOSIT = "4";
    public static final String TXNNATURE_FUND_ALLOC = "5";
    public static final String TXNNATURE_FUND_REQUEST = "6";
    public static final String TXNNATURE_REQUEST = "7";

    /*
         Transfer to Account Type:
         1 ?C Owner Account in Account Group
         2 ?C Other 3rd Party Account
     */
    public static final String ACCTYPE_OWNER_ACCOUNT = "1";
    public static final String ACCTYPE_3RD_PARTY = "2";

    /*
        20=C/A, 21=O/A, 30=Time, 50=Loan ,26=S/A
     */
    public static final String APPCODE_CURRENT_ACCOUNT = "20";
    public static final String APPCODE_OVERDRAFT_ACCOUNT = "21";
    public static final String APPCODE_TIME_DEPOSIT = "30";
    public static final String APPCODE_LOAN_ACCOUNT = "50";
    public static final String APPCODE_SAVING_ACCOUNT = "26";
    
//  add by long_zg 2014-12-19 for CR192 bob batch
    public static final String APPCODE_CREDIT_VISA = "02";
    public static final String APPCODE_CREDIT_MASTER = "03";
    public static final String APPCODE_CREDIT_AE = "04";
    public static final String APPCODE_CREDIT_UT = "05";

    public static final String CURRENCY_LOCAL = "MOP";
    public static final String CURRENCY_FOREIGN_A = "HKD";
    public static final String CURRENCY_FOREIGN_B = "USD";

    private Map alpha8Map = new HashMap();
    private NTBErrorArray errorArray = new NTBErrorArray();

    private static XMLElement hostNotAvailableInHolidayXML = Config.getConfigXML("HostNotAvailableInHolidayXML");

    public CibTransClient(String newTransCode) throws
            NTBException {
        super(newTransCode);
    }

    public CibTransClient(String newServiceName, String newTransCode) throws
            NTBException {
        super(newServiceName, newTransCode);
    }

    private int time2Int(String timeStr){
        if(timeStr == null)return 0;
        timeStr = Utils.replaceStr(timeStr, ":", "");
        return Integer.parseInt(timeStr);
    }

  //modify by long_zg 2014-12-15 for CR192 batch bob
    /*private void checkTransAvailable(String transCode) throws NTBException {*/
    private void checkTransAvailable(String transCode,boolean fullDay,boolean isFX) throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();
    	CutOffTimeService cutOffTimeService = (CutOffTimeService)appContext.getBean("CutOffTimeService");

        // checkTransAvailable, add by hjs
    	//modify by long_zg 2014-12-15 for CR192 batch bob
    	/*cutOffTimeService.checkTransAvailable(transCode);*/
    	cutOffTimeService.checkTransAvailable(transCode,fullDay,isFX);

        /**
        if(hostTimeTableXML == null) return true;

        XMLElement timeSetting = hostTimeTableXML.findNodeByAtrribute("transaction", "id", transCode);

        if(timeSetting == null) return true;

        Calendar cal = Calendar.getInstance();
        boolean available = false;

        //�������ж�
        String dayOfWeek =  String.valueOf(cal.get(cal.DAY_OF_WEEK)-1);
        XMLElement openRange = timeSetting.findNodeByAtrribute("open", "DAY_OF_WEEK", dayOfWeek);
        if(openRange != null){
            String fromStr = openRange.getAttribute("from");
            String toStr = openRange.getAttribute("to");
            int fromInt = time2Int(fromStr);
            int toInt = time2Int(toStr);
            int nowInt = time2Int(getCurrentTime());
            if (nowInt > fromInt && nowInt < toInt) {
                available = true;
            }
            if(!available){
                throw new NTBException("err.sys.HostTxnNotAvailable", new Object[]{fromStr, toStr});
            }
        }else{
            throw new NTBException("err.sys.HostTxnNotAvailableToday");
        }
        */
    }

    public Map doTransaction(Map toData) throws NTBException {

        //����Ƿ���ʱ����
    	//modify by long_zg 2014-12-15 for CR192 batch bob begin
        /*checkTransAvailable(transCode);*/
    	boolean fullDay = false ;
    	boolean isFX = false;//foreign exchange
    	String bankFullDay = Config.getProperty("bankFullDayCurrency") ;
    	String otherFullDay = Config.getProperty("otherBankFullDayCurrency") ;
    	
    	String checkStr = "" ;
    	
    	String currency = (String)toData.get("CHECK_FULLDAY_CURRENCY") ;
    	String fromCurrency = (String)toData.get("CHECK_FULLDAY_CURRENCY_FROM") ;
    	String toCurrency = (String)toData.get("CHECK_FULLDAY_CURRENCY_TO") ;
    	String checkFlag = (String)toData.get("CHECK_FULLDAY_CURRENCY_FLAG") ;
    	if("B".equals(checkFlag)){
    		checkStr = bankFullDay ;
    	} else if("O".equals(checkFlag)){
    		checkStr = otherFullDay ;
    	}
    	if(null !=currency && !"".equals(currency) && null!=checkStr && !"".equals(checkStr)){
    		
    		if(0<=checkStr.indexOf(currency) && "O".equals(checkFlag)){
    			fullDay = true ;
    		}
    		if("B".equals(checkFlag) && toCurrency!=null && !"".equals(toCurrency)
    				&& 0<=checkStr.indexOf(currency) 
    				&& 0<=checkStr.indexOf(toCurrency) ){
    			fullDay = true ;
    		}
    	}
    	if(fromCurrency!=null && toCurrency!= null && !fromCurrency.equals(toCurrency)){ 		
    		isFX=true;
    	}
    	checkTransAvailable(transCode,fullDay,isFX) ;
    	//modify by long_zg 2014-12-15 for CR192 batch bob end

        //���� TELLER_NO �� UNIQUE_SEQUENCE
        if(toData.get("TELLER_NO") == null ||toData.get("UNIQUE_SEQUENCE") == null){
            String refNo = (String)toData.get("LOCAL_TRANSACTION_REF");
            boolean refNoStillValid = false;
            if(refNo != null){
                String currentDate = CibTransClient.getCurrentDate();
                //�ж��Ƿ�����refNo
                if(refNo.length()==18 && refNo.startsWith("TH") && refNo.endsWith(currentDate)){
                    refNoStillValid = true;
                }
            }
            //����ǽ����refNo����Ҫһ���µ�RefNo
            if(!refNoStillValid){
                refNo = CibIdGenerator.getRefNoForTransaction();
            }
            setTellerSeqByRefNo(refNo);
        }

        //��ʼ������
        toData.putAll(alpha8Map);

        Map fromData = super.doTransaction(toData, new CibPacketHandler());

        /*String msg1 = (String) fromData.get("RETURN_MESSAGE_NO1");
        String msg2 = (String) fromData.get("RETURN_MESSAGE_NO2");
        String msg3 = (String) fromData.get("RETURN_MESSAGE_NO3");
        String msg4 = (String) fromData.get("RETURN_MESSAGE_NO4");
        String msg5 = (String) fromData.get("RETURN_MESSAGE_NO5");*/
        String errorCodeString = (String) fromData.get("MSG_CODE");
//        String rejectCode = msg1 + msg2 + msg3 + msg4 + msg5;
        String rejectCode = errorCodeString;
        fromData.put("REJECT_CODE", rejectCode);
        if (!this.isSucceed()) {
        	// add by hjs 2006-11-16
        	/*if (!Utils.null2EmptyWithTrim(msg1).equals("") && msg1.equals("972")) {
        		this.setIsSucceed(true);

        	} else {*/
                if(!Utils.null2EmptyWithTrim(rejectCode).equals("") && !rejectCode.equals("000")){
//              if(!Utils.null2EmptyWithTrim(msg1).equals("") && !msg1("000")){
                    errorArray.addError(rejectCode);
//                	errorArray.addError("System busy:"+rejectCode);
                }
                /*if(!Utils.null2EmptyWithTrim(msg2).equals("") && !msg2.equals("000")){
                    errorArray.addError(msg2);
                }
                if(!Utils.null2EmptyWithTrim(msg3).equals("") && !msg3.equals("000")){
                    errorArray.addError(msg3);
                }
                if(!Utils.null2EmptyWithTrim(msg4).equals("") && !msg4.equals("000")){
                    errorArray.addError(msg4);
                }
                if(!Utils.null2EmptyWithTrim(msg5).equals("") && !msg5.equals("000")){
                    errorArray.addError(msg5);
                }
        	}*/
        }
        Log.debug("Parse Result = " + fromData.toString());
        return fromData;
    }

    public static String getCurrentDate() {
        //ϵͳʱ��
        Date date = new Date();
        DateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMdd");
        String currentDate = dateFormat.format(date);

        return currentDate;
    }

    public static String getCurrentTime() {
        //ϵͳʱ��
        Date date = new Date();
        DateFormat timeFormat = new java.text.SimpleDateFormat("HHmmss");
        String currentTime = timeFormat.format(date);

        return currentTime;
    }

    private void setTellerSeqByRefNo(String refNo) {
        alpha8Map.put("TELLER_NO", refNo.substring(2, 6));
        alpha8Map.put("UNIQUE_SEQUENCE", refNo.substring(6, 10));
    }

    public void setAlpha8(CorpUser user, String txnNature, String accType,
                          String refNo) {
        String userId = user.getUserId();
        String corpId = user.getCorpId();
        String lang = user.getLanguage().toString();//add by linrui for mul-language to host 20171106
        
        alpha8Map.put("TRANSACTIONS_TIME", getCurrentTime());
        alpha8Map.put("CORPORATION_ID", corpId);
        alpha8Map.put("EBANKING_ID", userId);
        alpha8Map.put("LANGUAGE_CODE", transferLang(lang));//mod by linrui for mul-language to host 20171106 "E"
        alpha8Map.put("TRANSACTION_NATURE", txnNature);
        alpha8Map.put("TRANSFER2ACC_TYPE", accType);
        alpha8Map.put("LOCAL_TRANSACTION_REF", refNo);
        alpha8Map.put("51_TRANSACTION_FLAG", " ");
    }

    public void setAlpha8(CorpUser user, String txnNature, String accType,
                          String refNo, String index) {
        String userId = user.getUserId();
        String corpId = user.getCorpId();
        String lang = user.getLanguage().toString();//add by linrui for mul-language to host 20171106
        
        alpha8Map.put("TRANSACTIONS_TIME" + index, getCurrentTime());
        alpha8Map.put("CORPORATION_ID" + index, corpId);
        alpha8Map.put("EBANKING_ID" + index, userId);
        alpha8Map.put("LANGUAGE_CODE" + index, transferLang(lang));//mod by linrui for mul-language to host 20171106 "E"
        alpha8Map.put("TRANSACTION_NATURE" + index, txnNature);
        alpha8Map.put("TRANSFER2ACC_TYPE" + index, accType);
        alpha8Map.put("LOCAL_TRANSACTION_REF" + index, refNo);
        alpha8Map.put("51_TRANSACTION_FLAG", " ");
    }

    public void setAlpha8_for51xx(CorpUser user, String txnNature,
                                  String accType,
                                  String refNo, String flag_51xx) {
        String userId = user.getUserId();
        String corpId = user.getCorpId();
        String lang = user.getLanguage().toString();//add by linrui for mul-language to host 20171106
        
        alpha8Map.put("TRANSACTIONS_TIME", getCurrentTime());
        alpha8Map.put("CORPORATION_ID", corpId);
        alpha8Map.put("EBANKING_ID", userId);
        alpha8Map.put("LANGUAGE_CODE", transferLang(lang));//mod by linrui for mul-language to host 20171106 "E"
        alpha8Map.put("TRANSACTION_NATURE", txnNature);
        alpha8Map.put("TRANSFER2ACC_TYPE", accType);
        alpha8Map.put("LOCAL_TRANSACTION_REF", refNo);
        alpha8Map.put("51_TRANSACTION_FLAG", flag_51xx);
    }

    /*
         From Currency              To Currency             Amount
     Local Currency (MOP)	    Foreign Currency 	        0
     Foreign Currency 	        Local Currency (MOP)	    To Amount
     Local Currency (MOP)	    Local Currency (MOP)	    0
     Foreign Currency A (HKD)	Foreign Currency A (HKD)	0
     Foreign Currency A (YEN)	Foreign Currency B (USD)	0
     */
    public BigDecimal getExchangeIn(String fromCurrency, String toCurrency, BigDecimal toAmount) {
        if (!fromCurrency.equals("MOP") && toCurrency.equals("MOP")) {
            return toAmount;
        } else {
            return new BigDecimal("0");
        }
    }

    /*
         From Currency              To Currency                Amount
     Local Currency (MOP)	    Foreign Currency (HKD)	    From Amount
     Foreign Currency (HKD)	    Local Currency (MOP)	    From  Amount
     Local Currency (MOP)	    Local Currency (MOP)	    0
     Foreign Currency A (HKD)	Foreign Currency A (HKD)	0
     Foreign Currency A (HKD)	Foreign Currency B (USD)	From Amount
     */
    public BigDecimal getExchangeOut(String fromCurrency, String toCurrency, BigDecimal fromAmount) {
        // Jet modify 2008-04-16
        if (fromCurrency.equals("MOP") && toCurrency.equals("MOP")) {
            return new BigDecimal("0");
        } else if(!fromCurrency.equals("MOP") && !toCurrency.equals("MOP") && fromCurrency.equals(toCurrency)){
            return new BigDecimal("0");        	
        } else {
        	return fromAmount;
        }        
    }

    /*
       From Currency                 To Currency             Amount
     Local Currency (MOP)	    Foreign Currency (HKD)	    To Amount
     Foreign Currency (HKD)	    Local Currency (MOP)	    0
     Local Currency (MOP)	    Local Currency (MOP)	    0
     Foreign Currency A (HKD)	Foreign Currency A (HKD)	0
     Foreign Currency A (HKD)	Foreign Currency B (USD)	To Amount
     */
    public BigDecimal getPassbookBalance(String fromCurrency, String toCurrency, BigDecimal toAmount) {
    	// Jet modify 2008-04-16
    	if (toCurrency.equals("MOP")){
    		return new BigDecimal("0");
    	} else if (toCurrency.equals(fromCurrency)){
    		return new BigDecimal("0");
    	} else {
        	return toAmount;
    	}
    }

    class CibPacketHandler implements PacketHandler {
        public CibPacketHandler() {
        }

        public boolean processPacket(String transId, Map data) throws
                NTBHostException {
            String responseCode = (String) data.get("RESPONSE_CODE");
            RBFactory rb = RBFactory.getInstance("hostResponse");
            String accepted = rb.getString(transId);

            if (accepted == null) {
                Log.warn("No response code definition for transaction " +
                         transId);
                return false;
            }
            if (!accepted.equals(responseCode)) {
                return false;
            }
            return true;
        }
    }


    public NTBErrorArray getErrorArray() {
        return errorArray;
    }
    //add by linrui for mul-language 20171106 to host
    public static String transferLang(String lang){
       if(lang.trim().equalsIgnoreCase("")||lang.trim()==null){
    	   return Constants.US_HOST;
       }
       else if(lang.trim().equalsIgnoreCase(Constants.HK)||lang.trim().equalsIgnoreCase(Constants.TW)){
 		   return Constants.HK_TW_HOST;
 	   }
 	   else if (lang.trim().equalsIgnoreCase(Constants.CN)){
 		   return Constants.CN_HOST;
 	   }
 	   else if(lang.trim().equalsIgnoreCase(Constants.PT)){
 		   return Constants.PT_HOST;
 	   }else 
 		   return Constants.US_HOST; 	   
    }
}
