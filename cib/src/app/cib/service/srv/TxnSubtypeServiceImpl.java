package app.cib.service.srv;

import app.cib.bo.sys.TxnSubtype;
import app.cib.dao.sys.TxnSubtypeDao;

import com.neturbo.set.exception.NTBException;

public class TxnSubtypeServiceImpl implements TxnSubtypeService {

	private TxnSubtypeDao txnSubtypeDao = null;

	public TxnSubtype load(String txnSubtype) throws NTBException {
		return (TxnSubtype) txnSubtypeDao.load(TxnSubtype.class, txnSubtype);
	}

	public TxnSubtypeDao getTxnSubtypeDao() {
		return txnSubtypeDao;
	}

	public void setTxnSubtypeDao(TxnSubtypeDao txnSubtypeDao) {
		this.txnSubtypeDao = txnSubtypeDao;
	}

}
