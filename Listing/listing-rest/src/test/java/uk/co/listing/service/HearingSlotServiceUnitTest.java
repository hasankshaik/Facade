package uk.co.listing.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import uk.co.listing.BaseMockingUnitTest;
import uk.co.listing.dao.HearingDao;
import uk.co.listing.dao.SessionBlockDao;
import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.Hearing;
import uk.co.listing.domain.JudicialOfficer;
import uk.co.listing.domain.PersonInCase;
import uk.co.listing.domain.constant.HearingType;
import uk.co.listing.domain.constant.JudicialOfficerType;
import uk.co.listing.domain.constant.RoleInCase;
import uk.co.listing.domain.constant.SessionBlockType;
import uk.co.listing.rest.response.RoomDateForHearing;
import uk.co.listing.utils.DateTimeUtils;

/**
 * These are real integration tests and not the mocked ones
 *
 * @author rvinayak
 *
 */
public class HearingSlotServiceUnitTest extends BaseMockingUnitTest {

    @InjectMocks
    private HearingSlotService hearingSlotService;

    @Mock
    private HearingDao hearingDaoMock;

    @Mock
    private SessionBlockDao sessionBlockDaoMock;

    @Before
    public void before() {
        ReflectionTestUtils.setField(hearingSlotService, "MAX_SLOTS_SIZE", 10);
        ReflectionTestUtils.setField(hearingSlotService, "OVERBOOK_LOOKUP_COUNT", 10);
    }

    /**
     * for block dates in June 2015 01, 02, 03, 04 and hearing 4, should return
     * 01 as slot
     */
    @Test
    public void shouldGetSlotsFromBlockOfDates_forHearingEstimate() {
        final List<RoomDateForHearing> emptyBlockDates = buildDatesForDaysInJune2015(new Long[] { 1L, 2L, 3L, 4L });
        final List<RoomDateForHearing> slotDates = hearingSlotService.inferSlotsFromBlocks(emptyBlockDates, 4, false);

        assertNotNull(slotDates);
        assertEquals(1, slotDates.size());
        assertEquals(1, DateTimeUtils.getDayOfMonth(slotDates.get(0).getSlotDate()));
        assertEquals("room-name", slotDates.get(0).getRoomName());
        assertEquals(0, slotDates.get(0).getHearingCount().intValue());
    }

    /**
     * for block dates in June 2015 01, 02, 03, 04, 05 and hearing 4, should
     * return 01, 02 as slot
     */
    @Test
    public void shouldGetTwoSlotsFromBlockOfDates_forHearingEstimate() {
        final List<RoomDateForHearing> emptyBlockDates = buildDatesForDaysInJune2015(new Long[] { 1L, 2L, 3L, 4L, 5L });
        final List<RoomDateForHearing> slotDates = hearingSlotService.inferSlotsFromBlocks(emptyBlockDates, 4, false);

        assertNotNull(slotDates);
        assertEquals(2, slotDates.size());
        assertEquals(1, DateTimeUtils.getDayOfMonth(slotDates.get(0).getSlotDate()));
        assertEquals(2, DateTimeUtils.getDayOfMonth(slotDates.get(1).getSlotDate()));
        assertEquals(0, slotDates.get(0).getHearingCount().intValue());

    }

    /**
     * for block dates in June 2015 01, 02, 03, 04, 05, 08, 09, 10, 11 and
     * hearing 4, should return 01, 02, 03, 04, 05, 06 as slot
     */
    @Test
    public void shouldGetThreeSlotsOverTwoWeeks_FromBlockOfDates_forHearingEstimate() {
        final List<RoomDateForHearing> emptyBlockDates = buildDatesForDaysInJune2015(new Long[] { 1L, 2L, 3L, 4L, 5L, 8L, 9L, 10L, 11L });
        final List<RoomDateForHearing> slotDates = hearingSlotService.inferSlotsFromBlocks(emptyBlockDates, 4, false);

        assertNotNull(slotDates);
        assertEquals(6, slotDates.size());
        assertEquals(1, DateTimeUtils.getDayOfMonth(slotDates.get(0).getSlotDate()));
        assertEquals(2, DateTimeUtils.getDayOfMonth(slotDates.get(1).getSlotDate()));
        assertEquals(3, DateTimeUtils.getDayOfMonth(slotDates.get(2).getSlotDate()));
        assertEquals(4, DateTimeUtils.getDayOfMonth(slotDates.get(3).getSlotDate()));
        assertEquals(5, DateTimeUtils.getDayOfMonth(slotDates.get(4).getSlotDate()));
        assertEquals(8, DateTimeUtils.getDayOfMonth(slotDates.get(5).getSlotDate()));
        assertEquals(0, slotDates.get(0).getHearingCount().intValue());
        assertEquals(0, slotDates.get(1).getHearingCount().intValue());
        assertEquals(0, slotDates.get(2).getHearingCount().intValue());
        assertEquals(0, slotDates.get(3).getHearingCount().intValue());
        assertEquals(0, slotDates.get(4).getHearingCount().intValue());
        assertEquals(0, slotDates.get(5).getHearingCount().intValue());
    }

    /**
     * for block dates in June 2015 01, 02, 03, 04, 09, 10, 11, 12 and hearing
     * 4, should return 01, 09 as slot
     */
    @Test
    public void shouldGetTwoSlotsOverTwoWeeks_1() {
        final List<RoomDateForHearing> emptyBlockDates = buildDatesForDaysInJune2015(new Long[] { 1L, 2L, 3L, 4L, 9L, 10L, 11L, 12L });
        final List<RoomDateForHearing> slotDates = hearingSlotService.inferSlotsFromBlocks(emptyBlockDates, 4, false);

        assertNotNull(slotDates);
        assertEquals(2, slotDates.size());
        assertEquals(1, DateTimeUtils.getDayOfMonth(slotDates.get(0).getSlotDate()));
        assertEquals(9, DateTimeUtils.getDayOfMonth(slotDates.get(1).getSlotDate()));
        assertEquals(0, slotDates.get(0).getHearingCount().intValue());
        assertEquals(0, slotDates.get(1).getHearingCount().intValue());
    }

    /**
     * for block dates in June 2015 01, 02, 03, 04, 05, 10, 11, 12 and hearing
     * 4, should return 01, 02, 09 as slot
     */
    @Test
    public void shouldGetTwoSlotsOverTwoWeeks_2() {
        final List<RoomDateForHearing> emptyBlockDates = buildDatesForDaysInJune2015(new Long[] { 1L, 2L, 3L, 4L, 5L, 9L, 10L, 11L, 12L });
        final List<RoomDateForHearing> slotDates = hearingSlotService.inferSlotsFromBlocks(emptyBlockDates, 4, false);

        assertNotNull(slotDates);
        assertEquals(3, slotDates.size());
        assertEquals(1, DateTimeUtils.getDayOfMonth(slotDates.get(0).getSlotDate()));
        assertEquals(2, DateTimeUtils.getDayOfMonth(slotDates.get(1).getSlotDate()));
        assertEquals(9, DateTimeUtils.getDayOfMonth(slotDates.get(2).getSlotDate()));
        assertEquals(0, slotDates.get(0).getHearingCount().intValue());
        assertEquals(0, slotDates.get(1).getHearingCount().intValue());
        assertEquals(0, slotDates.get(2).getHearingCount().intValue());

    }

    /**
     * for block dates in June 2015 01, 02 and hearing size 1, should return only overbooked and not underbooked(0 hearing count)
     */
    @Test
    public void shouldGetOnlyOverbookedSlotWith_overbook_true() {
        final List<RoomDateForHearing> emptyBlockDates = new ArrayList<RoomDateForHearing>();
        final RoomDateForHearing roomDate_0 = new RoomDateForHearing("room-name", new GregorianCalendar(2015, Calendar.JUNE, 1).getTime(), 0L);
        final RoomDateForHearing roomDate_1 = new RoomDateForHearing("room-name", new GregorianCalendar(2015, Calendar.JUNE, 2).getTime(), 1L);
        emptyBlockDates.add(roomDate_0);
        emptyBlockDates.add(roomDate_1);

        final List<RoomDateForHearing> slotDates = hearingSlotService.inferSlotsFromBlocks(emptyBlockDates, 1, true);

        assertNotNull(slotDates);
        assertEquals(1, slotDates.size());
        assertEquals(2, DateTimeUtils.getDayOfMonth(slotDates.get(0).getSlotDate()));
        assertEquals(1, slotDates.get(0).getHearingCount().intValue());
    }

    /**
     * for block dates in June 2015 1,2,3,4,5,8,9,10,11,12,15,16,17,18,19 and
     * hearing 3, should return maximum 10 slots
     */
    @Test
    public void shouldGetMaximumTenSlots() {
        final List<RoomDateForHearing> emptyBlockDates = buildDatesForDaysInJune2015(new Long[] { 1L, 2L, 3L, 4L, 5L, 8L, 9L, 10L, 11L, 12L, 15L, 16L, 17L, 18L, 19L });
        final List<RoomDateForHearing> slotDates = hearingSlotService.inferSlotsFromBlocks(emptyBlockDates, 2, false);

        assertNotNull(slotDates);
        assertEquals(10, slotDates.size());
    }

    @Test
    public void shouldReturnHearingCountForEntireSlot() {
        final List<RoomDateForHearing> dates = new ArrayList<RoomDateForHearing>();

        GregorianCalendar gc = new GregorianCalendar(2015, Calendar.JUNE, 1);
        dates.add(new RoomDateForHearing("room-name", gc.getTime(), 0L));

        gc = new GregorianCalendar(2015, Calendar.JUNE, 2);
        dates.add(new RoomDateForHearing("room-name", gc.getTime(), 1L));

        gc = new GregorianCalendar(2015, Calendar.JUNE, 3);
        dates.add(new RoomDateForHearing("room-name", gc.getTime(), 2L));

        final List<RoomDateForHearing> slotDates = hearingSlotService.inferSlotsFromBlocks(dates, 2, false);

        assertEquals(2, slotDates.size());
        assertEquals(1, DateTimeUtils.getDayOfMonth(slotDates.get(0).getSlotDate()));
        assertEquals(2, DateTimeUtils.getDayOfMonth(slotDates.get(1).getSlotDate()));
        assertEquals(1, slotDates.get(0).getHearingCount().intValue());
        assertEquals(2, slotDates.get(1).getHearingCount().intValue());
    }

    @Test
    public void shouldGetHearingForSlot() {
        final String center = "Birmingham";
        final Date today = new Date();
        final Hearing hearing = new Hearing();
        hearing.setHearingType(HearingType.TRIAL);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setDateOfSending(today);
        caseRelated.getHearings().add(hearing);

        final PersonInCase defendant1 = new PersonInCase();
        defendant1.setRoleInCase(RoleInCase.DEFENDANT);
        defendant1.setCtlExpiresOn(DateUtils.addDays(today, 15));

        final PersonInCase defendant2 = new PersonInCase();
        defendant2.setRoleInCase(RoleInCase.DEFENDANT);
        defendant2.setCtlExpiresOn(DateUtils.addDays(today, 10));
        caseRelated.getPersonInCase().add(defendant1);
        caseRelated.getPersonInCase().add(defendant2);

        final Hearing hearing1 = new Hearing();
        hearing1.setHearingType(HearingType.PCM);
        hearing1.setStartDate(DateUtils.addDays(today,2));
        final Hearing hearing2 = new Hearing();
        hearing2.setHearingType(HearingType.PCM);
        hearing2.setStartDate(DateUtils.addDays(today,3));
        caseRelated.getHearings().add(hearing1);
        caseRelated.getHearings().add(hearing2);

        hearing.setCaseRelated(caseRelated);
        final List<Object[]> emptyBlockRoomDates = new ArrayList<Object[]>();
        final String room1 = "Room1";
        final Date date1 = DateUtils.addDays(today,7);
        final Long count1 = 10L;
        final Object[] object1 = new Object[3];
        object1[0] = room1;
        object1[1] = date1;
        object1[2] = count1;
        emptyBlockRoomDates.add(object1);
        final String room2 = "Room2";
        final Date date2 = DateUtils.addDays(today,14);
        final Long count2 = 20L;
        final Object[] object2 = new Object[3];
        object2[0] = room2;
        object2[1] = date2;
        object2[2] = count2;
        emptyBlockRoomDates.add(object2);

        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing);
        when(sessionBlockDaoMock.findAvailableSessionBlockDates(Mockito.anyString(),Mockito.any(Date.class),Mockito.any(Date.class),Mockito.any(SessionBlockType.class), Mockito.any(CaseRelated.class))).thenReturn(emptyBlockRoomDates);

        final List<RoomDateForHearing> roomDates = hearingSlotService.getHearingDateSlotList(center, room1, 1, false);
        assertNotNull(roomDates);
        assertEquals(2, roomDates.size());
    }

    @Test
    public void shouldGetHearingForSlotEndDateAfterStartDate() {
        final String center = "Birmingham";
        final Date today = new Date();
        final Hearing hearing = new Hearing();
        hearing.setHearingType(HearingType.TRIAL);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setDateOfSending(today);
        caseRelated.getHearings().add(hearing);

        final PersonInCase defendant1 = new PersonInCase();
        defendant1.setRoleInCase(RoleInCase.DEFENDANT);
        defendant1.setCtlExpiresOn(DateUtils.addDays(today, 35));

        final PersonInCase defendant2 = new PersonInCase();
        defendant2.setRoleInCase(RoleInCase.DEFENDANT);
        defendant2.setCtlExpiresOn(DateUtils.addDays(today, 42));
        caseRelated.getPersonInCase().add(defendant1);
        caseRelated.getPersonInCase().add(defendant2);

        final Hearing hearing1 = new Hearing();
        hearing1.setHearingType(HearingType.PCM);
        hearing1.setStartDate(DateUtils.addDays(today,28));
        final Hearing hearing2 = new Hearing();
        hearing2.setHearingType(HearingType.PCM);
        hearing2.setStartDate(DateUtils.addDays(today,35));
        caseRelated.getHearings().add(hearing1);
        caseRelated.getHearings().add(hearing2);

        hearing.setCaseRelated(caseRelated);
        final List<Object[]> emptyBlockRoomDates = new ArrayList<Object[]>();
        final String room1 = "Room1";
        final Date date1 = DateUtils.addDays(today,7);
        final Long count1 = 10L;
        final Object[] object1 = new Object[3];
        object1[0] = room1;
        object1[1] = date1;
        object1[2] = count1;
        emptyBlockRoomDates.add(object1);
        final String room2 = "Room2";
        final Date date2 = DateUtils.addDays(today,14);
        final Long count2 = 20L;
        final Object[] object2 = new Object[3];
        object2[0] = room2;
        object2[1] = date2;
        object2[2] = count2;
        emptyBlockRoomDates.add(object2);

        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing);
        when(sessionBlockDaoMock.findAvailableSessionBlockDates(Mockito.anyString(),Mockito.any(Date.class),Mockito.any(Date.class),Mockito.any(SessionBlockType.class), Mockito.any(CaseRelated.class))).thenReturn(emptyBlockRoomDates);

        final List<RoomDateForHearing> roomDates = hearingSlotService.getHearingDateSlotList(center, room1, 1, false);
        assertNotNull(roomDates);
        assertEquals(2, roomDates.size());
    }
    

    @Test
    public void shouldGetHearingForSlotWithJudge() {
        final String center = "Birmingham";
        JudicialOfficer judge = new JudicialOfficer("Jueza Alaya", JudicialOfficerType.CIRCUIT, true);
        final Date today = new Date();
        final Hearing hearing = new Hearing();
        hearing.setHearingType(HearingType.TRIAL);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setDateOfSending(today);
        caseRelated.getHearings().add(hearing);
        caseRelated.setJudge(judge);

        final PersonInCase defendant1 = new PersonInCase();
        defendant1.setRoleInCase(RoleInCase.DEFENDANT);
        defendant1.setCtlExpiresOn(DateUtils.addDays(today, 15));

        final PersonInCase defendant2 = new PersonInCase();
        defendant2.setRoleInCase(RoleInCase.DEFENDANT);
        defendant2.setCtlExpiresOn(DateUtils.addDays(today, 10));
        caseRelated.getPersonInCase().add(defendant1);
        caseRelated.getPersonInCase().add(defendant2);

        final Hearing hearing1 = new Hearing();
        hearing1.setHearingType(HearingType.PCM);
        hearing1.setStartDate(DateUtils.addDays(today,2));
        final Hearing hearing2 = new Hearing();
        hearing2.setHearingType(HearingType.PCM);
        hearing2.setStartDate(DateUtils.addDays(today,3));
        caseRelated.getHearings().add(hearing1);
        caseRelated.getHearings().add(hearing2);

        hearing.setCaseRelated(caseRelated);
        final List<Object[]> emptyBlockRoomDates = new ArrayList<Object[]>();
        final String room1 = "Room1";
        final Date date1 = DateUtils.addDays(today,7);
        final Long count1 = 10L;
        final Object[] object1 = new Object[3];
        object1[0] = room1;
        object1[1] = date1;
        object1[2] = count1;
        emptyBlockRoomDates.add(object1);
        final String room2 = "Room2";
        final Date date2 = DateUtils.addDays(today,14);
        final Long count2 = 20L;
        final Object[] object2 = new Object[3];
        object2[0] = room2;
        object2[1] = date2;
        object2[2] = count2;
        emptyBlockRoomDates.add(object2);

        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing);
        when(sessionBlockDaoMock.findAvailableSessionBlockDatesWithJudge(Mockito.anyString(),Mockito.any(Date.class),Mockito.any(Date.class),Mockito.any(SessionBlockType.class), Mockito.any(CaseRelated.class))).thenReturn(emptyBlockRoomDates);

        final List<RoomDateForHearing> roomDates = hearingSlotService.getHearingDateSlotList(center, room1, 1, false);
        assertNotNull(roomDates);
        assertEquals(2, roomDates.size());
    }
    
    @Test
    public void shouldGetHearingForSlotWithJudgeAndOverbooking() {
        final String center = "Birmingham";
        JudicialOfficer judge = new JudicialOfficer("Jueza Alaya", JudicialOfficerType.CIRCUIT, true);
        final Date today = new Date();
        final Hearing hearing = new Hearing();
        hearing.setHearingType(HearingType.TRIAL);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setDateOfSending(today);
        caseRelated.getHearings().add(hearing);
        caseRelated.setJudge(judge);

        final PersonInCase defendant1 = new PersonInCase();
        defendant1.setRoleInCase(RoleInCase.DEFENDANT);
        defendant1.setCtlExpiresOn(DateUtils.addDays(today, 15));

        final PersonInCase defendant2 = new PersonInCase();
        defendant2.setRoleInCase(RoleInCase.DEFENDANT);
        defendant2.setCtlExpiresOn(DateUtils.addDays(today, 10));
        caseRelated.getPersonInCase().add(defendant1);
        caseRelated.getPersonInCase().add(defendant2);

        final Hearing hearing1 = new Hearing();
        hearing1.setHearingType(HearingType.PCM);
        hearing1.setStartDate(DateUtils.addDays(today,2));
        final Hearing hearing2 = new Hearing();
        hearing2.setHearingType(HearingType.PCM);
        hearing2.setStartDate(DateUtils.addDays(today,3));
        caseRelated.getHearings().add(hearing1);
        caseRelated.getHearings().add(hearing2);

        hearing.setCaseRelated(caseRelated);
        final List<Object[]> emptyBlockRoomDates = new ArrayList<Object[]>();
        final String room1 = "Room1";
        final Date date1 = DateUtils.addDays(today,7);
        final Long count1 = 10L;
        final Object[] object1 = new Object[3];
        object1[0] = room1;
        object1[1] = date1;
        object1[2] = count1;
        emptyBlockRoomDates.add(object1);
        final String room2 = "Room2";
        final Date date2 = DateUtils.addDays(today,14);
        final Long count2 = 20L;
        final Object[] object2 = new Object[3];
        object2[0] = room2;
        object2[1] = date2;
        object2[2] = count2;
        emptyBlockRoomDates.add(object2);

        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing);
        when(sessionBlockDaoMock.findAvailableSessionBlockDatesWithOverbookAndJudge(Mockito.anyString(),Mockito.any(Date.class),Mockito.any(Date.class),Mockito.any(SessionBlockType.class), Mockito.anyInt(), Mockito.any(CaseRelated.class))).thenReturn(emptyBlockRoomDates);

        final List<RoomDateForHearing> roomDates = hearingSlotService.getHearingDateSlotList(center, room1, 1, true);
        assertNotNull(roomDates);
        assertEquals(2, roomDates.size());
    }
    
    @Test
    public void shouldGetHearingForSlotWithOverbooking() {
        final String center = "Birmingham";
        final Date today = new Date();
        final Hearing hearing = new Hearing();
        hearing.setHearingType(HearingType.TRIAL);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setDateOfSending(today);
        caseRelated.getHearings().add(hearing);

        final PersonInCase defendant1 = new PersonInCase();
        defendant1.setRoleInCase(RoleInCase.DEFENDANT);
        defendant1.setCtlExpiresOn(DateUtils.addDays(today, 15));

        final PersonInCase defendant2 = new PersonInCase();
        defendant2.setRoleInCase(RoleInCase.DEFENDANT);
        defendant2.setCtlExpiresOn(DateUtils.addDays(today, 10));
        caseRelated.getPersonInCase().add(defendant1);
        caseRelated.getPersonInCase().add(defendant2);

        final Hearing hearing1 = new Hearing();
        hearing1.setHearingType(HearingType.PCM);
        hearing1.setStartDate(DateUtils.addDays(today,2));
        final Hearing hearing2 = new Hearing();
        hearing2.setHearingType(HearingType.PCM);
        hearing2.setStartDate(DateUtils.addDays(today,3));
        caseRelated.getHearings().add(hearing1);
        caseRelated.getHearings().add(hearing2);

        hearing.setCaseRelated(caseRelated);
        final List<Object[]> emptyBlockRoomDates = new ArrayList<Object[]>();
        final String room1 = "Room1";
        final Date date1 = DateUtils.addDays(today,7);
        final Long count1 = 10L;
        final Object[] object1 = new Object[3];
        object1[0] = room1;
        object1[1] = date1;
        object1[2] = count1;
        emptyBlockRoomDates.add(object1);
        final String room2 = "Room2";
        final Date date2 = DateUtils.addDays(today,14);
        final Long count2 = 20L;
        final Object[] object2 = new Object[3];
        object2[0] = room2;
        object2[1] = date2;
        object2[2] = count2;
        emptyBlockRoomDates.add(object2);

        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing);
        when(sessionBlockDaoMock.findAvailableSessionBlockDatesWithOverbook(Mockito.anyString(),Mockito.any(Date.class),Mockito.any(Date.class),Mockito.any(SessionBlockType.class), Mockito.anyInt(), Mockito.any(CaseRelated.class))).thenReturn(emptyBlockRoomDates);

        final List<RoomDateForHearing> roomDates = hearingSlotService.getHearingDateSlotList(center, room1, 1, true);
        assertNotNull(roomDates);
        assertEquals(2, roomDates.size());
    }


    private List<RoomDateForHearing> buildDatesForDaysInJune2015(final Long[] daysInJune) {
        final List<RoomDateForHearing> dates = new ArrayList<RoomDateForHearing>();
        for (final Long day : daysInJune) {
            final GregorianCalendar gc = new GregorianCalendar(2015, Calendar.JUNE, day.intValue());
            dates.add(new RoomDateForHearing("room-name", gc.getTime(), 0L));
        }
        return dates;
    }
}
