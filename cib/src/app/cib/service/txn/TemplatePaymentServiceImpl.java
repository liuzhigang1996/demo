/**
 * 
 */
package app.cib.service.txn;

import java.util.*;

import app.cib.bo.txn.BillPayment;
import app.cib.dao.txn.BillPaymentDao;
import app.cib.util.Constants;

import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 
 */
public class TemplatePaymentServiceImpl implements TemplatePaymentService {

	/**
	 * @param args
	 */
	private BillPaymentDao billPaymentDao;

	public static void main(String[] args) {
	}

	public BillPaymentDao getBillPaymentDao() {
		return billPaymentDao;
	}

	public void setBillPaymentDao(BillPaymentDao billPaymentDao) {
		this.billPaymentDao = billPaymentDao;
	}

	public BillPayment addTemplate(BillPayment pojoPayment) throws NTBException {
		if (pojoPayment != null) {
			if ((pojoPayment.getPayType()
					.equals(BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE))
					|| (pojoPayment.getPayType()
							.equals(BillPayment.PAYMENT_TYPE_CARD_TEMPLATE))) {
				pojoPayment.setOperation(Constants.OPERATION_NEW);
				pojoPayment.setStatus(Constants.STATUS_NORMAL);
				pojoPayment.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
				billPaymentDao.add(pojoPayment);
			} else {
				throw new NTBException("err.txn.ThePayTypeIsNotATemplate");
			}
		} else {
			throw new NTBException("err.txn.PaymentPojoIsNull");
		}
		return pojoPayment;
	}

	public BillPayment deleteTemplate(BillPayment pojoPayment, String userID)
			throws NTBException {
		BillPayment billPayment = pojoPayment;
		/*
		if (billPayment.getUserId().equals(userID)) {
		} else {
			throw new NTBException("err.txn.UserNotTheCreator");
		}
		*/
		if ((billPayment.getPayType()
				.equals(BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE))
				|| (billPayment.getPayType()
						.equals(BillPayment.PAYMENT_TYPE_CARD_TEMPLATE))) {
			billPayment.setOperation(Constants.OPERATION_REMOVE);
			billPayment.setStatus(Constants.STATUS_REMOVED);
			billPayment.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			billPaymentDao.update(billPayment);
		} else {
			throw new NTBException("err.txn.ThePayTypeIsNotATemplate");
		}
		return billPayment;
	}

	public BillPayment editTemplate(BillPayment pojoPayment, String userID)
			throws NTBException {
		if (pojoPayment != null) {
			/*
			if (pojoPayment.getUserId().equals(userID)) {
			} else {
				throw new NTBException("err.txn.UserNotTheCreator");
			}
			*/
			if ((pojoPayment.getPayType()
					.equals(BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE))
					|| (pojoPayment.getPayType()
							.equals(BillPayment.PAYMENT_TYPE_CARD_TEMPLATE))) {
				pojoPayment.setOperation(Constants.OPERATION_UPDATE);
				pojoPayment.setStatus(Constants.STATUS_NORMAL);
				pojoPayment.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
				billPaymentDao.update(pojoPayment);
			} else {
				throw new NTBException("err.txn.ThePayTypeIsNotATemplate");
			}
		} else {
			throw new NTBException("err.txn.PaymentPojoIsNull");
		}
		return pojoPayment;
	}

	public BillPayment viewTemplate(String transID) throws NTBException {
		BillPayment billPayment = null;
		if ((transID != null) && (!transID.equals(""))) {
			billPayment = (BillPayment) billPaymentDao.load(BillPayment.class,
					transID);
			if ((billPayment.getPayType()
					.equals(BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE))
					|| (billPayment.getPayType()
							.equals(BillPayment.PAYMENT_TYPE_CARD_TEMPLATE))) {
			} else {
				throw new NTBException("err.txn.ThePayTypeIsNotATemplate");
			}
		} else {
			throw new NTBException("err.txn.TransIDIsNullOrEmpty");
		}
		return billPayment;
	}

	public List listTemplate(String corpID, String merchant)
			throws NTBException {
		List templateList = null;
		if ((corpID != null) && (!corpID.equals(""))) {
			templateList = billPaymentDao.listTemplate(corpID, merchant);
		} else {
			throw new NTBException("err.txn.CorpIDIsNullOrEmpty");
		}
		return templateList;
	}

	public List payTemplate(List paymentPojoList) throws NTBException {
		return null;
	}

}
