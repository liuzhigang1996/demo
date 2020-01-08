/*
 * Created Mon Jul 17 10:29:28 CST 2006 by MyEclipse Hibernate Tool.
 */ 
package app.cib.bo.txn;

import java.io.Serializable;

/**
 * A class that represents a row in the 'FILE_TRANSFER' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class FileTransfer
    extends AbstractFileTransfer
    implements Serializable
{
    /**
     * Simple constructor of FileTransfer instances.
     */
    public FileTransfer()
    {
    }

    /**
     * Constructor of FileTransfer instances given a simple primary key.
     * @param transId
     */
    public FileTransfer(java.lang.String transId)
    {
        super(transId);
    }

    /* Add customized code below */

}
