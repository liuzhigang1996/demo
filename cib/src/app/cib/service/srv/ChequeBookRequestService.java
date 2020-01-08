package app.cib.service.srv;

import app.cib.bo.srv.ChequeBookRequest;
import app.cib.bo.sys.CorpUser;

import com.neturbo.set.exception.NTBException;

public interface ChequeBookRequestService {

	public void add(ChequeBookRequest bookRequest) throws NTBException ;
	
	public ChequeBookRequest load(String transNo) throws NTBException ;
	
	public void approveChequeBookRequest(ChequeBookRequest bookRequest,CorpUser corpUser) throws NTBException ;
	
	public void rejectChequeBookRequest(ChequeBookRequest bookRequest) throws NTBException ;

	public void removeCheque(String tableName, String transNo);
}
