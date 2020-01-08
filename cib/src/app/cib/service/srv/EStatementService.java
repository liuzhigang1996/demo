/**
 * @author hjs
 * 2007-4-23
 */
package app.cib.service.srv;

import java.util.*;

import com.neturbo.set.core.NTBUser;
import com.neturbo.set.exception.NTBException;

/**
 * @author hjs
 * 2007-4-23
 */
public interface EStatementService {
	
	public List list(String corpId, String range, String dateFrom, String dateTo, 
			String accountType,	String accountNo) throws NTBException;
	
	public String getHyperLink(NTBUser user, String fileName) throws NTBException;
	
	//add by linrui 20190907 for security
	public boolean isBelongCorpId(String corpId, String fileName) ;
	//add by linrui 20190923
	public List listEstatement(String functionName, String stmtType, String corpId, String range, String dateFrom, String dateTo, String accountNo) throws NTBException;

	public void changeMarkRead(String interalId, String reportDate) throws NTBException;

	public boolean getReadFlag(String reportCode, String ciNo) throws NTBException;
	
	public boolean getReadFlagByActionMethod(String ActionMethod, String ciNo) throws NTBException;

	public void readCheckAll(String stmtType, String actionMethod,String ciNo) throws NTBException;

	public void changeMarkReadByPdfName(String estatementName)  throws NTBException;
}
