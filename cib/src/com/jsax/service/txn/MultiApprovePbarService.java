package com.jsax.service.txn;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.jsax.core.NotLogined;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.utils.RBFactory;

/**
 * @author Kevin.Wen 20130309
 * Multi Approve by showing Progress Bar
 */
public class MultiApprovePbarService extends JsaxAction implements JsaxService,NotLogined {
	
	public void doTransaction() throws Exception {
		this.setTargetType(TARGET_TYPE_ELEMENT);
		
		/*RBFactory rb = RBFactory.getInstance("app.cib.resource.flow.progress_bar_status");
		String workId = getParameter("workId");
		// �ж������Ƿ��Ѿ���ɴ���(��������ɹ���ʧ��)
		Map worksStatus4pb = (HashMap)getResultData().get("worksStatus4pb");
		if(worksStatus4pb.get(workId)!=null){
			this.addSubResponseListByDefaultType("bp_"+workId,rb.getString("taskDone"));
		}else{
			this.addSubResponseListByDefaultType("bp_"+workId,rb.getString("taskBeingProcessed"));
		}*/
		//add by linrui for mul-language20171116
		String lang = getParameter("language");
		Locale language;
		if (null == lang ||"".equals(lang)){
		language = Config.getDefaultLocale();
		}else{
		language = new Locale(lang.substring(0,2),lang.substring(3,5));
		}
		//end
//        RBFactory rb = RBFactory.getInstance("app.cib.resource.flow.progress_bar_status");
        RBFactory rb = RBFactory.getInstance("app.cib.resource.flow.progress_bar_status" ,language.toString());//mod by linrui 20180517
		
		//�Ѵ�����ϵ�����
		Map worksStatus4pb = (HashMap)getResultData().get("worksStatus4pb");
		//��������
		List checkoutWorks = (List) getResultData().get("checkoutWorks");
		
		
		int	allCount=checkoutWorks.size();
		int	doneCount=0;
		for (int i=0;i<checkoutWorks.size();i++){
        	Map map = (HashMap) checkoutWorks.get(i);
        	String workId = (String) map.get("workId");
        	 //�ж������Ƿ��Ѿ���ɴ���(��������ɹ���ʧ��)
    		if(worksStatus4pb.get(workId)!=null){
    			doneCount++;
    			this.addSubResponseListByDefaultType("bp_"+workId,rb.getString("taskDone"));
    			Log.info("MultiApprovePbarService()* work done: "+workId);
    		}else{
    			this.addSubResponseListByDefaultType("bp_"+workId,rb.getString("taskBeingProcessed"));
    		}
        }
		
		this.addSubResponseListByDefaultType("parPercent",""+((int)((double)doneCount/allCount*100)));
		
		Log.info("MultiApprovePbarService()* work count: "+allCount);
		Log.info("MultiApprovePbarService()* work finished: "+doneCount);
		Log.info("MultiApprovePbarService()* work parPercent: "+((int)((double)doneCount/allCount*100))+"%");
		
	}
	

	public String processNotLogined(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
