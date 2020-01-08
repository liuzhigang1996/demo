package app.cib.service.txn;

import java.util.List;

import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.Autopay;
import app.cib.bo.txn.AutopayAuthorization;
import app.cib.bo.txn.AutopayAuthorizationHis;

import com.neturbo.set.exception.NTBException;

public interface AutopayAuthorizationService {

	public List listAutopay(String corpId) throws NTBException ;
	
	public void addAutopay(AutopayAuthorization autopayAuthorization) throws NTBException ;
	
	public AutopayAuthorization loadAutopayAuthorization(String transNo,String apsCode,String contractNo, String corpId) throws NTBException ;
	
	public Autopay loadAutopay(String apsCode,String contractNo, String corpId) throws NTBException ;
	
	public void approveAutopay(Autopay autopay, AutopayAuthorization autopayAuthorization,AutopayAuthorizationHis autopayAuthorizationHis, CorpUser corpUser, boolean isExsitAutopayAuthorization) throws NTBException ;
	
	public void updateAutopay(AutopayAuthorization autopayAuthorization) throws NTBException ;
	
	public void rejectAutopay(AutopayAuthorizationHis autopayAuthorizationHis) throws NTBException ;

	public void addAutopayHis(AutopayAuthorizationHis autopayAuthorizationHis);

	public AutopayAuthorizationHis loadAutopayAuthorizationHis(String id);

	public AutopayAuthorization loadauthorization(String transNo) throws NTBException;

	public void updateAutopayAuthorizationHis(
			AutopayAuthorizationHis autopayAuthorizationHis);

	public void updateAutopayAuthorization(
			AutopayAuthorization autopayAuthorization);

	public List listAutopayAuthorization(String corpId)throws NTBException;
	
	public boolean getMerchant(String apsCode) throws NTBException;
	
	public AutopayAuthorizationHis loadHisbyKey(String corpId, String apsCode, String contractNo);
}
