package app.cib.dao.flow;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import app.cib.service.flow.FlowEngineService;

import com.neturbo.set.core.Config;
import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

public class FlowWorkHisDao extends GenericHibernateDao {

	private static final String defalutPattern = Config.getProperty("DefaultDatePattern");
    
    // add by hjs 20070430 - for function(VIEW ALL TRANSACTION)
    public List listRecords(String corpId, String userId, String procStatus,
    		String dateFrom, String dateTo) throws NTBException {

		List valueList = new ArrayList();
    	String hql = "from FlowWorkHis as t where t.workStatus = ? and t.flowProcessHis.corpId = ? ";
    	
    	valueList.add(FlowEngineService.WORK_STATUS_FINISH);
    	valueList.add(corpId);

		if (!Utils.null2EmptyWithTrim(userId).equals("") && !Utils.null2EmptyWithTrim(userId).equals("0")) {
			hql += "and t.workDealer = ? ";
			valueList.add(userId);
		}
		if (!Utils.null2EmptyWithTrim(procStatus).equals("") && !Utils.null2EmptyWithTrim(procStatus).equals("0")) {
			hql += "and t.flowProcessHis.procStatus = ? ";
			valueList.add(procStatus);
		}
		if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
			dateFrom = DateTime.formatDate(dateFrom, defalutPattern, "yyyy-MM-dd");
			Timestamp timeFrom = DateTime.getTimestampByStr(dateFrom, true);
			hql += "and t.dealEndTime >= ? ";
			valueList.add(timeFrom);
		}
		if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
			dateTo = DateTime.formatDate(dateTo, defalutPattern, "yyyy-MM-dd");
			Timestamp timeTo = DateTime.getTimestampByStr(dateTo, false);
			hql += "and t.dealEndTime <= ? ";
			valueList.add(timeTo);
		}
		
		hql += "order by t.flowProcessHis.procId";

		return this.list(hql, valueList.toArray());

    }
    //add by linrui 20190916
    public List listRecords(String corpId, String userId, String procStatus,
    		String dateFrom, String dateTo, String sortOrder) throws NTBException {
    	sortOrder = sortOrder.equals("1")?"desc":"asc";
    	List valueList = new ArrayList();
    	String hql = "from FlowWorkHis as t where t.workStatus = ? and t.flowProcessHis.corpId = ? ";
    	
    	valueList.add(FlowEngineService.WORK_STATUS_FINISH);
    	valueList.add(corpId);
    	
    	if (!Utils.null2EmptyWithTrim(userId).equals("") && !Utils.null2EmptyWithTrim(userId).equals("0")) {
    		hql += "and t.workDealer = ? ";
    		valueList.add(userId);
    	}
    	if (!Utils.null2EmptyWithTrim(procStatus).equals("") && !Utils.null2EmptyWithTrim(procStatus).equals("0")) {
    		hql += "and t.flowProcessHis.procStatus = ? ";
    		valueList.add(procStatus);
    	}
    	if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
    		dateFrom = DateTime.formatDate(dateFrom, defalutPattern, "yyyy-MM-dd");
    		Timestamp timeFrom = DateTime.getTimestampByStr(dateFrom, true);
    		hql += "and t.dealEndTime >= ? ";
    		valueList.add(timeFrom);
    	}
    	if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
    		dateTo = DateTime.formatDate(dateTo, defalutPattern, "yyyy-MM-dd");
    		Timestamp timeTo = DateTime.getTimestampByStr(dateTo, false);
    		hql += "and t.dealEndTime <= ? ";
    		valueList.add(timeTo);
    	}
    	
    	hql += "order by t.flowProcessHis.procCreateTime " + sortOrder;
    	
    	return this.list(hql, valueList.toArray());
    	
    }

}
