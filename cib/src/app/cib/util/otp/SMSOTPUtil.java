package app.cib.util.otp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.springframework.context.ApplicationContext;

import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.BillPayment;
import app.cib.bo.txn.TransferBank;
import app.cib.core.CibIdGenerator;
import app.cib.core.CibTransClient;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.txn.BillPaymentService;
import app.cib.service.txn.TransferService;
//import app.cib.util.smsclient.LogClient;
//import app.cib.util.smsclient.SMSClient;
import app.cib.util.Constants;
import app.cib.util.smsclient.log.LogSmsRequest;
import app.cib.util.smsclient.sms.SmsRequest;
import app.cib.util.smsclient.sms.SmsResponse;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

public class SMSOTPUtil {
	private static Map smsInstance = new HashMap();
	public static String clientSystemId = "BOL";
	public static final String SMS_RETURN_CODE_RIGHT = "0000";
	public static String sysReTryCountStr = Config
			.getProperty("app.sms.otpRetryCount");

	public static String sysReSendCountStr = Config
			.getProperty("app.sms.otpResendCount");

	public static StringBuffer OTP_N = new StringBuffer("0,1,2,3,4,5,6,7,8,9");

	public static StringBuffer OTP_U = new StringBuffer(
			"A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z");
	public static StringBuffer OTP_L = new StringBuffer(
			"a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z");
	protected static final String ERM_HAVE_NO_PHNOE = "ERM2822";
	protected static final String ERM_EXCEED_RESEND_COUNT = "ERM2820";
	protected static final String ERM_EXCEED_RETRY_COUNT = "ERM2821";
	protected static final String ERM_EXCEED_VALIDITY_PERIOD = "ERM2823";
	protected static final String ERM_INVALID_ONE_TIME_PASSWORD = "ERM2824";
	private static final String TABLE_SMS_OTP_LOG = "SMS_OTP_LOG";

	protected static String thisName = "";
	protected static String errMsg = "";
	private static final String BLOCK_ACCT_AT_FUNDTRANDER = "FT";

	public SMSOTPUtil() {
		thisName = getClass().getName();
	}

	public static String getSmsContect(Map dataMap, String lang) {
		String smsPath = Config.getProperty("app.sms.smsContent");

		Log.info("SMSOTPUtil getSmsContect smsPath:" + smsPath);

		if (smsPath.endsWith(".properties")) {
			String[] smsPathArray = smsPath.split(".properties");
			smsPath = smsPathArray[0];
			Log.info("SMSOTPUtil getSmsContect smsPathArray:" + smsPath);
		}

		PropertyContent pc = PropertyContent.getInstance(smsPath, Format.transferLang(lang));

		String content = pc.getProperty("TRANS_OTP");

		Log.info("SMSOTPUtil getSmsContect content:" + content);

		Iterator imap = dataMap.entrySet().iterator();
		while (imap.hasNext()) {
			Map.Entry entry = (Map.Entry) imap.next();

			String key = (String) entry.getKey();
			key = "${" + key + "}";
			String value = entry.getValue().toString();
			content = Utils.replaceStr(content, key, value);
		}

		Log.info("SMSOTPUtil getSmsContect return content:" + content);

		return content;
	}

	public static String sendOtp(String language, String mobile,
			String sessionId, String transactionType, String transactionAmount,
			String transactionTime, String cardNo, String cif, String function,
			String ipAddress, String userId, String tranCode) throws Exception {
		//new org.apache.cxf.bus.CXFBusFactory().createBus(); 
		
		/*String smsFlowNo = CibIdGenerator.getIdForOperation("SMS_FLOW");
		String transId = CibIdGenerator.getIdForOperation("SMS_OTP_LOG");

		String otp = generateOTP();
		Date newTime = new Date();

		SMSOTPObject otpObject = new SMSOTPObject();
		otpObject.setLanguage(language);
		otpObject.setMobile(mobile);
		otpObject.setSessionId(sessionId);
		otpObject.setTransactionAmount(transactionAmount);
		otpObject.setTransactionId(transId);
		otpObject.setTransactionTime(transactionTime);
		otpObject.setTransactionType(transactionType);
		otpObject.setOtp(otp);
		otpObject.setCreateTime(newTime);
		// add by long_zg 20140724 for CR194
		otpObject.setSendTimes(1);

		SmsRequest smsRequest = generateSmsRequest(otpObject);

		LogSmsRequest logSmsRequest = new LogSmsRequest();
		logSmsRequest.setCardNo(cardNo);
		logSmsRequest.setCif(cif);
		logSmsRequest.setClientSystemId(clientSystemId);

		logSmsRequest.setContent(Utils.bytes2HexStr(smsRequest.getContent()
				.getBytes()));

		logSmsRequest.setFunction(function);
		logSmsRequest.setIpAddress(ipAddress);

		logSmsRequest.setLanguage(language);

		logSmsRequest.setMobileNo(mobile);

		logSmsRequest.setTransId(transId);
		logSmsRequest.setUserId(userId);

		// add by long_zg 20140724 for CR194
		logSmsRequest.setSendTimes(otpObject.getSendTimes());

		Log.info("SMSOTPUtil sendOtp content:" + smsRequest.getContent()
				+ "; transId:" + otpObject.getTransactionId());

		LogClient wsLogClient = LogClient.getInstance();

		if ((mobile == null) || ("".equals(mobile))) {
			logSmsRequest.setEvent("retrieve mobile number fail");
			wsLogClient.logSms(logSmsRequest);
			return "-1";
		}

		logSmsRequest.setEvent("retrieve mobile number successfully");
		wsLogClient.logSms(logSmsRequest);

		String smsServer1 = Config.getProperty("app.sms.server.url_1");
		String smsServer2 = Config.getProperty("app.sms.server.url_2");

		SMSClient smsClient = SMSClient.getInstance();
		SMSClient.setMainServer(smsServer1);
		if (null != smsServer2 && !"".equals(smsServer2)) {
			SMSClient.setSecondaryServer(smsServer2);
		}

		SmsResponse response = smsClient.sendSms(smsRequest);

		String statusCode = response.getStatusCode();

		writeLogToSmsOtpLog(transId, cif, userId, language, tranCode, "", "",
				"", "", function, "0");

		Log.info("Sending otp = " + otpObject.getOtp());

		wsLogClient.logSms(logSmsRequest);

		otpObject.setLogSmsRequest(logSmsRequest);
		smsInstance.put(smsFlowNo, otpObject);

		return smsFlowNo;*/
		//add by linrui for flow the job begin
		String otp = generateOTP();
		Date newTime = new Date();
//		String smsFlowNo = CibIdGenerator.getIdForOperation("SMS_FLOW");
		String transId = CibIdGenerator.getIdForOperation("SMS_OTP_LOG");
		SMSOTPObject otpObject = new SMSOTPObject();
		otpObject.setLanguage(language);
		otpObject.setMobile(mobile);
		otpObject.setSessionId(sessionId);
		otpObject.setTransactionAmount(transactionAmount);
		otpObject.setTransactionId(transId);
		otpObject.setTransactionTime(transactionTime);
		otpObject.setTransactionType(transactionType);
		otpObject.setOtp(otp);
		otpObject.setCreateTime(newTime);
		otpObject.setSendTimes(1);
		smsInstance.put("123456", otpObject);
		return "123456";
	}
	//add by linrui for send sms by host
	public static String sendOtpSMS(String language, String mobile,
			String sessionId, String transactionType, String transactionAmount,
			String transactionTime, String cardNo, String cif, String function,
			String ipAddress, String userId, String tranCode) throws Exception {
		
		String smsFlowNo = CibIdGenerator.getIdForOperation("SMS_FLOW");
		String transId = CibIdGenerator.getIdForOperation("SMS_OTP_LOG");
		String otp = generateOTP();
		Date newTime = new Date();

		SMSOTPObject otpObject = new SMSOTPObject();
		otpObject.setLanguage(language);
		otpObject.setMobile(mobile);
		otpObject.setSessionId(sessionId);
		otpObject.setTransactionAmount(transactionAmount);
		otpObject.setTransactionId(transId);
		otpObject.setTransactionTime(transactionTime);
		otpObject.setTransactionType(transactionType);
		otpObject.setOtp(otp);
		otpObject.setCreateTime(newTime);
		otpObject.setSendTimes(1);

		SmsRequest smsRequest = generateSmsRequest(otpObject);

		LogSmsRequest logSmsRequest = new LogSmsRequest();
		logSmsRequest.setCardNo(cardNo);
		logSmsRequest.setCif(cif);
		logSmsRequest.setClientSystemId(clientSystemId);
		logSmsRequest.setContent(Utils.bytes2HexStr(smsRequest.getContent().getBytes()));
		logSmsRequest.setFunction(function);
		logSmsRequest.setIpAddress(ipAddress);
		logSmsRequest.setLanguage(language);
		logSmsRequest.setMobileNo(mobile);
		logSmsRequest.setTransId(transId);
		logSmsRequest.setUserId(userId);
		// add by long_zg 20140724 for CR194
		logSmsRequest.setSendTimes(otpObject.getSendTimes());
		Log.info("SMSOTPUtil sendOtp content:" + smsRequest.getContent() + "; transId:" + otpObject.getTransactionId());

		if ((mobile == null) || ("".equals(mobile))) {
			logSmsRequest.setEvent("retrieve mobile number fail");
			return "-1";
		}
		logSmsRequest.setEvent("retrieve mobile number successfully");
		writeLogToSmsOtpLog(transId, cif, userId, language, tranCode, "", "", "", "", function, "0");
		Log.info("Sending otp = " + otpObject.getOtp());
		otpObject.setLogSmsRequest(logSmsRequest);
		smsInstance.put(smsFlowNo, otpObject);
		//send otp begin
		CibTransClient testClient = new CibTransClient("CIB", "MAIL");
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService accService = (CorpAccountService) appContext.getBean("CorpAccountService");
		
			Map toHost = new HashMap();
			Map fromHost = new HashMap();
			String refNo = CibIdGenerator.getRefNoForTransaction();
			toHost.put("transactionType", "T");
			toHost.put("sendType", "S");
			toHost.put("phoneNO", mobile);
			toHost.put("SMStemplateID", getSMSTemplateID("OTP",language));//get OTP template id
			toHost.put("SendDate", "00000000");//all 0 is current date
			toHost.put("SendTime", "000000");//all 0 is current time
			toHost.put("messageLength", 450);
			toHost.put("opt", otpObject.getOtp());
			toHost.put("transactionName", changeTransferType(transactionType,language));//changeTransferType(transactionType));//get transaction name
			toHost.put("REQ_REF", refNo);
			fromHost = testClient.doTransaction(toHost);
			if (!testClient.isSucceed()) {
				throw new NTBHostException(testClient.getErrorArray());
			}
		//end
		return smsFlowNo;
	}
	
	/* Add by long_zg 2019-06-02 UAT6-465 COB锛氱煭淇′氦鏄撻鍨嬬己澶�  begin */
	public static void sendNotificationSMS(String language, String mobile,
			String sessionId, String transactionType, String transactionAmount,
			String transactionTime, /*String cardNo,*/String accountNO, String cif, String currency,
			String ipAddress, String userId, String tranCode) throws Exception {
		
		if(null==mobile || "".equals(mobile)){
			Log.error("have no mobile phone") ;
			return ;
		}
		CibTransClient testClient = new CibTransClient("CIB", "MAIL");
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService accService = (CorpAccountService) appContext.getBean("CorpAccountService");
		
			Map toHost = new HashMap();
			Map fromHost = new HashMap();
			String refNo = CibIdGenerator.getRefNoForTransaction();
			toHost.put("transactionType", "T");
			toHost.put("sendType", "S");
			toHost.put("phoneNO", mobile);
			toHost.put("SMStemplateID", getSMSTemplateIDByTranType(transactionType,language));//get OTP template id
			toHost.put("SendDate", "00000000");//all 0 is current date
			toHost.put("SendTime", "000000");//all 0 is current time
			toHost.put("messageLength", 450);
//			toHost.put("opt", otpObject.getOtp());
			toHost.put("transactionName", changeTransferType(transactionType,language));//changeTransferType(transactionType));//get transaction name
			toHost.put("REQ_REF", refNo);
			toHost.put("currency", currency);
			toHost.put("amount", transactionAmount);
			toHost.put("accountNO", accountNO);
			if(transactionType.equals("LOGGED_IN")){
				toHost.put("channel", changeTransferType(Constants.SMS_TEMPLATE_CHANNEL_WEB_LOGIN,language));
			}else{
				toHost.put("channel", changeTransferType(Constants.SMS_TEMPLATE_CHANNEL_WEB,language));
			}
			fromHost = testClient.doTransaction(toHost);
			if (!testClient.isSucceed()) {
				throw new NTBHostException(testClient.getErrorArray());
			}
	}
	
	public static void sendNotificationSMS(String language, String mobile,
			String sessionId, String transactionType, String transactionAmount,
			String transactionTime, /*String cardNo,*/String accountNO, String cif, String currency,
			String ipAddress, String userId, String tranCode,String serialNumber) throws Exception {
		
		if(null==mobile || "".equals(mobile)){
			Log.error("have no mobile phone") ;
			return ;
		}
		CibTransClient testClient = new CibTransClient("CIB", "MAIL");
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService accService = (CorpAccountService) appContext.getBean("CorpAccountService");
		
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		toHost.put("transactionType", "T");
		toHost.put("sendType", "S");
		toHost.put("phoneNO", mobile);
		toHost.put("SMStemplateID", getSMSTemplateIDByTranType(transactionType,language));//get OTP template id
		toHost.put("SendDate", "00000000");//all 0 is current date
		toHost.put("SendTime", "000000");//all 0 is current time
		toHost.put("messageLength", 450);
		toHost.put("transactionName", changeTransferType(transactionType,language));//changeTransferType(transactionType));//get transaction name
		toHost.put("bussinessReference", serialNumber);
		toHost.put("currency", currency);
		toHost.put("amount", transactionAmount);
		toHost.put("accountNO", accountNO);
		toHost.put("channel", changeTransferType(Constants.SMS_TEMPLATE_CHANNEL_WEB,language));
		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
	}

	public static int retryCheck(String smsFlowNo, String checkOtp) {
		SMSOTPObject optObject = (SMSOTPObject) smsInstance.get(smsFlowNo);

		if ((sysReTryCountStr == null) || ("".endsWith(sysReTryCountStr))) {
			sysReTryCountStr = "3";
		}
		int sysReTryCount = Integer.parseInt(sysReTryCountStr);

		int retryCount = optObject.getRetryCount() + 1;

		optObject.setRetryCount(retryCount);
		smsInstance.put(smsFlowNo, optObject);

		if ((optObject.getOtp().equals(checkOtp))
				|| ((sysReTryCount + 1 == retryCount) && (optObject.getOtp()
						.equals(checkOtp)))) {
			return 0;
		}

		if (sysReTryCount < retryCount) {
			return -1;
		}

		return 1;
	}

	public static int resendCheck(String smsFlowNo) {
		SMSOTPObject otpObject = (SMSOTPObject) smsInstance.get(smsFlowNo);

		if ((sysReSendCountStr == null) || ("".equals(sysReSendCountStr))) {
			sysReSendCountStr = "3";
		}
		int sysReSendCount = Integer.parseInt(sysReSendCountStr);

		int resendCount = otpObject.getResendCount();

		resendCount++;
		otpObject.setResendCount(resendCount);
		if (sysReSendCount <= resendCount) {
			return -1;
		}
		return 1;
	}

	public static String resend(String smsFlowNo) throws Exception {
		SMSOTPObject otpObject = (SMSOTPObject) smsInstance.get(smsFlowNo);

		Log.info("otp = " + otpObject.getOtp());
		// add by long_zg 20140724 for CR194
		otpObject.setSendTimes(otpObject.getSendTimes() + 1);

		SmsRequest smsRequest = generateSmsRequest(otpObject);

		Log.info("SMSOTPUtil resend content:" + smsRequest.getContent()
				+ "; transId:" + otpObject.getTransactionId());

//		SMSClient smsClient = SMSClient.getInstance();
//		SmsResponse response = smsClient.sendSms(smsRequest);

		LogSmsRequest logSmsRequest = otpObject.getLogSmsRequest();
		logSmsRequest.setEvent("customer request to re-send OTP");
		// add by long_zg 20140724 for CR194
		logSmsRequest.setSendTimes(otpObject.getSendTimes());

//		LogClient wsLogClient = LogClient.getInstance();
//		wsLogClient.logSms(logSmsRequest);
		smsInstance.put(smsFlowNo, otpObject);
		return smsFlowNo;
	}
	
	//add by linrui 20190512
	public static String resendSMS(String smsFlowNo) throws Exception {
		SMSOTPObject otpObject = (SMSOTPObject) smsInstance.get(smsFlowNo);
		Log.info("old otp is = " + otpObject.getOtp());
		otpObject.setCreateTime(new Date());//generateOTP
		String newOtp = generateOTP();
		otpObject.setOtp(newOtp);
		Log.info("new otp is = " + newOtp);
		smsInstance.put(smsFlowNo, otpObject);
		//send otp begin
		CibTransClient testClient = new CibTransClient("CIB", "MAIL");
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService accService = (CorpAccountService) appContext.getBean("CorpAccountService");
		
			Map toHost = new HashMap();
			Map fromHost = new HashMap();
			String refNo = CibIdGenerator.getRefNoForTransaction();
			toHost.put("transactionType", "T");
			toHost.put("sendType", "S");
			toHost.put("phoneNO", otpObject.getMobile());
			toHost.put("SMStemplateID", getSMSTemplateID("OTP",otpObject.getLanguage()));//get OTP template id
			toHost.put("SendDate", "00000000");//all 0 is current date
			toHost.put("SendTime", "000000");//all 0 is current time
			toHost.put("messageLength", 450);
			toHost.put("opt", otpObject.getOtp());
			toHost.put("transactionName", changeTransferType(otpObject.getTransactionType(),otpObject.getLanguage()));//changeTransferType(transactionType));//get transaction name
			toHost.put("REQ_REF", refNo);
			fromHost = testClient.doTransaction(toHost);
			if (!testClient.isSucceed()) {
				throw new NTBHostException(testClient.getErrorArray());
			}
		return smsFlowNo;
	}

	public static boolean checkValidityPeriod(String smsFlowNo) {
		SMSOTPObject otpObject = (SMSOTPObject) smsInstance.get(smsFlowNo);

		String timeOutStr = Config.getProperty("app.sms.timeout");
		if ((timeOutStr == null) || ("".equals(timeOutStr))) {
			timeOutStr = "30";
		}
		int timeOut = Integer.parseInt(timeOutStr);
		Date nowTime = new Date();

		Date createTime = otpObject.getCreateTime();
		int intervalTime = getSecondTween(nowTime, createTime);

		if (intervalTime > timeOut) {
			return false;
		}
		return true;
	}

	public static SmsRequest generateSmsRequest(SMSOTPObject otpObject) {
		Map dataMap = new HashMap();
		dataMap.put("OTP", otpObject.getOtp());

		String content = getSmsContect(dataMap, otpObject.getLanguage());

		Log.info("SMSOTPUtil generateSmsRequest content:" + content);

		SmsRequest smsRequest = new SmsRequest();

		smsRequest.setClientSystemId(clientSystemId);
		smsRequest.setContent(otpObject.getOtp());
		smsRequest.setLanguage(otpObject.getLanguage());
		smsRequest.setMobileNo(otpObject.getMobile());
		smsRequest.setTransactionId(otpObject.getTransactionId());
		smsRequest.setTransactionTime(otpObject.getTransactionTime());
		smsRequest.setContent(content);
		// add by long_zg 20140724 for CR194
		smsRequest.setSendTimes(otpObject.getSendTimes());

		return smsRequest;
	}

	public static String generateOTP() {
		String otpType = Config.getProperty("app.sms.otpType");
		String[] arr = (String[]) null;
		if ("U".equals(otpType)) {
			arr = OTP_U.toString().split(",");
		} else if ("L".equals(otpType)) {
			arr = OTP_L.toString().split(",");
		} else if ("NU".equals(otpType)) {
			String otpStr = "," + OTP_U;
			arr = otpStr.toString().split(",");
		} else if ("NL".equals(otpType)) {
			String otpStr = "," + OTP_L;
			arr = otpStr.toString().split(",");
		} else if ("UL".equals(otpType)) {
			String otpStr = "," + OTP_L;
			arr = otpStr.toString().split(",");
		} else if ("NUL".equals(otpType)) {
			String otpStr = "," + OTP_U + "," + OTP_L;
			arr = otpStr.toString().split(",");
		} else {
			arr = OTP_N.toString().split(",");
		}

		List pswdList = new ArrayList();
		StringBuffer b = new StringBuffer();

		String lengthStr = Config.getProperty("app.sms.optLength");
		if ((lengthStr == null) || ("".equals(lengthStr))) {
			lengthStr = "6";
		}

		int length = Integer.parseInt(lengthStr);

		for (int i = 0; i < length; i++) {
			Random r = new Random();
			int k = r.nextInt();
			b.append(String.valueOf(arr[r.nextInt(arr.length)]));
		}
		return b.toString();
	}

	public static void check(SMSReturnObject returnObject, String smsFlowNo,
			String otp, String exceedResend, String lang) throws NTBException{
		Log.info("SMSOTPUtil check smsFlowNo : " + smsFlowNo);

		SMSOTPObject otpObject = (SMSOTPObject) smsInstance.get(smsFlowNo);
		LogSmsRequest logSmsRequest = otpObject.getLogSmsRequest();
//		LogClient wsLogClient = LogClient.getInstance();
		returnObject.setReturnFlag(false);
		returnObject.setReturnValid(true);

		Boolean periodValid = Boolean.valueOf(checkValidityPeriod(smsFlowNo));
		if (!periodValid.booleanValue()) {
			Log.info("SMSOTPUtil check periodInvalid");

			returnObject.setReturnFlag(false);
			returnObject.setReturnValid(false);
			returnObject.setErrorFlag("P");
			
			logSmsRequest.setEvent("OTP is invalid");
//			wsLogClient.logSms(logSmsRequest);

			smsInstance.remove(smsFlowNo);
			Log.info("SMSOTPUtil check exceed period remove smsFlowNo");

			updateLogToSmsOtpLog(otpObject.getTransactionId(), null, null, "Y",
					null);
			returnObject.setReturnErr("err.sms.OTPInvalid");
			return;
		//	throw new NTBException("err.sms.OTPInvalid");
		}

		if ("Y".equals(exceedResend)) {
			Log.info("SMSOTPUtil check exceed Resend");

			returnObject.setReturnFlag(false);
			returnObject.setReturnValid(false);
			//returnObject.setErrorFlag("R");
			
			logSmsRequest.setEvent("customer exceed re-send OTP count");
//			wsLogClient.logSms(logSmsRequest);

			smsInstance.remove(smsFlowNo);
			Log.info("SMSOTPUtil check exceed Resend remove smsFlowNo");

			updateLogToSmsOtpLog(otpObject.getTransactionId(), null, "Y", null,
					null);
		 	throw new NTBException("SMSOTPUtil check exceed Resend");
		}

		int retryCheck = retryCheck(smsFlowNo, otp);
		if (1 == retryCheck) {
			Log.info("SMSOTPUtil check invalid otp");

			String err = "ERM2824";
			returnObject.setReturnFlag(true);
			returnObject.setReturnValid(false);
			//returnObject.setErrorFlag("W");
			logSmsRequest.setEvent("customer input wrong OTP");
//			wsLogClient.logSms(logSmsRequest);
			//returnObject.setReturnErr("err.sms.OTPWrong");
			if(otp == null || "".equals(otp)){
				throw new NTBException("err.sms.OTPEmpty");
			}
		    throw new NTBException("err.sms.OTPWrong");
		}
		if (-1 == retryCheck) {
			Log.info("SMSOTPUtil check exceed retryCount");
			returnObject.setReturnFlag(false);
			returnObject.setReturnValid(false);
			returnObject.setErrorFlag("R");
			
			logSmsRequest.setEvent("exceed input re-try count");
//			wsLogClient.logSms(logSmsRequest);

			smsInstance.remove(smsFlowNo);
			Log.info("SMSOTPUtil check exceed retryCount remove smsFlowNo");
			updateLogToSmsOtpLog(otpObject.getTransactionId(), "Y", null, null,
					null);
			returnObject.setReturnErr("err.sms.ExceedsResendOTPCount");
			//return;
			throw new NTBException("err.sms.ExceedsResendOTPCount");
		}

		logSmsRequest.setEvent("customer input correct OTP");
//		wsLogClient.logSms(logSmsRequest);
	}

	public static void writeLogToSmsOtpLog(String otpTransId, String cif,
			String userId, String lang, String tranCode, String exceedRetry,
			String exceedResend, String exceedPeriod, String refId,
			String function, String status) {
		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext
				.getBean("genericJdbcDao");

		String sqlStr = "insert into "
				+ "SMS_OTP_LOG(OTP_TRANS_ID, CIF, USER_ID, LANG, TRAN_CODE, EXCEED_RETRY, "
				+ "EXCEED_RESEND, EXCEED_PERIOD, REF_ID, FUNCTION, STATUS)"
				+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			dao.update(sqlStr, new Object[] { otpTransId, cif, userId, Format.transferLang(lang),
					tranCode, exceedRetry, exceedResend, exceedPeriod, refId, function, status });
		} catch (Exception e) {
			Log.error("SMSUtil:Error inserting into the log ", e);
		}
	}

	public static void updateLogToSmsOtpLog(String otpTransId,
			String exceedRetry, String exceedResend, String exceedPeriod,
			String refId) {

		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext
				.getBean("genericJdbcDao");

		String sqlStr = "update SMS_OTP_LOG set STATUS='0' ";
		
		
		if ((exceedRetry != null) && (!"".equals(exceedRetry))) {
		      sqlStr = sqlStr + ",EXCEED_RETRY='" + exceedRetry + "'";
		    }
		    if ((exceedResend != null) && (!"".equals(exceedResend))) {
		      sqlStr = sqlStr + ",EXCEED_RESEND='" + exceedResend + "'";
		    }
		    if ((exceedPeriod != null) && (!"".equals(exceedPeriod))) {
		      sqlStr = sqlStr + ",EXCEED_PERIOD='" + exceedPeriod + "'";
		    }
		    if ((refId != null) && (!"".equals(refId))) {
		      sqlStr = sqlStr + ",REF_ID='" + refId + "'";
		    }
	    sqlStr += " where  OTP_TRANS_ID=?";
		    
		try {
			dao.update(sqlStr, new Object[] { otpTransId });
		} catch (Exception e) {
			Log.error("SMSUtil:Error updating exceed info the log ", e);
		}
	}

	public static void updateRefIdToSmsOtpLog(String smsFlowNo, String refId) {
		if ((!"0".equals(smsFlowNo)) && (smsFlowNo != null)
				&& (!"".equals(smsFlowNo))) {
			SMSOTPObject otpObject = (SMSOTPObject) smsInstance.get(smsFlowNo);

			ApplicationContext appContext = Config.getAppContext();
			GenericJdbcDao dao = (GenericJdbcDao) appContext
					.getBean("genericJdbcDao");

			String sqlStr = "update SMS_OTP_LOG set STATUS='0',REF_ID=? where OTP_TRANS_ID=?";
			try {
				dao.update(sqlStr,
						new Object[] { refId, otpObject.getTransactionId() });
			} catch (Exception e) {
				Log.error("SMSUtil:Error updating ref ID to the log", e);
			} finally{
				Log.info("SMSUtil:Removed sms instance(SMS Flow No:"
						+ smsFlowNo + ")");
				smsInstance.remove(smsFlowNo);
			}
		}
	}

	public static int getSecondTween(Date date1, Date date2) {
		long mill1 = date1.getTime();
		long mill2 = date2.getTime();
		return (int) ((mill1 - mill2) / 1000L);
	}
	
	public static String getLang(Locale locale){
		String lang="E";
		if(locale != null){
			String lang1 = locale.getLanguage();
			if("zh".equals(lang1)){
				lang = "C";
			}
			if("pt".equals(lang1)){
				lang = "P";
			}
		}
		return lang;
		
	}

	public static SMSOTPObject getOtpObject(String smsFlowNo) {
		return (SMSOTPObject) smsInstance.get(smsFlowNo);
	}
	
	public static String getFuncName(String txnType) throws NTBException{
		String funcName = "";
		if (txnType.equals("TRANSFER_MACAU")){
			funcName = "app.cib.action.txn.TransferInMacau";
		}
		if (txnType.equals("TRANSFER_OVERSEAS")){
			funcName = "app.cib.action.txn.TransferInOverseas";
		}
		if (txnType.equals("TRANSFER_BANK_1TON")){
			funcName = "app.cib.action.txn.TransferInBankStM";
		}
		if (txnType.equals("TRANSFER_BANK_NTO1")){
			funcName = "app.cib.action.txn.TransferInBankMtS";
		}
		if (txnType.equals("TRANSFER_MACAU_N")){
			funcName = "app.cib.action.txn.TransferInMacauStM";
		}
		if (txnType.equals("TRANSFER_OVERSEAS_N")){
			funcName = "app.cib.action.txn.TransferInOverseaStM";
		}
		if (txnType.equals("DELIVERY_TO_SUB")||txnType.equals("TRANSFER_BANK")
				||txnType.equals("TRANFER_BETWEEN_SUB")||txnType.equals("REPETRIATE_FROM_SUB")){
			if (txnType.equals("DELIVERY_TO_SUB")){
				funcName = "app.cib.action.txn.CorpDelivery";
			}
			if (txnType.equals("TRANSFER_BANK")){
				funcName = "aapp.cib.action.txn.TransferInBank";
			}
			if (txnType.equals("TRANFER_BETWEEN_SUB")){
				funcName = "app.cib.action.txn.CorpSubsidiary";
			}
			if (txnType.equals("REPETRIATE_FROM_SUB")){
				funcName = "app.cib.action.txn.CorpRepatriate";
			}
	    }
		if (txnType.equals("SCHEDULE_TXN")||txnType.equals("NEW_SCHEDULE_TXN")
				||txnType.equals("EDIT_SCHEDULE_TXN")||txnType.equals("BLOCK_SCHEDULE_TXN")
				||txnType.equals("UNBLOCK_SCHEDULE_TXN")||txnType.equals("REMOVE_SCHEDULE_TXN")){
			
			funcName="app.cib.action.txn.ScheduleTransfer";
			
		}
		if (txnType.equals("BATCH_PAYMENT")){
			funcName = "app.cib.action.txn.TemplatePayment";
		}
		if(txnType.equals("GENERAL_PAYMENT")||txnType.equals("CARD_PAYMENT")
				||txnType.equals("TAX_PAYMENT")){
			
			funcName = "app.cib.action.txn.BillPayment";
		}
		
		if(txnType.equals("STOP_CHEQUE")){
			funcName = "app.cib.action.srv.StopCheque";
		}
		if(txnType.equals("CHEQUE_PROTECTION")){
			funcName = "app.cib.action.srv.ProtectionCheque";
		}
		if(txnType.equals("BANK_DRAFT")){
			funcName = "app.cib.action.srv.BankRequest";
		}
		
		if(txnType.equals("CASHIER_ORDER")){
			funcName = "app.cib.action.srv.CashOrder";
		}
		
		if (txnType.equals("AUTOPAY_ADD")||txnType.equals("AUTOPAY_EDIT")||txnType.equals("AUTOPAY_DELETE")){
			funcName = "app.cib.action.txn.Autopay";
		}
		
		if (txnType.equals("CHEQUE_BOOK_REQUEST")){
			funcName = "app.cib.action.srv.ChequeBookRequest";
		}
		
		/* Add by long_zg 2019-05-30 UAT6-514 COB - Time Deposit Approval 鐭俊 For 涔嬪緦閮芥槸婕� begin */
		if ("NEW_TIME_DEPOSIT".equals(txnType)){
			funcName = "app.cib.action.txn.openTimeDeposit";
		}
		if( "WITHDRAW_DEPOSIT".equals(txnType)){
			funcName = "app.cib.action.txn.timeDepositWithDrawal" ;
		}
		/* Add by long_zg 2019-05-30 UAT6-514 COB - Time Deposit Approval 鐭俊 For 涔嬪緦閮芥槸婕� end */

		
		/* Add by long_zg 2019-06-03 UAT6-553 COB - Transfer 鍚屽悕鎴惰綁璩�  begin */
		if ("TRANSFER_OWN_ACCOUNT".equals(txnType)){
			funcName = "app.cib.action.txn.toMyAccount";
		}
		if ("TRANSFER_BANK".equals(txnType)){
			funcName = "app.cib.action.txn.toMDBAccount";
		}
		/* Add by long_zg 2019-06-03 UAT6-553 COB - Transfer 鍚屽悕鎴惰綁璩�  end */
		
		return funcName;
	}
	
	public static String getTranCode(String txnType, String txnId) throws NTBException{
		String transCode = "";
		if (txnType.equals("TRANSFER_MACAU")){
			transCode = "ZJ55";
		}
		if (txnType.equals("TRANSFER_OVERSEAS")){
			transCode = "ZJ55";
		}
		if (txnType.equals("TRANSFER_BANK_1TON")){
			transCode = "51XX";
		}
		if (txnType.equals("TRANSFER_BANK_NTO1")){
			transCode = "51XX";
		}
		if (txnType.equals("TRANSFER_MACAU_N")){
			transCode = "XJ55";
		}
		if (txnType.equals("TRANSFER_OVERSEAS_N")){
			transCode = "XJ55";
		}
		if (txnType.equals("DELIVERY_TO_SUB")||txnType.equals("TRANSFER_BANK")
				||txnType.equals("TRANFER_BETWEEN_SUB")||txnType.equals("REPETRIATE_FROM_SUB")){
			
			ApplicationContext appContext = Config.getAppContext();
			TransferService transferService = (TransferService) appContext
					.getBean("TransferService");
			TransferBank transferBank = transferService.viewInBANK(txnId);
			String fromAcctypeCode="";
			String toAcctypeCode="";
			
			/* Modify by long_zg 2019-05-28 UAT6-113 COB 绗笁鑰呰綁璩け鏁� begin */
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
			if (CorpAccount.ACCOUNT_TYPE_CURRENT.equals(transferBank.getToAcctype()) || 
					CorpAccount.ACCOUNT_TYPE_OVER_DRAFT.equals(transferBank.getToAcctype())) {
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
			
			/* Modify by long_zg 2019-05-28 UAT6-113 COB 绗笁鑰呰綁璩け鏁� end */
			
			transCode = "51" + fromAcctypeCode + toAcctypeCode;
	    }
		if (txnType.equals("SCHEDULE_TXN")||txnType.equals("NEW_SCHEDULE_TXN")
				||txnType.equals("EDIT_SCHEDULE_TXN")||txnType.equals("BLOCK_SCHEDULE_TXN")
				||txnType.equals("UNBLOCK_SCHEDULE_TXN")||txnType.equals("REMOVE_SCHEDULE_TXN")){
			
			transCode="";
			
		}
		if (txnType.equals("BATCH_PAYMENT")){
			transCode = "59CC";
		}
		if(txnType.equals("GENERAL_PAYMENT")||txnType.equals("CARD_PAYMENT")
				||txnType.equals("TAX_PAYMENT")){

			ApplicationContext appContext = Config.getAppContext();
			BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
	        CorpAccountService corpAccService = (CorpAccountService)appContext.getBean("CorpAccountService");
	        String payType = billPaymentService.txnType2PayType(txnType);
			BillPayment billPayment = billPaymentService.viewPayment(txnId, payType);
			if (corpAccService.isSavingAccount(billPayment.getFromAccount(),billPayment.getCurrency())){
				transCode="59SC";
		    } else {
		        transCode="59CC";
		   }
		}
		
		if(txnType.equals("STOP_CHEQUE")){
			transCode = "ZC07";
		}
		if(txnType.equals("CHEQUE_PROTECTION")){
			transCode = "ZC08";
		}
		if(txnType.equals("BANK_DRAFT")){
			transCode = "ZC05";
		}
		
		if(txnType.equals("CASHIER_ORDER")){
			transCode = "ZC06";
		}
		
		if(txnType.equals("CASHIER_ORDER")){
			transCode = "ZC06";
		}
		
		if (txnType.equals("AUTOPAY_ADD")||txnType.equals("AUTOPAY_EDIT")||txnType.equals("AUTOPAY_DELETE")){
			transCode = "ZB08";
		}
		
		if (txnType.equals("CHEQUE_BOOK_REQUEST")){
			transCode = "ZJ29";
		}
		return transCode;
	}
	
	public static Map<String,String> getOTPDesc(String txnType, String txnId) throws NTBException{
		String funcName = "";
		String transCode = "";
		Map<String,String> result = new HashMap<String,String>();
		if (txnType.equals("TRANSFER_MACAU")){
			funcName = "app.cib.action.txn.TransferInMacau";
			transCode = "ZJ55";
		}
		if (txnType.equals("TRANSFER_OVERSEAS")){
			funcName = "app.cib.action.txn.TransferInOverseas";
			transCode = "ZJ55";
		}
		if (txnType.equals("TRANSFER_BANK_1TON")){
			funcName = "app.cib.action.txn.TransferInBankStM";
			transCode = "51XX";
		}
		if (txnType.equals("TRANSFER_BANK_NTO1")){
			funcName = "app.cib.action.txn.TransferInBankMtS";
			transCode = "51XX";
		}
		if (txnType.equals("TRANSFER_MACAU_N")){
			funcName = "app.cib.action.txn.TransferInMacauStM";
			transCode = "XJ55";
		}
		if (txnType.equals("TRANSFER_OVERSEAS_N")){
			funcName = "app.cib.action.txn.TransferInOverseaStM";
			transCode = "XJ55";
		}
		if (txnType.equals("DELIVERY_TO_SUB")||txnType.equals("TRANSFER_BANK")
				||txnType.equals("TRANFER_BETWEEN_SUB")||txnType.equals("REPETRIATE_FROM_SUB")){
			if (txnType.equals("DELIVERY_TO_SUB")){
				funcName = "app.cib.action.txn.CorpDelivery";
			}
			if (txnType.equals("TRANSFER_BANK")){
				funcName = "aapp.cib.action.txn.TransferInBank";
			}
			if (txnType.equals("TRANFER_BETWEEN_SUB")){
				funcName = "app.cib.action.txn.CorpSubsidiary";
			}
			if (txnType.equals("REPETRIATE_FROM_SUB")){
				funcName = "app.cib.action.txn.CorpRepatriate";
			}
			if (null == txnId || txnId.equals("")){
				result.put("funcName", funcName);
				result.put("transCode", transCode);
				return result;
			}
			ApplicationContext appContext = Config.getAppContext();
			TransferService transferService = (TransferService) appContext
					.getBean("TransferService");
			TransferBank transferBank = transferService.viewInBANK(txnId);
			String fromAcctypeCode="";
			String toAcctypeCode="";
			if (transferBank.getFromAcctype().equals(
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
			}
			transCode = "51" + fromAcctypeCode + toAcctypeCode;
	    }
		if (txnType.equals("SCHEDULE_TXN")||txnType.equals("NEW_SCHEDULE_TXN")
				||txnType.equals("EDIT_SCHEDULE_TXN")||txnType.equals("BLOCK_SCHEDULE_TXN")
				||txnType.equals("UNBLOCK_SCHEDULE_TXN")||txnType.equals("REMOVE_SCHEDULE_TXN")){
			
			transCode="";
			funcName="app.cib.action.txn.ScheduleTransfer";
			
		}
		if (txnType.equals("BATCH_PAYMENT")){
			funcName = "app.cib.action.txn.TemplatePayment";
			transCode = "59CC";
		}
		if(txnType.equals("GENERAL_PAYMENT")||txnType.equals("CARD_PAYMENT")
				||txnType.equals("TAX_PAYMENT")){
			
			funcName = "app.cib.action.txn.BillPayment";
			if (null == txnId || txnId.equals("")){
				result.put("funcName", funcName);
				result.put("transCode", transCode);
				return result;
			}
			ApplicationContext appContext = Config.getAppContext();
			BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
	        CorpAccountService corpAccService = (CorpAccountService)appContext.getBean("CorpAccountService");
	        String payType = billPaymentService.txnType2PayType(txnType);
			BillPayment billPayment = billPaymentService.viewPayment(txnId, payType);
			if (corpAccService.isSavingAccount(billPayment.getFromAccount(),billPayment.getCurrency())){
				transCode="59SC";
		    } else {
		        transCode="59CC";
		   }
		}
		
		if(txnType.equals("STOP_CHEQUE")){
			funcName = "app.cib.action.srv.StopCheque";
			transCode = "ZC07";
		}
		if(txnType.equals("CHEQUE_PROTECTION")){
			funcName = "app.cib.action.srv.ProtectionCheque";
			transCode = "ZC08";
		}
		if(txnType.equals("BANK_DRAFT")){
			funcName = "app.cib.action.srv.BankRequest";
			transCode = "ZC05";
		}
		
		if(txnType.equals("CASHIER_ORDER")){
			funcName = "app.cib.action.srv.CashOrder";
			transCode = "ZC06";
		}
		
		if(txnType.equals("CASHIER_ORDER")){
			funcName = "app.cib.action.srv.CashOrder";
			transCode = "ZC06";
		}
		
		if (txnType.equals("AUTOPAY_ADD")||txnType.equals("AUTOPAY_EDIT")||txnType.equals("AUTOPAY_DELETE")){
			funcName = "app.cib.action.txn.Autopay";
			transCode = "ZB08";
		}
		
		if (txnType.equals("CHEQUE_BOOK_REQUEST")){
			funcName = "app.cib.action.srv.ChequeBookRequest";
			transCode = "ZJ29";
		}

		result.put("funcName", funcName);
		result.put("transCode", transCode);
		return null;
	}
	
	public static void updateTxnCodeToSmsOtpLog(String smsFlowNo, String tranCode) {
		if ((!"0".equals(smsFlowNo)) && (smsFlowNo != null)
				&& (!"".equals(smsFlowNo))) {
			SMSOTPObject otpObject = (SMSOTPObject) smsInstance.get(smsFlowNo);

			ApplicationContext appContext = Config.getAppContext();
			GenericJdbcDao dao = (GenericJdbcDao) appContext
					.getBean("genericJdbcDao");

			String sqlStr = "update SMS_OTP_LOG set TRAN_CODE=? where OTP_TRANS_ID=?";
			try {
				dao.update(sqlStr,
						new Object[] { tranCode, otpObject.getTransactionId() });
			} catch (Exception e) {
				Log.error("SMSUtil:Error updating trans Code to the log", e);
			} 
		}
	}
	
	public static  void removeInstance(String smsFlowNo){
		Log.info("SMSUtil:Removed sms instance(SMS Flow No:"
				+ smsFlowNo + ")");
		smsInstance.remove(smsFlowNo);
	}
	//get tran name
	public static String changeTransferType(String transferType, String language){
		RBFactory rb = RBFactory.getInstance("app.cib.resource.common.sms_trans_type",language);
		if( null != rb.getString(transferType)){
			return rb.getString(transferType);
		}else{
			return transferType;
		}
	}
	public static String getSMSTemplateID(String idType, String language){
		RBFactory rb = RBFactory.getInstance("app.cib.resource.common.sms_template_id",language);
		if( null != rb.getString(idType)){
			return rb.getString(idType);
		}else{
			return idType;
		}
	}
	
	/* Add by long_zg 2019-06-02 UAT6-465 COB锛氱煭淇′氦鏄撻鍨嬬己澶�  begin */
	public static String getSMSTemplateIDByTranType(String tranType,String lang){
		
		String idType= Constants.SMS_TEMPLATE_ID_TYPE_OTP ;
		
		if(Constants.SMS_TRAN_TYPE_LOGGED_IN.equalsIgnoreCase(tranType)){
			idType = Constants.SMS_TEMPLATE_ID_TYPE_LOGIN ;
		} else if(Constants.SMS_TRAN_TYPE_CHANGE_LOGIN_PASSWORD.equalsIgnoreCase(tranType)
				|| Constants.SMS_TRAN_TYPE_CHANGE_TRANSACTION_PASSWORD.equalsIgnoreCase(tranType)){
			idType = Constants.SMS_TEMPLATE_ID_TYPE_SET_PASS ;
		}else if(Constants.SMS_TRAN_TYPE_SUSPENDED_LOGIN_PASSWORD.equalsIgnoreCase(tranType)
				|| Constants.SMS_TRAN_TYPE_LOCKED_LOGIN_PASSWORD.equalsIgnoreCase(tranType)
				|| Constants.SMS_TRAN_TYPE_FROZEN_LOGIN_PASSWORD.equalsIgnoreCase(tranType)
				|| Constants.SMS_TRAN_TYPE_LOCKED_TRANSACTION_PASSWORD.equalsIgnoreCase(tranType)){
			idType = Constants.SMS_TEMPLATE_ID_TYPE_LOC_PASS ;
		}else if(Constants.SMS_TEMPLATE_ID_TYPE_REMITTANCE.equalsIgnoreCase(tranType)){
			idType = Constants.SMS_TEMPLATE_ID_TYPE_REMITTANCE;
		}
		
		RBFactory rb = RBFactory.getInstance("app.cib.resource.common.sms_template_id",lang);
		if( null != rb.getString(idType)){
			return rb.getString(idType);
		}else{
			return idType;
		}
	}
	/* Add by long_zg 2019-06-02 UAT6-465 COB锛氱煭淇′氦鏄撻鍨嬬己澶�  end */
	

}