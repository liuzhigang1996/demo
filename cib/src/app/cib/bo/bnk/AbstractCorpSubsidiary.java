/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Thu Mar 01 15:49:40 CST 2007 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.bnk;

import java.io.Serializable;

/**
 * A class that represents a row in the CORP_SUBSIDIARY table. 
 * You can customize the behavior of this class by editing the class, {@link CorpSubsidiary()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractCorpSubsidiary 
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.String relationId;

    /** The value of the simple afterModifyId property. */
    private java.lang.String afterModifyId;

    /** The value of the simple prefId property. */
    private java.lang.String prefId;

    /** The value of the simple perentId property. */
    private java.lang.String perentId;

    /** The value of the simple subsidiryId property. */
    private java.lang.String subsidiryId;

    /** The value of the simple status property. */
    private java.lang.String status;

    /** The value of the simple authStatus property. */
    private java.lang.String authStatus;

    /** The value of the simple operation property. */
    private java.lang.String operation;

    /** The value of the simple lastUpdateTime property. */
    private java.util.Date lastUpdateTime;

    /** The value of the simple requester property. */
    private java.lang.String requester;

    /**
     * Simple constructor of AbstractCorpSubsidiary instances.
     */
    public AbstractCorpSubsidiary()
    {
    }

    /**
     * Constructor of AbstractCorpSubsidiary instances given a simple primary key.
     * @param relationId
     */
    public AbstractCorpSubsidiary(java.lang.String relationId)
    {
        this.setRelationId(relationId);
    }

    /**
     * Return the simple primary key value that identifies this object.
     * @return java.lang.String
     */
    public java.lang.String getRelationId()
    {
        return relationId;
    }

    /**
     * Set the simple primary key value that identifies this object.
     * @param relationId
     */
    public void setRelationId(java.lang.String relationId)
    {
        this.hashValue = 0;
        this.relationId = relationId;
    }

    /**
     * Return the value of the AFTER_MODIFY_ID column.
     * @return java.lang.String
     */
    public java.lang.String getAfterModifyId()
    {
        return this.afterModifyId;
    }

    /**
     * Set the value of the AFTER_MODIFY_ID column.
     * @param afterModifyId
     */
    public void setAfterModifyId(java.lang.String afterModifyId)
    {
        this.afterModifyId = afterModifyId;
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
     * Return the value of the PERENT_ID column.
     * @return java.lang.String
     */
    public java.lang.String getPerentId()
    {
        return this.perentId;
    }

    /**
     * Set the value of the PERENT_ID column.
     * @param perentId
     */
    public void setPerentId(java.lang.String perentId)
    {
        this.perentId = perentId;
    }

    /**
     * Return the value of the SUBSIDIRY_ID column.
     * @return java.lang.String
     */
    public java.lang.String getSubsidiryId()
    {
        return this.subsidiryId;
    }

    /**
     * Set the value of the SUBSIDIRY_ID column.
     * @param subsidiryId
     */
    public void setSubsidiryId(java.lang.String subsidiryId)
    {
        this.subsidiryId = subsidiryId;
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
        if (! (rhs instanceof CorpSubsidiary))
            return false;
        CorpSubsidiary that = (CorpSubsidiary) rhs;
        if (this.getRelationId() == null || that.getRelationId() == null)
            return false;
        return (this.getRelationId().equals(that.getRelationId()));
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
            int relationIdValue = this.getRelationId() == null ? 0 : this.getRelationId().hashCode();
            result = result * 37 + relationIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }
}
