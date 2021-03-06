/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Wed Aug 23 16:28:38 CST 2006 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.txn;

import java.io.Serializable;

/**
 * A class that represents a row in the TXN_LIMIT_USAGE table. 
 * You can customize the behavior of this class by editing the class, {@link TxnLimitUsage()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractTxnLimitUsage 
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.String usageId;

    /** The value of the simple corpId property. */
    private java.lang.String corpId;

    /** The value of the simple prefId property. */
    private java.lang.String prefId;

    /** The value of the simple txnType property. */
    private java.lang.String txnType;

    /** The value of the simple account property. */
    private java.lang.String account;

    /** The value of the simple currency property. */
    private java.lang.String currency;

    /** The value of the simple usageDate property. */
    private java.lang.String usageDate;

    /** The value of the simple limit property. */
    private java.lang.Double limit;

    /** The value of the simple limit1 property. */
    private java.lang.Double limit1;

    /** The value of the simple limit2 property. */
    private java.lang.Double limit2;

    /** The value of the simple limit3 property. */
    private java.lang.Double limit3;

    /** The value of the simple limit4 property. */
    private java.lang.Double limit4;

    /** The value of the simple limit5 property. */
    private java.lang.Double limit5;

    /** The value of the simple limit6 property. */
    private java.lang.Double limit6;

    /** The value of the simple limit7 property. */
    private java.lang.Double limit7;

    /** The value of the simple limit8 property. */
    private java.lang.Double limit8;

    /** The value of the simple limit9 property. */
    private java.lang.Double limit9;

    /** The value of the simple limit10 property. */
    private java.lang.Double limit10;

    /**
     * Simple constructor of AbstractTxnLimitUsage instances.
     */
    public AbstractTxnLimitUsage()
    {
    }

    /**
     * Constructor of AbstractTxnLimitUsage instances given a simple primary key.
     * @param usageId
     */
    public AbstractTxnLimitUsage(java.lang.String usageId)
    {
        this.setUsageId(usageId);
    }

    /**
     * Return the simple primary key value that identifies this object.
     * @return java.lang.String
     */
    public java.lang.String getUsageId()
    {
        return usageId;
    }

    /**
     * Set the simple primary key value that identifies this object.
     * @param usageId
     */
    public void setUsageId(java.lang.String usageId)
    {
        this.hashValue = 0;
        this.usageId = usageId;
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
     * Return the value of the PREF_ID column.
     * @return java.lang.String
     */
    public java.lang.String getPrefId()
    {
        return this.prefId;
    }

    /**
     * Set the value of the PREF_ID column.
     * @param prefId
     */
    public void setPrefId(java.lang.String prefId)
    {
        this.prefId = prefId;
    }

    /**
     * Return the value of the TXN_TYPE column.
     * @return java.lang.String
     */
    public java.lang.String getTxnType()
    {
        return this.txnType;
    }

    /**
     * Set the value of the TXN_TYPE column.
     * @param txnType
     */
    public void setTxnType(java.lang.String txnType)
    {
        this.txnType = txnType;
    }

    /**
     * Return the value of the ACCOUNT column.
     * @return java.lang.String
     */
    public java.lang.String getAccount()
    {
        return this.account;
    }

    /**
     * Set the value of the ACCOUNT column.
     * @param account
     */
    public void setAccount(java.lang.String account)
    {
        this.account = account;
    }

    /**
     * Return the value of the CURRENCY column.
     * @return java.lang.String
     */
    public java.lang.String getCurrency()
    {
        return this.currency;
    }

    /**
     * Set the value of the CURRENCY column.
     * @param currency
     */
    public void setCurrency(java.lang.String currency)
    {
        this.currency = currency;
    }

    /**
     * Return the value of the USAGE_DATE column.
     * @return java.lang.String
     */
    public java.lang.String getUsageDate()
    {
        return this.usageDate;
    }

    /**
     * Set the value of the USAGE_DATE column.
     * @param usageDate
     */
    public void setUsageDate(java.lang.String usageDate)
    {
        this.usageDate = usageDate;
    }

    /**
     * Return the value of the LIMIT column.
     * @return java.lang.Double
     */
    public java.lang.Double getLimit()
    {
        return this.limit;
    }

    /**
     * Set the value of the LIMIT column.
     * @param limit
     */
    public void setLimit(java.lang.Double limit)
    {
        this.limit = limit;
    }

    /**
     * Return the value of the LIMIT1 column.
     * @return java.lang.Double
     */
    public java.lang.Double getLimit1()
    {
        return this.limit1;
    }

    /**
     * Set the value of the LIMIT1 column.
     * @param limit1
     */
    public void setLimit1(java.lang.Double limit1)
    {
        this.limit1 = limit1;
    }

    /**
     * Return the value of the LIMIT2 column.
     * @return java.lang.Double
     */
    public java.lang.Double getLimit2()
    {
        return this.limit2;
    }

    /**
     * Set the value of the LIMIT2 column.
     * @param limit2
     */
    public void setLimit2(java.lang.Double limit2)
    {
        this.limit2 = limit2;
    }

    /**
     * Return the value of the LIMIT3 column.
     * @return java.lang.Double
     */
    public java.lang.Double getLimit3()
    {
        return this.limit3;
    }

    /**
     * Set the value of the LIMIT3 column.
     * @param limit3
     */
    public void setLimit3(java.lang.Double limit3)
    {
        this.limit3 = limit3;
    }

    /**
     * Return the value of the LIMIT4 column.
     * @return java.lang.Double
     */
    public java.lang.Double getLimit4()
    {
        return this.limit4;
    }

    /**
     * Set the value of the LIMIT4 column.
     * @param limit4
     */
    public void setLimit4(java.lang.Double limit4)
    {
        this.limit4 = limit4;
    }

    /**
     * Return the value of the LIMIT5 column.
     * @return java.lang.Double
     */
    public java.lang.Double getLimit5()
    {
        return this.limit5;
    }

    /**
     * Set the value of the LIMIT5 column.
     * @param limit5
     */
    public void setLimit5(java.lang.Double limit5)
    {
        this.limit5 = limit5;
    }

    /**
     * Return the value of the LIMIT6 column.
     * @return java.lang.Double
     */
    public java.lang.Double getLimit6()
    {
        return this.limit6;
    }

    /**
     * Set the value of the LIMIT6 column.
     * @param limit6
     */
    public void setLimit6(java.lang.Double limit6)
    {
        this.limit6 = limit6;
    }

    /**
     * Return the value of the LIMIT7 column.
     * @return java.lang.Double
     */
    public java.lang.Double getLimit7()
    {
        return this.limit7;
    }

    /**
     * Set the value of the LIMIT7 column.
     * @param limit7
     */
    public void setLimit7(java.lang.Double limit7)
    {
        this.limit7 = limit7;
    }

    /**
     * Return the value of the LIMIT8 column.
     * @return java.lang.Double
     */
    public java.lang.Double getLimit8()
    {
        return this.limit8;
    }

    /**
     * Set the value of the LIMIT8 column.
     * @param limit8
     */
    public void setLimit8(java.lang.Double limit8)
    {
        this.limit8 = limit8;
    }

    /**
     * Return the value of the LIMIT9 column.
     * @return java.lang.Double
     */
    public java.lang.Double getLimit9()
    {
        return this.limit9;
    }

    /**
     * Set the value of the LIMIT9 column.
     * @param limit9
     */
    public void setLimit9(java.lang.Double limit9)
    {
        this.limit9 = limit9;
    }

    /**
     * Return the value of the LIMIT10 column.
     * @return java.lang.Double
     */
    public java.lang.Double getLimit10()
    {
        return this.limit10;
    }

    /**
     * Set the value of the LIMIT10 column.
     * @param limit10
     */
    public void setLimit10(java.lang.Double limit10)
    {
        this.limit10 = limit10;
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
        if (! (rhs instanceof TxnLimitUsage))
            return false;
        TxnLimitUsage that = (TxnLimitUsage) rhs;
        if (this.getUsageId() == null || that.getUsageId() == null)
            return false;
        return (this.getUsageId().equals(that.getUsageId()));
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
            int usageIdValue = this.getUsageId() == null ? 0 : this.getUsageId().hashCode();
            result = result * 37 + usageIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }
}
