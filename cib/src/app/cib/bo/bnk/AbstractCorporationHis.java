/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Thu Mar 01 15:48:59 CST 2007 by MyEclipse Hibernate Tool.
 */
package app.cib.bo.bnk;

import java.io.Serializable;

/**
 * A class that represents a row in the CORPORATION_HIS table. 
 * You can customize the behavior of this class by editing the class, {@link CorporationHis()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractCorporationHis 
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.String seqNo;

    /** The value of the simple afterModifyId property. */
    private java.lang.String afterModifyId;

    /** The value of the simple prefId property. */
    private java.lang.String prefId;

    /** The value of the simple corpId property. */
    private java.lang.String corpId;

    /** The value of the simple corpType property. */
    private java.lang.String corpType;

    /** The value of the simple allowFinancialController property. */
    private java.lang.String allowFinancialController;

    /** The value of the simple authenticationMode property. */
    private java.lang.String authenticationMode;
    
  //add by long_zg 2019-05-16 for otp login
    /** The value of the simple otpLogin property. */
    private java.lang.String otpLogin;
    
    /** The value of the simple parentCorp property. */
    private java.lang.String parentCorp;

    /** The value of the simple corpName property. */
    private java.lang.String corpName;

    /** The value of the simple address property. */
    private java.lang.String address;

    /** The value of the simple allowTd property. */
    private java.lang.String allowTd;

    /** The value of the simple allowExecutor property. */
    private java.lang.String allowExecutor;

    /** The value of the simple operation property. */
    private java.lang.String operation;

    /** The value of the simple operationDesc property. */
    private java.lang.String operationDesc;

    /** The value of the simple status property. */
    private java.lang.String status;

    /** The value of the simple authStatus property. */
    private java.lang.String authStatus;

    /** The value of the simple version property. */
    private java.lang.Integer version;

    /** The value of the simple allowApproverSelection property. */
    private java.lang.String allowApproverSelection;

    /** The value of the simple allowDisplayBottom property. */
    private java.lang.String allowDisplayBottom;

    /** The value of the simple allowTaxPayment property. */
    private java.lang.String allowTaxPayment;

    /** The value of the simple authCurMop property. */
    private java.lang.String authCurMop;

    /** The value of the simple authCurHkd property. */
    private java.lang.String authCurHkd;

    /** The value of the simple authCurUsd property. */
    private java.lang.String authCurUsd;

    /** The value of the simple authCurEur property. */
    private java.lang.String authCurEur;

    /** The value of the simple lastUpdateTime property. */
    private java.util.Date lastUpdateTime;

    /** The value of the simple requester property. */
    private java.lang.String requester;

    /** The value of the simple fullName property. */
    private java.lang.String fullName;

    /** The value of the simple commercialRegistryNo property. */
    private java.lang.String commercialRegistryNo;

    /** The value of the simple registryCountry property. */
    private java.lang.String registryCountry;

    /** The value of the simple address1 property. */
    private java.lang.String address1;

    /** The value of the simple address2 property. */
    private java.lang.String address2;

    /** The value of the simple address3 property. */
    private java.lang.String address3;

    /** The value of the simple address4 property. */
    private java.lang.String address4;

    /** The value of the simple telephone property. */
    private java.lang.String telephone;

    /** The value of the simple faxNo property. */
    private java.lang.String faxNo;

    /** The value of the simple email property. */
    private java.lang.String email;

    /** The value of the simple rep1FullName property. */
    private java.lang.String rep1FullName;

    /** The value of the simple rep1ShortName property. */
    private java.lang.String rep1ShortName;

    /** The value of the simple rep1Title property. */
    private java.lang.String rep1Title;

    /** The value of the simple rep1IdType property. */
    private java.lang.String rep1IdType;

    /** The value of the simple rep1IdNo property. */
    private java.lang.String rep1IdNo;

    /** The value of the simple rep1IdIssueDate property. */
    private java.util.Date rep1IdIssueDate;

    /** The value of the simple rep1IdIssuer property. */
    private java.lang.String rep1IdIssuer;

    /** The value of the simple rep1Telephone property. */
    private java.lang.String rep1Telephone;

    /** The value of the simple rep1Mobile property. */
    private java.lang.String rep1Mobile;

    /** The value of the simple rep1Email property. */
    private java.lang.String rep1Email;

    /** The value of the simple rep1FaxNo property. */
    private java.lang.String rep1FaxNo;

    /** The value of the simple rep2FullName property. */
    private java.lang.String rep2FullName;

    /** The value of the simple rep2ShortName property. */
    private java.lang.String rep2ShortName;

    /** The value of the simple rep2Title property. */
    private java.lang.String rep2Title;

    /** The value of the simple rep2IdType property. */
    private java.lang.String rep2IdType;

    /** The value of the simple rep2IdNo property. */
    private java.lang.String rep2IdNo;

    /** The value of the simple rep2IdIssueDate property. */
    private java.util.Date rep2IdIssueDate;

    /** The value of the simple rep2IdIssuer property. */
    private java.lang.String rep2IdIssuer;

    /** The value of the simple rep2Telephone property. */
    private java.lang.String rep2Telephone;

    /** The value of the simple rep2Mobile property. */
    private java.lang.String rep2Mobile;

    /** The value of the simple rep2Email property. */
    private java.lang.String rep2Email;

    /** The value of the simple rep2FaxNo property. */
    private java.lang.String rep2FaxNo;

    /** The value of the simple foreignCity property. */
    private java.lang.String foreignCity;

    /** The value of the simple timeZone property. */
    private java.lang.String timeZone;
    
    private java.lang.String merchantGroup;
    
    private java.lang.String merchantGroupDes;
    private java.lang.String cifNo;//add by linrui 20180211

    public java.lang.String getCifNo() {
		return cifNo;
	}

	public void setCifNo(java.lang.String cifNo) {
		this.cifNo = cifNo;
	}

    public java.lang.String getMerchantGroup() {
		return merchantGroup;
	}

	public void setMerchantGroup(java.lang.String merchantGroup) {
		this.merchantGroup = merchantGroup;
	}

	public java.lang.String getMerchantGroupDes() {
		return merchantGroupDes;
	}

	public void setMerchantGroupDes(java.lang.String merchantGroupDes) {
		this.merchantGroupDes = merchantGroupDes;
	}

	/**
     * Simple constructor of AbstractCorporationHis instances.
     */
    public AbstractCorporationHis()
    {
    }

    /**
     * Constructor of AbstractCorporationHis instances given a simple primary key.
     * @param seqNo
     */
    public AbstractCorporationHis(java.lang.String seqNo)
    {
        this.setSeqNo(seqNo);
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
     * Return the value of the PARENT_CORP column.
     * @return java.lang.String
     */
    public java.lang.String getParentCorp()
    {
        return this.parentCorp;
    }

    /**
     * Set the value of the PARENT_CORP column.
     * @param parentCorp
     */
    public void setParentCorp(java.lang.String parentCorp)
    {
        this.parentCorp = parentCorp;
    }

    /**
     * Return the value of the CORP_NAME column.
     * @return java.lang.String
     */
    public java.lang.String getCorpName()
    {
        return this.corpName;
    }

    /**
     * Set the value of the CORP_NAME column.
     * @param corpName
     */
    public void setCorpName(java.lang.String corpName)
    {
        this.corpName = corpName;
    }

    /**
     * Return the value of the ADDRESS column.
     * @return java.lang.String
     */
    public java.lang.String getAddress()
    {
        return this.address;
    }

    /**
     * Set the value of the ADDRESS column.
     * @param address
     */
    public void setAddress(java.lang.String address)
    {
        this.address = address;
    }

    /**
     * Return the value of the ALLOW_TD column.
     * @return java.lang.String
     */
    public java.lang.String getAllowTd()
    {
        return this.allowTd;
    }

    /**
     * Set the value of the ALLOW_TD column.
     * @param allowTd
     */
    public void setAllowTd(java.lang.String allowTd)
    {
        this.allowTd = allowTd;
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
     * Return the value of the OPERATION_DESC column.
     * @return java.lang.String
     */
    public java.lang.String getOperationDesc()
    {
        return this.operationDesc;
    }

    /**
     * Set the value of the OPERATION_DESC column.
     * @param operationDesc
     */
    public void setOperationDesc(java.lang.String operationDesc)
    {
        this.operationDesc = operationDesc;
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
     * Return the value of the ALLOW_APPROVER_SELECTION column.
     * @return java.lang.String
     */
    public java.lang.String getAllowApproverSelection()
    {
        return this.allowApproverSelection;
    }

    /**
     * Set the value of the ALLOW_APPROVER_SELECTION column.
     * @param allowApproverSelection
     */
    public void setAllowApproverSelection(java.lang.String allowApproverSelection)
    {
        this.allowApproverSelection = allowApproverSelection;
    }

    /**
     * Return the value of the ALLOW_DISPLAY_BOTTOM column.
     * @return java.lang.String
     */
    public java.lang.String getAllowDisplayBottom()
    {
        return this.allowDisplayBottom;
    }

    /**
     * Set the value of the ALLOW_DISPLAY_BOTTOM column.
     * @param allowDisplayBottom
     */
    public void setAllowDisplayBottom(java.lang.String allowDisplayBottom)
    {
        this.allowDisplayBottom = allowDisplayBottom;
    }

    /**
     * Return the value of the ALLOW_TAX_PAYMENT column.
     * @return java.lang.String
     */
    public java.lang.String getAllowTaxPayment()
    {
        return this.allowTaxPayment;
    }

    /**
     * Set the value of the ALLOW_TAX_PAYMENT column.
     * @param allowTaxPayment
     */
    public void setAllowTaxPayment(java.lang.String allowTaxPayment)
    {
        this.allowTaxPayment = allowTaxPayment;
    }

    /**
     * Return the value of the AUTH_CUR_MOP column.
     * @return java.lang.String
     */
    public java.lang.String getAuthCurMop()
    {
        return this.authCurMop;
    }

    /**
     * Set the value of the AUTH_CUR_MOP column.
     * @param authCurMop
     */
    public void setAuthCurMop(java.lang.String authCurMop)
    {
        this.authCurMop = authCurMop;
    }

    /**
     * Return the value of the AUTH_CUR_HKD column.
     * @return java.lang.String
     */
    public java.lang.String getAuthCurHkd()
    {
        return this.authCurHkd;
    }

    /**
     * Set the value of the AUTH_CUR_HKD column.
     * @param authCurHkd
     */
    public void setAuthCurHkd(java.lang.String authCurHkd)
    {
        this.authCurHkd = authCurHkd;
    }

    /**
     * Return the value of the AUTH_CUR_USD column.
     * @return java.lang.String
     */
    public java.lang.String getAuthCurUsd()
    {
        return this.authCurUsd;
    }

    /**
     * Set the value of the AUTH_CUR_USD column.
     * @param authCurUsd
     */
    public void setAuthCurUsd(java.lang.String authCurUsd)
    {
        this.authCurUsd = authCurUsd;
    }

    /**
     * Return the value of the AUTH_CUR_EUR column.
     * @return java.lang.String
     */
    public java.lang.String getAuthCurEur()
    {
        return this.authCurEur;
    }

    /**
     * Set the value of the AUTH_CUR_EUR column.
     * @param authCurEur
     */
    public void setAuthCurEur(java.lang.String authCurEur)
    {
        this.authCurEur = authCurEur;
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
     * Return the value of the FULL_NAME column.
     * @return java.lang.String
     */
    public java.lang.String getFullName()
    {
        return this.fullName;
    }

    /**
     * Set the value of the FULL_NAME column.
     * @param fullName
     */
    public void setFullName(java.lang.String fullName)
    {
        this.fullName = fullName;
    }

    /**
     * Return the value of the COMMERCIAL_REGISTRY_NO column.
     * @return java.lang.String
     */
    public java.lang.String getCommercialRegistryNo()
    {
        return this.commercialRegistryNo;
    }

    /**
     * Set the value of the COMMERCIAL_REGISTRY_NO column.
     * @param commercialRegistryNo
     */
    public void setCommercialRegistryNo(java.lang.String commercialRegistryNo)
    {
        this.commercialRegistryNo = commercialRegistryNo;
    }

    /**
     * Return the value of the REGISTRY_COUNTRY column.
     * @return java.lang.String
     */
    public java.lang.String getRegistryCountry()
    {
        return this.registryCountry;
    }

    /**
     * Set the value of the REGISTRY_COUNTRY column.
     * @param registryCountry
     */
    public void setRegistryCountry(java.lang.String registryCountry)
    {
        this.registryCountry = registryCountry;
    }

    /**
     * Return the value of the ADDRESS1 column.
     * @return java.lang.String
     */
    public java.lang.String getAddress1()
    {
        return this.address1;
    }

    /**
     * Set the value of the ADDRESS1 column.
     * @param address1
     */
    public void setAddress1(java.lang.String address1)
    {
        this.address1 = address1;
    }

    /**
     * Return the value of the ADDRESS2 column.
     * @return java.lang.String
     */
    public java.lang.String getAddress2()
    {
        return this.address2;
    }

    /**
     * Set the value of the ADDRESS2 column.
     * @param address2
     */
    public void setAddress2(java.lang.String address2)
    {
        this.address2 = address2;
    }

    /**
     * Return the value of the ADDRESS3 column.
     * @return java.lang.String
     */
    public java.lang.String getAddress3()
    {
        return this.address3;
    }

    /**
     * Set the value of the ADDRESS3 column.
     * @param address3
     */
    public void setAddress3(java.lang.String address3)
    {
        this.address3 = address3;
    }

    /**
     * Return the value of the ADDRESS4 column.
     * @return java.lang.String
     */
    public java.lang.String getAddress4()
    {
        return this.address4;
    }

    /**
     * Set the value of the ADDRESS4 column.
     * @param address4
     */
    public void setAddress4(java.lang.String address4)
    {
        this.address4 = address4;
    }

    /**
     * Return the value of the TELEPHONE column.
     * @return java.lang.String
     */
    public java.lang.String getTelephone()
    {
        return this.telephone;
    }

    /**
     * Set the value of the TELEPHONE column.
     * @param telephone
     */
    public void setTelephone(java.lang.String telephone)
    {
        this.telephone = telephone;
    }

    /**
     * Return the value of the FAX_NO column.
     * @return java.lang.String
     */
    public java.lang.String getFaxNo()
    {
        return this.faxNo;
    }

    /**
     * Set the value of the FAX_NO column.
     * @param faxNo
     */
    public void setFaxNo(java.lang.String faxNo)
    {
        this.faxNo = faxNo;
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
     * Return the value of the REP1_FULL_NAME column.
     * @return java.lang.String
     */
    public java.lang.String getRep1FullName()
    {
        return this.rep1FullName;
    }

    /**
     * Set the value of the REP1_FULL_NAME column.
     * @param rep1FullName
     */
    public void setRep1FullName(java.lang.String rep1FullName)
    {
        this.rep1FullName = rep1FullName;
    }

    /**
     * Return the value of the REP1_SHORT_NAME column.
     * @return java.lang.String
     */
    public java.lang.String getRep1ShortName()
    {
        return this.rep1ShortName;
    }

    /**
     * Set the value of the REP1_SHORT_NAME column.
     * @param rep1ShortName
     */
    public void setRep1ShortName(java.lang.String rep1ShortName)
    {
        this.rep1ShortName = rep1ShortName;
    }

    /**
     * Return the value of the REP1_TITLE column.
     * @return java.lang.String
     */
    public java.lang.String getRep1Title()
    {
        return this.rep1Title;
    }

    /**
     * Set the value of the REP1_TITLE column.
     * @param rep1Title
     */
    public void setRep1Title(java.lang.String rep1Title)
    {
        this.rep1Title = rep1Title;
    }

    /**
     * Return the value of the REP1_ID_TYPE column.
     * @return java.lang.String
     */
    public java.lang.String getRep1IdType()
    {
        return this.rep1IdType;
    }

    /**
     * Set the value of the REP1_ID_TYPE column.
     * @param rep1IdType
     */
    public void setRep1IdType(java.lang.String rep1IdType)
    {
        this.rep1IdType = rep1IdType;
    }

    /**
     * Return the value of the REP1_ID_NO column.
     * @return java.lang.String
     */
    public java.lang.String getRep1IdNo()
    {
        return this.rep1IdNo;
    }

    /**
     * Set the value of the REP1_ID_NO column.
     * @param rep1IdNo
     */
    public void setRep1IdNo(java.lang.String rep1IdNo)
    {
        this.rep1IdNo = rep1IdNo;
    }

    /**
     * Return the value of the REP1_ID_ISSUE_DATE column.
     * @return java.util.Date
     */
    public java.util.Date getRep1IdIssueDate()
    {
        return this.rep1IdIssueDate;
    }

    /**
     * Set the value of the REP1_ID_ISSUE_DATE column.
     * @param rep1IdIssueDate
     */
    public void setRep1IdIssueDate(java.util.Date rep1IdIssueDate)
    {
        this.rep1IdIssueDate = rep1IdIssueDate;
    }

    /**
     * Return the value of the REP1_ID_ISSUER column.
     * @return java.lang.String
     */
    public java.lang.String getRep1IdIssuer()
    {
        return this.rep1IdIssuer;
    }

    /**
     * Set the value of the REP1_ID_ISSUER column.
     * @param rep1IdIssuer
     */
    public void setRep1IdIssuer(java.lang.String rep1IdIssuer)
    {
        this.rep1IdIssuer = rep1IdIssuer;
    }

    /**
     * Return the value of the REP1_TELEPHONE column.
     * @return java.lang.String
     */
    public java.lang.String getRep1Telephone()
    {
        return this.rep1Telephone;
    }

    /**
     * Set the value of the REP1_TELEPHONE column.
     * @param rep1Telephone
     */
    public void setRep1Telephone(java.lang.String rep1Telephone)
    {
        this.rep1Telephone = rep1Telephone;
    }

    /**
     * Return the value of the REP1_MOBILE column.
     * @return java.lang.String
     */
    public java.lang.String getRep1Mobile()
    {
        return this.rep1Mobile;
    }

    /**
     * Set the value of the REP1_MOBILE column.
     * @param rep1Mobile
     */
    public void setRep1Mobile(java.lang.String rep1Mobile)
    {
        this.rep1Mobile = rep1Mobile;
    }

    /**
     * Return the value of the REP1_EMAIL column.
     * @return java.lang.String
     */
    public java.lang.String getRep1Email()
    {
        return this.rep1Email;
    }

    /**
     * Set the value of the REP1_EMAIL column.
     * @param rep1Email
     */
    public void setRep1Email(java.lang.String rep1Email)
    {
        this.rep1Email = rep1Email;
    }

    /**
     * Return the value of the REP1_FAX_NO column.
     * @return java.lang.String
     */
    public java.lang.String getRep1FaxNo()
    {
        return this.rep1FaxNo;
    }

    /**
     * Set the value of the REP1_FAX_NO column.
     * @param rep1FaxNo
     */
    public void setRep1FaxNo(java.lang.String rep1FaxNo)
    {
        this.rep1FaxNo = rep1FaxNo;
    }

    /**
     * Return the value of the REP2_FULL_NAME column.
     * @return java.lang.String
     */
    public java.lang.String getRep2FullName()
    {
        return this.rep2FullName;
    }

    /**
     * Set the value of the REP2_FULL_NAME column.
     * @param rep2FullName
     */
    public void setRep2FullName(java.lang.String rep2FullName)
    {
        this.rep2FullName = rep2FullName;
    }

    /**
     * Return the value of the REP2_SHORT_NAME column.
     * @return java.lang.String
     */
    public java.lang.String getRep2ShortName()
    {
        return this.rep2ShortName;
    }

    /**
     * Set the value of the REP2_SHORT_NAME column.
     * @param rep2ShortName
     */
    public void setRep2ShortName(java.lang.String rep2ShortName)
    {
        this.rep2ShortName = rep2ShortName;
    }

    /**
     * Return the value of the REP2_TITLE column.
     * @return java.lang.String
     */
    public java.lang.String getRep2Title()
    {
        return this.rep2Title;
    }

    /**
     * Set the value of the REP2_TITLE column.
     * @param rep2Title
     */
    public void setRep2Title(java.lang.String rep2Title)
    {
        this.rep2Title = rep2Title;
    }

    /**
     * Return the value of the REP2_ID_TYPE column.
     * @return java.lang.String
     */
    public java.lang.String getRep2IdType()
    {
        return this.rep2IdType;
    }

    /**
     * Set the value of the REP2_ID_TYPE column.
     * @param rep2IdType
     */
    public void setRep2IdType(java.lang.String rep2IdType)
    {
        this.rep2IdType = rep2IdType;
    }

    /**
     * Return the value of the REP2_ID_NO column.
     * @return java.lang.String
     */
    public java.lang.String getRep2IdNo()
    {
        return this.rep2IdNo;
    }

    /**
     * Set the value of the REP2_ID_NO column.
     * @param rep2IdNo
     */
    public void setRep2IdNo(java.lang.String rep2IdNo)
    {
        this.rep2IdNo = rep2IdNo;
    }

    /**
     * Return the value of the REP2_ID_ISSUE_DATE column.
     * @return java.util.Date
     */
    public java.util.Date getRep2IdIssueDate()
    {
        return this.rep2IdIssueDate;
    }

    /**
     * Set the value of the REP2_ID_ISSUE_DATE column.
     * @param rep2IdIssueDate
     */
    public void setRep2IdIssueDate(java.util.Date rep2IdIssueDate)
    {
        this.rep2IdIssueDate = rep2IdIssueDate;
    }

    /**
     * Return the value of the REP2_ID_ISSUER column.
     * @return java.lang.String
     */
    public java.lang.String getRep2IdIssuer()
    {
        return this.rep2IdIssuer;
    }

    /**
     * Set the value of the REP2_ID_ISSUER column.
     * @param rep2IdIssuer
     */
    public void setRep2IdIssuer(java.lang.String rep2IdIssuer)
    {
        this.rep2IdIssuer = rep2IdIssuer;
    }

    /**
     * Return the value of the REP2_TELEPHONE column.
     * @return java.lang.String
     */
    public java.lang.String getRep2Telephone()
    {
        return this.rep2Telephone;
    }

    /**
     * Set the value of the REP2_TELEPHONE column.
     * @param rep2Telephone
     */
    public void setRep2Telephone(java.lang.String rep2Telephone)
    {
        this.rep2Telephone = rep2Telephone;
    }

    /**
     * Return the value of the REP2_MOBILE column.
     * @return java.lang.String
     */
    public java.lang.String getRep2Mobile()
    {
        return this.rep2Mobile;
    }

    /**
     * Set the value of the REP2_MOBILE column.
     * @param rep2Mobile
     */
    public void setRep2Mobile(java.lang.String rep2Mobile)
    {
        this.rep2Mobile = rep2Mobile;
    }

    /**
     * Return the value of the REP2_EMAIL column.
     * @return java.lang.String
     */
    public java.lang.String getRep2Email()
    {
        return this.rep2Email;
    }

    /**
     * Set the value of the REP2_EMAIL column.
     * @param rep2Email
     */
    public void setRep2Email(java.lang.String rep2Email)
    {
        this.rep2Email = rep2Email;
    }

    /**
     * Return the value of the REP2_FAX_NO column.
     * @return java.lang.String
     */
    public java.lang.String getRep2FaxNo()
    {
        return this.rep2FaxNo;
    }

    /**
     * Set the value of the REP2_FAX_NO column.
     * @param rep2FaxNo
     */
    public void setRep2FaxNo(java.lang.String rep2FaxNo)
    {
        this.rep2FaxNo = rep2FaxNo;
    }

    /**
     * Return the value of the FOREIGN_CITY column.
     * @return java.lang.String
     */
    public java.lang.String getForeignCity()
    {
        return this.foreignCity;
    }

    /**
     * Set the value of the FOREIGN_CITY column.
     * @param foreignCity
     */
    public void setForeignCity(java.lang.String foreignCity)
    {
        this.foreignCity = foreignCity;
    }

    /**
     * Return the value of the TIME_ZONE column.
     * @return java.lang.String
     */
    public java.lang.String getTimeZone()
    {
        return this.timeZone;
    }

    /**
     * Set the value of the TIME_ZONE column.
     * @param timeZone
     */
    public void setTimeZone(java.lang.String timeZone)
    {
        this.timeZone = timeZone;
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
        if (! (rhs instanceof CorporationHis))
            return false;
        CorporationHis that = (CorporationHis) rhs;
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

	public java.lang.String getCorpType() {
		return corpType;
	}

	public void setCorpType(java.lang.String corpType) {
		this.corpType = corpType;
	}

	public java.lang.String getAllowFinancialController() {
		return allowFinancialController;
	}

	public void setAllowFinancialController(
			java.lang.String allowFinancialController) {
		this.allowFinancialController = allowFinancialController;
	}

	public java.lang.String getAuthenticationMode() {
		return authenticationMode;
	}

	public void setAuthenticationMode(java.lang.String authenticationMode) {
		this.authenticationMode = authenticationMode;
	}

	public java.lang.String getOtpLogin() {
		return otpLogin;
	}

	public void setOtpLogin(java.lang.String otpLogin) {
		this.otpLogin = otpLogin;
	}
}