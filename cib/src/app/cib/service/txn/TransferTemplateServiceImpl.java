package app.cib.service.txn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;

import org.springframework.dao.DataAccessResourceFailureException;

import com.neturbo.set.database.IDGenerator;
import com.neturbo.set.exception.NTBException;

import app.cib.bo.txn.BillPayment;
import app.cib.bo.txn.TransferBank;
import app.cib.bo.txn.TransferMacau;
import app.cib.bo.txn.TransferOversea;
import app.cib.dao.txn.TransferTemplateDao;
import app.cib.util.Constants;

/**
 * @author mxl
 * 2006-07-16
 */
/* Execute a Template In BANK */
public class TransferTemplateServiceImpl implements TransferTemplateService {

    private TransferTemplateDao transferTemplateDao;

    /* Execute a Template In BANK */
    public TransferBank execTemplateBANK(TransferBank pojoTemplate) throws
            NTBException {

        if (pojoTemplate != null) {
            if ((pojoTemplate.getRecordType().equals(TransferBank.
                    TRANSFER_TYPE_TEMPLATE))) {
                pojoTemplate.setRecordType(TransferOversea.
                                           TRANSFER_TYPE_GENERAL);
                pojoTemplate.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
                pojoTemplate.setOperation(Constants.OPERATION_NEW);
                pojoTemplate.setStatus(Constants.STATUS_NORMAL);
                transferTemplateDao.update(pojoTemplate);
            } else {
                throw new NTBException("err.txn.TheTransferTypeIsNotATemplate");
            }
        } else {
            throw new NTBException("err.txn.TransferPojoIsNull");
        }
        return pojoTemplate;
    }

    /* Add aTemplate In BANK */
    public TransferBank addTemplateBANK(TransferBank pojoTemplate) throws
            NTBException {

        if (pojoTemplate != null) {

            if ((pojoTemplate.getRecordType().equals(TransferBank.
                    TRANSFER_TYPE_TEMPLATE))) {
                //pojoTemplate.setStatus(TransferBank.TRANSFER_STATUS_NEW);

                transferTemplateDao.add(pojoTemplate);
            } else {
                throw new NTBException("err.txn.TheTransferTypeIsNotATemplate");
            }
        } else {
            throw new NTBException("err.txn.TransferPojoIsNull");
        }
        return pojoTemplate;
    }

    public TransferTemplateDao getTransferTemplateDao() {
        return transferTemplateDao;
    }

    public void setTransferTemplateDao(TransferTemplateDao transferTemplateDao) {
        this.transferTemplateDao = transferTemplateDao;
    }

    /* edit a template In BANK */
    public TransferBank editTemplateBANK(TransferBank pojoTemplate) throws
            NTBException {
        if (pojoTemplate != null) {
                  if ((pojoTemplate.getRecordType().equals(TransferBank.
                        TRANSFER_TYPE_TEMPLATE))) {

                    pojoTemplate.setOperation(Constants.OPERATION_UPDATE);
                    transferTemplateDao.update(pojoTemplate);
                } else {
                    throw new NTBException(
                            "err.txn.TheTransferTemplateTypeIsNotATemplate");
                }
 
        }
        return pojoTemplate;
    }

    /* delete a template in BANK edit by mxl 0123 */
    public TransferBank deleteTemplateBANK(String transId) throws
            NTBException {
        TransferBank transferBank = null;
        if ((transId != null) && (!transId.equals(" "))) {
            transferBank = this.viewTemplate(transId);
                if ((transferBank.getRecordType().equals(TransferBank.
                        TRANSFER_TYPE_TEMPLATE))) {
                    transferBank.setStatus(Constants.STATUS_REMOVED);
                    transferBank.setOperation(Constants.OPERATION_REMOVE);
                    transferTemplateDao.update(transferBank);
                } else {
                    throw new NTBException(
                            "err.txn.ThTransferTemplateTypeIsNotATemplate");
                }
            } else {
                throw new NTBException("err.txn.TheUserHaveNoSuchFunction");
            }
        return transferBank;
    }


    /* View a template In Bank */
    public TransferBank viewTemplate(String transID) throws NTBException {

        TransferBank transferBank = null;
        if ((transID != null) && (!transID.equals(""))) {

            transferBank = (TransferBank) transferTemplateDao.load(TransferBank.class,
                    transID);
            if ((transferBank.getRecordType().equals(TransferBank.
                    TRANSFER_TYPE_TEMPLATE))) {
            } else {
                throw new NTBException("err.txn.TheTransferTypeIsNotATemplate");
            }
        } else {
            throw new NTBException("err.txn.TransIDIsNullOrEmpty");
        }

        return transferBank;
    }

    /* Add a template In Macau */
    public TransferMacau addTemplateMacau(TransferMacau pojoTemplate) throws
            NTBException {

        if (pojoTemplate != null) {
            if ((pojoTemplate.getRecordType().equals(TransferMacau.
                    TRANSFER_TYPE_TEMPLATE))) {
                //pojoTemplate.setStatus(Constants.STATUS_NORMAL );
                pojoTemplate.setOperation(Constants.OPERATION_NEW);

                transferTemplateDao.add(pojoTemplate);
            } else {
                throw new NTBException("err.txn.TheTransferTypeIsNotATemplate");
            }
        } else {
            throw new NTBException("err.txn.TransferPojoIsNull");
        }
        return pojoTemplate;
    }

    /* Edit a template in Macau by mxl 0123*/
    public TransferMacau editTemplateMacau(TransferMacau pojoTemplate) throws NTBException {
        if (pojoTemplate != null) {
            if ((pojoTemplate.getRecordType().equals(TransferMacau.
                        TRANSFER_TYPE_TEMPLATE))) {
                    pojoTemplate.setOperation(Constants.OPERATION_UPDATE);
                    transferTemplateDao.update(pojoTemplate);
                } else {
                    throw new NTBException(
                            "err.txn.TheTransferTemplateTypeIsNotATemplate");
                }
             }
        return pojoTemplate;
    }

    /* Delete a Template In Macau*/
    public TransferMacau deleteTemplateMacau(String transId) throws
            NTBException {
        TransferMacau transferMacau = null;
        if ((transId != null) && (!transId.equals(" "))) {
            transferMacau = this.viewTemplateMacau(transId);
                if ((transferMacau.getRecordType().equals(TransferMacau.
                        TRANSFER_TYPE_TEMPLATE))) {

                    transferMacau.setOperation(Constants.OPERATION_REMOVE);
                    transferTemplateDao.update(transferMacau);
                } else {
                    throw new NTBException(
                            "err.txn.ThTransferTemplateTypeIsNotATemplate");
                }
            
        } else {
            throw new NTBException("err.txn.TransIDIsNullOrEmpty");
        }
        return transferMacau;
    }

   
    /* Execute a Template In Macau */
    public TransferMacau execTemplateMacau(TransferMacau pojoTemplate) throws
            NTBException {

        if (pojoTemplate != null) {
            if ((pojoTemplate.getRecordType().equals(TransferMacau.
                    TRANSFER_TYPE_TEMPLATE))) {
                pojoTemplate.setRecordType(TransferOversea.
                                           TRANSFER_TYPE_GENERAL);
                pojoTemplate.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
                pojoTemplate.setOperation(Constants.OPERATION_NEW);
                pojoTemplate.setStatus(Constants.STATUS_NORMAL);
                transferTemplateDao.update(pojoTemplate);
            } else {
                throw new NTBException("err.txn.TheTransferTypeIsNotATemplate");
            }

        } else {
            throw new NTBException("err.txn.TransferPojoIsNull");
        }
        return pojoTemplate;
    }

    /* view a Template in Macau */
    public TransferMacau viewTemplateMacau(String transID) throws NTBException {
        TransferMacau transferMacau = null;
        if ((transID != null) && (!transID.equals(""))) {

            transferMacau = (TransferMacau) transferTemplateDao.load(
                    TransferMacau.class, transID);

            if ((transferMacau.getRecordType().equals(TransferMacau.
                    TRANSFER_TYPE_TEMPLATE))) {

            } else {
                throw new NTBException("err.txn.ThePayTypeIsNotATemplate");
            }
        } else {
            throw new NTBException("err.txn.TransIDIsNullOrEmpty");
        }

        return transferMacau;
    }

    /* Add a Template in Oversea */
    public TransferOversea addTemplateOversea(TransferOversea pojoTemplate) throws
            NTBException {
        if (pojoTemplate != null) {

            if ((pojoTemplate.getRecordType().equals(TransferOversea.
                    TRANSFER_TYPE_TEMPLATE))) {
                //pojoTemplate.setStatus(Constants.STATUS_NORMAL );
                pojoTemplate.setOperation(Constants.OPERATION_NEW);
                transferTemplateDao.add(pojoTemplate);
            } else {
                throw new NTBException("err.txn.TheTransferTypeIsNotATemplate");
            }
        } else {
            throw new NTBException("err.txn.TransferPojoIsNull");
        }
        return pojoTemplate;

    }

    /* edit a template In oversea bank */
    public TransferOversea editTemplateOversea(TransferOversea pojoTemplate) throws
            NTBException {
        if (pojoTemplate != null) {
           
                if ((pojoTemplate.getRecordType().equals(TransferOversea.
                        TRANSFER_TYPE_TEMPLATE))) {

                    pojoTemplate.setOperation(Constants.OPERATION_UPDATE);
                    transferTemplateDao.update(pojoTemplate);
                } else {
                    throw new NTBException(
                            "err.txn.TheTransferTemplateTypeIsNotATemplate");
                }
            
        }
        return pojoTemplate;
    }

    /* Delete a template In Oversea bank */
    public TransferOversea deleteTemplateOversea(String transId) throws
            NTBException {
        TransferOversea transferOversea = null;
        if ((transId != null) && (!transId.equals(" "))) {
            transferOversea = this.viewTemplateOversea(transId);
                if ((transferOversea.getRecordType().equals(TransferOversea.
                        TRANSFER_TYPE_TEMPLATE))) {
                    //transferOversea.setStatus(Constants.STATUS_REMOVED );
                    //transferOversea.setAuthStatus(TransferOversea.TRANSFER_AUTH_STATUS_COMPLETED);
                    transferOversea.setOperation(Constants.OPERATION_REMOVE);
                    transferTemplateDao.update(transferOversea);
                } else {
                    throw new NTBException(
                            "err.txn.ThTransferTemplateTypeIsNotATemplate");
                }
           
        } else {
            throw new NTBException("err.txn.TransIDIsNullOrEmpty");
        }
        return transferOversea;
    }

    /* View a Template In Oversea */
    public TransferOversea viewTemplateOversea(String transID) throws
            NTBException {
        TransferOversea transferOversea = null;
        if ((transID != null) && (!transID.equals(""))) {
            transferOversea = (TransferOversea) transferTemplateDao.load(
                    TransferOversea.class, transID);

            if ((transferOversea.getRecordType().equals(TransferOversea.
                    TRANSFER_TYPE_TEMPLATE))) {
            } else {
                throw new NTBException("err.txn.ThePayTypeIsNotATemplate");
            }
        } else {
            throw new NTBException("err.txn.TransIDIsNullOrEmpty");
        }
        return transferOversea;
    }

    /* Execute a Template In Oversea */
    public TransferOversea execTemplateOversea(TransferOversea pojoTemplate) throws
            NTBException {

        if (pojoTemplate != null) {
            if ((pojoTemplate.getRecordType().equals(TransferOversea.
                    TRANSFER_TYPE_TEMPLATE))) {
                pojoTemplate.setRecordType(TransferOversea.
                                           TRANSFER_TYPE_GENERAL);
                //pojoTemplate.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
                //pojoTemplate.setOperation(Constants.OPERATION_NEW );
                //pojoTemplate.setStatus(Constants.STATUS_NORMAL );
                transferTemplateDao.update(pojoTemplate);
            } else {
                throw new NTBException("err.txn.TheTransferTypeIsNotATemplate");
            }

        } else {
            throw new NTBException("err.txn.TransferPojoIsNull");
        }
        return pojoTemplate;
    }

    public List listTemplateBANK(String corpID) throws
            NTBException {
        List templateList = null;
        if ((corpID != null) && (!corpID.equals(""))) {
            templateList = transferTemplateDao.listBANK(corpID);
        } else {
            throw new NTBException("err.txn.CorpIDIsNullOrEmpty");
        }
        return templateList;
    }
    
    /* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
    public List listTemplateBANK(String corpID,String ownerAccFlag) throws
    NTBException {
    	List templateList = null;
    	if ((corpID != null) && (!corpID.equals(""))) {
    		templateList = transferTemplateDao.listBANK(corpID,ownerAccFlag);
    	} else {
    		throw new NTBException("err.txn.CorpIDIsNullOrEmpty");
    	}
    	return templateList;
    }
    /* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */

    public List listTemplateMacau(String corpID) throws
            NTBException {
        List templateList = null;
        if ((corpID != null) && (!corpID.equals(""))) {
            templateList = transferTemplateDao.listMacau(corpID);
        } else {
            throw new NTBException("err.txn.CorpIDIsNullOrEmpty");
        }

        return templateList;
    }

    public List listTemplateOversea(String corpID) throws
            NTBException {
        List templateList = null;
        if ((corpID != null) && (!corpID.equals(""))) {
            templateList = transferTemplateDao.listOversea(corpID);
        } else {
            throw new NTBException("err.txn.CorpIDIsNullOrEmpty");
        }
        return templateList;
    }

	

}
