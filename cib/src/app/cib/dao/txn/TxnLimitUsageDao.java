package app.cib.dao.txn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import net.sf.hibernate.*;
import app.cib.bo.txn.TxnLimitUsage;
import com.jcraft.jsch.Session;
import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class TxnLimitUsageDao extends GenericHibernateDao {
	private static final String FIND_BY_CORP_AND_TXN_1 = "from TxnLimitUsage as txnLimitUsage where "
			+ "txnLimitUsage.corpId = ? and txnLimitUsage.account =? "
			+ "and txnLimitUsage.txnType =? order by txnLimitUsage.usageId desc";
	private static final String FIND_BY_CORP_AND_TXN_2 = "from TxnLimitUsage as txnLimitUsage where "
			+ "txnLimitUsage.corpId = ? and txnLimitUsage.account =? "
			+ "and txnLimitUsage.txnType is null order by txnLimitUsage.usageId desc";
	private static final String SQL_TOTAL_AMOUNT = "select {p.*} "
		   	+ " from(select t.* , row_number() over(partition by t.ACCOUNT order by t.USAGE_ID desc) as num from TXN_LIMIT_USAGE t "
		    + "where t.CORP_ID = ? and t.USAGE_DATE = ? ) {p}   where num= 1 ";
	public TxnLimitUsage findByCorpAndTxn(String corpId, String account,
			String txnType) throws NTBException {
		TxnLimitUsage txnLimitUsage = null;

		List aList = null;
		if (txnType != null) {
			try {
				aList = getHibernateTemplate().find(FIND_BY_CORP_AND_TXN_1,
						new Object[] { corpId, account, txnType });
			} catch (Exception e) {
				throw new NTBException("err.sys.DBError");
			}
		} else {
			try {
				aList = getHibernateTemplate().find(FIND_BY_CORP_AND_TXN_2,
						new Object[] { corpId, account });
			} catch (Exception e) {
				throw new NTBException("err.sys.DBError");
			}
		}
		if (null != aList && aList.size() > 0) {
			txnLimitUsage = (TxnLimitUsage) aList.get(0);
		}
		return txnLimitUsage;
	}
	public List getTxnLimitUsageList(String corpId,String usageDate)throws NTBException{
		double amount = 0.0;	
		List aList;
		try{
			
			Query query =  getSession().createSQLQuery(SQL_TOTAL_AMOUNT,"p",TxnLimitUsage.class);;
			query.setString(0,corpId);
			query.setString(1, usageDate);
			//System.out.println(query.getQueryString());
			aList = query.list();
		}catch(Exception e){
			throw new NTBException("err.sys.DBError");
		}
		return aList;
	}
   
}
