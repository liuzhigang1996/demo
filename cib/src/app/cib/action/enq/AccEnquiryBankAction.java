package app.cib.action.enq;

/**
 * @author jet
 *
 */
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

import app.cib.bo.bnk.BankUser;
import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibAction;
import app.cib.core.CibTransClient;
import app.cib.service.enq.AccountEnqureBankService;
import app.cib.service.enq.AccountEnqureService;
import app.cib.service.sys.CorpAccountService;
import app.cib.service.txn.TimeDepositService;
import app.cib.util.CachedDBRCFactory;
import app.cib.util.RcCurrency;
import app.cib.util.RcTransactionDescription;
import app.cib.util.RcTransactionFilter;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.DBRCFactory;
import com.neturbo.set.utils.DateTime;
import com.neturbo.set.utils.Format;
import com.neturbo.set.utils.Sorting;
import com.neturbo.set.utils.Utils;
import com.oroinc.text.regex.Util;

public class AccEnquiryBankAction extends CibAction {

	private static String defaultDatePattern = Config
			.getProperty("DefaultDatePattern");

	public void listAccountSummary() throws NTBException {
		BankUser bankuser = (BankUser) this.getUser();	
		String corpId = this.getParameter("corpId");
		if (corpId == null){
			corpId = (String)this.getUsrSessionDataValue("corpId");
		}
		
		String corpName = this.getParameter("corpName");
		if (corpName == null){
			corpName = (String)this.getUsrSessionDataValue("corpName");
		}
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser user = new CorpUser(bankuser.getUserId());
		user.setCorpId(corpId);
        user.setLanguage(lang);//add by linrui for mul-language
		// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗
		Map resultData = new HashMap();
		setResultData(resultData);
		resultData.put("showNothing", "1");
		resultData.put("corpId", corpId);
		resultData.put("corpName", corpName);		
		
		AccountEnqureBankService accountEnqureBankService = (AccountEnqureBankService) Config
				.getAppContext().getBean("AccountEnqureBankService");

		List currentAccountList = null;
		List timeDepositList = null;
		List overdraftAccountList = null;
		List loanAccountList = null;
		List savingAccountList = null;
		List creditCardList = null;
		// 闁跨喐鏋婚幏鐑芥晸閿熺禍qCcy = null, 闁跨喐鏋婚幏鐑芥晸缂佺偞鎷濋幏宄扮埐闁跨喐褰导娆愬鐠嬫捇鏁撻弬銈嗗閼板嫷鍓ㄩ幏鐑芥晸閺傘倖瀚归柨鐔告灮閹烽銇氶柨鐔虹叓娴兼瑦瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喕顢滅喊澶嬪闁跨喐鏋婚幏椋庣剨闁跨喐鏋婚幏鐑芥晸閿燂拷
		// 闁跨喐鏋婚幏铚傜闁跨喕濞囬弬銈嗗闁跨喕顕犻敍宀勬晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喓鐛ら銈嗗闁跨喎褰ㄦ潏鐐闁跨喓姘甧ssion
		// APPCODE_CURRENT_ACCOUNT
		currentAccountList = accountEnqureBankService.listSummaryByAccType(user,
				corpId, CorpAccount.ACCOUNT_TYPE_CURRENT, CibTransClient.APPCODE_CURRENT_ACCOUNT);
		//setUsrSessionDataValue("currentAccountList", currentAccountList);
		// APPCODE_TIME_DEPOSIT
		timeDepositList = accountEnqureBankService.listSummaryByAccType(user,
				corpId, CorpAccount.ACCOUNT_TYPE_TIME_DEPOSIT, CibTransClient.APPCODE_TIME_DEPOSIT);
		//setUsrSessionDataValue("timeDepositList", timeDepositList);
		// APPCODE_OVERDRAFT_ACCOUNT
		//modify by wcc 20180209
		/*overdraftAccountList = accountEnqureBankService.listSummaryByAccType(user,
				corpId, CorpAccount.ACCOUNT_TYPE_OVER_DRAFT, CibTransClient.APPCODE_OVERDRAFT_ACCOUNT);*/
		//setUsrSessionDataValue("overdraftAccountList", overdraftAccountList);
		// APPCODE_LOAN_ACCOUNT
		loanAccountList = accountEnqureBankService.listSummaryByAccType(user,
				corpId, CorpAccount.ACCOUNT_TYPE_LOAN, CibTransClient.APPCODE_LOAN_ACCOUNT);
		//setUsrSessionDataValue("loanAccountList", loanAccountList);
		// APPCODE_SAVING_ACCOUNT add by mxl 1120
		
		savingAccountList = accountEnqureBankService.listSummaryByAccType(user,
				corpId, CorpAccount.ACCOUNT_TYPE_SAVING, CibTransClient.APPCODE_SAVING_ACCOUNT);
		//setUsrSessionDataValue("savingAccountList", savingAccountList);
		//add by chen_y 2014-12-16 for CR192 bob batch
		//mofify by wcc 20180209
		//creditCardList =  creditAccountList(user,corpId);
		
		//add by lzg 20190701
		List currentAccountListTemp = new ArrayList();
		List availableCurrenciesList = accountEnqureBankService.getAvailableCurrencies();
		for(int i = 0;i < currentAccountList.size(); i++){
			Map retMap = (HashMap)currentAccountList.get(i);
			for(int j = 0; j < availableCurrenciesList.size(); j++){
				Map availableCcyMap = (HashMap)availableCurrenciesList.get(j);
				if(!availableCcyMap.get("CCY_CODE").equals(retMap.get("CURRENCY_CODE"))){
					continue;
				}
				currentAccountListTemp.add(currentAccountList.get(i));
				break;
			}
		}
		currentAccountList = currentAccountListTemp;
		
		//add by lzg 20190701
		/*List savingAccountListTemp = new ArrayList();
		for(int i = 0;i < savingAccountList.size(); i++){
			Map retMap = (HashMap)savingAccountList.get(i);
			for(int j = 0; j < availableCurrenciesList.size(); j++){
				Map availableCcyMap = (HashMap)availableCurrenciesList.get(j);
				if(!availableCcyMap.get("CCY_CODE").equals(retMap.get("CURRENCY_CODE"))){
					continue;
				}
				savingAccountListTemp.add(savingAccountList.get(i));
				break;
			}
		}
		savingAccountList = savingAccountListTemp;*/
		
		List timeDepositListTemp = new ArrayList();
		for(int i = 0;i < timeDepositList.size(); i++){
			Map retMap = (HashMap)timeDepositList.get(i);
			for(int j = 0; j < availableCurrenciesList.size(); j++){
				Map availableCcyMap = (HashMap)availableCurrenciesList.get(j);
				if(!availableCcyMap.get("CCY_CODE").equals(retMap.get("CURRENCY_CODE"))){
					continue;
				}
				timeDepositListTemp.add(timeDepositList.get(i));
				break;
			}
		}
		timeDepositList = timeDepositListTemp;
		
		List loanAccountListTemp = new ArrayList();
		for(int i = 0;i < loanAccountList.size(); i++){
			Map retMap = (HashMap)loanAccountList.get(i);
			for(int j = 0; j < availableCurrenciesList.size(); j++){
				Map availableCcyMap = (HashMap)availableCurrenciesList.get(j);
				if(!availableCcyMap.get("CCY_CODE").equals(retMap.get("CURRENCY_CODE"))){
					continue;
				}
				loanAccountListTemp.add(loanAccountList.get(i));
				break;
			}
		}
		loanAccountList = loanAccountListTemp;
		//add by lzg end
		
		
		List csvList = new ArrayList();
		// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�
		resultData.put("currentList", currentAccountList);
		if (currentAccountList.size() > 0) {
			resultData.put("showCurrentAccount", "1");
			resultData.put("showNothing", "0");
			String subTotal = "0";
			resultData.put("CA_SubTotal", subTotal);

			Map totalMap = new HashMap();
			totalMap.put("SUBTOTAL_TITLE", Utils.subTotalString(lang.toString()));//mod by linrui 20190729 for mul-lang download
			totalMap.put("SUBTOTAL", subTotal);

			Map firstMap = (Map) currentAccountList.get(0);
			firstMap.put("showAccType", Utils.getStringFromProperties("app.cib.resource.common.account_type","1",lang.toString()));//20190124 "CURRENT ACCOUNT"
			csvList.addAll(currentAccountList);
			csvList.add(totalMap);
		}

		// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�
		resultData.put("tdList", timeDepositList);
		if (timeDepositList.size() > 0) {
			resultData.put("showTimeDeposit", "1");
			resultData.put("showNothing", "0");
			String subTotal = "0";
			resultData.put("TD_SubTotal", subTotal);

			Map totalMap = new HashMap();
			totalMap.put("SUBTOTAL_TITLE", Utils.subTotalString(lang.toString()));//mod by linrui 20190729 for mul-lang download
			totalMap.put("SUBTOTAL", subTotal);
			Map firstMap = (Map) timeDepositList.get(0);
			firstMap.put("showAccType", Utils.getStringFromProperties("app.cib.resource.common.account_type","2",lang.toString()));//"TIME DEPOSIT"
			csvList.addAll(timeDepositList);
			csvList.add(totalMap);
		}

		// 闁繑鏁�
		resultData.put("overdraftList", overdraftAccountList);
		/*if (overdraftAccountList.size() > 0) {
			resultData.put("showOverdraft", "1");
			resultData.put("showNothing", "0");
			String subTotal = "0";
			resultData.put("OA_SubTotal", subTotal);

			Map totalMap = new HashMap();
			totalMap.put("SUBTOTAL_TITLE", Utils.subTotalString(lang.toString()));//mod by linrui 20190729 for mul-lang download
			totalMap.put("SUBTOTAL", subTotal);
			Map firstMap = (Map) overdraftAccountList.get(0);
			firstMap.put("showAccType", Utils.getStringFromProperties("app.cib.resource.common.account_type","3",lang.toString()));//"OVERDRAFT ACCOUNT"
			csvList.addAll(overdraftAccountList);
			csvList.add(totalMap);
		}*/

		// 闁跨喐鏋婚幏鐑芥晸閿燂拷
		resultData.put("loanList", loanAccountList);
		if (loanAccountList.size() > 0) {
			resultData.put("showLoanAccount", "1");
			resultData.put("showNothing", "0");
			String subTotal = "0";
			resultData.put("LA_SubTotal", subTotal);

			Map totalMap = new HashMap();
			totalMap.put("SUBTOTAL_TITLE", Utils.subTotalString(lang.toString()));//mod by linrui 20190729 for mul-lang download
			totalMap.put("SUBTOTAL", subTotal);
			Map firstMap = (Map) loanAccountList.get(0);
			firstMap.put("showAccType", Utils.getStringFromProperties("app.cib.resource.common.account_type","4",lang.toString()));//"LOAN ACCOUNT"
			csvList.addAll(loanAccountList);
			csvList.add(totalMap);
		}
		// Saving Account 1120
		resultData.put("savingList", savingAccountList);
		if (savingAccountList.size() > 0) {
			resultData.put("showSavingAccount", "1");
			resultData.put("showNothing", "0");
			String subTotal = "0";
			resultData.put("CC_SubTotal", subTotal);

			Map totalMap = new HashMap();
			totalMap.put("SUBTOTAL_TITLE", Utils.subTotalString(lang.toString()));//mod by linrui 20190729 for mul-lang download
			totalMap.put("SUBTOTAL", subTotal);

			Map firstMap = (Map) savingAccountList.get(0);
			firstMap.put("showAccType", Utils.getStringFromProperties("app.cib.resource.common.account_type","5",lang.toString()));//"SAVING ACCOUNT"
			csvList.addAll(savingAccountList);
			csvList.add(totalMap);
		}
		//add by chen_y 2014-12-16 for CR192 bob batch
		//Credit Card
		resultData.put("creditCardList", creditCardList);
		/*if (creditCardList.size() > 0) {
			resultData.put("showCreditCard", "1");
			resultData.put("showNothing", "0");
			String subTotal = "0";
			resultData.put("SA_SubTotal", subTotal);

			Map totalMap = new HashMap();
			totalMap.put("SUBTOTAL_TITLE", Utils.subTotalString(lang.toString()));//mod by linrui 20190729 for mul-lang download
			totalMap.put("SUBTOTAL", subTotal);

			Map firstMap = (Map) creditCardList.get(0);
			firstMap.put("showAccType", Utils.getStringFromProperties("app.cib.resource.common.account_type","6",lang.toString()));//"CREDIT CARD"
			csvList.addAll(creditCardList);
			csvList.add(totalMap);
		}*/
		
		// 閸愭瑩鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔峰建閹插瀚�
		resultData.put("csvList", csvList);
		
		this.setUsrSessionDataValue("corpId", corpId);
		this.setUsrSessionDataValue("corpName", corpName);
	}

	public double caculateTotal(List calList, String fieldName) {
		double total = 0.0;
		for (int i = 0; i < calList.size(); i++) {
			Map recordMap = (Map) calList.get(i);
			double add1 = Utils.parseDouble(Utils.null2Empty(recordMap
					.get(fieldName)));
			total += add1;
		}
		return total;
	}

	public void listCurrentAccount() throws NTBException {

		// modify by Jet
		BankUser bankuser = (BankUser) this.getUser();		
		String corpId = this.getParameter("corpId");
		if (corpId == null){
			corpId = (String)this.getUsrSessionDataValue("corpId");
		}
		
		String corpName = this.getParameter("corpName");
		if (corpName == null){
			corpName = (String)this.getUsrSessionDataValue("corpName");
		}
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser user = new CorpUser(bankuser.getUserId());
		user.setCorpId(corpId);
		user.setLanguage(lang);//add by linrui for mul-language

		AccountEnqureBankService accountEnqureBankService = (AccountEnqureBankService) Config
				.getAppContext().getBean("AccountEnqureBankService");
		CorpAccountService corpAccountService = (CorpAccountService) Config
				.getAppContext().getBean("CorpAccountService");
		
		//add by wcc 20180203
		//keyAll閻庢稒蓱閺備線骞嶉敓鑺ョ畳闂佹鍠栭敓浠嬪触閸絾宕抽柡澶堝劤濞堟垹锟藉Δ鍐惧剨濞戞搫鎷�
		String keyAll = Utils.null2EmptyWithTrim(this.getParameter("keyAll"));
		//閻犙冾儑缁増绋夋繝浣侯伇濡炪倗鏁稿▓鎴︽煥椤旂》鎷�
		String prePage = Utils.null2EmptyWithTrim(this.getParameter("prePage"));
		//閻犙冾儑缁増绋夌�ｂ晝顏卞銈囨暩濞堟垿鏌ㄩ纭锋嫹
		String nextPage = Utils.null2EmptyWithTrim(this.getParameter("nextPage"));
		//闁告帇鍊栭弻鍥倷閻熸澘姣婂ù婊冩缁楀倹绋夐敓濮愶拷閺夆晜蓱濡插憡绋夌�ｂ晝顏卞銈忔嫹
		String flagPage = Utils.null2EmptyWithTrim(this.getParameter("flagPage"));
		// monify by wcc 20180115 濠⒀呭仜婵偤宕楅崼锝囩闁告艾楠搁崵顓㈡儍閸曨剛鍨絪tack
		Stack<String> stackPage = new Stack<String>();
		//keyPage閻犲洢鍎查弳鐔虹磼閸曨厽鏆忛柡澶堝劚閻°劑寮ㄧ欢濯媑ekey缂傚牆顭烽妴澶愭儍閸曨垱鏆涢柛濠忔嫹
		String[] keyPage = new String[]{};
		//闁告帇鍊栭弻鍥及椤栨碍鍎婇柡鍕靛灣椤戝洦绋夐敓濮愶拷
		if("".equals(keyAll)){
			stackPage.push(prePage);
		}else{
			 if(keyAll.substring(keyAll.length()-1,keyAll.length()).equals(",")){
			    	keyAll = keyAll+" ";
			    }
			    keyPage = keyAll.split(",");
			   
			  for(int i=0;i<keyPage.length;i++){
				  stackPage.push(keyPage[i]);
			  }
		}
		//flagPage闁哄鍎遍崹浠嬪棘椤撶偛绔鹃柡宥呯墳缁辨繈宕欓悜妯煎灲	闁汇劌瀚弻鐔煎触閿燂拷
		if("N".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//闁绘劗鎳撻崵顔界▔鐎ｂ晝顏卞銈呯仛濡炲倿鏁嶇仦鐣屾闁哄秴鐗呴懙鎴炵▔鐎ｂ晝顏卞銈囨暩濞堟垿鏌ㄩ纭锋嫹閻犙冾儏閿熺晫绱掑▎搴ｇ憪濞戞搫鎷烽妴澶愭晬瀹�锟戒化闁告垼顔婄粭鍌涚▔閿熷锟介柡鍐啇缁辨繄浜搁崱妯煎灲濞戞搩鍘虹粭鍌涚▔閿熷锟介柣銊ュ閺侇參宕愰懝鎵偞闁稿﹪妫跨粭鍌涚▔閿熷锟�
			prePage = nextPage;
		}else if("".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//闁绘劗鎳撻崵顔界▔鐎ｂ晝顏卞銈呯仛濡炲倿鏁嶇仦鐣屾闁哄秴鐗呴懙鎴炵▔鐎ｂ晝顏卞銈囨暩濞堟垿鏌ㄩ纭锋嫹閻犙冾儏閿熺晫绱掑▎搴ｇ憪濞戞搫鎷烽妴澶愭晬瀹�锟戒化闁告垼顔婄粭鍌涚▔閿熷锟介柡鍐啇缁辨繄浜搁崱妯煎灲濞戞搩鍘虹粭鍌涚▔閿熷锟介柣銊ュ閺侇參宕愰懝鎵偞闁稿﹪妫跨粭鍌涚▔閿熷锟�
			prePage = nextPage;
		}else if("P".equals(flagPage)){
			//闁绘劗鎳撻崵顔界▔婵犱胶顏卞銈呯仛濡炲倿鏁嶇仦钘夊弗閻忓繐妫楅妵鏃�绋夐妶鍕殝闂佹鍠栭敓锟�
			stackPage.pop();
			stackPage.pop();
			//闁瑰嘲鐏濋崺宀�绱欐繝姘ワ拷闁汇劌瀚伴弫顓㈠磹閿燂拷
			prePage = stackPage.pop();
			stackPage.push(prePage);
		}
		//modify by wcc 20180208
		/*List currentAccountList = accountEnqureBankService.listCurrentAccount(user,
				user.getCorpId());*/
		List currentAccountList = accountEnqureBankService.listCurrentAccount(user,user.getCorpId(),CibTransClient.APPCODE_CURRENT_ACCOUNT,prePage);
		
		//modify by wcc 20180115 闁瑰嘲鐏濋崵锟絅EXT_KEY"闁汇劌瀚伴弫顓㈠磹閿燂拷
		nextPage = currentAccountList.get(currentAccountList.size()-1).toString();
		
		//disableN,disableP闁活枌鍔嶅鐢稿礆閵堝棙鐒藉☉鎾筹梗缁斿瓨銇勭喊澶岀濞戞挸顑勭粩瀛樸亜閸偄鐦婚梺绛嬪枟濡叉悂宕ラ敂鑺モ枖缂侊拷浜介敓锟�
		String disableN = "";
		if(!"".equals(nextPage)&&!"".equals(nextPage.trim())){
			 disableN = "button";
		}else{
			 disableN = "hidden";
		}
		String disableP = "";
		if(!"".equals(prePage)){
			disableP = "button";
		}else{
			disableP = "hidden";
		}
		//modify by wcc 20180115 remove闁瑰搫顦诲﹢鐚tCurrentPostingStmt闁哄倽顬冪涵鑸电▔椤撶儐鏉婚柛鏃傚Х濞堟唲erPage閻庣數顢婇挅锟�
		currentAccountList.remove(currentAccountList.size()-1);
		//閻忓繐妫楃欢閬嶅礆閹殿喗鐣遍柡鍌涘濞堟垿鏌ㄩ纭锋嫹闁规亽鍎冲鍐储鐎ｎ亜寮抽柡宥忔嫹
		
		//add by lzg 20190701
		List currentAccountListTemp = new ArrayList();
		List availableCurrenciesList = accountEnqureBankService.getAvailableCurrencies();
		for(int i = 0;i < currentAccountList.size(); i++){
			Map retMap = (HashMap)currentAccountList.get(i);
			for(int j = 0; j < availableCurrenciesList.size(); j++){
				Map availableCcyMap = (HashMap)availableCurrenciesList.get(j);
				if(!availableCcyMap.get("CCY_CODE").equals(retMap.get("CURRENCY_CODE"))){
					continue;
				}
				currentAccountListTemp.add(currentAccountList.get(i));
				break;
			}
		}
		currentAccountList = currentAccountListTemp;
		//add by lzg end
		
		stackPage.push(nextPage);
		//闁烩懇濞峵ringBuffer闁哄鍎茬敮鎾绩閸洘鏆涢柛濠呭亹濞堟垹锟藉Δ鍐惧剨濞戞捁顕滅槐婵囩閵夆晪鎷烽柛娆忓槻閸ㄥ酣姊鹃弮鎾剁闁告ê顭峰▍搴ㄥ嫉閿熻姤鍊靛☉鎿勬嫹闁叉粍寰勫顐ょ▏闁汇劌瀚伴敓浠嬪矗閺嬵偓鎷�
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<stackPage.size();i++){
			sb.append(stackPage.get(i)).append(",");
		}
		sb.replace(sb.length()-1, sb.length(),"");
		keyAll = sb.toString();
		
		// modify by hjs//modfiy by wcc 20180208
		Map row = null;
		int status4Count = 0;
		for (int i = 0; i < currentAccountList.size(); i++) {
			row = (Map) currentAccountList.get(i);
			String account = row.get("ACCOUNT_NO").toString().trim();
			/*String accountName = corpAccountService.getAccountName(account);
			row.put("ACCOUNT_NAME", accountName);*/
			if(row.get("ACCOUNT_STATUS").equals("4")){
				status4Count++;
			}
		}
		if(currentAccountList.size() == status4Count){
			currentAccountList.clear();
		}
		Map resultData = new HashMap();
		resultData.put("currentAccountList", currentAccountList);
		resultData.put("corpId", corpId);
		resultData.put("corpName", corpName);
		//modify by wcc 20180203
		resultData.put("nextPage",nextPage);
		resultData.put("prePage",prePage);
		resultData.put("disableN",disableN);
		resultData.put("disableP",disableP);
		resultData.put("keyAll",keyAll);
		setResultData(resultData);

		this.setUsrSessionDataValue("corpId", corpId);
		this.setUsrSessionDataValue("corpName", corpName);

	}

	public void viewCurrentAccount() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService accService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171106
		// modify by Jet
		BankUser bankuser = (BankUser) this.getUser();		
		String corpId = (String)this.getUsrSessionDataValue("corpId");
		CorpUser user = new CorpUser(bankuser.getUserId());
		user.setCorpId(corpId);
		user.setLanguage(lang);//add by linrui for mul-language 20171106

		String accountNo = this.getParameter("accountNo");
		String from_ccy = this.getParameter("from_ccy");
		AccountEnqureBankService accountEnqureBankService = (AccountEnqureBankService) Config
				.getAppContext().getBean("AccountEnqureBankService");
		//add by linrui 20190918
		String sortOrder = Utils.null2EmptyWithTrim(this.getParameter("sortOrder"));
		if("".equals(sortOrder)) sortOrder = "1";
		Map currentAccountView = accountEnqureBankService.viewCurrentDetail(user,
				accountNo,from_ccy);
		currentAccountView.put("ACCOUNT_TYPE", accService
				.getAccountType(accountNo,from_ccy));
		currentAccountView.put("fromPage", Utils.null2EmptyWithTrim(this
				.getParameter("fromPage")));
		currentAccountView.put("sortOrder", sortOrder);
		/*if("".equals(Utils.null2EmptyWithTrim(currentAccountView.get("CCY_CODE_OF_AC")))){
			currentAccountView.put("CCY_CODE_OF_AC", from_ccy);
		}*/
		ArrayList ccyBalList = (ArrayList)currentAccountView.get("CCY_BAL_LIST");
		int count = 0;
		HashMap ccyBal;
		ArrayList ccyList = new ArrayList();
		for(int i = 0;i < ccyBalList.size();i++){
			ccyBal = (HashMap)ccyBalList.get(i);
			String ccy = (String)ccyBal.get("CCY_CODE_OF_AC");
			if(ccy != null){
				if(!"".equals(ccy.trim())){
					ccyList.add(ccy);
				}
			}
			
		}
		if(ccyList.size() == 1){
			if("".equals(ccyList.get(0).toString().trim())){
				currentAccountView.put("CCY_CODE_OF_AC", from_ccy);
			}else{
				currentAccountView.put("CCY_CODE_OF_AC", ccyList.get(0).toString().trim());
			}
			currentAccountView.put("CURRENT_BALANCE", ((HashMap)ccyBalList.get(0)).get("CURRENT_BALANCE"));
			currentAccountView.put("AVAILABLE_BALANCE", ((HashMap)ccyBalList.get(0)).get("AVAILABLE_BALANCE"));
		}else if(ccyList.size() > 1){
			currentAccountView.put("ccyList", ccyList);
			currentAccountView.put("ccyBalList", ccyBalList);
		}
		this.setUsrSessionDataValue("accDetail", currentAccountView);
		listTransHistory();

	}

	// add by mxl 1120
	public void listSavingAccount() throws NTBException {
		// modify by Jet
		BankUser bankuser = (BankUser) this.getUser();		
		String corpId = this.getParameter("corpId");
		if (corpId == null){
			corpId = (String)this.getUsrSessionDataValue("corpId");
		}		
		String corpName = this.getParameter("corpName");
		if (corpName == null){
			corpName = (String)this.getUsrSessionDataValue("corpName");
		}
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser user = new CorpUser(bankuser.getUserId());
		user.setCorpId(corpId);
        user.setLanguage(lang);//add by linrui for mul-language
		CorpAccountService corpAccountService = (CorpAccountService) Config
				.getAppContext().getBean("CorpAccountService");
		AccountEnqureBankService accountEnqureBankService = (AccountEnqureBankService) Config
				.getAppContext().getBean("AccountEnqureBankService");
		
		//add by wcc 20180203
		//keyAll閻庢稒蓱閺備線骞嶉敓鑺ョ畳闂佹鍠栭敓浠嬪触閸絾宕抽柡澶堝劤濞堟垹锟藉Δ鍐惧剨濞戞搫鎷�
		String keyAll = Utils.null2EmptyWithTrim(this.getParameter("keyAll"));
		//閻犙冾儑缁増绋夋繝浣侯伇濡炪倗鏁稿▓鎴︽煥椤旂》鎷�
		String prePage = Utils.null2EmptyWithTrim(this.getParameter("prePage"));
		//閻犙冾儑缁増绋夌�ｂ晝顏卞銈囨暩濞堟垿鏌ㄩ纭锋嫹
		String nextPage = Utils.null2EmptyWithTrim(this.getParameter("nextPage"));
		//闁告帇鍊栭弻鍥倷閻熸澘姣婂ù婊冩缁楀倹绋夐敓濮愶拷閺夆晜蓱濡插憡绋夌�ｂ晝顏卞銈忔嫹
		String flagPage = Utils.null2EmptyWithTrim(this.getParameter("flagPage"));
		// monify by wcc 20180115 濠⒀呭仜婵偤宕楅崼锝囩闁告艾楠搁崵顓㈡儍閸曨剛鍨絪tack
		Stack<String> stackPage = new Stack<String>();
		//keyPage閻犲洢鍎查弳鐔虹磼閸曨厽鏆忛柡澶堝劚閻°劑寮ㄧ欢濯媑ekey缂傚牆顭烽妴澶愭儍閸曨垱鏆涢柛濠忔嫹
		String[] keyPage = new String[]{};
		//闁告帇鍊栭弻鍥及椤栨碍鍎婇柡鍕靛灣椤戝洦绋夐敓濮愶拷
		if("".equals(keyAll)){
			stackPage.push(prePage);
		}else{
			 if(keyAll.substring(keyAll.length()-1,keyAll.length()).equals(",")){
			    	keyAll = keyAll+" ";
			    }
			    keyPage = keyAll.split(",");
			   
			  for(int i=0;i<keyPage.length;i++){
				  stackPage.push(keyPage[i]);
			  }
		}
		//flagPage闁哄鍎遍崹浠嬪棘椤撶偛绔鹃柡宥呯墳缁辨繈宕欓悜妯煎灲	闁汇劌瀚弻鐔煎触閿燂拷
		if("N".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//闁绘劗鎳撻崵顔界▔鐎ｂ晝顏卞銈呯仛濡炲倿鏁嶇仦鐣屾闁哄秴鐗呴懙鎴炵▔鐎ｂ晝顏卞銈囨暩濞堟垿鏌ㄩ纭锋嫹閻犙冾儏閿熺晫绱掑▎搴ｇ憪濞戞搫鎷烽妴澶愭晬瀹�锟戒化闁告垼顔婄粭鍌涚▔閿熷锟介柡鍐啇缁辨繄浜搁崱妯煎灲濞戞搩鍘虹粭鍌涚▔閿熷锟介柣銊ュ閺侇參宕愰懝鎵偞闁稿﹪妫跨粭鍌涚▔閿熷锟�
			prePage = nextPage;
		}else if("".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//闁绘劗鎳撻崵顔界▔鐎ｂ晝顏卞銈呯仛濡炲倿鏁嶇仦鐣屾闁哄秴鐗呴懙鎴炵▔鐎ｂ晝顏卞銈囨暩濞堟垿鏌ㄩ纭锋嫹閻犙冾儏閿熺晫绱掑▎搴ｇ憪濞戞搫鎷烽妴澶愭晬瀹�锟戒化闁告垼顔婄粭鍌涚▔閿熷锟介柡鍐啇缁辨繄浜搁崱妯煎灲濞戞搩鍘虹粭鍌涚▔閿熷锟介柣銊ュ閺侇參宕愰懝鎵偞闁稿﹪妫跨粭鍌涚▔閿熷锟�
			prePage = nextPage;
		}else if("P".equals(flagPage)){
			//闁绘劗鎳撻崵顔界▔婵犱胶顏卞銈呯仛濡炲倿鏁嶇仦钘夊弗閻忓繐妫楅妵鏃�绋夐妶鍕殝闂佹鍠栭敓锟�
			stackPage.pop();
			stackPage.pop();
			//闁瑰嘲鐏濋崺宀�绱欐繝姘ワ拷闁汇劌瀚伴弫顓㈠磹閿燂拷
			prePage = stackPage.pop();
			stackPage.push(prePage);
		}
		//modify by wcc 20180209
		//List savingAccountList = accountEnqureBankService.listSavingAccount(user,
				//user.getCorpId());
		List savingAccountList = accountEnqureBankService.listSavingAccount(user,user.getCorpId(),CibTransClient.APPCODE_SAVING_ACCOUNT,prePage);
		//modify by wcc 20180115 闁瑰嘲鐏濋崵锟絅EXT_KEY"闁汇劌瀚伴弫顓㈠磹閿燂拷
		
		nextPage = savingAccountList.get(savingAccountList.size()-1).toString();
		
		//disableN,disableP闁活枌鍔嶅鐢稿礆閵堝棙鐒藉☉鎾筹梗缁斿瓨銇勭喊澶岀濞戞挸顑勭粩瀛樸亜閸偄鐦婚梺绛嬪枟濡叉悂宕ラ敂鑺モ枖缂侊拷浜介敓锟�
		String disableN = "";
		if(!"".equals(nextPage)&&!"".equals(nextPage.trim())){
			 disableN = "button";
		}else{
			 disableN = "hidden";
		}
		String disableP = "";
		if(!"".equals(prePage)){
			disableP = "button";
		}else{
			disableP = "hidden";
		}
		//modify by wcc 20180115 remove闁瑰搫顦诲﹢鐚tCurrentPostingStmt闁哄倽顬冪涵鑸电▔椤撶儐鏉婚柛鏃傚Х濞堟唲erPage閻庣數顢婇挅锟�
		savingAccountList.remove(savingAccountList.size()-1);
		//閻忓繐妫楃欢閬嶅礆閹殿喗鐣遍柡鍌涘濞堟垿鏌ㄩ纭锋嫹闁规亽鍎冲鍐储鐎ｎ亜寮抽柡宥忔嫹
		
		
		//add by lzg 20191224
				if ( savingAccountList != null && savingAccountList.size() > 1) {
					Collections.sort(savingAccountList, new Comparator(){
						@Override
						public int compare(Object o1, Object o2) {
							HashMap map1 =  (HashMap) o1;
							HashMap map2 =  (HashMap) o2;
							BigDecimal value1 =new BigDecimal(map1.get("ACCOUNT_NO").toString());
							BigDecimal value2 =new BigDecimal(map2.get("ACCOUNT_NO").toString());
							return value1.compareTo(value2);
						}
						
					});
				}
				//end
		
		//add by lzg 20190701
		/*List savingAccountListTemp = new ArrayList();
		List availableCurrenciesList = accountEnqureBankService.getAvailableCurrencies();
		for(int i = 0;i < savingAccountList.size(); i++){
			Map retMap = (HashMap)savingAccountList.get(i);
			for(int j = 0; j < availableCurrenciesList.size(); j++){
				Map availableCcyMap = (HashMap)availableCurrenciesList.get(j);
				if(!availableCcyMap.get("CCY_CODE").equals(retMap.get("CURRENCY_CODE"))){
					continue;
				}
				savingAccountListTemp.add(savingAccountList.get(i));
				break;
			}
		}
		savingAccountList = savingAccountListTemp;*/
		//add by lzg end
		
		stackPage.push(nextPage);
		//闁烩懇濞峵ringBuffer闁哄鍎茬敮鎾绩閸洘鏆涢柛濠呭亹濞堟垹锟藉Δ鍐惧剨濞戞捁顕滅槐婵囩閵夆晪鎷烽柛娆忓槻閸ㄥ酣姊鹃弮鎾剁闁告ê顭峰▍搴ㄥ嫉閿熻姤鍊靛☉鎿勬嫹闁叉粍寰勫顐ょ▏闁汇劌瀚伴敓浠嬪矗閺嬵偓鎷�
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<stackPage.size();i++){
			sb.append(stackPage.get(i)).append(",");
		}
		sb.replace(sb.length()-1, sb.length(),"");
		keyAll = sb.toString();
		
		// modify by hjs
		Map row = null;
		int status4Count = 0;
		for (int i = 0; i < savingAccountList.size(); i++) {
			row = (Map) savingAccountList.get(i);
			row.put("ACCOUNT_NAME", corpAccountService.getAccountName(row.get(
					"ACCOUNT_NO").toString().trim(),row.get("CURRENCY_CODE").toString().trim()));
			if(row.get("ACCOUNT_STATUS").equals("4")){
				status4Count++;
			}
		}
		if(savingAccountList.size() == status4Count){
			savingAccountList.clear();
		}
		Map resultData = new HashMap();
		resultData.put("savingAccountList", savingAccountList);
		resultData.put("corpId", corpId);
		resultData.put("corpName", corpName);
		//modify by wcc 20180203
		resultData.put("nextPage",nextPage);
		resultData.put("prePage",prePage);
		resultData.put("disableN",disableN);
		resultData.put("disableP",disableP);
		resultData.put("keyAll",keyAll);
		setResultData(resultData);

		this.setUsrSessionDataValue("corpId", corpId);
		this.setUsrSessionDataValue("corpName", corpName);
	}

	public void viewSavingAccount() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService accService = (CorpAccountService) appContext
				.getBean("CorpAccountService");

		// modify by Jet
		BankUser bankuser = (BankUser) this.getUser();	
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171106
		
		String corpId = (String)this.getUsrSessionDataValue("corpId");
		CorpUser user = new CorpUser(bankuser.getUserId());
		user.setCorpId(corpId);
		user.setLanguage(lang);//add by linrui for mul-language 20171106

		String accountNo = this.getParameter("accountNo");
		String from_ccy = this.getParameter("from_ccy");
		AccountEnqureBankService accountEnqureBankService = (AccountEnqureBankService) Config
				.getAppContext().getBean("AccountEnqureBankService");
		//add by linrui 20190918
		String sortOrder = Utils.null2EmptyWithTrim(this.getParameter("sortOrder"));
		if("".equals(sortOrder)) sortOrder = "1";
		Map savingAccountView = accountEnqureBankService.viewSavingDetail(user,
				accountNo,from_ccy);
		savingAccountView.put("ACCOUNT_TYPE", accService
				.getAccountType(accountNo,from_ccy));
		savingAccountView.put("fromPage", Utils.null2EmptyWithTrim(this
				.getParameter("fromPage")));
		savingAccountView.put("sortOrder", sortOrder);
		/*if("".equals(Utils.null2EmptyWithTrim(savingAccountView.get("CCY_CODE_OF_AC")))){
			savingAccountView.put("CCY_CODE_OF_AC", from_ccy);
		}*/
		ArrayList ccyBalList = (ArrayList)savingAccountView.get("CCY_BAL_LIST");
		int count = 0;
		HashMap ccyBal;
		ArrayList ccyList = new ArrayList();
		for(int i = 0;i < ccyBalList.size();i++){
			ccyBal = (HashMap)ccyBalList.get(i);
			String ccy = (String)ccyBal.get("CCY_CODE_OF_AC");
			if(ccy != null){
				if(!"".equals(ccy.trim())){
					ccyList.add(ccy);
				}
			}
			
		}
		if(ccyList.size() == 1){
			if("".equals(ccyList.get(0).toString().trim())){
				savingAccountView.put("CCY_CODE_OF_AC", from_ccy);
			}else{
				savingAccountView.put("CCY_CODE_OF_AC", ccyList.get(0).toString().trim());
			}
			savingAccountView.put("CURRENT_BALANCE", ((HashMap)ccyBalList.get(0)).get("CURRENT_BALANCE"));
			savingAccountView.put("AVAILABLE_BALANCE", ((HashMap)ccyBalList.get(0)).get("AVAILABLE_BALANCE"));
		}else if(ccyList.size() > 1){
			savingAccountView.put("ccyList", ccyList);
			savingAccountView.put("ccyBalList", ccyBalList);
		}
		this.setUsrSessionDataValue("accDetail", savingAccountView);
		listTransHistory();
	}

	public void listTimeDeposit() throws NTBException {
		// modify by Jet
		BankUser bankuser = (BankUser) this.getUser();		
		String corpId = this.getParameter("corpId");
		if (corpId == null){
			corpId = (String)this.getUsrSessionDataValue("corpId");
		}
		String corpName = this.getParameter("corpName");
		if (corpName == null){
			corpName = (String)this.getUsrSessionDataValue("corpName");
		}
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171106
		CorpUser user = new CorpUser(bankuser.getUserId());
		user.setCorpId(corpId);
        user.setLanguage(lang);//add by linrui for mul-language 20171106
		AccountEnqureBankService accountEnqureBankService = (AccountEnqureBankService) Config
				.getAppContext().getBean("AccountEnqureBankService");
		
		//add by wcc 20180203
		//keyAll閻庢稒蓱閺備線骞嶉敓鑺ョ畳闂佹鍠栭敓浠嬪触閸絾宕抽柡澶堝劤濞堟垹锟藉Δ鍐惧剨濞戞搫鎷�
		String keyAll = Utils.null2EmptyWithTrim(this.getParameter("keyAll"));
		//閻犙冾儑缁増绋夋繝浣侯伇濡炪倗鏁稿▓鎴︽煥椤旂》鎷�
		String prePage = Utils.null2EmptyWithTrim(this.getParameter("prePage"));
		//閻犙冾儑缁増绋夌�ｂ晝顏卞銈囨暩濞堟垿鏌ㄩ纭锋嫹
		String nextPage = Utils.null2EmptyWithTrim(this.getParameter("nextPage"));
		//闁告帇鍊栭弻鍥倷閻熸澘姣婂ù婊冩缁楀倹绋夐敓濮愶拷閺夆晜蓱濡插憡绋夌�ｂ晝顏卞銈忔嫹
		String flagPage = Utils.null2EmptyWithTrim(this.getParameter("flagPage"));
		// monify by wcc 20180115 濠⒀呭仜婵偤宕楅崼锝囩闁告艾楠搁崵顓㈡儍閸曨剛鍨絪tack
		Stack<String> stackPage = new Stack<String>();
		//keyPage閻犲洢鍎查弳鐔虹磼閸曨厽鏆忛柡澶堝劚閻°劑寮ㄧ欢濯媑ekey缂傚牆顭烽妴澶愭儍閸曨垱鏆涢柛濠忔嫹
		String[] keyPage = new String[]{};
		//闁告帇鍊栭弻鍥及椤栨碍鍎婇柡鍕靛灣椤戝洦绋夐敓濮愶拷
		if("".equals(keyAll)){
			stackPage.push(prePage);
		}else{
			 if(keyAll.substring(keyAll.length()-1,keyAll.length()).equals(",")){
			    	keyAll = keyAll+" ";
			    }
			    keyPage = keyAll.split(",");
			   
			  for(int i=0;i<keyPage.length;i++){
				  stackPage.push(keyPage[i]);
			  }
		}
		//flagPage闁哄鍎遍崹浠嬪棘椤撶偛绔鹃柡宥呯墳缁辨繈宕欓悜妯煎灲	闁汇劌瀚弻鐔煎触閿燂拷
		if("N".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//闁绘劗鎳撻崵顔界▔鐎ｂ晝顏卞銈呯仛濡炲倿鏁嶇仦鐣屾闁哄秴鐗呴懙鎴炵▔鐎ｂ晝顏卞銈囨暩濞堟垿鏌ㄩ纭锋嫹閻犙冾儏閿熺晫绱掑▎搴ｇ憪濞戞搫鎷烽妴澶愭晬瀹�锟戒化闁告垼顔婄粭鍌涚▔閿熷锟介柡鍐啇缁辨繄浜搁崱妯煎灲濞戞搩鍘虹粭鍌涚▔閿熷锟介柣銊ュ閺侇參宕愰懝鎵偞闁稿﹪妫跨粭鍌涚▔閿熷锟�
			prePage = nextPage;
		}else if("".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//闁绘劗鎳撻崵顔界▔鐎ｂ晝顏卞銈呯仛濡炲倿鏁嶇仦鐣屾闁哄秴鐗呴懙鎴炵▔鐎ｂ晝顏卞銈囨暩濞堟垿鏌ㄩ纭锋嫹閻犙冾儏閿熺晫绱掑▎搴ｇ憪濞戞搫鎷烽妴澶愭晬瀹�锟戒化闁告垼顔婄粭鍌涚▔閿熷锟介柡鍐啇缁辨繄浜搁崱妯煎灲濞戞搩鍘虹粭鍌涚▔閿熷锟介柣銊ュ閺侇參宕愰懝鎵偞闁稿﹪妫跨粭鍌涚▔閿熷锟�
			prePage = nextPage;
		}else if("P".equals(flagPage)){
			//闁绘劗鎳撻崵顔界▔婵犱胶顏卞銈呯仛濡炲倿鏁嶇仦钘夊弗閻忓繐妫楅妵鏃�绋夐妶鍕殝闂佹鍠栭敓锟�
			stackPage.pop();
			stackPage.pop();
			//闁瑰嘲鐏濋崺宀�绱欐繝姘ワ拷闁汇劌瀚伴弫顓㈠磹閿燂拷
			prePage = stackPage.pop();
			stackPage.push(prePage);
		}
		//modify by wcc 20180
		/*List timeDepositList = accountEnqureBankService.listTimeDeposit(user, user
				.getCorpId());*/
		List timeDepositList = accountEnqureBankService.listTimeDeposit(user, user.getCorpId(),CibTransClient.APPCODE_TIME_DEPOSIT, prePage);
		//modify by wcc 20180115 闁瑰嘲鐏濋崵锟絅EXT_KEY"闁汇劌瀚伴弫顓㈠磹閿燂拷
		nextPage = timeDepositList.get(timeDepositList.size()-1).toString();
		
		//disableN,disableP闁活枌鍔嶅鐢稿礆閵堝棙鐒藉☉鎾筹梗缁斿瓨銇勭喊澶岀濞戞挸顑勭粩瀛樸亜閸偄鐦婚梺绛嬪枟濡叉悂宕ラ敂鑺モ枖缂侊拷浜介敓锟�
		String disableN = "";
		if(!"".equals(nextPage)&&!"".equals(nextPage.trim())){
			 disableN = "button";
		}else{
			 disableN = "hidden";
		}
		String disableP = "";
		if(!"".equals(prePage)){
			disableP = "button";
		}else{
			disableP = "hidden";
		}
		//modify by wcc 20180115 remove闁瑰搫顦诲﹢鐚tCurrentPostingStmt闁哄倽顬冪涵鑸电▔椤撶儐鏉婚柛鏃傚Х濞堟唲erPage閻庣數顢婇挅锟�
		timeDepositList.remove(timeDepositList.size()-1);
		//閻忓繐妫楃欢閬嶅礆閹殿喗鐣遍柡鍌涘濞堟垿鏌ㄩ纭锋嫹闁规亽鍎冲鍐储鐎ｎ亜寮抽柡宥忔嫹
		
		//add by lzg 20190701
		List timeDepositListTemp = new ArrayList();
		List availableCurrenciesList = accountEnqureBankService.getAvailableCurrencies();
		for(int i = 0;i < timeDepositList.size(); i++){
			Map retMap = (HashMap)timeDepositList.get(i);
			for(int j = 0; j < availableCurrenciesList.size(); j++){
				Map availableCcyMap = (HashMap)availableCurrenciesList.get(j);
				if(!availableCcyMap.get("CCY_CODE").equals(retMap.get("CURRENCY_CODE"))){
					continue;
				}
				timeDepositListTemp.add(timeDepositList.get(i));
				break;
			}
		}
		timeDepositList = timeDepositListTemp;
		//add by lzg end
		
		stackPage.push(nextPage);
		//闁烩懇濞峵ringBuffer闁哄鍎茬敮鎾绩閸洘鏆涢柛濠呭亹濞堟垹锟藉Δ鍐惧剨濞戞捁顕滅槐婵囩閵夆晪鎷烽柛娆忓槻閸ㄥ酣姊鹃弮鎾剁闁告ê顭峰▍搴ㄥ嫉閿熻姤鍊靛☉鎿勬嫹闁叉粍寰勫顐ょ▏闁汇劌瀚伴敓浠嬪矗閺嬵偓鎷�
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<stackPage.size();i++){
			sb.append(stackPage.get(i)).append(",");
		}
		sb.replace(sb.length()-1, sb.length(),"");
		keyAll = sb.toString();
		
		// 鐠ㄣ劑鏁撻懘姘枦閹风兘鏁撶徊锟�
		// modify by hjs 2006-09-01
		Map timeDeposit = null;
		Map tdDetial = null;
		String period = "";
		List openList = new ArrayList();
		RcCurrency rc = new RcCurrency();
		for (int i = 0; i < timeDepositList.size(); i++) {

			// hjs: set period
			timeDeposit = (Map) timeDepositList.get(i);
			//modify by wcc 20180209
			//if (timeDeposit.get("ACCOUNT_STATUS").toString().trim().equals("1")) {

				timeDeposit.put("MATURITY_DATE", timeDeposit.get(
						"MATURITY_DATE").toString().trim());
				tdDetial = accountEnqureBankService.viewDepositDetial(user,
						timeDeposit.get("ACCOUNT_NO").toString());
				period = tdDetial.get("INT_FREQUENCY").toString()
						+ tdDetial.get("REN_PERIOD").toString();
				timeDeposit.put("PERIOD", accountEnqureBankService
						.formatPeriod(period));
				timeDeposit.put("currency", rc.getCcyByCbsNumber(timeDeposit.get("CURRENCY_CODE").toString().trim()));

				openList.add(timeDeposit);
			//}
		}
		String sortType = Utils.null2EmptyWithTrim(this.getParameter("sortType"));
		if(sortType.equals("M")) {
			Sorting.sortMapList(openList,
					new String[]{"MATURITY_DATE", "ACCOUNT_NO"}, Sorting.SORT_TYPE_ASC);
		} else {
			Sorting.sortMapList(openList,
					new String[]{"currency", "ACCOUNT_NO"}, Sorting.SORT_TYPE_ASC);
		}

		Map resultData = new HashMap();
		resultData.put("timeDepositList", openList);
		resultData.put("sortType", sortType);
		resultData.put("corpId", corpId);
		resultData.put("corpName", corpName);
		//modify by wcc 20180203
		resultData.put("nextPage",nextPage);
		resultData.put("prePage",prePage);
		resultData.put("disableN",disableN);
		resultData.put("disableP",disableP);
		resultData.put("keyAll",keyAll);
		setResultData(resultData);

		this.setUsrSessionDataValue("corpId", corpId);
		this.setUsrSessionDataValue("corpName", corpName);
	}

	public void viewTimeDeposit() throws NTBException {
		BankUser bankuser = (BankUser) this.getUser();		
		String corpId = (String)this.getUsrSessionDataValue("corpId");
		String corpName = (String)this.getUsrSessionDataValue("corpName");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171106
		CorpUser user = new CorpUser(bankuser.getUserId());
		user.setCorpId(corpId);
        user.setLanguage(lang);//add by linrui for mul-langguage 20171106
		// 闁跨喐鏋婚幏鐑芥晸閻偆鈹栫喊澶嬪 ResultData 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔虹哺閹惧懏瀚归柨鐔告灮閹凤拷
		setResultData(new HashMap(1));
		this.clearUsrSessionDataValue();

		ApplicationContext appContext = Config.getAppContext();
		AccountEnqureBankService accountEnqureBankService = (AccountEnqureBankService) appContext.getBean("AccountEnqureBankService");
		TimeDepositService timeDepositService = (TimeDepositService) appContext.getBean("TimeDepositService");

		String accountNo = this.getParameter("tdAccountNo");
		// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹峰嘲褰囬柨鐔稿焻绾板瀚筪eposit闁跨喐鏋婚幏椋庣矎闁跨喐鏋婚幏閿嬩紖
		Map hostTDDetial = accountEnqureBankService.viewDepositDetial(user, accountNo);
		//String principal = hostTDDetial.get("CUR_BAL").toString();
		String principal = hostTDDetial.get("ISSUE_AMOUNT").toString();//mod by linrui 20180402
		String interest = hostTDDetial.get("NEXT_INT_AMT").toString();
		//String period = hostTDDetial.get("INT_FREQUENCY").toString() + hostTDDetial.get("REN_PERIOD").toString();
		double netCreditAmt = Double.parseDouble(hostTDDetial.get("CUR_BAL").toString());//mod by linrui 20180402 
		double principalAmt = Double.parseDouble(principal);
//		hostTDDetial.put("period", accountEnqureBankService.formatPeriod(period));
//		hostTDDetial.put("period", Utils.tinor2pe(hostTDDetial.get("INT_FREQUENCY").toString()));//mod by linrui 20180402
		hostTDDetial.put("period", timeDepositService.getPeriodDescription(Utils.prefixZero(hostTDDetial.get("INT_FREQUENCY").toString(),3),Format.transferLang(lang.toString())));//mod by linrui 20190326
//		hostTDDetial.put("netCreditAmt", String.valueOf(netCreditAmt));
		hostTDDetial.put("netCreditAmt", String.valueOf(netCreditAmt+principalAmt));//mod by linrui 20180402
          
		Map resultData = new HashMap();
		resultData.putAll(hostTDDetial);
		resultData.put("corpId", corpId);		
		resultData.put("corpName", corpName);
		//add by linrui 20180402
		String interserRate = hostTDDetial.get("INT_RATE").toString();
		//add by lzg 20190814
		resultData.put("instCd", hostTDDetial.get("INST_CD"));
		//add by lzg end
		resultData.put("INT_RATE", (interserRate.indexOf(".")>0)?interserRate.replaceAll("0+?$", "").replaceAll("[.]$", ""):interserRate);
		setResultData(resultData);
	}

	public void listOverdraftAccount() throws NTBException {
		// modify by Jet
		BankUser bankuser = (BankUser) this.getUser();		
		String corpId = this.getParameter("corpId");
		if (corpId == null){
			corpId = (String)this.getUsrSessionDataValue("corpId");
		}

		String corpName = this.getParameter("corpName");
		if (corpName == null){
			corpName = (String)this.getUsrSessionDataValue("corpName");
		}
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser user = new CorpUser(bankuser.getUserId());
		user.setCorpId(corpId);
		user.setLanguage(lang);//add by linrui for mul-language

		// modify by hjs
		AccountEnqureBankService accountEnqureBankService = (AccountEnqureBankService) Config
				.getAppContext().getBean("AccountEnqureBankService");
		List overdraftAccountList = accountEnqureBankService.listOverdraftAccount(
				user, user.getCorpId());
		CorpAccountService corpAccountService = (CorpAccountService) Config
				.getAppContext().getBean("CorpAccountService");
		// modify by hjs
		Map row = null;
		int status4Count = 0;
		for (int i = 0; i < overdraftAccountList.size(); i++) {
			row = (Map) overdraftAccountList.get(i);
			row.put("ACCOUNT_NAME", corpAccountService.getAccountName(row.get(
					"ACCOUNT_NO").toString().trim(),row.get("CURRENCY_CODE").toString().trim()));
			if(row.get("ACCOUNT_STATUS").equals("4")){
				status4Count++;
			}
		}
		if(overdraftAccountList.size() == status4Count){
			overdraftAccountList.clear();
		}
		Map resultData = new HashMap();
		resultData.put("overdraftAccountList", overdraftAccountList);
		resultData.put("corpId", corpId);
		resultData.put("corpName", corpName);
		setResultData(resultData);

		this.setUsrSessionDataValue("corpId", corpId);
		this.setUsrSessionDataValue("corpName", corpName);
	}

	public void viewOverdraftAccount() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService accService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171106

		// modify by Jet
		BankUser bankuser = (BankUser) this.getUser();		
		String corpId = (String)this.getUsrSessionDataValue("corpId");
		CorpUser user = new CorpUser(bankuser.getUserId());
		user.setCorpId(corpId);
		user.setLanguage(lang);//add by linrui for mul-language 20171106

		String accountNo = this.getParameter("accountNo");
		String from_ccy = this.getParameter("from_ccy");
		AccountEnqureBankService accountEnqureBankService = (AccountEnqureBankService) Config
				.getAppContext().getBean("AccountEnqureBankService");
		Map overdraftAccountView = accountEnqureBankService.viewOverdraftDetial(
				user, accountNo,from_ccy);
		overdraftAccountView.put("ACCOUNT_TYPE", accService
				.getAccountType(accountNo,from_ccy));
		overdraftAccountView.put("fromPage", Utils.null2EmptyWithTrim(this
				.getParameter("fromPage")));
		if("".equals(Utils.null2EmptyWithTrim(overdraftAccountView.get("CCY_CODE_OF_AC")))){
			overdraftAccountView.put("CCY_CODE_OF_AC", from_ccy);
		}
		this.setUsrSessionDataValue("accDetail", overdraftAccountView);
		listTransHistory();

	}

	public void listLoanAccount() throws NTBException {
		BankUser bankuser = (BankUser) this.getUser();		
		String corpId = this.getParameter("corpId");
		if (corpId == null){
			corpId = (String)this.getUsrSessionDataValue("corpId");
		}

		String corpName = this.getParameter("corpName");
		if (corpName == null){
			corpName = (String)this.getUsrSessionDataValue("corpName");
		}
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser user = new CorpUser(bankuser.getUserId());
		user.setCorpId(corpId);
        user.setLanguage(lang);//add by linrui for mul-language
		// modify by hjs
		AccountEnqureBankService accountEnqureBankService = (AccountEnqureBankService) Config
				.getAppContext().getBean("AccountEnqureBankService");
		
		//add by wcc 20180203
		//keyAll閻庢稒蓱閺備線骞嶉敓鑺ョ畳闂佹鍠栭敓浠嬪触閸絾宕抽柡澶堝劤濞堟垹锟藉Δ鍐惧剨濞戞搫鎷�
		String keyAll = Utils.null2EmptyWithTrim(this.getParameter("keyAll"));
		//閻犙冾儑缁増绋夋繝浣侯伇濡炪倗鏁稿▓鎴︽煥椤旂》鎷�
		String prePage = Utils.null2EmptyWithTrim(this.getParameter("prePage"));
		//閻犙冾儑缁増绋夌�ｂ晝顏卞銈囨暩濞堟垿鏌ㄩ纭锋嫹
		String nextPage = Utils.null2EmptyWithTrim(this.getParameter("nextPage"));
		//闁告帇鍊栭弻鍥倷閻熸澘姣婂ù婊冩缁楀倹绋夐敓濮愶拷閺夆晜蓱濡插憡绋夌�ｂ晝顏卞銈忔嫹
		String flagPage = Utils.null2EmptyWithTrim(this.getParameter("flagPage"));
		// monify by wcc 20180115 濠⒀呭仜婵偤宕楅崼锝囩闁告艾楠搁崵顓㈡儍閸曨剛鍨絪tack
		Stack<String> stackPage = new Stack<String>();
		//keyPage閻犲洢鍎查弳鐔虹磼閸曨厽鏆忛柡澶堝劚閻°劑寮ㄧ欢濯媑ekey缂傚牆顭烽妴澶愭儍閸曨垱鏆涢柛濠忔嫹
		String[] keyPage = new String[]{};
		//闁告帇鍊栭弻鍥及椤栨碍鍎婇柡鍕靛灣椤戝洦绋夐敓濮愶拷
		if("".equals(keyAll)){
			stackPage.push(prePage);
		}else{
			if(keyAll.substring(keyAll.length()-1,keyAll.length()).equals(",")){
				keyAll = keyAll+" ";
			}
			keyPage = keyAll.split(",");
			
			for(int i=0;i<keyPage.length;i++){
				stackPage.push(keyPage[i]);
			}
		}
		//flagPage闁哄鍎遍崹浠嬪棘椤撶偛绔鹃柡宥呯墳缁辨繈宕欓悜妯煎灲	闁汇劌瀚弻鐔煎触閿燂拷
		if("N".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//闁绘劗鎳撻崵顔界▔鐎ｂ晝顏卞銈呯仛濡炲倿鏁嶇仦鐣屾闁哄秴鐗呴懙鎴炵▔鐎ｂ晝顏卞銈囨暩濞堟垿鏌ㄩ纭锋嫹閻犙冾儏閿熺晫绱掑▎搴ｇ憪濞戞搫鎷烽妴澶愭晬瀹�锟戒化闁告垼顔婄粭鍌涚▔閿熷锟介柡鍐啇缁辨繄浜搁崱妯煎灲濞戞搩鍘虹粭鍌涚▔閿熷锟介柣銊ュ閺侇參宕愰懝鎵偞闁稿﹪妫跨粭鍌涚▔閿熷锟�
			prePage = nextPage;
		}else if("".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//闁绘劗鎳撻崵顔界▔鐎ｂ晝顏卞銈呯仛濡炲倿鏁嶇仦鐣屾闁哄秴鐗呴懙鎴炵▔鐎ｂ晝顏卞銈囨暩濞堟垿鏌ㄩ纭锋嫹閻犙冾儏閿熺晫绱掑▎搴ｇ憪濞戞搫鎷烽妴澶愭晬瀹�锟戒化闁告垼顔婄粭鍌涚▔閿熷锟介柡鍐啇缁辨繄浜搁崱妯煎灲濞戞搩鍘虹粭鍌涚▔閿熷锟介柣銊ュ閺侇參宕愰懝鎵偞闁稿﹪妫跨粭鍌涚▔閿熷锟�
			prePage = nextPage;
		}else if("P".equals(flagPage)){
			//闁绘劗鎳撻崵顔界▔婵犱胶顏卞銈呯仛濡炲倿鏁嶇仦钘夊弗閻忓繐妫楅妵鏃�绋夐妶鍕殝闂佹鍠栭敓锟�
			stackPage.pop();
			stackPage.pop();
			//闁瑰嘲鐏濋崺宀�绱欐繝姘ワ拷闁汇劌瀚伴弫顓㈠磹閿燂拷
			prePage = stackPage.pop();
			stackPage.push(prePage);
		}
		/*List loanAccountList = accountEnqureBankService.listLoanAccount(user, user
				.getCorpId());*/
		List loanAccountList = accountEnqureBankService.listLoanAccount(user, user.getCorpId(),CibTransClient.APPCODE_LOAN_ACCOUNT,prePage);
		CorpAccountService corpAccountService = (CorpAccountService) Config
				.getAppContext().getBean("CorpAccountService");
		
		//modify by wcc 20180115 闁瑰嘲鐏濋崵锟絅EXT_KEY"闁汇劌瀚伴弫顓㈠磹閿燂拷
		nextPage = loanAccountList.get(loanAccountList.size()-1).toString();
		
		//disableN,disableP闁活枌鍔嶅鐢稿礆閵堝棙鐒藉☉鎾筹梗缁斿瓨銇勭喊澶岀濞戞挸顑勭粩瀛樸亜閸偄鐦婚梺绛嬪枟濡叉悂宕ラ敂鑺モ枖缂侊拷浜介敓锟�
		String disableN = "";
		if(!"".equals(nextPage)&&!"".equals(nextPage.trim())){
			 disableN = "button";
		}else{
			 disableN = "hidden";
		}
		String disableP = "";
		if(!"".equals(prePage)){
			disableP = "button";
		}else{
			disableP = "hidden";
		}
		//modify by wcc 20180115 remove闁瑰搫顦诲﹢鐚tCurrentPostingStmt闁哄倽顬冪涵鑸电▔椤撶儐鏉婚柛鏃傚Х濞堟唲erPage閻庣數顢婇挅锟�
		loanAccountList.remove(loanAccountList.size()-1);
		//閻忓繐妫楃欢閬嶅礆閹殿喗鐣遍柡鍌涘濞堟垿鏌ㄩ纭锋嫹闁规亽鍎冲鍐储鐎ｎ亜寮抽柡宥忔嫹
		
		//add by lzg 20190701
		List loanAccountListTemp = new ArrayList();
		List availableCurrenciesList = accountEnqureBankService.getAvailableCurrencies();
		for(int i = 0;i < loanAccountList.size(); i++){
			Map retMap = (HashMap)loanAccountList.get(i);
			for(int j = 0; j < availableCurrenciesList.size(); j++){
				Map availableCcyMap = (HashMap)availableCurrenciesList.get(j);
				if(!availableCcyMap.get("CCY_CODE").equals(retMap.get("CURRENCY_CODE"))){
					continue;
				}
				loanAccountListTemp.add(loanAccountList.get(i));
				break;
			}
		}
		loanAccountList = loanAccountListTemp;
		//add by lzg end
		
		stackPage.push(nextPage);
		//闁烩懇濞峵ringBuffer闁哄鍎茬敮鎾绩閸洘鏆涢柛濠呭亹濞堟垹锟藉Δ鍐惧剨濞戞捁顕滅槐婵囩閵夆晪鎷烽柛娆忓槻閸ㄥ酣姊鹃弮鎾剁闁告ê顭峰▍搴ㄥ嫉閿熻姤鍊靛☉鎿勬嫹闁叉粍寰勫顐ょ▏闁汇劌瀚伴敓浠嬪矗閺嬵偓鎷�
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<stackPage.size();i++){
			sb.append(stackPage.get(i)).append(",");
		}
		sb.replace(sb.length()-1, sb.length(),"");
		keyAll = sb.toString();
		// modify by hjs
		Map row = null;
		int status4Count = 0;
		for (int i = 0; i < loanAccountList.size(); i++) {
			row = (Map) loanAccountList.get(i);
			//loan閻犳劧绠戣ぐ鎸庣▔瀹ュ浠橀悷鏇氱劍閻撯剝鎯旈幘鎲嬫嫹
			/*row.put("ACCOUNT_NAME", corpAccountService.getAccountName(row.get(
					"ACCOUNT_NO").toString().trim()));*/
			if(row.get("ACCOUNT_STATUS").equals("4")){
				status4Count++;
			}
		}
		if(loanAccountList.size() == status4Count){
			loanAccountList.clear();
		}
		Map resultData = new HashMap();
		resultData.put("loanAccountList", loanAccountList);
		resultData.put("corpId", corpId);
		resultData.put("corpName", corpName);
		//modify by wcc 20180203
		resultData.put("nextPage",nextPage);
		resultData.put("prePage",prePage);
		resultData.put("disableN",disableN);
		resultData.put("disableP",disableP);
		resultData.put("keyAll",keyAll);
		setResultData(resultData);

		this.setUsrSessionDataValue("corpId", corpId);
	}

	public void viewLoanAccount() throws NTBException {
		// modify by Jet
		BankUser bankuser = (BankUser) this.getUser();		
		String corpId = (String)this.getUsrSessionDataValue("corpId");
		String corpName = (String)this.getUsrSessionDataValue("corpName");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171106
		CorpUser user = new CorpUser(bankuser.getUserId());
		user.setCorpId(corpId);
        user.setLanguage(lang);//add by linrui for mul-language
		String accountNo = this.getParameter("accountNo");

		AccountEnqureBankService accountEnqureBankService = (AccountEnqureBankService) Config
				.getAppContext().getBean("AccountEnqureBankService");

		Map currentAccountView = accountEnqureBankService.viewLoanDetail(user,
				accountNo);
		// by add mxl 0925 闁跨喐鏋婚幏绋珹ST_PAYMENT_DATE闁跨喐鏋婚幏绋琌AN_OPEN_DATE娑擄拷00000000",閸掔娀鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喕濡埥鍛
		if ("00000000".equals(currentAccountView.get("LAST_PAYMENT_DATE"))
				|| ("null".equals(currentAccountView.get("LAST_PAYMENT_DATE")))) {
			// currentAccountView.remove("LAST_PAYMENT_DATE");
			currentAccountView.put("LAST_PAYMENT_DATE", "--");

		}
		if ("00000000".equals(currentAccountView.get("LOAN_OPEN_DATE"))
				|| ("null".equals(currentAccountView.get("LAST_PAYMENT_DATE")))) {
			// currentAccountView.remove("LOAN_OPEN_DATE");
			currentAccountView.put("LOAN_OPEN_DATE", "--");
		}
		// by add mxl 0925 闁跨喐鏋婚幏绋癆YMENT_ACCOUNT娑擄拷閺冨爼鏁撻弬銈嗗閸掔娀鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚圭仦搴㈠闁跨噦鎷�
		if ("0".equals(currentAccountView.get("PAYMENT_ACCOUNT"))) {
			currentAccountView.remove("PAYMENT_ACCOUNT");
		}
		Map resultData = new HashMap();
		resultData.putAll(currentAccountView);
		resultData.put("corpId", corpId);
		resultData.put("corpName", corpName);
		//modify by wcc 20180320 闁告ê顭峰▍宥狽TEREST_RATE闁汇劌瀚弳鐔煎箲椤斿吋鍊甸梻鍫涘灩楠炴捇姊介敓锟絤od by linrui 20180329
		resultData.put("INTEREST_RATE", Utils.dropZeroAfter(currentAccountView.get("INTEREST_RATE").toString()));
		//modify by wcc 20180321
		String outstandingBalance = currentAccountView.get("PRINCIPAL_DUE").toString();
		resultData.put("PRINCIPAL_BALANCE", outstandingBalance);
		
		setResultData(resultData);
	}

	public void listTransHistory() throws NTBException {
		ApplicationContext context = Config.getAppContext();
		AccountEnqureBankService accountEnqureBankService = (AccountEnqureBankService) context
				.getBean("AccountEnqureBankService");
		// modify by Jet
		BankUser bankuser = (BankUser) this.getUser();		
		String corpId = (String)this.getUsrSessionDataValue("corpId");
		String corpName = (String)this.getUsrSessionDataValue("corpName");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = new CorpUser(bankuser.getUserId());
		corpUser.setCorpId(corpId);
		corpUser.setLanguage(lang);//add by linrui for mul-language
		
		//modify by wcc 20180123
		Map accDetail = (Map) this.getUsrSessionDataValue("accDetail");
		BigDecimal balance = (BigDecimal) accDetail.get("CURRENT_BALANCE");
		String ccy = accDetail.get("CCY_CODE_OF_AC").toString();
		String sortOrder = Utils.null2Empty(getParameter("sortOrder"));
		if("".equals(sortOrder)) sortOrder = "1";
		BigDecimal available_balace = (BigDecimal)accDetail.get("AVAILABLE_BALANCE");
		//Double a = Double.parseDouble("2141093.56");
		//BigDecimal balance = BigDecimal.valueOf(a);
		
		String dateRange = Utils.null2Empty(getParameter("date_range"));
		String range = Utils.null2Empty(getParameter("range"));
		String viewBy = Utils.null2Empty(getParameter("viewBy"));
		String remarks = Utils.null2Empty(getParameter("remark"));
		//modify by wcc 20180123
		String dateFromStr = Utils.null2Empty(getParameter("dateFrom"));
		String dateToStr = Utils.null2Empty(getParameter("dateTo"));
		Map condition = new HashMap();
		
		// monify by wcc 20180116 濠⒀呭仜婵偤宕楅崼锝囩闁告艾楠搁崵顓㈡儍閸曨剛鍨絪tack
		Stack<String> stackPage = new Stack<String>();
		//keyPage閻犲洢鍎查弳鐔虹磼閸曨厽鏆忛柡澶堝劚閻°劑寮ㄧ欢濯媑ekey缂傚牆顭烽妴澶愭儍閸曨垱鏆涢柛濠忔嫹
		String[] keyPage = new String[]{};
		//modify by wcc 20180123
		//String accountNo = Utils.prefixZero(getParameter("accountNo"), 16);
		String accountNo = Utils.null2EmptyWithTrim(getParameter("accountNo"));
		//keyAll閻庢稒蓱閺備線骞嶉敓鑺ョ畳闂佹鍠栭敓浠嬪触閸絾宕抽柡澶堝劤濞堟垹锟藉Δ鍐惧剨濞戞搫鎷�
		String keyAll = Utils.null2EmptyWithTrim(getParameter("keyAllHis"));
		//閻犙冾儑缁増绋夋繝浣侯伇濡炪倗鏁稿▓鎴︽煥椤旂》鎷�
		String prePage = Utils.null2EmptyWithTrim(getParameter("prePageHis"));
		//閻犙冾儑缁増绋夌�ｂ晝顏卞銈囨暩濞堟垿鏌ㄩ纭锋嫹
		String nextPage = Utils.null2EmptyWithTrim(getParameter("nextPageHis"));
		//闁告帇鍊栭弻鍥倷閻熸澘姣婂ù婊冩缁楀倹绋夐敓濮愶拷閺夆晜蓱濡插憡绋夌�ｂ晝顏卞銈忔嫹
		String flagPage = Utils.null2EmptyWithTrim(getParameter("flagPageHis"));
		//闁告帇鍊栭弻鍥及椤栨碍鍎婇柡鍕靛灣椤戝洦绋夐敓濮愶拷
		if("".equals(keyAll)){
			stackPage.push(prePage);
		}else{
		    if(keyAll.substring(keyAll.length()-1,keyAll.length()).equals(",")){
		    	keyAll = keyAll+" ";
		    }
		    keyPage = keyAll.split(",");
		   
		    for(int i=0;i<keyPage.length;i++){
		    	stackPage.push(keyPage[i]);
		    }
		}
		//modify by wcc 20180117
		//flagPage闁哄鍎遍崹浠嬪棘椤撶偛绔鹃柡宥呯墳缁辨繈宕欓悜妯煎灲	闁汇劌瀚弻鐔煎触閿燂拷
		if("N".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			prePage = nextPage;
		}else if("".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			prePage = nextPage;
		}else if("P".equals(flagPage)){
			stackPage.pop();
			stackPage.pop();
			prePage = stackPage.pop();
			stackPage.push(prePage);
		}
		long dateFrom = 0;
		long dateTo = 0;
		// User input Date Range
		if (dateRange.equals("date")) {
			if (!dateFromStr.equals("")) {
				dateFromStr = Format.formatDateTime(dateFromStr, defaultDatePattern,
						"yyyyMMdd");
				dateFrom = Long.parseLong(dateFromStr);
				condition.put("POSTING_DATE>", dateFrom);
			}
			if (!dateToStr.equals("")) {
				dateToStr = Format.formatDateTime(dateToStr, defaultDatePattern,
						"yyyyMMdd");
				dateTo = Long.parseLong(dateToStr);
			}
		} else {
			Calendar cal = Calendar.getInstance();
			if (range.equals("1")) {
				// modify by wcc 20180123 Current Month
				// modify by hjs 2006-10-04
				cal.set(Calendar.DAY_OF_MONTH, 1);
				dateFromStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateFrom = Long.parseLong(dateFromStr);
				dateToStr = DateTime.formatDate(new Date(), "yyyyMMdd");
				dateTo = Long.parseLong(dateToStr);
				/*dateFrom = 19900101;
				dateTo = 20200123;*/
			}  else if (range.equals("2")) {
				//modify by wcc 20180123 Previous Month
				//modified by lzg 20190621 Last 3 Months
				/*cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.add(Calendar.DATE, -1);
				dateToStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd"); 
				dateTo = Long.parseLong(dateToStr);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				dateFromStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateFrom = Long.parseLong(dateFromStr);*/
				int todayNum = cal.get(Calendar.DATE);
				dateToStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateTo = Long.parseLong(dateToStr);
				
				cal.add(Calendar.MONTH, -2);
				cal.set(Calendar.DAY_OF_MONTH, todayNum);
				dateFromStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd"); 
				dateFrom = Long.parseLong(dateFromStr);
			} else if (range.equals("3")) {
				//modify by wcc 20180123 Last 6 Months
				int todayNum = cal.get(Calendar.DATE);
				dateToStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateTo = Long.parseLong(dateToStr);
				
				cal.add(Calendar.MONTH, -5);
				cal.set(Calendar.DAY_OF_MONTH, todayNum);
				dateFromStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd"); 
				dateFrom = Long.parseLong(dateFromStr);
			}else if(range.equals("4")){
				//add by lzg 20190719
				int todayNum = cal.get(Calendar.DATE);
				dateToStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateTo = Long.parseLong(dateToStr);
				
				cal.add(Calendar.MONTH, -11);
				cal.set(Calendar.DAY_OF_MONTH, todayNum);
				dateFromStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd"); 
				dateFrom = Long.parseLong(dateFromStr);
			}else if(range.equals("5")){
				//add by lzg 20190719
				int todayNum = cal.get(Calendar.DATE);
				dateToStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateTo = Long.parseLong(dateToStr);
				
				cal.add(Calendar.MONTH, -23);
				cal.set(Calendar.DAY_OF_MONTH, todayNum);
				dateFromStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd"); 
				dateFrom = Long.parseLong(dateFromStr);
			}else if(range.equals("6")){
				//add by lzg 20190719
				int todayNum = cal.get(Calendar.DATE);
				dateToStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateTo = Long.parseLong(dateToStr);
				
				cal.add(Calendar.MONTH, -35);
				cal.set(Calendar.DAY_OF_MONTH, todayNum);
				dateFromStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd"); 
				dateFrom = Long.parseLong(dateFromStr);
			}else{
				// modify by wcc 20180123 Current Month
				cal.set(Calendar.DAY_OF_MONTH, 1);
				dateFromStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateFrom = Long.parseLong(dateFromStr);
				dateToStr = DateTime.formatDate(new Date(), "yyyyMMdd");
				dateTo = Long.parseLong(dateToStr);
				/*dateFrom = 19900101;
				dateTo = 20200123;*/
			}
			if (!dateFromStr.equals("")) {
				//modify by wcc 20180123
				// modify by hjs 2006-10-04
				dateFrom = Long.parseLong(dateFromStr);
				condition.put("POSTING_DATE>", dateFrom);
			}
			if (!dateToStr.equals("")) {
			}
		}
		condition.put("CARD_NO", accountNo);

		List transHistoryList = accountEnqureBankService
				.listTransHistoryFromDB(condition);
		//modify by wcc 20180224 闁告帇鍊栭弻鍥及椤栨碍鍎婄紒妤婂厸缁旀潙鈻庨敓鐙呮嫹闁规灚鍏檃te-->Radio,濠碘�冲�归悘澶愬及閻ョ棾ePage闂佹鍠栭敓鐣屾導閿燂拷
		String isFirst = Utils.null2EmptyWithTrim(this.getParameter("isFirst"));
		if("Y".equalsIgnoreCase(isFirst)){
			prePage="";
		}
		//modify by wcc 20180123
		// 濞戞挸锕ｇ�靛矂寮电花鏀�10/zb11
		/*List accHisFromHost = accountEnqureBankService.listTransHistory(corpUser,
				getParameter("accountNo"));*/
//		List accHisFromHost = accountEnqureBankService.listTransHistory(corpUser,accountNo, dateFrom,dateTo,prePage,ccy);
		List accHisFromHost = accountEnqureBankService.listTransHistory(corpUser,accountNo, dateFrom,dateTo,prePage,ccy, sortOrder);
		//modify by wcc 20180115 闂侇偅淇虹换鍍瞐p閺夌儐鍓氬畷鍙夌瀹稿儯cHisFromHost濞戞搩鍘界�ｄ線宕欓敓绲圚IS_NEXT_KEY"闁汇劌瀚伴弫顓㈠磹閿燂拷
		nextPage = accHisFromHost.get(accHisFromHost.size()-1).toString();
		//disableN,disableP闁活枌鍔嶅鐢稿礆閵堝棙鐒藉☉鎾筹梗缁斿瓨銇勭喊澶岀濞戞挸顑勭粩瀛樸亜閸偄鐦婚梺绛嬪枟濡叉悂宕ラ敂鑺モ枖缂侊拷浜介敓锟�
		String disableN = "";
		if(!"".equals(nextPage)&&!"".equals(nextPage.trim())){
			 disableN = "button";
		}else{
			 disableN = "hidden";
		}
		String disableP = "";
		if(!"".equals(prePage)){
			disableP = "button";
		}else{
			disableP = "hidden";
		}
		//modify by wcc 20180115 remove闁瑰搫顦诲﹢鐚tCurrentPostingStmt闁哄倽顬冪涵鑸电▔椤撶儐鏉婚柛鏃傚Х濞堟唲erPage閻庣數顢婇挅锟�
		accHisFromHost.remove(accHisFromHost.size()-1);
		//閻忓繐妫楃欢閬嶅礆閹殿喗鐣遍柡鍌涘濞堟垿鏌ㄩ纭锋嫹闁规亽鍎冲鍐储鐎ｎ亜寮抽柡宥忔嫹
		stackPage.push(nextPage);
		//闁烩懇濞峵ringBuffer闁哄鍎茬敮鎾绩閸洘鏆涢柛濠呭亹濞堟垹锟藉Δ鍐惧剨濞戞捁顕滅槐婵囩閵夆晪鎷烽柛娆忓槻閸ㄥ酣姊鹃弮鎾剁闁告ê顭峰▍搴ㄥ嫉閿熻姤鍊靛☉鎿勬嫹闁叉粍寰勫顐ょ▏闁汇劌瀚伴敓浠嬪矗閺嬵偓鎷�
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<stackPage.size();i++){
			sb.append(stackPage.get(i)).append(",");
		}
		sb.replace(sb.length()-1, sb.length(),"");
		keyAll = sb.toString();
		

		//modify by wcc 20180123 闁绘粍婢樺﹢顏堝矗椤忓啰鐟愬☉鎾剁帛濠э拷鏁嶇仦鑲╃憹闁哄被鍎查弳鐔煎箲椤旇偐姘ㄩ柨娑樻湰婢у秵绂掗妷銉фtransHistoryList缂傚喚鍠氶埞锟�
		transHistoryList = accountEnqureBankService.filter(null,
				accHisFromHost);
		// 闁跨喕绶濈拠褎瀚�, 缁涙盯锟� add by hjs 2006-09-30
		/*transHistoryList = accountEnqureBankService.filter(transHistoryList,
				accHisFromHost);*/

		// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚筨alance add by hgh
		// modify by hjs
		
		//add by lzg 20190621
		Integer currentPage = 0;
		//The first calculated balance for the current page comes from the previous page
		Map pageBalance = null;
		if("".equals(prePage)){
			currentPage = 1;
			pageBalance = new HashMap();
		}else{
			if("N".equals(flagPage)){
				currentPage = Integer.parseInt(this.getParameter("currentPage"))+1;
			}else{
				currentPage = Integer.parseInt(this.getParameter("currentPage"))-1;
			}
			pageBalance = (HashMap)this.getSession().getAttribute("pageBalance");
			balance = (BigDecimal)pageBalance.get(currentPage-1);
		}
		
		//addby lzg end
		
		
		Map txnHist = null;
		if (transHistoryList != null) {
			for (int i = 0; i < transHistoryList.size(); i++) {
				txnHist = (Map) transHistoryList.get(i);

				// add by hjs
				String key = accountNo.trim()
						+ ","
						+ txnHist.get("POST_DATE").toString().trim()
						+ ","
						+ txnHist.get("EFFECTIVE_DATE").toString().trim()
						+ ","
						+ txnHist.get("TELLER_NO").toString().trim()
						+ ","
						+ txnHist.get("TELLER_SEQ").toString().trim()
						+ ","
						+ (txnHist.containsKey("TRANS_NO") ? Utils.prefixZero(
								txnHist.get("TRANS_NO").toString().trim(), 5)
								: Utils.prefixZero(txnHist.get("RECORD_NO")
										.toString().trim(), 5));

				/*int cr_Dr_Code = Integer.parseInt(txnHist.get("DR_CR_CODE")
						.toString());*/
				//modify by wcc 2018 闁绘粍婢樺﹢顏堝箳閵夈儱缍撳☉鎿冨弨缁绘垿宕堕悙鍨暠cr_Dr_Code闁挎稑鐡�:DEBITC:CREDIT闁挎冻鎷�
				//濞寸媴绲块悥婊堝储閻斿憡闄嶉柍銉嫹闁炽儻鎷稢redit  (If DR/CR < 6, Credit)闁炽儻鎷烽柍銉嫹Debit    (If DR/CR >= 6, Debit)
				String cr_Dr_Code = txnHist.get("DR_CR_CODE").toString();
				int cr_Dr_Code_Temp = 0 ;
				if(cr_Dr_Code.equals("D")){
					cr_Dr_Code_Temp = 6;
				}else if(cr_Dr_Code.equals("C")) {
					cr_Dr_Code_Temp = 0;
				}
				//modified by lzg 20190621
				//txnHist.put("BALANCE", balance);
				if("".equals(prePage)){
					txnHist.put("BALANCE", balance);
				}
				//modified by lzg end
				// 闁跨喐鏋婚幏鐑芥晸缁辩暴bit闁跨喐鏋婚幏绋edit
				if (cr_Dr_Code_Temp < 6) {
					//modify by wcc 20180321
					balance = balance.subtract((BigDecimal) txnHist
							.get("POST_AMOUNT"));
					if(!"".equals(prePage)){
						txnHist.put("BALANCE", balance);
					}
					txnHist.put("CREDIT", txnHist.get("POST_AMOUNT"));
					txnHist.remove("POST_AMOUNT");
					// add by hjs
					txnHist.put("INWARD_KEY", key);
					txnHist.put("OUTWARD_KEY", "");
				} else {
					//modify by wcc 20180321
					balance = balance.add((BigDecimal) txnHist
							.get("POST_AMOUNT"));
					// add by hjs
					if(!"".equals(prePage)){
						txnHist.put("BALANCE", balance);
					}
					txnHist.put("INWARD_KEY", "");
					txnHist.put("OUTWARD_KEY", key);
				}
				// Jet added for excel file format 2008-05-27
				txnHist.put("DESCRIPTION_EXCEL", (String) txnHist.get("DESCRIPTION"));
				String descriptionDisplay = getTxnDescription((String) txnHist.get("DESCRIPTION"));
				String[] transactionInfoExcel = descriptionDisplay.split("<br>");
				if (transactionInfoExcel.length > 1){
					txnHist.put("TRANSACTION_INFO_EXCEL", transactionInfoExcel[1]);													
				} else {
					txnHist.put("TRANSACTION_INFO_EXCEL", "");																		
				}
				txnHist.put("DESCRIPTION", descriptionDisplay);
			}
		}

		// add by hjs 2006-09-30
		// 缁涙盯锟介柨鐔告灮閹峰嘲鏋╅柨鐔虹崵椤栥倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗閸曠喖鏁撻柧鎵�嬮幏閿嬫灘闁跨噦鎷�
		
		//add by long_zg 2014-01-08 for CR192 bob batch
		/*RcTransactionFilter txnFilter = new RcTransactionFilter();*/
		RcTransactionFilter txnFilter = new RcTransactionFilter(Config.getProperty("app.tx.filter.account"));
		
//		txnFilter.setArgs((CorpUser) getUser());
		// 闁跨喐鏋婚幏鐑芥晸閹恒儴顕滈幏鐤嚄闁跨喐鏋婚幏鐑芥晸閺傘倖瀚� ViewBy
		Map txnFilterObj = null;
		String filterType = "";
		String filterValue = "";
		if (!viewBy.equals("")) {
			txnFilterObj = txnFilter.getObject(getParameter("viewBy"));
			filterType = Utils.null2Empty(txnFilterObj.get("FILTER_TYPE"));
			filterValue = Utils.null2Empty(txnFilterObj.get("FILTER_VALUE"));
		}
		List removedList = new ArrayList();
		for (int i = 0; i < transHistoryList.size(); i++) {
			Map row = (Map) transHistoryList.get(i);

			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚筪ate from
			//modify by wcc
			if (!dateFromStr.equals("")
					&& Integer.parseInt(row.get("POST_DATE").toString()) < Integer
							.parseInt(dateFromStr)) {
				removedList.add(row);
				continue;
			}

			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚筪ate to
			if (!dateToStr.equals("")
					&& Integer.parseInt(row.get("POST_DATE").toString()) > Integer
							.parseInt(dateToStr)) {
				removedList.add(row);
				continue;
			}
			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚箆iew by
			// ViewBy闁跨喐鏋婚幏鐑芥晸缁辩祤LTER_TYPE 闁鏁撻弬銈嗗闁跨喓鐛ら銈嗗闁跨喐鏋婚幏鐑芥晸閿熺祳 = TRANS_SOURCE,N=TRANS_NATURE
			if (filterType.equals("S")) {
				if (!row.get("TRANSACTION_SOURCE").toString().equals(
						filterValue)) {
					removedList.add(row);
					continue;
				}
				// condition.put("TRANS_SOURCE",
				// txnFilterObj.get("FILTER_VALUE"));
			} else if (filterType.equals("N")) {
				if (!row.get("TRANSACTION_NATURE").toString().equals(
						filterValue)) {
					removedList.add(row);
					continue;
				}
				// condition.put("TRANS_NATURE",
				// txnFilterObj.get("FILTER_VALUE"));
			}
			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚筊emark
			if (Utils.null2EmptyWithTrim(row.get("REMARK").toString()).indexOf(
					remarks) < 0) {
				removedList.add(row);
				continue;
			}
		}
		transHistoryList.removeAll(removedList);

		// modify by hjs
		if (range.equals("1") && transHistoryList.size() >= 15) {// Last 15
																	// transactions
			// 缁涙盯锟介柨鐔告灮閹风兘鏁撻敓锟介柨鐔告灮閹风兘鏁撻弬銈嗗瑜帮拷
			transHistoryList = transHistoryList.subList(0, 15);
		}
		//add by lzg 20190621
		pageBalance.put(currentPage, balance);
		this.getSession().setAttribute("pageBalance", pageBalance);
		//add by lzg end
		Map resultData = new HashMap();
		//resultData.putAll(accDetail);
		resultData.put("date_range", Utils
				.null2Empty(getParameter("date_range")));
		resultData.put("range", Utils.null2Empty(getParameter("range")));
		resultData.put("viewBy", Utils.null2Empty(getParameter("viewBy")));
		resultData.put("remark", Utils.null2Empty(getParameter("remark")));
		resultData.put("transHistoryView", transHistoryList);
		resultData.put("corpId", corpId);
		resultData.put("corpName", corpName);
		//modify by wcc 20180115
		resultData.put("CCY_CODE_OF_AC", ccy);
		//resultData.put("CURRENT_BALANCE", balance);
		resultData.put("CURRENT_BALANCE", (BigDecimal) accDetail.get("CURRENT_BALANCE"));
		resultData.put("AVAILABLE_BALANCE", available_balace);
		resultData.put("dateFrom",DateTime.formatDate(dateFrom+"","yyyyMMdd", "dd/MM/yyyy"));
		resultData.put("dateTo",DateTime.formatDate(dateTo+"","yyyyMMdd", "dd/MM/yyyy"));
		resultData.put("ACCOUNT_NO",accountNo);
		resultData.put("nextPageHis",nextPage);
		resultData.put("prePageHis",prePage);
		resultData.put("disableN",disableN);
		resultData.put("disableP",disableP);
		resultData.put("keyAllHis",keyAll);
		//add by lzg 20190621
		resultData.put("currentPage", currentPage);
		resultData.put("sortOrder", sortOrder);
		//add by lzg end
		this.setResultData(resultData);

	}
	public void listTransHistorySa() throws NTBException {
		ApplicationContext context = Config.getAppContext();
		AccountEnqureBankService accountEnqureBankService = (AccountEnqureBankService) context
				.getBean("AccountEnqureBankService");
		// modify by Jet
		BankUser bankuser = (BankUser) this.getUser();		
		String corpId = (String)this.getUsrSessionDataValue("corpId");
		String corpName = (String)this.getUsrSessionDataValue("corpName");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = new CorpUser(bankuser.getUserId());
		corpUser.setCorpId(corpId);
		corpUser.setLanguage(lang);//add by linrui for mul-language
		
		//modify by wcc 20180123
		Map accDetail = (Map) this.getUsrSessionDataValue("accDetail");
		BigDecimal balance = (BigDecimal) accDetail.get("CURRENT_BALANCE");
		String ccy = accDetail.get("CCY_CODE_OF_AC").toString();
		String sortOrder = Utils.null2Empty(getParameter("sortOrder"));
		if("".equals(sortOrder)) sortOrder = "1";
		BigDecimal available_balace = (BigDecimal)accDetail.get("AVAILABLE_BALANCE");
		//Double a = Double.parseDouble("2141093.56");
		//BigDecimal balance = BigDecimal.valueOf(a);
		
		String dateRange = Utils.null2Empty(getParameter("date_range"));
		String range = Utils.null2Empty(getParameter("range"));
		String viewBy = Utils.null2Empty(getParameter("viewBy"));
		String remarks = Utils.null2Empty(getParameter("remark"));
		//modify by wcc 20180123
		String dateFromStr = Utils.null2Empty(getParameter("dateFrom"));
		String dateToStr = Utils.null2Empty(getParameter("dateTo"));
		Map condition = new HashMap();
		
		// monify by wcc 20180116 濠⒀呭仜婵偤宕楅崼锝囩闁告艾楠搁崵顓㈡儍閸曨剛鍨絪tack
		Stack<String> stackPage = new Stack<String>();
		//keyPage閻犲洢鍎查弳鐔虹磼閸曨厽鏆忛柡澶堝劚閻°劑寮ㄧ欢濯媑ekey缂傚牆顭烽妴澶愭儍閸曨垱鏆涢柛濠忔嫹
		String[] keyPage = new String[]{};
		//modify by wcc 20180123
		//String accountNo = Utils.prefixZero(getParameter("accountNo"), 16);
		String accountNo = Utils.null2EmptyWithTrim(getParameter("accountNo"));
		//keyAll閻庢稒蓱閺備線骞嶉敓鑺ョ畳闂佹鍠栭敓浠嬪触閸絾宕抽柡澶堝劤濞堟垹锟藉Δ鍐惧剨濞戞搫鎷�
		String keyAll = Utils.null2EmptyWithTrim(getParameter("keyAllHis"));
		//閻犙冾儑缁増绋夋繝浣侯伇濡炪倗鏁稿▓鎴︽煥椤旂》鎷�
		String prePage = Utils.null2EmptyWithTrim(getParameter("prePageHis"));
		//閻犙冾儑缁増绋夌�ｂ晝顏卞銈囨暩濞堟垿鏌ㄩ纭锋嫹
		String nextPage = Utils.null2EmptyWithTrim(getParameter("nextPageHis"));
		//闁告帇鍊栭弻鍥倷閻熸澘姣婂ù婊冩缁楀倹绋夐敓濮愶拷閺夆晜蓱濡插憡绋夌�ｂ晝顏卞銈忔嫹
		String flagPage = Utils.null2EmptyWithTrim(getParameter("flagPageHis"));
		//闁告帇鍊栭弻鍥及椤栨碍鍎婇柡鍕靛灣椤戝洦绋夐敓濮愶拷
		if("".equals(keyAll)){
			stackPage.push(prePage);
		}else{
			if(keyAll.substring(keyAll.length()-1,keyAll.length()).equals(",")){
				keyAll = keyAll+" ";
			}
			keyPage = keyAll.split(",");
			
			for(int i=0;i<keyPage.length;i++){
				stackPage.push(keyPage[i]);
			}
		}
		//modify by wcc 20180117
		//flagPage闁哄鍎遍崹浠嬪棘椤撶偛绔鹃柡宥呯墳缁辨繈宕欓悜妯煎灲	闁汇劌瀚弻鐔煎触閿燂拷
		if("N".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			prePage = nextPage;
		}else if("".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			prePage = nextPage;
		}else if("P".equals(flagPage)){
			stackPage.pop();
			stackPage.pop();
			prePage = stackPage.pop();
			stackPage.push(prePage);
		}
		long dateFrom = 0;
		long dateTo = 0;
		// User input Date Range
		if (dateRange.equals("date")) {
			if (!dateFromStr.equals("")) {
				dateFromStr = Format.formatDateTime(dateFromStr, defaultDatePattern,
						"yyyyMMdd");
				dateFrom = Long.parseLong(dateFromStr);
				condition.put("POSTING_DATE>", dateFrom);
			}
			if (!dateToStr.equals("")) {
				dateToStr = Format.formatDateTime(dateToStr, defaultDatePattern,
						"yyyyMMdd");
				dateTo = Long.parseLong(dateToStr);
			}
		} else {
			Calendar cal = Calendar.getInstance();
			if (range.equals("1")) {
				// modify by wcc 20180123 Current Month
				// modify by hjs 2006-10-04
				cal.set(Calendar.DAY_OF_MONTH, 1);
				dateFromStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateFrom = Long.parseLong(dateFromStr);
				dateToStr = DateTime.formatDate(new Date(), "yyyyMMdd");
				dateTo = Long.parseLong(dateToStr);
				/*dateFrom = 19900101;
				dateTo = 20200123;*/
			}  else if (range.equals("2")) {
				//modify by wcc 20180123 Previous Month
				//modified by lzg 20190621 Last 3 Months
				/*cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.add(Calendar.DATE, -1);
				dateToStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd"); 
				dateTo = Long.parseLong(dateToStr);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				dateFromStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateFrom = Long.parseLong(dateFromStr);*/
				int todayNum = cal.get(Calendar.DATE);
				dateToStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateTo = Long.parseLong(dateToStr);
				
				cal.add(Calendar.MONTH, -2);
				cal.set(Calendar.DAY_OF_MONTH, todayNum);
				dateFromStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd"); 
				dateFrom = Long.parseLong(dateFromStr);
			} else if (range.equals("3")) {
				//modify by wcc 20180123 Last 6 Months
				int todayNum = cal.get(Calendar.DATE);
				dateToStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateTo = Long.parseLong(dateToStr);
				
				cal.add(Calendar.MONTH, -5);
				cal.set(Calendar.DAY_OF_MONTH, todayNum);
				dateFromStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd"); 
				dateFrom = Long.parseLong(dateFromStr);
			}else if(range.equals("4")){
				//add by lzg 20190719
				int todayNum = cal.get(Calendar.DATE);
				dateToStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateTo = Long.parseLong(dateToStr);
				
				cal.add(Calendar.MONTH, -11);
				cal.set(Calendar.DAY_OF_MONTH, todayNum);
				dateFromStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd"); 
				dateFrom = Long.parseLong(dateFromStr);
			}else if(range.equals("5")){
				//add by lzg 20190719
				int todayNum = cal.get(Calendar.DATE);
				dateToStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateTo = Long.parseLong(dateToStr);
				
				cal.add(Calendar.MONTH, -23);
				cal.set(Calendar.DAY_OF_MONTH, todayNum);
				dateFromStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd"); 
				dateFrom = Long.parseLong(dateFromStr);
			}else if(range.equals("6")){
				//add by lzg 20190719
				int todayNum = cal.get(Calendar.DATE);
				dateToStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateTo = Long.parseLong(dateToStr);
				
				cal.add(Calendar.MONTH, -35);
				cal.set(Calendar.DAY_OF_MONTH, todayNum);
				dateFromStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd"); 
				dateFrom = Long.parseLong(dateFromStr);
			}else{
				// modify by wcc 20180123 Current Month
				cal.set(Calendar.DAY_OF_MONTH, 1);
				dateFromStr = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateFrom = Long.parseLong(dateFromStr);
				dateToStr = DateTime.formatDate(new Date(), "yyyyMMdd");
				dateTo = Long.parseLong(dateToStr);
				/*dateFrom = 19900101;
				dateTo = 20200123;*/
			}
			if (!dateFromStr.equals("")) {
				//modify by wcc 20180123
				// modify by hjs 2006-10-04
				dateFrom = Long.parseLong(dateFromStr);
				condition.put("POSTING_DATE>", dateFrom);
			}
			if (!dateToStr.equals("")) {
			}
		}
		condition.put("CARD_NO", accountNo);
		
		List transHistoryList = accountEnqureBankService
				.listTransHistoryFromDB(condition);
		//modify by wcc 20180224 闁告帇鍊栭弻鍥及椤栨碍鍎婄紒妤婂厸缁旀潙鈻庨敓鐙呮嫹闁规灚鍏檃te-->Radio,濠碘�冲�归悘澶愬及閻ョ棾ePage闂佹鍠栭敓鐣屾導閿燂拷
		String isFirst = Utils.null2EmptyWithTrim(this.getParameter("isFirst"));
		if("Y".equalsIgnoreCase(isFirst)){
			prePage="";
		}
		//modify by wcc 20180123
		// 濞戞挸锕ｇ�靛矂寮电花鏀�10/zb11
		/*List accHisFromHost = accountEnqureBankService.listTransHistory(corpUser,
				getParameter("accountNo"));*/
//		List accHisFromHost = accountEnqureBankService.listTransHistory(corpUser,accountNo, dateFrom,dateTo,prePage,ccy);
		List accHisFromHost = accountEnqureBankService.listTransHistory(corpUser,accountNo, dateFrom,dateTo,prePage,ccy, sortOrder);
		//modify by wcc 20180115 闂侇偅淇虹换鍍瞐p閺夌儐鍓氬畷鍙夌瀹稿儯cHisFromHost濞戞搩鍘界�ｄ線宕欓敓绲圚IS_NEXT_KEY"闁汇劌瀚伴弫顓㈠磹閿燂拷
		nextPage = accHisFromHost.get(accHisFromHost.size()-1).toString();
		//disableN,disableP闁活枌鍔嶅鐢稿礆閵堝棙鐒藉☉鎾筹梗缁斿瓨銇勭喊澶岀濞戞挸顑勭粩瀛樸亜閸偄鐦婚梺绛嬪枟濡叉悂宕ラ敂鑺モ枖缂侊拷浜介敓锟�
		String disableN = "";
		if(!"".equals(nextPage)&&!"".equals(nextPage.trim())){
			disableN = "button";
		}else{
			disableN = "hidden";
		}
		String disableP = "";
		if(!"".equals(prePage)){
			disableP = "button";
		}else{
			disableP = "hidden";
		}
		//modify by wcc 20180115 remove闁瑰搫顦诲﹢鐚tCurrentPostingStmt闁哄倽顬冪涵鑸电▔椤撶儐鏉婚柛鏃傚Х濞堟唲erPage閻庣數顢婇挅锟�
		accHisFromHost.remove(accHisFromHost.size()-1);
		//閻忓繐妫楃欢閬嶅礆閹殿喗鐣遍柡鍌涘濞堟垿鏌ㄩ纭锋嫹闁规亽鍎冲鍐储鐎ｎ亜寮抽柡宥忔嫹
		stackPage.push(nextPage);
		//闁烩懇濞峵ringBuffer闁哄鍎茬敮鎾绩閸洘鏆涢柛濠呭亹濞堟垹锟藉Δ鍐惧剨濞戞捁顕滅槐婵囩閵夆晪鎷烽柛娆忓槻閸ㄥ酣姊鹃弮鎾剁闁告ê顭峰▍搴ㄥ嫉閿熻姤鍊靛☉鎿勬嫹闁叉粍寰勫顐ょ▏闁汇劌瀚伴敓浠嬪矗閺嬵偓鎷�
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<stackPage.size();i++){
			sb.append(stackPage.get(i)).append(",");
		}
		sb.replace(sb.length()-1, sb.length(),"");
		keyAll = sb.toString();
		
		
		//modify by wcc 20180123 闁绘粍婢樺﹢顏堝矗椤忓啰鐟愬☉鎾剁帛濠э拷鏁嶇仦鑲╃憹闁哄被鍎查弳鐔煎箲椤旇偐姘ㄩ柨娑樻湰婢у秵绂掗妷銉фtransHistoryList缂傚喚鍠氶埞锟�
		transHistoryList = accountEnqureBankService.filter(null,
				accHisFromHost);
		// 闁跨喕绶濈拠褎瀚�, 缁涙盯锟� add by hjs 2006-09-30
		/*transHistoryList = accountEnqureBankService.filter(transHistoryList,
				accHisFromHost);*/
		
		// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚筨alance add by hgh
		// modify by hjs
		
		//add by lzg 20190621
		Integer currentPage = 0;
		//The first calculated balance for the current page comes from the previous page
		Map pageBalance = null;
		if("".equals(prePage)){
			currentPage = 1;
			pageBalance = new HashMap();
		}else{
			if("N".equals(flagPage)){
				currentPage = Integer.parseInt(this.getParameter("currentPage"))+1;
			}else{
				currentPage = Integer.parseInt(this.getParameter("currentPage"))-1;
			}
			pageBalance = (HashMap)this.getSession().getAttribute("pageBalance");
			balance = (BigDecimal)pageBalance.get(currentPage-1);
		}
		
		//addby lzg end
		
		
		Map txnHist = null;
		if (transHistoryList != null) {
			for (int i = 0; i < transHistoryList.size(); i++) {
				txnHist = (Map) transHistoryList.get(i);
				
				// add by hjs
				String key = accountNo.trim()
						+ ","
						+ txnHist.get("POST_DATE").toString().trim()
						+ ","
						+ txnHist.get("EFFECTIVE_DATE").toString().trim()
						+ ","
						+ txnHist.get("TELLER_NO").toString().trim()
						+ ","
						+ txnHist.get("TELLER_SEQ").toString().trim()
						+ ","
						+ (txnHist.containsKey("TRANS_NO") ? Utils.prefixZero(
								txnHist.get("TRANS_NO").toString().trim(), 5)
								: Utils.prefixZero(txnHist.get("RECORD_NO")
										.toString().trim(), 5));
				
				/*int cr_Dr_Code = Integer.parseInt(txnHist.get("DR_CR_CODE")
						.toString());*/
				//modify by wcc 2018 闁绘粍婢樺﹢顏堝箳閵夈儱缍撳☉鎿冨弨缁绘垿宕堕悙鍨暠cr_Dr_Code闁挎稑鐡�:DEBITC:CREDIT闁挎冻鎷�
				//濞寸媴绲块悥婊堝储閻斿憡闄嶉柍銉嫹闁炽儻鎷稢redit  (If DR/CR < 6, Credit)闁炽儻鎷烽柍銉嫹Debit    (If DR/CR >= 6, Debit)
				String cr_Dr_Code = txnHist.get("DR_CR_CODE").toString();
				int cr_Dr_Code_Temp = 0 ;
				if(cr_Dr_Code.equals("D")){
					cr_Dr_Code_Temp = 6;
				}else if(cr_Dr_Code.equals("C")) {
					cr_Dr_Code_Temp = 0;
				}
				//modified by lzg 20190621
				//txnHist.put("BALANCE", balance);
				if("".equals(prePage)){
					txnHist.put("BALANCE", balance);
				}
				//modified by lzg end
				// 闁跨喐鏋婚幏鐑芥晸缁辩暴bit闁跨喐鏋婚幏绋edit
				if (cr_Dr_Code_Temp < 6) {
					//modify by wcc 20180321
					balance = balance.subtract((BigDecimal) txnHist
							.get("POST_AMOUNT"));
					if(!"".equals(prePage)){
						txnHist.put("BALANCE", balance);
					}
					txnHist.put("CREDIT", txnHist.get("POST_AMOUNT"));
					txnHist.remove("POST_AMOUNT");
					// add by hjs
					txnHist.put("INWARD_KEY", key);
					txnHist.put("OUTWARD_KEY", "");
				} else {
					//modify by wcc 20180321
					balance = balance.add((BigDecimal) txnHist
							.get("POST_AMOUNT"));
					// add by hjs
					if(!"".equals(prePage)){
						txnHist.put("BALANCE", balance);
					}
					txnHist.put("INWARD_KEY", "");
					txnHist.put("OUTWARD_KEY", key);
				}
				// Jet added for excel file format 2008-05-27
				txnHist.put("DESCRIPTION_EXCEL", (String) txnHist.get("DESCRIPTION"));
				String descriptionDisplay = getTxnDescription((String) txnHist.get("DESCRIPTION"));
				String[] transactionInfoExcel = descriptionDisplay.split("<br>");
				if (transactionInfoExcel.length > 1){
					txnHist.put("TRANSACTION_INFO_EXCEL", transactionInfoExcel[1]);													
				} else {
					txnHist.put("TRANSACTION_INFO_EXCEL", "");																		
				}
				txnHist.put("DESCRIPTION", descriptionDisplay);
			}
		}
		
		// add by hjs 2006-09-30
		// 缁涙盯锟介柨鐔告灮閹峰嘲鏋╅柨鐔虹崵椤栥倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗閸曠喖鏁撻柧鎵�嬮幏閿嬫灘闁跨噦鎷�
		
		//add by long_zg 2014-01-08 for CR192 bob batch
		/*RcTransactionFilter txnFilter = new RcTransactionFilter();*/
		RcTransactionFilter txnFilter = new RcTransactionFilter(Config.getProperty("app.tx.filter.account"));
		
//		txnFilter.setArgs((CorpUser) getUser());
		// 闁跨喐鏋婚幏鐑芥晸閹恒儴顕滈幏鐤嚄闁跨喐鏋婚幏鐑芥晸閺傘倖瀚� ViewBy
		Map txnFilterObj = null;
		String filterType = "";
		String filterValue = "";
		if (!viewBy.equals("")) {
			txnFilterObj = txnFilter.getObject(getParameter("viewBy"));
			filterType = Utils.null2Empty(txnFilterObj.get("FILTER_TYPE"));
			filterValue = Utils.null2Empty(txnFilterObj.get("FILTER_VALUE"));
		}
		List removedList = new ArrayList();
		for (int i = 0; i < transHistoryList.size(); i++) {
			Map row = (Map) transHistoryList.get(i);
			
			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚筪ate from
			//modify by wcc
			if (!dateFromStr.equals("")
					&& Integer.parseInt(row.get("POST_DATE").toString()) < Integer
					.parseInt(dateFromStr)) {
				removedList.add(row);
				continue;
			}
			
			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚筪ate to
			if (!dateToStr.equals("")
					&& Integer.parseInt(row.get("POST_DATE").toString()) > Integer
					.parseInt(dateToStr)) {
				removedList.add(row);
				continue;
			}
			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚箆iew by
			// ViewBy闁跨喐鏋婚幏鐑芥晸缁辩祤LTER_TYPE 闁鏁撻弬銈嗗闁跨喓鐛ら銈嗗闁跨喐鏋婚幏鐑芥晸閿熺祳 = TRANS_SOURCE,N=TRANS_NATURE
			if (filterType.equals("S")) {
				if (!row.get("TRANSACTION_SOURCE").toString().equals(
						filterValue)) {
					removedList.add(row);
					continue;
				}
				// condition.put("TRANS_SOURCE",
				// txnFilterObj.get("FILTER_VALUE"));
			} else if (filterType.equals("N")) {
				if (!row.get("TRANSACTION_NATURE").toString().equals(
						filterValue)) {
					removedList.add(row);
					continue;
				}
				// condition.put("TRANS_NATURE",
				// txnFilterObj.get("FILTER_VALUE"));
			}
			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚筊emark
			if (Utils.null2EmptyWithTrim(row.get("REMARK").toString()).indexOf(
					remarks) < 0) {
				removedList.add(row);
				continue;
			}
		}
		transHistoryList.removeAll(removedList);
		
		// modify by hjs
		if (range.equals("1") && transHistoryList.size() >= 15) {// Last 15
			// transactions
			// 缁涙盯锟介柨鐔告灮閹风兘鏁撻敓锟介柨鐔告灮閹风兘鏁撻弬銈嗗瑜帮拷
			transHistoryList = transHistoryList.subList(0, 15);
		}
		//add by lzg 20190621
		pageBalance.put(currentPage, balance);
		this.getSession().setAttribute("pageBalance", pageBalance);
		//add by lzg end
		Map resultData = new HashMap();
		//resultData.putAll(accDetail);
		resultData.put("date_range", Utils
				.null2Empty(getParameter("date_range")));
		resultData.put("range", Utils.null2Empty(getParameter("range")));
		resultData.put("viewBy", Utils.null2Empty(getParameter("viewBy")));
		resultData.put("remark", Utils.null2Empty(getParameter("remark")));
		resultData.put("transHistoryView", transHistoryList);
		resultData.put("corpId", corpId);
		resultData.put("corpName", corpName);
		//modify by wcc 20180115
		resultData.put("CCY_CODE_OF_AC", ccy);
		//resultData.put("CURRENT_BALANCE", balance);
		resultData.put("CURRENT_BALANCE", (BigDecimal) accDetail.get("CURRENT_BALANCE"));
		resultData.put("AVAILABLE_BALANCE", available_balace);
		resultData.put("dateFrom",DateTime.formatDate(dateFrom+"","yyyyMMdd", "dd/MM/yyyy"));
		resultData.put("dateTo",DateTime.formatDate(dateTo+"","yyyyMMdd", "dd/MM/yyyy"));
		resultData.put("ACCOUNT_NO",accountNo);
		resultData.put("nextPageHis",nextPage);
		resultData.put("prePageHis",prePage);
		resultData.put("disableN",disableN);
		resultData.put("disableP",disableP);
		resultData.put("keyAllHis",keyAll);
		//add by lzg 20190621
		resultData.put("currentPage", currentPage);
		resultData.put("sortOrder", sortOrder);
		//add by lzg end
		this.setResultData(resultData);
		
	}

	/**
	 * add by Huang_gh 2006-8-1 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚规潪顒勬晸閺傘倖瀚筎ransaction Description 闁跨喐鏋婚幏椋庛仛闁跨喍鑼庨棃鈺傚瀵繋璐熼柨鐔告灮閹风兘鏁撻弬銈嗗:
	 * 闁跨喐鏋婚幏铚傜闁跨喐鏋婚幏铚傝礋闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏锟�4闁跨喐鏋婚幏绋璦pString 闁跨喕濡拋瑙勫闁跨喎褰ㄧ拠褎瀚圭拠顢簉ansaction description table闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹凤拷闁跨喓鐓拠銉﹀敾閹风兘鏁撶徊顦損ping
	 * string闁跨喐鏋婚幏宄板爱闁跨喐鏋婚幏绋祌ansaction Description闁跨喐鏋婚幏宄般仈12 bytes闁跨喐鏋婚幏锟絙ytes, 3bytes
	 * 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归崠褰掓晸閺傘倖瀚规径锟�2闁跨喐鏋婚幏绋tes,閸栧綊鏁撻幋鎺嶇瑝闁跨喕绶濋敐蹇斿闁跨喐鏋婚幏宄板爱闁跨喐鏋婚幏锟介柨鐔告灮閹风āytes闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喓瀚涢敐蹇斿闁跨喐鏋婚幏鐑芥晸閻櫬板劵閹烽攱闂导娆愬鐢矂鏁撴潪娆句悍閹烽婀柨鐕傛嫹
	 * 
	 */
	private String getTxnDescription(String mapString) {
		RcTransactionDescription txnDesc = new RcTransactionDescription();
		StringBuffer temp = new StringBuffer(mapString).append("<br>");
		try {
			String mapStr1 = mapString.substring(0, 12);
			String mapStr2 = mapString.substring(0, 7);
			String mapStr3 = mapString.substring(0, 3);
			Map DescObj = txnDesc.getObject(mapStr1);
			if (DescObj != null) {
				return temp.append(DescObj.get("TRANS_DESCRIPTION")).toString();
			}
			DescObj = txnDesc.getObject(mapStr2);
			if (DescObj != null) {
				return temp.append(DescObj.get("TRANS_DESCRIPTION")).toString();
			}
			DescObj = txnDesc.getObject(mapStr3);
			if (DescObj != null) {
				return temp.append(DescObj.get("TRANS_DESCRIPTION")).toString();
			}

		} catch (Exception e) {
			return mapString;
		}
		return mapString;
	}

	public void viewInwardInfo() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		AccountEnqureService accEnqervice = (AccountEnqureService) appContext
				.getBean("AccountEnqureService");

		String key = this.getParameter("key").trim();
		Map row = accEnqervice.viewInwardInfo(key);

		Map resultData = new HashMap();
		resultData.putAll(row);
		this.setResultData(resultData);
	}
	
	public void listCreditAccount() throws NTBException {

		BankUser bankUser = (BankUser) this.getUser();
		String corpId = this.getParameter("corpId");
		if (corpId == null){
			corpId = (String)this.getUsrSessionDataValue("corpId");
		}
		
		String corpName = this.getParameter("corpName");
		if (corpName == null){
			corpName = (String)this.getUsrSessionDataValue("corpName");
		}
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = new CorpUser(bankUser.getUserId());
		corpUser.setCorpId(corpId);
		corpUser.setLanguage(lang);//add by linrui for mul-language
		AccountEnqureBankService accountEnqureBankService = (AccountEnqureBankService) Config
				.getAppContext().getBean("AccountEnqureBankService");
		List creditAccountList = accountEnqureBankService.listCreditCard(corpUser, corpUser.getCorpId()) ;
		
		
		Map resultData = new HashMap();
		resultData.put("creditAccountList", creditAccountList);
		
		resultData.put("corpId", corpId);
		resultData.put("corpName", corpName);	
		setResultData(resultData);
	}
	
	public void viewCreditAccount() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService accService = (CorpAccountService) appContext
				.getBean("CorpAccountService");

		BankUser bankUser = (BankUser) this.getUser();
		String corpId = this.getParameter("corpId");
		if (corpId == null){
			corpId = (String)this.getUsrSessionDataValue("corpId");
		}
		
		String corpName = this.getParameter("corpName");
		if (corpName == null){
			corpName = (String)this.getUsrSessionDataValue("corpName");
		}
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = new CorpUser(bankUser.getUserId());
		corpUser.setCorpId(corpId);
		corpUser.setLanguage(lang);//add by linrui for mul-language
		String accountNo = this.getParameter("accountNo");
		Log.info("bank viewCreditAccount accountNo="+accountNo);
		AccountEnqureBankService accountEnqureBankService = (AccountEnqureBankService) Config
				.getAppContext().getBean("AccountEnqureBankService");
		Map currentAccountView = accountEnqureBankService.viewCreditDetail(corpUser, accountNo) ;
		currentAccountView.put("ACCOUNT_TYPE", accService
				.getAccountType(accountNo));
		currentAccountView.put("fromPage", Utils.null2EmptyWithTrim(this
				.getParameter("fromPage")));
		
		// add by Li_zd 20160926
		DBRCFactory rc = CachedDBRCFactory.getInstance("statementForDetail");
		rc.setArgs(corpUser);
		String statementForDetail = rc.getString(accountNo);
		String str = "option";
		Log.info(statementForDetail);
		currentAccountView.put("showStatement",
				StringUtils.countMatches(statementForDetail, str)>2?"show":"notshow");
		
		//add by kevin 20131120
		currentAccountView.put("callMethod", "viewCreditAccount");
		
		this.setUsrSessionDataValue("accDetail", currentAccountView);
//		listTransHistory();
		listCreditTransHistory();
	}
	
	public void listCreditTransHistory() throws NTBException {
		ApplicationContext context = Config.getAppContext();
		AccountEnqureBankService accountEnqureBankService = (AccountEnqureBankService) context
				.getBean("AccountEnqureBankService");

		BankUser bankUser = (BankUser) this.getUser();
		String corpId = this.getParameter("corpId");
		if (corpId == null){
			corpId = (String)this.getUsrSessionDataValue("corpId");
		}
		
		String corpName = this.getParameter("corpName");
		if (corpName == null){
			corpName = (String)this.getUsrSessionDataValue("corpName");
		}
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser corpUser = new CorpUser(bankUser.getUserId());
		corpUser.setCorpId(corpId);
		corpUser.setLanguage(lang);//add by linrui for mul-language
		Map accDetail = (Map) this.getUsrSessionDataValue("accDetail");
//		BigDecimal balance = (BigDecimal) accDetail.get("CARD_BALANCE");
		String dateRange = Utils.null2Empty(getParameter("date_range"));
		String range = Utils.null2Empty(getParameter("range"));
		String dateFrom = Utils.null2Empty(getParameter("dateFrom"));
		String dateTo = Utils.null2Empty(getParameter("dateTo"));
		String viewBy = Utils.null2Empty(getParameter("viewBy"));
		String remarks = Utils.null2Empty(getParameter("remark"));
		String accountNo = Utils.prefixZero(getParameter("accountNo"), 16);
		Map condition = new HashMap();
		if (dateRange.equals("date")) {// User input Date Range
			if (!dateFrom.equals("")) {
				dateFrom = Format.formatDateTime(dateFrom, defaultDatePattern,
						"yyyyMMdd");
				condition.put("POSTING_DATE>", dateFrom);
			}
			if (!dateTo.equals("")) {
				dateTo = Format.formatDateTime(dateTo, defaultDatePattern,
						"yyyyMMdd");
			}
		} else {
			Calendar cal = Calendar.getInstance();
			if (range.equals("1")) {
				//modify by hjs 2006-10-04
				dateTo = DateTime.formatDate(new Date(), "yyyyMMdd");
			} else if (range.equals("2")) {// Current Month
				// modify by hjs 2006-10-04
				cal.set(Calendar.DAY_OF_MONTH, 1);
				dateFrom = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				dateTo = DateTime.formatDate(new Date(), "yyyyMMdd");
			} else if (range.equals("3")) {// Previous Month
				// cal.add(Calendar.MONTH, -1);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.add(Calendar.DATE, -1);
				//modify by hjs 2006-10-04
				dateTo = DateTime.formatDate(cal.getTime(), "yyyyMMdd"); 
				cal.set(Calendar.DAY_OF_MONTH, 1);
				//modify by hjs 2006-10-04
				dateFrom = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
			} else if (range.equals("4")) {// Last 6 Months
				cal.add(Calendar.MONTH, -6);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				//modify by hjs 2006-10-04
				dateFrom = DateTime.formatDate(cal.getTime(), "yyyyMMdd");
				//modify by hjs 2006-10-04
				dateTo = DateTime.formatDate(new Date(), "yyyyMMdd");
			}
			if (!dateFrom.equals("")) {
				// modify by hjs 2006-10-04
				condition.put("POSTING_DATE>", dateFrom);
			}
			if (!dateTo.equals("")) {
			}
		}
		condition.put("CARD_NO", accountNo);

		List transHistoryList = accountEnqureBankService
				.listTransHistoryFromDB(condition);

		// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹凤拷
		List accHisFromHost = accountEnqureBankService.listCreditTransHistory(corpUser,
				getParameter("accountNo"));

		// 闁跨喕绶濈拠褎瀚�, 缁涙盯锟� add by hjs 2006-09-30
		transHistoryList = accountEnqureBankService.filterCredit(transHistoryList,
				accHisFromHost);

		// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚筨alance add by hgh
		// modify by hjs
		Map txnHist = null;
		if (transHistoryList != null) {
			for (int i = 0; i < transHistoryList.size(); i++) {
				txnHist = (Map) transHistoryList.get(i);

				// add by hjs
				String key = accountNo.trim()
						+ ","
						+ txnHist.get("POST_DATE").toString().trim()
						+ ","
						+ txnHist.get("EFFECTIVE_DATE").toString().trim()
						+ ","
						+ txnHist.get("TELLER_NO").toString().trim()
						+ ","
						+ txnHist.get("TELLER_SEQ").toString().trim()
						+ ","
						+ (txnHist.containsKey("TRANS_NO") ? Utils.prefixZero(
								txnHist.get("TRANS_NO").toString().trim(), 5)
								: Utils.prefixZero(txnHist.get("RECORD_NO")
										.toString().trim(), 5));

				String cr_Dr_Code = txnHist.get("DR_CR_CODE").toString();
				//txnHist.put("BALANCE", balance);
				// 闁跨喐鏋婚幏鐑芥晸缁辩暴bit闁跨喐鏋婚幏绋edit
				if (cr_Dr_Code.equals("C")) {
//					balance = balance.subtract((BigDecimal) txnHist
//							.get("POST_AMOUNT"));
					txnHist.put("CREDIT", txnHist.get("POST_AMOUNT"));
					//txnHist.remove("POST_AMOUNT");
					// add by hjs
					txnHist.put("INWARD_KEY", key);
					txnHist.put("OUTWARD_KEY", "");
				} else {
//					BigDecimal kk = (BigDecimal)txnHist.get("POST_AMOUNT") ;
//					balance = balance.add(kk);
					// add by hjs
					txnHist.put("INWARD_KEY", "");
					txnHist.put("OUTWARD_KEY", key);
				}
				// Jet added for excel file format 2008-05-27
				txnHist.put("DESCRIPTION_EXCEL", (String) txnHist.get("DESCRIPTION"));
				String descriptionDisplay = getTxnDescription((String) txnHist.get("DESCRIPTION"));
				String[] transactionInfoExcel = descriptionDisplay.split("<br>");
				if (transactionInfoExcel.length > 1){
					txnHist.put("TRANSACTION_INFO_EXCEL", transactionInfoExcel[1]);													
				} else {
					txnHist.put("TRANSACTION_INFO_EXCEL", "");																		
				}
				txnHist.put("DESCRIPTION", descriptionDisplay);
			}
		}

		// add by hjs 2006-09-30
		// 缁涙盯锟介柨鐔告灮閹峰嘲鏋╅柨鐔虹崵椤栥倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗閸曠喖鏁撻柧鎵�嬮幏閿嬫灘闁跨噦鎷�

		//add by long_zg 2014-01-08 for CR192 bob batch
		/*RcTransactionFilter txnFilter = new RcTransactionFilter();*/
		RcTransactionFilter txnFilter = new RcTransactionFilter(Config.getProperty("app.tx.filter.account"));
		
		
		txnFilter.setArgs(corpUser);
		// 闁跨喐鏋婚幏鐑芥晸閹恒儴顕滈幏鐤嚄闁跨喐鏋婚幏鐑芥晸閺傘倖瀚� ViewBy
		Map txnFilterObj = null;
		String filterType = "";
		String filterValue = "";
		if (!viewBy.equals("")) {
			txnFilterObj = txnFilter.getObject(getParameter("viewBy"));
			filterType = Utils.null2Empty(txnFilterObj.get("FILTER_TYPE"));
			filterValue = Utils.null2Empty(txnFilterObj.get("FILTER_VALUE"));
		}
		List removedList = new ArrayList();
		for (int i = 0; i < transHistoryList.size(); i++) {
			Map row = (Map) transHistoryList.get(i);

			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚筪ate from
			if (!dateFrom.equals("")
					&& Integer.parseInt(row.get("POST_DATE").toString()) < Integer
							.parseInt(dateFrom)) {
				removedList.add(row);
				continue;
			}

			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚筪ate to
			if (!"".equals(dateTo)
					&& Integer.parseInt(row.get("POST_DATE").toString()) > Integer
							.parseInt(dateTo)) {
				removedList.add(row);
				continue;
			}
			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚箆iew by
			// ViewBy闁跨喐鏋婚幏鐑芥晸缁辩祤LTER_TYPE 闁鏁撻弬銈嗗闁跨喓鐛ら銈嗗闁跨喐鏋婚幏鐑芥晸閿熺祳 = TRANS_SOURCE,N=TRANS_NATURE
			if ("S".equals(filterType)) {
				if (!filterValue.equals(
						row.get("TRANSACTION_SOURCE").toString())) {
					removedList.add(row);
					continue;
				}
				// condition.put("TRANS_SOURCE",
				// txnFilterObj.get("FILTER_VALUE"));
			} else if ("N".equals(filterType)) {
				if (!filterValue.equals(
						row.get("TRANSACTION_NATURE").toString())) {
					removedList.add(row);
					continue;
				}
				// condition.put("TRANS_NATURE",
				// txnFilterObj.get("FILTER_VALUE"));
			}
			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚筊emark
			if (Utils.null2EmptyWithTrim(row.get("REMARK").toString()).indexOf(
					remarks) < 0) {
				removedList.add(row);
				continue;
			}
		}
		transHistoryList.removeAll(removedList);

		// modify by hjs
		if (range.equals("1") && transHistoryList.size() >= 15) {// Last 15
																	// transactions
			// 缁涙盯锟介柨鐔告灮閹风兘鏁撻敓锟介柨鐔告灮閹风兘鏁撻弬銈嗗瑜帮拷
			transHistoryList = transHistoryList.subList(0, 15);
		}

		Map resultData = new HashMap();
		resultData.putAll(accDetail);
		resultData.put("date_range", Utils
				.null2Empty(getParameter("date_range")));
		resultData.put("range", Utils.null2Empty(getParameter("range")));
		resultData.put("dateFrom", Utils.null2Empty(getParameter("dateFrom")));
		resultData.put("dateTo", Utils.null2Empty(getParameter("dateTo")));
		resultData.put("viewBy", Utils.null2Empty(getParameter("viewBy")));
		resultData.put("remark", Utils.null2Empty(getParameter("remark")));
		resultData.put("transHistoryView", transHistoryList);
		this.setResultData(resultData);

	}
	
	/**
	 * add by long_zg 2014-12-19 for CR192 bob batch
	 * @return
	 * @throws NTBException
	 */
	public List creditAccountList(CorpUser user,String corpId) throws NTBException {
		AccountEnqureBankService accountEnqureBankService = (AccountEnqureBankService) Config
		.getAppContext().getBean("AccountEnqureBankService");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		user.setLanguage(lang);//add by linrui for mul-language
		List visaCreditAccountList = accountEnqureBankService.listSummaryByAccType(user,corpId, CorpAccount.ACCOUNT_TYPE_CREDIT,CibTransClient.APPCODE_CREDIT_VISA);
		List aeCreditAccountList = accountEnqureBankService.listSummaryByAccType(user,corpId, CorpAccount.ACCOUNT_TYPE_CREDIT,CibTransClient.APPCODE_CREDIT_AE);
		List masterCreditAccountList = accountEnqureBankService.listSummaryByAccType(user,corpId, CorpAccount.ACCOUNT_TYPE_CREDIT,CibTransClient.APPCODE_CREDIT_MASTER);
		List utCreditAccountList = accountEnqureBankService.listSummaryByAccType(user,corpId, CorpAccount.ACCOUNT_TYPE_CREDIT,CibTransClient.APPCODE_CREDIT_UT);

		List creditList = new ArrayList() ;
		creditList = this.unionList(creditList, visaCreditAccountList) ;
		creditList = this.unionList(creditList, aeCreditAccountList) ;
		creditList = this.unionList(creditList, masterCreditAccountList) ;
		creditList = this.unionList(creditList, utCreditAccountList) ;
		return creditList ;
	}
	
	public List unionList(List list1,List list2) {
		if(null==list2 || list2.size()==0){
			return list1 ;
		}
		if(null==list1 || list1.size()==0){
			return list2 ;
		}
		Log.info("list1="+list1);
		Log.info("list2="+list2);
		for(int i=0;i<list1.size();i++){
			Map map1 = (Map)list1.get(i) ;
			String currencyCode1 = (String)map1.get("CURRENCY_CODE") ;
			int k = 0;
			int length = list2.size();
			for(int j=0;j<length;j++){
				Map map2 = (Map)list2.get(j) ;
				String currencyCode2 = (String)map2.get("CURRENCY_CODE") ;
				if(currencyCode1.equals(currencyCode2)){
					double totalLcyBalance1=0;
					double totalLcyBalance2=0;
					double totalBalance1=0;
					double totalBalance2=0;
					if (map1.get("TOTAL_LCY_BALANCE") instanceof BigDecimal){
						totalLcyBalance1 = ((BigDecimal)map1.get("TOTAL_LCY_BALANCE")).doubleValue() ;
					}else if(map1.get("TOTAL_LCY_BALANCE") instanceof Double){
						totalLcyBalance1 = (Double)map1.get("TOTAL_LCY_BALANCE") ;	
					}
					if (map2.get("TOTAL_LCY_BALANCE") instanceof BigDecimal){
						totalLcyBalance2 = ((BigDecimal)map2.get("TOTAL_LCY_BALANCE")).doubleValue() ;
					}else if(map2.get("TOTAL_LCY_BALANCE") instanceof Double){
						totalLcyBalance2 = (Double)map2.get("TOTAL_LCY_BALANCE") ;	
					}
					if (map1.get("TOTAL_BALANCE") instanceof BigDecimal){
						totalBalance1 = ((BigDecimal)map1.get("TOTAL_BALANCE")).doubleValue() ;
					}else if(map1.get("TOTAL_BALANCE") instanceof Double){
						totalBalance1 = (Double)map1.get("TOTAL_BALANCE") ;	
					}
					if (map2.get("TOTAL_BALANCE") instanceof BigDecimal){
						totalBalance1 = ((BigDecimal)map2.get("TOTAL_BALANCE")).doubleValue() ;
					}else if(map2.get("TOTAL_BALANCE") instanceof Double){
						totalBalance1 = (Double)map2.get("TOTAL_BALANCE") ;	
					}

					totalLcyBalance1 += totalLcyBalance2 ;
					totalBalance1 += totalBalance2 ;
					map1.put("TOTAL_LCY_BALANCE", totalLcyBalance1) ;
					map1.put("TOTAL_BALANCE", totalBalance1) ;
				}else{
					k++;
				}
				if(k==length){
					list1.add(map2);
				}
			}
		}
		
		return list1 ;
	}
	
}
