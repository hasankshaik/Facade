package uk.co.listing.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import uk.co.listing.BaseMockingUnitTest;
import uk.co.listing.dao.CourtCenterDao;
import uk.co.listing.dao.CourtDiaryDao;
import uk.co.listing.dao.CourtRoomDao;
import uk.co.listing.dao.CourtRoomInDiaryDao;
import uk.co.listing.dao.CourtSessionDao;
import uk.co.listing.dao.JudicialOfficerDao;
import uk.co.listing.dao.PanelDao;
import uk.co.listing.dao.PanelMemberDao;
import uk.co.listing.dao.SessionBlockDao;
import uk.co.listing.dao.SittingDao;
import uk.co.listing.dao.SittingTargetDao;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.CourtDiary;
import uk.co.listing.domain.CourtRoom;
import uk.co.listing.domain.CourtRoomInDiary;
import uk.co.listing.domain.CourtSession;
import uk.co.listing.domain.HearingInstance;
import uk.co.listing.domain.JudicialOfficer;
import uk.co.listing.domain.MonthlyTarget;
import uk.co.listing.domain.Panel;
import uk.co.listing.domain.PanelMember;
import uk.co.listing.domain.SessionBlock;
import uk.co.listing.domain.SittingDate;
import uk.co.listing.domain.SittingTarget;
import uk.co.listing.domain.constant.JudicialOfficerType;
import uk.co.listing.domain.constant.SessionBlockType;
import uk.co.listing.exceptions.CcsException;
import uk.co.listing.rest.response.CourtRoomWeb;
import uk.co.listing.rest.response.CourtSessionSaveWeb;
import uk.co.listing.rest.response.JudicialOfficerWeb;
import uk.co.listing.rest.response.ManageSessionAction;

public class ListingAdminServiceTest extends BaseMockingUnitTest {

    private final static String testJudge1 = "testJudgeone";
    private final static String testJudge2 = "testJudgetwo";
    private final static String falseCourt = "Torrijos 12221";
    private final static String workingDate = "09/10/2015";
    private final static String workingDate2 = "14/10/2015";
    private final static String nonWorkingDate = "11/10/2015";
    private final static String blockType = "Trial";

    @Mock
    JudicialOfficerDao judicialOfficerDaoMock;

    @Mock
    SittingTargetDao mockSittingTargetDao;

    @Mock
    CourtCenterDao mockCourtCenterDao;

    @Mock
    CourtRoomDao courtRoomDaoMock;

    @Mock
    PanelDao panelDaoMock;

    @Mock
    PanelMemberDao panelMemberDaoMock;

    @Mock
    SittingDao sittingDaoMock;

    @Mock
    CourtSessionDao courtSessionDaoMock;

    @Mock
    CourtDiaryDao courtDiaryDaoMock;

    @Mock
    SessionBlockDao sessionBlockDaoMock;

    @Mock
    CourtRoomInDiaryDao courtRoomInDiaryDao;

    @InjectMocks
    ListingAdminService listingAdminService;

    @Test
    public void testFindJudicialOfficerByName() {
        when(judicialOfficerDaoMock.findJudicialOfficerByName(testJudge1)).thenReturn(new JudicialOfficer(testJudge1, JudicialOfficerType.CIRCUIT, true));
        Assert.assertEquals(testJudge1, listingAdminService.findJudicialOfficerByName(testJudge1).getFullname());
    }

    @Test
    public void testFindJudicialOfficerByNameNull() {
        when(judicialOfficerDaoMock.findJudicialOfficerByName(testJudge2)).thenReturn(null);
        Assert.assertNull(listingAdminService.findJudicialOfficerByName(testJudge2));
    }

    @Test
    public void testSaveJudicialOfficer() {
        final JudicialOfficerWeb judicialOfficerWeb = new JudicialOfficerWeb(testJudge1, JudicialOfficerType.CIRCUIT.getDescription(), true, true);
        judicialOfficerWeb.getJudicialTickets().add("Murder");
        judicialOfficerWeb.getJudicialTickets().add("Attempted Murder");
        listingAdminService.saveJudicialOfficer(judicialOfficerWeb);
        verify(judicialOfficerDaoMock, times(1)).save(any(JudicialOfficer.class));
    }

    @Test
    public void testSaveJudicialOfficerPatternMismatch() {

        JudicialOfficerWeb judicialOfficerWeb = new JudicialOfficerWeb("Judge1", JudicialOfficerType.CIRCUIT.getDescription(), true, true);
        try {
            listingAdminService.saveJudicialOfficer(judicialOfficerWeb);
            fail();
        } catch (final Exception e) {
            assertTrue(e.getMessage().startsWith("Invalid name: "));
        }

        judicialOfficerWeb = new JudicialOfficerWeb("", JudicialOfficerType.CIRCUIT.getDescription(), true, true);
        try {
            listingAdminService.saveJudicialOfficer(judicialOfficerWeb);
            fail();
        } catch (final Exception e) {
            assertTrue(e.getMessage().startsWith("Invalid name: "));
        }

        judicialOfficerWeb = new JudicialOfficerWeb("Judge #", JudicialOfficerType.CIRCUIT.getDescription(), true, true);
        try {
            listingAdminService.saveJudicialOfficer(judicialOfficerWeb);
            fail();
        } catch (final Exception e) {
            assertTrue(e.getMessage().startsWith("Invalid name: "));
        }

        judicialOfficerWeb = new JudicialOfficerWeb("Judge £", JudicialOfficerType.CIRCUIT.getDescription(), true, true);
        try {
            listingAdminService.saveJudicialOfficer(judicialOfficerWeb);
            fail();
        } catch (final Exception e) {
            assertTrue(e.getMessage().startsWith("Invalid name: "));
        }

        verify(judicialOfficerDaoMock, times(0)).save(any(JudicialOfficer.class));
    }

    @Test
    public void testSaveJudicialOfficerPatternMatch() {
        when(judicialOfficerDaoMock.findJudicialOfficerByName(any(String.class))).thenReturn(null);

        JudicialOfficerWeb judicialOfficerWeb = new JudicialOfficerWeb("Judge-One", JudicialOfficerType.CIRCUIT.getDescription(), false, false);
        listingAdminService.saveJudicialOfficer(judicialOfficerWeb);

        judicialOfficerWeb = new JudicialOfficerWeb("first last", JudicialOfficerType.DEPUTY.getDescription(), true, true);
        listingAdminService.saveJudicialOfficer(judicialOfficerWeb);

        judicialOfficerWeb = new JudicialOfficerWeb("first-name. last'name", JudicialOfficerType.CIRCUIT.getDescription(), true, true);
        listingAdminService.saveJudicialOfficer(judicialOfficerWeb);

        verify(judicialOfficerDaoMock, times(3)).save(any(JudicialOfficer.class));
        verify(judicialOfficerDaoMock, times(3)).findJudicialOfficerByName(any(String.class));
    }

    @Test(expected = RuntimeException.class)
    public void testSaveJudicialOfficerNameExist() {
        when(judicialOfficerDaoMock.findJudicialOfficerByName(any(String.class))).thenReturn(new JudicialOfficer(testJudge1, JudicialOfficerType.CIRCUIT, true));
        final JudicialOfficerWeb judicialOfficerWeb = new JudicialOfficerWeb("Judge-One", JudicialOfficerType.CIRCUIT.getDescription(), true, true);
        listingAdminService.saveJudicialOfficer(judicialOfficerWeb);

    }

    @Test
    public void testFindAllJudicialOfficers() {
        when(judicialOfficerDaoMock.findAll()).thenReturn(
                Arrays.asList(new JudicialOfficer(testJudge1, JudicialOfficerType.CIRCUIT, true, false), new JudicialOfficer(testJudge1, JudicialOfficerType.CIRCUIT, true, true), new JudicialOfficer(
                        testJudge2, JudicialOfficerType.DEPUTY, false)));
        Assert.assertEquals(3, listingAdminService.findAllJudicialOfficers().size());
    }

    @Test(expected = RuntimeException.class)
    public void allocateJudgeNonWorkingStartDate() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(nonWorkingDate, workingDate, falseCourt, testJudge1);
        final CourtSessionSaveWeb result = listingAdminService.allocateJudgeToCourtRoom(courtSessionWeb);
        Assert.assertTrue(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).save(any(Panel.class));
        verify(panelMemberDaoMock, times(0)).save(any(PanelMember.class));
        verify(sittingDaoMock, times(0)).save(any(SittingDate.class));
        verify(courtSessionDaoMock, times(0)).save(any(CourtSession.class));
        verify(sessionBlockDaoMock, times(0)).save(any(SessionBlock.class));
    }

    @Test(expected = RuntimeException.class)
    public void allocateJudgeNonWorkingEndDate() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate, nonWorkingDate, falseCourt, testJudge1);
        final CourtSessionSaveWeb result = listingAdminService.allocateJudgeToCourtRoom(courtSessionWeb);
        Assert.assertTrue(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).save(any(Panel.class));
        verify(panelMemberDaoMock, times(0)).save(any(PanelMember.class));
        verify(sittingDaoMock, times(0)).save(any(SittingDate.class));
        verify(courtSessionDaoMock, times(0)).save(any(CourtSession.class));
        verify(sessionBlockDaoMock, times(0)).save(any(SessionBlock.class));
    }

    @Test(expected = RuntimeException.class)
    public void allocateJudgeStartDateAfterEndDate() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate2, workingDate, falseCourt, testJudge1);
        final CourtSessionSaveWeb result = listingAdminService.allocateJudgeToCourtRoom(courtSessionWeb);
        Assert.assertTrue(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).save(any(Panel.class));
        verify(panelMemberDaoMock, times(0)).save(any(PanelMember.class));
        verify(sittingDaoMock, times(0)).save(any(SittingDate.class));
        verify(courtSessionDaoMock, times(0)).save(any(CourtSession.class));
        verify(sessionBlockDaoMock, times(0)).save(any(SessionBlock.class));
    }

    @Test(expected = RuntimeException.class)
    public void allocateJudgeNoCourtRoom() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate, workingDate, falseCourt, testJudge1);
        when(judicialOfficerDaoMock.findJudicialOfficerByName(testJudge1)).thenReturn(new JudicialOfficer(testJudge1, JudicialOfficerType.CIRCUIT, true));
        final CourtSessionSaveWeb result = listingAdminService.allocateJudgeToCourtRoom(courtSessionWeb);
        Assert.assertTrue(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).save(any(Panel.class));
        verify(panelMemberDaoMock, times(0)).save(any(PanelMember.class));
        verify(sittingDaoMock, times(0)).save(any(SittingDate.class));
        verify(courtSessionDaoMock, times(0)).save(any(CourtSession.class));
        verify(sessionBlockDaoMock, times(0)).save(any(SessionBlock.class));
    }

    @Test(expected = RuntimeException.class)
    public void allocateJudgeNoJudge() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate, workingDate, falseCourt, testJudge1);
        when(courtRoomDaoMock.findCourtRoomByName(any(String.class))).thenReturn(new CourtRoom(falseCourt));
        final CourtSessionSaveWeb result = listingAdminService.allocateJudgeToCourtRoom(courtSessionWeb);
        Assert.assertTrue(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).save(any(Panel.class));
        verify(panelMemberDaoMock, times(0)).save(any(PanelMember.class));
        verify(sittingDaoMock, times(0)).save(any(SittingDate.class));
        verify(courtSessionDaoMock, times(0)).save(any(CourtSession.class));
        verify(sessionBlockDaoMock, times(0)).save(any(SessionBlock.class));
    }

    @Test(expected = RuntimeException.class)
    public void allocateJudgeWrongDateFormat() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb("01-01-2014", workingDate, falseCourt, testJudge1);
        when(courtRoomDaoMock.findCourtRoomByName(any(String.class))).thenReturn(new CourtRoom(falseCourt));
        final CourtSessionSaveWeb result = listingAdminService.allocateJudgeToCourtRoom(courtSessionWeb);
        Assert.assertTrue(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).save(any(Panel.class));
        verify(panelMemberDaoMock, times(0)).save(any(PanelMember.class));
        verify(sittingDaoMock, times(0)).save(any(SittingDate.class));
        verify(courtSessionDaoMock, times(0)).save(any(CourtSession.class));
        verify(sessionBlockDaoMock, times(0)).save(any(SessionBlock.class));
    }

    @Test
    public void allocateJudge() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate, workingDate2, falseCourt, testJudge1);
        when(courtRoomDaoMock.findCourtRoomByName(any(String.class))).thenReturn(new CourtRoom(falseCourt));
        when(judicialOfficerDaoMock.findJudicialOfficerByName(testJudge1)).thenReturn(new JudicialOfficer(testJudge1, JudicialOfficerType.CIRCUIT, true));
        final CourtSessionSaveWeb result = listingAdminService.allocateJudgeToCourtRoom(courtSessionWeb);
        Assert.assertFalse(result.hasErrorMsg());
        verify(panelDaoMock, times(4)).save(any(Panel.class));
        verify(panelMemberDaoMock, times(4)).save(any(PanelMember.class));
        verify(sittingDaoMock, times(4)).save(any(SittingDate.class));
        verify(courtSessionDaoMock, times(4)).save(any(CourtSession.class));
        verify(sessionBlockDaoMock, times(4)).save(any(SessionBlock.class));
    }

    @Test
    public void allocateExistingJudgeInExistingSession() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate, workingDate2, falseCourt, testJudge1);
        final JudicialOfficer judicialOfficer = new JudicialOfficer(testJudge1, JudicialOfficerType.CIRCUIT, true);
        judicialOfficer.setId(1L);
        final CourtSession session = new CourtSession();
        final Panel panel = new Panel();
        final PanelMember panelMember = new PanelMember();
        panelMember.setJudicialOfficer(judicialOfficer);
        panelMember.setPanel(panel);
        panel.getPanelMember().add(panelMember);
        session.setPanel(panel);
        session.setIsClosed(false);
        final List<CourtSession> sessions = new ArrayList<CourtSession>();
        sessions.add(session);
        when(courtRoomDaoMock.findCourtRoomByName(any(String.class))).thenReturn(new CourtRoom(falseCourt));
        when(judicialOfficerDaoMock.findJudicialOfficerByName(testJudge1)).thenReturn(judicialOfficer);
        when(courtSessionDaoMock.findOpenAndClosedSessionsForSittings(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(sessions);
        final CourtSessionSaveWeb result = listingAdminService.allocateJudgeToCourtRoom(courtSessionWeb);
        verify(courtSessionDaoMock, times(4)).findOpenAndClosedSessionsForSittings(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class));
        Assert.assertTrue(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).save(any(Panel.class));
        verify(panelMemberDaoMock, times(0)).save(any(PanelMember.class));
        verify(sittingDaoMock, times(0)).save(any(SittingDate.class));
        verify(courtSessionDaoMock, times(0)).save(any(CourtSession.class));
        verify(sessionBlockDaoMock, times(0)).save(any(SessionBlock.class));
    }

    @Test
    public void allocateNonExistingJudgeInExistingSession() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate, workingDate2, falseCourt, testJudge1);
        final JudicialOfficer judicialOfficer = new JudicialOfficer(testJudge1, JudicialOfficerType.CIRCUIT, true);
        judicialOfficer.setId(1L);
        final JudicialOfficer judicialOfficer2 = new JudicialOfficer(testJudge2, JudicialOfficerType.CIRCUIT, true);
        judicialOfficer2.setId(3L);
        final CourtSession session = new CourtSession();
        final Panel panel = new Panel();
        final PanelMember panelMember = new PanelMember();
        panelMember.setJudicialOfficer(judicialOfficer2);
        panelMember.setPanel(panel);
        panel.getPanelMember().add(panelMember);
        session.setPanel(panel);
        session.setIsClosed(false);
        final List<CourtSession> sessions = new ArrayList<CourtSession>();
        sessions.add(session);
        when(courtRoomDaoMock.findCourtRoomByName(any(String.class))).thenReturn(new CourtRoom(falseCourt));
        when(judicialOfficerDaoMock.findJudicialOfficerByName(testJudge1)).thenReturn(judicialOfficer);
        when(courtSessionDaoMock.findOpenAndClosedSessionsForSittings(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(sessions);
        final CourtSessionSaveWeb result = listingAdminService.allocateJudgeToCourtRoom(courtSessionWeb);
        verify(courtSessionDaoMock, times(4)).findOpenAndClosedSessionsForSittings(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class));
        Assert.assertFalse(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).save(any(Panel.class));
        verify(panelMemberDaoMock, times(4)).save(any(PanelMember.class));
        verify(sittingDaoMock, times(0)).save(any(SittingDate.class));
        verify(courtSessionDaoMock, times(0)).save(any(CourtSession.class));
        verify(sessionBlockDaoMock, times(0)).save(any(SessionBlock.class));
    }

    @Test(expected = RuntimeException.class)
    public void deallocateJudgeNonWorkingStartDate() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(nonWorkingDate, workingDate, falseCourt, testJudge1);
        final CourtSessionSaveWeb result = listingAdminService.deallocateJudgeFromCourtRoom(courtSessionWeb);
        Assert.assertTrue(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).delete(any(Panel.class));
    }

    @Test(expected = RuntimeException.class)
    public void deallocateJudgeNonWorkingEndDate() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate, nonWorkingDate, falseCourt, testJudge1);
        final CourtSessionSaveWeb result = listingAdminService.deallocateJudgeFromCourtRoom(courtSessionWeb);
        Assert.assertTrue(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).delete(any(Panel.class));
    }

    @Test(expected = RuntimeException.class)
    public void deallocateJudgeStartDateAfterEndDate() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate2, workingDate, falseCourt, testJudge1);
        final CourtSessionSaveWeb result = listingAdminService.deallocateJudgeFromCourtRoom(courtSessionWeb);
        Assert.assertTrue(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).delete(any(Panel.class));
    }

    @Test(expected = RuntimeException.class)
    public void deallocateJudgeNoCourtRoom() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate, workingDate, falseCourt, testJudge1);
        when(judicialOfficerDaoMock.findJudicialOfficerByName(testJudge1)).thenReturn(new JudicialOfficer(testJudge1, JudicialOfficerType.CIRCUIT, true));
        final CourtSessionSaveWeb result = listingAdminService.deallocateJudgeFromCourtRoom(courtSessionWeb);
        Assert.assertTrue(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).delete(any(Panel.class));
    }

    @Test(expected = RuntimeException.class)
    public void deallocateJudgeNoJudge() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate, workingDate, falseCourt, testJudge1);
        when(courtRoomDaoMock.findCourtRoomByName(any(String.class))).thenReturn(new CourtRoom(falseCourt));
        final CourtSessionSaveWeb result = listingAdminService.deallocateJudgeFromCourtRoom(courtSessionWeb);
        Assert.assertTrue(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).delete(any(Panel.class));
    }

    @Test(expected = RuntimeException.class)
    public void deallocateJudgeWrongDateFormat() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb("01-01-2014", workingDate, falseCourt, testJudge1);
        when(courtRoomDaoMock.findCourtRoomByName(any(String.class))).thenReturn(new CourtRoom(falseCourt));
        final CourtSessionSaveWeb result = listingAdminService.deallocateJudgeFromCourtRoom(courtSessionWeb);
        Assert.assertTrue(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).delete(any(Panel.class));
    }

    @Test
    public void deallocateExistingJudgeFromSession() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate, workingDate2, falseCourt, testJudge1);
        final JudicialOfficer judicialOfficer = new JudicialOfficer(testJudge1, JudicialOfficerType.CIRCUIT, true);
        judicialOfficer.setId(1L);
        final CourtSession session = new CourtSession();
        final Panel panel = new Panel();
        final PanelMember panelMember = new PanelMember();
        panelMember.setJudicialOfficer(judicialOfficer);
        panelMember.setPanel(panel);
        panel.getPanelMember().add(panelMember);
        session.setPanel(panel);
        final List<CourtSession> sessions = new ArrayList<CourtSession>();
        sessions.add(session);
        when(courtRoomDaoMock.findCourtRoomByName(any(String.class))).thenReturn(new CourtRoom(falseCourt));
        when(judicialOfficerDaoMock.findJudicialOfficerByName(testJudge1)).thenReturn(judicialOfficer);
        when(courtSessionDaoMock.findOpenCourtRoomSittingByRoomNameBetweenDates(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(sessions);
        final CourtSessionSaveWeb result = listingAdminService.deallocateJudgeFromCourtRoom(courtSessionWeb);
        verify(courtSessionDaoMock, times(4)).findOpenCourtRoomSittingByRoomNameBetweenDates(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class));
        Assert.assertFalse(result.hasErrorMsg());
        verify(panelMemberDaoMock, times(4)).delete(any(PanelMember.class));
    }

    @Test
    public void deallocateNonAllocatedJudgeFromSession() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate, workingDate2, falseCourt, testJudge1);
        final JudicialOfficer judicialOfficer = new JudicialOfficer(testJudge1, JudicialOfficerType.CIRCUIT, true);
        judicialOfficer.setId(1L);
        final JudicialOfficer judicialOfficer2 = new JudicialOfficer(testJudge2, JudicialOfficerType.CIRCUIT, true);
        judicialOfficer2.setId(3L);
        final CourtSession session = new CourtSession();
        final Panel panel = new Panel();
        final PanelMember panelMember = new PanelMember();
        panelMember.setJudicialOfficer(judicialOfficer2);
        panelMember.setPanel(panel);
        panel.getPanelMember().add(panelMember);
        session.setPanel(panel);
        final List<CourtSession> sessions = new ArrayList<CourtSession>();
        sessions.add(session);
        when(courtRoomDaoMock.findCourtRoomByName(any(String.class))).thenReturn(new CourtRoom(falseCourt));
        when(judicialOfficerDaoMock.findJudicialOfficerByName(testJudge1)).thenReturn(judicialOfficer);
        when(courtSessionDaoMock.findOpenCourtRoomSittingByRoomNameBetweenDates(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(sessions);
        final CourtSessionSaveWeb result = listingAdminService.deallocateJudgeFromCourtRoom(courtSessionWeb);
        assertEquals(result.getCourtRoomName(), falseCourt);
        verify(panelMemberDaoMock, times(0)).delete(any(PanelMember.class));
    }

    @Test
    public void deallocateExistingJudgeFromNoSession() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate, workingDate2, falseCourt, testJudge1);
        final JudicialOfficer judicialOfficer = new JudicialOfficer(testJudge1, JudicialOfficerType.CIRCUIT, true);
        judicialOfficer.setId(1L);
        final List<CourtSession> sessions = new ArrayList<CourtSession>();
        when(courtRoomDaoMock.findCourtRoomByName(any(String.class))).thenReturn(new CourtRoom(falseCourt));
        when(judicialOfficerDaoMock.findJudicialOfficerByName(testJudge1)).thenReturn(judicialOfficer);
        when(courtSessionDaoMock.findOpenCourtRoomSittingByRoomNameBetweenDates(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(sessions);
        final CourtSessionSaveWeb result = listingAdminService.deallocateJudgeFromCourtRoom(courtSessionWeb);
        assertEquals(result.getCourtRoomName(), falseCourt);
        verify(panelMemberDaoMock, times(0)).delete(any(PanelMember.class));
    }

    @Test
    public void testSaveCourtRoom() {
        final CourtRoomWeb courtRoomWeb = new CourtRoomWeb();
        courtRoomWeb.setCourtRoomName("courtRoomName");
        courtRoomWeb.setCourtCenterName("Birmingham");
        when(courtDiaryDaoMock.findCourtDiary(Mockito.anyString())).thenReturn(new CourtDiary(new CourtCenter(1000L, "Birmingham")));
        listingAdminService.saveCourtRoom(courtRoomWeb);
        verify(courtRoomDaoMock, times(1)).save(any(CourtRoom.class));
        verify(courtRoomInDiaryDao, times(1)).save(any(CourtRoomInDiary.class));
    }

    @Test
    public void testInsertingItemsInMonthlyTargetSet() {
        final Set<MonthlyTarget> targetSet = new HashSet<MonthlyTarget>();
        final SittingTarget sittingTarget = new SittingTarget();

        final MonthlyTarget mt_1 = new MonthlyTarget();
        mt_1.setMonth("month_1");
        mt_1.setSitting(100L);
        mt_1.setSittingTarget(sittingTarget);

        final MonthlyTarget mt_2 = new MonthlyTarget();
        mt_2.setMonth("month_2");
        mt_2.setSitting(100L);
        mt_2.setSittingTarget(sittingTarget);

        final MonthlyTarget mt_3 = new MonthlyTarget();
        mt_3.setMonth("month_3");
        mt_3.setSitting(100L);
        mt_3.setSittingTarget(sittingTarget);

        targetSet.add(mt_1);
        targetSet.add(mt_2);
        targetSet.add(mt_3);

        Assert.assertTrue(targetSet.size() == 3);
    }

    @Test
    public void testInsertingDuplicateItemsInMonthlyTargetSet() {
        final Set<MonthlyTarget> targetSet = new HashSet<MonthlyTarget>();
        final SittingTarget sittingTarget = new SittingTarget();

        final MonthlyTarget mt_1 = new MonthlyTarget();
        mt_1.setMonth("month_1");
        mt_1.setSitting(100L);
        mt_1.setSittingTarget(sittingTarget);

        final MonthlyTarget mt_2 = new MonthlyTarget();
        mt_2.setMonth("month_1");
        mt_2.setSitting(200L);
        mt_2.setSittingTarget(sittingTarget);

        final MonthlyTarget mt_3 = new MonthlyTarget();
        mt_3.setMonth("month_2");
        mt_3.setSitting(100L);
        mt_3.setSittingTarget(sittingTarget);

        final MonthlyTarget mt_4 = new MonthlyTarget();
        mt_4.setMonth("month_3");
        mt_4.setSitting(200L);
        mt_4.setSittingTarget(sittingTarget);

        targetSet.add(mt_1);
        targetSet.add(mt_2);
        targetSet.add(mt_3);
        targetSet.add(mt_4);

        Assert.assertTrue("wrong size", targetSet.size() == 3);
    }

    @Test(expected = RuntimeException.class)
    public void allocateSetSessionBlockNoCourtRoom() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate, workingDate, falseCourt, testJudge1, blockType);
        final CourtSessionSaveWeb result = listingAdminService.setSessionBlockWithType(courtSessionWeb, false);
        Assert.assertTrue(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).save(any(Panel.class));
        verify(panelMemberDaoMock, times(0)).save(any(PanelMember.class));
        verify(sittingDaoMock, times(0)).save(any(SittingDate.class));
        verify(courtSessionDaoMock, times(0)).save(any(CourtSession.class));
        verify(sessionBlockDaoMock, times(0)).save(any(SessionBlock.class));
    }

    @Test(expected = RuntimeException.class)
    public void allocateSetSessionBlockStartDateAfterEndDate() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate2, workingDate, falseCourt, testJudge1);
        final CourtSessionSaveWeb result = listingAdminService.setSessionBlockWithType(courtSessionWeb, false);
        Assert.assertTrue(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).save(any(Panel.class));
        verify(panelMemberDaoMock, times(0)).save(any(PanelMember.class));
        verify(sittingDaoMock, times(0)).save(any(SittingDate.class));
        verify(courtSessionDaoMock, times(0)).save(any(CourtSession.class));
        verify(sessionBlockDaoMock, times(0)).save(any(SessionBlock.class));
    }

    @Test(expected = RuntimeException.class)
    public void allocateSetSessionBlockNonWorkingStartDate() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(nonWorkingDate, workingDate, falseCourt, testJudge1);
        final CourtSessionSaveWeb result = listingAdminService.setSessionBlockWithType(courtSessionWeb, false);
        Assert.assertTrue(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).save(any(Panel.class));
        verify(panelMemberDaoMock, times(0)).save(any(PanelMember.class));
        verify(sittingDaoMock, times(0)).save(any(SittingDate.class));
        verify(courtSessionDaoMock, times(0)).save(any(CourtSession.class));
        verify(sessionBlockDaoMock, times(0)).save(any(SessionBlock.class));
    }

    @Test(expected = RuntimeException.class)
    public void allocateSetSessionBlockNonWorkingEndDate() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate, nonWorkingDate, falseCourt, testJudge1);
        final CourtSessionSaveWeb result = listingAdminService.setSessionBlockWithType(courtSessionWeb, false);
        Assert.assertTrue(result.hasErrorMsg());
        verify(panelDaoMock, times(0)).save(any(Panel.class));
        verify(panelMemberDaoMock, times(0)).save(any(PanelMember.class));
        verify(sittingDaoMock, times(0)).save(any(SittingDate.class));
        verify(courtSessionDaoMock, times(0)).save(any(CourtSession.class));
        verify(sessionBlockDaoMock, times(0)).save(any(SessionBlock.class));
    }

    @Test
    public void SetSessionBlockWithTypeTest() {
        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate, workingDate2, falseCourt, testJudge1, blockType);
        when(courtRoomDaoMock.findCourtRoomByName(any(String.class))).thenReturn(new CourtRoom(falseCourt));
        final CourtSessionSaveWeb result = listingAdminService.setSessionBlockWithType(courtSessionWeb, false);
        Assert.assertFalse(result.hasErrorMsg());
        verify(panelDaoMock, times(4)).save(any(Panel.class));
        verify(sittingDaoMock, times(4)).save(any(SittingDate.class));
        verify(courtSessionDaoMock, times(4)).save(any(CourtSession.class));
        verify(sessionBlockDaoMock, times(4)).save(any(SessionBlock.class));
    }

    @Test
    public void unSetSessionBlockWithNoHearingsTest() {
        final CourtSession session = new CourtSession();
        final SessionBlock sessionBlock = new SessionBlock();
        sessionBlock.setBlockType(SessionBlockType.getSessionBlockType(blockType));
        sessionBlock.setSession(session);
        session.getSessionBlock().add(sessionBlock);

        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate, workingDate2, falseCourt, testJudge1, blockType);
        when(courtRoomDaoMock.findCourtRoomByName(any(String.class))).thenReturn(new CourtRoom(falseCourt));
        when(courtSessionDaoMock.findOpenCourtRoomSittingByRoomNameBetweenDates(any(Long.class), any(Date.class), any(Date.class))).thenReturn(Arrays.asList(session));
        final CourtSessionSaveWeb result = listingAdminService.setSessionBlockWithType(courtSessionWeb, true);
        Assert.assertFalse(result.hasErrorMsg());
        verify(sessionBlockDaoMock, times(4)).delete(any(SessionBlock.class));
        verify(panelDaoMock, times(0)).save(any(Panel.class));
        verify(sittingDaoMock, times(0)).save(any(SittingDate.class));
        verify(courtSessionDaoMock, times(0)).save(any(CourtSession.class));
        verify(sessionBlockDaoMock, times(0)).save(any(SessionBlock.class));
    }

    @Test(expected = CcsException.class)
    public void unSetSessionBlockWithHearingsTest() {
        final CourtSession session = new CourtSession();
        final SessionBlock sessionBlock = new SessionBlock();
        sessionBlock.setBlockType(SessionBlockType.getSessionBlockType(blockType));
        sessionBlock.getHearings().add(new HearingInstance());
        sessionBlock.setSession(session);
        session.getSessionBlock().add(sessionBlock);

        final CourtSessionSaveWeb courtSessionWeb = new CourtSessionSaveWeb(workingDate, workingDate2, falseCourt, testJudge1, blockType);
        when(courtRoomDaoMock.findCourtRoomByName(any(String.class))).thenReturn(new CourtRoom(falseCourt));
        when(courtSessionDaoMock.findOpenCourtRoomSittingByRoomNameBetweenDates(any(Long.class), any(Date.class), any(Date.class))).thenReturn(Arrays.asList(session));
        listingAdminService.setSessionBlockWithType(courtSessionWeb, true);
    }

    @Test
    public void manageCourtSessionTest() {
        final Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2015, 9, 12);
        final Date startDay = calendar1.getTime();
        final Date endDay = DateUtils.addDays(startDay, 1);
        final ManageSessionAction manageSessionAction = new ManageSessionAction();
        manageSessionAction.setClosed(Boolean.TRUE);
        manageSessionAction.setSittingStartDate(startDay);
        manageSessionAction.setSittingEndDate(endDay);
        manageSessionAction.setCourtRoomNames(Arrays.asList(falseCourt));
        when(courtRoomDaoMock.findCourtRoomByName(any(String.class))).thenReturn(new CourtRoom(falseCourt));
        when(courtSessionDaoMock.findOpenAndClosedSessionsForSittings(any(Long.class), any(Date.class), any(Date.class))).thenReturn(Arrays.asList(new CourtSession()));
        final ManageSessionAction result = listingAdminService.manageCourtSession(manageSessionAction);
        Assert.assertFalse(result.hasErrorMsg());
        verify(courtSessionDaoMock, times(1)).save(any(CourtSession.class));
    }

}
