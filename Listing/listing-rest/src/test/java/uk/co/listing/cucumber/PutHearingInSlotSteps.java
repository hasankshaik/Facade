package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateFormats;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PutHearingInSlotSteps {

    @Given("^hearing \"(.*?)\" exists for Case \"(.*?)\" with lead defendant as \"(.*?)\"$")
    public void exists_for_Case(final String hearingName, final String caseName, final String defendantName) throws Throwable {
        SeleniumConnector.openAndWait("case");
        SeleniumConnector.openAndWait("case");
        SeleniumConnector.waitForTextPresent("Case Crest Number");
        SeleniumConnector.setInputValue("caseCrestNumber", caseName);
        SeleniumConnector.clickAndWait("getCaseDetailsButton");
        SeleniumConnector.waitForTextPresent("Lead Defendant:");
        Assert.assertEquals(defendantName, SeleniumConnector.getValue("leadDefendant"));
        SeleniumConnector.waitForTextPresent("New Hearing Name");
        SeleniumConnector.setInputValue("hearingName", hearingName);
        SeleniumConnector.clickAndWait("createHearing");
    }

    @When("^I go to Planner page$")
    public void i_go_to_Planner_page() throws Throwable {
        SeleniumConnector.openAndWait("schedule");
        SeleniumConnector.waitForTextPresent("Room Name");
    }

    @When("^I select hearing \"(.*?)\" in \"(.*?)\"$")
    public void i_select_hearing_in(final String hearingName, final String unlistedHearingDropDown) throws Throwable {
        SeleniumConnector.openAndWait("schedule");
        SeleniumConnector.waitForTextPresent("Room Name");
        SeleniumConnector.isSelectOptionPresent(unlistedHearingDropDown, hearingName);
        SeleniumConnector.selectFromDropDown(unlistedHearingDropDown, hearingName);
    }

    @When("^I select court room \"(.*?)\" in roomForListing$")
    public void i_select_court_room_in_roomForListing(final String courtRoom) throws Throwable {
        SeleniumConnector.selectFromDropDown("roomForListing", courtRoom);
    }

    @When("^I see court room \"(.*?)\", start date \"(.*?)\" date and slot is empty$")
    public void i_see_court_room_start_date_date_and(final String courtRoom, final String startDate) throws Throwable {
        // As court room was just created, it will be empty.
        SeleniumConnector.waitForTextPresent(courtRoom);
    }

    @When("^Trial Estimate is \"(.*?)\" days$")
    public void trial_Estimate_is_days(final String trialEstimate) throws Throwable {
        Assert.assertEquals(trialEstimate, SeleniumConnector.getValue("trialEstimateId"));
    }

    @When("^I enter current date in \"(.*?)\" for room \"(.*?)\"$")
    public void i_enter_current_date_in(final String dateSelector, final String courtRoom) throws Throwable {
        SeleniumConnector.enterDateInSelector(dateSelector, new Date());
        SeleniumConnector.isSelectOptionPresent("roomForListing", courtRoom);
        SeleniumConnector.selectFromDropDown("roomForListing", courtRoom);
    }

    @Given("^court room \"(.*?)\" exists in courtRooms$")
    public void court_Room_exists_in(final String courtRoomName) throws Throwable {
        SeleniumConnector.openAndWait("admin");
        SeleniumConnector.waitForTextPresent("Room Name:");
        SeleniumConnector.setInputValue("courtRoomInput", courtRoomName);
        SeleniumConnector.clickAndWait("saveCourtRoomButton");
    }

    @Given("^court room \"(.*?)\" is allocated to judge \"(.*?)\" for current date plus (\\d+) days$")
    public void court_room_is_allocated_to_judge_for_current_date_plus_days(final String courtRoomName, final String judgeName, final int plusDays) throws Throwable {
        SeleniumConnector.openAndWait("schedule");
        SeleniumConnector.waitForTextPresent("Room Name");
        SeleniumConnector.isSelectOptionPresent("courtRooms", courtRoomName);
        SeleniumConnector.selectFromDropDown("courtRooms", courtRoomName);
        SeleniumConnector.isSelectOptionPresent("listOfJudgesForAlloc", judgeName);
        SeleniumConnector.selectFromDropDown("listOfJudgesForAlloc", judgeName);
        SeleniumConnector.enterDateInSelector("courtRoomStartDate", new Date());
        final List<Date> dates = DateTimeUtils.getWorkingDaysFromStartDate(new Date(), plusDays);
        SeleniumConnector.enterDateInSelector("courtRoomEndDate", dates.get(dates.size() - 1));
        SeleniumConnector.clickAndWait("allocateJudge");
    }

    @Then("^I can see for court room \"(.*?)\", hearing \"(.*?)\", case \"(.*?)\", defendant \"(.*?)\" in current date plus (\\d+) days$")
    public void i_can_see_for_court_room(final String courtRoom, final String hearing, final String caseName, final String defendant, final int estimateDays) throws Throwable {

        final String gridValue = hearing + " " + caseName + " " + defendant;
        SeleniumConnector.openAndWait("schedule");
        SeleniumConnector.waitForTextPresent(gridValue);

        Date startDay = new Date();
        final Date endDay = DateUtils.addDays(startDay, estimateDays);
        while (!startDay.after(endDay)) {
            if (!DateTimeUtils.isWeekend(startDay)) {
                SeleniumConnector.waitForTextPresent(gridValue);
                assertTrue(gridValue.equals(SeleniumConnector.getTextFromElementBySelector(".//*[@id = '" + courtRoom + "_" + DateFormatUtils.format(startDay, DateFormats.STANDARD.getValue()) + "_"
                        + caseName + "_" + hearing + "_link']", "xpath")));
            }
            final Calendar day = Calendar.getInstance();
            day.setTime(startDay);
            // We check if the day is Sunday to move to the next week
            if (day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                SeleniumConnector.clickAndWait("nextButton");
            }
            startDay = DateUtils.addDays(startDay, 1);
        }
    }

    @Given("^case \"(.*?)\" has hearing \"(.*?)\"$")
    public void case_case_has_hearing(final String caseName, final String hearing) throws Throwable {
        SeleniumConnector.openAndWait("case");
        SeleniumConnector.waitForTextPresent("View Cases");
        SeleniumConnector.setInputValue("caseCrestNumber", caseName);
        SeleniumConnector.clickAndWait("getCaseDetailsButton");
        SeleniumConnector.waitForTextPresent("New Hearing Name");
        SeleniumConnector.setInputValue("hearingName", hearing);
        SeleniumConnector.clickAndWait("createHearing");
        // SeleniumConnector.waitForTextPresent(hearing);
        assertTrue(SeleniumConnector.isTextPresent(hearing));
    }

    @Given("^hearing \"(.*?)\" is booked for case \"(.*?)\" for a current date in room \"(.*?)\"$")
    public void hearing_is_booked_for_case_for_current_date(final String hearingName, final String caseName, final String courtRoom) throws Throwable {
        SeleniumConnector.openAndWait("schedule");
        SeleniumConnector.waitForTextPresent("Room Name");
        SeleniumConnector.isSelectOptionPresent("selectedUnscheduledHearing", hearingName);
        SeleniumConnector.selectFromDropDown("selectedUnscheduledHearing", hearingName);
        SeleniumConnector.enterDateInSelector("hearingStartDate", new Date());
        SeleniumConnector.isSelectOptionPresent("roomForListing", courtRoom);
        SeleniumConnector.selectFromDropDown("roomForListing", courtRoom);
        SeleniumConnector.selectFromDropDown("hearingBookingType", "Provisional");
        SeleniumConnector.clickAndWait("listHearing");
    }

    @Then("^hearing \"(.*?)\" is booked for case \"(.*?)\" for current date in room \"(.*?)\"$")
    public void hearing_is_booked_for_case_for_current_date_in_room(final String hearing, final String caseName, final String courtRoom) throws Throwable {
        final Date startDay = new Date();
        SeleniumConnector.waitForTextPresent(caseName);
        SeleniumConnector.waitForElementClickableBySelector(".//*[@id = '" + courtRoom + "_" + DateFormatUtils.format(startDay, DateFormats.STANDARD.getValue()) + "_" + caseName + "_" + hearing
                + "_link']", "xpath");
        assertTrue(SeleniumConnector.getTextFromElementBySelector(
                ".//*[@id = '" + courtRoom + "_" + DateFormatUtils.format(startDay, DateFormats.STANDARD.getValue()) + "_" + caseName + "_" + hearing + "_link']", "xpath").contains(caseName));
    }

    @Given("^hearing \"(.*?)\" is not booked for case \"(.*?)\" for current date in room \"(.*?)\"$")
    public void hearing_is_not_booked_for_case_for_current_date_in_room(final String hearing, final String caseName, final String courtRoom) throws Throwable {
        assertTrue(SeleniumConnector.isTextNotPresent(caseName));
    }

    @Then("^I see listing error \"(.*?)\"$")
    public void i_see_listing_error(final String errorMessage) throws Throwable {
        SeleniumConnector.waitForTextPresent(errorMessage);
        assertTrue(SeleniumConnector.getTextFromElementBySelector("listHearingMessage", "id").contains(errorMessage));
    }

    @Then("^I can not see for court room \"(.*?)\", hearing \"(.*?)\", case \"(.*?)\", defendant \"(.*?)\" in current date plus (\\d+) days$")
    public void i_can_not_see_for_court_room_hearing_case_defendant_in_current_date_plus_days(final String courtRoom, final String hearingName, final String caseName, final String defendant,
            final int noDays) throws Throwable {
        final Date startDay = new Date();
        assertTrue(SeleniumConnector.isElementAbsent(".//*[@id = '" + courtRoom + "_" + DateFormatUtils.format(startDay, DateFormats.STANDARD.getValue()) + "_" + caseName + "_" + hearingName
                + "_link']", "xpath"));
    }

    @When("^I save the hearing estimate as (\\d+) day$")
    public void i_save_the_hearing_estimate_as_day(final int newEstimate) throws Throwable {
        SeleniumConnector.isSelectOptionPresent("selectedUnscheduledHearing", "Trial-Estimate-1D");
        SeleniumConnector.selectFromDropDown("selectedUnscheduledHearing", "Trial-Estimate-1D");
        SeleniumConnector.selectFromDropDown("selectedUnscheduledHearing", "Trial-Estimate-0D");
        SeleniumConnector.setInputValue("trialEstimateId", String.valueOf(newEstimate));
        SeleniumConnector.clickAndWait("editEstimate");
    }

    @Given("^hearing \"(.*?)\" of estimate (\\d+) days exists for Case \"(.*?)\" with lead defendant as \"(.*?)\"$")
    public void hearing_of_estimate_days_exists_for_Case_with_lead_defendant_as(final String arg1, final int arg2, final String arg3, final String arg4) throws Throwable {
        // Nothing to do here as hearing "Trial-Estimate-0D" for T600 has been
        // setup from import.sql
    }
}
