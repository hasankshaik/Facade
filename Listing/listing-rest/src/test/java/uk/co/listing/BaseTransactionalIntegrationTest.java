package uk.co.listing;

import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class for JUnit tests which are expected to clean up after themselves like Database tests and other integration tests. 
 * Extend from this to make use of auto rollback after the DB operation. 
 * @author rvinayak
 *
 */

@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=true)
@Transactional
public abstract class BaseTransactionalIntegrationTest extends BaseIntegrationTest {

}
