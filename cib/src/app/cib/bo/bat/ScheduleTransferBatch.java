/*
 * Created Mon Oct 30 14:50:54 CST 2006 by MyEclipse Hibernate Tool.
 */ 
package app.cib.bo.bat;

import java.io.Serializable;

/**
 * A class that represents a row in the 'SCHEDULE_TRANSFER_BATCH' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class ScheduleTransferBatch
    extends AbstractScheduleTransferBatch
    implements Serializable
{
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5424185518753660330L;

	public static String STATUS_COMPLETED = "0";
	public static String STATUS_PENDING = "1";
	public static String STATUS_CANCELED = "2";
	public static String STATUS_ERROR = "9";

	/**
     * Simple constructor of ScheduleTransferBatch instances.
     */
    public ScheduleTransferBatch()
    {
    }

    /**
     * Constructor of ScheduleTransferBatch instances given a simple primary key.
     * @param batchId
     */
    public ScheduleTransferBatch(java.lang.String batchId)
    {
        super(batchId);
    }

    /* Add customized code below */

}
