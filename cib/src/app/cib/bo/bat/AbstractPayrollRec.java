/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Fri Dec 29 23:22:10 CST 2006 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.bat;

import java.io.Serializable;

/**
 * A class that represents a row in the PAYROLL_REC table. 
 * You can customize the behavior of this class by editing the class, {@link PayrollRec()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractPayrollRec 
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.String transId;

    /** The value of the simple payrollId property. */
    private java.lang.String payrollId;

    /** The value of the simple referenceNo property. */
    private java.lang.String referenceNo;

    /** The value of the simple corpId property. */
    private java.lang.String corpId;

    /** The value of the simple fromAccount property. */
    private java.lang.String fromAccount;

    /** The value of the simple toAccount property. */
    private java.lang.String toAccount;

    /** The value of the simple creditAmount property. */
    private java.lang.Double creditAmount;

    /** The value of the simple remark property. */
    private java.lang.String remark;

    /** The value of the simple filler property. */
    private java.lang.String filler;

    /** The value of the simple problemType property. */
    private java.lang.String problemType;

    /** The value of the simple lineNo property. */
    private java.lang.Integer lineNo;

    /** The value of the simple status property. */
    private java.lang.String status;

    /** The value of the simple executeTime property. */
    private java.util.Date executeTime;

    /** The value of the simple detailResult property. */
    private java.lang.String detailResult;

    /**
     * Simple constructor of AbstractPayrollRec instances.
     */
    public AbstractPayrollRec()
    {
    }

    /**
     * Constructor of AbstractPayrollRec instances given a simple primary key.
     * @param transId
     */
    public AbstractPayrollRec(java.lang.String transId)
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
     * Return the value of the PAYROLL_ID column.
     * @return java.lang.String
     */
    public java.lang.String getPayrollId()
    {
        return this.payrollId;
    }

    /**
     * Set the value of the PAYROLL_ID column.
     * @param payrollId
     */
    public void setPayrollId(java.lang.String payrollId)
    {
        this.payrollId = payrollId;
    }

    /**
     * Return the value of the REFERENCE_NO column.
     * @return java.lang.String
     */
    public java.lang.String getReferenceNo()
    {
        return this.referenceNo;
    }

    /**
     * Set the value of the REFERENCE_NO column.
     * @param referenceNo
     */
    public void setReferenceNo(java.lang.String referenceNo)
    {
        this.referenceNo = referenceNo;
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
     * Return the value of the FROM_ACCOUNT column.
     * @return java.lang.String
     */
    public java.lang.String getFromAccount()
    {
        return this.fromAccount;
    }

    /**
     * Set the value of the FROM_ACCOUNT column.
     * @param fromAccount
     */
    public void setFromAccount(java.lang.String fromAccount)
    {
        this.fromAccount = fromAccount;
    }

    /**
     * Return the value of the TO_ACCOUNT column.
     * @return java.lang.String
     */
    public java.lang.String getToAccount()
    {
        return this.toAccount;
    }

    /**
     * Set the value of the TO_ACCOUNT column.
     * @param toAccount
     */
    public void setToAccount(java.lang.String toAccount)
    {
        this.toAccount = toAccount;
    }

    /**
     * Return the value of the CREDIT_AMOUNT column.
     * @return java.lang.Double
     */
    public java.lang.Double getCreditAmount()
    {
        return this.creditAmount;
    }

    /**
     * Set the value of the CREDIT_AMOUNT column.
     * @param creditAmount
     */
    public void setCreditAmount(java.lang.Double creditAmount)
    {
        this.creditAmount = creditAmount;
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
        if (! (rhs instanceof PayrollRec))
            return false;
        PayrollRec that = (PayrollRec) rhs;
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
