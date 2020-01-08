/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Mon Jul 17 10:29:27 CST 2006 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.txn;

import java.io.Serializable;

/**
 * A class that represents a row in the FILE_TRANSFER table. 
 * You can customize the behavior of this class by editing the class, {@link FileTransfer()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractFileTransfer 
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.String transId;

    /** The value of the simple userId property. */
    private java.lang.String userId;

    /** The value of the simple corpId property. */
    private java.lang.String corpId;

    /** The value of the simple beneficiaryType property. */
    private java.lang.String beneficiaryType;

    /** The value of the simple frequnceType property. */
    private java.lang.String frequnceType;

    /** The value of the simple frequnceDays property. */
    private java.lang.String frequnceDays;

    /** The value of the simple status property. */
    private java.lang.String status;

    /**
     * Simple constructor of AbstractFileTransfer instances.
     */
    public AbstractFileTransfer()
    {
    }

    /**
     * Constructor of AbstractFileTransfer instances given a simple primary key.
     * @param transId
     */
    public AbstractFileTransfer(java.lang.String transId)
    {
        this.setTransId(transId);
    }

    /**
     * Return the simple primary key value that identifies this object.
     * @return java.lang.String
     */
    public java.lang.String getTransId()
    {
        return transId;
    }

    /**
     * Set the simple primary key value that identifies this object.
     * @param transId
     */
    public void setTransId(java.lang.String transId)
    {
        this.hashValue = 0;
        this.transId = transId;
    }

    /**
     * Return the value of the USER_ID column.
     * @return java.lang.String
     */
    public java.lang.String getUserId()
    {
        return this.userId;
    }

    /**
     * Set the value of the USER_ID column.
     * @param userId
     */
    public void setUserId(java.lang.String userId)
    {
        this.userId = userId;
    }

    /**
     * Return the value of the CORP_ID column.
     * @return java.lang.String
     */
    public java.lang.String getCorpId()
    {
        return this.corpId;
    }

    /**
     * Set the value of the CORP_ID column.
     * @param corpId
     */
    public void setCorpId(java.lang.String corpId)
    {
        this.corpId = corpId;
    }

    /**
     * Return the value of the BENEFICIARY_TYPE column.
     * @return java.lang.String
     */
    public java.lang.String getBeneficiaryType()
    {
        return this.beneficiaryType;
    }

    /**
     * Set the value of the BENEFICIARY_TYPE column.
     * @param beneficiaryType
     */
    public void setBeneficiaryType(java.lang.String beneficiaryType)
    {
        this.beneficiaryType = beneficiaryType;
    }

    /**
     * Return the value of the FREQUNCE_TYPE column.
     * @return java.lang.String
     */
    public java.lang.String getFrequnceType()
    {
        return this.frequnceType;
    }

    /**
     * Set the value of the FREQUNCE_TYPE column.
     * @param frequnceType
     */
    public void setFrequnceType(java.lang.String frequnceType)
    {
        this.frequnceType = frequnceType;
    }

    /**
     * Return the value of the FREQUNCE_DAYS column.
     * @return java.lang.String
     */
    public java.lang.String getFrequnceDays()
    {
        return this.frequnceDays;
    }

    /**
     * Set the value of the FREQUNCE_DAYS column.
     * @param frequnceDays
     */
    public void setFrequnceDays(java.lang.String frequnceDays)
    {
        this.frequnceDays = frequnceDays;
    }

    /**
     * Return the value of the STATUS column.
     * @return java.lang.String
     */
    public java.lang.String getStatus()
    {
        return this.status;
    }

    /**
     * Set the value of the STATUS column.
     * @param status
     */
    public void setStatus(java.lang.String status)
    {
        this.status = status;
    }

    /**
     * Implementation of the equals comparison on the basis of equality of the primary key values.
     * @param rhs
     * @return boolean
     */
    public boolean equals(Object rhs)
    {
        if (rhs == null)
            return false;
        if (! (rhs instanceof FileTransfer))
            return false;
        FileTransfer that = (FileTransfer) rhs;
        if (this.getTransId() == null || that.getTransId() == null)
            return false;
        return (this.getTransId().equals(that.getTransId()));
    }

    /**
     * Implementation of the hashCode method conforming to the Bloch pattern with
     * the exception of array properties (these are very unlikely primary key types).
     * @return int
     */
    public int hashCode()
    {
        if (this.hashValue == 0)
        {
            int result = 17;
            int transIdValue = this.getTransId() == null ? 0 : this.getTransId().hashCode();
            result = result * 37 + transIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }
}
