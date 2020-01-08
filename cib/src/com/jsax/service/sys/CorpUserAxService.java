/**
 * @author hjs
 * 2007-4-26
 */
package com.jsax.service.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;

import app.cib.bo.sys.AbstractCorpUser;
import app.cib.bo.sys.CorpUser;
import app.cib.service.sys.CorpUserService;
import app.cib.service.txn.BillPaymentService;
import app.cib.util.Constants;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.jsax.core.NotLogined;
import com.jsax.core.SubElement;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBLoginException;
import com.neturbo.set.utils.RBFactory;

/**
 * @author Nabai
 * 2011-11-29
 * CR149 cert type
 */
public class CorpUserAxService extends JsaxAction implements JsaxService,NotLogined {
	
	public void doTransaction() throws Exception {
		//Get the parameters.
		this.setTargetType(TARGET_TYPE_ELEMENT);
//		this.setTargetId(getParameter("targetId"));
		
		String userId = getParameter("userId");
		
        ApplicationContext appContext = Config.getAppContext();
        CorpUserService corpUserService = (CorpUserService)appContext.getBean("corpUserService");
		
		//get the user information from db
		CorpUser user= corpUserService.load(userId);
		if (user == null || user.getStatus().equals(Constants.STATUS_REMOVED)) {
			throw new NTBLoginException("err.sys.UserNotExist");
		}
		//为了CR145投产，先注释CorpUserAxService,login.jsp/ Sam 2012-11-6
		String cardType = user.getCertCardType();
		Log.info("User[" + userId+ "] certCardType from db is:" + cardType);
		
		RBFactory rbFactory = RBFactory.getInstance("app.cib.resource.common.cert_card_type");
		List rbList = rbFactory.getStringArray();
		//
		
		String moduleStr = "";

		//为了CR145投产，先注释CorpUserAxService,login.jsp/ Sam 2012-11-6
		
		String appendingModuleStr = "";
		for(int i = 0;i <rbList.size(); i ++){
			String[] str2 = (String[])rbList.get(i);
			if (str2[1].equals(cardType)){
				moduleStr = str2[0];
			}else{
				appendingModuleStr = appendingModuleStr + ";" + str2[0];
			}
		}
		moduleStr = moduleStr + appendingModuleStr;
		if(moduleStr.startsWith(";")){
			moduleStr = moduleStr.substring(1);
		}
		
		//为了CR145投产，先注释CorpUserAxService,login.jsp/ Sam 2012-11-6
		
		//加入到sub response列表
//		this.addSubResponseListByDefaultType(getTargetId(), moduleStr);
		this.addSubResponseListByDefaultType(getParameter("targetId"),moduleStr);

		//add by Sam for CR145 2012-7-30 check coprUser corpration authenticationMode
        if (((CorpUser)user).getCorporation().getAuthenticationMode().equals(Constants.AUTHENTICATION_CERTIFICATION)) {
    		this.addSubResponseListByDefaultType("checkCertFlag", "Y");
        }else{
        	this.addSubResponseListByDefaultType("checkCertFlag", "N");
        }
	}

	public String processNotLogined(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
