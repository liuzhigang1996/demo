package app.cib.action.srv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;

import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.service.srv.EStatementService;
import app.cib.util.Constants;
import app.cib.util.StatementToken;


public class EStatementAction extends CibAction {
	
	
	
	public void listLoad() throws NTBException{
		
	}
	public void showMenu() throws NTBException{
		ApplicationContext appContext = Config.getAppContext();
		EStatementService statementService = (EStatementService) appContext.getBean("EStatementService");
		
		CorpUser user = (CorpUser) this.getUser();
		String ciNo = user.getCorpId().substring(1);
		String menuReadFlag = "";
		Map resultData = this.getResultData();
		try{
			menuReadFlag = menuReadFlag + "CoNote_" + statementService.getReadFlag(Constants.ESTATEMENT_REPORT_CODE_CoNote,ciNo) + ",";
			menuReadFlag = menuReadFlag + "PeNote_" + statementService.getReadFlag(Constants.ESTATEMENT_REPORT_CODE_PeNote,ciNo) + ",";
			menuReadFlag = menuReadFlag + "AsNote_" + statementService.getReadFlag(Constants.ESTATEMENT_REPORT_CODE_AsNote,ciNo) + ",";
			menuReadFlag = menuReadFlag + "ReNote_" + statementService.getReadFlag(Constants.ESTATEMENT_REPORT_CODE_ReNote,ciNo) + ",";
			menuReadFlag = menuReadFlag + "LoNote_" + statementService.getReadFlag(Constants.ESTATEMENT_REPORT_CODE_LoNote,ciNo) + ",";
			menuReadFlag = menuReadFlag + "CrNote_" + statementService.getReadFlag(Constants.ESTATEMENT_REPORT_CODE_CrNote,ciNo) + ",";
			menuReadFlag = menuReadFlag + "OvNote_" + statementService.getReadFlag(Constants.ESTATEMENT_REPORT_CODE_OvNote,ciNo);
		}catch (Exception e) {
			Log.info("==========menuReadFlag: " + menuReadFlag);
			resultData.put("menuReadFlag", menuReadFlag);
			this.setResultData(resultData);
			throw new NTBException("err.sys.DBError");
		}
		resultData.put("menuReadFlag", menuReadFlag);
		Locale locale = (Locale) this.getSession().getAttribute("Locale$Of$Neturbo");
		resultData.put("language", locale.toString());
		 this.setResultData(resultData);
	}
	public void showCoNote() throws NTBException{
		Map resultData = this.getResultData();
		resultData.put("ActionMethod", "showCoNoteResult");
		resultData.put("estType", "CO");
		
		 this.setResultData(resultData);
	}
	public void showCoNoteResult() throws NTBException{
		ApplicationContext appContext = Config.getAppContext();
		EStatementService statementService = (EStatementService) appContext.getBean("EStatementService");
		CorpUser corpUser = (CorpUser)this.getUser();
		Map resultData = this.getResultData();
		//all   range
		String date_Range = getParameter("date_range");
		String dateFrom = getParameter("dateFrom");
		String dateTo = getParameter("dateTo");
		String stmtType = getParameter("stmtType");
		String accountNo = getParameter("accountNo");
		
		//listEstatement
		List docList = new ArrayList();
		try{
		docList = statementService.listEstatement("ALL_TYPE_STATEMENT",stmtType, corpUser.getCorpId(), date_Range, dateFrom, dateTo, accountNo);
		Map row = new HashMap();
		for(int i=0; i<docList.size(); i++){
			row = (Map) docList.get(i);
			row.put("FILE_LINK", statementService.getHyperLink(corpUser, row.get("PDF_FILE_NAME").toString().trim()));
			row.put("KEY", "S-" + StatementToken.getToken(row.get("PDF_FILE_NAME").toString().trim()));
			}
		}catch (Exception e) {
			resultData.put("dateFrom", dateFrom);
		    resultData.put("dateTo", dateTo);
		    resultData.put("date_range", date_Range);
		    resultData.put("stmtType", stmtType);
		    resultData.put("ActionMethod", "showCoNoteResult");
			resultData.put("estType", "CO");
			throw new NTBException("err.sys.DBError");
		}
	    resultData.put("docList", docList);
	    resultData.put("dateFrom", dateFrom);
	    resultData.put("dateTo", dateTo);
	    resultData.put("date_range", date_Range);
	    resultData.put("stmtType", stmtType);
	    resultData.put("ActionMethod", "showCoNoteResult");
		resultData.put("estType", "CO");
	    this.setResultData(resultData);
		
		
	}
	public void showPeNote() throws NTBException{
		Map resultData = this.getResultData();
		resultData.put("ActionMethod", "showPeNoteResult");
		resultData.put("estType", "PE");
		 this.setResultData(resultData);
	}
	public void showPeNoteResult() throws NTBException{
		ApplicationContext appContext = Config.getAppContext();
		EStatementService statementService = (EStatementService) appContext.getBean("EStatementService");
		CorpUser corpUser = (CorpUser)this.getUser();
		Map resultData = this.getResultData();
		//all   range
		String date_Range = getParameter("date_range");
		String dateFrom = getParameter("dateFrom");
		String dateTo = getParameter("dateTo");
		String stmtType = getParameter("stmtType");
		String accountNo = getParameter("accountNo");
		
		//listEstatement
		List docList = new ArrayList();
		try{
		docList = statementService.listEstatement("ALL_TYPE_TIMEDEPOSIT",stmtType, corpUser.getCorpId(), date_Range, dateFrom, dateTo, accountNo);
		Map row = new HashMap();
		for(int i=0; i<docList.size(); i++){
			row = (Map) docList.get(i);
			row.put("FILE_LINK", statementService.getHyperLink(corpUser, row.get("PDF_FILE_NAME").toString().trim()));
			row.put("KEY", "S-" + StatementToken.getToken(row.get("PDF_FILE_NAME").toString().trim()));
			}
		}catch (Exception e) {
			resultData.put("dateFrom", dateFrom);
		    resultData.put("dateTo", dateTo);
		    resultData.put("date_range", date_Range);
		    resultData.put("stmtType", stmtType);
		    resultData.put("ActionMethod", "showPeNoteResult");
			resultData.put("estType", "PE");
			throw new NTBException("err.sys.DBError");
		}
	    resultData.put("docList", docList);
	    resultData.put("dateFrom", dateFrom);
	    resultData.put("dateTo", dateTo);
	    resultData.put("date_range", date_Range);
	    resultData.put("stmtType", stmtType);
	    resultData.put("ActionMethod", "showPeNoteResult");
		resultData.put("estType", "PE");
	    this.setResultData(resultData);
	}
	public void showReNote() throws NTBException{
		Map resultData = this.getResultData();
		resultData.put("ActionMethod", "showReNoteResult");
		resultData.put("estType", "RE");
		 this.setResultData(resultData);
	}
	public void showReNoteResult() throws NTBException{
		ApplicationContext appContext = Config.getAppContext();
		EStatementService statementService = (EStatementService) appContext.getBean("EStatementService");
		CorpUser corpUser = (CorpUser)this.getUser();
		Map resultData = this.getResultData();
		//all   range
		String date_Range = getParameter("date_range");
		String dateFrom = getParameter("dateFrom");
		String dateTo = getParameter("dateTo");
		String stmtType = getParameter("stmtType");
		String accountNo = getParameter("accountNo");
		
		//listEstatement
		List docList = new ArrayList();
		try{
		docList = statementService.listEstatement("ALL_TYPE_REMITTANCE",stmtType, corpUser.getCorpId(), date_Range, dateFrom, dateTo, accountNo);
		Map row = new HashMap();
		for(int i=0; i<docList.size(); i++){
			row = (Map) docList.get(i);
			row.put("FILE_LINK", statementService.getHyperLink(corpUser, row.get("PDF_FILE_NAME").toString().trim()));
			row.put("KEY", "S-" + StatementToken.getToken(row.get("PDF_FILE_NAME").toString().trim()));
			}
		}catch (Exception e) {
			resultData.put("dateFrom", dateFrom);
		    resultData.put("dateTo", dateTo);
		    resultData.put("date_range", date_Range);
		    resultData.put("stmtType", stmtType);
		    resultData.put("ActionMethod", "showReNoteResult");
			resultData.put("estType", "RE");
			throw new NTBException("err.sys.DBError");
		}
	    resultData.put("docList", docList);
	    resultData.put("dateFrom", dateFrom);
	    resultData.put("dateTo", dateTo);
	    resultData.put("date_range", date_Range);
	    resultData.put("stmtType", stmtType);
	    resultData.put("ActionMethod", "showReNoteResult");
		resultData.put("estType", "RE");
	    this.setResultData(resultData);
	}
	public void showLoNote() throws NTBException{
		Map resultData = this.getResultData();
		resultData.put("ActionMethod", "showLoNoteResult");
		resultData.put("estType", "LO");
		 this.setResultData(resultData);
	}
	public void showLoNoteResult() throws NTBException{
		ApplicationContext appContext = Config.getAppContext();
		EStatementService statementService = (EStatementService) appContext.getBean("EStatementService");
		CorpUser corpUser = (CorpUser)this.getUser();
		Map resultData = this.getResultData();
		//all   range
		String date_Range = getParameter("date_range");
		String dateFrom = getParameter("dateFrom");
		String dateTo = getParameter("dateTo");
		String stmtType = getParameter("stmtType");
		String accountNo = getParameter("accountNo");
		
		//listEstatement
		List docList = new ArrayList();
		try{
		docList = statementService.listEstatement("ALL_TYPE_LOAN",stmtType, corpUser.getCorpId(), date_Range, dateFrom, dateTo, accountNo);
		Map row = new HashMap();
		for(int i=0; i<docList.size(); i++){
			row = (Map) docList.get(i);
			row.put("FILE_LINK", statementService.getHyperLink(corpUser, row.get("PDF_FILE_NAME").toString().trim()));
			row.put("KEY", "S-" + StatementToken.getToken(row.get("PDF_FILE_NAME").toString().trim()));
			}
		}catch (Exception e) {
			resultData.put("dateFrom", dateFrom);
		    resultData.put("dateTo", dateTo);
		    resultData.put("date_range", date_Range);
		    resultData.put("stmtType", stmtType);
		    resultData.put("ActionMethod", "showLoNoteResult");
			resultData.put("estType", "LO");
			throw new NTBException("err.sys.DBError");
		}
	    resultData.put("docList", docList);
	    resultData.put("dateFrom", dateFrom);
	    resultData.put("dateTo", dateTo);
	    resultData.put("date_range", date_Range);
	    resultData.put("stmtType", stmtType);
	    resultData.put("ActionMethod", "showLoNoteResult");
		resultData.put("estType", "LO");
	    this.setResultData(resultData);
	}
	public void showCrNote() throws NTBException{
		Map resultData = this.getResultData();
		resultData.put("ActionMethod", "showCrNoteResult");
		resultData.put("estType", "CR");
		 this.setResultData(resultData);
	}
	public void showCrNoteResult() throws NTBException{
		ApplicationContext appContext = Config.getAppContext();
		EStatementService statementService = (EStatementService) appContext.getBean("EStatementService");
		CorpUser corpUser = (CorpUser)this.getUser();
		Map resultData = this.getResultData();
		//all   range
		String date_Range = getParameter("date_range");
		String dateFrom = getParameter("dateFrom");
		String dateTo = getParameter("dateTo");
		String stmtType = getParameter("stmtType");
		String accountNo = getParameter("accountNo");
		
		//listEstatement
		List docList = new ArrayList();
		try{
		docList = statementService.listEstatement("ALL_TYPE_DRAWDOWN",stmtType, corpUser.getCorpId(), date_Range, dateFrom, dateTo, accountNo);
		Map row = new HashMap();
		for(int i=0; i<docList.size(); i++){
			row = (Map) docList.get(i);
			row.put("FILE_LINK", statementService.getHyperLink(corpUser, row.get("PDF_FILE_NAME").toString().trim()));
			row.put("KEY", "S-" + StatementToken.getToken(row.get("PDF_FILE_NAME").toString().trim()));
			}
		}catch (Exception e) {
			resultData.put("dateFrom", dateFrom);
		    resultData.put("dateTo", dateTo);
		    resultData.put("date_range", date_Range);
		    resultData.put("stmtType", stmtType);
		    resultData.put("ActionMethod", "showCrNoteResult");
			resultData.put("estType", "CR");
			throw new NTBException("err.sys.DBError");
		}
	    resultData.put("docList", docList);
	    resultData.put("dateFrom", dateFrom);
	    resultData.put("dateTo", dateTo);
	    resultData.put("date_range", date_Range);
	    resultData.put("stmtType", stmtType);
	    resultData.put("ActionMethod", "showCrNoteResult");
		resultData.put("estType", "CR");
	    this.setResultData(resultData);
	}
	public void showOvNote() throws NTBException{
		Map resultData = this.getResultData();
		resultData.put("ActionMethod", "showOvNoteResult");
		resultData.put("estType", "OV");
		 this.setResultData(resultData);
	}
	public void showOvNoteResult() throws NTBException{
		ApplicationContext appContext = Config.getAppContext();
		EStatementService statementService = (EStatementService) appContext.getBean("EStatementService");
		CorpUser corpUser = (CorpUser)this.getUser();
		Map resultData = this.getResultData();
		//all   range
		String date_Range = getParameter("date_range");
		String dateFrom = getParameter("dateFrom");
		String dateTo = getParameter("dateTo");
		String stmtType = getParameter("stmtType");
		String accountNo = getParameter("accountNo");
		
		//listEstatement
		List docList = new ArrayList();
		try{
		docList = statementService.listEstatement("ALL_TYPE_OVERDUE_REPAYMENT",stmtType, corpUser.getCorpId(), date_Range, dateFrom, dateTo, accountNo);
		Map row = new HashMap();
		for(int i=0; i<docList.size(); i++){
			row = (Map) docList.get(i);
			row.put("FILE_LINK", statementService.getHyperLink(corpUser, row.get("PDF_FILE_NAME").toString().trim()));
			row.put("KEY", "S-" + StatementToken.getToken(row.get("PDF_FILE_NAME").toString().trim()));
			}
		}catch (Exception e) {
			resultData.put("dateFrom", dateFrom);
		    resultData.put("dateTo", dateTo);
		    resultData.put("date_range", date_Range);
		    resultData.put("stmtType", stmtType);
		    resultData.put("ActionMethod", "showOvNoteResult");
			resultData.put("estType", "OV");
			throw new NTBException("err.sys.DBError");
		}
	    resultData.put("docList", docList);
	    resultData.put("dateFrom", dateFrom);
	    resultData.put("dateTo", dateTo);
	    resultData.put("date_range", date_Range);
	    resultData.put("stmtType", stmtType);
	    resultData.put("ActionMethod", "showOvNoteResult");
		resultData.put("estType", "OV");
	    this.setResultData(resultData);
	}
	public void showAsNote() throws NTBException{
		Map resultData = this.getResultData();
		resultData.put("ActionMethod", "showAsNoteResult");
		resultData.put("estType", "AS");
		 this.setResultData(resultData);
	}
	public void showAsNoteResult() throws NTBException{
		ApplicationContext appContext = Config.getAppContext();
		EStatementService statementService = (EStatementService) appContext.getBean("EStatementService");
		CorpUser corpUser = (CorpUser)this.getUser();
		Map resultData = this.getResultData();
		//all   range
		String date_Range = getParameter("date_range");
		String dateFrom = getParameter("dateFrom");
		String dateTo = getParameter("dateTo");
		String stmtType = getParameter("stmtType");
		String accountNo = getParameter("accountNo");
		
		//listEstatement
		List docList = new ArrayList();
		try{
		docList = statementService.listEstatement("ALL_TYPE_INACTIVE_ACCOUNT",stmtType, corpUser.getCorpId(), date_Range, dateFrom, dateTo, accountNo);
		Map row = new HashMap();
		for(int i=0; i<docList.size(); i++){
			row = (Map) docList.get(i);
			row.put("FILE_LINK", statementService.getHyperLink(corpUser, row.get("PDF_FILE_NAME").toString().trim()));
			row.put("KEY", "S-" + StatementToken.getToken(row.get("PDF_FILE_NAME").toString().trim()));
			}
		}catch (Exception e) {
			resultData.put("dateFrom", dateFrom);
		    resultData.put("dateTo", dateTo);
		    resultData.put("date_range", date_Range);
		    resultData.put("stmtType", stmtType);
		    resultData.put("ActionMethod", "showAsNoteResult");
			resultData.put("estType", "AS");
			throw new NTBException("err.sys.DBError");
		}
	    resultData.put("docList", docList);
	    resultData.put("dateFrom", dateFrom);
	    resultData.put("dateTo", dateTo);
	    resultData.put("date_range", date_Range);
	    resultData.put("stmtType", stmtType);
	    resultData.put("ActionMethod", "showAsNoteResult");
		resultData.put("estType", "AS");
	    this.setResultData(resultData);
	}
	public void showTitle() throws NTBException{
		
	}
	

}
