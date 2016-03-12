package uk.co.listing.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import uk.co.listing.domain.CourtSession;

@Repository("CourtSessionDao")
public class CourtSessionDao extends GenericDao {
    private static final Logger LOGGER = Logger.getLogger(CourtSessionDao.class);

    @SuppressWarnings("unchecked")
    public List<CourtSession> findOpenCourtRoomSittingByRoomNameBetweenDates(final Long courtRoomId, final Date startDate, final Date endDate) {
        LOGGER.debug("findOpenCourtRoomSittingByRoomNameBetweenDates ," + courtRoomId + "," + startDate + "," + endDate);
        return (List<CourtSession>) getHibernateTemplate().findByNamedQueryAndNamedParam("findSessionByCourtRoomAndSittingBetweenDates", new String[] { "startDate", "endDate", "courtRoomId" },
                new Object[] { startDate, endDate, courtRoomId });
    }

    @SuppressWarnings("unchecked")
    public List<CourtSession> findOpenAndClosedSessionsForSittings(final Long courtRoomId, final Date startDate, final Date endDate) {
        LOGGER.debug("findOpenAndClosedSessionsForSittings ," + courtRoomId + "," + startDate + "," + endDate);
        return (List<CourtSession>) getHibernateTemplate().findByNamedQueryAndNamedParam("findOpenAndClosedSitting", new String[] { "startDate", "endDate", "courtRoomId" },
                new Object[] { startDate, endDate, courtRoomId });
    }
}
