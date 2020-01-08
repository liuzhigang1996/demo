package com.jsax.service.txn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibIdGenerator;
import app.cib.service.enq.AccountEnqureService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.txn.TransferService;
import app.cib.util.RcCurrency;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.jsax.core.SubElement;
import com.jsax.utils.TxnInOverseaUtils;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.utils.*;
import com.neturbo.set.exception.NTBErrorArray;
import com.neturbo.set.exception.NTBHostException;

/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 *
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class AccInTxnService extends JsaxAction implements JsaxService {

	private CorpAccountService corpAccountService;

	private AccountEnqureService accEnqureService;

	public AccountEnqureService getAccEnqureService() {
		return accEnqureService;
	}

	public void setAccEnqureService(AccountEnqureService accEnqureService) {
		this.accEnqureService = accEnqureService;
	}

	public AccInTxnService() {
	}

	public CorpAccountService getCorpAccountService() {
		return corpAccountService;
	}

	public void setCorpAccountService(CorpAccountService corpAccountService) {
		this.corpAccountService = corpAccountService;
	}

	/*public void doTransaction() throws Exception {

		this.setTargetType(TARGET_TYPE_ELEMENT);

		RcCurrency rcCurrency = new RcCurrency();
		String fromAccount = getParameter("showFromAcc");
		String from_ccy = getParameter("from_ccy");
		String toAccount = getParameter("showToAcc");
		String to_ccy = Utils.null2EmptyWithTrim(getParameter("to_ccy"));
		String toAccountType = getParameter("accType");
        //add by linrui for mul-language20171116
		String lang = getParameter("language");
		CorpUser user = (CorpUser) this.getUser();
		if(!(null==lang||"".equals(lang))){
		Locale language = new Locale(lang.substring(0,2),lang.substring(3,5));
		user.setLanguage(language);
		}
		//end
		if (fromAccount != null && !fromAccount.equals("")) {
			CorpAccount corpAccount = corpAccountService
					.viewCorpAccount(fromAccount,from_ccy);
			String balance = "0.00";
			Map accountDetail = null;  
			try {
				if (corpAccount.getAccountType().equals(
						CorpAccount.ACCOUNT_TYPE_CURRENT)) {
					accountDetail = accEnqureService.viewCurrentDetail(
							user, fromAccount,from_ccy);//mod by linrui for mul-language to host 0195 20171116
					balance = Format.formatAmount(accountDetail.get("AVAILABLE_BALANCE").toString(),null,lang);//mod by linrui for mul-language 20171116
				} else if (corpAccount.getAccountType().equals(
						CorpAccount.ACCOUNT_TYPE_OVER_DRAFT)) {
					accountDetail = accEnqureService.viewOverdraftDetial(
							user, fromAccount,from_ccy);//mod by linrui for mul-language to host 0195 20171116
					balance = Format.formatAmount(accountDetail.get("AVAILABLE_BALANCE").toString(),null,lang);//mod by linrui for mul-language 20171116
				} else if (corpAccount.getAccountType().equals( // add by mxl 1227
						CorpAccount.ACCOUNT_TYPE_SAVING)) {
					accountDetail = accEnqureService.viewSavingDetail(user, fromAccount,from_ccy);//mod by linrui for mul-language to host 0295 20171116
					balance = Format.formatAmount(accountDetail.get("AVAILABLE_BALANCE").toString(),null,lang);//mod by linrui for mul-language 20171116
				}
			} catch (NTBHostException e) {
				NTBErrorArray errArray = e.getErrorArray();
				for (int i = 0; i > errArray.size(); i++) {
					Log.debug(errArray.getError(i));
				}
				throw e;

			}
			// ���뵽sub response�б�
			this.addSubResponseListByDefaultType("showAmount", balance);
			this.addSubResponseListByDefaultType("showFromCurrency", accountDetail.get("CCY_CODE_OF_AC").toString());//corpAccount.getCurrency());mod by linrui for mul-ccy
		}
		if (toAccount != null && !toAccount.equals("")) {
			if (toAccountType != null && !toAccountType.equals("other")) {
				CorpAccount corpAccount = corpAccountService.viewCorpAccount(toAccount);
				// ���뵽sub response�б�
				this.addSubResponseListByDefaultType("showToCurrency", corpAccount.getCurrency());
			} else {
				ApplicationContext appContext = Config.getAppContext();
				TransferService transferService = (TransferService) appContext.getBean("TransferService");
				String refNo = CibIdGenerator.getRefNoForTransaction();
				Map fromHost = transferService.toHostAccountInBANK(refNo, user, toAccount);//mod by linrui for mul-language to host zj35 20171116
				String currencyCode = (String) fromHost.get("CCY_CODE_OF_AC");//CURRENCY_CODEmod by linrui 20180227
				String toCurrency = rcCurrency.getCcyByCbsNumber(currencyCode);
				// ���뵽sub response�б�
				this.addSubResponseListByDefaultType("showToCurrency", toCurrency);
			}
			//mod by linrui for mul-ccy
			ApplicationContext appContext = Config.getAppContext();
			TransferService transferService = (TransferService) appContext.getBean("TransferService");
			String refNo = CibIdGenerator.getRefNoForTransaction();
			Map fromHost = transferService.toHostAccountInBANK(refNo, user, toAccount, to_ccy);//mod by linrui for mul-language to host zj35 20171116
			String currencyCode = (String) fromHost.get("CCY_CODE_OF_AC");//CURRENCY_CODEmod by linrui 20180227
			String toCurrency = rcCurrency.getCcyByCbsNumber(currencyCode);
			this.addSubResponseListByDefaultType("showToCurrency", toCurrency);
		}
	}*/
	//mod by linrui for select acct show ccy or select acct or ccy show balance
	public void doTransaction() throws Exception {
		
		CorpUser user = (CorpUser) this.getUser();
		String lang = getParameter("language");/* mul-language */
		Locale language;
		if (lang == null || "".equals(lang)) {
			language = Config.getDefaultLocale();
		} else {
			language = new Locale(lang.substring(0, 2), lang.substring(3, 5));
		}
		user.setLanguage(language);//add by linrui 20190729 by mul-language
		this.setTargetType(getParameter("targetType"));// page send object
		this.setTargetId(getParameter("targetId"));// page send originSelect.id

		// String originValue = getParameter("originValue");
		String subListId = getParameter("subListId");

		ApplicationContext appContext = Config.getAppContext();
		String action = getParameter("action");// search ccy list or search

		List selectList = new ArrayList();// list include ccyList or balance
		if ("getFromCcy".equals(action)) {
			String showAccount = getParameter("showFromAcc");// from acct or to // acct
			Map accountDetail = null;// prepared to get CCY LIST
			try {
				accountDetail = accEnqureService.viewCurrentDetail(user, showAccount, ""/* don not send ccy */);
			} catch (NTBHostException e) {
				NTBErrorArray errArray = e.getErrorArray();
				for (int i = 0; i > errArray.size(); i++) {
					Log.debug(errArray.getError(i));
				}
				throw e;

			}
			ArrayList ccyBalList = (ArrayList) accountDetail.get("CCY_BAL_LIST");
			//add by lzg 20190701
			AccountEnqureService accountEnqureService = getAccEnqureService();
			List availableCurrencies = accountEnqureService.getAvailableCurrencies();
			ArrayList ccyBalListTemp = new ArrayList();
			for (int i = 0; i < ccyBalList.size(); i++){
				HashMap retMap = new HashMap();
				retMap = (HashMap)ccyBalList.get(i);
				for (int j = 0; j < availableCurrencies.size(); j++){
					HashMap availableCCYMap = new HashMap();
					availableCCYMap = (HashMap)availableCurrencies.get(j);
					if(!availableCCYMap.get("CCY_CODE").equals(retMap.get("CCY_CODE_OF_AC"))){
						continue;
					}
					ccyBalListTemp.add(ccyBalList.get(i));
					break;
				}
			}
			ccyBalList = ccyBalListTemp;
			//add by lzg end
			List ccyList = new ArrayList();
			List ccyLit = new ArrayList();//add by linrui for pwc check ccy 20190612
			int count = 0;
			HashMap ccyBal;
			for (int i = 0; i < ccyBalList.size(); i++) {
				HashMap ccyMap = new HashMap();
				ccyBal = (HashMap) ccyBalList.get(i);
				String ccy = (String) ccyBal.get("CCY_CODE_OF_AC");
				if (ccy != null) {
					if (!"".equals(ccy.trim())) {
						ccyMap.put("CCY_NAME", ccy + " - " +accEnqureService.getCcyName(ccy,Format.transferLang(lang)));
						ccyMap.put("CCY_CODE", ccy);
						ccyList.add(ccyMap);
						ccyLit.add(ccy);//add by linrui for pwc check ccy 20190612
					}
				}
			}

			HashMap notice = new HashMap();
			RBFactory rb = RBFactory.getInstance("app.cib.resource.txn.transfer_bank", lang);
			notice.put("CCY_NAME", rb.getString("select_ccy"));
			notice.put("CCY_CODE", "");
			ccyList.add(0, notice);
		
			SubElement element = mapList2Selector(ccyList, subListId, "CCY_NAME", "CCY_CODE");
			selectList.add(element);
			this.addSubResponseListByDefaultType(selectList);
			//put ccy to map and check for pwc linrui 20190612
			this.setCurrencyResultData(showAccount,ccyLit);
			//
		}else if("getFromCcyRemittance".equals(action)){

			String showAccount = getParameter("showFromAcc");// from acct or to // acct
			Map accountDetail = null;// prepared to get CCY LIST
			try {
				accountDetail = accEnqureService.viewCurrentDetail(user, showAccount, ""/* don not send ccy */);
			} catch (NTBHostException e) {
				NTBErrorArray errArray = e.getErrorArray();
				for (int i = 0; i > errArray.size(); i++) {
					Log.debug(errArray.getError(i));
				}
				throw e;

			}
			ArrayList ccyBalList = (ArrayList) accountDetail.get("CCY_BAL_LIST");
			//add by lzg 20190701
			AccountEnqureService accountEnqureService = getAccEnqureService();
			List availableCurrencies = accountEnqureService.getAvailableCurrencies();
			/*int removeIndex = 0;
			for(int i =0; i < availableCurrencies.size(); i++){
				if(((HashMap)availableCurrencies.get(i)).get("CCY_CODE").equals("USD")){
					removeIndex = i;
				}
			}
			availableCurrencies.remove(removeIndex);*/
			ArrayList ccyBalListTemp = new ArrayList();
			for (int i = 0; i < ccyBalList.size(); i++){
				HashMap retMap = new HashMap();
				retMap = (HashMap)ccyBalList.get(i);
				for (int j = 0; j < availableCurrencies.size(); j++){
					HashMap availableCCYMap = new HashMap();
					availableCCYMap = (HashMap)availableCurrencies.get(j);
					if(!availableCCYMap.get("CCY_CODE").equals(retMap.get("CCY_CODE_OF_AC"))){
						continue;
					}
					ccyBalListTemp.add(ccyBalList.get(i));
					break;
				}
			}
			ccyBalList = ccyBalListTemp;
			//add by lzg end
			List ccyList = new ArrayList();
			List ccyLit = new ArrayList();//add by linrui for pwc check ccy 20190612
			int count = 0;
			HashMap ccyBal;
			for (int i = 0; i < ccyBalList.size(); i++) {
				HashMap ccyMap = new HashMap();
				ccyBal = (HashMap) ccyBalList.get(i);
				String ccy = (String) ccyBal.get("CCY_CODE_OF_AC");
				if (ccy != null) {
					if (!"".equals(ccy.trim())) {
						ccyMap.put("CCY_NAME", ccy + " - " +accEnqureService.getCcyName(ccy,Format.transferLang(lang)));
						ccyMap.put("CCY_CODE", ccy);
						ccyList.add(ccyMap);
						ccyLit.add(ccy);//add by linrui for pwc check ccy 20190612
					}
				}
			}

			HashMap notice = new HashMap();
			RBFactory rb = RBFactory.getInstance("app.cib.resource.txn.transfer_oversea", lang);
			notice.put("CCY_NAME", rb.getString("ToCurrency"));
			notice.put("CCY_CODE", "");
			ccyList.add(0, notice);
		
			SubElement element = mapList2Selector(ccyList, subListId, "CCY_NAME", "CCY_CODE");
			selectList.add(element);
			this.addSubResponseListByDefaultType(selectList);
			//put ccy to map and check for pwc linrui 20190612
			this.setCurrencyResultData(showAccount,ccyLit);
			//
		
		} else if ("getToCcy".equals(action)) {
			String showAccount = getParameter("showToAcc");// from acct or to
			Map accountDetail = null;// prepared to get CCY LIST
			try {
				accountDetail = accEnqureService.viewCurrentDetail(user, showAccount, ""/* don not send ccy */);
			} catch (NTBHostException e) {
				NTBErrorArray errArray = e.getErrorArray();
				for (int i = 0; i > errArray.size(); i++) {
					Log.debug(errArray.getError(i));
				}
				throw e;

			}
			ArrayList ccyBalList = (ArrayList) accountDetail.get("CCY_BAL_LIST");
			//add by lzg 20190701
			AccountEnqureService accountEnqureService = getAccEnqureService();
			List availableCurrencies = accountEnqureService.getAvailableCurrencies();
			ArrayList ccyBalListTemp = new ArrayList();
			for (int i = 0; i < ccyBalList.size(); i++){
				HashMap retMap = new HashMap();
				retMap = (HashMap)ccyBalList.get(i);
				for (int j = 0; j < availableCurrencies.size(); j++){
					HashMap availableCCYMap = new HashMap();
					availableCCYMap = (HashMap)availableCurrencies.get(j);
					if(!availableCCYMap.get("CCY_CODE").equals(retMap.get("CCY_CODE_OF_AC"))){
						continue;
					}
					ccyBalListTemp.add(ccyBalList.get(i));
					break;
				}
			}
			ccyBalList = ccyBalListTemp;
			//add by lzg end
			List ccyList = new ArrayList();
			List ccyLit = new ArrayList();//add by linrui for pwc check ccy 20190612
			int count = 0;
			HashMap ccyBal;
			for (int i = 0; i < ccyBalList.size(); i++) {
				HashMap ccyMap = new HashMap();
				ccyBal = (HashMap) ccyBalList.get(i);
				String ccy = (String) ccyBal.get("CCY_CODE_OF_AC");
				if (ccy != null) {
					if (!"".equals(ccy.trim())) {
						ccyMap.put("CCY_NAME", ccy + " - " +accEnqureService.getCcyName(ccy,Format.transferLang(lang)));
						ccyMap.put("CCY_CODE", ccy);
						ccyList.add(ccyMap);
						ccyLit.add(ccy);//add by linrui for pwc check ccy 20190612
					}
				}
			}

			HashMap notice = new HashMap();
			RBFactory rb = RBFactory.getInstance("app.cib.resource.txn.transfer_oversea", lang);
			notice.put("CCY_NAME", rb.getString("ToCurrency"));
			//notice.put("CCY_NAME", "----Please select Currency----");
			notice.put("CCY_CODE", "");
			ccyList.add(0, notice);
			/*
			 * HashMap noticeLast = new HashMap(); noticeLast.put("CCY_NAME",
			 * "Other Ccy"); noticeLast.put("CCY_CODE", "other");
			 * ccyList.add(noticeLast);
			 */

			SubElement element = mapList2Selector(ccyList, subListId,
					"CCY_NAME", "CCY_CODE");
			selectList.add(element);
			this.addSubResponseListByDefaultType(selectList);
			//put ccy to map and check for pwc linrui 20190612
			this.setCurrencyResultData(showAccount,ccyLit);
			//end

		} else if ("fromCcy".equals(action)) {/* check balance and put showfromdiv */
			this.setTargetType(TARGET_TYPE_ELEMENT);
			String showFromAcc = getParameter("showFromAcc");
			String fromCcy = getParameter("fromCcy");
			Map accountDetail = null;
			try {
				accountDetail = accEnqureService.viewCurrentDetail(user,
						showFromAcc, fromCcy);
			} catch (NTBHostException e) {
				NTBErrorArray errArray = e.getErrorArray();
				for (int i = 0; i > errArray.size(); i++) {
					Log.debug(errArray.getError(i));
				}
				throw e;

			}
			ArrayList ccyBalList = (ArrayList) accountDetail.get("CCY_BAL_LIST");
			String ccyBal = ((HashMap) ccyBalList.get(0)).get("AVAILABLE_BALANCE").toString();
			ccyBal = Format.formatAmount(ccyBal);
			this.addSubResponseListByDefaultType("showAmount", ccyBal);
			this.addSubResponseListByDefaultType("showFromCurrency", fromCcy);
			

		} else {/* put showToDiv */
			this.setTargetType(TARGET_TYPE_ELEMENT);
			String showToCcy = getParameter("showToCcy");
			this.addSubResponseListByDefaultType("showToCurrency", showToCcy);

		}

	}
	
}
