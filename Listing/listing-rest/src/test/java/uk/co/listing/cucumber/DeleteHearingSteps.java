package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import uk.co.listing.SeleniumConnector;
import uk.co.listing.utils.DateFormats;
import uk.co.listing.utils.DateTimeUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DeleteHearingSteps {
    public static String CASENAME = "T700";
    public static String ROOMNAME = "UniqueRoom";
    public static String JUDGENAME = "UniqueJudge";

    // Book a hearing in a court room from start date until the trial estimate
    // of hearing
    public void bookHearing(final String hearingName, final Date startDate, final String courtRoom) {
        String dateString;
        String elementId;
        SeleniumConnector.openAndWait("schedule");
        SeleniumConnector.waitForTextPresent("Room Name");
        SeleniumConnector.selectFromDropDown("selectedUnscheduledHearing", hearingName);
        SeleniumConnector.enterDateInSelector("hearingStartDate", startDate);
        SeleniumConnector.selectFromDropDown("roomForListing", courtRoom);
        SeleniumConnector.selectFromDropDown("hearingBookingType", "Provisional");
        SeleniumConnector.clickAndWait("listHearing");
        dateString = DateFormatUtils.format(startDate, DateFormats.STANDARD.getValue());
        elementId = ".//*[@id = '" + courtRoom + "_" + dateString + "_" + CASENAME + "_" + hearingName + "_link']";
        SeleniumConnector.waitForElementClickableBySelector(elementId, "xpath");

        // Go back to the Case View page and open the case with our expected
        // hearings.
        SeleniumConnector.openAndWait("case");
        SeleniumConnector.setInputValue("caseCrestNumber", CASENAME);
        SeleniumConnector.clickAndWait("getCaseDetailsButton");
        SeleniumConnector.waitForTextPresent("New Hearing Name:");
        SeleniumConnector.waitForElementAttribute("crestCaseNumber", "value", CASENAME);
    }

    @Given("^I am logged in as Listing Officer$")
    public void i_am_logged_in_as_Listing_Officer() throws Throwable {
        // Nothing to be done here till we implement login
    }

    @Given("^I am on the View Case page$")
    public void i_am_on_the_View_Case_page() throws Throwable {
        SeleniumConnector.openAndWait("case");
        // SeleniumConnector.waitForTextPresent("New Hearing Name:");
    }

    @Given("^I am in CrestCaseNumber \"(.*?)\"$")
    public void i_am_in_CrestCaseNumber(final String crestCaseNumber) throws Throwable {
        SeleniumConnector.setInputValue("caseCrestNumber", crestCaseNumber);
        SeleniumConnector.clickAndWait("getCaseDetailsButton");
        SeleniumConnector.waitForElementAttribute("crestCaseNumber", "value", crestCaseNumber);
    }

    @Given("^CrestCaseNumber has Trial Hearing \"(.*?)\"$")
    public void crestcasenumber_has_Trial_Hearing(final String hearingName) throws Throwable {
        SeleniumConnector.waitForTextPresent(hearingName);
    }

    @Given("^Trial Hearing \"(.*?)\" is not in planner$")
    public void trial_Hearing_is_not_in_planner(final String hearingName) throws Throwable {
        SeleniumConnector.openAndWait("schedule");
        SeleniumConnector.waitForTextPresent("Room Name");
        assertTrue(!SeleniumConnector.isElementAbsent(".//*[@id='selectedUnscheduledHearing']/option[text()='" + hearingName + "']", "xpath"));

        // Go back to the Case View page and open the case with our expected
        // hearings.
        i_am_on_the_View_Case_page();
        i_am_in_CrestCaseNumber(CASENAME);
    }

    @When("^I click Delete button for hearing \"(.*?)\"$")
    public void i_click_Delete_button_for_hearing(final String hearingName) throws Throwable {
        SeleniumConnector.waitForTextPresent(hearingName);
        SeleniumConnector.clickAndWait("removeHearing_"+hearingName);
    }

    @Then("^I should see \"(.*?)\"$")
    public void i_should_see(final String deleteMessage) throws Throwable {
        SeleniumConnector.waitForTextPresent("Delete hearing");
        SeleniumConnector.waitForTextPresent(deleteMessage);
    }

    @When("^I click Yes$")
    public void i_click_Yes() throws Throwable {
        SeleniumConnector.clickAndWait("saveButton");
    }

    @Then("^I can( not)? see Trial Hearing \"(.*?)\" in View Case page$")
    public void i_can_not_see_Trial_Hearing_in_View_Case_page(final String canSee, final String hearingName) throws Throwable {
        if (canSee == null) {
            // I can see
            SeleniumConnector.waitForTextPresent(hearingName);
        } else if (canSee.equals(" not")) {
            // I can not see
            SeleniumConnector.waitForElementToDisappear(".//*[@id='hearings']//td[text()='" + hearingName + "']", "xpath");
            assertTrue(SeleniumConnector.isTextNotPresent(hearingName));
        }
    }

    @When("^I click No$")
    public void i_click_No() throws Throwable {
        SeleniumConnector.clickAndWait("cancelButton");
    }

    @Given("^Trial Hearing \"(.*?)\" is in planner starting on current date$")
    public void trial_Hearing_is_in_planner_starting_on_current_date(final String hearingName) throws Throwable {
        // Allocate hearing to a court room for current date.
        bookHearing(hearingName, new Date(), ROOMNAME);
    }

    @Then("^I should see message \"(.*?)\"current date\"(.*?)\"$")
    public void i_should_see_message_current_date(final String messageFirstPart, final String messageSecondPart) throws Throwable {
        final String deleteMessage = messageFirstPart + DateFormatUtils.format(new Date(), DateFormats.STANDARD.getValue()) + messageSecondPart;
        i_should_see(deleteMessage);
    }

    @Then("^I can( not)? see Trial Hearing \"(.*?)\" on Planner page for current date$")
    public void i_can_not_see_Trial_Hearing_on_Planner_page_for_current_date(final String canSee, final String hearingName) throws Throwable {
        SeleniumConnector.openAndWait("schedule");
        SeleniumConnector.waitForTextPresent("Room Name");
        SeleniumConnector.enterDateInSelector("dateInput", new Date());
        // SeleniumConnector.waitForElementAttribute(ROOMNAME + "_" +
        // DateTimeUtils.formatToStandardPattern(new Date()) + "_" + JUDGENAME,
        // "text", JUDGENAME);
        final String linkId = ROOMNAME + "_" + DateTimeUtils.formatToStandardPattern(new Date()) + "_" + CASENAME + "_" + hearingName + "_link";
        if (canSee == null) {
            // I can see
            SeleniumConnector.waitForElementClickableBySelector(linkId, "id");
        } else if (canSee.equals(" not")) {
            // I can not see
            SeleniumConnector.waitForElementToDisappear(linkId, "id");
        }
    }

}
