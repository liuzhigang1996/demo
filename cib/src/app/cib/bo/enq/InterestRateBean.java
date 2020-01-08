/**
 * @author hjs
 * 2006-9-11
 */
package app.cib.bo.enq;

import java.io.Serializable;
import java.util.List;

/**
 * @author hjs
 * 2006-9-11
 */
public class InterestRateBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8118401433354074658L;
	
	private String savingsRate;
	private String effectiveDate;
	private List iRateList;
	private String periodLabel1;
	private String periodLabel2;
	private String periodLabel3;
	private String periodLabel4;
	private String periodLabel5;
	/**
	 * @return the iRateList
	 */
	public List getIRateList() {
		return iRateList;
	}

	public void setIRateList(List rateList) {
		iRateList = rateList;
	}

	public String getSavingsRate() {
		return savingsRate;
	}

	public void setSavingsRate(String savingsRate) {
		this.savingsRate = savingsRate;
	}

	/**
	 * @return the periodLabel1
	 */
	public String getPeriodLabel1() {
		return periodLabel1;
	}

	/**
	 * @param periodLabel1 the periodLabel1 to set
	 */
	public void setPeriodLabel1(String periodLabel1) {
		this.periodLabel1 = periodLabel1;
	}

	/**
	 * @return the periodLabel2
	 */
	public String getPeriodLabel2() {
		return periodLabel2;
	}

	/**
	 * @param periodLabel2 the periodLabel2 to set
	 */
	public void setPeriodLabel2(String periodLabel2) {
		this.periodLabel2 = periodLabel2;
	}

	/**
	 * @return the periodLabel3
	 */
	public String getPeriodLabel3() {
		return periodLabel3;
	}

	/**
	 * @param periodLabel3 the periodLabel3 to set
	 */
	public void setPeriodLabel3(String periodLabel3) {
		this.periodLabel3 = periodLabel3;
	}

	/**
	 * @return the periodLabel4
	 */
	public String getPeriodLabel4() {
		return periodLabel4;
	}

	/**
	 * @param periodLabel4 the periodLabel4 to set
	 */
	public void setPeriodLabel4(String periodLabel4) {
		this.periodLabel4 = periodLabel4;
	}

	/**
	 * @return the periodLabel5
	 */
	public String getPeriodLabel5() {
		return periodLabel5;
	}

	/**
	 * @param periodLabel5 the periodLabel5 to set
	 */
	public void setPeriodLabel5(String periodLabel5) {
		this.periodLabel5 = periodLabel5;
	}

	/**
	 * @return the effectiveDate
	 */
	public String getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

}
