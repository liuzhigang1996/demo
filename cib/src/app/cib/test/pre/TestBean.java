/**
 * @author hjs
 * 2006-10-26
 */
package app.cib.test.pre;

import java.util.Map;

import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;

import app.cib.core.CibTransClient;

/**
 * @author hjs
 * 2006-10-26
 */
public class TestBean implements Runnable {
	
	private CibTransClient testClient = null;
	private Map toHost = null;
	private int times = 0;
	
	private TestBean(){
	}
	
	public TestBean(CibTransClient testClient, Map toHost, int times) {
		this.testClient = testClient;
		this.toHost = toHost;
		
		this.times = times;
	}

	public void run() {
		runTest(testClient, toHost, times);
	}
	
	private void runTest(CibTransClient testClient, Map toHost, int times) {
		
		for (int i=0; i<times; i++) {
			Log.info("testing...");
			try {
				testClient.doTransaction(toHost);
				
		        //如果交易不成功则报出主机错误
		        if(testClient.isSucceed()) {
		        	RunTest.addSucNums();
		        } else {
		        	RunTest.addErrNums();
		        }
			} catch (NTBException e) {
	        	RunTest.addErrNums();
			}
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestBean a = new TestBean(null, null, 1);
		Thread at = new Thread(a); 
		at.run();
	}

}
