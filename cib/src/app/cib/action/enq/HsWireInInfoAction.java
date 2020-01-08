/**
 * @author hjs
 * 2006-9-6
 */
package app.cib.action.enq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

import app.cib.bo.enq.HsWireInInfo;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.service.enq.HsWireInInfoService;

/**
 * @author hjs
 * 2006-9-6
 */
public class HsWireInInfoAction extends CibAction {
	
	public void listWireInInfo() throws NTBException {
        //设置空的 ResultData 清空显示数据
        setResultData(new HashMap(1));
        ApplicationContext appContext = Config.getAppContext();
        HsWireInInfoService wireInService = (HsWireInInfoService)appContext.getBean("HsWireInInfoService");
        
        CorpUser corpUser = (CorpUser) this.getUser();
        
        String currency = Utils.null2EmptyWithTrim(this.getParameter("currency"));
        
        List wireInfoList = wireInService.listWireInInfo(corpUser.getCorpId(), currency.equals("") ? "USD" : currency);
        wireInfoList = this.convertPojoList2MapList(wireInfoList);
        
        Map resultData = new HashMap();
        resultData.put("wireInfoList", wireInfoList);
        resultData.put("currency", currency);
        this.setResultData(resultData);
	}
	
	public void viewWireInInfo() throws NTBException {
        //设置空的 ResultData 清空显示数据
        setResultData(new HashMap(1));
        ApplicationContext appContext = Config.getAppContext();
        HsWireInInfoService wireInService = (HsWireInInfoService)appContext.getBean("HsWireInInfoService");
        
        String seqNo = this.getParameter("seqNo");
        
        HsWireInInfo wireInInfo = wireInService.viewWireInInfo(seqNo);

        Map resultData = new HashMap();           
        this.convertPojo2Map(wireInInfo, resultData);
        this.setResultData(resultData);
	}
}
