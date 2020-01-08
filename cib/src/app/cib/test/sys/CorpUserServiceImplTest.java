package app.cib.test.sys;

import org.springframework.context.ApplicationContext;

import app.cib.bo.sys.CorpUser;
import app.cib.service.sys.CorpUserService;
import app.cib.service.sys.CorpUserServiceImpl;
import com.neturbo.set.core.Config;

import junit.framework.TestCase;
import app.cib.core.CibAction;

public class CorpUserServiceImplTest extends TestCase {

    CorpUserService corpUserService = null;

    protected void setUp() throws Exception {
        super.setUp();
        ApplicationContext appContext = Config.getAppContext();
        corpUserService = (CorpUserService) appContext.getBean(
                "corpUserService");
    }


    /*
     * Test method for 'app.cib.service.sys.CorpUserServiceImpl.list()'
     */
    public void testListUserbyCorp() {

    }

    /*
     * Test method for 'app.cib.service.sys.CorpUserServiceImpl.load(String)'
     */
    public void testLoad() {
        CorpUser user = null;
        try {
            user = corpUserService.load("1101");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertNotNull(user);
    }

    /*
     * Test method for 'app.cib.service.sys.CorpUserServiceImpl.add(AppUser)'
     */
    public void testAdd() {

    }

    /*
     * Test method for 'app.cib.service.sys.CorpUserServiceImpl.remove(AppUser)'
     */
    public void testRemove() {

    }

    /*
     * Test method for 'app.cib.service.sys.CorpUserServiceImpl.update(AppUser)'
     */
    public void testUpdate() {

    }

    /*
     * Test method for 'app.cib.service.sys.CorpUserServiceImpl.update(AppUser)'
     */
    public void testAuthenticate() {
        CorpUser user = null;
        try {
//            user = corpUserService.authenticate("1101", "111112",new CibAction());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertNotNull(user);
    }

}
