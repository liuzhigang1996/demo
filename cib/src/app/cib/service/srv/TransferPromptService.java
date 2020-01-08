package app.cib.service.srv;

import java.util.List;

import com.neturbo.set.exception.NTBException;

import app.cib.bo.bnk.BankUser;
import app.cib.bo.srv.TxnPrompt;


public interface TransferPromptService {


//	public abstract TxnPrompt loadByMessageId(String messageId) throws NTBException;
	public TxnPrompt load(String messageId) throws NTBException;
	public TxnPrompt loadByTxnType(String txnType,String status) throws NTBException;
	public void add(TxnPrompt txnPrompt) throws NTBException;
	public void update(TxnPrompt txnPrompt) throws NTBException;
	public  String format(String descriptionC) throws NTBException;
	public  void addLoginMessageShow(List cMessageList,String messageId, String language) throws NTBException;
	public List getLoginMessageShow(String messageId, String language) throws NTBException;
	

	

}
