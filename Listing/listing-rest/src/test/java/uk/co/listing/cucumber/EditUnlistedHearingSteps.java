package uk.co.listing.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import uk.co.listing.SeleniumConnector;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class EditUnlistedHearingSteps {

    @When("^and I input \"(.*?)\" in  Trial Estimate inputbox \"(.*?)\"$")
    public void and_I_input_in_Trial_Estimate_inputbox(String estimate, String inputBox) throws Throwable {
        SeleniumConnector.setInputValue(inputBox, estimate);
    }

    @Then("^I see text \"(.*?)\"$")
    public void i_see(String trialEstimate) throws Throwable {
        SeleniumConnector.waitForTextPresent(trialEstimate);
    }

    @Then("^EditEstimate button \"(.*?)\" is disabled$")
    public void editEstimateIsDisabled(String editEstimateButton) throws Throwable {
        assertFalse(SeleniumConnector.isEnabled(editEstimateButton));
    }
    
    @Given("^PCM hearing \"(.*?)\" exists for case \"(.*?)\"$")
    public void pcm_hearing_exists_for_case(String hearingName, String caseName) throws Throwable {
        SeleniumConnector.openAndWait("case");
        SeleniumConnector.waitForTextPresent("Case Crest Number");
        SeleniumConnector.setInputValue("caseCrestNumber", caseName);
        SeleniumConnector.clickAndWait("getCaseDetailsButton");
        SeleniumConnector.waitForTextPresent("Lead Defendant:");
        assertEquals(caseName, SeleniumConnector.getValue("crestCaseNumber"));
        SeleniumConnector.waitForTextPresent(hearingName);
    }

    @When("^I click on Unscheduled Hearings drop down$")
    public void i_click_on_Unscheduled_Hearings_drop_down() throws Throwable {
        SeleniumConnector.waitForElementClickable("selectedUnscheduledHearing");
        SeleniumConnector.clickAndWait("selectedUnscheduledHearing");
    }

    @Then("^I should not see PCM hearing \"(.*?)\" in the Unscheduled Hearing drop down$")
    public void i_should_not_see_PCM_hearing_in_the_Unscheduled_Hearing_drop_down(String pcmHearingName) throws Throwable {
        assertTrue(SeleniumConnector.isElementAbsent(".//*[@id='selectedUnscheduledHearing']/option[text()='" + pcmHearingName + "']", "xpath"));
    }
}
