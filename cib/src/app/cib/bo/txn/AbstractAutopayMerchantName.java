package app.cib.bo.txn;

/**
 * AbstractAutopayMerchantName entity provides the base persistence definition
 * of the AutopayMerchantName entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractAutopayMerchantName implements
		java.io.Serializable {

	// Fields

	private AutopayMerchantNameId id;
	private String merchantName;

	// Constructors

	/** default constructor */
	public AbstractAutopayMerchantName() {
	}

	/** full constructor */
	public AbstractAutopayMerchantName(AutopayMerchantNameId id,
			String merchantName) {
		this.id = id;
		this.merchantName = merchantName;
	}

	// Property accessors

	public AutopayMerchantNameId getId() {
		return this.id;
	}

	public void setId(AutopayMerchantNameId id) {
		this.id = id;
	}

	public String getMerchantName() {
		return this.merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

}