package com.jsax.service.txn;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import app.cib.bo.sys.CorpUser;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.jsax.core.SubElement;
import com.neturbo.set.core.Config;
import com.neturbo.set.utils.DBRCFactory;
import com.neturbo.set.utils.RBFactory;

public class CorpAccListService extends JsaxAction implements JsaxService {

	public void doTransaction() throws Exception {
		this.setTargetType(getParameter("targetType"));
		this.setTargetId(getParameter("targetId"));
		
		String corpId = this.getParameter("corpId");
		String subListId = getParameter("subListId");
		//add by linrui for mul-language20171116
		String lang = getParameter("language");
		Locale language;
		if (lang == null||lang.trim().equals("")){
		language = Config.getDefaultLocale();
		}else{
		language = new Locale(lang.substring(0,2),lang.substring(3,5));
		}
		RBFactory rList = RBFactory.getInstance(
				"app.cib.resource.txn.corp_delivery", language.toString());
		
		//end

		CorpUser corpUser = new CorpUser();
		corpUser.setCorpId(corpId);
		corpUser.setLanguage(language);//add by linrui for mul-language
		corpUser.setAccountList(new ArrayList());
		DBRCFactory rbList = DBRCFactory.getInstance("accountBySubCorp");
		rbList.setArgs(corpUser);
		List accList = rbList.getStringArray();
		
		String[] notice1 = new String[2];
		notice1[0] ="";
		notice1[1] =rList.getString("select_account");//mod by linrui for mul-language
		accList.add(0, notice1);
		SubElement element1 = arrayList2Selector(accList,subListId,1, 0);
		List selectList = new ArrayList();
		selectList.add(element1);
		this.addSubResponseListByDefaultType(selectList);
	}

}
