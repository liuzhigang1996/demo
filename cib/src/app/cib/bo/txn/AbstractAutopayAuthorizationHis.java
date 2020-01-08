package app.cib.bo.txn;

import java.util.Date;

/**
 * AbstractAutopayAuthorization entity provides the base persistence definition
 * of the AutopayAuthorization entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractAutopayAuthorizationHis implements
		java.io.Serializable {

	// Fields
    private int hashValue = 0;
    private String seqNo;
	private String transNo;
	private String userId;
	private String apsCode;
	private String contractNo;
	private String payAcct;
	private double paymentLimit;
	private String payOption;
	private String mode;
	private String authStatus;
	private String status;
	private Date requestTime;
	private Date executeTime;
	private String corpId;

	// Constructors

	public AbstractAutopayAuthorizationHis(){
		
	}
	/** default constructor */
	public AbstractAutopayAuthorizationHis(java.lang.String seqNo) {
		this.setSeqNo(seqNo);
	}

	public java.lang.String getSeqNo()
    {
        return seqNo;
    }

    /**
     * Set the simple primary key value that identifies this object.
     * @param seqNo
     */
    public void setSeqNo(java.lang.String seqNo)
    {
        this.hashValue = 0;
        this.seqNo = seqNo;
    }
	
	/** minimal constructor */
	public AbstractAutopayAuthorizationHis(String transNo, String userId,
			String apsCode, String contractNo, String payAcct) {
		this.transNo = transNo;
		this.userId = userId;
		this.apsCode = apsCode;
		this.contractNo = contractNo;
		this.payAcct = payAcct;
	}

	/** full constructor */
	public AbstractAutopayAuthorizationHis(String transNo, String userId,
			String apsCode, String contractNo, String payAcct,
			double paymentLimit, String payOption, String mode,
			String authStatus, String status, Date requestTime, Date executeTime) {
		this.transNo = transNo;
		this.userId = userId;
		this.apsCode = apsCode;
		this.contractNo = contractNo;
		this.payAcct = payAcct;
		this.paymentLimit = paymentLimit;
		this.payOption = payOption;
		this.mode = mode;
		this.authStatus = authStatus;
		this.status = status;
		this.requestTime = requestTime;
		this.executeTime = executeTime;
	}

	// Property accessors

	public String getTransNo() {
		return this.transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getApsCode() {
		return this.apsCode;
	}

	public void setApsCode(String apsCode) {
		this.apsCode = apsCode;
	}

	public String getContractNo() {
		return this.contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getPayAcct() {
		return this.payAcct;
	}

	public void setPayAcct(String payAcct) {
		this.payAcct = payAcct;
	}

	public double getPaymentLimit() {
		return this.paymentLimit;
	}

	public void setPaymentLimit(double paymentLimit) {
		this.paymentLimit = paymentLimit;
	}

	public String getPayOption() {
		return this.payOption;
	}

	public void setPayOption(String payOption) {
		this.payOption = payOption;
	}

	public String getMode() {
		return this.mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getAuthStatus() {
		return this.authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getRequestTime() {
		return this.requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public Date getExecuteTime() {
		return this.executeTime;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
}