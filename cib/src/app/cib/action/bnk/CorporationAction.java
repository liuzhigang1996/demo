package app.cib.action.bnk;

/**
 * @author nabai
 *
 * 锟矫戯拷锟斤拷?锟斤拷锟斤拷锟睫改★拷删锟斤拷锟窖拷锟斤拷i锟斤拷锟斤拷锟斤拷锟絠锟斤拷锟斤拷锟斤拷锟杰碼
 */
import java.util.*;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.ApplicationContext;

import app.cib.bo.bnk.BankUser;
import app.cib.bo.bnk.Corporation;
import app.cib.bo.bnk.CorporationHis;
import app.cib.bo.sys.CorpUser;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.core.CibTransClient;
import app.cib.service.bat.SchTransferService;
import app.cib.service.bat.SchTxnBatchService;
import app.cib.service.bnk.CorporationService;
import app.cib.service.bnk.ViewerAccessListService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.sys.ApproveRuleService;
import app.cib.service.txn.TransferService;
import app.cib.util.Constants;
import app.cib.util.RcCorporation;

import com.neturbo.set.core.*;
import com.neturbo.set.exception.*;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

import app.cib.util.CachedDBRCFactory;

public class CorporationAction extends CibAction implements Approvable {

    public void addLoad() throws NTBException {
    	
    	//add by wen_chy 2009.09.16
    	/*CorporationService corpService = (CorporationService) Config
        .getAppContext().getBean("CorporationService");
    	List merchantGroupList=corpService.listMerchantGroup("");
    	if(merchantGroupList==null){
    		merchantGroupList =new ArrayList();
    	}
    	resultData.put("merchantGroupList", merchantGroupList);
    	*/
    	
        // 锟斤拷锟矫空碉拷 ResultData 锟斤拷锟斤拷锟绞撅拷锟斤拷
        Map resultData = new HashMap();
        //setResultData(new HashMap(1));
        resultData.put("allowTd", "Y");
        resultData.put("allowExecutor", "Y");
        resultData.put("allowApproverSelection", "Y");
        resultData.put("allowTaxPayment", "N");
        resultData.put("allowDisplayBottom", "N");
        resultData.put("authCurMop", "Y");
        resultData.put("allowFinancialController", "N");
        this.setResultData(resultData);

    }

    public void add() throws NTBException {
    	
    	CorporationService corpService = (CorporationService) Config
        .getAppContext().getBean("CorporationService");
    	
    	Map para = this.getParameters();
    	String issueDateString1 = this.getParameter("rep1IdIssueDate");
    	String issueDateString2 = this.getParameter("rep2IdIssueDate");

    	Date rep1IdIssueDate = null;
    	Date rep2IdIssueDate = null;
    	if(issueDateString1!=null && !issueDateString1.equals("")){
    		rep1IdIssueDate = DateTime.getDateFromStr(issueDateString1);
    	}
    	if(issueDateString2!=null && !issueDateString2.equals("")){
    		rep2IdIssueDate = DateTime.getDateFromStr(issueDateString2);
    	}
    	
    	para.put("rep1IdIssueDate", rep1IdIssueDate);
    	para.put("rep2IdIssueDate", rep2IdIssueDate);
    	CorporationHis corpHisObj = new CorporationHis();
        this.convertMap2Pojo(this.getParameters(), corpHisObj);
        //add by linrui 20180211
        corpHisObj.setCorpId("C"+corpHisObj.getCifNo());
        //add by wen_chy 20090917
        String merchantGroup=corpHisObj.getMerchantGroup();
        if(merchantGroup!=null&&!"".equals(merchantGroup)){
        	List merchantGroupList=corpService.listMerchantGroup(merchantGroup.toUpperCase());
        	if(merchantGroupList!=null&&merchantGroupList.size()>0){
        		corpHisObj.setMerchantGroup(merchantGroup.toUpperCase());
        	}else{
        		throw new NTBException("err.bnk.MerchantGroupNoExist");
        	}
        }
        
        //add by hjs 20070712
        //锟斤拷corp type为2锟斤拷3时锟斤拷锟斤拷锟斤拷欠锟窖★拷锟紸llow Executor
        if(Constants.CORP_TYPE_MIDDLE.equals(corpHisObj.getCorpType())
        		||Constants.CORP_TYPE_SMALL.equals(corpHisObj.getCorpType())
        		||Constants.CORP_TYPE_MIDDLE_NO_ADMIN.equals(corpHisObj.getCorpType())){
            if(Constants.YES.equals(corpHisObj.getAllowExecutor())){
            	throw new NTBException("err.bnk.NoExecutorAllowed");
            }
            corpHisObj.setAllowExecutor(Constants.NO);
        }
        
        // add by Jet 20071108
        // Authentication mode must be e-cert when the corp type is 1
        if (Constants.CORP_TYPE_LARGE.equals(corpHisObj.getCorpType())){
        	corpHisObj.setAuthenticationMode(Constants.AUTHENTICATION_CERTIFICATION);
        }


        Corporation corporation = corpService.view(corpHisObj.getCorpId());//mod by linrui 20180211  corpHisObj.getCorpId()      
        if (corporation != null) {
            throw new NTBException("err.bnk.CorpExist");
        }
        if (corpService.loadPendingHis(corpHisObj.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
        String statusFromHost = corpService.getCorporationStatus(corpHisObj//mod by linrui 20180211 corpHisObj .getCorpId()
                .getCifNo(), this.getUser(),lang);//end
//        if (statusFromHost.equals(Corporation.CORP_STATUS_NOTFOUND)) { mod by linrui 20180211
        if (statusFromHost.contains(Corporation.CORP_STATUS_NOTFOUND)) {
            throw new NTBException("err.bnk.CorpNotFound");
        }
//        if (statusFromHost.equals(Corporation.CORP_STATUS_CLOSE)) {mod by linrui 20180211
        if (statusFromHost.contains(Corporation.CORP_STATUS_CLOSE)) {
            throw new NTBException("err.bnk.CorpClosed");
        }
        
        //add by lzg 20190705
        Map retMap = getCusInfo(corpHisObj.getCorpId());
        if(retMap.get("CUS_TYPE").equals("P")){
        	throw new NTBException("err.bnk.CorpNotValid");
        }
        //add by lzg end
        
        // 锟斤拷式锟斤拷auth cur锟街讹拷
        /* Add by long_zg 2019-05-16 add by long_zg 2019-05-16 for transfer 3rd begin */
        /*
        if (!"Y".equals(corpHisObj.getAuthCurMop())) {
            corpHisObj.setAuthCurMop(Constants.NO);
        }
        if (!"Y".equals(corpHisObj.getAuthCurHkd())) {
            corpHisObj.setAuthCurHkd(Constants.NO);
        }
        if (!"Y".equals(corpHisObj.getAuthCurUsd())) {
            corpHisObj.setAuthCurUsd(Constants.NO);
        }
        if (!"Y".equals(corpHisObj.getAuthCurEur())) {
            corpHisObj.setAuthCurEur(Constants.NO);
        }
        */
        corpHisObj.setAuthCurMop(Constants.YES);
        corpHisObj.setAuthCurHkd(Constants.NO);
        corpHisObj.setAuthCurUsd(Constants.NO);
        corpHisObj.setAuthCurEur(Constants.NO);
        /* Add by long_zg 2019-05-16 add by long_zg 2019-05-16 for transfer 3rd begin */

        // 锟斤拷示锟斤拷锟�
        Map resultData = new HashMap();
        this.setResultData(resultData);
        this.convertPojo2Map(corpHisObj, resultData);
        resultData.put("rep1IdIssueDate", corpHisObj.getRep1IdIssueDate());
        resultData.put("rep2IdIssueDate", corpHisObj.getRep2IdIssueDate());
        
        //add by wen_chy 20090917
        /*List merchantGroupList=corpService.listMerchantGroup("");
    	if(merchantGroupList==null){
    		merchantGroupList =new ArrayList();
    	}
    	resultData.put("merchantGroupList", merchantGroupList);*/
        
        // 锟斤拷锟矫伙拷锟斤拷锟叫达拷锟絪ession锟斤拷锟皆憋拷confirm锟斤拷写锟斤拷锟斤拷菘锟�
        this.setUsrSessionDataValue("corpHisObj", corpHisObj);
    }

    private Map getCusInfo(String cifNo) throws NTBException {
		  Map toHost = new HashMap();
		  Map fromHost = new HashMap();
		  CibTransClient testClient = new CibTransClient("CIB", "ZB01");
		  toHost.put("CIF_NO",cifNo.substring(1));
		  fromHost = testClient.doTransaction(toHost);
		  if (!testClient.isSucceed()) {
		   throw new NTBHostException(testClient.getErrorArray());
		  }
		  return fromHost;
		 }

	public void addCancel() throws NTBException {
        // 锟秸猴拷锟斤拷乇嗉筹拷锟�
    }
    
    public void addConfirm() throws NTBException {
        // Confirm 锟斤拷写锟斤拷锟斤拷菘锟�
        CorporationHis corpHisObj = (CorporationHis)this
                                    .getUsrSessionDataValue("corpHisObj");
        CorporationService corpService = (CorporationService) Config
                                         .getAppContext().getBean(
                "CorporationService");

        Corporation corporation = corpService.view(corpHisObj.getCorpId());
        if (corporation != null) {
            throw new NTBException("err.bnk.CorpExist");
        }
        if (corpService.loadPendingHis(corpHisObj.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }

        String seqNo = CibIdGenerator.getIdForOperation("CORPORATION_HIS");

        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");
        String processId = flowEngineService.startProcess(
                Constants.TXN_SUBTYPE_CORP_ADD, "0", CorporationAction.class,
                null, 0, null, 0, 0, seqNo, null, getUser(),
                null, null, null);
        try {

            corpHisObj.setSeqNo(seqNo);
            corpHisObj.setOperation(Constants.OPERATION_NEW);
            corpHisObj.setStatus(Constants.STATUS_PENDING_APPROVAL);
            corpHisObj.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
            corpHisObj.setLastUpdateTime(new Date());
            corpHisObj.setRequester(this.getUser().getUserId());
            corpHisObj.setVersion(Integer.valueOf("1"));

            corpService.addHis(corpHisObj);

            Corporation corpObj = new Corporation(corpHisObj.getCorpId());
            BeanUtils.copyProperties(corpObj, corpHisObj);
            corpService.add(corpObj);
        } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error adding new corporation", e);
            throw new NTBException("err.bnk.AddCorpFailure");
        }

        // 锟斤拷示锟斤拷锟斤拷锟斤拷
        Map resultData = this.getResultData();
        resultData.put("seqNo", seqNo);
    }

    public void updateLoad() throws NTBException {
        Corporation corpObj = (Corporation)this
                              .getUsrSessionDataValue("corpObj");

        CorporationService corpService = (CorporationService) Config
                                         .getAppContext().getBean(
                "CorporationService");
        if (corpService.loadPendingHis(corpObj.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }

        //add by wen_chy 2009.09.16
    	/*List merchantGroupList=corpService.listMerchantGroup("");
    	if(merchantGroupList==null){
    		merchantGroupList =new ArrayList();
    	}
    	resultData.put("merchantGroupList", merchantGroupList);
    	*/
    	
        // 锟斤拷示锟斤拷锟�
        Map resultData = new HashMap();       
        this.setResultData(resultData);
        this.convertPojo2Map(corpObj, resultData);
    }

    public void updateCancel() throws NTBException {
        // 锟秸猴拷锟斤拷乇嗉筹拷锟�
    }
    
    public void update() throws NTBException {
        ApproveRuleService approveRuleService = 
        	(ApproveRuleService) Config.getAppContext().getBean("ApproveRuleService");
        
        CorporationService corpService = (CorporationService) Config
        .getAppContext().getBean("CorporationService");
        
    	Map para = this.getParameters();
    	String issueDateString1 = this.getParameter("rep1IdIssueDate");
    	String issueDateString2 = this.getParameter("rep2IdIssueDate");
    	Date rep1IdIssueDate = null;    	
    	if(issueDateString1!=null&&!issueDateString1.equals("")){
    		rep1IdIssueDate = DateTime.getDateFromStr(issueDateString1);
    	}
    	Date rep2IdIssueDate = null;
    	if(issueDateString2!=null&&!issueDateString2.equals("")){
    		rep2IdIssueDate = DateTime.getDateFromStr(issueDateString2);
    	}
    	para.put("rep1IdIssueDate", rep1IdIssueDate);
    	para.put("rep2IdIssueDate", rep2IdIssueDate);
        Corporation corpObj = new Corporation();
        this.convertMap2Pojo(this.getParameters(), corpObj);
        
        // add by wen_chy 20090917
        String merchantGroup=corpObj.getMerchantGroup();
        if(merchantGroup!=null&&!"".equals(merchantGroup)){
        	List merchantGroupList=corpService.listMerchantGroup(merchantGroup.toUpperCase());
        	if(merchantGroupList!=null&&merchantGroupList.size()>0){
        		corpObj.setMerchantGroup(merchantGroup.toUpperCase());
        	}else{
        		throw new NTBException("err.bnk.MerchantGroupNoExist");
        	}
        }
        
        //add by hjs 20070712
        //锟斤拷corp type为2锟斤拷3时锟斤拷锟斤拷锟斤拷欠锟窖★拷锟紸llow Executor
        if(Constants.CORP_TYPE_MIDDLE.equals(corpObj.getCorpType())
        		||Constants.CORP_TYPE_SMALL.equals(corpObj.getCorpType())
        		||Constants.CORP_TYPE_MIDDLE_NO_ADMIN.equals(corpObj.getCorpType())){
            if(Constants.YES.equals(corpObj.getAllowExecutor())){
            	throw new NTBException("err.bnk.NoExecutorAllowed");
            }
            corpObj.setAllowExecutor(Constants.NO);
        }
        para.put("allowExecutor", corpObj.getAllowExecutor());

        // add by Jet 20071108
        // Authentication mode must be e-cert when the corp type is 1
        if (Constants.CORP_TYPE_LARGE.equals(corpObj.getCorpType())){
        	corpObj.setAuthenticationMode(Constants.AUTHENTICATION_CERTIFICATION);
        }        
        para.put("authenticationMode", corpObj.getAuthenticationMode());

        // 锟斤拷式锟斤拷auth cur锟街讹拷
        /*List ccyList = new ArrayList();
        if (!"Y".equals(corpObj.getAuthCurMop())) {
            corpObj.setAuthCurMop(Constants.NO);
            ccyList.add("MOP");
        }
        if (!"Y".equals(corpObj.getAuthCurHkd())) {
            corpObj.setAuthCurHkd(Constants.NO);
            ccyList.add("HKD");
        }
        if (!"Y".equals(corpObj.getAuthCurUsd())) {
            corpObj.setAuthCurUsd(Constants.NO);
            ccyList.add("USD");
        }
        if (!"Y".equals(corpObj.getAuthCurEur())) {
            corpObj.setAuthCurEur(Constants.NO);
            ccyList.add("EUR");
        }
        if(ccyList.size()>0){
            approveRuleService.checkUsability(corpObj.getCorpId(), ccyList.toArray());
        }*/
        corpObj.setAuthCurMop(Constants.YES);
        corpObj.setAuthCurHkd(Constants.NO);
        corpObj.setAuthCurUsd(Constants.NO);
        corpObj.setAuthCurEur(Constants.NO);

        
        //add by wen_chy 2009.09.16
    	/*List merchantGroupList=corpService.listMerchantGroup("");
    	if(merchantGroupList==null){
    		merchantGroupList =new ArrayList();
    	} 
    	para.put("merchantGroupList", merchantGroupList);*/
    	
        // 锟斤拷示锟斤拷锟�
//        Map resultData = new HashMap();
        this.setResultData(para);
//        this.convertPojo2Map(corpObj, resultData);
        // 锟斤拷锟矫伙拷锟斤拷锟叫达拷锟絪ession锟斤拷锟皆憋拷confirm锟斤拷写锟斤拷锟斤拷菘锟�
        this.setUsrSessionDataValue("corpObj", corpObj);
    }

    public void updateConfirm() throws NTBException {
        Corporation corpObj = (Corporation)this .getUsrSessionDataValue("corpObj");
        CorporationService corpService = (CorporationService) Config
                                         .getAppContext().getBean("CorporationService");
        if (corpService.loadPendingHis(corpObj.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }

        CorporationHis corpHisObj = new CorporationHis();
        try {
            BeanUtils.copyProperties(corpHisObj, corpObj);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }
        String[] seqNo = CibIdGenerator.getIdsForOperation("CORPORATION_HIS", 2);

        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean("FlowEngineService");
        String processId = flowEngineService.startProcess(
                Constants.TXN_SUBTYPE_CORP_EDIT, "0", CorporationAction.class,
                null, 0, null, 0, 0, 
                seqNo[1], //hjs
                null, getUser(),
                null, null, null);
        try {
            //hjs before
        	Corporation corpBefore = corpService.view(corpObj.getCorpId());
        	CorporationHis corpHisBefore = new CorporationHis();
            try {
                BeanUtils.copyProperties(corpHisBefore, corpBefore);
            } catch (Exception e) {
                Log.error("Error copy properties", e);
                throw new NTBException("err.sys.GeneralError");
            }
            corpHisBefore.setSeqNo(seqNo[0]);
            corpHisBefore.setOperation(Constants.OPERATION_UPDATE_BEFORE);
            corpHisBefore.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
            corpHisBefore.setAfterModifyId(seqNo[1]);
            corpService.addHis(corpHisBefore);

            corpHisObj.setSeqNo(seqNo[1]);
            corpHisObj.setOperation(Constants.OPERATION_UPDATE);
            corpHisObj.setStatus(Constants.STATUS_PENDING_APPROVAL);
            corpHisObj.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
            corpHisObj.setLastUpdateTime(new Date());
            corpHisObj.setRequester(this.getUser().getUserId());
            corpHisObj.setVersion(new Integer(corpHisObj.getVersion()
                                              .intValue() + 1));

            corpService.addHis(corpHisObj);
        } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error editing new corporation", e);
            throw new NTBException("err.bnk.EditCorpFailure");
        }
        // 锟睫诧拷锟斤拷只锟斤拷示锟斤拷锟斤拷锟斤拷
        Map resultData = this.getResultData();
        resultData.put("seqNo", seqNo);
    }

    public void delete() throws NTBException {

        // 锟斤拷示锟斤拷锟�
        Corporation corpObj = (Corporation)this
                              .getUsrSessionDataValue("corpObj");
        Map resultData = new HashMap();
        this.setResultData(resultData);
        this.convertPojo2Map(corpObj, resultData);

        // 锟斤拷楣咀刺拷锟斤拷欠锟斤拷锟斤拷锟斤拷锟饺拷锟�
        CorporationService corpService = (CorporationService) Config
                                         .getAppContext().getBean(
                "CorporationService");

        if (corpService.loadPendingHis(corpObj.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
        // 锟斤拷楣撅拷锟斤拷锟斤拷状态锟斤拷锟角凤拷锟斤拷锟�锟角凤拷乇眨锟�
        String statusFromHost = corpService.getCorporationStatus(corpObj
                .getCorpId(), this.getUser(),lang);//end
        if (statusFromHost.equals(Corporation.CORP_STATUS_NOTFOUND)) {
            throw new NTBException("err.bnk.CorpNotFound");
        }
        if (statusFromHost.equals(Corporation.CORP_STATUS_CLOSE)) {
            throw new NTBException("err.bnk.CorpClosed");
        }
    }

    public void deleteCancel() throws NTBException {
        // 锟秸猴拷锟斤拷乇嗉筹拷锟�
    }
    
    public void deleteConfirm() throws NTBException {
        Corporation corpObj = (Corporation)this
                              .getUsrSessionDataValue("corpObj");
        CorporationService corpService = (CorporationService) Config
                                         .getAppContext().getBean(
                "CorporationService");

        // 锟斤拷楣咀刺拷锟斤拷欠锟斤拷锟斤拷锟斤拷锟饺拷锟�
        if (corpService.loadPendingHis(corpObj.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }

        CorporationHis corpHisObj = new CorporationHis();
        try {
            BeanUtils.copyProperties(corpHisObj, corpObj);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }
        String seqNo = CibIdGenerator.getIdForOperation("CORPORATION_HIS");

        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");
        String processId = flowEngineService.startProcess(
                Constants.TXN_SUBTYPE_CORP_DELETE, "0",
                CorporationAction.class, null, 0, null, 0, 0, seqNo, null,
                getUser(), null, null, null);
        try {

            corpHisObj.setSeqNo(seqNo);
            corpHisObj.setOperation(Constants.OPERATION_REMOVE);
            corpHisObj.setStatus(Constants.STATUS_PENDING_APPROVAL);
            corpHisObj.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
            corpHisObj.setLastUpdateTime(new Date());
            corpHisObj.setRequester(this.getUser().getUserId());
            corpHisObj.setVersion(corpHisObj.getVersion());

            corpService.addHis(corpHisObj);
        } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error deleting new corporation", e);
            throw new NTBException("err.bnk.DeleteCorpFailure");
        }
    }

    public void block() throws NTBException {
        Corporation corpObj = (Corporation)this
                              .getUsrSessionDataValue("corpObj");
        // 锟斤拷示锟斤拷锟�
        Map resultData = new HashMap();
        this.setResultData(resultData);
        this.convertPojo2Map(corpObj, resultData);

        // 锟斤拷楣咀刺拷锟斤拷欠锟斤拷锟斤拷锟斤拷锟饺拷锟�
        CorporationService corpService = (CorporationService) Config
                                         .getAppContext().getBean(
                "CorporationService");

        if (corpService.loadPendingHis(corpObj.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
    }

    public void blockCancel() throws NTBException {
        // 锟秸猴拷锟斤拷乇嗉筹拷锟�
    }
    
    public void blockConfirm() throws NTBException {
        Corporation corpObj = (Corporation)this
                              .getUsrSessionDataValue("corpObj");
        CorporationService corpService = (CorporationService) Config
                                         .getAppContext().getBean(
                "CorporationService");
        if (corpService.loadPendingHis(corpObj.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }

        CorporationHis corpHisObj = new CorporationHis();
        try {
            BeanUtils.copyProperties(corpHisObj, corpObj);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }
        String seqNo = CibIdGenerator.getIdForOperation("CORPORATION_HIS");

        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");
        String processId = flowEngineService.startProcess(
                Constants.TXN_SUBTYPE_CORP_BLOCK, "0", CorporationAction.class,
                null, 0, null, 0, 0, seqNo, null, getUser(),
                null, null, null);
        try {

            corpHisObj.setSeqNo(seqNo);
            corpHisObj.setOperation(Constants.OPERATION_BLOCK);
            corpHisObj.setStatus(Constants.STATUS_PENDING_APPROVAL);
            corpHisObj.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
            corpHisObj.setLastUpdateTime(new Date());
            corpHisObj.setRequester(this.getUser().getUserId());
            corpHisObj.setVersion(corpHisObj.getVersion());

            corpService.addHis(corpHisObj);
        } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error blocking new corporation", e);
            throw new NTBException("err.bnk.BlockCorpFailure");
        }
    }

    public void unblock() throws NTBException {
        Corporation corpObj = (Corporation)this
                              .getUsrSessionDataValue("corpObj");
        // 锟斤拷示锟斤拷锟�
        Map resultData = new HashMap();
        this.setResultData(resultData);
        this.convertPojo2Map(corpObj, resultData);

        // 锟斤拷楣咀刺拷锟斤拷欠锟斤拷锟斤拷锟斤拷锟饺拷锟�
        CorporationService corpService = (CorporationService) Config
                                         .getAppContext().getBean(
                "CorporationService");

        if (corpService.loadPendingHis(corpObj.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
    }

    public void unblockCancel() throws NTBException {
        // 锟秸猴拷锟斤拷乇嗉筹拷锟�
    }
    
    public void unblockConfirm() throws NTBException {
        Corporation corpObj = (Corporation)this
                              .getUsrSessionDataValue("corpObj");
        CorporationService corpService = (CorporationService) Config
                                         .getAppContext().getBean(
                "CorporationService");

        if (corpService.loadPendingHis(corpObj.getCorpId()) != null) {
            throw new NTBException("err.bnk.OperationPending");
        }
        if (!corpObj.getStatus().equals(Constants.STATUS_BLOCKED)) {
            throw new NTBException("err.bnk.CorpNotBlocked");
        }

        CorporationHis corpHisObj = new CorporationHis();
        try {
            BeanUtils.copyProperties(corpHisObj, corpObj);
        } catch (Exception e) {
            Log.error("Error copy properties", e);
            throw new NTBException("err.sys.GeneralError");
        }
        String seqNo = CibIdGenerator.getIdForOperation("CORPORATION_HIS");

        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");
        String processId = flowEngineService.startProcess(
                Constants.TXN_SUBTYPE_CORP_UNBLOCK, "0",
                CorporationAction.class, null, 0, null, 0, 0, seqNo, null,
                getUser(), null, null, null);
        try {

            corpHisObj.setSeqNo(seqNo);
            corpHisObj.setOperation(Constants.OPERATION_UNBLOCK);
            corpHisObj.setStatus(Constants.STATUS_PENDING_APPROVAL);
            corpHisObj.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
            corpHisObj.setLastUpdateTime(new Date());
            corpHisObj.setRequester(this.getUser().getUserId());
            corpHisObj.setVersion(corpHisObj.getVersion());

            corpService.addHis(corpHisObj);
        } catch (Exception e) {
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error unblocking new corporation", e);
            throw new NTBException("err.bnk.UnblockCorpFailure");
        }
    }

    public void view() throws NTBException {
        Corporation corpObj = new Corporation();
        this.convertMap2Pojo(this.getParameters(), corpObj);
        CorporationService corpService = (CorporationService) Config
                                         .getAppContext().getBean(
                "CorporationService");
        Corporation viewObj = corpService.view(corpObj.getCorpId());

        Map resultData = new HashMap();
        this.setResultData(resultData);
        this.convertPojo2Map(viewObj, resultData);

        // 锟斤拷锟矫伙拷锟斤拷锟叫达拷锟絪ession锟斤拷锟皆憋拷modify/delete/block锟斤拷示锟斤拷息
        this.setUsrSessionDataValue("corpObj", viewObj);
    }

    public void list() throws NTBException {
        Corporation corpObj = new Corporation();
        String corpId = this.getParameter("queryCorpId");
        String corpName = this.getParameter("queryCorpName");
        corpObj.setCorpId(corpId);
        corpObj.setCorpName(corpName);

        CorporationService corpService = (CorporationService) Config.getAppContext().getBean("CorporationService");

        List corpList = corpService.list(corpObj);
        corpList = this.convertPojoList2MapList(corpList);
        
        // added by Jet for role viewer 2008-04-17
        if ( this.getUser() instanceof BankUser){
            if (((BankUser)this.getUser()).getRoleId().equals(Constants.ROLE_BANK_VIEWER)){
                ViewerAccessListService ViewerAccessListService = (ViewerAccessListService)Config.getAppContext().getBean("ViewerAccessListService");
                List viewerCorpList = ViewerAccessListService.listSelectedCorpListByViewer(((BankUser)this.getUser()).getUserId());

                List corpListTemp = new ArrayList();
                for (int i = 0; i < viewerCorpList.size(); i ++){
                	String corpIdTemp = ((Map) viewerCorpList.get(i)).get("CORP_ID").toString();
                	for (int j = 0; j < corpList.size(); j++){
                		if (corpIdTemp.equals(((Map) corpList.get(j)).get("corpId").toString())){
                			corpListTemp.add(corpList.get(j));
                		}
                	}
                }
                corpList.clear();
                corpList.addAll(corpListTemp);
            }
        }
        
        Map resultData = new HashMap();
        resultData.put("queryCorpId", corpId);
        resultData.put("queryCorpName", corpName);
        resultData.put("corpList", corpList);
        setResultData(resultData);

    }

    public void prefEntry() throws NTBException {
        String prefType = this.getParameter("prefType");
        list();
        Map resultData = this.getResultData();
        resultData.put("prefType", prefType);
    }

    public void listCorpForTree() throws NTBException {

        String showRoot = this.getParameter("showRoot");
        String selectCorpId = this.getParameter("selectCorpId");

        RcCorporation rcCorp = new RcCorporation(RcCorporation.SHOW_CORP_WITHOUT_ROOT);

        CorpUser user = (CorpUser) getUser();
        String corpId = user.getCorpId();

        rcCorp.setArgs(user);
        List corpList = new ArrayList();
        List keyList = rcCorp.getKeys();
        for (int i = 0; i < keyList.size(); i++) {
            String key = (String) keyList.get(i);
            if (!key.equals(corpId)) {
                corpList.add(rcCorp.getObject(key));
            }
        }

        Map resultData = (Map) rcCorp.getObject(corpId);
        if("Y".equals(showRoot)){
            resultData.put("showRoot", showRoot);
        }else{
        	resultData.put("showRoot", "N");
        }
        resultData.put("corpList", corpList);
        resultData.put("selectCorpId", selectCorpId);
        this.setResultData(resultData);
    }

    public boolean approve(String txnType, String id, CibAction bean) throws
            NTBException {
        CorporationService corpService = (CorporationService) Config
                                         .getAppContext().getBean("CorporationService");
        ApproveRuleService approveRuleService = (ApproveRuleService) Config.getAppContext().getBean("ApproveRuleService");
        CorporationHis corpHis = corpService.loadHisBySeqNo(id);
        //add by linrui for test 20190604
        if(Constants.TXN_SUBTYPE_CORP_EDIT.equals(txnType)){
        	Log.info("*********Txn type is " + Constants.TXN_SUBTYPE_CORP_EDIT + " and id is "+ id);
        }
        //end 20190604
        if (txnType.equals(Constants.TXN_SUBTYPE_CORP_ADD)) {
            corpHis.setStatus(Constants.STATUS_NORMAL);
            corpHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            corpHis.setLastUpdateTime(new Date());
            corpService.updateHis(corpHis);
            // add corp
            Corporation corpObj = corpService.view(corpHis.getCorpId());
            corpObj.setStatus(Constants.STATUS_NORMAL);
            corpObj.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            corpService.update(corpObj);
            
            //add by hjs 20071024
            //init approver rule if corp type is middle or small
            if(Constants.CORP_TYPE_MIDDLE.equals(corpHis.getCorpType())
            		|| Constants.CORP_TYPE_SMALL.equals(corpHis.getCorpType())){
            	List ccyList = new ArrayList();
            	/*if("Y".equals(corpHis.getAuthCurMop())){
                    ccyList.add("MOP");
            	}
            	if("Y".equals(corpHis.getAuthCurHkd())){
                    ccyList.add("HKD");
            	}
            	if("Y".equals(corpHis.getAuthCurUsd())){
                    ccyList.add("USD");
            	}
            	if("Y".equals(corpHis.getAuthCurEur())){
                    ccyList.add("EUR");
            	}
            	if(ccyList.size()>0){
            		approveRuleService.initApproveRule(bean.getUser().getUserId(),
            				corpHis.getCorpId(), ccyList.toArray());
            	}*/
            	ccyList.add("MOP");
            	approveRuleService.initApproveRule(bean.getUser().getUserId(),
        				corpHis.getCorpId(), ccyList.toArray());
            }
        } else {
            if (txnType.equals(Constants.TXN_SUBTYPE_CORP_EDIT)) {
            	Log.info("*********Txn type is " + Constants.TXN_SUBTYPE_CORP_EDIT + " and id is "+ id);
            	//check auth ccy
            	/*List ccyList = new ArrayList();
            	if("N".equals(corpHis.getAuthCurMop())){
                    ccyList.add("MOP");
            	}
            	if("N".equals(corpHis.getAuthCurHkd())){
                    ccyList.add("HKD");
            	}
            	if("N".equals(corpHis.getAuthCurUsd())){
                    ccyList.add("USD");
            	}
            	if("N".equals(corpHis.getAuthCurEur())){
                    ccyList.add("EUR");
            	}
            	if(ccyList.size()>0){
            		Log.info("*********Txn type is " + Constants.TXN_SUBTYPE_CORP_EDIT + " ccyList.size()>0 ");
            		approveRuleService.checkUsability(corpHis.getCorpId(), ccyList.toArray());
            		approveRuleService.removeApproveRule(corpHis.getCorpId(), ccyList.toArray());
            	}*/
            	
            	
                corpHis.setStatus(Constants.STATUS_NORMAL);
                corpHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
                //hjs before
                List beforeList = corpService.listCorpHisByAfterId(id);
                if(beforeList!=null && beforeList.size()>0){
                	//add by linrui 20190604
                	Log.info("*********Txn type is " + Constants.TXN_SUBTYPE_CORP_EDIT + " corpService.listCorpHisByAfterId list is "
                    		+ beforeList.get(0));
                	//end
                	//Update by heyj 20190528
                	//CorporationHis corpHisBefore = (CorporationHis) beforeList.get(0);
                	Map corpHisBeforeMap = (Map)beforeList.get(0);
                	//add by linrui 20190604
                	Log.info("*********Txn type is " + Constants.TXN_SUBTYPE_CORP_EDIT + " corpHisBeforeMap.get SEQ_NO list is "
                    		+ Utils.null2EmptyWithTrim(corpHisBeforeMap.get("SEQ_NO")));
                	//end
                	String seqNo = Utils.null2EmptyWithTrim(corpHisBeforeMap.get("SEQ_NO"));
                	CorporationHis corpHisBefore = corpService.loadHisBySeqNo(seqNo);
                	corpHisBefore.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
                    corpHisBefore.setLastUpdateTime(new Date());
                    corpService.updateHis(corpHisBefore);
                    //end heyj 0190528
                }
            } else if (txnType.equals(Constants.TXN_SUBTYPE_CORP_BLOCK)) {
                corpHis.setStatus(Constants.STATUS_BLOCKED);
                corpHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            } else if (txnType.equals(Constants.TXN_SUBTYPE_CORP_UNBLOCK)) {
                corpHis.setStatus(Constants.STATUS_NORMAL);
                corpHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            } else if (txnType.equals(Constants.TXN_SUBTYPE_CORP_DELETE)) {
                corpHis.setStatus(Constants.STATUS_REMOVED);
                corpHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);

                //add by hjs 20070206
                ApplicationContext appContext = Config.getAppContext();
                SchTransferService schTransService = (SchTransferService) appContext.getBean("SchTransferService");
                SchTxnBatchService schTxnBatchService = (SchTxnBatchService) appContext.getBean("SchTxnBatchService");
                schTransService.removeByCorpId(corpHis.getCorpId());
                schTxnBatchService.cancelByCorpId(corpHis.getCorpId());
            }
            corpHis.setLastUpdateTime(new Date());
            corpService.updateHis(corpHis);
            // update corp
            Corporation corpObj = corpService.view(corpHis.getCorpId());
            try {
                BeanUtils.copyProperties(corpObj, corpHis);
            } catch (Exception e) {
                Log.error("Error copy properties", e);
                throw new NTBException("err.bnk.GeneralError");
            }
            corpService.update(corpObj);
        }
        // 锟斤拷锟斤拷corporation rc
        CachedDBRCFactory.addPendingCache("corpInTree");
        return true;
    }

    public boolean reject(String txnType, String id, CibAction bean) throws
            NTBException {
        CorporationService corpService = (CorporationService) Config
                                         .getAppContext().getBean(
                "CorporationService");
        CorporationHis corpHis = corpService.loadHisBySeqNo(id);
        corpHis.setStatus(Constants.STATUS_REMOVED);
        corpHis.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
        corpHis.setLastUpdateTime(new Date());
        corpService.updateHis(corpHis);
        //hjs before
        List beforeList = corpService.listCorpHisByAfterId(id);
        if(beforeList!=null && beforeList.size()>0){
            //Update by heyj 20190528
        	//CorporationHis corpHisBefore = (CorporationHis) beforeList.get(0);
            Map corpHisBeforeMap = (Map)beforeList.get(0);
        	String seqNo = Utils.null2EmptyWithTrim(corpHisBeforeMap.get("SEQ_NO"));   //Update by heyj 20190528
        	CorporationHis corpHisBefore = corpService.loadHisBySeqNo(seqNo);          //Update by heyj 20190528
            corpHisBefore.setLastUpdateTime(new Date());
            corpHisBefore.setStatus(Constants.STATUS_REMOVED);
            corpHisBefore.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
            corpService.updateHis(corpHisBefore);
        }
        
        Corporation corp = new Corporation();
        if (txnType.equals(Constants.TXN_SUBTYPE_CORP_ADD)) {
            try {
                BeanUtils.copyProperties(corp, corpHis);
            } catch (Exception e) {
                Log.error("Error copy properties", e);
                throw new NTBException("err.sys.GeneralError");
            }
            corpService.remove(corp);
        }

        return true;
    }

    public String viewDetail(String txnType, String id, CibAction bean) throws
            NTBException {
        CorporationService corpService = (CorporationService) Config
                                         .getAppContext().getBean(
                "CorporationService");
        CorporationHis corpHis = corpService.loadHisBySeqNo(id);
        Map resultData = bean.getResultData();
        this.convertPojo2Map(corpHis, resultData);
        bean.setResultData(resultData);
        return "/WEB-INF/pages/bank/corp_mng/corporation_view_detail.jsp";
    }


    public boolean cancel(String txnType, String id, CibAction bean) throws
            NTBException {
        return reject(txnType, id, bean);
    }

}
