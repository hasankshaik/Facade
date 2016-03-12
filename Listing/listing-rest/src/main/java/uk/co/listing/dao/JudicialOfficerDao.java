package uk.co.listing.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import uk.co.listing.domain.JudicialOfficer;

@Repository("JudicialOfficerDao")
public class JudicialOfficerDao extends GenericDao {
    private static final Logger LOGGER = Logger.getLogger(JudicialOfficerDao.class);
    @SuppressWarnings("unchecked")
    public JudicialOfficer findJudicialOfficerByName(final String name) {
        LOGGER.debug("findJudicialOfficerByName , "+ name);
        return getSingleResult((List<JudicialOfficer>) getHibernateTemplate().findByNamedQueryAndNamedParam("findJudicialOfficerByName", "name", name));
    }

    @SuppressWarnings("unchecked")
    public List<JudicialOfficer> findAll() {
        LOGGER.debug("findAll");
        final DetachedCriteria criteria = DetachedCriteria.forClass(JudicialOfficer.class);
        return (List<JudicialOfficer>) getHibernateTemplate().findByCriteria(criteria);
    }

}
