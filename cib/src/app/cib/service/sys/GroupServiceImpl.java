package app.cib.service.sys;

import java.util.*;


import org.apache.commons.beanutils.BeanUtils;

import app.cib.bo.sys.CorpAccount;
import app.cib.bo.sys.CorpPermission;
import app.cib.bo.sys.CorpPreference;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.sys.CorpUserHis;
import app.cib.bo.sys.UserGroup;
import app.cib.bo.sys.UserGroupHis;
import app.cib.core.CibIdGenerator;
import app.cib.dao.sys.CorpAccountDao;
import app.cib.dao.sys.CorpPermissionDao;
import app.cib.dao.sys.CorpUserDao;
import app.cib.dao.sys.UserGroupDao;
import app.cib.dao.sys.UserGroupHisDao;
import app.cib.util.Constants;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;

/**
 * @author not attributable
 * @version 1.0
 */
public class GroupServiceImpl implements GroupService {
    private UserGroupDao userGroupDao;

    private UserGroupHisDao userGroupHisDao;

    private CorpAccountDao corpAccountDao;

    private CorpPermissionDao corpPermissionDao;

    private CorpUserDao corpUserDao;

    public CorpPermissionDao getCorpPermissionDao() {
        return corpPermissionDao;
    }

    public void setCorpPermissionDao(CorpPermissionDao corpPermissionDao) {
        this.corpPermissionDao = corpPermissionDao;
    }

    public UserGroupDao getUserGroupDao() {
        return userGroupDao;
    }

    public void setUserGroupDao(UserGroupDao userGroupDao) {
        this.userGroupDao = userGroupDao;
    }

    public UserGroup loadByCorpAndName(String corpId, String groupName) throws
            NTBException {
        List groupList;
        groupList = userGroupDao.list(
                " from UserGroup as userGroup where userGroup.corpId=? and userGroup.groupName=? and userGroup.status !=?",
                new Object[] {corpId, groupName, Constants.STATUS_REMOVED});
        if (groupList != null && groupList.size() > 0) {
            return (UserGroup) groupList.get(0);
        }
        return null;
    }

    public UserGroup loadUserGroup(String groupId) throws NTBException {

        UserGroup userGroup = (UserGroup) userGroupDao.load(UserGroup.class,
                groupId);
        if (userGroup != null) {
            if (userGroup.getStatus().equals(Constants.STATUS_REMOVED)) {
                return null;
            }
        }
        return userGroup;
    }

    public void add(UserGroup userGroup) throws NTBException {

        userGroupDao.add(userGroup);
    }

    public void addGroupPermission(String groupId, Integer version,
                                   String[] accountList,
                                   String[] funtionList) throws NTBException {
        UserGroupHis userGroup = this.loadUserGroupHisById(groupId);
        if (accountList != null) {
            for (int i = 0; i < accountList.length; i++) {
                String permissionId = CibIdGenerator
                                      .getIdForOperation("CORP_PERMISSION");
                CorpPermission corpPermission = new CorpPermission();
                corpPermission.setCorpId(userGroup.getCorpId());
                corpPermission.setGroupId(userGroup.getGroupId());
                corpPermission.setPermissionId(permissionId);
                corpPermission.setPermissionResource(accountList[i]);
                corpPermission.setPermissionType(CorpPermission.
                                                 PERMISSION_TYPE_ACCOUNT);
                corpPermission.setRoleId(userGroup.getRoleId());
                corpPermission.setStatus(Constants.STATUS_PENDING_APPROVAL);
                corpPermission.setLastUpdateTime(new Date());
                corpPermission.setVersion(version);
                corpPermissionDao.add(corpPermission);
            }
        }
        if (funtionList != null) {
            for (int j = 0; j < funtionList.length; j++) {
                String permissionId = CibIdGenerator
                                      .getIdForOperation("CORP_PERMISSION");
                CorpPermission corpPermission = new CorpPermission();
                corpPermission.setCorpId(userGroup.getCorpId());
                corpPermission.setGroupId(userGroup.getGroupId());
                corpPermission.setPermissionId(permissionId);
                corpPermission.setPermissionResource(funtionList[j]);
                corpPermission.setPermissionType(CorpPermission.
                                                 PERMISSION_TYPE_FUNCTION);
                corpPermission.setRoleId(userGroup.getRoleId());
                corpPermission.setStatus(Constants.STATUS_PENDING_APPROVAL);
                corpPermission.setVersion(userGroup.getVersion());
                corpPermissionDao.add(corpPermission);
            }
        }
    }

    public void update(UserGroup userGroup) throws NTBException {
        userGroupDao.update(userGroup);

    }

    public void delete(UserGroup userGroup) throws NTBException {
        userGroupDao.delete(userGroup);

    }

    public List listUserGroupByCorp(String corpId) throws NTBException {
        List groupList;
        groupList = userGroupDao.list(
                " from UserGroup as userGroup where userGroup.corpId=? and userGroup.status !=? order by userGroup.roleId",
                new Object[] {corpId, Constants.STATUS_REMOVED});
        return groupList;
    }

    public List listCorpAccount(String corpID) throws NTBException {
        List accountList;
        List returnList = new ArrayList();
        List acctList = new ArrayList();
        Map conditionMap = new HashMap();
        conditionMap.put("corpId", corpID);
        conditionMap.put("status", Constants.STATUS_NORMAL);
        accountList = corpAccountDao.list(CorpAccount.class, conditionMap);
        for (int i = 0; i < accountList.size(); i++) {
        	CorpAccount ca = (CorpAccount) accountList.get(i);
        	String accountNo = ca.getAccountNo();
        	if (!acctList.contains(accountNo)) {
        		returnList.add(ca);
        		acctList.add(accountNo);
        	}
        }
        return returnList;
    }

    public CorpAccountDao getCorpAccountDao() {
        return corpAccountDao;
    }

    public void setCorpAccountDao(CorpAccountDao corpAccountDao) {
        this.corpAccountDao = corpAccountDao;
    }

    public String[] getFunctionPermissionList(String groupId, Integer ver) throws
            NTBException {
        Map conditionMap = new HashMap();
        conditionMap.put("groupId", groupId);
        conditionMap.put("version", ver);
        conditionMap.put("permissionType",
                         CorpPermission.PERMISSION_TYPE_FUNCTION);
        List corpPermissionList = corpPermissionDao.list(CorpPermission.class,
                conditionMap);
        String[] permissionList = new String[corpPermissionList.size()];
        for (int i = 0; i < corpPermissionList.size(); i++) {
            CorpPermission item = (CorpPermission) corpPermissionList.get(i);
            permissionList[i] = item.getPermissionResource();
        }
        return permissionList;
    }

    public String[] getAccountPermissionList(String groupId, Integer ver) throws
            NTBException {
        Map conditionMap = new HashMap();
        conditionMap.put("groupId", groupId);
        conditionMap.put("version", ver);
        conditionMap.put("permissionType",
                         CorpPermission.PERMISSION_TYPE_ACCOUNT);
        List corpPermissionList = corpPermissionDao.list(CorpPermission.class,
                conditionMap);
        String[] permissionList = new String[corpPermissionList.size()];
        for (int i = 0; i < corpPermissionList.size(); i++) {
            CorpPermission item = (CorpPermission) corpPermissionList.get(i);
            permissionList[i] = item.getPermissionResource();
        }
        return permissionList;
    }

    public CorpUserDao getCorpUserDao() {
        return corpUserDao;
    }

    public void setCorpUserDao(CorpUserDao corpUserDao) {
        this.corpUserDao = corpUserDao;
    }

    public List listCorpUser(String groupId) throws NTBException {
        Map conditionMap = new HashMap();
        conditionMap.put("groupId", groupId);
        // Jet modified, only show the normal user
        conditionMap.put("status", Constants.STATUS_NORMAL);
                
        List corpUserList = corpUserDao.list(CorpUser.class, conditionMap);
		if(null!=corpUserList && corpUserList.size()>0){
			for(int i=0;i<corpUserList.size();i++){
				CorpUser corpUser = (CorpUser)corpUserList.get(i) ;
			}
		}
        return corpUserList;
    }

    public List getAccountPermission(String groupId, Integer ver) throws
            NTBException {
        Map conditionMap = new HashMap();
        Map paramMap = new HashMap();
        conditionMap.put("groupId", groupId);
        conditionMap.put("version", ver);
        conditionMap.put("permissionType",
                         CorpPermission.PERMISSION_TYPE_ACCOUNT);
        List corpPermissionList = corpPermissionDao.list(CorpPermission.class, conditionMap);
        List resultData = new ArrayList();
        for (int i = 0; i < corpPermissionList.size(); i++) {
            String accountNo = ((CorpPermission) corpPermissionList.get(i)).getPermissionResource();
            paramMap.put("accountNo", accountNo);
            List caList = corpAccountDao.list(CorpAccount.class, paramMap);
            if(caList.size() == 0)
            	continue;
            CorpAccount ca = (CorpAccount) caList.get(0);
//            resultData.add((corpAccountDao.load(CorpAccount.class, accountNo)));
            resultData.add(ca);
        }
        return resultData;
    }

    public List getFunctionPermission(String groupId, Integer ver) throws
            NTBException {
        Map conditionMap = new HashMap();
        conditionMap.put("groupId", groupId);
        conditionMap.put("version", ver);
        conditionMap.put("permissionType",
                         CorpPermission.PERMISSION_TYPE_FUNCTION);
        List corpPermissionList = corpPermissionDao.list(CorpPermission.class,
                conditionMap);
        return corpPermissionList;
    }

    public void addHis(UserGroupHis userGroupHis) {
        userGroupHisDao.add(userGroupHis);
    }

    public UserGroupHisDao getUserGroupHisDao() {
        return userGroupHisDao;
    }

    public void setUserGroupHisDao(UserGroupHisDao userGroupHisDao) {
        this.userGroupHisDao = userGroupHisDao;
    }

    public UserGroupHis loadUserGroupHisById(String groupId) throws NTBException {
        Map conditionMap = new HashMap();
        conditionMap.put("groupId", groupId);
        conditionMap.put("status", Constants.STATUS_PENDING_APPROVAL);
        List userGroupHisList = userGroupHisDao.list(UserGroupHis.class,
                conditionMap);
        if (userGroupHisList == null || userGroupHisList.size() == 0) {
            return null;
        }
        UserGroupHis userGroupHis = (UserGroupHis) userGroupHisList.get(0);
        return userGroupHis;
    }

    public UserGroupHis loadUserGroupHisBySeqNo(String seqNo) throws NTBException {

        UserGroupHis userGroupHis = (UserGroupHis) userGroupDao.load(UserGroupHis.class,
                seqNo);
//        if (userGroupHis != null) {
//            if (userGroupHis.getStatus().equals(Constants.STATUS_REMOVED)) {
//                return null;
//            }
//        }
        return userGroupHis;
    }
    /*
    public void updateUserGroup(String groupId) throws NTBException {
        UserGroup userGroup = (UserGroup) userGroupDao.load(UserGroup.class,
                groupId);

        ApplicationContext appContext = Config.getAppContext();
        GenericJdbcDao dao = (GenericJdbcDao) appContext
                             .getBean("genericJdbcDao");
        Map columnMap = new HashMap();
        columnMap.put("STATUS", Constants.STATUS_REMOVED);
        Map conditionMap = new HashMap();
        conditionMap.put("GROUP_ID", userGroup.getGroupId());
        conditionMap.put("VERSION", userGroup.getVersion());

        try {
            dao.update("CORP_PERMISSION", columnMap, conditionMap);
        } catch (Exception e) {
            Log.error("Error updating group's permission", e);
            throw new NTBException("err.sys.DBError");
        }

        UserGroupHis userGroupHis = loadUserGroupHisById(groupId);

        // Update UserGroupHis
        userGroupHis.setStatus(Constants.STATUS_NORMAL);
        userGroupHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        userGroupHisDao.update(userGroupHis);

        userGroup.setGroupId(userGroupHis.getGroupId());
        userGroup.setRoleId(userGroupHis.getRoleId());
        userGroup.setGroupName(userGroupHis.getGroupName());
        userGroup.setGroupDesc(userGroupHis.getGroupDesc());
        userGroup.setOperation(userGroupHis.getOperation());
        userGroup.setStatus(userGroupHis.getStatus());
        userGroup.setAuthStatus(userGroupHis.getAuthStatus());
        userGroup.setVersion(userGroupHis.getVersion());
        userGroupDao.update(userGroup);
    }
*/

    public void deleteGroupPermission(String groupId, Integer version) throws
            NTBException {
        corpPermissionDao.deleteByGroupVersion(groupId, version);
    }

    public void deleteGroupPermission(String groupId, String status) throws
            NTBException {
        corpPermissionDao.deleteByGroupStatus(groupId, status);
    }

    public void updateGroupPermission(String groupId, Integer status) throws
            NTBException {
        corpPermissionDao.updateByGroupVersion(groupId, status);
    }


    public void updateHis(UserGroupHis userGroupHis) throws NTBException {
        userGroupHisDao.update(userGroupHis);
    }

    public void add4AssignGroup(CorpPreference corpPref, List newUserList) throws NTBException {
        CorpPreferenceService corpPrefService = (CorpPreferenceService) Config.getAppContext().getBean("CorpPreferenceService");
        CorpUserService corpUserService = (CorpUserService) Config.getAppContext().getBean("corpUserService");

        corpPref.setStatus(Constants.STATUS_PENDING_APPROVAL);
        corpPref.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
        corpPref.setPrefType(CorpPreference.PREF_TYPE_GROUP_ASSIGN);
        corpPref.setLastUpdateTime(new Date());
        corpPrefService.newPref(corpPref);

        for (int i = 0; i < newUserList.size(); i++) {
            CorpUser corpUser = (CorpUser) newUserList.get(i);
            String seqNo[] = CibIdGenerator.getIdsForOperation("CORP_USER_HIS", 2);
            try {
            	//hjs 20081111 before
            	//modified by lzg for GAPMC-EB-001-0040
            	//CorpUser corpUserBefore = corpUserService.load(corpUser.getUserId());
            	CorpUser corpUserBefore = corpUserService.loadWithCorpId(corpUser.getUserId(),corpUser.getCorpId());
            	//modified by lzg end
                CorpUserHis corpUserHisBefore = new CorpUserHis();
                BeanUtils.copyProperties(corpUserHisBefore, corpUserBefore);
                corpUserHisBefore.setSeqNo(seqNo[0]);
                corpUserHisBefore.setOperation(Constants.OPERATION_UPDATE_BEFORE);
                corpUserHisBefore.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
                corpUserHisBefore.setAfterModifyId(seqNo[1]);
                corpUserService.addCorpUserHis(corpUserHisBefore);
            	
            	//after
                CorpUserHis corpUserHis = new CorpUserHis();
                BeanUtils.copyProperties(corpUserHis, corpUser);
                corpUserHis.setSeqNo(seqNo[1]);
                corpUserHis.setPrefId(corpPref.getPrefId());
                corpUserHis.setOperation(Constants.OPERATION_ASSIGN_GROUP);
                corpUserHis.setStatus(Constants.STATUS_PENDING_APPROVAL);
                corpUserHis.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
                corpUserHis.setLastUpdateTime(new Date());
                corpUserService.addCorpUserHis(corpUserHis);
            } catch (Exception e) {
                e.printStackTrace();
                throw new NTBException("err.txn.copyPropertiesFault");
            }

        }
    }

    public void approveAssignGroup(String prefId) throws NTBException {
        CorpUserService corpUserService = (CorpUserService) Config.getAppContext().getBean("corpUserService");
        CorpPreferenceService corpPrefService = (CorpPreferenceService) Config.getAppContext().getBean("CorpPreferenceService");
        
        // 锟斤拷锟斤拷 Corp_Preference锟斤拷
        CorpPreference corpPref = corpPrefService.findCorpPrefByID(prefId);
        corpPref.setStatus(Constants.STATUS_NORMAL);
        corpPref.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        corpPref.setLastUpdateTime(new Date());
        corpPrefService.updatePref(corpPref);

        List corpUserHisList = corpUserService.listUserHisByPerfId(prefId);
        CorpUserHis corpUserHis = null;
        CorpUser corpUser = null;
        for (int i = 0; i < corpUserHisList.size(); i++) {
            corpUserHis = (CorpUserHis) corpUserHisList.get(i);
            corpUserHis.setStatus(Constants.STATUS_NORMAL);
            corpUserHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
            corpUserHis.setLastUpdateTime(new Date());
            corpUserService.updateCorpUserHis(corpUserHis);
            //modified by lzg for GAPMC-EB-001-0040
            //corpUser = corpUserService.load(corpUserHis.getUserId());
            corpUser = corpUserService.loadWithCorpId(corpUserHis.getUserId(),corpUserHis.getCorpId());
            //modified by lzg end
        	String orgPassword = corpUser.getUserPassword();
        	String orgSecurityCode = corpUser.getSecurityCode();
        	Integer orgLoginTimes = corpUser.getLoginTimes();
        	String orgLoginStatus = corpUser.getLoginStatus();
        	String orgCurrLoginIp = corpUser.getCurrLoginIp();
        	Date orgCurrLoginTime = corpUser.getCurrLoginTime();
        	String orgPervLoginStatus = corpUser.getPervLoginStatus();
        	String orgPrevLoginIp = corpUser.getPrevLoginIp();
        	Date orgPrevLoginTime = corpUser.getPrevLoginTime();
        	String orgStatus = corpUser.getStatus();
        	String orgBlockReason = corpUser.getBlockReason();
            try {
                BeanUtils.copyProperties(corpUser, corpUserHis);
            } catch (Exception e) {
                e.printStackTrace();
                throw new NTBException("err.txn.copyPropertiesFault");
            }
            // keeping original properties after assignment approval
    		corpUser.setUserPassword(orgPassword);
    		corpUser.setLoginTimes(orgLoginTimes);
    		corpUser.setSecurityCode(orgSecurityCode);
        	corpUser.setLoginStatus(orgLoginStatus);
        	corpUser.setCurrLoginIp(orgCurrLoginIp);
        	corpUser.setCurrLoginTime(orgCurrLoginTime);
        	corpUser.setPervLoginStatus(orgPervLoginStatus);
        	corpUser.setPrevLoginIp(orgPrevLoginIp);
        	corpUser.setPrevLoginTime(orgPrevLoginTime);
        	
        	// 锟斤拷锟斤拷锟叫讹拷锟角凤拷锟斤拷锟皆讹拷block锟斤拷锟�锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟诫超锟斤拷5锟斤拷)
        	if (orgStatus.equals(Constants.STATUS_BLOCKED) && orgBlockReason.equals(Constants.BLOCK_REASON_BY_RETRY)){
           		corpUser.setStatus(orgStatus);
        	}
        	
        	//update CorpUser
            corpUserService.update(corpUser);
        }
    }

    public void rejectAssignGroup(String prefId) throws NTBException {
        CorpUserService corpUserService = (CorpUserService) Config.getAppContext().getBean("corpUserService");
        CorpPreferenceService corpPrefService = (CorpPreferenceService) Config.getAppContext().getBean("CorpPreferenceService");
        // 锟斤拷锟斤拷 Corp_Preference锟斤拷
        CorpPreference corpPref = corpPrefService.findCorpPrefByID(prefId);
        corpPref.setStatus(Constants.STATUS_REMOVED);
        corpPref.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
        corpPref.setLastUpdateTime(new Date());
        corpPrefService.updatePref(corpPref);

        List corpUserHisList = corpUserService.listUserHisByPerfId(prefId);
        for (int i = 0; i < corpUserHisList.size(); i++) {
            CorpUserHis userHis = (CorpUserHis) corpUserHisList.get(i);
            userHis.setStatus(Constants.STATUS_REMOVED);
            userHis.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
            userHis.setLastUpdateTime(new Date());
            corpUserService.updateCorpUserHis(userHis);
        }
    }
}
