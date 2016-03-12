package uk.co.listing.service;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import uk.co.listing.domain.constant.ProcessingStatus;

@Service("AsyncTaskProcessingService")
public class AsyncTaskProcessingService implements IAsyncTaskProcessingService {

    private static final Logger LOGGER = Logger.getLogger(AsyncTaskProcessingService.class);

    @Autowired
    private ICrestDataProcessingService crestDataProcessingService;

    @PostConstruct
    public void Onload() {
        try {
            crestDataProcessingService.updateUnprocessedTaskAsError(ProcessingStatus.PROCESSING);
            crestDataProcessingService.updateUnprocessedTaskAsError(ProcessingStatus.AWAITING);
        } catch (final Exception e) {
            LOGGER.error("AsyncTaskProcessingService onload ,", e);
        }
    }

    @Async
    @Override
    public void process(final Long batchJobId) {
        try {
            crestDataProcessingService.updateCrestDataBatchJob(batchJobId, ProcessingStatus.PROCESSING, "File is being processed ");
            crestDataProcessingService.processCrestDataBatchJob(batchJobId);
            crestDataProcessingService.processCrestDefendantBatchJob(batchJobId);
            crestDataProcessingService.processCrestNotesBatchJob(batchJobId);
            crestDataProcessingService.processCrestNonAvailBatchJob(batchJobId);
            crestDataProcessingService.processCrestHearingBatchJob(batchJobId);
            crestDataProcessingService.processCrestLinkedCaseBatchJob(batchJobId);
            crestDataProcessingService.updateCrestDataBatchJob(batchJobId, ProcessingStatus.PROCESSED, "Success");
        } catch (final Exception e) {
            LOGGER.error(e);
            crestDataProcessingService.updateCrestDataBatchJob(batchJobId, ProcessingStatus.ERROR, "File processing failed with following reason : " + e.getMessage());
        }
        LOGGER.info("Job has been processed successfully " + batchJobId);
    }

}
