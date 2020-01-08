//package app.cib.util.smsclient;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import javax.xml.namespace.QName;
//
//import com.neturbo.set.core.Config;
//
//import app.cib.util.smsclient.sms.SmsInterAgent;
//import app.cib.util.smsclient.sms.SmsInterAgentService;
//import app.cib.util.smsclient.sms.SmsRequest;
//import app.cib.util.smsclient.sms.SmsResponse;
//
///**
// * 
// * @author Sam Peng
// * 
// */
//
//public class WsClientImpl implements WsClient{
//	
//	private static SmsInterAgent smsInterAgent = null;
//	private static SmsInterAgentService smsInterAgentService = null;
//	private static String smsWsdlUrl = null;
//	
//	static {
//
//		// ��ʼ�����ŷ���
//		if (smsWsdlUrl == null) {
//			smsWsdlUrl = Config.getProperty("app.sms.server.url_1");
////			smsWsdlUrl = BankUtil.getProperty("app.sms.server.url_2");
//			
//		}
////		smsWsdlUrl = "http://localhost:8082/smsinter?wsdl" ;
//		if (smsInterAgentService == null) {
//			try {
//				smsInterAgentService = new SmsInterAgentService(
//						new URL(smsWsdlUrl),
//						new QName("http://smsservice.bank.com/", "SmsInterAgentService"));
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			}
//		}
//		if (smsInterAgent == null) {
//			smsInterAgent = smsInterAgentService.getSmsInterAgentPort();
//		}
//
//	}
//	
//	public SmsResponse sendSms(SmsRequest req) {
//		SmsResponse smsResponse = smsInterAgent.sendSms(req) ;
//		return smsResponse;
//	}
//
//	
////	public static void main(String args) {
////		SmsRequest smsRequest = new SmsRequest();
////		smsRequest.setClientSystemId("dsf") ;
////		smsRequest.setContent("dsf") ;
////		smsRequest.setLanguage("E") ;
////		smsRequest.setMobileNo("2525") ;
////		smsRequest.setTransactionId("dsf") ;
////		smsRequest.setTransactionTime("dsf") ;
////		
////		WsClient wc = new WsClientImpl() ;
////		
////		wc.sendSms(smsRequest) ;
////	}
//}
