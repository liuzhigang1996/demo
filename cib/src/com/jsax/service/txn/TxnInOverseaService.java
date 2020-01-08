package com.jsax.service.txn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.springframework.context.ApplicationContext;

import app.cib.service.txn.TransferService;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.jsax.core.SubElement;
import com.jsax.utils.TxnInOverseaUtils;
import com.neturbo.set.core.Config;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.RBFactory;

public class TxnInOverseaService extends JsaxAction implements JsaxService {

	public void doTransaction() throws Exception {
		
		this.setTargetType(getParameter("targetType"));
		this.setTargetId(getParameter("targetId"));

		String originValue = getParameter("originValue");
		String subListId = getParameter("subListId");
		String subListId2 = getParameter("subListId2");
		String bankName = getParameter("bankName");
		//add by linrui for huge data for bank 20180312
		 String action = getParameter("action");
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		//add by linrui for mul-language20171116
		String lang = getParameter("language");
		Locale language;
		if (lang == null||"".equals(lang)){
		language = Config.getDefaultLocale();
		}else{
		language = new Locale(lang.substring(0,2),lang.substring(3,5));
		}
		RBFactory rbList = RBFactory.getInstance(
				"app.cib.resource.txn.transfer_oversea", language.toString());
		
		//end
	List subList = new ArrayList();
		// ����ҵ���߼�����
//	 if("country".equals(action)){//add by linrui 20180312
//		subList = transferService.loadOversea(originValue, null, "country");
		subList = transferService.loadOversea(originValue, null, "country" ,Format.transferLang(lang));//mod by linrui 20190729
		HashMap notice = new HashMap();
		notice.put("CITY_NAME", rbList.getString("select_city"));//mod by linrui for mul-language 20171123
		notice.put("CITY_CODE", "");
		subList.add(0, notice);
		/*HashMap noticeLast = new HashMap();
		noticeLast.put("CITY_NAME", rbList.getString("other_city"));
		//noticeLast.put("CITY_CODE", "other");
		noticeLast.put("CITY_CODE", "OTH");
		subList.add(noticeLast);*/
//	 }
     //end
	SubElement element = mapList2Selector(subList, subListId,
				"CITY_NAME", "CITY_CODE");
	List subList1 = new ArrayList();
//	if("city".equals(action)){//add by linrui 20180312
		subList1 = transferService.loadOversea(originValue, null, "city", Format.transferLang(lang));//mod by linrui 20180312
//		subList1 = transferService.loadOversea(originValue, null, "city");
		HashMap notice1 = new HashMap();
		notice1.put("BANK_NAME", rbList.getString("select_bene_bank"));//mod by linrui for mul-language 20171123
		notice1.put("BANK_CODE", "");
		subList1.add(0, notice1);
		HashMap bankNoticeLast = new HashMap();
		bankNoticeLast.put("BANK_NAME", rbList.getString("other_bank"));//mod by liqun for mul-language 20171127
		bankNoticeLast.put("BANK_CODE", "other");
		subList1.add(bankNoticeLast);
//	}
	//end
		SubElement element1 = mapList2Selector(subList1, subListId2, 
				"BANK_NAME", "BANK_CODE");

		// ���뵽select���б�
		List selectList = new ArrayList();
		selectList.add(element);
		selectList.add(element1);
		this.addSubResponseListByDefaultType(selectList);

		// ��ɶ�̬�����
		TxnInOverseaUtils overseaUtils = new TxnInOverseaUtils();		
		HashMap allLabel = transferService.loadOversea(originValue);
		List bankLabelList = (List) allLabel.get("BANK_LABEL");
		if (bankLabelList.size() == 0) {
			addSubResponseList(TARGET_TYPE_ELEMENT, "showBankLabel", "");
		} else {
			SubElement x1 = overseaUtils.convertMapList2TableElement(bankLabelList, "BANK_CODE_LABEL", "spbankCode", "35", "");
			addSubResponseList(TARGET_TYPE_ELEMENT, "showBankLabel", x1);
		}
		List accLabelList = (List) allLabel.get("ACCOUNT_LABEL");
		if (accLabelList.size() == 0) {
			addSubResponseList(TARGET_TYPE_ELEMENT, "showAccountLabel", "");
		} else {
			SubElement x2 = overseaUtils.convertMapList2TableElement(accLabelList, "accType", "ACC_LABEL", "ACC_LABEL", "40", "34");
			addSubResponseList(TARGET_TYPE_ELEMENT, "showAccountLabel", x2);
		}
	}

}
