package uk.co.listing.service;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.co.listing.rest.message.MessageBundler.getMessage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import uk.co.listing.BaseMockingUnitTest;
import uk.co.listing.dao.CaseRelatedDao;
import uk.co.listing.dao.CrestDataBatchJobDao;
import uk.co.listing.domain.CaseNote;
import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.CrestData;
import uk.co.listing.domain.CrestDataBatchJob;
import uk.co.listing.domain.CrestDefendant;
import uk.co.listing.domain.CrestLinkedCase;
import uk.co.listing.domain.CrestNonAvailable;
import uk.co.listing.domain.CrestNote;
import uk.co.listing.domain.CrestPcmh;
import uk.co.listing.domain.CrustNonAvailableDates;
import uk.co.listing.domain.PersonInCase;
import uk.co.listing.domain.constant.CaseNotePrintType;
import uk.co.listing.domain.constant.CaseNoteType;
import uk.co.listing.domain.constant.CustodyStatus;
import uk.co.listing.domain.constant.OffenceClass;
import uk.co.listing.domain.constant.ProcessingStatus;
import uk.co.listing.domain.constant.ReleaseDecision;
import uk.co.listing.domain.constant.TicketingRequirement;
import uk.co.listing.domain.constant.TimeEstimationUnit;
import uk.co.listing.rest.response.CourtCaseWeb;
import uk.co.listing.rest.response.HearingWeb;
import uk.co.listing.rest.response.NotAvailableDatesWeb;

public class CrestDataProcessingServiceTest extends BaseMockingUnitTest {

    @Mock
    CrestDataBatchJobDao crestDataBatchJobDao;

    @Mock
    private CaseRelatedDao caseRelatedDao;

    @Mock
    private CourtCaseService courtCaseService;

    @Mock
    private HearingService hearingService;

    @InjectMocks
    CrestDataProcessingService crestDataProcessingService;

    private static Date today = DateUtils.truncate(new Date(), Calendar.DATE);

    @Test
    public void testupdateUnprocessedTaskAsError() throws FileNotFoundException {

        final CrestDataBatchJob crestDataBatchJob = new CrestDataBatchJob();
        crestDataBatchJob.setProcessingState(ProcessingStatus.PROCESSING);
        crestDataBatchJob.setComments("comments");
        when(crestDataBatchJobDao.findAllTaskByProcessingStatus(ProcessingStatus.PROCESSING)).thenReturn(Arrays.asList(crestDataBatchJob));
        Mockito.doNothing().when(crestDataBatchJobDao).save(Mockito.anyObject());
        crestDataProcessingService.updateUnprocessedTaskAsError(ProcessingStatus.PROCESSING);
        verify(crestDataBatchJobDao, times(1)).save(crestDataBatchJob);
        assertTrue(crestDataBatchJob.getProcessingState().equals(ProcessingStatus.ERROR));
    }

    @Test
    public void testUpdateCrestDataBatchJob() throws FileNotFoundException {

        final CrestDataBatchJob crestDataBatchJob = new CrestDataBatchJob();
        crestDataBatchJob.setProcessingState(ProcessingStatus.PROCESSING);
        crestDataBatchJob.setComments("comments");
        final Long batchJobId = 10l;
        when(crestDataBatchJobDao.getCrestDataBatchJobById(batchJobId)).thenReturn(crestDataBatchJob);
        Mockito.doNothing().when(crestDataBatchJobDao).save(crestDataBatchJob);
        crestDataProcessingService.updateCrestDataBatchJob(batchJobId, ProcessingStatus.PROCESSING, "comments");
        verify(crestDataBatchJobDao, times(1)).save(crestDataBatchJob);

    }

    @Test
    public void testProcessCrestDataCaseBatchJob() throws FileNotFoundException {
        final CrestDataBatchJob crestDataBatchJob = new CrestDataBatchJob();
        crestDataBatchJob.setProcessingState(ProcessingStatus.PROCESSING);
        crestDataBatchJob.setComments("comments");
        final Long batchJobId = 10l;

        final Set<PersonInCase> personInCase = new HashSet<>();
        final PersonInCase pic = new PersonInCase();
        personInCase.add(pic);

        final Set<CrustNonAvailableDates> crustNonAvailableDatesSet = new HashSet<>();
        final CrustNonAvailableDates crustNonAvailableDates = new CrustNonAvailableDates();
        crustNonAvailableDatesSet.add(crustNonAvailableDates);

        final Set<CaseNote> caseNoteSet = new HashSet<>();
        final CaseNote caseNote = new CaseNote();
        caseNoteSet.add(caseNote);

        final CaseRelated caseUpdate = createCaseRelated("caseNumberUpdate");
        caseUpdate.setPersonInCase(personInCase);
        caseUpdate.setCrustNonAvailableDatesList(crustNonAvailableDatesSet);
        caseUpdate.setNotes(caseNoteSet);

        final CaseRelated caseCompletion = createCaseRelated("caseNumberCompletion");

        final List<CrestData> updateListData = crestData("caseNumberUpdate");
        final List<CrestData> insertListData = crestData("caseNumberInsert");

        when(crestDataBatchJobDao.getCreateDataForUpdate(batchJobId)).thenReturn(updateListData);
        when(crestDataBatchJobDao.getCreateDataForInsert(batchJobId)).thenReturn(insertListData);
        when(crestDataBatchJobDao.getCreateDataForCompletion(batchJobId)).thenReturn(Arrays.asList(caseCompletion));

        when(caseRelatedDao.findCaseByCrestCaseNumber("caseNumberUpdate")).thenReturn(caseUpdate);
        Mockito.doNothing().when(caseRelatedDao).delete(Mockito.any());
        Mockito.doNothing().when(caseRelatedDao).save(Mockito.any());

        crestDataBatchJob.setProcessingState(ProcessingStatus.PROCESSED);
        crestDataBatchJob.setComments("comments");

        when(crestDataBatchJobDao.getCrestDataBatchJobById(batchJobId)).thenReturn(crestDataBatchJob);
        Mockito.doNothing().when(crestDataBatchJobDao).save(crestDataBatchJob);

        crestDataProcessingService.processCrestDataBatchJob(batchJobId);
        verify(caseRelatedDao, times(2)).save(any());
    }

    @Test
    public void testProcessCrestDataDefendantBatchJob() throws FileNotFoundException {
        final Long batchJobId = 10l;
        final List<CrestDefendant> defendants = new ArrayList<CrestDefendant>();
        Mockito.doNothing().when(crestDataBatchJobDao).deleteCrestdefendant();
        when(crestDataBatchJobDao.getInvalidCrestdefendant(batchJobId)).thenReturn(defendants);

        final List<CrestDefendant> defendantData = new ArrayList<CrestDefendant>();
        final CrestDefendant crestDefendant1 = createDefendant("caseNumberUpdate", "Jose Maria del Nido");
        final CrestDefendant crestDefendant2 = createDefendant("caseNumberUpdate", "Barcenas");
        defendantData.add(crestDefendant1);
        defendantData.add(crestDefendant2);
        when(crestDataBatchJobDao.getCrestdefendant(batchJobId)).thenReturn(defendantData);
        Mockito.doNothing().when(courtCaseService).saveUpdateDefendantForCase(Mockito.any(CourtCaseWeb.class));
        crestDataProcessingService.processCrestDefendantBatchJob(batchJobId);
        verify(courtCaseService, times(defendantData.size())).saveUpdateDefendantForCase(Mockito.any(CourtCaseWeb.class));
    }

    @Test
    public void testProcessCrestDataNotesBatchJob() throws FileNotFoundException {
        final Long batchJobId = 10l;
        final List<CrestNote> notes = new ArrayList<CrestNote>();
        Mockito.doNothing().when(crestDataBatchJobDao).deleteCrestNotes();
        when(crestDataBatchJobDao.getInvalidCrestNotes(batchJobId)).thenReturn(notes);

        final List<CrestNote> notesData = new ArrayList<CrestNote>();
        final CrestNote note1 = createNote("caseNumberUpdate", "Jose Maria del Nido");
        final CrestNote note2 = createNote("caseNumberUpdate", "Barcenas");
        notesData.add(note1);
        notesData.add(note2);
        when(crestDataBatchJobDao.getCrestNotes(batchJobId)).thenReturn(notesData);
        Mockito.doNothing().when(courtCaseService).saveUpdateCaseNoteForCase(Mockito.any(CourtCaseWeb.class));
        crestDataProcessingService.processCrestNotesBatchJob(batchJobId);
        verify(courtCaseService, times(notesData.size())).saveUpdateCaseNoteForCase(Mockito.any(CourtCaseWeb.class));
    }

    @Test
    public void testProcessCrestDataNonAvBatchJob() throws FileNotFoundException {
        final Long batchJobId = 10l;
        final List<CrestNonAvailable> nonAv = new ArrayList<CrestNonAvailable>();
        Mockito.doNothing().when(crestDataBatchJobDao).deleteCrestNonAvail();
        when(crestDataBatchJobDao.getInvalidCrestNonAvail(batchJobId)).thenReturn(nonAv);

        final List<CrestNonAvailable> nonAvData = new ArrayList<CrestNonAvailable>();
        final CrestNonAvailable nonAv1 = createNonAv("caseNumberUpdate", "Jose Maria del Nido to prison");
        final CrestNonAvailable nonAv2 = createNonAv("caseNumberUpdate", "Barcenas to prison");
        nonAvData.add(nonAv1);
        nonAvData.add(nonAv2);
        when(crestDataBatchJobDao.getCrestNonAvail(batchJobId)).thenReturn(nonAvData);
        Mockito.doNothing().when(courtCaseService).saveUpdateNotAvailableDateForCase(Mockito.any(NotAvailableDatesWeb.class));
        crestDataProcessingService.processCrestNonAvailBatchJob(batchJobId);
        verify(courtCaseService, times(nonAvData.size())).saveUpdateNotAvailableDateForCase(Mockito.any(NotAvailableDatesWeb.class));
    }

    @Test
    public void testProcessCrestDataPcmhBatchJob() throws FileNotFoundException {
        final Long batchJobId = 10l;
        final List<CrestNonAvailable> nonAv = new ArrayList<CrestNonAvailable>();
        Mockito.doNothing().when(crestDataBatchJobDao).deleteCrestHearing();
        when(crestDataBatchJobDao.getInvalidCrestNonAvail(batchJobId)).thenReturn(nonAv);

        final List<CrestPcmh> pcmhList = new ArrayList<CrestPcmh>();
        final CrestPcmh pcmh1 = createPcmh("caseNumberUpdate", today);
        final CrestPcmh pcmh2 = createPcmh("caseNumberUpdate", DateUtils.addDays(today, 5));
        pcmhList.add(pcmh1);
        pcmhList.add(pcmh2);
        final HearingWeb hearingResult = new HearingWeb();
        when(crestDataBatchJobDao.getCrestHearing(batchJobId)).thenReturn(pcmhList);
        Mockito.when(hearingService.saveUpdateHearing(Mockito.any(HearingWeb.class))).thenReturn(hearingResult);
        Mockito.doNothing().when(hearingService).autoScheduleHearing(Mockito.any(HearingWeb.class), Mockito.anyString());
        crestDataProcessingService.processCrestHearingBatchJob(batchJobId);
        verify(hearingService, times(pcmhList.size())).saveUpdateHearing(Mockito.any(HearingWeb.class));
        verify(hearingService, times(pcmhList.size())).autoScheduleHearing(Mockito.any(HearingWeb.class), Mockito.anyString());
    }

    @Test
    public void testProcessCrestDataLinkedCaseBatchJob() throws FileNotFoundException {
        final Long batchJobId = 10l;
        final List<CrestLinkedCase> linkedCases = new ArrayList<CrestLinkedCase>();
        Mockito.doNothing().when(crestDataBatchJobDao).deleteCrestLinkedCase();
        when(crestDataBatchJobDao.getInvalidCrestLinkedcases(batchJobId)).thenReturn(linkedCases);

        final List<CrestLinkedCase> linkedCasesData = new ArrayList<CrestLinkedCase>();
        final CrestLinkedCase lc1 = createLinkedCase("caseNumberUpdate1", "caseNumberUpdate2");
        final CrestLinkedCase lc2 = createLinkedCase("caseNumberUpdate3", "caseNumberUpdate4");
        linkedCasesData.add(lc1);
        linkedCasesData.add(lc2);
        when(crestDataBatchJobDao.getCrestLinkedCases(batchJobId)).thenReturn(linkedCasesData);
        Mockito.doNothing().when(courtCaseService).saveUpdateLinkedCaseForCase(Mockito.any(CourtCaseWeb.class));
        crestDataProcessingService.processCrestLinkedCaseBatchJob(batchJobId);
        verify(courtCaseService, times(linkedCasesData.size())).saveUpdateLinkedCaseForCase(Mockito.any(CourtCaseWeb.class));
    }

    @Test
    public void testProcessCrestInvalidDataDefendantBatchJob() throws FileNotFoundException {
        final Long batchJobId = 10l;
        final List<CrestDefendant> defendantData = new ArrayList<CrestDefendant>();
        final CrestDefendant crestDefendant1 = createDefendant("caseNumberUpdate", "Jose Maria del Nido");
        final CrestDefendant crestDefendant2 = createDefendant("caseNumberUpdate", "Barcenas");
        defendantData.add(crestDefendant1);
        defendantData.add(crestDefendant2);
        when(crestDataBatchJobDao.getInvalidCrestdefendant(batchJobId)).thenReturn(defendantData);
        try {
            crestDataProcessingService.processCrestDefendantBatchJob(batchJobId);
            fail();
        } catch (final Exception e) {
            assertTrue(e.getMessage().contains("Invalid defendants file"));
        }
        verify(courtCaseService, times(0)).saveUpdateDefendantForCase(Mockito.any(CourtCaseWeb.class));
    }

    @Test
    public void testProcessCrestInvalidDataNotesBatchJob() throws FileNotFoundException {
        final Long batchJobId = 10l;
        final List<CrestNote> notesData = new ArrayList<CrestNote>();
        final CrestNote note1 = createNote("caseNumberUpdate", "Jose Maria del Nido");
        final CrestNote note2 = createNote("caseNumberUpdate", "Barcenas");
        notesData.add(note1);
        notesData.add(note2);
        when(crestDataBatchJobDao.getInvalidCrestNotes(batchJobId)).thenReturn(notesData);
        try {
            crestDataProcessingService.processCrestNotesBatchJob(batchJobId);
            fail();
        } catch (final Exception e) {
            assertTrue(e.getMessage().contains("Invalid notes file"));
        }
        verify(courtCaseService, times(0)).saveUpdateCaseNoteForCase(Mockito.any(CourtCaseWeb.class));
    }

    @Test
    public void testProcessCrestInvalidDataNonAvBatchJob() throws FileNotFoundException {
        final Long batchJobId = 10l;
        final List<CrestNonAvailable> nonAvData = new ArrayList<CrestNonAvailable>();
        final CrestNonAvailable nonAv1 = createNonAv("caseNumberUpdate", "Jose Maria del Nido to prison");
        final CrestNonAvailable nonAv2 = createNonAv("caseNumberUpdate", "Barcenas to prison");
        nonAvData.add(nonAv1);
        nonAvData.add(nonAv2);
        when(crestDataBatchJobDao.getInvalidCrestNonAvail(batchJobId)).thenReturn(nonAvData);
        try {
            crestDataProcessingService.processCrestNonAvailBatchJob(batchJobId);
            fail();
        } catch (final Exception e) {
            assertTrue(e.getMessage().contains(getMessage("INVALID_NON_AVAILABILITY_FILE")));
        }
        verify(courtCaseService, times(0)).saveUpdateNotAvailableDateForCase(Mockito.any(NotAvailableDatesWeb.class));
    }

    @Test
    public void testProcessCrestInvalidDataPcmhBatchJob() throws FileNotFoundException {
        final Long batchJobId = 10l;
        final List<CrestPcmh> pcmhList = new ArrayList<CrestPcmh>();
        final CrestPcmh pcmh1 = createPcmh("caseNumberUpdate", today);
        final CrestPcmh pcmh2 = createPcmh("caseNumberUpdate", DateUtils.addDays(today, 5));
        pcmhList.add(pcmh1);
        pcmhList.add(pcmh2);
        when(crestDataBatchJobDao.getInvalidCrestHearing(batchJobId)).thenReturn(pcmhList);
        try {
            crestDataProcessingService.processCrestHearingBatchJob(batchJobId);
            fail();
        } catch (final Exception e) {
            assertTrue(e.getMessage().contains(getMessage("INVALID_PCMH_FILE")));
        }
        verify(hearingService, times(0)).saveUpdateHearing(Mockito.any(HearingWeb.class));
        verify(hearingService, times(0)).autoScheduleHearing(Mockito.any(HearingWeb.class), Mockito.anyString());
    }

    @Test
    public void testProcessCrestInvalidDataLinkedCaseBatchJob() throws FileNotFoundException {
        final Long batchJobId = 10l;
        final List<CrestLinkedCase> linkedCasesData = new ArrayList<CrestLinkedCase>();
        final CrestLinkedCase lc1 = createLinkedCase("caseNumberUpdate1", "caseNumberUpdate2");
        final CrestLinkedCase lc2 = createLinkedCase("caseNumberUpdate3", "caseNumberUpdate4");
        linkedCasesData.add(lc1);
        linkedCasesData.add(lc2);
        when(crestDataBatchJobDao.getInvalidCrestLinkedcases(batchJobId)).thenReturn(linkedCasesData);
        try {
            crestDataProcessingService.processCrestLinkedCaseBatchJob(batchJobId);
            fail();
        } catch (final Exception e) {
            assertTrue(e.getMessage().contains("Invalid linked case file"));
        }
        verify(courtCaseService, times(0)).saveUpdateNotAvailableDateForCase(Mockito.any(NotAvailableDatesWeb.class));
    }

    private CaseRelated createCaseRelated(final String caseNumber) {
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setLeadDefendant("caseDescription");
        caseRelated.setMostSeriousOffence("mostSeriousOffence");
        caseRelated.setCrestCaseNumber(caseNumber);
        caseRelated.setEndDate(new Date());
        caseRelated.setOffenceClass(OffenceClass.CLASS1);
        caseRelated.setReleaseDecisionStatus(ReleaseDecision.AR);
        caseRelated.setTicketingRequirement(TicketingRequirement.FRA);
        caseRelated.setTrialEstimate(2);
        return caseRelated;
    }

    private static List<CrestData> crestData(final String caseNumber) {
        final List<CrestData> list = new ArrayList<CrestData>();
        list.add(createData(caseNumber, "nonAvailableDateReason1", "caseNoteText1", "linkedCases1", "defendantSurname1", today));
        return list;
    }

    private static CrestData createData(final String caseNumber, final String nonAvailableDateReason, final String caseNoteText, final String linkedCases, final String defendantSurname,
            final Date pcmhearingDate) {
        final CrestDataBatchJob crestDataBatchJob1 = new CrestDataBatchJob();
        crestDataBatchJob1.setComments("comments");
        crestDataBatchJob1.setFileName("fileName1");
        crestDataBatchJob1.setProcessingState(ProcessingStatus.AWAITING);
        final CrestData crestData1 = new CrestData();
        crestData1.setCrestDataBatchJob(crestDataBatchJob1);
        crestData1.setCaseNumber(caseNumber);
        crestData1.setCaseTitle(caseNumber);
        crestData1.setNoOfDefendants(1);
        crestData1.setOffence("offence");
        crestData1.setOffenceClass(OffenceClass.CLASS2);

        crestData1.setTrailEstProsecutionDuration(1.1d);
        crestData1.setTrailEstProsecutionUnits(TimeEstimationUnit.DAYS);
        crestData1.setTrailEstListingOfficerDuration(1.1d);
        crestData1.setTrailEstListingOfficerUnits(TimeEstimationUnit.DAYS);
        crestData1.setTicketingType(TicketingRequirement.ATT);
        crestData1.setTicketingRequirement("ticketingRequirement");
        crestData1.setSentForTrailDate(today);
        crestData1.setCommitalDate(today);
        return crestData1;
    }

    private static CrestNonAvailable createNonAv(final String caseNumber, final String reason) {
        final CrestNonAvailable crestNonAvailable = new CrestNonAvailable();
        crestNonAvailable.setNonAvailableDateStart(today);
        crestNonAvailable.setNonAvailableDateEnd(today);
        crestNonAvailable.setNonAvailableDateReason(reason);
        return crestNonAvailable;
    }

    private static CrestDefendant createDefendant(final String caseNumber, final String defendant) {
        final CrestDefendant crestDefendant = new CrestDefendant();
        crestDefendant.setCaseNumber(caseNumber);
        crestDefendant.setDefendantForeNameOne("defendantForeNameOne");
        crestDefendant.setDefendantForeNameTwo("defendantForeNameTwo");
        crestDefendant.setDefendantSurname(defendant);
        crestDefendant.setBailCustodyStatus(CustodyStatus.BAIL);
        crestDefendant.setPtiURN("ptiURN");
        return crestDefendant;
    }

    private static CrestNote createNote(final String caseNumber, final String caseNoteText) {
        final CrestNote crestNote = new CrestNote();
        crestNote.setCaseNoteType(CaseNoteType.CASE_NOTE);
        crestNote.setCaseNotePrintIndicator(CaseNotePrintType.PRIORITY);
        crestNote.setCaseNoteDate(today);
        crestNote.setCaseNoteText(caseNoteText);
        crestNote.setCaseNoteDiaryDate(today);
        return crestNote;
    }

    private static CrestPcmh createPcmh(final String caseNumber, final Date date) {
        final CrestPcmh crestPcmh = new CrestPcmh();
        crestPcmh.setPCMHDate(date);
        crestPcmh.setCaseNumber(caseNumber);
        return crestPcmh;
    }

    private static CrestLinkedCase createLinkedCase(final String caseNumber1, final String caseNumber2) {
        final CrestLinkedCase crestLinkedCase = new CrestLinkedCase();
        crestLinkedCase.setCaseNumber(caseNumber1);
        crestLinkedCase.setLinkedCases(caseNumber2);
        return crestLinkedCase;
    }

}