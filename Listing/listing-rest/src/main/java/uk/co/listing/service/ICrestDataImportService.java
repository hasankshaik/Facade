package uk.co.listing.service;

import java.io.InputStream;
import java.util.List;

import uk.co.listing.rest.response.CrestDataBatchJobWeb;

public interface ICrestDataImportService {

    public void importCrestData(InputStream uploadedInputStream, String fileName);

    public List<CrestDataBatchJobWeb> getCrestDataProcessingStatus();

}
