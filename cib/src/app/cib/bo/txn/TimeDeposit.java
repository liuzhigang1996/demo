/*
 * Created Sun Jul 23 14:09:27 CST 2006 by MyEclipse Hibernate Tool.
 */ 
package app.cib.bo.txn;

import java.io.Serializable;

/**
 * A class that represents a row in the 'TIME_DEPOSIT' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class TimeDeposit
    extends AbstractTimeDeposit
    implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1612792764070787932L;
	public static final String TD_TYPE_NEW = "1";
	public static final String TD_TYPE_WITHDRAW = "2";
	
    /**
     * Simple constructor of TimeDeposit instances.
     */
    public TimeDeposit()
    {
    }

    /**
     * Constructor of TimeDeposit instances given a simple primary key.
     * @param transId
     */
    public TimeDeposit(java.lang.String transId)
    {
        super(transId);
    }

    /* Add customized code below */

}
