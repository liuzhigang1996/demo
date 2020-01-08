package app.cib.service.bnk;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.cib.bo.bnk.Corporation;
import app.cib.bo.bnk.CorporationHis;
import app.cib.dao.bnk.CorporationDao;
import app.cib.dao.bnk.CorporationHisDao;
import app.cib.util.Constants;

import com.neturbo.set.core.*;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import app.cib.core.CibIdGenerator;
import app.cib.core.CibTransClient;
import app.cib.bo.sys.CorpUser;
import com.neturbo.set.exception.NTBHostException;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CorporationServiceImpl implements CorporationService {
    private CorporationDao corporationDao;
    private CorporationHisDao corporationHisDao;
    private GenericJdbcDao genericJdbcDao;


	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}


	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}
	
	
    public CorporationHisDao getCorporationHisDao() {
        return corporationHisDao;
    }


    public void setCorporationHisDao(CorporationHisDao corporationHisDao) {
        this.corporationHisDao = corporationHisDao;
    }


    public CorporationServiceImpl() {
    }


    public void add(Corporation corpObj) throws NTBException {
        Corporation oldObj = (Corporation)corporationDao.load(corpObj.getClass(), corpObj.getCorpId());
        if(oldObj != null){
            corporationDao.delete(oldObj);
        }
        corporationDao.add(corpObj);

    }

    public void remove(Corporation corpObj) throws NTBException {
        corporationDao.delete(corpObj);
    }

    public void update(Corporation corpObj) throws NTBException {
        //corporationDao.update(corpObj);
    	Map conditionMap = new HashMap();
    	conditionMap.put("corp_id", corpObj.getCorpId());
    	genericJdbcDao.updateByObject("corporation", corpObj, conditionMap);
    }


    public CorporationDao getCorporationDao() {
        return corporationDao;
    }

    public void setCorporationDao(CorporationDao corporationDao) {
        this.corporationDao = corporationDao;
    }


    public List list(Corporation corpObj) throws NTBException {
        List list = null;
        try {
            list = corporationDao.list(corpObj);
        } catch (Exception e) {
            Log.error("Error list Corporation", e);
            throw new NTBException("err.sys.DBError");
        }
        return list;
    }


    public Corporation view(String corpId) throws NTBException {
        Corporation corpObj = null;
        if ((corpId != null) && (!corpId.equals(""))) {
            corpObj = (Corporation) corporationDao.load(Corporation.class,
                    corpId);
            if(corpObj == null){
                return null;
            }
            if(corpObj.getStatus().equals(Constants.STATUS_REMOVED)){
                return null;
            }
            corporationHisDao.getHibernateTemplate().evict(corpObj);
        } else {
            return null;
        }
        
        
        return corpObj;
    }

    public String getCorporationStatus(String corpId, NTBUser user,Locale lang) throws
            NTBException {
        Map toHost = new HashMap();
        Map fromHost = new HashMap();
        CibTransClient testClient = new CibTransClient("CIB", "ZC09");
        toHost.put("CORPORATE_ID", corpId.startsWith("C")?corpId.substring(1):corpId);//mod by linrui 20180403
        String refNo = CibIdGenerator.getRefNoForTransaction();
        CorpUser corpUser = new CorpUser();
        corpUser.setUserId(user.getUserId());
        corpUser.setCorpId("");
        corpUser.setLanguage(lang);//add by linrui for mul-language
        testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_ENQUIRY,
                             CibTransClient.ACCTYPE_3RD_PARTY, refNo);
        fromHost = testClient.doTransaction(toHost);
        if (!testClient.isSucceed()) {
        	throw new NTBHostException(testClient.getErrorArray());
        }
        return (String)fromHost.get("STATUS");
    }

    public void addHis(CorporationHis corpHis) throws NTBException {
        corporationHisDao.add(corpHis);
    }


    public void updateHis(CorporationHis corpHis) throws NTBException {
        //corporationHisDao.update(corpHis);
    	Map conditionMap = new HashMap();
    	conditionMap.put("SEQ_NO", corpHis.getSeqNo());
    	genericJdbcDao.updateByObject("corporation_his", corpHis, conditionMap);
    }

    public CorporationHis loadPendingHis(String corpId) throws NTBException {
        Map conditionMap = new HashMap();
        conditionMap.put("corpId", corpId);
        conditionMap.put("status", Constants.STATUS_PENDING_APPROVAL);
        List corpHisList = corporationHisDao.list(CorporationHis.class,
                                                  conditionMap);
        if (corpHisList == null || corpHisList.size() == 0) {
            return null;
        }
        CorporationHis corpHis = (CorporationHis) corpHisList.get(0);
        return corpHis;
    }

    public CorporationHis loadHisBySeqNo(String seqNo) throws NTBException {
        Map conditionMap = new HashMap();
        conditionMap.put("seqNo", seqNo);
        List corpHisList = corporationHisDao.list(CorporationHis.class,
                                                  conditionMap);
        if (corpHisList == null || corpHisList.size() == 0) {
            return null;
        }
        CorporationHis corpHis = (CorporationHis) corpHisList.get(0);
        corporationHisDao.getHibernateTemplate().evict(corpHis);
        return corpHis;
    }

    public List listCorpHisByAfterId(String afterModifyId) throws NTBException {
        //HashMap condictionMap = new HashMap();
        //condictionMap.put("afterModifyId", afterModifyId);
        //condictionMap.put("status", Constants.STATUS_PENDING_APPROVAL);
        List resList = null;
        try {
        	//Update by heyj 20190528
            //resList = this.corporationHisDao.list(CorporationHis.class, condictionMap);
        	String sql = "select * from corporation_his t where t.AFTER_MODIFY_ID = ? and t.status = ?";
        	resList = genericJdbcDao.query(sql, new Object[]{afterModifyId, Constants.STATUS_PENDING_APPROVAL});
        } catch (Exception e) {
        	Log.error("DBError", e);
            throw new NTBException("err.sys.DBError");
        }
        return resList;
    }
    
    /**
     * add by wen_chy 20090916
     */
    public List listMerchantGroup(String merchantGroup) throws NTBException {
    	
    	String sql="select MERCHANT_GROUP,DESCRIPTION from MERCHANT_GROUP where 1=1";
    	
        if(merchantGroup!=null&&!"".equals(merchantGroup)){
        	sql=sql+" and MERCHANT_GROUP='"+merchantGroup+"'";
        }
        List resList = null;
        try {
            resList = this.genericJdbcDao.query(sql, new Object[]{});
        } catch (Exception e) {
        	Log.error("DBError", e);
            throw new NTBException("err.sys.DBError");
        }
        return resList;
    }

}
