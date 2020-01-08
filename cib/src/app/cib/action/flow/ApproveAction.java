package app.cib.action.flow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.*;

import app.cib.bo.bnk.BankUser;
import app.cib.bo.bnk.Corporation;
import app.cib.bo.flow.FlowProcess;
import app.cib.bo.flow.FlowWork;
import app.cib.bo.flow.WorkObject;
import app.cib.bo.sys.AbstractCorpUser;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TxnSignData;
import app.cib.cert.server.CertProcessor;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.cription.AESUtil;
import app.cib.service.bnk.CorporationService;
import app.cib.service.flow.FlowEngineService;
import app.cib.util.Constants;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;

import app.cib.util.PageActionHandler;
import app.cib.util.otp.SMSOTPObject;
import app.cib.util.otp.SMSOTPUtil;
import app.cib.util.otp.SMSReturnObject;

import com.neturbo.set.core.NTBAction;
import com.neturbo.base.action.ActionForward;

public class ApproveAction extends CibAction implements PageActionHandler,Runnable {

	private static final String defalutPattern = Config.getProperty("DefaultDatePattern");

    public void list() throws NTBException {
        HashMap resultData = new HashMap();
        NTBUser user = getUser();
        String executeFlag = "N";

        if (user instanceof AbstractCorpUser
            && (Constants.ROLE_APPROVER.equals(((AbstractCorpUser) user)
                                               .getRoleId()) ||
                Constants.ROLE_EXECUTOR
                .equals(((AbstractCorpUser) user).getRoleId()))) {
            resultData.put("FinanceFlag", "Y");

            if (Constants.ROLE_EXECUTOR.equals(((AbstractCorpUser) user)
                                               .getRoleId())) {
                executeFlag = "Y";
            }

        } else {
            resultData.put("FinanceFlag", "N");
        }
        resultData.put("ExecuteFlag", executeFlag);

        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");
//        List works = flowEngineService.listWorkChecked(user);
        String sortOrder = Utils.null2EmptyWithTrim(this.getParameter("sortOrder"));
        if("".equals(sortOrder)) sortOrder = "1";
        List works = flowEngineService.listWorkChecked(user,sortOrder);

        //hjs 20070308
		String date_range = Utils.null2EmptyWithTrim(this.getParameter("date_range"));
		String dateFrom = Utils.null2EmptyWithTrim(this.getParameter("dateFrom"));
		String dateTo = Utils.null2EmptyWithTrim(this.getParameter("dateTo"));
		String transId = Utils.null2EmptyWithTrim(this.getParameter("transId"));
        if (null != works) {
        	if(!dateFrom.equals("") || !dateTo.equals("") || !transId.equals("")) {
        		Calendar cal = Calendar.getInstance();
        		Date tmpDateFrom = null;
        		Date tmpDateTo = null;
        		if(dateFrom.equals("")){
        			cal.add(Calendar.YEAR, -50);
        			tmpDateFrom = cal.getTime();
        		} else {
        			tmpDateFrom = DateTime.getDateFromStr(dateFrom, defalutPattern);
        		}
        		if(dateTo.equals("")){
        			cal.setTime(new Date());
        			cal.add(Calendar.YEAR, 50);
        			tmpDateTo = cal.getTime();
        		} else {
        			tmpDateTo = DateTime.getDateFromStr(dateTo, defalutPattern);
        		}
        		Date procCreateTime = null;
        		List newWorkList = new ArrayList();
        		for(int i=0; i<works.size(); i++) {
        			Map row = (Map) works.get(i);
        			procCreateTime = DateTime.getDateFromStr(row.get("procCreateTime").toString().split(" ")[0],
        					"yyyy-MM-dd");
        			if(DateTime.compareDate(procCreateTime, tmpDateFrom)>=0
        					&& DateTime.compareDate(procCreateTime, tmpDateTo)<=0){
        				if(!transId.equals("")){
        					if(transId.equals(row.get("transNo"))) {
    							newWorkList.add(row);
        					}
        				} else {
            				newWorkList.add(row);
        				}
        			}
        		}
                resultData.put("workList", newWorkList);
        	} else {
                resultData.put("workList", works);
        	}
        }
		resultData.put("date_range", date_range);
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("transId", transId);
		resultData.put("sortOrder", sortOrder);
        setResultData(resultData);
    }

    public void returnList() throws NTBException {
        //閿熸枻鎷烽敓鏂ゆ嫹view椤甸敓鏂ゆ嫹閿熺单ext 閿熸枻鎷穜eturn 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鍙鎷烽敓瑙掑嚖鎷烽敓鏂ゆ嫹瑕侀敓鏂ゆ嫹閿熸枻鎷峰墠view 閿熸枻鎷穡ork 閿熸枻鎷烽敓鏂ゆ嫹閿熺氮elected work閿熷彨鎲嬫嫹
        addSelectedWorkFromView();
    }

    private void addSelectedWorkFromView() {
        //閿熸枻鎷烽敓鏂ゆ嫹view椤甸敓鏂ゆ嫹閿熺单ext 閿熸枻鎷穜eturn 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鍙鎷烽敓瑙掑嚖鎷烽敓鏂ゆ嫹瑕侀敓鏂ゆ嫹閿熸枻鎷峰墠view 閿熸枻鎷穡ork 閿熸枻鎷烽敓鏂ゆ嫹閿熺氮elected work閿熷彨鎲嬫嫹
        Map selectedWorkIdMap = (Map) getResultData().get("workIdMap");
        if (selectedWorkIdMap == null) {
            selectedWorkIdMap = new HashMap();
        }
        String addToMultiStr = getParameter("addToMulti");
        String selectedWorkId = getParameter("selectedWorkId");
        boolean addToMultiFlag = "YES".equals(addToMultiStr);
        if (selectedWorkId != null) {
            if (addToMultiFlag) {
                selectedWorkIdMap.put(selectedWorkId, "1");
            } else {
                if (selectedWorkIdMap.containsKey(selectedWorkId)) {
                    selectedWorkIdMap.remove(selectedWorkId);
                }
            }

            String[] newWorkIds = new String[selectedWorkIdMap.size()];
            int index = 0;
            Iterator ids = selectedWorkIdMap.keySet().iterator();
            while (ids.hasNext()) {
                String id = (String) ids.next();
                newWorkIds[index++] = id;
            }

            getResultData().put("workIdMap", selectedWorkIdMap);
            getResultData().put("workIds", newWorkIds);
        }
    }

    private void addSelectedWorkFromList() {
        //閿熸枻鎷烽敓鏂ゆ嫹list椤甸敓鏂ゆ嫹閿熺淡iew 閿熸枻鎷�multiapprove 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鍙鎷烽敓瑙掑嚖鎷烽敓鏂ゆ嫹瑕侀敓鏂ゆ嫹閿熸枻鎷峰墠check 閿熸枻鎷穡ork 閿熸枻鎷烽敓鏂ゆ嫹閿熺氮elected work閿熷彨鎲嬫嫹
        Map selectedWorkIdMap = (Map) getResultData().get("workIdMap");
        if (selectedWorkIdMap == null) {
            selectedWorkIdMap = new HashMap();
        }

        String[] listWorkIds = getParameterValues("listIds");
        String[] newSelectedWorkIds = getParameterValues("workIds");
        if (listWorkIds != null) {
            for (int i = 0; i < listWorkIds.length; i++) {
                if (selectedWorkIdMap.containsKey(listWorkIds[i])) {
                    selectedWorkIdMap.remove(listWorkIds[i]);
                }
            }
        }
        if (newSelectedWorkIds != null) {
            for (int i = 0; i < newSelectedWorkIds.length; i++) {
                selectedWorkIdMap.put(newSelectedWorkIds[i], "1");
            }
        }

        String[] newWorkIds = new String[selectedWorkIdMap.size()];
        int index = 0;
        Iterator ids = selectedWorkIdMap.keySet().iterator();
        while (ids.hasNext()) {
            String id = (String) ids.next();
            newWorkIds[index++] = id;
        }

        getResultData().put("workIdMap", selectedWorkIdMap);
        getResultData().put("workIds", newWorkIds);
    }

    public void view() throws NTBException {
        NTBUser user = getUser();

        //閿熸枻鎷风帿閿熻鐨恛rk
        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");
        String workId = getParameter("workId");
        
        if (workId==null || workId.equals("")){
        	workId = (String) this.getResultData().get("workId");
        }
        FlowWork work = flowEngineService.viewWork(workId, user);
        //閿熸枻鎷烽敓鏂ゆ嫹view椤甸敓鏂ゆ嫹閿熺单ext 閿熸枻鎷穜eturn 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鍙鎷烽敓瑙掑嚖鎷烽敓鏂ゆ嫹瑕侀敓鏂ゆ嫹閿熸枻鎷峰墠view 閿熸枻鎷穡ork 閿熸枻鎷烽敓鏂ゆ嫹閿熺氮elected work閿熷彨鎲嬫嫹
        addSelectedWorkFromView();
        //閿熸枻鎷烽敓鏂ゆ嫹list椤甸敓鏂ゆ嫹閿熺淡iew 閿熸枻鎷�multiapprove 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鍙鎷烽敓瑙掑嚖鎷烽敓鏂ゆ嫹瑕侀敓鏂ゆ嫹閿熸枻鎷峰墠check 閿熸枻鎷穡ork 閿熸枻鎷烽敓鏂ゆ嫹閿熺氮elected work閿熷彨鎲嬫嫹
        addSelectedWorkFromList();

        //閿熸枻鎷烽敓鏂ゆ嫹璧傞敓绲痮rk
        List totalWorks = (List) getResultData().get("workList");
        if(totalWorks != null){
            int indexNow = 0;
            for (indexNow = 0; indexNow < totalWorks.size(); indexNow++) {
                Map item = (Map) totalWorks.get(indexNow);
                if (item.get("workId").equals(workId)) {
                    break;
                }
            }
            String hasNextStr = "YES";
            if (indexNow < totalWorks.size() - 1) {
                Map nextWork = (Map) totalWorks.get(indexNow + 1);
                getResultData().put("nextWorkId", nextWork.get("workId"));
            } else {
                hasNextStr = "NO";
            }
            getResultData().put("hasNext", hasNextStr);
        }

        //閿熸枻鎷烽敓鏂ゆ嫹娆犻敓绐栤槄鎷烽敓锟�
        Map selectedWorkIdMap = (Map) getResultData().get("workIdMap");
        if (selectedWorkIdMap != null) {
            String addToMulti = "NO";
            if (selectedWorkIdMap.containsKey(workId)) {
                addToMulti = "YES";
            }
            getResultData().put("addToMulti", addToMulti);
        }

        try {
            Approvable approver = (Approvable) (Class.forName(work
                    .getFlowProcess().getTxnBean()).newInstance());
            String url = approver.viewDetail(
                    work.getFlowProcess().getTxnType(), work.getFlowProcess()
                    .getTransNo(), this);
            if (null == url || "".equals(url)) {
                throw new NTBException("err.flow.ViewDetail");
            }
            getResultData().put("detailPageUrl", url);

        } catch (Exception e) {
            Log.error("Process error at ApproveAction.view(): ", e);
            if (e instanceof NTBException) {
                throw (NTBException) e;
            } else {
                throw new NTBException("err.flow.ViewDetail");
            }
        }

        getResultData().put("workList", totalWorks);
        getResultData().put("workId", workId);
        getResultData().put("procId", work.getFlowProcess().getProcId());
        getResultData().put("txnType", work.getFlowProcess().getTxnType());
        getResultData().put("procCreateTime",
                            work.getFlowProcess().getProcCreateTime());
        getResultData()
                .put("procStatus", work.getFlowProcess().getProcStatus());
        getResultData().put(
                "progressList",
                flowEngineService.getProgress(
                        work.getFlowProcess().getProcId(), user));

        List procWorkList = flowEngineService.listWorkByProcDealed(work
                .getFlowProcess().getProcId(), getUser());
        if (null != procWorkList) {
            getResultData().put("procWorkList", procWorkList);
        }

        if (user instanceof AbstractCorpUser
            && (Constants.ROLE_OPERATOR.equals(((AbstractCorpUser) user)
                                               .getRoleId())
                || Constants.ROLE_APPROVER
                .equals(((AbstractCorpUser) user).getRoleId()) ||
                Constants.ROLE_EXECUTOR
                .equals(((AbstractCorpUser) user).getRoleId()))) {
            getResultData().put("FinanceFlag", "Y");

        } else {
            getResultData().put("FinanceFlag", "N");
        }
    }

    public void statusEnquiryView() throws NTBException {
        viewHistory();
        getResultData().put("returnPage", Utils.null2EmptyWithTrim(this.getParameter("returnPage")));
        getResultData().put("actionMethodReturn", "statusEnquiry");
    }

    public void historyEnquiryView() throws NTBException {
        String fromDate = getParameter("dateFrom");
        String toDate = getParameter("dateTo");
        viewHistory();
        getResultData().put("dateFrom", fromDate);
        getResultData().put("dateTo", toDate);
        getResultData().put("actionMethodReturn", "historyEnquiry");
    }


    public void viewHistory() throws NTBException {
        NTBUser user = getUser();

        //閿熸枻鎷风帿閿熻鐨恛rk
        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");
        String workId = getParameter("workId");
        FlowWork work = flowEngineService.viewWork(workId, user);

        try {
            Approvable approver = (Approvable) (Class.forName(work
                    .getFlowProcess().getTxnBean()).newInstance());
            String url = approver.viewDetail(
                    work.getFlowProcess().getTxnType(), work.getFlowProcess()
                    .getTransNo(), this);
            if (null == url || "".equals(url)) {
                throw new NTBException("err.flow.ViewDetail");
            }
            getResultData().put("detailPageUrl", url);

        } catch (Exception e) {
            Log.error("Process error at ApproveAction.view(): ", e);
            if (e instanceof NTBException) {
                throw (NTBException) e;
            } else {
                throw new NTBException("err.flow.ViewDetail");
            }
        }

        getResultData().put("workId", workId);
        getResultData().put("procId", work.getFlowProcess().getProcId());
        getResultData().put("txnType", work.getFlowProcess().getTxnType());
        getResultData().put("procCreateTime",
                            work.getFlowProcess().getProcCreateTime());
        getResultData()
                .put("procStatus", work.getFlowProcess().getProcStatus());
        getResultData().put(
                "progressList",
                flowEngineService.getProgress(
                        work.getFlowProcess().getProcId(), user));

        List procWorkList = flowEngineService.listWorkByProcDealed(work
                .getFlowProcess().getProcId(), getUser());
        if (null != procWorkList) {
            getResultData().put("procWorkList", procWorkList);
        }

        if (user instanceof AbstractCorpUser
            && (Constants.ROLE_OPERATOR.equals(((AbstractCorpUser) user)
                                               .getRoleId())
                || Constants.ROLE_APPROVER
                .equals(((AbstractCorpUser) user).getRoleId()) ||
                Constants.ROLE_EXECUTOR
                .equals(((AbstractCorpUser) user).getRoleId()))) {
            getResultData().put("FinanceFlag", "Y");

        } else {
            getResultData().put("FinanceFlag", "N");
        }
    }

    public void approveLoad() throws NTBException {
    	
    	Log.info("approve.do approveLoad begin sessionID="+this.getSession().getId());
        NTBUser user = getUser();
        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");
        String workId = getParameter("workId");
        FlowWork work = flowEngineService.viewWork(workId, getUser());

        Map resultData = this.getResultData();
        String corpType = "";
        boolean hasMobile = true;
        if(user instanceof CorpUser){
        	CorpUser cu = (CorpUser) user;
        	corpType = cu.getCorporation().getCorpType();
        	resultData.put("corpType", corpType);
        	if(Utils.null2EmptyWithTrim(cu.getMobile()).equals("")){
        		hasMobile = false;
        	}
        }
        
        if (flowEngineService.checkoutWork(workId, getUser())) {

            try {
                Approvable approver = (Approvable) (Class.forName(work
                        .getFlowProcess().getTxnBean()).newInstance());
                String url = approver
                             .viewDetail(work.getFlowProcess().getTxnType(),
                                         work
                                         .getFlowProcess().getTransNo(), this);
                if (null == url || "".equals(url)) {
                    throw new NTBException("err.flow.ViewDetail");
                }
                resultData.put("detailPageUrl", url);
                
            } catch (Exception e) {
                Log.error("Process error at ApproveAction.approveLoad(): ", e);
                if (e instanceof NTBException) {
                    throw (NTBException) e;
                } else {
                    throw new NTBException("err.flow.ViewDetail");
                }
            }

            resultData.put("workId", workId);
            resultData.put("txnType", work.getFlowProcess().getTxnType());
            resultData.put("transNo", work.getFlowProcess().getTransNo());
            resultData.put("procCreateTime",
                           work.getFlowProcess().getProcCreateTime());
            resultData.put("procStatus",
                           work.getFlowProcess().getProcStatus());
            resultData.put(
                    "progressList",
                    flowEngineService.getProgress(work.getFlowProcess()
                                                  .getProcId(), user));

            List procWorkList = flowEngineService.listWorkByProcDealed(work
                    .getFlowProcess().getProcId(), getUser());
            if (null != procWorkList) {
                resultData.put("procWorkList", procWorkList);
            }

            String executeFlag = "N";

            if (user instanceof AbstractCorpUser
                && (Constants.ROLE_APPROVER
                    .equals(((AbstractCorpUser) user).getRoleId()) ||
                    Constants.ROLE_EXECUTOR
                    .equals(((AbstractCorpUser) user).getRoleId()))) {
                resultData.put("FinanceFlag", "Y");
                if (Constants.ROLE_APPROVER.equals(((AbstractCorpUser) user).getRoleId()) &&
                    /*CertProcessor.getCheckCertFlag()&&*/ 
                    ((CorpUser)user).getCorporation().getAuthenticationMode().equals(Constants.AUTHENTICATION_CERTIFICATION)
                    ) {
                    //resultData.put("SignDataFlag", "Y");//mod by linrui 20190403
                    //add by linrui for temp otp 20190403
                    resultData.put("OtpFlag", "Y");
                }
                if (Constants.ROLE_APPROVER.equals(((AbstractCorpUser) user).getRoleId()) &&
                        ((CorpUser)user).getCorporation().getAuthenticationMode().equals(Constants.AUTHENTICATION_SECURITY_CODE)) {
                        getResultData().put("CheckSecurityCodeFlag", "Y");
                      //add by linrui for temp otp 20190403
                        resultData.put("OtpFlag", "N");
                }
                if (Constants.ROLE_EXECUTOR.equals(((AbstractCorpUser) user)
                        .getRoleId())) {
                    executeFlag = "Y";
                    resultData.put("OtpFlag", "Y");
                }

            } else {
                resultData.put("FinanceFlag", "N");
            }
            resultData.put("ExecuteFlag", executeFlag);
            //20130129
            //resultData.put("SignDataFlag", "Y");//test
            resultData.put("certModuleListString", this.getSession().getAttribute("certModuleListString"));
            this.setResultData(resultData);
        } else {
            throw new NTBException("err.flow.WorkHolding");
        }
        
        if(!hasMobile){
        	resultData.put("hasMobile", "N");
        }else{
        	resultData.put("hasMobile", "Y");
        	resultData.put("operationType", "send");
        	if(user instanceof CorpUser)
        	resultData.put("showMobileNo", ((AbstractCorpUser)user).getMobile());
        }
        Log.info("approve.do approveLoad sessionID="+this.getSession().getId());
    }

    public void cancelLoad() throws NTBException {
        view();
    }

    public void cancel() throws NTBException {
        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");
        String procId = getParameter("procId");
        flowEngineService.cancelProcess(procId, getUser(), this);
        view();
    }

    public void approve() throws NTBException {

       
    	//add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB begin
		String jCryption = this.getParameter("jCryption") ;
		String workId="";
		String act="";
		String memo="";
		String securityCode="";
		if(null!=jCryption && !"".equals(jCryption)){
			String jCryptionKey = (String)this.getRequest().getSession().getAttribute("jCryptionKey") ;
			
			String loginParameters = AESUtil.decryptJC(jCryption, jCryptionKey) ;
			
			Map paraMap = AESUtil.getParamFromUrl(loginParameters) ;
			
	        workId = (String)paraMap.get("workId");
	        act = (String)paraMap.get("act");
	        memo = (String)paraMap.get("memo");
	        securityCode = (String)paraMap.get("securityCode");
	        this.getResultData().put("act", paraMap.get("act"));
	        this.getResultData().put("memo",paraMap.get("memo"));
	        this.getResultData().put("_fieldsToBeSigned", paraMap.get("_fieldsToBeSigned"));
	        this.getResultData().put("dataToBeSigned", paraMap.get("dataToBeSigned"));
	        this.getResultData().put("signatureValue", paraMap.get("signatureValue"));
	        this.getResultData().put("txnType", paraMap.get("txnType"));
	        this.getResultData().put("workId", paraMap.get("workId"));
	        this.getResultData().put("userCert", paraMap.get("userCert"));
		}else{
			workId = getParameter("workId");
	        act = getParameter("act");
	        memo = getParameter("memo");
	        securityCode = getParameter("securityCode");
		}
		
		
		Map resultData = this.getResultData();
	    resultData.put("workId", workId);
	    this.setResultData(resultData);
        String signSeqNo = null;
        if (FlowEngineService.ACTION_APPROVE.equals(act)) {
            signSeqNo = CertProcessor.checkSignData(getUser(), this,
                                      TxnSignData.SIGN_ACTION_APPROVE);
        } else if (FlowEngineService.ACTION_REJECT.equals(act)) {
            signSeqNo = CertProcessor.checkSignData(getUser(), this,
                                      TxnSignData.SIGN_ACTION_REJECT);
        }
        
        String corpType = "" ;
        if(this.getUser() instanceof CorpUser){
        	CorpUser Corpuser = (CorpUser)this.getUser() ;
        	corpType =  Corpuser.getCorporation().getCorpType();
        	//check otp again by linrui 20190502
        	if (Constants.ROLE_APPROVER.equals(((AbstractCorpUser) Corpuser).getRoleId()) && 
        			((CorpUser)Corpuser).getCorporation().getAuthenticationMode().equals(Constants.AUTHENTICATION_CERTIFICATION)){
        		String otp = getParameter("otp");
                String smsFlowNo = getParameter("smsFlowNo");
                String exceedResend = getParameter("exceedResend");
                SMSOTPObject otpObject = SMSOTPUtil.getOtpObject(smsFlowNo) ;
                SMSReturnObject returnObject = new SMSReturnObject() ;
                
                //SMSOTPUtil.check(returnObject, smsFlowNo, otp, exceedResend, SMSOTPUtil.getLang(this.getUser().getLanguage())) ;
                try{
                	SMSOTPUtil.check(returnObject, smsFlowNo, otp, "N", "E") ;
                }catch (NTBException e) {
                	Log.info("OTP Error");
                	returnObject.setErrorFlag("Y") ;
    				returnObject.setReturnErr(e.getErrorCode()) ;
				}
                if(!returnObject.getErrorFlag().equals("N")){
    				Log.info("One time password error") ;
    				resultData.put("smsFlowNo", smsFlowNo);
    				setResultData(resultData);
    				setUsrSessionData(resultData);
    				throw new NTBException(returnObject.getReturnErr());
        		}
        	}
        	//end
    	}
    	 
//        if(Constants.CORP_TYPE_SMALL.equals(corpType)){
        if(Constants.CORP_TYPE_SMALL.equals(corpType)){
        	//this.checkSMS() ;
        }else{//check security code
        	checkSecurityCode(securityCode);
        }
  
        
        //閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓瑙掆晪鎷烽敓鏂ゆ嫹閿熻锛岃揪鎷烽敓锟藉墠閿熸枻鎷�work
        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");
        boolean successFlag = flowEngineService.doWork(workId, act, memo,
                getUser(), this);

        //閿熸枻鎷疯嵔閿熸枻鎷烽敓闃跺埡顒婃嫹閿熸枻鎷烽敓锟絪ign data table
        FlowWork flowWork = flowEngineService.viewWork(workId, getUser());
        Map signDataUpdater = new HashMap();
        signDataUpdater.put("transId", flowWork.getFlowProcess().getTransNo());
        signDataUpdater.put("procId", flowWork.getFlowProcess().getProcId());
        if (!successFlag) {
            //閿熸枻鎷烽敓鏂ゆ嫹澶遍敓鏂ゆ嫹
            if (signSeqNo != null) {
                signDataUpdater.put("processResult",
                                    TxnSignData.ACTION_RESULT_FAILED);
                CertProcessor.updateSignData(signSeqNo, signDataUpdater);
            }
            throw new NTBException("err.flow.OperationFail");
        }
        //閿熸枻鎷烽敓鏂ゆ嫹鏅掗敓锟�
        if (signSeqNo != null) {
            signDataUpdater.put("processResult",
                                TxnSignData.ACTION_RESULT_ACCOMPLISHED);
            CertProcessor.updateSignData(signSeqNo, signDataUpdater);
        }

        //Map resultData = this.getResultData();
        if (null != flowWork && null != flowWork.getFlowProcess()) {
            resultData.put("procStatus",
                           flowWork.getFlowProcess().getProcStatus());
        }

        //閿熶粙鐪媎etail閿熸枻鎷烽敓闃讹綇鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓缁炴拝鎷烽敓鏂ゆ嫹
        view();
        
     // modify by long_zg 2014-04-03 for CR204 Apply OTP to BOB begin 
        if(Constants.CORP_TYPE_SMALL.equals(corpType)){
        	SMSOTPUtil.updateRefIdToSmsOtpLog(this.getParameter("smsFlowNo"), flowWork.getFlowProcess().getTransNo()) ;
        }
        //閿熸枻鎷烽敓鐭鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹
        resultData.put("act", act);
        setResultData(resultData);

        // 閿熸枻鎷烽敓閾拌揪鎷烽敓鏂ゆ嫹閿熸枻鎷峰垹閿熸枻鎷�2006/10/16
        // populate cache 閿熺煫纰夋嫹閿熸枻鎷�genericJdbcDao, 閿熸枻鎷�Approve 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鐭鎷穌ao閿熸枻鎷烽敓鏂ゆ嫹鍚�
        // 閿熸枻鎷烽敓锟紸pprove 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鑿橀敓鏂ゆ嫹閿熸枻鎷锋湭閿熺粨浜ら敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹 populate cache閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�populate 閿熸枻鎷疯幎閿熸枻鎷烽敓锟�
        // 鐩墠閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹 Approve 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�addPendingCache閿熸枻鎷烽敓鏂ゆ嫹apporve閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿燂拷
        // populatePendingCache();
        // CachedDBRCFactory.populatePendingCache();
    }

    public void cancelApprove() throws NTBException {
        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");
        String workId = getParameter("workId");
        flowEngineService.undoCheckoutWork(workId, getUser().getUserId());
        list();
    }

    public void multiApproveLoad() throws NTBException {
        NTBUser user = getUser();
        Map resultData = this.getResultData();
        String corpType = "";
        boolean hasMobile = true;
        if(user instanceof CorpUser){
        	CorpUser cu = (CorpUser) user;
        	corpType = cu.getCorporation().getCorpType();
        	resultData.put("corpType", corpType);
        	if(Utils.null2EmptyWithTrim(cu.getMobile()).equals("")){
        		hasMobile = false;
        	}
        }
        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");
        //閿熸枻鎷烽〉閿熸枻鎷烽敓鏂ゆ嫹閿熺獤鈽呮嫹閿熸枻鎷烽敓鏂ゆ嫹鐚擃剨鎷峰嵈閿燂拷resultData 閿熸枻鎷烽�鍙�
        addSelectedWorkFromList();
        String[] workIds = (String[]) getResultData().get("workIds");
        if (workIds == null) {
            workIds = getParameterValues("workIds");
        }

        //閿熸枻鎷烽敓杞垮洷鈽呮嫹鏍￠敓鏂ゆ嫹铏归敓锟�
        if (workIds == null || workIds.length == 0) {
            throw new NTBException("err.flow.NoWorksSelected");
        }

        //閿熸枻鎷烽敓琛楄皫鈽呮嫹閿熸彮浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓璇崟閿熸枻鎷烽敓鏂ゆ嫹鏉冪姸鎬�
        if (workIds.length == 1) {
            ActionForward forward = new ActionForward(
                    "/approve.do?ActionMethod=approveLoad&workId=" + workIds[0], true);
            setForward(forward);
            return;
        }

        //閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鏉�
        List totalWorks = (List) getResultData().get("workList");

        List selectedWorks = new ArrayList();
        List checkoutWorks = new ArrayList();
        HashMap item = null;

        for (int index = 0; index < totalWorks.size(); index++) {
            item = (HashMap) totalWorks.get(index);
            String itemId = (String)item.get("workId");
            boolean itemSelected = false;
            for (int i = 0; i < workIds.length; i++) {
                if (itemId.equals(workIds[i])) {
                    itemSelected = true;
                    break;
                }
            }

            if(itemSelected){
                selectedWorks.add(item);
                if (flowEngineService.checkoutWork(itemId, getUser())) {
                    checkoutWorks.add(item);
                    item.put("checkout", "Y");
                } else {
                    item.put("checkout", "N");
                }
            }
        }

        if (checkoutWorks.size() == 0) {
            throw new NTBException("err.flow.NoWorksCanApprove");
        }

        String executeFlag = "N";

        if (user instanceof AbstractCorpUser
            && (Constants.ROLE_APPROVER.equals(((AbstractCorpUser) user)
                                               .getRoleId()) ||
                Constants.ROLE_EXECUTOR
                .equals(((AbstractCorpUser) user).getRoleId()))) {
            getResultData().put("FinanceFlag", "Y");
            //閿熸枻鎷烽敓锟�閿熻鏂ゆ嫹閿熻妭鏂ゆ嫹閿熸枻鎷�2.閿熸枻鎷疯壊涓篈pprover 3.閿熸枻鎷烽敓鏂ゆ嫹checkCertFlag閿熸枻鎷风‘ 閿熸枻鎷蜂竴閿熸枻鎷疯閿熸枻鎷烽敓鏂ゆ嫹绛鹃敓鏂ゆ嫹
            if (Constants.ROLE_APPROVER.equals(((AbstractCorpUser) user).getRoleId()) &&
                ((CorpUser)user).getCorporation().getAuthenticationMode().equals(Constants.AUTHENTICATION_CERTIFICATION)) {
                //getResultData().put("SignDataFlag", "Y");
              //add by linrui for temp otp 20190403
            	getResultData().put("OtpFlag", "Y");
            }
            if (Constants.ROLE_APPROVER.equals(((AbstractCorpUser) user).getRoleId()) &&
                    ((CorpUser)user).getCorporation().getAuthenticationMode().equals(Constants.AUTHENTICATION_SECURITY_CODE)) {
                    getResultData().put("CheckSecurityCodeFlag", "Y");
                  //add by linrui for temp otp 20190403
                    getResultData().put("OtpFlag", "N");
            }
            if (Constants.ROLE_EXECUTOR.equals(((AbstractCorpUser) user)
                                               .getRoleId())) {
                executeFlag = "Y";
              //add by linrui for temp otp 20190403
                getResultData().put("OtpFlag", "Y");
            }
        } else {
            getResultData().put("FinanceFlag", "N");
        }
        
        if(!hasMobile){
        	resultData.put("hasMobile", "N");
        }else{
        	resultData.put("hasMobile", "Y");
        	resultData.put("operationType", "send");
        	if(user instanceof CorpUser)
        	resultData.put("showMobileNo", ((AbstractCorpUser)user).getMobile());
        }

        getResultData().put("ExecuteFlag", executeFlag);

        getResultData().put("selectedWorkList", selectedWorks);
        getResultData().put("checkoutWorks", checkoutWorks);
        //20130129
        //getResultData().put("SignDataFlag", "Y");//test
        getResultData().put("certModuleListString", this.getSession().getAttribute("certModuleListString"));

    }

    public void multiApproveProgressBar() throws NTBException {
        NTBUser user = getUser();

        String[] signSeqNoArray = (String[]) getResultData().get("signSeqNoArray");

        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");
        String act = getParameter("act");
        String memo = getParameter("memo");
        if(act==null||act.equals("")){
        	act=(String) this.getResultData().get("act");
        }
        if(memo==null||memo.equals("")){
        	memo=(String) this.getResultData().get("memo");
        }
        //List checkoutWorks = (List) getResultData().get("checkoutWorks");
        
        //by wen 20110321 filter for checkoutWorkList
        List checkoutWorksOriginal = (List) getResultData().get("checkoutWorks");
        List checkoutWorks = new ArrayList();
        Map tempMap = new HashMap();
        for (int i=0;i<checkoutWorksOriginal.size();i++){
        	Map mapc = (HashMap) checkoutWorksOriginal.get(i);
        	String workid = (String) mapc.get("workId");
        	if(tempMap.get(workid)!=null){
        		Log.info("multiApprove() >> workid duplicate sumbit and will be filtered: "+workid);
        	}else{
        		tempMap.put(workid, workid);
        		checkoutWorks.add(mapc);
        	}
        }
        //end 20110321
        
        //by wency 20130117 Filte works from session
        checkoutWorks =  this.checkWorksFromSession(checkoutWorks);
        //end 20130117

        HashMap item = null;
        FlowProcess flowProcess = null;

        String[] workIds = new String[checkoutWorks.size()];

        for (int i = 0; i < checkoutWorks.size(); i++) {
            item = (HashMap) checkoutWorks.get(i);
            workIds[i] = (String) item.get("workId");
        }

//        boolean[] boolRs = flowEngineService.doMultiWork(workIds, act, memo,
//                user, this);
        Object[] errorMsg = flowEngineService.execMultiWork(workIds, act, memo,
              user, this);

        for (int i = 0; i < checkoutWorks.size(); i++) {
            item = (HashMap) checkoutWorks.get(i);
            flowProcess = flowEngineService.viewFlowProcess((String) item
                    .get("procId"));

            Map signDataUpdater = new HashMap();
            signDataUpdater.put("transId", flowProcess.getTransNo());
            signDataUpdater.put("procId", flowProcess.getProcId());

            if (errorMsg[i] == "success") {
                item.put("successFlag", "Y");
                if (null != flowProcess) {
                    item.put("procStatus", flowProcess.getProcStatus());
                }
                item.put("progressList", flowEngineService.getProgress(
                        (String) item.get("procId"), user));

                //閿熸枻鎷烽敓鏂ゆ嫹鏅掗敓锟�
                if (signSeqNoArray != null) {
                    String signSeqNo = signSeqNoArray[i];
                    signDataUpdater.put("processResult",
                                        TxnSignData.ACTION_RESULT_ACCOMPLISHED);
                    CertProcessor.updateSignData(signSeqNo, signDataUpdater);
                }
            } else {
                item.put("successFlag", "N");
                item.put("errorMsg", errorMsg[i]);
                item.put("div_id", "div_"+i);
                
                //by wen_cy 20130117 remove works from session when process failed.
                this.removeWorksFromSession(item.get("workId"));
                //20130117
                
                //閿熸枻鎷烽敓鏂ゆ嫹澶遍敓鏂ゆ嫹
                if (signSeqNoArray != null) {
                    String signSeqNo = signSeqNoArray[i];
                    signDataUpdater.put("processResult",
                                        TxnSignData.ACTION_RESULT_FAILED);
                    CertProcessor.updateSignData(signSeqNo, signDataUpdater);
                }
            }
            
        }
        
    	//20130403 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓浠娾�杈炬嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷峰織閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鍙鎷烽〉閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷风姸鎬侀敓鏂ゆ嫹涓洪敓鏂ゆ嫹閿熸彮浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓缂存唻鎷峰噯
		setWorksStatus4ProgressBar(workIds[workIds.length-1]);

        getResultData().put("act", act);
        // Modified by nabai 2006/10/16
        // populate cache 閿熺煫纰夋嫹閿熸枻鎷�genericJdbcDao, 閿熸枻鎷�Approve 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鐭鎷穌ao閿熸枻鎷烽敓鏂ゆ嫹鍚�
        // 閿熸枻鎷烽敓锟紸pprove 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鑿橀敓鏂ゆ嫹閿熸枻鎷锋湭閿熺粨浜ら敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹 populate cache閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�populate 閿熸枻鎷疯幎閿熸枻鎷烽敓锟�
        // 鐩墠閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹 Approve 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�addPendingCache閿熸枻鎷烽敓鏂ゆ嫹apporve閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿燂拷
        // populatePendingCache();
        // CachedDBRCFactory.populatePendingCache();
    }
    
    public void multiApprove() throws NTBException {
    	
    	//String securityCode = this.getParameter("securityCode");
    	//add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB begin
		String jCryption = this.getParameter("jCryption") ;
		String securityCode="";
		if(null!=jCryption && !"".equals(jCryption)){
			String jCryptionKey = (String)this.getRequest().getSession().getAttribute("jCryptionKey") ;
			
			String loginParameters = AESUtil.decryptJC(jCryption, jCryptionKey) ;
			
			Map paraMap = AESUtil.getParamFromUrl(loginParameters) ;
			
	        securityCode = (String)paraMap.get("securityCode");
	        this.getResultData().put("act", paraMap.get("act"));
	        this.getResultData().put("memo",paraMap.get("memo"));
	        this.getResultData().put("_fieldsToBeSigned", paraMap.get("_fieldsToBeSigned"));
	        this.getResultData().put("dataToBeSigned", paraMap.get("dataToBeSigned"));
	        this.getResultData().put("signatureValue", paraMap.get("signatureValue"));
	        this.getResultData().put("txnType", paraMap.get("txnType"));
	        this.getResultData().put("transDesc", paraMap.get("transDesc"));
	        this.getResultData().put("fieldsToBeSigned", paraMap.get("fieldsToBeSigned"));
	        this.getResultData().put("toCurrency", paraMap.get("toCurrency"));
	        this.getResultData().put("currency", paraMap.get("currency"));
	        this.getResultData().put("amount", paraMap.get("toCurrency"));
	        this.getResultData().put("toAmount", paraMap.get("currency"));
	        this.getResultData().put("multiFlag", paraMap.get("multiFlag"));
	        this.getResultData().put("workId", paraMap.get("workId"));
	        this.getResultData().put("userCert", paraMap.get("userCert"));
		}else{
	        securityCode = getParameter("securityCode");
		}
		
        //update by liang_ly for CR204 2015-4-9
        //閿熸枻鎷烽敓绲奙S
        String corpType = "" ;
        if(this.getUser() instanceof CorpUser){
        	CorpUser Corpuser = (CorpUser)this.getUser() ;
        	corpType =  Corpuser.getCorporation().getCorpType();
        	//check otp again by linrui 20190502
        	if (Constants.ROLE_APPROVER.equals(((AbstractCorpUser) Corpuser).getRoleId()) && 
        			((CorpUser)Corpuser).getCorporation().getAuthenticationMode().equals(Constants.AUTHENTICATION_CERTIFICATION)){
        		String otp = getParameter("otp");
                String smsFlowNo = getParameter("smsFlowNo");
                String exceedResend = getParameter("exceedResend");
                SMSOTPObject otpObject = SMSOTPUtil.getOtpObject(smsFlowNo) ;
                SMSReturnObject returnObject = new SMSReturnObject() ;
                
                //modified by lzg 20190820
                /*SMSOTPUtil.check(returnObject, smsFlowNo, otp, exceedResend, SMSOTPUtil.getLang(this.getUser().getLanguage())) ;
                if(!returnObject.getErrorFlag().equals("N")){
        			throw new NTBException(returnObject.getReturnErr());
        		}*/
                try{
                	SMSOTPUtil.check(returnObject, smsFlowNo, otp, "N", "E") ;
                }catch (NTBException e) {
                	Log.info("OTP Error");
                	returnObject.setErrorFlag("Y") ;
    				returnObject.setReturnErr(e.getErrorCode()) ;
				}
                if(!returnObject.getErrorFlag().equals("N")){
    				Log.info("One time password error");
    				this.getResultData().put("smsFlowNo", smsFlowNo);
    				throw new NTBException(returnObject.getReturnErr());
        		}
                //modified by lzg end
        	}
        	//end
    	}
    	 
        if(Constants.CORP_TYPE_SMALL.equals(corpType)){
        	//this.checkSMS() ;
        	SMSOTPUtil.removeInstance(this.getParameter("smsFlowNo"));
        }else{
        	checkSecurityCode(securityCode);
        }
        
        String[] signSeqNoArray = CertProcessor.checkMultiSignData(getUser(), this,
                TxnSignData.SIGN_ACTION_APPROVE);
        
        getResultData().put("signSeqNoArray", signSeqNoArray);
        
        //update by liang_ly for CR204 2015-4-9  end
        
        List checkoutWorksOriginal = (List) getResultData().get("checkoutWorks");
        getResultData().put("checkoutWorksSize", checkoutWorksOriginal.size());
        
        //閿熺煫鍖℃嫹worksStatus
        setWorksStatus4ProgressBar("init");

        //labels neccesary
        //RBFactory rb = RBFactory.getInstance("app.cib.resource.flow.progress_bar_status");
        Locale locale = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");
        RBFactory rb = RBFactory.getInstance("app.cib.resource.flow.progress_bar_status", locale.toString());
        getResultData().put("progressNote", rb.getString("progressNote"));
        getResultData().put("taskWaiting", rb.getString("taskWaiting"));
        getResultData().put("taskBeingProcessed", rb.getString("taskBeingProcessed"));

        
        
        Thread trd = new Thread(this);
        trd.start();
        
    }
    
    public void multiApproveShowResult() throws NTBException {
    	//do nothing
    }
    
    public void run() {
		// TODO Auto-generated method stub
		try {
			multiApproveProgressBar();
		} catch (NTBException e) {
			Log.error("Thread error [multiApproveProgressBar] : "+e.getMessage(), e);
		}
	}
    
    public void multiApprove_bak20130304() throws NTBException {
        NTBUser user = getUser();
        String securityCode = this.getParameter("securityCode");

        String[] signSeqNoArray = CertProcessor.checkMultiSignData(getUser(), this,
                TxnSignData.SIGN_ACTION_APPROVE);
        
        //閿熸枻鎷烽敓绲猠curity code
        checkSecurityCode(securityCode);

        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");
        String act = getParameter("act");
        String memo = getParameter("memo");
        //List checkoutWorks = (List) getResultData().get("checkoutWorks");
        
        //by wen 20110321 filter for checkoutWorkList
        List checkoutWorksOriginal = (List) getResultData().get("checkoutWorks");
        List checkoutWorks = new ArrayList();
        Map tempMap = new HashMap();
        for (int i=0;i<checkoutWorksOriginal.size();i++){
        	Map mapc = (HashMap) checkoutWorksOriginal.get(i);
        	String workid = (String) mapc.get("workId");
        	if(tempMap.get(workid)!=null){
        		Log.info("multiApprove() >> workid duplicate sumbit and will be filtered: "+workid);
        	}else{
        		tempMap.put(workid, workid);
        		checkoutWorks.add(mapc);
        	}
        }
        //end 20110321
        
        //by wency 20130117 Filte works from session
        checkoutWorks =  this.checkWorksFromSession(checkoutWorks);
        //end 20130117

        HashMap item = null;
        FlowProcess flowProcess = null;

        String[] workIds = new String[checkoutWorks.size()];

        for (int i = 0; i < checkoutWorks.size(); i++) {
            item = (HashMap) checkoutWorks.get(i);
            workIds[i] = (String) item.get("workId");
        }

//        boolean[] boolRs = flowEngineService.doMultiWork(workIds, act, memo,
//                user, this);
        Object[] errorMsg = flowEngineService.execMultiWork(workIds, act, memo,
              user, this);

        for (int i = 0; i < checkoutWorks.size(); i++) {
            item = (HashMap) checkoutWorks.get(i);
            flowProcess = flowEngineService.viewFlowProcess((String) item
                    .get("procId"));

            Map signDataUpdater = new HashMap();
            signDataUpdater.put("transId", flowProcess.getTransNo());
            signDataUpdater.put("procId", flowProcess.getProcId());

            if (errorMsg[i] == "success") {
                item.put("successFlag", "Y");
                if (null != flowProcess) {
                    item.put("procStatus", flowProcess.getProcStatus());
                }
                item.put("progressList", flowEngineService.getProgress(
                        (String) item.get("procId"), user));

                //閿熸枻鎷烽敓鏂ゆ嫹鏅掗敓锟�
                if (signSeqNoArray != null) {
                    String signSeqNo = signSeqNoArray[i];
                    signDataUpdater.put("processResult",
                                        TxnSignData.ACTION_RESULT_ACCOMPLISHED);
                    CertProcessor.updateSignData(signSeqNo, signDataUpdater);
                }
            } else {
                item.put("successFlag", "N");
                item.put("errorMsg", errorMsg[i]);
                item.put("div_id", "div_"+i);
                
                //by wen_cy 20130117 remove works from session when process failed.
                this.removeWorksFromSession(item.get("workId"));
                //20130117
                
                //閿熸枻鎷烽敓鏂ゆ嫹澶遍敓鏂ゆ嫹
                if (signSeqNoArray != null) {
                    String signSeqNo = signSeqNoArray[i];
                    signDataUpdater.put("processResult",
                                        TxnSignData.ACTION_RESULT_FAILED);
                    CertProcessor.updateSignData(signSeqNo, signDataUpdater);
                }
            }

        }

        getResultData().put("act", act);
        // Modified by nabai 2006/10/16
        // populate cache 閿熺煫纰夋嫹閿熸枻鎷�genericJdbcDao, 閿熸枻鎷�Approve 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鐭鎷穌ao閿熸枻鎷烽敓鏂ゆ嫹鍚�
        // 閿熸枻鎷烽敓锟紸pprove 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鑿橀敓鏂ゆ嫹閿熸枻鎷锋湭閿熺粨浜ら敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹 populate cache閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�populate 閿熸枻鎷疯幎閿熸枻鎷烽敓锟�
        // 鐩墠閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹 Approve 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�addPendingCache閿熸枻鎷烽敓鏂ゆ嫹apporve閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿燂拷
        // populatePendingCache();
        // CachedDBRCFactory.populatePendingCache();
    }

    public void cancelMultiApprove() throws NTBException {
        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");

        List checkoutWorks = (List) getResultData().get("checkoutWorks");

        NTBUser user = getUser();
        String workId = null;
        HashMap item = null;

        for (int i = 0; i < checkoutWorks.size(); i++) {
            item = (HashMap) checkoutWorks.get(i);
            workId = (String) item.get("workId");
            flowEngineService.undoCheckoutWork(workId, user.getUserId());
        }

        list();
    }

    public void statusEnquiry() throws NTBException {
        Map workMap = null;
        String currencyDownload = null;
        String amountDownload = null;
        String financeFlag = null;
        String viewAllTransFlag = "";
        ApplicationContext appContext = Config.getAppContext();
        FlowEngineService flowEngineService = (FlowEngineService)appContext.getBean("FlowEngineService");
        CorporationService corporationService = (CorporationService)appContext.getBean("CorporationService");
        
        NTBUser user = getUser();
        String rangeType = Utils.null2EmptyWithTrim(this.getParameter("rangeType"));
        String dateFrom = Utils.null2EmptyWithTrim(this.getParameter("dateFrom"));
        String dateTo = Utils.null2EmptyWithTrim(this.getParameter("dateTo"));
        String roleId = Utils.null2EmptyWithTrim(this.getParameter("roleId"));
        String userId = Utils.null2EmptyWithTrim(this.getParameter("userId"));
        String procStatus = Utils.null2EmptyWithTrim(this.getParameter("procStatus"));
        String sortOrder = Utils.null2EmptyWithTrim(this.getParameter("sortOrder"));
        if("".equals(sortOrder)) sortOrder = "1";
        String corpType = "";
        
        if (user instanceof AbstractCorpUser){
        	if(Constants.ROLE_OPERATOR.equals(((AbstractCorpUser) user).getRoleId())
                    || Constants.ROLE_APPROVER.equals(((AbstractCorpUser) user).getRoleId()) 
                    ||Constants.ROLE_EXECUTOR.equals(((AbstractCorpUser) user).getRoleId())
                    ||/*add by linrui 20190815*/Constants.ROLE_ADMINISTRATOR.equals(((AbstractCorpUser) user).getRoleId())){
        		financeFlag = "Y";
                //add by hjs 20070425
                viewAllTransFlag = ((CorpUser) user).getViewAllTransFlag();
                Corporation corp = corporationService.view(((AbstractCorpUser) user).getCorpId());
                corpType = corp.getCorpType();
             }
        } else {
            financeFlag = "N";
        }
        
        //list by process status
        List works = null;
        Log.debug("[Outstanding Equiry]viewAllTransFlag is \"" + viewAllTransFlag + "\"");
        Log.debug("[Outstanding Equiry]procStatus is \"" + procStatus + "\"");
        
        if(CorpUser.VIEW_ALL_TRANS_ON.equals(viewAllTransFlag)){
            if(procStatus.equals("0") || procStatus.equals("")){
                List works1 = flowEngineService.listWorkByUserDealing(user, userId, dateFrom, dateTo, sortOrder);
            	List works2 = flowEngineService.listWorkByUserDealed(user, userId, procStatus, dateFrom, dateTo, sortOrder);
            	works1.addAll(works2);
            	Sorting.sortMapList(works1, new String[]{"procId"}, Sorting.SORT_TYPE_ASC);
//            	Sorting.sortMapList(works2, new String[]{"procId"}, Sorting.SORT_TYPE_ASC);
            	works = works1;
//            	works = works2;
            } else if(procStatus.equals("P")){
                works = flowEngineService.listWorkByUserDealing(user, userId, dateFrom, dateTo, sortOrder);
            }else if(procStatus.equals("3")){
            	if("0".equals(roleId)){
            		works = flowEngineService.listWorkByUserExpired(user, null, dateFrom, dateTo, sortOrder);
            	}else{
            		works = flowEngineService.listWorkByUserExpired(user, userId, dateFrom, dateTo, sortOrder);
            	}
            } else {
            	works = flowEngineService.listWorkByUserDealed(user, userId, procStatus, dateFrom, dateTo, sortOrder);
            }
        } else {
        	List works1 = null;
        	List works2 = null;
        	works1 = flowEngineService.listWorkByUserExpired(user, userId, dateFrom, dateTo, sortOrder);
            works2 = flowEngineService.listWorkByUserDealing(user, userId, dateFrom, dateTo, sortOrder);
            works1.addAll(works2);
        	Sorting.sortMapList(works1, new String[]{"procId"}, Sorting.SORT_TYPE_ASC);
        	works = works1;
        }

        // 閿熸枻鎷穌ownload閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷风ず閿熸枻鎷烽敓鎻拰鏂ゆ嫹閿燂拷
        if (null != works && "Y".equals(financeFlag)) {
            for (int i = 0; i < works.size(); i++) {
                workMap = (Map) works.get(i);
                //add by lzg 20190819
                if("3".equals(workMap.get("procStatus"))){
                	workMap.put("cancelFlag", "N");
                	continue;
                }
                //add by lzg end
                if (FlowEngineService.RULE_FLAG_TO.equals(workMap.get("ruleFlag"))) {
                    currencyDownload = (String) workMap.get("toCurrency");
                    amountDownload = (String) workMap.get("toAmount");
                } else if (FlowEngineService.RULE_FLAG_FROM.equals(workMap.get("ruleFlag"))) {
                    currencyDownload = (String) workMap.get("currency");
                    amountDownload = (String) workMap.get("amount");
                }
                workMap.put("currencyDownload", currencyDownload);
                workMap.put("amountDownload", amountDownload);

                if (Constants.ROLE_APPROVER.equals(((AbstractCorpUser) user).getRoleId())) {
                    workMap.put("cancelFlag", "Y");
                } else if (Constants.ROLE_OPERATOR.equals(((AbstractCorpUser) user).getRoleId())) {
                    int approvedCount = flowEngineService.getApprovedCount((String) workMap.get("approveStatus"));
                    if (approvedCount == 0) {
                        workMap.put("cancelFlag", "Y");
                    }
                }
                //add by hjs 20070809
                List progressList = null;
                Map row = null;
                if(CorpUser.VIEW_ALL_TRANS_ON.equals(viewAllTransFlag)){ //&& !user.getUserId().equals(userId)){
                	String status = (String) workMap.get("procStatus");
                	loop1:
                	if(("".equals(procStatus) || "0".equals(procStatus) || "P".equals(procStatus)) 
                			&& (!FlowEngineService.PROCESS_STATUS_FINISH.equals(status)
                					&& !FlowEngineService.PROCESS_STATUS_REJECT.equals(status))
                					&& !FlowEngineService.PROCESS_STATUS_CANCEL.equals(status)){
                    	progressList = (List) workMap.get("progressList");
                    	if(null!=progressList && !"".equals(progressList)){
                    		for(int j=0; j<progressList.size(); j++){
                        		row = (Map) progressList.get(j);
                        		if(row.get("DealerId")!=null 
                        				&& user.getUserId().equals(row.get("DealerId"))){
                                    workMap.put("cancelFlag", "Y");
                                    break loop1;
                        		}
                        	}
                    	}
                    	
                        workMap.put("cancelFlag", "N");
                	} else {
                        workMap.put("cancelFlag", "N");
                	}
                }
            }
        }
        
        Map resultData = new HashMap();
        resultData.put("FinanceFlag", financeFlag);
        resultData.put("viewAllTransFlag", viewAllTransFlag);
        resultData.put("statusWorkList", works);
        resultData.put("rangeType", rangeType);
        resultData.put("dateFrom", dateFrom);
        resultData.put("dateTo", dateTo);
        resultData.put("roleId", roleId);
        resultData.put("userId", userId);
        resultData.put("procStatus", procStatus);
        resultData.put("corpType", corpType);
        resultData.put("sortOrder", sortOrder);
        this.setResultData(resultData);
    }

    // Jet add for CR - bank side outstanding authorization
    public void statusEnquiryBank() throws NTBException {
        Map workMap = null;
        String currencyDownload = null;
        String amountDownload = null;
        String financeFlag = null;
        String viewAllTransFlag = "";
        ApplicationContext appContext = Config.getAppContext();
        FlowEngineService flowEngineService = (FlowEngineService)appContext.getBean("FlowEngineService");
        CorporationService corporationService = (CorporationService)appContext.getBean("CorporationService");
        
        //Jet modified - simulate an corpuser with approver role,view_all_transaction_on
//        NTBUser user = getUser();
		BankUser bankuser = (BankUser) this.getUser();		
		String corpId = this.getParameter("corpId");
		String corpName = this.getParameter("corpName");
		if (corpId == null){
			corpId = (String)this.getUsrSessionDataValue("corpId");
		}
		if (corpName == null){
			corpName = (String)this.getUsrSessionDataValue("corpName");
		}
		CorpUser user = new CorpUser(bankuser.getUserId());
		user.setCorpId(corpId);      
		user.setRoleId(Constants.ROLE_APPROVER);
		user.setViewAllTransFlag(CorpUser.VIEW_ALL_TRANS_ON);
		// end
		
        String rangeType = Utils.null2EmptyWithTrim(this.getParameter("rangeType"));
        String dateFrom = Utils.null2EmptyWithTrim(this.getParameter("dateFrom"));
        String dateTo = Utils.null2EmptyWithTrim(this.getParameter("dateTo"));
        String roleId = Utils.null2EmptyWithTrim(this.getParameter("roleId"));
        String userId = Utils.null2EmptyWithTrim(this.getParameter("userId"));
        String procStatus = Utils.null2EmptyWithTrim(this.getParameter("procStatus"));
        String corpType = "";
        
        if (user instanceof AbstractCorpUser
            && (Constants.ROLE_OPERATOR.equals(((AbstractCorpUser) user).getRoleId())
                || Constants.ROLE_APPROVER.equals(((AbstractCorpUser) user).getRoleId()) 
                ||Constants.ROLE_EXECUTOR.equals(((AbstractCorpUser) user).getRoleId()))) {
            financeFlag = "Y";
            //add by hjs 20070425
            viewAllTransFlag = ((CorpUser) user).getViewAllTransFlag();
            Corporation corp = corporationService.view(corpId);
            corpType = corp.getCorpType();
        } else {
            financeFlag = "N";
        }
        
        //list by process status
        List works = null;
        Log.debug("[Outstanding Equiry]viewAllTransFlag is \"" + viewAllTransFlag + "\"");
        Log.debug("[Outstanding Equiry]procStatus is \"" + procStatus + "\"");
        
        if(CorpUser.VIEW_ALL_TRANS_ON.equals(viewAllTransFlag)){
            if(procStatus.equals("0") || procStatus.equals("")){
                List works1 = flowEngineService.listWorkByUserDealing(user, userId, dateFrom, dateTo);
            	List works2 = flowEngineService.listWorkByUserDealed(user, userId, procStatus, dateFrom, dateTo);
            	works1.addAll(works2);
            	Sorting.sortMapList(works1, new String[]{"procId"}, Sorting.SORT_TYPE_ASC);
            	works = works1;
            } else if(procStatus.equals("P")){
                works = flowEngineService.listWorkByUserDealing(user, userId, dateFrom, dateTo);
            }else if(procStatus.equals("3")){
            	if("0".equals(roleId)){
            		works = flowEngineService.listWorkByUserExpired(user, null, dateFrom, dateTo);
            	}else{
            		works = flowEngineService.listWorkByUserExpired(user, userId, dateFrom, dateTo);
            	}
            } else {
            	works = flowEngineService.listWorkByUserDealed(user, userId, procStatus, dateFrom, dateTo);
            }
        } else {
        	List works1 = null;
        	List works2 = null;
        	works1 = flowEngineService.listWorkByUserExpired(user, userId, dateFrom, dateTo);
            works2 = flowEngineService.listWorkByUserDealing(user, userId, dateFrom, dateTo);
            works1.addAll(works2);
        	Sorting.sortMapList(works1, new String[]{"procId"}, Sorting.SORT_TYPE_ASC);
        	works = works1;
        }

        // 閿熸枻鎷穌ownload閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷风ず閿熸枻鎷烽敓鎻拰鏂ゆ嫹閿燂拷
        if (null != works && "Y".equals(financeFlag)) {
            for (int i = 0; i < works.size(); i++) {
                workMap = (Map) works.get(i);
                //add by lzg 20190819
                if("3".equals(workMap.get("procStatus"))){
                	workMap.put("cancelFlag", "N");
                	continue;
                }
                //add by lzg 20190819
                if (FlowEngineService.RULE_FLAG_TO.equals(workMap.get("ruleFlag"))) {
                    currencyDownload = (String) workMap.get("toCurrency");
                    amountDownload = (String) workMap.get("toAmount");
                } else if (FlowEngineService.RULE_FLAG_FROM.equals(workMap.get("ruleFlag"))) {
                    currencyDownload = (String) workMap.get("currency");
                    amountDownload = (String) workMap.get("amount");
                }
                workMap.put("currencyDownload", currencyDownload);
                workMap.put("amountDownload", amountDownload);

                if (Constants.ROLE_APPROVER.equals(((AbstractCorpUser) user).getRoleId())) {
                    workMap.put("cancelFlag", "Y");
                } else if (Constants.ROLE_OPERATOR.equals(((AbstractCorpUser) user).getRoleId())) {
                    int approvedCount = flowEngineService.getApprovedCount((String) workMap.get("approveStatus"));
                    if (approvedCount == 0) {
                        workMap.put("cancelFlag", "Y");
                    }
                }
                //add by hjs 20070809
                List progressList = null;
                Map row = null;
                if(CorpUser.VIEW_ALL_TRANS_ON.equals(viewAllTransFlag)){ //&& !user.getUserId().equals(userId)){
                	String status = (String) workMap.get("procStatus");
                	loop1:
                	if(("".equals(procStatus) || "0".equals(procStatus) || "P".equals(procStatus)) 
                			&& (!FlowEngineService.PROCESS_STATUS_FINISH.equals(status)
                					&& !FlowEngineService.PROCESS_STATUS_REJECT.equals(status))
                					&& !FlowEngineService.PROCESS_STATUS_CANCEL.equals(status)){
                    	progressList = (List) workMap.get("progressList");
                    	for(int j=0; j<progressList.size(); j++){
                    		row = (Map) progressList.get(j);
                    		if(row.get("DealerId")!=null 
                    				&& user.getUserId().equals(row.get("DealerId"))){
                                workMap.put("cancelFlag", "Y");
                                break loop1;
                    		}
                    	}
                        workMap.put("cancelFlag", "N");
                	} else {
                        workMap.put("cancelFlag", "N");
                	}
                }
            }
        }
        
        Map resultData = new HashMap();
        resultData.put("FinanceFlag", financeFlag);
        resultData.put("viewAllTransFlag", viewAllTransFlag);
        resultData.put("statusWorkList", works);
        resultData.put("rangeType", rangeType);
        resultData.put("dateFrom", dateFrom);
        resultData.put("dateTo", dateTo);
        resultData.put("roleId", roleId);
        resultData.put("userId", userId);
        resultData.put("procStatus", procStatus);
        resultData.put("corpType", corpType);
        resultData.put("corpId", corpId);
        resultData.put("corpName", corpName);
        this.setResultData(resultData);
    }
    
    public void historyEnquiryLoad() throws NTBException {

        HashMap resultData = new HashMap();
        NTBUser user = getUser();
        if (user instanceof AbstractCorpUser
            && (Constants.ROLE_OPERATOR.equals(((AbstractCorpUser) user)
                                               .getRoleId())
                || Constants.ROLE_APPROVER
                .equals(((AbstractCorpUser) user).getRoleId()) ||
                Constants.ROLE_EXECUTOR
                .equals(((AbstractCorpUser) user).getRoleId()))) {
            resultData.put("FinanceFlag", "Y");

        } else {
            resultData.put("FinanceFlag", "N");
        }

      /*  NTBCalendar cal = new NTBCalendar();
        String toDate = cal.getTimeFormatted("dd/MM/yyyy");
        cal.add(Calendar.DATE, -7);
        String fromDate = cal.getTimeFormatted("dd/MM/yyyy");
        //String date_range = Utils.null2EmptyWithTrim(this.getParameter("date_range"));

//        resultData.put("dateFrom", fromDate);

        resultData.put("dateFrom", fromDate);
        resultData.put("dateTo", toDate);
       */
        setResultData(resultData);

    }

    public void historyEnquiry() throws NTBException {
        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");
        NTBUser user = getUser();
        String dateRange = Utils.null2EmptyWithTrim(this.getParameter("date_range"));
        String fromDateStr = Utils.null2EmptyWithTrim(getParameter("dateFrom"));
        String toDateStr = Utils.null2EmptyWithTrim(getParameter("dateTo"));
        String sortOrder = Utils.null2EmptyWithTrim(getParameter("sortOrder"));
        //modified by lzg 20190605
        if(!"all".equals(dateRange)){
        	Date dateFrom = DateTime.getDateFromStr(fromDateStr);
    		Date dateTo = DateTime.getDateFromStr(toDateStr);
        	NTBCalendar cal1 = new NTBCalendar(dateTo);
    		int month = cal1.get(Calendar.MONTH);
    		cal1.set(Calendar.MONTH, month - 6);
    		Date toDate2 = cal1.getTime();

    		int compartValue = DateTime.compareDate(toDate2, dateFrom);
    		if (compartValue > 0) {
    			Log.error("err.flow.queryPeriodCanNotExceed6Month");
    			throw new NTBException("err.flow.queryPeriodCanNotExceed6Month");
    		}
        }
		
		
        Map workMap = null;
        String currencyDownload = null;
        String amountDownload = null;
        String financeFlag = null;
        Date fromDate=null;
        Date toDate=null;
        Calendar cal = Calendar.getInstance();
        if(dateRange.equals("all"))
        {
        	cal.add(Calendar.YEAR,-10);
        	fromDate=cal.getTime();
        	toDate=new Date();
          }else{
        if (fromDateStr!="") {
             fromDate = DateTime.getDateFromStr(fromDateStr, true);
        						}
        if (toDateStr!="") {
            toDate = DateTime.getDateFromStr(toDateStr, true);
            cal.setTime(toDate);
            cal.add(Calendar.DATE, 1);
            toDate = cal.getTime();

       }
        }
        List works = flowEngineService
                     .listWorkByUserAll(user, fromDate, toDate, sortOrder);
        
        Map resultData = new HashMap();
        
        resultData.put("date_range",dateRange);
        resultData.put("dateTo", toDateStr);
        resultData.put("dateFrom", fromDateStr);
        resultData.put("sortOrder", sortOrder);

        if (user instanceof AbstractCorpUser
            && (Constants.ROLE_OPERATOR.equals(((AbstractCorpUser) user)
                                               .getRoleId())
                || Constants.ROLE_APPROVER
                .equals(((AbstractCorpUser) user).getRoleId()) ||
                Constants.ROLE_EXECUTOR
                .equals(((AbstractCorpUser) user).getRoleId()))) {
            resultData.put("FinanceFlag", "Y");
            financeFlag = "Y";

        } else {
        	resultData.put("FinanceFlag", "N");
            financeFlag = "N";
        }

        if (null != works && "Y".equals(financeFlag)) {
            for (int i = 0; i < works.size(); i++) {
                workMap = (Map) works.get(i);
                if (FlowEngineService.RULE_FLAG_TO.equals(workMap
                        .get("ruleFlag"))) {
                    currencyDownload = (String) workMap.get("toCurrency");
                    amountDownload = (String) workMap.get("toAmount");
                } else if (FlowEngineService.RULE_FLAG_FROM.equals(workMap
                        .get("ruleFlag"))) {
                    currencyDownload = (String) workMap.get("currency");
                    amountDownload = (String) workMap.get("amount");
                }

                workMap.put("currencyDownload", currencyDownload);
                workMap.put("amountDownload", amountDownload);
            }
        }
        resultData.put("historyWorkList", works);
        
        setResultData(resultData);
    }

    public void changeApproverLoad() throws NTBException {
        NTBUser user = getUser();
        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");
        String transNoToChange = getParameter("transNoToChange");
        String txnTypeToChange = getParameter("txnTypeToChange");

        FlowProcess flowProcess = flowEngineService.viewFlowProcess(
                txnTypeToChange, transNoToChange);

        if (null == flowProcess
            || !FlowEngineService.PROCESS_STATUS_NEW.equals(flowProcess
                .getProcStatus())) {
            throw new NTBException("err.flow.TransactionStateAbnormal");
        }

        try {
            Approvable approver = (Approvable) (Class.forName(flowProcess
                    .getTxnBean()).newInstance());
            String url = approver.viewDetail(flowProcess.getTxnType(),
                                             flowProcess.getTransNo(), this);
            if (null == url || "".equals(url)) {
                throw new NTBException("err.flow.ViewDetail");
            }
            getResultData().put("detailPageUrl", url);

        } catch (Exception e) {
            Log.error("Process error at ApproveAction.changeApproverLoad(): ",
                      e);
            if (e instanceof NTBException) {
                throw (NTBException) e;
            } else {
                throw new NTBException("err.flow.ViewDetail");
            }
        }

        getResultData().put("procId", flowProcess.getProcId());
        getResultData().put("txnType", flowProcess.getTxnType());
        getResultData().put("transNoToChange", transNoToChange);
        getResultData().put("txnTypeToChange", txnTypeToChange);
        getResultData().put("procCreateTime", flowProcess.getProcCreateTime());
        getResultData().put("procStatus", flowProcess.getProcStatus());
        getResultData().put("progressList",
                            flowEngineService.getProgress(flowProcess.getProcId(),
                user));

        List procWorkList = flowEngineService.listWorkByProcDealed(flowProcess
                .getProcId(), getUser());
        if (null != procWorkList) {
            getResultData().put("procWorkList", procWorkList);
        }

        String executeFlag = "N";

        if (user instanceof AbstractCorpUser
            && (Constants.ROLE_APPROVER.equals(((AbstractCorpUser) user)
                                               .getRoleId()) ||
                Constants.ROLE_EXECUTOR
                .equals(((AbstractCorpUser) user).getRoleId()))) {
            getResultData().put("FinanceFlag", "Y");
            if (Constants.ROLE_EXECUTOR.equals(((AbstractCorpUser) user)
                                               .getRoleId())) {
                executeFlag = "Y";
            }
        } else {
            getResultData().put("FinanceFlag", "N");
        }

        getResultData().put("ExecuteFlag", executeFlag);

    }

    public void changeApprover() throws NTBException {
        NTBUser user = getUser();
        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");
        String procId = getParameter("procId");
        String assignedUser = getParameter("assignedUser");

        if (null != assignedUser && !"".equals(assignedUser)) {
            boolean flag = flowEngineService.changeAssignedUser(procId,
                    assignedUser, this);

            if (!flag) {
                throw new NTBException("err.flow.OperationFail");
            }
        }

        FlowProcess flowProcess = flowEngineService.viewFlowProcess(procId);

        try {
            Approvable approver = (Approvable) (Class.forName(flowProcess
                    .getTxnBean()).newInstance());
            String url = approver.viewDetail(flowProcess.getTxnType(),
                                             flowProcess.getTransNo(), this);
            if (null == url || "".equals(url)) {
                throw new NTBException("err.flow.ViewDetail");
            }
            getResultData().put("detailPageUrl", url);

        } catch (Exception e) {
            Log.error("Process error at ApproveAction.changeApprover(): ", e);
            if (e instanceof NTBException) {
                throw (NTBException) e;
            } else {
                throw new NTBException("err.flow.ViewDetail");
            }
        }

        getResultData().put("procId", flowProcess.getProcId());
        getResultData().put("txnType", flowProcess.getTxnType());
        getResultData().put("procCreateTime", flowProcess.getProcCreateTime());
        getResultData().put("procStatus", flowProcess.getProcStatus());
        getResultData().put("progressList",
                            flowEngineService.getProgress(flowProcess.getProcId(),
                user));

        List procWorkList = flowEngineService.listWorkByProcDealed(flowProcess
                .getProcId(), getUser());
        if (null != procWorkList) {
            getResultData().put("procWorkList", procWorkList);
        }

    }

    public void processPageAction(NTBAction action) throws NTBException {
        //閿熸枻鎷烽敓鏂ゆ嫹list椤甸敓鏂ゆ嫹閿熺淡iew 閿熸枻鎷�multiapprove 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鍙鎷烽敓瑙掑嚖鎷烽敓鏂ゆ嫹瑕侀敓鏂ゆ嫹閿熸枻鎷峰墠check 閿熸枻鎷穡ork 閿熸枻鎷烽敓鏂ゆ嫹閿熺氮elected work閿熷彨鎲嬫嫹
        Map selectedWorkIdMap = (Map) action.getResultData().get("workIdMap");
        if (selectedWorkIdMap == null) {
            selectedWorkIdMap = new HashMap();
        }

        String[] listWorkIds = action.getParameterValues("listIds");
        String[] newSelectedWorkIds = action.getParameterValues("workIds");
        if (listWorkIds != null) {
            for (int i = 0; i < listWorkIds.length; i++) {
                if (selectedWorkIdMap.containsKey(listWorkIds[i])) {
                    selectedWorkIdMap.remove(listWorkIds[i]);
                }
            }
        }
        if (newSelectedWorkIds != null) {
            for (int i = 0; i < newSelectedWorkIds.length; i++) {
                selectedWorkIdMap.put(newSelectedWorkIds[i], "1");
            }
        }

        String[] newWorkIds = new String[selectedWorkIdMap.size()];
        int index = 0;
        Iterator ids = selectedWorkIdMap.keySet().iterator();
        while (ids.hasNext()) {
            String id = (String) ids.next();
            newWorkIds[index++] = id;
        }

        action.getResultData().put("workIdMap", selectedWorkIdMap);
        action.getResultData().put("workIds", newWorkIds);
    }
    
    public void setSecurityCodeLoad() throws NTBException {
    	//update by liang_ly for CR204 
    	Map resultData = this.getResultData();
    	  String corpType = "" ;
          if(this.getUser() instanceof CorpUser){
          	CorpUser Corpuser = (CorpUser)this.getUser() ;
          	corpType =  Corpuser.getCorporation().getCorpType();
      	}
//    	CorpUser Corpuser = (CorpUser)this.getUser() ;
//    	String corpType =  Corpuser.getCorporation().getCorpType();
        resultData.put("corpType", corpType);
    	setResultData(resultData) ;
    }
    
    //add by hjs 20071016
    private void checkSecurityCode(String secCode) throws NTBException {
    	if(this.getUser() instanceof BankUser){
    		return;
    	}
    	if(secCode == null){
    		throw new NTBException("err.sys.getSecurityCodeError");
    	}
    	
    	CorpUser corpUser = (CorpUser) this.getUser();
        String encryptedCode = Encryption.digest(corpUser.getUserId() + secCode, "MD5");
        String savedCode = corpUser.getSecurityCode();
        
        if (Constants.ROLE_APPROVER.equals(((AbstractCorpUser) corpUser).getRoleId()) &&
                ((CorpUser)corpUser).getCorporation().getAuthenticationMode().equals(Constants.AUTHENTICATION_SECURITY_CODE)) {
            
            if (savedCode == null) {
            	this.getResultData().put("otpWrongFlag", "Y");
                throw new NTBException("err.sys.SecurityCodeIsNull");
            }
            if ("R".equals(savedCode)) {
            	this.getResultData().put("otpWrongFlag", "Y");
                throw new NTBException("err.sys.SecurityCodeResetError");
            }        
            if (!savedCode.equals(encryptedCode)) {
            	this.getResultData().put("otpWrongFlag", "Y");
                throw new NTBException("err.sys.SecurityCodeError");
            }
        }
    }
    
    /**
     * - wency 2013017 
     * to assign a map of the submit works to user session.
     *//*
    public void setWorksList2Session(ArrayList works){
    	Map submitWorks = (HashMap)this.getSession().getAttribute("submitWorks");
    	if (submitWorks==null){
    		submitWorks = new HashMap();
    	}
    	
    	HashMap item = null;
    	Iterator it = works.iterator();
    	while(it.hasNext()){
    		item = (HashMap)it.next();
    		String workId = (String)item.get("workId");
    		if(submitWorks.get(workId)!=null){
    			continue;
    		}else{
    			submitWorks.put(workId, workId);
    		} 		
    	} 	 
    	this.getSession().setAttribute("submitWorks", submitWorks);
    }*/
    
    
    public List checkWorksFromSession(List checkoutWorksOriginal) throws NTBException{
    	
    	this.removeWorksFromSessionWhenTimeout();
    	
    	Map submitWorks = (HashMap)this.getSession().getAttribute("submitWorks");
    	if (submitWorks==null){
    		submitWorks = new HashMap();
    	}
    	
        List checkoutWorks = new ArrayList();
//        Map tempMap = new HashMap();
        WorkObject workObject = null;
        for (int i=0;i<checkoutWorksOriginal.size();i++){
        	Map mapc = (HashMap) checkoutWorksOriginal.get(i);
        	
        	String workId = (String) mapc.get("workId");
        	workObject = new WorkObject();
        	workObject.setTimeIn(new Date());
        	workObject.setWorkCreateTime((String)mapc.get("workCreateTime"));
        	workObject.setWorkCreator((String)mapc.get("workCreator"));
        	workObject.setWorkId((String)mapc.get("workId"));
        	
        	if(submitWorks.get(workId)!=null){
        		Log.info("checkWorksFromSession() >> duplicate sumbit on workId : "+workId);
        	}else{
        		submitWorks.put(workId, workObject);
//        		tempMap.put(workId, workId);
        		checkoutWorks.add(mapc);
        	}
        }
        
        //it means "duplicated submit" when checkout works list is empty
        if(checkoutWorks.size()==0){
        	/*Iterator it = tempMap.entrySet().iterator();
        	while(it.hasNext()){
        		submitWorks.remove(it.next());
        	}*/
        	throw new NTBException("Duplicate sumbit failed: These transactions have been submit! ");
        }
        
    	this.getSession().setAttribute("submitWorks", submitWorks);
    	
    	return checkoutWorks;
    	
    }
    
    public void removeWorksFromSession(Object workId){
    	Map submitWorks = (HashMap)this.getSession().getAttribute("submitWorks");
    	if (submitWorks==null){
    		submitWorks = new HashMap();
    	}
    	
    	if(submitWorks.get((String)workId)!=null){
    		submitWorks.remove((String)workId);
    	}
    	
    	this.getSession().setAttribute("submitWorks", submitWorks);
    }
    
    public void removeWorksFromSessionWhenTimeout(){
    	Map submitWorks = (HashMap)this.getSession().getAttribute("submitWorks");
    	if (submitWorks==null){
    		submitWorks = new HashMap();
    	}
    	
    	Iterator it = submitWorks.entrySet().iterator();
    	java.util.List list = new java.util.ArrayList();
    	while(it.hasNext()){
    		Map.Entry work = (Map.Entry)it.next();
    		String key = (String) work.getKey();
    		WorkObject obj = (WorkObject)work.getValue();
    		if(DateTime.getMinutesTween(new Date(), obj.getTimeIn() )>3){
    			list.add(key);
    		}
    	}
    	
    	if(list!=null&&list.size()>0){
    		for(int i=0; i<list.size(); i++){
        		String key = (String)list.get(i);
        		if(submitWorks.get(key)!=null){
        			submitWorks.remove(key);
        		}
        	}
    	}
    	
    	this.getSession().setAttribute("submitWorks", submitWorks);
    }
    
    
    public void setWorksStatus4ProgressBar(String id){
    	
    	if(id==null||"".equals(id)){
    		return;
    	}
    	
    	Map worksStatus4pb = (HashMap)getResultData().get("worksStatus4pb");
    	if (worksStatus4pb==null){
    		worksStatus4pb = new HashMap();
    	}
    	
    	if("init".equals(id)){ 
    		getResultData().put("worksStatus4pb", new HashMap()); //閿熺粨浜ら敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鍓嶉敓鏂ゆ嫹閿熺煫鍖℃嫹
    	}else{ 
    		worksStatus4pb.put(id, "Done!"); //姣忛敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷锋閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷峰獟閿熸枻鎷烽敓鏂ゆ嫹璇�
    	}	
    	
    	getResultData().put("worksStatus4pb", worksStatus4pb);
    }
    
    public void inputCodeLoad()throws NTBException{
    	Map resultData = this.getResultData();
    	String cropType = "";
    	String authenticationMode = "";
    	if(this.getUser() instanceof CorpUser){
    		CorpUser user = (CorpUser) this.getUser();
    		cropType = user.getCorporation().getCorpType();
    		authenticationMode = user.getCorporation().getAuthenticationMode();
    		//if (null != cropType && cropType.equals("3")){
    		if (null != authenticationMode && "C".equals(authenticationMode)){
	    		String txnType = (String) resultData.get("txnType");
	        	String approveType=this.getParameter("approveType");
	        	String funcName = "";
	        	if (approveType==null || approveType.equals("M") ){
	        		funcName = "Multi-Approval";
	        	}else {
	        		funcName = SMSOTPUtil.getFuncName(txnType);
	        	}
	        	resultData.put("funcName", funcName);
    		}
    	}
    	resultData.put("corpType", cropType);
    	resultData.put("authenticationMode", authenticationMode);
    	setResultData(resultData);
    }
    
    public void unavailableApprove() throws NTBException{
    	
    	List checkOutWorks = 
    		(ArrayList)getResultData().get("checkoutWorks");
    	if(checkOutWorks!=null){
    		
    		HashMap item = new HashMap();
    		for(int i=0;i<checkOutWorks.size();i++){
				item = (HashMap)checkOutWorks.get(i);
				//value 7 means this operation has been rejected;
				item.put("procStatus", 7);
			}
    	}
    	
		throw new NTBException("err.sms.MobileUnavaliable");
	

}
	public void unavailableMultiApprove() throws NTBException{
		List checkOutWorks = 
			(ArrayList)getResultData().get("checkoutWorks");
		if(checkOutWorks!=null){
			
			HashMap item = new HashMap();
			for(int i=0;i<checkOutWorks.size();i++){
				item = (HashMap)checkOutWorks.get(i);
				//value 7 means this operation has been rejected;
				item.put("procStatus", 7);
			}
		}
		throw new NTBException("err.sms.MobileUnavaliable");
	}
}
