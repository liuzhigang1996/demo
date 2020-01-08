package app.cib.dao.txn;

import java.util.ArrayList;
import java.util.List;

import app.cib.bo.txn.Autopay;
import app.cib.bo.txn.AutopayAuthorization;
import app.cib.bo.txn.AutopayAuthorizationHis;
import app.cib.util.Constants;

import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;

public class AutopayAuthorizationDao extends GenericHibernateDao {

	public List listAutopay(String corpId) throws NTBException {
		String hql = "from Autopay a where 1=1 and a.corpId=?" ;
		return this.list(hql, new Object[]{corpId}) ;
	}
	
	public AutopayAuthorization loadAutopayAuthorization(String transNo,
			String apsCode, String contractNo,String corpId) throws NTBException {
		List resultList = new ArrayList() ;
		List paraList = new ArrayList() ;
		
		String hql = "from AutopayAuthorization a where 1=1 " ;
		
		if(null!=transNo && !"".equals(transNo)){
			hql += " and a.transNo = ?" ;
			paraList.add(transNo) ;
		}
		if(null!=apsCode && !"".equals(apsCode)){
			hql += " and a.apsCode = ?" ;
			paraList.add(apsCode) ;
		}
		if(null!=contractNo && !"".equals(contractNo)){
			hql += " and a.contractNo = ?" ;
			paraList.add(contractNo) ;
		}
		if(null!=corpId && !"".equals(corpId)){
			hql += " and a.corpId = ?" ;
			paraList.add(corpId) ;
		}
		resultList = this.list(hql, paraList.toArray()) ;
		if(null!=resultList && resultList.size()>0){
			return (AutopayAuthorization)resultList.get(0) ;
		}
		return null;
	}
	
	public Autopay loadAutopay(String apsCode, String contractNo,String corpId) throws NTBException {
		List resultList = new ArrayList() ;
		List paraList = new ArrayList() ;
		
		String hql = "from Autopay a where 1=1 " ;
		
		
		if(null!=apsCode && !"".equals(apsCode)){
			hql += " and a.apsCode = ?" ;
			paraList.add(apsCode) ;
		}
		if(null!=contractNo && !"".equals(contractNo)){
			hql += " and a.contractNo = ?" ;
			paraList.add(contractNo) ;
		}
		if(null!=corpId && !"".equals(corpId)){
			hql += " and a.corpId = ?" ;
			paraList.add(corpId) ;
		}
		resultList = this.list(hql, paraList.toArray()) ;
		if(null!=resultList && resultList.size()>0){
			return (Autopay)resultList.get(0) ;
		}
		return null;
	}

	public AutopayAuthorizationHis getHisByTransNo(String transNo,String corpId) {
		String hql = "from AutopayAuthorizationHis as his where his.transNo = ? and his.corpId=? order by his.seqNo desc";
		List list = this.list(hql, new Object[]{transNo,corpId});
		if (list.size()>0) {
			return (AutopayAuthorizationHis) list.get(0);
		}
		return null;
	}

	public AutopayAuthorizationHis getHisbyKey(String corpId,String apsCode,String contractNo) {
		String hql = "from AutopayAuthorizationHis as his where his.corpId = ? and his.apsCode=? and his.contractNo=?  order by his.seqNo desc";
		List list = this.list(hql, new Object[]{corpId,apsCode,contractNo});
		if (list.size()>0) {
			return (AutopayAuthorizationHis) list.get(0);
		}
		return null;
	}
	public List listAutopayAuthorization(String transNo) {
		String hql = "from AutopayAuthorizationHis as auto where auto.transNo = ? and auto.status != ? ";
		List valueList = new ArrayList();
		valueList.add(transNo);
		valueList.add(Constants.STATUS_REMOVED);
		return this.list(hql, valueList.toArray());

	}
	
	public boolean getMerchant(String apsCode)throws NTBException {
	    List list=new ArrayList();
		List param = new ArrayList();
		boolean flag = false;
//		String sqlStr = " select m.apsCode, m.payByAcct, m.payByCreditCard, n.merchantName "
//				      + " from AutopayMerchantInfo as m, AutopayMerchantName as n "
//				      + " where m.apsCode= n.AutopayMerchantNameId.apsCode "
//				      + " and m.apsCode = ? and n.AutopayMerchantNameId.lang = ? "
//				      + " order by n.MerchantName";
		String sqlStr=" from AutopayMerchantInfo as auto where auto.apsCode = ?";
		param.add(apsCode);
		//param.add("E");
		Object[] obj = new Object[] {};
		try {
			list =this.list(sqlStr, param.toArray());
			if(null!=list &&list.size()>0){
				flag = true;
			}
		} catch (Exception e) {
			Log.error("getAutopayMerchant error :" + e.getMessage(),e);
			throw new NTBException("getAutopayMerchant error");
		}
		return flag;
	}
	
	public AutopayAuthorization loadauthorization(String transNo) throws NTBException{
		List list=new ArrayList();
		List param = new ArrayList();
		boolean flag = false;
		AutopayAuthorization authorization = null;
		String sqlStr=" from AutopayAuthorization as auto where auto.transNo = ?";
		param.add(transNo);
		//param.add("E");
		Object[] obj = new Object[] {};
		try {
			list =this.list(sqlStr, param.toArray());
			if(null!=list &&list.size()>0){
				authorization = (AutopayAuthorization) list.get(0);
			}
		} catch (Exception e) {
			Log.error("loadauthorization error :" + e.getMessage(),e);
			throw new NTBException("getAutopayMerchant error");
		}
		return authorization;
	}
}
