/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Mon Oct 30 14:50:53 CST 2006 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.bat;

import java.io.Serializable;

/**
 * A class that represents a row in the SCHEDULE_TRANSFER_BATCH table. 
 * You can customize the behavior of this class by editing the class, {@link ScheduleTransferBatch()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractScheduleTransferBatch 
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.String batchId;

    /** The value of the simple scheduleId property. */
    private java.lang.String scheduleId;

    /** The value of the simple scheduleName property. */
    private java.lang.String scheduleName;

    /** The value of the simple scheduleDate property. */
    private java.lang.String scheduleDate;

    /** The value of the simple scheduleTime property. */
    private java.lang.String scheduleTime;

    /** The value of the simple corporaitonId property. */
    private java.lang.String corporaitonId;

    /** The value of the simple userId property. */
    private java.lang.String userId;

    /** The value of the simple beneficiaryType property. */
    private java.lang.String beneficiaryType;

    /** The value of the simple frequnceType property. */
    private java.lang.String frequnceType;

    /** The value of the simple frequnceDays property. */
    private java.lang.String frequnceDays;

    /** The value of the simple transId property. */
    private java.lang.String transId;

    /** The value of the simple status property. */
    private java.lang.String status;

    /** The value of the simple headerResult property. */
    private java.lang.String headerResult;

    /**
     * Simple constructor of AbstractScheduleTransferBatch instances.
     */
    public AbstractScheduleTransferBatch()
    {
    }

    /**
     * Constructor of AbstractScheduleTransferBatch instances given a simple primary key.
     * @param batchId
     */
    public AbstractScheduleTransferBatch(java.lang.String batchId)
    {
        this.setBatchId(batchId);
    }

    /**
     * Return the simple primary key value that identifies this object.
     * @return java.lang.String
     */
    public java.lang.String getBatchId()
    {
        return batchId;
    }

    /**
     * Set the simple primary key value that identifies this object.
     * @param batchId
     */
    public void setBatchId(java.lang.String batchId)
    {
        this.hashValue = 0;
        this.batchId = batchId;
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
     * Return the value of the SCHEDULE_DATE column.
     * @return java.lang.String
     */
    public java.lang.String getScheduleDate()
    {
        return this.scheduleDate;
    }

    /**
     * Set the value of the SCHEDULE_DATE column.
     * @param scheduleDate
     */
    public void setScheduleDate(java.lang.String scheduleDate)
    {
        this.scheduleDate = scheduleDate;
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
        if (! (rhs instanceof ScheduleTransferBatch))
            return false;
        ScheduleTransferBatch that = (ScheduleTransferBatch) rhs;
        if (this.getBatchId() == null || that.getBatchId() == null)
            return false;
        return (this.getBatchId().equals(that.getBatchId()));
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
            int batchIdValue = this.getBatchId() == null ? 0 : this.getBatchId().hashCode();
            result = result * 37 + batchIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }

	public java.lang.String getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(java.lang.String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public java.lang.String getHeaderResult() {
		return headerResult;
	}

	public void setHeaderResult(java.lang.String headerResult) {
		this.headerResult = headerResult;
	}
}
