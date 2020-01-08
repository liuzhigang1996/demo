/*
 * Created Fri Oct 13 16:27:29 CST 2006 by MyEclipse Hibernate Tool.
 */ 
package app.cib.bo.bat;

import java.io.Serializable;

/**
 * A class that represents a row in the 'PAYROLL' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class Payroll
    extends AbstractPayroll
    implements Serializable
{
    /**
     * Simple constructor of Payroll instances.
     */
    public Payroll()
    {
    }

    /**
     * Constructor of Payroll instances given a simple primary key.
     * @param payrollId
     */
    public Payroll(java.lang.String payrollId)
    {
        super(payrollId);
    }

    /* Add customized code below */

}
