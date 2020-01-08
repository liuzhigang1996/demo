/**
 * @author hjs
 * 2006-8-17
 */
package com.jsax.service.txn;

import java.math.BigDecimal;

import app.cib.bo.sys.CorpUser;
import app.cib.service.enq.ExchangeRatesService;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 2006-8-17
 */
public class EquivalentService extends JsaxAction implements JsaxService {
	
	private ExchangeRatesService exRatesService;

	/**
	 * @return the exRatesService
	 */
	public ExchangeRatesService getExRatesService() {
		return exRatesService;
	}

	/**
	 * @param exRatesService the exRatesService to set
	 */
	public void setExRatesService(ExchangeRatesService exRatesService) {
		this.exRatesService = exRatesService;
	}

	public  void doTransaction() throws Exception {
		//
		this.setTargetType(getParameter("targetType"));
		
		String[] targetId = getParameterValues("targetId");
		String[] fromCcy = getParameterValues("fromCcy");
		String[] fromAmt = getParameterValues("fromAmt");
		String toCcy = getParameter("toCcy");
        //add by linrui for mul-language20171116
		String lang = getParameter("language");
		NTBUser user = this.getUser();
		String corpId = "";
		if(user instanceof CorpUser){
			corpId = ((CorpUser)user).getCorpId();
		} else {
			corpId = getParameter("corpId");
		}
		
		if ((targetId.length != fromCcy.length) || (fromCcy.length != fromAmt.length)) {
			throw new NTBException("err.jsax.targetId.LengthException");
		} else {
			for (int i=0; i<fromCcy.length; i++) {
				String toAmt = getEquivalent(corpId, fromCcy[i], toCcy, new BigDecimal(fromAmt[i]), 2);		
				if ("pt_PT".equals(lang)) {// add by linrui for mul-language 20171116
					toAmt = toAmt.replaceAll(",", "");
					toAmt = Format.formatAmount(toAmt, null, lang);
				}
				//加入到sub response列表
				this.addSubResponseListByDefaultType(targetId[i], toAmt);
			}
		}
	}
	
	public String getEquivalent(String corpId, String fromCCY, String toCCY, BigDecimal fromAmount, int scale) throws NTBException {
        String strToAmt = "";
    	boolean isNegative = false;
    	if (fromAmount.doubleValue() < 0) {
    		isNegative = true;
    	}
    	fromAmount = fromAmount.abs();
		BigDecimal toAmount = exRatesService.getEquivalent(corpId, fromCCY, toCCY, fromAmount, null, scale);
		strToAmt = (toAmount.doubleValue()<0) ? 
				"--"
				:Format.formatData((isNegative?
						toAmount.multiply(new BigDecimal("-1"))
						:toAmount).toString(), "AMOUNT", null);
		return strToAmt;
	}

}
