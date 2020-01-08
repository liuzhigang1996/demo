/*
 * Created Mon Aug 07 14:49:55 CST 2006 by MyEclipse Hibernate Tool.
 */ 
package app.cib.bo.sys;

import java.io.Serializable;

/**
 * A class that represents a row in the 'EMAIL_NOTIFICATION' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class EmailNotification
    extends AbstractEmailNotification
    implements Serializable
{
    /**
     * Simple constructor of EmailNotification instances.
     */
    public EmailNotification()
    {
    }

    /**
     * Constructor of EmailNotification instances given a simple primary key.
     * @param seqNo
     */
    public EmailNotification(java.lang.String seqNo)
    {
        super(seqNo);
    }

    /* Add customized code below */

}
