package uk.co.listing.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import uk.co.listing.domain.CourtRoom;

@Repository("CourtRoomDao")
public class CourtRoomDao extends GenericDao {
    private static final Logger LOGGER = Logger.getLogger(CourtRoomDao.class);

    @SuppressWarnings("unchecked")
    public List<CourtRoom> findCourtRoomsByCenterName(final String courtCenterName) {
        LOGGER.debug("findCourtRoomsByCenterName " + courtCenterName);
        return (List<CourtRoom>) getHibernateTemplate().findByNamedQueryAndNamedParam("findCourtRoomsByCenterName", "centerName", courtCenterName);
    }

    @SuppressWarnings("unchecked")
    public CourtRoom findCourtRoomByName(final String roomName) {
        LOGGER.debug("findCourtRoomByName " + roomName);
        return getSingleResult((List<CourtRoom>) getHibernateTemplate().findByNamedQueryAndNamedParam("findCourtRoomsByName", "roomName", roomName));
    }

    @SuppressWarnings("unchecked")
    public CourtRoom findHearingCourtRoom(final String hearingKey) {
        LOGGER.debug("findHearingCourtRoom " + hearingKey);
        return getSingleResult((List<CourtRoom>) getHibernateTemplate().findByNamedQuery("findHearingCourtRoom", hearingKey));
    }
}