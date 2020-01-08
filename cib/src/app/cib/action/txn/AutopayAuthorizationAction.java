package app.cib.action.txn;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.ApplicationContext;

import app.cib.action.srv.StopChequeAction;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.Autopay;
import app.cib.bo.txn.AutopayAuthorization;
import app.cib.bo.txn.AutopayAuthorizationHis;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.MailService;
import app.cib.service.txn.AutopayAuthorizationService;
import app.cib.service.txn.TransferLimitService;
import app.cib.util.Constants;
import app.cib.util.TransAmountFormate;
import app.cib.util.UploadReporter;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBWarningException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.KeyNameUtils;
import com.neturbo.set.utils.Utils;

/**
 * add by long_zg 2014-01-08 for CR192 bob batch
 * 
 * @author long_zg
 * 
 */
public class AutopayAuthorizationAction extends CibAction implements Approvable {

	private final static String CARD_MERCHANT_CODE = Config
			.getProperty("app.autopay.creditcard.code");

	public void listAutopay() throws NTBException {
		CorpUser user = (CorpUser) this.getUser();
		String corpId = user.getCorpId();
		AutopayAuthorizationService autopayInstructionService = (AutopayAuthorizationService) Config
				.getAppContext().getBean("autopayAuthorizationService");
		List autopayList = autopayInstructionService.listAutopayAuthorization(corpId);
		
		autopayList = KeyNameUtils.convertPojoList2MapList(autopayList);
		Log.info("autopayList="+autopayList);
		Map resultData = new HashMap();
		resultData.put("autopayList", autopayList);
		setResultData(resultData);
	}

	public void addLoad() throws NTBException {
		Map resultData = new HashMap();

		String mode = Utils.null2Empty(this.getParameter("mode"));
		if (Constants.AUTOPAYMENT_MODE_EDIT.equals(mode)) {
			String transNo = this.getParameter("transNo");
			String corpId = transNo.split("\\|")[0];
			String apsCode = transNo.split("\\|")[1];
			String contractNo = transNo.split("\\|")[2];
			AutopayAuthorization autopayAuthorization = new AutopayAuthorization();
			ApplicationContext appContext = Config.getAppContext();
			
			AutopayAuthorizationService autopayInstructionService = (AutopayAuthorizationService) appContext
					.getBean("autopayAuthorizationService");
			
			Autopay autopay = autopayInstructionService
					.loadAutopay(corpId, apsCode, contractNo);
			
			AutopayAuthorization oldAutopayAuthorization = autopayInstructionService.loadAutopayAuthorization(null,corpId, apsCode, contractNo);
			autopay.setTransNo(oldAutopayAuthorization.getTransNo());
			try {
				BeanUtils.copyProperties(autopayAuthorization, autopay);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.error("autopay to autopayAuthorization fail",e);
			} 
			
			if (null == autopayAuthorization ) {
				throw new NTBException(
						"err.autopay.notExistAutopayAuthorization");
			}
			if ("".equals(KeyNameUtils.null2Empty(autopayAuthorization
					.getPayOption()))) {
				if (Double.valueOf(Constants.AUTOPAYMENT_NO_LIMIT.toString()) == autopayAuthorization
						.getPaymentLimit()) {
					autopayAuthorization
							.setPayOption(Constants.AUTOPAYMENT_PAYMENT_FULL);
					autopayAuthorization.setPaymentLimit(0);
				} else {
					autopayAuthorization
							.setPayOption(Constants.AUTOPAYMENT_PAYMENT_INPUT);
				}
			}

			KeyNameUtils.convertPojo2Map(autopayAuthorization, resultData);
			resultData.put("payOption", autopayAuthorization.getPayOption());
		}
		if(Constants.AUTOPAYMENT_MODE_ADD.equals(mode)){
			resultData.put("payOption", Constants.AUTOPAYMENT_PAYMENT_FULL);
		}
		resultData.put("card_merchant", CARD_MERCHANT_CODE);
		resultData.put("mode", mode);
		
		setResultData(resultData);
	}

	public void editLoad() throws NTBException {
		Map resultData = new HashMap();
		resultData.put("title", "edit");

		String transNo = this.getParameter("transNo");
		String corpId = transNo.split("\\|")[0];
		String apsCode = transNo.split("\\|")[1];
		String contractNo = transNo.split("\\|")[2];
		AutopayAuthorization autopayAuthorization = new AutopayAuthorization();
		ApplicationContext appContext = Config.getAppContext();
		AutopayAuthorizationService autopayInstructionService = (AutopayAuthorizationService) appContext
				.getBean("autopayAuthorizationService");
		Autopay autopay = autopayInstructionService
				.loadAutopay(apsCode, contractNo, corpId);
		AutopayAuthorization oldAutopayAuthorization = autopayInstructionService.loadAutopayAuthorization(null, apsCode, contractNo, corpId);
		if (oldAutopayAuthorization!=null){
			if(oldAutopayAuthorization.getAuthStatus().equals(Constants.STATUS_PENDING_APPROVAL)){
				throw new NTBException("err.autopay.existAutopayPending");
			}
			autopay.setTransNo(oldAutopayAuthorization.getTransNo());
		}else{
			autopay.setTransNo(CibIdGenerator.getRefNoForTransaction());
		}
		try {
			BeanUtils.copyProperties(autopayAuthorization, autopay);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.error("autopay to autopayAuthorization fail",e);
		} 
		if (null == autopayAuthorization) {
			throw new NTBException("err.autopay.notExistAutopayAuthorization");
		}
		if ("".equals(KeyNameUtils.null2Empty(autopayAuthorization
				.getPayOption()))) {
			if (Double.valueOf(Constants.AUTOPAYMENT_NO_LIMIT.toString()) == autopayAuthorization
					.getPaymentLimit()) {
				autopayAuthorization
						.setPayOption(Constants.AUTOPAYMENT_PAYMENT_FULL);
				autopayAuthorization.setPaymentLimit(0);
			} else {
				autopayAuthorization
						.setPayOption(Constants.AUTOPAYMENT_PAYMENT_INPUT);
			}
		}

		KeyNameUtils.convertPojo2Map(autopayAuthorization, resultData);
		// confirm by Li_zd for CR_192 20160921
		resultData.put("card_merchant", CARD_MERCHANT_CODE);
		resultData.put("mode", Constants.AUTOPAYMENT_MODE_EDIT);
		resultData.put("payOption", autopayAuthorization.getPayOption());
		// Add by Li_zd for CR_192
		if ("F".equals(resultData.get("payOption")) || "M".equals(resultData.get("payOption"))) {
			resultData.put("paymentLimit", "");
		}
		setResultData(resultData);
	}

	public void edit() throws NTBException {
		HashMap resultData = new HashMap();

//		String payOption = this.getParameter("payOption");
//        if (payOption.equals("P") || payOption.equals("")) {	
//        	String paymentLimit =this.getParameter("paymentLimit");
//            if(paymentLimit.indexOf(".")>=0){
//            	throw new NTBException("ERM2106");
//            }
//		}
		CorpUser user = (CorpUser) this.getUser();
		String corpId = user.getCorpId();
		
		String contractNo = this.getParameter("contractNo") ;
		
		AutopayAuthorization autopayAuthorization = new AutopayAuthorization();
		KeyNameUtils
				.convertMap2Pojo(this.getParameters(), autopayAuthorization);

		ApplicationContext appContext = Config.getAppContext();
		AutopayAuthorizationService autopayInstructionService = (AutopayAuthorizationService) appContext
				.getBean("autopayAuthorizationService");

		Autopay oldAutopay = autopayInstructionService
				.loadAutopay(autopayAuthorization
						.getApsCode(), autopayAuthorization.getContractNo(), corpId);
		String mode = this.getParameter("mode");
		autopayAuthorization.setUserId(this.getUser().getUserId());
		autopayAuthorization.setStatus(Constants.AUTH_STATUS_SUBMITED);
		autopayAuthorization.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
		autopayAuthorization.setRequestTime(new Date());
		autopayAuthorization.setCorpId(corpId);
		
		//*********
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");
		Double paymentLimit  = TransAmountFormate.formateAmount(this.getParameter("paymentLimit"),lang);
		autopayAuthorization.setPaymentLimit(paymentLimit);

		Map assignuser = new HashMap();
		if (Constants.AUTOPAYMENT_PAYMENT_FULL.equals(autopayAuthorization
				.getPayOption())) {
			autopayAuthorization.setPaymentLimit(Double
					.valueOf(Constants.AUTOPAYMENT_NO_LIMIT.toString()));
		}
		
		if (Constants.AUTOPAYMENT_MODE_ADD.equals(mode)) {
			
			AutopayAuthorizationHis autoHis = autopayInstructionService.loadHisbyKey(corpId, autopayAuthorization.getApsCode(), contractNo);
			//check unique
			if (oldAutopay!=null || (autoHis!=null && Constants.AUTH_STATUS_SUBMITED.equals(autoHis.getAuthStatus())) ){
				
				if (autopayInstructionService.getMerchant(autopayAuthorization.getApsCode())){
					resultData.put("contractNo",contractNo);
					resultData.put("title", "add");
					resultData.put("payOption", this.getParameter("payOption"));
					
					this.setResultData(resultData);
					throw new NTBException("err.autopay.existAutopayAuthorization");
				}
			}
			
			String transNo = CibIdGenerator.getRefNoForTransaction();
			autopayAuthorization.setTransNo(transNo);
			resultData.put("title", "add");
			assignuser.put("txnType", Constants.TXN_SUBTYPE_AUTOPAYMENT_ADD);
		} else if (Constants.AUTOPAYMENT_MODE_EDIT.equals(mode)) {
//			if (null != oldAutopayAuthorization
//			&& !autopayAuthorization.getTransNo().equals(
//					oldAutopayAuthorization.getTransNo())) {
//		throw new NTBException(
//				"err.autopay.theUnionOfPaymentTypeAndContractNumberOrCreditCardNo.AreadyExist");
//	}
			resultData.put("title", "edit");
			assignuser.put("txnType", Constants.TXN_SUBTYPE_AUTOPAYMENT_EDIT);
			
		} else {
			throw new NTBException("err.autopay.modeException");
		}

		KeyNameUtils.convertPojo2Map(autopayAuthorization, resultData);

		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		String payCurrency = corpAccountService
				.getCurrency(autopayAuthorization.getPayAcct(),true);

		autopayAuthorization.setPayCurrency(payCurrency);

		this.setUsrSessionDataValue("autopayAuthorization",
				autopayAuthorization);

		
		CorpUser corpUser = (CorpUser) getUser();

		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		BigDecimal amountMopEq = exRatesService.getEquivalent(corpUser
				.getCorpId(), payCurrency, "MOP", new BigDecimal(
				autopayAuthorization.getPaymentLimit()), null, 2);

		// ��⽻�׽��(���������ת��ΪMOP)�Ƿ񳬳�ÿ�ս����ۼ�����
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		if (Constants.AUTOPAYMENT_PAYMENT_INPUT.equals(autopayAuthorization.getPayOption()) &&
				!transferLimitService.checkLimitQuotaByCorpId1(corpUser.getCorpId(),
				Constants.TXN_TYPE_AUTO_PAY_AUTHORIZATION, autopayAuthorization.getPaymentLimit(),
				autopayAuthorization.getPaymentLimit())) {
			// write limit report
			Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", autopayAuthorization.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_AUTO_PAY_AUTHORIZATION);
			reportMap.put("TRANS_NO", autopayAuthorization.getTransNo());
			reportMap.put("PAY_CURRENCY", autopayAuthorization.getPayCurrency());
			reportMap.put("PAYMENT_LIMIT", autopayAuthorization.getPaymentLimit());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
			throw new NTBWarningException("err.txn.ExceedDailyLimit");
		}
		
		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "payCurrency");
		assignuser.put("payCurrency", payCurrency);
		assignuser.put("amountField", "paymentLimit");
		assignuser.put("amountMopEqField", "amountMopEq");
		//assignuser.put("txnType", Constants.TXN_SUBTYPE_AUTOPAYMENT);
		assignuser.put("amountMopEq", amountMopEq);
		assignuser.put("payOption", this.getParameter("payOption"));
		// Add by Li_zd for CR_192
		resultData.put("labelContact", CARD_MERCHANT_CODE.equals(resultData.get("apsCode"))?"CCN":"CN");
		resultData.putAll(assignuser);

		this.setUsrSessionDataValue("assignuser", assignuser);
		setResultData(resultData);

	}

	public void confirm() throws NTBException {
		Map resultData = new HashMap();
		CorpUser user = (CorpUser) this.getUser();
		String corpId = user.getCorpId();
		setResultData(resultData);

		ApplicationContext appContext = Config.getAppContext();
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");
		AutopayAuthorizationService autopayInstructionService = (AutopayAuthorizationService) appContext
				.getBean("autopayAuthorizationService");

		CorpUser corpUser = (CorpUser) this.getUser();

		Map assignuser = (Map) this.getUsrSessionDataValue("assignuser");
		String txnType = (String) assignuser.get("txnType");
		BigDecimal amountMopEq = (BigDecimal) assignuser.get("amountMopEq");

		AutopayAuthorization autopayAuthorization = (AutopayAuthorization) this
				.getUsrSessionDataValue("autopayAuthorization");
		String payCurrency = (String) this
				.getUsrSessionDataValue("payCurrency");

		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));
		
		String seqNo = CibIdGenerator.getIdForOperation("AUTOPAY_AUTHORIZATION_HIS");
		
		String processId = flowEngineService.startProcess(txnType,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				AutopayAuthorizationAction.class, "MOP", new Double(
						autopayAuthorization.getPaymentLimit()).doubleValue(),
				payCurrency, new Double(autopayAuthorization.getPaymentLimit())
						.doubleValue(), amountMopEq.doubleValue(),
						seqNo, "", getUser(), null,
				((CorpUser) getUser()).getCorporation().getAllowExecutor(),
				FlowEngineService.RULE_FLAG_FROM);

		String payOption = (String)assignuser.get("payOption");
		
		try {
			if (Constants.AUTOPAYMENT_MODE_ADD.equals(autopayAuthorization
					.getMode())) {
				resultData.put("title", "add");
				
				AutopayAuthorizationHis autopayAuthorizationHis = new AutopayAuthorizationHis();
				autopayAuthorizationHis.setTransNo(autopayAuthorization.getTransNo());
				autopayAuthorizationHis.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
				autopayAuthorizationHis.setStatus(Constants.AUTH_STATUS_SUBMITED);
				autopayAuthorizationHis.setPayOption(payOption);
				autopayAuthorizationHis.setRequestTime(new Date());
				autopayAuthorizationHis.setMode(Constants.AUTOPAYMENT_MODE_ADD);
				autopayAuthorizationHis.setUserId(autopayAuthorization.getUserId());
				autopayAuthorizationHis.setApsCode(autopayAuthorization.getApsCode());
				autopayAuthorizationHis.setContractNo(autopayAuthorization.getContractNo());
				autopayAuthorizationHis.setPaymentLimit(autopayAuthorization.getPaymentLimit());
				autopayAuthorizationHis.setPayAcct(autopayAuthorization.getPayAcct());
				autopayAuthorizationHis.setCorpId(corpId);
				autopayAuthorizationHis.setSeqNo(seqNo);
				
				BeanUtils.copyProperties(autopayAuthorization, autopayAuthorizationHis);
				
				autopayInstructionService.addAutopay(autopayAuthorization);
				autopayInstructionService.addAutopayHis(autopayAuthorizationHis);
			} else {
				
				AutopayAuthorizationHis autopayAuthorizationHis = new AutopayAuthorizationHis();
				autopayAuthorizationHis.setTransNo(autopayAuthorization.getTransNo());
				autopayAuthorizationHis.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
				autopayAuthorizationHis.setStatus(Constants.AUTH_STATUS_SUBMITED);
				autopayAuthorizationHis.setPayOption(payOption);
				autopayAuthorizationHis.setRequestTime(new Date());
				autopayAuthorizationHis.setUserId(autopayAuthorization.getUserId());
				autopayAuthorizationHis.setApsCode(autopayAuthorization.getApsCode());
				autopayAuthorizationHis.setContractNo(autopayAuthorization.getContractNo());
				autopayAuthorizationHis.setPayAcct(autopayAuthorization.getPayAcct());
				autopayAuthorizationHis.setPaymentLimit(autopayAuthorization.getPaymentLimit());
				autopayAuthorizationHis.setCorpId(corpId);
				if(Constants.AUTOPAYMENT_MODE_EDIT.equals(autopayAuthorization
					.getMode())){
					autopayAuthorizationHis.setMode(Constants.AUTOPAYMENT_MODE_EDIT);
				}else if(Constants.AUTOPAYMENT_MODE_EDIT.equals(autopayAuthorization
					.getMode())){
					autopayAuthorizationHis.setMode(Constants.AUTOPAYMENT_MODE_DELETE);
				}
				autopayAuthorizationHis.setSeqNo(seqNo);
				
				BeanUtils.copyProperties(autopayAuthorization, autopayAuthorizationHis);
				
				autopayInstructionService.addAutopayHis(autopayAuthorizationHis);
				resultData.put("title", "edit");
			}

		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("err.txn.TranscationFaily", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			} else {
				throw new NTBException("err.txn.TranscationFaily");
			}
		}

		this.convertPojo2Map(autopayAuthorization, resultData);

		resultData.putAll(assignuser);

		// send mial to approver

		Map dataMap = new HashMap();
		dataMap.put("userID", corpUser.getUserId());
		dataMap.put("userName", corpUser.getUserName());
		dataMap.put("merchant", autopayAuthorization.getApsCode());
		dataMap.put("requestTime", autopayAuthorization.getRequestTime());
		dataMap.put("transNo", autopayAuthorization.getTransNo());
		// Add by Li_zd for CR_192
		resultData.put("labelContact", CARD_MERCHANT_CODE.equals(resultData.get("apsCode"))?"CCN":"CN");
		
		/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
		/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_AUTOPAYMENT, mailUser.split(";"), dataMap);*/
		mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_AUTOPAYMENT, mailUser.split(";"),corpUser.getCorpId(), dataMap);
		/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */
		
		
		setResultData(resultData);

	}

	public void deleteLoad() throws NTBException {
		Map resultData = new HashMap();

		//String transNo = this.getParameter("transNo");

		String transNo = this.getParameter("transNo");
		String corpId = transNo.split("\\|")[0];
		String apsCode = transNo.split("\\|")[1];
		String contractNo = transNo.split("\\|")[2];
		AutopayAuthorization autopayAuthorization = new AutopayAuthorization();
		ApplicationContext appContext = Config.getAppContext();
		AutopayAuthorizationService autopayInstructionService = (AutopayAuthorizationService) appContext
				.getBean("autopayAuthorizationService");
		Autopay autopay = autopayInstructionService
				.loadAutopay(apsCode, contractNo, corpId);
		AutopayAuthorization oldAutopayAuthorization = autopayInstructionService.loadAutopayAuthorization(null, apsCode, contractNo, corpId);
		if (oldAutopayAuthorization!=null){
			autopay.setTransNo(oldAutopayAuthorization.getTransNo());
		}else{
			autopay.setTransNo(CibIdGenerator.getRefNoForTransaction());
		}
		
		try {
			BeanUtils.copyProperties(autopayAuthorization, autopay);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.error("autopay to autopayAuthorization fail",e);
		} 
		
		if (null == autopayAuthorization) {
			throw new NTBException("err.autopay.notExistAutopayAuthorization");
		}
		if ("".equals(KeyNameUtils.null2Empty(autopayAuthorization
				.getPayOption()))) {
			if (Double.valueOf(Constants.AUTOPAYMENT_NO_LIMIT.toString()) == autopayAuthorization
					.getPaymentLimit()) {
				autopayAuthorization
						.setPayOption(Constants.AUTOPAYMENT_PAYMENT_FULL);
				autopayAuthorization.setPaymentLimit(0);
			} else {
				autopayAuthorization
						.setPayOption(Constants.AUTOPAYMENT_PAYMENT_INPUT);
			}
		}

		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		String payCurrency = corpAccountService
				.getCurrency(autopayAuthorization.getPayAcct(),true);

		autopayAuthorization.setPayCurrency(payCurrency);
		autopayAuthorization.setMode(Constants.AUTOPAYMENT_MODE_DELETE);
		autopayAuthorization.setRequestTime(new Date());
		autopayAuthorization.setStatus(Constants.AUTH_STATUS_SUBMITED);
		autopayAuthorization.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
		autopayAuthorization.setUserId(getUser().getUserId());

		this.setUsrSessionDataValue("autopayAuthorization",
				autopayAuthorization);

		KeyNameUtils.convertPojo2Map(autopayAuthorization, resultData);

		Map assignuser = new HashMap();
		CorpUser corpUser = (CorpUser) getUser();

		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		BigDecimal amountMopEq = exRatesService.getEquivalent(corpUser
				.getCorpId(), payCurrency, "MOP", new BigDecimal(
				autopayAuthorization.getPaymentLimit()), null, 2);
		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "payCurrency");
		assignuser.put("payCurrency", payCurrency);
		assignuser.put("amountField", "paymentLimit");
		assignuser.put("amountMopEqField", "amountMopEq");
		assignuser.put("txnType", Constants.TXN_SUBTYPE_AUTOPAYMENT_DELETE);
		assignuser.put("amountMopEq", amountMopEq);
		assignuser.put("payOption", autopayAuthorization.getPayOption());
		resultData.putAll(assignuser);
		// Add by Li_zd for CR_192
		resultData.put("labelContact", CARD_MERCHANT_CODE.equals(resultData.get("apsCode"))?"CCN":"CN");
		
		this.setUsrSessionDataValue("assignuser", assignuser);
		setResultData(resultData);
	}

	public void delete() throws NTBException {
		Map resultData = new HashMap();
		//���ڽ����жϴ�Ϊɾ�����
		resultData.put("title", "delete");
		setResultData(resultData);

		ApplicationContext appContext = Config.getAppContext();
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");
		AutopayAuthorizationService autopayInstructionService = (AutopayAuthorizationService) appContext
				.getBean("autopayAuthorizationService");

		AutopayAuthorization autopayAuthorization = (AutopayAuthorization) this
				.getUsrSessionDataValue("autopayAuthorization");

		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));

		Map assignuser = (Map) this.getUsrSessionDataValue("assignuser");
		String txnType = (String) assignuser.get("txnType");

		String payOption = (String)assignuser.get("payOption");
		String seqNo = CibIdGenerator.getIdForOperation("AUTOPAY_AUTHORIZATION_HIS");
		
		String processId = flowEngineService.startProcess(txnType,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				AutopayAuthorizationAction.class, "MOP", new Double(
						autopayAuthorization.getPaymentLimit()).doubleValue(),
				autopayAuthorization.getPayCurrency(), new Double(
						autopayAuthorization.getPaymentLimit()).doubleValue(),
				new Double(autopayAuthorization.getPaymentLimit())
						.doubleValue(), seqNo, "",
				getUser(), assignedUser, ((CorpUser) getUser())
						.getCorporation().getAllowExecutor(),
				FlowEngineService.RULE_FLAG_FROM);

		try {
			
			AutopayAuthorizationHis autopayAuthorizationHis = new AutopayAuthorizationHis();
			autopayAuthorizationHis.setTransNo(autopayAuthorization.getTransNo());
			autopayAuthorizationHis.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			autopayAuthorizationHis.setStatus(Constants.AUTH_STATUS_SUBMITED);
			autopayAuthorizationHis.setPayOption(payOption);
			autopayAuthorizationHis.setRequestTime(new Date());
			autopayAuthorizationHis.setUserId(autopayAuthorization.getUserId());
			autopayAuthorizationHis.setApsCode(autopayAuthorization.getApsCode());
			autopayAuthorizationHis.setContractNo(autopayAuthorization.getContractNo());
			autopayAuthorizationHis.setPayAcct(autopayAuthorization.getPayAcct());
			autopayAuthorizationHis.setMode(Constants.AUTOPAYMENT_MODE_DELETE);
			// Add by Li_zd for CR_192
			autopayAuthorizationHis.setPaymentLimit(autopayAuthorization.getPaymentLimit());
			autopayAuthorizationHis.setPayCurrency(autopayAuthorization.getPayCurrency());
			autopayAuthorizationHis.setCorpId(autopayAuthorization.getCorpId());
			autopayAuthorizationHis.setSeqNo(seqNo);
			
			
			BeanUtils.copyProperties(autopayAuthorization, autopayAuthorizationHis);
			
			autopayInstructionService.addAutopayHis(autopayAuthorizationHis);

		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("err.txn.TranscationFaily", e);
			if (e instanceof NTBException) {
				throw (NTBException) e;
			} else {
				throw new NTBException("err.txn.TranscationFaily");
			}
		}

		KeyNameUtils.convertPojo2Map(autopayAuthorization, resultData) ;
		// Add by Li_zd for CR_192
		resultData.put("labelContact", CARD_MERCHANT_CODE.equals(resultData.get("apsCode"))?"CCN":"CN");
	}

	public boolean approve(String txnType, String id, CibAction bean)
			throws NTBException {
		CorpUser user = (CorpUser) bean.getUser();
		if (null != txnType) {
			ApplicationContext appContext = Config.getAppContext();
			AutopayAuthorizationService autopayInstructionService = (AutopayAuthorizationService) appContext
					.getBean("autopayAuthorizationService");
			
			AutopayAuthorizationHis autopayAuthorizationHis = autopayInstructionService.loadAutopayAuthorizationHis(id);
			
			if (txnType.equals(Constants.TXN_SUBTYPE_AUTOPAYMENT_ADD)) {
				Autopay oldAutopay = autopayInstructionService.loadAutopay(autopayAuthorizationHis.getApsCode(), autopayAuthorizationHis.getContractNo(), autopayAuthorizationHis.getCorpId());
				
	            if (oldAutopay!=null){
					
					if (autopayInstructionService.getMerchant(autopayAuthorizationHis.getApsCode())){
						throw new NTBException("err.autopay.existAutopayAuthorization");
					}
				}
				autopayAuthorizationHis.setStatus(Constants.STATUS_NORMAL);
				autopayAuthorizationHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			} else if (txnType.equals(Constants.TXN_SUBTYPE_AUTOPAYMENT_EDIT)) {
				autopayAuthorizationHis.setStatus(Constants.STATUS_NORMAL);
				autopayAuthorizationHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			} else if (txnType.equals(Constants.TXN_SUBTYPE_AUTOPAYMENT_DELETE)) {
				autopayAuthorizationHis.setStatus(Constants.STATUS_REMOVED);
				autopayAuthorizationHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			} 
			boolean isExsitAutopayAuthorization = true;
			boolean isExsitAutopay = true;
			AutopayAuthorization authorization = autopayInstructionService.loadauthorization(autopayAuthorizationHis.getTransNo());
			Autopay autopay = autopayInstructionService.loadAutopay(autopayAuthorizationHis.getApsCode(), autopayAuthorizationHis.getContractNo(), autopayAuthorizationHis.getCorpId());
			
			if (authorization==null){
				authorization = new AutopayAuthorization();
				isExsitAutopayAuthorization=false;
			}
			if(autopay==null &&(Constants.AUTOPAYMENT_MODE_DELETE.equals(autopayAuthorizationHis.getMode()) 
					|| Constants.AUTOPAYMENT_MODE_EDIT.equals(autopayAuthorizationHis.getMode())) ){

				throw new NTBException("err.autopay.notExistAutopayAuthorization");
			}else if(autopay==null){
				autopay = new Autopay();
			}
			try {
				BeanUtils.copyProperties(authorization, autopayAuthorizationHis);
				BeanUtils.copyProperties(autopay, autopayAuthorizationHis);
			} catch (Exception e) {
                Log.error("Error copy properties", e);
                throw new NTBException("err.sys.GeneralError");
            }
			
			autopayInstructionService.approveAutopay(autopay, authorization,autopayAuthorizationHis,
					user,isExsitAutopayAuthorization);//mod by linrui for mul-language
			
			return true;
		}
		return false;
	}

	public boolean cancel(String txnType, String id, CibAction bean)
			throws NTBException {
		return reject(txnType, id, bean);
	}

	public boolean reject(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		AutopayAuthorizationService autopayInstructionService = (AutopayAuthorizationService) appContext
				.getBean("autopayAuthorizationService");
		if (txnType != null) {
			AutopayAuthorizationHis autopayAuthorizationHis = autopayInstructionService
					.loadAutopayAuthorizationHis(id);
			autopayInstructionService.rejectAutopay(autopayAuthorizationHis);
			return true;
		} else {
			return false;
		}
	}

	public String viewDetail(String txnType, String id, CibAction bean)
			throws NTBException {
		Map resultData = bean.getResultData();
		ApplicationContext appContext = Config.getAppContext();
		AutopayAuthorizationService autopayInstructionService = (AutopayAuthorizationService) appContext
				.getBean("autopayAuthorizationService");
		AutopayAuthorizationHis autopayAuthorizationHis = autopayInstructionService
				.loadAutopayAuthorizationHis(id);

		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		String payCurrency = corpAccountService.getCurrency(autopayAuthorizationHis.getPayAcct(),true);
		autopayAuthorizationHis.setPayCurrency(payCurrency);

		KeyNameUtils.convertPojo2Map(autopayAuthorizationHis, resultData);

		Map assignuser = new HashMap();
		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "currency");
		assignuser.put("amountField", "transferAmount");
		assignuser.put("amountMopEqField", "amountMopEq");
		assignuser.put("txnType", txnType);
		resultData.putAll(assignuser);
		// Add by Li_zd for CR_192
		resultData.put("labelContact", CARD_MERCHANT_CODE.equals(resultData.get("apsCode"))?"CCN":"CN");

		bean.setResultData(resultData);
		String viewPageUrl = "/WEB-INF/pages/txn/bill_payment/autopay_instruction_approve_view.jsp";
		return viewPageUrl;
	}
}
