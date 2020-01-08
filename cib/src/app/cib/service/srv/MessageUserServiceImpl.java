package app.cib.service.srv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import app.cib.bo.srv.MessageUser;
import app.cib.dao.srv.MessageUserDao;
import app.cib.dao.srv.MessageUserJdbcDao;
import app.cib.util.Constants;

import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;

public class MessageUserServiceImpl implements MessageUserService {

	private MessageUserDao messageUserDao = null;

	private MessageUserJdbcDao messageUserJdbcDao = null;

	public void add(MessageUser messageUser) throws NTBException {
		try {
			messageUserDao.add(messageUser);
		} catch (Exception e) {
			Log.debug("Process error at MessageUserServiceImpl.add(MessageUser messageUser): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			} else {

			}
		}
	}

	public void delete(String muId) throws NTBException {
		try {
			MessageUser messageUser = load(muId);
			messageUser.setMuStatus(Constants.STATUS_REMOVED);
			messageUserDao.update(messageUser);
		} catch (Exception e) {
			Log.debug("Process error at MessageUserServiceImpl.delete(String muId): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			} else {

			}
		}
	}

	public MessageUser load(String muId) throws NTBException {
		return (MessageUser) messageUserDao.load(MessageUser.class, muId);
	}

	public void update(MessageUser messageUser) throws NTBException {
		try {
			messageUserDao.update(messageUser);
		} catch (Exception e) {
			Log.debug("Process error at MessageUserService.load(String muId): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			} else {

			}
		}

	}

	public MessageUserDao getMessageUserDao() {
		return messageUserDao;
	}

	public void setMessageUserDao(MessageUserDao messageUserDao) {
		this.messageUserDao = messageUserDao;
	}

	public List list(String userId) throws NTBException {
		try {
			return messageUserDao.findByUser(userId);
		} catch (Exception e) {
			throw new NTBException(e.getMessage());
		}
	}

	public List messageList2MapList(List messageList) throws NTBException {
		List mapList = null;

		if (null != messageList && messageList.size() > 0) {
			mapList = new ArrayList(messageList.size());
			HashMap map = null;
			MessageUser messageUser = null;
			for (int i = 0; i < messageList.size(); i++) {
				messageUser = (MessageUser) messageList.get(i);
				map = new HashMap();
				try {
					map.putAll(BeanUtils.describe(messageUser));
					map.putAll(BeanUtils.describe(messageUser.getMessage()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				mapList.add(map);
			}
		}

		return mapList;
	}

	public void updateByMessage(String messageId, String status)
			throws NTBException {
		try {
			messageUserJdbcDao
					.update(
							"update MESSAGE_USER set MU_STATUS = ? where MESSAGE_ID = ? ",
							new String[] { status, messageId });
		} catch (Exception e) {
			throw new NTBException(e.getMessage());
		}

	}

	public MessageUserJdbcDao getMessageUserJdbcDao() {
		return messageUserJdbcDao;
	}

	public void setMessageUserJdbcDao(MessageUserJdbcDao messageUserJdbcDao) {
		this.messageUserJdbcDao = messageUserJdbcDao;
	}
}
