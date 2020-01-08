/**
 * @author hjs
 * 2006-8-3
 */
package app.cib.action.bnk;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

import app.cib.core.*;
import app.cib.util.*;
import app.cib.service.bnk.ViewerAccessListService;
import app.cib.service.flow.FlowEngineService;
import com.neturbo.set.core.Log;

/**
 * @author Jet
 * 2008-4-16
 */
public class ViewerAccessListAction extends CibAction implements Approvable {

    public void listLoad() throws NTBException {
        //设置空的 ResultData 清空显示数据
        setResultData(new HashMap(1));

        // show all the viewers
        ApplicationContext appContext = Config.getAppContext();
        ViewerAccessListService viewerAccessListService = (ViewerAccessListService)appContext.getBean("ViewerAccessListService");
        List viewerList = viewerAccessListService.listViewer();
        
        //显示原设置内容
        Map resultData = new HashMap();
        resultData.put("viewerList", viewerList);

        setResultData(resultData);
        
        this.setUsrSessionDataValue("viewerList", viewerList);
    }

    public void listCorp() throws NTBException {
        //获得 viewerId
        String viewerId = Utils.null2EmptyWithTrim(this.getParameter("viewerId"));
        List viewerList = (List) this.getUsrSessionDataValue("viewerList");
        
        String viewerName = "";
        for (int i =0; i < viewerList.size(); i ++){
        	if(viewerId.equals(((Map) viewerList.get(i)).get("USER_ID").toString())){
            	viewerName = ((Map) viewerList.get(i)).get("USER_NAME").toString();
        	}
        }
            	
        // show corp list
    	ApplicationContext appContext = Config.getAppContext();
        ViewerAccessListService viewerAccessListService = (ViewerAccessListService)appContext.getBean("ViewerAccessListService");
        
        List selectedCorpList = viewerAccessListService.listSelectedCorpListByViewer(viewerId);
        List candidateCorpList = viewerAccessListService.listCandidateCorp(selectedCorpList);
        
        //显示原设置内容        
        Map resultData = new HashMap();
        resultData.put("viewerId", viewerId);
        resultData.put("viewerName", viewerName);
        resultData.put("viewerList", viewerList);
        resultData.put("selectedCorpList", selectedCorpList);
        resultData.put("candidateCorpList", candidateCorpList);
        
        setResultData(resultData);
        
        this.setUsrSessionDataValue("viewerId", viewerId);
        this.setUsrSessionDataValue("viewerName", viewerName);
        this.setUsrSessionDataValue("selectedCorpList", selectedCorpList);        
    }
    
    public void updateAccessListReturn() throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();
        ViewerAccessListService viewerAccessListService = (ViewerAccessListService)appContext.getBean("ViewerAccessListService");
        
    	String viewerId = (String) getUsrSessionDataValue("viewerId");
    	List viewerList = (List) getUsrSessionDataValue("viewerList");
    	List selectedCorpList = new ArrayList((List) getUsrSessionDataValue("newAccessList"));
        List candidateCorpList = viewerAccessListService.listCandidateCorp(selectedCorpList);
        
        Map resultData = new HashMap();
        resultData.put("viewerId", viewerId);
        resultData.put("viewerList", viewerList);
        resultData.put("selectedCorpList", selectedCorpList);
        resultData.put("candidateCorpList", candidateCorpList);
        
        setResultData(resultData);
    }

    public void updateAccessList() throws NTBException {
        String viewerId = (String) this.getUsrSessionDataValue("viewerId");
        String viewerName = (String) this.getUsrSessionDataValue("viewerName");
        List selectedCorpList = (List) this.getUsrSessionDataValue("selectedCorpList");
        String[] selectedArray = this.getParameterValues("associatedList");
        
        if(selectedArray == null){
        	selectedArray = new String[]{};
        }
        
    	ApplicationContext appContext = Config.getAppContext();
        ViewerAccessListService viewerAccessListService = (ViewerAccessListService)appContext.getBean("ViewerAccessListService");

        // pending checking
        if (viewerAccessListService.isPending(viewerId)){
            throw new NTBException("err.bnk.LastTransNotCompleted");
        }
        
        List allCorpList = viewerAccessListService.listAllCorp();
        List newAccessList = new ArrayList();
        
        Map row = null;
        for(int i = 0; i < selectedArray.length; i ++){
        	String corpId = selectedArray[i];
        	for (int j = 0; j < allCorpList.size(); j ++){
            	row = (Map)allCorpList.get(j);
        		if (corpId.equals(row.get("CORP_ID"))){
        			newAccessList.add(allCorpList.get(j));
        		}
        	}
        }
        Map resultDataReturn = this.getResultData();
        resultDataReturn.put("selectedCorpList", newAccessList);

        // if no change, throw exectption
        if (CollectionUtils.isEqualCollection(selectedCorpList, newAccessList)){
            throw new NTBException("err.bnk.AccessListNotChanged");
        }
                        
        //显示原设置内容        
        Map resultData = new HashMap();
        resultData.put("viewerId", viewerId);
        resultData.put("viewerName", viewerName);
        resultData.put("newAccessList", newAccessList);
                
        setResultData(resultData);

        this.setUsrSessionDataValue("viewerId", viewerId);
        this.setUsrSessionDataValue("newAccessList", newAccessList);        
    }
    
    public void updateAccessListCfm() throws NTBException {
        String viewerId = (String) this.getUsrSessionDataValue("viewerId");
        String viewerName = (String) this.getUsrSessionDataValue("viewerName");
        List newAccessList = (List) this.getUsrSessionDataValue("newAccessList");

    	ApplicationContext appContext = Config.getAppContext();
        ViewerAccessListService viewerAccessListService = (ViewerAccessListService)appContext.getBean("ViewerAccessListService");
                
        String batchId = CibIdGenerator.getIdForOperation("VIEWER_ACCESS_LIST");
        String batchIdBefore = viewerAccessListService.getBatchIdByViewer(viewerId);
        String operation = Constants.OPERATION_NEW;
        if (batchIdBefore != null && !batchIdBefore.equals("")){
        	operation = Constants.OPERATION_UPDATE;
        }
        List newSelectedList = new ArrayList();
        for (int i=0; i < newAccessList.size(); i ++){
        	Map record = new HashMap();
        	
        	record.put("BATCH_ID", batchId);
        	record.put("BATCH_ID_BEFORE", batchIdBefore);
        	record.put("USER_ID", viewerId);
        	record.put("CORP_ID", ((Map)newAccessList.get(i)).get("CORP_ID"));
        	record.put("OPERATION", operation);
        	record.put("STATUS", Constants.STATUS_PENDING_APPROVAL);
        	record.put("AUTH_STATUS", Constants.AUTH_STATUS_SUBMITED);
        	record.put("REQUESTER", this.getUser().getUserId());
//        	record.put("LAST_UPDATE_TIME", new Date());
        	
        	newSelectedList.add(record);
        }
        
        //启动工作流
        FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");
        String processId = flowEngineService.startProcess(Constants.TXN_SUBTYPE_VIEWER_ACCESS_LIST,
                "0",this.getClass(), null, 0, null, 0, 0, batchId, null, getUser(), null,null,null);

        try {
            //写数据库
        	viewerAccessListService.insertForApprove(newSelectedList);
        } catch (Exception e) {
            //如果出现错误，中止工作流
            flowEngineService.cancelProcess(processId, getUser());
            Log.error("Error processing confirmation operation of VIEWER ACCESS LIST", e);
            throw new NTBException(ErrConstants.GENERAL_ERROR);
        }
        
        //显示原设置内容        
        Map resultData = new HashMap();
        resultData.put("viewerId", viewerId);
        resultData.put("viewerName", viewerName);
        resultData.put("newAccessList", newAccessList);
                
        setResultData(resultData);
    }
    
    public boolean approve(String txnType, String id, CibAction bean) throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();
        ViewerAccessListService viewerAccessListService = (ViewerAccessListService)appContext.getBean("ViewerAccessListService");
        
        viewerAccessListService.updateForApprove(id);

        return true;
    }
        
    public boolean reject(String txnType, String id, CibAction bean) throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();
        ViewerAccessListService viewerAccessListService = (ViewerAccessListService)appContext.getBean("ViewerAccessListService");

        viewerAccessListService.updateForReject(id);
		return true;
	}
    
    public boolean cancel(String txnType, String id, CibAction bean) throws NTBException {
		return reject(txnType, id, bean);
	}
    
    public String viewDetail(String txnType, String id, CibAction bean) throws NTBException {

    	ApplicationContext appContext = Config.getAppContext();
        ViewerAccessListService viewerAccessListService = (ViewerAccessListService)appContext.getBean("ViewerAccessListService");

        List viewerList = viewerAccessListService.getViewerByBatchId(id);
        List selectedCorpList = viewerAccessListService.listSelectedCorpListByBatchId(id);
        
        String viewerId = "";
        String viewerName = ""; 
        if(viewerList.size() > 0){
        	viewerId = ((Map)viewerList.get(0)).get("USER_ID").toString();
        	viewerName = ((Map)viewerList.get(0)).get("USER_NAME").toString();
        }

		// 查出原设置内容
		Map resultData = bean.getResultData();
        resultData.put("selectedCorpList", selectedCorpList);
		resultData.put("viewerId", viewerId);
		resultData.put("viewerName", viewerName);
		
		bean.setResultData(resultData);
		return "/WEB-INF/pages/bank/viewer_access_list/viewer_access_list_approval_view.jsp";
	}    
}
