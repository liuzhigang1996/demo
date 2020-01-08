package app.cib.test.txn;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.transaction.*;
import com.neturbo.set.exception.NTBException;

import app.cib.bo.sys.CorpUser;
import app.cib.bo.txn.BillPayment;
import app.cib.bo.txn.TransferBank;
import app.cib.bo.txn.TransferMacau;
import app.cib.bo.txn.TransferOversea;
import app.cib.service.txn.TransferService;
import junit.framework.TestCase;

public class TransferServiceImplTest extends TestCase {
	TransferService transferService;
	CorpUser user = null;
	
	protected void setUp() throws Exception {
		super.setUp();

        ApplicationContext appContext = Config.getAppContext();
        transferService = (TransferService)appContext.getBean("TransferService");
        TransXMLFactory.setTransXMLDir("C:/BANKCIB/WebContent/WEB-INF/transxml");
	}

	/*
	 * Test method for 'app.cib.service.txn.TransferServiceImpl.main(String[])'
	 */
	public void testMain() {

	}
	/*
	public void testToHostChargeEnquiryNew() {
		System.err.println("enter the testToHostChargeEnquiryNew");
		//CorpUser user = null;
		user = new CorpUser("10011");
		user.setUserId("1101");
		Map fromHost = new HashMap();
		TransferMacau transferMacau = null;
		
		try {
			System.err.println("enter the try");
			transferMacau = transferService.viewInMacau("TH8004220060826");
			//fromHost = transferService.toHostChargeEnquiry("2336",user, new BigDecimal(transferBank.getDebitAmount().toString()),"000","000","Y","000","000","s");
			//fromHost = transferService.toHostChargeEnquiry("TH8009220060826",user,"000","000","Y","000","000","s");
			fromHost = transferService.toHostChargeEnquiryNew("TH8004220060826",user, new BigDecimal(transferMacau.getTransferAmount().toString())," ","Y","001","002",transferMacau.getRecordType(),"000","Y");
		    System.err.println("COMM_AMT="+fromHost.get("COMM_AMT"));
	} catch (Exception e) {
		System.err.println("enter exception");
		e.printStackTrace();
		
	}
	}
	*/
   /*
	public void testToHostChargeEnquiry() {
		System.err.println("enter the testToHostChargeEnquiry");
		//CorpUser user = null;
		user = new CorpUser("10011");
		user.setUserId("1101");
		Map fromHost = new HashMap();
		TransferBank transferBank = null;
		
		try {
			System.err.println("enter the try");
			//corpUser.setCorpId("1001");
			//fromHost = transferService.toHostChargeEnquiry("2336",user, new BigDecimal(transferBank.getDebitAmount().toString()),"000","000","Y","000","000","s");
			fromHost = transferService.toHostChargeEnquiry("2336",user,"000","000","Y","000","000","s");		
		    System.err.println("COMM_AMT="+fromHost.get("COMM_AMT"));
	} catch (Exception e) {
		System.err.println("enter exception");
		e.printStackTrace();
		
	}
	}
	*/

	public void testloadUploadMacau() {
  	  Map fromhostMap = new HashMap();
  	  TransferMacau transferMacau = null;
  	  try {
  		transferMacau = transferService.viewInMacau("TH6608000620060930");
  		
  		  transferService.loadUploadMacau(transferMacau,fromhostMap);
  		  
  		  
  	  } catch (NTBException e){
  		  e.printStackTrace();
  	  }
      	}
  
	/*
	public void testloadUploadOversea() {
	  	  Map fromhostMap = new HashMap();
	  	 
	  	  TransferOversea transferOversea = null;
	  	  try {
	  		  transferOversea = transferService.viewInOversea("TH8010320060816");
	  		  transferService.loadUploadOversea(transferOversea,fromhostMap);
	  		  	  		  
	  	  } catch (NTBException e){
	  		  e.printStackTrace();
	  	  }
	      	}
	      	
	*/
	/*
	public void testuploadEnquiryOversea() {
		  Map fromhostMap = new HashMap();
	  	 
	  	  TransferOversea transferOversea = null;
	  	  try {
	  		  transferOversea = transferService.viewInOversea("TH8010320060816");
	  		  transferService.uploadEnquiryOversea(transferOversea,fromhostMap);
	  		  //.loadUploadOversea(transferOversea,fromhostMap);
	  		  	  		  
	  	  } catch (NTBException e){
	  		  e.printStackTrace();
	  	  }
		
	}
	*/
	/*
	public void testuploadEnquiryMacau() {
		Map fromhostMap = new HashMap();
	  	  TransferMacau transferMacau = null;
	  	  try {
	  		transferMacau = transferService.viewInMacau("TH8010820060814");
	  		
	  		  transferService.uploadEnquiryMacau(transferMacau,fromhostMap);
	  		  //.loadUploadMacau(transferMacau,fromhostMap);
	  		  
	  		  
	  	  } catch (NTBException e){
	  		  e.printStackTrace();
	  	  }	
		
	}
	*/
	/*
	public void testuploadEnquiryBANK() {
	  Map fromhostMap = new HashMap();
   	  TransferBank transferBank = null;
   	  try {
   		  transferBank = transferService.viewInBANK("TH8010320060815");
   		  transferService.uploadEnquiryBANK(transferBank,fromhostMap);
   		  // .loadUploadBANK(transferBank,fromhostMap);
   		  
   	  } catch (NTBException e){
   		  e.printStackTrace();
   	  }
		
	}
	*/
     /*
	public void testloadUploadBANK() {
            	  Map fromhostMap = new HashMap();
            	  TransferBank transferBank = null;
            	  try {
            		  transferBank = transferService.viewInBANK("TH8009220060826");
            		  transferService.loadUploadBANK(transferBank,fromhostMap);
            		  
            	  } catch (NTBException e){
            		  e.printStackTrace();
            	  }
                	}
                	*/
 
 /*
	public void testToHostInBANK() {
		System.out.println("enter the toHostInBANK");
		//CorpUser user =  ;
		user = new CorpUser("1101");
		user.setUserId("1101");
		//user.setCorpId("1001");
		//user.setUserId("10001");
		user.setCorpId("10001");
		Map fromHost = new HashMap();
		
		try {
			 System.err.println("enter the try");
			//fromHost = transferService.toHostChargeEnquiry("2336",corpUser, new BigDecimal(transferBank.getDebitAmount().toString()),"000","000","Y","000","000","s");	
			 fromHost = transferService.toHostInBANK("1949",user,Contants.);
		    ;
	} catch (NTBException e) {
		e.printStackTrace();
		//assertTrue(false);
	}
	}
*/
   
	/*
	public void testQueryTest() {
		System.err.println("enter the testQueryTest");
	    user =new CorpUser("1101");
	    user.setCorpId("10001");
		Map fromHost = new HashMap();
		TransferMacau transferMacau = null;
		
		try {
			transferMacau=transferService.viewInMacau("TH8005520060815");
			transferService.qureyTest(transferMacau);
		    
	} catch (NTBException e) {
		e.printStackTrace();
		assertTrue(false);
	}
	}
	
	*/

  /*
	public void testToHostInMacau() {
		System.err.println("enter the toHostInMacau");
	    user =new CorpUser("1101");
	    user.setCorpId("10001");
		Map fromHost = new HashMap();
		TransferBank transferBank = null;
		try {
			System.err.println("enter the try");
			fromHost = transferService.toHostInMacau("TH8008220060826",user);
			
		    
	} catch (NTBException e) {
		e.printStackTrace();
		assertTrue(false);
	}
	}

*/
	/*

	public void testToHostOverseas() {
		System.err.println("enter the testToHostOverseas");
		 user = new CorpUser("1101");
		 user.setCorpId("10001");
		Map fromHost = new HashMap();
		
		try {
			System.err.println("enter the try");
			fromHost = transferService.toHostOverseas("TH6603000520060916",user,"null");
			//fromHost = transferService.toHostInMacau("TH8010320060815",corpUser);
			
		    
	} catch (NTBException e) {
		e.printStackTrace();
		
	}
		
		
	}
	*/


	/*
	 * Test method for 'app.cib.service.txn.TransferServiceImpl.transferAccInBANK1to1(Object)'
	 */
   /*
	public void testTransferAccInBANK1to1() {
		 TransferBank transferBank = new TransferBank("2336");
		 transferBank.setCorpId("1001");
		 transferBank.setUserId("mxl");
		 transferBank.setFromAccount("123");
		 
		 try {
				transferService.transferAccInBANK1to1(transferBank);
			} catch (Exception e) {
				e.printStackTrace();
			}
		

	}
	*/
	
	
	/*
	public void testTransferHistoryInBANK() {
		//fail("Not yet implemented");
		System.out.println("Test history");
		System.err.println("Test history");
		BillPayment billPayment = null;
		TransferBank transferBank = null;
		try {
			//List list = billPaymentService.listHistory("2006-07-18 00:00:00", "2006-07-18 00:00:00", "CEM");
			List list = transferService.transferHistoryInBANK("2006-07-18 00:00:00","2006-07-30 00:00:00","10001","1101","9000010011");
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
	public void testTransferHistory() {
		//fail("Not yet implemented");
		System.out.println("Test history");
		System.err.println("Test history");
		BillPayment billPayment = null;
		TransferBank transferBank = null;
		TransferMacau transferMacau = null;
		try {
			//List list = billPaymentService.listHistory("TransferMacau","2006-07-18 00:00:00", "2006-07-18 00:00:00", "CEM");
			List list = transferService.transferHistory("1","2006-07-18 00:00:00","2006-08-31 00:00:00","10001","1101","9000010011");
			System.err.println("|testProcessPayment");
			System.err.println("|-> TransId" + "\t" + "PayType" + "\t" + "Status" + "\t" + "AuthStatus" + "\t" + "Merchant" + "\t" + "RequestTime");
			for (int i = 0; i<list.size(); i++){
				//billPayment = (BillPayment) list.get(i);
				transferMacau = (TransferMacau) list.get(i);
				System.err.println("|-> " + transferMacau.getTransId());
				//System.err.println("|-> " + billPayment.getTransId() + "\t" + billPayment.getPayType() + "\t" + billPayment.getStatus() + "\t" + billPayment.getAuthStatus() + "\t\t" + billPayment.getMerchant() + "\t\t" + billPayment.getRequestTime());
			}
		} catch (NTBException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	*/
	

	/*
	
	
	
	*/
	/*{
	 * 
	public void testToHostInBANK() {
		System.err.println("enter the testToHostInBank");
		CorpUser corpuser=null;
		Map fromHost = new HashMap();
		try{
			fromHost = transferService
		}
	}
	*/
	/*
	public void testTransferHistoryInMacau() {
		//fail("Not yet implemented");
		System.out.println("Test history");
		System.err.println("Test history");
		BillPayment billPayment = null;
		TransferMacau transferMacau = null;
		try {
			//List list = billPaymentService.listHistory("2006-07-18 00:00:00", "2006-07-18 00:00:00", "CEM");
			List list = transferService.transferHistoryInMacau("2006-07-18 00:00:00","2006-07-30 00:00:00","10001","1101","9000010011");
			System.err.println("|testProcessPayment");
			System.err.println("|-> TransId" + "\t" + "PayType" + "\t" + "Status" + "\t" + "AuthStatus" + "\t" + "Merchant" + "\t" + "RequestTime");
			for (int i = 0; i<list.size(); i++){
				//billPayment = (BillPayment) list.get(i);
				transferMacau = (TransferMacau) list.get(i);
				System.err.println("|-> " + transferMacau.getTransId());
				//System.err.println("|-> " + billPayment.getTransId() + "\t" + billPayment.getPayType() + "\t" + billPayment.getStatus() + "\t" + billPayment.getAuthStatus() + "\t\t" + billPayment.getMerchant() + "\t\t" + billPayment.getRequestTime());
			}
		} catch (NTBException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	*/

	/*
	 * Test method for 'app.cib.service.txn.TransferServiceImpl.transferAccMacau(Object)'
	 */
	/*
	public void testTransferAccMacau() {
		TransferMacau transferMacau = new TransferMacau("26");
		transferMacau.setCorpId("bbc");
		transferMacau.setCurreny("aac" );
		
		try {
			transferService.transferAccMacau(transferMacau);
		} catch (Exception e ){
			e.printStackTrace();
			
		}
		

	}
	*/
	/*
	 * Test method for 'app.cib.service.txn.TransferServiceImpl.transferAccOverseas(Object)'
	 */
	/*
	public void testTransferAccOverseas() {
		TransferOversea transferOversea = new TransferOversea("10");
		transferOversea.setCorpId("qwe");
		transferOversea.setBatchId("ddd");
		try {
			transferService.transferAccOverseas(transferOversea);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	*/

	/*
	 * Test method for 'app.cib.service.txn.TransferServiceImpl.transferHistoryInBANK(Map)'
	 */
	/*
	public void testTransferHistoryInBANK() {
		Map queryParam = new HashMap();
		queryParam.put("userId","mxl");
		queryParam.put("corpId","aaaa");
		List historyList;
		try {
			historyList =  transferService.transferHistoryInBANK(queryParam);
			for (int i = 0; i< historyList.size(); i++){
				//System.err.println("Merchant : " + ((BillPayment)historyList.get(i)).getMerchant());
				System.err.println("Trans_ID : " +((TransferBank)historyList.get(i)).getTransId());
				System.err.println("From_Account: " +((TransferBank)historyList.get(i)).getFromAccount());
				System.err.println("---------------------------------------------------------------");
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	*/

	/*
	 * Test method for 'app.cib.service.txn.TransferServiceImpl.transferHistoryInMacau(Map)'
	 */
	/*
	public void testTransferHistoryInMacau() {
		Map queryParam = new HashMap();
		//queryParam.put("userId","mxl");
		queryParam.put("corpId","bbc");
		List historyList;
		try {
			historyList = transferService.transferHistoryInMacau(queryParam);
			for(int i = 0; i< historyList.size(); i++) {
				System.err.println("Trans_ID : " + ((TransferMacau)historyList.get(i)).getTransId());
				System.err.println("_______________________________________________________________");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	

	}
	*/

	/*
	 * Test method for 'app.cib.service.txn.TransferServiceImpl.transferHistoryOverseas(Map)'
	 */
	/*
	public void testTransferHistoryOverseas() {
		Map queryParam = new HashMap();
		queryParam.put("corpId","qwe");
		List historyList;
		try {
			historyList = transferService.transferHistoryOverseas(queryParam);
			for(int i=0; i<historyList.size();i++) {
				System.err.println("Trans_ID : " + ((TransferOversea)historyList.get(i)).getTransId());
				System.err.println("_______________________________________________________________");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
*/
}
