package app.cib.util;

import com.neturbo.set.exception.NTBException;
import com.neturbo.set.core.NTBAction;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface PageActionHandler {
    public void processPageAction(NTBAction action) throws NTBException;
}
