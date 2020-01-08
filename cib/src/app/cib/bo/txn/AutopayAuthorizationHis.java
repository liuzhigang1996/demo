package app.cib.bo.txn;

import java.util.Date;

/**
 * AutopayAuthorization entity. @author MyEclipse Persistence Tools
 */
public class AutopayAuthorizationHis extends AbstractAutopayAuthorizationHis
		implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public AutopayAuthorizationHis() {
	}

	/** minimal constructor */
	public AutopayAuthorizationHis(String transNo, String userId, String apsCode,
			String contractNo, String payAcct) {
		super(transNo, userId, apsCode, contractNo, payAcct);
	}

	/** full constructor */
	public AutopayAuthorizationHis(String transNo, String userId, String apsCode,
			String contractNo, String payAcct, double paymentLimit,
			String payOption, String mode, String authStatus, String status,
			Date requestTime, Date executeTime) {
		super(transNo, userId, apsCode, contractNo, payAcct, paymentLimit,
				payOption, mode, authStatus, status, requestTime, executeTime);
	}

	private String payCurrency ;

	public String getPayCurrency() {
		return payCurrency;
	}

	public void setPayCurrency(String payCurrency) {
		this.payCurrency = payCurrency;
	}
}
