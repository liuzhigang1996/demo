/**
 * @author hjs
 * 2006-9-5
 */
package com.jsax.service.txn;

import java.util.Map;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.neturbo.set.database.GenericJdbcDao;

/**
 * @author hjs
 * 2006-9-5
 */
public class MerchantWebsiteService extends JsaxAction implements JsaxService {
	
	private GenericJdbcDao genericJdbcDao;

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	/* (non-Javadoc)
	 * @see com.jsax.core.JsaxService#performService(javax.servlet.http.HttpServletRequest, com.neturbo.set.core.NTBUser)
	 */
	public void doTransaction() throws Exception {
		this.setTargetType(TARGET_TYPE_OBJECT);
		this.setTargetId(getParameter("targetId"));
		
		String merchant = getParameter("merchant");
		String website = this.getMerWebsite(merchant);
		
		this.addSubResponseListByDefaultType(this.getTargetId(), website);
	}
	
	private String getMerWebsite(String merchant) throws Exception {
		String sql = "select MER_WEBSITE from HS_MERCHANT_WEBSITE where mer_code = ?";
		Map rowMap = this.genericJdbcDao.querySingleRow(sql, new Object[] {merchant});
		return rowMap.get("MER_WEBSITE").toString();
	}

}
