package uk.co.listing.service;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.co.listing.rest.message.MessageBundler.getMessage;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import uk.co.listing.BaseMockingUnitTest;
import uk.co.listing.dao.CourtCenterDao;
import uk.co.listing.dao.CourtDiaryDao;
import uk.co.listing.dao.CourtRoomDao;
import uk.co.listing.dao.CourtRoomInDiaryDao;
import uk.co.listing.dao.CourtSessionDao;
import uk.co.listing.dao.SittingDao;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.CourtDiary;
import uk.co.listing.domain.CourtRoom;
import uk.co.listing.domain.CourtSession;
import uk.co.listing.domain.JudicialOfficer;
import uk.co.listing.domain.Panel;
import uk.co.listing.domain.PanelMember;
import uk.co.listing.domain.SittingDate;
import uk.co.listing.domain.constant.JudicialOfficerType;
import uk.co.listing.domain.constant.PanelMemberType;
import uk.co.listing.exceptions.CcsException;
import uk.co.listing.rest.response.CourtCenterWeb;
import uk.co.listing.rest.response.CourtRoomWeb;
import uk.co.listing.utils.DateTimeUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DateTimeUtils.class)
public class CourtListingServiceMockTest extends BaseMockingUnitTest {

    @Mock
    CourtRoomDao courtRoomDaoMock;

    @Mock
    CourtCenterDao courtCenterDaoMock;

    @Mock
    CourtDiaryDao courtDiaryDaoMock;

    @Mock
    CourtRoomInDiaryDao courtRoomInDiaryDao;

    @Mock
    CourtSessionDao courtSessionDaoMock;

    @Mock
    SittingDao sittingDaoMock;

    @InjectMocks
    CourtListingService courtListingService;

	@Before
	public void setUp() {
		PowerMockito.mockStatic(DateTimeUtils.class);
	}

    @Test
    public void testGetListOfCourtCenters() {
        when(courtCenterDaoMock.findAllCourtCenters()).thenReturn(Arrays.asList(new CourtCenter(10000L, "London"), new CourtCenter(40000L, "Birmingham")));
        Assert.assertEquals(2, courtListingService.getListOfCourtCenters().size());
    }

    @Test
    public void testSaveCourtCenter() {
        final CourtCenterWeb courtCenterWeb = new CourtCenterWeb();
        courtCenterWeb.setCourtCenterName("courtCenterName");
        courtCenterWeb.setCode(1000L);
        courtListingService.addCourtCentreAndDiary(courtCenterWeb);

        verify(courtCenterDaoMock).findCourtCentreByNameOrCode("courtCenterName", 1000L);
        verify(courtCenterDaoMock, times(1)).save(any(CourtCenter.class));
        verify(courtDiaryDaoMock).save(any(CourtDiary.class));
    }

    @Test
    public void testSaveCourtCenterWithDuplicateName() {
        when(courtCenterDaoMock.findCourtCentreByNameOrCode("name", 1000L)).thenReturn(new CourtCenter(10000L, "London"));

        final CourtCenterWeb courtCenterWeb = new CourtCenterWeb();
        courtCenterWeb.setCourtCenterName("name");
        courtCenterWeb.setCode(1000L);
        try {
            courtListingService.addCourtCentreAndDiary(courtCenterWeb);
            fail("sould throw exception");
        } catch (final Exception e) {
            assertEquals(CcsException.class, e.getClass());
            assertEquals(getMessage("COURT_CENTRE_NAME_AND_CODE_EXSIST",new String[] { "${CENTRENAME}", "${CENTRECODE}" },new String[]{"name","1000"}), e.getMessage());
            verify(courtCenterDaoMock).findCourtCentreByNameOrCode("name", 1000L);
            verify(courtDiaryDaoMock, times(0)).save(any(CourtDiary.class));
        }
    }

    @Test
    public void testGetListOfCourtRoomsByCenterName() {
        when(courtRoomDaoMock.findCourtRoomsByCenterName(Mockito.anyString())).thenReturn(Arrays.asList(new CourtRoom("Court 1"), new CourtRoom("Court 2")));
        Assert.assertEquals(2, courtListingService.getListOfCourtRooms("Birmingham").size());
    }

    @Test
    public void testGetListOfCourtRoomSitting() {
        final CourtRoom courtRoom = new CourtRoom("Room 1");
        final CourtRoom courtRoom2 = new CourtRoom("Room 2");
        when(courtRoomDaoMock.findCourtRoomsByCenterName(Mockito.anyString())).thenReturn(Arrays.asList(courtRoom, courtRoom2));

        final Panel panel = new Panel();
        panel.setDescription("Panel for " + "Judge A");

        final JudicialOfficer judicialOfficer = new JudicialOfficer("Judge A", JudicialOfficerType.CIRCUIT, true);

        final PanelMember panelMember = new PanelMember();
        panelMember.setPanel(panel);
        panelMember.setPanelMemberType(PanelMemberType.JUDGE);
        panelMember.setJudicialOfficer(judicialOfficer);
        final Set<PanelMember> panelMembers = new HashSet<PanelMember>();
        panelMembers.add(panelMember);
        panel.setPanelMember(panelMembers);

        final Date day = new Date();
        final SittingDate sitting = new SittingDate();
        sitting.setCourtRoom(courtRoom);
        sitting.setDay(day);

        final CourtSession session = new CourtSession();
        session.setSitting(sitting);
        session.setPanel(panel);

        final Date day2 = new Date();
        final Date day3 = new Date();
        when(courtSessionDaoMock.findOpenCourtRoomSittingByRoomNameBetweenDates(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(Arrays.asList(session));

        final List<CourtRoomWeb> listCourtCenterWeb = courtListingService.getListOfCourtRoomSitting("Birmingham", day2, day3);

        verify(courtSessionDaoMock, times(2)).findOpenAndClosedSessionsForSittings(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class));
        Assert.assertEquals(2, listCourtCenterWeb.size());

    }


}
