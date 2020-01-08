/**
 * @author hjs
 * 2006-8-23
 */
package com.jsax.service.txn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.BillPayment;
import app.cib.util.Constants;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.jsax.core.SubElement;
import com.neturbo.set.core.Config;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.utils.RBFactory;

/**
 * @author hjs
 * 2006-8-23
 */
public class BillNoListService extends JsaxAction implements JsaxService {
	
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

	/* (non-Javadoc)
	 * @see com.jsax.core.JsaxService#performService(javax.servlet.http.HttpServletRequest, com.neturbo.set.core.NTBUser)
	 */
	public void doTransaction() throws Exception{
		//
		this.setTargetType(getParameter("targetType"));
		
		setTargetId(getParameter("targetId"));
		String merchant = getParameter("originValue");
		String subListId = getParameter("subListId");

		//mod by lq20171123
		String language = getParameter("language");
//		Locale locale = (this.getUser().getLanguage()==null) ? Config.getDefaultLocale() : this.getUser().getLanguage();
//		RBFactory rb = RBFactory.getInstance("app.cib.resource.txn.bill_payment", locale.toString());
		RBFactory rb = RBFactory.getInstance("app.cib.resource.txn.bill_payment", language);
		
		Map firstOption = new HashMap();
		firstOption.put("BILL_NAME", rb.getString("sel_bill_no"));
		firstOption.put("BILL_NO1", "0");
		
		Map lastOption = new HashMap();
		lastOption.put("BILL_NAME", rb.getString("other_bill_no"));
		lastOption.put("BILL_NO1", "9");
		
		//调用业务逻辑处理
		List subList = this.getSubList((CorpUser) this.getUser(), merchant);
		subList.add(0, firstOption);
		if (!merchant.equals("0")) {
			subList.add(lastOption);
		}
		
		//一个element对应一个select下拉框
		SubElement element =  mapList2Selector(subList, subListId, "BILL_NAME", "BILL_NO1");
		
		//加入到select框列表
		List elementList = new ArrayList();
		elementList.add(element);
		
		this.addSubResponseListByDefaultType(elementList);
	}
	
	//业务逻辑处理函数
	private List getSubList(CorpUser corpUser, String merchant) throws Exception {
		String sql = "select BILL_NO1, concat(BILL_NO1,concat(' - ', BILL_NAME)) as BILL_NAME from BILL_PAYMENT where CORP_ID = ? and MERCHANT = ? and (PAY_TYPE = ? or PAY_TYPE = ?) and STATUS = ?";
		return this.genericJdbcDao.query(sql, new Object[]{corpUser.getCorpId(), merchant, BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE, BillPayment.PAYMENT_TYPE_CARD_TEMPLATE, Constants.STATUS_NORMAL});
	}

}
