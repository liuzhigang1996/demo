package app.cib.bo.srv;

import java.util.Date;

/**
 * AbstractChequeBookRequest entity provides the base persistence definition of
 * the ChequeBookRequest entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractChequeBookRequest implements java.io.Serializable {

	// Fields

	private String transNo;
	private String accountNo;
	private double noOfBook;
	private String pickupBranchCode;
	private Date executeTime;
	private String authStatus;
	private String status;
	private Date requestTime;
	private String userId;
	private String corpId;
	// Constructors
	//ADD BY LINRUI 20180323
	private String packageName;
	private String packageAdress;
	//end
	// add by wcc 20190401
	private String payCurrency;
	//add by linrui 20190527
	private String pickupType;

	/** default constructor */
	public AbstractChequeBookRequest() {
	}

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageAdress() {
		return this.packageAdress;
	}

	public void setPackageAdress(String packageAdress) {
		this.packageAdress = packageAdress;
	}

	/** minimal constructor */
	public AbstractChequeBookRequest(String transNo) {
		this.transNo = transNo;
	}

	/** full constructor */
	public AbstractChequeBookRequest(String transNo, String accountNo,
			double noOfBook, String pickupBranchCode, Date executeTime,
			String authStatus, String status, Date requestTime, String userId,String payCurrency) {//modify by wcc 20190401
		this.transNo = transNo;
		this.accountNo = accountNo;
		this.noOfBook = noOfBook;
		this.pickupBranchCode = pickupBranchCode;
		this.executeTime = executeTime;
		this.authStatus = authStatus;
		this.status = status;
		this.requestTime = requestTime;
		this.userId = userId;
		this.payCurrency = payCurrency;
	}

	// Property accessors

	public String getTransNo() {
		return this.transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public double getNoOfBook() {
		return this.noOfBook;
	}

	public void setNoOfBook(double noOfBook) {
		this.noOfBook = noOfBook;
	}

	public String getPickupBranchCode() {
		return this.pickupBranchCode;
	}

	public void setPickupBranchCode(String pickupBranchCode) {
		this.pickupBranchCode = pickupBranchCode;
	}

	public Date getExecuteTime() {
		return this.executeTime;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
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

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setPayCurrency(String payCurrency) {
		this.payCurrency = payCurrency;
	}
	
	public String getPayCurrency() {
		return payCurrency;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getPickupType() {
		return pickupType;
	}

	public void setPickupType(String pickupType) {
		this.pickupType = pickupType;
	}
	

}