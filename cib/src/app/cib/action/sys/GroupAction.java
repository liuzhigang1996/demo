package app.cib.action.sys;

/**
 * @author nabai
 *
 * 閿熺煫鎴嫹閿熸枻鎷烽敓锟介敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熺潾鏀光槄鎷峰垹閿熸枻鎷烽敓绐栴垽鎷烽敓鏂ゆ嫹i閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓绲犻敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸澃纰�
 */
import java.util.*;

import org.apache.commons.beanutils.BeanUtils;

import app.cib.bo.bnk.Corporation;
import app.cib.bo.sys.CorpPreference;
import app.cib.bo.sys.CorpUser;
import app.cib.bo.sys.CorpUserHis;
import app.cib.bo.sys.UserGroup;
import app.cib.bo.sys.UserGroupHis;
import app.cib.core.Approvable;
import app.cib.core.CibAction;
import app.cib.core.CibIdGenerator;
import app.cib.service.bnk.CorporationService;
import app.cib.service.flow.FlowEngineService;
import app.cib.service.sys.CorpUserService;
import app.cib.service.sys.GroupService;
import app.cib.util.CachedDBRCFactory;
import app.cib.util.Constants;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.core.Log;
import org.springframework.context.ApplicationContext;
import app.cib.service.sys.MailService;

public class GroupAction extends CibAction implements Approvable {

	// 鍙朑roup_User閿熸枻鎷烽敓鏂ゆ嫹Y閿熸枻鎷�, 閿熺捶绀洪敓鏂ゆ嫹閿熸枻鎷� 閿熺煫鎴嫹閿熺祫閿熺禍
	public void groupList() throws NTBException {
		CorpUser user = (CorpUser) this.getUser();
		GroupService groupService = (GroupService) Config.getAppContext()
				.getBean("GroupService");
		List userGroupList = groupService.listUserGroupByCorp(user.getCorpId());
		userGroupList = this.convertPojoList2MapList(userGroupList);
		Map resultData = new HashMap();
		
		for (int i = 0; i < userGroupList.size(); i++) {
			HashMap retMap = (HashMap) userGroupList.get(i);
			String groupId = retMap.get("groupId").toString();
			if (groupService.loadUserGroupHisById(groupId) != null) {
				retMap.put("status", "1");
			}
		}
		
		resultData.put("userGroupList", userGroupList);
		
		
		setResultData(resultData);

	}

	// 閿熸枻鎷疯 閿熺煫鎴嫹閿熺祫閿熺禍 閿熸枻鎷疯┏閿熸枻鎷烽敓绲愰敓鏂ゆ嫹
	public void view() throws NTBException {
		String groupId = this.getParameter("groupId");
		Map resultData = new HashMap();
		GroupService groupService = (GroupService) Config.getAppContext()
				.getBean("GroupService");
		resultData = viewByGroupId(groupId);
		List corpUserList = groupService.listCorpUser(groupId);
		resultData.put("corpUserList", this
				.convertPojoList2MapList(corpUserList));
		setResultData(resultData);
	}

	public Map viewByGroupId(String groupId) throws NTBException {
		GroupService groupService = (GroupService) Config.getAppContext()
				.getBean("GroupService");

		UserGroup userGroup = groupService.loadUserGroup(groupId);
		Map resultData = new HashMap();
		this.convertPojo2Map(userGroup, resultData);

		// add by HuangGengHan 2006-8-8
		List accountPermissionList = groupService.getAccountPermission(groupId,
				userGroup.getVersion());
		List functonPermissionList = groupService.getFunctionPermission(
				groupId, userGroup.getVersion());
		resultData.put("item_accountList", this
				.convertPojoList2MapList(accountPermissionList));
		resultData.put("item_functionList", this
				.convertPojoList2MapList(functonPermissionList));
		return resultData;
	}

	public Map viewHisBySeqNo(String seqNo) throws NTBException {
		GroupService groupService = (GroupService) Config.getAppContext()
				.getBean("GroupService");

		UserGroupHis userGroupHis = groupService.loadUserGroupHisBySeqNo(seqNo);
		Map resultData = new HashMap();
		this.convertPojo2Map(userGroupHis, resultData);

		// add by HuangGengHan 2006-8-8
		List accountPermissionList = groupService.getAccountPermission(
				userGroupHis.getGroupId(), userGroupHis.getVersion());
		List functonPermissionList = groupService.getFunctionPermission(
				userGroupHis.getGroupId(), userGroupHis.getVersion());
		resultData.put("item_accountList", this
				.convertPojoList2MapList(accountPermissionList));
		resultData.put("item_functionList", this
				.convertPojoList2MapList(functonPermissionList));
		return resultData;
	}

	public List viewAssignGroup(String prefId) throws NTBException {
		CorpUserService corpUserService = (CorpUserService) Config
				.getAppContext().getBean("corpUserService");
		List corpUserHisList = corpUserService.listUserHisByPerfId(prefId);
		
		List retList = new ArrayList();
		
		for (int i = 0; i < corpUserHisList.size(); i++) {
			CorpUserHis corpUserHis = (CorpUserHis) corpUserHisList.get(i);
			if(retList.size() == 0){
				retList.add(corpUserHis);
				continue;
			}
			String userId = corpUserHis.getUserId();
			boolean flag = false;
			for (int j = 0; j < retList.size(); j++) {
				CorpUserHis resultCorpUserHis = (CorpUserHis)  retList.get(j);
				if(userId.equals(resultCorpUserHis.getUserId())){
					flag = true;
				}
			}
			if(!flag){
				retList.add(corpUserHis);
			}
		}
		
		return convertPojoList2MapList(retList);
	}

	public void addGroupLoad() throws NTBException {
		// 閿熸枻鎷烽敓鐭┖纰夋嫹 ResultData 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸鎾呮嫹閿熸枻鎷�
		setResultData(new HashMap(1));
		HashMap resultData = new HashMap();
		GroupService groupService = (GroupService) Config.getAppContext()
				.getBean("GroupService");
		CorpUser user = (CorpUser) getUser();
		resultData.put("accountList", this.convertPojoList2MapList(groupService
				.listCorpAccount(user.getCorpId())));
		resultData.put("corpType", user.getCorporation().getCorpType());
		setResultData(resultData);
	}

	public void addGroup() throws NTBException {
		UserGroupHis userGroupHis = new UserGroupHis();
		this.convertMap2Pojo(this.getParameters(), userGroupHis);

		GroupService groupService = (GroupService) Config.getAppContext()
				.getBean("GroupService");
		CorpUser user = (CorpUser) getUser();
		UserGroup testGroup = groupService.loadByCorpAndName(user.getCorpId(),
				userGroupHis.getGroupName());
		if (testGroup != null) {
			throw new NTBException("err.sys.GroupExist");
		}

		// 閿熸枻鎷风ず閿熸枻鎷烽敓锟�
		Map resultData = new HashMap();
		this.setResultData(resultData);

		resultData.put("accountList", this.convertPojoList2MapList(groupService
				.listCorpAccount(user.getCorpId())));
		this.convertPojo2Map(userGroupHis, resultData);
		String[] funtionList = getParameterValues("item_functionList");
		resultData.put("item_functionList", funtionList);
		String[] accountList = getParameterValues("item_accountList");
		resultData.put("item_accountList", accountList);
		resultData.put("corpType", user.getCorporation().getCorpType());

		// 閿熸枻鎷烽敓鐭紮鎷烽敓鏂ゆ嫹閿熷彨杈炬嫹閿熺氮ession閿熸枻鎷烽敓鐨嗘唻鎷穋onfirm閿熸枻鎷峰啓閿熸枻鎷烽敓鏂ゆ嫹鑿橀敓锟�
		this.setUsrSessionDataValue("userGroup", userGroupHis);
		this.setUsrSessionDataValue("item_functionList", funtionList);
		this.setUsrSessionDataValue("item_accountList", accountList);
	}

	public void addGroupCancel() throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		CorporationService corpService = (CorporationService)appContext.getBean("CorporationService");
		Map resultData = this.getResultData();
		
		String corpId = ((CorpUser)getUser()).getCorpId();
		Corporation corp = corpService.view(corpId);
		resultData.put("corpType", corp.getCorpType());
	}

	public void addGroupConfirm() throws NTBException {
		// 閿熸枻鎷烽敓鎻紮鎷烽敓鏂ゆ嫹閿熷彨杈炬嫹閿熸枻鎷烽敓鎹峰尅鎷�
		UserGroupHis userGroupHis = (UserGroupHis) getUsrSessionDataValue("userGroup");
		String[] funtionList = (String[]) getUsrSessionDataValue("item_functionList");
		String[] accountList = (String[]) getUsrSessionDataValue("item_accountList");

		// 閿熸枻鎷烽敓鏂ゆ嫹娆犻敓鏂ゆ嫹姹涢敓鏂ゆ嫹閿燂拷
		GroupService groupService = (GroupService) Config.getAppContext().getBean("GroupService");
		CorpUser user = (CorpUser) this.getUser();
		UserGroup testGroup = groupService.loadByCorpAndName(user.getCorpId(), userGroupHis.getGroupName());
		if (testGroup != null) {
			throw new NTBException("err.sys.GroupExist");
		}

		// 閿熼摪鏂ゆ嫹 Group 閿熸枻鎷� GroupHis
		Map resultData = this.getResultData();
		String seqNo = CibIdGenerator.getIdForOperation("USER_GROUP_HIS");
		String groupId = CibIdGenerator.getIdForOperation("USER_GROUP");

		int adminCount = user.getCorpAdministatorCount();
		// 閿熷彨璁规嫹Administrator閿熶茎闈╂嫹閿熸枻鎷�
		if (adminCount > 1) {
			FlowEngineService flowEngineService = (FlowEngineService) Config.getAppContext().getBean("FlowEngineService");
			String processId = flowEngineService.startProcess(
					Constants.TXN_SUBTYPE_GROUP_ADD, "0", GroupAction.class,
					null, 0, null, 0, 0, seqNo, null, getUser(), null, null,
					null);

			try {
				// 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸鍑ゆ嫹閿燂拷
				userGroupHis.setSeqNo(seqNo);
				userGroupHis.setGroupId(groupId);
				userGroupHis.setCorpId(user.getCorpId());
				//add by linrui 20190314
				String corpType = user.getCorporation().getCorpType();
				if (Constants.ROLE_APPROVER.equals(user.getRoleId())) {
					if (Constants.CORP_TYPE_MIDDLE.equals(corpType)) {
						user.setRoleId(Constants.ROLE_OPERATOR_APPROVER);
					} else if (Constants.CORP_TYPE_SMALL.equals(corpType)) {
						user.setRoleId(Constants.ROLE_OPERATOR_APPROVER_ADMIN);
					}
				}
				//end
				userGroupHis.setOperation(Constants.OPERATION_NEW);
				userGroupHis.setStatus(Constants.STATUS_PENDING_APPROVAL);
				userGroupHis.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
				Integer version = new Integer(1);
				userGroupHis.setVersion(version);
				groupService.addHis(userGroupHis);
				// 閿熸枻鎷烽敓楗侯煉鎷疯枃閿燂拷
				groupService.addGroupPermission(groupId, version, accountList,
						funtionList);
				// 閿熸枻鎷烽敓鏂ゆ嫹娌￠敓鏂ゆ嫹閿熸枻鎷�
				UserGroup userGroup = new UserGroup();
				BeanUtils.copyProperties(userGroup, userGroupHis);
				groupService.add(userGroup);

			} catch (Exception e) {
				flowEngineService.cancelProcess(processId, getUser());
				Log.error("Error adding user group", e);
				throw new NTBException("err.bnk.AddGroupFailure");
			}
			resultData.put("ackStatus", Constants.ACK_STATUS_PENDING_APPROVAL);
		} else {
			// 閿熸枻鎷烽敓绱竏ministrator 閿熸枻鎷烽敓鏂ゆ嫹灏忛敓鏂ゆ嫹1閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷锋潈
			// 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸鍑ゆ嫹閿燂拷
			userGroupHis.setSeqNo(seqNo);
			userGroupHis.setGroupId(groupId);
			userGroupHis.setCorpId(user.getCorpId());
			userGroupHis.setOperation(Constants.OPERATION_NEW);
			userGroupHis.setStatus(Constants.STATUS_PENDING_APPROVAL);
			userGroupHis.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
			Integer version = new Integer(1);
			userGroupHis.setVersion(version);
			groupService.addHis(userGroupHis);
			// 閿熸枻鎷烽敓楗侯煉鎷疯枃閿燂拷
			groupService.addGroupPermission(groupId, version, accountList,
					funtionList);

			// 閿熺潾闈╂嫹閿熸枻鎷峰彶閿熸枻鎷�
			userGroupHis.setStatus(Constants.STATUS_NORMAL);
			userGroupHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			userGroupHis.setLastUpdateTime(new Date());
			groupService.updateHis(userGroupHis);
			// 閿熸枻鎷烽敓鏂ゆ嫹娌￠敓鏂ゆ嫹閿熸枻鎷�
			UserGroup userGroup = new UserGroup();
			try {
				BeanUtils.copyProperties(userGroup, userGroupHis);
			} catch (Exception e) {
				Log.error("Error copy properties", e);
				throw new NTBException("err.sys.GeneralError");
			}
			groupService.add(userGroup);
			// 閿熺潾闈╂嫹鏉冮敓鐫唻鎷�
			groupService.updateGroupPermission(userGroup.getGroupId(),
					userGroup.getVersion());
			resultData.put("ackStatus", Constants.ACK_STATUS_ACCOMPLISHED);
			CachedDBRCFactory.addPendingCache("groupByCorp");
		}

		// 閿熸枻鎷风ず閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�
		resultData.putAll(viewByGroupId(groupId));
		setResultData(resultData);

	}

	public void editGroupLoad() throws NTBException {
		String groupId = this.getParameter("groupId");

		GroupService groupService = (GroupService) Config.getAppContext()
				.getBean("GroupService");

		// 閿熸枻鎷烽敓鏂ゆ嫹娌￠敓鏂ゆ嫹閿熼樁鍒侯儵顏庢嫹閿熸枻鎷烽敓鏂ゆ嫹閿熼ズ锟�
		if (groupService.loadUserGroupHisById(groupId) != null) {
			throw new NTBException("err.sys.OperationPending");
		}

		UserGroup userGroup = groupService.loadUserGroup(groupId);
		if (!userGroup.getStatus().equals(Constants.STATUS_NORMAL)) {
			throw new NTBException("err.sys.OperationPending");
		}

		// 閿熸枻鎷风ず閿熸枻鎷烽敓锟�
		Map resultData = new HashMap();
		this.setResultData(resultData);
		this.convertPojo2Map(userGroup, resultData);

		CorpUser user = (CorpUser) this.getUser();
		resultData.put("accountList", this.convertPojoList2MapList(groupService
				.listCorpAccount(user.getCorpId())));
		String[] accountPermissionList = groupService.getAccountPermissionList(
				groupId, userGroup.getVersion());
		String[] functonPermissionList = groupService
				.getFunctionPermissionList(groupId, userGroup.getVersion());
		resultData.put("item_accountList", accountPermissionList);
		resultData.put("item_functionList", functonPermissionList);
		resultData.put("corpType", user.getCorporation().getCorpType());

		setResultData(resultData);

	}

	public void editGroup() throws NTBException {
		UserGroup userGroup = new UserGroup();
		this.convertMap2Pojo(this.getParameters(), userGroup);

		GroupService groupService = (GroupService) Config.getAppContext()
				.getBean("GroupService");

		// 閿熸枻鎷风ず閿熸枻鎷烽敓锟�
		Map resultData = new HashMap();
		this.setResultData(resultData);

		CorpUser user = (CorpUser) getUser();
		resultData.put("accountList", this.convertPojoList2MapList(groupService
				.listCorpAccount(user.getCorpId())));
		this.convertPojo2Map(userGroup, resultData);
		String[] funtionList = getParameterValues("item_functionList");
		resultData.put("item_functionList", funtionList);
		String[] accountList = getParameterValues("item_accountList");
		resultData.put("item_accountList", accountList);
		resultData.put("corpType", user.getCorporation().getCorpType());

		// 閿熸枻鎷烽敓鐭紮鎷烽敓鏂ゆ嫹閿熷彨杈炬嫹閿熺氮ession閿熸枻鎷烽敓鐨嗘唻鎷穋onfirm閿熸枻鎷峰啓閿熸枻鎷烽敓鏂ゆ嫹鑿橀敓锟�
		this.setUsrSessionDataValue("userGroup", userGroup);
		this.setUsrSessionDataValue("item_functionList", funtionList);
		this.setUsrSessionDataValue("item_accountList", accountList);
	}

	public void editGroupCancel() throws NTBException {
		// 閿熺Ц鐚存嫹閿熸枻鎷蜂箛鍡夘厠绛规嫹閿燂拷
	}

	public void editGroupConfirm() throws NTBException {
		// 閿熸枻鎷烽敓鎻紮鎷烽敓鏂ゆ嫹閿熷彨杈炬嫹閿熸枻鎷烽敓鎹峰尅鎷�
		UserGroup userGroup = (UserGroup) getUsrSessionDataValue("userGroup");
		String[] funtionList = (String[]) getUsrSessionDataValue("item_functionList");
		String[] accountList = (String[]) getUsrSessionDataValue("item_accountList");

		GroupService groupService = (GroupService) Config.getAppContext()
				.getBean("GroupService");
		CorpUser user = (CorpUser) this.getUser();
		// 閿熸枻鎷烽敓鏂ゆ嫹娌￠敓鏂ゆ嫹閿熼樁鍒侯儵顏庢嫹閿熸枻鎷烽敓鏂ゆ嫹閿熼ズ锟�
		String groupId = userGroup.getGroupId();
		if (groupService.loadUserGroupHisById(groupId) != null) {
			throw new NTBException("err.sys.OperationPending");
		}

		// 閿熼摪鏂ゆ嫹 GroupHis
		Map resultData = this.getResultData();
		String seqNo = CibIdGenerator.getIdForOperation("USER_GROUP_HIS");

		int adminCount = user.getCorpAdministatorCount();
		// 閿熷彨璁规嫹Administrator閿熶茎闈╂嫹閿熸枻鎷�
		if (adminCount > 1) {
			FlowEngineService flowEngineService = (FlowEngineService) Config
					.getAppContext().getBean("FlowEngineService");
			String processId = flowEngineService.startProcess(
					Constants.TXN_SUBTYPE_GROUP_EDIT, "0", GroupAction.class,
					null, 0, null, 0, 0, seqNo, null, getUser(), null, null,
					null);
			try {
				// 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸鍑ゆ嫹閿燂拷
				UserGroupHis userGroupHis = new UserGroupHis();
				BeanUtils.copyProperties(userGroupHis, userGroup);
				userGroupHis.setSeqNo(seqNo);
				userGroupHis.setCorpId(user.getCorpId());
				userGroupHis.setOperation(Constants.OPERATION_UPDATE);
				userGroupHis.setStatus(Constants.STATUS_PENDING_APPROVAL);
				userGroupHis.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);

				UserGroup userGroupNow = groupService.loadUserGroup(groupId);
				Integer version = new Integer(Utils.nullEmpty2Zero(userGroupNow
						.getVersion()) + 1);
				userGroupHis.setVersion(version);
				groupService.addHis(userGroupHis);
				// 閿熸枻鎷烽敓楗侯煉鎷疯枃閿燂拷
				groupService.addGroupPermission(userGroup.getGroupId(),
						version, accountList, funtionList);

			} catch (Exception e) {
				flowEngineService.cancelProcess(processId, getUser());
				Log.error("Error editing user group", e);
				throw new NTBException("err.bnk.AddGroupFailure");
			}
			resultData.put("ackStatus", Constants.ACK_STATUS_PENDING_APPROVAL);
		} else {
			try {
				// 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸鍑ゆ嫹閿燂拷
				UserGroupHis userGroupHis = new UserGroupHis();
				BeanUtils.copyProperties(userGroupHis, userGroup);
				userGroupHis.setSeqNo(seqNo);
				userGroupHis.setCorpId(user.getCorpId());
				userGroupHis.setOperation(Constants.OPERATION_UPDATE);
				userGroupHis.setStatus(Constants.STATUS_PENDING_APPROVAL);
				userGroupHis.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
				userGroup = groupService.loadUserGroup(groupId);
				Integer version = new Integer(Utils.nullEmpty2Zero(userGroup
						.getVersion()) + 1);
				userGroupHis.setVersion(version);
				groupService.addHis(userGroupHis);
				// 閿熸枻鎷烽敓楗侯煉鎷疯枃閿燂拷
				groupService.addGroupPermission(userGroup.getGroupId(),
						version, accountList, funtionList);

				
				// 閿熺潾闈╂嫹閿熸枻鎷峰彶閿熸枻鎷�
				userGroupHis.setStatus(Constants.STATUS_NORMAL);
				userGroupHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
				userGroupHis.setLastUpdateTime(new Date());
				groupService.updateHis(userGroupHis);
				groupService.deleteGroupPermission(userGroupHis.getGroupId(),
						Constants.STATUS_NORMAL);
				
				// 閿熸枻鎷烽敓鏂ゆ嫹閿熺煫浼欐嫹閿熸枻鎷烽敓锟�
				try {
					BeanUtils.copyProperties(userGroup, userGroupHis);
				} catch (Exception e) {
					Log.error("Error copy properties", e);
					throw new NTBException("err.sys.GeneralError");
				}
				groupService.update(userGroup);
				// 閿熸枻鎷烽敓鏂ゆ嫹鏉冮敓鐫唻鎷�
				groupService.updateGroupPermission(userGroup.getGroupId(),
						userGroup.getVersion());

				// 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸纭锋嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷锋病閿燂拷
				Map mailData = new HashMap();
				this.convertPojo2Map(userGroup, mailData);

				CachedDBRCFactory.addPendingCache("groupByCorp");
			} catch (Exception e) {
				Log.error("Error editing user group", e);
				throw new NTBException("err.bnk.AddGroupFailure");
			}
			resultData.put("ackStatus", Constants.ACK_STATUS_ACCOMPLISHED);
		}

		// 閿熸枻鎷风ず閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�
		resultData.putAll(viewHisBySeqNo(seqNo));
		setResultData(resultData);
	}

	public void delGroupLoad() throws NTBException {
		Map resultData = new HashMap();
		String groupId = this.getParameter("groupId");
		// 閿熸枻鎷烽敓鏂ゆ嫹娌￠敓鏂ゆ嫹閿熼樁鍒侯儵顏庢嫹閿熸枻鎷烽敓鏂ゆ嫹閿熼ズ锟�
		GroupService groupService = (GroupService) Config.getAppContext()
				.getBean("GroupService");

		if (groupService.loadUserGroupHisById(groupId) != null) {
			throw new NTBException("err.sys.OperationPending");
		}
		// 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熺煫浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鍒犻敓鏂ゆ嫹
		List corpUserList = groupService.listCorpUser(groupId);
		if (corpUserList.size() > 0) {
			throw new NTBException("err.sys.UserExsitInGroup");
		}

		resultData.putAll(viewByGroupId(groupId));
		setResultData(resultData);
	}

	public void delGroupCancel() throws NTBException {
		// 閿熺Ц鐚存嫹閿熸枻鎷蜂箛鍡夘厠绛规嫹閿燂拷
	}

	public void delGroupConfirm() throws NTBException {
		// 閿熸枻鎷烽敓鎻紮鎷烽敓鏂ゆ嫹閿熷彨杈炬嫹閿熸枻鎷烽敓鎹峰尅鎷�
		String groupId = this.getParameter("groupId");
		// 閿熸枻鎷烽敓鏂ゆ嫹娌￠敓鏂ゆ嫹閿熼樁鍒侯儵顏庢嫹閿熸枻鎷烽敓鏂ゆ嫹閿熼ズ锟�
		GroupService groupService = (GroupService) Config.getAppContext()
				.getBean("GroupService");
		if (groupService.loadUserGroupHisById(groupId) != null) {
			throw new NTBException("err.sys.OperationPending");
		}
		// 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熺煫浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鍒犻敓鏂ゆ嫹
		List corpUserList = groupService.listCorpUser(groupId);
		if (corpUserList.size() > 0) {
			throw new NTBException("err.sys.UserExsitInGroup");
		}

		// 閿熸枻鎷风ず閿熸枻鎷烽敓锟�
		Map resultData = this.getResultData();
		CorpUser user = (CorpUser) this.getUser();
		String seqNo = CibIdGenerator.getIdForOperation("USER_GROUP_HIS");

		int adminCount = user.getCorpAdministatorCount();
		// 閿熷彨璁规嫹Administrator閿熶茎闈╂嫹閿熸枻鎷�
		if (adminCount > 1) {

			FlowEngineService flowEngineService = (FlowEngineService) Config
					.getAppContext().getBean("FlowEngineService");
			String processId = flowEngineService.startProcess(
					Constants.TXN_SUBTYPE_GROUP_DELETE, "0", GroupAction.class,
					null, 0, null, 0, 0, seqNo, null, getUser(), null, null,
					null);
			try {
				// 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸鍑ゆ嫹閿燂拷
				UserGroupHis userGroupHis = new UserGroupHis();
				UserGroup userGroup = groupService.loadUserGroup(groupId);
				BeanUtils.copyProperties(userGroupHis, userGroup);
				userGroupHis.setSeqNo(seqNo);
				userGroupHis.setOperation(Constants.OPERATION_REMOVE);
				userGroupHis.setStatus(Constants.STATUS_PENDING_APPROVAL);
				userGroupHis.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
				groupService.addHis(userGroupHis);
			} catch (Exception e) {
				flowEngineService.cancelProcess(processId, getUser());
				Log.error("Error deleting user group", e);
				throw new NTBException(e.getMessage());
			}
			resultData.put("ackStatus", Constants.ACK_STATUS_PENDING_APPROVAL);
		} else {
			try {
				// 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸鍑ゆ嫹閿燂拷
				UserGroupHis userGroupHis = new UserGroupHis();
				UserGroup userGroup = groupService.loadUserGroup(groupId);
				BeanUtils.copyProperties(userGroupHis, userGroup);
				userGroupHis.setSeqNo(seqNo);
				userGroupHis.setOperation(Constants.OPERATION_REMOVE);
				userGroupHis.setStatus(Constants.STATUS_REMOVED);
				userGroupHis.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
				userGroupHis.setLastUpdateTime(new Date());
				groupService.addHis(userGroupHis);

				// 鍒犻敓鏂ゆ嫹鏉冮敓鐫唻鎷�
				groupService.deleteGroupPermission(userGroupHis.getGroupId(),
						Constants.STATUS_NORMAL);

				// 閿熸枻鎷烽敓鏂ゆ嫹閿熺煫浼欐嫹閿熸枻鎷烽敓锟�
				userGroup = groupService.loadUserGroup(groupId);
				try {
					BeanUtils.copyProperties(userGroup, userGroupHis);
				} catch (Exception e) {
					Log.error("Error copy properties", e);
					throw new NTBException("err.sys.GeneralError");
				}
				groupService.update(userGroup);

				CachedDBRCFactory.addPendingCache("groupByCorp");

			} catch (Exception e) {
				Log.error("Error deleting user group", e);
				throw new NTBException(e.getMessage());
			}
			resultData.put("ackStatus", Constants.ACK_STATUS_ACCOMPLISHED);
		}

		this.setResultData(resultData);
	}

	public void assignGroupLoad() throws NTBException {
		CorpUser user = (CorpUser) this.getUser();
		CorpUserService corpUserService = (CorpUserService) Config
				.getAppContext().getBean("corpUserService");
		String corpId = null;
		corpId = user.getCorpId();
		if ((corpId != null) && (!corpId.equals(""))) {
			List userList = corpUserService.listNormalByCorp(corpId);
			userList = this.convertPojoList2MapList(userList);
			Map resultData = new HashMap();
			resultData.put("userList", userList);

			setResultData(resultData);

		}
	}

	public void assignGroupAdd() throws NTBException {

		CorpUser user = (CorpUser) this.getUser();
		CorpUserService corpUserService = (CorpUserService) Config
				.getAppContext().getBean("corpUserService");
		String corpId = null;
		corpId = user.getCorpId();
		List userList = corpUserService.listNormalByCorp(corpId);
		List newUserList = new ArrayList();
		String newGroupId = null;

		for (int i = 0; i < userList.size(); i++) {
			CorpUser corpUser = (CorpUser) userList.get(i);
			newGroupId = Utils.null2EmptyWithTrim(this.getParameter("groupId_"
					+ corpUser.getUserId()));
			if (!newGroupId.equals(corpUser.getGroupId())) {
				//modified by lzg for GAPMC-EB-001-0040
				/*if (corpUserService.loadPendingHis(corpUser.getUserId()) != null) {
					throw new NTBException("err.sys.UserNotAvailableForPending");
				}*/
				if (corpUserService.loadPendingHisWithCorpId(corpUser.getUserId(),corpUser.getCorpId()) != null) {
					throw new NTBException("err.sys.UserNotAvailableForPending");
				}
				//modified by lzg end
				//add by linrui 
				if("0".equals(corpUser.getGroupId())){
					continue;
				}
				if("".equals(newGroupId)){
					newGroupId = "0";
				}
				corpUser.setGroupId(newGroupId);
				newUserList.add(corpUser);
			}

		}
		if (newUserList.size() == 0) {
			throw new NTBException("err.txn.NoUserAssigned");
		}
		Map usrData = new HashMap();
		usrData.put("corpUserList", newUserList);

		//Jet modified for assignGroupCancel
		userList = this.convertPojoList2MapList(userList);
		usrData.put("userList", userList);
		this.setUsrSessionData(usrData);

		newUserList = this.convertPojoList2MapList(newUserList);
		Map resultData = new HashMap();
		resultData.put("newUserList", newUserList);

		setResultData(resultData);

	}

	public void assignGroupCancel() throws NTBException {
		List userList = (List) this.getUsrSessionDataValue("userList");		
		Map resultData = this.getResultData();		
		resultData.put("userList", userList);
	}
		
	public void assignGroupConfirm() throws NTBException {
		CorpUserService corpUserService = (CorpUserService) Config
				.getAppContext().getBean("corpUserService");
		GroupService groupService = (GroupService) Config.getAppContext()
				.getBean("GroupService");

		CorporationService corporationService = (CorporationService) Config
				.getAppContext().getBean("CorporationService");

		MailService mailService = (MailService) Config.getAppContext().getBean(
				"MailService");
		List newUserList = (List) getUsrSessionDataValue("corpUserList");
		for (int i = 0; i < newUserList.size() - 1; i++) {
			CorpUser corpUser = (CorpUser) newUserList.get(i);
			//modified by lzg for GAPMC-EB-001-0040
			/*if (corpUserService.loadPendingHis(corpUser.getUserId()) != null) {
				throw new NTBException("err.sys.UserNotAvailableForPending");
			}*/
			if (corpUserService.loadPendingHisWithCorpId(corpUser.getUserId(),corpUser.getCorpId()) != null) {
				throw new NTBException("err.sys.UserNotAvailableForPending");
			}
			//modified by lzg end
		}

		// 閿熸枻鎷风粐CorpPreference pojo
		Map resultData = this.getResultData();
		String prefId = CibIdGenerator.getIdForOperation("CORP_PREFERENCE");
		CorpUser user = (CorpUser) this.getUser();

		int adminCount = user.getCorpAdministatorCount();
		// 閿熷彨璁规嫹Administrator閿熶茎闈╂嫹閿熸枻鎷�
		if (adminCount > 1) {
			FlowEngineService flowEngineService = (FlowEngineService) Config
					.getAppContext().getBean("FlowEngineService");
			String processId = flowEngineService.startProcess(
					Constants.TXN_SUBTYPE_GROUP_ASSIGN, "0", GroupAction.class,
					null, 0, null, 0, 0, prefId, null, getUser(), null, null,
					null);
			try {
				// 閿熸枻鎷烽敓绲瀝oup閿熸枻鎷烽敓鏂ゆ嫹
				CorpPreference corpPref = new CorpPreference(prefId);
				corpPref.setCorpId(user.getCorpId());
				groupService.add4AssignGroup(corpPref, newUserList);
				// 閿熸枻鎷烽敓缁炵》鎷�
				for (int i = 0; i < newUserList.size(); i++) {
					CorpUser newUser = (CorpUser) newUserList.get(i);
					Map mailData = new HashMap();
					this.convertPojo2Map(newUser, mailData);
					mailData.put("requestTime", new Date());
					// mailData.put("corpName",
					// newUser.getCorporation().getCorpName());
					mailData.put("corpName", corporationService.view(
							newUser.getCorpId()).getCorpName());
					mailService.toMember_GroupAssigned(newUser.getUserId(),
							newUser.getGroupId(), mailData);
				}
			} catch (Exception e) {
				flowEngineService.cancelProcess(processId, getUser());
				e.printStackTrace();
				throw new NTBException(e.getMessage());
			}
			resultData.put("ackStatus", Constants.ACK_STATUS_PENDING_APPROVAL);
		} else {
			try {
				// 閿熸枻鎷烽敓绲瀝oup閿熸枻鎷烽敓鏂ゆ嫹
				CorpPreference corpPref = new CorpPreference(prefId);
				corpPref.setCorpId(user.getCorpId());
				groupService.add4AssignGroup(corpPref, newUserList);

				// 閿熸枻鎷烽敓鏂ゆ嫹group閿熸枻鎷烽敓鏂ゆ嫹
				groupService.approveAssignGroup(prefId);

				// 閿熸枻鎷烽敓缁炵》鎷�
				for (int i = 0; i < newUserList.size(); i++) {
					CorpUser newUser = (CorpUser) newUserList.get(i);
					Map mailData = new HashMap();
					this.convertPojo2Map(newUser, mailData);
					mailData.put("requestTime", new Date());
					// mailData.put("corpName",
					// newUser.getCorporation().getCorpName());
					mailData.put("corpName", corporationService.view(
							newUser.getCorpId()).getCorpName());
					mailService.toMember_GroupAssigned(newUser.getUserId(),
							newUser.getGroupId(), mailData);
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw new NTBException(e.getMessage());
			}
			resultData.put("ackStatus", Constants.ACK_STATUS_ACCOMPLISHED);
		}

		this.setResultData(resultData);

	}

	public boolean approve(String txnType, String id, CibAction bean)
			throws NTBException {
		ApplicationContext appContext = Config.getAppContext();
		GroupService groupService = (GroupService) Config.getAppContext()
				.getBean("GroupService");
		MailService mailService = (MailService) appContext
				.getBean("MailService");
		if (txnType.equals(Constants.TXN_SUBTYPE_GROUP_ADD)) {
			UserGroupHis userGroupHis = groupService
					.loadUserGroupHisBySeqNo(id);
			userGroupHis.setStatus(Constants.STATUS_NORMAL);
			userGroupHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			userGroupHis.setLastUpdateTime(new Date());
			groupService.updateHis(userGroupHis);

			// update corpUser
			UserGroup userGroup = new UserGroup();
			try {
				BeanUtils.copyProperties(userGroup, userGroupHis);
			} catch (Exception e) {
				Log.error("Error copy properties", e);
				throw new NTBException("err.sys.GeneralError");
			}
			groupService.update(userGroup);
			groupService.updateGroupPermission(userGroup.getGroupId(),
					userGroup.getVersion());
		} else if (txnType.equals(Constants.TXN_SUBTYPE_GROUP_EDIT)) {
			UserGroupHis userGroupHis = groupService
					.loadUserGroupHisBySeqNo(id);
			userGroupHis.setStatus(Constants.STATUS_NORMAL);
			userGroupHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			userGroupHis.setLastUpdateTime(new Date());
			groupService.updateHis(userGroupHis);
			groupService.deleteGroupPermission(userGroupHis.getGroupId(),
					Constants.STATUS_NORMAL);

			// update corpUser
			UserGroup userGroup = new UserGroup();
			try {
				BeanUtils.copyProperties(userGroup, userGroupHis);
			} catch (Exception e) {
				Log.error("Error copy properties", e);
				throw new NTBException("err.sys.GeneralError");
			}
			groupService.update(userGroup);
			groupService.updateGroupPermission(userGroup.getGroupId(),
					userGroup.getVersion());

			// 閿熸枻鎷烽敓鏂ゆ嫹閿熺粸纭锋嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷锋病閿燂拷
			Map mailData = new HashMap();
			this.convertPojo2Map(userGroup, mailData);
			mailService.toMember_GroupUpdated(id, mailData);
		} else if (txnType.equals(Constants.TXN_SUBTYPE_GROUP_DELETE)) {
			UserGroupHis userGroupHis = groupService
					.loadUserGroupHisBySeqNo(id);
			userGroupHis.setStatus(Constants.STATUS_REMOVED);
			userGroupHis.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
			userGroupHis.setLastUpdateTime(new Date());
			groupService.updateHis(userGroupHis);
			groupService.deleteGroupPermission(userGroupHis.getGroupId(),
					Constants.STATUS_NORMAL);

			// update corpUser
			UserGroup userGroup = new UserGroup();
			try {
				BeanUtils.copyProperties(userGroup, userGroupHis);
			} catch (Exception e) {
				Log.error("Error copy properties", e);
				throw new NTBException("err.sys.GeneralError");
			}
			groupService.update(userGroup);
		} else if (txnType.equals(Constants.TXN_SUBTYPE_GROUP_ASSIGN)) {
			groupService.approveAssignGroup(id);
		}

		// 閿熸枻鎷烽敓鏂ゆ嫹Group
		CachedDBRCFactory.addPendingCache("groupByCorp");
		return true;
	}

	public boolean reject(String txnType, String id, CibAction bean)
			throws NTBException {
		GroupService groupService = (GroupService) Config.getAppContext()
				.getBean("GroupService");
		if (txnType.equals(Constants.TXN_SUBTYPE_GROUP_ADD)) {
			UserGroupHis userGroupHis = groupService
					.loadUserGroupHisBySeqNo(id);
			userGroupHis.setStatus(Constants.STATUS_REMOVED);
			userGroupHis.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
			userGroupHis.setLastUpdateTime(new Date());
			groupService.updateHis(userGroupHis);
			groupService.deleteGroupPermission(userGroupHis.getGroupId(),
					userGroupHis.getVersion());

			UserGroup userGroup = groupService.loadUserGroup(userGroupHis
					.getGroupId());
			userGroup.setStatus(Constants.STATUS_REMOVED);
			userGroup.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
			userGroupHis.setLastUpdateTime(new Date());
			groupService.update(userGroup);
		} else if (txnType.equals(Constants.TXN_SUBTYPE_GROUP_EDIT)) {
			UserGroupHis userGroupHis = groupService
					.loadUserGroupHisBySeqNo(id);
			userGroupHis.setStatus(Constants.STATUS_REMOVED);
			userGroupHis.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
			userGroupHis.setLastUpdateTime(new Date());
			groupService.updateHis(userGroupHis);
			groupService.deleteGroupPermission(userGroupHis.getGroupId(),
					userGroupHis.getVersion());
		} else if (txnType.equals(Constants.TXN_SUBTYPE_GROUP_DELETE)) {
			UserGroupHis userGroupHis = groupService
					.loadUserGroupHisBySeqNo(id);
			userGroupHis.setStatus(Constants.STATUS_REMOVED);
			userGroupHis.setAuthStatus(Constants.AUTH_STATUS_REJECTED);
			userGroupHis.setLastUpdateTime(new Date());
			groupService.updateHis(userGroupHis);
			groupService.deleteGroupPermission(userGroupHis.getGroupId(),
					userGroupHis.getVersion());
		} else if (txnType.equals(Constants.TXN_SUBTYPE_GROUP_ASSIGN)) {
			groupService.rejectAssignGroup(id);
		}
		return true;
	}

	public String viewDetail(String txnType, String id, CibAction bean)
			throws NTBException {
		// TODO Auto-generated method stub
		Map resultData = bean.getResultData();
		if (txnType.equals(Constants.TXN_SUBTYPE_GROUP_ASSIGN)) {
			if(resultData.get("newUserList") == null){
				resultData.put("newUserList", viewAssignGroup(id));
			}
			return "/WEB-INF/pages/sys/group_man/assignGroupView.jsp";
		} else {
			resultData.putAll(this.viewHisBySeqNo(id));
		}
		bean.setResultData(resultData);
		return "/WEB-INF/pages/sys/group_man/userGroup_view.jsp";
	}

	public boolean cancel(String txnType, String id, CibAction bean)
			throws NTBException {
		return reject(txnType, id, bean);
	}
}
