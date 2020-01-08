package app.cib.service.srv;

import java.util.List;

import com.neturbo.set.exception.NTBException;

import app.cib.bo.srv.Message;

/**
 * @author panwen
 * 
 */
public interface MessageService {
	public Message load(String messageId) throws NTBException;

	public void add(Message message) throws NTBException;

	public void update(Message message) throws NTBException;

	public void delete(String messageId) throws NTBException;

	public List listByBank(int count) throws NTBException;

}
