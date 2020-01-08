package app.cib.service.bnk;

import java.util.*;

import app.cib.util.*;

import com.neturbo.set.core.*;
import com.neturbo.set.database.*;
import com.neturbo.set.exception.*;
import com.neturbo.set.utils.Utils;

import org.springframework.context.*;

public class ViewerAccessListServiceImpl implements ViewerAccessListService {
    private GenericJdbcDao genericJdbcDao;

    public List listViewer() throws NTBException{
		String sql = "SELECT USER_ID, concat(USER_ID, concat(' - ', USER_NAME)) as USER_NAME FROM BANK_USER WHERE ROLE_ID=? AND STATUS=?";
		try{
			List subList = genericJdbcDao.query(sql, new Object[] {Constants.ROLE_BANK_VIEWER, Constants.STATUS_NORMAL});
			return subList;
		} catch (Exception e) {
			Log.error("ViewerAccessListServiceImpl.listViewer ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
    }
        
    public List listSelectedCorpListByViewer(String userId) throws NTBException {
		String sql = "SELECT V.CORP_ID, concat(V.CORP_ID, concat(' - ', C.CORP_NAME)) as CORP_NAME FROM VIEWER_ACCESS_LIST V, CORPORATION C WHERE V.USER_ID=? AND V.CORP_ID = C.CORP_ID AND C.STATUS=? AND V.STATUS=?";
		try{
			List subList = genericJdbcDao.query(sql, new Object[] {userId, Constants.STATUS_NORMAL, Constants.STATUS_NORMAL});
			return subList;
		} catch (Exception e) {
			Log.error("ViewerAccessListServiceImpl.listSelectedCorpListByViewer ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
	}

    public List listSelectedCorpListByBatchId(String batchId) throws NTBException {
		String sql = "SELECT V.CORP_ID, concat(V.CORP_ID, concat(' - ', C.CORP_NAME)) as CORP_NAME FROM VIEWER_ACCESS_LIST V, CORPORATION C WHERE V.BATCH_ID=? AND V.CORP_ID = C.CORP_ID AND C.STATUS=?";
		try{
			List subList = genericJdbcDao.query(sql, new Object[] {batchId, Constants.STATUS_NORMAL});
			return subList;
		} catch (Exception e) {
			Log.error("ViewerAccessListServiceImpl.listSelectedCorpListByBatchId ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
    }
    
    public List listCandidateCorp(List selectedList) throws NTBException {
		String sql = "SELECT CORP_ID, concat(CORP_ID, concat(' - ', CORP_NAME)) as CORP_NAME FROM CORPORATION WHERE STATUS=?";
		try{
			List corpList = genericJdbcDao.query(sql, new Object[] {Constants.STATUS_NORMAL});
			corpList.removeAll(selectedList);
			
			return corpList;
		} catch (Exception e) {
			Log.error("ViewerAccessListServiceImpl.listCandidateCorp ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
    }

    public List listAllCorp() throws NTBException{
		String sql = "SELECT CORP_ID, concat(CORP_ID, concat(' - ', CORP_NAME)) as CORP_NAME FROM CORPORATION WHERE STATUS=?";
		try{
			List corpList = genericJdbcDao.query(sql, new Object[] {Constants.STATUS_NORMAL});			
			return corpList;
		} catch (Exception e) {
			Log.error("ViewerAccessListServiceImpl.listAllCorp ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
    }

    public boolean isPending(String viewerId) throws NTBException{
    	boolean isPending = false;
    	try{
    		List batchIdList = genericJdbcDao.query("SELECT BATCH_ID FROM VIEWER_ACCESS_LIST WHERE USER_ID=? AND STATUS=?", 
    				new Object[] {viewerId, Constants.STATUS_PENDING_APPROVAL});    		
    		if (batchIdList.size() > 0){
    			isPending = true;
    		}    		    		
        	return isPending;
    	} catch (Exception e) {
			Log.error("ViewerAccessListServiceImpl.isPending ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}    	
    }
    
    
    public String getBatchIdByViewer(String viewerId) throws NTBException{
    	String batchId = "";
    	try{
    		List batchIdList = genericJdbcDao.query("SELECT DISTINCT BATCH_ID FROM VIEWER_ACCESS_LIST WHERE USER_ID=? and STATUS=?", 
    				new Object[] {viewerId, Constants.STATUS_NORMAL});    		
    		if (batchIdList.size() > 0){
    			batchId = (((Map)batchIdList.get(0)).get("BATCH_ID")).toString();
    		}
    		    		
    		return batchId;
    	} catch (Exception e) {
			Log.error("ViewerAccessListServiceImpl.getBatchIdByViewer ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
    }
    
    public String getBatchIdBeforeByBatchId(String batchId) throws NTBException{
    	String batchIdBefore = "";
    	try{
    		List batchIdList = genericJdbcDao.query(
    				"SELECT DISTINCT BATCH_ID_BEFORE FROM VIEWER_ACCESS_LIST WHERE BATCH_ID=?", 
    				new Object[] {batchId});    		
    		if (batchIdList.size() > 0){
    			batchIdBefore = Utils.null2EmptyWithTrim((((Map)batchIdList.get(0)).get("BATCH_ID_BEFORE")));//update by linrui 20180305
    		}
    		    		
    		return batchIdBefore;
    	} catch (Exception e) {
			Log.error("ViewerAccessListServiceImpl.getBatchIdBeforeByBatchId ERROR :", e);    		
			throw new NTBException("err.sys.DBError");
		}
    }

    public List getViewerByBatchId(String batchId) throws NTBException{
    	String viewerId = "";
    	try{
    		List batchIdList = genericJdbcDao.query("SELECT DISTINCT USER_ID FROM VIEWER_ACCESS_LIST WHERE BATCH_ID=?", 
    				new Object[] {batchId});    		
    		if (batchIdList.size() > 0){
    			viewerId = (((Map)batchIdList.get(0)).get("USER_ID")).toString();
    		}

			List resList = genericJdbcDao.query("SELECT USER_ID, concat(USER_ID, concat(' - ', USER_NAME)) as USER_NAME FROM BANK_USER WHERE USER_ID=?",
							new Object[] { viewerId });
    		    		
    		return resList;
    	} catch (Exception e) {
			Log.error("ViewerAccessListServiceImpl.getViewerByBatchId ERROR :", e);    		
			throw new NTBException("err.sys.DBError");
		}    	
    }
    
    public void insertForApprove(List newAccessList)throws NTBException{
		try{
			genericJdbcDao.batchAdd("VIEWER_ACCESS_LIST", newAccessList);
		} catch (Exception e) {
			Log.error("ViewerAccessListServiceImpl.insertForApprove ERROR :", e);    					
			throw new NTBException("err.sys.DBError");
		}    	
    }
    
    public void updateForApprove(String batchId)throws NTBException{
		try{
	    	ApplicationContext appContext = Config.getAppContext();
	        ViewerAccessListService ViewerAccessListService = (ViewerAccessListService)appContext.getBean("ViewerAccessListService");

			String batchIdBefore = ViewerAccessListService.getBatchIdBeforeByBatchId(batchId);
			
			// old list
			genericJdbcDao.update("UPDATE VIEWER_ACCESS_LIST SET STATUS=?,LAST_UPDATE_TIME=? WHERE BATCH_ID=?", 
					new Object[] {Constants.STATUS_REMOVED, new Date(), batchIdBefore});

			// new list
			genericJdbcDao.update("UPDATE VIEWER_ACCESS_LIST SET STATUS=?, AUTH_STATUS=?, LAST_UPDATE_TIME=? WHERE BATCH_ID=?", 
					new Object[] {Constants.STATUS_NORMAL, Constants.AUTH_STATUS_COMPLETED, new Date(), batchId});

		} catch (Exception e) {
			Log.error("ViewerAccessListServiceImpl.updateForApprove ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}    	
    }

    public void updateForReject(String batchId)throws NTBException{
		try{			
			genericJdbcDao.update("UPDATE VIEWER_ACCESS_LIST SET STATUS=?, AUTH_STATUS=?, LAST_UPDATE_TIME=? WHERE BATCH_ID=?", 
					new Object[] {Constants.STATUS_REMOVED, Constants.AUTH_STATUS_REJECTED, new Date(), batchId});

		} catch (Exception e) {
			Log.error("ViewerAccessListServiceImpl.updateForReject ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}    	
    }
    
    
    public GenericJdbcDao getGenericJdbcDao() {
        return genericJdbcDao;
    }

    public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
        this.genericJdbcDao = genericJdbcDao;
    }
    
    
}
