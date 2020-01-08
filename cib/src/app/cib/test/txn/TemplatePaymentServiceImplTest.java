package app.cib.test.txn;

import java.util.*;

import org.springframework.context.ApplicationContext;

import app.cib.bo.txn.BillPayment;
import app.cib.core.CibIdGenerator;
import app.cib.service.txn.TemplatePaymentService;
import app.cib.util.Constants;

import com.neturbo.set.core.Config;
import com.neturbo.set.database.IDGenerator;
import com.neturbo.set.exception.NTBException;

import junit.framework.TestCase;

public class TemplatePaymentServiceImplTest extends TestCase {
	
	TemplatePaymentService tempPaymentService = null;

	protected void setUp() throws Exception {
		super.setUp();
		
        ApplicationContext appContext = Config.getAppContext();
        tempPaymentService = (TemplatePaymentService)appContext.getBean("TemplatePaymentService");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddTemplate() {
		int transID = Integer.parseInt(CibIdGenerator.getRefNoForTransaction());
        BillPayment billPayment = new BillPayment(String.valueOf(transID));
        billPayment.setBillNo1("79123123");
        billPayment.setCategory("0");
        billPayment.setCorpId("10001");
        billPayment.setCurrency("0");
        billPayment.setFromAccount("9000034567");
        billPayment.setMerchant("0");
        billPayment.setPayType(BillPayment.PAYMENT_TYPE_GENERAL_TEMPLATE);
        billPayment.setRequestTime(new Date());
        billPayment.setOperation(Constants.OPERATION_NEW);
        billPayment.setStatus(Constants.STATUS_NORMAL);
        billPayment.setAuthStatus(Constants.AUTH_STATUS_COMPLETED);
        billPayment.setTransferAmount(new Double(1000));
        billPayment.setUserId("1101");
        try {
			tempPaymentService.addTemplate(billPayment);
			assertNotNull(tempPaymentService.viewTemplate(String.valueOf(transID)));
		} catch (NTBException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	public void testDeleteTemplate() {
		//fail("Not yet implemented");
		BillPayment billPayment = new BillPayment("1");
		try {
			tempPaymentService.deleteTemplate(billPayment, "1101");
		} catch (NTBException e) {
			e.printStackTrace();
		}
	}

	public void testEditTemplate() {
		//fail("Not yet implemented");
		BillPayment billPayment;
		try {
			billPayment = tempPaymentService.viewTemplate("1");
			billPayment.setBillNo1("1234");
			tempPaymentService.editTemplate(billPayment, "1101");
			assertEquals("1234", tempPaymentService.viewTemplate(billPayment.getTransId()).getBillNo1());
		} catch (NTBException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	public void testViewTemplate() {
		//fail("Not yet implemented");
		try {
			assertNotNull(tempPaymentService.viewTemplate("5"));
		} catch (NTBException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	public void testListTemplate() {
		//fail("Not yet implemented");
		BillPayment billPayment = null;
		try {
			List list = tempPaymentService.listTemplate("10001", "CEM");
			System.err.println("|testListTemplate");
			System.err.println("|-> TransId" + "\t" + "PayType" + "\t" + "Status" + "\t" + "AuthStatus");
			for (int i = 0; i<list.size(); i++){
				billPayment = (BillPayment) list.get(i);
				System.err.println("|-> " + billPayment.getTransId() + "\t" + billPayment.getPayType() + "\t" + billPayment.getStatus() + "\t" + billPayment.getAuthStatus());				
			}
		} catch (NTBException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	public void testPayTemplate() {
		//fail("Not yet implemented");
		List pojoList = new ArrayList();
		BillPayment billPayment = null;
		try {
			billPayment = tempPaymentService.viewTemplate("1");
			pojoList.add(billPayment);
			billPayment = tempPaymentService.viewTemplate("5");
			pojoList.add(billPayment);
			List list = tempPaymentService.payTemplate(pojoList);
			System.err.println("|testPayTemplate");
			System.err.println("|-> TransId" + "\t" + "PayType" + "\t" + "Status" + "\t" + "AuthStatus");
			for (int i = 0; i<list.size(); i++){
				billPayment = (BillPayment) list.get(i);
				System.err.println("|-> " + billPayment.getTransId() + "\t" + billPayment.getPayType() + "\t" + billPayment.getStatus() + "\t" + billPayment.getAuthStatus());
			}
		} catch (NTBException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
