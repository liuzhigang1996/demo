package app.cib.bo.bnk;

// Generated by MyEclipse - Hibernate Tools



/**
 * Corporation generated by MyEclipse - Hibernate Tools
 */
public class Corporation extends AbstractCorporation implements java.io.
        Serializable {

    public static final String CORP_STATUS_EXIST = "C00";//1 mod by linrui 20180211
    public static final String CORP_STATUS_CLOSE = "C01";//2 mod by linrui 20180211 
    public static final String CORP_STATUS_NOTFOUND = "3";

    // Constructors

    /** default constructor */
    public Corporation() {
    }

    /** minimal constructor */
    public Corporation(String corpId) {
        super(corpId);
    }

}
