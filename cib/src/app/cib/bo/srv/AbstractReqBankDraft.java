/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Fri Dec 29 23:26:41 CST 2006 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.srv;

import java.io.Serializable;

/**
 * A class that represents a row in the REQ_BANK_DRAFT table. 
 * You can customize the behavior of this class by editing the class, {@link ReqBankDraft()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractReqBankDraft 
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.String transId;

    /** The value of the simple batchId property. */
    private java.lang.String batchId;

    /** The value of the simple draftAmount property. */
    private java.lang.Double draftAmount;

    /** The value of the simple beneficiaryName property. */
    private java.lang.String beneficiaryName;

    /** The value of the simple beneficiaryName2 property. */
    private java.lang.String beneficiaryName2;

    /** The value of the simple beneficiaryName3 property. */
    private java.lang.String beneficiaryName3;

    /** The value of the simple printNameIndicator property. */
    private java.lang.String printNameIndicator;

    /** The value of the simple bankAddress1 property. */
    private java.lang.String bankAddress1;

    /** The value of the simple bankAddress2 property. */
    private java.lang.String bankAddress2;

    /** The value of the simple chargeAccount property. */
    private java.lang.String chargeAccount;

    /** The value of the simple senderReference property. */
    private java.lang.String senderReference;

    /** The value of the simple filler property. */
    private java.lang.String filler;

    /** The value of the simple remark property. */
    private java.lang.String remark;

    /** The value of the simple status property. */
    private java.lang.String status;

    /** The value of the simple corpId property. */
    private java.lang.String corpId;

    /** The value of the simple userId property. */
    private java.lang.String userId;

    /** The value of the simple requestTime property. */
    private java.util.Date requestTime;

    /** The value of the simple executeTime property. */
    private java.util.Date executeTime;

    /** The value of the simple problemType property. */
    private java.lang.String problemType;

    /** The value of the simple chargeAmount property. */
    private java.lang.Double chargeAmount;

    /** The value of the simple bankReference property. */
    private java.lang.String bankReference;

    /** The value of the simple bankDraftNumber property. */
    private java.lang.String bankDraftNumber;

    /** The value of the simple detailResult property. */
    private java.lang.String detailResult;

    /** The value of the simple lineNo property. */
    private java.lang.Integer lineNo;
   	
	//add by lw 20100904
	private java.lang.String purposeCode;
	private java.lang.String purpose;
	private java.lang.String otherPurpose;
	private java.lang.String proofOfPurpose;

	public java.lang.String getPurposeCode() {
		return purposeCode;
	}

	public void setPurposeCode(java.lang.String purposeCode) {
		this.purposeCode = purposeCode;
	}
	
	public java.lang.String getPurpose() {
		return purpose;
	}

	public void setPurpose(java.lang.String purpose) {
		this.purpose = purpose;
	}
	
	public java.lang.String getOtherPurpose() {
		return otherPurpose;
	}

	public void setOtherPurpose(java.lang.String otherPurpose) {
		this.otherPurpose = otherPurpose;
	}

	public java.lang.String getProofOfPurpose() {
		return proofOfPurpose;
	}

	public void setProofOfPurpose(java.lang.String proofOfPurpose) {
		this.proofOfPurpose = proofOfPurpose;
	}
	//add by lw end
	
	/**
     * Simple constructor of AbstractReqBankDraft instances.
     */
    public AbstractReqBankDraft()
    {
    }

    /**
     * Constructor of AbstractReqBankDraft instances given a simple primary key.
     * @param transId
     */
    public AbstractReqBankDraft(java.lang.String transId)
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
     * Return the value of the DRAFT_AMOUNT column.
     * @return java.lang.Double
     */
    public java.lang.Double getDraftAmount()
    {
        return this.draftAmount;
    }

    /**
     * Set the value of the DRAFT_AMOUNT column.
     * @param draftAmount
     */
    public void setDraftAmount(java.lang.Double draftAmount)
    {
        this.draftAmount = draftAmount;
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
     * Return the value of the BENEFICIARY_NAME2 column.
     * @return java.lang.String
     */
    public java.lang.String getBeneficiaryName2()
    {
        return this.beneficiaryName2;
    }

    /**
     * Set the value of the BENEFICIARY_NAME2 column.
     * @param beneficiaryName2
     */
    public void setBeneficiaryName2(java.lang.String beneficiaryName2)
    {
        this.beneficiaryName2 = beneficiaryName2;
    }

    /**
     * Return the value of the BENEFICIARY_NAME2 column.
     * @return java.lang.String
     */
    public java.lang.String getBeneficiaryName3()
    {
        return this.beneficiaryName3;
    }

    /**
     * Set the value of the BENEFICIARY_NAME3 column.
     * @param beneficiaryName3
     */
    public void setBeneficiaryName3(java.lang.String beneficiaryName3)
    {
        this.beneficiaryName3 = beneficiaryName3;
    }

    /**
     * Return the value of the PRINT_NAME_INDICATOR column.
     * @return java.lang.String
     */
    public java.lang.String getPrintNameIndicator()
    {
        return this.printNameIndicator;
    }

    /**
     * Set the value of the PRINT_NAME_INDICATOR column.
     * @param printNameIndicator
     */
    public void setPrintNameIndicator(java.lang.String printNameIndicator)
    {
        this.printNameIndicator = printNameIndicator;
    }

    /**
     * Return the value of the BANK_ADDRESS_1 column.
     * @return java.lang.String
     */
    public java.lang.String getBankAddress1()
    {
        return this.bankAddress1;
    }

    /**
     * Set the value of the BANK_ADDRESS_1 column.
     * @param bankAddress1
     */
    public void setBankAddress1(java.lang.String bankAddress1)
    {
        this.bankAddress1 = bankAddress1;
    }

    /**
     * Return the value of the BANK_ADDRESS_2 column.
     * @return java.lang.String
     */
    public java.lang.String getBankAddress2()
    {
        return this.bankAddress2;
    }

    /**
     * Set the value of the BANK_ADDRESS_2 column.
     * @param bankAddress2
     */
    public void setBankAddress2(java.lang.String bankAddress2)
    {
        this.bankAddress2 = bankAddress2;
    }

    /**
     * Return the value of the CHARGE_ACCOUNT column.
     * @return java.lang.String
     */
    public java.lang.String getChargeAccount()
    {
        return this.chargeAccount;
    }

    /**
     * Set the value of the CHARGE_ACCOUNT column.
     * @param chargeAccount
     */
    public void setChargeAccount(java.lang.String chargeAccount)
    {
        this.chargeAccount = chargeAccount;
    }

    /**
     * Return the value of the SENDER_REFERENCE column.
     * @return java.lang.String
     */
    public java.lang.String getSenderReference()
    {
        return this.senderReference;
    }

    /**
     * Set the value of the SENDER_REFERENCE column.
     * @param senderReference
     */
    public void setSenderReference(java.lang.String senderReference)
    {
        this.senderReference = senderReference;
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
     * Return the value of the CHARGE_AMOUNT column.
     * @return java.lang.Double
     */
    public java.lang.Double getChargeAmount()
    {
        return this.chargeAmount;
    }

    /**
     * Set the value of the CHARGE_AMOUNT column.
     * @param chargeAmount
     */
    public void setChargeAmount(java.lang.Double chargeAmount)
    {
        this.chargeAmount = chargeAmount;
    }

    /**
     * Return the value of the BANK_REFERENCE column.
     * @return java.lang.String
     */
    public java.lang.String getBankReference()
    {
        return this.bankReference;
    }

    /**
     * Set the value of the BANK_REFERENCE column.
     * @param bankReference
     */
    public void setBankReference(java.lang.String bankReference)
    {
        this.bankReference = bankReference;
    }

    /**
     * Return the value of the BANK_DRAFT_NUMBER column.
     * @return java.lang.String
     */
    public java.lang.String getBankDraftNumber()
    {
        return this.bankDraftNumber;
    }

    /**
     * Set the value of the BANK_DRAFT_NUMBER column.
     * @param bankDraftNumber
     */
    public void setBankDraftNumber(java.lang.String bankDraftNumber)
    {
        this.bankDraftNumber = bankDraftNumber;
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
     * Implementation of the equals comparison on the basis of equality of the primary key values.
     * @param rhs
     * @return boolean
     */
    public boolean equals(Object rhs)
    {
        if (rhs == null)
            return false;
        if (! (rhs instanceof ReqBankDraft))
            return false;
        ReqBankDraft that = (ReqBankDraft) rhs;
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
