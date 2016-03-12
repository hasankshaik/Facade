package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Map;

import org.openqa.selenium.Keys;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class EditBookedHearingSteps {
    private final static Date startDate = DateTimeUtils.getEndWorkingDateFromStartDate(new Date(), 1);
    private final static Date nextstartDate = DateTimeUtils.getEndWorkingDateFromStartDate(new Date(), 2);
    private final static Date prevstartDate = DateTimeUtils.getPreviousWorkingDateFromDate(new Date());
    private boolean bExecuted = false;

    private void runOnce() {
        if (!bExecuted) {
            SeleniumConnector.openAndWait("schedule");

            SeleniumConnector.selectFromDropDown("listOfJudgesForAlloc", "UniqueJudge");
            SeleniumConnector.selectFromDropDown("courtRooms", "FirstRoom");
            SeleniumConnector.enterDateInSelector("courtRoomStartDate", startDate);
            SeleniumConnector.enterDateInSelector("courtRoomEndDate", DateTimeUtils.getEndWorkingDateFromStartDate(startDate, 30));
            SeleniumConnector.clickAndWait("allocateJudge");

            SeleniumConnector.openAndWait("schedule");

            SeleniumConnector.selectFromDropDown("listOfJudgesForAlloc", "UniqueJudge");
            SeleniumConnector.selectFromDropDown("courtRooms", "SecondRoom");
            SeleniumConnector.enterDateInSelector("courtRoomStartDate", startDate);
            SeleniumConnector.enterDateInSelector("courtRoomEndDate", DateTimeUtils.getEndWorkingDateFromStartDate(startDate, 30));

            SeleniumConnector.clickAndWait("allocateJudge");
            bExecuted = true;
        }
    }

    @Given("^The CourtRoom is booked for a judge for  a month$")
    public void the_CourtRoom_is_booked_for_a_judge_for_a_month() throws Throwable {
        runOnce();
        SeleniumConnector.openAndWait("schedule");
    }

    @Given("^I can see a \"(.*?)\" for \"(.*?)\" with  type \"(.*?)\" listed in \"(.*?)\" with (\\d+) starting today in planner page$")
    public void i_can_see_a_for_with_type_listed_in_with_starting_today_in_planner_page(final String hearing, final String caseNo, final String bookingType, final String courtRoom,
            final int trialEstimate) throws Throwable {
        SeleniumConnector.waitForTextPresent("Room Name");
        SeleniumConnector.selectFromDropDown("selectedUnscheduledHearing", hearing);
        SeleniumConnector.selectFromDropDown("roomForListing", courtRoom);
        SeleniumConnector.enterDateInSelector("hearingStartDate", startDate);
        SeleniumConnector.selectFromDropDown("hearingBookingType", bookingType);
        SeleniumConnector.clickAndWait("listHearing");
        final String linkId = courtRoom + "_" + DateTimeUtils.formatToStandardPattern(startDate) + "_" + caseNo + "_" + hearing + "_link";

        // Wait for the link to appear in the planner, then attempt to click on
        // the link.
        SeleniumConnector.waitForElementClickableBySelector(linkId, "id");
        SeleniumConnector.clickAndWait(linkId);
    }

    @Given("^I can see and select \"(.*?)\" for \"(.*?)\" with  type \"(.*?)\" listed in \"(.*?)\" with in planner page$")
    public void i_can_see_and_select_for_with_type_listed_in_with_starting_today_in_planner_page(final String hearing, final String caseNo, final String bookingType, final String courtRoom)
            throws Throwable {
        final String linkId = courtRoom + "_" + DateTimeUtils.formatToStandardPattern(nextstartDate) + "_" + caseNo + "_" + hearing + "_link";
        // Wait for the link to appear in the planner, then attempt to click on
        // the link.
        SeleniumConnector.enterDateInSelector("dateInput", nextstartDate);
        SeleniumConnector.clickAndWait("goto");
        SeleniumConnector.waitForElementClickableBySelector(linkId, "id");
        SeleniumConnector.clickAndWait(linkId);
    }

    @When("^I change startDate as next working date , (\\d+) , \"(.*?)\" and \"(.*?)\" of  \"(.*?)\" from Edit Hearing panel and relist$")
    public void i_change_startDate_as_next_working_date_and_of_from_Edit_Hearing_panel_and_relist(final int newTrialEstimate, final String newBookingType, final String anotherCourtRoom,
            final String hearing) throws Throwable {
        SeleniumConnector.selectFromDropDown("scheduledRoomForListing", anotherCourtRoom);
        SeleniumConnector.enterDateInSelector("scheduledHearingStartDate", nextstartDate);
        SeleniumConnector.selectFromDropDown("scheduledHearingBookingType", newBookingType);
        SeleniumConnector.setInputValue("scheduledTrialEstimateId", new Integer(newTrialEstimate).toString());
        SeleniumConnector.setInputValue("scheduledHearingNoteId", "my annotation");
        SeleniumConnector.clickAndWait("updateHearing");
    }

    @Then("^I find \"(.*?)\" for \"(.*?)\" being relisted on \"(.*?)\" with new start date and  (\\d+)$")
    public void i_find_for_being_relisted_on_with_new_start_date_and(final String hearing, final String caseNo, final String anotherCourtRoom, final int newTrialEstimate) throws Throwable {

        final String linkId = anotherCourtRoom + "_" + DateTimeUtils.formatToStandardPattern(nextstartDate) + "_" + caseNo + "_" + hearing + "_link";
        SeleniumConnector.waitForElementClickableBySelector(linkId, "id");
        SeleniumConnector.clickAndWait(linkId);
        SeleniumConnector.waitForElementClickableBySelector("unListHearing", "id");
        SeleniumConnector.clickAndWait("unListHearing");
        final String alertMsg = "Confirm that " + hearing + " starting on " + DateTimeUtils.formatToStandardPattern(nextstartDate) + " should be un-listed";
        SeleniumConnector.waitForTextPresent(alertMsg);
        SeleniumConnector.pressKey("saveButton", Keys.ENTER);
        SeleniumConnector.waitForElementToDisappear("saveButton", "id");
    }

    @When("^I enter \"(.*?)\" in Annotation field$")
    public void i_enter_in_Annotation_field(final String annotation) throws Throwable {
        SeleniumConnector.setInputValue("scheduledHearingNoteId", annotation);
    }

    @Then("^I can see \"(.*?)\" in Annotation field$")
    public void i_can_see_in_Annotation_field(final String annotation) throws Throwable {
        assertTrue(annotation.equals(SeleniumConnector.getValue("scheduledHearingNoteId")));
    }

    @When("^I click \"(.*?)\" button to add Annotation$")
    public void i_click_button_to_add_Annotation(final String updateHearing) throws Throwable {
        SeleniumConnector.clickAndWait(updateHearing);
    }

    @Then("^I can see \"(.*?)\" button is disabled to update Annotation$")
    public void i_can_see_button_is_disabled_to_update_Annotation(final String buttonId) throws Throwable {
        SeleniumConnector.waitForElementAttribute(buttonId, "disabled", "true");
    }

    @When("^I change StartDate to CurrentDate minus one$")
    public void i_change_StartDate_to_CurrentDate_minus_one() throws Throwable {
        SeleniumConnector.enterDateInSelector("scheduledHearingStartDate", prevstartDate);
        SeleniumConnector.clickAndWait("updateHearing");
    }
    
    @When("^I can see following Case Details in Schedule page$")
    public void i_can_see_follo_case_details_in_schedule(Map<String, String> caseDetails) throws Throwable {
    	SeleniumConnector.waitForTextPresent("The Crown Court at");
        SeleniumConnector.waitForTextPresent("Case Detail");
    	for (String key : caseDetails.keySet()) {
    		SeleniumConnector.waitForElementPresent(key);
    		assertTrue(caseDetails.get(key).equals(SeleniumConnector.getValue(key)));
    	}
    }
}
