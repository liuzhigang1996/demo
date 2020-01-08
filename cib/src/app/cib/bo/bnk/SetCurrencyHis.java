/*
 * Created Mon Jul 10 18:49:14 CST 2006 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.bnk;

import java.io.Serializable;

/**
 * A class that represents a row in the 'TXN_LIMIT' table.
 * This class may be customized as it is never re-generated
 * after being created.
 */
public class SetCurrencyHis
    extends AbstractSetCurrencyHis
    implements Serializable
{
    

	/**
     * Simple constructor of TxnLimit instances.
     */
    public SetCurrencyHis()
    {
    }

    /**
     * Constructor of TxnLimit instances given a simple primary key.
     * @param seqNo
     */
    public SetCurrencyHis(java.lang.String ccyCode)
    {
        super(ccyCode);
    }

    /* Add customized code below */

}
