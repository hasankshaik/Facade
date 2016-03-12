package uk.co.listing.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.listing.BaseTransactionalIntegrationTest;
import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.CourtRoom;
import uk.co.listing.domain.CourtSession;
import uk.co.listing.domain.CrustNonAvailableDates;
import uk.co.listing.domain.Hearing;
import uk.co.listing.domain.HearingInstance;
import uk.co.listing.domain.JudicialOfficer;
import uk.co.listing.domain.Panel;
import uk.co.listing.domain.PanelMember;
import uk.co.listing.domain.SessionBlock;
import uk.co.listing.domain.SittingDate;
import uk.co.listing.domain.constant.BookingStatusEnum;
import uk.co.listing.domain.constant.JudicialOfficerType;
import uk.co.listing.domain.constant.PanelMemberType;
import uk.co.listing.domain.constant.SessionBlockType;

public class SessionBlockDaoTest extends BaseTransactionalIntegrationTest {

	private static final String TEST_COURT_CENTER = "Birmingham";
	private static final String HEARING_NAME = "test-hearing-name";
	private static final String FIRST_ROOM = "FirstRoom";
	private static final String JUDICIAL_OFFICER = "CourtSessionDaoTest JudgeA";

	@Autowired
	private SessionBlockDao sessionBlockDao;

	@Autowired
	private CourtRoomDao courtRoomDao;

	@Autowired
	private JudicialOfficerDao judicialOfficerDao;

	@Autowired
	private PanelDao panelDao;

	@Autowired
	private PanelMemberDao panelMemberDao;

	@Autowired
	private SittingDao sittingDao;
	@Autowired
	private CourtSessionDao courtSessionDao;

	@Autowired
	private CaseRelatedDao caseRelatedDao;

	@Autowired
	private HearingDao hearingDao;

	@Autowired
	private HearingInstanceDao hearingInstanceDao;

	@Autowired
	private CourtCenterDao courtCenterDao;

	@Autowired
	private CourtDiaryDao courtDiaryDao;

	@Autowired
	private CourtRoomInDiaryDao courtRoomInDiaryDao;

	private final CourtCenter center = new CourtCenter(1000L, "tester-centre");

	@Test
	public void testFindSessionBlocksByDateCourtCenterAndType() {
		final Date date = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		createSessionBlock(date, SessionBlockType.PCM);
		createSessionBlock(date, SessionBlockType.TRIAL);
		List<SessionBlock> roomDates = sessionBlockDao
				.findSessionBlocksByDateCourtCenterAndType(TEST_COURT_CENTER,
						SessionBlockType.PCM, date);
		assertEquals(1, roomDates.size());
		assertEquals(SessionBlockType.PCM, roomDates.get(0).getBlockType());
		roomDates = sessionBlockDao.findSessionBlocksByDateCourtCenterAndType(
				TEST_COURT_CENTER, SessionBlockType.TRIAL, date);
		assertEquals(1, roomDates.size());
		assertEquals(SessionBlockType.TRIAL, roomDates.get(0).getBlockType());

	}

	@Test
	public void testFindSessionBlocksOrSlotByDateAndRoom() {
		final Calendar calendar1 = Calendar.getInstance();
		calendar1.set(2015, 5, 27);

		createSessionBlock(calendar1.getTime(), SessionBlockType.TRIAL);

		courtSessionDao.getHibernateTemplate().flush();

		final CourtRoom courtRoom = courtRoomDao
				.findCourtRoomByName(FIRST_ROOM);

		final List<CourtSession> listCourtSession = courtSessionDao
				.findOpenCourtRoomSittingByRoomNameBetweenDates(
						courtRoom.getId(),
						DateUtils.truncate(calendar1.getTime(), Calendar.DATE),
						DateUtils.addDays(DateUtils.truncate(
								calendar1.getTime(), Calendar.DATE), 1));
		assertTrue(listCourtSession.size() > 0);
		for (final CourtSession courtSession : listCourtSession) {
			assertTrue(courtSession
					.getSitting()
					.getDay()
					.equals(DateUtils.truncate(calendar1.getTime(),
							Calendar.DATE)));
		}
		final Set<SessionBlock> x = listCourtSession.get(0).getSessionBlock();
		for (final SessionBlock sessionBlock : x) {
			assertTrue(sessionBlock.getBlockType().equals(
					SessionBlockType.TRIAL));
		}
		final List<SessionBlock> listSessionBlock = sessionBlockDao
				.findSessionBlocksOrSlotByDateAndRoom(FIRST_ROOM, Arrays
						.asList(DateUtils.truncate(calendar1.getTime(),
								Calendar.DATE)), SessionBlockType.TRIAL);

		assertTrue(listSessionBlock.size() > 0);
	}

	@Test
	public void testFindAvailableSlotInNonAvailableDateOneDay() {

		final Date date = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		createSessionBlock(date, SessionBlockType.TRIAL);
		createSessionBlock(DateUtils.addDays(date, 1), SessionBlockType.TRIAL);
		createSessionBlock(DateUtils.addDays(date, 2), SessionBlockType.TRIAL);

		final Date dateStart = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		final Date dateEnd = new GregorianCalendar(1977, Calendar.SEPTEMBER, 3)
				.getTime();

		final CaseRelated caseRelated = createCase(null, "crestNumber100");

		final CrustNonAvailableDates crustNonAvailableDates = new CrustNonAvailableDates();
		crustNonAvailableDates.setStartDate(date);
		crustNonAvailableDates.setEndDate(date);
		crustNonAvailableDates.setCaseRelated(caseRelated);
		crustNonAvailableDates.setReason("witnesses in hospital ");
		caseRelatedDao.save(crustNonAvailableDates);

		final List<Object[]> roomDates = sessionBlockDao
				.findAvailableSessionBlockDates(TEST_COURT_CENTER, dateStart,
						dateEnd, SessionBlockType.TRIAL, caseRelated);

		assertEquals(2, roomDates.size());
		final Date dateZero = (Date) (roomDates.get(0)[1]);
		final Date dateOne = (Date) (roomDates.get(1)[1]);
		assertTrue(dateZero.before(dateOne));
		assertEquals(FIRST_ROOM, String.valueOf(roomDates.get(0)[0]));
		assertEquals(FIRST_ROOM, String.valueOf(roomDates.get(1)[0]));

	}

	@Test
	public void testFindAvailableSlotInNonAvailableDateMultipleDay() {

		final Date date = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		createSessionBlock(date, SessionBlockType.TRIAL);
		createSessionBlock(DateUtils.addDays(date, 1), SessionBlockType.TRIAL);
		createSessionBlock(DateUtils.addDays(date, 2), SessionBlockType.TRIAL);

		final Date dateStart = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		final Date dateEnd = new GregorianCalendar(1977, Calendar.SEPTEMBER, 3)
				.getTime();

		final CaseRelated caseRelated = createCase(null, "crestNumber100");

		final CrustNonAvailableDates crustNonAvailableDates1 = new CrustNonAvailableDates();
		crustNonAvailableDates1.setStartDate(date);
		crustNonAvailableDates1.setEndDate(DateUtils.addDays(date, 1));
		crustNonAvailableDates1.setCaseRelated(caseRelated);
		crustNonAvailableDates1.setReason("witnesses in hospital 1");
		caseRelatedDao.save(crustNonAvailableDates1);

		final CrustNonAvailableDates crustNonAvailableDates2 = new CrustNonAvailableDates();
		crustNonAvailableDates2.setStartDate(DateUtils.addDays(date, 2));
		crustNonAvailableDates2.setEndDate(DateUtils.addDays(date, 2));
		crustNonAvailableDates2.setCaseRelated(caseRelated);
		crustNonAvailableDates2.setReason("witnesses in hospital 2 ");
		caseRelatedDao.save(crustNonAvailableDates2);

		final List<Object[]> roomDates = sessionBlockDao
				.findAvailableSessionBlockDates(TEST_COURT_CENTER, dateStart,
						dateEnd, SessionBlockType.TRIAL, caseRelated);

		assertEquals(0, roomDates.size());

	}

	@Test
	public void shouldFindAvailableSessionBlockDatesWithinDateLimit_andSorted() {
		final Date date = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		createSessionBlock(date, SessionBlockType.TRIAL);
		createSessionBlock(DateUtils.addDays(date, 2), SessionBlockType.TRIAL);
		createSessionBlock(DateUtils.addDays(date, 4), SessionBlockType.TRIAL);

		final Date dateStart = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		final Date dateEnd = new GregorianCalendar(1977, Calendar.SEPTEMBER, 5)
				.getTime();
		final List<Object[]> roomDates = sessionBlockDao
				.findAvailableSessionBlockDates(TEST_COURT_CENTER, dateStart,
						dateEnd, SessionBlockType.TRIAL, null);

		assertEquals(3, roomDates.size());
		final Date dateZero = (Date) (roomDates.get(0)[1]);
		final Date dateOne = (Date) (roomDates.get(1)[1]);
		final Date dateTwo = (Date) (roomDates.get(2)[1]);
		assertTrue(dateZero.before(dateOne));
		assertTrue(dateOne.before(dateTwo));
		assertEquals(FIRST_ROOM, String.valueOf(roomDates.get(0)[0]));
		assertEquals(FIRST_ROOM, String.valueOf(roomDates.get(1)[0]));
		assertEquals(FIRST_ROOM, String.valueOf(roomDates.get(2)[0]));
	}

	@Test
	public void shouldNotFindAvailableSessionBlockDatesOutsideDateLimit() {
		final Date date = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		createSessionBlock(date, SessionBlockType.TRIAL);
		createSessionBlock(DateUtils.addDays(date, 2), SessionBlockType.TRIAL);
		createSessionBlock(DateUtils.addDays(date, 4), SessionBlockType.TRIAL);
		createSessionBlock(DateUtils.addMonths(date, 9), SessionBlockType.TRIAL);

		final Date dateStart = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		final Date dateEnd = new GregorianCalendar(1977, Calendar.SEPTEMBER, 5)
				.getTime();
		final List<Object[]> roomDates = sessionBlockDao
				.findAvailableSessionBlockDates(TEST_COURT_CENTER, dateStart,
						dateEnd, SessionBlockType.TRIAL, null);

		assertEquals(3, roomDates.size());
	}

	@Test
	public void shouldReturnSessionDateHaving_BlockTypeTrial_only() {
		final Date date = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		createSessionBlock(date, SessionBlockType.APPEALCONVICTION);
		createSessionBlock(DateUtils.addDays(date, 1),
				SessionBlockType.APPEALSENTENCE);
		createSessionBlock(DateUtils.addDays(date, 2), SessionBlockType.TRIAL);
		createSessionBlock(DateUtils.addDays(date, 4), SessionBlockType.TRIAL);

		final Date dateStart = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		final Date dateEnd = new GregorianCalendar(1977, Calendar.SEPTEMBER, 5)
				.getTime();
		final List<Object[]> roomDates = sessionBlockDao
				.findAvailableSessionBlockDates(TEST_COURT_CENTER, dateStart,
						dateEnd, SessionBlockType.TRIAL, null);

		assertEquals(2, roomDates.size());
	}

	@Test
	public void shouldReturnOnlyOneDateNotHavingHearing() {
		final Date date = new GregorianCalendar(1977, Calendar.JANUARY, 2)
				.getTime();
		createSessionBlock(date, SessionBlockType.TRIAL);
		final SessionBlock sessionBlock = createSessionBlock(
				DateUtils.addDays(date, 1), SessionBlockType.TRIAL);

		createAndAttachHearingToSession(sessionBlock, HEARING_NAME);

		final Date dateStart = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		final Date dateEnd = new GregorianCalendar(1977, Calendar.SEPTEMBER, 5)
				.getTime();
		final List<Object[]> roomDates = sessionBlockDao
				.findAvailableSessionBlockDates(TEST_COURT_CENTER, dateStart,
						dateEnd, SessionBlockType.TRIAL, null);

		assertEquals(1, roomDates.size());
		assertEquals(date, (roomDates.get(0)[1]));
	}

	@Test
	public void shouldReturnOnlyOneDateNotHavingHearingMatchingJudge() {
		final Date date = new GregorianCalendar(1977, Calendar.JANUARY, 2)
				.getTime();
		final JudicialOfficer judge = new JudicialOfficer(JUDICIAL_OFFICER,
				JudicialOfficerType.CIRCUIT, true);
		judicialOfficerDao.save(judge);
		createSessionBlockWithJudge(date, SessionBlockType.TRIAL, judge);
		final SessionBlock sessionBlock = createSessionBlockWithJudge(
				DateUtils.addDays(date, 1), SessionBlockType.TRIAL, judge);

		CaseRelated caseRelated = createCase(judge, "T11111");

		createAndAttachHearingToSession(sessionBlock, HEARING_NAME);

		final Date dateStart = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		final Date dateEnd = new GregorianCalendar(1977, Calendar.SEPTEMBER, 5)
				.getTime();
		final List<Object[]> roomDates = sessionBlockDao
				.findAvailableSessionBlockDatesWithJudge(TEST_COURT_CENTER,
						dateStart, dateEnd, SessionBlockType.TRIAL, caseRelated);

		assertEquals(1, roomDates.size());
		assertEquals(date, (roomDates.get(0)[1]));
	}

	@Test
	public void shouldReturnSessionDatesWith_one_OverBooking_withExistingHearings() {
		final Date date = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		final SessionBlock createSessionBlock = createSessionBlock(date,
				SessionBlockType.TRIAL);
		final SessionBlock createSessionBlock2 = createSessionBlock(
				DateUtils.addDays(date, 2), SessionBlockType.TRIAL);

		createAndAttachHearingToSession(createSessionBlock, HEARING_NAME + "-1");
		createAndAttachHearingToSession(createSessionBlock2, HEARING_NAME
				+ "-2");

		final Date dateStart = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		final Date dateEnd = new GregorianCalendar(1977, Calendar.SEPTEMBER, 5)
				.getTime();
		final List<Object[]> roomDatesHear = sessionBlockDao
				.findAvailableSessionBlockDatesWithOverbook(TEST_COURT_CENTER,
						dateStart, dateEnd, SessionBlockType.TRIAL, 10, null);

		assertEquals(2, roomDatesHear.size());

		assertEquals(FIRST_ROOM, roomDatesHear.get(0)[0]);
		assertEquals(date, roomDatesHear.get(0)[1]);
		assertEquals(1L, roomDatesHear.get(0)[2]);

		assertEquals(FIRST_ROOM, roomDatesHear.get(1)[0]);
		assertEquals(DateUtils.addDays(date, 2), roomDatesHear.get(1)[1]);
		assertEquals(1L, roomDatesHear.get(1)[2]);
	}

	@Test
	public void shouldReturnSessionDatesWith_one_OverBooking_withExistingHearingMatchingJudge() {
		final JudicialOfficer judge = new JudicialOfficer(JUDICIAL_OFFICER,
				JudicialOfficerType.CIRCUIT, true);
		judicialOfficerDao.save(judge);
		final Date date = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		final SessionBlock createSessionBlock = createSessionBlockWithJudge(
				date, SessionBlockType.TRIAL, judge);
		final SessionBlock createSessionBlock2 = createSessionBlockWithJudge(
				DateUtils.addDays(date, 2), SessionBlockType.TRIAL, judge);

		createAndAttachHearingToSession(createSessionBlock, HEARING_NAME + "-1");
		createAndAttachHearingToSession(createSessionBlock2, HEARING_NAME
				+ "-2");

		CaseRelated caseRelated = createCase(judge, "T11112");

		final Date dateStart = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		final Date dateEnd = new GregorianCalendar(1977, Calendar.SEPTEMBER, 5)
				.getTime();
		final List<Object[]> roomDatesHear = sessionBlockDao
				.findAvailableSessionBlockDatesWithOverbookAndJudge(
						TEST_COURT_CENTER, dateStart, dateEnd,
						SessionBlockType.TRIAL, 10, caseRelated);

		assertEquals(2, roomDatesHear.size());

		assertEquals(FIRST_ROOM, roomDatesHear.get(0)[0]);
		assertEquals(date, roomDatesHear.get(0)[1]);
		assertEquals(1L, roomDatesHear.get(0)[2]);

		assertEquals(FIRST_ROOM, roomDatesHear.get(1)[0]);
		assertEquals(DateUtils.addDays(date, 2), roomDatesHear.get(1)[1]);
		assertEquals(1L, roomDatesHear.get(1)[2]);
	}

	@Test
	public void findOverBooking_withExistingHearingsAndNonAvailability() {
		final Date date = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		final SessionBlock createSessionBlock = createSessionBlock(date,
				SessionBlockType.TRIAL);
		final SessionBlock createSessionBlock2 = createSessionBlock(
				DateUtils.addDays(date, 2), SessionBlockType.TRIAL);

		createAndAttachHearingToSession(createSessionBlock, HEARING_NAME + "-1");
		createAndAttachHearingToSession(createSessionBlock2, HEARING_NAME
				+ "-2");

		final Date dateStart = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		final Date dateEnd = new GregorianCalendar(1977, Calendar.SEPTEMBER, 5)
				.getTime();

		final CourtCenter courtCenter = courtCenterDao
				.findCourtCentreByName(TEST_COURT_CENTER);

		final CaseRelated caseRelated = new CaseRelated();
		caseRelated.setCrestCaseNumber("crestNumber100");
		caseRelated.setCourtCenter(courtCenter);
		caseRelatedDao.save(caseRelated);

		final CrustNonAvailableDates crustNonAvailableDates = new CrustNonAvailableDates();
		crustNonAvailableDates.setStartDate(date);
		crustNonAvailableDates.setEndDate(DateUtils.addDays(date, 2));
		crustNonAvailableDates.setCaseRelated(caseRelated);
		crustNonAvailableDates.setReason("witnesses in hospital ");
		caseRelatedDao.save(crustNonAvailableDates);

		final List<Object[]> roomDatesHear = sessionBlockDao
				.findAvailableSessionBlockDatesWithOverbook(TEST_COURT_CENTER,
						dateStart, dateEnd, SessionBlockType.TRIAL, 10,
						caseRelated);

		assertEquals(0, roomDatesHear.size());

	}

	@Test
	public void shouldReturnSessionDatesWith_one_and_two_OverBooking_withExistingHearings() {
		final Date date = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		final SessionBlock createSessionBlock = createSessionBlock(date,
				SessionBlockType.TRIAL);
		final SessionBlock createSessionBlock2 = createSessionBlock(
				DateUtils.addDays(date, 2), SessionBlockType.TRIAL);

		// two hearings in same block
		createAndAttachHearingToSession(createSessionBlock, HEARING_NAME
				+ "-1.1");
		createAndAttachHearingToSession(createSessionBlock, HEARING_NAME
				+ "-1.2");
		// one hearing in the second block
		createAndAttachHearingToSession(createSessionBlock2, HEARING_NAME
				+ "-2.1");

		final Date dateStart = new GregorianCalendar(1977, Calendar.JANUARY, 1)
				.getTime();
		final Date dateEnd = new GregorianCalendar(1977, Calendar.SEPTEMBER, 5)
				.getTime();
		final List<Object[]> roomDatesHear = sessionBlockDao
				.findAvailableSessionBlockDatesWithOverbook(TEST_COURT_CENTER,
						dateStart, dateEnd, SessionBlockType.TRIAL, 10, null);

		assertEquals(2, roomDatesHear.size());

		// the least overbooking dates should appear on top
		// 3rd january on top with one over booking
		assertEquals(FIRST_ROOM, roomDatesHear.get(0)[0]); // room-name
		assertEquals(DateUtils.addDays(date, 2), roomDatesHear.get(0)[1]); // session-date
		assertEquals(1L, roomDatesHear.get(0)[2]); // overbooking count

		// 1st january at bottom with two overbookings
		assertEquals(FIRST_ROOM, roomDatesHear.get(1)[0]);
		assertEquals(date, roomDatesHear.get(1)[1]);
		assertEquals(2L, roomDatesHear.get(1)[2]);
	}

	private void createAndAttachHearingToSession(
			final SessionBlock sessionBlock, final String hearingName) {
		courtCenterDao.save(center);
		final CaseRelated caseRelated = new CaseRelated();
		caseRelated.setCourtCenter(center);
		caseRelatedDao.save(caseRelated);

		final Hearing hearing = new Hearing();
		hearing.setCaseRelated(caseRelated);
		hearing.setName(hearingName);
		hearing.setBookingStatus(BookingStatusEnum.NOTBOOKED);
		hearing.setDaysEstimated(4.0);
		hearing.setActive(true);
		hearingDao.save(hearing);

		final HashSet<HearingInstance> hearingInstances = new HashSet<HearingInstance>();
		final HearingInstance hearInst = new HearingInstance();
		hearInst.setBlock(sessionBlock);
		hearInst.setHearing(hearing);
		hearingInstanceDao.save(hearInst);

		hearingInstances.add(hearInst);
		sessionBlock.setHearings(hearingInstances);
	}

	private SessionBlock createSessionBlock(final Date sittingDate,
			final SessionBlockType blockType) {
		final CourtRoom courtRoom = courtRoomDao
				.findCourtRoomByName(FIRST_ROOM);

		final SittingDate sitDate = new SittingDate();
		sitDate.setCourtRoom(courtRoom);
		sitDate.setDay(DateUtils.truncate(sittingDate, Calendar.DATE));
		sittingDao.save(sitDate);

		final Panel panel = new Panel();
		panel.setDescription("Panel for " + JUDICIAL_OFFICER);
		panelDao.save(panel);

		Random random = new Random();
		JudicialOfficer judge = new JudicialOfficer(JUDICIAL_OFFICER
				+ random.nextInt(), JudicialOfficerType.CIRCUIT, false);
		judicialOfficerDao.save(judge);

		PanelMember panelMember = new PanelMember();
		panelMember.setJudicialOfficer(judge);
		panelMember.setPanel(panel);
		panelMember.setPanelMemberType(PanelMemberType.JUDGE);
		panelMemberDao.save(panelMember);

		final CourtSession session = new CourtSession();
		session.setSitting(sitDate);
		session.setPanel(panel);
		courtSessionDao.save(session);

		final SessionBlock block = new SessionBlock();
		block.setBlockType(blockType);
		block.setSession(session);
		sessionBlockDao.save(block);
		return block;
	}

	private SessionBlock createSessionBlockWithJudge(final Date sittingDate,
			final SessionBlockType blockType, JudicialOfficer judge) {
		final CourtRoom courtRoom = courtRoomDao
				.findCourtRoomByName(FIRST_ROOM);

		final SittingDate sitDate = new SittingDate();
		sitDate.setCourtRoom(courtRoom);
		sitDate.setDay(DateUtils.truncate(sittingDate, Calendar.DATE));
		sittingDao.save(sitDate);

		final Panel panel = new Panel();
		panel.setDescription("Panel for " + JUDICIAL_OFFICER);
		panelDao.save(panel);

		PanelMember panelMember = new PanelMember();
		panelMember.setJudicialOfficer(judge);
		panelMember.setPanel(panel);
		panelMember.setPanelMemberType(PanelMemberType.JUDGE);
		panelMemberDao.save(panelMember);

		final CourtSession session = new CourtSession();
		session.setSitting(sitDate);
		session.setPanel(panel);
		courtSessionDao.save(session);

		final SessionBlock block = new SessionBlock();
		block.setBlockType(blockType);
		block.setSession(session);
		sessionBlockDao.save(block);
		return block;
	}

	private CaseRelated createCase(JudicialOfficer judge, String crestNumber) {
		CourtCenter center = courtCenterDao
				.findCourtCentreByName(TEST_COURT_CENTER);
		CaseRelated caseRelated = new CaseRelated();
		caseRelated.setCrestCaseNumber(crestNumber);
		caseRelated.setCourtCenter(center);
		caseRelated.setJudge(judge);

		caseRelatedDao.save(caseRelated);

		return caseRelated;
	}
}
