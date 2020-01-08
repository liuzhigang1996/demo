package app.cib.service.enq;

import java.util.*;

import org.springframework.context.ApplicationContext;

import app.cib.bo.sys.*;
import app.cib.service.sys.*;
import app.cib.util.UploadReporter;
import app.cib.dao.enq.*;
import app.cib.core.*;

import com.neturbo.set.core.*;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.*;
import com.neturbo.set.utils.*;

public class AccountEnqureServiceImpl implements AccountEnqureService {

	private AccountDao accountDao;
	private GenericJdbcDao genericJdbcDao;

	public AccountDao getAccountDao() {
		return accountDao;
	}

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}
	//modify by wcc 20180208
	public List listSummaryByAccType(CorpUser user, String corpId,
			String accType, String appCode) throws NTBException {
		CibTransClient testClient = new CibTransClient("CIB", "ZC01");
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService accService = (CorpAccountService) appContext
				.getBean("CorpAccountService");

		// 闁跨喐鏋婚幏鐑芥晸缁叉rpId 娑撴椽鏁撶粔闈╃秶閹风兘鏁撻弬銈嗗闁鏁撻弬銈嗗闁跨喐鏋婚幏宄板緝闁跨喐鏋婚幏鐑芥晸缂佺偟灏ㄩ幏锟�
		/*List accountList = new ArrayList();
		if (corpId == null || user.getCorpId().equals(corpId)) {
			List priviledgeList = new ArrayList();
			priviledgeList = accService.listPrivilegedAccount(user, accType);
			for (int i = 0; i < priviledgeList.size(); i++) {
				CorpAccount accObj = (CorpAccount) priviledgeList.get(i);
				Map accountItem = new HashMap();
				accountItem.put("ACCOUNT_NO", accObj.getAccountNo());

				accountList.add(accountItem);
			}
		} else {
			// 闁跨喐鏋婚幏鐑芥晸缁叉rpId 闁跨喐鏋婚幏铚傝礋闁跨喓笑閿濆繑瀚归柨鐔告灮閹风兘锟絚orpId闁跨喐鏋婚幏宄扮安闁跨喐鏋婚幏宄板緝闁跨喐鏋婚幏鐑芥晸缂佺偟灏ㄩ幏锟�
			List corpAccList = new ArrayList();
			corpAccList = accService.listCorpAccountByAccType(corpId, accType);
			for (int i = 0; i < corpAccList.size(); i++) {
				CorpAccount accObj = (CorpAccount) corpAccList.get(i);
				Map accountItem = new HashMap();
				accountItem.put("ACCOUNT_NO", accObj.getAccountNo());

				accountList.add(accountItem);
			}
		}*/

		//if (accountList.size() > 0) {
			Map toHost = new HashMap();
			Map fromHost = new HashMap();
			// add by mxl 0102 闁跨喐鏋婚幏鐑芥晸閹恒儴顕滈幏鐤嚄闁跨喐鏋婚幏宄板緝闁跨喐鏋婚幏绌媜rpId
			toHost.put("ACCOUNT_CORPORATION_ID", getCifNoByCorpId(corpId));
			//toHost.put("ACCOUNT_LIST", accountList);

			// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁悮瀛樺闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�(APPLICATION_CODE)
			/*String appCode = "";
			if (accType.equals(CorpAccount.ACCOUNT_TYPE_CURRENT)) {
				appCode = CibTransClient.APPCODE_CURRENT_ACCOUNT;
			} else if (accType.equals(CorpAccount.ACCOUNT_TYPE_OVER_DRAFT)) {
				appCode = CibTransClient.APPCODE_OVERDRAFT_ACCOUNT;
			} else if (accType.equals(CorpAccount.ACCOUNT_TYPE_TIME_DEPOSIT)) {
				appCode = CibTransClient.APPCODE_TIME_DEPOSIT;
			} else if (accType.equals(CorpAccount.ACCOUNT_TYPE_LOAN)) {
				appCode = CibTransClient.APPCODE_LOAN_ACCOUNT;
			} else if (accType.equals(CorpAccount.ACCOUNT_TYPE_SAVING)) {
				appCode = CibTransClient.APPCODE_SAVING_ACCOUNT; // add by mxl
																	// 1120
			} else if (accType.equals(CorpAccount.ACCOUNT_TYPE_CREDIT_VISA)) {
				appCode = CibTransClient.APPCODE_CREDIT_VISA; // add by chen_y 20160907 for CR192
			} else if (accType.equals(CorpAccount.ACCOUNT_TYPE_CREDIT_AE)) {
				appCode = CibTransClient.APPCODE_CREDIT_AE; // add by chen_y 20160907 for CR192
			} else if (accType.equals(CorpAccount.ACCOUNT_TYPE_CREDIT_MASTER)) {
				appCode = CibTransClient.APPCODE_CREDIT_MASTER; // add by chen_y 20160907 for CR192
			} 
			
			 * else if (accType.equals(CorpAccount.ACCOUNT_TYPE_CREDIT_MASTER))
			 * { appCode = CibTransClient.APPCODE_CREDIT_MASTER; //add by mxl
			 * 1120 } else if
			 * (accType.equals(CorpAccount.ACCOUNT_TYPE_CREDIT_AE)) { appCode =
			 * CibTransClient.APPCODE_CREDIT_AE; //add by mxl 1120 }
			 */
			//toHost.put("APPLICATION_CODE", appCode);
			// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁崣鐑芥交閹风兘鏁撻弬銈嗗(NO_OF_ACCOUNT)
			//toHost.put("NO_OF_ACCOUNT", String.valueOf(accountList.size()));

			// 闁跨喐鏋婚幏鐑芥晸閿熺祲eference No
			String refNo = CibIdGenerator.getRefNoForTransaction();
			// 闁跨喐鏋婚幏鐑芥晸閿熺锤lpha 8 闁跨喐鏋婚幏鐤祿闁跨噦鎷�
			testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
					CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);
			//
			//TESTTEST zj05
//			CibTransClient testClient2 = new CibTransClient("CIB", "ZJ05");
//			Map toHost1 = new HashMap();
//			Map fromHost1 = new HashMap();
//            toHost1.put("EFFECTIVE_DATE", "20171215");
//            fromHost1 = testClient2.doTransaction(toHost1);
			//
			fromHost = testClient.doTransaction(toHost);
			if (!testClient.isSucceed()) {
				throw new NTBHostException(testClient.getErrorArray());
			}

			UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);
			
			List ccyList = (List) fromHost.get("ACCOUNT_LIST");
			//add by wcc 20181101 begin
			String internalCounter = fromHost.get("INTERNAL_COUNTER").toString();
			int num = Integer.parseInt(internalCounter);
			Log.info("internal counter: " + num);
			//add by wcc 20181101 end
			List tempCcyList = new ArrayList();
			//for(int i =0;i<ccyList.size();i++){
			for(int i = 0; i < num;i++){	
				//modify by wcc 20180208
				Map map = (Map)ccyList.get(i);//new HashMap();
				
				String bussType = map.get("APPLICATION_CODE").toString();
				if(appCodeToBussType(appCode).equals(bussType)){
					tempCcyList.add(ccyList.get(i));
				}
			}
			return tempCcyList;
			
		//} else {
			//return accountList;
		//}
	}

	public List listCurrentAccount(CorpUser user, String corpId,String appCode,String prePage)
			throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();

		CibTransClient testClient = new CibTransClient("CIB", "ZC02");
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService accService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		// 闁跨喐鏋婚幏鐑芥晸缁叉rpId 娑撴椽鏁撶粔闈╃秶閹风兘鏁撻弬銈嗗闁鏁撻弬銈嗗闁跨喐鏋婚幏宄板緝闁跨喐鏋婚幏鐑芥晸缂佺偟灏ㄩ幏锟�
		//modify by wcc 20180209
//		List accountList = new ArrayList();
//		if (corpId == null || user.getCorpId().equals(corpId)) {
//			List priviledgeList = new ArrayList();
//			priviledgeList = accService.listPrivilegedAccount(user,
//					CorpAccount.ACCOUNT_TYPE_CURRENT);
//			for (int i = 0; i < priviledgeList.size(); i++) {
//				CorpAccount accObj = (CorpAccount) priviledgeList.get(i);
//				Map accountItem = new HashMap();
//				accountItem.put("ACCOUNT_NO", accObj.getAccountNo());
//				accountList.add(accountItem);
//			}
//		} else {
//			// 闁跨喐鏋婚幏鐑芥晸缁叉rpId 闁跨喐鏋婚幏铚傝礋闁跨喓笑閿濆繑瀚归柨鐔告灮閹风兘锟絚orpId闁跨喐鏋婚幏宄扮安闁跨喐鏋婚幏宄板緝闁跨喐鏋婚幏鐑芥晸缂佺偟灏ㄩ幏锟�
//			List corpAccList = new ArrayList();
//			corpAccList = accService.listCorpAccountByAccType(corpId,
//					CorpAccount.ACCOUNT_TYPE_CURRENT);
//			for (int i = 0; i < corpAccList.size(); i++) {
//				CorpAccount accObj = (CorpAccount) corpAccList.get(i);
//				Map accountItem = new HashMap();
//				accountItem.put("ACCOUNT_NO", accObj.getAccountNo());
//				accountList.add(accountItem);
//			}
//		}
//		if (accountList.size() > 0) {
//			toHost.put("ACCOUNT_LIST", accountList);
			toHost.put("CI_NO",getCifNoByCorpId(corpId));
			// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁悮瀛樺闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�(APPLICATION_CODE)
			//String appCode = CibTransClient.APPCODE_CURRENT_ACCOUNT;
			toHost.put("AC_TYP", appCodeToBussType(appCode));
			// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁崣鐑芥交閹风兘鏁撻弬銈嗗(NO_OF_ACCOUNT)
			//toHost.put("NO_OF_ACCOUNT", String.valueOf(accountList.size()));
			toHost.put("TURN_KEY",prePage);

			// 闁跨喐鏋婚幏鐑芥晸閿熺祲eference No
			String refNo = CibIdGenerator.getRefNoForTransaction();
			// 闁跨喐鏋婚幏鐑芥晸閿熺锤lpha 8 闁跨喐鏋婚幏鐤祿闁跨噦鎷�
			testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
					CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);

			// 闁跨喐鏋婚幏鐑芥晸闁颁絻顕滈幏鐑芥晸閺傘倖瀚归柨鐔告緝閸戙倖瀚归柨鐔告灮閹凤拷
			fromHost = testClient.doTransaction(toHost);
			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归幘鐐烘晸閺傘倖瀚归弲鎺楁晸閺傘倖瀚归搹顔炴棃鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹凤拷
			if (!testClient.isSucceed()) {
				throw new NTBHostException(testClient.getErrorArray());
			}
			// add by mxl 0818 insert a row into the table RP_ACCTENQ
			UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);

			List retList = (List) fromHost.get("ACCOUNT_LIST");
			retList.add(fromHost.get("NEXT_KEY").toString());
			return (retList);
		//} else {
			//return accountList;
		//}
	}

	public List listSavingAccount(CorpUser user, String corpId,String appCode,String prePage)
			throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();

		CibTransClient testClient = new CibTransClient("CIB", "ZC02");
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService accService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		// 闁跨喐鏋婚幏鐑芥晸缁叉rpId 娑撴椽鏁撶粔闈╃秶閹风兘鏁撻弬銈嗗闁鏁撻弬銈嗗闁跨喐鏋婚幏宄板緝闁跨喐鏋婚幏鐑芥晸缂佺偟灏ㄩ幏锟�
		//modify by wcc 20180209
//		List accountList = new ArrayList();
//		if (corpId == null || user.getCorpId().equals(corpId)) {
//			List priviledgeList = new ArrayList();
//			priviledgeList = accService.listPrivilegedAccount(user,
//					CorpAccount.ACCOUNT_TYPE_SAVING);
//			for (int i = 0; i < priviledgeList.size(); i++) {
//				CorpAccount accObj = (CorpAccount) priviledgeList.get(i);
//				Map accountItem = new HashMap();
//				accountItem.put("ACCOUNT_NO", accObj.getAccountNo());
//				accountList.add(accountItem);
//			}
//		} else {
//			// 闁跨喐鏋婚幏鐑芥晸缁叉rpId 闁跨喐鏋婚幏铚傝礋闁跨喓笑閿濆繑瀚归柨鐔告灮閹风兘锟絚orpId闁跨喐鏋婚幏宄扮安闁跨喐鏋婚幏宄板緝闁跨喐鏋婚幏鐑芥晸缂佺偟灏ㄩ幏锟�
//			List corpAccList = new ArrayList();
//			corpAccList = accService.listCorpAccountByAccType(corpId,
//					CorpAccount.ACCOUNT_TYPE_SAVING);
//			for (int i = 0; i < corpAccList.size(); i++) {
//				CorpAccount accObj = (CorpAccount) corpAccList.get(i);
//				Map accountItem = new HashMap();
//				accountItem.put("ACCOUNT_NO", accObj.getAccountNo());
//				accountList.add(accountItem);
//			}
//		}
//		if (accountList.size() > 0) {
//			toHost.put("ACCOUNT_LIST", accountList);
			toHost.put("CI_NO",getCifNoByCorpId(corpId));
			// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁悮瀛樺闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�(APPLICATION_CODE)
			//String appCode = CibTransClient.APPCODE_SAVING_ACCOUNT;
			//toHost.put("APPLICATION_CODE", appCode);
			toHost.put("AC_TYP", appCodeToBussType(appCode));
			// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁崣鐑芥交閹风兘鏁撻弬銈嗗(NO_OF_ACCOUNT)
			//toHost.put("NO_OF_ACCOUNT", String.valueOf(accountList.size()));
			toHost.put("TURN_KEY",prePage);

			// 闁跨喐鏋婚幏鐑芥晸閿熺祲eference No
			String refNo = CibIdGenerator.getRefNoForTransaction();
			// 闁跨喐鏋婚幏鐑芥晸閿熺锤lpha 8 闁跨喐鏋婚幏鐤祿闁跨噦鎷�
			testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
					CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);

			// 闁跨喐鏋婚幏鐑芥晸闁颁絻顕滈幏鐑芥晸閺傘倖瀚归柨鐔告緝閸戙倖瀚归柨鐔告灮閹凤拷
			fromHost = testClient.doTransaction(toHost);
			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归幘鐐烘晸閺傘倖瀚归弲鎺楁晸閺傘倖瀚归搹顔炴棃鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹凤拷
			if (!testClient.isSucceed()) {
				throw new NTBHostException(testClient.getErrorArray());
			}
			// add by mxl 0818 insert a row into the table RP_ACCTENQ
			UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);

			List retList = (List) fromHost.get("ACCOUNT_LIST");
			retList.add(fromHost.get("NEXT_KEY").toString());
			return (retList);
		//} else {
			//return accountList;
		//}

	}

	public List listTimeDeposit(CorpUser user, String corpId,String appCode,String prePage)
			throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();

		CibTransClient testClient = new CibTransClient("CIB", "ZC03");
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService accService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		// 闁跨喐鏋婚幏鐑芥晸缁叉rpId 娑撴椽鏁撶粔闈╃秶閹风兘鏁撻弬銈嗗闁鏁撻弬銈嗗闁跨喐鏋婚幏宄板緝闁跨喐鏋婚幏鐑芥晸缂佺偟灏ㄩ幏锟�
		//modify by wcc 20180209
//		List accountList = new ArrayList();
//		if (corpId == null || user.getCorpId().equals(corpId)) {
//			List priviledgeList = new ArrayList();
//			priviledgeList = accService.listPrivilegedAccount(user,
//					CorpAccount.ACCOUNT_TYPE_TIME_DEPOSIT);
//			for (int i = 0; i < priviledgeList.size(); i++) {
//				CorpAccount accObj = (CorpAccount) priviledgeList.get(i);
//				Map accountItem = new HashMap();
//				accountItem.put("ACCOUNT_NO", accObj.getAccountNo());
//				accountList.add(accountItem);
//			}
//		} else {
//			// 闁跨喐鏋婚幏鐑芥晸缁叉rpId 闁跨喐鏋婚幏铚傝礋闁跨喓笑閿濆繑瀚归柨鐔告灮閹风兘锟絚orpId闁跨喐鏋婚幏宄扮安闁跨喐鏋婚幏宄板緝闁跨喐鏋婚幏鐑芥晸缂佺偟灏ㄩ幏锟�
//			List corpAccList = new ArrayList();
//			corpAccList = accService.listCorpAccountByAccType(corpId,
//					CorpAccount.ACCOUNT_TYPE_TIME_DEPOSIT);
//			for (int i = 0; i < corpAccList.size(); i++) {
//				CorpAccount accObj = (CorpAccount) corpAccList.get(i);
//				Map accountItem = new HashMap();
//				accountItem.put("ACCOUNT_NO", accObj.getAccountNo());
//				accountList.add(accountItem);
//			}
//		}
//		if (accountList.size() > 0) {
//			toHost.put("ACCOUNT_LIST", accountList);
			toHost.put("CI_NO",getCifNoByCorpId(corpId));
			// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁悮瀛樺闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�(APPLICATION_CODE)
			//String appCode = CibTransClient.APPCODE_TIME_DEPOSIT;
			//toHost.put("APPLICATION_CODE", appCode);
			toHost.put("AC_TYP", appCodeToBussType(appCode));
			// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁崣鐑芥交閹风兘鏁撻弬銈嗗(NO_OF_ACCOUNT)
			//toHost.put("NO_OF_ACCOUNT", String.valueOf(accountList.size()));
			toHost.put("TURN_KEY",Utils.null2EmptyWithTrim(prePage));
			// 闁跨喐鏋婚幏鐑芥晸閿熺祲eference No
			String refNo = CibIdGenerator.getRefNoForTransaction();
			// 闁跨喐鏋婚幏鐑芥晸閿熺锤lpha 8 闁跨喐鏋婚幏鐤祿闁跨噦鎷�
			testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
					CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);
			// 闁跨喐鏋婚幏鐑芥晸闁颁絻顕滈幏鐑芥晸閺傘倖瀚归柨鐔告緝閸戙倖瀚归柨鐔告灮閹凤拷
			fromHost = testClient.doTransaction(toHost);
			if (!testClient.isSucceed()) {
				throw new NTBHostException(testClient.getErrorArray());
			}
			// add by mxl 0818 insert a row into the table RP_ACCTENQ
			UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);

			List retList = (List) fromHost.get("ACCOUNT_LIST");
			retList.add(fromHost.get("NEXT_KEY").toString());
			return (retList);
		//} else {
			//return accountList;
		//}
	}

	public List listOverdraftAccount(CorpUser user, String corpId,String appCode,String prePage)
			throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();

		CibTransClient testClient = new CibTransClient("CIB", "ZC02");
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService accService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		// 闁跨喐鏋婚幏鐑芥晸缁叉rpId 娑撴椽鏁撶粔闈╃秶閹风兘鏁撻弬銈嗗闁鏁撻弬銈嗗闁跨喐鏋婚幏宄板緝闁跨喐鏋婚幏鐑芥晸缂佺偟灏ㄩ幏锟�
		//modify by wcc 20180209
//		List accountList = new ArrayList();
//		if (corpId == null || user.getCorpId().equals(corpId)) {
//			List priviledgeList = new ArrayList();
//			priviledgeList = accService.listPrivilegedAccount(user,
//					CorpAccount.ACCOUNT_TYPE_OVER_DRAFT);
//			for (int i = 0; i < priviledgeList.size(); i++) {
//				CorpAccount accObj = (CorpAccount) priviledgeList.get(i);
//				Map accountItem = new HashMap();
//				accountItem.put("ACCOUNT_NO", accObj.getAccountNo());
//				accountList.add(accountItem);
//			}
//		} else {
//			// 闁跨喐鏋婚幏鐑芥晸缁叉rpId 闁跨喐鏋婚幏铚傝礋闁跨喓笑閿濆繑瀚归柨鐔告灮閹风兘锟絚orpId闁跨喐鏋婚幏宄扮安闁跨喐鏋婚幏宄板緝闁跨喐鏋婚幏鐑芥晸缂佺偟灏ㄩ幏锟�
//			List corpAccList = new ArrayList();
//			corpAccList = accService.listCorpAccountByAccType(corpId,
//					CorpAccount.ACCOUNT_TYPE_OVER_DRAFT);
//			for (int i = 0; i < corpAccList.size(); i++) {
//				CorpAccount accObj = (CorpAccount) corpAccList.get(i);
//				Map accountItem = new HashMap();
//				accountItem.put("ACCOUNT_NO", accObj.getAccountNo());
//				accountList.add(accountItem);
//			}
//		}
//		if (accountList.size() > 0) {
//			toHost.put("ACCOUNT_LIST", accountList);
			toHost.put("CI_NO",getCifNoByCorpId(corpId));
			// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁悮瀛樺闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�(APPLICATION_CODE)
			//String appCode = CibTransClient.APPCODE_OVERDRAFT_ACCOUNT;
			//toHost.put("APPLICATION_CODE", appCode);
			toHost.put("AC_TYP", appCodeToBussType(appCode));
			// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁崣鐑芥交閹风兘鏁撻弬銈嗗(NO_OF_ACCOUNT)
			//toHost.put("NO_OF_ACCOUNT", String.valueOf(accountList.size()));
			toHost.put("TURN_KEY",prePage);
			// 闁跨喐鏋婚幏鐑芥晸閿熺祲eference No
			String refNo = CibIdGenerator.getRefNoForTransaction();
			// 闁跨喐鏋婚幏鐑芥晸閿熺锤lpha 8 闁跨喐鏋婚幏鐤祿闁跨噦鎷�
			testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
					CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);
			// 闁跨喐鏋婚幏鐑芥晸闁颁絻顕滈幏鐑芥晸閺傘倖瀚归柨鐔告緝閸戙倖瀚归柨鐔告灮閹凤拷
			fromHost = testClient.doTransaction(toHost);
			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归幘鐐烘晸閺傘倖瀚归弲鎺楁晸閺傘倖瀚归搹顔炴棃鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹凤拷
			if (!testClient.isSucceed()) {
				throw new NTBHostException(testClient.getErrorArray());
			}
			// add by mxl 0818 insert a row into the table RP_ACCTENQ
			UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);

			List retList = (List) fromHost.get("ACCOUNT_LIST");
			retList.add(fromHost.get("NEXT_KEY").toString());
			return (retList);
		//} else {
			//return accountList;
		//}
	}

	public List listLoanAccount(CorpUser user, String corpId,String appCode,String prePage)
			throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();

		// CorpUser userObj = (CorpUser)this.getUser();
		CibTransClient testClient = new CibTransClient("CIB", "ZC04");
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService accService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		// 闁跨喐鏋婚幏鐑芥晸缁叉rpId 娑撴椽鏁撶粔闈╃秶閹风兘鏁撻弬銈嗗闁鏁撻弬銈嗗闁跨喐鏋婚幏宄板緝闁跨喐鏋婚幏鐑芥晸缂佺偟灏ㄩ幏锟�
		//modify by wcc 20180209
//		List accountList = new ArrayList();
//		if (corpId == null || user.getCorpId().equals(corpId)) {
//			List priviledgeList = new ArrayList();
//			priviledgeList = accService.listPrivilegedAccount(user,
//					CorpAccount.ACCOUNT_TYPE_LOAN);
//			for (int i = 0; i < priviledgeList.size(); i++) {
//				CorpAccount accObj = (CorpAccount) priviledgeList.get(i);
//				Map accountItem = new HashMap();
//				accountItem.put("ACCOUNT_NO", accObj.getAccountNo());
//				accountList.add(accountItem);
//			}
//		} else {
//			// 闁跨喐鏋婚幏鐑芥晸缁叉rpId 闁跨喐鏋婚幏铚傝礋闁跨喓笑閿濆繑瀚归柨鐔告灮閹风兘锟絚orpId闁跨喐鏋婚幏宄扮安闁跨喐鏋婚幏宄板緝闁跨喐鏋婚幏鐑芥晸缂佺偟灏ㄩ幏锟�
//			List corpAccList = new ArrayList();
//			corpAccList = accService.listCorpAccountByAccType(corpId,
//					CorpAccount.ACCOUNT_TYPE_LOAN);
//			for (int i = 0; i < corpAccList.size(); i++) {
//				CorpAccount accObj = (CorpAccount) corpAccList.get(i);
//				Map accountItem = new HashMap();
//				accountItem.put("ACCOUNT_NO", accObj.getAccountNo());
//				accountList.add(accountItem);
//			}
//		}
//		if (accountList.size() > 0) {
//			toHost.put("ACCOUNT_LIST", accountList);
			toHost.put("CI_NO",getCifNoByCorpId(corpId));
			// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁悮瀛樺闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�(APPLICATION_CODE)
			//String appCode = CibTransClient.APPCODE_LOAN_ACCOUNT;
			//toHost.put("APPLICATION_CODE", appCode);
			toHost.put("AC_TYP", appCodeToBussType(appCode));
			// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁崣鐑芥交閹风兘鏁撻弬銈嗗(NO_OF_ACCOUNT)
			//toHost.put("NO_OF_ACCOUNT", String.valueOf(accountList.size()));
			toHost.put("TURN_KEY",prePage);
			// 闁跨喐鏋婚幏鐑芥晸閿熺祲eference No
			String refNo = CibIdGenerator.getRefNoForTransaction();
			// 闁跨喐鏋婚幏鐑芥晸閿熺锤lpha 8 闁跨喐鏋婚幏鐤祿闁跨噦鎷�
			testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
					CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);
			// 闁跨喐鏋婚幏鐑芥晸闁颁絻顕滈幏鐑芥晸閺傘倖瀚归柨鐔告緝閸戙倖瀚归柨鐔告灮閹凤拷
			fromHost = testClient.doTransaction(toHost);
			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归幘鐐烘晸閺傘倖瀚归弲鎺楁晸閺傘倖瀚归搹顔炴棃鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹凤拷
			if (!testClient.isSucceed()) {
				throw new NTBHostException(testClient.getErrorArray());
			}
			// add by mxl 0818 insert a row into the table RP_ACCTENQ
			UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);

			List retList = (List) fromHost.get("ACCOUNT_LIST");
			retList.add(fromHost.get("NEXT_KEY").toString());
			return (retList);
		//} else {
			//return accountList;
		//}
	}

	public Map viewCurrentDetail(CorpUser user, String accountNo)
			throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		// 闁跨喐鏋婚幏宄邦瀶闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏锟�
		CibTransClient testClient = new CibTransClient("CIB", "0195");

		// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁悮瀛樺闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�(APPLICATION_CODE)
		toHost.put("ACCOUNT_NO", accountNo);

		// 闁跨喐鏋婚幏鐑芥晸閿熺祲eference No
		String refNo = CibIdGenerator.getRefNoForTransaction();
		// 闁跨喐鏋婚幏鐑芥晸閿熺锤lpha 8 闁跨喐鏋婚幏鐤祿闁跨噦鎷�
		testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);
		// 闁跨喐鏋婚幏鐑芥晸闁颁絻顕滈幏鐑芥晸閺傘倖瀚归柨鐔告緝閸戙倖瀚归柨鐔告灮閹凤拷
		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		// add by mxl 0818 insert a row into the table RP_ACCTENQ
		UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);
		return fromHost;
	}
	
	public Map viewCurrentDetail(CorpUser user, String accountNo, String from_ccy)
	throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		// 闁跨喐鏋婚幏宄邦瀶闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏锟�
		CibTransClient testClient = new CibTransClient("CIB", "0195");
		
		// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁悮瀛樺闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�(APPLICATION_CODE)
		toHost.put("ACCOUNT_NO", accountNo);
		toHost.put("CCY", from_ccy);
		// 闁跨喐鏋婚幏鐑芥晸閿熺祲eference No
		String refNo = CibIdGenerator.getRefNoForTransaction();
		// 闁跨喐鏋婚幏鐑芥晸閿熺锤lpha 8 闁跨喐鏋婚幏鐤祿闁跨噦鎷�
		testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);
		// 闁跨喐鏋婚幏鐑芥晸闁颁絻顕滈幏鐑芥晸閺傘倖瀚归柨鐔告緝閸戙倖瀚归柨鐔告灮閹凤拷
		fromHost = testClient.doTransaction(toHost);
		// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归幘鐐烘晸閺傘倖瀚归弲鎺楁晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐕傛嫹
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		// add by mxl 0818 insert a row into the table RP_ACCTENQ
		UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);
		return fromHost;
	}

	public Map viewSavingDetail(CorpUser user, String accountNo)
			throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		// 闁跨喐鏋婚幏宄邦瀶闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏锟�
		CibTransClient testClient = new CibTransClient("CIB", "0295");

		// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁悮瀛樺闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�(APPLICATION_CODE)
		toHost.put("ACCOUNT_NO", accountNo);

		// 闁跨喐鏋婚幏鐑芥晸閿熺祲eference No
		String refNo = CibIdGenerator.getRefNoForTransaction();
		// 闁跨喐鏋婚幏鐑芥晸閿熺锤lpha 8 闁跨喐鏋婚幏鐤祿闁跨噦鎷�
		testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);
		// 闁跨喐鏋婚幏鐑芥晸闁颁絻顕滈幏鐑芥晸閺傘倖瀚归柨鐔告緝閸戙倖瀚归柨鐔告灮閹凤拷
		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		// add by mxl 0818 insert a row into the table RP_ACCTENQ
		UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);
		return fromHost;

	}
	
	public Map viewSavingDetail(CorpUser user, String accountNo,String fromCCY)
	throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		// 闁跨喐鏋婚幏宄邦瀶闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏锟�
		CibTransClient testClient = new CibTransClient("CIB", "0295");
		
		// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁悮瀛樺闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�(APPLICATION_CODE)
		toHost.put("ACCOUNT_NO", accountNo);
		toHost.put("CCY", fromCCY);
		
		// 闁跨喐鏋婚幏鐑芥晸閿熺祲eference No
		String refNo = CibIdGenerator.getRefNoForTransaction();
		// 闁跨喐鏋婚幏鐑芥晸閿熺锤lpha 8 闁跨喐鏋婚幏鐤祿闁跨噦鎷�
		testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);
		// 闁跨喐鏋婚幏鐑芥晸闁颁絻顕滈幏鐑芥晸閺傘倖瀚归柨鐔告緝閸戙倖瀚归柨鐔告灮閹凤拷
		fromHost = testClient.doTransaction(toHost);
		// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归幘鐐烘晸閺傘倖瀚归弲鎺楁晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐕傛嫹
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		// add by mxl 0818 insert a row into the table RP_ACCTENQ
		UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);
		return fromHost;
		
	}

	public Map viewDepositDetial(CorpUser user, String accountNo)
			throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		// 闁跨喐鏋婚幏宄邦瀶闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏锟�
		CibTransClient testClient = new CibTransClient("CIB", "0395");

		// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁悮瀛樺闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�(APPLICATION_CODE)
		toHost.put("ACCOUNT_NO", accountNo);

		// 闁跨喐鏋婚幏鐑芥晸閿熺祲eference No
		String refNo = CibIdGenerator.getRefNoForTransaction();
		// 闁跨喐鏋婚幏鐑芥晸閿熺锤lpha 8 闁跨喐鏋婚幏鐤祿闁跨噦鎷�
		testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);
		// 闁跨喐鏋婚幏鐑芥晸闁颁絻顕滈幏鐑芥晸閺傘倖瀚归柨鐔告緝閸戙倖瀚归柨鐔告灮閹凤拷
		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		// add by mxl 0818 insert a row into the table RP_ACCTENQ
		UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);

		return fromHost;
	}

	public Map viewOverdraftDetial(CorpUser user, String accountNo)
			throws NTBException {

		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		CibTransClient testClient = new CibTransClient("CIB", "0195");

		toHost.put("ACCOUNT_NO", accountNo);

		String refNo = CibIdGenerator.getRefNoForTransaction();
		testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);
		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		// add by mxl 0818 insert a row into the table RP_ACCTENQ
		UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);

		return fromHost;
	}
	
	public Map viewOverdraftDetial(CorpUser user, String accountNo,String fromCCY)
	throws NTBException {

		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		CibTransClient testClient = new CibTransClient("CIB", "0195");
		
		toHost.put("ACCOUNT_NO", accountNo);
		toHost.put("CCY", fromCCY);
		String refNo = CibIdGenerator.getRefNoForTransaction();
		testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);
		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		// add by mxl 0818 insert a row into the table RP_ACCTENQ
		UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);
		
		return fromHost;
	}

	public Map viewLoanDetail(CorpUser user, String accountNo)
			throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		CibTransClient testClient = new CibTransClient("CIB", "0495");

		toHost.put("ACCOUNT_NO", accountNo);

		String refNo = CibIdGenerator.getRefNoForTransaction();
		testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);
		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}

		// add by mxl 0818 insert a row into the table RP_ACCTENQ
		UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);

		return fromHost;
	}
	//modify by wcc 20180123
/*	public List listTransHistory(CorpUser user, String accountNo)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService corpAccService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		CibTransClient testClient = null;

		// modify for checking saving account add by mxl 1121
		if (corpAccService.isSavingAccount(accountNo)) {
			testClient = new CibTransClient("CIB", "ZB10");
		} else {
			testClient = new CibTransClient("CIB", "ZB11");
		}

		toHost.put("ACCOUNT_NO", accountNo);

		String refNo = CibIdGenerator.getRefNoForTransaction();
		testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);

		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);

		List retList = (List) fromHost.get("CUSTOMER_LIST");
		return (retList);
	}*/
	public List listTransHistory(CorpUser user, String accountNo,long dateFrom,long dateTo,String prePage,String ccy)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService corpAccService = (CorpAccountService)appContext.getBean("CorpAccountService");
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		 CibTransClient testClient = null;
		
		//  modify for checking saving account add by mxl 1121
		/*if (corpAccService.isSavingAccount(accountNo)){
		    testClient = new CibTransClient("CIB", "ZB10");
	    } else {
	        testClient = new CibTransClient("CIB", "ZB11");
	    }*/
	    testClient = new CibTransClient("CIB", "ZB10");
		
		toHost.put("ACCOUNT_NO", accountNo);
		toHost.put("CCY", ccy);
		//modify by wcc 20180123
		toHost.put("from_date", dateFrom);
		toHost.put("to_date", dateTo);
		//modify by wcc 20180115
		if(prePage.equals("")){
			prePage = Utils.appendSpace("", 80);
			toHost.put("ST_KEY",prePage);
		}else{
			toHost.put("ST_KEY",Utils.appendSpace(prePage.trim(),80));
		}
		toHost.put("SORT_MTH", "D");
		String refNo = CibIdGenerator.getRefNoForTransaction();
		testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);
		
		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);
		
		List retList = (List) fromHost.get("CUSTOMER_LIST");
		
		retList.add(fromHost.get("QHIS_NEXT_KEY").toString());
		return (retList);
	}
	//add by linrui 20190918 for CR589
	public List listTransHistory(CorpUser user, String accountNo,long dateFrom,long dateTo,String prePage,String ccy,String sortOrder)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService corpAccService = (CorpAccountService)appContext.getBean("CorpAccountService");
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		CibTransClient testClient = null;
		
		//  modify for checking saving account add by mxl 1121
		/*if (corpAccService.isSavingAccount(accountNo)){
		    testClient = new CibTransClient("CIB", "ZB10");
	    } else {
	        testClient = new CibTransClient("CIB", "ZB11");
	    }*/
		testClient = new CibTransClient("CIB", "ZB10");
		
		toHost.put("ACCOUNT_NO", accountNo);
		toHost.put("CCY", ccy);
		//modify by wcc 20180123
		toHost.put("from_date", dateFrom);
		toHost.put("to_date", dateTo);
		//modify by wcc 20180115
		if(prePage.equals("")){
			prePage = Utils.appendSpace("", 80);
			toHost.put("ST_KEY",prePage);
		}else{
			toHost.put("ST_KEY",Utils.appendSpace(prePage.trim(),80));
		}
		toHost.put("SORT_MTH", sortOrder.equals("1")?"D":"A");
		String refNo = CibIdGenerator.getRefNoForTransaction();
		testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);
		
		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);
		
		List retList = (List) fromHost.get("CUSTOMER_LIST");
		
		retList.add(fromHost.get("QHIS_NEXT_KEY").toString());
		
		//add by lzg 20191018
		for (int i = 0; i < retList.size()-1; i++) {
			Map retMap = (HashMap)retList.get(i);
			if(retMap.get("SERIAL_NO") != null){
				String serialNumber = retMap.get("SERIAL_NO").toString();
				if(serialNumber.trim() != ""){
					int len = 9-serialNumber.length();
					serialNumber = String.format("%09d", Integer.parseInt(serialNumber));
					retMap.put("SERIAL_NO", serialNumber);
				}
			}
			if(retMap.get("QHIS_TX_TIME") != null){
				String transactionTime = retMap.get("QHIS_TX_TIME").toString();
				if(transactionTime.length() == 6){
					transactionTime = transactionTime.substring(0,2) + ":" + transactionTime.substring(2,4) + ":" + transactionTime.substring(4);
					retMap.put("QHIS_TX_TIME", transactionTime);
				}else{
					transactionTime = "";
					retMap.put("QHIS_TX_TIME", "");
				}
			}
			
		}
		//add by lzg end
		
		return (retList);
	}

	public List listTransHistoryFromDB(Map condition) throws NTBException {
		StringBuffer txnHistory = new StringBuffer(
				"select APP_CODE, CARD_NO, DR_CR_CODE, POSTING_DATE as POST_DATE, EFFECTIVE_DATE,"
						+ "TRANS_AMT as POST_AMOUNT,TRANS_CCY, TRANS_SOURCE as TRANSACTION_SOURCE, TRANS_NATURE as TRANSACTION_NATURE, "
						+ "TELLER_NO, SEQ_NO as TELLER_SEQ, TRANS_DESCRIPTION as DESCRIPTION, TRANS_NO as RECORD_NO, BATCH_SEQ_NO, "
						+ "REMARK,ORIGINAL_CURRENCY,ORIGINAL_AMOUNT " // add by hjs
						+ "from HS_TRANSACTION_HISTORY where ");
		List conditionList = new ArrayList();
		Set conditionKeySet = condition.keySet();
		for (Iterator it = conditionKeySet.iterator(); it.hasNext();) {
			String keyWord = (String) it.next();
			txnHistory.append(keyWord);
			// REMARK 闁跨喐鏋婚幏鐑芥晸鐟欙絽顦甸柨鐔告灮閹凤拷
			if (keyWord.equals("REMARK")) {
				txnHistory.append(" LIKE '%").append(condition.get(keyWord))
						.append("%' ");
				// }else if(keyWord.equals("EFFECTIVE_DATE")){
				// txnHistory.append("<=? ");
			} else {
				txnHistory.append("=? ");
				Object value = condition.get(keyWord);
				conditionList.add(value);
			}
			if (it.hasNext()) {
				txnHistory.append(" and ");
			}

		}
		// modify by hjs
		// txnHistory.append(" Order by POST_DATE DESC, EFFECTIVE_DATE DESC, TRANS_NO DESC");
		// Jet modified - only sort by post_date,transaction within a day
		txnHistory.append(" Order by POST_DATE DESC, RECORD_NO DESC");
		Object[] conditionObj = conditionList.toArray();
		try {
			List bankLabelList = genericJdbcDao.query(txnHistory.toString(),
					conditionObj);
			return bankLabelList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * listFromDB 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚� listFromHost 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�
	 * 
	 * add by hjs 2006-09-30
	 */
	public List filter(List listFromDB, List listFromHost) throws NTBException {
		List removedList = new ArrayList();

		Map dbData = null;
		Map hostData = null;
		for (int i=0; i<listFromHost.size(); i++) {
			hostData = (Map) listFromHost.get(i);
			/*String pDateHost = DateTime.formatDate(hostData.get("POST_DATE").toString(), "MMddyy", "yyyyMMdd");
			String eDateHost = DateTime.formatDate(hostData.get("EFFECTIVE_DATE").toString(), "MMddyy", "yyyyMMdd");*/
			String pDateHost = hostData.get("POST_DATE").toString();
			String eDateHost = hostData.get("EFFECTIVE_DATE").toString();
			String tNoHost = hostData.get("TELLER_NO").toString();
			String sNoHost = hostData.get("TELLER_SEQ").toString();
			// reset the date format to host data
			hostData.put("POST_DATE", pDateHost);
			hostData.put("EFFECTIVE_DATE", eDateHost);
			//modify by wcc 20180123 闁烩晩鍠栨晶鐘诲矗椤忓洩娉查柟鎭掑劚瑜版盯鏁嶇仦鎯ь暡濞寸姰鍎遍惃銏ゅ蓟閵夘煈鍤勯柡浣哄瀹撲線鏌堥懞銉︽殘闂佽鎷�
			/*for (int j=0; j<listFromDB.size(); j++) {
				dbData = (Map) listFromDB.get(j);
				String pDateDb = dbData.get("POST_DATE").toString();
				String eDateDb = dbData.get("EFFECTIVE_DATE").toString();
				String tNoDb = dbData.get("TELLER_NO").toString();
				String sNoDb = dbData.get("TELLER_SEQ").toString();

				if ( //闁鏁撻弬銈嗗闁跨喐鍩呴棃鈺傚闁跨喐鏋婚幏鐑芥晸閿燂拷						pDateDb.equals(pDateHost)
						&& eDateDb.equals(eDateHost)
						&& tNoDb.equals(tNoHost)
						&& sNoDb.equals(sNoHost)
				) {
					removedList.add(hostData);
					break;
				}
			}*/
		}
		//閸掔娀鏁撻弬銈嗗闁跨喐鍩呴棃鈺傚闁跨喐鏋婚幏鐑芥晸閿燂拷		listFromHost.removeAll(removedList);
		//闁跨喐鏋婚幏鐑芥晸閺傘倖瀚� java.util.Collections.reverse(listFromHost);
		
		//闁跨喐鏋婚幏鐑芥晸閿熶粙鏁撻弬銈嗗list
		List retList = new ArrayList();
		//modify by wcc 20180123 闁烩晩鍠栨晶鐘诲矗椤忓洩娉查柟鎭掑劚瑜版盯鏁嶇仦鎯ь暡濞寸姰鍎遍惃銏ゅ蓟閵夘煈鍤勯柡浣哄瀹撲線鏌堥懞銉︽殘闂佽鎷�
		//retList.addAll(listFromDB);
		retList.addAll(listFromHost);
		//闁跨喐鏋婚幏鐑芥晸濞层儻绱為敐蹇斿闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�
		// Jet modified - only sort by post_date
//		retList = sorting.sortByMapList(retList,
//				new String[]{"POST_DATE", "EFFECTIVE_DATE", "TELLER_NO", "TELLER_SEQ"},
//				Sorting.SORT_TYPE_DESC); //闁跨喐R鏋婚幏鐑芥晸閺傘倖瀚�
		/*Sorting.sortMapList(retList,
				new String[]{"POST_DATE", "RECORD_NO"}, Sorting.SORT_TYPE_DESC); */

		return retList;
	}

	public Map viewInwardInfo(String key) throws NTBException {
		String[] keys = key.split(",");
		String sql = "select * from HS_INWARD_REMITTANCE_INFO where ACCOUNT_NO = ? and POSTING_DATE = ? and EFFECTIVE_DATE = ? and TELLER_NO = ? and SEQ_NO = ? and TRANS_NO = ?";

		try {
			Map row = genericJdbcDao.querySingleRow(sql, new Object[] {
					keys[0], keys[1], keys[2], keys[3], keys[4], keys[5] });
			return row;
		} catch (Exception e) {
			Log.error(e.getMessage());
			throw new NTBException("err.enq.viewInwardInfoError");
		}
	}

	public String formatPeriod(String periodFromHost) throws NTBException {
		String s = periodFromHost;
		if (s.indexOf("M") > 0 || s.indexOf("m") > 0) {
			s = s.substring(0, s.length() - 1);
			int frequence = Integer.parseInt(s);
			if (frequence >= 12) {
				return String.valueOf(frequence / 12) + "Y";
			}
		}
		return periodFromHost;
	}

	/**
	 * add by long_zg 2014-12-22 for CR192 bob batch
	 */
	public List listCreditAccount(CorpUser user, String corpId)
			throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();

		CibTransClient testClient = new CibTransClient("CIB", "ZC14");
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService accService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		// 闁跨喐鏋婚幏鐑芥晸缁叉rpId 娑撴椽鏁撶粔闈╃秶閹风兘鏁撻弬銈嗗闁鏁撻弬銈嗗闁跨喐鏋婚幏宄板緝闁跨喐鏋婚幏鐑芥晸缂佺偟灏ㄩ幏锟�
		List accountList = new ArrayList();
		if (corpId == null || user.getCorpId().equals(corpId)) {
			List priviledgeList = new ArrayList();
			/*
			 * if(CibTransClient.APPCODE_CREDIT_VISA.equals(appCode)){
			 * priviledgeList = accService.listPrivilegedAccount(user,
			 * CorpAccount.ACCOUNT_TYPE_CREDIT_VISA); }
			 * if(CibTransClient.APPCODE_CREDIT_MASTER.equals(appCode)){
			 * priviledgeList = accService.listPrivilegedAccount(user,
			 * CorpAccount.ACCOUNT_TYPE_CREDIT_MASTER); }
			 * if(CibTransClient.APPCODE_CREDIT_AE.equals(appCode)){
			 * priviledgeList = accService.listPrivilegedAccount(user,
			 * CorpAccount.ACCOUNT_TYPE_CREDIT_AE); }
			 */
			priviledgeList = accService.listPrivilegedAccount(user,
					CorpAccount.ACCOUNT_TYPE_CREDIT);

			for (int i = 0; i < priviledgeList.size(); i++) {
				CorpAccount accObj = (CorpAccount) priviledgeList.get(i);
				Map accountItem = new HashMap();
				accountItem.put("CREDIT_CARD_NO", accObj.getAccountNo());
				accountList.add(accountItem);
			}
		} else {
			// 闁跨喐鏋婚幏鐑芥晸缁叉rpId 闁跨喐鏋婚幏铚傝礋闁跨喓笑閿濆繑瀚归柨鐔告灮閹风兘锟絚orpId闁跨喐鏋婚幏宄扮安闁跨喐鏋婚幏宄板緝闁跨喐鏋婚幏鐑芥晸缂佺偟灏ㄩ幏锟�
			List corpAccList = new ArrayList();
			/*
			 * if(CibTransClient.APPCODE_CREDIT_VISA.equals(appCode)){
			 * corpAccList = accService.listCorpAccountByAccType(corpId,
			 * CorpAccount.ACCOUNT_TYPE_CREDIT_VISA); }
			 * if(CibTransClient.APPCODE_CREDIT_MASTER.equals(appCode)){
			 * corpAccList = accService.listCorpAccountByAccType(corpId,
			 * CorpAccount.ACCOUNT_TYPE_CREDIT_MASTER); }
			 * if(CibTransClient.APPCODE_CREDIT_AE.equals(appCode)){ corpAccList
			 * = accService.listCorpAccountByAccType(corpId,
			 * CorpAccount.ACCOUNT_TYPE_CREDIT_AE); }
			 */
			corpAccList = accService.listCorpAccountByAccType(corpId,
					CorpAccount.ACCOUNT_TYPE_CREDIT);
			for (int i = 0; i < corpAccList.size(); i++) {
				CorpAccount accObj = (CorpAccount) corpAccList.get(i);
				Map accountItem = new HashMap();
				accountItem.put("CREDIT_CARD_NO", accObj.getAccountNo());
				accountList.add(accountItem);
			}
		}
		if (accountList.size() > 0) {
			toHost.put("ACCOUNT_LIST", accountList);
			Log.info("to Host ACCOUNT_LIST="+accountList);
			
			
			// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁崣鐑芥交閹风兘鏁撻弬銈嗗(NO_OF_ACCOUNT)
			toHost.put("NO_OF_ACCOUNT", String.valueOf(accountList.size()));
			
			Log.info("toHost="+toHost);
			// 闁跨喐鏋婚幏鐑芥晸閿熺祲eference No
			String refNo = CibIdGenerator.getRefNoForTransaction();
			// 闁跨喐鏋婚幏鐑芥晸閿熺锤lpha 8 闁跨喐鏋婚幏鐤祿闁跨噦鎷�
			testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
					CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);

			// 闁跨喐鏋婚幏鐑芥晸闁颁絻顕滈幏鐑芥晸閺傘倖瀚归柨鐔告緝閸戙倖瀚归柨鐔告灮閹凤拷
			fromHost = testClient.doTransaction(toHost);
			Log.info("fromHost="+fromHost);
			// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归幘鐐烘晸閺傘倖瀚归弲鎺楁晸閺傘倖瀚归搹顔炴棃鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹凤拷
			if (!testClient.isSucceed()) {
				throw new NTBHostException(testClient.getErrorArray());
			}
			// add by mxl 0818 insert a row into the table RP_ACCTENQ
			UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);

			List retList = (List) fromHost.get("ACCOUNT_LIST");
			return (retList);
		} else {
			return accountList;
		}
	}

	public Map viewCreditDetail(CorpUser user, String accountNo)
			throws NTBException {
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		// 闁跨喐鏋婚幏宄邦瀶闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏锟�
		CibTransClient testClient = new CibTransClient("CIB", "ZJ33");

		// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁悮瀛樺闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�(APPLICATION_CODE)
		toHost.put("CARD_NO", accountNo);

		// 闁跨喐鏋婚幏鐑芥晸閿熺祲eference No
		String refNo = CibIdGenerator.getRefNoForTransaction();
		// 闁跨喐鏋婚幏鐑芥晸閿熺锤lpha 8 闁跨喐鏋婚幏鐤祿闁跨噦鎷�
		testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);
		// 闁跨喐鏋婚幏鐑芥晸闁颁絻顕滈幏鐑芥晸閺傘倖瀚归柨鐔告緝閸戙倖瀚归柨鐔告灮閹凤拷
		fromHost = testClient.doTransaction(toHost);
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		// add by chen_y for CR192 log ACCOUNT_NO+APP_CODE into RP_ACCTENQ
		toHost.put("ACCOUNT_NO", accountNo);
		toHost.put("APPLICATION_CODE", getAppCode(accountNo));
		// add by mxl 0818 insert a row into the table RP_ACCTENQ
		UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);
		return fromHost;
	}
	
	private String getAppCode(String accountNo)
			throws NTBException {
		accountNo = Utils.prefixZero(Utils.null2EmptyWithTrim(accountNo), 20);
		String sql = "select APP_CODE from HS_CORP_ACCOUNT_LIST where ACCOUNT_NO = ? ";
		try {
			Map rowMap = this.genericJdbcDao.querySingleRow(sql, new Object[] {accountNo});
			return rowMap == null ? "" : rowMap.get("APP_CODE").toString();
		} catch (Exception e) {
			throw new NTBException("err.bnk.GetPrDescriptionException");
		}
	}

	/**
	 * add by long_zg 2015-01-16 for CR192  BATCH BOB
	 */
	public List listCreditTransHistory(CorpUser user, String accountNo)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorpAccountService corpAccService = (CorpAccountService) appContext
				.getBean("CorpAccountService");
		Map toHost = new HashMap();
		Map fromHost = new HashMap();
		CibTransClient testClient = null;

		// 闁跨喐鏋婚幏宄邦瀶闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻弬銈嗗闁跨喐鏋婚幏锟�
		// modify for checking saving account add by mxl 1121
		testClient = new CibTransClient("CIB", "ZC15");

		// 闁跨喐鏋婚幏宄板晸闁跨喓绮搁悮瀛樺闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�(APPLICATION_CODE)
		toHost.put("CARD_NO", accountNo);

		// 闁跨喐鏋婚幏鐑芥晸閿熺祲eference No
		String refNo = CibIdGenerator.getRefNoForTransaction();
		// 闁跨喐鏋婚幏鐑芥晸閿熺锤lpha 8 闁跨喐鏋婚幏鐤祿闁跨噦鎷�
		testClient.setAlpha8(user, CibTransClient.TXNNATURE_ENQUIRY,
				CibTransClient.ACCTYPE_OWNER_ACCOUNT, refNo);

		// 闁跨喐鏋婚幏鐑芥晸闁颁絻顕滈幏鐑芥晸閺傘倖瀚归柨鐔告緝閸戙倖瀚归柨鐔告灮閹凤拷
		fromHost = testClient.doTransaction(toHost);
		// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归幘鐐烘晸閺傘倖瀚归弲鎺楁晸閺傘倖瀚归搹顔炴棃鏁撻弬銈嗗闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹凤拷
		if (!testClient.isSucceed()) {
			throw new NTBHostException(testClient.getErrorArray());
		}
		
		// add by chen_y for CR192 log ACCOUNT_NO+APP_CODE into RP_ACCTENQ
		toHost.put("ACCOUNT_NO", accountNo);
		toHost.put("APPLICATION_CODE", getAppCode(accountNo));
		UploadReporter.writeAccEnquiryRpt(toHost, fromHost, user);

		List retList = (List) fromHost.get("CUSTOMER_LIST");
		return (retList);
	}

	public List filterCredit(List listFromDB, List listFromHost) throws NTBException {
		List removedList = new ArrayList();

		Map dbData = null;
		Map hostData = null;
		for (int i = 0; i < listFromHost.size(); i++) {
			hostData = (Map) listFromHost.get(i);
			String pDateHost = DateTime.formatDate(hostData.get("POST_DATE")
					.toString(), "yyyyMMdd", "yyyyMMdd");
			String dRCrCode = hostData.get("DR_CR_CODE").toString() ;
			String eDateHost = hostData.get("EFFECTIVE_DATE").toString();
			String description = hostData.get("DESCRIPTION").toString();
			String tNoHost = hostData.get("TELLER_NO").toString();
			String sNoHost = hostData.get("TELLER_SEQ").toString();
			String rNoHost = hostData.get("RECORD_NO").toString();
			
			
			// reset the date format to host data
			hostData.put("POST_DATE", pDateHost);
			hostData.put("REMARK", description);
			hostData.put("EFFECTIVE_DATE", eDateHost);
			hostData.put("ORIGINAL_CURRENCY", hostData.get("ORIGINAL_CURRENCY").toString().trim());
			for (int j = 0; j < listFromDB.size(); j++) {
				dbData = (Map) listFromDB.get(j);
				String pDateDb = dbData.get("POST_DATE").toString();
				String eDateDb = dbData.get("EFFECTIVE_DATE").toString();
				String tNoDb = dbData.get("TELLER_NO").toString();
				String sNoDb = dbData.get("TELLER_SEQ").toString();
				String rNoDb = dbData.get("RECORD_NO").toString();

				if ( // 闁鏁撻弬銈嗗闁跨喐鍩呴棃鈺傚闁跨喐鏋婚幏鐑芥晸閿燂拷				
						pDateDb.equals(pDateHost) && eDateDb.equals(eDateHost)
						&& tNoDb.equals(tNoHost) && sNoDb.equals(sNoHost)
						&& rNoDb.equals(rNoHost)) {
					removedList.add(hostData);
					break;
				}
			}
		}
		// 閸掔娀鏁撻弬銈嗗闁跨喐鍩呴棃鈺傚闁跨喐鏋婚幏鐑芥晸閿燂拷		listFromHost.removeAll(removedList);
		// 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚� java.util.Collections.reverse(listFromHost);

		// 闁跨喐鏋婚幏鐑芥晸閿熶粙鏁撻弬銈嗗list
		List retList = new ArrayList();
		retList.addAll(listFromDB);
		retList.addAll(listFromHost);

		// 闁跨喐鏋婚幏鐑芥晸濞层儻绱為敐蹇斿闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�
		// Jet modified - only sort by post_date
		// retList = sorting.sortByMapList(retList,
		// new String[]{"POST_DATE", "EFFECTIVE_DATE", "TELLER_NO",
		// "TELLER_SEQ"},
		// Sorting.SORT_TYPE_DESC); //闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�
		Sorting.sortMapList(retList, new String[] { "POST_DATE", "RECORD_NO" },
				Sorting.SORT_TYPE_DESC); // 闁跨喐鏋婚幏鐑芥晸閺傘倖瀚�

		return retList;
	}
	
	/**
	 * get cifNo by corpId
	 * 
	 * @param corpId
	 * @return
	 * @throws NTBException
	 */
	/*public String getCifNoByCorpId(String corpId) throws NTBException {
		String sql = "select CIF_NO from HS_CORPORATE_INFO where CORP_ID = ?";
		String cifNo = null;
		try {
			List cifNoList = genericJdbcDao.query(sql, new Object[] { corpId });
			if (cifNoList != null && cifNoList.size() > 0) {
				Map cifNoMap = (Map) cifNoList.get(0);
				cifNo = Utils.null2Empty(cifNoMap.get("CIF_NO"));
			}
		} catch (Exception e) {
			Log.error(e);
			throw new NTBException("err.txn.GetCIFNoException");
		}
		return cifNo;
	}*/
	public String getCifNoByCorpId(String corpId) throws NTBException {
		if(!("".equals(corpId) || null == corpId)){
			return corpId.substring(1);
		}
		return "";
	}
	
	//modify by wcc 20180201
	public String bussTypeToAppCode(String appCode){
		RBFactory rb = RBFactory.getInstance("app.cib.resource.common.mappingAppCode");
		if( null != rb.getString(appCode)){
			return rb.getString(appCode);
		}else{
			return appCode;
		}
	}
	//modify by wcc 20180201
	public String appCodeToBussType(String bussType){
		RBFactory rb = RBFactory.getInstance("app.cib.resource.common.mappingBussType");
		if( null != rb.getString(bussType)){
			return rb.getString(bussType);
		}else{
			return bussType;
		}
	}
	//add by linrui for get ccyname
	public String getCcyName(String ccyCode,String language) throws NTBException {
		String sql = "SELECT CCY_CODE, CCY_LONG_NAME FROM HS_CURRENCY_NAME where CCY_CODE = ? ";
		sql = sql + " and LOCAL_CODE = '" + language + "'";
		try {
			Map rowMap = genericJdbcDao.querySingleRow(sql, new Object[] { ccyCode });
			return rowMap == null ? "" : rowMap.get("CCY_LONG_NAME").toString();
		} catch (Exception e) {
			throw new NTBException("err.bnk.CcyLongNameException");
		}
	}

	public List getAvailableCurrencies() throws NTBException {
		String sql = "SELECT CCY_CODE FROM available_currencies where available_flag = 'Y'";
		try {
			return genericJdbcDao.query(sql, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
