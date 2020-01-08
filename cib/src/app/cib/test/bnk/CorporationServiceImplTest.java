package app.cib.test.bnk;

import org.springframework.context.ApplicationContext;

import com.neturbo.set.core.Config;
import com.neturbo.set.exception.NTBException;

import app.cib.bo.bnk.Corporation;
import app.cib.service.bnk.CorporationService;
import app.cib.service.sys.CorpUserService;
import junit.framework.TestCase;

public class CorporationServiceImplTest extends TestCase {
	
	CorporationService corpService = null;

	protected void setUp() throws Exception {
		super.setUp();
		ApplicationContext appContext = Config.getAppContext();
		corpService = (CorporationService) appContext.getBean(
                "CorporationService");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'app.cib.service.bnk.CorporationServiceImpl.add(Corporation)'
	 */
	public void testAdd() {
		Corporation corpObj = new Corporation();
		corpObj.setCorpId("555555555");
		corpObj.setCorpName("bbbbbbbbbbbbb");
		try {
			corpService.add(corpObj);
			//assertEquals();
		} catch (NTBException e) {
			
			e.printStackTrace();
		}
		

	}

	/*
	 * Test method for 'app.cib.service.bnk.CorporationServiceImpl.remove(Corporation)'
	 */
	public void testRemove() {

	}

	/*
	 * Test method for 'app.cib.service.bnk.CorporationServiceImpl.update(Corporation)'
	 */
	public void testUpdate() {

	}

}
