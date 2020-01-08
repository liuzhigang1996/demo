package app.cib.dao.txn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessResourceFailureException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;

import app.cib.bo.txn.BillPayment;
import app.cib.bo.txn.TransferBank;
import app.cib.bo.txn.TransferMacau;
import app.cib.bo.txn.TransferOversea;

import com.neturbo.set.database.GenericHibernateDao;
import app.cib.util.Constants;

public class TransferTemplateDao extends GenericHibernateDao {
    TransferOversea transferOversea = null;
    TransferMacau transferMacau = null;
    TransferBank transferBank = null;
    public List listOversea(String corpID)  {

        String hql = "from TransferOversea as tb where tb.corpId = ? and (tb.operation = ? or tb.operation = ?) and  tb.recordType = ?";
        List valueList = new ArrayList();

        valueList.add(corpID);
        valueList.add(Constants.OPERATION_NEW);
        valueList.add(Constants.OPERATION_UPDATE);
        valueList.add(transferOversea.TRANSFER_TYPE_TEMPLATE);
        return this.list(hql, valueList.toArray());

    }

    public List listMacau(String corpID){

        String hql = "from TransferMacau as tb where tb.corpId = ? and (tb.operation = ? or tb.operation = ?) and  tb.recordType = ?";
        List valueList = new ArrayList();
        valueList.add(corpID);
        valueList.add(Constants.OPERATION_NEW);
        valueList.add(Constants.OPERATION_UPDATE);
        valueList.add(transferMacau.TRANSFER_TYPE_TEMPLATE);
        return this.list(hql, valueList.toArray());

    }

    public List listBANK(String corpID) {

        String hql = "from TransferBank as tb where tb.corpId = ? and (tb.operation = ? or tb.operation = ?) and  tb.recordType = ?";
        List valueList = new ArrayList();
        valueList.add(corpID);
        valueList.add(Constants.OPERATION_NEW);
        valueList.add(Constants.OPERATION_UPDATE);
        valueList.add(transferBank.TRANSFER_TYPE_TEMPLATE);
        return this.list(hql, valueList.toArray());

    }
    
    /* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
    public List listBANK(String corpID,String ownerAccFlag) {
    	
    	String hql = "from TransferBank as tb where tb.corpId = ? and (tb.operation = ? or tb.operation = ?) and  tb.recordType = ? and tb.ownerAccFlag = ?";
    	List valueList = new ArrayList();
    	valueList.add(corpID);
    	valueList.add(Constants.OPERATION_NEW);
    	valueList.add(Constants.OPERATION_UPDATE);
    	valueList.add(transferBank.TRANSFER_TYPE_TEMPLATE);
    	valueList.add(ownerAccFlag);
    	return this.list(hql, valueList.toArray());
    	
    }
    /* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */
}
