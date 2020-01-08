/*
 * Created Mon Nov 17 12:47:30 CST 2008 by MyEclipse Hibernate Tool.
 */ 
package app.cib.bo.sys;

import java.io.Serializable;

/**
 * A class that represents a row in the 'PIN_MAILER' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class PinMailer
    extends AbstractPinMailer
    implements Serializable
{
    /**
	 * <code>serialVersionUID</code> Comments
	 */
	private static final long serialVersionUID = -4957460939710789186L;

	/**
     * Simple constructor of PinMailer instances.
     */
    public PinMailer()
    {
    }

    /**
     * Constructor of PinMailer instances given a simple primary key.
     * @param seqNo
     */
    public PinMailer(java.lang.String seqNo)
    {
        super(seqNo);
    }

    /* Add customized code below */

}
