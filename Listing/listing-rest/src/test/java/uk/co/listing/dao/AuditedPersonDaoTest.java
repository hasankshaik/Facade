package uk.co.listing.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.listing.BaseIntegrationTest;
import uk.co.listing.domain.Person;

public class AuditedPersonDaoTest extends BaseIntegrationTest {

    @Autowired
    private PersonDao personDao;

    @Test
    public void createPerson() {
        final Person person = new Person();
        person.setPersonFullName("audited name");
        personDao.save(person);
        personDao.getHibernateTemplate().flush();

    }

    @Test
    public void afterCreateHearingHistoryTest() {
        final long size = personDao.findAuditedPerson("audited name");
        assertEquals(size, 1);
    }

}
