//package app.cib.util.smsclient;
//
//import java.net.URL;
//
//import javax.xml.namespace.QName;
//
//import app.cib.util.smsclient.log.LogInterAgent;
//import app.cib.util.smsclient.log.LogInterAgentService;
//import app.cib.util.smsclient.log.LogRequest;
//import app.cib.util.smsclient.log.LogResponse;
//import app.cib.util.smsclient.log.LogSmsRequest;
//
//import com.neturbo.set.core.Config;
//import com.neturbo.set.core.Log;
//
//
//public class WsLogClientImpl2 implements WsLogClient{
//	
//	private static LogInterAgent logInterAgent = null;
//	private static LogInterAgentService logInterAgentService = null;
//	private static String logWsdlUrl = null;
//	private static WsLogClientImpl2 wsLogClientImpl2 = null;
//	
//	/*static {
//		
//		// ��ʼ�����ŷ���
//		if (smsWsdlUrl == null) {
//			smsWsdlUrl = BankUtil.getProperty("app.sms.server.url_1");	
//		}
//		
//		if (smsInterAgentService == null) {
//			try {
//				smsInterAgentService = new SmsInterAgentService(
//						new URL(smsWsdlUrl),
//						new QName("http://smsservice.bank.com/", "SmsInterAgentService"));
//			} catch (Exception e) {
//				Log.info("server_"+smsWsdlUrl+" error: "+e.getMessage());
//				//e.printStackTrace();
//			}
//		}
//		if (smsInterAgent == null) {
//			if(smsInterAgentService!=null)
//			   smsInterAgent = smsInterAgentService.getSmsInterAgentPort();
//		}
//
//	}*/
//	
//	private WsLogClientImpl2(){
//		 // ��ʼ�����ŷ���
//		if (logWsdlUrl == null) {
//			logWsdlUrl = Config.getProperty("app.sms.log.server.url_2");	
//		}
//		
//		if (logInterAgentService == null) {
//			try {
//				logInterAgentService = new LogInterAgentService(
//						new URL(logWsdlUrl),
//						new QName("http://logservice.bank.com/", "LogInterAgentService"));
//			} catch (Exception e) {
//				Log.info("log server_"+logWsdlUrl+" init error: "+e.getMessage());
//				//e.printStackTrace();
//			}
//		}
//		if (logInterAgent == null) {
//			if(logInterAgentService!=null)
//				logInterAgent = logInterAgentService.getLogInterAgentPort() ;
//		}
//	}
//	
//	public static WsLogClientImpl2 getInstance(){	
//		wsLogClientImpl2 = new WsLogClientImpl2();
//		return wsLogClientImpl2;
//	}
//	
//	
//	/*public LogResponse sendSms(SmsRequest req) {
//		if(smsInterAgent==null){
//			return null;
//		}
//		SmsResponse smsResponse = null ;
//		try {
//			Log.info("log server_"+smsWsdlUrl+" sendSms().....");
//			smsResponse = smsInterAgent.sendSms(req);
//		} catch (Exception e) {
//			Log.info("log server_"+smsWsdlUrl+" connect error: "+e.getMessage());
//			//e.printStackTrace();
//		}		
//		return smsResponse;
//	}*/
//	
//	public LogResponse logSms(LogSmsRequest logSmsRequest) {
//		if(logInterAgent==null){
//			return null;
//		}
//		LogResponse logResponse = null ;
//		try {
//			Log.info("Log server_"+logWsdlUrl+" logSms().....");
//			logResponse = logInterAgent.logSms(logSmsRequest) ;
//		} catch (Exception e) {
//			Log.info("log server_"+logWsdlUrl+" connect error: "+e.getMessage());
//			//e.printStackTrace();
//		}		
//		return logResponse;
//	}
//	
//	 public LogResponse logInfo(LogRequest logRequest){
//		 if(logInterAgent==null){
//				return null;
//			}
//			LogResponse logResponse = null ;
//			try {
//				Log.info("Log server_"+logWsdlUrl+" logSms().....");
//				logResponse = logInterAgent.logInfo(logRequest) ;
//			} catch (Exception e) {
//				Log.info("log server_"+logWsdlUrl+" connect error: "+e.getMessage());
//				//e.printStackTrace();
//			}		
//			return logResponse;
//	 }
//	 
///*	public static void main(String[] args) {
//		SmsRequest smsRequest = new SmsRequest();
//		smsRequest.setClientSystemId("dsf") ;
//		smsRequest.setContent("dsf") ;
//		smsRequest.setLanguage("E") ;
//		smsRequest.setMobileNo("2525") ;
//		smsRequest.setTransactionId("dsf") ;
//		smsRequest.setTransactionTime("dsf") ;
//		
//		WsClient wc = new WsClientImpl1() ;
//		
//		SmsResponse abcResponse = wc.sendSms(smsRequest) ;
//		System.out.println("testing..."+abcResponse.getStatusCode());
//	}*/
//	
//}
