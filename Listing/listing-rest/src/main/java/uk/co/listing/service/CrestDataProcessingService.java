package uk.co.listing.service;

import static uk.co.listing.rest.message.MessageBundler.getMessage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.co.listing.dao.CaseRelatedDao;
import uk.co.listing.dao.CrestDataBatchJobDao;
import uk.co.listing.domain.CaseRelated;
import uk.co.listing.domain.CrestData;
import uk.co.listing.domain.CrestDataBatchJob;
import uk.co.listing.domain.CrestDefendant;
import uk.co.listing.domain.CrestLinkedCase;
import uk.co.listing.domain.CrestNonAvailable;
import uk.co.listing.domain.CrestNote;
import uk.co.listing.domain.CrestPcmh;
import uk.co.listing.domain.constant.ProcessingStatus;
import uk.co.listing.exceptions.CcsException;
import uk.co.listing.rest.response.CourtCaseWeb;
import uk.co.listing.rest.response.HearingWeb;
import uk.co.listing.rest.response.NotAvailableDatesWeb;
import uk.co.listing.utils.CrestDataHelper;

@Service("BatchJobProcessor")
public class CrestDataProcessingService implements ICrestDataProcessingService {

    private static final Logger LOGGER = Logger.getLogger(CrestDataProcessingService.class);
    @Autowired(required = true)
    private CrestDataBatchJobDao crestDataBatchJobDao;

    @Autowired(required = true)
    private CaseRelatedDao caseRelatedDao;

    @Autowired(required = true)
    private ICourtCaseService courtCaseService;

    @Autowired
    private IHearingService hearingService;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void processCrestDataBatchJob(final Long batchJobId) {
        updateCases(batchJobId);
        insertCases(batchJobId);
        deleteCases(batchJobId);
        LOGGER.info("Crest Data batch Job processed successfully");
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void processCrestDefendantBatchJob(final Long batchJobId) {
        validateDefendant(batchJobId);
        deleteDefendant();
        insertDefendant(batchJobId);
        LOGGER.info("Defendant job processed successfully");
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void processCrestHearingBatchJob(final Long batchJobId) {
        validateHearing(batchJobId);
        deleteHearing();
        insertHearing(batchJobId);
        LOGGER.info("Hearing job processed successfully");
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void processCrestLinkedCaseBatchJob(final Long batchJobId) {
        validateLinkedCase(batchJobId);
        deleteLinkedCase();
        insertLinkedCase(batchJobId);
        LOGGER.info("Linked case processed successfully");

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void processCrestNonAvailBatchJob(final Long batchJobId) {
        validateNonAvail(batchJobId);
        deleteNonAvail();
        insertNonAvail(batchJobId);
        LOGGER.info("Non available date processed successfully");
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void processCrestNotesBatchJob(final Long batchJobId) {
        validateNotes(batchJobId);
        deleteNotes();
        insertNotes(batchJobId);
        LOGGER.info("Notes job processed successfully");
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void updateCrestDataBatchJob(final Long batchJobId, final ProcessingStatus processingStatus, final String comments) {
        for (int i = 0; i < 5; i++) {
            final CrestDataBatchJob crestDataBatchJob = crestDataBatchJobDao.getCrestDataBatchJobById(batchJobId);
            if (crestDataBatchJob != null) {
                crestDataBatchJob.setProcessingState(processingStatus);
                crestDataBatchJob.setComments(comments);
                crestDataBatchJobDao.save(crestDataBatchJob);
                break;
            } else {
                if (i == 4) {
                    throw new CcsException(getMessage("PROCESSING_STOPPED_UNEXPECTEDILY"));
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (final InterruptedException e) {
                    LOGGER.error(e);
                }
            }
        }
        LOGGER.info("Crest Data Batch Job updated");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateUnprocessedTaskAsError(final ProcessingStatus processingStatus) {
        final List<CrestDataBatchJob> crestDataBatchJobListProcessing = crestDataBatchJobDao.findAllTaskByProcessingStatus(processingStatus);
        for (final CrestDataBatchJob crestDataBatchJob : crestDataBatchJobListProcessing) {
            crestDataBatchJob.setProcessingState(ProcessingStatus.ERROR);
            crestDataBatchJob.setComments(getMessage("INTERNAL_SERVER_ERROR"));
            crestDataBatchJobDao.save(crestDataBatchJob);
        }
        LOGGER.info("Unprocessed tasks are marked with status error");
    }

    private void createAndListHearing(final HearingWeb hearingWeb, final String centerName) {
        final HearingWeb hearingWebSaved = hearingService.saveUpdateHearing(hearingWeb);
        hearingService.autoScheduleHearing(hearingWebSaved, centerName);
    }

    private void createCourtCase(final CourtCaseWeb courtCaseWeb) {
        try {
            courtCaseService.saveUpdateCaseRelated(courtCaseWeb);
        } catch (final Exception e) {
            LOGGER.error(e);
            throw new CcsException(courtCaseWeb.getCrestCaseNumber() + ":" + e.getMessage());
        }
    }

    private void deleteCases(final Long batchJobId) {
        final List<CaseRelated> dataForCompletion = crestDataBatchJobDao.getCreateDataForCompletion(batchJobId);
        for (final CaseRelated caseRelated : dataForCompletion) {
            caseRelated.setCaseClosed(true);
            caseRelatedDao.save(caseRelated);
        }
    }

    private void deleteDefendant() {
        crestDataBatchJobDao.deleteCrestdefendant();
    }

    private void deleteHearing() {
        crestDataBatchJobDao.deleteCrestHearing();

    }

    private void deleteLinkedCase() {
        crestDataBatchJobDao.deleteCrestLinkedCase();

    }

    private void deleteNonAvail() {
        crestDataBatchJobDao.deleteCrestNonAvail();
    }

    private void deleteNotes() {
        crestDataBatchJobDao.deleteCrestNotes();
    }

    private void insertCases(final Long batchJobId) {
        final List<CrestData> dataForInsert = crestDataBatchJobDao.getCreateDataForInsert(batchJobId);
        final List<CourtCaseWeb> listCourtCaseWeb = CrestDataHelper.createCourtCaseWeb(dataForInsert);
        for (final CourtCaseWeb courtCaseWeb : listCourtCaseWeb) {
            createCourtCase(courtCaseWeb);
        }
    }

    private void insertDefendant(final Long batchJobId) {
        final List<CrestDefendant> defendantData = crestDataBatchJobDao.getCrestdefendant(batchJobId);
        for (final CrestDefendant crestDef : defendantData) {
            final CourtCaseWeb courtCaseWeb = CrestDataHelper.caseDefendantToWeb(crestDef);
            courtCaseService.saveUpdateDefendantForCase(courtCaseWeb);
        }
    }

    private void insertHearing(final Long batchJobId) {
        final List<CrestPcmh> hearingData = crestDataBatchJobDao.getCrestHearing(batchJobId);
        for (final CrestPcmh crestPcmh : hearingData) {
            final HearingWeb hearingWeb = CrestDataHelper.hearingToWeb(crestPcmh);
            createAndListHearing(hearingWeb, "Birmingham");
        }
    }

    private void insertLinkedCase(final Long batchJobId) {
        final List<CrestLinkedCase> linkedCaseData = crestDataBatchJobDao.getCrestLinkedCases(batchJobId);
        for (final CrestLinkedCase crestLinkedcase : linkedCaseData) {
            final CourtCaseWeb courtCaseWeb = CrestDataHelper.caseLinkedCaseToWeb(crestLinkedcase);
            courtCaseService.saveUpdateLinkedCaseForCase(courtCaseWeb);
        }

    }

    private void insertNonAvail(final Long batchJobId) {
        final List<CrestNonAvailable> nonAvailData = crestDataBatchJobDao.getCrestNonAvail(batchJobId);
        for (final CrestNonAvailable crestNonAvail : nonAvailData) {
            final NotAvailableDatesWeb courtCaseWeb = CrestDataHelper.caseNonAvailToWeb(crestNonAvail);
            courtCaseService.saveUpdateNotAvailableDateForCase(courtCaseWeb);
        }

    }

    private void insertNotes(final Long batchJobId) {
        final List<CrestNote> defendantData = crestDataBatchJobDao.getCrestNotes(batchJobId);
        for (final CrestNote crestNote : defendantData) {
            final CourtCaseWeb courtCaseWeb = CrestDataHelper.caseNoteToWeb(crestNote);
            courtCaseService.saveUpdateCaseNoteForCase(courtCaseWeb);
        }

    }

    private void updateCases(final Long batchJobId) {
        final List<CrestData> dataForUpdate = crestDataBatchJobDao.getCreateDataForUpdate(batchJobId);
        final List<CourtCaseWeb> listCourtCaseWebUpdate = CrestDataHelper.createCourtCaseWeb(dataForUpdate);
        for (final CourtCaseWeb courtCaseWeb : listCourtCaseWebUpdate) {
            try {
                final CaseRelated caseRelated = caseRelatedDao.findCaseByCrestCaseNumber(courtCaseWeb.getCrestCaseNumber());
                caseRelatedDao.save(caseRelated);
                createCourtCase(courtCaseWeb);
            } catch (final Exception e) {
                LOGGER.error(e);
                throw new CcsException(courtCaseWeb.getCrestCaseNumber() + ":" + e.getMessage());
            }
        }
    }

    private void validateDefendant(final Long batchJobId) {
        final List<CrestDefendant> invalidData = crestDataBatchJobDao.getInvalidCrestdefendant(batchJobId);
        if (!invalidData.isEmpty()) {
            throw new CcsException(getMessage("INVALID_DEFENDANTS_FILE"));
        }
    }

    private void validateHearing(final Long batchJobId) {
        final List<CrestPcmh> invalidData = crestDataBatchJobDao.getInvalidCrestHearing(batchJobId);
        if (!invalidData.isEmpty()) {
            throw new CcsException(getMessage("INVALID_PCMH_FILE"));
        }

    }

    private void validateLinkedCase(final Long batchJobId) {
        final List<CrestLinkedCase> invalidData = crestDataBatchJobDao.getInvalidCrestLinkedcases(batchJobId);
        if (!invalidData.isEmpty()) {
            throw new CcsException(getMessage("INVALID_LINKED_CASE_FILE"));
        }

    }

    private void validateNonAvail(final Long batchJobId) {
        final List<CrestNonAvailable> invalidData = crestDataBatchJobDao.getInvalidCrestNonAvail(batchJobId);
        if (!invalidData.isEmpty()) {
            throw new CcsException(getMessage("INVALID_NON_AVAILABILITY_FILE"));
        }
    }

    private void validateNotes(final Long batchJobId) {
        final List<CrestNote> invalidData = crestDataBatchJobDao.getInvalidCrestNotes(batchJobId);
        if (!invalidData.isEmpty()) {
            throw new CcsException(getMessage("INVALID_NOTES_FILE"));
        }
    }

}
