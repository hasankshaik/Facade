package uk.co.listing.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import uk.co.listing.domain.SittingTarget;

@Repository("SittingTargetDao")
public class SittingTargetDao extends GenericDao {
    private static final Logger LOGGER = Logger.getLogger(SittingTargetDao.class);

    public Long getAnnualSittingTarget(final String courtCenter, final String financialYear) {
        LOGGER.debug("getAnnualSittingTarget , " + courtCenter + "," + financialYear);
        final SittingTarget sittingTarget = findSittingTarget(courtCenter, financialYear);
        if (sittingTarget == null) {
            return 0L;
        }
        return sittingTarget.getSittings();
    }

    public SittingTarget findSittingTarget(final String courtCenter, final String financialYear) {
        LOGGER.debug("findSittingTarget , " + courtCenter + "," + financialYear);
        final DetachedCriteria criteria = DetachedCriteria.forClass(SittingTarget.class);
        criteria.add(Restrictions.eq("year", financialYear));
        criteria.createAlias("courtCenter", "cc");
        criteria.add(Restrictions.eq("cc.centerName", courtCenter));

        @SuppressWarnings("unchecked")
        final List<SittingTarget> targets = (List<SittingTarget>) getHibernateTemplate().findByCriteria(criteria);
        if ((targets == null) || targets.isEmpty()) {
            return null;
        }
        return targets.get(0);
    }
}
