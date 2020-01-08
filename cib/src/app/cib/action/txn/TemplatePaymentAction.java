/**
 *
 */
package app.cib.action.txn;

import java.math.BigDecimal;
import java.util.*;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.ApplicationContext;

import app.cib.bo.txn.BillPayment;
import app.cib.util.Constants;
import app.cib.util.UploadReporter;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBWarningException;
import com.neturbo.set.utils.Amount;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bat.FileRequest;
import app.cib.bo.bnk.Corporation;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.service.bnk.CorporationService;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.srv.RequestService;
import app.cib.service.sys.ApproveRuleService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.CutOffTimeService;
import app.cib.service.sys.MailService;
import app.cib.service.txn.BillPaymentService;
import app.cib.service.txn.TemplatePaymentService;
import app.cib.service.txn.TransferLimitService;

/**
 * @author hjs
 *
 */
public class TemplatePaymentAction extends CibAction implements Approvable {

	public void listTemplate() throws NTBException {

		String merSelected= null;
		String corpID= null;

		ApplicationContext appContext = Config.getAppContext();
		TemplatePaymentService tempPaymentService = (TemplatePaymentService) appContext
				.getBean("TemplatePaymentService");

		merSelected = Utils.null2EmptyWithTrim(getParameter("merSelected")) ;
		
		CorpUser corpUser = (CorpUser) getUser();
		corpID =  corpUser.getCorpId();

		List ptList = tempPaymentService.listTemplate(corpID, merSelected.equals("0") ? "" : merSelected);
		List templateList = this.convertPojoList2MapList(ptList);

        Map resultData = new HashMap();
        resultData.put("listSize", new Integer(templateList.size()));
        resultData.put("merSelected", merSelected);
        resultData.put("templateList", templateList);
        resultData.put("general_Template", BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE);
        resultData.put("card_Template", BillPayment.PAYMENT_TYPE_CARD_TEMPLATE);
        setResultData(resultData);
        this.setUsrSessionDataValue("ptList", ptList);
	}

	public void addTemplateLoad() throws NTBException {
        Map resultData = new HashMap();
        resultData.put("payType1", BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE);
        resultData.put("payType2", BillPayment.PAYMENT_TYPE_CARD_TEMPLATE);
        setResultData(resultData);
	}

	public void addTemplate() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        CorpAccountService corpAccountService = (CorpAccountService)appContext.getBean("CorpAccountService");
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = (CorpUser) this.getUser();
		corpUser.setLanguage(lang);//add by linrui for mul-language
		CorpAccount corpAccount = null;
		String fromAccount = "";
		String fromAccountName = "";

		BillPayment billPayment = new BillPayment(CibIdGenerator.getRefNoForTransaction());
        this.convertMap2Pojo(this.getParameters(), billPayment);
        billPayment.setUserId(corpUser.getUserId());
        billPayment.setCorpId(corpUser.getCorpId());
        billPayment.setOperation(Constants.OPERATION_NEW);
        billPayment.setStatus(Constants.STATUS_NORMAL);
        billPayment.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        billPayment.setRequestTime(new Date());
        billPayment.setExecuteTime(new Date());

        //ͨ���ʺ�ȡ���ʺ����
        fromAccount = billPayment.getFromAccount();
        corpAccount = corpAccountService.viewCorpAccount(fromAccount);
        fromAccountName= corpAccount.getAccountName();

        Map resultData = new HashMap();
        resultData.put("fromAccountName", fromAccountName);
        setResultData(resultData);
        resultData.put("payType1", BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE);
        resultData.put("payType2", BillPayment.PAYMENT_TYPE_CARD_TEMPLATE);
        if(billPayment.getPayType().equals(BillPayment.PAYMENT_TYPE_CARD_TEMPLATE)){
    		// get infomation from host
    		Map fromHost = billPaymentService.getCardPaymentInfo(corpUser, billPayment.getBillNo1());
    		String cardCcy = fromHost.get("CARD_CURRENCY").toString();
            resultData.put("cardCcy", cardCcy);
        }
        this.convertPojo2Map(billPayment, resultData);
        setResultData(resultData);
        //���û����д��session���Ա�confirm��д����ݿ�
        this.setUsrSessionDataValue("billPayment", billPayment);
	}

	public void addTemplateCfm() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        TemplatePaymentService tempPaymentService = (TemplatePaymentService)appContext.getBean("TemplatePaymentService");

        String fromAccountName = this.getParameter("fromAccountName");

        //���һ����д����ݿ�
		BillPayment billPayment = (BillPayment)this.getUsrSessionDataValue("billPayment");
		tempPaymentService.addTemplate(billPayment);
        //��ʾ������
        Map resultData = new HashMap();
        resultData.put("fromAccountName", fromAccountName);
        resultData.put("payType1", BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE);
        resultData.put("payType2", BillPayment.PAYMENT_TYPE_CARD_TEMPLATE);
        resultData.put("cardCcy", this.getParameter("cardCcy"));
        setResultData(resultData);
        this.convertPojo2Map(billPayment, resultData);
	}

	public void addTemplateCancel() throws NTBException {
	}

	public void editTemplateLoad() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        TemplatePaymentService tempPaymentService = (TemplatePaymentService)appContext.getBean("TemplatePaymentService");

        String transID = getParameter("transId");
        BillPayment billPayment = tempPaymentService.viewTemplate(transID);
        Map resultData = new HashMap();
        resultData.put("payType1", BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE);
        resultData.put("payType2", BillPayment.PAYMENT_TYPE_CARD_TEMPLATE);
        this.convertPojo2Map(billPayment, resultData);
        setResultData(resultData);
        //���û����д��session���Ա�confirm��д����ݿ�
        this.setUsrSessionDataValue("billPayment", billPayment);
	}

	public void editTemplate() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        CorpAccountService corpAccountService = (CorpAccountService)appContext.getBean("CorpAccountService");

		CorpAccount corpAccount = null;
		String fromAccount = "";
		String fromAccountName = "";

		BillPayment billPayment = (BillPayment)this.getUsrSessionDataValue("billPayment");
        this.convertMap2Pojo(this.getParameters(), billPayment);
        billPayment.setOperation(Constants.OPERATION_UPDATE);
        billPayment.setStatus(Constants.STATUS_NORMAL);
        billPayment.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        billPayment.setRequestTime(new Date());
        billPayment.setExecuteTime(new Date());

        //ͨ���ʺ�ȡ���ʺ����
        fromAccount = billPayment.getFromAccount();
        corpAccount = corpAccountService.viewCorpAccount(fromAccount);
        fromAccountName= corpAccount.getAccountName();

        Map resultData = new HashMap();
        resultData.put("fromAccountName", fromAccountName);
        resultData.put("payType1", BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE);
        resultData.put("payType2", BillPayment.PAYMENT_TYPE_CARD_TEMPLATE);
        setResultData(resultData);
        this.convertPojo2Map(billPayment, resultData);
        //���û����д��session���Ա�confirm��д����ݿ�
        this.setUsrSessionDataValue("billPayment", billPayment);
	}

	public void editTemplateCfm() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        TemplatePaymentService tempPaymentService = (TemplatePaymentService)appContext.getBean("TemplatePaymentService");

        String fromAccountName = this.getParameter("fromAccountName");

        //���һ����д����ݿ�
        CorpUser corpUser = (CorpUser) this.getUser();
		BillPayment billPayment = (BillPayment)this.getUsrSessionDataValue("billPayment");
		tempPaymentService.editTemplate(billPayment, corpUser.getUserId());
        //��ʾ������
        Map resultData = new HashMap();
        resultData.put("fromAccountName", fromAccountName);
        resultData.put("payType1", BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE);
        resultData.put("payType2", BillPayment.PAYMENT_TYPE_CARD_TEMPLATE);
        setResultData(resultData);
        this.convertPojo2Map(billPayment, resultData);
	}

	public void editTemplateCancel() throws NTBException {
	}

	public void deleteTemplateLoad() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        TemplatePaymentService tempPaymentService = (TemplatePaymentService)appContext.getBean("TemplatePaymentService");
        CorpAccountService corpAccountService = (CorpAccountService)appContext.getBean("CorpAccountService");

        CorpAccount corpAccount = null;
		String fromAccount = "";
		String fromAccountName = "";

        String transID = getParameter("transId");
        BillPayment billPayment = tempPaymentService.viewTemplate(transID);
        billPayment.setOperation(Constants.OPERATION_REMOVE);
        billPayment.setStatus(Constants.STATUS_REMOVED);
        billPayment.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        billPayment.setRequestTime(new Date());
        billPayment.setExecuteTime(new Date());

        //ͨ���ʺ�ȡ���ʺ����
        fromAccount = billPayment.getFromAccount();
        corpAccount = corpAccountService.viewCorpAccount(fromAccount);
        fromAccountName= corpAccount.getAccountName();

        Map resultData = new HashMap();
        resultData.put("fromAccountName", fromAccountName);
        resultData.put("payType1", BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE);
        resultData.put("payType2", BillPayment.PAYMENT_TYPE_CARD_TEMPLATE);
        this.convertPojo2Map(billPayment, resultData);
        setResultData(resultData);
        //���û����д��session���Ա�confirm��д����ݿ�
        this.setUsrSessionDataValue("billPayment", billPayment);
	}

	public void deleteTemplate() throws NTBException {
		String fromAccountName = this.getParameter("fromAccountName");

        BillPayment billPayment = (BillPayment) this.getUsrSessionDataValue("billPayment");

        Map resultData = new HashMap();
        resultData.put("fromAccountName", fromAccountName);
        resultData.put("payType1", BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE);
        resultData.put("payType2", BillPayment.PAYMENT_TYPE_CARD_TEMPLATE);
        this.convertPojo2Map(billPayment, resultData);
        setResultData(resultData);
	}

	public void deleteTemplateCfm() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        TemplatePaymentService tempPaymentService = (TemplatePaymentService)appContext.getBean("TemplatePaymentService");

        String fromAccountName = this.getParameter("fromAccountName");

        //���һ����д����ݿ�
        CorpUser corpUser = (CorpUser) this.getUser();
		BillPayment billPayment = (BillPayment)this.getUsrSessionDataValue("billPayment");
		tempPaymentService.deleteTemplate(billPayment, corpUser.getUserId());
        //��ʾ������
        Map resultData = new HashMap();
        resultData.put("fromAccountName", fromAccountName);
        resultData.put("payType1", BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE);
        resultData.put("payType2", BillPayment.PAYMENT_TYPE_CARD_TEMPLATE);
        setResultData(resultData);
        this.convertPojo2Map(billPayment, resultData);
	}

	public void deleteTemplateCancel() throws NTBException {
	}

	public void gtPaymentLoad() throws NTBException {
		
        ApplicationContext appContext = Config.getAppContext();
        BillPaymentService billPaymentService = (BillPaymentService)appContext.getBean("BillPaymentService");
        Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = (CorpUser) this.getUser();
        corpUser.setLanguage(lang);//add by linrui for mul-language
		String transID = Utils.null2EmptyWithTrim(this.getParameter("transId"));
		BillPayment billPayment =  billPaymentService.viewPayment(transID, BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE);
		if (billPayment == null){
			billPayment = new BillPayment();
			listTemplate();
			throw new NTBException("err.txn.ThisTemplateIsNotAGenneralTemplate");
		}else{
			
			Double transferAmount = null;
			String currency = "";
			
			//Get Infomation from Host
			Map fromHost = null;
			String inputAmountFlag = "";
			if (billPayment.getCategory().equals("010")&&sepcialMerchant(billPayment.getMerchant())) {
				if (billPayment.getMerchant().equals("CEM")) {
					fromHost = billPaymentService.getCEMBillInfo(corpUser, billPayment.getBillNo1());
				} else if (billPayment.getMerchant().equals("SAAM")) {
					fromHost = billPaymentService.getSAAMBillInfo(corpUser, billPayment.getBillNo1());
				} else if (billPayment.getMerchant().equals("CTM")) {
					fromHost = billPaymentService.getCTMBillInfo(corpUser, billPayment.getBillNo1());				
				}
				transferAmount = Double.valueOf(fromHost.get("AMOUNT_DUE").toString());
				currency = "MOP";
				
				fromHost.put("EXPIRY_DATE", fromHost.get("BILLING_DATE"));
				inputAmountFlag = "0";
			} else {
				String acc = billPaymentService.checkOtherMerchant(billPayment.getCategory(), billPayment.getMerchant());
				if(acc == null){
					fromHost = billPaymentService.getGenPaymentInfo(corpUser, billPayment.getCategory(), billPayment.getMerchant(), billPayment.getBillNo1());
					transferAmount = Double.valueOf(fromHost.get("PAYMENT_AMOUNT").toString());
					currency = fromHost.get("BILL_CURRENCY").toString();
					inputAmountFlag = "0";
				} else {
					fromHost = new HashMap();
					currency = acc;
					inputAmountFlag = "1";
				}
			}
			
			billPayment.setTransId(String.valueOf(CibIdGenerator.getRefNoForTransaction()));
			billPayment.setUserId(corpUser.getUserId());
			billPayment.setCorpId(corpUser.getCorpId());
			billPayment.setTransferAmount(transferAmount);
			billPayment.setPayType(BillPayment.PAYMENT_TYPE_GENERAL);
			billPayment.setOperation(Constants.OPERATION_NEW);
			billPayment.setStatus(Constants.STATUS_PENDING_APPROVAL);
			billPayment.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			billPayment.setRequestTime(new Date());
			billPayment.setExecuteTime(null);
			billPayment.setCurrency(currency);

	        Map resultData = new HashMap();
	        this.convertPojo2Map(billPayment, resultData);
	        resultData.putAll(fromHost);
			resultData.put("inputAmountFlag", inputAmountFlag);
	        setResultData(resultData);
	        
	        //���û����д��session���Ա�confirm��д����ݿ�
	        this.setUsrSessionDataValue("billPayment", billPayment);
			this.setUsrSessionDataValue("EXPIRY_DATE", fromHost.get("EXPIRY_DATE"));
			this.setUsrSessionDataValue("REMARKS", fromHost.get("REMARKS"));
		}
	}

	public void ctPaymentLoad() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        BillPaymentService billPaymentService = (BillPaymentService)appContext.getBean("BillPaymentService");
        Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = (CorpUser) this.getUser();
        corpUser.setLanguage(lang);//add by linrui for mul-language
		String transID = Utils.null2EmptyWithTrim(this.getParameter("transId"));
		BillPayment billPayment =  billPaymentService.viewPayment(transID, BillPayment.PAYMENT_TYPE_CARD_TEMPLATE);
		if (billPayment == null){
			billPayment = new BillPayment();
			listTemplate();
			throw new NTBException("err.txn.theTemplateIsNotACardTemplate");
		}else{
			
			//get infomation from host
			Map fromHost= billPaymentService.getCardPaymentInfo(corpUser, billPayment.getBillNo1());
			String currency = fromHost.get("CARD_CURRENCY").toString();

			billPayment.setTransId(String.valueOf(CibIdGenerator.getRefNoForTransaction()));
			billPayment.setUserId(corpUser.getUserId());
			billPayment.setCorpId(corpUser.getCorpId());
			billPayment.setPayType(BillPayment.PAYMENT_TYPE_CARD);
			billPayment.setOperation(Constants.OPERATION_NEW);
			billPayment.setStatus(Constants.STATUS_PENDING_APPROVAL);
			billPayment.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			billPayment.setRequestTime(new Date());
			billPayment.setExecuteTime(null);
			billPayment.setCurrency(currency);
		}


        Map resultData = new HashMap();
        this.convertPojo2Map(billPayment, resultData);
        setResultData(resultData);
        //���û����д��session���Ա�confirm��д����ݿ�
        this.setUsrSessionDataValue("billPayment", billPayment);
	}
	
	public void batchPaymentLoad() throws NTBException {
        String[] transIds = this.getParameterValues("transId");
        List ptList = (List) this.getUsrSessionDataValue("ptList");
        List ptList2Jsp = new ArrayList();
        List ptList2DB = new ArrayList();
        BillPayment bill = null;
        for(int i=0; i<ptList.size(); i++){
        	bill = (BillPayment) ptList.get(i);
        	BillPayment billPayment = new BillPayment();
        	try {
				BeanUtils.copyProperties(billPayment, bill);
			} catch (Exception e) {
				Log.error("copy properties error", e);
				throw new NTBException("err.sys.GeneralError");
			}
        	if(isExist(transIds, billPayment.getTransId().trim())){
        		Map newRow = new HashMap();
        		newRow.put("inputAmountFlag", this.toHost(billPayment));
        		ptList2DB.add(billPayment);
        		this.convertPojo2Map(billPayment, newRow);
        		ptList2Jsp.add(newRow);
        	}
        }
        Map resultData = new HashMap();
        resultData.put("ptList2Jsp", ptList2Jsp);
        this.setResultData(resultData);
        this.clearUsrSessionDataValue();
        this.setUsrSessionDataValue("ptList2DB", ptList2DB);
	}
	
	private String toHost(BillPayment billPayment) throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        BillPaymentService billPaymentService = (BillPaymentService)appContext.getBean("BillPaymentService");
        Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
        String retFlag = "";
        CorpUser corpUser = (CorpUser) this.getUser();
        corpUser.setLanguage(lang);//add by linrui for mul-language
		Double transferAmount = null;
		String currency = "";
		Map fromHost = null;
        
		if(billPayment.getPayType().equals(BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE)){
			//Get Infomation from Host
			String acc = "";
			if (billPayment.getCategory().equals("010")&&sepcialMerchant(billPayment.getMerchant())) {
				if (billPayment.getMerchant().equals("CEM")) {
					fromHost = billPaymentService.getCEMBillInfo(corpUser, billPayment.getBillNo1());
				} else if (billPayment.getMerchant().equals("SAAM")) {
					fromHost = billPaymentService.getSAAMBillInfo(corpUser, billPayment.getBillNo1());
				} else if (billPayment.getMerchant().equals("CTM")) {
					fromHost = billPaymentService.getCTMBillInfo(corpUser, billPayment.getBillNo1());				
				}
				transferAmount = Double.valueOf(fromHost.get("AMOUNT_DUE").toString());
				currency = billPaymentService.getMerchantBill(
						billPayment.getCategory(), billPayment.getMerchant()).getSuspendAccCcy();
				//return
				retFlag="0";
			} else {
				acc = billPaymentService.checkOtherMerchant(billPayment.getCategory(), billPayment.getMerchant());
				if(acc == null){
					fromHost = billPaymentService.getGenPaymentInfo(corpUser, billPayment.getCategory(), billPayment.getMerchant(), billPayment.getBillNo1());
					transferAmount = Double.valueOf(fromHost.get("PAYMENT_AMOUNT").toString());
					currency = fromHost.get("BILL_CURRENCY").toString();
					//return
					retFlag="0";
				} else {
					fromHost = new HashMap();
					currency = acc;
					//return
					retFlag="1";
				}
			}
			billPayment.setUserId(corpUser.getUserId());
			billPayment.setCorpId(corpUser.getCorpId());
			billPayment.setTransferAmount(transferAmount);
			billPayment.setPayType(BillPayment.PAYMENT_TYPE_GENERAL);
			billPayment.setOperation(Constants.OPERATION_NEW);
			billPayment.setStatus(Constants.STATUS_PENDING_APPROVAL);
			billPayment.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			billPayment.setRequestTime(new Date());
			billPayment.setExecuteTime(null);
			billPayment.setCurrency(currency);
		} else if(billPayment.getPayType().equals(BillPayment.PAYMENT_TYPE_CARD_TEMPLATE)){
			//get infomation from host
			fromHost= billPaymentService.getCardPaymentInfo(corpUser, billPayment.getBillNo1());
			currency = fromHost.get("CARD_CURRENCY").toString();

			billPayment.setUserId(corpUser.getUserId());
			billPayment.setCorpId(corpUser.getCorpId());
			billPayment.setPayType(BillPayment.PAYMENT_TYPE_CARD);
			billPayment.setOperation(Constants.OPERATION_NEW);
			billPayment.setStatus(Constants.STATUS_PENDING_APPROVAL);
			billPayment.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			billPayment.setRequestTime(new Date());
			billPayment.setExecuteTime(null);
			billPayment.setCurrency(currency);
			//return
			retFlag="1";
		}
		// set primary key
		billPayment.setTransId(String.valueOf(CibIdGenerator.getRefNoForTransaction()));
		return retFlag;
	}
	
	private boolean isExist(String[] array, String value) {
		for(int i=0; i<array.length; i++){
			if(array[i].equals(value)){
				return true;
			}
		}
		return false;
	}
	
	public void batchPayment() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		
		//for review
		List reviewList = (List) this.getResultData().get("ptList2Jsp");
		Map row = null;
		for(int i=0; i<reviewList.size(); i++){
			row = (Map) reviewList.get(i);
			if(this.getParameter("transferAmount_"+i)!=null){
				row.put("transferAmount", this.getParameter("transferAmount_"+i));
			}
		}
		
		CorpUser corpUser = (CorpUser) getUser();
		String fromAccount = this.getParameter("fromAccount");
		
        List ptList2Jsp = new ArrayList();
		List ptList2DB = (List) this.getUsrSessionDataValue("ptList2DB");
		BillPayment billPayment = null;
		BigDecimal totalTransMopEqAmt = new BigDecimal("0");
		BigDecimal transMopEqAmt = null;;
		for(int i=0; i<ptList2DB.size(); i++){
			billPayment = (BillPayment) ptList2DB.get(i);
			billPayment.setFromAccount(this.getParameter("fromAccount"));
			if(this.getParameter("transferAmount_"+i)!=null){
				if(!Amount.check(this.getParameter("transferAmount_"+i), 13, 2)){
					this.getResultData().put("errIndex", new Integer(i));
					throw new NTBException("err.txn.AmountFormatError");
				}
				billPayment.setTransferAmount(new Double(Utils.replaceStr(this.getParameter("transferAmount_"+i), ",", "")));
			}
			transMopEqAmt = exRatesService.getEquivalent(corpUser.getCorpId(),
					billPayment.getCurrency(), "MOP",
					new BigDecimal(billPayment.getTransferAmount().toString()), null, 2);
			totalTransMopEqAmt = totalTransMopEqAmt.add(transMopEqAmt);
		}
		ptList2Jsp = this.convertPojoList2MapList(ptList2DB);
		
		// ȡ��Debit�ʺŻ������ͺͽ��
		String debitCurrency = corpAccountService.getCurrency(fromAccount,true);
		BigDecimal debitAmount = exRatesService.getEquivalent(corpUser.getCorpId(),
				debitCurrency, "MOP", //TODO TO_CCY
				null, totalTransMopEqAmt, 2);
		
		// ͷ��Ϣ
		FileRequest fileRequest = new FileRequest(CibIdGenerator.getRefNoForTransaction());
		fileRequest.setCorpId(corpUser.getCorpId());
		fileRequest.setUserId(corpUser.getUserId());
		fileRequest.setBatchType(FileRequest.BATCH_PAYMENT);
		fileRequest.setTotalNumber(new Integer(ptList2DB.size()));
		fileRequest.setFromAccount(fromAccount);
		fileRequest.setFromCurrency(debitCurrency);
		fileRequest.setFromAmount(new Double(debitAmount.toString()));
		fileRequest.setToAccount(null);
		fileRequest.setToCurrency("MOP"); //TODO TO_CCY
		fileRequest.setToAmount(new Double(totalTransMopEqAmt.toString()));
		fileRequest.setOperation(Constants.OPERATION_NEW);
		fileRequest.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
		fileRequest.setStatus(Constants.STATUS_PENDING_APPROVAL);
		fileRequest.setRequestTime(new Date());

		// ��֯assignuser_tag���
		Map assignuser = new HashMap();
		BigDecimal amountMopEq = totalTransMopEqAmt;
		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "currency");
		assignuser.put("amountField", "transferAmount");
		assignuser.put("amountMopEqField", "amountMopEq");
		assignuser.put("txnType", Constants.TXN_SUBTYPE_BATCH_PAYMENT);
		assignuser.put("currency", "MOP");
		assignuser.put("amountMopEq", amountMopEq);
		
		// Check User Limit
		BigDecimal debitAmountMopEq = totalTransMopEqAmt;
		if (!corpUser.checkUserLimit(Constants.TXN_TYPE_PAY_BILLS, debitAmountMopEq)) {
			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", fileRequest.getUserId());
			reportMap.put("CORP_ID", fileRequest.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", fileRequest.getBatchId());
			reportMap.put("FROM_ACCOUNT", fileRequest.getFromAccount());
			reportMap.put("FROM_CURRENCY", fileRequest.getFromCurrency());
			reportMap.put("TO_CURRENCY", fileRequest.getToCurrency());
			reportMap.put("FROM_AMOUNT", fileRequest.getFromAmount());
			reportMap.put("TO_AMOUNT", fileRequest.getToAmount());
			reportMap.put("EXCEEDING_TYPE", "2");
			reportMap.put("LIMIT_TYPE", "");
			reportMap.put("USER_LIMIT ", corpUser.getUserLimit(Constants.TXN_TYPE_PAY_BILLS));
			reportMap.put("DAILY_LIMIT ", new Double(0));
			reportMap.put("TOTAL_AMOUNT ", new Double(0));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			throw new NTBWarningException("err.txn.ExceedUserLimit");
		}
		// check Limit
		if (!transferLimitService.checkCurAmtLimitQuota(billPayment.getFromAccount(),
				corpUser.getCorpId(),
				Constants.TXN_TYPE_PAY_BILLS,
				fileRequest.getFromAmount().doubleValue(),
				amountMopEq.doubleValue())) {

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", billPayment.getUserId());
			reportMap.put("CORP_ID", billPayment.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", fileRequest.getBatchId());
			reportMap.put("FROM_ACCOUNT", fileRequest.getFromAccount());
			reportMap.put("FROM_CURRENCY", fileRequest.getFromCurrency());
			reportMap.put("TO_CURRENCY", fileRequest.getToCurrency());
			reportMap.put("FROM_AMOUNT", fileRequest.getFromAmount());
			reportMap.put("TO_AMOUNT", fileRequest.getToAmount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			throw new NTBWarningException("err.txn.ExceedDailyLimit");
		}
		
		Map resultData = new HashMap();
		resultData.put("ptList2Jsp", ptList2Jsp);
		resultData.put("fromAccount", fromAccount);
		resultData.put("fromCurrency", fileRequest.getFromCurrency());
		resultData.put("transferAmount", fileRequest.getToAmount());
		// ������ݵ�assignuser_tag
		resultData.putAll(assignuser);

		ApproveRuleService approveRuleService = (ApproveRuleService) Config.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(), resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}
		
		this.setResultData(resultData);

		this.setUsrSessionDataValue("fileRequest", fileRequest);
        this.setUsrSessionDataValue("ptList2DB", ptList2DB);
		this.setUsrSessionDataValue("assignuser", assignuser);
		this.setUsrSessionDataValue("billPayment", billPayment);
	}
	
	public void batchPaymentCancel() throws NTBException {
		List reviewList = (List) this.getResultData().get("ptList2Jsp");
		Map row = null;
		for(int i=0; i<reviewList.size(); i++){
			row = (Map) reviewList.get(i);
			row.put("transferAmount", (new BigDecimal(row.get("transferAmount").toString())));
			row.put("inputAmountFlag", getMerEnqFlag(
					row.get("payType").toString(),
					row.get("category").toString(),
					row.get("merchant").toString()));
		}
	}
	
	public void batchPaymentCfm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");
		MailService mailService = (MailService) appContext.getBean("MailService");

		CorpUser corpUser = (CorpUser) this.getUser();

		FileRequest fileRequest = (FileRequest) this.getUsrSessionDataValue("fileRequest");
		List ptList2DB = (List) this.getUsrSessionDataValue("ptList2DB");
		Map assignuser = (Map) this.getUsrSessionDataValue("assignuser");
		
		String txnType = (String) assignuser.get("txnType");
		BigDecimal amountMopEq = (BigDecimal) assignuser.get("amountMopEq");
		
		List ptList2Jsp = this.convertPojoList2MapList(ptList2DB);
		
		//check value date cut-off time
		checkCutoffTimeAndSetMsg(fileRequest);

		// add by chen_y for CR225 20170412
		BillPayment billPayment = (BillPayment) this.getUsrSessionDataValue("billPayment");
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		// check Limit
		if (!transferLimitService.checkLimitQuota(billPayment.getFromAccount(),
				corpUser.getCorpId(),
				Constants.TXN_TYPE_PAY_BILLS,
				fileRequest.getFromAmount().doubleValue(),
				amountMopEq.doubleValue())) {

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", billPayment.getUserId());
			reportMap.put("CORP_ID", billPayment.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", fileRequest.getBatchId());
			reportMap.put("FROM_ACCOUNT", fileRequest.getFromAccount());
			reportMap.put("FROM_CURRENCY", fileRequest.getFromCurrency());
			reportMap.put("TO_CURRENCY", fileRequest.getToCurrency());
			reportMap.put("FROM_AMOUNT", fileRequest.getFromAmount());
			reportMap.put("TO_AMOUNT", fileRequest.getToAmount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			//throw new NTBWarningException("err.txn.ExceedDailyLimit");
			setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg").getString("warnning.txn.ExceedDailyLimit"));
		}
		// add by chen_y for CR225 20170412 end
		String assignedUser = Utils.null2EmptyWithTrim(this.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this.getParameter("mailUser"));
		
		// �½�һ����Ȩ����
		String processId = flowEngineService.startProcess(txnType,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				TemplatePaymentAction.class,
				fileRequest.getFromCurrency(),
				fileRequest.getFromAmount().doubleValue(),
				fileRequest.getToCurrency(), //TODO TO_CCY
				fileRequest.getToAmount().doubleValue(),
				amountMopEq.doubleValue(),
				fileRequest.getBatchId(),
				"", corpUser, assignedUser,
				corpUser.getCorporation().getAllowExecutor(),
				FlowEngineService.RULE_FLAG_TO);
		try {
			billPaymentService.processBatchPayment(fileRequest, ptList2DB);
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("err.txn.TranscationFaily", e);
			if (e instanceof NTBException) {
				throw (NTBException)e;
			} else {
				throw new NTBException("err.txn.TranscationFaily");
			}
		}

		Map resultData = new HashMap();
		resultData.put("transId", fileRequest.getBatchId());
		resultData.put("ptList2Jsp", ptList2Jsp);
		resultData.put("fromAccount", fileRequest.getFromAccount());
		resultData.put("fromCurrency", fileRequest.getFromCurrency());
		resultData.put("transferAmount", fileRequest.getToAmount());
		// ������ݵ�assignuser_tag
		resultData.putAll(assignuser);
		this.setResultData(resultData);

		//send mial to approver
		Map dataMap = new HashMap();
		dataMap.put("userID", corpUser.getUserId());
		dataMap.put("userName", corpUser.getUserName());
		dataMap.put("requestTime", fileRequest.getRequestTime());
		
		/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
		/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_BATCH_PAYMENT, mailUser.split(";"), dataMap);*/
		mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_BATCH_PAYMENT, mailUser.split(";") ,corpUser.getCorpId(), dataMap);
		/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */
		
		this.clearUsrSessionDataValue();
	}
	
	private String getMerEnqFlag(String payType, String category, String merchant) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");

        String retFlag = "";
		if(payType.equals(BillPayment.PAYMENT_TYPE_GENERAL)){
			//Get Infomation from Host
			String acc = "";
			if (category.equals("010")) {
				//return
				retFlag="0";
			} else {
				acc = billPaymentService.checkOtherMerchant(category, merchant);
				if(acc == null){
					//return
					retFlag="0";
				} else {
					//return
					retFlag="1";
				}
			}
		} else if(payType.equals(BillPayment.PAYMENT_TYPE_CARD)){
			//return
			retFlag="1";
		}
		return retFlag;
		
	}

	public boolean approve(String txnType, String id, CibAction bean) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
		RequestService requestService = (RequestService) appContext.getBean("RequestService");
    	CutOffTimeService cutOffTimeService = (CutOffTimeService)appContext.getBean("CutOffTimeService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		
		FileRequest fileRequest = requestService.viewFileRequest(id);
		BigDecimal equivalentMOP = new BigDecimal(fileRequest.getToAmount().toString());
		

        // checkTransAvailable, add by Nabai 20121025, 59CC is the transaction no for bill payment.
		// we have to check if the bill payment transation is available or not before we update batch file to host.
		Log.info("Check 59CC for holiday before upload bill payment batch file to host");
		//modify by long_zg 2014-12-15 for CR192 batch bob
    	/*cutOffTimeService.checkTransAvailable("59CC");*/
		cutOffTimeService.checkTransAvailable("59CC",false,false);
		
		// check Limit
		if (!transferLimitService.checkLimitQuota(fileRequest.getFromAccount(),
				fileRequest.getCorpId(),
				Constants.TXN_TYPE_PAY_BILLS,
				fileRequest.getFromAmount().doubleValue(),
				equivalentMOP.doubleValue())) {

			// write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", fileRequest.getUserId());
			reportMap.put("CORP_ID", fileRequest.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_PAY_BILLS);
			reportMap.put("LOCAL_TRANS_CODE", fileRequest.getBatchId());
			reportMap.put("FROM_ACCOUNT", fileRequest.getFromAccount());
			reportMap.put("FROM_CURRENCY", fileRequest.getFromCurrency());
			reportMap.put("TO_CURRENCY", fileRequest.getToCurrency());
			reportMap.put("FROM_AMOUNT", fileRequest.getFromAmount());
			reportMap.put("TO_AMOUNT", fileRequest.getToAmount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);

			throw new NTBWarningException("err.txn.ExceedDailyLimit");
		}
		billPaymentService.approveBatchPayment(id);
		return true;
	}

	public boolean reject(String txnType, String id, CibAction bean) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
		
		billPaymentService.rejectBatchPayment(id);
		return true;
	}

	public String viewDetail(String txnType, String id, CibAction bean) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
		RequestService requestService = (RequestService) appContext.getBean("RequestService");

		String viewPageUrl = "/WEB-INF/pages/txn/bill_payment/template_batch_payment_approval_view.jsp";
		Map resultData = bean.getResultData();
		
		FileRequest fileRequest = requestService.viewFileRequest(id);
		List detailList = billPaymentService.listRecordByBatchId(id);
		detailList = bean.convertPojoList2MapList(detailList);

		// ��֯assignuser_tag���
		Map assignuser = new HashMap();
		BigDecimal amountMopEq = new BigDecimal(fileRequest.getFromAmount().toString());
		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "currency");
		assignuser.put("amountField", "transferAmount");
		assignuser.put("amountMopEqField", "amountMopEq");
		assignuser.put("txnType", txnType);
		assignuser.put("currency", "MOP");
		assignuser.put("amountMopEq", amountMopEq);
		// ������ݵ�assignuser_tag
		resultData.putAll(assignuser);
		
		resultData.put("ptList2Jsp", detailList);
		resultData.put("fromAccount", fileRequest.getFromAccount());
		resultData.put("fromCurrency", fileRequest.getFromCurrency());
		resultData.put("transferAmount", fileRequest.getToAmount());

		bean.setResultData(resultData);
		return viewPageUrl;
	}

	public boolean cancel(String txnType, String id, CibAction bean) throws NTBException {
		return reject(txnType, id, bean);
	}

    private void checkCutoffTimeAndSetMsg(FileRequest fileRequest) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext.getBean("CutOffTimeService");

		// check value date cut-off time
        if (corpAccountService.isSavingAccount(fileRequest.getFromAccount(),fileRequest.getCurrency())){
			setMessage(cutOffTimeService.check("59SC", "", fileRequest.getToCurrency()));
        } else {
			setMessage(cutOffTimeService.check("59CC", "", fileRequest.getToCurrency()));
        }
    }

	public void listHistoryLoad() throws NTBException {
		// ���ÿյ� ResultData �����ʾ���
		setResultData(new HashMap(1));
		this.clearUsrSessionDataValue();
	}
    
    public void listHistory() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
		CorporationService corpService = (CorporationService) appContext.getBean("CorporationService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");

		CorpUser corpUser = (CorpUser) this.getUser();

		String date_range = Utils.null2EmptyWithTrim(this.getParameter("date_range"));
		String dateFrom = Utils.null2EmptyWithTrim(this.getParameter("dateFrom"));
		String dateTo = Utils.null2EmptyWithTrim(this.getParameter("dateTo"));
		String fromAccount = Utils.null2EmptyWithTrim(this.getParameter("fromAccount"));

		List historyList = billPaymentService.listBatchPaymentHistory(
				corpUser.getCorpId(), corpUser.getUserId(), fromAccount, dateFrom, dateTo);
		historyList = this.convertPojoList2MapList(historyList);

		Map tmpMap = null;
		Corporation corp = corpService.view(corpUser.getCorpId());
		for (int i=0; i<historyList.size(); i++) {
			tmpMap = (Map) historyList.get(i);
			// set change flag
			if ("Y".equals(corp.getAllowApproverSelection())) {
				if (Constants.STATUS_PENDING_APPROVAL.equals(tmpMap.get("status").toString())) {
					if (flowEngineService.checkApproveComplete(
							Constants.TXN_SUBTYPE_BATCH_PAYMENT,
							tmpMap.get("batchId").toString())) {
						tmpMap.put("changeFlag", "N");
					} else {
						tmpMap.put("changeFlag", "Y");
						// set txnType for changing approver
						tmpMap.put("txnType", Constants.TXN_SUBTYPE_BATCH_PAYMENT);
					}
				}
			}
		}

		Map resultData = new HashMap();
		resultData.put("date_range", date_range);
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("fromAccount", fromAccount);
		resultData.put("historyList", historyList);
		setResultData(resultData);
    }
    
    public void viewHistoryDetail() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		BillPaymentService billPaymentService = (BillPaymentService) appContext.getBean("BillPaymentService");
		RequestService requestService = (RequestService) appContext.getBean("RequestService");

		String date_range = Utils.null2EmptyWithTrim(this.getParameter("date_range"));
		String dateFrom = Utils.null2EmptyWithTrim(this.getParameter("dateFrom"));
		String dateTo = Utils.null2EmptyWithTrim(this.getParameter("dateTo"));
		String fromAccount = Utils.null2EmptyWithTrim(this.getParameter("fromAccount"));
		
    	String batchId = this.getParameter("batchId");
    	FileRequest fileRequest = requestService.viewFileRequest(batchId);
    	List detailList = billPaymentService.listRecordByBatchId(batchId);
    	detailList = this.convertPojoList2MapList(detailList);
    	
    	Map resultData = new HashMap();
		resultData.put("fromAccount2", fileRequest.getFromAccount().trim());
		resultData.put("fromCurrency", fileRequest.getFromCurrency());
		resultData.put("transferAmount", fileRequest.getToAmount());
		resultData.put("date_range", date_range);
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("fromAccount", fromAccount);
    	resultData.put("detailList", detailList);
    	this.setResultData(resultData);
    }
    
    /**
     * if true return,that means it need special processing.
     * @param merchant
     * @return
     * @throws NTBException
     */
    public boolean sepcialMerchant(String merchant) throws NTBException {
    	
    	if (merchant.equals("CEM")||merchant.equals("SAAM")||merchant.equals("CTM")) {
			return true;
		} 
    	return false;
    	
    }
}
