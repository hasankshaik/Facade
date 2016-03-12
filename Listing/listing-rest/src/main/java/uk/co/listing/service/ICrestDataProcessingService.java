package uk.co.listing.service;

import uk.co.listing.domain.constant.ProcessingStatus;

public interface ICrestDataProcessingService {

    void updateCrestDataBatchJob(Long batchJobId, ProcessingStatus processingStatus, String comments);

    void processCrestDataBatchJob(Long batchJobId);

    void updateUnprocessedTaskAsError(ProcessingStatus processingStatus);

    void processCrestDefendantBatchJob(Long batchJobId);

    void processCrestNotesBatchJob(Long batchJobId);

    void processCrestNonAvailBatchJob(Long batchJobId);

    void processCrestHearingBatchJob(Long batchJobId);

    void processCrestLinkedCaseBatchJob(Long batchJobId);

}
