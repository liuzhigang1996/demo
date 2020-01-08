package app.cib.service.srv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;

import app.cib.bo.srv.TxnPrompt;
import app.cib.dao.srv.TransferPromptDao;

public class TransferPromptServiceImpl implements TransferPromptService {

	private static final Logger logger = Logger.getLogger(TransferPromptServiceImpl.class);

	private GenericJdbcDao genericJdbcDao;
	
	private TransferPromptDao transferPromptDao = null;

	private GenericHibernateDao genericHibernateDao;

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	public GenericHibernateDao getGenericHibernateDao() {
		return genericHibernateDao;
	}

	public void setGenericHibernateDao(GenericHibernateDao genericHibernateDao) {
		this.genericHibernateDao = genericHibernateDao;
	}
	
	public TransferPromptDao getTransferPromptDao() {
		return transferPromptDao;
	}

	public void setTransferPromptDao(TransferPromptDao messageDao) {
		this.transferPromptDao = transferPromptDao;
	}


	/*public TxnPrompt loadByMessageId(String messageId) throws NTBException {
		TxnPrompt txnPrompt = null;
		Map reqMap = new HashMap();
		reqMap.put("messageId", messageId);
		try {
			List list = genericHibernateDao.list(TxnPrompt.class, reqMap);
			if (list != null && list.size() > 0) {
				txnPrompt = (TxnPrompt) list.get(0);
			}
		} catch (Exception e) {
			logger.error("Error loadByRecId", e);
			throw new NTBException("err.sys.GeneralError");
		}
		return txnPrompt;
	}*/
	
	public TxnPrompt load(String messageId) throws NTBException {
		try {
			return (TxnPrompt) genericHibernateDao.load(TxnPrompt.class, messageId);
		} catch (Exception e) {
			logger.error("Error loadByRecId", e);
			throw new NTBException("err.sys.GeneralError");
		}
	}
	
	public  TxnPrompt loadByTxnType(String txnType,String status) throws NTBException {
		String[] conditions = new String[]{txnType,status};
		String hql = "from TxnPrompt t where t.txnType = ? and t.status=?";
		try {
			List<TxnPrompt> retList =  genericHibernateDao.list(hql, conditions);
			if(retList ==null || retList.size()!=1){
				return null;
			}else{
				return retList.get(0);
			}
		} catch (Exception e) {
			logger.error("Error loadByRecId", e);
			throw new NTBException("err.sys.GeneralError");
		}
	}

	public void add(TxnPrompt txnPrompt) throws NTBException {
		try {
			genericHibernateDao.add(txnPrompt);
			
		} catch (Exception e) {
			logger.debug("Process error at MessageServiceImpl.add(Message message): ", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			} else {

			}
		}
	}

	public void update(TxnPrompt txnPrompt) throws NTBException {
		genericHibernateDao.update(txnPrompt);
	}

	@Override
	public String format(String description) {
		description = description.replace("\r\n", "<br>");
		description = description.replace(" ", "&nbsp;");
		return description;
	}

	@Override
	public void addLoginMessageShow(List MessageList,String messageId,String language) throws NTBException {
		
		String sql = "";
		try{
			for (int i = 0; i < MessageList.size(); i++) {
				Map retMap = (Map) MessageList.get(i);
				String title = "";
				String content = "";
				if("C".equals(language)){
					title = (String) retMap.get("CTitle");
					content = (String) retMap.get("CContent");
				}else{
					title = (String) retMap.get("ETitle");
					content = (String) retMap.get("EContent");
				}
				title = title.replaceAll("'", "''");
				content = content.replaceAll("'", "''");
				sql = "insert into LOGIN_MESSAGE_SHOW (MESSAGE_ID,LOCAL_CODE,MESSAGE_TITLE,MESSAGE1) values('"+messageId+"','"+language+"','"+title+"','"+content+"')";
				genericJdbcDao.execute(sql);
			}
		}catch (Exception e){
			throw new NTBException("DBError");
		}
	}

	@Override
	public List getLoginMessageShow(String messageId, String language) throws NTBException {
		String sql = "select MESSAGE_TITLE,MESSAGE1 from LOGIN_MESSAGE_SHOW where MESSAGE_ID = ? and LOCAL_CODE=?";
		List retList = new ArrayList();
		try {
			List result = genericJdbcDao.query(sql, new String[]{messageId,language});
			if(result == null || result.size() ==0){
				return null;
			}
			for (int i = 0; i < result.size(); i++) {
				Map retMap = (Map) result.get(i);
				Map newMap = new HashMap();
				if("C".equals(language)){
					newMap.put("CTitle", retMap.get("MESSAGE_TITLE"));
					newMap.put("CContent", retMap.get("MESSAGE1"));
				}else{
					newMap.put("ETitle", retMap.get("MESSAGE_TITLE"));
					newMap.put("EContent", retMap.get("MESSAGE1"));
				}
				retList.add(newMap);
			}
		} catch (Exception e) {
			throw new NTBException("DBError");
		}
		return retList;
	}


}
