/**
 * @author hjs
 * 2006-10-11
 */
package app.cib.service.bat;

import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.neturbo.set.exception.NTBException;

import app.cib.bo.bat.Payroll;
import app.cib.bo.bat.PayrollFileBean;
import app.cib.bo.sys.CorpUser;

/**
 * @author hjs
 * 2006-10-11
 */
public interface PayrollService {
	public static String TOTAL_AMOUNT_ACCEPTED = "totalAmountAccepted";
	public static String TOTAL_NUMBER_ACCEPTED = "totalNumberAccepted";
	public static String TOTAL_AMOUNT_REJECTED = "totalAmountRejected";
	public static String TOTAL_NUMBER_REJECTED = "totalNumberRejected";
	
	public void addPayroll(Payroll payroll) throws NTBException;
	
	public PayrollFileBean parseFile(CorpUser corpUser, InputStream inStream) throws NTBException;
	public void cancelUpload(String payrollId, OutputStream outStream,InputStream inStreamForParsing, File saveFile);
	public void clearUnavailableDataByCorpId(String corpId) throws NTBException;
	public void clearUnavailableDataByPayrollId(String payrollId) throws NTBException;
	public void updateStatus(String payrollId) throws NTBException;
	public Payroll viewAvaliablePayroll(String payrollId) throws NTBException;
	public Payroll getLatestPayroll(String corpId) throws NTBException;
	public List listAvaliablePayrollRec(String payrollId) throws NTBException;
	public void approvePayroll(Payroll payroll, CorpUser corpUser, BigDecimal equivalentMOP) throws NTBException;
	public void rejectPayroll(Payroll payroll) throws NTBException ;
	public OutputStream getOutputStreamByPreFile(Payroll payroll) throws NTBException;
	public void addPayrollByPreFile(Payroll payroll, List payrollRecList, OutputStream newFileOutStream) throws NTBException ;
	public void isDoable(String corpId, String fromAcc, String groupId, String startValueDate) throws NTBException;
	public List listHistory(String corpId, String userId, String dateFrom, String dateTo) throws NTBException;
	public List listUploadErrorDetailRec(String payrollId) throws NTBException;
	
	public boolean checkValidation(String corpId, String accountNo, String groupId) throws NTBException;
	
	public Map calculate(Payroll payroll) throws NTBException;
	
	/**
	 * @param corpId
	 * @param accountNo
	 * @param groupId
	 * @param startDate
	 * @return 0表示检查通过, 小于0表示有异常, 大于0的值表示startValueDate未超过指定的工作日
	 * @throws NTBException
	 */
	public int checkStartValueDate(String corpId, String accountNo, String groupId, Date startValueDate) throws NTBException;
	//to host to get file name
	public void downloadHisToRec(Payroll payroll)throws NTBException;
}
