package app.cib.service.srv;

import java.util.List;

import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;

import app.cib.bo.srv.Message;
import app.cib.dao.srv.MessageDao;

public class MessageServiceImpl implements MessageService {
	private MessageDao messageDao = null;

	public void add(Message message) throws NTBException {
		try {
			messageDao.add(message);
		} catch (Exception e) {
			Log.debug("Process error at MessageServiceImpl.add(Message message): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			} else {

			}
		}

	}

	public void delete(String messageId) throws NTBException {
		try {
			Message message = load(messageId);
			messageDao.delete(message);
		} catch (Exception e) {
			Log.debug("Process error at MessageServiceImpl.delete(String messageId): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			} else {

			}
		}

	}

	public List listByBank(int count) throws NTBException {
		List aList = null;
		try {
			aList = messageDao.findByBank(count);
		} catch (Exception e) {
			Log.debug("Process error at MessageServiceImpl.listByBank(int count): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			} else {

			}
		}
		return aList;
	}

	public void update(Message message) throws NTBException {
		try {
			messageDao.update(message);
		} catch (Exception e) {
			Log.debug("Process error at MessageServiceImpl.update(Message message): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			} else {

			}
		}
	}

	public MessageDao getMessageDao() {
		return messageDao;
	}

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public Message load(String messageId) throws NTBException {
		return (Message) messageDao.load(Message.class, messageId);
	}

}
