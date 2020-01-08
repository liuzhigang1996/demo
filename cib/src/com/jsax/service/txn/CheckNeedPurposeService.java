package com.jsax.service.txn;

import org.springframework.context.ApplicationContext;

import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.service.enq.ExchangeRatesService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.txn.TransferService;
import app.cib.util.RcCurrency;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.neturbo.set.core.Config;
import com.neturbo.set.utils.Utils;

/**
 * 
 * @author heyongjiang
 * @since 20100805
 */
public class CheckNeedPurposeService extends JsaxAction implements JsaxService {

	public void doTransaction() throws Exception {
		// TODO Auto-generated method stub
		this.setTargetType(TARGET_TYPE_ELEMENT);
		ApplicationContext appContext = Config.getAppContext();
		ExchangeRatesService exRatesService = (ExchangeRatesService) appContext
				.getBean("ExchangeRatesService");
		CorpAccountService corpAccountService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		TransferService transferService = (TransferService) appContext
				.getBean("TransferService");
		CorpUser corpUser = (CorpUser) this.getUser();
		RcCurrency rcCurrency = new RcCurrency();

		String transferAmount = Utils
				.null2Empty(getParameter("transferAmount"));
		String debitAmount = Utils.null2Empty(getParameter("debitAmount"));
		String fromAccount = Utils.null2Empty(getParameter("fromAccount"));
		String toCurrency = Utils.null2Empty(getParameter("toCurrency"));
		String toCountryCode = Utils.null2Empty(getParameter("toCountryCode"));
		//add by linrui 20190729
		String language = Utils.null2Empty(getParameter("language"));
		if (!(transferAmount.equals("") && debitAmount.equals(""))
				//add by linrui for badcode
				&& validAmount(transferAmount,debitAmount)
				&& !fromAccount.equals("") && !toCurrency.equals("")
				&& !toCountryCode.equals("")) {
			String corpId = corpUser.getCorpId();
			CorpAccount corpAccount = corpAccountService
					.viewCorpAccount(fromAccount);
			String fromCurrency = corpAccount.getCurrency();
			if (transferAmount.indexOf(",") > 0) {
				transferAmount = transferAmount.replaceAll(",", "");
			}
			if (debitAmount.indexOf(",") > 0) {
				debitAmount = debitAmount.replaceAll(",", "");
			}
			// String toCurrencyCode = rcCurrency.getCbsNumberByCcy(toCurrency);
			int checkResult = transferService.checkNeedPurpose(corpId,
					fromAccount, transferAmount, debitAmount, fromCurrency,
					toCurrency, toCountryCode);
			if (checkResult != 0) {
				this.addSubResponseListByDefaultType("purposeVisible","visible");
				if (checkResult == 2) {
					this.addSubResponseListByDefaultType("proofVisible","needproof");
				} else {
					this.addSubResponseListByDefaultType("proofVisible","notneedproof");
				}
			} else {
				this.addSubResponseListByDefaultType("purposeVisible","notvisible");
				this.addSubResponseListByDefaultType("proofVisible","notneedproof");
			}
		} else {
			this.addSubResponseListByDefaultType("purposeVisible","notvisible");
			this.addSubResponseListByDefaultType("proofVisible","notneedproof");
		}
	}
	
	//add by linrui for judge amount type
	public static boolean validAmount(String amount1, String amount2){
		String amount = !"".equals(amount1)?amount1:amount2;
		try{
		Double.parseDouble(amount);
		}catch(Exception e){
			return false;
		}
		return true;
//		return Pattern.compile("[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*").matcher(amount).find();
	}
	
	public static void main(String args[]){
		System.out.println(validAmount("","."));
	}
}
