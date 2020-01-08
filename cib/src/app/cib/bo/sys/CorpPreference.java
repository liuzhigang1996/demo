/*
 * Created Mon Jul 31 12:15:11 CST 2006 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.sys;

import java.io.Serializable;

/**
 * A class that represents a row in the 'CORP_PREFERENCE' table.
 * This class may be customized as it is never re-generated
 * after being created.
 */
public class CorpPreference extends AbstractCorpPreference implements
        Serializable {

	private static final long serialVersionUID = 76513074179587417L;
	
	public static final String PREF_TYPE_ACCOUNT = "1";
    public static final String PREF_TYPE_LIMIT = "2";
    public static final String PREF_TYPE_AUTHORIZATION = "3";
    public static final String PREF_TYPE_CORP_USER= "4";
    public static final String PREF_TYPE_SUBSIDIARY= "5";
    public static final String PREF_TYPE_GROUP_ASSIGN= "6";
    /**
     * Simple constructor of CorpPreference instances.
     */
    public CorpPreference() {
    }

    /**
     * Constructor of CorpPreference instances given a simple primary key.
     * @param prefId
     */
    public CorpPreference(java.lang.String prefId) {
        super(prefId);
    }

    /* Add customized code below */

}
