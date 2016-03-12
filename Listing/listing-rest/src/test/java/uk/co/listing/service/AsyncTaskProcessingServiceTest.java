package uk.co.listing.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import uk.co.listing.BaseMockingUnitTest;
import uk.co.listing.domain.constant.ProcessingStatus;
import uk.co.listing.exceptions.CcsException;

public class AsyncTaskProcessingServiceTest extends BaseMockingUnitTest {

    @Mock
    private CrestDataProcessingService crestDataProcessingServiceMock;

    @InjectMocks
    AsyncTaskProcessingService asyncTaskProcessingService;

    @Test
    public void testOnload() {
        Mockito.doNothing().when(crestDataProcessingServiceMock).updateUnprocessedTaskAsError(ProcessingStatus.PROCESSING);
        Mockito.doNothing().when(crestDataProcessingServiceMock).updateUnprocessedTaskAsError(ProcessingStatus.AWAITING);
        asyncTaskProcessingService.Onload();
        verify(crestDataProcessingServiceMock, times(2)).updateUnprocessedTaskAsError(Mockito.any(ProcessingStatus.class));
    }

    @Test
    public void testprocess() {
        final Long batchJobId = 10l;
        Mockito.doNothing().when(crestDataProcessingServiceMock).updateCrestDataBatchJob(batchJobId, ProcessingStatus.PROCESSING, "File is being processed ");
        Mockito.doNothing().when(crestDataProcessingServiceMock).processCrestDataBatchJob(batchJobId);
        Mockito.doNothing().when(crestDataProcessingServiceMock).processCrestDefendantBatchJob(batchJobId);
        Mockito.doNothing().when(crestDataProcessingServiceMock).processCrestNotesBatchJob(batchJobId);
        asyncTaskProcessingService.process(batchJobId);
        verify(crestDataProcessingServiceMock, times(1)).updateCrestDataBatchJob(batchJobId, ProcessingStatus.PROCESSING, "File is being processed ");
        verify(crestDataProcessingServiceMock, times(1)).processCrestDataBatchJob(batchJobId);
        verify(crestDataProcessingServiceMock, times(1)).processCrestDefendantBatchJob(batchJobId);
        verify(crestDataProcessingServiceMock, times(1)).processCrestNotesBatchJob(batchJobId);
    }

    @Test
    public void testprocessError() {
        final Long batchJobId = 10l;
        Mockito.doThrow(new CcsException("Test")).when(crestDataProcessingServiceMock).updateCrestDataBatchJob(batchJobId, ProcessingStatus.PROCESSING, "File is being processed ");
        asyncTaskProcessingService.process(batchJobId);
        verify(crestDataProcessingServiceMock, times(0)).processCrestDataBatchJob(batchJobId);
    }

}