package uk.co.listing.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.openqa.selenium.Keys;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RemovingHearingFromSlotSteps {
    String linkId = "";

    @Given("^The CourtRoom is booked for judge for  Future$")
    public void the_CourtRoom_is_booked_for_judge_for_today() throws Throwable {
        SeleniumConnector.openAndWait("schedule");
    }

    @Given("^I can see \"(.*?)\" hearing \"(.*?)\" of \"(.*?)\" for \"(.*?)\" in Hearing Information$")
    public void i_can_see_hearing_of_with_for_in_Hearing_Information(final String Case, final String Hearing, final String BookingType, final String CourtRoom) throws Throwable {
        SeleniumConnector.waitForTextPresent("Room Name");
        SeleniumConnector.selectFromDropDown("selectedUnscheduledHearing", Hearing);
        SeleniumConnector.selectFromDropDown("roomForListing", CourtRoom);
        SeleniumConnector.enterDateInSelector("hearingStartDate", new Date());
        SeleniumConnector.selectFromDropDown("hearingBookingType", BookingType);
        SeleniumConnector.clickAndWait("listHearing");
        linkId = CourtRoom + "_" + DateTimeUtils.formatToStandardPattern(new Date()) + "_" + Case + "_" + Hearing + "_link";
        SeleniumConnector.clickAndWait(linkId);

    }

    @When("^I unlist \"(.*?)\" from Hearing Information$")
    public void i_unlist_from_Hearing_Information(final String hearingName) throws Throwable {
        SeleniumConnector.getTextFromElementBySelector("hearingInfo", "id");
        SeleniumConnector.pressKey("unListHearing", Keys.ENTER);
    }

    @Then("^I see confirmation message for \"(.*?)\"$")
    public void i_see_confirmation_message_for(final String hearingName) throws Throwable {
        final String alertMsg = "Confirm that " + hearingName + " starting on " + DateTimeUtils.formatToStandardPattern(new Date()) + " should be un-listed";
        SeleniumConnector.waitForTextPresent(alertMsg);
    }

    @Then("^\"(.*?)\" is not present in planner page and in not booked$")
    public void is_not_present_in_planner_page_and_in_not_booked(final String arg1) throws Throwable {
        assertTrue(SeleniumConnector.isTextNotPresent(linkId));
    }

    @Then("^I can see \"(.*?)\" in \"(.*?)\" element$")
    public void i_can_see_in_element(final String unlistedHearingName, final String selectedUnscheduledHearingId) throws Throwable {
        assertTrue(SeleniumConnector.isSelectOptionPresent(selectedUnscheduledHearingId, unlistedHearingName));
    }

    @Then("^I see \"(.*?)\" in HearingInformation element$")
    public void i_see_in_HearingInformation_element(final String hearingName) throws Throwable {
        assertEquals(hearingName, SeleniumConnector.getTextFromElementBySelector("hearingInfo", "id"));
    }
}
