package com.jsax.service.txn;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.neturbo.set.database.GenericJdbcDao;

public class FindBankByNameService extends JsaxAction implements JsaxService {
	
	private GenericJdbcDao genericJdbcDao;

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	@Override
	public void doTransaction() throws Exception {
		String countryCode = getParameter("countryCode");
		String bankName = getParameter("bankName");
		
		//System.out.println(getParameter("countryCode"));
		//System.out.println(getParameter("bankName"));
		
		List<HashMap<String, String>> list = genericJdbcDao.findBankByName(countryCode,bankName);
		
		StringBuffer sb = new StringBuffer();
		
		//将数据拼接成字符串返回，用   _ 和  & 区分数据
		for (HashMap<String, String> hashMap : list) {
			sb.append(hashMap.get("SWIFT_CODE"));
			sb.append("_");
			sb.append(hashMap.get("BANK_NAME"));
			sb.append("&");
		}
		setTargetType(TARGET_TYPE_ELEMENT);
		
		//System.out.println(getTargetType());
	
		this.addSubResponseListByDefaultType("searchBank", sb.toString());
		
		
		
		
		
		
		
		
		
		
	}

}
