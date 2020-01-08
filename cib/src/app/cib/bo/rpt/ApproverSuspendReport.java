/*
 * Created Mon Nov 27 10:54:33 CST 2006 by MyEclipse Hibernate Tool.
 */ 
package app.cib.bo.rpt;

import java.io.Serializable;

/**
 * A class that represents a row in the 'APPROVER_SUSPEND_REPORT' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class ApproverSuspendReport
    extends AbstractApproverSuspendReport
    implements Serializable
{
	private static final long serialVersionUID = -8174151811663538906L;
	
	public static final String TRANS_TYPE_SCHEDULE = "1";
	public static final String TRANS_TYPE_SCHEDULE_JOB = "2";
	public static final String TRANS_TYPE_SCHEDULE_PAYROLL = "3";

	public static final String OPERATION_TYPE_REQUESTED = "1";
	public static final String OPERATION_TYPE_APPROVED = "2";

	/**
     * Simple constructor of ApproverSuspendReport instances.
     */
    public ApproverSuspendReport()
    {
    }

    /**
     * Constructor of ApproverSuspendReport instances given a simple primary key.
     * @param reportId
     */
    public ApproverSuspendReport(java.lang.String reportId)
    {
        super(reportId);
    }

    /* Add customized code below */

}
