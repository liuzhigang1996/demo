package app.cib.bo.srv;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * AbstractOlAcOpenCustInfoId entity provides the base persistence definition of
 * the OlAcOpenCustInfoId entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractTxnPrompt implements
		java.io.Serializable {

	// Fields
	private String messageId;
	private String txnType;
	private String status;
	private String Emessage1;
	private String Emessage2;
	private String Emessage3;
	private String Emessage4;
	private String Cmessage1;
	private String Cmessage2;
	private String Cmessage3;
	private String Cmessage4;
	private String operator;
	private String approver;
	private String modifiedTime;
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	// Constructors
	/** default constructor */
	public AbstractTxnPrompt() {
	}
	/** minimal constructor */
	public AbstractTxnPrompt(String messageId) {
		this.messageId = messageId;
	}
	
	public AbstractTxnPrompt(String messageId, String txnType, String status, String emessage1,
			String emessage2, String emessage3, String emessage4, String cmessage1, String cmessage2, String cmessage3,
			String cmessage4, String operator, String approver, String modifiedTime) {
		super();
		this.messageId = messageId;
		this.txnType = txnType;
		this.status = status;
		Emessage1 = emessage1;
		Emessage2 = emessage2;
		Emessage3 = emessage3;
		Emessage4 = emessage4;
		Cmessage1 = cmessage1;
		Cmessage2 = cmessage2;
		Cmessage3 = cmessage3;
		Cmessage4 = cmessage4;
		this.operator = operator;
		this.approver = approver;
		this.modifiedTime = modifiedTime;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmessage1() {
		return Emessage1;
	}
	public void setEmessage1(String emessage1) {
		Emessage1 = emessage1;
	}
	public String getEmessage2() {
		return Emessage2;
	}
	public void setEmessage2(String emessage2) {
		Emessage2 = emessage2;
	}
	public String getEmessage3() {
		return Emessage3;
	}
	public void setEmessage3(String emessage3) {
		Emessage3 = emessage3;
	}
	public String getEmessage4() {
		return Emessage4;
	}
	public void setEmessage4(String emessage4) {
		Emessage4 = emessage4;
	}
	public String getCmessage1() {
		return Cmessage1;
	}
	public void setCmessage1(String cmessage1) {
		Cmessage1 = cmessage1;
	}
	public String getCmessage2() {
		return Cmessage2;
	}
	public void setCmessage2(String cmessage2) {
		Cmessage2 = cmessage2;
	}
	public String getCmessage3() {
		return Cmessage3;
	}
	public void setCmessage3(String cmessage3) {
		Cmessage3 = cmessage3;
	}
	public String getCmessage4() {
		return Cmessage4;
	}
	public void setCmessage4(String cmessage4) {
		Cmessage4 = cmessage4;
	}
	
	
}