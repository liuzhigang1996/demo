/**
 * @author hjs
 * 2007-4-30
 */
package com.jsax.service.sys;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibTransClient;
import app.cib.service.enq.SwiftCodeEnqureService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.txn.TimeDepositService;

import com.alibaba.fastjson.JSON;
import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.jsax.core.SubElement;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.RBFactory;

/**
 * @author hjs 2007-4-30
 */
public class GetInterestRateList extends JsaxAction implements JsaxService {

	private GenericJdbcDao genericJdbcDao;

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	public void doTransaction() throws Exception {
		ApplicationContext appContext = Config.getAppContext();
		TimeDepositService timeDepositService = (TimeDepositService) appContext
				.getBean("TimeDepositService");
		String language = this.getParameter("pageLanguage");
		if( "zh_HK".equals(language)){
			language = "C";
		}else{
			language = "E";
		}
		List showInterestList = timeDepositService.getInterestList(language);
		Log.info("====================first from Db LIst:" + showInterestList);
		showInterestList = filter(showInterestList);
		Log.info("====================secend from Db filter LIst:" + showInterestList);
		
		//add for yema
		List periodList = timeDepositService.getPeriodList(language);
		
		periodList = removeNoRatePeriod(periodList,showInterestList);
		this.addSubResponseList(TARGET_TYPE_FIELD, "showInterestList", JSON.toJSONString(showInterestList));
		this.addSubResponseList(TARGET_TYPE_FIELD, "tableHeader",JSON.toJSONString(periodList));
		this.addSubResponseList(TARGET_TYPE_FIELD, "updateShowTime",((HashMap)showInterestList.get(0)).get("UPDATE_TIME").toString());
	}
	
	private List removeNoRatePeriod(List periodList, List showInterestList) {
		List retList = new ArrayList();
M:for (int i = 0; i < periodList.size(); i++) {
			Map retMap = (HashMap)periodList.get(i);
			String period = (String) retMap.get("PERIOD_DESCRIPTION");
			period = period.replaceAll(" ", "");
			for (int j = 0; j < showInterestList.size(); j++) {
				Map interestListMap = (HashMap) showInterestList.get(j);
				if(!"--.-%".equals(interestListMap.get(period))){
						retList.add(retMap);
						continue M;
				}
			}
		}
		return retList;
	}

	private List filter(List showInterestList) {
		List retList = new ArrayList();
		for(int i =0; i < showInterestList.size(); i++){
			Map retMap = (Map) showInterestList.get(i);
			Map newMap = new HashMap();
			for(Object key : retMap.keySet()){
				if(retMap.get(key) == null){
					key = key.toString().replaceAll(" ", "");
					key = key.toString().replaceAll("'", "");
					newMap.put(key, "--.-%");
				}else if(retMap.get(key) instanceof BigDecimal){
					BigDecimal value = (BigDecimal) retMap.get(key);
					key = key.toString().replaceAll(" ", "");
					key = key.toString().replaceAll("'", "");
					newMap.put(key, value.multiply(new BigDecimal("100")) + "%");
				}else{
					String value = (String) retMap.get(key);
					key = key.toString().replaceAll(" ", "");
					key = key.toString().replaceAll("'", "");
					newMap.put(key, value);
				}
			}
			retList.add(newMap);
		}
		return retList;
	}
	
}