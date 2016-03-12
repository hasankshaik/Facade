package uk.co.listing.service;

import static uk.co.listing.rest.message.MessageBundler.getMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.co.listing.dao.CrestDataBatchJobDao;
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
import uk.co.listing.domain.constant.TicketingRequirement;
import uk.co.listing.domain.constant.TimeEstimationUnit;
import uk.co.listing.exceptions.CcsException;
import uk.co.listing.rest.response.CrestDataBatchJobWeb;
import uk.co.listing.utils.DataTransformationHelper;
import uk.co.listing.utils.DateTimeUtils;

@Service("crestDataImportService")
@Transactional(readOnly = true)
public class CrestDataImportService implements ICrestDataImportService {


    private static final Logger LOGGER = Logger.getLogger(CrestDataImportService.class);

    private static final String CASE_HEADER_LINE = "CASE_NO_03|CASE_TITLE_04|NO_DEFTS_05|CHARGE_06A|CASE_CLASS_06B|LENGTH_P_08A|LENGTH_UNIT_P_08B|LENGTH_LO_08C|LENGTH_UNIT_LO_08D|TICKET_REQ_14A|TICKET_TYPE_14B|SENT_DATE_24A|COMM_DATE24B";
    private static final String DEFENDANT_HEADER_LINE = "PARENT_CASE|FORENAME1_11A|FORENAME2_11B|SURNAME_11C|BC_STATUS_09|CTL_EXPIRY_10|URN_13";
    private static final String NOTE_HEADER_LINE = "PARENT_CASE|NOTE_TYPE_12|NOTE_PRINT_15A|NOTE_DATE_15B|NOTE_15C|NOTE_DIARY_DATE_15D";
    private static final String NONAV_HEADER_LINE = "PARENT_CASE|NON_START_16A|NON_END_16B|NON_REASON_16C";
    private static final String HEARING_HEADER_LINE = "PARENT_CASE|PCMH_DATE_07";
    private static final String LINKCASE_HEADER_LINE = "PARENT_CASE|LINKED_CASE_17";

    @Autowired
    private CrestDataBatchJobDao crestDataBatchJobDao;

    @Autowired
    private IAsyncTaskProcessingService asyncTaskProcessingService;

    @Override
    public List<CrestDataBatchJobWeb> getCrestDataProcessingStatus() {
        final List<CrestDataBatchJob> crestDataBatchJobList = crestDataBatchJobDao.getProcessingStatus();
        return DataTransformationHelper.crestDataBatchJobToWeb(crestDataBatchJobList);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void importCrestData(final InputStream uploadedInputStream, final String fileName) {
        try (final ZipInputStream zipInput = new ZipInputStream(uploadedInputStream)) {
            if (!fileNameIsvalid(fileName)) {
                throw new CcsException(getMessage("FILE_NAME_MUST_START_WITH_COURT_CENTER_CODE"));
            }
            final CrestDataBatchJob crestDataBatchJob = new CrestDataBatchJob();
            crestDataBatchJob.setFileName(fileName);
            crestDataBatchJob.setProcessingState(ProcessingStatus.AWAITING);
            final int numberOfFile = handleCrestFile(zipInput, crestDataBatchJob);
            if (numberOfFile != 6) {
                LOGGER.error("total number of files in zip are invalid");
                throw new CcsException(getMessage("INVALID_FILE"));
            }
            asyncTaskProcessingService.process(crestDataBatchJob.getBatchId());

        } catch (final Exception e) {
            LOGGER.error(e);
            throw new CcsException(e.getMessage());
        }
        LOGGER.info("Crest data imported");
    }

    private void checkCaseCardinality(final CrestData crestData) {

        if (crestData.getCaseNumber() == null) {
            throw new CcsException(getMessage("CARDINALITY_ERROR_NO_CASE_NUMBER"));
        }
        if (((crestData.getTrailEstListingOfficerDuration() == null) || (crestData.getTrailEstListingOfficerUnits() == null))
                && !((crestData.getTrailEstListingOfficerDuration() == null) && (crestData.getTrailEstListingOfficerUnits() == null))) {
            throw new CcsException(getMessage("CARDINALITY_ERROR_TRAIL_ESTIMATE_LISTING_OFFICER"));
        }

    }

    private void checkDefendantCardinality(final CrestDefendant crestDefendant) {
        if (crestDefendant.getCaseNumber() == null) {
            throw new CcsException(getMessage("CARDINALITY_ERROR_DEFENDANT_DETAILS"));
        }

        if (((crestDefendant.getDefendantSurname() == null) || (crestDefendant.getBailCustodyStatus() == null))
                && !((crestDefendant.getDefendantSurname() == null) && (crestDefendant.getBailCustodyStatus() == null))) {
            throw new CcsException(getMessage("CARDINALITY_ERROR_DEFENDANT_DETAILS"));
        }
    }

    private void checkNameFIle(final ZipEntry zipEntry) {
        if (!fileInnerNameIsvalid(zipEntry.getName())) {
            throw new CcsException(getMessage("FILE_NAME_MUST_START_WITH_COURT_CENTER_CODE"));
        }
    }

    private void checkNonAvCardinality(final CrestNonAvailable crestNonAv) {
        if (crestNonAv.getCaseNumber() == null) {
            throw new CcsException(getMessage("CARDINALITY_ERROR_NON_AVAILABLE_DATES"));
        }
        if (isAnyAvailableDateNull(crestNonAv) && !isAllAvalableDatesNull(crestNonAv)) {
            throw new CcsException(getMessage("CARDINALITY_ERROR_NON_AVAILABLE_DATES"));
        }
    }

    private void checkNoteCardinality(final CrestNote crestNote) {
        if (crestNote.getCaseNumber() == null) {
            throw new CcsException(getMessage("CARDINALITY_ERROR_CASE_NOTE"));
        }

        if (((crestNote.getCaseNoteDate() == null) || (crestNote.getCaseNoteText() == null)) && !((crestNote.getCaseNoteDate() == null) && (crestNote.getCaseNoteText() == null))) {
            throw new CcsException(getMessage("CARDINALITY_ERROR_CASE_NOTE"));
        }
    }

    private void extractCaseDetails(final BufferedReader br, final CrestDataBatchJob crestDataBatchJob) throws IOException {
        String strLine;
        int lineNumber = 1;
        if (validateCaseColumnHeader(br.readLine())) {
            lineNumber++;
            while ((strLine = br.readLine()) != null) {
                try {
                    final String[] tokens = strLine.split("\\|", -1);
                    final CrestData crestData = getCrestData(crestDataBatchJob, tokens);
                    crestDataBatchJobDao.save(crestData);
                } catch (final Exception e) {
                    throw new CcsException("File upload fail, error at case line " + lineNumber + "{ " + e.getMessage() + " }", e);
                }
                LOGGER.info(" Successfully read case line number : " + lineNumber);
                lineNumber++;
            }
        } else {
            LOGGER.error(getMessage("INVALID_FILE"));
            throw new CcsException(getMessage("INVALID_FILE"));
        }
    }

    private void extractDefendantDetails(final BufferedReader br, final CrestDataBatchJob crestDataBatchJob) throws IOException {
        String strLine;
        int lineNumber = 1;
        if (validateDefendantColumnHeader(br.readLine())) {
            lineNumber++;
            while ((strLine = br.readLine()) != null) {
                try {
                    final String[] tokens = strLine.split("\\|", -1);
                    final CrestDefendant crestDefendant = getCrestDefendant(crestDataBatchJob, tokens);
                    crestDataBatchJobDao.save(crestDefendant);

                } catch (final Exception e) {
                    throw new CcsException("File upload fail, error at defendant line " + lineNumber + "{ " + e.getMessage() + " }", e);
                }
                LOGGER.info(" Successfully read defendant line number : " + lineNumber);
                lineNumber++;
            }
        } else {
            LOGGER.error(getMessage("INVALID_FILE"));
            throw new CcsException(getMessage("INVALID_FILE"));
        }
    }

    private void extractHearingDetails(final BufferedReader br, final CrestDataBatchJob crestDataBatchJob) throws IOException {
        String strLine;
        int lineNumber = 1;
        if (validatePcmhColumnHeader(br.readLine())) {
            lineNumber++;
            while ((strLine = br.readLine()) != null) {
                try {
                    final String[] tokens = strLine.split("\\|", -1);
                    final CrestPcmh crestPcmh = getCrestHearing(crestDataBatchJob, tokens);
                    crestDataBatchJobDao.save(crestPcmh);

                } catch (final Exception e) {
                    throw new CcsException("File upload fail, error at hearing line " + lineNumber + "{ " + e.getMessage() + " }", e);
                }
                LOGGER.info(" Successfully read hearing line number : " + lineNumber);
                lineNumber++;
            }
        } else {
            LOGGER.error(getMessage("INVALID_FILE"));
            throw new CcsException(getMessage("INVALID_FILE"));
        }
    }

    private void extractLinksDetails(final BufferedReader br, final CrestDataBatchJob crestDataBatchJob) throws IOException {
        int lineNumber = 1;
        String strLine;
        if (validateLinkCaseColumnHeader(br.readLine())) {
            lineNumber++;
            while ((strLine = br.readLine()) != null) {
                try {
                    final String[] tokens = strLine.split("\\|", -1);
                    final CrestLinkedCase crestLinkedCase = getCrestLinkedCase(crestDataBatchJob, tokens);
                    crestDataBatchJobDao.save(crestLinkedCase);

                } catch (final Exception e) {
                    throw new CcsException("File upload fail, error at linked case line " + lineNumber + "{ " + e.getMessage() + " }", e);
                }
                LOGGER.info(" Successfully read linked case line number : " + lineNumber);
                lineNumber++;
            }
        } else {
            LOGGER.error(getMessage("INVALID_FILE"));
            throw new CcsException(getMessage("INVALID_FILE"));
        }
    }

    private void extractNonAvDetails(final BufferedReader br, final CrestDataBatchJob crestDataBatchJob) throws IOException {
        int lineNumber = 1;
        String strLine;
        if (validateNonAvColumnHeader(br.readLine())) {
            lineNumber++;
            while ((strLine = br.readLine()) != null) {
                try {
                    final String[] tokens = strLine.split("\\|", -1);
                    final CrestNonAvailable crestNonAv = new CrestNonAvailable();
                    crestNonAv.setCaseNumber(StringUtils.isBlank(tokens[0]) ? null : tokens[0]);
                    crestNonAv.setNonAvailableDateStart(StringUtils.isBlank(tokens[1]) ? null : DateTimeUtils.parseDate(tokens[1]));
                    crestNonAv.setNonAvailableDateEnd(StringUtils.isBlank(tokens[2]) ? null : DateTimeUtils.parseDate(tokens[2]));
                    crestNonAv.setNonAvailableDateReason(StringUtils.isBlank(tokens[3]) ? null : tokens[3]);
                    crestNonAv.setCrestDataBatchJob(crestDataBatchJob);
                    checkNonAvCardinality(crestNonAv);
                    crestDataBatchJobDao.save(crestNonAv);

                } catch (final Exception e) {
                    throw new CcsException("File upload fail, error at non available line " + lineNumber + "{ " + e.getMessage() + " }", e);
                }
                LOGGER.info(" Successfully read non available line number : " + lineNumber);
                lineNumber++;
            }
        } else {
            LOGGER.error(getMessage("INVALID_FILE"));
            throw new CcsException(getMessage("INVALID_FILE"));
        }
    }

    private void extractNotesDetails(final BufferedReader br, final CrestDataBatchJob crestDataBatchJob) throws IOException {
        String strLine;
        int lineNumber = 1;
        if (validateNoteColumnHeader(br.readLine())) {
            lineNumber++;
            while ((strLine = br.readLine()) != null) {
                try {
                    final String[] tokens = strLine.split("\\|", -1);
                    final CrestNote crestNote = getCrestNote(crestDataBatchJob, tokens);
                    crestDataBatchJobDao.save(crestNote);
                } catch (final Exception e) {
                    throw new CcsException("File upload fail, error at notes line " + lineNumber + "{ " + e.getMessage() + " }", e);
                }
                LOGGER.info(" Successfully read notes line number : " + lineNumber);
                lineNumber++;
            }
        } else {
            LOGGER.error(getMessage("INVALID_FILE"));
            throw new CcsException(getMessage("INVALID_FILE"));
        }
    }

    private boolean fileInnerNameIsvalid(final String fileName) {
        if (fileName.contains("404")) {
            return true;
        }
        return false;
    }

    private boolean fileNameIsvalid(final String fileName) {

        final String courtCenterCode = fileName.substring(0, 3);
        if ("404".equals(courtCenterCode)) {
            return true;
        }
        return false;
    }

    private CrestData getCrestData(final CrestDataBatchJob crestDataBatchJob, final String[] tokens) {
        final CrestData crestData = new CrestData();
        crestData.setCaseNumber(StringUtils.isBlank(tokens[0]) ? null : tokens[0]);
        crestData.setCaseTitle(StringUtils.isBlank(tokens[1]) ? null : tokens[1]);
        crestData.setNoOfDefendants(new Integer(tokens[2]));
        crestData.setOffence(StringUtils.isBlank(tokens[3]) ? null : tokens[3]);
        crestData.setOffenceClass(StringUtils.isBlank(tokens[4]) ? OffenceClass.NONE : OffenceClass.getOffenceClass(tokens[4]));
        handleCrestDataEstimation(tokens, crestData);
        crestData.setTicketingRequirement(StringUtils.isBlank(tokens[9]) ? null : tokens[9]);
        crestData.setTicketingType(StringUtils.isBlank(tokens[10]) ? null : TicketingRequirement.valueOf(tokens[10]));
        crestData.setSentForTrailDate(StringUtils.isBlank(tokens[11]) ? null : DateTimeUtils.parseDate(tokens[11]));
        crestData.setCommitalDate(StringUtils.isBlank(tokens[12]) ? null : DateTimeUtils.parseDate(tokens[12]));
        crestData.setCrestDataBatchJob(crestDataBatchJob);
        checkCaseCardinality(crestData);
        return crestData;
    }

    private CrestDefendant getCrestDefendant(final CrestDataBatchJob crestDataBatchJob, final String[] tokens) {
        final CrestDefendant crestDefendant = new CrestDefendant();
        crestDefendant.setCaseNumber(StringUtils.isBlank(tokens[0]) ? null : tokens[0]);
        crestDefendant.setDefendantForeNameOne(StringUtils.isBlank(tokens[1]) ? null : tokens[1]);
        crestDefendant.setDefendantForeNameTwo(StringUtils.isBlank(tokens[2]) ? null : tokens[2]);
        crestDefendant.setDefendantSurname(StringUtils.isBlank(tokens[3]) ? null : tokens[3]);
        crestDefendant.setBailCustodyStatus(StringUtils.isBlank(tokens[4]) ? CustodyStatus.BAIL : CustodyStatus.getCustodyStatusForCrest(tokens[4]));
        crestDefendant.setCtlExpiryDate(StringUtils.isBlank(tokens[5]) ? null : DateTimeUtils.parseDate(tokens[5]));
        crestDefendant.setPtiURN(StringUtils.isBlank(tokens[6]) ? null : tokens[6]);
        crestDefendant.setCrestDataBatchJob(crestDataBatchJob);
        checkDefendantCardinality(crestDefendant);
        return crestDefendant;
    }

    private CrestPcmh getCrestHearing(final CrestDataBatchJob crestDataBatchJob, final String[] tokens) {
        final CrestPcmh crestPcmh = new CrestPcmh();
        crestPcmh.setCaseNumber(StringUtils.isBlank(tokens[0]) ? null : tokens[0]);
        crestPcmh.setPCMHDate(StringUtils.isBlank(tokens[1]) ? null : DateTimeUtils.parseDate(tokens[1]));
        crestPcmh.setCrestDataBatchJob(crestDataBatchJob);
        if ((crestPcmh.getPCMHDate() == null) || (crestPcmh.getCaseNumber() == null)) {
            throw new CcsException(getMessage("CARDINALITY_ERROR_HERAING_DETAILS"));
        }
        return crestPcmh;
    }

    private CrestLinkedCase getCrestLinkedCase(final CrestDataBatchJob crestDataBatchJob, final String[] tokens) {
        final CrestLinkedCase crestLinkedCase = new CrestLinkedCase();
        crestLinkedCase.setCaseNumber(StringUtils.isBlank(tokens[0]) ? null : tokens[0]);
        crestLinkedCase.setLinkedCases(StringUtils.isBlank(tokens[1]) ? null : tokens[1]);
        crestLinkedCase.setCrestDataBatchJob(crestDataBatchJob);
        if ((crestLinkedCase.getCaseNumber() == null) || (crestLinkedCase.getLinkedCases() == null)) {
            throw new CcsException(getMessage("CARDINALITY_ERROR_LINKED_CASES"));
        }
        return crestLinkedCase;
    }

    private CrestNote getCrestNote(final CrestDataBatchJob crestDataBatchJob, final String[] tokens) {
        final CrestNote crestNote = new CrestNote();
        crestNote.setCaseNumber(StringUtils.isBlank(tokens[0]) ? null : tokens[0]);
        crestNote.setCaseNoteType(StringUtils.isBlank(tokens[1]) ? null : CaseNoteType.getCaseNoteType(tokens[1]));
        crestNote.setCaseNotePrintIndicator(StringUtils.isBlank(tokens[2]) ? null : CaseNotePrintType.getCaseNotePrintType(tokens[2]));
        crestNote.setCaseNoteDate(StringUtils.isBlank(tokens[3]) ? null : DateTimeUtils.parseDate(tokens[3]));
        crestNote.setCaseNoteText(StringUtils.isBlank(tokens[4]) ? null : tokens[4]);
        crestNote.setCaseNoteDiaryDate(StringUtils.isBlank(tokens[5]) ? null : DateTimeUtils.parseDate(tokens[5]));
        crestNote.setCrestDataBatchJob(crestDataBatchJob);
        checkNoteCardinality(crestNote);
        return crestNote;
    }

    private void handleCrestDataEstimation(final String[] tokens, final CrestData crestData) {
        crestData.setTrailEstProsecutionDuration(StringUtils.isBlank(tokens[5]) ? null : Double.valueOf(tokens[5]));
        crestData.setTrailEstProsecutionUnits(StringUtils.isBlank(tokens[6]) ? null : TimeEstimationUnit.getTimeEstimationUnitForCrest(tokens[6]));
        crestData.setTrailEstListingOfficerDuration(StringUtils.isBlank(tokens[7]) ? null : Double.valueOf(tokens[7]));
        crestData.setTrailEstListingOfficerUnits(StringUtils.isBlank(tokens[8]) ? null : TimeEstimationUnit.getTimeEstimationUnitForCrest(tokens[8]));
    }

    private int handleCrestFile(final ZipInputStream zipInput, final CrestDataBatchJob crestDataBatchJob) throws IOException {
        int numberOfFile = 0;
        ZipEntry zipEntry;
        while ((zipEntry = zipInput.getNextEntry()) != null) {
            if (!zipEntry.isDirectory()) {
                checkNameFIle(zipEntry);
                final InputStreamReader isr = new InputStreamReader(zipInput);
                final BufferedReader br = new BufferedReader(isr);
                crestDataBatchJobDao.save(crestDataBatchJob);
                if (zipEntry.getName().toLowerCase().contains("case")) {
                    extractCaseDetails(br, crestDataBatchJob);
                    numberOfFile++;
                } else if (zipEntry.getName().toLowerCase().contains("notes")) {
                    extractNotesDetails(br, crestDataBatchJob);
                    numberOfFile++;
                } else if (zipEntry.getName().toLowerCase().contains("non_avail")) {
                    extractNonAvDetails(br, crestDataBatchJob);
                    numberOfFile++;
                } else if (zipEntry.getName().toLowerCase().contains("defendant")) {
                    extractDefendantDetails(br, crestDataBatchJob);
                    numberOfFile++;
                } else if (zipEntry.getName().toLowerCase().contains("hearing")) {
                    extractHearingDetails(br, crestDataBatchJob);
                    numberOfFile++;
                } else if (zipEntry.getName().toLowerCase().contains("links")) {
                    extractLinksDetails(br, crestDataBatchJob);
                    numberOfFile++;
                }
            }
        }
        return numberOfFile;
    }

    private boolean isAllAvalableDatesNull(final CrestNonAvailable crestData) {
        return (crestData.getNonAvailableDateStart() == null) && (crestData.getNonAvailableDateEnd() == null) && (crestData.getNonAvailableDateReason() == null);
    }

    private boolean isAnyAvailableDateNull(final CrestNonAvailable crestData) {
        return (crestData.getNonAvailableDateStart() == null) || (crestData.getNonAvailableDateEnd() == null) || (crestData.getNonAvailableDateReason() == null);
    }

    private boolean validateCaseColumnHeader(final String readLine) {
        LOGGER.info("------Case Header -----" + readLine);
        if ((readLine != null) && CASE_HEADER_LINE.equalsIgnoreCase(readLine)) {
            return true;
        }
        return false;

    }

    private boolean validateDefendantColumnHeader(final String readLine) {
        LOGGER.info("------Defendant Header -----" + readLine);
        if ((readLine != null) && DEFENDANT_HEADER_LINE.equalsIgnoreCase(readLine)) {
            return true;
        }
        return false;

    }

    private boolean validateLinkCaseColumnHeader(final String readLine) {
        LOGGER.info("------LinkCase Header -----" + readLine);
        if ((readLine != null) && LINKCASE_HEADER_LINE.equalsIgnoreCase(readLine)) {
            return true;
        }
        return false;

    }

    private boolean validateNonAvColumnHeader(final String readLine) {
        LOGGER.info("------NonAv Header -----" + readLine);
        if ((readLine != null) && NONAV_HEADER_LINE.equalsIgnoreCase(readLine)) {
            return true;
        }
        return false;

    }

    private boolean validateNoteColumnHeader(final String readLine) {
        LOGGER.info("------Note Header -----" + readLine);
        if ((readLine != null) && NOTE_HEADER_LINE.equalsIgnoreCase(readLine)) {
            return true;
        }
        return false;

    }

    private boolean validatePcmhColumnHeader(final String readLine) {
        LOGGER.info("------PCMH Header -----" + readLine);
        if ((readLine != null) && HEARING_HEADER_LINE.equalsIgnoreCase(readLine)) {
            return true;
        }
        return false;

    }

}
