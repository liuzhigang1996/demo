/**
 * @author hjs
 * 2006-8-3
 */
package app.cib.action.bnk;

import java.util.*;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

import app.cib.core.*;
import app.cib.util.*;
import app.cib.service.bnk.CorpSubsidiaryService;
import app.cib.service.bnk.CorporationService;
import app.cib.bo.bnk.Corporation;
import app.cib.bo.sys.CorpPreference;
import app.cib.service.sys.CorpPreferenceService;
import app.cib.bo.bnk.CorpSubsidiary;
import app.cib.bo.sys.UserGroup;
import app.cib.service.sys.GroupService;
import app.cib.action.sys.GroupAction;
import app.cib.service.flow.FlowEngineService;
import com.neturbo.set.core.Log;

/**
 * @author hjs
 * 2006-8-3
 */
public class CorpSubsidiaryAction extends CibAction implements Approvable {

    public void listSubsidiaryLoad() throws NTBException {

        //���ÿյ� ResultData �����ʾ����
        setResultData(new HashMap(1));
    }

    public void listSubsidiary() throws NTBException {

        //��� corpId
        String corpId = Utils.null2EmptyWithTrim(this.getParameter("corpId"));

        ApplicationContext appContext = Config.getAppContext();
        CorporationService corporationService = (CorporationService)
                                                appContext.getBean(
                "CorporationService");
        //��� corpId ��Ӧ�� Corporation Object
        Corporation corp1 = corporationService.view(corpId);
        //��� Corporation �������򱨴�
        if (corp1 == null) {
            throw new NTBException("err.bnk.CorpNotExist");
        }
        String corpName = corp1.getCorpName();

        CorpSubsidiaryService corpSubsidiaryService = (
                CorpSubsidiaryService)
                appContext.getBean("CorpSubsidiaryService");
        CorpPreferenceService corpPrefService = (CorpPreferenceService)
                                                appContext.getBean(
                "CorpPreferenceService");

        String prefId = null;
        List subsidiaryList = corpSubsidiaryService.listSubsidiary(corpId);
        List subDefInApprvalList = corpSubsidiaryService.
                                   listSubsidiaryDefInApproval(corpId);
        if (subDefInApprvalList.size() > 0) {
            prefId = ((CorpSubsidiary) subDefInApprvalList.get(0)).getPrefId();
        }

        //�������Version �� CorpPreference
        CorpPreference corpPref = corpPrefService.findCorpPrefByID(prefId);
        //���������Ȩ��������У�������ò������޸ģ�����
        String allowEdit = "YES";
        String editableStatus = Constants.STATUS_NORMAL;
        if (corpPref != null &&
            corpPref.getStatus().equals(Constants.STATUS_PENDING_APPROVAL)) {
            allowEdit = "NO";
            editableStatus = Constants.STATUS_PENDING_APPROVAL;
        }

        //���ԭ��������
        Map corpInfoMap = new HashMap();
        corpInfoMap.put("corpId", corpId);
        corpInfoMap.put("corpName", corpName);
        corpInfoMap.put("prefId", prefId);
        corpInfoMap.put("allowEdit", allowEdit);
        corpInfoMap.put("editableStatus", editableStatus);
        corpInfoMap.put("subsidiaryList", subsidiaryList);
        List oldSubsidiaryList = new ArrayList();
        oldSubsidiaryList.addAll(subsidiaryList);
        corpInfoMap.put("oldSubsidiaryList", oldSubsidiaryList);

        //��ʾԭ��������
        List mapList = this.convertPojoList2MapList(subsidiaryList);
        Map resultData = new HashMap();
        resultData.putAll(corpInfoMap);
        resultData.put("subsidiaryList", mapList);

        setResultData(resultData);

        //�����ݴ���session
        this.setUsrSessionDataValue("corpInfoMap", corpInfoMap);
    }

    public void addSubsidiary() throws NTBException {

        String subsidiaryID = this.getParameter("subsidiaryIDs");
        String[] subArray = Utils.splitStr(subsidiaryID, ";");
        ApplicationContext appContext = Config.getAppContext();
        CorporationService corporationService = (CorporationService)
                                                appContext.getBean(
                "CorporationService");

        Map corpInfoMap = (Map)this.getUsrSessionDataValue("corpInfoMap");
        if (corpInfoMap == null) {
            corpInfoMap = new HashMap();
        }
        List subsidiaryList = (List) corpInfoMap.get("subsidiaryList");
        if (subsidiaryList == null) {
            subsidiaryList = new ArrayList();
        }

        String parentId = (String) corpInfoMap.get("corpId");
        
        // Jet modify 2008-1-16
        Set excludeIdSet = new HashSet();
        excludeIdSet.add(parentId);

//        String excludeIds = parentId;
        for (int i = 0; i < subsidiaryList.size(); i++) {
//            excludeIds += "," + ((Corporation) subsidiaryList.get(i)).getCorpId();
        	excludeIdSet.add(((Corporation) subsidiaryList.get(i)).getCorpId());
        }

        for (int i = 0; i < subArray.length; i++) {
            if (!subArray[i].equals("")) {            	
//                if (excludeIds.indexOf(subArray[i]) < 0) {
                if (!excludeIdSet.contains(subArray[i])){	
                    Corporation corpObj = corporationService.view(subArray[i]);
                    subsidiaryList.add(corpObj);
                }
            }
        }
        List mapList = this.convertPojoList2MapList(subsidiaryList);

        Map resultData = new HashMap();
        resultData.putAll(corpInfoMap);
        resultData.put("subsidiaryList", mapList);
        setResultData(resultData);

        corpInfoMap.put("subsidiaryList", subsidiaryList);
        this.setUsrSessionDataValue("corpInfoMap", corpInfoMap);
    }

    public void listCorpForAdd() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        CorpSubsidiaryService corpSubsidiaryService = (
                CorpSubsidiaryService)
                appContext.getBean("CorpSubsidiaryService");
        CorporationService corporationService = (CorporationService)
                                                appContext.getBean(
                "CorporationService");

        String corpId = Utils.null2EmptyWithTrim(this.getParameter("corpId"));
        String corpName = Utils.null2EmptyWithTrim(this.getParameter(
                "corpName"));



        Map corpInfoMap = (Map)this.getUsrSessionDataValue("corpInfoMap");
        String parentId = (String) corpInfoMap.get("corpId");
        //��� corpId ��Ӧ�� Parent Corporation Object
        Corporation corp1 = corporationService.view(parentId);
        while (corp1.getParentCorp() != null &&
               !corp1.getParentCorp().equals("")) {
            corp1 = corporationService.view(corp1.getParentCorp());
        }

        List subsidiaryList = (List) corpInfoMap.get("subsidiaryList");
        
        // Jet modify 2008-1-16
        Set excludeIdSet = new HashSet();
        excludeIdSet.add(parentId);
        //        String excludeIds = parentId;
        if (corp1 != null) {
        	excludeIdSet.add(corp1.getCorpId());
        	//            excludeIds += "," + corp1.getCorpId();
        }
        for (int i = 0; i < subsidiaryList.size(); i++) {
        	excludeIdSet.add(((Corporation) subsidiaryList.get(i)).getCorpId());
//            excludeIds += "," + ((Corporation) subsidiaryList.get(i)).getCorpId();
        }

        List corpList = corpSubsidiaryService.listCorpForAdd(parentId, corpId,
                corpName);
        for (int i = 0; i < corpList.size(); i++) {
//            if (excludeIds.indexOf(((Corporation) corpList.get(i)).getCorpId()) >= 0)
        	if (excludeIdSet.contains(((Corporation) corpList.get(i)).getCorpId())) {
                corpList.remove(corpList.get(i));
                i--;
            }
        }
        List oldSubsidiaryList = (List) corpInfoMap.get("oldSubsidiaryList");
        List removedList = new ArrayList();
        removedList.addAll(oldSubsidiaryList);
        for (int i = 0; i < removedList.size(); i++) {
//            if (excludeIds.indexOf(((Corporation) removedList.get(i)).getCorpId()) >= 0) {
        	if (excludeIdSet.contains(((Corporation) removedList.get(i)).getCorpId())){
                removedList.remove(removedList.get(i));
                i--;
            }
        }
        corpList.addAll(removedList);

        List mapList = this.convertPojoList2MapList(corpList);

        Map resultData = new HashMap();
        resultData.put("corpId", corpId);
        resultData.put("corpName", corpName);
        resultData.put("corpList", mapList);
        setResultData(resultData);
    }

    public void removeSubsidiary() throws NTBException {

        String subsidiaryID = this.getParameter("removeIDs");

        Map corpInfoMap = (Map)this.getUsrSessionDataValue("corpInfoMap");
        if (corpInfoMap == null) {
            corpInfoMap = new HashMap();
        }
        List subsidiaryList = (List) corpInfoMap.get("subsidiaryList");
        if (subsidiaryList == null) {
            subsidiaryList = new ArrayList();
        }

        for (int i = 0; i < subsidiaryList.size(); i++) {
            Corporation corpObj = (Corporation) subsidiaryList.get(i);
            if (corpObj.getCorpId().equals(subsidiaryID)) {
                subsidiaryList.remove(i);
            }
        }
        List mapList = this.convertPojoList2MapList(subsidiaryList);

        Map resultData = new HashMap();
        resultData.putAll(corpInfoMap);
        resultData.put("subsidiaryList", mapList);
        setResultData(resultData);

        corpInfoMap.put("subsidiaryList", subsidiaryList);
        this.setUsrSessionDataValue("corpInfoMap", corpInfoMap);
    }

    public void updateSubsidiary() throws NTBException {
        Map corpInfoMap = (Map)this.getUsrSessionDataValue("corpInfoMap");
        List subsidiaryList = (List) corpInfoMap.get("subsidiaryList");
        
        // Jet modify 2008-1-16
        Set excludeIdSet = new HashSet();
                
//        String excludeIds = "";
        for (int i = 0; i < subsidiaryList.size(); i++) {
        	excludeIdSet.add(((Corporation) subsidiaryList.get(i)).getCorpId());
//            excludeIds += "," + ((Corporation) subsidiaryList.get(i)).getCorpId();
        }

        //��ʾ
        List mapList = this.convertPojoList2MapList(subsidiaryList);

        Map resultData = new HashMap();
        resultData.putAll(corpInfoMap);
        resultData.put("subsidiaryList", mapList);
        setResultData(resultData);

        List oldSubsidiaryList = (List) corpInfoMap.get("oldSubsidiaryList");
        List removedList = new ArrayList();
        removedList.addAll(oldSubsidiaryList);
        for (int i = 0; i < removedList.size(); i++) {
//            if (excludeIds.indexOf(((Corporation) removedList.get(i)).getCorpId()) >= 0) {
            if(excludeIdSet.contains(((Corporation) removedList.get(i)).getCorpId())){
                removedList.remove(removedList.get(i));
                i--;
            }
        }
        //����δ�ı䣬����
        if (removedList.size() == 0 &&
            oldSubsidiaryList.size() == subsidiaryList.size()) {
            throw new NTBException("err.bnk.SubsidiaryNotChanged");
        }

    }

    public void updateSubsidiaryReturn() throws NTBException {
    }

    public void updateSubsidiaryCfm() throws NTBException {
        //��session �������
        Map corpInfoMap = (Map)this.getUsrSessionDataValue("corpInfoMap");
        String corpId = (String) corpInfoMap.get("corpId");
        String corpName = (String) corpInfoMap.get("corpName");
        String oldPrefId = (String) corpInfoMap.get("prefId");
        List subsidiaryList = (List) corpInfoMap.get("subsidiaryList");
        List oldSubsidiaryList = (List) corpInfoMap.get("oldSubsidiaryList");

        //��ʼ��Service
        ApplicationContext appContext = Config.getAppContext();
        CorpSubsidiaryService corpSubsidiaryService = (
                CorpSubsidiaryService)
                appContext.getBean(
                        "CorpSubsidiaryService");
        CorpPreferenceService corpPrefService = (CorpPreferenceService)
                                                appContext.getBean(
                "CorpPreferenceService");

        //�����һ�ν����Ƿ�����Ȩ�ɹ�
        //�������Version �� CorpPreference
        CorpPreference corpPref = corpPrefService.getLatestPref(oldPrefId);
        //���������Ȩ��������У�������ò������޸ģ�����
        if (corpPref != null &&
            corpPref.getStatus().equals(Constants.STATUS_PENDING_APPROVAL)) {
            throw new NTBException("err.bnk.LastTransNotCompleted");
        }

        //��֯CorpPreference pojo
        String prefId = CibIdGenerator.getIdForOperation(
                "CORP_PREFERENCE");

        //����������
        FlowEngineService flowEngineService = (FlowEngineService) appContext.
                                              getBean("FlowEngineService");
        String processId = flowEngineService.startProcess(Constants.
                TXN_SUBTYPE_PREF_SUBSIDIARY,
                "0",
                this.getClass(), null, 0, null, 0, 0, prefId, null, getUser(), null,
                null,null);

        try {
            //д���ݿ�
            if (corpPref == null) {
                corpPref = new CorpPreference(prefId);
                corpPref.setVersion(new Integer(1));
            } else {
                corpPref.setPrefId(prefId);
                corpPref.setVersion(new Integer(corpPref.getVersion().intValue() +
                                                1));
            }

            //д�� preference ��� subsidiary ��
            corpPref.setRequester(this.getUser().getUserId());
            corpPref.setLastUpdateTime(new Date());
            corpSubsidiaryService.updateSubsidiary(corpPref, corpId,oldSubsidiaryList,
                    subsidiaryList);
        } catch (Exception e) {
            //������ִ�����ֹ������
            flowEngineService.cancelProcess(processId, getUser());
            Log.error(
                    "Error processing confirmation operation of CORP_SUBSIDIARY",
                    e);
            throw new NTBException(ErrConstants.GENERAL_ERROR);
        }
    }

    public boolean approve(String txnType, String id, CibAction bean) throws
            NTBException {
        ApplicationContext appContext = Config.getAppContext();
        CorpSubsidiaryService corpSubsidiaryService = (
                CorpSubsidiaryService)
                appContext.getBean("CorpSubsidiaryService");
        corpSubsidiaryService.updateForApprove(id);
        //����corporation rc
        CachedDBRCFactory.addPendingCache("corpInTree");
        return true;
    }

    public boolean reject(String txnType, String id, CibAction bean) throws
            NTBException {
        ApplicationContext appContext = Config.getAppContext();

        CorpSubsidiaryService corpSubsidiaryService = (
                CorpSubsidiaryService)
                appContext.getBean("CorpSubsidiaryService");
        corpSubsidiaryService.updateForReject(id);
        return true;
    }

    public String viewDetail(String txnType, String id, CibAction bean) throws
            NTBException {

        ApplicationContext appContext = Config.getAppContext();
        //�������Version �� CorpPreference
        CorpPreferenceService corpPrefService = (CorpPreferenceService)
                                                appContext.getBean(
                "CorpPreferenceService");
        CorpPreference corpPref = corpPrefService.findCorpPrefByID(id);

        String corpId = corpPref.getCorpId();

        CorporationService corporationService = (CorporationService)
                                                appContext.getBean(
                "CorporationService");
        //��� corpId ��Ӧ�� Corporation Object
        Corporation corp1 = corporationService.view(corpId);
        //��� Corporation �������򱨴�
        if (corp1 == null) {
            throw new NTBException("err.bnk.CorpNotExist");
        }
        String corpName = corp1.getCorpName();

        CorpSubsidiaryService corpSubsidiaryService = (
                CorpSubsidiaryService)
                appContext.getBean("CorpSubsidiaryService");

        //���ԭ��������
        Map resultData = bean.getResultData();
        resultData.put("corpId", corpId);
        resultData.put("corpName", corpName);
        List subsidiaryList = corpSubsidiaryService.listSubsidiaryByPrefId(id);
        List mapList = this.convertPojoList2MapList(subsidiaryList);
        resultData.put("subsidiaryList", mapList);
        bean.setResultData(resultData);
        return "/WEB-INF/pages/bank/corp_subsidiary/corp_subsidiary_view.jsp";
    }

    public boolean cancel(String txnType, String id, CibAction bean) throws
            NTBException {
        return reject(txnType, id, bean);
    }
}
