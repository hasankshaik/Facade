package uk.co.listing.service;

import static uk.co.listing.rest.message.MessageBundler.getMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.co.listing.dao.CourtCenterDao;
import uk.co.listing.dao.CourtDiaryDao;
import uk.co.listing.dao.CourtRoomDao;
import uk.co.listing.dao.CourtSessionDao;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.CourtDiary;
import uk.co.listing.domain.CourtRoom;
import uk.co.listing.domain.CourtSession;
import uk.co.listing.exceptions.CcsException;
import uk.co.listing.rest.response.CourtCenterWeb;
import uk.co.listing.rest.response.CourtRoomWeb;
import uk.co.listing.utils.DataTransformationHelper;
import uk.co.listing.utils.DateTimeUtils;

@Service("courtListingService")
@Transactional(readOnly = true)
public class CourtListingService implements ICourtListingService {

    private static final Logger LOGGER = Logger.getLogger(CourtListingService.class);

    private @Value("${logout.url}") String LOGOUT_URL;

    @Autowired
    private CourtCenterDao courtCenterDao;

    @Autowired
    private CourtRoomDao courtRoomDao;

    @Autowired
    private CourtSessionDao courtSessionDao;

    @Autowired
    CourtDiaryDao courtDiaryDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addCourtCentreAndDiary(final CourtCenterWeb courtCenterWeb) {
        final CourtCenter courtCentre = saveCourtCentre(courtCenterWeb);
        saveCourtDiary(courtCentre);
        LOGGER.info("Court centre added , '" + courtCenterWeb.getCourtCenterName() + "' with code '" + courtCenterWeb.getCode() + "'");
    }

    @Override
    public List<CourtCenterWeb> getListOfCourtCenters() {
        final List<CourtCenter> courtCenters = courtCenterDao.findAllCourtCenters();
        final List<CourtCenterWeb> listCourtCenterWeb = new ArrayList<CourtCenterWeb>();
        for (final CourtCenter courtCenter : courtCenters) {
            final CourtCenterWeb courtCenterWeb = new CourtCenterWeb();
            courtCenterWeb.setCourtCenterName(courtCenter.getCenterName());
            courtCenterWeb.setCode(courtCenter.getCode());
            listCourtCenterWeb.add(courtCenterWeb);
        }
        LOGGER.info("List of court centre's fetched");
        return listCourtCenterWeb;

    }

    @Override
    public List<CourtRoomWeb> getListOfCourtRooms(final String courtCenterName) {
        final List<CourtRoom> courtRooms = courtRoomDao.findCourtRoomsByCenterName(courtCenterName);
        LOGGER.info("List of court rooms fetched");
        return DataTransformationHelper.courtRoomsToWeb(courtRooms);
    }

    @Override
    public List<CourtRoomWeb> getListOfCourtRoomSitting(final String courtCenterName, final Date startDate, final Date endDate) {
        final List<CourtRoom> courtRooms = courtRoomDao.findCourtRoomsByCenterName(courtCenterName);
        final List<CourtRoomWeb> listCourtCenterWeb = new ArrayList<CourtRoomWeb>();
        for (final CourtRoom courtRoom : courtRooms) {
            final List<CourtSession> courtSessions = courtSessionDao.findOpenAndClosedSessionsForSittings(courtRoom.getId(), DateTimeUtils.getBeginningOfTheDay(startDate),
                    DateTimeUtils.getBeginningOfTheDay(endDate));
            listCourtCenterWeb.add(DataTransformationHelper.courtRoomAndSessionToWeb(courtRoom, courtSessions));
        }
        LOGGER.info("List of sitting's for court room fetched");
        return listCourtCenterWeb;
    }

    /*
     * return logout url based on environment
     *
     * @see uk.co.listing.service.ICourtListingService#getLogoutUrl()
     */
    @Override
    public String getLogoutUrl() {
        return LOGOUT_URL;
    }

    private CourtCenter saveCourtCentre(final CourtCenterWeb courtCenterWeb) {
        if (StringUtils.isBlank(courtCenterWeb.getCourtCenterName()) || (courtCenterWeb.getCode() == null)) {
            throw new CcsException(getMessage("COURT_CENTRE_NAME_AND_CODE_CAN_NOT_NULL"));
        }
        final CourtCenter findCourtCenter = courtCenterDao.findCourtCentreByNameOrCode(courtCenterWeb.getCourtCenterName(), courtCenterWeb.getCode());

        if (findCourtCenter != null) {
            throw new CcsException(getMessage("COURT_CENTRE_NAME_AND_CODE_EXSIST", new String[] { "${CENTRENAME}", "${CENTRECODE}" }, new String[] { courtCenterWeb.getCourtCenterName(),
                    courtCenterWeb.getCode().toString() }));
        }
        final CourtCenter courtCenter = new CourtCenter();
        courtCenter.setCenterName(courtCenterWeb.getCourtCenterName());
        courtCenter.setCode(courtCenterWeb.getCode());
        courtCenterDao.save(courtCenter);

        return courtCenter;
    }

    private void saveCourtDiary(final CourtCenter courtCentre) {
        final CourtDiary diary = new CourtDiary(courtCentre);
        courtDiaryDao.save(diary);
    }
}
