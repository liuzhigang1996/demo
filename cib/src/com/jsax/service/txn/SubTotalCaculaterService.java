/**
 * @author hjs
 * 2007-2-2
 */
package com.jsax.service.txn;

import java.math.BigDecimal;
import java.util.*;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.neturbo.set.core.Log;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 2007-2-2
 */
public class SubTotalCaculaterService extends JsaxAction implements JsaxService {

	public void doTransaction() throws Exception {
		
		String[] targetIdArray = this.getParameterValues("targetId");
		String[] amtCaArray = this.getParameterValues("amountCA");
		String[] amtTdArray = this.getParameterValues("amountTD");
		String[] amtOvArray = this.getParameterValues("amountOV");
		String[] amtLaArray = this.getParameterValues("amountLA");
		String[] amtSaArray = this.getParameterValues("amountSA");
		//add by long_zg 2014-12-16 for CR192 bob batch
		String[] amtCCArray = this.getParameterValues("amountCC");
		
		//add by liqun for mul-language20171129
		String lang = getParameter("language");
		
		Map resultData = this.getResultData();
		List csvList = (List) resultData.get("csvList");

		List amtList = new ArrayList();
		if(amtCaArray != null)  amtList.add(amtCaArray);
		if(amtTdArray != null)  amtList.add(amtTdArray);
		if(amtOvArray != null)  amtList.add(amtOvArray);
		if(amtLaArray != null)  amtList.add(amtLaArray);
		if(amtSaArray != null)  amtList.add(amtSaArray);
		//add by long_zg 2014-12-16 for CR192 bob batch
		if(amtCCArray != null)  amtList.add(amtCCArray);
		
		BigDecimal subTotal = null;
		BigDecimal amount = null;
		String[] amtArray = null;
		Map row = null;
		int k = 0;
		for(int j=0; j<amtList.size(); j++) {
			subTotal = new BigDecimal("0");
			amtArray = (String[]) amtList.get(j);
			for(int i=0; i<amtArray.length; i++) {
				Log.info("---------------------------------------" + amtArray[i] + " -------------------------------------");
				try{
					amount = new BigDecimal(Utils.replaceStrForCaculater(amtArray[i], ",", ""));
				}catch (Exception e) {
					amount = new BigDecimal("0");
				}
				subTotal = subTotal.add(amount);
				row = (Map) csvList.get(k++);
				row.put("TOTAL_LCY_BALANCE", amount);
			}
			String strSubTotal = Format.formatData(subTotal.toString(), "AMOUNT", null,lang);//mod by liqun 20171129
			row = (Map) csvList.get(k++);
			row.put("TOTAL_LCY_BALANCE", strSubTotal);
			//加入到sub response列表
			this.addSubResponseList(TARGET_TYPE_ELEMENT, targetIdArray[j], strSubTotal);
			
			resultData.put("csvList", csvList);
			this.setResultData(resultData);
		}

	}

}
