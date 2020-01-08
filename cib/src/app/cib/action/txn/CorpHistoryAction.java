package app.cib.action.txn;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Utils;

import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TransferBank;
import app.cib.core.CibAction;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.txn.CorpTransferService;
import app.cib.util.Constants;

public class CorpHistoryAction extends CibAction {

	public void listLoad() throws NTBException {
        setResultData(new HashMap(1));
    }
	
	public void listHistory()throws  NTBException{
	    setResultData(new HashMap(1));
	    ApplicationContext appContext = Config.getAppContext();
		CorpTransferService corpTransferService = (CorpTransferService)appContext.getBean("CorpTransferService");
		FlowEngineService flowEngineService = (FlowEngineService) appContext.getBean("FlowEngineService");
		
		Date dateFrom = null;
		Date dateTo = null;
		String userId = null;
		String fromCorpId = null;
		String fromAccount = null;
		CorpUser user = (CorpUser) this.getUser();
		String approverFlag = user.getCorporation().getAllowApproverSelection();
		fromCorpId = Utils.null2EmptyWithTrim(this.getParameter("fromCorporation"));
		
        //add by mxl 0824
		String date_range = Utils
				.null2EmptyWithTrim(getParameter("date_range"));
		if (date_range.equals("range")) {
			if (!Utils.null2EmptyWithTrim(getParameter("dateFrom")).equals("")) {
				dateFrom = DateTime.getDateFromStr( Utils.null2EmptyWithTrim(getParameter("dateFrom")), true);
			}
			if (!Utils.null2EmptyWithTrim(getParameter("dateTo")).equals("")) {
				dateTo = DateTime.getDateFromStr( Utils.null2EmptyWithTrim(getParameter("dateTo")), false);
			}
		}
		
	    fromAccount= Utils.null2EmptyWithTrim(this.getParameter("fromAccount"));
	    userId = user.getUserId();
		String corpId = user.getCorpId();
		String changeFlag = null;
		List transferList = corpTransferService.transferHistory(dateFrom,dateTo,fromCorpId,userId,fromAccount,corpId);
		List toList = new ArrayList();
		for (int i = 0; i<transferList.size(); i++){
		    Map tranBankData = null;
			String currency = null;
			String transferAmount = null;
			String txnType = null; 
			TransferBank transferBank = (TransferBank) transferList.get(i);
			if ( transferBank.getRecordType().equals(TransferBank.TRANSFER_TYPE_CORPDELIVERY) ) {
				txnType = Constants.TXN_SUBTYPE_TRANSFER_DELIVERY;
			} else if (transferBank.getRecordType().equals(TransferBank.TRANSFER_TYPE_COPRREPATRIATE)) {
				txnType = Constants.TXN_SUBTYPE_TRANSFER_REPETRIATE;
			} else if (transferBank.getRecordType().equals(TransferBank.TRNASFER_TYPE_CORPSUBIDIARY)) {
				txnType = Constants.TXN_SUBTYPE_TRANSFER_BETWEEN_SUBSIDIARY;
			}
			
			if ("Y".equals(approverFlag)) {
				if ("1".equals(transferBank.getStatus())) {
					if (flowEngineService.checkApproveComplete(
							txnType, transferBank
									.getTransId())) {
						changeFlag = "N";
					} else {
						changeFlag = "Y";
					}
				}
			}
			tranBankData = new HashMap();
			
			if ( transferBank.getInputAmountFlag().equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferBank.getFromCurrency();
				transferAmount = transferBank.getDebitAmount().toString();					
			} else if ( transferBank.getInputAmountFlag().equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferBank.getToCurrency();
				transferAmount = transferBank.getTransferAmount().toString();
			}
			convertPojo2Map(transferBank, tranBankData);
			tranBankData.put("changeFlag", changeFlag);
			tranBankData.put("currency", currency);
			tranBankData.put("transferAmount", transferAmount);
			tranBankData.put("txnType", txnType);
			toList.add(tranBankData);
			
		}
		
		//transferList = this.convertPojoList2MapList(transferList);
		Map resultData = new HashMap();
		resultData.put("dateFrom", dateFrom);
	    resultData.put("dateTo", dateTo);
	    resultData.put("transferList", toList);
	    resultData.put("fromAccount", fromAccount);
	    resultData.put("fromCorporation", fromCorpId);
	    resultData.put("approverFlag", approverFlag);
	    setResultData(resultData);
	}

}
