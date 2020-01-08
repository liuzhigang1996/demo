/*
 * Created Mon Jul 17 10:31:07 CST 2006 by MyEclipse Hibernate Tool.
 */ 
package app.cib.bo.txn;

import java.io.Serializable;

/**
 * A class that represents a row in the 'TRANSFER_OVERSEA' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class TransferOversea
    extends AbstractTransferOversea
    implements Serializable
{
    /**
     * Simple constructor of TransferOversea instances.
     */
	public static final String TRANSFER_AUTH_STATUS_COMPLETED = "0";
	public static final String TRANSFER_AUTH_STATUS_SUBMIT = "1";
	public static final String TRANSFER_AUTH_STATUS_APPROVALING = "2";
	public static final String TRANSFER_AUTH_STATUS_APPROVALED = "3";
	public static final String TRANSFER_AUTH_STATUS_REJECT = "7";
	public static final String TRANSFER_AUTH_STATUS_ERROR = "8";
	public static final String TRANSFER_AUTH_STATUS_OTHER = "9";
	
	public static final String TRANSFER_STATUS_NEW = "1";
	public static final String TRANSFER_STATUS_EDIT = "2";
	public static final String TRANSFER_STATUS_DELETE = "3";
	
	public static final String TRANSFER_TYPE_GENERAL = "1";
	public static final String TRANSFER_TYPE_BATCH = "2";
	public static final String TRANSFER_TYPE_SCHEDULED = "3";
	public static final String TRANSFER_TYPE_SCHEDULEDHISTORY = "4";
	public static final String TRANSFER_TYPE_TEMPLATE = "9";
    public TransferOversea()
    {
    }

    /**
     * Constructor of TransferOversea instances given a simple primary key.
     * @param transId
     */
    public TransferOversea(java.lang.String transId)
    {
        super(transId);
    }

    /* Add customized code below */

}
