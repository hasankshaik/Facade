package uk.co.listing.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import uk.co.listing.domain.PersonInCase;

@Repository("PersonInCaseDao")
public class PersonInCaseDao extends GenericDao {
    private static final Logger LOGGER = Logger.getLogger(PersonInCaseDao.class);

    @SuppressWarnings("unchecked")
    public PersonInCase findPersonInCaseByCrestCaseNumberAndPersonName(final String crestCaseNumber, final String personName) {
        LOGGER.debug("find PersonInCase by crest case number = " + crestCaseNumber + " and personName = " + personName);
        return getSingleResult((List<PersonInCase>) getHibernateTemplate().findByNamedQuery("findPersonInCaseByCrestCaseNumberAndPersonName", crestCaseNumber, personName));
    }
}
