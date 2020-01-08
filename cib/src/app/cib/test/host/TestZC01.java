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
public class TestZC01 {
    public TestZC01() {
    }

    public static void main(String[] args) {

        Map toHost = new HashMap();
        Map fromHost = new HashMap();
        TransXMLFactory.setTransXMLDir(
                "E:/BANK_CIB/dev/WebContent/WEB-INF/transxml");
        try {
            CibTransClient testClient = new CibTransClient("CIB", "ZC01");

            /*
                        toHost.put("APPLICATION_CODE", "20");
                        toHost.put("NO_OF_ACCOUNTS", "2");

                        List accountList = new ArrayList();
                        Map accountItem = new HashMap();
                        accountItem.put("ACCOUNT_NO", "601428618102");
                        accountList.add(accountItem);

                        accountItem = new HashMap();
                        accountItem.put("ACCOUNT_NO", "601428618260");
                        accountList.add(accountItem);

                        toHost.put("TRANSACTIONS_TIME", "122011");
                        toHost.put("CORPORATION_ID", "6000120010");
                        toHost.put("EBANKING_ID", "1101");
                        toHost.put("LANGUAGE_CODE", "E");
                        toHost.put("TRANSACTION_NATURE", "1");
                        toHost.put("TRANSFER2ACC_TYPE", "1");
             toHost.put("LOCAL_TRANSACTION_REF", "TH8888000120060731");
             */

            toHost.put("applicationCode", "20");
            toHost.put("noOfAccounts", "2");

            List accountList = new ArrayList();
            Map accountItem = new HashMap();
            accountItem.put("accountNo", "601428618102");
            accountList.add(accountItem);
            accountItem = new HashMap();
            accountItem.put("accountNo", "601428618260");
            accountList.add(accountItem);
            toHost.put("accountList", accountList);

            /*
            toHost.put("transactionsTime", "122011");
            toHost.put("corporationId", "6000120010");
            toHost.put("ebankingId", "1101");
            toHost.put("languageCode", "E");
            toHost.put("transactionNature", "1");
            toHost.put("transfer2accType", "1");
            toHost.put("localTransactionRef", "TH8888000120060731");
            */

            toHost = KeyNameUtils.mapCaseDiff2Dash(toHost);
            testClient.setAlpha8(new CorpUser(),CibTransClient.TXNNATURE_TRANSFER,CibTransClient.ACCTYPE_3RD_PARTY,"TH8888000120060731");
            fromHost = testClient.doTransaction(toHost);

            System.out.println("FromHost:" + fromHost);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
