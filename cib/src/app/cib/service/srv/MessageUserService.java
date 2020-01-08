package app.cib.service.srv;

import java.util.List;

import com.neturbo.set.exception.NTBException;

import app.cib.bo.srv.MessageUser;

public interface MessageUserService {
	MessageUser load(String muId) throws NTBException;

	void add(MessageUser messageUser) throws NTBException;

	void update(MessageUser messageUser) throws NTBException;
	
	void updateByMessage(String messageId,String status) throws NTBException;

	void delete(String muId) throws NTBException;

	List list(String userId) throws NTBException;

	List messageList2MapList(List messageList) throws NTBException;

}
