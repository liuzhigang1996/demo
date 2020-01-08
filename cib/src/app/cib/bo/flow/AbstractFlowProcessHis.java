/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Tue Jan 02 12:37:21 CST 2007 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.flow;

import java.io.Serializable;

/**
 * A class that represents a row in the FLOW_PROCESS_HIS table. 
 * You can customize the behavior of this class by editing the class, {@link FlowProcessHis()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractFlowProcessHis 
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.String procId;

    /** The value of the flowWorkHisSet one-to-many association. */
    private java.util.Set flowWorkHisSet;

    /** The value of the simple txnType property. */
    private java.lang.String txnType;

    /** The value of the simple txnCategory property. */
    private java.lang.String txnCategory;

    /** The value of the simple ruleFlag property. */
    private java.lang.String ruleFlag;

    /** The value of the simple txnBean property. */
    private java.lang.String txnBean;

    /** The value of the simple transNo property. */
    private java.lang.String transNo;

    /** The value of the simple transDesc property. */
    private java.lang.String transDesc;

    /** The value of the simple corpId property. */
    private java.lang.String corpId;

    /** The value of the simple currency property. */
    private java.lang.String currency;

    /** The value of the simple amount property. */
    private java.lang.Double amount;

    /** The value of the simple toCurrency property. */
    private java.lang.String toCurrency;

    /** The value of the simple toAmount property. */
    private java.lang.Double toAmount;

    /** The value of the simple approveType property. */
    private java.lang.String approveType;

    /** The value of the simple approveRule property. */
    private java.lang.String approveRule;

    /** The value of the simple approveStatus property. */
    private java.lang.String approveStatus;

    /** The value of the simple approvers property. */
    private java.lang.String approvers;

    /** The value of the simple allowExecutor property. */
    private java.lang.String allowExecutor;

    /** The value of the simple procCreator property. */
    private java.lang.String procCreator;

    /** The value of the simple procCreatorName property. */
    private java.lang.String procCreatorName;

    /** The value of the simple procCreateTime property. */
    private java.util.Date procCreateTime;

    /** The value of the simple procFinishTime property. */
    private java.util.Date procFinishTime;

    /** The value of the simple latestDealer property. */
    private java.lang.String latestDealer;

    /** The value of the simple latestDealerName property. */
    private java.lang.String latestDealerName;

    /** The value of the simple latestDealTime property. */
    private java.util.Date latestDealTime;

    /** The value of the simple procStatus property. */
    private java.lang.String procStatus;

    /**
     * Simple constructor of AbstractFlowProcessHis instances.
     */
    public AbstractFlowProcessHis()
    {
    }

    /**
     * Constructor of AbstractFlowProcessHis instances given a simple primary key.
     * @param procId
     */
    public AbstractFlowProcessHis(java.lang.String procId)
    {
        this.setProcId(procId);
    }

    /**
     * Return the simple primary key value that identifies this object.
     * @return java.lang.String
     */
    public java.lang.String getProcId()
    {
        return procId;
    }

    /**
     * Set the simple primary key value that identifies this object.
     * @param procId
     */
    public void setProcId(java.lang.String procId)
    {
        this.hashValue = 0;
        this.procId = procId;
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
     * Return the value of the TXN_CATEGORY column.
     * @return java.lang.String
     */
    public java.lang.String getTxnCategory()
    {
        return this.txnCategory;
    }

    /**
     * Set the value of the TXN_CATEGORY column.
     * @param txnCategory
     */
    public void setTxnCategory(java.lang.String txnCategory)
    {
        this.txnCategory = txnCategory;
    }

    /**
     * Return the value of the RULE_FLAG column.
     * @return java.lang.String
     */
    public java.lang.String getRuleFlag()
    {
        return this.ruleFlag;
    }

    /**
     * Set the value of the RULE_FLAG column.
     * @param ruleFlag
     */
    public void setRuleFlag(java.lang.String ruleFlag)
    {
        this.ruleFlag = ruleFlag;
    }

    /**
     * Return the value of the TXN_BEAN column.
     * @return java.lang.String
     */
    public java.lang.String getTxnBean()
    {
        return this.txnBean;
    }

    /**
     * Set the value of the TXN_BEAN column.
     * @param txnBean
     */
    public void setTxnBean(java.lang.String txnBean)
    {
        this.txnBean = txnBean;
    }

    /**
     * Return the value of the TRANS_NO column.
     * @return java.lang.String
     */
    public java.lang.String getTransNo()
    {
        return this.transNo;
    }

    /**
     * Set the value of the TRANS_NO column.
     * @param transNo
     */
    public void setTransNo(java.lang.String transNo)
    {
        this.transNo = transNo;
    }

    /**
     * Return the value of the TRANS_DESC column.
     * @return java.lang.String
     */
    public java.lang.String getTransDesc()
    {
        return this.transDesc;
    }

    /**
     * Set the value of the TRANS_DESC column.
     * @param transDesc
     */
    public void setTransDesc(java.lang.String transDesc)
    {
        this.transDesc = transDesc;
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
     * Return the value of the AMOUNT column.
     * @return java.lang.Double
     */
    public java.lang.Double getAmount()
    {
        return this.amount;
    }

    /**
     * Set the value of the AMOUNT column.
     * @param amount
     */
    public void setAmount(java.lang.Double amount)
    {
        this.amount = amount;
    }

    /**
     * Return the value of the TO_CURRENCY column.
     * @return java.lang.String
     */
    public java.lang.String getToCurrency()
    {
        return this.toCurrency;
    }

    /**
     * Set the value of the TO_CURRENCY column.
     * @param toCurrency
     */
    public void setToCurrency(java.lang.String toCurrency)
    {
        this.toCurrency = toCurrency;
    }

    /**
     * Return the value of the TO_AMOUNT column.
     * @return java.lang.Double
     */
    public java.lang.Double getToAmount()
    {
        return this.toAmount;
    }

    /**
     * Set the value of the TO_AMOUNT column.
     * @param toAmount
     */
    public void setToAmount(java.lang.Double toAmount)
    {
        this.toAmount = toAmount;
    }

    /**
     * Return the value of the APPROVE_TYPE column.
     * @return java.lang.String
     */
    public java.lang.String getApproveType()
    {
        return this.approveType;
    }

    /**
     * Set the value of the APPROVE_TYPE column.
     * @param approveType
     */
    public void setApproveType(java.lang.String approveType)
    {
        this.approveType = approveType;
    }

    /**
     * Return the value of the APPROVE_RULE column.
     * @return java.lang.String
     */
    public java.lang.String getApproveRule()
    {
        return this.approveRule;
    }

    /**
     * Set the value of the APPROVE_RULE column.
     * @param approveRule
     */
    public void setApproveRule(java.lang.String approveRule)
    {
        this.approveRule = approveRule;
    }

    /**
     * Return the value of the APPROVE_STATUS column.
     * @return java.lang.String
     */
    public java.lang.String getApproveStatus()
    {
        return this.approveStatus;
    }

    /**
     * Set the value of the APPROVE_STATUS column.
     * @param approveStatus
     */
    public void setApproveStatus(java.lang.String approveStatus)
    {
        this.approveStatus = approveStatus;
    }

    /**
     * Return the value of the APPROVERS column.
     * @return java.lang.String
     */
    public java.lang.String getApprovers()
    {
        return this.approvers;
    }

    /**
     * Set the value of the APPROVERS column.
     * @param approvers
     */
    public void setApprovers(java.lang.String approvers)
    {
        this.approvers = approvers;
    }

    /**
     * Return the value of the ALLOW_EXECUTOR column.
     * @return java.lang.String
     */
    public java.lang.String getAllowExecutor()
    {
        return this.allowExecutor;
    }

    /**
     * Set the value of the ALLOW_EXECUTOR column.
     * @param allowExecutor
     */
    public void setAllowExecutor(java.lang.String allowExecutor)
    {
        this.allowExecutor = allowExecutor;
    }

    /**
     * Return the value of the PROC_CREATOR column.
     * @return java.lang.String
     */
    public java.lang.String getProcCreator()
    {
        return this.procCreator;
    }

    /**
     * Set the value of the PROC_CREATOR column.
     * @param procCreator
     */
    public void setProcCreator(java.lang.String procCreator)
    {
        this.procCreator = procCreator;
    }

    /**
     * Return the value of the PROC_CREATOR_NAME column.
     * @return java.lang.String
     */
    public java.lang.String getProcCreatorName()
    {
        return this.procCreatorName;
    }

    /**
     * Set the value of the PROC_CREATOR_NAME column.
     * @param procCreatorName
     */
    public void setProcCreatorName(java.lang.String procCreatorName)
    {
        this.procCreatorName = procCreatorName;
    }

    /**
     * Return the value of the PROC_CREATE_TIME column.
     * @return java.util.Date
     */
    public java.util.Date getProcCreateTime()
    {
        return this.procCreateTime;
    }

    /**
     * Set the value of the PROC_CREATE_TIME column.
     * @param procCreateTime
     */
    public void setProcCreateTime(java.util.Date procCreateTime)
    {
        this.procCreateTime = procCreateTime;
    }

    /**
     * Return the value of the PROC_FINISH_TIME column.
     * @return java.util.Date
     */
    public java.util.Date getProcFinishTime()
    {
        return this.procFinishTime;
    }

    /**
     * Set the value of the PROC_FINISH_TIME column.
     * @param procFinishTime
     */
    public void setProcFinishTime(java.util.Date procFinishTime)
    {
        this.procFinishTime = procFinishTime;
    }

    /**
     * Return the value of the LATEST_DEALER column.
     * @return java.lang.String
     */
    public java.lang.String getLatestDealer()
    {
        return this.latestDealer;
    }

    /**
     * Set the value of the LATEST_DEALER column.
     * @param latestDealer
     */
    public void setLatestDealer(java.lang.String latestDealer)
    {
        this.latestDealer = latestDealer;
    }

    /**
     * Return the value of the LATEST_DEALER_NAME column.
     * @return java.lang.String
     */
    public java.lang.String getLatestDealerName()
    {
        return this.latestDealerName;
    }

    /**
     * Set the value of the LATEST_DEALER_NAME column.
     * @param latestDealerName
     */
    public void setLatestDealerName(java.lang.String latestDealerName)
    {
        this.latestDealerName = latestDealerName;
    }

    /**
     * Return the value of the LATEST_DEAL_TIME column.
     * @return java.util.Date
     */
    public java.util.Date getLatestDealTime()
    {
        return this.latestDealTime;
    }

    /**
     * Set the value of the LATEST_DEAL_TIME column.
     * @param latestDealTime
     */
    public void setLatestDealTime(java.util.Date latestDealTime)
    {
        this.latestDealTime = latestDealTime;
    }

    /**
     * Return the value of the PROC_STATUS column.
     * @return java.lang.String
     */
    public java.lang.String getProcStatus()
    {
        return this.procStatus;
    }

    /**
     * Set the value of the PROC_STATUS column.
     * @param procStatus
     */
    public void setProcStatus(java.lang.String procStatus)
    {
        this.procStatus = procStatus;
    }

    /**
     * Return the value of the PROC_ID collection.
     * @return FlowWorkHis
     */
    public java.util.Set getFlowWorkHisSet()
    {
        return this.flowWorkHisSet;
    }

    /**
     * Set the value of the PROC_ID collection.
     * @param flowWorkHisSet
     */
    public void setFlowWorkHisSet(java.util.Set flowWorkHisSet)
    {
        this.flowWorkHisSet = flowWorkHisSet;
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
        if (! (rhs instanceof FlowProcessHis))
            return false;
        FlowProcessHis that = (FlowProcessHis) rhs;
        if (this.getProcId() == null || that.getProcId() == null)
            return false;
        return (this.getProcId().equals(that.getProcId()));
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
            int procIdValue = this.getProcId() == null ? 0 : this.getProcId().hashCode();
            result = result * 37 + procIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }
}
