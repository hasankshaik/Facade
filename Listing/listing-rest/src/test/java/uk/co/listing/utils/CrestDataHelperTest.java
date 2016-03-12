package uk.co.listing.utils;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

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
import uk.co.listing.domain.constant.HearingType;
import uk.co.listing.domain.constant.OffenceClass;
import uk.co.listing.domain.constant.ProcessingStatus;
import uk.co.listing.domain.constant.TicketingRequirement;
import uk.co.listing.domain.constant.TimeEstimationUnit;
import uk.co.listing.rest.response.CourtCaseWeb;
import uk.co.listing.rest.response.HearingWeb;
import uk.co.listing.rest.response.NotAvailableDatesWeb;

public class CrestDataHelperTest {
    private static Date today = DateUtils.truncate(new Date(), Calendar.DATE);
    private static Date nextDate = DateUtils.addDays(today, 1);

    @Test
    public void testCreateCourtCaseWeb() {
        final List<CourtCaseWeb> listCaseWeb = CrestDataHelper.createCourtCaseWeb(crestData());
        assertTrue(listCaseWeb.size() == 2);
    }

    @Test
    public void testCreateCourtCaseWebWithNulls() {
        final List<CourtCaseWeb> listCaseWeb = CrestDataHelper.createCourtCaseWeb(crestDataNulls());
        assertTrue(listCaseWeb.size() == 1);
    }

    @Test
    public void testCreateCourtCaseWebWithMinimalData() {
        final List<CourtCaseWeb> listCaseWeb = CrestDataHelper.createCourtCaseWeb(Arrays.asList(createMinmalData("caseNumber")));
        assertTrue(listCaseWeb.size() == 1);
        for (final CourtCaseWeb courtCaseWeb : listCaseWeb) {
            assertTrue(courtCaseWeb.getCrustNonAvailableDateList().size() == 0);
            assertTrue(courtCaseWeb.getLinkedCases().size() == 0);
            assertTrue(courtCaseWeb.getNotesWeb().size() == 0);
            assertTrue(courtCaseWeb.getHearings().size() == 0);
        }

    }

    private static List<CrestData> crestData() {
        final List<CrestData> list = new ArrayList<CrestData>();
        list.add(createData("caseNumber1", "nonAvailableDateReason1", "caseNoteText1", "linkedCases1", "defendantSurname1", today));
        list.add(createData("caseNumber1", "nonAvailableDateReason1", "caseNoteText1", "linkedCases1", "defendantSurname2", today));
        list.add(createData("caseNumber1", "nonAvailableDateReason1", "caseNoteText1", "linkedCases2", "defendantSurname1", today));
        list.add(createData("caseNumber1", "nonAvailableDateReason1", "caseNoteText1", "linkedCases2", "defendantSurname2", nextDate));
        list.add(createData("caseNumber1", "nonAvailableDateReason1", "caseNoteText2", "linkedCases1", "defendantSurname1", today));
        list.add(createData("caseNumber1", "nonAvailableDateReason1", "caseNoteText2", "linkedCases1", "defendantSurname2", today));
        list.add(createData("caseNumber1", "nonAvailableDateReason1", "caseNoteText2", "linkedCases2", "defendantSurname1", today));
        list.add(createData("caseNumber1", "nonAvailableDateReason1", "caseNoteText2", "linkedCases2", "defendantSurname2", today));

        list.add(createData("caseNumber1", "nonAvailableDateReason2", "caseNoteText1", "linkedCases1", "defendantSurname1", today));
        list.add(createData("caseNumber1", "nonAvailableDateReason2", "caseNoteText1", "linkedCases1", "defendantSurname2", nextDate));
        list.add(createData("caseNumber1", "nonAvailableDateReason2", "caseNoteText1", "linkedCases2", "defendantSurname1", today));
        list.add(createData("caseNumber1", "nonAvailableDateReason2", "caseNoteText1", "linkedCases2", "defendantSurname2", today));
        list.add(createData("caseNumber1", "nonAvailableDateReason2", "caseNoteText2", "linkedCases1", "defendantSurname1", today));
        list.add(createData("caseNumber1", "nonAvailableDateReason2", "caseNoteText2", "linkedCases1", "defendantSurname2", today));
        list.add(createData("caseNumber1", "nonAvailableDateReason2", "caseNoteText2", "linkedCases2", "defendantSurname1", today));
        list.add(createData("caseNumber1", "nonAvailableDateReason2", "caseNoteText2", "linkedCases2", "defendantSurname2", today));

        list.add(createData("caseNumber2", "nonAvailableDateReason1", "caseNoteText1", "linkedCases1", "defendantSurname1", today));
        list.add(createData("caseNumber2", "nonAvailableDateReason2", "caseNoteText2", "linkedCases2", "defendantSurname2", nextDate));

        return list;
    }

    private static List<CrestData> crestDataNulls() {
        final List<CrestData> list = new ArrayList<CrestData>();
        list.add(createData("caseNumber1", null, null, "linkedCases1", "defendantSurname1", today));
        list.add(createData("caseNumber1", null, "caseNoteText1", "linkedCases1", "defendantSurname2", null));
        list.add(createData("caseNumber1", null, "caseNoteText1", null, "defendantSurname1", today));
        list.add(createData("caseNumber1", null, "caseNoteText1", "linkedCases2", "defendantSurname2", today));
        list.add(createData("caseNumber1", "nonAvailableDateReason1", "caseNoteText2", "linkedCases1", "defendantSurname1", today));
        list.add(createData("caseNumber1", "nonAvailableDateReason1", "caseNoteText2", "linkedCases1", "defendantSurname2", null));
        list.add(createData("caseNumber1", "nonAvailableDateReason1", null, "linkedCases2", "defendantSurname1", today));
        list.add(createData("caseNumber1", "nonAvailableDateReason1", "caseNoteText2", "linkedCases2", "defendantSurname2", today));

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

    private static CrestData createMinmalData(final String caseNumber) {
        final CrestDataBatchJob crestDataBatchJob1 = new CrestDataBatchJob();
        crestDataBatchJob1.setComments("comments");
        crestDataBatchJob1.setFileName("fileName1");
        crestDataBatchJob1.setProcessingState(ProcessingStatus.AWAITING);

        final CrestData crestData1 = new CrestData();
        crestData1.setCrestDataBatchJob(crestDataBatchJob1);
        crestData1.setCaseNumber(caseNumber);
        crestData1.setCaseTitle(caseNumber);
        crestData1.setNoOfDefendants(1);
        crestData1.setOffenceClass(OffenceClass.CLASS2);
        return crestData1;

    }

    @Test
    public void testCaseDefendantToWeb() {
        final CourtCaseWeb courtCaseWeb = CrestDataHelper.caseDefendantToWeb(createDefendantData("caseNumber"));
        final CourtCaseWeb courtCaseWeb1 = CrestDataHelper.caseDefendantToWeb(createDefendantInCustodyData("caseNumber1"));
        assertTrue(courtCaseWeb.getPersonInCaseList().size() == 1);
        assertTrue(courtCaseWeb1.getPersonInCaseList().size() == 1);
    }

    @Test
    public void testCaseNoteToWeb() {
        final CourtCaseWeb courtCaseWeb = CrestDataHelper.caseNoteToWeb(createNoteData("caseNumber", "Del Nido a prision"));
        assertTrue(courtCaseWeb.getNotesWeb().size() == 1);
    }

    @Test
    public void testCaseNonAvToWeb() {
        final NotAvailableDatesWeb courtCaseWeb = CrestDataHelper.caseNonAvailToWeb(createNonAvData("caseNumber", "Del Nido a prision"));
        assertTrue(courtCaseWeb !=null);
    }

    @Test
    public void testLinkedCaseToWeb() {
        final CourtCaseWeb courtCaseWeb = CrestDataHelper.caseLinkedCaseToWeb(createLinkedCase("caseNumber1", "caseNumber2"));
        assertTrue(courtCaseWeb.getLinkedCases().size() == 1);
    }

    @Test
    public void testCaseEmptyPcmhToWeb() {
        final HearingWeb hearing = CrestDataHelper.hearingToWeb(creatEmptyHearingData("caseNumber"));
        assertTrue(hearing.getHearingType() == null);
        assertTrue(hearing.getActive() == null);
    }

    @Test
    public void testCasePcmhToWeb() {
        final HearingWeb hearing = CrestDataHelper.hearingToWeb(createHearingData("caseNumber"));
        assertTrue(hearing.getHearingType().equals(HearingType.PCM.name()));
        assertTrue(hearing.getActive().equals(Boolean.TRUE));
    }

    private static CrestDefendant createDefendantData(final String caseNumber) {
        final CrestDataBatchJob crestDataBatchJob1 = new CrestDataBatchJob();
        crestDataBatchJob1.setComments("comments");
        crestDataBatchJob1.setFileName("fileName1");
        crestDataBatchJob1.setProcessingState(ProcessingStatus.AWAITING);

        final CrestDefendant crestDef = new CrestDefendant();
        crestDef.setCrestDataBatchJob(crestDataBatchJob1);
        crestDef.setCaseNumber(caseNumber);
        crestDef.setDefendantForeNameOne("defendantForeNameOne");
        crestDef.setDefendantForeNameTwo("defendantForeNameTwo");
        crestDef.setDefendantSurname("defendantSurname");
        crestDef.setBailCustodyStatus(CustodyStatus.BAIL);
        crestDef.setPtiURN("ptiURN");
        return crestDef;
    }

    private static CrestDefendant createDefendantInCustodyData(final String caseNumber) {
        final CrestDataBatchJob crestDataBatchJob1 = new CrestDataBatchJob();
        crestDataBatchJob1.setComments("comments");
        crestDataBatchJob1.setFileName("fileName1");
        crestDataBatchJob1.setProcessingState(ProcessingStatus.AWAITING);

        final CrestDefendant crestDef = new CrestDefendant();
        crestDef.setCrestDataBatchJob(crestDataBatchJob1);
        crestDef.setCaseNumber(caseNumber);
        crestDef.setDefendantForeNameOne("defendantForeNameOne");
        crestDef.setDefendantForeNameTwo("defendantForeNameTwo");
        crestDef.setDefendantSurname("defendantSurname");
        crestDef.setBailCustodyStatus(CustodyStatus.CUSTODY);
        crestDef.setCtlExpiryDate(new Date());
        crestDef.setPtiURN("ptiURN");
        return crestDef;
    }

    private static CrestNonAvailable createNonAvData(final String caseNumber, final String reason) {
        final CrestDataBatchJob crestDataBatchJob1 = new CrestDataBatchJob();
        crestDataBatchJob1.setComments("comments");
        crestDataBatchJob1.setFileName("fileName1");
        crestDataBatchJob1.setProcessingState(ProcessingStatus.AWAITING);

        final CrestNonAvailable crestNonAvailable = new CrestNonAvailable();
        crestNonAvailable.setNonAvailableDateStart(today);
        crestNonAvailable.setNonAvailableDateEnd(today);
        crestNonAvailable.setNonAvailableDateReason(reason);

        return crestNonAvailable;
    }

    private static CrestNote createNoteData(final String caseNumber, final String caseNoteText) {
        final CrestDataBatchJob crestDataBatchJob1 = new CrestDataBatchJob();
        crestDataBatchJob1.setComments("comments");
        crestDataBatchJob1.setFileName("fileName1");
        crestDataBatchJob1.setProcessingState(ProcessingStatus.AWAITING);

        final CrestNote crestNote = new CrestNote();
        crestNote.setCaseNoteType(CaseNoteType.CASE_NOTE);
        crestNote.setCaseNotePrintIndicator(CaseNotePrintType.PRIORITY);
        crestNote.setCaseNoteDate(today);
        crestNote.setCaseNoteText(caseNoteText);
        crestNote.setCaseNoteDiaryDate(today);
        crestNote.setCrestDataBatchJob(crestDataBatchJob1);
        crestNote.setCaseNumber(caseNumber);

        return crestNote;
    }

    private static CrestPcmh createHearingData(final String caseNumber) {
        final CrestDataBatchJob crestDataBatchJob1 = new CrestDataBatchJob();
        crestDataBatchJob1.setComments("comments");
        crestDataBatchJob1.setFileName("fileName1");
        crestDataBatchJob1.setProcessingState(ProcessingStatus.AWAITING);

        final CrestPcmh crestPcmh = new CrestPcmh();
        crestPcmh.setPCMHDate(today);
        crestPcmh.setCrestDataBatchJob(crestDataBatchJob1);
        crestPcmh.setCaseNumber(caseNumber);

        return crestPcmh;
    }

    private static CrestPcmh creatEmptyHearingData(final String caseNumber) {
        final CrestPcmh crestPcmh = new CrestPcmh();
        return crestPcmh;
    }

    private static CrestLinkedCase createLinkedCase(final String caseNumber1, final String caseNumber2) {
        final CrestDataBatchJob crestDataBatchJob1 = new CrestDataBatchJob();
        crestDataBatchJob1.setComments("comments");
        crestDataBatchJob1.setFileName("fileName1");
        crestDataBatchJob1.setProcessingState(ProcessingStatus.AWAITING);

        final CrestLinkedCase crestLinkedCase = new CrestLinkedCase();
        crestLinkedCase.setCaseNumber(caseNumber1);
        crestLinkedCase.setLinkedCases(caseNumber2);
        crestLinkedCase.setCrestDataBatchJob(crestDataBatchJob1);
        return crestLinkedCase;
    }


}
