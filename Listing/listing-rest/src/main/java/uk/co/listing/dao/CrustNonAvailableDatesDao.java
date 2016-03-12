package uk.co.listing.dao;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import uk.co.listing.domain.CrustNonAvailableDates;

@Repository("CrustNonAvailableDatesDao")
public class CrustNonAvailableDatesDao extends GenericDao {
    private static final Logger LOGGER = Logger.getLogger(CrustNonAvailableDatesDao.class);
    public CrustNonAvailableDates findCrestNonAvailableDatesByProperties(final CrustNonAvailableDates crestNonAvailableDates) {
        final DetachedCriteria criteria = DetachedCriteria.forClass(CrustNonAvailableDates.class);
        criteria.add(Restrictions.eq("caseRelated", crestNonAvailableDates.getCaseRelated()));
        criteria.add(Restrictions.eq("reason", crestNonAvailableDates.getReason()));
        criteria.add(Restrictions.eq("startDate", crestNonAvailableDates.getStartDate()));
        criteria.add(Restrictions.eq("endDate", crestNonAvailableDates.getEndDate()));
        LOGGER.info("findCrestNonAvailableDatesByProperties , "+ criteria.toString());
        return (CrustNonAvailableDates) getSingleResult(getHibernateTemplate().findByCriteria(criteria));
    }

    public CrustNonAvailableDates findById(final Long id) {
        return getHibernateTemplate().get(CrustNonAvailableDates.class, id);
    }

}
