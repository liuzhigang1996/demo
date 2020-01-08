//package app.cib.util.smsclient;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//
//import javax.xml.namespace.QName;
//
//import com.neturbo.set.core.Log;
//
//import app.cib.util.smsclient.sms.SmsInterAgent;
//import app.cib.util.smsclient.sms.SmsInterAgentService;
//import app.cib.util.smsclient.sms.SmsRequest;
//import app.cib.util.smsclient.sms.SmsResponse;
//
//
//public class WsClientImpl1 implements WsClient{
//	
//	private static SmsInterAgent smsInterAgent = null;
//	private static SmsInterAgentService smsInterAgentService = null;
//	private static String smsWsdlUrl = null;
//	private static WsClientImpl1 wsClientImpl = null;
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
//	private WsClientImpl1(String url1) throws Exception{
//		 // ��ʼ�����ŷ���
//		if (smsWsdlUrl == null) {
//			smsWsdlUrl = url1 ;	
//		}
//		
//		if (smsInterAgentService == null) {
//			try {
//				smsInterAgentService = new SmsInterAgentService(
//						new URL(smsWsdlUrl),
//						new QName("http://smsservice.bank.com/", "SmsInterAgentService"));
//			} catch (Exception e) {
//				Log.info("SMSOTP server_"+smsWsdlUrl+" init error: "+e.getMessage());
//				throw e;
//				//e.printStackTrace();
//			}
//		}
//		if (smsInterAgent == null) {
//			if(smsInterAgentService!=null)
//			   smsInterAgent = smsInterAgentService.getSmsInterAgentPort();
//		}
//	}
//	
//	public static WsClientImpl1 getInstance(String url2) throws Exception{	
//		wsClientImpl = new WsClientImpl1(url2);
//		return wsClientImpl;
//	}
//	
//	
//	public SmsResponse sendSms(SmsRequest req) throws Exception {
//		if(smsInterAgent==null){
//			return null;
//		}
//		SmsResponse smsResponse = null ;
//		try {
//			Log.info("SMSOTP server_"+smsWsdlUrl+" sendSms().....");
//			smsResponse = smsInterAgent.sendSms(req);
//		} catch (Exception e) {
//			Log.info("SMSOTP sendSms error: "+e.getMessage());
//			throw e;
//			//e.printStackTrace();
//		}		
//		return smsResponse;
//	}
//	
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
