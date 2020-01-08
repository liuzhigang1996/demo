/*
 * Created Wed Aug 23 16:28:38 CST 2006 by MyEclipse Hibernate Tool.
 */ 
package app.cib.bo.txn;

import java.io.Serializable;

/**
 * A class that represents a row in the 'TXN_LIMIT_USAGE' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class TxnLimitUsage
    extends AbstractTxnLimitUsage
    implements Serializable
{
    /**
     * Simple constructor of TxnLimitUsage instances.
     */
    public TxnLimitUsage()
    {
    }

    /**
     * Constructor of TxnLimitUsage instances given a simple primary key.
     * @param usageId
     */
    public TxnLimitUsage(java.lang.String usageId)
    {
        super(usageId);
    }

    /* Add customized code below */

}
