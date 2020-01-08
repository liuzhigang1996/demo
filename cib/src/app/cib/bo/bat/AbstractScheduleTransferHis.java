/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Mon Dec 18 10:48:54 CST 2006 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.bat;

import java.io.Serializable;

/**
 * A class that represents a row in the SCHEDULE_TRANSFER_HIS table. 
 * You can customize the behavior of this class by editing the class, {@link ScheduleTransferHis()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractScheduleTransferHis 
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.String seqNo;

    /** The value of the simple scheduleId property. */
    private java.lang.String scheduleId;

    /** The value of the simple userId property. */
    private java.lang.String userId;

    /** The value of the simple corporaitonId property. */
    private java.lang.String corporaitonId;

    /** The value of the simple beneficiaryType property. */
    private java.lang.String beneficiaryType;

    /** The value of the simple transId property. */
    private java.lang.String transId;

    /** The value of the simple frequnceType property. */
    private java.lang.String frequnceType;

    /** The value of the simple frequnceDays property. */
    private java.lang.String frequnceDays;

    /** The value of the simple status property. */
    private java.lang.String status;

    /** The value of the simple authStatus property. */
    private java.lang.String authStatus;

    /** The value of the simple operation property. */
    private java.lang.String operation;

    /** The value of the simple scheduleName property. */
    private java.lang.String scheduleName;

    //add by long_zg 2014-05-06 for CR188
    /** The value of the simple lastUpdateTime property. */
    private java.util.Date lastUpdateTime;
    
    private java.lang.String endDate;   //add by heyj 20190528
    
    /**
     * Simple constructor of AbstractScheduleTransferHis instances.
     */
    public AbstractScheduleTransferHis()
    {
    }

    /**
     * Constructor of AbstractScheduleTransferHis instances given a simple primary key.
     * @param seqNo
     */
    public AbstractScheduleTransferHis(java.lang.String seqNo)
    {
        this.setSeqNo(seqNo);
    }
    

    public java.lang.String getEndDate() {
		return endDate;
	}

	public void setEndDate(java.lang.String endDate) {
		this.endDate = endDate;
	}

	/**
     * Return the simple primary key value that identifies this object.
     * @return java.lang.String
     */
    public java.lang.String getSeqNo()
    {
        return seqNo;
    }

    /**
     * Set the simple primary key value that identifies this object.
     * @param seqNo
     */
    public void setSeqNo(java.lang.String seqNo)
    {
        this.hashValue = 0;
        this.seqNo = seqNo;
    }

    /**
     * Return the value of the SCHEDULE_ID column.
     * @return java.lang.String
     */
    public java.lang.String getScheduleId()
    {
        return this.scheduleId;
    }

    /**
     * Set the value of the SCHEDULE_ID column.
     * @param scheduleId
     */
    public void setScheduleId(java.lang.String scheduleId)
    {
        this.scheduleId = scheduleId;
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
     * Return the value of the CORPORAITON_ID column.
     * @return java.lang.String
     */
    public java.lang.String getCorporaitonId()
    {
        return this.corporaitonId;
    }

    /**
     * Set the value of the CORPORAITON_ID column.
     * @param corporaitonId
     */
    public void setCorporaitonId(java.lang.String corporaitonId)
    {
        this.corporaitonId = corporaitonId;
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
     * Return the value of the TRANS_ID column.
     * @return java.lang.String
     */
    public java.lang.String getTransId()
    {
        return this.transId;
    }

    /**
     * Set the value of the TRANS_ID column.
     * @param transId
     */
    public void setTransId(java.lang.String transId)
    {
        this.transId = transId;
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
     * Return the value of the SCHEDULE_NAME column.
     * @return java.lang.String
     */
    public java.lang.String getScheduleName()
    {
        return this.scheduleName;
    }

    /**
     * Set the value of the SCHEDULE_NAME column.
     * @param scheduleName
     */
    public void setScheduleName(java.lang.String scheduleName)
    {
        this.scheduleName = scheduleName;
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
        if (! (rhs instanceof ScheduleTransferHis))
            return false;
        ScheduleTransferHis that = (ScheduleTransferHis) rhs;
        if (this.getSeqNo() == null || that.getSeqNo() == null)
            return false;
        return (this.getSeqNo().equals(that.getSeqNo()));
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
            int seqNoValue = this.getSeqNo() == null ? 0 : this.getSeqNo().hashCode();
            result = result * 37 + seqNoValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }

	public java.util.Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(java.util.Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}