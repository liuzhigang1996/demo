package app.cib.action.bnk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;

import app.cib.bo.bnk.BankUser;
import app.cib.core.CibAction;
import app.cib.service.flow.FlowEngineService;

import java.util.Date;
import java.util.ArrayList;
import com.neturbo.set.utils.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class WelcomeAction extends CibAction {

    public void load() throws NTBException {
        BankUser userObj = (BankUser)this.getUser();
        String userId = userObj.getUserId();
        String userName = userObj.getUserName();
        String roleId = userObj.getRoleId();

        Date prevLoginTime = userObj.getPrevLoginTime();
        String loginStatus = userObj.getPervLoginStatus();

        Map resultData = new HashMap();
        resultData.put("userId", userId);
        resultData.put("userName", userName);
        resultData.put("roleId", roleId);
        resultData.put("loginStatus", loginStatus);
        resultData.put("prevLoginTime", prevLoginTime);

        FlowEngineService flowEngineService = (FlowEngineService) Config
                                              .getAppContext().getBean(
                "FlowEngineService");

        List works = flowEngineService.listWork(userObj);

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
        }else {
            resultData.put("workCount", "0");
        }

        int workCount = Utils.nullEmpty2Zero(resultData.get("workCount"));
        if(workCount == 0){
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

        setResultData(resultData);
    }

}
