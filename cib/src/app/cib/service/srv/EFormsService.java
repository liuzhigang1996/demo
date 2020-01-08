/**
 * @author hjs
 * 2007-4-24
 */
package app.cib.service.srv;

import java.util.List;

import com.neturbo.set.core.NTBUser;
import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2007-4-24
 */
public interface EFormsService {
	
	public List list(String corpId, String typeOfForm) throws NTBException;
	
	public String getHyperLink(NTBUser user, String fileName) throws NTBException;

}
