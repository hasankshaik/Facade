package uk.co.listing.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.SessionBlock;
import uk.co.listing.domain.constant.SessionBlockType;

@Repository("SessionBlockDao")
public class SessionBlockDao extends GenericDao {
    private static final Logger LOGGER = Logger.getLogger(SessionBlockDao.class);

    @SuppressWarnings("unchecked")
    public List<SessionBlock> findSessionBlocksByDateCourtCenterAndType(final String courtCenterName, final SessionBlockType sessionblockType, final Date sittingdate) {
        LOGGER.debug("findSessionBlocksByDateCourtCenterAndType , " + courtCenterName + "," + sessionblockType.getDescription() + "," + sittingdate);
        return (List<SessionBlock>) getHibernateTemplate().findByNamedQueryAndNamedParam("findSessionBlocksByDateCourtCenterAndType",
                new String[] { "courtCenterName", "sessionblockType", "sittingdate" }, new Object[] { courtCenterName, sessionblockType, sittingdate });
    }

    @SuppressWarnings("unchecked")
    public List<SessionBlock> findSessionBlocksOrSlotByDateAndRoom(final String courtRoomName, final List<Date> listDates, final SessionBlockType sessionblockType) {
        LOGGER.debug("findSessionBlocksOrSlotByDateAndRoom , " + courtRoomName + "," + sessionblockType.getDescription() + "," + listDates);
        return (List<SessionBlock>) getHibernateTemplate().findByNamedQueryAndNamedParam("findSessionBlocksByDateAndRoom", new String[] { "courtRoomName", "sittingdate", "sessionblockType" },
                new Object[] { courtRoomName, listDates, sessionblockType });
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> findAvailableSessionBlockDates(final String courtCenterName, final Date dateStart, final Date dateEnd, final SessionBlockType sessionBlockType, final CaseRelated caseRelated) {
        LOGGER.debug("findAvailableSessionBlockDates , " + courtCenterName + "," + sessionBlockType.getDescription() + "," + dateStart + "," + dateEnd);
        return (List<Object[]>) getHibernateTemplate().findByNamedQueryAndNamedParam("findAvailableSessionBlockDates",
                new String[] { "dateStart", "dateEnd", "blockType", "courtCenterName", "caseRelated" }, new Object[] { dateStart, dateEnd, sessionBlockType, courtCenterName, caseRelated });
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> findAvailableSessionBlockDatesWithOverbook(final String courtCenterName, final Date dateStart, final Date dateEnd, final SessionBlockType sessionBlockType,
            final int overbookCount, final CaseRelated caseRelated) {
        LOGGER.debug("findAvailableSessionBlockDatesWithOverbook , " + courtCenterName + "," + sessionBlockType.getDescription() + "," + dateStart + "," + dateEnd);
        return (List<Object[]>) getHibernateTemplate().findByNamedQueryAndNamedParam("findAvailableSessionBlockDatesWithOverbooking",
                new String[] { "dateStart", "dateEnd", "blockType", "overbookCount", "courtCenterName", "caseRelated" },
                new Object[] { dateStart, dateEnd, sessionBlockType, new Long(overbookCount), courtCenterName, caseRelated });
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> findAvailableSessionBlockDatesWithJudge(final String courtCenterName, final Date dateStart, final Date dateEnd, final SessionBlockType sessionBlockType,
            final CaseRelated caseRelated) {
        LOGGER.debug("findAvailableSessionBlockDatesWithJudge , " + courtCenterName + "," + sessionBlockType.getDescription() + "," + dateStart + "," + dateEnd);
        return (List<Object[]>) getHibernateTemplate().findByNamedQueryAndNamedParam("findAvailableSessionBlockDatesWithJudge",
                new String[] { "dateStart", "dateEnd", "blockType", "courtCenterName", "caseRelated", "judge" },
                new Object[] { dateStart, dateEnd, sessionBlockType, courtCenterName, caseRelated, caseRelated.getJudge() });
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> findAvailableSessionBlockDatesWithOverbookAndJudge(final String courtCenterName, final Date dateStart, final Date dateEnd, final SessionBlockType sessionBlockType,
            final int overbookCount, final CaseRelated caseRelated) {
        LOGGER.debug("findAvailableSessionBlockDatesWithOverbookAndJudge , " + courtCenterName + "," + sessionBlockType.getDescription() + "," + dateStart + "," + dateEnd);
        return (List<Object[]>) getHibernateTemplate().findByNamedQueryAndNamedParam("findAvailableSessionBlockDatesWithOverbookingAndJudge",
                new String[] { "dateStart", "dateEnd", "blockType", "overbookCount", "courtCenterName", "caseRelated", "judge" },
                new Object[] { dateStart, dateEnd, sessionBlockType, new Long(overbookCount), courtCenterName, caseRelated, caseRelated.getJudge() });
    }

}