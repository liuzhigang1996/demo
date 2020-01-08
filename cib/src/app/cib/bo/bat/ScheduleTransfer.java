/*
 * Created Tue Oct 10 10:14:36 CST 2006 by MyEclipse Hibernate Tool.
 */ 
package app.cib.bo.bat;

import java.io.Serializable;

/**
 * A class that represents a row in the 'SCHEDULE_TRANSFER' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class ScheduleTransfer
    extends AbstractScheduleTransfer
    implements Serializable
{    
    /**
	 * 
	 */
	private static final long serialVersionUID = -3461967162261277193L;
	
	public static final String DAILY = "1";
    public static final String WEEKLY = "2";
    public static final String MONTHLY = "3";
    public static final String DAYS_PER_MONTH = "4";
    
    public static final String VIEW_FROM_SCHEDULE_LIST = "0";
    public static final String VIEW_FROM_SCHEDULE_TXN_REPORT = "1";
    public static final String VIEW_FROM_SCHEDULE_APPROVAL_REPORT = "2";
    public static final String VIEW_FROM_SCHEDULE_OPERATION_HIS = "3";
    public static final String VIEW_FROM_SCHEDULE_BATCH_HIS = "4";
    
    
    /**
     * Simple constructor of ScheduleTransfer instances.
     */
    public ScheduleTransfer()
    {
    }

    /**
     * Constructor of ScheduleTransfer instances given a simple primary key.
     * @param scheduleId
     */
    public ScheduleTransfer(java.lang.String scheduleId)
    {
        super(scheduleId);
    }

    /* Add customized code below */

}
