/**
 * @author hjs
 * 2006-9-16
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
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.RBFactory;

/**
 * @author hjs
 * 2006-9-16
 */
public class PeriodByCcyService extends JsaxAction implements JsaxService {
	
	private GenericJdbcDao genericJdbcDao;

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}
	
	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	/* (non-Javadoc)
	 * @see com.jsax.core.JsaxBean#doTransaction()
	 */
	public void doTransaction() throws Exception {
		//
		this.setTargetType(getParameter("targetType"));
		
		setTargetId(getParameter("targetId"));
		String category = getParameter("originValue");
		String subListId = getParameter("subListId");
		
//		Locale locale = (this.getUser().getLanguage()==null) ? Config.getDefaultLocale() : this.getUser().getLanguage();
//		RBFactory rb = RBFactory.getInstance("app.cib.resource.txn.time_deposit", locale.toString());
		String language = getParameter("language");
		RBFactory rb = RBFactory.getInstance("app.cib.resource.txn.time_deposit", language);
		
		Map headerOption = new HashMap();
		headerOption.put("PERIOD_DESCRIPTION", rb.getString("sel_period"));
		headerOption.put("PERIOD", "0");
		
		List subList = this.getSubList(category,language);
		subList.add(0, headerOption);
		//subList = this.convertPojoList2MapList(subList);
		
		SubElement element =  mapList2Selector(subList, subListId, "PERIOD_DESCRIPTION", "PERIOD&PRODUCT_NO");
		
		List elementList = new ArrayList();
		elementList.add(element);
		this.addSubResponseListByDefaultType(elementList);
	}
	
	private List getSubList(String ccy) throws Exception {
		/*String sql = "select substr(t1.period,1,1) as sort1, substr(t1.period,3,1) as sort2, t1.ccy, t1.period, t1.PRODUCT_NO, (t2.period_description || '-' || t1.PRODUCT_DESC) PERIOD_DESCRIPTION from hs_td_account_product_no t1, hs_period_code t2 ";
		sql += " where t1.period=t2.period_code and t1.ccy = ? and t1.resisdent_flag = ? order by sort2, sort1";
		String sql = "select t1.ccy, t1.period, t1.PRODUCT_NO, "
             + "(t2.period_description || '-' || t1.PRODUCT_DESC) PERIOD_DESCRIPTION from hs_td_account_product_no t1, "
             + "hs_period_code t2 where t1.period = t2.period_no and t1.ccy = ? and t1.resisdent_flag = ? order by t1.period ";*/
		//mod by linrui for only show time period, maybe later will use before one 
		String sql = "select t1.ccy, t1.period, t1.PRODUCT_NO, "
			+ "t2.period_description PERIOD_DESCRIPTION from hs_td_account_product_no t1, "
			+ "hs_period_code t2 where t1.period = t2.period_no and t1.ccy = ? and t1.resisdent_flag = ? order by t1.period ";
		return this.genericJdbcDao.query(sql, new Object[]{ccy, "R"});	
	}
	//add by linrui for mul-langue 20190729
	private List getSubList(String ccy,String language) throws Exception {
		/*String sql = "select substr(t1.period,1,1) as sort1, substr(t1.period,3,1) as sort2, t1.ccy, t1.period, t1.PRODUCT_NO, (t2.period_description || '-' || t1.PRODUCT_DESC) PERIOD_DESCRIPTION from hs_td_account_product_no t1, hs_period_code t2 ";
		sql += " where t1.period=t2.period_code and t1.ccy = ? and t1.resisdent_flag = ? order by sort2, sort1";*/
		/*String sql = "select t1.ccy, t1.period, t1.PRODUCT_NO, "
             + "(t2.period_description || '-' || t1.PRODUCT_DESC) PERIOD_DESCRIPTION from hs_td_account_product_no t1, "
             + "hs_period_code t2 where t1.period = t2.period_no and t1.ccy = ? and t1.resisdent_flag = ? order by t1.period ";*/
		//mod by linrui for only show time period, maybe later will use before one 
		/*String sql = "select t1.ccy, t1.period, t1.PRODUCT_NO, "
			+ "t2.period_description PERIOD_DESCRIPTION from hs_td_account_product_no t1, "
			+ "hs_period_code t2 where t1.period = t2.period_no and t1.ccy = ? and t1.resisdent_flag = ? and t2.LOCAL_CODE =? order by t1.period ";*/
		String sql = "select t1.ccy, t1.period, t1.PRODUCT_NO, "
			+ "t2.period_description PERIOD_DESCRIPTION from hs_td_account_product_no t1, "
			+ "hs_period_code t2 where t1.period = t2.period_no and t1.ccy = ? and t1.resisdent_flag = ? and t2.LOCAL_CODE =? and t1.LOCAL_CODE = t2.LOCAL_CODE and substr(t1.product_no,0,2) != 'HI' and substr(t1.product_no,0,1) != 'S'  order by t1.period ";
		return this.genericJdbcDao.query(sql, new Object[]{ccy, "R",Format.transferLang(language)});	
	}

}
