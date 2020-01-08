package app.cib.service.sys;

import java.math.BigDecimal;
import java.util.*;

import net.sf.hibernate.HibernateException;

import org.springframework.dao.DataAccessResourceFailureException;

import app.cib.bo.bnk.*;
import app.cib.bo.sys.*;
import app.cib.cert.server.CertProcessor;
import app.cib.core.CibTransClient;
import app.cib.dao.bnk.*;
import app.cib.dao.sys.*;
import app.cib.util.*;
import app.cib.util.otp.SMSOTPUtil;
import app.cib.util.otp.SMSReturnObject;

import com.neturbo.set.core.*;
import com.neturbo.set.database.GenericJdbcDao;
import com.neturbo.set.exception.*;
import com.neturbo.set.utils.*;
import app.cib.core.CibIdGenerator;
import org.apache.commons.beanutils.BeanUtils;
import app.cib.core.CibUserCache;

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
public class CorpUserServiceImpl implements CorpUserService {
	private CorpUserDao corpUserDao;

	private CorpUserHisDao corpUserHisDao;

	private UserGroupDao userGroupDao;

	private CorpPermissionDao corpPermissionDao;

	private CorporationDao corporationDao;

	private PinMailerDao pinMailerDao;

	private GenericJdbcDao genericJdbcDao;

	CibTransClient cibTransClient;

	public CorpUserServiceImpl() {
	}

	public List listUserByCorp(String corpId) throws NTBException {
		List resList = null;
		try {
			resList = corpUserDao.listByCorp(corpId);
		} catch (Exception e) {
			Log.error("Error list user by Corporation", e);
			throw new NTBException("err.sys.DBError");
		}
		return resList;
	}

	public List listRoleCountByCorp(String corpId, String roleId)
			throws NTBException {
		HashMap conditionMap = new HashMap();
		conditionMap.put("id.corpId", corpId);
		conditionMap.put("roleId", roleId);
		conditionMap.put("status", Constants.STATUS_NORMAL);
		List resList = null;
		try {
			resList = corpUserDao.list(CorpUser.class, conditionMap);
		} catch (Exception e) {
			Log.error("Error list user by Corporation", e);
			throw new NTBException("err.sys.DBError");
		}
		return resList;
	}

	public List listNormalByCorp(String corpId) throws NTBException {
		List resList = null;
		try {
			resList = corpUserDao.listNormalByCorp(corpId);
		} catch (Exception e) {
			Log.error("Error list user by Corporation", e);
			throw new NTBException("err.sys.DBError");
		}
		return resList;
	}

	public List listAllUser() throws NTBException {
		HashMap condictionMap = new HashMap();
		List resList = corpUserDao.list(CorpUser.class, condictionMap);
		return resList;
	}

	public List listUserByLevel(String corpId, String level)
			throws NTBException {
		List resList = null;

		try {
			resList = corpUserDao.listByCorpLevel(corpId, level);
		} catch (Exception e) {

		}
		if (null == resList) {
			resList = new ArrayList();
		}
		return resList;
	}
	
	//add by lzg for GAPMC-EB-001-0040
	/*
	public CorpUser loadWithCorpId(String userId,String corpId) throws NTBException {
		String selectCorpUser = "select * from corp_user where user_id = ? and corp_id = ?";
		Object[] conditionObject = {userId,corpId};
		CorpUser userObj = null;
		try {
			List list = null;//genericJdbcDao.load(CorpUser.class, conditionObject, selectCorpUser);
			if(list.size() == 1){
				userObj = (CorpUser)list.get(0);
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (userObj != null) {
			if (userObj.getStatus().equals(Constants.STATUS_REMOVED)) {
				return null;
			}
			String roleId = userObj.getRoleId();
			String groupId = userObj.getGroupId();
			Corporation corp = (Corporation) corporationDao.load(
					Corporation.class, corpId);
			String corpType = corp.getCorpType();
			if (Constants.ROLE_APPROVER.equals(roleId)) {
				if (Constants.CORP_TYPE_MIDDLE.equals(corpType)) {
					roleId = Constants.ROLE_OPERATOR_APPROVER;
					List approverList = listRoleCountByCorp(corpId,
							Constants.ROLE_APPROVER);
					userObj.setCorpApproverCount(approverList.size());
				}
				if (Constants.CORP_TYPE_SMALL.equals(corpType)) {
					roleId = Constants.ROLE_OPERATOR_APPROVER_ADMIN;
					List approverList = listRoleCountByCorp(corpId,
							Constants.ROLE_APPROVER);
					userObj.setCorpApproverCount(approverList.size());
				}
				if (Constants.CORP_TYPE_MIDDLE_NO_ADMIN.equals(corpType)) {
					roleId = Constants.ROLE_APPROVER_ADMIN;
					List approverList = listRoleCountByCorp(corpId,
							Constants.ROLE_APPROVER);
					userObj.setCorpApproverCount(approverList.size());
				}
			} else if (Constants.ROLE_ADMINISTRATOR.equals(roleId)) {
				List adminList = listRoleCountByCorp(corpId,
						Constants.ROLE_ADMINISTRATOR);
				userObj.setCorpAdministatorCount(adminList.size());
			}
			List funcList = corpPermissionDao.loadFuncPermByGroup(roleId,
					corpId, groupId);
			List accList = corpPermissionDao
					.loadAccPermByGroup(corpId, groupId);
			userObj.setFunctionList(funcList);
			userObj.setAccountList(accList);
			userObj.setCorporation(corp);
		}
		return userObj;
	}
	*/
	
	//Update by heyj 20190527
	public CorpUser loadWithCorpId(String userId, String corpId) throws NTBException {
		
		
		
		CorpUserId id = new CorpUserId() ;
		id.setUserId(userId) ;
		id.setCorpId(corpId) ;
//		CorpUser userObj = (CorpUser) corpUserDao.loadByUserIdAndCoprId(userId, corpId);
		CorpUser userObj = (CorpUser) corpUserDao.load(CorpUser.class, id) ;
		
		
		if (userObj != null) {
			if (userObj.getStatus().equals(Constants.STATUS_REMOVED)) {
				return null;
			}
			String roleId = userObj.getRoleId();
			//String corpId = userObj.getCorpId();
			String groupId = userObj.getGroupId();
			Corporation corp = (Corporation) corporationDao.load(
					Corporation.class, corpId);
			String corpType = corp.getCorpType();
			if (Constants.ROLE_APPROVER.equals(roleId)) {
				if (Constants.CORP_TYPE_MIDDLE.equals(corpType)) {
					roleId = Constants.ROLE_OPERATOR_APPROVER;
					List approverList = listRoleCountByCorp(corpId,
							Constants.ROLE_APPROVER);
					userObj.setCorpApproverCount(approverList.size());
				}
				if (Constants.CORP_TYPE_SMALL.equals(corpType)) {
					roleId = Constants.ROLE_OPERATOR_APPROVER_ADMIN;
					List approverList = listRoleCountByCorp(corpId,
							Constants.ROLE_APPROVER);
					userObj.setCorpApproverCount(approverList.size());
				}
				if (Constants.CORP_TYPE_MIDDLE_NO_ADMIN.equals(corpType)) {
					roleId = Constants.ROLE_APPROVER_ADMIN;
					List approverList = listRoleCountByCorp(corpId,
							Constants.ROLE_APPROVER);
					userObj.setCorpApproverCount(approverList.size());
				}
			} else if (Constants.ROLE_ADMINISTRATOR.equals(roleId)) {
				List adminList = listRoleCountByCorp(corpId,
						Constants.ROLE_ADMINISTRATOR);
				userObj.setCorpAdministatorCount(adminList.size());
			}
			List funcList = corpPermissionDao.loadFuncPermByGroup(roleId,
					corpId, groupId);
			List accList = corpPermissionDao
					.loadAccPermByGroup(corpId, groupId);
			userObj.setFunctionList(funcList);
			userObj.setAccountList(accList);
			userObj.setCorporation(corp);
		}
		return userObj;
	}

	public CorpUser load(String userId) throws NTBException {
		CorpUser userObj = (CorpUser) corpUserDao.load(CorpUser.class, userId);
		if (userObj != null) {
			if (userObj.getStatus().equals(Constants.STATUS_REMOVED)) {
				return null;
			}
			String roleId = userObj.getRoleId();
			String corpId = userObj.getCorpId();
			String groupId = userObj.getGroupId();
			Corporation corp = (Corporation) corporationDao.load(
					Corporation.class, corpId);
			String corpType = corp.getCorpType();
			if (Constants.ROLE_APPROVER.equals(roleId)) {
				if (Constants.CORP_TYPE_MIDDLE.equals(corpType)) {
					roleId = Constants.ROLE_OPERATOR_APPROVER;
					List approverList = listRoleCountByCorp(corpId,
							Constants.ROLE_APPROVER);
					userObj.setCorpApproverCount(approverList.size());
				}
				if (Constants.CORP_TYPE_SMALL.equals(corpType)) {
					roleId = Constants.ROLE_OPERATOR_APPROVER_ADMIN;
					List approverList = listRoleCountByCorp(corpId,
							Constants.ROLE_APPROVER);
					userObj.setCorpApproverCount(approverList.size());
				}
				if (Constants.CORP_TYPE_MIDDLE_NO_ADMIN.equals(corpType)) {
					roleId = Constants.ROLE_APPROVER_ADMIN;
					List approverList = listRoleCountByCorp(corpId,
							Constants.ROLE_APPROVER);
					userObj.setCorpApproverCount(approverList.size());
				}
			} else if (Constants.ROLE_ADMINISTRATOR.equals(roleId)) {
				List adminList = listRoleCountByCorp(corpId,
						Constants.ROLE_ADMINISTRATOR);
				userObj.setCorpAdministatorCount(adminList.size());
			}
			List funcList = corpPermissionDao.loadFuncPermByGroup(roleId,
					corpId, groupId);
			List accList = corpPermissionDao
					.loadAccPermByGroup(corpId, groupId);
			userObj.setFunctionList(funcList);
			userObj.setAccountList(accList);
			userObj.setCorporation(corp);
		}
		return userObj;
	}

	public void add(CorpUser userObj) throws NTBException {
		Map conditionMap = new HashMap();
		conditionMap.put("id.userId", userObj.getUserId());
		//add by lzg for GAPMC-EB-001-0040
		conditionMap.put("id.corpId", userObj.getCorpId());
		//add by lzg end
		List list = corpUserDao.list(CorpUser.class, conditionMap);
		
		/*
		 * CorpUser oldObj = (CorpUser) corpUserDao.load(userObj.getClass(),
		 * userObj.getUserId());
		 */
		if (list.size() > 0) {
			//modified by lzg for GAPMC-EB-001-0040
			//corpUserDao.delete(list.get(0));
			
			//Update by heyj 20190527
			try{
			    String sql = "delete from CORP_USER u where u.user_id = ? and u.corp_id = ?";
			    genericJdbcDao.update(sql, new Object[]{userObj.getUserId(), userObj.getCorpId()});
			}catch(Exception e){
				Log.error("Delete corp_user error", e);
				throw new NTBException("err.sys.GeneralError");
			}//End heyj 20190527
			
			/*
			String tableName = "CORP_USER";
			Map conditionMapForDel = new HashMap();
			conditionMapForDel.put("USER_ID", userObj.getUserId());
			conditionMapForDel.put("CORP_ID", userObj.getCorpId());
			genericJdbcDao.delete(tableName,conditionMapForDel);
			*/
			//modified by lzg end
		}
		corpUserDao.add(userObj);
	}

	public void remove(CorpUser userObj) throws NTBException {
		corpUserDao.delete(userObj);
	}

	public void update(CorpUser userObj) throws NTBException {
//		Log.info("userObj update info loginId=" + userObj.getUserId()) ;
//		Log.info("userObj update info loginFailTime=" + userObj.getLoginFailTimes());
//		//modified by lzg for GAPMC-EB-001-0040
//		//corpUserDao.update(userObj);
//
//		Map conditionMap = new HashMap();
//		conditionMap.put("USER_ID", userObj.getUserId());
//		conditionMap.put("CORP_ID", userObj.getCorpId());
//		//genericJdbcDao.updateByObject(tableName,userObj,conditionMap);
//		
//		Map columnMap = new HashMap();
//        columnMap.put("LIMIT6", userObj.getLimit6());
//        columnMap.put("LIMIT10", userObj.getLimit10());
//        columnMap.put("LIMIT5", userObj.getLimit5());
//        columnMap.put("LIMIT4", userObj.getLimit4());
//        columnMap.put("LIMIT3", userObj.getLimit3());
//        columnMap.put("MOBILE_COUNTRY_CODE", userObj.getMobileCountryCode());
//        columnMap.put("LIMIT9", userObj.getLimit9());
//        columnMap.put("LIMIT8", userObj.getLimit8());
//        columnMap.put("AUTH_STATUS", userObj.getAuthStatus());
//        columnMap.put("LIMIT7", userObj.getLimit7());
//        columnMap.put("LIMIT2", userObj.getLimit2());
//        columnMap.put("LIMIT1", userObj.getLimit1());
//        columnMap.put("ROLE_ID", userObj.getRoleId());
//        columnMap.put("PREF_ID", userObj.getPrefId());
//        columnMap.put("PREV_LOGIN_TIME", userObj.getPrevLoginTime());
//        columnMap.put("VERSION", userObj.getVersion());
//        columnMap.put("LAST_UPDATE_TIME", userObj.getLastUpdateTime());
//        columnMap.put("REQUESTER", userObj.getRequester());
//        columnMap.put("FINANCIAL_CONTROLLER_FLAG", userObj.getFinancialControllerFlag());
//        columnMap.put("LOGIN_STATUS", userObj.getLoginStatus());
//        columnMap.put("BLOCK_REASON", userObj.getBlockReason());
//        columnMap.put("CERT_CARD_TYPE", userObj.getCertCardType());
//        columnMap.put("GROUP_ID", userObj.getGroupId());
//        columnMap.put("SECURITY_CODE", userObj.getSecurityCode());
//        columnMap.put("LOGIN_FAIL_TIMES", userObj.getLoginFailTimes());
//        columnMap.put("PREV_LOGIN_IP", userObj.getPrevLoginIp());
//        columnMap.put("ID_ISSUE_DATE", userObj.getIdIssueDate());
//        columnMap.put("MOBILE_AREA_CODE", userObj.getMobileAreaCode());
//        columnMap.put("USER_DESC", userObj.getUserDesc());
//        columnMap.put("FAX_NO", userObj.getFaxNo());
//        columnMap.put("PERV_LOGIN_STATUS", userObj.getPervLoginStatus());
//        columnMap.put("ID_TYPE", userObj.getIdType());
//        columnMap.put("STATUS", userObj.getStatus());
//        columnMap.put("AUTH_LEVEL", userObj.getAuthLevel());
//        columnMap.put("EMAIL", userObj.getEmail());
//        columnMap.put("CERT_INFO3", userObj.getCertInfo3());
//        columnMap.put("CERT_INFO1", userObj.getCertInfo1());
//        columnMap.put("CERT_INFO2", userObj.getCertInfo2());
//        columnMap.put("LOGIN_ID", userObj.getLoginId());
//        columnMap.put("FULL_NAME", userObj.getFullName());
//        columnMap.put("ID_ISSUER", userObj.getIdIssuer());
//        columnMap.put("CURR_LOGIN_TIME", userObj.getCurrLoginTime());
//        columnMap.put("TELEPHONE", userObj.getTelephone());
//        columnMap.put("USER_PASSWORD", userObj.getUserPassword());
//        columnMap.put("ID_NO", userObj.getIdNo());
//        columnMap.put("CURR_LOGIN_IP", userObj.getCurrLoginIp());
//        columnMap.put("MOBILE", userObj.getMobile());
//        columnMap.put("USER_NAME", userObj.getUserName());
//        columnMap.put("GENDER", userObj.getGender());
//        columnMap.put("OPERATION", userObj.getOperation());
//        columnMap.put("MERCHANT_TYPE", userObj.getMerchantType());
//        columnMap.put("LOGIN_TIMES", userObj.getLoginTimes());
//        columnMap.put("VIEW_ALL_TRANS_FLAG", userObj.getViewAllTransFlag());
//        columnMap.put("TITLE", userObj.getTitle());     
//		try {
//			genericJdbcDao.update("CORP_USER", columnMap, conditionMap);
//		} catch (Exception e) {
//			Log.error("Update CorpUser error", e);
//			throw new NTBException("err.sys.GeneralError");
//		}
		
		corpUserDao.update(userObj) ;
	}

	/* Modify by long_zg 2019-05-16 for otp login begin */
	//modified by lzg for GAPMC-EB-001-0040 20190515 add parameter corpId
	/*public CorpUser authenticate(String userId,String corpId, String inputPassword,
			NTBAction action,Map paraMap) throws NTBException {*/
		
	public CorpUser authenticate(String userId,String corpId, String inputPassword,String smsFlowNo,String otp,
				NTBAction action,Map paraMap, boolean isChgFirLoginPass) throws NTBException {
	/* Modify by long_zg 2019-05-16 for otp login end */
		
		//add by long_zg 20140117 for BOB login error log
//		Log.info("CorpUserServiceImpl authenticate + userId=" + userId + " ; inputPassword=" + inputPassword ) ;
		if (action.getResultData() != null) {
			action.getResultData().remove("capsLockErrFlag");
		}

		Map userCache = CibUserCache.getCorpUserCache();
		String userKey =  corpId + "_" + userId;
		CorpUser cachedUser = (CorpUser) userCache.get(userKey);
//		CorpUser cachedUser = (CorpUser) userCache.get(userId);
		
		//add by long_zg 20140117 for BOB login error log
		Log.info("CorpUserServiceImpl authenticate + cachedUser =" + cachedUser) ;
		
		// 闂傚倸鍊搁崐鎼佸磹閹间礁纾归柟闂寸绾惧綊鏌熼梻瀵割槻缁炬儳缍婇弻鐔兼⒒鐎靛壊妲荤紒鐐劤濠�閬嶆晸娴犲绀嬫い鎺炴嫹閻濈敻鎮峰鍛暭閻㈩垱甯￠幃娆愮節閸曨剙鏋戦棅顐㈡处缁嬫垵效閺屻儲鐓ラ柡鍥╁仜閿熶粙鏌ｉ幘瀛樼闁靛洤瀚伴、姗�鎮㈡搴濇樊濠电偛鐡ㄧ划宥囨崲閸儱钃熸繛鎴炃氶弸搴ㄧ叓閸ャ劍绀堟い鏃�甯″娲传閸曨厾浼囬梺鍝ュУ閻楃娀濡存担绯曟闁靛繒濮撮懓鍨湤閵堝棙灏甸柛鐘查叄椤㈡棃宕奸弴鐔叉嫼闁荤姴娲﹁ぐ鍐敆閵忊�茬箚闁绘劘鍩栭ˉ鏇熴亜椤掞拷妯嗛悡銈嗐亜韫囨挸顏い鏂挎喘閺岀喖鎳濋悧鍫濇锭缂備焦褰冨锟犲箖妤ｅ啯顥旀繛鎴ｉ哺瀹搞儵姊洪棃娑氬缂佺粯甯￠幃鎯х暦閸ラ攱瀚归梺鍝勬川婵敻寮抽埡鍛厱闁圭儤鎸稿ù顕�鏌℃担鐟板鐎规洏鍔戝畷鎺戔槈濞嗘ɑ顥ら柣搴ゎ瀮濞插繘宕濋幋锔猴拷婵炲棙鎸哥粻姘辨喐瀹ュ洨涓嶅ù鐓庣摠閳锋帡鏌涚仦鍓ф噮闁告柨绉归弻鐔碱敊閼测晛鐓熼悗瑙勬礃濞茬喖銆佸☉銏″�烽柤纰卞墾缁辩敻姊婚崒娆戣窗闁告挻鐟х划鏃堟偨缁嬭法锛欓梺鐓庢憸閸嬶絾绂嶅鍫㈠彄闁搞儯鍔嶇亸闈涱熆瑜庨惄顖炲蓟閿濆绠婚悹铏瑰劋閻忓牓姊洪崫鍕伇闁哥姵鐗犻悰顕�宕卞鍏尖枖闂備礁鎼Λ婵囩閸洖钃熼柕濞炬櫆閸嬪嫰鏌ｉ幋鐐嗘垿鎮甸鍡欑＝濞达絼绮欓崫娲偨椤栨粌浠ф俊鍙夊姍楠炴帡骞婂畷鍥ф灈鐎规洘顨￠幃鈩冩償閿涘嫪鐢婚梻鍌氬�搁崐宄懊归崶褜婀滈柕濞炬櫆閸婂潡鏌ㄩ弴鐐诧拷缂侊拷顦肩换娑㈠幢濡ゅ啰顔婇梺鍝勬４缁犳挸顬夊ú顏嶆晢闁跨喎褰ㄩ姀銈嗙厱婵°倓绀侀埢鏇㈡煛瀹�瀣埌閾伙綁鏌熺粙鎸庢崳鐟滄妸鍐ｆ斀闁绘劕寮舵刊鍏肩箾瀹割喗鐤甧
		Iterator userSetShow = userCache.keySet().iterator();
		String userIdShow = "";
		while (userSetShow.hasNext()) {
			String tempUserId = (String) userSetShow.next();
			CorpUser tempUser = (CorpUser) userCache.get(tempUserId);
			String onlineStatus = "(OFFLINE)";
			if (tempUser.getOnlineStatus() == CorpUser.ONLINE_STATUS_ON) {
				onlineStatus = "(ONLINE)";
			}
			userIdShow += tempUserId + onlineStatus + ", ";
		}
		Log.info("*** cache class: " + userCache.getClass().getName());
		Log.info("*** Users in cache:" + userIdShow);

		if (cachedUser != null) {
			if (cachedUser.getOnlineStatus() == CorpUser.ONLINE_STATUS_ON) {
				Log.info("User " + userId + " online, force login");
				/*
				 * String forceLoginFlag =
				 * action.getParameter("ForceLoginFlag"); if
				 * (!"YES".equals(forceLoginFlag)) { return null; }
				 */
			}
		}

		// 闂傚倸鍊搁崐鎼佸磹閹间礁纾归柟闂寸绾惧綊鏌熼梻瀵割槻缁炬儳缍婇弻鐔兼⒒鐎靛壊妲荤紒鐐劤濠�閬嶆晸娴犲绀嬫い鎺炴嫹閻濈敻鎮峰鍛暭閻㈩垱甯￠幃娆愮節閸愶缚绨婚梺鍦劋閸╁牆螣閸ヮ剚鐓涢柛婊�妞掗柇顖炴煙椤旂厧妲绘い顓滃姂瀹曟﹢顢旈崱鈺佹櫔濠德板�楁慨鐑藉磻閻愬搫鍨傞柟绋垮閸欏繘鏌ㄩ悢缁橆棔閸ユ挳姊洪崨濠佺繁闁搞劍濞婂鍫曞川婵犱胶绠氶梺缁樺姦娴滄粓鏁撻懞銉ょ箚妞ゆ劧绱曢ˇ鏌ユ懚閻愮儤鐓曢柡鍥ュ妼婢ь垶鏌涙惔锝呮瀻闁宠鍨块幃鈺呭矗婢跺妯�闂備焦濞婇弨閬嶅垂瑜版帒绠熼柟闂寸劍閸嬪鏌涢锝囩畼闁荤喆鍔嶇换娑氾拷濡偐鐒奸梺鍛婎殱婵炲﹤顕ｇ拠娴嬫婵炲棙鍔曢崝鍛存⒑闂堟侗鐓梻鍕閺屻劑顢橀姀鈾�鎷洪柣搴℃贡婵敻藟閺囩喆浜滈柨鏃囧Г椤垿鏌ｈ箛鏂跨瑨闁宠鍨块幃鈺冩嫚瑜嶆导鎰版煟鎼淬垹鍤柛娆忓暢濡垽姊洪崫鍕殭闁稿锕獮蹇涙倻閼恒儳鍘搁梺鎼炲労閻撲線顢旈崨顔煎伎闂佺鏈粙蹇旂濠婂牊鐓忛煫鍥ュ劤绾惧潡鏌熼崘鍙夊枠闁哄瞼鍠栭、娆撴偡閺夊簱鎷ら梻浣筋嚃閸ㄥ崬螞閸愵喖鏄ラ柨鏇炲�归弲鎼佹煢濡警妯堟俊鎻掝煼濮婂宕掑顒婃嫹闂佺锕ラ幃鍌氱暦閹版澘绠瑰ù锝呮憸閿涙瑩姊虹紒妯虹伇濠殿喓鍊濆畷鐢稿礋椤栨稓鍘卞銈嗗姧缁插墽澹曢幖浣圭叆闁绘梻绮ˉ鏇㈡煃鐟欏嫬鐏撮柟顔规櫊瀹曪絾寰勭�ｎ偄锟界紓鍌氬�烽懗鑸垫叏閹绢喗鍊舵慨妯挎硾妗呴梺鍛婃处閸犳岸鎮块敓鑺ョ厵闁硅鍔栫涵鐐亜閵堝繑瀚规慨濠冩そ楠炲酣鎳為妷锔芥婵犵妲呴崑鍕疮閺夋埈鍤曢悹鍥ㄧゴ濡插牓鏌曡箛鏇炐㈢�规挸妫濆娲濞戣京鍔搁梺绋挎唉濞呮洜绮嬮幒妤�鐓涢柛鎰典簽閿涙粓姊洪崨濠冨瘷闁告剬鍐炬＊闂備浇顕уù鐑芥偡閹惰棄绀嬫い鎾寸♁閺侀潧鈹戦悩鍨毄濠殿喗娼欓湁闁稿瞼鍋涢弸浣猴拷閵夈儲顥柛娆忕箲娣囧﹪顢涘搴ｅ姼闂佸憡姊归懝楣冩箒闂佺粯顭堝▍鏇㈠Φ濠靛鐓欐い鏃傚帶濡茬粯銇勯婊冨鐎规洏鍔戦、妯款樋婵絽顧佸鍝勑ч崶褏鍔撮梺鎼炲姂娴滃爼濡撮崘顔肩伋闁归绀佸▓銊╂⒑閸濆嫯顤嗛柛搴ㄤ憾閹锋垿鎮㈤崗鑲╁帾婵犮垼鍩栫粙鎴︹�旂�ｎ喗鐓曟慨妤嬫嫹閹茬偓鎱ㄦ繝鍐┿仢婵☆偄鍟埥澶嬫綇閵娿垺鐎搁梻鍌欒兌椤牓鏌婇敐鍡欘湋闁割偅娲栬繚闂佺鐬奸崑娑氱不椤曪拷鐓犻柟顓熷笒閸斻倝鏌涘锟芥喚婵﹥妞藉畷顐﹀礋椤愶絾顔勯梻浣侯焾椤戝懎螞濠靛绠栭柣鎰劋閸婇攱銇勯幋婵嗙稏缂併劏顕ч—銏ゅΧ閸涱垳顔囬梺绋匡攻閹倿骞冨锟界闁瑰搫妫欓弬锟芥⒒閸曨偅鍠樼�规洘妞藉浠嬵敇閻愮數鏆伴梻浣告惈閸燁偊鎮ч崱娑欏�堕柟鎯板Г閻撴瑥螞妫颁浇鍏岄柛鏂跨Ч閺岋絾鎯旈敍鍕剁礊缂備浇椴搁幑鍥х暦閹烘埈鐏勬い鎺戯攻鐎氳绻濋悽闈涗沪婵炲吋鐟ョ叅妞ゆ搩鏋岄敓鑺ユ叏濡澧俊缁㈠灦濮婅櫣鎷犻垾铏闂佹悶鍎滈崪浣告櫔闂傚倷鑳堕崢褔鈥栬箛娑樼９闁哄洨濮烽惌鍫ユ煥閺囩偛锟介柍閿嬪灴閺屾稑鈽夊鍫濅紣婵犳鍠栭崐鍧楀蓟濞戙垹绠奸柛鎰╁妽閻濇繈鎮楀▓鍨珮闁革綇绲介悾鐑芥偂鎼存ɑ鏂�闂佸壊鍋掗崑鍛寸�烽梻鍌氬�搁崐椋庢濮橆剦鐒界憸鏃堝灳閿曞偆鏁囬柣鏂垮缁犳岸姊洪幖鐐插姶闁告挻宀稿鏌ュ箹娴ｅ湱鍙冨┑鈽嗗灟鐠�锕�螣閸岀偞鐓涢柨鐔烘櫕閻瑩鏌″畝瀣埌妞ゎ剚鐗犲畷鍗炍旀担杞伴偗闂傚倷娴囬褏鎹㈠Δ浣典粓闁告縿鍎插畷鍙夌箾閹寸偟鎳冪紒鍓佸仱閹鏁愭惔鈥愁枈闂佸憡甯楃粙鎴︹�旈姀銈呂ч柛鎰╁妿娴犻箖姊虹紒姗嗘濠殿喗鎸抽幃楣冩倻閽樺）鈺呮煃鏉炴媽鍏屾い鏃�鍔曢—銏ゅΧ閸℃ê鏆楅梺鍝ュУ閸旀洟鍩㈠澶婂耿婵炴垶鐟㈤幏缁樼箾鏉堝墽绉┑顔哄�楀☉鐢稿醇閺囩喓鍘遍梺鎸庣箓缁绘帡鎮鹃崹顐闁绘劘灏欑粻濠氭煛娴ｈ宕岄柡浣规崌閺佹捇鏁撻敓锟�

		String encryptedPass = Encryption.digest(userId + inputPassword, "MD5");
		//add by long_zg 20140117 for BOB login error log
//		Log.info("CorpUserServiceImpl authenticate + after digest userId =" + userId + " ;inputPassword=" +inputPassword) ;
//		Log.info("CorpUserServiceImpl authenticate + encryptedPass =" + encryptedPass) ;
		
		Log.info("authenticate userId=" + userId) ;
		
		CorpUserService corpUserService = (CorpUserService) Config
				.getAppContext().getBean("corpUserService");
		CorpUser user = null;
		try {
			//modified by lzg for GAPMC-EB-001-0040
			//user = (CorpUser) corpUserService.load(userId);
			user = (CorpUser) corpUserService.loadWithCorpId(userId,corpId);
			//modified by lzg end
		} catch (Exception e) {
			Log.error("Query user info error", e);
		}

		if (user == null || user.getStatus().equals(Constants.STATUS_REMOVED)) {
			throw new NTBLoginException("err.sys.UserNotExist");
		}
		
		//add by long_zg 20140117 for BOB login error log
//		Log.info("CorpUserServiceImpl authenticate + loadUser userId =" + user.getUserId()) ;
//		Log.info("CorpUserServiceImpl authenticate + loadUser userPassword =" + user.getUserPassword()) ;
		Log.info("local user.getLoginFailTimes() =" + user.getLoginFailTimes()) ;
		
		user.setPrevLoginTime(user.getCurrLoginTime());
		user.setPrevLoginIp(user.getCurrLoginIp());
		user.setPervLoginStatus(user.getLoginStatus());
		user.setCurrLoginTime(new Date());
		user.setCurrLoginIp(action.getRequestIP());
		user.setOnlineStatus(CorpUser.ONLINE_STATUS_OFF);
		user.setLoginStatus(CorpUser.LOGIN_STATUS_FAILED);

		// 闂傚倸鍊搁崐鎼佸磹閹间礁纾归柟闂寸绾惧綊鏌熼梻瀵割槻缁炬儳缍婇弻鐔兼⒒鐎靛壊妲荤紒鐐劤缂嶅﹪寮婚悢鍏尖拻閻庨潧澹婂Σ鑽ょ磼閹冣挃闁硅櫕鎹囬垾鏃堝椤ゅ孩瀚瑰銈呯箰閹冲繐鐣烽幎鑺モ拺婵炶尪顕ч獮鏍ㄤ繆椤愶綆娈瑰┑鈩冩尦楠炴帡骞嬮鐘辩盎闂備胶绮崝锕傚礈濞嗘搩鏁傞柛鎾茶兌绾捐棄霉閿濆懏鎯堥棄瀣湤閵堝骸澧柛鐔风摠娣囧﹪鎮界粙璺樇濡炪倖鐗楀銊╂偪閿熻姤鈷戦梻鍫熶緱閻擃厾绱掗悩鍐茬伌妞ゃ垺鑹捐灒閻炴稈鍓濋弬锟芥⒑缂佹ê濮囩�殿喖鐖奸崺銏ゅ即閻忚崵鎳撻…銊╁礋椤撶姷鍘愭繝鐢靛仦瑜板啴鎮樺┑瀣厴闁硅揪闄勯崑鎰亜閺冨洤浜瑰ù鐓庢搐閳规垿鎮欏顔解枏闂佺儵鏅╅崹鍫曞Υ娓氾拷鍐�妞ゆ挾鍋涚粣娑橆湤閵堝棙鈷掗柧蹇撻閿曘垽骞掑Δ浣叉嫽婵炴挻鑹惧ú銈嗘櫠椤斿墽纾煎璺烘湰閺嗩剟鏌涢埡鍌滄创妤犵偛顑夐弫鍌滄喆閿濆棗顏洪梻鍌欑婢瑰﹪宕戦崱娑樼獥閹艰揪绱曢弳锕傛煟閺傚灝鎮戦柣鎾存礃閵囧嫰骞囬澶广垽鏌涢弮锟藉摵闁哄矉缍佹俊鍫曞炊瑜屾竟鏇犵磽娴ｈ櫣甯涢柣鈺婂灦閻涱喚锟介浣圭�婚棅顐㈡处濞叉垿鎯�椤忓牊鈷戦悹鍥ㄥ絻椤掋垽鏌よぐ鎺旂暫妤犵偛锕ら‖鏍晸閸婄喐鎲伴梻浣瑰缁诲倿藝鐠囧弬娑橆灃閿熻棄浠梺璇″幗鐢帗淇婇懞銉ｄ簻闁哄倽娉曟晶锔芥叏婵犲啯銇濋柟顕呭枤閿熻棄螣娓氼垯绱︾紓鍌氬�风欢锟犲窗濡ゅ懏鍋￠柍杞扮贰閸ゆ洖鈹戦悩宕囶暡闁哄懏鐓￠弻娑樷槈濡尨鎷烽梺鍝ュУ閻楁洟鎮鹃悜绛嬫晝闁挎洩鎷烽ˇ鐓庮湤閵堝棙灏柛銊嚙閻ｅ嘲顭ㄩ崼鐔叉嫽闂佺鏈悷銊╁礂瀹�鍕厵闁惧浚鍋呭畷宀�锟介弴妯哄姢闁跨喍绮欓、娆撴嚃閳哄﹥鈻嶅┑鐘垫暩閸嬫稑螞濞嗘挸绠伴柛婵勫劤娑撳秹鏌″鍐ㄥ缂佽妫濋弻鏇㈠醇濠靛牏顔夐柣搴㈣壘缂嶅﹪寮婚敐鍫㈢杸闁圭偓鍓氭禒楣冩⒑闂堟稒鎼愰悗姘嵆閻涱噣骞掑Δ锟筋吅濡炪倖鎸鹃崯鍧楀几韫囨柧绻嗛柣鎰典簻閿熻棄螖閻樻鐒鹃悡銈嗐亜閹捐泛鍓遍柣搴＄摠缁绘稑顔忛鑽ょ泿婵炵鍋愭繛锟藉蓟濞戙垹鍗抽柕濞垮劜閻濐噣姊洪崫鍕靛剰闁稿﹤鐏濋‖澶娒洪鍕獩婵犵數濮撮崯鐘诲箯瑜版帗鈷戦柟鑲╁仜婵″吋绻涚涵椋庣瘈闁诡喕鍗抽、姘舵晸閻ｅ矈妲炬俊鐐�栧濠氬煕閸繍鐔嗘慨妤嬫嫹閳锋垿姊婚崼鐔恒�掔紒鐘冲哺閺屻劑鎮㈤崜浣虹厯闂佽鍠氶弫璇差嚕閼稿灚鍎熼柟鎯х摠閺夋悂姊绘担铏瑰笡闁告梹顭囨禍绋库枎閹邦厽鐝烽梺鍝勬川閸犲棙绂嶅鍫熺厵閻庢稒顭囩粻鏍ㄣ亜閵夘喖鏋熸い銊ャ偢閹瑩寮堕幋鐘辩礉闁诲氦顬冨ú鏍偉婵傛悶锟芥俊銈呮噺閺呮悂鏌ら幁鎺戝姕闁哥喐妞藉濠氬磼濞嗘垵濡介柣搴ｇ懗閸涱喖搴婇梺绋跨灱閸婃盯宕堕浣告闂侀潧鐗嗗ú锝囩箔婢跺绡�闂傚牊绋戦敓浠嬫煙鐠囇呯瘈闁诡噯绻濆鎾閿涘嫬骞愰梻浣规偠閸庮垶宕曟潏銊ょ箚闁绘挸瀵掗悢鍡欐喐濠婂牆绐楁俊銈呮噹妗呴梺鍛婃处閸犳岸鎮块敓鑺ョ厵闁硅鍔栫涵楣冩煟閹捐泛鏋庨柍瑙勫灴椤㈡瑧娑甸柨瀣毎婵犵绱曢崑鐘参涢崟顖涘仼闁绘垹鐡旈弫鍐煥閺囶亝瀚规い锔惧帶椤垽濡堕崱妯烘殫婵犳鍠栭悺銊╁箞閵娾晜鍋╃�癸拷鎷烽敍婵嬫⒑缁嬫寧婀伴柤褰掔畺閺佹捇鎮惧畝锟斤紲婵犮垼娉涢鍛村煝閺囥垺鐓欐い鏃�鍎虫禒閬嶆煛娴ｇ锟介柟顔煎⒔娴狅箓宕ㄦ繝鍐╂濠电姷鏁告慨鐑藉极閸涘﹥鍙忛柡澶嬪殮濞差亜围闁搞儮鏅欑粭澶愭⒑閼姐倕鏋戦柛锝囶棞閵囨劙骞掗幘瀛樼彸闂備礁澹婇崑鍛崲閹版澘绠柣鎴ｅГ閳锋垿姊婚崼鐔峰礋闁割偁鍎遍悿鐐節婵犲倻澧涢柛瀣�块悡顐﹀炊閵婏附鍎庨梺缁樻煥濡繈寮婚妶澶婁紶闁靛闄勫В鍕⒑鐎圭姵顥曟い锕�銈告俊鐢稿礋椤栵絾鏅滈梺鍛婃礋濞佳囧礈閸洘鈷戦柛婵嗗閻掕法绱掔紒妯肩畵闁伙絿鍏樺畷锟犳倷閳哄倻锟介梻浣哥秺閸嬪﹪宕滃☉妯锋灃婵℃儳鏀sControl闂傚倸鍊搁崐鎼佸磹閹间礁纾归柟闂寸绾惧綊鏌熼梻瀵割槻缁炬儳缍婇弻鐔兼⒒鐎靛壊妲荤紒鐐劤濠�閬嶆晸娴犲绀嬫い鎺炴嫹閻濈敻鎮峰鍛暭閻㈩垱甯￠幃娆愮節閸曨剙鏋戦棅顐㈡处缁嬫垵效閺屻儲鐓ラ柡鍥╁仜閿熶粙鏌ｉ幘璺烘灈闁哄本娲濈粻娑氾拷椤忓棗濮堕梻浣虹帛鐢帗鏅舵禒瀣剦妞ゅ繐鐗嗙粻姘辨喐濠婂牊鍋傚┑鍌氭啞閻撴稓锟介敓绛嬪剳缂佸鍠楅〃濂告倷閸欏鏋犲銈冨灪椤ㄦ繈骞忛崨瀛樺仭闁哄鐏濋ˉ鐐电磽閸屾艾锟介柛濠囶棢濞嗐垽濡舵径濠勵唵闂佺粯鍨煎Λ鍕偪椤斿浜滈柟鏉垮閻ｉ亶鏌ｉ幘杈捐�块柡宀�鍠愬蹇斻偅閸愨晩锟介梻浣侯焾椤戝棝宕濆▎鎾崇畺婵°倕鎳庨幑鑸点亜閹捐泛锟介柕寰涢绨婚梺闈涱樄閸ㄥ搫鈻嶉崶鈺冪＜閺夊牄鍔嶇亸浼存煙瀹勭増鍤囩�规洦鍋婂畷鐔哄枈婢跺顏堕梻鍌氬�烽悞锕傚箖閸洖绀夌�癸拷瀚崑澶愭煟閹惧啿鐦ㄩ柛銈嗘礀閳规垿鎮╃�圭姴顥╅柟顖滃枛濮婃椽宕橀崣澶嬪創闂佺锕ら幉锛勭矉瀹ュ鍊烽柣鎴炃氶幏铏圭磽娴ｅ壊鍎愰悗绗涘喛鑰块柟娈垮枤绾惧ジ鎮楅敐搴′簻闁诲繐鐡ㄩ妵鍕閳╁喚妫撻梺杞扮劍閹瑰洭寮幘缁樻櫢闁跨噦鎷�

		CachedDBRCFactory.getInstance("accessControl");
		RcAccessIp ipChecker = new RcAccessIp();
		if (!ipChecker.checkAccess(user.getCorpId(), action.getRequestIP())) {

			// Jet add ==> write report
			writeLoginReport(user, action);
			throw new NTBLoginException("err.sys.NotAccessableIp");
		}

		//add by long_zg 20140117 for BOB login error log
		Log.info("CorpUserServiceImpl authenticate +user.getStatus() =" + user.getStatus()) ;
		
		if (user.getStatus().equals(Constants.STATUS_PENDING_APPROVAL)) {
			corpUserService.update(user);
			writeLoginReport(user, action);

			throw new NTBLoginException("err.sys.UserPendingApproval");
		}
		
		if (user.getStatus().equals(Constants.STATUS_FROZEN)) {
			corpUserService.update(user);
			// Jet add ==> write report
			writeLoginReport(user, action);

			if(user.getLoginTimes() == 5){
				throw new NTBLoginException("err.wrong.password5");
			}
			if(user.getLoginTimes() == 6){
				throw new NTBLoginException("err.wrong.password6");
			}
			if(user.getLoginTimes() == 7){
				throw new NTBLoginException("err.wrong.password7");
			}
			if(user.getLoginTimes() == 8){
				throw new NTBLoginException("err.wrong.password8");
			}
			if(user.getLoginTimes() == 9){
				throw new NTBLoginException("err.wrong.password9");
			}
		}
		
		if (user.getStatus().equals(Constants.STATUS_BLOCKED)) {
			// 闂傚倸鍊搁崐鎼佸磹閹间礁纾归柟闂寸绾惧綊鏌熼梻瀵割槻缁炬儳缍婇弻鐔兼⒒鐎靛壊妲荤紒鐐劤缂嶅﹪寮婚悢鍏尖拻閻庨潧澹婂Σ鑽ょ磼閻愵剙鍔ょ紓宥咃躬瀵鏁愭径濠勵吅闂佹寧绻傞幉娑㈠箻缂佹鍘辨繝鐢靛Т閸婂綊宕戦妷鈺傜厸閻忕偠顕ф慨鍌滐拷椤旇姤灏︽い銏℃瀹曘劑顢樿缁憋箑鈹戦悩鍨毄濠殿垱鎸抽獮蹇涙倻閽樺锛欏┑鐘绘涧椤戝懘鎷戦悢鍝ョ闁瑰鍋熼惌銈嗕繆椤栨凹鍎忔い顏傚灲婵″爼宕橀妸銉宫缂傚倷绶￠崰妤呭箰閹间焦鍋╃�瑰嫭澹嬮弸搴ㄦ煙閻愵剚缍戝ù婊冩贡缁辨帡鎮欓浣哄嚒濡炪倧绠掗崑鎰版晸閸婄喆浜归柟鐑樻尨閹锋椽鏌ｉ悩鍏呰埅闁告柨鐭傚绋库枎閹邦亞绠氬銈嗗姂閸ㄥ綊顢旈鐔翠簻闁靛繆鍓濋ˉ鏃堟煕閳哄绡�鐎规洘锕㈤、鏃堝幢椤撶姴绨ラ梻鍌氬�峰ù鍥р枖閺囥垹绐楅柟鍓х帛閸嬨倝鏌￠崒婵愬殾闁绘梻鍘ч柋鍥煏婢舵稑顪嬮柛娆忔濮婅櫣绱掑Ο鑽ゅ弳婵犫拃鍐弰闁诡垰鑻埢搴ょ疀婵犲啯鏉搁梻浣瑰缁嬫垹锟介鍕偍闁哄鍨熼弨浠嬫煥濞戞ê顏╅柛妯虹摠閵囧嫰濮�閿涘嫭鍣梺鐟板级閹告娊寮幇鏉垮耿婵炲棙鐟︾�氬綊姊婚崒姘拷闁稿﹥鎮傚畷鎰樇鎼达絿鐒兼繛鎾寸啲閹烽绮荤憴鍕╀簻闁规壋鏅涢敓浠嬫煙妞嬪骸鍘撮柡灞炬礃缁绘盯鎮欓浣哄絽缂備胶鍋撻崕鍐差焽閿熺姴钃熼柕鍫濇搐閺嬪牓鏌涘Δ鍐ㄤ粶妞ゃ倕锕幃妤冩喆閸曨剛顦遍悗娈垮枛婢у酣骞戦姀鐘闁靛繒濮烽鍝勨攽閻愬弶顥ㄧ紒缁樺笚缁傛帡鎳栭埡鍐紳婵炶揪绲介幖顐︻敁閹惧墎纾奸柟缁樺笒閳锋棃鏌熼獮鍨仼闁宠棄顧佹慨锟芥偐閼艰鎷峰┑鐘茬棄閺夊尅鎷烽梺鍝ュ枍閸楁娊銆佸▎鎰瘈闁告劦浜為鏇㈡煟鎼达絾鏆╂い顓炵墦閸┿垽宕奸姀銏紲闂佺粯锚绾绢厽鏅堕鍛簻妞ゆ帪鎷峰銊╂煙閻撳海绉虹�规洜鍏橀、姗�鎮崨顖氱哎婵犵數濮烽。钘壩ｉ崨鏉戝瀭闁汇垹澹婇弫瀣喐閺冨牆绠柡鍥╁枔闂勫嫰鏌涘☉妯戝牓骞忛搹鍦＝濞达絽澹婇崕蹇旂箾绾绡�鐎规洩缍佸畷鐔碱敃閿熻姤鍤�妞ゎ厹鍔戝畷鍗炍熺粭鐚存嫹闂傚倷鐒︾�笛兠鸿箛娑樺瀭闁惧繐婀辩粻鏃堟煟閺傚灝鎮戦柡鍛倐閹嘲鈻庡▎鎴犐戦柣搴㈣壘閵堢顬夌紒妯诲闁告稑锕ㄧ涵锟筋湤閵堝骸骞栭柣妤�妫濆﹢浣逛繆閻愬樊鍎忔繛鎾敱缁嬪顓兼径瀣幍闂佸憡鎸嗛崨顓狀偧闂備胶绮幐濠氭偡閳哄懎钃熸繛鎴欏灩缁犳稒銇勯弽銊︾殤婵絻鍨荤槐鎾存媴閸濆嫅銉х磼椤曞懎鐏﹂柕鍡楀暞缁轰粙宕ㄦ繝鍌︽嫹闁诲海鎳撴竟濠囧窗濡ゅ拋鏁侀柛銉仜閺冨牊鍋愰梻鍫熺◥濞岊亪姊虹紒姗嗘妞ゎ煉绻濋悰顔跨疀濞戞ê绐涢梺鍝勵樇閸ㄨ泛鐣靛鍜佹工闁靛牆妫欑亸鐢告煕鐎ｎ偆銆掔紒顔剧帛鐎佃偐锟藉Ο閿嬪闂備胶顭堥張顒勬偡閵娾晛绀傜�癸拷鎷峰Λ顖涖亜閹绢垰澧茬痪顓㈢畺瀹曪繝鏌嗗鍡欏幈濠电偞鍨堕悷锔剧礊閹达附鐓曢悘鐐额嚙婵倿鏌″畝瀣М闁轰焦鍔欏畷銊╊敍濠婂啫蝎闂傚倷绀佺紞濠囁夐悢鐓庣婵鎷烽拑鐔兼煏婵炵偓娅嗛柛瀣閺屾稓浠﹂崜褉妲堝銈呴獜閹凤拷
			corpUserService.update(user);
			// Jet add ==> write report
			writeLoginReport(user, action);

			throw new NTBLoginException("err.sys.UserBlocked");
		}

		Corporation corp = user.getCorporation();
		if (corp == null || corp.getStatus().equals(Constants.STATUS_REMOVED)) {
			// Jet add ==> write report
			writeLoginReport(user, action);

			throw new NTBLoginException("err.sys.UserCorpNotExist");
		}
		
		//add by long_zg 20140117 for BOB login error log
		Log.info("CorpUserServiceImpl authenticate + corp.getStatus() =" + corp.getStatus()) ;
		
		if (corp.getStatus().equals(Constants.STATUS_BLOCKED)) {
			// Jet add ==> write report
			writeLoginReport(user, action);

			throw new NTBLoginException("err.sys.UserCorpBlocked");
		}

		String savedPass = user.getUserPassword();
		//add by long_zg 20140117 for BOB login error log
//		Log.info("CorpUserServiceImpl authenticate + savedPass =" + savedPass) ;
//		Log.info("CorpUserServiceImpl authenticate + encryptedPass =" + encryptedPass) ;
		
		//modified by lzg for GAPMC-EB-001-0040 add conditions corpId
		
		
		/*  Add by long_zg 2019-05-16 for otp login begin */
		SMSReturnObject returnObject = new SMSReturnObject() ;
		Corporation corporation = user.getCorporation() ;
		String authMode = corporation.getAuthenticationMode() ;
		String otpLogin = corporation.getOtpLogin();
		if(Constants.AUTHENTICATION_CERTIFICATION.equalsIgnoreCase(authMode) && Constants.YES.equalsIgnoreCase(otpLogin) && isChgFirLoginPass/*add by linrui*/){
			try{
				SMSOTPUtil.check(returnObject, smsFlowNo, otp, "N", "E") ;
			} catch (NTBException e) {
				returnObject.setErrorFlag("Y") ;
				returnObject.setReturnErr(e.getErrorCode()) ;
				e.printStackTrace() ;
			}
			
		}
		/*  Add by long_zg 2019-05-16 for otp login end */
		
		/*  Modify by long_zg 2019-05-16 for otp login begin */
		/*if (!savedPass.equals(encryptedPass) && !savedCorpId.equals(corpId)) {*/
		if ((!savedPass.equals(encryptedPass) || !returnObject.getErrorFlag().equals("N"))) {
			/*  Modify by long_zg 2019-05-16 for otp login begin */
			
			// 婵犵數濮烽弫鍛婃叏閻戣棄鏋侀柛娑橈攻閸欏繘鏌ｉ幋锝嗩棔闁哄绶氶弻娑樷槈濮楀牊鏁鹃梺鍛婄懃缁绘﹢寮婚敐澶婄闁挎繂妫Λ鏃堟⒑閸濆嫷鍎庣紒鑸靛哺瀵鈽夊Ο閿嬵瀲濠殿喗顨滈悧濠囧极妤ｅ啯鈷戦柛娑橈功閹冲啰绱掔紒姗堣�跨�殿喖顭烽弫鎰緞婵犲嫷鍚呮繝鐢靛Т閻忔岸宕濆Δ鍛櫢濞寸姴顑嗛埛鎴︽偣閹帒濡奸柡瀣懄娣囧﹪顢曢姀鐙�浼冨┑鐙呮嫹缁舵艾顬夐搹鍦＜婵☆垰娴氭禍婊嗙亽婵犵數濮村ú銈囧閸ф鐓欓柟娈垮枛椤ｅジ鏌ｉ幘瀵告创闁哄本绋戦埥澶愬础閻愬吀绱橀梻浣虹帛鐢紕绮婚弽顬稒鎷呴悷鎵獮闂佸綊鍋婇崢楣冨储閽樺鏀介柍钘夋閻忥綁鏌涙惔娑橈拷闁糕斂鍨藉鎾閿涘嫬骞堥梺纭呭閹活亞妲愰弴鐘典笉闁规儼濮ら悡鐔兼煏婢舵ê鏋欓梺顓у灡椤ㄥジ鎮欓崣澶樻４濡炪倧绠戝锟犲箖瑜版帒绠涢柛鎾茶兌閻﹀牓姊洪崫鍕伇闁哥姵鐗曢悾鐑芥晲閸℃绐為柣搴秵娴滅兘骞忛悜鑺モ拻濞达絿鐡旈崵鍐煕閻曚礁浜扮�规洘鍎奸¨鍛存煕閻愯埖纭鹃柍瑙勫灴閹晝绱掑Ο濠氭暘闂備胶绮〃婊呮崲濮楋拷宓佸┑鐘叉搐閸愶拷銇勯幘璺烘毐闁归绮换娑欐綇閸撗冾嚤闁荤媴鎷风粻鎾诲箖閻愭番鍋呴柛鎰ㄦ櫇閸欏棝姊洪崫鍕闁挎艾鈹戦垾鐐藉仮闁哄苯绉剁槐鎺懳熼懡銈呭汲闂備礁鐤囧Λ鍕ㄦ笟锟芥槬婵°倕鎳庨悡娑㈡倵閿濆骸浜滄い蹇撶埣濮婄粯鎷呴挊澶婃優婵犳鍠栭悘姘辩矉瀹ュ棛顪栭悗锝庝簽椤︽椽鎮楅獮鍨姎闁硅櫕鍔欏鎶芥晝閿熺晫鍞甸梺鍏兼倐濞佳勬叏閸儲鐓熼柟鍨缁夘噣鏌ｉ幙鍐ㄤ喊鐎规洖鐖兼俊鎼佹晝閿熺晫锟介梻浣藉吹婵敻宕濊閹广垹鈹戠�ｎ亣鎽曞┑鐐村灟閸ㄧ懓螞濮楋拷鍋ｉ柟顓熷笒婵″灝螖閺冿拷绡�婵炲牆鐏濋弸娑㈡煥閺囥劋绨界紒杈ㄦ尭椤鎹勬担鏇畵閺岀喖鎮ч崼鐔哄嚒缂備胶濮惧▍鏇犳崲濠靛顥旀繛鎴炵懅閵堫偆绱撴担浠嬪摵闁圭懓娲ら‖澶岀磼濡顎撴繛鎾村嚬閸ㄦ娊宕濋崫銉х＝濞达綀娅ｇ敮娑㈡煕閵娿儲鍋ョ�殿噮鍋婂畷姗�顢欑喊杈ㄧ凡闂備胶绮悷閬嶅箯瀹勬壋鏋旈柕濞炬櫆閻撶喖鏌ｅΟ鍝勭骇濠㈣泛瀚槐鎺撴媴鐟欏嫮绋囬柛妤呬憾閺屾盯顢曢悩鎻掑闂佺粯鎸哥换姗�鎮￠锕�鐐婇柕濠忓椤﹂亶鏌涢悢鍛婂�愭慨濠冩そ瀹曘劍绻濋崘锝嗗闂備礁婀遍…鍫ュ疮閸фせ锟藉璺衡檨濞差亶鏁傞柛鏇ㄥ亞閻涒晛鈹戦悩鍨毄濠殿喗鎸冲畷鎰灉瀹�濠冩緭婵犵數濮烽弫鎼佸磻濞戙垺鍋ら柕濞у啫鐏婇悗骞垮劚濞诧箑鐣烽弻銉︾厱妞ゆ劧绲跨粻姗�鏌￠崱顓犵暤闁哄瞼鍠栧畷婊勬媴閻戞ɑ鍟掗梻浣芥〃缁�渚�顢栨径鎰摕闁靛ň鏅滈崑鍕煙椤撶偟校閻㈩垽绻濋悰顔跨疀閹句焦妞介、鏃堝川椤忓懎顏圭紓鍌欒兌閾忓酣宕㈡總绋跨疇閹兼番鍨婚—鎴犳喐閻楀牆绗氶柣鎾存礋閹鏁愭惔鈥茬敖闂佸啿褰炵粈浣藉絹闂佹悶鍎滃鍫濇儓闂備礁婀遍…鍫ユ晝閵夈儺鍤楅柛鏇ㄥ灠楠炪垺淇婇婊呭笡婵犮垺鍨垮濠氬磼濞嗘帒鍘＄紓渚囧櫘閸ㄨ泛鐣峰┑鍡╃亙闁跨喕濮ら弲顏勨攽閻樿宸ラ柛鐘冲哺瀹曨垶宕ㄧ�涙鍘繝銏ｅ煐缁嬫牜绮堢�ｎ�㈢懓顭ㄩ崟顓犵厑闂侀潧娲ょ�氫即鐛幒妤佸�烽柡澶嬪焾娴兼挻绻濈喊妯活瀯闁搞劍婢樿灋婵犻潧顑呯粻鏍煥閻斿搫孝閸ユ挳姊虹化鏇炲⒉闁告梹甯為懞杈ㄧ節濮橆収妫栧銈嗗姂閸ㄧ儤寰勯崟顖涚厵闁告稑锕ラ崐鎰亜閵忊剝顥旀い銏℃礋閺佹劙宕樿閻╁酣姊绘担鍛婃儓闁稿﹤婀遍敓鍊燁檯鐎规洘婢橀‖鏍晸閻ｅ苯骞愰柣搴ｆ閹风兘骞忛悜钘夊嚑婵炴垯鍨洪悡娆撴煣韫囷絽浜炵紒鐘靛仱閺岀喖顢欓妸銉︽悙闂佸崬娲︾换婵嬫濞戞瑥鏋犻梺杞扮贰娴滅偟妲愰幘璇茬＜婵﹩鍓ㄩ幏鐑芥⒑缂佹ê閲滅紒鐘虫崌楠炲啴鏁撻悩铏珫闂佸憡娲﹂崢铏圭箔婢舵劖鈷戦柛婵嗗閻т線鏌涘☉鍗炴灍妞ゆ梹娲熷缁樻媴閻戞ê娈屾繝鈷�鍐弰闁诡喗妞藉顕�宕煎┑鍥╋拷闁诲骸绠嶉崕鍗炍涘▎鎾崇煑闊洦鏌х换鍡樸亜閺嶃劎顣查柟顖氱墦閺岀喖顢欓挊澶屼患闂佽法鍠嶇划娆撶嵁閺嶎厽鍊烽柟缁樺笒琚濆┑鐘愁問閸犳牠鏁冮妷銉工闁芥ê锛夊☉銏犵闁靛鍨洪‖鍥р攽椤旀枻渚涢柛鎿勭畵瀵娊顢曢敐鍥╃槇缂佸墽澧楄摫妞ゎ剙瀚伴弻娑氾拷椤忓懎娈楅梺璇″枤閺屽篓閿熻姤鐓曢柟鐑樻尭缁楁帗銇勯渚囨闁圭懓瀚叅妞ゅ繐鎳忓▓鏂库攽閻樿尙妫勯柡澶婂皡閹风兘姊洪悡搴℃毐闁绘牜鍘ч悾鐑藉閵堝棗浠洪梺鍛婄☉鑹岄柟閿嬫そ濮婃椽宕ㄦ繝鍕ㄦ闂佹寧娲忛崐婵嬪箖濮楋拷鍋勯柛蹇氬亹閸樿鲸绻濋悽闈浶㈤柛鐔哄閺呭爼鏌嗗鍡欏弳闂佺粯鏌ㄦ晶搴ㄦ儗濞嗘挻鐓涘ù锝堫瀮瀹曞矂鏌℃担瑙勫磳闁轰焦鎹囬弫鎾绘晸閿燂拷
			
			Log.info("user.getLoginFailTimes()=" + user.getLoginFailTimes()) ;
			
			int loginFailTimes = Utils.nullEmpty2Zero(user.getLoginFailTimes());
			int maxFailTimes = Utils.nullEmpty2Zero(Config
					.getProperty("AllowLoginFailTimes"));
			if (maxFailTimes == 0) {
				maxFailTimes = 5;
			}
			
			
			loginFailTimes++;
			
			Log.info("user loginFailTimes=" + loginFailTimes) ;
			Log.info("system maxFailTimes=" + maxFailTimes) ;
			
			// 闂傚倸鍊搁崐鎼佸磹閹间礁纾归柟闂寸绾惧綊鏌熼梻瀵割槻缁炬儳缍婇弻鐔兼⒒鐎靛壊妲荤紒鐐劤缂嶅﹪寮婚悢鍏尖拻閻庨潧澹婂Σ鑽ょ磼閻愵剙鍔ょ紓宥咃躬瀵鎮㈤崗灏栨嫽闁诲酣娼ф竟濠偽ｉ懡銈囩＜闁绘劦鍓欓崝銈囩磽瀹ュ拑韬�殿喖顭烽弫鎰緞婵犲嫷鍚呴梻浣瑰缁诲倿骞夊☉銏犵缂備焦顭囬崢杈ㄧ節閻㈤潧孝闁稿﹤缍婂畷鎴﹀Ψ閳哄倻鍘搁柣蹇曞仩椤曆勬叏閸屾粣鎷锋い鏇炴噹濞呭秹鏌℃担瑙勫磳闁圭锕ュ鍕沪閼恒儵鏁梻鍌氬�搁崐椋庣矆娴ｉ潻鑰块梺顒�绉寸粈鍌涙叏濡ゅ瀚瑰鑸靛姈閸嬪嫰鏌ｉ幘铏崳闁汇倕鎳樺楦裤亹閹烘搫绱电紓渚婃嫹閼冲爼鎮幆褜鍚嬪璺侯儑閸樺崬顪㈤妶鍡楀Ё缂佽尪娉曠划璇差灃閿熺晫鍘介梺鎸庣箓濡瑩濡靛┑鍥ㄥ弿濠电姴鎳忛鐘电磼鏉堛劌绗掗摶锝夋煣韫囨冻鎷风紒杈ㄉ戠换婵堝枈濡椿鐎梺绋款儏鐎氼噣鏁撴禒瀣垫晣闁靛繒濮烽敍鐔奉湤閵堝棗濮ч梻鍕閸╂盯骞掗幊銊ョ秺閺佹劙宕堕崜浣稿Ъ闂備礁鎼Λ婵囩閸洖钃熼柣鏂挎惈閺嬪牓鏌涘Δ鍐ㄤ粧闁哥姴锕鐑樻姜閹殿噮妲婚梺鍝ュ枎閻°劍绌辨繝鍥ㄥ�婚柦妯侯樈閸樺憡绻涙潏鍓ф偧闁稿簺鍊濋幃锟犳晲閸℃洜绠氶梺缁樺姦娴滄粓鏁撻悾宀�纾肩紓浣姑慨宥夋煙椤斻劌瀚弧锟芥煕閵夋垵鎳庨獮鍫ユ⒒娴ｄ警鏀伴柟娲讳邯濮婁粙宕熼姘卞幈闂佸湱鍎ら〃婵嬫偂閺囩喆浜滈柟鎵虫櫅閿熶粙鏌＄�ｎ偅顥旈柡灞炬礋瀹曞崬螣閾忓湱鎳嗘俊鐐�ら崣锟藉磿婵犳鍥箰鎼达絿顔曢梺鍛婄懃閿涘啴骞嬮悩鐢电厰闁哄鐗勯崝搴ｅ姬閿熻姤鐓曢柕澶堝妼閻撴劙鏌￠崨顔剧疄婵﹥妞介獮鎰償閿濆洨鏆ゆ繝鐢靛仩椤曟粎绮婚幘宕囨殾闁瑰墎鐡旈弫鍥煟濡吋鏆╅柨娑欑箖缁绘稒娼忛崜褎鍋у銈庡幖閻楁捇寮幘缁樺仼鐎癸拷鎷烽敍婊勭節閵忥絾纭鹃悗鍨浮閹顢欑拋鏉块叄瀹曟儼顧庨棅顒夊墴閺岀喖鎼归銈囩厜濠碘槅鍋勯崯顐﹀煡婢跺鐏勯弶鍫氭櫅缁侊箓姊婚崒娆愮グ妞ゆ泦鍥х闁伙絽鐬肩粈濠囨煕閳╁啰鈽夌紒锟界墛缁绘盯宕卞Ο璇叉殫閻庤鎸风欢姘跺蓟濞戙埄鏁冮柣妯诲絻婵洟姊洪幎鑺ユ暠閻㈩垱甯℃俊鎾箳閹搭厾浜滈梺缁樺姦閸撴瑩顢旂捄琛℃斀闁绘劖娼欏В鍡涙煕閺囨娅冪紒銊ヮ煼濮婃椽骞愭惔銏⑩敍闂佸綊顥熼崗妯虹暦閹达箑顫挎い銈呭鐎氬綊姊洪崫鍕窛闁稿鍠撶划鏂棵洪鍛幍濡炪倖姊婚崑鎾斥枍閺囩姷纾兼い鏃傛櫕閹冲洨锟介弴鐐电Ш妞ゃ垺鐟╅獮鍡氼檧缂佸鍠楃换婵堝枈婢跺瞼锛熼梺绋款儐閸ㄥ灝鐣烽幇鏉垮唨妞ゆ劧绲惧▓浼存⒑閸︻厼鍔嬫俊顐ｅ灴楠炲繘鎮滈懞銉у幗闂佺鎻徊楣兯夐弴銏＄厸閻庯絺鏅濋ˇ鏌ユ煏閸℃ê绗掓い顒侇殹閹瑩顢楅敓鎴掔处闂傚倷鑳剁划顖炲箰婵犳碍鍎庢い鏍仜閽冪喐绻涢幋娆忕仼缂佺姵鐩弻宥嗘姜閹殿噮妲诲銈庝憾閸嬪懐鎹㈠┑瀣厱闁跨喕妫勯顐ｇ箾閸欏灏﹂柡宀嬬磿娴狅箓鎮剧仦婢洦鐓涢悘鐐额嚙婵″潡鏌ㄩ悢鍓佺煓妞ゃ垺妫冨畷閬嶅即閻樻妯呴梻鍌氬�搁崐椋庣矆娓氾拷绠规い鎰堕檮閸庢柨鈹戦崒婊庣劸缂佹劖顨￠弻鐔煎箲閹邦厾銆愰梺鎼炲妽缁诲啰鎹㈠☉銏犲耿婵☆垵顕х喊宥囩磽娴ｅ搫小闁告濞婇悰顕�寮介‖銉ラ叄椤㈡鏁撻幋鎺撳亝闂傚倸鍊搁崐鎼佸磻閸℃稑鍨傞悹杞拌閻庡绱撴担鑲℃垶鍒婇幘顔界厱婵犻潧瀚崝婊堟煟鎼搭喖鏋涢柍瑙勫灴閸┿儵宕卞鍓х泿闂佽瀛╃喊宥嗙珶閸℃稒鍎婃い鎺嶈兌缁犻箖鏌熼幆褜鍤熼柛鐔哥叀閺岀喐顦兼惔鈥冲箣闂佸綊鏀遍崹鍧楀箖閵忋倕绀傞柤娴嬫櫅瀵櫕绻濋悽闈涒枅婵炰匠鍏炬盯顢橀悜鍡樺紦闂佽鍎兼慨銈夋偂濞戞◤褰掓晲閸涱喖鏆堥梺鍝ュУ閿曘垽寮婚垾宕囨殕閻庯綆鍓涜ⅵ婵°倗濮烽崑娑樏洪顤岋拷婵炴垯鍨洪弲鏌ユ煕濞戝崬鏋ら柡澶嬫そ濮婂宕掑▎鎴Щ缂傚倸绉撮敃顏囨＂闂佺粯姊婚崢褏绮堥崒娑欏弿婵°倕顑嗙�氬湱绱掗悩宕囧濞ｅ洤锕俊鎯扮疀閺傛浼撻梻浣告惈椤戝棗螞閸愵喖钃熸繛鎴旀噰閿熶粙鏌熺�涙绠栭柣蹇撶Ч濮婅櫣鎷犻懠顒傤吅缂備浇鍩栭懝楣冣�栨径濞掓椽顢旈崟顐ょ崺濠电姷鏁告慨鎾疮閻樿绠憸鐗堝笚閳锋垿鏌熼懖鈺佷粶濠碘�炽偢閺岋綀绠涢弮鍌滅杽濡炪們鍨哄畝鎼佸极閹邦厼绶炴俊顖滅帛濞呭洭姊绘担鐟邦嚋婵炴祴鏅犻獮蹇涙倻閼恒儱浠鹃梺璺ㄥ枙婵倝鍩涢幋锔界厱婵犻潧妫楅瀛樹繆椤栨凹鍎愮紒缁樼♁瀵板嫮锟介銈庢▏闁诲氦顬冨ú姗�宕濆▎鎾崇畺婵炲棙鎸婚崐缁樹繆椤栫偞鏁遍悗姘虫閳规垿鎮欓懜闈涙锭缂備浇寮撶划娆撶嵁濡わ拷鐓ラ柛鏇ㄥ弾濠�鎺楁⒑闂堟稓杩斿Λ棰佽兌閻╁酣姊绘担鐑樺殌闁宦板妿閹广垽宕橀鐓庢優闂佹悶鍎崝濠冪濠婂牊鐓忛煫鍥э工婢у弶銇勯妷锔筋棓闁哄瞼鍠栭幖褰掝敃閵堝洨鍘滈梻浣筋嚃閸犳洜鍒掑▎鎾崇畺濞寸姴顑嗛崑銊╂煕椤垵浜介柣搴ら哺缁绘繈鎮介棃娴躲儵鏌℃担瑙勫�愮�规洘鍨甸埥澶愬閳ュ厖缃曢梻浣虹《閸撴繄绮欓幒妤�纾归柤鍝ユ暩缁★拷姊婚崼鐔剁繁闁稿骸绻橀弻娑㈠箣閻樻祴鏋呴梺鍝勭焿缂嶄礁顕ｉ锟芥櫢闁芥ê顦洪崫妤�鈹戦悙鑼憼缂侇喖绉堕崚鎺戭吋婢跺á锕傛煕閺囥劌鐏犵紒鐘差煼閹銈﹂幐搴涳拷闂佽鎸抽弨杈╂崲濠靛鐓曢柨鐔绘椤忣偅绻涢崣澶嬪唉闁哄矉绱曟禒锕傛倷椤掑偆妯囬柣搴ゎ瀮濞叉牠鎮ラ崗闂寸箚闁归棿鐒﹂弲婊呯磽娴ｇ櫢渚涙俊顐犲妼閳规垶骞婇柛濠冾殱閹便劑鎮滈挊澶岋紱闂佺粯鍔楅崕銈夊磻鐎ｎ偂绻嗛柕鍫濇噺閸ｅ湱绱掗悪锟界仸闁哄本绋戦埥澶娾枎閹邦喚鈻忛柣搴ｆ嚀閹诧紕鎹㈤崒鐐茬厴闁硅揪闄勯崑鎰版煕濞嗗浚妯嗘繛鍛囧洦鈷戦柛娑橈功閹冲嫰鏌涢妸銉т虎妞ゎ剙顦肩换婵嗩灄椤掑偊绱叉繝娈垮枟椤牓宕戦幇鏉跨煑闁哄洢鍨洪埛鎴犵磼鐎ｎ偒鍎ラ柛搴＄箻閹顬楅悡搴☆枊闂佸憡鐟ラ幊妯侯瀴濞差亜绠伴幖娣灮閸欏棙绻濋埛锟藉�婚惌娆撴煙椤栨俺瀚伴柍璇查叄楠炴锟界粙鍨闂傚倷鑳剁划顖炲垂閸洘鏅濋柍杞拌閺嬫棃姊洪锟叫ｉ柣鎾存礋閹鏁愭惔婵堢泿闂佽法鍠撻弲顐ゆ閹烘梻纾兼俊顖氬悑閸掓盯鎮楃憴鍕鐎规洦鍓濋悘鎺楁煟閻樺弶绌块悘蹇旂懇閵嗗倿鎳犻鍌滐紳闂佺鏈悷銊╁礂瀹�鍕�垫慨姗嗗墰缁犺崵锟介弴鐐靛煟濠碘剝鐡曢ˇ鐐毤閻愯尙澧涘ǎ鍥э躬閹瑩顢旈崟鑸靛闂備礁鎲″褰掋�冨┑瀣疄闁靛ň鏅涚粻缁樸亜閺冨洦顥曢柨鐔剁矙濮婅櫣锟介悩鐐斤拷闂佽鍏欓崕鐢稿箖閻戣姤鐒介柨鏃�鍎冲鎶芥⒒娴ｇ顥涢柛瀣╃窔瀹曡绻濆顒佽緢濡炪倖鍔ч梽鍕煕閹达附鍋ｉ柛銉秶閹风兘鏌＄�ｎ剙鏋戦柕鍥у婵℃悂鏁傞崜褏鍘滈梻浣哄仺閸庢煡鎮ч弴锛勪簷闂備礁鎲℃笟妤呭窗閺嶎厽鏅搁柦妯侯檳濠�銈夋煏閸繃顥ㄩ梺鍙夌矒閺岀喖鎮滈懞銉︽殸闁诲酣娼ч妶鎼佸极閹剧粯鏅搁柨鐕傛嫹
			//add by long_zg 2015-04-23 for CR202  Application Level Encryption for BOL&BOB begin
			/*if (loginFailTimes == maxFailTimes) {*/
			if (loginFailTimes >= maxFailTimes) {
			//add by long_zg 2015-04-23 for CR202  Application Level Encryption for BOL&BOB end
				user.setStatus(Constants.STATUS_BLOCKED);
				Log.info("user set status " + Constants.STATUS_BLOCKED) ;
				// Jet added 2008-11-10
				user.setOperation(Constants.OPERATION_BLOCK);
				user.setRequester(user.getUserId());

				user.setBlockReason(Constants.BLOCK_REASON_BY_RETRY);
				String seqNo = CibIdGenerator
						.getIdForOperation("CORP_USER_HIS");
				CorpUserHis userHis = new CorpUserHis(seqNo);
				try {
					BeanUtils.copyProperties(userHis, user);
				} catch (Exception e) {
					Log.error("Error copy properties", e);
					throw new NTBException("err.sys.GeneralError");
				}
				// Jet added for user management report 2008-11-10
				userHis.setLastUpdateTime(new Date());

				corpUserService.addCorpUserHis(userHis);
			}
			
			
			user.setLoginFailTimes(new Integer(loginFailTimes));

			Log.info("user loginId = " + user.getUserId()) ;
			Log.info("user.getLoginFailTimes() = " + user.getLoginFailTimes()) ;
			
			corpUserService.update(user);
			//modified by lzg for GAPMC-EB-001-0040
			//CorpUser  testUser = corpUserService.load(user.getUserId()) ;
			CorpUser  testUser = corpUserService.loadWithCorpId(user.getUserId(),user.getCorpId()) ;
			//modified by lzg
			Log.info("testUser.getLoginFailTimes() = " + testUser.getLoginFailTimes()) ;
			Log.info("testUser.getLoginTimes() = " + testUser.getLoginTimes()) ;
			Log.info("testUser.getLoginStatus() = " + testUser.getLoginStatus()) ;
			
			// add by hjs 20070528: show caps lock warning message when password
			// error occured
			
			//<!-- modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB begin -->
			/*if ("Y".equals(Utils.null2EmptyWithTrim(action
					.getParameter("capsLockOnFlag")))) {*/
			if ("Y".equals(Utils.null2EmptyWithTrim(paraMap.get("capsLockOnFlag")))) {
			//<!-- modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB end -->
				if (action.getResultData() == null) {
					action.setResultData(new HashMap());
				}
				action.getResultData().put("capsLockErrFlag", "Y");
			}
			//add by long_zg 2015-04-23 for CR202  Application Level Encryption for BOL&BOB begin
			/*if (loginFailTimes == maxFailTimes) {*/
			Log.info("loginFailTimes >= maxFailTimes = " + (loginFailTimes >= maxFailTimes)) ;
			if (loginFailTimes >= maxFailTimes) {
			//add by long_zg 2015-04-23 for CR202  Application Level Encryption for BOL&BOB end
				// Jet add ==> write report
				writeLoginReport(user, action);

				
				/* Add by long_zg 2019-06-02 UAT6-465 COB锛氱煭淇′氦鏄撻鍨嬬己澶�  begin */
				/*String lang = SMSOTPUtil.getLang(null==user.getLanguage()?Config.getDefaultLocale():user.getLanguage());
				String sessionId ="" ;
				try {
					SMSOTPUtil.sendNotificationSMS(lang, user.getMobile(), sessionId, Constants.SMS_TRAN_TYPE_LOCKED_LOGIN_PASSWORD,
							"", (new Date()).toString(), "", user.getCorpId(), "",
							"", user.getUserId(), "") ;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.error("send logged in sms msg error", e) ;
				}*/
				/* Add by long_zg 2019-06-02 UAT6-465 COB锛氱煭淇′氦鏄撻鍨嬬己澶�  begin */
				
				throw new NTBLoginException("err.sys.UserBlocked");
			}
			// Jet add ==> write report
			writeLoginReport(user, action);

			/*  Add by long_zg 2019-05-16 for otp begin */
			if(!returnObject.getErrorFlag().equals("N")){
				Log.info("One time password error") ;
				HashMap resultData = new HashMap();
				String cifNo = corpId.substring(1);
				String showPassword = "";
				for (int i = 0; i < inputPassword.length(); i++) {
					showPassword = showPassword + "鈥�";
				}
				action.setResultData(resultData);
				action.setUsrSessionData(resultData);
				throw new NTBException(returnObject.getReturnErr());
			}
			/*  Add by long_zg 2019-05-16 for otp end */
			
			Log.info("throw passwordError") ;
			throw new NTBLoginException("err.sys.PasswordError");
		}
		
		
		
		
		

		//闂傚倸鍊搁崐鎼佸磹閹间礁纾归柟闂寸绾惧綊鏌熼梻瀵割槻缁炬儳缍婇弻鐔兼⒒鐎靛壊妲荤紒鐐劤缂嶅﹪寮婚悢鍏尖拻閻庨潧澹婂Σ鑽ょ磼閹冣挃闁硅櫕鎹囬垾鏃堝椤ゅ孩瀚瑰銈呯箰閹冲繐鐣烽幎鑺モ拺婵炶尪顕ч獮鏍ㄤ繆椤愶綆娈瑰┑鈩冩尦楠炴帡骞嬮鐘辩盎闂備胶绮崝锕傚礈濞嗘搩鏁傞柛鎾茶兌绾捐棄霉閿濆懏鎯堥崯鎼佹⒑閸濄儱校闁绘濮撮锝嗙節濮橆儵鈺呮煃閸濆嫬锟藉ù婊勭矒濮婃椽宕ㄦ繝鍕暤闁诲孩鑹鹃幊姗�寮幇顓炵窞閻庯綆鍓欓獮鍫ユ⒑鐠囨彃鍤遍柟顑惧劦瀹曟﹢濡搁姀鈥崇彾闂傚倸鍊风欢姘焽瑜嶇叅闁靛牆娴勯幏鐑芥煛婢跺鍎ラ柛銈嗘礀闇夐柣妯烘▕閸庡繘骞嗛悢鍏煎仭婵犲﹤鍠氶崕鏃堟煛娴ｈ宕岄柟绋匡攻瀵板嫬螣鐠囪弓绱ｉ梻鍌欑窔濞佳囨偋閸℃瑨濮抽柤娴嬫櫈婵啿鈹戦崒姘暈闁绘挻娲熼弻鏇熺箾閸喖濮㈤梺绯曟櫔缁蹭粙鍩為幋锔芥櫖闁告洦鍓氬В鍫ユ倵鐟欏嫭绀堥柛鐘崇墵閵嗕礁鈻庨幘宕囶樄閻熸粌楠搁…鍧楀箣閿旇В鎷绘繛杈剧悼閻℃棃鎮楅鎾呮嫹闁哄洨鍞婚鍫濈厺闁哄倸绨卞Σ鍫熴亜閵堝懐鐒块柟椋庡厴濮婃椽骞嗚缁犵増绻濋敓鎴掑惈濠㈣娲熼幐濠冪珶濠靛棛绉洪柡浣瑰姍瀹曘劑顢欓挊澶婂濠碉紕鍋戦崐鏇燁毤閹间焦鍎楅柛宀�鍋涢拑鐔哥箾閹存瑥鐏╅幆鐔兼⒑闂堟冻绱￠柛鎰典簻楠炴姊虹拠鎻掝劉妞ゆ梹鐗犲畷浼村冀椤撴冻鎷烽梻浣哥仢椤戝嫮鎹㈤崱娑欑厵闂傚倸顕ˇ鏌ュ船椤栫偞鈷戦梻鍫熺〒缁犳碍淇婇幓鎺撳殗闁诡喗顨″畷锝嗗緞瀹�锟芥缂備礁澧芥晶妤勬懌濡炪値鍓欑�氫即寮诲☉鈶╋拷闁瑰瓨绻勬禒顖炴⒑閻熸澘妲婚柛鐔告尦瀵偊宕掗悙鏉戠檮婵犮垼鍩栭敋婵炲牆銈稿缁樻媴閸涘﹨纭�闂佺绨洪崐婵嬪Υ閸愩劉鏀介悗锝庝簽閸橀亶姊洪悷閭﹀殶濠殿喚鏁诲畷浼村箛閻楀牏鍘藉┑掳鍊ч幏閿嬫櫠閺囩們搴ㄥ炊鐠鸿櫣浠繛锝呮搐閿曨亪銆侀弴銏″亹闁肩⒈鍏涚槐娆撴⒒娴ｅ憡鎯堥柡鍫墰缁瑩寮介銈勭瑝濠电偞鍨崹鍦尵瀹ュ鐓欓柛鎾楀嫷浠ч梺褰掝棢閸忔﹢寮婚敐鍡樺劅闁靛繆鏅涢弲閬嶆⒑閸濄儱校闁绘濞�閸ㄩ箖鏁冮崒娑橈拷闂佽姤锚椤﹀ジ鎮楅鍕拺鐟滅増甯掓禍浼存煕濡灝浜规繛鍡愬灲閹瑩鎮滃Ο鐚存嫹闂備焦瀵х换鍌滆姳閼测晞濮冲瀣捣绾惧ジ鏌曟繛鍨姶闁绘挸鍚嬮幈銊︾節閸愨斂浠㈤梺鍦劜缁绘繃淇婇崼鏇炲窛闁告侗鍋勯悘顏嗙磼缂佹鈽夐棁澶愭倵閿濆骸浜濋柣婵囩墬缁绘稓锟藉Ο鑲╃厒濡炪値鍘奸悧鎾诲春閵忊剝鍎熼柕濞垮劤椤旀帡鎮楃憴鍕婵炴潙鍊歌灒闁告稑鐡ㄩ埛鎺楁煕鐏炲墽鎳呮い锔界叀閺岋綀绠涢弮鍌滅杽濡炪們鍨洪悧鐘茬暦閸洖鐓涢柛鎰屽懐鎲规繝寰锋澘锟界紒韫矙瀹曨垶顢涢悙鑼紮闂佸壊鐓堥崑鍡欑不妤ｅ啯鐓欓悗鐢殿焾娴犙囨煙閾忣偄濮嶉柡宀嬬秮楠炴帒顓奸崶鈺冨幆闂備礁鎼惌澶屽緤娴犲锟介柕濞炬櫅楠炪垺淇婇妶蹇斿闁稿寒浜炵槐鎾诲磼濞嗘劗銈版俊鐐存綑閹芥粓寮锟芥櫢闁绘ê鍟挎禍妤�顪㈤妶鍡楀Ё閻庨潧鐭傚顐﹀炊椤掍胶鍘藉┑鈽嗗灠閸氬寮抽埡鍛厱婵妫楅悘锛勭磼缂佹绠為柟顔荤矙濡啫鈽夐幉瀣闂傚倷绀佸﹢閬嶅礂濮楋拷澶愭偄鐏忎焦鐎婚梺闈涚箳婵參鎮橀崟顑句簻闁圭儤鍩堝Ο鍫ユ煥閻旇袚缂佺粯绻堥幃浠嬫濞磋缍侀弻銈夋偡閺夊簱鎸冩繝銏ｎ瀮濞茬喎鐣峰Δ鍛闁绘挸楠告导搴㈢節濞堝灝鏋熸い銊︾矊铻炴俊銈勭劍濞呯姴螖閿濆懎鏆為柣鎾寸懃椤垻锟介鍫拷闂佸憡锚閻°劑骞堥妸锔剧瘈闁告侗鍣禒鈺呮⒑鐠恒劌鏋嶇紒顔界懇瀵偊宕掗悙鏉戞疅闂侀潧锛忛崘銊︻吋濠电姷鏁告慨鐢割敊閺嶎厼绐楅柡宥庡幖绾惧綊鎮归崶褎鈻曢柛銈嗘礈閹叉悂寮崼銏犵ウ濠碘槅鍨甸崑鎰閸忛棿绻嗘い鏍ㄧ矊鐢埖顨奸悙鑼ⅵ婵﹦绮幏鍛驳鐎ｎ剨鎷锋繝鐢靛О閸ㄦ椽鏁冮姀鐘垫殾闁靛鏅滈弲鎼佹煕婵犲嫬鏋庣亸蹇涙⒒娴ｉ涓茬紒韫矙閹鎮￠獮顒佺洴椤㈡盯鎮欑�电骞嶆俊鐐�栭悧鏇炍涘Δ鍛柈妞ゆ牜鍋涚壕瑙勩亜閺嶃劎銆掔紒鐘荤畺閺屻劌鈹戦崱妯烘闂佺懓鍚嬮崝娆撳蓟閵娾晛鍗抽柕濞垮�楅崙瑙勭箾閿濆懏鎼愰柨鏇ㄤ簻閻ｉ攱绺界粙鍨祮闂佺粯鍔栭悾顏呯閵忕姭鏀介柣鎰煐瑜把呯磼閹绘帗鍋ラ柍銉畵瀹曞ジ鎮㈤搹鐟伴獎濠电姰鍨奸崺鏍礉閺嶎厼鐓曢柟杈鹃檮閸嬶綁鏌涢妷锝呭缂佽尙绮妵鍕箳鐎ｎ亞浠鹃梺閫涚┒閸斿矁鐏掔紓浣割儏閻忔繈鈥栨担鍓叉工闁靛牆鍟悘顏呫亜椤帞绡�妤犵偛妫濆畷鍫曨敆閿熻棄鎽嬮梻浣告啞鐢偤骞嗙�碉拷by hjs 20071016
		String certCardType = null;
		//<!-- modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB begin -->
		/*certCardType = action.getParameter("CertCardType");*/
		certCardType = Utils.null2EmptyWithTrim(paraMap.get("CertCardType"));
		//<!-- modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB begin -->
		if (certCardType != null && ! "".equals(certCardType)) {
			RBFactory rb = RBFactory
					.getInstance("app.cib.resource.common.cert_card_type");
			certCardType = rb.getString(certCardType);
			user.setCertCardType(certCardType);
			Log.info("User[" + userId + 
			        "]'s certificate card type registered - (" + certCardType + 
			        ")");
		}
		
		Log.info("TEST---user.getCorporation().getCorpId()="+user.getCorporation().getCorpId()+"  ,user.getCorporation().getAuthenticationMode()="+user.getCorporation().getAuthenticationMode());
		if (user.getCorporation().getAuthenticationMode().equals(
				Constants.AUTHENTICATION_CERTIFICATION)) {
			// 闂傚倸鍊搁崐鎼佸磹閹间礁纾归柟闂寸绾惧綊鏌熼梻瀵割槻缁炬儳缍婇弻鐔兼⒒鐎靛壊妲荤紒鐐劤濠�閬嶆晸娴犲绀嬫い鎺炴嫹閻濈敻鎮峰鍛暭閻㈩垱甯￠幃娆愮節閸愶缚绨婚梺鍦劋閸╁牆螣閸ヮ剚鐓涢柛婊�妞掗柇顖炴煙椤旂厧妲绘い顓滃姂瀹曟﹢顢撻鍡楀缁★拷鏌ㄩ悢鐑樻珪缂佹劖姊归幈銊︾節閸涱噮浠╃紓浣介哺鐢帟鐏掗梺鎯х箻閿熺晫妲愬Ο鑽ょ瘈缁剧増蓱椤嫰鏌涚�ｎ亜顏柨鐔剁矙瀵泛鈻庨幆褎顓块梻浣告啞缁嬫垿鏁冮妷锕�鍨旈悗闈涙憸绾惧吋銇勯弽銊р檨缂侊拷鍟撮弻锟犲磼閿旇棄顏跺┑顔硷龚濞咃絿妲愰幒鎳崇喕绠涢敐鍕晼婵犮垼顬冨ú鐔笺�佸锟藉嘲鐣濈�ｎ偄顏堕梻鍌欑閹碱偆鎮锕�纾块柡灞诲劜閸嬪倹绻涢幋娆忕仾闁绘挻娲樼换娑㈠箣濠靛棜鍩為梺鍝勵儑閸犳牠寮诲☉娆戠瘈闁告劑鍔岄‖宀勬⒑閸濆嫬顦搁柨鐔剁矙閹儳鈹戦崼顒佸濡炪倖甯掗崐鎰煥閸啿鎷洪柡澶屽仦婢瑰棝宕濆澶嬬厱闁哄喛鎷烽幊鍐煟閿濆洤鍘村┑鈩冩倐閸┾剝绻濋崘鈺傜彨濠电姷鏁告慨鎾晝閵堝鐤い鎰╁劗閸欏灝鈹戦悩鍨毄濠殿喚鍏樺顐﹀川婵犲啫寮块梺姹囧灩婢瑰﹪寮繝鍥ㄧ厸鐎广儱楠搁獮鏍磼閹邦収娈卞ǎ鍥э躬婵″爼宕熼鐓庡腐缂傚倷鑳舵慨鐢稿磿閹惰棄鐓橀柟杈鹃檮閸嬫劙鏌涘▎蹇ｆ▏闁诡喗鐟╁娲寠婢跺﹥娈堕梺鍝ュУ閻楃姴顕ｆ繝姘╅柕澶堝灪閺傦拷姊虹化鏇炲⒉闁挎碍绻涢崨顔剧疄婵﹥妞介幃顔撅拷濡警浼撶紓鍌欑贰閸犳鎮烽埡渚囧殨閻犲洦绁村Σ鍫ユ煏韫囧﹥娅嗛柡鍛箓椤垽濡堕崱妤�袝闂佸搫顑呯粔鍫曟嚍鏉堚晝纾兼俊顖濆亹閻﹀牆鈹戦悙鑼闁诲繑绻傞埢宥呂熼懡銈囩槇闂侀潧绻掓慨鎾储閻滅湞er闂傚倸鍊搁崐鎼佸磹閹间礁纾归柟闂寸绾惧綊鏌熼梻瀵割槻缁炬儳缍婇弻鐔兼⒒鐎靛壊妲荤紒鐐劤缂嶅﹪寮婚悢鍏尖拻閻庨潧澹婂Σ鑽ょ磼閹冣挃闁硅櫕鎹囬垾鏃堝椤ゅ孩瀚瑰銈呯箰閹冲繐鐣烽幎鑺モ拺婵炶尪顕ч獮鏍ㄤ繆椤愶綆娈瑰┑鈩冩尦楠炴帡骞嬮鐘辩盎闂備胶绮崝锕傚礈濞嗘搩鏁傞柛鎾茶兌绾捐棄霉閿濆懏鎯堥棄瀣湤閵堝骸澧柛鐔风摠娣囧﹪鎮界粙璺樇濡炪倖鐗楀銊╂偪閿熻姤鈷戦柟鑲╁仜閸旀潙霉濠婂啰鍩ｇ�规洟娼ч埢搴ㄥ箛椤旂虎鍟庢繝鐢靛仦濞兼瑩顢栭崱妞绘瀺濠电姴鍊甸弨浠嬫煥濞戞ê顏柡鍡╁墯椤ㄥジ鎮欓崣澶婃灎閻庢鍠栨晶搴ㄥ箲閸曨垪锟介柟瀛樼箖閺嗩垶姊婚崒姘拷闁煎啿鐖奸獮濠囧箻濞茬粯鏅銈嗘尵閸犲酣宕归弮鍫熷�甸梻鍫熺♁閸熺偞銇勯锟界闁哄矉缍佹慨锟芥偐閸愬弶娈橀梻浣瑰▕閺�杈╂暜閹烘绠掗梻浣瑰缁诲倿骞婅箛娑樼柈闁绘劗鍎ら崑锝夋煙閺夊灝顣崇紒鈽呮嫹閺岋紕浠﹂悢閿嬵棳闁汇倧鎷烽弻鐔煎箲閹伴潧娈悗娈垮枙閸楁娊骞冨Δ鍐╁枂闁告洦鍓涢ˇ瀛樼節閻㈤潧浠滈柨鏇ㄤ邯婵″瓨鎷呴崜鑼簻闂佽偐顭堥悘姘枍閵忋倖鈷戦柛蹇涙？閼割亪鏌涙惔銏犫枙闁轰焦鎸荤粋鎺旓拷椤愮喐瀚奸梺鑽ゅТ濞诧箒銇愰崘顔煎惞閺夊牃鏅濈壕鐣岋拷閵夈儲鍣烘い銉枤閿熶粙寮撮悢鍝ュ嚒闁剧粯鐗犻弻娑樷槈閸楃偞鐏堟繛瀛樼矋椤ㄢ�愁瀴濞差亜宸濆┑鐘插�歌闂備礁鎽滄慨鐢搞�冮敓鐘茬畺闁跨喓濮撮崡鎶芥煟閺冨洦顏犻柣锕�鐗撳濠氬磼濮樺崬顤�缂備礁顑嗙敮陇妫㈤梺闈涱煭鐠愮喐绂嶅鍫㈠彄闁搞儜宥嗘暰濠电偛鎳庨敃顏堝蓟閿涘嫪娌悹鍥ㄥ絻椤線姊虹拠鈥虫灁闁搞劏妫勯悾宄邦煥閸曨剙顎撻梺鎯х箳閹虫捇鐛崱娑欌拻闁稿本鐟чˇ鏌ユ煙绾板崬浜伴柟宕囧枛椤㈡稑顭ㄩ崨顖滃帬闂備礁鎲￠幐鐑芥倿閿曞倵锟介煫鍥ㄧ♁閻撴瑩鏌ｉ幋鐏活亜鈻撴ィ鍐╃厽闁哄倸鐏濋幃鎴︽煕婵犲偆鐓奸柡灞界Ч瀹曨偊宕熼顒佸婵＄偑鍊曠换鎴濐湤閽樺鐎村┑鐘宠壘绾惧吋绻涢崱妯哄闁靛牊婢橀埞鎴︽倷瀹割喗鈻忓┑鐐跺皺閸犲酣鎮鹃悜钘夌骇婵炲棙鍎抽幃鎴︽⒑閹肩偛鍔�闁告劦浜欓崰濠傗攽閻樺灚鏆╅柛瀣☉铻炴繛鍡樻尭缁�澶愭煙閻戞ɑ鈷愮�规洘鐓￠弻娑㈠焺閸愵亖妾ㄩ梺缁樻尵閸犳牠寮婚悢鍛婄秶闁告挆鍚锋垵顪㈤妶鍐ㄥ姕婵炶尙鍠庨悾鐑筋敆閸曞灚鏅梺缁樺姉鐞涖儵骞忓ú顏呪拺闁革富鍙庨悞楣冩倵濞戞帗娅婇柟顕嗙節閺佹劖寰勭�ｎ剙骞堥梻浣告惈濞层劑宕抽敃鍌涘殌閻犱警鐓夐幏鐑芥煛閸モ晛鏋庨柣蹇撶摠椤ㄥジ鎮欓崣澶婃灎闂佽鍟崶褔鍞跺┑顔斤供閸樺ジ顢旈悜妯肩瘈闁汇垽娼ф禒鈺呮煙濞茶绨界�垫澘锕ョ粋鎺旓拷椤忓棛鏋�闂備浇娉曢崰鎾存叏閹绢喗鍋い鏇嫹閻撶喖鏌熼柇锕�鏋涢柛鏂款儔閺岋綁骞樼捄鐑樼亪闂佸搫鐭夌换婵嗙暦濮楋拷鐓涘ù锝堟〃濞ｎ喖鈹戦悩顐ｅ闁告劦浜濋幉鐓庮湤閵堝啫濡奸柕鍫熸倐楠炲啫鈻庨幘鏉戞濡炪倖甯掗崑鍡涘疮鐎ｎ喗鈷掑ù锝囩摂閸ゅ啴鏌涢悩鎰佹闁告帗甯￠獮妯兼嫚閼碱剦鍞跺┑鐘垫暩婵挳宕銈嗩偨闁绘劕鎼粻瑙勭箾閿濆骸澧柣蹇ｄ邯閺屾盯鎮㈤崨濞夸虎濠殿喖锕ら…宄扮暦閹烘垟鏋庨柟閭﹀枔閸嬫ɑ绻濋悽闈涗粶妞わ妇鍘х叅闁绘棃顥熼弳锕�霉閸忓吋缍戠紒鐘差煼閹鈽夊▍顓т簻椤曪綁宕稿Δ浣哄幗闁瑰吋鐣崝宥呪槈瑜旈弻鐔兼嚍閵壯呯厜闂佽鍨伴惌鍌氱暦閸楃偐妲堟繛鍡楅獜閹风兘姊绘担鐟邦嚋缂佽鍊归〃濂稿箹娴ｇ鍤戦梺缁樻煥閸氬鎮￠崘顔解拺闁割煈鍣崕蹇旂箾閹碱厼娅嶉柡宀嬬磿娴狅箓宕滆閸掓稑顪㈤妶鍐ㄧ仾婵炶尙鍠愭穱濠囧醇閺囩偛绐涘銈嗘煥瑜扮偟鑺遍幘顔解拻闁稿本鐟х粣鏃�绻涙担鍐插悩濞戞ǚ鏋庨柟瀵稿仦濞堜即姊洪崷顓炲妺妞ゃ劍绻勭划濠氬箳濡や讲鎷绘繛杈剧秬濞咃絿鏁☉銏＄叆闁哄洦顨濆▍濠勶拷閺囩喎鐏寸�规洏鍔戦、妯衡槈濞嗘埊鎷锋繝鐢靛Х閺佸憡鎱ㄩ幘顔肩柈妞ゆ牜鍋涢惌妤呯叓閸ャ劍灏ㄩ柡锟界矙閺岋絽螣閸濆嫮楠囬梺闈╃悼閸庛倝濡甸崟顔剧杸闁哄洨鍋愰弸鍛存⒑閸濆嫮鐏遍柛鐘冲姍钘濋柛蹇撳悑閸庣喖鏌嶉幘鍛闁诲簼鍗冲缁樻媴鐟欏嫬浠╅梺鍛婃煥缁夌懓鐣烽弴銏犵闁兼亽鍎辨禒濂告⒑缂佹ê鐏辨俊顐㈠閹瑦绻濋崶銊у弳闂佸搫鍟ú锕偹夐弴銏＄厽闁规儳鐡ㄧ粈瀣叏婵犲偆鐓肩�规洘甯掗‖妤咁敆婵犲啫顏堕梻鍌欒兌閸樠囧疮閹稿孩娅犻幖娣妼缁�鍡涙煙閻戞﹩娈鹃柡浣哥У缁绘繃绻濋崒姘间痪濠电偛鐗婂姗�鈥旈姀銈呂ч柛娑卞灣椤斿洤鈹戦埥鍡椾簼缂佸甯炵划娆愬緞婵犲骸鎮戞繝銏ｆ硾椤戝洭宕㈤悽鍛娾拺缁绢厼鎷嬮悗顕�鏌涢弮鎾剁暤鐎规洟娼ч埢搴ㄥ箻鐎电骞愰梺璇插嚱缁叉椽寮查悙鍝勭鐎癸拷瀚悡銉╂煟閺傚灝顣抽柣顓熺懃閳规垿鏁嶉崟顐㈠箣婵犵绱曢崗姗�銆佸▎鎴炲枂闁挎繂妫涢敓鐣岀磽閸屾艾锟介柛濠囶棢濞嗐垽濡舵径濠勵唵闂佺粯鍨煎Λ鍕偪椤斿浜滈柡宥庡亜娴犳粌顪涢崨瀛樷拺闁告稑锕ユ径鍕煕閵婏箑顥楅柣姘劤椤撳吋寰勭�ｎ剙骞堥梺璇茬箳閸嬬偛鐣峰Ο鑽ょ彾婵☆垰銈借ぐ鎺撳亹缂佹稓顢婇敓钘夆攽椤旂》鏀绘俊鐐舵閻ｇ兘顢曢敃锟姐劑鏌曟径鍫濓拷婵狀枎鎷峰缁樻媴閸涘﹤鏆堥梺鍝勮閸旀垿骞嗗畝鍕＜闁绘劘灏欐导瀣⒑閸濆嫬鏆欓柣妤�妫濋敐鐐哄锤濡や胶鍘梺鍓插亝缁诲啴宕幒鏃�鍠愰柡鍐ㄧ墢瀹撲線鏌″搴′簻闁搞劍绻堥弻锝堫樉闁硅绱曢敓浠嬪捶椤撶姷锛濇繛杈剧到閹碱偅鐗庢繝纰夌磿閸嬬姴螞閸曨垪锟藉璺衡檨濞差亶鏁傞柛鏇ㄥ弾閸熷洭姊绘担铏瑰笡闁告梹鐗為妵鎰板礃椤旂櫢鎷烽梺闈浥堥弲婊堝煕閹达附鍋ｉ柛銉簻閻ㄨ櫣绱掗悩鎻掔骇闁逛究鍔岃灒缂佸鐏濋—顐︽⒑閸涘﹤绗氶悽顖滃仱閺佹挻绂掔�ｅ灚鏅╅梺鍏肩ゴ閺呪晠宕ú顏呪拻濞达絽鎲￠幆鍫熺箾鐠囇冾湅婵狅拷绀侀埞鎴︻敊濞嗗墽锟藉銈冨妷閿熶粙鈥栨径濞掓椽顢旈崟顒�绁舵俊鐐�栭幐楣冨磻濞戞瑤绻嗘繛宸簼閳锋垿鏌ｉ悢鍝勵暭闁哥喓鍋ら弻鐔煎川婵犲倵鏋欓梺缁樹緱閸犳稓绮诲☉銏犵濞达綀顬冮妵婵囥亜閵忊剝顥旂�规洏鍔戦、娑橆灄椤掑偆鍟堥梻鍌氬�搁崐鐑芥嚄閸撲礁鍨濇い鏍仦閺咁亝绻濈喊妯活瀯闁搞劋鍗抽弫鍐敂閸曨厽娈鹃梺鍝勬祫缁辨洟鎮块敓鑺ョ厵闁绘劦鍓欐慨鍫熶繆椤愩垹鏆ｆ鐐插暣閸┾剝鎷呴崣澶屼簴闂佽崵濮垫禍浠嬪礉鐏炵偓鍙忓┑鍌氭啞閳锋垿鏌熺憴鍕闁告艾缍婇弻娑氾拷椤掞拷妲愭繝纰夌磿閺佽鐣烽悢纰辨晬婵﹢纭搁崯瀣⒒娴ｅ憡鍟炴い銊у枛瀹曟垿鎮㈤崫銉箳婵犻潧鍊搁幉锟犲煕閹达附鐓曟繛鎴烇公濮婃霉濠у灝鐏柕鍥у椤㈡鏁撻懞銉︻啀闂備胶鎳撶粻宥夊垂瑜版帒鐓″鑸靛姇椤潡鏌ｅΟ鍨敿闁跨喕濮ょ换婵堝枈濡椿鐎梺鎼炲姂濞佳囧煝瀹ュ鏅查柛鈩冪懐濠�鎺楁⒑閸忚偐銈撮柡鍛箘婢规洘绺介崨濠勫幍濡炪倖鐗曞Λ妤呭疮閻樿绠伴柛鎰靛枟閳锋垿鏌涘┑鍡楊仾濠殿垰銈搁弻娑氾拷椤忓氦鍚悗娈垮櫘閸嬪﹪鐛Ο纭锋嫹闁告挸寮剁粻鎶芥⒑鐠囨彃鍤辩紓宥呮閺佹挻绂掔�ｎ�晠鏌嶉崫鍕拷闁告﹩浜濈换婵嬪閿濆懐鍘紓浣割儐閹瑰洭骞冮悙顒佺秶闁靛ě鍜佸晭闂備礁鎲＄粙鎴澝洪妶鍚ゆ椽鏁冮崒娑樹簵濠电偞鍨堕埣銈堛亹閹烘挻娅滈梺鍛婁緱閸犳宕伴幇顓犵瘈闁冲嚖鎷风粔鐟邦湭椤曪拷鐓欏〒姘仢婵＄晫绱掔紒妯肩疄鐎规洜鍠栭、鏇㈠閻欙拷榫氭繝鐢靛Х閺佹悂宕戦悢鐓庣厱闁割偆鍠撻—鎴炪亜韫囨挾澧㈢�规挷鑳堕敓浠嬪Ψ閵夘喗顎掗梺杞扮椤戝寮诲☉妯锋闁告鍋炲▓銊╂⒑閸愬弶鎯堥柛鐔稿婢规洘绂掔�ｎ偆鍘遍柣蹇曞仜婢т粙骞婇崨顔轰簻闁挎洩鎷风痪褔鏌曢崶褍顏柡浣稿暣閺佹捇鎸婃径澶婂灊濠德板�楁慨鐑藉磻閻愬搫绀夋繛鍡樻尵瀹撲線骞栧ǎ顒�濡介悗鐢靛Т椤法鎹勯搹鍦紘闂佸搫妫崑濠傤瀴缂佹ɑ濯寸紒娑橆儏濞堟劙姊洪幖鐐插闂佸府缍佸顐﹀箻缂佹ê娈熼梺闈涱檶闂勫嫮娆㈤锔解拻闁稿本鐟︾粊鐗堛亜閺囧棗娲ょ粈鍕煟閿濆懐鐏辩紒锟斤躬閺屾盯鍩勯崘鍓у姺濡炪値浜濋崹鍧楀蓟閿濆绠涙い鎾跺Т椤ユ粓姊洪幖鐐插姶闁告挻宀稿畷鎴﹀磼閻愯尙顔愰梺鍦拡閸樺ジ寮搁悢鐚存嫹闁硅偐鍋涢崝锔芥叏婵犲啯銇濇俊顐㈠暙閳藉顬楅澶嬫瘒濠电姷顣藉Σ鍛村磻閸涙番锟芥繝闈涱儏缁犵偤鏌曟繛鐐珔缁炬儳銈搁弻锝夊箛椤掑娈堕梺鍛婏耿娴滆泛顬夊ú顏咁棓婵炴垼浜崝鎼佹⒑缁嬫寧鍞夐悘蹇旂懇楠炲繗銇愰幒鎾存珫闂佸憡娲﹂崢楣冩晬濠婂喚婀ら柕鍫濇閳锋帡鏌嶅畡鎵ⅵ闁诡喒鍓濈缓浠嬪川婵犲嫬骞愬┑鐐舵彧缁插潡骞婇幘瀛樺弿闁割偒鏋庨幏鐑芥煛婢跺﹦浠㈡い蹇ｅ亰閺屻劑鎮㈤崙銈嗗濡炪倧绠戝锟犲箖濡わ拷鐏勯柧蹇ｅ亽濡棃姊虹�圭媭鐎柛銊ユ健楠炲啴鍩￠崨顓炵�銈嗗姧缁查箖鎯佹惔鈾�鏀介柣妯诲墯閸熷繘鏌涢悩宕囧⒌闁诡喗妞芥俊鎼佸煘閸栤剝瀚瑰┑鐘绘涧閸婂鈥﹂崼婵愬晠婵犻潧妫岄弨浠嬫煟濡绲绘い蹇撶摠缁绘繈鏁撶粵瀣壈闂佸疇顬冮崹鍧楀箖閳哄啯瀚氱憸宥嗗閹扮増鐓熼幖娣灩閸ゎ剟鏌涢悩铏殤闁告帗甯炵槐鎺懳熺粙澶哥凹闂備礁鎲￠崝蹇涘疾濞戙垺鏅稿ù鐘差儐閳锋垹绱撴担濮戭亝鎱ㄩ崼銏″枑闁哄鐏濋弳锝夋煛娴ｈ灏扮紒鍌涘笧閿熶粙顢曢妶鍛辈闂傚倷鑳剁划顖毼涙笟锟芥晪鐟滄垿銆傞幐搴濈箚闁绘劦浜滈敓浠嬫煕閺傝法鐒稿┑鈥崇埣楠炴牗鎷呴崫銉ф毇婵犵數鍋涘Λ娆撳垂閸撲緤鎷锋慨妯块哺閸犳劗锟介妷銉︻棬妞ゆ洟浜堕弻鐔兼焽閿曪拷鈷掑┑鐙呮嫹閸婃繈寮婚弴銏犻唶婵犻潧顑愰敓鑺ョ節閳凤拷鑻悘锕傛煛瀹�瀣埌閾绘牠鏌涢幇鈺佸Ψ闁哄鎳橀幃妤�鈻撻崹顔界彯闂佺顑呴敃顏堟偘椤斿槈鐔烘兜闁垮顏舵繝纰樻閸垳鎷冮敃鍌涘�堕柛顐犲劜閳锋垿鏌ゆ慨鎰拷闁哄绉瑰濠氬礋椤愩伆褏锟介弴鐔峰鐎殿喗鎸虫慨锟芥偐濞堟寧鈻嶉梻鍌欑劍鐎笛兠洪弽顓炵９鐟滅増甯楅崑鍌炴煟閺傚灝鎮戦柣鎾寸懇閺屾盯鈥﹂幋婵囩亪闂佸搫妫欓悷銉╂箒濠电姴锕ょ�氼剟藟濡ゅ懏鐓ラ柣鏂垮閻瑧锟介鑺ュ唉闁诡喗鐟╁Λ鍐ㄢ槈濮橆剙绠哄┑鐘垫暩婵兘銆傛禒瀣婵犻潧顑嗛崑銈夋煛閸ワ絾鍤嶉柛銉墯閸婄粯淇婇姘础缂佺姵纰嶇换娑㈡晲閸涱喗鎮欓悗瑙勬处閸撴岸骞堥妸鈺佺＜婵炴垶鐟ュ鎸庣節閻㈤潧孝闁瑰啿绻橀、鏃堟偐閻㈢數锛滈柡澶婄墑閸斿瞼绮幒妤佹嚉闁挎繂顦奸悡锝夌叓閸ャ劌鍤柛銈呭暣閺屾稒鎯旈垾鎰佸妷婵烇絽娲ら敃顏堝箖濞嗘搩鏁傞柛鏇″煐鐎氳绻濋悽闈涗粶妞ぱ冪灱缁骞樺畷鍥ㄦ闂佸壊鍋呭ú姗�寮查弻銉у彄闁搞儯鍔嶉悡銉︺亜韫囨挾鍩ｆ慨濠勭帛閹峰懘鎸婃径濠冨劒濠电姵顔栭崰鏍倶濮樿京鐭夌�广儱妫涢—鏍倵閿濆簼绨芥い锔芥そ閹鐛崹顔煎闂佺娅曢崝娆忣嚕閹惰姤鏅濋柨鐔烘櫕閸炵敻鏌ｉ悩鐑樸�冮悹锟界畵閹偞銈ｉ崘鈺冨弳闂佺粯妫侀褑顣块梻渚�锟界粚鍫曞礉瀹�鍐航闂備胶绮幐鍛婎毤閸涘﹦顪栨慨妤嬫嫹閳锋垿鎮归崶顏勭毢缂佺姴鍟块埞鎴︽倻閸ャ劌顏堕悗娈垮枦椤曆囧煡婢舵劕顫块柣妯活問閸熷姊虹拠鎻掝劉缂佸甯￠垾锕傚炊椤掞拷顦甸梺鐐藉劜閸撴艾锕㈤妶澶嬬厪闁割偅绻勯崙鍦磼閵娧勬毈闁哄本鐩俊鎼佸Ω閵夋劖鎸歌彁闁搞儜宥堝惈閻庤娲栭妶绋款嚕閹绢喗鍊烽柤纰卞墰瀹曞搫鈹戦敍鍕杭闁稿﹥鐗犻幃褍螖閸愩劌鐏婇柟鍏肩暘閸斿瞼鐥闇夐柣妯烘▕閸庢劙鏌嶉柨瀣瑨闂囧鏌ㄥ┑鍡楊伂妞ゆ帞鍠栭弻锝夊箳閺傚じ澹曠紓浣虹帛缁嬫捇鏁撻悾宀�鐭欓柛顭戝枛缁狅綁鏌ｆ惔銏╁晱闁革綆鍣ｅ畷鎴﹀川椤栨稑搴婂┑鐘绘涧濡參鎮橀崟顖涚厱婵°倕鍟禒褔鏌曢崼顒傜暤婵﹦绮幏鍛村川婵犲倹娈橀梻浣告啞濡垹绮婚幘宕囨殾闁靛繈鍨洪崰鍡涙煕閺囥劌寮炬繝锟筋儔閹嘲顭ㄩ崨顓ф毉闁汇埄鍨遍〃锟犲箖閳ユ枼鏋庨柟鎯ь嚟閸樹粙姊洪悷閭﹀殶濞村吋绻堥、鏃堝醇閻斿嚖鎷烽梻浣稿暱閹碱偊骞婅箛娑欏亗闁哄洨鍋愰弨浠嬫煟濡绲绘い蹇婃櫅闇夐柣妯绘そ閸濇椽鏌熸笟鍨缂佺粯绻堝畷姗�鍩炴径姝屾濠德板�楁慨鐑藉磻閻愬搫绀夐柡宥庡幖缁犳岸鏌￠崘銊у闁绘挻鍨块弻宥夊传閸曨偓鎷峰銈呯箲濡炶棄顬夋繝姘＜婵炲棙甯掗崢锟犳⒑缁嬫寧鎹ｉ柡浣筋嚙閻ｅ嘲顭ㄩ崘锝嗘杸闁诲函缍嗘禍锝夊箺閺囥垺鈷戦梻鍫熷崟閸儱妫樺〒姘炬嫹閺呮悂鏌ら幁鎺戝姌濞存粍绮撻悡顐﹀炊閵婏箑鏆楁繝鈷�鍥︽喚闁哄备鍓濋幏鍛村川婵犲倹鐏庨梻浣告惈閺堫剙煤濠靛牏涓嶆繛鎴炵懅缁★拷鏌涢幇顓熷珪闁归鍏樺缁樻媴閸濄儳楔闂佺尨鎷峰畷顒勨�旈姀銈嗗癄濠㈣埖顭囬敓钘夆攽閻愬弶鈻曞ù婊勭矊椤斿繐鈹戦崱蹇旀杸闂佺粯蓱瑜板啴顢旈妶鍡曠箚闁圭粯甯炴晶锔芥叏婵犲啯銇濇鐐村姈閹棃濮�椤ゅ海鍠橀梻鍌欒兌椤牏鑺遍懖鈺佺筏閻犲洤寮☉銏狀澘闁靛牆妫岄幏娲⒒閸屾氨澧涚紒瀣尵缁鎮欓悜妯煎幈婵犵數濮撮崯鐗堟櫠閻㈠憡鐓欐い鏃傛閹风兘鏌嶇拠鏌ュ弰妤犵偞顭囬敓鐣岀矙閸喚鐤勯梻鍌氬�烽懗鍫曞箠閹剧粯鍋ら柕濞炬櫆閸嬶繝鏌曟径鍡樻珕闁哄懐顭堥湁闁绘ê妯婇崕鎰版煕婵犲倻浠涢柟渚垮妼铻ｉ柣鎾崇凹婢规洟鏌ｆ惔銏㈠暡闁瑰嚖鎷�

			
			//<!-- modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB begin -->
			/*CertProcessor.checkUserCert(user, action);*/
			CertProcessor.checkUserCert(user, Utils.null2EmptyWithTrim(paraMap.get("UserCert")));
			//<!-- modify by long_zg 2015-05-06 for CR202  Application Level Encryption for BOL&BOB end -->
		}

		// int loginTimes = Utils.nullEmpty2Zero(user.getLoginTimes());

		user.setLoginFailTimes(new Integer(0));
		// user.setLoginTimes(new Integer(loginTimes));
		user.setLoginStatus(CorpUser.LOGIN_STATUS_SUCCESSFUL);
		user.setOnlineStatus(CorpUser.ONLINE_STATUS_ON);

		// 闂傚倸鍊搁崐鎼佸磹閹间礁纾归柟闂寸绾惧綊鏌熼梻瀵割槻缁炬儳缍婇弻鐔兼⒒鐎靛壊妲荤紒鐐劤濠�閬嶆晸娴犲绀嬫い鎺炴嫹閻濈敻鎮峰鍛暭閻㈩垱甯￠幃娆愮節閸曨剙鏋戦棅顐㈡处缁嬫垵效閺屻儲鐓ラ柡鍥╁仜閿熶粙鏌ｉ幘瀛樼闁靛洤瀚伴、姗�鎮㈡搴濇樊濠电偛鐡ㄧ划宥囨崲閸儱钃熸繛鎴炃氶弸搴ㄧ叓閸ャ劍绀堟い鏃�甯″娲传閸曨厾浼囬梺鍝ュУ閻楃娀鎮伴锟藉耿婵炴垶顭囨鍥⒑閸濄儱鏋庨柛鐕佸灦楠炴垿宕堕锟芥憰濠电偞鍨堕悷褏寮ч敓鑺ョ厱閻忕偛澧介埥澶娒归悩铏唉婵﹥妞藉畷顐﹀礋椤曞懏鐎梻浣告啞濮婂綊宕归悽鍓叉晪闁挎繂顦荤粻缁樸亜閺囩偞鍣洪柡鍛矒濮婃椽宕ㄦ繝浣虹箒闂佸憡鐟ョ�涒晝锟芥總绋跨缂備焦菤閹风粯绻涙潏鍓хК婵炲拑绲块弫顔尖槈閵忥紕鍘搁柣蹇曞仩椤曆勪繆婵傚憡鎳氶柣鎰嚟缁犻箖鏌涢埄鍏╂垹浜搁鐘电＜闁绘﹢娼ф禒閬嶆煛鐏炵偓绀冪紒缁樼洴瀹曞綊顢欓悡搴）闂傚倷绀侀幖顐﹀疮椤愶箑纾诲┑鐘叉搐缁狀垶姊洪崹顕呭剭濞存粍绮撻弻鐔煎箥閾忣偅鐝旈梺缁樺笒閹碱偊婀侀梺绋跨箰閸氬鐣风仦鐐弿濠电姴瀚敮娑㈡煙瀹勭増鍣介柟鍙夋尦瀹曠喖鍩￠敓钘夌厱闂傚倸鍊风粈渚�骞夐敍鍕畳缂傚倷绶￠崰妤呮偡閳哄倷绻嗛柟缁㈠枛缁�鍐┿亜閺冨洤袚婵炲懌鍨藉铏圭磼濮楀棛鍔搁柣蹇撴禋娴滎亪鐛Δ鍛嵆闁绘劏鏅滈弬锟芥⒑缂佹ê濮堟繛鍏肩懇閹﹢鎮㈤崗鑲╁幈闂侀潧顭堥崕宕囩矓濞差亝鐓欐い鏃�鍎虫禒婊堟偂閵堝棎浜滈煫鍥ㄦ尰閸ｄ粙鏌涘锟芥喚婵﹪缂氶妵鎰板箳閹存粌鏋堥梻浣告啞閹稿鎮烽埡鍛槬婵炴垯鍨圭猾宥夋煕椤愩倕鏋旈柛姗�浜跺娲传閸曨偅娈梺绋匡攻閹倸鐣烽姀顤庢嫹婵☆垵顕у鎸庣節閻㈤潧孝闁稿﹤缍婇獮鍡涘醇閵夛妇鍘介梺鎸庣箓濞层倝宕㈢�甸潻鎷烽柡鍥╁仧閸╋綁鏌涢埞鎯у⒉闁瑰嘲鎳忕粭鐔碱敍濮橆偉绻曢梻鍌氬�搁崐鎼佸磹妞嬪海鐭嗗〒姘炬嫹缁犺銇勯幇鈺侊拷闁稿鐗楅妵鍕箛閸撲胶鏆犵紓渚婃嫹椤т線寮婚悢鍛婄秶闁告挆鍛闂備礁鎼�氥劑宕曢悽绋胯摕闁挎繂顦荤粻娑欍亜閹哄秶鍔嶉柛搴㈡尵缁辨挻鎷呴崫鍕碉拷缂傚倸绉崇欢姘嚕婵犳艾惟闁宠桨鑳堕惈鍕⒑閹肩偛鍔撮柣鎾崇墕铻為柡鍐挎嫹缁犻箖鎮楀☉娆樼劷闁活厼锕︾槐鎾愁吋閸滃啫浼愬銈庡墮椤﹁崵鍙呭銈呯箰鐎氼剛绮ｅ☉娆戠瘈闁汇垽娼у瓭闁诲孩鍑归崣鍐ㄧ暦鐎圭媭妯夊銈庝簵閸ㄨ姤淇婇幆鎵杸闁哄洨濮靛▓鐑樹繆閻愵亜锟芥繛澶涚畵瀹曟儼顧�闁告ɑ妞介幃妤呯嵁閸喖濮庡銈忕畳椤曆兠洪崸妤佲拻濞达綀顬冮崑鐘绘煕鎼搭喖娅嶇�殿喗褰冮埞鎴狅拷椤愶絾鐝繝鐢靛█濞佳兠洪妸褏涓嶅Δ锝呭暙缁狙囨煕椤愶絿绠撻柍閿嬫⒒缁辨帗寰勫Ο鐑樼彎闂佸搫鐭夌换婵嬪极閹捐绠ｉ柟鐑樻尭閺嬨倗绱撴担鎻掑⒉闁圭懓娲璇测槈閵忕姷鐤�闂傚倸鐗婄粙鎺楁倶瀹ュ鈷戠紓浣股戠亸鐗堢箾閸欏＃鎴犵矚鏉堛劎绡�闁搞儺鐏涜閺屾盯寮撮妸銉ュ濠碘剝褰冮…宄邦瀴閸濆嫮鏆﹂柛銉㈡櫅缁椻�斥攽閻愭彃绾х紒顔芥崌瀵偊骞樼�靛壊婀掗柣搴到閻忔岸寮查埡鍐＜闁绘劦鍓欓婊兠瑰搴″⒋鐎规洏鍎抽敓浠嬵敊鐟欙絾瀚奸梺鑽ゅТ濞茬娀鏁撴禒瀣嚑闁哄啫鐗婇悡鏇㈡煏婵炲灝锟介柣顓熺懄椤ㄥジ鎮欑拠褑鍚悗娈垮枙缁瑩銆佸锟藉�烽梻鍫熺☉缁犵増绻濋悽闈涗哗闁规椿浜炲濠冪鐎ｎ亞顔戝┑鐘诧工閹虫劙鎯屽▎蹇婃斀闁绘ê纾。鏌ユ煃闁垮娴柡灞剧〒娴狅箓宕滆閸ｎ喖顪㈤妶蹇曠暠鐎殿喖澧庨幑銏犫攽鐎ｎ偅娅㈤梺鐟扮摠鐢帡骞嗛崼銉︹拺闁告稑锕ょ粭姘舵偨椤栥倗绡�闁靛棔绀侀埥澶婎灃閸℃瑥寮抽梻浣告啞濞诧箓宕滃棰濇晩闁搞儺鍓氶埛鎺懨归敐鍛暈閻犳劧绻濋弻娑欐償濞戞﹫鎷峰Δ鐘靛仦閸旀牜鎹㈠┑鍡╃亜妞ゆ帒鍊婚崢顒勬⒒婵犲骸浜滄繛璇у缁瑩骞掑Δ浣镐簵闂婎偄娲︾粙鎺楁偂閺囩喆浜滈柟鏉垮閹偐绱掗悩顔煎姕濞ｅ洤锕、娑橆灄椤撶偞鐏庨梻浣告惈閼活垳绮旇ぐ鎺戣摕闁靛ě宥嗗濡炪倕绻愬Λ鏃傛濠靛洨绡�婵炲牆鐏濋弸娑㈡煥閺囨ê锟介柍銉畱閻ｏ繝骞嶉鑺ヮ啎濠电娀娼ч崐鎼佸箟閿熺姴纾婚柛鏇ㄥ灡閸嬬姵绻涢幋鐐茬瑨濠⒀冨级閵囧嫭鎯旈埄鍐ㄦ灎濠殿喖锕︾划顖炲箯閸涘瓨鍋￠梺顓ㄧ畱濞堟繄绱撻崒娆戭槻妞ゆ垵妫涢弫顕�骞掗弴鐘辩綍闂傚倷绀侀幉锟犲礉閿曞倸绐楅柡宥庡幗鐎氬懘鏌ｉ弬鍨倯闁绘挾鍠栭弻锟犲礃閵娿儻鎷烽柣銏╁灠婢у海妲愰幒妤佹櫢闁芥ê顧夋导鍐⒑閸濆嫭婀版繛鑼枎閻ｇ兘鎮℃惔妯绘杸闂佹悶鍎撮崺鏍晬鐏炲彞绻嗛柣鎰典簻閿熻棄螖閻樿尙绠绘鐐茬箻閺佹捇鏁撻敓锟�

		writeLoginReport(user, action);
		// if (loginTimes == 0) {
		// /*
		// * LOGIN_DATE CHAR(8), LOGIN_TIME CHAR(6), USER_ID CHAR(12),
		// * REFERENCE_ID CHAR(10), CORPORATION_ID CHAR(10), STATUS CHAR(1)
		// */
		// Map reportMap1 = new HashMap();
		// reportMap1.put("LOGIN_DATE", DateTime.formatDate(user
		// .getCurrLoginTime(), "yyyyMMdd"));
		// reportMap1.put("LOGIN_TIME", DateTime.formatDate(user
		// .getCurrLoginTime(), "HHmmss"));
		// reportMap1.put("USER_ID", user.getUserId());
		// // reportMap1.put("REFERENCE_ID", inputPassword);
		// reportMap1.put("CORPORATION_ID", user.getCorpId());
		// reportMap1.put("STATUS", user.getLoginStatus());
		// UploadReporter.write("RP_FIRSTLOGIN", reportMap1);
		// } else {
		//
		// /*
		// * LOGIN_DATE CHAR(8), LOGIN_TIME CHAR(6), USER_ID CHAR(12),
		// * CORPORATION_ID CHAR(10), STATUS CHAR(1), RETRY_COUNTER CHAR(3),
		// * PN_SIGN CHAR(1)
		// */
		// Map reportMap = new HashMap();
		// reportMap.put("LOGIN_DATE", DateTime.formatDate(user
		// .getCurrLoginTime(), "yyyyMMdd"));
		// reportMap.put("LOGIN_TIME", DateTime.formatDate(user
		// .getCurrLoginTime(), "HHmmss"));
		// reportMap.put("SEQ_NO", action.getSession().getId());
		// reportMap.put("USER_ID", user.getUserId());
		// reportMap.put("CORPORATION_ID", user.getCorpId());
		// reportMap.put("STATUS", user.getLoginStatus());
		// reportMap.put("RETRY_COUNTER", user.getLoginFailTimes());
		// reportMap.put("PN_SIGN", "+");
		// UploadReporter.write("RP_LOGIN", reportMap);
		// loginTimes++;
		// }

		corpUserService.update(user);
		// 闂傚倸鍊搁崐鎼佸磹閹间礁纾归柟闂寸绾惧綊鏌熼梻瀵割槻缁炬儳缍婇弻鐔兼⒒鐎靛壊妲荤紒鐐劤濠�閬嶆晸娴犲绀嬫い鎺炴嫹閻濈敻鎮峰鍛暭閻㈩垱甯￠幃娆愮節閸曨剙鏋戦棅顐㈡处缁嬫垵效閺屻儲鐓ラ柡鍥╁仜閿熶粙鏌ｉ幘瀛樼闁靛洤瀚伴、姗�鎮㈡搴濇樊濠电偛鐡ㄧ划宥囨崲閸儱钃熸繛鎴炃氶弸搴ㄧ叓閸ャ劍绀堟い鏃�甯″娲传閸曨厾浼囬梺鍝ュУ閻楃娀鎮伴锟藉耿婵炴垶顭囨鍥⒑閸濄儱鏋庨柛鐕佸灦楠炴垿宕堕锟芥憰濠电偞鍨堕悷褏寮ч敓鑺ョ厱閻忕偛澧介埥澶娒归悩铏唉婵﹥妞藉畷顐﹀礋椤曞懏鐎梻浣告啞濮婂綊宕归悽鍓叉晪闁挎繂顦荤粻缁樸亜閺囩偞鍣洪柡鍛矒濮婃椽宕ㄦ繝浣虹箒闂佸憡鐟ョ�涒晝锟芥總绋跨缂備焦菤閹风粯绻涙潏鍓хК婵炲拑绲块弫顔尖槈閵忥紕鍘搁柣蹇曞仩椤曆勪繆婵傚憡鎳氶柣鎰嚟缁犻箖鏌涢埄鍏╂垹浜搁鐘电＜闁绘﹢娼ф禒閬嶆煛鐏炵偓绀冪紒缁樼洴瀹曞綊顢欓悡搴）闂傚倷绀侀幖顐﹀疮椤愶箑纾诲┑鐘叉搐缁狀垶姊洪崹顕呭剭濞存粍绮撻弻鐔煎箥閾忣偅鐝旈梺缁樺笒閹碱偊婀侀梺绋跨箰閸氬鐣风仦鐐弿濠电姴瀚敮娑㈡煙瀹勭増鍣介柟鍙夋尦瀹曠喖鍩￠敓钘夌厱闂傚倸鍊风粈渚�骞夐敍鍕畳缂傚倷绶￠崰妤呮偡閳哄倷绻嗛柟缁㈠枛缁�鍐┿亜閺冨洤袚婵炲懌鍨藉铏圭磼濮楀棛鍔搁柣蹇撴禋娴滎亪鐛Δ鍛嵆闁绘劏鏅滈弬锟芥⒑缂佹ê濮堟繛鍏肩懇閹﹢鏁冮崒娑樻闂侀潧鐗嗛ˇ濂告偂濞戙垺鐓ラ柣鏃傚劋濞呮洟鏌ｉ鐔烘噰闁哄矉绱曟禒锕傚礈瑜庨崚娑橆湤閵堝啫鐏繛鑼枛楠炲啴鍩￠崨顓狀唽闂佸湱鍎ら崺鍫ユ倵閻戣姤鈷掗柛灞剧懆閸忓本銇勯鐐靛ⅵ妞ゃ垺鐗犲畷鍗炩槈濞嗗繋绮ч梻浣侯攰娴滎剟鎮欓敓锟�

		if (cachedUser != null) {
			cachedUser.setOnlineStatus(CorpUser.ONLINE_STATUS_OFF);
			cachedUser.setSessionId("");
		}
		userCache.put(userKey, user);
		// 闂傚倸鍊搁崐鎼佸磹閹间礁纾归柟闂寸绾惧綊鏌熼梻瀵割槻缁炬儳缍婇弻鐔兼⒒鐎靛壊妲荤紒鐐劤濠�閬嶆晸娴犲绀嬫い鎺炴嫹閻濈敻鎮峰鍛暭閻㈩垱甯￠幃娆愮節閸曨剙鏋戦棅顐㈡处缁嬫垵效閺屻儲鐓ラ柡鍥╁仜閿熶粙鏌ｉ幘瀛樼闁靛洤瀚伴、姗�鎮㈡搴濇樊濠电偛鐡ㄧ划宥囨崲閸儱钃熸繛鎴炃氶弸搴ㄧ叓閸ャ劍绀堟い鏂匡躬濮婅櫣鎹勯妸銉︾�虹紓渚囧枟閻熴儵顢氶敐澶婄妞ゆ棁妫勯敓浠嬫⒑閸濆嫷妯囬柛銊ョ秺閹即鍩￠崨顔规嫽婵炶揪缍�椤啴濡甸悢鍏肩厱婵☆垳鍘х敮鍫曟懚閻愮繝绻嗛柕鍫濇噺閸ｈ櫣绱掗悩鍐叉诞婵﹦绮幏鍛存偡闁箑娈濈紓鍌欐祰椤曆囧磹閸ф绠栫憸宥夆�﹂妸鈺侀唶闁绘柨鍤栭幏閿嬩繆閻愵亜锟芥慨濠傤煼瀵煡顢曢妶鍥︾瑝婵°倧绲介崯顖炴偂閺囩喓绠鹃柟瀛樼箓閼稿綊鏌ｈ箛鏇炐ラ柨鐔绘閳规垹锟介鐔稿闂備焦鎮堕崕婊呬沪缂侇枎鎷烽梻鍌欑閹碱偊鎯屾径灞界筏闁稿﹦鍠撴禍浠嬫⒒娴ｈ櫣甯涢柛銊﹀劶瑜版粓姊洪崫鍕靛剱闁荤啿鏅涢‖澶愭惞閸︻厾鐓撻梺鍓茬厛閸ｎ噣宕濇径鎰拺闁告稑锕ラ埛鎰版煟閻斿弶娅呮い鏇悼閹风姴霉鐎ｎ偒鐎叉繝娈垮枟椤洭宕戦幘璁规嫹鐟滅増甯楅埛鎴犵磼鐎ｎ厽纭剁紒鐘崇叀閺岀喖顢欓悡搴嫹閻庤娲橀崹浣冪亙闂佸憡渚楅崰鎺楀箯濞差亝鈷戦柛娑橈功缁犳捇鎮楀鐓庡箹闁挎洏鍨虹�佃偐锟藉Ο鐓庡箺闂備礁鍟块幖顐︽晝閵堝鍊堕柨鏃�鎷濋幏鐑芥煛閸モ晛鏋戦柕鍥ㄧ箘閿熶粙寮撮悩鍙夌亪濡炪們鍨虹粙鎴︹�栧Δ渚囩�烽柡宥冨壉椤掞拷鏀介柨娑樺娴滃ジ鏌涙繝鍐╃闁瑰箍鍨婚敓浠嬵敊閽樺鎷烽梻浣瑰濞叉牠宕戦崱娑樻瀬濞撴熬鎷烽悡鏇㈡煏婢舵ê鏋涘褜鍨堕弻宥夋煥鐎ｎ亞鐟ㄩ梻鍥ь檨閺屻劌鈹戦崱妤婁紓濡炪倖甯為崰鏍蓟閿濆绠抽柡鍌氥仒婢规洟姊洪棃娑欐悙閻庢矮鍗抽悰顔撅拷椤忎焦鏂�闂佺硶鍓濊摫妞ゆ梹甯￠弻锝嗘償閵堝海锟藉┑鐐插级椤洭骞戦姀銈呴唶闁靛／鍐偊闂佽鍑界紞鍡涘窗濡ゅ懎鐤炬繝闈涱儐閻撱儵鏌ｉ弮鍥跺殭鐞氭岸姊虹涵鍛处闁告鍟块‖澶岀磼濡顎撻梺鍛婄☉閿曘儵宕曢幘鏂ユ斀闁绘劘灏欏﹢鎾煕閺冿拷鐏ラ柣锝夋敱鐎靛ジ寮堕幋鐙�鍟嬮梺鑽ゅТ閹碱偊骞栭銉㈠彚闂傚倸鍊峰ù鍥р枖閺囥垹绐楅柟鎹愵嚙閻ょ偓銇勯幇鍓佺暠闁活厽顨￠弻鐔煎礈瑜忕敮娑㈡煟閹惧瓨绀嬮柡灞炬礃瀵板嫬鈽夊顒�鐓傚┑鐘殿暜缁辨洟寮查銈嗩瀼闁圭儤顨濋弲鎼佹煟閿濆懓瀚伴柍顏嗘暬濮婃椽宕崟顓犲姽缂備胶绮换鍌炴偩閻戣棄纭�闁绘劏鏅滈悗濠氭椤愩垺鎼愰柨鏇樺劦閹鎮ч崼銏㈢槇濠电偛鐗嗛悘婵嬪几濞戙垺鐓ラ柡鍥俊濂告煃鐠囪尙效闁轰焦鍔欏畷鍫曗�﹂幋鐙呮嫹闂傚倷绀佸﹢閬嶅磿閵堝鍚归柨鏇炲�归崑鍕煟閹寸伝顏堟偟娴煎瓨鈷戦柛婵嗗椤ゅ棝鏌涙惔銏犫枙闁轰焦鎹囬幐濠冨緞閸℃ɑ鏉搁梻浣虹帛閿氶柣蹇斿哺瀵娊鍩￠崨顔惧幈闁诲函缍嗛崜娆忔毄濠电偛鐡ㄧ划搴ㄢ�﹂崼銉ョ厺閹兼番鍔岀粻鑽わ拷閺囩偞宸濈�规洖鐖煎缁樻媴閸涘﹥鍎撻柣鐘叉川閸嬬喐绂掗敃鍌涘殟闁靛鍠栭幃顏堟⒒閸屾瑦绁扮�规洖鐏氶幈銊╁级閹炽劍妞介弫鍌炲礈瑜忛悞楣冩⒑閸濆嫷妯囬柛銈忕畵閺佹劖寰勬繝鍥ф暪闂備胶绮Λ浣糕枍閿濆鍎婇柛顐ゅ枔缁★拷鏌ｉ姀銈嗘锭鐎涙繈姊虹粙鍖″伐缂傚秴锕ら悾鐑芥惞椤愩値婀掗柣搴秵閸嬪棝宕㈤崨濠勭瘈闁靛骏绲剧涵鐐亜閹存繃顥�规洘鍔欓獮鍥敇閻斿嘲浼庡┑鐘垫暩婵挳宕鐐参︽繝闈涙祩濮ｃ倝鏌ｉ幇顓熺稇濠殿喖鐗撻弻鐔碱敍濞戞瑯妫撻梺杞扮劍閸旀瑥鐣烽崼鏇炵厸闁稿本渚楅崯灞解攽閻樺灚鏆╁┑顔惧厴閵嗗倿鎸婃竟鈺嬬秮瀹曘劑寮堕幋鐙呯幢闂備礁婀遍崕銈夈�冨鍫熷剹婵°倕鎳忛悡鏇㈡煙閹佃櫕娅呭┑鈥虫健閺岋繝宕遍鐘垫殼闂佸搫鏈ú妯侯嚗閸曨垰骞㈡俊顖濆吹瑜板懐绱撴担楦挎闁告瑥绻橀獮蹇涙倻閽樺鎽曢梺缁樻⒒閳峰牓寮崶顒佺厽婵☆垰鐏濋惃鍝劽瑰鍜冩嫹ion
		action.setUser(user);

		return user;
	}

	public void writeLoginReport(CorpUser user, NTBAction action)
			throws NTBException {
		int loginTimes = Utils.nullEmpty2Zero(user.getLoginTimes());
		if (loginTimes == 0) {
			/*
			 * LOGIN_DATE CHAR(8), LOGIN_TIME CHAR(6), USER_ID CHAR(12),
			 * REFERENCE_ID CHAR(10), CORPORATION_ID CHAR(10), STATUS CHAR(1)
			 */
			Map reportMap1 = new HashMap();
			reportMap1.put("LOGIN_DATE", DateTime.formatDate(user
					.getCurrLoginTime(), "yyyyMMdd"));
			reportMap1.put("LOGIN_TIME", DateTime.formatDate(user
					.getCurrLoginTime(), "HHmmss"));
			reportMap1.put("USER_ID", user.getUserId());
			// reportMap1.put("REFERENCE_ID", inputPassword);
			reportMap1.put("CORPORATION_ID", user.getCorpId());
			reportMap1.put("STATUS", user.getLoginStatus());

			// update by xyf 20081223, add ip file
			reportMap1.put("LOGIN_IP", action.getRequestIP());
			UploadReporter.write("RP_FIRSTLOGIN", reportMap1);
		} else {

			/*
			 * LOGIN_DATE CHAR(8), LOGIN_TIME CHAR(6), USER_ID CHAR(12),
			 * CORPORATION_ID CHAR(10), STATUS CHAR(1), RETRY_COUNTER CHAR(3),
			 * PN_SIGN CHAR(1)
			 */
			Map reportMap = new HashMap();
			reportMap.put("LOGIN_DATE", DateTime.formatDate(user
					.getCurrLoginTime(), "yyyyMMdd"));
			reportMap.put("LOGIN_TIME", DateTime.formatDate(user
					.getCurrLoginTime(), "HHmmss"));
			reportMap.put("SEQ_NO", action.getSession().getId());
			reportMap.put("USER_ID", user.getUserId());
			reportMap.put("CORPORATION_ID", user.getCorpId());
			reportMap.put("STATUS", user.getLoginStatus());
			reportMap.put("RETRY_COUNTER", user.getLoginFailTimes());
			reportMap.put("PN_SIGN", "+");

			// update by xyf 20081223, add ip file
			reportMap.put("LOGIN_IP", action.getRequestIP());
			UploadReporter.write("RP_LOGIN", reportMap);

			if ((CorpUser.LOGIN_STATUS_SUCCESSFUL)
					.equals(user.getLoginStatus())) {
				loginTimes++;
			}
		}
		user.setLoginTimes(new Integer(loginTimes));
	}

	public void logout(CorpUser user, NTBAction action) throws NTBException {
		// 闂傚倸鍊搁崐鎼佸磹閹间礁纾归柟闂寸绾惧綊鏌熼梻瀵割槻缁炬儳缍婇弻鐔兼⒒鐎靛壊妲荤紒鐐劤濠�閬嶆晸娴犲绀嬫い鎺炴嫹閻濈敻鎮峰鍛暭閻㈩垱甯￠幃娆愮節閸曨剙鏋戦棅顐㈡处缁嬫垵效閺屻儲鐓ラ柡鍥╁仜閿熶粙鏌ｉ幘瀛樼闁靛洤瀚伴、姗�鎮㈡搴濇樊濠电偛鐡ㄧ划宥囨崲閸儱钃熸繛鎴炃氶弸搴ㄧ叓閸ャ劍绀堟い鏃�甯″娲传閸曨厾浼囬梺鍝ュУ閻楃娀鎮伴锟藉耿婵炴垶顭囨鍥⒑閸濄儱鏋庨柛鐕佸灦楠炴垿宕堕锟芥憰濠电偞鍨堕悷褏寮ч敓鑺ョ厱閻忕偛澧介埥澶娒归悩铏唉婵﹥妞藉畷顐﹀礋椤曞懏鐎梻浣告啞濮婂綊宕归悽鍓叉晪闁挎繂顦荤粻缁樸亜閺囩偞鍣洪柡鍛矒濮婃椽宕ㄦ繝浣虹箒闂佸憡鐟ョ�涒晝锟芥總绋跨缂備焦菤閹风粯绻涙潏鍓хК婵炲拑绲块弫顔尖槈閵忥紕鍘搁柣蹇曞仩椤曆勪繆婵傚憡鎳氶柣鎰嚟缁犻箖鏌涢埄鍏╂垹浜搁鐘电＜闁绘﹢娼ф禒閬嶆煛鐏炵偓绀冪紒缁樼洴瀹曞綊顢欓悡搴）闂傚倷绀侀幖顐﹀疮椤愶箑纾诲┑鐘叉搐缁狀垶姊洪崹顕呭剭濞存粍绮撻弻鐔煎箥閾忣偅鐝旈梺缁樺笒閹碱偊婀侀梺绋跨箰閸氬鐣风仦鐐弿濠电姴瀚敮娑㈡煙瀹勭増鍣介柟鍙夋尦瀹曠喖鍩￠敓钘夌厱闂傚倸鍊风粈渚�骞夐敍鍕畳缂傚倷绶￠崰妤呮偡閳哄倷绻嗛柟缁㈠枛缁�鍐┿亜閺冨洤袚婵炲懌鍨藉铏圭磼濮楀棛鍔搁柣蹇撴禋娴滎亪鐛Δ鍛嵆闁绘劏鏅滈弬锟芥⒑缂佹ê濮堟繛鍏肩懇閹﹢鎮㈤崗鑲╁幈闂侀潧顭堥崕宕囩矓濞差亝鐓欐い鏃�鍎虫禒婊堟偂閵堝棎浜滈煫鍥ㄦ尰閸ｄ粙鏌涘锟芥喚婵﹪缂氶妵鎰板箳閹存粌鏋堥梻浣告啞閹稿鎮烽埡鍛槬婵炴垯鍨圭猾宥夋煕椤愩倕鏋旈柛姗�浜跺娲传閸曨偅娈梺绋匡攻閹倸鐣烽姀顤庢嫹婵☆垵顕у鎸庣節閻㈤潧孝闁稿﹤缍婇獮鍡涘醇閵夛妇鍘介梺鎸庣箓濞层倝宕㈢�甸潻鎷烽柡鍥╁仧閸╋綁鏌涢埞鎯у⒉闁瑰嘲鎳忕粭鐔碱敍濮橆偉绻曢梻鍌氬�搁崐鎼佸磹妞嬪海鐭嗗〒姘炬嫹缁犺銇勯幇鈺侊拷闁稿鐗楅妵鍕箛閸撲胶鏆犵紓渚婃嫹椤т線寮婚悢鍛婄秶闁告挆鍛闂備礁鎼�氥劑宕曢悽绋胯摕闁挎繂顦荤粻娑欍亜閹哄秶鍔嶉柛搴㈡尵缁辨挻鎷呴崫鍕碉拷缂傚倸绉崇欢姘嚕婵犳艾惟闁宠桨鑳堕惈鍕⒑閹肩偛鍔撮柣鎾崇墕铻為柡鍐挎嫹缁犻箖鎮楀☉娆樼劷闁活厼锕︾槐鎾愁吋閸滃啫浼愬銈庡墮椤﹁崵鍙呭銈呯箰鐎氼剛绮ｅ☉娆戠瘈闁汇垽娼у瓭闁诲孩鍑归崣鍐ㄧ暦鐎圭媭妯夊銈庝簵閸ㄨ姤淇婇幆鎵杸闁哄洨濮靛▓鐑樹繆閻愵亜锟芥繛澶涚畵瀹曟儼顧�闁告ɑ妞介幃妤呯嵁閸喖濮庡銈忕畳椤曆兠洪崸妤佲拻濞达綀顬冮崑鐘绘煕鎼搭喖娅嶇�殿喗褰冮埞鎴狅拷椤愶絾鐝繝鐢靛█濞佳兠洪妸褏涓嶅Δ锝呭暙缁狙囨煕椤愶絿绠撻柍閿嬫⒒缁辨帗寰勫Ο鐑樼彎闂佸搫鐭夌换婵嬪极閹捐绠ｉ柟鐑樻尭閺嬨倗绱撴担鎻掑⒉闁圭懓娲璇测槈閵忕姷鐤�闂傚倸鐗婄粙鎺楁倶瀹ュ鈷戠紓浣股戠亸鐗堢箾閸欏＃鎴犵矚鏉堛劎绡�闁搞儺鐏涜閺屾盯寮撮妸銉ュ濠碘剝褰冮…宄邦瀴閸濆嫮鏆﹂柛銉㈡櫅缁椻�斥攽閻愭彃绾х紒顔芥崌瀵偊骞樼�靛壊婀掗柣搴到閻忔岸寮查埡鍐＜闁绘劦鍓欓婊兠瑰搴″⒋鐎规洏鍎抽敓浠嬵敊鐟欙絾瀚奸梺鑽ゅТ濞茬娀鏁撴禒瀣嚑闁哄啫鐗婇悡鏇㈡煏婵炲灝锟介柣顓熺懄椤ㄥジ鎮欑拠褑鍚悗娈垮枙缁瑩銆佸锟藉�烽梻鍫熺☉缁犵増绻濋悽闈涗哗闁规椿浜炲濠冪鐎ｎ亞顔戝┑鐘诧工閹虫劙鎯屽▎蹇婃斀闁绘ê纾。鏌ユ煃闁垮娴柡灞剧〒娴狅箓宕滆閸ｎ喖顪㈤妶蹇曠暠鐎殿喖澧庨幑銏犫攽鐎ｎ偅娅㈤梺鐟扮摠鐢帡骞嗛崼銉︹拺闁告稑锕ょ粭姘舵偨椤栥倗绡�闁靛棔绀侀埥澶婎灃閸℃瑥寮抽梻浣告啞濞诧箓宕滃棰濇晩闁搞儺鍓氶埛鎺懨归敐鍛暈閻犳劧绻濋弻娑欐償濞戞﹫鎷峰Δ鐘靛仦閸旀牜鎹㈠┑鍡╃亜妞ゆ帒鍊婚崢顒勬⒒婵犲骸浜滄繛璇у缁瑩骞掑Δ浣镐簵闂婎偄娲︾粙鎺楁偂閺囩喆浜滈柟鏉垮閹偐绱掗悩顔煎姕濞ｅ洤锕、娑橆灄椤撶偞鐏庨梻浣告惈閼活垳绮旇ぐ鎺戣摕闁靛ě宥嗗濡炪倕绻愬Λ鏃傛濠靛洨绡�婵炲牆鐏濋弸娑㈡煥閺囨ê锟介柍銉畱閻ｏ繝骞嶉鑺ヮ啎濠电娀娼ч崐鎼佸箟閿熺姴纾婚柛鏇ㄥ灡閸嬬姵绻涢幋鐐茬瑨濠⒀冨级閵囧嫭鎯旈埄鍐ㄦ灎濠殿喖锕︾划顖炲箯閸涘瓨鍋￠梺顓ㄧ畱濞堟繄绱撻崒娆戭槻妞ゆ垵妫涢弫顕�骞掗弴鐘辩綍闂傚倷绀侀幉锟犲礉閿曞倸绐楅柡宥庡幗鐎氬懘鏌ｉ弬鍨倯闁绘挾鍠栭弻锟犲礃閵娿儻鎷烽柣銏╁灠婢у海妲愰幒妤佹櫢闁芥ê顧夋导鍐⒑閸濆嫭婀版繛鑼枎閻ｇ兘鎮℃惔妯绘杸闂佹悶鍎撮崺鏍晬鐏炲彞绻嗛柣鎰典簻閿熻棄螖閻樿尙绠绘鐐茬箻閺佹捇鏁撻敓锟�

		/*
		 * LOGOUT_DATE CHAR(8), LOGOUT_TIME CHAR(6), USER_ID CHAR(12),
		 * CORPORATION_ID CHAR(10)
		 */
		Map reportMap = new HashMap();
		reportMap.put("LOGOUT_DATE", DateTime
				.formatDate(new Date(), "yyyyMMdd"));
		reportMap.put("LOGOUT_TIME", DateTime.formatDate(new Date(), "HHmmss"));
		reportMap.put("SEQ_NO", action.getSession().getId());
		reportMap.put("USER_ID", user.getUserId());
		reportMap.put("CORPORATION_ID", user.getCorpId());
		UploadReporter.write("RP_LOGOUT", reportMap);

	}

	public CorpUserDao getCorpUserDao() {
		return corpUserDao;
	}

	public void setCorpUserDao(CorpUserDao corpUserDao) {
		this.corpUserDao = corpUserDao;
	}

	public UserGroupDao getUserGroupDao() {
		return userGroupDao;
	}

	public void setUserGroupDao(UserGroupDao userGroupDao) {
		this.userGroupDao = userGroupDao;
	}

	public CorpPermissionDao getCorpPermissionDao() {

		return corpPermissionDao;
	}

	public CorporationDao getCorporationDao() {
		return corporationDao;
	}

	public void setCorpPermissionDao(CorpPermissionDao corpPermissionDao) {

		this.corpPermissionDao = corpPermissionDao;
	}

	public void setCorporationDao(CorporationDao corporationDao) {
		this.corporationDao = corporationDao;
	}

	public CorpUserHisDao getCorpUserHisDao() {
		return corpUserHisDao;
	}

	public void setCorpUserHisDao(CorpUserHisDao corpUserHisDao) {
		this.corpUserHisDao = corpUserHisDao;
	}

	public void addCorpUserHis(CorpUserHis corpUserHis) throws NTBException {
		corpUserHisDao.add(corpUserHis);

	}
	
	//modified by lzg for GAPMC-EB-001-0040
	public CorpUserHis loadPendingHisWithCorpId(String userId,String corpId) throws NTBException {
		Map conditionMap = new HashMap();
		conditionMap.put("userId", userId);
		//add by lzg for GAPMC-EB-001-0040
		conditionMap.put("corpId",corpId);
		//add by lzg end
		conditionMap.put("status", Constants.STATUS_PENDING_APPROVAL);
		List userHisList = corpUserHisDao.list(CorpUserHis.class, conditionMap);
		if (userHisList == null || userHisList.size() == 0) {
			return null;
		}
		CorpUserHis userHis = (CorpUserHis) userHisList.get(0);
		return userHis;
	}
	//modified by lzg end
	public CorpUserHis loadHisBySeqNo(String seqNo) throws NTBException {
		Map conditionMap = new HashMap();
		conditionMap.put("seqNo", seqNo);
		List userHisList = corpUserHisDao.list(CorpUserHis.class, conditionMap);
		if (userHisList == null || userHisList.size() == 0) {
			return null;
		}
		CorpUserHis userHis = (CorpUserHis) userHisList.get(0);
		return userHis;
	}

	public CorpUser loadBySeqNo(String userId) throws NTBException {
		Log.info(userId);
		Map conMap = new HashMap();
		conMap.put("userId", userId);
		List userList = corpUserDao.list(CorpUser.class, conMap);
		
		if (userList == null || userList.size() == 0) {
			return null;
		}
		CorpUser user = (CorpUser) userList.get(0);
		return user;
	}

	public void updateCorpUserHis(CorpUserHis corpUserHis) throws NTBException {
		//modified by lzg for GAPMC-EB-001-0040
		//corpUserHisDao.update(corpUserHis);
		//String tableName = "CORP_USER_HIS";
		Map conditionMap = new HashMap();
		conditionMap.put("USER_ID", corpUserHis.getUserId());
		conditionMap.put("CORP_ID", corpUserHis.getCorpId());
		//genericJdbcDao.updateByObject(tableName,corpUserHis,conditionMap);
		//modified by lzg end
		
		Map columnMap = new HashMap();
	    columnMap.put("LIMIT6", corpUserHis.getLimit6());
	    columnMap.put("LIMIT10", corpUserHis.getLimit10());
	    columnMap.put("LIMIT5", corpUserHis.getLimit5());
	    columnMap.put("LIMIT4", corpUserHis.getLimit4());
	    columnMap.put("LIMIT3", corpUserHis.getLimit3());
	    columnMap.put("MOBILE_COUNTRY_CODE", corpUserHis.getMobileCountryCode());
	    columnMap.put("LIMIT9", corpUserHis.getLimit9());
	    columnMap.put("LIMIT8", corpUserHis.getLimit8());
	    columnMap.put("AUTH_STATUS", corpUserHis.getAuthStatus());
	    columnMap.put("LIMIT7", corpUserHis.getLimit7());
	    columnMap.put("LIMIT2", corpUserHis.getLimit2());
	    columnMap.put("LIMIT1", corpUserHis.getLimit1());
	    columnMap.put("ROLE_ID", corpUserHis.getRoleId());
	    columnMap.put("PREF_ID", corpUserHis.getPrefId());
	    columnMap.put("PREV_LOGIN_TIME", corpUserHis.getPrevLoginTime());
	    columnMap.put("VERSION", corpUserHis.getVersion());
	    columnMap.put("LAST_UPDATE_TIME", corpUserHis.getLastUpdateTime());
	    columnMap.put("REQUESTER", corpUserHis.getRequester());
	    columnMap.put("FINANCIAL_CONTROLLER_FLAG", corpUserHis.getFinancialControllerFlag());
	    columnMap.put("LOGIN_STATUS", corpUserHis.getLoginStatus());
	    columnMap.put("BLOCK_REASON", corpUserHis.getBlockReason());
	    columnMap.put("CERT_CARD_TYPE", corpUserHis.getCertCardType());
	    columnMap.put("GROUP_ID", corpUserHis.getGroupId());
	    columnMap.put("SECURITY_CODE", corpUserHis.getSecurityCode());
	    columnMap.put("LOGIN_FAIL_TIMES", corpUserHis.getLoginFailTimes());
	    columnMap.put("PREV_LOGIN_IP", corpUserHis.getPrevLoginIp());
	    columnMap.put("ID_ISSUE_DATE", corpUserHis.getIdIssueDate());
	    columnMap.put("MOBILE_AREA_CODE", corpUserHis.getMobileAreaCode());
	    columnMap.put("USER_DESC", corpUserHis.getUserDesc());
	    columnMap.put("FAX_NO", corpUserHis.getFaxNo());
	    columnMap.put("PERV_LOGIN_STATUS", corpUserHis.getPervLoginStatus());
	    columnMap.put("ID_TYPE", corpUserHis.getIdType());
	    columnMap.put("STATUS", corpUserHis.getStatus());
	    columnMap.put("AUTH_LEVEL", corpUserHis.getAuthLevel());
	    columnMap.put("EMAIL", corpUserHis.getEmail());
	    columnMap.put("CERT_INFO3", corpUserHis.getCertInfo3());
	    columnMap.put("CERT_INFO1", corpUserHis.getCertInfo1());
	    columnMap.put("CERT_INFO2", corpUserHis.getCertInfo2());
	    columnMap.put("LOGIN_ID", corpUserHis.getLoginId());
	    columnMap.put("FULL_NAME", corpUserHis.getFullName());
	    columnMap.put("ID_ISSUER", corpUserHis.getIdIssuer());
	    columnMap.put("CURR_LOGIN_TIME", corpUserHis.getCurrLoginTime());
	    columnMap.put("TELEPHONE", corpUserHis.getTelephone());
	    columnMap.put("USER_PASSWORD", corpUserHis.getUserPassword());
	    columnMap.put("ID_NO", corpUserHis.getIdNo());
	    columnMap.put("CURR_LOGIN_IP", corpUserHis.getCurrLoginIp());
	    columnMap.put("MOBILE", corpUserHis.getMobile());
	    columnMap.put("USER_NAME", corpUserHis.getUserName());
	    columnMap.put("GENDER", corpUserHis.getGender());
	    columnMap.put("OPERATION", corpUserHis.getOperation());
	    columnMap.put("MERCHANT_TYPE", corpUserHis.getMerchantType());
	    columnMap.put("LOGIN_TIMES", corpUserHis.getLoginTimes());
	    columnMap.put("VIEW_ALL_TRANS_FLAG", corpUserHis.getViewAllTransFlag());
	    columnMap.put("TITLE", corpUserHis.getTitle()); 
	    columnMap.put("AFTER_MODIFY_ID", corpUserHis.getAfterModifyId());
		try {
			genericJdbcDao.update("CORP_USER_HIS", columnMap, conditionMap);
		} catch (Exception e) {
			Log.error("Update CorpUserHis error", e);
			throw new NTBException("err.sys.GeneralError");
		}
	}

	public List listUserHisByPerfId(String prefId) throws NTBException {
		HashMap condictionMap = new HashMap();
		condictionMap.put("prefId", prefId);
		// condictionMap.put("status", Constants.STATUS_PENDING_APPROVAL);
		List resList = null;
		try {
			resList = corpUserDao.list(CorpUserHis.class, condictionMap);
		} catch (DataAccessResourceFailureException e) {
			throw new NTBException(
					"err.bnk.corpUserlist.DataAccessResourceFailureException");

		} catch (IllegalStateException e) {
			throw new NTBException("err.bnk.corpUserlist.IllegalStateException");

		}
		return resList;
	}

	public void loadUploadPin(CorpUser userObj) throws NTBException {
		Map uploadMap = new HashMap();
		uploadMap.put("TRANS_DATE", CibTransClient.getCurrentDate());
		uploadMap.put("TRANS_TIME ", CibTransClient.getCurrentTime());
		// uploadMap.put("PN_SIGN ", "+");
		uploadMap.put("USER_ID ", userObj.getUserId());
		uploadMap.put("CORPORATION_ID ", userObj.getCorpId());
		uploadMap.put("STATUS ", "");
		uploadMap.put("REJECT_CODE ", "");
		UploadReporter.write("RP_CHGPIN", uploadMap);

	}

	public List listUserHisByAfterId(String afterModifyId) throws NTBException {
		HashMap condictionMap = new HashMap();
		condictionMap.put("afterModifyId", afterModifyId);
		// condictionMap.put("status", Constants.STATUS_PENDING_APPROVAL);
		List resList = null;
		try {
			resList = corpUserDao.list(CorpUserHis.class, condictionMap);
		} catch (Exception e) {
			throw new NTBException("err.sys.DBError");
		}
		return resList;
	}

	public List listByRoleId(String corpId, String roleId, String status)
			throws NTBException {
		return corpUserDao.listByRoleId(corpId, roleId, status);
	}

	/**
	 * add by xyf 20081222, check period for PIN mailer time
	 */
	public boolean isInitPinActive(String userId, String corpId, Date loginDate)
			throws NTBException {
		boolean reFlag = true;
		String validityStr = Utils.null2EmptyWithTrim(
				Config.getProperty("pinMailer.active.period")).toUpperCase();
		
		//Update by heyj 20190524, add column corpid
		//Date pinMailerDate = pinMailerDao.getPinMailerDate(userId);
		Date pinMailerDate = null;
		String sql = "select to_char(p.confirmed,'yyyymmdd hh24:mi:ss') as CONFIRMED_C from pin_mailer p where p.user_id=? and p.corp_id=? order by p.created desc";
		try {
			List list = genericJdbcDao.query(sql, new Object[]{userId, corpId});
			if(list!=null && list.size()>0){
				Map map = (Map)list.get(0);
				String pinMailerDateStr = Utils.null2EmptyWithTrim(map.get("CONFIRMED_C"));
				if(!"".equals(pinMailerDateStr)){
					java.text.SimpleDateFormat f = new java.text.SimpleDateFormat();
					f.applyPattern("yyyyMMdd HH:mm:ss");
					pinMailerDate = f.parse(pinMailerDateStr);
				}
			}
		} catch (Exception e) {
			Log.error("Error query pin_mailer", e);
			throw new NTBException("err.sys.GeneralError");
		}
		
		
		if (pinMailerDate == null) {
			reFlag = false;
			return reFlag;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(pinMailerDate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		if (validityStr.indexOf("M") != -1) {
			validityStr = (validityStr.substring(0, validityStr.indexOf("M")))
					.trim();
			cal.add(Calendar.MONTH, Integer.parseInt(validityStr));
		} else if (validityStr.indexOf("D") != -1) {
			validityStr = (validityStr.substring(0, validityStr.indexOf("D")))
					.trim();
			cal.add(Calendar.DAY_OF_YEAR, Integer.parseInt(validityStr));
		} else if (validityStr.indexOf("H") != -1) {
			validityStr = (validityStr.substring(0, validityStr.indexOf("H")))
					.trim();
			cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(validityStr));
		} else {
			validityStr = "0";
			reFlag = false;
		}

		if (reFlag) {
			Date tempDate = cal.getTime();
			Log.info("check period for PIN mailer time,  tempDate="
					+ cal.toString());
			cal.clear();
			cal.setTime(loginDate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			loginDate = cal.getTime();
			Log.info("check period for PIN mailer time, loginDate="
					+ cal.toString());

			if (loginDate.after(tempDate))
				reFlag = false;
			else
				reFlag = true;
		}

		return reFlag;
	}

	public PinMailerDao getPinMailerDao() {
		return pinMailerDao;
	}

	public void setPinMailerDao(PinMailerDao pinMailerDao) {
		this.pinMailerDao = pinMailerDao;
	}

	public GenericJdbcDao getGenericJdbcDao() {
		return genericJdbcDao;
	}

	public void setGenericJdbcDao(GenericJdbcDao genericJdbcDao) {
		this.genericJdbcDao = genericJdbcDao;
	}

	/**
	 * by wen 20110803 get security alert message
	 * 
	 * @param userId
	 * @return
	 * @throws NTBException
	 */
	public String getSecurityAlertMsg(String userId ,String corpType) throws NTBException {

		corpType = getCorpType(corpType);
		String alertMessage = "";

		Date today = new Date();
		String todayStr = DateTime.formatDate(today, "yyyyMMdd");

		StringBuffer sql = new StringBuffer();
		sql.append("select POPUP_TIMES ");
		sql.append("from SECURITY_ALERT_CONTROL ");
		sql.append("where POPUP_FLAG='Y' ");
		sql.append("and END_DATE>=? ");
		sql.append("and START_DATE<=? ");

		StringBuffer sql2 = new StringBuffer();
		sql2.append("select ALERT_COUNT ");
		sql2.append("from SECURITY_ALERT_LOG ");
		sql2.append("where USER_ID=? ");
		sql2.append("and LOGIN_DATE=? ");

		StringBuffer sql3 = new StringBuffer();
		sql3.append("insert into SECURITY_ALERT_LOG(USER_ID,LOGIN_DATE,ALERT_COUNT) ");
		sql3.append("values(?,?,1)");

		StringBuffer sql4 = new StringBuffer();
		sql4.append("update SECURITY_ALERT_LOG ");
		sql4.append("set ALERT_COUNT=ALERT_COUNT+1 ");
		sql4.append("where USER_ID=? ");
		sql4.append("and LOGIN_DATE=? ");

		StringBuffer sql5 = new StringBuffer();
		sql5.append("select MESSAGE ");
		sql5.append("from SECURITY_ALERT_MESSAGE ");
		sql5.append("where MODE_FLAG=? ");
		
		
		String alertFlag = "N";

		try {
			int popupTimes = 0;

			/* query popup times */
			Object popupTimesObj = (Object) genericJdbcDao.querySingleValue(sql
					.toString(), new Object[] { todayStr, todayStr });
			popupTimes = Utils.nullEmpty2Zero(popupTimesObj);

			Log.info("getSecurityAlertMsg.popupTimes=" + popupTimes);

			if (popupTimes > 0) {
				int alertCount = 0;
				/* query alert count */
				Object alertCountObj = (Object) genericJdbcDao
						.querySingleValue(sql2.toString(), new Object[] {
								userId, new java.sql.Date(today.getTime()) });

				Log.info("getSecurityAlertMsg.alertCount(" + userId + ")="
						+ alertCountObj);

				if (alertCountObj != null) {
					alertCount = Utils.nullEmpty2Zero(alertCountObj);
					/* update alert count */
					if (alertCount < popupTimes) {
						alertFlag = "Y";
						genericJdbcDao.update(sql4.toString(), new Object[] {
								userId, new java.sql.Date(today.getTime()) });
					} else {
						alertFlag = "N";
					}
				} else {
					/* insert first alert log */
					alertFlag = "Y";
					genericJdbcDao.update(sql3.toString(), new Object[] {
							userId, new java.sql.Date(today.getTime()) });
				}
			}

			Log.info("getSecurityAlertMsg.alertFlag=" + alertFlag);

			if ("Y".equals(alertFlag)) {
				Object[] valueObjArray = { corpType };
				Object messageObj = (Object) genericJdbcDao.querySingleValue(sql5.toString(), valueObjArray);
				alertMessage = Utils.null2Empty(messageObj);
				if("".equals(alertMessage)){
					valueObjArray = new Object[]{ "d1" };
					messageObj = (Object) genericJdbcDao.querySingleValue(sql5.toString(), valueObjArray);
					alertMessage = Utils.null2Empty(messageObj);
				}
			} else {
				alertMessage = "";
			}

			Log.info("getSecurityAlertMsg.alertMessage=" + alertMessage + " with mode=" + corpType);

		} catch (Exception e) {
			Log.error("getSecurityAlertMsg() error", e);
		}

		return alertMessage;
	}
	
	// get mode name by corpType, add by li_zd at 20170810
	private String getCorpType(String corpType){
		if("3".equals(corpType)){
			corpType = "m1";
		} else if("2".equals(corpType)){
			corpType = "m2";
		} else if("4".equals(corpType)){
			corpType = "m3";
		} else if("1".equals(corpType)){
			corpType = "m4";
		}
		return corpType;
	}
	
	public void updateAddPrintInfo(CorpUser userObj, String operatorId) throws NTBException {
		String cifNo = userObj.getCorpId();
		String cifName = "";
		Corporation corp = (Corporation) corporationDao.load(
				Corporation.class, cifNo);
		if(corp != null){
		    cifName = corp.getCorpName();
		}
		String userId = userObj.getUserId();
		String loginId = userObj.getUserId().toUpperCase();
		String printId = UUID.randomUUID().toString();
        printId = printId.replace("-", "");
		String sql = "insert into corp_print_info(print_id, cif_no, cif_name, login_id, status, create_time, remark, operator_id, user_id)" 
                 + "values(?, ?, ?, ?, '0', sysdate, 'New Account', ?, ?)";
		try {
			genericJdbcDao.update(sql, new Object[]{printId, cifNo, cifName, loginId, operatorId, userId});
		} catch (Exception e) {
			Log.error("Add password print info error", e);
			throw new NTBException("err.sys.GeneralError");
		}
	}
	
	public void addPrintInfo(CorpUser userObj, String operatorId) throws NTBException {
		String cifNo = userObj.getCorpId();
		String cifName = "";
		Corporation corp = (Corporation) corporationDao.load(
				Corporation.class, cifNo);
		if(corp != null){
		    cifName = corp.getCorpName();
		}
		String loginId = userObj.getUserId().toUpperCase();
		String userId = userObj.getUserId();
		String printId = UUID.randomUUID().toString();
        printId = printId.replace("-", "");
		String sql = "insert into corp_print_info(print_id, cif_no, cif_name, login_id, status, create_time, remark, operator_id, user_id)" 
                 + "values(?, ?, ?, ?, '0', sysdate, 'Reset Password', ?, ?)";
		try {
			genericJdbcDao.update(sql, new Object[]{printId, cifNo, cifName, loginId, operatorId, userId});
		} catch (Exception e) {
			Log.error("Add password print info error", e);
			throw new NTBException("err.sys.GeneralError");
		}
	}
	//add by linrui 20190815 for CR0001-598
	public boolean checkIdDuplicate(CorpUserHis userObj)throws NTBException{
//		return true;
		String corpId = userObj.getCorpId();
		String idType = userObj.getIdType();
		String idNumber = userObj.getIdNo();
		boolean flag = true;
		String sql = "select us.ID_NO from CORP_USER us where " +
				"us.corp_id = ? and  " +
				"us.ID_TYPE = ? and " +
				"us.ID_NO = ? and us.STATUS != '" + Constants.STATUS_REMOVED + "' ";
		try {
			List list = genericJdbcDao.query(sql, new Object[]{corpId, idType, idNumber});
			if(list.size()>0)
				flag =  false;
		} catch (Exception e) {
			Log.error("Add password print info error", e);
			throw new NTBException("err.sys.GeneralError");
		}
		return flag;
	}
}
