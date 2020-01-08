package app.cib.service.bnk;

import java.util.*;

import app.cib.bo.bnk.*;
import app.cib.bo.sys.*;
import app.cib.core.*;
import app.cib.dao.bnk.*;
import app.cib.service.sys.*;
import app.cib.util.*;
import com.neturbo.set.core.*;
import com.neturbo.set.database.*;
import com.neturbo.set.exception.*;
import org.springframework.context.*;

public class CorpSubsidiaryServiceImpl implements CorpSubsidiaryService {
    private CorpSubsidiaryDao corpSubsidiaryDao;
    private GenericJdbcDao genericJdbcDao;

    public void updateSubsidiary(CorpPreference corpPref, String parentId,List oldSubsidiaryList,
                                 List subsidiaryList) throws NTBException {

        //д��CorpPreference pojo
        ApplicationContext appContext = Config.getAppContext();
        CorpPreferenceService corpPrefService = (CorpPreferenceService)
                                                appContext.getBean(
                "CorpPreferenceService");

        corpPref.setCorpId(parentId);
        corpPref.setStatus(Constants.STATUS_PENDING_APPROVAL);
        corpPref.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
        corpPref.setPrefType(CorpPreference.PREF_TYPE_SUBSIDIARY);
        corpPrefService.newPref(corpPref);

        //�� Before ��¼ д�� CorpSubsidiary pojo
        for (int i = 0; i < oldSubsidiaryList.size(); i++) {
            Corporation subCorp = (Corporation) oldSubsidiaryList.get(i);
            CorpSubsidiary subsidiary = new CorpSubsidiary(CibIdGenerator.
                    getIdForOperation(
                            "CORP_SUBSIDIARY"));
            subsidiary.setPrefId(corpPref.getPrefId());
            subsidiary.setPerentId(parentId);
            subsidiary.setSubsidiryId(subCorp.getCorpId());
            //Operation �� Status ����Ϊ UPDATE_BEFORE, AUTH_STATUS ����Ϊ SUBMITED
            subsidiary.setOperation(Constants.OPERATION_UPDATE_BEFORE);
            subsidiary.setStatus(Constants.STATUS_UPDATE_BEFORE);
            subsidiary.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
            subsidiary.setRequester(corpPref.getRequester());
            subsidiary.setLastUpdateTime(corpPref.getLastUpdateTime());
            corpSubsidiaryDao.add(subsidiary);
        }

        //д��CorpSubsidiary pojo
        //����ϼ�¼Ϊ�գ���Ϊ����������Ϊ�޸�
        String operation = Constants.OPERATION_NEW;
        if(oldSubsidiaryList.size()>0){
            operation = Constants.OPERATION_UPDATE;
        }
        for (int i = 0; i < subsidiaryList.size(); i++) {
            Corporation subCorp = (Corporation) subsidiaryList.get(i);
            CorpSubsidiary subsidiary = new CorpSubsidiary(CibIdGenerator.
                    getIdForOperation(
                            "CORP_SUBSIDIARY"));
            subsidiary.setPrefId(corpPref.getPrefId());
            subsidiary.setPerentId(parentId);
            subsidiary.setSubsidiryId(subCorp.getCorpId());
            subsidiary.setOperation(operation);
            subsidiary.setStatus(Constants.STATUS_PENDING_APPROVAL);
            subsidiary.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
            subsidiary.setRequester(corpPref.getRequester());
            subsidiary.setLastUpdateTime(corpPref.getLastUpdateTime());
            corpSubsidiaryDao.add(subsidiary);
        }
    }

    public List listSubsidiary(String corpId) throws NTBException {
        List subList = corpSubsidiaryDao.list(
                "from Corporation as corp where corp.parentCorp=?",
                new Object[] {corpId});

        return subList;
    }

    public List listSubsidiaryDef(String corpId) throws NTBException {
        List subList = corpSubsidiaryDao.list(
                "from CorpSubsidiary as corp where corp.perentId=? and corp.status='0'",
                new Object[] {corpId});

        return subList;
    }

    public List listSubsidiaryDefInApproval(String corpId) throws NTBException {
        List subList = corpSubsidiaryDao.list(
                "from CorpSubsidiary as corp where corp.perentId=? and corp.status='1'",
                new Object[] {corpId});

        return subList;
    }

    public List listSubsidiaryByPrefId(String prefId) throws NTBException {
        List subList = corpSubsidiaryDao.list("Select corp from Corporation as corp, CorpSubsidiary as subcorp where corp.corpId=subcorp.subsidiryId and subcorp.prefId=? and subcorp.status <> ?",
                                              new Object[] {prefId, Constants.STATUS_UPDATE_BEFORE});
        return subList;
    }

    public void updateForApprove(String prefId) throws NTBException {
        try {
            ApplicationContext appContext = Config.getAppContext();
            CorpPreferenceService corpPrefService = (CorpPreferenceService)
                    appContext.getBean(
                            "CorpPreferenceService");
            CorpPreference corpPref = corpPrefService.findCorpPrefByID(prefId);
            String corpId = corpPref.getCorpId();

            corpPref.setStatus(Constants.STATUS_NORMAL);
            corpPref.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            corpPref.setLastUpdateTime(new Date());
            corpPrefService.updatePref(corpPref);

            genericJdbcDao.update(
                    "UPDATE CORPORATION CORP SET CORP.PARENT_CORP=NULL WHERE CORP.PARENT_CORP=?",
                    new Object[] {corpId});
            genericJdbcDao.update("UPDATE CORPORATION  CORP SET (CORP.PARENT_CORP)=(SELECT PERENT_ID FROM CORP_SUBSIDIARY SUBS WHERE CORP.CORP_ID=SUBS.SUBSIDIRY_ID AND SUBS.PREF_ID=? AND SUBS.OPERATION <>?) WHERE CORP.CORP_ID IN (SELECT SUBS.SUBSIDIRY_ID FROM CORP_SUBSIDIARY SUBS WHERE SUBS.PREF_ID=? AND SUBS.OPERATION <>?)",
                                  new Object[] {prefId, Constants.OPERATION_UPDATE_BEFORE, prefId, Constants.OPERATION_UPDATE_BEFORE});
            genericJdbcDao.update(
                    "UPDATE CORP_SUBSIDIARY  SUBS SET STATUS=?, AUTH_STATUS=?, LAST_UPDATE_TIME=? WHERE SUBS.PREF_ID=? AND OPERATION <>? ",
                    new Object[] {Constants.STATUS_NORMAL, Constants.AUTH_STATUS_COMPLETED, new Date(), prefId, Constants.OPERATION_UPDATE_BEFORE});
            genericJdbcDao.update(
                    "UPDATE CORP_SUBSIDIARY  SUBS SET AUTH_STATUS=?, LAST_UPDATE_TIME=? WHERE SUBS.PREF_ID=? AND OPERATION=?",
                    new Object[] {Constants.AUTH_STATUS_COMPLETED, new Date(), prefId, Constants.OPERATION_UPDATE_BEFORE});
        } catch (Exception e) {
            Log.error("Error updating to table of corporation", e);
            throw new NTBException(ErrConstants.GENERAL_ERROR);
        }
    }

    public void updateForReject(String prefId) throws NTBException {
        try {
            ApplicationContext appContext = Config.getAppContext();
            CorpPreferenceService corpPrefService = (CorpPreferenceService)
                    appContext.getBean(
                            "CorpPreferenceService");
            CorpPreference corpPref = corpPrefService.findCorpPrefByID(prefId);
            corpPref.setStatus(Constants.STATUS_REMOVED);
            corpPref.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
            corpPref.setLastUpdateTime(new Date());
            corpPrefService.updatePref(corpPref);

            genericJdbcDao.update(
                    "UPDATE CORP_SUBSIDIARY SUBS SET STATUS = ?,AUTH_STATUS=?,LAST_UPDATE_TIME=?  WHERE SUBS.PREF_ID=?",
                    new Object[] {new Date(),Constants.STATUS_REMOVED,Constants.AUTH_STATUS_REJECTED,new Date(), prefId});
        } catch (Exception e) {
            Log.error("Error delete from table of corp_subsidiary", e);
            throw new NTBException(ErrConstants.GENERAL_ERROR);
        }
    }


    public List listCorpForAdd(String parentId, String corpId, String corpName) throws
            NTBException {
        String hql = "from Corporation as corp where (corp.parentCorp is null or corp.parentCorp='') and corp.corpId <> ? and corp.status='0'";
        ArrayList objs = new ArrayList();
        objs.add(parentId);
        if (corpId != null && !corpId.equals("")) {
            hql += " and corp.corpId like '%" + corpId + "%'";
        }
        if (corpName != null && !corpName.equals("")) {
            hql += " and corp.corpName like '%" + corpName + "%'";
        }
        List subList = corpSubsidiaryDao.list(hql, objs.toArray());
        return subList;
    }

    public CorpSubsidiaryDao getCorpSubsidiaryDao() {
        return corpSubsidiaryDao;
    }

    public GenericJdbcDao getGenericJdbcDao() {
        return genericJdbcDao;
    }

    public void setCorpSubsidiaryDao(CorpSubsidiaryDao corpSubsidiaryDao) {
        this.corpSubsidiaryDao = corpSubsidiaryDao;
    }

    public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
        this.genericJdbcDao = genericJdbcDao;
    }

}
