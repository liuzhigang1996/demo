/**
 * @author js
 * 2007-8-3
 */
package app.cib.action.bnk;

import java.util.*;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bnk.Corporation;
import app.cib.core.CibAction;
import app.cib.service.bnk.ConfigCheckingService;
import app.cib.service.bnk.CorporationService;

/**
 * @author js
 * 2007-8-3
 */
public class ConfigCheckingAction extends CibAction {
    
    public void checkConfig() throws NTBException {
    	ApplicationContext appContext = Config.getAppContext();
    	CorporationService corpService = (CorporationService) appContext.getBean("CorporationService");
    	ConfigCheckingService configCheckingService = (ConfigCheckingService) appContext.getBean("ConfigCheckingService");
    	
    	String corpId = Utils.null2EmptyWithTrim(getParameter("corpId"));
    	Corporation corp = corpService.view(corpId);
    	
    	//check user info in this option
    	List userInfoList = configCheckingService.checkUsersInfo(corp);
    	
    	//check account limits
    	List accLimitInfoList = configCheckingService.checkAccLimitsInfo(corpId, this);
    	
    	//check no setting rules
    	List noSettingRuleList = configCheckingService.checkNoSettingRules(corp);
    	
    	//check normal rules
    	List normalRuleList = configCheckingService.checkNormalRules(corp, this);
    	
    	Map resultData = new HashMap();
    	resultData.put("corpId", corpId);
    	resultData.put("corpName", corp.getCorpName());
    	resultData.put("corpType", corp.getCorpType());
    	resultData.put("allowFinancialController", corp.getAllowFinancialController());
    	resultData.put("userInfoList", userInfoList);
    	resultData.put("accLimitInfoList", accLimitInfoList);
    	resultData.put("noSettingRuleList", noSettingRuleList);
    	resultData.put("normalRuleList", normalRuleList);
    	this.setResultData(resultData);
    	
    }

}
