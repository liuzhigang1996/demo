/*
 * Created Mon Mar 19 14:08:45 CST 2007 by MyEclipse Hibernate Tool.
 */ 
package app.cib.bo.sys;

import java.io.Serializable;

/**
 * A class that represents a row in the 'ID_GENERATOR' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class IdGenerator
    extends AbstractIdGenerator
    implements Serializable
{
    /**
     * Simple constructor of IdGenerator instances.
     */
    public IdGenerator()
    {
    }

    /**
     * Constructor of IdGenerator instances given a simple primary key.
     * @param idType
     */
    public IdGenerator(java.lang.String idType)
    {
        super(idType);
    }

    /* Add customized code below */

}
