package app.cib.bo.txn;

/**
 * AutopayMerchantName entity. @author MyEclipse Persistence Tools
 */
public class AutopayMerchantName extends AbstractAutopayMerchantName implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public AutopayMerchantName() {
	}

	/** full constructor */
	public AutopayMerchantName(AutopayMerchantNameId id, String merchantName) {
		super(id, merchantName);
	}

}
