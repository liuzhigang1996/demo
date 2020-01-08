/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Thu Mar 01 15:52:11 CST 2007 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.bnk;

import java.io.Serializable;

/**
 * A class that represents a row in the BANK_USER table. 
 * You can customize the behavior of this class by editing the class, {@link BankUser()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractBankUser 
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.String userId;

    /** The value of the simple prefId property. */
    private java.lang.String prefId;

    /** The value of the simple userPassword property. */
    private java.lang.String userPassword;

    /** The value of the simple userName property. */
    private java.lang.String userName;

    /** The value of the simple roleId property. */
    private java.lang.String roleId;

    /** The value of the simple email property. */
    private java.lang.String email;

    /** The value of the simple userDesc property. */
    private java.lang.String userDesc;

    /** The value of the simple loginStatus property. */
    private java.lang.String loginStatus;

    /** The value of the simple loginTimes property. */
    private java.lang.Integer loginTimes;

    /** The value of the simple loginFailTimes property. */
    private java.lang.Integer loginFailTimes;

    /** The value of the simple currLoginIp property. */
    private java.lang.String currLoginIp;

    /** The value of the simple currLoginTime property. */
    private java.util.Date currLoginTime;

    /** The value of the simple prevLoginIp property. */
    private java.lang.String prevLoginIp;

    /** The value of the simple prevLoginTime property. */
    private java.util.Date prevLoginTime;

    /** The value of the simple pervLoginStatus property. */
    private java.lang.String pervLoginStatus;

    /** The value of the simple operation property. */
    private java.lang.String operation;

    /** The value of the simple blockReason property. */
    private java.lang.String blockReason;

    /** The value of the simple status property. */
    private java.lang.String status;

    /** The value of the simple authStatus property. */
    private java.lang.String authStatus;

    /** The value of the simple updateId property. */
    private java.lang.String updateId;

    /** The value of the simple lastUpdateTime property. */
    private java.util.Date lastUpdateTime;

    /** The value of the simple requester property. */
    private java.lang.String requester;

    /**
     * Simple constructor of AbstractBankUser instances.
     */
    public AbstractBankUser()
    {
    }

    /**
     * Constructor of AbstractBankUser instances given a simple primary key.
     * @param userId
     */
    public AbstractBankUser(java.lang.String userId)
    {
        this.setUserId(userId);
    }

    /**
     * Return the simple primary key value that identifies this object.
     * @return java.lang.String
     */
    public java.lang.String getUserId()
    {
        return userId;
    }

    /**
     * Set the simple primary key value that identifies this object.
     * @param userId
     */
    public void setUserId(java.lang.String userId)
    {
        this.hashValue = 0;
        this.userId = userId;
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
     * Return the value of the USER_PASSWORD column.
     * @return java.lang.String
     */
    public java.lang.String getUserPassword()
    {
        return this.userPassword;
    }

    /**
     * Set the value of the USER_PASSWORD column.
     * @param userPassword
     */
    public void setUserPassword(java.lang.String userPassword)
    {
        this.userPassword = userPassword;
    }

    /**
     * Return the value of the USER_NAME column.
     * @return java.lang.String
     */
    public java.lang.String getUserName()
    {
        return this.userName;
    }

    /**
     * Set the value of the USER_NAME column.
     * @param userName
     */
    public void setUserName(java.lang.String userName)
    {
        this.userName = userName;
    }

    /**
     * Return the value of the ROLE_ID column.
     * @return java.lang.String
     */
    public java.lang.String getRoleId()
    {
        return this.roleId;
    }

    /**
     * Set the value of the ROLE_ID column.
     * @param roleId
     */
    public void setRoleId(java.lang.String roleId)
    {
        this.roleId = roleId;
    }

    /**
     * Return the value of the EMAIL column.
     * @return java.lang.String
     */
    public java.lang.String getEmail()
    {
        return this.email;
    }

    /**
     * Set the value of the EMAIL column.
     * @param email
     */
    public void setEmail(java.lang.String email)
    {
        this.email = email;
    }

    /**
     * Return the value of the USER_DESC column.
     * @return java.lang.String
     */
    public java.lang.String getUserDesc()
    {
        return this.userDesc;
    }

    /**
     * Set the value of the USER_DESC column.
     * @param userDesc
     */
    public void setUserDesc(java.lang.String userDesc)
    {
        this.userDesc = userDesc;
    }

    /**
     * Return the value of the LOGIN_STATUS column.
     * @return java.lang.String
     */
    public java.lang.String getLoginStatus()
    {
        return this.loginStatus;
    }

    /**
     * Set the value of the LOGIN_STATUS column.
     * @param loginStatus
     */
    public void setLoginStatus(java.lang.String loginStatus)
    {
        this.loginStatus = loginStatus;
    }

    /**
     * Return the value of the LOGIN_TIMES column.
     * @return java.lang.Integer
     */
    public java.lang.Integer getLoginTimes()
    {
        return this.loginTimes;
    }

    /**
     * Set the value of the LOGIN_TIMES column.
     * @param loginTimes
     */
    public void setLoginTimes(java.lang.Integer loginTimes)
    {
        this.loginTimes = loginTimes;
    }

    /**
     * Return the value of the LOGIN_FAIL_TIMES column.
     * @return java.lang.Integer
     */
    public java.lang.Integer getLoginFailTimes()
    {
        return this.loginFailTimes;
    }

    /**
     * Set the value of the LOGIN_FAIL_TIMES column.
     * @param loginFailTimes
     */
    public void setLoginFailTimes(java.lang.Integer loginFailTimes)
    {
        this.loginFailTimes = loginFailTimes;
    }

    /**
     * Return the value of the CURR_LOGIN_IP column.
     * @return java.lang.String
     */
    public java.lang.String getCurrLoginIp()
    {
        return this.currLoginIp;
    }

    /**
     * Set the value of the CURR_LOGIN_IP column.
     * @param currLoginIp
     */
    public void setCurrLoginIp(java.lang.String currLoginIp)
    {
        this.currLoginIp = currLoginIp;
    }

    /**
     * Return the value of the CURR_LOGIN_TIME column.
     * @return java.util.Date
     */
    public java.util.Date getCurrLoginTime()
    {
        return this.currLoginTime;
    }

    /**
     * Set the value of the CURR_LOGIN_TIME column.
     * @param currLoginTime
     */
    public void setCurrLoginTime(java.util.Date currLoginTime)
    {
        this.currLoginTime = currLoginTime;
    }

    /**
     * Return the value of the PREV_LOGIN_IP column.
     * @return java.lang.String
     */
    public java.lang.String getPrevLoginIp()
    {
        return this.prevLoginIp;
    }

    /**
     * Set the value of the PREV_LOGIN_IP column.
     * @param prevLoginIp
     */
    public void setPrevLoginIp(java.lang.String prevLoginIp)
    {
        this.prevLoginIp = prevLoginIp;
    }

    /**
     * Return the value of the PREV_LOGIN_TIME column.
     * @return java.util.Date
     */
    public java.util.Date getPrevLoginTime()
    {
        return this.prevLoginTime;
    }

    /**
     * Set the value of the PREV_LOGIN_TIME column.
     * @param prevLoginTime
     */
    public void setPrevLoginTime(java.util.Date prevLoginTime)
    {
        this.prevLoginTime = prevLoginTime;
    }

    /**
     * Return the value of the PERV_LOGIN_STATUS column.
     * @return java.lang.String
     */
    public java.lang.String getPervLoginStatus()
    {
        return this.pervLoginStatus;
    }

    /**
     * Set the value of the PERV_LOGIN_STATUS column.
     * @param pervLoginStatus
     */
    public void setPervLoginStatus(java.lang.String pervLoginStatus)
    {
        this.pervLoginStatus = pervLoginStatus;
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
     * Return the value of the BLOCK_REASON column.
     * @return java.lang.String
     */
    public java.lang.String getBlockReason()
    {
        return this.blockReason;
    }

    /**
     * Set the value of the BLOCK_REASON column.
     * @param blockReason
     */
    public void setBlockReason(java.lang.String blockReason)
    {
        this.blockReason = blockReason;
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
     * Return the value of the UPDATE_ID column.
     * @return java.lang.String
     */
    public java.lang.String getUpdateId()
    {
        return this.updateId;
    }

    /**
     * Set the value of the UPDATE_ID column.
     * @param updateId
     */
    public void setUpdateId(java.lang.String updateId)
    {
        this.updateId = updateId;
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
        if (! (rhs instanceof BankUser))
            return false;
        BankUser that = (BankUser) rhs;
        if (this.getUserId() == null || that.getUserId() == null)
            return false;
        return (this.getUserId().equals(that.getUserId()));
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
            int userIdValue = this.getUserId() == null ? 0 : this.getUserId().hashCode();
            result = result * 37 + userIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }
}