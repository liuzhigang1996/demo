package app.cib.bo.flow;

import java.io.Serializable;

public class WorkObject implements Serializable {
	
	private java.lang.String workId;
	private java.util.Date timeIn;
	private java.lang.String workCreator;
	private java.lang.String workCreateTime;
	
	public java.lang.String getWorkCreateTime() {
		return workCreateTime;
	}
	public void setWorkCreateTime(java.lang.String workCreateTime) {
		this.workCreateTime = workCreateTime;
	}
	public java.util.Date getTimeIn() {
		return timeIn;
	}
	public void setTimeIn(java.util.Date timeIn) {
		this.timeIn = timeIn;
	}
	
	public java.lang.String getWorkCreator() {
		return workCreator;
	}
	public void setWorkCreator(java.lang.String workCreator) {
		this.workCreator = workCreator;
	}
	public java.lang.String getWorkId() {
		return workId;
	}
	public void setWorkId(java.lang.String workId) {
		this.workId = workId;
	}
	
}
