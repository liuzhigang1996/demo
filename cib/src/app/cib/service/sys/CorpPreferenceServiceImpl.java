/**
 * @author hjs
 * 2006-8-2
 */
package app.cib.service.sys;

import java.util.*;
import app.cib.bo.sys.CorpPreference;
import app.cib.dao.sys.CorpPreferenceDao;
import app.cib.util.Constants;

import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 2006-8-2
 */
public class CorpPreferenceServiceImpl implements CorpPreferenceService {

    private CorpPreferenceDao corpPreferenceDao;

    public CorpPreferenceDao getCorpPreferenceDao() {
        return corpPreferenceDao;
    }

    public void setCorpPreferenceDao(CorpPreferenceDao corpPreferenceDao) {
        this.corpPreferenceDao = corpPreferenceDao;
    }

    /* (non-Javadoc)
     * @see app.cib.service.sys.CorpPreferenceService#newPref(app.cib.bo.sys.CorpPreference)
     */
    public void newPref(CorpPreference pojoCorpPref) throws NTBException {
        corpPreferenceDao.add(pojoCorpPref);

    }

    /* (non-Javadoc)
     * @see app.cib.service.sys.CorpPreferenceService#removePref(app.cib.bo.sys.CorpPreference)
     */
    public void removePref(CorpPreference pojoCorpPref) throws NTBException {
        corpPreferenceDao.update(pojoCorpPref);

    }

    public void updatePref(CorpPreference pojoCorpPref) throws NTBException {
        corpPreferenceDao.update(pojoCorpPref);
    }

    public CorpPreference findCorpPrefByID(String prefID) throws NTBException {
    	prefID = Utils.null2EmptyWithTrim(prefID);
        return (CorpPreference) corpPreferenceDao.load(CorpPreference.class, prefID);
    }

	public CorpPreference findCorpPrefByCorpId(String corpId, String prefType, String status) throws NTBException {
		Map conditionMap = new HashMap();
		conditionMap.put("corpId", corpId);
		conditionMap.put("prefType", prefType);
		conditionMap.put("status", status);
		List list = corpPreferenceDao.list(CorpPreference.class, conditionMap);
		if (list.size()>0) {
			return (CorpPreference) list.get(0);
		} else {
			return null;
		}
	}

    public boolean isAuthorization(String prefID) throws NTBException {
        CorpPreference corpPref = (CorpPreference) corpPreferenceDao.load(CorpPreference.class, prefID);
        String status = corpPref.getStatus();
        String authStatus = corpPref.getAuthStatus();
        
        if (((status.equals(Constants.STATUS_NORMAL)) && (authStatus.equals(Constants.AUTH_STATUS_COMPLETED))) ||
        	((status.equals(Constants.STATUS_REMOVED)) && (authStatus.equals(Constants.AUTH_STATUS_REJECTED)))
        	) {
        	return true;
        } else {
        	return false;
        }
        /*
        if ((!corpPref.getAuthStatus().equals(Constants.AUTH_STATUS_COMPLETED)) ||
            (!corpPref.getStatus().equals(Constants.STATUS_NORMAL)) ||
            (!corpPref.getStatus().equals(Constants.STATUS_REMOVED))) {
            return false;
        } else {
            return true;
        }
        */
    }

    public CorpPreference getLatestPref(String relativeId) throws NTBException {
        if(relativeId == null) return null;
        String hql = "from CorpPreference as pref where pref.relativeId=?";
        List prefList = corpPreferenceDao.list(hql, new Object[] {relativeId});
        if (prefList != null && prefList.size() > 0) {
            return (CorpPreference) prefList.get(0);
        } else {
            return null;
        }
    }

}
