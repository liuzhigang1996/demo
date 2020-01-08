/**
 * @author hjs
 * 2006-10-14
 */
package app.cib.bo.bat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author hjs
 * 2006-10-14
 */
public class PayrollFileBean {
	
	private List normalList;
	private List errList;
	private BigDecimal normalTotalAmt;
	private BigDecimal errTotalAmt;
	private int allCount;
	private Map payrollHeader;
	private boolean errFlag;
	
	public List getErrList() {
		return errList;
	}
	
	public void setErrList(List errList) {
		this.errList = errList;
	}
	
	public List getNormalList() {
		return normalList;
	}
	public void setNormalList(List normalList) {
		this.normalList = normalList;
	}

	public BigDecimal getErrTotalAmt() {
		return errTotalAmt;
	}

	public void setErrTotalAmt(BigDecimal errTotalAmt) {
		this.errTotalAmt = errTotalAmt;
	}

	public BigDecimal getNormalTotalAmt() {
		return normalTotalAmt;
	}

	public void setNormalTotalAmt(BigDecimal normalTotalAmt) {
		this.normalTotalAmt = normalTotalAmt;
	}

	public int getAllCount() {
		return allCount;
	}

	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}

	public Map getPayrollHeader() {
		return payrollHeader;
	}

	public void setPayrollHeader(Map payrollHeader) {
		this.payrollHeader = payrollHeader;
	}

	public boolean isErrFlag() {
		return errFlag;
	}

	public void setErrFlag(boolean errFlag) {
		this.errFlag = errFlag;
	}
}
