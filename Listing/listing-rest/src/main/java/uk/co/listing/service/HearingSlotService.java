package uk.co.listing.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.listing.dao.HearingDao;
import uk.co.listing.dao.SessionBlockDao;
import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.Hearing;
import uk.co.listing.domain.PersonInCase;
import uk.co.listing.domain.constant.HearingType;
import uk.co.listing.domain.constant.RoleInCase;
import uk.co.listing.domain.constant.SessionBlockType;
import uk.co.listing.rest.response.RoomDateForHearing;
import uk.co.listing.utils.DataTransformationHelper;
import uk.co.listing.utils.DateTimeUtils;

/**
 * Service to find the best possible hearing slots with or without overbooking flag
 * @author rvinayak
 *
 */
@Service("hearingSlotService")
@Transactional(readOnly = true)
public class HearingSlotService implements IHearingSlotService {

    private static final Logger LOGGER = Logger.getLogger(HearingSlotService.class);
    private @Value("${overbook.lookup.count}") Integer OVERBOOK_LOOKUP_COUNT;
    private @Value("${max.slot.lookup.months}") Integer SLOT_LOOKUP_MONTHS;

    private @Value("${max.slots.size}") Integer MAX_SLOTS_SIZE;

    @Autowired
    private SessionBlockDao sessionBlockDao;

    @Autowired
    private HearingDao hearingDao;

    /* (non-Javadoc)
     * @see uk.co.listing.service.IHearingSlotService#getHearingDateSlotList(java.lang.String, int, boolean)
     */
    @Override
    public List<RoomDateForHearing> getHearingDateSlotList(final String courtCenterName, final String hearingKey, final int hearingEstimate, final boolean overbook) {
        final Hearing hearing = hearingDao.findHearingByKey(hearingKey);
        final CaseRelated caseRelated = hearing.getCaseRelated();
        Date dateEnd = DateTimeUtils.getBeginningOfTheDay(getEndDateForSlot(hearing));
        Date dateStart = DateTimeUtils.getBeginningOfTheDay(getStartDateForSlot(hearing,dateEnd));
        if(dateEnd.before(dateStart)) {
        	dateEnd = DateTimeUtils.getPreviousFridayFromDate(DateUtils.addDays(dateStart,34));
            dateStart = DateTimeUtils.getBeginningOfTheDay(DateUtils.addDays(dateEnd,-26));
        }
        final boolean hasJudge = caseRelated.getJudge()!=null;
        // find all available session blocks in date range with Trial session blockType
        final List<Object[]> emptyBlockRoomDates = new ArrayList<Object[]>();
        if (overbook && !hasJudge) {
            emptyBlockRoomDates.addAll(sessionBlockDao.findAvailableSessionBlockDatesWithOverbook(courtCenterName, dateStart, dateEnd, SessionBlockType.TRIAL, OVERBOOK_LOOKUP_COUNT, caseRelated ));
        } else if (!overbook && !hasJudge) {
            emptyBlockRoomDates.addAll(sessionBlockDao.findAvailableSessionBlockDates(courtCenterName, dateStart, dateEnd, SessionBlockType.TRIAL, caseRelated ));
        } else if (overbook && hasJudge) {
            emptyBlockRoomDates.addAll(sessionBlockDao.findAvailableSessionBlockDatesWithOverbookAndJudge(courtCenterName, dateStart, dateEnd, SessionBlockType.TRIAL, OVERBOOK_LOOKUP_COUNT, caseRelated ));
        } else {//!overbook && hasJudge
            emptyBlockRoomDates.addAll(sessionBlockDao.findAvailableSessionBlockDatesWithJudge(courtCenterName, dateStart, dateEnd, SessionBlockType.TRIAL, caseRelated ));
        }
        final List<RoomDateForHearing> roomDates = transformObjectArrToRoomDate(emptyBlockRoomDates);
        final List<RoomDateForHearing> result= inferSlotsFromBlocks(roomDates, hearingEstimate, overbook);
        LOGGER.info("Slot found for listing the hearing");
        return result;
    }

    protected List<RoomDateForHearing> inferSlotsFromBlocks(final List<RoomDateForHearing> roomDates, final int hearingEstimate, final boolean overbook) {
        final List<RoomDateForHearing> slots = new ArrayList<RoomDateForHearing>();
        for (final RoomDateForHearing roomDateForHearing : roomDates) {
            // get a list of dates adding hearing estimate which can be later checked if they exist in the available session block dates
            final List<RoomDateForHearing> tempSlotOfRoomDates = createPossibleSlotOfRoomDates(roomDateForHearing, hearingEstimate);

            // check if all dates exist in the available session blocks, if they do add the first day as the opening slot after setting the overbook(hearings) count
            if (roomDates.containsAll(tempSlotOfRoomDates)) {
                final long hearingCount = inferHighestSlotHearing(roomDates, tempSlotOfRoomDates);
                if (overbook && (hearingCount == 0)) { // if overbook is true just don't add underbook slots (i.e. slots with 0 hearings)
                    continue;
                }
                roomDateForHearing.setHearingCount(hearingCount);
                slots.add(roomDateForHearing);
            }
        }
        Collections.sort(slots);
        LOGGER.debug("found " + slots.size() + " slot dates for hearing est: " + hearingEstimate + ", returning top "+ MAX_SLOTS_SIZE);
        return slots.subList(0, slots.size() <= MAX_SLOTS_SIZE ? slots.size() : MAX_SLOTS_SIZE);
    }

    /**
     * create possible slot dates upto hearing estimate excluding the weekends
     * @param roomDateForHearing
     * @param hearingEstimate
     * @return
     */
    private List<RoomDateForHearing> createPossibleSlotOfRoomDates(final RoomDateForHearing roomDateForHearing, final int hearingEstimate) {
        final List<RoomDateForHearing> roomDates = new ArrayList<RoomDateForHearing>();
        Date startDate = roomDateForHearing.getSlotDate();
        final String roomName = roomDateForHearing.getRoomName();
        final Long hearCount = roomDateForHearing.getHearingCount();

        while (roomDates.size() < hearingEstimate) {
            if (!DateTimeUtils.isWeekend(startDate)) {
                roomDates.add(new RoomDateForHearing(roomName, startDate, hearCount));
            }
            startDate = DateUtils.addDays(startDate, 1);
        }
        return roomDates;
    }

    /**
     * Methid to get the end of the slot window
     * @param hearing Hearing
     * @return the end of the slot
     */
    private Date getEndDateForSlot(final Hearing hearing) {
        final CaseRelated caseRelated = hearing.getCaseRelated();
        Date endDate = DataTransformationHelper.getHearingKPIDateForTrial(hearing);
        for (final PersonInCase defendant : caseRelated.getPersonInCase()) {
            if (defendant.getRoleInCase().equals(RoleInCase.DEFENDANT)) {
                if ((defendant.getCtlExpiresOn() != null) && defendant.getCtlExpiresOn().before(endDate)) {
                    endDate = defendant.getCtlExpiresOn();
                }
            }
        }

        return DateTimeUtils.getPreviousFridayFromDate(endDate);
    }

    /**
     * Method to get the beginning of the slot window
     * @param hearing Hearing
     * @return Beginning of the slot
     */
    private Date getStartDateForSlot(final Hearing hearing, final Date dateEnd) {
        final CaseRelated caseRelated = hearing.getCaseRelated();
        Date start = new Date();
        final Date endDate = DateUtils.addDays(dateEnd,-25);
        if(endDate.after(start)) {
            start = endDate;
        }
        for (final Hearing hearingFromCase : caseRelated.getHearings()) {
            if (hearingFromCase.getHearingType().equals(HearingType.PCM)) {
                if ((hearingFromCase.getStartDate() != null) && DateUtils.addDays(hearingFromCase.getStartDate(),1).after(start)) {
                    start = DateUtils.addDays(hearingFromCase.getStartDate(),1);
                }
            }
        }

        return start;
    }

    // and now find the highest degree of overbooked hearings in the slot
    // sadly not very efficient to loop over so many times, alas thats the requirement :(
    private long inferHighestSlotHearing(final List<RoomDateForHearing> roomDates, final List<RoomDateForHearing> testSlotOfRoomDates) {
        long highestHearing = 0;
        for (final RoomDateForHearing testSlot : testSlotOfRoomDates) {
            final Optional<RoomDateForHearing> findFirst = roomDates.stream().filter(o -> o.equals(testSlot)).findFirst();
            final Long hearingCount = findFirst.get().getHearingCount();
            highestHearing = hearingCount > highestHearing ? hearingCount : highestHearing;
        }
        return highestHearing;
    }

    // transform list of object arrays to list of RoomDateForHearing, obj[0] is the room name, obj[1] is the available block date, obj[2] being existingHearingCount
    private List<RoomDateForHearing> transformObjectArrToRoomDate(final List<Object[]> emptyBlockRoomDates) {
        final List<RoomDateForHearing> roomDates = new ArrayList<RoomDateForHearing>();
        LOGGER.info("empty session blocks count: " + emptyBlockRoomDates.size());
        for (final Object[] objects : emptyBlockRoomDates) {
            LOGGER.info("Room: " + (String) objects[0] + ", " + objects[1] + ", hearing-count: " + objects[2]);
            final RoomDateForHearing roomDateHearing = new RoomDateForHearing((String) objects[0], (Date) objects[1], new Long(objects[2].toString()));
            roomDates.add(roomDateHearing);
        }
        return roomDates;
    }
}
