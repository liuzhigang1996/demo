package app.cib.bo.txn;

/**
 * AbstractAutopayMerchantInfo entity provides the base persistence definition
 * of the AutopayMerchantInfo entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractAutopayMerchantInfo implements
		java.io.Serializable {

	// Fields

	private String apsCode;
	private String payByAcct;
	private String payByCreditCard;

	// Constructors

	/** default constructor */
	public AbstractAutopayMerchantInfo() {
	}

	/** full constructor */
	public AbstractAutopayMerchantInfo(String apsCode, String payByAcct,
			String payByCreditCard) {
		this.apsCode = apsCode;
		this.payByAcct = payByAcct;
		this.payByCreditCard = payByCreditCard;
	}

	// Property accessors

	public String getApsCode() {
		return this.apsCode;
	}

	public void setApsCode(String apsCode) {
		this.apsCode = apsCode;
	}

	public String getPayByAcct() {
		return this.payByAcct;
	}

	public void setPayByAcct(String payByAcct) {
		this.payByAcct = payByAcct;
	}

	public String getPayByCreditCard() {
		return this.payByCreditCard;
	}

	public void setPayByCreditCard(String payByCreditCard) {
		this.payByCreditCard = payByCreditCard;
	}

}