package app.cib.test.host;

import java.util.*;
import com.neturbo.set.utils.*;
import com.neturbo.set.transaction.TransClient;
import com.neturbo.set.transaction.TransXMLFactory;
import app.cib.core.CibTransClient;
import app.cib.bo.sys.CorpUser;

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
public class TestZJ05 {
    public TestZJ05() {
    }

    public static void main(String[] args) {

        Map toHost = new HashMap();
        Map fromHost = new HashMap();
        TransXMLFactory.setTransXMLDir(
                "D:/workspace/BANK/WebContent/WEB-INF/transxml");        
        try {
            CibTransClient testClient = new CibTransClient("CIB", "ZJ05");

            toHost.put("effectiveDate", "20171215");


//            toHost = KeyNameUtils.mapCaseDiff2Dash(toHost);
//            testClient.setAlpha8(new CorpUser(),CibTransClient.TXNNATURE_TRANSFER,CibTransClient.ACCTYPE_3RD_PARTY,"TH8888000120060731");
            fromHost = testClient.doTransaction(toHost);

            System.out.println("FromHost:" + fromHost);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
