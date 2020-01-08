/*
 * Created Sun Aug 06 15:30:12 CST 2006 by MyEclipse Hibernate Tool.
 */ 
package app.cib.bo.sys;

import java.io.Serializable;

/**
 * A class that represents a row in the 'TELLER_SEQUENCE' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class TellerSequence
    extends AbstractTellerSequence
    implements Serializable
{
    /**
     * Simple constructor of TellerSequence instances.
     */
    public TellerSequence()
    {
    }

    /**
     * Constructor of TellerSequence instances given a simple primary key.
     * @param seqNo
     */
    public TellerSequence(java.lang.String seqNo)
    {
        super(seqNo);
    }

    /* Add customized code below */

}
