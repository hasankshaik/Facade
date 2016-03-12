package uk.co.listing.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import uk.co.listing.SeleniumConnector;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoadCrestDataSteps {

    @Given("^case \"(.*?)\" does not exist$")
    public void case_does_not_exist(final String caseNo) throws Throwable {
        SeleniumConnector.openAndWait("case");
        SeleniumConnector.waitForTextPresent("View Cases");
        SeleniumConnector.setInputValue("caseCrestNumber", caseNo);
        SeleniumConnector.clickAndWait("getCaseDetailsButton");
        SeleniumConnector.waitForTextPresent("No case found with the Crest case number ");
        SeleniumConnector.openAndWait("admin");
    }

    @Then("^I see File Upload Status for \"(.*?)\" is \"(.*?)\"$")
    public void i_see_File_Upload_Status_for_is(final String fileName, final String status) throws Throwable {
        int row = 1;
        boolean rowFound = false;
        SeleniumConnector.waitForTextPresent(fileName);
        while (SeleniumConnector.getNumberOfTableBodyRows("processingStatusTable") >= row) {
            final String procStatus = SeleniumConnector.getTextFromTable("processingStatusTable", row, 2);
            if (fileName.equals(SeleniumConnector.getTextFromTable("processingStatusTable", row, 1))
                    && (status.equals(procStatus) || "Awaiting".equals(procStatus) || "Processed".equals(procStatus))) {
                rowFound = true;
                break;
            }
            row++;
        }
        assertTrue(rowFound);
    }

    @When("^I click on \"(.*?)\" till Status is \"(.*?)\" for \"(.*?)\"$")
    public void i_click_on_till_Status_is(final String refreshButton, final String status, final String fileName) throws Throwable {
        int row = 1;
        int clickCount = 10;
        boolean rowFound = false;
        while (SeleniumConnector.getNumberOfTableBodyRows("processingStatusTable") >= row) {
            while (fileName.equals(SeleniumConnector.getTextFromTable("processingStatusTable", row, 1)) && (clickCount > 0)
                    && !status.equals(SeleniumConnector.getTextFromTable("processingStatusTable", row, 2))) {
                // Scrolling is required to make the status button visible. This button
                // moves outside the visible area after many test files have been uploaded.
                SeleniumConnector.waitForElementClickableBySelector("refreshProcessingStatusButton", "id");
                SeleniumConnector.clickAndWait("refreshProcessingStatusButton");
                clickCount--;
            }
            if (fileName.equals(SeleniumConnector.getTextFromTable("processingStatusTable", row, 1)) && status.equals(SeleniumConnector.getTextFromTable("processingStatusTable", row, 2))) {
                rowFound = true;
                break;
            }
            row++;
        }
        assertTrue(rowFound);
    }

    @Given("^File \"(.*?)\" exits and satisfies the condition \"(.*?)\"$")
    public void file_exits_and_satisfies_the_condition(final String fileName, final String condition) throws Throwable {
        /*
         * This step is only for improving readability of the Scenario. Each
         * file is manually created to produce a specific error condition. So
         * there is nothing to verify here. We will verify in the front end
         * during test execution that the specific error message is displayed
         * while processing the file.
         */
    }

    @When("^I enter Upload path for (?:valid|invalid) file \"(.*?)\" and click Upload button$")
    public void i_enter_path_for_file_for_Upload(final String fileName) throws Throwable {
        SeleniumConnector.waitForTextPresent("Upload file");
        final ClassLoader classLoader = getClass().getClassLoader();
        final File testfile = new File(classLoader.getResource("TestFiles/" + fileName).getFile());
        SeleniumConnector.setFileBrowsePath(testfile.getAbsolutePath(), "fileUpload");
        SeleniumConnector.clickAndWait("fileUploadBtn");
    }

    @When("^I wait for the Upload button to get Enabled$")
    public void i_wait_upload_button_to_be_enabled() throws Throwable {
        SeleniumConnector.waitForElementClickableBySelector("fileUploadBtn", "id");
    }

    @Then("^Upload button is disabled$")
    public void upload_button_is_disabled() throws Throwable {
        SeleniumConnector.waitForElementAttribute("fileUploadBtn", "disabled", "true");
    }

    @Then("^I see Upload success message \"(.*?)\"$")
    public void i_see_Upload_success_message(final String successMsg) throws Throwable {
        SeleniumConnector.waitForTextPresent(successMsg);
        assertTrue(successMsg.equals(SeleniumConnector.getTextFromElementBySelector("fileUploadSuccessMessage", "id")));
    }

    @Then("^I see Upload error message \"(.*?)\"$")
    public void i_see_Upload_error_message(final String errorMsg) throws Throwable {
        assertTrue(SeleniumConnector.getTextFromElementBySelector("fileUploadSuccessMessage", "id").contains(errorMsg));
    }

    @Then("^I should see Case \"(.*?)\" is closed$")
    public void i_should_see_Case_closed(final String caseNo) throws Throwable {
        assertTrue("true".equals(SeleniumConnector.getValue("caseClosed")));
    }

    @Then("^I should see case \"(.*?)\"$")
    public void i_should_see_Case_present(final String caseNo) throws Throwable {
        SeleniumConnector.waitForTextPresent("Date of Sending");
        assertTrue(caseNo.equals(SeleniumConnector.getValue("crestCaseNumber")));
    }

    @Then("^Upload button is enabled$")
    public void upload_button_is_enabled() throws Throwable {
        SeleniumConnector.waitForElementClickableBySelector("fileUploadBtn", "id");
    }

    @When("^I should see the case with details \"(.*?)\" , \"(.*?)\" , \"(.*?)\" , \"(.*?)\" , \"(.*?)\" , \"(.*?)\" , \"(.*?)\" , \"(.*?)\" , \"(.*?)\" , \"(.*?)\" , \"(.*?)\" displayed on the page$")
    public void i_should_see_the_case_with_details_displayed_on_the_page(final String caseNo, final String leadDefendant, final String noOfDefendants, final String offence, final String offenceClass,
            final String trialEstimate, final String estimateUnit, final String releaseDecisionStatus, final String ticketingRequirements, final String sendingDate, final String committalDate)
            throws Throwable {
        SeleniumConnector.waitForTextPresent("Date of Committal");
        assertTrue(caseNo.equals(SeleniumConnector.getValue("crestCaseNumber")));
        assertTrue(SeleniumConnector.getValue("leadDefendant").contains(leadDefendant));
        assertTrue(noOfDefendants.equals(SeleniumConnector.getValue("numberOfDefendent")));
        assertTrue(offence.equals(SeleniumConnector.getValue("mostSeriousOffence")));
        assertTrue(trialEstimate.equals(SeleniumConnector.getValue("trialEstimate")));
        assertTrue(estimateUnit.equals(SeleniumConnector.getValue("trialEstimateUnit")));
        assertTrue(offenceClass.equals(SeleniumConnector.getValue("offenceClass")));
        assertTrue(releaseDecisionStatus.equals(SeleniumConnector.getValue("releaseDecisionStatus")));
        assertTrue(ticketingRequirements.equals(SeleniumConnector.getValue("ticketingRequirement")));
        assertTrue(sendingDate.equals(SeleniumConnector.getValue("dateOfSending")));
        assertTrue(committalDate.equals(SeleniumConnector.getValue("dateOfCommittal")));
    }

    @When("^I should see (\\d+) Notes$")
    public void i_should_see_Notes(final int noOfNotes) throws Throwable {
        SeleniumConnector.waitForTextPresent("URN");
        assertTrue(String.valueOf(noOfNotes).equals(String.valueOf(SeleniumConnector.getNumberOfTableBodyRows("caseNotes"))));
    }

    @When("^I should see (\\d+) Linked cases$")
    public void i_should_see_Linked_cases(final int noOfLinkedCases) throws Throwable {
        assertTrue(String.valueOf(noOfLinkedCases).equals(String.valueOf(SeleniumConnector.getNumberOfTableBodyRows("linkedCases"))));
    }

    @When("^I should see (\\d+) Defendants$")
    public void i_should_see_Defendants(final int noOfDefendants) throws Throwable {
        assertTrue(String.valueOf(noOfDefendants).equals(String.valueOf(SeleniumConnector.getNumberOfTableBodyRows("peopleInCase"))));
    }

    @When("^I should see (\\d+) Hearings$")
    public void i_should_see_Hearings(final int noOfHearings) throws Throwable {
        assertTrue(String.valueOf(noOfHearings).equals(String.valueOf(SeleniumConnector.getNumberOfTableBodyRows("hearings"))));
    }

    @When("^I should see Note with Date \"(.*?)\" , Content \"(.*?)\" and Diary date \"(.*?)\"$")
    public void i_should_see_Note_with_Date_Content_and_Diary_date(final String noteCreateDate, final String contents, final String diaryDate) throws Throwable {
        int row = 1;
        boolean rowFound = false;
        while (SeleniumConnector.getNumberOfTableBodyRows("caseNotes") >= row) {
            if (noteCreateDate.equals(SeleniumConnector.getTextFromTable("caseNotes", row, 1)) && contents.equals(SeleniumConnector.getTextFromTable("caseNotes", row, 2))
                    && diaryDate.equals(SeleniumConnector.getTextFromTable("caseNotes", row, 3))) {
                rowFound = true;
                break;
            }
            row++;
        }
        assertTrue(rowFound);
    }

    @When("^I should see following Linked cases$")
    public void i_should_see_linked_cases(final List<String> linkedCases) throws Throwable {
        for (final String expectedCase : linkedCases) {
            assertTrue(SeleniumConnector.isTextPresent(expectedCase));
        }
    }

    @When("^I should see Non Availability data \"(.*?)\" , \"(.*?)\" , \"(.*?)\"$")
    public void i_should_see_non_availability_data(final String startDate, final String endDate, final String reason) throws Throwable {
        int row = 1;
        boolean rowFound = false;
        while (SeleniumConnector.getNumberOfTableBodyRows("nonAvailableDates") >= row) {
            if (reason.equals(SeleniumConnector.getTextFromTable("nonAvailableDates", row, 1)) && startDate.equals(SeleniumConnector.getTextFromTable("nonAvailableDates", row, 2))
                    && endDate.equals(SeleniumConnector.getTextFromTable("nonAvailableDates", row, 3))) {
                rowFound = true;
                break;
            }
            row++;
        }
        assertTrue(rowFound);
    }

    @When("^I should see Defendant with details \"(.*?)\" , \"(.*?)\" , \"(.*?)\" , \"(.*?)\" , \"(.*?)\"$")
    public void i_should_see_defendant_with_details(final String forename1, final String forename2, final String surname, final String urn, final String status) throws Throwable {
        int row = 1;
        boolean rowFound = false;
        while (SeleniumConnector.getNumberOfTableBodyRows("peopleInCase") >= row) {
            if (SeleniumConnector.getTextFromTable("peopleInCase", row, 1).contains(forename1) && SeleniumConnector.getTextFromTable("peopleInCase", row, 1).contains(forename2)
                    && SeleniumConnector.getTextFromTable("peopleInCase", row, 1).contains(surname) && urn.equals(SeleniumConnector.getTextFromTable("peopleInCase", row, 2))
                    && status.equals(SeleniumConnector.getTextFromTable("peopleInCase", row, 3))) {
                rowFound = true;
                break;
            }
            row++;
        }
        assertTrue(rowFound);
    }

    @When("^I should see PCMH Hearing with date \"(.*?)\"$")
    public void i_should_see_pcmh_hearing_with_date(final String startDate) throws Throwable {
        int row = 1;
        boolean rowFound = false;
        final String hearingName = "PCM-" + startDate;
        while (SeleniumConnector.getNumberOfTableBodyRows("hearings") >= row) {
            if (hearingName.equals(SeleniumConnector.getTextFromTable("hearings", row, 1))) {
                rowFound = true;
                break;
            }
            row++;
        }
        assertTrue(rowFound);
    }

    @Given("^Upload file \"(.*?)\" contains data to create valid case \"(.*?)\"$")
    public void upload_file_contains_data_to_create_valid_case(final String fileName, final String caseNo) throws Throwable {
        // The data to create case exists in the file. We are verifying that after
        // the upload, the case is loaded successfully. Hence nothing to do here.
        // This step is only for readability.
    }

    @Given("^File for \"(.*?)\" has a non existant case \"(.*?)\"$")
    public void file_for_has_a_non_existant_case(final String fileName, final String nonExistCase) throws Throwable {
        // The data in the file for defendant, notes, non availability, linked, hearings
        // has a non existent case which we have already verified previously.
        // Hence nothing to do here. This step is only for readability.
    }

    @Then("^I see Processing message \"(.*?)\" in \"(.*?)\" for file \"(.*?)\"$")
    public void i_see_Processing_message_in(final String processingMessage, final String statusTableId, final String fileName) throws Throwable {
        final String processStatusXpath = ".//*[@id='" + statusTableId + "']/tbody//td[text()='" + fileName + "']/../td[3]";
        assertTrue(StringUtils.containsAny(SeleniumConnector.getTextFromElementBySelector(processStatusXpath, "xpath"), processingMessage));
    }

    @Then("^I should see (\\d+) \"(.*?)\"$")
    public void i_should_see(final int expectedCount, final String itemToLook) throws Throwable {
        int actualCount;
        switch (itemToLook) {
        case "Defendants":
            actualCount = SeleniumConnector.isElementAbsent("peopleInCase", "id") ? 0 : SeleniumConnector.getNumberOfTableBodyRows("peopleInCase");
            break;
        case "Notes":
            actualCount = SeleniumConnector.isElementAbsent("caseNotes", "id") ? 0 : SeleniumConnector.getNumberOfTableBodyRows("caseNotes");
            break;
        case "Non Availability Dates":
            actualCount = SeleniumConnector.isElementAbsent("nonAvailableDates", "id") ? 0 : SeleniumConnector.getNumberOfTableBodyRows("nonAvailableDates");
            break;
        case "Hearings":
            actualCount = SeleniumConnector.isElementAbsent("hearings", "id") ? 0 : SeleniumConnector.getNumberOfTableBodyRows("hearings");
            break;
        case "Linked Cases":
            actualCount = SeleniumConnector.isElementAbsent("linkedCases", "id") ? 0 : SeleniumConnector.getNumberOfTableBodyRows("linkedCases");
            break;
        default:
            actualCount = 1000;
        }
        assertEquals("Expected count: " + expectedCount + " Actual count : " + actualCount, expectedCount, actualCount);
    }
}