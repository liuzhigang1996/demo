/**
 * @author hjs
 * 2006-8-8
 */
package com.jsax.service.txn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.jsax.core.SubElement;
import com.neturbo.set.core.Config;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.utils.RBFactory;

/**
 * @author hjs
 * 2006-8-8
 */
public class MerchantListService extends JsaxAction implements JsaxService {
	
	private GenericJdbcDao genericJdbcDao;

	/**
	 * @return the genericJdbcDao
	 */
	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	/**
	 * @param genericJdbcDao the genericJdbcDao to set
	 */
	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}
	
	public void doTransaction() throws Exception {
		//
		this.setTargetType(getParameter("targetType"));
		
		setTargetId(getParameter("targetId"));
		String category = getParameter("originValue");
		String subListId = getParameter("subListId");
		
		//mod by lq20171123
		String language = getParameter("language");
//		Locale locale = (this.getUser().getLanguage()==null) ? Config.getDefaultLocale() : this.getUser().getLanguage();
//		RBFactory rb = RBFactory.getInstance("app.cib.resource.txn.bill_payment", locale.toString());
		RBFactory rb = RBFactory.getInstance("app.cib.resource.txn.bill_payment", language);
		
		Map headerOption = new HashMap();
		headerOption.put("MER_SHORT_NAME", rb.getString("sel_mer"));
		headerOption.put("MER_CODE", "0");
		
		//调用业务逻辑处理
		List subList = this.getSubList(category);
		subList.add(0, headerOption);
		//subList = this.convertPojoList2MapList(subList);
		
		//一个element对应一个select下拉框
		SubElement element =  mapList2Selector(subList, subListId, "MER_SHORT_NAME", "MER_CODE");
		
		//加入到select框列表
		List elementList = new ArrayList();
		elementList.add(element);
		this.addSubResponseListByDefaultType(elementList);
	}
	
	//业务逻辑处理函数
	private List getSubList(String category) throws Exception {
		String sql = "select MER_CODE, MER_SHORT_NAME from HS_MERCHANT_NAME where CATEGORY_CODE=?";
		return this.genericJdbcDao.query(sql, new Object[]{category});
	}

}
