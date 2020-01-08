package app.cib.service.srv;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.cib.bo.srv.ChequeBookRequest;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibIdGenerator;
import app.cib.core.CibTransClient;
import app.cib.dao.srv.ChequeBookRequestDao;
import app.cib.util.Constants;
import app.cib.util.UploadReporter;

import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.KeyNameUtils;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

public class ChequeBookRequestServiceImpl implements ChequeBookRequestService {

	private ChequeBookRequestDao chequeBookRequestDao;
	private GenericJdbcDao genericJdbcDao;
	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	public void add(ChequeBookRequest bookRequest) throws NTBException {
		chequeBookRequestDao.add(bookRequest);
	}

	public ChequeBookRequest load(String transNo) throws NTBException {
		return (ChequeBookRequest) chequeBookRequestDao.load(ChequeBookRequest.class, transNo);
	}

	public void approveChequeBookRequest(ChequeBookRequest bookRequest,CorpUser corpUser)
			throws NTBException {
		if(null != bookRequest){
			Map toHost = new HashMap();
	    	Map fromHost = new HashMap();
	    	
	    	bookRequest.setStatus(Constants.STATUS_NORMAL);
	    	bookRequest.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
	    	bookRequest.setExecuteTime(new Date());
	    	chequeBookRequestDao.update(bookRequest);
			
	    	//add by linrui for pickup type 20180324
	    	String packName  = bookRequest.getPackageName();
	    	String packAdress  = bookRequest.getPackageAdress();
	    	//end
	        CibTransClient testClient = new CibTransClient("CIB", "ZJ29");
	        //up to host
	        toHost.put("REQ_REF", CibIdGenerator.getTDRef()); //update by gan 20180102
	        toHost.put("ACCOUNT_NO",bookRequest.getAccountNo());
	        toHost.put("NO_OF_BOOKS", KeyNameUtils.fillWith((int)bookRequest.getNoOfBook()+"", "0", 2, true));
	        toHost.put("CCY", bookRequest.getPayCurrency());
	        toHost.put("CBK_TYPE", getCBKType(bookRequest.getPayCurrency()));
	        toHost.put("DEL_MTH", bookRequest.getPickupType());
	        toHost.put("COLBR", "".equals(Utils.null2EmptyWithTrim(packName).trim())?801:0);
	        
	        packName = Utils.appendSpace(packName, 210) ;
	        packAdress = Utils.appendSpace(packAdress, 630) ;
	        
	        
	        
	        toHost.put("NAME01",  packName.substring(0, 35));
	        toHost.put("NAME02",  packName.substring(35, 70));
	        toHost.put("ADDR01",  packAdress.substring(0, 35));
	        toHost.put("ADDR02",  packAdress.substring(35, 70));
	        toHost.put("ADDR03",  packAdress.substring(70, 105));
	        toHost.put("ADDR04",  packAdress.substring(105, 140));
	        toHost.put("ADDR05",  packAdress.substring(140, 175));
	        toHost.put("ADDR06",  packAdress.substring(175, 210));

//	        toHost.put("PICKUP_BRANCH_CODE", bookRequest.getPickupBranchCode());
	        testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_BILL_PAYMENT, CibTransClient.ACCTYPE_3RD_PARTY, bookRequest.getTransNo());
	        //send to host
			fromHost = testClient.doTransaction(toHost);
			

			//report
			Map reportMap = new HashMap();
			reportMap.put("TRAN_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRAN_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("TRAN_CODE", "ZJ29");
			reportMap.put("CORR_FLAG", " ");
			reportMap.put("TRAN_REF", fromHost.get("TELLER_ID").toString()+ fromHost.get("UNIQUE_SEQUENCE_NO").toString());
			reportMap.put("NO_OF_BOOK", bookRequest.getNoOfBook());
		    reportMap.put("CURR_ACCT",bookRequest.getAccountNo());
			reportMap.put("PICKUP_BRANCH", bookRequest.getPickupBranchCode());
			reportMap.put("USER_ID", bookRequest.getUserId());
			reportMap.put("CORP_ID", bookRequest.getCorpId());
			reportMap.put("STATUS", fromHost.get("RESPONSE_CODE"));	
			reportMap.put("REJECT_CODE", fromHost.get("REJECT_CODE"));
			reportMap.put("TRAN_USER_REF",bookRequest.getTransNo());
			
			//UploadReporter.write("RP_CHQBOOK", reportMap);
			 //code=3 success
	        if(!testClient.isSucceed()){
	        	throw new NTBHostException(testClient.getErrorArray());
	        }
		}
	}

	public void rejectChequeBookRequest(ChequeBookRequest bookRequest)
			throws NTBException {
		if (null != bookRequest) {
			bookRequest.setStatus(Constants.STATUS_REMOVED);
			bookRequest.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
			bookRequest.setExecuteTime(new Date());
			chequeBookRequestDao.update(bookRequest);
		}
	}

	public ChequeBookRequestDao getChequeBookRequestDao() {
		return chequeBookRequestDao;
	}

	public void setChequeBookRequestDao(
			ChequeBookRequestDao chequeBookRequestDao) {
		this.chequeBookRequestDao = chequeBookRequestDao;
	}
	/**
	 * get cifNo by corpId
	 * 
	 * @param corpId
	 * @return
	 * @throws NTBException
	 */
	public String getCcyByAcct(String acct) throws NTBException {
		try {
			CorpAccount corpAccount = (CorpAccount)chequeBookRequestDao.load(CorpAccount.class,acct);
			return corpAccount.getCurrency();
		} catch (Exception e) {
			Log.error(e);
			throw new NTBException("err.txn.GetCIFNoException");
		}		
	}
	//ADD BY LINRUI 20180324
	public String getCBKType(String ccy){
		RBFactory rb = RBFactory.getInstance("app.cib.resource.srv.chequeBookRequest");
		if( null != rb.getString(ccy)){
			return rb.getString(ccy).toString();
		}else{
			return "H1";
		}
		
	}

	public void removeCheque(String tableName, String transNo) {
		genericJdbcDao.removeCheque(tableName,transNo);
		
	}


}
