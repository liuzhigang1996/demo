/**
 * @author hjs
 * 2007-4-30
 */
package com.jsax.service.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibTransClient;
import app.cib.service.sys.CorpAccountService;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.jsax.core.SubElement;
import com.neturbo.set.core.Config;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.RBFactory;

/**
 * @author hjs 2007-4-30
 */
public class AccountsService extends JsaxAction implements JsaxService {

	private GenericJdbcDao genericJdbcDao;

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	public void doTransaction() throws Exception {
		//
		this.setTargetType(getParameter("targetType"));

		setTargetId(getParameter("targetId"));
		String appCode = getParameter("originValue");
		String subListId = getParameter("subListId");

		CorpUser corpUser = (CorpUser) this.getUser();
		Locale locale = (corpUser.getLanguage() == null) ? Config
				.getDefaultLocale() : corpUser.getLanguage();
		RBFactory rb = RBFactory.getInstance(
				"app.cib.resource.srv.doc_archive", locale.toString());
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService accService = (CorpAccountService) appContext.getBean("CorpAccountService");
		String accountType = appCode==null ? null : appCode2AccType(appCode);
		List priviledgeList = accService.listAllPrivilegedAccount((CorpUser)this.getUser(),
				accountType);
		
		// 填充第一个选项
		Map headerOption = new HashMap();
		headerOption.put("ACCOUNT_NAME", rb.getString("all"));
		headerOption.put("ACCOUNT_NO", "0");

		List subList = new ArrayList();
		// 调用业务逻辑处理
		if (!appCode.equals("0")) {
			subList.addAll(this.getSubList(corpUser.getCorpId(), appCode));
		}
		//mod by chen_y for CR192 20161117
		//刷选出有权限的账户的结单
		Map row = null;
		List eDocList = new ArrayList();
		for(int i=0; i<subList.size();i++){
			row = (Map) subList.get(i);
			boolean isExsit = false;
			for(int j=0; j<priviledgeList.size(); j++){
				
				CorpAccount corpAccount = (CorpAccount) priviledgeList.get(j);
				
				if(corpAccount.getAccountNo().equals((String)row.get("ACCOUNT_NO"))){
					eDocList.add(row);
					break;
				}
			}
		}
		
		eDocList.add(0, headerOption);
		// subList = this.convertPojoList2MapList(subList);

		// 一个element对应一个select下拉框
		SubElement element = mapList2Selector(eDocList, subListId, "ACCOUNT_NAME",
				"ACCOUNT_NO");

		// 加入到select框列表
		List elementList = new ArrayList();
		elementList.add(element);
		this.addSubResponseListByDefaultType(elementList);

	}

	// 业务逻辑处理函数
	private List getSubList(String corpId, String appCode) throws Exception {
		String accType = appCode2AccType(appCode);
		String sql = "";
		sql += "select ";
		sql += "ACCOUNT_NO, ";
		sql += "concat(";
		sql += "	concat(ACCOUNT_NO, ";
		sql += "		case ";
		sql += "			when ACCOUNT_NAME is not null then concat(' - ', ACCOUNT_NAME) ";
		sql += "			when ACCOUNT_NAME<>'' then concat(' - ', ACCOUNT_NAME) ";
		sql += "			else '' ";
		sql += "		end ";
		sql += "	), ";
		sql += "	case ";
		sql += "		when ACCOUNT_DESC is not null then concat(' - ', ACCOUNT_DESC) ";
		sql += "		when ACCOUNT_DESC<>'' then concat(' - ', ACCOUNT_DESC) ";
		sql += "		else '' ";
		sql += "	end ";
		sql += ") as ACCOUNT_NAME ";
		sql += "from CORP_ACCOUNT ";
		sql += "where CORP_ID=? and ACCOUNT_TYPE=? ";
		return this.genericJdbcDao.query(sql, new Object[]{corpId, accType});
	}

	private String appCode2AccType(String appCode) throws NTBException {
		String accType = "";
		if (appCode.equals(CibTransClient.APPCODE_CURRENT_ACCOUNT)) {
			accType = CorpAccount.ACCOUNT_TYPE_CURRENT;
		} else if (appCode.equals(CibTransClient.APPCODE_OVERDRAFT_ACCOUNT)) {
			accType = CorpAccount.ACCOUNT_TYPE_OVER_DRAFT;
		} else if (appCode.equals(CibTransClient.APPCODE_TIME_DEPOSIT)) {
			accType = CorpAccount.ACCOUNT_TYPE_TIME_DEPOSIT;
		} else if (appCode.equals(CibTransClient.APPCODE_LOAN_ACCOUNT)) {
			accType = CorpAccount.ACCOUNT_TYPE_LOAN;
		} else if (appCode.equals(CibTransClient.APPCODE_SAVING_ACCOUNT)) {
			accType = CorpAccount.ACCOUNT_TYPE_SAVING;
		} else if (appCode.equals(CibTransClient.APPCODE_CREDIT_VISA) 
				|| appCode.equals(CibTransClient.APPCODE_CREDIT_MASTER) 
				|| appCode.equals(CibTransClient.APPCODE_CREDIT_AE)
				|| appCode.equals(CibTransClient.APPCODE_CREDIT_UT)) {
			accType = CorpAccount.ACCOUNT_TYPE_CREDIT_VISA;
		} 
		return accType;
	}

}
