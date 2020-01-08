/**
 * @author hjs
 * 2007-4-27
 */
package app.cib.service.enq;

import com.neturbo.set.exception.NTBException;

import app.cib.bo.enq.DaylightSavingTime;

/**
 * @author hjs
 * 2007-4-27
 */
public interface DaylightSavingTimeService {
	
	public DaylightSavingTime checkDST(String cityName) throws NTBException;

}
