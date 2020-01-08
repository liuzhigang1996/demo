package app.cib.dao.txn;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import app.cib.bo.txn.BillPayment;
import app.cib.util.*;

import com.neturbo.set.core.Config;
import com.neturbo.set.database.*;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs 2006-07-17
 */
public class BillPaymentDao extends GenericHibernateDao {

	private static final String defalutPattern = Config.getProperty("DefaultDatePattern");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/*
	 * list payment template which status is not "PAYMENT_STATUS_DELETE"
	 */
	public List listTemplate(String corpId, String merchant) throws NTBException {
		if (!corpId.equals("")) {

			List valueList = new ArrayList();

			String hql = "from BillPayment as bp where bp.corpId = ? and (bp.payType = ? or bp.payType = ?) and bp.status = ? ";

			valueList.add(corpId);
			valueList.add(BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE);
			valueList.add(BillPayment.PAYMENT_TYPE_CARD_TEMPLATE);
			valueList.add(Constants.STATUS_NORMAL);

			if ((merchant.equals("") || (merchant.equals("9999")))) {
			} else {
				hql += "and bp.merchant = ? ";
				valueList.add(merchant);
			}

			hql += "order by bp.category, bp.merchant, bp.requestTime";

			return this.list(hql, valueList.toArray());

		} else {
			return null;
		}
	}

	public List listHistory(String corpId, String userId, String dateFrom, String dateTo,
			String merchant, String fromAcc) throws NTBException {

		String hql = "from BillPayment as bp where bp.batchId is null and bp.corpId = ? and (bp.payType = ? or bp.payType = ? or bp.payType = ?) ";
		List valueList = new ArrayList();

		valueList.add(corpId);
		valueList.add(BillPayment.PAYMENT_TYPE_GENERAL);
		valueList.add(BillPayment.PAYMENT_TYPE_CARD);
		valueList.add(BillPayment.PAYMENT_TYPE_TAX);

		if (!Utils.null2EmptyWithTrim(dateFrom).equals("")) {
			dateFrom = DateTime.formatDate(dateFrom, defalutPattern, "yyyy-MM-dd");
			Timestamp timeFrom = DateTime.getTimestampByStr(dateFrom, true);
			hql += "and bp.requestTime >= ? ";
			valueList.add(timeFrom);
		}
		if (!Utils.null2EmptyWithTrim(dateTo).equals("")) {
			dateTo = DateTime.formatDate(dateTo, defalutPattern, "yyyy-MM-dd");
			Timestamp timeTo = DateTime.getTimestampByStr(dateTo, false);
			hql += "and bp.requestTime <= ? ";
			valueList.add(timeTo);
		}
		if ((!Utils.null2EmptyWithTrim(merchant).equals("")) && (!merchant.equals("9999"))) {
			hql += "and bp.merchant = ? ";
			valueList.add(merchant);
		}
		if (!Utils.null2EmptyWithTrim(fromAcc).equals("") && !Utils.null2EmptyWithTrim(fromAcc).equals("0")) {
			hql += "and bp.fromAccount = ? ";
			valueList.add(fromAcc);
		}
		if (!Utils.null2EmptyWithTrim(userId).equals("")) {
			hql += "and bp.userId = ? ";
			valueList.add(userId);
		}

		hql += "order by bp.category, bp.merchant, bp.requestTime";

		return this.list(hql, valueList.toArray());
	}

	public List listBillNo(String corpId) throws NTBException{
		String hql = "from BillPayment as bp where bp.corpId = ? and (bp.payType = ? or bp.payType = ?) and bp.status = ?";
		List list = this.list(hql, new Object[] {corpId,
				BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE,
				BillPayment.PAYMENT_TYPE_CARD_TEMPLATE,
				Constants.STATUS_NORMAL});

		return list;

	}
	
	public List listRecordByBatchId(String batchId) throws NTBException {
		String hql = "from BillPayment as t where t.batchId = ?";
		List list = this.list(hql, new Object[] {batchId});

		return list;
		
	}

}
