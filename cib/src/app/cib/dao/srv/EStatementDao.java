/**
 * @author hjs
 * 2007-4-23
 */
package app.cib.dao.srv;

import java.util.*;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

import app.cib.util.Constants;

/**
 * @author hjs
 * 2007-4-23
 */
public class EStatementDao extends GenericJdbcDao {

	private static final String defalutPattern = Config.getProperty("DefaultDatePattern");
	
	
	//add by linrui 20190923 for estatement
	public List listHisRecords(String functionName, String stmtType, String corpId, String range, String dateFrom, String dateTo, String accountNo) throws NTBException {
		String sql = "select * from E_STATEMENT where CI_NO = ? ";
		List valueList = new ArrayList();
		valueList.add(corpId.substring(1));
		// select range not all
		if (!"all".equals(range)) {
			if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
				dateFrom = DateTime.formatDate(dateFrom, defalutPattern, "yyyyMMdd");
				sql += "and to_number(REPORT_DATE) >= to_number( ? ) ";
				valueList.add(dateFrom);
			}
			if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
				dateTo = DateTime.formatDate(dateTo, defalutPattern, "yyyyMMdd");
				sql += "and to_number(REPORT_DATE) <= to_number( ? ) ";
				valueList.add(dateTo);
			}
		}
		
		if ((!Utils.null2EmptyWithTrim(accountNo).equals("")) && (!accountNo.equals("0"))) {
			sql += "and ACCOUNT_NO = ? ";
			valueList.add(accountNo);
		}
		if ((!Utils.null2EmptyWithTrim(stmtType).equals(""))) {
			if("1".equals(stmtType)){
				if("ALL_TYPE_STATEMENT".equals(functionName)){
					sql += Constants.ALL_TYPE_STATEMENT;
				}else if("ALL_TYPE_TIMEDEPOSIT".equals(functionName)){
					sql += Constants.ALL_TYPE_TIMEDEPOSIT;
				}else if("ALL_TYPE_REMITTANCE".equals(functionName)){
					sql += Constants.ALL_TYPE_REMITTANCE;
				}else if("ALL_TYPE_LOAN".equals(functionName)){
					sql += Constants.ALL_TYPE_LOAN;
				}else if("ALL_TYPE_DRAWDOWN".equals(functionName)){
					sql += Constants.ALL_TYPE_DRAWDOWN;
				}else if("ALL_TYPE_OVERDUE_REPAYMENT".equals(functionName)){
					sql += Constants.ALL_TYPE_OVERDUE_REPAYMENT;
				}else if("ALL_TYPE_INACTIVE_ACCOUNT".equals(functionName)){
					sql += Constants.ALL_TYPE_INACTIVE_ACCOUNT;
				}
			}else{
			  sql += "and REPORT_CODE = ? ";
			  valueList.add(stmtType);
			}
		}
		sql += "order by REPORT_DATE desc ";
		
		
		try {
			Log.info("====sql :" + sql);
			Log.info("====args :" + valueList.toArray());
			return this.query(sql, valueList.toArray());
		} catch (Exception e) {
			Log.error("DB Error", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	public List listRecords(String corpId, String range, String dateFrom, String dateTo, 
			String accountType,	String accountNo) throws NTBException {


		String sql = "select * from HS_E_STATEMENT where CORP_ID = ? ";
		List valueList = new ArrayList();

		valueList.add(corpId);

		if (!Utils.null2EmptyWithTrim(range).equals("")) {
			Calendar cal = Calendar.getInstance();
			if(Utils.null2EmptyWithTrim(range).equals("1")){
				cal.add(Calendar.DATE, -1);
				dateFrom = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateTo = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
			} else if(Utils.null2EmptyWithTrim(range).equals("2")){
				cal.add(Calendar.DATE, -7);
				cal.set(Calendar.DAY_OF_WEEK, 1);
				dateFrom = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				cal.set(Calendar.DAY_OF_WEEK, 7);
				dateTo = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
			} else if(Utils.null2EmptyWithTrim(range).equals("3")){
				cal.set(Calendar.DAY_OF_WEEK, 1);
				dateFrom = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				cal.set(Calendar.DAY_OF_WEEK, 7);
				dateTo = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
			} else if(Utils.null2EmptyWithTrim(range).equals("4")){
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.add(Calendar.DATE, -1);
				dateTo = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				cal.set(Calendar.DAY_OF_MONTH, 1);
				dateFrom = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
			} else {
				cal.add(Calendar.MONTH, 1);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.add(Calendar.DATE, -1);
				dateTo = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				cal.set(Calendar.DAY_OF_MONTH, 1);
				dateFrom = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
			}
			sql += "and STATEMENT_DATE >= ? and STATEMENT_DATE <= ? ";
			valueList.add(dateFrom);
			valueList.add(dateTo);
		} else {
			if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
				dateFrom = DateTime.formatDate(dateFrom, defalutPattern, "yyyyMMdd");
				sql += "and STATEMENT_DATE >= ? ";
				valueList.add(dateFrom);
			}
			if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
				dateTo = DateTime.formatDate(dateTo, defalutPattern, "yyyyMMdd");
				sql += "and STATEMENT_DATE <= ? ";
				valueList.add(dateTo);
			}
			
		}
		if ((!Utils.null2EmptyWithTrim(accountType).equals("")) && (!accountType.equals("0")) && !accountType.equals("02")) {
			sql += "and ACCOUNT_TYPE = ? ";
			valueList.add(accountType);
		} else if ((!Utils.null2EmptyWithTrim(accountType).equals("")) && (accountType.equals("02"))) {
			sql += "and ACCOUNT_TYPE in ('02', '03', '04', '05') ";
		}
		if ((!Utils.null2EmptyWithTrim(accountNo).equals("")) && (!accountNo.equals("0"))) {
			sql += "and ACCOUNT_NO = ? ";
			valueList.add(accountNo);
		}

		sql += "order by STATEMENT_DATE desc, ACCOUNT_TYPE, ACCOUNT_NO";


		try {
			return this.query(sql, valueList.toArray());
		} catch (Exception e) {
			Log.error("DB Error", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	public void changeMarkRead(String interalId, String reportDate) throws Exception {
		String sql = "update E_STATEMENT set MARK_READ = 'Y' where INTERNAL_ID = ? and REPORT_DATE = ?";
		String[] conditonParameter = new String[]{interalId,reportDate};
		this.update(sql, conditonParameter);
	}
	
	public void readCheckAll(String stmtType, String actionMethod,String ciNo) throws Exception {
		if(!"1".equals(stmtType)){
			String sql = "update E_STATEMENT set MARK_READ = 'Y' where REPORT_CODE = ? and ci_no = ?";
			this.update(sql, new String[]{stmtType,ciNo});
		}else{
			String[] reportCodes = null;
			if(actionMethod.toUpperCase().contains("CONOTE")){
				reportCodes = Constants.ESTATEMENT_REPORT_CODE_CoNote.split(",");
			}else if(actionMethod.toUpperCase().contains("PENOTE")){
				reportCodes = Constants.ESTATEMENT_REPORT_CODE_PeNote.split(",");
			}else if(actionMethod.toUpperCase().contains("ASNOTE")){
				reportCodes = Constants.ESTATEMENT_REPORT_CODE_AsNote.split(",");
			}else if(actionMethod.toUpperCase().contains("RENOTE")){
				reportCodes = Constants.ESTATEMENT_REPORT_CODE_ReNote.split(",");
			}else if(actionMethod.toUpperCase().contains("LONOTE")){
				reportCodes = Constants.ESTATEMENT_REPORT_CODE_LoNote.split(",");
			}else if(actionMethod.toUpperCase().contains("CRNOTE")){
				reportCodes = Constants.ESTATEMENT_REPORT_CODE_CrNote.split(",");
			}else if(actionMethod.toUpperCase().contains("OVNOTE")){
				reportCodes = Constants.ESTATEMENT_REPORT_CODE_OvNote.split(",");
			}
			String sql = "update E_STATEMENT set MARK_READ = 'Y' where REPORT_CODE in ('";
			for (int i = 0; i < reportCodes.length; i++) {
				if(reportCodes.length == 1){
					sql = sql +reportCodes[i];
					continue;
				}
				if( i == 0){
					sql = sql + reportCodes[i] + "'";
					continue;
				}else if(i == reportCodes.length-1){
					sql = sql + ",'" +reportCodes[i];
					continue;
				}else{
					sql = sql + ",'" +reportCodes[i]+ "'";
				}
			}
			 sql = sql + "') and ci_no = ?";
			 this.update(sql, new String[]{ciNo});
			 
		}
		
	}
	
	public boolean getReadFlag(String reportCode,String ciNo) throws Exception {
		String[] reportCodes =  reportCode.split(",");
		String sql = "select MARK_READ from E_STATEMENT where REPORT_CODE in ('";
		for (int i = 0; i < reportCodes.length; i++) {
			if(reportCodes.length == 1){
				sql = sql +reportCodes[i];
				continue;
			}
			if(i == reportCodes.length-1){
				sql = sql + ",'" +reportCodes[i];
				continue;
			}else if(i == 0){
				sql = sql + reportCodes[i] + "'";
				continue;
			}else{
				sql = sql + ",'" +reportCodes[i]+ "'";
			}
		}
		sql = sql + "') and CI_NO = ?";
			List retList = this.query(sql, new String[]{ciNo});
			if(retList != null){
				for (int i = 0; i < retList.size(); i++) {
					String MARK_READ =(String) ((HashMap) retList.get(i)).get("MARK_READ");
					if("N".equals(MARK_READ)){
						return false;
					}
				}
			}
			return true;
	}
	public boolean getReadFlagByActionMethod(String actionMethod, String ciNo) throws Exception {

		String[] reportCodes = null;
		if(actionMethod.toUpperCase().contains("CONOTE")){
			reportCodes = Constants.ESTATEMENT_REPORT_CODE_CoNote.split(",");
		}else if(actionMethod.toUpperCase().contains("PENOTE")){
			reportCodes = Constants.ESTATEMENT_REPORT_CODE_PeNote.split(",");
		}else if(actionMethod.toUpperCase().contains("ASNOTE")){
			reportCodes = Constants.ESTATEMENT_REPORT_CODE_AsNote.split(",");
		}else if(actionMethod.toUpperCase().contains("RENOTE")){
			reportCodes = Constants.ESTATEMENT_REPORT_CODE_ReNote.split(",");
		}else if(actionMethod.toUpperCase().contains("LONOTE")){
			reportCodes = Constants.ESTATEMENT_REPORT_CODE_LoNote.split(",");
		}else if(actionMethod.toUpperCase().contains("CRNOTE")){
			reportCodes = Constants.ESTATEMENT_REPORT_CODE_CrNote.split(",");
		}else if(actionMethod.toUpperCase().contains("OVNOTE")){
			reportCodes = Constants.ESTATEMENT_REPORT_CODE_OvNote.split(",");
		}
		String sql = "select MARK_READ from E_STATEMENT where REPORT_CODE in ('";
		for (int i = 0; i < reportCodes.length; i++) {
			if(reportCodes.length == 1){
				sql = sql +reportCodes[i];
				continue;
			}
			if(i == reportCodes.length-1){
				sql = sql + ",'" +reportCodes[i];
				continue;
			}else if(i == 0){
				sql = sql + reportCodes[i] + "'";
				continue;
			}else{
				sql = sql + ",'" +reportCodes[i]+ "'";
			}
		}
		sql = sql + "') and CI_NO = ?";
			List retList = this.query(sql, new String[]{ciNo});
			if(retList != null){
				for (int i = 0; i < retList.size(); i++) {
					String MARK_READ =(String) ((HashMap) retList.get(i)).get("MARK_READ");
					if("N".equals(MARK_READ)){
						return false;
					}
				}
			}
			return true;
	
	}
	public void changeMarkReadByPdfName(String estatementName) throws Exception {
		String sql = "update E_STATEMENT set MARK_READ = 'Y' where PDF_FILE_NAME = ?";
		String[] conditonParameter = new String[]{estatementName};
		this.update(sql, conditonParameter);
		
	}
}
