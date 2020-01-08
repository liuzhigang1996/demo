package app.cib.test.txn;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;

import app.cib.bo.txn.BillPayment;
import app.cib.bo.txn.TransferBank;
import app.cib.bo.txn.TransferMacau;
import app.cib.bo.txn.TransferOversea;
import app.cib.service.txn.TransferTemplateService;
import junit.framework.TestCase;

public class TransferTemplateServiceImplTest extends TestCase {
	TransferTemplateService transferTemplateService;
	public static void main(String[] args) {
	}

	protected void setUp() throws Exception {
		super.setUp();
		ApplicationContext appContext = Config.getAppContext();
	    transferTemplateService = (TransferTemplateService)appContext.getBean("TransferTemplateService");
	}

	/*
	 * Test method for 'app.cib.service.txn.TransferTemplateServiceImpl.addTemplateBANK(TransferBank)'
	 */
   
	public void testAddTemplateBANK() {
		 TransferBank transferBank = new TransferBank("1949");
		 transferBank.setCorpId("10001");
		 transferBank.setUserId("1101");
		 transferBank.setFromAccount("123");
		 transferBank.setRecordType("9");
		 
		 try {
				transferTemplateService.addTemplateBANK(transferBank);
			} catch (Exception e) {
				e.printStackTrace();
			}

	}	
    
	/*
	public void testAddTemplateOversea() {
		 TransferOversea transferOversea = new TransferOversea("1499");
		 transferOversea.setCorpId("aaaa");
		 transferOversea.setUserId("mxl");
		 transferOversea.setFromAccount("123");
		 transferOversea.setRecordType("9");
		 
		 try {
				transferTemplateService.addTemplateOversea(transferOversea);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	*/
	/*
	public void testAddTemplateMacau() {
		 TransferMacau transferMacau = new TransferMacau("1499");
		 transferMacau.setCorpId("aaaa");
		 transferMacau.setUserId("mxl");
		 transferMacau.setFromAccount("123");
		 transferMacau.setRecordType("9");
		 
		 try {
				transferTemplateService.addTemplateMacau(transferMacau);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	*/
    /*
     * Test Edit
     */
	/*
	public void testeditTemplateBANK() {
		 TransferBank transferBank;
		 try {
			 transferBank = transferTemplateService.viewTemplate("1949");
			 System.out.println("TransId="+transferBank.getTransId());
			 transferBank.setRemark("My sister");
			 transferBank.setUserId("1101");
			 transferBank.setToAccount("2345");
			 transferTemplateService.editTemplateBANK(transferBank,"1101");
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		
	}
	*/
	
	/*
	public void testeditTemplateMacau() {
		 TransferMacau transferMacau;
		 try {
			 transferMacau = transferTemplateService.viewTemplateMacau("1399");
			 transferMacau.setCorpId("kskdjjk");
			 transferTemplateService.editTemplateMacau(transferMacau,"mxl");
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		
	}
	
	
	public void testeditTemplateOversea() {
		 TransferOversea transferOversea;
		 try {
			 transferOversea= transferTemplateService.viewTemplateOversea("1399");
			 transferOversea.setCorpId("kskdjjk");
			 transferTemplateService.editTemplateOversea(transferOversea,"mxl");
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		
	}
	*/
	
	
	
	
	
	
	/*
	 * Test method for 'app.cib.service.txn.TransferTemplateServiceImpl.editTemplateBANK(TransferBank)'
	 */


	/*
	 * Test method for 'app.cib.service.txn.TransferTemplateServiceImpl.deleteTemplateBANK(String)'
	 */
	

	/*
	 * Test method for 'app.cib.service.txn.TransferTemplateServiceImpl.listTemplateBANK(Map)'
	 */

	public void testListTemplateBANK() {
		TransferBank transferBank = null;
		try {
			List list = transferTemplateService.listTemplateBANK("10001");
			for (int i = 0; i<list.size(); i++){
				transferBank = (TransferBank) list.get(i);
				System.err.println("Trans_ID "+transferBank.getTransId());
				System.err.println("Record_Type "+transferBank.getRecordType());
				System.err.println("Operation "+transferBank.getOperation() );
			}
		} catch (NTBException e ) {
			e.printStackTrace();
		}

	}


	
 /*
	public void testListTemplateOversea() {
		TransferOversea transferOversea = null;
		try {
			List list = transferTemplateService.listTemplateOversea("10001","1101");
			for (int i = 0; i<list.size(); i++){
				transferOversea = (TransferOversea) list.get(i);
				System.err.println("Trans_ID "+transferOversea.getTransId());
				System.err.println("Record_Type "+transferOversea.getRecordType());
				System.err.println("Operation "+transferOversea.getOperation() );
			}
		} catch (NTBException e ) {
			e.printStackTrace();
		}

	}
	*/

/*
	public void testListTemplateMacau() {
		TransferMacau transferMacau = null;
		try {
			List list = transferTemplateService.listTemplateMacau("10001","1101");
			for (int i = 0; i<list.size(); i++){
				transferMacau = (TransferMacau) list.get(i);
				System.err.println("Trans_ID "+transferMacau.getTransId());
				System.err.println("Record_Type "+transferMacau.getRecordType());
				System.err.println("Operation "+transferMacau.getOperation() );
			}
		} catch (NTBException e ) {
			e.printStackTrace();
		}

	}

	*/

	
	
	/*
	 * Test method for 'app.cib.service.txn.TransferTemplateServiceImpl.execTemplateBANK(TransferBank)'
	 */
	/*
	public void testExecTemplateBANK() {
		TransferBank transferBank = null;
		try {
			transferBank = transferTemplateService.viewTemplate("1399");
			transferTemplateService.execTemplateBANK(transferBank);

	} catch (NTBException e) {
		e.printStackTrace();
	}
	}
	*/
	/*
	public void testExecTemplateOversea() {
		TransferOversea transferOversea = null;
		try {
			transferOversea = transferTemplateService.viewTemplateOversea("1399");
			transferTemplateService.execTemplateOversea(transferOversea);

	} catch (NTBException e) {
		e.printStackTrace();
	}
	}
	public void testExecTemplateMacau() {
		TransferMacau transferMacau = null;
		try {
			transferMacau = transferTemplateService.viewTemplateMacau("1399");
			transferTemplateService.execTemplateMacau(transferMacau);

	} catch (NTBException e) {
		e.printStackTrace();
	}
	}
	 */
	/* Test view */
	/*
	public void testviewTemplate() {
		try {
			transferTemplateService.viewTemplate("1399");
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	*/
	/*
	public void testviewTemplateMacau() {
		try {
			transferTemplateService.viewTemplateOversea("1399");
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	
	
	
public void testviewTemplateOversea() {
	try {
		transferTemplateService.viewTemplateOversea("1399");
					
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}
*/
/* Test Delete */
/*
public void testdeleteTemplateBANK() {
	try {
		transferTemplateService.deleteTemplateBANK("1399","mxl");
	} catch (NTBException e) {
		e.printStackTrace();
	}
}
*/
/*
public void testdeleteTemplateMacau() {
	try {
		transferTemplateService.deleteTemplateMacau("1399","mxl");
	} catch (NTBException e) {
		e.printStackTrace();
	}
}

public void testdeleteTemplateOversea() {
	try {
		transferTemplateService.deleteTemplateOversea("1399","mxl");
	} catch (NTBException e) {
		e.printStackTrace();
	}
}
*/


}




	

