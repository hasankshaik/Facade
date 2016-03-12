package uk.co.listing.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository("PersonDao")
public class PersonDao extends GenericDao {
    private static final Logger LOGGER = Logger.getLogger(PersonDao.class);
    @SuppressWarnings("unchecked")
    public Long findAuditedPerson(final String personName) {
        LOGGER.debug("findAuditedPerson , " + personName );
        final Long result = getSingleResult((List<BigInteger>) getHibernateTemplate().findByNamedQuery("findAuditedPerson", StringUtils.trimToEmpty(personName))).longValue();
        return result;
    }
}
