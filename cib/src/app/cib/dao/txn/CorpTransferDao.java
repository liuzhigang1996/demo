package app.cib.dao.txn;

import java.util.*;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import org.apache.axis.encoding.ser.ArrayDeserializer.ArrayListExtension;
import org.springframework.dao.DataAccessResourceFailureException;

import com.neturbo.set.database.GenericHibernateDao;

import app.cib.bo.txn.TransferBank;

public class CorpTransferDao extends GenericHibernateDao {

    public List listTransfer(Date dateFrom, Date dateTo, String fromCorpID,
                             String userID, String fromAccount,String corpId){
        String hql = "from TransferBank as tb where (tb.recordType= ?  or tb.recordType= ? or tb.recordType= ?)";

        if ((dateFrom != null) && (!dateFrom.equals(""))) {
            hql = hql + "and tb.requestTime >= ? ";
        }

        if ((dateTo != null) && (!dateTo.equals(""))) {
            hql = hql + "and tb.requestTime <= ? ";
        }
        if ((fromCorpID != null) && (!fromCorpID.equals(""))) {
            hql = hql + "and  tb.fromCorporation = ? ";
        }

        if ((userID != null) && (!userID.equals(""))) {
            hql = hql + "and  tb.userId = ? ";
        }
        if ((fromAccount != null) && (!fromAccount.equals(""))) {
            hql = hql + "and  tb.fromAccount = ? ";
        }
        if ((corpId != null) && (!corpId.equals(""))) {
            hql = hql + "and  tb.corpId = ? ";
        }
        
        ArrayList valueList = new ArrayList();
        valueList.add(TransferBank.TRANSFER_TYPE_COPRREPATRIATE);
        valueList.add(TransferBank.TRANSFER_TYPE_CORPDELIVERY);
        valueList.add(TransferBank.TRNASFER_TYPE_CORPSUBIDIARY);

        if ((dateFrom != null) && (!dateFrom.equals(""))) {
            valueList.add(dateFrom);
        }
        if ((dateTo != null) && (!dateTo.equals(""))) {
            valueList.add(dateTo);
        }
        if ((fromCorpID != null) && (!fromCorpID.equals(""))) {
            valueList.add(fromCorpID);
        }
        if ((userID != null) && (!userID.equals(""))) {
            valueList.add(userID);
        }
        if ((fromAccount != null) && (!fromAccount.equals(""))) {
            valueList.add(fromAccount);
        }
        if ((corpId != null) && (!corpId.equals(""))) {
            valueList.add(corpId);
        }
        hql += "order by tb.requestTime DESC";
        return getHibernateTemplate().find(hql, valueList.toArray());

    }

}
