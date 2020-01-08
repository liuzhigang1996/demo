package app.cib.dao.srv;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.neturbo.set.core.Config;
import com.neturbo.set.database.GenericHibernateDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

public class RequestDao extends GenericHibernateDao {
	//add by hjs 20070412
	private static final String defalutPattern = Config.getProperty("DefaultDatePattern");

	// add by mxl 1101
	public List listReqBankDraft(String batchId) {
		String tableName = "ReqBankDraft";
		List valueList = new ArrayList();
		String hql = "from " + tableName
				+ " as tb where tb.batchId= ? order by tb.lineNo";
		valueList.add(batchId);
		return this.list(hql, valueList.toArray());

	}

	// add by mxl 1101
	public List listReqCashierOrder(String batchId) {
		String tableName = "ReqCashierOrder";
		List valueList = new ArrayList();
		String hql = "from " + tableName
				+ " as tb where tb.batchId= ? order by tb.lineNo";
		valueList.add(batchId);
		return this.list(hql, valueList.toArray());

	}

	// add by mxl 1101
	public List listReqStopCheque(String batchId) {
		String tableName = "ReqStopCheque";
		List valueList = new ArrayList();
		String hql = "from " + tableName
				+ " as tb where tb.batchId= ? order by tb.lineNo";
		valueList.add(batchId);
		return this.list(hql, valueList.toArray());

	}

	// add by mxl 1101
	public List listReqChequeProtection(String batchId) {
		String tableName = "ReqChequeProtection";
		List valueList = new ArrayList();
		String hql = "from " + tableName
				+ " as tb where tb.batchId= ? order by tb.lineNo";
		valueList.add(batchId);
		return this.list(hql, valueList.toArray());

	}

	// add by mxl 1215
	public List listFileRequestBythreekeys(String corpId, String orderDate,
			String batchReference) {

		String tableName = "FileRequest";
		List valueList = new ArrayList();
		String hql = "from " + tableName + " as tb where tb.orderDate= ? ";
		valueList.add(orderDate);

		hql = hql + "and  tb.corpId = ? ";
		valueList.add(corpId);

		hql = hql + "and  tb.batchReference = ? ";
		valueList.add(batchReference);

		return this.list(hql, valueList.toArray());

	}

	// add by mxl 0124
	public List listReqStopChequeHistory(Date dateFrom, Date dateTo,
			String corpID, String userID, String chequeNumber) {
		String hql = "from ReqStopCheque as tb where tb.corpId = ?";
		List valueList = new ArrayList();
		valueList.add(corpID);
		if ((dateFrom != null) && (!dateFrom.equals(""))) {
			hql = hql + "tb.requestTime > ? ";
			valueList.add(dateFrom);
		}

		if ((dateTo != null) && (!dateTo.equals(""))) {
			hql = hql + "and tb.requestTime < ? ";
			valueList.add(dateTo);
		}
		if ((userID != null) && (!userID.equals(""))) {
			hql = hql + "and  tb.userId = ? ";
			valueList.add(userID);
		}
		if ((chequeNumber != null) && (!chequeNumber.equals(""))) {
			hql = hql + "and  tb.chequeNumber = ? ";
			valueList.add(chequeNumber);
		}
		hql += "order by tb.requestTime DESC";
		return this.list(hql, valueList.toArray());

	}

	// add by mxl 0124
	public List listReqChequeProtectionHistory(Date dateFrom, Date dateTo,
			String corpID, String userID, String chequeNumber) {
		String hql = "from ReqChequeProtection as tb where tb.corpId = ?";
		List valueList = new ArrayList();
		valueList.add(corpID);
		if ((dateFrom != null) && (!dateFrom.equals(""))) {
			hql = hql + "tb.requestTime > ? ";
			valueList.add(dateFrom);
		}

		if ((dateTo != null) && (!dateTo.equals(""))) {
			hql = hql + "and tb.requestTime < ? ";
			valueList.add(dateTo);
		}
		if ((userID != null) && (!userID.equals(""))) {
			hql = hql + "and  tb.userId = ? ";
			valueList.add(userID);
		}
		if ((chequeNumber != null) && (!chequeNumber.equals(""))) {
			hql = hql + "and  tb.chequeNumber = ? ";
			valueList.add(chequeNumber);
		}
		hql += "order by tb.requestTime DESC";
		return this.list(hql, valueList.toArray());

	}
	
	// add by hjs 20070412
	public List listRecords(String corpId, String userId, String batchType,
			String fromAcc,	String dateFrom, String dateTo) throws NTBException {

		String hql = "from FileRequest as t where t.corpId = ? ";
		List valueList = new ArrayList();

		valueList.add(corpId);

		if (!Utils.null2EmptyWithTrim(userId).equals("")) {
			hql += "and t.userId = ? ";
			valueList.add(userId);
		}
		if (!Utils.null2EmptyWithTrim(batchType).equals("")) {
			hql += "and t.batchType = ? ";
			valueList.add(batchType);
		}
		if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
			dateFrom = DateTime.formatDate(dateFrom, defalutPattern, "yyyy-MM-dd");
			Timestamp timeFrom = DateTime.getTimestampByStr(dateFrom, true);
			hql += "and t.requestTime >= ? ";
			valueList.add(timeFrom);
		}
		if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
			dateTo = DateTime.formatDate(dateTo, defalutPattern, "yyyy-MM-dd");
			Timestamp timeTo = DateTime.getTimestampByStr(dateTo, false);
			hql += "and t.requestTime <= ? ";
			valueList.add(timeTo);
		}
		if (!Utils.null2EmptyWithTrim(fromAcc).equals("") && !Utils.null2EmptyWithTrim(fromAcc).equals("0")) {
			hql += "and t.fromAccount = ? ";
			valueList.add(fromAcc);
		}

		hql += "order by t.requestTime, t.fromAccount";

		return this.list(hql, valueList.toArray());
		
	}

}
