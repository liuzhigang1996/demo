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
public class TxnLimit
    extends AbstractTxnLimit
    implements Serializable
{
	private static final long serialVersionUID = 8023685320059022015L;
    /**
	 *
	 */
        public final static String TXN_TYPE_ALL = "ALL";
	public final static String ACCOUNT_ALL = "9999999999";

	/**
     * Simple constructor of TxnLimit instances.
     */
    public TxnLimit()
    {
    }

    /**
     * Constructor of TxnLimit instances given a simple primary key.
     * @param seqNo
     */
    public TxnLimit(java.lang.String seqNo)
    {
        super(seqNo);
    }

    /* Add customized code below */

}
