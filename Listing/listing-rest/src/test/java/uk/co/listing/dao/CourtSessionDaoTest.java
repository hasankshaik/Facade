package uk.co.listing.dao;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.listing.BaseTransactionalIntegrationTest;
import uk.co.listing.domain.CourtRoom;
import uk.co.listing.domain.CourtSession;
import uk.co.listing.domain.JudicialOfficer;
import uk.co.listing.domain.Panel;
import uk.co.listing.domain.PanelMember;
import uk.co.listing.domain.SittingDate;
import uk.co.listing.domain.constant.JudicialOfficerType;
import uk.co.listing.domain.constant.PanelMemberType;

public class CourtSessionDaoTest extends BaseTransactionalIntegrationTest {

    // private static final String TEST_CENTER = "CourtSessionDaoTest-center";
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

    @Test
    public void testFindCourtRoomSittingByCenterNameBetweenDates() {
        final Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2015, 9, 12);
        final Date startDay = calendar1.getTime();
        final Date endDay = DateUtils.addDays(startDay, 3) ;
        final Date sittingDay1 = DateUtils.addDays(startDay, 1) ;
        final Date sittingDay2 = DateUtils.addDays(startDay, 2) ;

        courtRoomDao.save(new CourtRoom(TEST_ROOM));
        final CourtRoom courtRoom = courtRoomDao.findCourtRoomByName(TEST_ROOM);
        final int preSize = courtSessionDao.findOpenCourtRoomSittingByRoomNameBetweenDates(courtRoom.getId(), startDay, endDay).size();

        createSessions(sittingDay1, sittingDay2, courtRoom);

        final int postSize = courtSessionDao.findOpenCourtRoomSittingByRoomNameBetweenDates(courtRoom.getId(), startDay, endDay).size();
        log.info("pre size:" + preSize);
        log.info("post size:" + postSize);

        assertTrue(postSize == (preSize + 1));
    }


    private void createSessions(final Date sittingDay1, final Date sittingDay2, final CourtRoom courtRoom) {
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

        final SittingDate sitting = new SittingDate();
        sitting.setCourtRoom(courtRoom);
        sitting.setDay(sittingDay1);
        sittingDao.save(sitting);

        final CourtSession session = new CourtSession();
        session.setSitting(sitting);
        session.setPanel(panel);
        courtSessionDao.save(session);

        final SittingDate sitting1 = new SittingDate();
        sitting1.setCourtRoom(courtRoom);
        sitting1.setDay(sittingDay2);
        sittingDao.save(sitting1);

        final CourtSession session1 = new CourtSession();
        session1.setSitting(sitting1);
        session1.setPanel(panel);
        session1.setIsClosed(true);
        courtSessionDao.save(session1);
    }


    @Test
    public void testFindOpenAndClosedSitting() {
        final Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2015, 9, 12);
        final Date startDay = calendar1.getTime();
        final Date endDay = DateUtils.addDays(startDay, 3) ;
        final Date sittingDay1 = DateUtils.addDays(startDay, 1) ;
        final Date sittingDay2 = DateUtils.addDays(startDay, 2) ;

        courtRoomDao.save(new CourtRoom(TEST_ROOM));
        final CourtRoom courtRoom = courtRoomDao.findCourtRoomByName(TEST_ROOM);
        final int preSize = courtSessionDao.findOpenCourtRoomSittingByRoomNameBetweenDates(courtRoom.getId(), startDay, endDay).size();

        createSessions(sittingDay1, sittingDay2, courtRoom);

        final int postSize = courtSessionDao.findOpenAndClosedSessionsForSittings(courtRoom.getId(), startDay, endDay).size();
        log.info("pre size:" + preSize);
        log.info("post size:" + postSize);

        assertTrue(postSize == (preSize + 2));
    }
}
