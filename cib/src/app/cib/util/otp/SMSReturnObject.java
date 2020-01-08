package app.cib.util.otp;

public class SMSReturnObject implements java.io.Serializable{

	private String returnPage ;
	private boolean returnFlag ;
	private boolean returnValid ;
	
	private String errorFlag="N";
	private String returnErr;
	
	public String getReturnErr() {
		return returnErr;
	}
	public void setReturnErr(String returnErr) {
		this.returnErr = returnErr;
	}
	public String getErrorFlag() {
		return errorFlag;
	}
	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}
	public String getReturnPage() {
		return returnPage;
	}
	public void setReturnPage(String returnPage) {
		this.returnPage = returnPage;
	}
	public boolean isReturnFlag() {
		return returnFlag;
	}
	public void setReturnFlag(boolean returnFlag) {
		this.returnFlag = returnFlag;
	}
	public boolean isReturnValid() {
		return returnValid;
	}
	public void setReturnValid(boolean returnValid) {
		this.returnValid = returnValid;
	}
	
}
