package uk.co.listing.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.listing.BaseTransactionalIntegrationTest;
import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.Hearing;
import uk.co.listing.domain.constant.BookingStatusEnum;
import uk.co.listing.domain.constant.BookingTypeEnum;
import uk.co.listing.domain.constant.HearingStatusEnum;
import uk.co.listing.domain.constant.HearingType;
import uk.co.listing.utils.DateTimeUtils;

public class HearingDaoTest extends BaseTransactionalIntegrationTest {

    @Autowired
    private HearingDao hearingDao;
    @Autowired
    private CaseRelatedDao caseRelatedDao;
    @Autowired
    private CourtCenterDao courtCenterDao;

    @Test
    public void testFindHearingByName() {
        final String hearingName = "TestHearingOne1";
        final String crestCaseNumber = "T999999";
        final BookingTypeEnum bookingType = BookingTypeEnum.PROVISIONAL;
        final CourtCenter center = new CourtCenter(9000L, "test-center-1");
        courtCenterDao.save(center);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setCourtCenter(center);
        caseRelatedDao.save(caseRelated);

        final Set<Hearing> hearings = new HashSet<>();
        final Hearing hearing = new Hearing();
        hearings.add(hearing);
        hearing.setCaseRelated(caseRelated);
        hearing.setDaysEstimated(4.0);
        hearing.setHearingType(HearingType.TRIAL);
        hearing.setName(hearingName);
        hearing.setActive(true);
        hearing.setBookingType(bookingType);
        hearing.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearingDao.save(hearing);
        final Hearing hearingRs = hearingDao.findHearingByKey(hearing.getHearingKey());
        assertNotNull(hearingRs);
        assertEquals(hearingRs.getName(), hearingName);
        assertEquals(hearingRs.getCaseRelated().getCrestCaseNumber(), crestCaseNumber);
        assertEquals(hearingRs.getBookingType(), bookingType);
    }

    @Test
    public void testFindUnlistedHearing() {
        final String hearingName = "TestHearingTwo2";
        final String crestCaseNumber = "T999998";
        final CourtCenter center = new CourtCenter(9000L, "test-center-1");
        courtCenterDao.save(center);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setCourtCenter(center);
        caseRelatedDao.save(caseRelated);

        final Set<Hearing> hearings = new HashSet<>();
        final Hearing hearing = new Hearing();
        hearings.add(hearing);
        hearing.setCaseRelated(caseRelated);
        hearing.setDaysEstimated(4.0);
        hearing.setHearingType(HearingType.TRIAL);
        hearing.setName(hearingName);
        hearing.setActive(true);
        hearing.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearingDao.save(hearing);
        final List<Hearing> hearingRs = hearingDao.findHearingByProperties(BookingStatusEnum.NOTBOOKED, HearingType.TRIAL);
        assertNotNull(hearingRs);
        assertTrue(hearingRs.size() > 0);
        boolean success = false;
        for (final Hearing hearing2 : hearingRs) {
            if (StringUtils.equals(hearingName, hearing2.getName())) {
                success = true;
            }
        }
        assertTrue(success);
    }

    @Test
    public void testFindHearing() {
        final String hearingName = "TestHearing";
        final String crestCaseNumber = "T999998";
        final CourtCenter center = new CourtCenter(9000L, "test-center-1");
        courtCenterDao.save(center);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setCourtCenter(center);
        caseRelatedDao.save(caseRelated);

        final Set<Hearing> hearings = new HashSet<>();
        final Hearing hearingOne = new Hearing();
        hearings.add(hearingOne);
        hearingOne.setCaseRelated(caseRelated);
        hearingOne.setDaysEstimated(4.0);
        hearingOne.setHearingType(HearingType.TRIAL);
        hearingOne.setName(hearingName);
        hearingOne.setActive(true);
        hearingOne.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearingDao.save(hearingOne);

        final Hearing hearingTwo = new Hearing();
        hearings.add(hearingTwo);
        hearingTwo.setCaseRelated(caseRelated);
        hearingTwo.setDaysEstimated(4.0);
        hearingTwo.setHearingType(HearingType.TRIAL);
        hearingTwo.setName(hearingName + "Two");
        hearingTwo.setActive(true);
        hearingTwo.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearingDao.save(hearingTwo);

        final List<Hearing> hearingRs = hearingDao.findHearingByProperties(BookingStatusEnum.NOTBOOKED, HearingType.PCM);
        assertNotNull(hearingRs);
        assertTrue(hearingRs.size() > 0);
        boolean success = false;
        for (final Hearing hearing2 : hearingRs) {
            if (StringUtils.equals(hearingName, hearing2.getName())) {
                success = true;
            }
        }
        assertFalse(success);
    }

    @Test
    public void findPcmHearingForTomorrow() {
        final String hearingName = "TestHearingOne1";
        final String crestCaseNumber = "T999999";
        final BookingTypeEnum bookingType = BookingTypeEnum.PROVISIONAL;
        final CourtCenter center = new CourtCenter(9000L, "test-center-1");
        courtCenterDao.save(center);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setCourtCenter(center);
        caseRelatedDao.save(caseRelated);

        final Set<Hearing> hearings = new HashSet<>();
        final Hearing hearing = new Hearing();
        hearings.add(hearing);
        hearing.setCaseRelated(caseRelated);
        hearing.setDaysEstimated(4.0);
        hearing.setHearingType(HearingType.PCM);
        hearing.setName(hearingName);
        hearing.setActive(true);
        hearing.setBookingType(bookingType);
        hearing.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing.setHearingStatus(HearingStatusEnum.PENDING);
        hearing.setStartDate((DateTimeUtils.getWorkingDaysFromStartDate(new Date(), 2)).get(1));
        hearingDao.save(hearing);
        final List<Hearing> hearingRs = hearingDao.findPcmHearingForTomorrow(hearing.getStartDate(), HearingStatusEnum.PENDING);
        assertNotNull(hearingRs);
        assertTrue(hearingRs.size() > 0);
        for (final Hearing h : hearingRs) {
            if (h.getName().equals(hearingName)) {
                assertEquals(h.getCaseRelated().getCrestCaseNumber(), crestCaseNumber);
                assertEquals(h.getBookingType(), bookingType);
            }
        }
    }

    @Test
    public void findPcmHearingForToday() {
        final String hearingName = "TestHearingOne1";
        final String crestCaseNumber = "T999999";
        final BookingTypeEnum bookingType = BookingTypeEnum.PROVISIONAL;
        final CourtCenter center = new CourtCenter(9000L, "test-center-1");
        courtCenterDao.save(center);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setCourtCenter(center);
        caseRelatedDao.save(caseRelated);

        final Set<Hearing> hearings = new HashSet<>();
        final Hearing hearing = new Hearing();
        hearings.add(hearing);
        hearing.setCaseRelated(caseRelated);
        hearing.setDaysEstimated(4.0);
        hearing.setHearingType(HearingType.PCM);
        hearing.setName(hearingName);
        hearing.setActive(true);
        hearing.setBookingType(bookingType);
        hearing.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing.setHearingStatus(HearingStatusEnum.PENDING);
        hearing.setStartDate(new Date());
        hearingDao.save(hearing);
        // find pcmh hearing with non complete status
        final List<Hearing> hearingRs = hearingDao.findPcmHearingForToday(hearing.getStartDate(), HearingStatusEnum.COMPLETE);
        assertNotNull(hearingRs);
        assertTrue(hearingRs.size() > 0);
        for (final Hearing h : hearingRs) {
            if (h.getName().equals(hearingName)) {
                assertEquals(h.getCaseRelated().getCrestCaseNumber(), crestCaseNumber);
                assertEquals(h.getBookingType(), bookingType);
            }

        }
    }

    @Test
    public void findPcmHearingForPast() {
        final String hearingName = "TestHearingOne1";
        final String crestCaseNumber = "T999999";
        final BookingTypeEnum bookingType = BookingTypeEnum.PROVISIONAL;
        final CourtCenter center = new CourtCenter(9000L, "test-center-1");
        courtCenterDao.save(center);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        caseRelated.setCourtCenter(center);
        caseRelatedDao.save(caseRelated);

        final Set<Hearing> hearings = new HashSet<>();
        final Hearing hearing = new Hearing();
        hearings.add(hearing);
        hearing.setCaseRelated(caseRelated);
        hearing.setDaysEstimated(4.0);
        hearing.setHearingType(HearingType.PCM);
        hearing.setName(hearingName);
        hearing.setActive(true);
        hearing.setBookingType(bookingType);
        hearing.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearing.setHearingStatus(HearingStatusEnum.PENDING);
        final Date date = new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime();
        hearing.setStartDate(date);
        hearingDao.save(hearing);
        // find past pcmh hearing with non complete status
        final List<Hearing> hearingRs = hearingDao.findPcmHearingForPast(new Date(), HearingStatusEnum.COMPLETE);
        assertNotNull(hearingRs);
        assertTrue(hearingRs.size() > 0);
        for (final Hearing h : hearingRs) {
            if (h.getName().equals(hearingName)) {
                assertEquals(h.getCaseRelated().getCrestCaseNumber(), crestCaseNumber);
                assertEquals(h.getBookingType(), bookingType);
            }

        }
    }
}
