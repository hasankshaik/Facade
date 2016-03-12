package uk.co.listing.cucumber;

import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.co.listing.SeleniumConnector;
import cucumber.api.java.After;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class EditHearingFromCasePageSteps {
    public static Log log = LogFactory.getLog(EditHearingFromCasePageSteps.class);

    @Then("^I can see a \"(.*?)\" hearing in the \"(.*?)\" hearing$")
    public void i_can_see_a_hearing(final String arg1, final String arg2) {
        SeleniumConnector.waitForTextPresent("Booked Trial");
        final String text1 = SeleniumConnector.getTextFromElementBySelector("hearingBookingStatus_"+arg2, "id");
        assertTrue(arg1.equals(text1));
    }

    @Then("^I can see \"(.*?)\" for the trial estimation in the \"(.*?)\" hearing$")
    public void i_can_see_for_the_trial_estimation(final String arg1, final String arg2) {
        SeleniumConnector.waitForTextPresent("Booked Trial");
        String text1 = SeleniumConnector.getTextFromElementBySelector("hearingTrialEstimateSelected_"+arg2, "id");
        if(StringUtils.isBlank(text1)) {
            text1 = SeleniumConnector.getNullableValue("hearingTrialEstimateSelected_"+arg2);  
        }

        assertTrue(arg1.equals(text1));

    }

    @Then("^I can see \"(.*?)\" for the hearing annotation in the \"(.*?)\" hearing$")
    public void i_can_see_for_the_hearing_annotation(final String arg1, final String arg2) throws Throwable {
        final String text1 = SeleniumConnector.getNullableValue("hearingNoteSelected_"+arg2);
        assertTrue(arg1.equals(text1));
    }

    @When("^I modify \"(.*?)\" to \"(.*?)\" in the \"(.*?)\" hearing$")
    public void i_modify_to(final String arg1, final String arg2, final String arg3) {
        if (arg1.equals("Booking Type")) {
            SeleniumConnector.selectFromDropDown("hearingBookingTypeSelected_" + arg3, arg2);
        } else if (arg1.equals("Hearing Estimation")) {
            SeleniumConnector.setInputValue("hearingTrialEstimateSelected_" + arg3, arg2);
        } else if (arg1.equals("Annotation")) {
            SeleniumConnector.setInputValue("hearingNoteSelected_" + arg3, arg2);
        }
    }

    @Then("^I can add \"(.*?)\" to Annotation in the \"(.*?)\" hearing$")
    public void i_can_add_to(final String annotation, final String arg2) throws Throwable {
        SeleniumConnector.setInputValue("hearingNoteSelected_" + arg2, annotation);
    }

    @When("^I click set in the \"(.*?)\" hearing$")
    public void i_click_set(final String arg1) {
        SeleniumConnector.clickAndWait("editHearing_" + arg1);
    }

    @When("^I reload the page$")
    public void i_reload_the_page() {
        SeleniumConnector.reloadPage();
    }

    @When("^I go to View Case page$")
    public void i_go_to_view_case_page() {
        SeleniumConnector.openAndWait("case");
        SeleniumConnector.waitForTextPresent("Case Crest Number");
    }

    @Then("^I can not modify \"(.*?)\" in the \"(.*?)\" hearing$")
    public void i_can_not_modify(final String arg1, final String arg2) {
        SeleniumConnector.getTextFromElementBySelector(arg1 + "_" + arg2, "id");
    }

    @Then("^I can see \"(.*?)\" for the Booking Type in the \"(.*?)\" hearing$")
    public void i_can_see_for_the_Booking_Type(final String arg1, final String arg2) {
        SeleniumConnector.waitForTextPresent("Booked Trial");
        SeleniumConnector.waitForTextPresent("Plea and Case Management");
        final String text1 = SeleniumConnector.getSelectedOption("hearingBookingTypeSelected_"+arg2);
        assertTrue(arg1.equals(text1));
    }

    @Then("^I can see \"(.*?)\" message$")
    public void i_can_see_message(final String arg1) {
        SeleniumConnector.waitForTextPresent(arg1);
    }

    @When("^View case page is loaded successfully$")
    public void view_case_page_is_loaded_successfully() throws Throwable {
        SeleniumConnector.waitForTextPresent("Case Crest Number");
    }

    @After("@CCS-383")
    public void afterScenario() throws Throwable {
        i_modify_to("Hearing Estimation", "10", "Trial");
        i_click_set("Trial");
        i_modify_to("Booking Type", "Confirmed", "Booked Trial");
        i_click_set("Booked Trial");
    }
}