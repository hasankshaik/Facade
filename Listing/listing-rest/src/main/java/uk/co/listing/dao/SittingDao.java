package uk.co.listing.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository("SittingDao")
public class SittingDao extends GenericDao {
    private static final Logger LOGGER = Logger.getLogger(SittingDao.class);

    public Long countActualSittingDaysBetweenDates(final String courtCenter, final Date startDate, final Date endDate) {

        LOGGER.debug("countActualSittingDaysBetweenDates , " + courtCenter + " BetweenDates " + startDate + " and " + endDate);
        @SuppressWarnings("unchecked")
        final  Long resultS = getSingleResult((List<BigInteger>) getHibernateTemplate().findByNamedQuery("countActualSittingDays", startDate, endDate, courtCenter)).longValue();
        return resultS;

    }

}
