/**
 * @author hjs
 * 2006-10-26
 */
package app.cib.test.pre;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;

import app.cib.bo.sys.CorpUser;
import app.cib.core.CibIdGenerator;
import app.cib.core.CibTransClient;

/**
 * @author hjs
 * 2006-10-26
 */
public class RunTest {

	private static long sucNums = 0;
	private static long errNums = 0;
	
	private CibTransClient testClient = null;
	private Map toHost = null;
	private int times = 0;
	private int threads = 0; 
	
	public static synchronized void addSucNums(){
		sucNums ++;
	}
	
	public static synchronized void addErrNums(){
		errNums ++;
	}
	
	private RunTest(){
	}
	
	public RunTest(CibTransClient testClient, Map toHost, int times) {
		this.testClient = testClient;
		this.toHost = toHost;
		this.times = times;
		this.threads = 1;
		
		sucNums = 0;
		errNums = 0;
	}
	
	public RunTest(CibTransClient testClient, Map toHost, int times, int threads) {
		this.testClient = testClient;
		this.toHost = toHost;
		this.times = times;
		this.threads = threads;
		
		sucNums = 0;
		errNums = 0;
		
	}
	
	public void run() throws InterruptedException {
		int i = 0;
		Thread testBean = null;
		if (times>0 && threads>0) {
			while(i < threads){
				testBean = new Thread(new TestBean(testClient, toHost, times)); 
				testBean.start();
				i++;
			}
		}
		
		if (testBean!=null) {
			while (true){
				if (testBean.isAlive()) {
					Thread.sleep(500);
				} else {
					Log.info("success nums: " + sucNums);
					Log.info("error nums: " + errNums);
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter("c:/test_result"));
						bw.write("success nums: " + sucNums);
						bw.newLine();
						bw.write("error nums: " + errNums);
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}
	
	public static void main(String[] args){
		Config.setAppRoot("D:\\Java_IDE\\eclipse-SDK-3.2-win32\\workspace\\BANKCIB\\WebContent");
		CorpUser corpUser = new CorpUser("1101");
		corpUser.setCorpId("10001");
		
		Map toHost = new HashMap();
        CibTransClient testClient;
		try {
			testClient = new CibTransClient("CIB", "ZJ39");

	        toHost.put("CIF_NO", "0");
	        toHost.put("BILL_NO_1", "0");
	        toHost.put("BILL_NO_2", "0");
	        String refNo = CibIdGenerator.getRefNoForTransaction();
	        testClient.setAlpha8(corpUser, CibTransClient.TXNNATURE_BILL_PAYMENT, CibTransClient.ACCTYPE_3RD_PARTY, refNo);
	        
	        /*
	         * 5: ��һ�߳��ﴮ�еĴ���
	         * 18 : ���̲߳�����
	         */
	        RunTest test = new RunTest(testClient, toHost, 5, 18);
	        test.run();
	        
		} catch (NTBException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
	}

}
