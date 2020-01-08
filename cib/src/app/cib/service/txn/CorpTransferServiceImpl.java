package app.cib.service.txn;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.hibernate.HibernateException;

import org.springframework.dao.DataAccessResourceFailureException;

import app.cib.bo.txn.TransferBank;
import app.cib.dao.txn.CorpTransferDao;
import app.cib.util.Constants;

import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;

/**
 * @author mxl
 * 2006-08-02
 */
public class CorpTransferServiceImpl implements CorpTransferService {
	private static String SELECT_CORP_ID = "Select CORP_ID from CORP_ACCOUNT Where ACCOUNT_NO=?";
    private CorpTransferDao corpTransferDao;
    private GenericJdbcDao genericJdbcDao;
    public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}
    public TransferBank transferDelivery(TransferBank pojoTransfer) throws
            NTBException {
        if (pojoTransfer != null) {
            /*
             pojoTransfer.setRecordType(TransferBank.TRANSFER_TYPE_CORPDELIVERY);
                pojoTransfer.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
                pojoTransfer.setOperation(Constants.OPERATION_NEW );
             */
            corpTransferDao.add(pojoTransfer);
        } else {
            throw new NTBException("err.txn.pojoTransferIsNull");
        }
        return pojoTransfer;
    }

    public TransferBank transferSubsidiary(TransferBank pojoTransfer) throws
            NTBException {
        if (pojoTransfer != null) {
            /*
             pojoTransfer.setRecordType(TransferBank.TRNASFER_TYPE_CORPSUBIDIARY);
                pojoTransfer.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
                pojoTransfer.setOperation(Constants.OPERATION_NEW );
             */
            corpTransferDao.add(pojoTransfer);
        } else {
            throw new NTBException("err.txn.pojoTransferIsNull");
        }
        return pojoTransfer;
    }

    public TransferBank transferRepatriate(TransferBank pojoTransfer) throws
            NTBException {
        if (pojoTransfer != null) {
            /*
             pojoTransfer.setRecordType(TransferBank.TRANSFER_TYPE_COPRREPATRIATE);
                pojoTransfer.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
                pojoTransfer.setOperation(Constants.OPERATION_NEW );
             */
            corpTransferDao.add(pojoTransfer);
        } else {
            throw new NTBException("err.txn.pojoTransferIsNull");
        }
        return pojoTransfer;
    }

    public CorpTransferDao getCorpTransferDao() {
        return corpTransferDao;
    }

    public void setCorpTransferDao(CorpTransferDao corpTransferDao) {
        this.corpTransferDao = corpTransferDao;
    }

    public List transferHistory(Date dateFrom, Date dateTo, String fromCcorpID,
                                String userID, String fromAccount, String corpId) throws
            NTBException {
        List historyList = null;
        historyList = corpTransferDao.listTransfer(dateFrom, dateTo, fromCcorpID,
                userID, fromAccount, corpId);

        return historyList;
    }

    public TransferBank viewInBANK(String transID) throws NTBException {
        TransferBank transferBank = null;
        if ((transID != null) && (!transID.equals(""))) {

            transferBank = (TransferBank) corpTransferDao.load(TransferBank.class,
                    transID);

        } else {
            throw new NTBException("err.txn.TransIDIsNullOrEmpty");
        }

        return transferBank;
    }

    public void approveBank(TransferBank transferBank) throws NTBException {
        if (transferBank != null) {
            transferBank.setStatus(Constants.STATUS_NORMAL);
            transferBank.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            transferBank.setExecuteTime(new Date());
            corpTransferDao.update(transferBank);
        }

    }

    public void rejectBank(TransferBank transferBank) throws NTBException {
        if (transferBank != null) {
            transferBank.setStatus(Constants.STATUS_REMOVED);
            transferBank.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
            transferBank.setExecuteTime(new Date());
            corpTransferDao.update(transferBank);
        }

    }

	public String getCorpIdByAccount(String account) throws NTBException {
		HashMap accountDescMap = new HashMap();
        String corpId = null;
        try {
			List idList = null;
			idList = genericJdbcDao.query(SELECT_CORP_ID,
					new Object[] { account });
			accountDescMap = (HashMap) idList.get(0);
			corpId = (String) accountDescMap.get("CORP_ID");
			return corpId;

		} catch (Exception e) {
			Log.info("***Can not found account:" + account);
			if (account.contains("err.sys.IllegalAccountAccess.errorCurrency")) {
                throw new NTBException("err.sys.IllegalAccountAccess.errorCurrency");
			} else {
				throw new NTBException("err.sys.NoSuchAccountNoInDB");
			}
		}
	}

}
