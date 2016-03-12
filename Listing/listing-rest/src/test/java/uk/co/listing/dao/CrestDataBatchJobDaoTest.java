package uk.co.listing.dao;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.listing.BaseTransactionalIntegrationTest;
import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.CourtCenter;
import uk.co.listing.domain.CrestData;
import uk.co.listing.domain.CrestDataBatchJob;
import uk.co.listing.domain.CrestDefendant;
import uk.co.listing.domain.CrestLinkedCase;
import uk.co.listing.domain.CrestNonAvailable;
import uk.co.listing.domain.CrestNote;
import uk.co.listing.domain.CrestPcmh;
import uk.co.listing.domain.constant.CaseNotePrintType;
import uk.co.listing.domain.constant.CaseNoteType;
import uk.co.listing.domain.constant.CustodyStatus;
import uk.co.listing.domain.constant.OffenceClass;
import uk.co.listing.domain.constant.ProcessingStatus;
import uk.co.listing.domain.constant.ReleaseDecision;
import uk.co.listing.domain.constant.TicketingRequirement;

public class CrestDataBatchJobDaoTest extends BaseTransactionalIntegrationTest {

    private static final String crestCaseNumberUpdate = "forupdate-crestCaseNumber";
    private static final String crestCaseNumberInsert = "forinsert-crestCaseNumber";
    private static final String crestCaseNumberCompletion = "forcompletion-crestCaseNumber";
    private static final String TEST_CENTRE = "test-centre";

    @Autowired
    private CrestDataBatchJobDao crestDataBatchJobDao;

    @Autowired
    private CaseRelatedDao caseRelatedDao;

    @Autowired
    private CourtCenterDao courtCenterDao;

    @Autowired
    private PersonInCaseDao personInCaseDao;

    @Test
    public void testfindTaskByProcessingStatus() {
        createData();
        final CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        assertTrue(crestDataBatchJob.getFileName().equals("fileName1"));
        final List<CrestDataBatchJob> crestDataBatchJobList = crestDataBatchJobDao.findAllTaskByProcessingStatus(ProcessingStatus.AWAITING);
        assertTrue(crestDataBatchJobList.size() == 3);
    }

    @Test
    public void testGetProcessingStatus() {
        createData();
        final List<CrestDataBatchJob> crestDataBatchJobList = crestDataBatchJobDao.getProcessingStatus();
        assertTrue(crestDataBatchJobList.size() == 3);
    }

    @Test
    public void testFindCaseByCrustCaseNumber() {
        createData();
        CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        assertTrue(crestDataBatchJob.getFileName().equals("fileName1"));
        crestDataBatchJob.setProcessingState(ProcessingStatus.PROCESSED);
        crestDataBatchJobDao.save(crestDataBatchJob);
        crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        assertTrue(crestDataBatchJob.getFileName().equals("fileName2"));
        crestDataBatchJob.setProcessingState(ProcessingStatus.PROCESSED);
        crestDataBatchJobDao.save(crestDataBatchJob);
        crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        assertTrue(crestDataBatchJob.getFileName().equals("fileName3"));
    }

    @Test
    public void testGetCreateDataForInsert() {
        createCaseRelated();
        createData();
        final CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        final List<CrestData> crestDataList = crestDataBatchJobDao.getCreateDataForInsert(crestDataBatchJob.getBatchId());
        for (final CrestData crestData : crestDataList) {
            assertTrue(crestData.getCaseNumber().equals(crestCaseNumberInsert));
        }
    }

    @Test
    public void testGetCreateDataForUpdate() {
        createCaseRelated();
        createData();
        final CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        final List<CrestData> crestDataList = crestDataBatchJobDao.getCreateDataForUpdate(crestDataBatchJob.getBatchId());
        for (final CrestData crestData : crestDataList) {
            assertTrue(crestData.getCaseNumber().equals(crestCaseNumberUpdate));
        }
    }

    @Test
    public void testGetCreateDataForCompletion() {
        createCaseRelated();
        createData();
        final CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        final List<CaseRelated> caseRelatedList = crestDataBatchJobDao.getCreateDataForCompletion(crestDataBatchJob.getBatchId());
        for (final CaseRelated caseRelated : caseRelatedList) {
            assertTrue(caseRelated.getCrestCaseNumber().equals(crestCaseNumberCompletion));
        }
    }

    @Test
    public void testGetCreateDataByBatchId() {
        createData();
        final CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        final List<CrestData> caseRelatedList = crestDataBatchJobDao.getCreateDataByBatchId(crestDataBatchJob.getBatchId());
        assertTrue(caseRelatedList.size() == 2);
    }

    @Test
    public void testGetBatchId() {
        final Long batchId = createBatch();
        final CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.getCrestDataBatchJobById(batchId);
        assertTrue(crestDataBatchJob.getFileName().equals("fileName1"));
    }

    @Test
    public void testDeleteDdefendant() {
        createCaseRelated();
        createDefendantData(crestCaseNumberUpdate);
        crestDataBatchJobDao.deleteCrestdefendant();
        final CaseRelated caseRelated = caseRelatedDao.findCaseByCrestCaseNumberAndCenter(crestCaseNumberUpdate, TEST_CENTRE);
        assertTrue(caseRelated.getPersonInCase().size() == 0);
    }

    @Test
    public void testDeleteNotes() {
        createCaseRelated();
        createNotesData(crestCaseNumberUpdate);
        crestDataBatchJobDao.deleteCrestNotes();
        final CaseRelated caseRelated = caseRelatedDao.findCaseByCrestCaseNumberAndCenter(crestCaseNumberUpdate, TEST_CENTRE);
        assertTrue(caseRelated.getNotes().size() == 0);
    }

    @Test
    public void testDeleteNonAv() {
        createCaseRelated();
        createNonAvailData(crestCaseNumberUpdate);
        crestDataBatchJobDao.deleteCrestNonAvail();
        final CaseRelated caseRelated = caseRelatedDao.findCaseByCrestCaseNumberAndCenter(crestCaseNumberUpdate, TEST_CENTRE);
        assertTrue(caseRelated.getCrustNonAvailableDatesList().size() == 0);
    }

    @Test
    public void testDeleteHearing() {
        createCaseRelated();
        createNonAvailData(crestCaseNumberUpdate);
        crestDataBatchJobDao.deleteCrestHearing();
        final CaseRelated caseRelated = caseRelatedDao.findCaseByCrestCaseNumberAndCenter(crestCaseNumberUpdate, TEST_CENTRE);
        assertTrue(caseRelated.getHearings().size() == 0);
    }

    @Test
    public void testDeleteLinkedCase() {
        createCaseRelated();
        createLinkedCase(crestCaseNumberUpdate, crestCaseNumberCompletion);
        crestDataBatchJobDao.deleteCrestLinkedCase();
        final CaseRelated caseRelated = caseRelatedDao.findCaseByCrestCaseNumberAndCenter(crestCaseNumberUpdate, TEST_CENTRE);
        assertTrue(caseRelated.getLinkedCases().size() == 0);
    }

    @Test
    public void testGetInvalidCrestdefendant() {
        createCaseRelated();
        createDefendantData("invalidCase");
        final CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        final List<CrestDefendant> crestDataList = crestDataBatchJobDao.getInvalidCrestdefendant(crestDataBatchJob.getBatchId());
        for (final CrestDefendant crestData : crestDataList) {
            assertTrue(crestData.getCaseNumber().equals("invalidCase"));
        }
    }

    @Test
    public void testGetCrestdefendant() {
        createCaseRelated();
        createDefendantData(crestCaseNumberUpdate);
        final CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        final List<CrestDefendant> crestDataList = crestDataBatchJobDao.getCrestdefendant(crestDataBatchJob.getBatchId());
        assertTrue(crestDataList.size() == 1);
    }

    @Test
    public void testGetInvalidCrestNotes() {
        createCaseRelated();
        createNotesData("invalidCase");
        final CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        final List<CrestNote> crestDataList = crestDataBatchJobDao.getInvalidCrestNotes(crestDataBatchJob.getBatchId());
        for (final CrestNote crestData : crestDataList) {
            assertTrue(crestData.getCaseNumber().equals("invalidCase"));
        }
    }

    @Test
    public void testGetCrestNotes() {
        createCaseRelated();
        createNotesData(crestCaseNumberUpdate);
        final CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        final List<CrestNote> crestDataList = crestDataBatchJobDao.getCrestNotes(crestDataBatchJob.getBatchId());
        assertTrue(crestDataList.size() == 1);
    }

    @Test
    public void testGetInvalidCrestNonAvail() {
        createCaseRelated();
        createNonAvailData("invalidCase");
        final CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        final List<CrestNonAvailable> crestDataList = crestDataBatchJobDao.getInvalidCrestNonAvail(crestDataBatchJob.getBatchId());
        for (final CrestNonAvailable crestData : crestDataList) {
            assertTrue(crestData.getCaseNumber().equals("invalidCase"));
        }
    }

    @Test
    public void testGetCrestNonAvail() {
        createCaseRelated();
        createNonAvailData(crestCaseNumberUpdate);
        final CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        final List<CrestNonAvailable> crestDataList = crestDataBatchJobDao.getCrestNonAvail(crestDataBatchJob.getBatchId());
        assertTrue(crestDataList.size() == 1);
    }

    @Test
    public void testGetInvalidCrestHearing() {
        createCaseRelated();
        createHearing("invalidCase");
        final CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        final List<CrestPcmh> crestDataList = crestDataBatchJobDao.getInvalidCrestHearing(crestDataBatchJob.getBatchId());
        for (final CrestPcmh crestData : crestDataList) {
            assertTrue(crestData.getCaseNumber().equals("invalidCase"));
        }
    }

    @Test
    public void testGetCrestHearing() {
        createCaseRelated();
        createHearing(crestCaseNumberUpdate);
        final CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        final List<CrestPcmh> crestDataList = crestDataBatchJobDao.getCrestHearing(crestDataBatchJob.getBatchId());
        assertTrue(crestDataList.size() == 1);
    }

    @Test
    public void testGetInvalidCrestLinkedCase() {
        createCaseRelated();
        createLinkedCase("invalidCase1", "invalidCase1");
        final CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        final List<CrestLinkedCase> crestDataList = crestDataBatchJobDao.getInvalidCrestLinkedcases(crestDataBatchJob.getBatchId());
        for (final CrestLinkedCase crestData : crestDataList) {
            assertTrue(crestData.getCaseNumber().equals("invalidCase1"));
        }
    }

    @Test
    public void testGetCrestLinkedCase() {
        createCaseRelated();
        createLinkedCase(crestCaseNumberUpdate, crestCaseNumberCompletion);
        final CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.findTaskByProcessingStatus(ProcessingStatus.AWAITING);
        final List<CrestLinkedCase> crestDataList = crestDataBatchJobDao.getCrestLinkedCases(crestDataBatchJob.getBatchId());
        assertTrue(crestDataList.size() == 1);
    }

    public void createCaseRelated() {
        final CourtCenter center = new CourtCenter(4041L, TEST_CENTRE);
        courtCenterDao.save(center);
        final CaseRelated caseRelated = new CaseRelated();
        caseRelated.setLeadDefendant("caseDescription");
        caseRelated.setMostSeriousOffence("mostSeriousOffence");
        caseRelated.setCrestCaseNumber(crestCaseNumberUpdate);
        caseRelated.setFromCrest(true);
        caseRelated.setEndDate(new Date());
        caseRelated.setOffenceClass(OffenceClass.CLASS1);
        caseRelated.setReleaseDecisionStatus(ReleaseDecision.AR);
        caseRelated.setTicketingRequirement(TicketingRequirement.FRA);
        caseRelated.setTrialEstimate(2);
        caseRelated.setCourtCenter(center);
        caseRelated.setCaseClosed(false);
        caseRelatedDao.save(caseRelated);

        final CaseRelated caseRelatedComplete = new CaseRelated();
        caseRelatedComplete.setLeadDefendant("caseDescription");
        caseRelatedComplete.setMostSeriousOffence("mostSeriousOffence");
        caseRelatedComplete.setCrestCaseNumber(crestCaseNumberCompletion);
        caseRelatedComplete.setFromCrest(true);
        caseRelatedComplete.setEndDate(new Date());
        caseRelatedComplete.setOffenceClass(OffenceClass.CLASS1);
        caseRelatedComplete.setReleaseDecisionStatus(ReleaseDecision.AR);
        caseRelatedComplete.setTicketingRequirement(TicketingRequirement.FRA);
        caseRelatedComplete.setTrialEstimate(2);
        caseRelatedComplete.setCourtCenter(center);
        caseRelatedDao.save(caseRelatedComplete);

    }

    private void createData() {
        final CrestDataBatchJob crestDataBatchJob1 = new CrestDataBatchJob();
        crestDataBatchJob1.setComments("comments");
        crestDataBatchJob1.setFileName("fileName1");
        crestDataBatchJob1.setProcessingState(ProcessingStatus.AWAITING);
        crestDataBatchJobDao.save(crestDataBatchJob1);
        final CrestData crestData1 = new CrestData();
        crestData1.setCrestDataBatchJob(crestDataBatchJob1);
        crestData1.setCaseNumber(crestCaseNumberInsert);
        crestData1.setCaseTitle(crestCaseNumberInsert);
        crestData1.setNoOfDefendants(1);
        crestData1.setOffenceClass(OffenceClass.CLASS2);
        crestDataBatchJobDao.save(crestData1);
        final CrestData crestData2 = new CrestData();
        crestData2.setCrestDataBatchJob(crestDataBatchJob1);
        crestData2.setCaseNumber(crestCaseNumberUpdate);
        crestData2.setCaseTitle(crestCaseNumberUpdate);
        crestData2.setNoOfDefendants(1);
        crestData2.setOffenceClass(OffenceClass.CLASS2);
        crestDataBatchJobDao.save(crestData2);

        final CrestDataBatchJob crestDataBatchJob2 = new CrestDataBatchJob();
        crestDataBatchJob2.setComments("comments");
        crestDataBatchJob2.setFileName("fileName2");
        crestDataBatchJob2.setProcessingState(ProcessingStatus.AWAITING);
        crestDataBatchJobDao.save(crestDataBatchJob2);

        final CrestData crestData21 = new CrestData();
        crestData21.setCrestDataBatchJob(crestDataBatchJob2);
        crestData21.setCaseNumber(crestCaseNumberUpdate);
        crestData21.setCaseTitle(crestCaseNumberUpdate);
        crestData21.setNoOfDefendants(1);
        crestData21.setOffenceClass(OffenceClass.CLASS2);
        crestDataBatchJobDao.save(crestData21);

        final CrestDataBatchJob crestDataBatchJob3 = new CrestDataBatchJob();
        crestDataBatchJob3.setComments("comments");
        crestDataBatchJob3.setFileName("fileName3");
        crestDataBatchJob3.setProcessingState(ProcessingStatus.AWAITING);
        crestDataBatchJobDao.save(crestDataBatchJob3);
    }

    private void createDefendantData(final String caseNumber) {
        final CrestDataBatchJob crestDataBatchJob1 = new CrestDataBatchJob();
        crestDataBatchJob1.setComments("comments");
        crestDataBatchJob1.setFileName("fileName1");
        crestDataBatchJob1.setProcessingState(ProcessingStatus.AWAITING);
        crestDataBatchJobDao.save(crestDataBatchJob1);

        final CrestDefendant crestDef = new CrestDefendant();
        crestDef.setCrestDataBatchJob(crestDataBatchJob1);
        crestDef.setCaseNumber(caseNumber);
        crestDef.setDefendantForeNameOne("defendantForeNameOne");
        crestDef.setDefendantForeNameTwo("defendantForeNameTwo");
        crestDef.setDefendantSurname("defendantSurname");
        crestDef.setBailCustodyStatus(CustodyStatus.BAIL);
        crestDef.setPtiURN("ptiURN");
        crestDataBatchJobDao.save(crestDef);
    }

    private void createNotesData(final String caseNumber) {
        final CrestDataBatchJob crestDataBatchJob1 = new CrestDataBatchJob();
        crestDataBatchJob1.setComments("comments");
        crestDataBatchJob1.setFileName("fileName1");
        crestDataBatchJob1.setProcessingState(ProcessingStatus.AWAITING);
        crestDataBatchJobDao.save(crestDataBatchJob1);

        final CrestNote crestNote = new CrestNote();
        crestNote.setCrestDataBatchJob(crestDataBatchJob1);
        crestNote.setCaseNumber(caseNumber);
        crestNote.setCaseNoteText("Note 1");
        crestNote.setCaseNoteType(CaseNoteType.CASE_NOTE);
        crestNote.setCaseNotePrintIndicator(CaseNotePrintType.STANDARD);
        crestNote.setCaseNoteDate(new Date());
        crestNote.setCaseNoteDiaryDate(new Date());
        crestDataBatchJobDao.save(crestNote);
    }

    private void createNonAvailData(final String caseNumber) {
        final CrestDataBatchJob crestDataBatchJob1 = new CrestDataBatchJob();
        crestDataBatchJob1.setComments("comments");
        crestDataBatchJob1.setFileName("fileName1");
        crestDataBatchJob1.setProcessingState(ProcessingStatus.AWAITING);
        crestDataBatchJobDao.save(crestDataBatchJob1);

        final CrestNonAvailable crestNonavail = new CrestNonAvailable();
        crestNonavail.setCrestDataBatchJob(crestDataBatchJob1);
        crestNonavail.setCaseNumber(caseNumber);
        crestNonavail.setNonAvailableDateEnd(new Date());
        crestNonavail.setNonAvailableDateStart(new Date());
        crestNonavail.setNonAvailableDateReason("non avail 1");
        crestDataBatchJobDao.save(crestNonavail);
    }

    private void createHearing(final String caseNumber) {
        final CrestDataBatchJob crestDataBatchJob1 = new CrestDataBatchJob();
        crestDataBatchJob1.setComments("comments");
        crestDataBatchJob1.setFileName("fileName1");
        crestDataBatchJob1.setProcessingState(ProcessingStatus.AWAITING);
        crestDataBatchJobDao.save(crestDataBatchJob1);

        final CrestPcmh crestPcmh = new CrestPcmh();
        crestPcmh.setCrestDataBatchJob(crestDataBatchJob1);
        crestPcmh.setPCMHDate(new Date());
        crestPcmh.setCaseNumber(caseNumber);
        crestDataBatchJobDao.save(crestPcmh);
    }

    private void createLinkedCase(final String caseNumber1, final String caseNumber2) {
        final CrestDataBatchJob crestDataBatchJob1 = new CrestDataBatchJob();
        crestDataBatchJob1.setComments("comments");
        crestDataBatchJob1.setFileName("fileName1");
        crestDataBatchJob1.setProcessingState(ProcessingStatus.AWAITING);
        crestDataBatchJobDao.save(crestDataBatchJob1);

        final CrestLinkedCase crestLinkedCase = new CrestLinkedCase();
        crestLinkedCase.setCaseNumber(caseNumber1);
        crestLinkedCase.setLinkedCases(caseNumber2);
        crestLinkedCase.setCrestDataBatchJob(crestDataBatchJob1);
        crestDataBatchJobDao.save(crestLinkedCase);
    }

    private Long createBatch() {
        final CrestDataBatchJob crestDataBatchJob1 = new CrestDataBatchJob();
        crestDataBatchJob1.setComments("comments");
        crestDataBatchJob1.setFileName("fileName1");
        crestDataBatchJob1.setProcessingState(ProcessingStatus.AWAITING);
        crestDataBatchJobDao.save(crestDataBatchJob1);
        final CrestData crestData1 = new CrestData();
        crestData1.setCrestDataBatchJob(crestDataBatchJob1);
        crestData1.setCaseNumber(crestCaseNumberInsert);
        crestData1.setCaseTitle(crestCaseNumberInsert);
        crestData1.setNoOfDefendants(1);
        crestData1.setOffenceClass(OffenceClass.CLASS2);
        crestDataBatchJobDao.save(crestData1);
        final CrestData crestData2 = new CrestData();
        crestData2.setCrestDataBatchJob(crestDataBatchJob1);
        crestData2.setCaseNumber(crestCaseNumberUpdate);
        crestData2.setCaseTitle(crestCaseNumberUpdate);
        crestData2.setNoOfDefendants(1);
        crestData2.setOffenceClass(OffenceClass.CLASS2);
        crestDataBatchJobDao.save(crestData2);
        return crestDataBatchJob1.getBatchId();
    }
}
