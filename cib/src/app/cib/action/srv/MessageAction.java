package app.cib.action.srv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;

import app.cib.bo.srv.MessageUser;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.service.srv.MessageUserService;

public class MessageAction extends CibAction {

	public void list() throws NTBException {
		String fetchSize = this.getParameter("fetchSize");
		if (null == fetchSize || "".equals(fetchSize)) {
			fetchSize = "20";
		}

		CorpUser user = (CorpUser) this.getUser();
		MessageUserService messageUserService = (MessageUserService) Config
				.getAppContext().getBean("MessageUserService");
		List messageList = messageUserService.list(user.getUserId());

		HashMap resultData = new HashMap();
		messageList = messageUserService.messageList2MapList(messageList);
		resultData.put("messageList", messageList);
		setResultData(resultData);

	}

	public void view() throws NTBException {
		String muId = getParameter("muId");
		MessageUserService messageUserService = (MessageUserService) Config
				.getAppContext().getBean("MessageUserService");
		MessageUser messageUser = messageUserService.load(muId);
		
		//add by hjs 20070205
		messageUser.setMuStatus("1");
		messageUserService.update(messageUser);

		Map resultData = getResultData();
		convertPojo2Map(messageUser, resultData);
		convertPojo2Map(messageUser.getMessage(), resultData);
		setResultData(resultData);
	}

	public void delete() throws NTBException {
		String[] muId = getParameterValues("muId");
		if (null != muId) {
			MessageUserService messageUserService = (MessageUserService) Config
					.getAppContext().getBean("MessageUserService");
			for (int i = 0; i < muId.length; i++) {
				messageUserService.delete(muId[i]);
			}
		}

	}

}
