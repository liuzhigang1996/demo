package app.cib.action.srv;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.KeyNameUtils;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.exception.NTBWarningException;

import app.cib.bo.bat.FileRequest;
import app.cib.bo.srv.ReqBankDraft;
import app.cib.bo.srv.ReqCashierOrder;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TransferMacau;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.service.bat.CashOrderBatchService;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.srv.RequestService;
import app.cib.service.sys.ApproveRuleService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.sys.CutOffTimeService;
import app.cib.service.sys.MailService;
import app.cib.service.txn.TransferLimitService;
import app.cib.service.txn.TransferService;
import app.cib.util.CacheController;
import app.cib.util.Constants;
import app.cib.util.TransAmountFormate;
import app.cib.util.UploadReporter;

public class CashOrderAction extends CibAction implements Approvable {
	public void listLoad() throws NTBException {
		// 閿熸枻鎷烽敓鐭┖纰夋嫹 ResultData 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸鎾呮嫹閿熸枻鎷�
		setResultData(new HashMap(1));
	}

	// modify by Peng Haisen 2009-10-10 for CR103
	public void listHistory() throws NTBException {
		// 閿熸枻鎷烽敓鐭┖纰夋嫹 ResultData 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸鎾呮嫹閿熸枻鎷�
		setResultData(new HashMap(1));
		CorpUser user = (CorpUser) this.getUser();
		// String userId = user.getUserId();
		String corpId = user.getCorpId();
		String groupId = user.getGroupId();
		String approverFlag = user.getCorporation().getAllowApproverSelection();
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");

		Date dateFrom = null;
		Date dateTo = null;
		String fromAccount = null;
		String changeFlag = null;

		fromAccount = Utils.null2EmptyWithTrim(getParameter("fromAccount"));

		// add by mxl 0824
		String date_range = Utils
				.null2EmptyWithTrim(getParameter("date_range"));
		if (date_range.equals("range")) {

			if (!Utils.null2EmptyWithTrim(getParameter("dateFrom")).equals("")) {

				dateFrom = DateTime.getDateFromStr(Utils
						.null2EmptyWithTrim(getParameter("dateFrom")), true);
			}
			if (!Utils.null2EmptyWithTrim(getParameter("dateTo")).equals("")) {

				dateTo = DateTime.getDateFromStr(Utils
						.null2EmptyWithTrim(getParameter("dateTo")), false);
			}
		}

		// File Request閿熶茎璇ф嫹璇�
		List transferList = transferService.fileRequestHistoryForRequestHis(
				FileRequest.CASH_ORDER_BATCH_TYPE, dateFrom, dateTo, corpId,
				groupId, fromAccount);
		transferList = KeyNameUtils.listDash2CaseDiff(transferList);
		List toList = new ArrayList();
		for (int i = 0; i < transferList.size(); i++) {
			// FileRequest fileRequest = (FileRequest) transferList
			// .get(i);
			Map fileRequestMap = (Map) transferList.get(i);
			// add by mxl 0922 閿熷彨璁规嫹閿熻鍑ゆ嫹閿熸枻鎷锋効璋嬮敓鏂ゆ嫹閿熼ズ顭掓嫹閿燂拷
			if ("Y".equals(approverFlag)) {
				if ("1".equals((String) fileRequestMap.get("status"))) {
					if (flowEngineService.checkApproveComplete(
							Constants.TXN_SUBTYPE_CASHIER_ORDER,
							(String) fileRequestMap.get("batchId"))) {
						changeFlag = "N";
					} else {
						changeFlag = "Y";
					}
				}
			}
			String requestType = null;
			if (((String) fileRequestMap.get("fileName")).equals("#")) {
				requestType = "ONLINE REQUEST";
			} else {
				requestType = "FILE REQUEST";
			}
			// Map fileRequestData = new HashMap();
			// this.convertPojo2Map(fileRequest, fileRequestData);
			fileRequestMap.put("changeFlag", changeFlag);
			fileRequestMap.put("requestType", requestType);
			//add by linrui for oracle
			fileRequestMap.put("requestTime", CacheController.getDateBySqlTimestamp(fileRequestMap.get("requestTime")));
			toList.add(fileRequestMap);

		}
		Map resultData = new HashMap();

		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("toList", toList);
		resultData.put("fromAccount", fromAccount);
		resultData.put("txnType", Constants.TXN_SUBTYPE_CASHIER_ORDER);
		resultData.put("methodType", "viewBankRequest");
		resultData.put("approverFlag", approverFlag);
		setResultData(resultData);
	}

	public void viewDetail() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		setResultData(new HashMap(1));
		String batchID = getParameter("batchId");
		FileRequest pojoFileRequest = requestService.viewFileRequest(batchID);
		// add by lw 20100904
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		// add by lw end
		Map resultData = new HashMap();
		resultData.put("fromAccount", pojoFileRequest.getFromAccount());
		resultData.put("fromCurrency", pojoFileRequest.getFromCurrency());
		resultData.put("toCurrency", pojoFileRequest.getToCurrency());
		resultData.put("fromAmount", pojoFileRequest.getFromAmount());
		// 閿熸枻鎷烽敓绲檃tchId閿熻妭鎲嬫嫹ReqCashierOrder閿熸枻鎷疯閿熸枻鎷峰綍
		List requestList = requestService.listCashierOrderByBatchid(batchID);
		List toList = new ArrayList();
		double totalAmount = 0;
		for (int i = 0; i < requestList.size(); i++) {
			ReqCashierOrder reqCashierOrder = (ReqCashierOrder) requestList
					.get(i);
			totalAmount = totalAmount
					+ (new Double(reqCashierOrder.getCashierAmount().toString()))
							.doubleValue();
			Map cashierOrderData = new HashMap();
			this.convertPojo2Map(reqCashierOrder, cashierOrderData);
			cashierOrderData.put("sequenceNo", String.valueOf(i + 1));
			// add by lw 20100904
			String purpose = reqCashierOrder.getOtherPurpose();
			if(purpose != null && !"".equals(purpose)){
				cashierOrderData.put("purpose", purpose);
				cashierOrderData.put("purposeCode", "99");
			}
			/*
			String purposeCode = reqCashierOrder.getPurposeCode();
			String otherPurpose = reqCashierOrder.getOtherPurpose();
			String purposeDesc = transferService
					.getPurposeDescription(purposeCode);
			if (purposeCode != null && !"".equals(purposeCode)) {
				cashierOrderData.put("purpose", purposeDesc);
			} else if (otherPurpose != null && !"".equals(otherPurpose)) {
				cashierOrderData.put("purpose", otherPurpose);
			}
			*/
			// add by lw end
			toList.add(cashierOrderData);
		}
		resultData.put("toList", toList);
		resultData.put("totalNumber", String.valueOf(requestList.size()));
		resultData.put("totalAmount", String.valueOf(totalAmount));
		setResultData(resultData);
	}

	public void addLoad() throws NTBException {
		// 閿熸枻鎷烽敓鐭┖纰夋嫹 ResultData 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸鎾呮嫹閿熸枻鎷�
		setResultData(new HashMap(1));
	}

	public void addDetail() throws NTBException {
		// 閿熸枻鎷峰閿熸枻鎷稴ervice
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		// 閿熸枻鎷峰閿熸枻鎷�POJO
		CorpUser corpUser = (CorpUser) this.getUser();
		FileRequest fileRequest = new FileRequest(CibIdGenerator
				.getRefNoForTransaction());
		this.convertMap2Pojo(this.getParameters(), fileRequest);
		String chargeBy = Utils.null2EmptyWithTrim(getParameter("chargeBy"));
		String chargeAccount = null;
		// 閿熷彨璁规嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽┒閿燂拷
		if (chargeBy.equals("1")) {
			fileRequest.setChargeFlag("1");
			chargeAccount = fileRequest.getFromAccount();
		} else if (chargeBy.equals("2")) {
			fileRequest.setChargeFlag("2");
			chargeAccount = Utils
					.null2EmptyWithTrim(getParameter("chargeAccount"));
		}
		fileRequest.setCurrency(fileRequest.getToCurrency());
		fileRequest.setChargeAccount(chargeAccount);
		// 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽┒閿熸枻鎷峰箷閿熸枻鎷烽敓锟絘dd by mxl 1108
		// CorpAccount corpAccount =
		// corpAccountService.viewCorpAccount(fileRequest.getChargeAccount());
		// edit by mxl 0209
		fileRequest.setChargeCurrency("MOP");
		// 閿熸枻鎷烽敓鏂ゆ嫹FromCurrency
		CorpAccount corpFromAccount = corpAccountService
				.viewCorpAccount(fileRequest.getFromAccount());
		fileRequest.setFromCurrency(corpFromAccount.getCurrency());

		/*
		 * 閿熸枻鎷烽敓鏂ゆ嫹FromAmount BigDecimal toAmount = new
		 * BigDecimal(fileRequest.getToAmount().toString()); BigDecimal
		 * fromAmount = exRatesService.getEquivalent(corpUser.getCorpId(),
		 * fileRequest.getFromCurrency(), fileRequest .getToCurrency(), null,
		 * toAmount, 2); fileRequest.setFromAmount(new
		 * Double(fromAmount.toString()));
		 */
		fileRequest.setCorpId(corpUser.getCorpId());
		fileRequest.setUserId(corpUser.getUserId());
		// 閿熸枻鎷烽敓鏂ゆ嫹List
		List inputList = new ArrayList();
		// edit by mxl 0112 閿熼叺浼欐嫹鍙敓鏂ゆ嫹閿熸枻鎷蜂竴閿熻剼浼欐嫹绁ㄦ椂閿熶茎杈炬嫹閿熸枻鎷�
		if (fileRequest.getTotalNumber().toString().equals("1")) {
			Map inputCashierOrderData = new HashMap();
			inputCashierOrderData.put("index", String.valueOf(1));
			inputCashierOrderData.put("beneficiaryName", "");
			inputCashierOrderData.put("beneficiaryName2", "");
			inputCashierOrderData.put("beneficiaryName3", "");
			inputCashierOrderData.put("cashierAmount", fileRequest
					.getToAmount());
			inputList.add(inputCashierOrderData);
		} else {
			for (int i = 1; i <= fileRequest.getTotalNumber().intValue(); i++) {
				Map inputCashierOrderData = new HashMap();
				inputCashierOrderData.put("index", String.valueOf(i));
				inputCashierOrderData.put("beneficiaryName", "");
				inputCashierOrderData.put("beneficiaryName2", "");
				inputCashierOrderData.put("beneficiaryName3", "");
				inputCashierOrderData.put("cashierAmount_", "");
				inputList.add(inputCashierOrderData);
			}
		}

		Map resultData = new HashMap();
		this.convertPojo2Map(fileRequest, resultData);
		// -----------------
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		resultData.put("language", lang.toString());
		setResultData(resultData);//end
		// -----------------
		resultData.put("inputList", inputList);
		this.setResultData(resultData);
		// 閿熸枻鎷烽敓鐭紮鎷烽敓鏂ゆ嫹閿熷彨杈炬嫹閿熺氮ession閿熸枻鎷烽敓鐨嗘唻鎷穋onfirm閿熸枻鎷峰啓閿熸枻鎷烽敓鏂ゆ嫹鑿橀敓锟�
		this.setUsrSessionDataValue("fileRequest", fileRequest);

	}

	public void add() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		FileRequest fileRequest = (FileRequest) this
				.getUsrSessionDataValue("fileRequest");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = (CorpUser) this.getUser();
        corpUser.setLanguage(lang);//end
		// 鍙朾eneficiaryName閿熸枻鎷穋ashierAmount閿熸枻鎷峰�
		String[] beneficiaryNameString = new String[21];
		String[] beneficiaryName2String = new String[21];
		String[] beneficiaryName3String = new String[21];
		String[] cashierAmountString = new String[21];
		String[] draftChargeString = new String[21];
		// add by lw 20100902
		//String[] purposeCodeString = new String[21];
		String[] purposeString = new String[21];
		String proofOfPurpose = null;
		// add by lw end
		double inputTotalAmount = 0;

		for (int i = 1; i <= fileRequest.getTotalNumber().intValue(); i++) {
			String beneficiaryName = "beneficiaryName_" + i;
			String beneficiaryName2 = "beneficiaryName2_" + i;
			String beneficiaryName3 = "beneficiaryName3_" + i;
			String cashierAmount = "cashierAmount_" + i;
			// add by lw 20100902
//			String purposeCode = "purposeCode_" + i;
			String purpose = "purpose_" + i;
			proofOfPurpose = "proofOfPurpose";
			// add by lw end
			beneficiaryNameString[i - 1] = Utils
					.null2EmptyWithTrim(getParameter(beneficiaryName));
			beneficiaryName2String[i - 1] = Utils
					.null2EmptyWithTrim(getParameter(beneficiaryName2));
			beneficiaryName3String[i - 1] = Utils
					.null2EmptyWithTrim(getParameter(beneficiaryName3));
			cashierAmountString[i - 1] = Utils
					.null2EmptyWithTrim(getParameter(cashierAmount));
			// add by lw 20100902
//			purposeCodeString[i - 1] = Utils
//					.null2EmptyWithTrim(getParameter(purposeCode));
			purposeString[i - 1] = Utils
					.null2EmptyWithTrim(getParameter(purpose));
			proofOfPurpose = Utils.null2EmptyWithTrim(getParameter(proofOfPurpose));
			// add by lw end
			try {
				//对葡币进行金额处理
				inputTotalAmount = inputTotalAmount + TransAmountFormate.formateAmount(cashierAmountString[i - 1],lang);
			} catch (Exception e) {
				throw new NTBException("err.txn.cashierAmountIsNull");
			}
		}

		fileRequest.setToAmount(new Double(inputTotalAmount));
		
		// 閿熸枻鎷烽敓鏂ゆ嫹FromAmount
		BigDecimal toAmount = new BigDecimal(fileRequest.getToAmount()
				.toString());
		BigDecimal fromAmount = exRatesService.getEquivalent(corpUser
				.getCorpId(), fileRequest.getFromCurrency(), fileRequest
				.getToCurrency(), null, toAmount, 2);
		fileRequest.setFromAmount(new Double(fromAmount.toString()));
		// 閿熸枻鎷烽敓鏂ゆ嫹chargeList add bxml 1108
		List chargeList = new ArrayList();

		for (int i = 1; i <= fileRequest.getTotalNumber().intValue(); i++) {
			Map chargeMap = new HashMap();
			chargeMap.put("CASHIER_ORDER_AMT", cashierAmountString[i - 1]);
			chargeList.add(chargeMap);
		}

		double totalChargesAmount = 0; // 閿熸澃纰夋嫹閿熸枻鎷烽敓鏂ゆ嫹閿燂拷
		// 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿燂拷add by mxl 1108
		List fromChargeList = requestService.toHostCashierOrderCharges(
				fileRequest.getChargeCurrency(), fileRequest.getCurrency(),
				fileRequest.getTotalNumber().toString(), chargeList, corpUser,
				fileRequest.getBatchId());
		for (int i = 0; i < fromChargeList.size(); i++) {
			Map recordMap = (Map) fromChargeList.get(i);
			draftChargeString[i] = recordMap.get("CASHIER_ORDER_COMMISSION")
					.toString();
			totalChargesAmount = totalChargesAmount
					+ (new Double(draftChargeString[i]).doubleValue());
		}
		fileRequest.setTotalChargesamount(new Double(totalChargesAmount));
		// 閿熸枻鎷烽敓鏂ゆ嫹List
		List inputList = new ArrayList();
		for (int i = 1; i <= fileRequest.getTotalNumber().intValue(); i++) {
			Map inputCashierOrderData = new HashMap();
			inputCashierOrderData.put("index", String.valueOf(i));
			inputCashierOrderData.put("beneficiaryName",
					beneficiaryNameString[i - 1]);
			inputCashierOrderData.put("beneficiaryName2",
					beneficiaryName2String[i - 1]);
			inputCashierOrderData.put("beneficiaryName3",
					beneficiaryName3String[i - 1]);
			inputCashierOrderData.put("cashierAmount",
					cashierAmountString[i - 1]);
			inputCashierOrderData.put("chargeAmount", draftChargeString[i - 1]);
			// add by lw 20100902
			if(purposeString[i - 1] != null && !"".equals(purposeString[i - 1])){
				inputCashierOrderData.put("showPurpose", "true");
				inputCashierOrderData.put("purposeString", purposeString[i - 1]);
			}
			//proofOfPurpose checkbox checked
			if(proofOfPurpose.equals("on")){
				inputCashierOrderData.put("showProof", "true");
			}else{
				inputCashierOrderData.put("showProof", "false");
			}
			/*
			if (purposeCodeString[i - 1] != null
					&& !"".equals(purposeCodeString[i - 1])) {
				if (purposeCodeString[i - 1].equals("99")) {
					if (otherPurposeString[i - 1] != null
							&& !"".equals(otherPurposeString[i - 1])) {
						inputCashierOrderData.put("showOthers", "true");
						inputCashierOrderData.put("otherPurposeString",
								otherPurposeString[i - 1]);
					}
				} else {
					inputCashierOrderData.put("showOthers", "false");
					inputCashierOrderData.put("purposeCodeString",
							purposeCodeString[i - 1]);
				}
			}
			*/
			// add by lw end
			inputList.add(inputCashierOrderData);
		}

		Map resultData = new HashMap();
		resultData.put("inputList", inputList);
		this.setResultData(resultData);
		// 閿熸枻鎷烽敓鏂ゆ嫹姒烽敓鏂ゆ嫹濯抽敓锟�
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), fileRequest.getToCurrency(), "MOP",
				new BigDecimal(fileRequest.getToAmount().toString()), null, 2);

		// 閿熷彨鏂枻鎷烽敓鏂ゆ嫹閿熺潾璁规嫹
		if (!corpUser.checkUserLimit(Constants.TXN_TYPE_CASHIER_ORDER,
				equivalentMOP)) {
			// write limit report
			Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
					"yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
					"HHmmss"));
			reportMap.put("USER_ID", fileRequest.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_CASHIER_ORDER);
			reportMap.put("LOCAL_TRANS_CODE", fileRequest.getBatchId());
			reportMap.put("FROM_CURRENCY", fileRequest.getFromCurrency());
			reportMap.put("FROM_ACCOUNT", fileRequest.getFromAccount());
			reportMap.put("TO_CURRENCY", fileRequest.getToCurrency());
			reportMap.put("FROM_AMOUNT", fileRequest.getFromAmount());
			// reportMap.put("TO_AMOUNT", fileRequest.getToAccount());
			reportMap.put("EXCEEDING_TYPE", "2");
			reportMap.put("LIMIT_TYPE", "");
			reportMap.put("USER_LIMIT ", corpUser
					.getUserLimit(Constants.TXN_TYPE_CASHIER_ORDER));
			reportMap.put("DAILY_LIMIT ", new Double(0));
			reportMap.put("TOTAL_AMOUNT ", new Double(0));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
			throw new NTBWarningException("err.txn.ExceedUserLimit");
		}

		// add by mxl 閿熸枻鎷烽敓绲沘ily limit
		if (!transferLimitService.checkCurAmtLimitQuota(fileRequest.getFromAccount(),
				corpUser.getCorpId(), Constants.TXN_TYPE_CASHIER_ORDER,
				fileRequest.getFromAmount().doubleValue(), equivalentMOP
						.doubleValue())) {
			// write limit report
			Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
					"yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
					"HHmmss"));
			reportMap.put("USER_ID", fileRequest.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_CASHIER_ORDER);
			reportMap.put("LOCAL_TRANS_CODE", fileRequest.getBatchId());
			reportMap.put("FROM_CURRENCY", fileRequest.getFromCurrency());
			reportMap.put("FROM_ACCOUNT", fileRequest.getFromAccount());
			reportMap.put("TO_CURRENCY", fileRequest.getToCurrency());
			reportMap.put("FROM_AMOUNT", fileRequest.getFromAmount());
			reportMap.put("TO_AMOUNT", fileRequest.getToAmount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService
					.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService
					.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
			throw new NTBWarningException("err.txn.ExceedDailyLimit");
		}
		// 涓篠electUser Tag鍑嗛敓鏂ゆ嫹閿熸枻鎷烽敓锟�
		resultData.put("txnTypeField", "txnType");
		resultData.put("currencyField", "currency");
		resultData.put("amountField", "amount");
		resultData.put("amountMopEqField", "amountMopEq");

		resultData.put("txnType", Constants.TXN_SUBTYPE_CASHIER_ORDER);
		resultData.put("currency", fileRequest.getToCurrency());
		resultData.put("amount", fileRequest.getToAmount());
		resultData.put("amountMopEq", equivalentMOP);

		ApproveRuleService approveRuleService = (ApproveRuleService) Config
				.getAppContext().getBean("ApproveRuleService");
		if (!approveRuleService.checkApproveRule(corpUser.getCorpId(),
				resultData)) {
			throw new NTBException("err.flow.ApproveRuleNotDefined");
		}

		this.convertPojo2Map(fileRequest, resultData);
		this.setResultData(resultData);
		/*
		 * 閿熷彨璁规嫹閿熸枻鎷烽敓鏂ゆ嫹鎱曢敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹缁嗛敓鏂ゆ嫹閿熸枻鎷峰垗閿熻顕嗘嫹閿熸枻鎷锋瑺閿熸枻鎷烽敓鏂ゆ嫹 if (!(inputTotalAmount ==
		 * fileRequest.getToAmount().doubleValue())) { throw new
		 * NTBException("err.txn.totalAmountIsError"); }
		 */

		// 閿熸枻鎷烽敓鐭紮鎷烽敓鏂ゆ嫹閿熷彨杈炬嫹閿熺氮ession閿熸枻鎷烽敓鐨嗘唻鎷穋onfirm閿熸枻鎷峰啓閿熸枻鎷烽敓鏂ゆ嫹鑿橀敓锟�
		resultData.put("totalChargesAmount", (Object) new Double(
				totalChargesAmount));
		this.setUsrSessionDataValue("fileRequest", fileRequest);
		this.setUsrSessionDataValue("beneficiaryNameString",
				beneficiaryNameString);
		this.setUsrSessionDataValue("beneficiaryName2String",
				beneficiaryName2String);
		this.setUsrSessionDataValue("beneficiaryName3String",
				beneficiaryName3String);
		this.setUsrSessionDataValue("cashierAmountString", cashierAmountString);
		// add by lw 20100902
		this.setUsrSessionDataValue("purposeString", purposeString);
		this.setUsrSessionDataValue("proofOfPurpose", proofOfPurpose);
		//this.setUsrSessionDataValue("otherPurposeString", otherPurposeString);
		// add by lw end
		this.setUsrSessionDataValue("draftChargeString", draftChargeString);
		this.setUsrSessionDataValue("inputList", inputList);

	}

	public void addConfirm() throws NTBException {
		// 閿熸枻鎷峰閿熸枻鎷稴ervice
		ApplicationContext appContext = Config.getAppContext();
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		FileRequest fileRequest = (FileRequest) this
				.getUsrSessionDataValue("fileRequest");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");
		// String totalChargesAmount =
		// this.getUsrSessionDataValue("totalChargesAmount").toString();
		String[] beneficiaryNameString = new String[21];
		String[] beneficiaryName2String = new String[21];
		String[] beneficiaryName3String = new String[21];
		String[] cashierAmountString = new String[21];
		String[] draftChargeString = new String[21];
		// add by lw 20100902
		TransferService transferService = (TransferService) appContext
		.getBean("TransferService");
		Map resultData = new HashMap();
		//String[] purposeCodeString = new String[21];
		String[] purposeString = new String[21];
		// add by lw end

		List inputList = new ArrayList();
		CorpUser corpUser = (CorpUser) this.getUser();
		beneficiaryNameString = (String[]) this
				.getUsrSessionDataValue("beneficiaryNameString");
		beneficiaryName2String = (String[]) this
				.getUsrSessionDataValue("beneficiaryName2String");
		beneficiaryName3String = (String[]) this
				.getUsrSessionDataValue("beneficiaryName3String");

		cashierAmountString = (String[]) this
				.getUsrSessionDataValue("cashierAmountString");
		// add by lw 20100902
//		purposeCodeString = (String[]) this
//				.getUsrSessionDataValue("purposeCodeString");
		purposeString = (String[]) this
				.getUsrSessionDataValue("purposeString");
		// add by lw end
		draftChargeString = (String[]) this
				.getUsrSessionDataValue("draftChargeString");
		inputList = (List) this.getUsrSessionDataValue("inputList");
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), fileRequest.getToCurrency(), "MOP",
				new BigDecimal(fileRequest.getToAmount().toString()), null, 2);
		String assignedUser = Utils.null2EmptyWithTrim(this
				.getParameter("assignedUser"));
		String mailUser = Utils.null2EmptyWithTrim(this
				.getParameter("mailUser"));
		// check value date cut-off time add by mxl 1123
		checkCutoffTimeAndSetMsg(fileRequest);
		// add by chen_y for CR225 20170412
		TransferLimitService transferLimitService = (TransferLimitService) appContext.getBean("TransferLimitService");
		if (!transferLimitService.checkLimitQuota(fileRequest.getFromAccount(),
				corpUser.getCorpId(), Constants.TXN_TYPE_CASHIER_ORDER,
				fileRequest.getFromAmount().doubleValue(), equivalentMOP
						.doubleValue())) {
			// write limit report
			Map reportMap = new HashMap();
			reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
					"yyyyMMdd"));
			reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
					"HHmmss"));
			reportMap.put("USER_ID", fileRequest.getUserId());
			reportMap.put("CORP_ID", corpUser.getCorpId());
			reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_CASHIER_ORDER);
			reportMap.put("LOCAL_TRANS_CODE", fileRequest.getBatchId());
			reportMap.put("FROM_CURRENCY", fileRequest.getFromCurrency());
			reportMap.put("FROM_ACCOUNT", fileRequest.getFromAccount());
			reportMap.put("TO_CURRENCY", fileRequest.getToCurrency());
			reportMap.put("FROM_AMOUNT", fileRequest.getFromAmount());
			reportMap.put("TO_AMOUNT", fileRequest.getToAmount());
			reportMap.put("EXCEEDING_TYPE", "1");
			reportMap.put("LIMIT_TYPE", transferLimitService.getLimitType());
			reportMap.put("USER_LIMIT ", "0");
			reportMap.put("DAILY_LIMIT ", new Double(transferLimitService
					.getDailyLimit()));
			reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService
					.getTotalLimit()));
			UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
			//throw new NTBWarningException("err.txn.ExceedDailyLimit");
			setMessage(RBFactory.getInstance("app.cib.resource.common.errmsg").getString("warnning.txn.ExceedDailyLimit"));
		}
		// add by chen_y for CR225 20170412 end
		// 閿熼摪鏂ゆ嫹涓�敓鏂ゆ嫹閿熸枻鎷锋潈閿熸枻鎷烽敓鏂ゆ嫹 add by mxl 1109
		String processId = flowEngineService.startProcess(
				Constants.TXN_SUBTYPE_CASHIER_ORDER,
				FlowEngineService.TXN_CATEGORY_FINANCE, CashOrderAction.class,
				fileRequest.getFromCurrency(), fileRequest.getFromAmount()
						.doubleValue(), fileRequest.getToCurrency(),
				fileRequest.getToAmount().doubleValue(), equivalentMOP
						.doubleValue(), fileRequest.getBatchId(), fileRequest
						.getRemark(), getUser(), assignedUser, corpUser
						.getCorporation().getAllowExecutor(),
				Constants.INPUT_AMOUNT_FLAG_TO);
		try {
			fileRequest.setRequestTime(new Date());
			fileRequest.setExecuteTime(new Date());
			fileRequest.setFileName("#");
			fileRequest.setBatchResult("--");
			fileRequest.setBatchType(FileRequest.CASH_ORDER_BATCH_TYPE);
			fileRequest.setStatus(Constants.STATUS_PENDING_APPROVAL);
			requestService.addFileRequest(fileRequest);
			for (int i = 1; i <= fileRequest.getTotalNumber().intValue(); i++) {
				ReqCashierOrder reqCashierOrder = new ReqCashierOrder(
						CibIdGenerator.getRefNoForTransaction());
				reqCashierOrder.setRequestTime(new Date());
				reqCashierOrder.setExecuteTime(new Date());
				reqCashierOrder.setDetailResult("--");
				reqCashierOrder.setUserId(corpUser.getUserId());
				reqCashierOrder.setCorpId(corpUser.getCorpId());
				reqCashierOrder.setStatus(Constants.STATUS_PENDING_APPROVAL);
				reqCashierOrder.setBatchId(fileRequest.getBatchId());
				reqCashierOrder
						.setBeneficiaryName(beneficiaryNameString[i - 1]);
				reqCashierOrder
						.setBeneficiaryName2(beneficiaryName2String[i - 1]);
				reqCashierOrder
						.setBeneficiaryName3(beneficiaryName3String[i - 1]);
				reqCashierOrder.setCashierAmount(new Double(
						cashierAmountString[i - 1]));
				reqCashierOrder.setChargeAmount(new Double(
						draftChargeString[i - 1]));
				// add by lw 20100902
				if(purposeString[i - 1] != null && !"".equals(purposeString[i - 1])){
					reqCashierOrder.setOtherPurpose(purposeString[i - 1]);
					reqCashierOrder.setPurposeCode("99");
				}
				/*
				if (purposeCodeString[i - 1] != null
						&& !"".equals(purposeCodeString[i - 1])) {
					if (purposeCodeString[i - 1].equals("99")) {
						if (otherPurposeString[i - 1] != null
								&& !"".equals(otherPurposeString[i - 1])) {
							reqCashierOrder
									.setOtherPurpose(otherPurposeString[i - 1]);
						}
					} else {
						reqCashierOrder
								.setPurposeCode(purposeCodeString[i - 1]);
					}
				}
				*/
				String corpId = corpUser.getCorpId();
				String fromAccount = "";
				String fromCurrency = "";
				String chargeAccount = null;
				if (fileRequest.getChargeFlag().equals("1")) {
					chargeAccount = "0";
				} else if (fileRequest.getChargeFlag().equals("2")) {
					chargeAccount = fileRequest.getChargeAccount();
				}
				if (chargeAccount.equals("0")) {
					fromAccount = fileRequest.getFromAccount().trim();
					fromCurrency = fileRequest.getFromCurrency();
				} else {
					fromAccount = fileRequest.getChargeAccount().trim();
					fromCurrency = fileRequest.getChargeCurrency();
				}
				String draftAmount = reqCashierOrder.getCashierAmount()
						.toString();
				String chargeAmount = reqCashierOrder.getChargeAmount()
						.toString();
				String toCurrency = fileRequest.getToCurrency();
				
				//modified by lzg 20190602
				/*int checkPurpose = transferService.checkNeedPurpose(corpId,
						fromAccount, draftAmount, chargeAmount, fromCurrency,
						toCurrency, "MO");
				String purpose = reqCashierOrder.getOtherPurpose();
				if(purpose != null && !"".equals(purpose)){
					reqCashierOrder.setOtherPurpose(purpose);
					reqCashierOrder.setPurposeCode("99");
				}

				if (checkPurpose == 1) {
					reqCashierOrder.setProofOfPurpose("N");
				} else if (checkPurpose == 2) {
					reqCashierOrder.setProofOfPurpose("Y");
				} else {
					reqCashierOrder.setProofOfPurpose("N");
					reqCashierOrder.setPurposeCode("");
					reqCashierOrder.setOtherPurpose("");
				}*/
				// modified by lzg end
				reqCashierOrder.setBankAddress1("");
				reqCashierOrder.setBankAddress2("");
				reqCashierOrder.setSenderReference("");
				requestService.addCashierOrder(reqCashierOrder);
				// add by lw 20100902
//				if (reqCashierOrder.getProofOfPurpose().equals("Y")) {
//					resultData.put("needProof", "Y");
//				}
				// add by lw end
			}

			resultData.put("txnTypeField", "txnType");
			resultData.put("currencyField", "currency");
			resultData.put("amountField", "amount");
			resultData.put("amountMopEqField", "amountMopEq");

			resultData.put("txnType", Constants.TXN_SUBTYPE_CASHIER_ORDER);
			resultData.put("currency", fileRequest.getToCurrency());
			resultData.put("amount", fileRequest.getToAmount());
			resultData.put("amountMopEq", equivalentMOP);
			this.convertPojo2Map(fileRequest, resultData);
			resultData.put("inputList", inputList);
			setResultData(resultData);
			// send mail to approver
			// 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸纭锋嫹 add by mxl 1225
			String[] userName = mailUser.split(";");
			Map dataMap = new HashMap();
			dataMap.put("userID", corpUser.getUserId());
			dataMap.put("userName", corpUser.getUserName());
			dataMap.put("requestTime", fileRequest.getRequestTime());
			dataMap.put("batchId", fileRequest.getBatchId());
			dataMap.put("fromCurrency", fileRequest.getFromCurrency());
			dataMap.put("fromAmount", fileRequest.getFromAmount());
			dataMap.put("fromAccount", fileRequest.getFromAccount());
			dataMap.put("corpName", corpUser.getCorporation().getCorpName());
			dataMap.put("remark", fileRequest.getRemark());
			
			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template begin */
			/*mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_CASHIER_ORDER, userName, dataMap);*/
			mailService.toApprover_Seleted(Constants.TXN_SUBTYPE_CASHIER_ORDER, userName,corpUser.getCorpId(), dataMap);
			/* Add by long_zg 2015-05-22 UAT6-242 第三者轉賬operator成功頁缺少save as template end */
			
		} catch (Exception e) {
			flowEngineService.cancelProcess(processId, getUser());
			Log.error("Error  CashOrderAction", e);
			throw new NTBException("err.txn.TranscationFaily");
		}

	}

	public boolean approve(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferLimitService transferLimitService = (TransferLimitService) appContext
				.getBean("TransferLimitService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		CorpUser corpUser = (CorpUser) bean.getUser();
		if (txnType != null) {
			FileRequest pojoFileRequest = requestService.viewFileRequest(id);
			BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
					.getCorpId(), pojoFileRequest.getFromCurrency(), "MOP",
					new BigDecimal(pojoFileRequest.getFromAmount().toString()),
					null, 2);
			if (!transferLimitService.checkLimitQuota(pojoFileRequest
					.getFromAccount(), corpUser.getCorpId(),
					Constants.TXN_TYPE_CASHIER_ORDER, pojoFileRequest
							.getFromAmount().doubleValue(), equivalentMOP
							.doubleValue())) {
				// write limit report
				Map reportMap = new HashMap();
				reportMap.put("TRANS_DATE", DateTime.formatDate(new Date(),
						"yyyyMMdd"));
				reportMap.put("TRANS_TIME", DateTime.formatDate(new Date(),
						"HHmmss"));
				reportMap.put("USER_ID", pojoFileRequest.getUserId());
				reportMap.put("CORP_ID", corpUser.getCorpId());
				reportMap.put("TRANS_TYPE", Constants.TXN_TYPE_CASHIER_ORDER);
				reportMap.put("LOCAL_TRANS_CODE", pojoFileRequest.getBatchId());
				reportMap.put("FROM_CURRENCY", pojoFileRequest
						.getFromCurrency());
				reportMap.put("TO_CURRENCY", pojoFileRequest.getToCurrency());
				reportMap.put("FROM_AMOUNT", pojoFileRequest.getFromAmount());
				reportMap.put("TO_AMOUNT", pojoFileRequest.getToAmount());
				reportMap.put("EXCEEDING_TYPE", "1");
				reportMap
						.put("LIMIT_TYPE", transferLimitService.getLimitType());
				reportMap.put("FROM_ACCOUNT", pojoFileRequest.getFromAccount());
				reportMap.put("USER_LIMIT ", "0");
				reportMap.put("DAILY_LIMIT ", new Double(transferLimitService
						.getDailyLimit()));
				reportMap.put("TOTAL_AMOUNT ", new Double(transferLimitService
						.getTotalLimit()));
				UploadReporter.writeBuffer("RP_EXCLIMIT", reportMap);
				throw new NTBWarningException("err.txn.ExceedDailyLimit");
			}
			requestService.toHostCashierOrder(pojoFileRequest.getBatchId(),
					corpUser);
			return true;
		} else {
			return false;
		}
	}

	public boolean cancel(String txnType, String id, CibAction bean)
			throws NTBException {
		return this.reject(txnType, id, bean);
	}

	public boolean reject(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CashOrderBatchService cashOrderBatchService = (CashOrderBatchService) appContext
				.getBean("CashOrderBatchService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		if (txnType != null) {
			FileRequest pojoFileRequest = requestService.viewFileRequest(id);
			cashOrderBatchService.rejectFileRequest(pojoFileRequest);
			return true;
		} else {
			return false;
		}
	}

	public String viewDetail(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");

		String viewPageUrl = "";
		Map resultData = bean.getResultData();
		CorpUser corpUser = (CorpUser) bean.getUser();
		FileRequest pojoFileRequest = requestService.viewFileRequest(id);

		// 閿熸枻鎷烽敓绲檃tchId閿熻妭鎲嬫嫹ReqCashierOrder閿熸枻鎷疯閿熸枻鎷峰綍
		List requestList = requestService
				.listCashierOrderByBatchid(pojoFileRequest.getBatchId());
		List inputList = this.convertPojoList2MapList(requestList);
		viewPageUrl = "/WEB-INF/pages/srv/request/cashierOrder_approval_view.jsp";

		// 閿熸枻鎷风粐assignuser_tag閿熸枻鎷烽敓锟�
		Map assignuser = new HashMap();
		BigDecimal equivalentMOP = exRatesService.getEquivalent(corpUser
				.getCorpId(), pojoFileRequest.getToCurrency(), "MOP",
				new BigDecimal(pojoFileRequest.getToAmount().toString()), null,
				2);

		assignuser.put("txnTypeField", "txnType");
		assignuser.put("currencyField", "currency");
		assignuser.put("amountField", "totalAmount");
		assignuser.put("amountMopEqField", "amountMopEq");
		assignuser.put("txnType", txnType);
		assignuser.put("amountMopEq", equivalentMOP);

		bean.convertPojo2Map(pojoFileRequest, resultData);
		// add by lw 20101007
		//modified by lzg 20190602
		/*if (null != requestList && requestList.size() > 0){
			for (int i = 0; i < inputList.size(); i++) {
				ReqCashierOrder reqCashierOrder = (ReqCashierOrder) requestList.get(i);
				String proofOfPurpose = reqCashierOrder.getProofOfPurpose();
				if(proofOfPurpose != null && !"".equals(proofOfPurpose)){
					if (proofOfPurpose.equals("Y")) {
						resultData.put("needProof", "Y");
					}
				}
			}
		}*/
		//modified by lzg end
		// add by lw end
		// 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷疯幐閿熺禈ssignuser_tag

		resultData.put("currency", pojoFileRequest.getToCurrency());
		resultData.putAll(assignuser);
		resultData.put("inputList", inputList);
		bean.setResultData(resultData);
		return viewPageUrl;
	}

	private void checkCutoffTimeAndSetMsg(FileRequest pojoFileRequest)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CutOffTimeService cutOffTimeService = (CutOffTimeService) appContext
				.getBean("CutOffTimeService");

		// check value date cut-off time
		setMessage(cutOffTimeService.check("ZC06", pojoFileRequest
				.getFromCurrency(), pojoFileRequest.getToCurrency()));

	}

	public void addCancel() throws NTBException {
		FileRequest fileRequest = (FileRequest) this
				.getUsrSessionDataValue("fileRequest");

		// 鍙朾eneficiaryName閿熸枻鎷穋ashierAmount閿熸枻鎷峰�
		String[] beneficiaryNameString = new String[21];
		String[] beneficiaryName2String = new String[21];
		String[] beneficiaryName3String = new String[21];
		String[] cashierAmountString = new String[21];
		// add by lw 20100902
//		String[] purposeCodeString = new String[21];
		String[] purposeString = new String[21];
		String proofOfPurpose = null;
		// add by lw end

		beneficiaryNameString = (String[]) this
				.getUsrSessionDataValue("beneficiaryNameString");
		beneficiaryName2String = (String[]) this
				.getUsrSessionDataValue("beneficiaryName2String");
		beneficiaryName3String = (String[]) this
				.getUsrSessionDataValue("beneficiaryName3String");
		cashierAmountString = (String[]) this
				.getUsrSessionDataValue("cashierAmountString");
		// add by lw 20100902
//		purposeCodeString = (String[]) this
//				.getUsrSessionDataValue("purposeCodeString");
		purposeString = (String[]) this
				.getUsrSessionDataValue("purposeString");
		proofOfPurpose = (String) this.getUsrSessionDataValue("proofOfPurpose");
		// add by lw end

		// 閿熸枻鎷烽敓鏂ゆ嫹List
		List inputList = new ArrayList();
		for (int i = 1; i <= fileRequest.getTotalNumber().intValue(); i++) {
			Map inputCashierOrderData = new HashMap();
			inputCashierOrderData.put("index", String.valueOf(i));
			inputCashierOrderData.put("beneficiaryName",
					beneficiaryNameString[i - 1]);
			inputCashierOrderData.put("beneficiaryName2",
					beneficiaryName2String[i - 1]);
			inputCashierOrderData.put("beneficiaryName3",
					beneficiaryName3String[i - 1]);
			inputCashierOrderData.put("cashierAmount",
					cashierAmountString[i - 1]);
			// add by lw 20100902
			if(purposeString[i - 1] != null && !"".equals(purposeString[i - 1])){
				inputCashierOrderData.put("showPurpose", "true");
				inputCashierOrderData.put("purposeString", purposeString[i - 1]);
				inputCashierOrderData.put("purposeCode","99");
			}
			if(proofOfPurpose.equals("on")){
				inputCashierOrderData.put("showProof", "true");
			}else{
				inputCashierOrderData.put("showProof", "false");
			}
			/*
			if (purposeCodeString[i - 1] != null
					&& !"".equals(purposeCodeString[i - 1])) {
				inputCashierOrderData.put("showPurpose", "true");
				if (purposeCodeString[i - 1].equals("99")) {
					if (otherPurposeString[i - 1] != null
							&& !"".equals(otherPurposeString[i - 1])) {
						inputCashierOrderData.put("showOthers", "true");
						inputCashierOrderData.put("otherPurposeString",
								otherPurposeString[i - 1]);
					}
				} else {
					inputCashierOrderData.put("showOthers", "false");
					inputCashierOrderData.put("purposeCodeString",
							purposeCodeString[i - 1]);
				}
			} else {
				inputCashierOrderData.put("showPurpose", "false");
			}
			*/
			// add by lw end
			inputList.add(inputCashierOrderData);
		}

		Map resultData = new HashMap();
		resultData.put("inputList", inputList);
		this.convertPojo2Map(fileRequest, resultData);
		this.setResultData(resultData);

		/*
		 * 閿熷彨璁规嫹閿熸枻鎷烽敓鏂ゆ嫹鎱曢敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹缁嗛敓鏂ゆ嫹閿熸枻鎷峰垗閿熻顕嗘嫹閿熸枻鎷锋瑺閿熸枻鎷烽敓鏂ゆ嫹 if (!(inputTotalAmount ==
		 * fileRequest.getToAmount().doubleValue())) { throw new
		 * NTBException("err.txn.totalAmountIsError"); }
		 */
		// 閿熸枻鎷烽敓鐭紮鎷烽敓鏂ゆ嫹閿熷彨杈炬嫹閿熺氮ession閿熸枻鎷烽敓鐨嗘唻鎷穋onfirm閿熸枻鎷峰啓閿熸枻鎷烽敓鏂ゆ嫹鑿橀敓锟�
		this.setUsrSessionDataValue("fileRequest", fileRequest);
		this.setUsrSessionDataValue("beneficiaryNameString",
				beneficiaryNameString);
		this.setUsrSessionDataValue("beneficiaryName2String",
				beneficiaryName2String);
		this.setUsrSessionDataValue("beneficiaryName3String",
				beneficiaryName3String);
		this.setUsrSessionDataValue("cashierAmountString", cashierAmountString);
		// add by lw 20100902
//		this.setUsrSessionDataValue("purposeCodeString", purposeCodeString);
		this.setUsrSessionDataValue("purposeString", purposeString);
		this.setUsrSessionDataValue("proofOfPurpose", proofOfPurpose);
		// add by lw end
		this.setUsrSessionDataValue("inputList", inputList);
	}
}
