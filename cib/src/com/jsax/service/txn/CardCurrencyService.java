/**
 * @author hjs
 * 2006-9-5
 */
package com.jsax.service.txn;

import java.util.Map;


import org.springframework.context.ApplicationContext;

import app.cib.bo.sys.CorpUser;
import app.cib.service.txn.BillPaymentService;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.neturbo.set.core.Config;

/**
 * @author hjs
 * 2006-9-5
 */
public class CardCurrencyService extends JsaxAction implements JsaxService {

	/* (non-Javadoc)
	 * @see com.jsax.core.JsaxService#performService(javax.servlet.http.HttpServletRequest, com.neturbo.set.core.NTBUser)
	 */
	public void doTransaction() throws Exception {
		this.setTargetType(TARGET_TYPE_ELEMENT);
		this.setTargetId(getParameter("targetId"));
		
		String cardNo = getParameter("cardNo");
		
        ApplicationContext appContext = Config.getAppContext();
        BillPaymentService billPaymentService = (BillPaymentService)appContext.getBean("BillPaymentService");
		
		//get infomation from host
		Map fromHost= billPaymentService.getCardPaymentInfo((CorpUser) this.getUser(), cardNo);
		String currency = fromHost.get("CARD_CURRENCY").toString();
		
		//加入到sub response列表
		this.addSubResponseListByDefaultType(getTargetId(), currency + " - ");
		
	}

}
