package uk.co.listing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Base class for normal JUnit tests that are expected to be mocking objects. 
 * Extend from this to make use use of mocking framework and put extra common feature like logging etc here in this class 
 * @author rvinayak
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class BaseMockingUnitTest {
	
	protected static Log log = LogFactory.getLog(BaseMockingUnitTest.class);

	public BaseMockingUnitTest() {
		super();
		log.info("RUNNING TEST " + this.getClass().getName());
	}

}
