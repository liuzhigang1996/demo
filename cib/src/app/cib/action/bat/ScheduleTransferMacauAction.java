package app.cib.action.bat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBHostException;
import com.neturbo.set.exception.NTBWarningException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bat.ScheduleTransfer;
import app.cib.bo.bat.ScheduleTransferHis;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TransferMacau;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.core.CibTransClient;
import app.cib.dao.enq.ExchangeRatesDao;
import app.cib.service.bat.SchTransferService;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.sys.ApproveRuleService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.MailService;
import app.cib.service.sys.TransAmountService;
import app.cib.service.txn.TransferLimitService;
import app.cib.service.txn.TransferService;
import app.cib.util.Constants;
import app.cib.util.RcCurrency;
import app.cib.util.TransAmountFormate;
import app.cib.util.TransferConstants;
import app.cib.util.UploadReporter;

public class ScheduleTransferMacauAction extends CibAction implements Approvable {
	
	public void list() throws NTBException {
		setResultData(new HashMap(1));
		CorpUser user = (CorpUser) this.getUser();

		ApplicationContext appContext = Config.getAppContext();
		SchTransferService schTransferService = (SchTransferService) appContext.getBean("SchTransferService");
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		
		//hjs
		List schTransferList = schTransferService.listSchTransfer(
				TransferConstants.TRANSFER_TYPE_MACAU, user.getCorpId());
		schTransferList = this.convertPojoList2MapList(schTransferList);
		String recordFlag = null;
		if (schTransferList.size() == 0) {
			recordFlag = "Y";
		}
		Map row = null;
		TransferMacau transferMacau = null;
		String status = "", authStatus = "", operation = "";
		for (int i = 0; i < schTransferList.size(); i++) {
			row = (Map) schTransferList.get(i);
			status = row.get("status").toString();
			authStatus = row.get("authStatus").toString();
			operation = row.get("operation").toString();
			transferMacau = transferService.viewInMacau(row.get("transId").toString());
			convertPojo2Map(transferMacau, row);
			row.put("status", status);
			row.put("authStatus", authStatus);
			row.put("operation", operation);

		}
		Map resultData = new HashMap();
		resultData.put("toList", schTransferList);
		resultData.put("recordFlag", recordFlag);
		setResultData(resultData);

	}

	public void addLoad() throws NTBException {
		// 閿熸枻鎷烽敓鐭┖纰夋嫹 ResultData 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸鎾呮嫹閿熸枻鎷�
//		mod by lq 20171123
//		setMessage("2 working days are required for a new schedule transfer record to take effect");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
//		setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg",lang+"").getString("warnning.payroll.ScheduleTransfer"));
		//add by linrui 20180313
		//setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg",lang+"").getString("warnning.transfer.DifferenceCcy"));
		HashMap resultData = new HashMap(1);
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		CorpUser corpUser = (CorpUser) this.getUser();
		//modified by lzg 20190708
		//resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("transferuser", transferService.loadAccountForRemittance(corpUser.getCorpId()));
		//modified by lzg end
		
		setResultData(resultData);
//		setResultData(new HashMap(1));
	}

	public void add() throws NTBException {
		// initial service
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		TransAmountService transAmountService = (TransAmountService) appContext
				.getBean("TransAmountService");

		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		
		String SELECT_CGDLOCAL = "select CGD_FLAG,BANK_NAME from HS_LOCAL_BANK_CODE where BANK_CODE=? ";
		CorpUser corpUser = (CorpUser) this.getUser();
		corpUser.setLanguage(lang);//add by linrui for mul-language
		RcCurrency rcCurrency = new RcCurrency();
		TransferMacau transferMacau = new TransferMacau();
		String CGD = "N";

		//update by heyj 20190529
		Map reqMap = new HashMap();
		reqMap.putAll(this.getParameters());
		String endDate = DateTime.formatDate(Utils.null2EmptyWithTrim(getParameter("endDate")),"dd/MM/yyyy", "yyyyMMdd");
		
		Date tomorrow = new Date();
		Date oDate = DateTime.getDateFromStr(endDate, "yyyyMMdd");
		java.util.Calendar calendar = new java.util.GregorianCalendar();
		calendar.setTime(tomorrow);
		calendar.add(calendar.DATE,1);
		tomorrow=calendar.getTime(); 
		String tomorrowStr = DateTime.formatDate(tomorrow, "yyyyMMdd");
		tomorrow = DateTime.getDateFromStr(tomorrowStr, "yyyyMMdd");
		long intervalMilli = oDate.getTime() - tomorrow.getTime();
		int diffDays =  (int) (intervalMilli / (24 * 60 * 60 * 1000));
		if (diffDays <= 0){
			 Log.error("Transfer End Date should be later than tomorrow");
		     throw new NTBException("Transfer End Date should be later than tomorrow");
		}
		
		reqMap.put("endDate", endDate);
		this.convertMap2Pojo(reqMap, transferMacau);
		//this.convertMap2Pojo(this.getParameters(), transferMacau);
		
		//add by lzg 20190531
		boolean flag = checkChargeAccount(transferMacau);
		if(!flag){
			throw new NTBWarningException("err.txn.chargeAccountError");
		}
		//add by lzg end
		
		transferMacau.setUserId(corpUser.getUserId());
		transferMacau.setCorpId(corpUser.getCorpId());
		// add by li_zd at 20171117 for mul-language
        Double transAmt = TransAmountFormate.formateAmount(this.getParameter("transferAmount"), lang);
        Double debitAmt = TransAmountFormate.formateAmount(this.getParameter("debitAmount"), lang);
        transferMacau.setTransferAmount(transAmt);
        transferMacau.setDebitAmount(debitAmt);
        // end
//		transferMacau.setExecuteTime(new Date());
//		transferMacau.setRequestTime(new Date());
		
		 //add by mxl 0130 閿熸枻鎷峰�閿熸枻鎷穋hargeAccount,chargeBy
//		transferMacau.setChargeAccount(transferMacau.getFromAccount());
		// Jet modified 2007-12-18
//		transferMacau.setChargeBy("O");
		
		ScheduleTransferHis schTransferHis = new ScheduleTransferHis();
		schTransferHis.setUserId(corpUser.getUserId());
		schTransferHis.setCorporaitonId(corpUser.getCorpId());
		schTransferHis.setBeneficiaryType(TransferConstants.TRANSFER_TYPE_MACAU);
		schTransferHis.setTransId(transferMacau.getTransId());
		schTransferHis.setScheduleName(Utils.null2EmptyWithTrim(getParameter("scheduleName")));
        
		schTransferHis.setEndDate(endDate);  //add by heyj 20190529
		
		// add by mxl 1008 閿熸枻鎷锋灇閿熺粸寮婎亷鎷烽敓缁炴唻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹
		String dayType = null;
		dayType = Utils.null2EmptyWithTrim(getParameter("dayType"));
		schTransferHis.setFrequnceType(dayType);

        // add by hjs 20070723 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓锟�
		if (schTransferHis.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			for (int i = 0; i <= getParameterValues("frequenceWeekDays").length - 1; i++) {
				this.getParameters().put("week" + i, getParameterValues("frequenceWeekDays")[i]);
			}
		}
		
		/* add 0814 get currency accounding to account 閿熸枻鎷烽敓鏂ゆ嫹灞庡憰閿熸枻鎷烽敓缁炲彿鐨勪紮鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹 */
//		CorpAccount corpAccount = corpAccountService
//				.viewCorpAccount(transferMacau.getFromAccount());
//		transferMacau.setFromCurrency(corpAccount.getCurrency());

		/* add by mxl 0827 get the currency number according the currency code */
//		CorpAccount corpChargeAccount = corpAccountService
//				.viewCorpAccount(transferMacau.getChargeAccount());
//		String chargeCurrency = corpChargeAccount.getCurrency();
//		transferMacau.setChargeCurrency(chargeCurrency);
		String chargeCurrency = this.getParameter("chargeCurrency");

		String fromCurrencyCode = rcCurrency.getCbsNumberByCcy(transferMacau
				.getFromCurrency());
		String toCurrencyCode = rcCurrency.getCbsNumberByCcy(transferMacau
				.getToCurrency());
		String chareCurrencyCode = rcCurrency.getCbsNumberByCcy(chargeCurrency);

		// get CGD from local_bank_code according to the bank_name
		GenericJdbcDao dao = (GenericJdbcDao) appContext
				.getBean("genericJdbcDao");
		try {
			List idList = null;
			idList = dao.query(SELECT_CGDLOCAL, new Object[] { transferMacau
					.getBeneficiaryBank() });
			Map cdtMap = (Map) idList.get(0);
			CGD = (String) cdtMap.get("CGD_FLAG");
		} catch (Exception e) {
			throw new NTBException("err.txn.GetCGDException");
		}
		
		//閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓绐栴垽鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�
		Map fromHost = transferService.toHostChargeEnquiryNew(transferMacau
				.getTransId(), corpUser, new BigDecimal(transferMacau
				.getTransferAmount().toString()), "MO", CGD, fromCurrencyCode,
				toCurrencyCode, transferMacau.getChargeBy(), chareCurrencyCode,
				"N");
		transferService.uploadEnquiryMacau(transferMacau, fromHost);

		// 閿熸枻鎷烽敓濮愪氦閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓锟絚omputing the charge amount 0912
		double chargeAmount = (new Double(fromHost.get("HANDLING_CHRG_AMT")
				.toString())).doubleValue();		
//				+ (new Double(fromHost.get("COMM_AMT").toString()))
//						.doubleValue()
//				+ (new Double(fromHost.get("TAX_AMOUNT").toString()))
//						.doubleValue()
//				+ (new Double(fromHost.get("SWIFT_CHRG_AMT").toString()))
//						.doubleValue()
//				+ (new Double(fromHost.get("OUR_CHG_AMT").toString()))
//						.doubleValue();
		//update by linrui 20180124
		transferMacau.setHandlingAmount(new Double(fromHost.get(
				"HANDLING_CHRG_AMT").toString()));
		/*transferMacau.setCommissionAmount(new Double(fromHost.get("COMM_AMT")
				.toString()));
		transferMacau.setTaxAmount(new Double(fromHost.get("TAX_AMOUNT")
				.toString()));
		transferMacau.setSwiftAmount(new Double(fromHost.get("SWIFT_CHRG_AMT")
				.toString()));
		transferMacau.setChargeOur(new Double(fromHost.get("OUR_CHG_AMT")
				.toString()));*///comment by linrui 20180124
		transferMacau.setChargeAmount(new Double(chargeAmount));
		
        // 閿熸枻鎷烽敓绲沞bitAmount 閿熸枻鎷�transferAmount涔嬩竴,閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷蜂竴閿熸枻鎷烽敓鏂ゆ嫹閿燂拷
		if (transferMacau.getTransferAmount().doubleValue() == 0) {
            // 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹杞敓缁炴枻鎷烽敓渚ユ唻鎷峰織
			transferMacau.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_FROM);
			BigDecimal transferAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(), new BigDecimal(transferMacau
							.getDebitAmount().toString()), null, 2);

			transferMacau.setTransferAmount(new Double(transferAmount
					.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinAmtOtherBanks(transferMacau.getFromCurrency(), transferMacau.getDebitAmount().doubleValue());
			
			
			// 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓锟絚ompuing the exchange rate 0907
			Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(), 7);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

			// Jet modified. show only one exchange rate at the page, it should be always buy rate/sell rate 
			if (null == buyRate){
				buyRate = new BigDecimal("1");
			}
			if (null == sellRate){
				sellRate = new BigDecimal("1");
			}			
			if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
				exchangeRate = new BigDecimal("0");
			} else {
				exchangeRate = buyRate.divide(sellRate, 7,
						BigDecimal.ROUND_HALF_UP);
			}
			transferMacau.setExchangeRate(new Double(exchangeRate.doubleValue()));
		} else if (transferMacau.getDebitAmount() == null
				|| transferMacau.getDebitAmount().doubleValue() == 0) {

            // 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹杞敓缁炴枻鎷烽敓渚ユ唻鎷峰織
			transferMacau.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_TO);
			BigDecimal toAmount = new BigDecimal(transferMacau
					.getTransferAmount().toString());
			BigDecimal fromAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(),
					null, toAmount, 2);

			transferMacau.setDebitAmount(new Double(fromAmount.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinAmtOtherBanks(transferMacau.getToCurrency(), transferMacau.getTransferAmount().doubleValue());
			
			// 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓锟絚ompuing the exchange rate 0907
			Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(),

			transferMacau.getFromCurrency(), transferMacau.getToCurrency(), 7);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

			// Jet modified. show only one exchange rate at the page, it should be always buy rate/sell rate 
			if (null == buyRate){
				buyRate = new BigDecimal("1");
			}
			if (null == sellRate){
				sellRate = new BigDecimal("1");
			}			
			if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
				exchangeRate = new BigDecimal("0");
			} else {
				exchangeRate = buyRate.divide(sellRate, 7,
						BigDecimal.ROUND_HALF_UP);
			}
			transferMacau.setExchangeRate(new Double(exchangeRate.doubleValue()));
		}

		// 閿熸枻鎷风ず閿熸枻鎷烽敓锟�閿熸枻鎷烽敓鑺傜》鎷烽敓鏂ゆ嫹閿熸枻鎷风枱涓氬閿熺粸鏂ゆ嫹閿熺祴ransferInMacauAction.java閿熷彨璇ф嫹涓�敓鏂ゆ嫹, 閿熸枻鎷锋敞閿熸枻鎷� 2017/7/13 at Li_zd
//		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
//				.getCorpId(), transferMacau.getToCurrency(), "MOP",
//				new BigDecimal(transferMacau.getTransferAmount().toString()),
//				null, 2);
		
		// 閿熷彨璁规嫹閿熺煫浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹骞曢敓鏂ゆ嫹娑插緬閿熸枻鎷�add by mxl 0922
		String inputAmountFlag = transferMacau.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferMacau.getFromCurrency();
			transferAmount = transferMacau.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferMacau.getToCurrency();
			transferAmount = transferMacau.getTransferAmount();
		}
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), currency, "MOP",
				new BigDecimal(transferAmount.toString()),
				null, 2);
		
		// 閿熸枻鎷烽エ浼欐嫹鎹夐敓鏂ゆ嫹閿熻鍚﹁秴绛规嫹姣忛敓绉告枻鎷烽敓鏂ゆ嫹閿熸纭锋嫹閿熸枻鎷烽敓鏂ゆ嫹
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		if (!transferLimitService.checkLimitQuotaByCorpId1(corpUser.getCorpId(),
				Constants.TXN_TYPE_TRANSFER_MACAU_SCHEDULE, transferMacau.getDebitAmount().doubleValue(),
				equivalentMOP.doubleValue())) {
			// write limit report
			Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", transferMacau.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_MACAU_SCHEDULE);
			reportMap.put("LOCAL_TRANS_CODE", transferMacau.getTransId());
			reportMap.put("FROM_CURRENCY", transferMacau.getFromCurrency());
			reportMap.put("FROM_ACCOUNT", transferMacau.getFromAccount());
			reportMap.put("TO_CURRENCY", transferMacau.getToCurrency());
			reportMap.put("FROM_AMOUNT", transferMacau.getDebitAmount());
			reportMap.put("TO_AMOUNT", transferMacau.getTransferAmount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
			throw new NTBWarningException("err.txn.ExceedDailyLimit");
		}

		// 閿熸枻鎷风ず閿熸枻鎷烽敓锟�
		Map resultData = new HashMap();
		this.convertPojo2Map(transferMacau, resultData);
		//add by lzg 20190522
		//resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("transferuser", transferService.loadAccountForRemittance(corpUser.getCorpId()));
		//add by lzg end
		this.setResultData(resultData);
		
		//modified by lzg 20190602
		/*int checkPurpose = transferService.checkNeedPurpose(corpUser
				.getCorpId(), transferMacau.getFromAccount(), Utils
				.null2Empty(transferMacau.getTransferAmount()), Utils
				.null2Empty(transferMacau.getDebitAmount()), transferMacau
				.getFromCurrency(), transferMacau.getToCurrency(), "MO");
		if (checkPurpose == 1) {
			transferMacau.setPurposeCode("99");
			transferMacau.setProofOfPurpose("N");
			if(transferMacau.getOtherPurpose() == null 
					|| "".equals(transferMacau.getOtherPurpose())){
				throw new NTBWarningException("err.txn.NullPurpose");
			}
		} else if (checkPurpose == 2) {
			transferMacau.setPurposeCode("99");
			transferMacau.setProofOfPurpose("Y");
			if(transferMacau.getOtherPurpose() == null 
					|| "".equals(transferMacau.getOtherPurpose())){
				throw new NTBWarningException("err.txn.NullPurpose");
			}
		} else {
			transferMacau.setProofOfPurpose("N");
			transferMacau.setPurposeCode("");
			transferMacau.setOtherPurpose("");
		}
		String purpose = transferMacau.getOtherPurpose();
		if(purpose != null && !"".equals(purpose)){
			resultData.put("showPurpose", "true");
			resultData.put("purpose", purpose);
		}*/
		//modified by lzg end
		// add by mxl 0821
		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		resultData.put("txnType", Constants.TXN_SUBTYPE_SCHEDULE_TXN_NEW);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);

		// add by mxl 1008 涓洪敓鏂ゆ嫹閿熸枻鎷烽〉閿熸枻鎷烽敓鏂ゆ嫹绀簊cheduleTransfer閿熸枻鎷烽敓鏂ゆ嫹鎭�
		resultData.put("frequenceType", schTransferHis.getFrequnceType());
		resultData.put("frequenceDays", schTransferHis.getFrequnceDays());
		resultData.put("scheduleName", schTransferHis.getScheduleName());

        // edit by mxl 1214 閿熸枻鎷烽敓闃额亷鎷烽敓缁炴唻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓閰典紮鎷锋灇閿熺粸寮婎亷鎷烽敓缁炴唻鎷烽敓锟�
		if (dayType.equals(ScheduleTransfer.DAILY)) {
			schTransferHis.setFrequnceDays("");
		} else if (dayType.equals(ScheduleTransfer.WEEKLY)) {
			String[] frequenceWeekDays = this
					.getParameterValues("frequenceWeekDays");
			String frequenceDays = frequenceWeekDays[0];
			if (frequenceWeekDays.length == 1) {
				schTransferHis.setFrequnceDays(frequenceDays);
			} else {
				for (int i = 1; i <= frequenceWeekDays.length - 1; i++) {
					frequenceDays = frequenceDays + "," + frequenceWeekDays[i];
					schTransferHis.setFrequnceDays(frequenceDays);
				}
			}
		} else if (dayType.equals(ScheduleTransfer.MONTHLY)) {
			String monthType = Utils
					.null2EmptyWithTrim(getParameter("month_type"));
			if (monthType.equals("1")) {
				schTransferHis.setFrequnceDays("1");
			} else if (monthType.equals("-1")) {
				schTransferHis.setFrequnceDays("-1");
			} else if (monthType.equals("0")) {
				String frequenceDays = Utils
						.null2EmptyWithTrim(getParameter("designed_day"));
				schTransferHis.setFrequnceDays(frequenceDays);
			}
		} else if (dayType.equals(ScheduleTransfer.DAYS_PER_MONTH)) {
			String frequenceDays = Utils
					.null2EmptyWithTrim(getParameter("days_per_month"));

			// add by hjs 2006-12-14
			schTransferService.checkSeparator(frequenceDays);
			// add by hjs 20070207
			frequenceDays = schTransferService.sortFrequenceDays(frequenceDays);
			
			schTransferHis.setFrequnceDays(frequenceDays);
		}
		if (schTransferHis.getFrequnceType().equals(
				ScheduleTransfer.DAYS_PER_MONTH)) {
			resultData.put("days_per_month", schTransferHis.getFrequnceDays());
		}
		
		String week[] = null;
		if (schTransferHis.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			week = schTransferHis.getFrequnceDays().split(",");
			for (int i = 0; i <= week.length - 1; i++) {
				resultData.put("week" + i, week[i]);
			}
		}
		resultData.put("frequenceDays", schTransferHis.getFrequnceDays());
		resultData.put("transferDate", transferMacau.getTransferDate());
       
		ApproveRuleService approveRuleService = (ApproveRuleService) Config
				.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
				resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}
		
		// scheduleName閿熶茎纭锋嫹閿燂拷add by mxl 0109
		schTransferService.checkScheduleName(schTransferHis.getScheduleName());
		
		// 閿熸枻鎷烽敓鐭紮鎷烽敓鏂ゆ嫹閿熷彨杈炬嫹閿熺氮ession閿熸枻鎷烽敓鐨嗘唻鎷穋onfirm閿熸枻鎷峰啓閿熸枻鎷烽敓鏂ゆ嫹鑿橀敓锟�
		this.setUsrSessionDataValue("transferMacau", transferMacau);
		this.setUsrSessionDataValue("schTransferHis", schTransferHis);
	}

	private boolean checkChargeAccount(TransferMacau transferMacau) throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		CibTransClient testClient = new CibTransClient("CIB", "0195");
		String chargeAccount = transferMacau.getChargeAccount();
		toHost.put("ACCOUNT_NO", chargeAccount);
		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		ArrayList ccyList = new ArrayList();
		boolean flag = false;
		ArrayList ccyBalList = (ArrayList)fromHost.get("CCY_BAL_LIST");
		for(int i = 0;i < ccyBalList.size();i++){
			String ccy = (String)((HashMap)ccyBalList.get(i)).get("CCY_CODE_OF_AC");
			if(ccy != null){
				if(!"".equals(ccy.trim())){
					if(ccy.equals(transferMacau.getFromCurrency())){
						flag = true;
					}
					
				}
			}
		}
		return flag;
	}

	public void addCancel() throws NTBException {
		TransferMacau transferMacau = (TransferMacau) this.getUsrSessionDataValue("transferMacau");
		ScheduleTransferHis schTransferHis = (ScheduleTransferHis) this.getUsrSessionDataValue("schTransferHis");
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		// add by mxl 1212
		String week[] = null;
		if (schTransferHis.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			week = schTransferHis.getFrequnceDays().split(",");
		}

		// 閿熷彨璁规嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�
		if (transferMacau.getInputAmountFlag().equals(
				Constants.INPUT_AMOUNT_FLAG_FROM)) {
			transferMacau.setTransferAmount(null);
		} else if (transferMacau.getInputAmountFlag().equals(
				Constants.INPUT_AMOUNT_FLAG_TO)) {
			transferMacau.setDebitAmount(null);
		}
		
		Map resultData = new HashMap();
		if (schTransferHis.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			for (int i = 0; i <= week.length - 1; i++) {
				resultData.put("week" + i, week[i]);
			}
		}
		this.convertPojo2Map(transferMacau, resultData);
		// 涓洪敓鏂ゆ嫹椤甸敓鏂ゆ嫹閿熸枻鎷风ずSchedule Transfer閿熸枻鎷烽敓鏂ゆ嫹鎭�
		resultData.put("frequenceType", schTransferHis.getFrequnceType());
		resultData.put("frequenceDays", schTransferHis.getFrequnceDays());
		resultData.put("scheduleName", schTransferHis.getScheduleName());
		// add by hjs 20070723
		if(schTransferHis.getFrequnceType().equals(ScheduleTransfer.MONTHLY)){
			resultData.put("month_type", schTransferHis.getFrequnceDays());
		}
		CorpUser corpUser = (CorpUser) this.getUser();
		//modified by lzg 20190708
		//resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("transferuser", transferService.loadAccountForRemittance(corpUser.getCorpId()));
		//modified by lzg end
		resultData.put("selectFromAcct", transferMacau.getFromAccount() + " - "  + transferMacau.getFromCurrency());//add by linrui for mul-ccy
		setResultData(resultData);

		// 閿熸枻鎷烽敓鐭紮鎷烽敓鏂ゆ嫹閿熷彨杈炬嫹閿熺氮ession閿熸枻鎷烽敓鐨嗘唻鎷穋onfirm閿熸枻鎷峰啓閿熸枻鎷烽敓鏂ゆ嫹鑿橀敓锟�
		this.setUsrSessionDataValue("transferMacau", transferMacau);
		this.setUsrSessionDataValue("schTransferHis", schTransferHis);
	}

	public void addConfirm() throws NTBException {

		ApplicationContext appContext = Config.getAppContext();
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		// 閿熸枻鎷烽敓鎻紮鎷烽敓鏂ゆ嫹閿熷彨杈炬嫹閿熸枻鎷烽敓鎹峰尅鎷�
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");
		
		TransferMacau transferMacau = (TransferMacau) this
				.getUsrSessionDataValue("transferMacau");
		ScheduleTransferHis schTransferHis = (ScheduleTransferHis) this
				.getUsrSessionDataValue("schTransferHis");
		CorpUser corpUser = (CorpUser) this.getUser();
		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));

		//hjs
		String seqNo = CibIdGenerator.getIdForOperation("SCHEDULE_TRANSFER_HIS");
		// 閿熼摪鏂ゆ嫹涓�敓鏂ゆ嫹閿熸枻鎷锋潈閿熸枻鎷烽敓鏂ゆ嫹
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferMacau.getToCurrency(), "MOP",
				new BigDecimal(transferMacau.getTransferAmount().toString()),
				null, 2);
		String inputAmountFlag = transferMacau.getInputAmountFlag();
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_SCHEDULE_TXN_NEW,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				ScheduleTransferMacauAction.class, 
				transferMacau.getFromCurrency(),
				transferMacau.getDebitAmount().doubleValue(),
				transferMacau.getToCurrency(),
				transferMacau.getTransferAmount().doubleValue(), 
				equivalentMOP.doubleValue(), 
				seqNo, transferMacau.getRemark(), corpUser, assignedUser, 
				corpUser.getCorporation().getAllowExecutor(), inputAmountFlag);

		try {
			transferMacau.setExecuteTime(new Date());
			transferMacau.setRequestTime(new Date());
			transferMacau.setTransId(CibIdGenerator.getRefNoForTransaction());
			transferMacau.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			transferMacau.setStatus(Constants.STATUS_PENDING_APPROVAL);
			transferMacau.setOperation(Constants.OPERATION_NEW);
			// add by mxl 1010 閿熸枻鎷烽敓绲奵hedule Transfer 閿熸枻鎷烽敓鏂ゆ嫹涓�敓鏂ゆ嫹閿熸枻鎷峰綍
			schTransferHis.setScheduleId(CibIdGenerator.getRefNoForTransaction());
			schTransferHis.setTransId(transferMacau.getTransId());
			schTransferHis.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			schTransferHis.setStatus(Constants.STATUS_PENDING_APPROVAL);
			schTransferHis.setOperation(Constants.OPERATION_NEW);
			//hjs
			
			//add by long_zg 2014-05-06 for CR188
			schTransferHis.setLastUpdateTime(new Date()) ;
			
			ScheduleTransfer schTransfer = new ScheduleTransfer();
	        try {
	            BeanUtils.copyProperties(schTransfer, schTransferHis);
	        } catch (Exception e) {
	            Log.error("Error copy properties", e);
	            throw new NTBException("err.sys.GeneralError");
	        }
	        schTransferHis.setSeqNo(seqNo);
			schTransferService.addSchTransfer(schTransfer);
			schTransferService.addSchTransferHis(schTransferHis);
			schTransferService.addTransferMacau(transferMacau);

			Map resultData = new HashMap();
			
			// add by lw 2011-01-18
			String purpose = transferMacau.getOtherPurpose();
			if(purpose != null && !"".equals(purpose)){
				resultData.put("showPurpose", "true");
				resultData.put("purpose", purpose);
			}
			// add by lw end
			
			// add by mxl 0821
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");
			// 閿熷彨璁规嫹閿熺煫浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹骞曢敓鏂ゆ嫹娑插緬閿熸枻鎷�add by mxl 0922

			String currency = null;
			Double transferAmount = new Double(0);
			if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferMacau.getFromCurrency();
				transferAmount = transferMacau.getDebitAmount();
			} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferMacau.getToCurrency();
				transferAmount = transferMacau.getTransferAmount();
			}
			resultData.put("txnType", Constants.TXN_SUBTYPE_SCHEDULE_TXN_NEW);
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);
			// 涓洪敓鏂ゆ嫹椤甸敓鏂ゆ嫹閿熸枻鎷风ずSchedule Transfer閿熸枻鎷烽敓鏂ゆ嫹鎭�
			resultData.put("frequenceType", schTransferHis.getFrequnceType());
			resultData.put("frequenceDays", schTransferHis.getFrequnceDays());
			resultData.put("scheduleName", schTransferHis.getScheduleName());

			this.convertPojo2Map(transferMacau, resultData);

			ApproveRuleService approveRuleService = (ApproveRuleService) Config
					.getAppContext().getBean("ApproveRuleService");
			if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
					resultData)) {
				throw new NTBException("err.flow.ApproveRuleNotDefined");
			}

			setResultData(resultData);
			resultData.put("transferDate", transferMacau.getTransferDate());
			// 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸纭锋嫹 add by mxl 0928
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", transferMacau.getRequestTime());
			dataMap.put("transId", transferMacau.getTransId());
			dataMap.put("toAmount", transferMacau.getTransferAmount());
			dataMap.put("corpName", corpUser.getCorporation().getCorpName());
			dataMap.put("toAccount", transferMacau.getToAccount());
			dataMap.put("beneficiaryAccName", transferMacau
					.getBeneficiaryName1());
			dataMap.put("beneficiaryBank", transferMacau.getBeneficiaryBank());
			dataMap.put("transId", transferMacau.getTransId());

			dataMap.put("remark", transferMacau.getRemark());
			
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_NEW, userName, dataMap);*/
			//mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_NEW, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("Error process confirmation of transfering in MACAU", e);
			throw new NTBException("err.txn.TranscationFaily");
		}
	}

	public void deleteLoad() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpUser corpUser = (CorpUser) this.getUser();

		String transID = getParameter("transId");
		String scheduleId = getParameter("scheduleId");

		//閿熷彨璁规嫹閿熻鍑ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鏉冪姸鎬�
		if (schTransferService.isPending(scheduleId)) {
			throw new NTBException("err.bat.OperationPending");
		}
		
		// load閿熸枻鎷穝cheduleTransfer
		ScheduleTransfer scheduleTransfer = schTransferService.loadSchTransfer(scheduleId);
		TransferMacau transferMacau = transferService.viewInMacau(transID);
		
		Map resultData = new HashMap();
		
		// add by lw 2011-01-27
		String purpose = transferMacau.getOtherPurpose();
		if(purpose != null && !"".equals(purpose)){
			resultData.put("showPurpose", "true");
			resultData.put("purpose", purpose);
		}
		// add by lw end
		
		this.convertPojo2Map(transferMacau, resultData);
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferMacau.getToCurrency(), "MOP",
				new BigDecimal(transferMacau.getTransferAmount().toString()),
				null, 2);
		this.setResultData(resultData);

		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		// 閿熷彨璁规嫹閿熺煫浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹骞曢敓鏂ゆ嫹娑插緬閿熸枻鎷�add by mxl 0922
		String inputAmountFlag = transferMacau.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferMacau.getFromCurrency();
			transferAmount = transferMacau.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferMacau.getToCurrency();
			transferAmount = transferMacau.getTransferAmount();
		}
		resultData.put("txnType", Constants.TXN_SUBTYPE_SCHEDULE_TXN_REMOVE);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);

		resultData.put("frequenceType", scheduleTransfer.getFrequnceType());
		resultData.put("frequenceDays", scheduleTransfer.getFrequnceDays());
		resultData.put("scheduleName", scheduleTransfer.getScheduleName());
		if (scheduleTransfer.getFrequnceType().equals(
				ScheduleTransfer.DAYS_PER_MONTH)) {
			resultData.put("days_per_month", scheduleTransfer.getFrequnceDays());
		}

		ApproveRuleService approveRuleService = (ApproveRuleService) Config
				.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
				resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}

		// 涓洪敓鏂ゆ嫹椤甸敓鏂ゆ嫹閿熸枻鎷风ずSchedule Transfer閿熸枻鎷烽敓鏂ゆ嫹鎭�
		resultData.put("frequenceType", scheduleTransfer.getFrequnceType());
		resultData.put("frequenceDays", scheduleTransfer.getFrequnceDays());
		resultData.put("scheduleName", scheduleTransfer.getScheduleName());
		setResultData(resultData);
		// 閿熸枻鎷烽敓鐭紮鎷烽敓鏂ゆ嫹閿熷彨杈炬嫹閿熺氮ession閿熸枻鎷烽敓鐨嗘唻鎷穋onfirm閿熸枻鎷峰啓閿熸枻鎷烽敓鏂ゆ嫹鑿橀敓锟�
		this.setUsrSessionDataValue("transferMacau", transferMacau);
		this.setUsrSessionDataValue("scheduleTransfer", scheduleTransfer);

	}

	public void deleteConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");
		
		CorpUser corpUser = (CorpUser) this.getUser();
		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));
		TransferMacau transferMacau = (TransferMacau) this
				.getUsrSessionDataValue("transferMacau");
		ScheduleTransfer schTransfer = (ScheduleTransfer) this
		.getUsrSessionDataValue("scheduleTransfer");

		//hjs
		String seqNo = CibIdGenerator.getIdForOperation("SCHEDULE_TRANSFER_HIS");
		// 閿熼摪鏂ゆ嫹涓�敓鏂ゆ嫹閿熸枻鎷锋潈閿熸枻鎷烽敓鏂ゆ嫹
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferMacau.getToCurrency(), "MOP",
				new BigDecimal(transferMacau.getTransferAmount().toString()),
				null, 2);
		String inputAmountFlag = transferMacau.getInputAmountFlag();
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_SCHEDULE_TXN_REMOVE,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				ScheduleTransferMacauAction.class, 
				transferMacau.getFromCurrency(),
				transferMacau.getDebitAmount().doubleValue(),
				transferMacau.getToCurrency(),
				transferMacau.getTransferAmount().doubleValue(),
				equivalentMOP.doubleValue(), 
				seqNo, transferMacau.getRemark(), corpUser, assignedUser, 
				corpUser.getCorporation().getAllowExecutor(), inputAmountFlag);
		try {
			//hjs
			// 閿熸枻鎷烽敓绲奵hedule Transfer 閿熺潾闈╂嫹涓�敓鏂ゆ嫹閿熸枻鎷峰綍
			schTransfer.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			schTransfer.setStatus(Constants.STATUS_PENDING_APPROVAL);
			schTransfer.setOperation(Constants.OPERATION_REMOVE);
			
			//add by long_zg 2014-05-06 for CR188
			schTransfer.setLastUpdateTime(new Date()) ;
			
			//hjs
			ScheduleTransferHis schTransferHis = new ScheduleTransferHis();
	        try {
	            BeanUtils.copyProperties(schTransferHis, schTransfer);
	        } catch (Exception e) {
	            Log.error("Error copy properties", e);
	            throw new NTBException("err.sys.GeneralError");
	        }
	        schTransferHis.setSeqNo(seqNo);
			schTransferService.addSchTransferHis(schTransferHis);

			Map resultData = new HashMap();
			
			// add by lw 2011-01-27
			String purpose = transferMacau.getOtherPurpose();
			if(purpose != null && !"".equals(purpose)){
				resultData.put("showPurpose", "true");
				resultData.put("purpose", purpose);
			}
			// add by lw end
			
			// 涓洪敓鏂ゆ嫹椤甸敓鏂ゆ嫹閿熸枻鎷风ずSchedule Transfer閿熸枻鎷烽敓鏂ゆ嫹鎭�
			resultData.put("frequenceType", schTransferHis.getFrequnceType());
			resultData.put("frequenceDays", schTransferHis.getFrequnceDays());
			resultData.put("scheduleName", schTransferHis.getScheduleName());
			// add by mxl 0821
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");
			// 閿熷彨璁规嫹閿熺煫浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹骞曢敓鏂ゆ嫹娑插緬閿熸枻鎷�add by mxl 0922

			String currency = null;
			Double transferAmount = new Double(0);
			if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferMacau.getFromCurrency();
				transferAmount = transferMacau.getDebitAmount();
			} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferMacau.getToCurrency();
				transferAmount = transferMacau.getTransferAmount();
			}
			resultData
					.put("txnType", Constants.TXN_SUBTYPE_SCHEDULE_TXN_REMOVE);
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);

			this.convertPojo2Map(transferMacau, resultData);

			ApproveRuleService approveRuleService = (ApproveRuleService) Config
					.getAppContext().getBean("ApproveRuleService");
			if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
					resultData)) {
				throw new NTBException("err.flow.ApproveRuleNotDefined");
			}

			setResultData(resultData);
			resultData.put("transferDate", transferMacau.getTransferDate());
			// 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸纭锋嫹 add by mxl 0928
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", new Date());
			dataMap.put("transId", transferMacau.getTransId());
			dataMap.put("toAmount", transferMacau.getTransferAmount());
			dataMap.put("corpName", corpUser.getCorporation().getCorpName());
			dataMap.put("toAccount", transferMacau.getToAccount());
			dataMap.put("beneficiaryAccName", transferMacau.getBeneficiaryName1());
			dataMap.put("beneficiaryBank", transferMacau.getBeneficiaryBank());
			dataMap.put("transId", transferMacau.getTransId());
			dataMap.put("remark", transferMacau.getRemark());
			
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_REMOVE, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_REMOVE, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("Error process confirmation of transfering in MACAU", e);
			throw new NTBException("err.txn.TranscationFaily");
		}

	}

	public void blockLoad() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		
		CorpUser corpUser = (CorpUser) this.getUser();
		String transID = getParameter("transId");
		String scheduleId = getParameter("scheduleId");

		//閿熷彨璁规嫹閿熻鍑ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鏉冪姸鎬�
		if (schTransferService.isPending(scheduleId)) {
			throw new NTBException("err.bat.OperationPending");
		}
		
		// load閿熸枻鎷穝cheduleTransfer
		ScheduleTransfer scheduleTransfer = schTransferService.loadSchTransfer(scheduleId);
		TransferMacau transferMacau = transferService.viewInMacau(transID);
		
		Map resultData = new HashMap();
		
		// add by lw 2011-01-27
		String purpose = transferMacau.getOtherPurpose();
		if(purpose != null && !"".equals(purpose)){
			resultData.put("showPurpose", "true");
			resultData.put("purpose", purpose);
		}
		// add by lw end
		
		this.convertPojo2Map(transferMacau, resultData);
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferMacau.getToCurrency(), "MOP",
				new BigDecimal(transferMacau.getTransferAmount().toString()),
				null, 2);
		this.setResultData(resultData);

		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		// 閿熷彨璁规嫹閿熺煫浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹骞曢敓鏂ゆ嫹娑插緬閿熸枻鎷�add by mxl 0922
		String inputAmountFlag = transferMacau.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferMacau.getFromCurrency();
			transferAmount = transferMacau.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferMacau.getToCurrency();
			transferAmount = transferMacau.getTransferAmount();
		}
		resultData.put("txnType", Constants.TXN_SUBTYPE_SCHEDULE_TXN_BLOCK);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);

		resultData.put("frequenceType", scheduleTransfer.getFrequnceType());
		resultData.put("frequenceDays", scheduleTransfer.getFrequnceDays());
		resultData.put("scheduleName", scheduleTransfer.getScheduleName());
		if (scheduleTransfer.getFrequnceType().equals(
				ScheduleTransfer.DAYS_PER_MONTH)) {
			resultData.put("days_per_month", scheduleTransfer.getFrequnceDays());
		}

		ApproveRuleService approveRuleService = (ApproveRuleService) Config
				.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
				resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}

		// 涓洪敓鏂ゆ嫹椤甸敓鏂ゆ嫹閿熸枻鎷风ずSchedule Transfer閿熸枻鎷烽敓鏂ゆ嫹鎭�
		resultData.put("frequenceType", scheduleTransfer.getFrequnceType());
		resultData.put("frequenceDays", scheduleTransfer.getFrequnceDays());
		resultData.put("scheduleName", scheduleTransfer.getScheduleName());
		setResultData(resultData);
		// 閿熸枻鎷烽敓鐭紮鎷烽敓鏂ゆ嫹閿熷彨杈炬嫹閿熺氮ession閿熸枻鎷烽敓鐨嗘唻鎷穋onfirm閿熸枻鎷峰啓閿熸枻鎷烽敓鏂ゆ嫹鑿橀敓锟�
		this.setUsrSessionDataValue("transferMacau", transferMacau);
		this.setUsrSessionDataValue("scheduleTransfer", scheduleTransfer);

	}

	public void blockConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		
		CorpUser corpUser = (CorpUser) this.getUser();
		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));
		TransferMacau transferMacau = (TransferMacau) this
				.getUsrSessionDataValue("transferMacau");
		ScheduleTransfer schTransfer = (ScheduleTransfer) this.
				getUsrSessionDataValue("scheduleTransfer");

		//hjs
		String seqNo = CibIdGenerator.getIdForOperation("SCHEDULE_TRANSFER_HIS");
		// 閿熼摪鏂ゆ嫹涓�敓鏂ゆ嫹閿熸枻鎷锋潈閿熸枻鎷烽敓鏂ゆ嫹
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferMacau.getToCurrency(), "MOP",
				new BigDecimal(transferMacau.getTransferAmount().toString()),
				null, 2);
		String inputAmountFlag = transferMacau.getInputAmountFlag();
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_SCHEDULE_TXN_BLOCK,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				ScheduleTransferMacauAction.class, 
				transferMacau.getFromCurrency(), 
				transferMacau.getDebitAmount().doubleValue(), 
				transferMacau.getToCurrency(),
				transferMacau.getTransferAmount().doubleValue(), 
				equivalentMOP.doubleValue(), 
				seqNo, transferMacau.getRemark(), corpUser, assignedUser,
				corpUser.getCorporation().getAllowExecutor(), inputAmountFlag);
		try {
			//hjs
			// 閿熸枻鎷烽敓绲奵hedule Transfer 閿熺潾闈╂嫹涓�敓鏂ゆ嫹閿熸枻鎷峰綍
			schTransfer.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			schTransfer.setStatus(Constants.STATUS_PENDING_APPROVAL);
			schTransfer.setOperation(Constants.OPERATION_BLOCK);
			
			//add by long_zg 2014-05-06 for CR188
			schTransfer.setLastUpdateTime(new Date()) ;
			
			ScheduleTransferHis schTransferHis = new ScheduleTransferHis();
	        try {
	            BeanUtils.copyProperties(schTransferHis, schTransfer);
	        } catch (Exception e) {
	            Log.error("Error copy properties", e);
	            throw new NTBException("err.sys.GeneralError");
	        }
	        schTransferHis.setSeqNo(seqNo);
			schTransferService.addSchTransferHis(schTransferHis);

			Map resultData = new HashMap();
			
			// add by lw 2011-01-27
			String purpose = transferMacau.getOtherPurpose();
			if(purpose != null && !"".equals(purpose)){
				resultData.put("showPurpose", "true");
				resultData.put("purpose", purpose);
			}
			// add by lw end
			
			// 涓洪敓鏂ゆ嫹椤甸敓鏂ゆ嫹閿熸枻鎷风ずSchedule Transfer閿熸枻鎷烽敓鏂ゆ嫹鎭�
			resultData.put("frequenceType", schTransferHis.getFrequnceType());
			resultData.put("frequenceDays", schTransferHis.getFrequnceDays());
			resultData.put("scheduleName", schTransferHis.getScheduleName());
			// add by mxl 0821
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");
			// 閿熷彨璁规嫹閿熺煫浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹骞曢敓鏂ゆ嫹娑插緬閿熸枻鎷�add by mxl 0922

			String currency = null;
			Double transferAmount = new Double(0);
			if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferMacau.getFromCurrency();
				transferAmount = transferMacau.getDebitAmount();
			} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferMacau.getToCurrency();
				transferAmount = transferMacau.getTransferAmount();
			}
			resultData.put("txnType", Constants.TXN_SUBTYPE_SCHEDULE_TXN_BLOCK);
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);

			this.convertPojo2Map(transferMacau, resultData);

			ApproveRuleService approveRuleService = (ApproveRuleService) Config
					.getAppContext().getBean("ApproveRuleService");
			if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
					resultData)) {
				throw new NTBException("err.flow.ApproveRuleNotDefined");
			}

			setResultData(resultData);
			resultData.put("transferDate", transferMacau.getTransferDate());
			// 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸纭锋嫹 add by mxl 0928
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", transferMacau.getRequestTime());
			dataMap.put("transId", transferMacau.getTransId());
			dataMap.put("toAmount", transferMacau.getTransferAmount());
			dataMap.put("corpName", corpUser.getCorporation().getCorpName());
			dataMap.put("toAccount", transferMacau.getToAccount());
			dataMap.put("beneficiaryAccName", transferMacau
					.getBeneficiaryName1());
			dataMap.put("beneficiaryBank", transferMacau.getBeneficiaryBank());
			dataMap.put("transId", transferMacau.getTransId());

			dataMap.put("remark", transferMacau.getRemark());
			
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_BLOCK, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_BLOCK, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("Error process confirmation of transfering in MACAU", e);
			throw new NTBException("err.txn.TranscationFaily");
		}

	}

	public void unblockLoad() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		
		CorpUser corpUser = (CorpUser) this.getUser();
		String transID = getParameter("transId");
		String scheduleId = getParameter("scheduleId");

		//閿熷彨璁规嫹閿熻鍑ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鏉冪姸鎬�
		if (schTransferService.isPending(scheduleId)) {
			throw new NTBException("err.bat.OperationPending");
		}
		
		// load閿熸枻鎷穝cheduleTransfer
		ScheduleTransfer scheduleTransfer = schTransferService.loadSchTransfer(scheduleId);
       	TransferMacau transferMacau = transferService.viewInMacau(transID);
       	
		Map resultData = new HashMap();
		
		// add by lw 2011-01-27
		String purpose = transferMacau.getOtherPurpose();
		if(purpose != null && !"".equals(purpose)){
			resultData.put("showPurpose", "true");
			resultData.put("purpose", purpose);
		}
		// add by lw end
		
		this.convertPojo2Map(transferMacau, resultData);
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferMacau.getToCurrency(), "MOP",
				new BigDecimal(transferMacau.getTransferAmount().toString()),
				null, 2);
		this.setResultData(resultData);

		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		// 閿熷彨璁规嫹閿熺煫浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹骞曢敓鏂ゆ嫹娑插緬閿熸枻鎷�add by mxl 0922
		String inputAmountFlag = transferMacau.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferMacau.getFromCurrency();
			transferAmount = transferMacau.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferMacau.getToCurrency();
			transferAmount = transferMacau.getTransferAmount();
		}
		resultData.put("txnType", Constants.TXN_SUBTYPE_SCHEDULE_TXN_UNBLOCK);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);

		resultData.put("frequenceType", scheduleTransfer.getFrequnceType());
		resultData.put("frequenceDays", scheduleTransfer.getFrequnceDays());
		resultData.put("scheduleName", scheduleTransfer.getScheduleName());
		if (scheduleTransfer.getFrequnceType().equals(
				ScheduleTransfer.DAYS_PER_MONTH)) {
			resultData.put("days_per_month", scheduleTransfer.getFrequnceDays());
		}

		ApproveRuleService approveRuleService = (ApproveRuleService) Config
				.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
				resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}

		// 涓洪敓鏂ゆ嫹椤甸敓鏂ゆ嫹閿熸枻鎷风ずSchedule Transfer閿熸枻鎷烽敓鏂ゆ嫹鎭�
		resultData.put("frequenceType", scheduleTransfer.getFrequnceType());
		resultData.put("frequenceDays", scheduleTransfer.getFrequnceDays());
		resultData.put("scheduleName", scheduleTransfer.getScheduleName());
		setResultData(resultData);
		// 閿熸枻鎷烽敓鐭紮鎷烽敓鏂ゆ嫹閿熷彨杈炬嫹閿熺氮ession閿熸枻鎷烽敓鐨嗘唻鎷穋onfirm閿熸枻鎷峰啓閿熸枻鎷烽敓鏂ゆ嫹鑿橀敓锟�
		this.setUsrSessionDataValue("transferMacau", transferMacau);
		this.setUsrSessionDataValue("scheduleTransfer", scheduleTransfer);

	}

	public void unblockConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		
		CorpUser corpUser = (CorpUser) this.getUser();
		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));
		TransferMacau transferMacau = (TransferMacau) this
				.getUsrSessionDataValue("transferMacau");
		ScheduleTransfer schTransfer = (ScheduleTransfer) this.getUsrSessionDataValue("scheduleTransfer");

		//hjs
		String seqNo = CibIdGenerator.getIdForOperation("SCHEDULE_TRANSFER_HIS");
		// 閿熼摪鏂ゆ嫹涓�敓鏂ゆ嫹閿熸枻鎷锋潈閿熸枻鎷烽敓鏂ゆ嫹
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferMacau.getToCurrency(), "MOP",
				new BigDecimal(transferMacau.getTransferAmount().toString()),
				null, 2);
		String inputAmountFlag = transferMacau.getInputAmountFlag();
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_SCHEDULE_TXN_UNBLOCK,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				ScheduleTransferMacauAction.class,
				transferMacau.getFromCurrency(), 
				transferMacau.getDebitAmount().doubleValue(), 
				transferMacau.getToCurrency(),
				transferMacau.getTransferAmount().doubleValue(),
				equivalentMOP.doubleValue(), 
				seqNo,	transferMacau.getRemark(), corpUser, assignedUser,
				corpUser.getCorporation().getAllowExecutor(), inputAmountFlag);
		try {
			//hjs
			// 閿熸枻鎷烽敓绲奵hedule Transfer 閿熺潾闈╂嫹涓�敓鏂ゆ嫹閿熸枻鎷峰綍
			schTransfer.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			schTransfer.setStatus(Constants.STATUS_PENDING_APPROVAL);
			schTransfer.setOperation(Constants.OPERATION_UNBLOCK);
			
			// add by long_zg 2014-05-06 for CR188
			schTransfer.setLastUpdateTime(new Date()) ;
			
			//hjs
			ScheduleTransferHis schTransferHis = new ScheduleTransferHis();
	        try {
	            BeanUtils.copyProperties(schTransferHis, schTransfer);
	        } catch (Exception e) {
	            Log.error("Error copy properties", e);
	            throw new NTBException("err.sys.GeneralError");
	        }
	        schTransferHis.setSeqNo(seqNo);
			schTransferService.addSchTransferHis(schTransferHis);

			Map resultData = new HashMap();
			
			// add by lw 2011-01-27
			String purpose = transferMacau.getOtherPurpose();
			if(purpose != null && !"".equals(purpose)){
				resultData.put("showPurpose", "true");
				resultData.put("purpose", purpose);
			}
			// add by lw end
			
			// 涓洪敓鏂ゆ嫹椤甸敓鏂ゆ嫹閿熸枻鎷风ずSchedule Transfer閿熸枻鎷烽敓鏂ゆ嫹鎭�
			resultData.put("frequenceType", schTransferHis.getFrequnceType());
			resultData.put("frequenceDays", schTransferHis.getFrequnceDays());
			resultData.put("scheduleName", schTransferHis.getScheduleName());
			// add by mxl 0821
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");
			// 閿熷彨璁规嫹閿熺煫浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹骞曢敓鏂ゆ嫹娑插緬閿熸枻鎷�add by mxl 0922

			String currency = null;
			Double transferAmount = new Double(0);
			if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferMacau.getFromCurrency();
				transferAmount = transferMacau.getDebitAmount();
			} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferMacau.getToCurrency();
				transferAmount = transferMacau.getTransferAmount();
			}
			resultData.put("txnType",
					Constants.TXN_SUBTYPE_SCHEDULE_TXN_UNBLOCK);
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);

			this.convertPojo2Map(transferMacau, resultData);

			ApproveRuleService approveRuleService = (ApproveRuleService) Config
					.getAppContext().getBean("ApproveRuleService");
			if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
					resultData)) {
				throw new NTBException("err.flow.ApproveRuleNotDefined");
			}

			setResultData(resultData);
			resultData.put("transferDate", transferMacau.getTransferDate());
			// 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸纭锋嫹 add by mxl 0928
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", transferMacau.getRequestTime());
			dataMap.put("transId", transferMacau.getTransId());
			dataMap.put("toAmount", transferMacau.getTransferAmount());
			dataMap.put("corpName", corpUser.getCorporation().getCorpName());
			dataMap.put("toAccount", transferMacau.getToAccount());
			dataMap.put("beneficiaryAccName", transferMacau
					.getBeneficiaryName1());
			dataMap.put("beneficiaryBank", transferMacau.getBeneficiaryBank());
			dataMap.put("transId", transferMacau.getTransId());

			dataMap.put("remark", transferMacau.getRemark());
			
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_UNBLOCK, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_UNBLOCK, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("Error process confirmation of transfering in MACAU", e);
			throw new NTBException("err.txn.TranscationFaily");
		}

	}

	public void viewDetail() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");

		String transId = getParameter("transId");
		String frequenceType = Utils.null2EmptyWithTrim(this.getParameter("frequenceType"));
		String frequenceDays = Utils.null2EmptyWithTrim(this.getParameter("frequenceDays"));
		String scheduleName = Utils.null2EmptyWithTrim(this.getParameter("scheduleName"));
		String status = Utils.null2EmptyWithTrim(this.getParameter("status"));
		TransferMacau transferMacau = transferService.viewInMacau(transId);

		Map resultData = new HashMap();
		this.convertPojo2Map(transferMacau, resultData);
		
		// add by lw 2011-01-18
		String purpose = transferMacau.getOtherPurpose();
		if(purpose != null && !"".equals(purpose)){
			resultData.put("showPurpose", "true");
			resultData.put("purpose", purpose);
		}
		// add by lw end
		
		// 涓洪敓鏂ゆ嫹椤甸敓鏂ゆ嫹閿熸枻鎷风ずSchedule Transfer閿熸枻鎷烽敓鏂ゆ嫹鎭�
		resultData.put("frequenceType", frequenceType);
		resultData.put("frequenceDays", frequenceDays);
		resultData.put("scheduleName", scheduleName);
		resultData.put("customReturn", ScheduleTransfer.VIEW_FROM_SCHEDULE_LIST);
		if(!status.equals("")){
			resultData.put("status", status);
		}
		setResultData(resultData);
	}

	public void editLoad() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		
		String transID = getParameter("transId");
		String scheduleId = getParameter("scheduleId");

		//閿熷彨璁规嫹閿熻鍑ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鏉冪姸鎬�
		if (schTransferService.isPending(scheduleId)) {
			throw new NTBException("err.bat.OperationPending");
		}
		
		// load閿熸枻鎷穝cheduleTransfer
		ScheduleTransfer scheduleTransfer = schTransferService.loadSchTransfer(scheduleId);
		
		// add by mxl 1212
		String week[] = null;
		if (scheduleTransfer.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			week = scheduleTransfer.getFrequnceDays().split(",");
		}
		TransferMacau transferMacau = transferService.viewInMacau(transID);
		// 閿熷彨璁规嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�
		if (transferMacau.getInputAmountFlag().equals(
				Constants.INPUT_AMOUNT_FLAG_FROM)) {
			transferMacau.setTransferAmount(null);
		} else if (transferMacau.getInputAmountFlag().equals(
				Constants.INPUT_AMOUNT_FLAG_TO)) {
			transferMacau.setDebitAmount(null);
		}		
		Map resultData = new HashMap();
		
		// add by lw 2011-01-27
		//modified by lzg 20190602
		/*String purpose = transferMacau.getOtherPurpose();
		if(purpose != null && !"".equals(purpose)){
			resultData.put("showPurpose", "true");
			resultData.put("purpose", purpose);
		}
		String proofOfPurpose = transferMacau.getProofOfPurpose();
		if(proofOfPurpose != null && !"".equals(proofOfPurpose)){
			if("Y".equals(proofOfPurpose)){
				resultData.put("needProof", "Y");
			}
		}*/
		//modified by lzg end
		// add by lw end
		
		if (scheduleTransfer.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			for (int i = 0; i <= week.length - 1; i++) {
				resultData.put("week" + i, week[i]);
			}
		}
		this.convertPojo2Map(transferMacau, resultData);
		// 涓洪敓鏂ゆ嫹椤甸敓鏂ゆ嫹閿熸枻鎷风ずSchedule Transfer閿熸枻鎷烽敓鏂ゆ嫹鎭�
		resultData.put("frequenceType", scheduleTransfer.getFrequnceType());
		resultData.put("frequenceDays", scheduleTransfer.getFrequnceDays());
		resultData.put("scheduleName", scheduleTransfer.getScheduleName());
		CorpUser corpUser = (CorpUser) this.getUser();
		//modified by lzg 20190708
		//resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("transferuser", transferService.loadAccountForRemittance(corpUser.getCorpId()));
		//modified by lzg end
		resultData.put("selectFromAcct", transferMacau.getFromAccount() + " - "  + transferMacau.getFromCurrency());//add by linrui for mul-ccy
		setResultData(resultData);
		//add by linrui 20180313
		//setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg",Constants.US).getString("warnning.transfer.DifferenceCcy"));
		// 閿熸枻鎷烽敓鐭紮鎷烽敓鏂ゆ嫹閿熷彨杈炬嫹閿熺氮ession閿熸枻鎷烽敓鐨嗘唻鎷穋onfirm閿熸枻鎷峰啓閿熸枻鎷烽敓鏂ゆ嫹鑿橀敓锟�
		this.setUsrSessionDataValue("transferMacau", transferMacau);
		this.setUsrSessionDataValue("scheduleTransfer", scheduleTransfer);
	}

	public void edit() throws NTBException {
		//initial service
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		TransAmountService transAmountService = (TransAmountService) appContext
				.getBean("TransAmountService");

		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = (CorpUser) this.getUser();
		corpUser.setLanguage(lang);//add by linrui for mul-language
		TransferMacau transferMacau = (TransferMacau) this.getUsrSessionDataValue("transferMacau");
		ScheduleTransfer scheduleTransfer = (ScheduleTransfer) this.getUsrSessionDataValue("scheduleTransfer");

		String SELECT_CGDLOCAL = "select CGD_FLAG,BANK_NAME from HS_LOCAL_BANK_CODE where BANK_CODE=? ";
		RcCurrency rcCurrency = new RcCurrency();
		String CGD = "N";
		
		scheduleTransfer.setScheduleName(Utils
				.null2EmptyWithTrim(getParameter("scheduleName")));

		// add by mxl 1008 閿熸枻鎷锋灇閿熺粸寮婎亷鎷烽敓缁炴唻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹
		String dayType = null;
		dayType = Utils.null2EmptyWithTrim(getParameter("dayType"));
		scheduleTransfer.setFrequnceType(dayType);

        // add by hjs 20070723 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓锟�
		if (scheduleTransfer.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			for (int i = 0; i <= getParameterValues("frequenceWeekDays").length - 1; i++) {
				this.getParameters().put("week" + i, getParameterValues("frequenceWeekDays")[i]);
			}
		}
        
		//add by heyj 20190529
		Map reqMap = new HashMap();
		reqMap.putAll(this.getParameters());
		String endDate = DateTime.formatDate(Utils.null2EmptyWithTrim(getParameter("endDate")),"dd/MM/yyyy", "yyyyMMdd");
		
		Date tomorrow = new Date();
		Date oDate = DateTime.getDateFromStr(endDate, "yyyyMMdd");
		java.util.Calendar calendar = new java.util.GregorianCalendar();
		calendar.setTime(tomorrow);
		calendar.add(calendar.DATE,1);
		tomorrow=calendar.getTime(); 
		String tomorrowStr = DateTime.formatDate(tomorrow, "yyyyMMdd");
		tomorrow = DateTime.getDateFromStr(tomorrowStr, "yyyyMMdd");
		long intervalMilli = oDate.getTime() - tomorrow.getTime();
		int diffDays =  (int) (intervalMilli / (24 * 60 * 60 * 1000));
		if (diffDays <= 0){
			 Log.error("Transfer End Date should be later than tomorrow");
		     throw new NTBException("Transfer End Date should be later than tomorrow");
		}
		
		reqMap.put("endDate", endDate);
		this.convertMap2Pojo(reqMap, transferMacau);
		//this.convertMap2Pojo(this.getParameters(), transferMacau);
		
		//add by lzg 20190531
		boolean flag = checkChargeAccount(transferMacau);
		if(!flag){
			throw new NTBWarningException("err.txn.chargeAccountError");
		}
		//add by lzg end
		
		transferMacau.setUserId(corpUser.getUserId());
		transferMacau.setCorpId(corpUser.getCorpId());
		// add by li_zd at 20171117 for mul-language
        Double transAmt = TransAmountFormate.formateAmount(this.getParameter("transferAmount"), lang);
        Double debitAmt = TransAmountFormate.formateAmount(this.getParameter("debitAmount"), lang);
        transferMacau.setTransferAmount(transAmt);
        transferMacau.setDebitAmount(debitAmt);
        // end
//		transferMacau.setExecuteTime(new Date());
//		transferMacau.setRequestTime(new Date());
		
		 //add by mxl 0130 閿熸枻鎷峰�閿熸枻鎷穋hargeAccount,chargeBy
        //modified by lzg 20190603
		//transferMacau.setChargeAccount(transferMacau.getFromAccount());
		//modified by lzg end
		// Jet modified 2007-12-18
//		transferMacau.setChargeBy("O");

		/* add 0814 get currency accounding to account 閿熸枻鎷烽敓鏂ゆ嫹灞庡憰閿熸枻鎷烽敓缁炲彿鐨勪紮鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹 */
//		CorpAccount corpAccount = corpAccountService
//				.viewCorpAccount(transferMacau.getFromAccount());
//		transferMacau.setFromCurrency(corpAccount.getCurrency());

		/* add by mxl 0827 get the currency number according the currency code */
//		CorpAccount corpChargeAccount = corpAccountService
//				.viewCorpAccount(transferMacau.getChargeAccount());
//		String chargeCurrency = corpChargeAccount.getCurrency();
//		transferMacau.setChargeCurrency(chargeCurrency);
		String chargeCurrency = this.getParameter("chargeCurrency");

		String fromCurrencyCode = rcCurrency.getCbsNumberByCcy(transferMacau
				.getFromCurrency());
		String toCurrencyCode = rcCurrency.getCbsNumberByCcy(transferMacau
				.getToCurrency());
		String chareCurrencyCode = rcCurrency.getCbsNumberByCcy(chargeCurrency);

		// get CGD from local_bank_code according to the bank_name
		GenericJdbcDao dao = (GenericJdbcDao) appContext
				.getBean("genericJdbcDao");
		try {
			List idList = null;
			idList = dao.query(SELECT_CGDLOCAL, new Object[] { transferMacau
					.getBeneficiaryBank() });
			Map cdtMap = (Map) idList.get(0);
			CGD = (String) cdtMap.get("CGD_FLAG");
		} catch (Exception e) {
			throw new NTBException("err.txn.GetCGDException");
		}
		
        // 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓绐栴垽鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�edit by mxl 0115
		Map fromHost = transferService.toHostChargeEnquiryNew(transferMacau
				.getTransId(), corpUser, new BigDecimal(transferMacau
				.getTransferAmount().toString()), "MO", CGD, fromCurrencyCode,
				toCurrencyCode, transferMacau.getChargeBy(), chareCurrencyCode,
				"N");
		transferService.uploadEnquiryMacau(transferMacau, fromHost);

		// 閿熸枻鎷烽敓濮愪氦閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓锟絚omputing the charge amount 0912
		double chargeAmount = (new Double(fromHost.get("HANDLING_CHRG_AMT")
				.toString())).doubleValue();		
//				+ (new Double(fromHost.get("COMM_AMT").toString()))
//						.doubleValue()
//				+ (new Double(fromHost.get("TAX_AMOUNT").toString()))
//						.doubleValue()
//				+ (new Double(fromHost.get("SWIFT_CHRG_AMT").toString()))
//						.doubleValue()
//				+ (new Double(fromHost.get("OUR_CHG_AMT").toString()))
//						.doubleValue();
		//update by linrui 20180124
		transferMacau.setHandlingAmount(new Double(fromHost.get(
				"HANDLING_CHRG_AMT").toString()));
		/*transferMacau.setCommissionAmount(new Double(fromHost.get("COMM_AMT")
				.toString()));
		transferMacau.setTaxAmount(new Double(fromHost.get("TAX_AMOUNT")
				.toString()));
		transferMacau.setSwiftAmount(new Double(fromHost.get("SWIFT_CHRG_AMT")
				.toString()));
		transferMacau.setChargeOur(new Double(fromHost.get("OUR_CHG_AMT")
				.toString()));*///comment by linrui 20180124
		transferMacau.setChargeAmount(new Double(chargeAmount));
		
        // 閿熸枻鎷烽敓绲沞bitAmount 閿熸枻鎷�transferAmount涔嬩竴,閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷蜂竴閿熸枻鎷烽敓鏂ゆ嫹閿燂拷
		if (transferMacau.getTransferAmount().doubleValue() == 0) {
            // 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹杞敓缁炴枻鎷烽敓渚ユ唻鎷峰織
			transferMacau.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_FROM);
			BigDecimal transferAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(), new BigDecimal(transferMacau
							.getDebitAmount().toString()), null, 2);

			transferMacau.setTransferAmount(new Double(transferAmount
					.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinAmtOtherBanks(transferMacau.getFromCurrency(), transferMacau.getDebitAmount().doubleValue());
			
			
			// 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓锟絚ompuing the exchange rate 0907
			Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(), 7);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

			// Jet modified. show only one exchange rate at the page, it should be always buy rate/sell rate 
			if (null == buyRate){
				buyRate = new BigDecimal("1");
			}
			if (null == sellRate){
				sellRate = new BigDecimal("1");
			}			
			if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
				exchangeRate = new BigDecimal("0");
			} else {
				exchangeRate = buyRate.divide(sellRate, 7,
						BigDecimal.ROUND_HALF_UP);
			}
			transferMacau.setExchangeRate(new Double(exchangeRate.doubleValue()));
		} else if (transferMacau.getDebitAmount() == null
				|| transferMacau.getDebitAmount().doubleValue() == 0) {

            // 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹杞敓缁炴枻鎷烽敓渚ユ唻鎷峰織
			transferMacau.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_TO);
			BigDecimal toAmount = new BigDecimal(transferMacau
					.getTransferAmount().toString());
			BigDecimal fromAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(),
					null, toAmount, 2);

			transferMacau.setDebitAmount(new Double(fromAmount.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinAmtOtherBanks(transferMacau.getToCurrency(), transferMacau.getTransferAmount().doubleValue());
			
			// 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓锟絚ompuing the exchange rate 0907
			Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(),

			transferMacau.getFromCurrency(), transferMacau.getToCurrency(), 7);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

			// Jet modified. show only one exchange rate at the page, it should be always buy rate/sell rate 
			if (null == buyRate){
				buyRate = new BigDecimal("1");
			}
			if (null == sellRate){
				sellRate = new BigDecimal("1");
			}			
			if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
				exchangeRate = new BigDecimal("0");
			} else {
				exchangeRate = buyRate.divide(sellRate, 7,
						BigDecimal.ROUND_HALF_UP);
			}
			transferMacau.setExchangeRate(new Double(exchangeRate.doubleValue()));
		}

		// 閿熸枻鎷风ず閿熸枻鎷烽敓锟�閿熸枻鎷烽敓鑺傜》鎷烽敓鏂ゆ嫹閿熸枻鎷风枱涓氬閿熺粸鏂ゆ嫹閿熺祴ransferInMacauAction.java閿熷彨璇ф嫹涓�敓鏂ゆ嫹, 閿熸枻鎷锋敞閿熸枻鎷� 2017/7/13 at Li_zd
//		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
//				.getCorpId(), transferMacau.getToCurrency(), "MOP",
//				new BigDecimal(transferMacau.getTransferAmount().toString()),
//				null, 2);

		// 閿熷彨璁规嫹閿熺煫浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹骞曢敓鏂ゆ嫹娑插緬閿熸枻鎷�add by mxl 0922
		String inputAmountFlag = transferMacau.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferMacau.getFromCurrency();
			transferAmount = transferMacau.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferMacau.getToCurrency();
			transferAmount = transferMacau.getTransferAmount();
		}
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), currency, "MOP",
				new BigDecimal(transferAmount.toString()),
				null, 2);
		
		// 閿熸枻鎷烽エ浼欐嫹鎹夐敓鏂ゆ嫹閿熻鍚﹁秴绛规嫹姣忛敓绉告枻鎷烽敓鏂ゆ嫹閿熸纭锋嫹閿熸枻鎷烽敓鏂ゆ嫹
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		if (!transferLimitService.checkLimitQuotaByCorpId1(corpUser.getCorpId(),
				Constants.TXN_TYPE_TRANSFER_MACAU_SCHEDULE, transferMacau.getDebitAmount().doubleValue(),
				equivalentMOP.doubleValue())) {
			// write limit report
			Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", transferMacau.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_MACAU_SCHEDULE);
			reportMap.put("LOCAL_TRANS_CODE", transferMacau.getTransId());
			reportMap.put("FROM_CURRENCY", transferMacau.getFromCurrency());
			reportMap.put("FROM_ACCOUNT", transferMacau.getFromAccount());
			reportMap.put("TO_CURRENCY", transferMacau.getToCurrency());
			reportMap.put("FROM_AMOUNT", transferMacau.getDebitAmount());
			reportMap.put("TO_AMOUNT", transferMacau.getTransferAmount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
			throw new NTBWarningException("err.txn.ExceedDailyLimit");
		}

		// 閿熸枻鎷风ず閿熸枻鎷烽敓锟�
		Map resultData = new HashMap();
		
		//modified by lzg 20190602
		/*int checkPurpose = transferService.checkNeedPurpose(corpUser
				.getCorpId(), transferMacau.getFromAccount(), Utils
				.null2Empty(transferMacau.getTransferAmount()), Utils
				.null2Empty(transferMacau.getDebitAmount()), transferMacau
				.getFromCurrency(), transferMacau.getToCurrency(), "MO");
		if (checkPurpose == 1) {
			transferMacau.setPurposeCode("99");
			transferMacau.setProofOfPurpose("N");
			if(transferMacau.getOtherPurpose() == null 
					|| "".equals(transferMacau.getOtherPurpose())){
				throw new NTBWarningException("err.txn.NullPurpose");
			}
		} else if (checkPurpose == 2) {
			transferMacau.setPurposeCode("99");
			transferMacau.setProofOfPurpose("Y");
			if(transferMacau.getOtherPurpose() == null 
					|| "".equals(transferMacau.getOtherPurpose())){
				throw new NTBWarningException("err.txn.NullPurpose");
			}
		} else {
			transferMacau.setProofOfPurpose("N");
			transferMacau.setPurposeCode("");
			transferMacau.setOtherPurpose("");
		}
		String purpose = transferMacau.getOtherPurpose();
		if(purpose != null && !"".equals(purpose)){
			resultData.put("showPurpose", "true");
			resultData.put("purpose", purpose);
		}*/
		//modified by lzg end

		// add by mxl 0821
		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		resultData.put("txnType", Constants.TXN_SUBTYPE_SCHEDULE_TXN_EDIT);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);

		// 涓洪敓鏂ゆ嫹椤甸敓鏂ゆ嫹閿熸枻鎷风ずSchedule Transfer閿熸枻鎷烽敓鏂ゆ嫹鎭�
        // edit by mxl 1214閿熸枻鎷烽敓闃额亷鎷烽敓缁炴唻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓閰典紮鎷锋灇閿熺粸寮婎亷鎷烽敓缁炴唻鎷烽敓锟�
		if (dayType.equals(ScheduleTransfer.DAILY)) {
			scheduleTransfer.setFrequnceDays("");
		} else if (dayType.equals(ScheduleTransfer.WEEKLY)) {
			String[] frequenceWeekDays = this
					.getParameterValues("frequenceWeekDays");
			String frequenceDays = frequenceWeekDays[0];
			if (frequenceWeekDays.length == 1) {
				scheduleTransfer.setFrequnceDays(frequenceDays);
			} else {
				for (int i = 1; i <= frequenceWeekDays.length - 1; i++) {
					frequenceDays = frequenceDays + "," + frequenceWeekDays[i];
					scheduleTransfer.setFrequnceDays(frequenceDays);
				}
			}
		} else if (dayType.equals(ScheduleTransfer.MONTHLY)) {
			String monthType = Utils
					.null2EmptyWithTrim(getParameter("month_type"));
			if (monthType.equals("1")) {
				scheduleTransfer.setFrequnceDays("1");
			} else if (monthType.equals("-1")) {
				scheduleTransfer.setFrequnceDays("-1");
			} else if (monthType.equals("0")) {
				String frequenceDays = Utils
						.null2EmptyWithTrim(getParameter("designed_day"));
				scheduleTransfer.setFrequnceDays(frequenceDays);
			}
		} else if (dayType.equals(ScheduleTransfer.DAYS_PER_MONTH)) {
			String frequenceDays = Utils
					.null2EmptyWithTrim(getParameter("days_per_month"));
			// add by hjs 2006-12-14
			schTransferService.checkSeparator(frequenceDays);
			// add by hjs 20070207
			frequenceDays = schTransferService.sortFrequenceDays(frequenceDays);
			
			scheduleTransfer.setFrequnceDays(frequenceDays);
		}

		resultData.put("frequenceType", scheduleTransfer.getFrequnceType());
		resultData.put("frequenceDays", scheduleTransfer.getFrequnceDays());
		resultData.put("scheduleName", scheduleTransfer.getScheduleName());
		
		this.convertPojo2Map(transferMacau, resultData);
//		resultData.put("transferDate", transferMacau.getTransferDate());

		ApproveRuleService approveRuleService = (ApproveRuleService) Config
				.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
				resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}

		this.setResultData(resultData);

		// 閿熸枻鎷烽敓鐭紮鎷烽敓鏂ゆ嫹閿熷彨杈炬嫹閿熺氮ession閿熸枻鎷烽敓鐨嗘唻鎷穋onfirm閿熸枻鎷峰啓閿熸枻鎷烽敓鏂ゆ嫹鑿橀敓锟�
		this.setUsrSessionDataValue("transferMacau", transferMacau);
		this.setUsrSessionDataValue("scheduleTransfer", scheduleTransfer);
	}

    public void editCancel() throws NTBException {
		TransferMacau transferMacau = (TransferMacau) this.getUsrSessionDataValue("transferMacau");
		ScheduleTransfer scheduleTransfer = (ScheduleTransfer) this.getUsrSessionDataValue("scheduleTransfer");
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		
		// add by mxl 1212
		String week[] = null;
		if (scheduleTransfer.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			week = scheduleTransfer.getFrequnceDays().split(",");
		}

		// 閿熷彨璁规嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�
		if (transferMacau.getInputAmountFlag().equals(
				Constants.INPUT_AMOUNT_FLAG_FROM)) {
			transferMacau.setTransferAmount(null);
		} else if (transferMacau.getInputAmountFlag().equals(
				Constants.INPUT_AMOUNT_FLAG_TO)) {
			transferMacau.setDebitAmount(null);
		}
		
		Map resultData = new HashMap();
		if (scheduleTransfer.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			for (int i = 0; i <= week.length - 1; i++) {
				resultData.put("week" + i, week[i]);
			}
		}
		this.convertPojo2Map(transferMacau, resultData);
		// 涓洪敓鏂ゆ嫹椤甸敓鏂ゆ嫹閿熸枻鎷风ずSchedule Transfer閿熸枻鎷烽敓鏂ゆ嫹鎭�
		resultData.put("frequenceType", scheduleTransfer.getFrequnceType());
		resultData.put("frequenceDays", scheduleTransfer.getFrequnceDays());
		resultData.put("scheduleName", scheduleTransfer.getScheduleName());
		// add by hjs 20070723
		if(scheduleTransfer.getFrequnceType().equals(ScheduleTransfer.MONTHLY)){
			resultData.put("month_type", scheduleTransfer.getFrequnceDays());
		}
		CorpUser corpUser = (CorpUser) this.getUser();
		//modified by lzg 20190708
		//resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("transferuser", transferService.loadAccountForRemittance(corpUser.getCorpId()));
		//modified by lzg end
		resultData.put("selectFromAcct", transferMacau.getFromAccount() + " - "  + transferMacau.getFromCurrency());//add by linrui for mul-ccy
		setResultData(resultData);
    }

	public void editConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");

		CorpUser corpUser = (CorpUser) this.getUser();
		TransferMacau transferMacau = (TransferMacau) this
				.getUsrSessionDataValue("transferMacau");
		ScheduleTransfer schTransfer = (ScheduleTransfer) this
				.getUsrSessionDataValue("scheduleTransfer");
		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));

		//hjs
		String seqNo = CibIdGenerator.getIdForOperation("SCHEDULE_TRANSFER_HIS");
		// 閿熼摪鏂ゆ嫹涓�敓鏂ゆ嫹閿熸枻鎷锋潈閿熸枻鎷烽敓鏂ゆ嫹
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferMacau.getToCurrency(), "MOP",
				new BigDecimal(transferMacau.getTransferAmount().toString()),
				null, 2);
		String inputAmountFlag = transferMacau.getInputAmountFlag();
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_SCHEDULE_TXN_EDIT,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				ScheduleTransferMacauAction.class, 
				transferMacau.getFromCurrency(), 
				transferMacau.getDebitAmount().doubleValue(), 
				transferMacau.getToCurrency(),
				transferMacau.getTransferAmount().doubleValue(), 
				equivalentMOP.doubleValue(), 
				seqNo, transferMacau.getRemark(), corpUser, assignedUser, 
				corpUser.getCorporation().getAllowExecutor(), inputAmountFlag);

		try {
			//hjs
			transferMacau.setExecuteTime(new Date());
			transferMacau.setRequestTime(new Date());
			transferMacau.setTransId(CibIdGenerator.getRefNoForTransaction());
			transferMacau.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			transferMacau.setStatus(Constants.STATUS_PENDING_APPROVAL);
			transferMacau.setOperation(Constants.OPERATION_UPDATE);

			// 閿熸枻鎷烽敓绲奵hedule Transfer 閿熺潾闈╂嫹涓�敓鏂ゆ嫹閿熸枻鎷峰綍
			schTransfer.setTransId(transferMacau.getTransId());
			schTransfer.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			schTransfer.setStatus(Constants.STATUS_PENDING_APPROVAL);
			schTransfer.setOperation(Constants.OPERATION_UPDATE);
			
			//add by long_zg 2014-05-06 for CR188
			schTransfer.setLastUpdateTime(new Date()) ;
			
			//hjs
			ScheduleTransferHis schTransferHis = new ScheduleTransferHis();
	        try {
	            BeanUtils.copyProperties(schTransferHis, schTransfer);
	        } catch (Exception e) {
	            Log.error("Error copy properties", e);
	            throw new NTBException("err.sys.GeneralError");
	        }
	        schTransferHis.setSeqNo(seqNo);
			schTransferService.addSchTransferHis(schTransferHis);
			schTransferService.addTransferMacau(transferMacau);

			// 閿熷彨璁规嫹閿熺煫浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹骞曢敓鏂ゆ嫹娑插緬閿熸枻鎷�add by mxl 0922
			String currency = null;
			Double transferAmount = new Double(0);
			if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferMacau.getFromCurrency();
				transferAmount = transferMacau.getDebitAmount();
			} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferMacau.getToCurrency();
				transferAmount = transferMacau.getTransferAmount();
			}

			Map resultData = new HashMap();
			
			// add by lw 2011-01-27
			String purpose = transferMacau.getOtherPurpose();
			if(purpose != null && !"".equals(purpose)){
				resultData.put("showPurpose", "true");
				resultData.put("purpose", purpose);
			}
			// add by lw end
			
			// add by mxl 0821
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");
			
			resultData.put("txnType", Constants.TXN_SUBTYPE_SCHEDULE_TXN_EDIT);
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);

			// 涓洪敓鏂ゆ嫹椤甸敓鏂ゆ嫹閿熸枻鎷风ずSchedule Transfer閿熸枻鎷烽敓鏂ゆ嫹鎭�
			resultData.put("frequenceType", schTransferHis.getFrequnceType());
			resultData.put("frequenceDays", schTransferHis.getFrequnceDays());
			resultData.put("scheduleName", schTransferHis.getScheduleName());

			this.convertPojo2Map(transferMacau, resultData);

			ApproveRuleService approveRuleService = (ApproveRuleService) Config
					.getAppContext().getBean("ApproveRuleService");
			if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
					resultData)) {
				throw new NTBException("err.flow.ApproveRuleNotDefined");
			}

			setResultData(resultData);
			resultData.put("transferDate", transferMacau.getTransferDate());
			// 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸纭锋嫹 add by mxl 0928
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", transferMacau.getRequestTime());
			dataMap.put("transId", transferMacau.getTransId());
			dataMap.put("toAmount", transferMacau.getTransferAmount());
			dataMap.put("corpName", corpUser.getCorporation().getCorpName());
			dataMap.put("toAccount", transferMacau.getToAccount());
			dataMap.put("beneficiaryAccName", transferMacau.getBeneficiaryName1());
			dataMap.put("beneficiaryBank", transferMacau.getBeneficiaryBank());
			dataMap.put("transId", transferMacau.getTransId());
			dataMap.put("remark", transferMacau.getRemark());
			
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_EDIT, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_EDIT, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			throw new NTBException("err.txn.TranscationFaily");
		}
	}

	public boolean approve(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		CorpUser corpUser = (CorpUser) bean.getUser();

		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		if (txnType != null) {
			// load閿熸枻鎷穝cheduleTransferHis
			ScheduleTransferHis schTransferHis = schTransferService.loadSchTransferHis(id);

			if (txnType.equals(Constants.TXN_SUBTYPE_SCHEDULE_TXN_NEW)) {
				schTransferHis.setStatus(Constants.STATUS_NORMAL);
				schTransferHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			} else if (txnType.equals(Constants.TXN_SUBTYPE_SCHEDULE_TXN_EDIT)) {
				schTransferHis.setStatus(Constants.STATUS_NORMAL);
				schTransferHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			} else if (txnType.equals(Constants.TXN_SUBTYPE_SCHEDULE_TXN_REMOVE)) {
				schTransferHis.setStatus(Constants.STATUS_REMOVED);
				schTransferHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			} else if (txnType.equals(Constants.TXN_SUBTYPE_SCHEDULE_TXN_BLOCK)) {
				schTransferHis.setStatus(Constants.STATUS_BLOCKED);
				schTransferHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			} else if (txnType.equals(Constants.TXN_SUBTYPE_SCHEDULE_TXN_UNBLOCK)) {
				schTransferHis.setStatus(Constants.STATUS_NORMAL);
				schTransferHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			}
			ScheduleTransfer schTransfer = schTransferService.loadSchTransfer(schTransferHis.getScheduleId());
			
			//add by long_zg 2014-05-06 for CR188
			schTransferHis.setLastUpdateTime(new Date()) ;
			
            try {
                BeanUtils.copyProperties(schTransfer, schTransferHis);
            } catch (Exception e) {
                Log.error("Error copy properties", e);
                throw new NTBException("err.sys.GeneralError");
            }
			schTransferService.updateSchTransfer(schTransfer);
			schTransferService.updateSchTransferHis(schTransferHis);
			
			// 閿熸枻鎷烽敓绲猚heduleId閿熺煫绛规嫹transId,load閿熸枻鎷穞ransferMacau
			TransferMacau transferMacau = transferService.viewInMacau(schTransferHis.getTransId());
			if (txnType.equals(Constants.TXN_SUBTYPE_SCHEDULE_TXN_NEW)) {
				transferMacau.setStatus(Constants.STATUS_NORMAL);
				transferMacau.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			} else if (txnType.equals(Constants.TXN_SUBTYPE_SCHEDULE_TXN_EDIT)) {
				transferMacau.setStatus(Constants.STATUS_NORMAL);
				transferMacau.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			} else if (txnType.equals(Constants.TXN_SUBTYPE_SCHEDULE_TXN_REMOVE)) {
				transferMacau.setStatus(Constants.STATUS_REMOVED);
				transferMacau.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			} else if (txnType.equals(Constants.TXN_SUBTYPE_SCHEDULE_TXN_BLOCK)) {
				transferMacau.setStatus(Constants.STATUS_BLOCKED);
				transferMacau.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			} else if (txnType.equals(Constants.TXN_SUBTYPE_SCHEDULE_TXN_UNBLOCK)) {
				transferMacau.setStatus(Constants.STATUS_NORMAL);
				transferMacau.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			}
			transferMacau.setExecuteTime(new Date());
			// add by mxl 0921 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓閾颁紮鎷烽敓鏂ゆ嫹
			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(), 7);*/
			Map exchangeMap = exRatesService.getExchangeRateFromHost(corpUser
					.getCorpId(), transferMacau.getFromCurrency(),
					transferMacau.getToCurrency(), 7);
			String rateType = (String) exchangeMap.get("rateType");
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
			BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

			// Jet modified. show only one exchange rate at the page, it should be always buy rate/sell rate 
			if (null == buyRate){
				buyRate = new BigDecimal("1");
			}
			if (null == sellRate){
				sellRate = new BigDecimal("1");
			}			
			if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
				exchangeRate = new BigDecimal("0");
			} else {
				exchangeRate = buyRate.divide(sellRate, 7,
						BigDecimal.ROUND_HALF_UP);
			}
			
			transferMacau
					.setExchangeRate(new Double(exchangeRate.doubleValue()));
			transferService.updateMacau(transferMacau);
			  // add by mxl 0201 鍐欓敓鏂ゆ嫹閿熸枻鎷�
			schTransferService.uploadSchTransferInMacauReprot(schTransfer,transferMacau);
			return true;
		} else {
			return false;
		}
	}

	public boolean cancel(String txnType, String id, CibAction bean)
			throws NTBException {
		return reject(txnType, id, bean);
	}

	public boolean reject(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		if (txnType != null) {
			// load閿熸枻鎷稴cheduleTransferHis
			ScheduleTransferHis schTransferHis = schTransferService.loadSchTransferHis(id);
			schTransferHis.setStatus(Constants.STATUS_REMOVED);
			schTransferHis.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
			schTransferService.updateSchTransferHis(schTransferHis);
			return true;
		} else {
			return false;
		}
	}

	public String viewDetail(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		
		CorpUser corpUser = (CorpUser) bean.getUser();
		
		// load閿熸枻鎷稴cheduleTransferHis
		ScheduleTransferHis schTransferHis = schTransferService.loadSchTransferHis(id);
		// 閿熸枻鎷烽敓绲猚heduleId閿熺煫绛规嫹transId,load閿熸枻鎷穞ransferMacau
		TransferMacau transferMacau = transferService.viewInMacau(schTransferHis.getTransId());
		
		Map resultData = bean.getResultData();
		bean.convertPojo2Map(transferMacau, resultData);

		// add by mxl 0921 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓閾颁紮鎷烽敓鏂ゆ嫹
		Map exchangeMap = exRatesService.getExchangeRate(corpUser.getCorpId(),
				transferMacau.getFromCurrency(), transferMacau.getToCurrency(),
				7);
		String rateType = (String) exchangeMap.get("rateType");
		BigDecimal exchangeRate = new BigDecimal("0");
		BigDecimal buyRate = (BigDecimal) exchangeMap.get("buyRate");
		BigDecimal sellRate = (BigDecimal) exchangeMap.get("sellRate");

		// Jet modified. show only one exchange rate at the page, it should be always buy rate/sell rate 
		if (null == buyRate){
			buyRate = new BigDecimal("1");
		}
		if (null == sellRate){
			sellRate = new BigDecimal("1");
		}			
		if (rateType.equals(ExchangeRatesDao.RATE_TYPE_NO_RATE)) {
			exchangeRate = new BigDecimal("0");
		} else {
			exchangeRate = buyRate.divide(sellRate, 7,
					BigDecimal.ROUND_HALF_UP);
		}
		
		transferMacau.setExchangeRate(new Double(exchangeRate.doubleValue()));
		// transferService.updateMacau(transferMacau);

		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferMacau.getToCurrency(), "MOP",
				new BigDecimal(transferMacau.getTransferAmount().toString()),
				null, 2);

		// 閿熷彨璁规嫹閿熺煫浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹骞曢敓鏂ゆ嫹娑插緬閿熸枻鎷�add by mxl 0922
		String inputAmountFlag = transferMacau.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferMacau.getFromCurrency();
			transferAmount = transferMacau.getDebitAmount();
			
			//jet modified for showing new transaction amount
			Double new_From_Amount = transferMacau.getDebitAmount();			
			BigDecimal new_To_Amount_temp = exRatesService.getEquivalent(
					corpUser.getCorpId(), currency,
					transferMacau.getToCurrency(), new BigDecimal(transferAmount
							.toString()), null, 2);
			Double new_To_Amount = new Double(new_To_Amount_temp.toString());
			resultData.put("newFromAmount", new_From_Amount);
			resultData.put("newToAmount", new_To_Amount);

		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferMacau.getToCurrency();
			transferAmount = transferMacau.getTransferAmount();
			//jet modified for showing new transaction amount
			Double new_To_Amount = transferMacau.getTransferAmount();			
			BigDecimal new_From_Amount_temp = exRatesService.getEquivalent(
					corpUser.getCorpId(), transferMacau.getFromCurrency(),
					currency, null, new BigDecimal(transferAmount.toString()),
					2);		
			Double new_From_Amount = new Double(new_From_Amount_temp.toString());
			resultData.put("newFromAmount", new_From_Amount);
			resultData.put("newToAmount", new_To_Amount);

		}

		// edit by mxl 0819
		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");
		
		// add by lw 2011-01-18
		//modified by lzg 20190602
		/*String purpose = transferMacau.getOtherPurpose();
		if(purpose != null && !"".equals(purpose)){
			resultData.put("showPurpose", "true");
			resultData.put("purpose", purpose);
		}
		String proofOfPurpose = transferMacau.getProofOfPurpose();
		if(proofOfPurpose != null && !"".equals(proofOfPurpose)){
			if("Y".equals(proofOfPurpose)){
				resultData.put("needProof", "Y");
			}
		}*/
		//modified by lzg 20190602
		// add by lw end

		resultData.put("txnType", txnType);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);
		// add by mxl 0930 閿熸枻鎷风ず閿熸枻鎷烽敓閾颁紮鎷烽敓鏂ゆ嫹
		resultData.put("newExchangeRate", exchangeRate);
		resultData.put("frequenceType", schTransferHis.getFrequnceType());
		resultData.put("frequenceDays", schTransferHis.getFrequnceDays());
		resultData.put("scheduleName", schTransferHis.getScheduleName());
		bean.setResultData(resultData);
		return "/WEB-INF/pages/bat/schedule_transfer/schTransfer_InMacau_approval_view.jsp";
	}
}
