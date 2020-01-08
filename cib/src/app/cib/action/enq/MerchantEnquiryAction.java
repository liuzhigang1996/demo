package app.cib.action.enq;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.cib.core.CibAction;
import app.cib.service.enq.MerchantEnquiryService;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.Sorting;
import com.neturbo.set.utils.Utils;

/**
 * 
 * @author wen_chy
 *
 */
public class MerchantEnquiryAction extends CibAction {

	private static String defaultDatePattern = Config
			.getProperty("DefaultDatePattern");

	public void listSummary() throws NTBException {
		
		MerchantEnquiryService merchantEnquiryService = (MerchantEnquiryService)Config.getAppContext().getBean("MerchantEnquiryService");
		String merchantGroup = Utils.null2EmptyWithTrim(merchantEnquiryService.getMerchantGroupByUser(this.getUser().getUserId()));
		//cannot use 'null2EmptyWithTrim' here, for merchantType include ' '
		String merchantType = Utils.null2Empty(merchantEnquiryService.getMerchantTypeByUser(this.getUser().getUserId()));
		List merchantIdList = merchantEnquiryService.listMerchantIdByGroup(merchantGroup);
		
		Map resultData = this.getParameters();
		
		String queryFlag = this.getParameter("queryFlag");
		if(queryFlag!=null&&"true".equals(queryFlag)){
			
			String postingDate = Utils.null2EmptyWithTrim(this.getParameter("postingDate"));
			if (!"".equals(postingDate)) {
				postingDate = Format.formatDateTime(postingDate, defaultDatePattern,
						"yyyyMMdd");
			}
			String merchantId = this.getParameter("merchantId");
			List summaryFilterList = merchantEnquiryService.listSummaryFilter(postingDate, merchantId, merchantGroup, merchantType);
			resultData.put("summaryFilterList", summaryFilterList);//summary information
			resultData.put("merchantIdList", merchantIdList);//merchatId for combo box
			
			String downloadFlag="false";
			if(summaryFilterList!=null&&summaryFilterList.size()>0){
				downloadFlag="true";//if data exist,show the download flag	
			}
			resultData.put("downloadFlag", downloadFlag);
			
			List summaryDownloadList= this.summaryListDownloadFormat(summaryFilterList);
			resultData.put("summaryDownloadList",summaryDownloadList);//data for download
			
		}else{
			String dateDefault = this.lastWorkingDate(); //show last working day
			resultData.put("postingDate", dateDefault);
			resultData.put("merchantIdList", merchantIdList);//merchatId for combo box
		}
		
		setResultData(resultData);	
	}
	
	public void listDetail() throws NTBException {
		
		MerchantEnquiryService merchantEnquiryService = (MerchantEnquiryService)Config.getAppContext().getBean("MerchantEnquiryService");
		String merchantGroup = Utils.null2EmptyWithTrim(merchantEnquiryService.getMerchantGroupByUser(this.getUser().getUserId()));
        //cannot use 'null2EmptyWithTrim' here, for merchantType include ' '
		String merchantType = Utils.null2Empty(merchantEnquiryService.getMerchantTypeByUser(this.getUser().getUserId()));
		List merchantIdList = merchantEnquiryService.listMerchantIdByGroup(merchantGroup);
		
		Map resultData = this.getParameters();
		
		String queryFlag = this.getParameter("queryFlag");
		if(queryFlag!=null&&"true".equals(queryFlag)){
			
			String date = Utils.null2EmptyWithTrim(this.getParameter("date"));
			if (!"".equals(date)) {
				date = Format.formatDateTime(date, defaultDatePattern,
						"yyyyMMdd");
			}
			String dateType = this.getParameter("dateType");
			String merchantId = this.getParameter("merchantId");
			String merchantName = merchantEnquiryService.getMerNameForDet(dateType, date, merchantId, merchantGroup, merchantType);
			List detailFilterList = merchantEnquiryService.listDetailFilter(dateType, date, merchantId, merchantGroup, merchantType);
			resultData.put("merchantName", merchantName);//merchant name for show
			resultData.put("detailFilterList", detailFilterList);//detail information
			resultData.put("merchantIdList", merchantIdList);//merchatId for combo box
			
			String downloadFlag="false";
			if(detailFilterList!=null&&detailFilterList.size()>0){
				downloadFlag="true";//if data exist,show the download flag
			}
			resultData.put("downloadFlag", downloadFlag);
			
			List detailDownloadList= this.detailListDownloadFormat(detailFilterList, merchantId,merchantName,dateType);
			resultData.put("detailDownloadList",detailDownloadList);//data for download
			
		}else{
			String dateDefault = this.lastWorkingDate(); //show last working day
			resultData.put("date", dateDefault);
			resultData.put("merchantIdList", merchantIdList);//merchatId for combo box
		}
		
		setResultData(resultData);
	}

	public String lastWorkingDate() {
		Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());   
        int day =calendar.get(Calendar.DAY_OF_WEEK)-1;
        
        if(day==6){//Saturday
        	calendar.add(Calendar.DATE, -1);
        	SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");
        	return format.format(calendar.getTime());
        }else if(day==0){//Sunday
        	calendar.add(Calendar.DATE, -2);
        	SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");
        	return format.format(calendar.getTime());
        }else{//Monday - Friday
        	SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");
        	return format.format(calendar.getTime());
        }
        
	}
	
	public List summaryListDownloadFormat(List summaryFilterList){
		
		List summaryFormatList=new ArrayList();
		
		if(summaryFilterList!=null&&summaryFilterList.size()>0){
			for(int i=0;i<summaryFilterList.size();i++){
				
				Map map=(HashMap)summaryFilterList.get(i);
				
				Map map1=new HashMap();
				Map map2=new HashMap();
				Map map3=this.getSummaryMap();
				List list4=(List)map.get("summaryList");
				Map map5=new HashMap();
				
				
				map1.put("POSTING_DATE", "Merchant ID");
				map1.put("CARD_TYPE", map.get("MERID"));
				
				map2.put("POSTING_DATE", "Merchant Name");
				map2.put("CARD_TYPE", map.get("MERNAME"));
				
				map5.put("POSTING_DATE", "SUB-TOTAL");
				map5.put("SALES_COUNT", map.get("SCSUM"));
				map5.put("SALES_AMOUNT", map.get("SASUM"));
				map5.put("RETURN_COUNT", map.get("RCSUM"));
				map5.put("RETURN_AMOUNT", map.get("RASUM"));
				map5.put("TOTAL_COMMISSION", map.get("TCSUM"));
				map5.put("NET_AMOUNT", map.get("NASUM"));
				
				summaryFormatList.add(map1);
				summaryFormatList.add(map2);
				summaryFormatList.add(map3);
				summaryFormatList.addAll(list4);
				summaryFormatList.add(map5);
				summaryFormatList.add(new HashMap());
				
			}
		}
		
		return summaryFormatList;
	}
	
    public List detailListDownloadFormat(List detailFilterList,String merchantId,String merchantName,String dateType){
		
        List detailFormatList=new ArrayList();
		
		if(detailFilterList!=null&&detailFilterList.size()>0){
			for(int i=0;i<detailFilterList.size();i++){
				
				Map map=(HashMap)detailFilterList.get(i);
				
				Map map1=new HashMap();
				Map map2=new HashMap();
				Map map3=new HashMap();
				Map map4=this.getDetailMap(dateType);
				List list5=(List)map.get("detailList");
				Map map6=new HashMap();
				Map map7=new HashMap();
				
				if("s".equals(dateType)){
					map1.put("SETTLEMENT_DATE", "Merchant ID");
		    	}else{
		    		map1.put("TRANSACTION_DATE", "Merchant ID");
		    	}				
				map1.put("TRANSACTION_TYPE", merchantId);
				
				if("s".equals(dateType)){
					map2.put("SETTLEMENT_DATE", "Merchant Name");
		    	}else{
		    		map2.put("TRANSACTION_DATE", "Merchant Name");
		    	}				
				map2.put("TRANSACTION_TYPE", merchantName);
				
				if("s".equals(dateType)){
					map3.put("SETTLEMENT_DATE", "Terminal ID");
		    	}else{
		    		map3.put("TRANSACTION_DATE", "Terminal ID");
		    	}				
				map3.put("TRANSACTION_TYPE", map.get("TERID"));
				
				if("s".equals(dateType)){
					map6.put("SETTLEMENT_DATE", "Total Count");
		    	}else{
		    		map6.put("TRANSACTION_DATE", "Total Count");
		    	}
				map6.put("TRANSACTION_TYPE", map.get("ROWSUM"));
				
				if("s".equals(dateType)){
					map7.put("SETTLEMENT_DATE", "Settlement Amount");
		    	}else{
		    		map7.put("TRANSACTION_DATE", "Settlement Amount");
		    	}
				map7.put("TRANSACTION_TYPE", map.get("TASUM"));
				
				detailFormatList.add(map1);
				detailFormatList.add(map2);
				detailFormatList.add(map3);
				detailFormatList.add(map4);
				detailFormatList.addAll(list5);
				detailFormatList.add(map6);
				detailFormatList.add(map7);
				detailFormatList.add(new HashMap());
				
			}
		}
		
		return detailFormatList;
	}
    
    public Map getSummaryMap(){
    	Map map=new HashMap();
    	map.put("POSTING_DATE", "Settlement Date");
    	map.put("CARD_TYPE", "Card Type");
    	map.put("SALES_COUNT", "Sales Count");
    	map.put("SALES_AMOUNT", "Sales Amount");
    	map.put("RETURN_COUNT", "Refund Count");
    	map.put("RETURN_AMOUNT", "Refund Amount");
    	map.put("TOTAL_COMMISSION", "Commission");
    	map.put("NET_AMOUNT", "Net Amount");
    	return map;
    }
    
    public Map getDetailMap(String dateType){
    	Map map=new HashMap();
    	if("s".equals(dateType)){
    		map.put("SETTLEMENT_DATE", "Settlement Date");
    	}else{
    		map.put("TRANSACTION_DATE", "Transaction Date");
    	}
    	map.put("TRANSACTION_TYPE", "Transaction Type");
    	map.put("AUTHORIZED_NUMBER", "Authorization Code");
    	map.put("CARD_NUMBER", "Card Number");
    	map.put("TRANSACTION_CURRENCY", "CCY");
    	map.put("TRANSACTION_AMOUNT", "Transaction Amount");
    	return map;
    }
}
