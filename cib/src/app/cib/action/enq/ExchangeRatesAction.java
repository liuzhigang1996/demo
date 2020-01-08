/**
 * @author hjs
 * 2006-9-9
 */
package app.cib.action.enq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.service.enq.ExchangeRatesService;

/**
 * @author hjs
 * 2006-9-9
 */
public class ExchangeRatesAction extends CibAction {
	
	public void listExchangeRates() throws NTBException {
        //设置空的 ResultData 清空显示数据
        setResultData(new HashMap(1));
        ApplicationContext appContext = Config.getAppContext();
        ExchangeRatesService eRateService = (ExchangeRatesService)appContext.getBean("ExchangeRatesService");
        
        String zoneCode = Utils.null2EmptyWithTrim(this.getParameter("zoneCode"));
        if (zoneCode.equals("")) {
        	zoneCode = "AP";
        }
        
        List eRatesList = eRateService.listERateByZoneCode(zoneCode, ((CorpUser) this.getUser()).getCorpId());
        
        Map resultData = new HashMap();
        resultData.put("eRatesList", eRatesList);
        resultData.put("zoneCode", zoneCode);
        this.setResultData(resultData);
	}

}
