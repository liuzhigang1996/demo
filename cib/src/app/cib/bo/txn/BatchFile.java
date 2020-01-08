/*
 * Created Mon Aug 07 14:50:28 CST 2006 by MyEclipse Hibernate Tool.
 */ 
package app.cib.bo.txn;

import java.io.Serializable;

/**
 * A class that represents a row in the 'BATCH_FILE' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class BatchFile
    extends AbstractBatchFile
    implements Serializable
{
    /**
     * Simple constructor of BatchFile instances.
     */
    public BatchFile()
    {
    }

    /**
     * Constructor of BatchFile instances given a simple primary key.
     * @param transId
     */
    public BatchFile(java.lang.String transId)
    {
        super(transId);
    }

    /* Add customized code below */

}
