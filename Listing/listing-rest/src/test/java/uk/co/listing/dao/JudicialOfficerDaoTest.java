package uk.co.listing.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.listing.BaseTransactionalIntegrationTest;
import uk.co.listing.domain.JudicialOfficer;
import uk.co.listing.domain.constant.JudicialOfficerType;

public class JudicialOfficerDaoTest extends BaseTransactionalIntegrationTest {

    private static final String judgeName = "test-judge";

    @Autowired
    private JudicialOfficerDao judicialOfficerDao;

    @Test
    public void testFindJudicialOfficerByName() {
	assertTrue(judicialOfficerDao.findJudicialOfficerByName(judgeName) == null);
	judicialOfficerDao.save(new JudicialOfficer(judgeName, JudicialOfficerType.CIRCUIT, true));
	JudicialOfficer judicialOfficer = judicialOfficerDao.findJudicialOfficerByName(judgeName);
	assertTrue(judgeName.equals(judicialOfficer.getFullName()));
	assertTrue(JudicialOfficerType.CIRCUIT.equals(judicialOfficer.getJudicialOfficerType()));
    }

     @Test
    public void testFindAll() {
    	 int preSize = judicialOfficerDao.findAll().size();
    	 judicialOfficerDao.save(new JudicialOfficer(judgeName, JudicialOfficerType.CIRCUIT, true));
    	 int postSize = judicialOfficerDao.findAll().size();
    	 assertEquals(preSize + 1, postSize);
    }

}
