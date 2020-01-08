package app.cib.service.sys;

import java.util.*;

import org.springframework.context.ApplicationContext;

import app.cib.bo.bnk.*;
import app.cib.bo.sys.*;
import app.cib.dao.bnk.*;
import app.cib.dao.sys.*;
import app.cib.core.*;
import app.cib.util.*;
import com.neturbo.set.exception.*;
import com.neturbo.set.utils.*;
import com.neturbo.set.core.*;

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
public class MailServiceImpl implements MailService {
    private CorpUserDao corpUserDao;
    private BankUserDao bankUserDao;
    private EmailNotificationDao emailDao;

    private NTBUser fromUser;
    
	/* hjs
    private SendMail mailProcessor;
    */

    public MailServiceImpl() {
    	/* hjs
    	 mailProcessor = SendMail.getSystemMailProcessor();
    	*/
    }

    private String findNextParameter(String str) {
        int istart = str.indexOf("<%");
        if (istart < 0) {
            return null;
        }
        int iend = str.indexOf("%>", istart);
        if (iend < 0) {
            return null;
        }
        return str.substring(istart, iend + 2);
    }

    /**
     *  Just do it as this
     */
    private synchronized void sendMail(String toName, String templateName,
                                      Map mailData, NTBUser toUser) {

        String mailBody[] = TemplateFactory.getInstance(templateName).
                            getMailContent();
        Date sendTime = new Date();

        if (mailData != null) {
            mailData.put("_sendTime", sendTime);

            for (int i = 0; i < mailBody.length; i++) {
            	//寰幆姣忎釜鍙橀噺
                String key = findNextParameter(mailBody[i]);
                while (key != null) {

                	//濡傛灉鏄甫鍙傛暟鐨勫�
                    String rb = null;
                    String db = null;
                    String format = null;
                    String pattern = null;
                    // Modified by Jet for list content
                    String listfields = null;

                    String keyName = key.substring(2, key.length() - 2);
                    if (keyName.indexOf("||") > 0) {
                        String[] temp1 = Utils.splitStr(keyName, "||");
                        keyName = temp1[0];
                        for (int j = 1; j < temp1.length; j++) {
                            String[] temp2 = Utils.splitStr(temp1[j], "@");
                            if (temp2.length == 2) {
                                if (temp2[0].equals("rb")) {
                                    rb = temp2[1];
                                } else if (temp2[0].equals("db")) {
                                    db = temp2[1];
                                } else if (temp2[0].equals("format")) {
                                    format = temp2[1];
                                } else if (temp2[0].equals("pattern")) {
                                    pattern = temp2[1];
                                } else if (temp2[0].equals("list")){
                                	listfields = temp2[1];
                                }
                            }
                        }
                    }

                    // Jet modified to handle list data                    
                    if (listfields != null){
                        String value = "";
                    	String[] fields = Utils.splitStr(listfields, ",");
                    	List listContent = (List) mailData.get(keyName);
                    	for (int j = 0; j < listContent.size(); j++){
                    		Map record = new HashMap();
                    		record = (Map)listContent.get(j);

                            String valueString = "";
                    		for (int k = 0; k < fields.length; k++){
                    			String[] field = Utils.splitStr(fields[k],"-");
                                Object valueObj = record.get(field[0]);

                                if (valueObj instanceof Date) {
                                	valueString = DateTime.Millis2DateTime(((Date) valueObj)
                                            .getTime());
                                } else if (valueObj.getClass().isArray()) {
                                    Object[] valueObjArray = (Object[]) valueObj;
                                    if (valueObjArray.length > 0) {
                                    	valueString = valueObjArray[0].toString().trim();
                                    }
                                } else {
                                	valueString = valueObj.toString().trim();
                                }

                             // 鏍煎紡鍖栧瓧涓�
                                if (field[1] != null && !field[1].trim().equals("")) {
                                	valueString = Format.formatData(valueString, field[1], pattern);
                                }
                                if (k%fields.length == 0){
                                	value = value + "<tr>\n";
                                }
                                value = value + "<td class=\"STYLE2\">" + valueString + "</td>\n";
                                if (k%fields.length == fields.length - 1){
                                	value = value + "<tr>\n";
                                }
                    		}
                    	}
                        mailBody[i] = Utils.replaceStr(mailBody[i], key, value);                    	
                    } else {
						Object valueObj = mailData.get(keyName);
						if (valueObj != null) {
							String value = "";
							if (valueObj instanceof Date) {
								value = DateTime
										.Millis2DateTime(((Date) valueObj)
												.getTime());
							} else if (valueObj.getClass().isArray()) {
								Object[] valueObjArray = (Object[]) valueObj;
								if (valueObjArray.length > 0) {
									value = valueObjArray[0].toString().trim();
								}
							} else {
								value = valueObj.toString().trim();
							}

							// 濡傛灉rb瀛樺湪锛屽垯璇�
							if (rb != null) {
								// RBFactory rbList = RBFactory.getInstance(rb,
								// locale.toString());
								RBFactory rbList = RBFactory.getInstance(rb);
								value = rbList.getString(value);
							}
							if (db != null) {
								DBRCFactory rbList = DBRCFactory
										.getInstance(db);
								if (rbList != null) {
									rbList.setArgs(null);
									value = rbList.getString(value);
								}
							}

							// 濡傛灉format瀛樺湪锛屽垯鏍煎紡鍖栧瓧涓�
							if (format != null && !format.trim().equals("")) {
								value = Format.formatData(value, format,
										pattern);
							}

							mailBody[i] = Utils.replaceStr(mailBody[i], key,
									value);
						} else {
							mailBody[i] = Utils
									.replaceStr(mailBody[i], key, "");
						}
					} // end for list != null
                    key = findNextParameter(mailBody[i]);
                }
            }
        }

    	/*
		 * hjs //mailProcessor.setDefaultCharset("");
		 * mailProcessor.setSubject(mailBody[0]);
		 * mailProcessor.setBody(mailBody[1]); mailProcessor.setTo(toName);
		 */

        EmailNotification emailObj = new EmailNotification(CibIdGenerator.
                getIdForOperation("EMAIL_NOTIFICATION"));
        emailObj.setTitle(mailBody[0]);
        emailObj.setContent(mailBody[1]);
        emailObj.setEmail(toName);
        emailObj.setStatus(Constants.STATUS_PENDING_APPROVAL);
        emailObj.setSubmitTime(sendTime);
        if (fromUser != null) {
            emailObj.setUserId(fromUser.getUserId());
            if (fromUser instanceof CorpUser) {
                emailObj.setCorpId(((CorpUser) fromUser).getCorpId());
            }
        }
        if (toUser != null) {
            emailObj.setRecipientUser(toUser.getUserId());
            if (toUser instanceof CorpUser) {
                emailObj.setRecipientCorp(((CorpUser) toUser).getCorpId());
            }
        }

        emailDao.add(emailObj); 

    	/* hjs
        if (mailProcessor.send() == true) {
            emailObj.setStatus(Constants.STATUS_NORMAL);
            emailDao.update(emailObj);
        }
        */
    }


    //鏍规嵁妯＄増鍜屾暟鎹�鍙戦�鍒颁竴缁勭敤鎴�
    private void sendToCorpUserList(String templateName, String[] userList,
                                    Map dataMap) {
        for (int i = 0; i < userList.length; i++) {
            String userId = (String) userList[i];
            //sendToCorpUser(templateName, userId, dataMap);
        }
    }
    /* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
    private void sendToCorpUserList(String templateName, String[] userList,String corpId,
    		Map dataMap) throws NTBException {
    	for (int i = 0; i < userList.length; i++) {
    		String userId = (String) userList[i];
    		sendToCorpUser(templateName, userId, corpId, dataMap);
    	}
    }
    /* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
//add by linrui for flow approve reply to bank user
    private void sendToBankUserList(String templateName, String[] userList,
    		Map dataMap) {
    	for (int i = 0; i < userList.length; i++) {
    		String userId = (String) userList[i];
    		sendToBankUser(templateName, userId, dataMap);
    	}
    }

    //鏍规嵁妯＄増鍜屾暟鎹�鍙戦�鍒颁竴涓叕鍙哥敤鎴�
    private void sendToCorpUser(String templateName, String userId,
                                Map dataMap) {
        CorpUser user = (CorpUser) corpUserDao.load(CorpUser.class, userId);
        
        if (user != null) {
            String emailAddress = user.getEmail();
            if (emailAddress != null) {
                sendMail(emailAddress, templateName, dataMap, user);
            }
        }
    }
    /* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
    private void sendToCorpUser(String templateName, String userId,String corpId,
    		Map dataMap) throws NTBException {
    	CorpUserService corpUserService = (CorpUserService) Config
        .getAppContext().getBean(
                "corpUserService");
    	CorpUser user = corpUserService.loadWithCorpId(userId, corpId) ;
    	if (user != null) {
    		String emailAddress = user.getEmail();
    		if (emailAddress != null) {
    			sendMail(emailAddress, templateName, dataMap, user);
    		}
    	}
    }
    /* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */

    //鏍规嵁妯＄増鍜屾暟鎹�鍙戦�鍒颁竴涓摱琛岀敤鎴�
    private void sendToBankUser(String templateName, String userId,
                                Map dataMap) {
        BankUser user = (BankUser) bankUserDao.load(BankUser.class, userId);
        if (user != null) {
            String emailAddress = user.getEmail();
            if (emailAddress != null) {
                sendMail(emailAddress, templateName, dataMap, user);
            }
        }
    }

    public void toRequester_Approved(String txnType, String[] userList,
                                     Map dataMap) {
        String templateName = "Mail_ToRequester_Approved.jsp";
      //add by linrui 20180529
        if("B".equals(Utils.null2Empty(dataMap.get("userType")))){
        	//sendToBankUserList(templateName, userList, dataMap);
        }else{
            //sendToCorpUserList(templateName, userList, dataMap);
        }
    }

    public void toRequester_Rejected(String txnType, String[] userList,
                                     Map dataMap) {
        String templateName = "Mail_ToRequester_Rejected.jsp";
      //add by linrui 20180529
        if("B".equals(Utils.null2Empty(dataMap.get("userType")))){
        	//sendToBankUserList(templateName, userList, dataMap);
        }else{
            //sendToCorpUserList(templateName, userList, dataMap);
        }
    }

    public void toApprover_Requested(String txnType, String[] userList,
                                     Map dataMap) {
        String templateName = "";
      //add by linrui 20180529
        if("B".equals(Utils.null2Empty(dataMap.get("userType")))){
        	sendToBankUserList(templateName, userList, dataMap);
        }else{
            sendToCorpUserList(templateName, userList, dataMap);
        }
    }

    public void toApprover_Seleted(String txnType, String[] userList,
                                   Map dataMap) {
        try {
            RBFactory rb = RBFactory.getInstance(
                    "app.cib.resource.mail.assign_approver");
            String templateName = rb.getString(txnType);
            sendToCorpUserList(templateName, userList, dataMap);
        } catch (Exception e) {
            Log.error("Error sending email in toCorpUser_Operated", e);
        }
    }
    /* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
    public void toApprover_Seleted(String txnType, String[] userList,String corpId,
    		Map dataMap) {
    	try {
    		RBFactory rb = RBFactory.getInstance(
    				"app.cib.resource.mail.assign_approver");
    		String templateName = rb.getString(txnType);
    		sendToCorpUserList(templateName, userList, corpId, dataMap);
    	} catch (Exception e) {
    		Log.error("Error sending email in toCorpUser_Operated", e);
    	}
    }
    /* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */

    public void toApprover_Changed(String txnType, String[] userList,
                                   Map dataMap) {
        String templateName = "Mail_ToChgApprover_Changed.jsp";
        //add by linrui 20180529
        if("B".equals(Utils.null2Empty(dataMap.get("userType")))){
        	sendToBankUserList(templateName, userList, dataMap);
        }else{
            sendToCorpUserList(templateName, userList, dataMap);
        }
    }

    public void toMember_GroupUpdated(String groupId, Map dataMap) {
        String templateName = "Mail_ToMember_GroupUpdate.jsp";
        // 濡傛灉鐢ㄦ埛鐨勭姸鎬佷负姝ｅ湪鎺堟潈
        GroupService groupService = (GroupService) Config.getAppContext()
                        .getBean("GroupService");
        try{
            List corpUserList = groupService.listCorpUser(groupId);
            String[] userArray = new String[corpUserList.size()];
            for(int i=0; i< corpUserList.size(); i++) {
                CorpUser user = (CorpUser) corpUserList.get(i);
                userArray[i] = user.getUserId();
            }
            sendToCorpUserList(templateName, userArray, dataMap);
        }catch(Exception e){
            Log.error("Error sending email to group user", e);
        }
    }

    public void toMember_GroupAssigned(String userId, String groupId,
                                       Map dataMap) {
        String templateName = "Mail_ToMember_GroupAssign.jsp";
        //sendToCorpUser(templateName, userId, dataMap);
    }

    public void toCorpUser_Operated(String operation, String userId,
                                    Map dataMap) {
        try {
            RBFactory rb = RBFactory.getInstance(
                    "app.cib.resource.mail.corp_user");
            String templateName = rb.getString(operation);
            //sendToCorpUser(templateName, userId, dataMap);
        } catch (Exception e) {
            Log.error("Error sending email in toCorpUser_Operated", e);
        }
    }

    public void toBankUser_Operated(String operation,String userId,
                                    Map dataMap) {
        try {
            RBFactory rb = RBFactory.getInstance(
                    "app.cib.resource.mail.bank_user");
            String templateName = rb.getString(operation);
            sendToBankUser(templateName, userId, dataMap);
        } catch (Exception e) {
            Log.error("Error sending email in toBankUser_Operated", e);
        }
    }
    
    public void toLastApprover_Executor(String txnType, 
    		String userId, Map dataMap) {
        try {
            RBFactory rb = RBFactory.getInstance(
                    "app.cib.resource.mail.last_approver");
            String templateName = rb.getString(txnType);
            //sendToCorpUser(templateName, userId, dataMap);
        } catch (Exception e) {
            Log.error("Error sending email in toCorpUser_Operated", e);
        }
    }
    /* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
    public void toLastApprover_Executor(String txnType, 
    		String userId, String corpId ,Map dataMap) {
    	try {
    		RBFactory rb = RBFactory.getInstance(
    				"app.cib.resource.mail.last_approver");
    		String templateName = rb.getString(txnType);
    		//sendToCorpUser(templateName, userId, corpId, dataMap);
    	} catch (Exception e) {
    		Log.error("Error sending email in toCorpUser_Operated", e);
    	}
    }
    /* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
    
    public BankUserDao getBankUserDao() {
        return bankUserDao;
    }

    public CorpUserDao getCorpUserDao() {
        return corpUserDao;
    }

    public EmailNotificationDao getEmailDao() {
        return emailDao;
    }

    public NTBUser getFromUser() {
        return fromUser;
    }

    public void setEmailDao(EmailNotificationDao emailDao) {
        this.emailDao = emailDao;
    }

    public void setCorpUserDao(CorpUserDao corpUserDao) {
        this.corpUserDao = corpUserDao;
    }

    public void setBankUserDao(BankUserDao bankUserDao) {
        this.bankUserDao = bankUserDao;
    }

    public void setFromUser(NTBUser fromUser) {
        this.fromUser = fromUser;
    }

    public static void main (String[] args) {
        Config.setAppRoot("E:\\BANK_CIB\\DEV\\WebContent");
        ApplicationContext appContext = Config.getAppContext();
        MailService mailService = (MailService) appContext.getBean("MailService");
        Map dataMap = null;

        String operation = "1";
        String userId = "7101";
        dataMap = new HashMap();
        dataMap.put("userId", "7101");
        dataMap.put("userName", "Test Operator 1");
        mailService.toBankUser_Operated(operation, userId, dataMap);
        Log.info("Mail Accomplished!");

        operation = "1";
        userId = "7101";
        dataMap = new HashMap();
        dataMap.put("userId", "7101");
        dataMap.put("userName", "Test Operator 1");
        mailService.toBankUser_Operated(operation, userId, dataMap);
        /*
        String[] userList = new String[]{"2101", "2201"};
        dataMap = new HashMap();
        dataMap.put("userId", "1101");
        dataMap.put("userName", "Test Operator 1");
        mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_TRANSFER_BANK, userList, dataMap);
        */
    }

}
