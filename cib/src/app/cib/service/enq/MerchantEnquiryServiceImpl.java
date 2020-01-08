package app.cib.service.enq;

import java.util.*;

import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.*;

/**
 * 
 * @author wen_chy
 *
 */
public class MerchantEnquiryServiceImpl implements MerchantEnquiryService {
	
	private GenericJdbcDao genericJdbcDao;

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	/**
	 * list summary information
	 */
	public List listSummary(String date,
			String merchantId,String merchantGroup,
			String merchantType) throws NTBException {
		
		String sql = "select * from MERCHANT_DATA_SUMMARY where POSTING_DATE=? and MERCHANT_GROUP=? and MERCHANT_ID=? ";
		
		if(!"".equals(merchantType)){
			sql=sql+" and MERCHANT_TYPE in "+this.merchantTypeFormat(merchantType);
		}else{
			sql=sql+" and 1=0";//if merchantType is empty,then nothing can be enquiry
		}
		
		try{
			List summaryList=new ArrayList();		
			summaryList = genericJdbcDao.query(sql, new Object[] {date,merchantGroup,merchantId});
			return summaryList;
		} catch (Exception e) {
			Log.error("MerchantEnquiryServiceImpl.listSummary ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	
	/**
	 * to set merchant name for merchant id
	 * @param dataMap
	 * @param date
	 * @param merchantId
	 * @param merchantGroup
	 * @param merchantType
	 * @throws NTBException
	 */
	public void setMerNameForSum(Map dataMap, String date,
			String merchantId,String merchantGroup,
			String merchantType) throws NTBException {
		
		String sql = "select MERCHANT_NAME MERNAME from MERCHANT_DATA_SUMMARY where POSTING_DATE=? and MERCHANT_GROUP=? and MERCHANT_ID=? ";
		
		if(!"".equals(merchantType)){
			sql=sql+" and MERCHANT_TYPE in "+this.merchantTypeFormat(merchantType);
		}else{
			sql=sql+" and 1=0";//if merchantType is empty,then nothing can be enquiry
		}
		
		try{
			List list=new ArrayList();		
			list = genericJdbcDao.query(sql, new Object[] {date,merchantGroup,merchantId});
			if(list!=null&&list.size()>0){
				Map map = (HashMap)list.get(0);
				dataMap.put("MERNAME", map.get("MERNAME"));
			}else{
				dataMap.put("MERNAME", "");
			}
		} catch (Exception e) {
			Log.error("MerchantEnquiryServiceImpl.setMerchantName ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	
	/**
	 * calculate amount of summary list
	 * @param date
	 * @param merchantId
	 * @param merchantGroup
	 * @param merchantType
	 * @return
	 * @throws NTBException
	 */
	public List listSummarySum(String date,
			String merchantId,String merchantGroup,
			String merchantType) throws NTBException {
		
		String sql = "select merchant_id MERID,sum(sales_amount) SASUM,sum(sales_count) SCSUM,sum(return_amount) RASUM,sum(return_count) RCSUM,sum(total_commission) TCSUM,sum(net_amount) NASUM " +
				"from MERCHANT_DATA_SUMMARY where POSTING_DATE=? and MERCHANT_GROUP=? ";
	
		if(!"".equals(merchantType)){
			sql=sql+" and MERCHANT_TYPE in "+this.merchantTypeFormat(merchantType);
		}else{
			sql=sql+" and 1=0";//if merchantType is empty,then nothing can be enquiry
		}
		
		if(!"all".equals(merchantId)){	
			sql=sql+" and MERCHANT_ID='"+merchantId+"'";
		}
		
		sql=sql+" group by merchant_id";
		
		Log.info("listSummarySum sql-srcipt ***"+sql+"***");
		Log.info("listSummarySum sql-srcipt ? "+date);
		Log.info("listSummarySum sql-srcipt ? "+merchantGroup);
		
		try{
			List summarySumList=new ArrayList();		
			summarySumList = genericJdbcDao.query(sql, new Object[] {date,merchantGroup});
			return summarySumList;
		} catch (Exception e) {
			Log.error("MerchantEnquiryServiceImpl.listSummarySum ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	
	/**
	 * get merchant name for merchant id
	 * @param dateType
	 * @param date
	 * @param merchantId
	 * @param merchantGroup
	 * @param merchantType
	 * @return
	 * @throws NTBException
	 */
	public String getMerNameForDet(String dateType, String date,
			String merchantId,String merchantGroup,
			String merchantType) throws NTBException {
		
		String merchatName="";
		
		String sql = "select MERCHANT_NAME MERNAME from MERCHANT_DATA_DETAIL where ";
		if(dateType=="t"){
			sql=sql+" TRANSACTION_DATE='"+date+"'";
		}else{
			sql=sql+" SETTLEMENT_DATE='"+date+"'";
		}
		
		sql=sql+" and MERCHANT_GROUP=? and MERCHANT_ID=? ";
		
		if(!"".equals(merchantType)){
			sql=sql+" and MERCHANT_TYPE in "+this.merchantTypeFormat(merchantType);
		}else{
			sql=sql+" and 1=0";//if merchantType is empty,then nothing can be enquiry
		}
		
		try{
			List list=new ArrayList();		
			list = genericJdbcDao.query(sql, new Object[] {merchantGroup,merchantId});
			if(list!=null&&list.size()>0){
				Map map = (HashMap)list.get(0);
				merchatName = (String)map.get("MERNAME");
				if(merchatName==null){
					merchatName = "";
				}
			}
		} catch (Exception e) {
			Log.error("MerchantEnquiryServiceImpl.getMerNameForDet ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
		
		return merchatName;
	}
	
	/**
	 * list detail information except Settlement
	 */
	public List listDetail(String dateType, String date,
			String merchantId,String merchantGroup,
			String merchantType,String terminalId) throws NTBException {
		
		String sql = "select * from MERCHANT_DATA_DETAIL where ";
		if(dateType=="t"){
			sql=sql+" TRANSACTION_DATE='"+date+"'";
		}else{
			sql=sql+" SETTLEMENT_DATE='"+date+"'";
		}
		sql=sql+" and MERCHANT_GROUP=? and MERCHANT_ID=? ";
		
		if(!"".equals(merchantType)){
			sql=sql+" and MERCHANT_TYPE in "+this.merchantTypeFormat(merchantType);
		}else{
			sql=sql+" and 1=0";//if merchantType is empty,then nothing can be enquiry
		}
		
		sql=sql+" and TERMINAL_ID=? ";
		
		sql=sql+" order by RETRIEVAL_NUMBER ";
		
		try{
			List detailList=new ArrayList();		
			detailList = genericJdbcDao.query(sql, new Object[] {merchantGroup,merchantId,terminalId});
			return detailList;
		} catch (Exception e) {
			Log.error("MerchantEnquiryServiceImpl.listDetail ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	
	/**
	 * get detail information of "Settlement" as sum value
	 */
	/*public Map setSettlementAsSum(Map map,String dateType, String date,
			String merchantId,String merchantGroup,
			String merchantType,String terminalId) throws NTBException {
		
		map.put("ROWSUM", "");
		map.put("TASUM", "");
		
		String sql = "select CARD_NUMBER ROWSUM,TRANSACTION_AMOUNT TASUM from MERCHANT_DATA_DETAIL where ";
		if(dateType=="t"){
			sql=sql+" TRANSACTION_DATE='"+date+"'";
		}else{
			sql=sql+" SETTLEMENT_DATE='"+date+"'";
		}
		sql=sql+" and MERCHANT_GROUP=? and MERCHANT_ID=? ";
		
		if(!"".equals(merchantType)){
			sql=sql+" and MERCHANT_TYPE in "+this.merchantTypeFormat(merchantType);
		}else{
			sql=sql+" and 1=0";//if merchantType is empty,then nothing can be enquiry
		}
		
		sql=sql+" and TERMINAL_ID=? ";
		
		sql=sql+" and TRANSACTION_TYPE=? ";
		
		try{
			List detailList=new ArrayList();		
			detailList = genericJdbcDao.query(sql, new Object[] {merchantGroup,merchantId,terminalId,"Settlement"});
			if(detailList!=null&&detailList.size()>0){
				Map detailMap = (HashMap)detailList.get(0);
				map.put("ROWSUM", detailMap.get("ROWSUM"));
				map.put("TASUM", detailMap.get("TASUM"));
			}
			return map;
		} catch (Exception e) {
			Log.error("MerchantEnquiryServiceImpl.listDetail ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
	}*/
	
	/**
	 * calculate amount of detail list
	 * @param dateType
	 * @param date
	 * @param merchantId
	 * @param merchantGroup
	 * @param merchantType
	 * @return
	 * @throws NTBException
	 */
	public List listDetailSum(String dateType, String date,
			String merchantId,String merchantGroup,
			String merchantType) throws NTBException {
		
		String sql = "select TERMINAL_ID TERID,count(TERMINAL_ID) ROWSUM,sum(TRANSACTION_AMOUNT) TASUM from MERCHANT_DATA_DETAIL where ";
		if(dateType=="t"){
			sql=sql+" TRANSACTION_DATE='"+date+"'";
		}else{
			sql=sql+" SETTLEMENT_DATE='"+date+"'";
		}
		
		sql=sql+" and MERCHANT_GROUP=? and MERCHANT_ID=? ";
		
		if(!"".equals(merchantType)){
			sql=sql+" and MERCHANT_TYPE in "+this.merchantTypeFormat(merchantType);
		}else{
			sql=sql+" and 1=0";//if merchantType is empty,then nothing can be enquiry
		}
		
		sql=sql+" and TRANSACTION_TYPE<>? ";//skip TRANSACTION_TYPE='Settlement'
		
		sql=sql+" group by TERMINAL_ID";
		
		Log.info("listDetailSum sql-srcipt ***"+sql+"***");
		Log.info("listDetailSum sql-srcipt ? "+merchantGroup);
		Log.info("listDetailSum sql-srcipt ? "+merchantId);
		Log.info("listDetailSum sql-srcipt ? Settlement");
		
		try{
			List detailList=new ArrayList();		
			detailList = genericJdbcDao.query(sql, new Object[] {merchantGroup,merchantId,"Settlement"});
			return detailList;
		} catch (Exception e) {
			Log.error("MerchantEnquiryServiceImpl.listDetailSum ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
	}
	
	/**
	 * calculate amount of detail list
	 * @param dateType
	 * @param date
	 * @param merchantId
	 * @param merchantGroup
	 * @param merchantType
	 * @return
	 * @throws NTBException
	 */
	/*public List listDetailSum_bak20100226(String dateType, String date,
			String merchantId,String merchantGroup,
			String merchantType) throws NTBException {
		
		String sql = "select TERMINAL_ID TERID from MERCHANT_DATA_DETAIL where ";
		if(dateType=="t"){
			sql=sql+" TRANSACTION_DATE='"+date+"'";
		}else{
			sql=sql+" SETTLEMENT_DATE='"+date+"'";
		}
		
		sql=sql+" and MERCHANT_GROUP=? and MERCHANT_ID=? ";
		
		if(!"".equals(merchantType)){
			sql=sql+" and MERCHANT_TYPE in "+this.merchantTypeFormat(merchantType);
		}else{
			sql=sql+" and 1=0";//if merchantType is empty,then nothing can be enquiry
		}
		
		sql=sql+" group by TERMINAL_ID";
		
		try{
			List detailList=new ArrayList();		
			detailList = genericJdbcDao.query(sql, new Object[] {merchantGroup,merchantId});
			return detailList;
		} catch (Exception e) {
			Log.error("MerchantEnquiryServiceImpl.listDetailSum ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
	}*/
	
	/**
	 * list all the merchantId base on the merchantGroup
	 * @param merchantGroup
	 * @return
	 * @throws NTBException
	 */
	public List listMerchantIdByGroup(String merchantGroup) throws NTBException {
		String sql = "select MERCHANT_ID from MERCHANT_ID where MERCHANT_GROUP=? order by MERCHANT_ID asc";
		try{
			List merchantIdList=new ArrayList();
			if(merchantGroup!=null&&!"".equals(merchantGroup.trim())){
				merchantIdList = genericJdbcDao.query(sql, new Object[] {merchantGroup});
			}
			return merchantIdList;
		} catch (Exception e) {
			Log.error("MerchantEnquiryServiceImpl.listMerchantIdByGroup ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}	
	}
	
	/**
	 * get merchantGroup by userId
	 * @param userId
	 * @return
	 * @throws NTBException
	 */
	public String getMerchantGroupByUser(String userId) throws NTBException {
		
		String sql = "select MERCHANT_GROUP from CORPORATION c,CORP_USER u where u.user_id=? and u.corp_id=c.corp_id";
		try{
			String merchantGroup=null;
			if(userId!=null&&!"".equals(userId.trim())){
				merchantGroup = (String)genericJdbcDao.querySingleValue(sql, new Object[] {userId});
			}
			return merchantGroup;
		} catch (Exception e) {
			Log.error("MerchantEnquiryServiceImpl.getMerchantGroupByUser ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}		
	}
	
   public String getMerchantTypeByUser(String userId) throws NTBException {
		
		String sql = "select MERCHANT_TYPE from CORP_USER where USER_ID=? ";
		try{
			String merchantType=null;
			if(userId!=null&&!"".equals(userId.trim())){
				merchantType = (String)genericJdbcDao.querySingleValue(sql, new Object[] {userId});
			}
			return merchantType;
		} catch (Exception e) {
			Log.error("MerchantEnquiryServiceImpl.getMerchantTypeByUser ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}		
	}
	
	/**
	 * process detail list
	 * @param dateType
	 * @param date
	 * @param merchantId
	 * @param merchantGroup
	 * @param merchantType
	 * @return
	 * @throws NTBException
	 */
	/*public List listDetailFilter_bak20100226(String dateType, String date,
			String merchantId,String merchantGroup,
			String merchantType) throws NTBException {
		
        List detailFilterList=new ArrayList();
        
        try{
        	
        	List detailSumList=this.listDetailSum(dateType, date, merchantId, merchantGroup, merchantType);
            if(detailSumList!=null&&detailSumList.size()>0){
            	for(int i=0;i<detailSumList.size();i++){       		  		
            		Map detailCell=(HashMap)detailSumList.get(i);      		
            		String terminalIdSum=(String)detailCell.get("TERID");
            		//sum
            		this.setSettlementAsSum(detailCell, dateType, date, merchantId, merchantGroup, merchantType, terminalIdSum);
            		//list info
            		List detailList=this.listDetail(dateType, date, merchantId, merchantGroup, merchantType, terminalIdSum);	
            		
            		detailCell.put("detailList", detailList);
            		
            		detailFilterList.add(detailCell);
            	}
            }
            
		} catch (Exception e) {
			Log.error("MerchantEnquiryServiceImpl.listDetailFilter ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
		
		return detailFilterList;
	}*/
	
	/**
	 * process detail list
	 * @param dateType
	 * @param date
	 * @param merchantId
	 * @param merchantGroup
	 * @param merchantType
	 * @return
	 * @throws NTBException
	 */
	public List listDetailFilter(String dateType, String date,
			String merchantId,String merchantGroup,
			String merchantType) throws NTBException {
		
        Log.info("MerchantEnquiryService >> listDetailFilter");
        Log.info("listDetailFilter.dateType: " + dateType);
		Log.info("listDetailFilter.date: " + date);
		Log.info("listDetailFilter.merchantId: " + merchantId);
		Log.info("listDetailFilter.merchantGroup: " + merchantGroup);
		Log.info("listDetailFilter.merchantType: '" + merchantType+"'");
		
		List detailFilterList=new ArrayList();
        
        try{
        	
        	Log.info("listDetailFilter >> listDetailSum");
        	List detailSumList=this.listDetailSum(dateType, date, merchantId, merchantGroup, merchantType);
        	
        	if(detailSumList!=null&&detailSumList.size()>0){
        		Log.info("listDetailSum.detailSumList size: "+detailSumList.size());
            	for(int i=0;i<detailSumList.size();i++){       		  		
            		Map detailCell=(HashMap)detailSumList.get(i);      		
            		String terminalIdSum=(String)detailCell.get("TERID");
            		Log.info("detailSumList("+i+").terminalId: "+terminalIdSum);
            		List detailList=this.listDetail(dateType, date, merchantId, merchantGroup, merchantType, terminalIdSum);	
            		detailCell.put("detailList", detailList);
            		
            		detailFilterList.add(detailCell);
            	}
            }
            
		} catch (Exception e) {
			Log.error("MerchantEnquiryServiceImpl.listDetailFilter ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
		
		return detailFilterList;
	}
	
	/**
	 * process summary list
	 * @param date
	 * @param merchantId
	 * @param merchantGroup
	 * @param merchantType
	 * @return
	 * @throws NTBException
	 */
	public List listSummaryFilter(String date,
			String merchantId,String merchantGroup,
			String merchantType) throws NTBException {	
		
		Log.info("MerchantEnquiryService >> listSummaryFilter");
		
		Log.info("listSummaryFilter.date: " + date);
		Log.info("listSummaryFilter.merchantId: " + merchantId);
		Log.info("listSummaryFilter.merchantGroup: " + merchantGroup);
		Log.info("listSummaryFilter.merchantType: '" + merchantType+"'");
        
		List summaryFilterList=new ArrayList();
        
        try{
        	
        	Log.info("listSummaryFilter >> listSummarySum");
        	List summarySumList=this.listSummarySum(date, merchantId, merchantGroup, merchantType);
        	
            if(summarySumList!=null&&summarySumList.size()>0){
            	Log.info("listSummarySum.summarySumList size: "+summarySumList.size());
            	for(int i=0;i<summarySumList.size();i++){       		  		
            		Map summaryCell=(HashMap)summarySumList.get(i);      		
            		String merchantIdSum=(String)summaryCell.get("MERID");
            		Log.info("summarySumList("+i+").merchantId: "+merchantIdSum);
            		//set merchant name for merchant id
            		this.setMerNameForSum(summaryCell, date, merchantIdSum, merchantGroup, merchantType);
            		//list data for each merchant id
            		List summaryList=this.listSummary(date, merchantIdSum, merchantGroup, merchantType);        		
            		summaryCell.put("summaryList", summaryList);
            		
            		summaryFilterList.add(summaryCell);
            	}
            }
            
		} catch (Exception e) {
			Log.error("MerchantEnquiryServiceImpl.listSummaryFilter ERROR :", e);
			throw new NTBException("err.sys.DBError");
		}
		
		return summaryFilterList;
	}
	
	public String merchantTypeFormat(String merchantType){
		String merchantTypeFormat="(";
		
		String[] merType=merchantType.split(",");
		for(int i=0;i<merType.length;i++){
			String merTypeCell=merType[i];
			if(i==merType.length-1){
				merchantTypeFormat=merchantTypeFormat+"'"+merTypeCell+"'";
			}else{
				merchantTypeFormat=merchantTypeFormat+"'"+merTypeCell+"',";
			}			
		}
		
		merchantTypeFormat=merchantTypeFormat+")";
		
		return merchantTypeFormat;
	}

}
