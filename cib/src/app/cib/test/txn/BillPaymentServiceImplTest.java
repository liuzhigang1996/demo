/**
 *
 */
package app.cib.test.txn;

import java.util.List;

import org.springframework.context.ApplicationContext;

import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.BillPayment;
import app.cib.service.txn.BillPaymentService;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;

import junit.framework.TestCase;

/**
 * @author hjs
 *
 */
public class BillPaymentServiceImplTest extends TestCase {

	BillPaymentService billPaymentService ;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

        ApplicationContext appContext = Config.getAppContext();
        billPaymentService = (BillPaymentService)appContext.getBean("BillPaymentService");
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link app.cib.service.txn.BillPaymentServiceImpl#processPayment(app.cib.bo.txn.BillPayment)}.
	 */
	public void testProcessPayment() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link app.cib.service.txn.BillPaymentServiceImpl#listHistory(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public void testListHistory() {
		//fail("Not yet implemented");
		BillPayment billPayment = null;
		CorpUser corpUser = new CorpUser("1101");
		corpUser.setCorpId("10001");
		try {
			List list = billPaymentService.listHistory("10001", "1101", "2006-07-18 00:00:00", "2006-07-29 00:00:00", "0002", "908919254");
			System.err.println("|testProcessPayment");
			System.err.println("|-> TransId" + "\t" + "PayType" + "\t" + "Status" + "\t" + "AuthStatus" + "\t" + "Merchant" + "\t" + "RequestTime");
			for (int i = 0; i<list.size(); i++){
				billPayment = (BillPayment) list.get(i);
				System.err.println("|-> " + billPayment.getTransId() + "\t" + billPayment.getPayType() + "\t" + billPayment.getStatus() + "\t" + billPayment.getAuthStatus() + "\t\t" + billPayment.getMerchant() + "\t\t" + billPayment.getRequestTime());
			}
		} catch (NTBException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
