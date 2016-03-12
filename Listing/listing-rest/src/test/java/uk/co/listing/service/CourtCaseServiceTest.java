package uk.co.listing.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

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
import uk.co.listing.dao.JudicialOfficerDao;
import uk.co.listing.dao.PersonDao;
import uk.co.listing.dao.PersonInCaseDao;
import uk.co.listing.dao.SessionBlockDao;
import uk.co.listing.domain.CaseNote;
import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.CrustNonAvailableDates;
import uk.co.listing.domain.Hearing;
import uk.co.listing.domain.JudicialOfficer;
import uk.co.listing.domain.Person;
import uk.co.listing.domain.PersonInCase;
import uk.co.listing.domain.constant.BookingStatusEnum;
import uk.co.listing.domain.constant.BookingTypeEnum;
import uk.co.listing.domain.constant.CustodyStatus;
import uk.co.listing.domain.constant.HearingType;
import uk.co.listing.domain.constant.JudicialOfficerType;
import uk.co.listing.domain.constant.OffenceClass;
import uk.co.listing.domain.constant.ReleaseDecision;
import uk.co.listing.domain.constant.RoleInCase;
import uk.co.listing.domain.constant.TicketingRequirement;
import uk.co.listing.domain.constant.TimeEstimationUnit;
import uk.co.listing.exceptions.CcsException;
import uk.co.listing.rest.response.CaseNoteWeb;
import uk.co.listing.rest.response.CourtCaseWeb;
import uk.co.listing.rest.response.NotAvailableDatesWeb;
import uk.co.listing.rest.response.PersonInCaseWeb;

public class CourtCaseServiceTest extends BaseMockingUnitTest {
    private final String crestCaseNumber = "crest2222222";
    private final String hearingTest = "hearingTest";
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
    CourtCaseService courtCaseService;

    @Mock
    PersonDao personDaoMock;

    @Mock
    PersonInCaseDao personInCaseDaoMock;

    @Mock
    CaseNoteDao caseNoteDaoMock;

    @Mock
    CrustNonAvailableDatesDao nonAvailableDatesMock;

    @Mock
    JudicialOfficerDao judicialOfficerDaoMock;

    @Test
    public void testGetCase() {
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
        final CaseNote note = new CaseNote();
        note.setCreationDate(new Date());
        note.setNote("Betis");
        caseRelated.getNotes().add(note);
        caseRelated.setOffenceClass(OffenceClass.CLASS1);
        caseRelated.setEstimationUnit(TimeEstimationUnit.DAYS);
        final CaseRelated caseLinked = new CaseRelated();
        caseLinked.setCrestCaseNumber(crestCaseNumber);
        caseLinked.setLeadDefendant("Paco porras");
        caseLinked.setMostSeriousOffence("Steal napkin from magician");
        caseRelated.setTicketingRequirement(TicketingRequirement.FRA);
        caseRelated.setReleaseDecisionStatus(ReleaseDecision.AR);
        caseRelated.getLinkedCases().add(caseLinked);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumber(crestCaseNumber)).thenReturn(caseRelated);
        Assert.assertTrue(crestCaseNumber.equals(courtCaseService.getCaseDetailsByCrestNumber(crestCaseNumber).getCrestCaseNumber()));
    }

    @Test(expected = CcsException.class)
    public void testGetEmptyCase() {
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setCrestCaseNumber(crestCaseNumber);
        courtCaseService.getCaseDetailsByCrestNumber(crestCaseNumber);
    }

    @Test
    public void testSaveUpdateCaseRelatedWithExistingCase() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        final CaseRelated caseRelated = new CaseRelated();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));

    }

    @Test(expected = CcsException.class)
    public void testSaveUpdateCaseRelatedWithNoCourtCenter() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(null);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock, times(0)).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));

    }

    @Test
    public void testSaveUpdateCaseRelatedWithOutDates() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setLeadDefendant("Lopera");
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.AR.getValue());
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(100);
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));

    }

    @Test
    public void testSaveUpdateCaseRelated() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setLeadDefendant("Lopera");
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.AR.getValue());
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(100);
        courtCaseWeb.setTrialEstimateUnit(TimeEstimationUnit.WEEKS.getValue());
        courtCaseWeb.setDateOfCommittal(new Date());
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(1)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void testSaveUpdateCaseRelatedNoCrestNumberError() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setLeadDefendant("Lopera");
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.AR.getValue());
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(100);
        courtCaseWeb.setDateOfCommittal(new Date());
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void testSaveUpdateCaseRelatedEmptyCrestNumberError() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setCrestCaseNumber("");
        courtCaseWeb.setLeadDefendant("Lopera");
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.AR.getValue());
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(100);
        courtCaseWeb.setDateOfCommittal(new Date());
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void testSaveUpdateCaseRelatedDateOfSending() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setLeadDefendant("Lopera");
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.AR.getValue());
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(100);
        courtCaseWeb.setDateOfSending(new Date());
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(1)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void testSaveUpdateCaseRelatedNoEstimation() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setLeadDefendant("Lopera and castanyo");
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.AR.getValue());
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(0);
        courtCaseWeb.setDateOfSending(new Date());
        courtCaseWeb.setFromCrest(true);
        courtCaseWeb.setCaseCompleted(true);
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(1)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void testSaveUpdateCaseRelatedTrialNegativeError() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setLeadDefendant("Lopera");
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.AR.getValue());
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(-100);
        courtCaseWeb.setDateOfCommittal(new Date());
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void testSaveUpdateCaseRelatedNumberOfDefendantsNegativeError() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setLeadDefendant("Lopera");
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(-1);
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.AR.getValue());
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(100);
        courtCaseWeb.setDateOfCommittal(new Date());
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void testSaveUpdateCaseRelatedNoOffenceClassError() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setLeadDefendant("Lopera");
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.AR.getValue());
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(100);
        courtCaseWeb.setTrialEstimateUnit(TimeEstimationUnit.WEEKS.getValue());
        courtCaseWeb.setDateOfCommittal(new Date());
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void testSaveUpdateCaseRelatedEmptyOffenceClassError() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setOffenceClass("");
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.AR.getValue());
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(100);
        courtCaseWeb.setTrialEstimateUnit(TimeEstimationUnit.WEEKS.getValue());
        courtCaseWeb.setDateOfCommittal(new Date());
        final CourtCenter center = new CourtCenter();
        courtCaseWeb.setLeadDefendant("Lopera");
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void testSaveUpdateCaseRelatedNoReleaseError() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setLeadDefendant("Lopera");
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(100);
        courtCaseWeb.setTrialEstimateUnit(TimeEstimationUnit.WEEKS.getValue());
        courtCaseWeb.setDateOfCommittal(new Date());
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void testSaveUpdateCaseRelatedEmptyReleaseError() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setReleaseDecisionStatus("");
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(100);
        courtCaseWeb.setTrialEstimateUnit(TimeEstimationUnit.WEEKS.getValue());
        courtCaseWeb.setDateOfCommittal(new Date());
        final CourtCenter center = new CourtCenter();
        courtCaseWeb.setLeadDefendant("Lopera");
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void testSaveUpdateCaseRelatedNoTicketingError() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setLeadDefendant("Lopera");
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.AR.getValue());
        courtCaseWeb.setTrialEstimate(100);
        courtCaseWeb.setTrialEstimateUnit(TimeEstimationUnit.WEEKS.getValue());
        courtCaseWeb.setDateOfCommittal(new Date());
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void testSaveUpdateCaseRelatedEmptyTicketingError() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.AR.getValue());
        courtCaseWeb.setTicketingRequirement("");
        courtCaseWeb.setTrialEstimate(100);
        courtCaseWeb.setTrialEstimateUnit(TimeEstimationUnit.WEEKS.getValue());
        courtCaseWeb.setDateOfCommittal(new Date());
        final CourtCenter center = new CourtCenter();
        courtCaseWeb.setLeadDefendant("Lopera");
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void testSaveUpdateCaseRelatedNoLeadDefendantError() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.AR.getValue());
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(100);
        courtCaseWeb.setTrialEstimateUnit(TimeEstimationUnit.WEEKS.getValue());
        courtCaseWeb.setDateOfCommittal(new Date());
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void testSaveUpdateCaseRelatedEmptyLeadDefendantError() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.AR.getValue());
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(100);
        courtCaseWeb.setTrialEstimateUnit(TimeEstimationUnit.WEEKS.getValue());
        courtCaseWeb.setDateOfCommittal(new Date());
        final CourtCenter center = new CourtCenter();
        courtCaseWeb.setLeadDefendant("");
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void testaddDefendentsForCase() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        final CaseRelated caseRelated = new CaseRelated();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        final PersonInCaseWeb personInCaseWeb = new PersonInCaseWeb();
        personInCaseWeb.setFullname("Jayen");
        personInCaseWeb.setCtlExpiryDate(new Date());
        personInCaseWeb.setCrestURN("URN1");
        personInCaseWeb.setCustodyStatus(CustodyStatus.CUSTODY.getValue());
        courtCaseWeb.getPersonInCaseList().add(personInCaseWeb);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.saveUpdateDefendantForCase(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
    }

    @Test
    public void testaddDefendantsWithDefendantForCase() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        final CaseRelated caseRelated = new CaseRelated();
        final PersonInCase defendant = new PersonInCase();
        final Person person = new Person();
        defendant.setRoleInCase(RoleInCase.DEFENDANT);
        defendant.setCaseUrn("caseUrn");
        defendant.setCustodyStatus(CustodyStatus.BAIL);
        defendant.setPerson(person);
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        final PersonInCaseWeb personInCaseWeb = new PersonInCaseWeb();
        personInCaseWeb.setOriginalFullname("Jayen");
        personInCaseWeb.setCtlExpiryDate(new Date());
        personInCaseWeb.setCrestURN("URN1");
        personInCaseWeb.setCustodyStatus(CustodyStatus.CUSTODY.getValue());
        courtCaseWeb.getPersonInCaseList().add(personInCaseWeb);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        when(personInCaseDaoMock.findPersonInCaseByCrestCaseNumberAndPersonName(Mockito.anyString(), Mockito.anyString())).thenReturn(defendant);
        courtCaseService.saveUpdateDefendantForCase(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
    }

    @Test(expected = CcsException.class)
    public void testAddDefendantsWrongDefendantForCase() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        final CaseRelated caseRelated = new CaseRelated();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        final PersonInCaseWeb personInCaseWeb = new PersonInCaseWeb();
        personInCaseWeb.setOriginalFullname("Jayen");
        personInCaseWeb.setCtlExpiryDate(new Date());
        personInCaseWeb.setCrestURN("URN1");
        personInCaseWeb.setCustodyStatus(CustodyStatus.CUSTODY.getValue());
        courtCaseWeb.getPersonInCaseList().add(personInCaseWeb);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.saveUpdateDefendantForCase(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
    }

    @Test(expected = CcsException.class)
    public void testAddDefendantsNoDefendantsForCase() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        final CaseRelated caseRelated = new CaseRelated();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.saveUpdateDefendantForCase(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
    }

    @Test(expected = CcsException.class)
    public void testaddDefendentsForCaseWithoutCase() {
        final CaseRelated caseRelated = new CaseRelated();
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        courtCaseService.saveUpdateDefendantForCase(courtCaseWeb);
    }

    @Test(expected = CcsException.class)
    public void testaddDefendentsForCaseWithoutDefendant() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        courtCaseService.saveUpdateDefendantForCase(courtCaseWeb);
    }

    @Test(expected = CcsException.class)
    public void testAddDefendentsForCaseWithoutCenter() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseService.saveUpdateDefendantForCase(courtCaseWeb);
    }

    @Test(expected = CcsException.class)
    public void saveNoteNoCenterTestException() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseService.saveUpdateCaseNoteForCase(courtCaseWeb);
        verify(caseNoteDaoMock, times(0)).save(Mockito.any(CaseNote.class));
    }

    @Test(expected = CcsException.class)
    public void saveNoteNoCaseTestException() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseNoteForCase(courtCaseWeb);
        verify(caseNoteDaoMock, times(0)).save(Mockito.any(CaseNote.class));
    }

    @Test(expected = CcsException.class)
    public void saveNoteNoNotesTestException() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        final CaseRelated caseRelated = new CaseRelated();
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.saveUpdateCaseNoteForCase(courtCaseWeb);
        verify(caseNoteDaoMock, times(0)).save(Mockito.any(CaseNote.class));
    }

    @Test(expected = CcsException.class)
    public void saveNoteEmptyNoteTestException() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        final CaseRelated caseRelated = new CaseRelated();
        final CaseNoteWeb caseNoteWeb = new CaseNoteWeb();
        courtCaseWeb.getNotesWeb().add(caseNoteWeb);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.saveUpdateCaseNoteForCase(courtCaseWeb);
        verify(caseNoteDaoMock, times(0)).save(Mockito.any(CaseNote.class));
    }

    @Test(expected = CcsException.class)
    public void saveNoteTestLongNoteException() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        final CaseRelated caseRelated = new CaseRelated();
        final CaseNoteWeb caseNoteWeb = new CaseNoteWeb();
        caseNoteWeb.setNote("This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length"
                + " This is an really long note for the case, and I am going to repeat it a lot of times to get the perfect length");
        courtCaseWeb.getNotesWeb().add(caseNoteWeb);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.saveUpdateCaseNoteForCase(courtCaseWeb);
        verify(caseNoteDaoMock, times(1)).save(Mockito.any(CaseNote.class));
    }

    @Test
    public void saveNoteTestNoCreationDate() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        final CaseRelated caseRelated = new CaseRelated();
        final CaseNoteWeb caseNoteWeb = new CaseNoteWeb();
        caseNoteWeb.setNote("This is an amazing note for the case");
        courtCaseWeb.getNotesWeb().add(caseNoteWeb);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.saveUpdateCaseNoteForCase(courtCaseWeb);
        verify(caseNoteDaoMock, times(1)).save(Mockito.any(CaseNote.class));
    }

    @Test
    public void saveNoteTestCreationDate() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        final CaseRelated caseRelated = new CaseRelated();
        final CaseNoteWeb caseNoteWeb = new CaseNoteWeb();
        caseNoteWeb.setNote("This is an amazing note for the case");
        caseNoteWeb.setCreationDate(new Date());
        courtCaseWeb.getNotesWeb().add(caseNoteWeb);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.saveUpdateCaseNoteForCase(courtCaseWeb);
        verify(caseNoteDaoMock, times(1)).save(Mockito.any(CaseNote.class));
    }

    @Test(expected = CcsException.class)
    public void saveNotAvailableDateNoCenterTestException() {
        final NotAvailableDatesWeb courtCaseWeb = new NotAvailableDatesWeb();
        courtCaseService.saveUpdateNotAvailableDateForCase(courtCaseWeb);
        verify(nonAvailableDatesMock, times(0)).save(Mockito.any(CrustNonAvailableDates.class));
    }

    @Test(expected = CcsException.class)
    public void saveNotAvailableDateNoCaseTestException() {
        final NotAvailableDatesWeb courtCaseWeb = new NotAvailableDatesWeb();
        courtCaseService.saveUpdateNotAvailableDateForCase(courtCaseWeb);
        verify(nonAvailableDatesMock, times(0)).save(Mockito.any(CrustNonAvailableDates.class));
    }

    @Test(expected = CcsException.class)
    public void saveNotAvailableDateNoDatesTestException() {
        final NotAvailableDatesWeb courtCaseWeb = new NotAvailableDatesWeb();
        final CaseRelated caseRelated = new CaseRelated();
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.saveUpdateNotAvailableDateForCase(courtCaseWeb);
        verify(nonAvailableDatesMock, times(0)).save(Mockito.any(CrustNonAvailableDates.class));
    }

    @Test(expected = CcsException.class)
    public void saveNotAvailableDateEmptyReasonTestException() {
        final CaseRelated caseRelated = new CaseRelated();
        final NotAvailableDatesWeb nonDatesWeb = new NotAvailableDatesWeb();
        nonDatesWeb.setEndDate(new Date());
        nonDatesWeb.setStartDate(new Date());
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.saveUpdateNotAvailableDateForCase(nonDatesWeb);
        verify(nonAvailableDatesMock, times(0)).save(Mockito.any(CrustNonAvailableDates.class));
    }

    @Test(expected = CcsException.class)
    public void saveNotAvailableDateEmptyStartDateTestException() {
        final CaseRelated caseRelated = new CaseRelated();
        final NotAvailableDatesWeb nonDatesWeb = new NotAvailableDatesWeb();
        nonDatesWeb.setEndDate(new Date());
        nonDatesWeb.setReason("Betis is playing that day");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.saveUpdateNotAvailableDateForCase(nonDatesWeb);
        verify(nonAvailableDatesMock, times(0)).save(Mockito.any(CrustNonAvailableDates.class));
    }

    @Test(expected = CcsException.class)
    public void saveNotAvailableDateLongReasonTestException() {
        final CaseRelated caseRelated = new CaseRelated();
        final NotAvailableDatesWeb nonDatesWeb = new NotAvailableDatesWeb();
        nonDatesWeb.setEndDate(new Date());
        nonDatesWeb.setStartDate(new Date());
        nonDatesWeb.setReason("Betis is playing that day, so it is going to be a really really really amazing match. I can not even imagine to be able to watch this math in the stadium");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.saveUpdateNotAvailableDateForCase(nonDatesWeb);
        verify(nonAvailableDatesMock, times(0)).save(Mockito.any(CrustNonAvailableDates.class));
    }

    @Test(expected = CcsException.class)
    public void saveNotAvailableDateEmptyEndDateTestException() {
        final CaseRelated caseRelated = new CaseRelated();
        final NotAvailableDatesWeb nonDatesWeb = new NotAvailableDatesWeb();
        nonDatesWeb.setStartDate(new Date());
        nonDatesWeb.setReason("Betis is playing that day");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.saveUpdateNotAvailableDateForCase(nonDatesWeb);
        verify(nonAvailableDatesMock, times(0)).save(Mockito.any(CrustNonAvailableDates.class));
    }

    @Test
    public void saveNotAvailableDateTest() {
        final CaseRelated caseRelated = new CaseRelated();
        final NotAvailableDatesWeb nonDatesWeb = new NotAvailableDatesWeb();
        nonDatesWeb.setStartDate(new Date());
        nonDatesWeb.setEndDate(new Date());
        nonDatesWeb.setReason("Betis is playing that day");
        nonDatesWeb.setCrestCaseNumber(crestCaseNumber);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumber(Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.saveUpdateNotAvailableDateForCase(nonDatesWeb);
        verify(nonAvailableDatesMock, times(1)).save(Mockito.any(CrustNonAvailableDates.class));
    }

    @Test
    public void deleteNotAvailableDateTest() {
        final CrustNonAvailableDates nonDates = new CrustNonAvailableDates();

        when(nonAvailableDatesMock.findById(Mockito.anyLong())).thenReturn(nonDates);
        courtCaseService.deleteNotAvailableDateForCase(1000l);
        verify(nonAvailableDatesMock, times(1)).delete(Mockito.any(CrustNonAvailableDates.class));
    }

    @Test(expected = CcsException.class)
    public void deleteNotAvailableDateNoCaseTestException() {
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.deleteNotAvailableDateForCase(null);
        verify(nonAvailableDatesMock, times(0)).save(Mockito.any(CrustNonAvailableDates.class));
    }

    @Test(expected = CcsException.class)
    public void deleteNotAvailableDateNoDatesTestException() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        final CaseRelated caseRelated = new CaseRelated();
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.deleteNotAvailableDateForCase(null);
        verify(nonAvailableDatesMock, times(0)).save(Mockito.any(CrustNonAvailableDates.class));
    }

    @Test(expected = CcsException.class)
    public void allocateJudgeNoCenterTestException() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseService.allocateJudgeForCase(courtCaseWeb);
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test(expected = CcsException.class)
    public void allocateJudgeNoCaseTestException() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.allocateJudgeForCase(courtCaseWeb);
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test(expected = CcsException.class)
    public void allocateJudgeNoJudgeTestException() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        final CaseRelated caseRelated = new CaseRelated();
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.allocateJudgeForCase(courtCaseWeb);
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void allocateJudgeTest() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        final CaseRelated caseRelated = new CaseRelated();
        final JudicialOfficer judge = new JudicialOfficer("Judge Alaya", JudicialOfficerType.CIRCUIT, true);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        when(judicialOfficerDaoMock.findJudicialOfficerByName(Mockito.anyString())).thenReturn(judge);
        courtCaseService.allocateJudgeForCase(courtCaseWeb);
        verify(caseRelatedDaoMock, times(1)).save(Mockito.any(CaseRelated.class));
    }

    @Test(expected = CcsException.class)
    public void deallocateJudgeNoCenterTestException() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseService.deallocateJudgeForCase(courtCaseWeb);
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test(expected = CcsException.class)
    public void deallocateJudgeNoCaseTestException() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.deallocateJudgeForCase(courtCaseWeb);
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void deallocateJudgeTest() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        final CaseRelated caseRelated = new CaseRelated();
        final JudicialOfficer judge = new JudicialOfficer("Judge Alaya", JudicialOfficerType.CIRCUIT, true);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        when(judicialOfficerDaoMock.findJudicialOfficerByName(Mockito.anyString())).thenReturn(judge);
        courtCaseService.deallocateJudgeForCase(courtCaseWeb);
        verify(caseRelatedDaoMock, times(1)).save(Mockito.any(CaseRelated.class));
    }

    @Test(expected = CcsException.class)
    public void saveLinkedCaseNoCenterTestException() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseService.saveUpdateLinkedCaseForCase(courtCaseWeb);
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test(expected = CcsException.class)
    public void saveLinkedCaseNoCaseTestException() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateLinkedCaseForCase(courtCaseWeb);
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test(expected = CcsException.class)
    public void saveLinkedCaseNoLinkedCasesTestException() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        final CaseRelated caseRelated = new CaseRelated();
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        courtCaseService.saveUpdateLinkedCaseForCase(courtCaseWeb);
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
        verify(caseRelatedDaoMock, times(1)).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
    }

    @Test(expected = CcsException.class)
    public void saveLinkedCaseNoExistingCaseTestException() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCaseWeb linkedCaseWeb = new CourtCaseWeb();
        linkedCaseWeb.setCrestCaseNumber("Betis");
        courtCaseWeb.getLinkedCases().add(linkedCaseWeb);
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        final CaseRelated caseRelated = new CaseRelated();
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(crestCaseNumber, "Birmingham")).thenReturn(caseRelated);
        courtCaseService.saveUpdateLinkedCaseForCase(courtCaseWeb);
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
        verify(caseRelatedDaoMock, times(1)).findCaseByCrestCaseNumberAndCenter(crestCaseNumber, "Birmingham");
        verify(caseRelatedDaoMock, times(1)).findCaseByCrestCaseNumberAndCenter("Betis", "Birmingham");
    }

    @Test
    public void saveLinkedCaseTest() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        final CourtCaseWeb linkedCaseWeb = new CourtCaseWeb();
        linkedCaseWeb.setCrestCaseNumber("Betis");
        courtCaseWeb.getLinkedCases().add(linkedCaseWeb);
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        final CaseRelated caseRelated1 = new CaseRelated();
        final CaseRelated caseRelated2 = new CaseRelated();
        when(courtCenterDaoMock.findCourtCentreByName("Birmingham")).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated1);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated2);
        courtCaseService.saveUpdateLinkedCaseForCase(courtCaseWeb);
        verify(caseRelatedDaoMock, times(1)).save(Mockito.any(CaseRelated.class));
        verify(caseRelatedDaoMock, times(2)).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void removeDefendantForCaseTest() {
        final String crestCaseNumber = "crestCaseNumber";
        final String name = "Ruben Castro";
        final PersonInCase person = new PersonInCase();
        when(personInCaseDaoMock.findPersonInCaseByCrestCaseNumberAndPersonName(crestCaseNumber, name)).thenReturn(person);
        courtCaseService.removeDefendantForCase(crestCaseNumber, name);
        verify(personInCaseDaoMock, times(1)).delete(Mockito.any(PersonInCase.class));
    }

    @Test
    public void removeDefendantNoCaseTest() {
        final String crestCaseNumber = "";
        final String name = "Ruben Castro";
        final PersonInCase person = new PersonInCase();
        when(personInCaseDaoMock.findPersonInCaseByCrestCaseNumberAndPersonName(crestCaseNumber, name)).thenReturn(person);
        courtCaseService.removeDefendantForCase(crestCaseNumber, name);
        verify(personInCaseDaoMock, times(0)).delete(Mockito.any(PersonInCase.class));
    }

    @Test
    public void removeNoDefendantFromCaseTest() {
        final String crestCaseNumber = "Hello my friend";
        final String name = "";
        final PersonInCase person = new PersonInCase();
        when(personInCaseDaoMock.findPersonInCaseByCrestCaseNumberAndPersonName(crestCaseNumber, name)).thenReturn(person);
        courtCaseService.removeDefendantForCase(crestCaseNumber, name);
        verify(personInCaseDaoMock, times(0)).delete(Mockito.any(PersonInCase.class));
    }

    @Test
    public void removeCaseNoteForCaseTest() {
        final String crestCaseNumber = "crestCaseNumber";
        final Long desc = 1001L;
        final CaseNote note = new CaseNote();
        when(caseNoteDaoMock.findNoteByCrestCaseNumberAndDescription(crestCaseNumber, desc)).thenReturn(note);
        courtCaseService.removeCaseNoteForCase(crestCaseNumber, desc.toString());
        verify(caseNoteDaoMock, times(1)).findNoteByCrestCaseNumberAndDescription(Mockito.anyString(), Mockito.anyLong());
        verify(caseNoteDaoMock, times(1)).delete(Mockito.any(CaseNote.class));
    }

    @Test
    public void removeNoCaseNoteForCaseTest() {
        final String crestCaseNumber = "crestCaseNumber";
        final Long desc = 1001L;
        courtCaseService.removeCaseNoteForCase(crestCaseNumber, desc.toString());
        verify(caseNoteDaoMock, times(1)).findNoteByCrestCaseNumberAndDescription(Mockito.anyString(), Mockito.anyLong());
        verify(caseNoteDaoMock, times(0)).delete(Mockito.any(CaseNote.class));
    }

    @Test(expected = CcsException.class)
    public void removeCaseNoteForCaseNoCrestNumberExceptionTest() {
        final String crestCaseNumber = "";
        final Long desc = 1001L;
        courtCaseService.removeCaseNoteForCase(crestCaseNumber, desc.toString());
        verify(caseNoteDaoMock, times(0)).findNoteByCrestCaseNumberAndDescription(Mockito.anyString(), Mockito.anyLong());
        verify(caseNoteDaoMock, times(0)).delete(Mockito.any(CaseNote.class));
    }

    @Test(expected = CcsException.class)
    public void removeCaseNoteForCaseNoDescExceptionTest() {
        final String crestCaseNumber = "Viva el beti";
        courtCaseService.removeCaseNoteForCase(crestCaseNumber, "");
        verify(caseNoteDaoMock, times(0)).findNoteByCrestCaseNumberAndDescription(Mockito.anyString(), Mockito.anyLong());
        verify(caseNoteDaoMock, times(0)).delete(Mockito.any(CaseNote.class));
    }

    @Test(expected = CcsException.class)
    public void removeLinkedForCaseNoCrestNumberExceptionTest() {
        final String crestCaseNumber = "";
        final String crestLinkedCaseNumber = "T600";
        final String courtCenter = "test";
        courtCaseService.removeLinkedCaseForCase(courtCenter, crestCaseNumber, crestLinkedCaseNumber);
        verify(courtCenterDaoMock, times(0)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test(expected = CcsException.class)
    public void removeLinkedForCaseNoLinkedExceptionTest() {
        final String crestCaseNumber = "T600";
        final String crestLinkedCaseNumber = "";
        final String courtCenter = "test";
        courtCaseService.removeLinkedCaseForCase(courtCenter, crestCaseNumber, crestLinkedCaseNumber);
        verify(courtCenterDaoMock, times(0)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test(expected = CcsException.class)
    public void removeLinkedForCaseNoCenterExceptionTest() {
        final String crestCaseNumber = "T600";
        final String crestLinkedCaseNumber = "test";
        final String courtCenter = "";
        courtCaseService.removeLinkedCaseForCase(courtCenter, crestCaseNumber, crestLinkedCaseNumber);
        verify(courtCenterDaoMock, times(0)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test(expected = CcsException.class)
    public void removeLinkedForCaseNoExistingCenterExceptionTest() {
        final String crestCaseNumber = "hi";
        final String crestLinkedCaseNumber = "test";
        final String courtCenter = "test";
        courtCaseService.removeLinkedCaseForCase(courtCenter, crestCaseNumber, crestLinkedCaseNumber);
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test(expected = CcsException.class)
    public void removeLinkedForCaseNoExistingCaseExceptionTest() {
        final CourtCenter center = new CourtCenter(100L, "Birmingham");
        final String crestCaseNumber = "hi";
        final String crestLinkedCaseNumber = "test";
        final String courtCenter = "test";
        Mockito.when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.removeLinkedCaseForCase(courtCenter, crestCaseNumber, crestLinkedCaseNumber);
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(1)).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void removeLinkedForCaseTest() {
        final CourtCenter center = new CourtCenter(100L, "Birmingham");
        final String crestCaseNumber = "hi";
        final String crestLinkedCaseNumber = "test";
        final String courtCenter = "test";
        final CaseRelated case1 = new CaseRelated();
        final CaseRelated linkedCase = new CaseRelated();
        case1.setCrestCaseNumber(crestCaseNumber);
        linkedCase.setCrestCaseNumber(crestLinkedCaseNumber);
        case1.getLinkedCases().add(linkedCase);
        Mockito.when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(case1);
        Mockito.doNothing().when(caseRelatedDaoMock).save(Mockito.any(CaseRelated.class));
        courtCaseService.removeLinkedCaseForCase(courtCenter, crestCaseNumber, crestLinkedCaseNumber);
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(1)).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(caseRelatedDaoMock, times(1)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void removeNoLinkedForCaseTest() {
        final CourtCenter center = new CourtCenter(100L, "Birmingham");
        final String crestCaseNumber = "hi";
        final String crestLinkedCaseNumber = "test";
        final String courtCenter = "test";
        final CaseRelated case1 = new CaseRelated();
        final CaseRelated linkedCase = new CaseRelated();
        case1.setCrestCaseNumber(crestCaseNumber);
        linkedCase.setCrestCaseNumber(crestLinkedCaseNumber);
        case1.getLinkedCases().add(linkedCase);
        Mockito.when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(case1);
        Mockito.doNothing().when(caseRelatedDaoMock).save(Mockito.any(CaseRelated.class));
        courtCaseService.removeLinkedCaseForCase(courtCenter, crestCaseNumber, "Real Betis");
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(1)).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void testInvalidNamedJudgeReleaseDecision() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setLeadDefendant("Lopera");
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.NJ.getValue());
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(100);
        courtCaseWeb.setDateOfSending(new Date());
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        try {
            courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
            fail("sould throw exception");
        } catch (final Exception e) {
            assertEquals(CcsException.class, e.getClass());
            assertEquals("You must allocate a judge when you select Release Decision: Named Judge", e.getMessage());
            verify(caseRelatedDaoMock, times(0)).save(Mockito.any(CaseRelated.class));
        }
    }

    @Test
    public void testAnyRecorderReleaseDecisionWhenJudgeAllocate() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setLeadDefendant("Lopera");
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.AR.getValue());
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(100);
        courtCaseWeb.setDateOfSending(new Date());

        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        final CaseRelated caseRelated = new CaseRelated();
        final JudicialOfficer judge = new JudicialOfficer("Judge Alaya", JudicialOfficerType.CIRCUIT, true);
        caseRelated.setJudge(judge);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        when(judicialOfficerDaoMock.findJudicialOfficerByName(Mockito.anyString())).thenReturn(judge);
        courtCaseService.allocateJudgeForCase(courtCaseWeb);
        verify(caseRelatedDaoMock, times(1)).save(Mockito.any(CaseRelated.class));
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(courtCenterDaoMock, times(2)).findCourtCentreByName(Mockito.anyString());
        // invoked for allocate judge
        verify(caseRelatedDaoMock, times(2)).save(Mockito.any(CaseRelated.class));

    }

    @Test
    public void testValidNamedJudgeReleaseDecision() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setLeadDefendant("Lopera");
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.AR.getValue());
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(100);
        courtCaseWeb.setDateOfSending(new Date());
        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(caseRelatedDaoMock).findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString());
        verify(courtCenterDaoMock, times(1)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(1)).save(Mockito.any(CaseRelated.class));
    }

    @Test
    public void testValidReleaseDecisionWhenJudgeAllocate() {
        final CourtCaseWeb courtCaseWeb = new CourtCaseWeb();
        courtCaseWeb.setCrestCaseNumber(crestCaseNumber);
        courtCaseWeb.setCourtCenter("Birmingham");
        courtCaseWeb.setLeadDefendant("Lopera");
        courtCaseWeb.setMostSeriousOffence("Estafar al Betis");
        courtCaseWeb.setNumberOfDefendent(1);
        courtCaseWeb.setOffenceClass(OffenceClass.CLASS1.getValue());
        courtCaseWeb.setReleaseDecisionStatus(ReleaseDecision.NJ.getValue());
        courtCaseWeb.setTicketingRequirement(TicketingRequirement.FRA.getValue());
        courtCaseWeb.setTrialEstimate(100);
        courtCaseWeb.setDateOfSending(new Date());

        final CourtCenter center = new CourtCenter();
        center.setCenterName("Birmingham");
        final CaseRelated caseRelated = new CaseRelated();
        final JudicialOfficer judge = new JudicialOfficer("Judge Alaya", JudicialOfficerType.CIRCUIT, true);
        caseRelated.setJudge(judge);
        courtCaseWeb.setJudicialOfficer(judge.getFullName());
        when(courtCenterDaoMock.findCourtCentreByName(Mockito.anyString())).thenReturn(center);
        when(caseRelatedDaoMock.findCaseByCrestCaseNumberAndCenter(Mockito.anyString(), Mockito.anyString())).thenReturn(caseRelated);
        when(judicialOfficerDaoMock.findJudicialOfficerByName(Mockito.anyString())).thenReturn(judge);
        courtCaseService.allocateJudgeForCase(courtCaseWeb);
        verify(caseRelatedDaoMock, times(1)).save(Mockito.any(CaseRelated.class));
        courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        verify(courtCenterDaoMock, times(2)).findCourtCentreByName(Mockito.anyString());
        verify(caseRelatedDaoMock, times(2)).save(Mockito.any(CaseRelated.class));

    }
}
