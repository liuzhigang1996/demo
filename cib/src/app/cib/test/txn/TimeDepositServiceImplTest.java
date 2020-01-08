/**
 * 
 */
package app.cib.test.txn;

import java.util.Date;

import org.springframework.context.ApplicationContext;

import app.cib.bo.txn.TimeDeposit;
import app.cib.core.CibIdGenerator;
import app.cib.service.txn.TimeDepositService;
import app.cib.util.Constants;

import com.neturbo.set.core.Config;
import com.neturbo.set.database.IDGenerator;
import com.neturbo.set.exception.NTBException;

import junit.framework.TestCase;

/**
 * @author hjs
 *
 */
public class TimeDepositServiceImplTest extends TestCase {

	TimeDepositService tDAccountService;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
        ApplicationContext appContext = Config.getAppContext();
        tDAccountService = (TimeDepositService)appContext.getBean("TDAccountService");
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link app.cib.service.txn.TimeDepositServiceImpl#addTiemDeposit(app.cib.bo.txn.TimeDeposit)}.
	 */
	public void testAddTiemDeposit() {
		//fail("Not yet implemented");
		int transID = Integer.parseInt(CibIdGenerator.getRefNoForTransaction());
		TimeDeposit timeDeposit = new TimeDeposit(String.valueOf(transID));
		timeDeposit.setCorpId("10001");
		timeDeposit.setCurrentAccount("9000034567");
		timeDeposit.setMaturityDate(new Date());
		timeDeposit.setCurrency("MOP");
		timeDeposit.setPrincipal(new Double(1000));
		timeDeposit.setRequestTime(new Date());
		timeDeposit.setUserId("1101");
		timeDeposit.setTdType(TimeDeposit.TD_TYPE_NEW);
		timeDeposit.setStatus(Constants.STATUS_PENDING_APPROVAL);
		timeDeposit.setAuthStatus(Constants.AUTH_STATUS_SUBMITED);
        try {
        	tDAccountService.addTiemDeposit(timeDeposit);
		} catch (NTBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link app.cib.service.txn.TimeDepositServiceImpl#viewTiemDeposit(java.lang.String)}.
	 */
	public void testViewTiemDeposit() {
		//fail("Not yet implemented");
		try {
			assertNotNull(tDAccountService.viewTimeDeposit("10009"));
		} catch (NTBException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	/**
	 * Test method for {@link app.cib.service.txn.TimeDepositServiceImpl#withdrawTiemDeposit(java.lang.String, java.lang.String)}.
	 */
	public void testWithdrawTiemDeposit() {
		//fail("Not yet implemented");
		TimeDeposit timeDeposit = null;
		try {
			timeDeposit = tDAccountService.viewTimeDeposit("10009");
			tDAccountService.withdrawTiemDeposit(timeDeposit);
			assertEquals("3", tDAccountService.viewTimeDeposit("10009").getStatus());
		} catch (NTBException e) {
			e.printStackTrace();
			assertTrue(false);
		}
		
	}

	/**
	 * Test method for {@link app.cib.service.txn.TimeDepositServiceImpl#listTimeDeposit(java.lang.String, java.lang.String)}.
	 */
	public void testListTimeDeposit() {
		//fail("Not yet implemented");
		/*
		TimeDeposit timeDeposit = null;
		try {
			List list = tDAccountService.listTimeDeposit("10001", "MOP");
			for (int i = 0; i<list.size(); i++){
				timeDeposit = (TimeDeposit) list.get(i);
				System.err.println(timeDeposit.getTransId());				
			}
		} catch (NTBException e) {
			e.printStackTrace();
			this.assertTrue(false);
		}
		*/
	}

	/**
	 * Test method for {@link app.cib.service.txn.TimeDepositServiceImpl#downloadList(java.util.List)}.
	 */
	public void testDownloadList() {
		//fail("Not yet implemented");
	}

}
