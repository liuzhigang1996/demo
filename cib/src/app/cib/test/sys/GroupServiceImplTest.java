package app.cib.test.sys;

import java.util.List;

import org.springframework.context.ApplicationContext;

import app.cib.bo.sys.UserGroup;
import app.cib.bo.txn.TransferOversea;
import app.cib.service.sys.GroupService;
import app.cib.service.txn.TransferTemplateService;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;

import junit.framework.TestCase;

public class GroupServiceImplTest extends TestCase {
	GroupService groupService = null;
	public static void main(String[] args) {
	}

	protected void setUp() throws Exception {
		super.setUp();
		//ApplicationContext appContext = Config.getAppContext();
		//GroupService groupService = (GroupService) Config.getAppContext().getBean("GroupService");
		ApplicationContext appContext = Config.getAppContext();
	    groupService = (GroupService)appContext.getBean("GroupService");
	}

	/*
	 * Test method for 'app.cib.service.sys.GroupServiceImpl.getUserGroupDao()'
	 */
	public void testGetUserGroupDao() {

	}

	/*
	 * Test method for 'app.cib.service.sys.GroupServiceImpl.setUserGroupDao(UserGroupDao)'
	 */
	public void testSetUserGroupDao() {

	}

	/*
	 * Test method for 'app.cib.service.sys.GroupServiceImpl.listUserGroup(CorpUser)'
	 */
	public void testListUserGroupCorpUser() {

	}

	/*
	 * Test method for 'app.cib.service.sys.GroupServiceImpl.loadUserGroup(String)'
	 */
	public void testLoadUserGroup() {

	}

	/*
	 * Test method for 'app.cib.service.sys.GroupServiceImpl.add(UserGroup)'
	 */
	public void testAdd() {

	}

	/*
	 * Test method for 'app.cib.service.sys.GroupServiceImpl.update(UserGroup)'
	 */
	public void testUpdate() {

	}

	/*
	 * Test method for 'app.cib.service.sys.GroupServiceImpl.delete(UserGroup)'
	 */
	public void testDelete() {

	}

	/*
	 * Test method for 'app.cib.service.sys.GroupServiceImpl.listUserGroup(String)'
	 */
	public void testListUserGroupString() {
		//TransferOversea transferOversea = null;
		UserGroup userGroup = null;
		try {
			//List list = transferTemplateService.listTemplateOversea("10001","1101");
			List list = groupService.listUserGroupByCorp("10001");

			for (int i = 0; i<list.size(); i++){
				userGroup = (UserGroup) list.get(i);
				System.err.println("Trans_ID "+userGroup.getGroupId());
				System.err.println("Record_Type "+userGroup.getGroupName());
			}
		} catch (NTBException e ) {
			e.printStackTrace();
		}

	}

}
