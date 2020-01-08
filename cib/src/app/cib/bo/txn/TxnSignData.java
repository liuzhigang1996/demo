/*
 * Created Tue Nov 21 18:27:47 CST 2006 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.txn;

import java.io.Serializable;

/**
 * A class that represents a row in the 'TXN_SIGN_DATA' table.
 * This class may be customized as it is never re-generated
 * after being created.
 */
public class TxnSignData extends AbstractTxnSignData implements Serializable {

    public static final String SIGN_ACTION_APPROVE = "APPROVE";
    public static final String SIGN_ACTION_REJECT = "REJECT";
    public static final String SIGN_ACTION_CANCEL = "CANCEL";
    public static final String SIGN_ACTION_INPUT = "INPUT";

    public static final String ACTION_RESULT_FAILED = "F";
    public static final String ACTION_RESULT_ACCOMPLISHED = "A";

    /**
     * Simple constructor of TxnSignData instances.
     */
    public TxnSignData() {
    }

    /**
     * Constructor of TxnSignData instances given a simple primary key.
     * @param seqNo
     */
    public TxnSignData(java.lang.String seqNo) {
        super(seqNo);
    }

    /* Add customized code below */

}
