package app.cib.action.enq;

/**
 * 
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.core.CibTransClient;
import app.cib.service.enq.AccountEnqureService;
import app.cib.service.sys.CorpUserService;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;

public class AccEnquiryTestAction extends CibAction {
	
	//no login for test
    protected void processNotLogin() throws NTBException {
    }

	public void listAccountSummary() throws NTBException {
		Log.info("[AccEnquiryTest]Start test");
		ApplicationContext appContext = Config.getAppContext();
		CorpUserService userService = (CorpUserService)appContext.getBean("corpUserService");

		//modify the user id below before test
		String userId = "corpopr1";
		Log.info("load a user: " + userId);
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser user = userService.load(userId);
		this.setUser(user);
		user.setLanguage(lang);//add by linrui for mul-language
		String corpId = user.getCorpId();
		String eqCcy = this.getParameter("eqCcy");

		// ��������
		Map resultData = new HashMap();
		setResultData(resultData);
		resultData.put("showNothing", "1");

		AccountEnqureService accountEnqureService = (AccountEnqureService) Config
				.getAppContext().getBean("AccountEnqureService");

		List currentAccountList = null;
		List timeDepositList = null;
		List overdraftAccountList = null;
		List loanAccountList = null;
		List savingAccountList = null;

		// ��� eqCcy = null, ���ʾ�ǵ�һ�ν��룬�����ʾ�û������ֵ���ֽ���
		if (eqCcy == null) {
			// ��һ�ν��룬�������ѯ��д��session
			// APPCODE_CURRENT_ACCOUNT
			currentAccountList = accountEnqureService.listSummaryByAccType(
					user, corpId, CorpAccount.ACCOUNT_TYPE_CURRENT, CibTransClient.APPCODE_CURRENT_ACCOUNT);
			setUsrSessionDataValue("currentAccountList", currentAccountList);
			// APPCODE_TIME_DEPOSIT
			timeDepositList = accountEnqureService.listSummaryByAccType(user,
					corpId, CorpAccount.ACCOUNT_TYPE_TIME_DEPOSIT, CibTransClient.APPCODE_TIME_DEPOSIT);
			setUsrSessionDataValue("timeDepositList", timeDepositList);
			// APPCODE_OVERDRAFT_ACCOUNT
			overdraftAccountList = accountEnqureService.listSummaryByAccType(
					user, corpId, CorpAccount.ACCOUNT_TYPE_OVER_DRAFT, CibTransClient.APPCODE_OVERDRAFT_ACCOUNT);
			setUsrSessionDataValue("overdraftAccountList", overdraftAccountList);
			// APPCODE_LOAN_ACCOUNT
			loanAccountList = accountEnqureService.listSummaryByAccType(user,
					corpId, CorpAccount.ACCOUNT_TYPE_LOAN, CibTransClient.APPCODE_LOAN_ACCOUNT);
			setUsrSessionDataValue("loanAccountList", loanAccountList);
            // APPCODE_SAVING_ACCOUNT add by mxl 1120
			savingAccountList = accountEnqureService.listSummaryByAccType(
					user, corpId, CorpAccount.ACCOUNT_TYPE_SAVING, CibTransClient.APPCODE_SAVING_ACCOUNT);
			setUsrSessionDataValue("savingAccountList", savingAccountList);
		} else {
			// �û������ֵ���ֽ���, ��session�ж������
			currentAccountList = (List) this
					.getUsrSessionDataValue("currentAccountList");
			timeDepositList = (List) this
					.getUsrSessionDataValue("timeDepositList");
			overdraftAccountList = (List) this
					.getUsrSessionDataValue("overdraftAccountList");
			loanAccountList = (List) this
					.getUsrSessionDataValue("loanAccountList");
			// add by mxl 1120
			savingAccountList = (List) this
			.getUsrSessionDataValue("savingAccountList");
		}

		List csvList = new ArrayList();
		// ����
		/*
		currentAccountList = changeEquivalent(currentAccountList,
				"TOTAL_BALANCE", "CURRENCY_CODE", eqCcy, "TOTAL_LCY_BALANCE");
				*/
		resultData.put("currentAccountList", currentAccountList);
		if (currentAccountList.size() > 0) {
			resultData.put("showCurrentAccount", "1");
			resultData.put("showNothing", "0");
			String subTotal = String.valueOf(caculateTotal(currentAccountList,
					"TOTAL_LCY_BALANCE"));
			resultData.put("CA_SubTotal", subTotal);

			Map totalMap = new HashMap();
			totalMap.put("SUBTOTAL_TITLE", Utils.subTotalString(lang.toString()));//mod by linrui 20180522 for mul-lang download
			totalMap.put("SUBTOTAL", subTotal);

            Map firstMap = (Map)currentAccountList.get(0);
            firstMap.put("showAccType", "CURRENT ACCOUNT");
			csvList.addAll(currentAccountList);
			csvList.add(totalMap);
		}
        

		// ����
		/*
		timeDepositList = changeEquivalent(timeDepositList,
				"TOTAL_BALANCE", "CURRENCY_CODE", eqCcy, "TOTAL_LCY_BALANCE");
				*/
		resultData.put("timeDepositList", timeDepositList);
		if (timeDepositList.size() > 0) {
			resultData.put("showTimeDeposit", "1");
			resultData.put("showNothing", "0");
			String subTotal = String.valueOf(caculateTotal(timeDepositList,
					"TOTAL_LCY_BALANCE"));
			resultData.put("TD_SubTotal", subTotal);

			Map totalMap = new HashMap();
			totalMap.put("SUBTOTAL_TITLE", Utils.subTotalString(lang.toString()));//mod by linrui 20180522 for mul-lang download
			totalMap.put("SUBTOTAL", subTotal);
                        Map firstMap = (Map)timeDepositList.get(0);
                        firstMap.put("showAccType", "TIME DEPOSIT");
			csvList.addAll(timeDepositList);
			csvList.add(totalMap);
		}

		// ͸֧
		/*
		overdraftAccountList = changeEquivalent(overdraftAccountList,
				"TOTAL_BALANCE", "CURRENCY_CODE", eqCcy, "TOTAL_LCY_BALANCE");
				*/
		resultData.put("overdraftAccountList", overdraftAccountList);
		if (overdraftAccountList.size() > 0) {
			resultData.put("showOverdraft", "1");
			resultData.put("showNothing", "0");
			String subTotal = String.valueOf(caculateTotal(
					overdraftAccountList, "TOTAL_LCY_BALANCE"));
			resultData.put("OA_SubTotal", subTotal);

			Map totalMap = new HashMap();
			totalMap.put("SUBTOTAL_TITLE", Utils.subTotalString(lang.toString()));//mod by linrui 20180522 for mul-lang download
			totalMap.put("SUBTOTAL", subTotal);
                        Map firstMap = (Map)overdraftAccountList.get(0);
                        firstMap.put("showAccType", "OVERDRAFT ACCOUNT");
			csvList.addAll(overdraftAccountList);
			csvList.add(totalMap);
		}

		// ���
		/*
		loanAccountList = changeEquivalent(loanAccountList,
				"TOTAL_BALANCE", "CURRENCY_CODE", eqCcy, "TOTAL_LCY_BALANCE");
				*/
		resultData.put("loanAccountList", loanAccountList);
		if (loanAccountList.size() > 0) {
			resultData.put("showLoanAccount", "1");
			resultData.put("showNothing", "0");
			String subTotal = String.valueOf(caculateTotal(loanAccountList,
					"TOTAL_LCY_BALANCE"));
			resultData.put("LA_SubTotal", subTotal);

			Map totalMap = new HashMap();
			totalMap.put("SUBTOTAL_TITLE", Utils.subTotalString(lang.toString()));//mod by linrui 20180522 for mul-lang download
			totalMap.put("SUBTOTAL", subTotal);
                        Map firstMap = (Map)loanAccountList.get(0);
                        firstMap.put("showAccType", "LOAN ACCOUNT");
			csvList.addAll(loanAccountList);
			csvList.add(totalMap);
		}
        //Saving Account 1120
		/*
		savingAccountList = changeEquivalent(savingAccountList,
				"TOTAL_BALANCE", "CURRENCY_CODE", eqCcy, "TOTAL_LCY_BALANCE");
				*/
		resultData.put("savingAccountList", savingAccountList);
		if (savingAccountList.size() > 0) {
			resultData.put("showSavingAccount", "1");
			resultData.put("showNothing", "0");
			String subTotal = String.valueOf(caculateTotal(savingAccountList,
					"TOTAL_LCY_BALANCE"));
			resultData.put("SA_SubTotal", subTotal);

			Map totalMap = new HashMap();
			totalMap.put("SUBTOTAL_TITLE", Utils.subTotalString(lang.toString()));//mod by linrui 20180522 for mul-lang download
			totalMap.put("SUBTOTAL", subTotal);

            Map firstMap = (Map)savingAccountList.get(0);
            firstMap.put("showAccType", "SAVING ACCOUNT");
			csvList.addAll(savingAccountList);
			csvList.add(totalMap);
		}

		// ��ʾ�ȶ����
		resultData.put("eqCcy", eqCcy);
		// д�������б�
		resultData.put("csvList", csvList);
		Log.info("[AccEnquiryTest]End test");

	}

	public double caculateTotal(List calList, String fieldName) {
		double total = 0.0;
		for (int i = 0; i < calList.size(); i++) {
			Map recordMap = (Map) calList.get(i);
			double add1 = Utils.parseDouble(
					Utils.null2Empty(recordMap.get(fieldName)));
			total += add1;
		}
		return total;
	}
}
