package app.cib.bo.bat;

import java.util.List;
import java.util.Map;

public class FileRequestFileBean {
	private List normalList;
	private List errList;
	private double normalTotleAmt;
	private double normalTotalAmt;
	private double errTotleAmt;
	private double errTotalAmt;
	private int allCount;
	private Map fileRequestHeader;
	private boolean errFlag;
	public int getAllCount() {
		return allCount;
	}
	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}
	public List getErrList() {
		return errList;
	}
	public void setErrList(List errList) {
		this.errList = errList;
	}
	public double getErrTotleAmt() {
		return errTotleAmt;
	}
	public void setErrTotleAmt(double errTotleAmt) {
		this.errTotleAmt = errTotleAmt;
	}
	public Map getFileRequestHeader() {
		return fileRequestHeader;
	}
	public void setFileRequestHeader(Map fileRequestHeader) {
		this.fileRequestHeader = fileRequestHeader;
	}
	public List getNormalList() {
		return normalList;
	}
	public void setNormalList(List normalList) {
		this.normalList = normalList;
	}
	public double getNormalTotleAmt() {
		return normalTotleAmt;
	}
	public void setNormalTotleAmt(double normalTotleAmt) {
		this.normalTotleAmt = normalTotleAmt;
	}
	public boolean isErrFlag() {
		return errFlag;
	}
	public void setErrFlag(boolean errFlag) {
		this.errFlag = errFlag;
	}
	public double getErrTotalAmt() {
		return errTotalAmt;
	}
	public void setErrTotalAmt(double errTotalAmt) {
		this.errTotalAmt = errTotalAmt;
	}
	public double getNormalTotalAmt() {
		return normalTotalAmt;
	}
	public void setNormalTotalAmt(double normalTotalAmt) {
		this.normalTotalAmt = normalTotalAmt;
	}

}
