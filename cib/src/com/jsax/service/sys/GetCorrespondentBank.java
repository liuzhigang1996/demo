/**
 * @author hjs
 * 2007-4-26
 */
package com.jsax.service.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSON;
import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.jsax.core.NotLogined;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;

import app.cib.service.enq.SwiftCodeEnqureService;
import app.cib.service.sys.CorpUserService;

/**
 * @author Nabai
 * 2011-11-29
 * CR149 cert type
 */
public class GetCorrespondentBank extends JsaxAction implements JsaxService,NotLogined {
	
	public void doTransaction() throws Exception {
		ApplicationContext appContext = Config.getAppContext();
		SwiftCodeEnqureService swiftCodeEnqureService = (SwiftCodeEnqureService) appContext
				.getBean("SwiftCodeEnqureService");
		Log.info("---------Start Load Bank----------------------");
		String operator = this.getParameter("operator");
		String isFirstSearch = this.getParameter("isFirstSearch");
		String firstRecord = this.getParameter("firstRecord");
		String endRecord = this.getParameter("endRecord");
		
		if("swiftCode".equals(operator)){
			String CorrespondentBankCode = this.getParameter("arg");
			if("".equals(isFirstSearch)){
				int count = swiftCodeEnqureService.getCorrespondentBankBySwiftCodeCount(CorrespondentBankCode);
				Log.info("======total found count: " + count);
				this.addSubResponseList(TARGET_TYPE_FIELD, "GetCorrespondentBankResultCount", count+"");
			}
			List retList = swiftCodeEnqureService.getCorrespondentBankBySwiftCode(CorrespondentBankCode,firstRecord,endRecord);
			String GetCorrespondentBankResult = JSON.toJSONString(retList);
			Log.info("return JSON: " + GetCorrespondentBankResult);
			this.addSubResponseList(TARGET_TYPE_FIELD, "GetCorrespondentBankResult", GetCorrespondentBankResult);
		}else{
			String CorrespondentBankName = this.getParameter("arg");
			if("".equals(isFirstSearch)){
				int count = swiftCodeEnqureService.getCorrespondentBankByBankNameCount(CorrespondentBankName);
				Log.info("======total found count: " + count);
				this.addSubResponseList(TARGET_TYPE_FIELD, "GetCorrespondentBankResultCount", count+"");
			}
			List retList = swiftCodeEnqureService.getCorrespondentBankByBankName(CorrespondentBankName,firstRecord,endRecord);
			String GetCorrespondentBankResult = JSON.toJSONString(retList);
			Log.info("return JSON: " + GetCorrespondentBankResult);
			this.addSubResponseList(TARGET_TYPE_FIELD, "GetCorrespondentBankResult", GetCorrespondentBankResult);
		}
		Log.info("---------Load Bank End----------------------");
	}

	public String processNotLogined(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
