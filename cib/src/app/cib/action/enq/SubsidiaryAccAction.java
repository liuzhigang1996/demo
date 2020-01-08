package app.cib.action.enq;

/**
 * @author nabai
 *
 * ��?�����?�������޸ġ�ɾ���ѯ��?i������?i��������?a
 */
import app.cib.core.CibAction;
import app.cib.core.CibTransClient;

import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.*;
import com.neturbo.set.core.*;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.context.ApplicationContext;

import app.cib.bo.bnk.BankUser;
import app.cib.bo.sys.*;
import app.cib.service.enq.AccountEnqureBankService;
import app.cib.service.enq.AccountEnqureService;
import app.cib.service.sys.CorpAccountService;
import app.cib.util.RcCorporation;
import app.cib.util.RcCurrency;
import app.cib.util.RcTransactionFilter;

public class SubsidiaryAccAction extends CibAction {
	
	private static String defaultDatePattern = Config
	.getProperty("DefaultDatePattern");

	public void listAccountSummary() throws NTBException {
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser user = (CorpUser) this.getUser();
		user.setLanguage(lang);//add by linrui for mul-language
		String corpId = user.getCorpId();
		String lastCorpId = Utils.null2Empty(getParameter("lastCorpId"));
		String subCorpId = Utils.null2Empty(getParameter("corpId"));
		String flag = null;
		// ���ѡ�����ĸ��˾�Ĳ�ѯ�����ñ�־flagΪY,������ҳ����ֻ��ʾ��ѯ�������Ҳ��������� add by mxl 0130
		if (subCorpId.equals("") && lastCorpId.equals("")) {
			flag = "Y";
		} else {
			flag = "N";
		}
		List currentAccountList = null;
		List timeDepositList = null;
		List overdraftAccountList = null;
		List loanAccountList = null;
		List savingAccountList = null;
		
		//add by Li_zd 2016-08-18
		List creditAccountList = null ;

		// ��ѯ�ӹ�˾
		RcCorporation rcCorp = new RcCorporation(
				RcCorporation.SHOW_CORP_WITHOUT_ROOT);
		rcCorp.setArgs(user);
		List corpList = getCorpList(corpId, subCorpId, rcCorp);
		

		Map resultData = new HashMap();
		resultData.put("showNothing", "1");
		resultData.putAll((Map) rcCorp.getObject(corpId));
		resultData.put("corpList", corpList);
		resultData.put("flag", flag);
		AccountEnqureService accountEnqureService = (AccountEnqureService) Config
				.getAppContext().getBean("AccountEnqureService");
		// ����ǵ�һ�ε����ѯ,�Ͳ������� add by mxl 0130
		if (flag.equals("N")) {
			if (subCorpId.equals("") || !subCorpId.equals(lastCorpId)) {

				if (subCorpId.equals("")) {
					subCorpId = lastCorpId;
				}

				// �������ѯ��д��session
				// APPCODE_CURRENT_ACCOUNT
				currentAccountList = accountEnqureService.listSummaryByAccType(
						user, subCorpId, CorpAccount.ACCOUNT_TYPE_CURRENT, CibTransClient.APPCODE_CURRENT_ACCOUNT);
				setUsrSessionDataValue("currentList", currentAccountList);
				// APPCODE_TIME_DEPOSIT
				timeDepositList = accountEnqureService.listSummaryByAccType(
						user, subCorpId, CorpAccount.ACCOUNT_TYPE_TIME_DEPOSIT, CibTransClient.APPCODE_TIME_DEPOSIT);
				setUsrSessionDataValue("tdList", timeDepositList);
				// APPCODE_OVERDRAFT_ACCOUNT
				//modify by wcc 20180208
				/*overdraftAccountList = accountEnqureService
						.listSummaryByAccType(user, subCorpId,
								CorpAccount.ACCOUNT_TYPE_OVER_DRAFT, CibTransClient.APPCODE_OVERDRAFT_ACCOUNT);*/
				setUsrSessionDataValue("overdraftList",
						overdraftAccountList);
				// APPCODE_LOAN_ACCOUNT
				loanAccountList = accountEnqureService.listSummaryByAccType(
						user, subCorpId, CorpAccount.ACCOUNT_TYPE_LOAN, CibTransClient.APPCODE_LOAN_ACCOUNT);
				setUsrSessionDataValue("loanList", loanAccountList);
				// APPCODE_SAVING_ACCOUNT add by mxl 1120
				savingAccountList = accountEnqureService.listSummaryByAccType(
						user, subCorpId, CorpAccount.ACCOUNT_TYPE_SAVING, CibTransClient.APPCODE_SAVING_ACCOUNT);
				setUsrSessionDataValue("savingList", savingAccountList);
				//add by Li_zd 2016-08-16
				//modify by wcc 20180208
				/*creditAccountList = creditAccountList(user,subCorpId);*/
				setUsrSessionDataValue("creditList", creditAccountList);
			} else {
				// �û������ֵ���ֽ���, ��session�ж������
				currentAccountList = (List) this
						.getUsrSessionDataValue("currentList");
				timeDepositList = (List) this
						.getUsrSessionDataValue("tdList");
				overdraftAccountList = (List) this
						.getUsrSessionDataValue("overdraftList");
				loanAccountList = (List) this
						.getUsrSessionDataValue("loanList");
				// add by mxl 1120
				savingAccountList = (List) this
						.getUsrSessionDataValue("savingList");
				creditAccountList = (List) this
						.getUsrSessionDataValue("creditList");
			}
			
			
			
			List csvList = new ArrayList();
			// ����
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
				firstMap.put("showAccType", Utils.getStringFromProperties("app.cib.resource.common.account_type","1",lang.toString()));//"CURRENT ACCOUNT"
				csvList.addAll(currentAccountList);
				csvList.add(totalMap);
			}

			// ����
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

			// ͸֧
			resultData.put("overdraftList", overdraftAccountList);
			//modify by wcc 20180208
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

			// ���
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

			// Saving Account add by mxl 1120
			resultData.put("savingList", savingAccountList);
			if (savingAccountList.size() > 0) {
				resultData.put("showSavingAccount", "1");
				resultData.put("showNothing", "0");
				String subTotal = "0";
				resultData.put("SA_SubTotal", subTotal);

				Map totalMap = new HashMap();
				totalMap.put("SUBTOTAL_TITLE", Utils.subTotalString(lang.toString()));//mod by linrui 20190729 for mul-lang download
				totalMap.put("SUBTOTAL", subTotal);

				Map firstMap = (Map) savingAccountList.get(0);
				firstMap.put("showAccType", Utils.getStringFromProperties("app.cib.resource.common.account_type","5",lang.toString()));//"SAVING ACCOUNT"
				csvList.addAll(savingAccountList);
				csvList.add(totalMap);
			}
			// ��ʾ���ÿ�����
			resultData.put("creditList", creditAccountList);
			//modify by wcc 20180208
			/*if (creditAccountList.size() > 0) {
				resultData.put("showCreditAccount", "1");
				resultData.put("showNothing", "0");
				String subTotal = "0";
				resultData.put("CC_SubTotal", subTotal);

				Map totalMap = new HashMap();
				totalMap.put("SUBTOTAL_TITLE", Utils.subTotalString(lang.toString()));//mod by linrui 20190729 for mul-lang download
				totalMap.put("SUBTOTAL", subTotal);

				Map firstMap = (Map) creditAccountList.get(0);
				firstMap.put("showAccType", Utils.getStringFromProperties("app.cib.resource.common.account_type","6",lang.toString()));//"CREDIT CARD"
				csvList.addAll(creditAccountList);
				csvList.add(totalMap);
			}*/
			resultData.put("lastCorpId", subCorpId);
			// д�������б�
			resultData.put("csvList", csvList);
		}
		setResultData(resultData);

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
	
	public List getCorpList(String corpId, String subCorpId, RcCorporation rcCorp) throws NTBException{
		boolean hasRight = false;
		if(subCorpId.equals("")){
			hasRight = true;
		}
		List corpList = new ArrayList();
		List keyList = rcCorp.getKeys();
		for (int i = 0; i < keyList.size(); i++) {
			String key = (String) keyList.get(i);
			if (!key.equals(corpId)) {
				Map corpMap = rcCorp.getObject(key);
				corpList.add(corpMap);
				if(subCorpId.equals(corpMap.get("CORP_ID"))){
					hasRight = true;
				}
			}
		}
		if(!hasRight){
			throw new NTBException("err.sys.IllegalInput");
		}
		return corpList;

	}

	public void listCurrentAccount() throws NTBException {
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser user = (CorpUser) this.getUser();
		String corpId = user.getCorpId();
		user.setLanguage(lang);//add by linrui for mul-language
		//modify by long_zg 2014-08-28 for BOB_security_update begin
		/*String subCorpId = this.getParameter("corpId");*/
		String subCorpId = Utils.null2Empty(this.getParameter("corpId"));
		//modify by long_zg 2014-08-28 for BOB_security_update end

		String flag = null;
		// ���ѡ�����ĸ��˾�Ĳ�ѯ�����ñ�־flagΪY,������ҳ����ֻ��ʾ��ѯ�������Ҳ��������� add by mxl 0130
		if (subCorpId == null || subCorpId.equals("")) {
			flag = "Y";
		} else {
			flag = "N";
		}
		String eqCcy = this.getParameter("eqCcy");
		List currentAccountList = null;

		// ��ѯ�ӹ�˾
		RcCorporation rcCorp = new RcCorporation(
				RcCorporation.SHOW_CORP_WITHOUT_ROOT);
		rcCorp.setArgs(user);
		List corpList = getCorpList(corpId, subCorpId, rcCorp);

		Map resultData = new HashMap();
		resultData.putAll(rcCorp.getObject(corpId));
		resultData.put("corpList", corpList);
		resultData.put("eqCcy", eqCcy);
		setResultData(resultData);

		
		//add by wcc 20180203
		//keyAll存放所有键值合起来的字符串
		String keyAll = Utils.null2EmptyWithTrim(this.getParameter("keyAll"));
		//赋给上一页的键值
		String prePage = Utils.null2EmptyWithTrim(this.getParameter("prePage"));
		//赋给下一页的键值
		String nextPage = Utils.null2EmptyWithTrim(this.getParameter("nextPage"));
		//判断点击了上一页还是下一页
		String flagPage = Utils.null2EmptyWithTrim(this.getParameter("flagPage"));
		// monify by wcc 20180115 增加先进后出的栈stack
		Stack<String> stackPage = new Stack<String>();
		//keyPage该数组用来存放pagekey翻页的键值
		String[] keyPage = new String[]{};
		//判断是否是第一页
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
		//flagPage来判断压栈，出栈	的方向
		if("N".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//点击下一页时，将栈中下一页的键值赋值给上一页，点击上一页时，将栈中上一页的键值赋值上一页
			prePage = nextPage;
		}else if("".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//点击下一页时，将栈中下一页的键值赋值给上一页，点击上一页时，将栈中上一页的键值赋值上一页
			prePage = nextPage;
		}else if("P".equals(flagPage)){
			//点击上一页时，先将头两个键值
			stackPage.pop();
			stackPage.pop();
			//拿到翻页的键值
			prePage = stackPage.pop();
			stackPage.push(prePage);
		}
		//disableN,disableP用来判断上一页，下一页按钮是否显示。
		String disableN = "";
		String disableP = "";
		
		// ����ǵ�һ�ε����ѯ,�Ͳ������� add by mxl 0130
		if (flag.equals("N")) {
			AccountEnqureService accountEnqureService = (AccountEnqureService) Config
					.getAppContext().getBean("AccountEnqureService");
			CorpAccountService corpAccountService = (CorpAccountService) Config
					.getAppContext().getBean("CorpAccountService");
			//modify by wcc 201803012
			/*currentAccountList = accountEnqureService.listCurrentAccount(user,
					subCorpId);*/
			currentAccountList = accountEnqureService.listCurrentAccount(user,subCorpId,CibTransClient.APPCODE_CURRENT_ACCOUNT,prePage);
			//modify by wcc 20180115 拿出"NEXT_KEY"的键值
			nextPage = currentAccountList.get(currentAccountList.size()-1).toString();
			
			if(!"".equals(nextPage)&&!"".equals(nextPage.trim())){
				 disableN = "button";
			}else{
				 disableN = "hidden";
			}
			if(!"".equals(prePage)){
				disableP = "button";
			}else{
				disableP = "hidden";
			}
			//modify by wcc 20180115 remove掉在getCurrentPostingStmt方法中增加的perPage对象
			currentAccountList.remove(currentAccountList.size()-1);
			//将得到的新的键值接着压入栈
			stackPage.push(nextPage);
			//用StringBuffer来接收键值的字符串，以逗号分隔，去除最后一个多余的逗号。
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<stackPage.size();i++){
				sb.append(stackPage.get(i)).append(",");
			}
			sb.replace(sb.length()-1, sb.length(),"");
			keyAll = sb.toString();
			
			// add by mxl 0110 ����ʺŶ�Ӧ���ʻ�����
			List toList = new ArrayList();
			Map accountData = null;
			int status4Count = 0;
			for (int i = 0; i < currentAccountList.size(); i++) {
				accountData = (Map) currentAccountList.get(i);
				String account = accountData.get("ACCOUNT_NO").toString().trim();
				String ccy = accountData.get("CURRENCY_CODE").toString().trim();
				String accountName = corpAccountService.getAccountName(account,ccy);
				accountData.put("ACCOUNT_NAME", accountName);
				if(accountData.get("ACCOUNT_STATUS").equals("4")){
					status4Count++;
				}
				toList.add(accountData);
			}
			if(toList.size() == status4Count){
				toList.clear();
			}
			resultData.put("currentAccountList", toList);
		}
		resultData.put("flag", flag);
		//modify by wcc 20180203
		resultData.put("nextPage",nextPage);
		resultData.put("prePage",prePage);
		resultData.put("disableN",disableN);
		resultData.put("disableP",disableP);
		resultData.put("keyAll",keyAll);
		setResultData(resultData);

	}

	public void listSavingAccount() throws NTBException {
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser user = (CorpUser) this.getUser();
		user.setLanguage(lang);//add by linrui for mul-language
		String corpId = user.getCorpId();
		
		//modify by long_zg 2014-08-28 for BOB_security_update begin
		/*String subCorpId = this.getParameter("corpId");*/
		String subCorpId = Utils.null2Empty(this.getParameter("corpId"));
		//modify by long_zg 2014-08-28 for BOB_security_update end
		
		String flag = null;
		// ���ѡ�����ĸ��˾�Ĳ�ѯ�����ñ�־flagΪY,������ҳ����ֻ��ʾ��ѯ�������Ҳ��������� add by mxl 0130
		if (subCorpId == null || subCorpId.equals("")) {
			flag = "Y";
		} else {
			flag = "N";
		}
		String eqCcy = this.getParameter("eqCcy");
		List savingAccountList = null;

		// ��ѯ�ӹ�˾
		RcCorporation rcCorp = new RcCorporation(
				RcCorporation.SHOW_CORP_WITHOUT_ROOT);
		rcCorp.setArgs(user);

		List corpList = getCorpList(corpId, subCorpId, rcCorp);

		Map resultData = new HashMap();
		resultData.putAll(rcCorp.getObject(corpId));
		resultData.put("corpList", corpList);
		resultData.put("eqCcy", eqCcy);
		setResultData(resultData);

		//add by wcc 20180203
		//keyAll存放所有键值合起来的字符串
		String keyAll = Utils.null2EmptyWithTrim(this.getParameter("keyAll"));
		//赋给上一页的键值
		String prePage = Utils.null2EmptyWithTrim(this.getParameter("prePage"));
		//赋给下一页的键值
		String nextPage = Utils.null2EmptyWithTrim(this.getParameter("nextPage"));
		//判断点击了上一页还是下一页
		String flagPage = Utils.null2EmptyWithTrim(this.getParameter("flagPage"));
		// monify by wcc 20180115 增加先进后出的栈stack
		Stack<String> stackPage = new Stack<String>();
		//keyPage该数组用来存放pagekey翻页的键值
		String[] keyPage = new String[]{};
		//判断是否是第一页
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
		//flagPage来判断压栈，出栈	的方向
		if("N".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//点击下一页时，将栈中下一页的键值赋值给上一页，点击上一页时，将栈中上一页的键值赋值上一页
			prePage = nextPage;
		}else if("".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//点击下一页时，将栈中下一页的键值赋值给上一页，点击上一页时，将栈中上一页的键值赋值上一页
			prePage = nextPage;
		}else if("P".equals(flagPage)){
			//点击上一页时，先将头两个键值
			stackPage.pop();
			stackPage.pop();
			//拿到翻页的键值
			prePage = stackPage.pop();
			stackPage.push(prePage);
		}
		//disableN,disableP用来判断上一页，下一页按钮是否显示。
		String disableN = "";
		String disableP = "";
		
		// ����ǵ�һ�ε����ѯ,�Ͳ������� add by mxl 0130
		if (flag.equals("N")) {
			AccountEnqureService accountEnqureService = (AccountEnqureService) Config
					.getAppContext().getBean("AccountEnqureService");
			CorpAccountService corpAccountService = (CorpAccountService) Config
					.getAppContext().getBean("CorpAccountService");
			//modify by wcc 20180312 
			/*savingAccountList = accountEnqureService.listSavingAccount(user,
					subCorpId);*/
			savingAccountList = accountEnqureService.listSavingAccount(user,subCorpId,CibTransClient.APPCODE_SAVING_ACCOUNT,prePage);
			//modify by wcc 20180115 拿出"NEXT_KEY"的键值
			nextPage = savingAccountList.get(savingAccountList.size()-1).toString();
			
			if(!"".equals(nextPage)&&!"".equals(nextPage.trim())){
				 disableN = "button";
			}else{
				 disableN = "hidden";
			}
			if(!"".equals(prePage)){
				disableP = "button";
			}else{
				disableP = "hidden";
			}
			//modify by wcc 20180115 remove掉在getCurrentPostingStmt方法中增加的perPage对象
			savingAccountList.remove(savingAccountList.size()-1);
			//将得到的新的键值接着压入栈
			stackPage.push(nextPage);
			//用StringBuffer来接收键值的字符串，以逗号分隔，去除最后一个多余的逗号。
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<stackPage.size();i++){
				sb.append(stackPage.get(i)).append(",");
			}
			sb.replace(sb.length()-1, sb.length(),"");
			keyAll = sb.toString();
			
			// add by mxl 0110 ����ʺŶ�Ӧ���ʻ�����
			List toList = new ArrayList();
			Map accountData = null;
			int status4Count = 0;
			for (int i = 0; i < savingAccountList.size(); i++) {
				accountData = (Map) savingAccountList.get(i);
				String account = accountData.get("ACCOUNT_NO").toString().trim();
				String ccy = accountData.get("CURRENCY_CODE").toString().trim();
				String accountName = corpAccountService.getAccountName(account,ccy);
				accountData.put("ACCOUNT_NAME", accountName);
				if(accountData.get("ACCOUNT_STATUS").equals("4")){
					status4Count++;
				}
				toList.add(accountData);
			}
			if(toList.size() == status4Count){
				toList.clear();
			}
			resultData.put("savingAccountList", toList);
		}
		resultData.put("flag", flag);
		//modify by wcc 20180203
		resultData.put("nextPage",nextPage);
		resultData.put("prePage",prePage);
		resultData.put("disableN",disableN);
		resultData.put("disableP",disableP);
		resultData.put("keyAll",keyAll);
		setResultData(resultData);
	}

	public void listTimeDeposit() throws NTBException {
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171106
		CorpUser user = (CorpUser) this.getUser();
		user.setLanguage(lang);
		//hjs
		String lastCorpId = Utils.null2EmptyWithTrim(this.getResultData().get("subCorpId"));
		
		String corpId = user.getCorpId();
		
		//modify by long_zg 2014-08-28 for BOB_security_update begin
		/*String subCorpId = this.getParameter("corpId");*/
		String subCorpId = Utils.null2Empty(this.getParameter("corpId"));
		//modify by long_zg 2014-08-28 for BOB_security_update end
		
		String flag = null;
		// ���ѡ�����ĸ��˾�Ĳ�ѯ�����ñ�־flagΪY,������ҳ����ֻ��ʾ��ѯ�������Ҳ��������� add by mxl 0130
		if (subCorpId == null || subCorpId.equals("")) {
			flag = "Y";
		} else {
			flag = "N";
		}
		List timeDepositList = null;

		// ��ѯ�ӹ�˾
		RcCorporation rcCorp = new RcCorporation(
				RcCorporation.SHOW_CORP_WITHOUT_ROOT);
		rcCorp.setArgs(user);

		List corpList = getCorpList(corpId, subCorpId, rcCorp);

		Map resultData = new HashMap();
		resultData.putAll(rcCorp.getObject(corpId));
		resultData.put("corpList", corpList);
		setResultData(resultData);
		
		//add by wcc 20180203
		//keyAll存放所有键值合起来的字符串
		String keyAll = Utils.null2EmptyWithTrim(this.getParameter("keyAll"));
		//赋给上一页的键值
		String prePage = Utils.null2EmptyWithTrim(this.getParameter("prePage"));
		//赋给下一页的键值
		String nextPage = Utils.null2EmptyWithTrim(this.getParameter("nextPage"));
		//判断点击了上一页还是下一页
		String flagPage = Utils.null2EmptyWithTrim(this.getParameter("flagPage"));
		// monify by wcc 20180115 增加先进后出的栈stack
		Stack<String> stackPage = new Stack<String>();
		//keyPage该数组用来存放pagekey翻页的键值
		String[] keyPage = new String[]{};
		//判断是否是第一页
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
		//flagPage来判断压栈，出栈	的方向
		if("N".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//点击下一页时，将栈中下一页的键值赋值给上一页，点击上一页时，将栈中上一页的键值赋值上一页
			prePage = nextPage;
		}else if("".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//点击下一页时，将栈中下一页的键值赋值给上一页，点击上一页时，将栈中上一页的键值赋值上一页
			prePage = nextPage;
		}else if("P".equals(flagPage)){
			//点击上一页时，先将头两个键值
			stackPage.pop();
			stackPage.pop();
			//拿到翻页的键值
			prePage = stackPage.pop();
			stackPage.push(prePage);
		}
		//disableN,disableP用来判断上一页，下一页按钮是否显示。
		String disableN = "";
		String disableP = "";
		
		
		// ����ǵ�һ�ε����ѯ,�Ͳ������� add by mxl 0130
		if (flag.equals("N")) {
			AccountEnqureService accountEnqureService = (AccountEnqureService) Config
					.getAppContext().getBean("AccountEnqureService");
			Config.getAppContext().getBean("ExchangeRatesService");
			//modfiy by wcc 20180312
			/*timeDepositList = accountEnqureService.listTimeDeposit(user,
					subCorpId);*/
			timeDepositList = accountEnqureService.listTimeDeposit(user,subCorpId,CibTransClient.APPCODE_TIME_DEPOSIT,prePage);
			//modify by wcc 20180115 拿出"NEXT_KEY"的键值
			nextPage = timeDepositList.get(timeDepositList.size()-1).toString();
			
			if(!"".equals(nextPage)&&!"".equals(nextPage.trim())){
				 disableN = "button";
			}else{
				 disableN = "hidden";
			}
			if(!"".equals(prePage)){
				disableP = "button";
			}else{
				disableP = "hidden";
			}
			//modify by wcc 20180115 remove掉在getCurrentPostingStmt方法中增加的perPage对象
			timeDepositList.remove(timeDepositList.size()-1);
			//将得到的新的键值接着压入栈
			stackPage.push(nextPage);
			//用StringBuffer来接收键值的字符串，以逗号分隔，去除最后一个多余的逗号。
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<stackPage.size();i++){
				sb.append(stackPage.get(i)).append(",");
			}
			sb.replace(sb.length()-1, sb.length(),"");
			keyAll = sb.toString();
			
			Map timeDeposit = null;
			Map tdDetial = null;
			String period = "";
			List openList = new ArrayList();
			RcCurrency rc = new RcCurrency();
			CorpUser user2 = (CorpUser) getUser();//add by linrui for mul-language
			user2.setLanguage(lang);
			for (int i = 0; i < timeDepositList.size(); i++) {

				// hjs: set period
				timeDeposit = (Map) timeDepositList.get(i);
				//modify by wcc 20180209 先去掉下面的判断
				/*if (timeDeposit.get("ACCOUNT_STATUS").toString().trim().equals(
						"1")) {*/

					timeDeposit.put("MATURITY_DATE", timeDeposit.get(
							"MATURITY_DATE").toString().trim());
					tdDetial = accountEnqureService.viewDepositDetial(
							user2, timeDeposit.get("ACCOUNT_NO")//mod by linrui for mul-language
									.toString());
					period = tdDetial.get("INT_FREQUENCY").toString()
							+ tdDetial.get("REN_PERIOD").toString();
					timeDeposit.put("PERIOD", accountEnqureService.formatPeriod(period));
					timeDeposit.put("currency", rc.getCcyByCbsNumber(timeDeposit.get("CURRENCY_CODE").toString().trim()));

					openList.add(timeDeposit);

				//}
			}
			String sortType = Utils.null2EmptyWithTrim(this.getParameter("sortType"));
			if(!lastCorpId.equals(subCorpId)){
				sortType = "C";
			}
			if(sortType.equalsIgnoreCase("M")) {
				Sorting.sortMapList(openList,
						new String[]{"MATURITY_DATE", "ACCOUNT_NO"}, Sorting.SORT_TYPE_ASC);
			} else {
				Sorting.sortMapList(openList,
						new String[]{"currency", "ACCOUNT_NO"}, Sorting.SORT_TYPE_ASC);
			}
			resultData.put("timeDepositList", openList);
			resultData.put("sortType", sortType);
		}
		resultData.put("subCorpId", subCorpId);
		resultData.put("flag", flag);
		//modify by wcc 20180203
		resultData.put("nextPage",nextPage);
		resultData.put("prePage",prePage);
		resultData.put("disableN",disableN);
		resultData.put("disableP",disableP);
		resultData.put("keyAll",keyAll);
		setResultData(resultData);
	}

	public void listOverdraftAccount() throws NTBException {
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser user = (CorpUser) this.getUser();
		user.setLanguage(lang);//add by linrui for mul-language
		String corpId = user.getCorpId();
		//modify by long_zg 2014-08-28 for BOB_security_update begin
		/*String subCorpId = this.getParameter("corpId");*/
		String subCorpId = Utils.null2Empty(this.getParameter("corpId"));
		//modify by long_zg 2014-08-28 for BOB_security_update end
		
		String flag = null;
		// ���ѡ�����ĸ��˾�Ĳ�ѯ�����ñ�־flagΪY,������ҳ����ֻ��ʾ��ѯ�������Ҳ��������� add by mxl 0130
		if (subCorpId == null || subCorpId.equals("")) {
			flag = "Y";
		} else {
			flag = "N";
		}
		List overdraftAccountList = null;

		String eqCcy = this.getParameter("eqCcy");

		// ��ѯ�ӹ�˾
		RcCorporation rcCorp = new RcCorporation(
				RcCorporation.SHOW_CORP_WITHOUT_ROOT);
		rcCorp.setArgs(user);

		List corpList = getCorpList(corpId, subCorpId, rcCorp);

		Map resultData = new HashMap();
		resultData.putAll(rcCorp.getObject(corpId));
		resultData.put("corpList", corpList);
		setResultData(resultData);
		
		//add by wcc 20180203
		//keyAll存放所有键值合起来的字符串
		String keyAll = Utils.null2EmptyWithTrim(this.getParameter("keyAll"));
		//赋给上一页的键值
		String prePage = Utils.null2EmptyWithTrim(this.getParameter("prePage"));
		//赋给下一页的键值
		String nextPage = Utils.null2EmptyWithTrim(this.getParameter("nextPage"));
		//判断点击了上一页还是下一页
		String flagPage = Utils.null2EmptyWithTrim(this.getParameter("flagPage"));
		// monify by wcc 20180115 增加先进后出的栈stack
		Stack<String> stackPage = new Stack<String>();
		//keyPage该数组用来存放pagekey翻页的键值
		String[] keyPage = new String[]{};
		//判断是否是第一页
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
		//flagPage来判断压栈，出栈	的方向
		if("N".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//点击下一页时，将栈中下一页的键值赋值给上一页，点击上一页时，将栈中上一页的键值赋值上一页
			prePage = nextPage;
		}else if("".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//点击下一页时，将栈中下一页的键值赋值给上一页，点击上一页时，将栈中上一页的键值赋值上一页
			prePage = nextPage;
		}else if("P".equals(flagPage)){
			//点击上一页时，先将头两个键值
			stackPage.pop();
			stackPage.pop();
			//拿到翻页的键值
			prePage = stackPage.pop();
			stackPage.push(prePage);
		}
		//disableN,disableP用来判断上一页，下一页按钮是否显示。
		String disableN = "";
		String disableP = "";
		
		// ����ǵ�һ�ε����ѯ,�Ͳ������� add by mxl 0130
		if (flag.equals("N")) {
			AccountEnqureService accountEnqureService = (AccountEnqureService) Config
					.getAppContext().getBean("AccountEnqureService");
			CorpAccountService corpAccountService = (CorpAccountService) Config
					.getAppContext().getBean("CorpAccountService");

			eqCcy = "MOP";
			//modify by wcc 20180312
			/*overdraftAccountList = accountEnqureService.listOverdraftAccount(
					user, subCorpId);*/
			overdraftAccountList = accountEnqureService.listOverdraftAccount(user, subCorpId,CibTransClient.APPCODE_OVERDRAFT_ACCOUNT,prePage);
			//modify by wcc 20180115 拿出"NEXT_KEY"的键值
			nextPage = overdraftAccountList.get(overdraftAccountList.size()-1).toString();
			
			if(!"".equals(nextPage)&&!"".equals(nextPage.trim())){
				 disableN = "button";
			}else{
				 disableN = "hidden";
			}
			if(!"".equals(prePage)){
				disableP = "button";
			}else{
				disableP = "hidden";
			}
			//modify by wcc 20180115 remove掉在getCurrentPostingStmt方法中增加的perPage对象
			overdraftAccountList.remove(overdraftAccountList.size()-1);
			//将得到的新的键值接着压入栈
			stackPage.push(nextPage);
			//用StringBuffer来接收键值的字符串，以逗号分隔，去除最后一个多余的逗号。
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<stackPage.size();i++){
				sb.append(stackPage.get(i)).append(",");
			}
			sb.replace(sb.length()-1, sb.length(),"");
			keyAll = sb.toString();
			
			// add by mxl 0110 ����ʺŶ�Ӧ���ʻ�����
			List toList = new ArrayList();
			Map accountData = null;
			int status4Count = 0;
			for (int i = 0; i < overdraftAccountList.size(); i++) {
				accountData = (Map) overdraftAccountList.get(i);
				String account = accountData.get("ACCOUNT_NO").toString().trim();
				String ccy = accountData.get("CURRENCY_CODE").toString().trim();
				String accountName = corpAccountService.getAccountName(account,ccy);
				accountData.put("ACCOUNT_NAME", accountName);
				if(accountData.get("ACCOUNT_STATUS").equals("4")){
					status4Count++;
				}
				toList.add(accountData);
			}
			if(toList.size() == status4Count){
				toList.clear();
			}
			resultData.put("overdraftAccountList", toList);
			resultData.put("eqCcy", eqCcy);
		}
		resultData.put("flag", flag);
		//modify by wcc 20180203
		resultData.put("nextPage",nextPage);
		resultData.put("prePage",prePage);
		resultData.put("disableN",disableN);
		resultData.put("disableP",disableP);
		resultData.put("keyAll",keyAll);
		setResultData(resultData);

	}

	public void listLoanAccount() throws NTBException {
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser user = (CorpUser) this.getUser();
		user.setLanguage(lang);//add by linrui for mul-language
		String corpId = user.getCorpId();
		
		//modify by long_zg 2014-08-28 for BOB_security_update begin
		/*String subCorpId = this.getParameter("corpId");*/
		String subCorpId = Utils.null2Empty(this.getParameter("corpId"));
		//modify by long_zg 2014-08-28 for BOB_security_update end
		
		String flag = null;
		// ���ѡ�����ĸ��˾�Ĳ�ѯ�����ñ�־flagΪY,������ҳ����ֻ��ʾ��ѯ�������Ҳ��������� add by mxl 0130
		if (subCorpId == null || subCorpId.equals("")) {
			flag = "Y";
		} else {
			flag = "N";
		}
		List loanAccountList = null;

		String eqCcy = this.getParameter("eqCcy");

		// ��ѯ�ӹ�˾
		RcCorporation rcCorp = new RcCorporation(
				RcCorporation.SHOW_CORP_WITHOUT_ROOT);
		rcCorp.setArgs(user);

		List corpList = getCorpList(corpId, subCorpId, rcCorp);

		Map resultData = new HashMap();
		resultData.putAll(rcCorp.getObject(corpId));
		resultData.put("corpList", corpList);
		setResultData(resultData);
		
		//add by wcc 20180203
		//keyAll存放所有键值合起来的字符串
		String keyAll = Utils.null2EmptyWithTrim(this.getParameter("keyAll"));
		//赋给上一页的键值
		String prePage = Utils.null2EmptyWithTrim(this.getParameter("prePage"));
		//赋给下一页的键值
		String nextPage = Utils.null2EmptyWithTrim(this.getParameter("nextPage"));
		//判断点击了上一页还是下一页
		String flagPage = Utils.null2EmptyWithTrim(this.getParameter("flagPage"));
		// monify by wcc 20180115 增加先进后出的栈stack
		Stack<String> stackPage = new Stack<String>();
		//keyPage该数组用来存放pagekey翻页的键值
		String[] keyPage = new String[]{};
		//判断是否是第一页
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
		//flagPage来判断压栈，出栈	的方向
		if("N".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//点击下一页时，将栈中下一页的键值赋值给上一页，点击上一页时，将栈中上一页的键值赋值上一页
			prePage = nextPage;
		}else if("".equals(flagPage)){
			stackPage.push(nextPage);
			nextPage = stackPage.pop();
			//点击下一页时，将栈中下一页的键值赋值给上一页，点击上一页时，将栈中上一页的键值赋值上一页
			prePage = nextPage;
		}else if("P".equals(flagPage)){
			//点击上一页时，先将头两个键值
			stackPage.pop();
			stackPage.pop();
			//拿到翻页的键值
			prePage = stackPage.pop();
			stackPage.push(prePage);
		}
		//disableN,disableP用来判断上一页，下一页按钮是否显示。
		String disableN = "";
		String disableP = "";
		// ����ǵ�һ�ε����ѯ,�Ͳ������� add by mxl 0130
		if (flag.equals("N")) {
			AccountEnqureService accountEnqureService = (AccountEnqureService) Config
					.getAppContext().getBean("AccountEnqureService");
			CorpAccountService corpAccountService = (CorpAccountService) Config
					.getAppContext().getBean("CorpAccountService");

			eqCcy = "MOP";
			//modify by wcc 20180209
			/*loanAccountList = accountEnqureService.listLoanAccount(user,
					subCorpId);*/
			loanAccountList = accountEnqureService.listLoanAccount(user,subCorpId,CibTransClient.APPCODE_LOAN_ACCOUNT,prePage);
			//modify by wcc 20180115 拿出"NEXT_KEY"的键值
			nextPage = loanAccountList.get(loanAccountList.size()-1).toString();
			
			if(!"".equals(nextPage)&&!"".equals(nextPage.trim())){
				 disableN = "button";
			}else{
				 disableN = "hidden";
			}
			if(!"".equals(prePage)){
				disableP = "button";
			}else{
				disableP = "hidden";
			}
			//modify by wcc 20180115 remove掉在getCurrentPostingStmt方法中增加的perPage对象
			loanAccountList.remove(loanAccountList.size()-1);
			//将得到的新的键值接着压入栈
			stackPage.push(nextPage);
			//用StringBuffer来接收键值的字符串，以逗号分隔，去除最后一个多余的逗号。
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<stackPage.size();i++){
				sb.append(stackPage.get(i)).append(",");
			}
			sb.replace(sb.length()-1, sb.length(),"");
			keyAll = sb.toString();
			
			// add by mxl 0110 ����ʺŶ�Ӧ���ʻ�����
			List toList = new ArrayList();
			Map accountData = null;
			int status4Count = 0;
			for (int i = 0; i < loanAccountList.size(); i++) {
				accountData = (Map) loanAccountList.get(i);
				String account = accountData.get("ACCOUNT_NO").toString().trim();
				String ccy = accountData.get("CURRENCY_CODE").toString().trim();
				String accountName = corpAccountService.getAccountName(account,ccy);
				accountData.put("ACCOUNT_NAME", accountName);
				if(accountData.get("ACCOUNT_STATUS").equals("4")){
					status4Count++;
				}
				toList.add(accountData);
			}
			if(toList.size() == status4Count){
				toList.clear();
			}
			resultData.put("loanAccountList", toList);
			resultData.put("eqCcy", eqCcy);
		}
		resultData.put("flag", flag);
		//modify by wcc 20180203
		resultData.put("nextPage",nextPage);
		resultData.put("prePage",prePage);
		resultData.put("disableN",disableN);
		resultData.put("disableP",disableP);
		resultData.put("keyAll",keyAll);
		setResultData(resultData);
	}
	
	public void listCreditAccount() throws NTBException {
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		CorpUser user = (CorpUser) this.getUser();
		user.setLanguage(lang);//add by linrui for mul-language
		String corpId = user.getCorpId();
		
		//modify by long_zg 2014-08-28 for BOB_security_update begin
		/*String subCorpId = this.getParameter("corpId");*/
		String subCorpId = Utils.null2Empty(this.getParameter("corpId"));
		//modify by long_zg 2014-08-28 for BOB_security_update end
		
		String flag = null;
		// ���ѡ�����ĸ��˾�Ĳ�ѯ�����ñ�־flagΪY,������ҳ����ֻ��ʾ��ѯ�������Ҳ��������� add by mxl 0130
		if (subCorpId == null || subCorpId.equals("")) {
			flag = "Y";
		} else {
			flag = "N";
		}
		String eqCcy = this.getParameter("eqCcy");
		List creditAccountList = null;

		// ��ѯ�ӹ�˾
		RcCorporation rcCorp = new RcCorporation(
				RcCorporation.SHOW_CORP_WITHOUT_ROOT);
		rcCorp.setArgs(user);

		List corpList = getCorpList(corpId, subCorpId, rcCorp);

		Map resultData = new HashMap();
		resultData.putAll(rcCorp.getObject(corpId));
		resultData.put("corpList", corpList);
		resultData.put("eqCcy", eqCcy);
		setResultData(resultData);

		// ����ǵ�һ�ε����ѯ,�Ͳ������� add by mxl 0130
		if (flag.equals("N")) {
			
			CorpAccountService corpAccountService = (CorpAccountService) Config
					.getAppContext().getBean("CorpAccountService");
			
			AccountEnqureService accountEnqureService = (AccountEnqureService) Config
			.getAppContext().getBean("AccountEnqureService");

			creditAccountList = accountEnqureService.listCreditAccount(user, subCorpId);
			// add by mxl 0110 ����ʺŶ�Ӧ���ʻ�����
			List toList = new ArrayList();
			Map accountData = null;
			int status4Count = 0;
			for (int i = 0; i < creditAccountList.size(); i++) {
				accountData = (Map) creditAccountList.get(i);
				String account = accountData.get("CREDIT_CARD_NO").toString().trim();
				String ccy = accountData.get("CURRENCY_CODE").toString().trim();
				
				Log.info("from host CREDIT_CARD_NO="+account);
				String accountName = corpAccountService.getAccountName(account,ccy);
				accountData.put("ACCOUNT_NAME", accountName);
				toList.add(accountData);
			}
			if(toList.size() == status4Count){
				toList.clear();
			}
			resultData.put("creditAccountList", toList);
		}
		resultData.put("flag", flag);
		setResultData(resultData);

	}
	
	/**
	 * add by long_zg 2014-12-19 for CR192 bob batch
	 * @return
	 * @throws NTBException
	 */
	public List creditAccountList(CorpUser user,String corpId) throws NTBException {
		AccountEnqureService accountEnqureService = (AccountEnqureService) Config
		.getAppContext().getBean("AccountEnqureService");
		Locale lang = (Locale)this.getSession().getAttribute("Locale$Of$Neturbo");//add by linrui for mul-language 20171107
		user.setLanguage(lang);//add by linrui for mul-language
		List visaCreditAccountList = accountEnqureService.listSummaryByAccType(user,corpId, CorpAccount.ACCOUNT_TYPE_CREDIT,CibTransClient.APPCODE_CREDIT_VISA);
		List aeCreditAccountList = accountEnqureService.listSummaryByAccType(user,corpId, CorpAccount.ACCOUNT_TYPE_CREDIT,CibTransClient.APPCODE_CREDIT_AE);
		List masterCreditAccountList = accountEnqureService.listSummaryByAccType(user,corpId, CorpAccount.ACCOUNT_TYPE_CREDIT,CibTransClient.APPCODE_CREDIT_MASTER);
		List utCreditAccountList = accountEnqureService.listSummaryByAccType(user,corpId, CorpAccount.ACCOUNT_TYPE_CREDIT,CibTransClient.APPCODE_CREDIT_UT);

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
