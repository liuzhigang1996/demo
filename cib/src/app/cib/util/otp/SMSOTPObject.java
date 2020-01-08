package app.cib.util.otp;

import java.util.Date;

import app.cib.util.smsclient.log.LogSmsRequest;

public class SMSOTPObject implements java.io.Serializable{
	
	
	String otp;
	String language;
	String mobile;
	String sessionId;
	String transactionId;
	String transactionType;
	String transactionAmount;
	String transactionTime;
	Date createTime;
	int retryCount ;
	int resendCount ;
	//add by long_zg 20140724 for CR194 
	int sendTimes ;
	
	private LogSmsRequest logSmsRequest ;
	
	public SMSOTPObject(){
		this.resendCount = 0 ;
		this.retryCount = 0 ;
	}


	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public String getOtp() {
		return otp;
	}


	public void setOtp(String otp) {
		this.otp = otp;
	}


	public int getRetryCount() {
		return retryCount;
	}


	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}


	public int getResendCount() {
		return resendCount;
	}


	public void setResendCount(int resendCount) {
		this.resendCount = resendCount;
	}


	public LogSmsRequest getLogSmsRequest() {
		return logSmsRequest;
	}


	public void setLogSmsRequest(LogSmsRequest logSmsRequest) {
		this.logSmsRequest = logSmsRequest;
	}

	public int getSendTimes() {
		return sendTimes;
	}


	public void setSendTimes(int sendTimes) {
		this.sendTimes = sendTimes;
	}
	
}
