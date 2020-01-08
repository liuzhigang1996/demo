/**
 * @author hjs
 * 2006-8-2
 */
package app.cib.service.sys;

import app.cib.bo.sys.CorpPreference;

import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2006-8-2
 */
public interface CorpPreferenceService {

    public void removePref(CorpPreference pojoCorpPref) throws NTBException;

    public void newPref(CorpPreference pojoCorpPref) throws NTBException;

    public void updatePref(CorpPreference pojoCorpPref) throws NTBException;

    public CorpPreference findCorpPrefByID(String prefID) throws NTBException;

    public CorpPreference findCorpPrefByCorpId(String corpId, String prefType, String status) throws NTBException;

    public CorpPreference getLatestPref(String corpId) throws NTBException;

    public boolean isAuthorization(String prefID) throws NTBException;

}
