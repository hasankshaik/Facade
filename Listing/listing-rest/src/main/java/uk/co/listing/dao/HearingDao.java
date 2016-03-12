package uk.co.listing.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import uk.co.listing.domain.Hearing;
import uk.co.listing.domain.constant.BookingStatusEnum;
import uk.co.listing.domain.constant.HearingStatusEnum;
import uk.co.listing.domain.constant.HearingType;

@Repository("HearingDao")
public class HearingDao extends GenericDao {
    private static final Logger LOGGER = Logger.getLogger(HearingDao.class);

    @SuppressWarnings("unchecked")
    public Hearing findHearingByKey(final String hearingKey) {
        LOGGER.debug("findHearingByKey , " + hearingKey);
        return getSingleResult((List<Hearing>) getHibernateTemplate().findByNamedQuery("findHearingByKey", StringUtils.trimToEmpty(hearingKey)));
    }

    @SuppressWarnings("unchecked")
    public List<Hearing> findHearingByProperties(final BookingStatusEnum bookingStatusEnum, final HearingType hearingType) {
        LOGGER.debug("findHearingByProperties , " + bookingStatusEnum.getDescription() + "," + hearingType.getDescription());
        final DetachedCriteria criteria = DetachedCriteria.forClass(Hearing.class);
        criteria.add(Restrictions.eq("bookingStatus", bookingStatusEnum));
        criteria.add(Restrictions.eq("hearingType", hearingType));
        criteria.add(Restrictions.eq("active", Boolean.TRUE));
        return (List<Hearing>) getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    public List<Hearing> findPcmHearingForTomorrow(final Date startDate, final HearingStatusEnum stauts) {
        LOGGER.debug("findPcmHearingForTomorrow , " + startDate + "," + stauts.getDescription());
        return (List<Hearing>) getHibernateTemplate().findByNamedQueryAndNamedParam("findPcmHearingForTomorrow", new String[] { "startDate", "stauts" }, new Object[] { startDate, stauts });
    }

    @SuppressWarnings("unchecked")
    public List<Hearing> findPcmHearingForToday(final Date startDate, final HearingStatusEnum stauts) {
        LOGGER.debug("findPcmHearingForToday , " + startDate + "," + stauts.getDescription());
        return (List<Hearing>) getHibernateTemplate().findByNamedQueryAndNamedParam("findPcmHearingForToday", new String[] { "startDate", "stauts" }, new Object[] { startDate, stauts });
    }

    @SuppressWarnings("unchecked")
    public List<Hearing> findPcmHearingForPast(final Date startDate, final HearingStatusEnum stauts) {
        LOGGER.debug("findPcmHearingForPast , " + startDate + "," + stauts.getDescription());
        return (List<Hearing>) getHibernateTemplate().findByNamedQueryAndNamedParam("findPcmHearingForPast", new String[] { "startDate", "stauts" }, new Object[] { startDate, stauts });
    }
}
