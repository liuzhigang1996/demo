/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Fri Dec 29 23:27:04 CST 2006 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.srv;

import java.io.Serializable;

/**
 * A class that represents a row in the REQ_CHEQUE_PROTECTION table. 
 * You can customize the behavior of this class by editing the class, {@link ReqChequeProtection()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractReqChequeProtection 
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.String transId;

    /** The value of the simple batchId property. */
    private java.lang.String batchId;

    /** The value of the simple userId property. */
    private java.lang.String userId;

    /** The value of the simple corpId property. */
    private java.lang.String corpId;

    /** The value of the simple chequeNumber property. */
    private java.lang.String chequeNumber;

    /** The value of the simple chequeStyle property. */
    private java.lang.String chequeStyle;

    /** The value of the simple account property. */
    private java.lang.String account;

    /** The value of the simple amount property. */
    private java.lang.Double amount;

    /** The value of the simple beneficiaryName property. */
    private java.lang.String beneficiaryName;

    /** The value of the simple issueDate property. */
    private java.util.Date issueDate;

    /** The value of the simple remark property. */
    private java.lang.String remark;

    /** The value of the simple status property. */
    private java.lang.String status;

    /** The value of the simple requestTime property. */
    private java.util.Date requestTime;

    /** The value of the simple executeTime property. */
    private java.util.Date executeTime;

    /** The value of the simple problemType property. */
    private java.lang.String problemType;

    /** The value of the simple currency property. */
    private java.lang.String currency;

    /** The value of the simple lineNo property. */
    private java.lang.Integer lineNo;

    /** The value of the simple filler property. */
    private java.lang.String filler;

    /** The value of the simple detailResult property. */
    private java.lang.String detailResult;

    /**
     * Simple constructor of AbstractReqChequeProtection instances.
     */
    public AbstractReqChequeProtection()
    {
    }

    /**
     * Constructor of AbstractReqChequeProtection instances given a simple primary key.
     * @param transId
     */
    public AbstractReqChequeProtection(java.lang.String transId)
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
     * Return the value of the BATCH_ID column.
     * @return java.lang.String
     */
    public java.lang.String getBatchId()
    {
        return this.batchId;
    }

    /**
     * Set the value of the BATCH_ID column.
     * @param batchId
     */
    public void setBatchId(java.lang.String batchId)
    {
        this.batchId = batchId;
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
     * Return the value of the CHEQUE_NUMBER column.
     * @return java.lang.String
     */
    public java.lang.String getChequeNumber()
    {
        return this.chequeNumber;
    }

    /**
     * Set the value of the CHEQUE_NUMBER column.
     * @param chequeNumber
     */
    public void setChequeNumber(java.lang.String chequeNumber)
    {
        this.chequeNumber = chequeNumber;
    }

    /**
     * Return the value of the CHEQUE_STYLE column.
     * @return java.lang.String
     */
    public java.lang.String getChequeStyle()
    {
        return this.chequeStyle;
    }

    /**
     * Set the value of the CHEQUE_STYLE column.
     * @param chequeStyle
     */
    public void setChequeStyle(java.lang.String chequeStyle)
    {
        this.chequeStyle = chequeStyle;
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
     * Return the value of the BENEFICIARY_NAME column.
     * @return java.lang.String
     */
    public java.lang.String getBeneficiaryName()
    {
        return this.beneficiaryName;
    }

    /**
     * Set the value of the BENEFICIARY_NAME column.
     * @param beneficiaryName
     */
    public void setBeneficiaryName(java.lang.String beneficiaryName)
    {
        this.beneficiaryName = beneficiaryName;
    }

    /**
     * Return the value of the ISSUE_DATE column.
     * @return java.util.Date
     */
    public java.util.Date getIssueDate()
    {
        return this.issueDate;
    }

    /**
     * Set the value of the ISSUE_DATE column.
     * @param issueDate
     */
    public void setIssueDate(java.util.Date issueDate)
    {
        this.issueDate = issueDate;
    }

    /**
     * Return the value of the REMARK column.
     * @return java.lang.String
     */
    public java.lang.String getRemark()
    {
        return this.remark;
    }

    /**
     * Set the value of the REMARK column.
     * @param remark
     */
    public void setRemark(java.lang.String remark)
    {
        this.remark = remark;
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
     * Return the value of the REQUEST_TIME column.
     * @return java.util.Date
     */
    public java.util.Date getRequestTime()
    {
        return this.requestTime;
    }

    /**
     * Set the value of the REQUEST_TIME column.
     * @param requestTime
     */
    public void setRequestTime(java.util.Date requestTime)
    {
        this.requestTime = requestTime;
    }

    /**
     * Return the value of the EXECUTE_TIME column.
     * @return java.util.Date
     */
    public java.util.Date getExecuteTime()
    {
        return this.executeTime;
    }

    /**
     * Set the value of the EXECUTE_TIME column.
     * @param executeTime
     */
    public void setExecuteTime(java.util.Date executeTime)
    {
        this.executeTime = executeTime;
    }

    /**
     * Return the value of the PROBLEM_TYPE column.
     * @return java.lang.String
     */
    public java.lang.String getProblemType()
    {
        return this.problemType;
    }

    /**
     * Set the value of the PROBLEM_TYPE column.
     * @param problemType
     */
    public void setProblemType(java.lang.String problemType)
    {
        this.problemType = problemType;
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
     * Return the value of the LINE_NO column.
     * @return java.lang.Integer
     */
    public java.lang.Integer getLineNo()
    {
        return this.lineNo;
    }

    /**
     * Set the value of the LINE_NO column.
     * @param lineNo
     */
    public void setLineNo(java.lang.Integer lineNo)
    {
        this.lineNo = lineNo;
    }

    /**
     * Return the value of the FILLER column.
     * @return java.lang.String
     */
    public java.lang.String getFiller()
    {
        return this.filler;
    }

    /**
     * Set the value of the FILLER column.
     * @param filler
     */
    public void setFiller(java.lang.String filler)
    {
        this.filler = filler;
    }

    /**
     * Return the value of the DETAIL_RESULT column.
     * @return java.lang.String
     */
    public java.lang.String getDetailResult()
    {
        return this.detailResult;
    }

    /**
     * Set the value of the DETAIL_RESULT column.
     * @param detailResult
     */
    public void setDetailResult(java.lang.String detailResult)
    {
        this.detailResult = detailResult;
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
        if (! (rhs instanceof ReqChequeProtection))
            return false;
        ReqChequeProtection that = (ReqChequeProtection) rhs;
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
