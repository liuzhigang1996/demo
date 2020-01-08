/**
 * @author hjs
 * 2006-8-29
 */
package app.cib.bo.enq;

import java.io.Serializable;

/**
 * @author hjs
 * 2006-8-29
 */
public class MerchantBillBean implements Serializable{
	
	private static final long serialVersionUID = 7592362585109245590L;
	
	private String suspendAccNo;
	private String suspendAccCcy;
	private String paymentDescription;
	private String merEnqFlag;
	/**
	 * @return the paymentDescription
	 */
	public String getPaymentDescription() {
		return paymentDescription;
	}
	/**
	 * @param paymentDescription the paymentDescription to set
	 */
	public void setPaymentDescription(String paymentDescription) {
		this.paymentDescription = paymentDescription;
	}
	/**
	 * @return the suspendAccCcy
	 */
	public String getSuspendAccCcy() {
		return suspendAccCcy;
	}
	/**
	 * @param suspendAccCcy the suspendAccCcy to set
	 */
	public void setSuspendAccCcy(String suspendAccCcy) {
		this.suspendAccCcy = suspendAccCcy;
	}
	/**
	 * @return the suspendAccNo
	 */
	public String getSuspendAccNo() {
		return suspendAccNo;
	}
	/**
	 * @param suspendAccNo the suspendAccNo to set
	 */
	public void setSuspendAccNo(String suspendAccNo) {
		this.suspendAccNo = suspendAccNo;
	}
	public String getMerEnqFlag() {
		return merEnqFlag;
	}
	public void setMerEnqFlag(String merEnqFlag) {
		this.merEnqFlag = merEnqFlag;
	}
	
}
