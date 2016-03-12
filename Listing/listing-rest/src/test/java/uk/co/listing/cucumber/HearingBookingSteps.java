package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class HearingBookingSteps {
    public static Log log = LogFactory.getLog(HearingBookingSteps.class);

    @Given("I am logged in as ListingOfficer")
    public void i_can_see_hearings() throws Throwable {
        SeleniumConnector.openAndWait("schedule");
    }

    @When("^I select \"(.*?)\" from \"(.*?)\" element$")
    public void i_select_from_element(final String elementNameToSelected, final String elementId) throws Throwable {
        SeleniumConnector.isSelectOptionPresent(elementId, elementNameToSelected);
        SeleniumConnector.selectFromDropDown(elementId, elementNameToSelected);
    }

    @When("^I enter current Date in \"(.*?)\" element$")
    public void i_enter_current_Date_in(final String element) throws Throwable {
        SeleniumConnector.enterDateInSelector(element, new Date());
    }

    @When("^I click on \"(.*?)\" button$")
    public void i_click_on_button(final String id) throws Throwable {
        SeleniumConnector.clickAndWait(id);
    }

    @Then("^I can see \"(.*?)\" in Planner on CurrentDate$")
    public void i_can_see_in_Planner_on_CurrentDate(final String arg1) throws Throwable {
        assertTrue(SeleniumConnector.isTextPresent(arg1));
    }

    @Given("^I can see \"(.*?)\" in Planner page$")
    public void i_can_see(final String arg1) throws Throwable {
        assertTrue(SeleniumConnector.isTextPresent(arg1));
    }

    @Given("^hearing \"(.*?)\" is Booked in Planner$")
    public void hearing_is_Booked(final String arg1) throws Throwable {
        assertTrue(SeleniumConnector.isTextPresent(arg1));
    }

    @Given("^\"(.*?)\" has \"(.*?)\"$")
    public void has(final String arg1, final String arg2) throws Throwable {
        assertTrue(SeleniumConnector.isTextPresent(arg1));
        assertTrue(SeleniumConnector.isTextPresent(arg2));
    }

    @Given("^hearing\"(.*?)\" is Booked$")
    public void is_Booked(final String arg1) throws Throwable {
        assertTrue(SeleniumConnector.isTextPresent(arg1));
    }

    @Given("^I see \"(.*?)\" drop down element is cleared$")
    public void i_see_element_is_cleared(final String dropdownId) throws Throwable {
        assertTrue("".equals(SeleniumConnector.getSelectedOption(dropdownId)));
    }

    @When("^I click on \"(.*?)\" link for \"(.*?)\"$")
    public void i_click_on_link_for(final String arg1, final String hearingName) throws Throwable {
        final String linkId = "UniqueRoom_" + DateTimeUtils.formatToStandardPattern(new Date()) + "_T900_" + hearingName + "_link";
        SeleniumConnector.clickAndWait(linkId);
    }

    @Then("^I see \"(.*?)\" element for \"(.*?)\"$")
    public void i_see_element_for(final String arg1, final String arg2) throws Throwable {
        assertTrue(SeleniumConnector.isTextPresent(arg2));
    }

    @When("^I do not enter hearingBookingType$")
    public void i_do_not_enter_hearingBookingType() throws Throwable {
        // nothing to do here
    }

    @Then("^I cannot click on  \"(.*?)\" button$")
    public void i_cannot_click_on_button(final String id) throws Throwable {
        assertTrue(!SeleniumConnector.isEnabled(id));
    }

    @Given("^I select CaseView page$")
    public void i_am_in_View_case_page() throws Throwable {
        SeleniumConnector.openAndWait("case");
    }

    @When("^I enter case \"(.*?)\" in \"(.*?)\" element$")
    public void i_enter_case_in_element(final String arg1, final String arg2) throws Throwable {
        SeleniumConnector.setInputValue(arg2, arg1);
    }

    @Then("^I can see \"(.*?)\" in Hearing Booking Type element$")
    public void i_can_see_in_Hearing_Booking_Type_element(final String element) throws Throwable {
        assertTrue(SeleniumConnector.isTextPresent(element));
    }

    @Then("^I cannot see text Booking Type: \"(.*?)\" in the \"(.*?)\" element$")
    public void i_cannot_see_text_Booking_Type_in_the_element(final String text, final String id) throws Throwable {
        Assert.assertNotEquals("Booking Type: "+text, SeleniumConnector.getTextFromElementBySelector(id, "id"));
    }

    @Given("^Court Room \"(.*?)\" has no judge allocated for date \"(.*?)\"$")
    public void court_Room_has_no_judge_allocated_for_date(final String arg1, final String arg2) throws Throwable {
        // As this room is not used for any another tests and since we do not allocate any judge for any days
        // in this room, nothing to be done here.
    }

    @Given("^I try to list (?:Unlisted|Listed) hearing \"(.*?)\" for date \"(.*?)\" in Court Room \"(.*?)\" as type \"(.*?)\"$")
    public void i_try_to_list_Unlisted_hearing_for_date_in_Court_Room_as_type(final String hearingName, final String startDay, final String courtRoom, final String bookType) throws Throwable {
        SeleniumConnector.waitForTextPresent("List Hearing");
        SeleniumConnector.isSelectOptionPresent("selectedUnscheduledHearing", hearingName);
        SeleniumConnector.selectFromDropDown("selectedUnscheduledHearing", hearingName);
        SeleniumConnector.waitForElementClickable("hearingStartDate");
        SeleniumConnector.enterDateInSelector("hearingStartDate", CommonSteps.getDate(startDay));
        SeleniumConnector.isSelectOptionPresent("roomForListing", courtRoom);
        SeleniumConnector.selectFromDropDown("roomForListing", courtRoom);
        SeleniumConnector.isSelectOptionPresent("hearingBookingType", bookType);
        SeleniumConnector.selectFromDropDown("hearingBookingType", bookType);
        SeleniumConnector.clickAndWait("listHearing");
    }

    @Then("^I see listing error message \"(.*?)\"$")
    public void i_see_listing_error_message(final String expMsg) throws Throwable {
        SeleniumConnector.waitForTextPresent(expMsg);
    }

    @When("^I click on Listed hearing \"(.*?)\"$")
    public void i_click_on_Listed_hearing(final String hearingName) throws Throwable {
        final String hearingLink = ".//a[contains(@id, '" + hearingName + "')]";
        SeleniumConnector.clickAndWaitBySelector(hearingLink, "xpath");
    }

    @When("^I enter Date \"(.*?)\" with pattern \\(dd/MM/yyyy\\) in \"(.*?)\" element$")
    public void i_enter_Date_with_pattern_dd_MM_yyyy_in_element(String dateString, String hearingStartElem) throws Throwable {
        final String[] dateArr = dateString.split("/");
        SeleniumConnector.enterDateInSelector(hearingStartElem, dateArr[0], dateArr[1], dateArr[2]);
    }
}