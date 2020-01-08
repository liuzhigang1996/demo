package app.cib.action.srv;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.*;

import app.cib.bo.srv.Message;
import app.cib.bo.srv.MessageUser;
import app.cib.bo.sys.CorpUser;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.service.bnk.BankUserService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.srv.MessageService;
import app.cib.service.srv.MessageUserService;
import app.cib.service.sys.CorpUserService;
import app.cib.util.Constants;

public class MessageManageAction extends CibAction implements Approvable {

	public void addLoad() throws NTBException {
		setResultData(new HashMap(1));
	}

	public void add() throws NTBException {
		Message message = new Message();
		Map dataMap = getParameters();
		String fromDate = (String) dataMap.get("fromDate");
		String toDate = (String) dataMap.get("toDate");

		if (null != fromDate) {
			dataMap.put("fromDate", DateTime.getDateFromStr(fromDate));
		}
		if (null != toDate) {
			dataMap.put("toDate", DateTime.getDateFromStr(toDate));
		}
		convertMap2Pojo(dataMap, message);
		setUsrSessionDataValue("messageObj", message);
		setResultData(getParameters());
	}

	public void addConfirm() throws NTBException {
		String messageId = CibIdGenerator.getIdForOperation("Message");
		FlowEngineService flowEngineService = (FlowEngineService) Config
				.getAppContext().getBean("FlowEngineService");
		String procId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_MESSAGE_ADD,
				FlowEngineService.TXN_CATEGORY_NONFINANCE,
				MessageManageAction.class, null, 0, null, 0, 0, messageId,
				null, getUser(), null, null, null);
		try {
			Message message = (Message) getUsrSessionDataValue("messageObj");
			message.setMessageId(messageId);
			message.setStatus(Constants.STATUS_PENDING_APPROVAL);
			message.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			message.setUserId(getUser().getUserId());
			message.setSubmitTime(new Date());
			MessageService messageService = (MessageService) Config
					.getAppContext().getBean("MessageService");
			messageService.add(message);
		} catch (Exception e) {
			flowEngineService.cancelProcess(procId, getUser());
			Log.debug("Process error at MessageManageAction.addConfirm(): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			} else {

			}
		}
		getResultData().put("messageId", messageId);
	}

	public void delete() throws NTBException {
		String[] messageIds = getParameterValues("messageId");
		FlowEngineService flowEngineService = (FlowEngineService) Config
				.getAppContext().getBean("FlowEngineService");
		MessageService messageService = (MessageService) Config.getAppContext()
				.getBean("MessageService");

		List deleteMessageList = new ArrayList();

		Message message = null;
		String procId = null;
		for (int i = 0; i < messageIds.length; i++) {
			procId = flowEngineService.startProcess(
					Constants.TXN_SUBTYPE_MESSAGE_DELETE,
					FlowEngineService.TXN_CATEGORY_NONFINANCE,
					MessageManageAction.class, null, 0, null, 0, 0,
					messageIds[i], null, getUser(), null, null, null);
			try {
				message = messageService.load(messageIds[i]);
				message.setStatus(Constants.STATUS_PENDING_APPROVAL);
				message.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
				messageService.update(message);
				deleteMessageList.add(message);
			} catch (Exception e) {
				flowEngineService.cancelProcess(procId, getUser());
			}
		}
		deleteMessageList = this.convertPojoList2MapList(deleteMessageList);

		getResultData().put("deleteMessageList", deleteMessageList);

	}

	public void listByBank() throws NTBException {
		String fetchSize = getParameter("fetchSize");
		if (null == fetchSize || "".equals(fetchSize)) {
			fetchSize = "20";
		}
		MessageService messageService = (MessageService) Config.getAppContext()
				.getBean("MessageService");
		List messageList = messageService.listByBank(Integer
				.parseInt(fetchSize));

		HashMap resultData = new HashMap();
		messageList = this.convertPojoList2MapList(messageList);
		resultData.put("messageList", messageList);
		setResultData(resultData);

	}

	public void view() throws NTBException {
		String messageId = getParameter("messageId");
		MessageService messageService = (MessageService) Config.getAppContext()
				.getBean("MessageService");
		Message message = messageService.load(messageId);

		Map resultData = getResultData();
		convertPojo2Map(message, resultData);
		setResultData(resultData);
	}

	public void listByBankSupervisor() throws NTBException {
		listByBank();
	}

	public void viewBySupervisor() throws NTBException {
		view();
	}

	public boolean approve(String txnType, String id, CibAction bean)
			throws NTBException {
		MessageService messageService = (MessageService) Config.getAppContext()
				.getBean("MessageService");
		Message message = messageService.load(id);

		if (Constants.TXN_SUBTYPE_MESSAGE_ADD.equals(txnType)) {
			message.setStatus(Constants.STATUS_NORMAL);
			message.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			messageService.update(message);

			CorpUserService corpUserService = (CorpUserService) Config
					.getAppContext().getBean("corpUserService");
			List allCorpUsers = corpUserService.listAllUser();

			if (null != allCorpUsers && allCorpUsers.size() > 0) {
				MessageUserService messageUserService = (MessageUserService) Config
						.getAppContext().getBean("MessageUserService");

				String[] muIds = CibIdGenerator.getIdsForOperation(
						"MessageUser", allCorpUsers.size());
				CorpUser corpUser = null;
				for (int i = 0; i < allCorpUsers.size(); i++) {
					MessageUser messageUser = new MessageUser();
					messageUser.setMessage(message);
					messageUser.setMuStatus(Constants.STATUS_NORMAL);
					messageUser.setMuId(muIds[i]);
					corpUser = (CorpUser) allCorpUsers.get(i);
					messageUser.setUserId(corpUser.getUserId());
					messageUserService.add(messageUser);
				}
			}
		} else if (Constants.TXN_SUBTYPE_MESSAGE_DELETE.equals(txnType)) {
			message.setStatus(Constants.STATUS_REMOVED);
			message.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			messageService.update(message);

			MessageUserService messageUserService = (MessageUserService) Config
					.getAppContext().getBean("MessageUserService");
			messageUserService.updateByMessage(message.getMessageId(),
					Constants.STATUS_REMOVED);
		}

		return true;
	}

	public boolean reject(String txnType, String id, CibAction bean)
			throws NTBException {
		MessageService messageService = (MessageService) Config.getAppContext()
				.getBean("MessageService");
		Message message = messageService.load(id);
		if (Constants.TXN_SUBTYPE_MESSAGE_ADD.equals(txnType)) {
			message.setStatus(Constants.STATUS_REMOVED);
			message.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
			messageService.update(message);
		} else if (Constants.TXN_SUBTYPE_MESSAGE_DELETE.equals(txnType)) {
			message.setStatus(Constants.STATUS_NORMAL);
			message.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			messageService.update(message);
		}
		return true;
	}

	public String viewDetail(String txnType, String id, CibAction bean)
			throws NTBException {
		MessageService messageService = (MessageService) Config.getAppContext()
				.getBean("MessageService");
		Message message = messageService.load(id);
		Map resultData = bean.getResultData();
		convertPojo2Map(message, resultData);
		bean.setResultData(resultData);
		return "/WEB-INF/pages/srv/message/message_view.jsp";
	}

	public boolean cancel(String txnType, String id, CibAction bean)
			throws NTBException {
		return reject(txnType, id, bean);
	}
	
	
	public void listPrintPassword() throws NTBException {
		//Log.info("SessionId:[" + this.getRequest().getSession().getId() + "]");
		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext
		.getBean("genericJdbcDao");
		String sql = "select t.print_id, t.cif_no, t.cif_name, t.login_id, to_char(t.create_time, 'yyyy-mm-dd hh24:mi:ss') as create_time, t.remark from corp_print_info t where t.status = ? order by t.create_time desc";
		List printList = new ArrayList();
		List tmpList = new ArrayList();
		try {
			tmpList = dao.query(sql, new Object[] {"0"});
			for(int i = 0; tmpList!=null && i<tmpList.size(); i++){
				Map printInfo = (Map)tmpList.get(i);
				String transPwsdFlag = "Y";
				String cifNo = Utils.null2EmptyWithTrim(printInfo.get("CIF_NO"));
				String userId = Utils.null2EmptyWithTrim(printInfo.get("LOGIN_ID"));
			    String sql1 = "select user_id from corp_user where corp_id=? and user_id=? and role_id=?";
			    List list1 = dao.query(sql1, new Object[]{cifNo, userId, Constants.ROLE_APPROVER});
			
			    String sql2 = "select t.authentication_mode from corporation t where t.corp_id=? and t.authentication_mode=?";
			    List list2 = dao.query(sql2, new Object[]{cifNo, Constants.AUTHENTICATION_SECURITY_CODE});
			
			    if(list1!=null && list1.size()>0 && list2!=null && list2.size()>0){
			    	transPwsdFlag = "Y";
			    }else{
			    	transPwsdFlag = "N";
			    }
			    printInfo.put("TRANS_PWSD_FLAG", transPwsdFlag);
			    printList.add(printInfo);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.error("listPrintPassword Error", e);
			throw new NTBException("System Error"); 
		}
		List rsltList = KeyNameUtils.listDash2CaseDiff(printList);
	    Map resultData = new HashMap();
	    resultData.put("printList", rsltList);
	    setResultData(resultData);
	}
}
