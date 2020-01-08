package app.cib.bo.srv;

import java.util.Date;

/**
 * ChequeBookRequest entity. @author MyEclipse Persistence Tools
 */
public class ChequeBookRequest extends AbstractChequeBookRequest implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public ChequeBookRequest() {
	}

	/** minimal constructor */
	public ChequeBookRequest(String transNo) {
		super(transNo);
	}

	/** full constructor */
	public ChequeBookRequest(String transNo, String accountNo, double noOfBook,
			String pickupBranchCode, Date executeTime, String authStatus,
			String status, Date requestTime, String userId,String payCurrency) {
		super(transNo, accountNo, noOfBook, pickupBranchCode, executeTime,
				authStatus, status, requestTime, userId, payCurrency);
	}

	private String payCurrency ;

	public String getPayCurrency() {
		return payCurrency;
	}

	public void setPayCurrency(String payCurrency) {
		this.payCurrency = payCurrency;
	}
}
