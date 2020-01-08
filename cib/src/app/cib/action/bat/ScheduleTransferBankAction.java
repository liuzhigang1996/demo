package app.cib.action.bat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBWarningException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bat.ScheduleTransfer;
import app.cib.bo.bat.ScheduleTransferHis;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TransferBank;
import app.cib.bo.txn.TransferMacau;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
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

public class ScheduleTransferBankAction extends CibAction implements Approvable {
	
	public void list() throws NTBException {
		setResultData(new HashMap(1));
		CorpUser user = (CorpUser) this.getUser();

		ApplicationContext appContext = Config.getAppContext();
		SchTransferService schTransferService = (SchTransferService) appContext.getBean("SchTransferService");
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		
		//hjs
		/* Add by long_zg 2019-05-22 UAT6-246 COB锛氭柊澧炵殑绗笁鑰呰綁璩湭瀹屽叏鏀瑰ソ  begin */
		String ownerAccFlag = this.getParameter("ownerAccFlag") ;
		String tranType = Constants.NO.equalsIgnoreCase(ownerAccFlag)? TransferConstants.TRANSFER_TYPE_MDB : TransferConstants.TRANSFER_TYPE_BANK ;
		/* Add by long_zg 2019-05-22 UAT6-246 COB锛氭柊澧炵殑绗笁鑰呰綁璩湭瀹屽叏鏀瑰ソ  end */
		/* Modify by long_zg 2019-05-22 UAT6-246 COB锛氭柊澧炵殑绗笁鑰呰綁璩湭瀹屽叏鏀瑰ソ  begin */
		/*List schTransferList = schTransferService.listSchTransfer(TransferConstants.TRANSFER_TYPE_BANK, user.getCorpId());*/
		List schTransferList = schTransferService.listSchTransfer(tranType, user.getCorpId());
		/* Modify by long_zg 2019-05-22 UAT6-246 COB锛氭柊澧炵殑绗笁鑰呰綁璩湭瀹屽叏鏀瑰ソ  end */
		schTransferList = this.convertPojoList2MapList(schTransferList);
		String recordFlag = null;
		if (schTransferList.size() == 0) {
			recordFlag = "Y";
		}
		Map row = null;
		TransferBank transferBank = null;
		String status = "", authStatus = "", operation = "";
		for (int i = 0; i < schTransferList.size(); i++) {
			row = (Map) schTransferList.get(i);
			status = row.get("status").toString();
			authStatus = row.get("authStatus").toString();
			operation = row.get("operation").toString();
			transferBank = transferService.viewInBANK(row.get("transId").toString());
			convertPojo2Map(transferBank, row);
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
		// 璁剧疆绌虹殑 ResultData 娓呯┖鏄剧ず鏁版嵁
//		mod by lq 20171123
//		setMessage("2 working days are required for a new schedule transfer record to take effect");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg",lang+"").getString("warnning.payroll.ScheduleTransfer"));
        
		HashMap resultData = new HashMap(1);
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		
		setResultData(resultData);
//		setResultData(new HashMap(1));
		
	}

	public void add() throws NTBException {
		// 鍒濆鍖朣ervice
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
		// 鍒濆鍖�POJO
		CorpUser corpUser = (CorpUser) this.getUser();
		String corpId = corpUser.getCorpId();
		
		corpUser.setLanguage(lang);//add by linrui for mul-language
		TransferBank transferBank = new TransferBank();
	   
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
		this.convertMap2Pojo(reqMap, transferBank);
		//this.convertMap2Pojo(this.getParameters(), transferBank);
		//end by heyj 20190529
		
		transferBank.setUserId(corpUser.getUserId());
		transferBank.setCorpId(corpUser.getCorpId());
		// add by li_zd at 20171117 for mul-language
        Double transAmt = TransAmountFormate.formateAmount(this.getParameter("transferAmount"), lang);
        Double debitAmt = TransAmountFormate.formateAmount(this.getParameter("debitAmount"), lang);
        transferBank.setTransferAmount(transAmt);
        transferBank.setDebitAmount(debitAmt);
        // end

		// 鍒濆鍖�ScheduleTransfer
		//hjs
		ScheduleTransferHis schTransferHis = new ScheduleTransferHis();
		schTransferHis.setUserId(corpUser.getUserId());
		schTransferHis.setCorporaitonId(corpUser.getCorpId());
		schTransferHis.setBeneficiaryType(TransferConstants.TRANSFER_TYPE_BANK);
		schTransferHis.setTransId(transferBank.getTransId());
		schTransferHis.setScheduleName(Utils.null2EmptyWithTrim(getParameter("scheduleName")));
		
		schTransferHis.setEndDate(endDate); //add by heyj 010529
        
		//add by mxl 1008 鑾峰緱瀹氭椂杞笎鏃堕棿鐨勭被鍨�
		String dayType = null;
		dayType = Utils.null2EmptyWithTrim(getParameter("dayType"));
		schTransferHis.setFrequnceType(dayType);

        // add by hjs 20070723 鍥炴樉鏁版嵁
		if (schTransferHis.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			for (int i = 0; i <= getParameterValues("frequenceWeekDays").length - 1; i++) {
				this.getParameters().put("week" + i, getParameterValues("frequenceWeekDays")[i]);
			}
		}
		
		// From Account
		CorpAccount corpAccount = corpAccountService
				.viewCorpAccount(transferBank.getFromAccount());
//		transferBank.setFromCurrency(corpAccount.getCurrency());
		transferBank.setFromAcctype(corpAccount.getAccountType());

		String ownerAccFlag = this.getParameter("ownerAccFlag") ;
		String toName = "";
		if (Constants.NO.equalsIgnoreCase(ownerAccFlag)) {
		/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */

			/* Modify by long_zg 2015-05-25 UAT6-292 COB锛氱涓夎�杞夎超寰炴牳蹇冩嬁鎴跺悕閷锛屽皫鑷翠氦鏄撳牨CM1013 begin */
			/*toName = this.getParameter("toName");	*/
			Map curMap = transferService.viewCurrentDetail(transferBank.getToAccount()) ;
			toName = (String ) curMap.get("SHORT_NAME") ;
			transferBank.setToName(toName);
			/* Modify by long_zg 2015-05-25 UAT6-292 COB锛氱涓夎�杞夎超寰炴牳蹇冩嬁鎴跺悕閷锛屽皫鑷翠氦鏄撳牨CM1013 begin */
			
			Map fromHost = transferService.toHostAccountInBANK(transferBank
					.getTransId(), corpUser, transferBank.getToAccount());
			
			String toCurrency = Utils.null2EmptyWithTrim(this.getParameter("toCurrency"));
			String toAccType = Utils.null2EmptyWithTrim(fromHost.get("PRODUCT_TYPE"));

			if(toAccType.length()>=2){	//add by zzh 20180224 for to host accountType
				toAccType=toAccType.substring(0, 2);
			}
			transferBank.setToAcctype(toAccType);
			RcCurrency rcCurrency = new RcCurrency();
			transferBank.setToCurrency(toCurrency);

			/* Add by long_zg 2019-05-22 UAT6-246 COB锛氭柊澧炵殑绗笁鑰呰綁璩湭瀹屽叏鏀瑰ソ  begin */
			schTransferHis.setBeneficiaryType(TransferConstants.TRANSFER_TYPE_MDB) ;
			/* Add by long_zg 2019-05-22 UAT6-246 COB锛氭柊澧炵殑绗笁鑰呰綁璩湭瀹屽叏鏀瑰ソ  end */
		} else {
			//add by wcc 20190402
			toName = transferService.getBenName(corpId);
			transferBank.setToName(toName);
			//end
			/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			/*transferBank.setToAccount(toAccount);*/
			/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			corpAccount = corpAccountService.viewCorpAccount(transferBank
					.getToAccount());
//			transferBank.setToCurrency(corpAccount.getCurrency());
			transferBank.setToAcctype(corpAccount.getAccountType());
			
			/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			/*transferBank.setOwnerAccFlag("Y");*/
			transferBank.setOwnerAccFlag(Constants.YES);
			schTransferHis.setBeneficiaryType(TransferConstants.TRANSFER_TYPE_BANK) ;
			/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		}
			
		//add by lzg 20190708
		transferService.checkConvertibility(transferBank.getFromCurrency(), transferBank.getToCurrency());
		//add by lzg end

		if (transferBank.getTransferAmount().doubleValue() == 0) {
			transferBank.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_FROM);
			transferBank.setFlag("1");
			BigDecimal transferAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), new BigDecimal(transferBank
					.getDebitAmount().toString()), null, 2);


			transferBank.setTransferAmount(new Double(transferAmount.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinTransAmt(transferBank.getFromCurrency(), transferBank.getDebitAmount().doubleValue());
			
			
			// add by mxl 0907 get the exchange rate
			Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), 7);
			String rateType = (String) exchangeMap
					.get(ExchangeRatesDao.RATE_TYPE);
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap
					.get(ExchangeRatesDao.BUY_RATE);
			BigDecimal sellRate = (BigDecimal) exchangeMap
					.get(ExchangeRatesDao.SELL_RATE);

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
			transferBank.setExchangeRate(new Double(exchangeRate.doubleValue()));

		} else if (transferBank.getDebitAmount().doubleValue() == 0) {
			transferBank.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_TO);
			transferBank.setFlag("2");
			BigDecimal toAmount = new BigDecimal(transferBank
					.getTransferAmount().toString());
			BigDecimal fromAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), null, toAmount, 2);

			transferBank.setDebitAmount(new Double(fromAmount.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinTransAmt(transferBank.getToCurrency(), transferBank.getTransferAmount().doubleValue());
			
			// add by mxl 0907 get the exchange rate
			Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), 7);
			String rateType = (String) exchangeMap
					.get(ExchangeRatesDao.RATE_TYPE);
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap
					.get(ExchangeRatesDao.BUY_RATE);
			BigDecimal sellRate = (BigDecimal) exchangeMap
					.get(ExchangeRatesDao.SELL_RATE);

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
			transferBank.setExchangeRate(new Double(exchangeRate.doubleValue()));
		}

		// 鏄剧ず鏁版嵁, 鐢变簬璁＄畻婢抽棬甯佺殑鏂瑰紡涓嶵ransferInBnuAction.java涓笉涓�嚧, 鏁呮敞閲� 2017/7/13 at Li_zd
//		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
//				.getCorpId(), transferBnu.getToCurrency(), "MOP",
//				new BigDecimal(transferBnu.getTransferAmount().toString()),
//				null, 2);
		
		// 鍒ゆ柇鐢ㄦ埛杈撳叆鐨勮揣甯佸拰閲戦 add by mxl 0922
		String inputAmountFlag = transferBank.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferBank.getFromCurrency();
			transferAmount = transferBank.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferBank.getToCurrency();
			transferAmount = transferBank.getTransferAmount();
		}
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), currency, "MOP",
				new BigDecimal(transferAmount.toString()),
				null, 2);
		
		// 妫�祴浜ゆ槗閲戦鏄惁瓒呭嚭姣忔棩浜ゆ槗绱涓婇檺
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		if (!transferLimitService.checkLimitQuotaByCorpId1(corpUser.getCorpId(),
				Constants.TXN_TYPE_TRANSFER_BANK_SCHEDULE, transferBank.getDebitAmount().doubleValue(),
				equivalentMOP.doubleValue())) {
            //write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", transferBank.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_BANK_SCHEDULE);
			reportMap.put("LOCAL_TRANS_CODE", transferBank.getTransId());
			reportMap.put("FROM_CURRENCY", transferBank.getFromCurrency());
			reportMap.put("TO_CURRENCY", transferBank.getToCurrency());
			reportMap.put("FROM_AMOUNT", transferBank.getDebitAmount());
			reportMap.put("TO_AMOUNT", transferBank.getTransferAmount());
			reportMap.put("FROM_ACCOUNT", transferBank.getFromAccount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
			throw new NTBWarningException("err.txn.ExceedDailyLimit");
		}

		// 涓篠electUser Tag鍑嗗鏁版嵁
		Map resultData = new HashMap();
		this.convertPojo2Map(transferBank, resultData);
		this.setResultData(resultData);
		//add by lzg 20190522
		resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		//add by lzg end
		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		resultData.put("txnType", Constants.TXN_SUBTYPE_SCHEDULE_TXN_NEW);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);
        
		resultData.put("frequenceType", schTransferHis.getFrequnceType());
		resultData.put("frequenceDays", schTransferHis.getFrequnceDays());
		resultData.put("scheduleName", schTransferHis.getScheduleName());

		/* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		/*if (transferBank.getOwnerAccFlag().equals("N")) {
        	toAccount2 = transferBank.getToAccount();
        }
        resultData.put("toAccount2", toAccount2);*/
        /* Modify by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
		
		// edit by mxl 1214鏍规嵁杞笎鏃堕棿鐨勭被鍨嬭幏寰楀畾鏃惰浆甯愭椂闂�
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
		
		resultData.put("frequenceDays", schTransferHis.getFrequnceDays());
		
		if (schTransferHis.getFrequnceType().equals(
				ScheduleTransfer.DAYS_PER_MONTH)) {
			resultData
					.put("days_per_month", schTransferHis.getFrequnceDays());
		}

		ApproveRuleService approveRuleService = (ApproveRuleService) Config
				.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
				resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}
        
		String week[] = null;
		if (schTransferHis.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			week = schTransferHis.getFrequnceDays().split(",");
			for (int i = 0; i <= week.length - 1; i++) {
				resultData.put("week" + i, week[i]);
			}
		}
		// 濡傛灉From Account = To Account, 鍒欓敊
		if (transferBank.getFromAccount().equals(transferBank.getToAccount())&&transferBank.getFromCurrency().equals(transferBank.getToCurrency())) {
			throw new NTBException("err.txn.TransferAccError");
		}
		
		// 灏嗙敤鎴锋暟鎹啓鍏ession锛屼互渚縞onfirm鍚庡啓鍏ユ暟鎹簱
		this.setUsrSessionDataValue("transferBank", transferBank);
		this.setUsrSessionDataValue("schTransferHis", schTransferHis);
	}

	public void addConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		
		CorpUser corpUser = (CorpUser) getUser();

		TransferBank transferBank = (TransferBank) this.getUsrSessionDataValue("transferBank");
		//hjs
		ScheduleTransferHis schTransferHis = (ScheduleTransferHis) this.getUsrSessionDataValue("schTransferHis");
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferBank.getToCurrency(), "MOP",
				new BigDecimal(transferBank.getTransferAmount().toString()),
				null, 2);
		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));
		
		//hjs
		String seqNo = CibIdGenerator.getIdForOperation("SCHEDULE_TRANSFER_HIS");

		// 鏂板缓涓�」鎺堟潈浠诲姟
		String inputAmountFlag = transferBank.getInputAmountFlag();
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_SCHEDULE_TXN_NEW,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				ScheduleTransferBankAction.class, transferBank.getFromCurrency(),
				transferBank.getDebitAmount().doubleValue(), transferBank
						.getToCurrency(), transferBank.getTransferAmount()
						.doubleValue(), equivalentMOP.doubleValue(),
						seqNo, transferBank.getRemark(),
						getUser(), assignedUser, corpUser.getCorporation()
						.getAllowExecutor(), inputAmountFlag);

		try {
			// 鏈�悗涓�鎵嶅啓鍏ユ暟鎹簱
			transferBank.setTransId(CibIdGenerator.getRefNoForTransaction());
			transferBank.setExecuteTime(new Date());
			transferBank.setRequestTime(new Date());
			transferBank.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			transferBank.setStatus(Constants.STATUS_PENDING_APPROVAL);
			transferBank.setOperation(Constants.OPERATION_NEW);
			//缁欒〃Schedule Transfer 澧炲姞涓�潯璁板綍
			schTransferHis.setScheduleId(CibIdGenerator.getRefNoForTransaction());
			schTransferHis.setTransId(transferBank.getTransId());
			schTransferHis.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			schTransferHis.setStatus(Constants.STATUS_PENDING_APPROVAL);
			schTransferHis.setOperation(Constants.OPERATION_NEW);
			
			//add by long_zg 2014-05-06 for CR188
			schTransferHis.setLastUpdateTime(new Date()) ;
			
			//hjs
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
			schTransferService.addTransferBank(transferBank);

			// 鍒ゆ柇鐢ㄦ埛杈撳叆鐨勮揣甯佸拰閲戦 add by mxl 0922
			String currency = null;
			Double transferAmount = new Double(0);
			if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferBank.getFromCurrency();
				transferAmount = transferBank.getDebitAmount();
			} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferBank.getToCurrency();
				transferAmount = transferBank.getTransferAmount();
			}
			// resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);
			Map resultData = new HashMap();
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");

			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);
			resultData.put("txnType", Constants.TXN_SUBTYPE_SCHEDULE_TXN_NEW);
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);

			this.convertPojo2Map(transferBank, resultData);
			// 涓哄湪椤甸潰鏄剧ずSchedule Transfer鐨勪俊鎭�
			resultData.put("frequenceType", schTransferHis.getFrequnceType());
			resultData.put("frequenceDays", schTransferHis.getFrequnceDays());
			resultData.put("scheduleName", schTransferHis.getScheduleName());

			ApproveRuleService approveRuleService = (ApproveRuleService) Config
					.getAppContext().getBean("ApproveRuleService");
			if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
					resultData)) {
				throw new NTBException("err.flow.ApproveRuleNotDefined");
			}

			setResultData(resultData);
			// 鍙戦�閭欢 add by mxl 0928
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", transferBank.getRequestTime());
			dataMap.put("transId", transferBank.getTransId());
			dataMap.put("fromCurrency", transferBank.getFromCurrency());
			dataMap.put("fromAmount", transferBank.getDebitAmount());
			dataMap.put("corpName", corpUser.getCorporation().getCorpName());
			dataMap.put("fromAccount", transferBank.getFromAccount());
			dataMap.put("transId", transferBank.getTransId());
			dataMap.put("remark", transferBank.getRemark());
			
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_NEW, userName, dataMap);*/
			//mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_NEW, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			throw new NTBException("err.txn.TranscationFaily");
		}
	}

	public void addCancel() throws NTBException {
		TransferBank transferBank = (TransferBank) this.getUsrSessionDataValue("transferBank");
		ScheduleTransferHis schTransferHis = (ScheduleTransferHis) this.getUsrSessionDataValue("schTransferHis");
		
		// add by mxl 1212
		String week[] = null;
		if (schTransferHis.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			week = schTransferHis.getFrequnceDays().split(",");
		}
		
		// 鍒ゆ柇杈撳叆閲戦
		if (transferBank.getInputAmountFlag().equals(
				Constants.INPUT_AMOUNT_FLAG_FROM)) {
			transferBank.setTransferAmount(null);
		} else if (transferBank.getInputAmountFlag().equals(
				Constants.INPUT_AMOUNT_FLAG_TO)) {
			transferBank.setDebitAmount(null);
		}
		
		// for display
		Map resultData = new HashMap();

		String toAccount2 = null;
		toAccount2 = transferBank.getToAccount();
		resultData.put("toAccount2", toAccount2);

		if (schTransferHis.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			for (int i = 0; i <= week.length - 1; i++) {
				resultData.put("week" + i, week[i]);
			}
		}
		
		this.convertPojo2Map(transferBank, resultData);
		
		/*if("N".equals(transferBank.getOwnerAccFlag())){
			resultData.put("toAccount", "0");
		}*/
		
		// 涓哄湪椤甸潰鏄剧ずSchedule Transfer鐨勪俊鎭�
		resultData.put("frequenceType", schTransferHis.getFrequnceType());
		resultData.put("frequenceDays", schTransferHis.getFrequnceDays());
		resultData.put("scheduleName", schTransferHis.getScheduleName());
		// add by hjs 20070723
		if(schTransferHis.getFrequnceType().equals(ScheduleTransfer.MONTHLY)){
			resultData.put("month_type", schTransferHis.getFrequnceDays());
		}
		
		resultData.put("selectFromAcct", transferBank.getFromAccount() + " - "  + transferBank.getFromCurrency());//add by linrui for mul-ccy
		resultData.put("selectToAcct", transferBank.getToAccount() + " - "  + transferBank.getToCurrency());//add by linrui for mul-ccy
		setResultData(resultData);
		
		// 灏嗙敤鎴锋暟鎹啓鍏ession锛屼互渚縞onfirm鍚庡啓鍏ユ暟鎹簱
		this.setUsrSessionDataValue("transferBank", transferBank);
		this.setUsrSessionDataValue("schTransferHis", schTransferHis);
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
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

		//鍒ゆ柇鏄惁澶勪簬鎺堟潈鐘舵�
		if (schTransferService.isPending(scheduleId)) {
			throw new NTBException("err.bat.OperationPending");
		}
		
		// load鍑簊cheduleTransfer
		ScheduleTransfer scheduleTransfer = schTransferService.loadSchTransfer(scheduleId);
		TransferBank transferBank = transferService.viewInBANK(transID);

		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferBank.getToCurrency(), "MOP",
				new BigDecimal(transferBank.getTransferAmount().toString()),
				null, 2);
		// 鍒ゆ柇鐢ㄦ埛杈撳叆鐨勮揣甯佸拰閲戦 add by mxl 0922
		String inputAmountFlag = transferBank.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferBank.getFromCurrency();
			transferAmount = transferBank.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferBank.getToCurrency();
			transferAmount = transferBank.getTransferAmount();
		}

		// 涓篠electUser Tag鍑嗗鏁版嵁
		Map resultData = new HashMap();
		this.convertPojo2Map(transferBank, resultData);
		this.setResultData(resultData);

		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		resultData.put("txnType", Constants.TXN_SUBTYPE_SCHEDULE_TXN_REMOVE);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);

		resultData.put("frequenceType", scheduleTransfer.getFrequnceType());
		resultData.put("frequenceDays", scheduleTransfer.getFrequnceDays());
		resultData.put("scheduleName", scheduleTransfer.getScheduleName());
		if (scheduleTransfer.getFrequnceType().equals(ScheduleTransfer.DAYS_PER_MONTH)) {
			resultData.put("days_per_month", scheduleTransfer.getFrequnceDays());
		}

		ApproveRuleService approveRuleService = (ApproveRuleService) Config
				.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
				resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}

		// 涓哄湪椤甸潰鏄剧ずSchedule Transfer鐨勪俊鎭�
		resultData.put("frequenceType", scheduleTransfer.getFrequnceType());
		resultData.put("frequenceDays", scheduleTransfer.getFrequnceDays());
		resultData.put("scheduleName", scheduleTransfer.getScheduleName());
		setResultData(resultData);
		// 灏嗙敤鎴锋暟鎹啓鍏ession锛屼互渚縞onfirm鍚庡啓鍏ユ暟鎹簱
		this.setUsrSessionDataValue("transferBank", transferBank);
		this.setUsrSessionDataValue("scheduleTransfer", scheduleTransfer);
	}

	public void deleteConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		CorpUser corpUser = (CorpUser) this.getUser();
		TransferBank transferBank = (TransferBank) this
				.getUsrSessionDataValue("transferBank");
		ScheduleTransfer schTransfer = (ScheduleTransfer) this
				.getUsrSessionDataValue("scheduleTransfer");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");
		
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferBank.getToCurrency(), "MOP",
				new BigDecimal(transferBank.getTransferAmount().toString()),
				null, 2);
		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));

		//hjs
		String seqNo = CibIdGenerator.getIdForOperation("SCHEDULE_TRANSFER_HIS");
		// 鏂板缓涓�」鎺堟潈浠诲姟
		String inputAmountFlag = transferBank.getInputAmountFlag();
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_SCHEDULE_TXN_REMOVE,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				ScheduleTransferBankAction.class, transferBank.getFromCurrency(),
				transferBank.getDebitAmount().doubleValue(), transferBank
						.getToCurrency(), transferBank.getTransferAmount()
						.doubleValue(), equivalentMOP.doubleValue(),
						seqNo, transferBank.getRemark(),
						getUser(), assignedUser, corpUser.getCorporation()
						.getAllowExecutor(), inputAmountFlag);
		try {
			// 鏈�悗涓�鎵嶅啓鍏ユ暟鎹簱
			// 缁欒〃Schedule Transfer 淇敼涓�潯璁板綍
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
			// edit by mxl 0819
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");
			
			// 鍒ゆ柇鐢ㄦ埛杈撳叆鐨勮揣甯佸拰閲戦 add by mxl 0922
			String currency = null;
			Double transferAmount = new Double(0);
			if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferBank.getFromCurrency();
				transferAmount = transferBank.getDebitAmount();
			} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferBank.getToCurrency();
				transferAmount = transferBank.getTransferAmount();
			}
			// resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);
			resultData.put("txnType", Constants.TXN_SUBTYPE_SCHEDULE_TXN_REMOVE);
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);
			// 涓哄湪椤甸潰鏄剧ずSchedule Transfer鐨勪俊鎭�
			resultData.put("frequenceType", schTransferHis.getFrequnceType());
			resultData.put("frequenceDays", schTransferHis.getFrequnceDays());
			resultData.put("scheduleName", schTransferHis.getScheduleName());
			setResultData(resultData);
			this.convertPojo2Map(transferBank, resultData);
			
			ApproveRuleService approveRuleService = (ApproveRuleService) Config
					.getAppContext().getBean("ApproveRuleService");
			if (!approveRuleService.checkApproveRule(corpUser.getCorpId(), resultData)) {
				throw new NTBException("err.flow.ApproveRuleNotDefined");
			}

			setResultData(resultData);
			// 鍙戦�閭欢 add by mxl 0928
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", new Date());
			dataMap.put("transId", transferBank.getTransId());
			dataMap.put("fromCurrency", transferBank.getFromCurrency());
			dataMap.put("fromAmount", transferBank.getDebitAmount());
			dataMap.put("corpName", corpUser.getCorporation().getCorpName());
			dataMap.put("fromAccount", transferBank.getFromAccount());
			dataMap.put("transId", transferBank.getTransId());
			dataMap.put("remark", transferBank.getRemark());
			
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_REMOVE, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_REMOVE, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("Error process confirmation of transfering in BANK", e);
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

		//鍒ゆ柇鏄惁澶勪簬鎺堟潈鐘舵�
		if (schTransferService.isPending(scheduleId)) {
			throw new NTBException("err.bat.OperationPending");
		}
		
		// load鍑簊cheduleTransfer
		ScheduleTransfer scheduleTransfer = schTransferService.loadSchTransfer(scheduleId);
		TransferBank transferBank = transferService.viewInBANK(transID);
		
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferBank.getToCurrency(), "MOP",
				new BigDecimal(transferBank.getTransferAmount().toString()),
				null, 2);
		Map resultData = new HashMap();
		this.convertPojo2Map(transferBank, resultData);
		this.setResultData(resultData);

		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		// 鍒ゆ柇鐢ㄦ埛杈撳叆鐨勮揣甯佸拰閲戦 add by mxl 0922
		String inputAmountFlag = transferBank.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferBank.getFromCurrency();
			transferAmount = transferBank.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferBank.getToCurrency();
			transferAmount = transferBank.getTransferAmount();
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
		this.setUsrSessionDataValue("transferBank", transferBank);
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
		CorpUser corpUser = (CorpUser) getUser();

		TransferBank transferBank = (TransferBank) this
				.getUsrSessionDataValue("transferBank");
		ScheduleTransfer schTransfer = (ScheduleTransfer) this
				.getUsrSessionDataValue("scheduleTransfer");

		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferBank.getToCurrency(), "MOP",
				new BigDecimal(transferBank.getTransferAmount().toString()),
				null, 2);
		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));

		//hjs
		String seqNo = CibIdGenerator.getIdForOperation("SCHEDULE_TRANSFER_HIS");
		// 鏂板缓涓�」鎺堟潈浠诲姟
		String inputAmountFlag = transferBank.getInputAmountFlag();
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_SCHEDULE_TXN_BLOCK,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				ScheduleTransferBankAction.class, transferBank.getFromCurrency(),
				transferBank.getDebitAmount().doubleValue(), transferBank
						.getToCurrency(), transferBank.getTransferAmount()
						.doubleValue(), equivalentMOP.doubleValue(),
						seqNo, transferBank.getRemark(),
						getUser(), assignedUser, corpUser.getCorporation()
						.getAllowExecutor(), inputAmountFlag);
		try {
			// 鏈�悗涓�鎵嶅啓鍏ユ暟鎹簱
			// 缁欒〃Schedule Transfer 淇敼涓�潯璁板綍
			schTransfer.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			schTransfer.setStatus(Constants.STATUS_PENDING_APPROVAL);
			schTransfer.setOperation(Constants.OPERATION_BLOCK);
			// add by long_zg 2014-05-06 for CR188
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
			// edit by mxl 0819
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");
			// 鍒ゆ柇鐢ㄦ埛杈撳叆鐨勮揣甯佸拰閲戦 add by mxl 0922
			String currency = null;
			Double transferAmount = new Double(0);
			if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferBank.getFromCurrency();
				transferAmount = transferBank.getDebitAmount();
			} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferBank.getToCurrency();
				transferAmount = transferBank.getTransferAmount();
			}
			// resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);
			resultData.put("txnType", Constants.TXN_SUBTYPE_SCHEDULE_TXN_BLOCK);
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);
			// 涓哄湪椤甸潰鏄剧ずSchedule Transfer鐨勪俊鎭�
			resultData.put("frequenceType", schTransferHis.getFrequnceType());
			resultData.put("frequenceDays", schTransferHis.getFrequnceDays());
			resultData.put("scheduleName", schTransferHis.getScheduleName());
			setResultData(resultData);
			this.convertPojo2Map(transferBank, resultData);
			ApproveRuleService approveRuleService = (ApproveRuleService) Config
					.getAppContext().getBean("ApproveRuleService");
			if (!approveRuleService.checkApproveRule(corpUser.getCorpId(), resultData)) {
				throw new NTBException("err.flow.ApproveRuleNotDefined");
			}

			setResultData(resultData);
			// 鍙戦�閭欢 add by mxl 0928
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", transferBank.getRequestTime());
			dataMap.put("transId", transferBank.getTransId());
			dataMap.put("fromCurrency", transferBank.getFromCurrency());
			dataMap.put("fromAmount", transferBank.getDebitAmount());
			dataMap.put("corpName", corpUser.getCorporation().getCorpName());
			dataMap.put("fromAccount", transferBank.getFromAccount());
			dataMap.put("transId", transferBank.getTransId());
			dataMap.put("remark", transferBank.getRemark());
			
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_BLOCK, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_BLOCK, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("Error process confirmation of transfering in BANK", e);
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

		//鍒ゆ柇鏄惁澶勪簬鎺堟潈鐘舵�
		if (schTransferService.isPending(scheduleId)) {
			throw new NTBException("err.bat.OperationPending");
		}
		
		// load鍑簊cheduleTransfer
		ScheduleTransfer scheduleTransfer = schTransferService.loadSchTransfer(scheduleId);
		TransferBank transferBank = transferService.viewInBANK(transID);
		
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferBank.getToCurrency(), "MOP",
				new BigDecimal(transferBank.getTransferAmount().toString()),
				null, 2);
		// 涓篠electUser Tag鍑嗗鏁版嵁
		Map resultData = new HashMap();
		this.convertPojo2Map(transferBank, resultData);
		this.setResultData(resultData);

		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		// 鍒ゆ柇鐢ㄦ埛杈撳叆鐨勮揣甯佸拰閲戦 add by mxl 0922
		String inputAmountFlag = transferBank.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferBank.getFromCurrency();
			transferAmount = transferBank.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferBank.getToCurrency();
			transferAmount = transferBank.getTransferAmount();
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

		// 涓哄湪椤甸潰鏄剧ずSchedule Transfer鐨勪俊鎭�
		resultData.put("frequenceType", scheduleTransfer.getFrequnceType());
		resultData.put("frequenceDays", scheduleTransfer.getFrequnceDays());
		resultData.put("scheduleName", scheduleTransfer.getScheduleName());
		setResultData(resultData);

		// 灏嗙敤鎴锋暟鎹啓鍏ession锛屼互渚縞onfirm鍚庡啓鍏ユ暟鎹簱
		this.setUsrSessionDataValue("transferBank", transferBank);
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
		CorpUser corpUser = (CorpUser) getUser();

		TransferBank transferBank = (TransferBank) this.getUsrSessionDataValue("transferBank");
		ScheduleTransfer schTransfer = (ScheduleTransfer) this.getUsrSessionDataValue("scheduleTransfer");

		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferBank.getToCurrency(), "MOP",
				new BigDecimal(transferBank.getTransferAmount().toString()),
				null, 2);
		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));

		//hjs
		String seqNo = CibIdGenerator.getIdForOperation("SCHEDULE_TRANSFER_HIS");
		// 鏂板缓涓�」鎺堟潈浠诲姟
		String inputAmountFlag = transferBank.getInputAmountFlag();
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_SCHEDULE_TXN_UNBLOCK,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				ScheduleTransferBankAction.class, transferBank.getFromCurrency(),
				transferBank.getDebitAmount().doubleValue(), transferBank.getToCurrency(), 
				transferBank.getTransferAmount().doubleValue(),
				equivalentMOP.doubleValue(),
				seqNo, 
				transferBank.getRemark(),
				getUser(), assignedUser, 
				corpUser.getCorporation().getAllowExecutor(), 
				inputAmountFlag);
		try {
			// 鏈�悗涓�鎵嶅啓鍏ユ暟鎹簱
			// 缁欒〃Schedule Transfer 淇敼涓�潯璁板綍
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
			// edit by mxl 0819
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");
			// 鍒ゆ柇鐢ㄦ埛杈撳叆鐨勮揣甯佸拰閲戦 add by mxl 0922
			String currency = null;
			Double transferAmount = new Double(0);
			if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferBank.getFromCurrency();
				transferAmount = transferBank.getDebitAmount();
			} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferBank.getToCurrency();
				transferAmount = transferBank.getTransferAmount();
			}
			// resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);
			resultData.put("txnType",
					Constants.TXN_SUBTYPE_SCHEDULE_TXN_UNBLOCK);
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);
			
			// 涓哄湪椤甸潰鏄剧ずSchedule Transfer鐨勪俊鎭�
			resultData.put("frequenceType", schTransferHis.getFrequnceType());
			resultData.put("frequenceDays", schTransferHis.getFrequnceDays());
			resultData.put("scheduleName", schTransferHis.getScheduleName());
			setResultData(resultData);
			this.convertPojo2Map(transferBank, resultData);
			ApproveRuleService approveRuleService = (ApproveRuleService) Config
					.getAppContext().getBean("ApproveRuleService");
			if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
					resultData)) {
				throw new NTBException("err.flow.ApproveRuleNotDefined");
			}

			setResultData(resultData);
			
			// 鍙戦�閭欢 add by mxl 0928
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", transferBank.getRequestTime());
			dataMap.put("transId", transferBank.getTransId());
			dataMap.put("fromCurrency", transferBank.getFromCurrency());
			dataMap.put("fromAmount", transferBank.getDebitAmount());
			dataMap.put("corpName", corpUser.getCorporation().getCorpName());
			dataMap.put("fromAccount", transferBank.getFromAccount());
			dataMap.put("transId", transferBank.getTransId());
			dataMap.put("remark", transferBank.getRemark());

			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_UNBLOCK, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_UNBLOCK, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */
			
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("Error process confirmation of transfering in BANK", e);
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
		TransferBank transferBank = transferService.viewInBANK(transId);

		Map resultData = new HashMap();
		this.convertPojo2Map(transferBank, resultData);
		// 涓哄湪椤甸潰鏄剧ずSchedule Transfer鐨勪俊鎭�
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

		//鍒ゆ柇鏄惁澶勪簬鎺堟潈鐘舵�
		if (schTransferService.isPending(scheduleId)) {
			throw new NTBException("err.bat.OperationPending");
		}
		
		// load鍑簊cheduleTransfer
		ScheduleTransfer scheduleTransfer = schTransferService.loadSchTransfer(scheduleId);
		
		// add by mxl 1212
		String week[] = null;
		if (scheduleTransfer.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			week = scheduleTransfer.getFrequnceDays().split(",");
		}
		
		
		TransferBank transferBank = transferService.viewInBANK(transID);
		
		// 鍒ゆ柇杈撳叆閲戦
		if (transferBank.getInputAmountFlag().equals(
				Constants.INPUT_AMOUNT_FLAG_FROM)) {
			transferBank.setTransferAmount(null);
		} else if (transferBank.getInputAmountFlag().equals(
				Constants.INPUT_AMOUNT_FLAG_TO)) {
			transferBank.setDebitAmount(null);
		}
		
		// for display
		Map resultData = new HashMap();
//		this.convertPojo2Map(transferBank, resultData);
		
		/* Modify by long_zg 2019-05-22 UAT6-246 COB锛氭柊澧炵殑绗笁鑰呰綁璩湭瀹屽叏鏀瑰ソ  begin */
		/*String toAccount2 = null;
		toAccount2 = transferBank.getToAccount();
		resultData.put("toAccount2", toAccount2);
		if("N".equals(transferBank.getOwnerAccFlag())){
			resultData.put("toAccount", "0");
		}
		*/
		/* Modify by long_zg 2019-05-22 UAT6-246 COB锛氭柊澧炵殑绗笁鑰呰綁璩湭瀹屽叏鏀瑰ソ  end */
		
		if (scheduleTransfer.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			for (int i = 0; i <= week.length - 1; i++) {
				resultData.put("week" + i, week[i]);
			}
		}
		this.convertPojo2Map(transferBank, resultData);
		
		// 涓哄湪椤甸潰鏄剧ずSchedule Transfer鐨勪俊鎭�
		resultData.put("frequenceType", scheduleTransfer.getFrequnceType());
		resultData.put("frequenceDays", scheduleTransfer.getFrequnceDays());
		resultData.put("scheduleName", scheduleTransfer.getScheduleName());
		
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		resultData.put("selectFromAcct", transferBank.getFromAccount() + " - "  + transferBank.getFromCurrency());//add by linrui for mul-ccy
		resultData.put("selectToAcct", transferBank.getToAccount() + " - "  + transferBank.getToCurrency());//add by linrui for mul-ccy
		setResultData(resultData);
		
		// 灏嗙敤鎴锋暟鎹啓鍏ession锛屼互渚縞onfirm鍚庡啓鍏ユ暟鎹簱
		this.setUsrSessionDataValue("transferBank", transferBank);
		this.setUsrSessionDataValue("scheduleTransfer", scheduleTransfer);
	}

	public void edit() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		TransAmountService transAmountService = (TransAmountService) appContext
				.getBean("TransAmountService");

		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = (CorpUser) this.getUser();
		corpUser.setLanguage(lang);//add by linrui for mul-language
		TransferBank transferBank = (TransferBank) this
				.getUsrSessionDataValue("transferBank");
		ScheduleTransfer scheduleTransfer = (ScheduleTransfer) this
				.getUsrSessionDataValue("scheduleTransfer");
		
		//Add by heyj 20190529
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
		this.convertMap2Pojo(reqMap, transferBank);
		//this.convertMap2Pojo(this.getParameters(), transferBank);
		
		// add by li_zd at 20171117 for mul-language
        Double transAmt = TransAmountFormate.formateAmount(this.getParameter("transferAmount"), lang);
        Double debitAmt = TransAmountFormate.formateAmount(this.getParameter("debitAmount"), lang);
        transferBank.setTransferAmount(transAmt);
        transferBank.setDebitAmount(debitAmt);
        // end
		
//		transferBank.setRequestTime(new Date());
//		transferBank.setExecuteTime(new Date());

		scheduleTransfer.setScheduleName(Utils
				.null2EmptyWithTrim(getParameter("scheduleName")));
		// add by mxl 1008 鑾峰緱瀹氭椂杞笎鏃堕棿鐨勭被鍨�
		String dayType = null;
		dayType = Utils.null2EmptyWithTrim(getParameter("dayType"));
		scheduleTransfer.setFrequnceType(dayType);

        // add by hjs 20070723 鍥炴樉鏁版嵁
		if (scheduleTransfer.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			for (int i = 0; i <= getParameterValues("frequenceWeekDays").length - 1; i++) {
				this.getParameters().put("week" + i, getParameterValues("frequenceWeekDays")[i]);
			}
		}
		
		// From Account
		CorpAccount corpAccount = corpAccountService
				.viewCorpAccount(transferBank.getFromAccount());
		transferBank.setFromCurrency(corpAccount.getCurrency());
		transferBank.setFromAcctype(corpAccount.getAccountType());

		
		// To Account
		/* Modify by long_zg 2019-05-22 UAT6-246 COB锛氭柊澧炵殑绗笁鑰呰綁璩湭瀹屽叏鏀瑰ソ  begin */
		/*String toAccount = this.getParameter("toAccount");
		String toAccount2 = this.getParameter("toAccount2");
		if (toAccount.equals("0")) {
		transferBank.setToAccount(toAccount2);
        //缃垽鏂璷wn account鐨勬爣蹇�add mxl 0918
		transferBank.setOwnerAccFlag("N");*/
		
		String ownerAccFlag = this.getParameter("ownerAccFlag") ;
		
		String toName = "" ;
		
		if(Constants.NO.equalsIgnoreCase(ownerAccFlag)){
			
		/* Modify by long_zg 2019-05-22 UAT6-246 COB锛氭柊澧炵殑绗笁鑰呰綁璩湭瀹屽叏鏀瑰ソ  end */
		
			/* Modify by long_zg 2015-05-25 UAT6-292 COB锛氱涓夎�杞夎超寰炴牳蹇冩嬁鎴跺悕閷锛屽皫鑷翠氦鏄撳牨CM1013 begin */
			/*toName = this.getParameter("toName");	*/
			Map curMap = transferService.viewCurrentDetail(transferBank.getToAccount()) ;
			toName = (String ) curMap.get("SHORT_NAME") ;
			transferBank.setToName(toName);
			/* Modify by long_zg 2015-05-25 UAT6-292 COB锛氱涓夎�杞夎超寰炴牳蹇冩嬁鎴跺悕閷锛屽皫鑷翠氦鏄撳牨CM1013 begin */
			
			/* get the currency of the toAccount and accType from the fromHost */
			Map fromHost = transferService.toHostAccountInBANK(transferBank
					.getTransId(), corpUser, transferBank.getToAccount());
			String currencyCode = Utils.null2EmptyWithTrim(this.getParameter("toCurrency"));
			String toAccType = (String) fromHost.get("APPLICATION_CODE");
			if (toAccType.equals(CorpAccount.APPLICATION_CODE_CURRENT)) {
				transferBank.setToAcctype(CorpAccount.ACCOUNT_TYPE_CURRENT);
			} else if (toAccType.equals(CorpAccount.APPLICATION_CODE_SAVING)) {
				transferBank.setToAcctype(CorpAccount.ACCOUNT_TYPE_SAVING);
			} else if (toAccType.equals(CorpAccount.APPLICATION_CODE_OVERDRAFT)) {
				transferBank.setToAcctype(CorpAccount.ACCOUNT_TYPE_OVER_DRAFT);
			}

			RcCurrency rcCurrency = new RcCurrency();
			transferBank.setToCurrency(rcCurrency.getCcyByCbsNumber(currencyCode));

		} else {
			/* Modify by long_zg 2019-05-22 UAT6-246 COB锛氭柊澧炵殑绗笁鑰呰綁璩湭瀹屽叏鏀瑰ソ  end */
			/*transferBank.setToAccount(toAccount);*/
			
			String corpId = corpUser.getCorpId();
			toName = transferService.getBenName(corpId);
			transferBank.setToName(toName);
			
			/* Modify by long_zg 2019-05-22 UAT6-246 COB锛氭柊澧炵殑绗笁鑰呰綁璩湭瀹屽叏鏀瑰ソ  end */
			corpAccount = corpAccountService.viewCorpAccount(transferBank
					.getToAccount());
			transferBank.setToCurrency(corpAccount.getCurrency());
			transferBank.setToAcctype(corpAccount.getAccountType());
			//缃垽鏂璷wn account鐨勬爣蹇�add mxl 0918
			transferBank.setOwnerAccFlag(Constants.YES);
		}
		
		//add by lzg 20190708
		transferService.checkConvertibility(transferBank.getFromCurrency(), transferBank.getToCurrency());
		//add by lzg end

		if (transferBank.getTransferAmount().doubleValue() == 0) {
			// 璁剧疆杈撳叆杞笎閲戦鐨勬爣蹇�0923
			transferBank.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_FROM);
			transferBank.setFlag("1");
			BigDecimal transferAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), new BigDecimal(transferBank
					.getDebitAmount().toString()), null, 2);

			transferBank.setTransferAmount(new Double(transferAmount.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinTransAmt(transferBank.getFromCurrency(), transferBank.getDebitAmount().doubleValue());
			
			
			// add by mxl 0907 get the exchange rate
			Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), 7);
			String rateType = (String) exchangeMap
					.get(ExchangeRatesDao.RATE_TYPE);
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap
					.get(ExchangeRatesDao.BUY_RATE);
			BigDecimal sellRate = (BigDecimal) exchangeMap
					.get(ExchangeRatesDao.SELL_RATE);

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
			transferBank.setExchangeRate(new Double(exchangeRate.doubleValue()));

		} else if (transferBank.getDebitAmount().doubleValue() == 0) {
			transferBank.setInputAmountFlag(Constants.INPUT_AMOUNT_FLAG_TO);
			transferBank.setFlag("2");
			BigDecimal toAmount = new BigDecimal(transferBank
					.getTransferAmount().toString());
			BigDecimal fromAmount = exRatesService.getEquivalent(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), null, toAmount, 2);

			transferBank.setDebitAmount(new Double(fromAmount.toString()));
			
			//Jet add, check minimum trans amount
			transAmountService.checkMinTransAmt(transferBank.getToCurrency(), transferBank.getTransferAmount().doubleValue());
			
			// add by mxl 0907 get the exchange rate
			Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), 7);
			String rateType = (String) exchangeMap
					.get(ExchangeRatesDao.RATE_TYPE);
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap
					.get(ExchangeRatesDao.BUY_RATE);
			BigDecimal sellRate = (BigDecimal) exchangeMap
					.get(ExchangeRatesDao.SELL_RATE);

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
			transferBank.setExchangeRate(new Double(exchangeRate.doubleValue()));
		}


// 鏄剧ず鏁版嵁, 鐢变簬璁＄畻婢抽棬甯佺殑鏂瑰紡涓嶵ransferInBnuAction.java涓笉涓�嚧, 鏁呮敞閲� 2017/7/13 at Li_zd
//		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
//				.getCorpId(), transferBank.getToCurrency(), "MOP",
//				new BigDecimal(transferBank.getTransferAmount().toString()),
//				null, 2);
		
		// 鍒ゆ柇鐢ㄦ埛杈撳叆鐨勮揣甯佸拰閲戦 add by mxl 0922
		String inputAmountFlag = transferBank.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferBank.getFromCurrency();
			transferAmount = transferBank.getDebitAmount();
		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferBank.getToCurrency();
			transferAmount = transferBank.getTransferAmount();
		}
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), currency, "MOP",
				new BigDecimal(transferAmount.toString()),
				null, 2);
		
		// 妫�祴浜ゆ槗閲戦鏄惁瓒呭嚭姣忔棩浜ゆ槗绱涓婇檺
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		if (!transferLimitService.checkLimitQuotaByCorpId1(corpUser.getCorpId(),
				Constants.TXN_TYPE_TRANSFER_BANK_SCHEDULE, transferBank.getDebitAmount().doubleValue(),
				equivalentMOP.doubleValue())) {
            //write limit report
        	Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(), "HHmmss"));
			reportMap.put("USER_ID", transferBank.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_TRANSFER_BANK_SCHEDULE);
			reportMap.put("LOCAL_TRANS_CODE", transferBank.getTransId());
			reportMap.put("FROM_CURRENCY", transferBank.getFromCurrency());
			reportMap.put("TO_CURRENCY", transferBank.getToCurrency());
			reportMap.put("FROM_AMOUNT", transferBank.getDebitAmount());
			reportMap.put("TO_AMOUNT", transferBank.getTransferAmount());
			reportMap.put("FROM_ACCOUNT", transferBank.getFromAccount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
			throw new NTBWarningException("err.txn.ExceedDailyLimit");
		}

		// 涓篠electUser Tag鍑嗗鏁版嵁
		Map resultData = new HashMap();
		this.convertPojo2Map(transferBank, resultData);
		this.setResultData(resultData);

		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		resultData.put("txnType", Constants.TXN_SUBTYPE_SCHEDULE_TXN_EDIT);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);

		ApproveRuleService approveRuleService = (ApproveRuleService) Config
				.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
				resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}

		//  From Account = To Account 
		if (transferBank.getFromAccount().equals(transferBank.getToAccount())&&transferBank.getFromCurrency().equals(transferBank.getToCurrency())) {
			throw new NTBException("err.txn.TransferAccError");
		}

		// 涓哄湪椤甸潰鏄剧ずSchedule Transfer鐨勪俊鎭�	
        //edit by mxl 1214鏍规嵁杞笎鏃堕棿鐨勭被鍨嬭幏寰楀畾鏃惰浆甯愭椂闂�
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

		this.setResultData(resultData);
		this.convertPojo2Map(transferBank, resultData);

		// 灏嗙敤鎴锋暟鎹啓鍏ession锛屼互渚縞onfirm鍚庡啓鍏ユ暟鎹簱
		this.setUsrSessionDataValue("transferBank", transferBank);
		this.setUsrSessionDataValue("scheduleTransfer", scheduleTransfer);
	}

    public void editCancel() throws NTBException {
		TransferBank transferBank = (TransferBank) this.getUsrSessionDataValue("transferBank");
		ScheduleTransfer scheduleTransfer = (ScheduleTransfer) this.getUsrSessionDataValue("scheduleTransfer");
		
		// add by mxl 1212
		String week[] = null;
		if (scheduleTransfer.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			week = scheduleTransfer.getFrequnceDays().split(",");
		}
		
		// 鍒ゆ柇杈撳叆閲戦
		if (transferBank.getInputAmountFlag().equals(
				Constants.INPUT_AMOUNT_FLAG_FROM)) {
			transferBank.setTransferAmount(null);
		} else if (transferBank.getInputAmountFlag().equals(
				Constants.INPUT_AMOUNT_FLAG_TO)) {
			transferBank.setDebitAmount(null);
		}
		
		// for display
		Map resultData = new HashMap();

		String toAccount2 = null;
		toAccount2 = transferBank.getToAccount();
		resultData.put("toAccount2", toAccount2);

		if (scheduleTransfer.getFrequnceType().equals(ScheduleTransfer.WEEKLY)) {
			for (int i = 0; i <= week.length - 1; i++) {
				resultData.put("week" + i, week[i]);
			}
		}
		
		this.convertPojo2Map(transferBank, resultData);
		
		/*if("N".equals(transferBank.getOwnerAccFlag())){
			resultData.put("toAccount", "0");
		}*/
		
		// 涓哄湪椤甸潰鏄剧ずSchedule Transfer鐨勪俊鎭�
		resultData.put("frequenceType", scheduleTransfer.getFrequnceType());
		resultData.put("frequenceDays", scheduleTransfer.getFrequnceDays());
		resultData.put("scheduleName", scheduleTransfer.getScheduleName());
		// add by hjs 20070723
		if(scheduleTransfer.getFrequnceType().equals(ScheduleTransfer.MONTHLY)){
			resultData.put("month_type", scheduleTransfer.getFrequnceDays());
		}
		
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccount(corpUser.getCorpId()));
		setResultData(resultData);
    }

	public void editConfirm() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		
		CorpUser corpUser = (CorpUser) getUser();
		ScheduleTransfer schTransfer = (ScheduleTransfer) this.getUsrSessionDataValue("scheduleTransfer");
		TransferBank transferBank = (TransferBank) this.getUsrSessionDataValue("transferBank");
		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));
		
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferBank.getToCurrency(), "MOP",
				new BigDecimal(transferBank.getTransferAmount().toString()),
				null, 2);

		//hjs
		String seqNo = CibIdGenerator.getIdForOperation("SCHEDULE_TRANSFER_HIS");
		// 鏂板缓涓�」鎺堟潈浠诲姟
		String inputAmountFlag = transferBank.getInputAmountFlag();
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_SCHEDULE_TXN_EDIT,
				FlowEngineService.TXN_CATEGORY_FINANCE,
				ScheduleTransferBankAction.class, transferBank.getFromCurrency(),
				transferBank.getDebitAmount().doubleValue(), transferBank
						.getToCurrency(), transferBank.getTransferAmount()
						.doubleValue(), equivalentMOP.doubleValue(),
						seqNo, transferBank.getRemark(),
						getUser(), assignedUser, corpUser.getCorporation()
						.getAllowExecutor(), inputAmountFlag);

		try {
			// 鏈�悗涓�鎵嶅啓鍏ユ暟鎹簱
			//hjs
			transferBank.setTransId(CibIdGenerator.getRefNoForTransaction());
			transferBank.setExecuteTime(new Date());
			transferBank.setRequestTime(new Date());
			transferBank.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			transferBank.setStatus(Constants.STATUS_PENDING_APPROVAL);
			transferBank.setOperation(Constants.OPERATION_UPDATE);
			
			// 缁欒〃Schedule Transfer 淇敼涓�潯璁板綍 
			schTransfer.setTransId(transferBank.getTransId());
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
			schTransferService.addTransferBank(transferBank);

			Map resultData = new HashMap();
			// edit by mxl 0819
			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");
			// 鍒ゆ柇鐢ㄦ埛杈撳叆鐨勮揣甯佸拰閲戦 add by mxl 0922
			String currency = null;
			Double transferAmount = new Double(0);
			if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferBank.getFromCurrency();
				transferAmount = transferBank.getDebitAmount();
			} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferBank.getToCurrency();
				transferAmount = transferBank.getTransferAmount();
			}
			// resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);
			resultData.put("txnType", Constants.TXN_SUBTYPE_SCHEDULE_TXN_EDIT);
			resultData.put("currency", currency);
			resultData.put("amount", transferAmount);
			resultData.put("amountMopEq", equivalentMOP);

			resultData.put("frequenceType", schTransfer.getFrequnceType());
			resultData.put("frequenceDays", schTransfer.getFrequnceDays());
			resultData.put("scheduleName", schTransfer.getScheduleName());

			this.convertPojo2Map(transferBank, resultData);

			ApproveRuleService approveRuleService = (ApproveRuleService) Config
					.getAppContext().getBean("ApproveRuleService");
			if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
					resultData)) {
				throw new NTBException("err.flow.ApproveRuleNotDefined");
			}

			setResultData(resultData);
			// 鍙戦�閭欢 add by mxl 0928
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", transferBank.getRequestTime());
			dataMap.put("transId", transferBank.getTransId());
			dataMap.put("fromCurrency", transferBank.getFromCurrency());
			dataMap.put("fromAmount", transferBank.getDebitAmount());
			dataMap.put("corpName", corpUser.getCorporation().getCorpName());
			dataMap.put("fromAccount", transferBank.getFromAccount());
			dataMap.put("transId", transferBank.getTransId());
			dataMap.put("remark", transferBank.getRemark());
			
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_EDIT, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_SCHEDULE_TXN_EDIT, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 绗笁鑰呰綁璩琽perator鎴愬姛闋佺己灏憇ave as template end */

		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("Error process confirmation of transfering in BANK", e);
			throw new NTBException("err.txn.TranscationFaily");
		}

	}

	public boolean approve(String txnType, String id, CibAction bean) throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		SchTransferService schTransferService = (SchTransferService) appContext
				.getBean("SchTransferService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpUser corpUser = (CorpUser) bean.getUser();

		if (txnType != null) {
			// load閿熸枻鎷稴cheduleTransferHis
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
			
			// 鏍规嵁scheduleId寰楀嚭transId,load鍑簍ransferBnu
			TransferBank transferBank = transferService.viewInBANK(schTransferHis.getTransId());
			transferBank.setExecuteTime(new Date());
			// add by mxl 璁＄畻鏈�柊姹囩巼 0921
			/*Map exchangeMap = exRatesService.getExchangeRate(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), 7);*/
			Map exchangeMap = exRatesService.getExchangeRateFromHost(corpUser
					.getCorpId(), transferBank.getFromCurrency(), transferBank
					.getToCurrency(), 7);
			String rateType = (String) exchangeMap
					.get(ExchangeRatesDao.RATE_TYPE);
			BigDecimal exchangeRate = new BigDecimal("0");
			BigDecimal buyRate = (BigDecimal) exchangeMap
					.get(ExchangeRatesDao.BUY_RATE);
			BigDecimal sellRate = (BigDecimal) exchangeMap
					.get(ExchangeRatesDao.SELL_RATE);
			
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
			
			transferBank.setExchangeRate(new Double(exchangeRate.doubleValue()));
			transferService.updateBANK(transferBank);
			
			// add by mxl 0201 鍐欐姤琛�
			schTransferService.uploadSchTransferInBankReprot(schTransfer,transferBank);
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
			// load ScheduleTransferHis
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
		
		// load鍑篠cheduleTransferHis
		ScheduleTransferHis schTransferHis = schTransferService.loadSchTransferHis(id);
		// 鏍规嵁scheduleId寰楀嚭transId,load鍑簍ransferBnu
		TransferBank transferBank = transferService.viewInBANK(schTransferHis.getTransId());

		// add by mxl 璁＄畻鏈�柊姹囩巼 0921
		Map exchangeMap = exRatesService.getExchangeRate(corpUser.getCorpId(),
				transferBank.getFromCurrency(), transferBank.getToCurrency(), 7);
		String rateType = (String) exchangeMap.get(ExchangeRatesDao.RATE_TYPE);
		BigDecimal exchangeRate = new BigDecimal("0");
		BigDecimal buyRate = (BigDecimal) exchangeMap
				.get(ExchangeRatesDao.BUY_RATE);
		BigDecimal sellRate = (BigDecimal) exchangeMap
				.get(ExchangeRatesDao.SELL_RATE);
		
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
		
		transferBank.setExchangeRate(new Double(exchangeRate.doubleValue()));
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), transferBank.getToCurrency(), "MOP",
				new BigDecimal(transferBank.getTransferAmount().toString()),
				null, 2);

		Map resultData = bean.getResultData();
		convertPojo2Map(transferBank, resultData);

		// edit by mxl 0819
		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");
		
		// 鍒ゆ柇鐢ㄦ埛杈撳叆鐨勮揣甯佸拰閲戦 add by mxl 0922
		String inputAmountFlag = transferBank.getInputAmountFlag();
		String currency = null;
		Double transferAmount = new Double(0);
		if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
			currency = transferBank.getFromCurrency();
			transferAmount = transferBank.getDebitAmount();
			
			//jet modified for showing new transaction amount
			Double new_From_Amount = transferBank.getDebitAmount();			
			BigDecimal new_To_Amount_temp = exRatesService.getEquivalent(
					corpUser.getCorpId(), currency,
					transferBank.getToCurrency(), new BigDecimal(transferAmount
							.toString()), null, 2);
			Double new_To_Amount = new Double(new_To_Amount_temp.toString());
			resultData.put("newFromAmount", new_From_Amount);
			resultData.put("newToAmount", new_To_Amount);

		} else if (inputAmountFlag.equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
			currency = transferBank.getToCurrency();
			transferAmount = transferBank.getTransferAmount();
			
			//jet modified for showing new transaction amount
			Double new_To_Amount = transferBank.getTransferAmount();			
			BigDecimal new_From_Amount_temp = exRatesService.getEquivalent(
					corpUser.getCorpId(), transferBank.getFromCurrency(),
					currency, null, new BigDecimal(transferAmount.toString()),
					2);		
			Double new_From_Amount = new Double(new_From_Amount_temp.toString());
			resultData.put("newFromAmount", new_From_Amount);
			resultData.put("newToAmount", new_To_Amount);

		}
		resultData.put("txnType", txnType);
		resultData.put("currency", currency);
		resultData.put("amount", transferAmount);
		resultData.put("amountMopEq", equivalentMOP);
		// add by mxl 0930 鏄剧ず鏈�柊姹囩巼
		resultData.put("newExchangeRate", exchangeRate);

		resultData.put("frequenceType", schTransferHis.getFrequnceType());
		resultData.put("frequenceDays", schTransferHis.getFrequnceDays());
		resultData.put("scheduleName", schTransferHis.getScheduleName());

		bean.setResultData(resultData);
		return "/WEB-INF/pages/bat/schedule_transfer/schTransfer_InBank_approval_view.jsp";
	}
}
