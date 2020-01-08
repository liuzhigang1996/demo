package app.cib.test.txn;

import java.util.Date;
import java.util.List;
import com.neturbo.set.exception.NTBException;
import org.springframework.context.ApplicationContext;

import app.cib.bo.txn.TransferBank;
import app.cib.service.txn.CorpTransferService;
import app.cib.service.txn.TransferService;

import com.neturbo.set.core.Config;
import com.neturbo.set.database.IDGenerator;

import junit.framework.TestCase;
import app.cib.core.CibIdGenerator;

public class CorpTransferServiceImplTest extends TestCase {

	CorpTransferService corpTransferService;

	public static void main(String[] args) {
	}

	protected void setUp() throws Exception {
		super.setUp();
		ApplicationContext appContext = Config.getAppContext();
		corpTransferService = (CorpTransferService)appContext.getBean("CorpTransferService");
	}
	/*
	public void testTransferHistory() {
		//fail("Not yet implemented");
		System.out.println("Test history");
		System.err.println("Test history");

		TransferBank transferBank = null;
		try {
			//List list = billPaymentService.listHistory("2006-07-18 00:00:00", "2006-07-18 00:00:00", "CEM");
			List list = corpTransferService.transferHistory("2006-07-18 00:00:00","2006-09-30 00:00:00","10001","1101","9000010011");

			System.err.println("|testProcessPayment");
			System.err.println("|-> TransId" + "\t" + "PayType" + "\t" + "Status" + "\t" + "AuthStatus" + "\t" + "Merchant" + "\t" + "RequestTime");
			for (int i = 0; i<list.size(); i++){
				//billPayment = (BillPayment) list.get(i);
				transferBank = (TransferBank) list.get(i);
				System.err.println("|-> " + transferBank.getTransId());
				//System.err.println("|-> " + billPayment.getTransId() + "\t" + billPayment.getPayType() + "\t" + billPayment.getStatus() + "\t" + billPayment.getAuthStatus() + "\t\t" + billPayment.getMerchant() + "\t\t" + billPayment.getRequestTime());
			}
		} catch (NTBException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
     */
	/*
	 * Test method for 'app.cib.service.txn.CorpTransferServiceImpl.transferDelivery(TransferBank)'
	 */

	/*
	public void testTransferDelivery() {
		 TransferBank transferBank = new TransferBank("2336");
		 transferBank.setCorpId("aaaa");
		 transferBank.setUserId("mxl");
		 transferBank.setFromAccount("123");

		 try {
			 corpTransferService.transferDelivery(transferBank);

			} catch (Exception e) {
				e.printStackTrace();
			}

	}
     */
	/*
	 * Test method for 'app.cib.service.txn.CorpTransferServiceImpl.transferSubsidiary(TransferBank)'
	 */


	public void testTransferSubsidiary() {
		TransferBank transferBank = new TransferBank(CibIdGenerator.getRefNoForTransaction());
		// TransferBank transferBank = new TransferBank("2337");
		 transferBank.setCorpId("bbbb");
		 transferBank.setUserId("mxl");
		 transferBank.setFromAccount("123");
		 transferBank.setExecuteTime(new Date());
		 transferBank.setRequestTime(new Date());

		 try {
			 corpTransferService.transferDelivery(transferBank);

			} catch (Exception e) {
				e.printStackTrace();
			}

	}


	/*
	 * Test method for 'app.cib.service.txn.CorpTransferServiceImpl.transferRepatriate(TransferBank)'
	 */

	/*
	public void testTransferRepatriate() {
		TransferBank transferBank = new TransferBank(String.valueOf(IDGenerator.getId("TRANSFER_BANK")));
		// TransferBank transferBank = new TransferBank("2337");
		 transferBank.setCorpId("aaaa");
		 transferBank.setUserId("mxl");
		 transferBank.setFromAccount("123");
		 transferBank.setExecuteTime(new Date());
		 transferBank.setRequestTime(new Date());

		 try {
			 corpTransferService.transferDelivery(transferBank);

			} catch (Exception e) {
				e.printStackTrace();
			}


	}
*/
	/*
	 * Test method for 'app.cib.service.txn.CorpTransferServiceImpl.getCorpTransferDao()'
	 */
	public void testGetCorpTransferDao() {

	}

	/*
	 * Test method for 'app.cib.service.txn.CorpTransferServiceImpl.setCorpTransferDao(CorpTransferDao)'
	 */
	public void testSetCorpTransferDao() {

	}

}
