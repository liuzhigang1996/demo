/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Tue Sep 26 18:41:38 CST 2006 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.sys;

import java.io.Serializable;

/**
 * A class that represents a row in the CORP_PREFERENCE table. 
 * You can customize the behavior of this class by editing the class, {@link CorpPreference()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractCorpPreference 
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.String prefId;

    /** The value of the simple relativeId property. */
    private java.lang.String relativeId;

    /** The value of the simple corpId property. */
    private java.lang.String corpId;

    /** The value of the simple prefType property. */
    private java.lang.String prefType;

    /** The value of the simple prefDesc property. */
    private java.lang.String prefDesc;

    /** The value of the simple status property. */
    private java.lang.String status;

    /** The value of the simple authStatus property. */
    private java.lang.String authStatus;

    /** The value of the simple version property. */
    private java.lang.Integer version;

    /** The value of the simple lastUpdateTime property. */
    private java.util.Date lastUpdateTime;

    /** The value of the simple requester property. */
    private java.lang.String requester;

    /**
     * Simple constructor of AbstractCorpPreference instances.
     */
    public AbstractCorpPreference()
    {
    }

    /**
     * Constructor of AbstractCorpPreference instances given a simple primary key.
     * @param prefId
     */
    public AbstractCorpPreference(java.lang.String prefId)
    {
        this.setPrefId(prefId);
    }

    /**
     * Return the simple primary key value that identifies this object.
     * @return java.lang.String
     */
    public java.lang.String getPrefId()
    {
        return prefId;
    }

    /**
     * Set the simple primary key value that identifies this object.
     * @param prefId
     */
    public void setPrefId(java.lang.String prefId)
    {
        this.hashValue = 0;
        this.prefId = prefId;
    }

    /**
     * Return the value of the RELATIVE_ID column.
     * @return java.lang.String
     */
    public java.lang.String getRelativeId()
    {
        return this.relativeId;
    }

    /**
     * Set the value of the RELATIVE_ID column.
     * @param relativeId
     */
    public void setRelativeId(java.lang.String relativeId)
    {
        this.relativeId = relativeId;
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
     * Return the value of the PREF_TYPE column.
     * @return java.lang.String
     */
    public java.lang.String getPrefType()
    {
        return this.prefType;
    }

    /**
     * Set the value of the PREF_TYPE column.
     * @param prefType
     */
    public void setPrefType(java.lang.String prefType)
    {
        this.prefType = prefType;
    }

    /**
     * Return the value of the PREF_DESC column.
     * @return java.lang.String
     */
    public java.lang.String getPrefDesc()
    {
        return this.prefDesc;
    }

    /**
     * Set the value of the PREF_DESC column.
     * @param prefDesc
     */
    public void setPrefDesc(java.lang.String prefDesc)
    {
        this.prefDesc = prefDesc;
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

    /**
     * Return the value of the VERSION column.
     * @return java.lang.Integer
     */
    public java.lang.Integer getVersion()
    {
        return this.version;
    }

    /**
     * Set the value of the VERSION column.
     * @param version
     */
    public void setVersion(java.lang.Integer version)
    {
        this.version = version;
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
     * Implementation of the equals comparison on the basis of equality of the primary key values.
     * @param rhs
     * @return boolean
     */
    public boolean equals(Object rhs)
    {
        if (rhs == null)
            return false;
        if (! (rhs instanceof CorpPreference))
            return false;
        CorpPreference that = (CorpPreference) rhs;
        if (this.getPrefId() == null || that.getPrefId() == null)
            return false;
        return (this.getPrefId().equals(that.getPrefId()));
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
            int prefIdValue = this.getPrefId() == null ? 0 : this.getPrefId().hashCode();
            result = result * 37 + prefIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }
}