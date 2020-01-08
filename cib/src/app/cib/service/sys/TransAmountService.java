/**
 * @author hjs
 * 2007-5-29
 */
package app.cib.service.sys;

import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2007-5-29
 */
public interface TransAmountService {
	
	public void checkMinTransAmt(String ccyCode, double transAmt) throws NTBException;
	
	public void checkMinAmtOtherBanks(String ccyCode, double transAmt) throws NTBException;

}
