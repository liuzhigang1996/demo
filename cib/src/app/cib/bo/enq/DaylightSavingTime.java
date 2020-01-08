/**
 * @author hjs
 * 2007-4-27
 */
package app.cib.bo.enq;

/**
 * @author hjs
 * 2007-4-27
 */
public class DaylightSavingTime {
	
	private String cityName;
	private String dstBeginTime;
	private String dstEndTime;
	private String dstTimeZone;
	private String dstTimeZoneName;
	private String timeZoneName;
	private boolean flag = false;
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getDstBeginTime() {
		return dstBeginTime;
	}
	public void setDstBeginTime(String dstBeginTime) {
		this.dstBeginTime = dstBeginTime;
	}
	public String getDstEndTime() {
		return dstEndTime;
	}
	public void setDstEndTime(String dstEndTime) {
		this.dstEndTime = dstEndTime;
	}
	public String getDstTimeZone() {
		return dstTimeZone;
	}
	public void setDstTimeZone(String dstTimeZone) {
		this.dstTimeZone = dstTimeZone;
	}
	public String getDstTimeZoneName() {
		return dstTimeZoneName;
	}
	public void setDstTimeZoneName(String dstTimeZoneName) {
		this.dstTimeZoneName = dstTimeZoneName;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getTimeZoneName() {
		return timeZoneName;
	}
	public void setTimeZoneName(String timeZoneName) {
		this.timeZoneName = timeZoneName;
	}
	

}
