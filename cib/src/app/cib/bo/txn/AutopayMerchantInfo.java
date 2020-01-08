package app.cib.bo.txn;

/**
 * AutopayMerchantInfo entity. @author MyEclipse Persistence Tools
 */
public class AutopayMerchantInfo extends AbstractAutopayMerchantInfo implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public AutopayMerchantInfo() {
	}

	/** full constructor */
	public AutopayMerchantInfo(String apsCode, String payByAcct,
			String payByCreditCard) {
		super(apsCode, payByAcct, payByCreditCard);
	}

}
