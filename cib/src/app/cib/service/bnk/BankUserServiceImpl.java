package app.cib.service.bnk;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.cib.bo.bnk.BankUser;
import app.cib.bo.bnk.BankUserHis;
import app.cib.bo.sys.CorpUser;
import app.cib.core.CibTransClient;
import app.cib.dao.bnk.BankUserDao;
import app.cib.dao.bnk.BankUserHisDao;
import app.cib.dao.sys.CorpPermissionDao;
import app.cib.util.Constants;
import app.cib.util.IpValidator;
import app.cib.util.UploadReporter;
import app.cib.core.*;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBAction;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.exception.NTBLoginException;
import com.neturbo.set.utils.Encryption;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.utils.DateTime;
import org.apache.commons.beanutils.BeanUtils;
import java.util.Iterator;

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
public class BankUserServiceImpl implements BankUserService {
	private BankUserDao bankUserDao;
	private BankUserHisDao bankUserHisDao;
	private CorpPermissionDao corpPermissionDao;
	CibTransClient cibTransClient;

	public BankUserServiceImpl() {
	}

	public List list() throws NTBException {
		List resList = bankUserDao.list(" from BankUser as t where t.status <> ?", new Object[] { Constants.STATUS_REMOVED });
		return resList;
	}

	public BankUser load(String userId) throws NTBException {
		BankUser userObj = (BankUser) bankUserDao.load(BankUser.class, userId);
		if (userObj != null) {
			String roleId = userObj.getRoleId();
			List funcList = corpPermissionDao.loadFuncPermByGroup(roleId, "0", "0");
			userObj.setFunctionList(funcList);
		}
		return userObj;
	}

	public BankUserHis loadPendingHis(String userId) throws NTBException {
		Map conditionMap = new HashMap();
		conditionMap.put("userId", userId);
		conditionMap.put("status", Constants.STATUS_PENDING_APPROVAL);
		List userHisList = bankUserHisDao.list(BankUserHis.class, conditionMap);
		if (userHisList == null || userHisList.size() == 0) {
			return null;
		}
		BankUserHis userHis = (BankUserHis) userHisList.get(0);
		return userHis;
	}

	public BankUserHis loadHisBySeqNo(String seqNo) throws NTBException {
		Map conditionMap = new HashMap();
		conditionMap.put("seqNo", seqNo);
		List userHisList = bankUserHisDao.list(BankUserHis.class, conditionMap);
		if (userHisList == null || userHisList.size() == 0) {
			return null;
		}
		BankUserHis userHis = (BankUserHis) userHisList.get(0);
		return userHis;
	}

	public void addBankUserHis(BankUserHis usrHis) throws NTBException {
		bankUserHisDao.add(usrHis);
	}

	public void updateBankUserHis(BankUserHis usrHis) throws NTBException {
		bankUserHisDao.update(usrHis);
	}

	public void add(BankUser userObj) throws NTBException {
		BankUser oldObj = (BankUser) bankUserDao.load(userObj.getClass(), userObj.getUserId());
		if (oldObj != null) {
			bankUserDao.delete(oldObj);
		}
		bankUserDao.add(userObj);

	}

	public void remove(BankUser userObj) throws NTBException {
		bankUserDao.delete(userObj);
	}

	public void update(BankUser userObj) throws NTBException {
		bankUserDao.update(userObj);
	}

	// 锟斤拷证锟斤拷锟斤拷锟斤拷锟斤拷锟�
	//modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB begin
	/*public BankUser authenticate(String userId, String inputPassword, NTBAction action) throws NTBException {*/
	public BankUser authenticate(String userId, String inputPassword, NTBAction action, String capsLockOnFlag) throws NTBException {
	//modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB begin
		// added by hjs 20090104: Do not allow BOB BANK user to access function outside BANK
		String internalIP = Config.getProperty("InternalIP");
		String[] patterns = internalIP.split(",");
		
		printValidIP(patterns);
		
//mod by lr	if(!IpValidator.validateByPatterns(action.getRequestIP(), patterns)){
//			Log.warn("Client IP[" + action.getRequestIP() + "] Forbidden");
//			throw new NTBException("err.bnk.IpForbidden");
//		}
		Log.info("Client IP[" + action.getRequestIP() + "] Permitted");
		
		
		// added by hjs 20070528: show caps lock warning message when password
		// error occured
		if (action.getResultData() != null) {
			action.getResultData().remove("capsLockErrFlag");
		}

		Map userCache = CibUserCache.getBankUserCache();
		BankUser cachedUser = (BankUser) userCache.get(userId);

		// 锟斤拷锟斤拷Cache
		Iterator userSetShow = userCache.keySet().iterator();
		String userIdShow = "";
		while (userSetShow.hasNext()) {
			userIdShow += (String) userSetShow.next() + ", ";
		}
		Log.info("*** cache class: " + userCache.getClass().getName());
		Log.info("*** Users in cache:" + userIdShow);

		if (cachedUser != null) {
			if (cachedUser.getOnlineStatus() == CorpUser.ONLINE_STATUS_ON) {
				Log.info("User " + userId + " online, force login");
				/*
				 * String forceLoginFlag =
				 * action.getParameter("ForceLoginFlag");
				 * if(!"YES".equals(forceLoginFlag)){ return null; }
				 */
			}
		}

		// 锟斤拷锟斤拷锟斤拷锟斤拷锟�
		String encryptedPass = Encryption.digest(userId + inputPassword, "MD5");

		BankUserService bankUserService = (BankUserService) Config.getAppContext().getBean("bankUserService");
		BankUser user = null;
		try {
			user = (BankUser) bankUserService.load(userId);
		} catch (Exception e) {
			Log.error("Query user info error", e);
		}

		if (user == null || user.getStatus().equals(Constants.STATUS_REMOVED)) {
			// 锟矫伙拷锟斤拷锟斤拷锟斤拷
			throw new NTBLoginException("err.sys.bank.UserNotExist");
		}

		user.setPrevLoginTime(user.getCurrLoginTime());
		user.setPrevLoginIp(user.getCurrLoginIp());
		user.setPervLoginStatus(user.getLoginStatus());
		user.setCurrLoginTime(new Date());
		user.setCurrLoginIp(action.getRequestIP());
		user.setLoginStatus(CorpUser.LOGIN_STATUS_FAILED);

		if (user.getStatus().equals(Constants.STATUS_PENDING_APPROVAL)) {
			// 锟矫伙拷锟斤拷锟斤拷权
			bankUserService.update(user);
			// Jet add ==> write report
			writeLoginLog(user, action);

			throw new NTBLoginException("err.sys.UserPendingApproval");
		}

		if (user.getStatus().equals(Constants.STATUS_BLOCKED)) {
			// 锟矫伙拷锟斤拷
			bankUserService.update(user);
			// Jet add ==> write report
			writeLoginLog(user, action);

			throw new NTBLoginException("err.sys.UserBlocked");
		}

		String savedPass = user.getUserPassword();
		if (savedPass.equals(Constants.OPERATION_RESET_PASSWORD)) {
			// Jet add ==> write report
			writeLoginLog(user, action);

			throw new NTBLoginException("err.sys.PasswordReset");
		}

		if (!savedPass.equals(encryptedPass)) {
			// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
			int loginFailTimes = Utils.nullEmpty2Zero(user.getLoginFailTimes());
			int maxFailTimes = Utils.nullEmpty2Zero(Config.getProperty("AllowLoginFailTimes"));
			if (maxFailTimes == 0) {
				maxFailTimes = 5;
			}
			loginFailTimes++;
			// 锟斤拷锟斤拷锟洁，锟斤拷锟矫伙拷
			//add by long_zg 2015-04-23 for CR202  Application Level Encryption for BOL&BOB begin
			/*if (loginFailTimes == maxFailTimes) {*/
			if (loginFailTimes >= maxFailTimes) {
			//add by long_zg 2015-04-23 for CR202  Application Level Encryption for BOL&BOB end
				user.setStatus(Constants.STATUS_BLOCKED);
				// Jet added 2008-11-10
				user.setOperation(Constants.OPERATION_BLOCK);
				user.setRequester(user.getUserId());

				user.setBlockReason(Constants.BLOCK_REASON_BY_RETRY);
				String seqNo = CibIdGenerator.getIdForOperation("BANK_USER_HIS");
				BankUserHis userHis = new BankUserHis(seqNo);
				try {
					BeanUtils.copyProperties(userHis, user);
				} catch (Exception e) {
					Log.error("Error copy properties", e);
					throw new NTBException("err.sys.GeneralError");
				}

				// Jet added for user management report 2008-11-10
				userHis.setLastUpdateTime(new Date());

				bankUserService.addBankUserHis(userHis);
			}
			user.setLoginFailTimes(new Integer(loginFailTimes));

			bankUserService.update(user);
			// add by hjs 20070528: show caps lock warning message when password
			// error occured
			//modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB begin
			/*if ("Y".equals(Utils.null2EmptyWithTrim(action.getParameter("capsLockOnFlag")))) {*/
			if ("Y".equals(Utils.null2EmptyWithTrim(capsLockOnFlag))) {
			//modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB end
				
				if (action.getResultData() == null) {
					action.setResultData(new HashMap());
				}
				action.getResultData().put("capsLockErrFlag", "Y");
			}
			//add by long_zg 2015-04-23 for CR202  Application Level Encryption for BOL&BOB begin
			/*if (loginFailTimes == maxFailTimes) {*/
			if (loginFailTimes >= maxFailTimes) {
			//add by long_zg 2015-04-23 for CR202  Application Level Encryption for BOL&BOB end
				// Jet add ==> write report
				writeLoginLog(user, action);

				throw new NTBLoginException("err.sys.UserBlocked");
			}
			// Jet add ==> write report
			writeLoginLog(user, action);

			throw new NTBLoginException("err.sys.bank.PasswordError");
		}

		// int loginTimes = Utils.nullEmpty2Zero(user.getLoginTimes());
		user.setLoginFailTimes(new Integer(0));
		// user.setLoginTimes(new Integer(loginTimes));

		user.setLoginStatus(CorpUser.LOGIN_STATUS_SUCCESSFUL);

		// 锟斤拷锟斤拷锟斤拷锟截憋拷锟斤拷
		writeLoginLog(user, action);
		// if (loginTimes == 0) {
		// Map reportMap1 = new HashMap();
		// reportMap1.put("LOGIN_DATE", DateTime.formatDate(user
		// .getCurrLoginTime(), "yyyyMMdd"));
		// reportMap1.put("LOGIN_TIME", DateTime.formatDate(user
		// .getCurrLoginTime(), "HHmmss"));
		// reportMap1.put("USER_ID", user.getUserId());
		// // reportMap1.put("REFERENCE_ID", inputPassword);
		// reportMap1.put("STATUS", user.getLoginStatus());
		// UploadReporter.write("RP_FIRSTLOGIN", reportMap1);
		// } else {
		// Map reportMap = new HashMap();
		// reportMap.put("LOGIN_DATE", DateTime.formatDate(user
		// .getCurrLoginTime(), "yyyyMMdd"));
		// reportMap.put("LOGIN_TIME", DateTime.formatDate(user
		// .getCurrLoginTime(), "HHmmss"));
		// reportMap.put("SEQ_NO", action.getSession().getId());
		// reportMap.put("USER_ID", user.getUserId());
		// reportMap.put("STATUS", user.getLoginStatus());
		// reportMap.put("RETRY_COUNTER", user.getLoginFailTimes());
		// reportMap.put("PN_SIGN", "+");
		// UploadReporter.write("RP_LOGIN", reportMap);
		// loginTimes++;
		// }

		bankUserService.update(user);
		// 锟斤拷锟斤拷cache
		if (cachedUser != null) {
			cachedUser.setOnlineStatus(CorpUser.ONLINE_STATUS_OFF);
			cachedUser.setSessionId("");
		}
		userCache.put(userId, user);
		// 锟斤拷锟斤拷锟矫伙拷锟斤拷sesseion
		action.setUser(user);

		return user;
	}

	public void writeLoginLog(BankUser user, NTBAction action) throws NTBException {
		int loginTimes = Utils.nullEmpty2Zero(user.getLoginTimes());
		if (loginTimes == 0) {
			Map reportMap1 = new HashMap();
			reportMap1.put("LOGIN_DATE", DateTime.formatDate(user.getCurrLoginTime(), "yyyyMMdd"));
			reportMap1.put("LOGIN_TIME", DateTime.formatDate(user.getCurrLoginTime(), "HHmmss"));
			reportMap1.put("USER_ID", user.getUserId());
			// reportMap1.put("REFERENCE_ID", inputPassword);
			reportMap1.put("STATUS", user.getLoginStatus());
			UploadReporter.write("RP_FIRSTLOGIN", reportMap1);
		} else {
			Map reportMap = new HashMap();
			reportMap.put("LOGIN_DATE", DateTime.formatDate(user.getCurrLoginTime(), "yyyyMMdd"));
			reportMap.put("LOGIN_TIME", DateTime.formatDate(user.getCurrLoginTime(), "HHmmss"));
			reportMap.put("SEQ_NO", action.getSession().getId());
			reportMap.put("USER_ID", user.getUserId());
			reportMap.put("STATUS", user.getLoginStatus());
			reportMap.put("RETRY_COUNTER", user.getLoginFailTimes());
			reportMap.put("PN_SIGN", "+");
			UploadReporter.write("RP_LOGIN", reportMap);
			loginTimes++;
		}
		user.setLoginTimes(new Integer(loginTimes));
	}

	public void logout(BankUser user, NTBAction action) throws NTBException {
		// 锟斤拷锟斤拷锟斤拷锟截憋拷锟斤拷
		/*
		 * LOGOUT_DATE CHAR(8), LOGOUT_TIME CHAR(6), USER_ID CHAR(12),
		 * CORPORATION_ID CHAR(10)
		 */
		Map reportMap = new HashMap();
		reportMap.put("LOGOUT_DATE", DateTime.formatDate(new Date(), "yyyyMMdd"));
		reportMap.put("LOGOUT_TIME", DateTime.formatDate(new Date(), "HHmmss"));
		reportMap.put("SEQ_NO", action.getSession().getId());
		reportMap.put("USER_ID", user.getUserId());
		UploadReporter.write("RP_LOGOUT", reportMap);
	}

	public BankUserDao getBankUserDao() {
		return bankUserDao;
	}

	public CorpPermissionDao getCorpPermissionDao() {
		return corpPermissionDao;
	}

	public void setBankUserDao(BankUserDao bankUserDao) {
		this.bankUserDao = bankUserDao;
	}

	public void setCorpPermissionDao(CorpPermissionDao corpPermissionDao) {
		this.corpPermissionDao = corpPermissionDao;
	}

	public BankUserHisDao getBankUserHisDao() {
		return bankUserHisDao;
	}

	public void setBankUserHisDao(BankUserHisDao bankUserHisDao) {
		this.bankUserHisDao = bankUserHisDao;
	}

	public void loadUploadPin(BankUser userObj) throws NTBException {
		Map uploadMap = new HashMap();
		uploadMap.put("TRANS_DATE", CibTransClient.getCurrentDate());
		uploadMap.put("TRANS_TIME ", CibTransClient.getCurrentTime());
		uploadMap.put("USER_ID ", userObj.getUserId());
		uploadMap.put("CORPORATION_ID ", " ");
		uploadMap.put("STATUS ", "");
		uploadMap.put("REJECT_CODE ", "");
		UploadReporter.write("RP_CHGPIN", uploadMap);

	}

	public List listOperationHistory(String requester, String dateFrom, String dateTo) throws NTBException {
		return bankUserHisDao.listRecords(requester, dateFrom, dateTo);

	}
	
	private static void printValidIP(String[] ips){
		for (int i = 0; i < ips.length; i++) {
			Log.info("Valid Internal IP = " + ips[i].trim());
		}
	}

}
