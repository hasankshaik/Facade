package uk.co.listing.dao;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.listing.BaseTransactionalIntegrationTest;
import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.CourtDiary;
import uk.co.listing.domain.CourtRoom;
import uk.co.listing.domain.CourtRoomInDiary;
import uk.co.listing.domain.CourtSession;
import uk.co.listing.domain.Hearing;
import uk.co.listing.domain.HearingInstance;
import uk.co.listing.domain.JudicialOfficer;
import uk.co.listing.domain.Panel;
import uk.co.listing.domain.PanelMember;
import uk.co.listing.domain.SessionBlock;
import uk.co.listing.domain.SittingDate;
import uk.co.listing.domain.constant.BookingStatusEnum;
import uk.co.listing.domain.constant.BookingTypeEnum;
import uk.co.listing.domain.constant.HearingType;
import uk.co.listing.domain.constant.JudicialOfficerType;
import uk.co.listing.domain.constant.PanelMemberType;
import uk.co.listing.domain.constant.SessionBlockType;

public class CourtRoomDaoTest extends BaseTransactionalIntegrationTest {

    private static final String TEST_CENTER = "test-center";
    private static final String TEST_ROOM = "test-room";
    private static final String JUDICIAL_OFFICER = "CourtSessionDaoTest JudgeA";

    @Autowired
    private SessionBlockDao sessionBlockDao;

    @Autowired
    private JudicialOfficerDao judicialOfficerDao;

    @Autowired
    private PanelDao panelDao;

    @Autowired
    private PanelMemberDao panelMemberDao;

    @Autowired
    private SittingDao sittingDao;
    @Autowired
    CourtSessionDao courtSessionDao;

    @Autowired
    HearingDao hearingDao;
    @Autowired
    HearingInstanceDao hearingInstanceDao;
    @Autowired
    private CourtCenterDao courtCenterDao;

    @Autowired
    private CourtRoomDao courtRoomDao;

    @Autowired
    private CourtDiaryDao courtDiaryDao;

    @Autowired
    private CourtRoomInDiaryDao courtRoomInDiaryDao;

    @Autowired
    private CaseRelatedDao caseRelatedDao;

    @Test
    public void testfindCourtRoomByName() {
        final CourtRoom courtRoom = new CourtRoom(TEST_ROOM);
        courtRoomDao.save(courtRoom);
        final CourtRoom courtRoomFetched = courtRoomDao.findCourtRoomByName(TEST_ROOM);
        assertTrue(courtRoom.getRoomName().equals(courtRoomFetched.getRoomName()));

    }

    @Test
    public void testFindCourtRooms() {
        final int preSize = courtRoomDao.findCourtRoomsByCenterName(TEST_CENTER).size();
        courtCenterDao.save(new CourtCenter(5050L, TEST_CENTER));
        final CourtCenter courtCenter = courtCenterDao.findCourtCentreByName(TEST_CENTER);
        courtDiaryDao.save(new CourtDiary(courtCenter));
        final CourtDiary courtDiary = courtDiaryDao.findCourtDiary(TEST_CENTER);
        courtRoomDao.save(new CourtRoom(TEST_ROOM));
        final CourtRoom courtRoom = courtRoomDao.findCourtRoomByName(TEST_ROOM);
        courtRoomInDiaryDao.save(new CourtRoomInDiary(courtRoom, courtDiary));

        final int postSize = courtRoomDao.findCourtRoomsByCenterName(TEST_CENTER).size();

        log.info("pre size:" + preSize);
        log.info("post size:" + postSize);

        assertTrue(postSize == (preSize + 1));
    }

    @Test
    public void testFindHearingCourtRoom() {

        final Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2015, 5, 27);
        final Date day1 = DateUtils.truncate(calendar1.getTime(), Calendar.DATE);

        final Date day2 = DateUtils.addDays(day1, 1);

        // Court Room
        courtRoomDao.save(new CourtRoom(TEST_ROOM));
        final CourtRoom courtRoom = courtRoomDao.findCourtRoomByName(TEST_ROOM);

        // Judge
        final JudicialOfficer judicialOfficer = new JudicialOfficer(JUDICIAL_OFFICER, JudicialOfficerType.CIRCUIT, true);
        judicialOfficerDao.save(judicialOfficer);

        final Panel panel = new Panel();
        panel.setDescription("Panel for " + JUDICIAL_OFFICER);
        panelDao.save(panel);

        final PanelMember panelMember = new PanelMember();
        panelMember.setPanel(panel);
        panelMember.setJudicialOfficer(judicialOfficer);
        panelMember.setPanelMemberType(PanelMemberType.JUDGE);
        panelMemberDao.save(panelMember);

        final SittingDate sitting1 = new SittingDate();
        sitting1.setCourtRoom(courtRoom);
        sitting1.setDay(day1);
        sittingDao.save(sitting1);

        final CourtSession session1 = new CourtSession();
        session1.setSitting(sitting1);
        session1.setPanel(panel);
        courtSessionDao.save(session1);

        final SessionBlock sessionBlock1 = new SessionBlock();
        sessionBlock1.setBlockType(SessionBlockType.TRIAL);
        sessionBlock1.setSession(session1);
        sessionBlockDao.save(sessionBlock1);

        final SittingDate sitting2 = new SittingDate();
        sitting2.setCourtRoom(courtRoom);
        sitting2.setDay(day2);
        sittingDao.save(sitting2);

        final CourtSession session2 = new CourtSession();
        session2.setSitting(sitting2);
        session2.setPanel(panel);
        courtSessionDao.save(session2);

        final SessionBlock sessionBlock2 = new SessionBlock();
        sessionBlock2.setBlockType(SessionBlockType.TRIAL);
        sessionBlock2.setSession(session2);
        sessionBlockDao.save(sessionBlock2);

        courtSessionDao.getHibernateTemplate().flush();

        final List<SessionBlock> listSessionBlock = sessionBlockDao.findSessionBlocksOrSlotByDateAndRoom(TEST_ROOM, Arrays.asList(day1, day2), SessionBlockType.TRIAL);
        assertTrue(listSessionBlock.size() > 0);

        final String hearingName = "TestHearingOne1";
        final String crestCaseNumber = "T999999";
        final BookingTypeEnum bookingType = BookingTypeEnum.PROVISIONAL;
        final CourtCenter center = new CourtCenter(1000L, TEST_CENTER);
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
        hearing.setActive(true);
        hearing.setHearingType(HearingType.TRIAL);
        hearing.setName(hearingName);
        hearing.setBookingType(bookingType);
        hearing.setBookingStatus(BookingStatusEnum.NOTBOOKED);
        hearingDao.save(hearing);

        hearingDao.getHibernateTemplate().flush();
        final Hearing unlistedHearing = hearingDao.findHearingByKey(hearing.getHearingKey());
        assertTrue(unlistedHearing != null);
        final HearingInstance hearingInstance1 = new HearingInstance();
        hearingInstance1.setBlock(sessionBlock1);
        hearingInstance1.setHearing(unlistedHearing);
        final HearingInstance hearingInstance2 = new HearingInstance();
        hearingInstance2.setBlock(sessionBlock2);
        hearingInstance2.setHearing(unlistedHearing);

        hearingInstanceDao.save(hearingInstance1);
        hearingInstanceDao.save(hearingInstance2);
        hearingInstanceDao.getHibernateTemplate().flush();

        final CourtRoom courtRoomFetched = courtRoomDao.findHearingCourtRoom(hearing.getHearingKey());
        assertTrue(courtRoomFetched.getRoomName().equals(TEST_ROOM));
    }
}
