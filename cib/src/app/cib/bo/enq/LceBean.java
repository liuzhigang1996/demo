/**
 * @author hjs
 * 2006-8-29
 */
package app.cib.bo.enq;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hjs
 * 2006-8-29
 */
public class LceBean implements Serializable{
	
	private static final long serialVersionUID = -4703940260801333810L;

	// LCE of from amount
	private BigDecimal fromAmtLCE;
	
	// LCE of from exchange rate
	private BigDecimal fromRateLCE;
	
	// LCE of to amount
	private BigDecimal toAmtLCE;
	
	// LCE of to exchange rate
	private BigDecimal toRateLCE;
	
	/**
	 * @return the fromAmtLCE
	 */
	public BigDecimal getFromAmtLCE() {
		return fromAmtLCE;
	}
	/**
	 * @param fromAmtLCE the fromAmtLCE to set
	 */
	public void setFromAmtLCE(BigDecimal fromAmtLCE) {
		this.fromAmtLCE = fromAmtLCE;
	}
	/**
	 * @return the fromRateLCE
	 */
	public BigDecimal getFromRateLCE() {
		return fromRateLCE;
	}
	/**
	 * @param fromRateLCE the fromRateLCE to set
	 */
	public void setFromRateLCE(BigDecimal fromRateLCE) {
		this.fromRateLCE = fromRateLCE;
	}
	/**
	 * @return the toAmtLCE
	 */
	public BigDecimal getToAmtLCE() {
		return toAmtLCE;
	}
	/**
	 * @param toAmtLCE the toAmtLCE to set
	 */
	public void setToAmtLCE(BigDecimal toAmtLCE) {
		this.toAmtLCE = toAmtLCE;
	}
	/**
	 * @return the toRateLCE
	 */
	public BigDecimal getToRateLCE() {
		return toRateLCE;
	}
	/**
	 * @param toRateLCE the toRateLCE to set
	 */
	public void setToRateLCE(BigDecimal toRateLCE) {
		this.toRateLCE = toRateLCE;
	}

}
