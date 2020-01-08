package app.cib.service.srv;

import com.neturbo.set.exception.NTBException;

import app.cib.bo.sys.TxnSubtype;

/**
 * @author panwen
 * 
 */
public interface TxnSubtypeService {
	TxnSubtype load(String txnSubtype) throws NTBException;
}
