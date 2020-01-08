package app.cib.bo.txn;

/**
 * AbstractAutopayMerchantNameId entity provides the base persistence definition
 * of the AutopayMerchantNameId entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractAutopayMerchantNameId implements
		java.io.Serializable {

	// Fields

	private String apsCode;
	private String lang;

	// Constructors

	/** default constructor */
	public AbstractAutopayMerchantNameId() {
	}

	/** full constructor */
	public AbstractAutopayMerchantNameId(String apsCode, String lang) {
		this.apsCode = apsCode;
		this.lang = lang;
	}

	// Property accessors

	public String getApsCode() {
		return this.apsCode;
	}

	public void setApsCode(String apsCode) {
		this.apsCode = apsCode;
	}

	public String getLang() {
		return this.lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AbstractAutopayMerchantNameId))
			return false;
		AbstractAutopayMerchantNameId castOther = (AbstractAutopayMerchantNameId) other;

		return ((this.getApsCode() == castOther.getApsCode()) || (this
				.getApsCode() != null
				&& castOther.getApsCode() != null && this.getApsCode().equals(
				castOther.getApsCode())))
				&& ((this.getLang() == castOther.getLang()) || (this.getLang() != null
						&& castOther.getLang() != null && this.getLang()
						.equals(castOther.getLang())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getApsCode() == null ? 0 : this.getApsCode().hashCode());
		result = 37 * result
				+ (getLang() == null ? 0 : this.getLang().hashCode());
		return result;
	}

}