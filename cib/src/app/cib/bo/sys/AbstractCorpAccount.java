/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Thu Sep 28 16:59:31 CST 2006 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.sys;

import java.io.Serializable;

/**
 * A class that represents a row in the CORP_ACCOUNT table. 
 * You can customize the behavior of this class by editing the class, {@link CorpAccount()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractCorpAccount 
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.String accountNo;

    /** The value of the simple accountType property. */
    private java.lang.String accountType;

    /** The value of the simple currency property. */
    private java.lang.String currency;

    /** The value of the simple prefId property. */
    private java.lang.String prefId;

    /** The value of the simple corpId property. */
    private java.lang.String corpId;

    /** The value of the simple accountName property. */
    private java.lang.String accountName;

    /** The value of the simple accountDesc property. */
    private java.lang.String accountDesc;

    /** The value of the simple operation property. */
    private java.lang.String operation;

    /** The value of the simple status property. */
    private java.lang.String status;

    /** The value of the simple lastUpdateTime property. */
    private java.util.Date lastUpdateTime;

    /** The value of the simple requester property. */
    private java.lang.String requester;

    /** The value of the simple authStatus property. */
    private java.lang.String authStatus;
    
    /** The value of the simple cifNo property. */
    private java.lang.String cifNo;

    /**
     * Simple constructor of AbstractCorpAccount instances.
     */
    public AbstractCorpAccount()
    {
    }

    /**
     * Constructor of AbstractCorpAccount instances given a simple primary key.
     * @param accountNo
     */
    public AbstractCorpAccount(java.lang.String accountNo)
    {
        this.setAccountNo(accountNo);
    }

    /**
     * Return the simple primary key value that identifies this object.
     * @return java.lang.String
     */
    public java.lang.String getAccountNo()
    {
        return accountNo;
    }

    /**
     * Set the simple primary key value that identifies this object.
     * @param accountNo
     */
    public void setAccountNo(java.lang.String accountNo)
    {
        this.hashValue = 0;
        this.accountNo = accountNo;
    }

    /**
     * Return the value of the ACCOUNT_TYPE column.
     * @return java.lang.String
     */
    public java.lang.String getAccountType()
    {
        return this.accountType;
    }

    /**
     * Set the value of the ACCOUNT_TYPE column.
     * @param accountType
     */
    public void setAccountType(java.lang.String accountType)
    {
        this.accountType = accountType;
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
     * Return the value of the ACCOUNT_NAME column.
     * @return java.lang.String
     */
    public java.lang.String getAccountName()
    {
        return this.accountName;
    }

    /**
     * Set the value of the ACCOUNT_NAME column.
     * @param accountName
     */
    public void setAccountName(java.lang.String accountName)
    {
        this.accountName = accountName;
    }

    /**
     * Return the value of the ACCOUNT_DESC column.
     * @return java.lang.String
     */
    public java.lang.String getAccountDesc()
    {
        return this.accountDesc;
    }

    /**
     * Set the value of the ACCOUNT_DESC column.
     * @param accountDesc
     */
    public void setAccountDesc(java.lang.String accountDesc)
    {
        this.accountDesc = accountDesc;
    }

    /**
     * Return the value of the OPERATION column.
     * @return java.lang.String
     */
    public java.lang.String getOperation()
    {
        return this.operation;
    }

    /**
     * Set the value of the OPERATION column.
     * @param operation
     */
    public void setOperation(java.lang.String operation)
    {
        this.operation = operation;
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
     * Return the value of the LAST_UPDATE_TIME column.
     * @return java.util.Date
     */
    public java.util.Date getLastUpdateTime()
    {
        return this.lastUpdateTime;
    }

    /**
     * Set the value of the LAST_UPDATE_TIME column.
     * @param lastUpdateTime
     */
    public void setLastUpdateTime(java.util.Date lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * Return the value of the REQUESTER column.
     * @return java.lang.String
     */
    public java.lang.String getRequester()
    {
        return this.requester;
    }

    /**
     * Set the value of the REQUESTER column.
     * @param requester
     */
    public void setRequester(java.lang.String requester)
    {
        this.requester = requester;
    }

    /**
     * Return the value of the AUTH_STATUS column.
     * @return java.lang.String
     */
    public java.lang.String getAuthStatus()
    {
        return this.authStatus;
    }

    /**
     * Set the value of the AUTH_STATUS column.
     * @param authStatus
     */
    public void setAuthStatus(java.lang.String authStatus)
    {
        this.authStatus = authStatus;
    }
 //add by linrui 20180408
    public java.lang.String getCifNo() {
		return this.cifNo;
	}

	public void setCifNo(java.lang.String cifNo) {
		this.cifNo = cifNo;
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
        if (! (rhs instanceof CorpAccount))
            return false;
        CorpAccount that = (CorpAccount) rhs;
        if (this.getAccountNo() == null || that.getAccountNo() == null)
            return false;
        return (this.getAccountNo().equals(that.getAccountNo()));
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
            int accountNoValue = this.getAccountNo() == null ? 0 : this.getAccountNo().hashCode();
            result = result * 37 + accountNoValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }
}
