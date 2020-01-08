/**
 * @author hjs
 * 2006-11-21
 */
package app.cib.service.utl;

import java.io.File;
import java.util.Date;

import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2006-11-21
 */
public interface UtilService {
	
	public void uploadFileToHost(String transCode, File uploadFile) throws NTBException ;
	//add by linrui 20180309
	public void uploadFileToHost(String transCode, File uploadFile, String batchType) throws NTBException ;
	
	public Date getNextSchDate() throws NTBException;
	
	public Date getNextWorkingDate(Date today) throws NTBException;
	
	public boolean isHoliday(Date date) throws NTBException;
	
	public String getCcyByAcct(String acctno) ;

}
