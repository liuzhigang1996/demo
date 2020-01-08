package app.cib.bo.sys;

/**
 * CorpUserId entity. @author MyEclipse Persistence Tools
 */
public class CorpUserId extends AbstractCorpUserId implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public CorpUserId() {
	}

	/** full constructor */
	public CorpUserId(String userId, String corpId) {
		super(userId, corpId);
	}

}
