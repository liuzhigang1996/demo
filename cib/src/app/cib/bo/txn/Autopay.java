package app.cib.bo.txn;

import java.util.Date;

public class Autopay implements
		java.io.Serializable {

	// Fields

	private String corpId;
	private String apsCode;
	private String contractNo;
	private String payAcct;
	private double paymentLimit;
	private String payOption;
	private String mode;
	
	private String transNo;
	private String status;
	private String authStatus;

	// Constructors

	/** default constructor */
	public Autopay() {
	}

	/** minimal constructor */
	public Autopay(String apsCode, String contractNo, String payAcct) {

		this.apsCode = apsCode;
		this.contractNo = contractNo;
		this.payAcct = payAcct;
	}

	/** full constructor */
	public Autopay(String apsCode, String contractNo, String payAcct,
			double paymentLimit, String payOption, String mode) {

		this.apsCode = apsCode;
		this.contractNo = contractNo;
		this.payAcct = payAcct;
		this.paymentLimit = paymentLimit;
		this.payOption = payOption;
		this.mode = mode;
	}

	// Property accessors

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

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof Autopay))
			return false;
		Autopay castOther = (Autopay) other;

		return ((this.getApsCode() == castOther.getApsCode()) || (this
				.getApsCode() != null
				&& castOther.getApsCode() != null && this.getApsCode().equals(
				castOther.getApsCode())))
				&&
				((this.getCorpId() == castOther.getCorpId()) || (this.getCorpId() != null
				&& castOther.getCorpId() != null && this.getCorpId().equals(
				castOther.getCorpId())))
				&&
				((this.getContractNo() == castOther.getContractNo()) || (this.getContractNo() != null
				&& castOther.getContractNo() != null && this.getContractNo().equals(
				castOther.getContractNo())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
		+ (getCorpId() == null ? 0 : this.getCorpId().hashCode());
		
		result = 37 * result
				+ (getApsCode() == null ? 0 : this.getApsCode().hashCode());
		result = 37 * result
				+ (getContractNo() == null ? 0 : this.getContractNo().hashCode());
		
		return result;
	}
}
