package app.cib.action.sys;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.List;
import java.util.Properties;


import com.neturbo.set.utils.*;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;

import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.dao.srv.TransferPromptDao;
import app.cib.bo.bnk.Corporation;
import app.cib.bo.enq.DaylightSavingTime;
import app.cib.bo.srv.TxnPrompt;

import java.util.Date;

import app.cib.service.enq.DaylightSavingTimeService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.srv.MessageUserService;
import app.cib.service.srv.TransferPromptService;
import app.cib.service.sys.CorpUserService;
import app.cib.util.Constants;

import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class WelcomeAction extends CibAction {

    public void load() throws NTBException {

        CorpUser userObj = (CorpUser)this.getUser();
        String userId = userObj.getUserId();
        String userName = userObj.getUserName();
        String title = userObj.getTitle();
        String roleId = userObj.getRoleId();

        Date prevLoginTime = userObj.getPrevLoginTime();
        String loginStatus = userObj.getPervLoginStatus();

        Corporation corpObj = userObj.getCorporation();
        String corpName = corpObj.getCorpName();

        Map resultData = new HashMap();
        resultData.put("userId", userId);
        resultData.put("userName", userName);
        resultData.put("title", title);
        resultData.put("roleId", roleId);
        resultData.put("loginStatus", loginStatus);
        resultData.put("prevLoginTime", prevLoginTime);

        Corporation corp = userObj.getCorporation();
        resultData.put("corpName", corp.getCorpName());
        resultData.put("foreignCity", corp.getForeignCity());
        resultData.put("timeZone", corp.getTimeZone());
        resultData.put("timeMacau", String.valueOf(new Date().getTime()));

        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");

        List works = flowEngineService.listWork(userObj);
        //add by linrui 20190822 model1 works is 0
        if(Constants.CORP_TYPE_SMALL.equals(corp.getCorpType())){
        	works = null;
        }
        //end
        

        if (null != works) {
            resultData.put("workCount", String.valueOf(works.size()));
            List works1 = new ArrayList();
            if (works.size() > 5) {
                for (int i = 0; i < 5; i++) {
                    works1.add(works.get(i));
                }
            } else {
                works1.addAll(works);
            }
            resultData.put("workList", works1);
        } else {
            resultData.put("workCount", "0");
        }

        MessageUserService messageUserService = (MessageUserService) Config
                                                .getAppContext().getBean(
                "MessageUserService");
        List messageList = messageUserService
                           .messageList2MapList(messageUserService.list(userObj
                .getUserId()));

        //add by hjs 20070206
        if(messageList != null){
            List tmpList = new ArrayList();
            for(int j=0; j<messageList.size(); j++) {
            	Map row = (Map) messageList.get(j);
            	if (!row.get("muStatus").equals("1")) {
            		tmpList.add(row);
            	}
            }
            messageList = tmpList;
        }

        if (null != messageList) {
            resultData.put("messageCount", String.valueOf(messageList.size()));
            List messageList1 = new ArrayList();
            if (messageList.size() > 5) {
                for (int i = 0; i < 5; i++) {
                    messageList1.add(messageList.get(i));
                }
            } else {
                messageList1.addAll(messageList);
            }
            resultData.put("messageList", messageList1);
        } else {
            resultData.put("messageCount", "0");
        }

        int workCount = Utils.nullEmpty2Zero(resultData.get("workCount"));
        int messageCount = Utils.nullEmpty2Zero(resultData.get("messageCount"));
        if(workCount == 0 && messageCount == 0){
            resultData.put("listCount", "0");
        }else{
            resultData.put("listCount", "1");
        }


        DateFormat myformat = new SimpleDateFormat("HHmmss");
        String currentTimeStr = myformat.format(new Date());
        int currentTime = Integer.parseInt(currentTimeStr);
        String greetingTime = "";
        if(currentTime >= 0 && currentTime < 120000){
            greetingTime = "Greeting_Morning";
        }
        if(currentTime >= 120000 && currentTime <180000){
            greetingTime = "Greeting_Afternoon";
        }
        if(currentTime >= 180000){
            greetingTime = "Greeting_Evening";
        }
        resultData.put("greetingTime", greetingTime);
        
        //by wen 20110803
		CorpUserService corpUserService = (CorpUserService) Config
				.getAppContext().getBean("corpUserService");
        String corpType = corpObj.getCorpType();
        resultData.put("corpType", corpType);
        String alertMessageFlag = Config.getProperty("app.login.otp.alertMessageFlag");
        resultData.put("alertMessageFlag", alertMessageFlag);
        // ��alertMessageFlag=Yʱ,corpType=3��ʾ����ʾ��,coreType=���� ��ʾԭ�ȵ���ʾ��
        // ��alertMessageFlag=Nʱ,����ʾԭ�ȵ���ʾ��
        if (alertMessageFlag.equals("N") || !corpType.equals("3")) {
        	resultData.put("alertMessage", corpUserService.getSecurityAlertMsg(userId, corpType));
        }
    	//end
        
        TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
        TxnPrompt txnPrompt = new TxnPrompt();
        txnPrompt  = transferPromptService.loadByTxnType("8",TransferPromptDao.STATUS_NORMAL);
        if(txnPrompt == null)
			throw new NTBException("DBError");
        List cMessageList = transferPromptService.getLoginMessageShow(txnPrompt.getMessageId(),"C");
		List eMessageList = transferPromptService.getLoginMessageShow(txnPrompt.getMessageId(),"E");
		resultData.put("menu2nd", txnPrompt.getTxnType());
		if(cMessageList !=null){
			resultData.put("cMessageList", cMessageList);
			resultData.put("cMessageCount", cMessageList.size());
		}
		if(eMessageList != null){
			resultData.put("eMessageList", eMessageList);	
			resultData.put("eMessageCount", eMessageList.size());	
		}
        
        this.setResultData(resultData);
    }
    
    //add by linrui for change language in-page 20190805
    public void changePage() throws NTBException {
		/*CorpUser userObj = (CorpUser) this.getUser();
		Map resultData = this.getParameters();
		String localStr = this.getParameter("PageLanguage");
		if (!(localStr.equals(Constants.US) || localStr.equals(Constants.CN)
			|| localStr.equals(Constants.TW)|| localStr.equals(Constants.HK) 
			|| localStr.equals(Constants.PT))) {
			localStr = Constants.US;
		}// end
		this.setPageLanguage(localStr);
		userObj.setLanguage(new Locale(localStr.substring(0, 2), localStr.substring(3, 5)));
		this.setResultData(resultData);*/
    	//mod to copy loginaction login()
        String localStr = this.getParameter("PageLanguage");
		//add by linrui for mul-language
		if (!(localStr.equals(Constants.US) || localStr.equals(Constants.CN) 
				|| localStr.equals(Constants.TW)|| localStr.equals(Constants.HK)|| localStr.equals(Constants.PT))) {
			localStr = Constants.US;
		}//end
		this.setPageLanguage(localStr);
		CorpUser user = (CorpUser)this.getUser();
		try{
		if (user == null) {
			Map resultData = new HashMap();
			resultData.put("PageLanguage", localStr);
			setResultData(resultData);
			this.setError(new NTBException("err.sys.UserHasLogined"));
			setForward("forceLoginLoad");
			Log.info("+--------- LINK TO FORCE LOGIN ------+");
			Log.info("+------------------------------------+");
			return;
		}
		user.setLanguage(new Locale(localStr.substring(0, 2), localStr.substring(3, 5)));//add by linrui 20190730

		Map resultData = new HashMap();
		resultData.put("userId", user.getUserId());
		resultData.put("cifNo", user.getCorpId().substring(1));
		resultData.put("userName", user.getUserName());
		resultData.put("roleId", user.getRoleId());

		Corporation corp = user.getCorporation();
		String time_zone = null;
		if (corp.getTimeZone() != null) {
			time_zone = corp.getTimeZone().substring(0, 1)+ 
			Utils.removePrefixZero(corp.getTimeZone().substring(1, corp.getTimeZone().length()));
		}
		
		resultData.put("corpName", corp.getCorpName());
		resultData.put("foreignCity", corp.getForeignCity());
		resultData.put("timeZone", time_zone);
		resultData.put("timeMacau", String.valueOf(new Date().getTime()));
		resultData.put("PageLanguage", localStr);

		String cityName = corp.getForeignCity();
		
		if (cityName != null) {
			DaylightSavingTimeService daylightSavingTimeService = (DaylightSavingTimeService) Config.getAppContext().getBean("DaylightSavingTimeService");
			DaylightSavingTime daylightSavingTime = daylightSavingTimeService.checkDST(cityName);
			if (daylightSavingTime != null) {
				if (daylightSavingTime.isFlag()) {
					// Jet modified to change time zone format, like "+08" to "+8"
					String DST_time_zone = null;
					if (daylightSavingTime.getDstTimeZone() != null) {
						DST_time_zone = daylightSavingTime.getDstTimeZone().substring(0, 1)
								+ Utils.removePrefixZero(daylightSavingTime
										.getDstTimeZone().substring(1,daylightSavingTime.getDstTimeZone().length()));
					}

					resultData.put("timeZone", DST_time_zone);
					resultData.put("timeZoneName", daylightSavingTime
							.getDstTimeZoneName());
					resultData.put("isDst", "YES");
				} else {
					resultData.put("timeZoneName", daylightSavingTime
							.getTimeZoneName());
					resultData.put("isDst", "NO");
				}
			}
		}
		//resultData.put("changeLangCode", "Y");
		
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
        TxnPrompt txnPrompt = new TxnPrompt();
        txnPrompt  = transferPromptService.loadByTxnType("8",TransferPromptDao.STATUS_NORMAL);
        if(txnPrompt == null)
			throw new NTBException("DBError");
        List cMessageList = transferPromptService.getLoginMessageShow(txnPrompt.getMessageId(),"C");
		List eMessageList = transferPromptService.getLoginMessageShow(txnPrompt.getMessageId(),"E");
		resultData.put("menu2nd", txnPrompt.getTxnType());
		if(cMessageList !=null){
			resultData.put("cMessageList", cMessageList);
			resultData.put("cMessageCount", cMessageList.size());
		}
		if(eMessageList != null){
			resultData.put("eMessageList", eMessageList);	
			resultData.put("eMessageCount", eMessageList.size());	
		}
		
		setResultData(resultData);

		Log.info("+--------- CHANGE LANGUAGE SUCCESSFUL ---------------+");
		Log.info("LOGINID  = " + user.getUserId());
		Log.info("SESSIONID  = " + user.getSessionId());
		Log.info("+------------------------------------+");
	} catch (NTBException e) {
		Map resultData = new HashMap();
		resultData.put("loginId", user.getLoginId());
		resultData.put("PageLanguage", localStr);
		setResultData(resultData);
		throw e;
	}
    	

    }

}
