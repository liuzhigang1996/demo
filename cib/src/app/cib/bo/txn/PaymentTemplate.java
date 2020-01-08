/*
 * Created Mon Jul 17 10:29:44 CST 2006 by MyEclipse Hibernate Tool.
 */ 
package app.cib.bo.txn;

import java.io.Serializable;

/**
 * A class that represents a row in the 'PAYMENT_TEMPLATE' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class PaymentTemplate
    extends AbstractPaymentTemplate
    implements Serializable
{
    /**
     * Simple constructor of PaymentTemplate instances.
     */
    public PaymentTemplate()
    {
    }

    /**
     * Constructor of PaymentTemplate instances given a simple primary key.
     * @param seqNo
     */
    public PaymentTemplate(java.lang.String seqNo)
    {
        super(seqNo);
    }

    /* Add customized code below */

}
