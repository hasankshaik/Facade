package uk.co.listing.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import uk.co.listing.BaseMockingUnitTest;
import uk.co.listing.dao.CaseNoteDao;
import uk.co.listing.dao.CaseRelatedDao;
import uk.co.listing.dao.CourtCenterDao;
import uk.co.listing.dao.CourtRoomDao;
import uk.co.listing.dao.CrustNonAvailableDatesDao;
import uk.co.listing.dao.HearingDao;
import uk.co.listing.dao.HearingInstanceDao;
import uk.co.listing.dao.PersonDao;
import uk.co.listing.dao.PersonInCaseDao;
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
import uk.co.listing.domain.constant.TimeEstimationUnit;
import uk.co.listing.exceptions.CcsException;
import uk.co.listing.rest.response.HearingWeb;

public class HearingServiceTest extends BaseMockingUnitTest {
    private final String ANNOTATION = "annotation";
    private final String crestCaseNumber = "crest2222222";
    private final String hearingTest = "hearingTest";
    private final String courtRoomTest = "courtRoom";
    private final BookingTypeEnum bookingType = BookingTypeEnum.PROVISIONAL;

    @Mock
    CaseRelatedDao caseRelatedDaoMock;

    @Mock
    HearingDao hearingDaoMock;

    @Mock
    HearingInstanceDao hearingInstanceDaoMock;

    @Mock
    SessionBlockDao sessionBlockDaoMock;

    @Mock
    CourtRoomDao courtRoomDaoMock;

    @Mock
    CourtCenterDao courtCenterDaoMock;

    @InjectMocks
    HearingService hearingService;

    @Mock
    PersonDao personDaoMock;

    @Mock
    PersonInCaseDao personInCaseDaoMock;

    @Mock
    CaseNoteDao caseNoteDaoMock;

    @Mock
    CrustNonAvailableDatesDao nonAvailableDatesMock;

    @Test
    public void testSaveHearing() {
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setEstimationUnit(TimeEstimationUnit.WEEKS);
        caseRelated.setTrialEstimate(5);
        final HearingWeb hearingWeb = new HearingWeb(hearingTest, crestCaseNumber, "TRIAL", "20/10/2015", 5.0, BookingStatusEnum.NOTBOOKED.name());
        hearingWeb.setBookingType(BookingTypeEnum.CONFIRMED.getDescription());
        when(caseRelatedDaoMock.findCaseByCrestCaseNumber(crestCaseNumber)).thenReturn(caseRelated);
        Mockito.doNothing().when(hearingDaoMock).save(Mockito.any(Hearing.class));
        final HearingWeb result = hearingService.saveUpdateHearing(hearingWeb);
        assertNotNull(result);
        assertTrue(result.getTrialEstimate().equals(25.0));
        verify(caseRelatedDaoMock, times(1)).findCaseByCrestCaseNumber(Mockito.anyString());
        verify(hearingDaoMock, times(1)).findHearingByKey(Mockito.anyString());
        verify(hearingDaoMock, times(1)).save(Mockito.any(Hearing.class));

    }

    @Test
    public void testSaveHearingWithNullEstimation() {
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        final HearingWeb hearingWeb = new HearingWeb(hearingTest, crestCaseNumber, null, null, 7.0, null);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumber(crestCaseNumber)).thenReturn(caseRelated);
        Mockito.doNothing().when(hearingDaoMock).save(Mockito.any(Hearing.class));
        final HearingWeb result = hearingService.saveUpdateHearing(hearingWeb);
        assertNotNull(result);
        verify(caseRelatedDaoMock, times(1)).findCaseByCrestCaseNumber(Mockito.anyString());
        verify(hearingDaoMock, times(1)).findHearingByKey(Mockito.anyString());
        verify(hearingDaoMock, times(1)).save(Mockito.any(Hearing.class));
        assertTrue(result.getTrialEstimate() == 0);

    }

    @Test
    public void testUpdateHearing() {
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        final Hearing hearing = new Hearing();
        hearing.setName(hearingTest);
        hearing.setHearingType(HearingType.TRIAL);
        hearing.setStartDate(new Date());
        hearing.setBookingType(bookingType);
        hearing.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing.setDaysEstimated(2.0);
        hearing.setActive(true);
        hearing.setCaseRelated(caseRelated);
        caseRelated.getHearings().add(hearing);
        final HearingWeb hearingWeb = new HearingWeb(hearingTest, crestCaseNumber, "TRIAL", "20/10/2015", 5.0, BookingStatusEnum.NOTBOOKED.getDescription());
        hearingWeb.setHearingNote("Hearing note");
        hearingWeb.setBookingType(bookingType.getDescription());
        when(caseRelatedDaoMock.findCaseByCrestCaseNumber(crestCaseNumber)).thenReturn(caseRelated);
        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing);
        Mockito.doNothing().when(hearingDaoMock).save(Mockito.any(Hearing.class));
        final HearingWeb result = hearingService.saveUpdateHearing(hearingWeb);
        assertNotNull(result);
        verify(caseRelatedDaoMock, times(0)).findCaseByCrestCaseNumber(Mockito.anyString());
        verify(hearingDaoMock, times(1)).findHearingByKey(Mockito.anyString());
        verify(hearingDaoMock, times(1)).save(Mockito.any(Hearing.class));
    }

    @Test
    public void testUpdateHearingWithEmptyValues() {
        final Hearing hearing = new Hearing();
        final Date now = new Date();
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        hearing.setBookingStatus(BookingStatusEnum.BOOKED);
        hearing.setBookingType(BookingTypeEnum.CONFIRMED);
        hearing.setHearingType(HearingType.APPEAL);
        hearing.setDaysEstimated(7.0);
        hearing.setStartDate(now);
        hearing.setCaseRelated(caseRelated);
        final HearingWeb hearingWeb = new HearingWeb(hearingTest, crestCaseNumber, "", "", 1.0, "");
        hearingWeb.setBookingType("");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumber(crestCaseNumber)).thenReturn(caseRelated);
        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing);
        Mockito.doNothing().when(hearingDaoMock).save(Mockito.any(Hearing.class));
        final HearingWeb result = hearingService.saveUpdateHearing(hearingWeb);
        assertNotNull(result);
        verify(caseRelatedDaoMock, times(0)).findCaseByCrestCaseNumber(Mockito.anyString());
        verify(hearingDaoMock, times(1)).findHearingByKey(Mockito.anyString());
        verify(hearingDaoMock, times(1)).save(Mockito.any(Hearing.class));
        assertTrue(hearing.getBookingStatus().equals(BookingStatusEnum.BOOKED));
        assertTrue(hearing.getHearingType().equals(HearingType.APPEAL));
        assertTrue(hearing.getBookingType().equals(BookingTypeEnum.CONFIRMED));
        assertTrue(hearing.getDaysEstimated().equals(1.0));
        assertTrue(hearing.getStartDate().equals(now));
    }

    @Test
    public void testUpdateHearingWithNullValues() {
        final Hearing hearing = new Hearing();
        final Date now = new Date();
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        hearing.setBookingStatus(BookingStatusEnum.BOOKED);
        hearing.setBookingType(BookingTypeEnum.CONFIRMED);
        hearing.setHearingType(HearingType.APPEAL);
        hearing.setDaysEstimated(7.0);
        hearing.setStartDate(now);
        hearing.setCaseRelated(caseRelated);
        final HearingWeb hearingWeb = new HearingWeb(hearingTest, crestCaseNumber, null, null, 7.0, null);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumber(crestCaseNumber)).thenReturn(caseRelated);
        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing);
        Mockito.doNothing().when(hearingDaoMock).save(Mockito.any(Hearing.class));
        final HearingWeb result = hearingService.saveUpdateHearing(hearingWeb);
        assertNotNull(result);
        verify(caseRelatedDaoMock, times(0)).findCaseByCrestCaseNumber(Mockito.anyString());
        verify(hearingDaoMock, times(1)).findHearingByKey(Mockito.anyString());
        verify(hearingDaoMock, times(1)).save(Mockito.any(Hearing.class));
        assertTrue(hearing.getBookingStatus().equals(BookingStatusEnum.BOOKED));
        assertTrue(hearing.getHearingType().equals(HearingType.APPEAL));
        assertTrue(hearing.getBookingType().equals(BookingTypeEnum.CONFIRMED));
        assertTrue(hearing.getDaysEstimated().equals(7.0));
        assertTrue(hearing.getStartDate().equals(now));

    }

    @Test(expected = Exception.class)
    public void testUpdateHearingWithWrongEstimation() {
        final Hearing hearing = new Hearing();
        final Date now = new Date();
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        hearing.setBookingStatus(BookingStatusEnum.BOOKED);
        hearing.setBookingType(BookingTypeEnum.CONFIRMED);
        hearing.setHearingType(HearingType.APPEAL);
        hearing.setDaysEstimated(7.0);
        hearing.setStartDate(now);
        hearing.setCaseRelated(caseRelated);
        final HearingWeb hearingWeb = new HearingWeb(hearingTest, crestCaseNumber, null, null, -7.0, null);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumber(crestCaseNumber)).thenReturn(caseRelated);
        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing);
        Mockito.doNothing().when(hearingDaoMock).save(Mockito.any(Hearing.class));
        final HearingWeb result = hearingService.saveUpdateHearing(hearingWeb);
        assertEquals(result.getBookingStatus(), hearing.getBookingStatus());
        verify(caseRelatedDaoMock, times(0)).findCaseByCrestCaseNumber(Mockito.anyString());
        verify(hearingDaoMock, times(1)).findHearingByKey(Mockito.anyString());
        verify(hearingDaoMock, times(0)).save(Mockito.any(Hearing.class));

    }

    @Test(expected = Exception.class)
    public void testUpdateHearingWithWrongNote() {
        final Hearing hearing = new Hearing();
        final Date now = new Date();
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        hearing.setBookingStatus(BookingStatusEnum.BOOKED);
        hearing.setBookingType(BookingTypeEnum.CONFIRMED);
        hearing.setHearingType(HearingType.APPEAL);
        hearing.setDaysEstimated(7.0);
        hearing.setStartDate(now);
        hearing.setCaseRelated(caseRelated);
        final HearingWeb hearingWeb = new HearingWeb(hearingTest, crestCaseNumber, null, null, 7.0, null);
        hearingWeb.setHearingNote("Annotation 12345678921234567892123456789212345678921234567892");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumber(crestCaseNumber)).thenReturn(caseRelated);
        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing);
        Mockito.doNothing().when(hearingDaoMock).save(Mockito.any(Hearing.class));
        final HearingWeb result = hearingService.saveUpdateHearing(hearingWeb);
        assertEquals(result.getBookingStatus(), hearing.getBookingStatus());
        verify(caseRelatedDaoMock, times(0)).findCaseByCrestCaseNumber(Mockito.anyString());
        verify(hearingDaoMock, times(1)).findHearingByKey(Mockito.anyString());
        verify(hearingDaoMock, times(0)).save(Mockito.any(Hearing.class));

    }

    @Test
    public void testGetUnlistedHearing() {
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setTrialEstimate(2);

        final Hearing hearing1 = new Hearing();
        hearing1.setName("Hearing 1");
        hearing1.setHearingType(HearingType.TRIAL);
        hearing1.setCaseRelated(caseRelated);
        hearing1.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing1.setDaysEstimated(2.0);

        final Hearing hearing2 = new Hearing();
        hearing2.setName("Hearing 2");
        hearing2.setCaseRelated(caseRelated);
        hearing2.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing2.setDaysEstimated(2.0);

        when(hearingDaoMock.findHearingByProperties(BookingStatusEnum.NOTBOOKED, HearingType.TRIAL)).thenReturn(Arrays.asList(hearing1, hearing2));
        Assert.assertEquals(2, hearingService.getUnlistedHearing().size());
    }

    // Test case for list Hearing
    @SuppressWarnings("unchecked")
    @Test
    public void testListHearing() {
        final Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2025, 4, 26);
        final Date today = DateUtils.truncate(calendar1.getTime(), Calendar.DATE);

        final Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2025, 4, 27);

        final HearingWeb hearingWeb = new HearingWeb();
        hearingWeb.setHearingName(hearingTest);
        hearingWeb.setHearingCase(crestCaseNumber);
        hearingWeb.setHearingType(HearingType.TRIAL.getDescription());
        hearingWeb.setTrialEstimate(1.0);
        hearingWeb.setStartDateForSlot(today);
        hearingWeb.setBookingType(bookingType.getDescription());
        hearingWeb.setCourtRoomName(courtRoomTest);
        final Hearing hearing1 = new Hearing();
        hearing1.setName(hearingTest);
        hearing1.setHearingType(HearingType.TRIAL);
        hearing1.setStartDate(today);
        hearing1.setBookingType(bookingType);

        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        hearing1.setCaseRelated(caseRelated);

        final SessionBlock sessionBlock = new SessionBlock();

        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing1);
        when(sessionBlockDaoMock.findSessionBlocksOrSlotByDateAndRoom(Mockito.any(String.class), Mockito.any(List.class), Mockito.any(SessionBlockType.class))).thenReturn(Arrays.asList(sessionBlock));
        final HearingWeb xs = hearingService.listHearing(hearingWeb);
        assertTrue(BookingStatusEnum.getBookinsStatus(xs.getBookingStatus()) == BookingStatusEnum.BOOKED);
        verify(hearingInstanceDaoMock).save(Mockito.any(HearingInstance.class));
    }

    @Test
    public void testGetHearingDetails() {
        final Hearing hearing1 = new Hearing();
        hearing1.setName(hearingTest);
        hearing1.setHearingType(HearingType.TRIAL);
        hearing1.setBookingType(bookingType);
        hearing1.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing1.setDaysEstimated(2.0);

        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        hearing1.setCaseRelated(caseRelated);

        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing1);
        Assert.assertTrue(bookingType.getDescription().equals(hearingService.getHearingDetail(hearingTest).getBookingType()));

    }

    // Test case for Un list Hearing
    @Test
    public void testUnListHearing() {
        final Date today = DateUtils.truncate(new Date(), Calendar.DATE);

        final HearingInstance hearingInstance = new HearingInstance();
        final Set<HearingInstance> listHearingInstance = new HashSet<HearingInstance>();
        listHearingInstance.add(hearingInstance);

        final Hearing hearing1 = new Hearing();
        hearing1.setName(hearingTest);
        hearing1.setHearingType(HearingType.TRIAL);
        hearing1.setStartDate(today);
        hearing1.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing1.setHearingInstance(listHearingInstance);

        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        hearing1.setCaseRelated(caseRelated);

        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing1);
        hearingService.unListHearing(hearingTest, true);
        verify(hearingInstanceDaoMock).delete(Mockito.any(HearingInstance.class));
    }

    @Test(expected = Exception.class)
    public void testUnListHearingException() {
        final Date today = DateUtils.truncate(new Date(), Calendar.DATE);

        final HearingInstance hearingInstance = new HearingInstance();
        final Set<HearingInstance> listHearingInstance = new HashSet<HearingInstance>();
        listHearingInstance.add(hearingInstance);

        final Hearing hearing1 = new Hearing();
        hearing1.setName(hearingTest);
        hearing1.setHearingType(HearingType.TRIAL);
        hearing1.setStartDate(today);
        hearing1.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing1.setHearingInstance(listHearingInstance);

        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        hearing1.setCaseRelated(caseRelated);

        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing1);
        doThrow(new Exception()).when(hearingInstanceDaoMock).delete(HearingInstance.class);
        hearingService.unListHearing(hearingTest, true);
    }

    @Test
    public void testReListHearingWithNoChange() {
        final CourtRoom listedcourtRoom = new CourtRoom();
        listedcourtRoom.setRoomName(courtRoomTest);
        final Double DaysEst = 2.0;
        final Double DaysEstWeb = 2.0;

        final Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2016, 4, 26);
        final Date today = DateUtils.truncate(calendar1.getTime(), Calendar.DATE);

        final HearingWeb hearingWeb = new HearingWeb();
        hearingWeb.setHearingName(hearingTest);
        hearingWeb.setHearingCase(crestCaseNumber);
        hearingWeb.setHearingType(HearingType.TRIAL.name());
        hearingWeb.setTrialEstimate(DaysEstWeb);
        hearingWeb.setStartDateForSlot(today);
        hearingWeb.setBookingType(bookingType.name());
        hearingWeb.setCourtRoomName(courtRoomTest);
        hearingWeb.setHearingNote(ANNOTATION);

        final Hearing listedhearing = new Hearing();
        listedhearing.setName(hearingTest);
        listedhearing.setHearingType(HearingType.TRIAL);
        listedhearing.setStartDate(today);
        listedhearing.setBookingType(bookingType);
        listedhearing.setDaysEstimated(DaysEst);

        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        listedhearing.setCaseRelated(caseRelated);

        when(courtRoomDaoMock.findHearingCourtRoom(Mockito.anyString())).thenReturn(listedcourtRoom);
        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(listedhearing);
        hearingService.reListingHearing(hearingWeb);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReListHearingWithPeriodChange() {
        final CourtRoom listedcourtRoom = new CourtRoom();
        listedcourtRoom.setRoomName(courtRoomTest);
        final Double DaysEst = 2.0;
        final Double DaysEstWeb = 1.0;

        final Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2020, 4, 26);
        final Date today = DateUtils.truncate(calendar1.getTime(), Calendar.DATE);

        final Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2020, 4, 27);

        final Calendar calendar3 = Calendar.getInstance();
        calendar3.set(2020, 4, 28);

        final HearingWeb hearingWeb = new HearingWeb();
        hearingWeb.setHearingName(hearingTest);
        hearingWeb.setHearingCase(crestCaseNumber);
        hearingWeb.setHearingType(HearingType.TRIAL.name());
        hearingWeb.setTrialEstimate(DaysEstWeb);
        hearingWeb.setStartDateForSlot(today);
        hearingWeb.setBookingType(bookingType.name());
        hearingWeb.setCourtRoomName(courtRoomTest);
        hearingWeb.setHearingNote(ANNOTATION);

        final HearingInstance hearingInstance = new HearingInstance();
        final Set<HearingInstance> listHearingInstance = new HashSet<HearingInstance>();
        listHearingInstance.add(hearingInstance);

        final Hearing listedHearing = new Hearing();
        listedHearing.setName(hearingTest);
        listedHearing.setHearingType(HearingType.TRIAL);
        listedHearing.setStartDate(today);
        listedHearing.setBookingType(bookingType);
        listedHearing.setDaysEstimated(DaysEst);
        listedHearing.setHearingInstance(listHearingInstance);

        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        listedHearing.setCaseRelated(caseRelated);

        final SessionBlock sessionBlock = new SessionBlock();
        when(courtRoomDaoMock.findHearingCourtRoom(hearingTest)).thenReturn(listedcourtRoom);
        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(listedHearing);

        when(sessionBlockDaoMock.findSessionBlocksOrSlotByDateAndRoom(Mockito.any(String.class), Mockito.any(List.class), Mockito.any(SessionBlockType.class))).thenReturn(Arrays.asList(sessionBlock));

        hearingService.reListingHearing(hearingWeb);
        verify(hearingDaoMock, times(1)).save(Mockito.any(HearingInstance.class));
        verify(hearingInstanceDaoMock, times(1)).delete(Mockito.any(HearingInstance.class));
        verify(hearingInstanceDaoMock, times(1)).save(Mockito.any(HearingInstance.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReListHearingWithRoomChange() {
        final CourtRoom listedcourtRoom = new CourtRoom();
        listedcourtRoom.setRoomName(courtRoomTest);
        final String newRoomName = "newcourtRoomTest";
        final CourtRoom newcourtRoom = new CourtRoom();
        newcourtRoom.setRoomName("newcourtRoomTest");
        final Double DaysEst = 3.0;
        final Double DaysEstWeb = 1.0;

        final Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2025, 4, 26);
        final Date today = DateUtils.truncate(calendar1.getTime(), Calendar.DATE);

        final Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2025, 4, 27);

        final Calendar calendar3 = Calendar.getInstance();
        calendar3.set(2025, 4, 28);

        final HearingWeb hearingWeb = new HearingWeb();
        hearingWeb.setHearingName(hearingTest);
        hearingWeb.setHearingKey(hearingTest);
        hearingWeb.setHearingCase(crestCaseNumber);
        hearingWeb.setHearingType(HearingType.TRIAL.name());
        hearingWeb.setTrialEstimate(DaysEstWeb);
        hearingWeb.setStartDateForSlot(today);
        hearingWeb.setBookingType(bookingType.name());
        hearingWeb.setCourtRoomName(newRoomName);
        hearingWeb.setHearingNote(ANNOTATION);

        final HearingInstance hearingInstance = new HearingInstance();
        final Set<HearingInstance> listHearingInstance = new HashSet<HearingInstance>();
        listHearingInstance.add(hearingInstance);

        final Hearing listedHearing = new Hearing();
        listedHearing.setName(hearingTest);
        listedHearing.setHearingType(HearingType.TRIAL);
        listedHearing.setStartDate(today);
        listedHearing.setBookingType(bookingType);
        listedHearing.setDaysEstimated(DaysEst);
        listedHearing.setHearingInstance(listHearingInstance);

        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        listedHearing.setCaseRelated(caseRelated);

        final SessionBlock sessionBlock = new SessionBlock();
        when(courtRoomDaoMock.findHearingCourtRoom(hearingTest)).thenReturn(listedcourtRoom);
        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(listedHearing);

        when(sessionBlockDaoMock.findSessionBlocksOrSlotByDateAndRoom(Mockito.any(String.class), Mockito.any(List.class), Mockito.any(SessionBlockType.class))).thenReturn(Arrays.asList(sessionBlock));

        hearingService.reListingHearing(hearingWeb);
        verify(hearingDaoMock, times(1)).save(Mockito.any(HearingInstance.class));
        verify(hearingInstanceDaoMock, times(1)).delete(Mockito.any(HearingInstance.class));
        verify(hearingInstanceDaoMock, times(1)).save(Mockito.any(HearingInstance.class));
    }

    @Test
    public void testReListHearingException() {
        final Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2025, 4, 27);
        HearingWeb hearingWeb = new HearingWeb();
        hearingWeb.setStartDateForSlot(calendar2.getTime());

        when(courtRoomDaoMock.findHearingCourtRoom(hearingTest)).thenThrow(new RuntimeException());
        try {
            hearingWeb = hearingService.reListingHearing(hearingWeb);
            org.junit.Assert.fail("Should have thrown business exception.");
        } catch (final CcsException e) {

        }
        verify(hearingDaoMock, times(0)).save(Mockito.any(HearingInstance.class));
        verify(hearingInstanceDaoMock, times(0)).delete(Mockito.any(HearingInstance.class));
        verify(hearingInstanceDaoMock, times(0)).save(Mockito.any(HearingInstance.class));
    }

    @Test
    public void testReListHearingForPastDate() {
        HearingWeb hearingWeb = new HearingWeb();
        hearingWeb.setStartDateForSlot(new Date());

        try {
            hearingWeb = hearingService.reListingHearing(hearingWeb);
            org.junit.Assert.fail("Should have thrown business exception.");
        } catch (final CcsException e) {

        }
        verify(hearingDaoMock, times(0)).save(Mockito.any(HearingInstance.class));
        verify(hearingInstanceDaoMock, times(0)).delete(Mockito.any(HearingInstance.class));
        verify(hearingInstanceDaoMock, times(0)).save(Mockito.any(HearingInstance.class));
    }

    @Test
    public void testListHearingForPastDate() {
        HearingWeb hearingWeb = new HearingWeb();
        hearingWeb.setHearingName(hearingTest);
        hearingWeb.setHearingKey(hearingTest);
        hearingWeb.setHearingCase(crestCaseNumber);
        hearingWeb.setHearingType(HearingType.TRIAL.name());
        hearingWeb.setTrialEstimate(1.0);
        hearingWeb.setStartDateForSlot(DateUtils.addDays(new Date(), -1));
        hearingWeb.setBookingType(bookingType.name());
        hearingWeb.setCourtRoomName("name");

        try {
            hearingWeb = hearingService.listHearing(hearingWeb);
            org.junit.Assert.fail("Should have thrown business exception.");
        } catch (final CcsException e) {

        }
        verify(hearingDaoMock, times(0)).findHearingByKey(Mockito.any(String.class));
    }

    @Test
    public void testAutoListingOfPcmHearing() {
        final Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2025, 4, 26);
        final Date today = DateUtils.truncate(calendar1.getTime(), Calendar.DATE);

        final Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2025, 4, 27);

        final HearingWeb hearingWeb1 = new HearingWeb();
        hearingWeb1.setHearingName(hearingTest);
        hearingWeb1.setHearingCase(crestCaseNumber);
        hearingWeb1.setHearingType(HearingType.PCM.getDescription());
        hearingWeb1.setStartDateForSlot(today);

        final Hearing hearing1 = new Hearing();
        hearing1.setName(hearingTest);
        hearing1.setHearingType(HearingType.PCM);
        hearing1.setStartDate(today);

        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        hearing1.setCaseRelated(caseRelated);

        final SessionBlock sessionBlock1 = new SessionBlock();
        final SessionBlock sessionBlock2 = new SessionBlock();
        final SessionBlock sessionBlock3 = new SessionBlock();

        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing1);
        when(sessionBlockDaoMock.findSessionBlocksByDateCourtCenterAndType(Mockito.any(String.class), Mockito.any(SessionBlockType.class), Mockito.any(Date.class))).thenReturn(
                Arrays.asList(sessionBlock1, sessionBlock2, sessionBlock3));
        when(sessionBlockDaoMock.getSingleResult(Mockito.any())).thenReturn(sessionBlock1);

        hearingService.autoScheduleHearing(hearingWeb1, "centerName");
        hearingService.autoScheduleHearing(hearingWeb1, "centerName");
        hearingService.autoScheduleHearing(hearingWeb1, "centerName");
        hearingService.autoScheduleHearing(hearingWeb1, "centerName");
        hearingService.autoScheduleHearing(hearingWeb1, "centerName");
        hearingService.autoScheduleHearing(hearingWeb1, "centerName");
        hearingService.autoScheduleHearing(hearingWeb1, "centerName");
        hearingService.autoScheduleHearing(hearingWeb1, "centerName");

        verify(hearingInstanceDaoMock, times(8)).save(Mockito.any(HearingInstance.class));
        verify(hearingDaoMock, times(8)).save(Mockito.any(Hearing.class));
    }

    @Test
    public void testAutoSchedulingFutureDateHearing() {
        final HearingWeb hearingWeb1 = new HearingWeb();
        hearingWeb1.setHearingName(hearingTest);
        hearingWeb1.setHearingCase(crestCaseNumber);
        hearingWeb1.setHearingType(HearingType.PCM.getDescription());
        hearingWeb1.setStartDateForSlot(DateUtils.addDays(new Date(), -1));

        final Hearing hearing1 = new Hearing();
        hearing1.setName(hearingTest);
        hearing1.setHearingType(HearingType.PCM);
        hearing1.setStartDate(new Date());

        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing1);
        hearingService.autoScheduleHearing(hearingWeb1, "centerName");
        verify(sessionBlockDaoMock, times(0)).findSessionBlocksByDateCourtCenterAndType(Mockito.any(String.class), Mockito.any(SessionBlockType.class), Mockito.any(Date.class));

    }

    @Test
    public void testGetPcmHearingForAction() {
        final Hearing hearing1 = new Hearing();
        hearing1.setName(hearingTest);
        hearing1.setHearingType(HearingType.PCM);
        hearing1.setBookingType(bookingType);
        hearing1.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing1.setDaysEstimated(2.0);

        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        hearing1.setCaseRelated(caseRelated);

        final List<Hearing> list = new ArrayList<>();
        list.add(hearing1);

        when(hearingDaoMock.findPcmHearingForToday(Mockito.any(Date.class), Mockito.any(HearingStatusEnum.class))).thenReturn(list);

        final Map<String, List<HearingWeb>> map = hearingService.getPcmHearingForAction();
        final List<HearingWeb> pcmhToday = map.get("current");

        assertNotNull(pcmhToday);
        Assert.assertTrue(bookingType.getDescription().equals(pcmhToday.get(0).getBookingType()));
        verify(hearingDaoMock, times(1)).findPcmHearingForToday(Mockito.any(Date.class), Mockito.any(HearingStatusEnum.class));
        verify(hearingDaoMock, times(1)).findPcmHearingForPast(Mockito.any(Date.class), Mockito.any(HearingStatusEnum.class));
        verify(hearingDaoMock, times(1)).findPcmHearingForTomorrow(Mockito.any(Date.class), Mockito.any(HearingStatusEnum.class));
    }

    @Test
    public void testUpdatePcmHearingStatusFail() {
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setEstimationUnit(TimeEstimationUnit.WEEKS);
        caseRelated.setTrialEstimate(5);
        final HearingWeb hearingWeb = new HearingWeb(hearingTest, crestCaseNumber, "PCM", "20/10/2015", 5.0, BookingStatusEnum.NOTBOOKED.name());
        hearingWeb.setBookingType(BookingTypeEnum.CONFIRMED.getDescription());
        when(caseRelatedDaoMock.findCaseByCrestCaseNumber(crestCaseNumber)).thenReturn(caseRelated);
        Mockito.doNothing().when(hearingDaoMock).save(Mockito.any(Hearing.class));
        try {
            hearingService.updatePcmHearingStatus(hearingWeb);
            org.junit.Assert.fail("This hearing cannot be updated.");
        } catch (final CcsException e) {

        }
        verify(hearingDaoMock, times(1)).findHearingByKey(Mockito.anyString());

    }

    @Test
    public void testUpdatePcmHearingStatusSuccess() {
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        final Hearing hearing = new Hearing();
        hearing.setName(hearingTest);
        hearing.setHearingType(HearingType.PCM);
        hearing.setStartDate(new Date());
        hearing.setBookingType(bookingType);
        hearing.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing.setDaysEstimated(2.0);
        hearing.setActive(true);
        hearing.setCaseRelated(caseRelated);
        hearing.setHearingStatus(HearingStatusEnum.PENDING);
        caseRelated.getHearings().add(hearing);
        final HearingWeb hearingWeb = new HearingWeb(hearingTest, crestCaseNumber, "PCM", "20/10/2015", 5.0, BookingStatusEnum.NOTBOOKED.getDescription());
        hearingWeb.setHearingStatus(HearingStatusEnum.COMPLETE.getDescription());
        when(caseRelatedDaoMock.findCaseByCrestCaseNumber(crestCaseNumber)).thenReturn(caseRelated);
        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing);
        Mockito.doNothing().when(hearingDaoMock).save(Mockito.any(Hearing.class));
        hearingService.updatePcmHearingStatus(hearingWeb);
        verify(caseRelatedDaoMock, times(0)).findCaseByCrestCaseNumber(Mockito.anyString());
        verify(hearingDaoMock, times(1)).findHearingByKey(Mockito.anyString());
        verify(hearingDaoMock, times(1)).save(Mockito.any(Hearing.class));
    }

    @Test
    public void testDateOfSendingKPIDate() {
        final Hearing hearing1 = new Hearing();
        hearing1.setName(hearingTest);
        hearing1.setHearingType(HearingType.TRIAL);
        hearing1.setBookingType(bookingType);
        hearing1.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing1.setDaysEstimated(2.0);

        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setDateOfSending(new Date());
        hearing1.setCaseRelated(caseRelated);

        final Date kpiDate = DateUtils.addDays(new Date(), 182);

        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing1);
        Assert.assertTrue(bookingType.getDescription().equals(hearingService.getHearingDetail(hearingTest).getBookingType()));
        Assert.assertTrue(kpiDate.equals(hearingService.getHearingDetail(hearingTest).getTrialKPIDate()));
    }

    @Test
    public void testDateOfCommittalKPIDate() {
        final Hearing hearing1 = new Hearing();
        hearing1.setName(hearingTest);
        hearing1.setHearingType(HearingType.TRIAL);
        hearing1.setBookingType(bookingType);
        hearing1.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing1.setDaysEstimated(2.0);

        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setDateOfCommittal(new Date());
        hearing1.setCaseRelated(caseRelated);

        final Date kpiDate = DateUtils.addDays(new Date(), 182);

        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing1);
        Assert.assertTrue(bookingType.getDescription().equals(hearingService.getHearingDetail(hearingTest).getBookingType()));
        Assert.assertTrue(kpiDate.equals(hearingService.getHearingDetail(hearingTest).getTrialKPIDate()));
    }

    @Test
    public void testDateOfSendingCommittalKPIDate() {
        final Hearing hearing1 = new Hearing();
        hearing1.setName(hearingTest);
        hearing1.setHearingType(HearingType.TRIAL);
        hearing1.setBookingType(bookingType);
        hearing1.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing1.setDaysEstimated(2.0);

        final Date sendingDage = DateUtils.addDays(new Date(), 2);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setDateOfSending(sendingDage);
        caseRelated.setDateOfCommittal(new Date());
        hearing1.setCaseRelated(caseRelated);

        final Date kpiDate = DateUtils.addDays(sendingDage, 182);

        when(hearingDaoMock.findHearingByKey(Mockito.anyString())).thenReturn(hearing1);
        Assert.assertTrue(bookingType.getDescription().equals(hearingService.getHearingDetail(hearingTest).getBookingType()));
        Assert.assertTrue(kpiDate.equals(hearingService.getHearingDetail(hearingTest).getTrialKPIDate()));
    }

}
