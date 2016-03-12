package uk.co.listing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class for JUnit tests which are expected to do end to end integration
 * tests (using real appContext configurations) This does not roll back
 * transactions Also you can add your other app-context and test specific
 * configuration files here in @ContextConfiguration
 * 
 * @author rvinayak
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public abstract class BaseIntegrationTest {
	static {
	    // test env variable would mean it would run against the test_listing_db, and will clean up DB after its done
		System.setProperty("env", "test");
	}

	public static Log log = LogFactory.getLog(BaseIntegrationTest.class);

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

}
