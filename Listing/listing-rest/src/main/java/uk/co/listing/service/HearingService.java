package uk.co.listing.service;

import static uk.co.listing.rest.message.MessageBundler.getMessage;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.co.listing.dao.CaseRelatedDao;
import uk.co.listing.dao.CourtRoomDao;
import uk.co.listing.dao.HearingDao;
import uk.co.listing.dao.HearingInstanceDao;
import uk.co.listing.dao.SessionBlockDao;
import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.CourtRoom;
import uk.co.listing.domain.Hearing;
import uk.co.listing.domain.HearingInstance;
import uk.co.listing.domain.SessionBlock;
import uk.co.listing.domain.constant.BookingStatusEnum;
import uk.co.listing.domain.constant.BookingTypeEnum;
import uk.co.listing.domain.constant.HearingStatusEnum;
import uk.co.listing.domain.constant.HearingType;
import uk.co.listing.domain.constant.SessionBlockType;
import uk.co.listing.exceptions.CcsException;
import uk.co.listing.rest.response.HearingWeb;
import uk.co.listing.utils.DataTransformationHelper;
import uk.co.listing.utils.DateTimeUtils;

@Service("HearingService")
@Transactional(readOnly = true)
public class HearingService implements IHearingService {

    private static final Logger LOGGER = Logger.getLogger(HearingService.class);

    @Autowired
    private CourtRoomDao courtRoomDao;

    @Autowired
    private CaseRelatedDao caseRelatedDao;

    @Autowired
    private HearingDao hearingDao;

    @Autowired
    private SessionBlockDao sessionBlockDao;

    @Autowired
    private HearingInstanceDao hearingInstanceDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void autoScheduleHearing(final HearingWeb hearingWeb, final String courtCenterName) {
        final Hearing hearing = hearingDao.findHearingByKey(hearingWeb.getHearingKey());
        final Date startDateForSlot = DateTimeUtils.getBeginningOfTheDay(hearingWeb.getStartDateForSlot());
        final Date today = DateTimeUtils.getBeginningOfTheDay(new Date());
        if (!DateUtils.isSameDay(startDateForSlot, today) && startDateForSlot.before(today)) {
            return;
        }
        final List<SessionBlock> listSessionBlock = sessionBlockDao.findSessionBlocksByDateCourtCenterAndType(courtCenterName, SessionBlockType.PCM, startDateForSlot);
        SessionBlock sessionBlock = sessionBlockDao.getSingleResult(listSessionBlock);
        for (final SessionBlock block : listSessionBlock) {
            if ((block.getHearings() != null) && (sessionBlock.getHearings() != null) && (block.getHearings().size() < sessionBlock.getHearings().size())) {
                sessionBlock = block;
            }
        }

        if (sessionBlock != null) {
            final HearingInstance hearingInstance = new HearingInstance();
            hearingInstance.setHearing(hearing);
            hearingInstance.setBlock(sessionBlock);
            sessionBlock.getHearings().add(hearingInstance);
            hearingInstanceDao.save(hearingInstance);
            hearing.setBookingStatus(BookingStatusEnum.BOOKED);
            hearing.setBookingType(BookingTypeEnum.CONFIRMED);
            hearingDao.save(hearing);
        }
        LOGGER.info("Auto scheduling : Hearing has been listeed successfully");

    }

    @Override
    public HearingWeb getHearingDetail(final String hearingKey) {
        final Hearing hearing = hearingDao.findHearingByKey(hearingKey);
        return DataTransformationHelper.hearingToWeb(hearing);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<HearingWeb>> getPcmHearingForAction() {
        final Map<String, List<HearingWeb>> result = new HashMap<>();
        final Date date = new Date();
        result.put("future", DataTransformationHelper.hearingsToWeb(hearingDao.findPcmHearingForTomorrow(DateUtils.addDays(date, 1), HearingStatusEnum.PENDING)));
        result.put("current", DataTransformationHelper.hearingsToWeb(hearingDao.findPcmHearingForToday(date, HearingStatusEnum.COMPLETE)));
        result.put("past", DataTransformationHelper.hearingsToWeb(hearingDao.findPcmHearingForPast(date, HearingStatusEnum.COMPLETE)));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<HearingWeb> getUnlistedHearing() {
        final List<Hearing> hearings = hearingDao.findHearingByProperties(BookingStatusEnum.NOTBOOKED, HearingType.TRIAL);
        return DataTransformationHelper.hearingsToWeb(hearings);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public HearingWeb listHearing(final HearingWeb hearingWeb) {
        validateListHearing(hearingWeb);

        final Date startDate = DateUtils.truncate(hearingWeb.getStartDateForSlot(), Calendar.DATE);

        validateStartDate(startDate);
        final Hearing hearing = hearingDao.findHearingByKey(hearingWeb.getHearingKey());

        final List<SessionBlock> listSessonBlock = sessionBlockDao.findSessionBlocksOrSlotByDateAndRoom(hearingWeb.getCourtRoomName(),
                DateTimeUtils.getWorkingDaysFromStartDate(startDate, hearingWeb.getTrialEstimate().intValue()), SessionBlockType.TRIAL);

        if (CollectionUtils.isEmpty(listSessonBlock) || (listSessonBlock.size() != hearingWeb.getTrialEstimate())) {
            throw new CcsException(getMessage("NO_SLOT_AVAILABLE_TO_LIST_THIS_HEARING"));
        }
        for (final SessionBlock sessionBlock : listSessonBlock) {
            final HearingInstance hearingInstance = new HearingInstance();
            hearingInstance.setHearing(hearing);
            hearingInstance.setBlock(sessionBlock);
            hearingInstanceDao.save(hearingInstance);
        }
        hearing.setBookingType(BookingTypeEnum.valueOf(hearingWeb.getBookingType().toUpperCase()));
        hearing.setBookingStatus(BookingStatusEnum.BOOKED);
        hearing.setBookedCourtRoomName(hearingWeb.getCourtRoomName());
        hearing.setStartDate(startDate);
        hearing.setDaysEstimated(hearingWeb.getTrialEstimate());
        hearingDao.save(hearing);
        LOGGER.info("Hearing has been listed successfully");
        return DataTransformationHelper.hearingToWeb(hearing);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public HearingWeb reListingHearing(final HearingWeb hearingWeb) {
        validateReList(hearingWeb);

        final CourtRoom listedCourtRoom = courtRoomDao.findHearingCourtRoom(hearingWeb.getHearingKey());
        final Hearing listedHearing = hearingDao.findHearingByKey(hearingWeb.getHearingKey());

        if (!listedHearing.getStartDate().equals(hearingWeb.getStartDateForSlot()) || !listedHearing.getDaysEstimated().equals(hearingWeb.getTrialEstimate())
                || !hearingWeb.getCourtRoomName().equals(listedCourtRoom.getRoomName())) {
            LOGGER.debug("reListTheHearing");
            return reListTheHearing(hearingWeb, hearingWeb.getTrialEstimate().intValue());
        }
        LOGGER.info("Hearing has been relisted successfully");
        return hearingWeb;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public HearingWeb saveUpdateHearing(final HearingWeb hearingWeb) {
        Hearing hearing = hearingDao.findHearingByKey(hearingWeb.getHearingKey());
        if (hearing == null) {
            hearing = createNewHearing(hearingWeb);
        } else {
            updateTheHearing(hearingWeb, hearing);
        }
        hearingDao.save(hearing);
        final HearingWeb hearingWebResponse = DataTransformationHelper.hearingToWeb(hearing);
        hearingWebResponse.setSuccessMessage("Hearing saved");

        LOGGER.info("Hearing has been saved successfully");
        return hearingWebResponse;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void unListHearing(final String hearingKey, final boolean active) {
        final Hearing hearing = hearingDao.findHearingByKey(hearingKey);
        hearing.getHearingInstance().forEach(hearingInstance -> hearingInstanceDao.delete(hearingInstance));
        hearing.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing.setBookedCourtRoomName("");
        hearing.setStartDate(null);
        hearing.setBookingType(null);
        hearing.setActive(active);
        hearingDao.save(hearing);
        LOGGER.info("Hearing has been unlisted successfully");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updatePcmHearingStatus(final HearingWeb hearingWeb) {
        final Hearing hearing = hearingDao.findHearingByKey(hearingWeb.getHearingKey());
        if (hearing == null) {
            throw new CcsException(getMessage("THIS_HEARING_CANNOT_BE_UPDATED"));
        } else {
            hearing.setHearingStatus(HearingStatusEnum.getHearingStatus(hearingWeb.getHearingStatus()));
        }
        hearingDao.save(hearing);
        LOGGER.info("PCM Hearing has been updated successfully");
    }

    private Hearing createNewHearing(final HearingWeb hearingWeb) {
        final Hearing hearing = new Hearing();
        final CaseRelated caseRelated = caseRelatedDao.findCaseByCrestCaseNumber(hearingWeb.getHearingCase());
        if (caseRelated == null) {
            throw new CcsException(getMessage("HEARING_WITHOUT_CASE_RELATED"));
        }
        hearing.setCaseRelated(caseRelated);
        hearing.setName(hearingWeb.getHearingName());
        hearing.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        if (caseRelated.getEstimationUnit() != null) {
            hearing.setDaysEstimated(new Double(DateTimeUtils.fromDateUnitToDays(caseRelated.getTrialEstimate(), caseRelated.getEstimationUnit().getValue())));
        } else {
            hearing.setDaysEstimated(new Double(0));
        }

        if (StringUtils.isNotBlank(hearingWeb.getHearingType())) {
            hearing.setHearingType(HearingType.valueOf(hearingWeb.getHearingType()));
            hearing.setStartDate(hearingWeb.getStartDateForSlot());
        } else {
            hearing.setHearingType(HearingType.TRIAL);
        }
        if (StringUtils.isNotBlank(hearingWeb.getHearingNote())) {
            hearing.setHearingNote(hearingWeb.getHearingNote());
        }
        hearing.setHearingStatus(HearingStatusEnum.PENDING);
        hearing.setActive(hearingWeb.getActive());
        return hearing;
    }

    private HearingWeb reListTheHearing(final HearingWeb hearingWeb, final int noOfDays) {
        final Date startDate = DateUtils.truncate(hearingWeb.getStartDateForSlot(), Calendar.DATE);
        final List<SessionBlock> listSessonBlock = sessionBlockDao.findSessionBlocksOrSlotByDateAndRoom(hearingWeb.getCourtRoomName(), DateTimeUtils.getWorkingDaysFromStartDate(startDate, noOfDays),
                SessionBlockType.TRIAL);
        if (CollectionUtils.isEmpty(listSessonBlock) || (listSessonBlock.size() != noOfDays)) {
            throw new CcsException(getMessage("NO_SLOT_AVAILABLE_TO_LIST_THIS_HEARING"));
        }
        final Hearing hearing = hearingDao.findHearingByKey(hearingWeb.getHearingKey());
        hearing.getHearingInstance().forEach(hearingInstance -> hearingInstanceDao.delete(hearingInstance));
        for (final SessionBlock sessionBlock : listSessonBlock) {
            final HearingInstance hearingInstance = new HearingInstance();
            hearingInstance.setHearing(hearing);
            hearingInstance.setBlock(sessionBlock);
            hearingInstanceDao.save(hearingInstance);
        }
        hearing.setBookingType(BookingTypeEnum.valueOf(hearingWeb.getBookingType().toUpperCase()));
        hearing.setBookingStatus(BookingStatusEnum.BOOKED);
        hearing.setBookedCourtRoomName(hearingWeb.getCourtRoomName());
        hearing.setStartDate(startDate);
        hearing.setDaysEstimated(new Double(noOfDays));
        hearing.setHearingNote(hearingWeb.getHearingNote());
        hearingDao.save(hearing);
        return DataTransformationHelper.hearingToWeb(hearing);
    }

    private void updateHearingEstimate(final HearingWeb hearingWeb, final Hearing hearing) {
        if (hearingWeb.getTrialEstimate() != null) {
            if (hearingWeb.getTrialEstimate() >= 0) {
                hearing.setDaysEstimated(hearingWeb.getTrialEstimate());
            } else {
                throw new CcsException(getMessage("TRAIL_ESTIMATE_CANNOT_BE_NEGATIVE_NUMBER"));
            }
        }
    }

    private void updateTheHearing(final HearingWeb hearingWeb, final Hearing hearing) {
        if (StringUtils.isNotBlank(hearingWeb.getHearingType())) {
            hearing.setHearingType(HearingType.valueOf(hearingWeb.getHearingType().toUpperCase()));
        }
        if (StringUtils.isNotBlank(hearingWeb.getBookingStatus())) {
            hearing.setBookingStatus(BookingStatusEnum.getBookinsStatus(hearingWeb.getBookingStatus()));
        }
        if (StringUtils.isNotBlank(hearingWeb.getBookingType())) {
            hearing.setBookingType(BookingTypeEnum.valueOf(hearingWeb.getBookingType().toUpperCase()));
        }
        updateHearingEstimate(hearingWeb, hearing);
        if (StringUtils.isNotBlank(hearingWeb.getStartDate())) {
            hearing.setStartDate(DateTimeUtils.parseDate(hearingWeb.getStartDate()));
        }
        if (StringUtils.isNotBlank(hearingWeb.getHearingNote())) {
            if (hearingWeb.getHearingNote().length() > 50) {
                throw new CcsException(getMessage("PLEASE_ENTER_A_VALID_ANNOTATION"));
            }
            hearing.setHearingNote(hearingWeb.getHearingNote());
        }
        hearing.setActive(hearingWeb.getActive());
    }

    private void validateListHearing(final HearingWeb hearingWeb) {
        if (hearingWeb.getTrialEstimate() < 1) {
            throw new CcsException(getMessage("HEARING_HAS_NO_ESTIMATE"));
        }
        if (StringUtils.isBlank(hearingWeb.getBookingType()) || StringUtils.isBlank(hearingWeb.getCourtRoomName()) || (hearingWeb.getStartDateForSlot() == null)) {
            throw new CcsException(getMessage("THIS_HEARING_CANNOT_BE_LISTED_WITH_INVALID_INPUT"));
        }
        if (hearingWeb.getStartDateForSlot().before(DateUtils.truncate(new Date(), Calendar.DATE))) {
            throw new CcsException(getMessage("HEARING_CANNOT_BE_LISTED_IN_PAST"));
        }
    }

    private void validateReList(final HearingWeb hearingWeb) {
        if (hearingWeb.getStartDateForSlot().before(DateUtils.truncate(new Date(), Calendar.DATE))) {
            throw new CcsException(getMessage("HEARING_CANNOT_BE_LISTED_IN_PAST"));
        }
        final Date startDate = DateUtils.truncate(hearingWeb.getStartDateForSlot(), Calendar.DATE);
        validateStartDate(startDate);

        if (StringUtils.isBlank(hearingWeb.getBookingType()) || StringUtils.isBlank(hearingWeb.getCourtRoomName()) || (hearingWeb.getStartDateForSlot() == null) || (hearingWeb.getTrialEstimate() < 1)) {
            throw new CcsException(getMessage("SCHEDULED_LISTING_CANNOT_BE_UPDATED"));
        }
    }

    private void validateStartDate(final Date startDate) {
        if (DateTimeUtils.isWeekend(startDate)) {
            throw new CcsException(getMessage("START_DATE_IS_NOT_WORKING_DAY"));
        }
    }
}
