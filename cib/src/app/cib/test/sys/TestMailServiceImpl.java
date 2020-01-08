package app.cib.test.sys;

import junit.framework.*;
import app.cib.service.sys.*;
import java.util.*;
import org.springframework.context.ApplicationContext;
import com.neturbo.set.core.Config;
import com.neturbo.set.database.GenericJdbcDao;

import app.cib.dao.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class TestMailServiceImpl extends TestCase {
    private MailService mailService = null;
    
    private GenericJdbcDao dao1 = null;

    protected void setUp() throws Exception {
        super.setUp();
        Config.setAppRoot("E:/BANK_CIB/dev/WebContent");
        ApplicationContext appContext = Config.getAppContext();
        mailService = (MailService) appContext.getBean("MailService");
        dao1 = (GenericJdbcDao) appContext.getBean("genericJdbcDao");
    }

    protected void tearDown() throws Exception {
        mailService = null;
        super.tearDown();
    }
/*
    public void testToApprover_Changed() {
        String txnType = "";
        List userList = null;
        Map dataMap = null;
        mailService.toApprover_Changed(txnType, userList, dataMap);
    }

    public void testToApprover_Requested() {
        String txnType = "";
        List userList = null;
        Map dataMap = null;
        mailService.toApprover_Requested(txnType, userList, dataMap);
    }

    public void testToApprover_Seleted() {
        String txnType = "";
        List userList = null;
        Map dataMap = null;
        mailService.toApprover_Seleted(txnType, userList, dataMap);
    }

    public void testToBankUser_Operated() {
        String operation = "";
        String userId = "";
        Map dataMap = null;
        mailService.toBankUser_Operated(operation, userId, dataMap);
    }
*/
    public void testToCorpUser_Operated() {
    	try {
			List aaa = dao1.query("values strip('000001', 'L','0')", new Object[]{});
			System.out.println(aaa);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	/*
        String operation = "1";
        String userId = "nabai";
        Map dataMap = new HashMap();
        dataMap.put("userId", "Nabai");
        dataMap.put("userName", "Nabai");
        mailService.toCorpUser_Operated(operation, userId, dataMap);
        assertTrue(true);
        */
    }
/*
    public void testToMember_GroupAssigned() {
        String userId = "";
        String groupId = "";
        Map dataMap = null;
        mailService.toMember_GroupAssigned(userId, groupId, dataMap);
    }

    public void testToMember_GroupUpdated() {
        String groupId = "";
        Map dataMap = null;
        mailService.toMember_GroupUpdated(groupId, dataMap);
    }

    public void testToRequester_Approved() {
        String txnType = "";
        List userList = null;
        Map dataMap = null;
        mailService.toRequester_Approved(txnType, userList, dataMap);
    }

    public void testToRequester_Rejected() {
        String txnType = "";
        List userList = null;
        Map dataMap = null;
        mailService.toRequester_Rejected(txnType, userList, dataMap);
    }
*/
}
