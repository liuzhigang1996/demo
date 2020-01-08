/*
 * Created Thu Oct 12 18:23:54 CST 2006 by MyEclipse Hibernate Tool.
 */ 
package app.cib.bo.bat;

import java.io.Serializable;

/**
 * A class that represents a row in the 'FILE_REQUEST' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class FileRequest
    extends AbstractFileRequest
    implements Serializable
{
    /**
     * Simple constructor of FileRequest instances.
     */
	public static final String BANK_DRAFT_BATCH_TYPE = "1";
	public static final String CASH_ORDER_BATCH_TYPE = "2";
	public static final String STOP_CHEQUE_BATCH_TYPE = "3";
	public static final String CHEQUE_PROTECTION_BATCH_TYPE = "4";
	public static final String TRANSFER_BANK_STM = "5";
	public static final String TRANSFER_BANK_MTS = "6";
	public static final String TRANSFER_MACAU_STM = "7";
	public static final String TRANSFER_OVERSEA_STM = "8";
	public static final String BATCH_PAYMENT = "9";
    public FileRequest()
    {
    }

    /**
     * Constructor of FileRequest instances given a simple primary key.
     * @param batchId
     */
    public FileRequest(java.lang.String batchId)
    {
        super(batchId);
    }

    /* Add customized code below */

}
