package app.cib.action.srv;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import app.cib.bo.bnk.BankUser;
import app.cib.bo.flow.FlowProcess;
import app.cib.bo.srv.ChequeBookRequest;
import app.cib.bo.srv.TxnPrompt;
import app.cib.bo.sys.AbstractCorpUser;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.AutopayAuthorization;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.dao.srv.TransferPromptDao;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.srv.ChequeBookRequestService;
import app.cib.service.srv.RequestService;
import app.cib.service.srv.TransferPromptService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.MailService;
import app.cib.service.txn.TransferService;
import app.cib.util.Constants;
import app.cib.util.otp.SMSOTPObject;
import app.cib.util.otp.SMSOTPUtil;
import app.cib.util.otp.SMSReturnObject;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Encryption;
import com.neturbo.set.utils.KeyNameUtils;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

public class ChequeBookRequestAction extends CibAction implements Approvable{

	public void addLoad() throws NTBException {
		Map resultData = new HashMap() ;
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccountCheckBook(corpUser.getCorpId()));
		
		//add by lzg 20191022
		TransferPromptService transferPromptService = (TransferPromptService) Config.getAppContext().getBean("TransferPromptService");
		TxnPrompt txnPrompt = new TxnPrompt();
		try{
			txnPrompt = transferPromptService.loadByTxnType("6",TransferPromptDao.STATUS_NORMAL);
			if(txnPrompt == null){
				throw new NTBException("DBError");
			}
		}catch (Exception e) {
			throw new NTBException("DBError");
		}
		String descriptionE = txnPrompt.getDescription("E");
		String descriptionC = txnPrompt.getDescription("C");
		descriptionE =transferPromptService.format(descriptionE);
		descriptionC = transferPromptService.format(descriptionC);
		resultData.put("descriptionE", descriptionE);
		resultData.put("descriptionC", descriptionC);
		//add by lzg end
		
		setResultData(resultData);
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");
		setMessage(RBFactory.getInstance("app.cib.resource.srv.check_request",lang.toString()).getString("WARNING"));
	}
	public void addCancel() throws NTBException {
		Map resultData = this.getResultData();
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccountCheckBook(corpUser.getCorpId()));
		
		
		/* Add by long_zg 2019-05-31 UAT6-464 COB：COB：申請支票報錯，交易記錄也不見了 begin */
		ChequeBookRequest bookRequest = (ChequeBookRequest)this.getUsrSessionDataValue("chequeBookRequest") ;
		KeyNameUtils.convertPojo2Map(bookRequest, resultData) ;
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");
		setMessage(RBFactory.getInstance("app.cib.resource.srv.check_request",lang.toString()).getString("WARNING"));
		setResultData(resultData);
	}
	
	public void add() throws NTBException {
		Map resultData = new HashMap() ;
		
		
		CorpUser user = (CorpUser) this.getUser();
		String corpId = user.getCorpId();
		
		//add by lzg 20190820
		String corpType = user.getCorporation().getCorpType();
		String checkFlag = user.getCorporation().getAuthenticationMode();
		resultData.put("corpType", corpType);
		resultData.put("checkFlag", checkFlag);
		resultData.put("operationType", "send");
		resultData.put("showMobileNo", user.getMobile());
		resultData.put("txnType", Constants.TXN_SUBTYPE_CHEQUE_BOOK_REQUEST);
		
		//add by lzg end
		
		setResultData(resultData) ;
		
		ApplicationContext appContext = Config.getAppContext();
		
		ChequeBookRequest bookRequest = new ChequeBookRequest() ;
		KeyNameUtils.convertMap2Pojo(this.getParameters(), bookRequest) ;
		String transNo = CibIdGenerator.getRefNoForTransaction();
		bookRequest.setTransNo(transNo) ;
		bookRequest.setUserId(this.getUser().getUserId()) ;
		bookRequest.setRequestTime(new Date()) ;
		bookRequest.setCorpId(corpId);
		bookRequest.setAuthStatus(Constants.AUTH_STATUS_SUBMITED) ;
		bookRequest.setStatus(Constants.STATUS_PENDING_APPROVAL) ;
		this.setUsrSessionDataValue("chequeBookRequest", bookRequest) ;
		KeyNameUtils.convertPojo2Map(bookRequest, resultData) ;
		//add by linrui for mul-ccy
		//String accountCcy = Utils.null2EmptyWithTrim(this.getParameter("AccountCcy"));
		
		
		/* Add by long_zg 2019-05-31 UAT6-464 COB：COB：申請支票報錯，交易記錄也不見了 begin */
		String currency = bookRequest.getPayCurrency() ;
		if(!"HKD".equals(currency) && !"MOP".equals(currency)){
			resultData.putAll(this.getParameters()) ;
			
			TransferService transferService = (TransferService) appContext.getBean("TransferService");
			CorpUser corpUser = (CorpUser) this.getUser();
			resultData.put("transferuser", transferService.loadAccountCheckBook(corpUser.getCorpId()));
			
			throw new NTBException("err.chequeBookRequest.notAllowCurrency") ;
			
		}
		/* Add by long_zg 2019-05-31 UAT6-464 COB：COB：申請支票報錯，交易記錄也不見了 end */
		
		
		CorpAccountService corpAccountService = (CorpAccountService) appContext
		.getBean("CorpAccountService");
		//String payCurrency = corpAccountService.getCurrency(bookRequest.getAccountNo(),true);
		//String payCurrency = accountCcy;
		//bookRequest.setPayCurrency(payCurrency) ;
		//add by linrui for pickup or local mail 20180324
		Map parameterMap = this.getParameters();		
		bookRequest.setPickupType(parameterMap.get("Pickuptype").toString());//add by linrui 20190527
		if("S".equalsIgnoreCase(Utils.null2EmptyWithTrim(parameterMap.get("Pickuptype").toString()))){
		  if(""==Utils.null2EmptyWithTrim(parameterMap.get("mailName1").toString())||
				  ""==Utils.null2EmptyWithTrim(parameterMap.get("mailAdress1").toString())){
			throw new NTBException("err.chebookrequest.pickupNameAdr");
		  }
		  else{
			  bookRequest.setPackageName(
					    Utils.null2EmptyWithTrim(parameterMap.get("mailName1").toString())+
						Utils.null2EmptyWithTrim(parameterMap.get("mailName2").toString()));
			  bookRequest.setPackageAdress(
						Utils.null2EmptyWithTrim(parameterMap.get("mailAdress1").toString())+
						Utils.null2EmptyWithTrim(parameterMap.get("mailAdress2").toString())+
						Utils.null2EmptyWithTrim(parameterMap.get("mailAdress3").toString()));
			  resultData.put("Pickuptype", parameterMap.get("Pickuptype").toString());			  
			  resultData.put("mailName1", parameterMap.get("mailName1").toString());
			  resultData.put("mailName2", parameterMap.get("mailName2").toString());
			  resultData.put("mailAdress1", parameterMap.get("mailAdress1").toString());
			  resultData.put("mailAdress2", parameterMap.get("mailAdress2").toString());
			  resultData.put("mailAdress3", parameterMap.get("mailAdress3").toString());
		  }
		}else{
			resultData.put("Pickuptype", parameterMap.get("Pickuptype").toString());
			bookRequest.setPackageName(" ");
			bookRequest.setPackageAdress(" ");
		}
		//end
		Map assignuser = new HashMap();
		CorpUser corpUser = (CorpUser) getUser();

//		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
//				.getBean("ExchangeRatesService");
//		BigDecimal amountMopEq = exRatesService.getEquivalent(corpUser
//				.getCorpId(), payCurrency, "MOP", new BigDecimal(
//				autopayAuthorization.getPaymentLimit()), null, 2);
		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "payCurrency");
		assignuser.put("payCurrency", bookRequest.getPayCurrency()/*payCurrency*/);
		assignuser.put("amountField", "paymentLimit");
		assignuser.put("paymentLimit", 0.0);
		assignuser.put("amountMopEqField", "amountMopEq");
		assignuser.put("txnType", Constants.TXN_SUBTYPE_CHEQUE_BOOK_REQUEST);
		assignuser.put("amountMopEq", new BigDecimal(0));
		resultData.putAll(assignuser);

		this.setUsrSessionDataValue("assignuser", assignuser);
		this.setUsrSessionDataValue("payCurrency", bookRequest.getPayCurrency()/*payCurrency*/);
		setResultData(resultData);
	}
	
	public void confirm() throws NTBException {
		Map resultData = new HashMap() ;
		
		String corpType = getParameter("corpType");
		resultData.put("corpType", corpType);
		
		ApplicationContext appContext = Config.getAppContext();
		FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");
		MailService mailService = (MailService) appContext.getBean("MailService");
		
		ChequeBookRequest bookRequest = (ChequeBookRequest)this.getUsrSessionDataValue("chequeBookRequest") ;
		
		CorpUser corpUser = (CorpUser) this.getUser();

		Map assignuser = (Map) this.getUsrSessionDataValue("assignuser");
		String txnType = (String) assignuser.get("txnType");
		BigDecimal amountMopEq = (BigDecimal) assignuser.get("amountMopEq");
		String payCurrency = (String) this
		.getUsrSessionDataValue("payCurrency");
		
		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));
		
		String processId = flowEngineService.startProcess(txnType,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				ChequeBookRequestAction.class, "MOP", new Double(
						0).doubleValue(),
				payCurrency, new Double(0)
						.doubleValue(), amountMopEq.doubleValue(),
						bookRequest.getTransNo(), "", getUser(), assignedUser/*null mod by linrui 20190618*/,
				((CorpUser) getUser()).getCorporation().getAllowExecutor(),
				FlowEngineService.RULE_FLAG_TO);
		
		ChequeBookRequestService bookRequestService = (ChequeBookRequestService) appContext.getBean("chequeBookRequestService") ;

		this.convertPojo2Map(bookRequest, resultData);
		Map parameterMap = this.getParameters();		
		if("S".equalsIgnoreCase(Utils.null2EmptyWithTrim(parameterMap.get("Pickuptype").toString()))){		  		  
			  resultData.put("Pickuptype", parameterMap.get("Pickuptype").toString());			  
			  resultData.put("mailName1", parameterMap.get("mailN1").toString());
			  resultData.put("mailName2", parameterMap.get("mailN2").toString());
			  resultData.put("mailAdress1", parameterMap.get("mailAdr1").toString());
			  resultData.put("mailAdress2", parameterMap.get("mailAdr2").toString());
			  resultData.put("mailAdress3", parameterMap.get("mailAdr3").toString());		  
		}else{
			resultData.put("Pickuptype", parameterMap.get("Pickuptype").toString());
		}
		//end
		resultData.putAll(assignuser);
		
		
		//add by lzg 20190820
		if("3".equals(corpType)){
			List processList = flowEngineService.loadProcess(processId);
			FlowProcess currentFlowProcess = (FlowProcess) processList.get(0);
			flowEngineService.cancelProcess(processId, getUser());
			String checkFlag = getParameter("checkFlag");
			if("C".equals(checkFlag)){
				String otp = getParameter("otp");
				String smsFlowNo = getParameter("smsFlowNo");
				SMSOTPObject otpObject = SMSOTPUtil.getOtpObject(smsFlowNo) ;
				SMSReturnObject returnObject = new SMSReturnObject();
				try{
	            	SMSOTPUtil.check(returnObject, smsFlowNo, otp, "N", "E") ;
	            }catch (NTBException e) {
	            	Log.info("OTP Error");
	            	returnObject.setErrorFlag("Y") ;
					returnObject.setReturnErr(e.getErrorCode()) ;
				}
	            if(!returnObject.getErrorFlag().equals("N")){
					Log.info("One time password error") ;
					resultData.put("smsFlowNo", smsFlowNo);
					setResultData(resultData);
					throw new NTBException(returnObject.getReturnErr());
	    		}
			}else if("S".equals(checkFlag)){
				String securityCode = getParameter("showSecurityCode");
				checkSecurityCode(securityCode);
			}
			try {
				bookRequestService.add(bookRequest) ;
			} catch (Exception e) {
				flowEngineService.cancelProcess(processId, getUser());
				Log.error("err.txn.TranscationFaily", e);
				if (e instanceof NTBException) {
					throw (NTBException) e;
				} else {
					throw new NTBException("err.txn.TranscationFaily");
				}
			}
			try{
				approve(currentFlowProcess.getTxnType(), currentFlowProcess.getTransNo(), this);
			}catch (NTBHostException e) {
				bookRequestService.removeCheque("cheque_book_request", currentFlowProcess.getTransNo());
				throw new NTBHostException(e.getErrorArray());
			}catch (NTBException e) {
				bookRequestService.removeCheque("cheque_book_request", currentFlowProcess.getTransNo());
				throw new NTBException(e.getErrorCode());
			}
		}
		if(!"3".equals(corpType)){
			try {
				bookRequestService.add(bookRequest) ;
			} catch (Exception e) {
				flowEngineService.cancelProcess(processId, getUser());
				Log.error("err.txn.TranscationFaily", e);
				if (e instanceof NTBException) {
					throw (NTBException) e;
				} else {
					throw new NTBException("err.txn.TranscationFaily");
				}
			}
		}
		//add by lzg end
		
		
	}
	
	private void checkSecurityCode(String secCode) throws NTBException {
    	if(this.getUser() instanceof BankUser){
    		return;
    	}
    	if(secCode == null){
    		throw new NTBException("err.sys.getSecurityCodeError");
    	}
    	
    	CorpUser corpUser = (CorpUser) this.getUser();
        String encryptedCode = Encryption.digest(corpUser.getUserId() + secCode, "MD5");
        String savedCode = corpUser.getSecurityCode();
        
        if (Constants.ROLE_APPROVER.equals(((AbstractCorpUser) corpUser).getRoleId()) &&
                ((CorpUser)corpUser).getCorporation().getAuthenticationMode().equals(Constants.AUTHENTICATION_SECURITY_CODE)) {
            
            if (savedCode == null) {
                throw new NTBException("err.sys.SecurityCodeIsNull");
            }
            if ("R".equals(savedCode)) {
                throw new NTBException("err.sys.SecurityCodeResetError");
            }        
            if (!savedCode.equals(encryptedCode)) {
                throw new NTBException("err.sys.SecurityCodeError");
            }
        }
    }
	
	
	public boolean approve(String txnType, String id, CibAction bean)
			throws NTBException {
		if (null != txnType) {
			CorpUser user = (CorpUser)bean.getUser();
			ApplicationContext appContext = Config.getAppContext();
			ChequeBookRequestService bookRequestService = (ChequeBookRequestService) appContext.getBean("chequeBookRequestService") ;
			ChequeBookRequest bookRequest = bookRequestService.load(id) ;
			bookRequestService.approveChequeBookRequest(bookRequest,user) ;
			
			Map resultData = bean.getResultData();
			this.convertPojo2Map(bookRequest, resultData);
			setResultData(resultData);
			
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
		ChequeBookRequestService bookRequestService = (ChequeBookRequestService) appContext.getBean("chequeBookRequestService") ;
		if (txnType != null) {
			ChequeBookRequest bookRequest = bookRequestService.load(id) ;
			bookRequestService.rejectChequeBookRequest(bookRequest) ;
			return true;
		} else {
			return false;
		}
	}

	public String viewDetail(String txnType, String id, CibAction bean)
			throws NTBException {
		Map resultData = bean.getResultData() ;
		ApplicationContext appContext = Config.getAppContext();
		ChequeBookRequestService bookRequestService = (ChequeBookRequestService) appContext.getBean("chequeBookRequestService") ;
		ChequeBookRequest bookRequest = bookRequestService.load(id) ;
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		String payCurrency = corpAccountService.getCurrency(bookRequest.getAccountNo(),true);
		bookRequest.setPayCurrency(payCurrency) ;
		KeyNameUtils.convertPojo2Map(bookRequest, resultData) ;
		
		Map assignuser = new HashMap();
		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "payCurrency");
		assignuser.put("payCurrency", payCurrency);
		assignuser.put("amountField", "paymentLimit");
		assignuser.put("paymentLimit", 0.0);
		assignuser.put("amountMopEqField", "amountMopEq");
		assignuser.put("txnType", Constants.TXN_SUBTYPE_CHEQUE_BOOK_REQUEST);
		assignuser.put("amountMopEq", new BigDecimal(0));
		resultData.putAll(assignuser);
		bean.setResultData(resultData) ;
		String viewPageUrl = "/WEB-INF/pages/srv/request/chequeBookRequest_approve_view.jsp";
		return viewPageUrl;
	}
	
	//add by lzg 20190722
	public void listLoad() throws NTBException {
		setResultData(new HashMap(1));
	}
	
	public void listHistory() throws NTBException {
		setResultData(new HashMap(1));
		CorpUser user = (CorpUser) this.getUser();
		String corpId = user.getCorpId();
		String groupId= user.getGroupId();
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext
		.getBean("RequestService");

		Date dateFrom = null;
		Date dateTo = null;
		String bookNo = null;

		bookNo = Utils.null2EmptyWithTrim(getParameter("bookNo"));

		String date_range = Utils
				.null2EmptyWithTrim(getParameter("date_range"));
		if (date_range.equals("range")) {

			if (!Utils.null2EmptyWithTrim(getParameter("dateFrom"))
					.equals("")) {

				dateFrom = DateTime.getDateFromStr(Utils
						.null2EmptyWithTrim(getParameter("dateFrom")), true);
				
			}
			if (!Utils.null2EmptyWithTrim(getParameter("dateTo")).equals("")) {

				dateTo = DateTime.getDateFromStr(Utils
						.null2EmptyWithTrim(getParameter("dateTo")), false);
				
			}
		}

		List chequeBookRequestList = requestService.listReqChequeForRequestHis(dateFrom, dateTo, corpId, groupId);
		chequeBookRequestList=KeyNameUtils.listDash2CaseDiff(chequeBookRequestList);

		Map resultData = new HashMap();

		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("chequeBookRequestList", chequeBookRequestList);
		resultData.put("bookNo", bookNo);
		resultData.put("date_range", date_range);
		
		setResultData(resultData);
	}
	//add by lzg end

}
