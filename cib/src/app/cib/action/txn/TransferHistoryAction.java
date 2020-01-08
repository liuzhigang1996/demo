package app.cib.action.txn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

import app.cib.bo.sys.CorpUser;
import app.cib.bo.bat.FileRequest;
import app.cib.bo.bat.TransferBankStm;
import app.cib.bo.bat.TransferMacauStm;
import app.cib.bo.bat.TransferOverseaStm;
import app.cib.bo.txn.TransferBank;
import app.cib.bo.txn.TransferMacau;
import app.cib.bo.txn.TransferOversea;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;

import app.cib.service.flow.FlowEngineService;
import app.cib.service.srv.RequestService;
import app.cib.service.txn.TransferService;
import app.cib.util.Constants;
import app.cib.util.TransferConstants;

public class TransferHistoryAction extends CibAction {
	public void listLoad() throws NTBException {
		HashMap resultData = new HashMap(1);
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccount(corpUser
				.getCorpId()));
		this.setResultData(resultData);
	}

	public void listHistory() throws NTBException {
		
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext.getBean("TransferService");
		String isBack = Utils.null2Empty(getParameter("isBack"));
		Map resultData = new HashMap(1);
		if(!"".equals(isBack)){
			resultData.putAll(this.getParameters());
		}
//		setResultData(new HashMap(1));
//		Map resultData = new HashMap();

		CorpUser user = (CorpUser) this.getUser();
		String userId = user.getUserId();
		String corpId = user.getCorpId();
		String approverFlag = user.getCorporation().getAllowApproverSelection();
		
		

		FlowEngineService flowEngineService = (FlowEngineService) appContext
				.getBean("FlowEngineService");

		Date dateFrom = null;
		Date dateTo = null;
		String fromAccount = null;
		String business = null;
		String changeFlag = null;

		fromAccount = Utils.null2EmptyWithTrim(getParameter("fromAccount"));
		business = Utils.null2EmptyWithTrim(getParameter("business_type"));

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
		// add by hjs 20070321
		Locale locale = (this.getUser().getLanguage() == null) ? Config
				.getDefaultLocale() : this.getUser().getLanguage();
		RBFactory rbFactory = RBFactory.getInstance(
				"app.cib.resource.rpt.txn_report", locale.toString());
		/* if (TransferConstants.TRANSFER_BANK_1TO1.equals(business)) { */
		if (TransferConstants.TRANSFER_BANK_1TO1.equals(business)
				|| TransferConstants.TRANSFER_BANK_3RD_1TO1.equals(business)) {
			List transferList = transferService.transferHistory(business,
					dateFrom, dateTo, corpId, userId, fromAccount);

			List toList = new ArrayList();
			Map tranBankData = null;
			String currency = null;
			String transferAmount = null;

			for (int i = 0; i < transferList.size(); i++) {
				TransferBank transferBank = (TransferBank) transferList.get(i);

				String ownerAccFlag = transferBank.getOwnerAccFlag();
				String txnType = Constants.NO.equals(ownerAccFlag) ? Constants.TXN_TYPE_TRANSFER_BANK
						: Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT;
				if ("Y".equals(approverFlag)) {
					if ("1".equals(transferBank.getStatus())) {
						if (flowEngineService.checkApproveComplete(
						/* Constants.TXN_SUBTYPE_TRANSFER_BANK, */
						txnType,
						transferBank.getTransId())) {
							changeFlag = "N";
						} else {
							changeFlag = "Y";
						}
					}
				}

				tranBankData = new HashMap();
				if (transferBank.getInputAmountFlag().equals(
						Constants.INPUT_AMOUNT_FLAG_FROM)) {
					currency = transferBank.getFromCurrency();
				} else if (transferBank.getInputAmountFlag().equals(
						Constants.INPUT_AMOUNT_FLAG_TO)) {
					currency = transferBank.getToCurrency();
				}
				if (transferBank.getInputAmountFlag().equals(
						Constants.INPUT_AMOUNT_FLAG_FROM)) {
					transferAmount = transferBank.getDebitAmount().toString();
				} else if (transferBank.getInputAmountFlag().equals(
						Constants.INPUT_AMOUNT_FLAG_TO)) {
					transferAmount = transferBank.getTransferAmount()
							.toString();
				}
				convertPojo2Map(transferBank, tranBankData);
				tranBankData.put("changeFlag", changeFlag);
				tranBankData.put("currency", currency);
				tranBankData.put("transferAmount", transferAmount);
				toList.add(tranBankData);

			}
			// transferList = this.convertPojoList2MapList(transferList);

			resultData.put("business_type", business);
			resultData.put("dateFrom", dateFrom);
			resultData.put("dateTo", dateTo);
			resultData.put("toList", toList);
			resultData.put("fromAccount", fromAccount);
//			resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK);
			String txnType = TransferConstants.TRANSFER_BANK_1TO1.equals(business)?
					Constants.TXN_TYPE_TRANSFER_OWN_ACCOUNT:Constants.TXN_SUBTYPE_TRANSFER_BANK;
			resultData.put("txnType", txnType);//mod by linrui 20190718
			resultData.put("methodType", "viewInBANK");
			resultData.put("approverFlag", approverFlag);
			resultData.put("uploadStatusFlag", "N");

		} else if (TransferConstants.TRANSFER_MACAU_1TO1.equals(business)) {
			List transferList = transferService.transferHistory(business,
					dateFrom, dateTo, corpId, userId, fromAccount);
			List toList = new ArrayList();
			String currency = null;
			String transferAmount = null;
			for (int i = 0; i < transferList.size(); i++) {
				TransferMacau transferMacau = (TransferMacau) transferList
						.get(i);
				if ("Y".equals(approverFlag)) {
					if ("1".equals(transferMacau.getStatus())) {
						if (flowEngineService.checkApproveComplete(
								Constants.TXN_SUBTYPE_TRANSFER_MACAU,
								transferMacau.getTransId())) {
							changeFlag = "N";
						} else {
							changeFlag = "Y";
						}
					}
				}
				if (transferMacau.getInputAmountFlag().equals(
						Constants.INPUT_AMOUNT_FLAG_FROM)) {
					currency = transferMacau.getFromCurrency();
				} else if (transferMacau.getInputAmountFlag().equals(
						Constants.INPUT_AMOUNT_FLAG_TO)) {
					currency = transferMacau.getToCurrency();
				}
				if (transferMacau.getInputAmountFlag().equals(
						Constants.INPUT_AMOUNT_FLAG_FROM)) {
					transferAmount = transferMacau.getDebitAmount().toString();
				} else if (transferMacau.getInputAmountFlag().equals(
						Constants.INPUT_AMOUNT_FLAG_TO)) {
					transferAmount = transferMacau.getTransferAmount()
							.toString();
				}
				Map tranMacauData = new HashMap();
				this.convertPojo2Map(transferMacau, tranMacauData);
				tranMacauData.put("changeFlag", changeFlag);
				tranMacauData.put("currency", currency);
				tranMacauData.put("transferAmount", transferAmount);
				toList.add(tranMacauData);

			}

			resultData.put("business_type", business);
			resultData.put("dateFrom", dateFrom);
			resultData.put("dateTo", dateTo);
			resultData.put("toList", toList);
			resultData.put("fromAccount", fromAccount);
			resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_MACAU);
			resultData.put("methodType", "viewInMacau");
			resultData.put("approverFlag", approverFlag);
			resultData.put("uploadStatusFlag", "N");

		} else if (TransferConstants.TRANSFER_OVERSEA_1TO1.equals(business)) {
			List transferList = transferService.transferHistory(business,
					dateFrom, dateTo, corpId, userId, fromAccount);
			List toList = new ArrayList();
			String currency = null;
			String transferAmount = null;
			for (int i = 0; i < transferList.size(); i++) {
				TransferOversea transferOversea = (TransferOversea) transferList
						.get(i);
				if ("Y".equals(approverFlag)) {
					if ("1".equals(transferOversea.getStatus())) {
						if (flowEngineService.checkApproveComplete(
								Constants.TXN_SUBTYPE_TRANSFER_OVERSEAS,
								transferOversea.getTransId())) {
							changeFlag = "N";
						} else {
							changeFlag = "Y";
						}
					}
				}
				if (transferOversea.getInputAmountFlag().equals(
						Constants.INPUT_AMOUNT_FLAG_FROM)) {
					currency = transferOversea.getFromCurrency();
				} else if (transferOversea.getInputAmountFlag().equals(
						Constants.INPUT_AMOUNT_FLAG_TO)) {
					currency = transferOversea.getToCurrency();
				}
				if (transferOversea.getInputAmountFlag().equals(
						Constants.INPUT_AMOUNT_FLAG_FROM)) {
					transferAmount = transferOversea.getDebitAmount()
							.toString();
				} else if (transferOversea.getInputAmountFlag().equals(
						Constants.INPUT_AMOUNT_FLAG_TO)) {
					transferAmount = transferOversea.getTransferAmount()
							.toString();
				}
				Map tranOverseaData = new HashMap();
				this.convertPojo2Map(transferOversea, tranOverseaData);
				tranOverseaData.put("changeFlag", changeFlag);
				tranOverseaData.put("currency", currency);
				tranOverseaData.put("transferAmount", transferAmount);
				toList.add(tranOverseaData);

			}

			resultData.put("business_type", business);
			resultData.put("dateFrom", dateFrom);
			resultData.put("dateTo", dateTo);
			resultData.put("toList", toList);
			resultData.put("fromAccount", fromAccount);
			resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_OVERSEAS);
			resultData.put("methodType", "viewInOversea");
			resultData.put("approverFlag", approverFlag);
			resultData.put("uploadStatusFlag", "N");
		} else if (TransferConstants.TRANSFER_BANK_1TOM.equals(business)) {
			List transferList = transferService.fileRequestHistory(
					FileRequest.TRANSFER_BANK_STM, dateFrom, dateTo, corpId,
					userId, fromAccount);
			List toList = new ArrayList();
			for (int i = 0; i < transferList.size(); i++) {
				FileRequest fileRequest = (FileRequest) transferList.get(i);
				if ("Y".equals(approverFlag)) {
					if ("1".equals(fileRequest.getStatus())) {
						if (flowEngineService.checkApproveComplete(
								Constants.TXN_SUBTYPE_TRANSFER_BANK_1TON,
								fileRequest.getBatchId())) {
							changeFlag = "N";
						} else {
							changeFlag = "Y";
						}
					}
				}
				Map fileRequestData = new HashMap();
				this.convertPojo2Map(fileRequest, fileRequestData);
				fileRequestData.put("changeFlag", changeFlag);
				fileRequestData.put("inputAmountFlag", "1");
				fileRequestData
						.put("transferAmount", fileRequest.getToAmount());
				fileRequestData.put("currency", fileRequest.getToCurrency());
				fileRequestData.put("transId", fileRequest.getBatchId());
				// add by hjs 20070322
				fileRequestData
						.put("toAccount", rbFactory.getString("mul_acc"));
				toList.add(fileRequestData);

			}
			resultData.put("business_type", business);
			resultData.put("dateFrom", dateFrom);
			resultData.put("dateTo", dateTo);
			resultData.put("toList", toList);
			resultData.put("fromAccount", fromAccount);
			resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK_1TON);
			resultData.put("methodType", "viewInBankStM");
			resultData.put("approverFlag", approverFlag);
			resultData.put("uploadStatusFlag", "Y");
		} else if (TransferConstants.TRANSFER_MACAU_1TOM.equals(business)) {
			List transferList = transferService.fileRequestHistory(
					FileRequest.TRANSFER_MACAU_STM, dateFrom, dateTo, corpId,
					userId, fromAccount);
			List toList = new ArrayList();
			for (int i = 0; i < transferList.size(); i++) {
				FileRequest fileRequest = (FileRequest) transferList.get(i);
				if ("Y".equals(approverFlag)) {
					if ("1".equals(fileRequest.getStatus())) {
						if (flowEngineService.checkApproveComplete(
								Constants.TXN_SUBTYPE_TRANSFER_MACAU_1TON,
								fileRequest.getBatchId())) {
							changeFlag = "N";
						} else {
							changeFlag = "Y";
						}
					}
				}
				Map fileRequestData = new HashMap();
				this.convertPojo2Map(fileRequest, fileRequestData);
				fileRequestData.put("changeFlag", changeFlag);
				fileRequestData
						.put("transferAmount", fileRequest.getToAmount());
				fileRequestData.put("inputAmountFlag", "1");
				fileRequestData.put("currency", fileRequest.getToCurrency());
				fileRequestData.put("transId", fileRequest.getBatchId());
				// add by hjs 20070322
				fileRequestData
						.put("toAccount", rbFactory.getString("mul_acc"));
				toList.add(fileRequestData);

			}
			resultData.put("business_type", business);
			resultData.put("dateFrom", dateFrom);
			resultData.put("dateTo", dateTo);
			resultData.put("toList", toList);
			resultData.put("fromAccount", fromAccount);
			resultData
					.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_MACAU_1TON);
			resultData.put("methodType", "viewInMacauStM");
			resultData.put("approverFlag", approverFlag);
			resultData.put("uploadStatusFlag", "Y");
		} else if (TransferConstants.TRANSFER_OVERSEA_1TOM.equals(business)) {
			List transferList = transferService.fileRequestHistory(
					FileRequest.TRANSFER_OVERSEA_STM, dateFrom, dateTo, corpId,
					userId, fromAccount);
			List toList = new ArrayList();
			for (int i = 0; i < transferList.size(); i++) {
				FileRequest fileRequest = (FileRequest) transferList.get(i);
				if ("Y".equals(approverFlag)) {
					if ("1".equals(fileRequest.getStatus())) {
						if (flowEngineService.checkApproveComplete(
								Constants.TXN_SUBTYPE_TRANSFER_OVERSEAS_1TON,
								fileRequest.getBatchId())) {
							changeFlag = "N";
						} else {
							changeFlag = "Y";
						}
					}
				}
				Map fileRequestData = new HashMap();
				this.convertPojo2Map(fileRequest, fileRequestData);
				fileRequestData.put("changeFlag", changeFlag);
				fileRequestData
						.put("transferAmount", fileRequest.getToAmount());
				fileRequestData.put("transId", fileRequest.getBatchId());
				fileRequestData.put("toCurrency", fileRequest.getToCurrency());
				fileRequestData.put("currency", fileRequest.getToCurrency());
				fileRequestData.put("inputAmountFlag", "1");
				// add by hjs 20070322
				fileRequestData
						.put("toAccount", rbFactory.getString("mul_acc"));
				toList.add(fileRequestData);

			}
			resultData.put("business_type", business);
			resultData.put("dateFrom", dateFrom);
			resultData.put("dateTo", dateTo);
			resultData.put("toList", toList);
			resultData.put("fromAccount", fromAccount);
			resultData.put("txnType",
					Constants.TXN_SUBTYPE_TRANSFER_OVERSEAS_1TON);
			resultData.put("methodType", "viewInOverseaStM");
			resultData.put("approverFlag", approverFlag);
			resultData.put("uploadStatusFlag", "Y");
		} else if (TransferConstants.TRANSFER_BANK_MTO1.equals(business)) {
			// System.out.println("toAccount=" + fromAccount);

			// actually)
			List transferList = transferService.fileRequestHistoryMtS(
					FileRequest.TRANSFER_BANK_MTS, dateFrom, dateTo, corpId,
					userId, fromAccount);
			List toList = new ArrayList();
			for (int i = 0; i < transferList.size(); i++) {
				FileRequest fileRequest = (FileRequest) transferList.get(i);
				if ("Y".equals(approverFlag)) {
					if ("1".equals(fileRequest.getStatus())) {
						if (flowEngineService.checkApproveComplete(
								Constants.TXN_SUBTYPE_TRANSFER_BANK_NTO1,
								fileRequest.getBatchId())) {
							changeFlag = "N";
						} else {
							changeFlag = "Y";
						}
					}
				}
				Map fileRequestData = new HashMap();
				this.convertPojo2Map(fileRequest, fileRequestData);
				fileRequestData.put("changeFlag", changeFlag);
				fileRequestData
						.put("transferAmount", fileRequest.getToAmount());
				fileRequestData.put("toCurrency", fileRequest.getToCurrency());
				fileRequestData.put("currency", fileRequest.getToCurrency());
				fileRequestData.put("transId", fileRequest.getBatchId());
				fileRequestData.put("inputAmountFlag", "1");
				// add by hjs 20070322
				fileRequestData.put("fromAccount", rbFactory
						.getString("mul_acc"));
				toList.add(fileRequestData);

			}

			resultData.put("business_type", business);
			resultData.put("dateFrom", dateFrom);
			resultData.put("dateTo", dateTo);
			resultData.put("toList", toList);
			resultData.put("fromAccount", fromAccount);
			resultData.put("txnType", Constants.TXN_SUBTYPE_TRANSFER_BANK_NTO1);
			resultData.put("methodType", "viewInBankMtS");
			resultData.put("approverFlag", approverFlag);
			resultData.put("uploadStatusFlag", "Y");

		}

		CorpUser corpUser = (CorpUser) this.getUser();
		resultData.put("transferuser", transferService.loadAccount(corpUser
				.getCorpId()));
		//add by lzg 20190807
		resultData.put("date_range", date_range);
		//add by lzg end
		setResultData(resultData);
	}

	public void viewInBankMtS() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		setResultData(new HashMap(1));
		String batchId = getParameter("transId");
		FileRequest pojoFileRequest = requestService.viewFileRequest(batchId);
		Map resultData = new HashMap();
		resultData.put("toAccount", pojoFileRequest.getToAccount());
		resultData.put("toCurrency", pojoFileRequest.getToCurrency());
		resultData.put("fromCurrency", pojoFileRequest.getFromCurrency());
		resultData.put("toAmount", pojoFileRequest.getToAmount());

		// Add by heyongjiang 20100512
		resultData.put("toAccountName", pojoFileRequest.getToAccountName());
		resultData.put("toAccountName2", pojoFileRequest.getToAccountName2());

		List requestList = transferService
				.listTransferBankMtsByBatchid(batchId);
		List toList = new ArrayList();
		double totalAmount = 0;
		for (int i = 0; i < requestList.size(); i++) {
			TransferBankStm transferBankStm = (TransferBankStm) requestList
					.get(i);
			totalAmount = totalAmount
					+ (new Double(transferBankStm.getDebitAmount().toString()))
							.doubleValue();
			Map transferBankStmData = new HashMap();
			this.convertPojo2Map(transferBankStm, transferBankStmData);
			transferBankStmData.put("sequenceNo", String.valueOf(i + 1));
			toList.add(transferBankStmData);
		}
		resultData.put("toList", toList);
		resultData.put("totalNumber", String.valueOf(requestList.size()));
		resultData.put("totalAmount", String.valueOf(totalAmount));
		setResultData(resultData);
	}

	public void viewInBankStM() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		setResultData(new HashMap(1));
		String batchID = getParameter("transId");
		FileRequest pojoFileRequest = requestService.viewFileRequest(batchID);
		Map resultData = new HashMap();
		resultData.put("fromAccount", pojoFileRequest.getFromAccount());
		resultData.put("fromCurrency", pojoFileRequest.getFromCurrency());
		resultData.put("toCurrency", pojoFileRequest.getToCurrency());
		resultData.put("fromAmount", pojoFileRequest.getFromAmount());

		List requestList = transferService
				.listTransferBankStmByBatchid(batchID);
		List toList = new ArrayList();
		double totalAmount = 0;
		for (int i = 0; i < requestList.size(); i++) {
			TransferBankStm transferBankStm = (TransferBankStm) requestList
					.get(i);

			totalAmount = totalAmount
					+ (new Double(transferBankStm.getTransferAmount()
							.toString())).doubleValue();
			Map transferBankStmData = new HashMap();
			this.convertPojo2Map(transferBankStm, transferBankStmData);
			transferBankStmData.put("sequenceNo", String.valueOf(i + 1));
			toList.add(transferBankStmData);
		}
		resultData.put("toList", toList);
		resultData.put("totalNumber", String.valueOf(requestList.size()));
		resultData.put("totalAmount", String.valueOf(totalAmount));
		setResultData(resultData);

	}

	public void viewInMacauStM() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		setResultData(new HashMap(1));
		String batchID = getParameter("transId");
		FileRequest pojoFileRequest = requestService.viewFileRequest(batchID);
		Map resultData = new HashMap();
		resultData.put("fromAccount", pojoFileRequest.getFromAccount());
		resultData.put("fromCurrency", pojoFileRequest.getFromCurrency());
		resultData.put("toCurrency", pojoFileRequest.getToCurrency());
		resultData.put("fromAmount", pojoFileRequest.getFromAmount());

		List requestList = transferService
				.listTransferMacauStmByBatchid(batchID);
		List toList = new ArrayList();
		double totalAmount = 0;
		for (int i = 0; i < requestList.size(); i++) {
			TransferMacauStm transferMacauStm = (TransferMacauStm) requestList
					.get(i);
			totalAmount = totalAmount
					+ (new Double(transferMacauStm.getTransferAmount()
							.toString())).doubleValue();
			Map transferMacauStmData = new HashMap();
			this.convertPojo2Map(transferMacauStm, transferMacauStmData);
			transferMacauStmData.put("sequenceNo", String.valueOf(i + 1));
			toList.add(transferMacauStmData);
		}
		resultData.put("toList", toList);
		resultData.put("totalNumber", String.valueOf(requestList.size()));
		resultData.put("totalAmount", String.valueOf(totalAmount));
		setResultData(resultData);
	}

	public void viewInOverseaStM() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		RequestService requestService = (RequestService) appContext
				.getBean("RequestService");
		setResultData(new HashMap(1));
		String batchID = getParameter("transId");
		FileRequest pojoFileRequest = requestService.viewFileRequest(batchID);
		Map resultData = new HashMap();
		resultData.put("fromAccount", pojoFileRequest.getFromAccount());
		resultData.put("fromCurrency", pojoFileRequest.getFromCurrency());
		resultData.put("fromAmount", pojoFileRequest.getFromAmount());
		resultData.put("toCurrency", pojoFileRequest.getToCurrency());

		List requestList = transferService
				.listTransferOverseaStmByBatchid(batchID);
		List toList = new ArrayList();
		double totalAmount = 0;
		for (int i = 0; i < requestList.size(); i++) {
			TransferOverseaStm transferOverseaStm = (TransferOverseaStm) requestList
					.get(i);
			totalAmount = totalAmount
					+ (new Double(transferOverseaStm.getTransferAmount()
							.toString())).doubleValue();
			Map transferOverseaStmData = new HashMap();
			this.convertPojo2Map(transferOverseaStm, transferOverseaStmData);
			transferOverseaStmData.put("sequenceNo", String.valueOf(i + 1));
			toList.add(transferOverseaStmData);
		}
		resultData.put("toList", toList);
		resultData.put("totalNumber", String.valueOf(requestList.size()));
		resultData.put("totalAmount", String.valueOf(totalAmount));
		setResultData(resultData);
	}

	public void viewInBANK() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");

		setResultData(new HashMap(1));
		String transID = getParameter("transId");

		TransferBank transferBank = transferService.viewInBANK(transID);
		Map resultData = new HashMap();
		//add by linrui 20190926
		resultData.putAll(this.getParameters());
		//end
		this.convertPojo2Map(transferBank, resultData);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		resultData.put("executeTime", sdf.format(transferBank.getExecuteTime()));
		setResultData(resultData);
	}

	public void viewInMDB() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");

		setResultData(new HashMap(1));
		String transID = getParameter("transId");

		TransferBank transferBank = transferService.viewInBANK(transID);

		Map resultData = new HashMap();
		//add by linrui 20190926
		resultData.putAll(this.getParameters());
		//end
		this.convertPojo2Map(transferBank, resultData);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		resultData.put("executeTime", sdf.format(transferBank.getExecuteTime()));
		setResultData(resultData);
	}


	public void viewInMacau() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");

		setResultData(new HashMap(1));
		String transID = getParameter("transId");

		TransferMacau transferMacau = transferService.viewInMacau(transID);

		Map resultData = new HashMap();
		//add by linrui 20190926
		resultData.putAll(this.getParameters());
		//end
		this.convertPojo2Map(transferMacau, resultData);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		resultData.put("executeTime", sdf.format(transferMacau.getExecuteTime()));
		setResultData(resultData);
	}

	public void viewInOversea() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");

		setResultData(new HashMap(1));
		String transID = getParameter("transId");

		TransferOversea transferOversea = transferService
				.viewInOversea(transID);

		Map resultData = new HashMap();
		//add by linrui 20190926
		resultData.putAll(this.getParameters());
		//end
		this.convertPojo2Map(transferOversea, resultData);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		resultData.put("executeTime", sdf.format(transferOversea.getExecuteTime()));
		setResultData(resultData);
	}

	public void saveTemplate() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		String transId = Utils.null2EmptyWithTrim(this.getParameter("transId"));
		String tmp = Utils.null2EmptyWithTrim(this.getParameter("tmp"));
		if ("BAN".equalsIgnoreCase(tmp)) {
			TransferBank transferBank = transferService.viewInBANK(transId);
			// add by mxl 0913
			transferBank.setTransId(CibIdGenerator.getRefNoForTransaction());
			transferBank.setRecordType(TransferBank.TRANSFER_TYPE_TEMPLATE);

			transferService.templateInBANK1to1(transferBank);
		} else if ("MAC".equalsIgnoreCase(tmp)) {
			TransferMacau transferMacau = transferService.viewInMacau(transId);
			// add by mxl 0913
			transferMacau.setTransId(CibIdGenerator.getRefNoForTransaction());
			transferMacau.setRecordType(TransferMacau.TRANSFER_TYPE_TEMPLATE);

			transferService.templateInMacau(transferMacau);
		} else {
			TransferOversea transferOversea = transferService
					.viewInOversea(transId);
			// add by mxl 0913
			transferOversea.setTransId(CibIdGenerator.getRefNoForTransaction());
			transferOversea
					.setRecordType(TransferOversea.TRANSFER_TYPE_TEMPLATE);

			transferService.templateInAccOverseas(transferOversea);
		}

	}
}
