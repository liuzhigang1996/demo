//package app.cib.util.smsclient.service;
//
//import java.util.Date;
//import java.util.Locale;
//import java.util.Map;
//
//import app.cib.util.smsclient.sms.SmsRequest;
//import app.cib.util.smsclient.sms.SmsResponse;
//import app.cib.util.smsclient.util.MailConstant;
//import app.cib.util.smsclient.util.WsServicePort;
//
//import com.neturbo.set.core.Config;
//import com.neturbo.set.core.Log;
//import com.neturbo.set.exception.NTBException;
//import com.neturbo.set.utils.DBRCFactory;
//import com.neturbo.set.utils.DateTime;
//import com.neturbo.set.utils.Format;
//import com.neturbo.set.utils.RBFactory;
//import com.neturbo.set.utils.TemplateFactory;
//import com.neturbo.set.utils.Utils;
//
///**
// * <p>
// * Title:���ʼ�
// * </p>
// * 
// * <p>
// * Description:
// * </p>
// * 
// * @author not attributable
// * @version 1.0
// */
//public class WsServiceImpl implements WsService {
//
//
//	public static String EMAIL_ADDRESS_DELIMITER = Config
//			.getProperty("EmailAddressDelimiter");
//
//	public WsServiceImpl() {
//	}
//
//	// ���Ͷ���
//	public String sendSms(String templateType,String to,
//			Map dataMap,String language) throws NTBException {
//		
//		String templateName = getTemplateName(templateType);//���templateType ��ȡtemplateName
//		Date sendTime = new Date();
//		String mailBody[] = parseMailBody(templateName, dataMap, sendTime);
//
//		String errMsg = MailConstant.ERROR_CODE_STATUS_FAILED;
//		try {
//			//��װ�ʼ�
//			SmsRequest sms=new SmsRequest();
////			sms.setClientSystemId(value) ;
//			sms.setContent(mailBody[1]) ;
////			sms.setLanguage(value) ;
//			sms.setMobileNo(to) ;
////			sms.setTransactionId(value) ;
////			sms.setTransactionTime(value) ;
//			
//			// =============�����ʼ�===============
//			SmsResponse smsResp =WsServicePort.sendSms(sms);//�����ʼ�
//			errMsg=smsResp.getStatusCode();
//			if (!errMsg.equals(MailConstant.SEND_EMAIL_RESPONSE_MSG_NORMAL_0000)) {
//				// error
//				Log.error("<sendSms2Server> mailTo[" + to + "] ERROR:"
//						+ errMsg);
//				errMsg = smsResp.getStatusDesc();
//			} else {
//				//0000
////				errMsg = Constants.STATUS_NORMAL;
//				// errMsg = null;
//			}
//
//			Log.info("<sendSms2Server> errMsg:" + errMsg);
//
//
//		} catch (Exception e) {
//			Log.error("sendSms2Server ERROR " + e);
//			e.printStackTrace();
//			errMsg = e.getMessage();
//		} finally {
//
//
//			return errMsg;
//		}
//	}
//
//	private String[] parseMailBody(String templateName, Map mailData,
//			Date sendTime) throws NTBException {
//		String mailBody[] = TemplateFactory.getInstance(templateName)
//				.getMailContent();
//
//		if (mailData != null) {
//			mailData.put("sendTime", sendTime);
//
//			for (int i = 0; i < mailBody.length; i++) {
//				// ѭ��ÿ������
//				String key = findNextParameter(mailBody[i]);
//				while (key != null) {
//
//					// ����Ǵ�����ֵ
//					String rb = null;
//					String db = null;
//					String format = null;
//					String pattern = null;
//
//					String keyName = key.substring(2, key.length() - 2);
//					if (keyName.indexOf("||") > 0) {
//						String[] temp1 = Utils.splitStr(keyName, "||");
//						keyName = temp1[0];
//						for (int j = 1; j < temp1.length; j++) {
//							String[] temp2 = Utils.splitStr(temp1[j], "@");
//							if (temp2.length == 2) {
//								if (temp2[0].equals("rb")) {
//									rb = temp2[1];
//								} else if (temp2[0].equals("db")) {
//									db = temp2[1];
//								} else if (temp2[0].equals("format")) {
//									format = temp2[1];
//								} else if (temp2[0].equals("pattern")) {
//									pattern = temp2[1];
//								}
//
//							}
//						}
//					}
//
//					Object valueObj = mailData.get(keyName);
//					if (valueObj != null) {
//						String value = "";
//						if (valueObj instanceof Date) {
//							value = DateTime.Millis2DateTime(((Date) valueObj)
//									.getTime());
//						} else if (valueObj.getClass().isArray()) {
//							Object[] valueObjArray = (Object[]) valueObj;
//							if (valueObjArray.length > 0) {
//								value = valueObjArray[0].toString().trim();
//							}
//						} else {
//							value = valueObj.toString().trim();
//						}
//
//						// ���rb���ڣ����
//						if (rb != null) {
//							// RBFactory rbList = RBFactory.getInstance(rb,
//							// locale.toString());
//							RBFactory rbList = RBFactory.getInstance(rb);
//							value = rbList.getString(value);
//						}
//						if (db != null) {
//							DBRCFactory rbList = DBRCFactory.getInstance(db);
//							if (rbList != null) {
//								rbList.setArgs(null);
//								value = rbList.getString(value);
//							}
//						}
//
//						// ���format���ڣ����ʽ���ִ�
//						if (format != null && !format.trim().equals("")) {
//							value = Format.formatData(value, format, pattern);
//						}
//
//						mailBody[i] = Utils.replaceStr(mailBody[i], key, value);
//					} else {
//						mailBody[i] = Utils.replaceStr(mailBody[i], key, "");
//					}
//					key = findNextParameter(mailBody[i]);
//				}
//			}
//		}
//		return mailBody;
//	}
//
//	private String findNextParameter(String str) throws NTBException {
//		int istart = str.indexOf("<%");
//		if (istart < 0) {
//			return null;
//		}
//		int iend = str.indexOf("%>", istart);
//		if (iend < 0) {
//			return null;
//		}
//		return str.substring(istart, iend + 2);
//	}
///**
// * ��ȡTemplateName
// * @param templateType
// * @return
// * @throws NTBException
// */
//	private String getTemplateName(String templateType) throws NTBException {
//		String templateName = "MAIL_template.jsp";
//
//		Locale locale = Config.getDefaultLocale();
//		RBFactory rbFactory = RBFactory.getInstance(
//				Config.getProperty("mailTemplateTypeProperties"), locale.toString());
//
//		if (rbFactory != null) {
//			templateName = rbFactory.getString(templateType);
//		}
//		if (null == templateName || "".equals(templateName)) {
//			templateName = "MAIL_template.jsp";
//		}
//		return templateName;
//	}
//
//
//}
