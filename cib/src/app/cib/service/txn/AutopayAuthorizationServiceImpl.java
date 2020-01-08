package app.cib.service.txn;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.ObjectNotFoundException;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.Autopay;
import app.cib.bo.txn.AutopayAuthorization;
import app.cib.bo.txn.AutopayAuthorizationHis;
import app.cib.core.CibTransClient;
import app.cib.dao.txn.AutopayAuthorizationDao;
import app.cib.util.Constants;
import app.cib.util.UploadReporter;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.utils.KeyNameUtils;
import com.neturbo.set.utils.Utils;

public class AutopayAuthorizationServiceImpl implements
		AutopayAuthorizationService {

	private GenericJdbcDao genericJdbcDao;
	private AutopayAuthorizationDao autopayAuthorizationDao;
		
	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	public List listAutopay(String corpId) throws NTBException {
		// TODO Auto-generated method stub
		return autopayAuthorizationDao.listAutopay(corpId);
	}
	
	public void addAutopay(AutopayAuthorization autopayAuthorization)
			throws NTBException {
		autopayAuthorizationDao.add(autopayAuthorization);
	}

	public AutopayAuthorization loadAutopayAuthorization(String transNo,
			String apsCode, String contractNo, String corpId) throws NTBException {
		// TODO Auto-generated method stub
		return autopayAuthorizationDao.loadAutopayAuthorization(transNo, apsCode, contractNo, corpId);
	}
	public Autopay loadAutopay(String apsCode, String contractNo, String corpId) throws NTBException {
		// TODO Auto-generated method stub
		
		Autopay autopay = autopayAuthorizationDao.loadAutopay(apsCode, contractNo, corpId);
		if (autopay!=null){
			if(autopay.getPaymentLimit()==Double.parseDouble(Constants.AUTOPAYMENT_NO_LIMIT)){
          		autopay.setPayOption(Constants.AUTOPAYMENT_PAYMENT_FULL);
          	}else if ((autopay.getPaymentLimit()==0)){
          		autopay.setPayOption(Constants.AUTOPAYMENT_PAYMENT_MIN);
          	}else{
          		autopay.setPayOption(Constants.AUTOPAYMENT_PAYMENT_INPUT);
          	}
			autopay.setPayAcct(Utils.removePrefixZero(autopay.getPayAcct()));
		}
		return autopay;
	}
	
	public void approveAutopay(Autopay autopay, AutopayAuthorization autopayAuthorization,AutopayAuthorizationHis autopayAuthorizationHis,CorpUser corpUser, boolean isExsitAutopayAuthorization) throws NTBException{
		if(null != autopayAuthorization){
			Map toHost = new HashMap();
	    	Map fromHost = new HashMap();
	    	
	    	//UPDATE本地数据库信息
	    	if(null!=autopayAuthorization.getPayOption() && autopayAuthorization.getPayOption().equals("F")){
	        	autopayAuthorization.setPaymentLimit(Double.parseDouble(Constants.AUTOPAYMENT_NO_LIMIT));
	        }
	        //Credit Card payment set CARD_PAYMENT_OPTION null
	        if (!autopayAuthorization.getApsCode().equals(Config.getProperty("app.autopay.creditcard.code"))){
	     
	        	autopayAuthorization.setPayOption("");
	        }
	        autopay.setPayAcct(Utils.prefixZero(autopay.getPayAcct(), 16));
	    	if(Constants.AUTOPAYMENT_MODE_DELETE.equals(autopayAuthorization.getMode())){
	    		autopayAuthorization.setStatus(Constants.STATUS_REMOVED);
	    		autopayAuthorizationDao.delete(autopay);
		    	/*autopayAuthorization.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
		    	autopayAuthorization.setExecuteTime(new Date());
		    	autopayAuthorizationDao.update(autopayAuthorization);*/
//		    	autopayAuthorizationDao.delete(autopayAuthorization);
	    	} else if (Constants.AUTOPAYMENT_MODE_ADD.equals(autopayAuthorization.getMode())){
	    		autopayAuthorization.setStatus(Constants.STATUS_NORMAL);
	    		autopayAuthorizationDao.add(autopay);
	    	} else if(Constants.AUTOPAYMENT_MODE_EDIT.equals(autopayAuthorization.getMode())){
	    		autopayAuthorization.setStatus(Constants.STATUS_NORMAL);
	    		autopayAuthorizationDao.update(autopay);
	    	} else {
	    		autopayAuthorization.setStatus(Constants.STATUS_NORMAL);
	    	}
//	    	autopayAuthorization.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
	    	autopayAuthorization.setExecuteTime(new Date());
	    	autopayAuthorizationHis.setExecuteTime(new Date());
	    	updateAutopayAuthorizationHis(autopayAuthorizationHis);
	    	if (isExsitAutopayAuthorization && !Constants.AUTOPAYMENT_MODE_DELETE.equals(autopayAuthorization.getMode())){
	    		updateAutopayAuthorization(autopayAuthorization);
	    	}
	        CibTransClient testClient = new CibTransClient("CIB", "ZB08");
	        //up to host
	        toHost.put("CIF_NUMBER",corpUser.getCorpId());
	        String apsCode = autopayAuthorization.getApsCode() ;
	        apsCode = KeyNameUtils.fillWith(apsCode, "0", 10, true) ;
	        toHost.put("APS_CODE", apsCode);
	        toHost.put("MAINTENANCE_CODE", autopayAuthorization.getMode());
	        toHost.put("CONTRACT_NUMBER", autopayAuthorization.getContractNo());
	        String payAcct = autopayAuthorization.getPayAcct() ;
	        payAcct = KeyNameUtils.fillWith(payAcct, "0", 16, true) ;
	        toHost.put("PAYMENT_ACCOUNT", payAcct);
	        toHost.put("PAYMENT_LIMIT", autopayAuthorization.getPaymentLimit());
	        toHost.put("CARD_PAYMENT_OPTION", autopayAuthorization.getPayOption());
	        
	        testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_BILL_PAYMENT, CibTransClient.ACCTYPE_3RD_PARTY, autopayAuthorization.getTransNo());
	        //send to host
			fromHost = testClient.doTransaction(toHost);
			
			 //如果交易不成功则报出主机错误
	        if(!testClient.isSucceed()){
	        	throw new NTBHostException(testClient.getErrorArray());
	        }
			//report
	        writeLog(autopayAuthorization, fromHost);
		}
	}
	
	public void updateAutopay(AutopayAuthorization autopayAuthorization) throws NTBException {
		autopayAuthorizationDao.update(autopayAuthorization) ;
	}
	
	public void rejectAutopay(AutopayAuthorizationHis autopayAuthorizationHis) throws NTBException {
		if (autopayAuthorizationHis != null) {
			autopayAuthorizationHis.setStatus(Constants.STATUS_REMOVED);
			autopayAuthorizationHis.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
			autopayAuthorizationHis.setExecuteTime(new Date());
			autopayAuthorizationDao.update(autopayAuthorizationHis);
		}
	}
	
	public AutopayAuthorizationDao getAutopayAuthorizationDao() {
		return autopayAuthorizationDao;
	}

	public void setAutopayAuthorizationDao(
			AutopayAuthorizationDao autopayAuthorizationDao) {
		this.autopayAuthorizationDao = autopayAuthorizationDao;
	}

	public void addAutopayHis(AutopayAuthorizationHis autopayAuthorizationHis) {
		autopayAuthorizationDao.add(autopayAuthorizationHis);
		
	}

	public AutopayAuthorizationHis loadAutopayAuthorizationHis(String id) {
		return (AutopayAuthorizationHis) autopayAuthorizationDao.load(AutopayAuthorizationHis.class,id);
	}

	public AutopayAuthorization loadauthorization(String transNo) throws NTBException {
		if ((transNo != null) && (!transNo.equals(""))) {
			return autopayAuthorizationDao.loadauthorization(transNo);
        } else {
            throw new NTBException("err.txn.TransIDIsNullOrEmpty");
        }
	}
	
	public void updateAutopayAuthorization(
			AutopayAuthorization autopayAuthorization) {
		autopayAuthorizationDao.update(autopayAuthorization);
		
	}

	public void updateAutopayAuthorizationHis(
			AutopayAuthorizationHis autopayAuthorizationHis) {
		autopayAuthorizationDao.update(autopayAuthorizationHis);
	}

	public List listAutopayAuthorization(String corpId) throws NTBException {
        
        List autoList =  autopayAuthorizationDao.listAutopay(corpId);
        Autopay autopay = null;
        AutopayAuthorizationHis autopayAuthorizationHis = null;
        
        if(null !=autoList){
        	  for(int i=0; i<autoList.size(); i++) {
        		autopay = (Autopay) autoList.get(i);
              	autopayAuthorizationHis = autopayAuthorizationDao.getHisbyKey(corpId,autopay.getApsCode(),autopay.getContractNo());
              	autopay.setPayAcct(Utils.removePrefixZero(autopay.getPayAcct()));
              	autopay.setTransNo(corpId+"|"+autopay.getApsCode()+"|"+autopay.getContractNo());
              	
              	if(autopay.getPaymentLimit()==Double.parseDouble(Constants.AUTOPAYMENT_NO_LIMIT)){
              		autopay.setPayOption(Constants.AUTOPAYMENT_PAYMENT_FULL);
              	}else if ((autopay.getPaymentLimit()==0)){
              		autopay.setPayOption(Constants.AUTOPAYMENT_PAYMENT_MIN);
              	}else{
              		autopay.setPayOption(Constants.AUTOPAYMENT_PAYMENT_INPUT);
              	}
              	if( autopayAuthorizationHis!=null && !autopayAuthorizationHis.getMode().equals(Constants.AUTOPAYMENT_MODE_ADD) 
              		&& autopayAuthorizationHis.getAuthStatus().equals(Constants.AUTH_STATUS_REJECTED)){
              		//Log.info("Edit/Delete rejected....");
              		autopay.setStatus(Constants.STATUS_NORMAL);
              	} else if(autopayAuthorizationHis!=null && autopayAuthorizationHis.getMode().equals(Constants.AUTOPAYMENT_MODE_ADD)&& autopayAuthorizationHis.getAuthStatus().equals(Constants.AUTH_STATUS_REJECTED)) {
              		//Log.info("remove add rejected....autopayAuthorization seq_no="+autopayAuthorization.getTransNo());
              		autoList.remove(i);
              		i--;//移除数据后，size会自动减1，导致数据不准，故需要i--;
              	}else if(autopayAuthorizationHis!=null){
              		autopay.setStatus(autopayAuthorizationHis.getStatus());
              		
              	}else{
              		autopay.setStatus(Constants.STATUS_NORMAL);
              	}

              }
        	  
        }
        //Log.info("autoList="+autoList);
        return autoList;
	}

	public boolean getMerchant(String apsCode) throws NTBException{
		
		return autopayAuthorizationDao.getMerchant(apsCode);
	}
	
	public AutopayAuthorizationHis loadHisbyKey(String corpId, String apsCode, String contractNo) {
		
		return  autopayAuthorizationDao.getHisbyKey(corpId,apsCode,contractNo);
	}
	
	public void writeLog(AutopayAuthorization autopay, Map fromHost) throws NTBException {
			
		Map uploadMap = new HashMap();
		uploadMap.put("TRAN_DATE", CibTransClient.getCurrentDate());
		uploadMap.put("TRAN_TIME ", CibTransClient.getCurrentTime());
		uploadMap.put("TRAN_CODE", "ZB08");
		uploadMap.put("TRAN_REF", fromHost.get("TELLER_ID").toString()+ fromHost.get("UNIQUE_SEQUENCE_NO").toString());
		uploadMap.put("APS_CODE", autopay.getApsCode());
		uploadMap.put("MAINTENANCE_CODE", autopay.getMode());
		uploadMap.put("CONTRACT_NO", autopay.getContractNo());
		uploadMap.put("PAY_ACCT", autopay.getPayAcct());
		uploadMap.put("PAYMENT_LIMIT", autopay.getPaymentLimit());
		uploadMap.put("CARD_PAYMENT_OPTION",autopay.getPayOption());	
		uploadMap.put("CORP_ID", autopay.getCorpId());
		uploadMap.put("USER_ID", autopay.getUserId());
		uploadMap.put("STATUS", fromHost.get("RESPONSE_CODE"));	
		uploadMap.put("REJECT_CODE", fromHost.get("REJECT_CODE"));
		uploadMap.put("TRAN_USER_REF",autopay.getTransNo());	
		UploadReporter.write("RP_AUTOPAY", uploadMap);
		
	}
}
