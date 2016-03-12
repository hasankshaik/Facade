package uk.co.listing.dao;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.listing.BaseTransactionalIntegrationTest;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.CourtDiary;
import uk.co.listing.domain.CourtRoom;
import uk.co.listing.domain.CourtRoomInDiary;
import uk.co.listing.domain.CourtSession;
import uk.co.listing.domain.JudicialOfficer;
import uk.co.listing.domain.Panel;
import uk.co.listing.domain.PanelMember;
import uk.co.listing.domain.SittingDate;
import uk.co.listing.domain.constant.JudicialOfficerType;
import uk.co.listing.domain.constant.PanelMemberType;

public class SittingDaoTest extends BaseTransactionalIntegrationTest {

	private static final String TEST_CENTER = "CourtSessionDaoTest-center";
	private static final String TEST_ROOM = "CourtSessionDaoTest-room";
	private static final String JUDICIAL_OFFICER = "CourtSessionDaoTest JudgeA";

	@Autowired
	private CourtSessionDao courtSessionDao;

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
	private CourtCenterDao courtCenterDao;

	@Autowired
	private CourtDiaryDao courtDiaryDao;

	@Autowired
	private CourtRoomInDiaryDao courtRoomInDiaryDao;

	@Test
	public void testCountActualSittingDaysBetweenDatesInTheYear() {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(2015, 3, 1);
		Date day1 = calendar1.getTime();

		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(2016, 2, 31);
		Date day2 = calendar2.getTime();

		Calendar calendar3 = Calendar.getInstance();
		calendar3.set(2015, 9, 14);
		Date day3 = calendar3.getTime();

		long preSize = sittingDao.countActualSittingDaysBetweenDates(
				TEST_CENTER, day1, day2);

		courtCenterDao.save(new CourtCenter(1010L, TEST_CENTER));
		CourtCenter center = courtCenterDao.findCourtCentreByName(TEST_CENTER);

		CourtDiary courtDiary = new CourtDiary();
		courtDiary.setCenter(center);
		courtDiaryDao.save(courtDiary);

		courtRoomDao.save(new CourtRoom(TEST_ROOM));
		CourtRoom courtRoom = courtRoomDao.findCourtRoomByName(TEST_ROOM);

		CourtRoomInDiary courtRoomInDiary = new CourtRoomInDiary();
		courtRoomInDiary.setCourtRoom(courtRoom);
		courtRoomInDiary.setCourtDiary(courtDiary);
		courtRoomInDiaryDao.save(courtRoomInDiary);

		final JudicialOfficer judicialOfficer = new JudicialOfficer(
				JUDICIAL_OFFICER, JudicialOfficerType.CIRCUIT, true);
		judicialOfficerDao.save(judicialOfficer);

		final Panel panel = new Panel();
		panel.setDescription("Panel for " + JUDICIAL_OFFICER);
		panelDao.save(panel);

		final PanelMember panelMember = new PanelMember();
		panelMember.setPanel(panel);
		panelMember.setJudicialOfficer(judicialOfficer);
		panelMember.setPanelMemberType(PanelMemberType.JUDGE);
		panelMemberDao.save(panelMember);

		SittingDate sitting = new SittingDate();
		sitting.setCourtRoom(courtRoom);
		sitting.setDay(day3);
		sittingDao.save(sitting);

		CourtSession session = new CourtSession();
		session.setSitting(sitting);
		session.setPanel(panel);
		courtSessionDao.save(session);

		// We are flushing here because the native query is not a proper get
		// from the hibernate session, so it is not persisting the previous save
		courtSessionDao.getHibernateTemplate().flush();

		long postSize = sittingDao.countActualSittingDaysBetweenDates(
				TEST_CENTER, day1, day2);
		log.info("pre size:" + preSize);
		log.info("post size:" + postSize);

		assertTrue(postSize == preSize + 1);
	}

	@Test
	public void testCountActualSittingDaysBetweenDatesInOtherYear() {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(2015, 3, 1);
		Date day1 = calendar1.getTime();

		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(2016, 2, 31);
		Date day2 = calendar2.getTime();

		Calendar calendar3 = Calendar.getInstance();
		calendar3.set(2017, 9, 6);
		Date day3 = calendar3.getTime();

		long preSize = sittingDao.countActualSittingDaysBetweenDates(
				TEST_CENTER, day1, day2);

		courtCenterDao.save(new CourtCenter(2020L, TEST_CENTER));
		CourtCenter center = courtCenterDao.findCourtCentreByName(TEST_CENTER);

		CourtDiary courtDiary = new CourtDiary();
		courtDiary.setCenter(center);
		courtDiaryDao.save(courtDiary);

		courtRoomDao.save(new CourtRoom(TEST_ROOM));
		CourtRoom courtRoom = courtRoomDao.findCourtRoomByName(TEST_ROOM);

		CourtRoomInDiary courtRoomInDiary = new CourtRoomInDiary();
		courtRoomInDiary.setCourtRoom(courtRoom);
		courtRoomInDiary.setCourtDiary(courtDiary);
		courtRoomInDiaryDao.save(courtRoomInDiary);

		final JudicialOfficer judicialOfficer = new JudicialOfficer(
				JUDICIAL_OFFICER, JudicialOfficerType.CIRCUIT, true);
		judicialOfficerDao.save(judicialOfficer);

		final Panel panel = new Panel();
		panel.setDescription("Panel for " + JUDICIAL_OFFICER);
		panelDao.save(panel);

		final PanelMember panelMember = new PanelMember();
		panelMember.setPanel(panel);
		panelMember.setJudicialOfficer(judicialOfficer);
		panelMember.setPanelMemberType(PanelMemberType.JUDGE);
		panelMemberDao.save(panelMember);

		SittingDate sitting = new SittingDate();
		sitting.setCourtRoom(courtRoom);
		sitting.setDay(day3);
		sittingDao.save(sitting);

		CourtSession session = new CourtSession();
		session.setSitting(sitting);
		session.setPanel(panel);
		courtSessionDao.save(session);

		// We are flushing here because the native query is not a proper get
		// from the hibernate session, so it is not persisting the previous save
		courtSessionDao.getHibernateTemplate().flush();

		long postSize = sittingDao.countActualSittingDaysBetweenDates(
				TEST_CENTER, day1, day2);
		log.info("pre size:" + preSize);
		log.info("post size:" + postSize);

		assertTrue(postSize == preSize);
	}

	@Test
	public void testTwoCountActualSittingDaysBetweenDatesInYear() {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(2015, 3, 1);
		Date day1 = calendar1.getTime();

		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(2016, 2, 31);
		Date day2 = calendar2.getTime();

		Calendar calendar3 = Calendar.getInstance();
		calendar3.set(2015, 9, 6);
		Date day3 = calendar3.getTime();

		Calendar calendar4 = Calendar.getInstance();
		calendar4.set(2015, 9, 5);
		Date day4 = calendar4.getTime();

		long preSize = sittingDao.countActualSittingDaysBetweenDates(
				TEST_CENTER, day1, day2);

		courtCenterDao.save(new CourtCenter(3030L, TEST_CENTER));
		CourtCenter center = courtCenterDao.findCourtCentreByName(TEST_CENTER);

		CourtDiary courtDiary = new CourtDiary();
		courtDiary.setCenter(center);
		courtDiaryDao.save(courtDiary);

		courtRoomDao.save(new CourtRoom(TEST_ROOM));
		CourtRoom courtRoom = courtRoomDao.findCourtRoomByName(TEST_ROOM);

		CourtRoomInDiary courtRoomInDiary = new CourtRoomInDiary();
		courtRoomInDiary.setCourtRoom(courtRoom);
		courtRoomInDiary.setCourtDiary(courtDiary);
		courtRoomInDiaryDao.save(courtRoomInDiary);

		final JudicialOfficer judicialOfficer = new JudicialOfficer(
				JUDICIAL_OFFICER, JudicialOfficerType.CIRCUIT, true);
		judicialOfficerDao.save(judicialOfficer);

		final Panel panel = new Panel();
		panel.setDescription("Panel for " + JUDICIAL_OFFICER);
		panelDao.save(panel);

		final PanelMember panelMember = new PanelMember();
		panelMember.setPanel(panel);
		panelMember.setJudicialOfficer(judicialOfficer);
		panelMember.setPanelMemberType(PanelMemberType.JUDGE);
		panelMemberDao.save(panelMember);

		SittingDate sitting = new SittingDate();
		sitting.setCourtRoom(courtRoom);
		sitting.setDay(day3);
		sittingDao.save(sitting);

		CourtSession session = new CourtSession();
		session.setSitting(sitting);
		session.setPanel(panel);
		courtSessionDao.save(session);

		Panel panelTwo = new Panel();
		panel.setDescription("Panel2 for " + JUDICIAL_OFFICER);
		panelDao.save(panelTwo);

		PanelMember panelMemberTwo = new PanelMember();
		panelMemberTwo.setPanel(panelTwo);
		panelMemberTwo.setJudicialOfficer(judicialOfficer);
		panelMemberTwo.setPanelMemberType(PanelMemberType.JUDGE);
		panelMemberDao.save(panelMemberTwo);

		SittingDate sittingTwo = new SittingDate();
		sittingTwo.setCourtRoom(courtRoom);
		sittingTwo.setDay(day4);
		sittingDao.save(sittingTwo);

		CourtSession sessionTwo = new CourtSession();
		sessionTwo.setSitting(sittingTwo);
		sessionTwo.setPanel(panelTwo);
		courtSessionDao.save(sessionTwo);

		// We are flushing here because the native query is not a proper get
		// from the hibernate session, so it is not persisting the previous save
		courtSessionDao.getHibernateTemplate().flush();

		long postSize = sittingDao.countActualSittingDaysBetweenDates(
				TEST_CENTER, day1, day2);
		log.info("pre size:" + preSize);
		log.info("post size:" + postSize);

		assertTrue(postSize == preSize + 2);
	}

}
