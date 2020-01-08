/**
 * @author hjs
 * 2006-8-3
 */
package app.cib.service.bnk;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessResourceFailureException;

import app.cib.bo.bnk.CorporationHis;
import app.cib.bo.bnk.SetCurrency;
import app.cib.bo.bnk.SetCurrencyHis;
import app.cib.dao.bnk.SetCurrencyDao;
import app.cib.util.Constants;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 2006-8-3
 */
public class SetCurrencyServiceImpl implements SetCurrencyService {
	
	private SetCurrencyDao setCurrencyDao;
	private GenericJdbcDao genericJdbcDao;
	private static String REJECT_CONTROL = "update AVAILABLE_CURRENCIES_HIS set status = '"+ Constants.STATUS_NORMAL +"'";
	


	public SetCurrencyDao getSetCurrencyDao() {
		return setCurrencyDao;
	}

	public void setSetCurrencyDao(SetCurrencyDao setCurrencyDao) {
		this.setCurrencyDao = setCurrencyDao;
	}
	
	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}



	public List listManageCcy() throws NTBException {
		try {
//			return setCurrencyDao.listManageCcy();
			return setCurrencyDao.listManageCcyNormal();
		} catch (Exception e) {
			throw new NTBException("err.sys.DBError");
		}
	}
	public List listManageCcyPending() throws NTBException {
		try {
			return setCurrencyDao.listManageCcyPending();
		} catch (Exception e) {
			throw new NTBException("err.sys.DBError");
		}
	}
	public List listManageCcyNormal() throws NTBException {
		try {
			return setCurrencyDao.listManageCcyNormal();
		} catch (Exception e) {
			throw new NTBException("err.sys.DBError");
		}
	}

	
	public void convertPojo2Map(Object pojo, Map valueMap) throws NTBException {
        try {
            valueMap.putAll(BeanUtils.describe(pojo));
        } catch (Exception e) {
            Log.warn("Error reading values from POJO", e);
        }
    }
	/*public void add(SetCurrency setCurrency) throws NTBException {
		SetCurrency oldObj = (SetCurrency)setCurrencyDao.load(setCurrency.getClass(), setCurrency.getCcyCode());
        if(oldObj != null){
        	setCurrencyDao.delete(oldObj);
        }
        setCurrencyDao.add(setCurrency);

    }*/
	
	public void update(SetCurrency setCurrency) throws NTBException {
		setCurrencyDao.update(setCurrency);
    	/*Map conditionMap = new HashMap();
    	conditionMap.put("CCY_CODE", setCurrency.getCcyCode());
    	conditionMap.put("AVAILABLE_FLAG", setCurrency.getAvailableFlag());
    	genericJdbcDao.updateByObject("AVAILABLE_CURRENCIES", setCurrency, conditionMap);*/
    }
	public void updateHis(SetCurrencyHis setCurrencyHis) throws NTBException {
		setCurrencyDao.update(setCurrencyHis);
		/*Map conditionMap = new HashMap();
    	conditionMap.put("CCY_CODE", setCurrencyHis.getCcyCode());
    	conditionMap.put("AVAILABLE_FLAG", setCurrencyHis.getAvailableFlag());
    	conditionMap.put("STATUS", setCurrency.getStatus());
    	genericJdbcDao.updateByObject("AVAILABLE_CURRENCIES_HIS", setCurrencyHis, conditionMap);*/
	}

	public void approve() throws NTBException {
		try {
			List list = this.setCurrencyDao.listManageCcyPending();
			SetCurrencyHis setCurrencyHis = null;
//			SetCurrency setCurrency = null;
			for (int i = 0; i < list.size(); i++) {
				setCurrencyHis = (SetCurrencyHis) list.get(i);
				SetCurrency setCurrency = new SetCurrency(setCurrencyHis.getCcyCode());
	            BeanUtils.copyProperties(setCurrency, setCurrencyHis);
//				setCurrency = loadCurrency(setCurrencyHis.getCcyCode());
				// set availableFlag
//				setCurrency.setAvailableFlag(setCurrencyHis.getAvailableFlag());
				setCurrencyDao.update(setCurrency);
				// set his normal
				setCurrencyHis.setStatus(Constants.STATUS_NORMAL);
				setCurrencyDao.update(setCurrencyHis);
			}
		} catch (Exception e) {
			throw new NTBException("err.sys.DBError");
		}
	}

	public void reject() throws NTBException {	
		/*try {
			genericJdbcDao.update(REJECT_CONTROL, new Object[]{});
		} catch (Exception e) {
			Log.error("reject control ccy error", e);
			throw new NTBException("err.sys.DBError");
		}*/
		try {
			List list = this.setCurrencyDao.listManageCcyPending();
			SetCurrencyHis setCurrencyHis = null;
			SetCurrency setCurrency = null;
			for (int i = 0; i < list.size(); i++) {
				setCurrencyHis = (SetCurrencyHis) list.get(i);
				setCurrency = loadCurrency(setCurrencyHis.getCcyCode());
				// set his roll back availableFlag
				setCurrencyHis.setAvailableFlag(setCurrency.getAvailableFlag());
				// set his normal
				setCurrencyHis.setStatus(Constants.STATUS_NORMAL);
				setCurrencyDao.update(setCurrencyHis);
			}
		} catch (Exception e) {
			throw new NTBException("err.sys.DBError");
		}		
	}
	public SetCurrencyHis loadPendingHis(String ccyCode) throws NTBException {
        Map conditionMap = new HashMap();
        conditionMap.put("ccyCode", ccyCode);
      //Constants.STATUS_PENDING_APPROVAL
        List ccyHisList = setCurrencyDao.list(SetCurrencyHis.class, conditionMap);
        if (ccyHisList == null || ccyHisList.size() == 0) {
            return null;
        }
        SetCurrencyHis ccyHis = (SetCurrencyHis) ccyHisList.get(0);
        return ccyHis;
    }
	public SetCurrency loadCurrency(String ccyCode) throws NTBException {
		Map conditionMap = new HashMap();
        conditionMap.put("ccyCode", ccyCode);
        List ccyList = setCurrencyDao.list(SetCurrency.class, conditionMap);
        if (ccyList == null || ccyList.size() == 0) {
            return null;
        }
        SetCurrency ccy = (SetCurrency) ccyList.get(0);
        return ccy;
    }
}
