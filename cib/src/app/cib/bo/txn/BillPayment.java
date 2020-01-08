/*
 * Created Mon Jul 17 10:29:13 CST 2006 by MyEclipse Hibernate Tool.
 */ 
package app.cib.bo.txn;

import java.io.Serializable;

/**
 * A class that represents a row in the 'BILL_PAYMENT' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class BillPayment
    extends AbstractBillPayment
    implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2890622296145636000L;
	
	public static final String PAYMENT_TYPE_GENERAL = "1";
	public static final String PAYMENT_TYPE_BATCH = "2";
	public static final String PAYMENT_TYPE_TAX = "3";
	public static final String PAYMENT_TYPE_CARD = "4";
	public static final String PAYMENT_TYPE_GENERAL_TEMPLATE = "7";
	public static final String PAYMENT_TYPE_CARD_TEMPLATE = "8";
    /**
     * Simple constructor of BillPayment instances.
     */
    public BillPayment()
    {
    }

    /**
     * Constructor of BillPayment instances given a simple primary key.
     * @param transId
     */
    public BillPayment(java.lang.String transId)
    {
        super(transId);
    }

    /* Add customized code below */

}
