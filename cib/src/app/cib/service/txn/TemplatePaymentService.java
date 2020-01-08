/**
 * 
 */
package app.cib.service.txn;

import java.util.*;

import app.cib.bo.txn.BillPayment;

import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2006-07-17
 */
public interface TemplatePaymentService {
	/*
	 * add a payment transaction template
	 */
	public BillPayment addTemplate(BillPayment pojoPayment) throws NTBException ;
	
	/*
	 * edit a payment transaction template
	 */
	public BillPayment editTemplate(BillPayment pojoPayment, String userID) throws NTBException ;
	
	/*
	 * delete a payment transaction template
	 */
	public BillPayment deleteTemplate(BillPayment pojoPayment, String userID) throws NTBException ;
	
	/*
	 * load a payment transaction template
	 */
	public BillPayment viewTemplate(String transID) throws NTBException ;
	
	/*
	 * list a payment transaction template list
	 */
	public List listTemplate(String corpID, String merchant) throws NTBException ;
	
	/*
	 * pay the payment transaction template
	 */
	public List payTemplate(List paymentPojoList) throws NTBException ;	
}
