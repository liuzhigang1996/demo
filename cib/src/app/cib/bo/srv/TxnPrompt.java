package app.cib.bo.srv;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * TxnPrompt entity. @author MyEclipse Persistence Tools
 */
public class TxnPrompt extends AbstractTxnPrompt implements
		java.io.Serializable {

	// Constructors
	/** default constructor */
	public TxnPrompt() {
	}
	/** minimal constructor */
	public TxnPrompt(String messageId) {
		super(messageId);
	}
	
	/** full constructor */
	public TxnPrompt(String messageId, String txnType, String status, String emessage1, String emessage2,
			String emessage3, String emessage4, String cmessage1, String cmessage2, String cmessage3, String cmessage4,
			String operator, String approver, String modifiedTime) {
		super(messageId, txnType, status, emessage1, emessage2, emessage3, emessage4, cmessage1, cmessage2, cmessage3,
				cmessage4, operator, approver, modifiedTime);
		// TODO Auto-generated constructor stub
	}
	
	public String getDescription(String lang){
		String description = "";
		if(lang == "E"){
			if(getEmessage1() != null)
				description = description + getEmessage1();
			if(getEmessage2() != null)
				description = description + getEmessage2();
			if(getEmessage3() != null)
				description = description + getEmessage3();
			if(getEmessage4() != null)
				description = description +getEmessage4();
		}
		if(lang == "C"){
			if(getCmessage1() != null)
				description = description + getCmessage1();
			if(getCmessage2() != null)
				description = description + getCmessage2();
			if(getCmessage3() != null)
				description = description + getCmessage3();
			if(getCmessage4() != null)
				description = description +getCmessage4();
		}
		return description;
	}
	public void setDescription(String description,String lang){
		if("E".equals(lang)){
			setEmessage1(description);
		}else if("C".equals(lang)){
			setCmessage1(description);
		}
	}
	public void clearDescription() {
		setEmessage1(null);
		setEmessage2(null);
		setEmessage3(null);
		setEmessage4(null);
		setCmessage1(null);
		setCmessage2(null);
		setCmessage3(null);
		setCmessage4(null);
		
	}
	
}
