/*
 * Created Tue Sep 05 12:24:41 CST 2006 by MyEclipse Hibernate Tool.
 */ 
package app.cib.bo.enq;

import java.io.Serializable;

/**
 * A class that represents a row in the 'HS_WIRE_IN_INFO' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class HsWireInInfo
    extends AbstractHsWireInInfo
    implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2756613769911862263L;

	/**
     * Simple constructor of HsWireInInfo instances.
     */
    public HsWireInInfo()
    {
    }

    /**
     * Constructor of HsWireInInfo instances given a simple primary key.
     * @param referenceNo
     */
    public HsWireInInfo(java.lang.String seqNo)
    {
        super(seqNo);
    }

    /* Add customized code below */

}
