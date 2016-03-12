package uk.co.listing;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;

import uk.co.listing.dao.GenericDao;
import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.CourtDiary;
import uk.co.listing.domain.CourtRoom;
import uk.co.listing.domain.CourtRoomInDiary;
import uk.co.listing.domain.CourtSession;
import uk.co.listing.domain.CrustNonAvailableDates;
import uk.co.listing.domain.Hearing;
import uk.co.listing.domain.Panel;
import uk.co.listing.domain.SessionBlock;
import uk.co.listing.domain.SittingDate;
import uk.co.listing.domain.constant.BookingStatusEnum;
import uk.co.listing.domain.constant.BookingTypeEnum;
import uk.co.listing.domain.constant.HearingType;
import uk.co.listing.domain.constant.SessionBlockType;

public class ListingTestUtils {
    public String centerName = "Sample-Test-Center";
    public String crestNumber = "SAMPLE100";
    public CourtCenter center;
    public CourtRoom courtRoom;
    public CaseRelated caseRelated;

    public ListingTestUtils() {

    }

    public ListingTestUtils(final String centerName, final String crestNumber) {
        super();
        this.centerName = centerName;
        this.crestNumber = crestNumber;
    }

    public CourtCenter createCourtCenter(final GenericDao courtCenterDao) {
        center = new CourtCenter(1800L, centerName);
        courtCenterDao.save(center);
        return center;
    }

    public CaseRelated createCase(final GenericDao caseRelatedDao) {
        caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestNumber);
        caseRelated.setCourtCenter(center);
        caseRelatedDao.save(caseRelated);

        final CrustNonAvailableDates crustNonAvailableDates = new CrustNonAvailableDates();
        crustNonAvailableDates.setStartDate(new Date());
        crustNonAvailableDates.setEndDate(new Date());
        crustNonAvailableDates.setCaseRelated(caseRelated);
        crustNonAvailableDates.setReason("witnesses in hospital ");
        caseRelatedDao.save(crustNonAvailableDates);

        return caseRelated;
    }

    public Hearing createHearing(final GenericDao hearingDao) {
        final Set<Hearing> hearings = new HashSet<>();
        final Hearing hearing = new Hearing();
        hearings.add(hearing);
        hearing.setCaseRelated(caseRelated);
        hearing.setDaysEstimated(4.0);
        hearing.setHearingType(HearingType.TRIAL);
        hearing.setName("SampleHearingOne");
        hearing.setActive(true);
        hearing.setBookingType(BookingTypeEnum.PROVISIONAL);
        hearing.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearingDao.save(hearing);
        return hearing;
    }

    public CourtRoom createCourtRoom(final GenericDao courtRoomDao) {
        courtRoom = new CourtRoom("SampleRoomName");
        courtRoomDao.save(courtRoom);
        final CourtDiary courtDiary = new CourtDiary(center);
        courtRoomDao.save(courtDiary);
        courtRoomDao.save(new CourtRoomInDiary(courtRoom, courtDiary));
        return courtRoom;
    }

    public SessionBlock createSessionBlock(final GenericDao courtRoomDao) {

        final SittingDate sitDate = new SittingDate();
        sitDate.setCourtRoom(courtRoom);
        sitDate.setDay(DateUtils.truncate(new Date(), Calendar.DATE));
        courtRoomDao.save(sitDate);

        final Panel panel = new Panel();
        panel.setDescription("Panel for  JUDICIAL_OFFICER");
        courtRoomDao.save(panel);

        final CourtSession session = new CourtSession();
        session.setSitting(sitDate);
        session.setPanel(panel);
        courtRoomDao.save(session);

        final SessionBlock block = new SessionBlock();
        block.setBlockType(SessionBlockType.TRIAL);
        block.setSession(session);
        courtRoomDao.save(block);
        return block;
    }
}
