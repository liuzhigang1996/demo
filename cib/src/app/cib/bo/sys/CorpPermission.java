/*
 * Created Mon Jul 10 18:46:41 CST 2006 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.sys;

import java.io.Serializable;
import com.neturbo.set.core.NTBPermission;

/**
 * A class that represents a row in the 'CORP_PERMISSION' table.
 * This class may be customized as it is never re-generated
 * after being created.
 */
public class CorpPermission
    extends AbstractCorpPermission
    implements NTBPermission, Serializable
{
	public static final String PERMISSION_TYPE_FUNCTION = "0";
	public static final String PERMISSION_TYPE_ACCOUNT = "1";
    /**
     * Simple constructor of CorpPermission instances.
     */
    public CorpPermission()
    {
    }

    /**
     * Constructor of CorpPermission instances given a simple primary key.
     * @param seqNo
     */
    public CorpPermission(java.lang.String seqNo)
    {
        super(seqNo);
    }

    /* Add customized code below */

}
