package app.cib.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import app.cib.bo.sys.CorpUser;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.NTBResources;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

public class RcStatementStatus implements NTBResources {
	private static List objList = null;
	private NTBUser user = null;

	private String corpId = null;
	//add by hjs 2006-10-12
	private String statementType = "";
	private final String linkTemplate = "<a href=\"/cib/DownloadStatement?accNo=%1&key=%2\">%3</a>"; 
	private final String comboBoxTemplate = 
		"<select name=\"statement\" onChange=\"if(this.value != '9'){window.location=this.value;}\">" +
			"<option value=\"9\">%2</option>" +//mod by linrui for mul-language 20171121
			"%1" +
		"</select>"; 
	private final String optionTemplate = "<option value=\"/cib/DownloadStatement?accNo=%1&key=%2\">%3</option>";

	private static boolean refreshed = false;
	
	public static final String STATEMENT_TYPE_FOR_LIST = "1";
	public static final String STATEMENT_TYPE_FOR_DETAIL = "2";

	public RcStatementStatus(String statementType) {
		this.statementType = statementType;
		if (!refreshed) {
			populate();
		}

	}

	public List getKeys() {
		List keys = new ArrayList();
		for (int i = 0; i < objList.size(); i++) {
			Map obj = (Map) objList.get(i);
			String accNo = (String) obj.get("ACC_N0");
			keys.add(accNo);
		}
		return keys;
	}

	public String getProperty(String key) {
		
		Locale locale = (user.getLanguage()==null) ? Config.getDefaultLocale() : user.getLanguage();
		
		RBFactory rbFactory = RBFactory.getInstance("app.cib.resource.enq.statement_Status", locale.toString());
		//add by hjs 2006-10-12
		String tmpRetLink = "";
		String retLink = rbFactory.getString("notApp");
		String accNo = "";
		String fileName = "";
		
		if (this.statementType.equals(STATEMENT_TYPE_FOR_LIST)) {
			for (int i = 0; i < objList.size(); i++) {
				Map obj = (Map) objList.get(i);
				accNo = (String) obj.get("ACC_N0");
				fileName = Utils.null2EmptyWithTrim(obj.get("PDF_FILENAME")) ;
				if (accNo.trim().equals(key)) {
					tmpRetLink = new String(linkTemplate);
					tmpRetLink = tmpRetLink.replaceAll("%1", accNo);
					tmpRetLink = tmpRetLink.replaceAll("%2", "S-" + StatementToken.getToken(fileName));
					tmpRetLink = tmpRetLink.replaceAll("%3", rbFactory.getString("view"));
					
					retLink = tmpRetLink;
					
					break;
				}
			}
		} else if (this.statementType.equals(STATEMENT_TYPE_FOR_DETAIL)){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -6);
			String beforeDate = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
			String statementDate = "";
			for (int i = 0; i < objList.size(); i++) {
				Map obj = (Map) objList.get(i);
				accNo = obj.get("ACC_N0").toString().trim();
				statementDate = obj.get("STATEMENT_DATE").toString().trim();
				fileName = Utils.null2EmptyWithTrim(obj.get("PDF_FILENAME")) ;
				if (accNo.equals(key)) {
					if (Integer.parseInt(statementDate) >= Integer.parseInt(beforeDate)) {
						tmpRetLink = new String(optionTemplate);
						tmpRetLink = tmpRetLink.replaceAll("%1", accNo);
						tmpRetLink = tmpRetLink.replaceAll("%2", "S-" + StatementToken.getToken(fileName));
						tmpRetLink = tmpRetLink.replaceAll("%3", DateTime.formatDate(Utils.null2EmptyWithTrim(obj.get("STATEMENT_DATE")), "yyyyMMdd", "yyyy-MM-dd"));

						retLink += tmpRetLink;
					} else {
						break;
					}
				}
			}
			if (!retLink.equals("")) {
				retLink = comboBoxTemplate.replaceAll("%1", retLink);
				//add by linrui for mul-language 20171121
				retLink = comboBoxTemplate.replaceAll("%2", rbFactory.getString("select_pdf_file"));
			}
			/*
			 else {
				retLink = rbFactory.getString("notApp");
			}
			 */
		}
		/*
		 else {
			retLink = rbFactory.getString("notApp");
		}
		*/
		
		return retLink;
	}

	public void populate() {
		ApplicationContext appContext = Config.getAppContext();
		GenericJdbcDao dao = (GenericJdbcDao) appContext
				.getBean("genericJdbcDao");
		String sql = "select CORP_ID, ACC_N0, STATEMENT_DATE, PDF_FILENAME from HS_E_STATEMENT_METADATA order by STATEMENT_DATE desc";
		try {
			objList = dao.query(sql, new Object[] {});
		} catch (Exception e) {
			Log.error("Error loading group resource", e);
		}
		refreshed = true;
	}

	public void setArgs(Object obj) {
		user = (NTBUser) obj;
		if(user instanceof CorpUser){
			corpId = ((CorpUser)user).getCorpId();
		}
	}
}
