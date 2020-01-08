/**
 * @author mxl
 * 2006-9-11
 */
package app.cib.action.enq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

import app.cib.bo.enq.InterestRateBean;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.enq.InterestRateService;

/**
 * @author mxl
 * 2006-9-11
 */
public class InterestRatesAction extends CibAction {
	
	public void listInterestRates() throws NTBException {
        //设置空的 ResultData 清空显示数据
        setResultData(new HashMap(1));
        ApplicationContext appContext = Config.getAppContext();
        InterestRateService interestRateService = (InterestRateService)appContext.getBean("InterestRateService");
                
        // Jet added for special interest rate 2008-06-04
		CorpUser user = (CorpUser) this.getUser();
		String corpId = user.getCorpId();

        List specialInterestRateList = interestRateService.listSpecialInterestRate(corpId);
        if (specialInterestRateList.size() > 0){
        	this.listSpecialInterestRates(corpId);
        	return;
        } 
        
        String currency = Utils.null2EmptyWithTrim(this.getParameter("currency"));
        if (currency.equals(null)||(currency.equals("")))
        {
        	currency = "MOP";
        }
        InterestRateBean interestRates = interestRateService.listInterestRate(currency,((CorpUser) this.getUser()).getCorpId());
        String effectiveDate = interestRates.getEffectiveDate();
        List interestRatesList = interestRates.getIRateList();
        String interestRate = interestRates.getSavingsRate();
        
        String label1 = interestRates.getPeriodLabel1();
        String label2 = interestRates.getPeriodLabel2();
        String label3 = interestRates.getPeriodLabel3();
        String label4 = interestRates.getPeriodLabel4();
        String label5 = interestRates.getPeriodLabel5();
        
        
        Map resultData = new HashMap();
        resultData.put("interestRatesList", interestRatesList);
        resultData.put("currency", currency);
        resultData.put("interestRate", interestRate);
        resultData.put("effectiveDate", effectiveDate);
        
        resultData.put("label1", label1);
        resultData.put("label1a", new Double(((new Double(label2)).doubleValue()-1)).toString());
        resultData.put("label2", label2);
        resultData.put("label2a", new Double(((new Double(label3)).doubleValue()-1)).toString());
        resultData.put("label3", label3);
        resultData.put("label3a", new Double(((new Double(label4)).doubleValue()-1)).toString());
        resultData.put("label4", label4);
        resultData.put("label4a",new Double(((new Double(label5)).doubleValue()-1)).toString());
        resultData.put("label5", label5);
        
        
        this.setResultData(resultData);
	}

	
    // Jet added for special interest rate 2008-06-04
	public void listSpecialInterestRates(String corpId) throws NTBException {
        //设置空的 ResultData 清空显示数据
        setResultData(new HashMap(1));
        ApplicationContext appContext = Config.getAppContext();
        InterestRateService interestRateService = (InterestRateService)appContext.getBean("InterestRateService");

        // currency list for display
        List currencyList = interestRateService.listCurrency(corpId);

        // 为了能够使用tag显示，list嵌套使用，第一重为period，第二重为某period下的各种currency的纪录
        // 同时，需要把没有的纪录补全，使之形成一个完整的方阵
        List specialInterestRateList = interestRateService.listPeriod(corpId);
        for (int i = 0; i < specialInterestRateList.size(); i ++){
        	Map periodMap = (Map)specialInterestRateList.get(i);
			String frequency = periodMap.get("FREQUENCY").toString();
			String unit = periodMap.get("UNIT").toString();

//			periodMap.put("PERIOD", frequency + unit);
			List rateListTemp = interestRateService.listRate(corpId, frequency, unit);
			
	        List rateList = new ArrayList();
			
			// fill in the absent record for integrated matrix
			for (int j = 0; j < currencyList.size(); j ++){
				boolean isExist = false; 
				String currencyAll = ((Map) currencyList.get(j)).get("CURRENCY").toString();

		        Map rateMap = new HashMap();
				rateList.add(j, rateMap);
				for (int k = 0; k < rateListTemp.size(); k ++){
					String currencySingle = ((Map) rateListTemp.get(k)).get("CURRENCY").toString();
					String interestRateSingle = ((Map) rateListTemp.get(k)).get("INTEREST_RATE").toString();
					if (currencySingle.equals(currencyAll)){
						rateMap.put("CURRENCY", currencySingle);
						rateMap.put("INTEREST_RATE", interestRateSingle);						
						isExist = true;
					}
				}
				if (!isExist){
					rateMap.put("CURRENCY", currencyAll);
					rateMap.put("INTEREST_RATE", "-");						
				}				
			}
			
			periodMap.put("rateList", rateList);
        }
        
        Map resultData = new HashMap();

        resultData.put("currencyList", currencyList);
        resultData.put("specialInterestRateList", specialInterestRateList);
        this.setResultData(resultData);
        this.setForward("listSpecialInterestRates");        
	}

}
