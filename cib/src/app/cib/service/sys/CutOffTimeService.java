/**
 * @author hjs
 * 2006-11-20
 */
package app.cib.service.sys;

import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2006-11-20
 */
public interface CutOffTimeService {
	
	/**
	 * @param transCode
	 * @return alert message
	 * @throws NTBException
	 */
	public String check(String transCode, String fromCcy, String toCcy) throws NTBException;
	
	
	public String check(String transCode, String fromCcy, String toCcy,String transferType,String lang) throws NTBException;
	/**
	 * 
	 * @param transCode
	 * @return true:Î´³¬¹ý30·ÖÖÓ
	 * @throws NTBException
	 */
	public String checkService(String transCode, boolean isFX) throws NTBException;
	
	/**
	 * 
	 * @param transCode
	 * @throws NTBException
	 */
	//modify by long_zg 2014-12-15 for CR192 batch bob
	/*public void checkTransAvailable(String transCode) throws NTBException;*/
	public void checkTransAvailable(String transCode,boolean fullDay, boolean isFX) throws NTBException;
}
