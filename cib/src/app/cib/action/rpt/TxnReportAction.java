/**
 * 
 */
package app.cib.action.rpt;
import java.lang.reflect.Field;
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
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

import app.cib.bo.bat.FileRequest;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.TimeDeposit;
import app.cib.bo.txn.TransferBank;
import app.cib.core.CibAction;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.txn.BillPaymentService;
import app.cib.service.txn.CorpTransferService;
import app.cib.service.txn.TimeDepositService;
import app.cib.service.txn.TransferService;
import app.cib.util.Constants;
import app.cib.util.TransferConstants;

/**
 * @author hjs
 *
 */
public class TxnReportAction extends CibAction{
	
	public void listTransferReportLoad() throws NTBException {
        //���ÿյ� ResultData �����ʾ���
        setResultData(new HashMap(1));
	}
	
	public void listTransferReport() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        TransferService transferService = (TransferService)appContext.getBean("TransferService");
        CorpUser corpUser = (CorpUser) this.getUser();
		List historyList = null;
		Date dateFrom = null;
		Date dateTo = null;
		String businessType = null;
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
		businessType = Utils.null2EmptyWithTrim(this.getParameter("businessType"));
		String uploadStatusFlag = "";
		String currency = "";
		String amount = "";
		Object transfer = null;
		List newList = new ArrayList();
        if(businessType.equals(TransferConstants.TRANSFER_BANK_1TOM)
        		|| businessType.equals(TransferConstants.TRANSFER_MACAU_1TOM)
        		|| businessType.equals(TransferConstants.TRANSFER_OVERSEA_1TOM)
        		|| businessType.equals(TransferConstants.TRANSFER_BANK_MTO1) ){
    		Locale locale = (this.getUser().getLanguage()==null) ? Config.getDefaultLocale() : this.getUser().getLanguage();
    		RBFactory rbFactory = RBFactory.getInstance("app.cib.resource.rpt.txn_report", locale.toString());
        	if(businessType.equals(TransferConstants.TRANSFER_BANK_MTO1)) {
        		historyList =  transferService.fileRequestHistoryMtS(recType2batType(businessType), dateFrom, dateTo, corpUser.getCorpId(), null, null);
        	} else {
        		historyList =  transferService.fileRequestHistory(recType2batType(businessType), dateFrom, dateTo, corpUser.getCorpId(), null, null);
        	}
        	uploadStatusFlag = "Y";
        	FileRequest fileRequest = null;
        	for(int i=0; i<historyList.size(); i++){
        		fileRequest = (FileRequest) historyList.get(i);
    			Map newRow = new HashMap();
    			this.convertPojo2Map(fileRequest, newRow);
    			newRow.put("inputAmountFlag", "1");
    			newRow.put("amount", fileRequest.getToAmount());
    			newRow.put("currency", fileRequest.getToCurrency());
    			newRow.put("transId", fileRequest.getBatchId());
    			if(businessType.equals(TransferConstants.TRANSFER_BANK_MTO1)){
        			newRow.put("fromAccount", rbFactory.getString("mul_acc"));
    			} else {
        			newRow.put("toAccount", rbFactory.getString("mul_acc"));
    			}
    			newList.add(newRow);
        	}
        } else {
        	uploadStatusFlag = "N";
    		historyList =  transferService.transferHistory(businessType, dateFrom, dateTo, corpUser.getCorpId(), null, null);
    		Field inputFlagfield = null;
    		Field fromCcyfield = null;
    		Field toCcyfield = null;
    		Field fromAmtfield = null;
    		Field toAmtfield = null;
    		for(int i=0; i<historyList.size(); i++){
    			try{
        			transfer = historyList.get(i);
        			Map newRow = new HashMap();
        			this.convertPojo2Map(transfer, newRow);
        			inputFlagfield = transfer.getClass().getSuperclass().getDeclaredField("inputAmountFlag");
        			fromCcyfield = transfer.getClass().getSuperclass().getDeclaredField("fromCurrency");
        			toCcyfield = transfer.getClass().getSuperclass().getDeclaredField("toCurrency");
        			fromAmtfield = transfer.getClass().getSuperclass().getDeclaredField("debitAmount");
        			toAmtfield = transfer.getClass().getSuperclass().getDeclaredField("transferAmount");
        			inputFlagfield.setAccessible(true);
        			fromCcyfield.setAccessible(true);
        			toCcyfield.setAccessible(true);
        			fromAmtfield.setAccessible(true);
        			toAmtfield.setAccessible(true);
        			// �ڱ�������ʾ����Ļ��Һ�ת�ʽ��
        			if (inputFlagfield.get(transfer).equals(
        					Constants.INPUT_AMOUNT_FLAG_FROM)) {
        				currency = fromCcyfield.get(transfer).toString();
        				amount = fromAmtfield.get(transfer).toString();
        			} else if (inputFlagfield.get(transfer).equals(
        					Constants.INPUT_AMOUNT_FLAG_TO)) {
        				currency = toCcyfield.get(transfer).toString();
        				amount = toAmtfield.get(transfer).toString();
        			}
        			newRow.put("currency", currency);
        			newRow.put("amount", amount);
        			newList.add(newRow);
    			} catch (Exception e) {
    				Log.error("get fiele error", e);
    				throw new NTBException(e.getMessage());
    			}
    		}
        }
        Map resultData = new HashMap();
        resultData.put("businessType", businessType);
        resultData.put("historyList", newList);
		resultData.put("uploadStatusFlag", uploadStatusFlag);
        setResultData(resultData);
	}
	
	public void listBillPaymentReportLoad() throws NTBException {
        //���ÿյ� ResultData �����ʾ���
        setResultData(new HashMap(1));
	}
	
	public void listBillPaymentReport() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        BillPaymentService billPaymentService = (BillPaymentService)appContext.getBean("BillPaymentService");
        CorpUser corpUser = (CorpUser) this.getUser();
        //corpUser.setUserId("");
        String dateFrom = Utils.null2EmptyWithTrim(this.getParameter("dateFrom"));
        String dateTo = Utils.null2EmptyWithTrim(this.getParameter("dateTo"));
        String merchant = Utils.null2EmptyWithTrim(this.getParameter("merchant"));
        String fromAccount = Utils.null2EmptyWithTrim(this.getParameter("fromAccount"));
		List historyList =  billPaymentService.listHistory(
				corpUser.getCorpId(), "", dateFrom, dateTo, merchant, fromAccount);
		historyList = this.convertPojoList2MapList(historyList);
        Map resultData = new HashMap();
        resultData.put("historyList", historyList);
        setResultData(resultData);
	}
	
	public void listTimeDepositReportLoad() throws NTBException {
        //���ÿյ� ResultData �����ʾ���
        setResultData(new HashMap(1));		
	}
	
	public void listTimeDepositReport() throws NTBException {
        ApplicationContext appContext = Config.getAppContext();
        TimeDepositService timeDepositService = (TimeDepositService)appContext.getBean("TimeDepositService");
		CorpUser corpUser = (CorpUser) this.getUser();
        //String dateRange = Utils.null2EmptyWithTrim(this.getParameter("date_range"));
        String dateFrom = Utils.null2EmptyWithTrim(this.getParameter("dateFrom"));
        String dateTo = Utils.null2EmptyWithTrim(this.getParameter("dateTo"));
		List historyList = timeDepositService.lsitHistory(corpUser.getCorpId(), "", dateFrom, dateTo);
		historyList = this.convertPojoList2MapList(historyList);
		Map tmpMap = null;
		double tmpInterest = 0;
		for (int i=0; i<historyList.size(); i++) {
			tmpMap = (Map) historyList.get(i);
			if (tmpMap.get("tdType").toString().equals(TimeDeposit.TD_TYPE_WITHDRAW)) {
				tmpInterest = new Double(tmpMap.get("interest").toString()).doubleValue() 
							- new Double(tmpMap.get("penalty").toString()).doubleValue();
				tmpInterest = tmpInterest >= 0 ? tmpInterest : 0;
				tmpMap.put("principal", 
						String.valueOf(
								new Double(tmpMap.get("principal").toString()).doubleValue() + tmpInterest
						)
				);
			}
		}
		Map resultData = new HashMap();
		resultData.put("dateFrom", dateFrom);
		resultData.put("dateTo", dateTo);
		resultData.put("historyList", historyList);
		this.setResultData(resultData);
	}
	
	public void viewNewHistory() throws NTBException {
		// ���ÿյ� ResultData �����ʾ���
		setResultData(new HashMap(1));
		this.clearUsrSessionDataValue();
		ApplicationContext appContext = Config.getAppContext();
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext.getBean("CorpAccountService");
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext.getBean("ExchangeRatesService");
		CorpUser corpUser = (CorpUser) this.getUser();
		String transId = Utils.null2EmptyWithTrim(this.getParameter("transId"));
		TimeDeposit timeDeposit = timeDepositService.viewTimeDeposit(transId);
		// ͨ���ʺ�ȡ�û�������(fromCcy)
		String currentAccountCcy = corpAccountService.getCurrency(timeDeposit.getCurrentAccount(),true);
		// ͨ��ҳ������Ķ����˺Ž������ӵ�ǰ�ʺ�ת���Ľ��
		String currentAccPrincipal = exRatesService.getEquivalent(
				corpUser.getCorpId(),
				currentAccountCcy,
				timeDeposit.getCurrency(),
				null,
				new BigDecimal(timeDeposit.getPrincipal().toString()),
				2).toString();
		Map resultData = new HashMap();
		resultData.put("currentAccountCcy", currentAccountCcy);
		resultData.put("currentAccPrincipal", currentAccPrincipal);
		resultData.put("returnUrl", "/cib/txnReport.do?ActionMethod=listTimeDepositReportLoad");
		this.convertPojo2Map(timeDeposit, resultData);
		this.setResultData(resultData);
	}
	
	public void viewWithdrawalHistory() throws NTBException {
		// ���ÿյ� ResultData �����ʾ���
		setResultData(new HashMap(1));
		this.clearUsrSessionDataValue();
		ApplicationContext appContext = Config.getAppContext();
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");
		String transId = Utils.null2EmptyWithTrim(this.getParameter("transId"));
		TimeDeposit timeDeposit = timeDepositService.viewTimeDeposit(transId);
		double interest = timeDeposit.getInterest().doubleValue();
		double penalty = timeDeposit.getPenalty().doubleValue();
		double principal = timeDeposit.getPrincipal().doubleValue();
		double netInterestAmt = (interest - penalty > 0) ? interest - penalty : 0;
		double netCreditAmount = principal + netInterestAmt;
		Map resultData = new HashMap();
		resultData.put("netInterestAmt", String.valueOf(netInterestAmt));
		resultData.put("netCreditAmount", String.valueOf(netCreditAmount));
		resultData.put("returnUrl", "/cib/txnReport.do?ActionMethod=listTimeDepositReportLoad");
		this.convertPojo2Map(timeDeposit, resultData);
		this.setResultData(resultData);
	}
	
	private String recType2batType(String recType){
		if (recType.equals(TransferConstants.TRANSFER_BANK_1TOM)){
			return FileRequest.TRANSFER_BANK_STM;
		} else if (recType.equals(TransferConstants.TRANSFER_MACAU_1TOM)) {
			return FileRequest.TRANSFER_MACAU_STM;
		} else if (recType.equals(TransferConstants.TRANSFER_OVERSEA_1TOM)) {
			return FileRequest.TRANSFER_OVERSEA_STM;
		} else if (recType.equals(TransferConstants.TRANSFER_BANK_MTO1)) {
			return FileRequest.TRANSFER_BANK_MTS;
		}
		return null;
	}
	
	public void listFundAllocationReportLoad() throws NTBException {
        //���ÿյ� ResultData �����ʾ���
        setResultData(new HashMap(1));		
	}
	
	public void listFundAllocationReport() throws NTBException {
	    ApplicationContext appContext = Config.getAppContext();
		CorpTransferService corpTransferService = (CorpTransferService)appContext.getBean("CorpTransferService");

		Date dateFrom = null;
		Date dateTo = null;
		
		String fromCorpId = Utils.null2EmptyWithTrim(this.getParameter("fromCorporation"));
		String fromAccount= Utils.null2EmptyWithTrim(this.getParameter("fromAccount"));
		String date_range = Utils.null2EmptyWithTrim(getParameter("date_range"));
		
		if (date_range.equals("range")) {
			if (!Utils.null2EmptyWithTrim(getParameter("dateFrom")).equals("")) {
				dateFrom = DateTime.getDateFromStr( Utils.null2EmptyWithTrim(getParameter("dateFrom")), true);
			}
			if (!Utils.null2EmptyWithTrim(getParameter("dateTo")).equals("")) {
				dateTo = DateTime.getDateFromStr( Utils.null2EmptyWithTrim(getParameter("dateTo")), false);
			}
		}
		
	    CorpUser userObj = (CorpUser)this.getUser();
		String corpId = userObj.getCorpId();
		List transferList = corpTransferService.transferHistory(
				dateFrom, dateTo, fromCorpId, null, fromAccount, corpId);
		List reportList = new ArrayList();
		for (int i = 0; i<transferList.size(); i++){
		    Map tranBankData = new HashMap();
			String currency = null;
			String transferAmount = null;
			TransferBank transferBank = (TransferBank) transferList.get(i);
             //���б�����ʾ����Ļ��Һ�ת�ʽ��
			if ( transferBank.getInputAmountFlag().equals(Constants.INPUT_AMOUNT_FLAG_FROM)) {
				currency = transferBank.getFromCurrency();
				transferAmount = transferBank.getDebitAmount().toString();					
			} else if ( transferBank.getInputAmountFlag().equals(Constants.INPUT_AMOUNT_FLAG_TO)) {
				currency = transferBank.getToCurrency();
				transferAmount = transferBank.getTransferAmount().toString();
			}
			convertPojo2Map(transferBank, tranBankData);
			tranBankData.put("currency", currency);
			tranBankData.put("transferAmount", transferAmount);
			reportList.add(tranBankData);
		}
		
		//transferList = this.convertPojoList2MapList(transferList);
		Map resultData = new HashMap();
		resultData.put("dateFrom", dateFrom);
	    resultData.put("dateTo", dateTo);
	    resultData.put("reportList", reportList);
	    resultData.put("fromAccount", fromAccount);
	    resultData.put("fromCorporation", fromCorpId);
	    setResultData(resultData);
	}
}
