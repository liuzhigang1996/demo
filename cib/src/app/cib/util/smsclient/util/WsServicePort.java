//package app.cib.util.smsclient.util;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import javax.xml.namespace.QName;
//
//import app.cib.util.smsclient.sms.SmsInterAgent;
//import app.cib.util.smsclient.sms.SmsInterAgentService;
//import app.cib.util.smsclient.sms.SmsRequest;
//import app.cib.util.smsclient.sms.SmsResponse;
//
//import com.neturbo.set.core.Config;
//import com.neturbo.set.utils.Utils;
//
///**
// * 
// * @author Sam Peng Create Date: 2013-07-17 Update:
// * 
// */
//public class WsServicePort {
//
//
//	private static SmsInterAgent smsInterAgent = null;
//	private static SmsInterAgentService smsInterAgentService = null;
//	private static String smsWsdlUrl = null;
//
//	static {
//
//		// ��ʼ�����ŷ���
//		if (smsWsdlUrl == null) {
//			smsWsdlUrl = Config.getProperty("smsWsdlUrl");
//			// wsURL = "http://192.168.1.81:8082/mail?wsdl";
//		}
//		if (smsInterAgentService == null) {
//			try {
//				smsInterAgentService = new SmsInterAgentService(
//						new URL(smsWsdlUrl),
//						new QName("http://entry.apms.app/", "SmsServiceService"));
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
//
//
//	/**
//	 * ���Ͷ��Žӿ�
//	 * 
//	 * @param Mail
//	 *            mailObj
//	 */
//	public static SmsResponse sendSms(SmsRequest sms) throws Exception {
//		// ���ʼ�������ʼ�����ת����16���ƽ��д��䡣�������ӵ����ٽ���
//		 sms.setContent(Utils.bytes2HexStr(sms.getContent().getBytes()));
//		return smsInterAgent.sendSms(sms);
//	}
//
//	/**
//	 * send Mail
//	 * 
//	 * @param map
//	 *            �����map�е�KeyҪ��bcc��cc�� content�� subject�� to��
//	 *            attachments(��һ��List����FileInputStream)��
//	 *            fileNames(��һ��List����FileInputStream��Ӧ���ļ���Ҫһһ��Ӧ) appId��appCert
//	 * @return String
//	 * @throws MalformedURLException
//	 */
//
//	// /**
//	// * ���?������Map
//	// * ��Mapת��ΪMail
//	// * @return Mail
//	// * @throws MalformedURLException
//	// */
//	// private static Mail processMap(Map map) throws Exception {
//	// String bcc = (String) map.get("bcc");
//	// String cc = (String) map.get("cc");
//	// String content = (String) map.get("content");
//	// // content=encode(content);
//	// // Log.info("decode="+decode(content));
//	// // map.put("content", content);
//	// String subject = (String) map.get("subject");
//	// String to = (String) map.get("to");
//	// List<FileInputStream> attachments = (List) map.get("attachments");
//	// String appId = (String) map.get("appId");
//	// String appCert = (String) map.get("appCert");
//	// List<String> fileNames = (List) map.get("fileNames");
//	//		
//	// Mail mail = new Mail();
//	// List<byte[]> attachmentsList = new ArrayList();
//	// if(attachments!=null && attachments.size()>0) {
//	// for(int i=0; i<attachments.size(); i++) {
//	// FileInputStream fis = attachments.get(i);
//	//				
//	// byte[] buffer = new byte[MailUtilConstant.SIZE + 1];
//	// long size = fis.read(buffer);
//	//				
//	// //���Ƹ����Ĵ�С
//	// if(size > MailUtilConstant.SIZE){
//	// Log.error(MailUtilConstant.ERROR_LIMITSIZE);
//	// throw new Exception(MailUtilConstant.ERROR_LIMITSIZE);
//	// }
//	//		    	
//	// //����������һ��byte������
//	// byte[] sendByte = new byte[(int)size];
//	// for(int j=0; j<size; j++) {
//	// sendByte[j] = buffer[j];
//	// }
//	// fis.read(sendByte);
//	// fis.close();
//	//		    	
//	// mail.getAttachments().add(sendByte);
//	// mail.getFileNames().add(fileNames.get(i));
//	// }
//	// }
//	//
//	// // Log.info("processMap="+map);
//	// mail.setBcc(bcc);
//	// mail.setCc(cc);
//	// mail.setContent(content);
//	// mail.setSubject(subject);
//	// mail.setTo(to);
//	// mail.setAppId(appId);
//	// mail.setAppCert(appCert);
//	//		
//	// return mail;
//	// }
//}
