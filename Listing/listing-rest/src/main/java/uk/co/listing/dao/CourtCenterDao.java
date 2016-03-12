package uk.co.listing.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import uk.co.listing.domain.CourtCenter;

@Repository("CourtCenterDao")
public class CourtCenterDao extends GenericDao {
    private static final Logger LOGGER = Logger.getLogger(CourtCenterDao.class);
    @SuppressWarnings("unchecked")
    public List<CourtCenter> findAllCourtCenters() {
        LOGGER.debug("findAllCourtCenters");
        final DetachedCriteria criteria = DetachedCriteria.forClass(CourtCenter.class);
        return (List<CourtCenter>) getHibernateTemplate().findByCriteria(criteria);
    }

    /**
     * find the court centre by either name or code by passing in any or both of them
     * @param name
     * @param code
     * @return
     */
    @SuppressWarnings("unchecked")
    public CourtCenter findCourtCentreByNameOrCode(final String name, final Long code) {
        LOGGER.debug("findCourtCentreByNameOrCode"+name+","+code);
        final Disjunction or = Restrictions.disjunction();
        or.add(Restrictions.eq("centerName", name));
        or.add(Restrictions.eq("code", code));

        final DetachedCriteria criteria = DetachedCriteria.forClass(CourtCenter.class);
        criteria.add(or);

        return getSingleResult((List<CourtCenter>) getHibernateTemplate().findByCriteria(criteria));
    }

    public CourtCenter findCourtCentreByCode(final Long code) {
        return findCourtCentreByNameOrCode(null, code);
    }

    public CourtCenter findCourtCentreByName(final String name) {
        return findCourtCentreByNameOrCode(name, null);
    }
}