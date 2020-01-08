package app.cib.service.sys;

import java.util.*;

import app.cib.bo.sys.*;

import com.neturbo.set.exception.*;

public interface GroupService {
    public List listUserGroupByCorp(String CorpId) throws NTBException;
    public List listCorpAccount(String CorpId) throws NTBException;
    public List listCorpUser(String groupId) throws NTBException;

    public UserGroup loadByCorpAndName(String corpId, String groupName) throws NTBException;
    public UserGroup loadUserGroup(String groupId) throws NTBException;
    public UserGroupHis loadUserGroupHisById(String groupId) throws NTBException;
    public UserGroupHis loadUserGroupHisBySeqNo(String groupId) throws NTBException;

    //public void updateUserGroup(String groupId) throws NTBException;
    public void deleteGroupPermission(String groupId, String status) throws NTBException;
    public void deleteGroupPermission(String groupId, Integer version) throws NTBException;
    public void addGroupPermission(String groupId, Integer version, String[] accountList, String[] funtionList) throws NTBException;
    public void updateGroupPermission(String groupId, Integer version) throws NTBException;

    public List getFunctionPermission(String groupId, Integer ver) throws
            NTBException;
    public List getAccountPermission(String groupId, Integer ver) throws
            NTBException;
    public String[] getFunctionPermissionList(String groupId, Integer ver) throws
            NTBException;
    public String[] getAccountPermissionList(String groupId, Integer ver) throws
            NTBException;

    public void addHis(UserGroupHis userGroup) throws NTBException;
    public void updateHis(UserGroupHis userGroup) throws NTBException;

    public void add(UserGroup userGroup) throws NTBException;
    public void update(UserGroup userGroup) throws NTBException;
    public void delete(UserGroup userGroup) throws NTBException;


    public void add4AssignGroup(CorpPreference corpPref, List newUserList) throws
            NTBException;
    public void rejectAssignGroup(String prefId) throws NTBException;
    public void approveAssignGroup(String prefId) throws NTBException;

}
