package app.cib.test.bnk;

import app.cib.bo.bnk.BankUser;
import app.cib.service.bnk.BankUserService;
import junit.framework.TestCase;
import com.neturbo.set.core.Config;
import org.springframework.context.ApplicationContext;

public class BankUserServiceImplTest extends TestCase {
    BankUserService bankUserService;
    public static void main(String[] args) {
    }

    protected void setUp() throws Exception {
        super.setUp();
        ApplicationContext appContext = Config.getAppContext();
        bankUserService = (BankUserService) appContext.getBean(
                "bankUserService");
    }

    /*
     * Test method for 'app.cib.service.bnk.BankUserServiceImpl.add(BankUser)'
     */
    public void testAdd() {
        BankUser bankUser = new BankUser("test002");
        bankUser.setUserName("Test rollback 2");
        bankUser.setEmail("test@sohu.com");

        try {
            bankUserService.add(bankUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
