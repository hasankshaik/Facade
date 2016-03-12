package uk.co.listing.service;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static uk.co.listing.rest.message.MessageBundler.getMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import uk.co.listing.BaseMockingUnitTest;
import uk.co.listing.dao.CrestDataBatchJobDao;
import uk.co.listing.domain.CrestData;
import uk.co.listing.domain.CrestDataBatchJob;
import uk.co.listing.domain.constant.ProcessingStatus;
import uk.co.listing.exceptions.CcsException;
import uk.co.listing.rest.response.CrestDataBatchJobWeb;

public class CrestDataImportServiceTest extends BaseMockingUnitTest {

    @InjectMocks
    CrestDataImportService crestDataImportService;

    @Mock
    AsyncTaskProcessingService asyncTaskProcessingService;

    @Mock
    CrestDataBatchJobDao crestDataBatchJobDaoMock;

    private static final Logger LOGGER = Logger.getLogger(CrestDataImportServiceTest.class);

    @Test
    public void testUploadFileInvalidName() throws FileNotFoundException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/405-CREST-DATA-INVALID-WRONG-CC-CODE_14.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains(getMessage("FILE_NAME_MUST_START_WITH_COURT_CENTER_CODE")));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileInvalidNameInnerFile() throws FileNotFoundException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404.CREST-DATA-INVALID-INNER-CC-CODE.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains(getMessage("FILE_NAME_MUST_START_WITH_COURT_CENTER_CODE")));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileInvalidSurname() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404.CREST-DATA-INVALID-SURNAME_11C_NPre-BC_STATUS_09_Pre-27.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains(getMessage("CARDINALITY_ERROR_DEFENDANT_DETAILS")));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileBlankHeader() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404CREST-DATA-INVALID-BLANK-HEADER-LINE-CASE_9.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains("Invalid file"));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileInvalidNumberOfFiles() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404.CREST-DATA-INVALID-NO-OF-INNER-FILES.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains("Invalid file"));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileInvalidOffenceClass() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404.CREST-DATA-INVALID-TICKET-TYPE-WRONG.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains("File upload fail"));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileEmptyCaseFile() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404.CREST-DATA-INVALID-DATA-CASE-EMPTY.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains("File upload fail"));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileEmptyDefendantFile() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404.CREST-DATA-INVALID-DATA-DEFENDANT-EMPTY.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains("File upload fail"));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileEmptyNoteFile() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404.CREST-DATA-INVALID-DATA-NOTES-EMPTY.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains("File upload fail"));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileEmptyNonAvFile() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404.CREST-DATA-INVALID-DATA-HEARINGS-EMPTY.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains("File upload fail"));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileEmptyPCMHFile() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404.CREST-DATA-INVALID-DATA-NON-AVAIL-EMPTY.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains("File upload fail"));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileEmptyLinkedCaseFile() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404.CREST-DATA-INVALID-DATA-LINKED-CASES-EMPTY.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains("File upload fail"));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }


    @Test
    public void testUploadFiledCaseOnlyDefendantsFile() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404.CREST-DATA-INVALID-DATA-ONLY-DEFENDANTS.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                Mockito.doNothing().doThrow(new CcsException("File upload fail")).when(crestDataBatchJobDaoMock).save(Mockito.any(CrestData.class));
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains("File upload fail"));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }


    @Test
    public void testUploadFileEmptyDataFile() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404CREST-DATA-INVALID-EMPTY_CASE_10.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains("Invalid file"));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }


    @Test
    public void testUploadFileMissingHeader() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404CREST-DATA-INVALID-MISSING-HEADER-LINE-CASE_12.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains("Invalid file"));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileInvalidDefendantHeader() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404CREST-DATA-INVALID-HEADER-FIELD-MISSING-DEFENDANT_29.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains("Invalid file"));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileInvalidNoteHeader() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404CREST-DATA-INVALID-HEADER-FIELD-MISSING-NOTES.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains("Invalid file"));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileInvalidNonAvHeader() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404CREST-DATA-INVALID-HEADER-FIELD-MISSING-NON-AVAIL.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains("Invalid file"));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileInvalidPCMHHeader() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404CREST-DATA-INVALID-HEADER-FIELD-MISSING-HEARINGS.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains("Invalid file"));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileInvalidLinkedCaseHeader() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404CREST-DATA-INVALID-HEADER-FIELD-MISSING-LINKED-CASES.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains("Invalid file"));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileInvlidNonAvailableDate() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404.CREST-DATA-INVALID-NON-AVAIL-START-NULL.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains(getMessage("CARDINALITY_ERROR_NON_AVAILABLE_DATES")));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileInvlidCaseNoteNoNoteDate() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404.CREST-DATA-INVALID-NOTE_TYPE_12_Pre-NOTE_PRINT_15A_Pre-NOTE_DATE_15B_NPre-NOTE_15C_Pre_25.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains(getMessage("CARDINALITY_ERROR_CASE_NOTE")));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileInvlidCaseNoteNoNote() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404.CREST-DATA-INVALID-NOTE_TYPE_12_Pre-NOTE_PRINT_15A_Pre-NOTE_DATE_15B_Pre-NOTE_15C_NPre_24.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains(getMessage("CARDINALITY_ERROR_CASE_NOTE")));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadFileInvlidListingOfficer() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404-CREST-DATA-INVALID-UNIT-MISSING-CASE.zip").getFile());
        if (file.exists()) {
            try {
                final InputStream uploadedInputStream = new FileInputStream(file);
                crestDataImportService.importCrestData(uploadedInputStream, file.getName());
                fail();
            } catch (final Exception e) {
                assertTrue(e.getMessage().contains(getMessage("CARDINALITY_ERROR_TRAIL_ESTIMATE_LISTING_OFFICER")));
            }
        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void testUploadValidFile() throws FileNotFoundException {

        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("TestFiles/404.CrestData-Valid.zip").getFile());
        if (file.exists()) {

            final InputStream uploadedInputStream = new FileInputStream(file);
            crestDataImportService.importCrestData(uploadedInputStream, file.getName());

        } else {
            LOGGER.error("Please porvide file");
        }

    }

    @Test
    public void getCrestDataProcessingStatusTest () {
        final CrestDataBatchJob crestDataBatchJob1 = new CrestDataBatchJob();
        crestDataBatchJob1.setComments("comments");
        crestDataBatchJob1.setFileName("fileName1");
        crestDataBatchJob1.setProcessingState(ProcessingStatus.AWAITING);

        final CrestDataBatchJob crestDataBatchJob2 = new CrestDataBatchJob();
        crestDataBatchJob2.setComments("comments");
        crestDataBatchJob2.setFileName("fileName2");
        crestDataBatchJob2.setProcessingState(ProcessingStatus.AWAITING);
        final List<CrestDataBatchJob> crestDataBatchJobList = new ArrayList<CrestDataBatchJob>();
        crestDataBatchJobList.add(crestDataBatchJob1);
        crestDataBatchJobList.add(crestDataBatchJob2);

        Mockito.when(crestDataBatchJobDaoMock.getProcessingStatus()).thenReturn(crestDataBatchJobList);
        final List<CrestDataBatchJobWeb> crestDataBatchList = crestDataImportService.getCrestDataProcessingStatus();
        verify(crestDataBatchJobDaoMock, times(1)).getProcessingStatus();

        assertTrue(crestDataBatchList.size()==2);

    }

}