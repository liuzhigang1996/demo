/**
 * @author hjs
 * 2007-4-26
 */
package com.jsax.service.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;

import app.cib.bo.bnk.Corporation;
import app.cib.bo.sys.AbstractCorpUser;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.service.srv.EStatementService;
import app.cib.service.sys.CorpUserService;
import app.cib.service.txn.BillPaymentService;
import app.cib.util.Constants;
import app.cib.util.StatementToken;
import app.cib.util.otp.SMSOTPUtil;
import app.cib.util.otp.SMSReturnObject;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.jsax.core.NotLogined;
import com.jsax.core.SubElement;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBLoginException;
import com.neturbo.set.utils.Encryption;
import com.neturbo.set.utils.RBFactory;

/**
 * @author Nabai
 * 2011-11-29
 * CR149 cert type
 */
public class ReadCheckService extends JsaxAction implements JsaxService,NotLogined {
	
	public void doTransaction() throws Exception {
		ApplicationContext appContext = Config.getAppContext();
		EStatementService statementService = (EStatementService) appContext.getBean("EStatementService");
		
		CorpUser user = (CorpUser) this.getUser();
		String ciNo = user.getCorpId().substring(1);
		String readCheckAll = this.getParameter("readCheckAll");
		String actionMethod = this.getParameter("ActionMethod");
		
		if("Y".equals(readCheckAll)){
			String stmtType = this.getParameter("stmtType");
			String operator = this.getParameter("operator");
			try{
				if("readAll".equals(operator)){
					statementService.readCheckAll(stmtType,actionMethod,ciNo);
				}else{
					String[] compressKey = this.getParameterValues("key");
					for (int i = 0; i < compressKey.length; i++) {
						String estatementName = StatementToken.checkToken(compressKey[i].substring(2));
						statementService.changeMarkReadByPdfName(estatementName);
					}
				}
				boolean flag = statementService.getReadFlagByActionMethod(actionMethod,ciNo);
				
				if(actionMethod.toUpperCase().contains("CONOTE")){
					this.addSubResponseList(TARGET_TYPE_FIELD, "menuControlId", "CoNote");
				}
				
				if(actionMethod.toUpperCase().contains("PENOTE")){
					this.addSubResponseList(TARGET_TYPE_FIELD, "menuControlId", "PeNote");
				}
				
				if(actionMethod.toUpperCase().contains("ASNOTE")){
					this.addSubResponseList(TARGET_TYPE_FIELD, "menuControlId", "AsNote");
				}
				
				if(actionMethod.toUpperCase().contains("RENOTE")){
					this.addSubResponseList(TARGET_TYPE_FIELD, "menuControlId", "ReNote");
				}
				
				if(actionMethod.toUpperCase().contains("LONOTE")){
					this.addSubResponseList(TARGET_TYPE_FIELD, "menuControlId", "LoNote");
				}
				
				if(actionMethod.toUpperCase().contains("CRNOTE")){
					this.addSubResponseList(TARGET_TYPE_FIELD, "menuControlId", "CrNote");
				}
				
				if(actionMethod.toUpperCase().contains("OVNOTE")){
					this.addSubResponseList(TARGET_TYPE_FIELD, "menuControlId", "OvNote");
				}
				
				if(flag){
					this.addSubResponseList(TARGET_TYPE_FIELD, "readCheckFlag", "Y");
				}else{
					this.addSubResponseList(TARGET_TYPE_FIELD, "readCheckFlag", "N");
				}
			}catch (Exception e) {
				this.addSubResponseList(TARGET_TYPE_FIELD, "errorFlag", "Y");
				throw new NTBException("err.sys.DBError");
			}
		}else{
			String checkType = this.getParameter("checkType");
			String interalId = this.getParameter("internalId");
			String reportDate = this.getParameter("reportDate");
			String reportCode =  this.getParameter("reportCode");
			Map resultData = this.getResultData();
			try{
				statementService.changeMarkRead(interalId,reportDate);
				List docList = (LinkedList)resultData.get("docList");
				for (int i = 0; i < docList.size(); i++) {
					Map retMap = (HashMap)docList.get(i);
					if(retMap.get("INTERNAL_ID").equals(interalId) && retMap.get("REPORT_DATE").equals(reportDate)){
						retMap.put("MARK_READ", "Y");
					}
				}
				resultData.put("docList", docList);
				this.setResultData(resultData);
			}catch (Exception e) {
				this.addSubResponseList(TARGET_TYPE_FIELD, "errorFlag", "Y");
				e.printStackTrace();
				throw new NTBException("err.sys.DBError");
			}
			
			try{
				boolean flag = statementService.getReadFlagByActionMethod(actionMethod,ciNo);
				if(flag){
					this.addSubResponseList(TARGET_TYPE_FIELD, "readCheckFlag", "Y");
				}else{
					this.addSubResponseList(TARGET_TYPE_FIELD, "readCheckFlag", "N");
				}
			}catch (Exception e) {
				this.addSubResponseList(TARGET_TYPE_FIELD, "errorFlag", "Y");
				throw new NTBException("err.sys.DBError");
			}
		}
	}

	public String processNotLogined(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
