package app.cib.test.txn;

import org.springframework.context.ApplicationContext;

import app.cib.service.txn.TransferLimitService;
import app.cib.util.Constants;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;

import junit.framework.TestCase;

public class TransferLimitServiceImplTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		ApplicationContext appContext = Config.getAppContext();
		TransferLimitService transferLimitService = (TransferLimitService)appContext.getBean("TransferLimitService");
	}

	public void testGetTxnLimitDao() {
		fail("Not yet implemented");
	}

	public void testSetTxnLimitDao() {
		fail("Not yet implemented");
	}

	public void testGetTxnLimitUsageDao() {
		fail("Not yet implemented");
	}

	public void testSetTxnLimitUsageDao() {
		fail("Not yet implemented");
	}

	public void testAddUsedLimitQuota() throws NTBException{
		ApplicationContext appContext = Config.getAppContext();
		TransferLimitService transferLimitService = (TransferLimitService)appContext.getBean("TransferLimitService");
		if (transferLimitService.checkLimitQuota("9000010011", "10001", Constants.TXN_SUBTYPE_TRANSFER_BANK, 3000, 3000)) {
			System.out.println("ok1");
		} else {
			System.out.println("error1");
		}
		transferLimitService.addUsedLimitQuota("9000010011", "10001", Constants.TXN_SUBTYPE_TRANSFER_BANK, 3000, 3000);
		if (transferLimitService.checkLimitQuota("9000010011", "10001", Constants.TXN_SUBTYPE_TRANSFER_BANK, 3000, 3000)) {
			System.out.println("ok2");
		} else {
			System.out.println("error2");
		}
		transferLimitService.addUsedLimitQuota("9000010011", "10001", Constants.TXN_SUBTYPE_TRANSFER_BANK, 3000, 3000);
	}

	public void testCheckLimitQuota() {
		fail("Not yet implemented");
	}

	public void testHashCode() {
		fail("Not yet implemented");
	}

	public void testObject() {
		fail("Not yet implemented");
	}

	public void testFinalize() {
		fail("Not yet implemented");
	}

	public void testNotify() {
		fail("Not yet implemented");
	}

	public void testNotifyAll() {
		fail("Not yet implemented");
	}

	public void testWait() {
		fail("Not yet implemented");
	}

	public void testWaitLong() {
		fail("Not yet implemented");
	}

	public void testWaitLongInt() {
		fail("Not yet implemented");
	}

	public void testGetClass() {
		fail("Not yet implemented");
	}

	public void testClone() {
		fail("Not yet implemented");
	}

	public void testEquals() {
		fail("Not yet implemented");
	}

	public void testToString() {
		fail("Not yet implemented");
	}

}
