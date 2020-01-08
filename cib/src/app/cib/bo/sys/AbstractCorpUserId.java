package app.cib.bo.sys;

/**
 * AbstractCorpUserId entity provides the base persistence definition of the
 * CorpUserId entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractCorpUserId implements java.io.Serializable {

	// Fields

	private String userId;
	private String corpId;

	// Constructors

	/** default constructor */
	public AbstractCorpUserId() {
	}

	/** full constructor */
	public AbstractCorpUserId(String userId, String corpId) {
		this.userId = userId;
		this.corpId = corpId;
	}

	// Property accessors

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCorpId() {
		return this.corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AbstractCorpUserId))
			return false;
		AbstractCorpUserId castOther = (AbstractCorpUserId) other;

		return ((this.getUserId() == castOther.getUserId()) || (this
				.getUserId() != null
				&& castOther.getUserId() != null && this.getUserId().equals(
				castOther.getUserId())))
				&& ((this.getCorpId() == castOther.getCorpId()) || (this
						.getCorpId() != null
						&& castOther.getCorpId() != null && this.getCorpId()
						.equals(castOther.getCorpId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result
				+ (getCorpId() == null ? 0 : this.getCorpId().hashCode());
		return result;
	}

}